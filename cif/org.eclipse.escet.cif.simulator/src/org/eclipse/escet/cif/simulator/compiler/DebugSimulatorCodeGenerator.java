//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.simulator.compiler;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.options.Option;
import org.eclipse.escet.common.app.framework.options.OptionValue;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Strings;

/** Debug simulator code generator. */
public class DebugSimulatorCodeGenerator {
    /** Constructor for the {@link DebugSimulatorCodeGenerator} class. */
    private DebugSimulatorCodeGenerator() {
        // Static class.
    }

    /**
     * Generate Java code for debugging the simulator code.
     *
     * @param classesPath The absolute path to the compiled classes.
     * @param ctxt The compiler context to use.
     */
    public static void gencodeDebugSimulator(String classesPath, CifCompilerContext ctxt) {
        // Add new code file.
        JavaCodeFile file = ctxt.addCodeFile(CifCompilerContext.DBG_SIM_CLS_NAME);

        // Generate option arguments. Note that it shouldn't matter in which order
        // the option are given. We still sort them however, to get
        // deterministic output for the contents of the debug simulator file.
        List<String> args = list();
        Map<Option<?>, OptionValue<?>> opts = Options.getOptionMap();
        for (Entry<Option<?>, OptionValue<?>> optPair: opts.entrySet()) {
            Option<?> opt = optPair.getKey();
            OptionValue<?> value = optPair.getValue();
            for (String arg: opt.getCmdLine(value.getValue())) {
                args.add(arg);
            }
        }
        Collections.sort(args, Strings.SORTER);
        for (int i = 0; i < args.size(); i++) {
            String arg = args.get(i);
            arg = StringEscapeUtils.escapeJava(arg);
            arg = "\"" + arg + "\"";
            args.set(i, arg);
        }

        // Get path for the current working directory.
        String curWorkingDirPath = StringEscapeUtils.escapeJava(Paths.getCurWorkingDir());

        // Get path for the compiled classes.
        Assert.check(Paths.isAbsolute(classesPath));
        classesPath = StringEscapeUtils.escapeJava(classesPath);

        // Overwrite specific options for debugging.
        args.add(fmt("\"--debug-code=%s\"", classesPath));
        args.add("\"--option-dialog=1\"");

        // Add imports.
        file.imports.add("org.eclipse.escet.cif.simulator.CifSimulatorApp");
        file.imports.add("org.eclipse.escet.common.app.framework.AppProperties");
        file.imports.add("org.junit.Test");

        // Add header.
        CodeBox h = file.header;
        h.add("/** Starts simulator for debugging and contains the simulator options. */");
        h.add("public class %s {", CifCompilerContext.DBG_SIM_CLS_NAME);

        // Add body.
        CodeBox c = file.body;
        c.add("@Test");
        c.add("public void debug() {");
        c.indent();
        c.add("AppProperties properties = new AppProperties();");
        c.add("properties.set(\"user.dir\", \"%s\");", curWorkingDirPath);
        c.add("CifSimulatorApp app = new CifSimulatorApp(null, null, null, properties);");
        c.add();
        c.add("String[] arguments = {" + StringUtils.join(args, ", ") + "};");
        c.add("app.run(arguments);");
        c.dedent();
        c.add("}");
    }
}
