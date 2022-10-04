//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.plcgen.targets;

import java.util.BitSet;

import org.eclipse.escet.common.java.Assert;

/** Configurable settings guardian. */
public class PlcCodeGenSettings {
    /** Whether the PLC target type supports named constants. */
    private boolean supportsConstants;

    /** Replacement string the CIF input file extension in order to derive an output path. */
    private String outSuffixReplacement;

    /**
     * If not {@code null}, registration which fields have been initialized in the instance, else the fields are
     * available for retrieval.
     */
    private BitSet initialized = new BitSet();

    /**
     * Setter for configuring whether the PLC target type supports named constants.
     *
     * @param value Value to configure.
     */
    public void setSupportconstants(boolean value) {
        supportsConstants = value;
        initialized.set(SettingFields.SUPPORT_CONSTANTS.ordinal());
    }

    /**
     * Getter for obtaining whether the PLC target type supports named constants.
     *
     * @return The requested value.
     */
    public boolean getSupportconstants() {
        Assert.check(initialized == null);
        return supportsConstants;
    }

    /**
     * Setter for configuring whether the PLC target type supports named constants.
     *
     * @param value Value to configure.
     */
    public void setOutSuffixReplacement(String value) {
        outSuffixReplacement = value;
        initialized.set(SettingFields.OUT_SUFFIX_REPLACEMENT.ordinal());
    }

    /**
     * Getter for obtaining whether the PLC target type supports named constants.
     *
     * @return The requested value.
     */
    public String getOutSuffixReplacement() {
        Assert.check(initialized == null);
        return outSuffixReplacement;
    }

    /** Verify that all settings have been set, and make them available for reading. */
    public void lockCodeGenSettings() {
        Assert.check(initialized == null || initialized.cardinality() == SettingFields.values().length);
        initialized = null;
    }

    /** The available settings in the class. */
    private static enum SettingFields {
        /** entry for the {@link PlcCodeGenSettings#supportsConstants} field. */
        SUPPORT_CONSTANTS,

        /** entry for the {@link PlcCodeGenSettings#outSuffixReplacement} field. */
        OUT_SUFFIX_REPLACEMENT;
    }
}
