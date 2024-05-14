//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.plcgen;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.wrap;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.escet.cif.plcgen.options.ConvertEnums;
import org.eclipse.escet.cif.plcgen.options.ConvertEnumsOption;
import org.eclipse.escet.cif.plcgen.options.IoTablePathOption;
import org.eclipse.escet.cif.plcgen.options.PlcConfigurationNameOption;
import org.eclipse.escet.cif.plcgen.options.PlcIntTypeSizeOption;
import org.eclipse.escet.cif.plcgen.options.PlcMaxIterOption;
import org.eclipse.escet.cif.plcgen.options.PlcMaxIterOption.MaxIterLimits;
import org.eclipse.escet.cif.plcgen.options.PlcNumberBits;
import org.eclipse.escet.cif.plcgen.options.PlcProjectNameOption;
import org.eclipse.escet.cif.plcgen.options.PlcRealTypeSizeOption;
import org.eclipse.escet.cif.plcgen.options.PlcResourceNameOption;
import org.eclipse.escet.cif.plcgen.options.PlcTargetTypeOption;
import org.eclipse.escet.cif.plcgen.options.PlcTaskCycleTimeOption;
import org.eclipse.escet.cif.plcgen.options.PlcTaskNameOption;
import org.eclipse.escet.cif.plcgen.options.PlcTaskPriorityOption;
import org.eclipse.escet.cif.plcgen.options.ProgramHeaderTextFilePathOption;
import org.eclipse.escet.cif.plcgen.options.RenameWarningsOption;
import org.eclipse.escet.cif.plcgen.options.SimplifyValuesOption;
import org.eclipse.escet.cif.plcgen.targets.AbbTarget;
import org.eclipse.escet.cif.plcgen.targets.Iec611313Target;
import org.eclipse.escet.cif.plcgen.targets.PlcBaseTarget;
import org.eclipse.escet.cif.plcgen.targets.PlcOpenXmlTarget;
import org.eclipse.escet.cif.plcgen.targets.PlcTarget;
import org.eclipse.escet.cif.plcgen.targets.PlcTargetType;
import org.eclipse.escet.cif.plcgen.targets.SiemensS7Target;
import org.eclipse.escet.cif.plcgen.targets.TwinCatTarget;
import org.eclipse.escet.common.app.framework.AppEnv;
import org.eclipse.escet.common.app.framework.Application;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.io.AppStreams;
import org.eclipse.escet.common.app.framework.options.InputFileOption;
import org.eclipse.escet.common.app.framework.options.Option;
import org.eclipse.escet.common.app.framework.options.OptionCategory;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.OutputFileOption;
import org.eclipse.escet.common.app.framework.output.IOutputComponent;
import org.eclipse.escet.common.app.framework.output.OutputProvider;
import org.eclipse.escet.common.java.PathPair;
import org.eclipse.escet.common.java.exceptions.InputOutputException;
import org.eclipse.escet.common.java.output.WarnOutput;

/** PLC code generator application. */
public class CifPlcGenApp extends Application<IOutputComponent> {
    /** Text pattern to replace with the name of the application. */
    public static final String APP_NAME_PATTERN = "${app-name}";

    /** Text pattern to replace with the version of the application. */
    public static final String APP_VERSION_PATTERN = "${app-version}";

    /** Text pattern to replace with the current date and time. */
    public static final String TIME_STAMP_PATTERN = "${time-stamp}";

    /** Text pattern to replace with a brief explanation of how to interpret the PLC code. */
    public static final String BRIEF_EXPLANATION_PATTERN = "${brief-explanation}";

    /**
     * Application main method.
     *
     * @param args The command line arguments supplied to the application.
     */
    public static void main(String[] args) {
        CifPlcGenApp app = new CifPlcGenApp();
        app.run(args, true);
    }

    /** Constructor for the {@link CifPlcGenApp} class. */
    public CifPlcGenApp() {
        // Nothing to do here.
    }

    /**
     * Constructor for the {@link CifPlcGenApp} class.
     *
     * @param streams The streams to use for input, output, warning, and error streams.
     */
    public CifPlcGenApp(AppStreams streams) {
        super(streams);
    }

    @Override
    public String getAppName() {
        return "CIF PLC code generator";
    }

    @Override
    public String getAppDescription() {
        return "Generates PLC code from a suitable CIF specification.";
    }

