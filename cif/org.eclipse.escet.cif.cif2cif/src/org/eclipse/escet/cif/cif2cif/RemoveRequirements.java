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

package org.eclipse.escet.cif.cif2cif;

import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Sets.sortedstrings;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.ExternalCrossReferencer;
import org.eclipse.escet.cif.common.CifScopeUtils;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.SupKind;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/**
 * In-place transformation that removes requirements.
 *
 * <p>
 * Precondition:
 * <ul>
 * <li>Specifications with component definitions/instantiations are currently not supported.</li>
 * <li>Removing requirement automata or declarations from requirement automata that are being used (referred
 * to) in what remains after removal of requirements is not supported.</li>
 * <li>Invariants that may not be removed in locations of requirement automata (if removed) are not supported.</li>
 * </ul>
 * </p>
 */
public class RemoveRequirements implements CifToCifTransformation {
    /** Whether to remove requirement automata. */
    public boolean removeReqAuts = true;

    /** Whether to remove state/event exclusion requirement invariants. */
    public boolean removeStateEvtExclReqInvs = true;

    /** Whether to remove state requirement invariants. */
    public boolean removeStateReqInvs = true;

    /** Violations found so far. */
    private final Set<String> problemMessages = set();

    /**
     * Mapping from removed requirement automata to their absolute names. Is initialized before removal, and filled
     * during removal.
     */
    private Map<Automaton, String> absReqAutNames;

    @Override
    public void transform(Specification spec) {
        // Check no component definition/instantiation precondition.
        if (CifScopeUtils.hasCompDefInst(spec)) {
            String msg = "Removing requirements from a CIF specification with component definitions is currently not "
                    + "supported.";
            throw new CifToCifPreconditionException(msg);
        }

        // Prepare for removal.
        absReqAutNames = map();

        // Perform removal.
        removeRequirements(spec);

        // Check for references to removed declarations. They are no longer
        // valid, meaning the removal failed.
        checkRefs(spec);

        // Report problems.
        if (!problemMessages.isEmpty()) {
            String msg = "Removing requirements from a CIF specification failed due to the remaining part of the "
                    + "specification using declarations that are declared in the requirement automata that are to be "
                    + "removed, or a location of a requirement automaton contains invariants that cannot be removed:\n "
                    + "- " + StringUtils.join(sortedstrings(problemMessages), "\n - ");
            throw new CifToCifPreconditionException(msg);
        }
    }

    /**
     * Removes requirements from the given component, recursively.
     *
     * @param comp The component for which to remove requirements, recursively.
     */
    private void removeRequirements(ComplexComponent comp) {
        // Remove requirement invariants of this component.
        Iterator<Invariant> invIter = comp.getInvariants().iterator();
        while (invIter.hasNext()) {
            Invariant inv = invIter.next();
            if (shouldRemoveInvariant(inv)) {
                invIter.remove();
            }
        }

        // Apply recursively to child components.
        if (comp instanceof Group) {
            List<Component> children = ((Group)comp).getComponents();
            Iterator<Component> childIter = children.iterator();
            while (childIter.hasNext()) {
                Component child = childIter.next();

                // Remove child requirement automata if requested.
                if (child instanceof Automaton) {
                    SupKind kind = ((Automaton)child).getKind();
                    if (kind == SupKind.REQUIREMENT && removeReqAuts) {
                        // Automaton to be removed, move invariants that may not be removed to the parent.
                        moveInvariantsFromAut((Automaton)child);
                        absReqAutNames.put((Automaton)child, CifTextUtils.getAbsName(child));
                        childIter.remove();
                        continue;
                    } else {
                        // Automaton not to be removed, remove invariants in that automaton.
                        removeRequirements((ComplexComponent)child);
                    }
                }

                // Recursively remove in group.
                if (child instanceof Group) {
                    removeRequirements((ComplexComponent)child);
                }
            }
        }

        // Remove requirement invariants from locations of automata.
        if (comp instanceof Automaton) {
            for (Location loc: ((Automaton)comp).getLocations()) {
                invIter = loc.getInvariants().iterator();
                while (invIter.hasNext()) {
                    if (shouldRemoveInvariant(invIter.next())) {
                        invIter.remove();
                    }
                }
            }
        }
    }

