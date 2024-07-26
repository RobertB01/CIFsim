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

package org.eclipse.escet.cif.explorer;

import org.eclipse.escet.cif.checkers.CifPreconditionChecker;
import org.eclipse.escet.cif.checkers.checks.ExprNoSpecificExprsCheck;
import org.eclipse.escet.cif.checkers.checks.ExprNoSpecificExprsCheck.NoSpecificExpr;
import org.eclipse.escet.cif.checkers.checks.FuncNoSpecificStdLibCheck;
import org.eclipse.escet.cif.checkers.checks.FuncNoSpecificStdLibCheck.NoSpecificStdLib;
import org.eclipse.escet.cif.checkers.checks.FuncNoSpecificUserDefCheck;
import org.eclipse.escet.cif.checkers.checks.FuncNoSpecificUserDefCheck.NoSpecificUserDefFunc;
import org.eclipse.escet.cif.checkers.checks.TypeNoSpecificTypesCheck;
import org.eclipse.escet.cif.checkers.checks.TypeNoSpecificTypesCheck.NoSpecificType;
import org.eclipse.escet.common.java.Termination;

/** CIF explorer precondition checker. */
public class ExplorerPreChecker extends CifPreconditionChecker {
    /**
     * Constructor for the {@link ExplorerPreChecker} class.
     *
     * @param termination Cooperative termination query function.
     */
    public ExplorerPreChecker(Termination termination) {
        super(termination,

                // No distributions.
                new FuncNoSpecificStdLibCheck(NoSpecificStdLib.ALL_STOCHASTIC),
                new TypeNoSpecificTypesCheck(NoSpecificType.DIST_TYPES),

                // No derivatives.
                new ExprNoSpecificExprsCheck(NoSpecificExpr.CONT_VAR_DERIV_REFS),

                // No external user-defined functions.
                new FuncNoSpecificUserDefCheck(NoSpecificUserDefFunc.EXTERNAL)

        );
    }

//    /** Found problems in the specification. */
//    private Set<String> problems = null;
//
//    /** Set of configurable properties to check. */
//    private final EnumSet<CheckParameters> params;
//
//    /**
//     * Constructor of the {@link ExplorerPreChecker} class.
//     *
//     * @param params Set of configurable parameters for the explorer pre-checker.
//     */
//    public ExplorerPreChecker(EnumSet<CheckParameters> params) {
//        this.params = params;
//    }
//
//    /**
//     * Check the specification for the requirements.
//     *
//     * @param spec Specification to check.
//     */
//    public void checkSpec(Specification spec) {
//        problems = set();
//        walkSpecification(spec);
//
//        if (problems.isEmpty()) {
//            return;
//        }
//
//        // If we have any problems, the specification is unsupported.
//        String msg = "State space exploration failed due to unsatisfied preconditions:\n - "
//                + String.join("\n - ", sortedstrings(problems));
//        throw new UnsupportedException(msg);
//    }
//
//    /** Parameters of the explorer check process. */
//    public static enum CheckParameters {
//        /** Allow 'none' invariants. */
//        ALLOW_NON_INVS,
//
//        /** Allow 'supervisor' invariants. */
//        ALLOW_SUP_INVS,
//
//        /** Allow 'requirement' invariants. */
//        ALLOW_REQ_INVS,
//
//        /** Allow 'none' automaton. */
//        ALLOW_NON_AUT,
//
//        /** Allow 'supervisor' automaton. */
//        ALLOW_SUP_AUT,
//
//        /** Allow 'requirement' automaton. */
//        ALLOW_REQ_AUT,
//
//        /** Allow 'tau' events. */
//        ALLOW_TAU,
//    }
//
//    @Override
//    protected void preprocessAutomaton(Automaton aut) {
//        String msg;
//
//        switch (aut.getKind()) {
//            case NONE:
//                if (params.contains(CheckParameters.ALLOW_NON_AUT)) {
//                    break;
//                }
//                msg = fmt("Regular automaton \"%s\" is not supported.", CifTextUtils.getAbsName(aut));
//                problems.add(msg);
//                break;
//
//            case PLANT:
//                break; // Always supported.
//
//            case REQUIREMENT:
//                if (params.contains(CheckParameters.ALLOW_REQ_AUT)) {
//                    break;
//                }
//                msg = fmt("Requirement automaton \"%s\" is not supported.", CifTextUtils.getAbsName(aut));
//                problems.add(msg);
//                break;
//
//            case SUPERVISOR:
//                if (params.contains(CheckParameters.ALLOW_SUP_AUT)) {
//                    break;
//                }
//                msg = fmt("Supervisor automaton \"%s\" is not supported.", CifTextUtils.getAbsName(aut));
//                problems.add(msg);
//                break;
//        }
//    }
//
//    @Override
//    protected void preprocessEdge(Edge edge) {
//        if (params.contains(CheckParameters.ALLOW_TAU)) {
//            return;
//        }
//
//        String msg;
//        Location loc = (Location)edge.eContainer();
//        if (edge.getEvents().isEmpty()) {
//            msg = fmt("Tau events in edges of %s are not supported.", CifTextUtils.getLocationText2(loc));
//            problems.add(msg);
//            return;
//        }
//        for (EdgeEvent ee: edge.getEvents()) {
//            if (ee.getEvent() instanceof TauExpression) {
//                msg = fmt("Tau events in edges of %s are not supported.", CifTextUtils.getLocationText2(loc));
//                problems.add(msg);
//                return;
//            }
//        }
//    }
//
//    @Override
//    protected void preprocessInputVariable(InputVariable var) {
//        String msg = fmt("Input variable \"%s\" is not supported.", CifTextUtils.getAbsName(var));
//        problems.add(msg);
//    }
//
//    @Override
//    protected void preprocessDiscVariable(DiscVariable var) {
//        // Single initial value is always OK.
//        if (var.getValue() == null) {
//            return;
//        }
//        List<Expression> initVals = var.getValue().getValues();
//        if (initVals.size() == 1) {
//            return;
//        }
//
//        // Finite number of potential initial values is always OK.
//        if (!initVals.isEmpty()) {
//            return;
//        }
//
//        // Any value in its domain. Check for finite type. That is, the number
//        // of possible values must fit within the range of 'int', to ensure we
//        // can store the potential values in a list.
//        double cnt = CifValueUtils.getPossibleValueCount(var.getType());
//        if (cnt <= Integer.MAX_VALUE) {
//            return;
//        }
//
//        // Unsupported type for multiple initial values.
//        String cntTxt = Double.isInfinite(cnt) ? "infinite" : CifMath.realToStr(cnt);
//        String msg = fmt("Discrete variable \"%s\" of type \"%s\" with %s potential initial values is not supported.",
//                CifTextUtils.getAbsName(var), CifTextUtils.typeToStr(var.getType()), cntTxt);
//        problems.add(msg);
//    }
//
//    @Override
//    protected void preprocessInvariant(Invariant inv) {
//        EObject parent = inv.eContainer();
//        String parentTxt;
//        if (parent instanceof Location) {
//            parentTxt = CifTextUtils.getLocationText2((Location)parent);
//        } else {
//            Assert.check(parent instanceof ComplexComponent);
//            parentTxt = CifTextUtils.getComponentText2((ComplexComponent)parent);
//        }
//
//        String msg;
//        switch (inv.getSupKind()) {
//            case NONE:
//                if (params.contains(CheckParameters.ALLOW_NON_INVS)) {
//                    break;
//                }
//                msg = fmt("Regular invariants in %s are not supported.", parentTxt);
//                problems.add(msg);
//                break;
//
//            case PLANT:
//                break; // Always supported.
//
//            case REQUIREMENT:
//                if (params.contains(CheckParameters.ALLOW_REQ_INVS)) {
//                    break;
//                }
//                msg = fmt("Requirement invariants in %s are not supported.", parentTxt);
//                problems.add(msg);
//                break;
//
//            case SUPERVISOR:
//                if (params.contains(CheckParameters.ALLOW_SUP_INVS)) {
//                    break;
//                }
//                msg = fmt("Supervisor invariants in %s are not supported.", parentTxt);
//                problems.add(msg);
//                break;
//        }
//    }
}
