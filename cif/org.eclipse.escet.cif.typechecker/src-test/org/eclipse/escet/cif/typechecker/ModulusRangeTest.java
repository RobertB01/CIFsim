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

package org.eclipse.escet.cif.typechecker;

import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newIntType;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.escet.cif.common.CifEvalException;
import org.eclipse.escet.cif.common.CifMath;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.RangeCompat;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.common.box.GridBox;
import org.eclipse.escet.common.box.TextBox;
import org.junit.Test;

/** Modulus binary operator range tests. */
public class ModulusRangeTest {
    /** Whether to debug the unit test. */
    private static final boolean DEBUG = false;

    /** Test modulus. */
    @Test
    @SuppressWarnings("unused")
    public void testModulus() {
        int success = 0;
        int failure = 0;
        for (int l1 = -20; l1 <= 20; l1++) {
            for (int u1 = 20; u1 <= 20; u1++) {
                for (int l2 = -20; l2 <= 20; l2++) {
                    for (int u2 = -20; u2 <= 20; u2++) {
                        // Skip invalid ranges.
                        if (u1 < l1) {
                            continue;
                        }
                        if (u2 < l2) {
                            continue;
                        }

                        // Init result range.
                        int lRslt = Integer.MAX_VALUE;
                        int uRslt = Integer.MIN_VALUE;

                        // Determine result range (experimental).
                        for (int x = l1; x <= u1; x++) {
                            for (int y = l2; y <= u2; y++) {
                                int rslt;
                                try {
                                    rslt = CifMath.mod(x, y, null);
                                } catch (CifEvalException e) {
                                    // Skip divide by zero exceptions, etc.
                                    continue;
                                }
                                lRslt = Math.min(lRslt, rslt);
                                uRslt = Math.max(uRslt, rslt);
                            }
                        }
                        IntType experimentalType = newIntType();
                        experimentalType.setLower(lRslt);
                        experimentalType.setUpper(uRslt);

                        // Determine result range (formula).
                        int[] rangeFormula;
                        rangeFormula = CifExprsTypeChecker.getModResultRange(l1, u1, l2, u2);
                        assertEquals(2, rangeFormula.length);
                        IntType formulaType = newIntType();
                        formulaType.setLower(rangeFormula[0]);
                        formulaType.setUpper(rangeFormula[1]);

                        // Success or failure (experimental range in formal
                        // range, thus formula range over-approximation)?
                        boolean match = CifTypeUtils.checkTypeCompat(formulaType, experimentalType,
                                RangeCompat.CONTAINED);
                        if (match) {
                            success++;
                        }
                        if (!match) {
                            failure++;
                        }

                        // Print test case, for debugging.
                        if (DEBUG) {
                            String range1 = fmt("[%d..%d]", l1, u1);
                            String range2 = fmt("[%d..%d]", l2, u2);
                            System.out.format("%s mod %s = [%d..%d] %s= [%d..%d]\n", range1, range2, lRslt, uRslt,
                                    match ? "" : "!", formulaType.getLower(), formulaType.getUpper());
                        }

                        // Check ranges.
                        if (!DEBUG) {
                            assertTrue(match);
                        }
                    }
                }
            }
        }

        if (DEBUG) {
            System.out.println("success: " + success);
            System.out.println("failure: " + failure);
        }
    }

    /** Prints modulus grid. Can be enabled for debugging the test. */
    // @Test
    public void printModulusGrid() {
        GridBox box = new GridBox(13, 13);
        for (int x = -5; x <= 5; x++) {
            box.set(0, x + 7, new TextBox(fmt("%3d", x)));
            box.set(1, x + 7, new TextBox("------"));
            for (int y = -5; y <= 5; y++) {
                box.set(y + 7, 0, new TextBox(fmt("%3d", y)));
                box.set(y + 7, 1, new TextBox(" | "));

                if (x == 0) {
                    box.set(y + 7, x + 7, new TextBox("err"));
                    continue;
                }

                int r;
                try {
                    r = CifMath.mod(y, x, null);
                } catch (CifEvalException e) {
                    throw new RuntimeException(e);
                }
                box.set(y + 7, x + 7, new TextBox(fmt("%3d", r)));
            }
        }
        System.out.println(box.toString());
    }
}
