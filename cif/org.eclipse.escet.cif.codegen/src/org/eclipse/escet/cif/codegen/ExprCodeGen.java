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

package org.eclipse.escet.cif.codegen;

import static org.eclipse.escet.cif.common.CifTypeUtils.normalizeType;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Strings.str;

import java.util.List;

import org.eclipse.escet.cif.codegen.assignments.Destination;
import org.eclipse.escet.cif.codegen.typeinfos.ArrayTypeInfo;
import org.eclipse.escet.cif.codegen.typeinfos.BoolTypeInfo;
import org.eclipse.escet.cif.codegen.typeinfos.EnumTypeInfo;
import org.eclipse.escet.cif.codegen.typeinfos.IntTypeInfo;
import org.eclipse.escet.cif.codegen.typeinfos.NegateOperation;
import org.eclipse.escet.cif.codegen.typeinfos.OrderingOperations;
import org.eclipse.escet.cif.codegen.typeinfos.RealTypeInfo;
import org.eclipse.escet.cif.codegen.typeinfos.StringTypeInfo;
import org.eclipse.escet.cif.codegen.typeinfos.TupleTypeInfo;
import org.eclipse.escet.cif.codegen.typeinfos.TypeInfo;
import org.eclipse.escet.cif.common.CifEvalException;
import org.eclipse.escet.cif.common.CifEvalUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.RangeCompat;
import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.expressions.AlgVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BoolExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CastExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ConstantExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ContVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EnumLiteralExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.FieldExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.FunctionCallExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.FunctionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IfExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.InputVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IntExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ListExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ProjectionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.RealExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunction;
import org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunctionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.StringExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TimeExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TupleExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression;
import org.eclipse.escet.cif.metamodel.cif.functions.Function;
import org.eclipse.escet.cif.metamodel.cif.functions.InternalFunction;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.cif.metamodel.cif.types.ListType;
import org.eclipse.escet.cif.metamodel.cif.types.RealType;
import org.eclipse.escet.cif.metamodel.cif.types.StringType;
import org.eclipse.escet.cif.metamodel.cif.types.TupleType;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Strings;

/** Expression code generator base class. */
public abstract class ExprCodeGen {
    /**
     * Generate a target language code fragment for the given predicates, assuming conjunction between the predicates.
     *
     * @param preds The predicates.
     * @param ctxt Code generation context.
     * @return The target language code that represents the given predicates.
     */
    protected ExprCode predsToTarget(List<Expression> preds, CodeContext ctxt) {
        List<ExprCode> txts = listc(preds.size());
        for (Expression pred: preds) {
            txts.add(exprToTarget(pred, null, ctxt));
        }
        return predTextsToTarget(txts, ctxt);
    }

    /**
     * Generate a target language code fragment for the given predicates, assuming conjunction between the predicates.
     *
     * @param preds The predicates.
     * @param ctxt Code generation context.
     * @return The target language code that represents the given predicates.
     */
    protected abstract ExprCode predTextsToTarget(List<ExprCode> preds, CodeContext ctxt);

    /**
     * Generate a target language code fragment for the given expression.
     *
     * <p>
     * This function only performs contract checking, the real conversion happens in {@link #internalExprToTarget}.
     * </p>
     *
     * @param expr The expression.
     * @param dest Destination to write the value to if available, else {@code null}.
     * @param ctxt Code generation context.
     * @return The target language code that represents the given expression.
     */
    protected ExprCode exprToTarget(Expression expr, Destination dest, CodeContext ctxt) {
        ExprCode result = internalExprToTarget(expr, dest, ctxt);
        if (dest == null) {
            Assert.check(result.hasDataValue());
        } else {
            Assert.check(!result.hasDataValue());
        }
        return result;
    }

