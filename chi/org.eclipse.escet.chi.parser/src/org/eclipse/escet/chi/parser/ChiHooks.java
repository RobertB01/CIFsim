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

package org.eclipse.escet.chi.parser;

import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newAssignmentStatement;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newBinaryExpression;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newBoolLiteral;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newBoolType;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newBreakStatement;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newCallExpression;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newCastExpression;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newChannelExpression;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newChannelType;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newCloseStatement;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newConstantDeclaration;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newContinueStatement;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newDelayStatement;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newDictType;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newDictionaryExpression;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newDictionaryPair;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newDistributionType;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newEnumDeclaration;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newEnumValue;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newExitStatement;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newFileType;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newFinishStatement;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newForStatement;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newFunctionDeclaration;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newFunctionType;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newIfCase;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newIfStatement;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newInstanceType;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newIntNumber;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newIntType;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newIteratedCreateCase;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newIteratedSelectCase;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newListExpression;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newListType;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newMatrixExpression;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newMatrixRow;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newMatrixType;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newModelDeclaration;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newPassStatement;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newProcessDeclaration;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newProcessInstance;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newProcessType;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newReadCallExpression;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newRealNumber;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newRealType;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newReceiveStatement;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newReturnStatement;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newRunStatement;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newSelectCase;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newSelectStatement;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newSendStatement;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newSetExpression;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newSetType;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newSliceExpression;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newStdLibFunctionReference;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newStringLiteral;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newStringType;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newTimeLiteral;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newTimerType;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newTupleExpression;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newTupleField;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newTupleType;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newTypeDeclaration;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newUnaryExpression;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newUnresolvedReference;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newUnresolvedType;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newUnwind;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newVariableDeclaration;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newVoidType;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newWhileStatement;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newWriteStatement;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newXperDeclaration;
import static org.eclipse.escet.common.emf.EMFHelper.deepclone;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.unescape;
import static org.eclipse.escet.common.position.common.PositionUtils.copyPosition;
import static org.eclipse.escet.common.position.common.PositionUtils.toPosition;

import java.util.List;

import org.eclipse.escet.chi.metamodel.chi.BinaryOperators;
import org.eclipse.escet.chi.metamodel.chi.ChannelOps;
import org.eclipse.escet.chi.metamodel.chi.CreateCase;
import org.eclipse.escet.chi.metamodel.chi.Declaration;
import org.eclipse.escet.chi.metamodel.chi.DictionaryPair;
import org.eclipse.escet.chi.metamodel.chi.EnumValue;
import org.eclipse.escet.chi.metamodel.chi.Expression;
import org.eclipse.escet.chi.metamodel.chi.IfCase;
import org.eclipse.escet.chi.metamodel.chi.IteratedSelectCase;
import org.eclipse.escet.chi.metamodel.chi.MatrixRow;
import org.eclipse.escet.chi.metamodel.chi.SelectCase;
import org.eclipse.escet.chi.metamodel.chi.Statement;
import org.eclipse.escet.chi.metamodel.chi.StdLibFunctions;
import org.eclipse.escet.chi.metamodel.chi.TupleField;
import org.eclipse.escet.chi.metamodel.chi.Type;
import org.eclipse.escet.chi.metamodel.chi.UnaryOperators;
import org.eclipse.escet.chi.metamodel.chi.Unwind;
import org.eclipse.escet.chi.metamodel.chi.VariableDeclaration;
import org.eclipse.escet.common.java.Lists;
import org.eclipse.escet.common.java.Strings;
import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.setext.runtime.Parser;
import org.eclipse.escet.setext.runtime.Token;

/**
 * Call back hook methods for:
 * <ul>
 * <li>{@link ChiScanner}</li>
 * <li>{@link ChiParser}</li>
 * </ul>
 */
public final class ChiHooks implements ChiScanner.Hooks, ChiParser.Hooks {
    /** The parser that owns the call back hooks. */
    private Parser<?> parser;

    @Override
    public void setParser(Parser<?> parser) {
        this.parser = parser;
    }

    @Override
    public void stripQuotes(Token token) {
        token.text = Strings.slice(token.text, 1, -1);
    }

    @Override // Program : ;
    public List<Declaration> parseProgram1() {
        return list();
    }

    @Override // Program : EndSimpleProgram;
    public List<Declaration> parseProgram2(List<Declaration> l1) {
        return l1;
    }

    @Override // Program : EndSimpleProgram SEMICOLTK;
    public List<Declaration> parseProgram3(List<Declaration> l1) {
        return l1;
    }

    @Override // Program : EndCompoundProgram;
    public List<Declaration> parseProgram4(List<Declaration> l1) {
        return l1;
    }

    @Override // Program : EndCompoundProgram SEMICOLTK;
    public List<Declaration> parseProgram5(List<Declaration> l1) {
        return l1;
    }

    @Override // EndSimpleProgram : SimpleProgram;
    public List<Declaration> parseEndSimpleProgram1(List<Declaration> l1) {
        return l1;
    }

    @Override // EndSimpleProgram : EndSimpleProgram SEMICOLTK SimpleProgram;
    public List<Declaration> parseEndSimpleProgram2(List<Declaration> l1, List<Declaration> l3) {
        l1.addAll(l3);
        return l1;
    }

    @Override // EndSimpleProgram : EndCompoundProgram SEMICOLTK SimpleProgram;
    public List<Declaration> parseEndSimpleProgram3(List<Declaration> l1, List<Declaration> l3) {
        l1.addAll(l3);
        return l1;
    }

    @Override // EndSimpleProgram : EndCompoundProgram SimpleProgram;
    public List<Declaration> parseEndSimpleProgram4(List<Declaration> l1, List<Declaration> l2) {
        l1.addAll(l2);
        return l1;
    }

    @Override // EndCompoundProgram : CompoundProgram;
    public List<Declaration> parseEndCompoundProgram1(List<Declaration> l1) {
        return l1;
    }

    @Override // EndCompoundProgram : EndCompoundProgram CompoundProgram;
    public List<Declaration> parseEndCompoundProgram2(List<Declaration> l1, List<Declaration> l2) {
        l1.addAll(l2);
        return l1;
    }

    @Override // EndCompoundProgram : EndCompoundProgram SEMICOLTK CompoundProgram;
    public List<Declaration> parseEndCompoundProgram3(List<Declaration> l1, List<Declaration> l3) {
        l1.addAll(l3);
        return l1;
    }

    @Override // EndCompoundProgram : EndSimpleProgram SEMICOLTK CompoundProgram;
    public List<Declaration> parseEndCompoundProgram4(List<Declaration> l1, List<Declaration> l3) {
        l1.addAll(l3);
        return l1;
    }

    @Override // CompoundProgram : @PROCKW @IDENTIFIERTK PAROPENTK PARCLOSETK COLONTK Body @ENDKW;
    public List<Declaration> parseCompoundProgram01(Token t1, Token t2, ParserBody p6, Token t7) {
        parser.addFoldRange(t1, t7);
        List<VariableDeclaration> vars = list();
        if (p6.vardefs != null) {
            vars.addAll(p6.vardefs);
        }
        List<Declaration> result = list();
        result.add(newProcessDeclaration(t2.text, toPosition(t2.position), null, p6.stats, vars));
        return result;
    }

    @Override // CompoundProgram : @PROCKW Type @IDENTIFIERTK PAROPENTK PARCLOSETK COLONTK Body @ENDKW;
    public List<Declaration> parseCompoundProgram02(Token t1, Type t2, Token t3, ParserBody p7, Token t8) {
        parser.addFoldRange(t1, t8);
        List<VariableDeclaration> vars = list();
        if (p7.vardefs != null) {
            vars.addAll(p7.vardefs);
        }
        List<Declaration> result = list();
        result.add(newProcessDeclaration(t3.text, toPosition(t3.position), t2, p7.stats, vars));
        return result;
    }

