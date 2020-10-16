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

package org.eclipse.escet.chi.codegen.types;

import static org.eclipse.escet.chi.codegen.Constants.getClassname;
import static org.eclipse.escet.common.java.Assert.fail;
import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.chi.codegen.java.JavaFile;
import org.eclipse.escet.common.java.Assert;

/** Base class for types implemented as Java primitive type. */
public abstract class PrimitiveTypeID extends TypeID {
    /**
     * Constructor for the {@link PrimitiveTypeID} class.
     *
     * @param kind Type kind of the type.
     */
    public PrimitiveTypeID(TypeKind kind) {
        super(false, kind);
    }

    @Override
    public boolean hasDeepCopy() {
        return false;
    }

    @Override
    public String getDeepCopyName(String val, JavaFile jfc, boolean doDeep) {
        fail("getDeepCopyName() should not be accessed.");
        return null;
    }

    @Override
    public String getReadName(String stream, JavaFile jf) {
        Assert.check(isPrintable());

        String funcPath = getStaticReadFuncName();
        jf.addImport(funcPath, true);
        return fmt("%s(%s)", getClassname(funcPath), stream);
    }

    /**
     * Get the static function name of the read function of this type.
     *
     * @return The static function name of the read function of this type.
     */
    protected abstract String getStaticReadFuncName();

    @Override
    public String getEqual(String lhs, String rhs) {
        return "(" + lhs + ") == (" + rhs + ")";
    }

    @Override
    public String getUnequal(String lhs, String rhs) {
        return "(" + lhs + ") != (" + rhs + ")";
    }
}
