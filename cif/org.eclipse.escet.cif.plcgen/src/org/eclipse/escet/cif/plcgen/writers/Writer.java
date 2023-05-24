//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.plcgen.writers;

import static org.eclipse.escet.common.java.Strings.fmt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.eclipse.escet.cif.plcgen.model.declarations.PlcConfiguration;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcGlobalVarList;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcPou;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcPouInstance;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcProject;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcResource;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcTask;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcTypeDecl;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcVariable;
import org.eclipse.escet.cif.plcgen.model.types.PlcArrayType;
import org.eclipse.escet.cif.plcgen.model.types.PlcDerivedType;
import org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType;
import org.eclipse.escet.cif.plcgen.model.types.PlcEnumType;
import org.eclipse.escet.cif.plcgen.model.types.PlcStructType;
import org.eclipse.escet.cif.plcgen.model.types.PlcType;
import org.eclipse.escet.cif.plcgen.targets.PlcTarget;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.exceptions.InputOutputException;
import org.eclipse.escet.common.box.Box;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.box.HBox;
import org.eclipse.escet.common.box.MemoryCodeBox;
import org.eclipse.escet.common.box.TextBox;
import org.eclipse.escet.common.java.Assert;

/** Base class for writing PLC code for a given target type. */
public abstract class Writer {
    /** The indentation to use for the Structured Text files. */
    public static final int INDENT = 4;

    /** PLC target to generate code for. */
    protected final PlcTarget target;

    /**
     * Constructor of the {@link Writer} class.
     *
     * @param target PLC target to generate code for.
     */
    protected Writer(PlcTarget target) {
        this.target = target;
    }

    /**
     * Convert the project contents to output acceptable for a PLC target type.
     *
     * @param project PLC program code to convert.
     * @param outputPath The absolute local file system destination to write the converted output.
     */
    public abstract void write(PlcProject project, String outputPath);

    /**
     * Ensure a directory with the given path exists, possibly by creating it.
     *
     * @param outPath Path to the directory that should exist after the call.
     */
    protected void ensureDirectory(String outPath) {
        String absPath = Paths.resolve(outPath);
        Path nioAbsPath = java.nio.file.Paths.get(absPath);
        if (!Files.isDirectory(nioAbsPath)) {
            try {
                Files.createDirectories(nioAbsPath);
            } catch (IOException ex) {
                String msg = fmt("Failed to create output directory \"%s\" for the generated PLC code.", outPath);
                throw new InputOutputException(msg, ex);
            }
        }
    }

    /**
     * Convert a {@link PlcProject} instance to a {@link Box} text.
     *
     * @param project Project to convert.
     * @return The generated box representation.
     */
    protected Box toBox(PlcProject project) {
        // IEC 61131-3 has no projects, so this syntax is not standard
        // compliant.
        CodeBox c = new MemoryCodeBox(INDENT);
        c.add("PROJECT %s", project.name);
        c.indent();
        for (PlcTypeDecl typeDecl: project.typeDecls) {
            c.add(toBox(typeDecl));
        }
        for (PlcPou pou: project.pous) {
            c.add(toBox(pou));
        }
        for (PlcConfiguration configuration: project.configurations) {
            c.add(toBox(configuration));
        }
        c.dedent();
        c.add("END_PROJECT");
        return c;
    }

    /**
     * Convert a {@link PlcConfiguration} instance to a {@link Box} text.
     *
     * @param configuration Configuration to convert.
     * @return The generated box representation.
     */
    protected Box toBox(PlcConfiguration configuration) {
        CodeBox c = new MemoryCodeBox(INDENT);
        c.add("CONFIGURATION %s", configuration.name);
        c.indent();
        for (PlcGlobalVarList globalVarList: configuration.globalVarLists) {
            if (globalVarList.variables.isEmpty()) {
                continue;
            }
            c.add(toBox(globalVarList));
        }

        // Ensure one resource. At least one is required. More resources, means
        // we have to use 'RESOURCE <name> ON <type>' syntax, and we don't
        // want to specify the <type>.
        Assert.check(configuration.resources.size() <= 1);
        for (PlcResource resource: configuration.resources) {
            c.add(toBox(resource));
        }

        c.dedent();
        c.add("END_CONFIGURATION");
        return c;
    }

