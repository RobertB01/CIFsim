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

package org.eclipse.escet.cif.simulator.input;

import static org.eclipse.escet.cif.simulator.runtime.CifSimulatorMath.realToStr;
import static org.eclipse.escet.common.java.Lists.filter;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.str;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.escet.cif.simulator.runtime.model.RuntimeEvent;
import org.eclipse.escet.cif.simulator.runtime.model.RuntimeSpec;
import org.eclipse.escet.cif.simulator.runtime.model.RuntimeState;
import org.eclipse.escet.cif.simulator.runtime.ode.Trajectories;
import org.eclipse.escet.cif.simulator.runtime.transitions.EventTransition;
import org.eclipse.escet.cif.simulator.runtime.transitions.TimeTransition;
import org.eclipse.escet.cif.simulator.runtime.transitions.Transition;
import org.eclipse.escet.common.eclipse.ui.ControlEditor;
import org.eclipse.escet.common.eclipse.ui.SelectionListenerBase;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

/**
 * Graphical editor component of the {@link InteractiveGuiInputComponent}.
 *
 * @param <S> The type of state objects to use.
 */
public class InteractiveGuiInputEditor<S extends RuntimeState> extends ControlEditor {
    /** 0-based index of color composite widgets among their siblings. */
    private static final int COLOR_COMP_IDX = 0;

    /** 0-based index of button widgets among their siblings. */
    private static final int BUTTON_IDX = 1;

    /** 0-based index of count label widgets among their siblings. */
    private static final int CNT_LBL_IDX = 2;

    /** 0-based index of arrow button widgets among their siblings. */
    private static final int ARROW_IDX = 3;

    /** The runtime specification, if available among its siblings. */
    private RuntimeSpec<S> spec = null;

    /** The interactive GUI input component, if available. */
    private InteractiveGuiInputComponent<?> inputComp = null;

    /** Color for non-event choices (e.g. time, history), if available. */
    private Color otherColor = null;

    /** Color for events that are neither controllable nor uncontrollable, if available. */
    private Color eventColor = null;

    /** Color for events that are controllable, if available. */
    private Color ctrlColor = null;

    /** Color for events that are uncontrollable, if available. */
    private Color unctrlColor = null;

    /** Color for disabled, if available. */
    private Color disabledColor = null;

    /** The scrolled composite with the optional scroll bar, if available. */
    private ScrolledComposite scroll = null;

    /** The composite that contains the buttons, if available. */
    private Composite buttons = null;

    /** The italic button label font, if available. */
    private Font italicFont = null;

    /** The popup menu, if available. */
    private Menu popupMenu = null;

    /** The transitions to choose from, {@code null} if not available. */
    private List<Transition<S>> transitions = null;

    /** The current state, {@code null} if not available. */
    private S state = null;

    /**
     * Synchronization object to use to signal the {@link InteractiveGuiInputComponent} that the user has made a choice.
     */
    public final AtomicBoolean ready = new AtomicBoolean(false);

    /** The last choice made. Has value {@code null} not available/applicable. */
    public final AtomicReference<InteractiveGuiInputChoice> choice = new AtomicReference<>(null);

