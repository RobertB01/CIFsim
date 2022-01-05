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

package org.eclipse.escet.cif.cif2cif;

import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newAssignment;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newContVariableExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newDiscVariableExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newElifExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newIfExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newRealType;
import static org.eclipse.escet.common.emf.EMFHelper.deepclone;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Sets.set;

import java.util.List;
import java.util.Set;

import org.eclipse.escet.cif.common.CifAddressableUtils;
import org.eclipse.escet.cif.common.CifAddressableUtils.DuplVarAsgnException;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Assignment;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.ElifUpdate;
import org.eclipse.escet.cif.metamodel.cif.automata.IfUpdate;
import org.eclipse.escet.cif.metamodel.cif.automata.Update;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.expressions.ContVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ElifExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IfExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ProjectionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TupleExpression;
import org.eclipse.escet.cif.metamodel.java.CifWalker;
import org.eclipse.escet.common.java.Assert;

/**
 * In-place transformation that eliminates 'if' updates on edges.
 *
 * <p>
 * For instance:
 *
 * <pre>
 * if g: if g2: x := 5 else y := 6 end end,
 * z := 7
 * </pre>
 *
 * This would become:
 *
 * <pre>
 * x := if g: if g2: 5 else x end else x end,
 * y := if g: if g2: y else 6 end else y end,
 * z := 7
 * </pre>
 * </p>
 *
 * <p>
 * Precondition: Multi-assignments, as well as partial variable assignments (projected addressables), are currently not
 * supported by this transformation. That is, only variable references are supported as addressables.
 * </p>
 *
 * <p>
 * Furthermore, the 'if' structure may be replicated for multiple variables, which may result in a blow-up of the
 * specification size.
 * </p>
 *
 * <p>
 * Performance: As a result of this transformation, variables may be assigned their old value, resulting in superfluous
 * assignments.
 * </p>
 */
public class ElimIfUpdates extends CifWalker implements CifToCifTransformation {
    @Override
    public void transform(Specification spec) {
        walkSpecification(spec);
    }

    @Override
    protected void preprocessEdge(Edge edge) {
        // Optimization for edges without updates.
        if (edge.getUpdates().isEmpty()) {
            return;
        }

        // Get variables assigned on the edge. Also checks the addressables
        // of assignments, to ensure they are supported.
        Set<Declaration> vars = set();
        for (Update update: edge.getUpdates()) {
            vars.addAll(getVariables(update));
        }

        // Transform the updates to explicit assignments per variable.
        List<Assignment> assignments = listc(vars.size());
        for (Declaration var: vars) {
            // Create and add assignment.
            Assignment assignment = newAssignment();
            assignments.add(assignment);

            // Set addressable.
            assignment.setAddressable(varToAddr(var));

            // Set value.
            Expression value = updatesToValue(edge.getUpdates(), var);
            assignment.setValue(value);
        }

        // Replace all updates.
        edge.getUpdates().clear();
        edge.getUpdates().addAll(assignments);
    }

    /**
     * Returns the variables (partially) assigned in the given update.
     *
     * @param update The update.
     * @return The variables (partially) assigned in the given update.
     */
    private Set<Declaration> getVariables(Update update) {
        // 'if' update.
        if (update instanceof IfUpdate) {
            IfUpdate iupdate = (IfUpdate)update;
            Set<Declaration> rslt = set();
            for (Update upd: iupdate.getThens()) {
                rslt.addAll(getVariables(upd));
            }
            for (ElifUpdate elif: iupdate.getElifs()) {
                for (Update upd: elif.getThens()) {
                    rslt.addAll(getVariables(upd));
                }
            }
            for (Update upd: iupdate.getElses()) {
                rslt.addAll(getVariables(upd));
            }
            return rslt;
        }

        // Assignment. Check supported addressable, and get the variables.
        Expression addr = ((Assignment)update).getAddressable();
        checkAddressable(addr);

        try {
            return CifAddressableUtils.getRefs(addr);
        } catch (DuplVarAsgnException e) {
            // Should not occur. We can only have this if in a single
            // multi-assignment, we assign different non-overlapping parts of
            // the same variable. Since we don't support multi-assignments
            // and partial variable assignments, this won't occur for this
            // transformation.
            throw new RuntimeException("Precondition violated.");
        }
    }

