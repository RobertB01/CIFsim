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

package org.eclipse.escet.cif.typechecker;

import static org.eclipse.escet.cif.common.CifTextUtils.typeToStr;
import static org.eclipse.escet.cif.common.CifTypeUtils.normalizeType;

import java.util.List;

import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.cif.metamodel.cif.types.RealType;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.FormatDecoder;
import org.eclipse.escet.common.java.FormatDescription;
import org.eclipse.escet.common.java.FormatDescription.Conversion;
import org.eclipse.escet.common.java.Numbers;
import org.eclipse.escet.common.position.common.PositionUtils;
import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.common.typechecker.SemanticException;

/** CIF format pattern checker. */
public class CifFormatPatternChecker {
    /** Constructor for the {@link CifFormatPatternChecker} class. */
    private CifFormatPatternChecker() {
        // Static class.
    }

    /**
     * Performs type checking on a format pattern.
     *
     * @param tchecker The CIF type checker to use.
     * @param valueTypes The types of the values to use for the format pattern.
     * @param valuePositions The position information of the values to use for the format pattern. Used for reporting
     *     unused values.
     * @param pattern The text of the format pattern, without surrounding double quotes, without backslash escaping, and
     *     with percentage escaping.
     * @param patternPos The position information of the string literal that represents the format pattern.
     */
    public static void checkFormatPattern(CifTypeChecker tchecker, List<CifType> valueTypes,
            List<Position> valuePositions, String pattern, Position patternPos)
    {
        // Precondition checking.
        Assert.check(valueTypes.size() == valuePositions.size());

        // Decode format pattern.
        FormatDecoder decoder = new FormatDecoder();
        List<FormatDescription> parts = decoder.decode(pattern);

        // Handle errors.
        boolean decodingFailed = false;
        for (FormatDescription part: parts) {
            if (part.conversion != Conversion.ERROR) {
                continue;
            }

            decodingFailed = true;
            tchecker.addProblem(ErrMsg.FMT_PAT_DECODE_ERR, createSubPos(patternPos, part), part.text);
            // Non-fatal problem.
        }
        if (decodingFailed) {
            throw new SemanticException();
        }

        // Initialize for unused check.
        int valueCount = valueTypes.size();
        boolean[] used = new boolean[valueCount];

        // Check each specifier.
        int implicitIndex = 0;
        for (FormatDescription part: parts) {
            // Skip non-specifiers.
            if (part.conversion == Conversion.LITERAL) {
                continue;
            }

            // Get 1-based index.
            int idx1;
            if (!part.index.isEmpty()) {
                idx1 = part.getExplicitIndex();
                if (idx1 == -1) {
                    // Integer overflow.
                    tchecker.addProblem(ErrMsg.FMT_PAT_IDX_OVERFLOW, createSubPos(patternPos, part));
                    throw new SemanticException();
                }
            } else {
                implicitIndex++;
                idx1 = implicitIndex;
            }

            // Check 1-based index against available values.
            if (idx1 < 1 || idx1 > valueCount) {
                tchecker.addProblem(ErrMsg.FMT_PAT_IDX_OUT_OF_RANGE, createSubPos(patternPos, part),
                        Numbers.toOrdinal(idx1));
                throw new SemanticException();
            }

            // Set 0-based index and mark it used.
            int idx0 = idx1 - 1;
            used[idx0] = true;

            // Check type, based on type of conversion.
            CifType type = valueTypes.get(idx0);
            CifType ntype = normalizeType(type);

            switch (part.conversion) {
                case BOOLEAN:
                    if (!(ntype instanceof BoolType)) {
                        tchecker.addProblem(ErrMsg.FMT_PAT_WRONG_TYPE, createSubPos(patternPos, part), part.text,
                                "bool", Numbers.toOrdinal(idx1), typeToStr(type));
                        // Non-fatal error.
                    }
                    break;

                case INTEGER:
                    if (!(ntype instanceof IntType)) {
                        tchecker.addProblem(ErrMsg.FMT_PAT_WRONG_TYPE, createSubPos(patternPos, part), part.text, "int",
                                Numbers.toOrdinal(idx1), typeToStr(type));
                        // Non-fatal error.
                    }
                    break;

                case REAL: {
                    if (!(ntype instanceof RealType)) {
                        tchecker.addProblem(ErrMsg.FMT_PAT_WRONG_TYPE, createSubPos(patternPos, part), part.text,
                                "real", Numbers.toOrdinal(idx1), typeToStr(type));
                        // Non-fatal error.
                    }
                    break;
                }

                case STRING:
                    // All types allowed, except for component (definition).
                    if (CifTypeUtils.hasComponentLikeType(ntype)) {
                        tchecker.addProblem(ErrMsg.FMT_PAT_COMP_TYPE, createSubPos(patternPos, part), part.text,
                                Numbers.toOrdinal(idx1), typeToStr(type));
                        // Non-fatal error.
                    }
                    break;

                case ERROR:
                case LITERAL:
                    String msg = "Unexpected: " + part.conversion;
                    throw new RuntimeException(msg);
            }
        }

        // Unused values?
        for (int i = 0; i < used.length; i++) {
            if (!used[i]) {
                tchecker.addProblem(ErrMsg.FMT_PAT_UNUSED_VALUE, valuePositions.get(i), Numbers.toOrdinal(i + 1));
                // Non-fatal problem.
            }
        }
    }

    /**
     * Creates a sub {@link Position} covering the given format description.
     *
     * @param pos The original position covering the entire string literal of the format pattern.
     * @param fd The format description for which to obtain the sub position.
     * @return The sub position.
     */
    private static Position createSubPos(Position pos, FormatDescription fd) {
        // Increase 'offset' by one, to account for double quote.
        return PositionUtils.getSubRange(pos, fd.offset + 1, fd.length);
    }
}
