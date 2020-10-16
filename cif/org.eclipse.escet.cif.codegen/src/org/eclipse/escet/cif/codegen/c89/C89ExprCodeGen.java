//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.codegen.c89;

import static org.eclipse.escet.cif.codegen.c89.C89DataValue.constructReference;
import static org.eclipse.escet.cif.codegen.c89.C89DataValue.makeComputed;
import static org.eclipse.escet.cif.codegen.c89.C89DataValue.makeLiteral;
import static org.eclipse.escet.cif.codegen.c89.C89DataValue.makeValue;
import static org.eclipse.escet.cif.codegen.c89.typeinfos.C89TypeInfoHelper.typeUsesValues;
import static org.eclipse.escet.cif.common.CifTypeUtils.makeTupleType;
import static org.eclipse.escet.common.emf.EMFHelper.deepclone;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.str;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.escet.cif.codegen.CodeContext;
import org.eclipse.escet.cif.codegen.ExprCode;
import org.eclipse.escet.cif.codegen.ExprCodeGen;
import org.eclipse.escet.cif.codegen.IfElseGenerator;
import org.eclipse.escet.cif.codegen.assignments.Destination;
import org.eclipse.escet.cif.codegen.assignments.VariableInformation;
import org.eclipse.escet.cif.codegen.typeinfos.TypeInfo;
import org.eclipse.escet.cif.codegen.updates.VariableWrapper;
import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.expressions.ConstantExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ElifExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IfExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.InputVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.functions.InternalFunction;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.cif.metamodel.cif.types.RealType;
import org.eclipse.escet.cif.metamodel.cif.types.StringType;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.java.Assert;

/** C89 expression code generator. */
public class C89ExprCodeGen extends ExprCodeGen {
    @Override
    protected ExprCode predTextsToTarget(List<ExprCode> predCodes, CodeContext ctxt) {
        if (predCodes.isEmpty()) {
            ExprCode result = new ExprCode();
            result.setDataValue(makeLiteral("true"));
            return result;
        }
        if (predCodes.size() == 1) {
            return first(predCodes);
        }

        // Add parentheses around each expression.
        ExprCode result = new ExprCode();
        String[] preds = new String[predCodes.size()];
        for (int i = 0; i < predCodes.size(); i++) {
            // Due to short-circuit evaluation, only code for the first
            // condition may be evaluated. Since the Java code generator
            // shouldn't generate pre-execute code for conditions, an
            // assertion check suffices.
            if (i == 0) {
                result.add(predCodes.get(i));
            } else {
                Assert.check(!predCodes.get(i).hasCode());
            }
            preds[i] = fmt("(%s)", predCodes.get(i).getData());
        }
        result.setDataValue(makeValue(StringUtils.join(preds, " && ")));
        return result;
    }

    /**
     * Convert a type in a cast to a partial name of the cast function to apply.
     *
     * @param type Type to convert.
     * @return Partial name of the cast function.
     */
    private String getCastTypeName(CifType type) {
        if (type instanceof BoolType) {
            return "Bool";
        } else if (type instanceof IntType) {
            return "Int";
        } else if (type instanceof RealType) {
            return "Real";
        }
        throw new RuntimeException("Unexpected type in cast name conversion: " + str(type));
    }

    @Override
    protected ExprCode convertCastExpression(CifType exprType, CifType childType, Expression child, Destination dest,
            CodeContext ctxt)
    {
        ExprCode childCode = ctxt.exprToTarget(child, null);

        ExprCode result = new ExprCode();
        result.add(childCode);

        if (exprType instanceof StringType) {
            // bool/int/real -> string cast.
            String destVarname;
            String targetVar;
            if (dest == null) {
                // Construct temporary var.
                VariableInformation tempVarInfo = ctxt.makeTempVariable(exprType, "str_dest");
                destVarname = tempVarInfo.targetName;
                result.setDataValue(makeComputed(destVarname));
                targetVar = "&" + tempVarInfo.targetName;
            } else {
                destVarname = dest.getData();
                targetVar = dest.getReference();
            }
            String resultText = fmt("%sToString(%s, %s);", // BoolToString(true, &strvar);
                    getCastTypeName(childType), childCode.getData(), targetVar);
            result.add(resultText);
            return result;
        } else if (childType instanceof StringType) {
            // string -> bool/int/real cast.
            TypeInfo childTi = ctxt.typeToTarget(childType);
            String childRef = constructReference(childCode.getRawDataValue(), childTi, ctxt, result);
            String resultText = fmt("StringTo%s(%s)", getCastTypeName(exprType), childRef);
            result.setDestination(dest);
            result.setDataValue(makeComputed(resultText));
            return result;
        } else {
            // int -> real cast.
            Assert.check(exprType instanceof RealType);
            Assert.check(childType instanceof IntType);
            String resultText = fmt("(RealType)(%s)", childCode.getData());
            result.setDestination(dest);
            result.setDataValue(makeComputed(resultText));
            return result;
        }
    }

