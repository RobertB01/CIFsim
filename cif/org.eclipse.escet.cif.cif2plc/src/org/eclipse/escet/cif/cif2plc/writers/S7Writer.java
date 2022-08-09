//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.cif2plc.writers;

import static org.eclipse.escet.cif.cif2plc.options.PlcOutputType.S7_1200;
import static org.eclipse.escet.cif.cif2plc.options.PlcOutputType.S7_1500;
import static org.eclipse.escet.cif.cif2plc.plcdata.PlcPouType.PROGRAM;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumSet;
import java.util.List;

import org.eclipse.escet.cif.cif2plc.options.PlcOutputType;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcConfiguration;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcGlobalVarList;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcPou;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcPouType;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcProject;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcResource;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcStructType;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcTypeDecl;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcVariable;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.exceptions.InputOutputException;
import org.eclipse.escet.common.box.Box;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.box.HBox;
import org.eclipse.escet.common.box.MemoryCodeBox;
import org.eclipse.escet.common.java.Assert;

/** S7 writer for S7-1500, S7-1200, S7-400 and S7-300 SIMATIC controllers. */
public class S7Writer extends OutputTypeWriter {
    /** Targeted S7 PLC output type. */
    private final PlcOutputType outputType;

    /**
     * Constructor for the {@link S7Writer} class.
     *
     * @param outputType Targeted S7 PLC type.
     */
    public S7Writer(PlcOutputType outputType) {
        this.outputType = outputType;
    }

