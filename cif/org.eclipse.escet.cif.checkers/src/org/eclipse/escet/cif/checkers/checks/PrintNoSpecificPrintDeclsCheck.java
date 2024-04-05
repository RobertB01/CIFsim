//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.checkers.checks;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import org.eclipse.escet.cif.checkers.CifCheck;
import org.eclipse.escet.cif.checkers.CifCheckViolations;
import org.eclipse.escet.cif.metamodel.cif.print.Print;
import org.eclipse.escet.cif.metamodel.cif.print.PrintForKind;

/** CIF check that does not allow certain print declarations. */
public class PrintNoSpecificPrintDeclsCheck extends CifCheck {
    /** The features of print declarations to disallow. */
    private final EnumSet<NoSpecificPrintDecl> disalloweds;

    /**
     * Constructor for the {@link PrintNoSpecificPrintDeclsCheck} class.
     *
     * @param disalloweds The features of print declarations to disallow.
     */
    public PrintNoSpecificPrintDeclsCheck(NoSpecificPrintDecl... disalloweds) {
        this(EnumSet.copyOf(Arrays.asList(disalloweds)));
    }

    /**
     * Constructor for the {@link PrintNoSpecificPrintDeclsCheck} class.
     *
     * @param disalloweds The features of print declarations to disallow.
     */
    public PrintNoSpecificPrintDeclsCheck(EnumSet<NoSpecificPrintDecl> disalloweds) {
        this.disalloweds = disalloweds;
    }

    @Override
    protected void preprocessPrint(Print print, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificPrintDecl.TEXT_PRE) && print.getTxtPre() != null) {
            addViolation(print, "a pre/source state text", violations);
        }
        if (disalloweds.contains(NoSpecificPrintDecl.TEXT_POST) && print.getTxtPost() != null) {
            addViolation(print, "a post/target state text", violations);
        }
        if (disalloweds.contains(NoSpecificPrintDecl.TEXT_PRE_POST) && print.getTxtPre() != null
                && print.getTxtPost() != null)
        {
            addViolation(print, "both a pre/source state text and a post/target state text", violations);
        }

        if (disalloweds.contains(NoSpecificPrintDecl.FILTER_PRE) && print.getWhenPre() != null) {
            addViolation(print, "a pre/source state filter", violations);
        }
        if (disalloweds.contains(NoSpecificPrintDecl.FILTER_POST) && print.getWhenPost() != null) {
            addViolation(print, "a post/target state filter", violations);
        }
        if (disalloweds.contains(NoSpecificPrintDecl.FILTER_PRE_POST) && print.getWhenPre() != null
                && print.getWhenPost() != null)
        {
            addViolation(print, "both a pre/source state filter and a post/target state filter", violations);
        }

        if (disalloweds.contains(NoSpecificPrintDecl.TEXT_PRE_FILTER_POST) && print.getTxtPre() != null
                && print.getWhenPost() != null)
        {
            addViolation(print, "both a pre/source state text and a post/target state filter", violations);
        }
        if (disalloweds.contains(NoSpecificPrintDecl.TEXT_POST_FILTER_PRE) && print.getTxtPost() != null
                && print.getWhenPre() != null)
        {
            addViolation(print, "both a post/target state text and a pre/source state filter", violations);
        }

        List<PrintForKind> forKinds = print.getFors().stream().map(f -> f.getKind()).toList();
        if (disalloweds.contains(NoSpecificPrintDecl.FOR_EVENT)
                && (forKinds.contains(PrintForKind.EVENT) || forKinds.contains(PrintForKind.NAME)))
        {
            addViolation(print, "an event transition filter", violations);
        }
        if (disalloweds.contains(NoSpecificPrintDecl.FOR_TIME) && forKinds.contains(PrintForKind.TIME)) {
            addViolation(print, "a time transition filter", violations);
        }
        if (disalloweds.contains(NoSpecificPrintDecl.FOR_INITIAL) && forKinds.contains(PrintForKind.INITIAL)) {
            addViolation(print, "an initial virtual transition filter", violations);
        }
        if (disalloweds.contains(NoSpecificPrintDecl.FOR_FINAL) && forKinds.contains(PrintForKind.FINAL)) {
            addViolation(print, "a final transition filter", violations);
        }
    }

    /**
     * Add a violation for a feature of the given print declaration.
     *
     * @param print The print declaration.
     * @param featureName The name of the feature of the print declaration that is disallowed.
     * @param violations The violations collected so far. Is modified in-place.
     */
    private void addViolation(Print print, String featureName, CifCheckViolations violations) {
        violations.add(print, "Print declaration has %s", featureName);
    }

    /** The print declaration feature to disallow. */
    public static enum NoSpecificPrintDecl {
        /** Disallow print declarations with a pre text. */
        TEXT_PRE,

        /** Disallow print declarations with a post text. */
        TEXT_POST,

        /** Disallow print declarations with separate pre and post texts. */
        TEXT_PRE_POST,

        /** Disallow print declarations with a pre filter. */
        FILTER_PRE,

        /** Disallow print declarations with a post filter. */
        FILTER_POST,

        /** Disallow print declarations with separate pre and post filters. */
        FILTER_PRE_POST,

        /** Disallow print declarations with pre text and post filter. */
        TEXT_PRE_FILTER_POST,

        /** Disallow print declarations with post text and pre filter. */
        TEXT_POST_FILTER_PRE,

        /** Disallow print declarations for events transitions. */
        FOR_EVENT,

        /** Disallow print declarations for time transitions. */
        FOR_TIME,

        /** Disallow print declarations for the initial transition. */
        FOR_INITIAL,

        /** Disallow print declarations for the final transition. */
        FOR_FINAL,
    }
}
