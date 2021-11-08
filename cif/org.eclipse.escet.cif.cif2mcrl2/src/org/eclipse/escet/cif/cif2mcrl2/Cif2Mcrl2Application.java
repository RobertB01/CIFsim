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

package org.eclipse.escet.cif.cif2mcrl2;

import static org.eclipse.escet.common.app.framework.output.OutputProvider.warn;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.Set;

import org.eclipse.escet.cif.cif2cif.AddDefaultInitialValues;
import org.eclipse.escet.cif.cif2cif.ElimAlgVariables;
import org.eclipse.escet.cif.cif2cif.ElimComponentDefInst;
import org.eclipse.escet.cif.cif2cif.ElimConsts;
import org.eclipse.escet.cif.cif2cif.ElimMonitors;
import org.eclipse.escet.cif.cif2cif.ElimSelf;
import org.eclipse.escet.cif.cif2cif.EnumsToInts;
import org.eclipse.escet.cif.cif2cif.RemoveIoDecls;
import org.eclipse.escet.cif.cif2cif.SimplifyValues;
import org.eclipse.escet.cif.cif2mcrl2.options.DebugFileOption;
import org.eclipse.escet.cif.cif2mcrl2.options.DefineInstanceTreeOption;
import org.eclipse.escet.cif.cif2mcrl2.options.EnableDebugOutputOption;
import org.eclipse.escet.cif.cif2mcrl2.options.GenerateValueActionsOption;
import org.eclipse.escet.cif.cif2mcrl2.storage.AutomatonData;
import org.eclipse.escet.cif.cif2mcrl2.storage.VariableData;
import org.eclipse.escet.cif.cif2mcrl2.tree.ProcessNode;
import org.eclipse.escet.cif.cif2mcrl2.tree.TextNode;
import org.eclipse.escet.cif.io.CifReader;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.common.app.framework.Application;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.io.AppStream;
import org.eclipse.escet.common.app.framework.io.AppStreams;
import org.eclipse.escet.common.app.framework.io.FileAppStream;
import org.eclipse.escet.common.app.framework.options.InputFileOption;
import org.eclipse.escet.common.app.framework.options.Option;
import org.eclipse.escet.common.app.framework.options.OptionCategory;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.OutputFileOption;
import org.eclipse.escet.common.app.framework.output.IOutputComponent;
import org.eclipse.escet.common.app.framework.output.OutputProvider;
import org.eclipse.escet.common.box.Box;
import org.eclipse.escet.common.box.HBox;
import org.eclipse.escet.common.box.StreamCodeBox;
import org.eclipse.escet.common.box.VBox;

/** Application implementing the CIF to mCRL2 transformation. */
public class Cif2Mcrl2Application extends Application<IOutputComponent> {
    /**
     * Application main method.
     *
     * @param args The command-line arguments supplied to the application.
     */
    public static void main(String[] args) {
        Cif2Mcrl2Application cmApp = new Cif2Mcrl2Application();
        cmApp.run(args);
    }

    /** Constructor for the {@link Cif2Mcrl2Application} class. */
    public Cif2Mcrl2Application() {
        // Nothing to do.
    }

    /**
     * Constructor of the {@link Cif2Mcrl2Application} class.
     *
     * @param streams The streams to use for input, output, and errors.
     */
    public Cif2Mcrl2Application(AppStreams streams) {
        super(streams);
    }

    @Override
    protected OutputProvider<IOutputComponent> getProvider() {
        return new OutputProvider<>();
    }

