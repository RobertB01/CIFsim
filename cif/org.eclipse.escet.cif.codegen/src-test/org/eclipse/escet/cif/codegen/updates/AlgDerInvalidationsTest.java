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

package org.eclipse.escet.cif.codegen.updates;

import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newAlgVariable;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newAlgVariableExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBinaryExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newConstant;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newConstantExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newContVariable;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newContVariableExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newDiscVariable;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newDiscVariableExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newInputVariable;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newInputVariableExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newIntExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newRealExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newTimeExpression;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Sets.setc;
import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Set;

import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Constant;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.common.java.Assert;
import org.junit.Test;

/** Unit tests for invalidation computations of algebraic and derivative expressions. */
public class AlgDerInvalidationsTest {
    /**
     * Glue expressions together.
     *
     * @param exprs Expressions to glue together.
     * @return A combined expression.
     */
    private Expression glue(Expression... exprs) {
        Expression result = newIntExpression(null, null, 5);
        for (Expression e: exprs) {
            result = newBinaryExpression(result, BinaryOperator.ADDITION, null, e, null);
        }
        return result;
    }

    /**
     * Construct expressions with the given variables.
     *
     * @param vars Variables to add.
     * @return Expression containing the given algebraic, constant, or discrete variables.
     */
    private Expression makeVariables(Declaration... vars) {
        Expression[] exprs = new Expression[vars.length];
        for (int i = 0; i < vars.length; i++) {
            Declaration v = vars[i];

            if (v instanceof AlgVariable) {
                exprs[i] = newAlgVariableExpression(null, null, (AlgVariable)v);
                continue;
            } else if (v instanceof Constant) {
                exprs[i] = newConstantExpression((Constant)v, null, null);
                continue;
            } else if (v instanceof DiscVariable) {
                exprs[i] = newDiscVariableExpression(null, null, (DiscVariable)v);
                continue;
            } else if (v instanceof InputVariable) {
                exprs[i] = newInputVariableExpression(null, null, (InputVariable)v);
                continue;
            }

            Assert.fail("Unknown kind of variable.");
        }
        return glue(exprs);
    }

    /**
     * Construct continuous variable expressions.
     *
     * @param vars Variables to add.
     * @return Expression containing the given continuous variables.
     */
    private Expression makeContinuous(ContVariable... vars) {
        Expression[] exprs = new Expression[vars.length];
        for (int i = 0; i < vars.length; i++) {
            exprs[i] = newContVariableExpression(false, null, null, vars[i]);
        }
        return glue(exprs);
    }

    /** Object being tested. */
    AlgDerInvalidations invalidations;

    /**
     * Wrapper around accessing the invalidation set.
     *
     * @param d Declaration to retrieve.
     * @param affects Expected answer.
     */
    private void verifyAffecting(Declaration d, Set<Declaration> affects) {
        VariableWrapper wrappedD = new VariableWrapper(d, false);
        Set<VariableWrapper> wrappedAffects = setc(affects.size());
        for (Declaration ad: affects) {
            wrappedAffects.add(new VariableWrapper(ad, ad instanceof ContVariable));
        }
        assertEquals(wrappedAffects, invalidations.getAffecting(wrappedD));
    }

    /** Invalidation test for constants in algebraic variables. */
    @Test
    public void constAlg() {
        // alg A31 = 31;
        AlgVariable algA31 = newAlgVariable("A31", null, null, newIntExpression(null, null, 31));

        // const notusedB = 28;
        // const usedC = 28;
        // alg AuseC = usedC;
        Constant constB = newConstant("notusedB", null, null, newIntExpression(null, null, 28));
        Constant constC = newConstant("usedC", null, null, newIntExpression(null, null, 28));
        AlgVariable algAusedC = newAlgVariable("AuseC", null, null, makeVariables(constC));

        List<AlgVariable> algVars = list(algA31, algAusedC);
        List<ContVariable> derivVars = list();

        invalidations = new AlgDerInvalidations();
        invalidations.computeAffects(algVars, derivVars);

        verifyAffecting(constB, set());
        verifyAffecting(constC, set()); // No constant tracking, 'algAusedC' not noticed.
    }

    /** Invalidation test for discrete vars in algebraic variables. */
    @Test
    public void discAlg() {
        // alg A31 = 31;
        AlgVariable algA31 = newAlgVariable("A31", null, null, newIntExpression(null, null, 31));

        // disc notusedD;
        // disc usedE;
        // alg AusedE = usedE;
        DiscVariable notusedD = newDiscVariable("notusedD", null, null, null);
        DiscVariable usedE = newDiscVariable("usedE", null, null, null);
        AlgVariable algAusedE = newAlgVariable("AusedE", null, null, makeVariables(usedE));

        List<AlgVariable> algVars = list(algA31, algAusedE);
        List<ContVariable> derivVars = list();

        invalidations = new AlgDerInvalidations();
        invalidations.computeAffects(algVars, derivVars);

        verifyAffecting(notusedD, set());
        verifyAffecting(usedE, set(algAusedE));
    }

    /** Invalidation test for input vars in algebraic variables. */
    @Test
    public void inputAlg() {
        // alg A31 = 31;
        AlgVariable algA31 = newAlgVariable("A31", null, null, newIntExpression(null, null, 31));

        // input notusedI;
        // input usedJ;
        // alg AusedJ = usedJ;
        InputVariable notusedI = newInputVariable("notusedI", null, null);
        InputVariable usedJ = newInputVariable("usedJ", null, null);
        AlgVariable algAusedJ = newAlgVariable("AusedJ", null, null, makeVariables(usedJ));

        List<AlgVariable> algVars = list(algA31, algAusedJ);
        List<ContVariable> derivVars = list();

        invalidations = new AlgDerInvalidations();
        invalidations.computeAffects(algVars, derivVars);

        verifyAffecting(notusedI, set());
        verifyAffecting(usedJ, set()); // Input variables are not tracked.
    }

