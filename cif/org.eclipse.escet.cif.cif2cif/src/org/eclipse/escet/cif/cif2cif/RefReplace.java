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

import static org.eclipse.escet.common.emf.EMFHelper.deepclone;

import java.util.Map;

import org.eclipse.escet.cif.metamodel.cif.ComponentDef;
import org.eclipse.escet.cif.metamodel.cif.Equation;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.expressions.AlgVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CompInstWrapExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CompParamWrapExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ConstantExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ContVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EnumLiteralExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.FunctionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.InputVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.CompInstWrapType;
import org.eclipse.escet.cif.metamodel.cif.types.CompParamWrapType;
import org.eclipse.escet.cif.metamodel.cif.types.EnumType;
import org.eclipse.escet.cif.metamodel.cif.types.TypeRef;
import org.eclipse.escet.cif.metamodel.java.CifWalker;
import org.eclipse.escet.common.emf.EMFHelper;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/**
 * In-place transformation that replaces reference types/expressions that refer to declarations by the given
 * types/expressions. Also allows for the replacement of the variable references of equations.
 *
 * <p>
 * Precondition: Specifications with component definitions/instantiations are currently not supported.
 * </p>
 *
 * <p>
 * This transformation is an internal transformation and can not be applied using the CIF to CIF transformer
 * application.
 * </p>
 */
public class RefReplace extends CifWalker implements CifToCifTransformation {
    /**
     * The replacement mapping, from declarations to the expressions by which they should be replaced, for reference
     * expressions.
     */
    private final Map<PositionObject, Expression> refExprReplacements;

    /**
     * The replacement mapping, from declarations to the types by which they should be replaced, for reference types.
     */
    private final Map<Declaration, CifType> refTypeReplacements;

    /**
     * The replacement mapping, from declarations to the declarations by which they should be replaced, for the left
     * hand sides of equations.
     */
    private final Map<Declaration, Declaration> equationReplacements;

    /** Has a replacement been performed, so far? */
    private boolean replaced;

    /**
     * Constructor for the {@link RefReplace} class.
     *
     * @param refExprReplacements The replacement mapping, from declarations to the expressions by which they should be
     *     replaced, for reference expressions.
     * @param refTypeReplacements The replacement mapping, from declarations to the types by which they should be
     *     replaced, for reference types.
     * @param equationReplacements The replacement mapping, from declarations to the declarations by which they should
     *     be replaced, for the left hand sides of equations.
     */
    public RefReplace(Map<PositionObject, Expression> refExprReplacements,
            Map<Declaration, CifType> refTypeReplacements, Map<Declaration, Declaration> equationReplacements)
    {
        this.refExprReplacements = refExprReplacements;
        this.refTypeReplacements = refTypeReplacements;
        this.equationReplacements = equationReplacements;
    }

    @Override
    public void transform(Specification spec) {
        walkSpecification(spec);
    }

    /**
     * Performs the in-place transformation.
     *
     * @param expr The CIF expression for which to perform the transformation. The expression is modified in-place. The
     *     expression itself may also be replaced as a whole.
     */
    public void transform(Expression expr) {
        walkExpression(expr);
    }

    /**
     * Performs the in-place transformation.
     *
     * @param type The CIF type for which to perform the transformation. The type is modified in-place. The type itself
     *     may also be replaced as a whole.
     */
    public void transform(CifType type) {
        walkCifType(type);
    }

    /**
     * Has a replacement been performed, so far?
     *
     * @return {@code true} if a replacement has been performed, {@code false} otherwise.
     */
    public boolean isReplaced() {
        return replaced;
    }

    /**************************************************************************
     * Actual replacements code.
     *************************************************************************/

    @Override
    protected void walkAlgVariableExpression(AlgVariableExpression expr) {
        // Get replacement. If no replacement, then no special treatment.
        Expression replacement = refExprReplacements.get(expr.getVariable());
        if (replacement == null) {
            super.walkAlgVariableExpression(expr);
            return;
        }

        // Make unique copy of replacement value, and perform replacement.
        replacement = deepclone(replacement);
        EMFHelper.updateParentContainment(expr, replacement);
        replaced = true;

        // Recursively transform the replacement value.
        walkExpression(replacement);
    }

    @Override
    protected void walkConstantExpression(ConstantExpression expr) {
        // Get replacement. If no replacement, then no special treatment.
        Expression replacement = refExprReplacements.get(expr.getConstant());
        if (replacement == null) {
            super.walkConstantExpression(expr);
            return;
        }

        // Make unique copy of replacement value, and perform replacement.
        replacement = deepclone(replacement);
        EMFHelper.updateParentContainment(expr, replacement);
        replaced = true;

        // Recursively transform the replacement value.
        walkExpression(replacement);
    }

    @Override
    protected void walkContVariableExpression(ContVariableExpression expr) {
        // Get replacement. If no replacement, then no special treatment.
        Expression replacement = refExprReplacements.get(expr.getVariable());
        if (replacement == null) {
            super.walkContVariableExpression(expr);
            return;
        }

        // Make unique copy of replacement value, and perform replacement.
        replacement = deepclone(replacement);
        EMFHelper.updateParentContainment(expr, replacement);
        replaced = true;

        // Recursively transform the replacement value.
        walkExpression(replacement);
    }