    @Override
    protected int runInternal() {
        // Configure code generation.
        PlcTargetType targetType = PlcTargetTypeOption.getPlcTargetType();
        PlcBaseTarget target;
        switch (targetType) {
            case ABB:
                target = new AbbTarget();
                break;
            case IEC_61131_3:
                target = new Iec611313Target();
                break;
            case PLC_OPEN_XML:
                target = new PlcOpenXmlTarget();
                break;
            case S7_1200:
            case S7_1500:
            case S7_300:
            case S7_400:
                target = new SiemensS7Target(targetType);
                break;
            case TWINCAT:
                target = new TwinCatTarget();
                break;
            default:
                throw new RuntimeException("Unknown target type: " + targetType);
        }
        PlcGenSettings settings = makePlcGenSettings(target);

        // Generate PLC code and write it to the file system.
        target.generate(settings);

        return 0;
    }

    /**
     * Construct settings for the PLC code generator.
     *
     * @param target The target to generate PLC code for.
     * @return The constructed settings instance.
     */
    private PlcGenSettings makePlcGenSettings(PlcTarget target) {
        String projectName = PlcProjectNameOption.getProjName();
        String configurationName = PlcConfigurationNameOption.getCfgName();
        String resourceName = PlcResourceNameOption.getResName();
        String plcTaskName = PlcTaskNameOption.getTaskName();
        int taskCyceTime = PlcTaskCycleTimeOption.getTaskCycleTime();
        int priority = PlcTaskPriorityOption.getTaskPrio();
        MaxIterLimits iterLimits = PlcMaxIterOption.getMaxIterLimits();

        String inputPath = InputFileOption.getPath();
        String outputPath = OutputFileOption.getDerivedPath(".cif", target.getPathSuffixReplacement());
        String ioTablePath = IoTablePathOption.getDerivedPath();
        List<String> programHeaderLines = expandAndCleanProgramHeaderLines(obtainProgramHeaderLines());

        PlcNumberBits intSize = PlcIntTypeSizeOption.getNumberBits();
        PlcNumberBits realSize = PlcRealTypeSizeOption.getNumberBits();
        boolean simplifyValues = SimplifyValuesOption.simplifyValues();
        ConvertEnums enumConversion = ConvertEnumsOption.getValue();

        // Required invariant: Once it returns true, it must return true on subsequent calls.
        Supplier<Boolean> shouldTerminate = () -> AppEnv.isTerminationRequested();

        boolean warnOnRename = RenameWarningsOption.isEnabled();
        WarnOutput warnOutput = OutputProvider.getWarningOutputStream();

        return new PlcGenSettings(projectName, configurationName, resourceName, plcTaskName, taskCyceTime, priority,
                iterLimits.uncontrollableLimit(), iterLimits.controllableLimit(),
                new PathPair(inputPath, Paths.resolve(inputPath)), new PathPair(outputPath, Paths.resolve(outputPath)),
                new PathPair(ioTablePath, Paths.resolve(ioTablePath)), programHeaderLines, intSize, realSize,
                simplifyValues, enumConversion, shouldTerminate, warnOnRename, warnOutput);
    }

