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

package org.eclipse.escet.cif.multilevel;

import static org.eclipse.escet.cif.checkers.checks.invcheck.NoInvariantKind.STATE;
import static org.eclipse.escet.cif.checkers.checks.invcheck.NoInvariantPlaceKind.ALL_PLACES;
import static org.eclipse.escet.cif.checkers.checks.invcheck.NoInvariantPlaceKind.LOCATIONS;
import static org.eclipse.escet.cif.checkers.checks.invcheck.NoInvariantSupKind.KINDLESS;
import static org.eclipse.escet.cif.checkers.checks.invcheck.NoInvariantSupKind.PLANT;
import static org.eclipse.escet.cif.checkers.checks.invcheck.NoInvariantSupKind.REQUIREMENT;
import static org.eclipse.escet.cif.checkers.checks.invcheck.NoInvariantSupKind.SUPERVISOR;
import static org.eclipse.escet.cif.common.CifTextUtils.getAbsName;
import static org.eclipse.escet.cif.common.CifTextUtils.invToStr;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.dbg;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.ddbg;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.dodbg;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.idbg;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.out;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.warn;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.makeFixedLengthNumberText;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixFormat;
import org.eclipse.escet.cif.checkers.CifPreconditionChecker;
import org.eclipse.escet.cif.checkers.checks.AutOnlySpecificSupKindsCheck;
import org.eclipse.escet.cif.checkers.checks.AutOnlyWithOneInitLocCheck;
import org.eclipse.escet.cif.checkers.checks.CompNoInitPredsCheck;
import org.eclipse.escet.cif.checkers.checks.CompNoMarkerPredsCheck;
import org.eclipse.escet.cif.checkers.checks.EdgeOnlySimpleAssignmentsCheck;
import org.eclipse.escet.cif.checkers.checks.EqnNotAllowedCheck;
import org.eclipse.escet.cif.checkers.checks.EventNoChannelsCheck;
import org.eclipse.escet.cif.checkers.checks.EventNoTauCheck;
import org.eclipse.escet.cif.checkers.checks.EventOnlyWithControllabilityCheck;
import org.eclipse.escet.cif.checkers.checks.ExprNoSpecificBinaryExprsCheck;
import org.eclipse.escet.cif.checkers.checks.ExprNoSpecificBinaryExprsCheck.NoSpecificBinaryOp;
import org.eclipse.escet.cif.checkers.checks.ExprNoSpecificExprsCheck;
import org.eclipse.escet.cif.checkers.checks.ExprNoSpecificExprsCheck.NoSpecificExpr;
import org.eclipse.escet.cif.checkers.checks.ExprNoSpecificUnaryExprsCheck;
import org.eclipse.escet.cif.checkers.checks.ExprNoSpecificUnaryExprsCheck.NoSpecificUnaryOp;
import org.eclipse.escet.cif.checkers.checks.FuncNoSpecificUserDefCheck;
import org.eclipse.escet.cif.checkers.checks.FuncNoSpecificUserDefCheck.NoSpecificUserDefFunc;
import org.eclipse.escet.cif.checkers.checks.InvNoSpecificInvsCheck;
import org.eclipse.escet.cif.checkers.checks.TypeIntBoundsCheck;
import org.eclipse.escet.cif.checkers.checks.TypeNoSpecificTypesCheck;
import org.eclipse.escet.cif.checkers.checks.TypeNoSpecificTypesCheck.NoSpecificType;
import org.eclipse.escet.cif.checkers.checks.VarDiscOnlyStaticEvalInitCheck;
import org.eclipse.escet.cif.checkers.checks.VarNoContinuousCheck;
import org.eclipse.escet.cif.checkers.checks.VarNoDiscWithMultiInitValuesCheck;
import org.eclipse.escet.cif.checkers.checks.invcheck.NoInvariantKind;
import org.eclipse.escet.cif.checkers.checks.invcheck.NoInvariantSupKind;
import org.eclipse.escet.cif.cif2cif.ElimComponentDefInst;
import org.eclipse.escet.cif.cif2cif.ElimSelf;
import org.eclipse.escet.cif.cif2cif.RemoveIoDecls;
import org.eclipse.escet.cif.cif2cif.SimplifyValuesOptimized;
import org.eclipse.escet.cif.io.CifReader;
import org.eclipse.escet.cif.io.CifWriter;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.SupKind;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.multilevel.ciftodmm.CifRelations;
import org.eclipse.escet.cif.multilevel.ciftodmm.CifToDmm;
import org.eclipse.escet.cif.multilevel.ciftodmm.SpecHasPlantCheck;
import org.eclipse.escet.cif.multilevel.ciftodmm.SpecHasRequirementCheck;
import org.eclipse.escet.cif.multilevel.clustering.ComputeMultiLevelTree;
import org.eclipse.escet.cif.multilevel.clustering.TreeNode;
import org.eclipse.escet.cif.multilevel.options.DmmOutputFileOption;
import org.eclipse.escet.cif.multilevel.options.PartialSpecsOutputDirectoryOption;
import org.eclipse.escet.cif.multilevel.partialspecs.PartialSpecsBuilder;
import org.eclipse.escet.common.app.framework.Application;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.io.AppStreams;
import org.eclipse.escet.common.app.framework.options.InputFileOption;
import org.eclipse.escet.common.app.framework.options.Option;
import org.eclipse.escet.common.app.framework.options.OptionCategory;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.output.IOutputComponent;
import org.eclipse.escet.common.app.framework.output.OutputProvider;
import org.eclipse.escet.common.dsm.ClusterInput;
import org.eclipse.escet.common.dsm.Dmm;
import org.eclipse.escet.common.dsm.Dsm;
import org.eclipse.escet.common.dsm.DsmClustering;
import org.eclipse.escet.common.java.BitSetIterator;
import org.eclipse.escet.common.java.exceptions.InputOutputException;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/**
 * CIF multi-level synthesis application.
 *
 * <p>
 * This implementation is based on the paper Goorden 2020:
 *
 * M. Goorden, J. v. d. Mortel-Fronczak, M. Reniers, W. Fokkink and J. Rooda, "Structuring Multilevel Discrete-Event
 * Systems With Dependence Structure Matrices", IEEE Transactions on Automatic Control, volume 65, issue 4, pages
 * 1625-1639, 2020, doi:<a href="https://doi.org/10.1109/TAC.2019.2928119">10.1109/TAC.2019.2928119</a>.
 * </p>
 */
