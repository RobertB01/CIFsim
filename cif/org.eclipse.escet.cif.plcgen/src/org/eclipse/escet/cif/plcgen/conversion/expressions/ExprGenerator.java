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

import static org.eclipse.escet.cif.common.CifTypeUtils.normalizeType;
import static org.eclipse.escet.cif.common.CifValueUtils.flattenBinExpr;
import static org.eclipse.escet.cif.common.CifTypeUtils.isRangeless;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newIntType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newRealType;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.set;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.escet.cif.cif2plc.plcdata.PlcStructType;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcType;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcVariable;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.RangeCompat;
import org.eclipse.escet.cif.metamodel.cif.expressions.AlgVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BoolExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CastExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ConstantExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ContVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DictExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ElifExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EnumLiteralExpression;
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
import org.eclipse.escet.cif.metamodel.cif.expressions.SetExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SliceExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunction;
import org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunctionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.StringExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TimeExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TupleExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.EnumType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.cif.metamodel.cif.types.ListType;
import org.eclipse.escet.cif.metamodel.cif.types.RealType;
import org.eclipse.escet.cif.metamodel.cif.types.TupleType;
import org.eclipse.escet.cif.plcgen.conversion.PlcFunctionAppls;
import org.eclipse.escet.cif.plcgen.generators.NameGeneratorInterface;
import org.eclipse.escet.cif.plcgen.generators.TypeGeneratorInterface;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcBoolLiteral;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcExpression;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcIntLiteral;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcRealLiteral;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcVarExpression;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcVarExpression.PlcArrayProjection;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcVarExpression.PlcProjection;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcVarExpression.PlcStructProjection;
import org.eclipse.escet.cif.plcgen.model.functions.PlcFuncOperation;
import org.eclipse.escet.cif.plcgen.model.statements.PlcAssignmentStatement;
import org.eclipse.escet.cif.plcgen.model.statements.PlcSelectionStatement;
import org.eclipse.escet.cif.plcgen.model.statements.PlcSelectionStatement.PlcSelectChoice;
import org.eclipse.escet.cif.plcgen.model.statements.PlcStatement;
import org.eclipse.escet.cif.plcgen.targets.PlcTargetInterface;
import org.eclipse.escet.common.java.Assert;

/** Converter of CIF expressions to PLC expressions and statements. */
public class ExprGenerator {
    /** An integer CIF type, used for type conversions. */
    private static final CifType INT_TYPE = newIntType();

    /** A real CIF type, used for type conversions. */
    private static final CifType REAL_TYPE = newRealType();

    /** Type converter from CIF to PLC types. */
    private final TypeGeneratorInterface typeGenerator;

    /** Generator for obtaining clash-free names in the generated code. */
    private final NameGeneratorInterface nameGenerator;

    /** Map for the name generator to create local variables. */
    private final Map<String, Integer> localNameGenMap = map();

    /** PLC target to generate code for. */
    private final PlcTargetInterface target;

    /** Access to PLC equivalents of CIF variables, automata and locations. */
    private final CifDataProvider cifData;

    /** PLC function applications of the target. */
    private final PlcFunctionAppls funcAppls;

    /**
     * Constructor of the {@link ExprGenerator} class.
     *
     * @param target PLC target to generate code for.
     * @param cifData Access to PLC equivalents of CIF variables, automata and locations.
     * @param typeGenerator Type converter from CIF to PLC types.
     * @param nameGenerator Generator for obtaining clash-free names in the generated code.
     */
    public ExprGenerator(PlcTargetInterface target, CifDataProvider cifData, TypeGeneratorInterface typeGenerator,
            NameGeneratorInterface nameGenerator)
    {
        this.target = target;
        this.cifData = cifData;
        this.typeGenerator = typeGenerator;
        this.nameGenerator = nameGenerator;
        this.funcAppls = new PlcFunctionAppls(target);
    }

    /**
     * Obtain a local scratch variable. Its name starts with the provided prefix, and it will have a PLC type that
     * matches with the provided CIF type.
     *
     * @param prefix Initial part of the name of the variable.
     * @param cifType CIF type to convert to a PLC type.
     * @return The created variable.
     */
    public PlcVariable getTempVariable(String prefix, CifType cifType) {
        PlcType plcType = typeGenerator.convertType(cifType);
        return getTempVariable(prefix, plcType);
    }