    /**
     * Internal function for converting an expression to a target language code fragment.
     *
     * @param expr Expression to convert.
     * @param dest Destination to write the value to if available, else {@code null}.
     * @param ctxt Code generation context.
     * @return The generated code fragment.
     */
    private ExprCode internalExprToTarget(Expression expr, Destination dest, CodeContext ctxt) {
        if (expr instanceof BoolExpression) {
            return convertBoolExpression((BoolExpression)expr, dest, ctxt);
        } else if (expr instanceof IntExpression) {
            return convertIntExpression((IntExpression)expr, dest, ctxt);
        } else if (expr instanceof RealExpression) {
            return convertRealExpression((RealExpression)expr, dest, ctxt);
        } else if (expr instanceof StringExpression) {
            return convertStringExpression((StringExpression)expr, dest, ctxt);
        } else if (expr instanceof TimeExpression) {
            return convertTimeExpression((TimeExpression)expr, dest, ctxt);
        } else if (expr instanceof CastExpression) {
            return convertCastExpression((CastExpression)expr, dest, ctxt);
        } else if (expr instanceof UnaryExpression) {
            return convertUnaryExpression((UnaryExpression)expr, dest, ctxt);
        } else if (expr instanceof BinaryExpression) {
            return convertBinaryExpression((BinaryExpression)expr, dest, ctxt);
        } else if (expr instanceof IfExpression) {
            return convertIfExpression((IfExpression)expr, dest, ctxt);
        } else if (expr instanceof ProjectionExpression) {
            return convertProjectionExpression((ProjectionExpression)expr, dest, ctxt);
        } else if (expr instanceof FunctionCallExpression) {
            return convertFunctionCallExpression((FunctionCallExpression)expr, dest, ctxt);
        } else if (expr instanceof ListExpression) {
            return convertListExpression((ListExpression)expr, dest, ctxt);
        } else if (expr instanceof TupleExpression) {
            return convertTupleExpression((TupleExpression)expr, dest, ctxt);
        } else if (expr instanceof ConstantExpression) {
            return convertConstantExpression((ConstantExpression)expr, dest, ctxt);
        } else if (expr instanceof DiscVariableExpression) {
            return convertDiscVariableExpression((DiscVariableExpression)expr, dest, ctxt);
        } else if (expr instanceof AlgVariableExpression) {
            return convertAlgVariableExpression((AlgVariableExpression)expr, dest, ctxt);
        } else if (expr instanceof ContVariableExpression) {
            return convertContVariableExpression((ContVariableExpression)expr, dest, ctxt);
        } else if (expr instanceof EnumLiteralExpression) {
            return convertEnumLiteralExpression((EnumLiteralExpression)expr, dest, ctxt);
        } else if (expr instanceof InputVariableExpression) {
            return convertInputVariableExpression((InputVariableExpression)expr, dest, ctxt);
        } else {
            // Notes:
            // - SliceExpression is not supported (precondition).
            // - SetExpression is not supported (precondition).
            // - DictExpression is not supported (precondition).
            // - SwitchExpression is already eliminated (linearization).
            // - CompParamWrapExpression is already eliminated (preprocessing).
            // - CompInstWrapExpression is already eliminated (preprocessing).
            // - ComponentExpression is already eliminated (linearization).
            // - ReceivedExpression is already eliminated (linearization).
            // - SelfExpression is already eliminated (linearization).
            // - StdLibFunctionExpression is handled by FunctionCallExpression.
            // - FunctionExpression is handled by FunctionCallExpression.
            // - LocationExpression is already eliminated (linearization).
            // - FieldExpression is handled by ProjectionExpression.
            // - TauExpression can't occur in a value context.
            // - EventExpression can't occur in a value context.
            throw new RuntimeException("Unexpected expr: " + expr);
        }
    }

    /**
     * Convert a boolean literal expression to a target language code fragment.
     *
     * @param expr Expression to convert.
     * @param dest Destination to write the value to if available, else {@code null}.
     * @param ctxt Code generation context.
     * @return Code fragment representing the value.
     */
    protected ExprCode convertBoolExpression(BoolExpression expr, Destination dest, CodeContext ctxt) {
        BoolTypeInfo ti = (BoolTypeInfo)ctxt.typeToTarget(expr.getType());
        return ti.convertLiteral(expr.isValue(), dest, ctxt);
    }

    /**
     * Convert an integer literal expression to a target language code fragment.
     *
     * @param expr Expression to convert.
     * @param dest Destination to write the value to if available, else {@code null}.
     * @param ctxt Code generation context.
     * @return Code fragment representing the value.
     */
    protected ExprCode convertIntExpression(IntExpression expr, Destination dest, CodeContext ctxt) {
        String resultText = Integer.toString(expr.getValue());

        IntTypeInfo ti = (IntTypeInfo)ctxt.typeToTarget(expr.getType());
        return ti.convertLiteral(resultText, dest, ctxt);
    }

