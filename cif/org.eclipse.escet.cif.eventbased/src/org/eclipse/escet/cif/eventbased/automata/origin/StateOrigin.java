//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.eventbased.automata.origin;

import static org.eclipse.escet.common.emf.EMFHelper.deepclone;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Sets.list2set;

import java.util.Collections;
import java.util.List;

import org.eclipse.escet.cif.eventbased.automata.Location;
import org.eclipse.escet.cif.eventbased.builders.State;
import org.eclipse.escet.cif.metamodel.cif.annotations.Annotation;
import org.eclipse.escet.common.java.Assert;

/** Origin is a state. */
public class StateOrigin extends Origin {
    /** State representing the location. */
    public final State state;

    /**
     * Constructor of the {@link StateOrigin} class.
     *
     * @param state State representing the location.
     */
    public StateOrigin(State state) {
        this.state = state;
    }

    @Override
    public List<Annotation> createStateAnnos() {
        List<Annotation> annos = null;

        for (Location loc: state.locs) {
            // Combine result so far, with the annotations for this location.
            List<Annotation> locAnnos = loc.createStateAnnos();
            if (annos == null) {
                // First location. Use the annotations of that location.
                annos = locAnnos;
            } else {
                // Non-first location. Product of current annotations and location annotations.
                List<Annotation> newAnnos = listc(annos.size() * locAnnos.size());
                for (Annotation anno: annos) {
                    for (Annotation locAnno: locAnnos) {
                        // Add the combined annotation.
                        Annotation newAnno = deepclone(anno);
                        newAnno.getArguments().addAll(deepclone(locAnno.getArguments()));
                        newAnnos.add(newAnno);

                        // Ensure that there are no duplicate arguments.
                        List<String> argNames = newAnno.getArguments().stream().map(a -> a.getName()).toList();
                        Assert.areEqual(argNames.size(), list2set(argNames).size());
                    }
                }
                annos = newAnnos;
            }
        }

        return (annos == null) ? Collections.emptyList() : annos;
    }

    @Override
    public void toString(StringBuilder sb, int flags) {
        Assert.check((flags & (ALLOW_STATE | ADD_PREFIX)) == (ALLOW_STATE | ADD_PREFIX));
        sb.append("state ");

        boolean first = true;
        for (Location loc: state.locs) {
            if (!first) {
                sb.append(", ");
            }
            first = false;
            loc.origin.toString(sb, flags & ALLOW_CIF_LOCATION);
        }
    }
}
