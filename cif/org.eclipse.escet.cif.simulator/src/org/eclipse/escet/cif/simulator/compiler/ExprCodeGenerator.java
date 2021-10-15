//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.cif.common.CifTypeUtils.normalizeType;
import static org.eclipse.escet.cif.simulator.compiler.CifCompilerContext.CONT_SUB_STATE_FIELD_NAME;
import static org.eclipse.escet.cif.simulator.compiler.CifCompilerContext.RCVD_VALUE_VAR_NAME;
import static org.eclipse.escet.cif.simulator.compiler.CifFormatPatternCodeGenerator.gencodePattern;
import static org.eclipse.escet.cif.simulator.compiler.LiteralCodeGenerator.gencodeLiteral;
import static org.eclipse.escet.cif.simulator.compiler.LiteralCodeGenerator.isSerializableLiteral;
import static org.eclipse.escet.cif.simulator.compiler.TypeCodeGenerator.gencodeType;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.last;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.escet.cif.common.CifEvalException;
import org.eclipse.escet.cif.common.CifEvalUtils;
import org.eclipse.escet.cif.common.CifMath;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.RangeCompat;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumLiteral;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.cif.metamodel.cif.expressions.AlgVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BoolExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CastExpression;
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
import org.eclipse.escet.cif.metamodel.cif.expressions.TimeExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TupleExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression;
import org.eclipse.escet.cif.metamodel.cif.functions.Function;
import org.eclipse.escet.cif.metamodel.cif.functions.FunctionParameter;
import org.eclipse.escet.cif.metamodel.cif.functions.InternalFunction;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.ComponentType;
import org.eclipse.escet.cif.metamodel.cif.types.DictType;
import org.eclipse.escet.cif.metamodel.cif.types.Field;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.cif.metamodel.cif.types.ListType;
import org.eclipse.escet.cif.metamodel.cif.types.RealType;
import org.eclipse.escet.cif.metamodel.cif.types.SetType;
import org.eclipse.escet.cif.metamodel.cif.types.StringType;
import org.eclipse.escet.cif.metamodel.cif.types.TupleType;
import org.eclipse.escet.cif.simulator.runtime.CifSimulatorMath;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Strings;

/**
 * Expression code generator. Based on the code in {@link CifEvalUtils}.
 *
 * @see CifEvalUtils
 * @see CifMath
 */
public class ExprCodeGenerator {
    /** Constructor for the {@link ExprCodeGenerator} class. */
    private ExprCodeGenerator() {
        // Static class.
    }

    /**
     * Generate a Java code fragment for the conjunction of the given predicates. If no predicates are given,
     * {@code "true"} is returned.
     *
     * @param preds The predicates.
     * @param ctxt The compiler context to use.
     * @param state The name of the state variable in the context where the generated code is used. May be {@code null}
     *     only if the context in which the expression occurs can not access the state.
     * @return The Java code that represents the conjunction of the given predicates.
     */
    public static String gencodePreds(List<Expression> preds, CifCompilerContext ctxt, String state) {
        return gencodePreds(preds, ctxt, state, "true");
    }

    /**
     * Generate a Java code fragment for the conjunction of the given predicates.
     *
     * @param preds The predicates.
     * @param ctxt The compiler context to use.
     * @param state The name of the state variable in the context where the generated code is used. May be {@code null}
     *     only if the context in which the expression occurs can not access the state.
     * @param noPredsCode The code that is returned when no predicates are given.
     * @return The Java code that represents the conjunction of the given predicates.
     */
    public static String gencodePreds(List<Expression> preds, CifCompilerContext ctxt, String state,
            String noPredsCode)
    {
        if (preds.isEmpty()) {
            return noPredsCode;
        }
        List<String> txts = listc(preds.size());
        for (Expression pred: preds) {
            String txt = gencodeExpr(pred, ctxt, state);
            if (preds.size() > 1) {
                txt = "(" + txt + ")";
            }
            txts.add(txt);
        }
        return StringUtils.join(txts, " && ");
    }

    /**
     * Generate a Java code fragment for the given expressions. The code for the individual expressions is combined
     * using {@code ", "} as separator.
     *
     * @param exprs The expressions.
     * @param ctxt The compiler context to use.
     * @param state The name of the state variable in the context where the generated code is used. May be {@code null}
     *     only if the context in which the expression occurs can not access the state.
     * @return The Java code that represents the given expression.
     */
    public static String gencodeExprs(List<Expression> exprs, CifCompilerContext ctxt, String state) {
        // Optimization for no expressions.
        if (exprs.isEmpty()) {
            return "";
        }

        // Optimization for single expression.
        if (exprs.size() == 1) {
            return gencodeExpr(first(exprs), ctxt, state);
        }

        // General case.
        List<String> txts = listc(exprs.size());
        for (Expression expr: exprs) {
            txts.add(gencodeExpr(expr, ctxt, state));
        }
        return StringUtils.join(txts, ", ");
    }

