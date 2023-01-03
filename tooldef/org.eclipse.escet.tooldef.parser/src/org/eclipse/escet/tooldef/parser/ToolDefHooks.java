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

package org.eclipse.escet.tooldef.parser;

import static org.eclipse.escet.common.emf.EMFHelper.deepclone;
import static org.eclipse.escet.common.java.Lists.copy;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.position.common.PositionUtils.toPosition;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newAssignmentStatement;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newBoolExpression;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newBoolType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newBreakStatement;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newCastExpression;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newContinueStatement;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newDoubleExpression;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newDoubleType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newElifStatement;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newEmptySetMapExpression;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newExitStatement;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newForStatement;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newIfStatement;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newIntType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newJavaImport;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newListExpression;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newListType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newLongType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newMapEntry;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newMapExpression;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newMapType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newNullExpression;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newNumberExpression;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newObjectType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newProjectionExpression;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newReturnStatement;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newScript;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newSetExpression;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newSetType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newSliceExpression;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newStringExpression;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newStringType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newToolArgument;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newToolDefImport;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newToolDefTool;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newToolInvokeExpression;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newToolInvokeStatement;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newToolParameter;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newToolRef;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newTupleAddressableDecl;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newTupleExpression;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newTupleType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newTypeDecl;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newTypeParam;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newUnresolvedRefExpression;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newUnresolvedType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newVariable;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newVariableAddressableDecl;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newWhileStatement;

import java.util.List;

import org.eclipse.escet.common.java.Strings;
import org.eclipse.escet.common.position.common.PositionUtils;
import org.eclipse.escet.setext.runtime.Parser;
import org.eclipse.escet.setext.runtime.Token;
import org.eclipse.escet.tooldef.metamodel.tooldef.Declaration;
import org.eclipse.escet.tooldef.metamodel.tooldef.Script;
import org.eclipse.escet.tooldef.metamodel.tooldef.ToolDefImport;
import org.eclipse.escet.tooldef.metamodel.tooldef.ToolParameter;
import org.eclipse.escet.tooldef.metamodel.tooldef.TypeDecl;
import org.eclipse.escet.tooldef.metamodel.tooldef.TypeParam;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.Expression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.MapEntry;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ProjectionExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolArgument;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolInvokeExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolRef;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.AddressableDecl;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.ElifStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.Statement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.Variable;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.ToolDefType;

/**
 * Call back hook methods for:
 * <ul>
 * <li>{@link ToolDefScanner}</li>
 * <li>{@link ToolDefParser}</li>
 * </ul>
 */
public final class ToolDefHooks implements ToolDefScanner.Hooks, ToolDefParser.Hooks {
    /** The parser that owns the call back hooks. */
    private Parser<?> parser;

    @Override
    public void setParser(Parser<?> parser) {
        this.parser = parser;
    }

    @Override
    public void processStringLiteral(Token token) {
        // Remove outer double quotes, and unescape.
        token.text = Strings.unescape(Strings.slice(token.text, 1, -1));
    }

    @Override
    public void stripDollar(Token token) {
        token.text = token.text.replace("$", "");
    }

    @Override // BuiltInIoTool : @ERRKW;
    public Token parseBuiltInIoTool1(Token t1) {
        return t1;
    }

    @Override // BuiltInIoTool : @ERRLNKW;
    public Token parseBuiltInIoTool2(Token t1) {
        return t1;
    }

    @Override // BuiltInIoTool : @OUTKW;
    public Token parseBuiltInIoTool3(Token t1) {
        return t1;
    }

    @Override // BuiltInIoTool : @OUTLNKW;
    public Token parseBuiltInIoTool4(Token t1) {
        return t1;
    }

    @Override // BuiltInGenericTool : @APPKW;
    public Token parseBuiltInGenericTool1(Token t1) {
        return t1;
    }

    @Override // BuiltInGenericTool : @EXECKW;
    public Token parseBuiltInGenericTool2(Token t1) {
        return t1;
    }

    @Override // BuiltInGenericTool : @TOOLDEFKW;
    public Token parseBuiltInGenericTool3(Token t1) {
        return t1;
    }

    @Override // BuiltInPathTool : @ABSPATHKW;
    public Token parseBuiltInPathTool01(Token t1) {
        return t1;
    }

    @Override // BuiltInPathTool : @BASENAMEKW;
    public Token parseBuiltInPathTool02(Token t1) {
        return t1;
    }

    @Override // BuiltInPathTool : @CHDIRKW;
    public Token parseBuiltInPathTool03(Token t1) {
        return t1;
    }

    @Override // BuiltInPathTool : @CHFILEEXTKW;
    public Token parseBuiltInPathTool04(Token t1) {
        return t1;
    }

    @Override // BuiltInPathTool : @CURDIRKW;
    public Token parseBuiltInPathTool05(Token t1) {
        return t1;
    }

    @Override // BuiltInPathTool : @DIRNAMEKW;
    public Token parseBuiltInPathTool06(Token t1) {
        return t1;
    }

    @Override // BuiltInPathTool : @FILEEXTKW;
    public Token parseBuiltInPathTool07(Token t1) {
        return t1;
    }

    @Override // BuiltInPathTool : @HASFILEEXTKW;
    public Token parseBuiltInPathTool08(Token t1) {
        return t1;
    }

    @Override // BuiltInPathTool : @PATHJOINKW;
    public Token parseBuiltInPathTool09(Token t1) {
        return t1;
    }

    @Override // BuiltInPathTool : @SCRIPTPATHKW;
    public Token parseBuiltInPathTool10(Token t1) {
        return t1;
    }

    @Override // BuiltInFileTool : @CPDIRKW;
    public Token parseBuiltInFileTool01(Token t1) {
        return t1;
    }

    @Override // BuiltInFileTool : @CPFILEKW;
    public Token parseBuiltInFileTool02(Token t1) {
        return t1;
    }

    @Override // BuiltInFileTool : @DIFFKW;
    public Token parseBuiltInFileTool03(Token t1) {
        return t1;
    }

    @Override // BuiltInFileTool : @EXISTSKW;
    public Token parseBuiltInFileTool04(Token t1) {
        return t1;
    }

    @Override // BuiltInFileTool : @FILENEWERKW;
    public Token parseBuiltInFileTool05(Token t1) {
        return t1;
    }

    @Override // BuiltInFileTool : @FILESIZEKW;
    public Token parseBuiltInFileTool06(Token t1) {
        return t1;
    }

    @Override // BuiltInFileTool : @FINDKW;
    public Token parseBuiltInFileTool07(Token t1) {
        return t1;
    }

    @Override // BuiltInFileTool : @ISDIRKW;
    public Token parseBuiltInFileTool08(Token t1) {
        return t1;
    }

