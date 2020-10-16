//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.setext.runtime.exceptions.tests;

import static org.junit.Assert.assertEquals;

import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.common.position.metamodel.position.PositionFactory;
import org.eclipse.escet.setext.runtime.exceptions.CustomSyntaxException;
import org.junit.Test;

/** Unit tests for the {@link CustomSyntaxException} class. */
@SuppressWarnings("javadoc")
public class CustomSyntaxExceptionTest {
    @Test
    public void testCustomSyntaxExToStrNoSrc() {
        Position pos = PositionFactory.eINSTANCE.createPosition();
        pos.setStartLine(1);
        pos.setStartColumn(2);
        Exception ex = new CustomSyntaxException("Some msg.", pos);
        assertEquals("Syntax error at line 1, column 2: Some msg.", ex.toString());
    }

    @Test
    public void testCustomSyntaxExToStrWithSrc() {
        Position pos = PositionFactory.eINSTANCE.createPosition();
        pos.setSource("File \"file.ext\": ");
        pos.setStartLine(1);
        pos.setStartColumn(2);
        Exception ex = new CustomSyntaxException("Some msg.", pos);
        assertEquals("File \"file.ext\": Syntax error at line 1, column 2: Some msg.", ex.toString());
    }
}
