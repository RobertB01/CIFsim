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

package org.eclipse.escet.cif.multilevel.ciftodmm;

import static org.eclipse.escet.cif.common.checkers.checks.invcheck.NoInvariantKind.STATE;
import static org.eclipse.escet.cif.common.checkers.checks.invcheck.NoInvariantPlaceKind.ALL_PLACES;
import static org.eclipse.escet.cif.common.checkers.checks.invcheck.NoInvariantPlaceKind.LOCATIONS;
import static org.eclipse.escet.cif.common.checkers.checks.invcheck.NoInvariantSupKind.KINDLESS;
import static org.eclipse.escet.cif.common.checkers.checks.invcheck.NoInvariantSupKind.PLANT;
import static org.eclipse.escet.cif.common.checkers.checks.invcheck.NoInvariantSupKind.REQUIREMENT;
import static org.eclipse.escet.cif.common.checkers.checks.invcheck.NoInvariantSupKind.SUPERVISOR;

import java.util.BitSet;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.eclipse.escet.cif.common.CifPreconditionChecker;
import org.eclipse.escet.cif.common.checkers.checks.AutOnlySpecificSupKindsCheck;
import org.eclipse.escet.cif.common.checkers.checks.EqnNotAllowedCheck;
import org.eclipse.escet.cif.common.checkers.checks.EventNoTauCheck;
import org.eclipse.escet.cif.common.checkers.checks.InvNoSpecificInvsCheck;
import org.eclipse.escet.cif.common.checkers.checks.TypeNoSpecificTypesCheck;
import org.eclipse.escet.cif.common.checkers.checks.TypeNoSpecificTypesCheck.NoSpecificType;
import org.eclipse.escet.cif.common.checkers.checks.VarNoContinuousCheck;
import org.eclipse.escet.cif.common.checkers.checks.invcheck.NoInvariantKind;
import org.eclipse.escet.cif.common.checkers.checks.invcheck.NoInvariantSupKind;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.SupKind;
import org.eclipse.escet.common.app.framework.AppEnv;
import org.eclipse.escet.common.dsm.Dmm;
import org.eclipse.escet.common.dsm.Label;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.BitSetIterator;
import org.eclipse.escet.common.java.Lists;

/** Construct a set of DMMs to describe multi-level synthesis relations in a CIF specification. */
public class CifToDmm {
    /** Constructor of the static {@link CifToDmm} class. */
    private CifToDmm() {
        // Static class.
    }

    /**
     * Perform checking on the specification to decide if it appropriate for the DMM construction.
     *
     * @param spec Specification to check.
     */
    public static void checkSpec(Specification spec) {
        CifPreconditionChecker checker = new CifToDmmPreChecker();
        checker.reportPreconditionViolations(spec, "CIF to DMM transformation");
    }

    /** CIF checker class to check pre-conditions of the CIF to DMM transformation. */
    private static class CifToDmmPreChecker extends CifPreconditionChecker {
        /** Constructor of the {@link CifToDmm.CifToDmmPreChecker} class. */
        public CifToDmmPreChecker() {
            super(
                    // Should have only plant and requirement automata.
                    new AutOnlySpecificSupKindsCheck(SupKind.PLANT, SupKind.REQUIREMENT), //

                    // Need at least one plant element, to prevent empty DMMs.
                    // Both plant automata and input variables count as 'plant'.
                    new SpecHasPlantCheck(),

                    // Need at least one requirement element, to prevent empty DMMs.
                    // Both requirement automata and state/event exclusion invariants count as requirement.
                    new SpecHasRequirementCheck(),

                    // Only requirement state/event exclusion invariants in components are supported.
                    new InvNoSpecificInvsCheck()
                            .disallow(KINDLESS, NoInvariantKind.ALL_KINDS, ALL_PLACES) //
                            .disallow(SUPERVISOR, NoInvariantKind.ALL_KINDS, ALL_PLACES) //
                            .disallow(PLANT, NoInvariantKind.ALL_KINDS, ALL_PLACES) //
                            .disallow(REQUIREMENT, STATE, ALL_PLACES) //
                            .disallow(NoInvariantSupKind.ALL_KINDS, NoInvariantKind.ALL_KINDS, LOCATIONS),

                    // Unsupported features.
                    new TypeNoSpecificTypesCheck(NoSpecificType.COMP_DEF_TYPES, NoSpecificType.COMP_TYPES), //
                    new EventNoTauCheck(), // TODO Could be ignored in the CIF to DMM transformation.
                    new VarNoContinuousCheck(), //
                    new EqnNotAllowedCheck() //
            );
        }
    }

