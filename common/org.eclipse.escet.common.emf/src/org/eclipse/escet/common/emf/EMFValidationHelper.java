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

package org.eclipse.escet.common.emf;

import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.Diagnostician;

/** EMF validation helper methods. */
public class EMFValidationHelper {
    /** The indentation to use for diagnostic child messages. */
    private static final String DIAGNOSTIC_INDENT = "  ";

    /** Constructor for the {@link EMFValidationHelper} class. */
    private EMFValidationHelper() {
        // Static class.
    }

    /**
     * Performs EMF model validation on the given EMF object.
     *
     * @param obj The EMF object to validate.
     * @throws EMFValidationException If validation fails.
     */
    public static void validateObject(EObject obj) {
        Diagnostic diagnostic = Diagnostician.INSTANCE.validate(obj);
        if (diagnostic.getSeverity() == Diagnostic.OK) {
            return;
        }

        List<String> diagMsgs = getDiagMsgs(diagnostic, " - ");
        String msg = "EMF validation resulted in warnings/errors:";
        List<String> messages = list(msg);
        messages.addAll(diagMsgs);

        throw new EMFValidationException(StringUtils.join(messages, "\n"));
    }

    /**
     * Retrieves diagnostic messages, with given indentation.
     *
     * @param diagnostic The diagnostic information.
     * @param indent The indentation to use.
     * @return The retrieved diagnostic messages.
     */
    private static List<String> getDiagMsgs(Diagnostic diagnostic, String indent) {
        List<String> rslt = list();
        rslt.addAll(getDiagMsgs(diagnostic, indent, true));
        for (Diagnostic child: diagnostic.getChildren()) {
            rslt.addAll(getDiagMsgs(child, DIAGNOSTIC_INDENT + indent));
        }

        rslt.addAll(getDiagMsgs(diagnostic, indent, false));
        return rslt;
    }

    /**
     * Retrieves diagnostic messages, with given indentation, either for 'pre-children' output, or for 'post-children'
     * output.
     *
     * @param diagnostic The diagnostic information.
     * @param indent The indentation to use.
     * @param preChilds {@code true} for 'pre-children' output, and {@code false} for 'post-children' output.
     * @return The retrieved diagnostic messages.
     */
    private static List<String> getDiagMsgs(Diagnostic diagnostic, String indent, boolean preChilds) {
        if (diagnostic instanceof BasicDiagnostic) {
            return getDiagMsgsEmfValidation((BasicDiagnostic)diagnostic, indent, preChilds);
        }
        return getDiagMsgsGeneric(diagnostic, indent, preChilds);
    }

    /**
     * Retrieves diagnostic messages, with given indentation, either for 'pre-children' output, or for 'post-children'
     * output. This method applies to any object implementing the {@link Diagnostic} interface.
     *
     * @param diagnostic The diagnostic information.
     * @param indent The indentation to use.
     * @param preChilds {@code true} for 'pre-children' output, and {@code false} for 'post-children' output.
     * @return The retrieved diagnostic messages.
     */
    private static List<String> getDiagMsgsGeneric(Diagnostic diagnostic, String indent, boolean preChilds) {
        if (preChilds) {
            String msg = indent + "(Severity = " + diagSeverityToStr(diagnostic.getSeverity()) + "): "
                    + diagnostic.getMessage();
            return list(msg);
        }
        return list();
    }

    /**
     * Converts an EMF {@link Diagnostic} severity code to a human-readable representation.
     *
     * @param code The EMF diagnostic severity code to convert.
     * @return A human-readable representation of the EMF diagnostic severity code.
     */
    private static String diagSeverityToStr(int code) {
        if (code == Diagnostic.OK) {
            return "OK";
        }

        List<String> rslt = list();
        if ((code & Diagnostic.CANCEL) > 0) {
            rslt.add("CANCEL");
        }
        if ((code & Diagnostic.ERROR) > 0) {
            rslt.add("ERROR");
        }
        if ((code & Diagnostic.WARNING) > 0) {
            rslt.add("WARNING");
        }
        if ((code & Diagnostic.INFO) > 0) {
            rslt.add("INFO");
        }
        return StringUtils.join(rslt, ", ");
    }

    /**
     * Retrieves diagnostic messages, with given indentation, either for 'pre-children' output, or for 'post-children'
     * output. This method applies to EMF validation {@link BasicDiagnostic} information only.
     *
     * @param diagnostic The diagnostic information.
     * @param indent The indentation to use.
     * @param preChilds {@code true} for 'pre-children' output, and {@code false} for 'post-children' output.
     * @return The retrieved diagnostic messages.
     */
    private static List<String> getDiagMsgsEmfValidation(BasicDiagnostic diagnostic, String indent, boolean preChilds) {
        if (!preChilds) {
            return getDiagMsgsGeneric(diagnostic, indent, preChilds);
        }

        List<String> rslt = getDiagMsgsGeneric(diagnostic, indent, preChilds);

        if (diagnostic.getData().size() >= 2 && diagnostic.getData().get(0) instanceof EObject) {
            EObject eObj = (EObject)diagnostic.getData().get(0);
            if (diagnostic.getData().get(1) == null || diagnostic.getData().get(1) instanceof EStructuralFeature) {
                EStructuralFeature eFeat = (EStructuralFeature)diagnostic.getData().get(1);
                String msg = DIAGNOSTIC_INDENT + indent + "Path: " + makePath(eObj, eFeat);
                rslt.add(msg);
            }
        }

        return rslt;
    }

    /**
     * Constructs a human-readable path from the model root to the given structural feature of an EMF object.
     *
     * @param eObj The destination object.
     * @param eFeat The structural feature of the destination object. May be {@code null} if the object itself is the
     *     destination.
     * @return Text describing the path from the model root to (structural feature of the) the destination object.
     */
    private static String makePath(EObject eObj, EStructuralFeature eFeat) {
        EMFPath emfPath = new EMFPath(eObj, eFeat);
        return emfPath.toString();
    }
}
