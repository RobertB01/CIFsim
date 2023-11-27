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
import static org.eclipse.escet.common.java.Sets.set;

import java.util.Map;
import java.util.Set;

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

/** JavaScript code generator. */
public class JavaScriptCodeGen extends CodeGen {
    /** JavaScript code indent amount, as number of spaces. */
    private static final int INDENT = 4;

    /** Constructor for the {@link JavaScriptCodeGen} class. */
    public JavaScriptCodeGen() {
        super(TargetLanguage.JAVASCRIPT, INDENT);
    }

    @Override
    protected ExprCodeGen getExpressionCodeGenerator() {
        // TODO To be implemented.
        return null;
    }

    @Override
    protected TypeCodeGen getTypeCodeGenerator() {
        return new JavaScriptTypeCodeGen();
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected Set<String> getReservedTargetNames() {
        // TODO To be implemented.
        return set();
    }

    @Override
    protected Map<String, String> getTemplates() {
        Map<String, String> templates = map();
        templates.put("index.txt", ".html");
        templates.put("utils.txt", "_utils.js");
        return templates;
    }

    @Override
    protected void addConstants(CodeContext ctxt) {
        // TODO To be implemented.
    }

    @Override
    protected void addEvents(CodeContext ctxt) {
        // TODO To be implemented.
    }

    @Override
    protected void addStateVars(CodeContext ctxt) {
        // TODO To be implemented.
    }

    @Override
    protected void addContVars(CodeContext ctxt) {
        // TODO To be implemented.
    }

    @Override
    protected void addAlgVars(CodeContext ctxt) {
        // TODO To be implemented.
    }

    @Override
    protected void addInputVars(CodeContext ctxt) {
        // TODO To be implemented.
    }

    @Override
    protected void addFunctions(CodeContext ctxt) {
        // TODO To be implemented.
    }

    @Override
    protected void addEnum(EnumDecl enumDecl, CodeContext ctxt) {
        // TODO To be implemented.
    }

    @Override
    protected void addPrints(CodeContext ctxt) {
        // TODO To be implemented.
    }

    @Override
    protected void addEdges(CodeContext ctxt) {
        // TODO To be implemented.
    }

    @Override
    protected IfElseGenerator getIfElseUpdateGenerator() {
        // TODO To be implemented.
        return null;
    }

    @Override
    public void performSingleAssign(CodeBox code, SingleVariableAssignment asgn, Expression value, CodeContext readCtxt,
            CodeContext writeCtxt)
    {
        // TODO To be implemented.
    }

    @Override
    public void performAssign(CodeBox code, SingleVariableAssignment asgn, String rhsText, CodeContext readCtxt,
            CodeContext writeCtxt)
    {
        // TODO To be implemented.
    }

    @Override
    protected void addUpdatesBeginScope(CodeBox code) {
        // TODO To be implemented.
    }

    @Override
    protected void addUpdatesEndScope(CodeBox code) {
        // TODO To be implemented.
    }

    @Override
    public Destination makeDestination(VariableInformation varInfo) {
        // TODO To be implemented.
        return null;
    }
}
