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

package org.eclipse.escet.cif.common;

import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.escet.cif.metamodel.cif.AlgParameter;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.ComponentDef;
import org.eclipse.escet.cif.metamodel.cif.ComponentParameter;
import org.eclipse.escet.cif.metamodel.cif.EventParameter;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.LocationParameter;
import org.eclipse.escet.cif.metamodel.cif.Parameter;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.SupKind;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Constant;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumLiteral;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.cif.metamodel.cif.expressions.AlgVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BaseFunctionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.BoolExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CastExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CompInstWrapExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CompParamExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CompParamWrapExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ComponentExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ConstantExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ContVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DictExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DictPair;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ElifExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EnumLiteralExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.FieldExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.FunctionCallExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.FunctionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IfExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.InputVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IntExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ListExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.LocationExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ProjectionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.RealExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ReceivedExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SelfExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SetExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SliceExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunction;
import org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunctionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.StringExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SwitchCase;
import org.eclipse.escet.cif.metamodel.cif.expressions.SwitchExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TauExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TimeExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TupleExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryOperator;
import org.eclipse.escet.cif.metamodel.cif.functions.Function;
import org.eclipse.escet.cif.metamodel.cif.functions.FunctionParameter;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.CompInstWrapType;
import org.eclipse.escet.cif.metamodel.cif.types.CompParamWrapType;
import org.eclipse.escet.cif.metamodel.cif.types.ComponentDefType;
import org.eclipse.escet.cif.metamodel.cif.types.ComponentType;
import org.eclipse.escet.cif.metamodel.cif.types.DictType;
import org.eclipse.escet.cif.metamodel.cif.types.DistType;
import org.eclipse.escet.cif.metamodel.cif.types.EnumType;
import org.eclipse.escet.cif.metamodel.cif.types.Field;
import org.eclipse.escet.cif.metamodel.cif.types.FuncType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.cif.metamodel.cif.types.ListType;
import org.eclipse.escet.cif.metamodel.cif.types.RealType;
import org.eclipse.escet.cif.metamodel.cif.types.SetType;
import org.eclipse.escet.cif.metamodel.cif.types.StringType;
import org.eclipse.escet.cif.metamodel.cif.types.TupleType;
import org.eclipse.escet.cif.metamodel.cif.types.TypeRef;
import org.eclipse.escet.cif.metamodel.cif.types.VoidType;
import org.eclipse.escet.cif.parser.CifScanner;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Numbers;
import org.eclipse.escet.common.java.Strings;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** CIF textual formatting utility methods. */
public class CifTextUtils {
    /** Constructor for the {@link CifTextUtils} class. */
    private CifTextUtils() {
        // Static class.
    }

    /** CIF textual syntax keywords. Don't access directly. Use {@link #getKeywords} instead. */
    private static Set<String> keywords = null;

    /**
     * Returns the CIF textual syntax keywords.
     *
     * <p>
     * This method is thread safe.
     * </p>
     *
     * @return The CIF textual syntax keywords.
     */
    public static final Set<String> getKeywords() {
        // Get and return the keywords, if available.
        Set<String> rslt = keywords;
        if (rslt != null) {
            return rslt;
        }

        // Not yet available. Compute and store the keywords.
        rslt = set();
        for (String kw: CifScanner.getKeywords("Keywords")) {
            rslt.add(kw);
        }
        for (String kw: CifScanner.getKeywords("SupKind")) {
            rslt.add(kw);
        }
        for (String kw: CifScanner.getKeywords("StdLibFunction")) {
            rslt.add(kw);
        }
        for (String kw: CifScanner.getKeywords("Operator")) {
            rslt.add(kw);
        }
        keywords = rslt;
        return rslt;
    }

    /**
     * Returns the given CIF identifier in the CIF ASCII syntax. If it is equal to a {@link #keywords CIF keyword}, it
     * is escaped.
     *
     * @param id The CIF identifier.
     * @return The CIF identifier in CIF ASCII syntax.
     */
    public static String escapeIdentifier(String id) {
        return getKeywords().contains(id) ? "$" + id : id;
    }

    /**
     * Converts a CIF supervisory kind to a textual representation in the CIF ASCII syntax.
     *
     * @param kind The CIF supervisory kind. Must not be {@link SupKind#NONE}.
     * @return The textual representation of the supervisory kind in the CIF ASCII syntax.
     */
    public static String kindToStr(SupKind kind) {
        Assert.check(kind != SupKind.NONE);
        return kind.getName().toLowerCase(Locale.US);
    }

    /**
     * Converts a CIF event controllability to a textual representation in the CIF ASCII syntax.
     *
     * @param ctrl The CIF event controllability.
     * @return The textual representation of the event controllability in the CIF ASCII syntax. It is either
     *     {@code "event"}, {@code "controllable"}, or {@code "uncontrollable"},
     */
    public static String controllabilityToStr(Boolean ctrl) {
        return (ctrl == null) ? "event" : ctrl ? "controllable" : "uncontrollable";
    }

