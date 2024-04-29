//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022, 2024 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.cif.plcgen.model.declarations.PlcPouType.PROGRAM;
import static org.eclipse.escet.cif.plcgen.targets.PlcTargetType.S7_1200;
import static org.eclipse.escet.cif.plcgen.targets.PlcTargetType.S7_1500;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.EnumSet;
import java.util.List;

import org.eclipse.escet.cif.plcgen.conversion.ModelTextGenerator;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcBasicVariable;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcConfiguration;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcDataVariable;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcDeclaredType;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcGlobalVarList;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcGlobalVarList.PlcVarListKind;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcPou;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcPouType;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcProject;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcResource;
import org.eclipse.escet.cif.plcgen.model.types.PlcDerivedType;
import org.eclipse.escet.cif.plcgen.model.types.PlcEnumType;
import org.eclipse.escet.cif.plcgen.model.types.PlcStructType;
import org.eclipse.escet.cif.plcgen.targets.PlcTarget;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.box.Box;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.box.MemoryCodeBox;
import org.eclipse.escet.common.java.Assert;

/** S7 writer for S7-1500, S7-1200, S7-400 and S7-300 SIMATIC controllers. */
public class S7Writer extends Writer {
    /**
     * Constructor for the {@link S7Writer} class.
     *
     * @param target PLC target to generate code for.
     */
    public S7Writer(PlcTarget target) {
        super(target);
    }

    @Override
    public void write(PlcProject project, String outPath) {
        ensureDirectory(outPath);

        // Ensure exactly one configuration.
        Assert.areEqual(project.configurations.size(), 1);
        PlcConfiguration config = project.configurations.get(0);

        // Ensure exactly one resource.
        Assert.areEqual(config.resources.size(), 1);
        PlcResource resource = config.resources.get(0);

        // Write tag tables for the global variables, treating the TIMERS table as a special case.
        for (PlcGlobalVarList globalVarList: resource.globalVarLists) {
            if (globalVarList.variables.isEmpty()) {
                continue;
            }

            // Write the non-empty variable list.
            if (globalVarList.listKind == PlcVarListKind.TIMERS) {
                writeTimers(globalVarList.variables, outPath);
            } else {
                writeGlobalVarList(globalVarList, outPath);
            }
        }

        // Resource task and POU instances are not written as they cannot be imported in S7.

        // Write POUs.
        for (PlcPou pou: project.pous) {
            write(pou, outPath);
        }

        // Write DB for the main program.
        int programCount = 0;
        for (PlcPou pou: project.pous) {
            if (pou.pouType == PROGRAM) {
                programCount++;

                if (!pou.localVars.isEmpty()) {
                    writeDatabase(pou.localVars, outPath);
                }
            }
        }

        // Ensure exactly one program.
        Assert.areEqual(programCount, 1);

        // Write declared types.
        for (PlcDeclaredType declaredType: project.declaredTypes) {
            if (declaredType instanceof PlcStructType structType) {
                writeDeclaredType(structType, outPath);
            } else if (declaredType instanceof PlcEnumType enumType) {
                writeDeclaredType(enumType, outPath);
            } else {
                throw new AssertionError("Unexpected declared type found: \"" + declaredType + "\".");
            }
        }
    }

    /**
     * Writes the given POU to a file in S7 syntax.
     *
     * @param pou The POU to write.
     * @param outPath The absolute local file system path of the directory to which to write the file.
     */
    private void write(PlcPou pou, String outPath) {
        String path = Paths.join(outPath, pou.name + ".scl");
        Box code = toBox(pou);
        code.writeToFile(path);
    }

    /**
     * Writes timers to a database file in S7 syntax.
     *
     * @param timerVariables Timer variables to write.
     * @param outPath The absolute local file system path of the directory to which to write the file.
     */
    private void writeTimers(List<PlcBasicVariable> timerVariables, String outPath) {
        CodeBox c = new MemoryCodeBox(INDENT);

        // Use IEC timers if available, else use TON timers.
        boolean hasIecTimers = hasIecTimers();

        // Generate timer data blocks to the database.
        for (PlcBasicVariable timerVar: timerVariables) {
            // Don't let any non-TON block slip through.
            Assert.check(timerVar.type instanceof PlcDerivedType der && der.name.equals("TON"));

            // Generate the data block for the TON timer.
            c.add("DATA_BLOCK \"%s\"", timerVar.varName);
            c.add("{InstructionName := '%s';", hasIecTimers ? "IEC_TIMER" : "TON");
            c.add("LibVersion := '1.0';");
            c.add("S7_Optimized_Access := '%b' }", hasOptimizedBlockAccess());
            c.add("AUTHOR : Simatic");
            c.add("FAMILY : %s", hasIecTimers ? "IEC" : "IEC_TC");
            c.add("NAME : %s", hasIecTimers ? "IEC_TMR" : "TON");
            c.add("VERSION : 1.0");
            c.add("NON_RETAIN");
            c.add("%s", hasIecTimers ? "IEC_TIMER" : "TON");
            c.add();
            c.add("BEGIN");
            c.add();
            c.add("END_DATA_BLOCK");
            c.add();
        }

        // Write to file.
        String path = Paths.join(outPath, "timers.db");
        c.writeToFile(path);
    }

