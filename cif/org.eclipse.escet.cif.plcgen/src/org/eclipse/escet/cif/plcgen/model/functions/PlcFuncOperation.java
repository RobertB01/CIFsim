//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.plcgen.model.functions;

/** Semantic operation performed by the function application. */
public enum PlcFuncOperation {
    /** Negate (unary minus) operation. */
    NEGATE_OP,

    /** Exponentiation operation. */
    EXP_OP,

    /** Multiplication operation. */
    MULTIPLY_OP,

    /** Division operation. */
    DIVIDE_OP,

    /** Modulo operation. */
    MODULO_OP,

    /** Add operation. */
    ADD_OP,

    /** Subtract operation. */
    SUBTRACT_OP,

    /** Less than compare operation. */
    LESS_THAN_OP,

    /** Less or equal compare operation. */
    LESS_EQUAL_OP,

    /** Greater than compare operation. */
    GREATER_THAN_OP,

    /** Greater or equal compare operation. */
    GREATER_EQUAL_OP,

    /** Equality compare operation. */
    EQUAL_OP,

    /** Inequality compare operation. */
    UNEQUAL_OP,

    /** Complementing (NOT) operation. */
    COMPLEMENT_OP,

    /** Conjunction operation. */
    AND_OP,

    /** Exclusive-disjunction operation. */
    XOR_OP,

    /** Disjunction operation. */
    OR_OP,

    /** Convert type operation. */
    CAST_OP,

    /** Tertiary if/else operation. */
    SEL_OP;
}
