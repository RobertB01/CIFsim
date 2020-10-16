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

package org.eclipse.escet.chi.codegen.types;

import static org.eclipse.escet.chi.codegen.Constants.CHI_SIMULATOR_EXCEPTION_FQC;
import static org.eclipse.escet.common.java.Lists.last;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;

import org.eclipse.escet.chi.codegen.CodeGeneratorContext;
import org.eclipse.escet.chi.codegen.Constants;
import org.eclipse.escet.chi.codegen.expressions.ExpressionBase;
import org.eclipse.escet.chi.codegen.expressions.SimpleExpression;
import org.eclipse.escet.chi.codegen.java.JavaClass;
import org.eclipse.escet.chi.codegen.java.JavaFile;
import org.eclipse.escet.chi.codegen.java.JavaMethod;
import org.eclipse.escet.chi.metamodel.chi.Expression;
import org.eclipse.escet.chi.metamodel.chi.FunctionReference;
import org.eclipse.escet.chi.metamodel.chi.StdLibFunctionReference;
import org.eclipse.escet.common.java.Assert;

/** Class representing a function type of a Chi specification. */
public class FunctionTypeID extends StateLessObjectTypeID {
    /** Class name of the function type. */
    private String className;

    /**
     * Constructor for the {@link FunctionTypeID} class.
     *
     * @param args Argument and result types of the function.
     * @param ctxt Code generator context.
     */
    public FunctionTypeID(List<TypeID> args, CodeGeneratorContext ctxt) {
        super(false, TypeKind.FUNCTION, args);
        if (!ctxt.hasTypeName(this)) {
            className = ctxt.makeUniqueName("FunctionType");
            ctxt.addTypeName(this, className);
            addSelf(ctxt);
        } else {
            className = ctxt.getTypeName(this);
        }
    }

    @Override
    public String getTypeText() {
        String txt = "func " + last(subTypes).getTypeText() + "(";
        for (int i = 0; i < subTypes.size() - 1; i++) {
            if (i > 0) {
                txt += ", ";
            }
            txt += subTypes.get(i).getTypeText();
        }
        return txt + ")";
    }

    @Override
    public String getJavaClassType() {
        return className;
    }

    @Override
    public String getSimplestJavaValue() {
        return "null";
    }

    @Override
    public ExpressionBase convertExprNode(Expression expr, CodeGeneratorContext ctxt, JavaFile currentfile) {
        if (expr instanceof StdLibFunctionReference) {
            // TODO Implement me (but needs work in the runtime to represent
            // stdlib functions as object/enum). Probably not worth the effort.
            Assert.fail("Implement stdlib FunctionTypeID.convertExprNode()");
        } else if (expr instanceof FunctionReference) {
            FunctionReference fref = (FunctionReference)expr;
            String jName = ctxt.getDefinition(fref.getFunction());
            return new SimpleExpression("spec.instance" + jName, expr);
        }
        Assert.fail("Implement convertExprNode(" + expr.toString() + ") of FunctionTypeID");
        return null;
    }

    @Override
    public String getEmptyValue(JavaFile jf) {
        return "new " + getJavaClassType() + "(spec, chiCoordinator)";
    }

    /**
     * Generate a class for the function type, and add it to the type names.
     *
     * @param ctxt Code generator context.
     */
    private void addSelf(CodeGeneratorContext ctxt) {
        JavaMethod cM = null;
        String imports = null;
        List<String> ifaces = null;
        if (subTypes.size() == 3 && subTypes.get(2).kind.equals(TypeKind.BOOL)
                && subTypes.get(0).equals(subTypes.get(1)))
        {
            String jc = subTypes.get(0).getJavaClassType();
            int idx = jc.lastIndexOf('.');
            if (idx != -1) {
                imports = jc;
                jc = jc.substring(idx + 1);
            }
            cM = new JavaMethod("public int compare(" + jc + " x, " + jc + " y)");
            cM.lines.add("if (!compute(null, x, y)) return 1;");
            cM.lines.add("if (!compute(null, y, x)) return -1;");
            cM.lines.add("return 0;");

            ifaces = list("Comparator<" + jc + ">");
        }

        JavaClass cls = ctxt.addJavaClass(className, false, null, ifaces);
        if (imports != null) {
            cls.addImport(imports, false);
        }

        // Add constructor.
        String args = ctxt.specName + " spec, ChiCoordinator chiCoordinator";
        JavaMethod jm = new JavaMethod("public " + className + "(" + args + ")");
        jm.lines.add("this.spec = spec;");
        jm.lines.add("this.chiCoordinator = chiCoordinator;");
        cls.addMethod(jm);
        cls.addImport(Constants.COORDINATOR_FQC, false);
        cls.addVariable(ctxt.specName + " spec;");
        cls.addVariable("ChiCoordinator chiCoordinator;");

        // Add run method.
        String posdata = Constants.POSITION_DATA_FQC;
        posdata = posdata.substring(posdata.lastIndexOf('.') + 1);

        String argsString = "List<" + posdata + "> positionStack";
        cls.addImport("java.util.List", false);
        cls.addImport(Constants.POSITION_DATA_FQC, false);
        int i = 0;
        while (i < subTypes.size() - 1) {
            if (!argsString.isEmpty()) {
                argsString += ", ";
            }
            argsString += fmt("%s arg%d", subTypes.get(i).getJavaType(), i);
            i++;
        }

        JavaMethod runMethod = new JavaMethod("public", subTypes.get(i).getJavaType(), "compute", argsString, null);
        runMethod.lines.add("throw new ChiSimulatorException("
                + "\"Trying to call a function through an uninitialized function variable.\");");
        cls.addImport(CHI_SIMULATOR_EXCEPTION_FQC, false);
        cls.addMethod(runMethod);

        if (cM != null) {
            cls.addMethod(cM);
            cls.addImport("java.util.Comparator", false);
        }
    }
}