    @Override
    protected int runInternal() {
        // Read CIF input.
        CifReader cifReader = new CifReader().init();
        Specification spec = cifReader.read();
        if (isTerminationRequested()) {
            return 0;
        }

        // Remove/ignore I/O declarations, to increase the supported subset.
        RemoveIoDecls removeIoDecls = new RemoveIoDecls();
        removeIoDecls.transform(spec);
        if (removeIoDecls.haveAnySvgInputDeclarationsBeenRemoved()) {
            warn("The specification contains CIF/SVG input declarations. These will be ignored.");
        }

        // Perform preprocessing on the specification. The most expensive
        // variant of value simplification is used, to inline (and thus
        // support) constants, and get the most simple result.
        new ElimComponentDefInst().transform(spec);
        new ElimSelf().transform(spec);
        new ElimAlgVariables().transform(spec);
        new ElimConsts().transform(spec);
        new EnumsToInts().transform(spec);
        new ElimMonitors().transform(spec);
        new SimplifyValues().transform(spec);
        new AddDefaultInitialValues().transform(spec);
        if (isTerminationRequested()) {
            return 0;
        }

        // Check pre-conditions of the input.
        Cif2Mcrl2PreChecker pca = new Cif2Mcrl2PreChecker();
        pca.checkSpec(spec);
        if (isTerminationRequested()) {
            return 0;
        }

        // Extract CIF elements from the specification.
        AutomatonExtractor ae = new AutomatonExtractor();
        ae.findElements(spec);
        List<AutomatonData> autDatas = ae.getAutDatas();
        Set<VariableData> sharedVars = ae.getSharedVariables();
        Set<VariableData> singleUseVars = ae.getSingleUseVariables();
        if (isTerminationRequested()) {
            return 0;
        }

        // Construct tree with names, verify correctness, and get the local
        // variables of the specification.
        Set<VariableData> localVars;
        ProcessNode procRoot;
        String text = DefineInstanceTreeOption.getTreeText();
        if (text.isEmpty()) {
            localVars = singleUseVars;
            procRoot = InstanceTreeBuilder.buildDefaultTree(autDatas, sharedVars);
        } else {
            TextNode tn = InstanceTreeHelper.parseTreeText(text);
            localVars = InstanceTreeVerifier.checkAndGetLocals(tn, autDatas, sharedVars, singleUseVars);
            procRoot = InstanceTreeBuilder.buildProcessTree(tn, autDatas, sharedVars, singleUseVars);
        }
        if (isTerminationRequested()) {
            return 0;
        }

        // Verify tree shape (var1 || (var2 || ( ... || <behavior-without-vars>))).
        // Further consequences are in CombinedProcess.addInstantiations
        InstanceTreeVerifier.checkProcessTreeShape(procRoot);
        if (isTerminationRequested()) {
            return 0;
        }

        // Compute actions.
        procRoot.deriveActions(localVars);
        if (isTerminationRequested()) {
            return 0;
        }

        // If requested, dump debug information of the instance tree.
        if (EnableDebugOutputOption.getEnableDebugOutput()) {
            String path = DebugFileOption.getDerivedPath(".cif", "_dbg.txt");
            AppStream stream = new FileAppStream(path);
            StreamCodeBox code = new StreamCodeBox(stream, 4);
            procRoot.dumpActions(code);
            code.close();
        }
        if (isTerminationRequested()) {
            return 0;
        }

        // Generate and write output.
        Box code = generateCode(procRoot, localVars);
        if (isTerminationRequested()) {
            return 0;
        }

        String outFile = OutputFileOption.getDerivedPath(".cif", ".mcrl2");
        outFile = Paths.resolve(outFile);
        code.writeToFile(outFile);

        return 0;
    }

    /**
     * Generate mCRL2 code for the processes and instantiation tree.
     *
     * @param procRoot Root of the tree.
     * @param localVars Local variables.
     * @return Generated mCRL2 code.
     */
    private static Box generateCode(ProcessNode procRoot, Set<VariableData> localVars) {
        NameMaps names = new NameMaps();

        // Process definitions, sorts, and actions for the variables.
        VBox code = new VBox();
        procRoot.addDefinitions(names, localVars, code);

        // Event declarations.
        HBox hb = new HBox();
        hb.add("act ");
        boolean first = true;
        for (Event evt: procRoot.eventVarUse.keySet()) {
            if (!first) {
                hb.add(", ");
            }
            first = false;
            hb.add(fmt("%s, %s", names.getEventName(evt), names.getRenamedEventName(evt)));
        }
        hb.add(";");
        code.add(hb);
        code.add();

        // Instantiation of the tree.
        VBox vb = new VBox();
        procRoot.addInstantiations(names, localVars, vb);
        code.add(new HBox("init ", vb, ";"));
        return code;
    }

    @Override
    public String getAppName() {
        return "CIF to mCRL2 transformer";
    }

    @Override
    public String getAppDescription() {
        return "Convert CIF automata with integer and boolean variables to mCRL2.";
    }

    /**
     * Options specific to the CIF to mCRL2 transformer.
     *
     * @return Options specific to the CIF to mCRL2 transformer.
     */
    private OptionCategory getConvertOptionsCategory() {
        List<OptionCategory> subPages = list();

        @SuppressWarnings("rawtypes")
        List<Option> options = list();
        options.add(Options.getInstance(InputFileOption.class));
        options.add(Options.getInstance(DefineInstanceTreeOption.class));
        options.add(Options.getInstance(GenerateValueActionsOption.class));
        options.add(Options.getInstance(EnableDebugOutputOption.class));
        options.add(Options.getInstance(DebugFileOption.class));
        options.add(Options.getInstance(OutputFileOption.class));
        return new OptionCategory("CIF to mCRL2 options", "Options for converting a CIF specification to mCRL2.",
                subPages, options);
    }

    @Override
    protected OptionCategory getAllOptions() {
        List<OptionCategory> subPages = list();
        subPages.add(getConvertOptionsCategory());
        subPages.add(getGeneralOptionCategory());

        @SuppressWarnings("rawtypes")
        List<Option> options = list();
        String optDesc = "All options for the conversion from CIF to mCRL2.";
        return new OptionCategory("CIF to mCRL2 conversion options", optDesc, subPages, options);
    }
}
