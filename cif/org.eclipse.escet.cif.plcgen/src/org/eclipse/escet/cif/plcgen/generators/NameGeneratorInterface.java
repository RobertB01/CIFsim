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

package org.eclipse.escet.cif.plcgen.generators;

import java.util.Map;

import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** Code generator interface for a {@link NameGenerator}. */
public interface NameGeneratorInterface {
    /**
     * Convert the given object to a proper name that does not clash with the PLC language or with previously generated
     * global names. This function should not be used for generating names that also may contain local generated names.
     *
     * @param posObject Named CIF object.
     * @return A proper name that does not clash with PLC language keywords or previously generated global names.
     */
    public abstract String generateGlobalName(PositionObject posObject);

    /**
     * Convert the given name to a proper name that does not clash with the PLC language or with previously generated
     * global names. This function should not be used for generating names that also may contain local generated names.
     *
     * @param initialName Suggested name to to use.
     * @param initialIsCifName Whether the initial name is known by the CIF user. Used to produce rename warnings. As
     *     producing such rename warnings for elements that have no name in CIF is meaningless to a user, this parameter
     *     should be {@code false} for those names.
     * @return A proper name that does not clash with PLC language keywords or previously generated global names.
     */
    public abstract String generateGlobalName(String initialName, boolean initialIsCifName);

    /**
     * Convert the given name to a proper name that does not clash with the PLC language, with previously generated
     * global names or with previously generated local names that used the same {@code localSuffixes} information.
     *
     * @param initialName Suggested name to to use.
     * @param localSuffixes Name suffix information of local names. Use the same map to generate all local names in a
     *     scope.
     * @return A proper name that does not clash with PLC language keywords, previously generated global names or local
     *     names created using this function with the same {@code localSuffixes} map.
     * @note Local names are assumed not to be used for representing CIF variables.
     */
    public abstract String generateLocalName(String initialName, Map<String, Integer> localSuffixes);
}