    @Override // CompoundProgram : @PROCKW @IDENTIFIERTK PAROPENTK FormalParameters PARCLOSETK COLONTK Body @ENDKW;
    public List<Declaration> parseCompoundProgram03(Token t1, Token t2, List<VariableDeclaration> l4, ParserBody p7,
            Token t8)
    {
        parser.addFoldRange(t1, t8);
        List<VariableDeclaration> vars = list();
        vars.addAll(l4);
        if (p7.vardefs != null) {
            vars.addAll(p7.vardefs);
        }
        List<Declaration> result = list();
        result.add(newProcessDeclaration(t2.text, toPosition(t2.position), null, p7.stats, vars));
        return result;
    }

    @Override // CompoundProgram : @PROCKW Type @IDENTIFIERTK PAROPENTK FormalParameters PARCLOSETK COLONTK Body @ENDKW;
    public List<Declaration> parseCompoundProgram04(Token t1, Type t2, Token t3, List<VariableDeclaration> l5,
            ParserBody p8, Token t9)
    {
        parser.addFoldRange(t1, t9);
        List<VariableDeclaration> vars = list();
        vars.addAll(l5);
        if (p8.vardefs != null) {
            vars.addAll(p8.vardefs);
        }
        List<Declaration> result = list();
        result.add(newProcessDeclaration(t3.text, toPosition(t3.position), t2, p8.stats, vars));
        return result;
    }

    @Override // CompoundProgram : @FUNCKW Type @IDENTIFIERTK PAROPENTK PARCLOSETK COLONTK Body @ENDKW;
    public List<Declaration> parseCompoundProgram05(Token t1, Type t2, Token t3, ParserBody p7, Token t8) {
        parser.addFoldRange(t1, t8);
        List<VariableDeclaration> vars = list();
        if (p7.vardefs != null) {
            vars.addAll(p7.vardefs);
        }
        List<Declaration> result = list();
        result.add(newFunctionDeclaration(t3.text, toPosition(t3.position), t2, p7.stats, vars));
        return result;
    }

    @Override // CompoundProgram : @FUNCKW Type @IDENTIFIERTK PAROPENTK FormalParameters PARCLOSETK COLONTK Body @ENDKW;
    public List<Declaration> parseCompoundProgram06(Token t1, Type t2, Token t3, List<VariableDeclaration> l5,
            ParserBody p8, Token t9)
    {
        parser.addFoldRange(t1, t9);
        List<VariableDeclaration> vars = list();
        vars.addAll(l5);
        if (p8.vardefs != null) {
            vars.addAll(p8.vardefs);
        }
        List<Declaration> result = list();
        result.add(newFunctionDeclaration(t3.text, toPosition(t3.position), t2, p8.stats, vars));
        return result;
    }

    @Override // CompoundProgram : @MODELKW @IDENTIFIERTK PAROPENTK PARCLOSETK COLONTK Body @ENDKW;
    public List<Declaration> parseCompoundProgram07(Token t1, Token t2, ParserBody p6, Token t7) {
        parser.addFoldRange(t1, t7);
        List<VariableDeclaration> vars = list();
        if (p6.vardefs != null) {
            vars.addAll(p6.vardefs);
        }
        List<Declaration> result = list();
        result.add(newModelDeclaration(t2.text, toPosition(t2.position), null, p6.stats, vars));
        return result;
    }

    @Override // CompoundProgram : @MODELKW Type @IDENTIFIERTK PAROPENTK PARCLOSETK COLONTK Body @ENDKW;
    public List<Declaration> parseCompoundProgram08(Token t1, Type t2, Token t3, ParserBody p7, Token t8) {
        parser.addFoldRange(t1, t8);
        List<VariableDeclaration> vars = list();
        if (p7.vardefs != null) {
            vars.addAll(p7.vardefs);
        }
        List<Declaration> result = list();
        result.add(newModelDeclaration(t3.text, toPosition(t3.position), t2, p7.stats, vars));
        return result;
    }

    @Override // CompoundProgram : @MODELKW @IDENTIFIERTK PAROPENTK FormalParameters PARCLOSETK COLONTK Body @ENDKW;
    public List<Declaration> parseCompoundProgram09(Token t1, Token t2, List<VariableDeclaration> l4, ParserBody p7,
            Token t8)
    {
        parser.addFoldRange(t1, t8);
        List<VariableDeclaration> vars = list();
        vars.addAll(l4);
        if (p7.vardefs != null) {
            vars.addAll(p7.vardefs);
        }
        List<Declaration> result = list();
        result.add(newModelDeclaration(t2.text, toPosition(t2.position), null, p7.stats, vars));
        return result;
    }

    @Override // CompoundProgram : @MODELKW Type @IDENTIFIERTK PAROPENTK FormalParameters PARCLOSETK COLONTK Body
              // @ENDKW;
    public List<Declaration> parseCompoundProgram10(Token t1, Type t2, Token t3, List<VariableDeclaration> l5,
            ParserBody p8, Token t9)
    {
        parser.addFoldRange(t1, t9);
        List<VariableDeclaration> vars = list();
        vars.addAll(l5);
        if (p8.vardefs != null) {
            vars.addAll(p8.vardefs);
        }
        List<Declaration> result = list();
        result.add(newModelDeclaration(t3.text, toPosition(t3.position), t2, p8.stats, vars));
        return result;
    }

    @Override // CompoundProgram : @XPERKW @IDENTIFIERTK PAROPENTK PARCLOSETK COLONTK Body @ENDKW;
    public List<Declaration> parseCompoundProgram11(Token t1, Token t2, ParserBody p6, Token t7) {
        parser.addFoldRange(t1, t7);
        List<VariableDeclaration> vars = list();
        if (p6.vardefs != null) {
            vars.addAll(p6.vardefs);
        }
        List<Declaration> result = list();
        result.add(newXperDeclaration(t2.text, toPosition(t2.position), p6.stats, vars));
        return result;
    }

    @Override // CompoundProgram : @XPERKW @IDENTIFIERTK PAROPENTK FormalParameters PARCLOSETK COLONTK Body @ENDKW;
    public List<Declaration> parseCompoundProgram12(Token t1, Token t2, List<VariableDeclaration> l4, ParserBody p7,
            Token t8)
    {
        parser.addFoldRange(t1, t8);
        List<VariableDeclaration> vars = list();
        vars.addAll(l4);
        if (p7.vardefs != null) {
            vars.addAll(p7.vardefs);
        }
        List<Declaration> result = list();
        result.add(newXperDeclaration(t2.text, toPosition(t2.position), p7.stats, vars));
        return result;
    }

    @Override // SimpleProgram : TYPEKW TypeDefList;
    public List<Declaration> parseSimpleProgram1(List<Declaration> l2) {
        return l2;
    }

    @Override // SimpleProgram : CONSTKW ConstantDefList;
    public List<Declaration> parseSimpleProgram2(List<Declaration> l2) {
        return l2;
    }

    @Override // SimpleProgram : ENUMKW EnumDefList;
    public List<Declaration> parseSimpleProgram3(List<Declaration> l2) {
        return l2;
    }

    @Override // TypeDefList : @IDENTIFIERTK BECOMESTK Type;
    public List<Declaration> parseTypeDefList1(Token t1, Type t3) {
        List<Declaration> decls = list();
        decls.add(newTypeDeclaration(t1.text, toPosition(t1.position), t3));
        return decls;
    }

    @Override // TypeDefList : TypeDefList COMMATK @IDENTIFIERTK BECOMESTK Type;
    public List<Declaration> parseTypeDefList2(List<Declaration> l1, Token t3, Type t5) {
        List<Declaration> decls = list();
        decls.addAll(l1);
        decls.add(newTypeDeclaration(t3.text, toPosition(t3.position), t5));
        return decls;
    }

    @Override // ConstantDefList : Type @IDENTIFIERTK BECOMESTK Expression;
    public List<Declaration> parseConstantDefList1(Type t1, Token t2, Expression e4) {
        List<Declaration> decls = list();
        decls.add(newConstantDeclaration(t2.text, toPosition(t2.position), t1, e4));
        return decls;
    }

