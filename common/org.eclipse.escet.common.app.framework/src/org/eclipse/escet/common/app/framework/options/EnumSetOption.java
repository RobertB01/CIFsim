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

import static org.eclipse.escet.common.app.framework.output.OutputProvider.warn;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Sets.setc;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.escet.common.app.framework.exceptions.InvalidOptionException;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

/**
 * Base class for all options with sets of enumerated literals as their values. That is, each enumeration literal can be
 * either included or excluded; it can be enabled or disabled.
 *
 * <p>
 * The textual representations of the values of this option use comma separated textual representations of the
 * enumeration literals, as described below. Whitespace around the representations of the literals is ignored, as are
 * empty textual representations of literals.
 * </p>
 *
 * <p>
 * The user may specify comma separated textual representations of the enumeration literals. If so, they indicate the
 * literals to include in the value of the option. However, if the default includes many literals, removing just one
 * literal requires specifying all the remaining ones. To make this easier, it is also possible to specify a difference
 * relative to the default. By including a plus symbol ({@code +}) as prefix, the literal is added, and by including a
 * minus symbol ({@code -}) as prefix, the literal is removed. Multiple additions and removals may be specified.
 * However, additions and removals may not be combined with non-prefixed literals.
 * </p>
 *
 * <p>
 * By default, the {@link #parseValue} method uses the names of the enumeration literals in the textual representation
 * of the values for this option, where the enumeration literals are assumed to have names consisting of upper case
 * letters and underscore symbols ({@code _}) only. In the textual representation of the values, any casing may be used,
 * and a dash ({@code -}) may be used instead of the underscore symbol ({@code _}).
 * </p>
 *
 * <p>
 * By default, the {@link #getCmdLine} method uses the names of the enumeration constants for the textual representation
 * of the values for this option, by converting the names to lowercase, and replacing underscore symbols ({@code _}) by
 * dashes ({@code -}).
 * </p>
 *
 * <p>
 * By default, the {@link #getDialogOrder} method returns the enumeration literals in the order that they are defined in
 * the enumeration, and this is the order in which the literals are displayed in the option dialog.
 * </p>
 *
 * @param <T> The enumeration that is used for the values of this option.
 */
public abstract class EnumSetOption<T extends Enum<T>> extends Option<EnumSet<T>> {
    /** The default value of the set of enumeration literals valued option. */
    protected final EnumSet<T> defaultValue;

    /** The class of the enumeration. */
    protected final Class<T> enumClass;

    /**
     * The description for this option, in the option dialog, or {@code null} if {@link #showInDialog} is {@code false}.
     */
    protected final String optDialogDescr;

    /**
     * Constructor for the {@link EnumSetOption} class. Don't directly create instances of derived classes. Use the
     * {@link Options#getInstance} method instead.
     *
     * @param name The name of the option. Example: {@code "Solver
     *      algorithms"}.
     * @param description The description of the option. Example: {@code
     *      "The algorithms to use for the solver. Specify comma separated
     *      names of the solvers. Specify \"abc\" (default) for the Abc solver,
     *      \"def\" for the Def solver, and/or \"ghi\" (default) for the Ghi
     *      solver. Prefix a name with \"+\" to add it on top of the defaults,
     *      or with \"-\" to remove it from the defaults."}.
     * @param cmdShort The short (one letter) option name, for command line processing. May be {@code null} if the
     *     option has no short name.
     * @param cmdLong The long option name (excluding the "--" prefix), for command line processing. Must not be
     *     {@code null}. Must not be "*".
     * @param cmdValue The name of the option value, for command line processing. Must not be {@code null}.
     * @param defaultValue The default value of the option.
     * @param showInDialog Whether to show the option in option dialogs.
     * @param optDialogDescr The description for this option, in the option dialog, or {@code null} if
     *     {@link #showInDialog} is {@code false}. Example: {@code "The algorithms to use for the solver."}.
     * @param enumClass The class of the enumeration.
     */
    public EnumSetOption(String name, String description, Character cmdShort, String cmdLong, String cmdValue,
            EnumSet<T> defaultValue, boolean showInDialog, String optDialogDescr, Class<T> enumClass)
    {
        super(name, description, cmdShort, cmdLong, cmdValue, showInDialog);
        Assert.check(!cmdLong.equals("*"));
        Assert.check(cmdValue != null);
        Assert.ifAndOnlyIf(showInDialog, optDialogDescr != null);
        this.defaultValue = defaultValue;
        this.enumClass = enumClass;
        this.optDialogDescr = optDialogDescr;
    }

    @Override
    public EnumSet<T> getDefault() {
        return defaultValue;
    }

    /**
     * Clear the option value, to an empty set of enumerated literals. The original option value is not modified
     * in-place. Instead, a fresh set is created. This prevents the default value set from being modified.
     */
    protected void clear() {
        Options.set(this, EnumSet.noneOf(enumClass));
    }

