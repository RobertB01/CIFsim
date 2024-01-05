//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.chi.typecheck;

import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newBinaryExpression;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newBoolLiteral;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newBoolType;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newCallExpression;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newCastExpression;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newChannelExpression;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newChannelType;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newConstantReference;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newDictType;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newDictionaryExpression;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newDictionaryPair;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newEnumTypeReference;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newEnumValueReference;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newFieldReference;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newFileType;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newFunctionReference;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newFunctionType;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newInstanceType;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newIntNumber;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newIntType;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newListExpression;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newListType;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newMatrixExpression;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newMatrixRow;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newMatrixType;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newModelReference;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newModelType;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newProcessReference;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newProcessType;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newReadCallExpression;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newRealNumber;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newRealType;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newSetExpression;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newSetType;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newSliceExpression;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newStdLibFunctionReference;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newStringLiteral;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newStringType;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newTimeLiteral;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newTimerType;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newTupleExpression;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newTupleField;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newTupleType;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newUnaryExpression;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newVariableReference;
import static org.eclipse.escet.chi.typecheck.CheckType.copyType;
import static org.eclipse.escet.chi.typecheck.CheckType.copyTypes;
import static org.eclipse.escet.chi.typecheck.CheckType.dropReferences;
import static org.eclipse.escet.chi.typecheck.CheckType.isNumericType;
import static org.eclipse.escet.chi.typecheck.CheckType.newBoolDist;
import static org.eclipse.escet.chi.typecheck.CheckType.newIntDist;
import static org.eclipse.escet.chi.typecheck.CheckType.newRealDist;
import static org.eclipse.escet.chi.typecheck.CheckType.tlist;
import static org.eclipse.escet.chi.typecheck.CheckType.transNonvoidType;
import static org.eclipse.escet.chi.typecheck.CheckType.tuplet;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.position.common.PositionUtils.copyPosition;

import java.util.List;

import org.eclipse.escet.chi.metamodel.chi.BinaryExpression;
import org.eclipse.escet.chi.metamodel.chi.BinaryOperators;
import org.eclipse.escet.chi.metamodel.chi.BoolLiteral;
import org.eclipse.escet.chi.metamodel.chi.BoolType;
import org.eclipse.escet.chi.metamodel.chi.CallExpression;
import org.eclipse.escet.chi.metamodel.chi.CastExpression;
import org.eclipse.escet.chi.metamodel.chi.ChannelExpression;
import org.eclipse.escet.chi.metamodel.chi.ChannelOps;
import org.eclipse.escet.chi.metamodel.chi.ConstantDeclaration;
import org.eclipse.escet.chi.metamodel.chi.ConstantReference;
import org.eclipse.escet.chi.metamodel.chi.DictType;
import org.eclipse.escet.chi.metamodel.chi.DictionaryExpression;
import org.eclipse.escet.chi.metamodel.chi.DictionaryPair;
import org.eclipse.escet.chi.metamodel.chi.DistributionType;
import org.eclipse.escet.chi.metamodel.chi.EnumDeclaration;
import org.eclipse.escet.chi.metamodel.chi.EnumValue;
import org.eclipse.escet.chi.metamodel.chi.Expression;
import org.eclipse.escet.chi.metamodel.chi.FileType;
import org.eclipse.escet.chi.metamodel.chi.FunctionDeclaration;
import org.eclipse.escet.chi.metamodel.chi.FunctionType;
import org.eclipse.escet.chi.metamodel.chi.IntNumber;
import org.eclipse.escet.chi.metamodel.chi.IntType;
import org.eclipse.escet.chi.metamodel.chi.ListExpression;
import org.eclipse.escet.chi.metamodel.chi.ListType;
import org.eclipse.escet.chi.metamodel.chi.MatrixExpression;
import org.eclipse.escet.chi.metamodel.chi.MatrixRow;
import org.eclipse.escet.chi.metamodel.chi.MatrixType;
import org.eclipse.escet.chi.metamodel.chi.ModelDeclaration;
import org.eclipse.escet.chi.metamodel.chi.ModelType;
import org.eclipse.escet.chi.metamodel.chi.ProcessDeclaration;
import org.eclipse.escet.chi.metamodel.chi.ProcessType;
import org.eclipse.escet.chi.metamodel.chi.ReadCallExpression;
import org.eclipse.escet.chi.metamodel.chi.RealNumber;
import org.eclipse.escet.chi.metamodel.chi.RealType;
import org.eclipse.escet.chi.metamodel.chi.SetExpression;
import org.eclipse.escet.chi.metamodel.chi.SetType;
import org.eclipse.escet.chi.metamodel.chi.SliceExpression;
import org.eclipse.escet.chi.metamodel.chi.StdLibFunctionReference;
import org.eclipse.escet.chi.metamodel.chi.StdLibFunctions;
import org.eclipse.escet.chi.metamodel.chi.StringLiteral;
import org.eclipse.escet.chi.metamodel.chi.StringType;
import org.eclipse.escet.chi.metamodel.chi.TimeLiteral;
import org.eclipse.escet.chi.metamodel.chi.TimerType;
import org.eclipse.escet.chi.metamodel.chi.TupleExpression;
import org.eclipse.escet.chi.metamodel.chi.TupleField;
import org.eclipse.escet.chi.metamodel.chi.TupleType;
import org.eclipse.escet.chi.metamodel.chi.Type;
import org.eclipse.escet.chi.metamodel.chi.UnaryExpression;
import org.eclipse.escet.chi.metamodel.chi.UnresolvedReference;
import org.eclipse.escet.chi.metamodel.chi.VariableDeclaration;
import org.eclipse.escet.chi.metamodel.chi.VoidType;
import org.eclipse.escet.chi.typecheck.CheckContext.ContextItem;
import org.eclipse.escet.chi.typecheck.symbols.ConstantSymbolEntry;
import org.eclipse.escet.chi.typecheck.symbols.EnumValueSymbolEntry;
import org.eclipse.escet.chi.typecheck.symbols.FunctionDefSymbolEntry;
import org.eclipse.escet.chi.typecheck.symbols.ModelDefSymbolEntry;
import org.eclipse.escet.chi.typecheck.symbols.ProcessDefSymbolEntry;
import org.eclipse.escet.chi.typecheck.symbols.SymbolEntry;
import org.eclipse.escet.chi.typecheck.symbols.VariableSymbolEntry;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Numbers;
import org.eclipse.escet.common.position.metamodel.position.Position;

/** Type check expressions, and decorate them with type information. */
public abstract class CheckExpression {
    /**
     * Verify whether the expressions match with the types (pair-wise) in a call context.
     *
     * @param exprs List of expressions to check.
     * @param types List of types to match.
     * @param call Position information of the function call.
     * @param ctxt Type checker context.
     */
    private static void checkArguments(List<Expression> exprs, List<Type> types, Expression call, CheckContext ctxt) {
        ctxt.checkThrowError(exprs.size() == types.size(), Message.BAD_ARG_COUNT, call.getPosition(),
                String.valueOf(types.size()), String.valueOf(exprs.size()));

        int i;
        for (i = 0; i < exprs.size(); i++) {
            boolean cond = CheckType.matchType(exprs.get(i).getType(), types.get(i));
            ctxt.checkThrowError(cond, Message.ARGUMENT_TYPE_MISMATCH, exprs.get(i).getPosition(),
                    String.valueOf(i + 1), CheckType.toString(types.get(i)),
                    CheckType.toString(exprs.get(i).getType()));
        }
    }

    /**
     * For the standard library functions where just the function name is sufficient, obtain the list of function
     * signatures.
     *
     * @param fn Standard library function queried.
     * @return Available functions of the provided library function.
     */
    private static List<FunctionType> getSimpleStdLibFunctionTypes(StdLibFunctions fn) {
        List<FunctionType> results = list();

        switch (fn) {
            case SIGN:
            case ABS:
                results.add(newFunctionType(tlist(newIntType()), null, newIntType()));
                results.add(newFunctionType(tlist(newRealType()), null, newRealType()));
                return results;

            case ACOS:
            case ACOSH:
            case ASIN:
            case ASINH:
            case ATAN:
            case ATANH:
            case COS:
            case COSH:
            case SIN:
            case SINH:
            case TAN:
            case TANH:
            case LN:
            case LOG:
            case SQRT:
            case CBRT:
            case EXP:
                results.add(newFunctionType(tlist(newRealType()), null, newRealType()));
                return results;

            case CEIL:
            case FLOOR:
            case ROUND:
                results.add(newFunctionType(tlist(newRealType()), null, newIntType()));
                return results;

            case BERNOULLI:
                results.add(newFunctionType(tlist(newRealType()), null, newBoolDist()));
                return results;

            case BETA:
            case GAMMA:
            case LOG_NORMAL:
            case NORMAL:
            case WEIBULL:
                results.add(newFunctionType(tlist(newRealType(), newRealType()), null, newRealDist()));
                return results;

            case BINOMIAL:
                results.add(newFunctionType(tlist(newIntType(), newRealType()), null, newIntDist()));
                return results;

            case CONSTANT:
                results.add(newFunctionType(tlist(newBoolType()), null, newBoolDist()));
                results.add(newFunctionType(tlist(newIntType()), null, newIntDist()));
                results.add(newFunctionType(tlist(newRealType()), null, newRealDist()));
                return results;

            case ERLANG:
                results.add(newFunctionType(tlist(newRealType(), newIntType()), null, newRealDist()));
                return results;

            case EXPONENTIAL:
                results.add(newFunctionType(tlist(newRealType()), null, newRealDist()));
                return results;

            case FINISHED:
                results.add(newFunctionType(tlist(newInstanceType()), null, newBoolType()));
                return results;

            case GEOMETRIC:
            case POISSON:
                results.add(newFunctionType(tlist(newRealType()), null, newIntDist()));
                return results;

            case OPEN:
                results.add(newFunctionType(tlist(newStringType(), newStringType()), null, newFileType()));
                return results;

            case EOL:
            case EOF:
                results.add(newFunctionType(tlist(newFileType()), null, newBoolType()));
                return results;

            case NEWLINES:
                results.add(newFunctionType(tlist(newFileType()), null, newIntType()));
                return results;

            case RANDOM:
                results.add(newFunctionType(list(), null, newRealDist()));
                return results;

            case RANGE:
                results.add(newFunctionType(tlist(newIntType()), null, newListType(newIntType(), null, null)));
                results.add(newFunctionType(tlist(newIntType(), newIntType()), null,
                        newListType(newIntType(), null, null)));
                results.add(newFunctionType(tlist(newIntType(), newIntType(), newIntType()), null,
                        newListType(newIntType(), null, null)));
                return results;

            case READY:
                results.add(newFunctionType(tlist(newTimerType()), null, newBoolType()));
                return results;

            case TRIANGLE:
                results.add(newFunctionType(tlist(newRealType(), newRealType(), newRealType()), null, newRealDist()));
                return results;

            case UNIFORM:
                results.add(newFunctionType(tlist(newIntType(), newIntType()), null, newIntDist()));
                results.add(newFunctionType(tlist(newRealType(), newRealType()), null, newRealDist()));
                return results;

            default:
                Assert.fail("Unknown simple standard library function encountered.");
                return null;
        }
    }