    /**
     * Converts a CIF type to a textual representation derived from the CIF ASCII syntax.
     *
     * <p>
     * References to enumeration declarations, as well as components and component definitions, are converted to their
     * absolute name, with keyword escaping ({@code $}), and without absolute reference prefixes ({@code ^}). Note that
     * wrapping types are silently discarded, and two types referred to via different instantiations, will both result
     * in the same textual representation, which refers to the original declaration, regardless of via what it was
     * referenced.
     * </p>
     *
     * <p>
     * This method, unlike the methods of the {@code CifPrettyPrinter}, supports types that are not contained in a
     * specification (and thus have no scope).
     * </p>
     *
     * @param type The CIF type to convert.
     * @return The textual representation of the CIF type.
     */
    public static String typeToStr(CifType type) {
        // See also: CifPrettyPrinter

        if (type instanceof BoolType) {
            return "bool";
        }

        if (type instanceof IntType) {
            IntType itype = (IntType)type;
            if (CifTypeUtils.isRangeless(itype)) {
                return "int";
            }
            return fmt("int[%s..%s]", Numbers.formatNumber(itype.getLower()), Numbers.formatNumber(itype.getUpper()));
        }

        if (type instanceof TypeRef) {
            return typeToStr(((TypeRef)type).getType().getType());
        }

        if (type instanceof EnumType) {
            EnumType etype = (EnumType)type;
            EnumDecl edecl = etype.getEnum();
            return getAbsName(edecl);
        }

        if (type instanceof RealType) {
            return "real";
        }

        if (type instanceof StringType) {
            return "string";
        }

        if (type instanceof ListType) {
            ListType ltype = (ListType)type;
            String elemTxt = typeToStr(ltype.getElementType());
            if (CifTypeUtils.isRangeless(ltype)) {
                return "list " + elemTxt;
            }
            if (ltype.getLower().equals(ltype.getUpper())) {
                return fmt("list[%s] %s", Numbers.formatNumber(ltype.getLower()), elemTxt);
            }
            return fmt("list[%s..%s] %s", Numbers.formatNumber(ltype.getLower()),
                    Numbers.formatNumber(ltype.getUpper()), elemTxt);
        }

        if (type instanceof SetType) {
            SetType stype = (SetType)type;
            return "set " + typeToStr(stype.getElementType());
        }

        if (type instanceof FuncType) {
            FuncType ftype = (FuncType)type;
            List<String> paramTypes;
            paramTypes = listc(ftype.getParamTypes().size());
            for (CifType paramType: ftype.getParamTypes()) {
                paramTypes.add(typeToStr(paramType));
            }
            return "func " + typeToStr(ftype.getReturnType()) + "(" + String.join(", ", paramTypes) + ")";
        }

        if (type instanceof DictType) {
            DictType dtype = (DictType)type;
            return "dict(" + typeToStr(dtype.getKeyType()) + ":" + typeToStr(dtype.getValueType()) + ")";
        }

        if (type instanceof TupleType) {
            TupleType ttype = (TupleType)type;
            List<String> fields;
            fields = listc(ttype.getFields().size());
            for (Field field: ttype.getFields()) {
                String ftype = typeToStr(field.getType());
                if (field.getName() == null) {
                    fields.add(ftype);
                } else {
                    fields.add(ftype + " " + field.getName());
                }
            }
            return "tuple(" + String.join("; ", fields) + ")";
        }

        if (type instanceof DistType) {
            DistType dtype = (DistType)type;
            return "dist " + typeToStr(dtype.getSampleType());
        }

        if (type instanceof ComponentDefType) {
            ComponentDefType dtype = (ComponentDefType)type;
            ComponentDef cdef = dtype.getDefinition();
            return getAbsName(cdef);
        }

        if (type instanceof ComponentType) {
            ComponentType ctype = (ComponentType)type;
            Component component = ctype.getComponent();
            return getAbsName(component);
        }

        if (type instanceof CompInstWrapType) {
            // As documented, simply unwrap wrapping types.
            return typeToStr(((CompInstWrapType)type).getReference());
        }

        if (type instanceof CompParamWrapType) {
            // As documented, simply unwrap wrapping types.
            return typeToStr(((CompParamWrapType)type).getReference());
        }

        if (type instanceof VoidType) {
            return "void";
        }

        throw new RuntimeException("Unknown CIF type: " + type);
    }

    /**
     * Converts CIF expressions to a textual representation derived from the CIF ASCII syntax. References to
     * declarations etc, are converted to their absolute name, with keyword escaping ({@code $}), and without absolute
     * reference prefixes ({@code ^}).
     *
     * <p>
     * References to declarations etc, are converted to their absolute name, with keyword escaping ({@code $}), and
     * without absolute reference prefixes ({@code ^}). Note that wrapping expressions are silently discarded, and two
     * declarations referred to via different instantiations, will both result in the same textual representation, which
     * refers to the original declaration, regardless of via what it was referenced.
     * </p>
     *
     * <p>
     * This method, unlike the methods of the {@code CifPrettyPrinter}, supports expressions that are not contained in a
     * specification (and thus have no scope).
     * </p>
     *
     * <p>
     * Expressions are converted to string, and joined using {@code ", "}.
     * </p>
     *
     * @param exprs The CIF expressions to convert.
     * @return The textual representation of the CIF expressions.
     * @see #exprToStr
     * @see CifEvalUtils#objsToStr
     */
    public static String exprsToStr(List<Expression> exprs) {
        List<String> txts = listc(exprs.size());
        for (Expression expr: exprs) {
            txts.add(exprToStr(expr));
        }
        return String.join(", ", txts);
    }

