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

package org.eclipse.escet.cif.eventbased.apps.conversion;

import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newAnnotation;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newAnnotationArgument;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newStringExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newStringType;
import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.eventbased.automata.origin.Origin;
import org.eclipse.escet.cif.metamodel.cif.annotations.Annotation;
import org.eclipse.escet.cif.metamodel.cif.annotations.AnnotationArgument;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.common.java.Assert;

/** Origin is a location in a CIF automaton. */
public class CifOrigin extends Origin {
    /** CIF location. */
    public final Location cifLoc;

    /**
     * Constructor of the {@link CifOrigin} class.
     *
     * @param cifLoc Location in the CIF automaton.
     */
    public CifOrigin(Location cifLoc) {
        this.cifLoc = cifLoc;
    }

    @Override
    public List<Annotation> createStateAnnos() {
        Automaton cifAut = (Automaton)cifLoc.eContainer();
        Assert.notNull(cifAut);
        String autName = CifTextUtils.getAbsName(cifAut, false);
        String locName = (cifLoc.getName() == null) ? "*" : cifLoc.getName();
        Expression value = newStringExpression(null, newStringType(), locName);
        AnnotationArgument arg = newAnnotationArgument(autName, null, value);
        Annotation anno = newAnnotation(list(arg), "state", null);
        return list(anno);
    }

    @Override
    public void toString(StringBuilder sb, int flags) {
        if ((flags & ADD_PREFIX) != 0) {
            sb.append("location ");
        }
        Assert.check((flags & ALLOW_CIF_LOCATION) != 0);

        // Sort-of copy of 'CifTextUtils.getLocationText(cifLoc)', but changed
        // to remove the "location" prefix and the quotes.
        if (cifLoc.getName() != null) {
            sb.append("\"");
            sb.append(CifTextUtils.getAbsName(cifLoc));
            sb.append("\"");
            return;
        }

        // Nameless single location in an automaton.
        EObject parent = cifLoc.eContainer();
        Assert.check(parent instanceof Automaton);
        Automaton aut = (Automaton)parent;
        sb.append("\"");
        sb.append(CifTextUtils.getAbsName(aut));
        sb.append(".*\"");
    }
}