    /**
     * Convert a real literal expression to a target language code fragment.
     *
     * @param expr Expression to convert.
     * @param dest Destination to write the value to if available, else {@code null}.
     * @param ctxt Code generation context.
     * @return Code fragment representing the value.
     */
    protected ExprCode convertRealExpression(RealExpression expr, Destination dest, CodeContext ctxt) {
        // Convert to 'double' to make sure that for instance 1e-999
        // becomes '0.0', since '1e-999' can not be represented as a
        // double, and causes a compilation error.
        String valueTxt = expr.getValue();
        double value = Double.parseDouble(valueTxt);
        Assert.check(Double.isFinite(value));
        String resultText = Double.toString(value);

        RealTypeInfo ti = (RealTypeInfo)ctxt.typeToTarget(expr.getType());
        return ti.convertLiteral(resultText, dest, ctxt);
    }

    /**
     * Convert a string literal expression to a target language code fragment.
     *
     * @param expr Expression to convert.
     * @param dest Destination to write the value to if available, else {@code null}.
     * @param ctxt Code generation context.
     * @return Code fragment representing the value.
     */
    protected ExprCode convertStringExpression(StringExpression expr, Destination dest, CodeContext ctxt) {
        String resultText = Strings.stringToJava(expr.getValue());

        StringTypeInfo ti = (StringTypeInfo)ctxt.typeToTarget(expr.getType());
        return ti.convertLiteral(resultText, dest, ctxt);
    }

    /**
     * Convert a time expression to a target language code fragment.
     *
     * @param expr Expression to convert.
     * @param dest Destination to write the value to if available, else {@code null}.
     * @param ctxt Code generation context.
     * @return Code fragment representing the value.
     */
    protected ExprCode convertTimeExpression(TimeExpression expr, Destination dest, CodeContext ctxt) {
        RealTypeInfo ti = (RealTypeInfo)ctxt.typeToTarget(expr.getType());
        return ti.convertTimeExpression(dest, ctxt);
    }

    /**
     * Test whether the provided normalized type is a boolean, integer, or real type.
     *
     * @param type Type to test.
     * @return {@code true} if the given normalized type is a boolean, integer, or real type, else {@code false}.
     */
    private boolean isBoolIntRealType(CifType type) {
        return type instanceof BoolType || type instanceof IntType || type instanceof RealType;
    }

    /**
     * Convert a cast expression to a target language code fragment.
     *
     * @param expr Expression to convert.
     * @param dest Destination to write the value to if available, else {@code null}.
     * @param ctxt Code generation context.
     * @return Code fragment representing the value.
     */
    protected ExprCode convertCastExpression(CastExpression expr, Destination dest, CodeContext ctxt) {
        // No cast from automaton reference to string due to linearization.
        Expression child = expr.getChild();
        Assert.check(!CifTypeUtils.isAutRefExpr(child));

        CifType childType = normalizeType(child.getType());
        CifType exprType = normalizeType(expr.getType());

        if ((childType instanceof IntType && exprType instanceof RealType)
                || (isBoolIntRealType(childType) && exprType instanceof StringType)
                || (childType instanceof StringType && isBoolIntRealType(exprType)))
        {
            return convertCastExpression(exprType, childType, child, dest, ctxt);
        } else if (CifTypeUtils.checkTypeCompat(childType, exprType, RangeCompat.EQUAL)) {
            // Ignore cast to child type.
            return exprToTarget(child, dest, ctxt);
        } else {
            String msg = "Unknown cast: " + childType + ", " + exprType;
            throw new RuntimeException(msg);
        }
    }

    /**
     * Convert a cast expression to a target language code fragment.
     *
     * <p>
     * The only cases that must be handled are:
     * <table>
     * <th>
     * <td>Cast result</td>
     * <td>Child type</td></th>
     * <tr>
     * <td>real</td>
     * <td>int</td>
     * </tr>
     * <tr>
     * <td>string</td>
     * <td>int</td>
     * </tr>
     * <tr>
     * <td>string</td>
     * <td>real</td>
     * </tr>
     * <tr>
     * <td>string</td>
     * <td>bool</td>
     * </tr>
     * <tr>
     * <td>int</td>
     * <td>string</td>
     * </tr>
     * <tr>
     * <td>real</td>
     * <td>string</td>
     * </tr>
     * <tr>
     * <td>bool</td>
     * <td>string</td>
     * </tr>
     * </table>
     * </p>
     *
     * @param exprType Normalized result type to provide.
     * @param childType Normalized type of the child value.
     * @param child Child expression.
     * @param dest Destination to write the value to if available, else {@code null}.
     * @param ctxt Code generation context.
     * @return Code fragment representing the value.
     */
    protected abstract ExprCode convertCastExpression(CifType exprType, CifType childType, Expression child,
            Destination dest, CodeContext ctxt);

