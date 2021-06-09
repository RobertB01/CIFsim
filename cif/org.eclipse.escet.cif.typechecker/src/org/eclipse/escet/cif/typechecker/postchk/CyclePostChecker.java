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

package org.eclipse.escet.cif.typechecker.postchk;

import static org.eclipse.escet.cif.common.CifEquationUtils.getDerivativesForContVar;
import static org.eclipse.escet.cif.common.CifEquationUtils.getValuesForAlgVar;
import static org.eclipse.escet.cif.common.CifTextUtils.getAbsName;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.set;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.escet.cif.common.CifEquationUtils;
import org.eclipse.escet.cif.common.CifLocationUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Constant;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.TypeDecl;
import org.eclipse.escet.cif.metamodel.cif.expressions.AlgVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BoolExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CastExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CompInstWrapExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CompParamWrapExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ComponentExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ConstantExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ContVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DictExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DictPair;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ElifExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EnumLiteralExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.FieldExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.FunctionCallExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.FunctionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IfExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.InputVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IntExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ListExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.LocationExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ProjectionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.RealExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ReceivedExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SelfExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SetExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SliceExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunctionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.StringExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SwitchCase;
import org.eclipse.escet.cif.metamodel.cif.expressions.SwitchExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TauExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TimeExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TupleExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression;
import org.eclipse.escet.cif.metamodel.cif.functions.ExternalFunction;
import org.eclipse.escet.cif.metamodel.cif.functions.Function;
import org.eclipse.escet.cif.metamodel.cif.functions.InternalFunction;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.ComponentType;
import org.eclipse.escet.cif.typechecker.CifTypeChecker;
import org.eclipse.escet.cif.typechecker.ErrMsg;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;
import org.eclipse.escet.common.typechecker.SemanticException;

/**
 * Detects definition/use cycles. Assumes that constants and type declarations have already been checked before. Also
 * assumes that component definitions/instantiations are not present in the specification.
 */
public class CyclePostChecker {
    /** The post check environment to use. */
    private final CifPostCheckEnv env;

    /** The objects already checked so far. */
    private Set<PositionObject> done = set();

    /** Mapping from continuous variables to unique derivative objects. */
    private Map<ContVariable, Derivative> contDerMap = map();

    /**
     * Constructor for the {@link CyclePostChecker} class.
     *
     * @param env The post check environment to use.
     */
    private CyclePostChecker(CifPostCheckEnv env) {
        this.env = env;
    }

    /**
     * Check a specification for definition/use cycles.
     *
     * @param spec The specification to check.
     * @param env The post check environment to use.
     * @throws SemanticException In case a problem is found.
     * @see CyclePostChecker
     */
    public static void check(Specification spec, CifPostCheckEnv env) {
        CyclePostChecker checker = new CyclePostChecker(env);
        checker.check(spec);
    }

    /**
     * Returns the unique derivative object for the given continuous variable.
     *
     * @param var The continuous variable
     * @return The unique derivative object.
     */
    private Derivative getDerivative(ContVariable var) {
        Derivative rslt = contDerMap.get(var);
        if (rslt == null) {
            rslt = new Derivative(var);
            contDerMap.put(var, rslt);
        }
        return rslt;
    }

    /**
     * Returns the absolute name of the given element of a cycle.
     *
     * @param elem The element of the cycle.
     * @return The absolute name of the given element.
     */
    private String getCycleElementName(PositionObject elem) {
        // Special case for derivatives.
        if (elem instanceof Derivative) {
            return getCycleElementName(((Derivative)elem).var) + "'";
        }

        // Special case for nameless locations.
        if (elem instanceof Location) {
            Location loc = (Location)elem;
            if (loc.getName() == null) {
                Automaton aut = CifLocationUtils.getAutomaton(loc);
                return getAbsName(aut) + ".<loc>";
            }
        }

        // General case.
        return getAbsName(elem);
    }

