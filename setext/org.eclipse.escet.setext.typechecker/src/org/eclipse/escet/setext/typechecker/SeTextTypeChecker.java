//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.setext.typechecker;

import static org.eclipse.escet.common.java.Lists.filter;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.list2set;
import static org.eclipse.escet.common.java.Sets.set;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.apache.commons.lang.StringUtils;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Strings;
import org.eclipse.escet.common.position.common.PositionUtils;
import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.common.typechecker.SemanticException;
import org.eclipse.escet.common.typechecker.TypeChecker;
import org.eclipse.escet.setext.parser.ast.Decl;
import org.eclipse.escet.setext.parser.ast.HooksDecl;
import org.eclipse.escet.setext.parser.ast.SeTextObject;
import org.eclipse.escet.setext.parser.ast.Specification;
import org.eclipse.escet.setext.parser.ast.Symbol;
import org.eclipse.escet.setext.parser.ast.parser.ImportDecl;
import org.eclipse.escet.setext.parser.ast.parser.JavaType;
import org.eclipse.escet.setext.parser.ast.parser.NonTerminal;
import org.eclipse.escet.setext.parser.ast.parser.ParserRule;
import org.eclipse.escet.setext.parser.ast.parser.ParserRulePart;
import org.eclipse.escet.setext.parser.ast.parser.StartSymbol;
import org.eclipse.escet.setext.parser.ast.regex.RegEx;
import org.eclipse.escet.setext.parser.ast.regex.RegExAlts;
import org.eclipse.escet.setext.parser.ast.regex.RegExChar;
import org.eclipse.escet.setext.parser.ast.regex.RegExCharClass;
import org.eclipse.escet.setext.parser.ast.regex.RegExCharSeq;
import org.eclipse.escet.setext.parser.ast.regex.RegExChars;
import org.eclipse.escet.setext.parser.ast.regex.RegExDot;
import org.eclipse.escet.setext.parser.ast.regex.RegExOpt;
import org.eclipse.escet.setext.parser.ast.regex.RegExPlus;
import org.eclipse.escet.setext.parser.ast.regex.RegExSeq;
import org.eclipse.escet.setext.parser.ast.regex.RegExShortcut;
import org.eclipse.escet.setext.parser.ast.regex.RegExStar;
import org.eclipse.escet.setext.parser.ast.scanner.KeywordsIdentifier;
import org.eclipse.escet.setext.parser.ast.scanner.KeywordsTerminal;
import org.eclipse.escet.setext.parser.ast.scanner.ScannerDecl;
import org.eclipse.escet.setext.parser.ast.scanner.ShortcutDecl;
import org.eclipse.escet.setext.parser.ast.scanner.Terminal;
import org.eclipse.escet.setext.parser.ast.scanner.TerminalsDecl;

/** SeText type checker. */
public class SeTextTypeChecker extends TypeChecker<Specification, Specification> {
    /** The name of the Token class from the SeText run-time. */
    private static final String TOKEN_CLASS_NAME = "org.eclipse.escet.setext.runtime.Token";

    /**
     * Whether to skip the empty terminals check. Used to allow regular expression that accept the empty string, for
     * unit tests.
     */
    public static boolean skipEmptyTermCheck = false;

    /** Symbol table. */
    private SymbolTable symtable = new SymbolTable(this);

    /** The specification being type checked. */
    private Specification spec = null;

    /**
     * Adds a semantic problem to the list of problems found so far.
     *
     * @param message The SeText type checker problem message describing the semantic problem.
     * @param position Position information.
     * @param args The arguments to use when formatting the problem message.
     */
    public void addProblem(Message message, Position position, String... args) {
        addProblem(message.format(args), message.getSeverity(), position);
    }

    @Override
    protected Specification transRoot(Specification spec) {
        // Store specification for global access by the type checker.
        this.spec = spec;

        // Type check terminal shortcuts.
        tcheckTerminalShortcuts();

        // Type check terminals.
        spec.states = set();
        spec.states.add("");
        spec.terminals = list();
        spec.nonterminals = list();
        tcheckTerminals();

        // Check for unused shortcuts.
        checkUnusedShortcuts();

        // Check scanner states.
        checkScannerStates();

        // Type check imports.
        tcheckImports();

        // Type check non-terminals.
        tcheckNonTerminals();

        // Type check start symbols (incl. main).
        spec.mainSymbols = list();
        spec.startSymbols = list();
        tcheckStartSymbols();

        // Remove unused generated non-terminals.
        removeUnusedGeneratedNonTerminals();

        // Check for unused symbols.
        checkUnusedSymbols();

        // Check for unreachable non-terminals (from any main/start symbol).
        checkUnreachableNonTerminals();

        // Check main symbols.
        checkMainSymbols();

        // Type check hooks class.
        tcheckHooksClass();

        // Type check scanner class.
        tcheckScannerClass();

        // Check for unused imports.
        checkUnusedImports();

        // Check for duplicate generated classes.
        checkDuplicateGenClasses();

        // Return type checked specification.
        return spec;
    }

