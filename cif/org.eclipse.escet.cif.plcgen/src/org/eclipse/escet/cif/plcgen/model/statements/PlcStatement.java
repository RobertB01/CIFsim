//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.plcgen.model.statements;

import static org.eclipse.escet.common.java.Lists.listc;

import java.util.List;
import java.util.stream.Collectors;

/** Statement in PLC code. */
public abstract class PlcStatement {
    /**
     * Make a copy of the statement.
     *
     * @return The newly created statement.
     */
    public abstract PlcStatement copy();

    /**
     * Copy a sequence of statements.
     *
     * @param statements Statements to copy.
     * @return The copied statements.
     */
    public static List<PlcStatement> copy(List<PlcStatement> statements) {
        List<PlcStatement> stats = listc(statements.size());
        statements.stream().map(PlcStatement::copy).collect(Collectors.toCollection(() -> stats));
        return stats;
    }
}
