//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.common;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Sets.set;

import java.util.List;
import java.util.Set;

import org.eclipse.escet.cif.metamodel.cif.automata.Assignment;
import org.eclipse.escet.cif.metamodel.cif.automata.ElifUpdate;
import org.eclipse.escet.cif.metamodel.cif.automata.IfUpdate;
import org.eclipse.escet.cif.metamodel.cif.automata.Update;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.expressions.ContVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ProjectionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TupleExpression;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Lists;

/** CIF addressables utility methods. */
public class CifAddressableUtils {
    /** Constructor for the {@link CifAddressableUtils} class. */
    private CifAddressableUtils() {
        // Static class.
    }

    /**
     * Returns the variable reference expressions for the variables (partially) assigned in an addressable, by
     * recursively stripping of tuple expressions and projection expressions.
     *
     * @param addr The addressable expression.
     * @return The variable reference expressions for the variables (partially) assigned in the addressable.
     */
    public static List<Expression> getRefExprs(Expression addr) {
        if (addr instanceof TupleExpression) {
            // Recursively strip off top level tuples.
            List<Expression> rslt = list();
            for (Expression elem: ((TupleExpression)addr).getFields()) {
                rslt.addAll(getRefExprs(elem));
            }
            return rslt;
        } else if (addr instanceof ProjectionExpression) {
            // Strip off top level projections, but not anything else.
            while (addr instanceof ProjectionExpression) {
                addr = ((ProjectionExpression)addr).getChild();
            }
            return list(addr);
        } else {
            // Not a tuple or projection, so it is a reference directly (if
            // the addressable is valid to begin with).
            return list(addr);
        }
    }

    /**
     * Returns the variables (partially) assigned in an addressable, by recursively stripping of tuple expressions and
     * projection expressions, and obtaining the variables from the variable reference expressions.
     *
     * <p>
     * In the CIF language, it is allowed to assign different parts of the same variable, on a single edge, or even in a
     * single (multi-)assignment. This is allowed in CIF, as long as it can be statically checked that there is never a
     * possibility for overlap (assigning the same part of the variable more than once). This method however, assumes
     * that each variable that is fully or partially assigned in a single (multi-)assignment, is for a different
     * variable altogether.
     * </p>
     *
     * <p>
     * Due to the above assumption, this method should be avoided. Use the {@link #collectAddrVars(Expression, Set)}
     * method instead, if possible.
     * </p>
     *
     * <p>
     * Note that even though the result is a set, all addressable variables are included. Due to the above assumption
     * that all variables are in the addressable are unique, the number of variables in the set is equal to the number
     * of (partial) variable assignments. The set allows for fast element testing.
     * </p>
     *
     * <p>
     * The resulting set can be iterated, to return the addressable variables in the order that they occur (from left to
     * right) in the textual syntax of the addressable.
     * </p>
     *
     * @param addr The addressable expression.
     * @return The (partially) assigned variables.
     * @throws DuplVarAsgnException If the assumption that all variables that are (partially) assigned are unique,
     *     doesn't hold.
     */
    public static Set<Declaration> getRefs(Expression addr) throws DuplVarAsgnException {
        List<Expression> refExprs = getRefExprs(addr);
        Set<Declaration> rslt = set();
        for (Expression refExpr: refExprs) {
            if (refExpr instanceof DiscVariableExpression) {
                rslt.add(((DiscVariableExpression)refExpr).getVariable());
            } else if (refExpr instanceof ContVariableExpression) {
                rslt.add(((ContVariableExpression)refExpr).getVariable());
            } else {
                throw new RuntimeException("Unknown addressable: " + addr);
            }
        }
        if (refExprs.size() != rslt.size()) {
            throw new DuplVarAsgnException();
        }
        return rslt;
    }

    /**
     * Exception indicating duplicate assignment to (a part of) a variable, violating the 'unique assigned variables'
     * assumption of the {@link #getRefs} method.
     */
    public static class DuplVarAsgnException extends Exception {
        // No message or cause for this exception.
    }

    /**
     * Strips the projections of a possibly projected addressable variable.
     *
     * @param addr The possibly projected addressable variable.
     * @return The addressable variable, without any projections.
     */
    public static Expression stripProjs(Expression addr) {
        Expression rslt = addr;
        while (rslt instanceof ProjectionExpression) {
            rslt = ((ProjectionExpression)rslt).getChild();
        }
        return rslt;
    }

    /**
     * Collects the projection expressions in the addressable. Projections closest to the addressable variable are more
     * to the beginning of the resulting list than projections farther away from the addressable variable.
     *
     * @param addr The addressable. Must not be a tuple addressable.
     * @return The projection expressions.
     */
    public static List<ProjectionExpression> collectProjs(Expression addr) {
        Assert.check(!(addr instanceof TupleExpression));

        // Collect projects while we strip them off.
        List<ProjectionExpression> rslt = list();
        while (addr instanceof ProjectionExpression) {
            ProjectionExpression paddr = (ProjectionExpression)addr;
            rslt.add(paddr);
            addr = paddr.getChild();
        }

        // Reverse the list.
        rslt = Lists.reverse(rslt);

        // Return the collected projections.
        return rslt;
    }

    /**
     * Collects the variables (partially) assigned in the given updates.
     *
     * @param updates The updates.
     * @param vars The variables collected so far. Is modified in-place.
     * @see #getRefs
     */
    public static void collectAddrVars(List<Update> updates, Set<Declaration> vars) {
        for (Update update: updates) {
            collectAddrVars(update, vars);
        }
    }

    /**
     * Collects the variables (partially) assigned in the given update.
     *
     * @param update The update.
     * @param vars The variables collected so far. Is modified in-place.
     */
    public static void collectAddrVars(Update update, Set<Declaration> vars) {
        if (update instanceof IfUpdate) {
            IfUpdate ifUpd = (IfUpdate)update;
            collectAddrVars(ifUpd.getThens(), vars);
            for (ElifUpdate elifUpd: ifUpd.getElifs()) {
                collectAddrVars(elifUpd.getThens(), vars);
            }
            collectAddrVars(ifUpd.getElses(), vars);
        } else {
            Assignment asgn = (Assignment)update;
            collectAddrVars(asgn.getAddressable(), vars);
        }
    }

    /**
     * Collects the variables (partially) assigned in the given addressable.
     *
     * @param addr The addressable.
     * @param vars The variables collected so far. Is modified in-place.
     */
    public static void collectAddrVars(Expression addr, Set<Declaration> vars) {
        if (addr instanceof TupleExpression) {
            for (Expression elem: ((TupleExpression)addr).getFields()) {
                collectAddrVars(elem, vars);
            }
        } else if (addr instanceof ProjectionExpression) {
            while (addr instanceof ProjectionExpression) {
                addr = ((ProjectionExpression)addr).getChild();
            }
            collectAddrVars(addr, vars);
        } else if (addr instanceof DiscVariableExpression) {
            vars.add(((DiscVariableExpression)addr).getVariable());
        } else if (addr instanceof ContVariableExpression) {
            vars.add(((ContVariableExpression)addr).getVariable());
        } else {
            throw new RuntimeException("Unknown addr: " + addr);
        }
    }
}
