//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.explorer.runtime;

import static org.eclipse.escet.common.java.Lists.list;

import java.util.Collections;
import java.util.List;

import org.eclipse.escet.cif.common.CifInvariantUtils;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.SupKind;

/** Invariants collected for a component or location. */
public class CollectedInvariants {
    /** Collected {@link SupKind#NONE} invariants, or {@code null} if empty. */
    private List<Invariant> noneInvariants = null;

    /** Collected {@link SupKind#PLANT} invariants, or {@code null} if empty. */
    private List<Invariant> plantInvariants = null;

    /** Collected {@link SupKind#REQUIREMENT} invariants, or {@code null} if empty. */
    private List<Invariant> reqInvariants = null;

    /** Collected {@link SupKind#SUPERVISOR} invariants, or {@code null} if empty. */
    private List<Invariant> supInvariants = null;

    /**
     * Add an invariant to a location.
     *
     * @param inv Invariant to add.
     */
    public void add(Invariant inv) {
        switch (CifInvariantUtils.getSupKind(inv)) {
            case NONE:
                if (noneInvariants == null) {
                    noneInvariants = list();
                }
                noneInvariants.add(inv);
                break;

            case PLANT:
                if (plantInvariants == null) {
                    plantInvariants = list();
                }
                plantInvariants.add(inv);
                break;

            case REQUIREMENT:
                if (reqInvariants == null) {
                    reqInvariants = list();
                }
                reqInvariants.add(inv);
                break;

            case SUPERVISOR:
                if (supInvariants == null) {
                    supInvariants = list();
                }
                supInvariants.add(inv);
                break;
        }
    }

    /**
     * Return the collected invariants without a supervisory kind.
     *
     * @return The collected invariants without a supervisory kind.
     */
    public List<Invariant> getNoneInvariants() {
        if (noneInvariants == null) {
            return Collections.emptyList();
        }
        return noneInvariants;
    }

    /**
     * Return the collected plant invariants.
     *
     * @return The collected plant invariants.
     */
    public List<Invariant> getPlantInvariants() {
        if (plantInvariants == null) {
            return Collections.emptyList();
        }
        return plantInvariants;
    }

    /**
     * Return the collected requirement invariants.
     *
     * @return The collected requirement invariants.
     */
    public List<Invariant> getRequirementInvariants() {
        if (reqInvariants == null) {
            return Collections.emptyList();
        }
        return reqInvariants;
    }

    /**
     * Return the collected supervisor invariants.
     *
     * @return The collected supervisor invariants.
     */
    public List<Invariant> getSupervisorInvariants() {
        if (supInvariants == null) {
            return Collections.emptyList();
        }
        return supInvariants;
    }
}
