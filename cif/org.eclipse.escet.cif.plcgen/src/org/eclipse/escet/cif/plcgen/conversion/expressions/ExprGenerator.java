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

package org.eclipse.escet.cif.plcgen.conversion.expressions;

import java.util.Set;

import org.eclipse.escet.cif.cif2plc.plcdata.PlcVariable;
import org.eclipse.escet.cif.metamodel.cif.expressions.AlgVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.BoolExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CastExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ConstantExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ContVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DictExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ElifExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EnumLiteralExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.FunctionCallExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.FunctionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IfExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.InputVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IntExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ListExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.LocationExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ProjectionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.RealExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SetExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SliceExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunction;
import org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunctionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.StringExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TauExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TimeExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TupleExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryOperator;

/** Converter of CIF expressions to PLC expressions and statements. */
public class ExprGenerator {

    /**
     * Give temporary variables back to the generator for future re-use.
     *
     * <p>
     * Intended to be used by {@link ExprGenResult} instances.
     * </p>
     *
     * @param variables Variables being returned.
     */
    public void giveTempVariables(Set<PlcVariable> variables) {
        // TODO Auto-generated method stub
    }

    /**
     * Convert a CIF expression to a combination of PLC expressions and statements.
     *
     * @param expr CIF expression to convert.
     * @return The converted expression.
     */
    public ExprGenResult convertExpr(Expression expr) {
        if (expr instanceof BoolExpression) {
//            return ((BoolExpression)expr).isValue() ? "TRUE" : "FALSE";
        } else if (expr instanceof IntExpression) {
//            return str(((IntExpression)expr).getValue());
        } else if (expr instanceof RealExpression) {
//            String rslt = ((RealExpression)expr).getValue();
//            int idx = rslt.indexOf('.');
//            if (idx == -1) {
//                idx = rslt.indexOf('e');
//                if (idx == -1) {
//                    idx = rslt.indexOf('E');
//                }
//                rslt = rslt.substring(0, idx) + ".0" + rslt.substring(idx);
//            }
//            return rslt;
        } else if (expr instanceof StringExpression) {
            throw new RuntimeException("precond violation");
        } else if (expr instanceof TimeExpression) {
//            Assert.notNull(state);
//            return state + ".curTime";
        } else if (expr instanceof CastExpression ce) {
            return convertCastExpr(ce);
        } else if (expr instanceof UnaryExpression ue) {
            return convertUnaryExpr(ue);
        } else if (expr instanceof BinaryExpression be) {
            return convertBinaryExpr(be);
        } else if (expr instanceof IfExpression ife) {
            return convertIfExpr(ife);
        } else if (expr instanceof ProjectionExpression pe) {
            return convertProjectionExpr(pe);
        } else if (expr instanceof SliceExpression) {
            throw new RuntimeException("precond violation");
        } else if (expr instanceof FunctionCallExpression fce) {
            return convertFuncCallExpr(fce);
        } else if (expr instanceof ListExpression le) {
            return convertArrayExpr(le);
        } else if (expr instanceof SetExpression) {
            throw new RuntimeException("precond violation");
        } else if (expr instanceof TupleExpression te) {
            return convertTupleExpr(te);
        } else if (expr instanceof DictExpression) {
            throw new RuntimeException("precond violation");
        } else if (expr instanceof ConstantExpression) {
//            Assert.check(constantsAllowed);
//            Constant constant = ((ConstantExpression)expr).getConstant();
//            return getPlcName(constant);
        } else if (expr instanceof DiscVariableExpression) {
//            DiscVariable var = ((DiscVariableExpression)expr).getVariable();
//            EObject parent = var.eContainer();
//            if (parent instanceof ComplexComponent) {
//                // Discrete variable.
//                Assert.notNull(state);
//                return state + "." + getPlcName(var);
//            } else {
//                // Local variable or parameter of a function.
//                return getPlcName(var);
//            }
        } else if (expr instanceof AlgVariableExpression) {
//            Assert.notNull(state);
//            AlgVariable var = ((AlgVariableExpression)expr).getVariable();
//            return genFuncCall(getPlcName(var), false, "state", state);
        } else if (expr instanceof ContVariableExpression) {
//            Assert.notNull(state);
//            ContVariableExpression cvexpr = (ContVariableExpression)expr;
//            ContVariable var = cvexpr.getVariable();
//            if (cvexpr.isDerivative()) {
//                return genFuncCall("deriv" + getPlcName(var), false, "state", state);
//            } else {
//                return state + "." + getPlcName(var);
//            }
        } else if (expr instanceof LocationExpression) {
//            throw new RuntimeException("loc expr unexpected in lin spec");
        } else if (expr instanceof EnumLiteralExpression) {
//            // We have at most a single enumeration after linearization. There is
//            // no need to prefix literals with the enumeration, as the literal
//            // names are globally unique as well.
//            EnumLiteral lit = ((EnumLiteralExpression)expr).getLiteral();
//            return getPlcName(lit);
        } else if (expr instanceof FunctionExpression) {
            throw new RuntimeException("precond violation");
        } else if (expr instanceof InputVariableExpression) {
//            InputVariable var = ((InputVariableExpression)expr).getVariable();
//            return getPlcName(var);
        }
        throw new RuntimeException("Precondition violation.");
    }

