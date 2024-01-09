//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.multilevel.partialspecs;

import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBoolType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newInputVariable;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newInputVariableExpression;
import static org.eclipse.escet.common.java.Lists.list;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Constant;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumLiteral;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.TypeDecl;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.InputVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.LocationExpression;
import org.eclipse.escet.cif.metamodel.cif.functions.Function;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.Field;
import org.eclipse.escet.common.emf.EMFHelper;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** Construct one or more partial specifications by copying parts from an original specification. */
public class PartialSpecsBuilder {
    /** The supplied original specification. */
    private final Specification origSpec;

    /**
     * Constructor of the {@link PartialSpecsBuilder} class.
     *
     * @param origSpec The supplied original specification.
     */
    public PartialSpecsBuilder(Specification origSpec) {
        this.origSpec = origSpec;
    }

    /**
     * Create a partial specification from scratch that contains at least the plant and requirement objects of the
     * original specification given to the method. In addition, the partial specification may be extended with other
     * elements to make it a valid CIF specification.
     *
     * @param neededObjects Original objects that must at least be copied to the created partial specification.
     * @return The constructed partial specification.
     */
    public Specification createPartialSpecification(List<PositionObject> neededObjects) {
        PartialSpecManager partialMgr = new PartialSpecManager(origSpec);

        // First stage copies the core needed objects. First add the automata, then the other objects. Almost certainly
        // the result will still link to objects from the original specification. Such objects are known as dangling
        // objects and get resolved after the first stage.

        // Copy the automata to avoid getting them added as a group by the manager.
        for (PositionObject element: neededObjects) {
            if (element instanceof Automaton aut) {
                partialMgr.copyAutomatonSkeleton(aut);
            }
        }

        // Copy the other objects that must be in the partial specification.
        for (PositionObject object: neededObjects) {
            if (object instanceof Automaton aut) {
                continue;
            } else if (object instanceof Invariant inv) {
                partialMgr.directlyAttachAddedToComponent(inv, partialMgr.deepcloneAndAdd(inv));
            } else if (object instanceof InputVariable inp) {
                partialMgr.directlyAttachAddedToComponent(inp, partialMgr.deepcloneAndAdd(inp));
            } else {
                throw new AssertionError("Encountered unexpected needed object to copy: \"" + object + "\".");
            }
        }

        // Second stage fixes all dangling objects of the partial specification, except for fields.
        //
        // Repeat until we run out of dangling objects to fix.
        List<Entry<EObject, Collection<Setting>>> unresolvedFields = list();
        while (true) {
            // Get found dangling objects from a previous addition to the partial specification. Note that these objects
            // may already have been fixed in a previous iteration at other spots in the partial specification.
            Map<EObject, Collection<Setting>> danglings = partialMgr.getNextDanglingObjects();
            if (danglings == null) {
                break; // No more dangling objects.
            }

            // Fix each dangling object.
            for (Entry<EObject, Collection<Setting>> entry: danglings.entrySet()) {
                // Get original object connected with but not contained in the partial specification.
                EObject dangling = entry.getKey();

                // Get references from the partial specification to the dangling object.
                Collection<Setting> partialConnections = entry.getValue();

                // Find or construct a contained object in the partial specification to replace the dangling object.
                EObject contained = findOrMakeContained(dangling, partialMgr);

                // Check for fields of tuple types that have not been copied yet. Postpone resolving them under the
                // assumption that the object will eventually get copied into the partial specification.
                if (contained == null) {
                    unresolvedFields.add(entry);
                    continue;
                }

                // Replace all references to the dangling object by references to the contained object.
                if (dangling.getClass() == contained.getClass()) {
                    for (Setting setting: partialConnections) { // Same class, easily handled by the settings.
                        setting.set(contained);
                    }
                } else {
                    // Contained object has a different class. The partial object must be an input variable as that is
                    // the only way to get a different class. Also, the partial parent must either be a location or a
                    // discrete variable reference since those are the only two cases that may contain external state
                    // that needs to be hidden here.
                    // Not only the dangling object, but also its parent in the partial specification must be changed.
                    Assert.check(contained instanceof InputVariable);
                    for (Setting setting: partialConnections) {
                        EObject partialParent = setting.getEObject();

                        // As the partial parent is discarded and its type may have 'Field' nodes that we may need, we
                        // steal its type.
                        Assert.check(partialParent instanceof LocationExpression
                                || partialParent instanceof DiscVariableExpression);
                        CifType partialParentType = ((Expression)partialParent).getType();
                        InputVariableExpression newParent = newInputVariableExpression(null, partialParentType,
                                (InputVariable)contained);
                        EMFHelper.updateParentContainment(partialParent, newParent);
                    }
                }
            }
        }

        // Third stage resolves all remaining tuple type field references as by now all types are contained in the
        // partial specification.
        for (Entry<EObject, Collection<Setting>> entry: unresolvedFields) {
            // Get original 'Field' connected with but not contained in the partial specification.
            Field danglingField = (Field)entry.getKey();

            // Get references from the partial specification to the dangling field.
            Collection<Setting> partialConnections = entry.getValue();

            // The original field should have a partial counter-part now.
            EObject copiedField = partialMgr.getCopiedPartialObject(danglingField);
            Assert.notNull(copiedField);
            for (Setting setting: partialConnections) {
                setting.set(copiedField);
            }
        }

        // Return the newly constructed partial specification.
        return partialMgr.getPartialSpec();
    }