    /**
     * Get the available function types for a standard library call.
     *
     * <p>
     * The cases listed below need the actual parameters to decide. The 'simple' cases are listed in
     * {@link #getSimpleStdLibFunctionTypes}. The latter may still need the actual parameters to make a decision.
     * </p>
     *
     * @param fnr Standard library function reference.
     * @param arguments The actual arguments used in the call of the function. Supplying them is often needed to make a
     *     decision about the type to return here.
     * @param ctxt Type checker context.
     * @return Possible types of the standard library function reference.
     */
    private static List<FunctionType> getAvailableStdLibFunctions(StdLibFunctionReference fnr,
            List<Expression> arguments, CheckContext ctxt)
    {
        Assert.notNull(arguments);
        switch (fnr.getFunction()) {
            case DICT_KEYS: {
                if (arguments.isEmpty()) {
                    return reportZeroArguments("a dictionary argument", fnr.getPosition(), ctxt);
                }
                Type argType = arguments.get(0).getType();
                Type aType = dropReferences(argType);
                if (aType instanceof DictType) {
                    DictType arg0 = (DictType)aType;
                    Type resultType = newListType(copyType(arg0.getKeyType()), null, null);
                    return list(newFunctionType(tlist(copyType(arg0)), null, resultType));
                }
                String expectText = ", expected a dictionary argument";
                ctxt.throwError(Message.FUNC_CALL_WRONG_PARAMETER_TYPE, fnr.getPosition(), "1st",
                        CheckType.toString(argType), expectText);
                return null; // Not reached.
            }

            case DICT_VALUES: {
                if (arguments.isEmpty()) {
                    return reportZeroArguments("a dictionary argument", fnr.getPosition(), ctxt);
                }
                Type argType = arguments.get(0).getType();
                Type aType = dropReferences(argType);
                if (aType instanceof DictType) {
                    DictType arg0 = (DictType)aType;
                    Type rType = newListType(copyType(arg0.getValueType()), null, null);
                    return list(newFunctionType(tlist(copyType(arg0)), null, rType));
                }
                String expectText = ", expected a dictionary argument";
                ctxt.throwError(Message.FUNC_CALL_WRONG_PARAMETER_TYPE, fnr.getPosition(), "1st",
                        CheckType.toString(argType), expectText);
                return null; // Not reached.
            }

            case SORT: {
                if (arguments.isEmpty()) {
                    return reportZeroArguments("a list, and a predicate function argument", fnr.getPosition(), ctxt);
                }
                Type argType = arguments.get(0).getType();
                Type arg0 = dropReferences(argType);
                if (arg0 instanceof ListType) {
                    ListType a0 = (ListType)arg0;
                    Type eType = a0.getElementType();

                    // Create predicate function signature.
                    FunctionType funcType = newFunctionType(list(copyType(eType), copyType(eType)), null,
                            newBoolType());
                    funcType = newFunctionType(list(copyType(arg0), funcType), null, copyType(arg0));
                    return list(funcType);
                }
                String expectText = ", expected a list argument";
                ctxt.throwError(Message.FUNC_CALL_WRONG_PARAMETER_TYPE, fnr.getPosition(), "1st",
                        CheckType.toString(argType), expectText);
                return null; // Not reached.
            }

            case INSERT: {
                if (arguments.isEmpty()) {
                    return reportZeroArguments("a list, a new element, and a predicate function argument",
                            fnr.getPosition(), ctxt);
                }
                Type argType = arguments.get(0).getType();
                Type arg0 = dropReferences(argType);
                if (arg0 instanceof ListType) {
                    ListType a0 = (ListType)arg0;
                    Type eType = a0.getElementType();

                    // Create predicate function signature.
                    FunctionType funcType = newFunctionType(list(copyType(eType), copyType(eType)), null,
                            newBoolType());
                    funcType = newFunctionType(list(copyType(arg0), copyType(eType), funcType), null, copyType(arg0));
                    return list(funcType);
                }
                String expectText = ", expected a list argument";
                ctxt.throwError(Message.FUNC_CALL_WRONG_PARAMETER_TYPE, fnr.getPosition(), "1st",
                        CheckType.toString(argType), expectText);
                return null; // Not reached.
            }

            case ENUMERATE: {
                if (arguments.isEmpty()) {
                    return reportZeroArguments("a list, set, or dictionary argument", fnr.getPosition(), ctxt);
                }
                Type argType = arguments.get(0).getType();
                Type arg0 = dropReferences(argType);
                if (arg0 instanceof ListType) {
                    ListType a0 = (ListType)arg0;
                    Type resType = tuplet(newIntType(), copyType(a0.getElementType()));
                    resType = newListType(resType, null, null);
                    return list(newFunctionType(tlist(copyType(arg0)), null, resType));
                } else if (arg0 instanceof SetType) {
                    SetType a0 = (SetType)arg0;
                    Type resType = tuplet(newIntType(), copyType(a0.getElementType()));
                    resType = newListType(resType, null, null);
                    return list(newFunctionType(tlist(copyType(arg0)), null, resType));
                } else if (arg0 instanceof DictType) {
                    DictType a0 = (DictType)arg0;
                    Type resType = tuplet(copyType(a0.getKeyType()), copyType(a0.getValueType()));
                    resType = tuplet(newIntType(), resType);
                    resType = newListType(resType, null, null);
                    return list(newFunctionType(tlist(copyType(arg0)), null, resType));
                }
                String eText = ", expected a list, set, or dictionary argument";
                ctxt.throwError(Message.FUNC_CALL_WRONG_PARAMETER_TYPE, fnr.getPosition(), "1st",
                        CheckType.toString(argType), eText);
                return null; // Not reached.
            }

            case EMPTY: {
                if (arguments.isEmpty()) {
                    return reportZeroArguments("a list, set, or dictionary argument", fnr.getPosition(), ctxt);
                }
                Type argType = arguments.get(0).getType();
                Type arg0 = dropReferences(argType);
                if (arg0 instanceof ListType || arg0 instanceof SetType || arg0 instanceof DictType) {
                    return list(newFunctionType(tlist(copyType(arg0)), null, newBoolType()));
                }
                String eText = ", expected a list, set, or dictionary argument";
                ctxt.throwError(Message.FUNC_CALL_WRONG_PARAMETER_TYPE, fnr.getPosition(), "1st",
                        CheckType.toString(argType), eText);
                return null; // Not reached.
            }

            case SIZE: {
                if (arguments.isEmpty()) {
                    return reportZeroArguments("a list, set, or dictionary argument", fnr.getPosition(), ctxt);
                }
                Type argType = arguments.get(0).getType();
                Type arg0 = dropReferences(argType);
                if (arg0 instanceof ListType || arg0 instanceof SetType || arg0 instanceof DictType
                        || arg0 instanceof StringType)
                {
                    return list(newFunctionType(tlist(copyType(arg0)), null, newIntType()));
                }
                String eText = ", expected a list, set, or dictionary argument";
                ctxt.throwError(Message.FUNC_CALL_WRONG_PARAMETER_TYPE, fnr.getPosition(), "1st",
                        CheckType.toString(argType), eText);
                return null; // Not reached.
            }

            case MAX:
            case MIN: {
                if (arguments.isEmpty()) {
                    return reportZeroArguments(
                            "a single list, set, or dictionary argument, or two or more numbers or strings",
                            fnr.getPosition(), ctxt);
                } else if (arguments.size() == 1) {
                    Type argType = arguments.get(0).getType();
                    Type arg0 = dropReferences(argType);
                    if (arg0 instanceof ListType) {
                        ListType a0 = (ListType)arg0;
                        return list(newFunctionType(tlist(copyType(arg0)), null, copyType(a0.getElementType())));
                    } else if (arg0 instanceof SetType) {
                        SetType a0 = (SetType)arg0;
                        Type e0 = dropReferences(a0.getElementType());
                        if (e0 instanceof IntType || e0 instanceof RealType || e0 instanceof StringType) {
                            return list(newFunctionType(tlist(copyType(arg0)), null, copyType(e0)));
                        }
                    } else if (arg0 instanceof DictType) {
                        DictType a0 = (DictType)arg0;
                        return list(newFunctionType(tlist(copyType(arg0)), null, copyType(a0.getKeyType())));
                    }
                    String eT = ", expected a list, set, or dictionary argument";
                    ctxt.throwError(Message.FUNC_CALL_WRONG_PARAMETER_TYPE, fnr.getPosition(), "1st",
                            CheckType.toString(argType), eT);
                    return null; // Not reached.
                }
                List<Type> params = list();
                int stringCount = 0; // Argument count with type string.
                boolean hasRealType = false;
                // Process all arguments.
                for (Expression e: arguments) {
                    Type tp = dropReferences(e.getType());
                    Assert.notNull(tp);
                    params.add(copyType(tp));
                    if (tp instanceof StringType) {
                        stringCount++;
                    } else if (tp instanceof RealType) {
                        hasRealType = true;
                    } else if (!(tp instanceof IntType)) {
                        ctxt.throwError(Message.UNEXPECTED_TYPE_MINMAX, e.getPosition(), CheckType.toString(tp));
                    }
                }
                // If there is one string, everything must be a string.
                if (stringCount > 0) {
                    if (stringCount != params.size()) {
                        for (int i = 0; i < arguments.size(); i++) {
                            Type ptp = params.get(i);
                            if (!(ptp instanceof StringType)) {
                                ctxt.throwError(Message.STRING_TYPE_EXPECTED, arguments.get(i).getPosition(),
                                        CheckType.toString(ptp));
                            }
                        }
                        Assert.fail();
                    }
                    Type result = newStringType();
                    return list(newFunctionType(params, null, result));
                }
                // It is all numeric.
                Type result = hasRealType ? newRealType() : newIntType();
                return list(newFunctionType(params, null, result));
            }

            case POP: {
                if (arguments.isEmpty()) {
                    return reportZeroArguments("a list, set, or dictionary argument", fnr.getPosition(), ctxt);
                }
                Type argType = arguments.get(0).getType();
                Type arg0 = dropReferences(argType);
                if (arg0 instanceof ListType) {
                    ListType a0 = (ListType)arg0;
                    return list(newFunctionType(tlist(copyType(arg0)), null,
                            tuplet(copyType(a0.getElementType()), copyType(arg0))));
                } else if (arg0 instanceof SetType) {
                    SetType a0 = (SetType)arg0;
                    return list(newFunctionType(tlist(copyType(arg0)), null,
                            tuplet(copyType(a0.getElementType()), copyType(arg0))));
                } else if (arg0 instanceof DictType) {
                    DictType a0 = (DictType)arg0;
                    return list(newFunctionType(tlist(copyType(arg0)), null,
                            tuplet(copyType(a0.getKeyType()), copyType(a0.getValueType()), copyType(arg0))));
                }
                String eText = ", expected a list, set, or dictionary argument";
                ctxt.throwError(Message.FUNC_CALL_WRONG_PARAMETER_TYPE, fnr.getPosition(), "1st",
                        CheckType.toString(argType), eText);
                return null; // Not reached.
            }

            case DELETE: {
                if (arguments.isEmpty()) {
                    String m = "a list, set, or dictionary, and an index (for a list) or value (otherwise) argument";
                    return reportZeroArguments(m, fnr.getPosition(), ctxt);
                }
                Expression arg1 = arguments.get(0);
                Type tp1 = dropReferences(arg1.getType());
                if (tp1 instanceof ListType) {
                    return list(newFunctionType(tlist(copyType(tp1), newIntType()), null, copyType(tp1)));
                } else if (tp1 instanceof SetType) {
                    SetType st = (SetType)tp1;
                    Type eType = st.getElementType();
                    return list(newFunctionType(list(copyType(st), copyType(eType)), null, copyType(st)));
                } else if (tp1 instanceof DictType) {
                    DictType dt = (DictType)tp1;
                    Type eType = dt.getKeyType();
                    return list(newFunctionType(list(copyType(dt), copyType(eType)), null, copyType(dt)));
                }
                String eText = ", expected a list, set, or dictionary argument";
                ctxt.throwError(Message.FUNC_CALL_WRONG_PARAMETER_TYPE, fnr.getPosition(), "1st",
                        CheckType.toString(tp1), eText);
                return null; // Not reached.
            }

            default:
                return getSimpleStdLibFunctionTypes(fnr.getFunction());
        }
    }