    @Override // ConstantDefList : ConstantDefList COMMATK Type @IDENTIFIERTK BECOMESTK Expression;
    public List<Declaration> parseConstantDefList2(List<Declaration> l1, Type t3, Token t4, Expression e6) {
        List<Declaration> decls = list();
        decls.addAll(l1);
        decls.add(newConstantDeclaration(t4.text, toPosition(t4.position), t3, e6));
        return decls;
    }

    @Override // EnumDefList : @IDENTIFIERTK BECOMESTK CUROPENTK IdentifierList CURCLOSETK;
    public List<Declaration> parseEnumDefList1(Token t1, List<ParserIdentifier> l4) {
        List<EnumValue> values = list();
        for (ParserIdentifier enumIden: l4) {
            values.add(newEnumValue(enumIden.name, enumIden.pos));
        }
        List<Declaration> decls = list();
        decls.add(newEnumDeclaration(t1.text, toPosition(t1.position), values));
        return decls;
    }

    @Override // EnumDefList : EnumDefList COMMATK @IDENTIFIERTK BECOMESTK CUROPENTK IdentifierList CURCLOSETK;
    public List<Declaration> parseEnumDefList2(List<Declaration> l1, Token t3, List<ParserIdentifier> l6) {
        List<EnumValue> values = list();
        for (ParserIdentifier enumIden: l6) {
            values.add(newEnumValue(enumIden.name, enumIden.pos));
        }
        l1.add(newEnumDeclaration(t3.text, toPosition(t3.position), values));
        return l1;
    }

    @Override // Body : VarDefinitionList StatementList;
    public ParserBody parseBody1(List<VariableDeclaration> l1, List<Statement> l2) {
        return new ParserBody(l1, l2);
    }

    @Override // Body : StatementList;
    public ParserBody parseBody2(List<Statement> l1) {
        return new ParserBody(null, l1);
    }

    @Override // StatementList : EndSimple;
    public List<Statement> parseStatementList1(List<Statement> l1) {
        return l1;
    }

    @Override // StatementList : EndSimple SEMICOLTK;
    public List<Statement> parseStatementList2(List<Statement> l1) {
        return l1;
    }

    @Override // StatementList : EndCompound;
    public List<Statement> parseStatementList3(List<Statement> l1) {
        return l1;
    }

    @Override // StatementList : EndCompound SEMICOLTK;
    public List<Statement> parseStatementList4(List<Statement> l1) {
        return l1;
    }

    @Override // EndSimple : SimpleStatement;
    public List<Statement> parseEndSimple1(Statement s1) {
        return list(s1);
    }

    @Override // EndSimple : EndSimple SEMICOLTK SimpleStatement;
    public List<Statement> parseEndSimple2(List<Statement> l1, Statement s3) {
        l1.add(s3);
        return l1;
    }

    @Override // EndSimple : EndCompound SEMICOLTK SimpleStatement;
    public List<Statement> parseEndSimple3(List<Statement> l1, Statement s3) {
        l1.add(s3);
        return l1;
    }

    @Override // EndSimple : EndCompound SimpleStatement;
    public List<Statement> parseEndSimple4(List<Statement> l1, Statement s2) {
        l1.add(s2);
        return l1;
    }

    @Override // EndCompound : CompoundStatement;
    public List<Statement> parseEndCompound1(Statement s1) {
        return list(s1);
    }

    @Override // EndCompound : EndCompound CompoundStatement;
    public List<Statement> parseEndCompound2(List<Statement> l1, Statement s2) {
        l1.add(s2);
        return l1;
    }

    @Override // EndCompound : EndCompound SEMICOLTK CompoundStatement;
    public List<Statement> parseEndCompound3(List<Statement> l1, Statement s3) {
        l1.add(s3);
        return l1;
    }

    @Override // EndCompound : EndSimple SEMICOLTK CompoundStatement;
    public List<Statement> parseEndCompound4(List<Statement> l1, Statement s3) {
        l1.add(s3);
        return l1;
    }

    @Override // CompoundStatement : @FORKW IdentifierList INKW Expression COLONTK StatementList ENDKW;
    public Statement parseCompoundStatement1(Token t1, List<ParserIdentifier> l2, Expression e4, List<Statement> l6) {
        Unwind unw = newUnwind(copyPosition(first(l2).pos), e4, makeVariables(false, l2, null));
        return newForStatement(l6, toPosition(t1.position), list(unw));
    }

    @Override // CompoundStatement : @WHILEKW Expression COLONTK StatementList ENDKW;
    public Statement parseCompoundStatement2(Token t1, Expression e2, List<Statement> l4) {
        return newWhileStatement(l4, e2, toPosition(t1.position));
    }

    @Override // CompoundStatement : @IFKW Expression COLONTK StatementList ElifsOpt ElseOpt ENDKW;
    public Statement parseCompoundStatement3(Token t1, Expression e2, List<Statement> l4, List<IfCase> l5, IfCase i6) {
        IfCase ifCase = newIfCase(l4, e2, toPosition(t1.position));
        l5.add(0, ifCase);
        if (i6 != null) {
            l5.add(i6);
        }
        return newIfStatement(l5, toPosition(t1.position));
    }

    @Override // CompoundStatement : @SELECTKW Selections ENDKW;
    public Statement parseCompoundStatement4(Token t1, List<SelectCase> l2) {
        return newSelectStatement(l2, toPosition(t1.position));
    }

    @Override // SimpleStatement : @PASSKW;
    public Statement parseSimpleStatement01(Token t1) {
        return newPassStatement(toPosition(t1.position));
    }

    @Override // SimpleStatement : @BREAKKW;
    public Statement parseSimpleStatement02(Token t1) {
        return newBreakStatement(toPosition(t1.position));
    }

    @Override // SimpleStatement : @CONTINUEKW;
    public Statement parseSimpleStatement03(Token t1) {
        return newContinueStatement(toPosition(t1.position));
    }

    @Override // SimpleStatement : ExprList @BECOMESTK ExprList;
    public Statement parseSimpleStatement04(List<Expression> l1, Token t2, List<Expression> l3) {
        return newAssignmentStatement(packExpressions(l1), toPosition(t2.position), packExpressions(l3));
    }

    @Override // SimpleStatement : @DELAYKW Expression;
    public Statement parseSimpleStatement05(Token t1, Expression e2) {
        return newDelayStatement(e2, toPosition(t1.position));
    }

    @Override // SimpleStatement : @RUNKW Instances;
    public Statement parseSimpleStatement06(Token t1, List<CreateCase> l2) {
        return newRunStatement(l2, toPosition(t1.position), false);
    }

    @Override // SimpleStatement : @STARTKW Instances;
    public Statement parseSimpleStatement07(Token t1, List<CreateCase> l2) {
        return newRunStatement(l2, toPosition(t1.position), true);
    }

    @Override // SimpleStatement : @CLOSEKW PAROPENTK Expression PARCLOSETK;
    public Statement parseSimpleStatement08(Token t1, Expression e3) {
        return newCloseStatement(e3, toPosition(t1.position));
    }

    @Override // SimpleStatement : @FINISHKW ExprList;
    public Statement parseSimpleStatement09(Token t1, List<Expression> l2) {
        return newFinishStatement(l2, toPosition(t1.position));
    }

    @Override // SimpleStatement : @WRITEKW PAROPENTK ExprList PARCLOSETK;
    public Statement parseSimpleStatement10(Token t1, List<Expression> l3) {
        return newWriteStatement(false, toPosition(t1.position), l3);
    }

    @Override // SimpleStatement : @WRITELNKW PAROPENTK ExprList PARCLOSETK;
    public Statement parseSimpleStatement11(Token t1, List<Expression> l3) {
        return newWriteStatement(true, toPosition(t1.position), l3);
    }

    @Override // SimpleStatement : @RETURNKW ExprList;
    public Statement parseSimpleStatement12(Token t1, List<Expression> l2) {
        Expression value = packExpressions(l2);
        return newReturnStatement(toPosition(t1.position), value);
    }

    @Override // SimpleStatement : @EXITKW ExprList;
    public Statement parseSimpleStatement13(Token t1, List<Expression> l2) {
        Expression value = packExpressions(l2);
        return newExitStatement(toPosition(t1.position), value);
    }