    /**
     * Generate a Java code fragment for the given expression.
     *
     * @param expr The expression.
     * @param ctxt The compiler context to use.
     * @param state The name of the state variable in the context where the generated code is used. May be {@code null}
     *     only if the context in which the expression occurs can not access the state.
     * @return The Java code that represents the given expression.
     */
    public static String gencodeExpr(Expression expr, CifCompilerContext ctxt, String state) {
        if (expr instanceof BoolExpression) {
            return ((BoolExpression)expr).isValue() ? "true" : "false";
        } else if (expr instanceof IntExpression) {
            return Integer.toString(((IntExpression)expr).getValue());
        } else if (expr instanceof RealExpression) {
            // Convert to 'double' to make sure that for instance 1e-999
            // becomes '0.0', since '1e-999' can not be represented as a
            // double, and causes a compilation error.
            String valueTxt = ((RealExpression)expr).getValue();
            double value = Double.parseDouble(valueTxt);
            return CifSimulatorMath.realToStr(value);
        } else if (expr instanceof StringExpression) {
            return Strings.stringToJava(((StringExpression)expr).getValue());
        } else if (expr instanceof TimeExpression) {
            return fmt("%s.%s.time", state, CONT_SUB_STATE_FIELD_NAME);
        } else if (expr instanceof CastExpression) {
            return gencodeCastExpr((CastExpression)expr, ctxt, state);
        } else if (expr instanceof UnaryExpression) {
            return gencodeUnaryExpr((UnaryExpression)expr, ctxt, state);
        } else if (expr instanceof BinaryExpression) {
            return gencodeBinaryExpr((BinaryExpression)expr, ctxt, state);
        } else if (expr instanceof IfExpression) {
            return gencodeIfExpr((IfExpression)expr, ctxt, state);
        } else if (expr instanceof SwitchExpression) {
            return gencodeSwitchExpr((SwitchExpression)expr, ctxt, state);
        } else if (expr instanceof ProjectionExpression) {
            return gencodeProjExpr((ProjectionExpression)expr, ctxt, state);
        } else if (expr instanceof SliceExpression) {
            return gencodeSliceExpr((SliceExpression)expr, ctxt, state);
        } else if (expr instanceof FunctionCallExpression) {
            return gencodeFuncCallExpr((FunctionCallExpression)expr, ctxt, state);
        } else if (expr instanceof ListExpression) {
            return gencodeListExpr((ListExpression)expr, ctxt, state);
        } else if (expr instanceof SetExpression) {
            return gencodeSetExpr((SetExpression)expr, ctxt, state);
        } else if (expr instanceof TupleExpression) {
            return gencodeTupleExpr((TupleExpression)expr, ctxt, state);
        } else if (expr instanceof DictExpression) {
            return gencodeDictExpr((DictExpression)expr, ctxt, state);
        } else if (expr instanceof ConstantExpression) {
            ConstantExpression cexpr = (ConstantExpression)expr;
            return ctxt.getConstFieldName(cexpr.getConstant());
        } else if (expr instanceof DiscVariableExpression) {
            return gencodeDiscVarExpr((DiscVariableExpression)expr, ctxt, state);
        } else if (expr instanceof InputVariableExpression) {
            return gencodeInputVarExpr((InputVariableExpression)expr, ctxt, state);
        } else if (expr instanceof AlgVariableExpression) {
            AlgVariable var = ((AlgVariableExpression)expr).getVariable();
            return fmt("%s(%s)", ctxt.getAlgVarMethodName(var), state);
        } else if (expr instanceof ContVariableExpression) {
            return gencodeContVarExpr((ContVariableExpression)expr, ctxt, state);
        } else if (expr instanceof LocationExpression) {
            return gencodeLocExpr((LocationExpression)expr, ctxt, state);
        } else if (expr instanceof EnumLiteralExpression) {
            EnumLiteral lit = ((EnumLiteralExpression)expr).getLiteral();
            EnumDecl enumDecl = (EnumDecl)lit.eContainer();
            return fmt("%s.%s", ctxt.getEnumClassName(enumDecl), ctxt.getEnumConstName(lit));
        } else if (expr instanceof FunctionExpression) {
            Function func = ((FunctionExpression)expr).getFunction();
            return fmt("%s.%s", ctxt.getFuncClassName(func), ctxt.getFuncFieldName(func));
        } else if (expr instanceof EventExpression) {
            // Can't use event as value. Disallowed by type checker.
            throw new RuntimeException("Event used as value: " + expr);
        } else if (expr instanceof ReceivedExpression) {
            return RCVD_VALUE_VAR_NAME;
        } else if (expr instanceof SelfExpression) {
            // Should be handled as special case where allowed.
            throw new RuntimeException("Self expr unexpected.");
        } else if (expr instanceof ComponentExpression) {
            // Should be handled as special case where allowed.
            throw new RuntimeException("Component expr unexpected.");
        } else {
            throw new RuntimeException("Unexpected expr: " + expr);
        }
    }