    /**
     * Return the type of the standard library function reference.
     *
     * @param fnr Standard library function reference.
     * @param arguments If available, the actual arguments used in the call of the function. Supplying them is often
     *     needed to make a decision about the type to return here.
     * @param ctxt Type checker context.
     * @return Type of the standard library function reference.
     */
    private static FunctionType getStdLibFunctionType(StdLibFunctionReference fnr, List<Expression> arguments,
            CheckContext ctxt)
    {
        if (arguments == null) {
            ctxt.throwError(Message.STDLIB_FUNC_MUST_BE_CALLED, fnr.getPosition());
            return null; // Never reached, prevents false positive
        }

        List<FunctionType> candidates;
        candidates = getAvailableStdLibFunctions(fnr, arguments, ctxt);
        Assert.notNull(candidates);

        FunctionType result = null;
        int argCount = arguments.size();
        for (FunctionType fn: candidates) {
            if (fn.getParameterTypes().size() != argCount) {
                continue;
            }

            boolean match = true;
            for (int i = 0; i < arguments.size(); i++) {
                Type fnType = fn.getParameterTypes().get(i);
                Type argType = arguments.get(i).getType();
                if (!CheckType.matchType(fnType, argType)) {
                    match = false;
                    break;
                }
            }
            if (!match) {
                continue;
            }

            // Should be the first (and only) matching function.
            Assert.check(result == null);
            result = fn;
        }
        if (result != null) {
            return result; // Normal exit.
        }

        // No exact match found, try to find something nearby for error
        // reporting. There are two cases.
        //
        // - If an argument count matches (foundMatch == true), find the
        // function with the largest number of matching parameter types.
        //
        // - If no argument count matches (foundMatch == false), find the
        // closest number of parameters of the valid functions.
        boolean foundMatch = false;
        int manyCount = -1; // Best "too many" count.
        int fewCount = -1; // Best "too few" count.
        int matchCount = -1;
        FunctionType manyFn = null; // Best function of "too many".
        FunctionType fewFn = null; // Best function of "too few".
        FunctionType matchFn = null;

        for (FunctionType fn: candidates) {
            int fnCount = fn.getParameterTypes().size();

            // Too many valid function parameters.
            if (fnCount < argCount) {
                if (manyCount < fnCount) { // Implies manyCount < 0
                    manyCount = fnCount;
                    manyFn = fn;
                } else if (manyCount == fnCount) {
                    manyFn = null; // Ambiguous function reference.
                }
                continue;
            }

            // Too few valid function parameters.
            if (fnCount > argCount) {
                if (fewCount < 0 || fewCount > fnCount) {
                    fewCount = fnCount;
                    fewFn = fn;
                } else if (fewCount == fnCount) {
                    fewFn = null; // Ambiguous function reference.
                }
                continue;
            }

            // Number of parameters match, do the types match better too?
            foundMatch = true;

            int i;
            for (i = 0; true; i++) {
                Type fnType = fn.getParameterTypes().get(i);
                Type argType = arguments.get(i).getType();
                if (!CheckType.matchType(fnType, argType)) {
                    break;
                }
            }
            if (matchCount < i) { // Implies matchCount < 0
                matchCount = i;
                matchFn = fn;
            } else if (matchCount == i) {
                matchFn = null; // Ambiguous partial match found.
            }
        }

        if (foundMatch) {
            // Report which parameter gives a type match failure.
            Type argType = arguments.get(matchCount).getType();
            String funcText;
            if (matchFn == null) {
                funcText = "";
            } else {
                Type fnType = matchFn.getParameterTypes().get(matchCount);
                funcText = fmt(", expected type \"%s\"", CheckType.toString(fnType));
            }
            ctxt.throwError(Message.FUNC_CALL_WRONG_PARAMETER_TYPE, fnr.getPosition(),
                    Numbers.toOrdinal(matchCount + 1), CheckType.toString(argType), funcText);
        }

        // No matching parameter count found.
        if (manyCount < 0) {
            Assert.check(fewCount >= 0);
            reportTooFewArguments(fnr.getPosition(), argCount, fewCount, fewFn, ctxt);
        } else if (fewCount < 0) {
            reportTooManyArguments(fnr.getPosition(), argCount, manyCount, manyFn, ctxt);
        } else {
            // Both lower and higher counts exist, is there one closer?
            int manyError = argCount - manyCount;
            int fewError = fewCount - argCount;

            if (manyError < fewError) {
                reportTooManyArguments(fnr.getPosition(), argCount, manyCount, manyFn, ctxt);
            } else if (fewError < manyError) {
                reportTooFewArguments(fnr.getPosition(), argCount, fewCount, fewFn, ctxt);
            } else {
                // Report both.
                ctxt.throwError(Message.FUNC_CALL_INCORRECT_NUMBER_PARAMETERS, fnr.getPosition(),
                        String.valueOf(argCount), String.valueOf(fewCount), String.valueOf(manyCount));
            }
        }
        return null; // Not reached.
    }

