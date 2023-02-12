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

package org.eclipse.escet.cif.datasynth.varorder.parser;

import static org.eclipse.escet.common.java.Lists.list;

import java.util.Collections;
import java.util.List;

import org.eclipse.escet.cif.datasynth.varorder.parser.ast.VarOrderOrOrdererArg;
import org.eclipse.escet.cif.datasynth.varorder.parser.ast.VarOrderOrOrdererInstance;
import org.eclipse.escet.cif.datasynth.varorder.parser.ast.VarOrderOrOrdererListOrdersArg;
import org.eclipse.escet.cif.datasynth.varorder.parser.ast.VarOrderOrOrdererMultiInstance;
import org.eclipse.escet.cif.datasynth.varorder.parser.ast.VarOrderOrOrdererNumberArg;
import org.eclipse.escet.cif.datasynth.varorder.parser.ast.VarOrderOrOrdererOrderArg;
import org.eclipse.escet.cif.datasynth.varorder.parser.ast.VarOrderOrOrdererSingleInstance;
import org.eclipse.escet.cif.datasynth.varorder.parser.ast.VarOrderOrOrdererStringArg;
import org.eclipse.escet.setext.runtime.Parser;
import org.eclipse.escet.setext.runtime.Token;

/**
 * Call back hook methods for:
 * <ul>
 * <li>{@link VarOrderParser}</li>
 * </ul>
 */
public final class VarOrderParserHooks implements VarOrderParser.Hooks {
    @Override
    public void setParser(Parser<?> parser) {
        // No need to store the parser.
    }

    @Override // VarOrderOrOrdererSeq : VarOrderOrOrderer;
    public List<VarOrderOrOrdererInstance> parseVarOrderOrOrdererSeq1(VarOrderOrOrdererInstance v1) {
        return list(v1);
    }

    @Override // VarOrderOrOrdererSeq : VarOrderOrOrdererSeq ARROWTK VarOrderOrOrderer;
    public List<VarOrderOrOrdererInstance> parseVarOrderOrOrdererSeq2(List<VarOrderOrOrdererInstance> l1,
            VarOrderOrOrdererInstance v3)
    {
        l1.add(v3);
        return l1;
    }

    @Override // VarOrderOrOrdererList : VarOrderOrOrderer;
    public List<VarOrderOrOrdererInstance> parseVarOrderOrOrdererList1(VarOrderOrOrdererInstance v1) {
        return list(v1);
    }

    @Override // VarOrderOrOrdererList : VarOrderOrOrdererList COMMATK VarOrderOrOrderer;
    public List<VarOrderOrOrdererInstance> parseVarOrderOrOrdererList2(List<VarOrderOrOrdererInstance> l1,
            VarOrderOrOrdererInstance v3)
    {
        l1.add(v3);
        return l1;
    }

    @Override // VarOrderOrOrderer : @IDENTIFIERTK;
    public VarOrderOrOrdererInstance parseVarOrderOrOrderer1(Token t1) {
        return new VarOrderOrOrdererSingleInstance(t1, Collections.emptyList(), false);
    }

    @Override // VarOrderOrOrderer : @IDENTIFIERTK PAROPENTK PARCLOSETK;
    public VarOrderOrOrdererInstance parseVarOrderOrOrderer2(Token t1) {
        return new VarOrderOrOrdererSingleInstance(t1, Collections.emptyList(), true);
    }

    @Override // VarOrderOrOrderer : @IDENTIFIERTK PAROPENTK VarOrderOrOrdererArgs OptComma PARCLOSETK;
    public VarOrderOrOrdererInstance parseVarOrderOrOrderer3(Token t1, List<VarOrderOrOrdererArg> l3, Token t4) {
        return new VarOrderOrOrdererSingleInstance(t1, l3, true);
    }

    @Override // VarOrderOrOrderer : @PAROPENTK VarOrderOrOrdererSeq PARCLOSETK;
    public VarOrderOrOrdererInstance parseVarOrderOrOrderer4(Token t1, List<VarOrderOrOrdererInstance> l2) {
        return new VarOrderOrOrdererMultiInstance(t1.position, l2);
    }

    @Override // VarOrderOrOrdererArgs : VarOrderOrOrdererArg;
    public List<VarOrderOrOrdererArg> parseVarOrderOrOrdererArgs1(VarOrderOrOrdererArg v1) {
        return list(v1);
    }

    @Override // VarOrderOrOrdererArgs : VarOrderOrOrdererArgs COMMATK VarOrderOrOrdererArg;
    public List<VarOrderOrOrdererArg> parseVarOrderOrOrdererArgs2(List<VarOrderOrOrdererArg> l1,
            VarOrderOrOrdererArg v3)
    {
        l1.add(v3);
        return l1;
    }

    @Override // VarOrderOrOrdererArg : @IDENTIFIERTK EQUALTK @NUMBERTK;
    public VarOrderOrOrdererArg parseVarOrderOrOrdererArg1(Token t1, Token t3) {
        return new VarOrderOrOrdererNumberArg(t1, t3);
    }

    @Override // VarOrderOrOrdererArg : @IDENTIFIERTK EQUALTK @STRINGTK;
    public VarOrderOrOrdererArg parseVarOrderOrOrdererArg2(Token t1, Token t3) {
        return new VarOrderOrOrdererStringArg(t1, t3);
    }

    @Override // VarOrderOrOrdererArg : @IDENTIFIERTK EQUALTK VarOrderOrOrderer;
    public VarOrderOrOrdererArg parseVarOrderOrOrdererArg3(Token t1, VarOrderOrOrdererInstance v3) {
        return new VarOrderOrOrdererOrderArg(t1, v3);
    }

    @Override // VarOrderOrOrdererArg : @IDENTIFIERTK EQUALTK SQOPENTK VarOrderOrOrdererList SQCLOSETK;
    public VarOrderOrOrdererArg parseVarOrderOrOrdererArg4(Token t1, List<VarOrderOrOrdererInstance> l4) {
        return new VarOrderOrOrdererListOrdersArg(t1, l4);
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
