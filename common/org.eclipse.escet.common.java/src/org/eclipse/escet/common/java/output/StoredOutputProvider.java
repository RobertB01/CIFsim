//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.java.output;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Strings;

/**
 * Provider that provides debug, normal, warning and/or error output streams, and stores their output.
 *
 * <p>
 * <ol>
 * <li>Instantiate the class to create an output provider with storage. The provider can be queried afterwards to obtain
 * the stored output.</li>
 * <li>Create one or more output streams connected to the provider by calling {@link #getDebugOutput},
 * {@link #getNormalOutput}, {@link #getWarnOutput}, and/or {@link #getErrorOutput}. All created outputs share the same
 * storage. Output from all created streams is added in the order of producing it.</li>
 * <li>To get the stored output, invoke {@link #toString} on the provider.</li>
 * </ol>
 * </p>
 *
 * <p>
 * Other notes:
 * <ul>
 * <li>The output streams of {@link #getDebugOutput}, {@link #getNormalOutput}, {@link #getWarnOutput} and
 * {@link #getErrorOutput} are lazily created and saved. Asking for the same stream more than once will return the same
 * output stream object on every call. This ensures that indentation levels etc are shared.</li>
 * <li>To get separate storage of the output for a stream, create a provider dedicated for that stream.</li>
 * </ul>
 * </p>
 */
public class StoredOutputProvider implements WarnOutputProvider, ErrorOutputProvider, DebugNormalOutputProvider {
    /** Storage of the produced output. */
    private final StringBuilder stringStore = new StringBuilder();

    /** Whether debug output is enabled. */
    private final boolean isDebugEnabled;

    /** Whether normal output is enabled. */
    private final boolean isNormalEnabled;

    /** Whether warning output is enabled. */
    private final boolean isWarnEnabled;

    /** Number of spaces to insert for a single indent level. */
    private final int indentSize;

    /** Current total indentation level for both normal and debug output. */
    private int totalIndentLevel = 0;

    /** Cached indentation string, {@code null} means it must be recomputed. */
    private String curIndentText = "";

    /** Output stream for debug output. Is lazily constructed. */
    private DebugNormalOutput debugOutput = null;

    /** Output stream for normal output. Is lazily constructed. */
    private DebugNormalOutput normalOutput = null;

    /** Output stream for waning output. Is lazily constructed. */
    private WarnOutput warnOutput = null;

    /** Output stream for error output. Is lazily constructed. */
    private ErrorOutput errorOutput = null;

    /**
     * Constructor of the {@link StoredOutputProvider} class. Enables all streams. Uses 4 spaces for each indentation
     * level.
     */
    public StoredOutputProvider() {
        this(true, true, true, 4);
    }

    /**
     * Constructor of the {@link StoredOutputProvider} class. Uses 4 spaces for each indentation level.
     *
     * @param isDebugEnabled Whether debug output is enabled.
     * @param isNormalEnabled Whether normal output is enabled.
     * @param isWarnEnabled Whether warning output is enabled.
     */
    public StoredOutputProvider(boolean isDebugEnabled, boolean isNormalEnabled, boolean isWarnEnabled) {
        this(isDebugEnabled, isNormalEnabled, isWarnEnabled, 4);
    }

    /**
     * Constructor of the {@link StoredOutputProvider} class.
     *
     * @param isDebugEnabled Whether debug output is enabled.
     * @param isNormalEnabled Whether normal output is enabled.
     * @param isWarnEnabled Whether warning output is enabled.
     * @param indentSize Number of spaces to insert for a single indent level.
     */
    public StoredOutputProvider(boolean isDebugEnabled, boolean isNormalEnabled, boolean isWarnEnabled,
            int indentSize)
    {
        this.isDebugEnabled = isDebugEnabled;
        this.isNormalEnabled = isNormalEnabled;
        this.isWarnEnabled = isWarnEnabled;
        this.indentSize = indentSize;
    }

