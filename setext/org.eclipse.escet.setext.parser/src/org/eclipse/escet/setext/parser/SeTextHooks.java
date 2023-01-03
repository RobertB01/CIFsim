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

package org.eclipse.escet.setext.parser;

import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Strings;
import org.eclipse.escet.common.java.TextPosition;
import org.eclipse.escet.setext.parser.ast.Decl;
import org.eclipse.escet.setext.parser.ast.HooksDecl;
import org.eclipse.escet.setext.parser.ast.Identifier;
import org.eclipse.escet.setext.parser.ast.Name;
import org.eclipse.escet.setext.parser.ast.Specification;
import org.eclipse.escet.setext.parser.ast.Symbol;
import org.eclipse.escet.setext.parser.ast.TerminalDescription;
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
import org.eclipse.escet.setext.runtime.Parser;
import org.eclipse.escet.setext.runtime.Token;

/**
 * Call back hook methods for:
 * <ul>
 * <li>{@link SeTextScanner}</li>
 * <li>{@link SeTextParser}</li>
 * </ul>
 */
public final class SeTextHooks implements SeTextScanner.Hooks, SeTextParser.Hooks {
    /** The parser that owns the call back hooks. */
    private Parser<?> parser;

    @Override
    public void setParser(Parser<?> parser) {
        this.parser = parser;
    }

    @Override
    public void scanRegExEscapedChar(Token token) {
        Assert.check(token.text.length() == 2);
        Assert.check(token.text.charAt(0) == '\\');
        char c = token.text.charAt(1);
        switch (c) {
            case 'n':
                c = '\n';
                break;
            case 't':
                c = '\t';
                break;
            case 'r':
                c = '\r';
                break;
        }
        token.text = Character.toString(c);
    }

    @Override
    public void scanRegExShortcutName(Token token) {
        Assert.check(token.text.startsWith("{"));
        Assert.check(token.text.endsWith("}"));
        token.text = Strings.slice(token.text, 1, -1);
    }

    @Override // Specification : ;
    public Specification parseSpecification1() {
        List<Decl> decls = list();
        String src = parser.getSource();
        String loc = parser.getLocation();
        return new Specification(decls, TextPosition.createDummy(loc, src));
    }

    @Override // Specification : Decls;
    public Specification parseSpecification2(List<Decl> l1) {
        String src = parser.getSource();
        String loc = parser.getLocation();
        return new Specification(l1, TextPosition.createDummy(loc, src));
    }

    @Override // Decls : Decl;
    public List<Decl> parseDecls1(Decl d1) {
        return list(d1);
    }

    @Override // Decls : Decls Decl;
    public List<Decl> parseDecls2(List<Decl> l1, Decl d2) {
        l1.add(d2);
        return l1;
    }

    @Override // Decl : TerminalsDecl;
    public Decl parseDecl1(TerminalsDecl t1) {
        return t1;
    }

    @Override // Decl : ShortcutDecl;
    public Decl parseDecl2(ShortcutDecl s1) {
        return s1;
    }

    @Override // Decl : MainDecl;
    public Decl parseDecl3(StartSymbol s1) {
        return s1;
    }

    @Override // Decl : StartDecl;
    public Decl parseDecl4(StartSymbol s1) {
        return s1;
    }

    @Override // Decl : HooksDecl;
    public Decl parseDecl5(HooksDecl h1) {
        return h1;
    }

    @Override // Decl : ScannerDecl;
    public Decl parseDecl6(ScannerDecl s1) {
        return s1;
    }

    @Override // Decl : ImportDecl;
    public Decl parseDecl7(ImportDecl i1) {
        return i1;
    }

    @Override // Decl : RuleDecl;
    public Decl parseDecl8(NonTerminal n1) {
        return n1;
    }

    @Override // TerminalsDecl : @ATTK @TERMINALSKW OptIdentifier COLONTK Terminals @ENDKW;
    public TerminalsDecl parseTerminalsDecl1(Token t1, Token t2, Identifier i3, List<Symbol> l5, Token t6) {
        parser.addFoldRange(t1, t6);
        return new TerminalsDecl(i3, l5, (i3 == null) ? t2.position : i3.position);
    }

    @Override // Terminals : Terminal;
    public List<Symbol> parseTerminals1(Symbol s1) {
        return list(s1);
    }

    @Override // Terminals : Terminals Terminal;
    public List<Symbol> parseTerminals2(List<Symbol> l1, Symbol s2) {
        l1.add(s2);
        return l1;
    }

    @Override // Terminal : KeywordsTerminal;
    public Symbol parseTerminal1(KeywordsTerminal k1) {
        return k1;
    }

