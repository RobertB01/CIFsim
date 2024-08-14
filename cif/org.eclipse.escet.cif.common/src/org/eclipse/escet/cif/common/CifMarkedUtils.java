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
        boolean makeConditional = aut.getLocations().size() >= 2;
        List<Expression> locMarkeds = aut.getLocations().stream().map(loc -> getMarked(loc, makeConditional)).toList();
        return CifValueUtils.createConjunction(locMarkeds, true);
    }

    /**
     * Get the marker predicate of a CIF location.
     *
     * @param loc The CIF location.
     * @param makeConditional Whether to only consider the marker predicates of the location ({@code false}) or
     *     additionally also make those dependent on being in the location ({@code true}).
     * @return The marker predicate.
     */
    public static Expression getMarked(Location loc, boolean makeConditional) {
        // Get marker predicate of the location.
        Expression marked;
        if (loc.getMarkeds().isEmpty()) {
            marked = CifValueUtils.makeFalse();
        } else {
            List<Expression> preds = loc.getMarkeds().stream().map(p -> deepclone(p)).toList();
            marked = CifValueUtils.createConjunction(preds, true);
        }

        // If requested, make it conditional on being in the location.
        if (makeConditional) {
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

        // Return the marker predicate.
        return marked;
    }
}