    /**
     * Report that a function call needs at least one argument.
     *
     * @param expected Description of expected arguments.
     * @param pos Position of the call.
     * @param ctxt Checker context.
     * @return No matching function type found.
     */
    private static List<FunctionType> reportZeroArguments(String expected, Position pos, CheckContext ctxt) {
        ctxt.throwError(Message.FUNC_CALL_WITHOUT_PARAMETERS, pos, expected);
        return null;
    }

    /**
     * Report that a function call does not have a sufficient number of arguments.
     *
     * @param pos Position of the call.
     * @param argCount Number of supplied arguments.
     * @param neededCount Number of needed arguments.
     * @param bestFn Best matching function (may be {@code null} if not found or ambiguous).
     * @param ctxt Checker context.
     */
    private static void reportTooFewArguments(Position pos, int argCount, int neededCount, FunctionType bestFn,
            CheckContext ctxt)
    {
        String txt = (bestFn == null) ? "parameters" : fmt("to call \"%s\"", CheckType.toString(bestFn));
        ctxt.throwError(Message.FUNC_CALL_TOO_FEW_PARAMETERS, pos, String.valueOf(argCount),
                String.valueOf(neededCount), txt);
    }

    /**
     * Report that a function call has too many arguments.
     *
     * @param pos Position of the call.
     * @param argCount Number of supplied arguments.
     * @param neededCount Number of needed arguments.
     * @param bestFn Best matching function (may be {@code null} if not found or ambiguous).
     * @param ctxt Checker context.
     */
    private static void reportTooManyArguments(Position pos, int argCount, int neededCount, FunctionType bestFn,
            CheckContext ctxt)
    {
        String txt = (bestFn == null) ? "parameters" : fmt("to call \"%s\"", CheckType.toString(bestFn));
        ctxt.throwError(Message.FUNC_CALL_TOO_MANY_PARAMETERS, pos, String.valueOf(argCount),
                String.valueOf(neededCount), txt);
    }

    /**
     * Type check a sequence of expressions.
     *
     * @param exprs Expressions to check.
     * @param ctxt Context of the expression.
     * @return Type-checked expressions (without parent).
     */
    public static List<Expression> transExpressionList(List<Expression> exprs, CheckContext ctxt) {
        List<Expression> newExprs = list();
        for (Expression e: exprs) {
            newExprs.add(transExpression(e, ctxt));
        }
        return newExprs;
    }

    /**
     * Type check an expression.
     *
     * @param expr Expression to check.
     * @param ctxt Context of the expression.
     * @return Type-checked expression (without parent).
     */
    public static Expression transExpression(Expression expr, CheckContext ctxt) {
        if (expr instanceof BinaryExpression) {
            return transBinaryExpression((BinaryExpression)expr, ctxt);
        } else if (expr instanceof BoolLiteral) {
            return transBoolLiteral((BoolLiteral)expr);
        } else if (expr instanceof RealNumber) {
            return transRealNumber((RealNumber)expr, ctxt);
        } else if (expr instanceof IntNumber) {
            return transNumber((IntNumber)expr, ctxt);
        } else if (expr instanceof StringLiteral) {
            return transStringLiteral((StringLiteral)expr);
        } else if (expr instanceof CallExpression) {
            return transCallExpression((CallExpression)expr, ctxt);
        } else if (expr instanceof DictionaryExpression) {
            return transDictionaryExpression((DictionaryExpression)expr, ctxt);
        } else if (expr instanceof TupleExpression) {
            return transTupleExpression((TupleExpression)expr, ctxt);
        } else if (expr instanceof SetExpression) {
            return transSetExpression((SetExpression)expr, ctxt);
        } else if (expr instanceof ListExpression) {
            return transListExpression((ListExpression)expr, ctxt);
        } else if (expr instanceof TimeLiteral) {
            return transTimeLiteral((TimeLiteral)expr, ctxt);
        } else if (expr instanceof UnresolvedReference) {
            return transUnresolvedReference((UnresolvedReference)expr, ctxt);
        } else if (expr instanceof MatrixExpression) {
            return transMatrixExpression((MatrixExpression)expr, ctxt);
        } else if (expr instanceof ReadCallExpression) {
            return transReadCallExpression((ReadCallExpression)expr, ctxt);
        } else if (expr instanceof ChannelExpression) {
            return transChannelExpression((ChannelExpression)expr, ctxt);
        } else if (expr instanceof SliceExpression) {
            return transSliceExpression((SliceExpression)expr, ctxt);
        } else if (expr instanceof UnaryExpression) {
            return transUnaryExpression((UnaryExpression)expr, ctxt);
        } else if (expr instanceof StdLibFunctionReference) {
            return transStdLibFunctionReference((StdLibFunctionReference)expr, null, ctxt);
        } else if (expr instanceof CastExpression) {
            return transCastExpression((CastExpression)expr, ctxt);
        }

        Assert.fail("Unknown Expression class encountered");
        return null;
    }

    /**
     * Type-check a 'time' reference, and decorate it with type information.
     *
     * @param e Expression to check and decorate.
     * @param ctxt Context of the expression.
     * @return Decorated version of the expression.
     */
    private static Expression transTimeLiteral(TimeLiteral e, CheckContext ctxt) {
        if (ctxt.contains(CheckContext.ContextItem.NO_TIME)) {
            ctxt.throwError(Message.USE_OF_TIME_NOT_ALLOWED, e.getPosition());
        }

        return newTimeLiteral(copyPosition(e), newRealType());
    }

    /**
     * Type-check a boolean value literal, and decorate it with type information.
     *
     * @param e Expression to check and decorate.
     * @return Decorated version of the expression.
     */
    private static Expression transBoolLiteral(BoolLiteral e) {
        return newBoolLiteral(copyPosition(e), newBoolType(), e.isValue());
    }

    /**
     * Type-check a real number literal, and decorate it with type information.
     *
     * @param e Expression to check and decorate.
     * @param ctxt Context of the expression.
     * @return Decorated version of the expression.
     */
    private static Expression transRealNumber(RealNumber e, CheckContext ctxt) {
        // Verify that the literal is not too large (underflow is not
        // undetected).
        double d = Double.valueOf(e.getValue());
        if (Double.isInfinite(d)) {
            ctxt.throwError(Message.NUMBER_OUT_OF_RANGE, e.getPosition());
        }
        return newRealNumber(copyPosition(e), newRealType(), e.getValue());
    }

    /**
     * Type-check an integer number literal, and decorate it with type information.
     *
     * @param e Expression to check and decorate.
     * @param ctxt Context of the expression.
     * @return Decorated version of the expression.
     */
    private static Expression transNumber(IntNumber e, CheckContext ctxt) {
        try {
            Integer.parseInt(e.getValue(), 10);
        } catch (NumberFormatException exc) {
            ctxt.throwError(Message.NUMBER_OUT_OF_RANGE, e.getPosition());
        }
        return newIntNumber(copyPosition(e), newIntType(), e.getValue());
    }

    /**
     * Is the given type a boolean, integer, or real type?
     *
     * <p>
     * Function does not drop type references!
     * </p>
     *
     * @param tp Type to test.
     * @return Whether the provided type is a boolean, integer, or real type.
     */
    private static boolean isBoolIntRealType(Type tp) {
        return tp instanceof BoolType || tp instanceof IntType || tp instanceof RealType;
    }

    /**
     * Type-check and convert a cast expression.
     *
     * @param e Expression to check and decorate.
     * @param ctxt Context of the expression.
     * @return Decorated version of the expression.
     */
    private static Expression transCastExpression(CastExpression e, CheckContext ctxt) {
        Expression child = transExpression(e.getExpression(), ctxt);
        Type exprType = dropReferences(child.getType());
        if (exprType instanceof ListType) {
            ListType lt = (ListType)exprType;
            // Source expression is a list, can cast to a matrix or a set.
            //
            // Note that the parser enforces concrete type keywords, it is not
            // possible to use type references in the cast type at top-level.
            if (e.getCastType() instanceof MatrixType) {
                Type resType = transNonvoidType(e.getCastType(), ctxt);
                Type elemType = dropReferences(lt.getElementType());
                boolean cond = elemType instanceof RealType;
                ctxt.checkThrowError(cond, Message.CANNOT_CAST, e.getPosition(), CheckType.toString(exprType),
                        CheckType.toString(resType));
                return newCastExpression(resType, child, copyPosition(e), copyType(resType));
            } else if (e.getCastType() instanceof SetType) {
                // Element type of the set is not specified, as it is derived
                // from the list child expression.
                Type newType = copyType(dropReferences(lt.getElementType()));
                newType = newSetType(newType, e.getCastType().getPosition());
                return newCastExpression(newType, child, copyPosition(e), copyType(newType));
            } else {
                // Bad cast expression from a list, throw an error.
                Type newType = transNonvoidType(e.getCastType(), ctxt);
                Type resType = dropReferences(newType);

                ctxt.throwError(Message.CANNOT_CAST, e.getPosition(), CheckType.toString(exprType),
                        CheckType.toString(resType));
            }
        }
        // Not a child with a list type.

        Type newType = transNonvoidType(e.getCastType(), ctxt);
        Type resType = dropReferences(newType);
        if (CheckType.matchType(exprType, resType)) {
            return child; // No need to cast to itself.
        }
        // Give a nice error for "(int)3.0"
        if (resType instanceof IntType && exprType instanceof RealType) {
            ctxt.throwError(Message.NO_INT_REAL_CAST, e.getPosition());
        }
        // throw an error when real(<timer-expr>) is not allowed.
        if (ctxt.contains(ContextItem.NO_REAL_TIMER_CAST) && resType instanceof RealType
                && exprType instanceof TimerType)
        {
            ctxt.throwError(Message.REAL_TIMER_CAST_NOT_ALLOWED, e.getPosition());
        }

        boolean cond = resType instanceof TimerType && exprType instanceof RealType;
        cond |= resType instanceof RealType && exprType instanceof TimerType;
        cond |= isBoolIntRealType(resType) && exprType instanceof StringType;
        cond |= resType instanceof StringType && isBoolIntRealType(exprType);
        cond |= resType instanceof RealType && exprType instanceof IntType;

        ctxt.checkThrowError(cond, Message.CANNOT_CAST, e.getPosition(), CheckType.toString(exprType),
                CheckType.toString(resType));

        return newCastExpression(newType, child, copyPosition(e), copyType(newType));
    }