    /**
     * Adds an object to the cycle detection list.
     *
     * <p>
     * If a cycle is detected, problems are added to the type checker, for all elements of the cycle. All elements of
     * the cycle are also added to {@link #done}, never to be checked again.
     * </p>
     *
     * @param obj The object to add.
     * @param cycle The cycle so far. Is modified in-place.
     * @throws SemanticException If a cycle is found.
     * @see CifTypeChecker#addToCycle
     */
    private void addToCycle(PositionObject obj, List<PositionObject> cycle) {
        // If not yet present, just add it.
        if (!cycle.contains(obj)) {
            cycle.add(obj);
            return;
        }

        // Already present: cycle detected. Find ourselves in the cycle.
        int idx = cycle.indexOf(obj);
        Assert.check(idx >= 0);

        // Add error for each element in the found cycle.
        for (int i = idx; i < cycle.size(); i++) {
            // Get the element of the cycle for which to add the error.
            PositionObject startDecl = cycle.get(i);

            // Get the names of the elements of the cycle.
            StringBuilder txt = new StringBuilder();
            for (int j = i; j < cycle.size(); j++) {
                if (txt.length() > 0) {
                    txt.append(" -> ");
                }
                txt.append(getCycleElementName(cycle.get(j)));
            }
            for (int j = idx; j <= i; j++) {
                if (txt.length() > 0) {
                    txt.append(" -> ");
                }
                txt.append(getCycleElementName(cycle.get(j)));
            }

            // Add the problem for this element, and mark the element as done.
            env.addProblem(ErrMsg.DEF_USE_CYCLE, startDecl.getPosition(), getCycleElementName(startDecl),
                    txt.toString());
            done.add(startDecl);
        }

        // Cycle found.
        throw new SemanticException();
    }

    /**
     * Removes an object from the cycle detection list.
     *
     * @param obj The object to remove.
     * @param cycle The cycle so far. Is modified in-place.
     * @throws IllegalStateException If the object to remove is not the top of the cycle detection list.
     * @see CifTypeChecker#removeFromCycle
     */
    private void removeFromCycle(PositionObject obj, List<PositionObject> cycle) {
        PositionObject top = cycle.remove(cycle.size() - 1);
        if (top != obj) {
            throw new IllegalStateException("top of cycle != obj");
        }
    }

    /**
     * Check a complex component for cycles.
     *
     * @param comp The complex component to check.
     */
    private void check(ComplexComponent comp) {
        // Check declarations.
        for (Declaration decl: comp.getDeclarations()) {
            check(decl);
        }

        // Check automaton.
        if (comp instanceof Automaton) {
            // Start checking the declaration with an empty cycle.
            List<PositionObject> cycle = list();

            // Check automaton for cycle.
            try {
                check((Automaton)comp, cycle);

                // Make sure we end with an empty cycle (proper stack
                // push/pop).
                Assert.check(cycle.isEmpty());
            } catch (SemanticException ex) {
                // Cycle found. Continue looking for other cycles.
            }
        }

        // Check child components of groups.
        if (comp instanceof Group) {
            Group group = (Group)comp;

            // Check child components.
            for (Component child: group.getComponents()) {
                Assert.check(child instanceof ComplexComponent);
                check((ComplexComponent)child);
            }
        }
    }

    /**
     * Check an automaton for cycles. Performs only the additional checking not already performed by component checking.
     * That is, it checks the locations.
     *
     * @param aut The automaton to check.
     * @param cycle The cycle so far. Is modified in-place.
     */
    private void check(Automaton aut, List<PositionObject> cycle) {
        // If already checked, don't check again.
        if (done.contains(aut)) {
            return;
        }

        // Add to cycle.
        addToCycle(aut, cycle);

        // Check all locations. We check all locations. The location check has
        // some optimizations to save work.
        for (Location loc: aut.getLocations()) {
            if (loc.getInitials().isEmpty()) {
                continue;
            }
            check(loc, cycle);
        }

        // Remove object from cycle detection and mark the object as done.
        removeFromCycle(aut, cycle);
        done.add(aut);
    }

    /**
     * Check a location for cycles in its initialization predicates.
     *
     * @param loc The location to check.
     * @param cycle The cycle so far. Is modified in-place.
     */
    private void check(Location loc, List<PositionObject> cycle) {
        // We need to check the initialization predicates. However, locations
        // without initialization predicates can be skipped here, as they
        // don't have cycles in their initialization predicates. This is a
        // performance optimization, especially useful for automata with a
        // large number of locations, if only a few of them have initialization
        // predicates.
        List<Expression> initials = loc.getInitials();
        if (initials.isEmpty()) {
            return;
        }

        // We consider this location a potential initial location. We can't
        // further optimize this. Checking for statically false predicates and
        // thus non-initial locations is not an option, as we may still have
        // cycles. Similarly, we can't evaluate to determine that. Thus, all
        // locations with initialization predicates are potential initial
        // locations for now.

        // We also check nameless initial locations. They can't be referred to
        // directly. However, if they have initialization predicates that
        // directly or indirectly use automaton (self) references, they can
        // indirectly cause cycles.

        // If already checked, don't check again.
        if (done.contains(loc)) {
            return;
        }

        // Add to cycle.
        addToCycle(loc, cycle);

        // Check the initialization predicates.
        for (Expression initial: initials) {
            check(initial, cycle);
        }

        // Remove object from cycle detection and mark the object as done.
        removeFromCycle(loc, cycle);
        done.add(loc);
    }

