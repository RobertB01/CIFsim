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

package org.eclipse.escet.cif.typechecker.postchk;

import static org.eclipse.escet.common.java.Maps.map;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.escet.cif.common.CifAddressableUtils;
import org.eclipse.escet.cif.common.CifScopeUtils;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.IoDecl;
import org.eclipse.escet.cif.metamodel.cif.automata.Assignment;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.ElifUpdate;
import org.eclipse.escet.cif.metamodel.cif.automata.IfUpdate;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.automata.Update;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgIn;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.expressions.ContVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.InputVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ProjectionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ReceivedExpression;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.StringType;
import org.eclipse.escet.cif.typechecker.AssignmentUniquenessChecker;
import org.eclipse.escet.cif.typechecker.ErrMsg;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Pair;
import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;
import org.eclipse.escet.common.typechecker.SemanticException;

/**
 * Assignments type checker which is used for the 'post' type checking phase. It checks the following:
 * <ul>
 * <li>'Assignment.addressableSyntax' constraint.</li>
 * <li>'Assignment.variablesInScope' constraint.</li>
 * <li>'Edge.uniqueVariables' constraint.</li>
 * <li>'SvgIn.uniqueVariables' constraint.</li>
 * </ul>
 */
public class AssignmentPostChecker {
    /**
     * Checks the specification for various constraints (see {@link AssignmentPostChecker}).
     *
     * <p>
     * We need to check this after the elimination of component definition/instantiation to ensure proper scope checking
     * of variables (mostly related to component parameters and component instantiations).
     * </p>
     *
     * @param comp The component to check, recursively. The component must not include any component
     *     definitions/instantiations.
     * @param env The post check environment to use.
     */
    public void check(ComplexComponent comp, CifPostCheckEnv env) {
        // Check I/O declarations.
        check(comp.getIoDecls(), env);

        if (comp instanceof Group group) {
            // Check child components.
            for (Component child: group.getComponents()) {
                check((ComplexComponent)child, env);
            }
        } else if (comp instanceof Automaton aut) {
            // Check for automaton.
            check(aut, env);
        }
    }

    /**
     * Checks the automaton for various constraints related to assignments on edges (see {@link AssignmentPostChecker}).
     *
     * @param aut The automaton to check.
     * @param env The post check environment to use.
     */
    private void check(Automaton aut, CifPostCheckEnv env) {
        // Check all edges.
        for (Location loc: aut.getLocations()) {
            for (Edge edge: loc.getEdges()) {
                try {
                    // Check for referencing the correct objects.
                    for (Update update: edge.getUpdates()) {
                        check(update, aut, UpdateContext.EDGE_UPDATE, env);
                    }

                    // Check for assignments to unique parts of variables.
                    Map<Declaration, Set<Pair<Position, List<Object>>>> asgnMap = map();
                    AssignmentUniquenessChecker.checkUniqueAsgns(edge.getUpdates(), asgnMap, env,
                            ErrMsg.DUPL_VAR_ASGN_EDGE);
                } catch (SemanticException ex) {
                    // Exception is thrown for reference to wrong kind of object or in case a projection index cannot be
                    // statically evaluated. Continue with the next.
                    continue;
                }
            }
        }
    }

    /**
     * Checks the I/O declarations for various constraints related to assignments in SVG input mappings (see
     * {@link AssignmentPostChecker}).
     *
     * @param ioDecls The I/O declarations to check.
     * @param env The post check environment to use.
     */
    private void check(List<IoDecl> ioDecls, CifPostCheckEnv env) {
        for (IoDecl ioDecl: ioDecls) {
            // Only check SVG input mappings.
            if (!(ioDecl instanceof SvgIn svgIn)) {
                continue;
            }

            // Only check SVG input mappings that contain updates.
            if (svgIn.getUpdates().isEmpty()) {
                continue;
            }

            try {
                // Check for referencing the correct objects.
                for (Update update: svgIn.getUpdates()) {
                    check(update, (ComplexComponent)svgIn.eContainer(), UpdateContext.SVG_UPDATE, env);
                }

                // Check for assignments to unique parts of variables.
                Map<Declaration, Set<Pair<Position, List<Object>>>> asgnMap = map();
                AssignmentUniquenessChecker.checkUniqueAsgns(svgIn.getUpdates(), asgnMap, env,
                        ErrMsg.DUPL_VAR_ASGN_SVG);
            } catch (SemanticException ex) {
                // Exception is thrown for reference to wrong kind of object or in case a projection index cannot be
                // statically evaluated. Continue with the next.
                continue;
            }
        }
    }

    /**
     * Checks an update for assigning to wrong kind of object.
     *
     * @param update The update to check.
     * @param scope The scope in which the update is located.
     * @param context The update context to use.
     * @param env The post check environment to use.
     */
    private void check(Update update, ComplexComponent scope, UpdateContext context, CifPostCheckEnv env) {
        if (update instanceof Assignment assignment) {
            Expression addr = assignment.getAddressable();

            // Check based on update context.
            if (context == UpdateContext.EDGE_UPDATE) {
                checkEdgeAddressable(addr, scope, env);
            } else if (context == UpdateContext.SVG_UPDATE) {
                checkSvgAddressable(addr, env);
            } else {
                throw new RuntimeException("Update needs either edge context or SVG context.");
            }
        } else if (update instanceof IfUpdate ifUpdate) {
            checkIfUpdate(ifUpdate, scope, context, env);
        } else {
            throw new RuntimeException("Unknown update: " + update);
        }
    }

