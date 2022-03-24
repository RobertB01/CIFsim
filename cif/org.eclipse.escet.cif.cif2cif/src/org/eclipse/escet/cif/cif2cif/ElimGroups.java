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

package org.eclipse.escet.cif.cif2cif;

import static org.eclipse.escet.common.app.framework.output.OutputProvider.warn;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Sets.set;

import java.util.List;
import java.util.Set;

import org.eclipse.escet.cif.common.CifScopeUtils;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.Equation;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.IoDecl;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumLiteral;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Sets;

/**
 * In-place transformation that eliminates groups.
 *
 * <p>
 * Precondition: Specifications with component definitions/instantiations are currently not supported.
 * </p>
 *
 * <p>
 * As preprocessing, SVG file declarations are pushed into the other CIF/SVG declarations, using the
 * {@link SvgFileIntoDecls} CIF to CIF transformation. This ensures that the CIF/SVG declarations from the groups, which
 * are lifted to the specification, still have the proper SVG file declarations. Similarly, print file declaration are
 * pushed into print declarations, using the {@link PrintFileIntoDecls} CIF to CIF transformation.
 * </p>
 *
 * <p>
 * Since declarations and invariants of the groups are merged with the declarations and invariants of the specification,
 * renaming may be necessary to ensure uniquely named declarations and invariants.
 * </p>
 *
 * <p>
 * If enumeration literals are renamed, this may influence value equality for {@link CifTypeUtils#areEnumsCompatible
 * compatible} enumerations. As such, either use the {@link EnumsToInts} or {@link EnumsToConsts} transformation before
 * applying this transformation, or otherwise ensure that renaming does not result in an invalid specification. If this
 * transformation renames enumeration literals, a warning is printed.
 * </p>
 */
public class ElimGroups implements CifToCifTransformation {
    /** The groups to eliminate. */
    private final List<Group> groups = list();

    /** The automata to lift. */
    private final List<Automaton> automata = list();

