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

package org.eclipse.escet.cif.merger;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.escet.cif.cif2cif.ElimComponentDefInst;
import org.eclipse.escet.cif.cif2cif.PrintFileIntoDecls;
import org.eclipse.escet.cif.cif2cif.SvgFileIntoDecls;
import org.eclipse.escet.cif.common.CifRelativePathUtils;
import org.eclipse.escet.cif.io.CifReader;
import org.eclipse.escet.cif.io.CifWriter;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.typechecker.postchk.CifSvgPostChecker;
import org.eclipse.escet.cif.typechecker.postchk.CyclePostChecker;
import org.eclipse.escet.common.app.framework.Application;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.exceptions.UnsupportedException;
import org.eclipse.escet.common.app.framework.io.AppStreams;
import org.eclipse.escet.common.app.framework.options.FilesOption;
import org.eclipse.escet.common.app.framework.options.Option;
import org.eclipse.escet.common.app.framework.options.OptionCategory;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.OutputFileOption;
import org.eclipse.escet.common.app.framework.output.IOutputComponent;
import org.eclipse.escet.common.app.framework.output.OutputProvider;
import org.eclipse.escet.common.typechecker.SemanticException;

/** CIF merger application. */
public class CifMergerApp extends Application<IOutputComponent> {
    /**
     * Application main method.
     *
     * @param args The command line arguments supplied to the application.
     */
    public static void main(String[] args) {
        CifMergerApp app = new CifMergerApp();
        app.run(args);
    }

    /** Constructor for the {@link CifMergerApp} class. */
    public CifMergerApp() {
        // Nothing to do here.
    }

    /**
     * Constructor for the {@link CifMergerApp} class.
     *
     * @param streams The streams to use for input, output, and error streams.
     */
    public CifMergerApp(AppStreams streams) {
        super(streams);
    }

    @Override
    public String getAppName() {
        return "CIF merger";
    }

    @Override
    public String getAppDescription() {
        return "Merges CIF specifications together into a single CIF specification.";
    }

    @Override
    protected int runInternal() {
        // Initialize paths. For convenience, we explicitly allow merging only
        // one file.
        List<String> mergedPaths = list();
        Deque<String> todoPaths;
        todoPaths = new LinkedList<>(FilesOption.getPaths());

        // Read and merge CIF specifications.
        Specification mergedSpec = null;
        String mergedAbsDirPath = null;
        while (!todoPaths.isEmpty()) {
            // Read specification.
            String curFilePath = todoPaths.pollFirst();
            String absFilePath = Paths.resolve(curFilePath);
            String absDirPath = Paths.getAbsFilePathDir(absFilePath);
            CifReader reader = new CifReader();
            reader.init(curFilePath, absFilePath, false);
            Specification curSpec = reader.read();
            if (isTerminationRequested()) {
                return 0;
            }

            // Eliminate component definition/instantiation, to ensure we only
            // have to merge concrete components.
            new ElimComponentDefInst().transform(curSpec);
            if (isTerminationRequested()) {
                return 0;
            }

            // Push SVG file declarations inwards, to avoid duplicate SVG file
            // declarations in a single scope.
            new SvgFileIntoDecls().transform(curSpec);

            // Push print file declarations inwards, to avoid duplicate print
            // file declarations in a single scope.
            new PrintFileIntoDecls().transform(curSpec);

            // Merge.
            if (mergedSpec == null) {
                // First specification doesn't need actual merging or relative
                // path adapting.
                mergedSpec = curSpec;
                mergedAbsDirPath = absDirPath;
            } else {
                // Adapt relative paths.
                CifRelativePathUtils.adaptRelativePaths(curSpec, absDirPath, mergedAbsDirPath);

                // Merge previous specifications with current specification.
                try {
                    CifMerger merger = new CifMerger();
                    mergedSpec = merger.merge(mergedSpec, curSpec);
                    if (isTerminationRequested()) {
                        return 0;
                    }
                } catch (UnsupportedException e) {
                    List<String> paths = listc(mergedPaths.size());
                    for (String path: mergedPaths) {
                        paths.add("\"" + path + "\"");
                    }
                    String mergedPathsTxt = StringUtils.join(paths, ", ");
                    String msg = fmt("Merging CIF specification%s %s with CIF specification \"%s\" failed.",
                            (mergedPaths.size() == 1) ? "" : "s", mergedPathsTxt, curFilePath);
                    throw new UnsupportedException(msg, e);
                }
            }

            // Update merged paths.
            mergedPaths.add(curFilePath);
        }
        if (isTerminationRequested()) {
            return 0;
        }

        // Perform post phase type checking on the merged specification.
        CifMergerPostCheckEnv env = new CifMergerPostCheckEnv(mergedAbsDirPath);

        try {
            // Same checks as CIF type checker, in same order.
            CyclePostChecker.check(mergedSpec, env);
            new CifSvgPostChecker(env).check(mergedSpec);
            // CifPrintPostChecker skipped (warnings only, no new problems).
            // SingleEventUsePerAutPostChecker skipped (no new problems).
        } catch (SemanticException ex) {
            // Ignore.
        }

        env.printErrors();
        if (!env.errors.isEmpty()) {
            List<String> paths = listc(mergedPaths.size());
            for (String path: mergedPaths) {
                paths.add("\"" + path + "\"");
            }
            String mergedPathsTxt = StringUtils.join(paths, ", ");

            String msg = fmt("Merging CIF specifications %s failed.", mergedPathsTxt);
            throw new UnsupportedException(msg);
        }

        // Get output file path.
        String outPath = OutputFileOption.getPath();
        if (outPath == null) {
            outPath = "merged.cif";
        }
        outPath = Paths.resolve(outPath);

        // Write merged specification.
        CifWriter.writeCifSpec(mergedSpec, outPath, mergedAbsDirPath);

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

        List<Option> mergeOpts = list();
        mergeOpts.add(Options.getInstance(FilesOption.class));
        mergeOpts.add(Options.getInstance(OutputFileOption.class));
        List<OptionCategory> mergeSubCats = list();
        OptionCategory mergeCat = new OptionCategory("Merger", "Merger options.", mergeSubCats, mergeOpts);

        List<OptionCategory> cats = list(generalCat, mergeCat);
        OptionCategory options = new OptionCategory("CIF Merger Options", "All options for the CIF merger.", cats,
                list());

        return options;
    }
}