    @Override // BuiltInFileTool : @ISFILEKW;
    public Token parseBuiltInFileTool09(Token t1) {
        return t1;
    }

    @Override // BuiltInFileTool : @MKDIRKW;
    public Token parseBuiltInFileTool10(Token t1) {
        return t1;
    }

    @Override // BuiltInFileTool : @MVDIRKW;
    public Token parseBuiltInFileTool11(Token t1) {
        return t1;
    }

    @Override // BuiltInFileTool : @MVFILEKW;
    public Token parseBuiltInFileTool12(Token t1) {
        return t1;
    }

    @Override // BuiltInFileTool : @READLINESKW;
    public Token parseBuiltInFileTool13(Token t1) {
        return t1;
    }

    @Override // BuiltInFileTool : @RMDIRKW;
    public Token parseBuiltInFileTool14(Token t1) {
        return t1;
    }

    @Override // BuiltInFileTool : @RMFILEKW;
    public Token parseBuiltInFileTool15(Token t1) {
        return t1;
    }

    @Override // BuiltInFileTool : @WRITEFILEKW;
    public Token parseBuiltInFileTool16(Token t1) {
        return t1;
    }

    @Override // BuiltInDataTool : @ABSKW;
    public Token parseBuiltInDataTool01(Token t1) {
        return t1;
    }

    @Override // BuiltInDataTool : @CEILKW;
    public Token parseBuiltInDataTool02(Token t1) {
        return t1;
    }

    @Override // BuiltInDataTool : @CONTAINSKW;
    public Token parseBuiltInDataTool03(Token t1) {
        return t1;
    }

    @Override // BuiltInDataTool : @DELKW;
    public Token parseBuiltInDataTool04(Token t1) {
        return t1;
    }

    @Override // BuiltInDataTool : @DELIDXKW;
    public Token parseBuiltInDataTool05(Token t1) {
        return t1;
    }

    @Override // BuiltInDataTool : @EMPTYKW;
    public Token parseBuiltInDataTool06(Token t1) {
        return t1;
    }

    @Override // BuiltInDataTool : @ENDSWITHKW;
    public Token parseBuiltInDataTool07(Token t1) {
        return t1;
    }

    @Override // BuiltInDataTool : @ENTRIESKW;
    public Token parseBuiltInDataTool08(Token t1) {
        return t1;
    }

    @Override // BuiltInDataTool : @ENUMERATEKW;
    public Token parseBuiltInDataTool09(Token t1) {
        return t1;
    }

    @Override // BuiltInDataTool : @FLOORKW;
    public Token parseBuiltInDataTool10(Token t1) {
        return t1;
    }

    @Override // BuiltInDataTool : @FMTKW;
    public Token parseBuiltInDataTool11(Token t1) {
        return t1;
    }

    @Override // BuiltInDataTool : @INDEXOFKW;
    public Token parseBuiltInDataTool12(Token t1) {
        return t1;
    }

    @Override // BuiltInDataTool : @JOINKW;
    public Token parseBuiltInDataTool13(Token t1) {
        return t1;
    }

    @Override // BuiltInDataTool : @KEYSKW;
    public Token parseBuiltInDataTool14(Token t1) {
        return t1;
    }

    @Override // BuiltInDataTool : @LASTINDEXOFKW;
    public Token parseBuiltInDataTool15(Token t1) {
        return t1;
    }

    @Override // BuiltInDataTool : @LNKW;
    public Token parseBuiltInDataTool16(Token t1) {
        return t1;
    }

    @Override // BuiltInDataTool : @LOGKW;
    public Token parseBuiltInDataTool17(Token t1) {
        return t1;
    }

    @Override // BuiltInDataTool : @LOWERKW;
    public Token parseBuiltInDataTool18(Token t1) {
        return t1;
    }

    @Override // BuiltInDataTool : @LTRIMKW;
    public Token parseBuiltInDataTool19(Token t1) {
        return t1;
    }

    @Override // BuiltInDataTool : @MAXKW;
    public Token parseBuiltInDataTool20(Token t1) {
        return t1;
    }

    @Override // BuiltInDataTool : @MINKW;
    public Token parseBuiltInDataTool21(Token t1) {
        return t1;
    }

    @Override // BuiltInDataTool : @POWKW;
    public Token parseBuiltInDataTool22(Token t1) {
        return t1;
    }

    @Override // BuiltInDataTool : @RANGEKW;
    public Token parseBuiltInDataTool23(Token t1) {
        return t1;
    }

    @Override // BuiltInDataTool : @REPLACEKW;
    public Token parseBuiltInDataTool24(Token t1) {
        return t1;
    }

    @Override // BuiltInDataTool : @REVERSEKW;
    public Token parseBuiltInDataTool25(Token t1) {
        return t1;
    }

    @Override // BuiltInDataTool : @ROUNDKW;
    public Token parseBuiltInDataTool26(Token t1) {
        return t1;
    }

    @Override // BuiltInDataTool : @RTRIMKW;
    public Token parseBuiltInDataTool27(Token t1) {
        return t1;
    }

    @Override // BuiltInDataTool : @SIZEKW;
    public Token parseBuiltInDataTool28(Token t1) {
        return t1;
    }

    @Override // BuiltInDataTool : @SORTEDKW;
    public Token parseBuiltInDataTool29(Token t1) {
        return t1;
    }

    @Override // BuiltInDataTool : @SPLITKW;
    public Token parseBuiltInDataTool30(Token t1) {
        return t1;
    }

    @Override // BuiltInDataTool : @SQRTKW;
    public Token parseBuiltInDataTool31(Token t1) {
        return t1;
    }

    @Override // BuiltInDataTool : @STARTSWITHKW;
    public Token parseBuiltInDataTool32(Token t1) {
        return t1;
    }

    @Override // BuiltInDataTool : @STRKW;
    public Token parseBuiltInDataTool33(Token t1) {
        return t1;
    }

    @Override // BuiltInDataTool : @STRDUPKW;
    public Token parseBuiltInDataTool34(Token t1) {
        return t1;
    }

    @Override // BuiltInDataTool : @SUBSETKW;
    public Token parseBuiltInDataTool35(Token t1) {
        return t1;
    }

    @Override // BuiltInDataTool : @TRIMKW;
    public Token parseBuiltInDataTool36(Token t1) {
        return t1;
    }

    @Override // BuiltInDataTool : @UPPERKW;
    public Token parseBuiltInDataTool37(Token t1) {
        return t1;
    }

    @Override // BuiltInDataTool : @VALUESKW;
    public Token parseBuiltInDataTool38(Token t1) {
        return t1;
    }

