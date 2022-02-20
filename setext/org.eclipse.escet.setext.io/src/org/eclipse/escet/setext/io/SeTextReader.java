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

package org.eclipse.escet.setext.io;

import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.setext.parser.SeTextParser;
import org.eclipse.escet.setext.parser.ast.Specification;
import org.eclipse.escet.setext.parser.ast.regex.RegEx;
import org.eclipse.escet.setext.runtime.io.BaseReader;
import org.eclipse.escet.setext.typechecker.SeTextTypeChecker;

/** SeText file reader. */
public class SeTextReader
        extends BaseReader<SeTextReader, Specification, Specification, SeTextParser, SeTextTypeChecker>
{
    @Override
    protected SeTextParser createParser() {
        return new SeTextParser();
    }

    @Override
    protected SeTextTypeChecker createTypeChecker() {
        return new SeTextTypeChecker();
    }

    @Override
    protected String getLangName() {
        return "SeText";
    }

    /**
     * Parse a regular expression. The regular expression must not contain any shortcuts. Used for unit testing only.
     *
     * <p>
     * This method allows regular expressions that accept the empty string.
     * </p>
     *
     * @param regEx The regular expression in textual form.
     * @return The regular expression tree.
     */
    public static RegEx parseRegEx(String regEx) {
        String path = "/dummy.setext";
        String txt = "@scanner SomeScannerClass;\n\n" + fmt("@terminals: \"%s\"; @eof; end", regEx);
        Specification spec;
        SeTextTypeChecker.skipEmptyTermCheck = true;
        spec = new SeTextReader().init(path, path, false).read(txt);
        SeTextTypeChecker.skipEmptyTermCheck = false;
        Assert.check(spec.terminals.size() == 2);
        Assert.check(spec.terminals.get(1).isEof());
        return spec.terminals.get(0).regEx;
    }

    /**
     * Parse a regular expression with shortcuts. Used for unit testing only.
     *
     * <p>
     * This method allows regular expressions that accept the empty string.
     * </p>
     *
     * @param regEx The regular expression in textual form.
     * @param shortcuts Mapping from shortcut names to shortcut regular expression texts.
     * @return The regular expression tree.
     */
    public static RegEx parseRegEx(String regEx, Map<String, String> shortcuts) {
        String path = "/dummy.setext";
        String txt = "@scanner SomeScannerClass;\n\n@terminals:\n";
        txt += fmt("  \"%s\";\n", regEx);
        txt += "  @eof;\n";
        txt += "end\n";
        for (Entry<String, String> entry: shortcuts.entrySet()) {
            txt += fmt("@shortcut %s = \"%s\";\n", entry.getKey(), entry.getValue());
        }
        Specification spec;
        SeTextTypeChecker.skipEmptyTermCheck = true;
        spec = new SeTextReader().init(path, path, false).read(txt);
        SeTextTypeChecker.skipEmptyTermCheck = false;
        Assert.check(spec.terminals.size() == 2);
        Assert.check(spec.terminals.get(1).isEof());
        return spec.terminals.get(0).regEx;
    }
}
