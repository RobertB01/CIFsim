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

import org.eclipse.escet.common.position.metamodel.position.Position;

/** Problem reporter for type checker related issues. */
public interface CifTypeCheckerProblemReporter {
    /**
     * Adds a semantic problem to the list of problems found so far.
     *
     * @param message The CIF type checker problem message describing the semantic problem.
     * @param position Position information of the problem.
     * @param args The arguments to use when formatting the problem message.
     */
    public void addProblem(ErrMsg message, Position position, String... args);
}