    /**
     * Convert a cast expression.
     *
     * @param castExpr Expression to convert.
     * @return The generated result.
     */
    private ExprGenResult convertCastExpr(CastExpression castExpr) {
//            CastExpression cexpr = (CastExpression)expr;
//            CifType ctype = normalizeType(cexpr.getChild().getType());
//            CifType rtype = normalizeType(cexpr.getType());
//            if (ctype instanceof IntType && rtype instanceof RealType) {
//                String childTxt = transExpr(cexpr.getChild(), state, init);
//                return genFuncCall(fmt("DINT_TO_%s", largeRealType.name), true, "IN", childTxt);
//            }
//            if (CifTypeUtils.checkTypeCompat(ctype, rtype, RangeCompat.EQUAL)) {
//                // Ignore cast expression.
//                return transExpr(cexpr.getChild(), state, init);
//            }
//
//            throw new RuntimeException("precond violation");
    }

    /**
     * Convert a unary operator expression.
     *
     * @param unaryExpr Expression to convert.
     * @return The generated result.
     */
    private ExprGenResult convertUnaryExpr(UnaryExpression unaryExpr) {
//            UnaryExpression uexpr = (UnaryExpression)expr;
//            Expression child = uexpr.getChild();
//            String childTxt = transExpr(child, state, init);
//            UnaryOperator op = uexpr.getOperator();
//            switch (op) {
//                case INVERSE:
//                    return genFuncCall("NOT", true, null, childTxt);
//
//                case NEGATE:
//                    if (child instanceof IntExpression || child instanceof RealExpression) {
//                        return fmt("-%s", childTxt);
//                    }
//                    return fmt("-(%s)", childTxt);
//
//                case PLUS:
//                    return childTxt;
//
//                case SAMPLE:
//                    throw new RuntimeException("precond violation");
//
//                default:
//                    throw new RuntimeException("Unknown unop: " + op);
//            }
    }