    /**
     * Converts a CIF expression to a textual representation derived from the CIF ASCII syntax.
     *
     * <p>
     * References to declarations etc, are converted to their absolute name, with keyword escaping ({@code $}), and
     * without absolute reference prefixes ({@code ^}). Note that wrapping expressions are silently discarded, and two
     * declarations referred to via different instantiations, will both result in the same textual representation, which
     * refers to the original declaration, regardless of via what it was referenced.
     * </p>
     *
     * <p>
     * This method, unlike the methods of the {@code CifPrettyPrinter}, supports expressions that are not contained in a
     * specification (and thus have no scope).
     * </p>
     *
     * @param expr The CIF expression to convert.
     * @return The textual representation of the CIF expression.
     * @see CifEvalUtils#objToStr
     */
    public static String exprToStr(Expression expr) {
        // See also: CifPrettyPrinter

        if (expr instanceof BoolExpression) {
            boolean value = ((BoolExpression)expr).isValue();
            return CifMath.boolToStr(value);
        }

        if (expr instanceof IntExpression) {
            int value = ((IntExpression)expr).getValue();
            return CifMath.intToStr(value);
        }

        if (expr instanceof RealExpression) {
            String value = ((RealExpression)expr).getValue();
            return value;
        }

        if (expr instanceof StringExpression) {
            String value = ((StringExpression)expr).getValue();
            return "\"" + Strings.escape(value) + "\"";
        }

        if (expr instanceof TimeExpression) {
            return "time";
        }

        if (expr instanceof CastExpression) {
            CastExpression cexpr = (CastExpression)expr;

            String childTxt = exprToStr(cexpr.getChild());

            int castStrength = getBindingStrength(expr);
            int childStrength = getBindingStrength(cexpr.getChild());

            if (castStrength > childStrength) {
                childTxt = "(" + childTxt + ")";
            }

            return "<" + typeToStr(cexpr.getType()) + ">" + childTxt;
        }

        if (expr instanceof UnaryExpression) {
            UnaryExpression uexpr = (UnaryExpression)expr;
            String childTxt = exprToStr(uexpr.getChild());

            String opTxt = operatorToStr(uexpr.getOperator());

            int opStrength = getBindingStrength(expr);
            int childStrength = getBindingStrength(uexpr.getChild());
            if (opStrength > childStrength) {
                childTxt = "(" + childTxt + ")";
            } else if (StringUtils.isAlpha(StringUtils.right(opTxt, 1))) {
                // No parentheses, so add a space, if the operator ends with
                // a letter.
                opTxt += " ";
            }

            return opTxt + childTxt;
        }

        if (expr instanceof BinaryExpression) {
            BinaryExpression bexpr = (BinaryExpression)expr;
            BinaryOperator op = bexpr.getOperator();

            String opTxt = operatorToStr(op);

            int opStrength = getBindingStrength(expr);
            int leftStrength = getBindingStrength(bexpr.getLeft());
            int rightStrength = getBindingStrength(bexpr.getRight());

            String leftTxt = exprToStr(bexpr.getLeft());
            if (opStrength > leftStrength
                    || (opStrength == leftStrength && getAssociativity(op) != Associativity.LEFT))
            {
                // Local operator has higher binding strength, or they are
                // equal and it is not left-associative.
                leftTxt = "(" + leftTxt + ")";
            }

            String rightTxt = exprToStr(bexpr.getRight());
            if (opStrength > rightStrength
                    || (opStrength == rightStrength && getAssociativity(op) != Associativity.RIGHT))
            {
                // Local operator has higher binding strength, or they are
                // equal and it is not right-associative.
                rightTxt = "(" + rightTxt + ")";
            }

            return leftTxt + " " + opTxt + " " + rightTxt;
        }

        if (expr instanceof IfExpression) {
            IfExpression ifExpr = (IfExpression)expr;

            StringBuilder txt = new StringBuilder();
            txt.append("if ");
            txt.append(exprsToStr(ifExpr.getGuards()));
            txt.append(": ");
            txt.append(exprToStr(ifExpr.getThen()));

            for (ElifExpression elif: ifExpr.getElifs()) {
                txt.append(" elif ");
                txt.append(exprsToStr(elif.getGuards()));
                txt.append(": ");
                txt.append(exprToStr(elif.getThen()));
            }

            txt.append(" else ");
            txt.append(exprToStr(ifExpr.getElse()));
            txt.append(" end");

            return txt.toString();
        }

        if (expr instanceof SwitchExpression) {
            SwitchExpression switchExpr = (SwitchExpression)expr;
            boolean autRef = CifTypeUtils.isAutRefExpr(switchExpr.getValue());

            StringBuilder txt = new StringBuilder();
            txt.append("switch ");
            txt.append(exprToStr(switchExpr.getValue()));
            txt.append(": ");

            for (SwitchCase cse: switchExpr.getCases()) {
                Expression key = cse.getKey();
                if (key == null) {
                    txt.append("else ");
                } else {
                    txt.append("case ");
                    if (autRef) {
                        Expression locRef = CifTypeUtils.unwrapExpression(key);
                        Location loc = ((LocationExpression)locRef).getLocation();
                        String locName = loc.getName();
                        Assert.notNull(locName);
                        txt.append(escapeIdentifier(locName));
                    } else {
                        txt.append(exprToStr(key));
                    }
                    txt.append(": ");
                }
                txt.append(exprToStr(cse.getValue()));
                txt.append(" ");
            }

            txt.append("end");

            return txt.toString();
        }

        if (expr instanceof ProjectionExpression) {
            ProjectionExpression pexpr = (ProjectionExpression)expr;

            String childTxt = exprToStr(pexpr.getChild());

            int projStrength = getBindingStrength(expr);
            int childStrength = getBindingStrength(pexpr.getChild());

            if (projStrength > childStrength) {
                childTxt = "(" + childTxt + ")";
            }

            return childTxt + "[" + exprToStr(pexpr.getIndex()) + "]";
        }

        if (expr instanceof SliceExpression) {
            SliceExpression sexpr = (SliceExpression)expr;

            String childTxt = exprToStr(sexpr.getChild());

            int sliceStrength = getBindingStrength(expr);
            int childStrength = getBindingStrength(sexpr.getChild());

            if (sliceStrength > childStrength) {
                childTxt = "(" + childTxt + ")";
            }

            StringBuilder txt = new StringBuilder();
            txt.append(childTxt);
            txt.append("[");
            if (sexpr.getBegin() != null) {
                txt.append(exprToStr(sexpr.getBegin()));
            }
            txt.append(":");
            if (sexpr.getEnd() != null) {
                txt.append(exprToStr(sexpr.getEnd()));
            }
            txt.append("]");
            return txt.toString();
        }

        if (expr instanceof FunctionCallExpression) {
            FunctionCallExpression fexpr = (FunctionCallExpression)expr;

            String funcTxt = exprToStr(fexpr.getFunction());

            int callStrength = getBindingStrength(expr);
            int funcStrength = getBindingStrength(fexpr.getFunction());

            if (callStrength > funcStrength) {
                funcTxt = "(" + funcTxt + ")";
            }

            return funcTxt + "(" + exprsToStr(fexpr.getParams()) + ")";
        }

        if (expr instanceof ListExpression) {
            ListExpression lexpr = (ListExpression)expr;
            return "[" + exprsToStr(lexpr.getElements()) + "]";
        }

        if (expr instanceof SetExpression) {
            SetExpression sexpr = (SetExpression)expr;
            return "{" + exprsToStr(sexpr.getElements()) + "}";
        }

        if (expr instanceof TupleExpression) {
            TupleExpression texpr = (TupleExpression)expr;
            return "(" + exprsToStr(texpr.getFields()) + ")";
        }

        if (expr instanceof DictExpression) {
            DictExpression dexpr = (DictExpression)expr;
            StringBuilder txt = new StringBuilder();
            txt.append("{");
            boolean first = true;
            for (DictPair pair: dexpr.getPairs()) {
                if (first) {
                    first = false;
                } else {
                    txt.append(", ");
                }
                txt.append(exprToStr(pair.getKey()));
                txt.append(": ");
                txt.append(exprToStr(pair.getValue()));
            }
            txt.append("}");
            return txt.toString();
        }

        if (expr instanceof ConstantExpression) {
            Constant refObj = ((ConstantExpression)expr).getConstant();
            return getAbsName(refObj);
        }

        if (expr instanceof DiscVariableExpression) {
            DiscVariable refObj = ((DiscVariableExpression)expr).getVariable();
            return getAbsName(refObj);
        }

        if (expr instanceof AlgVariableExpression) {
            AlgVariable refObj = ((AlgVariableExpression)expr).getVariable();
            return getAbsName(refObj);
        }

        if (expr instanceof ContVariableExpression) {
            ContVariableExpression cvexpr = (ContVariableExpression)expr;
            ContVariable refObj = cvexpr.getVariable();
            String rslt = getAbsName(refObj);
            if (cvexpr.isDerivative()) {
                rslt += "'";
            }
            return rslt;
        }

        if (expr instanceof TauExpression) {
            return "tau";
        }

        if (expr instanceof LocationExpression) {
            // Locations may not have a name. However, we can't refer to
            // nameless locations in expressions.
            Location refObj = ((LocationExpression)expr).getLocation();
            return getAbsName(refObj);
        }

        if (expr instanceof EnumLiteralExpression) {
            EnumLiteral refObj = ((EnumLiteralExpression)expr).getLiteral();
            return getAbsName(refObj);
        }

        if (expr instanceof EventExpression) {
            Event refObj = ((EventExpression)expr).getEvent();
            return getAbsName(refObj);
        }

        if (expr instanceof FieldExpression) {
            // Due to the scoping rules, we can use the name of the field
            // directly.
            Field field = ((FieldExpression)expr).getField();
            Assert.notNull(field.getName());
            return field.getName();
        }

        if (expr instanceof StdLibFunctionExpression) {
            StdLibFunctionExpression fexpr = (StdLibFunctionExpression)expr;
            return functionToStr(fexpr.getFunction());
        }

        if (expr instanceof FunctionExpression) {
            Function refObj = ((FunctionExpression)expr).getFunction();
            return getAbsName(refObj);
        }

        if (expr instanceof InputVariableExpression) {
            InputVariable refObj = ((InputVariableExpression)expr).getVariable();
            return getAbsName(refObj);
        }

        if (expr instanceof ComponentExpression) {
            Component refObj = ((ComponentExpression)expr).getComponent();
            return getAbsName(refObj);
        }

        if (expr instanceof CompParamExpression) {
            ComponentParameter refObj = ((CompParamExpression)expr).getParameter();
            return getAbsName(refObj);
        }

        if (expr instanceof CompInstWrapExpression) {
            // As documented, simply unwrap wrapping types. Note that
            // 'inst1.constant' and 'inst2.constant', where 'inst1' and
            // 'inst2' are instantiations of 'group.CompDef', both become
            // 'group.CompDef.constant'. Such references are not valid in the
            // CIF ASCII syntax, but are informative enough for this context.
            CompInstWrapExpression wrapper = (CompInstWrapExpression)expr;
            return exprToStr(wrapper.getReference());
        }

        if (expr instanceof CompParamWrapExpression) {
            // As documented, simply unwrap wrapping types. Note that
            // 'param1.constant' and 'param2.constant', where 'param1' and
            // 'param2' are parameters of type 'group.CompDef', both become
            // 'group.CompDef.constant'. Such references are not valid in the
            // CIF ASCII syntax, but are informative enough for this context.
            CompParamWrapExpression wrapper = (CompParamWrapExpression)expr;
            return exprToStr(wrapper.getReference());
        }

        if (expr instanceof ReceivedExpression) {
            return "?";
        }

        if (expr instanceof SelfExpression) {
            return "self";
        }

        throw new RuntimeException("Unknown expr: " + expr);
    }

