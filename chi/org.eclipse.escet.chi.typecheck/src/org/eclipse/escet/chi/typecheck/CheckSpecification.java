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

package org.eclipse.escet.chi.typecheck;

import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newEnumDeclaration;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newEnumValue;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newSpecification;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.position.common.PositionUtils.copyPosition;

import java.util.List;
import java.util.Map;

import org.eclipse.escet.chi.metamodel.chi.ConstantDeclaration;
import org.eclipse.escet.chi.metamodel.chi.Declaration;
import org.eclipse.escet.chi.metamodel.chi.EnumDeclaration;
import org.eclipse.escet.chi.metamodel.chi.EnumValue;
import org.eclipse.escet.chi.metamodel.chi.FunctionDeclaration;
import org.eclipse.escet.chi.metamodel.chi.ModelDeclaration;
import org.eclipse.escet.chi.metamodel.chi.ProcessDeclaration;
import org.eclipse.escet.chi.metamodel.chi.Specification;
import org.eclipse.escet.chi.metamodel.chi.TypeDeclaration;
import org.eclipse.escet.chi.metamodel.chi.XperDeclaration;
import org.eclipse.escet.chi.typecheck.symbols.ConstantSymbolEntry;
import org.eclipse.escet.chi.typecheck.symbols.EnumValueSymbolEntry;
import org.eclipse.escet.chi.typecheck.symbols.FunctionDefSymbolEntry;
import org.eclipse.escet.chi.typecheck.symbols.ModelDefSymbolEntry;
import org.eclipse.escet.chi.typecheck.symbols.ProcessDefSymbolEntry;
import org.eclipse.escet.chi.typecheck.symbols.SymbolEntry;
import org.eclipse.escet.chi.typecheck.symbols.TypeSymbolEntry;
import org.eclipse.escet.chi.typecheck.symbols.XperDefSymbolEntry;
import org.eclipse.escet.common.typechecker.SemanticException;

/**
 * Perform type checking on a Chi specification.
 *
 * <p>
 * Make a copy of the entire specification, while checking correctness of all constructs and adding (type) decorations
 * where needed.
 * </p>
 */