    @Override // Terminal : RegExTerminal;
    public Symbol parseTerminal2(Terminal t1) {
        return t1;
    }

    @Override // Terminal : ATTK @EOFKW SEMICOLTK;
    public Symbol parseTerminal3(Token t2) {
        return new Terminal(null, new RegExChar(-1, t2.position), null, null, null, t2.position);
    }

    @Override // KeywordsTerminal : @ATTK KEYWORDSKW Identifier EQTK KeywordsIdentifiers @SEMICOLTK;
    public KeywordsTerminal parseKeywordsTerminal1(Token t1, Identifier i3, List<KeywordsIdentifier> l5, Token t6) {
        parser.addFoldRange(t1, t6);
        return new KeywordsTerminal(i3.id, l5, i3.position);
    }

    @Override // KeywordsIdentifiers : Identifier OptFunc OptTermDescr;
    public List<KeywordsIdentifier> parseKeywordsIdentifiers1(Identifier i1, Identifier i2, TerminalDescription t3) {
        return list(new KeywordsIdentifier(i1, i2, t3, i1.position));
    }

    @Override // KeywordsIdentifiers : KeywordsIdentifiers Identifier OptFunc OptTermDescr;
    public List<KeywordsIdentifier> parseKeywordsIdentifiers2(List<KeywordsIdentifier> l1, Identifier i2, Identifier i3,
            TerminalDescription t4)
    {
        l1.add(new KeywordsIdentifier(i2, i3, t4, i2.position));
        return l1;
    }

    @Override // RegExTerminal : OptRegExId RegExString OptFunc OptNewState OptTermDescr SEMICOLTK;
    public Terminal parseRegExTerminal1(Identifier i1, RegEx r2, Identifier i3, Identifier i4, TerminalDescription t5) {
        TextPosition p = (i1 != null) ? i1.position : r2.position;
        String name = (i1 != null) ? i1.id : null;
        return new Terminal(name, r2, i3, i4, t5, p);
    }

    @Override // OptRegExId : ;
    public Identifier parseOptRegExId1() {
        return null;
    }

    @Override // OptRegExId : Identifier EQTK;
    public Identifier parseOptRegExId2(Identifier i1) {
        return i1;
    }

    @Override // Identifier : @IDENTIFIERTK;
    public Identifier parseIdentifier1(Token t1) {
        return new Identifier(t1.text, t1.position);
    }

    @Override // OptTermDescr : ;
    public TerminalDescription parseOptTermDescr1() {
        return null;
    }

    @Override // OptTermDescr : @DESCRIPTIONTK;
    public TerminalDescription parseOptTermDescr2(Token t1) {
        return new TerminalDescription(t1.text, t1.position);
    }

    @Override // OptFunc : ;
    public Identifier parseOptFunc1() {
        return null;
    }

    @Override // OptFunc : CUROPENTK Identifier CURCLOSETK;
    public Identifier parseOptFunc2(Identifier i2) {
        return i2;
    }

    @Override // OptNewState : ;
    public Identifier parseOptNewState1() {
        return null;
    }

    @Override // OptNewState : @ARROWTK;
    public Identifier parseOptNewState2(Token t1) {
        return new Identifier("", t1.position);
    }

    @Override // OptNewState : ARROWTK Identifier;
    public Identifier parseOptNewState3(Identifier i2) {
        return i2;
    }

    @Override // ShortcutDecl : ATTK SHORTCUTKW Identifier EQTK RegExString SEMICOLTK;
    public ShortcutDecl parseShortcutDecl1(Identifier i3, RegEx r5) {
        return new ShortcutDecl(i3.id, r5, i3.position);
    }

    @Override // MainDecl : ATTK MAINKW Identifier COLONTK JavaType SEMICOLTK;
    public StartSymbol parseMainDecl1(Identifier i3, JavaType j5) {
        return new StartSymbol(true, i3, j5, i3.position);
    }

    @Override // StartDecl : ATTK STARTKW Identifier COLONTK JavaType SEMICOLTK;
    public StartSymbol parseStartDecl1(Identifier i3, JavaType j5) {
        return new StartSymbol(false, i3, j5, i3.position);
    }

    @Override // HooksDecl : ATTK HOOKSKW JavaType SEMICOLTK;
    public HooksDecl parseHooksDecl1(JavaType j3) {
        return new HooksDecl(j3, j3.position);
    }

    @Override // ScannerDecl : ATTK SCANNERKW JavaType SEMICOLTK;
    public ScannerDecl parseScannerDecl1(JavaType j3) {
        return new ScannerDecl(j3, j3.position);
    }