    /**
     * Convert a binary operator expression.
     *
     * @param binExpr Binary expression to convert.
     * @return The generated result.
     */
    private ExprGenResult convertBinaryExpr(BinaryExpression binExpr) {
//            BinaryExpression bexpr = (BinaryExpression)expr;
//            String left = transExpr(bexpr.getLeft(), state, init);
//            String right = transExpr(bexpr.getRight(), state, init);
//            CifType ltype = normalizeType(bexpr.getLeft().getType());
//            CifType rtype = normalizeType(bexpr.getRight().getType());
//            BinaryOperator op = bexpr.getOperator();
//            Pair<String, String> leftRight;
//            switch (op) {
//                case IMPLICATION:
//                    return fmt("%s OR (%s)", genFuncCall("NOT", true, null, left), right);
//
//                case BI_CONDITIONAL:
//                    return fmt("(%s) = (%s)", left, right);
//
//                case DISJUNCTION:
//                    if (ltype instanceof BoolType) {
//                        return fmt("(%s) OR (%s)", left, right);
//                    }
//
//                    throw new RuntimeException("precond violation");
//
//                case CONJUNCTION:
//                    if (ltype instanceof BoolType) {
//                        return fmt("(%s) AND (%s)", left, right);
//                    }
//
//                    throw new RuntimeException("precond violation");
//
//                case LESS_THAN:
//                    // S7-400 and S7-300 only support less than on the same types.
//                    leftRight = convertBinaryLeftRight(left, right, ltype, rtype);
//                    return fmt("(%s) < (%s)", leftRight.left, leftRight.right);
//
//                case LESS_EQUAL:
//                    // S7-400 and S7-300 only support less equal on the same types.
//                    leftRight = convertBinaryLeftRight(left, right, ltype, rtype);
//                    return fmt("(%s) <= (%s)", leftRight.left, leftRight.right);
//
//                case GREATER_THAN:
//                    // S7-400 and S7-300 only support greater than on the same types.
//                    leftRight = convertBinaryLeftRight(left, right, ltype, rtype);
//                    return fmt("(%s) > (%s)", leftRight.left, leftRight.right);
//
//                case GREATER_EQUAL:
//                    // S7-400 and S7-300 only support greater equal on the same types.
//                    leftRight = convertBinaryLeftRight(left, right, ltype, rtype);
//                    return fmt("(%s) >= (%s)", leftRight.left, leftRight.right);
//
//                case EQUAL:
//                    // Comparing structure types is not allowed in IEC 61131-3,
//                    // and thus equality on tuples can't be supported directly.
//                    // We could always create a function for it though.
//                    if (ltype instanceof BoolType || ltype instanceof IntType || ltype instanceof RealType
//                            || ltype instanceof EnumType)
//                    {
//                        return fmt("(%s) = (%s)", left, right);
//                    }
//
//                    throw new RuntimeException("precond violation");
//
//                case UNEQUAL:
//                    // Comparing structure types is not allowed in IEC 61131-3,
//                    // and thus equality on tuples can't be supported directly.
//                    // We could always create a function for it though.
//                    if (ltype instanceof BoolType || ltype instanceof IntType || ltype instanceof RealType
//                            || ltype instanceof EnumType)
//                    {
//                        return fmt("(%s) <> (%s)", left, right);
//                    }
//
//                    throw new RuntimeException("precond violation");
//
//                case ADDITION:
//                    // S7-400 and S7-300 only support addition on the same types.
//                    leftRight = convertBinaryLeftRight(left, right, ltype, rtype);
//                    if (ltype instanceof IntType || ltype instanceof RealType) {
//                        return fmt("(%s) + (%s)", leftRight.left, leftRight.right);
//                    }
//
//                    throw new RuntimeException("precond violation");
//
//                case SUBTRACTION:
//                    // S7-400 and S7-300 only support subtraction on the same types.
//                    leftRight = convertBinaryLeftRight(left, right, ltype, rtype);
//                    if (ltype instanceof IntType || ltype instanceof RealType) {
//                        return fmt("(%s) - (%s)", leftRight.left, leftRight.right);
//                    }
//
//                    throw new RuntimeException("precond violation");
//
//                case MULTIPLICATION:
//                    // S7-400 and S7-300 only support multiplication on the same types.
//                    leftRight = convertBinaryLeftRight(left, right, ltype, rtype);
//                    return fmt("(%s) * (%s)", leftRight.left, leftRight.right);
//
//                case DIVISION:
//                    // S7-400 and S7-300 only support division on the same types.
//                    if (ltype instanceof IntType && rtype instanceof IntType) {
//                        // Left value will become real type.
//                        leftRight = convertBinaryLeftRight(left, right, newRealType(), rtype);
//
//                        // Ensure real valued result.
//                        String toName = fmt("DINT_TO_%s", largeRealType.name);
//                        return fmt("%s / (%s)", genFuncCall(toName, true, "IN", leftRight.left), leftRight.right);
//                    }
//
//                    leftRight = convertBinaryLeftRight(left, right, ltype, rtype);
//                    return fmt("(%s) / (%s)", leftRight.left, leftRight.right);
//
//                case INTEGER_DIVISION:
//                    // Truncated towards zero in both CIF and IEC 61131-3.
//                    return fmt("(%s) / (%s)", left, right);
//
//                case MODULUS:
//                    // Note that in CIF division by zero is an error, while
//                    // in IEC 61131-3 it results in zero.
//                    return fmt("(%s) MOD (%s)", left, right);
//
//                case ELEMENT_OF:
//                    throw new RuntimeException("precond violation");
//
//                case SUBSET:
//                    throw new RuntimeException("precond violation");
//
//                default:
//                    throw new RuntimeException("Unknown binop: " + op);
//            }
    }