    @Override // Script : ;
    public Script parseScript1() {
        String src = parser.getSource();
        String loc = parser.getLocation();
        return newScript(null, null, PositionUtils.createDummy(loc, src));
    }

    @Override // Script : Decls;
    public Script parseScript2(List<Declaration> l1) {
        String src = parser.getSource();
        String loc = parser.getLocation();
        return newScript(l1, null, PositionUtils.createDummy(loc, src));
    }

    @Override // Decls : Decl;
    public List<Declaration> parseDecls1(List<Declaration> l1) {
        return l1;
    }

    @Override // Decls : Decls Decl;
    public List<Declaration> parseDecls2(List<Declaration> l1, List<Declaration> l2) {
        l1.addAll(l2);
        return l1;
    }

    @Override // Decl : Import;
    public List<Declaration> parseDecl1(List<Declaration> l1) {
        return l1;
    }

    @Override // Decl : TYPEKW TypeDecls SEMICOLTK;
    public List<Declaration> parseDecl2(List<Declaration> l2) {
        return l2;
    }

    @Override // Decl : @TOOLKW @IDENTIFIERTK OptTypeParams ToolParameters COLONTK OptStatements @ENDKW;
    public List<Declaration> parseDecl3(Token t1, Token t2, List<TypeParam> l3, List<ToolParameter> l4,
            List<Statement> l6, Token t7)
    {
        parser.addFoldRange(t1.position, t7.position);
        return list(newToolDefTool(t2.text, l4, toPosition(t2.position), null, l6, l3));
    }

    @Override // Decl : @TOOLKW Types @IDENTIFIERTK OptTypeParams ToolParameters COLONTK OptStatements @ENDKW;
    public List<Declaration> parseDecl4(Token t1, List<ToolDefType> l2, Token t3, List<TypeParam> l4,
            List<ToolParameter> l5, List<Statement> l7, Token t8)
    {
        parser.addFoldRange(t1.position, t8.position);
        return list(newToolDefTool(t3.text, l5, toPosition(t3.position), l2, l7, l4));
    }

    @Override // Decl : Statement;
    public List<Declaration> parseDecl5(List<Statement> l1) {
        return copy(l1); // Needed to convert List<Statement> to List<Declaration>.
    }

    @Override // Import : IMPORTKW @STRINGTK SEMICOLTK;
    public List<Declaration> parseImport1(Token t2) {
        return list(newToolDefImport(null, null, toPosition(t2.position), t2));
    }

    @Override // Import : IMPORTKW @STRINGTK ASKW @IDENTIFIERTK SEMICOLTK;
    public List<Declaration> parseImport2(Token t2, Token t4) {
        return list(newToolDefImport(t4, null, toPosition(t4.position), t2));
    }

    @Override // Import : IMPORTKW Name SEMICOLTK;
    public List<Declaration> parseImport3(Token t2) {
        return list(newJavaImport(null, t2, null, toPosition(t2.position)));
    }

    @Override // Import : IMPORTKW Name ASKW @IDENTIFIERTK SEMICOLTK;
    public List<Declaration> parseImport4(Token t2, Token t4) {
        return list(newJavaImport(t4, t2, null, toPosition(t4.position)));
    }

    @Override // Import : IMPORTKW Name COLONTK Name SEMICOLTK;
    public List<Declaration> parseImport5(Token t2, Token t4) {
        return list(newJavaImport(null, t4, t2, toPosition(t4.position)));
    }

    @Override // Import : IMPORTKW Name COLONTK Name ASKW @IDENTIFIERTK SEMICOLTK;
    public List<Declaration> parseImport6(Token t2, Token t4, Token t6) {
        return list(newJavaImport(t6, t4, t2, toPosition(t6.position)));
    }

    @Override // Import : FROMKW @STRINGTK IMPORTKW ImportParts SEMICOLTK;
    public List<Declaration> parseImport7(Token t2, List<Declaration> l4) {
        for (Declaration decl: l4) {
            // No need to clone a 'Token' as it is a data-type.
            ((ToolDefImport)decl).setSource(t2);
        }
        return l4;
    }

    @Override // ImportParts : @ASTERISKTK;
    public List<Declaration> parseImportParts1(Token t1) {
        // The 'source' is set later.
        return list(newToolDefImport(null, t1, toPosition(t1.position), null));
    }

    @Override // ImportParts : @IDENTIFIERTK;
    public List<Declaration> parseImportParts2(Token t1) {
        // The 'source' is set later.
        return list(newToolDefImport(null, t1, toPosition(t1.position), null));
    }

    @Override // ImportParts : @IDENTIFIERTK ASKW @IDENTIFIERTK;
    public List<Declaration> parseImportParts3(Token t1, Token t3) {
        // The 'source' is set later.
        return list(newToolDefImport(t3, t1, toPosition(t3.position), null));
    }

    @Override // ImportParts : ImportParts COMMATK @ASTERISKTK;
    public List<Declaration> parseImportParts4(List<Declaration> l1, Token t3) {
        // The 'source' is set later.
        l1.add(newToolDefImport(null, t3, toPosition(t3.position), null));
        return l1;
    }

    @Override // ImportParts : ImportParts COMMATK @IDENTIFIERTK;
    public List<Declaration> parseImportParts5(List<Declaration> l1, Token t3) {
        // The 'source' is set later.
        l1.add(newToolDefImport(null, t3, toPosition(t3.position), null));
        return l1;
    }

    @Override // ImportParts : ImportParts COMMATK @IDENTIFIERTK ASKW @IDENTIFIERTK;
    public List<Declaration> parseImportParts6(List<Declaration> l1, Token t3, Token t5) {
        // The 'source' is set later.
        l1.add(newToolDefImport(t5, t3, toPosition(t5.position), null));
        return l1;
    }

    @Override // TypeDecls : TypeDecl;
    public List<Declaration> parseTypeDecls1(TypeDecl t1) {
        return list(t1);
    }

    @Override // TypeDecls : TypeDecls COMMATK TypeDecl;
    public List<Declaration> parseTypeDecls2(List<Declaration> l1, TypeDecl t3) {
        l1.add(t3);
        return l1;
    }

    @Override // TypeDecl : @IDENTIFIERTK EQTK Type;
    public TypeDecl parseTypeDecl1(Token t1, ToolDefType t3) {
        return newTypeDecl(t1.text, toPosition(t1.position), t3);
    }

    @Override // OptTypeParams : ;
    public List<TypeParam> parseOptTypeParams1() {
        return list();
    }

    @Override // OptTypeParams : LTTK Names GTTK;
    public List<TypeParam> parseOptTypeParams2(List<Token> l2) {
        List<TypeParam> rslt = listc(l2.size());
        for (Token token: l2) {
            rslt.add(newTypeParam(token.text, toPosition(token.position)));
        }
        return rslt;
    }

