//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

import static java.util.Collections.EMPTY_SET;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newRealExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newRealType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newVariableValue;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.warn;
import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;
import java.util.Set;

import org.eclipse.escet.cif.common.CifScopeUtils;
import org.eclipse.escet.cif.common.CifValueUtils;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.VariableValue;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.RealExpression;
import org.eclipse.escet.cif.metamodel.cif.functions.FunctionParameter;
import org.eclipse.escet.cif.metamodel.cif.functions.InternalFunction;
import org.eclipse.escet.cif.metamodel.java.CifWalker;

/**
 * In-place transformation that adds the default initial values to variables that are not given an explicit initial
 * value. This includes discrete and continuous variables, as well as local variables of functions.
 *
 * <p>
 * Precondition: Specifications with component definitions/instantiations are currently not supported.
 * </p>
 *
 * <p>
 * This transformation may introduce new functions for the default values of function types.
 * </p>
 */
public class AddDefaultInitialValues extends CifWalker implements CifToCifTransformation {
    /** The newly created functions for default values of function types. */
    private List<InternalFunction> funcs = list();

    @Override
    public void transform(Specification spec) {
        // Check no component definition/instantiation precondition.
        if (CifScopeUtils.hasCompDefInst(spec)) {
            String msg = "Adding default initial values to variables for a CIF specification with component "
                    + "definitions is currently not supported.";
            throw new CifToCifPreconditionException(msg);
        }

        // Walk the specification.
        walkSpecification(spec);

        // Add the newly created functions.
        if (!funcs.isEmpty()) {
            // Get names already in use.
            Set<String> used = CifScopeUtils.getSymbolNamesForScope(spec, null);

            // Add all functions.
            for (InternalFunction func: funcs) {
                // Get unique name.
                String name = "_f";
                if (used.contains(name)) {
                    String oldName = name;
                    name = CifScopeUtils.getUniqueName(name, used, EMPTY_SET);
                    warn("Function \"%s\", introduced for the default value of a function type, is renamed to \"%s\".",
                            oldName, name);
                }

                // Set function name, and mark it as used.
                func.setName(name);
                used.add(name);

                // Add function.
                spec.getDeclarations().add(func);
            }
        }
    }

    @Override
    protected void preprocessDiscVariable(DiscVariable var) {
        // Skip function parameters.
        if (var.eContainer() instanceof FunctionParameter) {
            return;
        }

        // Skip variables that have one or more initial values already.
        if (var.getValue() != null) {
            return;
        }

        // Get default value for the variable.
        Expression defaultValue = CifValueUtils.getDefaultValue(var.getType(), funcs);

        // Set the default value.
        VariableValue vvalue = newVariableValue();
        vvalue.getValues().add(defaultValue);
        var.setValue(vvalue);
    }

    @Override
    protected void preprocessContVariable(ContVariable var) {
        if (var.getValue() == null) {
            RealExpression value = newRealExpression();
            value.setType(newRealType());
            value.setValue("0.0");
            var.setValue(value);
        }
    }
}
