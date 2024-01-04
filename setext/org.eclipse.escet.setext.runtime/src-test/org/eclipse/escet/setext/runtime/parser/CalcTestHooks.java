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

package org.eclipse.escet.setext.runtime.parser;

import static org.eclipse.escet.common.java.Lists.concat;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;

import java.util.List;
import java.util.Map;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.setext.runtime.Parser;
import org.eclipse.escet.setext.runtime.Token;

/**
 * Call back hook methods for:
 * <ul>
 * <li>{@link CalcTestParser}</li>
 * <li>{@link CalcTestExpressionParser}</li>
 * </ul>
 */
public final class CalcTestHooks implements CalcTestParser.Hooks, CalcTestExpressionParser.Hooks {
    /** Mapping from variable names to their values. */
    private final Map<String, Double> variables = map();

    @Override
    public void setParser(Parser<?> parser) {
        // No need to store this...
    }

    @Override // Program : ;
    public List<Double> parseProgram1() {
        return list();
    }

    @Override // Program : Statements;
    public List<Double> parseProgram2(List<Double> l1) {
        return l1;
    }

    @Override // Statements : Statement SEMICOL;
    public List<Double> parseStatements1(List<Double> l1) {
        return l1;
    }

    @Override // Statements : Statements Statement SEMICOL;
    public List<Double> parseStatements2(List<Double> l1, List<Double> l2) {
        return concat(l1, l2);
    }

    @Override // Statement : ;
    public List<Double> parseStatement1() {
        return list();
    }

    @Override // Statement : @NAME EQUALS Expression;
    public List<Double> parseStatement2(Token t1, Double d3) {
        variables.put(t1.text, d3);
        return list(d3);
    }

    @Override // Statement : Expression;
    public List<Double> parseStatement3(Double d1) {
        return list(d1);
    }

    @Override // Expression : TimesExpression;
    public Double parseExpression1(Double d1) {
        return d1;
    }

    @Override // Expression : Expression PLUS TimesExpression;
    public Double parseExpression2(Double d1, Double d3) {
        return d1 + d3;
    }

    @Override // Expression : Expression MINUS TimesExpression;
    public Double parseExpression3(Double d1, Double d3) {
        return d1 - d3;
    }

    @Override // TimesExpression : MinusExpression;
    public Double parseTimesExpression1(Double d1) {
        return d1;
    }

    @Override // TimesExpression : TimesExpression TIMES MinusExpression;
    public Double parseTimesExpression2(Double d1, Double d3) {
        return d1 * d3;
    }

    @Override // TimesExpression : TimesExpression DIVIDE MinusExpression;
    public Double parseTimesExpression3(Double d1, Double d3) {
        return d1 / d3;
    }

    @Override // MinusExpression : BaseExpression;
    public Double parseMinusExpression1(Double d1) {
        return d1;
    }

    @Override // MinusExpression : MINUS MinusExpression;
    public Double parseMinusExpression2(Double d2) {
        return -d2;
    }

    @Override // BaseExpression : @NUMBER;
    public Double parseBaseExpression1(Token t1) {
        return (double)Integer.parseInt(t1.text);
    }

    @Override // BaseExpression : @NAME;
    public Double parseBaseExpression2(Token t1) {
        Assert.check(variables.containsKey(t1.text));
        return variables.get(t1.text);
    }

    @Override // BaseExpression : PIKW;
    public Double parseBaseExpression3() {
        return Math.PI;
    }

    @Override // BaseExpression : LPAREN Expression RPAREN;
    public Double parseBaseExpression4(Double d2) {
        return d2;
    }
}
