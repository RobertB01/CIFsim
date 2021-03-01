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

package org.eclipse.escet.cif.cif2plc.writers;

import static org.eclipse.escet.common.java.Strings.fmt;

import java.io.File;

import org.eclipse.escet.cif.cif2plc.plcdata.PlcConfiguration;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcPou;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcProject;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcTypeDecl;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.exceptions.InvalidInputException;
import org.eclipse.escet.common.box.Box;

/** IEC 61131-3 writer. */
public class Iec611313Writer {
    /** Constructor for the {@link Iec611313Writer} class. */
    private Iec611313Writer() {
        // Static class.
    }

    /**
     * Writes the given PLC project to files in IEC 61131-3 syntax.
     *
     * @param project The PLC project to write.
     * @param outPath The absolute local file system path of the directory to which to write the files.
     */
    public static void write(PlcProject project, String outPath) {
        File outPathFile = new File(outPath);
        if (!outPathFile.isDirectory()) {
            String msg = fmt("Failed to write PLC code: path \"%s\" does not exist, or is not a directory.", outPath);
            throw new InvalidInputException(msg);
        }

        // Write configurations.
        for (PlcConfiguration config: project.configurations) {
            write(config, outPath);
        }

        // Write POUs.
        for (PlcPou pou: project.pous) {
            write(pou, outPath);
        }

        // Write type declarations.
        for (PlcTypeDecl typeDecl: project.typeDecls) {
            write(typeDecl, outPath);
        }
    }

    /**
     * Writes the given configuration to a file in IEC 61131-3 syntax.
     *
     * @param config The configuration to write.
     * @param outPath The absolute local file system path of the directory to which to write the file.
     */
    private static void write(PlcConfiguration config, String outPath) {
        String path = Paths.join(outPath, config.name + ".plccfg");
        Box code = config.toBox();
        code.writeToFile(path);
    }

    /**
     * Writes the given POU to a file in IEC 61131-3 syntax.
     *
     * @param pou The POU to write.
     * @param outPath The absolute local file system path of the directory to which to write the file.
     */
    private static void write(PlcPou pou, String outPath) {
        String ext = (pou.retType == null) ? ".plcprog" : ".plcfunc";
        String path = Paths.join(outPath, pou.name + ext);
        Box code = pou.toBox();
        code.writeToFile(path);
    }

    /**
     * Writes the given type declaration to a file in IEC 61131-3 syntax.
     *
     * @param typeDecl The type declaration to write.
     * @param outPath The absolute local file system path of the directory to which to write the file.
     */
    private static void write(PlcTypeDecl typeDecl, String outPath) {
        String path = Paths.join(outPath, typeDecl.name + ".plctype");
        Box code = typeDecl.toBox();
        code.writeToFile(path);
    }
}
