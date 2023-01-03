//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBinaryExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBoolType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newElifExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newIfExpression;
import static org.eclipse.escet.common.emf.EMFHelper.deepclone;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.last;

import java.util.List;

import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.ElifExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IfExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SwitchCase;
import org.eclipse.escet.cif.metamodel.cif.expressions.SwitchExpression;
import org.eclipse.escet.cif.metamodel.java.CifWalker;
import org.eclipse.escet.common.emf.EMFHelper;

/** In-place transformation that converts 'switch' expressions to 'if' expressions. */
public class SwitchesToIfs extends CifWalker implements CifToCifTransformation {
    @Override
    public void transform(Specification spec) {
        walkSpecification(spec);
    }

    @Override
    protected void walkSwitchExpression(SwitchExpression switchExpr) {
        // Special case for single alternative. No need for an 'if' expression.
        // This makes use of the fact that 'switch' expressions must be
        // statically complete, and this is enforced by the type checker.
        List<SwitchCase> cases = switchExpr.getCases();
        if (cases.size() == 1) {
            EMFHelper.updateParentContainment(switchExpr, first(cases).getValue());
            return;
        }

        // Change 'switch' keys to 'if' guards.
        Expression value = switchExpr.getValue();
        boolean isAutRef = CifTypeUtils.isAutRefExpr(value);

        for (SwitchCase cse: switchExpr.getCases()) {
            // Get 'switch' key. Skip 'else'.
            Expression key = cse.getKey();
            if (key == null) {
                continue;
            }

            // Convert to 'if' guard.
            if (isAutRef) {
                // No need to convert. Key is already location reference
                // expression valid from the scope of the 'switch' expression.
                // It also already has a boolean type.
            } else {
                // Convert to 'switch_value == case_key'.
                BinaryExpression bexpr = newBinaryExpression();
                bexpr.setOperator(BinaryOperator.EQUAL);
                bexpr.setType(newBoolType());
                bexpr.setLeft(deepclone(value));
                bexpr.setRight(key);
                cse.setKey(bexpr);
            }
        }

        // Create 'if' expression.
        IfExpression ifExpr = newIfExpression();
        ifExpr.setType(switchExpr.getType());

        // Set 'then' for first case.
        ifExpr.getGuards().add(first(cases).getKey());
        ifExpr.setThen(first(cases).getValue());

        // Add 'elif' for all cases, except for first and last ones.
        List<ElifExpression> elifs = ifExpr.getElifs();
        for (int i = 1; i < cases.size() - 1; i++) {
            ElifExpression elifExpr = newElifExpression();
            elifs.add(elifExpr);

            SwitchCase cse = cases.get(i);
            elifExpr.getGuards().add(cse.getKey());
            elifExpr.setThen(cse.getValue());
        }

        // Set 'else' for last case. Ignore the 'key', if present.
        ifExpr.setElse(last(cases).getValue());

        // Replace expression.
        EMFHelper.updateParentContainment(switchExpr, ifExpr);
    }
}
