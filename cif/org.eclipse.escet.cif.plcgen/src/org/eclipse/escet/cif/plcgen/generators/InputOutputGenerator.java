//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023, 2024 Contributors to the Eclipse Foundation
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
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newAlgVariableExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newDiscVariableExpression;
import static org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType.BOOL_TYPE;
import static org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType.DINT_TYPE;
import static org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType.INT_TYPE;
import static org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType.LINT_TYPE;
import static org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType.LREAL_TYPE;
import static org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType.REAL_TYPE;
import static org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType.isIntType;
import static org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType.isRealType;
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

import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.plcgen.PlcGenSettings;
import org.eclipse.escet.cif.plcgen.conversion.PlcFunctionAppls;
import org.eclipse.escet.cif.plcgen.conversion.expressions.CifDataProvider;
import org.eclipse.escet.cif.plcgen.conversion.expressions.ExprGenerator;
import org.eclipse.escet.cif.plcgen.conversion.expressions.ExprValueResult;
import org.eclipse.escet.cif.plcgen.generators.CifProcessor.CifObjectFinder;
import org.eclipse.escet.cif.plcgen.generators.io.IoAddress;
import org.eclipse.escet.cif.plcgen.generators.io.IoDirection;
import org.eclipse.escet.cif.plcgen.generators.io.IoEntry;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcDataVariable;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcExpression;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcVarExpression;
import org.eclipse.escet.cif.plcgen.model.statements.PlcAssignmentStatement;
import org.eclipse.escet.cif.plcgen.model.statements.PlcCommentLine;
import org.eclipse.escet.cif.plcgen.model.statements.PlcStatement;
import org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType;
import org.eclipse.escet.cif.plcgen.model.types.PlcType;
import org.eclipse.escet.cif.plcgen.targets.PlcTarget;
import org.eclipse.escet.common.emf.EMFHelper;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.CsvParser;
import org.eclipse.escet.common.java.PathPair;
import org.eclipse.escet.common.java.exceptions.InputOutputException;
import org.eclipse.escet.common.java.exceptions.InvalidInputException;
import org.eclipse.escet.common.java.output.WarnOutput;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** Generator that creates input and output PLC code. */
public class InputOutputGenerator {
    /** CSV-reader column number of the PLC I/O address text. */
    private static final int PLC_IO_ADDRESS_COLUMN = 0;

    /** CSV-reader column number of the PLC type of the I/O data. */
    private static final int PLC_TYPE_COLUMN = 1;

    /** CSV-reader column number of the absolute name of the CIF object related to the I/O address. */
    private static final int ABS_CIF_NAME_COLUMN = 2;

    /** Name of the I/O variable at the I/O address. */
    private static final int IO_NAME_COLUMN = 3;

    /**
     * PLC types that may be used for I/O.
     *
     * <p>
     * These types should be printable, see {@link #getNameOfPlcIoType}.
     * </p>
     */
    private static final Set<PlcElementaryType> FEASIBLE_IO_VAR_TYPES = set(BOOL_TYPE, INT_TYPE, DINT_TYPE, LINT_TYPE,
            REAL_TYPE, LREAL_TYPE);

    /** PLC target to generate code for. */
    private final PlcTarget target;

    /** Paths to the I/O table file, may not exist. */
    private final PathPair ioTablePaths;

    /** Callback to send warnings to the user. */
    private final WarnOutput warnOutput;

    /**
     * If {@code null}, CSV file loading has not been attempted yet. Otherwise, the parsed CSV file lines (empty list if
     * the file does not exist or is empty).
     */
    private List<List<String>> csvLines = null;

    /**
     * Constructor of the {@link InputOutputGenerator} class.
     *
     * @param target PLC target to generate code for.
     * @param settings Configuration to use.
     */
    public InputOutputGenerator(PlcTarget target, PlcGenSettings settings) {
        this.target = target;
        ioTablePaths = settings.ioTablePaths;
        warnOutput = settings.warnOutput;
    }

    /**
     * Retrieve the I/O names that may conflict with other names.
     *
     * @return The custom I/O names that may conflict with other names.
     */
    public Set<String> getCustomIoNames() {
        Set<String> result = set();
        for (List<String> line: getCsvLines()) {
            if (line.size() > IO_NAME_COLUMN) {
                String ioName = line.get(IO_NAME_COLUMN);
                if (!target.checkIoVariableName(ioName)) {
                    continue; // This name will never be used.
                }

                // Add the name in various forms.
                result.add(ioName);
                result.add(target.getUsageVariableText(PlcVariablePurpose.INPUT_VAR, ioName));
                result.add(target.getUsageVariableText(PlcVariablePurpose.OUTPUT_VAR, ioName));
            }
        }
        return result;
    }

