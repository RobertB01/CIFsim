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

import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.io.AppStream;
import org.eclipse.escet.common.app.framework.io.FileAppStream;
import org.eclipse.escet.common.dsm.Dmm;

/**
 * Class for storing the structure of plant groups and requirement groups, and for relations between these groups.
 *
 * <p>
 * The labels of the DMMs also contain the associated CIF element where applicable for easier further processing. See
 * also in {@link Labels}.
 * </p>
 */
public class CifRelations {
    /** Relations from plant-group columns to plant element rows. */
    public final Dmm plantGroups;

    /** Relations from requirement-group columns to requirement element rows. */
    public final Dmm requirementGroups;

    /** Relations from plant-group columns to requirement-group rows. */
    public final Dmm relations;

    /**
     * Constructor of the {@link CifRelations} class.
     *
     * @param plantGroups Relations from plant-group columns to plant element rows.
     * @param requirementGroups Relations from requirement-group columns to requirement element rows.
     * @param relations Relations from plant-group columns to requirement-group rows.
     */
    public CifRelations(Dmm plantGroups, Dmm requirementGroups, Dmm relations) {
        this.plantGroups = plantGroups;
        this.requirementGroups = requirementGroups;
        this.relations = relations;
    }

    /**
     * Write the DMMs to an output file.
     *
     * @param cifPath Path of the CIF specification that was transformed.
     * @param outPath Destination of the written output.
     */
    public void writeDmms(String cifPath, String outPath) {
        String absOutPath = Paths.resolve(outPath);

        try (AppStream stream = new FileAppStream(outPath, absOutPath)) {
            stream.printfln("DMMs of the \"%s\" CIF specification.", cifPath);
            stream.println();
            stream.printfln("Plant groups:%n%s", plantGroups.toString());
            stream.println();
            stream.printfln("Requirement groups:%n%s", requirementGroups.toString());
            stream.println();
            stream.printfln("Plant-groups/requirement-groups relations:%n%s", relations.toString());
        }
    }
}