    /**
     * Check a function for cycles.
     *
     * @param func The function to check.
     */
    private void check(Function func) {
        // Skip external user-defined functions.
        if (func instanceof ExternalFunction) {
            return;
        }

        // Get internal function.
        InternalFunction ifunc = (InternalFunction)func;

        // Check local variables.
        for (Declaration decl: ifunc.getVariables()) {
            check(decl);
        }
    }

    /**
     * Check a declaration for cycles.
     *
     * @param decl The declaration.
     */
    private void check(Declaration decl) {
        // Special case for functions, as they are scopes rather than actual
        // declarations.
        if (decl instanceof Function) {
            check((Function)decl);
            return;
        }

        // Start checking the declaration with an empty cycle.
        List<PositionObject> cycle = list();

        // Check based on type of declaration.
        try {
            if (decl instanceof AlgVariable) {
                check((AlgVariable)decl, cycle);
            } else if (decl instanceof Constant) {
                // Should already be free of cycles.
            } else if (decl instanceof Event) {
                // Should already be free of cycles.
            } else if (decl instanceof ContVariable) {
                ContVariable var = (ContVariable)decl;
                check(var, cycle);
                check(getDerivative(var), cycle);
            } else if (decl instanceof EnumDecl) {
                // Should already be free of cycles.
            } else if (decl instanceof Function) {
                throw new RuntimeException("Special case above.");
            } else if (decl instanceof TypeDecl) {
                // Should already be free of cycles.
            } else if (decl instanceof InputVariable) {
                // Should already be free of cycles.
            } else if (decl instanceof DiscVariable) {
                check((DiscVariable)decl, cycle);
            } else {
                throw new RuntimeException("Unknown decl: " + decl);
            }

            // Make sure we end with an empty cycle (proper stack push/pop).
            Assert.check(cycle.isEmpty());
        } catch (SemanticException ex) {
            // Cycle found. Continue looking for other cycles.
        }
    }

    /**
     * Check a discrete variable for cycles.
     *
     * @param var The discrete variable.
     * @param cycle The cycle so far. Is modified in-place.
     */
    private void check(DiscVariable var, List<PositionObject> cycle) {
        // If already checked, don't check again.
        if (done.contains(var)) {
            return;
        }

        // Add to cycle.
        addToCycle(var, cycle);

        // Check the initial values.
        if (var.getValue() != null) {
            for (Expression value: var.getValue().getValues()) {
                check(value, cycle);
            }
        }

        // Remove object from cycle detection and mark the object as done.
        removeFromCycle(var, cycle);
        done.add(var);
    }

    /**
     * Check a continuous variable for cycles. Does not check its derivative.
     *
     * @param var The continuous variable.
     * @param cycle The cycle so far. Is modified in-place.
     */
    private void check(ContVariable var, List<PositionObject> cycle) {
        // If already checked, don't check again.
        if (done.contains(var)) {
            return;
        }

        // Add to cycle.
        addToCycle(var, cycle);

        // Check the initial value.
        if (var.getValue() != null) {
            check(var.getValue(), cycle);
        }

        // Remove object from cycle detection and mark the object as done.
        removeFromCycle(var, cycle);
        done.add(var);
    }

    /**
     * Check a derivative of a continuous variable for cycles. Does not check the continuous variable.
     *
     * @param der The derivative.
     * @param cycle The cycle so far. Is modified in-place.
     */
    private void check(Derivative der, List<PositionObject> cycle) {
        // If already checked, don't check again.
        if (done.contains(der)) {
            return;
        }

        // Add to cycle.
        addToCycle(der, cycle);

        // Check derivative value/equations.
        List<Expression> values = getDerivativesForContVar(der.var, true);
        for (Expression value: values) {
            check(value, cycle);
        }

        // Remove object from cycle detection and mark the object as done.
        removeFromCycle(der, cycle);
        done.add(der);
    }

    /**
     * Check an algebraic variable for cycles.
     *
     * @param var The algebraic variable.
     * @param cycle The cycle so far. Is modified in-place.
     */
    private void check(AlgVariable var, List<PositionObject> cycle) {
        // If already checked, don't check again.
        if (done.contains(var)) {
            return;
        }

        // Add to cycle.
        addToCycle(var, cycle);

        // Check value/equations.
        List<Expression> values = getValuesForAlgVar(var, true);
        for (Expression value: values) {
            check(value, cycle);
        }

        // If there are equations in locations, check for cycles via initialization predicates.
        if (CifEquationUtils.hasLocationEquations(var)) {
            Automaton aut = (Automaton)var.eContainer();

            for (Location loc: aut.getLocations()) {
                check(loc, cycle);
            }
        }

        // Remove object from cycle detection and mark the object as done.
        removeFromCycle(var, cycle);
        done.add(var);
    }

