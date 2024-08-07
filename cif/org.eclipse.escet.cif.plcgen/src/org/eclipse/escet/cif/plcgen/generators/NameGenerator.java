//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.plcgen.generators;

import java.util.Collection;
import java.util.Set;

import org.eclipse.escet.cif.plcgen.generators.names.NameScope;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** Interface for a name generator. */
public interface NameGenerator {
    /**
     * Convert the given object to a valid name in the global scope that does not clash with the PLC language or with
     * previously generated names. The returned name should be used as-is.
     *
     * @param posObject Named CIF object.
     * @return A valid name that does not clash with PLC language keywords or previously generated names. The returned
     *     name should be used as-is.
     * @see #generateGlobalNames(Set, PositionObject)
     */
    public abstract String generateGlobalName(PositionObject posObject);

    /**
     * Convert the given object to a valid suffix name in the global scope that does not clash with the PLC language or
     * with previously generated names. Only the given prefixes should be prepended to the returned name before use.
     *
     * @param prefixes Set of identifier prefixes, preferably with a trailing underscore. One of the provided prefixes
     *     must be prepended to the returned name to obtain a valid name to use in the PLC code.
     * @param posObject The named CIF object to be converted to a name.
     * @return A valid name that does not clash with PLC language keywords or previously generated names. The returned
     *     name should be prepended with one of the given prefixes before use.
     * @see #generateGlobalName(PositionObject)
     */
    public abstract String generateGlobalNames(Set<String> prefixes, PositionObject posObject);

    /**
     * Convert the given name to a valid name in the global scope that does not clash with the PLC language or with
     * previously generated names. The returned name should be used as-is.
     *
     * @param initialName Suggested name to use. The method may return a different name.
     * @param isCifName Whether the initial name is known by the CIF user. Used to produce rename warnings. As producing
     *     such rename warnings for elements that have no name in CIF is meaningless to a user, this parameter should be
     *     {@code false} for those names.
     * @return A valid name that does not clash with PLC language keywords or previously generated names. The returned
     *     name should be used as-is.
     * @see #generateGlobalNames(Set, String, boolean)
     */
    public abstract String generateGlobalName(String initialName, boolean isCifName);

    /**
     * Convert the given object to a valid suffix name in the global scope that does not clash with the PLC language or
     * with previously generated names. Only the given prefixes should be prepended to the returned name before use.
     *
     * @param prefixes Set of identifier prefixes, preferably with a trailing underscore. One of the provided prefixes
     *     must be prepended to the returned name to obtain a valid name to use in the PLC code.
     * @param initialName Suggested name to use. The method may return a different name.
     * @param isCifName Whether the initial name is known by the CIF user. Used to produce rename warnings. As producing
     *     such rename warnings for elements that have no name in CIF is meaningless to a user, this parameter should be
     *     {@code false} for those names.
     * @return A valid name that does not clash with PLC language keywords or previously generated names. The returned
     *     name should be prepended with one of the given prefixes before use.
     * @see #generateGlobalName(String, boolean)
     */
    public abstract String generateGlobalNames(Set<String> prefixes, String initialName, boolean isCifName);

    /**
     * Convert the given name to a valid name in the provided scope that does not clash with the PLC language or with
     * previously generated names. The returned name should be used as-is.
     *
     * @param initialName Suggested name to use. The method may return a different name.
     * @param localScope Scope that will use the generated name.
     * @return A valid name that does not clash with PLC language keywords or previously generated names. The returned
     *     name should be used as-is.
     * @see #generateLocalNames(Set, String, NameScope)
     */
    public abstract String generateLocalName(String initialName, NameScope localScope);

    /**
     * Convert the given object to a valid suffix name in the provided scope that does not clash with the PLC language
     * or with previously generated names. Only the given prefixes should be prepended to the returned name before use.
     *
     * @param prefixes Set of identifier prefixes, preferably with a trailing underscore. One of the provided prefixes
     *     must be prepended to the returned name to obtain a valid name to use in the PLC code.
     * @param initialName Suggested name to use. The method may return a different name.
     * @param localScope Scope that will use the generated names.
     * @return A valid name that does not clash with PLC language keywords or previously generated names. The returned
     *     name should be prepended with one of the given prefixes before use.
     * @see #generateLocalName(String, NameScope)
     */
    public abstract String generateLocalNames(Set<String> prefixes, String initialName, NameScope localScope);

    /**
     * Declare the names in the provided array as unavailable in the PLC code.
     *
     * <p>
     * Names must be added before the first name is generated.
     * </p>
     *
     * @param names Names to declare as unavailable in the PLC code.
     */
    public void addDisallowedNames(String[] names);

    /**
     * Declare the names in the provided collection as unavailable in the PLC code.
     *
     * <p>
     * Names must be added before the first name is generated.
     * </p>
     *
     * @param names Names to declare as unavailable in the PLC code.
     */
    public void addDisallowedNames(Collection<String> names);
}
