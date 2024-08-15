//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.codegen;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Sets.set;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.escet.cif.common.CifEvalException;
import org.eclipse.escet.cif.common.CifEvalUtils;
import org.eclipse.escet.cif.metamodel.cif.IoDecl;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgCopy;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgIn;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEvent;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEventIf;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEventIfEntry;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEventSingle;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgMove;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgOut;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;

/** SVG code generator for the target language. */
public abstract class SvgCodeGen {
    /**
     * Gets the interactive events, the events coupled to SVG input mappings.
     *
     * @param svgIns The SVG input mappings.
     * @param events The events of the specification.
     * @return The interactive events.
     */
    public static Set<Event> getSvgInEvents(List<SvgIn> svgIns, List<Event> events) {
        Set<Event> svgInEvents = set();
        for (SvgIn svgIn: svgIns) {
            SvgInEvent event = svgIn.getEvent();
            if (event == null) {
                // Input mapping without event.
            } else if (event instanceof SvgInEventSingle) {
                // Single event.
                SvgInEventSingle singleEvt = (SvgInEventSingle)event;
                Event evt = ((EventExpression)singleEvt.getEvent()).getEvent();
                svgInEvents.add(evt);
            } else if (event instanceof SvgInEventIf) {
                // 'if/then/else' event mapping.
                SvgInEventIf ifEvent = (SvgInEventIf)event;
                for (SvgInEventIfEntry entry: ifEvent.getEntries()) {
                    Event evt = ((EventExpression)entry.getEvent()).getEvent();
                    svgInEvents.add(evt);
                }
            } else {
                throw new RuntimeException("Unknown SVG input mapping event: " + event);
            }
        }

        return svgInEvents;
    }

    /**
     * Filters the given CIF/SVG declarations to those for a certain SVG file, and group them per type.
     *
     * @param svgDecls The relevant CIF/SVG declarations of the specification to consider.
     * @param svgAbsPath The absolute local file system path of the SVG file to consider.
     * @param svgPathsRelToAbs Mapping from relative SVG file paths as used in SVG file declarations of the CIF/SVG
     *     declarations to their absolute paths.
     * @return The CIF/SVG declarations for the SVG file to consider, grouped per type.
     */
    public static CifSvgDecls filterAndGroup(List<IoDecl> svgDecls, String svgAbsPath,
            Map<String, String> svgPathsRelToAbs)
    {
        CifSvgDecls cifSvgDecls = new CifSvgDecls();
        for (IoDecl ioDecl: svgDecls) {
            if (ioDecl instanceof SvgCopy svgCopy) {
                if (svgAbsPath.equals(svgPathsRelToAbs.get(svgCopy.getSvgFile().getPath()))) {
                    cifSvgDecls.svgCopies.add(svgCopy);
                }
            } else if (ioDecl instanceof SvgMove svgMove) {
                if (svgAbsPath.equals(svgPathsRelToAbs.get(svgMove.getSvgFile().getPath()))) {
                    cifSvgDecls.svgMoves.add(svgMove);
                }
            } else if (ioDecl instanceof SvgOut svgOut) {
                if (svgAbsPath.equals(svgPathsRelToAbs.get(svgOut.getSvgFile().getPath()))) {
                    cifSvgDecls.svgOuts.add(svgOut);
                }
            } else if (ioDecl instanceof SvgIn svgIn) {
                if (svgAbsPath.equals(svgPathsRelToAbs.get(svgIn.getSvgFile().getPath()))) {
                    cifSvgDecls.svgIns.add(svgIn);
                }
            } else {
                throw new RuntimeException("Unexpected CIF/SVG declaration: " + ioDecl);
            }
        }
        return cifSvgDecls;
    }

    /**
     * Evaluates a CIF expression that can be statically evaluated for use by a CIF/SVG declaration.
     *
     * @param expr The expression to evaluate. The expression must have a string type.
     * @return The text resulting from evaluation of the expression.
     */
    protected static String evalSvgStringExpr(Expression expr) {
        try {
            return (String)CifEvalUtils.eval(expr, false);
        } catch (CifEvalException e) {
            // Shouldn't happen, as type checker already evaluated it.
            throw new RuntimeException(e);
        }
    }

    /**
     * Evaluates a CIF expression that can be statically evaluated for use by a CIF/SVG declaration.
     *
     * @param expr The expression to evaluate. The expression must have an integer or real type.
     * @return The number resulting from evaluation of the expression.
     */
    protected static double evalSvgNumberExpr(Expression expr) {
        try {
            Object rslt = CifEvalUtils.eval(expr, false);
            if (rslt instanceof Integer) {
                return (int)rslt;
            }
            if (rslt instanceof Double) {
                return (double)rslt;
            }
            throw new RuntimeException("Number expected: " + rslt);
        } catch (CifEvalException e) {
            // Shouldn't happen, as type checker already evaluated it.
            throw new RuntimeException(e);
        }
    }

    /** CIF/SVG declarations for a single SVG file, grouped per type. */
    public static class CifSvgDecls {
        /** SVG copy declarations. */
        public final List<SvgCopy> svgCopies = list();

        /** SVG move declarations. */
        public final List<SvgMove> svgMoves = list();

        /** SVG output mappings. */
        public final List<SvgOut> svgOuts = list();

        /** SVG input mappings. */
        public final List<SvgIn> svgIns = list();

        /**
         * Returns the number of declarations for this SVG file.
         *
         * @return The number of declarations.
         */
        public int size() {
            return svgCopies.size() + svgMoves.size() + svgOuts.size() + svgIns.size();
        }
    }
}