    /**
     * Type-check a string literal, and decorate it with type information.
     *
     * @param e Expression to check and decorate.
     * @return Decorated version of the expression.
     */
    private static Expression transStringLiteral(StringLiteral e) {
        return newStringLiteral(copyPosition(e), newStringType(), e.getValue());
    }

    /**
     * Type-check a matrix expression, and decorate it with type information.
     *
     * @param e Expression to check and decorate.
     * @param ctxt Context of the expression.
     * @return Decorated version of the expression.
     */
    private static Expression transMatrixExpression(MatrixExpression e, CheckContext ctxt) {
        int colLength = -1; // Column length check
        List<MatrixRow> newRows = list();
        for (MatrixRow r: e.getRows()) {
            List<Expression> vals = list();
            for (Expression elm: r.getElements()) {
                Expression me = transExpression(elm, ctxt);
                Type tme = dropReferences(me.getType());

                boolean cond = tme instanceof RealType;
                ctxt.checkThrowError(cond, Message.MATRIX_LITERAL_REAL_ELEMENTS, elm.getPosition(),
                        CheckType.toString(tme));

                vals.add(me);
            }
            // Check for equal length of each row.
            if (colLength < 0) {
                colLength = r.getElements().size();
            } else {
                // Check row length with first row.
                boolean cond = colLength == vals.size();
                ctxt.checkThrowError(cond, Message.MATRIX_LITERAL_ROW_LENGTH, r.getPosition(),
                        String.valueOf(colLength), String.valueOf(vals.size()));
            }
            MatrixRow mr = newMatrixRow(vals, copyPosition(r));
            newRows.add(mr);
        }
        Assert.check(colLength > 0);
        Expression columnSize = newIntNumber(null, newIntType(), String.valueOf(colLength));
        Expression rowSize = newIntNumber(null, newIntType(), String.valueOf(newRows.size()));
        Type type = newMatrixType(columnSize, null, rowSize);
        return newMatrixExpression(copyPosition(e), newRows, type);
    }

    /**
     * Compute the smallest common type of a sequence of expressions.
     *
     * @param exprs Non-empty sequence of converted expressions.
     * @param msg Message to report when no smallest type exists.
     * @param ctxt Context of the expressions.
     * @return The smallest common type of the expressions.
     */
    private static Type getExprsCommonType(List<Expression> exprs, Message msg, CheckContext ctxt) {
        Assert.check(!exprs.isEmpty());

        Type elmType = null;
        for (Expression elm: exprs) {
            if (elmType == null) {
                elmType = elm.getType();
            } else {
                Type sType = CheckType.smallestType(elmType, elm.getType());
                ctxt.checkThrowError(sType != null, msg, elm.getPosition(), CheckType.toString(elmType),
                        CheckType.toString(elm.getType()));
                elmType = sType;
            }
        }
        return elmType;
    }

    /**
     * Type-check a set expression literal, and decorate it with type information.
     *
     * @param e Expression to check and decorate.
     * @param ctxt Context of the expression.
     * @return Decorated version of the expression.
     */
    private static Expression transSetExpression(SetExpression e, CheckContext ctxt) {
        if (e.getElements().isEmpty()) {
            Assert.notNull(e.getType());
            Type newType = transNonvoidType(e.getType(), ctxt);
            return newSetExpression(null, copyPosition(e), newType);
        }
        List<Expression> newVals = transExpressionList(e.getElements(), ctxt);
        Type elmType = getExprsCommonType(newVals, Message.SET_LITERAL_ELEMENT_TYPE, ctxt);

        return newSetExpression(newVals, copyPosition(e), newSetType(copyType(elmType), null));
    }

    /**
     * Type-check a list expression literal, and decorate it with type information.
     *
     * @param e Expression to check and decorate.
     * @param ctxt Context of the expression.
     * @return Decorated version of the expression.
     */
    private static Expression transListExpression(ListExpression e, CheckContext ctxt) {
        if (e.getElements().isEmpty()) {
            Assert.notNull(e.getType());
            Type newType = transNonvoidType(e.getType(), ctxt);
            return newListExpression(null, copyPosition(e), newType);
        }
        List<Expression> newVals = transExpressionList(e.getElements(), ctxt);
        Type elmType = getExprsCommonType(newVals, Message.LIST_LITERAL_ELEMENT_TYPE, ctxt);

        Expression initLength = newIntNumber(null, newIntType(), "0");
        return newListExpression(newVals, copyPosition(e), newListType(copyType(elmType), initLength, null));
    }

    /**
     * Type-check a tuple expression literal, and decorate it with type information.
     *
     * @param e Expression to check and decorate.
     * @param ctxt Context of the expression.
     * @return Decorated version of the expression.
     */
    private static Expression transTupleExpression(TupleExpression e, CheckContext ctxt) {
        List<TupleField> fldTypes = list();
        List<Expression> fields = list();
        for (Expression fld: e.getFields()) {
            Expression nf = transExpression(fld, ctxt);
            fields.add(nf);
            fldTypes.add(newTupleField(null, null, copyType(nf.getType())));
        }
        return newTupleExpression(fields, copyPosition(e), newTupleType(fldTypes, null));
    }

    /**
     * Type-check a 'read' call expression, and decorate it with type information.
     *
     * @param e Expression to check and decorate.
     * @param ctxt Context of the expression.
     * @return Decorated version of the expression.
     */
    private static Expression transReadCallExpression(ReadCallExpression e, CheckContext ctxt) {
        if (ctxt.contains(ContextItem.NO_READ)) {
            ctxt.throwError(Message.READ_NOT_ALLOWED, e.getPosition());
        }

        Expression newFile = null;
        if (e.getFile() != null) {
            newFile = transExpression(e.getFile(), ctxt);
            Type tNewFile = dropReferences(newFile.getType());

            boolean cond = tNewFile instanceof FileType;
            ctxt.checkThrowError(cond, Message.FILE_TYPE_EXPECTED, e.getFile().getPosition(),
                    CheckType.toString(tNewFile));
        }
        Type newType = transNonvoidType(e.getLoadType(), ctxt);
        return newReadCallExpression(newFile, newType, copyPosition(e), copyType(newType));
    }

    /**
     * Type-check a 'channel' creation expression (constructs a new channel at runtime), and decorate it with type
     * information.
     *
     * @param e Expression to check and decorate.
     * @param ctxt Context of the expression.
     * @return Decorated version of the expression.
     */
    private static Expression transChannelExpression(ChannelExpression e, CheckContext ctxt) {
        if (ctxt.contains(ContextItem.NO_CHANNEL)) {
            ctxt.throwError(Message.CHANNEL_NOT_ALLOWED, e.getPosition());
        }

        Type newType = transNonvoidType(e.getElementType(), ctxt);
        Type chanType = newChannelType(copyType(newType), ChannelOps.SEND_RECEIVE, null);
        return newChannelExpression(newType, copyPosition(e), chanType);
    }