    /**
     * Checks whether an invariant should be removed in the specification, or if it should remain.
     *
     * @param inv The invariant.
     * @return {@code true} if it should be removed, {@code false} otherwise.
     */
    private boolean shouldRemoveInvariant(Invariant inv) {
        if (inv.getSupKind() != SupKind.REQUIREMENT) {
            return false;
        }
        switch (inv.getInvKind()) {
            case STATE:
                if (removeStateReqInvs) {
                    return true;
                }
                break;

            case EVENT_DISABLES:
            case EVENT_NEEDS:
                if (removeStateEvtExclReqInvs) {
                    return true;
                }
                break;
        }
        return false;
    }

    /**
     * Moves the invariants that should not be removed to the parent component. If invariants in locations are
     * encountered that may not be removed, a problem message is added to {@link #problemMessages}.
     *
     * @param aut The automaton to move the invariants for.
     */
    private void moveInvariantsFromAut(Automaton aut) {
        Assert.check(aut.eContainer() instanceof ComplexComponent);
        ComplexComponent parent = ((ComplexComponent)aut.eContainer());

        Iterator<Invariant> invIter = aut.getInvariants().iterator();
        while (invIter.hasNext()) {
            Invariant inv = invIter.next();
            if (!shouldRemoveInvariant(inv)) {
                invIter.remove();
                parent.getInvariants().add(inv);
            }
        }

        // Assert that all invariants in locations may be removed. We can't move them, since we cannot refer to the
        // location.
        for (Location loc: aut.getLocations()) {
            for (Invariant inv: loc.getInvariants()) {
                if (!shouldRemoveInvariant(inv)) {
                    // Problems found. Construct message.
                    SupKind supKind = inv.getSupKind();
                    String kindTxt = supKind == SupKind.NONE ? "kindless" : CifTextUtils.kindToStr(supKind);
                    String locTxt = loc.getName() == null ? "Location" : fmt("Location \"%s\"", loc.getName());
                    String msg = fmt("%s of requirement automaton \"%s\" contains a %s invariant.", locTxt,
                            CifTextUtils.getAbsName(aut), kindTxt);
                    problemMessages.add(msg);
                }
            }
        }
    }

    /**
     * Checks a specification for references to removed declarations. They are no longer valid, meaning the removal
     * failed. Problems are added to {@link #problemMessages}.
     *
     * @param spec The specification to check.
     */
    private void checkRefs(Specification spec) {
        // Find references to objects outside of the specification.
        Map<EObject, Collection<Setting>> problems;
        problems = ExternalCrossReferencer.find(spec);
        if (problems.isEmpty()) {
            return;
        }

        // Problems found. Construct messages.
        for (Entry<EObject, Collection<Setting>> problem: problems.entrySet()) {
            PositionObject removedObj = (PositionObject)problem.getKey();
            Collection<Setting> problemRefs = problem.getValue();

            // Get absolute name of the removed requirement automaton that
            // contains the removed declaration.
            EObject removedRootObj = EcoreUtil.getRootContainer(removedObj);
            Automaton removedReqAut = (Automaton)removedRootObj;
            String absReqName = absReqAutNames.get(removedReqAut);
            Assert.notNull(absReqName);

            // Get textual reference to the removed declaration, relative to
            // the removed requirement automaton.
            String relTxt;
            if (removedReqAut == removedObj) {
                relTxt = null;
            } else {
                // Removed declaration should be in removed requirement
                // automaton scope, as we don't have definition/instantiation
                // anymore, and functions and components can't be contained
                // in automata.
                PositionObject scope = CifScopeUtils.getScope(removedObj);
                Assert.check(scope == removedReqAut);

                // Can use direct name, escaped.
                relTxt = CifTextUtils.getName(removedObj);
                relTxt = CifTextUtils.escapeIdentifier(relTxt);
            }

            // Process all problematic references to the removed object.
            for (Setting problemRef: problemRefs) {
                // Get referencing object (object that has the problematic
                // reference).
                PositionObject refObj = (PositionObject)problemRef.getEObject();

                // Get scope of the referencing object, as that is something
                // that we can refer to textually, unlike expressions etc.
                PositionObject scope = CifScopeUtils.isScope(refObj) ? refObj : CifScopeUtils.getScope(refObj);

                // Get textual reference for the scope from which we reference
                // the removed declaration.
                ComplexComponent scopeComp = (ComplexComponent)scope;
                String scopeTxt = CifTextUtils.getComponentText2(scopeComp);

                // Add problem message.
                String declTxt;
                if (relTxt == null) {
                    declTxt = "";
                } else {
                    declTxt = fmt("contains declaration \"%s\" that ", relTxt);
                }
                String msg = fmt("Requirement automaton \"%s\" %sis used somewhere in %s.", absReqName, declTxt,
                        scopeTxt);
                problemMessages.add(msg);
            }
        }
    }
}
