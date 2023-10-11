//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.plcgen.generators;

import static org.eclipse.escet.cif.common.CifTextUtils.getAbsName;
import static org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType.BOOL_TYPE;
import static org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType.DINT_TYPE;
import static org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType.INT_TYPE;
import static org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType.LINT_TYPE;
import static org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType.LREAL_TYPE;
import static org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType.REAL_TYPE;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.cif.plcgen.PlcGenSettings;
import org.eclipse.escet.cif.plcgen.conversion.expressions.CifDataProvider;
import org.eclipse.escet.cif.plcgen.generators.io.IoAddress;
import org.eclipse.escet.cif.plcgen.generators.io.IoDirection;
import org.eclipse.escet.cif.plcgen.generators.io.IoEntry;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcVariable;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcExpression;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcVarExpression;
import org.eclipse.escet.cif.plcgen.model.statements.PlcAssignmentStatement;
import org.eclipse.escet.cif.plcgen.model.statements.PlcStatement;
import org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType;
import org.eclipse.escet.cif.plcgen.model.types.PlcType;
import org.eclipse.escet.cif.plcgen.targets.PlcTarget;
import org.eclipse.escet.common.app.framework.exceptions.InputOutputException;
import org.eclipse.escet.common.app.framework.exceptions.InvalidInputException;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.CsvParser;
import org.eclipse.escet.common.java.output.WarnOutput;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** Generator that creates input and output PLC code. */
public class InputOutputGenerator {
    /** PLC types that may be used for I/O. */
    private static final Set<PlcElementaryType> FEASIBLE_IO_VAR_TYPES = set(BOOL_TYPE, INT_TYPE, DINT_TYPE, LINT_TYPE,
            REAL_TYPE, LREAL_TYPE);

    /** PLC target to generate code for. */
    private final PlcTarget target;

    /** User-provided file path to the I/O table file. File may not exist. */
    private final String ioTablePath;

    /** Absolute file path to the I/O table file. File may not exist. */
    private final String absIoTablePath;

    /** Callback to send warnings to the user. */
    private final WarnOutput warnOutput;

    /**
     * Constructor of the {@link InputOutputGenerator} class.
     *
     * @param target PLC target to generate code for.
     * @param settings Configuration to use.
     */
    public InputOutputGenerator(PlcTarget target, PlcGenSettings settings) {
        this.target = target;
        ioTablePath = settings.ioTablePath;
        absIoTablePath = settings.absIoTablePath;
        warnOutput = settings.warnOutput;
    }

    /** Generate input/output code for communicating with the world outside the PLC. */
    public void process() {
        List<IoEntry> entries = convertIoTableEntries();
        generateIoCode(entries);
    }

