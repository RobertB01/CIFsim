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

import java.util.List;
import java.util.Set;

import org.eclipse.escet.cif.eventbased.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.annotations.Annotation;
import org.eclipse.escet.common.java.Assert;

/** Origin is a collection of locations. */
public class LocationSetOrigin extends Origin {
    /** Originating locations. */
    public final Set<Location> locSet;

    /**
     * Constructor of the {@link LocationSetOrigin} class.
     *
     * @param locSet Originating collection of locations.
     */
    public LocationSetOrigin(Set<Location> locSet) {
        this.locSet = locSet;
    }

    @Override
    public List<Annotation> createStateAnnos() {
        return locSet.stream().flatMap(loc -> loc.createStateAnnos().stream()).toList();
    }

    @Override
    public void toString(StringBuilder sb, int flags) {
        Assert.check((flags & (ALLOW_PARTITION | ADD_PREFIX)) == (ALLOW_PARTITION | ADD_PREFIX));
        sb.append("partition ");

        boolean first = true;
        for (Location loc: locSet) {
            if (!first) {
                sb.append(", ");
            }
            first = false;
            loc.origin.toString(sb, flags & ALLOW_CIF_LOCATION);
        }
    }
}
