//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
//
// See the NOTICE file(s) distributed with this work for additional
// information regarding copyright ownership.
//
// This program and the accompanying materials are made available
// under the terms of the MIT License which is available at
// https://opensource.org/licenses/MIT
//
// SPDX-License-Identifier: MIT
//////////////////////////////////////////////////////////////////////////////

package org.eclipse.escet.cif.simulator.compiler;

import static org.apache.commons.lang3.StringEscapeUtils.escapeJava;
import static org.eclipse.escet.cif.common.CifTextUtils.exprToStr;
import static org.eclipse.escet.cif.common.CifTextUtils.typeToStr;
import static org.eclipse.escet.cif.common.CifTypeUtils.checkTypeCompat;
import static org.eclipse.escet.cif.common.CifTypeUtils.getLowerBound;
import static org.eclipse.escet.cif.common.CifTypeUtils.getUpperBound;
import static org.eclipse.escet.cif.common.CifTypeUtils.normalizeType;
import static org.eclipse.escet.cif.simulator.compiler.ExprCodeGenerator.gencodeExpr;
import static org.eclipse.escet.cif.simulator.compiler.TypeCodeGenerator.gencodeType;
import static org.eclipse.escet.common.java.Lists.last;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.escet.cif.common.CifAddressableUtils;
import org.eclipse.escet.cif.common.CifEvalException;
import org.eclipse.escet.cif.common.CifEvalUtils;
import org.eclipse.escet.cif.common.RangeCompat;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.expressions.ContVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.FieldExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ProjectionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TupleExpression;
import org.eclipse.escet.cif.metamodel.cif.functions.FunctionParameter;
import org.eclipse.escet.cif.metamodel.cif.functions.InternalFunction;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.DictType;
import org.eclipse.escet.cif.metamodel.cif.types.Field;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.cif.metamodel.cif.types.ListType;
import org.eclipse.escet.cif.metamodel.cif.types.SetType;
import org.eclipse.escet.cif.metamodel.cif.types.TupleType;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.java.Assert;

/** Assignment code generator. */
public class AssignmentCodeGenerator {
    /** Constructor for the {@link AssignmentCodeGenerator} class. */
    private AssignmentCodeGenerator() {
        // Static class.
    }

    /**
     * Generate Java code for the given (multi-)assignment.
     *
     * @param addr The addressable of the (multi-)assignment.
     * @param value The value of the (multi-)assignment.
     * @param aut The automaton, for assignments on edges. {@code null} for assignments in functions.
     * @param c The code box to which to write the code.
     * @param ctxt The compiler context to use.
     * @param state The name of the state variable in the context where the generated code is used. Must be {@code null}
     *     for functions, and the source state variable name for edges of automata.
     */
    public static void gencodeAssignment(Expression addr, Expression value, Automaton aut, CodeBox c,
            CifCompilerContext ctxt, String state)
    {
        // Paranoia check.
        Assert.check((aut == null) == (state == null));

        // Get core assignments.
        List<CoreAssignment> cores = list();
        AtomicInteger counter = new AtomicInteger();
        getCoreAssignments(addr, value, aut, c, ctxt, state, cores, counter);

        // Generate code for each core assignment.
        int count = counter.get();
        Assert.check(count >= 1);

        // Generate local scope, to ensure uniqueness of the 'rslt*' variables.
        if (count > 1) {
            c.add("{");
            c.indent();
        }

        // Generate code for the declaration of the result variables.
        for (CoreAssignment core: cores) {
            core.count = count;
            core.genPreCode();
        }

        // Generate code for each core assignment.
        for (CoreAssignment core: cores) {
            core.genCode();
        }

        // Generate code for the assignment of the actual addressable
        // variables, using the result variables.
        for (CoreAssignment core: cores) {
            core.genPostCode();
        }

        // Close the local scope of the result variables, if any.
        if (count > 1) {
            c.dedent();
            c.add("}");
        }
    }