public class MultilevelApp extends Application<IOutputComponent> {
    /** Regular expression pattern of a generated subplant specification file. */
    private static final Pattern SPEC_FILE_PATTERN = Pattern.compile("spec_[0-9]+\\.cif");

    /** Matrix debug output format. */
    private static final RealMatrixFormat MAT_DEBUG_FORMAT;

    static {
        NumberFormat valueFmt = NumberFormat.getIntegerInstance(Locale.US);
        MAT_DEBUG_FORMAT = new RealMatrixFormat("", "", "  ", "", "\n", " ", valueFmt);
    }

    /**
     * Application main method.
     *
     * @param args The command line arguments supplied to the application.
     */
    public static void main(String[] args) {
        MultilevelApp app = new MultilevelApp();
        app.run(args, true);
    }

    /** Constructor of the {@link MultilevelApp} class. */
    public MultilevelApp() {
        // Nothing to do.
    }

    /**
     * Constructor of the {@link MultilevelApp} class.
     *
     * @param streams The streams to use for input, output, warning, and error streams.
     */
    public MultilevelApp(AppStreams streams) {
        super(streams);
    }

    @Override
    protected OutputProvider<IOutputComponent> getProvider() {
        return new OutputProvider<>();
    }

    @Override
    protected int runInternal() {
        // Load the provided CIF specification.
        CifReader cifReader = new CifReader().init();
        Specification spec = cifReader.read();
        String absSpecPath = Paths.resolve(InputFileOption.getPath());
        if (isTerminationRequested()) {
            return 0;
        }

        // Specification transformations.
        new ElimComponentDefInst().transform(spec);
        new ElimSelf().transform(spec);

        // Remove/ignore I/O declarations, to increase the supported subset.
        RemoveIoDecls removeIoDecls = new RemoveIoDecls();
        removeIoDecls.transform(spec);
        if (removeIoDecls.haveAnySvgInputDeclarationsBeenRemoved()) {
            OutputProvider.warn("The specification contains CIF/SVG input declarations. These will be ignored.");
        }

        new SimplifyValuesOptimized().transform(spec);
        if (isTerminationRequested()) {
            return 0;
        }

        // Verify pre-conditions.
        checkSpec(spec, absSpecPath);
        if (isTerminationRequested()) {
            return 0;
        }

        // Figure 1 of Goorden 2020: Record relations.
        //
        // CIF to DMM transformation.
        CifRelations cifRelations = CifToDmm.transformToDmms(spec);
        if (isTerminationRequested()) {
            return 0;
        }

        if (DmmOutputFileOption.getDmmOutputFilePath() != null) {
            cifRelations.writeDmms(InputFileOption.getPath(), DmmOutputFileOption.getDmmOutputFilePath());
        }
        Dmm reqsPlantsDmm = cifRelations.relations; // Requirement-group rows against plant-groups columns.
        if (dodbg()) {
            dbg("Plant groups:");
            dbg(cifRelations.plantGroups.toString());
            dbg();
            dbg("Requirement groups:");
            dbg(cifRelations.requirementGroups.toString());
            dbg();
            dbg("Requirement / Plant relations:");
            dbg(cifRelations.relations.toString());
            dbg();
        }
        if (isTerminationRequested()) {
            return 0;
        }

        // Warn about requirements of requirement groups that don't do anything useful since they are not related to
        // any plant group.
        for (PositionObject posObj: cifRelations.getUselessRequirements()) {
            if (posObj instanceof Automaton) {
                warn("Requirement automaton \"%s\" has no relation to any plant element and does not affect behavior.",
                        getAbsName(posObj, false));
            } else if (posObj instanceof Invariant inv) {
                warn("Requirement invariant \"%s\" has no relation to any plant element and does not affect behavior.",
                        invToStr(inv, true));
            } else {
                throw new AssertionError("Unexpected kind of requirement found: \"" + posObj + "\".");
            }
        }

        // Figure 1 of Goorden 2020: Transform the relations.
        //
        // Convert to a plant groups DSM on requirement groups relations.
        // Do the standard T . T^-1 conversion, except here T is already transposed.
        RealMatrix unclusteredMatrix = reqsPlantsDmm.adjacencies.transpose().multiply(reqsPlantsDmm.adjacencies);
        dbg("Unclustered reqsPlants:");
        dbg(MAT_DEBUG_FORMAT.format(unclusteredMatrix));
        dbg();
        if (isTerminationRequested()) {
            return 0;
        }

        // Figure 1 of Goorden 2020: And cluster the DSM.
        dbg("--- Start of clustering --");
        idbg();
        ClusterInput clusteringInput = new ClusterInput(unclusteredMatrix, reqsPlantsDmm.columnLabels,
                OutputProvider.getDebugOutputStream());
        Dsm clusteredDsm = DsmClustering.flowBasedMarkovClustering(clusteringInput);
        ddbg();
        dbg("--- End of clustering --");
        dbg();

        // The cluster result contains the found cluster groups with original indices. For debugging however, it seems
        // useful to also dump the clustered DSM, to understand group information.
        dbg("Clustered DSM for reqsPlantsDmm (for information only, this data is not actually used):");
        dbg(MAT_DEBUG_FORMAT.format(clusteredDsm.adjacencies));
        dbg();
        if (isTerminationRequested()) {
            return 0;
        }

        // Figure 1 of Goorden 2020: Create the node tree.
        TreeNode rootNode = ComputeMultiLevelTree.transformCluster(clusteredDsm.rootGroup, unclusteredMatrix,
                reqsPlantsDmm.adjacencies);
        if (isTerminationRequested()) {
            return 0;
        }

        // Dump the multi-level tree.
        List<TreeNode> linearizedTree = rootNode.linearizeTree();
        for (TreeNode node: linearizedTree) {
            out("Index: %d", node.index);
            out("Plant groups: %s", node.plantGroups.isEmpty() ? "<none>" : node.plantGroups.toString());
            out("Req groups:   %s", node.requirementGroups.isEmpty() ? "<none>" : node.requirementGroups.toString());
            String childNodeNumbers = node.childNodes.stream().map(n -> String.valueOf(n.index))
                    .collect(Collectors.joining(", "));
            out("Child nodes: %s", childNodeNumbers.isEmpty() ? "<none>" : childNodeNumbers);
            out();
        }
        if (isTerminationRequested()) {
            return 0;
        }

        // Construct the partial specifications. The partial builder is re-used for each partial
        // specification.
        List<Specification> partialSpecs = listc(linearizedTree.size());

        PartialSpecsBuilder partialBuilder = new PartialSpecsBuilder(spec);
        for (TreeNode node: linearizedTree) {
            // Find the objects that should be in the partial specification of this node.
            List<PositionObject> neededObjects = list();
            for (int plantGrp: new BitSetIterator(node.plantGroups)) {
                neededObjects.addAll(cifRelations.getPlantsOfGroup(plantGrp));
            }
            for (int reqGrp: new BitSetIterator(node.requirementGroups)) {
                neededObjects.addAll(cifRelations.getRequirementsOfGroup(reqGrp));
            }
            if (isTerminationRequested()) {
                return 0;
            }

            // Construct the specification.
            Specification partialSpec = partialBuilder.createPartialSpecification(neededObjects);
            partialSpecs.add(partialSpec);
        }
        if (isTerminationRequested()) {
            return 0;
        }

        // Optionally write the partial specifications.
        String partialSpecsDir = PartialSpecsOutputDirectoryOption.getPath();
        if (partialSpecsDir != null) {
            String absCifDir = cifReader.getAbsDirPath();
            writePartialSpecs(partialSpecsDir, partialSpecs, absCifDir);
        }
        if (isTerminationRequested()) {
            return 0;
        }

        // TODO Implement.
        OutputProvider.warn("Multi-level synthesis not yet implemented.");
        return 0;
    }

