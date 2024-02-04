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

package org.eclipse.escet.cif.codegen.simulink;

import static org.eclipse.escet.cif.common.CifTextUtils.getAbsName;
import static org.eclipse.escet.cif.common.CifTextUtils.typeToStr;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.warn;
import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.cif.codegen.CodeGenPreChecker;
import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;

/** Checker to check that the given CIF specification can be used for code generation with Simulink. */
public class SimulinkCodeGenPreChecker extends CodeGenPreChecker {
    @Override
    protected void walkInputVariable(InputVariable var) {
        if (!isGoodType(var.getType())) {
            String msg = fmt("Unsupported type \"%s\" found with input variable \"%s\".", typeToStr(var.getType()),
                    getAbsName(var));
            problems.add(msg);
            return;
        }
        super.walkInputVariable(var);
    }

    @Override
    protected void walkDiscVariable(DiscVariable var) {
        // A bad type will drop the discrete variable from the output.
        if (!isGoodType(var.getType())) {
            String msg = fmt("Unsupported output type \"%s\" found in discrete variable \"%s\", "
                    + "variable will be omitted from the output.", typeToStr(var.getType()), getAbsName(var));
            warn(msg);
        }

        super.walkDiscVariable(var);
    }

    @Override
    protected void walkAlgVariable(AlgVariable var) {
        // A bad type will drop the algebraic variable from the output.
        if (!isGoodType(var.getType())) {
            String msg = fmt("Unsupported output type \"%s\" found in algebraic variable \"%s\", "
                    + "variable will be omitted from the output.", typeToStr(var.getType()), getAbsName(var));
            warn(msg);
        }

        super.walkAlgVariable(var);
    }
}