    @Override
    protected ExprCode convertIfExpression(IfExpression expr, Destination dest, CodeContext ctxt) {
        VariableInformation tempVarInfo = ctxt.makeTempVariable(expr.getType(), "if_dest");

        CodeBox code = ctxt.makeCodeBox();
        code.add(fmt("%s %s;", tempVarInfo.typeInfo.getTargetType(), tempVarInfo.targetName));

        IfElseGenerator ifElse = ctxt.getIfElseUpdateGenerator();

        // Convert 'if' branch.
        ExprCode guards = predsToTarget(expr.getGuards(), ctxt);
        ifElse.generateIf(guards, code);
        ExprCode branchCode = ctxt.exprToTarget(expr.getThen(), ctxt.makeDestination(tempVarInfo));
        code.add(branchCode.getCode());

        // Convert else if branches.
        for (ElifExpression elif: expr.getElifs()) {
            guards = predsToTarget(elif.getGuards(), ctxt);
            ifElse.generateElseIf(guards, code);
            branchCode = ctxt.exprToTarget(elif.getThen(), ctxt.makeDestination(tempVarInfo));
            code.add(branchCode.getCode());
        }

        // Convert 'else' branch.
        ifElse.generateElse(code);
        branchCode = ctxt.exprToTarget(expr.getElse(), ctxt.makeDestination(tempVarInfo));
        code.add(branchCode.getCode());

        ifElse.generateEndIf(code); // Close the if statement.

        ExprCode ifCode = new ExprCode();
        ifCode.add(code);
        ifCode.setDestination(dest);
        ifCode.setDataValue(makeComputed(tempVarInfo.targetName));
        return ifCode;
    }

    @Override
    protected ExprCode convertInternalFunctionCall(InternalFunction func, List<ExprCode> argsCode, Destination dest,
            CodeContext ctxt)
    {
        ExprCode result = new ExprCode();
        for (ExprCode argCode: argsCode) {
            result.add(argCode);
        }

        StringBuilder callText = new StringBuilder();
        callText.append(ctxt.getFunctionName(func));
        callText.append('(');
        Assert.check(func.getParameters().size() == argsCode.size());
        for (int arg = 0; arg < func.getParameters().size(); arg++) {
            DiscVariable param = func.getParameters().get(arg).getParameter();
            TypeInfo paramTi = ctxt.typeToTarget(param.getType());
            ExprCode argCode = argsCode.get(arg);

            if (arg != 0) {
                callText.append(", ");
            }
            if (typeUsesValues(paramTi)) {
                callText.append(argCode.getData());
            } else {
                String refText = constructReference(argCode.getRawDataValue(), paramTi, ctxt, result);
                callText.append(refText);
            }
        }
        callText.append(')');

        CifType retType = makeTupleType(deepclone(func.getReturnTypes()));
        TypeInfo retTi = ctxt.typeToTarget(retType);

        // Result type is always a value!
        // Adapt to match expectations of the caller.
        boolean needsTemporary;
        if (dest == null) {
            if (typeUsesValues(retTi)) {
                // It's normal to return a value of this type, no temporary needed.
                needsTemporary = false;
            } else {
                // Type is passed by reference, but there is no destination. Create a temporary for it.
                needsTemporary = true;
            }
        } else {
            // A destination exists, conversion to a value is always possible.
            needsTemporary = false;
        }

        // If required, construct a temporary variable to store the result, else just return the value to the caller.
        if (needsTemporary) {
            VariableInformation tempVarInfo = ctxt.makeTempVariable(retType, "ret_val");
            result.add(fmt("%s %s = %s;", tempVarInfo.typeInfo.getTargetType(), tempVarInfo.targetName,
                    callText.toString()));
            result.setDestination(dest);
            result.setDataValue(makeValue(tempVarInfo.targetName));
            return result;
        } else {
            result.setDestination(dest);
            result.setDataValue(makeComputed(callText.toString()));
            return result;
        }
    }

    @Override
    protected ExprCode convertConstantExpression(ConstantExpression expr, Destination dest, CodeContext ctxt) {
        ExprCode result = new ExprCode();
        VariableWrapper var = new VariableWrapper(expr.getConstant(), false);
        VariableInformation varInfo = ctxt.getReadVarInfo(var);
        result.setDestination(dest);
        result.setDataValue(makeValue(varInfo.targetName));
        return result;
    }

    @Override
    public ExprCode convertDiscVariableExpression(DiscVariable discVar, Destination dest, CodeContext ctxt) {
        ExprCode result = new ExprCode();
        VariableWrapper var = new VariableWrapper(discVar, false);
        VariableInformation varInfo = ctxt.getReadVarInfo(var);
        result.setDestination(dest);
        result.setDataValue(makeValue(varInfo.targetName));
        return result;
    }

    @Override
    protected ExprCode convertAlgVariableExpression(AlgVariable algVar, Destination dest, CodeContext ctxt) {
        ExprCode result = new ExprCode();
        VariableWrapper var = new VariableWrapper(algVar, false);
        VariableInformation varInfo = ctxt.getReadVarInfo(var);
        if (varInfo.isTempVar) {
            String resultText = varInfo.targetName;
            result.setDataValue(makeValue(resultText));
        } else {
            String resultText = fmt("%s()", varInfo.targetName);
            result.setDataValue(makeComputed(resultText));
        }
        result.setDestination(dest);
        return result;
    }

    @Override
    protected ExprCode convertContVariableExpression(ContVariable contVar, boolean isDerivative, Destination dest,
            CodeContext ctxt)
    {
        VariableWrapper var = new VariableWrapper(contVar, isDerivative);
        VariableInformation varInfo = ctxt.getReadVarInfo(var);
        String varName = varInfo.targetName;

        ExprCode result = new ExprCode();
        result.setDestination(dest);
        if (!varInfo.isTempVar && isDerivative) {
            String resultText = fmt("%sderiv()", varName);
            result.setDataValue(makeComputed(resultText));
        } else {
            result.setDataValue(makeValue(varName));
        }
        return result;
    }

    @Override
    protected ExprCode convertInputVariableExpression(InputVariableExpression expr, Destination dest,
            CodeContext ctxt)
    {
        ExprCode result = new ExprCode();
        VariableWrapper var = new VariableWrapper(expr.getVariable(), false);
        VariableInformation varInfo = ctxt.getReadVarInfo(var);
        result.setDestination(dest);
        result.setDataValue(makeValue(varInfo.targetName));
        return result;
    }
}
