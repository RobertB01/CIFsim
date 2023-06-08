//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.emf.tests;

import static org.eclipse.escet.common.java.Lists.list;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.escet.common.app.framework.AppEnv;
import org.eclipse.escet.common.emf.EMFResourceException;
import org.eclipse.escet.common.emf.ResourceManager;
import org.eclipse.escet.common.java.Exceptions;
import org.eclipse.escet.common.java.Strings;
import org.junit.Test;

/** Tests for the {@link ResourceManager} class. */
public class ResourceManagerTest {
    /** Test simple valid Ecore file. */
    @Test
    public void testSimpleValidEcoreFile() {
        test("simple.ecore", Collections.emptyList());
    }

    /** Test empty file. */
    @Test
    public void testEmpty() {
        List<String> expectedErrorLines = list();
        expectedErrorLines.add("ERROR: Failed to load resource \"<uri>\".");
        expectedErrorLines.add("CAUSE: org.xml.sax.SAXParseExceptionpublicId: <uri>; systemId: <uri>; "
                + "lineNumber: 1; columnNumber: 1; Premature end of file.");
        expectedErrorLines.add("CAUSE: Premature end of file.");
        test("empty_file.ecore", expectedErrorLines);
    }

    /** Test no file extension. */
    @Test
    public void testNoFileExtension() {
        test("no_file_ext", Collections.emptyList());
    }

    /** Test Ecore with invalid tag. */
    @Test
    public void testInvalidEcore() {
        List<String> expectedErrorLines = list();
        expectedErrorLines.add("ERROR: Failed to load resource \"<uri>\".");
        expectedErrorLines.add("CAUSE: org.eclipse.emf.ecore.xmi.FeatureNotFoundException: "
                + "Feature 'invalid_tag' not found. (<uri>, 4, 19)");
        expectedErrorLines.add("CAUSE: Feature 'invalid_tag' not found. (<uri>, 4, 19)");
        test("ecore_invalid_tag.ecore", expectedErrorLines);
    }

    /** Test no root. */
    @Test
    public void testRootNone() {
        List<String> expectedErrorLines = list();
        expectedErrorLines.add("ERROR: Failed to load resource \"<uri>\", as it contains 0 root objects, "
                + "while a single root object is expected.");
        test("root_none.ecore", expectedErrorLines);
    }

    /** Test multiple roots. */
    @Test
    public void testRootMultiple() {
        List<String> expectedErrorLines = list();
        expectedErrorLines.add("ERROR: Failed to load resource \"<uri>\", as it contains 2 root objects, "
                + "while a single root object is expected.");
        test("root_multiple.ecore", expectedErrorLines);
    }

    /** Test non-existing file. */
    @Test
    public void testDoesNotExist() {
        List<String> expectedErrorLines = list();
        expectedErrorLines.add("ERROR: Could not find \"<uri>\".");
        test("does_not_exist.ecore", expectedErrorLines);
    }

    /** Test non-{@link EPackage} root. */
    @Test
    public void testRootNotEPackage() {
        List<String> expectedErrorLines = list();
        expectedErrorLines.add("ERROR: Could not load resource \"<uri>\", "
                + "since it contains a root object of type \"EClassImpl\", "
                + "while a root object of type \"EPackage\" was expected.");
        test("root_not_epackage.ecore", expectedErrorLines);
    }

    /** Test Ecore with invalid reference. */
    @Test
    public void testEcoreInvalidRef() {
        List<String> expectedErrorLines = list();
        expectedErrorLines.add("ERROR: Failed to load resource \"<uri>\".");
        expectedErrorLines.add("CAUSE: org.eclipse.emf.ecore.xmi.UnresolvedReferenceException: "
                + "Unresolved reference '//Test2'. (<uri>, 4, 77)");
        expectedErrorLines.add("CAUSE: Unresolved reference '//Test2'. (<uri>, 4, 77)");
        test("ecore_invalid_ref.ecore", expectedErrorLines);
    }

    /** Test Ecore with error. */
    @Test
    public void testEcoreWithError() {
        List<String> expectedErrorLines = list();
        expectedErrorLines.add("ERROR: The resource loaded from \"<uri>\" has errors.");
        expectedErrorLines.add("CAUSE: Could not load resource \"<uri>\": resource has 4 errors, 0 warnings.");
        expectedErrorLines.add("CAUSE: ERROR: Diagnosis of org.eclipse.emf.ecore.impl.EPackageImpl@<hash>{<uri>#/} "
                + "(source: org.eclipse.emf.ecore)");
        expectedErrorLines.add("ERROR: The name 'null' is not well formed (source: org.eclipse.emf.ecore.model)");
        expectedErrorLines.add("ERROR: The name 'null' is not well formed (source: org.eclipse.emf.ecore.model)");
        expectedErrorLines.add("ERROR: The name 'null' is not well formed (source: org.eclipse.emf.ecore.model)");
        test("ecore_with_error.ecore", expectedErrorLines);
    }

    /**
     * Performs a single test.
     *
     * @param testName The name of the test file to load.
     * @param expectedErrorLines Expected lines of text of error message.
     */
    private void test(String testName, List<String> expectedErrorLines) {
        AppEnv.registerSimple();
        try {
            testInternal(testName, expectedErrorLines);
        } finally {
            AppEnv.unregisterApplication();
        }
    }

    /**
     * Performs a single test.
     *
     * @param testName The name of the test file to load.
     * @param expectedErrorLines Expected lines of text of error message.
     */
    private void testInternal(String testName, List<String> expectedErrorLines) {
        String pluginName = getClass().getPackage().getName();
        String uri = "platform:/plugin/" + pluginName + "/test_models/" + testName;
        try {
            // Load, assuming single EPackage root object.
            ResourceManager.loadObject(uri, EPackage.class);
        } catch (EMFResourceException e) {
            String expected = String.join(Strings.NL, expectedErrorLines);
            String actual = Exceptions.exToStr(e);
            actual = actual.replace(uri, "<uri>");
            actual = actual.replaceAll("\\@[0-9a-fA-F]+", "@<hash>");
            assertEquals(expected, actual);
            return;
        }
        assertTrue(expectedErrorLines.isEmpty());
    }
}
