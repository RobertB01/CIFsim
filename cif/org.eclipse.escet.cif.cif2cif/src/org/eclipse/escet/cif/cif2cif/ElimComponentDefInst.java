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
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newCompInstWrapExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newCompInstWrapType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newCompParamWrapType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newComponentType;
import static org.eclipse.escet.common.emf.EMFHelper.deepclone;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Pair.pair;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.position.common.PositionUtils.copyPosition;

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
import org.eclipse.escet.cif.metamodel.cif.expressions.CompParamExpression;
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
import org.eclipse.escet.common.position.metamodel.position.Position;
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

    /** Whether component definitions were found during phase 1. */
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
     * Mapping of (cloned) component parameters to their arguments. Filled during phase 2. Used during phase 3.
     *
     * <p>
     * Don't look up anything in this mapping directly. Use the {@link #getArgument} method instead.
     * </p>
     */
    private Map<ComponentParameter, Expression> compParamMap;

    /**
     * Mapping of (cloned) component parameters to their original instantiations, and the index of the parameter. Filled
     * during phase 2, for all keys of {@link #compParamMap}. Used during phase 3. Once used, the entry is removed, so
     * that it is used only once.
     *
     * <p>
     * This mapping is to be used by the {@link #getArgument} method only, to update {@link #compParamMap} mapping.
     * </p>
     */
    private Map<ComponentParameter, Pair<ComponentInst, Integer>> paramOrigMap;

    /**
     * Mapping of (cloned) events from event parameters to their arguments. Filled during phase 2. Used during phase 3.
     */
    private Map<Event, Expression> eventParamMap;

    /**
     * Mapping of (cloned) locations from locations parameters to their arguments. Filled during phase 2. Used during
     * phase 3.
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

        // Fill parameter/argument mappings. Instantiate algebraic parameters.
        List<Parameter> params = cdef.getParameters();
        List<Expression> args = inst.getArguments();
        Assert.check(params.size() == args.size());
        for (int i = 0; i < params.size(); i++) {
            Parameter param = params.get(i);
            Expression arg = args.get(i);

            if (param instanceof AlgParameter) {
                // Note that 'arg' is deep-cloned, to make sure it doesn't disappear from 'args', where it was
                // previously contained.
                AlgVariable var = ((AlgParameter)param).getVariable();
                body.getDeclarations().add(var);
                var.setValue(deepclone(arg));
            } else if (param instanceof EventParameter) {
                Event event = ((EventParameter)param).getEvent();
                eventParamMap.put(event, arg);
            } else if (param instanceof LocationParameter) {
                Location loc = ((LocationParameter)param).getLocation();
                locParamMap.put(loc, arg);
            } else if (param instanceof ComponentParameter) {
                compParamMap.put((ComponentParameter)param, arg);
                paramOrigMap.put((ComponentParameter)param, pair(inst, i));
            } else {
                throw new RuntimeException("Unknown param: " + param);
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
        // Non-wrapped event reference expression. First, get argument, if any.
        Expression newRef = eventParamMap.get(evtRef.getEvent());
        if (newRef == null) {
            return;
        }

        // Copy argument.
        newRef = deepclone(newRef);

        // Use old position information for displaying potential warnings.
        setPositionInfo(newRef, evtRef.getPosition());

        // Replace reference by argument.
        EMFHelper.updateParentContainment(evtRef, newRef);

        // Make sure we process the argument, in case it contains references that we must process.
        walkExpression(newRef);
    }

    @Override
    protected void postprocessLocationExpression(LocationExpression locRef) {
        // Non-wrapped location reference expression. First, get argument, if any.
        Expression newRef = locParamMap.get(locRef.getLocation());
        if (newRef == null) {
            return;
        }

        // Copy argument.
        newRef = deepclone(newRef);

        // Use old position information for displaying potential warnings.
        setPositionInfo(newRef, locRef.getPosition());

        // Replace reference by argument.
        EMFHelper.updateParentContainment(locRef, newRef);

        // Make sure we process the argument, in case it contains references that we must process.
        walkExpression(newRef);
    }

    @Override
    protected void postprocessCompParamExpression(CompParamExpression compParamRef) {
        // Non-wrapped component parameter reference expression. First, get argument, if any.
        Expression newRef = getArgument(compParamRef.getParameter());
        if (newRef == null) {
            return;
        }

        // Copy argument.
        newRef = deepclone(newRef);

        // Use old position information for displaying potential warnings.
        setPositionInfo(newRef, compParamRef.getPosition());

        // Replace reference by argument.
        EMFHelper.updateParentContainment(compParamRef, newRef);

        // No need for processing the argument, getArgument method does that already.
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
        } else if (childRef instanceof CompParamExpression) {
            // Can't happen. Due to scoping constraints, component parameters can not be referenced via component
            // instantiations.
            throw new RuntimeException("Should never get here...");
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
            // Assume that 'wrap' (say 'x1.C') consists of a component instantiation 'x1' for component definition 'X'
            // and a reference to component definition 'C'. Then, since we are eliminating 'X' and 'x1', 'X' does no
            // longer contain that component definition ('C'). As such, we won't find that definition in the
            // instantiated component ('x1'). Every instantiation that has this type ('C') must have been instantiated
            // already. Only component parameters (say 'p') or component parameter references ('p') can still have this
            // type ('C'). Note there may be multiple 'via' instantiation wraps (e.g., 'r.y.x1.C'). Eventually the
            // parameter ('p') and thus this type will get replaced by an argument. Hence, we won't have to
            // eliminate this component definition type ('C').
            EObject parent = wrap.eContainer();
            while (parent instanceof CompInstWrapType) {
                parent = ((CompInstWrapType)parent).eContainer();
            }
            Assert.check(parent instanceof ComponentParameter || parent instanceof CompParamExpression);

            // We remove the wrap to properly indicate that the instantiation ('x1') has been removed. Otherwise we may
            // get trouble when the parent wrap ('y') gets removed, as it then references a component instantiation type
            // ('x1').
            EMFHelper.updateParentContainment(wrap, childRef);
            return;
        } else if (childRef instanceof CompInstWrapType) {
            // Assume that 'wrap' is a component instantiation 'x1' for component definition 'X'. Then, since we are
            // eliminating 'X' and 'x1', 'X' does not contain any component instantiations. As such, 'childRef' can not
            // be a component instantiation wrapping type.
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

        // In-place modify child reference type.
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
        } else if (childRef instanceof ComponentDefType) {
            // Already handled. See above.
            throw new RuntimeException("Should never get here...");
        } else if (childRef instanceof CompInstWrapType) {
            // Can't happen. See above.
            throw new RuntimeException("Should never get here...");
        } else if (childRef instanceof CompParamWrapType) {
            // Can't happen. See above.
            throw new RuntimeException("Should never get here...");
        } else {
            throw new RuntimeException("Unknown ref type: " + childRef);
        }

        // Replace wrapping type in the parent.
        EMFHelper.updateParentContainment(wrap, childRef);
    }

    @SuppressWarnings("null")
    @Override
    protected void walkCompParamWrapExpression(CompParamWrapExpression wrap) {
        // Consider the following running example that we'll use in comments throughout this method:
        //
        // group def D(P p):
        // invariant p.q.r.x;
        // end
        //
        // d: D(a.b.c.p1)
        //
        // We have a component definition 'D'. It has a component parameter 'p', which has component definition 'P' as
        // is type. For expression 'p.q.r.x', 'p' is a component parameter wrapping expression (and the argument to
        // this method), and 'q' is an object in the body of component definition 'P'. Component instantiation 'd'
        // instantiates component definition 'D' and provides argument 'a.b.c.p1' for parameter 'p' of 'D'. The referred
        // object 'q' will be part of 'a.b.c.p1', with 'p1' being an instance/instantiation of 'P'.
        //
        // We know the following regarding the component referred to by the argument ('p1'):
        //
        // 1) In the original specification, it ('p1') must have been a component instantiation that instantiates the
        // component definition ('P'), or a component parameter of type component definition 'P'. However, the
        // component definition might have already been instantiated earlier during this transformation. Hence, it is
        // now either a concrete component, a component instantiation, or a component parameter.
        //
        // 2) When it ('p1') is a concrete component or a component instantiation, it can still be inside a component
        // instantiation or referred to via a component parameter. It can thus be referred to via a component
        // instantiation or component parameter wrapping expression, respectively ('a.b.c').
        //
        // 3) When it ('p1') is a component parameter, it cannot be in a component instantiation or referred to via a
        // component parameter, due to scoping constraints.
        //
        // So the tasks are to:
        // 1) Process the given argument ('a.b.c.p1') to replace the component parameter wrapping expression ('p').
        // 2) Process the child reference expression ('q.r.x') and combine it with the result of task 1.

        // Determine whether we want to eliminate the parameter wrapping expression at all. That is, are we
        // instantiating the definition (e.g. 'D' in the example) that this parameter is a part of?
        ComponentParameter param = wrap.getParameter();
        Expression arg = getArgument(param);
        if (arg == null) {
            // The parameter is not being instantiated, walk over it normally.
            super.walkCompParamWrapExpression(wrap);
            return;
        }

        ///////////////////////////////////////////////////////////////////////////
        // Task 1
        ///////////////////////////////////////////////////////////////////////////

        // Task 1: Process the given argument ('a.b.c.p1') to replace the component parameter wrapping expression ('p').

        // Initialize the resulting new reference expression to the argument ('a.b.c.p1'). We clone it as there may be
        // multiple references via the parameter (e.g. 'p').
        Expression rsltExpr = deepclone(arg);

        // The argument either directly ('p1') or indirectly ('a.b.c.p1') points to some concrete component,
        // component instantiation ('p1'), or component parameter. It may be referred to by wrapping expressions
        // ('a.b.c'). We first obtain the argument's leaf component ('p1').
        Expression argLeaf = CifTypeUtils.unwrapExpression(rsltExpr);
        Assert.check(argLeaf instanceof ComponentExpression || argLeaf instanceof CompParamExpression);

        if (argLeaf instanceof CompParamExpression) {
            // The argument ('a.b.c.p1') is a component parameter. Note that since the argument refers to a component
            // parameter ('p1'), it cannot be a 'via' reference. That is because CIF considers parameters to be internal
            // and thus not in scope to be referred to via other wrapping expressions. As a result, 'a.b.c.p1' is
            // actually just 'p1'.
            Assert.check(argLeaf == rsltExpr);

            // The instantiation ('d') must be contained inside a component definition ('E'), see below:
            //
            // group def E(P p1):
            // group def D(P p):
            // invariant p.q.r.x;
            // end
            //
            // d: D(p1)
            // end

            // We replace the component parameter ('p') in the wrapping expression ('p.q.r.x') with the argument
            // component parameter ('p1').
            ComponentParameter compParam = ((CompParamExpression)argLeaf).getParameter();
            wrap.setParameter(compParam);

            // Since the outer component definition ('E') contains a component instantiation ('d'), this component
            // definition will not be eliminated this round. Hence, the supplied component parameter ('p1') and this
            // component parameter wrap ('p1.q.r.x') will not be eliminated. We don't need to process them further.
            Assert.check(getArgument(compParam) == null);

            // The child reference expression ('q.r.x') might need further processing.
            super.walkCompParamWrapExpression(wrap);
            return;
        }

        // The argument ('a.b.c.p1') is not a component parameter, it is either a concrete component or a component
        // instantiation.
        Component argLeafComp = ((ComponentExpression)argLeaf).getComponent();

        // Process the component reference ('p1'). Also get the body of that component, as the first child reference
        // expression ('q') will refer to an object in that body.
        Expression rsltInnerWrap;
        ComplexComponent newBody;
        if (argLeafComp instanceof ComponentInst) {
            // The argument leaf ('p1') is a component instantiation.
            ComponentInst argLeafInst = (ComponentInst)argLeafComp;

            // Since we already processed the argument ('a.b.c.p1'), we know for sure that we are not
            // instantiating this component instantiation ('p1') yet.
            Assert.check(!instMap.containsKey(argLeafInst));

            // Since the child reference expression ('q.r.x') is going to be placed in an instantiation ('p1'), we
            // have to create a new component instantiation wrapping expression (for 'p1'), as it is currently not a
            // 'via' reference expression. The child of the new wrapper will be set during task 2.
            CompInstWrapExpression newWrap = newCompInstWrapExpression();
            newWrap.setInstantiation(argLeafInst);
            if (argLeaf == rsltExpr) {
                // The argument consists of only a direct reference ('p1'), and there are no other 'via' references
                // (no 'a.b.c').
                Assert.check(arg instanceof ComponentExpression);

                // The result will be 'p1.q.r.x'. Thus the new 'p1' wrapper is the result and also the inner-most
                // wrapper that will contain 'q'.
                rsltExpr = newWrap;
                rsltInnerWrap = newWrap;
            } else {
                // The argument is an indirect reference to 'p1', through other 'via' references ('a.b.c').
                Assert.check(arg instanceof CompInstWrapExpression || arg instanceof CompParamWrapExpression);

                // The result will be 'a.b.c.p1.q.r.x'. Thus the new 'p1' wrapper will replace the old leaf 'p1'
                // reference in 'c' and it will also be the inner-most wrapper that will contain 'q' as a child.
                EMFHelper.updateParentContainment(argLeaf, newWrap);
                rsltInnerWrap = newWrap;
            }

            // Get the body of the instantiation (body of 'p1') in which to locate 'q'. Because the component ('p1') is
            // an instantiation, it doesn't have its own body. Instead, the body of its definition is used.
            ComponentDef argDef = CifTypeUtils.getCompDefFromCompInst(argLeafInst);
            newBody = argDef.getBody();
        } else {
            // The argument leaf ('p1') is a concrete component.
            Assert.check(argLeafComp instanceof ComplexComponent);
            ComplexComponent argLeafComplexComp = (ComplexComponent)argLeafComp;

            // In the CIF metamodel, concrete components don't need a 'via' wrapping expression. However, it may have
            // been indirectly referenced ('a.b.c').
            if (argLeaf == rsltExpr) {
                // The argument consists of only a direct reference ('p1'), and there are no other 'via' references
                // (no 'a.b.c').
                Assert.check(arg instanceof ComponentExpression);

                // The result will be 'q.r.x'. Thus 'q' itself will be the result and it will not have a parent
                // wrapper expression.
                rsltExpr = null;
                rsltInnerWrap = null;
            } else {
                // The argument is an indirect reference to 'p1', through other 'via' references ('a.b.c').
                Assert.check(arg instanceof CompInstWrapExpression || arg instanceof CompParamWrapExpression);

                // The result will be 'a.b.c.q.r.x'. Thus 'c' will be the parent of 'q' and the 'p1' reference can be
                // removed. In this case 'a' remains the result, but in cases where there is no 'a.b.c' but only 'c',
                // then 'c' is both the result and the parent of 'q'.
                rsltInnerWrap = (Expression)argLeaf.eContainer();
                EMFHelper.removeFromParentContainment(argLeaf);
            }

            // The first child reference ('q') will be located in the concrete component.
            newBody = argLeafComplexComp;
        }

        ///////////////////////////////////////////////////////////////////////////
        // Task 2
        ///////////////////////////////////////////////////////////////////////////

        // Task 2: Process the child reference expression ('q.r.x') and combine it with the result of task 1.

        // Get body of component definition ('P') used as type of the component parameter ('p') being eliminated by
        // this method. It serves as the current body for the first child reference ('q').
        CifType paramType = wrap.getParameter().getType();
        paramType = CifTypeUtils.normalizeType(paramType);
        Assert.check(paramType instanceof ComponentDefType);
        ComponentDef paramDef = ((ComponentDefType)paramType).getDefinition();
        ComplexComponent curBody = paramDef.getBody();

        // Get child reference expression ('q') of the component parameter wrapping expression ('p'). This is the first
        // child reference to process (from 'q.r.x').
        Expression childRef = wrap.getReference();

        // Process child reference expressions ('q.r.x'). We either have a non-wrapping expression (no 'q.r' and only
        // 'x' directly) or one or more component instantiation wrapping expression ('q.r' before 'x'). Given that 'p'
        // is a 'via' component parameter reference, 'q' and 'r' can't be 'via' component parameter references, as CIF
        // considers 'q' and 'r' to be internal and thus not in scope to be referred to via other wrapping expressions.
        Assert.check(!(childRef instanceof CompParamWrapExpression));

        // First, we'll handle all the zero or more component instantiation wrapping expressions ('q.r'). Later on
        // we'll process the leaf non-wrapping reference expression ('x').
        while (childRef instanceof CompInstWrapExpression) {
            // Get the 'via' component instantiation.
            CompInstWrapExpression childWrap = (CompInstWrapExpression)childRef;
            ComponentInst viaInst = childWrap.getInstantiation();

            // Update child reference used in the while loop to next level (e.g. from 'q' to 'r' or 'r' to 'x').
            childRef = childWrap.getReference();

            // Check whether we are instantiating the instantiation ('q' or 'r').
            ComplexComponent instComp = instMap.get(viaInst);
            if (instComp == null) {
                // We are not instantiating the instantiation.
                CompInstWrapExpression newWrap = newCompInstWrapExpression();
                newWrap.setInstantiation(viaInst);

                // Add the new inner wrapper.
                if (rsltExpr == null) {
                    // This means that the argument is a concrete component which is pointed at directly, without
                    // wrapping expressions.
                    rsltExpr = newWrap;
                } else {
                    // This means that either the argument is a component instantiation, or that it is a concrete
                    // component which is pointed at 'via' at least one wrapping expression.
                    Assert.notNull(rsltInnerWrap);
                    if (rsltInnerWrap instanceof CompInstWrapExpression) {
                        ((CompInstWrapExpression)rsltInnerWrap).setReference(newWrap);
                    } else {
                        Assert.check(rsltInnerWrap instanceof CompParamWrapExpression);
                        ((CompParamWrapExpression)rsltInnerWrap).setReference(newWrap);
                    }
                }

                // The new wrapper is now the inner wrapper.
                rsltInnerWrap = newWrap;

                // Continue from body of the component definition.
                ComponentDef viaDef = CifTypeUtils.getCompDefFromCompInst(viaInst);

                // We are not instantiating the instantiation, the body will remain the same.
                curBody = viaDef.getBody();
                newBody = viaDef.getBody();
            } else {
                // We are instantiating the instantiation, which means the component definition to instantiate
                // won't contain other component instantiations (see phase 1), and this is the last component
                // instantiation wrapper to consider. We're done with the loop.
                Assert.check(!(childRef instanceof CompInstWrapExpression));

                // We are instantiating the instantiation, so no need for a wrapper.
                ComponentDef viaDef = CifTypeUtils.getCompDefFromCompInst(viaInst);
                curBody = viaDef.getBody();
                newBody = instComp;
            }
        }

        // Get referenced leaf object ('x'). Method 'CifScopeUtils.getRefObjFromRef' does not handle wrapping
        // expressions. However, this is the leaf expression and thus not a 'via' reference.
        Assert.check(!(childRef instanceof CompInstWrapExpression));
        Assert.check(!(childRef instanceof CompParamWrapExpression));
        PositionObject refObj = CifScopeUtils.getRefObjFromRef(childRef);

        // Get non-via referenced object.
        Object newRefObj;
        if (curBody == newBody) {
            newRefObj = refObj;
        } else {
            newRefObj = getNonViaRefObj(refObj, curBody, newBody);
        }

        // In-place modify leaf reference expression ('x').
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
            Component c = (Component)newRefObj;
            ((ComponentExpression)childRef).setComponent(c);
            // Could be a component instantiation that is now instantiated, make sure to update that.
            walkComponentExpression((ComponentExpression)childRef);
        } else if (childRef instanceof CompParamExpression) {
            // Can't happen. The leaf reference expression ('x') is/was referenced 'via' a component parameter
            // ('p.q.r.x'). CIF considers parameters to be internal and thus not in scope to be referred to via other
            // wrapping expressions.
            throw new RuntimeException("Should never get here...");
        } else if (childRef instanceof CompInstWrapExpression) {
            // Can't happen. See above.
            throw new RuntimeException("Should never get here...");
        } else if (childRef instanceof CompParamWrapExpression) {
            // Can't happen. See above.
            throw new RuntimeException("Should never get here...");
        } else {
            throw new RuntimeException("Unknown ref expr: " + childRef);
        }

        // Put leaf reference ('x') into the result, and result into the model.
        if (rsltExpr == null) {
            // This means that we have not constructed any wrappers so far.
            rsltExpr = childRef;
            EMFHelper.updateParentContainment(wrap, rsltExpr);
        } else {
            // This means that we have constructed at least one wrapper. We put the leaf reference ('x') as the leaf
            // (i.e., child of inner wrapper).
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

        // Copy processed leaf reference type to all the wrappers.
        EObject ancestor = childRef.eContainer();
        while (ancestor instanceof CompInstWrapExpression || ancestor instanceof CompParamWrapExpression) {
            CifType newType = deepclone(childRef.getType());
            ((Expression)ancestor).setType(newType);
            ancestor = ancestor.eContainer();
        }
    }

    @SuppressWarnings("null")
    @Override
    protected void walkCompParamWrapType(CompParamWrapType wrap) {
        // Consider the following running example that we'll use in comments throughout this method:
        //
        // group def D(P p):
        // input p.q.r.x z;
        // end
        //
        // d: D(a.b.c.p1)
        //
        // We have a component definition 'D'. It has a component parameter 'p', which has component definition 'P' as
        // is type. For type 'p.q.r.x', 'p' is a component parameter wrapping type (and the argument to
        // this method), and 'q' is an object in the body of component definition 'P'. Component instantiation 'd'
        // instantiates component definition 'D' and provides argument 'a.b.c.p1' for parameter 'p' of 'D'. The referred
        // object 'q' will be part of 'a.b.c.p1', with 'p1' being an instance/instantiation of 'P'.
        //
        // We know the following regarding the component referred to by the argument ('p1'):
        //
        // 1) In the original specification, it ('p1') must have been a component instantiation that instantiates the
        // component definition ('P'), or a component parameter of type component definition 'P'. However, the
        // component definition might have already been instantiated earlier during this transformation. Hence, it is
        // now either a concrete component, a component instantiation, or a component parameter.
        //
        // 2) When it ('p1') is a concrete component or a component instantiation, it can still be inside a component
        // instantiation or referred to via a component parameter. It can thus be referred to via a component
        // instantiation or component parameter wrapping expression, respectively ('a.b.c').
        //
        // 3) When it ('p1') is a component parameter, it cannot be in a component instantiation or referred to via a
        // component parameter, due to scoping constraints.
        //
        // So the tasks are to:
        // 1) Process the given argument ('a.b.c.p1') to replace the component parameter wrapping type ('p').
        // 2) Process the child reference type ('q.r.x') and combine it with the result of task 1.

        // Determine whether we want to eliminate the parameter wrapping type at all. That is, are we
        // instantiating the definition (e.g. 'D' in the example) that this parameter is a part of?
        ComponentParameter param = wrap.getParameter();
        Expression arg = getArgument(param);
        if (arg == null) {
            // The parameter is not being instantiated, walk over it normally.
            super.walkCompParamWrapType(wrap);
            return;
        }

        ///////////////////////////////////////////////////////////////////////////
        // Task 1
        ///////////////////////////////////////////////////////////////////////////

        // Task 1: Process the given argument ('a.b.c.p1') to replace the component parameter wrapping type ('p').

        // Initialize the resulting new reference type.
        CifType rsltType;

        // The argument either directly ('p1') or indirectly ('a.b.c.p1') points to some concrete component,
        // component instantiation ('p1'), or component parameter. It may be referred to by wrapping expressions
        // ('a.b.c'). We first obtain the argument's leaf component ('p1').
        Expression argLeaf = CifTypeUtils.unwrapExpression(arg);
        Assert.check(argLeaf instanceof ComponentExpression || argLeaf instanceof CompParamExpression);

        if (argLeaf instanceof CompParamExpression) {
            // The argument ('a.b.c.p1') is a component parameter. Note that since the argument refers to a component
            // parameter ('p1'), it cannot be a 'via' reference. That is because CIF considers parameters to be internal
            // and thus not in scope to be referred to via other wrapping expressions. As a result, 'a.b.c.p1' is
            // actually just 'p1'.
            Assert.check(argLeaf == arg);

            // The instantiation ('d') must be contained inside a component definition ('E'), see below:
            //
            // group def E(P p1):
            // group def D(P p):
            // input p.q.r.x z;
            // end
            //
            // d: D(p1)
            // end

            // We replace the component parameter ('p') in the wrapping type ('p.q.r.x') with the argument component
            // parameter ('p1').
            ComponentParameter compParam = ((CompParamExpression)argLeaf).getParameter();
            wrap.setParameter(compParam);

            // Since the outer component definition ('E') contains a component instantiation ('d'), this component
            // definition will not be eliminated this round. Hence, the supplied component parameter ('p1') and this
            // component parameter wrap ('p1.q.r.x') will not be eliminated. We don't need to process them further.
            Assert.check(getArgument(compParam) == null);

            // The child reference type ('q.r.x') might need further processing.
            super.walkCompParamWrapType(wrap);
            return;
        }

        // The argument ('a.b.c.p1') is not a component parameter, it is either a concrete component or a component
        // instantiation.
        Component argLeafComp = ((ComponentExpression)argLeaf).getComponent();

        // Process the component reference ('p1'). Also get the body of that component, as the first child reference
        // type ('q') will refer to an object in that body.
        CifType rsltInnerWrap;
        ComplexComponent newBody;
        if (argLeafComp instanceof ComponentInst) {
            // The argument leaf ('p1') is a component instantiation.
            ComponentInst argLeafInst = (ComponentInst)argLeafComp;

            // Since we already processed the argument ('a.b.c.p1'), we know for sure that we are not
            // instantiating this component instantiation ('p1') yet.
            Assert.check(!instMap.containsKey(argLeafInst));

            // Since the child reference type ('q.r.x') is going to be placed in an instantiation ('p1'), we
            // have to create a new component instantiation wrapping type (for 'p1') for it.
            // The child of the new wrapper will be set during task 2.
            CompInstWrapType newWrap = newCompInstWrapType();
            newWrap.setInstantiation(argLeafInst);
            if (argLeaf == arg) {
                // The argument consists of only a direct reference ('p1'), and there are no other 'via' references
                // (no 'a.b.c').
                Assert.check(arg instanceof ComponentExpression);

                // The result will be 'p1.q.r.x'. Thus the new 'p1' wrapper is the result and also the inner-most
                // wrapper that will contain 'q'.
                rsltType = newWrap;
                rsltInnerWrap = newWrap;
            } else {
                // The argument is an indirect reference to 'p1', through other 'via' references ('a.b.c').
                Assert.check(arg instanceof CompInstWrapExpression || arg instanceof CompParamWrapExpression);

                // The result will be 'a.b.c.p1.q.r.x'. Thus the new 'p1' wrapper will replace the old leaf 'p1'
                // reference in 'c' and it will also be the inner-most wrapper that will contain 'q' as a child.
                // We convert 'a.b.c' from wrapping expressions to wrapping types, setting the new 'p1' as new leaf.
                rsltType = convertWrapExprToWrapType(arg, newWrap);
                rsltInnerWrap = newWrap;
            }

            // Get the body of the instantiation (body of 'p1') in which to locate 'q'. Because the component ('p1') is
            // an instantiation, it doesn't have its own body. Instead, the body of its definition is used.
            ComponentDef argDef = CifTypeUtils.getCompDefFromCompInst(argLeafInst);
            newBody = argDef.getBody();
        } else {
            // The argument leaf ('p1') is a concrete component.
            Assert.check(argLeafComp instanceof ComplexComponent);
            ComplexComponent argLeafComplexComp = (ComplexComponent)argLeafComp;

            // In the CIF metamodel, concrete components don't need a 'via' wrapping type. However, it may have
            // been indirectly referenced ('a.b.c').
            if (argLeaf == arg) {
                // The argument consists of only a direct reference ('p1'), and there are no other 'via' references
                // (no 'a.b.c').
                Assert.check(arg instanceof ComponentExpression);

                // The result will be 'q.r.x'. Thus 'q' itself will be the result and it will not have a parent
                // wrapper type.
                rsltType = null;
                rsltInnerWrap = null;
            } else {
                // The argument is an indirect reference to 'p1', through other 'via' references ('a.b.c').
                Assert.check(arg instanceof CompInstWrapExpression || arg instanceof CompParamWrapExpression);

                // The result will be 'a.b.c.q.r.x'. Thus 'c' will be the parent of 'q'. In this case 'a' remains the
                // result, but in cases where there is no 'a.b.c' but only 'c', then 'c' is both the result and the
                // parent of 'q'.
                //
                // We convert 'a.b.c' from wrapping expressions to wrapping types. We use a dummy boolean type as the
                // leaf (replacing 'p1'), to access the inner-most wrapping type ('c') after the conversion. We remove
                // the dummy type at the end, as in task 2 it will be 'q' that is the child of 'c'.
                CifType dummy = newBoolType();
                rsltType = convertWrapExprToWrapType(arg, dummy);
                rsltInnerWrap = (CifType)dummy.eContainer();
                EMFHelper.removeFromParentContainment(dummy);
            }

            // The first child reference ('q') will be located in the concrete component.
            newBody = argLeafComplexComp;
        }

        ///////////////////////////////////////////////////////////////////////////
        // Task 2
        ///////////////////////////////////////////////////////////////////////////

        // Task 2: Process the child reference type ('q.r.x') and combine it with the result of task 1.

        // Get body of component definition ('P') used as type of the component parameter ('p') being eliminated by
        // this method. It serves as the current body for the first child reference ('q').
        CifType paramType = wrap.getParameter().getType();
        paramType = CifTypeUtils.normalizeType(paramType);
        Assert.check(paramType instanceof ComponentDefType);
        ComponentDef paramDef = ((ComponentDefType)paramType).getDefinition();
        ComplexComponent curBody = paramDef.getBody();

        // Get child reference type ('q') of the component parameter wrapping type ('p'). This is the first
        // child reference to process (from 'q.r.x').
        CifType childRef = wrap.getReference();

        // Process child reference types ('q.r.x'). We either have a non-wrapping type (no 'q.r' and only
        // 'x' directly) or one or more component instantiation wrapping types ('q.r' before 'x'). Given that 'p'
        // is a 'via' component parameter reference, 'q' and 'r' can't be 'via' component parameter references, as CIF
        // considers 'q' and 'r' to be internal and thus not in scope to be referred to via other wrapping types.
        Assert.check(!(childRef instanceof CompParamWrapType));

        // First, we'll handle all the zero or more component instantiation wrapping types ('q.r'). Later on
        // we'll process the leaf non-wrapping reference type ('x').
        while (childRef instanceof CompInstWrapType) {
            // Get the 'via' component instantiation.
            CompInstWrapType childWrap = (CompInstWrapType)childRef;
            ComponentInst viaInst = childWrap.getInstantiation();

            // Update child reference used in the while loop to next level (e.g. from 'q' to 'r' or 'r' to 'x').
            childRef = childWrap.getReference();

            // Check whether we are instantiating the instantiation ('q' or 'r').
            ComplexComponent instComp = instMap.get(viaInst);

            if (instComp == null) {
                // We are not instantiating the instantiation.
                CompInstWrapType newWrap = newCompInstWrapType();
                newWrap.setInstantiation(viaInst);

                // Add the new inner wrapper.
                if (rsltType == null) {
                    // This means that the argument is a concrete component which is pointed at directly, without
                    // wrapping expressions.
                    rsltType = newWrap;
                } else {
                    // This means that either the argument is a component instantiation, or that it is a concrete
                    // component which is pointed at 'via' at least one wrapping expression (converted to a
                    // wrapping type).
                    Assert.notNull(rsltInnerWrap);
                    if (rsltInnerWrap instanceof CompInstWrapType) {
                        ((CompInstWrapType)rsltInnerWrap).setReference(newWrap);
                    } else {
                        Assert.check(rsltInnerWrap instanceof CompParamWrapType);
                        ((CompParamWrapType)rsltInnerWrap).setReference(newWrap);
                    }
                }

                // The new wrapper is now the inner wrapper.
                rsltInnerWrap = newWrap;

                // Continue from body of the component definition.
                ComponentDef viaDef = CifTypeUtils.getCompDefFromCompInst(viaInst);

                // We are not instantiating the instantiation, the body will remain the same.
                curBody = viaDef.getBody();
                newBody = viaDef.getBody();
            } else {
                // We are instantiating the instantiation, which means the component definition to instantiate
                // won't contain other component instantiations (see phase 1), and this is the last component
                // instantiation wrapper to consider. We're done with the loop.
                Assert.check(!(childRef instanceof CompInstWrapType));

                // We are instantiating the instantiation, so no need for a wrapper.
                ComponentDef viaDef = CifTypeUtils.getCompDefFromCompInst(viaInst);
                curBody = viaDef.getBody();
                newBody = instComp;
            }
        }

        // Get referenced leaf object ('x'). We know that:
        //
        // 1) This is the leaf type and thus not a 'via' reference.
        //
        // 2) This is not a component definition type. These can only be used as types of component parameters.
        // If 'x' was indeed a component parameter type, and 'p' is also a component parameter type, that means that
        // component definition 'D' that has 'p' as a parameter also contains a component definition that has a
        // parameter of which the type is 'x'. But that can't be the case, as we are eliminating component definition
        // 'D' and we only eliminate component definitions that don't contain other component definitions.
        Assert.check(!(childRef instanceof CompInstWrapType));
        Assert.check(!(childRef instanceof CompParamWrapType));
        Assert.check(!(childRef instanceof ComponentDefType));
        PositionObject refObj = CifScopeUtils.getRefObjFromRef(childRef);

        // Get non-via referenced object.
        Object newRefObj;
        if (curBody == newBody) {
            newRefObj = refObj;
        } else {
            newRefObj = getNonViaRefObj(refObj, curBody, newBody);
        }

        // In-place modify leaf reference type ('x').
        if (childRef instanceof TypeRef) {
            TypeDecl t = (TypeDecl)newRefObj;
            ((TypeRef)childRef).setType(t);
        } else if (childRef instanceof EnumType) {
            EnumDecl e = (EnumDecl)newRefObj;
            ((EnumType)childRef).setEnum(e);
        } else if (childRef instanceof ComponentType) {
            Component c = (Component)newRefObj;
            ((ComponentType)childRef).setComponent(c);
            // Could be a component instantiation that is now instantiated, make sure to update that.
            walkComponentType((ComponentType)childRef);
        } else if (childRef instanceof ComponentDefType) {
            // Can't happen. See above.
            throw new RuntimeException("Should never get here...");
        } else if (childRef instanceof CompInstWrapType) {
            // Can't happen. See above.
            throw new RuntimeException("Should never get here...");
        } else if (childRef instanceof CompParamWrapType) {
            // Can't happen. See above.
            throw new RuntimeException("Should never get here...");
        } else {
            throw new RuntimeException("Unknown ref type: " + childRef);
        }

        // Put leaf reference ('x') into the result, and result into the model.
        if (rsltType == null) {
            // This means that we have not constructed any wrappers so far.
            rsltType = childRef;
            EMFHelper.updateParentContainment(wrap, rsltType);
        } else {
            // This means that we have constructed at least one wrapper. We put the leaf reference ('x') as the leaf
            // (i.e., child of inner wrapper).
            Assert.notNull(rsltInnerWrap);
            EMFHelper.updateParentContainment(wrap, rsltType);

            if (rsltInnerWrap instanceof CompInstWrapType) {
                ((CompInstWrapType)rsltInnerWrap).setReference(childRef);
            } else {
                Assert.check(rsltInnerWrap instanceof CompParamWrapType);
                ((CompParamWrapType)rsltInnerWrap).setReference(childRef);
            }
        }
    }

    /**
     * For a component parameter, get the argument if the component parameter is being eliminated, and return
     * {@code null} otherwise. For component parameters that are being eliminated, process the argument, and
     * update all relevant mappings. This is necessary if the argument contains 'via component instantiation'
     * expressions for components that are being instantiated. The processing of arguments is performed only once
     * per component parameter.
     *
     * @param param The component parameter.
     * @return The argument, potentially update due to being processed, or {@code null}.
     */
    private Expression getArgument(ComponentParameter param) {
        // Get argument, if any.
        Expression arg = compParamMap.get(param);
        if (arg == null) {
            return null;
        }

        // See whether the argument has been processed before.
        Pair<ComponentInst, Integer> origInfo = paramOrigMap.get(param);
        if (origInfo == null) {
            // Processed before. Don't process it again.
            return arg;
        }

        // Process the argument. Note that the argument can be a direct reference to a component or component
        // instantiation, a 'via' parameter reference, a 'via' instantiation reference, or a direct component parameter
        // reference. For direct references, no mappings have to be updated. For 'via' references, it can be that a
        // (child) component was instantiated. In that case, mappings have to be updated.
        //
        // Consider the example below:
        //
        // group def X():
        // y: Y();
        // end
        //
        // x: X();
        // z: Z(x.y);
        //
        // Initially, the argument ('x.y') refers to a component instantiation ('y') that is part of a component
        // definition ('X'). When the component definitions (first 'Y' then 'X') get instantiated, the argument refers
        // to a concrete component ('y') that is part of a concrete component ('x'). The argument has to be
        // updated from a 'via' instantiation reference ('y' via 'x') to a direct component reference ('y').
        //
        // Note that when processing the argument ('x.y') we can't have infinite recursion for component
        // parameters (neither for 'x', nor for 'y'):
        //
        // 1) Consider an argument ('x.y') that has a 'via' component parameter reference ('x'). The component
        // parameter ('x') must come from a component definition (say 'C') that has that parameter ('x'). The
        // argument ('x.y') is an argument of a component instantiation ('z'). The outer component definition ('C')
        // contains that component instantiation ('z'), as otherwise the component parameter ('x') would not be in
        // scope. Since the outer component definition ('C') contains a component instantiation ('z'), 'C' can't be
        // instantiated during this round. Since 'C' is not being instantiated, its parameter ('x') is also not being
        // eliminated during this round. Then there can't be an infinite recursion for the component parameter ('x') as
        // this method skips component parameters that are not being instantiated during this round.
        //
        // 2) Consider an argument ('x.y') that refers to a component parameter ('y') 'via' a component
        // instantiation ('x') or component parameter ('x'). This can't happen as CIF considers the component parameter
        // ('y') internal to the component definition. That is, the CIF scoping rules don't allow referring to it ('y')
        // 'via' a component instantiation/parameter ('x'). Then there can't be an infinite recursion for the component
        // parameter 'y' as there can be no such references to begin with.
        //
        // 3) Consider an argument ('x') that is a direct component parameter reference. The component parameter
        // ('x') must come from a component definition (say 'C') that has that parameter ('x'). The argument
        // ('x') is an argument of a component instantiation ('z'). The outer component definition ('C') contains that
        // component instantiation ('z'), as otherwise the component parameter ('x') would not be in scope. The only way
        // for there to be infinite recursion would be for the argument (component parameter 'x') to be the
        // argument for that same parameter of the component definition being instantiated (parameter 'x' of
        // component definition 'C' being instantiated as 'z'). That means that 'C' contains 'z', which is an instance
        // of 'C', which leads to self-instantiation, which is not allowed in CIF.
        //
        // 4) Via component instantiation references and direct component references don't refer to a component
        // parameter at all, and thus trivially don't lead to infinite recursions on them.
        walkExpression(arg);

        // Get potentially updated argument.
        ComponentInst inst = origInfo.left;
        int paramIdx = origInfo.right;
        Expression newArg = inst.getArguments().get(paramIdx);

        // Update mappings.
        if (arg != newArg) {
            compParamMap.put(param, newArg);
        }
        paramOrigMap.remove(param);

        // Return potentially updated argument.
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

    /**
     * Converts a wrapping expression to a wrapping type, thereby setting the leaf node to the supplied type.
     *
     * @param wrap The wrapping to convert, either a {@link CompParamWrapExpression} or a
     *     {@link CompInstWrapExpression}.
     * @param leafType A {@link CifType} to be set as the leaf node.
     * @return The new wrapping type, either a {@link CompParamWrapType} or a {@link CompInstWrapType}.
     */
    private CifType convertWrapExprToWrapType(Expression wrap, CifType leafType) {
        Assert.check(wrap instanceof CompParamWrapExpression || wrap instanceof CompInstWrapExpression);

        if (wrap instanceof CompParamWrapExpression) {
            CompParamWrapExpression paramWrap = (CompParamWrapExpression)wrap;

            CompParamWrapType newParamWrap = newCompParamWrapType();
            newParamWrap.setParameter(paramWrap.getParameter());

            Expression reference = paramWrap.getReference();
            if (reference instanceof CompParamWrapExpression || reference instanceof CompInstWrapExpression) {
                newParamWrap.setReference(convertWrapExprToWrapType(reference, leafType));
            } else {
                newParamWrap.setReference(leafType);
            }

            return newParamWrap;
        } else {
            CompInstWrapExpression instWrap = (CompInstWrapExpression)wrap;

            CompInstWrapType newInstWrap = newCompInstWrapType();
            newInstWrap.setInstantiation(instWrap.getInstantiation());

            Expression reference = instWrap.getReference();
            if (reference instanceof CompParamWrapExpression || reference instanceof CompInstWrapExpression) {
                newInstWrap.setReference(convertWrapExprToWrapType(reference, leafType));
            } else {
                newInstWrap.setReference(leafType);
            }

            return newInstWrap;
        }
    }

    /**
     * Sets the position information for the position object. For wrapping expressions, the position information is also
     * set for the reference.
     *
     * @param obj The object.
     * @param position The new position information.
     */
    private void setPositionInfo(PositionObject obj, Position position) {
        obj.setPosition(position);

        PositionObject ref = null;
        if (obj instanceof CompParamWrapExpression) {
            ref = ((CompParamWrapExpression)obj).getReference();
        } else if (obj instanceof CompInstWrapExpression) {
            ref = ((CompInstWrapExpression)obj).getReference();
        } else if (obj instanceof CompParamWrapType) {
            ref = ((CompParamWrapType)obj).getReference();
        } else if (obj instanceof CompInstWrapType) {
            ref = ((CompInstWrapType)obj).getReference();
        }

        if (ref != null) {
            setPositionInfo(ref, copyPosition(position));
        }
    }
}