    /**
     * Convert an 'if' expression to PLC code.
     *
     * @param ifExpr Expression to convert.
     * @return The converted expression.
     */
    private ExprGenResult convertIfExpr(IfExpression ifExpr) {
//            // Create function for the 'if' expression.
//            int nr = nextIfFuncNr;
//            String name = "ifExprFunc" + nr;
//            nextIfFuncNr++;
//
//            PlcType rtype = transType(expr.getType());
//            PlcPou func = new PlcPou(name, PlcPouType.FUNCTION, rtype);
//            project.pous.add(func);
//
//            // Add parameters for state, as well as function parameters and
//            // local variables of functions (if they occur in the 'if'
//            // expression). If no parameters needed, add dummy one, as
//            // functions without parameters are not allowed.
//            String fstate;
//            boolean funcDummyParam = false;
//            if (state == null) {
//                fstate = null;
//
//                // Get function parameters and local variables of the function,
//                // referred to in the 'if' expression. Each unique variable is
//                // is only added once.
//                List<Expression> refs = list();
//                Set<DiscVariable> inputs = set();
//                CifScopeUtils.collectRefExprs(expr, refs);
//                for (Expression ref: refs) {
//                    if (ref instanceof DiscVariableExpression) {
//                        DiscVariable var = ((DiscVariableExpression)ref).getVariable();
//                        EObject parent = var.eContainer();
//                        if (parent instanceof ComplexComponent) {
//                            continue;
//                        }
//                        inputs.add(var);
//                    }
//                }
//
//                // Add parameters, to pass along those 'variables'.
//                for (DiscVariable input: inputs) {
//                    PlcType type = transType(input.getType());
//                    func.inputVars.add(new PlcVariable(getPlcName(input), type));
//                }
//
//                // If no parameters, add dummy one.
//                if (inputs.isEmpty()) {
//                    funcDummyParam = true;
//                    func.inputVars.add(new PlcVariable("dummy", INT_TYPE));
//                }
//            } else {
//                fstate = "state";
//                func.inputVars.add(new PlcVariable("state", STATE_TYPE));
//            }
//
//            // Add code for 'if' statement, for the 'if' expression.
//            IfExpression ifExpr = (IfExpression)expr;
//            func.body.add("IF %s THEN", transPreds(ifExpr.getGuards(), fstate, init));
//            func.body.indent();
//            func.body.add("%s := %s;", name, transExpr(ifExpr.getThen(), fstate, init));
//            func.body.dedent();
//            for (ElifExpression elifExpr: ifExpr.getElifs()) {
//                func.body.add("ELSIF %s THEN", transPreds(elifExpr.getGuards(), fstate, init));
//                func.body.indent();
//                func.body.add("%s := %s;", name, transExpr(elifExpr.getThen(), fstate, init));
//                func.body.dedent();
//            }
//            func.body.add("ELSE");
//            func.body.indent();
//            func.body.add("%s := %s;", name, transExpr(ifExpr.getElse(), fstate, init));
//            func.body.dedent();
//            func.body.add("END_IF;");
//
//            // Return function call.
//            if (state == null && funcDummyParam) {
//                return genFuncCall(name, false, "dummy", "0");
//            } else if (state == null) {
//                List<String> paramNames = listc(func.inputVars.size());
//                List<String> paramValues = listc(func.inputVars.size());
//                for (PlcVariable var: func.inputVars) {
//                    paramNames.add(var.name);
//                    paramValues.add(var.name);
//                }
//                return genFuncCall(name, false, paramNames, paramValues);
//            } else {
//                return genFuncCall(name, false, "state", state);
//            }
    }

