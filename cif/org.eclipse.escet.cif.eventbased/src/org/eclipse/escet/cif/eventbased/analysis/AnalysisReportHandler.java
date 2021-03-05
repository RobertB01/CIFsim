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

package org.eclipse.escet.cif.eventbased.analysis;

import static org.eclipse.escet.cif.eventbased.apps.SynthesisAnalysisEditor.makeCountText;

import java.util.List;

import org.eclipse.escet.cif.eventbased.analysis.reporttext.ReportText;
import org.eclipse.escet.cif.eventbased.analysis.reporttext.SimpleText;
import org.eclipse.escet.cif.eventbased.apps.SynthesisAnalysisEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/** Widget and handler of the report area of the synthesis analysis application. */
public class AnalysisReportHandler implements Listener {
    /** The application. */
    private SynthesisAnalysisEditor app = null;

    /** Widget displaying the report area. */
    private StyledText reportWidget;

    /**
     * Constructor of the {@link AnalysisReportHandler} class.
     *
     * @param root Root window.
     */
    public AnalysisReportHandler(Composite root) {
        ReportText reportText = new SimpleText("Initializing...");

        // Window displaying the found information.
        reportWidget = new StyledText(root, SWT.BORDER | SWT.MULTI | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
        reportWidget.setSize(600, 500);
        setResult(reportText);
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        reportWidget.setLayoutData(gridData);
        reportWidget.addListener(SWT.MouseDoubleClick, this);
    }

    /**
     * Set the application to call on events.
     *
     * @param app Application that should handle the events.
     */
    public void setApplication(SynthesisAnalysisEditor app) {
        this.app = app;

        int numPl = app.data.getNumberPlants();
        int numRq = app.data.getNumberAutomata() - numPl;
        int created = app.data.states.size();
        int removed = app.data.getRemovedCount();
        int remain = created - removed;
        ReportText reportText = new SimpleText(
                "Synthesis analysis tool\r\n" + makeCountText(numPl, "%d plant %s.\r\n", "automaton", "automata")
                        + makeCountText(numRq, "%d requirement %s.\r\n", "automaton", "automata")
                        + makeCountText(created, "Synthesis created %d %s, ", "state", "states")
                        + makeCountText(removed, "removed %d %s, ", "state", "states")
                        + makeCountText(remain, "giving %d %s in the result.\r\n", "state", "states")
                        + "\r\nSelect a combination of locations at the left, and press 'search state' to analyse "
                        + "the state.\r\n");

        setResult(reportText);
    }

    /**
     * Display the provided text as output onto the report widget.
     *
     * @param text Text to display.
     */
    public void setResult(ReportText text) {
        reportWidget.setStyleRange(null);
        reportWidget.setText("");

        text.addText(reportWidget, 0);
    }

    /**
     * Display the provided texts as output onto the report widget.
     *
     * @param texts Texts to display.
     */
    public void setResult(List<ReportText> texts) {
        reportWidget.setStyleRange(null);
        reportWidget.setText("");

        int length = 0;
        for (ReportText text: texts) {
            length = text.addText(reportWidget, length);
        }
    }

    /**
     * Append the provided text to the end of the output of the report.
     *
     * @param text Additional text to display.
     */
    public void addResult(ReportText text) {
        int length = reportWidget.getContent().getCharCount();
        text.addText(reportWidget, length);
    }

    /**
     * Append the provided texts to the end of the output of the report.
     *
     * @param texts Additional texts to display.
     */
    public void addResult(List<ReportText> texts) {
        int length = reportWidget.getContent().getCharCount();
        for (ReportText text: texts) {
            length = text.addText(reportWidget, length);
        }
    }

    /**
     * Check whether the character at the given offset in the text is a digit, and if so, get its numeric value.
     *
     * @param reportText Text of the report.
     * @param offset Offset to test.
     * @return Numeric value of the digit at the given offset, or {@code -1}.
     */
    private static int digitValue(String reportText, int offset) {
        if (offset < 0 || offset >= reportText.length()) {
            return -1;
        }
        char k = reportText.charAt(offset);
        if (k >= '0' && k <= '9') {
            return k - '0';
        }
        return -1;
    }

    @Override
    public void handleEvent(Event event) {
        if (app == null) {
            return; // Application is still initializing.
        }

        // On double click, check whether there is a number, at the clicked
        // position, and parse it.
        int offset = reportWidget.getOffsetAtPoint(new Point(event.x, event.y));
        if (offset == -1) {
            // No character under event.x, event.y.
            return;
        }

        String reportText = reportWidget.getText();

        // Find '#' in (hopefully) "#12345" in a very local backward search.
        int count = 0;
        while (count < 10 && offset >= 0) {
            if (offset < reportText.length() && reportText.charAt(offset) == '#') {
                break;
            }

            count++;
            offset--;
        }
        if (count >= 10 || offset < 0) {
            return;
        }

        offset++;
        if (digitValue(reportText, offset) < 0) {
            return; // Not a number behind '#'.
        }
        int value = digitValue(reportText, offset);
        while (true) {
            if (value < 0) {
                return; // Overflow.
            }
            offset++;
            int digit = digitValue(reportText, offset);
            if (digit < 0) {
                break;
            }
            value = value * 10 + digit;
        }
        app.setSelectedState(value);
    }
}
