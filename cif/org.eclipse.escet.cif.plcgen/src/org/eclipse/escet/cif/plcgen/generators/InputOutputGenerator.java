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
import java.util.List;
import java.util.Set;

import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.cif.plcgen.PlcGenSettings;
import org.eclipse.escet.cif.plcgen.WarnOutput;
import org.eclipse.escet.cif.plcgen.generators.io.IoAddress;
import org.eclipse.escet.cif.plcgen.generators.io.IoEntry;
import org.eclipse.escet.cif.plcgen.generators.io.IoKind;
import org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType;
import org.eclipse.escet.cif.plcgen.model.types.PlcType;
import org.eclipse.escet.cif.plcgen.targets.PlcTarget;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.exceptions.InputOutputException;
import org.eclipse.escet.common.app.framework.exceptions.InvalidInputException;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.CsvParser;
import org.eclipse.escet.common.java.Pair;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** Generator that creates input and output PLC code. */
public class InputOutputGenerator {
    /** Plc types that may be used for IO. */
    private static final Set<PlcElementaryType> FEASIBLE_IO_VAR_TYPES = set(BOOL_TYPE, INT_TYPE, DINT_TYPE, LINT_TYPE,
            REAL_TYPE, LREAL_TYPE);

    /** PLC target to generate code for. */
    private final PlcTarget target;

