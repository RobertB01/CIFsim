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

package org.eclipse.escet.cif.codegen.simulink;

import static org.eclipse.escet.cif.codegen.c89.C89DataValue.constructReference;
import static org.eclipse.escet.cif.codegen.c89.C89DataValue.makeComputed;
import static org.eclipse.escet.cif.codegen.c89.C89DataValue.makeValue;
import static org.eclipse.escet.cif.codegen.c89.typeinfos.C89TypeInfoHelper.typeUsesValues;
import static org.eclipse.escet.cif.codegen.simulink.SimulinkCodeGenPreChecker.getRowCount;
import static org.eclipse.escet.cif.codegen.simulink.typeinfos.SimulinkArrayTypeInfo.getElementConversionFromSimulinkVector;
import static org.eclipse.escet.cif.common.CifTypeUtils.makeTupleType;
import static org.eclipse.escet.common.emf.EMFHelper.deepclone;
import static org.eclipse.escet.common.java.Maps.mapc;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.Map;

import org.eclipse.escet.cif.codegen.CodeContext;
import org.eclipse.escet.cif.codegen.ExprCode;
import org.eclipse.escet.cif.codegen.assignments.Destination;
import org.eclipse.escet.cif.codegen.assignments.VariableInformation;
import org.eclipse.escet.cif.codegen.c89.C89ExprCodeGen;
import org.eclipse.escet.cif.codegen.typeinfos.TypeInfo;
import org.eclipse.escet.cif.codegen.updates.VariableWrapper;
import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.cif.metamodel.cif.expressions.InputVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.functions.InternalFunction;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.box.MemoryCodeBox;
import org.eclipse.escet.common.java.Assert;

/** Expression code generator for the Simulink target language. */
public class SimulinkExprCodeGen extends C89ExprCodeGen {
    /** Input variables of the specification. */
    public final List<InputVariable> inputVars;

    /** Continuous variables of the specification. */
    public final List<ContVariable> contVars;

    /** Mapping of input variables to their index, for fast access. Constructed on demand. */
    private Map<InputVariable, Integer> inputVarMap = null;

    /** Mapping of continuous variables to their index, for fast access. Constructed on demand. */
    private Map<ContVariable, Integer> contVarMap = null;

    /**
     * Constructor for {@link SimulinkExprCodeGen} class.
     *
     * @param inputVars Input variables of the specification.
     * @param contVars Continuous variables of the specification.
     */
    public SimulinkExprCodeGen(List<InputVariable> inputVars, List<ContVariable> contVars) {
        this.inputVars = inputVars; // Note, at this time, these lists may still be empty.
        this.contVars = contVars;
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
        callText.append("(sim_struct");
        Assert.check(func.getParameters().size() == argsCode.size());
        for (int arg = 0; arg < func.getParameters().size(); arg++) {
            DiscVariable param = func.getParameters().get(arg).getParameter();
            TypeInfo paramTi = ctxt.typeToTarget(param.getType());
            ExprCode argCode = argsCode.get(arg);

            callText.append(", ");
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

        // If required, construct a temporary variable to store the result,
        // else just return the value to the caller.
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
    protected ExprCode convertInputVariableExpression(InputVariableExpression expr, Destination dest,
            CodeContext ctxt)
    {
        // Initialize inputvar map on first call.
        if (inputVarMap == null) {
            inputVarMap = mapc(inputVars.size());
            for (int i = 0; i < inputVars.size(); i++) {
                inputVarMap.put(inputVars.get(i), i);
            }
        }

        InputVariable decl = expr.getVariable();
        VariableWrapper var = new VariableWrapper(decl, false);
        VariableInformation varInfo = ctxt.getReadVarInfo(var);
        int rows = getRowCount(decl.getType());

        ExprCode result = new ExprCode();

        // Generate code to read input from the Simulink input port.
        int index = inputVarMap.get(decl);
        CodeBox code = new MemoryCodeBox(SimulinkCodeGen.INDENT);
        code.add("if (!work->input_loaded%02d) {", index);
        code.indent();
        code.add("InputRealPtrsType uPtrs = ssGetInputPortRealSignalPtrs(sim_struct, %d);", index);
        if (rows == 0) {
            code.add("%s = %s;", varInfo.targetName,
                    getElementConversionFromSimulinkVector(varInfo.typeInfo, "*uPtrs[0]"));
        } else {
            code.add("%sTypeToSimulink(*uPtrs, &%s)", varInfo.typeInfo.getTypeName(), varInfo.targetName);
        }
        code.add("work->input_loaded%02d = TRUE;", index);
        code.dedent();
        code.add("}");
        result.add(code);

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
            String resultText = fmt("%s(sim_struct)", varInfo.targetName);
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

        ExprCode result = new ExprCode();
        result.setDestination(dest);
        if (!varInfo.isTempVar && isDerivative) {
            if (contVarMap == null) {
                contVarMap = mapc(contVars.size());
                for (int i = 0; i < contVars.size(); i++) {
                    contVarMap.put(contVars.get(i), i + 1);
                }
            }

            String resultText = fmt("deriv%02d(sim_struct)", contVarMap.get(contVar));
            result.setDataValue(makeComputed(resultText));
        } else {
            result.setDataValue(makeValue(varInfo.targetName));
        }
        return result;
    }
}
