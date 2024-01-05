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

package org.eclipse.escet.cif.typechecker.declwrap;

import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEquation;
import static org.eclipse.escet.cif.typechecker.CifExprsTypeChecker.REAL_TYPE_HINT;
import static org.eclipse.escet.cif.typechecker.CifExprsTypeChecker.transExpression;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.map;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.metamodel.cif.Equation;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.RealType;
import org.eclipse.escet.cif.parser.ast.ACifObject;
import org.eclipse.escet.cif.parser.ast.AEquation;
import org.eclipse.escet.cif.parser.ast.AEquationDecl;
import org.eclipse.escet.cif.parser.ast.automata.ALocation;
import org.eclipse.escet.cif.parser.ast.declarations.AContVariable;
import org.eclipse.escet.cif.parser.ast.expressions.AExpression;
import org.eclipse.escet.cif.typechecker.CheckStatus;
import org.eclipse.escet.cif.typechecker.CifTypeChecker;
import org.eclipse.escet.cif.typechecker.ErrMsg;
import org.eclipse.escet.cif.typechecker.scopes.ParentScope;
import org.eclipse.escet.common.java.Assert;

/** Continuous variable declaration wrapper. */
public class ContVariableDeclWrap extends DeclWrap<ContVariable> {
    /** The CIF AST representation of the continuous variable. */
    private final AContVariable astDecl;

    /**
     * Constructor for the {@link ContVariableDeclWrap} class.
     *
     * @param tchecker The CIF type checker to use.
     * @param scope The parent scope of this declaration.
     * @param astDecl The CIF AST representation of the continuous variable.
     * @param mmDecl The CIF metamodel representation of the continuous variable.
     */
    public ContVariableDeclWrap(CifTypeChecker tchecker, ParentScope<?> scope, AContVariable astDecl,
            ContVariable mmDecl)
    {
        super(tchecker, scope, mmDecl);
        this.astDecl = astDecl;
    }

    @Override
    public String getName() {
        return mmDecl.getName();
    }

    @Override
    public String getAbsName() {
        return CifTextUtils.getAbsName(mmDecl);
    }

    @Override
    public void tcheckForUseImpl() {
        // Check for reserved names.
        checkName();

        // Nothing else to check 'for use', as continuous variables and their
        // derivatives are always of type 'real', by definition.
        status = CheckStatus.USE;
    }

    @Override
    public void tcheckFull() {
        // First, check 'for use', and make sure we haven't checked it before.
        tcheckForUse();
        if (isCheckedFull()) {
            return;
        }

        // Determine the type of the initial value, if any.
        AExpression avalue = astDecl.value;
        if (avalue != null) {
            Expression value = transExpression(astDecl.value, REAL_TYPE_HINT, scope, null, tchecker);
            mmDecl.setValue(value);

            CifType vtype = value.getType();
            CifType nvtype = CifTypeUtils.normalizeType(vtype);

            // Check value type.
            if (!(nvtype instanceof RealType)) {
                tchecker.addProblem(ErrMsg.CONT_VAR_TYPE_VALUE_MISMATCH, astDecl.value.position,
                        CifTextUtils.typeToStr(vtype), getAbsName());
                // Non-fatal error.
            }
        }

        // Check derivatives.
        checkDerivatives();

        // This declaration is now fully checked.
        status = CheckStatus.FULL;
    }

