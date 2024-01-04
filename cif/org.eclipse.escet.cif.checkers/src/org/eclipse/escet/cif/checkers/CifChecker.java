//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.checkers;

import java.util.Arrays;
import java.util.List;

import org.eclipse.escet.cif.common.CifScopeUtils;
import org.eclipse.escet.cif.io.CifReader;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.java.CompositeCifWithArgWalker;
import org.eclipse.escet.cif.prettyprinter.CifPrettyPrinter;
import org.eclipse.escet.common.box.Box;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.exceptions.InvalidInputException;
import org.eclipse.escet.setext.runtime.exceptions.SyntaxException;

/** CIF checker. Checks whether a given CIF specification satisfies certain {@link CifCheck conditions}. */
public class CifChecker extends CompositeCifWithArgWalker<CifCheckViolations> {
    /** Whether all checks can handle component definitions and instantiations properly. */
    private final boolean supportCompDefInst;

    /**
     * Constructor for the {@link CifChecker} class.
     *
     * @param conditions The conditions to check.
     */
    public CifChecker(List<CifCheck> conditions) {
        this(conditions.toArray(n -> new CifCheck[n]));
    }

    /**
     * Constructor for the {@link CifChecker} class.
     *
     * @param conditions The conditions to check.
     */
    public CifChecker(CifCheck... conditions) {
        super(conditions);
        supportCompDefInst = Arrays.stream(conditions).allMatch(chk -> chk.supportsCompDefInst());
    }

    /**
     * Check whether a given CIF specification satisfies the given conditions.
     *
     * @param spec The CIF specification to check.
     * @param absSpecPath The absolute local file system path to the CIF file to check.
     * @return The violations.
     */
    public CifCheckViolations check(Specification spec, String absSpecPath) {
        // Check input.
        Assert.check(supportCompDefInst || !CifScopeUtils.hasCompDefInst(spec), "At least one check does not support "
                + "comp def/inst while the specification contains such a language construct.");

        // Ensure full position information is available, by pretty printing the specification, and parsing and type
        // checking it again.
        Box box = CifPrettyPrinter.boxSpec(spec);
        CifReader reader = new CifReader();
        reader.init("<in-memory-cif-spec>", absSpecPath, false);
        reader.suppressWarnings = true;
        Specification newSpec;
        try {
            newSpec = reader.read(box.toString());
        } catch (SyntaxException | InvalidInputException e) {
            throw new RuntimeException("Specification became invalid.", e);
        }

        // Check for violations, and report them back.
        CifCheckViolations violations = new CifCheckViolations(box.getLines());
        walkSpecification(newSpec, violations);
        return violations;
    }
}