    /**
     * Type-check a function call expression, and decorate it with type information.
     *
     * @param e Expression to check and decorate.
     * @param ctxt Context of the expression.
     * @return Decorated version of the expression.
     */
    private static Expression transCallExpression(CallExpression e, CheckContext ctxt) {
        // Process arguments.
        List<Expression> newArgs = list();
        for (Expression arg: e.getArguments()) {
            newArgs.add(transExpression(arg, ctxt));
        }

        // Process the function application / process instance.

        // stdlib function.
        if (e.getFunction() instanceof StdLibFunctionReference) {
            StdLibFunctionReference fnr;
            FunctionType fntp;
            Expression newFunc;
            fnr = (StdLibFunctionReference)e.getFunction();
            fntp = getStdLibFunctionType(fnr, newArgs, ctxt);
            newFunc = transStdLibFunctionReference(fnr, newArgs, ctxt);
            boolean cond = e.getName() == null;
            ctxt.checkThrowError(cond, Message.FUNCTION_CALL_NO_NAME, e.getFunction().getPosition());

            return newCallExpression(newArgs, newFunc, null, copyPosition(e), copyType(fntp.getResultType()));
        }
        Expression newFunc = transExpression(e.getFunction(), ctxt);
        Type tNewFunc = dropReferences(newFunc.getType());

        // Is it a user function?
        if (tNewFunc instanceof FunctionType) {
            FunctionType fntp = (FunctionType)tNewFunc;
            checkArguments(newArgs, fntp.getParameterTypes(), e, ctxt);

            boolean cond = e.getName() == null;
            ctxt.checkThrowError(cond, Message.FUNCTION_CALL_NO_NAME, e.getFunction().getPosition());

            return newCallExpression(newArgs, newFunc, null, copyPosition(e), copyType(fntp.getResultType()));
        }

        // Process instance name.
        Expression newName = null;
        if (e.getName() != null) {
            newName = transExpression(e.getName(), ctxt);
            Type t = dropReferences(newName.getType());

            boolean cond = t instanceof StringType || t instanceof IntType;
            ctxt.checkThrowError(cond, Message.STRING_INT_TYPE_EXPECTED, e.getName().getPosition(),
                    CheckType.toString(t));
        }

        // Instantiates a process.
        if (tNewFunc instanceof ProcessType) {
            ProcessType ptp = (ProcessType)tNewFunc;
            checkArguments(newArgs, ptp.getParameterTypes(), e, ctxt);
            return newCallExpression(newArgs, newFunc, newName, copyPosition(e), newInstanceType());
        }
        // Instantiates a model.
        if (tNewFunc instanceof ModelType) {
            ModelType mtp = (ModelType)tNewFunc;
            checkArguments(newArgs, mtp.getParameterTypes(), e, ctxt);
            Type exitType = mtp.getExitType();
            if (exitType != null) {
                exitType = dropReferences(exitType);
            }
            if (exitType == null || exitType instanceof VoidType) {
                ctxt.throwError(Message.CANNOT_CALL_NONVOID_MODEL, e.getFunction().getPosition());
            }
            return newCallExpression(newArgs, newFunc, newName, copyPosition(e), copyType(exitType));
        }
        ctxt.throwError(Message.NOT_CALLABLE, copyPosition(e.getFunction()));
        return null; // Never reached.
    }

    /**
     * Type-check a dictionary literal expression, and decorate it with type information.
     *
     * @param e Expression to check and decorate.
     * @param ctxt Context of the expression.
     * @return Decorated version of the expression.
     */
    private static Expression transDictionaryExpression(DictionaryExpression e, CheckContext ctxt) {
        if (e.getPairs().isEmpty()) {
            Assert.notNull(e.getType());
            Type newType = transNonvoidType(e.getType(), ctxt);
            return newDictionaryExpression(null, copyPosition(e), newType);
        }
        // Non-empty dictionary expression.
        Type keyType = null;
        Type valueType = null;
        List<DictionaryPair> newPairs = list();
        for (DictionaryPair dp: e.getPairs()) {
            Expression newKey = transExpression(dp.getKey(), ctxt);
            Expression newVal = transExpression(dp.getValue(), ctxt);
            newPairs.add(newDictionaryPair(newKey, copyPosition(dp), newVal));

            Type newType;
            // Check key type.
            if (keyType == null) {
                newType = newKey.getType();
            } else {
                newType = CheckType.smallestType(keyType, newKey.getType());
                ctxt.checkThrowError(newType != null, Message.DICT_LITERAL_KEY_TYPE, dp.getKey().getPosition(),
                        CheckType.toString(keyType), CheckType.toString(newKey.getType()));
            }
            keyType = newType;

            // Check value type
            if (valueType == null) {
                newType = newVal.getType();
            } else {
                newType = CheckType.smallestType(valueType, newVal.getType());
                ctxt.checkThrowError(newType != null, Message.DICT_LITERAL_VALUE_TYPE, dp.getValue().getPosition(),
                        CheckType.toString(valueType), CheckType.toString(newVal.getType()));
            }
            valueType = newType;
        }
        Type dictType = newDictType(copyType(keyType), null, copyType(valueType));
        return newDictionaryExpression(newPairs, copyPosition(e), dictType);
    }