    @Override
    protected OutputProvider<IOutputComponent> getProvider() {
        return new OutputProvider<>();
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected OptionCategory getAllOptions() {
        OptionCategory generalCat = getGeneralOptionCategory();

        List<Option> applicationOpts = list();
        applicationOpts.add(Options.getInstance(InputFileOption.class));
        applicationOpts.add(Options.getInstance(OutputFileOption.class));
        applicationOpts.add(Options.getInstance(IoTablePathOption.class));
        applicationOpts.add(Options.getInstance(ProgramHeaderTextFilePathOption.class));
        applicationOpts.add(Options.getInstance(PlcTargetTypeOption.class));
        applicationOpts.add(Options.getInstance(PlcConfigurationNameOption.class));
        applicationOpts.add(Options.getInstance(PlcProjectNameOption.class));
        applicationOpts.add(Options.getInstance(PlcResourceNameOption.class));
        applicationOpts.add(Options.getInstance(PlcTaskCycleTimeOption.class));
        applicationOpts.add(Options.getInstance(PlcTaskNameOption.class));
        applicationOpts.add(Options.getInstance(PlcTaskPriorityOption.class));
        applicationOpts.add(Options.getInstance(PlcIntTypeSizeOption.class));
        applicationOpts.add(Options.getInstance(PlcRealTypeSizeOption.class));
        applicationOpts.add(Options.getInstance(SimplifyValuesOption.class));
        applicationOpts.add(Options.getInstance(ConvertEnumsOption.class));
        applicationOpts.add(Options.getInstance(RenameWarningsOption.class));
        applicationOpts.add(Options.getInstance(PlcMaxIterOption.class));

        List<OptionCategory> generatorSubCats = list();
        OptionCategory generatorCat = new OptionCategory("Generator", "Generator options.", generatorSubCats,
                applicationOpts);

        List<OptionCategory> cats = list(generalCat, generatorCat);
        OptionCategory options = new OptionCategory("CIF PLC Code Generator Options",
                "All options for the CIF PLC code generator.", cats, list());

        return options;
    }

    /**
     * Construct the text of the PLC program header.
     *
     * @return The lines of text of the PLC program header.
     */
    private List<String> obtainProgramHeaderLines() {
        PathPair paths = ProgramHeaderTextFilePathOption.getProgramHeaderFilePaths();
        if (paths == null) {
            // No file supplied, use the default program header.
            return List.of( //
                    "This file is generated with CIF's PLC code generator from the Eclipse ESCET toolkit.", //
                    "", //
                    "Generator name:    ${app-name}", //
                    "Generator version: ${app-version}", //
                    "Generation time:   ${time-stamp}", //
                    "", //
                    "${brief-explanation}" //
            );
        }

        // Load the file for using as program header.
        List<String> lines = list();
        try (BufferedReader headerTextFile = new BufferedReader(new FileReader(paths.systemPath))) {
            boolean seenEmptyLine = false;
            for (String line = headerTextFile.readLine(); line != null; line = headerTextFile.readLine()) {
                String anotherLine = line.stripTrailing(); // Checkstyle Pro false positive fix.
                if (anotherLine.isEmpty()) {
                    seenEmptyLine = true; // Just store seeing an empty line rather than immediately adding it.
                } else {
                    if (seenEmptyLine) { // If a non-empty line follows insert an empty line before.
                        lines.add("");
                        seenEmptyLine = false;
                    }
                    lines.add(anotherLine);
                }
            }
            headerTextFile.close();
        } catch (FileNotFoundException ex) {
            OutputProvider.err(fmt("Program header text file \"%s\" does not exist, is a directory rather than a file, "
                    + "or could not be be opened for reading.", paths.userPath));
        } catch (IOException ex) {
            throw new InputOutputException(
                    "Failed to read or close program header text file \"" + paths.userPath + "\".", ex);
        }
        return lines;
    }

    /**
     * Find the text patterns in the given lines, and expand them. Also, remove non-printable ASCII, disable PLC comment
     * brackets, and drop trailing whitespace.
     *
     * @param lines Lines to expand and clean up.
     * @return The expanded and cleaned lines.
     */
    private List<String> expandAndCleanProgramHeaderLines(List<String> lines) {
        // Setup pattern replacements.
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.US);
        String appName = getAppName().strip();
        String appVersion = getAppVersion().strip();
        String timeStamp = df.format(new Date()).strip();

        // Setup the brief explanation replacement.
        List<String> briefExplanation = list();
        briefExplanation.add("- - - - - - - - - - - - -");
        formatText(briefExplanation, "This PLC prgram implements a controller that was designed in the CIF language.");
        briefExplanation.add("");
        formatText(briefExplanation, "For those that do not know the CIF language, this brief explanation relates",
                "concepts in the CIF language with known concepts in PLC programming. This makes it easier to understand what is being computed in this PLC program.");
        briefExplanation.add("");
        formatText(briefExplanation, "A CIF model has one or more automata (which work like state",
                "machines). All these automata work in parallel.");
        formatText(briefExplanation, "Each automaton has locations (equivalent to states). Like state machines, one of",
                "the locations is the \"current state\". Each location has edges to other locations of the same",
                "automaton, thus allowing to change the current state of the state machine by an edge. An edge also ",
                "has a guard condition that must be satisfied for the edge to be taken and updates to change the values of variables (like",
                "assignments).");
        briefExplanation.add("");
        formatText(briefExplanation, "Unlike regular state machines, CIF can couple edges of different state machines",
                "together by means of an event-name (often abbreviated to 'event'). The most common form of coupling",
                "is \"synchronizing\", which means that all automata that have the event must take an edge at the same",
                "time. Automata coupled with \"monitoring\" may (but are not required to) also take an edge at the",
                "same time. In some cases a channel may be used. In such a case, there is an",
                "additional coupled \"sender\" and \"receiver\" automaton that may exchange a data value.");
        briefExplanation.add("");
        formatText(briefExplanation, "In CIF there are two kinds of events. Uncontrollable events are events that are",
                "caused by received changes from the environment. Controllable events are performed while computing a",
                "control response. In a single PLC cycle first the state is updated from the sensors. Then the",
                "uncontrollable events are performed as much as possible, followed by performing as many as possible",
                "controllable events. Finally, output is written to the actuators.");
        briefExplanation.add("- - - - - - - - - - - - -");

        // Expand the patterns in the lines, and apply cleanup.
        List<String> resultLines = list();
        Pattern pat = Pattern.compile("\\$\\{[^}]+\\}"); // Match ${...} and grab the text at the dots.

        // Perform pattern replacements on each line, followed by cleanup and disabling comment sequences.
        for (String line: lines) {
            Matcher m = pat.matcher(line);
            boolean addLine = true; // If it holds after the loop, 'line' should be added to the output.

            int scanStart = 0;
            while (m.find(scanStart)) {
                if (BRIEF_EXPLANATION_PATTERN.equals(m.group())) {
                    // The explanation has multiple lines, splitting lines before and after the pattern makes no sense.
                    // Instead, ignore the input line, and insert the explanation.
                    for (String explainLine: briefExplanation) {
                        resultLines.add(postProcessLine(explainLine));
                    }
                    addLine = false;
                    break;
                } else {
                    String replacement = switch (m.group()) {
                        case APP_NAME_PATTERN -> appName;
                        case APP_VERSION_PATTERN -> appVersion;
                        case TIME_STAMP_PATTERN -> timeStamp;
                        default -> m.group(1);
                    };
                    line = line.substring(0, m.start()) + replacement + line.substring(m.end());
                    scanStart = m.end();
                }
            }

            if (addLine) {
                resultLines.add(postProcessLine(line));
            }
        }
        return resultLines;
    }