    /**
     * Checks an 'if' update for assigning to wrong kind of object.
     *
     * @param update The 'if' update to check.
     * @param scope The scope in which the update is located.
     * @param context The update context to use.
     * @param env The post check environment to use.
     */
    private void checkIfUpdate(IfUpdate update, ComplexComponent scope, UpdateContext context, CifPostCheckEnv env) {
        // Thens.
        for (Update then: update.getThens()) {
            check(then, scope, context, env);
        }

        // Elses.
        for (Update els: update.getElses()) {
            check(els, scope, context, env);
        }

        // Elifs.
        for (ElifUpdate elif: update.getElifs()) {
            // Elif/thens.
            for (Update then: elif.getThens()) {
                check(then, scope, context, env);
            }
        }
    }

    /**
     * Checks an addressable on an edge for reference to wrong kind of object.
     *
     * @param addr The addressable.
     * @param scope The scope in which the update is located.
     * @param env The post check environment to use.
     */
    private void checkEdgeAddressable(Expression addr, ComplexComponent scope, CifPostCheckEnv env) {
        Assert.check(scope instanceof Automaton);

        // Make sure we refer to local discrete and/or continuous variables.
        for (Expression expr: CifAddressableUtils.getRefExprs(addr)) {
            // Get variable.
            Declaration var;
            if (expr instanceof DiscVariableExpression dvarExpr) {
                var = dvarExpr.getVariable();
            } else if (expr instanceof ContVariableExpression cvarExpr) {
                Assert.check(!cvarExpr.isDerivative()); // Parser doesn't allow this.
                var = cvarExpr.getVariable();
            } else if (expr instanceof ReceivedExpression) {
                throw new RuntimeException("Parser doesn't allow this.");
            } else {
                // Reference to wrong kind of object.
                PositionObject obj = CifScopeUtils.getRefObjFromRef(expr);
                env.addProblem(ErrMsg.RESOLVE_NON_ASGN_VAR, expr.getPosition(), CifTextUtils.getAbsName(obj));
                throw new SemanticException();
            }

            // Check variable scoping: disallow non-local variables.
            // Get scope of addressed variable.
            EObject varParent = var.eContainer();
            Assert.check(varParent instanceof ComplexComponent);

            // Do the scope check.
            if (varParent != scope) {
                env.addProblem(ErrMsg.ASGN_NON_LOCAL_VAR, expr.getPosition(), CifTextUtils.getAbsName(var),
                        CifTextUtils.getAbsName(scope));
                // Non-fatal error.
            }

            // Check for no string projections as addressables.
            checkForStringProjection(expr, var, env);
        }
    }

    /**
     * Checks an addressable in an SVG input mapping for reference to wrong kind of object.
     *
     * @param addr The addressable.
     * @param env The post check environment to use.
     */
    private void checkSvgAddressable(Expression addr, CifPostCheckEnv env) {
        // Make sure we refer to input variables.
        for (Expression expr: CifAddressableUtils.getRefExprs(addr)) {
            // Get variable.
            Declaration var;
            if (expr instanceof InputVariableExpression ivexpr) {
                var = ivexpr.getVariable();
            } else {
                // Reference to wrong kind of object.
                PositionObject obj = CifScopeUtils.getRefObjFromRef(expr);
                env.addProblem(ErrMsg.RESOLVE_NON_SVG_ASGN_VAR, expr.getPosition(), CifTextUtils.getAbsName(obj));
                throw new SemanticException();
            }

            // Check for no string projections as addressables.
            checkForStringProjection(expr, var, env);
        }
    }

    /**
     * Check for no string projections as addressables.
     *
     * @param expr The variable reference expressions.
     * @param var The variable that is addressed.
     * @param env The post check environment to use.
     */
    private void checkForStringProjection(Expression expr, Declaration var, CifPostCheckEnv env) {
        PositionObject varAncestor = (PositionObject)expr.eContainer();
        while (varAncestor instanceof ProjectionExpression) {
            ProjectionExpression proj = (ProjectionExpression)varAncestor;
            CifType type = proj.getChild().getType();
            CifType ntype = CifTypeUtils.normalizeType(type);
            if (ntype instanceof StringType) {
                env.addProblem(ErrMsg.ASGN_STRING_PROJ, varAncestor.getPosition(), CifTextUtils.getAbsName(var));
                // Non-fatal error.
            }
            varAncestor = (PositionObject)varAncestor.eContainer();
        }
    }

    /** Context of the update. */
    enum UpdateContext {
        /** SVG input update. Allow assignments to input variables. */
        SVG_UPDATE,

        /** Edge update. Allow assignments to continuous and discrete variables. */
        EDGE_UPDATE;
    }
}
