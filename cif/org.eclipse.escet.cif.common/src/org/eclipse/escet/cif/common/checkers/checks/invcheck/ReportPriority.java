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

package org.eclipse.escet.cif.common.checkers.checks.invcheck;

/** Relevance of reporting an aspect of a rule. */
public enum ReportPriority {
    /** All invariants are disabled by the aspect. */
    GLOBAL(100),

    /** Some of the invariants are disabled on the aspect. */
    GROUP(10),

    /** One case of the invariants is disabled on the aspect. */
    ELEMENT(1);

    /** Relative relevance of reporting an aspect of a rule, higher value is more relevant. */
    private int relevance;

    /**
     * Constructor of the {@link ReportPriority} enumeration.
     *
     * @param relevance Relative relevance of reporting an aspect of a rule, higher value is more relevant.
     */
    private ReportPriority(int relevance) {
        this.relevance = relevance;
    }

    /**
     * Retrieve how relevant it is to report on the aspect.
     *
     * @return Relevance of reporting on the aspect.
     */
    public int getReportRelevance() {
        return relevance;
    }
}
