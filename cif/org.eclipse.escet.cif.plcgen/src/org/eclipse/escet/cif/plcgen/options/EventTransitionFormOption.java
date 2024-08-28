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

package org.eclipse.escet.cif.plcgen.options;

import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.makeElementsChoiceText;

import java.util.Arrays;
import java.util.List;

import org.eclipse.escet.common.app.framework.options.EnumOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** Option to specify the form of event transitions. */
public class EventTransitionFormOption extends EnumOption<EventTransitionForm> {
    /** Default value of the option. */
    private static final EventTransitionForm DEFAULT_VALUE = EventTransitionForm.CODE_IN_MAIN;

    /** Constructor of the {@link EventTransitionFormOption}. */
    public EventTransitionFormOption() {
        super("Event transitions form",
                "Specify where the event transitions code should be created. Specify "
                        + makeValueDescriptions() + ". [DEFAULT=\"" + DEFAULT_VALUE.name + "\"]",
                null,
                "transitions-form",
                "FORM",
                DEFAULT_VALUE,
                true,
                "Specify where the event transition code should be created.");
    }

    @Override
    protected String getDialogText(EventTransitionForm value) {
        return value.description;
    }

    /**
     * Construct the descriptive text of the option.
     *
     * @return The descriptive text of the option.
     */
    private static String makeValueDescriptions() {
        List<EventTransitionForm> enumValues = Arrays.asList(EventTransitionForm.values());
        return makeElementsChoiceText(enumValues, v -> fmt("\"%s\" for %s", v.name, v.shortDescription));
    }

    /**
     * Get the selected value of the {@link EventTransitionFormOption} option.
     *
     * @return The selected value of the option.
     */
    public static EventTransitionForm getValue() {
        return Options.get(EventTransitionFormOption.class);
    }
}