    /** Type checks the terminal shortcuts of the specification. */
    private void tcheckTerminalShortcuts() {
        for (Decl decl: spec.decls) {
            // Skip non-shortcut declarations.
            if (!(decl instanceof ShortcutDecl)) {
                continue;
            }

            // Store shortcut in symbol table.
            ShortcutDecl shortcut = (ShortcutDecl)decl;
            symtable.add(shortcut.name, shortcut);

            // Type check regular expression.
            tcheckRegEx(shortcut.regEx, true);
        }
    }

    /** Type checks the terminals of the specification. */
    private void tcheckTerminals() {
        int priority = 0;
        for (Decl decl: spec.decls) {
            // Skip non-terminal declarations.
            if (!(decl instanceof TerminalsDecl)) {
                continue;
            }

            // Process each of the terminals.
            TerminalsDecl terminals = (TerminalsDecl)decl;
            for (Symbol terminal: terminals.terminals) {
                if (terminal instanceof Terminal) {
                    tcheckTerminal((Terminal)terminal, priority);
                } else if (terminal instanceof KeywordsTerminal) {
                    tcheckKeywordsTerminal((KeywordsTerminal)terminal, priority);
                } else {
                    throw new RuntimeException("Unknown terminal.");
                }
            }

            // Increase priority for next set of terminals.
            priority++;
        }
    }

    /**
     * Type checks a regular expression terminal.
     *
     * @param terminal The regular expression terminal to check.
     * @param priority The priority of the terminal.
     */
    private void tcheckTerminal(Terminal terminal, int priority) {
        // Set priority.
        terminal.priority = priority;

        // Check regular expression.
        tcheckRegEx(terminal.regEx, false);

        // Make sure regular expression does not accept empty string.
        if (terminal.regEx.acceptsEmptyString() && !skipEmptyTermCheck) {
            Position pos = (terminal.name == null) ? terminal.regEx.position : terminal.position;
            String name = (terminal.name == null) ? "" : " \"" + terminal.name + "\"";
            addProblem(Message.TERM_ACCEPTS_EMPTY_STR, pos, name);
        }

        // Add source and target states.
        String src = terminal.getStateName();
        if (!spec.states.contains(src)) {
            spec.states.add(src);
        }

        // Add to symbol table and specification.
        if (terminal.name != null) {
            symtable.add(terminal.name, terminal);
        }
        spec.terminals.add(terminal);

        // Check terminal description.
        tcheckTerminalDescription(terminal);
    }

    /**
     * Type checks keyword terminals.
     *
     * @param kwterminal The keywords terminal to check.
     * @param priority The priority of the terminal.
     */
    private void tcheckKeywordsTerminal(KeywordsTerminal kwterminal, int priority) {
        // Add source state.
        String src = (kwterminal.state == null) ? "" : kwterminal.state.id;
        if (!spec.states.contains(src)) {
            spec.states.add(src);
        }

        // Construct terminals for each of the keywords.
        List<Terminal> keywordTerminals = list();
        for (KeywordsIdentifier keyword: kwterminal.keywords) {
            // Construct terminal.
            String name = keyword.keyword.id.toUpperCase(Locale.US);
            name += "KW";

            RegEx regEx = keywordToRegEx(keyword.keyword.id, keyword.keyword.position);

            Terminal terminal = new Terminal(name, regEx, keyword.func, null, keyword.description, keyword.position);

            // Add terminal.
            symtable.add(name, terminal);
            spec.terminals.add(terminal);
            keywordTerminals.add(terminal);

            // Check terminal description.
            tcheckTerminalDescription(terminal);
        }

        // Create JavaType for non-terminal.
        Position javaTypePos = PositionUtils.copyPosition(kwterminal.position);
        JavaType javaType = new JavaType(TOKEN_CLASS_NAME, null, javaTypePos);

        // Create rules for non-terminal.
        List<ParserRule> rules = list();
        for (Terminal keywordTerminal: keywordTerminals) {
            ParserRulePart part = new ParserRulePart(keywordTerminal.name, true, keywordTerminal.position);
            part.symbol = keywordTerminal;
            rules.add(new ParserRule(list(part)));
        }

        // Create non-terminal.
        NonTerminal nt = new NonTerminal(javaType, kwterminal.name, rules, true, kwterminal.position);

        // Add non-terminal to symbol table and specification.
        symtable.add(nt.name, nt);
        spec.nonterminals.add(nt);
    }