    @Override
    public EnumSet<T> parseValue(String optName, String valuesTxt) {
        // Split into separate value texts (for each literal).
        String[] valuesArray = StringUtils.split(valuesTxt, ',');

        // Trim values. Skip empty values.
        List<String> values = listc(valuesArray.length);
        for (String value: valuesArray) {
            value = value.trim();
            if (value.isEmpty()) {
                continue;
            }
            values.add(value);
        }

        // Convert each value text to a literal.
        List<String> names = listc(values.size());
        List<T> literals = listc(values.size());
        boolean[] additions = new boolean[values.size()];
        boolean[] removals = new boolean[values.size()];
        for (int i = 0; i < values.size(); i++) {
            // Get name.
            String name = values.get(i);

            // Check for additions/removals.
            if (name.startsWith("+")) {
                additions[i] = true;
                name = name.substring(1);
            } else if (name.startsWith("-")) {
                removals[i] = true;
                name = name.substring(1);
            }

            // Remember names.
            names.add(name);

            // Convert to upper case and normalize '-' vs '_'.
            String literal = name.toUpperCase(Locale.US).replace('-', '_');

            // Convert name to enumeration literal, and add it to the result.
            try {
                literals.add(Enum.valueOf(enumClass, literal));
            } catch (IllegalArgumentException e) {
                String msg = fmt("Unknown option value part: \"%s\".", name);
                throw new InvalidOptionException(msg);
            }
        }

        // Check for only replacements, or only additions/removals.
        boolean hasReplace = false;
        boolean hasAddRemove = false;
        for (int i = 0; i < literals.size(); i++) {
            if (additions[i] || removals[i]) {
                hasAddRemove = true;
            } else {
                hasReplace = true;
            }
        }
        if (hasReplace && hasAddRemove) {
            String msg = "Using both replacement values (no prefix) and addition/removal values (+/- prefix) "
                    + "is not allowed.";
            throw new InvalidOptionException(msg);
        }

        // Get option value.
        if (hasAddRemove) {
            // Additions/removals.
            EnumSet<T> rslt = getDefault().clone();
            for (int i = 0; i < literals.size(); i++) {
                T literal = literals.get(i);
                if (additions[i]) {
                    boolean added = rslt.add(literal);
                    if (!added) {
                        String msg = fmt(
                                "Adding value \"%s\" for option \"--%s\" has no effect, as the value is already "
                                        + "included in the default, or was added more than once.",
                                names.get(i), cmdLong);
                        warn(msg);
                    }
                } else {
                    Assert.check(removals[i]);
                    boolean removed = rslt.remove(literal);
                    if (!removed) {
                        String msg = fmt(
                                "Removing value \"%s\" for option \"--%s\" has no effect, as the value is not "
                                        + "included in the default, or was removed more than once.",
                                names.get(i), cmdLong);
                        warn(msg);
                    }
                }
            }
            return rslt;
        } else {
            // Replacements.
            EnumSet<T> rslt = EnumSet.noneOf(enumClass);
            for (int i = 0; i < literals.size(); i++) {
                T literal = literals.get(i);
                boolean added = rslt.add(literal);
                if (!added) {
                    String msg = fmt("Specifying value \"%s\" a second time for option \"--%s\" has no effect.",
                            names.get(i), cmdLong);
                    warn(msg);
                }
            }
            return rslt;
        }
    }

    @Override
    public String[] getCmdLine(Object value) {
        @SuppressWarnings("unchecked")
        EnumSet<T> literalSet = (EnumSet<T>)value;
        List<String> txts = listc(literalSet.size());
        for (T literal: literalSet) {
            txts.add(literal.name().toLowerCase(Locale.US).replace('_', '-'));
        }
        return new String[] {"--" + cmdLong + "=" + StringUtils.join(txts, ", ")};
    }

    /**
     * Returns the literals of the enumeration, in the order they are to be displayed in the option dialog.
     *
     * @return The literals of the enumeration, in the order they are to be displayed in the option dialog.
     */
    protected T[] getDialogOrder() {
        return enumClass.getEnumConstants();
    }

    /**
     * Checks whether the given enumeration literals cover all the possible literals of the enumeration, and whether
     * they don't contain duplicates.
     *
     * @param literals The enumeration literals to check.
     */
    protected void checkEnumValues(T[] literals) {
        // Make sure all literals are accounted for.
        for (T literal: enumClass.getEnumConstants()) {
            Assert.check(ArrayUtils.contains(literals, literal));
        }

        // Make sure we have no duplicates.
        Set<T> litSet = setc(literals.length);
        for (T lit: litSet) {
            boolean added = litSet.add(lit);
            Assert.check(added);
        }
    }

    /**
     * Returns the text to use for the radio button for the given enumeration literal, in the option dialog.
     *
     * <p>
     * For options that are not shown in the option dialog, just return a dummy value ({@code null} for instance).
     * </p>
     *
     * <p>
     * Example text: {@code "Abc solver"}.
     * </p>
     *
     * @param literal The enumeration literal.
     * @return The text to use for the radio button.
     */
    protected abstract String getDialogText(T literal);

    @Override
    public OptionGroup<EnumSet<T>> createOptionGroup(Composite page) {
        // Paranoia checking.
        Assert.check(showInDialog);

        // Construct option group.
        return new OptionGroup<>(page, this) {
            Button[] buttons;

            T[] literals;

            @Override
            protected void addComponents(Group group) {
                literals = getDialogOrder();
                checkEnumValues(literals);

                buttons = new Button[literals.length];

                for (int i = 0; i < literals.length; i++) {
                    buttons[i] = new Button(group, SWT.CHECK);
                    buttons[i].setText(getDialogText(literals[i]));
                }

                layoutGeneric(buttons);
            }

            @Override
            public String getDescription() {
                return optDialogDescr;
            }

            @Override
            public void setToValue(EnumSet<T> value) {
                for (T v: value) {
                    int idx = ArrayUtils.indexOf(literals, v);
                    buttons[idx].setSelection(true);
                }
            }

            @Override
            public String[] getCmdLine() {
                // Create enumeration set.
                EnumSet<T> values = EnumSet.noneOf(enumClass);
                for (int i = 0; i < buttons.length; i++) {
                    if (buttons[i].getSelection()) {
                        values.add(literals[i]);
                    }
                }

                // Return the command line.
                return EnumSetOption.this.getCmdLine(values);
            }
        };
    }
}
