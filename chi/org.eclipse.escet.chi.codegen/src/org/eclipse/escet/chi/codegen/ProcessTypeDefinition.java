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

package org.eclipse.escet.chi.codegen;

import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;

import org.eclipse.escet.chi.codegen.java.JavaEnum;
import org.eclipse.escet.chi.codegen.java.JavaFile;
import org.eclipse.escet.chi.codegen.java.JavaMethod;
import org.eclipse.escet.chi.codegen.types.TypeID;
import org.eclipse.escet.chi.metamodel.chi.Declaration;
import org.eclipse.escet.chi.metamodel.chi.ProcessDeclaration;

/**
 * Enumeration for storing everything of a particular process type, and for generating the process type instantiation
 * Java code.
 */
public class ProcessTypeDefinition {
    /** Type representation of the process type signature. */
    public final TypeID tid;

    /** Process definitions with the above type signature. */
    public List<ProcessDeclaration> processes;

    /**
     * Constructor of the {@link ProcessTypeDefinition} class.
     *
     * @param tid Type of the processes.
     */
    public ProcessTypeDefinition(TypeID tid) {
        this.tid = tid;
        processes = list();
    }

    /**
     * Generate process type construction with this process type signature.
     *
     * @param ctxt Code generation context.
     */
    public void generate(CodeGeneratorContext ctxt) {
        // Enum with names of process definitions.
        JavaEnum javaEnum = new JavaEnum(ctxt.getTypeName(tid));
        javaEnum.addValue("PP_NONE");
        for (ProcessDeclaration pd: processes) {
            String value = "P_" + pd.getName();
            javaEnum.addValue(value);
        }

        // Instantiating the process (Processtype.create(<args>) method in the
        // enumeration).
        String typedArgs = ctxt.specName + " spec, ChiCoordinator chiCoordinator";
        javaEnum.addImport(Constants.COORDINATOR_FQC, false);
        String actualArgs = "spec, chiCoordinator";
        for (int i = 0; i < tid.subTypes.size(); i++) {
            TypeID subTid = tid.subTypes.get(i);
            if (!typedArgs.isEmpty()) {
                typedArgs += ", ";
            }
            if (!actualArgs.isEmpty()) {
                actualArgs += ", ";
            }
            typedArgs += subTid.getJavaType() + " arg" + String.valueOf(i);
            actualArgs += "arg" + String.valueOf(i);
        }
        String header = "public BaseProcess create(" + typedArgs + ")";
        JavaMethod jm = new JavaMethod(header);
        javaEnum.addImport(Constants.BASEPROCESS_FQC, false);

        jm.lines.add("switch (this) {");
        jm.lines.indent();
        for (ProcessDeclaration pd: processes) {
            jm.lines.add("case P_%s: return new %s(%s);", pd.getName(), ctxt.getDefinition(pd), actualArgs);
        }
        jm.lines.dedent();
        jm.lines.add("}");
        jm.lines.add("throw new ChiSimulatorException(\"Process variable not initialized.\");");
        javaEnum.addImport(Constants.CHI_SIMULATOR_EXCEPTION_FQC, false);
        javaEnum.addMethod(jm);
        ctxt.addClass(javaEnum);
    }

    /**
     * Add a process definition with this process type.
     *
     * @param pd Process definition to add.
     */
    public void addProcess(ProcessDeclaration pd) {
        processes.add(pd);
    }

    /**
     * Add all process definitions of the specification to the process type mapping.
     *
     * @param decls Declarations of the specification.
     * @param specClass Specification class (storage of the process type enums)
     * @param ctxt Code generator context.
     */
    public static void addProcessTypes(List<Declaration> decls, JavaFile specClass, CodeGeneratorContext ctxt) {
        for (Declaration decl: decls) {
            if (!(decl instanceof ProcessDeclaration)) {
                continue;
            }
            ProcessDeclaration pd = (ProcessDeclaration)decl;
            ctxt.addProcessDefinition(pd);
        }
    }
}
