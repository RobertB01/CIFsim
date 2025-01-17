//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEnumDecl;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEnumLiteral;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.warn;
import static org.eclipse.escet.common.java.Lists.last;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.sortedstrings;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.escet.cif.common.CifScopeUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumLiteral;
import org.eclipse.escet.cif.metamodel.cif.expressions.EnumLiteralExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SwitchCase;
import org.eclipse.escet.cif.metamodel.cif.expressions.SwitchExpression;
import org.eclipse.escet.cif.metamodel.cif.types.EnumType;
import org.eclipse.escet.cif.metamodel.java.CifWalker;

/**
 * In-place transformation that merges all enumerations in the specification together to a single enumeration.
 *
 * <p>
 * Precondition: Specifications with component definitions/instantiations are currently not supported.
 * </p>
 *
 * <p>
 * As preprocessing, default initial values are added using the {@link AddDefaultInitialValues} CIF to CIF
 * transformation. This ensures that the initial values of for instance discrete variables, don't get changed.
 * </p>
 *
 * <p>
 * If enumeration literals are renamed, this may influence value equality for {@link CifTypeUtils#areEnumsCompatible
 * compatible} enumerations. However, since the resulting specification has at most one enumeration, there are no
 * multiple enumerations, and compatibility is thus not an issue.
 * </p>
 *
 * <p>
 * The annotations of all enumerations are lost. The annotations of the enumeration literals are all added to the
 * literals of the merged enumeration.
 * </p>
 *
 * <p>
 * All switch expressions that switch over a value that has a type that includes an enumeration type, but do not have an
 * 'else', may become incomplete by merging enumerations. Therefore, for any such switch expression, its last 'case' is
 * converted to an 'else'. The key of that last 'case' is thereby removed from the model.
 * </p>
 *
 * @see EnumsToInts
 * @see EnumsToConsts
 */
public class MergeEnums extends CifWalker implements CifToCifTransformation {
    /** The new merged enumeration declaration. Is modified in-place. */
    private final EnumDecl mergedEnum = newEnumDecl();

    /** Mapping from names to enumeration literals. Is modified in-place. */
    private final Map<String, EnumLiteral> nameToLitMap = map();

    @Override
    public void transform(Specification spec) {
        // Check no component definition/instantiation precondition.
        if (CifScopeUtils.hasCompDefInst(spec)) {
            String msg = "Merging enumerations for a CIF specification with component definitions is currently not "
                    + "supported.";
            throw new CifToCifPreconditionException(msg);
        }

        // Add default initial values, to ensure that the initial values of for
        // instance discrete variables, don't get changed.
        // The reasoning is as following: We use representative values for
        // compatible enumerations, but don't change the default value
        // accordingly, of e.g. discrete variables that have an enumeration
        // type. The transformation result would become invalid. This can be
        // solved by setting the default initial value for discrete variables,
        // and local variables of functions, to the actual value, instead of
        // leaving the default implicit. We only need to do this for variables
        // that have a data type that includes an enumeration, for which that
        // enumeration is not the one in the final result, but instead a
        // representative determines the final order of the literals. In fact,
        // we only need to do this if the representative and the actual
        // enumeration have a different first literal. Here, we simply use
        // a preprocessing transformation to ensure proper result of this
        // transformation.
        new AddDefaultInitialValues().transform(spec);

        // Remove all enumerations, create new literals, and update all
        // references to enumerations and their literals.
        walkSpecification(spec);

        // If we don't have enumerations, we are done.
        if (nameToLitMap.isEmpty()) {
            return;
        }

        // Get names already in use. Since we have no component
        // definition/instantiation, we only need a unique name (for the
        // enumeration declaration and its literals) w.r.t. the declarations
        // in the specification itself.
        Set<String> names = CifScopeUtils.getSymbolNamesForScope(spec, null);

        // Add literals, in sorted order. Also rename if not unique.
        Set<String> litNameSet = nameToLitMap.keySet();
        List<String> litNames = sortedstrings(litNameSet);
        List<EnumLiteral> lits = mergedEnum.getLiterals();
        for (String litName: litNames) {
            // Add literal.
            EnumLiteral lit = nameToLitMap.get(litName);
            lits.add(lit);

            // Rename if necessary.
            String name = lit.getName();
            if (names.contains(name)) {
                String oldName = name;
                name = CifScopeUtils.getUniqueName(name, names, litNameSet);
                lit.setName(name);
                warn("Enumeration literal \"%s\" is renamed to \"%s\".", oldName, name);
            }
            names.add(name);
        }

        // Set enumeration declaration name.
        String name = "E";
        if (names.contains(name)) {
            String oldName = name;
            name = CifScopeUtils.getUniqueName(name, names, Collections.emptySet());
            warn("Merged enumeration \"%s\" is renamed to \"%s\".", oldName, name);
        }
        mergedEnum.setName(name);

        // Add enumeration declaration to the specification.
        spec.getDeclarations().add(mergedEnum);
    }

    @Override
    protected void preprocessComplexComponent(ComplexComponent comp) {
        // Remove enumeration declarations, and add literals to new
        // enumeration declaration.
        List<Declaration> decls = comp.getDeclarations();
        Iterator<Declaration> declIter = decls.iterator();
        while (declIter.hasNext()) {
            Declaration decl = declIter.next();
            if (decl instanceof EnumDecl) {
                // Add literals.
                EnumDecl enumDecl = (EnumDecl)decl;
                for (EnumLiteral lit: enumDecl.getLiterals()) {
                    getLiteral(lit);
                }

                // Remove enumeration declaration.
                declIter.remove();
            }
        }
    }

    /**
     * Get the new enumeration literal for an original enumeration literal. If one has not yet been created before, it
     * will be created. This is based on the name of the literal: only one new enumeration literal is created for any
     * number of original enumeration literals with the same name.
     *
     * <p>
     * The annotations of all original enumeration literals are moved to the new enumeration literals. Each new
     * enumeration literal thus gets the annotations of all original enumeration literals for which it is created.
     * </p>
     *
     * <p>
     * All new enumeration literals are added to {@link #nameToLitMap}.
     * </p>
     *
     * @param origLiteral The original enumeration literal.
     * @return The new enumeration literal.
     */
    private EnumLiteral getLiteral(EnumLiteral origLiteral) {
        String name = origLiteral.getName();
        EnumLiteral newLiteral = nameToLitMap.get(name);
        if (newLiteral == null) {
            newLiteral = newEnumLiteral();
            newLiteral.setName(name);
            nameToLitMap.put(name, newLiteral);
        }
        newLiteral.getAnnotations().addAll(origLiteral.getAnnotations());
        return newLiteral;
    }

    @Override
    protected void preprocessEnumLiteralExpression(EnumLiteralExpression litRef) {
        // Replace literal reference.
        litRef.setLiteral(getLiteral(litRef.getLiteral()));
    }

    @Override
    protected void walkEnumType(EnumType enumType) {
        // Replace enumeration declaration reference.
        enumType.setEnum(mergedEnum);
    }

    @Override
    protected void postprocessSwitchExpression(SwitchExpression switchExpr) {
        // Make switch expression complete by changing last 'case' into an 'else', in case it may have become
        // incomplete. Only switch expressions with a value type that includes an enumeration type may have become
        // incomplete.
        if (CifTypeUtils.hasEnumType(switchExpr.getValue().getType())) {
            SwitchCase lastCase = last(switchExpr.getCases());
            if (lastCase.getKey() != null) {
                lastCase.setKey(null);
            }
        }
    }
}