    /**
     * Returns the CIF ASCII representation of a unary operator.
     *
     * @param op The unary operator.
     * @return The CIF ASCII representation of a unary operator.
     */
    public static String operatorToStr(UnaryOperator op) {
        switch (op) {
            case INVERSE:
                return "not";
            case NEGATE:
                return "-";
            case PLUS:
                return "+";
            case SAMPLE:
                return "sample";
        }
        throw new RuntimeException("Unknown unop: " + op);
    }

    /**
     * Returns the CIF ASCII representation of a binary operator.
     *
     * @param op The binary operator.
     * @return The CIF ASCII representation of a binary operator.
     */
    public static String operatorToStr(BinaryOperator op) {
        switch (op) {
            case ADDITION:
                return "+";
            case BI_CONDITIONAL:
                return "<=>";
            case CONJUNCTION:
                return "and";
            case DISJUNCTION:
                return "or";
            case DIVISION:
                return "/";
            case ELEMENT_OF:
                return "in";
            case EQUAL:
                return "=";
            case GREATER_EQUAL:
                return ">=";
            case GREATER_THAN:
                return ">";
            case IMPLICATION:
                return "=>";
            case INTEGER_DIVISION:
                return "div";
            case LESS_EQUAL:
                return "<=";
            case LESS_THAN:
                return "<";
            case MODULUS:
                return "mod";
            case MULTIPLICATION:
                return "*";
            case SUBSET:
                return "sub";
            case SUBTRACTION:
                return "-";
            case UNEQUAL:
                return "!=";
        }
        throw new RuntimeException("Unknown binop: " + op);
    }

