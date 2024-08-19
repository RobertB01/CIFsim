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

import static org.eclipse.escet.common.app.framework.output.OutputProvider.warn;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.last;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.wrap;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

import org.eclipse.escet.cif.common.CifControllerPropertiesAnnotationUtils;
import org.eclipse.escet.cif.io.CifReader;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.plcgen.options.ConvertEnums;
import org.eclipse.escet.cif.plcgen.options.ConvertEnumsOption;
import org.eclipse.escet.cif.plcgen.options.IoTablePathOption;
import org.eclipse.escet.cif.plcgen.options.PlcConfigurationNameOption;
import org.eclipse.escet.cif.plcgen.options.PlcIntTypeSizeOption;
import org.eclipse.escet.cif.plcgen.options.PlcMaxIterOption;
import org.eclipse.escet.cif.plcgen.options.PlcMaxIterOption.IterLimitKind;
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
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.PathPair;
import org.eclipse.escet.common.java.Termination;
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

        // Load the specification already. It's needed to compute maximum iteration bounds.
        String inputPath = InputFileOption.getPath();
        PathPair inputPathPair = new PathPair(inputPath, Paths.resolve(inputPath));
        Specification inputSpec = new CifReader().init(inputPathPair.userPath, inputPathPair.systemPath, false).read();
        PlcGenSettings settings = makePlcGenSettings(target, inputPathPair, inputSpec);

        // Generate PLC code and write it to the file system.
        target.generate(settings, inputSpec);

        return 0;
    }

    /**
     * Construct settings for the PLC code generator.
     *
     * @param target The target to generate PLC code for.
     * @param inputPathPair Paths to the CIF specification for which to generate PLC code.
     * @param inputSpec Input CIF specification.
     * @return The constructed settings instance.
     */
    private PlcGenSettings makePlcGenSettings(PlcTarget target, PathPair inputPathPair, Specification inputSpec) {
        String projectName = PlcProjectNameOption.getProjName();
        String configurationName = PlcConfigurationNameOption.getCfgName();
        String resourceName = PlcResourceNameOption.getResName();
        String plcTaskName = PlcTaskNameOption.getTaskName();
        int taskCyceTime = PlcTaskCycleTimeOption.getTaskCycleTime();
        int priority = PlcTaskPriorityOption.getTaskPrio();

        String outputPath = OutputFileOption.getDerivedPath(".cif", target.getPathSuffixReplacement());
        String ioTablePath = IoTablePathOption.getDerivedPath();
        PathPair outputPathPair = new PathPair(outputPath, Paths.resolve(outputPath));
        PathPair ioTablePathPair = new PathPair(ioTablePath, Paths.resolve(ioTablePath));
        List<String> programHeaderLines = expandAndCleanProgramHeaderLines(obtainProgramHeaderLines());
        LoopLimits iterLimits = deriveIterLimits(inputSpec);

        PlcNumberBits intSize = PlcIntTypeSizeOption.getNumberBits();
        PlcNumberBits realSize = PlcRealTypeSizeOption.getNumberBits();
        boolean simplifyValues = SimplifyValuesOption.simplifyValues();
        ConvertEnums enumConversion = ConvertEnumsOption.getValue();

        Termination termination = () -> isTerminationRequested();

        boolean warnOnRename = RenameWarningsOption.isEnabled();
        WarnOutput warnOutput = OutputProvider.getWarningOutputStream();

        return new PlcGenSettings(projectName, configurationName, resourceName, plcTaskName, taskCyceTime, priority,
                iterLimits.uncontrollableLoopLimit(), iterLimits.controllableLoopLimit(), inputPathPair, outputPathPair,
                ioTablePathPair, programHeaderLines, intSize, realSize, simplifyValues, enumConversion,
                termination, warnOnRename, warnOutput);
    }

    /**
     * Derive the maximum iteration limits to use in the PLC code generator for uncontrollable and controllable events.
     *
     * @param spec Input specification.
     * @return The maximum iteration limits to use.
     */
    @SuppressWarnings("null")
    private LoopLimits deriveIterLimits(Specification spec) {
        // Sentences to explain how to obtain the properties, and why it is important that they hold.
        String badNoProps = "Using control code generated from a CIF specification without bounded response, "
                + "confluence or non-blocking under control properties may result in undesired or unexpected behavior "
                + "of the controlled system.";
        // Check that the specification has controller properties for bounded response and confluence.
        Boolean hasBoundedResponse = CifControllerPropertiesAnnotationUtils.hasBoundedResponse(spec);
        Boolean hasConfluence = CifControllerPropertiesAnnotationUtils.hasConfluence(spec);
        Boolean hasNonBlocking = CifControllerPropertiesAnnotationUtils.isNonBlockingUnderControl(spec);
        Boolean hasFiniteResponse = CifControllerPropertiesAnnotationUtils.hasFiniteResponse(spec);

        boolean warned = false; // Whether warnings were given.

        // Check and possibly report about lack of existence of the bounded response and/or finite response properties.
        if (hasBoundedResponse == null) {
            warn("The input specification has no controller properties annotation that specifies the bounded response "
                    + "property.");
            warned = true;
        }
        if (hasConfluence == null) {
            warn("The input specification has no controller properties annotation that specifies the confluence "
                    + "property.");
            warned = true;
        }
        if (hasNonBlocking == null) {
            warn("The input specification has no controller properties annotation that specifies the non-blocking "
                    + "under control property.");
            warned = true;
        }
        // Don't care about lacking finite response, as bounded response covers it completely.

        if (warned) {
            warn("Before generating PLC code, the bounded response, confluence and non-blocking under control "
                    + "properties of the CIF specification should be checked and should hold.");
            warn("Please apply the CIF controller properties checker application on the CIF specification before "
                    + "generating PLC code from it.");
            warn();
            warn(badNoProps);
        }

        // Check and possibly report that the bounded response and/or confluence properties do not hold.
        // If provided, also check and possibly report that the finite response property does not hold.
        if (!warned) {
            if (hasBoundedResponse.equals(Boolean.FALSE)) { // The value cannot be 'null' here.
                warn("The controller properties annotation of the input specification that specifies the bounded "
                        + "response property does not hold.");
            }
            if (hasConfluence.equals(Boolean.FALSE)) { // The value cannot be 'null' here.
                warn("The controller properties annotation of the input specification that specifies the confluence "
                        + "property may not hold.");
            }
            if (hasNonBlocking.equals(Boolean.FALSE)) { // The value cannot be 'null' here.
                warn("The controller properties annotation of the input specification that specifies the non-blocking "
                        + "under control property does not hold.");
            }

            // Missing or true finite response is ok.
            if (hasFiniteResponse != null && hasFiniteResponse.equals(Boolean.FALSE)) {
                warn("The controller properties annotation of the input specification that specifies the finite "
                        + "response property may not hold.");
            }

            if (!hasBoundedResponse.equals(Boolean.TRUE) || !hasConfluence.equals(Boolean.TRUE)
                    || !hasNonBlocking.equals(Boolean.TRUE))
            {
                warn("Before generating PLC code, the bounded response, confluence and non-blocking under control "
                        + "properties of the CIF specification should hold.");
                warn("Please improve the CIF specification, and check the properties again.");
                warn();
                warn(badNoProps);
            }
        }

        boolean tooLow = false; // Flag to warn if the used limit is lower than the computed bounded response limit.
        MaxIterLimits iterLimits = PlcMaxIterOption.getMaxIterLimits();

        // Compute the used iteration bound for uncontrollable events.
        Integer uncontrBound = CifControllerPropertiesAnnotationUtils.getUncontrollablesBound(spec);
        Integer usedUncontrLimit = computeLimit(
                () -> iterLimits.getUncontrollableLimitKind(false),
                () -> iterLimits.getUncontrollableLimitKind(true),
                () -> iterLimits.getUncontrollableLimitNumber(false),
                () -> iterLimits.getUncontrollableLimitNumber(true), uncontrBound, "uncontrollable");
        Assert.check(usedUncontrLimit == null || usedUncontrLimit > 0);

        // Warn about a too low limit if possible.
        if (usedUncontrLimit != null && uncontrBound != null && usedUncontrLimit < uncontrBound) {
            warn("Used uncontrollable event iteration limit (at most %d attempts) is less than the bounded response "
                    + "limit of the controller properties annotation (%d attempts are needed).",
                    usedUncontrLimit, uncontrBound);
            tooLow = true;
        }

        // Compute the used iteration bound for controllable events.
        Integer contrBound = CifControllerPropertiesAnnotationUtils.getControllablesBound(spec);
        Integer usedContrLimit = computeLimit(
                () -> iterLimits.getControllableLimitKind(false),
                () -> iterLimits.getControllableLimitKind(true),
                () -> iterLimits.getControllableLimitNumber(false),
                () -> iterLimits.getControllableLimitNumber(true), contrBound, "controllable");
        Assert.check(usedContrLimit == null || usedContrLimit > 0);

        // Warn about a too low limit if possible.
        if (usedContrLimit != null && contrBound != null && usedContrLimit < contrBound) {
            warn("Used controllable event iteration limit (at most %d attempts) is less than the bounded response "
                    + "limit of the controller properties annotation (%d attempts are needed).",
                    usedContrLimit, contrBound);
            tooLow = true;
        }

        // If a 'too low' warning was given also explain why it's a bad idea to proceed.
        if (tooLow) {
            warn("Using a lower limit than the bound of the controller properties annotation in the specification may "
                    + "result in undesired or unexpected behavior of the controlled system.");
        }

        // Construct and return the derived limits.
        return new LoopLimits(usedUncontrLimit, usedContrLimit);
    }

    /**
     * Compute the iteration limit for a kind of events from the available preferences and information.
     *
     * @param getUserLimitKind Function to obtain the kind of bound requested by the user.
     * @param getFallbackLimitKind Function to obtain the kind of bound in case the user requested to use the computed
     *     response but it is not available.

     * @param getUserBound Function to obtain the numeric limit expressed by the user, if and only if the
     *     {@code getUserLimitKind} expresses that the user requested it.
     * @param getFallbackBound Function to obtain the numeric limit expressed as default, if and only if the
     *     {@code getFallbackLimitKind} expresses that as fallback kind.

     * @param boundedResponse The computed bounded response limit if available. Is either a positive value or
     *     {@code null} if there is no computed bound available.
     * @param eventKindName Name of the kind of events dealt with.
     * @return The iteration limit to use for the kind of events.
     */
    Integer computeLimit(Supplier<IterLimitKind> getUserLimitKind, Supplier<IterLimitKind> getFallbackLimitKind,
            IntSupplier getUserBound, IntSupplier getFallbackBound, Integer boundedResponse, String eventKindName)
    {
        // Try to realize what the user requested.
        switch (getUserLimitKind.get()) {
            case BOUNDED_RESPONSE: {
                if (boundedResponse != null) {
                    return boundedResponse;
                }
                break;
            }
            case INFINITE:
                return null;
            case INTEGER:
                return getUserBound.getAsInt();
            default:
                throw new AssertionError("Unexpected limit kind \"" + getUserLimitKind.get() + "\".");
        }

        // User specifed to use bounded response, but it is not available. Use the fallback.
        String kindText;
        Integer resultValue;
        switch (getFallbackLimitKind.get()) {
            case INFINITE:
                resultValue = null;
                kindText = "limitless trying events until blocked";
                break;
            case INTEGER:
                resultValue = getFallbackBound.getAsInt();
                kindText = "trying events up to " + resultValue + " attempts";
                break;
            case BOUNDED_RESPONSE:
            default:
                throw new AssertionError("Unexpected limit kind \"" + getFallbackLimitKind.get() + "\".");
        }

        warn("Bounded response limit for %s events is not available, falling back to %s.", eventKindName, kindText);
        return resultValue;
    }

    /**
     * Iteration loop limits for the controllable and uncontrollable events.
     *
     * @param uncontrollableLoopLimit If positive, the maximum number of iteration to allow for uncontrollable events.
     *     If {@code null}, there is no limit.
     * @param controllableLoopLimit If positive, the maximum number of iteration to allow for controllable events. If
     *     {@code null}, there is no limit.
     */
    public static record LoopLimits(Integer uncontrollableLoopLimit, Integer controllableLoopLimit) {
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

        // Load the file for using as program header. Cleanup happens further down, so don't bother with it now.
        List<String> lines = list();
        try (BufferedReader headerTextFile = new BufferedReader(new FileReader(paths.systemPath))) {
            for (String line = headerTextFile.readLine(); line != null; line = headerTextFile.readLine()) {
                lines.add(line.stripTrailing());
            }
        } catch (FileNotFoundException ex) {
            String msgText = fmt("Program header text file \"%s\" does not exist, is a directory rather than a file, "
                    + "or could not be opened for reading.", paths.userPath);
            throw new InputOutputException(msgText, ex);
        } catch (IOException ex) {
            throw new InputOutputException(
                    "Failed to read or close program header text file \"" + paths.userPath + "\".", ex);
        }
        return lines;
    }

    /**
     * Process the read header lines.
     *
     * <p>
     * Expand the text patterns. Also, remove non-printable ASCII, drop trailing whitespace and disable PLC comments.
     * Finally, drop leading and trailing empty text lines.
     * </p>
     *
     * @param rawHeaderLines Lines of the header text to process.
     * @return The expanded and cleaned lines.
     */
    private List<String> expandAndCleanProgramHeaderLines(List<String> rawHeaderLines) {
        // Setup the small replacements.
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.US);
        String appName = getAppName().strip();
        String appVersion = getAppVersion().strip();
        String timeStamp = df.format(new Date()).strip();

        // Setup the brief explanation replacement.
        List<String> briefExplanation = list();
        briefExplanation.add("- - - - - - - - - - - - -");
        formatText(briefExplanation, "This PLC program implements a controller that was designed in the CIF language.");
        briefExplanation.add("");
        formatText(briefExplanation, "For those that do not know the CIF language, this brief explanation relates",
                "concepts in the CIF language with known concepts in PLC programming. This makes it easier to",
                "understand what is being computed in this PLC program.");
        briefExplanation.add("");
        formatText(briefExplanation, "A CIF model has one or more automata (which work like state machines). All these",
                "automata work in parallel.");
        formatText(briefExplanation, "Each automaton has locations (equivalent to states). Like state machines, one of",
                "the locations is the \"current state\". Each location has edges to other locations of the same",
                "automaton, thus allowing to change the current state of the state machine by an edge. An edge also ",
                "has a guard condition that must be satisfied for the edge to be taken and updates to change the",
                "values of variables (like assignments).");
        briefExplanation.add("");
        formatText(briefExplanation, "In CIF, different state machines can be coupled by labeling their edges with the",
                "same \"event\". The most common form of coupling is \"synchronizing\", which means that all automata ",
                "that have the event must take an edge at the same time. Automata coupled with \"monitoring\" must",
                "also synchronize, but only if they have an edge for the event in their current state. In some cases a",
                "channel may be used. In such a case, one additional \"sender\" and one additional \"receiver\"",
                "automaton must be involved, and they may exchange a data value.");
        briefExplanation.add("");
        formatText(briefExplanation, "In CIF there are two kinds of events. Uncontrollable events are events that are",
                "caused by received changes from the environment (such as sensor signal changes). Controllable events",
                "are performed while computing a control response (such as enabling or disabling an actuator). In a",
                "single PLC cycle first the state is updated from the PLC inputs. Then the uncontrollable events are",
                "performed as much as possible, followed by performing as many as possible controllable events.",
                "Finally, the updated state is written to the PLC outputs.");
        briefExplanation.add("- - - - - - - - - - - - -");
        int lastExplainIndex = briefExplanation.size() - 1;
        int lengthLastExplainLine = last(briefExplanation).length();

        // Collected lines of the header text.
        List<String> headerLines = list();

        // Process the raw header text lines.
        for (String line: rawHeaderLines) {
            // Expand the one-line templates.
            line = line.replace(APP_NAME_PATTERN, appName).replace(APP_VERSION_PATTERN, appVersion)
                    .replace(TIME_STAMP_PATTERN, timeStamp);

            // Look for the brief explanation template pattern.
            int patternIndex = line.indexOf(BRIEF_EXPLANATION_PATTERN);
            while (patternIndex >= 0) {
                // If found, append the first line of the explanation to the text before the template pattern, and
                // process the result.
                postProcessLine(headerLines, line.substring(0, patternIndex) + first(briefExplanation));
                // Add all intermediate explanation lines.
                for (int explainLineNum = 1; explainLineNum < lastExplainIndex; explainLineNum++) {
                    postProcessLine(headerLines, briefExplanation.get(explainLineNum));
                }
                // Construct a new header line with the last line of the explanation together with the remaining text
                // after the template pattern. Also search for more explanation templates in the remaining text.
                line = last(briefExplanation) + line.substring(patternIndex + BRIEF_EXPLANATION_PATTERN.length());
                patternIndex = line.indexOf(BRIEF_EXPLANATION_PATTERN, lengthLastExplainLine);
            }
            postProcessLine(headerLines, line); // Always contains text if an explanation template was expanded.
        }

        // Trim leading and trailing empty lines from the header text.
        int firstNonEmpty; // Inclusive lower bound.
        for (firstNonEmpty = 0; firstNonEmpty < headerLines.size()
                && headerLines.get(firstNonEmpty).isEmpty(); firstNonEmpty++)
        {
            // Do nothing.
        }
        int lastNonEmpty; // Inclusive upper bound.
        for (lastNonEmpty = headerLines.size() - 1; lastNonEmpty >= firstNonEmpty
                && headerLines.get(lastNonEmpty).isEmpty(); lastNonEmpty--)
        {
            // Do nothing.
        }

        return (firstNonEmpty > lastNonEmpty) ? Collections.emptyList()
                : headerLines.subList(firstNonEmpty, lastNonEmpty + 1);
    }

    /**
     * Format a multi-string text by concatenating and splitting at length 75 characters. Add the formatted lines to the
     * destination.
     *
     * @param dest Destination of the formatted text.
     * @param texts New text parts forming one paragraph that should be added.
     */
    private void formatText(List<String> dest, String... texts) {
        for (String line: wrap(75, String.join(" ", texts))) {
            dest.add(line);
        }
    }

    /**
     * Perform cleanup of the text line, disable PLC comment brackets, and add it to the end of the program header text
     * lines.
     *
     * @param headerLines Text lines of the program header text, is extended in-place.
     * @param line Text line to process.
     */
    private void postProcessLine(List<String> headerLines, String line) {
        line = restrictToPrintableAscii(line); // Drop non-printable ASCII.

        // Disable PLC comment brackets and delete trailing white space.
        // Comment brackets may overlap (with "(*)"). Changing each bracket type separately handles that as well.
        headerLines.add(line.replace("(*", "(-*").replace("*)", "*-)").stripTrailing());
    }

    /**
     * Remove non-printable or non-ASCII characters from the text line.
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
