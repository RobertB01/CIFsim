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

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Sets.sortedgeneric;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.escet.common.app.framework.AppEnv;
import org.eclipse.escet.common.java.Assert;

/** Option category. An option category may contain options, as well as other option categories (sub-categories). */
public class OptionCategory {
    /** The name of the option category. This may be displayed in help messages, and user interfaces. */
    private final String name;

    /** The description of the option category. This may be displayed in help messages, and user interfaces. */
    private final String description;

    /**
     * The sub-categories that are part of this option category, not including any sub-categories of the sub-categories.
     */
    private final List<OptionCategory> categories;

    /** The options that are part of this option category, not including any options from sub-categories. */
    @SuppressWarnings("rawtypes")
    private final List<Option> options;

    /**
     * The parent category of this option category. May be {@code null} if this is a wrapper category. Is equal to
     * {@code null} until the category is added to a parent category.
     */
    private OptionCategory parent = null;

    /**
     * Constructor for the {@link OptionCategory} class.
     *
     * @param name The name of the option category. Ideally a single word for sub-categories.
     * @param description The description of the option category.
     * @param categories The sub-categories that are part of this option category, not including any sub-categories of
     *     the sub-categories.
     * @param options The options that are part of this option category, not including any options from sub-categories.
     */
    @SuppressWarnings("rawtypes")
    public OptionCategory(String name, String description, List<OptionCategory> categories, List<Option> options) {
        Assert.notNull(name);
        Assert.notNull(description);
        Assert.notNull(categories);
        Assert.notNull(options);
        this.name = name;
        this.description = description;
        this.categories = categories;
        this.options = options;
        for (OptionCategory category: categories) {
            category.setParent(this);
        }
    }

    /**
     * Sets the parent category.
     *
     * @param parent The parent category of this category.
     * @pre The parent category must not have been set yet.
     */
    private void setParent(OptionCategory parent) {
        Assert.check(this.parent == null);
        this.parent = parent;
    }

    /**
     * Returns the parent category of this option category. May be {@code null} if this is a wrapper category. Is equal
     * to {@code null} until the category is added to a parent category.
     *
     * @return The parent category of this option category, or {@code null}.
     */
    public OptionCategory getParent() {
        return parent;
    }

    /**
     * Returns the root category of this option category, which may be this category, or one of its ancestors. This
     * method assumes that the hierarchy is fully available.
     *
     * @return The root category of this option category.
     */
    public OptionCategory getRoot() {
        OptionCategory current = this;
        while (current.parent != null) {
            current = current.parent;
        }
        return current;
    }

    /**
     * Adds an option to this option category.
     *
     * @param option The option to add.
     */
    protected void addOption(Option<?> option) {
        options.add(option);
    }

    /**
     * Adds a sub-category to this option category.
     *
     * @param category The sub-category to add.
     */
    protected void addSubCategory(OptionCategory category) {
        categories.add(category);
    }

    /**
     * Returns the name of the option category. This may be displayed in help messages, and user interfaces.
     *
     * @return The name of the option category.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the full name of the option category, which includes the names of all parent categories, except for the
     * top level wrapper category. This may be displayed in help messages, and user interfaces.
     *
     * @return The full name of the option category.
     */
    public String getFullName() {
        List<String> names = list(getName());
        OptionCategory current = getParent();
        while (current != null) {
            OptionCategory parent = current.getParent();
            if (parent != null) {
                names.add(0, current.getName());
            }
            current = parent;
        }
        return String.join(", ", names);
    }

    /**
     * Returns the description of the option category. This may be displayed in help messages, and user interfaces.
     *
     * @return The description of the option category.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the options that are part of this option category, not including any options from sub-categories.
     *
     * @return The options that are part of this option category, not including any options from sub-categories.
     */
    @SuppressWarnings("rawtypes")
    public List<Option> getOptions() {
        return Collections.unmodifiableList(options);
    }

    /**
     * Returns the sub-categories that are part of this option category, not including any sub-categories of the
     * sub-categories.
     *
     * @return The sub-categories that are part of this option category, not including any sub-categories of the
     *     sub-categories.
     */
    public List<OptionCategory> getSubCategories() {
        return Collections.unmodifiableList(categories);
    }

    /**
     * Fills the given short option set recursively. Does not check for duplicates. Options that do not have a short
     * name are ignored.
     *
     * @param names The short option names. Is modified in-place.
     */
    public void fillShortOptSet(Set<Character> names) {
        for (OptionCategory cat: categories) {
            cat.fillShortOptSet(names);
        }

        for (Option<?> opt: options) {
            Character c = opt.getCmdShort();
            if (c == null) {
                continue;
            }
            names.add(c);
        }
    }

    /**
     * Fills the given short option mapping recursively. Also checks for unique short option names. Note that not all
     * options have a short name. Options that don't have a short name are omitted from the resulting mapping.
     *
     * @param map The short option mapping to fill.
     */
    public void fillShortOptMap(Map<Character, Option<?>> map) {
        // Fill for sub-categories.
        for (OptionCategory cat: categories) {
            cat.fillShortOptMap(map);
        }

        // Fill for options of this category.
        for (Option<?> opt: options) {
            // Add short name, if available, and not yet in use.
            Character c = opt.getCmdShort();
            if (c == null) {
                continue;
            }
            Option<?> opt2 = map.get(c);
            if (opt2 == null) {
                map.put(c, opt);
                continue;
            }

            // Get all short option names in use.
            Set<Character> shortNames = set();
            getRoot().fillShortOptSet(shortNames);

            // Inform developer of duplicate short name problem.
            String usedShortNames = sortedgeneric(shortNames).stream().map(String::valueOf)
                    .collect(Collectors.joining(", "));
            String msg = fmt(
                    "Duplicate short option name \"%s\" for application \"%s\" for options \"%s\" and \"%s\" "
                            + "(short names in use: %s).",
                    c, AppEnv.getApplication().getClass().getName(), opt.getClass().getName(),
                    opt2.getClass().getName(), usedShortNames);
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * Fills the given long option mapping recursively. Also checks for unique long option names. Note that all options
     * must have a long name. The mapping is thus complete.
     *
     * @param map The long option mapping to fill.
     */
    public void fillLongOptMap(Map<String, Option<?>> map) {
        // Fill for sub-categories.
        for (OptionCategory cat: categories) {
            cat.fillLongOptMap(map);
        }

        // Fill for options of this category.
        for (Option<?> opt: options) {
            // Add long name, if not yet in use.
            String s = opt.getCmdLong();
            Option<?> opt2 = map.get(s);
            if (opt2 == null) {
                map.put(s, opt);
                continue;
            }

            // Inform developer of duplicate long name problem.
            String msg = fmt("Duplicate long option name \"%s\" for application \"%s\" for options \"%s\" and \"%s\".",
                    s, AppEnv.getApplication().getClass().getName(), opt.getClass().getName(),
                    opt2.getClass().getName());
            throw new IllegalArgumentException(msg);
        }
    }
}
