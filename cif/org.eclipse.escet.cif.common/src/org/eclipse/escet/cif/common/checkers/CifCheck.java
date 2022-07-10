//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.common.checkers;

import org.eclipse.escet.cif.metamodel.java.CifWithArgWalker;

/**
 * CIF check. Checks whether a given CIF specification satisfies a certain condition.
 *
 * @implSpec Override only the relevant {@code preprocess*} and {@code postprocess*} methods. The {@code *crawl*} and
 *     {@code walk*} methods should not be overridden, as they are ignored by {@link CifChecker}.
 */
public abstract class CifCheck extends CifWithArgWalker<CifCheckViolations> {
    // Nothing to do here.
}