    /**
     * Format a multi-string text by concatenating and splitting at length 75 characters. Add the formatted lines to the
     * destination.
     *
     * @param lines Destination of the formatted text.
     * @param text New text to format and add.
     */
    private void formatText(List<String> lines, String... text) {
        // Merge the input text lines into a single string.
        int spaceNeeded = text.length - 1;
        for (String s: text) {
            spaceNeeded += s.length();
        }
        StringBuilder sb = new StringBuilder(spaceNeeded);
        boolean first = true;
        for (String s: text) {
            if (!first) {
                sb.append(' ');
            } else {
                first = false;
            }

            sb.append(s);
        }

        // Format the resulting text at 75 characters length and add the lines to the output.
        for (String line: wrap(75, sb.toString())) {
            lines.add(line);
        }
    }

    /**
     * Perform cleanup of the text line and disable PLC comment brackets.
     *
     * @param line Text line to process.
     * @return The cleaned text line.
     */
    private String postProcessLine(String line) {
        line = restrictToPrintableAscii(line); // Drop non-printable ASCII.

        // Disable PLC comment brackets and delete trailing white space.
        // Comment brackets may overlap (with "(*)"). Changing each bracket type separately handles that as well.
        return line.replace("(*", "(-*").replace("*)", "*-)").stripTrailing();
    }

    /**
     * Remove any non-printable or non-ASCII character from the text line.
     *
     * @param textLine Text to check. Text is cleaned to printable ASCII characters only. That makes all string
     *     backslash escape sequences such as {@code \t} or {@code \n} invalid.
     * @return The cleaned string.
     */
    private String restrictToPrintableAscii(String textLine) {
        char[] newChars = null; // Replacement text of the line if errors are found. Lazily created.
        int newLength = 0; // Length of the new text if it exists.

        // Check and possibly copy the text line if bad characters are found.
        for (int i = 0; i < textLine.length(); i++) {
            char c = textLine.charAt(i);
            if (c < 32 || c > 126) {
                // Non-printable or non-ASCII character, remove it.
                if (newChars == null) {
                    // First time such a character is found. Create a new array for storing valid characters only and
                    // fill it with the previous characters.
                    newChars = new char[textLine.length()]; // Original length is enough to store any new string.
                    newLength = 0;
                    for (; newLength < i; newLength++) {
                        newChars[newLength] = textLine.charAt(newLength);
                    }
                }
                continue; // Do not add 'c'.
            } else if (newChars != null) {
                // Valid character found, and we are copying to a new array. Add the valid character.
                newChars[newLength] = c;
                newLength++;
            }
        }

        // Return the original text if possible, else create the replacement text.
        return (newChars == null) ? textLine : new String(newChars, 0, newLength);
    }
}