    /**
     * Generate a Java code fragment for the given cast expression.
     *
     * @param expr The expression.
     * @param ctxt The compiler context to use.
     * @param state The name of the state variable in the context where the generated code is used. May be {@code null}
     *     only if the context in which the expression occurs can not access the state.
     * @return The Java code that represents the given expression.
     */
    private static String gencodeCastExpr(CastExpression expr, CifCompilerContext ctxt, String state) {
        // Handle cast from automaton reference to string as special case.
        Expression child = expr.getChild();
        if (CifTypeUtils.isAutRefExpr(child)) {
            // Get automaton index.
            CifType ctype = child.getType();
            Assert.check(ctype instanceof ComponentType);
            Automaton aut = (Automaton)((ComponentType)ctype).getComponent();
            int idx = ctxt.getAutomata().indexOf(aut);

            // Return code.
            return fmt("%s.getAutCurLocName(%d)", state, idx);
        }

        // Normal case: get child code.
        String crslt = gencodeExpr(expr.getChild(), ctxt, state);
        CifType nctype = normalizeType(expr.getChild().getType());

        // Convert based on child/result type combination.
        CifType ntype = normalizeType(expr.getType());
        if (nctype instanceof IntType && ntype instanceof RealType) {
            return fmt("intToReal(%s)", crslt);
        } else if (nctype instanceof IntType && ntype instanceof StringType) {
            return fmt("intToStr(%s)", crslt);
        } else if (nctype instanceof RealType && ntype instanceof StringType) {
            return fmt("realToStr(%s)", crslt);
        } else if (nctype instanceof BoolType && ntype instanceof StringType) {
            return fmt("boolToStr(%s)", crslt);
        } else if (nctype instanceof StringType && ntype instanceof IntType) {
            return fmt("strToInt(%s)", crslt);
        } else if (nctype instanceof StringType && ntype instanceof RealType) {
            return fmt("strToReal(%s)", crslt);
        } else if (nctype instanceof StringType && ntype instanceof BoolType) {
            return fmt("strToBool(%s)", crslt);
        } else if (CifTypeUtils.checkTypeCompat(nctype, ntype, RangeCompat.EQUAL)) {
            // Ignore cast to child type.
            return crslt;
        } else {
            String msg = "Unknown cast: " + nctype + ", " + ntype;
            throw new RuntimeException(msg);
        }
    }

    /**
     * Generate a Java code fragment for the given unary expression.
     *
     * @param expr The expression.
     * @param ctxt The compiler context to use.
     * @param state The name of the state variable in the context where the generated code is used. May be {@code null}
     *     only if the context in which the expression occurs can not access the state.
     * @return The Java code that represents the given expression.
     */
    private static String gencodeUnaryExpr(UnaryExpression expr, CifCompilerContext ctxt, String state) {
        // Get child code.
        String crslt = gencodeExpr(expr.getChild(), ctxt, state);

        // Convert based on operator.
        switch (expr.getOperator()) {
            case INVERSE:
                return fmt("!(%s)", crslt);

            case NEGATE:
                return fmt("negate(%s)", crslt);

            case PLUS:
                // Discard the '+'. No overflow etc possible.
                return crslt;

            case SAMPLE: {
                ctxt.needSampler = true;
                return fmt("Sampler.sample(%s)", crslt);
            }
        }

        // Should never get here.
        throw new RuntimeException("Unknown unop: " + expr.getOperator());
    }

