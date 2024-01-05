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

package org.eclipse.escet.cif.tests.utils;

import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.FormatDecoder;
import org.eclipse.escet.common.java.FormatDescription;
import org.eclipse.escet.common.java.FormatDescription.Conversion;
import org.eclipse.escet.common.java.ListProductIterator;
import org.eclipse.escet.common.java.Strings;

/** Generator for the CIF simulator 'fmt_all.cif' tests. */
public class CifFormatTestsGenerator {
    /** Constructor for the {@link CifFormatTestsGenerator} class. */
    private CifFormatTestsGenerator() {
        // Static class.
    }

    /**
     * Main method for the {@link CifFormatTestsGenerator}.
     *
     * @param args Command line arguments. Ignored.
     */
    public static void main(String[] args) {
        // Get all flag combinations.
        List<String> minusList = list("", "-");
        List<String> plusList = list("", "+");
        List<String> spaceList = list("", " ");
        List<String> zeroList = list("", "0");
        List<String> commaList = list("", ",");
        List<List<String>> combiList;
        combiList = list(minusList, plusList, spaceList, zeroList, commaList);

        ListProductIterator<String> iter;
        iter = new ListProductIterator<>(combiList);
        List<String> flagsList = list();
        while (iter.hasNext()) {
            List<String> item = iter.next();
            flagsList.add(String.join("", item));
        }

        // Get all format patterns.
        List<String> widthList = list("", "25");
        List<String> precisionList = list("", ".3");
        List<String> conversionList = list("b", "B", "d", "x", "X", "e", "E", "f", "g", "G", "s", "S");
        combiList = list(flagsList, widthList, precisionList, conversionList);

        iter = new ListProductIterator<>(combiList);
        List<String> patternList = list();
        while (iter.hasNext()) {
            List<String> item = iter.next();
            patternList.add("%" + String.join("", item));
        }

        // Remove invalid patterns.
        FormatDecoder decoder = new FormatDecoder();
        List<String> patterns = list();
        for (String pattern: patternList) {
            List<FormatDescription> parts = decoder.decode(pattern);
            Assert.check(parts.size() == 1);
            FormatDescription part = first(parts);
            if (part.conversion == Conversion.ERROR) {
                continue;
            }
            patterns.add("_" + pattern + "_");
        }

        // Generate CIF code for the test.
        for (String pattern: patterns) {
            List<FormatDescription> parts = decoder.decode(pattern);
            Assert.check(parts.size() == 3);
            Assert.check(parts.get(0).conversion == Conversion.LITERAL);
            Assert.check(parts.get(2).conversion == Conversion.LITERAL);
            Conversion conversion = parts.get(1).conversion;
            String value;
            switch (conversion) {
                case BOOLEAN:
                    value = "true";
                    break;
                case INTEGER:
                    value = "12345";
                    break;
                case REAL:
                    value = "1.23456789e9";
                    break;
                case STRING:
                    value = "\"aBcDeFg\"";
                    break;
                default:
                    String msg = "Unexpected conversion: " + conversion;
                    throw new RuntimeException(msg);
            }
//          System.out.format("print fmt(\"%%-35s: %s\", \"fmt(\\\"%s\\\", %s)\", %s);\n",
//                            Strings.escape(pattern), Strings.escape(pattern),
//                            Strings.escape(value), value);
            System.out.format("print fmt(\"fmt(%%-15s %%-15s)= %s\", \"\\\"%s\\\",\", \"%s\", %s);\n",
                    Strings.escape(pattern), Strings.escape(pattern), Strings.escape(value), value);
        }
    }
}
