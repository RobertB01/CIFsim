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

package org.eclipse.escet.tooldef.typechecker;

import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newBoolType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newIntType;

import org.eclipse.escet.common.emf.EMFHelper;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.BoolType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.DoubleType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.IntType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.ListType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.LongType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.MapType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.ObjectType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.SetType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.StringType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.ToolDefType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.TupleType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.TypeParamRef;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.TypeRef;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.UnresolvedType;

/** Type checker for ToolDef types. */
public class TypesChecker {
    /** Non-nullable boolean type. */
    public static final BoolType NON_NULLABLE_BOOL_TYPE = newBoolType(false, null);

    /** Non-nullable boolean type hint. */
    public static final TypeHints NON_NULLABLE_BOOL_HINT = new TypeHints().add(NON_NULLABLE_BOOL_TYPE);

    /** Non-nullable integer type. */
    public static final IntType NON_NULLABLE_INT_TYPE = newIntType(false, null);

    /** Non-nullable integer type hint. */
    public static final TypeHints NON_NULLABLE_INT_HINT = new TypeHints().add(NON_NULLABLE_INT_TYPE);

    /** Constructor for the {@link TypeDeclsChecker} class. */
    private TypesChecker() {
        // Static class.
    }

    /**
     * Type check a ToolDef type.
     *
     * <p>
     * May replace the type in its parent.
     * </p>
     *
     * @param type The ToolDef type.
     * @param ctxt The type checker context.
     */
    public static void tcheck(ToolDefType type, CheckerContext ctxt) {
        // Simple types.
        if (type instanceof BoolType) {
            return;
        } else if (type instanceof DoubleType) {
            return;
        } else if (type instanceof IntType) {
            return;
        } else if (type instanceof LongType) {
            return;
        } else if (type instanceof ObjectType) {
            return;
        } else if (type instanceof StringType) {
            return;
        }

        // Container types.
        if (type instanceof ListType) {
            ListType ltype = (ListType)type;
            tcheck(ltype.getElemType(), ctxt);
            return;
        } else if (type instanceof SetType) {
            SetType stype = (SetType)type;
            tcheck(stype.getElemType(), ctxt);
            return;
        } else if (type instanceof MapType) {
            MapType mtype = (MapType)type;
            tcheck(mtype.getKeyType(), ctxt);
            tcheck(mtype.getValueType(), ctxt);
            return;
        } else if (type instanceof TupleType) {
            TupleType ttype = (TupleType)type;
            Assert.check(ttype.getFields().size() >= 2);
            for (int i = 0; i < ttype.getFields().size(); i++) {
                ToolDefType fieldType = ttype.getFields().get(i);
                tcheck(fieldType, ctxt);
            }
            return;
        }

        // Reference types. Resolve and replace.
        if (type instanceof UnresolvedType) {
            UnresolvedType utype = (UnresolvedType)type;
            Assert.check(!utype.isNullable());
            ToolDefType resolved = ctxt.resolveType(utype.getName(), utype.getPosition());
            EMFHelper.updateParentContainment(type, resolved);
            return;
        }

        // Already resolved types. Should not be present in AST.
        if (type instanceof TypeRef || type instanceof TypeParamRef) {
            throw new RuntimeException("Already resolved ref? " + type);
        }

        // Unknown type.
        throw new RuntimeException("Unknown type: " + type);
    }
}