    @Override
    public DebugNormalOutput getDebugOutput(String linePrefix) {
        if (debugOutput == null) {
            debugOutput = makeDebugNormalOutput(isDebugEnabled, linePrefix);
        }
        return debugOutput;
    }

    @Override
    public DebugNormalOutput getNormalOutput(String linePrefix) {
        if (normalOutput == null) {
            normalOutput = makeDebugNormalOutput(isNormalEnabled, linePrefix);
        }
        return normalOutput;
    }

    /** Increment the indentation level by {@code 1}. */
    private void incTotalIndentLevel() {
        totalIndentLevel++;
        curIndentText = null;
    }

    /** Decrement the indentation level by {code 1}. */
    private void decTotalIndentLevel() {
        Assert.check(totalIndentLevel > 0);
        totalIndentLevel--;
        curIndentText = null;
    }

    /**
     * Get the text to use for indenting a line for the current indentation level.
     *
     * @return The text to use for indenting a line.
     */
    private String getIndentText() {
        // Update the indentation string if necessary.
        if (curIndentText == null) {
            curIndentText = Strings.spaces(totalIndentLevel * indentSize);
        }
        return curIndentText;
    }

    /**
     * Construct a debug or normal output stream.
     *
     * @param isEnabled Whether the stream is enabled.
     * @param linePrefix Prefix text added before a line. May be {@code null} to disable a prefix.
     * @return The constructed output stream.
     */
    private DebugNormalOutput makeDebugNormalOutput(boolean isEnabled, String linePrefix) {
        return new DebugNormalOutput() {
            /** Indentation level of this stream. */
            private int streamIndentLevel = 0;

            @Override
            public void line(String message) {
                // Do nothing if the stream is not enabled.
                if (!isEnabled()) {
                    return;
                }

                // Construct the output.
                String curIndentText = getIndentText();
                if (linePrefix != null && !linePrefix.isEmpty()) {
                    message = curIndentText + linePrefix + message + "\n";
                } else if (!message.isEmpty()) {
                    // Avoid creating an empty line with just indentation. Note that blank messages do get indented as
                    // technically it can be considered to be a message.
                    message = curIndentText + message + "\n";
                } else {
                    message = "\n";
                }
                stringStore.append(message);
            }

            @Override
            public boolean isEnabled() {
                return isEnabled;
            }

            @Override
            public void inc() {
                if (isEnabled()) {
                    streamIndentLevel++;
                    incTotalIndentLevel();
                }
            }

            @Override
            public void dec() {
                if (isEnabled()) {
                    Assert.check(streamIndentLevel > 0);
                    streamIndentLevel--;
                    decTotalIndentLevel();
                }
            }
        };
    }

    @Override
    public WarnOutput getWarnOutput(String linePrefix) {
        if (warnOutput == null) {
            warnOutput = new WarnOutput() {
                @Override
                public void line(String message) {
                    // Do nothing if the stream is not enabled.
                    if (!isEnabled()) {
                        return;
                    }

                    // Construct the output.
                    String curIndentText = getIndentText();
                    if (linePrefix != null && !linePrefix.isEmpty()) {
                        message = curIndentText + linePrefix + message + "\n";
                    } else if (!message.isEmpty()) {
                        // Avoid creating an empty line with just indentation. Note that blank messages do get indented
                        // as technically it can be considered to be a message.
                        message = curIndentText + message + "\n";
                    } else {
                        message = "\n";
                    }
                    stringStore.append(message);
                }

                @Override
                public boolean isEnabled() {
                    return isWarnEnabled;
                }
            };
        }
        return warnOutput;
    }

    @Override
    public ErrorOutput getErrorOutput(String linePrefix) {
        if (errorOutput == null) {
            errorOutput = new ErrorOutput() {
                @Override
                public void line(String message) {
                    if (linePrefix != null && !linePrefix.isEmpty()) {
                        message = linePrefix + message + "\n";
                    } else {
                        message = message + "\n";
                    }
                    stringStore.append(message);
                }
            };
        }
        return errorOutput;
    }

    @Override
    public String toString() {
        return stringStore.toString();
    }
}