    @Override // SimpleStatement : @EXITKW;
    public Statement parseSimpleStatement14(Token t1) {
        return newExitStatement(toPosition(t1.position), null);
    }

    @Override // SimpleStatement : CommStatement;
    public Statement parseSimpleStatement15(Statement s1) {
        return s1;
    }

    @Override // CommStatement : Expression @SENDTK;
    public Statement parseCommStatement1(Expression e1, Token t2) {
        return newSendStatement(e1, null, toPosition(t2.position));
    }

    @Override // CommStatement : Expression @SENDTK ExprList;
    public Statement parseCommStatement2(Expression e1, Token t2, List<Expression> l3) {
        Expression value = packExpressions(l3);
        return newSendStatement(e1, value, toPosition(t2.position));
    }

    @Override // CommStatement : Expression @RECEIVETK;
    public Statement parseCommStatement3(Expression e1, Token t2) {
        return newReceiveStatement(e1, null, toPosition(t2.position));
    }

    @Override // CommStatement : Expression @RECEIVETK ExprList;
    public Statement parseCommStatement4(Expression e1, Token t2, List<Expression> l3) {
        Expression value = packExpressions(l3);
        return newReceiveStatement(e1, value, toPosition(t2.position));
    }

    @Override // ElifsOpt : ;
    public List<IfCase> parseElifsOpt1() {
        return list();
    }

    @Override // ElifsOpt : ElifsOpt @ELIFKW Expression COLONTK StatementList;
    public List<IfCase> parseElifsOpt2(List<IfCase> l1, Token t2, Expression e3, List<Statement> l5) {
        IfCase newIf = newIfCase(l5, e3, toPosition(t2.position));
        l1.add(newIf);
        return l1;
    }

    @Override // ElseOpt : ;
    public IfCase parseElseOpt1() {
        return null;
    }

    @Override // ElseOpt : @ELSEKW COLONTK StatementList;
    public IfCase parseElseOpt2(Token t1, List<Statement> l3) {
        return newIfCase(l3, null, toPosition(t1.position));
    }

    @Override // Instances : EndIterated;
    public List<CreateCase> parseInstances1(List<CreateCase> l1) {
        return l1;
    }

    @Override // Instances : EndIterated COMMATK;
    public List<CreateCase> parseInstances2(List<CreateCase> l1) {
        return l1;
    }

    @Override // Instances : EndInstance;
    public List<CreateCase> parseInstances3(List<CreateCase> l1) {
        return l1;
    }

    @Override // Instances : EndInstance COMMATK;
    public List<CreateCase> parseInstances4(List<CreateCase> l1) {
        return l1;
    }

    @Override // Unwinds : UNWINDKW IdentifierList @INKW Expression;
    public List<Unwind> parseUnwinds1(List<ParserIdentifier> l2, Token t3, Expression e4) {
        Unwind unw = newUnwind(toPosition(t3.position), e4, makeVariables(false, l2, null));
        return list(unw);
    }

    @Override // Unwinds : Unwinds COMMATK IdentifierList @INKW Expression;
    public List<Unwind> parseUnwinds2(List<Unwind> l1, List<ParserIdentifier> l3, Token t4, Expression e5) {
        Unwind unw = newUnwind(toPosition(t4.position), e5, makeVariables(false, l3, null));
        l1.add(unw);
        return l1;
    }

    @Override // IteratedInstance : Unwinds @COLONTK Instances ENDKW;
    public CreateCase parseIteratedInstance1(List<Unwind> l1, Token t2, List<CreateCase> l3) {
        return newIteratedCreateCase(l3, toPosition(t2.position), l1);
    }

    @Override // ProcessInstance : Expression;
    public CreateCase parseProcessInstance1(Expression e1) {
        return newProcessInstance(e1, copyPosition(e1.getPosition()), null);
    }

    @Override // ProcessInstance : Expression @BECOMESTK Expression;
    public CreateCase parseProcessInstance2(Expression e1, Token t2, Expression e3) {
        return newProcessInstance(e3, toPosition(t2.position), e1);
    }

    @Override // EndInstance : ProcessInstance;
    public List<CreateCase> parseEndInstance1(CreateCase c1) {
        return list(c1);
    }

    @Override // EndInstance : EndIterated ProcessInstance;
    public List<CreateCase> parseEndInstance2(List<CreateCase> l1, CreateCase c2) {
        l1.add(c2);
        return l1;
    }

    @Override // EndInstance : EndIterated COMMATK ProcessInstance;
    public List<CreateCase> parseEndInstance3(List<CreateCase> l1, CreateCase c3) {
        l1.add(c3);
        return l1;
    }

    @Override // EndInstance : EndInstance COMMATK ProcessInstance;
    public List<CreateCase> parseEndInstance4(List<CreateCase> l1, CreateCase c3) {
        l1.add(c3);
        return l1;
    }

    @Override // EndIterated : IteratedInstance;
    public List<CreateCase> parseEndIterated1(CreateCase c1) {
        return list(c1);
    }

    @Override // EndIterated : EndInstance COMMATK IteratedInstance;
    public List<CreateCase> parseEndIterated2(List<CreateCase> l1, CreateCase c3) {
        l1.add(c3);
        return l1;
    }

    @Override // EndIterated : EndIterated IteratedInstance;
    public List<CreateCase> parseEndIterated3(List<CreateCase> l1, CreateCase c2) {
        l1.add(c2);
        return l1;
    }

    @Override // EndIterated : EndIterated COMMATK IteratedInstance;
    public List<CreateCase> parseEndIterated4(List<CreateCase> l1, CreateCase c3) {
        l1.add(c3);
        return l1;
    }

    @Override // Selections : SelectionAlt;
    public List<SelectCase> parseSelections1(SelectCase s1) {
        return list(s1);
    }

    @Override // Selections : Selections ALTKW SelectionAlt;
    public List<SelectCase> parseSelections2(List<SelectCase> l1, SelectCase s3) {
        l1.add(s3);
        return l1;
    }

    @Override // SelectionAlt : SelectCondition;
    public SelectCase parseSelectionAlt1(ParserSelectCondition p1) {
        List<Statement> statements = list();
        statements.add(p1.stat);
        return newSelectCase(statements, p1.expr, copyPosition(p1.getLeftPosition()));
    }

    @Override // SelectionAlt : SelectCondition @COLONTK StatementList;
    public SelectCase parseSelectionAlt2(ParserSelectCondition p1, Token t2, List<Statement> l3) {
        List<Statement> statements = list();
        statements.add(p1.stat);
        statements.addAll(l3);
        return newSelectCase(statements, p1.expr, toPosition(t2.position));
    }

    @Override // SelectionAlt : Unwinds COLONTK SelectionAlt ENDKW;
    public SelectCase parseSelectionAlt3(List<Unwind> l1, SelectCase s3) {
        List<Unwind> unws = Lists.copy(l1);
        if (s3 instanceof IteratedSelectCase) {
            IteratedSelectCase isc = (IteratedSelectCase)s3;
            unws.addAll(isc.getUnwinds());
        }
        return newIteratedSelectCase(s3.getBody(), s3.getGuard(), s3.getPosition(), unws);
    }

    @Override // SelectCondition : Expression;
    public ParserSelectCondition parseSelectCondition1(Expression e1) {
        Statement stat = newPassStatement(copyPosition(e1.getPosition()));
        return new ParserSelectCondition(e1, stat);
    }

    @Override // SelectCondition : Expression COMMATK CommStatement;
    public ParserSelectCondition parseSelectCondition2(Expression e1, Statement s3) {
        return new ParserSelectCondition(e1, s3);
    }

    @Override // SelectCondition : CommStatement;
    public ParserSelectCondition parseSelectCondition3(Statement s1) {
        return new ParserSelectCondition(null, s1);
    }

    @Override // FormalParameters : Type IdentifierList;
    public List<VariableDeclaration> parseFormalParameters1(Type t1, List<ParserIdentifier> l2) {
        List<VariableDeclaration> vdecls = list();
        vdecls.addAll(makeVariables(true, l2, t1));
        return vdecls;
    }