    /**
     * Type checks a terminal description.
     *
     * @param terminal The terminal for which to check the description.
     */
    private void tcheckTerminalDescription(Terminal terminal) {
        // Nameless terminals don't need a description, as they don't end up
        // in the parser. End-of-file terminals have built-in descriptions,
        // and they can't be given a description in SeText specifications.
        // Other terminals need a description if their regular expression is
        // not by itself a description.
        boolean needed = (terminal.name != null) && !terminal.isEof() && !terminal.regEx.isDescriptionText();
        boolean available = terminal.description != null;

        // Warn about mismatch between needing and description and having one.
        if (needed && !available) {
            addProblem(Message.TERM_DESCR_MISSING, terminal.position, terminal.name);
        } else if (!needed && available) {
            if (terminal.name == null) {
                addProblem(Message.TERM_DESCR_UNNECESSARY, terminal.description.position, "nameless terminals");
            } else if (terminal.isEof()) {
                // Can't specify terminal description for @eof in SeText
                // syntax.
                throw new RuntimeException("Terminal description for eof.");
            } else {
                // No need to check for overriding description being the same
                // as the default, as we also warn for overrides.
                String description = terminal.regEx.getDescriptionText();
                Assert.notNull(description);
                addProblem(Message.TERM_DESCR_OVERRIDE, terminal.description.position, terminal.name, description);
            }
        }
    }

    /**
     * Constructs a regular expression for a given keyword.
     *
     * @param keyword The keyword for which to construct a regular expression.
     * @param position The position information for the keyword.
     * @return The regular expression the given keyword.
     */
    private RegEx keywordToRegEx(String keyword, Position position) {
        int[] codePoints = Strings.getCodePoints(keyword);
        List<RegEx> chars = listc(codePoints.length);
        for (int idx = 0; idx < codePoints.length; idx++) {
            int codePoint = codePoints[idx];

            // We currently only support ASCII characters.
            Assert.check(position.getStartLine() == position.getEndLine());
            Position charPos = PositionUtils.copyPosition(position);
            charPos.setStartOffset(charPos.getStartOffset() + idx);
            charPos.setStartColumn(charPos.getStartColumn() + idx);

            chars.add(new RegExChar(codePoint, charPos));
        }
        if (chars.size() == 1) {
            return first(chars);
        }
        return new RegExSeq(chars);
    }

