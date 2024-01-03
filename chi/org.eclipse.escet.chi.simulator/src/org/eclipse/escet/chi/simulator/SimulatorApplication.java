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

package org.eclipse.escet.chi.simulator;

import static org.eclipse.escet.chi.codegen.JavaCodeGenerator.transCodeGen;
import static org.eclipse.escet.common.app.framework.Paths.resolve;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Platform;
import org.eclipse.escet.chi.codegen.CodeGeneratorContext;
import org.eclipse.escet.chi.codegen.java.JavaFile;
import org.eclipse.escet.chi.metamodel.chi.Declaration;
import org.eclipse.escet.chi.metamodel.chi.Specification;
import org.eclipse.escet.chi.parser.ChiParser;
import org.eclipse.escet.chi.runtime.ChiCoordinator;
import org.eclipse.escet.chi.runtime.ChiSimulatorException;
import org.eclipse.escet.chi.runtime.ChiSpecification;
import org.eclipse.escet.chi.runtime.SimulationResult;
import org.eclipse.escet.chi.runtime.data.BaseProcess;
import org.eclipse.escet.chi.simulator.options.InputFileOption;
import org.eclipse.escet.chi.simulator.options.OutputDirectoryOption;
import org.eclipse.escet.chi.simulator.options.OutputInfoOption;
import org.eclipse.escet.chi.simulator.options.PerformJavaCompileOption;
import org.eclipse.escet.chi.simulator.options.RunSpecificationClassOption;
import org.eclipse.escet.chi.simulator.options.SeedValueOption;
import org.eclipse.escet.chi.simulator.options.StartupInstanceOption;
import org.eclipse.escet.chi.simulator.options.WriteEMFOption;
import org.eclipse.escet.chi.simulator.options.WriteJAROption;
import org.eclipse.escet.chi.typecheck.ChiTypeChecker;
import org.eclipse.escet.common.app.framework.Application;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.io.AppStreams;
import org.eclipse.escet.common.app.framework.javacompiler.InMemoryJarClassLoader;
import org.eclipse.escet.common.app.framework.javacompiler.JavaCharSeqInputFileObject;
import org.eclipse.escet.common.app.framework.javacompiler.JavaCompilerOption;
import org.eclipse.escet.common.app.framework.javacompiler.JavaInputFileObject;
import org.eclipse.escet.common.app.framework.javacompiler.RuntimeJavaCompiler;
import org.eclipse.escet.common.app.framework.javacompiler.RuntimeJavaCompilerException;
import org.eclipse.escet.common.app.framework.options.Option;
import org.eclipse.escet.common.app.framework.options.OptionCategory;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.output.OutputComponentBase;
import org.eclipse.escet.common.app.framework.output.OutputProvider;
import org.eclipse.escet.common.java.exceptions.InputOutputException;
import org.eclipse.escet.common.java.exceptions.InvalidInputException;
import org.eclipse.escet.common.java.exceptions.InvalidModelException;
import org.eclipse.escet.common.typechecker.SemanticProblem;
import org.eclipse.escet.common.typechecker.SemanticProblemSeverity;
import org.eclipse.escet.setext.runtime.DebugMode;
import org.eclipse.escet.setext.runtime.SyntaxWarning;

/** Chi simulator application. */
public class SimulatorApplication extends Application<OutputComponentBase> {
    /**
     * Application main method.
     *
     * @param args The command line arguments supplied to the application.
     */
    public static void main(String[] args) {
        SimulatorApplication app = new SimulatorApplication();
        app.run(args, true);
    }

    /** Constructor for the {@link SimulatorApplication} class. */
    public SimulatorApplication() {
        // Nothing to do here.
    }

    /**
     * Chi simulation application constructor.
     *
     * @param streams The streams to use for input, output, warning, and error streams.
     */
    public SimulatorApplication(AppStreams streams) {
        super(streams);
    }

    @Override
    protected OutputProvider<OutputComponentBase> getProvider() {
        return new OutputProvider<>();
    }

