//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.datasynth;

import static org.eclipse.escet.cif.common.CifTextUtils.exprToStr;
import static org.eclipse.escet.cif.common.CifTextUtils.getAbsName;
import static org.eclipse.escet.cif.common.CifTextUtils.getLocationText2;
import static org.eclipse.escet.cif.common.CifTextUtils.getParentText2;
import static org.eclipse.escet.cif.common.CifTextUtils.invToStr;
import static org.eclipse.escet.cif.common.CifTextUtils.updateToStr;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.warn;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.escet.cif.common.CifEquationUtils;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.SupKind;
import org.eclipse.escet.cif.metamodel.cif.automata.Assignment;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.ElifUpdate;
import org.eclipse.escet.cif.metamodel.cif.automata.IfUpdate;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.automata.Update;
import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Constant;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.VariableValue;
import org.eclipse.escet.cif.metamodel.cif.expressions.AlgVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BoolExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CastExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ComponentExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ConstantExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ContVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DictExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DictPair;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ElifExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EnumLiteralExpression;
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
import org.eclipse.escet.cif.metamodel.cif.expressions.TimeExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TupleExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression;
import org.eclipse.escet.common.java.Assert;

/** Checker for plant invariants or plant automata that reference requirement automata. */
public class CifDataSynthesisRequirementReferenceChecker {
    /** Constructor for the {@link CifDataSynthesisRequirementReferenceChecker} class. */
    private CifDataSynthesisRequirementReferenceChecker() {
        // Static class.
    }

    /**
     * Checks whether there are plant automata or plant invariants that reference a discrete variable, continuous
     * variable or location that is part of a requirement automata.
     *
     * @param spec The CIF specification to check.
     */
    public static void checkPlantRefToRequirement(Specification spec) {
        // Check locally.
        for (Invariant inv: spec.getInvariants()) {
            check(inv);
        }

        // Check children.
        for (Component comp: spec.getComponents()) {
            if (comp instanceof Group) {
                check((Group)comp);
            } else {
                Assert.check(comp instanceof Automaton);
                check((Automaton)comp);
            }
        }
    }

    /**
     * Checks whether there are plant automata or plant invariants that reference a discrete variable, continuous
     * variable or location that is part of a requirement automata.
     *
     * @param group The group to check.
     */
    private static void check(Group group) {
        // Check locally.
        for (Invariant inv: group.getInvariants()) {
            check(inv);
        }

        // Check children.
        for (Component comp: group.getComponents()) {
            if (comp instanceof Group) {
                check((Group)comp);
            } else {
                Assert.check(comp instanceof Automaton);
                check((Automaton)comp);
            }
        }
    }

    /**
     * Checks whether a plant automaton references a discrete variable, continuous variable or location that is part of
     * a requirement automata.
     *
     * @param aut The automaton to check.
     */
    private static void check(Automaton aut) {
        // If the automaton is a plant, check if it contains references to requirements.
        if (aut.getKind() == SupKind.PLANT) {
            // Check whether discrete variables have initial values that reference requirements.
            for (Declaration decl: aut.getDeclarations()) {
                if (!(decl instanceof DiscVariable)) {
                    continue;
                }

                VariableValue variableValue = ((DiscVariable)decl).getValue();
                if (variableValue == null) {
                    continue;
                }

                for (Expression value: variableValue.getValues()) {
                    if (referencesReq(value)) {
                        warn("Initial value of plant discrete variables \"%s\" references a requirement.",
                                getAbsName(decl));
                    }
                }
            }

            // Check whether locations have elements that reference requirements.
            for (Location loc: aut.getLocations()) {
                // Check initialization predicates.
                for (Expression initPred: loc.getInitials()) {
                    if (referencesReq(initPred)) {
                        warn("Plant initialization predicate \"%s\" in %s references a requirement.",
                                exprToStr(initPred), getParentText2(loc));
                    }
                }

                // Check marker predicates.
                for (Expression markPred: loc.getMarkeds()) {
                    if (referencesReq(markPred)) {
                        warn("Plant marked predicate \"%s\" in %s references a requirement.", exprToStr(markPred),
                                getLocationText2(loc));
                    }
                }

                // Check edges.
                for (Edge edge: loc.getEdges()) {
                    // Check edge guards.
                    for (Expression guard: edge.getGuards()) {
                        if (referencesReq(guard)) {
                            warn("Plant edge guard \"%s\" in %s references a requirement.", exprToStr(guard),
                                    getParentText2(loc));
                        }
                    }

                    // check edge updates.
                    for (Update update: edge.getUpdates()) {
                        if (referencesReq(update)) {
                            warn("Plant edge update \"%s\" in %s references a requirement.", updateToStr(update),
                                    getParentText2(loc));
                        }
                    }
                }
            }
        }

        // If the automaton is a requirement, check if it has plant invariants in locations, as these depend on the
        // location of the requirement.
        if (aut.getKind() == SupKind.REQUIREMENT) {
            for (Location loc: aut.getLocations()) {
                for (Invariant inv: loc.getInvariants()) {
                    if (inv.getSupKind() == SupKind.PLANT) {
                        warn("plant invariant \"%s\" in %s.", invToStr(inv, false), getParentText2(loc));
                    }
                }
            }
        }

        // Check invariants in locations.
        for (Location loc: aut.getLocations()) {
            for (Invariant inv: loc.getInvariants()) {
                check(inv);
            }
        }
    }

