//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

/** IEC 61131-3 writer. */
public class Iec611313Writer extends Writer {
    /**
     * Constructor of the {@link Iec611313Writer} class.
     *
     * @param target PLC target to generate code for.
     */
    public Iec611313Writer(PlcTarget target) {
        super(target);
    }

    @Override
    public void write(PlcProject project, String outPath) {
        ensureDirectory(outPath);

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
    private void write(PlcConfiguration config, String outPath) {
        String path = Paths.join(outPath, config.name + ".plccfg");
        Box code = toBox(config);
        code.writeToFile(path);
    }

    /**
     * Writes the given POU to a file in IEC 61131-3 syntax.
     *
     * @param pou The POU to write.
     * @param outPath The absolute local file system path of the directory to which to write the file.
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
     * @param outPath The absolute local file system path of the directory to which to write the file.
     */
    private void write(PlcTypeDecl typeDecl, String outPath) {
        String path = Paths.join(outPath, typeDecl.name + ".plctype");
        Box code = toBox(typeDecl);
        code.writeToFile(path);
    }
}