    /**
     * Convert a unary expression to a target language code fragment.
     *
     * @param expr Expression to convert.
     * @param dest Destination to write the value to if available, else {@code null}.
     * @param ctxt Code generation context.
     * @return Code fragment representing the value.
     */
    protected ExprCode convertUnaryExpression(UnaryExpression expr, Destination dest, CodeContext ctxt) {
        TypeInfo ti = ctxt.typeToTarget(expr.getType());

        switch (expr.getOperator()) {
            case INVERSE: {
                BoolTypeInfo boolTi = (BoolTypeInfo)ti;
                return boolTi.convertInvert(expr.getChild(), dest, ctxt);
            }

            case NEGATE: {
                NegateOperation negateOp = (NegateOperation)ti;
                return negateOp.convertNegate(expr, dest, ctxt);
            }

            case PLUS:
                // Discard the '+'. No overflow etc possible.
                return exprToTarget(expr.getChild(), dest, ctxt);

            case SAMPLE:
                break;
            default:
                break;
        }
        throw new RuntimeException("Unsupported unary operator: " + expr.getOperator());
    }

    /**
     * Convert a binary expression to a target language code fragment.
     *
     * @param expr Expression to convert.
     * @param dest Destination to write the value to if available, else {@code null}.
     * @param ctxt Code generation context.
     * @return Code fragment representing the value.
     */
    protected ExprCode convertBinaryExpression(BinaryExpression expr, Destination dest, CodeContext ctxt) {
        TypeInfo resultTI = ctxt.typeToTarget(expr.getType());

        switch (expr.getOperator()) {
            // Short circuit operations.
            case CONJUNCTION:
            case DISJUNCTION:
            case IMPLICATION: {
                BoolTypeInfo boolTI = (BoolTypeInfo)resultTI;
                return boolTI.convertShortCircuit(expr, dest, ctxt);
            }

            // (Un)equality tests.
            case BI_CONDITIONAL:
            case EQUAL: {
                TypeInfo leftTI = ctxt.typeToTarget(expr.getLeft().getType());
                return leftTI.convertEqualsExpression(expr, dest, ctxt);
            }

            case UNEQUAL: {
                TypeInfo leftTI = ctxt.typeToTarget(expr.getLeft().getType());
                return leftTI.convertUnequalsExpression(expr, dest, ctxt);
            }

            // Ordering operations.
            case GREATER_EQUAL:
                return getOrderingOperation(expr, ctxt).convertGreaterEqual(expr, dest, ctxt);

            case GREATER_THAN:
                return getOrderingOperation(expr, ctxt).convertGreaterThan(expr, dest, ctxt);

            case LESS_EQUAL:
                return getOrderingOperation(expr, ctxt).convertLessEqual(expr, dest, ctxt);

            case LESS_THAN:
                return getOrderingOperation(expr, ctxt).convertLessThan(expr, dest, ctxt);

            // Div and mod operations.
            case INTEGER_DIVISION: {
                IntTypeInfo ti = (IntTypeInfo)resultTI;
                return ti.convertDiv(expr, dest, ctxt);
            }

            case MODULUS: {
                IntTypeInfo ti = (IntTypeInfo)resultTI;
                return ti.convertMod(expr, dest, ctxt);
            }

            case MULTIPLICATION:
                if (resultTI instanceof IntTypeInfo) {
                    IntTypeInfo ti = (IntTypeInfo)resultTI;
                    return ti.convertMultiplication(expr, dest, ctxt);
                } else {
                    RealTypeInfo ti = (RealTypeInfo)resultTI;
                    return ti.convertMultiplication(expr, dest, ctxt);
                }

            case DIVISION: {
                RealTypeInfo ti = (RealTypeInfo)resultTI;
                return ti.convertDivision(expr, dest, ctxt);
            }

            case ADDITION:
                if (resultTI instanceof IntTypeInfo) {
                    IntTypeInfo ti = (IntTypeInfo)resultTI;
                    return ti.convertAddition(expr, dest, ctxt);
                } else if (resultTI instanceof RealTypeInfo) {
                    RealTypeInfo ti = (RealTypeInfo)resultTI;
                    return ti.convertAddition(expr, dest, ctxt);
                } else {
                    Assert.check(resultTI instanceof StringTypeInfo);
                    StringTypeInfo ti = (StringTypeInfo)resultTI;
                    return ti.convertConcatenation(expr, dest, ctxt);
                }

            case SUBTRACTION:
                if (resultTI instanceof IntTypeInfo) {
                    IntTypeInfo ti = (IntTypeInfo)resultTI;
                    return ti.convertSubtraction(expr, dest, ctxt);
                } else {
                    Assert.check(resultTI instanceof RealTypeInfo);
                    RealTypeInfo ti = (RealTypeInfo)resultTI;
                    return ti.convertSubtraction(expr, dest, ctxt);
                }

            case ELEMENT_OF:
            case SUBSET:
            default:
                break;
        }
        String msg = "Unexpected binary expression: " + str(expr.getOperator());
        throw new RuntimeException(msg);
    }

