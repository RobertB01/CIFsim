//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.cif2supremica;

import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;

import org.eclipse.escet.cif.common.CifPreconditionChecker;
import org.eclipse.escet.cif.common.checkers.CifCheck;
import org.eclipse.escet.cif.common.checkers.checks.AutNoKindlessCheck;
import org.eclipse.escet.cif.common.checkers.checks.AutOnlyWithOneInitLocCheck;
import org.eclipse.escet.cif.common.checkers.checks.CompNoInitPredsCheck;
import org.eclipse.escet.cif.common.checkers.checks.CompOnlyVarValueMarkerPredsCheck;
import org.eclipse.escet.cif.common.checkers.checks.CompStateInvsOnlyReqsCheck;
import org.eclipse.escet.cif.common.checkers.checks.EdgeNoUrgentCheck;
import org.eclipse.escet.cif.common.checkers.checks.EdgeOnlySimpleAssignmentsCheck;
import org.eclipse.escet.cif.common.checkers.checks.EventNoChannelsCheck;
import org.eclipse.escet.cif.common.checkers.checks.EventOnlyWithControllabilityCheck;
import org.eclipse.escet.cif.common.checkers.checks.ExprNoSpecificBinaryExprsCheck;
import org.eclipse.escet.cif.common.checkers.checks.ExprNoSpecificBinaryExprsCheck.NoSpecificBinaryOp;
import org.eclipse.escet.cif.common.checkers.checks.ExprNoSpecificExprsCheck;
import org.eclipse.escet.cif.common.checkers.checks.ExprNoSpecificExprsCheck.NoSpecificExpr;
import org.eclipse.escet.cif.common.checkers.checks.ExprNoSpecificUnaryExprsCheck;
import org.eclipse.escet.cif.common.checkers.checks.ExprNoSpecificUnaryExprsCheck.NoSpecificUnaryOp;
import org.eclipse.escet.cif.common.checkers.checks.FuncNoUserDefinedCheck;
import org.eclipse.escet.cif.common.checkers.checks.InvNoKindlessStateEvtExclCheck;
import org.eclipse.escet.cif.common.checkers.checks.LocNoStateInvsCheck;
import org.eclipse.escet.cif.common.checkers.checks.LocNoUrgentCheck;
import org.eclipse.escet.cif.common.checkers.checks.LocOnlyStaticEvalMarkerPredsCheck;
import org.eclipse.escet.cif.common.checkers.checks.TypeNoSpecificTypesCheck;
import org.eclipse.escet.cif.common.checkers.checks.TypeNoSpecificTypesCheck.NoSpecificType;
import org.eclipse.escet.cif.common.checkers.checks.VarNoContinuousCheck;
import org.eclipse.escet.cif.common.checkers.checks.VarNoDiscWithMultiInitValuesCheck;
import org.eclipse.escet.cif.common.checkers.checks.VarNoInputCheck;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.common.app.framework.exceptions.UnsupportedException;

/** CIF to Supremica transformation precondition checker. */
public class CifToSupremicaPreChecker {
    /** Constructor for the {@link CifToSupremicaPreChecker} class. */
    private CifToSupremicaPreChecker() {
        // Static class.
    }