    /**
     * Generate a Java code fragment for the given binary expression.
     *
     * @param expr The expression.
     * @param ctxt The compiler context to use.
     * @param state The name of the state variable in the context where the generated code is used. May be {@code null}
     *     only if the context in which the expression occurs can not access the state.
     * @return The Java code that represents the given expression.
     */
    private static String gencodeBinaryExpr(BinaryExpression expr, CifCompilerContext ctxt, String state) {
        // Get children code.
        String lrslt = gencodeExpr(expr.getLeft(), ctxt, state);
        String rrslt = gencodeExpr(expr.getRight(), ctxt, state);

        // Convert based on operator.
        switch (expr.getOperator()) {
            case IMPLICATION:
                // Short circuit evaluation.
                return fmt("!(%s) || (%s)", lrslt, rrslt);

            case BI_CONDITIONAL:
                // Use 'equal' instead of '==' to avoid object equality for
                // two Boolean objects.
                return fmt("equal(%s, %s)", lrslt, rrslt);

            case DISJUNCTION: {
                CifType nltype = normalizeType(expr.getLeft().getType());
                if (nltype instanceof BoolType) {
                    // Short circuit evaluation.
                    return fmt("(%s) || (%s)", lrslt, rrslt);
                } else {
                    Assert.check(nltype instanceof SetType);
                    return fmt("union(%s, %s)", lrslt, rrslt);
                }
            }

            case CONJUNCTION: {
                CifType nltype = normalizeType(expr.getLeft().getType());
                if (nltype instanceof BoolType) {
                    // Short circuit evaluation.
                    return fmt("(%s) && (%s)", lrslt, rrslt);
                } else {
                    Assert.check(nltype instanceof SetType);
                    return fmt("intersection(%s, %s)", lrslt, rrslt);
                }
            }

            case LESS_THAN:
                return fmt("(%s) < (%s)", lrslt, rrslt);

            case LESS_EQUAL:
                return fmt("(%s) <= (%s)", lrslt, rrslt);

            case GREATER_THAN:
                return fmt("(%s) > (%s)", lrslt, rrslt);

            case GREATER_EQUAL:
                return fmt("(%s) >= (%s)", lrslt, rrslt);

            case EQUAL:
                return fmt("equal(%s, %s)", lrslt, rrslt);

            case UNEQUAL:
                return fmt("!equal(%s, %s)", lrslt, rrslt);

            case ADDITION: {
                CifType nltype = normalizeType(expr.getLeft().getType());
                CifType nrtype = normalizeType(expr.getRight().getType());
                if (nltype instanceof RealType) {
                    return fmt("addReal(%s, %s)", lrslt, rrslt);
                } else if (nrtype instanceof RealType) {
                    return fmt("addReal(%s, %s)", lrslt, rrslt);
                } else if (nltype instanceof ListType) {
                    return fmt("addList(%s, %s)", lrslt, rrslt);
                } else if (nltype instanceof StringType) {
                    return fmt("addString(%s, %s)", lrslt, rrslt);
                } else if (nltype instanceof DictType) {
                    return fmt("addDict(%s, %s)", lrslt, rrslt);
                } else {
                    return fmt("addInt(%s, %s)", lrslt, rrslt);
                }
            }

            case SUBTRACTION:
                return fmt("subtract(%s, %s)", lrslt, rrslt);

            case MULTIPLICATION:
                return fmt("multiply(%s, %s)", lrslt, rrslt);

            case DIVISION:
                return fmt("divide(%s, %s)", lrslt, rrslt);

            case INTEGER_DIVISION:
                return fmt("div(%s, %s)", lrslt, rrslt);

            case MODULUS:
                return fmt("mod(%s, %s)", lrslt, rrslt);

            case SUBSET:
                return fmt("subset(%s, %s)", lrslt, rrslt);

            case ELEMENT_OF:
                return fmt("in(%s, %s)", lrslt, rrslt);
        }

        // Should never get here.
        throw new RuntimeException("Unknown binop: " + expr.getOperator());
    }

    /**
     * Generate a Java code fragment for the given 'if' expression.
     *
     * @param expr The expression.
     * @param ctxt The compiler context to use.
     * @param state The name of the state variable in the context where the generated code is used. May be {@code null}
     *     only if the context in which the expression occurs can not access the state.
     * @return The Java code that represents the given expression.
     */
    private static String gencodeIfExpr(IfExpression expr, CifCompilerContext ctxt, String state) {
        // Start with 'else'.
        String rslt = gencodeExpr(expr.getElse(), ctxt, state);

        // Wrap 'elifs' around else.
        for (int i = expr.getElifs().size() - 1; i >= 0; i--) {
            ElifExpression elif = expr.getElifs().get(i);
            rslt = fmt("(%s) ? %s : (%s)", gencodePreds(elif.getGuards(), ctxt, state),
                    gencodeExpr(elif.getThen(), ctxt, state), rslt);
        }

        // Wrap 'if' around 'elifs/else'.
        rslt = fmt("(%s) ? %s : (%s)", gencodePreds(expr.getGuards(), ctxt, state),
                gencodeExpr(expr.getThen(), ctxt, state), rslt);

        // Return final result.
        return rslt;
    }

    /**
     * Generate a Java code fragment for the given 'switch' expression.
     *
     * @param expr The expression.
     * @param ctxt The compiler context to use.
     * @param state The name of the state variable in the context where the generated code is used. May be {@code null}
     *     only if the context in which the expression occurs can not access the state.
     * @return The Java code that represents the given expression.
     */
    private static String gencodeSwitchExpr(SwitchExpression expr, CifCompilerContext ctxt, String state) {
        // Generate code for the control value.
        Expression value = expr.getValue();
        boolean isAutRef = CifTypeUtils.isAutRefExpr(value);

        String valueTxt;
        if (isAutRef) {
            // Control value not needed. The metamodel contains location
            // references, for which we can generate 'loc_pointer == loc_value'
            // code directly.
            valueTxt = null;
        } else {
            // Generate code for the 'normal' expression.
            valueTxt = gencodeExpr(value, ctxt, state);
        }

        // Start with last case. Ignore key, if present. This assumes that
        // the switch always has at least one case that matches, which is
        // a language constraint, and is enforced by the type checker. Ignoring
        // the key means that it is not compared to the control value, and this
        // may lead to the control value of the 'switch' expression not being
        // evaluated, and may thus hide evaluation errors.
        List<SwitchCase> cases = expr.getCases();
        String rslt = gencodeExpr(last(cases).getValue(), ctxt, state);

        // Wrap other cases around it.
        for (int i = cases.size() - 2; i >= 0; i--) {
            // Generate code for the key.
            SwitchCase cse = cases.get(i);
            Expression key = cse.getKey();
            Assert.notNull(key);

            String keyTxt = gencodeExpr(key, ctxt, state);
            if (valueTxt != null) {
                keyTxt = fmt("equal(%s, %s)", valueTxt, keyTxt);
            }

            // Wrap result code for this case.
            rslt = fmt("(%s) ? %s : (%s)", keyTxt, gencodeExpr(cse.getValue(), ctxt, state), rslt);
        }

        // Return final result.
        return rslt;
    }