    /**
     * An object is referenced from the partial specification but is not contained. Since it's apparently needed, adapt
     * the partial specification to contain it if not done already.
     *
     * @param dangling Dangling original object that needs to be copied and added to the partial specification if not
     *     done already.
     * @param partialMgr The manager tracking everything in the partial specification.
     * @return A replacement object that represents the dangling object in the partial specification if one can be
     *     constructed. For tuple type fields, {@code null} is returned.
     */
    private EObject findOrMakeContained(EObject dangling, PartialSpecManager partialMgr) {
        // Try to find a contained object. Since dangling objects may be found multiple times and can be processed in
        // any order, it may have been processed already in the mean time. If so, don't do it again, but reuse the
        // object.
        EObject copiedObj = partialMgr.getCopiedPartialObject(dangling);
        if (copiedObj != null) {
            return copiedObj;
        }

        // A replacement object has to be created.

        // Some objects can simply be cloned. Currently, they are all declarations.
        if (dangling instanceof AlgVariable || dangling instanceof Constant || dangling instanceof EnumDecl
                || dangling instanceof Event || dangling instanceof Function || dangling instanceof InputVariable
                || dangling instanceof TypeDecl)
        {
            Declaration clonedDecl = (Declaration)partialMgr.deepcloneAndAdd(dangling);
            partialMgr.directlyAttachAddedToComponent(dangling, clonedDecl);
            return clonedDecl;
        }

        // Enumeration literals can be referenced but the entire enumeration must be copied.
        //
        // Copying the enumeration declaration also copies its literals. That means that:
        // - Code doesn't get here in that case, thus the surrounding enumeration declaration is not yet copied.
        // - Querying for the partial literals works after copying.
        if (dangling instanceof EnumLiteral) {
            EnumDecl enumDecl = (EnumDecl)dangling.eContainer();
            EnumDecl copiedEnumDecl = partialMgr.deepcloneAndAdd(enumDecl);
            partialMgr.directlyAttachAddedToComponent(enumDecl, copiedEnumDecl);

            EnumLiteral copiedEnumLit = (EnumLiteral)partialMgr.getCopiedPartialObject(dangling);
            Assert.notNull(copiedEnumLit);
            return copiedEnumLit;
        }

        // Locations that were not copied in the first stage are replaced by input variables.
        if (dangling instanceof Location loc) {
            InputVariable inputVar = newInputVariable(null, loc.getName(), null, newBoolType());
            // 'newBoolType' object above is not registered in the copy administration but it has nothing that can be
            // referenced.
            partialMgr.addCopiedObject(loc, inputVar);
            partialMgr.directlyAttachAddedToComponent(dangling, inputVar);
            return inputVar;
        }

        // Discrete variables of automata that were copied in the first stage should be deepcloned. Other discrete
        // variables become input variables with the same domains as the original discrete variables.
        if (dangling instanceof DiscVariable dv) {
            if (dv.eContainer() instanceof Automaton) {
                DiscVariable clonedVar = partialMgr.deepcloneAndAdd(dv);
                partialMgr.directlyAttachAddedToComponent(dangling, clonedVar);
                return clonedVar;
            } else {
                CifType clonedType = partialMgr.deepcloneAndAdd(dv.getType());
                InputVariable inputVar = newInputVariable(null, dv.getName(), null, clonedType);
                partialMgr.addCopiedObject(dangling, inputVar);
                partialMgr.directlyAttachAddedToComponent(dangling, inputVar);
                return inputVar;
            }
        }

        // Fields of tuple types that have not been copied yet are the only case left at this point. They are deferred
        // until everything else has been copied.
        Assert.check(dangling instanceof Field, "Found unexpected dangling object " + dangling);
        return null;
    }
}
