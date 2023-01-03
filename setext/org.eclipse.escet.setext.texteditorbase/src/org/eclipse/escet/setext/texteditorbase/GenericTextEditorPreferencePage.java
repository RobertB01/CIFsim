//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.setext.texteditorbase;

import static org.eclipse.escet.setext.texteditorbase.themes.DefaultTextEditorThemeName.AUTO;
import static org.eclipse.escet.setext.texteditorbase.themes.DefaultTextEditorThemeName.DARK;
import static org.eclipse.escet.setext.texteditorbase.themes.DefaultTextEditorThemeName.LIGHT;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

/**
 * Generic text editor preference page.
 *
 * <p>
 * This class is to be extended for concrete text editors. However, no methods need to be overridden. Simply extending
 * this class is sufficient. The derived preference page class must however be part of the the same package as the text
 * editor class for that text editor, for the preferences to be picked up by that text editor.
 * </p>
 */
public abstract class GenericTextEditorPreferencePage extends FieldEditorPreferencePage
        implements IWorkbenchPreferencePage
{
    /** Constructor for the {@link GenericTextEditorPreferencePage} class. */
    public GenericTextEditorPreferencePage() {
        super(GRID);
    }

    @Override
    public void init(IWorkbench workbench) {
        setPreferenceStore(new ScopedPreferenceStore(InstanceScope.INSTANCE, EditorsUI.PLUGIN_ID));
    }

    @Override
    protected void createFieldEditors() {
        String[][] namesAndValues = {{"Automatic", AUTO}, {"Dark", DARK}, {"Light", LIGHT}};
        addField(new ComboFieldEditor(getClass().getPackageName() + ".theme", "&Theme:", namesAndValues,
                getFieldEditorParent()));
    }
}
