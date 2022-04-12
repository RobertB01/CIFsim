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

package org.eclipse.escet.cif.texteditor;

import static org.eclipse.escet.common.java.Strings.fmt;

import org.apache.commons.lang3.ArrayUtils;
import org.eclipse.escet.cif.parser.CifScanner;
import org.eclipse.escet.setext.texteditorbase.ColorManager;
import org.eclipse.escet.setext.texteditorbase.RuleBasedScannerEx;
import org.eclipse.escet.setext.texteditorbase.detectors.GenericWhitespaceDetector;
import org.eclipse.escet.setext.texteditorbase.rules.IntNumberRule;
import org.eclipse.escet.setext.texteditorbase.rules.KeywordsRule;
import org.eclipse.escet.setext.texteditorbase.rules.LiteralsRule;
import org.eclipse.escet.setext.texteditorbase.rules.RegExRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.WhitespaceRule;

/** CIF text editor default partition scanner. */
public class CifTextEditorScanner extends RuleBasedScannerEx {
    /**
     * Constructor for the {@link CifTextEditorScanner} class.
     *
     * @param manager The color manager to use to create the color tokens.
     */
    public CifTextEditorScanner(ColorManager manager) {
        // Keywords copied from CIF scanner.
        String[] keywords = CifScanner.getKeywords("Keywords");
        String[] supKinds = CifScanner.getKeywords("SupKind");
        keywords = ArrayUtils.addAll(keywords, supKinds);
        String[] stdlibfuncs = CifScanner.getKeywords("StdLibFunction");
        String[] operators = CifScanner.getKeywords("Operator");

        // Regular expression patterns for identifiers and names.
        String idPat = "[$]?[A-Za-z_][A-Za-z0-9_]*";
        String namePat = fmt("[.^]?%s([.]%s)*", idPat, idPat);

        String cEvtPat = "[$]?c_[A-Za-z0-9_]+";
        String uEvtPat = "[$]?u_[A-Za-z0-9_]+";
        String eEvtPat = "[$]?e_[A-Za-z0-9_]+";

        String cNamePat = fmt("[.^]?(%s[.])*%s", idPat, cEvtPat);
        String uNamePat = fmt("[.^]?(%s[.])*%s", idPat, uEvtPat);
        String eNamePat = fmt("[.^]?(%s[.])*%s", idPat, eEvtPat);

        // Regular expression pattern for reals. Note that for the second line,
        // if we switch the alternatives, we get into trouble, as anything
        // that matches "\\.[0-9]+" will go into that alternative (which is
        // then first), and not into the other alternative, even though it
        // could have a longer match.
        String realPat = "(0|[1-9][0-9]*)((\\.[0-9]+)?[eE][\\-\\+]?[0-9]+|\\.[0-9]+)";

        // Literals:
        // - Literal '..' text. Recognized to avoid '..true' to be recognized
        // as '.' (default style) and '.true' (name, identifier style).
        String[] literals = {".."};

        // Construct and set predicate rules. Make sure we also have a default
        // token.
        IRule[] rules = new IRule[] { //
                new KeywordsRule(keywords, CifStyles.KEYWORD.createToken(manager)), //
                new KeywordsRule(stdlibfuncs, CifStyles.STDLIBFUNC.createToken(manager)), //
                new KeywordsRule(operators, CifStyles.OPERATOR.createToken(manager)), //
                new LiteralsRule(literals, CifStyles.DEFAULT.createToken(manager)), //
                new RegExRule(uNamePat, CifStyles.U_EVENT.createToken(manager)), //
                new RegExRule(cNamePat, CifStyles.C_EVENT.createToken(manager)), //
                new RegExRule(eNamePat, CifStyles.E_EVENT.createToken(manager)), //
                new RegExRule(namePat, CifStyles.IDENTIFIER.createToken(manager)), //
                new RegExRule(realPat, CifStyles.NUMBER.createToken(manager)), //
                new IntNumberRule(CifStyles.NUMBER.createToken(manager)), //
                new WhitespaceRule(new GenericWhitespaceDetector()), //
        };
        setRules(rules);

        setDefaultReturnToken(CifStyles.DEFAULT.createToken(manager));
    }
}
