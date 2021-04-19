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

import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newCompInstWrapExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newCompInstWrapType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newComponentType;
import static org.eclipse.escet.common.emf.EMFHelper.deepclone;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Pair.pair;
import static org.eclipse.escet.common.java.Sets.set;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.escet.cif.common.CifScopeUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.metamodel.cif.AlgParameter;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.ComponentDef;
import org.eclipse.escet.cif.metamodel.cif.ComponentInst;
import org.eclipse.escet.cif.metamodel.cif.ComponentParameter;
import org.eclipse.escet.cif.metamodel.cif.EventParameter;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.LocationParameter;
import org.eclipse.escet.cif.metamodel.cif.Parameter;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Constant;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumLiteral;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.TypeDecl;
import org.eclipse.escet.cif.metamodel.cif.expressions.AlgVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CompInstWrapExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CompParamWrapExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ComponentExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ConstantExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ContVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EnumLiteralExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.FunctionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.InputVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.LocationExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SelfExpression;
import org.eclipse.escet.cif.metamodel.cif.functions.Function;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.CompInstWrapType;
import org.eclipse.escet.cif.metamodel.cif.types.CompParamWrapType;
import org.eclipse.escet.cif.metamodel.cif.types.ComponentDefType;
import org.eclipse.escet.cif.metamodel.cif.types.ComponentType;
import org.eclipse.escet.cif.metamodel.cif.types.EnumType;
import org.eclipse.escet.cif.metamodel.cif.types.TypeRef;
import org.eclipse.escet.cif.metamodel.java.CifWalker;
import org.eclipse.escet.common.emf.EMFHelper;
import org.eclipse.escet.common.emf.EMFPath;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Pair;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/**
 * In-place transformation that eliminates component definition/instantiation.
 *
 * <p>
 * Since component definitions are shortcuts for components, eliminating them could result in a blow-up of the size of
 * the specification.
 * </p>
 */
public class ElimComponentDefInst extends CifWalker implements CifToCifTransformation {
    /**
     * The component definitions without component definitions/instantiations in them. Filled during phase 1. These are
     * the ones to be eliminated during phase 2.
     */
    private Set<ComponentDef> elimDefs;

    /** Whether component definitions were found in during phase 1. */
    private boolean foundDefs;

    /**
     * Mapping from copied component definitions, to their instantiated forms. Filled during phase 2. Used during phase
     * 3.
     */
    private Map<ComponentDef, ComplexComponent> cdefMap;

    /**
     * Mapping from component instantiations to their instantiated forms. Filled during phase 2. Used during phase 3.
     */
    private Map<ComponentInst, ComplexComponent> instMap;

    /**
     * Mapping of (cloned) formal component parameters to their actual arguments. Filled during phase 2. Used during
     * phase 3.
     *
     * <p>
     * Don't look up anything in this mapping directly. Use the {@link #getActualArgument} method instead.
     * </p>
     */
    private Map<ComponentParameter, Expression> compParamMap;

    /**
     * Mapping of (cloned) formal component parameters to their original instantiations, and the index of the parameter.
     * Filled during phase 2, for all keys of {@link #compParamMap}. Used during phase 3. Once used, the entry is
     * removed, so that it is used only once.
     *
     * <p>
     * This mapping is to be used by the {@link #getActualArgument} method only, to update {@link #compParamMap}
     * mapping.
     * </p>
     */
    private Map<ComponentParameter, Pair<ComponentInst, Integer>> paramOrigMap;

    /**
     * Mapping of (cloned) events from formal event parameters to their actual arguments. Filled during phase 2. Used
     * during phase 3.
     */
    private Map<Event, Expression> eventParamMap;

    /**
     * Mapping of (cloned) locations from formal locations parameters to their actual arguments. Filled during phase 2.
     * Used during phase 3.
     */
    private Map<Location, Expression> locParamMap;