    /**
     * Convert projection expressions to a PLC expression.
     *
     * @param expr Projection expression to convert.
     * @return The converted expression.
     */
    private ExprGenResult convertProjectionExpr(Expression expr) {
//            // Since projection on function call results etc are not allowed,
//            // we generate functions for the projections, and use them instead.
//            ProjectionExpression pexpr = (ProjectionExpression)expr;
//            CifType ctype = normalizeType(pexpr.getChild().getType());
//            Expression child = pexpr.getChild();
//            if (ctype instanceof TupleType) {
//                Field field = CifValueUtils.getTupleProjField(pexpr);
//                TupleType ttype = (TupleType)field.eContainer();
//                int idx = ttype.getFields().indexOf(field);
//                String fname = fmt("proj%d_%s", idx, transTupleType(ttype));
//                String childTxt = transExpr(child, state, init);
//                return genFuncCall(fname, false, "tuple", childTxt);
//            } else if (ctype instanceof ListType) {
//                ListType ltype = (ListType)ctype;
//                String childTxt = transExpr(child, state, init);
//                String idxTxt = transExpr(pexpr.getIndex(), state, init);
//
//                if (child instanceof DiscVariableExpression || child instanceof ConstantExpression) {
//                    // Optimize for direct discrete variable or constant
//                    // reference, which we can directly projected.
//                    int size = ltype.getLower();
//                    String normProjIdxName = genNormProjIdxFunc();
//                    String normTxt = genFuncCall(normProjIdxName, false, list("idx", "size"), list(idxTxt, str(size)));
//                    return fmt("%s[%s]", childTxt, normTxt);
//                } else {
//                    // General case. Use function call on projection function.
//                    String fname = genArrayProjFunc(ltype);
//                    List<String> argTxts = list("arr", "idx");
//                    List<String> valueTxts = list(childTxt, idxTxt);
//                    return genFuncCall(fname, false, argTxts, valueTxts);
//                }
//            }
//
//            throw new RuntimeException("precond violation");
    }

