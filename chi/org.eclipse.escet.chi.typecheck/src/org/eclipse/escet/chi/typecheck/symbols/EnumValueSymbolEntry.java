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

package org.eclipse.escet.chi.typecheck.symbols;

import org.eclipse.escet.chi.metamodel.chi.EnumValue;
import org.eclipse.escet.chi.typecheck.CheckContext;
import org.eclipse.escet.chi.typecheck.Message;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.Position;

/** Symbol storage of an enumeration value. */
public class EnumValueSymbolEntry extends SymbolEntry {
    /** Type of the enumeration value. */
    public final TypeSymbolEntry enumDeclSymbol;

    /** Enumeration data value itself. */
    private EnumValue enumValue;

    /**
     * Constructor of the {@link EnumValueSymbolEntry} class.
     *
     * @param enumDeclSymbol Enumeration declaration Symbol containing the value.
     * @param enumVal Enumeration value associated with the symbol.
     * @param ctxt Type checker context for checking the symbol.
     */
    public EnumValueSymbolEntry(TypeSymbolEntry enumDeclSymbol, EnumValue enumVal, CheckContext ctxt) {
        super(true, TypeCheckState.FULL_CHECK_DONE, ctxt);
        this.enumDeclSymbol = enumDeclSymbol;
        this.enumValue = enumVal;
    }

    @Override
    public String getName() {
        return enumValue.getName();
    }

    @Override
    public Position getPosition() {
        return enumValue.getPosition();
    }

    @Override
    public void typeCheckForUse() {
        // Already checked.
    }

    @Override
    public void fullTypeCheck() {
        // Already checked.
    }

    @Override
    public void checkUsage(CheckContext ctxt) {
        if (isUsed) {
            return;
        }
        ctxt.addWarning(Message.UNUSED_ENUM_VALUE, getPosition(), getName());
    }

    /**
     * Retrieve the type-checked enumeration value.
     *
     * @return The type-checked enumeration value.
     */
    public EnumValue getEnumValue() {
        Assert.check(checkState == TypeCheckState.FULL_CHECK_DONE);
        Assert.notNull(enumValue);
        return enumValue;
    }
}
