//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBoolType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newIntType;
import static org.eclipse.escet.cif.simulator.compiler.CifCompilerContext.CONT_SUB_STATE_FIELD_NAME;
import static org.eclipse.escet.cif.simulator.compiler.CifCompilerContext.INPUT_SUB_STATE_FIELD_NAME;
import static org.eclipse.escet.cif.simulator.compiler.CifCompilerContext.RCVD_VALUE_VAR_NAME;
import static org.eclipse.escet.cif.simulator.compiler.CifFormatPatternCodeGenerator.gencodePattern;
import static org.eclipse.escet.cif.simulator.compiler.ExprCodeGeneratorResult.merge;
import static org.eclipse.escet.cif.simulator.compiler.LiteralCodeGenerator.gencodeLiteral;
import static org.eclipse.escet.cif.simulator.compiler.LiteralCodeGenerator.isSerializableLiteral;
import static org.eclipse.escet.cif.simulator.compiler.TypeCodeGenerator.gencodeType;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.last;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Collections;
import java.util.List;

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
    public static ExprCodeGeneratorResult gencodePreds(List<Expression> preds, CifCompilerContext ctxt, String state) {
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
    public static ExprCodeGeneratorResult gencodePreds(List<Expression> preds, CifCompilerContext ctxt, String state,
            String noPredsCode)
    {
        // Handle no predicate case.
        if (preds.isEmpty()) {
            return new ExprCodeGeneratorResult(noPredsCode, newBoolType());
        }

        // Optimization for single predicate.
        ExprCodeGeneratorResult rslt = gencodeExpr(preds.get(0), ctxt, state);
        if (preds.size() == 1) {
            return rslt;
        }

        // General case.
        rslt = merge("(%s)", preds.get(0).getType(), ctxt, rslt);
        for (int i = 1; i < preds.size(); i++) {
            ExprCodeGeneratorResult prslt = gencodeExpr(preds.get(i), ctxt, state);
            rslt = merge("%s && (%s)", newBoolType(), ctxt, rslt, prslt);
        }
        return rslt;
    }

    /**
     * Generate Java code fragments for the given expressions.
     *
     * @param exprs The expressions.
     * @param ctxt The compiler context to use.
     * @param state The name of the state variable in the context where the generated code is used. May be {@code null}
     *     only if the context in which the expression occurs can not access the state.
     * @return The Java code fragments that represent the given expressions.
     */
    public static List<ExprCodeGeneratorResult> gencodeExprs(List<Expression> exprs, CifCompilerContext ctxt,
            String state)
    {
        List<ExprCodeGeneratorResult> rslts = listc(exprs.size());
        for (Expression expr: exprs) {
            rslts.add(gencodeExpr(expr, ctxt, state));
        }
        return rslts;
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
    public static ExprCodeGeneratorResult gencodeExpr(Expression expr, CifCompilerContext ctxt, String state) {
        if (expr instanceof BoolExpression) {
            String result = ((BoolExpression)expr).isValue() ? "true" : "false";
            return new ExprCodeGeneratorResult(result, expr.getType());
        } else if (expr instanceof IntExpression) {
            return new ExprCodeGeneratorResult(Integer.toString(((IntExpression)expr).getValue()), expr.getType());
        } else if (expr instanceof RealExpression) {
            // Convert to 'double' to make sure that for instance 1e-999
            // becomes '0.0', since '1e-999' can not be represented as a
            // double, and causes a compilation error.
            String valueTxt = ((RealExpression)expr).getValue();
            double value = Double.parseDouble(valueTxt);
            return new ExprCodeGeneratorResult(CifSimulatorMath.realToStr(value), expr.getType());
        } else if (expr instanceof StringExpression) {
            return new ExprCodeGeneratorResult(Strings.stringToJava(((StringExpression)expr).getValue()),
                    expr.getType());
        } else if (expr instanceof TimeExpression) {
            return new ExprCodeGeneratorResult(fmt("%s.%s.time", state, CONT_SUB_STATE_FIELD_NAME), expr.getType());
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
            return new ExprCodeGeneratorResult(ctxt.getConstFieldName(cexpr.getConstant()), expr.getType());
        } else if (expr instanceof DiscVariableExpression) {
            return gencodeDiscVarExpr((DiscVariableExpression)expr, ctxt, state);
        } else if (expr instanceof InputVariableExpression) {
            return gencodeInputVarExpr((InputVariableExpression)expr, ctxt, state);
        } else if (expr instanceof AlgVariableExpression) {
            AlgVariable var = ((AlgVariableExpression)expr).getVariable();
            return new ExprCodeGeneratorResult(fmt("%s(%s)", ctxt.getAlgVarMethodName(var), state), expr.getType());
        } else if (expr instanceof ContVariableExpression) {
            return gencodeContVarExpr((ContVariableExpression)expr, ctxt, state);
        } else if (expr instanceof LocationExpression) {
            return gencodeLocExpr((LocationExpression)expr, ctxt, state);
        } else if (expr instanceof EnumLiteralExpression) {
            EnumLiteral lit = ((EnumLiteralExpression)expr).getLiteral();
            EnumDecl enumDecl = (EnumDecl)lit.eContainer();
            return new ExprCodeGeneratorResult(
                    fmt("%s.%s", ctxt.getEnumClassName(enumDecl), ctxt.getEnumConstName(lit)), expr.getType());
        } else if (expr instanceof FunctionExpression) {
            Function func = ((FunctionExpression)expr).getFunction();
            return new ExprCodeGeneratorResult(fmt("%s.%s", ctxt.getFuncClassName(func), ctxt.getFuncFieldName(func)),
                    expr.getType());
        } else if (expr instanceof EventExpression) {
            // Can't use event as value. Disallowed by type checker.
            throw new RuntimeException("Event used as value: " + expr);
        } else if (expr instanceof ReceivedExpression) {
            return new ExprCodeGeneratorResult(RCVD_VALUE_VAR_NAME, expr.getType());
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
    private static ExprCodeGeneratorResult gencodeCastExpr(CastExpression expr, CifCompilerContext ctxt, String state) {
        // Handle cast from automaton reference to string as special case.
        Expression child = expr.getChild();
        if (CifTypeUtils.isAutRefExpr(child)) {
            // Get automaton index.
            CifType ctype = child.getType();
            Assert.check(ctype instanceof ComponentType);
            Automaton aut = (Automaton)((ComponentType)ctype).getComponent();
            int idx = ctxt.getAutomata().indexOf(aut);

            // Return code.
            return new ExprCodeGeneratorResult(fmt("%s.getAutCurLocName(%d)", state, idx), expr.getType());
        }

        // Normal case: get child code.
        ExprCodeGeneratorResult crslt = gencodeExpr(expr.getChild(), ctxt, state);
        CifType nctype = normalizeType(expr.getChild().getType());

        // Convert based on child/result type combination.
        CifType ntype = normalizeType(expr.getType());
        String text;
        if (nctype instanceof IntType && ntype instanceof RealType) {
            text = "intToReal(%s)";
        } else if (nctype instanceof IntType && ntype instanceof StringType) {
            text = "intToStr(%s)";
        } else if (nctype instanceof RealType && ntype instanceof StringType) {
            text = "realToStr(%s)";
        } else if (nctype instanceof BoolType && ntype instanceof StringType) {
            text = "boolToStr(%s)";
        } else if (nctype instanceof StringType && ntype instanceof IntType) {
            text = "strToInt(%s)";
        } else if (nctype instanceof StringType && ntype instanceof RealType) {
            text = "strToReal(%s)";
        } else if (nctype instanceof StringType && ntype instanceof BoolType) {
            text = "strToBool(%s)";
        } else if (CifTypeUtils.checkTypeCompat(nctype, ntype, RangeCompat.EQUAL)) {
            // Ignore cast to child type.
            return crslt;
        } else {
            String msg = "Unknown cast: " + nctype + ", " + ntype;
            throw new RuntimeException(msg);
        }

        return merge(text, expr.getType(), ctxt, crslt);
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
    private static ExprCodeGeneratorResult gencodeUnaryExpr(UnaryExpression expr, CifCompilerContext ctxt,
            String state)
    {
        // Get child code.
        ExprCodeGeneratorResult crslt = gencodeExpr(expr.getChild(), ctxt, state);

        // Convert based on operator.
        String text;
        switch (expr.getOperator()) {
            case INVERSE:
                text = "!(%s)";
                break;

            case NEGATE:
                text = "negate(%s)";
                break;

            case PLUS:
                // Discard the '+'. No overflow etc possible.
                return crslt;

            case SAMPLE:
                ctxt.needSampler = true;
                text = "Sampler.sample(%s)";
                break;

            default:
                // Should never get here.
                throw new RuntimeException("Unknown unop: " + expr.getOperator());
        }

        return merge(text, expr.getType(), ctxt, crslt);
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
    private static ExprCodeGeneratorResult gencodeBinaryExpr(BinaryExpression expr, CifCompilerContext ctxt,
            String state)
    {
        // Get children code.
        ExprCodeGeneratorResult lrslt = gencodeExpr(expr.getLeft(), ctxt, state);
        ExprCodeGeneratorResult rrslt = gencodeExpr(expr.getRight(), ctxt, state);

        // Convert based on operator.
        String text;
        switch (expr.getOperator()) {
            case IMPLICATION:
                // Short circuit evaluation.
                text = "!(%s) || (%s)";
                break;

            case BI_CONDITIONAL:
                // Use 'equal' instead of '==' to avoid object equality for
                // two Boolean objects.
                text = "equal(%s, %s)";
                break;

            case DISJUNCTION: {
                CifType nltype = normalizeType(expr.getLeft().getType());
                if (nltype instanceof BoolType) {
                    // Short circuit evaluation.
                    text = "(%s) || (%s)";
                } else {
                    Assert.check(nltype instanceof SetType);
                    text = "union(%s, %s)";
                }
                break;
            }

            case CONJUNCTION: {
                CifType nltype = normalizeType(expr.getLeft().getType());
                if (nltype instanceof BoolType) {
                    // Short circuit evaluation.
                    text = "(%s) && (%s)";
                } else {
                    Assert.check(nltype instanceof SetType);
                    text = "intersection(%s, %s)";
                }
                break;
            }

            case LESS_THAN:
                text = "(%s) < (%s)";
                break;

            case LESS_EQUAL:
                text = "(%s) <= (%s)";
                break;

            case GREATER_THAN:
                text = "(%s) > (%s)";
                break;

            case GREATER_EQUAL:
                text = "(%s) >= (%s)";
                break;

            case EQUAL:
                text = "equal(%s, %s)";
                break;

            case UNEQUAL:
                text = "!equal(%s, %s)";
                break;

            case ADDITION: {
                CifType nltype = normalizeType(expr.getLeft().getType());
                CifType nrtype = normalizeType(expr.getRight().getType());
                if (nltype instanceof RealType) {
                    text = "addReal(%s, %s)";
                } else if (nrtype instanceof RealType) {
                    text = "addReal(%s, %s)";
                } else if (nltype instanceof ListType) {
                    text = "addList(%s, %s)";
                } else if (nltype instanceof StringType) {
                    text = "addString(%s, %s)";
                } else if (nltype instanceof DictType) {
                    text = "addDict(%s, %s)";
                } else {
                    text = "addInt(%s, %s)";
                }
                break;
            }

            case SUBTRACTION:
                text = "subtract(%s, %s)";
                break;

            case MULTIPLICATION:
                text = "multiply(%s, %s)";
                break;

            case DIVISION:
                text = "divide(%s, %s)";
                break;

            case INTEGER_DIVISION:
                text = "div(%s, %s)";
                break;

            case MODULUS:
                text = "mod(%s, %s)";
                break;

            case SUBSET:
                text = "subset(%s, %s)";
                break;

            case ELEMENT_OF:
                text = "in(%s, %s)";
                break;

            default:
                // Should never get here.
                throw new RuntimeException("Unknown binop: " + expr.getOperator());
        }

        return merge(text, expr.getType(), ctxt, lrslt, rrslt);
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
    private static ExprCodeGeneratorResult gencodeIfExpr(IfExpression expr, CifCompilerContext ctxt, String state) {
        // Start with 'else'.
        ExprCodeGeneratorResult rslt = gencodeExpr(expr.getElse(), ctxt, state);

        // Wrap 'elifs' around else.
        for (int i = expr.getElifs().size() - 1; i >= 0; i--) {
            ElifExpression elif = expr.getElifs().get(i);
            ExprCodeGeneratorResult grslt = gencodePreds(elif.getGuards(), ctxt, state);
            ExprCodeGeneratorResult trslt = gencodeExpr(elif.getThen(), ctxt, state);
            rslt = merge("(%s) ? %s : (%s)", expr.getType(), ctxt, grslt, trslt, rslt);
        }

        // Wrap 'if' around 'elifs/else'.
        ExprCodeGeneratorResult grslt = gencodePreds(expr.getGuards(), ctxt, state);
        ExprCodeGeneratorResult trslt = gencodeExpr(expr.getThen(), ctxt, state);
        rslt = merge("(%s) ? %s : (%s)", expr.getType(), ctxt, grslt, trslt, rslt);

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
    private static ExprCodeGeneratorResult gencodeSwitchExpr(SwitchExpression expr, CifCompilerContext ctxt,
            String state)
    {
        // Generate code for the control value.
        Expression value = expr.getValue();
        boolean isAutRef = CifTypeUtils.isAutRefExpr(value);

        ExprCodeGeneratorResult valueRslt;
        if (isAutRef) {
            // Control value not needed. The metamodel contains location
            // references, for which we can generate 'loc_pointer == loc_value'
            // code directly.
            valueRslt = null;
        } else {
            // Generate code for the 'normal' expression.
            valueRslt = gencodeExpr(value, ctxt, state);
        }

        // Start with last case. Ignore key, if present. This assumes that
        // the switch always has at least one case that matches, which is
        // a language constraint, and is enforced by the type checker. Ignoring
        // the key means that it is not compared to the control value, and this
        // may lead to the control value of the 'switch' expression not being
        // evaluated, and may thus hide evaluation errors.
        List<SwitchCase> cases = expr.getCases();
        ExprCodeGeneratorResult rslt = gencodeExpr(last(cases).getValue(), ctxt, state);

        // Wrap other cases around it.
        for (int i = cases.size() - 2; i >= 0; i--) {
            // Generate code for the key.
            SwitchCase cse = cases.get(i);
            Expression key = cse.getKey();
            Assert.notNull(key);

            ExprCodeGeneratorResult keyRslt = gencodeExpr(key, ctxt, state);
            if (valueRslt != null) {
                keyRslt = merge("equal(%s, %s)", newBoolType(), ctxt, valueRslt, keyRslt);
            }

            // Wrap result code for this case.
            ExprCodeGeneratorResult cseValueRslt = gencodeExpr(cse.getValue(), ctxt, state);
            rslt = merge("(%s) ? %s : (%s)", expr.getType(), ctxt, keyRslt, cseValueRslt, rslt);
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
    private static ExprCodeGeneratorResult gencodeProjExpr(ProjectionExpression expr, CifCompilerContext ctxt,
            String state)
    {
        // Get child code.
        ExprCodeGeneratorResult crslt = gencodeExpr(expr.getChild(), ctxt, state);

        // Special case for tuple field projection.
        CifType nctype = normalizeType(expr.getChild().getType());
        if (nctype instanceof TupleType && expr.getIndex() instanceof FieldExpression) {
            // Get field (name).
            Field field = ((FieldExpression)expr.getIndex()).getField();
            String fieldName = ctxt.getTupleTypeFieldFieldName(field);
            return merge(fmt("(%%s).%s", fieldName), expr.getType(), ctxt, crslt);
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
            return merge(fmt("(%%s).%s", fieldName), expr.getType(), ctxt, crslt);
        } else {
            // List, dictionary, and string.
            ExprCodeGeneratorResult irslt = gencodeExpr(expr.getIndex(), ctxt, state);
            return merge("project(%s, %s)", expr.getType(), ctxt, crslt, irslt);
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
    private static ExprCodeGeneratorResult gencodeSliceExpr(SliceExpression expr, CifCompilerContext ctxt,
            String state)
    {
        ExprCodeGeneratorResult crslt = gencodeExpr(expr.getChild(), ctxt, state);
        ExprCodeGeneratorResult brslt = (expr.getBegin() == null) ? new ExprCodeGeneratorResult("null", newIntType())
                : gencodeExpr(expr.getBegin(), ctxt, state);
        ExprCodeGeneratorResult erslt = (expr.getEnd() == null) ? new ExprCodeGeneratorResult("null", newIntType())
                : gencodeExpr(expr.getEnd(), ctxt, state);
        return merge("slice(%s, %s, %s)", expr.getType(), ctxt, crslt, brslt, erslt);
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
    private static ExprCodeGeneratorResult gencodeFuncCallExpr(FunctionCallExpression expr, CifCompilerContext ctxt,
            String state)
    {
        // User-defined functions.
        if (!(expr.getFunction() instanceof StdLibFunctionExpression)) {
            List<ExprCodeGeneratorResult> funcAndArgResults = listc(expr.getParams().size() + 1);
            funcAndArgResults.add(gencodeExpr(expr.getFunction(), ctxt, state));
            funcAndArgResults.addAll(gencodeExprs(expr.getParams(), ctxt, state));
            String paramsTxt = String.join(", ", Collections.nCopies(expr.getParams().size(), "%s"));
            return merge(fmt("(%%s).evalFunc(%s)", paramsTxt), expr.getType(), ctxt, funcAndArgResults);
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
            List<ExprCodeGeneratorResult> valueRslts = listc(expr.getParams().size() - 1);
            List<CifType> valueTypes = listc(expr.getParams().size() - 1);
            for (int i = 1; i < expr.getParams().size(); i++) {
                Expression value = expr.getParams().get(i);
                valueRslts.add(gencodeExpr(value, ctxt, state));
                valueTypes.add(value.getType());
            }

            // Generate code for the pattern.
            return gencodePattern(pattern, valueRslts, valueTypes, expr, ctxt);
        }

        // Generate standard library function call code.
        List<ExprCodeGeneratorResult> paramRslts = gencodeExprs(expr.getParams(), ctxt, state);
        String paramsTxt = String.join(", ", Collections.nCopies(expr.getParams().size(), "%s"));
        String text;
        switch (stdlib) {
            case ACOSH:
                text = fmt("acosh(%s)", paramsTxt);
                break;

            case ACOS:
                text = fmt("acos(%s)", paramsTxt);
                break;

            case ASINH:
                text = fmt("asinh(%s)", paramsTxt);
                break;

            case ASIN:
                text = fmt("asin(%s)", paramsTxt);
                break;

            case ATANH:
                text = fmt("atanh(%s)", paramsTxt);
                break;

            case ATAN:
                text = fmt("atan(%s)", paramsTxt);
                break;

            case COSH:
                text = fmt("cosh(%s)", paramsTxt);
                break;

            case COS:
                text = fmt("cos(%s)", paramsTxt);
                break;

            case SINH:
                text = fmt("sinh(%s)", paramsTxt);
                break;

            case SIN:
                text = fmt("sin(%s)", paramsTxt);
                break;

            case TANH:
                text = fmt("tanh(%s)", paramsTxt);
                break;

            case TAN:
                text = fmt("tan(%s)", paramsTxt);
                break;

            case ABS:
                text = fmt("abs(%s)", paramsTxt);
                break;

            case CBRT:
                text = fmt("cbrt(%s)", paramsTxt);
                break;

            case CEIL:
                text = fmt("ceil(%s)", paramsTxt);
                break;

            case DELETE:
                text = fmt("delete(%s)", paramsTxt);
                break;

            case EMPTY:
                text = fmt("empty(%s)", paramsTxt);
                break;

            case EXP:
                text = fmt("exp(%s)", paramsTxt);
                break;

            case FLOOR:
                text = fmt("floor(%s)", paramsTxt);
                break;

            case FORMAT:
                throw new RuntimeException("Already handled above: " + stdlib);

            case LN:
                text = fmt("ln(%s)", paramsTxt);
                break;

            case LOG:
                text = fmt("log(%s)", paramsTxt);
                break;

            case MAXIMUM:
                text = fmt("max(%s)", paramsTxt);
                break;

            case MINIMUM:
                text = fmt("min(%s)", paramsTxt);
                break;

            case POP: {
                // Get tuple type for result, and generate code for it.
                TupleType rsltType = (TupleType)normalizeType(expr.getType());
                String className = ctxt.getTupleTypeClassName(rsltType);

                // Generate code for the 'pop' function call.
                text = fmt("%s.pop(%s)", className, paramsTxt);
                break;
            }

            case POWER: {
                CifType rsltType = normalizeType(expr.getType());

                if (rsltType instanceof IntType) {
                    text = fmt("powInt(%s)", paramsTxt);
                } else {
                    text = fmt("powReal(%s)", paramsTxt);
                }
                break;
            }

            case ROUND:
                text = fmt("round(%s)", paramsTxt);
                break;

            case SCALE:
                text = fmt("scale(%s)", paramsTxt);
                break;

            case SIGN:
                text = fmt("sign(%s)", paramsTxt);
                break;

            case SIZE:
                text = fmt("size(%s)", paramsTxt);
                break;

            case SQRT:
                text = fmt("sqrt(%s)", paramsTxt);
                break;

            case BERNOULLI:
                text = fmt("new BernoulliDistribution(new CifMersenneTwister(%s.spec.getNextSeed()), %s)", state,
                        paramsTxt);
                break;

            case BETA:
                text = fmt("new BetaDistribution(new CifMersenneTwister(%s.spec.getNextSeed()), %s)", state, paramsTxt);
                break;

            case BINOMIAL:
                text = fmt("new BinomialDistribution(new CifMersenneTwister(%s.spec.getNextSeed()), %s)", state,
                        paramsTxt);
                break;

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

                text = fmt("new %s(%s)", className, paramsTxt);
                break;
            }

            case ERLANG:
                text = fmt("new ErlangDistribution(new CifMersenneTwister(%s.spec.getNextSeed()), %s)", state,
                        paramsTxt);
                break;

            case EXPONENTIAL:
                text = fmt("new ExponentialDistribution(new CifMersenneTwister(%s.spec.getNextSeed()), %s)", state,
                        paramsTxt);
                break;

            case GAMMA:
                text = fmt("new GammaDistribution(new CifMersenneTwister(%s.spec.getNextSeed()), %s)", state,
                        paramsTxt);
                break;

            case GEOMETRIC:
                text = fmt("new GeometricDistribution(new CifMersenneTwister(%s.spec.getNextSeed()), %s)", state,
                        paramsTxt);
                break;

            case LOG_NORMAL:
                text = fmt("new LogNormalDistribution(new CifMersenneTwister(%s.spec.getNextSeed()), %s)", state,
                        paramsTxt);
                break;

            case NORMAL:
                text = fmt("new NormalDistribution(new CifMersenneTwister(%s.spec.getNextSeed()), %s)", state,
                        paramsTxt);
                break;

            case POISSON:
                text = fmt("new PoissonDistribution(new CifMersenneTwister(%s.spec.getNextSeed()), %s)", state,
                        paramsTxt);
                break;

            case RANDOM:
                text = fmt("new RandomDistribution(new CifMersenneTwister(%s.spec.getNextSeed()))", state);
                break;

            case TRIANGLE:
                text = fmt("new TriangleDistribution(new CifMersenneTwister(%s.spec.getNextSeed()), %s)", state,
                        paramsTxt);
                break;

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

                text = fmt("new %s(new CifMersenneTwister(%s.spec.getNextSeed()), %s)", className, state, paramsTxt);
                break;
            }

            case WEIBULL:
                text = fmt("new WeibullDistribution(new CifMersenneTwister(%s.spec.getNextSeed()), %s)", state,
                        paramsTxt);
                break;

            default:
                // Should never get here.
                throw new RuntimeException("Unknown stdlib func: " + stdlib);
        }
        return merge(text, expr.getType(), ctxt, paramRslts);
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
    private static ExprCodeGeneratorResult gencodeListExpr(ListExpression expr, CifCompilerContext ctxt, String state) {
        // Don't generate code for large list literals. Generate a data file
        // and read it again at runtime. Prevents generating so much Java code
        // that the Java compiler can't compile it.
        if (expr.getElements().size() >= 100 && isSerializableLiteral(expr)) {
            return new ExprCodeGeneratorResult(gencodeLiteral(expr, ctxt), expr.getType());
        }

        // Get list type and element type.
        ListType ltype = (ListType)normalizeType(expr.getType());
        CifType etype = ltype.getElementType();

        // Generate list construction code.
        int size = expr.getElements().size();
        String constructorCode = fmt("new ArrayList<%s>(%d)", gencodeType(etype, ctxt, true), size);

        // Special case for empty lists.
        if (expr.getElements().isEmpty()) {
            return new ExprCodeGeneratorResult(constructorCode, expr.getType());
        }

        // For non-empty lists, add the elements.
        List<ExprCodeGeneratorResult> elemRslts = gencodeExprs(expr.getElements(), ctxt, state);
        String elemsTxt = String.join(", ", Collections.nCopies(expr.getElements().size(), "%s"));
        return merge(fmt("makelist(%s, %s)", constructorCode, elemsTxt), expr.getType(), ctxt, elemRslts);
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
    private static ExprCodeGeneratorResult gencodeSetExpr(SetExpression expr, CifCompilerContext ctxt, String state) {
        // Don't generate code for large set literals. Generate a data file
        // and read it again at runtime. Prevents generating so much Java code
        // that the Java compiler can't compile it.
        if (expr.getElements().size() >= 100 && isSerializableLiteral(expr)) {
            return new ExprCodeGeneratorResult(gencodeLiteral(expr, ctxt), expr.getType());
        }

        // Get set type and element type.
        SetType stype = (SetType)normalizeType(expr.getType());
        CifType etype = stype.getElementType();

        // Generate set construction code. Use LinkedHashSet for determinism.
        int size = expr.getElements().size();
        String constructorCode = fmt("new LinkedHashSet<%s>(%d)", gencodeType(etype, ctxt, true), size);

        // Special case for empty set.
        if (expr.getElements().isEmpty()) {
            return new ExprCodeGeneratorResult(constructorCode, expr.getType());
        }

        // For non-empty sets, add the elements.
        List<ExprCodeGeneratorResult> elemRslts = gencodeExprs(expr.getElements(), ctxt, state);
        String elemsTxt = String.join(", ", Collections.nCopies(expr.getElements().size(), "%s"));
        return merge(fmt("makeset(%s, %s)", constructorCode, elemsTxt), expr.getType(), ctxt, elemRslts);
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
    private static ExprCodeGeneratorResult gencodeTupleExpr(TupleExpression expr, CifCompilerContext ctxt,
            String state)
    {
        // Don't generate code for large tuple literals. Generate a data
        // file and read it again at runtime. Prevents generating so much Java
        // code that the Java compiler can't compile it.
        if (expr.getFields().size() >= 100 && isSerializableLiteral(expr)) {
            return new ExprCodeGeneratorResult(gencodeLiteral(expr, ctxt), expr.getType());
        }

        // Get tuple type class name.
        TupleType tupleType = (TupleType)normalizeType(expr.getType());
        String className = ctxt.getTupleTypeClassName(tupleType);

        // Generate constructor call code.
        List<ExprCodeGeneratorResult> fRslts = gencodeExprs(expr.getFields(), ctxt, state);
        String fieldsTxt = String.join(", ", Collections.nCopies(expr.getFields().size(), "%s"));
        return merge(fmt("new %s(%s)", className, fieldsTxt), expr.getType(), ctxt, fRslts);
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
    private static ExprCodeGeneratorResult gencodeDictExpr(DictExpression expr, CifCompilerContext ctxt, String state) {
        // Don't generate code for large dictionary literals. Generate a data
        // file and read it again at runtime. Prevents generating so much Java
        // code that the Java compiler can't compile it.
        if (expr.getPairs().size() >= 100 && isSerializableLiteral(expr)) {
            return new ExprCodeGeneratorResult(gencodeLiteral(expr, ctxt), expr.getType());
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
        List<ExprCodeGeneratorResult> keyRslts = listc(pairs.size());
        List<ExprCodeGeneratorResult> valueRslts = listc(pairs.size());
        for (DictPair pair: pairs) {
            keyRslts.add(gencodeExpr(pair.getKey(), ctxt, state));
            valueRslts.add(gencodeExpr(pair.getValue(), ctxt, state));
        }

        // Generate code text placeholders for key/value arrays.
        String keysTxt = fmt("array(%s)", String.join(", ", Collections.nCopies(keyRslts.size(), "%s")));
        String valuesTxt = fmt("array(%s)", String.join(", ", Collections.nCopies(valueRslts.size(), "%s")));

        // Return the code for the dictionary literal.
        keyRslts.addAll(valueRslts);
        return merge(fmt("addpairs(%s, %s, %s)", rslt, keysTxt, valuesTxt), expr.getType(), ctxt, keyRslts);
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
    private static ExprCodeGeneratorResult gencodeDiscVarExpr(DiscVariableExpression expr, CifCompilerContext ctxt,
            String state)
    {
        DiscVariable var = expr.getVariable();
        EObject parent = var.eContainer();
        if (parent instanceof ComplexComponent) {
            return new ExprCodeGeneratorResult(fmt("%s.%s.%s", state, ctxt.getAutSubStateFieldName((Automaton)parent),
                    ctxt.getDiscVarFieldName(var)), expr.getType());
        } else if (parent instanceof FunctionParameter) {
            return new ExprCodeGeneratorResult(ctxt.getFuncParamMethodParamName(var), expr.getType());
        } else if (parent instanceof InternalFunction) {
            return new ExprCodeGeneratorResult(ctxt.getFuncLocalVarName(var), expr.getType());
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
    private static ExprCodeGeneratorResult gencodeInputVarExpr(InputVariableExpression expr, CifCompilerContext ctxt,
            String state)
    {
        InputVariable var = expr.getVariable();
        return new ExprCodeGeneratorResult(
                fmt("%s.%s.%s", state, INPUT_SUB_STATE_FIELD_NAME, ctxt.getInputVarFieldName(var)), expr.getType());
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
    private static ExprCodeGeneratorResult gencodeContVarExpr(ContVariableExpression expr, CifCompilerContext ctxt,
            String state)
    {
        ContVariable var = expr.getVariable();

        if (expr.isDerivative()) {
            // Derivative reference.
            return new ExprCodeGeneratorResult(fmt("Derivatives.%s(%s)", ctxt.getDerivativeMethodName(var), state),
                    expr.getType());
        } else {
            // Continuous variable reference.
            return new ExprCodeGeneratorResult(
                    fmt("%s.%s.%s", state, ctxt.getContVarSubStateName(var), ctxt.getContVarFieldName(var)),
                    expr.getType());
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
    private static ExprCodeGeneratorResult gencodeLocExpr(LocationExpression expr, CifCompilerContext ctxt,
            String state)
    {
        Location loc = expr.getLocation();
        Automaton aut = (Automaton)loc.eContainer();
        return new ExprCodeGeneratorResult(fmt("%s.%s.%s == %s", state, ctxt.getAutSubStateFieldName(aut),
                ctxt.getLocationPointerFieldName(aut), ctxt.getLocationValueText(loc)), expr.getType());
    }
}