    /**
     * Returns the CIF ASCII representation of a standard library function.
     *
     * @param func The standard library function.
     * @return The CIF ASCII representation of a standard library function.
     */
    public static String functionToStr(StdLibFunction func) {
        switch (func) {
            case ACOSH:
                return "acosh";
            case ACOS:
                return "acos";
            case ASINH:
                return "asinh";
            case ASIN:
                return "asin";
            case ATANH:
                return "atanh";
            case ATAN:
                return "atan";
            case COSH:
                return "cosh";
            case COS:
                return "cos";
            case SINH:
                return "sinh";
            case SIN:
                return "sin";
            case TANH:
                return "tanh";
            case TAN:
                return "tan";
            case ABS:
                return "abs";
            case CBRT:
                return "cbrt";
            case CEIL:
                return "ceil";
            case DELETE:
                return "del";
            case EMPTY:
                return "empty";
            case EXP:
                return "exp";
            case FLOOR:
                return "floor";
            case FORMAT:
                return "fmt";
            case LN:
                return "ln";
            case LOG:
                return "log";
            case MAXIMUM:
                return "max";
            case MINIMUM:
                return "min";
            case POP:
                return "pop";
            case POWER:
                return "pow";
            case ROUND:
                return "round";
            case SCALE:
                return "scale";
            case SIGN:
                return "sign";
            case SIZE:
                return "size";
            case SQRT:
                return "sqrt";
            case BERNOULLI:
                return "bernoulli";
            case BETA:
                return "beta";
            case BINOMIAL:
                return "binomial";
            case CONSTANT:
                return "constant";
            case ERLANG:
                return "erlang";
            case EXPONENTIAL:
                return "exponential";
            case GAMMA:
                return "gamma";
            case GEOMETRIC:
                return "geometric";
            case LOG_NORMAL:
                return "lognormal";
            case NORMAL:
                return "normal";
            case POISSON:
                return "poisson";
            case RANDOM:
                return "random";
            case TRIANGLE:
                return "triangle";
            case UNIFORM:
                return "uniform";
            case WEIBULL:
                return "weibull";
        }
        throw new RuntimeException("Unknown stdlib: " + func);
    }

    /**
     * Returns the associativity of the binary operator.
     *
     * @param op The binary operator.
     * @return The associativity of the binary operator.
     */
    public static Associativity getAssociativity(BinaryOperator op) {
        switch (op) {
            case ADDITION:
                return Associativity.LEFT;
            case BI_CONDITIONAL:
                return Associativity.NONE;
            case CONJUNCTION:
                return Associativity.LEFT;
            case DISJUNCTION:
                return Associativity.LEFT;
            case DIVISION:
                return Associativity.LEFT;
            case ELEMENT_OF:
                return Associativity.LEFT;
            case EQUAL:
                return Associativity.LEFT;
            case GREATER_EQUAL:
                return Associativity.LEFT;
            case GREATER_THAN:
                return Associativity.LEFT;
            case IMPLICATION:
                return Associativity.NONE;
            case INTEGER_DIVISION:
                return Associativity.LEFT;
            case LESS_EQUAL:
                return Associativity.LEFT;
            case LESS_THAN:
                return Associativity.LEFT;
            case MODULUS:
                return Associativity.LEFT;
            case MULTIPLICATION:
                return Associativity.LEFT;
            case SUBSET:
                return Associativity.LEFT;
            case SUBTRACTION:
                return Associativity.LEFT;
            case UNEQUAL:
                return Associativity.LEFT;
        }
        throw new RuntimeException("Unknown binop: " + op);
    }