    /**
     * Load the I/O table from the table file, process each line and collect the found entries.
     *
     * <p>
     * Table format: Each line is an input or an output that connects a CIF variable to an input or output port.
     *
     * <ul>
     * <li>First field contains the PLC I/O address. Address syntax may vary between PLCs.</li>
     * <li>Second field contains the PLC type. If empty, the CIF type of the variable is used instead.</li>
     * <li>Third field contains the absolute name of the CIF variable to connect to the input or output in non-escaped
     * notation.</li>
     * </ul>
     * </p>
     *
     * @return The collected entries of the I/O table, list is empty if no table file was found.
     */
    private List<IoEntry> convertIoTableEntries() {
        try (BufferedReader ioTableText = new BufferedReader(new FileReader(absIoTablePath))) {
            Set<PositionObject> connectedInputCifObjects = set(); // Used CIF objects for input.
            Set<IoAddress> connectedPlcAddresses = set(); // Used PLC addresses for output.

            // Read the I/O table file, process each line, and store the entries.
            List<IoEntry> entries = list();

            CsvParser parser = new CsvParser(ioTableText);
            int lineNumber = 0;
            while (true) {
                lineNumber++;

                List<String> line = parser.getLine();
                if (line == null) {
                    break;
                }

                // Text stating the CSV line being processed, for reporting any error.
                String tableLinePositionText = fmt("at line %d of I/O table file \"%s\"", lineNumber, ioTablePath);

                // Check the line length of the CSV parser.
                if (line.size() != 3) {
                    String message = fmt("Incorrect number of fields (expected 3 fields, found %d) %s.", line.size(),
                            tableLinePositionText);
                    throw new InvalidInputException(message);
                }

                // Second field, the PLC type, may be empty. If empty the CIF type is used instead.
                String plcTableTypeText = line.get(1).trim();
                PlcType plcTableType = checkIoType(plcTableTypeText, tableLinePositionText);

                // Third field, the absolute name of the CIF object to connect to the I/O address.
                String absCifName = line.get(2).trim();
                PositionObject cifObj;
                try {
                    cifObj = target.getCifProcessor().findCifObjectByAbsName(absCifName);
                } catch (IllegalArgumentException ex) {
                    String message = fmt(
                            "The 'CIF name' field containing \"%s\" does not refer to an object in the CIF "
                                    + "specification (third field %s).",
                            absCifName, tableLinePositionText);
                    throw new InvalidInputException(message, ex);
                }

                // Verify the returned CIF object.
                PlcType plcTypeFromCif = decideTypeFromCif(absCifName, cifObj, tableLinePositionText);
                IoDirection directionFromCif = decideIoDirectionFromCif(cifObj);

                // Don't allow 2 uses for input with the same CIF object.
                if (directionFromCif.equals(IoDirection.IO_READ)) {
                    if (connectedInputCifObjects.contains(cifObj)) {
                        String message = fmt(
                                "The CIF variable for entry %s is already in use for receiving a value from an input, "
                                        + "as specified by an earlier I/O table entry.",
                                tableLinePositionText);
                        throw new InvalidInputException(message);
                    }
                    connectedInputCifObjects.add(cifObj);
                }

                // Check that the type from CIF does not conflict with the PLC table type and settle on the final type.
                plcTableType = decidePlcType(plcTableType, absCifName, plcTypeFromCif, tableLinePositionText);

                // First field, the I/O address. Convert to the parsed form.
                String plcAddressText = line.get(0).trim();
                if (plcAddressText.isEmpty()) {
                    String message = fmt("The 'address' field is empty (first field %s).", tableLinePositionText);
                    throw new InvalidInputException(message);
                }
                IoAddress plcAddress = target.parseIoAddress(plcAddressText);
                if (plcAddress == null) {
                    String message = fmt("The 'address' field does not have a correct form (first field %s).",
                            tableLinePositionText);
                    throw new InvalidInputException(message);
                }

                // Check for output conflicts (multiple uses of a PLC output).
                if (directionFromCif.equals(IoDirection.IO_WRITE)) {
                    if (connectedPlcAddresses.contains(plcAddress)) {
                        String message = fmt(
                                "The PLC address for the entry %s is already in use for outputting a value, as "
                                        + "specified by an earlier I/O table entry.",
                                tableLinePositionText);
                        throw new InvalidInputException(message);
                    }
                    connectedPlcAddresses.add(plcAddress);
                }

                // Verify with the target whether the configured I/O table data makes sense.
                target.verifyIoTableEntry(plcAddress, plcTableType, directionFromCif, tableLinePositionText);

                IoEntry entry = new IoEntry(plcAddress, plcTableType, cifObj, directionFromCif);
                entries.add(entry);
            }
            return entries;
        } catch (FileNotFoundException ex) {
            // File does not exist, don't generate I/O handling.
            warnOutput.line(
                    "I/O table file \"%s\" not found. The PLC code will not perform any I/O with the environment.",
                    ioTablePath);
            return List.of();
        } catch (IOException ex) {
            throw new InputOutputException("Failed to read I/O table file \"" + ioTablePath + "\".", ex);
        }
    }

    /**
     * Check the I/O type field from a CSV line.
     *
     * @param plcTableTypeText Text of the second field of the CSV line, may be empty.
     * @param tableLinePositionText Text for reporting about the CSV line.
     * @return The derived PLC type of the type field, or {@code null} if the field was empty.
     */
    private PlcType checkIoType(String plcTableTypeText, String tableLinePositionText) {
        PlcType plcTableType;
        if (!plcTableTypeText.isEmpty()) {
            plcTableType = getIoVarType(plcTableTypeText);
            if (plcTableType == null) {
                String message = fmt(
                        "Type \"%s\" contained in the 'PLC type' field is not a usable type for input/output (second "
                                + "field %s).",
                        plcTableTypeText, tableLinePositionText);
                throw new InvalidInputException(message);
            }
        } else {
            plcTableType = null;
        }
        return plcTableType;
    }

    /**
     * Derive a PLC type for an entry from its attached CIF object.
     *
     * @param absName Absolute name of the object in CIF.
     * @param cifObj The CIF object found from the absolute name in the specification.
     * @param tableLinePositionText Text for reporting about the CSV line.
     * @return The PLC type to use for the I/O entry according to the matched CIF object.
     * @throws InvalidInputException If no valid CIF object can be attached to the provided search result.
     */
    private PlcType decideTypeFromCif(String absName, PositionObject cifObj, String tableLinePositionText) {
        if (cifObj instanceof DiscVariable dv) {
            return target.getTypeGenerator().convertType(dv.getType());
        } else if (cifObj instanceof InputVariable iv) {
            return target.getTypeGenerator().convertType(iv.getType());
        } else {
            String message = fmt("The 'CIF name' field containing \"%s\" does not indicate an input or discrete "
                    + "variable (third field %s).", absName, tableLinePositionText);
            throw new InvalidInputException(message);
        }
    }

    /**
     * Obtain the I/O direction of the I/O from the CIF object associated with the I/O entry.
     *
     * @param posObject CIF object attached to the entry.
     * @return Direction of I/O for the entry in the PLC.
     */
    private IoDirection decideIoDirectionFromCif(PositionObject posObject) {
        if (posObject instanceof DiscVariable) {
            return IoDirection.IO_WRITE;
        } else if (posObject instanceof InputVariable) {
            return IoDirection.IO_READ;
        } else {
            throw new AssertionError("Unexpected CIF object \"" + posObject + "\".");
        }
    }

