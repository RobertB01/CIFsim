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

package org.eclipse.escet.cif.common;

import static org.eclipse.escet.cif.common.CifCollectUtils.getComplexComponentsStream;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newAutomaton;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newGroup;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newSpecification;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.junit.jupiter.api.Test;

/** Tests for the {@link CifCollectUtils} class. */
public class CifCollectUtilsTest {
    /** Streaming test with an empty specification. */
    @Test
    public void testEmptyComponentStream() {
        Specification spec = newSpecification();

        long numComponents = getComplexComponentsStream(spec).count();
        assertEquals(1, numComponents);
    }

    /** Streaming test with a few nested complex components. */
    @Test
    public void testNestedComponentStream() {
        Specification spec = newSpecification();
        Group grp1 = newGroup();
        spec.getComponents().add(grp1);
        spec.getComponents().add(newAutomaton());
        Group grp2 = newGroup();
        grp1.getComponents().add(grp2);

        long numComponents = getComplexComponentsStream(spec).count();
        assertEquals(4, numComponents);
    }
}
