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

package org.eclipse.escet.cif.plcgen.generators.io;

import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Arrays;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.eclipse.escet.common.java.Assert;

/** Data of a PLC IO address. */
public class DefaultIoAddress implements IoAddress {
    /** Regular expression to match an address. */
    private static final Pattern ADDRESSPATTERN = Pattern.compile(" *%([IQM])([XBWDL]?)([0-9]+(\\.[0-9]+)*) *");

    /** Type of memory being accessed {@code (I, M, Q)}. */
    public final String memType;

    /** Size of the access {@code (X, <empty>, B, W, D, L)}. */
    public final String sizeType;

    /** Numeric parts of the address, one or more unsigned decimal number texts. */
    public final String[] numericParts;

    /**
     * Constructor of the {@link DefaultIoAddress} class.
     *
     * @param memType Type of memory being accessed {@code (I, M, Q)}.
     * @param sizeType Size of the access {@code (X, <empty>, B, W, D, L)}.
     * @param numericParts Numeric parts of the address, one or more unsigned decimal number texts.
     */
    public DefaultIoAddress(String memType, String sizeType, String[] numericParts) {
        this.memType = memType;
        this.sizeType = sizeType;
        this.numericParts = numericParts;
    }

    @Override
    public boolean supportsInput() {
        return "I".equals(memType) || "M".equals(memType);
    }

    @Override
    public boolean supportsOutput() {
        return "Q".equals(memType) || "M".equals(memType);
    }

    @Override
    public int size() {
        return switch (sizeType) {
            case "X", "" -> 1;
            case "B" -> 8;
            case "W" -> 16;
            case "D" -> 32;
            case "L" -> 64;
            default -> throw new AssertionError("Unknown size type: " + sizeType);
        };
    }

    /**
     * Parse the supplied address text, and convert it into a {@link DefaultIoAddress} instance.
     *
     * @param addressText Text to parse.
     * @return The converted address or {@code null} if the text cannot be parsed.
     */
    public static DefaultIoAddress parseAddress(String addressText) {
        addressText = addressText.toUpperCase(Locale.US); // Enforce upper-case.

        Matcher matcher = ADDRESSPATTERN.matcher(addressText);
        if (!matcher.matches()) {
            return null;
        }
        String[] numericParts = matcher.group(3).split("\\.");
        return new DefaultIoAddress(matcher.group(1), matcher.group(2), numericParts);
    }

    @Override
    public String getAddress() {
        String numericAddress = String.join(".", numericParts);
        return fmt("%%%s%s%s", memType, sizeType, numericAddress);
    }

    @Override
    public String toString() {
        return getAddress();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof DefaultIoAddress da)) {
            return false;
        }
        return memType.equals(da.memType) && sizeType.equals(da.sizeType)
                && Arrays.equals(numericParts, da.numericParts);
    }

    @Override
    public int hashCode() {
        return memType.hashCode() + sizeType.hashCode() + Arrays.hashCode(numericParts);
    }
}