    /**
     * Obtain a local scratch variable. Its name starts with the provided prefix, and it will have the provided type.
     *
     * @param prefix Initial part of the name of the variable.
     * @param plcType Type of the returned variable.
     * @return The created variable.
     */
    public PlcVariable getTempVariable(String prefix, PlcType plcType) {
        String name = nameGenerator.generateLocalName(prefix, localNameGenMap);
        return new PlcVariable(name, plcType);
    }

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
        // TODO: Currently variables are silently discarded.
    }

    /**
     * Convert a CIF expression to a combination of PLC expressions and statements.
     *
     * @param expr CIF expression to convert.
     * @return The converted expression.
     */
    public ExprGenResult convertExpr(Expression expr) {
        if (expr instanceof BoolExpression be) {
            return new ExprGenResult(this).setValue(new PlcBoolLiteral(be.isValue()));
        } else if (expr instanceof IntExpression ie) {
            return new ExprGenResult(this).setValue(new PlcIntLiteral(ie.getValue()));
        } else if (expr instanceof RealExpression re) {
            return new ExprGenResult(this).setValue(new PlcRealLiteral(re.getValue()));
        } else if (expr instanceof StringExpression) {
            throw new RuntimeException("Precondition violation.");
        } else if (expr instanceof TimeExpression) {
            throw new RuntimeException("Precondition violation.");
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
            throw new RuntimeException("Precondition violation.");
        } else if (expr instanceof FunctionCallExpression fce) {
            return convertFuncCallExpr(fce);
        } else if (expr instanceof ListExpression le) {
            return convertArrayExpr(le);
        } else if (expr instanceof SetExpression) {
            throw new RuntimeException("Precondition violation.");
        } else if (expr instanceof TupleExpression te) {
            return convertTupleExpr(te);
        } else if (expr instanceof DictExpression) {
            throw new RuntimeException("Precondition violation.");
        } else if (expr instanceof ConstantExpression ce) {
            return new ExprGenResult(this).setValue(cifData.getExprForConstant(ce.getConstant()));
        } else if (expr instanceof DiscVariableExpression de) {
            return new ExprGenResult(this).setValue(cifData.getExprForDiscVar(de.getVariable()));
        } else if (expr instanceof AlgVariableExpression ae) {
            // TODO: Decide how to deal with algebraic variables.
            return convertExpr(ae.getVariable().getValue()); // Convert its definition.
        } else if (expr instanceof ContVariableExpression ce) {
            return new ExprGenResult(this).setValue(cifData.getExprForContvar(ce.getVariable(), ce.isDerivative()));
        } else if (expr instanceof LocationExpression le) {
            return new ExprGenResult(this).setValue(cifData.getExprForLocation(le.getLocation()));
        } else if (expr instanceof EnumLiteralExpression eLit) {
            // Handled in convertProjection().
            throw new RuntimeException("Precondition violation.");
        } else if (expr instanceof FunctionExpression) {
            throw new RuntimeException("Precondition violation.");
        } else if (expr instanceof InputVariableExpression ie) {
            return new ExprGenResult(this).setValue(cifData.getExprForInputVar(ie.getVariable()));
        }
        throw new RuntimeException("Unexpected expr: " + expr);
    }

    /**
     * Convert a cast expression.
     *
     * @param castExpr Expression to convert.
     * @return The generated result.
     */
    private ExprGenResult convertCastExpr(CastExpression castExpr) {
        ExprGenResult result = convertExpr(castExpr.getChild());
        CifType ctype = normalizeType(castExpr.getChild().getType());
        CifType rtype = normalizeType(castExpr.getType());
        if (ctype instanceof IntType && rtype instanceof RealType) {
            return result
                    .setValue(funcAppls.castFunctionAppl(result.value, target.getIntegerType(), target.getRealType()));
        }
        if (CifTypeUtils.checkTypeCompat(ctype, rtype, RangeCompat.EQUAL)) {
            // Ignore cast expression.
            return result;
        }

        throw new RuntimeException("Precondition violation.");
    }

    /**
     * Convert a unary operator expression.
     *
     * @param unaryExpr Expression to convert.
     * @return The generated result.
     */
    private ExprGenResult convertUnaryExpr(UnaryExpression unaryExpr) {
        ExprGenResult result = convertExpr(unaryExpr.getChild());
        switch (unaryExpr.getOperator()) {
            case INVERSE:
                return result.setValue(funcAppls.complementFuncAppl(result.value));

            case NEGATE:
                return result.setValue(funcAppls.negateFuncAppl(result.value));

            case PLUS:
                return result;

            case SAMPLE:
                throw new RuntimeException("Precondition violation.");

            default:
                throw new RuntimeException("Unknown unop: " + unaryExpr.getOperator());
        }
    }

    /**
     * Convert a binary operator expression.
     *
     * @param binExpr Binary expression to convert.
     * @return The generated result.
     */
    private ExprGenResult convertBinaryExpr(BinaryExpression binExpr) {
        CifType ltype = normalizeType(binExpr.getLeft().getType());
        CifType rtype = normalizeType(binExpr.getRight().getType());

        ExprGenResult leftResult = convertExpr(binExpr.getLeft());
        ExprGenResult rightResult = convertExpr(binExpr.getRight());
        ExprGenResult result = new ExprGenResult(this, leftResult, rightResult);
        switch (binExpr.getOperator()) {
            case IMPLICATION:
                // Right-value or not left-value.
                return result.setValue(
                        funcAppls.orFuncAppl(rightResult.value, funcAppls.complementFuncAppl(leftResult.value)));

            case BI_CONDITIONAL:
                return result.setValue(funcAppls.equalFuncAppl(leftResult.value, rightResult.value));

            case DISJUNCTION:
                if (ltype instanceof BoolType) {
                    return convertFlattenedExpr(binExpr);
                }

                throw new RuntimeException("Precondition violation.");

            case CONJUNCTION:
                if (ltype instanceof BoolType) {
                    return convertFlattenedExpr(binExpr);
                }

                throw new RuntimeException("Precondition violation.");

            case LESS_THAN: {
                // S7-400 and S7-300 only support less than on the same types.
                PlcExpression leftSide = unifyTypeOfExpr(leftResult.value, ltype, rtype);
                PlcExpression rightSide = unifyTypeOfExpr(rightResult.value, rtype, ltype);
                return result.setValue(funcAppls.lessThanFuncAppl(leftSide, rightSide));
            }

            case LESS_EQUAL: {
                // S7-400 and S7-300 only support less equal on the same types.
                PlcExpression leftSide = unifyTypeOfExpr(leftResult.value, ltype, rtype);
                PlcExpression rightSide = unifyTypeOfExpr(rightResult.value, rtype, ltype);
                return result.setValue(funcAppls.lessEqualFuncAppl(leftSide, rightSide));
            }

            case GREATER_THAN: {
                // S7-400 and S7-300 only support greater than on the same types.
                PlcExpression leftSide = unifyTypeOfExpr(leftResult.value, ltype, rtype);
                PlcExpression rightSide = unifyTypeOfExpr(rightResult.value, rtype, ltype);
                return result.setValue(funcAppls.greaterThanFuncAppl(leftSide, rightSide));
            }

            case GREATER_EQUAL: {
                // S7-400 and S7-300 only support greater equal on the same types.
                PlcExpression leftSide = unifyTypeOfExpr(leftResult.value, ltype, rtype);
                PlcExpression rightSide = unifyTypeOfExpr(rightResult.value, rtype, ltype);
                return result.setValue(funcAppls.greaterEqualFuncAppl(leftSide, rightSide));
            }

            case EQUAL:
                // Comparing structure types is not allowed in IEC 61131-3,
                // and thus equality on tuples can't be supported directly.
                // We could always create code for it though.
                if (ltype instanceof BoolType || ltype instanceof IntType || ltype instanceof RealType
                        || ltype instanceof EnumType)
                {
                    return result.setValue(funcAppls.equalFuncAppl(leftResult.value, rightResult.value));
                }

                throw new RuntimeException("Precondition violation.");

            case UNEQUAL:
                // Comparing structure types is not allowed in IEC 61131-3,
                // and thus equality on tuples can't be supported directly.
                // We could always create code for it though.
                if (ltype instanceof BoolType || ltype instanceof IntType || ltype instanceof RealType
                        || ltype instanceof EnumType)
                {
                    return result.setValue(funcAppls.unEqualFuncAppl(leftResult.value, rightResult.value));
                }

                throw new RuntimeException("Precondition violation.");

            case ADDITION:
                // S7-400 and S7-300 only support addition on the same types.
                if (ltype instanceof IntType || ltype instanceof RealType) {
                    return convertFlattenedExpr(binExpr);
                }

                throw new RuntimeException("Precondition violation.");

            case SUBTRACTION:
                // S7-400 and S7-300 only support subtraction on the same types.
                if (ltype instanceof IntType || ltype instanceof RealType) {
                    PlcExpression leftSide = unifyTypeOfExpr(leftResult.value, ltype, rtype);
                    PlcExpression rightSide = unifyTypeOfExpr(rightResult.value, rtype, ltype);
                    return result.setValue(funcAppls.subtractFuncAppl(leftSide, rightSide));
                }

                throw new RuntimeException("Precondition violation.");

            case MULTIPLICATION: {
                return convertFlattenedExpr(binExpr);
            }

            case DIVISION: {
                // S7-400 and S7-300 only support division on the same types.
                PlcExpression leftSide = unifyTypeOfExpr(leftResult.value, ltype, REAL_TYPE);
                PlcExpression rightSide = unifyTypeOfExpr(rightResult.value, rtype, REAL_TYPE);
                return result.setValue(funcAppls.divideFuncAppl(leftSide, rightSide));
            }

            case INTEGER_DIVISION:
                // Truncated towards zero in both CIF and IEC 61131-3.
                return result.setValue(funcAppls.divideFuncAppl(leftResult.value, rightResult.value));

            case MODULUS:
                // Note that in CIF division by zero is an error, while
                // in IEC 61131-3 it results in zero.
                return result.setValue(funcAppls.moduloFuncAppl(leftResult.value, rightResult.value));

            case ELEMENT_OF:
                throw new RuntimeException("Precondition violation.");

            case SUBSET:
                throw new RuntimeException("Precondition violation.");

            default:
                throw new RuntimeException("Unknown binary expression operator: " + binExpr.getOperator());
        }
    }

    /**
     * Flatten the binary expression on its operator, convert the collection children, and combine the children into an
     * n-ary PLC function.
     *
     * @param binExpr Binary expression to flatten and convert. Must be a disjunction, conjunction, addition, or
     *     multiplication expression.
     * @return The converted expression.
     */
    private ExprGenResult convertFlattenedExpr(BinaryExpression binExpr) {
        // Configure some variables to guide the conversion.
        java.util.function.Function<PlcExpression[], PlcExpression> applFunc;
        boolean unifyTypes;
        switch (binExpr.getOperator()) {
            case DISJUNCTION:
                applFunc = values -> funcAppls.orFuncAppl(values);
                unifyTypes = false;
                break;
            case CONJUNCTION:
                applFunc = values -> funcAppls.andFuncAppl(values);
                unifyTypes = false;
                break;
            case ADDITION:
                applFunc = values -> funcAppls.addFuncAppl(values);
                unifyTypes = true; // S7-400 and S7-300 only support addition on the same types.
                break;
            case MULTIPLICATION:
                applFunc = values -> funcAppls.multiplyFuncAppl(values);
                unifyTypes = true; // S7-400 and S7-300 only support multiplication on the same types.
                break;
            default:
                throw new RuntimeException("Unexpected flattened binary expression operator: " + binExpr.getOperator());
        }

        // Collect the child expressions and compute a unified type if needed.
        List<Expression> exprs = flattenBinExpr(List.of(binExpr), binExpr.getOperator());
        CifType unifiedType;
        if (unifyTypes) {
            unifiedType = INT_TYPE;
            for (Expression expr: exprs) {
                if (normalizeType(expr.getType()) instanceof RealType) {
                    unifiedType = REAL_TYPE;
                    break;
                }
            }
        } else {
            unifiedType = null;
        }

        // Convert each child expression and extract the collect the results.
        ExprGenResult[] exprGenResults = new ExprGenResult[exprs.size()];
        PlcExpression[] values = new PlcExpression[exprs.size()];
        int i = 0;
        for (Expression expr: exprs) {
            exprGenResults[i] = convertExpr(expr);
            if (unifyTypes) {
                values[i] = unifyTypeOfExpr(exprGenResults[i].value, normalizeType(expr.getType()), unifiedType);
            } else {
                values[i] = exprGenResults[i].value;
            }
            i++;
        }

        // Create the final result and give it to the caller.
        ExprGenResult exprGenResult = new ExprGenResult(this, exprGenResults);
        return exprGenResult.setValue(applFunc.apply(values));
    }

    /**
     * Convert an 'if' expression to PLC code.
     *
     * @param ifExpr Expression to convert.
     * @return The converted expression.
     */
    private ExprGenResult convertIfExpr(IfExpression ifExpr) {
        ExprGenResult result = new ExprGenResult(this);
        PlcType resultValueType = typeGenerator.convertType(ifExpr.getType());
        PlcVariable resultVar = getTempVariable("ifResult", resultValueType);
        result.valueVariables.add(resultVar);
        result.setValue(new PlcVarExpression(resultVar));

        PlcSelectionStatement selStat = null;
        selStat = addBranch(ifExpr.getGuards(), ifExpr.getThen(), resultVar, selStat, result.code);

        for (ElifExpression elif: ifExpr.getElifs()) {
            selStat = addBranch(elif.getGuards(), elif.getThen(), resultVar, selStat, result.code);
        }
        addBranch(null, ifExpr.getElse(), resultVar, selStat, result.code);
        return result;
    }

    /**
     * Append an {@link IfExpression} branch to the PLC code.
     *
     * @param guards CIF expressions that must hold to select the branch. Is {@code null} for the 'else' branch.
     * @param thenExpr Expression value to return if the guards hold.
     * @param resultVar Variable to assign the 'thenExpr' value to.
     * @param selStat Selection statement used the previous time, or {@code null} if no selection statement has been created yet.
     * @param rootCode Code block for storing the entire 'if' expression.
     * @return The last used selection statement after adding the branch.
     */
    private PlcSelectionStatement addBranch(List<Expression> guards, Expression thenExpr, PlcVariable resultVar,
            PlcSelectionStatement selStat, List<PlcStatement> rootCode)
    {
        // Place to store generated guard condition code. If no guards are present (that is, it's the 'else' of the
        // 'if' expression), the final assignment of the return value is put there.
        List<PlcStatement> codeStorage = (selStat != null) ? selStat.elseStats : rootCode;

        if (guards != null) {
            // Convert the guard conditions. Copy any generated code into storage, collect the used variables and the
            // converted expression for the final N-ary AND.
            PlcExpression[] grdValues = new PlcExpression[guards.size()];
            boolean seenGuardCode = false;
            Set<PlcVariable> grdVariables = set();

            // For all guard expressions, convert them and store their output.
            int grdNum = 0;
            for (Expression guard: guards) {
                ExprGenResult grdResult = convertExpr(guard);
                if (grdResult.hasCode()) {
                    seenGuardCode = true;
                    codeStorage.addAll(grdResult.code);
                    grdVariables.addAll(grdResult.codeVariables);
                }
                grdVariables.addAll(grdResult.valueVariables);
                grdValues[grdNum] = grdResult.value;
                grdNum++;
            }

            // If there is no previous selection statement or we added code to the 'else' branch of it, the previous
            // selection statement cannot be used for this branch. Append a new selection statement to the code block
            // in that case.
            if (selStat == null || seenGuardCode) {
                selStat = new PlcSelectionStatement();
                codeStorage.add(selStat);
            }

            // Add a new branch in the previous selection statement or in the just appended new selection statement.
            PlcSelectChoice choice;
            if (grdNum == 1) {
                choice = new PlcSelectChoice(grdValues[0], list());
            } else {
                choice = new PlcSelectChoice(funcAppls.andFuncAppl(grdValues), list());
            }
            selStat.condChoices.add(choice);
            giveTempVariables(grdVariables);

            // The 'then' statements of that choice are now the spot to write the 'then' code + value.
            codeStorage = choice.thenStats;
        }
        // else there is no guard and 'codeStorage' already points at the right spot for writing the final 'else' code +
        // value.

        // Convert the result value. As we released the guard condition temporaries above, these may be used again here.
        ExprGenResult retValueResult = convertExpr(thenExpr);
        codeStorage.addAll(retValueResult.code);
        codeStorage.add(new PlcAssignmentStatement(new PlcVarExpression(resultVar), retValueResult.value));
        giveTempVariables(retValueResult.codeVariables);
        giveTempVariables(retValueResult.valueVariables);
        return selStat;
    }

    /**
     * Convert projection expressions to a PLC expression.
     *
     * @param expr Projection expression to convert.
     * @return The converted expression.
     */
    private ExprGenResult convertProjectionExpr(Expression expr) {
        // Unwrap and store the nested projections, last projection at index 0.
        List<ProjectionExpression> projections = list();
        while (expr instanceof ProjectionExpression proj) {
            projections.add(proj);
            expr = proj.getChild();
        }
        Assert.check(!projections.isEmpty());

        // Convert the projection root value and make it usable for the PLC.
        ExprGenResult exprResult = convertExpr(expr);

        // Setup the result of this method and prepare it for stacking the above collected projections on top of it.
        if (exprResult.value instanceof PlcVarExpression varExpr) {
            // We received a variable to project at, grab the result to add more projections.

            // Copy the already converted expressions of 'varExpr', append the new CIF projections to it.
            List<PlcProjection> plcProjections = list();
            plcProjections.addAll(varExpr.projections);
            convertProjections(projections, plcProjections, exprResult);

            // Build a new PLC projections expressions with the parent variable and the collected projections.
            PlcVarExpression lhsExpr = new PlcVarExpression(varExpr.variable, plcProjections);
            exprResult.setValue(lhsExpr);
            return exprResult;
        } else {
            // We got something different from a single variable. Assume the worst and use a new variable, copy the
            // parent result into it, and assign its value to the new variable.
            PlcType plcType = typeGenerator.convertType(expr.getType());
            PlcVariable plcProjectRoot = getTempVariable("project", plcType);

            // Build a new result and fill it with the result of the converted root.
            ExprGenResult convertResult = new ExprGenResult(this, exprResult);
            convertResult.mergeCodeVariables(exprResult);
            convertResult.mergeCode(exprResult);

            // Convert the CIF projections, so they can be added to the new 'project' variable.
            List<PlcProjection> plcProjections = list();
            convertProjections(projections, plcProjections, convertResult);

            // Append "plcProjectRoot := <root-value expression>;" to the code, and return a new projection expression
            // with the converted CIF projections.
            PlcVarExpression lhsExpr = new PlcVarExpression(plcProjectRoot, plcProjections);
            convertResult.code.add(new PlcAssignmentStatement(lhsExpr, convertResult.value));
            convertResult.codeVariables.addAll(exprResult.valueVariables); // Parent value is now in code.

            convertResult.valueVariables.add(plcProjectRoot);
            convertResult.setValue(lhsExpr);
            return convertResult;
        }
    }

    /**
     * Convert CIF projections to PLC projections, reversing order.
     *
     * @param cifProjections CIF projections to convert, in reverse order. Last projection to apply should be at index
     *     {@code 0}.
     * @param plcProjections Storage of converted CIF projections. Is extended in-place.
     * @param convertResult Storage of expression generator results from CIF array index expressions.
     */
    private void convertProjections(List<ProjectionExpression> cifProjections, List<PlcProjection> plcProjections,
            ExprGenResult convertResult)
    {
        for (int i = cifProjections.size() - 1; i >= 0; i--) {
            CifType unProjectedType = normalizeType(cifProjections.get(i).getChild().getType());
            Expression cifIndexExpr = cifProjections.get(i).getIndex();

            if (unProjectedType instanceof ListType) {
                // Convert the index.
                ExprGenResult indexResult = convertExpr(cifIndexExpr);
                convertResult.mergeCodeAndVariables(indexResult);
                plcProjections.add(new PlcArrayProjection(indexResult.value));
            } else if (unProjectedType instanceof TupleType tt) {
                int fieldIndex;
                if (cifIndexExpr instanceof FieldExpression fe) {
                    fieldIndex = tt.getFields().indexOf(fe.getField());
                } else if (cifIndexExpr instanceof IntExpression ie) {
                    fieldIndex = ie.getValue();
                } else {
                    throw new AssertionError("Unexpected index expr \"" + cifIndexExpr + "\" found.");
                }

                PlcType structTypeName = typeGenerator.convertType(unProjectedType);
                PlcStructType structType = typeGenerator.getStructureType(structTypeName);
                plcProjections.add(new PlcStructProjection(structType.fields.get(fieldIndex).name));
            } else {
                throw new AssertionError("Unexpected unprojected type \"" + unProjectedType + "\" found.");
            }
        }
    }

    /**
     * Convert a function call.
     *
     * @param funcCallExpr Expression performing the call.
     * @return The converted expression.
     */
    private ExprGenResult convertFuncCallExpr(FunctionCallExpression funcCallExpr) {
        // Convert all parameters of the call.
        List<ExprGenResult> argumentResults = listc(funcCallExpr.getParams().size());
        for (Expression param: funcCallExpr.getParams()) {
            argumentResults.add(convertExpr(param));
        }

        // Dispatch call construction based on the function being called.
        Expression fexpr = funcCallExpr.getFunction();
        if (fexpr instanceof StdLibFunctionExpression stdlibExpr) {
            return convertStdlibExpr(funcCallExpr, argumentResults);
        }
        // TODO: Implement function calls to internal user-defined functions.
        throw new RuntimeException("Calls to internal user-defined functions are not implemented yet.");
    }

    /**
     * Convert a call to a standard library function.
     *
     * @param stdlibCallExpr The performed call to convert.
     * @param argumentResults Already converted argument values of the call.
     * @return The converted expression.
     */
    private ExprGenResult convertStdlibExpr(FunctionCallExpression stdlibCallExpr,
            List<ExprGenResult> argumentResults)
    {
        List<Expression> arguments = stdlibCallExpr.getParams();
        StdLibFunction stdlib = ((StdLibFunctionExpression)stdlibCallExpr.getFunction()).getFunction();
        switch (stdlib) {
            case ABS: {
                Assert.check(argumentResults.size() == 1);
                ExprGenResult arg1 = argumentResults.get(0);
                return arg1.setValue(funcAppls.absFuncAppl(arg1.value));
            }

            case CBRT: {
                // Use reals to get real result. Use two real-typed values to support S7-400 and S7-300.
                PlcExpression expValue = funcAppls.divideFuncAppl(new PlcRealLiteral("1.0"), new PlcRealLiteral("3.0"));

                Assert.check(argumentResults.size() == 1);
                ExprGenResult arg1 = argumentResults.get(0);
                return arg1.setValue(funcAppls.powerFuncAppl(arg1.value, expValue));
            }

            case CEIL:
                // Unsupported. IEC 61131-3 has only TRUNC (round
                // towards zero) and REAL_TO_INT (rounds to the nearest
                // even integer if equally far from two integers).
                throw new RuntimeException("Precondition violation.");

            case DELETE:
                // Unsupported.
                throw new RuntimeException("Precondition violation.");

            case EMPTY:
                // Unsupported.
                throw new RuntimeException("Precondition violation.");

            case EXP: {
                Assert.check(argumentResults.size() == 1);
                ExprGenResult arg1 = argumentResults.get(0);
                return arg1.setValue(funcAppls.expFuncAppl(arg1.value));
            }

            case FLOOR:
                // Unsupported. IEC 61131-3 has only TRUNC (round
                // towards zero) and REAL_TO_INT (rounds to the nearest
                // even integer if equally far from two integers).
                throw new RuntimeException("Precondition violation.");

            case FORMAT:
                // Unsupported.
                throw new RuntimeException("Precondition violation.");

            case LN: {
                Assert.check(argumentResults.size() == 1);
                ExprGenResult arg1 = argumentResults.get(0);
                return arg1.setValue(funcAppls.lnFuncAppl(arg1.value));
            }

            case LOG: {
                Assert.check(argumentResults.size() == 1);
                ExprGenResult arg1 = argumentResults.get(0);

                if (!target.supportsOperation(PlcFuncOperation.STDLIB_LOG)) {
                    // Fallback to log10(x) = ln(x) / ln(10).
                    PlcExpression lnX = funcAppls.lnFuncAppl(arg1.value);
                    PlcExpression ln10 = funcAppls.lnFuncAppl(new PlcRealLiteral("10.0"));
                    return arg1.setValue(funcAppls.divideFuncAppl(lnX, ln10));
                }
                return arg1.setValue(funcAppls.logFuncAppl(arg1.value));
            }

            case MAXIMUM:
            case MINIMUM: {
                CifType ltype = normalizeType(arguments.get(0).getType());
                CifType rtype = normalizeType(arguments.get(1).getType());
                PlcExpression leftSide = unifyTypeOfExpr(argumentResults.get(0).value, ltype, rtype);
                PlcExpression rightSide = unifyTypeOfExpr(argumentResults.get(1).value, rtype, ltype);

                // TODO Both MIN and MAX can be flattened to N-ary function calls thus allowing to perform multiple such
                // CIF function calls at the same time.
                ExprGenResult result = new ExprGenResult(this, argumentResults.get(0), argumentResults.get(1));
                if (stdlib == StdLibFunction.MAXIMUM) {
                    return result.setValue(funcAppls.maxFuncAppl(leftSide, rightSide));
                } else {
                    return result.setValue(funcAppls.minFuncAppl(leftSide, rightSide));
                }
            }

            case POP:
                // Unsupported.
                throw new RuntimeException("Precondition violation.");

            case POWER: {
                CifType baseType = normalizeType(arguments.get(0).getType());
                CifType powerType = normalizeType(arguments.get(1).getType());
                boolean baseIsInt = baseType instanceof IntType;
                boolean powerIsInt = powerType instanceof IntType;

                // CIF input and output expectations.
                boolean baseAllowsInt = baseIsInt && !isRangeless((IntType)baseType);
                boolean powerAllowsInt = powerIsInt && !isRangeless((IntType)powerType) && ((IntType)powerType).getLower() >= 0;
                boolean cifIntResult = baseAllowsInt & powerAllowsInt;

                // Find an input type combination that works for the PLC.
                boolean plcBaseIsInt = baseIsInt;
                boolean plcPowerIsInt = powerIsInt;
                if (!target.supportsPower(plcBaseIsInt, plcPowerIsInt) && plcBaseIsInt) {
                    plcBaseIsInt = false; // 'int ** X' doesn't work, use a real as base type.
                }
                if (!target.supportsPower(plcBaseIsInt, plcPowerIsInt) && plcPowerIsInt) {
                    plcPowerIsInt = false; // 'X ** int' doesn't work, use a real as power type.
                }
                // Either a working combination has been found or we fell back to the always supported
                // POW(real, real) case.

                // Convert both sides if needed.
                PlcExpression baseSide = argumentResults.get(0).value;
                if (baseIsInt && !plcBaseIsInt) {
                    baseSide = funcAppls.castFunctionAppl(baseSide, target.getIntegerType(), target.getRealType());
                }
                PlcExpression powerSide = argumentResults.get(1).value;
                if (powerType instanceof IntType && !plcPowerIsInt) {
                    powerSide = funcAppls.castFunctionAppl(powerSide, target.getIntegerType(), target.getRealType());
                }

                // Generate the call.
                PlcExpression powCall = funcAppls.powerFuncAppl(baseSide, powerSide);
                boolean plcIntResult = plcBaseIsInt & plcPowerIsInt;

                // Convert the result back if CIF and PLC types are not the same. Note that the PLC cannot reach an
                // integer typed result if CIF does not have it as the PLC sides are never changed to integer type.
                if (cifIntResult && !plcIntResult) {
                    powCall = funcAppls.castFunctionAppl(powCall, target.getRealType(), target.getIntegerType());
                }

                ExprGenResult result = new ExprGenResult(this, argumentResults.get(0), argumentResults.get(1));
                return result.setValue(powCall);
            }

            case ROUND:
                // Unsupported. IEC 61131-3 has only TRUNC (round
                // towards zero) and REAL_TO_INT (rounds to the nearest
                // even integer if equally far from two integers).
                throw new RuntimeException("Precondition violation.");

            case SCALE:
                // Unsupported. We could support this by expanding
                // it to the definition of 'scale', using addition,
                // subtraction, etc.
                throw new RuntimeException("Precondition violation.");

            case SIGN:
                // Unsupported. We could support this by adding our
                // own sign function.
                throw new RuntimeException("Precondition violation.");

            case SIZE:
                // Unsupported.
                throw new RuntimeException("Precondition violation.");

            case SQRT: {
                Assert.check(argumentResults.size() == 1);
                ExprGenResult arg1 = argumentResults.get(0);
                return arg1.setValue(funcAppls.sqrtFuncAppl(arg1.value));
            }

            case ACOS: {
                Assert.check(argumentResults.size() == 1);
                ExprGenResult arg1 = argumentResults.get(0);
                return arg1.setValue(funcAppls.acosFuncAppl(arg1.value));
            }

            case ASIN: {
                Assert.check(argumentResults.size() == 1);
                ExprGenResult arg1 = argumentResults.get(0);
                return arg1.setValue(funcAppls.asinFuncAppl(arg1.value));
            }

            case ATAN: {
                Assert.check(argumentResults.size() == 1);
                ExprGenResult arg1 = argumentResults.get(0);
                return arg1.setValue(funcAppls.atanFuncAppl(arg1.value));
            }

            case COS: {
                Assert.check(argumentResults.size() == 1);
                ExprGenResult arg1 = argumentResults.get(0);
                return arg1.setValue(funcAppls.cosFuncAppl(arg1.value));
            }

            case SIN: {
                Assert.check(argumentResults.size() == 1);
                ExprGenResult arg1 = argumentResults.get(0);
                return arg1.setValue(funcAppls.sinFuncAppl(arg1.value));
            }

            case TAN: {
                Assert.check(argumentResults.size() == 1);
                ExprGenResult arg1 = argumentResults.get(0);
                return arg1.setValue(funcAppls.tanFuncAppl(arg1.value));
            }

            case ACOSH:
            case ASINH:
            case ATANH:
            case COSH:
            case SINH:
            case TANH:
                // Unsupported.
                throw new RuntimeException("Precondition violation.");

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
                // Unsupported.
                throw new RuntimeException("Precondition violation.");
        }
        throw new RuntimeException("Unexpected standard library function: " + stdlib);
    }

    /**
     * Convert an array literal expression to PLC code.
     *
     * @param listExpr Expression to convert.
     * @return The converted expression.
     */
    private ExprGenResult convertArrayExpr(ListExpression listExpr) {
        PlcType listType = typeGenerator.convertType(listExpr.getType());
        PlcVariable arrayVar = getTempVariable("litArray", listType);

        ExprGenResult result = new ExprGenResult(this);
        int idx = 0;
        for (Expression e: listExpr.getElements()) {
            ExprGenResult childResult = convertExpr(e);
            // Add child computation to the result, return the temporary variables of it.
            result.mergeCode(childResult);
            giveTempVariables(childResult.codeVariables);

            // Construct assignment.
            PlcArrayProjection arrayProj = new PlcArrayProjection(List.of(new PlcIntLiteral(idx)));
            PlcVarExpression lhs = new PlcVarExpression(arrayVar, List.of(arrayProj));
            PlcAssignmentStatement assignment = new PlcAssignmentStatement(lhs, childResult.value);
            idx++;

            // Add statement to the result.
            result.code.add(assignment);
            giveTempVariables(childResult.valueVariables);
        }
        result.valueVariables.add(arrayVar);
        return result.setValue(new PlcVarExpression(arrayVar));
    }

    /**
     * Convert a tuple literal expression to PLC code.
     *
     * @param tupleExpr Expression to convert.
     * @return The converted expression.
     */
    private ExprGenResult convertTupleExpr(TupleExpression tupleExpr) {
        PlcType varType = typeGenerator.convertType(tupleExpr.getType());
        PlcVariable structVar = getTempVariable("litStruct", varType);
        PlcStructType structType = typeGenerator.getStructureType(varType); // The underlying structure type.

        ExprGenResult result = new ExprGenResult(this);
        int idx = 0;
        for (Expression e: tupleExpr.getFields()) {
            ExprGenResult childResult = convertExpr(e);
            // Add child computation to the result, return the temporary variables of it.
            result.mergeCode(childResult);
            giveTempVariables(childResult.codeVariables);

            // Construct assignment.
            PlcStructProjection structProj = new PlcStructProjection(structType.fields.get(idx).name);
            PlcVarExpression lhs = new PlcVarExpression(structVar, List.of(structProj));
            PlcAssignmentStatement assignment = new PlcAssignmentStatement(lhs, childResult.value);
            idx++;

            // Add statement to the result.
            result.code.add(assignment);
            giveTempVariables(childResult.valueVariables);
        }
        result.valueVariables.add(structVar);
        return result.setValue(new PlcVarExpression(structVar));
    }

    /**
     * Unify type {@code myType} of {@code subExpr} to match with type {@code otherType}.
     *
     * @param subExpr Sub expression to unify.
     * @param myType Type of the sub expression.
     * @param otherType Type to unify to.
     * @return Sub expression with matching type to {@code otherType}.
     */
    private PlcExpression unifyTypeOfExpr(PlcExpression subExpr, CifType myType, CifType otherType) {
        if (myType instanceof IntType && otherType instanceof RealType) {
            return funcAppls.castFunctionAppl(subExpr, target.getIntegerType(), target.getRealType());
        }
        return subExpr;
    }
}