    /**
     * Convert the CIF specification to a set of DMMs.
     *
     * <p>
     * Specification is assumed to be within the boundaries of the {@link #checkSpec} conditions.
     * </p>
     *
     * @param spec Specification to transformation.
     * @return The resulting DMMs. May be {@code null} if case termination is requested.
     */
    public static CifRelations transformToDmms(Specification spec) {
        // Construct a new collector and fill it with the relations from the given specification.
        RelationsCollector collector = new RelationsCollector();
        collector.collect(spec);
        if (AppEnv.isTerminationRequested()) {
            return null;
        }

        // Construct plant groups and requirement groups, and store them in DMMs.
        //
        // Group columns to element rows.
        List<OwnedAndAccessedElements> plantGroups = collector.computePlantGroups();
        List<OwnedAndAccessedElements> requirementGroups = collector.computeRequirementGroups();
        Dmm plantGroupsDmm = makeGroupDmm(collector, plantGroups, Labels::makePlantGroupLabel);
        Dmm requirementGroupsDmm = makeGroupDmm(collector, requirementGroups, Labels::makeRequirementGroupLabel);

        // Construct the plant-groups / requirement-groups DMM.
        //
        // Plant-group columns to requirement-group rows.
        List<BitSet> plantRelations = plantGroups.stream().map(pg -> pg.getRelations())
                .collect(Collectors.toCollection(Lists::list));
        List<BitSet> requirementRelations = requirementGroups.stream().map(rg -> rg.getRelations())
                .collect(Collectors.toCollection(Lists::list));

        int numColumns = plantRelations.size();
        int numRows = requirementRelations.size();
        Label[] columnLabels = IntStream.range(0, numColumns).mapToObj(Labels::makePlantGroupLabel)
                .toArray(Label[]::new);
        Label[] rowLabels = IntStream.range(0, numRows).mapToObj(Labels::makeRequirementGroupLabel)
                .toArray(Label[]::new);

        RealMatrix values = new BlockRealMatrix(numRows, numColumns);
        for (int row = 0; row < numRows; row++) {
            BitSet rowRelations = requirementRelations.get(row);
            for (int col = 0; col < numColumns; col++) {
                BitSet colRelations = plantRelations.get(col);
                values.setEntry(row, col, rowRelations.intersects(colRelations) ? 1 : 0);
            }
        }
        Dmm plantGrpsRequirementGrpsDmm = new Dmm(values, rowLabels, columnLabels);

        // Store everything in a single object and give it to the caller.
        return new CifRelations(plantGroupsDmm, requirementGroupsDmm, plantGrpsRequirementGrpsDmm);
    }

    /**
     * Construct a DMM to define groups.
     *
     * <p>
     * Groups are stored column-wise, elements of the group are stored row-wise.
     * </p>
     *
     * @param collector Collected relations in the specification.
     * @param groups Groups that must be stored in the DMM.
     * @param labelFunc Function that constructs each group label.
     * @return The created DMM.
     */
    private static Dmm makeGroupDmm(RelationsCollector collector, List<OwnedAndAccessedElements> groups,
            Function<Integer, Label> labelFunc)
    {
        int numColumns = groups.size();
        int numRows = groups.stream().collect(Collectors.summingInt(grp -> grp.groupElements.cardinality()));

        Label[] columnLabels = new Label[numColumns];
        Label[] rowLabels = new Label[numRows];
        RealMatrix dmmValues = new BlockRealMatrix(numRows, numColumns);

        int row = 0;
        for (int col = 0; col < numColumns; col++) {
            OwnedAndAccessedElements group = groups.get(col);
            columnLabels[col] = labelFunc.apply(col);

            for (int groupElement: new BitSetIterator(group.groupElements)) {
                rowLabels[row] = Labels.makeLabel(collector.getElement(groupElement));
                dmmValues.setEntry(row, col, 1);
                row++;
            }
        }
        Assert.areEqual(numRows, row);
        return new Dmm(dmmValues, rowLabels, columnLabels);
    }
}