    /**
     * Get the {@link CoreAssignment core assignments} for the given addressable and value.
     *
     * @param addr The addressable.
     * @param value The value.
     * @param aut The automaton, for assignments on edges. {@code null} for assignments in functions.
     * @param c The code box to which to write the code.
     * @param ctxt The compiler context to use.
     * @param state The name of the state variable in the context where the generated code is used. Must be {@code null}
     *     for functions, and the source state variable name for edges of automata.
     * @param cores The core assignments constructed so far. Is modified in-place.
     * @param counter Counter to use to generate unique numbers for the result variables of each of the addressable
     *     variables. Is modified in-place.
     */
    private static void getCoreAssignments(Expression addr, Expression value, Automaton aut, CodeBox c,
            CifCompilerContext ctxt, String state, List<CoreAssignment> cores, AtomicInteger counter)
    {
        // If both sides are tuples, pair-wise continue recursively.
        if (addr instanceof TupleExpression && value instanceof TupleExpression) {
            TupleExpression taddr = (TupleExpression)addr;
            TupleExpression tvalue = (TupleExpression)value;
            int size = taddr.getFields().size();
            for (int i = 0; i < size; i++) {
                getCoreAssignments(taddr.getFields().get(i), tvalue.getFields().get(i), aut, c, ctxt, state, cores,
                        counter);
            }
            return;
        }

        // One or both of the sides are not tuples. We found a core assignment.
        CoreAssignment asgn = new CoreAssignment(addr, value, aut, c, ctxt, state, counter);
        cores.add(asgn);
    }

    /** A core assignment. Either the addressable, or the value, or both are not a tuple. */
    private static class CoreAssignment {
        /** The addressable. */
        private final Expression addr;

        /** The value. */
        private final Expression value;

        /** The automaton, for assignments on edges. {@code null} for assignments in functions. */
        private final Automaton aut;

        /** The code box to which to write the code. */
        private final CodeBox c;

        /** The compiler context to use. */
        private final CifCompilerContext ctxt;

        /**
         * The name of the state variable in the context where the generated code is used. Must be {@code null} for
         * functions, and the source state variable name for edges of automata.
         */
        private final String state;

        /**
         * Mapping from addressable variable reference expression to the unique numbers to use for their result
         * variables.
         */
        private final Map<Expression, Integer> addrRsltNrMap;

        /**
         * The number of addressable variables assigned in the (multi-)assignment of which this core assignment is a
         * part. Is {@code -1} until set to a useful value.
         */
        public int count = -1;

        /**
         * Constructor for the {@link CoreAssignment} class.
         *
         * @param addr The addressable.
         * @param value The value.
         * @param aut The automaton, for assignments on edges. {@code null} for assignments in functions.
         * @param c The code box to which to write the code.
         * @param ctxt The compiler context to use.
         * @param state The name of the state variable in the context where the generated code is used. Must be
         *     {@code null} for functions, and the source state variable name for edges of automata.
         * @param counter Counter to use to generate unique numbers for the result variables of each of the addressable
         *     variables. Is modified in-place.
         */
        public CoreAssignment(Expression addr, Expression value, Automaton aut, CodeBox c, CifCompilerContext ctxt,
                String state, AtomicInteger counter)
        {
            this.addr = addr;
            this.value = value;
            this.aut = aut;
            this.c = c;
            this.ctxt = ctxt;
            this.state = state;
            addrRsltNrMap = map();

            // Fill 'addrRsltNrMap'.
            for (Expression varRef: CifAddressableUtils.getRefExprs(addr)) {
                addrRsltNrMap.put(varRef, counter.getAndIncrement());
            }
        }

        /** Generate pre-processing code for the core assignment. */
        public void genPreCode() {
            // If only one assignment, no need for pre-processing.
            Assert.check(count > 0);
            if (count == 1) {
                return;
            }

            // Generate fields in which to store the results.
            for (Entry<Expression, Integer> entry: addrRsltNrMap.entrySet()) {
                Expression addrRef = entry.getKey();
                int rsltIdx = entry.getValue();
                c.add("%s rslt%d;", gencodeType(addrRef.getType(), ctxt), rsltIdx);
            }
        }

