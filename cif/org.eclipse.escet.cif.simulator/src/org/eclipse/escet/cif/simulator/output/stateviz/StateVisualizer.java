//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.simulator.output.stateviz;

import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.eclipse.escet.cif.simulator.runtime.meta.RuntimeStateFilterer;
import org.eclipse.escet.cif.simulator.runtime.meta.RuntimeStateObjectMeta;
import org.eclipse.escet.cif.simulator.runtime.meta.StateObjectType;
import org.eclipse.escet.cif.simulator.runtime.model.RuntimeState;
import org.eclipse.escet.common.eclipse.ui.ControlEditor;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.exceptions.InvalidOptionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Widget;

/** Visualizer to show the state of the automata and variables, as text in a table. */
public class StateVisualizer extends ControlEditor {
    /** The state objects filters text, or {@code null} if not available. */
    public String filtersTxt;

    /** The SWT table. Is {@code null} until set by {@link #createContents}. */
    private Table table;

    /**
     * The meta data of the state objects, in the order that they are displayed in the {@link #table}. Is {@code null}
     * until set by {@link #initMeta}.
     */
    private List<RuntimeStateObjectMeta> metas;

    /** Mapping from state object types to icons. Mapping is empty if not available. */
    private Map<StateObjectType, Image> iconMap = map();

    /** Column width support for the 'value' column. */
    private StateVisualizerColumnWidthSupport valueColumnWidthSupport;

    @Override
    protected Control createContents(Composite parent) {
        // Load the icons.
        Image iconAut = loadIcon(parent, "aut");
        Image iconDiscVar = loadIcon(parent, "discvar");
        Image iconInputVar = loadIcon(parent, "inputvar");
        Image iconContVar = loadIcon(parent, "contvar");
        Image iconDeriv = loadIcon(parent, "deriv");
        Image iconAlgVar = loadIcon(parent, "algvar");
        iconMap.put(StateObjectType.AUTOMATON, iconAut);
        iconMap.put(StateObjectType.TIME, iconContVar);
        iconMap.put(StateObjectType.DISCRETE, iconDiscVar);
        iconMap.put(StateObjectType.INPUT, iconInputVar);
        iconMap.put(StateObjectType.CONTINUOUS, iconContVar);
        iconMap.put(StateObjectType.DERIVATIVE, iconDeriv);
        iconMap.put(StateObjectType.ALGEBRAIC, iconAlgVar);

        // Create table.
        table = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);

        // Fill visualizer with the table.
        FormData tableData = new FormData();
        tableData.left = new FormAttachment(0, 0);
        tableData.right = new FormAttachment(100, 0);
        tableData.top = new FormAttachment(0, 0);
        tableData.bottom = new FormAttachment(100, 0);
        table.setLayoutData(tableData);

        // Show header, and make rows more visible.
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        // Add columns, and make sure they can't be manually resized.
        TableColumn column1 = new TableColumn(table, SWT.NONE);
        TableColumn column2 = new TableColumn(table, SWT.NONE);

        column1.setText("Name");
        column2.setText("Value");

        column1.setResizable(false);
        column2.setResizable(false);

        // Return table as the contents.
        return table;
    }

    /**
     * Initializes the state object meta data for the given state.
     *
     * @param state The state for which to initialize the state object meta data.
     * @throws InvalidOptionException If one of the state visualizer filters has invalid syntax.
     */
    public void initMeta(RuntimeState state) {
        // Get state objects meta data.
        metas = state.spec.stateObjectsMeta;

        // Filter state objects meta data.
        metas = RuntimeStateFilterer.filter(metas, filtersTxt, "State visualizer", "shown in the state visualizer");
        filtersTxt = null;
    }

    /**
     * Initializes the table for the given state.
     *
     * @param state The state for which to initialize the table.
     */
    public void initTable(RuntimeState state) {
        // Add state objects to the table.
        for (RuntimeStateObjectMeta meta: metas) {
            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(meta.name);
            item.setImage(iconMap.get(meta.type));
        }

        // Auto size the columns.
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumn(i).pack();
        }

        // Make first column a bit wider, to get some spacing between the
        // columns.
        int w = table.getColumn(0).getWidth();
        w += 8;
        table.getColumn(0).setWidth(w);

        // Initialize column width support for the 'value' column.
        valueColumnWidthSupport = new StateVisualizerColumnWidthSupport(table.getColumn(1));
    }

    /**
     * Updates the table for the given state.
     *
     * @param state The state from which to obtain the values.
     */
    public void update(RuntimeState state) {
        // Update the 'value' column cells, and its width.
        valueColumnWidthSupport.initNewWidth();
        for (int i = 0; i < metas.size(); i++) {
            RuntimeStateObjectMeta meta = metas.get(i);
            String text = state.getStateObjValueText(meta);
            table.getItem(i).setText(1, text);
            valueColumnWidthSupport.updateNewWidth(text);
        }
        valueColumnWidthSupport.applyNewWidth();
    }

    /**
     * Loads an icon.
     *
     * @param widget The widget from which to obtain the display.
     * @param name The name of the icon (not the full file name).
     * @return The loaded icon.
     */
    private static Image loadIcon(Widget widget, String name) {
        name = fmt("icon_%s.png", name);
        InputStream stream = StateVisualizer.class.getResourceAsStream(name);
        Assert.notNull(stream);
        try {
            return new Image(widget.getDisplay(), stream);
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        for (Image icon: iconMap.values()) {
            icon.dispose();
        }
        iconMap.clear();
        if (valueColumnWidthSupport != null) {
            valueColumnWidthSupport.dispose();
            valueColumnWidthSupport = null;
        }
    }
}