    /**
     * Generate a Java code fragment for the given projection expression.
     *
     * @param expr The expression.
     * @param ctxt The compiler context to use.
     * @param state The name of the state variable in the context where the generated code is used. May be {@code null}
     *     only if the context in which the expression occurs can not access the state.
     * @return The Java code that represents the given expression.
     */
    private static String gencodeProjExpr(ProjectionExpression expr, CifCompilerContext ctxt, String state) {
        // Get child code.
        String crslt = gencodeExpr(expr.getChild(), ctxt, state);

        // Special case for tuple field projection.
        CifType nctype = normalizeType(expr.getChild().getType());
        if (nctype instanceof TupleType && expr.getIndex() instanceof FieldExpression) {
            // Get field (name).
            Field field = ((FieldExpression)expr.getIndex()).getField();
            String fieldName = ctxt.getTupleTypeFieldFieldName(field);
            return fmt("(%s).%s", crslt, fieldName);
        }

        // Case distinction on child.
        if (nctype instanceof TupleType) {
            // Get field index for tuple index projection. Index is valid:
            // type checker already checked it.
            int idx;
            try {
                idx = (Integer)CifEvalUtils.eval(expr.getIndex(), false);
            } catch (CifEvalException e) {
                // Should never fail: type checker already evaluated this.
                throw new RuntimeException(e);
            }

            // Generate and return projection code.
            TupleType tupleType = (TupleType)nctype;
            String fieldName = ctxt.getTupleTypeFieldFieldName(tupleType, idx);
            return fmt("(%s).%s", crslt, fieldName);
        } else {
            // List, dictionary, and string.
            String irslt = gencodeExpr(expr.getIndex(), ctxt, state);
            return fmt("project(%s, %s)", crslt, irslt);
        }
    }

    /**
     * Generate a Java code fragment for the given slice expression.
     *
     * @param expr The expression.
     * @param ctxt The compiler context to use.
     * @param state The name of the state variable in the context where the generated code is used. May be {@code null}
     *     only if the context in which the expression occurs can not access the state.
     * @return The Java code that represents the given expression.
     */
    private static String gencodeSliceExpr(SliceExpression expr, CifCompilerContext ctxt, String state) {
        String crslt = gencodeExpr(expr.getChild(), ctxt, state);
        String begin = (expr.getBegin() == null) ? "null" : gencodeExpr(expr.getBegin(), ctxt, state);
        String end = (expr.getEnd() == null) ? "null" : gencodeExpr(expr.getEnd(), ctxt, state);
        return fmt("slice(%s, %s, %s)", crslt, begin, end);
    }

