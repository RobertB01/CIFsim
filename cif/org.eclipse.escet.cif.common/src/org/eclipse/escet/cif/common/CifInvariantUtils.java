//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.common;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.SupKind;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.common.java.Assert;

/** CIF invariant utility methods. */
public class CifInvariantUtils {
    /** Constructor for the {@link CifInvariantUtils} class. */
    private CifInvariantUtils() {
        // Static class.
    }

    /**
     * Returns the supervisory kind of the given invariant. Can handle both explicit kinds and implicit kinds (inherited
     * from automata).
     *
     * @param inv The invariant.
     * @return The supervisory kind of the invariant.
     */
    public static SupKind getSupKind(Invariant inv) {
        // Explicit kind.
        if (inv.getSupKind() != SupKind.NONE) {
            return inv.getSupKind();
        }

        // Get inheritable/implicit kind.
        SupKind inheritKind = getInheritableSupKind(inv);
        if (inheritKind != null) {
            return inheritKind;
        }

        // No explicit/implicit kind.
        return SupKind.NONE;
    }

    /**
     * Does the invariant have an explicit supervisory kind?
     *
     * @param inv The invariant.
     * @return {@code true} if the invariant has an explicit supervisory kind, {@code false} otherwise.
     */
    public static boolean hasExplicitSupKind(Invariant inv) {
        return inv.getSupKind() != SupKind.NONE;
    }

    /**
     * Makes the supervisory kind of the invariant explicit, if applicable. Updates the supervisory kind of the given
     * invariant, if the invariant does not have an explicit kind, but inherits an implicit kind from an automaton. That
     * is, if the invariant has an implicit kind, that kind is set as the explicit kind of the invariant.
     *
     * @param inv The invariant. May be modified in-place.
     */
    public static void makeSupKindExplicit(Invariant inv) {
        // Kind already explicit.
        if (inv.getSupKind() != SupKind.NONE) {
            return;
        }

        // Get inheritable/implicit kind.
        SupKind inheritKind = getInheritableSupKind(inv);
        if (inheritKind != null) {
            inv.setSupKind(inheritKind);
        }
    }

    /**
     * Makes the supervisory kind of the invariant implicit, if possible. Updates the kind of the given invariant, if
     * the invariant has an explicit kind, and that kind is the same as the implicit kind it would have if the invariant
     * would not have an explicit kind.
     *
     * @param inv The invariant. May be modified in-place.
     */
    public static void makeSupKindImplicit(Invariant inv) {
        // Kind already implicit.
        if (inv.getSupKind() == SupKind.NONE) {
            return;
        }

        // Get inheritable/implicit kind.
        SupKind inheritKind = getInheritableSupKind(inv);

        // Make implicit if possible.
        if (inheritKind != inv.getSupKind()) {
            return;
        }
        inv.setSupKind(SupKind.NONE);
    }

    /**
     * Returns the inheritable supervisory kind of the invariant, if applicable/available.
     *
     * @param inv The invariant.
     * @return The inheritable supervisory kind, or {@code null}.
     */
    public static SupKind getInheritableSupKind(Invariant inv) {
        // Get surrounding component.
        EObject obj = inv;
        while (!(obj instanceof ComplexComponent)) {
            obj = obj.eContainer();
        }

        // Groups have no inheritable kind.
        if (obj instanceof Group) {
            return null;
        }

        // Automata may have an inheritable kind.
        Assert.check(obj instanceof Automaton);
        Automaton aut = (Automaton)obj;
        if (aut.getKind() == SupKind.NONE) {
            return null;
        }
        return aut.getKind();
    }
}
