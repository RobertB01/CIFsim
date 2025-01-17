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

package org.eclipse.escet.cif.simulator.compiler;

import static org.eclipse.escet.cif.common.CifTypeUtils.normalizeType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newStringType;
import static org.eclipse.escet.cif.simulator.compiler.ExprCodeGeneratorResult.merge;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.map;

import java.util.List;
import java.util.Map;

import org.eclipse.escet.cif.common.CifMath;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
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
     * @param valueRslts The code to use to evaluate the values.
     * @param valueTypes The types of the values.
     * @param expr The expression of the code.
     * @param ctxt The compiler context to use.
     * @return The Java code that represents the call to the {@link Strings#fmt} method.
     * @see CifMath#fmt
     */
    public static ExprCodeGeneratorResult gencodePattern(String pattern, List<ExprCodeGeneratorResult> valueRslts,
            List<CifType> valueTypes, Expression expr, CifCompilerContext ctxt)
    {
        // See also CifMath for similar code.

        // Decode pattern.
        FormatDecoder decoder = new FormatDecoder();
        List<FormatDescription> parts = decoder.decode(pattern);

        // Process all parts.
        // - patternCode: The code of the format pattern being generated.
        // - argCodes: The format specifiers to be replaced by the code generated for the arguments.
        // - implicitIndex: The explicit index for the next format specifier with an implicit index.
        // - argIdxToConvertCode: Maps an argument index to 'argument to string' conversion code (if already
        // generated). This mapping is used to prevent generating duplicate code.
        // - argRslts: Code generated for the arguments.
        StringBuilder patternCode = new StringBuilder();
        List<String> argCodes = listc(parts.size());
        int implicitIndex = 0;
        Map<Integer, ExprCodeGeneratorResult> argIdxToConvertCode = map();
        List<ExprCodeGeneratorResult> argRslts = listc(valueRslts.size());
        for (FormatDescription part: parts) {
            // Literal.
            if (part.conversion == Conversion.LITERAL) {
                if (part.text.equals("%")) {
                    patternCode.append("%%%%");
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

            // Add to formatted result.
            ExprCodeGeneratorResult rslt;
            switch (part.conversion) {
                case BOOLEAN:
                case INTEGER:
                case REAL: {
                    patternCode.append(part.toString(false).replace("%", "%%"));
                    argCodes.add("%s");
                    rslt = valueRslts.get(idx);
                    break;
                }

                case STRING: {
                    patternCode.append(part.toString(false).replace("%", "%%"));
                    argCodes.add("%s");
                    CifType t = valueTypes.get(idx);
                    CifType nt = normalizeType(t);
                    if (!(nt instanceof StringType)) {
                        // Convert argument values ourselves, as CIF textual syntax of values differs from Java.
                        // Only generate new argument value conversion code if we don't have it yet.
                        // If we already have it, reuse it to prevent generating duplicate code.
                        rslt = argIdxToConvertCode.get(idx);
                        if (rslt == null) {
                            ExprCodeGeneratorResult valueRslt = valueRslts.get(idx);
                            rslt = merge("runtimeToString(%s)", newStringType(), ctxt, valueRslt);
                            argIdxToConvertCode.put(idx, rslt);
                        }
                    } else {
                        rslt = valueRslts.get(idx);
                    }
                    break;
                }

                default:
                    String msg = "Unexpected: " + part.conversion;
                    throw new RuntimeException(msg);
            }
            argRslts.add(rslt);
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
        return ExprCodeGeneratorResult.merge(rslt.toString(), expr.getType(), ctxt, argRslts);
    }
}
