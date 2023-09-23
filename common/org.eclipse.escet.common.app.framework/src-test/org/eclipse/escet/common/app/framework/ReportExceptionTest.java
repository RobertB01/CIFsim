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

package org.eclipse.escet.common.app.framework;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.escet.common.app.framework.exceptions.ApplicationException;
import org.eclipse.escet.common.app.framework.io.AppStream;
import org.eclipse.escet.common.app.framework.io.AppStreams;
import org.eclipse.escet.common.app.framework.io.MemAppStream;
import org.eclipse.escet.common.app.framework.management.AppManager;
import org.eclipse.escet.common.app.framework.options.OptionCategory;
import org.eclipse.escet.common.app.framework.output.IOutputComponent;
import org.eclipse.escet.common.app.framework.output.OutputProvider;
import org.junit.jupiter.api.Test;

/** Unit tests for testing application framework exception reporting. */
public class ReportExceptionTest {
    @Test
    @SuppressWarnings("javadoc")
    public void testSimple() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                throw new ApplicationException("Some error...");
            }
        };

        test(runnable, "ERROR: Some error...\n");
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testCause1() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                RuntimeException cause = new RuntimeException("Cause...");
                throw new ApplicationException("Some error...", cause);
            }
        };

        test(runnable, "ERROR: Some error...\nCAUSE: (RuntimeException) Cause...\n");
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testCause3() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                RuntimeException cause3 = new RuntimeException("Cause 3...");
                RuntimeException cause2 = new RuntimeException("Cause 2...", cause3);
                RuntimeException cause1 = new RuntimeException("Cause 1...", cause2);
                throw new ApplicationException("Some error...", cause1);
            }
        };

        test(runnable, "ERROR: Some error...\n" + //
                "CAUSE: (RuntimeException) Cause 1...\n" + //
                "CAUSE: (RuntimeException) Cause 2...\n" + //
                "CAUSE: (RuntimeException) Cause 3...\n");
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testOutOfMemory() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                throw new OutOfMemoryError("Some error...");
            }
        };

        test(runnable, "ERROR: The application has run out of memory.\nCAUSE: (OutOfMemoryError) Some error...\n");
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testSuppression() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try (SimpleResource res1 = new SimpleResource("Close1");
                     SimpleResource res2 = new SimpleResource("Close2"))
                {
                    throw new ApplicationException("Some error...");
                }
            }
        };

        test(runnable, "ERROR: Some error...\n\nERROR: Close2\n\nERROR: Close1\n");
    }

    /** Simple resource used to test suppressed exceptions generated by the try-with-resource concept. */
    private static class SimpleResource implements AutoCloseable {
        /** Exception message. */
        private final String msg;

        /**
         * Constructor for the {@link SimpleResource} class.
         *
         * @param msg Exception message.
         */
        public SimpleResource(String msg) {
            this.msg = msg;
        }

        @Override
        public void close() throws ApplicationException {
            throw new ApplicationException(msg);
        }
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testSuppressions() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                AtomicInteger counter = new AtomicInteger(0);
                testSuppressionsInternal(true, counter);
            }
        };

        test(runnable, //
                "ERROR: First.\n" + //
                        "CAUSE: Second.\n" + //
                        "\n" + //
                        "ERROR: Close failure 3 second outer #1\n" + //
                        "CAUSE: Close cause 3 second outer #1\n" + //
                        "\n" + //
                        "ERROR: Close failure 3 second inner #2\n" + //
                        "CAUSE: Close cause 3 second inner #2\n" + //
                        "\n" + //
                        "ERROR: Close failure 2 second outer #3\n" + //
                        "CAUSE: Close cause 2 second outer #3\n" + //
                        "\n" + //
                        "ERROR: Close failure 2 second inner #4\n" + //
                        "CAUSE: Close cause 2 second inner #4\n" + //
                        "\n" + //
                        "ERROR: Close failure 1 second outer #5\n" + //
                        "CAUSE: Close cause 1 second outer #5\n" + //
                        "\n" + //
                        "ERROR: Close failure 1 second inner #6\n" + //
                        "CAUSE: Close cause 1 second inner #6\n" + //
                        "\n" + //
                        "ERROR: Close failure 0 second outer #7\n" + //
                        "CAUSE: Close cause 0 second outer #7\n" + //
                        "\n" + //
                        "ERROR: Close failure 0 second inner #8\n" + //
                        "CAUSE: Close cause 0 second inner #8\n" + //
                        "\n" + //
                        "ERROR: Close failure 3 first outer #9\n" + //
                        "CAUSE: Close cause 3 first outer #9\n" + //
                        "\n" + //
                        "ERROR: Close failure 3 first inner #10\n" + //
                        "CAUSE: Close cause 3 first inner #10\n" + //
                        "\n" + //
                        "ERROR: Close failure 2 first outer #11\n" + //
                        "CAUSE: Close cause 2 first outer #11\n" + //
                        "\n" + //
                        "ERROR: Close failure 2 first inner #12\n" + //
                        "CAUSE: Close cause 2 first inner #12\n" + //
                        "\n" + //
                        "ERROR: Close failure 1 first outer #13\n" + //
                        "CAUSE: Close cause 1 first outer #13\n" + //
                        "\n" + //
                        "ERROR: Close failure 1 first inner #14\n" + //
                        "CAUSE: Close cause 1 first inner #14\n" + //
                        "\n" + //
                        "ERROR: Close failure 0 first outer #15\n" + //
                        "CAUSE: Close cause 0 first outer #15\n" + //
                        "\n" + //
                        "ERROR: Close failure 0 first inner #16\n" + //
                        "CAUSE: Close cause 0 first inner #16\n" //
        );
    }

    /**
     * Internal method to generate the exception with causes and suppressions, as well suppressions with suppressions,
     * etc.
     *
     * @param first Is this the first call, or the second (recursive) call?
     * @param counter Counter to use to generate incremental integer values, to use in error messages, to make them
     *     distinguishable. Used to check that exceptions are reported in the correct order.
     */
    private void testSuppressionsInternal(boolean first, AtomicInteger counter) {
        try (MyResource res1 = new MyResource(0, first, false, counter);
             MyResource res2 = new MyResource(1, first, false, counter))
        {
            try (MyResource res3 = new MyResource(2, first, false, counter);
                 MyResource res4 = new MyResource(3, first, false, counter))
            {
                if (first) {
                    try {
                        testSuppressionsInternal(!first, counter);
                    } catch (Throwable ex) {
                        throw new ApplicationException("First.", ex);
                    }
                } else {
                    throw new ApplicationException("Second.");
                }
            }
        }
    }

    /** Resource used to test suppressed exceptions generated by the try-with-resource concept. */
    private class MyResource implements AutoCloseable {
        /** Resource index. */
        private final int idx;

        /** The value of the 'first' parameter of the {@link #testSuppressionsInternal} method. */
        private final boolean first;

        /** Is this the inner exception? */
        private final boolean inner;

        /**
         * Counter to use to generate incremental integer values, to use in error messages, to make them
         * distinguishable. Used to check that exceptions are reported in the correct order.
         */
        private final AtomicInteger counter;

        /**
         * Constructor for the {@link MyResource} class.
         *
         * @param idx Resource index.
         * @param first The value of the 'first' parameter of the {@link #testSuppressionsInternal} method.
         * @param inner Is this the inner exception?
         * @param counter Counter to use to generate incremental integer values, to use in error messages, to make them
         *     distinguishable. Used to check that exceptions are reported in the correct order.
         */
        public MyResource(int idx, boolean first, boolean inner, AtomicInteger counter) {
            this.idx = idx;
            this.first = first;
            this.inner = inner;
            this.counter = counter;
        }

        @Override
        public void close() {
            int cnt = counter.incrementAndGet();
            String pattern = fmt("Close %%s %d %s %s #%d", idx, (first ? "first" : "second"),
                    (inner ? "inner" : "outer"), cnt);
            ApplicationException cause = new ApplicationException(fmt(pattern, "cause"));
            ApplicationException ex = new ApplicationException(fmt(pattern, "failure"), cause);
            if (inner) {
                throw ex;
            } else {
                try (MyResource res = new MyResource(idx, first, true, counter)) {
                    throw ex;
                }
            }
        }
    }

    /**
     * Run actual application framework exception reporting test.
     *
     * @param runnable The application code to execute.
     * @param expected The exception error stream output.
     */
    private void test(final Runnable runnable, String expected) {
        // Create streams.
        AppStream outStream = new MemAppStream();
        AppStream errStream = new MemAppStream();
        errStream.setUnixNewLineBytes();
        AppStreams streams = new AppStreams(System.in, outStream, errStream);

        // Create application.
        Application<?> app = new Application<>(streams) {
            @Override
            protected OutputProvider<IOutputComponent> getProvider() {
                return new OutputProvider<>();
            }

            @Override
            protected int runInternal() {
                runnable.run();
                return 0;
            }

            @Override
            public String getAppName() {
                return "Dummy name";
            }

            @Override
            public String getAppDescription() {
                return "Dummy description.";
            }

            @Override
            protected OptionCategory getAllOptions() {
                OptionCategory generalOpts = getGeneralOptionCategory();
                OptionCategory options = new OptionCategory("Dummy Options", "All dummy options.", list(generalOpts),
                        list());
                return options;
            }
        };

        // Add application to application manager.
        AppManager.add(app, null);

        // Run application.
        app.runApplication(new String[] {"--gui=off"}, false);

        // Remove application from application manager.
        AppManager.remove(app);

        // Check error stream.
        assertEquals("", outStream.toString());
        assertEquals(expected, errStream.toString());
    }
}
