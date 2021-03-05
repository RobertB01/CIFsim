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

package org.eclipse.escet.cif.eventbased.analysis;

import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;

/** Storage class for temporary storing read source information. */
public class SourceReadInfo {
    /** Read number of plants. */
    public int numPlants = 0;

    /** Read source information. */
    public List<AutomatonNamesInfo> sourceInfo = list();
}
