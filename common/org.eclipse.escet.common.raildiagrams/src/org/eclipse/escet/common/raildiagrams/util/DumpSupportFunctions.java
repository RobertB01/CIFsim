//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021, 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.raildiagrams.util;

import static org.eclipse.escet.common.app.framework.output.OutputProvider.dbg;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.ddbg;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.idbg;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;

import org.eclipse.escet.common.raildiagrams.railroad.DiagramElement;
import org.eclipse.escet.common.raildiagrams.railroad.ProxyDiagramElement;

/** Class with helper functions to create dump output. */
public class DumpSupportFunctions {
    /** Constructor of the static {@link DumpSupportFunctions} class. */
    private DumpSupportFunctions() {
        // Static class.
    }

    /**
     * Construct the name identifying the given element.
     *
     * @param element Element to identify.
     * @return Name identifying the given element.
     */
    public static String getElementName(DiagramElement element) {
        return fmt("%s-%d", element.kindName, element.id);
    }

    /**
     * Write a element header to debug output.
     *
     * @param element       Element to write.
     * @param childElements Children of the element.
     */
    public static void writeDumpHeaderElements(DiagramElement element, List<DiagramElement> childElements) {
        dbg("==========================================");
        dbg("Element %s:", getElementName(element));

        if (childElements != null) {
            idbg();
            for (DiagramElement child : childElements) {
                dbg("Child %s", getElementName(child));
            }
            ddbg();
        }
    }

    /**
     * Write a element header to debug output.
     *
     * @param element       Element to write.
     * @param childElements Children of the element.
     */
    public static void writeDumpHeaderProxies(DiagramElement element, List<ProxyDiagramElement> childElements) {
        dbg("==========================================");
        dbg("Element %s:", getElementName(element));

        if (childElements != null) {
            idbg();
            for (ProxyDiagramElement child : childElements) {
                dbg("Child %s", getElementName(child.child));
            }
            ddbg();
        }
    }
}