    /**
     * Generate input/output code for communicating with the world outside the PLC.
     *
     * @param cifObjectFinder Finder to get CIF objects from the input specification from their absolute name.
     */
    public void process(CifObjectFinder cifObjectFinder) {
        List<IoEntry> entries = convertIoTableEntries(cifObjectFinder);
        generateIoCode(entries);
    }

    /**
     * Get the CSV file lines.
     *
     * @return Lines of the CSV file, or the empty list if the file is not available.
     */
    private List<List<String>> getCsvLines() {
        if (csvLines != null) {
            return csvLines; // Already attempted to load the CSV file.
        }

        // Read the I/O table file.
        csvLines = list();
        try (BufferedReader ioTableText = new BufferedReader(new FileReader(ioTablePaths.systemPath))) {
            CsvParser parser = new CsvParser(ioTableText);
            while (true) {
                List<String> line = parser.getLine();
                if (line == null) {
                    break;
                }
                csvLines.add(line);
            }
        } catch (FileNotFoundException ex) {
            // File does not exist, don't generate I/O handling.
            warnOutput.line(
                    "I/O table file \"%s\" not found. The PLC code will not perform any I/O with the environment.",
                    ioTablePaths.userPath);
        } catch (IOException ex) {
            throw new InputOutputException("Failed to read I/O table file \"" + ioTablePaths.userPath + "\".", ex);
        }
        return csvLines;
    }