        /** Generate post-processing code for the core assignment. */
        public void genPostCode() {
            // If only one assignment, no need for post-processing.
            Assert.check(count > 0);
            if (count == 1) {
                return;
            }

            // Generate code to assign actual state variables.
            for (Entry<Expression, Integer> entry: addrRsltNrMap.entrySet()) {
                Expression addrRef = entry.getKey();
                int rsltIdx = entry.getValue();
                c.add("%s = rslt%d;", gencodeAddrVarRef(addrRef), rsltIdx);
            }
        }

        /** Generate actual assignment code for the core assignment. */
        public void genCode() {
            // Start of 'try'.
            c.add("try {");
            c.indent();

            // Special case, and general case.
            if (addr instanceof DiscVariableExpression || addr instanceof ContVariableExpression) {
                // Special case for very simple addressables (no tuples, no
                // projections).
                String rhsCode = gencodeExpr(value, ctxt, "source");
                gencodeAddrVarAsgn(addr, rhsCode);

                // Generate type range bound check if needed, on entire value
                // of the variable, as the variable as a whole is assigned.
                gencodeTypeRangeBoundCheck(addr, gencodeAddrVarRef(addr), value.getType(), addr.getType(), 0);
            } else {
                // General case.

                // Tuples and/or projections. Create a local scope, and
                // evaluate the rhs.
                c.add("{");
                c.indent();
                c.add("%s rhs = %s;", gencodeType(value.getType(), ctxt), gencodeExpr(value, ctxt, state));
                gencodeComplex(addr, "rhs", value.getType());
                c.dedent();
                c.add("}");
            }

            // End of 'try'.
            c.dedent();
            c.add("} catch (CifSimulatorException e) {");
            c.indent();
            c.add("throw new CifSimulatorException(\"Execution of assignment \\\"%s := %s\\\" failed.\", e, %s);",
                    escapeJava(exprToStr(addr)), escapeJava(exprToStr(value)), state);
            c.dedent();
            c.add("}");
        }