    /**
     * Checks whether a plant invariant references a discrete variable, continuous variable or location that is part of
     * a requirement automata.
     *
     * @param inv The plant invariant to check.
     */
    private static void check(Invariant inv) {
        // Skip non plant invariants.
        if (inv.getSupKind() != SupKind.PLANT) {
            return;
        }

        // If the invariant references a requirement, show a warning.
        Expression pred = inv.getPredicate();
        if (referencesReq(pred)) {
            warn("Plant invariant \"%s\" in %s references a requirement.", exprToStr(pred),
                    getParentText2(inv.eContainer()));
        }
    }

    /**
     * Checks whether an update references a discrete variable, continuous variable or location that is part of a
     * requirement automata.
     *
     * @param update The update to check.
     * @return {@code true} if the update reference a discrete variable, continuous variable or location that is part of
     *     a requirement automata, {@code false} otherwise.
     */
    private static boolean referencesReq(Update update) {
        if (update instanceof Assignment) {
            Expression value = ((Assignment)update).getValue();
            return referencesReq(value);
        } else if (update instanceof IfUpdate) {
            IfUpdate ifUpdate = (IfUpdate)update;

            // Check if guard.
            for (Expression guard: ifUpdate.getGuards()) {
                if (referencesReq(guard)) {
                    return true;
                }
            }

            // Check thens.
            for (Update then: ifUpdate.getThens()) {
                if (referencesReq(then)) {
                    return true;
                }
            }

            // Check elif guards and thens.
            for (ElifUpdate elif: ifUpdate.getElifs()) {
                for (Expression guard: elif.getGuards()) {
                    if (referencesReq(guard)) {
                        return true;
                    }
                }
                for (Update then: elif.getThens()) {
                    if (referencesReq(then)) {
                        return true;
                    }
                }
            }

            // Check else's.
            for (Update elseUpdate: ifUpdate.getElses()) {
                if (referencesReq(elseUpdate)) {
                    return true;
                }
            }

            // No requirement reference found.
            return false;
        } else {
            throw new RuntimeException("Unexpected update: " + update);
        }
    }