    /** File path to the IO table file. File may not exist. */
    private final String ioTablePath;

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
        warnOutput = settings.warnOutput;
    }

    /** Generate input/output code for communicating with the world outside the PLC. */
    public void process() {
        List<IoEntry> entries = convertIoTableEntries();
    }

    /**
     * Load the IO table from the table file path, process each line and collect the found entries.
     *
     * <p>
     * Table format: Each line is an input or an output thjat connects a CIF variable to an input or output port.
     *
     * <ul>
     * <li>First field contains the PLC IO address. Address syntax may vary between PLCs.</li>
     * <li>Second field contains the PLC type. If empty, the CIF type of the variable is used instead.</li>
     * <li>Third field contains the absolute name of the CIF variable to connect to the input or output in non-escaped
     * notation.</li>
     * </ul>
     * </p>
     *
     * @return The collected entries of the IO table, list is empty if no table file was found.
     */
    private List<IoEntry> convertIoTableEntries() {
        try (BufferedReader ioTableText = new BufferedReader(new FileReader(Paths.resolve(ioTablePath)))) {
            Set<PositionObject> connectedInputCifObjects = set(); // Used CIF objects for input.
            Set<IoAddress> connectedPlcAddresses = set(); // used PLC addresses for output.

            // Read the IO table file, process each line, and store the entries
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
                String tableLinePositionText = fmt("at line %d of io table file \"%s\"", lineNumber, ioTablePath);

                // Check the line length of the CSV parser.
                if (line.size() != 3) {
                    String message = fmt("Incorrect number of fields (expected 3 fields, found %d) %s.", line.size(),
                            tableLinePositionText);
                    throw new InvalidInputException(message);
                }

                // Second field, the PLC type, may be empty. If empty the CIF type is used instead.
                String plcTableTypeText = line.get(1).trim();
                PlcType plcTableType = checkIoType(plcTableTypeText, tableLinePositionText);

                // Third field, the CIF object path to connect to the IO address.
                String cifNamePath = line.get(2).trim();
                Pair<String, PositionObject> matched = target.getCifProcessor().findCifObjectByPath(cifNamePath);

                // Verify the returned CIF object.
                PlcType plcTypeFromCif = decideTypeFromCif(cifNamePath, matched, tableLinePositionText);
                IoKind kindFromCif = decideIoDirectionFromCif(matched.right);

                // Don't allow 2 uses for input with the same CIF object.
                if (kindFromCif.equals(IoKind.INPUT)) {
                    if (connectedInputCifObjects.contains(matched.right)) {
                        String message = fmt(
                                "The CIF object is already in use for receiving another value from input %s.",
                                tableLinePositionText);
                        throw new InvalidInputException(message);
                    }
                    connectedInputCifObjects.add(matched.right);
                }

                // Check that the type from CIF does not conflict with the PLC table type and settle on the final type.
                plcTableType = decidePlcType(plcTableType, cifNamePath, plcTypeFromCif, tableLinePositionText);

                // First field, the IO address. Convert to the parsed form.
                String plcAddressText = line.get(0).trim();
                if (plcAddressText.isEmpty()) {
                    String message = fmt("The 'address' entry is empty (first field %s).", tableLinePositionText);
                    throw new InvalidInputException(message);
                }
                IoAddress plcAddress = target.parseIoAddress(plcAddressText);

                // Check for output conflicts (multiple uses of a PLC output)/
                if (kindFromCif.equals(IoKind.OUTPUT)) {
                    if (connectedPlcAddresses.contains(plcAddress)) {
                        String message = fmt(
                                "The PLC address is already in use for receiving another value to output %s.",
                                tableLinePositionText);
                        throw new InvalidInputException(message);
                    }
                    connectedPlcAddresses.add(plcAddress);
                }

                // Verify with the target whether the configured IO table data makes sense.
                target.verifyIoTableEntry(plcAddress, plcTableType, kindFromCif, tableLinePositionText);

                IoEntry entry = new IoEntry(plcAddress, plcTableType, matched.right, kindFromCif);
                entries.add(entry);
            }
            return entries;
        } catch (FileNotFoundException ex) {
            // File does not exist, don't generate IO handling.
            warnOutput.warn("IO table file \"%s\" not found, PLC code will not perform any IO with the environment.",
                    ioTablePath);
            return List.of();
        } catch (IOException ex) {
            throw new InputOutputException("Failed to close the IO table file \"" + ioTablePath + "\".", ex);
        }
    }

    /**
     * Check the IO type field from a CSV line.
     *
     * @param plcTableTypeText Text of of the second field of the CSV line, may be empty.
     * @param tableLinePositionText Text for reporting about the CSV line.
     * @return The derived PLC type of the type field, or {@code null} if the field was empty.
     */
    private PlcType checkIoType(String plcTableTypeText, String tableLinePositionText) {
        PlcType plcTableType;
        if (!plcTableTypeText.isEmpty()) {
            plcTableType = getIoVarType(plcTableTypeText);
            if (plcTableType == null) {
                String message = fmt(
                        "The 'plc type' field containing \"%s\" is not usable type for input/output (second field %s).",
                        plcTableTypeText, tableLinePositionText);
                throw new InvalidInputException(message);
            }
        } else {
            plcTableType = null; // Try to derive the type from the CIF name below.
        }
        return plcTableType;
    }

    /**
     * Derive a PLC type for an entry from its attached CIF object.
     *
     * @param cifNamePath Name of the object in CIF.
     * @param matched Result of the search in CIF.
     * @param tableLinePositionText Text for reporting about the CSV line.
     * @return The PLC type to use for the IO entry according to the matched CIF object.
     * @throws InvalidInputException If no valid CIF object can be attached to the provided search result.
     */
    private PlcType decideTypeFromCif(String cifNamePath, Pair<String, PositionObject> matched,
            String tableLinePositionText)
    {
        if (!cifNamePath.equals(matched.left)) { // Partial path match.
            String message = fmt("The 'cif name' entry containing \"%s\" could not be fully matched (third field %s).",
                    cifNamePath, tableLinePositionText);
            throw new InvalidInputException(message);
        } else if (matched.right instanceof DiscVariable dv) {
            return target.getTypeGenerator().convertType(dv.getType());
        } else if (matched.right instanceof InputVariable iv) {
            return target.getTypeGenerator().convertType(iv.getType());
        } else {
            String message = fmt("The 'cif name' entry containing \"%s\" does not indicate an input or discrete "
                    + "variable (third field %s).", cifNamePath, tableLinePositionText);
            throw new InvalidInputException(message);
        }
    }

    /**
     * Obtain the IO direction of the IO entry from the attached CIF object.
     *
     * @param posObject Attached CIF object to the entry.
     * @return Direction of IO for the entry in the PLC.
     */
    private IoKind decideIoDirectionFromCif(PositionObject posObject) {
        if (posObject instanceof DiscVariable) {
            return IoKind.OUTPUT;
        } else if (posObject instanceof InputVariable) {
            return IoKind.INPUT;
        } else {
            throw new AssertionError("Unexpected CIF object \"" + posObject + "\".");
        }
    }

    /**
     * Make the final decision about the PLC type of the IO value.
     *
     * @param plcTableType PLC type stated in the CSV file, may be {@code null}.
     * @param cifNamePath CIF object connected to the IO address.
     * @param plcTypeFromCif PLC type related to the CIF object.
     * @param tableLinePositionText Text for reporting about the CSV line.
     * @return Check or assign the PLC type to use for the IO entry.
     */
    private PlcType decidePlcType(PlcType plcTableType, String cifNamePath, PlcType plcTypeFromCif,
            String tableLinePositionText)
    {
        Assert.notNull(plcTypeFromCif); // This is assumed in the next code block.
        if (plcTableType != null) {
            if (!plcTableType.equals(plcTypeFromCif)) {
                String message = fmt(
                        "The type stated in the 'plc type' field does not correspond with the type "
                                + "of the connected CIF element from the 'cif name' entry containing \"%s\" "
                                + "does not indicate an input or discrete variable (third field %s).",
                        cifNamePath, tableLinePositionText);
                throw new InvalidInputException(message);
            }
            // TODO Allow for a different sized type, like DINT <-> INT, REAL <-> LREAL, etc.
        } else {
            // Verify that the CIF type is a feasible type before selecting it.
            if (!FEASIBLE_IO_VAR_TYPES.contains(plcTypeFromCif)) {
                String message = fmt(
                        "The type of the variable indicated in \"%s\" 'cif name' field is not usable type for "
                                + "input/output (third field %s).",
                        cifNamePath, tableLinePositionText);
                throw new InvalidInputException(message);
            }
            plcTableType = plcTypeFromCif; // Use found CIF type as the IO table type.
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
}