    /**
     * Type checks a regular expression.
     *
     * @param isShortcut Is this regular expression part of shortcut definition?
     * @param regEx The regular expression to check.
     */
    private void tcheckRegEx(RegEx regEx, boolean isShortcut) {
        if (regEx instanceof RegExAlts) {
            // Just check the children.
            for (RegEx alt: ((RegExAlts)regEx).alts) {
                tcheckRegEx(alt, isShortcut);
            }
        } else if (regEx instanceof RegExCharClass) {
            // Check the children.
            RegExCharClass cls = (RegExCharClass)regEx;
            boolean anyChildEmpty = false;
            for (RegExChars c: cls.chars) {
                tcheckRegEx(c, isShortcut);
                if (c.getCodePoints().isEmpty()) {
                    anyChildEmpty = true;
                }
            }

            // Make sure the class is not empty. Only report this if none of
            // the children is empty, as otherwise it is already reported for
            // the children.
            if (!anyChildEmpty && cls.getCodePoints().isEmpty()) {
                addProblem(Message.CHAR_CLS_EMPTY, cls.position);
            }
        } else if (regEx instanceof RegExChar) {
            RegExChar ch = (RegExChar)regEx;
            if (ch.isEof()) {
                return;
            }

            // We currently only support ASCII characters.
            int codePoint = ch.character;
            boolean isASCII = 0 <= codePoint && codePoint <= 127;
            if (!isASCII) {
                addProblem(Message.UNSUPPORTED_NON_ASCII_CHAR, regEx.position, Strings.codePointToStr(codePoint),
                        String.valueOf(codePoint));
                throw new SemanticException();
            }
        } else if (regEx instanceof RegExCharSeq) {
            // First check the children.
            RegExCharSeq seq = (RegExCharSeq)regEx;
            tcheckRegEx(seq.start, isShortcut);
            tcheckRegEx(seq.end, isShortcut);

            // Check for valid character sequence. We currently only support
            // ASCII characters.
            int start = seq.start.character;
            int end = seq.end.character;
            if (start > end) {
                addProblem(Message.CHAR_SEQ_INVALID, regEx.position,
                        Strings.codePointToStr(start) + "-" + Strings.codePointToStr(end));
            }
            if (start == end) {
                addProblem(Message.CHAR_SEQ_SINGLE, regEx.position,
                        Strings.codePointToStr(start) + "-" + Strings.codePointToStr(end));
            }
        } else if (regEx instanceof RegExDot) {
            // Nothing to check here.
        } else if (regEx instanceof RegExOpt) {
            // Just check the child.
            tcheckRegEx(((RegExOpt)regEx).child, isShortcut);
        } else if (regEx instanceof RegExPlus) {
            // Just check the child.
            tcheckRegEx(((RegExPlus)regEx).child, isShortcut);
        } else if (regEx instanceof RegExSeq) {
            // Just check the children.
            for (RegEx re: ((RegExSeq)regEx).sequence) {
                tcheckRegEx(re, isShortcut);
            }
        } else if (regEx instanceof RegExShortcut) {
            // Try to obtain shortcut from symbol table.
            RegExShortcut shortcut = (RegExShortcut)regEx;

            if (!symtable.contains(shortcut.name)) {
                addProblem(isShortcut ? Message.SHORTCUT_UNDEFINED_ORDER : Message.SHORTCUT_UNDEFINED, regEx.position,
                        shortcut.name);
                throw new SemanticException();
            }
            SeTextObject obj = symtable.get(shortcut.name);
            if (!(obj instanceof ShortcutDecl)) {
                addProblem(Message.SHORTCUT_INVALID_REF, regEx.position, shortcut.name);
                throw new SemanticException();
            }

            // Store resolved shortcut, and mark it as used.
            shortcut.shortcut = (ShortcutDecl)obj;
            shortcut.shortcut.used = true;
        } else if (regEx instanceof RegExStar) {
            // Just check the child.
            tcheckRegEx(((RegExStar)regEx).child, isShortcut);
        } else {
            throw new RuntimeException("Unknown reg ex: " + regEx);
        }
    }

    /** Checks the shortcut declarations for unused shortcuts. */
    private void checkUnusedShortcuts() {
        for (Decl decl: spec.decls) {
            // Skip non-shortcut declarations.
            if (!(decl instanceof ShortcutDecl)) {
                continue;
            }

            // Check shortcut.
            ShortcutDecl shortcut = (ShortcutDecl)decl;
            if (!shortcut.used) {
                addProblem(Message.SHORTCUT_UNUSED, decl.position, shortcut.name);
            }
        }
    }

    /** Checks the scanner states. */
    private void checkScannerStates() {
        // Make sure target states actually exist.
        for (Terminal terminal: spec.terminals) {
            if (terminal.newState != null) {
                String dst = terminal.newState.id;
                if (!spec.states.contains(dst)) {
                    addProblem(Message.STATE_DOES_NOT_EXIST, terminal.newState.position, dst);
                }
            }
        }

        // Make sure target states specifications are not superfluous.
        for (Terminal terminal: spec.terminals) {
            if (terminal.newState != null) {
                String src = terminal.getStateName();
                String dst = terminal.newState.id;
                if (src.equals(dst)) {
                    addProblem(Message.STATE_SAME_AS_SOURCE, terminal.newState.position, dst);
                }
            }
        }

        // Make sure each scanner state has at most one EOF acceptance.
        Map<String, Position> eofMap = map();
        for (Terminal terminal: spec.terminals) {
            // Skip non-EOF terminals.
            if (!(terminal.regEx instanceof RegExChar)) {
                continue;
            }
            RegExChar c = (RegExChar)terminal.regEx;
            if (!c.isEof()) {
                continue;
            }

            // Check for duplicate.
            String state = terminal.getStateName();
            if (eofMap.containsKey(state)) {
                addProblem(Message.STATE_DUPL_EOF, terminal.position, state);
                addProblem(Message.STATE_DUPL_EOF, eofMap.get(state), state);
            } else {
                eofMap.put(state, terminal.position);
            }
        }

        // Check EOF acceptance.
        if (eofMap.isEmpty()) {
            // At least one state must have EOF acceptance, to be able to
            // accept any input.
            addProblem(Message.NO_STATE_EOF, spec.position);
        } else if (!eofMap.containsKey("")) {
            // Default state should have EOF acceptance.
            addProblem(Message.DEFAULT_STATE_NO_EOF, spec.position);
        }

        // Check for unreachable scanner states. Note that if regular
        // expressions overlap, this may result in them being unused, and thus
        // in more scanner states being unreachable.
        Set<String> reachableStates = set("");
        for (Terminal terminal: spec.terminals) {
            String state = terminal.getStateName();
            String newState = (terminal.newState == null) ? state : terminal.newState.id;
            if (!state.equals(newState)) {
                reachableStates.add(newState);
            }
        }
        for (String state: spec.states) {
            if (!reachableStates.contains(state)) {
                for (Decl decl: spec.decls) {
                    // Skip all but terminals declarations.
                    if (!(decl instanceof TerminalsDecl)) {
                        continue;
                    }
                    TerminalsDecl tdecl = (TerminalsDecl)decl;

                    // Skip terminals declarations with wrong name.
                    String sname = (tdecl.state == null) ? "" : tdecl.state.id;
                    if (!sname.equals(state)) {
                        continue;
                    }

                    // Add problem.
                    addProblem(Message.STATE_UNREACHABLE, tdecl.position, state);
                }
            }
        }
    }