    /** Invalidation test for continuous vars in algebraic variables. */
    @Test
    public void contAlg() {
        // alg A31 = 31;
        AlgVariable algA31 = newAlgVariable("A31", null, null, newIntExpression(null, null, 31));

        // cont notusedP;
        // cont usedQ;
        // alg AcontQ = usedQ;
        ContVariable notusedP = newContVariable(newRealExpression(null, null, "1.0"), "notusedP", null, null);
        ContVariable usedQ = newContVariable(newRealExpression(null, null, "1.0"), "usedQ", null, null);
        AlgVariable algAcontQ = newAlgVariable("AcontQ", null, null, makeContinuous(usedQ));

        List<AlgVariable> algVars = list(algA31, algAcontQ);
        List<ContVariable> derivVars = list();

        invalidations = new AlgDerInvalidations();
        invalidations.computeAffects(algVars, derivVars);

        verifyAffecting(notusedP, set());
        verifyAffecting(usedQ, set(algAcontQ));
    }

    /** Invalidation test for time in algebraic variables. */
    @Test
    public void timeAlg() {
        ContVariable usedQ = newContVariable(newRealExpression(null, null, "1.0"), "usedQ", null, null);
        AlgVariable algAcontQ = newAlgVariable("AcontQ", null, null, makeContinuous(usedQ));
        AlgVariable algTime = newAlgVariable("algTime", null, null, newTimeExpression());

        List<AlgVariable> algVars = list(algAcontQ, algTime);
        List<ContVariable> derivVars = list();

        invalidations = new AlgDerInvalidations();
        invalidations.computeAffects(algVars, derivVars);

        verifyAffecting(null, set(algTime));
        verifyAffecting(usedQ, set(algAcontQ));
    }

    /** Invalidation test for constants in derivative expressions. */
    @Test
    public void constDer() {
        // const notusedB = 28;
        // const usedC = 28;
        // alg AuseC = usedC;
        Constant constB = newConstant("notusedB", null, null, newIntExpression(null, null, 28));
        Constant constC = newConstant("usedC", null, null, newIntExpression(null, null, 28));
        ContVariable contUseC = newContVariable(makeVariables(constC), "usedC", null, null);

        List<AlgVariable> algVars = list();
        List<ContVariable> derivVars = list(contUseC);

        invalidations = new AlgDerInvalidations();
        invalidations.computeAffects(algVars, derivVars);

        verifyAffecting(constB, set());
        verifyAffecting(constC, set()); // No constant tracking, 'der(contUseC)' not noticed.
    }

    /** Invalidation test for discrete vars in derivative expressions. */
    @Test
    public void discDer() {
        // disc notusedD;
        // disc usedE;
        // alg AusedE = usedE;
        DiscVariable notusedD = newDiscVariable("notusedD", null, null, null);
        DiscVariable usedE = newDiscVariable("usedE", null, null, null);
        ContVariable contUsedE = newContVariable(makeVariables(usedE), "contUsedE", null, null);

        List<AlgVariable> algVars = list();
        List<ContVariable> derivVars = list(contUsedE);

        invalidations = new AlgDerInvalidations();
        invalidations.computeAffects(algVars, derivVars);

        verifyAffecting(notusedD, set());
        verifyAffecting(usedE, set(contUsedE));
    }

    /** Invalidation test for input vars in derivative expressions. */
    @Test
    public void inputDer() {
        // input notusedI;
        // input usedJ;
        // alg AusedJ = usedJ;
        InputVariable notusedI = newInputVariable("notusedI", null, null);
        InputVariable usedJ = newInputVariable("usedJ", null, null);
        ContVariable contUseJ = newContVariable(makeVariables(usedJ), "contUsedJ", null, null);

        List<AlgVariable> algVars = list();
        List<ContVariable> derivVars = list(contUseJ);

        invalidations = new AlgDerInvalidations();
        invalidations.computeAffects(algVars, derivVars);

        verifyAffecting(notusedI, set());
        verifyAffecting(usedJ, set()); // Input variables are not tracked.
    }

    /** Invalidation test for continuous vars in derivative expressions. */
    @Test
    public void contDer() {
        // cont notusedP;
        // cont usedQ;
        // alg AcontQ = usedQ;
        ContVariable notusedP = newContVariable(newRealExpression(null, null, "1.0"), "notusedP", null, null);
        ContVariable usedQ = newContVariable(newRealExpression(null, null, "1.0"), "usedQ", null, null);
        ContVariable contUsedQ = newContVariable(makeContinuous(usedQ), "contUsedQ", null, null);

        List<AlgVariable> algVars = list();
        List<ContVariable> derivVars = list(contUsedQ);

        invalidations = new AlgDerInvalidations();
        invalidations.computeAffects(algVars, derivVars);

        verifyAffecting(notusedP, set());
        verifyAffecting(usedQ, set(contUsedQ));
    }

    /** Invalidation test for algebraic vars in algebraic variables. */
    @Test
    public void algAlg() {
        DiscVariable discD = newDiscVariable("discD", null, null, null);
        DiscVariable discE = newDiscVariable("discE", null, null, null);
        AlgVariable algD = newAlgVariable("algD", null, null, makeVariables(discD));
        AlgVariable algE = newAlgVariable("algE", null, null, makeVariables(algD, discE));

        List<AlgVariable> algVars = list(algD, algE);
        List<ContVariable> derivVars = list();

        invalidations = new AlgDerInvalidations();
        invalidations.computeAffects(algVars, derivVars);

        verifyAffecting(discD, set(algD, algE));
        verifyAffecting(discE, set(algE));
    }
}
