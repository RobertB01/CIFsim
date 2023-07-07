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

package org.eclipse.escet.cif.io.emf;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Factory.Registry;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** CIF ASCII file EMF load/save integration tests. */
@SuppressWarnings("javadoc")
public class CifEmfIoTest {
    @BeforeEach
    public void setup() {
        // Register CIF resource factory with EMF.
        Registry registry = Resource.Factory.Registry.INSTANCE;
        Map<String, Object> mapping = registry.getExtensionToFactoryMap();
        mapping.put("cif", new CifResourceFactory());
    }

    @Test
    public void testLoadSaveSuccess() throws IOException {
        // Set input.
        String input = "const bool x = true;" + System.lineSeparator();
        byte[] bytes = input.getBytes(Charset.forName("UTF-8"));
        InputStream inputStream = new ByteArrayInputStream(bytes);

        // Create resource.
        ResourceSet resources = new ResourceSetImpl();
        URI uri = URI.createURI("test.cif");
        Resource resource = resources.createResource(uri);

        // Load.
        resource.load(inputStream, null);

        // Check results.
        assertEquals(0, resource.getErrors().size());
        assertEquals(0, resource.getWarnings().size());
        assertEquals(1, resource.getContents().size());
        EObject root = resource.getContents().get(0);
        assertTrue(root instanceof Specification);

        // Save.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        resource.save(outputStream, null);

        // Check results.
        assertEquals(0, resource.getErrors().size());
        assertEquals(0, resource.getWarnings().size());
        String output = outputStream.toString("UTF-8");
        assertEquals(input, output);
    }

    @Test
    public void testLoadSyntaxErr() throws IOException {
        // Set input.
        String input = "const int x =\n;";
        byte[] bytes = input.getBytes(Charset.forName("UTF-8"));
        InputStream stream = new ByteArrayInputStream(bytes);

        // Create resource.
        ResourceSet resources = new ResourceSetImpl();
        URI uri = URI.createURI("test.cif");
        Resource resource = resources.createResource(uri);

        // Load.
        resource.load(stream, null);

        // Check results.
        assertEquals(0, resource.getContents().size());
        assertEquals(1, resource.getErrors().size());
        assertEquals(0, resource.getWarnings().size());
    }

    @Test
    public void testLoadSemanticErr() throws IOException {
        // Set input.
        String input = "const int x = true;";
        byte[] bytes = input.getBytes(Charset.forName("UTF-8"));
        InputStream stream = new ByteArrayInputStream(bytes);

        // Create resource.
        ResourceSet resources = new ResourceSetImpl();
        URI uri = URI.createURI("test.cif");
        Resource resource = resources.createResource(uri);

        // Load.
        resource.load(stream, null);

        // Check results.
        assertEquals(0, resource.getContents().size());
        assertEquals(1, resource.getErrors().size());
        assertEquals(0, resource.getWarnings().size());
    }

    @Test
    public void testLoadSyntaxWarning() {
        // There are no syntax warnings in the CIF language.
    }

    @Test
    public void testLoadSemanticWarning() throws IOException {
        // Set input.
        String input = "controllable c;";
        byte[] bytes = input.getBytes(Charset.forName("UTF-8"));
        InputStream stream = new ByteArrayInputStream(bytes);

        // Create resource.
        ResourceSet resources = new ResourceSetImpl();
        URI uri = URI.createURI("test.cif");
        Resource resource = resources.createResource(uri);

        // Load.
        resource.load(stream, null);

        // Check results.
        assertEquals(1, resource.getContents().size());
        assertEquals(0, resource.getErrors().size());
        assertEquals(1, resource.getWarnings().size());
    }
}