    /**
     * Get the type information that can convert an ordering operation (less-than, less-equal, greater-equal, or
     * greater-than) to the target language.
     *
     * @param expr Compare expression to convert.
     * @param ctxt Code context of the expression.
     * @return The type information that can convert the expression.
     */
    private OrderingOperations getOrderingOperation(BinaryExpression expr, CodeContext ctxt) {
        CifType leftType = normalizeType(expr.getLeft().getType());
        if (leftType instanceof RealType) {
            return (OrderingOperations)ctxt.typeToTarget(leftType);
        }

        return (OrderingOperations)ctxt.typeToTarget(expr.getRight().getType());
    }

    /**
     * Convert an 'if' expression to a target language code fragment.
     *
     * @param expr Expression to convert.
     * @param dest Destination to write the value to if available, else {@code null}.
     * @param ctxt Code generation context.
     * @return Code fragment representing the value.
     */
    protected abstract ExprCode convertIfExpression(IfExpression expr, Destination dest, CodeContext ctxt);

    /**
     * Convert a projection expression to a target language code fragment.
     *
     * @param expr Expression to convert.
     * @param dest Destination to write the value to if available, else {@code null}.
     * @param ctxt Code generation context.
     * @return Code fragment representing the value.
     */
    protected ExprCode convertProjectionExpression(ProjectionExpression expr, Destination dest, CodeContext ctxt) {
        Expression child = expr.getChild();
        CifType nctype = normalizeType(child.getType());
        Expression idxExpr = expr.getIndex();

        ExprCode childCode = exprToTarget(child, null, ctxt); // Get child code.

        TypeInfo childTi = ctxt.typeToTarget(child.getType());

        // Tuple.
        if (nctype instanceof TupleType) {
            TupleType tupleType = (TupleType)nctype;
            TupleTypeInfo tupleTi = (TupleTypeInfo)ctxt.typeToTarget(tupleType);

            // Projection on field name.
            if (idxExpr instanceof FieldExpression) {
                FieldExpression fieldExpr = (FieldExpression)idxExpr;

                int idx = tupleType.getFields().indexOf(fieldExpr.getField());
                return tupleTi.getProjectedValue(childCode, idx, dest, ctxt);
            }

            // Projection on constant value.

            // Get field index for tuple index projection. Index is valid:
            // type checker already checked it.
            int idx;
            try {
                idx = (Integer)CifEvalUtils.eval(idxExpr, false);
            } catch (CifEvalException e) {
                // Should never fail: type checker already evaluated this.
                throw new RuntimeException(e);
            }

            return tupleTi.getProjectedValue(childCode, idx, dest, ctxt);
        }

        // Array or string. Dictionary not supported (precondition).
        if (nctype instanceof ListType) {
            ExprCode indexCode = exprToTarget(idxExpr, null, ctxt);
            ArrayTypeInfo arrayTi = (ArrayTypeInfo)childTi;
            return arrayTi.getProjectedValue(childCode, indexCode, dest, ctxt);
        } else {
            Assert.check(nctype instanceof StringType);

            ExprCode indexCode = exprToTarget(idxExpr, null, ctxt);
            StringTypeInfo strTi = (StringTypeInfo)childTi;
            return strTi.getProjectedValue(childCode, indexCode, dest, ctxt);
        }
    }

