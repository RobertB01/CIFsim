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
import org.eclipse.escet.cif.plcgen.model.declarations.PlcConfiguration;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcDataVariable;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcDeclaredType;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcGlobalVarList;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcGlobalVarList.PlcVarListKind;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcPou;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcPouType;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcProject;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcResource;
import org.eclipse.escet.cif.plcgen.model.types.PlcArrayType;
import org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType;
import org.eclipse.escet.cif.plcgen.model.types.PlcEnumType;
import org.eclipse.escet.cif.plcgen.model.types.PlcFuncBlockType;
import org.eclipse.escet.cif.plcgen.model.types.PlcStructField;
import org.eclipse.escet.cif.plcgen.model.types.PlcStructType;
import org.eclipse.escet.cif.plcgen.model.types.PlcType;
import org.eclipse.escet.cif.plcgen.targets.PlcTarget;
import org.eclipse.escet.common.box.Box;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.box.MemoryCodeBox;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.PathPair;

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
    public void write(PlcProject project, PathPair outPaths) {
        ensureDirectory(outPaths);

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
                writeTimers(globalVarList.variables, outPaths);
            } else {
                writeGlobalVarList(globalVarList, outPaths);
            }
        }

        // Resource task and POU instances are not written as they cannot be imported in S7.

        // Write POUs.
        for (PlcPou pou: project.pous) {
            write(pou, outPaths);
        }

        // Write DB for the main program.
        int programCount = 0;
        for (PlcPou pou: project.pous) {
            if (pou.pouType == PROGRAM) {
                programCount++;

                if (!pou.localVars.isEmpty()) {
                    writeDatabase(pou.localVars, outPaths);
                }
            }
        }

        // Ensure exactly one program.
        Assert.areEqual(programCount, 1);

        // Write declared types.
        for (PlcDeclaredType declaredType: project.declaredTypes) {
            if (declaredType instanceof PlcStructType structType) {
                writeDeclaredType(structType, outPaths);
            } else if (declaredType instanceof PlcEnumType enumType) {
                writeDeclaredType(enumType, outPaths);
            } else {
                throw new AssertionError("Unexpected declared type found: \"" + declaredType + "\".");
            }
        }
    }

    /**
     * Writes the given POU to a file in S7 syntax.
     *
     * @param pou The POU to write.
     * @param outPaths The relative or absolute local file system path and the absolute local file system path of the
     *     directory to which to write the file.
     */
    private void write(PlcPou pou, PathPair outPaths) {
        String fileName = pou.name + ".scl";
        Box code = toBox(pou);
        writeFile(code, outPaths, fileName);
    }

    /**
     * Writes timers to a database file in S7 syntax.
     *
     * @param timerVariables Timer variables to write.
     * @param outPaths The relative or absolute local file system path and the absolute local file system path of the
     *     directory to which to write the file.
     */
    private void writeTimers(List<PlcDataVariable> timerVariables, PathPair outPaths) {
        CodeBox c = new MemoryCodeBox(INDENT);

        // Use IEC timers if available, else use TON timers.
        boolean hasIecTimers = hasIecTimers();

        // Generate timer data blocks to the database.
        for (PlcDataVariable timerVar: timerVariables) {
            // Don't let any non-TON block slip through.
            Assert.check(timerVar.type instanceof PlcFuncBlockType blockType
                    && blockType.funcBlockDescription.typeName.equals("TON"));

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
        writeFile(c, outPaths, "timers.db");
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
     * @param outPaths The relative or absolute local file system path and the absolute local file system path of the
     *     directory to which to write the file.
     */
    private void writeDeclaredType(PlcStructType structType, PathPair outPaths) {
        String fileName = structType.typeName + ".udt";
        Box code = toTypeDeclBox(structType);
        writeFile(code, outPaths, fileName);
    }

    /**
     * Writes the type declaration of the given enum type to a file in S7 syntax.
     *
     * @param enumType The enum type to write.
     * @param outPaths The relative or absolute local file system path and the absolute local file system path of the
     *     directory to which to write the file.
     */
    private void writeDeclaredType(PlcEnumType enumType, PathPair outPaths) {
        String fileName = enumType.typeName + ".udt";
        Box code = toTypeDeclBox(enumType);
        writeFile(code, outPaths, fileName);
    }

    /**
     * Writes the given global variable list to a file in S7 syntax.
     *
     * @param gvl The global variable list to write.
     * @param outPaths The relative or absolute local file system path and the absolute local file system path of the
     *     directory to which to write the file.
     */
    private void writeGlobalVarList(PlcGlobalVarList gvl, PathPair outPaths) {
        String fileName = gvl.name + ".xml";
        Box code = makeTagTable(gvl);
        writeFile(code, outPaths, fileName);
    }

    /**
     * Writes the given variables as a database file in S7 syntax.
     *
     * @param variables The variables to write.
     * @param outPaths The relative or absolute local file system path and the absolute local file system path of the
     *     directory to which to write the file.
     */
    private void writeDatabase(List<PlcDataVariable> variables, PathPair outPaths) {
        CodeBox c = new MemoryCodeBox(INDENT);

        // The header.
        c.add("DATA_BLOCK \"DB\"");
        c.add("{ S7_Optimized_Access := '%b' }", hasOptimizedBlockAccess());

        // The variables.
        c.indent();
        writeVarTable(c, "VAR", variables, 0);
        c.dedent();

        // Initialization of variables.
        c.add("BEGIN");
        c.indent();
        ModelTextGenerator modelTextGenerator = target.getModelTextGenerator();
        for (PlcDataVariable var: variables) {
            if (var.value != null) {
                c.add("%s := %s;", var.varName, modelTextGenerator.toString(var.value));
            }
        }
        c.dedent();
        c.add("END_DATA_BLOCK");

        // Write to file.
        writeFile(c, outPaths, "DB.db");
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
            for (PlcDataVariable constant: globVarList.variables) {
                c.add("<Constant type='%s' remark='' value='%s'>%s</Constant>", toTypeRefBox(constant.type),
                        modelTextGenerator.literalToString(constant.value), constant.varName);
            }
        } else {
            for (PlcDataVariable var: globVarList.variables) {
                c.add("<Tag type='%s' hmiVisible='True' hmiWriteable='False' hmiAccessible='True' retain='False' "
                        + "remark='' addr='%s'>%s</Tag>", toTypeRefBox(var.type), var.address, var.varName);
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
            writeVarTable(c, "VAR_INPUT", pou.inputVars, 0);
        }

        // Write the input/output variables.
        if (!pou.inOutVars.isEmpty()) {
            writeVarTable(c, "VAR_IN_OUT", pou.inOutVars, 0);
        }

        // Write the output variables.
        if (!pou.outputVars.isEmpty()) {
            // In S7 the main program cannot have output variables.
            Assert.areEqual(pou.pouType, PlcPouType.FUNCTION);

            writeVarTable(c, "VAR_OUTPUT", pou.outputVars, 0);
        }

        // Currently user-defined function blocks don't exist, so local variables in functions should be empty. The
        // local variables of the main program are persistent and get written to a DB file elsewhere.
        Assert.check(pou.pouType != PlcPouType.FUNCTION || pou.localVars.isEmpty());

        // Write the temporary variables.
        if (!pou.tempVars.isEmpty()) {
            // In IEC 61131-3, functions use VAR for their temporary variables, while programs and user-defined function
            // blocks use VAR_TEMP. With S7 however, functions use VAR_TEMP instead.
            //
            // S7-300 needs to have 20 bytes in the variable table, or it reports:
            // "The interface of the standard OB is smaller than the minimum value of 20 bytes".
            writeVarTable(c, "VAR_TEMP", pou.tempVars, 20);
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

    /**
     * Generate a variable table.
     *
     * @param c Text storage of the table.
     * @param headerText Header to use above the table.
     * @param variables Variables to add to the table.
     * @param minByteSize Minimum size of the variable table (the sum of the sizes of the added variables) in bytes.
     */
    private void writeVarTable(CodeBox c, String headerText, List<PlcDataVariable> variables, int minByteSize) {
        PlcElementaryType typeOfDummies = PlcElementaryType.DINT_TYPE;
        Assert.check(typeOfDummies.bitSize > 0);

        // Temporary variables needs a block size of at least 20 bytes.
        // There are variables "dummyVar[1,5]" declared as reserved words in S7, for this purpose. The DINT type is
        // sufficient to do this.
        Assert.check(minByteSize == 0 || headerText.equals("VAR_TEMP"));
        Assert.check(minByteSize <= 20);

        // Over-approximation of the number of bits still needed to reach minimum variable table size.
        int remainingBitSize = minByteSize * 8;

        c.add(headerText);

        c.indent();
        for (PlcDataVariable var: variables) {
            c.add("%s: %s;", var.varName, toTypeRefBox(var.type));
            remainingBitSize -= guessTypeSize(var.type); // Update remaining table size for as far as known.
        }

        // If the table may be too small, add dummy variables.
        for (int dummyNum = 1; remainingBitSize > 0 && dummyNum < 6; dummyNum++) {
            String dummyName = "dummyVar" + dummyNum;
            c.add("%s: %s;", dummyName, toTypeRefBox(typeOfDummies));
            remainingBitSize -= typeOfDummies.bitSize;
        }

        c.dedent();
        c.add("END_VAR");
    }

    /**
     * Estimate a lower bound for values of the given type.
     *
     * @param type Type to analyze.
     * @return Estimated size in bits for a value of the given type.
     */
    private static int guessTypeSize(PlcType type) {
        if (type instanceof PlcElementaryType elemType) {
            return elemType.bitSize;
        } else if (type instanceof PlcStructType strType) {
            int totalSize = 0;
            for (PlcStructField field: strType.fields) {
                totalSize += guessTypeSize(field.type);
            }
            return totalSize;
        } else if (type instanceof PlcArrayType arrType) {
            int elementSize = guessTypeSize(arrType.elemType);
            return elementSize * (arrType.upper - arrType.lower + 1);
        }
        return 0; // Unknown size.
    }
}
