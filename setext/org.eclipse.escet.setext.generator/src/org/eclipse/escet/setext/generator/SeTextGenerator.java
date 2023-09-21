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

package org.eclipse.escet.setext.generator;

import static org.eclipse.escet.common.app.framework.output.OutputProvider.out;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.warn;
import static org.eclipse.escet.common.java.Lists.copy;
import static org.eclipse.escet.common.java.Lists.filter;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.last;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Lists.set2list;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Sets.sortedgeneric;
import static org.eclipse.escet.common.java.Sets.sortedstrings;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.str;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.exceptions.InputOutputException;
import org.eclipse.escet.common.app.framework.exceptions.InvalidInputException;
import org.eclipse.escet.common.app.framework.exceptions.UnsupportedException;
import org.eclipse.escet.common.app.framework.io.AppStream;
import org.eclipse.escet.common.app.framework.io.FileAppStream;
import org.eclipse.escet.common.app.framework.io.NullAppStream;
import org.eclipse.escet.common.app.framework.options.InputFileOption;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.box.MemoryCodeBox;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.JavaCodeUtils;
import org.eclipse.escet.common.java.Pair;
import org.eclipse.escet.common.java.Strings;
import org.eclipse.escet.setext.generator.parser.EmptySymbol;
import org.eclipse.escet.setext.generator.parser.GrammarItem;
import org.eclipse.escet.setext.generator.parser.LALR1Automaton;
import org.eclipse.escet.setext.generator.parser.LALR1AutomatonState;
import org.eclipse.escet.setext.generator.parser.LALR1ParserGenerator;
import org.eclipse.escet.setext.generator.parser.LookaheadItem;
import org.eclipse.escet.setext.generator.parser.ParserAcceptAction;
import org.eclipse.escet.setext.generator.parser.ParserAction;
import org.eclipse.escet.setext.generator.parser.ParserReduceAction;
import org.eclipse.escet.setext.generator.parser.ParserShiftAction;
import org.eclipse.escet.setext.generator.scanner.Automaton;
import org.eclipse.escet.setext.generator.scanner.AutomatonState;
import org.eclipse.escet.setext.generator.scanner.RegExToDfa;
import org.eclipse.escet.setext.parser.ast.Specification;
import org.eclipse.escet.setext.parser.ast.Symbol;
import org.eclipse.escet.setext.parser.ast.parser.NonTerminal;
import org.eclipse.escet.setext.parser.ast.parser.ParserRule;
import org.eclipse.escet.setext.parser.ast.parser.ParserRulePart;
import org.eclipse.escet.setext.parser.ast.parser.StartSymbol;
import org.eclipse.escet.setext.parser.ast.scanner.KeywordsIdentifier;
import org.eclipse.escet.setext.parser.ast.scanner.KeywordsTerminal;
import org.eclipse.escet.setext.parser.ast.scanner.Terminal;
import org.eclipse.escet.setext.parser.ast.scanner.TerminalsDecl;

/** SeText scanner/parser generator. */
public class SeTextGenerator {
    /** Constructor for the {@link SeTextGenerator} class. */
    private SeTextGenerator() {
        // Static class.
    }

    /**
     * Generates scanner DFAs for the given specification.
     *
     * @param spec The specification for which to generate scanner DFAs.
     */
    public static void generateScanner(Specification spec) {
        // Make sure scanner class is specified.
        if (spec.scannerClass == null) {
            throw new InvalidInputException("Scanner class not specified.");
        }

        // Generate single scanner for the entire specification.
        out("Generating scanner class \"%s\".", spec.scannerClass.toString());

        // Check current working directory against package name.
        String packagePath = spec.scannerClass.getPackageName().replace('.', '/');
        String curWorkDir = Paths.getCurWorkingDir().replace('\\', '/');
        if (!curWorkDir.endsWith(packagePath)) {
            warn("Current working directory \"%s\" does not end with \"%s\", which is expected for Java type \"%s\".",
                    curWorkDir, packagePath, spec.scannerClass.toString());
        }

        // Open scanner debug file.
        String debugFilePath = InputFileOption.getPath() + "." + spec.scannerClass.getSimpleClassName() + ".dbg";
        AppStream dbgStream = openDebugFile(debugFilePath);

        // Generate DFA per scanner state.
        Map<String, Automaton> scannerAutomata = map();
        Assert.check(spec.states.iterator().next().equals(""));
        try {
            for (String state: spec.states) {
                // Filter terminals for state.
                List<Terminal> stateTerminals = copy(spec.terminals);
                Iterator<Terminal> terminalsIter = stateTerminals.iterator();
                while (terminalsIter.hasNext()) {
                    Terminal terminal = terminalsIter.next();
                    if (!terminal.getStateName().equals(state)) {
                        terminalsIter.remove();
                    }
                }
                if (stateTerminals.isEmpty()) {
                    String msg = fmt("No terminals for scanner state \"%s\".", state);
                    throw new InvalidInputException(msg);
                }

                // Generate DFA, and store it.
                Automaton dfa = RegExToDfa.terminalsToDfa(stateTerminals);
                scannerAutomata.put(state, dfa);

                // Print DFA.
                if (!state.equals("")) {
                    dbgStream.println();
                    dbgStream.println();
                }
                String msg = fmt("*** DFA for scanner state \"%s\". ***", state);
                dbgStream.println(msg);
                dfa.print(dbgStream);
            }
        } finally {
            // Close scanner debug file.
            dbgStream.close();
        }

        // Write scanner class.
        String scannerFilePath = spec.scannerClass.getSimpleClassName();
        scannerFilePath += ".java";
        scannerFilePath = Paths.resolve(scannerFilePath);

        writeScannerClass(spec, scannerAutomata, scannerFilePath);
    }

    /**
     * Generates a parser for the given start symbol.
     *
     * @param spec The SeText specification.
     * @param start The start symbol for which to generate the parser.
     */
    public static void generateParser(Specification spec, StartSymbol start) {
        out("Generating parser class \"%s\" for %s symbol \"%s\".", start.javaType.toString(), start.getStartType(),
                start.symbol.name);

        // Check current working directory against package name.
        String packagePath = start.javaType.getPackageName().replace('.', '/');
        String curWorkDir = Paths.getCurWorkingDir().replace('\\', '/');
        if (!curWorkDir.endsWith(packagePath)) {
            warn("Current working directory \"%s\" does not end with "
                    + "\"%s\", which is expected for Java type \"%s\".", curWorkDir, packagePath,
                    start.javaType.toString());
        }

        // Write parser syntax to a file, in BNF syntax.
        if (OutputBnfFileOption.isEnabled()) {
            writeParserSyntaxBnf(spec);
        }

        // Open parser debug file.
        String debugFilePath = InputFileOption.getPath() + "." + start.javaType.getSimpleClassName() + ".dbg";
        AppStream dbgStream = openDebugFile(debugFilePath);

        // Generate parser.
        LALR1ParserGenerator generator;
        LALR1Automaton aut;
        try {
            // Generate parser automaton.
            generator = new LALR1ParserGenerator();
            aut = generator.generate(spec, start);

            // Print parser automaton.
            String msg = fmt("Parser automaton for %s state \"%s\":", start.getStartType(), start.name.id);
            dbgStream.println(msg);
            aut.printTable(dbgStream, generator);

            // Conflicts.
            if (generator.conflicts > 0) {
                msg = generator.getConflictsText();
                throw new InvalidInputException(msg);
            }
        } finally {
            // Close parser debug file.
            dbgStream.close();
        }

        // Write parser class.
        String javaFilePath = start.javaType.getSimpleClassName();
        javaFilePath += ".java";
        javaFilePath = Paths.resolve(javaFilePath);

        writeParserClass(spec, start, aut, generator, javaFilePath);
    }

