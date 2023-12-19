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

package org.eclipse.escet.cif.typechecker;

import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newAssignment;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newElifUpdate;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newIfUpdate;
import static org.eclipse.escet.cif.typechecker.CifExprsTypeChecker.BOOL_TYPE_HINT;
import static org.eclipse.escet.cif.typechecker.CifExprsTypeChecker.NO_TYPE_HINT;
import static org.eclipse.escet.cif.typechecker.CifExprsTypeChecker.transExpression;
import static org.eclipse.escet.cif.typechecker.ExprContext.Condition.EDGE_UPDATE;
import static org.eclipse.escet.cif.typechecker.ExprContext.Condition.SVG_UPDATE;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.escet.cif.common.CifAddressableUtils;
import org.eclipse.escet.cif.common.CifScopeUtils;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.RangeCompat;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.automata.Assignment;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.ElifUpdate;
import org.eclipse.escet.cif.metamodel.cif.automata.IfUpdate;
import org.eclipse.escet.cif.metamodel.cif.automata.Update;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.expressions.ContVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.InputVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ProjectionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ReceivedExpression;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.StringType;
import org.eclipse.escet.cif.parser.ast.automata.AAssignmentUpdate;
import org.eclipse.escet.cif.parser.ast.automata.AElifUpdate;
import org.eclipse.escet.cif.parser.ast.automata.AIfUpdate;
import org.eclipse.escet.cif.parser.ast.automata.AUpdate;
import org.eclipse.escet.cif.parser.ast.expressions.AExpression;
import org.eclipse.escet.cif.typechecker.scopes.AutDefScope;
import org.eclipse.escet.cif.typechecker.scopes.AutScope;
import org.eclipse.escet.cif.typechecker.scopes.ParentScope;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;
import org.eclipse.escet.common.typechecker.SemanticException;

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
     * @param context The expression type checking context to use, or {@code null} for the default context.
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
        Expression addr = typeCheckAddressable(astUpdate.addressable, scope, context, tchecker);
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
     * @param context The expression type checking context to use, or {@code null} for the default context.
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

    /**
     * Type checks an addressable.
     *
     * @param astAddr The CIF AST representation of the addressable.
     * @param scope The scope to resolve update in.
     * @param context The expression type checking context to use, or {@code null} for the default context.
     * @param tchecker The CIF type checker to use.
     * @return The CIF metamodel representation of the addressable.
     */
    private static Expression typeCheckAddressable(AExpression astAddr, ParentScope<?> scope, ExprContext context,
            CifTypeChecker tchecker)
    {
        // Type check addressable expression.
        Expression addr = transExpression(astAddr, NO_TYPE_HINT, scope, context, tchecker);

        // Check if there is context.
        if (context == null) {
            throw new RuntimeException("Need expression context.");
        }

        // Type check based on context.
        if (context.conditions.contains(EDGE_UPDATE)) {
            typeCheckEdgeAddressable(addr, scope, tchecker);
        } else if (context.conditions.contains(SVG_UPDATE)) {
            typeCheckSvgAddressable(addr, scope, tchecker);
        } else {
            throw new RuntimeException("Update needs either edge context or SVG context.");
        }

        // Return metamodel representation of the addressable expression.
        return addr;
    }

    /**
     * Type checks an addressable on an edge.
     *
     * @param addr The CIF metamodel representation of the addressable.
     * @param scope The scope to resolve update in.
     * @param tchecker The CIF type checker to use.
     */
    private static void typeCheckEdgeAddressable(Expression addr, ParentScope<?> scope, CifTypeChecker tchecker) {
        // Make sure we refer to local discrete and/or continuous variables.
        for (Expression expr: CifAddressableUtils.getRefExprs(addr)) {
            // Get variable.
            Expression uexpr = CifTypeUtils.unwrapExpression(expr);
            Declaration var;
            if (uexpr instanceof DiscVariableExpression) {
                var = ((DiscVariableExpression)uexpr).getVariable();
            } else if (uexpr instanceof ContVariableExpression) {
                var = ((ContVariableExpression)uexpr).getVariable();
            } else if (uexpr instanceof ReceivedExpression) {
                throw new RuntimeException("Parser doesn't allow this.");
            } else {
                // Reference to wrong kind of object.
                PositionObject obj = CifScopeUtils.getRefObjFromRef(uexpr);
                tchecker.addProblem(ErrMsg.RESOLVE_NON_ASGN_VAR, expr.getPosition(), CifTextUtils.getAbsName(obj));
                throw new SemanticException();
            }

            // Check variable scoping: disallow non-local variables.
            // Get scope of addressed variable.
            EObject varParent = var.eContainer();
            Assert.check(varParent instanceof ComplexComponent);

            // Get scope of update.
            Automaton curAut;
            if (scope instanceof AutScope) {
                curAut = ((AutScope)scope).getAutomaton();
            } else if (scope instanceof AutDefScope) {
                curAut = ((AutDefScope)scope).getAutomaton();
            } else {
                throw new RuntimeException("Must be an automaton scope.");
            }

            // Do the scope check.
            if (varParent != curAut) {
                tchecker.addProblem(ErrMsg.ASGN_NON_LOCAL_VAR, expr.getPosition(), CifTextUtils.getAbsName(var),
                        CifTextUtils.getAbsName(curAut));
                // Non-fatal error.
            }

            // Warn for string projections as addressables.
            checkForStringProjection(expr, var, tchecker);
        }
    }

    /**
     * Type checks an addressable in an SVG input mapping.
     *
     * @param addr The CIF metamodel representation of the addressable.
     * @param scope The scope to resolve update in.
     * @param tchecker The CIF type checker to use.
     */
    private static void typeCheckSvgAddressable(Expression addr, ParentScope<?> scope, CifTypeChecker tchecker) {
        // Make sure we refer to an input variables.
        for (Expression expr: CifAddressableUtils.getRefExprs(addr)) {
            // Get variable.
            Expression uexpr = CifTypeUtils.unwrapExpression(expr);
            Declaration var;
            if (uexpr instanceof InputVariableExpression ivexpr) {
                var = ivexpr.getVariable();
            } else {
                // Reference to wrong kind of object.
                PositionObject obj = CifScopeUtils.getRefObjFromRef(uexpr);
                tchecker.addProblem(ErrMsg.RESOLVE_NON_SVG_ASGN_VAR, expr.getPosition(), CifTextUtils.getAbsName(obj));
                throw new SemanticException();
            }

            // Warn for string projections as addressables.
            checkForStringProjection(expr, var, tchecker);
        }
    }

    /**
     * Warns for string projections as addressables.
     *
     * @param expr The variable reference expressions.
     * @param var The variable that is addressed.
     * @param tchecker The CIF type checker to use.
     */
    private static void checkForStringProjection(Expression expr, Declaration var, CifTypeChecker tchecker) {
        // String projections are not allowed as addressables.
        PositionObject varAncestor = (PositionObject)expr.eContainer();
        while (varAncestor instanceof ProjectionExpression) {
            ProjectionExpression proj = (ProjectionExpression)varAncestor;
            CifType type = proj.getChild().getType();
            CifType ntype = CifTypeUtils.normalizeType(type);
            if (ntype instanceof StringType) {
                tchecker.addProblem(ErrMsg.ASGN_STRING_PROJ, varAncestor.getPosition(), CifTextUtils.getAbsName(var));
                // Non-fatal error.
            }
            varAncestor = (PositionObject)varAncestor.eContainer();
        }
    }
}