    /**
     * Whether the PLC target type supports IEC timers.
     *
     * @return Whether IEC timers are supported for the current target type.
     */
    private boolean hasIecTimers() {
        return EnumSet.of(S7_1200, S7_1500).contains(target.getTargetType());
    }

    /**
     * Writes the type declaration of the given struct type to a file in S7 syntax.
     *
     * @param structType The structure type to write.
     * @param outPath The absolute local file system path of the directory to which to write the file.
     */
    private void writeDeclaredType(PlcStructType structType, String outPath) {
        String path = Paths.join(outPath, structType.typeName + ".udt");
        Box code = toTypeDeclBox(structType);
        code.writeToFile(path);
    }

    /**
     * Writes the type declaration of the given enum type to a file in S7 syntax.
     *
     * @param enumType The enum type to write.
     * @param outPath The absolute local file system path of the directory to which to write the file.
     */
    private void writeDeclaredType(PlcEnumType enumType, String outPath) {
        String path = Paths.join(outPath, enumType.typeName + ".udt");
        Box code = toTypeDeclBox(enumType);
        code.writeToFile(path);
    }

    /**
     * Writes the given global variable list to a file in S7 syntax.
     *
     * @param gvl The global variable list to write.
     * @param outPath The absolute local file system path of the directory to which to write the file.
     */
    private void writeGlobalVarList(PlcGlobalVarList gvl, String outPath) {
        String path = Paths.join(outPath, gvl.name + ".xml");
        Box code = makeTagTable(gvl);
        code.writeToFile(path);
    }

    /**
     * Writes the given variables as a database file in S7 syntax.
     *
     * @param variables The variables to write.
     * @param outPath The absolute local file system path of the directory to which to write the file.
     */
    private void writeDatabase(List<PlcBasicVariable> variables, String outPath) {
        CodeBox c = new MemoryCodeBox(INDENT);

        // The header.
        c.add("DATA_BLOCK \"DB\"");
        c.add("{ S7_Optimized_Access := '%b' }", hasOptimizedBlockAccess());

        // The variables.
        c.indent();
        c.add("VAR");
        c.indent();
        for (PlcBasicVariable var: variables) {
            c.add("%s: %s;", var.varName, toTypeRefBox(var.type));
        }
        c.dedent();
        c.add("END_VAR");
        c.dedent();

        // Initialization of variables.
        c.add("BEGIN");
        c.indent();
        ModelTextGenerator modelTextGenerator = target.getModelTextGenerator();
        for (PlcBasicVariable var: variables) {
            if (var instanceof PlcDataVariable dataVar && dataVar.value != null) {
                c.add("%s := %s;", var.varName, modelTextGenerator.toString(dataVar.value));
            }
        }
        c.dedent();
        c.add("END_DATA_BLOCK");

        // Write to file.
        String path = Paths.join(outPath, "DB.db");
        c.writeToFile(path);
    }

    /**
     * Whether the PLC target type supports optimized block access.
     *
     * <p>
     * If {@code true}, it optimizes data storage and performance.
     * </p>
     *
     * @return Whether optimized block access is supported for the current target type.
     */
    private boolean hasOptimizedBlockAccess() {
        return EnumSet.of(S7_1200, S7_1500).contains(target.getTargetType());
    }

    @Override
    protected Box toVarDeclBox(PlcGlobalVarList globVarList) {
        throw new UnsupportedOperationException("Should not be used.");
    }

