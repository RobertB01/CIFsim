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

package org.eclipse.escet.cif.typechecker.declwrap;

import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEquation;
import static org.eclipse.escet.cif.typechecker.CifExprsTypeChecker.transExpression;
import static org.eclipse.escet.cif.typechecker.CifTypesTypeChecker.transCifType;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.map;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.RangeCompat;
import org.eclipse.escet.cif.metamodel.cif.Equation;
import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.cif.parser.ast.ACifObject;
import org.eclipse.escet.cif.parser.ast.AEquation;
import org.eclipse.escet.cif.parser.ast.AEquationDecl;
import org.eclipse.escet.cif.parser.ast.automata.ALocation;
import org.eclipse.escet.cif.parser.ast.declarations.AAlgVariable;
import org.eclipse.escet.cif.parser.ast.declarations.AAlgVariableDecl;
import org.eclipse.escet.cif.typechecker.CheckStatus;
import org.eclipse.escet.cif.typechecker.CifTypeChecker;
import org.eclipse.escet.cif.typechecker.ErrMsg;
import org.eclipse.escet.cif.typechecker.scopes.ParentScope;
import org.eclipse.escet.common.emf.EMFHelper;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.typechecker.SemanticException;

/** Algebraic variable declaration wrapper. */
public class AlgVariableDeclWrap extends DeclWrap<AlgVariable> {
    /** The CIF AST representation of the algebraic variables. */
    private final AAlgVariableDecl astDecls;

    /** The CIF AST representation of the algebraic variable. */
    private final AAlgVariable astDecl;

    /**
     * Constructor for the {@link AlgVariableDeclWrap} class.
     *
     * @param tchecker The CIF type checker to use.
     * @param scope The parent scope of this declaration.
     * @param astDecls The CIF AST representation of the algebraic variables.
     * @param astDecl The CIF AST representation of the algebraic variable.
     * @param mmDecl The CIF metamodel representation of the algebraic variable.
     */
    public AlgVariableDeclWrap(CifTypeChecker tchecker, ParentScope<?> scope, AAlgVariableDecl astDecls,
            AAlgVariable astDecl, AlgVariable mmDecl)
    {
        super(tchecker, scope, mmDecl);
        this.astDecls = astDecls;
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

        // Get the type of the declaration.
        tchecker.addToCycle(this);

        CifType type;
        try {
            type = transCifType(astDecls.type, scope, tchecker);
        } finally {
            tchecker.removeFromCycle(this);
        }

        CifType ntype = CifTypeUtils.normalizeType(type);

        // Check for allowed types.
        if (CifTypeUtils.hasComponentLikeType(type)) {
            tchecker.addProblem(ErrMsg.DECL_INVALID_TYPE, type.getPosition(), "Algebraic variable", getAbsName(),
                    CifTextUtils.typeToStr(type));
            throw new SemanticException();
        }

        // Set the type of the algebraic variable, and mark the variable as
        // ready 'for use':
        //
        // - Since we don't check algebraic variables for cycles while type
        // checking, we need to be able to use the type of the variable for
        // references to the variable, in case of such cycles, while
        // checking the value/equations of the algebraic variable.
        //
        // - We need to check the value while checking the algebraic variable
        // 'for use', as it may affect the bounds of the type of the
        // variable, if it is a rangeless integer type.
        //
        // - If we don't have a cycle, changing the type of the algebraic
        // variable, after we have checked the value/equations, is no
        // problem, since the variable is not yet referenced.
        //
        // - If there is a cycle, the variable is already referred to,
        // directly or indirectly in its value/equations. The rangeless
        // integer type is then used. However, since we have a cycle, we
        // can't possibly determine a narrower integer type (underspecified
        // specification), and the rangeless integer type is the best we can
        // derive.
        //
        // - If we have a cycle, the specification will be erroneous later on,
        // so the exact final type checking result does not matter.
        //
        // - The use of the rangeless integer type, which is wider than a
        // ranged integer type, may lead to typing errors. However, as
        // mentioned above, it is the best we can do. As such, such errors
        // are also the best we can do.
        mmDecl.setType(type);
        status = CheckStatus.USE;

        // Check value and equations.
        CifType vtype = checkValue(type);

        // Restrict the type of the algebraic variable, if the type of the
        // algebraic variable is a rangeless integer type, while the value
        // has a ranged integer type. That is, we derive the range from the
        // type of the value. We only do this for algebraic variables that
        // directly have an integer type.
        if (ntype instanceof IntType && CifTypeUtils.isRangeless((IntType)ntype)) {
            mmDecl.setType(EMFHelper.deepclone(vtype));
        }

        // This declaration is now fully checked.
        status = CheckStatus.FULL;
    }