    @Override // FormalParameters : FormalParameters SEMICOLTK Type IdentifierList;
    public List<VariableDeclaration> parseFormalParameters2(List<VariableDeclaration> l1, Type t3,
            List<ParserIdentifier> l4)
    {
        List<VariableDeclaration> vdecls = list();
        vdecls.addAll(l1);
        vdecls.addAll(makeVariables(true, l4, t3));
        return vdecls;
    }

    @Override // VarDefinitionList : Type VarNames SEMICOLTK;
    public List<VariableDeclaration> parseVarDefinitionList1(Type t1, List<ParserVarValue> l2) {
        List<VariableDeclaration> vdecls = list();
        vdecls.addAll(makeVariables(false, t1, l2));
        return vdecls;
    }

    @Override // VarDefinitionList : VarDefinitionList Type VarNames SEMICOLTK;
    public List<VariableDeclaration> parseVarDefinitionList2(List<VariableDeclaration> l1, Type t2,
            List<ParserVarValue> l3)
    {
        List<VariableDeclaration> vdecls = list();
        vdecls.addAll(l1);
        vdecls.addAll(makeVariables(false, t2, l3));
        return vdecls;
    }

    @Override // VarNames : @IDENTIFIERTK;
    public List<ParserVarValue> parseVarNames1(Token t1) {
        return list(new ParserVarValue(t1.text, toPosition(t1.position), null));
    }

    @Override // VarNames : @IDENTIFIERTK BECOMESTK Expression;
    public List<ParserVarValue> parseVarNames2(Token t1, Expression e3) {
        return list(new ParserVarValue(t1.text, toPosition(t1.position), e3));
    }

    @Override // VarNames : VarNames COMMATK @IDENTIFIERTK;
    public List<ParserVarValue> parseVarNames3(List<ParserVarValue> l1, Token t3) {
        ParserVarValue pvn = new ParserVarValue(t3.text, toPosition(t3.position), null);
        l1.add(pvn);
        return l1;
    }

    @Override // VarNames : VarNames COMMATK @IDENTIFIERTK BECOMESTK Expression;
    public List<ParserVarValue> parseVarNames4(List<ParserVarValue> l1, Token t3, Expression e5) {
        ParserVarValue pvn = new ParserVarValue(t3.text, toPosition(t3.position), e5);
        l1.add(pvn);
        return l1;
    }

    @Override // ElementaryType : @VOIDKW;
    public Type parseElementaryType1(Token t1) {
        return newVoidType(toPosition(t1.position));
    }

    @Override // ElementaryType : @BOOLKW;
    public Type parseElementaryType2(Token t1) {
        return newBoolType(toPosition(t1.position));
    }

    @Override // ElementaryType : @INTKW;
    public Type parseElementaryType3(Token t1) {
        return newIntType(toPosition(t1.position));
    }

    @Override // ElementaryType : @REALKW;
    public Type parseElementaryType4(Token t1) {
        return newRealType(toPosition(t1.position));
    }

    @Override // ElementaryType : @STRINGKW;
    public Type parseElementaryType5(Token t1) {
        return newStringType(toPosition(t1.position));
    }

    @Override // ElementaryType : @FILEKW;
    public Type parseElementaryType6(Token t1) {
        return newFileType(toPosition(t1.position));
    }

    @Override // ElementaryType : @INSTKW;
    public Type parseElementaryType7(Token t1) {
        return newInstanceType(toPosition(t1.position));
    }

    @Override // ElementaryType : @TIMERKW;
    public Type parseElementaryType8(Token t1) {
        return newTimerType(toPosition(t1.position));
    }

    @Override // Type : ElementaryType;
    public Type parseType01(Type t1) {
        return t1;
    }

    @Override // Type : @MATRIXKW PAROPENTK Expression COMMATK Expression PARCLOSETK;
    public Type parseType02(Token t1, Expression e3, Expression e5) {
        return newMatrixType(e5, toPosition(t1.position), e3);
    }

    @Override // Type : @SETKW Type;
    public Type parseType03(Token t1, Type t2) {
        return newSetType(t2, toPosition(t1.position));
    }

    @Override // Type : @LISTKW Type;
    public Type parseType04(Token t1, Type t2) {
        return newListType(t2, null, toPosition(t1.position));
    }

    @Override // Type : @LISTKW PAROPENTK Expression PARCLOSETK Type;
    public Type parseType05(Token t1, Expression e3, Type t5) {
        return newListType(t5, e3, toPosition(t1.position));
    }

    @Override // Type : @CHANKW Type;
    public Type parseType06(Token t1, Type t2) {
        return newChannelType(t2, ChannelOps.SEND_RECEIVE, toPosition(t1.position));
    }

    @Override // Type : @CHANKW RECEIVETK Type;
    public Type parseType07(Token t1, Type t3) {
        return newChannelType(t3, ChannelOps.RECEIVE, toPosition(t1.position));
    }

    @Override // Type : @CHANKW SENDTK Type;
    public Type parseType08(Token t1, Type t3) {
        return newChannelType(t3, ChannelOps.SEND, toPosition(t1.position));
    }

    @Override // Type : @CHANKW SENDRECEIVETK Type;
    public Type parseType09(Token t1, Type t3) {
        return newChannelType(t3, ChannelOps.SEND_RECEIVE, toPosition(t1.position));
    }

    @Override // Type : @DICTKW PAROPENTK Type COLONTK Type PARCLOSETK;
    public Type parseType10(Token t1, Type t3, Type t5) {
        return newDictType(t3, toPosition(t1.position), t5);
    }

    @Override // Type : @TUPLEKW PAROPENTK TupleTypeList PARCLOSETK;
    public Type parseType11(Token t1, List<TupleField> l3) {
        return newTupleType(l3, toPosition(t1.position));
    }

    @Override // Type : @FUNCKW Type PAROPENTK PARCLOSETK;
    public Type parseType12(Token t1, Type t2) {
        return newFunctionType(null, toPosition(t1.position), t2);
    }

    @Override // Type : @FUNCKW Type PAROPENTK TypeList PARCLOSETK;
    public Type parseType13(Token t1, Type t2, List<Type> l4) {
        return newFunctionType(l4, toPosition(t1.position), t2);
    }

    @Override // Type : @PROCKW PAROPENTK TypeList PARCLOSETK;
    public Type parseType14(Token t1, List<Type> l3) {
        return newProcessType(null, l3, toPosition(t1.position));
    }

    @Override // Type : @PROCKW PAROPENTK PARCLOSETK;
    public Type parseType15(Token t1) {
        return newProcessType(null, null, toPosition(t1.position));
    }

    @Override // Type : @PROCKW Type PAROPENTK TypeList PARCLOSETK;
    public Type parseType16(Token t1, Type t2, List<Type> l4) {
        return newProcessType(t2, l4, toPosition(t1.position));
    }

    @Override // Type : @PROCKW Type PAROPENTK PARCLOSETK;
    public Type parseType17(Token t1, Type t2) {
        return newProcessType(t2, null, toPosition(t1.position));
    }

    @Override // Type : @DISTKW Type;
    public Type parseType18(Token t1, Type t2) {
        return newDistributionType(toPosition(t1.position), t2);
    }

    @Override // Type : @IDENTIFIERTK;
    public Type parseType19(Token t1) {
        return newUnresolvedType(t1.text, toPosition(t1.position));
    }

    @Override // TupleTypeList : TupleType;
    public List<TupleField> parseTupleTypeList1(List<TupleField> l1) {
        return l1;
    }

    @Override // TupleTypeList : TupleTypeList SEMICOLTK TupleType;
    public List<TupleField> parseTupleTypeList2(List<TupleField> l1, List<TupleField> l3) {
        l1.addAll(l3);
        return l1;
    }

    @Override // TupleType : Type IdentifierList;
    public List<TupleField> parseTupleType1(Type t1, List<ParserIdentifier> l2) {
        return makeTupleFields(t1, l2);
    }

    @Override // TypeList : Type;
    public List<Type> parseTypeList1(Type t1) {
        return list(t1);
    }

