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

package org.eclipse.escet.cif.plcgen.writers;

import org.eclipse.escet.cif.plcgen.model.declarations.PlcConfiguration;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcPou;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcProject;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcTypeDecl;
import org.eclipse.escet.cif.plcgen.targets.PlcTarget;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.box.Box;

/** Writer for generated PLC code for the ABB PLC target type. */
public class AbbWriter extends OutputTypeWriter {
    /**
     * Constructor of the {@link AbbWriter} class.
     *
     * @param target PLC target to generate code for.
     */
    public AbbWriter(PlcTarget target) {
        super(target);
    }

    @Override
    public void write(PlcProject project, String outputPath) {
        ensureDirectory(outputPath);

        // Write configurations.
        for (PlcConfiguration config: project.configurations) {
            write(config, outputPath);
        }

        // Write POUs.
        for (PlcPou pou: project.pous) {
            write(pou, outputPath);
        }

        // Write type declarations.
        for (PlcTypeDecl typeDecl: project.typeDecls) {
            write(typeDecl, outputPath);
        }
    }

    /**
     * Writes the given configuration to a file in IEC 61131-3 syntax.
     *
     * @param config The configuration to write.
     * @param outPath The absolute local file system path of the directory that should store the file.
     */
    private void write(PlcConfiguration config, String outPath) {
        String path = Paths.join(outPath, config.name + ".plccfg");
        Box code = toBox(config);
        code.writeToFile(path);
    }

    /**
     * Writes the given POU to a file in IEC 61131-3 syntax.
     *
     * @param pou The POU to write.
     * @param outPath The absolute local file system path of the directory that should store the file.
     */
    private void write(PlcPou pou, String outPath) {
        String ext = (pou.retType == null) ? ".plcprog" : ".plcfunc";
        String path = Paths.join(outPath, pou.name + ext);
        Box code = toBox(pou);
        code.writeToFile(path);
    }

    /**
     * Writes the given type declaration to a file in IEC 61131-3 syntax.
     *
     * @param typeDecl The type declaration to write.
     * @param outPath The absolute local file system path of the directory that should store the file.
     */
    private void write(PlcTypeDecl typeDecl, String outPath) {
        String path = Paths.join(outPath, typeDecl.name + ".plctype");
        Box code = toBox(typeDecl);
        code.writeToFile(path);
    }
}