    /**
     * Convert a function call.
     *
     * @param funcCallExpr Expression performing the call.
     * @return The converted expression.
     */
    private ExprGenResult convertFuncCallExpr(FunctionCallExpression funcCallExpr) {
//            FunctionCallExpression fcexpr = (FunctionCallExpression)expr;
//
//            List<String> paramTxts = listc(fcexpr.getParams().size());
//            List<Expression> params = fcexpr.getParams();
//            for (Expression param: params) {
//                paramTxts.add(transExpr(param, state, init));
//            }
//            String paramsTxt = String.join(", ", paramTxts);
//
//            Expression fexpr = fcexpr.getFunction();
//            if (fexpr instanceof FunctionExpression) {
//                // Get function, and generate code for it.
//                Function func = ((FunctionExpression)fexpr).getFunction();
//                Assert.check(func instanceof InternalFunction);
//                PlcPou funcPou = transFunction((InternalFunction)func);
//
//                // Get function and parameter names from the POU, as they may
//                // have been renamed.
//                List<String> paramNames = listc(funcPou.inputVars.size());
//                for (PlcVariable param: funcPou.inputVars) {
//                    paramNames.add(param.name);
//                }
//
//                // Generate function call.
//                return genFuncCall(funcPou.name, false, paramNames, paramTxts);
//            } else if (fexpr instanceof StdLibFunctionExpression) {
//                StdLibFunction stdlib = ((StdLibFunctionExpression)fexpr).getFunction();
//                switch (stdlib) {
//                    case ABS:
//                        return genFuncCall("ABS", true, null, paramsTxt);
//
//                    case CBRT:
//                        if (PlcOutputTypeOption.isS7Output()) {
//                            // Use reals to get real result. Use two real-typed values to support S7-400 and S7-300.
//                            return fmt("(%s) ** (1.0/3.0)", paramsTxt);
//                        }
//
//                        // The 'a ** b' syntax seemed not to work in TwinCAT
//                        // 3.1. Using the named function instead.
//                        return genFuncCall("EXPT", true, list("IN1", "IN2"), list(paramsTxt, "1.0/3"));
//
//                    case CEIL:
//                        // Unsupported. IEC 61131-3 has only TRUNC (round
//                        // towards zero) and REAL_TO_INT (rounds to the nearest
//                        // even integer if equally far from two integers).
//                        throw new RuntimeException("precond violation");
//
//                    case DELETE:
//                        // Unsupported.
//                        throw new RuntimeException("precond violation");
//
//                    case EMPTY:
//                        // Unsupported.
//                        throw new RuntimeException("precond violation");
//
//                    case EXP:
//                        return genFuncCall("EXP", true, null, paramsTxt);
//
//                    case FLOOR:
//                        // Unsupported. IEC 61131-3 has only TRUNC (round
//                        // towards zero) and REAL_TO_INT (rounds to the nearest
//                        // even integer if equally far from two integers).
//                        throw new RuntimeException("precond violation");
//
//                    case FORMAT:
//                        // Unsupported.
//                        throw new RuntimeException("precond violation");
//
//                    case LN:
//                        return genFuncCall("LN", true, null, paramsTxt);
//
//                    case LOG:
//                        if (PlcOutputTypeOption.isS7Output()) {
//                            // S7 doesn't have a function for log10. But log10(x) = ln(x) / ln(10).
//                            return fmt("%s / %s", genFuncCall("LN", true, null, paramsTxt),
//                                    genFuncCall("LN", true, null, "10"));
//                        }
//
//                        return genFuncCall("LOG", true, null, paramsTxt);
//
//                    case MAXIMUM:
//                    case MINIMUM: {
//                        CifType type0 = normalizeType(params.get(0).getType());
//                        CifType type1 = normalizeType(params.get(1).getType());
//                        if ((type0 instanceof IntType && type1 instanceof IntType)
//                                || (type0 instanceof RealType && type1 instanceof RealType))
//                        {
//                            // paramTxts OK.
//                        } else if (type0 instanceof IntType && type1 instanceof RealType) {
//                            String toName = fmt("DINT_TO_%s", largeRealType.name);
//                            paramTxts.set(0, genFuncCall(toName, true, "IN", paramTxts.get(0)));
//                        } else {
//                            Assert.check(type0 instanceof RealType && type1 instanceof IntType);
//                            String toName = fmt("DINT_TO_%s", largeRealType.name);
//                            paramTxts.set(1, genFuncCall(toName, true, "IN", paramTxts.get(1)));
//                        }
//                        return genFuncCall((stdlib == StdLibFunction.MAXIMUM) ? "MAX" : "MIN", true, list("IN1", "IN2"),
//                                paramTxts);
//                    }
//
//                    case POP:
//                        // Unsupported.
//                        throw new RuntimeException("precond violation");
//
//                    case POWER: {
//                        CifType type0 = normalizeType(params.get(0).getType());
//                        CifType type1 = normalizeType(params.get(1).getType());
//
//                        // S7-400 and S7-300 only support power on real types.
//                        if (getPlcOutputType() == S7_400 || getPlcOutputType() == S7_300) {
//                            String paramTxt0 = paramTxts.get(0);
//                            String paramTxt1 = paramTxts.get(1);
//                            if (type0 instanceof IntType) {
//                                String toName = fmt("%s_TO_%s", largeIntType.name, largeRealType.name);
//                                paramTxt0 = genFuncCall(toName, true, "IN", paramTxt0);
//                            }
//
//                            if (type1 instanceof IntType) {
//                                String toName = fmt("%s_TO_%s", largeIntType.name, largeRealType.name);
//                                paramTxt1 = genFuncCall(toName, true, "IN", paramTxt1);
//                            }
//
//                            String resultTxt = fmt("(%s) ** (%s)", paramTxt0, paramTxt1);
//
//                            // If the resulting type is integer, we need to convert that explicitly.
//                            FuncType functionType = (FuncType)normalizeType(fexpr.getType());
//                            CifType resultType = normalizeType(functionType.getReturnType());
//                            if (resultType instanceof IntType) {
//                                String toName = fmt("%s_TO_%s", largeRealType.name, largeIntType.name);
//                                resultTxt = genFuncCall(toName, true, "IN", resultTxt);
//                            }
//
//                            return resultTxt;
//                        }
//
//                        // S7-1500 and S7-1200 use the 'a ** b' syntax for power.
//                        if (getPlcOutputType() == S7_1500 || getPlcOutputType() == S7_1200) {
//                            return fmt("(%s) ** (%s)", paramTxts.get(0), paramTxts.get(1));
//                        }
//
//                        // The 'a ** b' syntax seemed not to work in TwinCAT
//                        // 3.1. Using the named function instead.
//                        if (type0 instanceof IntType && type1 instanceof IntType && !isRangeless((IntType)type0)
//                                && !isRangeless((IntType)type1))
//                        {
//                            // First parameter must be of real type.
//                            String f0 = fmt("DINT_TO_%s", largeRealType.name);
//                            String c1 = genFuncCall(f0, true, "IN", paramTxts.get(0));
//                            String c2 = genFuncCall("EXPT", true, list("IN1", "IN2"), list(c1, paramTxts.get(1)));
//                            String f1 = fmt("%s_TO_DINT", largeRealType.name);
//                            return genFuncCall(f1, true, "IN", c2);
//                        } else if (type0 instanceof IntType && type1 instanceof RealType) {
//                            // First parameter must be of real type.
//                            String cf = fmt("DINT_TO_%s", largeRealType.name);
//                            String f0 = genFuncCall(cf, true, "IN", paramTxts.get(0));
//                            return genFuncCall("EXPT", true, list("IN1", "IN2"), list(f0, paramTxts.get(1)));
//                        } else {
//                            return genFuncCall("EXPT", true, list("IN1", "IN2"), paramTxts);
//                        }
//                    }
//
//                    case ROUND:
//                        // Unsupported. IEC 61131-3 has only TRUNC (round
//                        // towards zero) and REAL_TO_INT (rounds to the nearest
//                        // even integer if equally far from two integers).
//                        throw new RuntimeException("precond violation");
//
//                    case SCALE:
//                        // Unsupported. We could support this by expanding
//                        // it to the definition of 'scale', using addition,
//                        // subtraction, etc.
//                        throw new RuntimeException("precond violation");
//
//                    case SIGN:
//                        // Unsupported. We could support this by adding our
//                        // own sign function.
//                        throw new RuntimeException("precond violation");
//
//                    case SIZE:
//                        // Unsupported.
//                        throw new RuntimeException("precond violation");
//
//                    case SQRT:
//                        return genFuncCall("SQRT", true, null, paramsTxt);
//
//                    case ACOS:
//                        return genFuncCall("ACOS", true, null, paramsTxt);
//
//                    case ASIN:
//                        return genFuncCall("ASIN", true, null, paramsTxt);
//
//                    case ATAN:
//                        return genFuncCall("ATAN", true, null, paramsTxt);
//
//                    case COS:
//                        return genFuncCall("COS", true, null, paramsTxt);
//
//                    case SIN:
//                        return genFuncCall("SIN", true, null, paramsTxt);
//
//                    case TAN:
//                        return genFuncCall("TAN", true, null, paramsTxt);
//
//                    case ACOSH:
//                    case ASINH:
//                    case ATANH:
//                    case COSH:
//                    case SINH:
//                    case TANH:
//                        // Unsupported.
//                        throw new RuntimeException("precond violation");
//
//                    case BERNOULLI:
//                    case BETA:
//                    case BINOMIAL:
//                    case CONSTANT:
//                    case ERLANG:
//                    case EXPONENTIAL:
//                    case GAMMA:
//                    case GEOMETRIC:
//                    case LOG_NORMAL:
//                    case NORMAL:
//                    case POISSON:
//                    case RANDOM:
//                    case TRIANGLE:
//                    case UNIFORM:
//                    case WEIBULL:
//                        // Unsupported.
//                        throw new RuntimeException("precond violation");
//                }
//            }
//
//            throw new RuntimeException("precond violation");
    }