    @Override // ToolParameters : PAROPENTK PARCLOSETK;
    public List<ToolParameter> parseToolParameters1() {
        return list();
    }

    @Override // ToolParameters : PAROPENTK ToolParams PARCLOSETK;
    public List<ToolParameter> parseToolParameters2(List<ToolParameter> l2) {
        return l2;
    }

    @Override // ToolParams : Type ToolParam;
    public List<ToolParameter> parseToolParams1(ToolDefType t1, ToolParameter t2) {
        t2.setType(deepclone(t1));
        return list(t2);
    }

    @Override // ToolParams : Type DOTSTK ToolParam;
    public List<ToolParameter> parseToolParams2(ToolDefType t1, ToolParameter t3) {
        t3.setType(deepclone(t1));
        t3.setVariadic(true);
        return list(t3);
    }

    @Override // ToolParams : ToolParams COMMATK Type ToolParam;
    public List<ToolParameter> parseToolParams3(List<ToolParameter> l1, ToolDefType t3, ToolParameter t4) {
        t4.setType(deepclone(t3));
        l1.add(t4);
        return l1;
    }

    @Override // ToolParams : ToolParams COMMATK Type DOTSTK ToolParam;
    public List<ToolParameter> parseToolParams4(List<ToolParameter> l1, ToolDefType t3, ToolParameter t5) {
        t5.setType(deepclone(t3));
        t5.setVariadic(true);
        l1.add(t5);
        return l1;
    }

    @Override // ToolParam : @IDENTIFIERTK;
    public ToolParameter parseToolParam1(Token t1) {
        // The 'type' and 'variadic' are set later.
        return newToolParameter(t1.text, toPosition(t1.position), null, null, false);
    }

    @Override // ToolParam : @IDENTIFIERTK @EQTK Expression;
    public ToolParameter parseToolParam2(Token t1, Token t2, Expression e3) {
        // The 'type' and 'variadic' are set later.
        return newToolParameter(t1.text, toPosition(t1.position), null, e3, false);
    }

    @Override // OptStatements : ;
    public List<Statement> parseOptStatements1() {
        return list();
    }

    @Override // OptStatements : OptStatements Statement;
    public List<Statement> parseOptStatements2(List<Statement> l1, List<Statement> l2) {
        l1.addAll(l2);
        return l1;
    }

    @Override // Statement : Type VarDecls SEMICOLTK;
    public List<Statement> parseStatement01(ToolDefType t1, List<Statement> l2) {
        for (Statement vdecl: l2) {
            ((Variable)vdecl).setType(deepclone(t1));
        }
        return l2;
    }

    @Override // Statement : @WHILEKW Expression COLONTK OptStatements @ENDKW;
    public List<Statement> parseStatement02(Token t1, Expression e2, List<Statement> l4, Token t5) {
        parser.addFoldRange(t1.position, t5.position);
        return list(newWhileStatement(e2, toPosition(t1.position), l4));
    }

    @Override // Statement : @WHILEKW Expression COLONCOLONTK Statement;
    public List<Statement> parseStatement03(Token t1, Expression e2, List<Statement> l4) {
        return list(newWhileStatement(e2, toPosition(t1.position), l4));
    }

    @Override // Statement : @FORKW AddressableDecls INKW Expression COLONTK OptStatements @ENDKW;
    public List<Statement> parseStatement04(Token t1, List<AddressableDecl> l2, Expression e4, List<Statement> l6,
            Token t7)
    {
        parser.addFoldRange(t1.position, t7.position);
        return list(newForStatement(l2, toPosition(t1.position), e4, l6));
    }

    @Override // Statement : @FORKW AddressableDecls INKW Expression COLONCOLONTK Statement;
    public List<Statement> parseStatement05(Token t1, List<AddressableDecl> l2, Expression e4, List<Statement> l6) {
        return list(newForStatement(l2, toPosition(t1.position), e4, l6));
    }

    @Override // Statement : @IFKW Expression COLONTK OptStatements OptElifStatements OptElseStatement @ENDKW;
    public List<Statement> parseStatement06(Token t1, Expression e2, List<Statement> l4, List<ElifStatement> l5,
            List<Statement> l6, Token t7)
    {
        parser.addFoldRange(t1.position, t7.position);
        return list(newIfStatement(e2, l5, l6, toPosition(t1.position), l4));
    }

    @Override // Statement : @IFKW Expression COLONCOLONTK Statement;
    public List<Statement> parseStatement07(Token t1, Expression e2, List<Statement> l4) {
        return list(newIfStatement(e2, null, null, toPosition(t1.position), l4));
    }

    @Override // Statement : @BREAKKW SEMICOLTK;
    public List<Statement> parseStatement08(Token t1) {
        return list(newBreakStatement(toPosition(t1.position)));
    }

    @Override // Statement : @CONTINUEKW SEMICOLTK;
    public List<Statement> parseStatement09(Token t1) {
        return list(newContinueStatement(toPosition(t1.position)));
    }

    @Override // Statement : Addressables @EQTK Expressions SEMICOLTK;
    public List<Statement> parseStatement10(List<Expression> l1, Token t2, List<Expression> l3) {
        return list(newAssignmentStatement(l1, toPosition(t2.position), l3));
    }

    @Override // Statement : @RETURNKW SEMICOLTK;
    public List<Statement> parseStatement11(Token t1) {
        return list(newReturnStatement(toPosition(t1.position), null));
    }

    @Override // Statement : @RETURNKW Expressions SEMICOLTK;
    public List<Statement> parseStatement12(Token t1, List<Expression> l2) {
        return list(newReturnStatement(toPosition(t1.position), l2));
    }

    @Override // Statement : ToolInvokeExpression SEMICOLTK;
    public List<Statement> parseStatement13(ToolInvokeExpression t1) {
        return list(newToolInvokeStatement(t1, deepclone(t1.getPosition())));
    }

    @Override // Statement : @EXITKW SEMICOLTK;
    public List<Statement> parseStatement14(Token t1) {
        return list(newExitStatement(null, toPosition(t1.position)));
    }

    @Override // Statement : @EXITKW Expression SEMICOLTK;
    public List<Statement> parseStatement15(Token t1, Expression e2) {
        return list(newExitStatement(e2, toPosition(t1.position)));
    }

    @Override // VarDecls : VarDecl;
    public List<Statement> parseVarDecls1(Statement s1) {
        return list(s1);
    }

    @Override // VarDecls : VarDecls COMMATK VarDecl;
    public List<Statement> parseVarDecls2(List<Statement> l1, Statement s3) {
        l1.add(s3);
        return l1;
    }