    /**
     * Returns the binding strength of the expression. The binding strength defines the order in which sub-expressions
     * are applied in an expression. Expressions with higher binding strength are applied before expressions with lower
     * binding strength.
     *
     * @param expr The expression for which to return the binding strength.
     * @return The binding strength of the expression.
     */
    public static int getBindingStrength(Expression expr) {
        // 0: =>, <=>
        // 1: or
        // 2: and
        // 3: <, <=, >, >=, =, !=, in, sub
        // 4: + (binary), - (binary)
        // 5: *, /, div, mod
        // 6: + (unary), - (unary), not, sample
        // 7: l[x], l[x:y], f(x)
        // 8: true, false, 5, 6.0, "a", time, [1], {1}, {1:2}, (1, 2), <real>x, if, switch, a, abs, ?, self

        if (expr instanceof BoolExpression) {
            return 8;
        } else if (expr instanceof IntExpression) {
            return 8;
        } else if (expr instanceof RealExpression) {
            return 8;
        } else if (expr instanceof StringExpression) {
            return 8;
        } else if (expr instanceof TimeExpression) {
            return 8;
        } else if (expr instanceof CastExpression) {
            return 8;
        } else if (expr instanceof ReceivedExpression) {
            return 8;
        }

        if (expr instanceof UnaryExpression) {
            UnaryOperator op = ((UnaryExpression)expr).getOperator();
            switch (op) {
                case INVERSE:
                    return 6;
                case NEGATE:
                    return 6;
                case PLUS:
                    return 6;
                case SAMPLE:
                    return 6;
                default:
                    throw new RuntimeException("Unknown unary op: " + op);
            }
        }

        if (expr instanceof BinaryExpression) {
            BinaryOperator op = ((BinaryExpression)expr).getOperator();
            switch (op) {
                case IMPLICATION:
                case BI_CONDITIONAL:
                    return 0;

                case DISJUNCTION:
                    return 1;

                case CONJUNCTION:
                    return 2;

                case GREATER_EQUAL:
                case GREATER_THAN:
                case LESS_EQUAL:
                case LESS_THAN:
                case EQUAL:
                case UNEQUAL:
                case ELEMENT_OF:
                case SUBSET:
                    return 3;

                case ADDITION:
                case SUBTRACTION:
                    return 4;

                case MULTIPLICATION:
                case DIVISION:
                case INTEGER_DIVISION:
                case MODULUS:
                    return 5;

                default:
                    throw new RuntimeException("Unknown unary op: " + op);
            }
        }

        if (expr instanceof IfExpression) {
            return 8;
        } else if (expr instanceof SwitchExpression) {
            return 8;
        }

        if (expr instanceof ProjectionExpression) {
            return 7;
        } else if (expr instanceof SliceExpression) {
            return 7;
        } else if (expr instanceof FunctionCallExpression) {
            return 7;
        }

        if (expr instanceof ListExpression || expr instanceof SetExpression || expr instanceof TupleExpression
                || expr instanceof DictExpression)
        {
            return 8;
        }

        if (expr instanceof ConstantExpression || expr instanceof DiscVariableExpression
                || expr instanceof AlgVariableExpression || expr instanceof ContVariableExpression
                || expr instanceof TauExpression || expr instanceof LocationExpression
                || expr instanceof EnumLiteralExpression || expr instanceof EventExpression
                || expr instanceof FieldExpression || expr instanceof BaseFunctionExpression
                || expr instanceof InputVariableExpression || expr instanceof ComponentExpression
                || expr instanceof CompParamExpression || expr instanceof CompInstWrapExpression
                || expr instanceof CompParamWrapExpression || expr instanceof SelfExpression)
        {
            return 8;
        }

        throw new RuntimeException("Unknown expr: " + expr);
    }

    /**
     * Converts a CIF invariant to a textual representation derived from the CIF ASCII syntax. The name, supervisory
     * kind keyword and 'invariant' keyword may optionally be included.
     *
     * <p>
     * References to declarations etc, are converted to their absolute name, with keyword escaping ({@code $}), and
     * without absolute reference prefixes ({@code ^}). Note that wrapping expressions are silently discarded, and two
     * declarations referred to via different instantiations, will both result in the same textual representation, which
     * refers to the original declaration, regardless of via what it was referenced.
     * </p>
     *
     * <p>
     * This method, unlike the methods of the {@code CifPrettyPrinter}, supports expressions that are not contained in a
     * specification (and thus have no scope).
     * </p>
     *
     * @param inv The CIF invariant to convert.
     * @param inclPrefix Whether to include the name, supervisory kind (if specified) and 'invariant' keyword as a
     *     prefix.
     * @return The textual representation of the CIF invariant.
     * @see #exprToStr
     */
    public static String invToStr(Invariant inv, boolean inclPrefix) {
        StringBuilder txt = new StringBuilder();

        if (inclPrefix) {
            if (inv.getSupKind() != SupKind.NONE) {
                txt.append(kindToStr(inv.getSupKind()));
                txt.append(" ");
            }
            txt.append("invariant ");
            if (inv.getName() != null) {
                txt.append(inv.getName());
                txt.append(": ");
            }
        }

        switch (inv.getInvKind()) {
            case STATE:
                txt.append(exprToStr(inv.getPredicate()));
                return txt.toString();

            case EVENT_DISABLES:
                txt.append(exprToStr(inv.getPredicate()));
                txt.append(" disables ");
                txt.append(exprToStr(inv.getEvent()));
                return txt.toString();

            case EVENT_NEEDS:
                txt.append(exprToStr(inv.getEvent()));
                txt.append(" needs ");
                txt.append(exprToStr(inv.getPredicate()));
                return txt.toString();
        }
        throw new RuntimeException("Unknown inv kind: " + inv.getInvKind());
    }

