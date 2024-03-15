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

package org.eclipse.escet.cif.typechecker;

import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newAssignment;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newElifUpdate;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newIfUpdate;
import static org.eclipse.escet.cif.typechecker.CifExprsTypeChecker.BOOL_TYPE_HINT;
import static org.eclipse.escet.cif.typechecker.CifExprsTypeChecker.NO_TYPE_HINT;
import static org.eclipse.escet.cif.typechecker.CifExprsTypeChecker.transExpression;

import java.util.List;

import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.RangeCompat;
import org.eclipse.escet.cif.metamodel.cif.automata.Assignment;
import org.eclipse.escet.cif.metamodel.cif.automata.ElifUpdate;
import org.eclipse.escet.cif.metamodel.cif.automata.IfUpdate;
import org.eclipse.escet.cif.metamodel.cif.automata.Update;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.parser.ast.automata.AAssignmentUpdate;
import org.eclipse.escet.cif.parser.ast.automata.AElifUpdate;
import org.eclipse.escet.cif.parser.ast.automata.AIfUpdate;
import org.eclipse.escet.cif.parser.ast.automata.AUpdate;
import org.eclipse.escet.cif.parser.ast.expressions.AExpression;
import org.eclipse.escet.cif.typechecker.scopes.ParentScope;

/** Type checker for CIF updates. */
public class CifUpdateTypeChecker {
    /** Constructor for the {@link CifUpdateTypeChecker} class. */
    private CifUpdateTypeChecker() {
        // Static class.
    }

    /**
     * Type checks an update.
     *
     * @param astUpdate The CIF AST representation of the update.
     * @param scope The scope to resolve update in.
     * @param context The expression type checking context to use.
     * @param tchecker The CIF type checker to use.
     * @return The CIF metamodel representation of the update.
     */
    public static Update typeCheckUpdate(AUpdate astUpdate, ParentScope<?> scope, ExprContext context,
            CifTypeChecker tchecker)
    {
        if (astUpdate instanceof AAssignmentUpdate) {
            return typeCheckAssignment((AAssignmentUpdate)astUpdate, scope, context, tchecker);
        } else if (astUpdate instanceof AIfUpdate) {
            return typeCheckIfUpdate((AIfUpdate)astUpdate, scope, context, tchecker);
        } else {
            throw new RuntimeException("Unknown update: " + astUpdate);
        }
    }

    /**
     * Type checks an assignment update.
     *
     * @param astUpdate The CIF AST representation of the update.
     * @param scope The scope to resolve update in.
     * @param context The expression type checking context to use, or {@code null} for the default context.
     * @param tchecker The CIF type checker to use.
     * @return The CIF metamodel representation of the update.
     */
    private static Assignment typeCheckAssignment(AAssignmentUpdate astUpdate, ParentScope<?> scope,
            ExprContext context, CifTypeChecker tchecker)
    {
        // Construct assignment.
        Assignment asgn = newAssignment();
        asgn.setPosition(astUpdate.createPosition());

        // Type check and set addressable expression.
        // Do not check for references to non-local variables. Do this during post checking to avoid false positives and
        // false negatives.
        Expression addr = transExpression(astUpdate.addressable, NO_TYPE_HINT, scope, context, tchecker);
        asgn.setAddressable(addr);

        // Type check and set value.
        Expression value = transExpression(astUpdate.value, addr.getType(), scope, context, tchecker);
        asgn.setValue(value);

        // Compatible types for addressable and value.
        CifType valueType = value.getType();
        CifType addrType = addr.getType();
        if (!CifTypeUtils.checkTypeCompat(addrType, valueType, RangeCompat.OVERLAP)) {
            tchecker.addProblem(ErrMsg.ASGN_TYPE_VALUE_MISMATCH, astUpdate.position, CifTextUtils.typeToStr(valueType),
                    CifTextUtils.typeToStr(addrType));
            // Non-fatal error.
        }

        // Return metamodel representation of the assignment.
        return asgn;
    }

    /**
     * Type checks an 'if' update.
     *
     * @param astUpdate The CIF AST representation of the update.
     * @param scope The scope to resolve update in.
     * @param context The expression type checking context to use.
     * @param tchecker The CIF type checker to use.
     * @return The CIF metamodel representation of the update.
     */
    private static IfUpdate typeCheckIfUpdate(AIfUpdate astUpdate, ParentScope<?> scope, ExprContext context,
            CifTypeChecker tchecker)
    {
        // Construct 'if' update.
        IfUpdate update = newIfUpdate();
        update.setPosition(astUpdate.createPosition());

        // Guards.
        List<Expression> guards = update.getGuards();
        for (AExpression g: astUpdate.guards) {
            Expression guard = transExpression(g, BOOL_TYPE_HINT, scope, context, tchecker);
            CifType t = guard.getType();
            CifType nt = CifTypeUtils.normalizeType(t);
            if (!(nt instanceof BoolType)) {
                tchecker.addProblem(ErrMsg.GUARD_NON_BOOL, guard.getPosition(), CifTextUtils.typeToStr(t));
                // Non-fatal error.
            }
            guards.add(guard);
        }

        // Thens.
        List<Update> thens = update.getThens();
        for (AUpdate then1: astUpdate.thens) {
            Update then2 = typeCheckUpdate(then1, scope, context, tchecker);
            thens.add(then2);
        }

        // Elses.
        List<Update> elses = update.getElses();
        for (AUpdate else1: astUpdate.elses) {
            Update else2 = typeCheckUpdate(else1, scope, context, tchecker);
            elses.add(else2);
        }

        // Elifs.
        List<ElifUpdate> elifs = update.getElifs();
        for (AElifUpdate elif1: astUpdate.elifs) {
            ElifUpdate elif2 = newElifUpdate();
            elif2.setPosition(elif1.createPosition());
            elifs.add(elif2);

            // Guards.
            guards = elif2.getGuards();
            for (AExpression g: elif1.guards) {
                Expression guard = transExpression(g, BOOL_TYPE_HINT, scope, context, tchecker);
                CifType t = guard.getType();
                CifType nt = CifTypeUtils.normalizeType(t);
                if (!(nt instanceof BoolType)) {
                    tchecker.addProblem(ErrMsg.GUARD_NON_BOOL, guard.getPosition(), CifTextUtils.typeToStr(t));
                    // Non-fatal error.
                }
                guards.add(guard);
            }

            // Elif/thens.
            List<Update> elifThens = elif2.getThens();
            for (AUpdate then1: elif1.thens) {
                Update then2 = typeCheckUpdate(then1, scope, context, tchecker);
                elifThens.add(then2);
            }
        }

        // Return metamodel representation of the 'if' update.
        return update;
    }
}