    @Override // TypeList : TypeList COMMATK Type;
    public List<Type> parseTypeList2(List<Type> l1, Type t3) {
        l1.add(t3);
        return l1;
    }

    @Override // IdentifierList : @IDENTIFIERTK;
    public List<ParserIdentifier> parseIdentifierList1(Token t1) {
        ParserIdentifier nm = new ParserIdentifier(t1.text, toPosition(t1.position));
        return list(nm);
    }

    @Override // IdentifierList : IdentifierList COMMATK @IDENTIFIERTK;
    public List<ParserIdentifier> parseIdentifierList2(List<ParserIdentifier> l1, Token t3) {
        ParserIdentifier nm = new ParserIdentifier(t3.text, toPosition(t3.position));
        l1.add(nm);
        return l1;
    }

    @Override // OptExpression : ;
    public Expression parseOptExpression1() {
        return null;
    }

    @Override // OptExpression : Expression;
    public Expression parseOptExpression2(Expression e1) {
        return e1;
    }

    @Override // ExprList : Expression;
    public List<Expression> parseExprList1(Expression e1) {
        return list(e1);
    }

    @Override // ExprList : ExprList COMMATK Expression;
    public List<Expression> parseExprList2(List<Expression> l1, Expression e3) {
        l1.add(e3);
        return l1;
    }

    @Override // MatExprList : ExprList;
    public List<MatrixRow> parseMatExprList1(List<Expression> l1) {
        MatrixRow mr = newMatrixRow(l1, copyPosition(first(l1).getPosition()));
        return list(mr);
    }

    @Override // MatExprList : MatExprList SEMICOLTK ExprList;
    public List<MatrixRow> parseMatExprList2(List<MatrixRow> l1, List<Expression> l3) {
        MatrixRow mr = newMatrixRow(l3, copyPosition(first(l3).getPosition()));
        l1.add(mr);
        return l1;
    }

    @Override // Expression : AndExpression;
    public Expression parseExpression1(Expression e1) {
        return e1;
    }

    @Override // Expression : Expression @ORKW AndExpression;
    public Expression parseExpression2(Expression e1, Token t2, Expression e3) {
        e1 = newBinaryExpression(e1, BinaryOperators.DISJUNCTION, toPosition(t2.position), e3, null);
        return e1;
    }

    @Override // AndExpression : NotExpression;
    public Expression parseAndExpression1(Expression e1) {
        return e1;
    }

    @Override // AndExpression : AndExpression @ANDKW NotExpression;
    public Expression parseAndExpression2(Expression e1, Token t2, Expression e3) {
        e1 = newBinaryExpression(e1, BinaryOperators.CONJUNCTION, toPosition(t2.position), e3, null);
        return e1;
    }

    @Override // NotExpression : CompareExpression;
    public Expression parseNotExpression1(Expression e1) {
        return e1;
    }

    @Override // NotExpression : @NOTKW NotExpression;
    public Expression parseNotExpression2(Token t1, Expression e2) {
        return newUnaryExpression(e2, UnaryOperators.INVERSE, toPosition(t1.position), null);
    }

    @Override // CompareExpression : AddExpression;
    public Expression parseCompareExpression1(Expression e1) {
        return e1;
    }

    @Override // CompareExpression : CompareExpression @LTTK AddExpression;
    public Expression parseCompareExpression2(Expression e1, Token t2, Expression e3) {
        e1 = newBinaryExpression(e1, BinaryOperators.LESS_THAN, toPosition(t2.position), e3, null);
        return e1;
    }

    @Override // CompareExpression : CompareExpression @LETK AddExpression;
    public Expression parseCompareExpression3(Expression e1, Token t2, Expression e3) {
        e1 = newBinaryExpression(e1, BinaryOperators.LESS_EQUAL, toPosition(t2.position), e3, null);
        return e1;
    }

    @Override // CompareExpression : CompareExpression @EQTK AddExpression;
    public Expression parseCompareExpression4(Expression e1, Token t2, Expression e3) {
        e1 = newBinaryExpression(e1, BinaryOperators.EQUAL, toPosition(t2.position), e3, null);
        return e1;
    }

    @Override // CompareExpression : CompareExpression @NETK AddExpression;
    public Expression parseCompareExpression5(Expression e1, Token t2, Expression e3) {
        e1 = newBinaryExpression(e1, BinaryOperators.NOT_EQUAL, toPosition(t2.position), e3, null);
        return e1;
    }

    @Override // CompareExpression : CompareExpression @GETK AddExpression;
    public Expression parseCompareExpression6(Expression e1, Token t2, Expression e3) {
        e1 = newBinaryExpression(e1, BinaryOperators.GREATER_EQUAL, toPosition(t2.position), e3, null);
        return e1;
    }

    @Override // CompareExpression : CompareExpression @GTTK AddExpression;
    public Expression parseCompareExpression7(Expression e1, Token t2, Expression e3) {
        e1 = newBinaryExpression(e1, BinaryOperators.GREATER_THAN, toPosition(t2.position), e3, null);
        return e1;
    }

    @Override // CompareExpression : CompareExpression @INKW AddExpression;
    public Expression parseCompareExpression8(Expression e1, Token t2, Expression e3) {
        e1 = newBinaryExpression(e1, BinaryOperators.ELEMENT_TEST, toPosition(t2.position), e3, null);
        return e1;
    }

    @Override // CompareExpression : CompareExpression @SUBKW AddExpression;
    public Expression parseCompareExpression9(Expression e1, Token t2, Expression e3) {
        e1 = newBinaryExpression(e1, BinaryOperators.SUBSET, toPosition(t2.position), e3, null);
        return e1;
    }

    @Override // AddExpression : MulExpression;
    public Expression parseAddExpression1(Expression e1) {
        return e1;
    }

    @Override // AddExpression : AddExpression @PLUSTK MulExpression;
    public Expression parseAddExpression2(Expression e1, Token t2, Expression e3) {
        e1 = newBinaryExpression(e1, BinaryOperators.ADDITION, toPosition(t2.position), e3, null);
        return e1;
    }

    @Override // AddExpression : AddExpression @MINUSTK MulExpression;
    public Expression parseAddExpression3(Expression e1, Token t2, Expression e3) {
        e1 = newBinaryExpression(e1, BinaryOperators.SUBTRACTION, toPosition(t2.position), e3, null);
        return e1;
    }

    @Override // MulExpression : PowerExpression;
    public Expression parseMulExpression1(Expression e1) {
        return e1;
    }

    @Override // MulExpression : MulExpression @ASTERISKTK PowerExpression;
    public Expression parseMulExpression2(Expression e1, Token t2, Expression e3) {
        e1 = newBinaryExpression(e1, BinaryOperators.MULTIPLICATION, toPosition(t2.position), e3, null);
        return e1;
    }

    @Override // MulExpression : MulExpression @SLASHTK PowerExpression;
    public Expression parseMulExpression3(Expression e1, Token t2, Expression e3) {
        e1 = newBinaryExpression(e1, BinaryOperators.DIVISION, toPosition(t2.position), e3, null);
        return e1;
    }

    @Override // MulExpression : MulExpression @DIVKW PowerExpression;
    public Expression parseMulExpression4(Expression e1, Token t2, Expression e3) {
        e1 = newBinaryExpression(e1, BinaryOperators.FLOOR_DIVISION, toPosition(t2.position), e3, null);
        return e1;
    }

    @Override // MulExpression : MulExpression @MODKW PowerExpression;
    public Expression parseMulExpression5(Expression e1, Token t2, Expression e3) {
        e1 = newBinaryExpression(e1, BinaryOperators.MODULUS, toPosition(t2.position), e3, null);
        return e1;
    }

    @Override // PowerExpression : UnaryExpression;
    public Expression parsePowerExpression1(Expression e1) {
        return e1;
    }

    @Override // PowerExpression : PowerExpression @CARETTK UnaryExpression;
    public Expression parsePowerExpression2(Expression e1, Token t2, Expression e3) {
        e1 = newBinaryExpression(e1, BinaryOperators.POWER, toPosition(t2.position), e3, null);
        return e1;
    }

