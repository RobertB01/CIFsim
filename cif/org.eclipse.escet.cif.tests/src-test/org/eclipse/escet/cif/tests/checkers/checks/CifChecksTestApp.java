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

package org.eclipse.escet.cif.tests.checkers.checks;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Arrays;
import java.util.List;

import org.eclipse.escet.cif.checkers.CifCheck;
import org.eclipse.escet.cif.checkers.CifPreconditionChecker;
import org.eclipse.escet.cif.cif2cif.CifToCifTransformation;
import org.eclipse.escet.cif.cif2cif.app.CifToCifTransOption;
import org.eclipse.escet.cif.io.CifReader;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.common.app.framework.Application;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.io.AppStreams;
import org.eclipse.escet.common.app.framework.options.InputFileOption;
import org.eclipse.escet.common.app.framework.options.OptionCategory;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.StringOption;
import org.eclipse.escet.common.app.framework.output.IOutputComponent;
import org.eclipse.escet.common.app.framework.output.OutputProvider;
import org.eclipse.escet.common.java.Assert;

/** CIF checks test application. */
public class CifChecksTestApp extends Application<IOutputComponent> {
    /**
     * Constructor for the {@link CifChecksTestApp} class.
     *
     * @param streams The streams to use for input, output, and error streams.
     */
    public CifChecksTestApp(AppStreams streams) {
        super(streams);
    }

    @Override
    public String getAppName() {
        return "CIF checks tester";
    }

    @Override
    public String getAppDescription() {
        return "Tests CIF checks on CIF specifications.";
    }

    @Override
    protected int runInternal() {
        // Read CIF specification.
        CifReader cifReader = new CifReader().init();
        Specification spec = cifReader.read();
        String absSpecPath = Paths.resolve(InputFileOption.getPath());
        if (isTerminationRequested()) {
            return 0;
        }

        // Add configured check.
        List<CifCheck> checks = list();
        String simpleCheckClassName = CifCheckClassNameToTestOption.getCheckClassNameToTest();
        String[] packageNames = { //
                CifCheck.class.getPackageName() + ".checks", // Checks package.
                getClass().getPackageName() // Test checks package.
        };
        for (String packageName: packageNames) {
            // Get class name and load the class.
            String fullCheckClassName = packageName + "." + simpleCheckClassName;
            Class<?> cls;
            try {
                cls = getClass().getClassLoader().loadClass(fullCheckClassName);
            } catch (ClassNotFoundException e) {
                // Try next package.
                continue;
            }

            // Instantiate the check.
            CifCheck check;
            try {
                check = (CifCheck)cls.getDeclaredConstructor().newInstance();
            } catch (ReflectiveOperationException | IllegalArgumentException | SecurityException e) {
                throw new RuntimeException("Failed to instantiate class: " + fullCheckClassName);
            }

            // Add the check.
            checks.add(check);
        }
        Assert.check(!checks.isEmpty(),
                fmt("Class \"%s\" not found in packages: %s", simpleCheckClassName, Arrays.toString(packageNames)));

        // Perform preprocessing.
        List<CifToCifTransformation> transformations = CifToCifTransOption.getTransformations();
        for (CifToCifTransformation transformation: transformations) {
            transformation.transform(spec);
        }

        // Perform check.
        new CifPreconditionChecker(checks).reportPreconditionViolations(spec, absSpecPath, "CIF checks tester");

        // All done.
        return 0;
    }

    @Override
    protected OutputProvider<IOutputComponent> getProvider() {
        return new OutputProvider<>();
    }

    @Override
    protected OptionCategory getAllOptions() {
        OptionCategory generalOpts = getGeneralOptionCategory();

        OptionCategory transOpts = new OptionCategory("Checks", "Check options.", list(),
                list(Options.getInstance(InputFileOption.class),
                        Options.getInstance(CifCheckClassNameToTestOption.class),
                        Options.getInstance(CifToCifTransOption.class)));

        OptionCategory options = new OptionCategory("CIF Checks Tester Options",
                "All options for the CIF checks tester.", list(generalOpts, transOpts), list());

        return options;
    }

    /** CIF check class name option. */
    public static class CifCheckClassNameToTestOption extends StringOption {
        /** Constructor for the {@link CifCheckClassNameToTestOption} class. */
        public CifCheckClassNameToTestOption() {
            super(
                    // name
                    "Check",

                    // description
                    "Specify the name of the check class to test.",

                    // cmdShort
                    null,

                    // cmdLong
                    "check-class-name",

                    // cmdValue
                    "NAME",

                    // defaultValue
                    null,

                    // emptyAsNull
                    true,

                    // showInDialog
                    false,

                    // optDialogDescr
                    null,

                    // optDialogLabelText
                    null);
        }

        /**
         * Returns the name of the check class to test.
         *
         * @return The name of the check class to test.
         */
        public static String getCheckClassNameToTest() {
            return Options.get(CifCheckClassNameToTestOption.class);
        }
    }
}
