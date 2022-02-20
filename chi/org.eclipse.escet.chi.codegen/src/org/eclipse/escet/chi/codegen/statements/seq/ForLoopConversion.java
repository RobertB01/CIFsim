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

package org.eclipse.escet.chi.codegen.statements.seq;

import static org.eclipse.escet.chi.codegen.OutputPosition.genCurrentPositionStatement;
import static org.eclipse.escet.chi.codegen.expressions.ExpressionBase.convertExpression;
import static org.eclipse.escet.chi.codegen.expressions.ExpressionBase.makeExpression;
import static org.eclipse.escet.chi.codegen.statements.seq.AssignmentConversions.convertAssignment;
import static org.eclipse.escet.chi.codegen.types.TypeIDCreation.createTypeID;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newTupleExpression;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newTupleField;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newTupleType;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newVariableReference;
import static org.eclipse.escet.chi.typecheck.CheckType.copyType;
import static org.eclipse.escet.common.java.Assert.check;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.position.common.PositionUtils.copyPosition;

import java.util.List;

import org.eclipse.escet.chi.codegen.CodeGeneratorContext;
import org.eclipse.escet.chi.codegen.Constants;
import org.eclipse.escet.chi.codegen.expressions.ExpressionBase;
import org.eclipse.escet.chi.codegen.expressions.SimpleExpression;
import org.eclipse.escet.chi.codegen.java.JavaFile;
import org.eclipse.escet.chi.codegen.types.TypeID;
import org.eclipse.escet.chi.codegen.types.TypeIDCreation;
import org.eclipse.escet.chi.metamodel.chi.CallExpression;
import org.eclipse.escet.chi.metamodel.chi.DictType;
import org.eclipse.escet.chi.metamodel.chi.Expression;
import org.eclipse.escet.chi.metamodel.chi.ForStatement;
import org.eclipse.escet.chi.metamodel.chi.ListType;
import org.eclipse.escet.chi.metamodel.chi.SetType;
import org.eclipse.escet.chi.metamodel.chi.StdLibFunctionReference;
import org.eclipse.escet.chi.metamodel.chi.StdLibFunctions;
import org.eclipse.escet.chi.metamodel.chi.TupleExpression;
import org.eclipse.escet.chi.metamodel.chi.TupleField;
import org.eclipse.escet.chi.metamodel.chi.TupleType;
import org.eclipse.escet.chi.metamodel.chi.Type;
import org.eclipse.escet.chi.metamodel.chi.Unwind;
import org.eclipse.escet.chi.metamodel.chi.VariableDeclaration;
import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/**
 * Wrapper class for the for-loop conversion to sequential language.
 *
 * <p>
 * Generalized to a separate class as for loops are used at several places in the statements.
 * </p>
 */
public abstract class ForLoopConversion {
    /** Result of the conversion. */
    public static class ForLoopSeqResult {
        /** Initialization code. May be {@code null}. */
        public final ExpressionBase loopInit;

        /** End condition check code. May be {@code null}. */
        public final ExpressionBase loopCond;

        /** Increment code at end of the loop. May be {@code null}. */
        public final ExpressionBase loopIncr;

        /** Variable assignments to do at the start of the loop. May be {@code null}. */
        public final SeqCode varInit;

        /**
         * Constructor for the {@link ForLoopSeqResult} class.
         *
         * @param loopInit Initialization code. May be {@code null}.
         * @param loopCond End condition check code. May be {@code null}.
         * @param loopIncr Increment code at end of the loop. May be {@code null}.
         * @param varInit Variable assignments to do at the start of the loop. May be {@code null}.
         */
        public ForLoopSeqResult(ExpressionBase loopInit, ExpressionBase loopCond, ExpressionBase loopIncr,
                SeqCode varInit)
        {
            this.loopInit = loopInit;
            this.loopCond = loopCond;
            this.loopIncr = loopIncr;
            this.varInit = varInit;
        }
    }

    /**
     * Convert the 'for loop' in case the source argument is a 'range(x [,y, [,z]])'.
     *
     * @param source Source expression.
     * @param ctxt Code generator context.
     * @param currentClass Java class targeted for code generation.
     * @return Conversion result if the source has the correct form, else {@code null}.
     */
    private static ExpressionBase convertRange(Expression source, CodeGeneratorContext ctxt, JavaFile currentClass) {
        if (!(source instanceof CallExpression)) {
            return null;
        }
        CallExpression ce = (CallExpression)source;
        if (!(ce.getFunction() instanceof StdLibFunctionReference)) {
            return null;
        }
        StdLibFunctionReference slibref = (StdLibFunctionReference)ce.getFunction();

        if (slibref.getFunction() != StdLibFunctions.RANGE) {
            return null;
        }
        ExpressionBase init;
        ExpressionBase cond;
        ExpressionBase incr;
        if (ce.getArguments().size() == 1) {
            init = new SimpleExpression("0", source);
            cond = convertExpression(ce.getArguments().get(0), ctxt, currentClass);
            incr = new SimpleExpression("1", source);
        } else if (ce.getArguments().size() == 2) {
            init = convertExpression(ce.getArguments().get(0), ctxt, currentClass);
            cond = convertExpression(ce.getArguments().get(1), ctxt, currentClass);
            incr = new SimpleExpression("1", source);
        } else if (ce.getArguments().size() == 3) {
            init = convertExpression(ce.getArguments().get(0), ctxt, currentClass);
            cond = convertExpression(ce.getArguments().get(1), ctxt, currentClass);
            incr = convertExpression(ce.getArguments().get(2), ctxt, currentClass);
        } else {
            return null;
        }

        List<String> lines = list();
        lines.addAll(init.getCode());
        lines.addAll(cond.getCode());
        lines.addAll(incr.getCode());

        String line = genCurrentPositionStatement(source);
        if (line != null) {
            lines.add(line);
        }
        currentClass.addImport(Constants.RANGE_ITERATOR_FQC, false);
        line = fmt("new RangeIterator(%s, %s, %s)", init.getValue(), cond.getValue(), incr.getValue());
        return makeExpression(lines, line, source);
    }