    /** Perform type check on the derivatives. */
    private void checkDerivatives() {
        // Check derivative of the declaration itself.
        boolean inDecl = astDecl.derivative != null;
        if (inDecl) {
            Expression der = transExpression(astDecl.derivative, REAL_TYPE_HINT, scope, null, tchecker);
            mmDecl.setDerivative(der);

            CifType dtype = der.getType();
            CifType ndtype = CifTypeUtils.normalizeType(dtype);

            // Check derivative type.
            if (!(ndtype instanceof RealType)) {
                tchecker.addProblem(ErrMsg.CONT_VAR_DER_TYPE, astDecl.position, CifTextUtils.typeToStr(dtype),
                        getAbsName());
                // Non-fatal error.
            }
        }

        // Check the equations.
        List<AEquation> eqns = scope.astEquations.get(getName());
        List<AEquation> inComp = list();
        Map<ALocation, List<AEquation>> inLocs = map();
        if (eqns != null) {
            for (AEquation astEqn: eqns) {
                // Keep track of equation sources (component vs location).
                ACifObject parent = astEqn.parent;
                Assert.notNull(parent);

                if (parent instanceof AEquationDecl) {
                    inComp.add(astEqn);
                } else {
                    Assert.check(parent instanceof ALocation);

                    List<AEquation> prev = inLocs.get(parent);
                    if (prev == null) {
                        prev = listc(1);
                        inLocs.put((ALocation)parent, prev);
                    }
                    prev.add(astEqn);
                }

                // Construct new equation metamodel object, and store it.
                Equation eqn = newEquation();
                eqn.setPosition(astEqn.createPosition());
                eqn.setDerivative(astEqn.derivative);
                eqn.setVariable(mmDecl);

                scope.mmEquations.put(astEqn, eqn);

                // Must be a derivative equation.
                if (!astEqn.derivative) {
                    tchecker.addProblem(ErrMsg.EQN_CONT_NON_DER, astEqn.position, getAbsName());
                    // Non-fatal error.
                }

                // Check value.
                Expression value = transExpression(astEqn.value, REAL_TYPE_HINT, scope, null, tchecker);
                eqn.setValue(value);

                CifType dtype = value.getType();
                CifType ndtype = CifTypeUtils.normalizeType(dtype);

                // Check derivative type.
                if (!(ndtype instanceof RealType)) {
                    tchecker.addProblem(ErrMsg.CONT_VAR_DER_TYPE, astEqn.position, CifTextUtils.typeToStr(dtype),
                            getAbsName());
                    // Non-fatal error.
                }
            }
        }

        // Check for too many derivatives.
        if ((inDecl && !inComp.isEmpty()) || inComp.size() > 1) {
            // Both in declaration and in component equations or multiple times
            // in component equations.
            if (inDecl) {
                tchecker.addProblem(ErrMsg.DUPL_DER_FOR_CONT_VAR, astDecl.position, getAbsName());
            }
            for (AEquation eqn: inComp) {
                tchecker.addProblem(ErrMsg.DUPL_DER_FOR_CONT_VAR, eqn.position, getAbsName());
            }
            // Non-fatal error.
        }

        for (Entry<ALocation, List<AEquation>> entry: inLocs.entrySet()) {
            if (inDecl || !inComp.isEmpty() || entry.getValue().size() > 1) {
                // Both in declaration and in location equations, or both
                // in component equations and location equations, or multiple
                // times in location equations.
                if (inDecl) {
                    tchecker.addProblem(ErrMsg.DUPL_DER_FOR_CONT_VAR, astDecl.position, getAbsName());
                }
                for (AEquation eqn: inComp) {
                    tchecker.addProblem(ErrMsg.DUPL_DER_FOR_CONT_VAR, eqn.position, getAbsName());
                }
                for (AEquation eqn: entry.getValue()) {
                    tchecker.addProblem(ErrMsg.DUPL_DER_FOR_CONT_VAR, eqn.position, getAbsName());
                }
                // Non-fatal error.
            }
        }

        // Check for no derivatives.
        if (!inDecl && inComp.isEmpty() && inLocs.isEmpty()) {
            tchecker.addProblem(ErrMsg.CONT_VAR_NO_DER, astDecl.position, getAbsName());
            // Non-fatal error.
        }

        // Check for incomplete equations in locations.
        if (!inLocs.isEmpty()) {
            if (scope.getAstLocs().size() != inLocs.size()) {
                // Incomplete.
                for (ALocation loc: scope.getAstLocs()) {
                    if (!inLocs.containsKey(loc)) {
                        tchecker.addProblem(ErrMsg.CONT_VAR_NO_DER, loc.position, getAbsName());
                        // Non-fatal error.
                    }
                }
            }
        }
    }
}
