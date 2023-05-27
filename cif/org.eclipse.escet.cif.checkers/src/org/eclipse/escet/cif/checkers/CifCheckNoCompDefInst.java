//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.checkers;

/**
 * CIF check. Checks whether a given CIF specification satisfies a certain condition.
 *
 * <p>
 * Deriving from this class implies that the check does not support component definitions and/or component
 * instantiations. Such language constructs should not exist in the checked CIF specification.
 * </p>
 *
 * @implSpec Override only the relevant {@code preprocess*} and {@code postprocess*} methods. The {@code *crawl*} and
 *     {@code walk*} methods should not be overridden, as they are ignored by {@link CifChecker}.
 */
public class CifCheckNoCompDefInst extends CifCheck {
    @Override
    public boolean supportsCompDefInst() {
        return false;
    }
}