    @Override // VarDecl : @IDENTIFIERTK;
    public Statement parseVarDecl1(Token t1) {
        // The 'type' is set later.
        return newVariable(t1.text, toPosition(t1.position), null, null);
    }

    @Override // VarDecl : @IDENTIFIERTK @EQTK Expression;
    public Statement parseVarDecl2(Token t1, Token t2, Expression e3) {
        // The 'type' is set later.
        return newVariable(t1.text, toPosition(t1.position), null, e3);
    }

    @Override // OptElifStatements : ;
    public List<ElifStatement> parseOptElifStatements1() {
        return list();
    }

    @Override // OptElifStatements : OptElifStatements @ELIFKW Expression COLONTK OptStatements;
    public List<ElifStatement> parseOptElifStatements2(List<ElifStatement> l1, Token t2, Expression e3,
            List<Statement> l5)
    {
        l1.add(newElifStatement(e3, toPosition(t2.position), l5));
        return l1;
    }

    @Override // OptElseStatement : ;
    public List<Statement> parseOptElseStatement1() {
        return list();
    }

    @Override // OptElseStatement : ELSEKW OptStatements;
    public List<Statement> parseOptElseStatement2(List<Statement> l2) {
        return l2;
    }

    @Override // AddressableDecls : AddressableDecl;
    public List<AddressableDecl> parseAddressableDecls1(AddressableDecl a1) {
        return list(a1);
    }

    @Override // AddressableDecls : AddressableDecls COMMATK AddressableDecl;
    public List<AddressableDecl> parseAddressableDecls2(List<AddressableDecl> l1, AddressableDecl a3) {
        l1.add(a3);
        return l1;
    }

    @Override // AddressableDecl : @IDENTIFIERTK;
    public AddressableDecl parseAddressableDecl1(Token t1) {
        Variable var = newVariable(t1.text, toPosition(t1.position), null, null);
        return newVariableAddressableDecl(deepclone(var.getPosition()), var);
    }

    @Override // AddressableDecl : @PAROPENTK AddressableDecl COMMATK AddressableDecls PARCLOSETK;
    public AddressableDecl parseAddressableDecl2(Token t1, AddressableDecl a2, List<AddressableDecl> l4) {
        l4.add(0, a2);
        return newTupleAddressableDecl(l4, toPosition(t1.position));
    }

    @Override // Addressables : Addressable;
    public List<Expression> parseAddressables1(Expression e1) {
        return list(e1);
    }

    @Override // Addressables : Addressables COMMATK Addressable;
    public List<Expression> parseAddressables2(List<Expression> l1, Expression e3) {
        l1.add(e3);
        return l1;
    }

    @Override // Addressable : @IDENTIFIERTK;
    public Expression parseAddressable1(Token t1) {
        return newUnresolvedRefExpression(t1.text, toPosition(t1.position), null);
    }

    @Override // Addressable : @IDENTIFIERTK Projections;
    public Expression parseAddressable2(Token t1, List<Expression> l2) {
        Expression rslt = newUnresolvedRefExpression(t1.text, toPosition(t1.position), null);
        for (Expression idx: l2) {
            ((ProjectionExpression)idx).setChild(rslt);
            rslt = idx;
        }
        return rslt;
    }

    @Override // Addressable : @PAROPENTK Addressable COMMATK Addressables PARCLOSETK;
    public Expression parseAddressable3(Token t1, Expression e2, List<Expression> l4) {
        l4.add(0, e2);
        return newTupleExpression(l4, toPosition(t1.position), null);
    }

    @Override // Projections : Projection;
    public List<Expression> parseProjections1(Expression e1) {
        return list(e1);
    }

    @Override // Projections : Projections Projection;
    public List<Expression> parseProjections2(List<Expression> l1, Expression e2) {
        l1.add(e2);
        return l1;
    }

    @Override // Projection : @SQOPENTK Expression SQCLOSETK;
    public Expression parseProjection1(Token t1, Expression e2) {
        // The 'child' is set later.
        return newProjectionExpression(null, e2, toPosition(t1.position), null);
    }

    @Override // Types : Type;
    public List<ToolDefType> parseTypes1(ToolDefType t1) {
        return list(t1);
    }

    @Override // Types : Types COMMATK Type;
    public List<ToolDefType> parseTypes2(List<ToolDefType> l1, ToolDefType t3) {
        l1.add(t3);
        return l1;
    }

    @Override // Type : @BOOLKW;
    public ToolDefType parseType01(Token t1) {
        return newBoolType(false, toPosition(t1.position));
    }

    @Override // Type : @BOOLKW QUESTIONTK;
    public ToolDefType parseType02(Token t1) {
        return newBoolType(true, toPosition(t1.position));
    }

    @Override // Type : @INTKW;
    public ToolDefType parseType03(Token t1) {
        return newIntType(false, toPosition(t1.position));
    }

    @Override // Type : @INTKW QUESTIONTK;
    public ToolDefType parseType04(Token t1) {
        return newIntType(true, toPosition(t1.position));
    }

    @Override // Type : @LONGKW;
    public ToolDefType parseType05(Token t1) {
        return newLongType(false, toPosition(t1.position));
    }

    @Override // Type : @LONGKW QUESTIONTK;
    public ToolDefType parseType06(Token t1) {
        return newLongType(true, toPosition(t1.position));
    }

    @Override // Type : @DOUBLEKW;
    public ToolDefType parseType07(Token t1) {
        return newDoubleType(false, toPosition(t1.position));
    }

    @Override // Type : @DOUBLEKW QUESTIONTK;
    public ToolDefType parseType08(Token t1) {
        return newDoubleType(true, toPosition(t1.position));
    }

    @Override // Type : @STRINGKW;
    public ToolDefType parseType09(Token t1) {
        return newStringType(false, toPosition(t1.position));
    }

    @Override // Type : @STRINGKW QUESTIONTK;
    public ToolDefType parseType10(Token t1) {
        return newStringType(true, toPosition(t1.position));
    }

    @Override // Type : @LISTKW Type;
    public ToolDefType parseType11(Token t1, ToolDefType t2) {
        return newListType(t2, false, toPosition(t1.position));
    }

    @Override // Type : @LISTKW QUESTIONTK Type;
    public ToolDefType parseType12(Token t1, ToolDefType t3) {
        return newListType(t3, true, toPosition(t1.position));
    }

    @Override // Type : @SETKW Type;
    public ToolDefType parseType13(Token t1, ToolDefType t2) {
        return newSetType(t2, false, toPosition(t1.position));
    }