    /** Type checks imports. */
    private void tcheckImports() {
        // Note that we don't type check the Java type, as we don't allow
        // for imports to be used for other imports. That is, all imports
        // should be specified as fully quantified Java types.

        for (Decl decl: spec.decls) {
            // Skip non-import declarations.
            if (!(decl instanceof ImportDecl)) {
                continue;
            }

            // Get name.
            ImportDecl imp = (ImportDecl)decl;
            if (imp.name == null) {
                imp.name = imp.javaType.className;
                int idx = imp.name.lastIndexOf('.');
                if (idx != -1) {
                    imp.name = imp.name.substring(idx + 1);
                }
            }

            // Store import in symbol table.
            symtable.add(imp.name, imp);
        }
    }

    /** Type checks non-terminals. */
    private void tcheckNonTerminals() {
        // Put non-terminals in symbol table and specification.
        for (Decl decl: spec.decls) {
            // Skip all but non-terminal declarations.
            if (!(decl instanceof NonTerminal)) {
                continue;
            }

            // Add non-terminal.
            NonTerminal nonterm = (NonTerminal)decl;
            symtable.add(nonterm.name, nonterm);
            spec.nonterminals.add(nonterm);
        }

        // Type check all the non-terminals.
        for (NonTerminal nonterm: spec.nonterminals) {
            tcheckNonTerminal(nonterm);
        }
    }

    /**
     * Type checks a non-terminal.
     *
     * @param nonterm The non-terminal to check.
     */
    private void tcheckNonTerminal(NonTerminal nonterm) {
        // Skip type check for generated non-terminals.
        if (nonterm.generated) {
            return;
        }

        // Check return type.
        tcheckJavaType(nonterm.returnType);

        // Check rules.
        for (ParserRule rule: nonterm.rules) {
            for (ParserRulePart part: rule.symbols) {
                tcheckParserRulePart(part);
            }
        }
    }

    /**
     * Type checks a parser rule part.
     *
     * @param part The parser rule part to check.
     */
    private void tcheckParserRulePart(ParserRulePart part) {
        // Resolve symbol.
        if (!symtable.contains(part.name)) {
            addProblem(Message.SYMBOL_UNDEFINED, part.position, part.name);
            return;
        }

        SeTextObject obj = symtable.get(part.name);
        if (!(obj instanceof Symbol)) {
            addProblem(Message.SYMBOL_INVALID_REF, part.position, part.name);
            return;
        }

        Symbol sym = (Symbol)obj;
        Assert.check(!(sym instanceof KeywordsTerminal));

        // Store resolved symbol.
        part.symbol = sym;

        // Check for useless @ annotations.
        if (part.callBackArg && part.symbol instanceof NonTerminal) {
            addProblem(Message.CALLBACK_BY_DEF, part.position, part.name);
        }

        // Make sure all non-terminals are included in the callback.
        if (part.symbol instanceof NonTerminal) {
            part.callBackArg = true;
        }
    }

