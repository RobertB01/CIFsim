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

package org.eclipse.escet.common.svg;

import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.str;

import java.util.Arrays;

import org.apache.batik.bridge.BridgeException;
import org.apache.batik.bridge.UserAgent;
import org.apache.batik.bridge.UserAgentAdapter;
import org.eclipse.escet.common.java.ReflectionUtils;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

/** SVG {@link UserAgent} used by the {@link SvgCanvas} to record problems. */
public class SvgUserAgent extends UserAgentAdapter {
    /** The problem recorded by the user agent, or {@code null} if none. */
    public Throwable problem = null;

    @Override
    public void displayError(Exception ex) {
        // If a problem has already been recorded, ignore this problem.
        if (problem != null) {
            return;
        }

        // Store problem.
        problem = convertBatikException(ex);
    }

    /**
     * Converts an exception thrown by Batik to an exception that can be presented to the end user. Exceptions that
     * represent crashes are not converted.
     *
     * <p>
     * This method may return an {@link SvgException}, in which case it is recommended to wrap that exception in another
     * {@link SvgException}, which indicates the kind of SVG operation that failed (usually rendering), and the path to
     * the SVG image file for which it failed.
     * </p>
     *
     * @param ex The exception thrown by Batik.
     * @return The converted exception.
     */
    public static Throwable convertBatikException(Exception ex) {
        // Handle end-user errors as special cases.
        if (ex instanceof BridgeException) {
            // Get internal data from exception by means of reflection.
            BridgeException bex = (BridgeException)ex;
            Object[] params = ReflectionUtils.getFieldValue(bex, "params");
            int line = ReflectionUtils.getFieldValue(bex, "line");

            // Initialize result to the cause of the original exception.
            Throwable rslt = ex.getCause();

            // Get single line message for the BridgeException.
            String msg = bex.getMessage();
            msg = msg.replace("\r", "").replace("\n", " ");

            // Remove URI (with SVG file absolute path) and line number prefix.
            Element elem = bex.getElement();
            SVGDocument doc = null;
            if (elem != null) {
                doc = (SVGDocument)elem.getOwnerDocument();
            }
            String uri = (doc == null) ? "<Unknown URI>" : doc.getURL();
            String prefix = uri + ":" + str(line);
            if (msg.startsWith(prefix)) {
                msg = msg.substring(prefix.length());
            }

            // Add wrapped exception for the BridgeException message.
            msg = msg.trim();
            msg = "Problem message: " + msg;
            rslt = new SvgException(msg, rslt);

            // Add wrapped exception for problem code and problem parameters.
            String code = bex.getCode();
            String detailsTxt = code + Arrays.deepToString(params);
            msg = fmt("Problem details: %s", detailsTxt);
            rslt = new SvgException(msg, rslt);

            // Add wrapped exception for element name and id.
            String elemName = (elem == null) ? null : elem.getLocalName();
            String elemTxt = (elemName == null) ? "" : "\"" + elemName + "\" ";
            String id = (elem == null) ? null : SvgUtils.getSvgElementId(elem);
            String idTxt = (id == null) ? "unknown id" : "id \"" + id + "\"";
            msg = fmt("Problem for SVG %selement with %s.", elemTxt, idTxt);
            rslt = new SvgException(msg, rslt);

            return rslt;
        } else if (ex instanceof DOMException) {
            return new SvgException("SVG DOM problem.", ex);
        }

        // Not an end-user error. Keep exception as is.
        return ex;
    }

    /**
     * Reports the problem recorded by this user agent, if any.
     *
     * <p>
     * This method may throw an {@link SvgException}, in which case it is recommended to wrap that exception in another
     * {@link SvgException}, which indicates the kind of SVG operation that failed (usually rendering), and the path to
     * the SVG image file for which it failed.
     * </p>
     */
    public void reportProblem() {
        if (problem == null) {
            return;
        }

        if (problem instanceof SvgException) {
            throw (SvgException)problem;
        } else if (problem instanceof OutOfMemoryError) {
            throw (OutOfMemoryError)problem;
        } else {
            String msg = fmt("%s reported problem.", getClass().getName());
            throw new RuntimeException(msg, problem);
        }
    }
}