    /**
     * Generate a Java code fragment for the given function call expression.
     *
     * @param expr The expression.
     * @param ctxt The compiler context to use.
     * @param state The name of the state variable in the context where the generated code is used. May be {@code null}
     *     only if the context in which the expression occurs can not access the state.
     * @return The Java code that represents the given expression.
     */
    private static String gencodeFuncCallExpr(FunctionCallExpression expr, CifCompilerContext ctxt, String state) {
        // User-defined functions.
        if (!(expr.getFunction() instanceof StdLibFunctionExpression)) {
            String argsTxt = gencodeExprs(expr.getParams(), ctxt, state);
            return fmt("(%s).evalFunc(%s)", gencodeExpr(expr.getFunction(), ctxt, state), argsTxt);
        }

        // Get standard library function.
        StdLibFunctionExpression stdlibExpr = (StdLibFunctionExpression)expr.getFunction();
        StdLibFunction stdlib = stdlibExpr.getFunction();

        // Special case for 'fmt'.
        if (stdlib == StdLibFunction.FORMAT) {
            // Get pattern.
            Expression patternExpr = expr.getParams().get(0);
            String pattern = ((StringExpression)patternExpr).getValue();

            // Generate code for the values (remaining arguments), and also get
            // their types.
            List<String> valueTxts = listc(expr.getParams().size() - 1);
            List<CifType> valueTypes = listc(expr.getParams().size() - 1);
            for (int i = 1; i < expr.getParams().size(); i++) {
                Expression value = expr.getParams().get(i);
                valueTxts.add(gencodeExpr(value, ctxt, state));
                valueTypes.add(value.getType());
            }

            // Generate code for the pattern.
            return gencodePattern(pattern, valueTxts, valueTypes);
        }

        // Generate standard library function call code.
        String argsTxt = gencodeExprs(expr.getParams(), ctxt, state);
        switch (stdlib) {
            case ACOSH:
                return fmt("acosh(%s)", argsTxt);

            case ACOS:
                return fmt("acos(%s)", argsTxt);

            case ASINH:
                return fmt("asinh(%s)", argsTxt);

            case ASIN:
                return fmt("asin(%s)", argsTxt);

            case ATANH:
                return fmt("atanh(%s)", argsTxt);

            case ATAN:
                return fmt("atan(%s)", argsTxt);

            case COSH:
                return fmt("cosh(%s)", argsTxt);

            case COS:
                return fmt("cos(%s)", argsTxt);

            case SINH:
                return fmt("sinh(%s)", argsTxt);

            case SIN:
                return fmt("sin(%s)", argsTxt);

            case TANH:
                return fmt("tanh(%s)", argsTxt);

            case TAN:
                return fmt("tan(%s)", argsTxt);

            case ABS:
                return fmt("abs(%s)", argsTxt);

            case CBRT:
                return fmt("cbrt(%s)", argsTxt);

            case CEIL:
                return fmt("ceil(%s)", argsTxt);

            case DELETE:
                return fmt("delete(%s)", argsTxt);

            case EMPTY:
                return fmt("empty(%s)", argsTxt);

            case EXP:
                return fmt("exp(%s)", argsTxt);

            case FLOOR:
                return fmt("floor(%s)", argsTxt);

            case FORMAT:
                throw new RuntimeException("Already handled above: " + stdlib);

            case LN:
                return fmt("ln(%s)", argsTxt);

            case LOG:
                return fmt("log(%s)", argsTxt);

            case MAXIMUM:
                return fmt("max(%s)", argsTxt);

            case MINIMUM:
                return fmt("min(%s)", argsTxt);

            case POP: {
                // Get tuple type for result, and generate code for it.
                TupleType rsltType = (TupleType)normalizeType(expr.getType());
                String className = ctxt.getTupleTypeClassName(rsltType);

                // Generate code for the 'pop' function call.
                return fmt("%s.pop(%s)", className, argsTxt);
            }

            case POWER: {
                CifType rsltType = normalizeType(expr.getType());

                if (rsltType instanceof IntType) {
                    return fmt("powInt(%s)", argsTxt);
                } else {
                    return fmt("powReal(%s)", argsTxt);
                }
            }

            case ROUND:
                return fmt("round(%s)", argsTxt);

            case SCALE:
                return fmt("scale(%s)", argsTxt);

            case SIGN:
                return fmt("sign(%s)", argsTxt);

            case SIZE:
                return fmt("size(%s)", argsTxt);

            case SQRT:
                return fmt("sqrt(%s)", argsTxt);

            case BERNOULLI:
                return fmt("new BernoulliDistribution(new CifMersenneTwister(%s.spec.getNextSeed()), %s)", state,
                        argsTxt);

            case BETA:
                return fmt("new BetaDistribution(new CifMersenneTwister(%s.spec.getNextSeed()), %s)", state, argsTxt);

            case BINOMIAL:
                return fmt("new BinomialDistribution(new CifMersenneTwister(%s.spec.getNextSeed()), %s)", state,
                        argsTxt);

            case CONSTANT: {
                Expression arg = first(expr.getParams());
                CifType argType = normalizeType(arg.getType());

                String className;
                if (argType instanceof BoolType) {
                    className = "ConstantBooleanDistribution";
                } else if (argType instanceof IntType) {
                    className = "ConstantIntegerDistribution";
                } else if (argType instanceof RealType) {
                    className = "ConstantRealDistribution";
                } else {
                    String msg = "Unknown constant distribution: " + argType;
                    throw new RuntimeException(msg);
                }

                return fmt("new %s(%s)", className, argsTxt);
            }

            case ERLANG:
                return fmt("new ErlangDistribution(new CifMersenneTwister(%s.spec.getNextSeed()), %s)", state, argsTxt);

            case EXPONENTIAL:
                return fmt("new ExponentialDistribution(new CifMersenneTwister(%s.spec.getNextSeed()), %s)", state,
                        argsTxt);

            case GAMMA:
                return fmt("new GammaDistribution(new CifMersenneTwister(%s.spec.getNextSeed()), %s)", state, argsTxt);

            case GEOMETRIC:
                return fmt("new GeometricDistribution(new CifMersenneTwister(%s.spec.getNextSeed()), %s)", state,
                        argsTxt);

            case LOG_NORMAL:
                return fmt("new LogNormalDistribution(new CifMersenneTwister(%s.spec.getNextSeed()), %s)", state,
                        argsTxt);

            case NORMAL:
                return fmt("new NormalDistribution(new CifMersenneTwister(%s.spec.getNextSeed()), %s)", state, argsTxt);

            case POISSON:
                return fmt("new PoissonDistribution(new CifMersenneTwister(%s.spec.getNextSeed()), %s)", state,
                        argsTxt);

            case RANDOM:
                return fmt("new RandomDistribution(new CifMersenneTwister(%s.spec.getNextSeed()))", state);

            case TRIANGLE:
                return fmt("new TriangleDistribution(new CifMersenneTwister(%s.spec.getNextSeed()), %s)", state,
                        argsTxt);

            case UNIFORM: {
                Expression arg = first(expr.getParams());
                CifType argType = normalizeType(arg.getType());

                String className;
                if (argType instanceof IntType) {
                    className = "UniformIntegerDistribution";
                } else if (argType instanceof RealType) {
                    className = "UniformRealDistribution";
                } else {
                    String msg = "Unknown uniform distribution: " + argType;
                    throw new RuntimeException(msg);
                }

                return fmt("new %s(new CifMersenneTwister(%s.spec.getNextSeed()), %s)", className, state, argsTxt);
            }

            case WEIBULL:
                return fmt("new WeibullDistribution(new CifMersenneTwister(%s.spec.getNextSeed()), %s)", state,
                        argsTxt);
        }

        // Should never get here.
        throw new RuntimeException("Unknown stdlib func: " + stdlib);
    }