    /**
     * Type checks a Java type.
     *
     * @param javaType The Java type to check.
     */
    private void tcheckJavaType(JavaType javaType) {
        String className = javaType.className;
        int idx = className.indexOf('.');
        String prefix = (idx == -1) ? className : Strings.slice(className, null, idx);
        if (symtable.contains(prefix)) {
            // We found a matching symbol table entry. Make sure it is an
            // import.
            SeTextObject obj = symtable.get(prefix);
            if (!(obj instanceof ImportDecl)) {
                addProblem(Message.IMPORT_INVALID_REF, javaType.position, prefix);
                throw new SemanticException();
            }
            ImportDecl imp = (ImportDecl)obj;

            // Mark import as used.
            imp.used = true;

            // If import has generic type parameters, it is not a package,
            // and we can't use a relative local name.
            if (imp.javaType.genericTypeParams != null && idx != -1) {
                addProblem(Message.REL_REF_ON_COMPLETE_TYPE, javaType.position, javaType.className, prefix);
            }

            // Import and this Java type may not both define generic type
            // parameters.
            if (imp.javaType.genericTypeParams != null && javaType.genericTypeParams != null) {
                addProblem(Message.DUPL_GENERIC_TYPE_PARAMS, javaType.position, prefix);
            }

            // Copy over generic type parameters from import.
            if (imp.javaType.genericTypeParams != null && javaType.genericTypeParams == null) {
                javaType.genericTypeParams = imp.javaType.genericTypeParams;
            }

            // Combine class names.
            javaType.className = imp.javaType.className + Strings.slice(javaType.className, prefix.length(), null);
        } // else: fully quantified name.

        // Type check the generic type parameters.
        if (javaType.genericTypeParams != null) {
            for (JavaType param: javaType.genericTypeParams) {
                tcheckJavaType(param);
            }
        }
    }

    /** Type checks start symbols (incl. main). */
    private void tcheckStartSymbols() {
        int cnt = 0;
        Map<NonTerminal, Position> startNonTerms = map();

        for (Decl decl: spec.decls) {
            // Skip non start symbol declarations.
            if (!(decl instanceof StartSymbol)) {
                continue;
            }

            // Processed a start symbol.
            cnt++;

            // Resolve non-terminal.
            StartSymbol start = (StartSymbol)decl;
            if (!symtable.contains(start.name.id)) {
                addProblem(Message.NONTERM_UNDEFINED, start.name.position, start.name.id);
                throw new SemanticException();
            }

            SeTextObject obj = symtable.get(start.name.id);
            if (!(obj instanceof NonTerminal)) {
                addProblem(Message.NONTERM_INVALID_REF, start.name.position, start.name.id);
                throw new SemanticException();
            }

            // Store resolved non-terminal.
            NonTerminal symbol = (NonTerminal)obj;
            start.symbol = symbol;

            // Type check Java type.
            tcheckJavaType(start.javaType);

            // Store start symbol.
            if (start.main) {
                spec.mainSymbols.add(start);
            } else {
                spec.startSymbols.add(start);
            }

            // Check for non-generic parser class.
            if (start.javaType.genericTypeParams != null) {
                addProblem(Message.PARSER_CLASS_GENERIC, start.javaType.position, start.javaType.toString(),
                        start.getStartType(), start.name.id);
            }

            // Check for unique start/main non-terminals.
            if (startNonTerms.containsKey(symbol)) {
                addProblem(Message.START_DUPL, start.position, symbol.name);
                addProblem(Message.START_DUPL, startNonTerms.get(symbol), symbol.name);
            } else {
                startNonTerms.put(symbol, start.position);
            }
        }

        // We should have at least one start symbol. To avoid errors in
        // empty specifications, we check this only if we have at least one
        // non-terminal specified.
        if (cnt == 0) {
            for (NonTerminal nonterm: spec.nonterminals) {
                if (!nonterm.generated) {
                    addProblem(Message.NO_START_SYMBOL, spec.position);
                    break;
                }
            }
        }
    }

    /** Checks the import declarations for unused imports. */
    private void checkUnusedImports() {
        for (Decl decl: spec.decls) {
            // Skip non-import declarations.
            if (!(decl instanceof ImportDecl)) {
                continue;
            }

            // Check import.
            ImportDecl imp = (ImportDecl)decl;
            if (!imp.used) {
                addProblem(Message.IMPORT_UNUSED, decl.position, imp.name);
            }
        }
    }

    /** Removes unused generated non-terminals. */
    private void removeUnusedGeneratedNonTerminals() {
        // Check parser rules for unused non-terminals (regardless of start
        // symbol).
        Set<NonTerminal> usedNonTerminals = set();
        for (NonTerminal nonterm: spec.nonterminals) {
            for (ParserRule rule: nonterm.rules) {
                for (ParserRulePart part: rule.symbols) {
                    if (part.symbol instanceof NonTerminal) {
                        usedNonTerminals.add((NonTerminal)part.symbol);
                    }
                }
            }
        }
        for (StartSymbol start: spec.mainSymbols) {
            usedNonTerminals.add(start.symbol);
        }
        for (StartSymbol start: spec.startSymbols) {
            usedNonTerminals.add(start.symbol);
        }

        // Get unused symbols (regardless of start symbol).
        Set<NonTerminal> unusedNonTerminals = list2set(spec.nonterminals);
        unusedNonTerminals.removeAll(usedNonTerminals);

        // Remove unused generated non-terminals.
        Iterator<NonTerminal> iter = spec.nonterminals.iterator();
        while (iter.hasNext()) {
            NonTerminal nonterm = iter.next();
            if (!nonterm.generated) {
                continue;
            }
            if (!unusedNonTerminals.contains(nonterm)) {
                continue;
            }
            iter.remove();
        }
    }