    @Override
    public void transform(Specification spec) {
        while (true) {
            // Phase 1: find component definitions to instantiate.
            elimDefs = set();
            foundDefs = false;
            analyzeCompDefs(spec);
            if (elimDefs.isEmpty()) {
                Assert.check(!foundDefs);
                break;
            }

            // Phase 2: remove component definitions/instantiations.
            cdefMap = map();
            instMap = map();
            compParamMap = map();
            paramOrigMap = map();
            eventParamMap = map();
            locParamMap = map();
            instantiate(spec);

            // Phase 3: fix references.
            walkSpecification(spec);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Phase 1.
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Find component definitions without component definitions/instantiations in them. These can be eliminated in phase
     * 2. If such component definitions are found, they are added to {@link #elimDefs}.
     *
     * @param group The group in which to search.
     * @return {@code true} if the group contains definitions or instantiations, {@code false} otherwise.
     * @see #elimDefs
     * @see #foundDefs
     */
    private boolean analyzeCompDefs(Group group) {
        boolean foundDefOrInst = false;

        // Process child component definitions.
        for (ComponentDef cdef: group.getDefinitions()) {
            analyzeCompDefs(cdef);
            foundDefOrInst = true;
        }

        // Process child components.
        for (Component comp: group.getComponents()) {
            if (comp instanceof ComponentInst) {
                foundDefOrInst = true;
            } else if (comp instanceof Group) {
                boolean foundDefOrInstInGroup = analyzeCompDefs((Group)comp);
                foundDefOrInst |= foundDefOrInstInGroup;
            }
        }

        return foundDefOrInst;
    }

    /**
     * Find component definitions without component definitions/instantiations in them. These can be eliminated in phase
     * 2. If such component definitions are found, they are added to {@link #elimDefs}.
     *
     * @param cdef The component definition in which to search.
     * @see #elimDefs
     * @see #foundDefs
     */
    private void analyzeCompDefs(ComponentDef cdef) {
        ComplexComponent body = cdef.getBody();
        foundDefs = true;

        // Automaton body.
        if (body instanceof Automaton) {
            elimDefs.add(cdef);
            return;
        }

        // Group body.
        Assert.check(body instanceof Group);
        Group group = (Group)body;
        boolean foundDefOrInst = false;

        for (ComponentDef cdef2: group.getDefinitions()) {
            // Found a child definition.
            foundDefOrInst = true;

            // Search for definitions that can be eliminated.
            analyzeCompDefs(cdef2);
        }

        // Search child components for more definitions or instantiations.
        for (Component comp: group.getComponents()) {
            if (comp instanceof ComponentInst) {
                foundDefOrInst = true;
            } else if (comp instanceof Group) {
                boolean foundDefOrInstInGroup = analyzeCompDefs((Group)comp);
                foundDefOrInst |= foundDefOrInstInGroup;
            }
        }

        if (!foundDefOrInst) {
            elimDefs.add(cdef);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Phase 2.
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Eliminate the component definitions found during phase 1, as well as their corresponding instantiations.
     *
     * @param group The group in which to perform the elimination.
     * @see #elimDefs
     */
    private void instantiate(Group group) {
        // Process child component definitions.
        List<ComponentDef> childDefs = group.getDefinitions();
        Iterator<ComponentDef> childIter = childDefs.iterator();
        while (childIter.hasNext()) {
            ComponentDef cdef = childIter.next();

            if (elimDefs.contains(cdef)) {
                // Remove component definition.
                childIter.remove();
            } else {
                // Process recursively.
                ComplexComponent body = cdef.getBody();
                if (body instanceof Group) {
                    instantiate((Group)body);
                }
            }
        }

        // Process child components.
        List<Component> childComps = group.getComponents();
        for (int i = 0; i < childComps.size(); i++) {
            Component comp = childComps.get(i);

            if (comp instanceof Group) {
                instantiate((Group)comp);
            } else if (comp instanceof ComponentInst) {
                // Should we eliminate this one?
                ComponentInst inst = (ComponentInst)comp;
                ComponentDef cdef = CifTypeUtils.getCompDefFromCompInst(inst);
                if (!elimDefs.contains(cdef)) {
                    continue;
                }

                // Eliminate this one.
                Component comp2 = instantiate((ComponentInst)comp);
                childComps.set(i, comp2);
            }
        }
    }

    /**
     * Instantiates the component instantiation.
     *
     * @param inst The component instantiation to instantiate.
     * @return The component resulting from instantiation.
     * @see #instMap
     * @see #compParamMap
     * @see #paramOrigMap
     * @see #eventParamMap
     * @see #locParamMap
     */
    private ComplexComponent instantiate(ComponentInst inst) {
        // Get component definition clone, so that we may modify it in-place.
        ComponentDef cdef = CifTypeUtils.getCompDefFromCompInst(inst);
        cdef = deepclone(cdef);

        // The body of the copied component definition is used as the result, i.e. as the instantiated form.
        ComplexComponent body = cdef.getBody();

        // When cloning the component definition, all references to itself from within itself, are changed to refer to
        // the copy. Since we need to update those references during phase 3, we relate the copy to its instantiated
        // form.
        cdefMap.put(cdef, body);

        // Set instantiation name.
        body.setName(inst.getName());

        // Fill instantiation mapping.
        instMap.put(inst, body);

        // Fill formal/actual mappings. Instantiate algebraic parameters.
        List<Parameter> formals = cdef.getParameters();
        List<Expression> actuals = inst.getParameters();
        Assert.check(formals.size() == actuals.size());
        for (int i = 0; i < formals.size(); i++) {
            Parameter formal = formals.get(i);
            Expression actual = actuals.get(i);

            if (formal instanceof AlgParameter) {
                // Note that 'actual' is deep-cloned, to make sure it doesn't disappear from 'actuals', where it was
                // previously contained.
                AlgVariable var = ((AlgParameter)formal).getVariable();
                body.getDeclarations().add(var);
                var.setValue(deepclone(actual));
            } else if (formal instanceof EventParameter) {
                Event event = ((EventParameter)formal).getEvent();
                eventParamMap.put(event, actual);
            } else if (formal instanceof LocationParameter) {
                Location loc = ((LocationParameter)formal).getLocation();
                locParamMap.put(loc, actual);
            } else if (formal instanceof ComponentParameter) {
                compParamMap.put((ComponentParameter)formal, actual);
                paramOrigMap.put((ComponentParameter)formal, pair(inst, i));
            } else {
                throw new RuntimeException("Unknown formal param: " + formal);
            }
        }

        // Return final result.
        return body;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Phase 3.
    ///////////////////////////////////////////////////////////////////////////

    @Override
    protected void postprocessEventExpression(EventExpression evtRef) {
        // Non-wrapped event reference expression. First, get actual argument, if any.
        Expression newRef = eventParamMap.get(evtRef.getEvent());
        if (newRef == null) {
            return;
        }

        // Copy actual parameter.
        newRef = deepclone(newRef);

        // Replace reference by actual argument.
        EMFHelper.updateParentContainment(evtRef, newRef);

        // Make sure we process the actual argument, in case it contains references that we must process.
        walkExpression(newRef);
    }

    @Override
    protected void postprocessLocationExpression(LocationExpression locRef) {
        // Non-wrapped location reference expression. First, get actual argument, if any.
        Expression newRef = locParamMap.get(locRef.getLocation());
        if (newRef == null) {
            return;
        }

        // Copy actual parameter.
        newRef = deepclone(newRef);

        // Replace reference by actual argument.
        EMFHelper.updateParentContainment(locRef, newRef);

        // Make sure we process the actual argument, in case it contains references that we must process.
        walkExpression(newRef);
    }

    @Override
    protected void postprocessComponentExpression(ComponentExpression compRef) {
        // Get component instantiation, if any.
        Component c = compRef.getComponent();
        if (!(c instanceof ComponentInst)) {
            return;
        }

        // See if we eliminated it.
        ComplexComponent comp = instMap.get(c);
        if (comp == null) {
            return;
        }

        // Replace reference.
        compRef.setComponent(comp);
    }

    @Override
    protected void postprocessSelfExpression(SelfExpression expr) {
        // If the type is a component definition type, and the component definition was instantiated, we need to update
        // the type to a component type.

        // Check for component definition type.
        CifType type = expr.getType();
        if (!(type instanceof ComponentDefType)) {
            return;
        }

        // Obtain instantiated component, if component definition was instantiated during this iteration.
        ComponentDef cdef = ((ComponentDefType)type).getDefinition();
        ComplexComponent comp = cdefMap.get(cdef);
        if (comp == null) {
            return;
        }

        // Paranoia checking.
        Assert.check(comp instanceof Automaton);

        // Replace type.
        ComponentType ctype = newComponentType();
        ctype.setComponent(comp);
        expr.setType(ctype);
    }

    @Override
    protected void postprocessComponentType(ComponentType compType) {
        // Get component instantiation, if any.
        Component c = compType.getComponent();
        if (!(c instanceof ComponentInst)) {
            return;
        }

        // See if we eliminated it.
        ComplexComponent comp = instMap.get(c);
        if (comp == null) {
            return;
        }

        // Replace reference.
        compType.setComponent(comp);
    }

    @Override
    protected void walkCompInstWrapExpression(CompInstWrapExpression wrap) {
        // Get instantiated component, and see whether it needs processing.
        ComplexComponent comp = instMap.get(wrap.getInstantiation());
        if (comp == null) {
            super.walkCompInstWrapExpression(wrap);
            return;
        }

        // Get component instantiation and child reference expression.
        ComponentInst inst = wrap.getInstantiation();
        Expression childRef = wrap.getReference();

        // Get referenced child object. Method getRefObjFromRef does not handle wrapping expressions. However, they
        // can't occur here:
        //
        // - CompInstWrapExpression: Assume that 'wrap' is a component instantiation 'x1' for component definition 'X'.
        // Then, since we are eliminating 'X' and 'x1', 'X' does not contain any component instantiations. As such,
        // 'childRef' can not be a component instantiation wrapping expression.
        //
        // - CompParamWrapExpression: Due to scoping constraints, component parameters can not be referenced via
        // component instantiations.
        //
        // Also, 'childRef' can not be a component reference that refers to a component instantiation:
        //
        // - Assume that 'wrap' is a component instantiation 'x1' for component definition 'X'. Then, since we are
        // eliminating 'X' and 'x1', 'X' does not contain any component instantiations. As such, 'childRef' can not
        // reference a component instantiation.
        PositionObject refObj = CifScopeUtils.getRefObjFromRef(childRef);

        // Get component definition body.
        ComponentDef cdef = CifTypeUtils.getCompDefFromCompInst(inst);
        ComplexComponent body = cdef.getBody();

        // Get non-via referenced object.
        Object newRefObj = getNonViaRefObj(refObj, body, comp);

        // In-place modify child reference expression.
        if (childRef instanceof ConstantExpression) {
            Constant c = (Constant)newRefObj;
            ((ConstantExpression)childRef).setConstant(c);
        } else if (childRef instanceof DiscVariableExpression) {
            DiscVariable v = (DiscVariable)newRefObj;
            ((DiscVariableExpression)childRef).setVariable(v);
        } else if (childRef instanceof AlgVariableExpression) {
            AlgVariable a = (AlgVariable)newRefObj;
            ((AlgVariableExpression)childRef).setVariable(a);
        } else if (childRef instanceof ContVariableExpression) {
            ContVariable v = (ContVariable)newRefObj;
            ((ContVariableExpression)childRef).setVariable(v);
        } else if (childRef instanceof LocationExpression) {
            Location l = (Location)newRefObj;
            ((LocationExpression)childRef).setLocation(l);
        } else if (childRef instanceof EnumLiteralExpression) {
            EnumLiteral l = (EnumLiteral)newRefObj;
            ((EnumLiteralExpression)childRef).setLiteral(l);
        } else if (childRef instanceof EventExpression) {
            Event e = (Event)newRefObj;
            ((EventExpression)childRef).setEvent(e);
        } else if (childRef instanceof FunctionExpression) {
            Function f = (Function)newRefObj;
            ((FunctionExpression)childRef).setFunction(f);
        } else if (childRef instanceof InputVariableExpression) {
            InputVariable v = (InputVariable)newRefObj;
            ((InputVariableExpression)childRef).setVariable(v);
        } else if (childRef instanceof ComponentExpression) {
            // This component reference can not reference a component instantiation. See above.
            ComponentExpression compRef = (ComponentExpression)childRef;
            Component c = (Component)newRefObj;
            compRef.setComponent(c);
        } else if (childRef instanceof CompInstWrapExpression) {
            // Can't happen. See above.
            throw new RuntimeException("Should never get here...");
        } else if (childRef instanceof CompParamWrapExpression) {
            // Can't happen. See above.
            throw new RuntimeException("Should never get here...");
        } else {
            throw new RuntimeException("Unknown child ref expr: " + childRef);
        }

        // Replace wrapping expression in the parent.
        EMFHelper.updateParentContainment(wrap, childRef);

        // See whether type needs additional processing.
        walkCifType(childRef.getType());
    }

    @Override
    protected void walkCompInstWrapType(CompInstWrapType wrap) {
        // Get instantiated component, and see whether it needs processing.
        ComplexComponent comp = instMap.get(wrap.getInstantiation());
        if (comp == null) {
            super.walkCompInstWrapType(wrap);
            return;
        }

        // Get component instantiation and child reference type.
        ComponentInst inst = wrap.getInstantiation();
        CifType childRef = wrap.getReference();

        // Get referenced child object.
        EObject refObj;
        if (childRef instanceof TypeRef) {
            refObj = ((TypeRef)childRef).getType();
        } else if (childRef instanceof EnumType) {
            refObj = ((EnumType)childRef).getEnum();
        } else if (childRef instanceof ComponentType) {
            // Assume that 'wrap' is a component instantiation 'x1' for component definition 'X'. Then, since we are
            // eliminating 'X' and 'x1', 'X' does not contain any component instantiations. As such, 'childRef' can not
            // reference a component instantiation.
            refObj = ((ComponentType)childRef).getComponent();
        } else if (childRef instanceof ComponentDefType) {
            // Assume that 'wrap' is a component instantiation 'x1' for component definition 'X'. Then, since we are
            // eliminating 'X' and 'x1', 'X' does not contain any component definitions. As such, 'childRef' can not be
            // a component definition reference.
            throw new RuntimeException("Invalid comp def type.");
        } else if (childRef instanceof CompInstWrapType) {
            // Assume that 'wrap' is a component instantiation 'x1' for component definition 'X'. Then, since we are
            // eliminating 'X' and 'x1', 'X' does not contain any component instantiations. As such, 'childRef' can not
            // be a component instantiation wrapping expression.
            throw new RuntimeException("Invalid comp inst wrap type.");
        } else if (childRef instanceof CompParamWrapType) {
            // Due to scoping constraints, component parameters can not be referenced via component instantiations.
            throw new RuntimeException("Invalid comp param wrap type.");
        } else {
            throw new RuntimeException("Unknown ref type: " + childRef);
        }

        // Get component definition body.
        ComponentDef cdef = CifTypeUtils.getCompDefFromCompInst(inst);
        ComplexComponent body = cdef.getBody();

        // Get non-via referenced object.
        Object newRefObj = getNonViaRefObj(refObj, body, comp);

        // In-place modify child reference expression.
        if (childRef instanceof TypeRef) {
            TypeDecl t = (TypeDecl)newRefObj;
            ((TypeRef)childRef).setType(t);
        } else if (childRef instanceof EnumType) {
            EnumDecl e = (EnumDecl)newRefObj;
            ((EnumType)childRef).setEnum(e);
        } else if (childRef instanceof ComponentType) {
            // This component reference can not reference a component instantiation. See above.
            Component c = (Component)newRefObj;
            ((ComponentType)childRef).setComponent(c);
        } else if (childRef instanceof CompInstWrapType) {
            // Can't happen. See above.
            throw new RuntimeException("Should never get here...");
        } else if (childRef instanceof CompParamWrapType) {
            // Can't happen. See above.
            throw new RuntimeException("Should never get here...");
        } else {
            throw new RuntimeException("Unknown ref type: " + childRef);
        }

        // Replace wrapping expression in the parent.
        EMFHelper.updateParentContainment(wrap, childRef);
    }

    @SuppressWarnings("null")
    @Override
    protected void walkCompParamWrapExpression(CompParamWrapExpression wrap) {
        // Currently, we have a component parameter wrapping expression, which means that the expression object is in
        // the parameter's body. If we instantiate the definition where the parameter is a part of, the expression
        // object becomes part of the supplied argument's body. The supplied argument is either a concrete component or
        // a component instantiation (CompInst). Note that it is not possible that the argument is a component parameter
        // (CompParam), that is not allowed by Cif. Second note, in the original specification, the argument must have
        // been a CompInst, but it might already been eliminated. Third note, even if the leaf of the argument is a
        // concrete component, this concrete component can still be inside a CompInst or in a CompParam parameter, and
        // we'll end up with a CompInstWrapExpression or a CompParamWrapExpression, respectively.
        // So the task is to:
        // 1) Process the given argument to replace the CompParam part of the wrapper.
        // 2) process the expression part of the wrapper, and combine it with the result of 1.

        // Determine if we want to eliminate the parameter wrapping expression at all. That is, are we instantiating
        // the definition where this parameter is a part of?
        ComponentParameter param = wrap.getParameter();
        Expression arg = getActualArgument(param);
        if (arg == null) {
            // The parameter is not being instantiated, walk over it normally.
            super.walkCompParamWrapExpression(wrap);
            return;
        }

        ////////////////
        // Task 1
        ////////////////

        // In the first part we are going to fix the wrappers from the place where we are instantiating the definition
        // to where the supplied argument is located. For example, h.i.j.x1 below.
        //
        // group def D(X x):
        // invariant x.e.f.g.m.
        // end
        //
        // d : D(h.i.j.x1);

        // Initialize the resulting new reference expression. We clone it as it is possible that other expressions in
        // the definition also use this parameter.
        Expression rsltExpr = deepclone(arg);

        // So we know that this argument either directly or indirectly points to some concrete component or component
        // instantiation. With indirectly we mean via a component instantiation or via a component parameter. If the
        // component is pointed to via a wrapping expression, we still want to get the actual component. We can find
        // that component at the leaf. It is now always the next reference, as we can also have multiple nested wraps.
        // Initially, that component must have been a component instantiation, as that is required by Cif. However, it
        // can be that that component has already been instantiated, and is now a concrete component.
        Expression argLeaf = CifTypeUtils.unwrapExpression(rsltExpr);
        Assert.check(argLeaf instanceof ComponentExpression);
        Component argLeafComp = ((ComponentExpression)argLeaf).getComponent();

        // Process the component reference. Also get the body of that component, as this is will serve as the new body
        // for the first child reference.
        Expression rsltInnerWrap;
        ComplexComponent newBody;
        if (argLeafComp instanceof ComponentInst) {
            // The actual argument is an instantiation.
            ComponentInst argLeafInst = (ComponentInst)argLeafComp;

            // Since we already processed the actual argument, we know for sure that we are not instantiating this
            // component.
            Assert.check(!instMap.containsKey(argLeafInst));

            // So since the reference expression is going to be placed in an instantiation, we have to create a new
            // component instantiation wrapper. When we process the expression part we are going to put the result in
            // this wrapper, we do that in the second part.
            CompInstWrapExpression newWrap = newCompInstWrapExpression();
            newWrap.setInstantiation(argLeafInst);
            if (argLeaf == rsltExpr) {
                // This means there is no other 'via' expression, that is, the argument was not pointing via an
                // CompInst or CompParam. Hence, the actual argument was a ComponentExpression and not a wrapping
                // expression.
                Assert.check(arg instanceof ComponentExpression);

                // This means that the original CompParamWrapExpression that we are walking is going to be replaced
                // directly by the new CompInstWrapExpression. Because it is the only wrapping expression it is
                // automatically the inner wrapping expression.
                rsltExpr = newWrap;
                rsltInnerWrap = newWrap;
            } else {
                // This means that there are other 'via' expressions, either a CompInst or a CompParam. Hence, the
                // actual argument must be a wrapping expression.
                Assert.check(arg instanceof CompInstWrapExpression || arg instanceof CompParamWrapExpression);

                // The new wrap expression is going to be placed in another (existing) wrap expression. The leaf of
                // rsltExpr is argLeaf, which is a CompInst. We substitute CompInst with the new wrap, so we don't
                // have to recreate all the other wraps.
                EMFHelper.updateParentContainment(argLeaf, newWrap);
                rsltInnerWrap = newWrap;
            }

            // Get the body of the instantiation where the reference expression is going to be placed. Because the
            // component is an instantiation, it doesn't have its own body. Instead the body of its definition is used.
            ComponentDef argDef = CifTypeUtils.getCompDefFromCompInst(argLeafInst);
            newBody = argDef.getBody();
        } else {
            // The actual argument is a concrete component.
            Assert.check(argLeafComp instanceof ComplexComponent);
            ComplexComponent argLeafComplexComp = (ComplexComponent)argLeafComp;

            // We don't need wrapping expression for a concrete component, so we don't create any. However, it could be
            // that the leaf component is located in another CompInst or CompParam.
            if (argLeaf == rsltExpr) {
                // This means there is no other 'via' expression, the argument was not a wrapper.
                Assert.check(arg instanceof ComponentExpression);

                // This means that the original wrapping expression that we are walking is going to be replaced by a non
                // wrapping expression. So the rsltExpr is not a wrapper. And there is also no inner wrap.
                rsltExpr = null;
                rsltInnerWrap = null;
            } else {
                // This means that there are other 'via' expressions, either a CompInst or a CompParam. Hence, the
                // actual argument must be a wrapping expression.
                Assert.check(arg instanceof CompInstWrapExpression || arg instanceof CompParamWrapExpression);

                // The expression is going to be placed in another (existing) wrap expression. The leaf of rsltExpr is
                // argLeaf, which is a concrete component. We empty the leaf of that wrap, so we don't have to recreate
                // all the other wraps.
                rsltInnerWrap = (Expression)argLeaf.eContainer();
                EMFHelper.removeFromParentContainment(argLeaf);

                // Notice that rsltExpr already is a wrapper, where we have updated its leaf argument. rsltExpr could be
                // rsltInnerWrap, but there can also be multiple nested wraps.
            }

            // The expression is going to be part of the concrete component.
            newBody = argLeafComplexComp;
        }

        /////////////////////
        // Task 2
        /////////////////////

        // In the second part we are going to fix the wrappers from the supplied component to where the expression is
        // pointing, for example, x.e.f.g.m below.
        //
        // group def D(X x):
        // invariant x.e.f.g.m;
        // end
        //
        // d : D(h.i.j.x1);

        // Get body of component definition used as type of the component parameter being eliminated by this method. It
        // serves as the current body for the first child reference. We get it as the body from the CompDef type of the
        // component parameter.
        CifType paramType = wrap.getParameter().getType();
        paramType = CifTypeUtils.normalizeType(paramType);
        Assert.check(paramType instanceof ComponentDefType);
        ComponentDef paramDef = ((ComponentDefType)paramType).getDefinition();
        ComplexComponent curBody = paramDef.getBody();

        // Get child reference expression of the component parameter wrapping expression. This is the first child
        // reference to process.
        Expression childRef = wrap.getReference();

        // Process child reference. We either have
        // 1) non-wrapping expression (m below).
        //
        // group def D(X x):
        // invariant x.m;
        // end
        //
        // 2) CompInstWrapExpression (e.m below, if we are instantiating D but not E).
        //
        // group def X():
        // e: E();
        // end
        // group def D(X x):
        // invariant x.e.m;
        // end
        //
        // Note that it is impossible to have a CompParamWrapExpression due to scoping constraints. In the example below
        // it is not allowed to refer to component parameter e via x.
        //
        // group def X(E e):
        // ..
        // end
        // group def D(X x):
        // invariant x.e.m;
        // end
        //
        // First, we'll handle all the possible component instantiation wrapping expressions (zero or more levels).
        // Later on we'll process the leaf non-wrapping reference expression.
        Assert.check(!(childRef instanceof CompParamWrapExpression));

        while (childRef instanceof CompInstWrapExpression) {
            // Get the 'via' component instantiation.
            CompInstWrapExpression childWrap = (CompInstWrapExpression)childRef;
            ComponentInst viaInst = childWrap.getInstantiation();

            // Update child reference used in the while loop to next level.
            childRef = childWrap.getReference();

            // Make sure we have a valid reference to the instantiation at this level, for the new body.
            Component viaComp;
            if (curBody == newBody) {
                // Reference to instantiation is already valid, as (for the first level) the actual argument uses the
                // body of the component definition that is also the type of the component parameter that we are
                // eliminating. For later levels, similar conditions hold. That is, the already processed argument and
                // already processed part of the reference end up in the same scope as the scope that is used for the
                // child reference at this level.
                viaComp = viaInst;
            } else {
                // Need a new reference to the component instantiation, or the already instantiated variant of it.
                viaComp = (Component)getNonViaRefObj(viaInst, curBody, newBody);
            }

            // Process the instantiation at this level.
            if (viaComp instanceof ComplexComponent) {
                // A concrete component. No need to wrap anything.
                ComponentDef viaDef = CifTypeUtils.getCompDefFromCompInst(viaInst);
                curBody = viaDef.getBody();
                newBody = (ComplexComponent)viaComp;
            } else {
                // A component instantiation.
                Assert.check(viaComp instanceof ComponentInst);
                ComponentInst newViaInst = (ComponentInst)viaComp;

                // check whether we are instantiating the instantiation.
                ComplexComponent instComp = instMap.get(newViaInst);
                if (instComp == null) {
                    // We are not instantiating the instantiation.
                    CompInstWrapExpression newWrap = newCompInstWrapExpression();
                    newWrap.setInstantiation(viaInst);

                    // Add the new inner wrap.
                    if (rsltExpr == null) {
                        // This means that the supplied component is a concrete component which is pointed at directly,
                        // without wrapping expressions.
                        rsltExpr = newWrap;
                    } else {
                        // This means that either the supplied component is an instantiation, or that it is a concrete
                        // component which is pointed at via a CompInst wrap or a ParamInst wrap.
                        Assert.notNull(rsltInnerWrap);
                        if (rsltInnerWrap instanceof CompInstWrapExpression) {
                            ((CompInstWrapExpression)rsltInnerWrap).setReference(newWrap);
                        } else {
                            Assert.check(rsltInnerWrap instanceof CompParamWrapExpression);
                            ((CompParamWrapExpression)rsltInnerWrap).setReference(newWrap);
                        }
                    }
                    // Since we put a new wrap in the inner wrap, it is not the inner wrap anymore.
                    rsltInnerWrap = newWrap;

                    // Continue from body of the component definition.
                    ComponentDef viaDef = CifTypeUtils.getCompDefFromCompInst(viaInst);
                    curBody = viaDef.getBody();

                    ComponentDef newViaDef = CifTypeUtils.getCompDefFromCompInst(newViaInst);
                    newBody = newViaDef.getBody();
                } else {
                    // We are instantiating the instantiation, no need for wrapper.
                    ComponentDef viaDef = CifTypeUtils.getCompDefFromCompInst(viaInst);
                    curBody = viaDef.getBody();
                    newBody = instComp;
                }
            }
        }

        // Get referenced child object. Method getRefObjFromRef does not handle wrapping expressions. However, they
        // can't occur here:
        //
        // - CompInstWrapExpression: Handled above.
        // - CompParamWrapExpression: Due to scoping constraints, component parameters can not be referenced via
        // component instantiations.
        //
        // Also, 'childRef' can not be a component reference that refers to a component instantiation:
        //
        // - Assume that 'wrap' is a component instantiation 'x1' for component definition 'X'. Then, since we are
        // eliminating 'X' and 'x1', 'X' does not contain any component instantiations. As such, 'childRef' can not
        // reference a component instantiation.
        Assert.check(!(childRef instanceof CompParamWrapExpression));
        PositionObject refObj = CifScopeUtils.getRefObjFromRef(childRef);

        // Get non-via referenced object.
        Object newRefObj;
        if (curBody == newBody) {
            newRefObj = refObj;
        } else {
            newRefObj = getNonViaRefObj(refObj, curBody, newBody);
        }

        // In-place modify leaf reference expression.
        if (childRef instanceof ConstantExpression) {
            Constant c = (Constant)newRefObj;
            ((ConstantExpression)childRef).setConstant(c);
        } else if (childRef instanceof DiscVariableExpression) {
            DiscVariable v = (DiscVariable)newRefObj;
            ((DiscVariableExpression)childRef).setVariable(v);
        } else if (childRef instanceof AlgVariableExpression) {
            AlgVariable a = (AlgVariable)newRefObj;
            ((AlgVariableExpression)childRef).setVariable(a);
        } else if (childRef instanceof ContVariableExpression) {
            ContVariable v = (ContVariable)newRefObj;
            ((ContVariableExpression)childRef).setVariable(v);
        } else if (childRef instanceof LocationExpression) {
            Location l = (Location)newRefObj;
            ((LocationExpression)childRef).setLocation(l);
        } else if (childRef instanceof EnumLiteralExpression) {
            EnumLiteral l = (EnumLiteral)newRefObj;
            ((EnumLiteralExpression)childRef).setLiteral(l);
        } else if (childRef instanceof EventExpression) {
            Event e = (Event)newRefObj;
            ((EventExpression)childRef).setEvent(e);
        } else if (childRef instanceof FunctionExpression) {
            Function f = (Function)newRefObj;
            ((FunctionExpression)childRef).setFunction(f);
        } else if (childRef instanceof InputVariableExpression) {
            InputVariable v = (InputVariable)newRefObj;
            ((InputVariableExpression)childRef).setVariable(v);
        } else if (childRef instanceof ComponentExpression) {
            // This component reference can not reference a component instantiation. See above.
            ComponentExpression compRef = (ComponentExpression)childRef;
            Component c = (Component)newRefObj;
            compRef.setComponent(c);
        } else if (childRef instanceof CompInstWrapExpression) {
            // Can't happen. See above.
            throw new RuntimeException("Should never get here...");
        } else if (childRef instanceof CompParamWrapExpression) {
            // Can't happen. See above.
            throw new RuntimeException("Should never get here...");
        } else {
            throw new RuntimeException("Unknown ref expr: " + childRef);
        }

        // Put leaf reference into the result, and result into the metamodel.
        if (rsltExpr == null) {
            // This means that we have not constructed any wrappers so far.
            rsltExpr = childRef;
            EMFHelper.updateParentContainment(wrap, rsltExpr);
        } else {
            // This means that we have constructed at least one wrapper. We put the child as the leaf.
            Assert.notNull(rsltInnerWrap);
            EMFHelper.updateParentContainment(wrap, rsltExpr);

            if (rsltInnerWrap instanceof CompInstWrapExpression) {
                ((CompInstWrapExpression)rsltInnerWrap).setReference(childRef);
            } else {
                Assert.check(rsltInnerWrap instanceof CompParamWrapExpression);
                ((CompParamWrapExpression)rsltInnerWrap).setReference(childRef);
            }
        }

        // See whether type needs additional processing.
        walkCifType(childRef.getType());

        // Copy processed child reference type to all the wrappers.
        EObject ancestor = childRef.eContainer();
        while (ancestor instanceof CompInstWrapExpression || ancestor instanceof CompParamWrapExpression) {
            CifType newType = deepclone(childRef.getType());
            ((Expression)ancestor).setType(newType);
            ancestor = ancestor.eContainer();
        }
    }

    @Override
    protected void walkCompParamWrapType(CompParamWrapType wrap) {
        // Get actual argument, if any.
        ComponentParameter param = wrap.getParameter();
        Expression arg = getActualArgument(param);
        if (arg == null) {
            super.walkCompParamWrapType(wrap);
            return;
        }

        // What component does the actual argument refer to?
        Expression leafArg = CifTypeUtils.unwrapExpression(arg);
        Assert.check(leafArg instanceof ComponentExpression);
        Component compArg = ((ComponentExpression)leafArg).getComponent();

        // Special case for not yet instantiated actual component.
        if (compArg instanceof ComponentInst) {
            ComponentInst instArg = (ComponentInst)compArg;
            ComponentDef cdef = CifTypeUtils.getCompDefFromCompInst(instArg);
            if (!elimDefs.contains(cdef)) {
                // Actual argument is instantiation of a component that we are not yet eliminating. Change 'via param'
                // reference, to 'via instantiation' reference.
                arg = deepclone(arg);
                EMFHelper.updateParentContainment(wrap, arg);

                // Last part is a component type, which we need to change into a 'via' instantiation wrapping type. The
                // child is simply moved, as it still refers to something in the component definition that it already
                // pointed to. No need to map to a deep-cloned variant, as there is none.
                leafArg = CifTypeUtils.unwrapExpression(arg);
                Assert.check(leafArg instanceof ComponentExpression);
                compArg = ((ComponentExpression)leafArg).getComponent();
                Assert.check(compArg instanceof ComponentInst);

                CompInstWrapType newWrap = newCompInstWrapType();
                newWrap.setInstantiation((ComponentInst)compArg);
                newWrap.setReference(wrap.getReference());

                EMFHelper.updateParentContainment(leafArg, newWrap);
                return;
            }
        }

        // The child reference type.
        CifType childRef = wrap.getReference();

        // Get referenced child object.
        EObject refObj;
        if (childRef instanceof TypeRef) {
            refObj = ((TypeRef)childRef).getType();
        } else if (childRef instanceof EnumType) {
            refObj = ((EnumType)childRef).getEnum();
        } else if (childRef instanceof ComponentType) {
            // Assume that 'wrap' is a component parameter 'x1' of type component definition 'X'. Then, since the actual
            // argument was an already instantiated 'X', 'X' does not contain any component instantiations. As such,
            // 'childRef' can not reference a component instantiation.
            refObj = ((ComponentType)childRef).getComponent();
        } else if (childRef instanceof ComponentDefType) {
            // Assume that 'wrap' is a component parameter 'x1' of type component definition 'X'. Then, since the actual
            // argument was an already instantiated 'X', 'X' does not contain any component definitions. As such,
            // 'childRef' can not be a component definition reference.
            throw new RuntimeException("Invalid comp def type.");
        } else if (childRef instanceof CompInstWrapType) {
            // Assume that 'wrap' is a component parameter 'x1' of type component definition 'X'. Then, since the actual
            // argument was an already instantiated 'X', 'X' does not contain any component instantiations. As such,
            // 'childRef' can not be a component instantiation wrapping expression.
            throw new RuntimeException("Invalid comp inst wrap type.");
        } else if (childRef instanceof CompParamWrapType) {
            // Due to scoping constraints, component parameters can not be referenced via other component parameters.
            throw new RuntimeException("Invalid comp param wrap type.");
        } else {
            throw new RuntimeException("Unknown ref type: " + childRef);
        }

        // Get component used as actual argument.
        ComplexComponent instComp;
        if (compArg instanceof ComponentInst) {
            instComp = instMap.get(compArg);
            Assert.notNull(instComp);
        } else {
            instComp = (ComplexComponent)compArg;
        }

        // Get component definition body.
        CifType cdefType = wrap.getParameter().getType();
        cdefType = CifTypeUtils.normalizeType(cdefType);
        Assert.check(cdefType instanceof ComponentDefType);
        ComponentDef cdef = ((ComponentDefType)cdefType).getDefinition();
        ComplexComponent body = cdef.getBody();

        // Get non-via referenced object.
        Object newRefObj = getNonViaRefObj(refObj, body, instComp);

        // In-place modify child reference expression.
        if (childRef instanceof TypeRef) {
            TypeDecl t = (TypeDecl)newRefObj;
            ((TypeRef)childRef).setType(t);
        } else if (childRef instanceof EnumType) {
            EnumDecl e = (EnumDecl)newRefObj;
            ((EnumType)childRef).setEnum(e);
        } else if (childRef instanceof ComponentType) {
            // This component reference can not reference a component instantiation. See above.
            Component c = (Component)newRefObj;
            ((ComponentType)childRef).setComponent(c);
        } else if (childRef instanceof CompInstWrapType) {
            // Can't happen. See above.
            throw new RuntimeException("Should never get here...");
        } else if (childRef instanceof CompParamWrapType) {
            // Can't happen. See above.
            throw new RuntimeException("Should never get here...");
        } else {
            throw new RuntimeException("Unknown ref type: " + childRef);
        }

        // Replace wrapping expression in the parent.
        EMFHelper.updateParentContainment(wrap, childRef);
    }

    /**
     * For a component parameter, get the actual argument if the component parameter is being eliminated, and return
     * {@code null} otherwise. For component parameters that are being eliminated, process the actual argument, and
     * update all relevant mapping. This is necessary if the argument contains 'via component instantiations'
     * expressions for components that are being instantiated. The processing of actual argument is performed only once
     * per component parameter.
     *
     * @param param The component parameter.
     * @return The actual argument, potentially update due to being processed, or {@code null}.
     */
    private Expression getActualArgument(ComponentParameter param) {
        // Get actual argument, if any.
        Expression arg = compParamMap.get(param);
        if (arg == null) {
            return null;
        }

        // See whether actual argument has been processed before.
        Pair<ComponentInst, Integer> origInfo = paramOrigMap.get(param);
        if (origInfo == null) {
            // Processed before. Don't process it again.
            return arg;
        }

        // Process actual argument. Note that we can't have 'via parameter' references in the actual argument, as that
        // is not allowed. For example, in the example below, it is not allowed to use x as an argument for Z.
        //
        // group def Y(X x):
        // z : Z(x);
        // end
        //
        // Not that we can have 'via component instantiations' references in the actual argument. For example, x.y is a
        // reference via the component instantiation x to component instantiation y.
        //
        // x : X();
        // z : Z(x.y);
        //
        // The instantiation that we are eliminate, to which the actual argument is given, is part of a component
        // (definition) that has that parameter. Since the body of that component (definition) includes the
        // instantiation that we're eliminating, that component definition itself can't be eliminated during this
        // iteration (if it is a definition). Via a parameter reference we can't get to other parameter references,
        // so we know there are no parameter references in the actual argument that may need processing. Therefore, we
        // won't get into trouble with infinite recursion due to processing of the actual argument needing to process
        // this parameter again.
        walkExpression(arg);

        // Get potentially updated actual argument.
        ComponentInst inst = origInfo.left;
        int paramIdx = origInfo.right;
        Expression newArg = inst.getParameters().get(paramIdx);

        // update mappings.
        if (arg != newArg) {
            compParamMap.put(param, newArg);
        }
        paramOrigMap.remove(param);

        // Return potentially update argument.
        return newArg;
    }

    /**
     * For a referenced object, which is referenced via a component instantiation or component parameter, get the
     * referenced object without a 'via' reference.
     *
     * @param refObj The referenced object, which is referenced via a component instantiation or a component parameter.
     * @param compDefBody The body of the original component definition that the referenced object was/is a part of.
     * @param compInstBody The body of the instantiation that the referenced object is now a part of.
     * @return The referenced object, in relation to 'compInstBody'.
     */
    private Object getNonViaRefObj(EObject refObj, ComplexComponent compDefBody, ComplexComponent compInstBody) {
        // Get path from referred object to the component root. We have several cases here:
        //
        // 1) The reference is contained outside of any instantiated component definition. Then the reference was not
        // deep-cloned, and thus still refers to a descendant of the component definition. In this case, we create a
        // path to the referenced object in relation to the original body of the component definition. We then resolve
        // the result using a reverse path, from the 'compInstBody'.
        //
        // 2) The reference is contained inside of an instantiated component definition, and refers to a local object
        // inside that component definition, via a component instantiation wrapping. During deep-cloning, the child of
        // the component instantiation wrapping was changed to refer to the local object in the instantiation, instead
        // of in the component definition body. We have two cases here:
        //
        // 2a) The component instantiation wrapping is contained in the same component as the result of the
        // instantiation ('compInstBody') referred to by that wrapping. In this case, we already have a reference to a
        // descendant of 'compInstBody', and we are done.
        //
        // 2b) The component instantiation wrapping is contained in a different component than the result of the
        // instantiation ('compInstBody') referred to by that wrapping. In this case, we create a path to the referenced
        // object in relation to the body of the 'other' instantiation. We then resolve the result using a reverse path,
        // from the 'compInstBody'.

        // Get 'other' component instantiations than the one our wrapping is concerned with.
        Set<ComplexComponent> otherInstComps;
        otherInstComps = new LinkedHashSet<>(instMap.values());
        otherInstComps.remove(compInstBody);

        // Find roots.
        EObject curObj = refObj;
        boolean inDefBody = false;
        boolean inInstBody = false;
        ComplexComponent otherInstBody = null;
        while (curObj != null) {
            if (curObj == compDefBody) {
                inDefBody = true;
            }
            if (curObj == compInstBody) {
                inInstBody = true;
            }
            if (otherInstComps.contains(curObj)) {
                // It is an 'other' instantiation. Make sure we have only one of those.
                Assert.check(otherInstBody == null);
                otherInstBody = (ComplexComponent)curObj;
            }
            curObj = curObj.eContainer();
        }

        // Make sure we have only one root.
        int successes = 0;
        if (inDefBody) {
            successes++;
        }
        if (inInstBody) {
            successes++;
        }
        if (otherInstBody != null) {
            successes++;
        }
        Assert.check(successes == 1);

        // Create path to root, and get object in instantiated component.
        Object newRefObj;
        if (inDefBody) {
            // Resolve in component definition body.
            EMFPath path = new EMFPath(refObj, null, compDefBody);
            newRefObj = path.resolveAgainst(compInstBody);
        } else if (inInstBody) {
            // Already a correct reference.
            newRefObj = refObj;
        } else {
            // Resolve in body of 'other' instantiated component.
            EMFPath path = new EMFPath(refObj, null, otherInstBody);
            newRefObj = path.resolveAgainst(compInstBody);
        }

        // Return non-via referenced object.
        return newRefObj;
    }
}