    /**
     * Does the scanner have call back hooks?
     *
     * @param spec The SeText specification.
     * @return {@code true} if the scanner has call back hooks, {@code false} otherwise.
     */
    private static boolean scannerHasHooks(Specification spec) {
        for (Terminal terminal: spec.terminals) {
            if (terminal.func != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Writes the scanner class to a file.
     *
     * @param spec The SeText specification.
     * @param scannerAutomata Mapping from scanner states to the DFA for that state.
     * @param javaFilePath The absolute local file system path of the Java file to which to write the class.
     */
    public static void writeScannerClass(Specification spec, Map<String, Automaton> scannerAutomata,
            String javaFilePath)
    {
        // Initialization.
        CodeBox code = new MemoryCodeBox();

        // Determine DFA start states per scanner state.
        List<String> states = set2list(spec.states);
        Assert.check(first(states).equals(""));

        int[] initials = new int[states.size()];
        initials[0] = 0;
        for (int i = 0; i < initials.length - 1; i++) {
            String scannerState = states.get(i);

            int dfaStates = scannerAutomata.get(scannerState).states.size();
            initials[i + 1] = initials[i] + dfaStates;
        }

        // Determine imports.
        List<String> imports = list();
        imports.add("java.io.IOException");
        imports.add("org.eclipse.escet.setext.runtime.Scanner");
        imports.add("org.eclipse.escet.setext.runtime.Token");
        boolean scannerHasHooks = scannerHasHooks(spec);
        if (scannerHasHooks) {
            imports.add(spec.hooksClass.className);
        }
        Collections.sort(imports);

        // Check for conflicting imports. Note that while we now generate an
        // error, we could use fully quantified names to get around this
        // problem. That workaround may not work for classes in the default
        // package though. Also note that we can have other conflicts, such
        // as class names that have the names of local variables etc, though
        // this is much more unlikely.
        Map<String, String> importNames = map();
        for (String imp: imports) {
            int idx = imp.lastIndexOf('.');
            String name = (idx == -1) ? imp : imp.substring(idx + 1);
            String entry = importNames.get(name);
            if (entry == null) {
                // First entry for this name.
                importNames.put(name, imp);
            } else {
                // Conflicting name.
                String msg = fmt("Conflicting class names: \"%s\" and \"%s\".", entry, imp);
                throw new UnsupportedException(msg);
            }
        }

        // Add header.
        addJavaHeader(code);

        // Add Eclipse Java formatter disable comments.
        code.add("// Disable Eclipse Java formatter for generated code file:");
        code.add("// @formatter:off");
        code.add();

        // Add package and imports.
        String packageName = spec.scannerClass.getPackageName();
        if (!packageName.isEmpty()) {
            code.add("package %s;", packageName);
            code.add();
        }
        for (String imp: JavaCodeUtils.formatImports(imports, packageName)) {
            code.add(imp);
        }

        // Add class JavaDoc.
        code.add();
        code.add("/**");
        code.add(" * %s.", spec.scannerClass.getSimpleClassName());
        code.add(" *");
        code.add(" * <p>This scanner is generated by SeText.</p>");
        code.add(" */");

        // Add class header.
        code.add("public final class %s extends Scanner {", spec.scannerClass.getSimpleClassName());

        // Add 'SCANNER_STATES' field.
        code.indent();
        code.add("/** Textual representations of the scanner states, for debugging. */");
        code.add("private static final String[] SCANNER_STATES = new String[] {");
        code.indent();
        for (int i = 0; i < states.size(); i++) {
            String stateTxt = states.get(i);
            code.add("%s, // %d", Strings.stringToJava(stateTxt), i);
        }
        code.dedent();
        code.add("};");

        // Add 'TERMINAL_NEEDS_POST' field.
        code.add();
        code.add("/** For each terminal, whether it needs post processing. */");
        code.add("private static final boolean[] TERMINAL_NEEDS_POST = new boolean[] {");
        code.indent();
        for (int i = 0; i < spec.terminals.size(); i++) {
            Terminal terminal = spec.terminals.get(i);
            boolean needsPost = (terminal.newState != null) || (terminal.func != null);
            code.add("%s, // %d", needsPost, i);
        }
        code.dedent();
        code.add("};");

        // Add 'TERMINALS' field.
        code.add();
        code.add("/** Textual representations of the terminals, for debugging. */");
        code.add("private static final String[] TERMINALS = new String[] {");
        code.indent();
        for (int i = 0; i < spec.terminals.size(); i++) {
            Terminal terminal = spec.terminals.get(i);
            String terminalTxt = "\"" + terminal.regEx.toString() + "\"";
            if (terminal.name != null) {
                terminalTxt = terminal.name + "=" + terminalTxt;
            }
            code.add("%s, // %d", Strings.stringToJava(terminalTxt), i);
        }
        code.dedent();
        code.add("};");

        // Add 'TERMINAL_NAMES' field.
        code.add();
        code.add("/** Names of the terminals (may be {@code null}), for exceptions. */");
        code.add("private static final String[] TERMINAL_NAMES = new String[] {");
        code.indent();
        for (int i = 0; i < spec.terminals.size(); i++) {
            Terminal terminal = spec.terminals.get(i);
            code.add("%s, // %d", (terminal.name == null) ? "null" : Strings.stringToJava(terminal.name), i);
        }
        code.dedent();
        code.add("};");

        // Add 'TERMINAL_DESCRIPTIONS' field.
        code.add();
        code.add("/** Descriptions of the terminals (may be {@code null}), for exceptions. */");
        code.add("private static final String[] TERMINAL_DESCRIPTIONS = new String[] {");
        code.indent();
        for (int i = 0; i < spec.terminals.size(); i++) {
            Terminal terminal = spec.terminals.get(i);

            String descr;
            if (terminal.description != null) {
                // Cut of surrounding '[' and ']' and trim whitespace.
                descr = Strings.slice(terminal.description.description, 1, -1);
                descr = descr.trim();
            } else if (terminal.isEof()) {
                descr = "end-of-file";
            } else {
                descr = terminal.regEx.getDescriptionText();
                if (descr == null) {
                    descr = terminal.name;
                } else {
                    descr = "\"" + descr + "\"";
                }
            }

            code.add("%s, // %d", (descr == null) ? "null" : Strings.stringToJava(descr), i);
        }
        code.dedent();
        code.add("};");

        // Add 'hooks' field, if needed. Is 'public' to allow parser to access
        // the hooks and reuse them.
        if (scannerHasHooks) {
            code.add();
            code.add("/** Scanner call back hook methods. */");
            code.add("public final %s hooks;", spec.hooksClass.getSimpleClassName());
        }

        // Add 'state' field.
        code.add();
        code.add("/** The current DFA state of the scanner. */");
        code.add("private int state;");

        // Add constructor.
        code.add();
        code.add("/** Constructor for the {@link %s} class. */", spec.scannerClass.getSimpleClassName());
        code.add("public %s() {", spec.scannerClass.getSimpleClassName());
        code.indent();
        code.add("scannerStates = SCANNER_STATES;");
        code.add("terminalNeedsPost = TERMINAL_NEEDS_POST;");
        code.add("terminals = TERMINALS;");
        code.add("terminalNames = TERMINAL_NAMES;");
        code.add("terminalDescriptions = TERMINAL_DESCRIPTIONS;");
        if (scannerHasHooks) {
            code.add("hooks = new %s();", spec.hooksClass.getSimpleClassName());
        }
        code.dedent();
        code.add("}");

        // Add 'nextTokenInternal' method.
        code.add();
        code.add("@Override");
        code.add("public final Token nextTokenInternal() throws IOException {");
        code.indent();
        if (spec.states.size() == 1) {
            code.add("state = 0;");
        } else {
            code.add("switch (scannerState) {");
            code.indent();
            for (int i = 0; i < initials.length; i++) {
                code.add("case %d: state = %d; break;", i, initials[i]);
            }
            code.add("default:");
            code.indent();
            code.add("String msg = \"Unknown scanner state: \" + scannerState;");
            code.add("throw new RuntimeException(msg);");
            code.dedent();
            code.dedent();
            code.add("}");
        }
        code.add("startOffset = curOffset;");
        code.add("startLine = curLine;");
        code.add("startColumn = curColumn;");
        code.add("acceptOffset = -1;");
        code.add("acceptLine = -1;");
        code.add("acceptColumn = -1;");
        code.add("accept = -1;");
        code.add();
        code.add("while (true) {");
        code.indent();
        code.add("// Read next code point (the one for 'curOffset').");
        code.add("int codePoint = getNextCodePoint();");
        code.add();
        code.add("// Process the code point.");
        code.add("Token rslt;");
        code.add("switch (state) {");
        code.indent();
        int stateIdx = 0;
        for (int autIdx = 0; autIdx < states.size(); autIdx++) {
            Assert.check(stateIdx == initials[autIdx]);

            String state = states.get(autIdx);
            code.add("// Scanner state \"%s\".", state);

            Automaton aut = scannerAutomata.get(state);
            for (AutomatonState autState: aut.states.keySet()) {
                Assert.check(autState.id == stateIdx - initials[autIdx]);

                code.add("case %d:", stateIdx);
                code.indent();

                if (autState.edges.isEmpty()) {
                    code.add("return acceptOrError();");
                } else {
                    code.add("rslt = nextToken%d(codePoint);", stateIdx);
                    code.add("if (rslt != null) {");
                    code.indent();
                    code.add("return rslt;");
                    code.dedent();
                    code.add("}");
                    code.add("break;");
                }
                code.dedent();

                stateIdx++;
            }
        }
        code.add("default:");
        code.indent();
        code.add("String msg = \"Unknown scanner DFA state: \" + state;");
        code.add("throw new RuntimeException(msg);");
        code.dedent();
        code.dedent();
        code.add("}");
        code.add();
        code.add("// The code point has been processed. Move on to the next one.");
        code.add("// Also update line/column tracking information.");
        code.add("curOffset++;");
        code.add("if (codePoint == '\\n') {");
        code.indent();
        code.add("curLine++;");
        code.add("curColumn = 1;");
        code.dedent();
        code.add("} else {");
        code.indent();
        code.add("curColumn++;");
        code.dedent();
        code.add("}");
        code.dedent();
        code.add("}");
        code.dedent();
        code.add("}");

        // Add 'nextToken*' methods.
        stateIdx = 0;
        for (int autIdx = 0; autIdx < states.size(); autIdx++) {
            Assert.check(stateIdx == initials[autIdx]);

            String state = states.get(autIdx);
            Automaton aut = scannerAutomata.get(state);
            for (AutomatonState autState: aut.states.keySet()) {
                Assert.check(autState.id == stateIdx - initials[autIdx]);

                if (autState.edges.isEmpty()) {
                    stateIdx++;
                    continue;
                }

                code.add();
                code.add("@SuppressWarnings(\"javadoc\")");
                code.add("private final Token nextToken%d(int codePoint) {", stateIdx);
                code.indent();
                code.add("switch (codePoint) {");
                code.indent();
                List<Pair<Integer, AutomatonState>> edges;
                edges = autState.getSortedEdges();
                for (int edgeIdx = 0; edgeIdx < edges.size(); edgeIdx++) {
                    // Get current edge.
                    Pair<Integer, AutomatonState> edge;
                    edge = edges.get(edgeIdx);

                    // Output case for this code point.
                    int codePoint = edge.left;
                    String codePointTxt;
                    Assert.check(-1 <= codePoint && codePoint <= 127, "Unexpected code point.");
                    if (codePoint == '\n') {
                        codePointTxt = "'\\n'";
                    } else if (codePoint == '\r') {
                        codePointTxt = "'\\r'";
                    } else if (codePoint == '\t') {
                        codePointTxt = "'\\t'";
                    } else if (codePoint == '\'') {
                        codePointTxt = "'\\''";
                    } else if (codePoint == '\\') {
                        codePointTxt = "'\\\\'";
                    } else if (codePoint <= 31 || codePoint == 127) {
                        codePointTxt = Integer.toString(codePoint);
                    } else {
                        codePointTxt = "'" + Strings.codePointToStr(codePoint) + "'";
                    }
                    code.add("case %s:", codePointTxt);

                    // If this edge has the same target state as the next
                    // edge, merge them.
                    AutomatonState curTarget = edge.right;
                    if (edgeIdx + 1 < edges.size()) {
                        AutomatonState nextTarget = edges.get(edgeIdx + 1).right;
                        if (curTarget == nextTarget) {
                            continue;
                        }
                    }

                    // Output handling code for the (merged) code point(s).
                    AutomatonState target = edge.right;
                    code.indent();
                    if (target.accept != null) {
                        code.add("acceptOffset = curOffset;");
                        code.add("acceptLine = curLine;");
                        code.add("acceptColumn = curColumn;");
                        code.add("accept = %d;", spec.terminals.indexOf(target.accept));
                    }
                    if (autState != target) {
                        code.add("state = %d;", initials[autIdx] + target.id);
                    }
                    code.add("break;");
                    code.dedent();
                }
                code.add("default:");
                code.indent();
                code.add("return acceptOrError();");
                code.dedent();
                code.dedent();
                code.add("}");
                code.add("if (debugScanner) {");
                code.indent();
                code.add("debugScanner(codePoint, state);");
                code.dedent();
                code.add("}");
                code.add("return null;");
                code.dedent();
                code.add("}");

                stateIdx++;
            }
        }

        // Add 'tokenAccepted' method.
        code.add();
        code.add("@Override");
        code.add("protected final void tokenAccepted(Token token) {");
        code.indent();
        code.add("switch (token.id) {");
        code.indent();
        for (int i = 0; i < spec.terminals.size(); i++) {
            Terminal terminal = spec.terminals.get(i);
            code.add("case %d:", i);
            code.indent();
            if (terminal.newState != null) {
                int newState = (terminal.newState.id.equals("")) ? 0 : states.indexOf(terminal.newState.id);
                Assert.check(newState >= 0);
                code.add("scannerState = %d;", newState);
            }
            if (terminal.func != null) {
                code.add("hooks.%s(token);", terminal.func.id);
            }
            code.add("return;");
            code.dedent();
        }
        code.add("default:");
        code.indent();
        code.add("throw new RuntimeException(\"Unknown terminal id: \" + token.id);");
        code.dedent();
        code.dedent();
        code.add("}");
        code.dedent();
        code.add("}");

        // Add 'getKeywords' method.
        code.add();
        code.add("/**");
        code.add(" * Returns the keywords in the given category.");
        code.add(" *");
        code.add(" * @param keywordCategory The name of the keyword category.");
        code.add(" * @return The keywords in the given category.");
        code.add(" * @throws IllegalArgumentException If the category does not exist for");
        code.add(" *      this scanner.");
        code.add(" */");
        code.add("public static String[] getKeywords(String keywordCategory) {");
        code.indent();
        for (TerminalsDecl tdecl: filter(spec.decls, TerminalsDecl.class)) {
            List<KeywordsTerminal> kwterms;
            kwterms = filter(tdecl.terminals, KeywordsTerminal.class);
            for (KeywordsTerminal kwterm: kwterms) {
                code.add("if (keywordCategory.equals(%s)) {", Strings.stringToJava(kwterm.name));
                code.indent();
                code.add("return new String[] {");
                code.indent();
                for (KeywordsIdentifier keyword: kwterm.keywords) {
                    code.add("%s,", Strings.stringToJava(keyword.keyword.id));
                }
                code.dedent();
                code.add("};");
                code.dedent();
                code.add("}");
                code.add();
            }
        }
        code.add("String msg = \"Unknown keyword category: \" + keywordCategory;");
        code.add("throw new IllegalArgumentException(msg);");
        code.dedent();
        code.add("}");
        code.dedent();

        // Hooks interface.
        if (scannerHasHooks) {
            Set<String> callbackSet = set();
            for (Terminal terminal: spec.terminals) {
                if (terminal.func != null) {
                    callbackSet.add(terminal.func.id);
                }
            }
            List<String> callbacks = sortedstrings(callbackSet);

            code.indent();
            code.add();
            code.add("/** Scanner call back hooks for {@link %s}. */", spec.scannerClass.getSimpleClassName());
            code.add("public interface Hooks {");
            code.indent();
            boolean firstCallback = true;
            for (String callback: callbacks) {
                if (firstCallback) {
                    firstCallback = false;
                } else {
                    code.add();
                }
                code.add("/**");
                code.add(" * Call back hook \"%s\" for {@link %s}.", callback, spec.scannerClass.getSimpleClassName());
                code.add(" * May perform in-place modifications to the scanned text of the token.");
                code.add(" *");
                code.add(" * @param token The scanned token.");
                code.add(" */");
                code.add("public void %s(Token token);", callback);
            }
            code.dedent();
            code.add("}");
            code.dedent();
        }

        // End footer.
        code.add("}");

        // Write Java code.
        if (OutputJavaFilesOption.isEnabled()) {
            code.writeToFile(javaFilePath);

            out("Scanner class \"%s\" written to file \"%s\".", spec.scannerClass.toString(), javaFilePath);
        }
    }

    /**
     * Write parser syntax to a file, in BNF syntax.
     *
     * @param spec The SeText specification.
     */
    private static void writeParserSyntaxBnf(Specification spec) {
        // Get path.
        String path = InputFileOption.getPath();
        if (path.endsWith(".setext")) {
            path = Strings.slice(path, 0, -".setext".length());
        }
        path += ".bnf";
        path = Paths.resolve(path);

        // Write text.
        try (AppStream stream = new FileAppStream(path)) {
            boolean first = true;
            for (boolean generated: new boolean[] {false, true}) {
                for (NonTerminal nonterm: spec.nonterminals) {
                    if (nonterm.generated != generated) {
                        continue;
                    }

                    if (first) {
                        first = false;
                    } else {
                        stream.println();
                    }

                    int nameLength = nonterm.name.length();
                    String nameSpaces = Strings.spaces(nameLength);
                    if (nonterm.rules.isEmpty()) {
                        stream.printfln("%s : /* empty */", nonterm.name);
                    } else {
                        for (int i = 0; i < nonterm.rules.size(); i++) {
                            ParserRule rule = nonterm.rules.get(i);
                            String prefix = (i == 0) ? nonterm.name : nameSpaces;
                            String sep = (i == 0) ? ":" : "|";
                            List<String> parts = listc(rule.symbols.size());
                            for (ParserRulePart part: rule.symbols) {
                                Symbol symbol = part.symbol;
                                if (symbol instanceof NonTerminal) {
                                    parts.add(symbol.name);
                                } else {
                                    Terminal term = (Terminal)symbol;
                                    if (term.regEx.isDescriptionText()) {
                                        String txt = term.regEx.getDescriptionText();
                                        parts.add(fmt("\"%s\"", txt));
                                    } else {
                                        parts.add(symbol.name);
                                    }
                                }
                            }
                            if (parts.isEmpty()) {
                                parts.add("/* empty */");
                            }
                            stream.printfln("%s %s %s", prefix, sep, String.join(" ", parts));
                        }
                    }
                    stream.printfln("%s ;", nameSpaces);
                }
            }
        }

        // Done writing.
        out("Syntax in BNF format written to file \"%s\".", path);
    }

    /**
     * Writes a parser class to a file.
     *
     * @param spec The SeText specification.
     * @param start The start/main symbol for which to write the class.
     * @param aut The LALR(1) parser automaton.
     * @param generator The LALR(1) parser generator used to construct the LALR(1) parser automaton.
     * @param javaFilePath The absolute local file system path of the Java file to which to write the class.
     */
    public static void writeParserClass(Specification spec, StartSymbol start, LALR1Automaton aut,
            LALR1ParserGenerator generator, String javaFilePath)
    {
        // Initialization.
        CodeBox code = new MemoryCodeBox();

        // Determine non-terminals.
        Set<NonTerminal> nonterminalSet = set();
        for (LALR1AutomatonState state: aut.states) {
            nonterminalSet.addAll(state.gotos.keySet());
        }
        List<NonTerminal> nonterminals = copy(spec.nonterminals);
        Iterator<NonTerminal> nontermsIter = nonterminals.iterator();
        while (nontermsIter.hasNext()) {
            NonTerminal nonterminal = nontermsIter.next();
            if (!nonterminalSet.contains(nonterminal)) {
                nontermsIter.remove();
            }
        }

        // Determine imports.
        SortedSet<String> imports = new TreeSet<>();
        imports.add("static org.eclipse.escet.common.java.Strings.fmt");

        imports.add("java.io.IOException");
        imports.add("org.eclipse.escet.setext.runtime.Parser");
        imports.add("org.eclipse.escet.setext.runtime.ParserHooksBase");
        imports.add("org.eclipse.escet.setext.runtime.Token");

        imports.add(spec.scannerClass.className);
        imports.add(spec.hooksClass.className);

        boolean anyGenericTypes = false;
        for (NonTerminal nonterminal: nonterminals) {
            imports.addAll(nonterminal.returnType.getNames());
            if (nonterminal.returnType.isGeneric()) {
                anyGenericTypes = true;
            }
        }

        // Check for conflicting imports. Note that while we now generate an
        // error, we could use fully quantified names to get around this
        // problem. That workaround may not work for classes in the default
        // package though. Also note that we can have other conflicts, such
        // as class names that have the names of local variables etc, though
        // this is much more unlikely.
        Map<String, String> importNames = map();
        for (String imp: imports) {
            int idx = imp.lastIndexOf('.');
            String name = (idx == -1) ? imp : imp.substring(idx + 1);
            String entry = importNames.get(name);
            if (entry == null) {
                // First entry for this name.
                importNames.put(name, imp);
            } else {
                // Conflicting name.
                String msg = fmt("Conflicting class names: \"%s\" and \"%s\".", entry, imp);
                throw new UnsupportedException(msg);
            }
        }

        // Add header.
        addJavaHeader(code);

        // Add Eclipse Java formatter disable comments.
        code.add("// Disable Eclipse Java formatter for generated code file:");
        code.add("// @formatter:off");
        code.add();

        // Add package and imports.
        String packageName = start.javaType.getPackageName();
        if (!packageName.isEmpty()) {
            code.add("package %s;", packageName);
            code.add();
        }

        for (String imp: JavaCodeUtils.formatImports(imports, packageName)) {
            code.add(imp);
        }

        // Add class JavaDoc.
        code.add();
        code.add("/**");
        code.add(" * %s.", start.javaType.getSimpleClassName());
        code.add(" *");
        code.add(" * <p>This parser is generated by SeText for %s symbol", start.getStartType());
        code.add(" * \"%s\".</p>", start.name.id);
        code.add(" */");

        // Add class header.
        if (anyGenericTypes) {
            code.add("@SuppressWarnings(\"unchecked\")");
        }
        code.add("public final class %s extends Parser<%s> {", start.javaType.getSimpleClassName(),
                start.symbol.returnType.toSimpleString());

        // Add 'NON_TERMINAL_NAMES' field.
        code.indent();
        code.add("/** The names of the non-terminals, ordered by their unique ids. */");
        code.add("private static final String[] NON_TERMINAL_NAMES = {");
        code.indent();
        for (NonTerminal nonterminal: nonterminals) {
            code.add("\"%s\",", nonterminal.name);
        }
        code.dedent();
        code.add("};");

        // Add 'STATES_TO_NON_TERMINAL_NAMES' field.
        code.add();
        code.add("/**");
        code.add(" * The entry symbol names for each of the parser states, and {@code null}");
        code.add(" * for the initial state.");
        code.add(" */");
        code.add("private static final String[] ENTRY_SYMBOL_NAMES = new String[] {");
        code.indent();
        for (LALR1AutomatonState state: aut.states) {
            if (state.id == 0) {
                Assert.check(state.entrySymbol == null);
                code.add("null,");
            } else {
                Assert.notNull(state.entrySymbol);
                Assert.notNull(state.entrySymbol.name);
                code.add("\"%s\",", state.entrySymbol.name);
            }
        }
        code.dedent();
        code.add("};");

        // Add other fields.
        code.add();
        code.add("/** Parser call back hook methods. */");
        code.add("private final %s hooks;", spec.hooksClass.getSimpleClassName());
        code.add();
        code.add("/** Whether parsing has completed (final result has been accepted). */");
        code.add("private boolean accept;");
        code.add();
        code.add("/** The parse result, but only if {@code #accept} is {@code true}. */");
        code.add("private %s acceptObject;", start.symbol.returnType.toSimpleString());
        code.add();
        code.add("/** The current scanner token to process, if any. */");
        code.add("private Token token;");
        code.add();
        code.add("/** Whether parsing has resulted in a reduce action. */");
        code.add("private boolean reduce;");
        code.add();
        code.add("/** The state from which to reduce, if {@code #reduce} is {@code true}. */");
        code.add("private int reduceState;");
        code.add();
        code.add("/** The non-terminal to reduce, if {@code #reduce} is {@code true}. */");
        code.add("private int reduceNonTerminal;");

        // Add constructor.
        boolean scannerHasHooks = scannerHasHooks(spec);
        code.add();
        code.add("/** Constructor for the {@link %s} class. */", start.javaType.getSimpleClassName());
        code.add("public %s() {", start.javaType.getSimpleClassName());
        code.indent();
        code.add("super(new %s());", spec.scannerClass.getSimpleClassName());
        code.add("entrySymbolNames = ENTRY_SYMBOL_NAMES;");
        code.add("firstTerminals = FirstTerminals.FIRST_TERMINALS;");
        code.add("firstTerminalsReduced = FirstTerminalsReduced.FIRST_TERMINALS_REDUCED;");
        code.add("reducibleNonTerminals = ReducibleNonTerminals.REDUCIBLE_NON_TERMINALS;");
        code.add("reducibleNonTerminalsReduced = ReducibleNonTerminalsReduced.REDUCIBLE_NON_TERMINALS_REDUCED;");
        if (scannerHasHooks) {
            // Reuse from scanner.
            code.add("hooks = ((%s)scanner).hooks;", spec.scannerClass.getSimpleClassName());
        } else {
            // Nothing to reuse, so create new instance.
            code.add("hooks = new %s();", spec.hooksClass.getSimpleClassName());
        }
        code.dedent();
        code.add("}");

        // Add 'getHooks' method.
        code.add();
        code.add("@Override");
        code.add("public ParserHooksBase getHooks() {");
        code.indent();
        code.add("return hooks;");
        code.dedent();
        code.add("}");

        // Add 'parse' method.
        code.add();
        code.add("@Override");
        code.add("protected final %s parse() throws IOException {", start.symbol.returnType.toSimpleString());
        code.indent();
        code.add("token = nextToken();");
        code.add("int state;");
        code.add();
        code.add("accept = false;");
        code.add();
        code.add("while (true) {");
        code.indent();
        code.add("// Perform action.");
        code.add("state = getCurrentState();");
        code.add("reduce = false;");
        code.add();
        code.add("switch (state) {");
        code.indent();
        for (LALR1AutomatonState state: aut.states) {
            code.add("case %d:", state.id);
            code.indent();
            code.add("action%d();", state.id);
            code.add("break;");
            code.dedent();
        }
        code.add("default:");
        code.indent();
        code.add("String msg = \"Unknown parser state: \" + state;");
        code.add("throw new RuntimeException(msg);");
        code.dedent();
        code.dedent();
        code.add("}");
        code.add();
        code.add("// Accept action.");
        code.add("if (accept) {");
        code.indent();
        code.add("return acceptObject;");
        code.dedent();
        code.add("}");
        code.add();
        code.add("// Shift action.");
        code.add("if (!reduce) {");
        code.indent();
        code.add("continue;");
        code.dedent();
        code.add("}");
        code.add();
        code.add("// Perform goto (as part of a reduce action).");
        code.add("switch (reduceState) {");
        code.indent();
        for (LALR1AutomatonState state: aut.states) {
            code.add("case %d:", state.id);
            code.indent();
            code.add("goto%d();", state.id);
            code.add("break;");
            code.dedent();
        }
        code.add("default:");
        code.indent();
        code.add("String msg = fmt(\"Unknown reduce state %d.\", reduceState);");
        code.add("throw new RuntimeException(msg);");
        code.dedent();
        code.dedent();
        code.add("}");
        code.dedent();
        code.add("}");
        code.dedent();
        code.add("}");

        // Add 'action*' methods.
        for (LALR1AutomatonState state: aut.states) {
            code.add();
            code.add("/**");
            code.add(" * Parser action code for parser state %d.", state.id);
            code.add(" *");
            code.add(" * @throws IOException If reading the input failed due to an I/O error.");
            code.add(" */");
            code.add("private final void action%d() throws IOException {", state.id);
            code.indent();
            code.add("switch (token.id) {");
            code.indent();
            List<Pair<Terminal, ParserAction>> actionEntries;
            actionEntries = state.getSortedActions(spec.terminals, nonterminals);
            for (int actIdx = 0; actIdx < actionEntries.size(); actIdx++) {
                // Get terminal and action.
                Pair<Terminal, ParserAction> entry;
                entry = actionEntries.get(actIdx);
                Terminal terminal = entry.left;
                ParserAction action = entry.right;

                // Generate action case.
                int termId = spec.terminals.indexOf(terminal);

                // If this terminal has the same action as the next terminal,
                // merge them.
                if (actIdx + 1 < actionEntries.size()) {
                    ParserAction nextAction = actionEntries.get(actIdx + 1).right;
                    if (action.equals(nextAction)) {
                        code.add("case %d:", termId);
                        continue;
                    }
                }

                // Generate action code.
                code.add("case %d: {", termId);
                code.indent();
                if (action instanceof ParserShiftAction) {
                    ParserShiftAction shift = (ParserShiftAction)action;
                    code.add("// Shift %d.", shift.target.id);
                    code.add("token = doShift(token, %d);", shift.target.id);
                    code.add("return;");
                } else if (action instanceof ParserAcceptAction) {
                    code.add("// Accept.");
                    code.add("Object rslt = doAccept(token);");
                    code.add("accept = true;");
                    code.add("acceptObject = (%s)rslt;", start.symbol.returnType.toSimpleString());
                    code.add("return;");
                } else {
                    Assert.check(action instanceof ParserReduceAction);
                    ParserReduceAction reduce = (ParserReduceAction)action;

                    int reduceId = nonterminals.indexOf(reduce.nonterminal);

                    code.add("// Reduce %s : %s;", reduce.nonterminal.name, reduce.rule.toString());

                    code.add("doReduce1(token, %d);", reduceId);

                    List<ParserRulePart> symbols = reduce.rule.symbols;

                    for (int i = symbols.size() - 1; i >= 0; i--) {
                        if (symbols.get(i).callBackArg) {
                            code.add("Object o%d = doReduce2();", i + 1);
                        } else {
                            code.add("doReduce2();");
                        }
                    }
                    if (!symbols.isEmpty()) {
                        code.add();
                    }

                    List<String> paramTxts = list();
                    for (int i = 0; i < symbols.size(); i++) {
                        ParserRulePart part = symbols.get(i);
                        if (!part.callBackArg) {
                            continue;
                        }

                        Symbol symbol = part.symbol;
                        String argTypeTxt;
                        if (symbol instanceof Terminal) {
                            argTypeTxt = "Token";
                        } else {
                            argTypeTxt = ((NonTerminal)symbol).returnType.toSimpleString();
                        }

                        paramTxts.add("(" + argTypeTxt + ")o" + Integer.toString(i + 1));
                    }

                    int maxRuleNr = reduce.nonterminal.rules.size();
                    int maxRuleNrLen = Integer.toString(maxRuleNr).length();
                    int ruleNr = reduce.nonterminal.rules.indexOf(reduce.rule);
                    String ruleNrStr = Integer.toString(ruleNr + 1);
                    ruleNrStr = StringUtils.leftPad(ruleNrStr, maxRuleNrLen, '0');

                    String hookName = "hooks.parse" + reduce.nonterminal.name + ruleNrStr;

                    code.add("%s o = %s(%s);", reduce.nonterminal.returnType.toSimpleString(), hookName,
                            String.join(", ", paramTxts));
                    code.add();
                    code.add("reduce = true;");
                    code.add("reduceNonTerminal = %d;", reduceId);
                    code.add("reduceState = doReduce3(o);");
                    code.add("return;");
                }
                code.dedent();
                code.add("}");
                code.add();
            }
            code.add("default:");
            code.indent();
            code.add("parsingFailed(token);");
            code.dedent();
            code.dedent();
            code.add("}");
            code.dedent();
            code.add("}");
        }

        // Add 'goto*' methods.
        for (LALR1AutomatonState state: aut.states) {
            code.add();
            code.add("/** Parser goto code for parser state %d. */", state.id);
            code.add("private final void goto%d() {", state.id);
            code.indent();
            code.add("switch (reduceNonTerminal) {");
            code.indent();
            int nonTerminalId = 0;
            for (NonTerminal nonterminal: nonterminals) {
                LALR1AutomatonState target = state.gotos.get(nonterminal);
                if (target != null) {
                    code.add("case %d:", nonTerminalId);
                    code.indent();
                    code.add("// %s", nonterminal.name);
                    code.add("doGoto(%d);", target.id);
                    code.add("return;");
                    code.add();
                    code.dedent();
                }
                nonTerminalId++;
            }
            code.add("default:");
            code.indent();
            code.add("String msg = fmt(\"Unknown non-terminal %d (%s) for reduce \" +");
            code.add("                 \"state %d.\", reduceNonTerminal,");
            code.add("                 NON_TERMINAL_NAMES[reduceNonTerminal],");
            code.add("                 reduceState);");
            code.add("throw new RuntimeException(msg);");
            code.dedent();
            code.dedent();
            code.add("}");
            code.dedent();
            code.add("}");
        }

        // Add 'getNonTerminalName' method.
        code.add();
        code.add("@Override");
        code.add("protected final String getNonTerminalName(int nonTerminalId) {");
        code.indent();
        code.add("return NON_TERMINAL_NAMES[nonTerminalId];");
        code.dedent();
        code.add("}");

        // Add 'FirstTerminals' inner class.
        code.add();
        code.add("/** See {@code Parser.firstTerminals}. */");
        code.add("private static final class FirstTerminals {");
        code.indent();
        code.add("/** See {@code Parser.firstTerminals}. */");
        code.add("private static final int[][] FIRST_TERMINALS = new int[][] {");
        code.indent();
        for (int si = 0; si < aut.states.size(); si++) {
            LALR1AutomatonState state = aut.states.get(si);

            // Process all productions.
            Set<LookaheadItem> productions = state.itemsClosure;
            Set<Integer> termIdSet = set();
            for (LookaheadItem production: productions) {
                // Get remainder of production.
                GrammarItem item = production.item;
                List<Symbol> remainingSymbols = item.remainder();

                // Add terminals from 'first' of remainder.
                Set<Symbol> first = remainingSymbols.isEmpty() ? set(EmptySymbol.EMPTY_SYMBOL)
                        : generator.getFirst(remainingSymbols);
                boolean hasEmpty = false;
                for (Symbol symbol: first) {
                    if (symbol instanceof EmptySymbol) {
                        hasEmpty = true;
                        continue;
                    }
                    if (symbol instanceof NonTerminal) {
                        continue;
                    }
                    Assert.check(symbol instanceof Terminal);
                    termIdSet.add(spec.terminals.indexOf(symbol));
                }

                // Add end-of-file terminals for augmented start rule.
                if (item.nonterminal.isAugmentedStartSymbol() && hasEmpty) {
                    for (Terminal eofTerminal: spec.getEofTerminals()) {
                        termIdSet.add(spec.terminals.indexOf(eofTerminal));
                    }
                }
            }

            // Write terminal ids.
            code.add("{%s}, // state %d", intsToStr(sortedgeneric(termIdSet)), si);
        }
        code.dedent();
        code.add("};");
        code.dedent();
        code.add("}");

        // Add 'FirstTerminalReduced' inner class.
        code.add();
        code.add("/** See {@code Parser.firstTerminalsReduced}. */");
        code.add("private static final class FirstTerminalsReduced {");
        code.indent();
        code.add("/** See {@code Parser.firstTerminalsReduced}. */");
        code.add("private static final int[][][] FIRST_TERMINALS_REDUCED = new int[][][] {");
        code.indent();
        for (int si = 0; si < aut.states.size(); si++) {
            LALR1AutomatonState state = aut.states.get(si);

            // Process all productions, to get mapping from reduced
            // non-terminal ids to 'first' terminals.
            Set<LookaheadItem> productions = state.itemsClosure;
            Map<Integer, Set<Integer>> termIdMap = map();
            for (LookaheadItem production: productions) {
                // Get remainder of production.
                GrammarItem item = production.item;
                List<Symbol> remainingSymbols = item.remainder();

                // Get non-terminal to reduce and strip it off the remainder.
                if (remainingSymbols.isEmpty()) {
                    continue;
                }
                Symbol remainingHead = first(remainingSymbols);
                if (!(remainingHead instanceof NonTerminal)) {
                    continue;
                }
                NonTerminal reducedNonTerm = (NonTerminal)remainingHead;
                int reducedIdx = spec.nonterminals.indexOf(reducedNonTerm);
                Assert.check(reducedIdx != -1);
                remainingSymbols.remove(0);

                // Get or initialize entry in mapping for reduced non-terminal.
                Set<Integer> termIdSet = termIdMap.get(reducedIdx);
                if (termIdSet == null) {
                    termIdSet = set();
                    termIdMap.put(reducedIdx, termIdSet);
                }

                // Add terminals from 'first' of remainder.
                Set<Symbol> first = remainingSymbols.isEmpty() ? set(EmptySymbol.EMPTY_SYMBOL)
                        : generator.getFirst(remainingSymbols);
                boolean hasEmpty = false;
                for (Symbol symbol: first) {
                    if (symbol instanceof EmptySymbol) {
                        hasEmpty = true;
                        continue;
                    }
                    if (symbol instanceof NonTerminal) {
                        continue;
                    }
                    Assert.check(symbol instanceof Terminal);
                    termIdSet.add(spec.terminals.indexOf(symbol));
                }

                // Add end-of-file terminals for augmented start rule.
                if (item.nonterminal.isAugmentedStartSymbol() && hasEmpty) {
                    for (Terminal eofTerminal: spec.getEofTerminals()) {
                        termIdSet.add(spec.terminals.indexOf(eofTerminal));
                    }
                }
            }

            // Write terminal ids.
            List<String> lines = list();
            for (Entry<Integer, Set<Integer>> e: termIdMap.entrySet()) {
                if (e.getValue().isEmpty()) {
                    continue;
                }
                List<Integer> termIds = sortedgeneric(e.getValue());
                lines.add(fmt("{%d, %s},", e.getKey(), intsToStr(termIds)));
            }

            if (lines.isEmpty()) {
                code.add("{}, // state %d", si);
            } else {
                Collections.sort(lines, Strings.SORTER);
                code.add("{ // state %d", si);
                code.indent();
                for (String line: lines) {
                    code.add(line);
                }
                code.dedent();
                code.add("},");
            }
        }
        code.dedent();
        code.add("};");
        code.dedent();
        code.add("}");

        // Add 'ReducibleNonTerminals' inner class.
        code.add();
        code.add("/** See {@code Parser.reducibleNonTerminals}. */");
        code.add("private static final class ReducibleNonTerminals {");
        code.indent();
        code.add("/** See {@code Parser.reducibleNonTerminals}. */");
        code.add("private static final int[][][] REDUCIBLE_NON_TERMINALS = new int[][][] {");
        code.indent();
        for (int si = 0; si < aut.states.size(); si++) {
            LALR1AutomatonState state = aut.states.get(si);

            // Process all productions, to get mapping from reducible
            // non-terminal ids to the number of symbols to pop.
            Set<LookaheadItem> productions = state.itemsClosure;
            Map<Integer, Set<Integer>> nontermIdMap = map();
            for (LookaheadItem production: productions) {
                // Get remainder of production.
                GrammarItem item = production.item;
                List<Symbol> remainingSymbols = item.remainder();

                // Check whether remainder accepts empty.
                Set<Symbol> first = remainingSymbols.isEmpty() ? set(EmptySymbol.EMPTY_SYMBOL)
                        : generator.getFirst(remainingSymbols);
                boolean isEmpty = false;
                for (Symbol symbol: first) {
                    if (symbol instanceof EmptySymbol) {
                        isEmpty = true;
                        break;
                    }
                }

                // Add reducible non-terminal with pop count.
                if (isEmpty) {
                    if (item.nonterminal.isAugmentedStartSymbol()) {
                        continue;
                    }

                    // Get or initialize entry in mapping.
                    int ntId = spec.nonterminals.indexOf(item.nonterminal);
                    Set<Integer> nonTermPopCnts = nontermIdMap.get(ntId);
                    if (nonTermPopCnts == null) {
                        nonTermPopCnts = set();
                        nontermIdMap.put(ntId, nonTermPopCnts);
                    }

                    // Add pop count.
                    nonTermPopCnts.add(item.progress);
                }
            }

            // Write reducible non-terminal ids with their pop counts.
            List<String> lines = list();
            for (Entry<Integer, Set<Integer>> e: nontermIdMap.entrySet()) {
                if (e.getValue().isEmpty()) {
                    continue;
                }
                List<Integer> popCnts = sortedgeneric(e.getValue());
                lines.add(fmt("{%d, %s},", e.getKey(), intsToStr(popCnts)));
            }

            if (lines.isEmpty()) {
                code.add("{}, // state %d", si);
            } else {
                Collections.sort(lines, Strings.SORTER);
                code.add("{ // state %d", si);
                code.indent();
                for (String line: lines) {
                    code.add(line);
                }
                code.dedent();
                code.add("},");
            }
        }
        code.dedent();
        code.add("};");
        code.dedent();
        code.add("}");

        // Add 'ReducibleNonTerminalsReduced' inner class.
        code.add();
        code.add("/** See {@code Parser.reducibleNonTerminalsReduced}. */");
        code.add("private static final class ReducibleNonTerminalsReduced {");
        code.indent();
        code.add("/** See {@code Parser.reducibleNonTerminalsReduced}. */");
        code.add("private static final int[][][] REDUCIBLE_NON_TERMINALS_REDUCED = new int[][][] {");
        code.indent();
        for (int si = 0; si < aut.states.size(); si++) {
            LALR1AutomatonState state = aut.states.get(si);

            // Process all productions, to get mapping from reduced
            // non-terminal ids to a mapping from reducible non-terminal ids
            // to the number of symbols to pop.
            Set<LookaheadItem> productions = state.itemsClosure;
            Map<Integer, Map<Integer, Set<Integer>>> outerMap = map();
            for (LookaheadItem production: productions) {
                // Get remainder of production.
                GrammarItem item = production.item;
                List<Symbol> remainingSymbols = item.remainder();

                // Get non-terminal to reduce and strip it off the remainder.
                if (remainingSymbols.isEmpty()) {
                    continue;
                }
                Symbol remainingHead = first(remainingSymbols);
                if (!(remainingHead instanceof NonTerminal)) {
                    continue;
                }
                NonTerminal reducedNonTerm = (NonTerminal)remainingHead;
                int reducedIdx = spec.nonterminals.indexOf(reducedNonTerm);
                Assert.check(reducedIdx != -1);
                remainingSymbols.remove(0);

                // Check whether remainder accepts empty.
                Set<Symbol> first = remainingSymbols.isEmpty() ? set(EmptySymbol.EMPTY_SYMBOL)
                        : generator.getFirst(remainingSymbols);
                boolean isEmpty = false;
                for (Symbol symbol: first) {
                    if (symbol instanceof EmptySymbol) {
                        isEmpty = true;
                        break;
                    }
                }

                // Add reducible non-terminal with pop count.
                if (isEmpty) {
                    if (item.nonterminal.isAugmentedStartSymbol()) {
                        continue;
                    }

                    // Get or initialize entry in mapping for reduced
                    // non-terminal.
                    Map<Integer, Set<Integer>> innerMap;
                    innerMap = outerMap.get(reducedIdx);
                    if (innerMap == null) {
                        innerMap = map();
                        outerMap.put(reducedIdx, innerMap);
                    }

                    // Get or initialize entry in mapping for reducing
                    // non-terminal.
                    int ntId = spec.nonterminals.indexOf(item.nonterminal);
                    Set<Integer> nonTermPopCnts = innerMap.get(ntId);
                    if (nonTermPopCnts == null) {
                        nonTermPopCnts = set();
                        innerMap.put(ntId, nonTermPopCnts);
                    }

                    // Add pop count.
                    nonTermPopCnts.add(item.progress);
                }
            }

            // Write reducible non-terminal ids with their pop counts.
            List<String> lines = list();
            Set<Entry<Integer, Map<Integer, Set<Integer>>>> outers;
            outers = outerMap.entrySet();
            for (Entry<Integer, Map<Integer, Set<Integer>>> outer: outers) {
                Set<Entry<Integer, Set<Integer>>> inners;
                inners = outer.getValue().entrySet();
                for (Entry<Integer, Set<Integer>> inner: inners) {
                    if (inner.getValue().isEmpty()) {
                        continue;
                    }
                    List<Integer> popCnts = sortedgeneric(inner.getValue());
                    lines.add(fmt("{%d, %d, %s},", outer.getKey(), inner.getKey(), intsToStr(popCnts)));
                }
            }

            if (lines.isEmpty()) {
                code.add("{}, // state %d", si);
            } else {
                Collections.sort(lines, Strings.SORTER);
                code.add("{ // state %d", si);
                code.indent();
                for (String line: lines) {
                    code.add(line);
                }
                code.dedent();
                code.add("},");
            }
        }
        code.dedent();
        code.add("};");
        code.dedent();
        code.add("}");

        // Hooks interface.
        code.add();
        code.add("/** Parser call back hooks for {@link %s}. */", start.javaType.getSimpleClassName());
        code.add("public interface Hooks extends ParserHooksBase {");
        code.indent();

        boolean firstMethod = true;
        for (NonTerminal nonterminal: nonterminals) {
            for (int ri = 0; ri < nonterminal.rules.size(); ri++) {
                ParserRule rule = nonterminal.rules.get(ri);

                if (firstMethod) {
                    firstMethod = false;
                } else {
                    code.add();
                }

                List<String> ruleTxts = list();
                for (ParserRulePart part: rule.symbols) {
                    if (part.symbol instanceof Terminal) {
                        Terminal terminal = (Terminal)part.symbol;
                        ruleTxts.add((part.callBackArg ? "@" : "") + terminal.name);
                    } else {
                        ruleTxts.add(((NonTerminal)part.symbol).name);
                    }
                }

                code.add("/**");
                code.add(" * Parser call back hook for rule/production:");
                code.add(" *");
                code.add(" * <p>{@code %s : %s;}</p>", nonterminal.name, String.join(" ", ruleTxts));
                code.add(" *");

                List<String> argTxts = list();
                for (int pi = 0; pi < rule.symbols.size(); pi++) {
                    // Skip non-callback parts.
                    ParserRulePart part = rule.symbols.get(pi);
                    if (!part.callBackArg) {
                        continue;
                    }

                    // Get type.
                    String typeTxt;
                    if (part.symbol instanceof Terminal) {
                        typeTxt = "Token";
                    } else {
                        typeTxt = ((NonTerminal)part.symbol).returnType.toSimpleString();
                    }

                    // Get argument name.
                    String argName = typeTxt.substring(0, 1).toLowerCase(Locale.US);
                    argName += str(pi + 1);

                    // Add parameter JavaDoc and parameter text.
                    code.add(" * @param %s {@code %s}.", argName, part.name);
                    argTxts.add(typeTxt + " " + argName);
                }

                code.add(" * @return The parser call back hook result.");
                code.add(" */");

                int maxRuleNr = nonterminal.rules.size();
                int maxRuleNrLen = Integer.toString(maxRuleNr).length();
                String ruleNrStr = Integer.toString(ri + 1);
                ruleNrStr = StringUtils.leftPad(ruleNrStr, maxRuleNrLen, '0');

                code.add("public %s parse%s%s(%s);", nonterminal.returnType.toSimpleString(), nonterminal.name,
                        ruleNrStr, String.join(", ", argTxts));
            }
        }
        code.dedent();
        code.add("}");
        code.dedent();

        // End footer.
        code.add("}");

        // Write Java code.
        if (OutputJavaFilesOption.isEnabled()) {
            code.writeToFile(javaFilePath);

            out("Parser class \"%s\" for %s symbol \"%s\" written to file \"%s\".", start.javaType.toString(),
                    start.getStartType(), start.symbol.name, javaFilePath);
        }
    }

    /**
     * Convert a list of integers to a string with comma-separated values.
     *
     * @param integers Integer values to convert.
     * @return The created string.
     */
    private static String intsToStr(List<Integer> integers) {
        return integers.stream().map(String::valueOf).collect(Collectors.joining(", "));
    }

    /**
     * Writes a hooks class skeleton to a file.
     *
     * @param spec The SeText specification.
     * @param filePath The absolute local file system path of the file to which to write the class skeleton.
     */
    public static void writeHooksSkeleton(Specification spec, String filePath) {
        out("Generating hooks skeleton class \"%s\".", spec.hooksClass.className);

        // Initialization.
        CodeBox code = new MemoryCodeBox();

        // Scanner hooks needed.
        boolean scannerHooksNeeded = false;
        for (Terminal terminal: spec.terminals) {
            if (terminal.func != null) {
                scannerHooksNeeded = true;
                break;
            }
        }

        // Determine imports.
        SortedSet<String> imports = new TreeSet<>();

        if (scannerHooksNeeded) {
            imports.add("org.eclipse.escet.setext.runtime.Token");
        }
        if (scannerHooksNeeded) {
            imports.add(spec.scannerClass.className);
        }

        if (!spec.getStartSymbols().isEmpty()) {
            imports.add("org.eclipse.escet.setext.runtime.Parser");
        }

        for (NonTerminal nonterminal: spec.nonterminals) {
            imports.addAll(nonterminal.returnType.getNames());
            for (ParserRule rule: nonterminal.rules) {
                for (ParserRulePart part: rule.symbols) {
                    if (part.callBackArg && part.symbol instanceof Terminal) {
                        imports.add("org.eclipse.escet.setext.runtime.Token");
                    }
                }
            }
        }

        for (StartSymbol start: spec.getStartSymbols()) {
            imports.add(start.javaType.className);
        }

        // Check for conflicting imports. Note that while we now generate an
        // error, we could use fully quantified names to get around this
        // problem. That workaround may not work for classes in the default
        // package though. Also note that we can have other conflicts, such
        // as class names that have the names of local variables etc, though
        // this is much more unlikely.
        Map<String, String> importNames = map();
        for (String imp: imports) {
            int idx = imp.lastIndexOf('.');
            String name = (idx == -1) ? imp : imp.substring(idx + 1);
            String entry = importNames.get(name);
            if (entry == null) {
                // First entry for this name.
                importNames.put(name, imp);
            } else {
                // Conflicting name.
                String msg = fmt("Conflicting class names: \"%s\" and \"%s\".", entry, imp);
                throw new UnsupportedException(msg);
            }
        }

        // Add header.
        addJavaHeader(code);

        // Add package and imports.
        String packageName = spec.hooksClass.getPackageName();
        if (!packageName.isEmpty()) {
            code.add("package %s;", packageName);
            code.add();
        }

        for (String imp: JavaCodeUtils.formatImports(imports, packageName)) {
            code.add(imp);
        }

        // Get scanner/parser classes with hooks interfaces.
        List<String> interfaceClasses = list();
        if (scannerHooksNeeded) {
            interfaceClasses.add(spec.scannerClass.getSimpleClassName());
        }
        for (StartSymbol start: spec.getStartSymbols()) {
            interfaceClasses.add(start.javaType.getSimpleClassName());
        }

        // Add class JavaDoc.
        code.add();
        code.add("/**");
        code.add(" * Call back hook methods for:");
        code.add(" * <ul>");
        for (String interfaceClass: interfaceClasses) {
            code.add(" *  <li>{@link %s}</li>", interfaceClass);
        }
        code.add(" * </ul>");
        code.add(" */");

        // Add class header.
        code.add("public final class %s", spec.hooksClass.getSimpleClassName());
        for (int i = 0; i < interfaceClasses.size(); i++) {
            String preFix = (i == 0) ? "implements" : "          ";
            String postFix = (i == interfaceClasses.size() - 1) ? "" : ",";
            code.add("%s %s.Hooks%s", preFix, interfaceClasses.get(i), postFix);
        }
        code.add("{");

        // Generate methods.
        boolean firstMethod = true;

        // Add 'setParser' method, if needed.
        if (!spec.getStartSymbols().isEmpty()) {
            code.indent();
            code.add("@Override");
            code.add("public void setParser(Parser<?> parser) {");
            code.add("}");
            code.dedent();

            firstMethod = false;
        }

        // Get scanner call back hook method names.
        Set<String> callbackSet = set();
        for (Terminal terminal: spec.terminals) {
            if (terminal.func != null) {
                callbackSet.add(terminal.func.id);
            }
        }
        List<String> callbacks = sortedstrings(callbackSet);

        // Add scanner call back hook methods.
        for (String callback: callbacks) {
            if (firstMethod) {
                firstMethod = false;
            } else {
                code.add();
            }

            code.indent();
            code.add("@Override");
            code.add("public void %s(Token token) {", callback);
            code.add("}");
            code.dedent();
        }

        // Add parser call back hook methods.
        for (NonTerminal nonterminal: spec.nonterminals) {
            for (int ri = 0; ri < nonterminal.rules.size(); ri++) {
                ParserRule rule = nonterminal.rules.get(ri);

                if (firstMethod) {
                    firstMethod = false;
                } else {
                    code.add();
                }

                List<String> ruleTxts = list();
                for (ParserRulePart part: rule.symbols) {
                    if (part.symbol instanceof Terminal) {
                        Terminal terminal = (Terminal)part.symbol;
                        ruleTxts.add((part.callBackArg ? "@" : "") + terminal.name);
                    } else {
                        ruleTxts.add(((NonTerminal)part.symbol).name);
                    }
                }

                code.indent();
                code.add("@Override // %s : %s;", nonterminal.name, String.join(" ", ruleTxts));
                code.dedent();

                List<String> argTxts = list();
                for (int pi = 0; pi < rule.symbols.size(); pi++) {
                    // Skip non-callback parts.
                    ParserRulePart part = rule.symbols.get(pi);
                    if (!part.callBackArg) {
                        continue;
                    }

                    // Get type.
                    String typeTxt;
                    if (part.symbol instanceof Terminal) {
                        typeTxt = "Token";
                    } else {
                        typeTxt = ((NonTerminal)part.symbol).returnType.toSimpleString();
                    }

                    // Get argument name.
                    String argName = typeTxt.substring(0, 1).toLowerCase(Locale.US);
                    argName += str(pi + 1);

                    // Add parameter text.
                    argTxts.add(typeTxt + " " + argName);
                }

                int maxRuleNr = nonterminal.rules.size();
                int maxRuleNrLen = Integer.toString(maxRuleNr).length();
                String ruleNrStr = Integer.toString(ri + 1);
                ruleNrStr = StringUtils.leftPad(ruleNrStr, maxRuleNrLen, '0');

                code.indent();
                code.add("public %s parse%s%s(%s) {", nonterminal.returnType.toSimpleString(), nonterminal.name,
                        ruleNrStr, String.join(", ", argTxts));
                code.indent();
                code.add("// return null;");
                code.dedent();
                code.add("}");
                code.dedent();
            }
        }

        // End footer.
        code.add("}");

        // Write code.
        if (OutputJavaFilesOption.isEnabled()) {
            code.writeToFile(filePath);

            out("Hooks skeleton class \"%s\" written to file \"%s\".", spec.hooksClass.className, filePath);
        }
    }

    /**
     * Adds the user-requested header to the Java code.
     *
     * @param code The code box to which to add the header.
     */
    private static void addJavaHeader(CodeBox code) {
        // Header requested?
        if (JavaHeaderOption.getPath() == null) {
            return;
        }

        // Read header.
        String headerPath = JavaHeaderOption.getPath();
        String absHeaderPath = Paths.resolve(headerPath);
        List<String> lines;
        try (InputStream stream = new BufferedInputStream(new FileInputStream(absHeaderPath))) {
            lines = IOUtils.readLines(stream, "UTF-8");
        } catch (IOException e) {
            String msg = fmt("Failed to read Java header file \"%s\".", headerPath);
            throw new InputOutputException(msg, e);
        }

        // Add header to code.
        for (String line: lines) {
            code.add(line);
        }

        // Add separator line, if needed.
        if (!lines.isEmpty() && last(lines).length() > 0) {
            code.add();
        }
    }

    /**
     * Open a debug file.
     *
     * @param path The absolute or relative local file system path of the debug file to open.
     * @return The print stream for the opened debug file.
     */
    public static AppStream openDebugFile(String path) {
        path = Paths.resolve(path);

        AppStream dbgStream;
        try {
            if (OutputDebugFilesOption.isEnabled()) {
                dbgStream = new FileAppStream(path);
            } else {
                dbgStream = NullAppStream.NULL_APP_STREAM;
            }
        } catch (InputOutputException e) {
            String msg = fmt("Failed to create debug file \"%s\".", path);
            throw new InputOutputException(msg, e);
        }

        return dbgStream;
    }
}