    /**
     * Checks for unused symbols (regardless of start symbol). Checks for both terminals and non-terminals. Symbols
     * referenced in parser rules are considered used, as are the main/start non-terminals.
     */
    private void checkUnusedSymbols() {
        // Check parser rules for used symbols.
        Set<Terminal> usedTerminals = set();
        Set<NonTerminal> usedNonTerminals = set();
        for (NonTerminal nonterm: spec.nonterminals) {
            for (ParserRule rule: nonterm.rules) {
                for (ParserRulePart part: rule.symbols) {
                    Symbol symbol = part.symbol;
                    if (symbol == null) {
                        // Skip parts for which scope resolving failed. Since
                        // they don't refer to existing symbols, it won't
                        // affect the unused check anyway.
                        continue;
                    } else if (symbol instanceof Terminal) {
                        usedTerminals.add((Terminal)symbol);
                    } else {
                        Assert.check(symbol instanceof NonTerminal);
                        usedNonTerminals.add((NonTerminal)symbol);
                    }
                }
            }
        }

        // The main/start symbols are used as well.
        for (StartSymbol start: spec.mainSymbols) {
            usedNonTerminals.add(start.symbol);
        }
        for (StartSymbol start: spec.startSymbols) {
            usedNonTerminals.add(start.symbol);
        }

        // Get unused symbols.
        Set<Terminal> unusedTerminals = list2set(spec.terminals);
        Set<NonTerminal> unusedNonTerminals = list2set(spec.nonterminals);
        unusedTerminals.removeAll(usedTerminals);
        unusedNonTerminals.removeAll(usedNonTerminals);

        // Report unused symbols.
        if (!spec.nonterminals.isEmpty()) {
            for (Terminal terminal: unusedTerminals) {
                if (terminal.name == null) {
                    continue;
                }
                addProblem(Message.TERM_UNUSED, terminal.position, terminal.name);
            }
        }
        for (NonTerminal nonterminal: unusedNonTerminals) {
            Assert.notNull(nonterminal.name);
            addProblem(Message.NONTERM_UNUSED, nonterminal.position, nonterminal.name);
        }
    }

    /** Check for unreachable non-terminals (from any main/start symbol). */
    private void checkUnreachableNonTerminals() {
        Set<NonTerminal> unreachables = list2set(spec.nonterminals);
        for (StartSymbol start: spec.getStartSymbols()) {
            Set<NonTerminal> reachables = getReachableNonTerms(start.symbol);
            unreachables.removeAll(reachables);
        }
        for (NonTerminal unreachable: unreachables) {
            addProblem(Message.NONTERM_UNREACHABLE, unreachable.position, unreachable.name);
        }
    }

    /** Check main symbols for unreachable non-terminals. */
    private void checkMainSymbols() {
        for (StartSymbol main: spec.mainSymbols) {
            Set<NonTerminal> reachables = getReachableNonTerms(main.symbol);
            Set<NonTerminal> unreachables = list2set(spec.nonterminals);
            unreachables.removeAll(reachables);
            if (!unreachables.isEmpty()) {
                List<String> names = list();
                for (NonTerminal unreachable: unreachables) {
                    names.add(unreachable.name);
                }
                Collections.sort(names, Strings.SORTER);
                addProblem(Message.MAIN_UNREACHABLES, main.position, main.name.id, StringUtils.join(names, ", "));
            }
        }
    }