    /**
     * Type-check a binary expression, and decorate it with type information.
     *
     * @param e Expression to check and decorate.
     * @param ctxt Context of the expression.
     * @return Decorated version of the expression.
     */
    private static Expression transBinaryExpression(BinaryExpression e, CheckContext ctxt) {
        Expression newLeft = transExpression(e.getLeft(), ctxt);
        Type tNewLeft = dropReferences(newLeft.getType());

        // Special case <tuple>.<right>, where <right> is not a sub-expression
        // that should be computed separately.
        if (e.getOp() == BinaryOperators.FIELD_PROJECTION) {
            if (tNewLeft instanceof TupleType) {
                boolean cond = e.getRight() instanceof UnresolvedReference;
                ctxt.checkThrowError(cond, Message.RHS_TUPLE_PROJ_FIELD_NAME, e.getRight().getPosition());

                String name = ((UnresolvedReference)e.getRight()).getName();
                Assert.check(!name.isEmpty());

                List<TupleField> fields;
                fields = ((TupleType)tNewLeft).getFields();
                int i;
                for (i = 0; i < fields.size(); i++) {
                    TupleField tf = fields.get(i);
                    if (name.equals(tf.getName())) {
                        Expression newRight = newFieldReference(tf, null, copyType(tf.getType()));
                        return newBinaryExpression(newLeft, BinaryOperators.PROJECTION, copyPosition(e), newRight,
                                copyType(tf.getType()));
                    }
                }
                ctxt.throwError(Message.RHS_TUPLE_PROJ_UNKNOWN_NAME, e.getRight().getPosition(),
                        CheckType.toString(tNewLeft), name);
            }
            // Field projection with lhs not a tuple.
            ctxt.throwError(Message.LHS_TUPLE_PROJ_NOT_A_TUPLE, e.getLeft().getPosition(),
                    CheckType.toString(tNewLeft));
        }

        Expression newRight = transExpression(e.getRight(), ctxt);
        Type tNewRight = dropReferences(newRight.getType());

        Type resultType = null;
        switch (e.getOp()) {
            case CONJUNCTION:
            case DISJUNCTION: {
                ctxt.checkThrowError(tNewLeft instanceof BoolType, Message.LHS_OPERAND_MUST_HAVE_BOOL_TYPE,
                        e.getLeft().getPosition(), CheckType.toString(tNewLeft));

                ctxt.checkThrowError(tNewRight instanceof BoolType, Message.RHS_OPERAND_MUST_HAVE_BOOL_TYPE,
                        e.getRight().getPosition(), CheckType.toString(tNewRight));

                resultType = newBoolType();
                break;
            }

            case DIVISION:
            case POWER: {
                ctxt.checkThrowError(isNumericType(tNewLeft), Message.LHS_OPERAND_MUST_HAVE_NUMERIC_TYPE,
                        e.getLeft().getPosition(), CheckType.toString(tNewLeft));

                ctxt.checkThrowError(isNumericType(tNewRight), Message.RHS_OPERAND_MUST_HAVE_NUMERIC_TYPE,
                        e.getRight().getPosition(), CheckType.toString(tNewRight));

                resultType = newRealType();
                break;
            }

            case EQUAL:
            case NOT_EQUAL: {
                if (isNumericType(tNewLeft) && isNumericType(tNewRight)) {
                    resultType = newBoolType();
                    break;
                } else {
                    Type st = CheckType.smallestType(tNewLeft, tNewRight);
                    if (st != null) {
                        resultType = newBoolType();
                        break;
                    }
                }
                ctxt.throwError(Message.OPERANDS_EQUAL_OR_NUMERIC, e.getPosition(), CheckType.toString(tNewLeft),
                        CheckType.toString(tNewRight));
                break; // Not reached.
            }

            case FLOOR_DIVISION:
            case MODULUS: {
                ctxt.checkThrowError(tNewLeft instanceof IntType, Message.LHS_OPERAND_MUST_HAVE_INT_TYPE,
                        e.getLeft().getPosition(), CheckType.toString(tNewLeft));

                ctxt.checkThrowError(tNewRight instanceof IntType, Message.RHS_OPERAND_MUST_HAVE_INT_TYPE,
                        e.getRight().getPosition(), CheckType.toString(tNewRight));

                resultType = newIntType();
                break;
            }

            case GREATER_EQUAL:
            case GREATER_THAN:
            case LESS_THAN:
            case LESS_EQUAL: {
                boolean cond = isNumericType(tNewLeft) || tNewLeft instanceof StringType;
                ctxt.checkThrowError(cond, Message.LHS_MUST_NUMERIC_STRING_TYPE, e.getLeft().getPosition(),
                        CheckType.toString(tNewLeft));

                if (isNumericType(tNewLeft)) {
                    ctxt.checkThrowError(isNumericType(tNewRight), Message.RHS_OPERAND_MUST_HAVE_NUMERIC_TYPE,
                            e.getRight().getPosition(), CheckType.toString(tNewRight));

                    resultType = newBoolType();
                    break;
                } else {
                    Assert.check(tNewLeft instanceof StringType);
                    ctxt.checkThrowError(tNewRight instanceof StringType, Message.RHS_OPERAND_MUST_HAVE_STRING_TYPE,
                            e.getRight().getPosition(), CheckType.toString(tNewRight));

                    resultType = newBoolType();
                    break;
                }
                // Never reached.
            }

            case ADDITION: {
                if (tNewLeft instanceof StringType && tNewRight instanceof StringType) {
                    resultType = newStringType();
                    break;
                }
                if (isNumericType(tNewLeft) && isNumericType(tNewRight)) {
                    if (tNewLeft instanceof IntType && tNewRight instanceof IntType) {
                        resultType = newIntType();
                    } else {
                        resultType = newRealType();
                    }
                    break;
                }
                if (tNewLeft instanceof ListType || tNewLeft instanceof SetType || tNewLeft instanceof DictType) {
                    Type st = CheckType.smallestType(tNewLeft, tNewRight);
                    if (st != null) {
                        resultType = copyType(st);
                        break;
                    }
                }

                if (tNewLeft instanceof TupleType && tNewRight instanceof TupleType) {
                    List<TupleField> lf = ((TupleType)tNewLeft).getFields();
                    List<TupleField> rf = ((TupleType)tNewRight).getFields();
                    List<TupleField> rslt;
                    rslt = listc(lf.size() + rf.size());
                    for (TupleField fld: lf) {
                        rslt.add(newTupleField("", null, copyType(fld.getType())));
                    }
                    for (TupleField fld: rf) {
                        rslt.add(newTupleField("", null, copyType(fld.getType())));
                    }
                    resultType = newTupleType(rslt, null);
                    break;
                }
                ctxt.throwError(Message.BAD_ADDITION, e.getPosition(), CheckType.toString(tNewLeft),
                        CheckType.toString(tNewRight));
                return null; // Never reached.
            }

            case MULTIPLICATION: {
                if (isNumericType(tNewLeft) && isNumericType(tNewRight)) {
                    if (tNewLeft instanceof IntType && tNewRight instanceof IntType) {
                        resultType = newIntType();
                    } else {
                        resultType = newRealType();
                    }
                    break;
                }

                if (tNewLeft instanceof SetType) {
                    Type st = CheckType.smallestType(tNewLeft, tNewRight);
                    if (st != null) {
                        resultType = copyType(st);
                        break;
                    }
                }
                ctxt.throwError(Message.BAD_MULTIPLICATION, e.getPosition(), CheckType.toString(tNewLeft),
                        CheckType.toString(tNewRight));
                return null; // Never reached.
            }

            case SUBTRACTION: {
                if (isNumericType(tNewLeft) && isNumericType(tNewRight)) {
                    if (tNewLeft instanceof IntType && tNewRight instanceof IntType) {
                        resultType = newIntType();
                    } else {
                        resultType = newRealType();
                    }
                    break;
                }

                // set - set / dict - dict / list - list
                if ((tNewLeft instanceof SetType && tNewRight instanceof SetType)
                        || (tNewLeft instanceof ListType && tNewRight instanceof ListType)
                        || (tNewLeft instanceof DictType && tNewRight instanceof DictType))
                {
                    Type st = CheckType.smallestType(tNewLeft, tNewRight);
                    if (st != null) {
                        resultType = copyType(tNewLeft);
                        break;
                    }
                }

                // dict - set
                if (tNewLeft instanceof DictType && tNewRight instanceof SetType) {
                    DictType dt = (DictType)tNewLeft;
                    SetType st = (SetType)tNewRight;
                    Type smt = CheckType.smallestType(dt.getKeyType(), st.getElementType());
                    if (smt != null) {
                        resultType = copyType(dt);
                        break;
                    }
                }

                // dict - list
                if (tNewLeft instanceof DictType && tNewRight instanceof ListType) {
                    DictType dt = (DictType)tNewLeft;
                    ListType st = (ListType)tNewRight;
                    Type smt = CheckType.smallestType(dt.getKeyType(), st.getElementType());
                    if (smt != null) {
                        resultType = copyType(dt);
                        break;
                    }
                }
                ctxt.throwError(Message.BAD_SUBTRACTION, e.getPosition(), CheckType.toString(tNewLeft),
                        CheckType.toString(tNewRight));
                return null; // Never reached.
            }

            case ELEMENT_TEST: {
                Type elementType = null;
                if (tNewRight instanceof ListType) {
                    ListType tp = (ListType)tNewRight;
                    elementType = tp.getElementType();
                } else if (tNewRight instanceof SetType) {
                    SetType tp = (SetType)tNewRight;
                    elementType = tp.getElementType();
                } else if (tNewRight instanceof DictType) {
                    DictType tp = (DictType)tNewRight;
                    elementType = tp.getKeyType();
                }

                boolean cond = (elementType != null);
                ctxt.checkThrowError(cond, Message.LIST_SET_DICT_EXPECTED, e.getRight().getPosition(),
                        CheckType.toString(tNewRight));

                Type t = CheckType.smallestType(tNewLeft, elementType);
                cond = (t != null);
                ctxt.checkThrowError(cond, Message.LHS_MATCH_NOT_ELEMENT_TYPE, e.getPosition(),
                        CheckType.toString(elementType), CheckType.toString(tNewLeft));

                resultType = newBoolType();
                break;
            }

            case PROJECTION:
                // Projection on lists (gives element type).
                if (tNewLeft instanceof ListType) {
                    boolean cond = tNewRight instanceof IntType;
                    ctxt.checkThrowError(cond, Message.INDEX_EXPR_MUST_BE_INT, e.getRight().getPosition(),
                            CheckType.toString(tNewRight));

                    ListType tp = (ListType)tNewLeft;
                    resultType = copyType(tp.getElementType());
                    break;
                }
                // Projection on strings (gives another string).
                if (tNewLeft instanceof StringType) {
                    boolean cond = tNewRight instanceof IntType;
                    ctxt.checkThrowError(cond, Message.INDEX_EXPR_MUST_BE_INT, e.getRight().getPosition(),
                            CheckType.toString(tNewRight));

                    resultType = copyType(tNewLeft);
                    break;
                }
                // Projection on dictionaries (gives value type).
                if (tNewLeft instanceof DictType) {
                    DictType dt = (DictType)tNewLeft;

                    boolean cond = CheckType.matchType(tNewRight, dt.getKeyType());
                    ctxt.checkThrowError(cond, Message.DICT_PROJ_TYPE_MATCH, e.getRight().getPosition(),
                            CheckType.toString(dt.getKeyType()), CheckType.toString(tNewRight));

                    resultType = copyType(dt.getValueType());
                    break;
                }
                // Tuple projection already handled near top of the function.
                ctxt.throwError(Message.TYPE_HAS_NO_PROJECTION, e.getPosition(), CheckType.toString(tNewLeft));
                break;

            case SUBSET: {
                boolean cond = tNewLeft instanceof SetType;
                ctxt.checkThrowError(cond, Message.SET_EXPECTED, e.getPosition(), CheckType.toString(tNewLeft));

                Type t = CheckType.smallestType(tNewLeft, tNewRight);
                ctxt.checkThrowError((t != null), Message.OPERANDS_EQUAL, e.getPosition(), CheckType.toString(tNewLeft),
                        CheckType.toString(tNewRight));

                resultType = newBoolType();
                break;
            }

            default:
                Assert.fail("Unknown binary operator encountered.");
        }
        Assert.check(resultType != null);
        return newBinaryExpression(newLeft, e.getOp(), copyPosition(e), newRight, resultType);
    }

    /**
     * Type-check a unary expression, and decorate it with type information.
     *
     * @param e Expression to check and decorate.
     * @param ctxt Context of the expression.
     * @return Decorated version of the expression.
     */
    private static Expression transUnaryExpression(UnaryExpression e, CheckContext ctxt) {
        Expression newChild = transExpression(e.getChild(), ctxt);
        Type tNewChild = dropReferences(newChild.getType());

        Type resultType = null;
        switch (e.getOp()) {
            case INVERSE: {
                boolean cond = tNewChild instanceof BoolType;
                ctxt.checkThrowError(cond, Message.BOOLEAN_TYPE_EXPECTED, e.getChild().getPosition(),
                        CheckType.toString(tNewChild));

                resultType = newBoolType();
                break;
            }

            case NEGATE:
            case PLUS: {
                boolean cond = isNumericType(tNewChild);
                ctxt.checkThrowError(cond, Message.NUMERIC_TYPE_EXPECTED, e.getChild().getPosition(),
                        CheckType.toString(tNewChild));

                resultType = copyType(tNewChild);
                break;
            }

            case SAMPLE: {
                if (ctxt.contains(CheckContext.ContextItem.NO_SAMPLE)) {
                    ctxt.throwError(Message.SAMPLING_NOT_ALLOWED_HERE, e.getPosition());
                }

                boolean cond = tNewChild instanceof DistributionType;
                ctxt.checkThrowError(cond, Message.DISTRIBUTION_TYPE_EXPECTED, e.getChild().getPosition(),
                        CheckType.toString(tNewChild));

                DistributionType tp = (DistributionType)tNewChild;
                Type elmType = tp.getResultType();

                cond = elmType instanceof BoolType || isNumericType(elmType);
                ctxt.checkThrowError(cond, Message.DISTRIBUTION_ELEMENT_TYPE_WRONG, e.getChild().getPosition(),
                        CheckType.toString(elmType));

                resultType = copyType(elmType);
                break;
            }

            default:
                Assert.fail("Unknown unary operator encountered.");
        }
        return newUnaryExpression(newChild, e.getOp(), copyPosition(e), resultType);
    }

