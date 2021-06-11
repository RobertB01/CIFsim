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

package org.eclipse.escet.common.app.framework;

import static org.eclipse.escet.common.java.DateTimeUtils.durationToString;
import static org.eclipse.escet.common.java.DateTimeUtils.formatDateTime;
import static org.eclipse.escet.common.java.DateTimeUtils.nanoTimeToMillis;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.io.File;
import java.io.IOException;
import java.lang.management.ClassLoadingMXBean;
import java.lang.management.CompilationMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.MissingResourceException;

import org.eclipse.core.runtime.Platform;
import org.eclipse.escet.common.app.framework.io.AppStream;
import org.eclipse.escet.common.app.framework.io.FileAppStream;
import org.eclipse.escet.common.app.framework.io.StdAppStream;
import org.eclipse.escet.common.app.framework.options.OptionCategory;
import org.eclipse.escet.common.app.framework.output.IOutputComponent;
import org.eclipse.escet.common.app.framework.output.OutputProvider;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.FileSizes;
import org.eclipse.escet.common.java.Strings;
import org.osgi.framework.Bundle;

/** Class for generating crash reports. */
public final class CrashReport {
    /** Constructor for the {@link CrashReport} class. */
    private CrashReport() {
        // Private constructor to make the class static.
    }

    /**
     * Main method for testing the crash report.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        final Application<?> app = new Application<>() {
            @Override
            protected OutputProvider<IOutputComponent> getProvider() {
                return null; // Not really valid, but suffices here.
            }

            @Override
            protected int runInternal() {
                return 0;
            }

            @Override
            public String getAppName() {
                return "Test App Name";
            }

            @Override
            public String getAppDescription() {
                return "Test App Description.";
            }

            @Override
            protected OptionCategory getAllOptions() {
                return null; // Not really valid, but suffices here.
            }
        };

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                AppEnv.registerApplication(new AppEnvData(app));
                try {
                    throw new IllegalArgumentException("Crash report test.");
                } catch (Exception ex) {
                    writeCrashReport(ex, StdAppStream.OUT);
                }
            }
        });

        t.start();
    }

    /**
     * Writes a crash report for the given exception to a crash report file.
     *
     * @param ex The exception to write the crash report for.
     * @return The absolute local file system path to the file that contains the crash report.
     */
    public static String writeCrashReportFile(Throwable ex) {
        // Get temporary file.
        File tempDir = new File(Paths.getCurWorkingDir());
        File tempFile;
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS", Locale.US);
            tempFile = File.createTempFile("crash_report_" + df.format(new Date()) + "_", ".log", tempDir);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create crash report file.", e);
        }
        String tempFilePath = tempFile.getAbsolutePath();

        // Write crash report to the file.
        AppStream s = null;
        try {
            s = new FileAppStream(tempFilePath);
            writeCrashReport(ex, s);
            s.flush();
            s.close();
        } finally {
            if (s != null) {
                s.close();
            }
        }

