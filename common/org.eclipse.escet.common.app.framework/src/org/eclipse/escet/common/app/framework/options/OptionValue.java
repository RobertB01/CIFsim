//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

/**
 * A value for an {@link Option}.
 *
 * <p>
 * Unlike options, which must be singletons, option values may have many instances, even for a single option.
 * </p>
 *
 * @param <T> The type of the data value for the option.
 */
public class OptionValue<T> {
    /** The option for which the value is provided. */
    private final Option<T> option;

    /** The data value that is provided for the option. */
    private final T value;

    /**
     * Constructor for the {@link OptionValue} class.
     *
     * @param option The option for which the value is provided.
     * @param value The data value that is provided for the option.
     */
    public OptionValue(Option<T> option, T value) {
        this.option = option;
        this.value = value;
    }

    /**
     * Returns the option for which the value is provided.
     *
     * @return The option for which the value is provided.
     */
    public Option<T> getOption() {
        return option;
    }

    /**
     * Returns the data value that is provided for the option.
     *
     * @return The data value that is provided for the option.
     */
    public T getValue() {
        return value;
    }
}
