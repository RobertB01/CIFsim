//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.explorer;

import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Sets.sortedstrings;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.escet.cif.common.CifMath;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.CifValueUtils;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.cif.metamodel.cif.expressions.ContVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunctionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TauExpression;
import org.eclipse.escet.cif.metamodel.cif.functions.ExternalFunction;
import org.eclipse.escet.cif.metamodel.cif.types.DistType;
import org.eclipse.escet.cif.metamodel.java.CifWalker;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.exceptions.UnsupportedException;

/** Checker class to verify that a given specification satisfies the pre-conditions required for exploration. */
public class ExplorerPreChecker extends CifWalker {
    /** Found problems in the specification. */
    private Set<String> problems = null;

    /** Set of configurable properties to check. */
    private final EnumSet<CheckParameters> params;

    /**
     * Constructor of the {@link ExplorerPreChecker} class.
     *
     * @param params Set of configurable parameters for the explorer pre-checker.
     */
    public ExplorerPreChecker(EnumSet<CheckParameters> params) {
        this.params = params;
    }

    /**
     * Check the specification for the requirements.
     *
     * @param spec Specification to check.
     */
    public void checkSpec(Specification spec) {
        problems = set();
        walkSpecification(spec);

        if (problems.isEmpty()) {
            return;
        }

        // If we have any problems, the specification is unsupported.
        String msg = "State space exploration failed due to unsatisfied preconditions:\n - "
                + String.join("\n - ", sortedstrings(problems));
        throw new UnsupportedException(msg);
    }

    /** Parameters of the explorer check process. */
    public static enum CheckParameters {
        /** Allow 'none' invariants. */
        ALLOW_NON_INVS,

        /** Allow 'supervisor' invariants. */
        ALLOW_SUP_INVS,

        /** Allow 'requirement' invariants. */
        ALLOW_REQ_INVS,

        /** Allow 'none' automaton. */
        ALLOW_NON_AUT,

        /** Allow 'supervisor' automaton. */
        ALLOW_SUP_AUT,

        /** Allow 'requirement' automaton. */
        ALLOW_REQ_AUT,

        /** Allow 'tau' events. */
        ALLOW_TAU,
    }

    @Override
    protected void preprocessDistType(DistType tp) {
        String msg = fmt("Distribution type \"%s\" is not supported.", CifTextUtils.typeToStr(tp));
        problems.add(msg);
    }

    @Override
    protected void preprocessAutomaton(Automaton aut) {
        String msg;

        switch (aut.getKind()) {
            case NONE:
                if (params.contains(CheckParameters.ALLOW_NON_AUT)) {
                    break;
                }
                msg = fmt("Regular automaton \"%s\" is not supported.", CifTextUtils.getAbsName(aut));
                problems.add(msg);
                break;

            case PLANT:
                break; // Always supported.

            case REQUIREMENT:
                if (params.contains(CheckParameters.ALLOW_REQ_AUT)) {
                    break;
                }
                msg = fmt("Requirement automaton \"%s\" is not supported.", CifTextUtils.getAbsName(aut));
                problems.add(msg);
                break;

            case SUPERVISOR:
                if (params.contains(CheckParameters.ALLOW_SUP_AUT)) {
                    break;
                }
                msg = fmt("Supervisor automaton \"%s\" is not supported.", CifTextUtils.getAbsName(aut));
                problems.add(msg);
                break;
        }
    }

    @Override
    protected void preprocessEdge(Edge edge) {
        if (params.contains(CheckParameters.ALLOW_TAU)) {
            return;
        }

        String msg;
        Location loc = (Location)edge.eContainer();
        if (edge.getEvents().isEmpty()) {
            msg = fmt("Tau events in edges of %s are not supported.", CifTextUtils.getLocationText2(loc));
            problems.add(msg);
            return;
        }
        for (EdgeEvent ee: edge.getEvents()) {
            if (ee.getEvent() instanceof TauExpression) {
                msg = fmt("Tau events in edges of %s are not supported.", CifTextUtils.getLocationText2(loc));
                problems.add(msg);
                return;
            }
        }
    }

    @Override
    protected void preprocessExternalFunction(ExternalFunction func) {
        String msg = fmt("External user-defined function \"%s\" is not supported.", CifTextUtils.getAbsName(func));
        problems.add(msg);
    }

    @Override
    protected void preprocessInputVariable(InputVariable var) {
        String msg = fmt("Input variable \"%s\" is not supported.", CifTextUtils.getAbsName(var));
        problems.add(msg);
    }

    @Override
    protected void preprocessDiscVariable(DiscVariable var) {
        // Single initial value is always OK.
        if (var.getValue() == null) {
            return;
        }
        List<Expression> initVals = var.getValue().getValues();
        if (initVals.size() == 1) {
            return;
        }

        // Finite number of potential initial values is always OK.
        if (!initVals.isEmpty()) {
            return;
        }

        // Any value in its domain. Check for finite type. That is, the number
        // of possible values must fit within the range of 'int', to ensure we
        // can store the potential values in a list.
        double cnt = CifValueUtils.getPossibleValueCount(var.getType());
        if (cnt <= Integer.MAX_VALUE) {
            return;
        }

        // Unsupported type for multiple initial values.
        String cntTxt = Double.isInfinite(cnt) ? "infinite" : CifMath.realToStr(cnt);
        String msg = fmt("Discrete variable \"%s\" of type \"%s\" with %s potential initial values is not supported.",
                CifTextUtils.getAbsName(var), CifTextUtils.typeToStr(var.getType()), cntTxt);
        problems.add(msg);
    }

    @Override
    protected void preprocessInvariant(Invariant inv) {
        EObject parent = inv.eContainer();
        String parentTxt;
        if (parent instanceof Location) {
            parentTxt = CifTextUtils.getLocationText2((Location)parent);
        } else {
            Assert.check(parent instanceof ComplexComponent);
            parentTxt = CifTextUtils.getComponentText2((ComplexComponent)parent);
        }

        String msg;
        switch (inv.getSupKind()) {
            case NONE:
                if (params.contains(CheckParameters.ALLOW_NON_INVS)) {
                    break;
                }
                msg = fmt("Regular invariants in %s are not supported.", parentTxt);
                problems.add(msg);
                break;

            case PLANT:
                break; // Always supported.

            case REQUIREMENT:
                if (params.contains(CheckParameters.ALLOW_REQ_INVS)) {
                    break;
                }
                msg = fmt("Requirement invariants in %s are not supported.", parentTxt);
                problems.add(msg);
                break;

            case SUPERVISOR:
                if (params.contains(CheckParameters.ALLOW_SUP_INVS)) {
                    break;
                }
                msg = fmt("Supervisor invariants in %s are not supported.", parentTxt);
                problems.add(msg);
                break;
        }
    }

    @Override
    protected void preprocessContVariableExpression(ContVariableExpression expr) {
        if (expr.isDerivative()) {
            String msg = "Use of derivatives of continuous variables is not supported.";
            problems.add(msg);
        }
    }

    @Override
    protected void preprocessStdLibFunctionExpression(StdLibFunctionExpression expr) {
        if (CifTypeUtils.isDistFunction(expr.getFunction())) {
            String msg = fmt("Distribution standard library function \"%s\" is not supported.",
                    CifTextUtils.functionToStr(expr.getFunction()));
            problems.add(msg);
        }
    }
}