    /**
     * Returns the non-terminals reachable from the given start symbol.
     *
     * @param start The start symbol.
     * @return The non-terminals reachable from the given start symbol, including the start symbol itself.
     */
    public static Set<NonTerminal> getReachableNonTerms(NonTerminal start) {
        // Initially, start symbol is reachable, and unprocessed.
        Set<NonTerminal> rslt = set(start);
        Stack<NonTerminal> unprocesseds = new Stack<>();
        unprocesseds.add(start);

        // As long as there is more work, process it.
        while (!unprocesseds.isEmpty()) {
            // Get next non-terminal to process, and mark it as reachable.
            NonTerminal todo = unprocesseds.pop();
            rslt.add(todo);

            // Get all non-terminals from the rule parts.
            Set<NonTerminal> candidates = set();
            for (ParserRule rule: todo.rules) {
                for (ParserRulePart part: rule.symbols) {
                    // Note that we skip parts for which scope resolving
                    // failed. This is allowed, since they don't refer to
                    // existing non-terminals.
                    if (part.symbol instanceof NonTerminal) {
                        candidates.add((NonTerminal)part.symbol);
                    }
                }
            }

            // Add the candidates that we didn't encounter before.
            for (NonTerminal candidate: candidates) {
                if (rslt.contains(candidate)) {
                    continue;
                }
                if (unprocesseds.contains(candidate)) {
                    continue;
                }
                unprocesseds.add(candidate);
            }
        }

        // Return the reachable non-terminals.
        return rslt;
    }

    /** Type checks the hooks class. */
    private void tcheckHooksClass() {
        // Get hooks declarations.
        List<HooksDecl> hooksDecls = filter(spec.decls, HooksDecl.class);

        // Check for duplicate hooks decls.
        if (hooksDecls.size() > 1) {
            for (HooksDecl hooksDecl: hooksDecls) {
                addProblem(Message.HOOKS_DECL_DUPL, hooksDecl.position);
            }
            throw new SemanticException();
        }

        // Make sure at least one hooks decl (if needed).
        boolean needed = false;
        for (Terminal terminal: spec.terminals) {
            if (terminal.func != null) {
                needed = true;
                break;
            }
        }
        if (!needed) {
            for (NonTerminal nonterminal: spec.nonterminals) {
                if (!nonterminal.generated) {
                    needed = true;
                    break;
                }
            }
        }
        if (needed && hooksDecls.isEmpty()) {
            addProblem(Message.HOOKS_DECL_MISSING, spec.position);
            throw new SemanticException();
        }

        // If none available, nothing more to check.
        if (hooksDecls.isEmpty()) {
            return;
        }

        // Type check the Java type.
        JavaType hooksClass = first(hooksDecls).hooksClass;
        tcheckJavaType(hooksClass);

        // Make sure the Java type is non-generic.
        if (hooksClass.genericTypeParams != null) {
            addProblem(Message.HOOKS_DECL_GENERIC, hooksClass.position);
        }

        // Store the Java type of the hooks class in the specification.
        spec.hooksClass = hooksClass;
    }

    /** Type checks the scanner class. */
    private void tcheckScannerClass() {
        // Get scanner class declarations.
        List<ScannerDecl> scannerDecls = filter(spec.decls, ScannerDecl.class);

        // Check for duplicate scanner class decls.
        if (scannerDecls.size() > 1) {
            for (ScannerDecl scannerDecl: scannerDecls) {
                addProblem(Message.SCANNER_DECL_DUPL, scannerDecl.position);
            }
            throw new SemanticException();
        }

        // Make sure at least one scanner class decl (if needed).
        boolean needed = !spec.terminals.isEmpty();
        if (needed && scannerDecls.isEmpty()) {
            addProblem(Message.SCANNER_DECL_MISSING, spec.position);
            throw new SemanticException();
        }

        // If none available, nothing more to check.
        if (scannerDecls.isEmpty()) {
            return;
        }

        // Type check the Java type.
        JavaType scannerClass = first(scannerDecls).scannerClass;
        tcheckJavaType(scannerClass);

        // Make sure the Java type is non-generic.
        if (scannerClass.genericTypeParams != null) {
            addProblem(Message.SCANNER_DECL_GENERIC, scannerClass.position);
        }

        // Store the Java type of the scanner class in the specification.
        spec.scannerClass = scannerClass;
    }

    /** Check for duplicate generated scanner/parser classes. */
    private void checkDuplicateGenClasses() {
        // Initialize mapping.
        Map<String, Position> classNames = map();

        // Check parser class names.
        for (StartSymbol start: spec.getStartSymbols()) {
            String className = start.javaType.className;
            if (classNames.containsKey(className)) {
                addProblem(Message.GEN_CLASS_DUPL, start.javaType.position, className);
                addProblem(Message.GEN_CLASS_DUPL, classNames.get(className), className);
            } else {
                classNames.put(className, start.javaType.position);
            }
        }

        // Check scanner class name.
        if (spec.scannerClass != null) {
            String className = spec.scannerClass.className;
            if (classNames.containsKey(className)) {
                addProblem(Message.GEN_CLASS_DUPL, spec.scannerClass.position, className);
                addProblem(Message.GEN_CLASS_DUPL, classNames.get(className), className);
            } else {
                classNames.put(className, spec.scannerClass.position);
            }
        }
    }
}