    /**
     * Check an expression for cycles.
     *
     * @param expr The expression to check.
     * @param cycle The cycle so far. Is modified in-place.
     */
    public void check(Expression expr, List<PositionObject> cycle) {
        if (expr instanceof BoolExpression) {
            // Literal has no references, and thus no cycles.
        } else if (expr instanceof IntExpression) {
            // Literal has no references, and thus no cycles.
        } else if (expr instanceof RealExpression) {
            // Literal has no references, and thus no cycles.
        } else if (expr instanceof StringExpression) {
            // Literal has no references, and thus no cycles.
        } else if (expr instanceof TimeExpression) {
            // Time is an 'implicit' variable. It has no dependencies, and thus
            // no cycles.
        } else if (expr instanceof CastExpression) {
            // Special case for automaton to string casts.
            Expression child = ((CastExpression)expr).getChild();
            if (CifTypeUtils.isAutRefExpr(child)) {
                CifType ctype = CifTypeUtils.normalizeType(child.getType());
                Assert.check(ctype instanceof ComponentType);
                Component comp = ((ComponentType)ctype).getComponent();
                Automaton aut = (Automaton)comp;
                check(aut, cycle);
                return;
            }

            // Normal case: check recursively.
            check(child, cycle);
        } else if (expr instanceof UnaryExpression) {
            // Check recursively.
            check(((UnaryExpression)expr).getChild(), cycle);
        } else if (expr instanceof BinaryExpression) {
            // Check recursively.
            BinaryExpression bexpr = (BinaryExpression)expr;
            check(bexpr.getLeft(), cycle);
            check(bexpr.getRight(), cycle);
        } else if (expr instanceof IfExpression) {
            // Check recursively.
            IfExpression ifExpr = (IfExpression)expr;
            for (Expression guard: ifExpr.getGuards()) {
                check(guard, cycle);
            }
            check(ifExpr.getThen(), cycle);
            for (ElifExpression elif: ifExpr.getElifs()) {
                for (Expression guard: elif.getGuards()) {
                    check(guard, cycle);
                }
                check(elif.getThen(), cycle);
            }
            check(ifExpr.getElse(), cycle);
        } else if (expr instanceof SwitchExpression) {
            // Check recursively.
            SwitchExpression switchExpr = (SwitchExpression)expr;

            Expression value = switchExpr.getValue();
            if (CifTypeUtils.isAutRefExpr(value)) {
                CifType ctype = CifTypeUtils.normalizeType(value.getType());
                Assert.check(ctype instanceof ComponentType);
                Component comp = ((ComponentType)ctype).getComponent();
                Automaton aut = (Automaton)comp;
                check(aut, cycle);
            } else {
                check(switchExpr.getValue(), cycle);
            }

            for (SwitchCase cse: switchExpr.getCases()) {
                if (cse.getKey() != null) {
                    check(cse.getKey(), cycle);
                }
                check(cse.getValue(), cycle);
            }
        } else if (expr instanceof ProjectionExpression) {
            // Check recursively.
            ProjectionExpression pexpr = (ProjectionExpression)expr;
            check(pexpr.getChild(), cycle);
            check(pexpr.getIndex(), cycle);
        } else if (expr instanceof SliceExpression) {
            // Check recursively.
            SliceExpression sexpr = (SliceExpression)expr;
            check(sexpr.getChild(), cycle);
            if (sexpr.getBegin() != null) {
                check(sexpr.getBegin(), cycle);
            }
            if (sexpr.getEnd() != null) {
                check(sexpr.getEnd(), cycle);
            }
        } else if (expr instanceof FunctionCallExpression) {
            // Check recursively.
            FunctionCallExpression fcexpr = (FunctionCallExpression)expr;
            check(fcexpr.getFunction(), cycle);
            for (Expression param: fcexpr.getParams()) {
                check(param, cycle);
            }
        } else if (expr instanceof ListExpression) {
            // Check recursively.
            ListExpression lexpr = (ListExpression)expr;
            for (Expression elem: lexpr.getElements()) {
                check(elem, cycle);
            }
        } else if (expr instanceof SetExpression) {
            // Check recursively.
            SetExpression sexpr = (SetExpression)expr;
            for (Expression elem: sexpr.getElements()) {
                check(elem, cycle);
            }
        } else if (expr instanceof TupleExpression) {
            // Check recursively.
            TupleExpression texpr = (TupleExpression)expr;
            for (Expression elem: texpr.getFields()) {
                check(elem, cycle);
            }
        } else if (expr instanceof DictExpression) {
            // Check recursively.
            DictExpression dexpr = (DictExpression)expr;
            for (DictPair pair: dexpr.getPairs()) {
                check(pair.getKey(), cycle);
                check(pair.getValue(), cycle);
            }
        } else if (expr instanceof ConstantExpression) {
            // Should already be free of cycles.
        } else if (expr instanceof DiscVariableExpression) {
            // Continue checking the discrete variable. Check discrete
            // variables and local variables of internal functions. Skip
            // function parameters.
            DiscVariable var = ((DiscVariableExpression)expr).getVariable();
            EObject parent = var.eContainer();
            if (parent instanceof ComplexComponent || parent instanceof InternalFunction) {
                check(var, cycle);
            }
        } else if (expr instanceof AlgVariableExpression) {
            // Continue checking the algebraic variable.
            AlgVariable var = ((AlgVariableExpression)expr).getVariable();
            check(var, cycle);
        } else if (expr instanceof ContVariableExpression) {
            // Continue checking the continuous variable or derivative.
            ContVariableExpression ref = (ContVariableExpression)expr;
            ContVariable var = ref.getVariable();
            if (ref.isDerivative()) {
                check(getDerivative(var), cycle);
            } else {
                check(var, cycle);
            }
        } else if (expr instanceof TauExpression) {
            // We should never encounter 'tau' expressions in a value context.
            throw new RuntimeException("Tau expression in value context.");
        } else if (expr instanceof LocationExpression) {
            // Continue checking the location.
            Location loc = ((LocationExpression)expr).getLocation();
            check(loc, cycle);
        } else if (expr instanceof EnumLiteralExpression) {
            // Enumeration literals have no dependencies or cycles.
        } else if (expr instanceof EventExpression) {
            // Events have no dependencies or cycles. Note that the type should
            // already be cycle free. Also, we should never encounter an event
            // in a value context.
        } else if (expr instanceof FieldExpression) {
            // Fields have no dependencies or cycles. Note that the type should
            // already be cycle free.
        } else if (expr instanceof StdLibFunctionExpression) {
            // Standard library functions have no dependencies or cycles.
        } else if (expr instanceof FunctionExpression) {
            // No need to check the function here, as functions can be called
            // recursively. Furthermore, functions can't refer to algebraic
            // variables, etc. For recursively calls, there is no way to refer
            // to the local variables or parameters of other invocations.
        } else if (expr instanceof InputVariableExpression) {
            // Input variables have no dependencies or cycles. Note that the
            // type should already be cycle free.
        } else if (expr instanceof ComponentExpression) {
            // Component definition has been eliminated, so we shouldn't use
            // components as values. If allowed, it should be handled by the
            // parent expression as special case.
            throw new RuntimeException("Component ref in value context.");
        } else if (expr instanceof CompInstWrapExpression) {
            // Component definition has been eliminated.
            throw new RuntimeException("Wrap expr unexpected.");
        } else if (expr instanceof CompParamWrapExpression) {
            // Component definition has been eliminated.
            throw new RuntimeException("Wrap expr unexpected.");
        } else if (expr instanceof ReceivedExpression) {
            // Received value reference expressions have no dependencies or
            // cycles.
        } else if (expr instanceof SelfExpression) {
            // Automaton 'self' reference expressions shouldn't be used as
            // values. If allowed, it should be handled by the parent
            // expression as special case.
            throw new RuntimeException("Aut self ref in value context.");
        } else {
            throw new RuntimeException("Unknown expr: " + expr);
        }
    }

    /**
     * A unique derivative object for a continuous variable. Used to distinguish a continuous variable from its
     * derivative in the cycle detection.
     *
     * @see #getDerivative
     */
    private static class Derivative extends EObjectImpl implements PositionObject {
        /** The continuous variable of which this is the derivative. */
        public final ContVariable var;

        /**
         * Constructor for the {@link Derivative} class.
         *
         * @param var The continuous variable of which to construct the derivative.
         */
        public Derivative(ContVariable var) {
            this.var = var;
        }

        @Override
        public Position getPosition() {
            return var.getPosition();
        }

        @Override
        public void setPosition(Position value) {
            // Can't change position, as it is derived from the continuous
            // variable.
            throw new UnsupportedOperationException();
        }
    }
}
