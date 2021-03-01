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

package org.eclipse.escet.common.app.framework.options;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

/**
 * Option group for a certain {@link Option}, for use in an {@link OptionDialog}.
 *
 * <p>
 * The layout data of option groups, is set by the {@link OptionDialog}.
 * </p>
 *
 * @param <T> The type of the data value for the option.
 */
public abstract class OptionGroup<T> extends Composite {
    /** The option for which this option group represents the graphical interface. */
    private final Option<T> option;

    /**
     * The {@link Group} control that acts as parent for all the child controls of this option group. It uses
     * {@link FormLayout} for its children.
     */
    protected final Group group;

    /**
     * Label that displays the description of the option.
     *
     * <p>
     * Within the {@link FormLayout} of the {@link #group}, this label is put in the upper left corner. Other controls
     * should be placed relative to this label.
     * </p>
     */
    protected final Label descrLabel;

    /**
     * Constructor for the {@link OptionGroup} class.
     *
     * @param page The options page that is the parent of this option group.
     * @param option The option for which this option group represents the graphical interface.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public OptionGroup(Composite page, Option option) {
        super(page, SWT.NONE);
        this.group = new Group(this, SWT.NONE);
        this.option = option;
        this.descrLabel = new Label(group, SWT.WRAP);

        // Initialize layout, add the components, etc.
        initGroup();

        // Initialize the option to its value, or to its default value in case
        // no value is currently set.
        setToValue((T)(Options.hasValue(option) ? Options.get(option) : option.getDefault()));
    }

    /** Initializes the option groups controls and layout. */
    private void initGroup() {
        // Fill the composite with the group.
        setLayout(new FillLayout());

        // Get command line for the option, to include in option name.
        String cmdLineForOption;
        if (option.getCmdLong().equals("*")) {
            cmdLineForOption = " (remaining command line arguments)";
        } else {
            cmdLineForOption = " (--" + option.getCmdLong();
            if (option.getCmdShort() != null) {
                cmdLineForOption += " or -" + option.getCmdShort();
            }
            cmdLineForOption += ")";
        }

        // Set a form layout for the children.
        group.setText(" " + option.getName() + cmdLineForOption + " ");
        FormLayout grpLayout = new FormLayout();
        grpLayout.marginWidth = 8;
        grpLayout.marginHeight = 8;
        grpLayout.spacing = 8;
        group.setLayout(grpLayout);

        // Initialize description label.
        descrLabel.setText(getDescription());
        FormData descrData = new FormData();
        descrData.top = new FormAttachment(0, 0);
        descrData.left = new FormAttachment(0, 0);
        descrData.right = new FormAttachment(100, 0);
        descrLabel.setLayoutData(descrData);

        // Add the option specific components.
        addComponents(group);

        // Add a resize listener to the page, to automatically resize the
        // children of the contents to the width of the page.
        group.addListener(SWT.Resize, new Listener() {
            @Override
            public void handleEvent(Event event) {
                Rectangle bounds = group.getBounds();
                for (Control c: group.getChildren()) {
                    if (c == descrLabel) {
                        Object layoutData = c.getLayoutData();
                        Assert.check(layoutData instanceof FormData);
                        FormData grpData = (FormData)c.getLayoutData();
                        grpData.width = bounds.width - 24;
                    }
                }
                group.getShell().layout();
            }
        });
    }

    /**
     * Helper method that can be used from the {@link #addComponents} method, to properly vertically layout the given
     * components, in the given order, from top to bottom, below the {@link #descrLabel} label. All controls are
     * automatically fixed to the left and right borders.
     *
     * @param components The components to layout.
     */
    protected void layoutVertical(Control[] components) {
        Control previous = descrLabel;
        for (Control component: components) {
            // Set the form data.
            FormData grpData = new FormData();
            grpData.left = new FormAttachment(0, 0);
            grpData.right = new FormAttachment(100, 0);
            grpData.top = new FormAttachment(previous, 0);
            component.setLayoutData(grpData);

            // Update previous for next loop.
            previous = component;
        }
    }