        /**
         * Generate Java code for the given complex assignment (with tuple and/or projection addressables).
         *
         * @param addr The (part of the) addressable for which to generate the code.
         * @param rhsRefCode The Java code fragment to refer to the rhs value for the (part of the) addressable.
         * @param rhsType The type of the rhs value for the (part of the) addressable.
         */
        private void gencodeComplex(Expression addr, String rhsRefCode, CifType rhsType) {
            // Unfold tuples.
            if (addr instanceof TupleExpression) {
                TupleExpression taddr = (TupleExpression)addr;
                TupleType type = (TupleType)normalizeType(taddr.getType());
                for (int i = 0; i < taddr.getFields().size(); i++) {
                    Expression subAddr = taddr.getFields().get(i);
                    CifType subType = type.getFields().get(i).getType();
                    String newRhs = rhsRefCode + "." + ctxt.getTupleTypeFieldFieldName(type, i);
                    gencodeComplex(subAddr, newRhs, subType);
                }
                return;
            }

            // Special case for very simple addressables (no projections).
            if (addr instanceof DiscVariableExpression || addr instanceof ContVariableExpression) {
                // Generate actual assignment.
                gencodeAddrVarAsgn(addr, rhsRefCode);

                // Generate type range bound check if needed, on entire value
                // of the variable, as the variable as a whole is assigned.
                gencodeTypeRangeBoundCheck(addr, gencodeAddrVarRef(addr), rhsType, addr.getType(), 0);

                // Done with special case.
                return;
            }

            // Projected addressable. Create a local scope to unsure that we
            // can use the same local variable names for all projected
            // addressables of this assignment.
            c.add("{");
            c.indent();

            // Get projections, in order from closest to the addressable to
            // farthest from the addressable.
            List<ProjectionExpression> projs;
            projs = CifAddressableUtils.collectProjs(addr);

            // Get the normalized types of the projected children.
            List<CifType> childTypes = listc(projs.size());
            for (ProjectionExpression proj: projs) {
                CifType nctype = normalizeType(proj.getChild().getType());
                childTypes.add(nctype);
            }

            // Generate code for the (calculation of) projection indices.
            List<String> idxCodes = listc(projs.size());
            boolean anyIdxVars = false;
            for (int i = 0; i < projs.size(); i++) {
                ProjectionExpression proj = projs.get(i);
                CifType nctype = childTypes.get(i);

                if (nctype instanceof ListType) {
                    // Generate code to compute index value, and use the local
                    // variable whenever we need it.
                    c.add("int idx%d = %s;", i, gencodeExpr(proj.getIndex(), ctxt, state));
                    anyIdxVars = true;
                    idxCodes.add("idx" + i);
                } else if (nctype instanceof DictType) {
                    // Generate code to compute key value, and use the local
                    // variable whenever we need it.
                    CifType keyType = ((DictType)nctype).getKeyType();
                    c.add("%s key%d = %s;", gencodeType(keyType, ctxt), i, gencodeExpr(proj.getIndex(), ctxt, state));
                    anyIdxVars = true;
                    idxCodes.add("key" + i);
                } else if (nctype instanceof TupleType) {
                    // Statically compute index field name.
                    if (proj.getIndex() instanceof FieldExpression) {
                        Field field = ((FieldExpression)proj.getIndex()).getField();
                        idxCodes.add(ctxt.getTupleTypeFieldFieldName(field));
                    } else {
                        // Get field index for tuple index projection. Index
                        // is valid: type checker already checked it.
                        int idx;
                        try {
                            idx = (Integer)CifEvalUtils.eval(proj.getIndex(), false);
                        } catch (CifEvalException e) {
                            // Should never fail: type checker already
                            // evaluated this.
                            throw new RuntimeException(e);
                        }
                        TupleType tupleType = (TupleType)nctype;
                        idxCodes.add(ctxt.getTupleTypeFieldFieldName(tupleType, idx));
                    }
                } else {
                    String msg = "Unexpected addr proj child type: " + nctype;
                    throw new RuntimeException(msg);
                }
            }
            if (anyIdxVars) {
                c.add();
            }

            // Generate code for the retrieval of the variable from the state.
            Expression varRef = CifAddressableUtils.stripProjs(addr);
            c.add("%s part0 = %s;", gencodeType(varRef.getType(), ctxt), gencodeAddrVarRef(varRef));

            // Generate code for the retrieval of the parts of the projected
            // variable.
            for (int i = 0; i < projs.size(); i++) {
                ProjectionExpression proj = projs.get(i);
                CifType nctype = childTypes.get(i);
                String idxCode = idxCodes.get(i);
                boolean last = (i == projs.size() - 1);
                String rsltTypeCode = gencodeType(proj.getType(), ctxt);

                if (nctype instanceof ListType) {
                    // Use 'project' method for index normalization and bound
                    // checking. Do this for all projections, including the
                    // last one, to make sure the index is not out of bounds.
                    c.add("%s part%d = project(part%d, %s);", rsltTypeCode, i + 1, i, idxCode);
                } else if (nctype instanceof DictType) {
                    // Use 'project' method for key existence checking. Don't
                    // do this for the last projection, as that index does not
                    // yet have to exist (it is assigned).
                    if (last) {
                        continue;
                    }
                    c.add("%s part%d = project(part%d, %s);", rsltTypeCode, i + 1, i, idxCode);
                } else if (nctype instanceof TupleType) {
                    // Don't retrieve the field for the last projection, as
                    // we know it exists, and we don't need the old value.
                    if (last) {
                        continue;
                    }
                    c.add("%s part%d = part%d.%s;", rsltTypeCode, i + 1, i, idxCode);
                } else {
                    String msg = "Unexpected addr proj child type: " + nctype;
                    throw new RuntimeException(msg);
                }
            }
            c.add();

            // Generate code for updating the target state (or result
            // variable), in the reverse order of retrieving the parts.
            for (int i = projs.size() - 1; i >= 0; i--) {
                CifType nctype = childTypes.get(i);
                String idxCode = idxCodes.get(i);
                String newValueCode = (i == projs.size() - 1) ? rhsRefCode : "part" + (i + 1);

                if (nctype instanceof ListType) {
                    c.add("part%d = modify(part%d, %s, %s);", i, i, idxCode, newValueCode);
                } else if (nctype instanceof DictType) {
                    c.add("part%d = modify(part%d, %s, %s);", i, i, idxCode, newValueCode);
                } else if (nctype instanceof TupleType) {
                    c.add("part%d = part%d.copy();", i, i);
                    c.add("part%d.%s = %s;", i, idxCode, newValueCode);
                } else {
                    String msg = "Unexpected addr proj child type: " + nctype;
                    throw new RuntimeException(msg);
                }
            }

            gencodeAddrVarAsgn(varRef, "part0");

            // Generate type range bound check if needed, on value assigned to
            // part of the variable.
            gencodeTypeRangeBoundCheck(varRef, rhsRefCode, rhsType, last(projs).getType(), 0);

            // Close the local scope.
            c.dedent();
            c.add("}");
        }

