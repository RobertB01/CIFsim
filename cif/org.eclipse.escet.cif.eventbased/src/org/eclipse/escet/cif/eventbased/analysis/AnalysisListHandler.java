//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.eventbased.analysis;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

/** Handler for clicking in the list of locations of an automaton. */
public class AnalysisListHandler implements Listener {
    /** List being handled. */
    private final Combo locationsList;

    /** Selected value in the list. */
    public int value = 0;

    /**
     * Constructor of the {@link AnalysisListHandler} class.
     *
     * @param parent Parent widget of the selection list.
     * @param preIndent Amount of vertical indent above the label.
     * @param autInfo Information of the displayed automaton.
     */
    public AnalysisListHandler(Composite parent, int preIndent, AutomatonNamesInfo autInfo) {
        // Add label.
        Label lbl = new Label(parent, SWT.HORIZONTAL | SWT.LEFT);
        lbl.setText(autInfo.autName);
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        if (preIndent != 0) {
            gridData.verticalIndent = preIndent;
        }
        lbl.setLayoutData(gridData);

        // Add combo drop-down.
        locationsList = new Combo(parent, SWT.READ_ONLY | SWT.DROP_DOWN);
        List<String> locNames = autInfo.locNames;
        for (String locName: locNames) {
            locationsList.add(locName);
        }
        locationsList.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        locationsList.select(0);
        locationsList.addListener(SWT.Selection, this);
    }

    @Override
    public void handleEvent(Event event) {
        value = locationsList.getSelectionIndex();
    }

    /**
     * Set the selected location of the automaton.
     *
     * @param newValue Location index number to select.
     */
    public void setValue(int newValue) {
        if (value == newValue) {
            return;
        }
        value = newValue;
        locationsList.select(newValue);
    }
}