        // Return the path to the crash report.
        return tempFilePath;
    }

    /**
     * Writes a crash report for the given exception to the given {@link AppStream}.
     *
     * @param ex The exception to write the crash report for.
     * @param s The {@link AppStream} to write the crash report to.
     */
    public static void writeCrashReport(Throwable ex, AppStream s) {
        // Get application.
        Application<?> app = AppEnv.getApplication();

        // Provide users with issue reporting instructions.
        s.println("[Reporting]");
        for (String line: Strings.wrap(app.getCrashReportIssueReportingInstructions())) {
            s.println(line);
        }
        s.println();

        // Exception.
        s.println("[Exception]");
        s.printStackTrace(ex);
        s.println();

        // Application framework.
        s.println("[Application]");
        s.println("Application name    = " + app.getAppName());
        s.println("Application version = " + app.getAppVersionSafe());
        s.println("Application class   = " + app.getClass().getName());
        s.println();

        // Application framework's option framework.
        s.println("[Options]");
        s.println("Command line arguments = "
                + AppEnv.getProperty("org.eclipse.escet.common.app.framework.args.cmdline"));
        s.println("Dialog arguments       = "
                + AppEnv.getProperty("org.eclipse.escet.common.app.framework.args.dialog", "N/A"));
        s.println(
                "Final arguments        = " + AppEnv.getProperty("org.eclipse.escet.common.app.framework.args.final"));
        s.println();

        // Date/time.
        s.println("[Date/time]");
        long startEpoch = Long.parseLong(AppEnv.getProperty("org.eclipse.escet.common.app.framework.start.epoch", "0"));
        long startNano = Long.parseLong(AppEnv.getProperty("org.eclipse.escet.common.app.framework.start.nanos", "0"));
        Date startDate = new Date(startEpoch);
        Date curDate = new Date();
        long curNano = System.nanoTime();
        long runningTime = curNano - startNano;
        String[][] dateTable = { //
                {"App start (local)", "=", formatDateTime(startDate, false, true)}, //
                {"App start (UTC)", "=", formatDateTime(startDate, true, true)}, //
                {"Crash (local)", "=", formatDateTime(curDate, false, true)}, //
                {"Crash (UTC)", "=", formatDateTime(curDate, true, true)}, //
                {"App running time", "=", durationToString(nanoTimeToMillis(runningTime), false)}, //
        };
        s.println(formatTable(dateTable));
        s.println();

        // System Properties.
        s.println("[System Properties]");
        s.println(formatTable(sortTable(getPropertiesTable())));
        s.println();

        // Environment.
        s.println("[Environment]");
        s.println(formatTable(sortTable(getEnvironmentTable())));
        s.println();

        // JVM Runtime.
        s.println("[JVM Runtime]");
        Runtime r = Runtime.getRuntime();
        String[][] runtimeTable = { //
                {"Processors", "=", String.valueOf(r.availableProcessors())}, //
                {"Memory (free)", "=", FileSizes.formatFileSizeEx(r.freeMemory())}, //
                {"Memory (max)", "=", FileSizes.formatFileSizeEx(r.maxMemory())}, //
                {"Memory (total)", "=", FileSizes.formatFileSizeEx(r.totalMemory())}, //
                {"Default class loader", "=", ClassLoader.getSystemClassLoader().getClass().getName()}, //
        };
        s.println(formatTable(runtimeTable));
        s.println();

        // Threads.
        s.println("[Threads]");
        printThreadGroups(getRootThreadGroup(), s, 0);
        s.println();

        // File systems.
        s.println("[File systems]");
        File[] roots = File.listRoots();
        if (roots == null) {
            s.println("(file system roots could not be determined)");
        } else {
            for (File root: File.listRoots()) {
                s.println("Path: " + root.getPath());
                s.println("    Free space   = " + FileSizes.formatFileSizeEx(root.getFreeSpace()));
                s.println("    Total space  = " + FileSizes.formatFileSizeEx(root.getTotalSpace()));
                s.println("    Usable space = " + FileSizes.formatFileSizeEx(root.getUsableSpace()));
            }
        }
        s.println();

        // Locale.
        s.println("[Locale]");
        Locale locale = Locale.getDefault();
        String isoCountry;
        String isoLanguage;
        try {
            isoCountry = locale.getISO3Country();
        } catch (MissingResourceException e) {
            isoCountry = "<unknown>";
        }
        try {
            isoLanguage = locale.getISO3Language();
        } catch (MissingResourceException e) {
            isoLanguage = "<unknown>";
        }
        String[][] localeTable = { //
                {"Default locale", "=", locale.toString()}, //
                {"Country", "=", locale.getCountry()}, //
                {"Display country", "=", locale.getDisplayCountry()}, //
                {"Display language", "=", locale.getDisplayLanguage()}, //
                {"Display name", "=", locale.getDisplayName()}, //
                {"Display variant", "=", locale.getDisplayVariant()}, //
                {"ISO 3166 country", "=", isoCountry}, //
                {"ISO 639-2/T language", "=", isoLanguage}, //
                {"Language", "=", locale.getLanguage()}, //
                {"Variant", "=", locale.getVariant()}, //
        };
        for (String[] localeEntry: localeTable) {
            localeEntry[2] = Strings.stringToJava(localeEntry[2]);
        }
        s.println(formatTable(localeTable));
        s.println();

        // Management data.
        s.println("[Management data]");
        ClassLoadingMXBean clbean = ManagementFactory.getClassLoadingMXBean();
        CompilationMXBean cmpbean = ManagementFactory.getCompilationMXBean();
        MemoryMXBean membean = ManagementFactory.getMemoryMXBean();
        OperatingSystemMXBean osbean = ManagementFactory.getOperatingSystemMXBean();
        RuntimeMXBean rtbean = ManagementFactory.getRuntimeMXBean();
        ThreadMXBean tbean = ManagementFactory.getThreadMXBean();
        Date startTime = new Date(rtbean.getStartTime());
        String[][] managementTable = { //
                {"Current loaded class count", "=", String.valueOf(clbean.getLoadedClassCount())}, //
                {"Total loaded class count", "=", String.valueOf(clbean.getTotalLoadedClassCount())}, //
                {"Unloaded class count", "=", String.valueOf(clbean.getUnloadedClassCount())}, //

                {"JIT compiler name", "=", (cmpbean == null) ? "<N/A>" : cmpbean.getName()}, //
                {"Total compilation time", "=",
                        (cmpbean == null) ? "<N/A>" : (cmpbean.isCompilationTimeMonitoringSupported())
                                ? durationToString(cmpbean.getTotalCompilationTime(), false) : "<not supported>"}, //

                {"Heap memory usage", "=", memoryUsageToString(membean.getHeapMemoryUsage())}, //
                {"Non-heap memory usage", "=", memoryUsageToString(membean.getNonHeapMemoryUsage())}, //
                {"Object pending finaliz. cnt", "=", String.valueOf(membean.getObjectPendingFinalizationCount())}, //

                {"Operating system architecture", "=", osbean.getArch()}, //
                {"Available processors", "=", String.valueOf(osbean.getAvailableProcessors())}, //
                {"Operating system name", "=", osbean.getName()}, //
                {"System load average", "=",
                        (osbean.getSystemLoadAverage() < 0) ? "<not available>"
                                : String.valueOf(osbean.getSystemLoadAverage())}, //
                {"Operating system version", "=", osbean.getVersion()}, //

                {"Boot class path", "=",
                        rtbean.isBootClassPathSupported() ? rtbean.getBootClassPath() : "<unsupported>"}, //
                {"Class path", "=", rtbean.getClassPath()}, //
                {"Runtime input arguments", "=",
                        Strings.stringArrayToJava(rtbean.getInputArguments().toArray(new String[] {}))}, //
                {"Library path", "=", rtbean.getLibraryPath()}, //
                {"Management spec version", "=", rtbean.getManagementSpecVersion()}, //
                {"Runtime name", "=", rtbean.getName()}, //
                {"Spec name", "=", rtbean.getSpecName()}, //
                {"Spec vendor", "=", rtbean.getSpecVendor()}, //
                {"Spec version", "=", rtbean.getSpecVersion()}, //
                {"Runtime start time (local)", "=", formatDateTime(startTime, false, true)}, //
                {"Runtime start time (UTC)", "=", formatDateTime(startTime, true, true)}, //
                {"Runtime up-time", "=", durationToString(rtbean.getUptime(), false)}, //
                {"VM name", "=", rtbean.getVmName()}, //
                {"VM vendor", "=", rtbean.getVmVendor()}, //
                {"VM version", "=", rtbean.getVmVersion()}, //

                {"Deamon thread count", "=", String.valueOf(tbean.getDaemonThreadCount())}, //
                {"Peak thread count", "=", String.valueOf(tbean.getPeakThreadCount())}, //
                {"Thread count", "=", String.valueOf(tbean.getThreadCount())}, //
                {"Total started thread count", "=", String.valueOf(tbean.getTotalStartedThreadCount())}, //
        };
        s.println(formatTable(managementTable));
        s.println();

        // Platform (OSGi).
        s.println("[Platform (OSGi)]");
        if (!Platform.isRunning()) {
            s.println("(OSGi platform is not running)");
        } else {
            s.println(formatTable(getOsgiTable()));
        }
        s.println();

        // Bundles (OSGi).
        s.println("[Bundles (OSGi)]");
        if (!Platform.isRunning()) {
            s.println("(OSGi platform is not running)");
        } else {
            Bundle[] bundles = Activator.getContext().getBundles();
            String[][] bundleTable = new String[bundles.length][6];
            int i = 0;
            for (Bundle bundle: bundles) {
                bundleTable[i][0] = String.valueOf(bundle.getBundleId());
                bundleTable[i][1] = PlatformUtils.getStateName(bundle);
                bundleTable[i][2] = " ";
                bundleTable[i][3] = bundle.getSymbolicName();
                bundleTable[i][4] = bundle.getVersion().toString();
                bundleTable[i][5] = bundle.getLocation();
                i++;
            }
            s.println(formatTable(sortTable(bundleTable, 3)));
        }
        s.println();
    }

    /**
     * Returns a table filled with the system properties.
     *
     * @return A table filled with the system properties.
     * @see AppEnv#getProperties
     */
    private static String[][] getPropertiesTable() {
        AppProperties props = AppEnv.getProperties();
        String[][] table = new String[props.size()][3];
        int i = 0;
        for (Entry<String, String> entry: props.entrySet()) {
            table[i][0] = entry.getKey();
            table[i][1] = "=";
            table[i][2] = Strings.stringToJava(entry.getValue());
            i++;
        }
        return table;
    }

    /**
     * Returns a table filled with the environment variables.
     *
     * @return A table filled with the environment variables.
     * @see System#getenv
     */
    private static String[][] getEnvironmentTable() {
        Map<String, String> env = System.getenv();
        String[][] table = new String[env.size()][3];
        int i = 0;
        for (Entry<String, String> entry: env.entrySet()) {
            table[i][0] = entry.getKey();
            table[i][1] = "=";
            table[i][2] = Strings.stringToJava(entry.getValue());
            i++;
        }
        return table;
    }

    /**
     * Returns a table filled with OSGi platform information. This method may only be invoked if the platform is
     * {@link Platform#isRunning running}.
     *
     * @return A table filled with OSGi platform information.
     * @see Platform
     */
    private static String[][] getOsgiTable() {
        Assert.check(Platform.isRunning());
        String[][] table = new String[][] { //
                {"Application arguments", "X", Strings.stringArrayToJava(Platform.getApplicationArgs())}, //
                {"Command line arguments", "X", Strings.stringArrayToJava(Platform.getCommandLineArgs())}, //
                {"Configuration location", "=", Platform.getConfigurationLocation().getURL().toString()}, //
                {"Install location", "=", Platform.getInstallLocation().getURL().toString()}, //
                {"Instance location", "=", Platform.getInstanceLocation().getURL().toString()}, //
                {"Location", "=", Platform.getLocation().toString()}, //
                {"Log file location", "=", Platform.getLogFileLocation().toString()}, //
                {"Natural language", "=", Platform.getNL()}, //
                {"Natural language extensions", "=", Platform.getNLExtensions()}, //
                {"Operating System", "=", Platform.getOS()}, //
                {"Operating System architecture", "=", Platform.getOSArch()}, //
                {"Product / Application", "=", Platform.getProduct().getApplication()}, //
                {"Product / Description", "=", Platform.getProduct().getDescription()}, //
                {"Product / Id", "=", Platform.getProduct().getId()}, //
                {"Product / Name", "=", Platform.getProduct().getName()}, //
                {"Product / Defining bundle", "=", Platform.getProduct().getDefiningBundle().getSymbolicName()}, //
                {"State stamp", "=", ((Long)Platform.getStateStamp()).toString()}, //
                {"User location", "=", Platform.getUserLocation().getURL().toString()}, //
                {"Window system", "=", Platform.getWS()}, //
        };
        for (String[] row: table) {
            if (row[1].equals("X")) {
                row[1] = "=";
            } else {
                row[2] = Strings.stringToJava(row[2]);
            }
        }
        return table;
    }

    /**
     * Sorts a table on the first column.
     *
     * @param table The table to sort.
     * @return The sorted table.
     */
    private static String[][] sortTable(String[][] table) {
        return sortTable(table, 0);
    }

    /**
     * Sorts a table on a given column.
     *
     * @param table The table to sort.
     * @param sortColIdx The 0-based index of the column to sort.
     * @return The sorted table.
     */
    private static String[][] sortTable(String[][] table, int sortColIdx) {
        List<List<String>> tableList = list();
        for (String[] row: table) {
            tableList.add(list(row));
        }
        Collections.sort(tableList, new TableRowComparator(sortColIdx));
        String[][] rslt = new String[tableList.size()][];
        String[] dummy = new String[0];
        for (int i = 0; i < tableList.size(); i++) {
            rslt[i] = tableList.get(i).toArray(dummy);
        }
        return rslt;
    }

    /**
     * {@link Comparator} for table rows, that can compare on a given column. The actual elements are compared using
     * {@link Strings#SORTER}.
     */
    private static class TableRowComparator implements Comparator<List<String>> {
        /** The 0-based index of the columns to compare. */
        private final int sortColIdx;

        /**
         * Constructor for the {@link TableRowComparator} class.
         *
         * @param sortColIdx The 0-based index of the columns to compare.
         */
        public TableRowComparator(int sortColIdx) {
            this.sortColIdx = sortColIdx;
        }

        @Override
        public int compare(List<String> arg0, List<String> arg1) {
            Assert.check(arg0.size() > sortColIdx);
            Assert.check(arg1.size() > sortColIdx);
            return Strings.SORTER.compare(arg0.get(sortColIdx), arg1.get(sortColIdx));
        }
    }

    /**
     * Formats a table to a readable representation.
     *
     * @param table The table to format. Note that jagged arrays are not supported. That is, the sub-arrays must all
     *     have the same length.
     * @return The formatted table.
     */
    private static String formatTable(String[][] table) {
        // Get maximum width of each column.
        int numcols = table[0].length;
        int[] width = new int[numcols];
        for (String[] row: table) {
            for (int i = 0; i < numcols; i++) {
                if (row[i].length() > width[i]) {
                    width[i] = row[i].length();
                }
            }
        }

        // Format the table.
        StringBuilder b = new StringBuilder();
        for (int r = 0; r < table.length; r++) {
            String[] row = table[r];
            for (int c = 0; c < numcols; c++) {
                if (c > 0) {
                    b.append(" ");
                }
                b.append(row[c]);
                if (c != numcols - 1) {
                    b.append(Strings.spaces(width[c] - row[c].length()));
                }
            }
            if (r != table.length - 1) {
                b.append('\n');
            }
        }

        // Return formatted table.
        return b.toString();
    }

    /**
     * Get the root {@link ThreadGroup} of the current application (Java interpreter).
     *
     * @return The root {@link ThreadGroup} of the current application (Java interpreter).
     */
    private static ThreadGroup getRootThreadGroup() {
        ThreadGroup grp = Thread.currentThread().getThreadGroup();
        ThreadGroup parent = grp.getParent();
        while (parent != null) {
            grp = parent;
            parent = grp.getParent();
        }
        return grp;
    }

    /**
     * Prints the details of the given {@link ThreadGroup} to the given {@link AppStream}, using the given indentation
     * level.
     *
     * @param grp The {@link ThreadGroup} to print the details for.
     * @param s The {@link AppStream} to print the details to.
     * @param indent The indentation level to use.
     */
    private static void printThreadGroups(ThreadGroup grp, AppStream s, int indent) {
        s.println(fmt("%sThreadGroup[name=%s, maxpi=%d, daemon=%s, destroyed=%s]", Strings.spaces(indent),
                grp.getName(), grp.getMaxPriority(), grp.isDaemon(), grp.isDestroyed()));

        for (Thread t: getChildThreads(grp)) {
            s.println(fmt( //
                    "%sThread[name=%s, id=%d, prio=%d, state=%s%s%s%s%s]", //
                    Strings.spaces(indent + 4), t.getName(), t.getId(), t.getPriority(), t.getState(), //
                    t.isAlive() ? ", alive" : ", dormant", //
                    t.isDaemon() ? ", daemon" : "", //
                    t.isInterrupted() ? ", interrupted" : "", //
                    t == Thread.currentThread() ? ", current" : "" //
            ));
            printStackTrace(t.getStackTrace(), s, indent + 8);
        }

        for (ThreadGroup child: getChildThreadGroups(grp)) {
            printThreadGroups(child, s, indent + 4);
        }
    }

    /**
     * Returns the direct child thread groups of the given {@link ThreadGroup}.
     *
     * @param grp The {@link ThreadGroup} to get the direct child thread groups for.
     * @return The direct child thread groups of the given thread group.
     */
    private static ThreadGroup[] getChildThreadGroups(ThreadGroup grp) {
        int guess = grp.activeGroupCount() + 1;
        int cnt = 0;
        ThreadGroup[] groups;
        while (true) {
            guess *= 2;
            groups = new ThreadGroup[guess];
            cnt = grp.enumerate(groups, false);
            if (cnt < guess) {
                break;
            }
        }
        ThreadGroup[] rslt = new ThreadGroup[cnt];
        System.arraycopy(groups, 0, rslt, 0, cnt);
        return rslt;
    }

    /**
     * Returns the direct child threads of the given {@link ThreadGroup}.
     *
     * @param grp The {@link ThreadGroup} to get the direct child threads for.
     * @return The direct child threads of the given thread group.
     */
    private static Thread[] getChildThreads(ThreadGroup grp) {
        int guess = grp.activeCount() + 1;
        int cnt = 0;
        Thread[] threads;
        while (true) {
            guess *= 2;
            threads = new Thread[guess];
            cnt = grp.enumerate(threads, false);
            if (cnt < guess) {
                break;
            }
        }
        Thread[] rslt = new Thread[cnt];
        System.arraycopy(threads, 0, rslt, 0, cnt);
        return rslt;
    }

    /**
     * Prints the given stack trace to the given {@link AppStream}, using the given indentation level.
     *
     * @param trace The stack trace to print.
     * @param s The {@link AppStream} to print to.
     * @param indent The indentation level to use.
     */
    private static void printStackTrace(StackTraceElement[] trace, AppStream s, int indent) {
        for (int i = 0; i < trace.length; i++) {
            s.println(Strings.spaces(indent) + trace[i].toString());
        }
    }

    /**
     * Converts a {@link MemoryUsage} instance to end-user readable text.
     *
     * @param mu The {@link MemoryUsage} instance to convert.
     * @return The converted {@link MemoryUsage} instance, in end-user readable text.
     */
    private static String memoryUsageToString(MemoryUsage mu) {
        String init = (mu.getInit() == -1) ? "undefined" : FileSizes.formatFileSizeEx(mu.getInit());
        String commit = FileSizes.formatFileSizeEx(mu.getCommitted());
        String used = FileSizes.formatFileSizeEx(mu.getUsed());
        String max = (mu.getMax() == -1) ? "undefined" : FileSizes.formatFileSizeEx(mu.getMax());
        return fmt("init=%s, commit=%s, used=%s, max=%s", init, commit, used, max);
    }
}
