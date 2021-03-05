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

package org.eclipse.escet.cif.explorer.runtime;

import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.cif.common.CifEvalException;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.common.app.framework.exceptions.InvalidModelException;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** {@link BaseState} for non-initial states. */
public class ExplorerState extends BaseState {
    /**
     * Constructor of the {@link ExplorerState} class.
     *
     * @param prev Previous state to copy (shallow copy).
     */
    public ExplorerState(BaseState prev) {
        super(prev.explorer, prev.locations.clone(), prev.values.clone());
    }

    @Override
    public Object getVarValue(PositionObject var) {
        return values[explorer.indices.get(var)];
    }

    @Override
    public void setVarValue(PositionObject var, Object value) {
        Assert.notNull(value);
        int index = explorer.indices.get(var);
        values[index] = value;
    }

    @Override
    public Location getCurrentLocation(int autIndex) {
        return locations[autIndex];
    }

    @Override
    public Expression getAlgExpression(AlgVariable algVar) {
        return explorer.algVariables.get(algVar).getExpression(locations);
    }

    @Override
    public boolean isInitial() {
        return false;
    }

    @Override
    public boolean isMarked() {
        // Check global marker predicates.
        for (Expression expr: explorer.markeds) {
            try {
                if (!(Boolean)eval(expr, null)) {
                    return false;
                }
            } catch (CifEvalException ex) {
                String msg = fmt("Failed to compute marker predicate \"%s\" for state \"%s\".",
                        CifTextUtils.exprToStr(expr), toString());
                throw new InvalidModelException(msg, ex);
            }
        }

        // Compute all marker predicates at all current locations. If they all
        // hold, the state is marked.
        for (Location loc: locations) {
            if (loc.getMarkeds().isEmpty()) {
                return false;
            }
            for (Expression expr: loc.getMarkeds()) {
                try {
                    if (!(Boolean)eval(expr, null)) {
                        return false;
                    }
                } catch (CifEvalException ex) {
                    String msg = fmt("Failed to compute marker predicate \"%s\" in %s, for state \"%s\".",
                            CifTextUtils.exprToStr(expr), CifTextUtils.getLocationText2(loc), toString());
                    throw new InvalidModelException(msg, ex);
                }
            }
        }

        return true;
    }
}