    /**
     * Convert a {@link PlcTask} instance to a {@link Box} text.
     *
     * @param task Task to convert.
     * @return The generated box representation.
     */
    protected Box toBox(PlcTask task) {
        // POU instances are boxed by the PLC resource.
        return new TextBox("TASK %s(INTERVAL := t#%dms, PRIORITY := %d);", task.name, task.cycleTime, task.priority);
    }

    /**
     * Convert a {@link PlcResource} instance to a {@link Box} text.
     *
     * @param resource Resource to convert.
     * @return The generated box representation.
     */
    protected Box toBox(PlcResource resource) {
        // We only support a single resource for now, so the 'name' is not
        // included in the box representation, to avoid having to specify a
        // resource type name.
        CodeBox c = new MemoryCodeBox(INDENT);
        for (PlcGlobalVarList globalVarList: resource.globalVarLists) {
            if (globalVarList.variables.isEmpty()) {
                continue;
            }
            c.add(toBox(globalVarList));
        }
        for (PlcTask task: resource.tasks) {
            c.add(toBox(task));
        }
        for (PlcPouInstance pouInstance: resource.pouInstances) {
            c.add(toBox(pouInstance, null));
        }
        for (PlcTask task: resource.tasks) {
            for (PlcPouInstance pouInstance: task.pouInstances) {
                c.add(toBox(pouInstance, task.name));
            }
        }
        return c;
    }

    /**
     * Convert a {@link PlcGlobalVarList} instance to a {@link Box} text.
     *
     * @param globVarList Global variable list to convert.
     * @return The generated box representation.
     */
    protected Box toBox(PlcGlobalVarList globVarList) {
        Assert.check(!globVarList.variables.isEmpty()); // Empty VAR_GLOBAL is illegal.
        CodeBox c = new MemoryCodeBox(INDENT);
        c.add("VAR_GLOBAL%s // %s", globVarList.constants ? " CONSTANT" : "", globVarList.name);
        c.indent();
        for (PlcVariable variable: globVarList.variables) {
            c.add(toBox(variable));
        }
        c.dedent();
        c.add("END_VAR");
        return c;
    }

    /**
     * Convert a {@link PlcVariable} instance to a {@link Box} text.
     *
     * @param variable Variable to convert.
     * @return The generated box representation.
     */
    protected Box toBox(PlcVariable variable) {
        String addrTxt = (variable.address == null) ? "" : fmt(" AT %s", variable.address);
        String valueTxt = (variable.value == null) ? ""
                : " := " + target.getModelTextGenerator().toString(variable.value);
        String txt = fmt("%s%s: %s%s;", variable.name, addrTxt, toBox(variable.type), valueTxt);
        return new TextBox(txt);
    }

    /**
     * Convert a {@link PlcPouInstance} instance to a {@link Box} text.
     *
     * @param pouInstance POU instance to convert.
     * @return The generated box representation.
     */
    protected Box toBox(PlcPouInstance pouInstance) {
        return toBox(pouInstance, null);
    }

    /**
     * Returns a {@link Box} representation of the {@link PlcPouInstance} object.
     *
     * @param pouInstance POU instance to convert.
     * @param taskName The name of the task on which to instantiate the POU, or {@code null} if not applicable.
     * @return The generated box representation.
     */
    protected Box toBox(PlcPouInstance pouInstance, String taskName) {
        String taskTxt = (taskName == null) ? "" : fmt(" WITH %s", taskName);
        return new TextBox("PROGRAM %s%s: %s;", pouInstance.name, taskTxt, pouInstance.pou.name);
    }

    /**
     * Convert a {@link PlcPou} instance to a {@link Box} text.
     *
     * @param pou POU to convert.
     * @return The generated box representation.
     */
    protected Box toBox(PlcPou pou) {
        CodeBox c = headerToBox(pou);
        c.add();
        c.add(pou.body);
        c.add("END_%s", pou.pouType);
        return c;
    }

