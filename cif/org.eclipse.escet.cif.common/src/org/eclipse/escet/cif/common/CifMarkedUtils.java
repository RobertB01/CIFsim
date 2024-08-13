//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBinaryExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBoolType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newLocationExpression;
import static org.eclipse.escet.common.emf.EMFHelper.deepclone;
import static org.eclipse.escet.common.java.Lists.concat;
import static org.eclipse.escet.common.java.Lists.listc;

import java.util.List;

import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.LocationExpression;

/** CIF marker predicates utility methods. */
public class CifMarkedUtils {
    /** Constructor for the {@link CifMarkedUtils} class. */
    private CifMarkedUtils() {
        // Static class.
    }

    /**
     * Get the marker predicate of a CIF specification.
     *
     * @param spec The CIF specification.
     * @return The marker predicate.
     */
    public static Expression getMarked(Specification spec) {
        List<Expression> autMarkeds = CifCollectUtils.getComplexComponentsStream(spec)
                .filter(c -> c instanceof Automaton).map(aut -> getMarked((Automaton)aut)).toList();
        List<Expression> compMarkeds = CifCollectUtils.getComplexComponentsStream(spec)
                .flatMap(c -> c.getMarkeds().stream()).map(p -> deepclone(p)).toList();
        return CifValueUtils.createConjunction(concat(autMarkeds, compMarkeds), true);
    }

    /**
     * Get the marker predicate of a CIF automaton.
     *
     * @param aut The CIF automaton.
     * @return The marker predicate.
     */
    public static Expression getMarked(Automaton aut) {
        // Create a conjunction of the markings of the locations of the automaton.
        List<Expression> locMarkeds = listc(aut.getLocations().size());
        for (Location loc: aut.getLocations()) {
            // Get marker predicate of the location. If the automaton has at least two locations, then make it
            // conditional on being in the location.
            Expression marked = getMarked(loc);
            if (aut.getLocations().size() >= 2) {
                // Create 'loc => marked'.
                LocationExpression locExpr = newLocationExpression();
                locExpr.setLocation(loc);
                locExpr.setType(newBoolType());

                BinaryExpression binExpr = newBinaryExpression();
                binExpr.setOperator(BinaryOperator.IMPLICATION);
                binExpr.setLeft(locExpr);
                binExpr.setRight(marked);
                binExpr.setType(newBoolType());

                marked = binExpr;
            }
            locMarkeds.add(marked);
        }
        return CifValueUtils.createConjunction(locMarkeds, true);
    }

    /**
     * Get the marker predicate of a CIF location. Note that it is not made conditional on the location.
     *
     * @param loc The CIF location.
     * @return The marker predicate.
     */
    public static Expression getMarked(Location loc) {
        // No marker predicates in a location means 'false'.
        if (loc.getMarkeds().isEmpty()) {
            return CifValueUtils.makeFalse();
        }

        // At least one marker predicate. Make a conjunction.
        List<Expression> preds = loc.getMarkeds().stream().map(p -> deepclone(p)).toList();
        return CifValueUtils.createConjunction(preds, true);
    }
}
