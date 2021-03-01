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

package org.eclipse.escet.cif.typechecker.postchk;

import static org.eclipse.escet.cif.common.CifTextUtils.exprToStr;
import static org.eclipse.escet.cif.common.CifTextUtils.getAbsName;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.IoDecl;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.cif.print.Print;
import org.eclipse.escet.cif.metamodel.cif.print.PrintFile;
import org.eclipse.escet.cif.metamodel.cif.print.PrintFor;
import org.eclipse.escet.cif.metamodel.cif.print.PrintForKind;
import org.eclipse.escet.cif.typechecker.ErrMsg;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.typechecker.SemanticException;

/** Print I/O declaration type checker, during 'post' type checking phase. */
public class CifPrintPostChecker {
    /** Constructor for the {@link CifPrintPostChecker} class. */
    private CifPrintPostChecker() {
        // Static class.
    }

    /**
     * Checks the given specification for print I/O declaration constraints that can only be checked during the 'post'
     * type checking phase.
     *
     * @param spec The specification to check.
     * @param env The post check environment to use.
     */
    public static void check(Specification spec, CifPostCheckEnv env) {
        checkComponent(spec, env, null);
    }

    /**
     * Checks the given component recursively for print I/O declaration constraints that can only be checked during the
     * 'post' type checking phase.
     *
     * @param comp The component to check recursively.
     * @param env The post check environment to use.
     * @param printFile The currently active print file declaration (from the parent component), or {@code null} if not
     *     available/applicable.
     */
    private static void checkComponent(ComplexComponent comp, CifPostCheckEnv env, PrintFile printFile) {
        // Get print file declaration that applies to this component.
        for (IoDecl ioDecl: comp.getIoDecls()) {
            if (ioDecl instanceof PrintFile) {
                // Found print file declaration, so update active one.
                printFile = (PrintFile)ioDecl;

                // We already checked before that we don't have multiple print
                // file declarations in a single scope, so we don't have to
                // continue looking for print file declarations in this scope.
                break;
            }
        }

        // Check I/O declarations of this scope.
        for (IoDecl ioDecl: comp.getIoDecls()) {
            try {
                if (ioDecl instanceof Print) {
                    checkPrint((Print)ioDecl, env, printFile);
                }
            } catch (SemanticException ex) {
                // If type checking fails for a declaration, continue with the
                // next one. Declarations should be independent from each
                // other (i.e. we can't refer to print declarations or parts
                // of them).
            }
        }

        // Recursively check child components.
        if (comp instanceof Group) {
            for (Component child: ((Group)comp).getComponents()) {
                checkComponent((ComplexComponent)child, env, printFile);
            }
        }
    }

    /**
     * Checks the given print I/O declaration.
     *
     * @param print The print I/O declaration to check.
     * @param env The post check environment to use.
     * @param printFile The print file declaration that is active for the component in which the declaration is
     *     declared, or {@code null} if not available.
     */
    private static void checkPrint(Print print, CifPostCheckEnv env, PrintFile printFile) {
        // Skip prints without 'for' filter.
        if (print.getFors().isEmpty()) {
            return;
        }

        // Create mapping of 'for' kinds to their filters.
        Map<PrintForKind, List<PrintFor>> kindToPosMap = map();
        for (PrintFor printFor: print.getFors()) {
            List<PrintFor> posList = kindToPosMap.get(printFor.getKind());
            if (posList == null) {
                posList = list();
                kindToPosMap.put(printFor.getKind(), posList);
            }
            posList.add(printFor);
        }

        // Report duplicate 'for' filters for same kind.
        Set<Entry<PrintForKind, List<PrintFor>>> entries;
        entries = kindToPosMap.entrySet();
        for (Entry<PrintForKind, List<PrintFor>> entry: entries) {
            // Skip duplicate named events, as they are checked below.
            PrintForKind kind = entry.getKey();
            if (kind == PrintForKind.NAME) {
                continue;
            }

            // Report duplicates.
            List<PrintFor> fors = entry.getValue();
            if (fors.size() < 2) {
                continue;
            }
            for (PrintFor printFor: fors) {
                String txt = fmt("\"%s\"", kind.name().toLowerCase(Locale.US));
                env.addProblem(ErrMsg.PRINT_DUPL_FOR, printFor.getPosition(), txt);
                // Non-fatal problem.
            }
        }

        // Report specific event 'for' filters when also all events.
        List<PrintFor> eventFors = kindToPosMap.get(PrintForKind.EVENT);
        List<PrintFor> nameFors = kindToPosMap.get(PrintForKind.NAME);
        if (eventFors != null && nameFors != null) {
            Assert.check(!eventFors.isEmpty());
            Assert.check(!nameFors.isEmpty());

            for (PrintFor printFor: nameFors) {
                String txt = fmt("\"event\" (all events) and \"%s\" (specific event)", exprToStr(printFor.getEvent()));
                env.addProblem(ErrMsg.PRINT_DUPL_FOR, printFor.getPosition(), txt);
                // Non-fatal problem.
            }
        }

        // Report duplicate specific events.
        if (nameFors != null && nameFors.size() > 1) {
            Map<Event, PrintFor> eventForMap = map();
            for (PrintFor printFor: nameFors) {
                EventExpression eventRef = (EventExpression)printFor.getEvent();
                Event event = eventRef.getEvent();

                PrintFor duplFor = eventForMap.get(event);
                if (duplFor == null) {
                    eventForMap.put(event, printFor);
                } else {
                    // Duplicate specific event.
                    String txt = fmt("event \"%s\"", getAbsName(event));
                    env.addProblem(ErrMsg.PRINT_DUPL_FOR, printFor.getPosition(), txt);
                    env.addProblem(ErrMsg.PRINT_DUPL_FOR, duplFor.getPosition(), txt);
                    // Non-fatal problem.
                }
            }
        }
    }
}