    @Override
    public void tcheckFull() {
        // Since for algebraic variables with a rangeless integer type the
        // bounds can be determined from the value/equations, we need to
        // include the checking of the values/equations to determine the type
        // of the variable 'for use'. As such, the 'for use' checking already
        // fully checks the algebraic variable.
        tcheckForUse();
    }

    /**
     * Checks the value of the declaration itself, and all the equations for this algebraic variable.
     *
     * @param declType The type of the declaration of the algebraic variable.
     * @return The (merged) type of the value(s).
     */
    private CifType checkValue(CifType declType) {
        CifType type = null;

        // Check value of the declaration itself.
        boolean inDecl = astDecl.value != null;
        if (inDecl) {
            Expression value = transExpression(astDecl.value, declType, scope, null, tchecker);
            mmDecl.setValue(value);

            CifType vtype = value.getType();

            // Make sure types are compatible.
            if (!CifTypeUtils.checkTypeCompat(declType, vtype, RangeCompat.CONTAINED)) {
                tchecker.addProblem(ErrMsg.ALG_VAR_TYPE_VALUE_MISMATCH, astDecl.position, CifTextUtils.typeToStr(vtype),
                        getAbsName(), CifTextUtils.typeToStr(declType));
                throw new SemanticException();
            }

            type = EMFHelper.deepclone(vtype);
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
                eqn.setPosition(astEqn.position);
                eqn.setDerivative(astEqn.derivative);
                eqn.setVariable(mmDecl);

                scope.mmEquations.put(astEqn, eqn);

                // Must not be a derivative equation.
                if (astEqn.derivative) {
                    tchecker.addProblem(ErrMsg.EQN_ALG_DER, astEqn.position, getAbsName());
                    // Non-fatal error.
                }

                // Check value.
                Expression value = transExpression(astEqn.value, declType, scope, null, tchecker);
                eqn.setValue(value);

                // Make sure types are compatible (equation vs type of decl).
                CifType vtype = value.getType();
                if (!CifTypeUtils.checkTypeCompat(declType, vtype, RangeCompat.CONTAINED)) {
                    tchecker.addProblem(ErrMsg.ALG_VAR_TYPE_VALUE_MISMATCH, astEqn.position,
                            CifTextUtils.typeToStr(vtype), getAbsName(), CifTextUtils.typeToStr(declType));
                    throw new SemanticException();
                }

                // Merge types. Since the types are all compatible with the
                // type of the declaration, they are also compatible amongst
                // each other.
                if (type == null) {
                    type = vtype;
                } else {
                    type = CifTypeUtils.mergeTypes(type, vtype);
                }
            }
        }

        // Check for too many values.
        if ((inDecl && !inComp.isEmpty()) || inComp.size() > 1) {
            // Both in declaration and in component equations or multiple times
            // in component equations.
            if (inDecl) {
                tchecker.addProblem(ErrMsg.DUPL_VALUE_FOR_ALG_VAR, astDecl.position, getAbsName());
            }
            for (AEquation eqn: inComp) {
                tchecker.addProblem(ErrMsg.DUPL_VALUE_FOR_ALG_VAR, eqn.position, getAbsName());
            }
            // Non-fatal error.
        }

        for (Entry<ALocation, List<AEquation>> entry: inLocs.entrySet()) {
            if (inDecl || !inComp.isEmpty() || entry.getValue().size() > 1) {
                // Both in declaration and in location equations, or both
                // in component equations and location equations, or multiple
                // times in location equations.
                if (inDecl) {
                    tchecker.addProblem(ErrMsg.DUPL_VALUE_FOR_ALG_VAR, astDecl.position, getAbsName());
                }
                for (AEquation eqn: inComp) {
                    tchecker.addProblem(ErrMsg.DUPL_VALUE_FOR_ALG_VAR, eqn.position, getAbsName());
                }
                for (AEquation eqn: entry.getValue()) {
                    tchecker.addProblem(ErrMsg.DUPL_VALUE_FOR_ALG_VAR, eqn.position, getAbsName());
                }
                // Non-fatal error.
            }
        }

        // Check for no value.
        if (!inDecl && inComp.isEmpty() && inLocs.isEmpty()) {
            // Fatal error, because no value also means no type.
            tchecker.addProblem(ErrMsg.ALG_VAR_NO_VALUE, astDecl.position, getAbsName());
            throw new SemanticException();
        }

        // Check for incomplete equations in locations.
        if (!inLocs.isEmpty()) {
            if (scope.getAstLocs().size() != inLocs.size()) {
                // Incomplete.
                for (ALocation loc: scope.getAstLocs()) {
                    if (!inLocs.containsKey(loc)) {
                        tchecker.addProblem(ErrMsg.ALG_VAR_NO_VALUE, loc.position, getAbsName());
                        // Non-fatal error.
                    }
                }
            }
        }

        // Return the (merged) type of the value(s).
        Assert.notNull(type);
        return type;
    }
}