    /**
     * Load the Chi text file, and parse it to an untyped Chi EMF model.
     *
     * @param inFile Input Chi file name.
     * @return The top-level declarations of the loaded and unchecked model.
     */
    private List<Declaration> parseChiFile(String inFile) {
        ChiParser parser = new ChiParser();
        List<Declaration> decls = parser.parseFile(inFile, DebugMode.NONE);

        for (SyntaxWarning warning: parser.getWarnings()) {
            OutputProvider.warn(warning.toString());
        }

        return decls;
    }

    /**
     * Type-check a Chi model.
     *
     * @param emfName Name of the EMF model file to write (may be {@code null}).
     * @param decls Declaration of the untyped and unchecked Chi model.
     * @return The Chi model after type checking / type decoration.
     */
    private Specification typecheckChiFile(String emfName, List<Declaration> decls) {
        ChiTypeChecker tpc = new ChiTypeChecker(emfName);
        Specification spec = tpc.typeCheck(decls);
        boolean hasWarnings = false;
        boolean hasErrors = false;
        for (SemanticProblem sp: tpc.getProblems()) {
            if (sp.severity == SemanticProblemSeverity.ERROR) {
                OutputProvider.out("Error: " + sp.toString());
                hasErrors = true;
            } else if (sp.severity == SemanticProblemSeverity.WARNING) {
                hasWarnings = true;
            }
        }
        if (hasWarnings && !hasErrors) {
            for (SemanticProblem sp: tpc.getProblems()) {
                if (sp.severity == SemanticProblemSeverity.WARNING) {
                    OutputProvider.out("Warning: " + sp.toString());
                }
            }
        }
        return spec;
    }

    /**
     * Make a nice directory at the given path if not available, otherwise verify the given path is actually a
     * directory.
     *
     * @param dirPath Directory to create or verify.
     */
    private static void ensureDirectory(String dirPath) {
        File thePath = new File(dirPath);
        if (!thePath.exists()) {
            // Path does not exist, create it.
            thePath.mkdirs();
            return;
        }
        if (!thePath.isDirectory()) {
            throw new InvalidInputException("\"" + dirPath + "\" is not a directory.");
        }
    }

    /**
     * Write generated Java files to the output directory.
     *
     * @param dirPath Directory path for writing the files.
     * @param ctxt Code generator context containing the generated files.
     */
    private void writeJavaFiles(String dirPath, CodeGeneratorContext ctxt) {
        for (JavaFile jc: ctxt.files) {
            String filePath = resolve(dirPath + "/" + jc.getClassName() + ".java");

            PrintWriter output;
            try {
                FileWriter w = new FileWriter(filePath);
                output = new PrintWriter(w);
            } catch (IOException e) {
                String msg = fmt("Saving of file \"%s\" failed.", filePath);
                throw new InputOutputException(msg, e);
            }
            for (String line: jc.toBox().getLines()) {
                output.printf("%s\n", line);
            }
            output.close();
        }
    }

    /**
     * Compile generated Java code to byte code.
     *
     * @param ctxt Code generator context.
     * @return The run-time Java compiler that holds the compiled code.
     */
    private RuntimeJavaCompiler compileJavaCode(CodeGeneratorContext ctxt) {
        // Construct run-time Java compiler. Use a class loader that can
        // resolve dependencies to the Chi runtime.
        String name = JavaCompilerOption.getCompilerName();
        ClassLoader classLoader = SimulatorApplication.class.getClassLoader();
        RuntimeJavaCompiler compiler = new RuntimeJavaCompiler(name, classLoader);

        // Prepare source file objects.
        Map<String, JavaInputFileObject> sources = map();
        for (JavaFile cls: ctxt.files) {
            JavaCharSeqInputFileObject jif = new JavaCharSeqInputFileObject(cls.getFQclassname(),
                    cls.toBox().toString());
            sources.put(cls.getFQclassname(), jif);
        }

        // Compile the sources.
        try {
            compiler.compile(sources);
        } catch (RuntimeJavaCompilerException e) {
            throw new RuntimeException("Chi compilation failed.", e);
        }

        // Return the compiler, to allow access to the compiled classes.
        return compiler;
    }

