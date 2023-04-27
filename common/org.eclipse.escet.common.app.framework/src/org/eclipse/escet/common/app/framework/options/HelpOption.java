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

package org.eclipse.escet.common.app.framework.options;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.reverse;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.spaces;
import static org.eclipse.escet.common.java.Strings.wrap;
import static org.eclipse.escet.common.java.Strings.wrapEx;

import java.io.IOException;
import java.io.InputStream;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.eclipse.escet.common.app.framework.AppEnv;
import org.eclipse.escet.common.app.framework.Application;
import org.eclipse.escet.common.app.framework.io.AppStream;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Strings;

/** Option to print the help text of the application to the console. */
public class HelpOption extends BooleanOption {
    /**
     * Constructor for the {@link HelpOption} class. Don't directly create instances of this class. Use the
     * {@link Options#getInstance} method instead.
     */
    public HelpOption() {
        super("Help", "Prints the help text of the application to the console.", 'h', "help", null, false, true,
                "Enable option to print the help text of the application to the console, and terminate the "
                        + "application.",
                "Print help message");
    }

    @Override
    public Boolean parseValue(String optName, String value) {
        AppEnv.getApplication().printHelpMessage(AppEnv.getStreams().out);
        throw new Application.SuccessfulExitException();
    }

    @Override
    public String[] getCmdLine(Object value) {
        return new String[] {};
    }

    /**
     * Prints the help message header of an application to the given stream.
     *
     * @param appName The full name of the application.
     * @param version The version of the application.
     * @param description A description of the application.
     * @param s The stream to print the output to.
     */
    public static void printHelpHeader(String appName, String version, String description, AppStream s) {
        String msg = fmt("%s (version %s)", appName, version);
        outw(s, msg);
        s.println();
        outw(s, description);
    }

    /**
     * Prints the help messages of the options of an application to the given stream.
     *
     * @param options The options category wrapper that contains all the option categories and options of the
     *     application.
     * @param s The stream to print the output to.
     */
    public static void printHelpOptions(OptionCategory options, AppStream s) {
        s.println("The available command line options, per category, are:");
        s.println();

        Deque<OptionCategory> cats = new LinkedList<>();
        cats.add(options);
        List<Option<?>> remainingArgsOpts = list();
        while (!cats.isEmpty()) {
            OptionCategory cat = cats.removeFirst();

            if (cat.getParent() != null) {
                outw(s, "[" + cat.getFullName() + "]");
                outw(s, cat.getDescription());
                s.println();
            }

            for (Option<?> opt: cat.getOptions()) {
                if (opt.getCmdLong().equals("*")) {
                    remainingArgsOpts.add(opt);
                    continue;
                }

                String signature = "--" + opt.getCmdLong();
                String cmdValue = opt.getCmdValue();
                if (cmdValue != null) {
                    signature += "=" + cmdValue;
                }
                Character cmdShort = opt.getCmdShort();
                if (cmdShort != null) {
                    signature += ", -" + cmdShort;
                    if (cmdValue != null) {
                        signature += " " + cmdValue;
                    }
                }

                outw(s, "  " + signature);
                s.println(wrapEx(spaces(6), spaces(6), opt.getDescription()));
                s.println();
            }

            List<OptionCategory> subcats = cat.getSubCategories();
            for (OptionCategory subcat: reverse(subcats)) {
                cats.addFirst(subcat);
            }
        }

        Assert.check(remainingArgsOpts.size() <= 1);
        if (remainingArgsOpts.size() == 1) {
            Option<?> opt = remainingArgsOpts.get(0);
            outw(s, "All other (non-option) arguments are interpreted as: " + opt.getDescription());
            s.println();
        }

        outw(s, "The following values can be used to enable any boolean option (BOOL): \"on\", \"true\", \"yes\", "
                + "and \"1\". The corresponding values to disable are: \"off\", \"false\", \"no\", and \"0\".");
    }

    /**
     * Prints the help message notes of an application to the given stream.
     *
     * @param notes The help message notes of the application. Must not be empty.
     * @param s The stream to print the output to.
     */
    public static void printHelpNotes(String[] notes, AppStream s) {
        Assert.check(notes.length > 0);
        s.println("Notes:");
        for (String note: notes) {
            s.println(wrapEx(" - ", "   ", note));
        }
    }

    /**
     * Prints the application exit codes explanation to the given stream.
     *
     * @param s The stream to print the output to.
     */
    public static void printHelpExitCodes(AppStream s) {
        outw(s, "The application can terminate with the following exit codes:");
        String[] lines = { //
                "Application finished without errors.", //
                "Application finished after reporting an error to the end user.", //
                "Application crashed after running out of memory.", //
                "Application crashed for any reason other than running out of memory.", //
        };
        Assert.check(lines.length <= 9); // Single digit exit codes.
        for (int i = 0; i < lines.length; i++) {
            s.println(wrapEx(spaces(2) + i + spaces(2), spaces(5), lines[i]));
        }
    }

    /**
     * Prints standard Eclipse ESCET copyright information to the given stream.
     *
     * @param s The stream to print the output to.
     */
    public static void printHelpCopyrightEclipseEscet(AppStream s) {
        outw(s, "Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation");
        outw(s, "");
        outw(s, "See the NOTICE file(s) distributed with this work for additional information regarding copyright "
                + "ownership.");
        outw(s, "");
        outw(s, "Eclipse ESCET and ESCET are trademarks of the Eclipse Foundation. Eclipse, and the Eclipse Logo "
                + "are registered trademarks of the Eclipse Foundation. Other names may be trademarks of their "
                + "respective owners.");
        outw(s, "");
        outw(s, "This product includes software developed by other open source projects.");
    }

    /**
     * Prints standard Eclipse ESCET license information to the given stream.
     *
     * @param s The stream to print the output to.
     */
    public static void printHelpLicenseEclipseEscet(AppStream s) {
        String resourceName = "org/eclipse/escet/common/app/framework/LICENSE.txt";
        InputStream licenseStream = HelpOption.class.getClassLoader().getResourceAsStream(resourceName);
        String licenseText;
        if (licenseStream == null) {
            licenseText = "ERROR: Could not find license file.";
        } else {
            try {
                licenseText = IOUtils.toString(licenseStream, "UTF-8");
            } catch (IOException e) {
                licenseText = "ERROR: Failed to read license file.";
            }
        }
        s.print(licenseText);
        s.flush();
    }

    /**
     * {@link Strings#wrap Wrap} and print the given output. This method uses {@link Strings#fmt} to obtain the string
     * to wrap.
     *
     * @param s The stream to print the output to.
     * @param msg The 'normal' output (pattern) to wrap and forward.
     * @param args The arguments of the 'normal' output pattern.
     */
    private static void outw(AppStream s, String msg, Object... args) {
        for (String txt: wrap(fmt(msg, args))) {
            s.println(txt);
        }
    }
}