    @Override // UnaryExpression : SampleExpression;
    public Expression parseUnaryExpression1(Expression e1) {
        return e1;
    }

    @Override // UnaryExpression : @MINUSTK UnaryExpression;
    public Expression parseUnaryExpression2(Token t1, Expression e2) {
        return newUnaryExpression(e2, UnaryOperators.NEGATE, toPosition(t1.position), null);
    }

    @Override // UnaryExpression : @PLUSTK UnaryExpression;
    public Expression parseUnaryExpression3(Token t1, Expression e2) {
        return newUnaryExpression(e2, UnaryOperators.PLUS, toPosition(t1.position), null);
    }

    @Override // SampleExpression : FuncExpression;
    public Expression parseSampleExpression1(Expression e1) {
        return e1;
    }

    @Override // SampleExpression : @SAMPLEKW FuncExpression;
    public Expression parseSampleExpression2(Token t1, Expression e2) {
        return newUnaryExpression(e2, UnaryOperators.SAMPLE, toPosition(t1.position), null);
    }

    @Override // FuncExpression : ExpressionFactor;
    public Expression parseFuncExpression01(Expression e1) {
        return e1;
    }

    @Override // FuncExpression : @READKW PAROPENTK Type PARCLOSETK;
    public Expression parseFuncExpression02(Token t1, Type t3) {
        return newReadCallExpression(null, t3, toPosition(t1.position), null);
    }

    @Override // FuncExpression : @READKW PAROPENTK Expression COMMATK Type PARCLOSETK;
    public Expression parseFuncExpression03(Token t1, Expression e3, Type t5) {
        return newReadCallExpression(e3, t5, toPosition(t1.position), null);
    }

    @Override // FuncExpression : @CHANNELKW PAROPENTK Type PARCLOSETK;
    public Expression parseFuncExpression04(Token t1, Type t3) {
        return newChannelExpression(t3, toPosition(t1.position), null);
    }

    @Override // FuncExpression : FuncExpression @DOTTK @IDENTIFIERTK;
    public Expression parseFuncExpression05(Expression e1, Token t2, Token t3) {
        Expression right = newUnresolvedReference(t3.text, toPosition(t3.position), null);
        return newBinaryExpression(e1, BinaryOperators.FIELD_PROJECTION, toPosition(t2.position), right, null);
    }

    @Override // FuncExpression : FuncExpression @SQOPENTK Expression SQCLOSETK;
    public Expression parseFuncExpression06(Expression e1, Token t2, Expression e3) {
        return newBinaryExpression(e1, BinaryOperators.PROJECTION, toPosition(t2.position), e3, null);
    }

    @Override // FuncExpression : FuncExpression @SQOPENTK OptExpression COLONTK OptExpression SQCLOSETK;
    public Expression parseFuncExpression07(Expression e1, Token t2, Expression e3, Expression e5) {
        return newSliceExpression(e5, toPosition(t2.position), e1, e3, null, null);
    }

    @Override // FuncExpression : FuncExpression @SQOPENTK OptExpression COLONTK OptExpression COLONTK OptExpression
              // SQCLOSETK;
    public Expression parseFuncExpression08(Expression e1, Token t2, Expression e3, Expression e5, Expression e7) {
        return newSliceExpression(e5, toPosition(t2.position), e1, e3, e7, null);
    }

    @Override // FuncExpression : FuncExpression CUROPENTK Expression CURCLOSETK @PAROPENTK ExprList PARCLOSETK;
    public Expression parseFuncExpression09(Expression e1, Expression e3, Token t5, List<Expression> l6) {
        return newCallExpression(l6, e1, e3, toPosition(t5.position), null);
    }

    @Override // FuncExpression : FuncExpression CUROPENTK Expression CURCLOSETK @PAROPENTK PARCLOSETK;
    public Expression parseFuncExpression10(Expression e1, Expression e3, Token t5) {
        return newCallExpression(null, e1, e3, toPosition(t5.position), null);
    }

    @Override // FuncExpression : FuncExpression @PAROPENTK ExprList PARCLOSETK;
    public Expression parseFuncExpression11(Expression e1, Token t2, List<Expression> l3) {
        return newCallExpression(l3, e1, null, toPosition(t2.position), null);
    }

    @Override // FuncExpression : FuncExpression @PAROPENTK PARCLOSETK;
    public Expression parseFuncExpression12(Expression e1, Token t2) {
        return newCallExpression(null, e1, null, toPosition(t2.position), null);
    }

