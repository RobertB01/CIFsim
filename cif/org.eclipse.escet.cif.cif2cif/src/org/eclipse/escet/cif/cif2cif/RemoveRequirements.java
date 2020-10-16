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
import org.eclipse.escet.cif.common.CifInvariantUtils;
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
 * Precondition: Specifications with component definitions/instantiations are currently not supported.
 * </p>
 *
 * <p>
 * Precondition: Removing requirement automata or declarations from requirement automata that are being used (referred
 * to) in what remains after removal of requirements is not supported.
 * </p>
 */
public class RemoveRequirements implements CifToCifTransformation {
    /** Whether to remove requirement automata. */
    public boolean removeReqAuts = true;

    /** Whether to remove state/event exclusion requirement invariants. */
    public boolean removeStateEvtExclReqInvs = true;

    /** Whether to remove state requirement invariants. */
    public boolean removeStateReqInvs = true;

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
            SupKind kind = CifInvariantUtils.getSupKind(inv);
            if (kind != SupKind.REQUIREMENT) {
                continue;
            }
            switch (inv.getInvKind()) {
                case STATE:
                    if (removeStateReqInvs) {
                        invIter.remove();
                    }
                    break;

                case EVENT_DISABLES:
                case EVENT_NEEDS:
                    if (removeStateEvtExclReqInvs) {
                        invIter.remove();
                    }
                    break;
            }
        }

        // Apply recursively to child components.
        if (comp instanceof Group) {
            List<Component> children = ((Group)comp).getComponents();
            Iterator<Component> childIter = children.iterator();
            while (childIter.hasNext()) {
                Component child = childIter.next();

                // Remove child requirement automata.
                if (child instanceof Automaton) {
                    SupKind kind = ((Automaton)child).getKind();
                    if (kind == SupKind.REQUIREMENT && removeReqAuts) {
                        absReqAutNames.put((Automaton)child, CifTextUtils.getAbsName(child));
                        childIter.remove();
                        continue;
                    }
                }

                // Recursively remove in group or automaton.
                removeRequirements((ComplexComponent)child);
            }
        }

        // Remove requirement invariants from locations of automata.
        if (comp instanceof Automaton) {
            for (Location loc: ((Automaton)comp).getLocations()) {
                invIter = loc.getInvariants().iterator();
                while (invIter.hasNext()) {
                    Invariant inv = invIter.next();
                    SupKind kind = CifInvariantUtils.getSupKind(inv);
                    if (kind != SupKind.REQUIREMENT) {
                        continue;
                    }
                    switch (inv.getInvKind()) {
                        case STATE:
                            if (removeStateReqInvs) {
                                invIter.remove();
                            }
                            break;

                        case EVENT_DISABLES:
                        case EVENT_NEEDS:
                            if (removeStateEvtExclReqInvs) {
                                invIter.remove();
                            }
                            break;
                    }
                }
            }
        }
    }

    /**
     * Checks a specification for references to removed declarations. They are no longer valid, meaning the removal
     * failed.
     *
     * @param spec The specification to check.
     * @throws CifToCifPreconditionException If a reference to a removed declaration is found.
     */
    private void checkRefs(Specification spec) {
        // Find references to objects outside of the specification.
        Map<EObject, Collection<Setting>> problems;
        problems = ExternalCrossReferencer.find(spec);
        if (problems.isEmpty()) {
            return;
        }

        // Problems found. Construct messages.
        Set<String> messages = set();
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
                messages.add(msg);
            }
        }

        // Report problems.
        String msg = "Removing requirements from a CIF specification failed due to the remaining part of the "
                + "specification using declarations that are declared in the requirement automata that are to be "
                + "removed:\n - " + StringUtils.join(sortedstrings(messages), "\n - ");
        throw new CifToCifPreconditionException(msg);
    }
}