    @Override
    public void transform(Specification spec) {
        // Check no component definition/instantiation precondition.
        if (CifScopeUtils.hasCompDefInst(spec)) {
            String msg = "Eliminating groups from a CIF specification with component definitions is currently not "
                    + "supported.";
            throw new CifToCifPreconditionException(msg);
        }

        // Push SVG file declarations into the other CIF/SVG declarations. This
        // ensures that the CIF/SVG declarations from the groups, which are
        // lifted to the specification, still have the proper SVG file
        // declarations.
        new SvgFileIntoDecls().transform(spec);

        // Push print file declarations into the print declarations. This
        // ensures that the print declarations from the groups, which are
        // lifted to the specification, still have the proper print file
        // declarations.
        new PrintFileIntoDecls().transform(spec);

        // Collect groups and automata.
        collect(spec);

        // Rename automata to absolute names, and collect lifted names.
        Set<String> liftedNames = set();
        for (Automaton automaton: automata) {
            String absName = CifTextUtils.getAbsName(automaton, false).replace('.', '_');
            automaton.setName(absName);
            liftedNames.add(absName);
        }

        // Rename declarations and invariants to absolute names. Keep
        // enumeration literal names. Meanwhile, collect lifted names.
        // Since we leave invariants in automata where they are, this won't
        // influence the implicit supervisory kinds of invariants.
        List<Declaration> liftedDecls = list();
        List<Invariant> liftedInvs = list();
        for (Group group: groups) {
            for (Declaration decl: group.getDeclarations()) {
                // Store declarations to lift.
                liftedDecls.add(decl);

                // Make name absolute, and add to lifted names.
                String absName = CifTextUtils.getAbsName(decl, false).replace('.', '_');
                decl.setName(absName);
                liftedNames.add(absName);
            }

            for (Invariant inv: group.getInvariants()) {
                // Store invariants to lift.
                liftedInvs.add(inv);

                // Make name absolute, and add to lifted names.
                if (inv.getName() != null) {
                    String absName = CifTextUtils.getAbsName(inv, false).replace('.', '_');
                    inv.setName(absName);
                    liftedNames.add(absName);
                }
            }
        }

        // Remove groups, lift automata, and get names of objects declared
        // in the specification (excluding any lifted objects).
        List<Component> components = spec.getComponents();
        components.clear();
        Set<String> specNames = CifScopeUtils.getSymbolNamesForScope(spec, null);
        components.addAll(automata);

        // Lift all initial and marker predicates, as well as equations.
        List<Expression> specInitials = spec.getInitials();
        List<Expression> specMarkeds = spec.getMarkeds();
        List<Equation> specEquations = spec.getEquations();
        for (Group group: groups) {
            specInitials.addAll(group.getInitials());
            specMarkeds.addAll(group.getMarkeds());
            specEquations.addAll(group.getEquations());
        }

        // Lift I/O declarations.
        List<IoDecl> specIoDecls = spec.getIoDecls();
        for (Group group: groups) {
            specIoDecls.addAll(group.getIoDecls());
        }

        // Lift all declarations.
        List<Declaration> specDecls = spec.getDeclarations();
        for (Declaration decl: liftedDecls) {
            // Lift.
            specDecls.add(decl);

            // Enumeration literals are lifted as well.
            if (decl instanceof EnumDecl) {
                for (EnumLiteral lit: ((EnumDecl)decl).getLiterals()) {
                    liftedNames.add(lit.getName());
                }
            }
        }

        // Lift all invariants.
        spec.getInvariants().addAll(liftedInvs);

        // Rename lifted automata, if needed.
        Set<String> usedNames = Sets.copy(specNames);

        for (Automaton automaton: automata) {
            if (usedNames.contains(automaton.getName())) {
                // Rename automaton.
                String oldName = automaton.getName();
                String newName = CifScopeUtils.getUniqueName(oldName, usedNames, liftedNames);
                automaton.setName(newName);
                warn("Automaton \"%s\" is renamed to \"%s\".", oldName, newName);
            }
            usedNames.add(automaton.getName());
        }

        // Rename lifted declarations and enumeration literals, if needed.
        for (Declaration decl: liftedDecls) {
            if (usedNames.contains(decl.getName())) {
                // Rename declaration.
                String oldName = decl.getName();
                String newName = CifScopeUtils.getUniqueName(oldName, usedNames, liftedNames);
                decl.setName(newName);
                warn("Declaration \"%s\" is renamed to \"%s\".", oldName, newName);
            }
            usedNames.add(decl.getName());

            if (decl instanceof EnumDecl) {
                for (EnumLiteral lit: ((EnumDecl)decl).getLiterals()) {
                    if (usedNames.contains(lit.getName())) {
                        // Rename enumeration literal.
                        String oldName = lit.getName();
                        String newName = CifScopeUtils.getUniqueName(oldName, usedNames, liftedNames);
                        lit.setName(newName);
                        warn("Enumeration literal \"%s\" is renamed to \"%s\". This may have changed the semantics of "
                                + "the specification. It may also cause the specification to be become invalid. It is "
                                + "highly recommended to avoid this renaming!", oldName, newName);
                    }
                    usedNames.add(lit.getName());
                }
            }
        }

        // Rename lifted invariants, if needed.
        for (Invariant inv: liftedInvs) {
            // Skip nameless invariants.
            if (inv.getName() == null) {
                continue;
            }

            if (usedNames.contains(inv.getName())) {
                // Rename invariant.
                String oldName = inv.getName();
                String newName = CifScopeUtils.getUniqueName(oldName, usedNames, liftedNames);
                inv.setName(newName);
                warn("Invariant \"%s\" is renamed to \"%s\".", oldName, newName);
            }
            usedNames.add(inv.getName());
        }
    }

    /**
     * Collects all the groups (excluding the specification) and automata in the given group.
     *
     * @param group The group in which to collect. May be a specification.
     * @see #groups
     * @see #automata
     */
    private void collect(Group group) {
        // Collect groups.
        if (!(group instanceof Specification)) {
            groups.add(group);
        }

        // Collect children.
        for (Component component: group.getComponents()) {
            // Collect automata.
            if (component instanceof Automaton) {
                automata.add((Automaton)component);
                continue;
            }

            // Child group.
            Assert.check(component instanceof Group);
            Group child = (Group)component;
            collect(child);
        }
    }
}