    @Override // ExpressionFactor : @TIMEKW;
    public Expression parseExpressionFactor01(Token t1) {
        return newTimeLiteral(toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @TRUEKW;
    public Expression parseExpressionFactor02(Token t1) {
        return newBoolLiteral(toPosition(t1.position), null, true);
    }

    @Override // ExpressionFactor : @FALSEKW;
    public Expression parseExpressionFactor03(Token t1) {
        return newBoolLiteral(toPosition(t1.position), null, false);
    }

    @Override // ExpressionFactor : @NUMBERTK;
    public Expression parseExpressionFactor04(Token t1) {
        return newIntNumber(toPosition(t1.position), null, t1.text);
    }

    @Override // ExpressionFactor : @REALTK;
    public Expression parseExpressionFactor05(Token t1) {
        return newRealNumber(toPosition(t1.position), null, t1.text);
    }

    @Override // ExpressionFactor : @STRINGTK;
    public Expression parseExpressionFactor06(Token t1) {
        return newStringLiteral(toPosition(t1.position), null, unescape(t1.text));
    }

    @Override // ExpressionFactor : LTTK Type GTTK @SQOPENTK SQCLOSETK;
    public Expression parseExpressionFactor07(Type t2, Token t4) {
        t2 = newListType(t2, null, toPosition(t4.position));
        return newListExpression(null, toPosition(t4.position), t2);
    }

    @Override // ExpressionFactor : @SQOPENTK MatExprList SQCLOSETK;
    public Expression parseExpressionFactor08(Token t1, List<MatrixRow> l2) {
        if (l2.size() == 1) {
            return newListExpression(l2.get(0).getElements(), toPosition(t1.position), null);
        } else {
            return newMatrixExpression(toPosition(t1.position), l2, null);
        }
    }

    @Override // ExpressionFactor : LTTK Type GTTK @CUROPENTK CURCLOSETK;
    public Expression parseExpressionFactor09(Type t2, Token t4) {
        t2 = newSetType(t2, toPosition(t4.position));
        return newSetExpression(null, toPosition(t4.position), t2);
    }

    @Override // ExpressionFactor : LTTK Type COLONTK Type GTTK @CUROPENTK CURCLOSETK;
    public Expression parseExpressionFactor10(Type t2, Type t4, Token t6) {
        Type dtype = newDictType(t2, toPosition(t6.position), t4);
        return newDictionaryExpression(null, toPosition(t6.position), dtype);
    }

    @Override // ExpressionFactor : @CUROPENTK ExprList CURCLOSETK;
    public Expression parseExpressionFactor11(Token t1, List<Expression> l2) {
        return newSetExpression(l2, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @CUROPENTK DictPairs CURCLOSETK;
    public Expression parseExpressionFactor12(Token t1, List<DictionaryPair> l2) {
        return newDictionaryExpression(l2, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : PAROPENTK ExprList PARCLOSETK;
    public Expression parseExpressionFactor13(List<Expression> l2) {
        return packExpressions(l2);
    }

    @Override // ExpressionFactor : @IDENTIFIERTK;
    public Expression parseExpressionFactor14(Token t1) {
        return newUnresolvedReference(t1.text, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : ElementaryType @PAROPENTK Expression PARCLOSETK;
    public Expression parseExpressionFactor15(Type t1, Token t2, Expression e3) {
        return newCastExpression(t1, e3, toPosition(t2.position), null);
    }

    @Override // ExpressionFactor : @MATRIXKW PAROPENTK Expression COMMATK Expression COMMATK Expression PARCLOSETK;
    public Expression parseExpressionFactor16(Token t1, Expression e3, Expression e5, Expression e7) {
        Type matType = newMatrixType(e5, toPosition(t1.position), e3);
        return newCastExpression(matType, e7, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @SETKW PAROPENTK Expression PARCLOSETK;
    public Expression parseExpressionFactor17(Token t1, Expression e3) {
        Type setType = newSetType(null, toPosition(t1.position));
        return newCastExpression(setType, e3, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @ABSKW;
    public Expression parseExpressionFactor18(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.ABS, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @SIGNKW;
    public Expression parseExpressionFactor19(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.SIGN, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @MAXKW;
    public Expression parseExpressionFactor20(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.MAX, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @MINKW;
    public Expression parseExpressionFactor21(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.MIN, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @CEILKW;
    public Expression parseExpressionFactor22(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.CEIL, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @FLOORKW;
    public Expression parseExpressionFactor23(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.FLOOR, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @ROUNDKW;
    public Expression parseExpressionFactor24(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.ROUND, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @CBRTKW;
    public Expression parseExpressionFactor25(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.CBRT, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @SQRTKW;
    public Expression parseExpressionFactor26(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.SQRT, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @EXPKW;
    public Expression parseExpressionFactor27(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.EXP, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @LNKW;
    public Expression parseExpressionFactor28(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.LN, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @LOGKW;
    public Expression parseExpressionFactor29(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.LOG, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @ACOSHKW;
    public Expression parseExpressionFactor30(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.ACOSH, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @ACOSKW;
    public Expression parseExpressionFactor31(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.ACOS, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @ASINHKW;
    public Expression parseExpressionFactor32(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.ASINH, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @ASINKW;
    public Expression parseExpressionFactor33(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.ASIN, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @ATANHKW;
    public Expression parseExpressionFactor34(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.ATANH, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @ATANKW;
    public Expression parseExpressionFactor35(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.ATAN, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @COSHKW;
    public Expression parseExpressionFactor36(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.COSH, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @COSKW;
    public Expression parseExpressionFactor37(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.COS, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @SINHKW;
    public Expression parseExpressionFactor38(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.SINH, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @SINKW;
    public Expression parseExpressionFactor39(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.SIN, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @TANHKW;
    public Expression parseExpressionFactor40(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.TANH, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @TANKW;
    public Expression parseExpressionFactor41(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.TAN, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @BERNOULLIKW;
    public Expression parseExpressionFactor42(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.BERNOULLI, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @BETAKW;
    public Expression parseExpressionFactor43(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.BETA, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @BINOMIALKW;
    public Expression parseExpressionFactor44(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.BINOMIAL, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @CONSTANTKW;
    public Expression parseExpressionFactor45(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.CONSTANT, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @LOGNORMALKW;
    public Expression parseExpressionFactor46(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.LOG_NORMAL, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @EXPONENTIALKW;
    public Expression parseExpressionFactor47(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.EXPONENTIAL, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @ERLANGKW;
    public Expression parseExpressionFactor48(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.ERLANG, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @GAMMAKW;
    public Expression parseExpressionFactor49(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.GAMMA, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @GEOMETRICKW;
    public Expression parseExpressionFactor50(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.GEOMETRIC, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @NORMALKW;
    public Expression parseExpressionFactor51(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.NORMAL, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @POISSONKW;
    public Expression parseExpressionFactor52(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.POISSON, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @RANDOMKW;
    public Expression parseExpressionFactor53(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.RANDOM, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @TRIANGLEKW;
    public Expression parseExpressionFactor54(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.TRIANGLE, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @UNIFORMKW;
    public Expression parseExpressionFactor55(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.UNIFORM, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @WEIBULLKW;
    public Expression parseExpressionFactor56(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.WEIBULL, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @DICTKEYSKW;
    public Expression parseExpressionFactor57(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.DICT_KEYS, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @DICTVALUESKW;
    public Expression parseExpressionFactor58(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.DICT_VALUES, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @EMPTYKW;
    public Expression parseExpressionFactor59(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.EMPTY, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @SIZEKW;
    public Expression parseExpressionFactor60(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.SIZE, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @POPKW;
    public Expression parseExpressionFactor61(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.POP, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @ENUMERATEKW;
    public Expression parseExpressionFactor62(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.ENUMERATE, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @RANGEKW;
    public Expression parseExpressionFactor63(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.RANGE, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @DELKW;
    public Expression parseExpressionFactor64(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.DELETE, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @INSERTKW;
    public Expression parseExpressionFactor65(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.INSERT, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @SORTKW;
    public Expression parseExpressionFactor66(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.SORT, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @FINISHEDKW;
    public Expression parseExpressionFactor67(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.FINISHED, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @READYKW;
    public Expression parseExpressionFactor68(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.READY, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @OPENKW;
    public Expression parseExpressionFactor69(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.OPEN, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @EOLKW;
    public Expression parseExpressionFactor70(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.EOL, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @EOFKW;
    public Expression parseExpressionFactor71(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.EOF, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @NEWLINESKW;
    public Expression parseExpressionFactor72(Token t1) {
        return newStdLibFunctionReference(StdLibFunctions.NEWLINES, toPosition(t1.position), null);
    }

    @Override // DictPairs : Expression @COLONTK Expression;
    public List<DictionaryPair> parseDictPairs1(Expression e1, Token t2, Expression e3) {
        return list(newDictionaryPair(e1, toPosition(t2.position), e3));
    }

    @Override // DictPairs : DictPairs COMMATK Expression @COLONTK Expression;
    public List<DictionaryPair> parseDictPairs2(List<DictionaryPair> l1, Expression e3, Token t4, Expression e5) {
        l1.add(newDictionaryPair(e3, toPosition(t4.position), e5));
        return l1;
    }

    /**
     * Pack one or more expressions together into a tuple if necessary.
     *
     * @param exprs Expressions to combine to a single value.
     * @return The combined single value.
     */
    private Expression packExpressions(List<Expression> exprs) {
        if (exprs.size() == 1) {
            return first(exprs);
        }
        Position pos = copyPosition(first(exprs).getPosition());
        return newTupleExpression(exprs, pos, null);
    }

    /**
     * Construct a sequence of data variables from a sequence of names (identifiers).
     *
     * @param isParam Variable names are parameters of a process/model/function.
     * @param names Sequence of names.
     * @param tp Tpye of the names (may be null).
     * @return Sequence of untyped uninitialized data variables.
     */
    private List<VariableDeclaration> makeVariables(boolean isParam, List<ParserIdentifier> names, Type tp) {
        List<VariableDeclaration> vars = list();
        for (ParserIdentifier pIden: names) {
            if (tp != null && !vars.isEmpty()) {
                tp = deepclone(tp);
            }
            VariableDeclaration vd = newVariableDeclaration(null, pIden.name, isParam, pIden.pos, tp);
            vars.add(vd);
        }
        return vars;
    }

    /**
     * Unfold variable names to a list of variable declarations.
     *
     * @param isParam Variable names are parameters of a process/model/function.
     * @param type Type of the var names.
     * @param vnames Variable names to unfold.
     * @return Variable declarations containing the variable names.
     */
    private List<VariableDeclaration> makeVariables(boolean isParam, Type type, List<ParserVarValue> vnames) {
        List<VariableDeclaration> vdecls = list();
        for (ParserVarValue vname: vnames) {
            if (!vdecls.isEmpty()) {
                type = deepclone(type);
            }
            VariableDeclaration vd = newVariableDeclaration(vname.expr, vname.name, isParam, vname.pos, type);
            vdecls.add(vd);
        }
        return vdecls;
    }

    /**
     * Construct tuple fields from a type and a list names.
     *
     * @param type Type of each name.
     * @param names Names to unfold.
     * @return Tuple fields.
     */
    private List<TupleField> makeTupleFields(Type type, List<ParserIdentifier> names) {
        List<TupleField> fields = list();
        for (ParserIdentifier pIden: names) {
            if (!fields.isEmpty()) {
                type = deepclone(type);
            }
            TupleField tf = newTupleField(pIden.name, copyPosition(pIden.pos), type);
            fields.add(tf);
        }
        return fields;
    }
}