    @Override
    protected void walkDiscVariableExpression(DiscVariableExpression expr) {
        // Get replacement. If no replacement, then no special treatment.
        Expression replacement = refExprReplacements.get(expr.getVariable());
        if (replacement == null) {
            super.walkDiscVariableExpression(expr);
            return;
        }

        // Make unique copy of replacement value, and perform replacement.
        replacement = deepclone(replacement);
        EMFHelper.updateParentContainment(expr, replacement);
        replaced = true;

        // Recursively transform the replacement value.
        walkExpression(replacement);
    }

    @Override
    protected void walkEventExpression(EventExpression expr) {
        // Get replacement. If no replacement, then no special treatment.
        Expression replacement = refExprReplacements.get(expr.getEvent());
        if (replacement == null) {
            super.walkEventExpression(expr);
            return;
        }

        // Make unique copy of replacement value, and perform replacement.
        replacement = deepclone(replacement);
        EMFHelper.updateParentContainment(expr, replacement);
        replaced = true;

        // Recursively transform the replacement value.
        walkExpression(replacement);
    }

    @Override
    protected void walkFunctionExpression(FunctionExpression expr) {
        // Get replacement. If no replacement, then no special treatment.
        Expression replacement = refExprReplacements.get(expr.getFunction());
        if (replacement == null) {
            super.walkFunctionExpression(expr);
            return;
        }

        // Make unique copy of replacement value, and perform replacement.
        replacement = deepclone(replacement);
        EMFHelper.updateParentContainment(expr, replacement);
        replaced = true;

        // Recursively transform the replacement value.
        walkExpression(replacement);
    }

    @Override
    protected void walkInputVariableExpression(InputVariableExpression expr) {
        // Get replacement. If no replacement, then no special treatment.
        Expression replacement = refExprReplacements.get(expr.getVariable());
        if (replacement == null) {
            super.walkInputVariableExpression(expr);
            return;
        }

        // Make unique copy of replacement value, and perform replacement.
        replacement = deepclone(replacement);
        EMFHelper.updateParentContainment(expr, replacement);
        replaced = true;

        // Recursively transform the replacement value.
        walkExpression(replacement);
    }

    @Override
    protected void walkEnumLiteralExpression(EnumLiteralExpression expr) {
        // Get replacement. If no replacement, then no special treatment.
        Expression replacement = refExprReplacements.get(expr.getLiteral());
        if (replacement == null) {
            super.walkEnumLiteralExpression(expr);
            return;
        }

        // Make unique copy of replacement value, and perform replacement.
        replacement = deepclone(replacement);
        EMFHelper.updateParentContainment(expr, replacement);
        replaced = true;

        // Recursively transform the replacement value.
        walkExpression(replacement);
    }

    @Override
    protected void walkTypeRef(TypeRef type) {
        // Get replacement. If no replacement, then no special treatment.
        CifType replacement = refTypeReplacements.get(type.getType());
        if (replacement == null) {
            super.walkTypeRef(type);
            return;
        }

        // Make unique copy of replacement type, and perform replacement.
        replacement = deepclone(replacement);
        EMFHelper.updateParentContainment(type, replacement);
        replaced = true;

        // Recursively transform the replacement type.
        walkCifType(replacement);
    }

    @Override
    protected void walkEnumType(EnumType type) {
        // Get replacement. If no replacement, then no special treatment.
        CifType replacement = refTypeReplacements.get(type.getEnum());
        if (replacement == null) {
            super.walkEnumType(type);
            return;
        }

        // Make unique copy of replacement type, and perform replacement.
        replacement = deepclone(replacement);
        EMFHelper.updateParentContainment(type, replacement);
        replaced = true;

        // Recursively transform the replacement type.
        walkCifType(replacement);
    }

    @Override
    protected void preprocessEquation(Equation eqn) {
        // Get replacement. If no replacement, then no special treatment.
        Declaration replacement = equationReplacements.get(eqn.getVariable());
        if (replacement == null) {
            return;
        }

        // Perform replacement.
        eqn.setVariable(replacement);
        replaced = true;
    }

    /**************************************************************************
     * Pre-condition violation checks (unsupported checks).
     *************************************************************************/

    @Override
    protected void preprocessComponentDef(ComponentDef cdef) {
        throw new RuntimeException("Comp defs unsupported: " + cdef);
    }

    @Override
    protected void preprocessCompInstWrapExpression(CompInstWrapExpression expr) {
        throw new RuntimeException("Comp inst wrap expr unupported: " + expr);
    }

    @Override
    protected void preprocessCompInstWrapType(CompInstWrapType type) {
        throw new RuntimeException("Comp inst wrap type unupported: " + type);
    }

    @Override
    protected void preprocessCompParamWrapExpression(CompParamWrapExpression expr) {
        throw new RuntimeException("Comp param wrap expr unupported: " + expr);
    }

    @Override
    protected void preprocessCompParamWrapType(CompParamWrapType type) {
        throw new RuntimeException("Comp param wrap type unupported: " + type);
    }
}