        /**
         * Generates code for the assignment of a new value to an addressable variable (or result variable).
         *
         * @param addr The addressable variable reference expression.
         * @param rhsCode The right hand side code (the new value code).
         */
        private void gencodeAddrVarAsgn(Expression addr, String rhsCode) {
            if (count == 1) {
                // One addressable variable: direct assignment.
                c.add("%s = %s;", gencodeAddrVarRef(addr), rhsCode);
            } else {
                // Multiple addressable variables: assign result variable.
                int rsltIdx = addrRsltNrMap.get(addr);
                c.add("rslt%d = %s;", rsltIdx, rhsCode);
            }
        }

        /**
         * Generate Java expression code to reference an addressable variable.
         *
         * @param addr The addressable variable reference expression.
         * @return The Java expression code to reference the addressable variable.
         */
        private String gencodeAddrVarRef(Expression addr) {
            if (addr instanceof DiscVariableExpression) {
                DiscVariableExpression daddr = (DiscVariableExpression)addr;
                DiscVariable var = daddr.getVariable();
                if (aut != null) {
                    // Discrete variable in an automaton.
                    return fmt("target.%s.%s", ctxt.getAutSubStateFieldName(aut), ctxt.getDiscVarFieldName(var));
                } else {
                    EObject parent = var.eContainer();
                    if (parent instanceof FunctionParameter) {
                        // Function parameter.
                        return ctxt.getFuncParamMethodParamName(var);
                    } else if (parent instanceof InternalFunction) {
                        // Local variable of a function.
                        return ctxt.getFuncLocalVarName(var);
                    } else {
                        String msg = "Unknown addr var in func: " + var;
                        throw new RuntimeException(msg);
                    }
                }
            } else if (addr instanceof ContVariableExpression) {
                ContVariableExpression caddr = (ContVariableExpression)addr;
                ContVariable var = caddr.getVariable();
                return fmt("target.%s.%s", ctxt.getAutSubStateFieldName(aut), ctxt.getContVarFieldName(var));
            } else {
                throw new RuntimeException("Unknown addr var: " + addr);
            }
        }

