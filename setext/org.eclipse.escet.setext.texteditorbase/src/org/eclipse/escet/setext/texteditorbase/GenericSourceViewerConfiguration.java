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

package org.eclipse.escet.setext.texteditorbase;

import static org.eclipse.ui.texteditor.AbstractDecoratedTextEditorPreferenceConstants.EDITOR_SPACES_FOR_TABS;
import static org.eclipse.ui.texteditor.spelling.SpellingService.PREFERENCE_SPELLING_ENABLED;

import java.util.Set;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Strings;
import org.eclipse.escet.setext.texteditorbase.scanners.GenericPartitionScanner;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.eclipse.jface.text.reconciler.MonoReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;
import org.eclipse.ui.texteditor.spelling.SpellingService;

/** Generic text editor source viewer configuration. */
public abstract class GenericSourceViewerConfiguration extends TextSourceViewerConfiguration {
    /**
     * Default partition content type.
     *
     * @see IDocument#DEFAULT_CONTENT_TYPE
     */
    public static final String DEFAULT_CONTENT_TYPE = IDocument.DEFAULT_CONTENT_TYPE;

    /** Color manager used to share color resources. */
    protected ColorManager colorManager;

    /** Partition scanner used by the text editor for which this is the source viewer configuration. */
    private GenericPartitionScanner partitionScanner;

    /**
     * Sets the color manager to use to share color resources. This method should only be called by the
     * {@link GenericTextEditor} class.
     *
     * @param colorManager Color manager used to share color resources.
     */
    public void setColorManager(ColorManager colorManager) {
        Assert.check(this.colorManager == null);
        this.colorManager = colorManager;
    }

    /**
     * Sets the partition scanner used by the text editor for which this is the source viewer configuration. This method
     * should only be called by the {@link GenericTextEditor} class.
     *
     * @param scanner Partition scanner used by the text editor for which this is the source viewer configuration.
     */
    protected void setPartitionScanner(GenericPartitionScanner scanner) {
        Assert.check(this.partitionScanner == null);
        this.partitionScanner = scanner;
    }

    /**
     * Sets the preference store used by the text editor for which this is the source viewer configuration. This method
     * should only be called by the {@link GenericTextEditor} class.
     *
     * @param preferenceStore Preference store used by the text editor for which this is the source viewer
     *     configuration.
     */
    public void setPreferenceStore(IPreferenceStore preferenceStore) {
        Assert.check(fPreferenceStore == null);
        fPreferenceStore = preferenceStore;
    }

    @Override
    protected String[] getIndentPrefixesForTab(int tabWidth) {
        // Super class implementation does not take into account the
        // EDITOR_SPACES_FOR_TABS preference.
        boolean spacesForTabs = fPreferenceStore.getBoolean(EDITOR_SPACES_FOR_TABS);
        String[] indentPrefixes = new String[tabWidth + 2];
        for (int i = 0; i <= tabWidth; i++) {
            String spaces = Strings.spaces(i);
            indentPrefixes[i] = (i >= tabWidth) ? spaces : spacesForTabs ? Strings.spaces(tabWidth) : spaces + '\t';
        }
        indentPrefixes[tabWidth + 1] = "";
        return indentPrefixes;
    }

    @Override
    public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
        String[] contentTypes = partitionScanner.getTypes();
        String[] rslt = new String[contentTypes.length + 1];
        rslt[0] = IDocument.DEFAULT_CONTENT_TYPE;
        System.arraycopy(contentTypes, 0, rslt, 1, contentTypes.length);
        return rslt;
    }

    @Override
    public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
        PresentationReconciler reconciler = new PresentationReconciler();
        addDamagersRepairers(reconciler);
        return reconciler;
    }

    /**
     * Add damagers and repairers to the given presentation reconciler.
     *
     * @param reconciler The presentation reconciler to add the damagers and repairers to.
     */
    protected abstract void addDamagersRepairers(PresentationReconciler reconciler);

    /**
     * Adds (registers) a token scanner for a given content type with the given presentation reconciler, as both damager
     * and repairer.
     *
     * @param reconciler The presentation reconciler to register the scanner with.
     * @param scanner The scanner to register.
     * @param contentType The content type under which to register.
     * @see DefaultDamagerRepairer
     */
    protected static void addDamagerRepairer(PresentationReconciler reconciler, ITokenScanner scanner,
            String contentType)
    {
        DefaultDamagerRepairer dr = new DefaultDamagerRepairer(scanner);
        reconciler.setDamager(dr, contentType);
        reconciler.setRepairer(dr, contentType);
    }

    @Override
    public IAnnotationHover getAnnotationHover(ISourceViewer sourceViewer) {
        return new GenericTextHover(sourceViewer);
    }

    @Override
    public ITextHover getTextHover(ISourceViewer sourceViewer, String contentType) {
        return new GenericTextHover(sourceViewer);
    }

    @Override
    public IAnnotationHover getOverviewRulerAnnotationHover(ISourceViewer sourceViewer) {
        return new GenericTextHover(sourceViewer);
    }

    @Override
    public IReconciler getReconciler(ISourceViewer sourceViewer) {
        // If spell checking is disabled, there is nothing to reconcile.
        if (fPreferenceStore == null) {
            return null;
        }
        if (!fPreferenceStore.getBoolean(PREFERENCE_SPELLING_ENABLED)) {
            return null;
        }

        // Get spelling service, if it is available.
        SpellingService spellingService = EditorsUI.getSpellingService();
        if (spellingService.getActiveSpellingEngineDescriptor(fPreferenceStore) == null) {
            return null;
        }

        // Get partition content types for which to spell check, if any.
        Set<String> spellingContentTypes = partitionScanner.getSpellingTypes();
        if (spellingContentTypes.isEmpty()) {
            return null;
        }

        // Construct incremental reconciler.
        IReconcilingStrategy strategy = new GenericSpellingReconcileStrategy(sourceViewer, spellingService,
                spellingContentTypes);
        MonoReconciler reconciler = new MonoReconciler(strategy, true);
        return reconciler;
    }
}
