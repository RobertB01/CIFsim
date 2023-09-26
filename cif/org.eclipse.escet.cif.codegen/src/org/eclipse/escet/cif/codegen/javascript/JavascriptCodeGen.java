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

package org.eclipse.escet.cif.codegen.javascript;

import static org.eclipse.escet.common.java.Maps.map;

import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import org.eclipse.escet.cif.codegen.CodeContext;
import org.eclipse.escet.cif.codegen.CodeGen;
import org.eclipse.escet.cif.codegen.ExprCodeGen;
import org.eclipse.escet.cif.codegen.IfElseGenerator;
import org.eclipse.escet.cif.codegen.TypeCodeGen;
import org.eclipse.escet.cif.codegen.assignments.Destination;
import org.eclipse.escet.cif.codegen.assignments.VariableInformation;
import org.eclipse.escet.cif.codegen.options.TargetLanguage;
import org.eclipse.escet.cif.codegen.updates.tree.SingleVariableAssignment;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.common.box.CodeBox;

/** Javascript code generator. */
public class JavascriptCodeGen extends CodeGen {
    /** Javascript code indent amount, as number of spaces. */
    private static final int INDENT = 4;

    /** Constructor for the {@link JavascriptCodeGen} class. */
    public JavascriptCodeGen() {
        super(TargetLanguage.JAVASCRIPT, INDENT);
    }

    @Override
    protected ExprCodeGen getExpressionCodeGenerator() {
        return null;
    }

    @Override
    protected TypeCodeGen getTypeCodeGenerator() {
        return new JavascriptTypeCodeGen();
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected Set<String> getReservedTargetNames() {
        return new HashSet<>();
    }

    @Override
    protected Map<String, String> getTemplates() {
        Map<String, String> templates = map();
        templates.put("main.txt", ".html");
        return templates;
    }

    @Override
    protected void addConstants(CodeContext ctxt) { }

    @Override
    protected void addEvents(CodeContext ctxt) { }

    @Override
    protected void addStateVars(CodeContext ctxt) { }

    @Override
    protected void addContVars(CodeContext ctxt) { }

    @Override
    protected void addAlgVars(CodeContext ctxt) { }

    @Override
    protected void addInputVars(CodeContext ctxt) { }

    @Override
    protected void addFunctions(CodeContext ctxt) { }

    @Override
    protected void addEnum(EnumDecl enumDecl, CodeContext ctxt) { }

    @Override
    protected void addPrints(CodeContext ctxt) { }

    @Override
    protected void addEdges(CodeContext ctxt) { }

    @Override
    protected IfElseGenerator getIfElseUpdateGenerator() {
        return null;
    }

    @Override
    public void performSingleAssign(CodeBox code, SingleVariableAssignment asgn, Expression value, CodeContext readCtxt,
            CodeContext writeCtxt) { }

    @Override
    public void performAssign(CodeBox code, SingleVariableAssignment asgn, String rhsText, CodeContext readCtxt,
            CodeContext writeCtxt) { }

    @Override
    protected void addUpdatesBeginScope(CodeBox code) { }

    @Override
    protected void addUpdatesEndScope(CodeBox code) { }

    @Override
    public Destination makeDestination(VariableInformation varInfo) {
        return null;
    }
}