    /**
     * Type-check a slice expression, and decorate it with type information.
     *
     * @param e Expression to check and decorate.
     * @param ctxt Context of the expression.
     * @return Decorated version of the expression.
     */
    private static Expression transSliceExpression(SliceExpression e, CheckContext ctxt) {
        Expression source = transExpression(e.getSource(), ctxt);
        Type tSource = dropReferences(source.getType());
        Type resultType = null;
        if (tSource instanceof ListType) {
            ListType tp = (ListType)tSource;
            resultType = copyType(tp);
        } else if (tSource instanceof StringType) {
            resultType = newStringType();
        }
        boolean cond = resultType != null;
        ctxt.checkThrowError(cond, Message.SLICE_EXPR_MUST_BE_STRING_OR_LIST, e.getSource().getPosition(),
                CheckType.toString(tSource));

        Expression start = null;
        if (e.getStart() != null) {
            start = transExpression(e.getStart(), ctxt);
            Type tStart = dropReferences(start.getType());

            cond = tStart instanceof IntType;
            ctxt.checkThrowError(cond, Message.START_EXPR_MUST_BE_INT, e.getStart().getPosition(),
                    CheckType.toString(tStart));
        }

        Expression step = null;
        if (e.getStep() != null) {
            step = transExpression(e.getStep(), ctxt);
            Type tStep = dropReferences(step.getType());

            cond = tStep instanceof IntType;
            ctxt.checkThrowError(cond, Message.STEP_EXPR_MUST_BE_INT, e.getStep().getPosition(),
                    CheckType.toString(tStep));
        }

        Expression end = null;
        if (e.getEnd() != null) {
            end = transExpression(e.getEnd(), ctxt);
            Type tEnd = dropReferences(end.getType());

            cond = tEnd instanceof IntType;
            ctxt.checkThrowError(cond, Message.END_EXPR_MUST_BE_INT, e.getEnd().getPosition(),
                    CheckType.toString(tEnd));
        }
        return newSliceExpression(end, copyPosition(e), source, start, step, resultType);
    }

    /**
     * Type-check a reference to a stdlib function (outside a call context), and decorate it with type information.
     *
     * @param e Expression to check and decorate.
     * @param args Arguments of the function (for deriving a function type). Use {@code null} if unknown.
     * @param ctxt Context of the expression.
     * @return Decorated version of the expression.
     */
    private static Expression transStdLibFunctionReference(StdLibFunctionReference e, List<Expression> args,
            CheckContext ctxt)
    {
        StdLibFunctions sf = e.getFunction();
        if (ctxt.contains(ContextItem.NO_INPUT)) {
            String s = null;
            switch (sf) {
                case EOF:
                    s = "eof";
                    break;
                case EOL:
                    s = "eol";
                    break;
                case NEWLINES:
                    s = "newlines";
                    break;
                default:
                    break;
            }
            if (s != null) {
                ctxt.throwError(Message.INPUT_FUNC_NOT_ALLOWED, e.getPosition(), s);
                // Not reached.
            }
        }
        Type newFnType = getStdLibFunctionType(e, args, ctxt);
        return newStdLibFunctionReference(sf, copyPosition(e), newFnType);
    }

    /**
     * Type-check a reference, and decorate it with type information.
     *
     * @param e Expression to check and decorate.
     * @param ctxt Context of the expression.
     * @return Decorated version of the expression.
     */
    private static Expression transUnresolvedReference(UnresolvedReference e, CheckContext ctxt) {
        SymbolEntry se = ctxt.getSymbol(e.getName());
        if (se != null) {
            se.typeCheckForUse();
            se.setUsed();
        }

        // Try to interpret the symbol as a variable.
        if (se instanceof VariableSymbolEntry) {
            if (ctxt.contains(ContextItem.NO_VARIABLES)) {
                ctxt.throwError(Message.VARIABLE_NOT_ALLOWED, e.getPosition(), e.getName());
            }

            VariableSymbolEntry vse = (VariableSymbolEntry)se;
            VariableDeclaration vd = vse.getVariable();
            return newVariableReference(copyPosition(e), copyType(vd.getType()), vd);
        }
        // Try to interpret the symbol as a constant.
        if (se instanceof ConstantSymbolEntry) {
            ConstantSymbolEntry cse = (ConstantSymbolEntry)se;
            ConstantDeclaration cd = cse.getConstant();
            return newConstantReference(cd, copyPosition(e), copyType(cd.getType()));
        }
        // Try to interpret the symbol as an enum value.
        if (se instanceof EnumValueSymbolEntry) {
            EnumValueSymbolEntry ese = (EnumValueSymbolEntry)se;
            EnumValue ev = ese.getEnumValue();
            EnumDeclaration ed = ese.enumDeclSymbol.getEnumTypeDeclaration();
            Assert.notNull(ed);
            return newEnumValueReference(copyPosition(e), newEnumTypeReference(null, ed), ev);
        }
        // Try to interpret the symbol as a process definition.
        if (se instanceof ProcessDefSymbolEntry) {
            if (ctxt.contains(ContextItem.NO_PROCESSES)) {
                ctxt.throwError(Message.PROCESS_REF_NOT_ALLOWED, e.getPosition(), e.getName());
            }
            ProcessDefSymbolEntry pse = (ProcessDefSymbolEntry)se;
            ProcessDeclaration pd = pse.getNewDecl();
            ProcessType pt = newProcessType(copyType(pd.getReturnType()), copyTypes(pse.getParameterTypes()), null);
            return newProcessReference(copyPosition(e), pd, pt);
        }
        // Try to interpret the symbol as a function definition.
        if (se instanceof FunctionDefSymbolEntry) {
            FunctionDefSymbolEntry fse = (FunctionDefSymbolEntry)se;
            FunctionDeclaration fdef = fse.getNewDecl();
            Type ftype = newFunctionType(fse.getParameterTypes(), null, copyType(fdef.getReturnType()));
            return newFunctionReference(fdef, copyPosition(e), ftype);
        }
        // Try to interpret it as a model definition.
        if (se instanceof ModelDefSymbolEntry) {
            if (ctxt.contains(ContextItem.NO_MODELS)) {
                ctxt.throwError(Message.MODEL_REF_NOT_ALLOWED, e.getPosition(), e.getName());
            }
            ModelDefSymbolEntry mde = (ModelDefSymbolEntry)se;
            ModelDeclaration mdef = mde.getNewDecl();
            ModelType mt = newModelType(copyType(mdef.getReturnType()), copyTypes(mde.getParameterTypes()), null);
            return newModelReference(mdef, copyPosition(e), mt);
        }

        ctxt.throwError(Message.NO_VALUE_NAME, e.getPosition(), e.getName());
        return null; // Never reached.
    }

    /**
     * Compute the integer value represented by the given expression.
     *
     * @param e Expression to evaluate.
     * @param ctxt Type checker context (may be {@code null}, but breaks badly if an error is raised in such a case).
     * @return Value of the expression.
     */
    public static int evalExpression(Expression e, CheckContext ctxt) {
        // Check result type.
        if (!(dropReferences(e.getType()) instanceof IntType)) {
            ctxt.throwError(Message.EVALUATE_ERROR, e.getPosition());
        }

        if (e instanceof IntNumber) {
            IntNumber n = (IntNumber)e;
            Assert.check(dropReferences(n.getType()) instanceof IntType);
            return Integer.parseInt(n.getValue());
        } else if (e instanceof ConstantReference) {
            ConstantReference cr = (ConstantReference)e;
            return evalExpression(cr.getConstant().getValue(), ctxt);
        } else if (e instanceof UnaryExpression) {
            UnaryExpression ue = (UnaryExpression)e;
            switch (ue.getOp()) {
                case NEGATE:
                    return -evalExpression(ue.getChild(), ctxt);

                case PLUS:
                    return evalExpression(ue.getChild(), ctxt);

                default:
                    // Will eventually throw the type error exception at the
                    // end of the function.
                    break;
            }
        } else if (e instanceof BinaryExpression) {
            BinaryExpression be = (BinaryExpression)e;
            int left, right;
            switch (be.getOp()) {
                case ADDITION:
                    left = evalExpression(be.getLeft(), ctxt);
                    right = evalExpression(be.getRight(), ctxt);
                    return left + right;

                case FLOOR_DIVISION:
                    left = evalExpression(be.getLeft(), ctxt);
                    right = evalExpression(be.getRight(), ctxt);
                    if (right != 0) {
                        return left / right;
                    }
                    break;

                case MODULUS:
                    left = evalExpression(be.getLeft(), ctxt);
                    right = evalExpression(be.getRight(), ctxt);
                    if (right != 0) {
                        return left % right;
                    }
                    break;

                case MULTIPLICATION:
                    left = evalExpression(be.getLeft(), ctxt);
                    right = evalExpression(be.getRight(), ctxt);
                    return left * right;

                case POWER: {
                    left = evalExpression(be.getLeft(), ctxt);
                    right = evalExpression(be.getRight(), ctxt);
                    // Gives a real answer -> make it throw an exception.
                    if (right < 0) {
                        break;
                    }
                    int result = 1;
                    while (right > 0) {
                        result = result * left;
                        right--;
                    }
                    return result;
                }

                case SUBTRACTION:
                    left = evalExpression(be.getLeft(), ctxt);
                    right = evalExpression(be.getRight(), ctxt);
                    return left - right;

                default:
                    // Will eventually throw the type error exception at the
                    // end of the function.
                    break;
            }
        } else if (e instanceof CallExpression) {
            CallExpression ce = (CallExpression)e;
            if (ce.getFunction() instanceof StdLibFunctionReference) {
                StdLibFunctionReference sr = (StdLibFunctionReference)ce.getFunction();
                if (sr.getFunction().equals(StdLibFunctions.ABS)) {
                    return evalExpression(ce.getArguments().get(0), ctxt);
                }
                if (sr.getFunction().equals(StdLibFunctions.SIGN)) {
                    int i = evalExpression(ce.getArguments().get(0), ctxt);
                    if (i > 0) {
                        return 1;
                    }
                    if (i < 0) {
                        return -1;
                    }
                    return 0;
                }
            }
        }
        ctxt.throwError(Message.EVALUATE_ERROR, e.getPosition());
        return 0; // Never reached.
    }
}