    /**
     * Instantiate the main class.
     *
     * @param mainClass Class to instantiate.
     * @param chiCoordinator Central coordinator of the simulator.
     * @return The instantiated class.
     */
    private ChiSpecification instantiateMainClass(Class<? extends ChiSpecification> mainClass,
            ChiCoordinator chiCoordinator)
    {
        Class<?>[] typeParm = {ChiCoordinator.class};
        Object[] parm = {chiCoordinator};

        final String constructError = "Cannot get constructor of main class.";
        Constructor<? extends ChiSpecification> c;
        try {
            c = mainClass.getConstructor(typeParm);
        } catch (SecurityException e) {
            throw new ChiSimulatorException(constructError, e);
        } catch (NoSuchMethodException e) {
            throw new ChiSimulatorException(constructError, e);
        }

        final String createError = "Cannot instantiate main class.";
        ChiSpecification cspec;
        try {
            cspec = c.newInstance(parm);
        } catch (IllegalArgumentException e) {
            throw new ChiSimulatorException(createError, e);
        } catch (InstantiationException e) {
            throw new ChiSimulatorException(createError, e);
        } catch (IllegalAccessException e) {
            throw new ChiSimulatorException(createError, e);
        } catch (InvocationTargetException e) {
            throw new ChiSimulatorException(createError, e);
        }
        return cspec;
    }

