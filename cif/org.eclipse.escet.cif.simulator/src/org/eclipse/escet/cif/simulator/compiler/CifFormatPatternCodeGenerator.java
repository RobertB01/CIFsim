//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.simulator.compiler;

import static org.eclipse.escet.cif.common.CifTypeUtils.normalizeType;
import static org.eclipse.escet.common.java.Lists.listc;

import java.util.List;

import org.eclipse.escet.cif.common.CifMath;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.StringType;
import org.eclipse.escet.common.java.FormatDecoder;
import org.eclipse.escet.common.java.FormatDescription;
import org.eclipse.escet.common.java.FormatDescription.Conversion;
import org.eclipse.escet.common.java.Strings;

/**
 * Format pattern code generator.
 *
 * @see CifMath#fmt
 */
public class CifFormatPatternCodeGenerator {
    /** Constructor for the {@link CifFormatPatternCodeGenerator} class. */
    private CifFormatPatternCodeGenerator() {
        // Static class.
    }

    /**
     * Generate a Java code fragment for the format pattern and values.
     *
     * @param pattern The pattern text, without backslash escaping, and with percentage escaping.
     * @param valueTxts The code to use to evaluate the values.
     * @param valueTypes The types of the values.
     * @return The Java code that represents the call to the {@link Strings#fmt} method.
     * @see CifMath#fmt
     */
    public static String gencodePattern(String pattern, List<String> valueTxts, List<CifType> valueTypes) {
        // See also CifMath for similar code.

        // Decode pattern.
        FormatDecoder decoder = new FormatDecoder();
        List<FormatDescription> parts = decoder.decode(pattern);

        // Process all parts.
        StringBuilder patternCode = new StringBuilder();
        List<String> argCodes = listc(parts.size());
        int implicitIndex = 0;
        for (FormatDescription part: parts) {
            // Literal.
            if (part.conversion == Conversion.LITERAL) {
                if (part.text.equals("%")) {
                    patternCode.append("%%");
                } else {
                    patternCode.append(part.text);
                }
                continue;
            }

            // Get 1-based index of specifier.
            int idx;
            if (!part.index.isEmpty()) {
                idx = part.getExplicitIndex() - 1;
            } else {
                idx = implicitIndex;
                implicitIndex++;
            }

            // Get argument code.
            String argCode = valueTxts.get(idx);

            // Add to formatted result.
            switch (part.conversion) {
                case BOOLEAN:
                case INTEGER:
                case REAL: {
                    patternCode.append(part.toString(false));
                    argCodes.add(argCode);
                    break;
                }

                case STRING: {
                    patternCode.append(part.toString(false));
                    CifType t = valueTypes.get(idx);
                    CifType nt = normalizeType(t);
                    if (!(nt instanceof StringType)) {
                        argCode = "runtimeToString(" + argCode + ")";
                    }
                    argCodes.add(argCode);
                    break;
                }

                default:
                    String msg = "Unexpected: " + part.conversion;
                    throw new RuntimeException(msg);
            }
        }

        // Return actual code for the entire 'fmt' function call.
        StringBuilder rslt = new StringBuilder();
        rslt.append("fmt(");
        rslt.append(Strings.stringToJava(patternCode.toString()));
        if (!argCodes.isEmpty()) {
            rslt.append(", ");
        }
        rslt.append(String.join(", ", argCodes));
        rslt.append(")");
        return rslt.toString();
    }
}
