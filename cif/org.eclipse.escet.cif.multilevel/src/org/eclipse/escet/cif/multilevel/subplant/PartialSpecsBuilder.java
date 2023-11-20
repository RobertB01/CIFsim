//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.multilevel.subplant;

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
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumLiteral;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.TypeDecl;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
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
        // objects and get resolved after he first stage.

        // Copy the automata to avoid getting them added as Group by the manager.
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
                partialMgr.attachAddedToComponent(inv, partialMgr.deepcloneAndAdd(inv));
            } else if (object instanceof InputVariable inp) {
                partialMgr.attachAddedToComponent(inp, partialMgr.deepcloneAndAdd(inp));
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
            Map<EObject, Collection<Setting>> danglings = partialMgr.getSomeDanglingObjects();
            if (danglings == null) {
                break;
            }

            for (Entry<EObject, Collection<Setting>> entry: danglings.entrySet()) {
                EObject dangling = entry.getKey(); // Original object connected with but not contained in the partial
                                                   // specification.
                Collection<Setting> partialConnections = entry.getValue(); // References from the partial specification
                                                                           // to the dangling object.
                // Find or construct a contained object in the partial specification to replace the dangling object.
                EObject contained = findOrMakeContained(dangling, partialMgr);
                if (contained == null) {
                    // Field of a tuple that has not been copied yet. Postpone resolving them under the assumption that
                    // the object will eventually get copied into the partial specification.
                    unresolvedFields.add(entry);
                    continue;
                }

                // Replace all references to the dangling object by references to the contained object.
                if (dangling.getClass() == contained.getClass()) {
                    for (Setting setting: partialConnections) { // Same class, easily handled by the settings.
                        setting.set(contained);
                    }
                } else {
                    // Contained object has a different class. The partial object must be an input variable.
                    // Not only the dangling object, but also its parent in the partial specification must be changed.
                    Assert.check(contained instanceof InputVariable);
                    for (Setting setting: partialConnections) {
                        EObject partialParent = setting.getEObject();

                        // As the partial parent is discarded and its type may have Field nodes that we may need, we
                        // steal its type.
                        InputVariableExpression newParent = newInputVariableExpression(null, getType(partialParent),
                                (InputVariable)contained);
                        EMFHelper.updateParentContainment(partialParent, newParent);
                    }
                }
            }
        }

        // Third stage resolves all remaining Field references as by now all types are contained in the partial
        // specification.
        for (Entry<EObject, Collection<Setting>> entry: unresolvedFields) {
            EObject danglingField = entry.getKey(); // Original Field connected with but not contained in the partial
                                                    // specification.
            Collection<Setting> partialConnections = entry.getValue(); // References from the partial specification
                                                                       // to the dangling Field.
            // The original field should have a partial counter-part now.
            EObject copiedField = partialMgr.getCopiedPartialObject(danglingField);
            Assert.notNull(copiedField);
            for (Setting setting: partialConnections) {
                setting.set(copiedField);
            }
        }

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
     *     constructed. Otherwise {@code null} is returned.
     */
    private EObject findOrMakeContained(EObject dangling, PartialSpecManager partialMgr) {
        // Maybe a contained object was created in the mean time?
        EObject copiedObj = partialMgr.getCopiedPartialObject(dangling);
        if (copiedObj != null) {
            return copiedObj;
        }

        // A replacement object has to be created.

        // Some of these objects can simply be cloned.
        if (dangling instanceof AlgVariable || dangling instanceof Constant || dangling instanceof EnumDecl
                || dangling instanceof Event || dangling instanceof Function || dangling instanceof InputVariable
                || dangling instanceof TypeDecl)
        {
            EObject clonedObj = partialMgr.deepcloneAndAdd(dangling);
            partialMgr.attachAddedToComponent(dangling, clonedObj);
            return clonedObj;
        }

        // Enumeration literals can be referenced but the entire enumeration must be copied.
        //
        // Copying the enumeration declaration also added its fields. That means that
        // - Code doesn't get here in that case, thus the surrounding enumeration declaration is not yet copied.
        // - Querying for the partial field works after copying.
        if (dangling instanceof EnumLiteral) {
            EObject enumDecl = dangling.eContainer();
            EObject copiedDeclObj = partialMgr.deepcloneAndAdd(enumDecl);
            partialMgr.attachAddedToComponent(enumDecl, copiedDeclObj);

            copiedDeclObj = partialMgr.getCopiedPartialObject(dangling);
            Assert.notNull(copiedDeclObj);
            return copiedDeclObj;
        }

        // Locations that were not copied in the first stage are replaced by input variables.
        if (dangling instanceof Location loc) {
            EObject inputVar = newInputVariable(null, loc.getName(), null, newBoolType());
            // 'newBoolType' object above is not registered in the copy administration but it has nothing that can be
            // referenced.
            partialMgr.addCopiedObject(loc, inputVar);
            partialMgr.attachAddedToComponent(dangling, inputVar);
            return inputVar;
        }

        // Discrete variables of copied automata in the first stage should be deepcloned, rather than becoming
        // input variables with the same domain.
        if (dangling instanceof DiscVariable dv) {
            if (dv.eContainer() instanceof Automaton) {
                EObject clonedObj = partialMgr.deepcloneAndAdd(dv);
                partialMgr.attachAddedToComponent(dangling, clonedObj);
                return clonedObj;
            } else {
                CifType clonedType = partialMgr.deepcloneAndAdd(dv.getType());
                EObject inputVar = newInputVariable(null, dv.getName(), null, clonedType);
                partialMgr.addCopiedObject(dangling, inputVar);
                partialMgr.attachAddedToComponent(dangling, inputVar);
                return inputVar;
            }
        }

        // Tuple fields that have not been copied yet are the only case left at this point. They are deferred until
        // everything else has been copied.
        Assert.check(dangling instanceof Field, "Found unexpected dangling object " + dangling);
        return null;
    }

    /**
     * Get the type of a location or discrete variable reference expression.
     *
     * @param expr Expression to retrieve the type from.
     * @return The type contained in the provided expression.
     */
    private CifType getType(EObject expr) {
        // Casting to Expression and getting the type would work, but having the class check is useful to check sanity.
        if (expr instanceof LocationExpression locExpr) {
            return locExpr.getType();
        } else if (expr instanceof DiscVariableExpression dvExpr) {
            return dvExpr.getType();
        }
        throw new AssertionError("Unexpected parent class \"" + expr + "\".");
    }
}
