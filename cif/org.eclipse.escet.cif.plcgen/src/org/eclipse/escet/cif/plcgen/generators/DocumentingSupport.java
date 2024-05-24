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

package org.eclipse.escet.cif.plcgen.generators;

import static org.eclipse.escet.cif.common.CifTextUtils.getAbsName;

import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** Support class for generating documentation comments for CIF elements. */
public class DocumentingSupport {
    /** Constructor of the {@link DocumentingSupport} class. */
    private DocumentingSupport() {
        // Static class.
    }

    /**
     * Get a description of a CIF element in PLC context. This function supports continuous variables, discrete
     * variables, input variables, events and complex components.
     *
     * <p>
     * This function can only describe the value of a derivative variable. To get a description for the derivative of a
     * continuous variable, use {@link #getDescription(PositionObject, boolean)}.
     * </p>
     *
     * @param posObj CIF element to describe.
     * @return The description of the given object.
     */
    public static String getDescription(PositionObject posObj) {
        return getDescription(posObj, false);
    }

    /**
     * Get a description of a CIF element in PLC context. This function supports continuous variables, discrete
     * variables, input variables, events and complex components.
     *
     * @param posObj CIF element to describe.
     * @param isDerivative Whether the derivative of the CIF element is intended. Is only used if the {@code posObj} is
     *     a continuous variable.
     * @return The description of the given object.
     */
    public static String getDescription(PositionObject posObj, boolean isDerivative) {
        if (posObj instanceof ContVariable) {
            String text = "continuous variable \"" + getAbsName(posObj, false) + "\"";
            return (isDerivative ? "derivative of " : "") + text;
        } else if (posObj instanceof DiscVariable dvar) {
            if (dvar.getName().isEmpty()) { // ElimLocRef transformation introduces variables with enpty name.
                Automaton aut = (Automaton)posObj.eContainer();
                return "current location of automaton \"" + getAbsName(aut, false) + "\"";
            } else {
                return "discrete variable \"" + getAbsName(posObj, false) + "\"";
            }
        } else if (posObj instanceof InputVariable) {
            return "input variable \"" + getAbsName(posObj, false) + "\"";
        } else if (posObj instanceof Event evt) {
            if (evt.getControllable() == null) {
                return "event \"" + getAbsName(posObj, false) + "\"";
            } else if (evt.getControllable()) {
                return "controllable event \"" + getAbsName(posObj, false) + "\"";
            } else {
                return "uncontrollable event \"" + getAbsName(posObj, false) + "\"";
            }
        } else if (posObj instanceof Automaton) {
            return "automaton \"" + getAbsName(posObj, false) + "\"";
        } else if (posObj instanceof Specification) {
            return "specification (top-level group)";
        } else if (posObj instanceof Group) {
            return "group \"" + getAbsName(posObj, false) + "\"";
        } else {
            throw new AssertionError("Unexpected position object \"" + posObj + "\" found.");
        }
    }
}
