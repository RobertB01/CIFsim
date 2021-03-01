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

package org.eclipse.escet.cif.simulator.compiler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

/** Version file code generator. */
public class VersionCodeGenerator {
    /** Constructor for the {@link VersionCodeGenerator} class. */
    private VersionCodeGenerator() {
        // Static class.
    }

    /**
     * Generate a version resource file.
     *
     * @param ctxt The compiler context to use.
     */
    public static void gencodeVersion(CifCompilerContext ctxt) {
        // Get output writer.
        String path = CifCompilerContext.VERSION_RES_NAME;
        path = CifCompilerContext.PACKAGE + "/" + path;
        ByteArrayOutputStream stream = ctxt.addResourceFile(path);
        Charset charset = Charset.forName("UTF-8");
        Writer writer = new OutputStreamWriter(stream, charset);

        // Write version.
        try {
            writer.write(ctxt.app.getAppVersion());
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            throw new RuntimeException("Failed to write app version.", ex);
        }
    }
}