    /**
     * Convert a global variable list as a S7 tag table.
     *
     * @param globVarList Variable list to convert.
     * @return The converted variable list.
     */
    private Box makeTagTable(PlcGlobalVarList globVarList) {
        // S7 uses a 'tag table' for PLC I/O and constants. We write the tag table as an .xml file for easy importing in
        // TIA Portal.
        CodeBox c = new MemoryCodeBox(INDENT);

        // The header.
        c.add("<?xml version='1.0' encoding='utf-8'?>");
        c.add("<Tagtable name='%s'>", globVarList.name);
        c.indent();

        // The variables, either constants or input variables. 'type', 'value', 'name' and 'address' shouldn't contain
        // XML characters that need escaping (&, <, >, ' or "). We also can't have values with string type.
        if (globVarList.listKind == PlcVarListKind.CONSTANTS) {
            ModelTextGenerator modelTextGenerator = target.getModelTextGenerator();
            for (PlcBasicVariable constant: globVarList.variables) {
                PlcDataVariable dataConstant = (PlcDataVariable)constant;
                c.add("<Constant type='%s' remark='' value='%s'>%s</Constant>", toTypeRefBox(dataConstant.type),
                        modelTextGenerator.toString(dataConstant.value), dataConstant.varName);
            }
        } else {
            for (PlcBasicVariable var: globVarList.variables) {
                PlcDataVariable dataVar = (PlcDataVariable)var;
                c.add("<Tag type='%s' hmiVisible='True' hmiWriteable='False' hmiAccessible='True' retain='False' "
                        + "remark='' addr='%s'>%s</Tag>", toTypeRefBox(dataVar.type), dataVar.address, dataVar.varName);
            }
        }
        c.dedent();

        // Close tag table.
        c.add("</Tagtable>");

        return c;
    }

    @Override
    protected Box toBox(PlcPou pou) {
        CodeBox c = new MemoryCodeBox(INDENT);

        // Get the POU text, either FUNCTION for functions, or ORGANIZATION_BLOCK for the main program.
        String pouTypeText;
        switch (pou.pouType) {
            case FUNCTION:
                pouTypeText = "FUNCTION";
                break;
            case PROGRAM:
                pouTypeText = "ORGANIZATION_BLOCK";
                break;

            default:
                throw new RuntimeException("Unknown pou type: " + pou.pouType);
        }

        // Write header. The header includes the POU type, name and return type.
        String retTypeTxt = (pou.retType == null) ? "" : fmt(": %s", toTypeRefBox(pou.retType));
        c.add("%s %s%s", pouTypeText, pou.name, retTypeTxt);
        c.add("{ S7_Optimized_Access := '%b' }", hasOptimizedBlockAccess());
        c.indent();

        // Write the input variables.
        if (!pou.inputVars.isEmpty()) {
            c.add("VAR_INPUT");
            c.indent();
            for (PlcBasicVariable var: pou.inputVars) {
                c.add("%s: %s;", var.varName, toTypeRefBox(var.type));
            }
            c.dedent();
            c.add("END_VAR");
        }

        // Write the output variables.
        if (!pou.outputVars.isEmpty()) {
            // In S7 the main program cannot have output variables.
            Assert.areEqual(pou.pouType, PlcPouType.FUNCTION);

            c.add("VAR_OUTPUT");
            c.indent();
            for (PlcBasicVariable var: pou.outputVars) {
                c.add("%s: %s;", var.varName, toTypeRefBox(var.type));
            }
            c.dedent();
            c.add("END_VAR");
        }

        // Currently user-defined function blocks don't exist, so local variables in functions should be empty. The
        // local variables of the main program are persistent and get written to a DB file elsewhere.
        Assert.check(pou.pouType != PlcPouType.FUNCTION || pou.localVars.isEmpty());

        // Write the temporary variables.
        if (!pou.tempVars.isEmpty()) {
            // In IEC 61131-3, functions use VAR for their temporary variables, while programs and user-defined function
            // blocks use VAR_TEMP. With S7 however, functions use VAR_TEMP instead.
            c.add("VAR_TEMP");

            c.indent();
            for (PlcBasicVariable var: pou.tempVars) {
                c.add("%s: %s;", var.varName, toTypeRefBox(var.type));
            }
            c.dedent();
            c.add("END_VAR");
        }

        // Write the program body.
        c.dedent();
        c.add();
        c.add("BEGIN");
        c.indent();
        if (!pou.body.isEmpty()) {
            c.add(pou.body);
        }
        c.dedent();

        // Close POU.
        c.add("END_%s", pouTypeText);

        return c;
    }
}