    @Override // ImportDecl : ATTK IMPORTKW JavaType SEMICOLTK;
    public ImportDecl parseImportDecl1(JavaType j3) {
        return new ImportDecl(j3, null, j3.position);
    }

    @Override // ImportDecl : ATTK IMPORTKW JavaType ASKW Identifier SEMICOLTK;
    public ImportDecl parseImportDecl2(JavaType j3, Identifier i5) {
        return new ImportDecl(j3, i5.id, i5.position);
    }

    @Override // RuleDecl : CUROPENTK JavaType CURCLOSETK Identifier COLONTK RuleAlts @SEMICOLTK;
    public NonTerminal parseRuleDecl1(JavaType j2, Identifier i4, List<ParserRule> l6, Token t7) {
        parser.addFoldRange(i4.position, t7.position);
        return new NonTerminal(j2, i4.id, l6, false, i4.position);
    }

    @Override // RuleAlts : ;
    public List<ParserRule> parseRuleAlts1() {
        List<ParserRulePart> parts = list();
        return list(new ParserRule(parts));
    }

    @Override // RuleAlts : RuleAlt;
    public List<ParserRule> parseRuleAlts2(ParserRule p1) {
        return list(p1);
    }

    @Override // RuleAlts : RuleAlts BARTK RuleAlt;
    public List<ParserRule> parseRuleAlts3(List<ParserRule> l1, ParserRule p3) {
        l1.add(p3);
        return l1;
    }

    @Override // RuleAlt : Identifier;
    public ParserRule parseRuleAlt1(Identifier i1) {
        List<ParserRulePart> parts = list();
        parts.add(new ParserRulePart(i1.id, false, i1.position));
        return new ParserRule(parts);
    }

    @Override // RuleAlt : ATTK Identifier;
    public ParserRule parseRuleAlt2(Identifier i2) {
        List<ParserRulePart> parts = list();
        parts.add(new ParserRulePart(i2.id, true, i2.position));
        return new ParserRule(parts);
    }

    @Override // RuleAlt : RuleAlt Identifier;
    public ParserRule parseRuleAlt3(ParserRule p1, Identifier i2) {
        List<ParserRulePart> symbols = list();
        symbols.addAll(p1.symbols);
        symbols.add(new ParserRulePart(i2.id, false, i2.position));
        return new ParserRule(symbols);
    }

    @Override // RuleAlt : RuleAlt ATTK Identifier;
    public ParserRule parseRuleAlt4(ParserRule p1, Identifier i3) {
        List<ParserRulePart> symbols = list();
        symbols.addAll(p1.symbols);
        symbols.add(new ParserRulePart(i3.id, true, i3.position));
        return new ParserRule(symbols);
    }

    @Override // JavaTypes : JavaType;
    public List<JavaType> parseJavaTypes1(JavaType j1) {
        return list(j1);
    }

    @Override // JavaTypes : JavaTypes COMMATK JavaType;
    public List<JavaType> parseJavaTypes2(List<JavaType> l1, JavaType j3) {
        l1.add(j3);
        return l1;
    }

    @Override // JavaType : Identifier;
    public JavaType parseJavaType1(Identifier i1) {
        return new JavaType(i1.id, null, i1.position);
    }

    @Override // JavaType : Identifier LTTK JavaTypes GTTK;
    public JavaType parseJavaType2(Identifier i1, List<JavaType> l3) {
        return new JavaType(i1.id, l3, i1.position);
    }

    @Override // JavaType : Name;
    public JavaType parseJavaType3(Name n1) {
        return new JavaType(n1.name, null, n1.position);
    }

    @Override // JavaType : Name LTTK JavaTypes GTTK;
    public JavaType parseJavaType4(Name n1, List<JavaType> l3) {
        return new JavaType(n1.name, l3, n1.position);
    }

    @Override // Name : @NAMETK;
    public Name parseName1(Token t1) {
        return new Name(t1.text, t1.position);
    }

    @Override // OptIdentifier : ;
    public Identifier parseOptIdentifier1() {
        return null;
    }

    @Override // OptIdentifier : Identifier;
    public Identifier parseOptIdentifier2(Identifier i1) {
        return i1;
    }

    @Override // RegExString : @REGEXSTARTTK RegEx @REGEXENDTK;
    public RegEx parseRegExString1(Token t1, RegEx r2, Token t3) {
        if (r2.position == null) {
            r2.position = TextPosition.mergePositions(t1.position, t3.position);
        }
        return r2;
    }