    /**
     * Convert an array literal expression to PLC code.
     *
     * @param listExpr Expression to convert.
     * @return The converted expression.
     */
    private ExprGenResult convertArrayExpr(ListExpression listExpr) {
//            // Transform the elements.
//            ListExpression lexpr = (ListExpression)expr;
//            List<String> elemTxts = listc(lexpr.getElements().size());
//            for (int i = 0; i < lexpr.getElements().size(); i++) {
//                Expression elem = lexpr.getElements().get(i);
//                String valueTxt = transExpr(elem, state, init);
//                elemTxts.add(valueTxt);
//            }
//
//            // Optimize for initialization value, as then we can use literals,
//            // and literals have the best performance. However, for the general
//            // case we can't use literals, so we generate a function per array
//            // type.
//            if (init) {
//                return fmt("[%s]", String.join(", ", elemTxts));
//            } else {
//                ListType ltype = (ListType)normalizeType(lexpr.getType());
//                List<String> argTxts = listc(lexpr.getElements().size());
//                for (int i = 0; i < lexpr.getElements().size(); i++) {
//                    argTxts.add(fmt("elem%d", i));
//                }
//                String name = genArrayLitCreateFunc(ltype);
//                return genFuncCall(name, false, argTxts, elemTxts);
//            }
    }