        /**
         * Generates type bounds range checking code, if applicable.
         *
         * <p>
         * Only for integer and list types with bounds, do we perform range checking here. However, if the type of the
         * value is entirely contained in the type of the addressable, there is no need to check anything.
         * </p>
         *
         * <p>
         * For instance, assume we have assignment:
         *
         * <pre>(x[0], (y, z)) := a</pre>
         *
         * Then we have three core assignments:
         *
         * <pre>
         * x[0] := a[0]
         * y := a[1][0]
         * z := a[1][1]
         * </pre>
         *
         * For the first core assignment, we have:
         * <ul>
         * <li>{@code a[0]} as {@code valueRefTxt}</li>
         * <li>the type of {@code a[0]} as {@code valueType}</li>
         * <li>the type of {@code x[0]} as {@code addrType}</li>
         * </ul>
         * Assuming {@code x[0]} is of type {@code list int[0..2]}, we will check all elements of {@code a[0]}, but only
         * if the type of {@code a[0]} is not entirely contained in the type of {@code x[0]}.
         * </p>
         *
         * @param addr A reference to the addressable variable that is (partly) assigned.
         * @param valueRefTxt Java code to use as textual reference to (a part of) the value of the addressable. This is
         *     the value that is to be checked.
         * @param valueType The type of the value that is to be checked.
         * @param addrType The type of (the part of) the addressable that is to be assigned the new value indicated by
         *     {@code valueRefTxt}.
         * @param level The level, as the number of containers (excluding tuples) unfolded so far.
         */
        private void gencodeTypeRangeBoundCheck(Expression addr, String valueRefTxt, CifType valueType,
                CifType addrType, int level)
        {
            // If entirely contained, no need for checking.
            boolean contained = checkTypeCompat(addrType, valueType, RangeCompat.CONTAINED);
            if (contained) {
                return;
            }

            // Paranoia checking, must have overlap.
            boolean overlap = checkTypeCompat(addrType, valueType, RangeCompat.OVERLAP);
            Assert.check(overlap);

            // Check depends on the type.
            valueType = normalizeType(valueType);
            addrType = normalizeType(addrType);
            if (valueType instanceof IntType) {
                // Integer types with overlap.
                Assert.check(addrType instanceof IntType);

                // Get both integer types.
                IntType typeValue = (IntType)valueType;
                IntType typeAddr = (IntType)addrType;

                // Get lower and upper bound checks.
                List<String> guards = listc(2);
                if (getLowerBound(typeValue) < getLowerBound(typeAddr)) {
                    guards.add(fmt("%s < %d", valueRefTxt, typeAddr.getLower()));
                }
                if (getUpperBound(typeValue) > getUpperBound(typeAddr)) {
                    guards.add(fmt("%s > %d", valueRefTxt, typeAddr.getUpper()));
                }
                Assert.check(!guards.isEmpty());
                String guard = StringUtils.join(guards, " || ");

                // Get the name of the addressable variable. An absolute name
                // is not needed, as the exception is wrapped in a stack trace
                // which makes the context clear enough.
                String name;
                if (addr instanceof DiscVariableExpression) {
                    DiscVariableExpression vaddr = (DiscVariableExpression)addr;
                    name = vaddr.getVariable().getName();
                } else {
                    ContVariableExpression vaddr = (ContVariableExpression)addr;
                    name = vaddr.getVariable().getName();
                }

                // Generate code.
                c.add("if (%s) {", guard);
                c.indent();
                c.add("throw new CifSimulatorException(fmt(\"Variable \\\"%s\\\" is assigned value \\\"%%s\\\", which "
                        + "violates the integer type bounds of the type \\\"%s\\\" of that variable.\", "
                        + "runtimeToString(%s)));", name, typeToStr(addr.getType()), gencodeAddrVarRef(addr));
                c.dedent();
                c.add("}");
            } else if (valueType instanceof ListType) {
                // List types with overlap. Overlap may be in the ranges of
                // the list types, or in the child elements.
                Assert.check(addrType instanceof ListType);

                // Get both list types.
                ListType typeValue = (ListType)valueType;
                ListType typeAddr = (ListType)addrType;

                // Get lower and upper bound checks.
                List<String> guards = listc(2);
                if (getLowerBound(typeValue) < getLowerBound(typeAddr)) {
                    guards.add(fmt("%s.size() < %d", valueRefTxt, typeAddr.getLower()));
                }
                if (getUpperBound(typeValue) > getUpperBound(typeAddr)) {
                    guards.add(fmt("%s.size() > %d", valueRefTxt, typeAddr.getUpper()));
                }

                // Generate code for list type range bound checking, if needed.
                if (!guards.isEmpty()) {
                    // Combine guards.
                    String guard = StringUtils.join(guards, " || ");

                    // Get the name of the addressable variable. An absolute
                    // name is not needed, as the exception is wrapped in a
                    // stack trace which makes the context clear enough.
                    String name;
                    if (addr instanceof DiscVariableExpression) {
                        DiscVariableExpression vaddr = (DiscVariableExpression)addr;
                        name = vaddr.getVariable().getName();
                    } else {
                        ContVariableExpression vaddr = (ContVariableExpression)addr;
                        name = vaddr.getVariable().getName();
                    }

                    // Generate code.
                    c.add("if (%s) {", guard);
                    c.indent();
                    c.add("throw new CifSimulatorException(fmt(\"Variable \\\"%s\\\" is assigned value \\\"%%s\\\", "
                            + "which violates the list type bounds of the type \\\"%s\\\" of that variable.\", "
                            + "runtimeToString(%s)));", name, typeToStr(addr.getType()), gencodeAddrVarRef(addr));
                    c.dedent();
                    c.add("}");
                }

                // Generate code for the checking of the elements.
                c.add("for (%s elem%d: %s) {", gencodeType(typeValue.getElementType(), ctxt), level, valueRefTxt);
                c.indent();
                gencodeTypeRangeBoundCheck(addr, "elem" + level, typeValue.getElementType(), typeAddr.getElementType(),
                        level + 1);
                c.dedent();
                c.add("}");
            } else if (valueType instanceof SetType) {
                // Set types with overlap. Check all elements recursively.
                Assert.check(addrType instanceof SetType);

                // Get both set types.
                SetType typeValue = (SetType)valueType;
                SetType typeAddr = (SetType)addrType;

                // Generate code for the checking of the elements.
                c.add("for (%s elem%d: %s) {", gencodeType(typeValue.getElementType(), ctxt), level, valueRefTxt);
                c.indent();
                gencodeTypeRangeBoundCheck(addr, "elem" + level, typeValue.getElementType(), typeAddr.getElementType(),
                        level + 1);
                c.dedent();
                c.add("}");
            } else if (valueType instanceof DictType) {
                // Dictionary types with overlap. Check all pairs recursively.
                Assert.check(addrType instanceof DictType);

                // Get both dictionary types.
                DictType typeValue = (DictType)valueType;
                DictType typeAddr = (DictType)addrType;

                // Generate code for the checking of the pairs.
                c.add("for (Entry<%s, %s> elem%d: %s.entrySet()) {", gencodeType(typeValue.getKeyType(), ctxt, true),
                        gencodeType(typeValue.getValueType(), ctxt, true), level, valueRefTxt);
                c.indent();
                gencodeTypeRangeBoundCheck(addr, "elem" + level + ".getKey()", typeValue.getKeyType(),
                        typeAddr.getKeyType(), level + 1);
                gencodeTypeRangeBoundCheck(addr, "elem" + level + ".getValue()", typeValue.getValueType(),
                        typeAddr.getValueType(), level + 1);
                c.dedent();
                c.add("}");
            } else if (valueType instanceof TupleType) {
                // Tuple types with overlap. Check all fields recursively.
                // Note that not all fields necessarily need checking, but at
                // least one of them does.
                Assert.check(addrType instanceof TupleType);

                // Get both tuple types, and their fields.
                TupleType typeValue = (TupleType)valueType;
                TupleType typeAddr = (TupleType)addrType;
                List<Field> fieldsValue = typeValue.getFields();
                List<Field> fieldsAddr = typeAddr.getFields();

                // Recursively generate for all fields.
                for (int i = 0; i < fieldsValue.size(); i++) {
                    Field fieldValue = fieldsValue.get(i);
                    Field fieldAddr = fieldsAddr.get(i);
                    String newRefTxt = valueRefTxt + ".";
                    newRefTxt += ctxt.getTupleTypeFieldFieldName(typeAddr, i);
                    gencodeTypeRangeBoundCheck(addr, newRefTxt, fieldValue.getType(), fieldAddr.getType(), level);
                }
            } else {
                // Type is not an integer or a container, so it doesn't contain
                // an integer type. No checking needed.
            }
        }
    }
}