    /**
     * Make the final decision about the PLC type of the I/O value.
     *
     * @param plcTableType PLC type stated in the CSV file, may be {@code null}.
     * @param absCifName Absolute name of the CIF object connected to the I/O address.
     * @param plcTypeFromCif PLC type related to the CIF object.
     * @param tableLinePositionText Text for reporting about the CSV line.
     * @return The final PLC type to use for the I/O entry.
     */
    private PlcType decidePlcType(PlcType plcTableType, String absCifName, PlcType plcTypeFromCif,
            String tableLinePositionText)
    {
        Assert.notNull(plcTypeFromCif); // This is assumed in the next code block.
        if (plcTableType != null) {
            if (!plcTableType.equals(plcTypeFromCif)) {
                String message = fmt("The type stated in the 'PLC type' field does not correspond with the type "
                        + "of the connected CIF variable from the 'CIF name' field containing \"%s\", "
                        + "for the entry %s.", absCifName, tableLinePositionText);
                throw new InvalidInputException(message);
            }
            // TODO Allow for a different sized type, like DINT <-> INT, REAL <-> LREAL, etc.
        } else {
            // Verify that the CIF type is a feasible type before selecting it.
            if (!FEASIBLE_IO_VAR_TYPES.contains(plcTypeFromCif)) {
                String message = fmt(
                        "The type of CIF variable \"%s\" is not a usable type for input/output (third field %s).",
                        absCifName, tableLinePositionText);
                throw new InvalidInputException(message);
            }
            plcTableType = plcTypeFromCif; // Use found CIF type as the I/O table type.
        }
        return plcTableType;
    }

    /**
     * Get the PLC type to use from its name.
     *
     * @param typeName Name of the type to use.
     * @return The requested type or {@code null} if the type is not available.
     */
    private PlcType getIoVarType(String typeName) {
        for (PlcElementaryType varType: FEASIBLE_IO_VAR_TYPES) {
            if (typeName.equalsIgnoreCase(varType.name)) {
                return varType;
            }
        }
        return null;
    }

    /**
     * Generate I/O variables and input/output function code for transferring values between input I/O, the CIF state,
     * and output I/O.
     *
     * @param entries I/O entries to use.
     */
    private void generateIoCode(List<IoEntry> entries) {
        List<PlcStatement> inputStats = list();
        List<PlcStatement> outputStats = list();

        NameGenerator nameGenerator = target.getNameGenerator();
        PlcCodeStorage codeStorage = target.getCodeStorage();

        CifDataProvider dataProvider = codeStorage.getExprGenerator().getScopeCifDataProvider();
        for (IoEntry entry: entries) {
            // Preliminaries (check I/O direction, construct links to the correct local data structures).
            Assert.check(EnumSet.of(IoDirection.IO_READ, IoDirection.IO_WRITE).contains(entry.ioDirection));
            boolean isInput = entry.ioDirection.equals(IoDirection.IO_READ);
            List<PlcStatement> stats = isInput ? inputStats : outputStats;

            // Construct a variable with the I/O address.
            String varPrefix = isInput ? "in__" : "out__";
            String ioVarName = varPrefix + getAbsName(entry.cifObject, false);
            ioVarName = nameGenerator.generateGlobalName(ioVarName, false);
            PlcVariable ioVar = new PlcVariable(ioVarName, entry.varType, entry.plcAddress.getAddress(), null);
            if (isInput) {
                codeStorage.addInputVariable(ioVar);
            } else {
                codeStorage.addOutputVariable(ioVar);
            }

            // Construct the assignment to perform the I/O.
            if (isInput) { // state-var := io-var;
                PlcVarExpression leftSide;
                if (entry.cifObject instanceof DiscVariable discVar) {
                    leftSide = dataProvider.getAddressableForDiscVar(discVar);
                } else if (entry.cifObject instanceof InputVariable inpVar) {
                    leftSide = dataProvider.getAddressableForInputVar(inpVar);
                } else {
                    throw new AssertionError("Unexpected state variable found: " + entry.cifObject);
                }

                PlcExpression rightSide = new PlcVarExpression(ioVar);
                stats.add(new PlcAssignmentStatement(leftSide, rightSide));
            } else { // io-var := state-var;
                PlcVarExpression leftSide = new PlcVarExpression(ioVar);

                PlcExpression rightSide;
                if (entry.cifObject instanceof DiscVariable discVar) {
                    rightSide = dataProvider.getValueForDiscVar(discVar);
                } else if (entry.cifObject instanceof InputVariable inpVar) {
                    rightSide = dataProvider.getValueForInputVar(inpVar);
                } else {
                    throw new AssertionError("Unexpected state variable found: " + entry.cifObject);
                }
                stats.add(new PlcAssignmentStatement(leftSide, rightSide));
            }
        }
        if (!inputStats.isEmpty()) {
            codeStorage.addInputFuncCode(inputStats);
        }
        if (!outputStats.isEmpty()) {
            codeStorage.addOutputFuncCode(outputStats);
        }
    }
}