    /**
     * Write the partial specifications to the provided directory.
     *
     * @param partialSpecsDir Directory to use for writing the partial specifications.
     * @param partialSpecs Created partial specifications.
     * @param absCifDir Absolute directory path containing the input CIF file.
     */
    private void writePartialSpecs(String partialSpecsDir, List<Specification> partialSpecs, String absCifDir) {
        // Get directory for storing the partial specifications.
        Path absDirPath = java.nio.file.Paths.get(Paths.resolve(partialSpecsDir));

        // In case the directory already exists, delete existing "spec_NNN.cif" entries.
        if (Files.isDirectory(absDirPath)) {
            try (Stream<Path> dirContent = Files.list(absDirPath)) {
                Predicate<String> matcher = SPEC_FILE_PATTERN.asMatchPredicate();
                dirContent.filter(p -> matcher.test(p.getFileName().toString())).forEach(p -> {
                    try {
                        Files.delete(p);
                    } catch (IOException ex) {
                        String msg = fmt("Failed to clean output directory \"%s\" for the partial specifications.",
                                partialSpecsDir);
                        throw new InputOutputException(msg, ex);
                    }
                });
            } catch (IOException ex) {
                String msg = fmt("Failed to clean output directory \"%s\" for the partial specifications.",
                        partialSpecsDir);
                throw new InputOutputException(msg, ex);
            }
        }

        // Create directory for storing the partial specifications, and any ancestor directories, if they don't exist
        // yet.
        try {
            Files.createDirectories(absDirPath);
        } catch (IOException ex) {
            String msg = fmt("Failed to create output directory \"%s\" for the partial specifications.",
                    partialSpecsDir);
            throw new InputOutputException(msg, ex);
        }

        // And write the output files.
        int specNumber = 1;
        for (Specification partialSpec: partialSpecs) {
            String outPath = Paths.join(Paths.resolve(partialSpecsDir),
                    "spec_" + makeFixedLengthNumberText(specNumber, partialSpecs.size()) + ".cif");
            CifWriter.writeCifSpec(partialSpec, outPath, absCifDir);
            specNumber++;
        }
        out("Wrote %d partial specification%s to directory \"%s\".", partialSpecs.size(),
                (partialSpecs.size() == 1) ? "" : "s", partialSpecsDir);
    }