    @Override // Type : @SETKW QUESTIONTK Type;
    public ToolDefType parseType14(Token t1, ToolDefType t3) {
        return newSetType(t3, true, toPosition(t1.position));
    }

    @Override // Type : @MAPKW PAROPENTK Type COLONTK Type PARCLOSETK;
    public ToolDefType parseType15(Token t1, ToolDefType t3, ToolDefType t5) {
        return newMapType(t3, false, toPosition(t1.position), t5);
    }

    @Override // Type : @MAPKW QUESTIONTK PAROPENTK Type COLONTK Type PARCLOSETK;
    public ToolDefType parseType16(Token t1, ToolDefType t4, ToolDefType t6) {
        return newMapType(t4, true, toPosition(t1.position), t6);
    }

    @Override // Type : @TUPLEKW PAROPENTK Type COMMATK Types PARCLOSETK;
    public ToolDefType parseType17(Token t1, ToolDefType t3, List<ToolDefType> l5) {
        l5.add(0, t3);
        return newTupleType(l5, false, toPosition(t1.position));
    }

    @Override // Type : @TUPLEKW QUESTIONTK PAROPENTK Type COMMATK Types PARCLOSETK;
    public ToolDefType parseType18(Token t1, ToolDefType t4, List<ToolDefType> l6) {
        l6.add(0, t4);
        return newTupleType(l6, true, toPosition(t1.position));
    }

    @Override // Type : @OBJECTKW;
    public ToolDefType parseType19(Token t1) {
        return newObjectType(false, toPosition(t1.position));
    }

    @Override // Type : @OBJECTKW QUESTIONTK;
    public ToolDefType parseType20(Token t1) {
        return newObjectType(true, toPosition(t1.position));
    }

    @Override // Type : Name;
    public ToolDefType parseType21(Token t1) {
        // The 'nullable' metamodel feature is ignored for type references, and
        // can't be specific in the syntax.
        return newUnresolvedType(t1.text, false, toPosition(t1.position));
    }

    @Override // Expressions : Expression;
    public List<Expression> parseExpressions1(Expression e1) {
        return list(e1);
    }