    /**
     * Returns the absolute name, in CIF ASCII syntax (potentially with {@code $} for keyword escaping), of the given
     * CIF object, without a {@code "^"} prefix. For specifications, {@code ""} is returned.
     *
     * <p>
     * {@link Field} objects are not supported, as they don't always have a name. Also, fields are part of tuple types,
     * which are not declared. The caller should handle fields as a special case.
     * </p>
     *
     * <p>
     * For nameless locations this method fails, as they don't have a name.
     * </p>
     *
     * @param obj The CIF object for which to return the absolute name. Must be a named object.
     * @return The absolute name in CIF ASCII syntax.
     */
    public static String getAbsName(PositionObject obj) {
        return getAbsName(obj, true);
    }

    /**
     * Returns the absolute name, of the given CIF object. Note that:
     * <ul>
     * <li>If an absolute identifier in CIF ASCII syntax is needed, identifiers should be escaped. A {@code "^"} prefix
     * however, is not included in the result.</li>
     * <li>For specifications, {@code ""} is returned.</li>
     * </ul>
     *
     * <p>
     * {@link Field} objects are not supported, as they don't always have a name. Also, fields are part of tuple types,
     * which are not declared. The caller should handle fields as a special case.
     * </p>
     *
     * <p>
     * For nameless locations this method fails, as they don't have a name.
     * </p>
     *
     * @param obj The CIF object for which to return the absolute name. Must be a named object.
     * @param escape Whether to escape identifiers ({@code $}).
     * @return The absolute name.
     */
    public static String getAbsName(PositionObject obj, boolean escape) {
        // Special case for specifications.
        if (obj instanceof Specification) {
            return "";
        }

        // Add name of 'obj'.
        String name = getName(obj);
        if (escape) {
            name = escapeIdentifier(name);
        }
        StringBuilder rslt = new StringBuilder();
        rslt.append(name);

        // Get parent of the object.
        PositionObject parent = (PositionObject)obj.eContainer();

        // Special case for parameters of component definitions. They don't
        // have the 'body' component of the component definition in their
        // ancestors, but need to get prefixed with the name of their component
        // definition nonetheless.
        if (obj instanceof ComponentParameter) {
            parent = ((ComponentDef)parent).getBody();
        } else if (parent instanceof Parameter) {
            parent = ((ComponentDef)parent.eContainer()).getBody();
        }

        // Walk up the parent hierarchy and add prefix names.
        while (true) {
            if (parent instanceof Specification) {
                break;
            }
            if (parent instanceof Component || parent instanceof Function) {
                String parentName = getName(parent);
                if (escape) {
                    parentName = escapeIdentifier(parentName);
                }
                rslt.insert(0, parentName + ".");
            }
            parent = (PositionObject)parent.eContainer();
        }

        // Return absolute name.
        return rslt.toString();
    }

    /**
     * Returns whether the given CIF object has a name.
     *
     * <p>
     * Note that {@link Specification}s are considered not to have a name, as the name can't be specified in the textual
     * syntax, and is fixed in the metamodel.
     * </p>
     *
     * <p>
     * Note that {@link Field}s are considered not to have a name, even when they do have one, for compatibility with
     * {@link #getName}. The caller should handle fields as a special case, if relevant.
     * </p>
     *
     * @param obj The CIF object for which to determine whether it has a name.
     * @return Whether the CIF object has a name ({@code true}) or not ({@code false}).
     * @see #getName
     * @see #getAbsName
     */
    public static boolean hasName(PositionObject obj) {
        if (obj instanceof Specification) {
            return false; // Explicitly 'false'. See method JavaDoc.
        } else if (obj instanceof Component) {
            return true; // Non-specification component.
        } else if (obj instanceof ComponentDef) {
            return true;
        } else if (obj instanceof Parameter) {
            return true;
        } else if (obj instanceof Declaration) {
            return true;
        } else if (obj instanceof EnumLiteral) {
            return true;
        } else if (obj instanceof Location) {
            return ((Location)obj).getName() != null;
        } else if (obj instanceof Invariant) {
            return ((Invariant)obj).getName() != null;
        } else if (obj instanceof FunctionParameter) {
            return true;
        } else if (obj instanceof Field) {
            return false; // Explicitly 'false'. See method JavaDoc.
        } else {
            return false; // Not a CIF object with a name.
        }
    }

    /**
     * Returns the name of the given CIF object. Names that are keywords are not escaped. For specifications, {@code ""}
     * is returned.
     *
     * <p>
     * Note that {@link Field} objects are not supported, as they don't always have a name. The caller should handle
     * fields as a special case.
     * </p>
     *
     * @param obj The CIF object for which to return the name. Must be a named object.
     * @return The name of the CIF object.
     * @see #hasName
     */
    public static String getName(PositionObject obj) {
        if (obj instanceof Component) {
            return ((Component)obj).getName();
        } else if (obj instanceof ComponentDef) {
            return ((ComponentDef)obj).getBody().getName();
        } else if (obj instanceof ComponentParameter) {
            return ((ComponentParameter)obj).getName();
        } else if (obj instanceof EventParameter) {
            return ((EventParameter)obj).getEvent().getName();
        } else if (obj instanceof LocationParameter) {
            return ((LocationParameter)obj).getLocation().getName();
        } else if (obj instanceof AlgParameter) {
            return ((AlgParameter)obj).getVariable().getName();
        } else if (obj instanceof Declaration) {
            return ((Declaration)obj).getName();
        } else if (obj instanceof EnumLiteral) {
            return ((EnumLiteral)obj).getName();
        } else if (obj instanceof Location) {
            String name = ((Location)obj).getName();
            Assert.notNull(name);
            return name;
        } else if (obj instanceof Invariant) {
            String name = ((Invariant)obj).getName();
            Assert.notNull(name);
            return name;
        } else if (obj instanceof FunctionParameter) {
            return ((FunctionParameter)obj).getParameter().getName();
        } else if (obj instanceof Field) {
            String msg = "Fields unsupported: caller should handle them as a special case.";
            throw new RuntimeException(msg);
        } else {
            throw new IllegalArgumentException("Unknown named obj: " + obj);
        }
    }

