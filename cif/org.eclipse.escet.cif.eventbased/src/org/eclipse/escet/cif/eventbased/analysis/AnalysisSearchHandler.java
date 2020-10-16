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

import org.eclipse.escet.cif.eventbased.apps.SynthesisAnalysisEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/** Event handler of the search button. */
public class AnalysisSearchHandler implements SelectionListener, VerifyListener {
    /** The application. */
    private SynthesisAnalysisEditor app = null;

    /** Button to jump to the initial state. */
    private Button initialState;

    /** Button to go back to the previously analyzed state. */
    private Button backButton;

    /** Button with 'search state'. */
    private Button searchButton;

    /** Toggle whether to display the location of each automaton behind each state. */
    private Button showFullState;

    /** Toggle whether to always display disabled controllable events. */
    private Button showControllables;

    /** Toggle whether to display disabled events caused by plant restrictions. */
    private Button showPlantRemoveds;

    /** Number of intermediate state to print when traversing chains. */
    private Text numberStates;

    /**
     * Constructor of the {@link AnalysisSearchHandler} class.
     *
     * @param root Root window.
     */
    public AnalysisSearchHandler(Composite root) {
        GridData gridData;
        Label lbl;

        Composite comp = new Composite(root, SWT.NONE);

        // 4 rows (one for each setting), and 4 columns (left for the buttons,
        // second column for stretching, 3rd and 4th columns for settings, where
        // checkbox toggles take 2 columns, and text settings use a column for
        // the text, and one for the label).
        GridLayout compGrid = new GridLayout(4, false);
        comp.setLayout(compGrid);

        final int numberSettings = 4; // Number of settings at the right side.

        // First row.

        // Left column, top-row: 'Initial state' button.
        initialState = new Button(comp, SWT.PUSH);
        initialState.setText("Jump to initial state");
        gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
        gridData.verticalSpan = 1;
        initialState.setLayoutData(gridData);
        initialState.addSelectionListener(this);

        // Middle column: empty label.
        lbl = new Label(comp, SWT.CENTER);
        gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        gridData.minimumWidth = 50;
        gridData.verticalSpan = numberSettings;
        lbl.setLayoutData(gridData);

        // Right column, 1st toggle: Display full state.
        showFullState = new Button(comp, SWT.CHECK);
        showFullState.setText("Display full state");
        showFullState.setSelection(false);
        gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
        gridData.horizontalSpan = 2;
        showFullState.setLayoutData(gridData);
        showFullState.addSelectionListener(this);

        // Second row.

        // Left column, bottom 3 rows: 'Search information' button.
        backButton = new Button(comp, SWT.PUSH);
        backButton.setText("Previous state");
        gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
        gridData.verticalSpan = 1;
        backButton.setLayoutData(gridData);
        backButton.addSelectionListener(this);

        // Right column, 2nd toggle: Always display disabled controllable events.
        showControllables = new Button(comp, SWT.CHECK);
        showControllables.setText("Always display disabled controllable events");
        showControllables.setSelection(false);
        gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
        gridData.horizontalSpan = 2;
        showControllables.setLayoutData(gridData);
        showControllables.addSelectionListener(this);

        // Third row.

        // Left column, bottom 2 rows: 'Search information' button.
        searchButton = new Button(comp, SWT.PUSH);
        searchButton.setText("Search state");
        gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
        gridData.minimumHeight = 100;
        gridData.minimumWidth = 300;
        gridData.verticalSpan = numberSettings - 2;
        searchButton.setLayoutData(gridData);
        searchButton.addSelectionListener(this);

        // Right column, 3rd toggle: Display disabled plant events.
        showPlantRemoveds = new Button(comp, SWT.CHECK);
        showPlantRemoveds.setText("Display disabled plant events");
        showPlantRemoveds.setSelection(false);
        gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
        gridData.horizontalSpan = 2;
        showPlantRemoveds.setLayoutData(gridData);
        showPlantRemoveds.addSelectionListener(this);

        // Fourth row.

        // Right column, 4th setting: Number of displayed states in a chain
        numberStates = new Text(comp, SWT.LEFT | SWT.BORDER | SWT.SINGLE);
        numberStates.setTextLimit(5);
        numberStates.setText("5");
        gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
        gridData.widthHint = 30; // minimumWidth is ignored, but this hint is not.
        numberStates.setLayoutData(gridData);
        numberStates.addVerifyListener(this);
        numberStates.addSelectionListener(this);

        lbl = new Label(comp, SWT.VERTICAL);
        lbl.setText("Number of displayed states in a chain");
        gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
        lbl.setLayoutData(gridData);

        // Layout composite in shell.
        gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        comp.setLayoutData(gridData);
    }

    @Override
    public void verifyText(VerifyEvent e) {
        // Assuming the text in the widget is already correct, only the new text needs checking.
        for (int i = 0; i < e.text.length(); i++) {
            if (e.text.charAt(i) < '0' || e.text.charAt(i) > '9') {
                e.doit = false;
                return;
            }
        }
    }

    /**
     * Set the application to call on events.
     *
     * @param app Application that should handle the events.
     */
    public void setApplication(SynthesisAnalysisEditor app) {
        this.app = app;
    }

    @Override
    public void widgetSelected(SelectionEvent e) {
        if (app == null) {
            return;
        }

        if (e.widget == initialState) {
            app.clickedResetState();
        } else if (e.widget == backButton) {
            app.clickedSearch(true);
        } else {
            app.clickedSearch(false); // Treat all other select events as a click on the search button.
        }
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent e) {
        widgetSelected(e);
    }

    /**
     * Whether to output the location of each automaton behind every state number.
     *
     * @return Whether to output the location of each automaton behind every state number.
     */
    public boolean getFullStateDisplayed() {
        return showFullState.getSelection();
    }

    /**
     * Whether to always display disabled controllable events.
     *
     * @return Whether to always display disabled controllable events.
     */
    public boolean getDisabledControllablesDisplayed() {
        return showControllables.getSelection();
    }

    /**
     * Whether to display disabled events due to plant restrictions.
     *
     * @return Whether to display disabled events due to plant restrictions.
     */
    public boolean getPlantRemovedDisplayed() {
        return showPlantRemoveds.getSelection();
    }

    /**
     * Get the number of intermediate states to output when traversing a chain.
     *
     * @return Number of intermediate states to output.
     */
    public int getNumberIntermediateStates() {
        String s = numberStates.getText();
        if (s.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(s, 10); // Should never fail.
    }

    /**
     * Set the enabled state of the 'back' button.
     *
     * @param enable If {@code true} enable the button, else disable the button.
     */
    public void setEnableBack(boolean enable) {
        backButton.setEnabled(enable);
    }
}