    @Override // Expressions : Expressions COMMATK Expression;
    public List<Expression> parseExpressions2(List<Expression> l1, Expression e3) {
        l1.add(e3);
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

    @Override // Expression : AndExpression;
    public Expression parseExpression1(Expression e1) {
        return e1;
    }

    @Override // Expression : Expression @ORKW AndExpression;
    public Expression parseExpression2(Expression e1, Token t2, Expression e3) {
        ToolRef tool = newToolRef(true, t2.text, toPosition(t2.position), null);
        ToolArgument arg1 = newToolArgument(null, deepclone(e1.getPosition()), e1);
        ToolArgument arg2 = newToolArgument(null, deepclone(e3.getPosition()), e3);
        return newToolInvokeExpression(list(arg1, arg2), deepclone(tool.getPosition()), tool, null);
    }

    @Override // AndExpression : CompareExpression;
    public Expression parseAndExpression1(Expression e1) {
        return e1;
    }

    @Override // AndExpression : AndExpression @ANDKW CompareExpression;
    public Expression parseAndExpression2(Expression e1, Token t2, Expression e3) {
        ToolRef tool = newToolRef(true, t2.text, toPosition(t2.position), null);
        ToolArgument arg1 = newToolArgument(null, deepclone(e1.getPosition()), e1);
        ToolArgument arg2 = newToolArgument(null, deepclone(e3.getPosition()), e3);
        return newToolInvokeExpression(list(arg1, arg2), deepclone(tool.getPosition()), tool, null);
    }

    @Override // CompareExpression : AddExpression;
    public Expression parseCompareExpression1(Expression e1) {
        return e1;
    }

    @Override // CompareExpression : CompareExpression @LTTK AddExpression;
    public Expression parseCompareExpression2(Expression e1, Token t2, Expression e3) {
        ToolRef tool = newToolRef(true, t2.text, toPosition(t2.position), null);
        ToolArgument arg1 = newToolArgument(null, deepclone(e1.getPosition()), e1);
        ToolArgument arg2 = newToolArgument(null, deepclone(e3.getPosition()), e3);
        return newToolInvokeExpression(list(arg1, arg2), deepclone(tool.getPosition()), tool, null);
    }

    @Override // CompareExpression : CompareExpression @LETK AddExpression;
    public Expression parseCompareExpression3(Expression e1, Token t2, Expression e3) {
        ToolRef tool = newToolRef(true, t2.text, toPosition(t2.position), null);
        ToolArgument arg1 = newToolArgument(null, deepclone(e1.getPosition()), e1);
        ToolArgument arg2 = newToolArgument(null, deepclone(e3.getPosition()), e3);
        return newToolInvokeExpression(list(arg1, arg2), deepclone(tool.getPosition()), tool, null);
    }

    @Override // CompareExpression : CompareExpression @EQEQTK AddExpression;
    public Expression parseCompareExpression4(Expression e1, Token t2, Expression e3) {
        ToolRef tool = newToolRef(true, t2.text, toPosition(t2.position), null);
        ToolArgument arg1 = newToolArgument(null, deepclone(e1.getPosition()), e1);
        ToolArgument arg2 = newToolArgument(null, deepclone(e3.getPosition()), e3);
        return newToolInvokeExpression(list(arg1, arg2), deepclone(tool.getPosition()), tool, null);
    }

    @Override // CompareExpression : CompareExpression @NETK AddExpression;
    public Expression parseCompareExpression5(Expression e1, Token t2, Expression e3) {
        ToolRef tool = newToolRef(true, t2.text, toPosition(t2.position), null);
        ToolArgument arg1 = newToolArgument(null, deepclone(e1.getPosition()), e1);
        ToolArgument arg2 = newToolArgument(null, deepclone(e3.getPosition()), e3);
        return newToolInvokeExpression(list(arg1, arg2), deepclone(tool.getPosition()), tool, null);
    }

    @Override // CompareExpression : CompareExpression @GETK AddExpression;
    public Expression parseCompareExpression6(Expression e1, Token t2, Expression e3) {
        ToolRef tool = newToolRef(true, t2.text, toPosition(t2.position), null);
        ToolArgument arg1 = newToolArgument(null, deepclone(e1.getPosition()), e1);
        ToolArgument arg2 = newToolArgument(null, deepclone(e3.getPosition()), e3);
        return newToolInvokeExpression(list(arg1, arg2), deepclone(tool.getPosition()), tool, null);
    }

    @Override // CompareExpression : CompareExpression @GTTK AddExpression;
    public Expression parseCompareExpression7(Expression e1, Token t2, Expression e3) {
        ToolRef tool = newToolRef(true, t2.text, toPosition(t2.position), null);
        ToolArgument arg1 = newToolArgument(null, deepclone(e1.getPosition()), e1);
        ToolArgument arg2 = newToolArgument(null, deepclone(e3.getPosition()), e3);
        return newToolInvokeExpression(list(arg1, arg2), deepclone(tool.getPosition()), tool, null);
    }

    @Override // AddExpression : MulExpression;
    public Expression parseAddExpression1(Expression e1) {
        return e1;
    }

    @Override // AddExpression : AddExpression @MINUSTK MulExpression;
    public Expression parseAddExpression2(Expression e1, Token t2, Expression e3) {
        ToolRef tool = newToolRef(true, t2.text, toPosition(t2.position), null);
        ToolArgument arg1 = newToolArgument(null, deepclone(e1.getPosition()), e1);
        ToolArgument arg2 = newToolArgument(null, deepclone(e3.getPosition()), e3);
        return newToolInvokeExpression(list(arg1, arg2), deepclone(tool.getPosition()), tool, null);
    }

    @Override // AddExpression : AddExpression @PLUSTK MulExpression;
    public Expression parseAddExpression3(Expression e1, Token t2, Expression e3) {
        ToolRef tool = newToolRef(true, t2.text, toPosition(t2.position), null);
        ToolArgument arg1 = newToolArgument(null, deepclone(e1.getPosition()), e1);
        ToolArgument arg2 = newToolArgument(null, deepclone(e3.getPosition()), e3);
        return newToolInvokeExpression(list(arg1, arg2), deepclone(tool.getPosition()), tool, null);
    }

    @Override // MulExpression : UnaryExpression;
    public Expression parseMulExpression1(Expression e1) {
        return e1;
    }

    @Override // MulExpression : MulExpression @ASTERISKTK UnaryExpression;
    public Expression parseMulExpression2(Expression e1, Token t2, Expression e3) {
        ToolRef tool = newToolRef(true, t2.text, toPosition(t2.position), null);
        ToolArgument arg1 = newToolArgument(null, deepclone(e1.getPosition()), e1);
        ToolArgument arg2 = newToolArgument(null, deepclone(e3.getPosition()), e3);
        return newToolInvokeExpression(list(arg1, arg2), deepclone(tool.getPosition()), tool, null);
    }

    @Override // MulExpression : MulExpression @SLASHTK UnaryExpression;
    public Expression parseMulExpression3(Expression e1, Token t2, Expression e3) {
        ToolRef tool = newToolRef(true, t2.text, toPosition(t2.position), null);
        ToolArgument arg1 = newToolArgument(null, deepclone(e1.getPosition()), e1);
        ToolArgument arg2 = newToolArgument(null, deepclone(e3.getPosition()), e3);
        return newToolInvokeExpression(list(arg1, arg2), deepclone(tool.getPosition()), tool, null);
    }

    @Override // MulExpression : MulExpression @DIVKW UnaryExpression;
    public Expression parseMulExpression4(Expression e1, Token t2, Expression e3) {
        ToolRef tool = newToolRef(true, t2.text, toPosition(t2.position), null);
        ToolArgument arg1 = newToolArgument(null, deepclone(e1.getPosition()), e1);
        ToolArgument arg2 = newToolArgument(null, deepclone(e3.getPosition()), e3);
        return newToolInvokeExpression(list(arg1, arg2), deepclone(tool.getPosition()), tool, null);
    }

    @Override // MulExpression : MulExpression @MODKW UnaryExpression;
    public Expression parseMulExpression5(Expression e1, Token t2, Expression e3) {
        ToolRef tool = newToolRef(true, t2.text, toPosition(t2.position), null);
        ToolArgument arg1 = newToolArgument(null, deepclone(e1.getPosition()), e1);
        ToolArgument arg2 = newToolArgument(null, deepclone(e3.getPosition()), e3);
        return newToolInvokeExpression(list(arg1, arg2), deepclone(tool.getPosition()), tool, null);
    }

    @Override // UnaryExpression : ProjExpression;
    public Expression parseUnaryExpression1(Expression e1) {
        return e1;
    }

    @Override // UnaryExpression : @MINUSTK UnaryExpression;
    public Expression parseUnaryExpression2(Token t1, Expression e2) {
        ToolRef tool = newToolRef(true, t1.text, toPosition(t1.position), null);
        ToolArgument arg = newToolArgument(null, deepclone(e2.getPosition()), e2);
        return newToolInvokeExpression(list(arg), deepclone(tool.getPosition()), tool, null);
    }

    @Override // UnaryExpression : @PLUSTK UnaryExpression;
    public Expression parseUnaryExpression3(Token t1, Expression e2) {
        ToolRef tool = newToolRef(true, t1.text, toPosition(t1.position), null);
        ToolArgument arg = newToolArgument(null, deepclone(e2.getPosition()), e2);
        return newToolInvokeExpression(list(arg), deepclone(tool.getPosition()), tool, null);
    }

    @Override // UnaryExpression : @NOTKW UnaryExpression;
    public Expression parseUnaryExpression4(Token t1, Expression e2) {
        ToolRef tool = newToolRef(true, t1.text, toPosition(t1.position), null);
        ToolArgument arg = newToolArgument(null, deepclone(e2.getPosition()), e2);
        return newToolInvokeExpression(list(arg), deepclone(tool.getPosition()), tool, null);
    }

    @Override // ProjExpression : ExpressionFactor;
    public Expression parseProjExpression1(Expression e1) {
        return e1;
    }

    @Override // ProjExpression : ProjExpression @SQOPENTK Expression SQCLOSETK;
    public Expression parseProjExpression2(Expression e1, Token t2, Expression e3) {
        return newProjectionExpression(e1, e3, toPosition(t2.position), null);
    }

    @Override // ProjExpression : ProjExpression @SQOPENTK OptExpression COLONTK OptExpression SQCLOSETK;
    public Expression parseProjExpression3(Expression e1, Token t2, Expression e3, Expression e5) {
        return newSliceExpression(e3, e1, e5, toPosition(t2.position), null);
    }

    @Override // ExpressionFactor : @TRUEKW;
    public Expression parseExpressionFactor01(Token t1) {
        return newBoolExpression(toPosition(t1.position), null, true);
    }

    @Override // ExpressionFactor : @FALSEKW;
    public Expression parseExpressionFactor02(Token t1) {
        return newBoolExpression(toPosition(t1.position), null, false);
    }

    @Override // ExpressionFactor : @NUMBERTK;
    public Expression parseExpressionFactor03(Token t1) {
        return newNumberExpression(toPosition(t1.position), null, t1.text);
    }

    @Override // ExpressionFactor : @DOUBLETK;
    public Expression parseExpressionFactor04(Token t1) {
        return newDoubleExpression(toPosition(t1.position), null, t1.text);
    }

    @Override // ExpressionFactor : @NULLKW;
    public Expression parseExpressionFactor05(Token t1) {
        return newNullExpression(toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @STRINGTK;
    public Expression parseExpressionFactor06(Token t1) {
        return newStringExpression(toPosition(t1.position), null, t1.text);
    }

    @Override // ExpressionFactor : @LTTK Type GTTK ExpressionFactor;
    public Expression parseExpressionFactor07(Token t1, ToolDefType t2, Expression e4) {
        return newCastExpression(e4, toPosition(t1.position), t2);
    }

    @Override // ExpressionFactor : @SQOPENTK SQCLOSETK;
    public Expression parseExpressionFactor08(Token t1) {
        List<Expression> elements = listc(0);
        return newListExpression(elements, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @SQOPENTK Expressions OptComma SQCLOSETK;
    public Expression parseExpressionFactor09(Token t1, List<Expression> l2, Token t3) {
        return newListExpression(l2, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @CUROPENTK CURCLOSETK;
    public Expression parseExpressionFactor10(Token t1) {
        return newEmptySetMapExpression(toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @CUROPENTK Expressions OptComma CURCLOSETK;
    public Expression parseExpressionFactor11(Token t1, List<Expression> l2, Token t3) {
        return newSetExpression(l2, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @CUROPENTK MapEntries OptComma CURCLOSETK;
    public Expression parseExpressionFactor12(Token t1, List<MapEntry> l2, Token t3) {
        return newMapExpression(l2, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : @PAROPENTK Expression COMMATK Expressions OptComma PARCLOSETK;
    public Expression parseExpressionFactor13(Token t1, Expression e2, List<Expression> l4, Token t5) {
        l4.add(0, e2);
        return newTupleExpression(l4, toPosition(t1.position), null);
    }

    @Override // ExpressionFactor : PAROPENTK Expression PARCLOSETK;
    public Expression parseExpressionFactor14(Expression e2) {
        return e2;
    }

    @Override // ExpressionFactor : ToolInvokeExpression;
    public Expression parseExpressionFactor15(ToolInvokeExpression t1) {
        return t1;
    }

    @Override // ExpressionFactor : Name;
    public Expression parseExpressionFactor16(Token t1) {
        return newUnresolvedRefExpression(t1.text, toPosition(t1.position), null);
    }

    @Override // MapEntries : Expression @COLONTK Expression;
    public List<MapEntry> parseMapEntries1(Expression e1, Token t2, Expression e3) {
        return list(newMapEntry(e1, toPosition(t2.position), e3));
    }

    @Override // MapEntries : MapEntries COMMATK Expression @COLONTK Expression;
    public List<MapEntry> parseMapEntries2(List<MapEntry> l1, Expression e3, Token t4, Expression e5) {
        l1.add(newMapEntry(e3, toPosition(t4.position), e5));
        return l1;
    }

    @Override // ToolInvokeExpression : ToolRef @PAROPENTK ToolArgs OptComma PARCLOSETK;
    public ToolInvokeExpression parseToolInvokeExpression1(ToolRef t1, Token t2, List<ToolArgument> l3, Token t4) {
        return newToolInvokeExpression(l3, toPosition(t2.position), t1, null);
    }

    @Override // ToolInvokeExpression : ToolRef @PAROPENTK PARCLOSETK;
    public ToolInvokeExpression parseToolInvokeExpression2(ToolRef t1, Token t2) {
        List<ToolArgument> arguments = listc(0);
        return newToolInvokeExpression(arguments, toPosition(t2.position), t1, null);
    }

    @Override // ToolRef : BuiltInTool;
    public ToolRef parseToolRef1(ToolRef t1) {
        return t1;
    }

    @Override // ToolRef : Name;
    public ToolRef parseToolRef2(Token t1) {
        return newToolRef(false, t1.text, toPosition(t1.position), null);
    }

    @Override // BuiltInTool : BuiltInIoTool;
    public ToolRef parseBuiltInTool1(Token t1) {
        return newToolRef(true, t1.text, toPosition(t1.position), null);
    }

    @Override // BuiltInTool : BuiltInGenericTool;
    public ToolRef parseBuiltInTool2(Token t1) {
        return newToolRef(true, t1.text, toPosition(t1.position), null);
    }

    @Override // BuiltInTool : BuiltInPathTool;
    public ToolRef parseBuiltInTool3(Token t1) {
        return newToolRef(true, t1.text, toPosition(t1.position), null);
    }

    @Override // BuiltInTool : BuiltInFileTool;
    public ToolRef parseBuiltInTool4(Token t1) {
        return newToolRef(true, t1.text, toPosition(t1.position), null);
    }

    @Override // BuiltInTool : BuiltInDataTool;
    public ToolRef parseBuiltInTool5(Token t1) {
        return newToolRef(true, t1.text, toPosition(t1.position), null);
    }

    @Override // ToolArgs : Expression;
    public List<ToolArgument> parseToolArgs1(Expression e1) {
        return list(newToolArgument(null, deepclone(e1.getPosition()), e1));
    }

    @Override // ToolArgs : @IDENTIFIERTK EQTK Expression;
    public List<ToolArgument> parseToolArgs2(Token t1, Expression e3) {
        return list(newToolArgument(t1.text, toPosition(t1.position), e3));
    }

    @Override // ToolArgs : ToolArgs COMMATK Expression;
    public List<ToolArgument> parseToolArgs3(List<ToolArgument> l1, Expression e3) {
        l1.add(newToolArgument(null, deepclone(e3.getPosition()), e3));
        return l1;
    }

    @Override // ToolArgs : ToolArgs COMMATK @IDENTIFIERTK EQTK Expression;
    public List<ToolArgument> parseToolArgs4(List<ToolArgument> l1, Token t3, Expression e5) {
        l1.add(newToolArgument(t3.text, toPosition(t3.position), e5));
        return l1;
    }

    @Override // Names : Name;
    public List<Token> parseNames1(Token t1) {
        return list(t1);
    }

    @Override // Names : Names COMMATK Name;
    public List<Token> parseNames2(List<Token> l1, Token t3) {
        l1.add(t3);
        return l1;
    }

    @Override // Name : @IDENTIFIERTK;
    public Token parseName1(Token t1) {
        return t1;
    }

    @Override // Name : @RELATIVENAMETK;
    public Token parseName2(Token t1) {
        return t1;
    }

    @Override // OptComma : ;
    public Token parseOptComma1() {
        return null;
    }

    @Override // OptComma : @COMMATK;
    public Token parseOptComma2(Token t1) {
        return t1;
    }
}