    /**
     * Convert a function call expression to a target language code fragment.
     *
     * @param expr Expression to convert.
     * @param dest Destination to write the value to if available, else {@code null}.
     * @param ctxt Code generation context.
     * @return Code fragment representing the value.
     */
    protected ExprCode convertFunctionCallExpression(FunctionCallExpression expr, Destination dest, CodeContext ctxt) {
        List<Expression> params = expr.getParams();

        // User-defined functions. Only a direct reference to an internal
        // user-defined function is supported (precondition). Expressions
        // that when evaluated result in such a reference are not
        // supported (precondition).
        Expression funcRef = expr.getFunction();
        if (funcRef instanceof FunctionExpression) {
            Function func = ((FunctionExpression)funcRef).getFunction();
            Assert.check(func instanceof InternalFunction);

            // Convert arguments to a target language code fragment.
            List<ExprCode> paramTexts = listc(params.size());
            for (Expression param: params) {
                paramTexts.add(exprToTarget(param, null, ctxt));
            }

            return convertInternalFunctionCall((InternalFunction)func, paramTexts, dest, ctxt);
        }

        // Get standard library function.
        StdLibFunctionExpression stdLibExpr = (StdLibFunctionExpression)funcRef;
        StdLibFunction stdLib = stdLibExpr.getFunction();

        // Special case: format function, since this function does not need to
        // generate code for the first argument.
        if (stdLib == StdLibFunction.FORMAT) {
            StringTypeInfo ti = (StringTypeInfo)ctxt.typeToTarget(expr.getType());
            return ti.convertFormatStdLib(params, dest, ctxt);
        }

        // 'Normal' standard library calls.
        return convertStdLibFunctionCall(expr, stdLib, params, dest, ctxt);
    }

    /**
     * Construct a call to an internal function in the target language.
     *
     * @param func Function to call.
     * @param paramTexts Arguments of the function.
     * @param dest Storage destination of the result if available, else {@code null}.
     * @param ctxt Code generation context.
     * @return Textual representation of the function call in the target language.
     */
    protected abstract ExprCode convertInternalFunctionCall(InternalFunction func, List<ExprCode> paramTexts,
            Destination dest, CodeContext ctxt);