    /**
     * Creates and returns an addressable expression for the given variable.
     *
     * @param var The variable.
     * @return The newly created addressable expression for the variable.
     */
    private Expression varToAddr(Declaration var) {
        if (var instanceof DiscVariable) {
            DiscVariable dvar = (DiscVariable)var;
            DiscVariableExpression daddr = newDiscVariableExpression();
            daddr.setVariable(dvar);
            daddr.setType(deepclone(dvar.getType()));
            return daddr;
        } else {
            ContVariable cvar = (ContVariable)var;
            ContVariableExpression caddr = newContVariableExpression();
            caddr.setVariable(cvar);
            caddr.setType(newRealType());
            return caddr;
        }
    }

    /**
     * Transforms an sequence of updates to a value for the given variable as a result of the updates.
     *
     * @param updates The sequence of updates.
     * @param var The variable.
     * @return The value of the variable as a result of the given updates.
     */
    private Expression updatesToValue(List<Update> updates, Declaration var) {
        // Note that for a sequence of updates, at most one of them can assign
        // any given variable. This is due to the 'Edge.uniqueVariables'
        // constraint, which can be found in the CIF metamodel documentation,
        // together with not supporting partial variable assignments in this
        // transformation.
        for (Update update: updates) {
            if (getVariables(update).contains(var)) {
                return updateToValue(update, var);
            }
        }

        // None of the updates assigned the variable, so the variable keeps
        // its value. Note that this is not possible for the top level sequence
        // of updates in the edge, but only for sequences of updates part of
        // an 'if' update.
        return varToAddr(var);
    }

    /**
     * Transforms an update to a value for the given variable as a result of that update.
     *
     * @param update The update.
     * @param var The variable.
     * @return The value of the variable as a result of the update.
     */
    private Expression updateToValue(Update update, Declaration var) {
        // Assignment.
        if (update instanceof Assignment) {
            // Get addressable.
            Assignment asgn = (Assignment)update;
            Expression addr = asgn.getAddressable();

            // Return value.
            Set<Declaration> addrVars;
            try {
                addrVars = CifAddressableUtils.getRefs(addr);
            } catch (DuplVarAsgnException e) {
                // Should not occur. We can only have this if in a single
                // multi-assignment, we assign different non-overlapping parts
                // of the same variable. Since we don't support
                // multi-assignments and partial variable assignments, this
                // won't occur for this transformation.
                throw new RuntimeException("Precondition violated.");
            }
            Assert.check(addrVars.size() == 1);
            Declaration addrVar = addrVars.iterator().next();
            if (addrVar == var) {
                return asgn.getValue();
            } else {
                return varToAddr(var);
            }
        }

        // 'if' update.
        IfUpdate iupdate = (IfUpdate)update;

        IfExpression ifExpr = newIfExpression();
        ifExpr.setType(varToAddr(var).getType());

        ifExpr.getGuards().addAll(deepclone(iupdate.getGuards()));
        ifExpr.setThen(updatesToValue(iupdate.getThens(), var));

        for (ElifUpdate elif: iupdate.getElifs()) {
            ElifExpression elifExpr = newElifExpression();
            elifExpr.getGuards().addAll(deepclone(elif.getGuards()));
            elifExpr.setThen(updatesToValue(elif.getThens(), var));

            ifExpr.getElifs().add(elifExpr);
        }

        ifExpr.setElse(updatesToValue(iupdate.getElses(), var));

        return ifExpr;
    }

    /**
     * Check the addressable, to see whether it is supported. We don't support tuple addressables and projected
     * addressables.
     *
     * <p>
     * One of the reasons we don't support them, is that {@link CifAddressableUtils#getRefs} can't handle them properly.
     * Another reason is that is more complex to handle them. There is however no fundamental reason for this
     * transformation to not support them.
     * </p>
     *
     * @param addr The addresslable to check.
     */
    private void checkAddressable(Expression addr) {
        if (addr instanceof TupleExpression || addr instanceof ProjectionExpression) {
            String msg = "Eliminating 'if' updates from edges, from a CIF specification with multi-assignments "
                    + "and/or partial variable assignments (projected addressables), is currently not supported.";
            throw new CifToCifPreconditionException(msg);
        }
    }
}