    /**
     * Generate a Java code fragment for the given list expression.
     *
     * @param expr The expression.
     * @param ctxt The compiler context to use.
     * @param state The name of the state variable in the context where the generated code is used. May be {@code null}
     *     only if the context in which the expression occurs can not access the state.
     * @return The Java code that represents the given expression.
     */
    private static String gencodeListExpr(ListExpression expr, CifCompilerContext ctxt, String state) {
        // Don't generate code for large list literals. Generate a data file
        // and read it again at runtime. Prevents generating so much Java code
        // that the Java compiler can't compile it.
        if (expr.getElements().size() >= 100 && isSerializableLiteral(expr)) {
            return gencodeLiteral(expr, ctxt);
        }

        // Get list type and element type.
        ListType ltype = (ListType)normalizeType(expr.getType());
        CifType etype = ltype.getElementType();

        // Generate list construction code.
        int size = expr.getElements().size();
        String rslt = fmt("new ArrayList<%s>(%d)", gencodeType(etype, ctxt, true), size);

        // Special case for empty lists.
        if (expr.getElements().isEmpty()) {
            return rslt;
        }

        // For non-empty lists, add the elements.
        String elemTxt = gencodeExprs(expr.getElements(), ctxt, state);
        return fmt("makelist(%s, %s)", rslt, elemTxt);
    }

    /**
     * Generate a Java code fragment for the given set expression.
     *
     * @param expr The expression.
     * @param ctxt The compiler context to use.
     * @param state The name of the state variable in the context where the generated code is used. May be {@code null}
     *     only if the context in which the expression occurs can not access the state.
     * @return The Java code that represents the given expression.
     */
    private static String gencodeSetExpr(SetExpression expr, CifCompilerContext ctxt, String state) {
        // Don't generate code for large set literals. Generate a data file
        // and read it again at runtime. Prevents generating so much Java code
        // that the Java compiler can't compile it.
        if (expr.getElements().size() >= 100 && isSerializableLiteral(expr)) {
            return gencodeLiteral(expr, ctxt);
        }

        // Get set type and element type.
        SetType stype = (SetType)normalizeType(expr.getType());
        CifType etype = stype.getElementType();

        // Generate set construction code. Use LinkedHashSet for determinism.
        int size = expr.getElements().size();
        String rslt = fmt("new LinkedHashSet<%s>(%d)", gencodeType(etype, ctxt, true), size);

        // Special case for empty set.
        if (expr.getElements().isEmpty()) {
            return rslt;
        }

        // For non-empty sets, add the elements.
        String elemTxt = gencodeExprs(expr.getElements(), ctxt, state);
        return fmt("makeset(%s, %s)", rslt, elemTxt);
    }

    /**
     * Generate a Java code fragment for the given tuple expression.
     *
     * @param expr The expression.
     * @param ctxt The compiler context to use.
     * @param state The name of the state variable in the context where the generated code is used. May be {@code null}
     *     only if the context in which the expression occurs can not access the state.
     * @return The Java code that represents the given expression.
     */
    private static String gencodeTupleExpr(TupleExpression expr, CifCompilerContext ctxt, String state) {
        // Get tuple type class name.
        TupleType tupleType = (TupleType)normalizeType(expr.getType());
        String className = ctxt.getTupleTypeClassName(tupleType);

        // Generate constructor call code.
        return fmt("new %s(%s)", className, gencodeExprs(expr.getFields(), ctxt, state));
    }