    /**
     * Get the CSV table file and process each line to construct the I/O table..
     *
     * <p>
     * Table format: Each line is an input or an output that connects a CIF variable to an input or output port.
     *
     * <ul>
     * <li>First field contains the PLC I/O address. Address syntax may vary between PLCs.</li>
     * <li>Second field may contain the PLC type. If empty, the CIF type of the variable is used instead.</li>
     * <li>Third field contains the absolute name of the CIF variable to connect to the input or output in non-escaped
     * notation.</li>
     * <li>Fourth field may contain the name of the I/O variable. If empty, a name is derived from the attached CIF
     * variable.</li>
     * </ul>
     * </p>
     *
     * @param cifObjectFinder Finder to get CIF objects from the input specification from their absolute name.
     * @return The created entries of the I/O table, list is empty if no table file was found.
     */
    private List<IoEntry> convertIoTableEntries(CifObjectFinder cifObjectFinder) {
        Set<PositionObject> connectedInputCifObjects = set(); // Used CIF objects for input.
        Set<IoAddress> connectedPlcAddresses = set(); // Used PLC addresses for output.

        // Construct the I/O entries.
        List<IoEntry> entries = list();
        int lineNumber = 0;
        for (List<String> line: getCsvLines()) {
            lineNumber++;

            // Text stating the CSV line being processed, for reporting any error.
            String tableLinePositionText = fmt("at line %d of I/O table file \"%s\"", lineNumber,
                    ioTablePaths.userPath);

            // Check the number of columns returned by the CSV parser.
            int numColumns = line.size();
            if (numColumns < 3 || numColumns > 4) {
                String message = fmt("Incorrect number of fields (expected 3 or 4 fields, found %d) %s.",
                        numColumns, tableLinePositionText);
                throw new InvalidInputException(message);
            }

            // Second field, the PLC type, may be empty. If empty the CIF type is used instead.
            String plcTableTypeText = line.get(PLC_TYPE_COLUMN).trim();
            PlcType plcTableType = checkIoType(plcTableTypeText, tableLinePositionText);

            // Third field, the absolute name of the CIF object to connect to the I/O address.
            String absCifName = line.get(ABS_CIF_NAME_COLUMN).trim();
            PositionObject cifObj;
            try {
                cifObj = cifObjectFinder.findCifObjectByAbsName(absCifName);
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
            if (directionFromCif == IoDirection.IO_READ) {
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
            String plcAddressText = line.get(PLC_IO_ADDRESS_COLUMN).trim();
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
            if (directionFromCif == IoDirection.IO_WRITE) {
                if (connectedPlcAddresses.contains(plcAddress)) {
                    String message = fmt(
                            "The PLC address for the entry %s is already in use for outputting a value, as "
                                    + "specified by an earlier I/O table entry.",
                            tableLinePositionText);
                    throw new InvalidInputException(message);
                }
                connectedPlcAddresses.add(plcAddress);
            }

            // Fourth field, optional name of the I/O variable.
            String ioName = (numColumns > IO_NAME_COLUMN) ? line.get(IO_NAME_COLUMN) : null;
            ioName = (ioName == null || ioName.isBlank()) ? null : ioName;

            // Verify with the target whether the configured I/O table data makes sense and add it to the collection
            // of entries.
            target.verifyIoTableEntry(plcAddress, plcTableType, directionFromCif, ioName, tableLinePositionText);
            IoEntry entry = new IoEntry(plcAddress, plcTableType, cifObj, directionFromCif, ioName);
            entries.add(entry);
        }
        return entries;
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
     * Derive a valid PLC type for an entry from its attached CIF object.
     *
     * @param absName Absolute name of the object in CIF.
     * @param cifObj The CIF object found from the absolute name in the specification.
     * @param tableLinePositionText Text for reporting about the CSV line.
     * @return The PLC type to use for the I/O entry according to the matched CIF object.
     * @throws InvalidInputException If no valid CIF object can be attached to the provided search result, or the CIF
     *     object does not have a boolean, integer or real type.
     */
    private PlcType decideTypeFromCif(String absName, PositionObject cifObj, String tableLinePositionText) {
        // Derive the PLC type.
        PlcType plcType;
        if (cifObj instanceof DiscVariable dv) {
            plcType = target.getTypeGenerator().convertType(dv.getType());
        } else if (cifObj instanceof InputVariable iv) {
            plcType = target.getTypeGenerator().convertType(iv.getType());
        } else if (cifObj instanceof AlgVariable av) {
            plcType = target.getTypeGenerator().convertType(av.getType());
        } else {
            String message = fmt("The 'CIF name' field containing \"%s\" does not indicate an algebraic, discrete or "
                    + "input variable (third field %s).", absName, tableLinePositionText);
            throw new InvalidInputException(message);
        }

        // Check for having a valid type, and return it.
        if (!FEASIBLE_IO_VAR_TYPES.contains(plcType)) {
            String message = fmt("The type of the CIF variable in the 'CIF name' field containing \"%s\" %s is not "
                    + "a boolean, integer or real type.", absName, tableLinePositionText);
            throw new InvalidInputException(message);
        }
        return plcType;
    }

    /**
     * Obtain the I/O direction of the I/O from the CIF object associated with the I/O entry.
     *
     * @param posObject CIF object attached to the entry.
     * @return Direction of I/O for the entry in the PLC.
     */
    private IoDirection decideIoDirectionFromCif(PositionObject posObject) {
        if (posObject instanceof DiscVariable || posObject instanceof AlgVariable) {
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
            // Throw an error if the types are fundamentally different. Different sizes (eg INT <-> DINT) are allowed.
            if ((isIntType(plcTableType) && !isIntType(plcTypeFromCif))
                    || (isRealType(plcTableType) && !isRealType(plcTypeFromCif))
                    || (plcTableType == BOOL_TYPE && plcTypeFromCif != BOOL_TYPE))
            {
                String message = fmt("The type stated in the 'PLC type' field (\"%s\") does not correspond with the "
                        + "PLC type (\"%s\") of the connected CIF variable from the 'CIF name' field containing "
                        + "\"%s\", for the entry %s.", getNameOfPlcIoType(plcTableType),
                        getNameOfPlcIoType(plcTypeFromCif), absCifName, tableLinePositionText);
                throw new InvalidInputException(message);
            }
        } else {
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
     * Convert a PLC type to a user-readable name.
     *
     * @param type Type to convert.
     * @return The name of the given type.
     */
    private String getNameOfPlcIoType(PlcType type) {
        if (type instanceof PlcElementaryType eType) {
            return eType.name;
        }

        // Allowed types are currently limited to elementary types, see FEASIBLE_IO_VAR_TYPES.
        throw new AssertionError("Unexpected type \"" + type + "\" found.");
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

        PlcFunctionAppls funcAppls = new PlcFunctionAppls(target);
        NameGenerator nameGenerator = target.getNameGenerator();
        PlcCodeStorage codeStorage = target.getCodeStorage();

        // All variables connected to a PLC input should be read at the same time in order to get a consistent input
        // state. For variables connected to a PLC output the same applies for consistency towards the controlled
        // system.
        // In addition, for outputs a safety requirement exists that requires a safety output to be written only once in
        // a PLC cycle. By writing all output at the same time it is easier to implement it correctly and easier to
        // verify in a review of the generated PLC code.
        CifDataProvider cifDataProvider = codeStorage.getExprGenerator().getScopeCifDataProvider();
        ExprGenerator exprGen = codeStorage.getExprGenerator();
        for (IoEntry entry: entries) {
            // Preliminaries (check I/O direction, construct links to the correct local data structures).
            Assert.check(EnumSet.of(IoDirection.IO_READ, IoDirection.IO_WRITE).contains(entry.ioDirection));
            boolean isInput = (entry.ioDirection == IoDirection.IO_READ);
            List<PlcStatement> stats = isInput ? inputStats : outputStats;

            // Construct the name of the variable with the I/O address.
            String ioVarName;
            if (entry.ioName != null) {
                ioVarName = entry.ioName; // Pick the custom name if available.
            } else {
                // Else, derive a name for the variable with the I/O address from the attached CIF state variable.
                String varPrefix = isInput ? "in_" : "out_";
                ioVarName = getAbsName(entry.cifObject, false);
                ioVarName = varPrefix + nameGenerator.generateGlobalNames(Set.of(varPrefix), ioVarName, false);
            }

            // Construct the variable with the I/O address, and store it.
            PlcDataVariable ioVar;
            if (isInput) {
                String targetText = target.getUsageVariableText(PlcVariablePurpose.INPUT_VAR, ioVarName);
                ioVar = new PlcDataVariable(targetText, ioVarName, entry.varType, entry.plcAddress.getAddress(), null);
                codeStorage.addInputVariable(ioVar);
            } else {
                String targetText = target.getUsageVariableText(PlcVariablePurpose.OUTPUT_VAR, ioVarName);
                ioVar = new PlcDataVariable(targetText, ioVarName, entry.varType, entry.plcAddress.getAddress(), null);
                codeStorage.addOutputVariable(ioVar);
            }

            // Construct the assignment to perform the I/O.
            if (isInput) { // state-var := io-var;
                // Generate a comment what CIF variable is written.
                String commentText = fmt("Read PLC input and write it to %s.",
                        DocumentingSupport.getDescription(entry.cifObject));
                stats.add(new PlcCommentLine(commentText));

                // Create the access expressions for the PLC input variable (LHS) and CIF input variable (RHS).
                Assert.check(entry.cifObject instanceof InputVariable);
                PlcVarExpression leftSide = cifDataProvider.getAddressableForInputVar((InputVariable)entry.cifObject);
                PlcExpression rightSide = new PlcVarExpression(ioVar);

                // Perform the assignment, possibly after unifying the type.
                if (!leftSide.type.equals(rightSide.type)) {
                    rightSide = funcAppls.castFunctionAppl(rightSide, (PlcElementaryType)leftSide.type);
                }
                stats.add(new PlcAssignmentStatement(leftSide, rightSide));

            } else { // io-var := state-var;
                // Generate a comment what CIF variable is read.
                String commentText = fmt("Write %s to PLC output.", DocumentingSupport.getDescription(entry.cifObject));
                stats.add(new PlcCommentLine(commentText));

                // Create the access expression for the PLC output variable (LHS).
                PlcVarExpression leftSide = new PlcVarExpression(ioVar);

                // Construct the right side of the assignment.
                Expression cifRightSide;
                if (entry.cifObject instanceof DiscVariable discVar) {
                    cifRightSide = newDiscVariableExpression(null, EMFHelper.deepclone(discVar.getType()), discVar);
                } else if (entry.cifObject instanceof AlgVariable algVar) {
                    cifRightSide = newAlgVariableExpression(null, EMFHelper.deepclone(algVar.getType()), algVar);
                } else {
                    throw new AssertionError("Unexpected state variable found: " + entry.cifObject);
                }
                ExprValueResult exprResult = exprGen.convertValue(cifRightSide);
                stats.addAll(exprResult.code);
                exprResult.releaseCodeVariables();
                PlcExpression rightSide = exprResult.value;

                // Perform the assignment, possibly after unifying the type.
                if (!leftSide.type.equals(rightSide.type)) {
                    rightSide = funcAppls.castFunctionAppl(rightSide, (PlcElementaryType)leftSide.type);
                }
                stats.add(new PlcAssignmentStatement(leftSide, rightSide));
                exprResult.releaseValueVariables();
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