    @Override // RegEx : RegExBranch;
    public RegEx parseRegEx1(RegEx r1) {
        return r1;
    }

    @Override // RegEx : RegEx REGEXBARTK RegExBranch;
    public RegEx parseRegEx2(RegEx r1, RegEx r3) {
        List<RegEx> alts = list();

        if (r1 instanceof RegExAlts) {
            alts.addAll(((RegExAlts)r1).alts);
        } else {
            alts.add(r1);
        }

        if (r3 instanceof RegExAlts) {
            alts.addAll(((RegExAlts)r3).alts);
        } else {
            alts.add(r3);
        }

        return new RegExAlts(alts);
    }

    @Override // RegExBranch : RegExPiece;
    public RegEx parseRegExBranch1(RegEx r1) {
        return r1;
    }

    @Override // RegExBranch : RegExBranch RegExPiece;
    public RegEx parseRegExBranch2(RegEx r1, RegEx r2) {
        List<RegEx> seq = list();

        if (r1 instanceof RegExSeq) {
            seq.addAll(((RegExSeq)r1).sequence);
        } else {
            seq.add(r1);
        }

        if (r2 instanceof RegExSeq) {
            seq.addAll(((RegExSeq)r2).sequence);
        } else {
            seq.add(r2);
        }

        return new RegExSeq(seq);
    }

    @Override // RegExPiece : RegExAtom;
    public RegEx parseRegExPiece1(RegEx r1) {
        return r1;
    }

    @Override // RegExPiece : RegExPiece @ASTERISKTK;
    public RegEx parseRegExPiece2(RegEx r1, Token t2) {
        return new RegExStar(r1, t2.position);
    }

    @Override // RegExPiece : RegExPiece @PLUSTK;
    public RegEx parseRegExPiece3(RegEx r1, Token t2) {
        return new RegExPlus(r1, t2.position);
    }

    @Override // RegExPiece : RegExPiece @QUESTIONTK;
    public RegEx parseRegExPiece4(RegEx r1, Token t2) {
        return new RegExOpt(r1, t2.position);
    }

    @Override // RegExAtom : PAROPENTK RegEx PARCLOSETK;
    public RegEx parseRegExAtom1(RegEx r2) {
        return r2;
    }

    @Override // RegExAtom : @REGEXSHORTCUTTK;
    public RegEx parseRegExAtom2(Token t1) {
        return new RegExShortcut(t1.text, t1.position);
    }

    @Override // RegExAtom : RegExCharClass;
    public RegEx parseRegExAtom3(RegEx r1) {
        return r1;
    }

    @Override // RegExAtom : RegExChar;
    public RegEx parseRegExAtom4(RegExChar r1) {
        return r1;
    }

    @Override // RegExAtom : @DOTTK;
    public RegEx parseRegExAtom5(Token t1) {
        return new RegExDot(t1.position);
    }

    @Override // RegExCharClass : @SQOPENTK RegExCharsList SQCLOSETK;
    public RegEx parseRegExCharClass1(Token t1, List<RegExChars> l2) {
        return new RegExCharClass(false, l2, t1.position);
    }

    @Override // RegExCharClass : @SQOPENTK CARETTK RegExCharsList SQCLOSETK;
    public RegEx parseRegExCharClass2(Token t1, List<RegExChars> l3) {
        return new RegExCharClass(true, l3, t1.position);
    }

    @Override // RegExCharsList : RegExChars;
    public List<RegExChars> parseRegExCharsList1(RegExChars r1) {
        return list(r1);
    }

    @Override // RegExCharsList : RegExCharsList RegExChars;
    public List<RegExChars> parseRegExCharsList2(List<RegExChars> l1, RegExChars r2) {
        l1.add(r2);
        return l1;
    }

    @Override // RegExChars : RegExChar;
    public RegExChars parseRegExChars1(RegExChar r1) {
        return r1;
    }

    @Override // RegExChars : RegExChar @DASHTK RegExChar;
    public RegExChars parseRegExChars2(RegExChar r1, Token t2, RegExChar r3) {
        return new RegExCharSeq(r1, r3, t2.position);
    }

    @Override // RegExChar : @REGEXCHARTK;
    public RegExChar parseRegExChar1(Token t1) {
        Assert.check(t1.text.length() == 1);
        return new RegExChar(t1.text.charAt(0), t1.position);
    }

    @Override // RegExChar : @REGEXESCTK;
    public RegExChar parseRegExChar2(Token t1) {
        Assert.check(t1.text.length() == 1);
        return new RegExChar(t1.text.charAt(0), t1.position);
    }
}