    /**
     * Convert a .chi input file to its Java equivalent.
     *
     * @param inFile Name of the Chi input file.
     * @return Generated Java code, or {@code null} if the generation process failed.
     */
    private CodeGeneratorContext generateJavaCode(String inFile) {
        // Parse the file.
        List<Declaration> decls = parseChiFile(inFile);

        // Perform type checking, and optional EMF writing.
        String emfBase = null;
        if (WriteEMFOption.getWriteEMF()) {
            emfBase = Paths.pathChangeExtension(inFile, "chi", null);
        }
        Specification spec = typecheckChiFile(emfBase, decls);
        if (spec == null) {
            return null; // Type check failure.
        }

        // Generate Java code.
        CodeGeneratorContext ctxt = transCodeGen(inFile, spec);

        // Optionally write java code to disk.
        String dirPath = OutputDirectoryOption.getPath();
        if (dirPath != null && !dirPath.isEmpty()) {
            dirPath = resolve(dirPath);

            ensureDirectory(dirPath);
            writeJavaFiles(dirPath, ctxt);
        }
        return ctxt;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected int runInternal() {
        String inFile = InputFileOption.getPath();
        Class<? extends ChiSpecification> mainClass = null;

        // Start from Chi source file?
        if (inFile != null && Paths.pathHasExtension(inFile, "chi")) {
            CodeGeneratorContext ctxt = generateJavaCode(inFile);
            if (ctxt == null) {
                return 1;
            }

            if (!PerformJavaCompileOption.getPerformCompilation()) {
                return 0; // Skip java compilation (and all following steps).
            }

            // Compile the generated code.
            RuntimeJavaCompiler compiler = compileJavaCode(ctxt);

            if (WriteJAROption.getWriteJAR()) {
                // Write the JAR file with the generated code.
                String jarPath = Paths.pathChangeExtension(inFile, "chi", "cchi");
                compiler.writeJarFile(resolve(jarPath), ctxt.specName, null);
                return 0; // End the simulator after writing.
            }
            mainClass = compiler.loadClass(ctxt.specName);
        }

        // Start Specification.main directly?
        String specClassname = RunSpecificationClassOption.getPath();
        if (mainClass == null && inFile == null && specClassname != null) {
            if (Platform.isRunning()) {
                String msg = "Specification application should be run as stand-alone plain Java application. That "
                        + "is, outside the JVM running Eclipse, and also not using command line releases. Use for "
                        + "example a Java Application launch configuration.";
                throw new ChiSimulatorException(msg);
            }

            try {
                ClassLoader loader = getClass().getClassLoader();
                Class<?> cls = loader.loadClass(specClassname);
                mainClass = (Class<? extends ChiSpecification>)cls;
            } catch (ClassNotFoundException e) {
                String msg = "Could not load class \"" + specClassname + "\".";
                throw new ChiSimulatorException(msg, e);
            }
        }

        // Start from compiled cchi file?
        InMemoryJarClassLoader jarClassLoader = null;
        if (mainClass == null && inFile != null && Paths.pathHasExtension(inFile, "cchi")) {
            // Load main class from the JAR file.
            jarClassLoader = new InMemoryJarClassLoader(inFile, getClass().getClassLoader());
            String mainName = jarClassLoader.getMainClassName();
            mainClass = jarClassLoader.loadClassByName(mainName);
        }

        if (mainClass == null) {
            String msg = "Missing input file to run, either provide a .chi or .cchi input file, "
                    + "or a specification class with --run-specification-class.";
            throw new ChiSimulatorException(msg);
        }

        // Run the simulation.
        try {
            // Instantiate the main class.
            long seed = SeedValueOption.getSeedValue();
            boolean seedProvided = (seed != 0);
            ChiCoordinator coord = new ChiCoordinator(this, seed, seedProvided);
            ChiSpecification cspec = instantiateMainClass(mainClass, coord);

            // Get model instantiation text (or use the default one).
            String instanceText = StartupInstanceOption.getStartupInstanceText();
            if (instanceText == null) {
                instanceText = cspec.findDefaultStartupInstance();
            }
            if (instanceText == null) {
                String msg = "No model or xper instance text provided, and no unique parameter-less model or xper "
                        + "definition available.";
                throw new InvalidModelException(msg);
            }

            // Instantiate/start the model.
            BaseProcess model;
            try {
                model = cspec.startStartup(coord, instanceText);
            } catch (ChiSimulatorException e) {
                String msg = "Instance text '" + instanceText + "' could not be matched with a model or an experiment "
                        + "definition, please change the instance text or the file.";
                throw new InvalidModelException(msg, e);
            }

            // Run the simulation, and if requested print info onto the output
            // afterwards.
            SimulationResult simResult = coord.run(model);
            if (OutputInfoOption.getOutputInfo()) {
                OutputProvider.out(simResult.getInfo(seedProvided));
            }
            return 0;
        } finally {
            // Cleanup.
            if (jarClassLoader != null) {
                try {
                    jarClassLoader.close();
                } catch (IOException e) {
                    // Ignore.
                }
            }
        }
    }

    @Override
    public String getAppName() {
        return "Chi simulator";
    }

    @Override
    public String getAppDescription() {
        return "Chi simulator";
    }

    /**
     * Get the simulator option page of the GUI.
     *
     * @return Description of the simulator option page in the GUI.
     */
    private OptionCategory getSimulatorOptionsPage() {
        List<OptionCategory> subPages = list();

        @SuppressWarnings("rawtypes")
        List<Option> options = list();
        options.add(Options.getInstance(InputFileOption.class));
        options.add(Options.getInstance(StartupInstanceOption.class));
        options.add(Options.getInstance(SeedValueOption.class));
        options.add(Options.getInstance(OutputInfoOption.class));

        return new OptionCategory("Simulator", "Chi Simulator Options.", subPages, options);
    }

    /**
     * Get the compiler option page of the GUI.
     *
     * @return Description of the compiler option page in the GUI.
     */
    private OptionCategory getCompilerOptionsPage() {
        List<OptionCategory> subPages = list();

        @SuppressWarnings("rawtypes")
        List<Option> options = list();
        options.add(Options.getInstance(WriteEMFOption.class));
        options.add(Options.getInstance(OutputDirectoryOption.class));
        options.add(Options.getInstance(PerformJavaCompileOption.class));
        options.add(Options.getInstance(WriteJAROption.class));
        options.add(Options.getInstance(JavaCompilerOption.class));

        return new OptionCategory("Compiler", "Chi Compiler Options.", subPages, options);
    }

    /**
     * Get the page with advanced options for the GUI.
     *
     * @return Description of the advanced options GUI page.
     */
    private OptionCategory getAdvancedOptionsPage() {
        List<OptionCategory> subPages = list();

        @SuppressWarnings("rawtypes")
        List<Option> options = list();
        options.add(Options.getInstance(RunSpecificationClassOption.class));

        return new OptionCategory("Advanced", "Chi Advanced Options.", subPages, options);
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected OptionCategory getAllOptions() {
        List<OptionCategory> subPages = list();
        subPages.add(getSimulatorOptionsPage());
        subPages.add(getGeneralOptionCategory());
        subPages.add(getCompilerOptionsPage());
        subPages.add(getAdvancedOptionsPage());

        List<Option> options = list();

        return new OptionCategory("Chi Simulator Options", "All options for the Chi simulator.", subPages, options);
    }
}