    /**
     * Checks the CIF specification to make sure it satisfies the preconditions for the transformation.
     *
     * @param spec The CIF specification to check.
     * @throws UnsupportedException If a precondition is violated.
     */
    public static void check(Specification spec) {
        // Configure precondition checks.
        List<CifCheck> preconditions = list();

        // Kindless automata are not supported.
        preconditions.add(new AutNoKindlessCheck());

        // Events must be controllable or uncontrollable. Tau events are thus not supported.
        preconditions.add(new EventOnlyWithControllabilityCheck());

        // Initialization predicates outside of locations are not supported.
        preconditions.add(new CompNoInitPredsCheck());

        // Marker predicates outside of locations are only supported if they have the form discrete_variable =
        // marked_value. Discrete variables with multiple marker predicates are also not supported.
        preconditions.add(new CompOnlyVarValueMarkerPredsCheck());

        // Locations with initialization or marker predicates that are not trivially true or false are not supported.
        // This check only checks marker predicates. The check for a single initial location in each automaton, checks
        // the initialization predicates.
        preconditions.add(new LocOnlyStaticEvalMarkerPredsCheck());

        // Automata that do not have exactly one initial location are not supported. We allow only exactly one initial
        // location to ensure that the elimination of location references in expressions does not introduce additional
        // initialization predicates.
        preconditions.add(new AutOnlyWithOneInitLocCheck());

        // State invariants in locations are not supported. We would need to change 'invariant X' in location 'L' to
        // 'invariant L => X' (in the automaton), but references to locations are not supported by the transformation.
        // We do eliminate location references, so we could make a CIF to CIF transformation that lifts state
        // invariants out of locations to the surrounding automaton, and apply that transformation before the
        // elimination of location references.
        preconditions.add(new LocNoStateInvsCheck());

        // State invariants in components are only supported if they are requirement invariants.
        preconditions.add(new CompStateInvsOnlyReqsCheck());

        // State/event exclusion invariants are transformed into automata and must have a kind.
        preconditions.add(new InvNoKindlessStateEvtExclCheck());

        // Discrete variables with multiple potential initial values are not supported. Actually, Supremica allows an
        // initialization predicate, rather than a value, and the latest version seems to require initialization
        // predicates instead of initial values. However, using 'x == 1 | x != 1' for a variable 'x' in range '0..9',
        // in the latest version, seems to result in value '0' as initial value during simulation, without any way to
        // get an other initial value. To be safe, we'll require a single initial value.
        preconditions.add(new VarNoDiscWithMultiInitValuesCheck());

        // Continuous variables are not supported.
        preconditions.add(new VarNoContinuousCheck());

        // Input variables are not supported.
        preconditions.add(new VarNoInputCheck());

        // Multi-assignments, partial variable assignments and conditional updates on edges are not supported.
        preconditions.add(new EdgeOnlySimpleAssignmentsCheck());

        // Urgent locations and urgent edges are not supported.
        preconditions.add(new LocNoUrgentCheck());
        preconditions.add(new EdgeNoUrgentCheck());

        // User-defined functions are not supported.
        preconditions.add(new FuncNoUserDefinedCheck());

        // Channels are not supported.
        preconditions.add(new EventNoChannelsCheck());

        // Only the following data types are supported: boolean types, ranged integer types, and enumeration types.
        TypeNoSpecificTypesCheck typesCheck = new TypeNoSpecificTypesCheck( //
                NoSpecificType.DICT_TYPES, //
                NoSpecificType.DIST_TYPES, //
                NoSpecificType.FUNC_TYPES, //
                NoSpecificType.INT_TYPES_RANGELESS, //
                NoSpecificType.LIST_TYPES, //
                NoSpecificType.REAL_TYPES, //
                NoSpecificType.SET_TYPES, //
                NoSpecificType.STRING_TYPES, //
                NoSpecificType.TUPLE_TYPES);
        preconditions.add(typesCheck);

        // Only the following expressions are supported: boolean literal values, integer literal values, binary
        // expressions (partially, see below), unary expressions (partially, see below), and references to constants,
        // discrete variables, enumeration literals, and casts that don’t change the type.
        ExprNoSpecificExprsCheck exprsCheck = new ExprNoSpecificExprsCheck( //
                NoSpecificExpr.CAST_EXPRS_NON_EQUAL_TYPE, //
                NoSpecificExpr.DICT_LITS, //
                NoSpecificExpr.FUNC_CALLS, //
                NoSpecificExpr.IF_EXPRS, //
                NoSpecificExpr.LIST_LITS, //
                NoSpecificExpr.PROJECTION_EXPRS, //
                NoSpecificExpr.REAL_LITS, //
                NoSpecificExpr.SET_LITS, //
                NoSpecificExpr.SLICE_EXPRS, //
                NoSpecificExpr.STRING_LITS, //
                NoSpecificExpr.SWITCH_EXPRS, //
                NoSpecificExpr.TIME_VAR_REFS, //
                NoSpecificExpr.TUPLE_LITS);
        preconditions.add(exprsCheck);

        // Only the following binary operators are supported: logical equivalence (<=>), logical implication (=>),
        // conjunction (and) on boolean operands, disjunction (or) on boolean operands, addition (+) on ranged integer
        // operands, subtraction (-) on ranged integer operands, multiplication (*) on ranged integer operands, integer
        // division (div) on ranged integer operands, integer modulus (mod) on ranged integer operands, equality (=),
        // inequality (!=), less than (<) on ranged integer operands, less than or equal to (<=) on ranged integer
        // operands, greater than (>) on ranged integer operands, and greater than or equal to (>=) on ranged integer
        // operands.
        ExprNoSpecificBinaryExprsCheck binCheck = new ExprNoSpecificBinaryExprsCheck( //
                NoSpecificBinaryOp.CONJUNCTION_SETS, //
                NoSpecificBinaryOp.DISJUNCTION_SETS, //
                NoSpecificBinaryOp.ADDITION_DICTS, //
                NoSpecificBinaryOp.ADDITION_INTS_RANGELESS, //
                NoSpecificBinaryOp.ADDITION_LISTS, //
                NoSpecificBinaryOp.ADDITION_REALS, //
                NoSpecificBinaryOp.ADDITION_STRINGS, //
                NoSpecificBinaryOp.SUBTRACTION_DICTS, //
                NoSpecificBinaryOp.SUBTRACTION_INTS_RANGELESS, //
                NoSpecificBinaryOp.SUBTRACTION_LISTS, //
                NoSpecificBinaryOp.SUBTRACTION_REALS, //
                NoSpecificBinaryOp.SUBTRACTION_SETS, //
                NoSpecificBinaryOp.MULTIPLICATION_INTS_RANGELESS, //
                NoSpecificBinaryOp.MULTIPLICATION_REALS, //
                NoSpecificBinaryOp.INTEGER_DIVISION_INTS_RANGELESS, //
                NoSpecificBinaryOp.MODULUS_INTS_RANGELESS, //
                NoSpecificBinaryOp.GREATER_EQUAL_INTS_RANGELESS, //
                NoSpecificBinaryOp.GREATER_EQUAL_REALS, //
                NoSpecificBinaryOp.GREATER_THAN_INTS_RANGELESS, //
                NoSpecificBinaryOp.GREATER_THAN_REALS, //
                NoSpecificBinaryOp.LESS_EQUAL_INTS_RANGELESS, //
                NoSpecificBinaryOp.LESS_EQUAL_REALS, //
                NoSpecificBinaryOp.LESS_THAN_INTS_RANGELESS, //
                NoSpecificBinaryOp.LESS_THAN_REALS, //
                NoSpecificBinaryOp.DIVISION, //
                NoSpecificBinaryOp.ELEMENT_OF, //
                NoSpecificBinaryOp.SUBSET);
        preconditions.add(binCheck);

        // Only the following unary operators are supported: logical inverse (not), negation (-) on a ranged integer
        // operand, and plus (+) on a ranged integer operand.
        ExprNoSpecificUnaryExprsCheck unCheck = new ExprNoSpecificUnaryExprsCheck( //
                NoSpecificUnaryOp.NEGATE_INTS_RANGELESS, //
                NoSpecificUnaryOp.NEGATE_REALS, //
                NoSpecificUnaryOp.PLUS_INTS_RANGELESS, //
                NoSpecificUnaryOp.PLUS_REALS, //
                NoSpecificUnaryOp.SAMPLE);
        preconditions.add(unCheck);

        // Perform precondition check.
        new CifPreconditionChecker(preconditions).reportPreconditionViolations(spec, "CIF to Supremica transformation");
    }
}