    @Override
    public void write(PlcProject project, String outPath) {
        // Create output directory, if it doesn't exist yet.
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

        // Ensure exactly one configuration.
        Assert.areEqual(project.configurations.size(), 1);
        PlcConfiguration config = project.configurations.get(0);

        // Ensure exactly one resource.
        Assert.areEqual(config.resources.size(), 1);
        PlcResource resource = config.resources.get(0);

        // Write the PLC inputs tag table and PLC constants tag table.
        for (PlcGlobalVarList globalVarList: resource.globalVarLists) {
            if (globalVarList.name.equals("INPUTS")) {
                write(globalVarList, outPath);
            } else if (globalVarList.name.equals("CONSTS")) {
                write(globalVarList, outPath);
            } else {
                // Write the timers. S7 timers are imported via a database file.
                Assert.areEqual(globalVarList.name, "TIMERS");
                writeTimers(outPath);
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
            if (pou.pouType != PROGRAM) {
                continue;
            }
            programCount++;

            if (pou.localVars.isEmpty()) {
                continue;
            }

            write(pou.localVars, outPath);
        }

        // Ensure exactly one program.
        Assert.areEqual(programCount, 1);

        // Write type declarations.
        for (PlcTypeDecl typeDecl: project.typeDecls) {
            write(typeDecl, outPath);
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
     * Writes two timers to a database file in S7 syntax.
     *
     * @param outPath The absolute local file system path of the directory to which to write the file.
     */
    private void writeTimers(String outPath) {
        String path = Paths.join(outPath, fmt("timers.db"));
        CodeBox c = new MemoryCodeBox(INDENT);

        // Use IEC timers if available, else use TON timers.
        boolean hasIecTimers = hasIecTimers();

        // Add timer0 and timer1.
        for (int timerIdx = 0; timerIdx < 2; timerIdx++) {
            c.add("DATA_BLOCK \"timer%d\"", timerIdx);
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
        c.writeToFile(path);
    }

    /**
     * Whether the targeted PLC output type supports IEC timers.
     *
     * @return Whether IEC timers are supported for the current output type.
     */
    private boolean hasIecTimers() {
        return EnumSet.of(S7_1200, S7_1500).contains(outputType);
    }

    /**
     * Writes the given type declaration to a file in S7 syntax.
     *
     * @param typeDecl The type declaration to write.
     * @param outPath The absolute local file system path of the directory to which to write the file.
     */
    private void write(PlcTypeDecl typeDecl, String outPath) {
        String path = Paths.join(outPath, typeDecl.name + ".udt");
        Box code = toBox(typeDecl);
        code.writeToFile(path);
    }

    /**
     * Writes the given global variable list to a file in S7 syntax.
     *
     * @param gvl The global variable list to write.
     * @param outPath The absolute local file system path of the directory to which to write the file.
     */
    private void write(PlcGlobalVarList gvl, String outPath) {
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
    private void write(List<PlcVariable> variables, String outPath) {
        String path = Paths.join(outPath, "DB.db");
        CodeBox c = new MemoryCodeBox(INDENT);

        // The header.
        c.add("DATA_BLOCK \"DB\"");
        c.add("{ S7_Optimized_Access := '%b' }", hasOptimizedBlockAccess());

        // The variables.
        c.indent();
        c.add("VAR");
        c.indent();
        for (PlcVariable var: variables) {
            c.add("%s: %s;", var.name, toBox(var.type));
        }

        // For now, we add 'curTimer' manually. Normally this is part of the GlobalVariableList 'Timers'.
        // TODO: Ideally, we change this, see Issue #281.
        c.add("curTimer: INT;");

        c.dedent();
        c.add("END_VAR");
        c.dedent();

        // Initialization of variables.
        c.add("BEGIN");
        c.indent();
        for (PlcVariable var: variables) {
            if (var.value == null) {
                continue;
            }
            c.add("%s := %s;", var.name, toBox(var.value));
        }
        c.dedent();

        // Close database.
        c.add("END_DATA_BLOCK");

        // Write to file.
        c.writeToFile(path);
    }

    /**
     * Whether the targeted PLC output type supports optimized block access.
     *
     * <p>
     * If {@code true}, it optimizes data storage and performance.
     * </p>
     *
     * @return Whether optimized block access is supported for the current output type.
     */
    private boolean hasOptimizedBlockAccess() {
        return EnumSet.of(S7_1200, S7_1500).contains(outputType);
    }

    @Override
    protected Box toBox(PlcGlobalVarList globVarList) {
        Assert.fail("Should not be used.");
        return null; // Never reached.
    }

    /**
     * Write a global variable list as a S7 tag table.
     *
     * @param globVarList Variable list to write.
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
        if (globVarList.constants) {
            for (PlcVariable constant: globVarList.variables) {
                c.add("<Constant type='%s' remark='' value='%s'>%s</Constant>", toBox(constant.type),
                        toBox(constant.value), constant.name);
            }
        } else {
            for (PlcVariable var: globVarList.variables) {
                c.add("<Tag type='%s' hmiVisible='True' hmiWriteable='False' hmiAccessible='True' retain='False' "
                        + "remark='' addr='%s'>%s</Tag>", toBox(var.type), var.address, var.name);
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
        String retTypeTxt = (pou.retType == null) ? "" : fmt(": %s", toBox(pou.retType));
        c.add("%s %s%s", pouTypeText, pou.name, retTypeTxt);
        c.add("{ S7_Optimized_Access := '%b' }", hasOptimizedBlockAccess());
        c.indent();

        // Write the input variables.
        if (!pou.inputVars.isEmpty()) {
            c.add("VAR_INPUT");
            c.indent();
            for (PlcVariable var: pou.inputVars) {
                c.add("%s: %s;", var.name, toBox(var.type));
            }
            c.dedent();
            c.add("END_VAR");
        }

        // Write the local variables.
        if (!pou.localVars.isEmpty()) {
            if (pou.pouType == PlcPouType.FUNCTION) {
                // S7 doesn't have local variables in functions. Instead, all variables are temporary. They do the same
                // but are named differently. That is, after exiting the function, the values are lost. This is similar
                // to how CIF and TwinCAT handle local variables in functions.
                c.add("VAR_TEMP");
                c.indent();
                for (PlcVariable var: pou.localVars) {
                    c.add("%s: %s;", var.name, toBox(var.type));
                }
                c.dedent();
                c.add("END_VAR");
            }
            // else: Different from functions, local variables of programs are persistent and are written to a DB file.
        }

        // Write the temporary variables.
        if (!pou.tempVars.isEmpty() || !pou.outputVars.isEmpty()) {
            // Functions shouldn't have variables declared as temporary. As all variables are temporary. Function can't
            // have output variables.
            Assert.areEqual(pou.pouType, PROGRAM);

            c.add("VAR_TEMP");
            c.indent();
            for (PlcVariable var: pou.tempVars) {
                c.add("%s: %s;", var.name, toBox(var.type));
            }
            for (PlcVariable var: pou.outputVars) {
                // There should only be two output variables, timerValue0 and timerValue1. These are part of the main
                // program. In S7 the main program cannot have output variables. Hence, we add them as temporary
                // variables.
                Assert.areEqual(pou.outputVars.size(), 2);
                c.add("%s: %s;", var.name, toBox(var.type));
            }
            c.dedent();
            c.add("END_VAR");
        }

        // Write the program body.
        c.dedent();
        c.add();
        c.add("BEGIN");

        c.indent();
        c.add(pou.body);

        // Close POU.
        c.dedent();
        c.add("END_%s", pouTypeText);

        return c;
    }

    @Override
    protected Box toBox(PlcTypeDecl typeDecl) {
        CodeBox c = new MemoryCodeBox(INDENT);
        c.add("TYPE %s:", typeDecl.name);
        c.indent();
        c.add(new HBox(toBox(typeDecl.type), ";"));
        c.dedent();
        c.add("END_TYPE");
        return c;
    }

    @Override
    protected Box toBox(PlcStructType structType) {
        CodeBox c = new MemoryCodeBox(INDENT);
        c.add("STRUCT");
        c.indent();
        for (PlcVariable field: structType.fields) {
            // Only name and type, not address.
            c.add("%s: %s;", field.name, toBox(field.type));
        }
        c.dedent();
        c.add("END_STRUCT");
        return c;
    }
}
