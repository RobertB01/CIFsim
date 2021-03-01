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

package org.eclipse.escet.chi.codegen.types;

import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;

import org.eclipse.escet.chi.codegen.java.JavaFile;
import org.eclipse.escet.common.java.Assert;

/** Base class for types implemented as Java class. */
public abstract class ObjectTypeID extends TypeID {
    /**
     * Constructor for an elementary type.
     *
     * @param needsCoordinator Whether the type needs a chiCoordinator for construction.
     * @param kind Type kind of the type.
     */
    public ObjectTypeID(boolean needsCoordinator, TypeKind kind) {
        super(needsCoordinator, kind);
    }

    /**
     * Constructor for a container type.
     *
     * @param needsCoordinator Whether the type needs a chiCoordinator for construction.
     * @param kind Type kind of the type.
     * @param subTypes Sub-types of the type.
     */
    public ObjectTypeID(boolean needsCoordinator, TypeKind kind, List<TypeID> subTypes) {
        super(needsCoordinator, kind, subTypes);
    }

    @Override
    public String getJavaType() {
        return getJavaClassType();
    }

    @Override
    public String getSimplestJavaValue() {
        return "null";
    }

    @Override
    public String getReadName(String stream, JavaFile jf) {
        Assert.check(isPrintable());

        String classPath = getJavaClassType();
        int idx = classPath.lastIndexOf('.');
        if (idx != -1) {
            jf.addImport(classPath + ".read", true);
        }
        return fmt("%s.read(chiCoordinator, %s)", classPath, stream);
    }

    @Override
    public String getWriteName(String stream, String val, JavaFile jf) {
        Assert.check(isPrintable());

        return fmt("(%s).write(%s);", val, stream);
    }

    @Override
    public String getToString(String val, JavaFile jf) {
        Assert.check(isPrintable());

        return fmt("(%s).toString()", val);
    }

    @Override
    public String getHashCodeName(String val, JavaFile jf) {
        return fmt("(%s).hashCode()", val);
    }

    @Override
    public String getEqual(String lhs, String rhs) {
        return fmt("(%s).equals(%s)", lhs, rhs);
    }

    @Override
    public String getUnequal(String lhs, String rhs) {
        return fmt("!(%s).equals(%s)", lhs, rhs);
    }

    @Override
    public String getEmptyValue(JavaFile jf) {
        String classPath = getJavaClassType();
        int idx = classPath.lastIndexOf('.');
        if (idx != -1) {
            jf.addImport(classPath, false);
            classPath = classPath.substring(idx + 1);
        }
        if (needsCoordinator) {
            return "new " + classPath + "(chiCoordinator)";
        } else {
            return "new " + classPath + "()";
        }
    }
}
