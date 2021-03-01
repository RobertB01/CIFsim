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

package org.eclipse.escet.chi.runtime;

import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.chi.runtime.data.BaseProcess;
import org.eclipse.escet.chi.runtime.data.io.ChiFileHandle;

/** Base class for a Chi specification. */
public abstract class ChiSpecification {
    /** Class describing a formal parameter of a model. */
    public class ParameterDescription {
        /** Description of the type of the parameter. */
        public final String typeText;

        /** Name of the parameter. */
        public final String parameterName;

        /**
         * Constructor of the {@link ParameterDescription} class.
         *
         * @param typeText Type description of the parameter.
         * @param parameterName Name of the parameter.
         */
        public ParameterDescription(String typeText, String parameterName) {
            this.typeText = typeText;
            this.parameterName = parameterName;
        }
    }

    /** Class describing a model or experiment that can be instantiated. */
    public class StartupDescription {
        /** Name of the model or experiment. */
        public final String name;

        /** Whether this startup description describes a model instance. */
        public final boolean isModel;

        /** Parameters of the model or experiment. */
        public final ParameterDescription[] parameters;

        /**
         * Constructor for the {@link StartupDescription} class.
         *
         * @param name Name of the model or experiment.
         * @param isModel Whether the provided name starts a model.
         * @param parameters Parameters of the model or experiment.
         */
        public StartupDescription(String name, boolean isModel, ParameterDescription[] parameters) {
            this.name = name;
            this.isModel = isModel;
            this.parameters = parameters;
        }
    }

    /**
     * Get the descriptions of the models that the Chi specification provides.
     *
     * @return The model descriptions of the specification.
     */
    public abstract StartupDescription[] getStartups();

    /**
     * Construct a model or experiment instance.
     *
     * @param coordinator Coordinator object of the simulation.
     * @param start Start text of the model or experiment to instantiate.
     * @return The model or experiment instance if it can be created, else {@code null}.
     */
    public abstract BaseProcess startStartup(ChiCoordinator coordinator, String start);

    /**
     * Find a default model or experiment instantiation to run.
     *
     * <p>
     * From the supplied startup definitions, check for a single parameter-less definition to instantiate. When a
     * parameter-less experiment exists, ignore all model instances.
     * </p>
     *
     * @return If a single parameter-less definition exists (while preferring experiments), returns the text to
     *     instantiate the definition. Returns {@code null} otherwise.
     */
    public String findDefaultStartupInstance() {
        StartupDescription modelStartup = null;
        boolean incorrectDefaultModel = false;
        StartupDescription xperStartup = null;
        for (StartupDescription md: getStartups()) {
            if (md.parameters.length > 0) {
                continue;
            }

            if (md.isModel) {
                // For models, register the first parameter-less model, or flag
                // model instantiation as infeasible.
                if (modelStartup == null) {
                    modelStartup = md;
                    continue;
                }
                incorrectDefaultModel = true;
                continue;
            } else {
                // For experiments, register the first parameter-less
                // experiment, or give up entirely.
                if (xperStartup == null) {
                    xperStartup = md;
                    continue;
                }
                return null;
            }
        }

        if (xperStartup != null) {
            return xperStartup.name + "()";
        }
        if (!incorrectDefaultModel && modelStartup != null) {
            return modelStartup.name + "()";
        }
        return null;
    }

    /**
     * After resetting the stream, attempt to recognize a model or experiment instantiation at the input of the form
     * {@code "<modelOrXperName>("}.
     *
     * <p>
     * Since this function may be called several times for different model or experiment names, it must not modify the
     * marker.
     * </p>
     *
     * @param handle Input file handle for reading the model or experiment instantiation text.
     * @param name Name of the model or experiment instantiation to recognize.
     * @return Whether a model or experiment instantiation of the given name has been recognized. If the instantiation
     *     is found, the stream is positioned at right after the opening parenthesis. Otherwise, the stream is
     *     positioned somewhere in or just after the name part.
     */
    public static boolean readStartupPrefix(ChiFileHandle handle, String name) {
        handle.resetStream();
        for (int i = 0; i < name.length(); i++) {
            if (handle.read() != name.charAt(i)) {
                return false;
            }
        }
        // Model or experiment name found, now find a '('. We also accept one
        // whitespace character at this point in case the name was a separate
        // argument at the command-line.
        int k = handle.read();
        if (k == ' ' || k == '\t' || k == '\n' || k == '\r') {
            k = handle.read();
        }

        return k == '(';
    }

    /**
     * Find a character at the input (after skipping white space).
     *
     * @param handle Input file handle.
     * @param code Character to find.
     * @throws ChiSimulatorException If the character was not found.
     */
    private static void findCharacter(ChiFileHandle handle, int code) {
        handle.skipWhitespace();
        int k = handle.read();
        if (k == code) {
            return;
        }
        String msg = fmt("Expected character '%c' at the input, but did not find it.", code);
        throw new ChiSimulatorException(msg);
    }

    /**
     * Read a value separator {@code ','} from the input file.
     *
     * @param handle Input file handle.
     * @throws ChiSimulatorException If no separator found.
     */
    public static void readValueSeparator(ChiFileHandle handle) {
        findCharacter(handle, ',');
    }

    /**
     * Read a closing parenthesis {@code ')'} from the input file.
     *
     * @param handle Input file handle.
     * @throws ChiSimulatorException If no closing parenthesis found.
     */
    public static void readStartupSuffix(ChiFileHandle handle) {
        findCharacter(handle, ')');
    }
}