    /**
     * Convert the 'for loop' in the general case.
     *
     * @param iterVal Source iterator value expression (already converted).
     * @param vars Variables assigned in the loop.
     * @param chiobj Chi source code reference.
     * @param ctxt Code generator context.
     * @param currentClass Java class targeted for code generation.
     * @return Conversion result.
     */
    private static ForLoopSeqResult convertGeneral(ExpressionBase iterVal, List<VariableDeclaration> vars,
            PositionObject chiobj, CodeGeneratorContext ctxt, JavaFile currentClass)
    {
        String iterVarName = ctxt.getDefinition(chiobj);
        String line;
        ExpressionBase init, cond;
        line = fmt("%s = %s", iterVarName, iterVal.getValue());
        init = makeExpression(iterVal.getCode(), line, iterVal.position);
        cond = makeExpression(null, iterVarName + ".hasNext()", chiobj);
        SeqCode code = new SeqCode(null, chiobj);

        // Construct an lhs expression.
        Expression lhs = constructLhsVars(vars);
        TypeID tid = createTypeID(lhs.getType(), ctxt);

        String valName = ctxt.makeUniqueName("val");
        code.lines.add(fmt("%s %s = %s.next();", tid.getJavaType(), valName, iterVarName));
        code.lines.addAll(convertAssignment(lhs, valName, lhs.getType(), ctxt, currentClass));

        List<Seq> seqs = list();
        seqs.add(code);
        return new ForLoopSeqResult(init, cond, null, code);
    }

    /**
     * Convert a for loop to sequential language.
     *
     * @param chiobj Looping object.
     * @param ctxt Code generator context.
     * @param currentClass Java class targeted for code generation.
     * @return Conversion result.
     */
    public static ForLoopSeqResult convertForLoop(PositionObject chiobj, CodeGeneratorContext ctxt,
            JavaFile currentClass)
    {
        Unwind unw;
        if (chiobj instanceof ForStatement) {
            ForStatement fs = (ForStatement)chiobj;
            check(fs.getUnwinds().size() == 1);
            unw = first(fs.getUnwinds());
        } else {
            check(chiobj instanceof Unwind);
            unw = (Unwind)chiobj;
        }
        Expression source = unw.getSource();
        List<VariableDeclaration> vars = unw.getVariables();

        ExpressionBase iterVal = convertRange(source, ctxt, currentClass);
        if (iterVal == null) {
            ExpressionBase lstVar = convertExpression(source, ctxt, currentClass);
            String line = fmt("%s.iterator()", lstVar.getValue());

            iterVal = makeExpression(lstVar.getCode(), line, lstVar.position);
        }
        return convertGeneral(iterVal, vars, chiobj, ctxt, currentClass);
    }

    /**
     * Extract the element type of an iteration over a container type.
     *
     * @param containerType Container type to iterate over.
     * @param ctxt Code generator context.
     * @return The element type being iterated over.
     */
    public static TypeID getElementType(Type containerType, CodeGeneratorContext ctxt) {
        if (containerType instanceof ListType) {
            ListType lt = (ListType)containerType;
            return createTypeID(lt.getElementType(), ctxt);
        } else if (containerType instanceof SetType) {
            SetType st = (SetType)containerType;
            return createTypeID(st.getElementType(), ctxt);
        } else {
            check(containerType instanceof DictType);
            DictType dt = (DictType)containerType;
            List<String> names = list("key", "value");
            List<TypeID> tids = list(createTypeID(dt.getKeyType(), ctxt), createTypeID(dt.getValueType(), ctxt));
            return TypeIDCreation.createTupleTypeID(names, tids, ctxt);
        }
    }

    /**
     * Construct a LHS expression for assigning to a list of variable declarations.
     *
     * @param vds Variable declarations to assign to.
     * @return Expression for an lhs assignment.
     */
    private static Expression constructLhsVars(List<VariableDeclaration> vds) {
        if (vds.size() == 1) {
            VariableDeclaration vd = vds.get(0);
            Position pos = copyPosition(vd);
            Type tp = copyType(vd.getType());
            return newVariableReference(pos, tp, vd);
        }
        List<TupleField> tfs = list();
        List<Expression> vrs = list();
        for (VariableDeclaration vd: vds) {
            Type tp = copyType(vd.getType());
            TupleField tf = newTupleField("", copyPosition(vd), tp);
            tfs.add(tf);

            tp = copyType(vd.getType());
            vrs.add(newVariableReference(copyPosition(vd), tp, vd));
        }
        TupleType lhsType = newTupleType(tfs, null);
        TupleExpression te = newTupleExpression(vrs, null, lhsType);
        return te;
    }
}
