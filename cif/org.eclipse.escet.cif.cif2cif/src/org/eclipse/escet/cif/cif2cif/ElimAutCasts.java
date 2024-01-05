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

package org.eclipse.escet.cif.cif2cif;

import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBoolType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newElifExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newIfExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newLocationExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newStringExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newStringType;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.last;

import java.util.List;

import org.eclipse.escet.cif.common.CifLocationUtils;
import org.eclipse.escet.cif.common.CifScopeUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.expressions.CastExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ElifExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IfExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.LocationExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.StringExpression;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.ComponentType;
import org.eclipse.escet.cif.metamodel.java.CifWalker;
import org.eclipse.escet.common.emf.EMFHelper;
import org.eclipse.escet.common.java.Assert;

/**
 * In-place transformation that replaces casts of automata to strings, by 'if' expressions, using the locations as
 * guards, and string literals containing the location names as values.
 *
 * <p>
 * All casts of automata to strings, using either implicit automaton 'self' references or explicit automata references,
 * are eliminated. If the referred automaton has only a single location, the cast is replaced by a string literal with
 * the name of that location. If the automaton has multiple locations, the cast is replaced by an 'if' expression, using
 * the locations of the automaton as guards, and string literals containing the location names as values. For nameless
 * locations the string literal "*" is used.
 * </p>
 *
 * <p>
 * Precondition: Specifications with component definitions/instantiations are currently not supported.
 * </p>
 *
 * <p>
 * This transformation generates 'if' expression guards with location reference expression. To eliminate them, use the
 * {@link ElimLocRefExprs} transformation after this transformation.
 * </p>
 */
public class ElimAutCasts extends CifWalker implements CifToCifTransformation {
    @Override
    public void transform(Specification spec) {
        // Check no component definition/instantiation precondition.
        if (CifScopeUtils.hasCompDefInst(spec)) {
            String msg = "Eliminating casts of automata to strings for a CIF specification with component definitions "
                    + "is currently not supported.";
            throw new CifToCifPreconditionException(msg);
        }

        // Perform actual transformation.
        walkSpecification(spec);
    }

    @Override
    protected void walkCastExpression(CastExpression castExpr) {
        // Skip casts on anything other than automaton (self) references.
        Expression child = castExpr.getChild();
        if (!CifTypeUtils.isAutRefExpr(child)) {
            super.walkCastExpression(castExpr);
            return;
        }

        // Get automaton and locations.
        CifType ctype = child.getType();
        Assert.check(ctype instanceof ComponentType);
        Component comp = ((ComponentType)ctype).getComponent();
        Assert.check(comp instanceof Automaton);
        Automaton aut = (Automaton)comp;
        List<Location> locs = aut.getLocations();

        // Special case for single location, no 'if' expression needed.
        if (locs.size() == 1) {
            // Replace cast expression by string literal.
            StringExpression strExpr = createStringLiteral(first(locs));
            EMFHelper.updateParentContainment(castExpr, strExpr);
            return;
        }

        // Create 'if' expression.
        IfExpression ifExpr = newIfExpression();
        ifExpr.setType(newStringType());

        // Set 'then' for first location.
        ifExpr.getGuards().add(createGuard(first(locs)));
        ifExpr.setThen(createStringLiteral(first(locs)));

        // Add 'elif' for all locations, except for first and last ones.
        List<ElifExpression> elifs = ifExpr.getElifs();
        for (int i = 1; i < locs.size() - 1; i++) {
            ElifExpression elifExpr = newElifExpression();
            elifs.add(elifExpr);

            Location loc = locs.get(i);
            elifExpr.getGuards().add(createGuard(loc));
            elifExpr.setThen(createStringLiteral(loc));
        }

        // Set 'else' for last location.
        ifExpr.setElse(createStringLiteral(last(locs)));

        // Replace expression.
        EMFHelper.updateParentContainment(castExpr, ifExpr);
    }

    /**
     * Creates a new location reference expression for the given location, to use as guard of the 'if' expression.
     *
     * @param loc The location. Must be a named location.
     * @return The new location reference expression.
     */
    private LocationExpression createGuard(Location loc) {
        LocationExpression rslt = newLocationExpression();
        rslt.setType(newBoolType());
        rslt.setLocation(loc);
        return rslt;
    }

    /**
     * Creates a new string literal expression with the name of the location, or {@code "*"} in case of a nameless
     * location.
     *
     * @param loc The location.
     * @return The new string literal.
     * @see CifLocationUtils#getName
     */
    private StringExpression createStringLiteral(Location loc) {
        StringExpression strExpr = newStringExpression();
        strExpr.setType(newStringType());
        strExpr.setValue(CifLocationUtils.getName(loc));
        return strExpr;
    }
}