public abstract class CheckSpecification {
    /**
     * Type check and transform the entire Chi specification.
     *
     * @param spec Source specification.
     * @param tchk Type checker.
     * @return Transformed (with type information) new specification.
     */
    public static Specification transSpecification(Specification spec, ChiTypeChecker tchk) {
        CheckContext topCtxt = new CheckContext(tchk);
        topCtxt = topCtxt.newSymbolContext();

        // Phase 1: add all definitions to the symbol table.
        List<TypeSymbolEntry> enums = list();
        List<TypeSymbolEntry> types = list();
        List<ConstantSymbolEntry> constants = list();
        List<FunctionDefSymbolEntry> functions = list();
        List<ProcessDefSymbolEntry> processes = list();
        List<ModelDefSymbolEntry> models = list();
        List<XperDefSymbolEntry> experiments = list();
        for (Declaration decl: spec.getDeclarations()) {
            if (decl instanceof EnumDeclaration) {
                EnumDeclaration eDecl = (EnumDeclaration)decl;
                eDecl = transEnumDeclaration(eDecl, topCtxt);

                TypeSymbolEntry se = new TypeSymbolEntry(eDecl, null);
                topCtxt.addSymbol(se);
                enums.add(se);

                for (EnumValue eVal: eDecl.getValues()) {
                    SymbolEntry sv = new EnumValueSymbolEntry(se, eVal, topCtxt);
                    topCtxt.addSymbol(sv);
                }
            } else if (decl instanceof TypeDeclaration) {
                TypeDeclaration tdef = (TypeDeclaration)decl;
                TypeSymbolEntry se = new TypeSymbolEntry(tdef, topCtxt);
                topCtxt.addSymbol(se);
                types.add(se);
            } else if (decl instanceof ConstantDeclaration) {
                ConstantDeclaration cdef = (ConstantDeclaration)decl;
                ConstantSymbolEntry se = new ConstantSymbolEntry(cdef, topCtxt);
                topCtxt.addSymbol(se);
                constants.add(se);
            } else if (decl instanceof FunctionDeclaration) {
                FunctionDeclaration fdef = (FunctionDeclaration)decl;
                FunctionDefSymbolEntry se = new FunctionDefSymbolEntry(fdef, topCtxt);
                topCtxt.addSymbol(se);
                functions.add(se);
            } else if (decl instanceof ProcessDeclaration) {
                ProcessDeclaration pdef = (ProcessDeclaration)decl;
                ProcessDefSymbolEntry se = new ProcessDefSymbolEntry(pdef, topCtxt);
                topCtxt.addSymbol(se);
                processes.add(se);
            } else if (decl instanceof ModelDeclaration) {
                ModelDeclaration mdef = (ModelDeclaration)decl;
                ModelDefSymbolEntry se = new ModelDefSymbolEntry(mdef, topCtxt);
                topCtxt.addSymbol(se);
                models.add(se);
            } else if (decl instanceof XperDeclaration) {
                XperDeclaration xdef = (XperDeclaration)decl;
                XperDefSymbolEntry se = new XperDefSymbolEntry(xdef, topCtxt);
                topCtxt.addSymbol(se);
                experiments.add(se);
            }
        }

        // Phase 2: Type check all definitions and add them to the output.
        List<Declaration> newDecls = list();

        for (TypeSymbolEntry se: enums) {
            if (doTypecheck(se)) {
                newDecls.add(se.getEnumTypeDeclaration());
            }
        }
        for (TypeSymbolEntry se: types) {
            if (doTypecheck(se)) {
                newDecls.add(se.getTypeDeclaration());
            }
        }
        for (ConstantSymbolEntry se: constants) {
            if (doTypecheck(se)) {
                newDecls.add(se.getConstant());
            }
        }
        for (FunctionDefSymbolEntry se: functions) {
            if (doTypecheck(se)) {
                newDecls.add(se.getFunction());
            }
        }
        for (ProcessDefSymbolEntry se: processes) {
            if (doTypecheck(se)) {
                newDecls.add(se.getProcess());
            }
        }
        for (ModelDefSymbolEntry se: models) {
            if (doTypecheck(se)) {
                newDecls.add(se.getModel());
            }
        }
        for (XperDefSymbolEntry se: experiments) {
            if (doTypecheck(se)) {
                newDecls.add(se.getXper());
            }
        }

        topCtxt.checkSymbolUsage();
        return newSpecification(newDecls);
    }

    /**
     * Perform type-checking of a top-level element, ignoring the type-check exception.
     *
     * @param se Symbol to check.
     * @return Whether type-checking succeeded.
     */
    private static boolean doTypecheck(SymbolEntry se) {
        try {
            se.fullTypeCheck();
            return true;
        } catch (SemanticException e) {
            // Ignore the exception, as the problem is already added.
            return false;
        }
    }

    /**
     * Convert enumeration values, while checking them for uniqueness.
     *
     * @param decl Declaration to check.
     * @param ctxt Type checker context (for reporting errors).
     * @return Type checked declaration.
     */
    private static EnumDeclaration transEnumDeclaration(EnumDeclaration decl, CheckContext ctxt) {
        List<EnumValue> newValues = list();
        Map<String, EnumValue> uniq = map();
        for (EnumValue ev: decl.getValues()) {
            String val = ev.getName();
            EnumValue prev = uniq.get(val);
            if (prev != null) {
                ctxt.addError(Message.ENUM_VALUE_DOUBLE_DEFINED, prev.getPosition(), val);
                ctxt.addError(Message.ENUM_VALUE_DOUBLE_DEFINED, ev.getPosition(), val);
                throw new SemanticException();
            }
            uniq.put(val, ev);

            newValues.add(newEnumValue(val, copyPosition(ev)));
        }
        return newEnumDeclaration(decl.getName(), copyPosition(decl), newValues);
    }
}