    /**
     * Perform checking on the specification to decide if it is appropriate for multi-level synthesis.
     *
     * @param spec Specification to check.
     * @param absSpecPath The absolute local file system path to the CIF file to check.
     */
    public static void checkSpec(Specification spec, String absSpecPath) {
        CifPreconditionChecker checker = new MultiLevelPreChecker();
        checker.reportPreconditionViolations(spec, absSpecPath, "CIF multi-level synthesis");
    }

    /** CIF checker class to check pre-conditions of the multi-level synthesis. */
    private static class MultiLevelPreChecker extends CifPreconditionChecker {
        /** Constructor of the {@link MultiLevelPreChecker} class. */
        public MultiLevelPreChecker() {
            super(
                    // Constraints from CIF to DMM:

                    // Ensure there are no relations between elements hidden in initialization expressions.
                    new AutOnlyWithOneInitLocCheck(), //
                    new VarNoDiscWithMultiInitValuesCheck(), //
                    new VarDiscOnlyStaticEvalInitCheck(),

                    // Should have only plant and requirement automata.
                    new AutOnlySpecificSupKindsCheck(SupKind.PLANT, SupKind.REQUIREMENT),

                    // Need at least one plant element, to prevent empty DMMs.
                    // Both plant automata and input variables count as 'plant'.
                    new SpecHasPlantCheck(),

                    // Need at least one requirement element, to prevent empty DMMs.
                    // Both requirement automata and state/event exclusion invariants count as requirement.
                    new SpecHasRequirementCheck(),

                    // Only requirement state/event exclusion invariants in components are supported.
                    new InvNoSpecificInvsCheck() //
                            .disallow(KINDLESS, NoInvariantKind.ALL_KINDS, ALL_PLACES) //
                            .disallow(SUPERVISOR, NoInvariantKind.ALL_KINDS, ALL_PLACES) //
                            .disallow(PLANT, NoInvariantKind.ALL_KINDS, ALL_PLACES) //
                            .disallow(REQUIREMENT, STATE, ALL_PLACES) //
                            .disallow(NoInvariantSupKind.ALL_KINDS, NoInvariantKind.ALL_KINDS, LOCATIONS),

                    // Unsupported features.
                    new TypeNoSpecificTypesCheck(NoSpecificType.COMP_DEF_TYPES, NoSpecificType.COMP_TYPES)
                            .ignoreAnnotations(), //
                    new EventNoTauCheck(), //
                    new VarNoContinuousCheck(), //
                    new EqnNotAllowedCheck(),

                    // Constraints from data-based synthesis (approximately):

                    // No user-defined functions.
                    new FuncNoSpecificUserDefCheck(NoSpecificUserDefFunc.EXTERNAL, NoSpecificUserDefFunc.INTERNAL),

                    // Only allow events that are either controllable or uncontrollable.
                    new EventOnlyWithControllabilityCheck(),

                    // Don't allow channels.
                    new EventNoChannelsCheck(),

                    // Only allow non-negative integer values in expressions.
                    new TypeIntBoundsCheck(true, 0, null, null, null).ignoreAnnotations(),

                    // Only allow ranged integers, enumerations, and booleans.
                    new TypeNoSpecificTypesCheck( //
                            NoSpecificType.COMP_DEF_TYPES, //
                            NoSpecificType.COMP_TYPES, //
                            NoSpecificType.DICT_TYPES, //
                            NoSpecificType.DIST_TYPES, //
                            NoSpecificType.FUNC_TYPES, //
                            NoSpecificType.FUNC_TYPES_AS_DATA, //
                            NoSpecificType.INT_TYPES_RANGELESS, //
                            NoSpecificType.LIST_TYPES, //
                            NoSpecificType.REAL_TYPES, //
                            NoSpecificType.SET_TYPES, //
                            NoSpecificType.STRING_TYPES, //
                            NoSpecificType.TUPLE_TYPES, //
                            NoSpecificType.VOID_TYPES)
                                    .ignoreAnnotations(),

                    // Disallow unsupported expressions.
                    new ExprNoSpecificExprsCheck( //
                            NoSpecificExpr.FUNC_REFS_USER_DEF, //
                            NoSpecificExpr.CAST_EXPRS, //
                            NoSpecificExpr.COMP_REFS, //
                            NoSpecificExpr.COMP_PARAM_REFS, //
                            NoSpecificExpr.CONT_VAR_REFS, //
                            NoSpecificExpr.DICT_LITS, //
                            NoSpecificExpr.TUPLE_FIELD_REFS, //
                            NoSpecificExpr.FUNC_CALLS, //
                            NoSpecificExpr.LIST_LITS, //
                            NoSpecificExpr.PROJECTION_EXPRS, //
                            NoSpecificExpr.REAL_LITS, //
                            NoSpecificExpr.RECEIVE_EXPRS, //
                            NoSpecificExpr.SET_LITS, //
                            NoSpecificExpr.SLICE_EXPRS, //
                            NoSpecificExpr.STRING_LITS, //
                            NoSpecificExpr.TIME_VAR_REFS, //
                            NoSpecificExpr.TUPLE_LITS)
                                    .ignoreAnnotations(),

                    // Only allow inversion unary operator.
                    new ExprNoSpecificUnaryExprsCheck( //
                            NoSpecificUnaryOp.NEGATE, //
                            NoSpecificUnaryOp.SAMPLE)
                                    .ignoreAnnotations(),

                    // Disallow all non-supported binary operators.
                    new ExprNoSpecificBinaryExprsCheck( //
                            NoSpecificBinaryOp.ADDITION_INTS_RANGELESS, //
                            NoSpecificBinaryOp.ADDITION_REALS, //
                            NoSpecificBinaryOp.ADDITION_LISTS, //
                            NoSpecificBinaryOp.ADDITION_STRINGS, //
                            NoSpecificBinaryOp.ADDITION_DICTS, //
                            NoSpecificBinaryOp.CONJUNCTION_SETS, //
                            NoSpecificBinaryOp.DISJUNCTION_SETS, //
                            NoSpecificBinaryOp.DIVISION, //
                            NoSpecificBinaryOp.ELEMENT_OF, //
                            NoSpecificBinaryOp.EQUAL_DICT, //
                            NoSpecificBinaryOp.EQUAL_INT_RANGELESS, //
                            NoSpecificBinaryOp.EQUAL_LIST, //
                            NoSpecificBinaryOp.EQUAL_REAL, //
                            NoSpecificBinaryOp.EQUAL_SET, //
                            NoSpecificBinaryOp.EQUAL_STRING, //
                            NoSpecificBinaryOp.EQUAL_TUPLE, //
                            NoSpecificBinaryOp.GREATER_EQUAL_INTS_RANGELESS, //
                            NoSpecificBinaryOp.GREATER_EQUAL_REALS, //
                            NoSpecificBinaryOp.GREATER_THAN_INTS_RANGELESS, //
                            NoSpecificBinaryOp.GREATER_THAN_REALS, //
                            NoSpecificBinaryOp.INTEGER_DIVISION_INTS_RANGELESS, //
                            NoSpecificBinaryOp.LESS_EQUAL_INTS_RANGELESS, //
                            NoSpecificBinaryOp.LESS_EQUAL_REALS, //
                            NoSpecificBinaryOp.LESS_THAN_INTS_RANGELESS, //
                            NoSpecificBinaryOp.LESS_THAN_REALS, //
                            NoSpecificBinaryOp.MODULUS_INTS_RANGELESS, //
                            NoSpecificBinaryOp.MULTIPLICATION_INTS_RANGELESS, //
                            NoSpecificBinaryOp.MULTIPLICATION_REALS, //
                            NoSpecificBinaryOp.SUBSET, //
                            NoSpecificBinaryOp.SUBTRACTION_INTS_RANGELESS, //
                            NoSpecificBinaryOp.SUBTRACTION_REALS, //
                            NoSpecificBinaryOp.SUBTRACTION_LISTS, //
                            NoSpecificBinaryOp.SUBTRACTION_SETS, //
                            NoSpecificBinaryOp.SUBTRACTION_DICTS, //
                            NoSpecificBinaryOp.UNEQUAL_DICT, //
                            NoSpecificBinaryOp.UNEQUAL_INT_RANGELESS, //
                            NoSpecificBinaryOp.UNEQUAL_LIST, //
                            NoSpecificBinaryOp.UNEQUAL_REAL, //
                            NoSpecificBinaryOp.UNEQUAL_SET, //
                            NoSpecificBinaryOp.UNEQUAL_STRING, //
                            NoSpecificBinaryOp.UNEQUAL_TUPLE)
                                    .ignoreAnnotations(),

                    // Conditional updates (if updates), multi-assignments, and partial variable assignments are not
                    // supported.
                    new EdgeOnlySimpleAssignmentsCheck(),

                    // Omitted data-based synthesis checks, as they are already covered by the CIF to DMM conversion
                    // checks:
                    // - Disallow supervisor and kind-less invariants.
                    // - Disallow supervisor and kind-less automata.
                    // - Disallow continuous variables.
                    // - Have at least one plant automaton.

                    // Some missing checks exist in data-based synthesis, but they can only be checked as part of
                    // performing data-based synthesis.

                    // Constraints of partial specifications:

                    // Disallow initialization and marker predicates outside locations.
                    new CompNoInitPredsCheck(true), //
                    new CompNoMarkerPredsCheck(true)
            //
            );
        }
    }

    @Override
    public String getAppName() {
        return "CIF multi-level synthesis";
    }

    @Override
    public String getAppDescription() {
        return "Performs synthesis by making several smaller co-operating supervisors.";
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected OptionCategory getAllOptions() {
        OptionCategory generalCat = getGeneralOptionCategory();

        List<Option> programOpts = List.of(Options.getInstance(InputFileOption.class),
                Options.getInstance(DmmOutputFileOption.class),
                Options.getInstance(PartialSpecsOutputDirectoryOption.class));
        OptionCategory programCat = new OptionCategory("Multi-level synthesis", "Multi-level synthesis options.",
                List.of(), programOpts);

        return new OptionCategory("CIF Multi-level Synthesis Options",
                "All options for the CIF multi-level synthesis tool.", List.of(generalCat, programCat), List.of());
    }
}