    /**
     * Checks whether an expression references a discrete variable, continuous variable or location that is part of a
     * requirement automata.
     *
     * @param expr The expression to check.
     * @return {@code true} if the expression references a discrete variable, continuous variable or location that is
     *     part of a requirement automata, {@code false} otherwise.
     */
    private static boolean referencesReq(Expression expr) {
        if (expr instanceof BoolExpression) {
            return false;
        }
        if (expr instanceof IntExpression) {
            return false;
        }
        if (expr instanceof RealExpression) {
            return false;
        }
        if (expr instanceof StringExpression) {
            return false;
        }
        if (expr instanceof TimeExpression) {
            return false;
        }
        if (expr instanceof CastExpression) {
            Expression child = ((CastExpression)expr).getChild();
            return referencesReq(child);
        }
        if (expr instanceof UnaryExpression) {
            Expression child = ((UnaryExpression)expr).getChild();
            return referencesReq(child);
        }
        if (expr instanceof BinaryExpression) {
            BinaryExpression bexpr = (BinaryExpression)expr;
            Expression left = bexpr.getLeft();
            Expression right = bexpr.getRight();
            return referencesReq(left) || referencesReq(right);
        }
        if (expr instanceof IfExpression) {
            IfExpression iexpr = (IfExpression)expr;

            // Check if guard.
            for (Expression guard: iexpr.getGuards()) {
                if (referencesReq(guard)) {
                    return true;
                }
            }

            // Check then.
            if (referencesReq(iexpr.getThen())) {
                return true;
            }

            // Check elif guards and thens.
            for (ElifExpression elif: iexpr.getElifs()) {
                for (Expression guard: elif.getGuards()) {
                    if (referencesReq(guard)) {
                        return true;
                    }
                }
                if (referencesReq(elif.getThen())) {
                    return true;
                }
            }

            // Check else.
            if (referencesReq(iexpr.getElse())) {
                return true;
            }

            // No requirement reference found.
            return false;
        }
        if (expr instanceof SwitchExpression) {
            SwitchExpression sexpr = (SwitchExpression)expr;

            // Check the value.
            if (referencesReq(sexpr.getValue())) {
                return true;
            }

            // Check case value and keys.
            for (SwitchCase cse: sexpr.getCases()) {
                if (cse.getKey() != null) {
                    if (referencesReq(cse.getKey())) {
                        return true;
                    }
                }
                if (referencesReq(cse.getValue())) {
                    return true;
                }
            }

            // No requirement reference found.
            return false;
        }
        if (expr instanceof ProjectionExpression) {
            ProjectionExpression pexpr = (ProjectionExpression)expr;
            Expression child = pexpr.getChild();
            Expression index = pexpr.getIndex();
            return referencesReq(child) || referencesReq(index);
        }
        if (expr instanceof SliceExpression) {
            SliceExpression sexpr = (SliceExpression)expr;
            if (referencesReq(sexpr.getChild())) {
                return true;
            }
            if (sexpr.getBegin() != null) {
                if (referencesReq(sexpr.getBegin())) {
                    return true;
                }
            }
            if (sexpr.getEnd() != null) {
                if (referencesReq(sexpr.getEnd())) {
                    return true;
                }
            }

            // No requirement reference found.
            return false;
        }
        if (expr instanceof FunctionCallExpression) {
            // Check the parameters.
            for (Expression param: ((FunctionCallExpression)expr).getParams()) {
                if (referencesReq(param)) {
                    return true;
                }
            }

            // No requirement reference found.
            return false;
        }
        if (expr instanceof ListExpression) {
            ListExpression lexpr = (ListExpression)expr;
            for (Expression elem: lexpr.getElements()) {
                if (referencesReq(elem)) {
                    return true;
                }
            }

            // No requirement reference found.
            return false;
        }
        if (expr instanceof SetExpression) {
            SetExpression sexpr = (SetExpression)expr;
            for (Expression elem: sexpr.getElements()) {
                if (referencesReq(elem)) {
                    return true;
                }
            }

            // No requirement reference found.
            return false;
        }
        if (expr instanceof TupleExpression) {
            TupleExpression texpr = (TupleExpression)expr;
            for (Expression field: texpr.getFields()) {
                if (referencesReq(field)) {
                    return true;
                }
            }

            // No requirement reference found.
            return false;
        }
        if (expr instanceof DictExpression) {
            DictExpression dexpr = (DictExpression)expr;
            for (DictPair pair: dexpr.getPairs()) {
                if (referencesReq(pair.getKey()) || referencesReq(pair.getValue())) {
                    return true;
                }
            }

            // No requirement reference found.
            return false;
        }
        if (expr instanceof ConstantExpression) {
            Constant constant = ((ConstantExpression)expr).getConstant();
            return referencesReq(constant.getValue());
        }
        if (expr instanceof EnumLiteralExpression) {
            return false;
        }
        if (expr instanceof FieldExpression) {
            return false;
        }
        if (expr instanceof StdLibFunctionExpression) {
            return false;
        }
        if (expr instanceof FunctionExpression) {
            return false;
        }
        if (expr instanceof DiscVariableExpression) {
            DiscVariable var = ((DiscVariableExpression)expr).getVariable();

            // If the parent is a requirement automaton, it is a reference to a requirement.
            EObject parent = var.eContainer();
            if (parent instanceof Automaton && ((Automaton)parent).getKind() == SupKind.REQUIREMENT) {
                return true;
            }

            // Check all possible initial values.
            VariableValue varValue = var.getValue();
            if (varValue == null) {
                return false;
            }

            for (Expression value: varValue.getValues()) {
                if (referencesReq(value)) {
                    return true;
                }
            }

            // No requirement reference found.
            return false;
        }

        if (expr instanceof AlgVariableExpression) {
            AlgVariable var = ((AlgVariableExpression)expr).getVariable();

            // Check the value(s).
            Expression value = CifEquationUtils.getSingleValueForAlgVar(var);
            return referencesReq(value);
        }

        if (expr instanceof ContVariableExpression) {
            ContVariable var = ((ContVariableExpression)expr).getVariable();

            // If the parent is a requirement automaton, it is a reference to a requirement.
            EObject parent = var.eContainer();
            if (parent instanceof Automaton && ((Automaton)parent).getKind() == SupKind.REQUIREMENT) {
                return true;
            }

            // Check the value and the derivative(s).
            Expression value = var.getValue();
            Expression derivative = CifEquationUtils.getSingleDerivativeForContVar(var);

            return referencesReq(value) || referencesReq(derivative);
        }

        if (expr instanceof InputVariableExpression) {
            return false;
        }

        if (expr instanceof LocationExpression) {
            Location loc = ((LocationExpression)expr).getLocation();

            // Check whether the parent automaton is a requirement.
            Automaton aut = (Automaton)loc.eContainer();
            return aut.getKind() == SupKind.REQUIREMENT;
        }

        if (expr instanceof ComponentExpression) {
            Component component = ((ComponentExpression)expr).getComponent();

            if (component instanceof Group) {
                return false;
            } else {
                Assert.check(component instanceof Automaton);
                Automaton aut = (Automaton)component;
                return aut.getKind() == SupKind.REQUIREMENT;
            }
        }

        if (expr instanceof SelfExpression) {
            return false;
        }

        if (expr instanceof ReceivedExpression) {
            return false;
        }

        // Not allowed in the initial values of state variables:
        // - TauExpression (not allowed as value)
        // - EventExpression (not allowed as value)

        // Should have been eliminated:
        // - CompParamExpression
        // - CompInstWrapExpression
        // - CompParamWrapExpression

        throw new RuntimeException("Unexpected expr: " + expr);
    }
}