    /**
     * Convert a tuple literal expression to PLC code.
     *
     * @param tupleExpr Expression to convert.
     * @return The converted expression.
     */
    private ExprGenResult convertTupleExpr(TupleExpression tupleExpr) {
//            // Transform the elements.
//            TupleExpression texpr = (TupleExpression)expr;
//            List<String> elemTxts = listc(texpr.getFields().size());
//            for (int i = 0; i < texpr.getFields().size(); i++) {
//                Expression value = texpr.getFields().get(i);
//                String valueTxt = transExpr(value, state, init);
//                elemTxts.add(valueTxt);
//            }
//
//            // Optimize for initialization value, as then we can use literals,
//            // and literals have the best performance. However, for the general
//            // case we can't use literals, so we generate a function per array
//            // type.
//            if (init) {
//                TupleType ttype = (TupleType)normalizeType(texpr.getType());
//                List<String> fieldTxts = listc(texpr.getFields().size());
//                for (int i = 0; i < texpr.getFields().size(); i++) {
//                    Field field = ttype.getFields().get(i);
//                    String fieldTxt = getPlcName(field);
//                    fieldTxts.add(fmt("%s:=%s", fieldTxt, elemTxts.get(i)));
//                }
//                return fmt("(%s)", String.join(", ", fieldTxts));
//            } else {
//                TupleType ttype = (TupleType)normalizeType(texpr.getType());
//                List<String> argTxts = listc(texpr.getFields().size());
//                for (int i = 0; i < texpr.getFields().size(); i++) {
//                    Field field = ttype.getFields().get(i);
//                    String fieldTxt = getPlcName(field);
//                    argTxts.add(fieldTxt);
//                }
//                String name = transTupleType(ttype);
//                return genFuncCall("make" + name, false, argTxts, elemTxts);
//            }
    }
}