    /**
     * Converts the header of the POU to IEC 61131-3 syntax. The header includes the POU type, name, return type, and
     * variables, but neither the body nor the final closing keyword.
     *
     * @param pou POU header to convert.
     * @return The header of the POU in IEC 61131-3 syntax.
     */
    protected CodeBox headerToBox(PlcPou pou) {
        CodeBox c = new MemoryCodeBox(INDENT);
        String retTypeTxt = (pou.retType == null) ? "" : fmt(": %s", toBox(pou.retType));
        c.add("%s %s%s", pou.pouType, pou.name, retTypeTxt);
        if (!pou.inputVars.isEmpty()) {
            c.add("VAR_INPUT");
            c.indent();
            for (PlcVariable var: pou.inputVars) {
                c.add(toBox(var));
            }
            c.dedent();
            c.add("END_VAR");
        }
        if (!pou.outputVars.isEmpty()) {
            c.add("VAR_OUTPUT");
            c.indent();
            for (PlcVariable var: pou.outputVars) {
                c.add(toBox(var));
            }
            c.dedent();
            c.add("END_VAR");
        }
        if (!pou.localVars.isEmpty()) {
            c.add("VAR");
            c.indent();
            for (PlcVariable var: pou.localVars) {
                c.add(toBox(var));
            }
            c.dedent();
            c.add("END_VAR");
        }
        if (!pou.tempVars.isEmpty()) {
            c.add("VAR_TEMP");
            c.indent();
            for (PlcVariable var: pou.tempVars) {
                c.add(toBox(var));
            }
            c.dedent();
            c.add("END_VAR");
        }
        return c;
    }

    /**
     * Convert a {@link PlcType} instance to a {@link Box} text.
     *
     * @param type Type to convert.
     * @return The generated box representation.
     */
    protected Box toBox(PlcType type) {
        if (type instanceof PlcArrayType) {
            return toBox((PlcArrayType)type);
        } else if (type instanceof PlcDerivedType) {
            return toBox((PlcDerivedType)type);
        } else if (type instanceof PlcElementaryType) {
            return toBox((PlcElementaryType)type);
        } else if (type instanceof PlcEnumType) {
            return toBox((PlcEnumType)type);
        } else if (type instanceof PlcStructType) {
            return toBox((PlcStructType)type);
        } else {
            String typeText = (type == null) ? "null" : type.getClass().toString();
            throw new AssertionError("Unexpected PlcType, found: " + typeText + ".");
        }
    }

    /**
     * Convert a {@link PlcTypeDecl} instance to a {@link Box} text.
     *
     * @param typeDecl Type declaration to convert.
     * @return The generated box representation.
     */
    protected Box toBox(PlcTypeDecl typeDecl) {
        CodeBox c = new MemoryCodeBox(INDENT);
        c.add("TYPE %s:", typeDecl.name);
        c.indent();
        c.add(new HBox(toBox(typeDecl.type), ";"));
        c.dedent();
        c.add("END_TYPE");
        return c;
    }

    /**
     * Convert a {@link PlcEnumType} instance to a {@link Box} text.
     *
     * @param enumType Enumeration type to convert.
     * @return The generated box representation.
     */
    protected Box toBox(PlcEnumType enumType) {
        return new TextBox("(%s)", String.join(", ", enumType.literals));
    }

    /**
     * Convert a {@link PlcElementaryType} instance to a {@link Box} text.
     *
     * @param elementaryType Elementary type to convert.
     * @return The generated box representation.
     */
    protected Box toBox(PlcElementaryType elementaryType) {
        return new TextBox(elementaryType.name);
    }

    /**
     * Convert a {@link PlcDerivedType} instance to a {@link Box} text.
     *
     * @param derivedType Derived type to convert.
     * @return The generated box representation.
     */
    protected Box toBox(PlcDerivedType derivedType) {
        return new TextBox(derivedType.name);
    }

    /**
     * Convert a {@link PlcArrayType} instance to a {@link Box} text.
     *
     * @param arrayType Array type to convert.
     * @return The generated box representation.
     */
    protected Box toBox(PlcArrayType arrayType) {
        HBox b = new HBox();
        b.add(fmt("ARRAY[%d..%d] of ", arrayType.lower, arrayType.upper));
        b.add(toBox(arrayType.elemType));
        return b;
    }

    /**
     * Convert a {@link PlcStructType} instance to a {@link Box} text.
     *
     * @param structType Struct type to convert.
     * @return The generated box representation.
     */
    protected Box toBox(PlcStructType structType) {
        CodeBox c = new MemoryCodeBox(INDENT);
        c.add("STRUCT");
        c.indent();
        for (PlcVariable field: structType.fields) {
            c.add(toBox(field));
        }
        c.dedent();
        c.add("END_STRUCT");
        return c;
    }
}