    /**
     * Helper method that can be used from the {@link #addComponents} method, to properly vertically layout the given
     * components, in the given order, from top to bottom, below the {@link #descrLabel} label. All control of the
     * vertical layout automatically fixed to the left and right borders. If a vertical component consists of multiple
     * components, then the sub-components are put horizontally, from left to right, from the left border, with an
     * indentation of 'indent' pixels.
     *
     * @param components The components to layout. The outer array is the components that should be layout vertically.
     *     The inner elements may be arrays of horizontal components, or components directly.
     * @param indent The horizontal indentation to use for groups of horizontal sub-components, in pixels.
     */
    protected void layoutGeneric(Object[] components, int indent) {
        Control vprevious = descrLabel;
        boolean vprevIndented = false;
        for (Object vcomponent: components) {
            if (vcomponent instanceof Control) {
                Control component = (Control)vcomponent;

                // Set the form data.
                FormData grpData = new FormData();
                grpData.left = new FormAttachment(0, 0);
                grpData.right = new FormAttachment(100, 0);
                grpData.top = new FormAttachment(vprevious, 0);
                component.setLayoutData(grpData);

                // Update vertical previous for next loop.
                vprevious = component;
                vprevIndented = false;
            } else if (vcomponent instanceof Control[]) {
                Control[] hcomponents = (Control[])vcomponent;
                Assert.check(hcomponents.length >= 1);
                Control hprevious = null;
                for (Control component: hcomponents) {
                    // Set the form data.
                    FormData grpData = new FormData();
                    grpData.left = (hprevious == null) ? new FormAttachment(0, indent)
                            : new FormAttachment(hprevious, 0);
                    grpData.top = (hprevious == null)
                            ? (vprevIndented) ? new FormAttachment(vprevious, 8) : new FormAttachment(vprevious, 0)
                            : new FormAttachment(hprevious, 0, SWT.CENTER);
                    component.setLayoutData(grpData);

                    // Update horizontal previous for next loop.
                    hprevious = component;
                }

                // Right side of last one should stretch to the end.
                @SuppressWarnings("null")
                FormData lastData = (FormData)hprevious.getLayoutData();
                lastData.right = new FormAttachment(100, 0);

                // Update vertical previous for next loop.
                vprevious = hcomponents[0];
                vprevIndented = true;
            } else {
                // Not one of the expected formats.
                throw new IllegalArgumentException("Invalid components format: " + vcomponent);
            }
        }
    }

    /**
     * Same as {@link #layoutGeneric(Object[], int)} with a default indentation of 32 pixels.
     *
     * @param components The components to layout. The outer array is the components that should be layout vertically.
     *     The inner elements may be arrays of horizontal components, or components directly.
     */
    protected void layoutGeneric(Object[] components) {
        layoutGeneric(components, 32);
    }

    /**
     * Adds the option specific components to the {@link Group} widget.
     *
     * <p>
     * The group has a {@link FormLayout}. All added components should use a {@link FormData} layout data object to
     * specify their relative positions. Note that the group already contains an option description label. All
     * components added to the group should position themselves relative to the {@link #group}, the {@link #descrLabel},
     * or other components that are added.
     * </p>
     *
     * @param group The widget to add the components to.
     */
    protected abstract void addComponents(Group group);

    /**
     * Returns the description to use for this option.
     *
     * @return The description to use for this option.
     */
    public abstract String getDescription();

    /**
     * Sets the GUI components to the given value of the option. In particular, {@code setToValue(option.getDefault())}
     * is used to initialize the GUI components.
     *
     * @param value The value of the option to set the GUI components to.
     */
    public abstract void setToValue(T value);

    /**
     * Returns the command line arguments that represent the current value of option, as specified by the user using the
     * GUI components.
     *
     * <p>
     * Note that command line arguments must be position independent, as the order in which option groups are asked for
     * the command line arguments, is not guaranteed to be fixed.
     * </p>
     *
     * <p>
     * In general, returning an empty sequence for the command line is unwanted, as parsing that has no effect. To make
     * sure the option gets the correct value, an argument for the option has to actually be parsed. Thus, even for the
     * default value of an option, returning an empty command line would result in undesired behavior.
     * </p>
     *
     * <p>
     * For options that don't have a value, such as the {@code --help} option, returning an empty sequence is allowed.
     * </p>
     *
     * @return The command line arguments that represent the current value of option, as specified by the user using the
     *     GUI components.
     * @see Option#getCmdLine
     */
    public abstract String[] getCmdLine();
}