    /**
     * Returns the name of the CIF object referenced by the given CIF reference expression. For specifications,
     * {@code ""} is returned. Can handle wrapping expressions.
     *
     * @param expr The CIF reference expression for which to return the name of the referenced object. May be a wrapping
     *     expression.
     * @return The name of the CIF object referenced by the reference expression.
     */
    public static String getNameForRefExpr(Expression expr) {
        // Unwrap expression.
        expr = CifTypeUtils.unwrapExpression(expr);

        // Get object referenced by non-wrapping expression.
        PositionObject obj = CifScopeUtils.getRefObjFromRef(expr);

        // Return the name of the object.
        return getName(obj);
    }

    /**
     * Returns the object itself if it is itself a named object, the closest named ancestor of the given object if one
     * exists, or {@code null} if the given object is itself not a named object, and also has no named ancestor.
     *
     * @param obj The given object.
     * @return The given object itself (if named), its closest named ancestor (if any), or {@code null} (otherwise).
     */
    public static PositionObject getNamedSelfOrAncestor(PositionObject obj) {
        PositionObject cur = obj;
        while (cur != null && !hasName(cur)) {
            cur = (PositionObject)cur.eContainer();
        }
        return cur;
    }

    /**
     * Returns an end-user readable textual (reference) representation of a location, mostly for use in error messages.
     *
     * <p>
     * Can for instance be used in {@code "Unsupported %s: ..."} messages.
     * </p>
     *
     * @param loc The location. Must be a location of an automaton, and not a location parameter.
     * @return {@code "location \"LOC\" of automaton \"AUT\""} if the location has name {@code LOC} and the automaton it
     *     is a part of has absolute name {@code AUT}, or {@code "location of automaton \"AUT\""} if the location has no
     *     name, and the automaton it is a part of has absolute name {@code AUT}.
     * @see #getAbsName
     */
    public static String getLocationText1(Location loc) {
        // Get automaton.
        EObject parent = loc.eContainer();
        Assert.check(parent instanceof Automaton);
        Automaton aut = (Automaton)parent;

        // Return text.
        if (loc.getName() != null) {
            return fmt("location \"%s\" of automaton \"%s\"", getName(loc), getAbsName(aut));
        } else {
            // Nameless locations are the only location in their automaton.
            return fmt("location of automaton \"%s\"", getAbsName(aut));
        }
    }

    /**
     * Returns an end-user readable textual (reference) representation of a location, mostly for use in error messages.
     *
     * <p>
     * Can for instance be used in {@code "... in %s."} messages.
     * </p>
     *
     * @param loc The location. Must be a location of an automaton, and not a location parameter.
     * @return {@code "location \"LOC\" of automaton \"AUT\""} if the location has name {@code LOC} and the automaton it
     *     is a part of has absolute name {@code AUT}, or {@code "the location of automaton \"AUT\""} if the location
     *     has no name, and the automaton it is a part of has absolute name {@code AUT}.
     * @see #getAbsName
     */
    public static String getLocationText2(Location loc) {
        // Get automaton.
        EObject parent = loc.eContainer();
        Assert.check(parent instanceof Automaton);
        Automaton aut = (Automaton)parent;

        // Return text.
        if (loc.getName() != null) {
            return fmt("location \"%s\" of automaton \"%s\"", getName(loc), getAbsName(aut));
        } else {
            // Nameless locations are the only location in their automaton.
            return fmt("the location of automaton \"%s\"", getAbsName(aut));
        }
    }

    /**
     * Returns an end-user readable textual (reference) representation of a component, mostly for use in error messages.
     *
     * <p>
     * Can for instance be used in {@code "Unsupported %s: ..."} messages.
     * </p>
     *
     * @param comp The component. Component definition and instantiation are not supported.
     * @return {@code "specification"} for specifications, {@code "group \"NAME\""} for other groups,
     *     {@code "automaton \"NAME\"} for automata, where {@code NAME} is the absolute name of the component, with
     *     escaping of identifiers.
     * @see #getAbsName
     */
    public static String getComponentText1(ComplexComponent comp) {
        if (comp instanceof Specification) {
            return "specification";
        }
        if (comp instanceof Automaton) {
            return fmt("automaton \"%s\"", getAbsName(comp));
        }
        return fmt("group \"%s\"", getAbsName(comp));
    }

    /**
     * Returns an end-user readable textual (reference) representation of a component, mostly for use in error messages.
     *
     * <p>
     * Can for instance be used in {@code "... in %s."} messages.
     * </p>
     *
     * @param comp The component. Component definition and instantiation are not supported.
     * @return {@code "the top level scope of the specification"} for specifications, {@code "group \"NAME\""} for other
     *     groups, {@code "automaton \"NAME\""} for automata, where {@code NAME} is the absolute name of the component,
     *     with escaping of identifiers.
     * @see #getAbsName
     */
    public static String getComponentText2(ComplexComponent comp) {
        if (comp instanceof Specification) {
            return "the top level scope of the specification";
        }
        if (comp instanceof Automaton) {
            return fmt("automaton \"%s\"", getAbsName(comp));
        }
        return fmt("group \"%s\"", getAbsName(comp));
    }
}
