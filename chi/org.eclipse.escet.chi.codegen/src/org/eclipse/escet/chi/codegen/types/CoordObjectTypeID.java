//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.chi.codegen.types;

import java.util.List;

import org.eclipse.escet.chi.codegen.java.JavaFile;

/** Base class for a type that takes the coordinator as parameter in the constructor. */
public abstract class CoordObjectTypeID extends ObjectTypeID {
    /**
     * Constructor for an elementary type.
     *
     * @param kind Type kind of the type.
     */
    public CoordObjectTypeID(TypeKind kind) {
        super(true, kind);
    }

    /**
     * Constructor for an elementary type.
     *
     * @param kind Type kind of the type.
     * @param subTypes Sub-types of the type.
     */
    public CoordObjectTypeID(TypeKind kind, List<TypeID> subTypes) {
        super(true, kind, subTypes);
    }

    @Override
    public boolean hasDeepCopy() {
        return true;
    }

    @Override
    public String getDeepCopyName(String val, JavaFile jf, boolean doDeep) {
        String classPath = getJavaClassType();
        int idx = classPath.lastIndexOf('.');
        if (idx != -1) {
            jf.addImport(classPath, false);
            classPath = classPath.substring(idx + 1);
        }
        String deepFlag = doDeep ? "true" : "false";
        return "new " + classPath + "(" + val + ", " + deepFlag + ")";
    }

    @Override
    public String getEmptyValue(JavaFile jf) {
        String classPath = getJavaClassType();
        int idx = classPath.lastIndexOf('.');
        if (idx != -1) {
            jf.addImport(classPath, false);
            classPath = classPath.substring(idx + 1);
        }
        return "new " + classPath + "(chiCoordinator)";
    }
}
