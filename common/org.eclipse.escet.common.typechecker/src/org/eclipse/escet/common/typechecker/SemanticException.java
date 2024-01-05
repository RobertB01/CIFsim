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

package org.eclipse.escet.common.typechecker;

/**
 * Semantic problem exception. Can be used in type checkers to indicate that a semantic problem has been detected, and
 * that type checking is prematurely terminated.
 */
public class SemanticException extends RuntimeException {
    // Only used to signal type checking has encountered a problem in the
    // input. Does not carry any additional data or functionality.
}