    /**
     * Convert a call to a standard library function to the target language.
     *
     * <p>
     * The 'fmt' function is a special case, and does not use this function.
     * </p>
     *
     * @param expr Expression to translate.
     * @param stdLib Standard library function being called.
     * @param params Parameters of the call.
     * @param dest Destination to write the value to if available, else {@code null}.
     * @param ctxt Code generation context.
     * @return Textual representation of the standard library call.
     */
    protected ExprCode convertStdLibFunctionCall(Expression expr, StdLibFunction stdLib, List<Expression> params,
            Destination dest, CodeContext ctxt)
    {
        TypeInfo ti = ctxt.typeToTarget(expr.getType());

        switch (stdLib) {
            case ABS:
                Assert.check(params.size() == 1);
                if (ti instanceof IntTypeInfo) {
                    IntTypeInfo intTi = (IntTypeInfo)ti;
                    return intTi.convertAbsStdLib(params.get(0), dest, ctxt);
                } else {
                    Assert.check(ti instanceof RealTypeInfo);
                    RealTypeInfo realTi = (RealTypeInfo)ti;
                    return realTi.convertAbsStdLib(params.get(0), dest, ctxt);
                }

            case MAXIMUM:
                if (ti instanceof IntTypeInfo) {
                    IntTypeInfo intTi = (IntTypeInfo)ti;
                    return intTi.convertMaximumStdLib(params, dest, ctxt);
                } else {
                    Assert.check(ti instanceof RealTypeInfo);
                    RealTypeInfo realTi = (RealTypeInfo)ti;
                    return realTi.convertMaximumStdLib(params, dest, ctxt);
                }

            case MINIMUM:
                if (ti instanceof IntTypeInfo) {
                    IntTypeInfo intTi = (IntTypeInfo)ti;
                    return intTi.convertMinimumStdLib(params, dest, ctxt);
                } else {
                    Assert.check(ti instanceof RealTypeInfo);
                    RealTypeInfo realTi = (RealTypeInfo)ti;
                    return realTi.convertMinimumStdLib(params, dest, ctxt);
                }

            case SIGN:
                Assert.check(params.size() == 1);
                if (ti instanceof IntTypeInfo) {
                    IntTypeInfo intTi = (IntTypeInfo)ti;
                    return intTi.convertSignStdLib(params.get(0), dest, ctxt);
                } else {
                    Assert.check(ti instanceof RealTypeInfo);
                    RealTypeInfo realTi = (RealTypeInfo)ti;
                    return realTi.convertSignStdLib(params.get(0), dest, ctxt);
                }

            case POWER:
                if (ti instanceof IntTypeInfo) {
                    IntTypeInfo intTi = (IntTypeInfo)ti;
                    return intTi.convertPowerStdLib(params, dest, ctxt);
                } else {
                    Assert.check(ti instanceof RealTypeInfo);
                    RealTypeInfo realTi = (RealTypeInfo)ti;
                    return realTi.convertPowerStdLib(params, dest, ctxt);
                }

            case SQRT: {
                Assert.check(params.size() == 1);
                RealTypeInfo realTi = (RealTypeInfo)ti;
                return realTi.convertSqrtStdLib(params.get(0), dest, ctxt);
            }

            case CBRT: {
                Assert.check(params.size() == 1);
                RealTypeInfo realTi = (RealTypeInfo)ti;
                return realTi.convertCbrtStdLib(params.get(0), dest, ctxt);
            }

            case CEIL: {
                Assert.check(params.size() == 1);
                IntTypeInfo intTi = (IntTypeInfo)ti;
                return intTi.convertCeilStdLib(params.get(0), dest, ctxt);
            }

            case FLOOR: {
                Assert.check(params.size() == 1);
                IntTypeInfo intTi = (IntTypeInfo)ti;
                return intTi.convertFloorStdLib(params.get(0), dest, ctxt);
            }

            case ROUND: {
                Assert.check(params.size() == 1);
                IntTypeInfo intTi = (IntTypeInfo)ti;
                return intTi.convertRoundStdLib(params.get(0), dest, ctxt);
            }

            case ACOS:
            case ASIN:
            case ATAN:
            case COS:
            case SIN:
            case TAN: {
                Assert.check(params.size() == 1);
                RealTypeInfo realTi = (RealTypeInfo)ti;
                return realTi.convertTrigonometryStdLib(stdLib, params.get(0), dest, ctxt);
            }

            case EXP:
            case LN:
            case LOG: {
                Assert.check(params.size() == 1);
                RealTypeInfo realTi = (RealTypeInfo)ti;
                return realTi.convertLogarithmicStdLib(stdLib, params.get(0), dest, ctxt);
            }

            case SCALE: {
                RealTypeInfo realTi = (RealTypeInfo)ti;
                return realTi.convertScaleStdLib(params, dest, ctxt);
            }

            case SIZE: {
                Assert.check(params.size() == 1);
                TypeInfo paramTi = ctxt.typeToTarget(params.get(0).getType());
                if (paramTi instanceof ArrayTypeInfo) {
                    ArrayTypeInfo listTi = (ArrayTypeInfo)paramTi;
                    return listTi.convertSizeStdLib(params.get(0), dest, ctxt);
                } else {
                    StringTypeInfo listTi = (StringTypeInfo)paramTi;
                    return listTi.convertSizeStdLib(params.get(0), dest, ctxt);
                }
            }

            case EMPTY: {
                Assert.check(params.size() == 1);
                ArrayTypeInfo arrayTi = (ArrayTypeInfo)ctxt.typeToTarget(params.get(0).getType());
                return arrayTi.convertEmptyStdLib(params.get(0), dest, ctxt);
            }

            case FORMAT: {
                StringTypeInfo stringTi = (StringTypeInfo)ti;
                return stringTi.convertFormatStdLib(params, dest, ctxt);
            }

            case ACOSH:
            case ASINH:
            case ATANH:
            case COSH:
            case SINH:
            case TANH:
                break;

            case BERNOULLI:
            case BETA:
            case BINOMIAL:
            case CONSTANT:
            case ERLANG:
            case EXPONENTIAL:
            case GAMMA:
            case GEOMETRIC:
            case LOG_NORMAL:
            case NORMAL:
            case POISSON:
            case RANDOM:
            case TRIANGLE:
            case UNIFORM:
            case WEIBULL:
                break;

            case DELETE:
            case POP:
                break;

            default:
                break;
        }
        throw new RuntimeException("Untranslated standard library function call: " + stdLib);
    }

    /**
     * Convert a literal list expression to a target language code fragment.
     *
     * @param expr Expression to convert.
     * @param dest Destination to write the value to if available, else {@code null}.
     * @param ctxt Code generation context.
     * @return Code fragment representing the value.
     */
    protected ExprCode convertListExpression(ListExpression expr, Destination dest, CodeContext ctxt) {
        ArrayTypeInfo listTi = (ArrayTypeInfo)ctxt.typeToTarget(expr.getType());
        return listTi.convertLiteral(expr, dest, ctxt);
    }