    @Override
    protected Control createContents(Composite parent) {
        // Scroll the contents horizontally, if it doesn't fit.
        parent.setLayout(new FillLayout());
        scroll = new ScrolledComposite(parent, SWT.H_SCROLL);
        scroll.setExpandHorizontal(true);
        scroll.setExpandVertical(true);

        // Contents of the scrolled area is the buttons.
        buttons = new Composite(scroll, SWT.NONE);
        scroll.setContent(buttons);

        // White background.
        Color white = buttons.getDisplay().getSystemColor(SWT.COLOR_WHITE);
        buttons.setBackground(white);

        // Update scroll bar when contents changes size.
        scroll.addControlListener(new ControlAdapter() {
            @Override
            public void controlResized(ControlEvent e) {
                Rectangle r = scroll.getClientArea();
                scroll.setMinSize(buttons.computeSize(SWT.DEFAULT, r.height));
            }
        });

        // Buttons in vertical row layout.
        RowLayout row = new RowLayout(SWT.VERTICAL);
        row.wrap = true;
        row.fill = true;
        row.spacing = 3;
        buttons.setLayout(row);

        // Create popup menu.
        popupMenu = new Menu(buttons);

        // Get/create colors.
        otherColor = buttons.getDisplay().getSystemColor(SWT.COLOR_BLACK);
        eventColor = new Color(0, 97, 192);
        ctrlColor = new Color(0, 128, 0);
        unctrlColor = new Color(160, 0, 0);
        disabledColor = new Color(192, 192, 192);

        // Get italic font.
        FontData[] fontDatas = buttons.getFont().getFontData();
        fontDatas[0].setStyle(fontDatas[0].getStyle() | SWT.ITALIC);
        italicFont = new Font(parent.getDisplay(), fontDatas);

        // Inform when GUI is closed.
        scroll.addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent e) {
                resetAndDisableUI();
                inform(new InteractiveGuiInputChoice());
            }
        });

        // Outer control is the composite with scroll bars.
        return scroll;
    }

    /**
     * Initializes the GUI with the buttons for the events, etc.
     *
     * @param spec The specification.
     * @param input The interactive GUI input component.
     */
    public void init(RuntimeSpec<S> spec, InteractiveGuiInputComponent<?> input) {
        // Store data for later use.
        this.spec = spec;
        this.inputComp = input;

        // Add buttons for the events, 'time', 'reset', and 'undo'.
        int cnt = spec.events.size() + 3;
        for (int i = 0; i < cnt; i++) {
            // Is this an event, 'time', 'reset', or 'undo'?
            final boolean isEvent = i < spec.events.size();
            final boolean isTime = i == spec.events.size();
            final boolean isReset = i == spec.events.size() + 1;
            final boolean isUndo = i == spec.events.size() + 2;

            // Skip if automatically chosen (events, time), or disabled (reset,
            // undo).
            if (isEvent && input.autoEvents[i]) {
                continue;
            }
            if (isTime && input.autoTime && input.autoTimeDur) {
                continue;
            }
            if (isReset && !input.isResetEnabled) {
                continue;
            }
            if (isUndo && !input.isUndoEnabled) {
                continue;
            }

            // Get the event. Skip 'tau' event if specification has no 'tau'
            // edges.
            final RuntimeEvent<?> event = isEvent ? spec.events.get(i) : null;
            if (event != null && event.isTauEvent && !spec.hasTauEdge()) {
                continue;
            }

            // Create composite with a border, to make it clear which GUI
            // elements belong together.
            Composite comp = new Composite(buttons, SWT.BORDER);

            // Set custom properties, to be able to distinguish buttons later
            // on.
            comp.setData("eventIdx", isEvent ? i : -1);
            String kind;
            if (isEvent) {
                kind = "event";
            } else if (isTime) {
                kind = "time";
            } else if (isReset) {
                kind = "reset";
            } else if (isUndo) {
                kind = "undo";
            } else {
                throw new RuntimeException("Unknown transition.");
            }
            comp.setData("kind", kind);

            // Create color composite.
            Composite colorComp = new Composite(comp, SWT.NONE);

            // Create button with the name of the event or an other text.
            Button button = new Button(comp, SWT.PUSH);
            button.setAlignment(SWT.LEFT);
            if (event != null && !spec.urgent[i]) {
                button.setFont(italicFont);
            }
            if (isEvent) {
                if (event == null) {
                    throw new RuntimeException();
                }
                button.setText(event.name);
            } else if (isTime) {
                button.setText("time delay");
            } else if (isReset) {
                button.setText("reset");
            } else if (isUndo) {
                button.setText("undo");
            } else {
                throw new RuntimeException("Unknown button.");
            }

            // Set color of the text of the button.
            button.setForeground(getColor(event, true));

            // Set up button click handler.
            final int eventIdx = i;
            button.addSelectionListener(new SelectionListenerBase() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    InteractiveGuiInputChoice choiceData = new InteractiveGuiInputChoice();

                    if (isTime) {
                        // Time.
                        choiceData.timeChosen = true;
                        if (inputComp.autoTimeDur) {
                            // Automatic input component chooses duration.
                            choiceData.targetTime = null;
                        } else {
                            // Get single time transition.
                            @SuppressWarnings("unchecked")
                            List<TimeTransition<S>> filtered = filter(transitions, TimeTransition.class);
                            Assert.check(filtered.size() == 1);
                            TimeTransition<S> transition = first(filtered);

                            // Choose maximum target time.
                            double timePre = transition.source.getTime();
                            double timePost = transition.getLastTime();
                            ChosenTargetTime targetTime = new ChosenTargetTime(timePre, timePost, true);
                            choiceData.targetTime = targetTime;
                        }
                    } else if (isEvent) {
                        // Event.
                        choiceData.eventIdx = eventIdx;
                    } else if (isReset) {
                        // Reset.
                        choiceData.resetChosen = true;
                    } else if (isUndo) {
                        // Undo.
                        choiceData.undoCount = 1;
                    } else {
                        throw new RuntimeException("Unknown button.");
                    }

                    resetAndDisableUI();
                    inform(choiceData);
                }
            });

            // Create transition count label. For events, it indicates the
            // number of transitions possible (if more than one). For 'time',
            // it indicates the duration that is chosen when clicking the
            // button (the count in number of time units). For 'reset', the
            // label is not used. For 'undo', the label indicates the maximum
            // number of transitions that can be undone.
            final Label countLabel = new Label(comp, SWT.NONE);
            countLabel.setText("");

            // Create arrow button.
            final Button arrow = new Button(comp, SWT.ARROW | SWT.DOWN);

            // Set up arrow button click hander.
            arrow.addSelectionListener(new SelectionListenerBase() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    // Get arrow button position and height.
                    Point pos = arrow.toDisplay(e.x, e.y);
                    int height = arrow.getSize().y;

                    // Add popup menu items and show the popup menu.
                    addPopupMenuItems(event, kind);
                    popupMenu.setLocation(pos.x, pos.y + height);
                    popupMenu.setVisible(true);
                }
            });

            // Disable buttons, for now.
            colorComp.setBackground(getColor(event, false));
            button.setEnabled(false);
            arrow.setEnabled(false);

            // Layout the buttons for this event.
            GridLayout gridLayout = new GridLayout(4, false);
            gridLayout.marginWidth = 0;
            gridLayout.marginHeight = 0;
            gridLayout.horizontalSpacing = 2;
            comp.setLayout(gridLayout);

            // Color component width.
            int buttonHeight = button.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).y;
            GridData colorCompData = new GridData();
            colorCompData.widthHint = 4;
            colorCompData.heightHint = buttonHeight;
            colorComp.setLayoutData(colorCompData);

            // Button with name can use remaining space.
            GridData buttonData = new GridData();
            buttonData.horizontalAlignment = SWT.FILL;
            buttonData.grabExcessHorizontalSpace = true;
            button.setLayoutData(buttonData);
        }

        // Force relayout.
        buttons.pack();
        buttons.layout();
        scroll.notifyListeners(SWT.Resize, null);
    }

    /**
     * Returns the color to use for a color component.
     *
     * @param event The event for which to determine the color of the color component, or {@code null} for 'time'.
     * @param enabled Whether to get an enabled color ({@code true}) or a disabled color ({@code false}).
     * @return The color to use.
     */
    private Color getColor(RuntimeEvent<?> event, boolean enabled) {
        if (!enabled) {
            return disabledColor;
        }
        if (event == null) {
            return otherColor;
        }
        if (event.controllable == null) {
            return eventColor;
        }
        if (event.controllable) {
            return ctrlColor;
        }
        return unctrlColor;
    }

    /**
     * Provides new transition choices for the user to choose from. The caller will wait for the user to make a choice.
     *
     * @param state The source state of the transitions.
     * @param transitions The transitions to choose from.
     */
    public void chooseTransition(S state, List<Transition<S>> transitions) {
        // Store transitions for later.
        this.transitions = transitions;
        this.state = state;

        // Get number of transitions per event, and whether a time transition
        // is present.
        int[] counts = new int[spec.events.size()];
        TimeTransition<?> timeTrans = null;
        for (Transition<?> transition: transitions) {
            if (transition instanceof EventTransition) {
                int eventIdx = ((EventTransition<?>)transition).event.idx;
                counts[eventIdx]++;
            } else {
                Assert.check(transition instanceof TimeTransition);
                timeTrans = (TimeTransition<?>)transition;
            }
        }

        // Get whether reset is possible, and how many transitions can be
        // undone.
        boolean resetPossible = false;
        int maxUndoCount = 0;
        if (inputComp.history != null) {
            resetPossible = inputComp.history.canReset(state);
            maxUndoCount = inputComp.history.getMaxUndoCount(state);
        }

        // Set transition count labels. Enable the appropriate buttons.
        Control firstEnabledButton = null;
        for (Control buttonsChild: buttons.getChildren()) {
            // Skip non-choice children.
            if (!(buttonsChild instanceof Composite)) {
                continue;
            }
            Composite comp = (Composite)buttonsChild;
            Control[] children = comp.getChildren();

            // Get event index, or '-1' for time. Check what kind of choice it
            // is.
            Object eventIdxObj = comp.getData("eventIdx");
            int eventIdx = (int)eventIdxObj;
            Object kindObj = comp.getData("kind");
            boolean isEvent = (eventIdx >= 0);
            boolean isTime = !isEvent && kindObj.equals("time");
            boolean isReset = !isEvent && kindObj.equals("reset");
            boolean isUndo = !isEvent && kindObj.equals("undo");

            // Get enabledness.
            int count = isEvent ? counts[eventIdx] : isTime ? (timeTrans == null ? 0 : 1)
                    : isReset ? (resetPossible ? 1 : 0) : isUndo ? maxUndoCount : -1; // Can't occur.
            boolean enabled = count > 0;

            // Get transition count label text.
            String cntTxt;
            if (isEvent) {
                // Event transition.
                cntTxt = (count <= 1) ? "" : fmt("(%d)", count);
            } else if (isTime && timeTrans == null) {
                // Time transition, disabled.
                cntTxt = "";
            } else if (isTime && timeTrans != null) {
                // Time transition, enabled.
                double maxDelay = timeTrans.getTrajectories().getMaxDelay();
                cntTxt = fmt("(~%.3g)", maxDelay);
            } else if (isReset) {
                // Reset transition.
                cntTxt = "";
            } else if (isUndo) {
                // Undo transition.
                cntTxt = enabled ? fmt("(%d)", count) : "";
            } else {
                throw new RuntimeException("Unknown transition.");
            }

            // Set transition count label.
            Label cntLbl = (Label)children[CNT_LBL_IDX];
            if (!cntLbl.getText().equals(cntTxt)) {
                cntLbl.setText(cntTxt);
                comp.pack();
                comp.layout();
            }

            // Set enabledness of buttons and color of color component.
            RuntimeEvent<?> event = isEvent ? spec.events.get(eventIdx) : null;
            children[COLOR_COMP_IDX].setBackground(getColor(event, enabled));
            children[BUTTON_IDX].setEnabled(enabled);
            children[ARROW_IDX].setEnabled(enabled);
            if (enabled && firstEnabledButton == null) {
                firstEnabledButton = children[BUTTON_IDX];
            }
        }

        buttons.pack();
        buttons.layout();

        if (firstEnabledButton != null) {
            firstEnabledButton.setFocus();
        }
    }

    /**
     * Provides new delay choices for the user to choose from. The caller will wait for the user to make a choice.
     *
     * @param state The source state of the time transition.
     * @param maxTargetTime The maximum allowed target time.
     */
    public void chooseDelay(S state, double maxTargetTime) {
        // Set dummy time transition, with appropriate source/target times.
        Trajectories trajs = new Trajectories();
        trajs.add(state.getTime(), new double[0]);
        trajs.add(maxTargetTime, new double[0]);
        transitions = list(new TimeTransition<>(spec, state, trajs));
        this.state = state;

        // Enable the appropriate buttons.
        Control firstEnabledButton = null;
        for (Control buttonsChild: buttons.getChildren()) {
            // Skip non-choice children.
            if (!(buttonsChild instanceof Composite)) {
                continue;
            }
            Composite comp = (Composite)buttonsChild;
            Control[] children = comp.getChildren();

            // Skip non-time choices, as they are already disabled.
            Object kindObj = comp.getData("kind");
            if (!kindObj.equals("time")) {
                continue;
            }

            // Get transition count label text.
            double maxDelay = trajs.getMaxDelay();
            String cntTxt = fmt("(~%.3g)", maxDelay);

            // Set transition count label.
            Label cntLbl = (Label)children[CNT_LBL_IDX];
            if (!cntLbl.getText().equals(cntTxt)) {
                cntLbl.setText(cntTxt);
                comp.pack();
                comp.layout();
            }

            // Set enabledness of buttons and color of color component.
            children[COLOR_COMP_IDX].setBackground(getColor(null, true));
            children[BUTTON_IDX].setEnabled(true);
            children[ARROW_IDX].setEnabled(true);
            if (firstEnabledButton == null) {
                firstEnabledButton = children[BUTTON_IDX];
            }
        }

        buttons.pack();
        buttons.layout();

        if (firstEnabledButton != null) {
            firstEnabledButton.setFocus();
        }
    }

    /**
     * Provides new choices for the user to choose from, for the case where no transitions are possible. The caller will
     * wait for the user to make a choice.
     *
     * @param state The source state of the transitions.
     */
    public void chooseNoTransition(S state) {
        // Store transitions for later.
        this.transitions = Collections.emptyList();
        this.state = state;

        // Get whether reset is possible, and how many transitions can be
        // undone.
        Assert.check(inputComp.history != null);
        boolean resetPossible = inputComp.history.canReset(state);
        int maxUndoCount = inputComp.history.getMaxUndoCount(state);

        // Set transition count labels. Enable the appropriate buttons.
        Control firstEnabledButton = null;
        for (Control buttonsChild: buttons.getChildren()) {
            // Skip non-choice children.
            if (!(buttonsChild instanceof Composite)) {
                continue;
            }
            Composite comp = (Composite)buttonsChild;
            Control[] children = comp.getChildren();

            // Check what kind of choice it is.
            Object kindObj = comp.getData("kind");
            boolean isReset = kindObj.equals("reset");
            boolean isUndo = kindObj.equals("undo");
            if (!isReset && !isUndo) {
                continue;
            }

            // Get enabledness.
            int count = isReset ? (resetPossible ? 1 : 0) : isUndo ? maxUndoCount : -1; // Can't occur.
            boolean enabled = count > 0;

            // Get transition count label text.
            String cntTxt;
            if (isReset) {
                // Reset transition.
                cntTxt = "";
            } else if (isUndo) {
                // Undo transition.
                cntTxt = enabled ? fmt("(%d)", count) : "";
            } else {
                throw new RuntimeException("Unknown transition.");
            }

            // Set transition count label.
            Label cntLbl = (Label)children[CNT_LBL_IDX];
            if (!cntLbl.getText().equals(cntTxt)) {
                cntLbl.setText(cntTxt);
                comp.pack();
                comp.layout();
            }

            // Set enabledness of buttons and color of color component.
            children[COLOR_COMP_IDX].setBackground(getColor(null, enabled));
            children[BUTTON_IDX].setEnabled(enabled);
            children[ARROW_IDX].setEnabled(enabled);
            if (enabled && firstEnabledButton == null) {
                firstEnabledButton = children[BUTTON_IDX];
            }
        }

        buttons.pack();
        buttons.layout();

        if (firstEnabledButton != null) {
            firstEnabledButton.setFocus();
        }
    }

    /**
     * Adds menu items to a popup menu.
     *
     * @param event The event for which to add items, or {@code null} if not adding items for an event.
     * @param kind The kind of items to add. Is {@code "event"}, {@code "time"}, {@code "reset"}, or {@code "undo"}.
     */
    private void addPopupMenuItems(RuntimeEvent<?> event, String kind) {
        // Remove old items.
        removePopupMenuItems();

        // Add new items.
        if (event != null) {
            // Event.
            for (int i = 0; i < transitions.size(); i++) {
                // Get transition.
                Transition<S> transition = transitions.get(i);

                // Skip if non-event or wrong event.
                if (!(transition instanceof EventTransition)) {
                    continue;
                }
                if (((EventTransition<S>)transition).event != event) {
                    continue;
                }

                // Create menu item.
                MenuItem item = new MenuItem(popupMenu, SWT.NONE);
                item.setText(transition.toString());

                // Set up click handling.
                final int transIdx = i;
                item.addSelectionListener(new SelectionListenerBase() {
                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        InteractiveGuiInputChoice choiceData = new InteractiveGuiInputChoice();
                        choiceData.transIdx = transIdx;
                        resetAndDisableUI();
                        inform(choiceData);
                    }
                });
            }
        } else if (kind.equals("time")) {
            // Time. Get single time transition.
            @SuppressWarnings("unchecked")
            List<TimeTransition<S>> filtered = filter(transitions, TimeTransition.class);
            Assert.check(filtered.size() == 1);
            final TimeTransition<S> transition = first(filtered);

            // Get time transition description.
            String description = transition.toString();

            // Create menu items.
            MenuItem[] menuItems = {null, null, null};
            final int AUTO_IDX = 0;
            final int CUSTOM_IDX = 1;
            final int MAX_IDX = 2;

            if (inputComp.autoTimeDur) {
                menuItems[AUTO_IDX] = new MenuItem(popupMenu, SWT.NONE);
                menuItems[AUTO_IDX].setText(description + ", automatic duration");
            } else {
                menuItems[CUSTOM_IDX] = new MenuItem(popupMenu, SWT.NONE);
                menuItems[CUSTOM_IDX].setText(description + ", custom duration...");

                menuItems[MAX_IDX] = new MenuItem(popupMenu, SWT.NONE);
                menuItems[MAX_IDX].setText(description + ", maximum duration");
            }

            // Set up click handling.
            for (int i = 0; i < menuItems.length; i++) {
                if (menuItems[i] == null) {
                    continue;
                }

                final int idx = i;
                menuItems[i].addSelectionListener(new SelectionListenerBase() {
                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        InteractiveGuiInputChoice choiceData = new InteractiveGuiInputChoice();
                        choiceData.timeChosen = true;
                        switch (idx) {
                            case AUTO_IDX:
                                // Automatic input component chooses duration.
                                choiceData.targetTime = null;
                                break;

                            case CUSTOM_IDX: {
                                // Custom duration.
                                Double delay = enterDelayDuration(transition);
                                if (delay == null) {
                                    return;
                                }
                                double timePre = transition.source.getTime();
                                double timePost = transition.getLastTime();
                                double time = timePre + delay;
                                if (time > timePost) {
                                    time = timePost;
                                }
                                ChosenTargetTime targetTime = new ChosenTargetTime(timePre, time, false);
                                choiceData.targetTime = targetTime;
                                break;
                            }

                            case MAX_IDX: {
                                // Maximum duration.
                                double timePre = transition.source.getTime();
                                double timePost = transition.getLastTime();
                                ChosenTargetTime targetTime = new ChosenTargetTime(timePre, timePost, true);
                                choiceData.targetTime = targetTime;
                                break;
                            }

                            default:
                                throw new RuntimeException("idx: " + idx);
                        }
                        resetAndDisableUI();
                        inform(choiceData);
                    }
                });
            }
        } else if (kind.equals("reset")) {
            // Reset. Create menu item.
            MenuItem menuItem = new MenuItem(popupMenu, SWT.NONE);
            menuItem.setText("reset to initial state");

            // Set up click handling.
            menuItem.addSelectionListener(new SelectionListenerBase() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    InteractiveGuiInputChoice choiceData = new InteractiveGuiInputChoice();
                    choiceData.resetChosen = true;
                    resetAndDisableUI();
                    inform(choiceData);
                }
            });
        } else if (kind.equals("undo")) {
            // Undo. Get maximum undo count.
            int maxUndoCount = inputComp.history.getMaxUndoCount(state);

            // Create menu items.
            int itemCount = Math.min(10, maxUndoCount);
            boolean addCustom = (itemCount != maxUndoCount);
            if (addCustom) {
                itemCount++;
            }
            MenuItem[] menuItems = new MenuItem[itemCount];
            for (int i = 0; i < itemCount; i++) {
                menuItems[i] = new MenuItem(popupMenu, SWT.NONE);
                String description;
                if (i == 0) {
                    description = "undo 1 transition";
                } else if (addCustom && i == itemCount - 1) {
                    description = fmt("undo custom number of transitions", i);
                } else {
                    description = fmt("undo %,d transitions", i + 1);
                }
                menuItems[i].setText(description);
            }

            // Set up click handling.
            final int customIdx = addCustom ? itemCount - 1 : -1;
            for (int i = 0; i < itemCount; i++) {
                final int idx = i;
                menuItems[i].addSelectionListener(new SelectionListenerBase() {
                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        InteractiveGuiInputChoice choiceData = new InteractiveGuiInputChoice();
                        choiceData.timeChosen = true;
                        if (idx == customIdx) {
                            // Custom number of transitions.
                            Integer count = enterUndoCount(maxUndoCount);
                            if (count == null) {
                                return;
                            }
                            choiceData.undoCount = count;
                        } else {
                            // Fixed number of transitions.
                            choiceData.undoCount = idx + 1;
                        }
                        resetAndDisableUI();
                        inform(choiceData);
                    }
                });
            }
        } else {
            throw new RuntimeException("Unknown kind: " + kind);
        }
    }

    /** Removes all {@link #popupMenu} items. */
    private void removePopupMenuItems() {
        if (popupMenu == null) {
            return;
        }
        if (popupMenu.isDisposed()) {
            return;
        }

        MenuItem[] items = popupMenu.getItems();
        for (MenuItem item: items) {
            item.dispose();
        }
    }

    /**
     * Interactively enter a delay duration for the time transition.
     *
     * @param transition The time transition.
     * @return The chosen delay, or {@code null} if cancelled.
     */
    private Double enterDelayDuration(TimeTransition<S> transition) {
        // Get maximum delay.
        final double timePre = transition.source.getTime();
        double timePost = transition.getLastTime();
        final double maxDelay = timePost - timePre;
        Assert.check(maxDelay > 0);

        // Get duration validator.
        IInputValidator validator = new IInputValidator() {
            @Override
            public String isValid(String newText) {
                // Parse.
                double duration;
                try {
                    duration = Double.parseDouble(newText);
                } catch (NumberFormatException ex) {
                    return fmt("Failed to parse delay duration \"%s\".", newText);
                }

                // Check range.
                if (Double.isNaN(duration)) {
                    return fmt("Invalid delay duration \"%s\".", newText);
                } else if (Double.isInfinite(duration)) {
                    return fmt("Invalid infinite delay duration \"%s\".", newText);
                } else if (duration <= 0.0 || maxDelay < duration) {
                    return fmt("Delay duration \"%s\" is not in range (0 .. %s].", newText, realToStr(maxDelay));
                }

                // Check too close to start.
                double targetTime = timePre + duration;
                if (targetTime == timePre) {
                    return fmt("Target time \"%s\" after delay \"%s\" is too close to the current time \"%s\".",
                            realToStr(targetTime), newText, realToStr(timePre));
                }

                // All OK.
                return null;
            }
        };

        // Get duration using input dialog.
        String title = "Time transition duration";
        String msg = fmt("Specify a time transition duration in the range (0 .. %s]:", maxDelay);
        InputDialog dialog = new InputDialog(contents.getShell(), title, msg, realToStr(maxDelay), validator);
        int result = dialog.open();

        // Check cancellation.
        if (result == Window.CANCEL) {
            return null;
        }

        // Get size.
        return Double.parseDouble(dialog.getValue());
    }

    /**
     * Interactively enter an undo count for the undo transition.
     *
     * @param maxUndoCount The maximum number of transitions that can be undone.
     * @return The chosen number of transitions to undo, or {@code null} if cancelled.
     */
    private Integer enterUndoCount(int maxUndoCount) {
        // Checking.
        Assert.check(maxUndoCount > 0);

        // Get count validator.
        IInputValidator validator = new IInputValidator() {
            @Override
            public String isValid(String newText) {
                // Parse.
                int count;
                try {
                    count = Integer.parseInt(newText);
                } catch (NumberFormatException ex) {
                    return fmt("Failed to parse undo count \"%s\".", newText);
                }

                // Check range.
                if (count < 1 || maxUndoCount < count) {
                    return fmt("Undo count \"%s\" is not in range [1 .. %,d].", newText, maxUndoCount);
                }

                // All OK.
                return null;
            }
        };

        // Get undo count using input dialog.
        String title = "Number of transitions to undo";
        String msg = fmt("Specify an undo count in the range [1 .. %,d]:", maxUndoCount);
        InputDialog dialog = new InputDialog(contents.getShell(), title, msg, str(maxUndoCount), validator);
        int result = dialog.open();

        // Check cancellation.
        if (result == Window.CANCEL) {
            return null;
        }

        // Get size.
        return Integer.parseInt(dialog.getValue());
    }

    /** Resets and disables all GUI buttons and removes all {@link #popupMenu} items. */
    public void resetAndDisableUI() {
        // Transitions and source state no longer needed, and may be garbage
        // collected.
        transitions = null;
        state = null;

        // Remove popup menu items.
        removePopupMenuItems();

        // Remove transition count label texts and disable buttons.
        for (Control buttonsChild: buttons.getChildren()) {
            // Skip non-choice children.
            if (!(buttonsChild instanceof Composite)) {
                continue;
            }
            Composite comp = (Composite)buttonsChild;
            Control[] children = comp.getChildren();

            // Get event index, or '-1' for non-events.
            Object eventIdxObj = comp.getData("eventIdx");
            int eventIdx = (int)eventIdxObj;

            // Reset transition count label.
            Label cntLbl = (Label)children[CNT_LBL_IDX];
            if (!cntLbl.getText().isEmpty()) {
                cntLbl.setText("");
                comp.pack();
                comp.layout();
            }

            // Set enabledness of buttons.
            RuntimeEvent<?> event = (eventIdx == -1) ? null : spec.events.get(eventIdx);
            children[COLOR_COMP_IDX].setBackground(getColor(event, false));
            children[BUTTON_IDX].setEnabled(false);
            children[ARROW_IDX].setEnabled(false);
        }

        buttons.pack();
        buttons.layout();
    }

    /**
     * Inform input component about choice and make the choice data available.
     *
     * @param data The choice data to make available.
     */
    private void inform(InteractiveGuiInputChoice data) {
        choice.set(data);
        synchronized (ready) {
            ready.set(true);
            ready.notify();
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        // Dispose fonts.
        if (italicFont != null) {
            italicFont.dispose();
        }
        italicFont = null;
    }
}
