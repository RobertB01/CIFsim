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

package org.eclipse.escet.cif.cif2yed;

import static org.eclipse.escet.common.app.framework.output.OutputProvider.warn;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.escet.cif.cif2yed.options.DiagramKind;
import org.eclipse.escet.cif.cif2yed.options.DiagramKindsOption;
import org.eclipse.escet.cif.cif2yed.options.ModelFiltersOption;
import org.eclipse.escet.cif.cif2yed.options.RelationKindsOption;
import org.eclipse.escet.cif.cif2yed.options.SyntaxHighlightingOption;
import org.eclipse.escet.cif.cif2yed.options.TransparentEdgeLabelsOption;
import org.eclipse.escet.cif.io.CifReader;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.common.app.framework.Application;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.XmlSupport;
import org.eclipse.escet.common.app.framework.exceptions.InvalidOptionException;
import org.eclipse.escet.common.app.framework.exceptions.UnsupportedException;
import org.eclipse.escet.common.app.framework.io.AppStreams;
import org.eclipse.escet.common.app.framework.options.InputFileOption;
import org.eclipse.escet.common.app.framework.options.Option;
import org.eclipse.escet.common.app.framework.options.OptionCategory;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.OutputFileOption;
import org.eclipse.escet.common.app.framework.output.IOutputComponent;
import org.eclipse.escet.common.app.framework.output.OutputProvider;
import org.eclipse.escet.common.java.Assert;
import org.w3c.dom.Document;

/** CIF to yEd transformation application. */
public class CifToYedApp extends Application<IOutputComponent> {
    /**
     * Application main method.
     *
     * @param args The command line arguments supplied to the application.
     */
    public static void main(String[] args) {
        CifToYedApp app = new CifToYedApp();
        app.run(args, true);
    }

    /** Constructor for the {@link CifToYedApp} class. */
    public CifToYedApp() {
        // Nothing to do here.
    }

    /**
     * Constructor for the {@link CifToYedApp} class.
     *
     * @param streams The streams to use for input, output, and error streams.
     */
    public CifToYedApp(AppStreams streams) {
        super(streams);
    }

    @Override
    public String getAppName() {
        return "CIF to yEd transformer";
    }

    @Override
    public String getAppDescription() {
        return "Transforms CIF files to yEd/GraphML (*.graphml) files.";
    }

    @Override
    protected int runInternal() {
        // Read CIF specification.
        Specification spec = new CifReader().init().read();
        if (isTerminationRequested()) {
            return 0;
        }

        // Generating no diagrams is kind of useless.
        Set<DiagramKind> kinds = DiagramKindsOption.getKinds();
        if (kinds.isEmpty()) {
            warn("No diagram kinds specified. No diagrams will be generated.");
        }

        // If multiple diagram kinds are enabled, and an explicit output file
        // path is given, this would lead to overwriting of the file.
        if (OutputFileOption.getPath() != null && kinds.size() > 1) {
            String msg = "Writing multiple diagrams to a single explicitly specified output file is not supported. "
                    + "Use implicit/default output file names, or generate one diagram at a time.";
            throw new InvalidOptionException(msg);
        }

        // Generate and write diagrams.
        for (DiagramKind kind: kinds) {
            // Get output file path.
            String name = kind.toString().toLowerCase(Locale.US);
            String ext = fmt(".%s.graphml", name);
            String outPath = OutputFileOption.getDerivedPath(".cif", ext);
            outPath = Paths.resolve(outPath);

            // Perform transformation to yEd.
            Document doc = null;
            try {
                switch (kind) {
                    case MODEL:
                        doc = new CifToYedModelDiagram().transform(spec);
                        break;

                    case RELATIONS:
                        doc = new CifToYedRelationsDiagram().transform(spec);
                        break;
                }
            } catch (UnsupportedException ex) {
                String kindTxt = kind.getDescription();
                kindTxt = StringUtils.uncapitalize(kindTxt);
                String msg = fmt("Failed to generate a yEd %s for CIF file \"%s\".", kindTxt,
                        InputFileOption.getPath());
                throw new UnsupportedException(msg, ex);
            }
            Assert.notNull(doc);
            if (isTerminationRequested()) {
                return 0;
            }

            // Write yEd file.
            XmlSupport.writeFile(doc, "yEd", outPath);
            if (isTerminationRequested()) {
                return 0;
            }
        }

        // All done.
        return 0;
    }

    @Override
    protected OutputProvider<IOutputComponent> getProvider() {
        return new OutputProvider<>();
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected OptionCategory getAllOptions() {
        OptionCategory generalCat = getGeneralOptionCategory();

        List<Option> transOpts = list();
        transOpts.add(Options.getInstance(InputFileOption.class));
        transOpts.add(Options.getInstance(OutputFileOption.class));
        transOpts.add(Options.getInstance(DiagramKindsOption.class));
        transOpts.add(Options.getInstance(SyntaxHighlightingOption.class));
        transOpts.add(Options.getInstance(TransparentEdgeLabelsOption.class));
        transOpts.add(Options.getInstance(ModelFiltersOption.class));
        transOpts.add(Options.getInstance(RelationKindsOption.class));
        List<OptionCategory> transSubCats = list();
        OptionCategory transCat = new OptionCategory("Transformation", "Transformation options.", transSubCats,
                transOpts);

        List<OptionCategory> cats = list(generalCat, transCat);
        OptionCategory options = new OptionCategory("CIF to yEd Transformer Options",
                "All options for the CIF to yEd transformer.", cats, list());

        return options;
    }
}