    /**
     * Generate a Java code fragment for the given dictionary expression.
     *
     * @param expr The expression.
     * @param ctxt The compiler context to use.
     * @param state The name of the state variable in the context where the generated code is used. May be {@code null}
     *     only if the context in which the expression occurs can not access the state.
     * @return The Java code that represents the given expression.
     */
    private static String gencodeDictExpr(DictExpression expr, CifCompilerContext ctxt, String state) {
        // Don't generate code for large dictionary literals. Generate a data
        // file and read it again at runtime. Prevents generating so much Java
        // code that the Java compiler can't compile it.
        if (expr.getPairs().size() >= 100 && isSerializableLiteral(expr)) {
            return gencodeLiteral(expr, ctxt);
        }

        // Get dictionary type, key type, and value type.
        DictType dtype = (DictType)normalizeType(expr.getType());
        CifType ktype = dtype.getKeyType();
        CifType vtype = dtype.getValueType();

        // Generate dictionary construction code. Use LinkedHashMap for
        // determinism.
        List<DictPair> pairs = expr.getPairs();
        int size = pairs.size();
        String ktypeTxt = gencodeType(ktype, ctxt, true);
        String vtypeTxt = gencodeType(vtype, ctxt, true);
        String rslt = fmt("new LinkedHashMap<%s, %s>(%d)", ktypeTxt, vtypeTxt, size);

        // Generate code for the keys and values of the pairs.
        List<String> keyTxts = listc(pairs.size());
        List<String> valueTxts = listc(pairs.size());
        for (DictPair pair: pairs) {
            keyTxts.add(gencodeExpr(pair.getKey(), ctxt, state));
            valueTxts.add(gencodeExpr(pair.getValue(), ctxt, state));
        }

        // Generate code for key/value arrays.
        String keysTxt = fmt("array(%s)", StringUtils.join(keyTxts, ", "));
        String valuesTxt = fmt("array(%s)", StringUtils.join(valueTxts, ", "));

        // Return the code for the dictionary literal.
        return fmt("addpairs(%s, %s, %s)", rslt, keysTxt, valuesTxt);
    }

    /**
     * Generate a Java code fragment for the given discrete variable expression.
     *
     * @param expr The expression.
     * @param ctxt The compiler context to use.
     * @param state The name of the state variable in the context where the generated code is used. May be {@code null}
     *     only if the context in which the expression occurs can not access the state.
     * @return The Java code that represents the given expression.
     */
    private static String gencodeDiscVarExpr(DiscVariableExpression expr, CifCompilerContext ctxt, String state) {
        DiscVariable var = expr.getVariable();
        EObject parent = var.eContainer();
        if (parent instanceof ComplexComponent) {
            return fmt("%s.%s.%s", state, ctxt.getAutSubStateFieldName((Automaton)parent),
                    ctxt.getDiscVarFieldName(var));
        } else if (parent instanceof FunctionParameter) {
            return ctxt.getFuncParamMethodParamName(var);
        } else if (parent instanceof InternalFunction) {
            return ctxt.getFuncLocalVarName(var);
        } else {
            throw new RuntimeException("Unknown disc var parent: " + parent);
        }
    }

    /**
     * Generate a Java code fragment for the given input variable expression.
     *
     * @param expr The expression.
     * @param ctxt The compiler context to use.
     * @param state The name of the state variable in the context where the generated code is used. May be {@code null}
     *     only if the context in which the expression occurs can not access the state.
     * @return The Java code that represents the given expression.
     */
    private static String gencodeInputVarExpr(InputVariableExpression expr, CifCompilerContext ctxt, String state) {
        InputVariable var = expr.getVariable();
        return fmt("%s.%s.%s", state, ctxt.getInputVarSubStateName(var), ctxt.getInputVarFieldName(var));
    }

    /**
     * Generate a Java code fragment for the given continuous variable expression.
     *
     * @param expr The expression.
     * @param ctxt The compiler context to use.
     * @param state The name of the state variable in the context where the generated code is used. May be {@code null}
     *     only if the context in which the expression occurs can not access the state.
     * @return The Java code that represents the given expression.
     */
    private static String gencodeContVarExpr(ContVariableExpression expr, CifCompilerContext ctxt, String state) {
        ContVariable var = expr.getVariable();

        if (expr.isDerivative()) {
            // Derivative reference.
            return fmt("Derivatives.%s(%s)", ctxt.getDerivativeMethodName(var), state);
        } else {
            // Continuous variable reference.
            return fmt("%s.%s.%s", state, ctxt.getContVarSubStateName(var), ctxt.getContVarFieldName(var));
        }
    }

    /**
     * Generate a Java code fragment for the given location expression.
     *
     * @param expr The expression.
     * @param ctxt The compiler context to use.
     * @param state The name of the state variable in the context where the generated code is used. May be {@code null}
     *     only if the context in which the expression occurs can not access the state.
     * @return The Java code that represents the given expression.
     */
    private static String gencodeLocExpr(LocationExpression expr, CifCompilerContext ctxt, String state) {
        Location loc = expr.getLocation();
        Automaton aut = (Automaton)loc.eContainer();
        return fmt("%s.%s.%s == %s", state, ctxt.getAutSubStateFieldName(aut), ctxt.getLocationPointerFieldName(aut),
                ctxt.getLocationValueText(loc));
    }
}
