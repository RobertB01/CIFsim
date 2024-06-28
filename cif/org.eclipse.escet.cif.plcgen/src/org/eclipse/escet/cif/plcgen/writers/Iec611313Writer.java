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
import org.eclipse.escet.cif.plcgen.model.declarations.PlcDeclaredType;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcPou;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcProject;
import org.eclipse.escet.cif.plcgen.model.types.PlcEnumType;
import org.eclipse.escet.cif.plcgen.model.types.PlcStructType;
import org.eclipse.escet.cif.plcgen.targets.PlcTarget;
import org.eclipse.escet.common.box.Box;
import org.eclipse.escet.common.java.PathPair;

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
    public void write(PlcProject project, PathPair outPaths) {
        ensureDirectory(outPaths);

        // Write configurations.
        for (PlcConfiguration config: project.configurations) {
            write(config, outPaths);
        }

        // Write POUs.
        for (PlcPou pou: project.pous) {
            write(pou, outPaths);
        }

        // Write declared types.
        for (PlcDeclaredType declaredType: project.declaredTypes) {
            writeDeclaredType(declaredType, outPaths);
        }
    }

    /**
     * Writes the given configuration to a file in IEC 61131-3 syntax.
     *
     * @param config The configuration to write.
     * @param outPaths The relative or absolute local file system path and the  absolute local file system path of the
     *     directory that should store the file.
     */
    private void write(PlcConfiguration config, PathPair outPaths) {
        String fileName = config.name + ".plccfg";
        Box code = toBox(config);
        writeFile(code, outPaths, fileName);
    }

    /**
     * Writes the given POU to a file in IEC 61131-3 syntax.
     *
     * @param pou The POU to write.
     * @param outPaths The relative or absolute local file system path and the  absolute local file system path of the
     *     directory that should store the file.
     */
    private void write(PlcPou pou, PathPair outPaths) {
        String ext = (pou.retType == null) ? ".plcprog" : ".plcfunc";
        String fileName = pou.name + ext;
        Box code = toBox(pou);
        writeFile(code, outPaths, fileName);
    }

    /**
     * Writes the given declared type to a file in IEC 61131-3 syntax.
     *
     * @param declaredType The declared type to write.
     * @param outPaths The relative or absolute local file system path and the  absolute local file system path of the
     *     directory that should store the file.
     */
    private void writeDeclaredType(PlcDeclaredType declaredType, PathPair outPaths) {
        String typeName;
        if (declaredType instanceof PlcStructType structType) {
            typeName = structType.typeName;
        } else if (declaredType instanceof PlcEnumType enumType) {
            typeName = enumType.typeName;
        } else {
            throw new AssertionError("Unexpected declared type found: \"" + declaredType + "\".");
        }

        String fileName = typeName + ".plctype";
        Box code = toTypeDeclBox(declaredType);
        writeFile(code, outPaths, fileName);
    }
}