    /**
     * Convert a literal tuple expression to a target language code fragment.
     *
     * @param expr Expression to convert.
     * @param dest Destination to write the value to if available, else {@code null}.
     * @param ctxt Code generation context.
     * @return Code fragment representing the value.
     */
    protected ExprCode convertTupleExpression(TupleExpression expr, Destination dest, CodeContext ctxt) {
        TupleTypeInfo tupleTi = (TupleTypeInfo)ctxt.typeToTarget(expr.getType());
        return tupleTi.convertLiteral(expr, dest, ctxt);
    }

    /**
     * Convert a reference to a constant, to a target language code fragment.
     *
     * @param expr Expression to convert.
     * @param dest Destination to write the value to if available, else {@code null}.
     * @param ctxt Code generation context.
     * @return Code fragment representing the value.
     */
    protected abstract ExprCode convertConstantExpression(ConstantExpression expr, Destination dest, CodeContext ctxt);

    /**
     * Convert a reference to a discrete variable, to a target language code fragment.
     *
     * @param expr Expression to convert.
     * @param dest Destination to write the value to if available, else {@code null}.
     * @param ctxt Code generation context.
     * @return Code fragment representing the value.
     */
    protected ExprCode convertDiscVariableExpression(DiscVariableExpression expr, Destination dest, CodeContext ctxt) {
        return convertDiscVariableExpression(expr.getVariable(), dest, ctxt);
    }

    /**
     * Convert a reference to a discrete variable, to a target language code fragment.
     *
     * @param var Variable read to convert.
     * @param dest Destination to write the value to if available, else {@code null}.
     * @param ctxt Code generation context.
     * @return Code fragment representing the value.
     */
    public abstract ExprCode convertDiscVariableExpression(DiscVariable var, Destination dest, CodeContext ctxt);

    /**
     * Convert a reference to an algebraic variable, to a target language code fragment.
     *
     * @param expr Expression to convert.
     * @param dest Destination to write the value to if available, else {@code null}.
     * @param ctxt Code generation context.
     * @return Code fragment representing the value.
     */
    protected ExprCode convertAlgVariableExpression(AlgVariableExpression expr, Destination dest, CodeContext ctxt) {
        return convertAlgVariableExpression(expr.getVariable(), dest, ctxt);
    }

    /**
     * Convert a reference to an algebraic variable to a target language code fragment.
     *
     * @param var Variable read to convert.
     * @param dest Destination to write the value to if available, else {@code null}.
     * @param ctxt Code generation context.
     * @return Code fragment representing the value.
     */
    protected abstract ExprCode convertAlgVariableExpression(AlgVariable var, Destination dest, CodeContext ctxt);

    /**
     * Convert a reference to a continuous variable or a reference to the derivative of a continuous variable, to a
     * target language code fragment.
     *
     * @param expr Expression to convert.
     * @param dest Destination to write the value to if available, else {@code null}.
     * @param ctxt Code generation context.
     * @return Code fragment representing the value.
     */
    protected ExprCode convertContVariableExpression(ContVariableExpression expr, Destination dest, CodeContext ctxt) {
        return convertContVariableExpression(expr.getVariable(), expr.isDerivative(), dest, ctxt);
    }

    /**
     * Convert a reference to a continuous variable or a reference to the derivative of a continuous variable, to a
     * target language code fragment.
     *
     * @param var Variable being accessed.
     * @param isDerivative Whether the derivative value should be returned.
     * @param dest Destination to write the value to if available, else {@code null}.
     * @param ctxt Code generation context.
     * @return Code fragment representing the value.
     */
    protected abstract ExprCode convertContVariableExpression(ContVariable var, boolean isDerivative, Destination dest,
            CodeContext ctxt);

    /**
     * Convert a reference to an enumeration literal, to a target language code fragment.
     *
     * @param expr Expression to convert.
     * @param dest Destination to write the value to if available, else {@code null}.
     * @param ctxt Code generation context.
     * @return Code fragment representing the value.
     */
    protected ExprCode convertEnumLiteralExpression(EnumLiteralExpression expr, Destination dest, CodeContext ctxt) {
        EnumTypeInfo enumTi = (EnumTypeInfo)ctxt.typeToTarget(expr.getType());
        return enumTi.convertEnumLiteral(expr, dest, ctxt);
    }

    /**
     * Convert a reference to an input variable, to a target language code fragment.
     *
     * @param expr Expression to convert.
     * @param dest Destination to write the value to if available, else {@code null}.
     * @param ctxt Code generation context.
     * @return Code fragment representing the value.
     */
    protected abstract ExprCode convertInputVariableExpression(InputVariableExpression expr, Destination dest,
            CodeContext ctxt);
}
