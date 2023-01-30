//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.datasynth.varorder.parser;

import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newInputVariable;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newIntType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newSpecification;
import static org.eclipse.escet.common.java.Lists.list;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;

import org.eclipse.escet.cif.datasynth.options.BddAdvancedVariableOrderOption;
import org.eclipse.escet.cif.datasynth.options.BddDcshVarOrderOption;
import org.eclipse.escet.cif.datasynth.options.BddForceVarOrderOption;
import org.eclipse.escet.cif.datasynth.options.BddHyperEdgeAlgoOption;
import org.eclipse.escet.cif.datasynth.options.BddHyperEdgeAlgoOption.BddHyperEdgeAlgo;
import org.eclipse.escet.cif.datasynth.options.BddSlidingWindowSizeOption;
import org.eclipse.escet.cif.datasynth.options.BddSlidingWindowVarOrderOption;
import org.eclipse.escet.cif.datasynth.options.BddVariableOrderOption;
import org.eclipse.escet.cif.datasynth.spec.SynthesisInputVariable;
import org.eclipse.escet.cif.datasynth.spec.SynthesisVariable;
import org.eclipse.escet.cif.datasynth.varorder.orders.VarOrder;
import org.eclipse.escet.cif.datasynth.varorder.parser.ast.VarOrderOrOrdererInstance;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.common.app.framework.AppEnv;
import org.eclipse.escet.common.app.framework.exceptions.InvalidOptionException;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.setext.runtime.DebugMode;
import org.eclipse.escet.setext.runtime.exceptions.SyntaxException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/** {@link VarOrderParser} tests. */
public class VarOrderParserTest {
    @Before
    @SuppressWarnings("javadoc")
    public void before() {
        AppEnv.registerSimple();
        Options.set(BddVariableOrderOption.class, BddVariableOrderOption.DEFAULT);
        Options.set(BddDcshVarOrderOption.class, BddDcshVarOrderOption.DEFAULT);
        Options.set(BddForceVarOrderOption.class, BddForceVarOrderOption.DEFAULT);
        Options.set(BddSlidingWindowVarOrderOption.class, BddSlidingWindowVarOrderOption.DEFAULT);
        Options.set(BddSlidingWindowSizeOption.class, BddSlidingWindowSizeOption.DEFAULT);
        Options.set(BddHyperEdgeAlgoOption.class, BddHyperEdgeAlgoOption.DEFAULT);
        Options.set(BddAdvancedVariableOrderOption.class, BddAdvancedVariableOrderOption.DEFAULT);
    }

    @After
    @SuppressWarnings("javadoc")
    public void after() {
        AppEnv.unregisterApplication();
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testScannerInvalid() {
        testInvalid("%", "Scanning failed for character \"%\" (Unicode U+25) at line 1, column 1.");
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testWhitespace() {
        testValid("  model \t \n ( ) ", "model");
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testDefault() {
        testValid("default", "sorted -> force(metric=total-span, relations=configured) -> "
                + "slidwin(size=4, metric=total-span, relations=configured)");
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testModelValid() {
        testValid("model", "model");
        testValid("model()", "model");
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testModelInvalid() {
        testInvalid("model(a=1)",
                "Semantic error at line 1, column 7: The \"model\" order does not support the \"a\" argument.");
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testSortedValid() {
        testValid("sorted", "sorted");
        testValid("sorted()", "sorted");
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testSortedInvalid() {
        testInvalid("sorted(a=1)",
                "Semantic error at line 1, column 8: The \"sorted\" order does not support the \"a\" argument.");
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testRandomValid() {
        testValid("random", "random");
        testValid("random()", "random");
        testValid("random(seed=5)", "random(seed=5)");
        testValid("random(seed=0)", "random(seed=0)");
        testValid("random(seed=-1)", "random(seed=-1)");
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testRandomInvalid() {
        testInvalid("random(seed=x)",
                "Semantic error at line 1, column 8: The \"random\" order has an unsupported value for the \"seed\" "
                        + "argument: the value must be a number.");
        testInvalid("random(seed=9223372036854775808)",
                "Semantic error at line 1, column 8: The \"random\" order has an unsupported value for the \"seed\" "
                        + "argument: the value is out of range.");
        testInvalid("random(seed=a())",
                "Semantic error at line 1, column 8: The \"random\" order has an unsupported value for the \"seed\" "
                        + "argument: the value must be a number.");
        testInvalid("random(seed=1, seed=2)",
                "Semantic error at line 1, column 16: The \"random\" order has a duplicate \"seed\" argument.");
        testInvalid("random(a=1)",
                "Semantic error at line 1, column 8: The \"random\" order does not support the \"a\" argument.");
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testCustomValid() {
        Specification spec = newSpecification();
        InputVariable va = newInputVariable("a", null, newIntType(0, null, 1));
        InputVariable vb = newInputVariable("b", null, newIntType(0, null, 1));
        InputVariable vc = newInputVariable("c", null, newIntType(0, null, 1));
        spec.getDeclarations().add(va);
        spec.getDeclarations().add(vb);
        spec.getDeclarations().add(vc);
        SynthesisVariable a = new SynthesisInputVariable(va, newIntType(0, null, 1), 2, 0, 1);
        SynthesisVariable b = new SynthesisInputVariable(vb, newIntType(0, null, 1), 2, 0, 1);
        SynthesisVariable c = new SynthesisInputVariable(vc, newIntType(0, null, 1), 2, 0, 1);
        List<SynthesisVariable> vars = list(a, b, c);

        testValid("custom(order=\"a,b,c\")", vars, "custom(order=\"a,b,c\")");
        testValid("custom(order=\"c,a,b\")", vars, "custom(order=\"c,a,b\")");
        testValid("custom(order=\"c,b,a\")", vars, "custom(order=\"c,b,a\")");

        testValid("custom(order=\"a;b,c\")", vars, "custom(order=\"a;b,c\")");
        testValid("custom(order=\"c,a;b\")", vars, "custom(order=\"c,a;b\")");
        testValid("custom(order=\"c;b;a\")", vars, "custom(order=\"c;b;a\")");
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testCustomInvalid() {
        testInvalid("custom", "Semantic error at line 1, column 1: "
                + "The \"custom\" order is missing its mandatory \"order\" argument.");
        testInvalid("custom()", "Semantic error at line 1, column 1: "
                + "The \"custom\" order is missing its mandatory \"order\" argument.");
        testInvalid("custom(order=\"\", order=\"\")",
                "Semantic error at line 1, column 18: The \"custom\" order has a duplicate \"order\" argument.");
        testInvalid("custom(order=1)", "Semantic error at line 1, column 8: The \"custom\" order has "
                + "an unsupported value for the \"order\" argument: the value must be a string.");
        testInvalid("custom(order=\"does_not_exist\")", "Semantic error at line 1, column 8: The \"custom\" order "
                + "has an unsupported value for the \"order\" argument: can't find a match for \"does_not_exist\". "
                + "There is no supported variable or automaton (with two or more locations) in the specification that "
                + "matches the given name pattern.");

        Specification spec = newSpecification();
        InputVariable va = newInputVariable("a", null, newIntType(0, null, 1));
        InputVariable vb = newInputVariable("b", null, newIntType(0, null, 1));
        InputVariable vc = newInputVariable("c", null, newIntType(0, null, 1));
        spec.getDeclarations().add(va);
        spec.getDeclarations().add(vb);
        spec.getDeclarations().add(vc);
        SynthesisVariable a = new SynthesisInputVariable(va, newIntType(0, null, 1), 2, 0, 1);
        SynthesisVariable b = new SynthesisInputVariable(vb, newIntType(0, null, 1), 2, 0, 1);
        SynthesisVariable c = new SynthesisInputVariable(vc, newIntType(0, null, 1), 2, 0, 1);
        List<SynthesisVariable> vars = list(a, b, c);

        testInvalid("custom(order=\"a,a,b,c\")", vars, "Semantic error at line 1, column 8: The \"custom\" order "
                + "has an unsupported value for the \"order\" argument: \"a\" is included more than once.");
        testInvalid("custom(order=\"a,b\")", vars, "Semantic error at line 1, column 8: The \"custom\" order has an "
                + "unsupported value for the \"order\" argument: the following are missing from the specified order: "
                + "\"c\".");
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testReverseOrderValid() {
        testValid("reverse(order=model)", "reverse(order=model)");
        testValid("reverse(order=sorted)", "reverse(order=sorted)");
        testValid("reverse(order=(model -> sloan))", "reverse(order=model -> sloan(relations=configured))");
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testReverseOrderInvalid() {
        testInvalid("reverse", "Semantic error at line 1, column 1: "
                + "The \"reverse\" order is missing its mandatory \"order\" argument.");
        testInvalid("reverse()", "Semantic error at line 1, column 1: "
                + "The \"reverse\" order is missing its mandatory \"order\" argument.");
        testInvalid("reverse(a=1)",
                "Semantic error at line 1, column 9: The \"reverse\" order does not support the \"a\" argument.");
        testInvalid("reverse(order=model, order=model)",
                "Semantic error at line 1, column 22: The \"reverse\" order has a duplicate \"order\" argument.");
        testInvalid("reverse(order=1)", "Semantic error at line 1, column 9: The \"reverse\" order has an "
                + "unsupported value for the \"order\" argument: the value must be a variable order.");
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testUnknownOrder() {
        testInvalid("unknown", "Semantic error at line 1, column 1: Unknown variable order \"unknown\".");
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testNotStartWithOrder() {
        testInvalid("dcsh -> model", "Semantic error at line 1, column 1: Unknown variable order \"dcsh\".");
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testInitialOrderNotFirst() {
        testInvalid("model -> sorted", "Semantic error at line 1, column 10: Unknown variable orderer \"sorted\".");
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testEnumValid() {
        testValid("model -> sloan(relations=linearized)", "model -> sloan(relations=linearized)");
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testEnumsInvalid() {
        testInvalid("model -> dcsh(node-finder=1)", "Semantic error at line 1, column 15: The \"dcsh\" orderer has "
                + "an unsupported value for the \"node-finder\" argument: the value must be a node finder algorithm.");
        testInvalid("model -> dcsh(node-finder=x)", "Semantic error at line 1, column 15: The \"dcsh\" orderer has "
                + "an unsupported value for the \"node-finder\" argument: the value must be a node finder algorithm.");
        testInvalid("model -> dcsh(node-finder=x())", "Semantic error at line 1, column 15: The \"dcsh\" orderer has "
                + "an unsupported value for the \"node-finder\" argument: the value must be a node finder algorithm.");
        testInvalid("model -> dcsh(node-finder=x(a=1))", "Semantic error at line 1, column 15: "
                + "The \"dcsh\" orderer has an unsupported value for the \"node-finder\" argument: the value must be a "
                + "node finder algorithm.");
        testInvalid("model -> dcsh(node-finder=(model -> dcsh))", "Semantic error at line 1, column 15: "
                + "The \"dcsh\" orderer has an unsupported value for the \"node-finder\" argument: the value must be a "
                + "node finder algorithm.");
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testDcshValid() {
        testValid("model -> dcsh", "model -> dcsh(node-finder=george-liu, metric=wes, relations=configured)");
        testValid("model -> dcsh()", "model -> dcsh(node-finder=george-liu, metric=wes, relations=configured)");
        testValid("model -> dcsh(node-finder=george-liu)",
                "model -> dcsh(node-finder=george-liu, metric=wes, relations=configured)");
        testValid("model -> dcsh(node-finder=sloan)",
                "model -> dcsh(node-finder=sloan, metric=wes, relations=configured)");
        testValid("model -> dcsh(metric=total-span)",
                "model -> dcsh(node-finder=george-liu, metric=total-span, relations=configured)");
        testValid("model -> dcsh(metric=wes)",
                "model -> dcsh(node-finder=george-liu, metric=wes, relations=configured)");
        testValid("model -> dcsh(relations=legacy)",
                "model -> dcsh(node-finder=george-liu, metric=wes, relations=legacy)");
        testValid("model -> dcsh(relations=linearized)",
                "model -> dcsh(node-finder=george-liu, metric=wes, relations=linearized)");
        testValid("model -> dcsh(relations=configured)",
                "model -> dcsh(node-finder=george-liu, metric=wes, relations=configured)");
        testValid("model -> dcsh(node-finder=sloan, metric=total-span, relations=linearized)",
                "model -> dcsh(node-finder=sloan, metric=total-span, relations=linearized)");
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testDcshInvalid() {
        testInvalid("model -> dcsh(node-finder=george-liu, node-finder=sloan)",
                "Semantic error at line 1, column 39: The \"dcsh\" orderer has a duplicate \"node-finder\" argument.");
        testInvalid("model -> dcsh(node-finder=1)",
                "Semantic error at line 1, column 15: "
                        + "The \"dcsh\" orderer has an unsupported value for the \"node-finder\" argument: "
                        + "the value must be a node finder algorithm.");
        testInvalid("model -> dcsh(metric=total-span, metric=wes)",
                "Semantic error at line 1, column 34: The \"dcsh\" orderer has a duplicate \"metric\" argument.");
        testInvalid("model -> dcsh(metric=1)",
                "Semantic error at line 1, column 15: "
                        + "The \"dcsh\" orderer has an unsupported value for the \"metric\" argument: "
                        + "the value must be a metric.");
        testInvalid("model -> dcsh(relations=legacy, relations=linearized)",
                "Semantic error at line 1, column 33: The \"dcsh\" orderer has a duplicate \"relations\" argument.");
        testInvalid("model -> dcsh(relations=1)",
                "Semantic error at line 1, column 15: "
                        + "The \"dcsh\" orderer has an unsupported value for the \"relations\" argument: "
                        + "the value must be a kind of relations.");
        testInvalid("model -> dcsh(a=1)",
                "Semantic error at line 1, column 15: The \"dcsh\" orderer does not support the \"a\" argument.");
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testForceValid() {
        testValid("model -> force", "model -> force(metric=total-span, relations=configured)");
        testValid("model -> force()", "model -> force(metric=total-span, relations=configured)");
        testValid("model -> force(metric=total-span)", "model -> force(metric=total-span, relations=configured)");
        testValid("model -> force(metric=wes)", "model -> force(metric=wes, relations=configured)");
        testValid("model -> force(relations=legacy)", "model -> force(metric=total-span, relations=legacy)");
        testValid("model -> force(relations=linearized)", "model -> force(metric=total-span, relations=linearized)");
        testValid("model -> force(relations=configured)", "model -> force(metric=total-span, relations=configured)");
        testValid("model -> force(metric=wes, relations=linearized)",
                "model -> force(metric=wes, relations=linearized)");
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testForceInvalid() {
        testInvalid("model -> force(metric=total-span, metric=wes)",
                "Semantic error at line 1, column 35: The \"force\" orderer has a duplicate \"metric\" argument.");
        testInvalid("model -> force(metric=1)",
                "Semantic error at line 1, column 16: "
                        + "The \"force\" orderer has an unsupported value for the \"metric\" argument: "
                        + "the value must be a metric.");
        testInvalid("model -> force(relations=legacy, relations=linearized)",
                "Semantic error at line 1, column 34: The \"force\" orderer has a duplicate \"relations\" argument.");
        testInvalid("model -> force(relations=1)",
                "Semantic error at line 1, column 16: "
                        + "The \"force\" orderer has an unsupported value for the \"relations\" argument: "
                        + "the value must be a kind of relations.");
        testInvalid("model -> force(a=1)",
                "Semantic error at line 1, column 16: The \"force\" orderer does not support the \"a\" argument.");
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testSlidWinValid() {
        testValid("model -> slidwin", "model -> slidwin(size=4, metric=total-span, relations=configured)");
        testValid("model -> slidwin()", "model -> slidwin(size=4, metric=total-span, relations=configured)");
        testValid("model -> slidwin(size=1)", "model -> slidwin(size=1, metric=total-span, relations=configured)");
        testValid("model -> slidwin(size=12)", "model -> slidwin(size=12, metric=total-span, relations=configured)");
        testValid("model -> slidwin(metric=total-span)",
                "model -> slidwin(size=4, metric=total-span, relations=configured)");
        testValid("model -> slidwin(metric=wes)", "model -> slidwin(size=4, metric=wes, relations=configured)");
        testValid("model -> slidwin(relations=legacy)",
                "model -> slidwin(size=4, metric=total-span, relations=legacy)");
        testValid("model -> slidwin(relations=linearized)",
                "model -> slidwin(size=4, metric=total-span, relations=linearized)");
        testValid("model -> slidwin(relations=configured)",
                "model -> slidwin(size=4, metric=total-span, relations=configured)");
        testValid("model -> slidwin(size=5, metric=wes, relations=linearized)",
                "model -> slidwin(size=5, metric=wes, relations=linearized)");
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testSlidWinInvalid() {
        testInvalid("model -> slidwin(size=-1)",
                "Semantic error at line 1, column 18: "
                        + "The \"slidwin\" orderer has an unsupported value for the \"size\" argument: "
                        + "the value must be in the range [1..12].");
        testInvalid("model -> slidwin(size=0)",
                "Semantic error at line 1, column 18: "
                        + "The \"slidwin\" orderer has an unsupported value for the \"size\" argument: "
                        + "the value must be in the range [1..12].");
        testInvalid("model -> slidwin(size=13)",
                "Semantic error at line 1, column 18: "
                        + "The \"slidwin\" orderer has an unsupported value for the \"size\" argument: "
                        + "the value must be in the range [1..12].");
        testInvalid("model -> slidwin(size=x)",
                "Semantic error at line 1, column 18: "
                        + "The \"slidwin\" orderer has an unsupported value for the \"size\" argument: "
                        + "the value must be a number.");
        testInvalid("model -> slidwin(size=2147483648)",
                "Semantic error at line 1, column 18: "
                        + "The \"slidwin\" order has an unsupported value for the \"size\" argument: "
                        + "the value is out of range.");
        testInvalid("model -> slidwin(size=a())",
                "Semantic error at line 1, column 18: "
                        + "The \"slidwin\" orderer has an unsupported value for the \"size\" argument: "
                        + "the value must be a number.");
        testInvalid("model -> slidwin(size=1, size=2)",
                "Semantic error at line 1, column 26: The \"slidwin\" orderer has a duplicate \"size\" argument.");
        testInvalid("model -> slidwin(metric=total-span, metric=wes)",
                "Semantic error at line 1, column 37: The \"slidwin\" orderer has a duplicate \"metric\" argument.");
        testInvalid("model -> slidwin(metric=1)",
                "Semantic error at line 1, column 18: "
                        + "The \"slidwin\" orderer has an unsupported value for the \"metric\" argument: "
                        + "the value must be a metric.");
        testInvalid("model -> slidwin(relations=legacy, relations=linearized)",
                "Semantic error at line 1, column 36: The \"slidwin\" orderer has a duplicate \"relations\" argument.");
        testInvalid("model -> slidwin(relations=1)",
                "Semantic error at line 1, column 18: "
                        + "The \"slidwin\" orderer has an unsupported value for the \"relations\" argument: "
                        + "the value must be a kind of relations.");
        testInvalid("model -> slidwin(a=1)",
                "Semantic error at line 1, column 18: The \"slidwin\" orderer does not support the \"a\" argument.");
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testSloanValid() {
        testValid("model -> sloan", "model -> sloan(relations=configured)");
        testValid("model -> sloan()", "model -> sloan(relations=configured)");
        testValid("model -> sloan(relations=legacy)", "model -> sloan(relations=legacy)");
        testValid("model -> sloan(relations=linearized)", "model -> sloan(relations=linearized)");
        testValid("model -> sloan(relations=configured)", "model -> sloan(relations=configured)");
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testSloanInvalid() {
        testInvalid("model -> sloan(relations=legacy, relations=linearized)",
                "Semantic error at line 1, column 34: The \"sloan\" orderer has a duplicate \"relations\" argument.");
        testInvalid("model -> sloan(relations=1)",
                "Semantic error at line 1, column 16: "
                        + "The \"sloan\" orderer has an unsupported value for the \"relations\" argument: "
                        + "the value must be a kind of relations.");
        testInvalid("model -> sloan(a=1)",
                "Semantic error at line 1, column 16: The \"sloan\" orderer does not support the \"a\" argument.");
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testWeightedCmValid() {
        testValid("model -> weighted-cm", "model -> weighted-cm(node-finder=george-liu, relations=configured)");
        testValid("model -> weighted-cm()", "model -> weighted-cm(node-finder=george-liu, relations=configured)");
        testValid("model -> weighted-cm(node-finder=george-liu)",
                "model -> weighted-cm(node-finder=george-liu, relations=configured)");
        testValid("model -> weighted-cm(node-finder=sloan)",
                "model -> weighted-cm(node-finder=sloan, relations=configured)");
        testValid("model -> weighted-cm(relations=legacy)",
                "model -> weighted-cm(node-finder=george-liu, relations=legacy)");
        testValid("model -> weighted-cm(relations=linearized)",
                "model -> weighted-cm(node-finder=george-liu, relations=linearized)");
        testValid("model -> weighted-cm(relations=configured)",
                "model -> weighted-cm(node-finder=george-liu, relations=configured)");
        testValid("model -> weighted-cm(node-finder=sloan, relations=linearized)",
                "model -> weighted-cm(node-finder=sloan, relations=linearized)");
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testWeightedCmInvalid() {
        testInvalid("model -> weighted-cm(node-finder=george-liu, node-finder=sloan)",
                "Semantic error at line 1, column 46: "
                        + "The \"weighted-cm\" orderer has a duplicate \"node-finder\" argument.");
        testInvalid("model -> weighted-cm(node-finder=1)",
                "Semantic error at line 1, column 22: "
                        + "The \"weighted-cm\" orderer has an unsupported value for the \"node-finder\" argument: "
                        + "the value must be a node finder algorithm.");
        testInvalid("model -> weighted-cm(relations=legacy, relations=linearized)",
                "Semantic error at line 1, column 40: "
                        + "The \"weighted-cm\" orderer has a duplicate \"relations\" argument.");
        testInvalid("model -> weighted-cm(relations=1)",
                "Semantic error at line 1, column 22: "
                        + "The \"weighted-cm\" orderer has an unsupported value for the \"relations\" argument: "
                        + "the value must be a kind of relations.");
        testInvalid("model -> weighted-cm(a=1)", "Semantic error at line 1, column 22: "
                + "The \"weighted-cm\" orderer does not support the \"a\" argument.");
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testChoiceOrdererValid() {
        testValid("model -> or(choices=[force, dcsh])",
                "model -> or(metric=wes, relations=configured, "
                        + "choices=[force(metric=total-span, relations=configured), "
                        + "dcsh(node-finder=george-liu, metric=wes, relations=configured)])");
        testValid("model -> or(choices=[(force -> dcsh), (dcsh -> force)])",
                "model -> or(metric=wes, relations=configured, "
                        + "choices=[force(metric=total-span, relations=configured) -> "
                        + "dcsh(node-finder=george-liu, metric=wes, relations=configured), "
                        + "dcsh(node-finder=george-liu, metric=wes, relations=configured) -> "
                        + "force(metric=total-span, relations=configured)])");
        testValid("model -> or(choices=[sloan, sloan], metric=total-span)",
                "model -> or(metric=total-span, relations=configured, "
                        + "choices=[sloan(relations=configured), sloan(relations=configured)])");
        testValid("model -> or(choices=[sloan, sloan], metric=wes)", "model -> or(metric=wes, relations=configured, "
                + "choices=[sloan(relations=configured), sloan(relations=configured)])");
        testValid("model -> or(choices=[sloan, sloan], relations=legacy)", "model -> or(metric=wes, relations=legacy, "
                + "choices=[sloan(relations=configured), sloan(relations=configured)])");
        testValid("model -> or(choices=[sloan, sloan], relations=linearized)",
                "model -> or(metric=wes, relations=linearized, "
                        + "choices=[sloan(relations=configured), sloan(relations=configured)])");
        testValid("model -> or(choices=[sloan, sloan], relations=configured)",
                "model -> or(metric=wes, relations=configured, "
                        + "choices=[sloan(relations=configured), sloan(relations=configured)])");
        testValid("model -> or(choices=[force, dcsh], metric=total-span, relations=linearized)",
                "model -> or(metric=total-span, relations=linearized, "
                        + "choices=[force(metric=total-span, relations=configured), "
                        + "dcsh(node-finder=george-liu, metric=wes, relations=configured)])");
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testChoiceOrdererInvalid() {
        testInvalid("model -> or", "Semantic error at line 1, column 10: "
                + "The \"or\" orderer is missing its mandatory \"orderers\" argument.");
        testInvalid("model -> or()", "Semantic error at line 1, column 10: "
                + "The \"or\" orderer is missing its mandatory \"orderers\" argument.");
        testInvalid("model -> or(choices=[force])",
                "Semantic error at line 1, column 13: "
                        + "The \"or\" orderer has an unsupported value for the \"choices\" argument: "
                        + "the value must be a list with at least two variable orderers.");
        testInvalid("model -> or(choices=1)",
                "Semantic error at line 1, column 13: "
                        + "The \"or\" orderer has an unsupported value for the \"choices\" argument: "
                        + "the value must be a list of variable orderers.");
        testInvalid("model -> or(choices=[force, dcsh], choices=[force, dcsh])",
                "Semantic error at line 1, column 36: The \"or\" orderer has a duplicate \"choices\" argument.");
        testInvalid("model -> or(metric=total-span, metric=wes)",
                "Semantic error at line 1, column 32: The \"or\" orderer has a duplicate \"metric\" argument.");
        testInvalid("model -> or(metric=1)",
                "Semantic error at line 1, column 13: "
                        + "The \"or\" orderer has an unsupported value for the \"metric\" argument: "
                        + "the value must be a metric.");
        testInvalid("model -> or(relations=legacy, relations=linearized)",
                "Semantic error at line 1, column 31: The \"or\" orderer has a duplicate \"relations\" argument.");
        testInvalid("model -> or(relations=1)",
                "Semantic error at line 1, column 13: "
                        + "The \"or\" orderer has an unsupported value for the \"relations\" argument: "
                        + "the value must be a kind of relations.");
        testInvalid("model -> or(a=1)",
                "Semantic error at line 1, column 13: The \"or\" orderer does not support the \"a\" argument.");
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testReverseOrdererValid() {
        testValid("model -> reverse(orderer=sloan)",
                "model -> reverse(relations=configured, orderer=sloan(relations=configured))");
        testValid("model -> reverse(orderer=(sloan -> sloan))", "model -> reverse(relations=configured, "
                + "orderer=sloan(relations=configured) -> sloan(relations=configured))");
        testValid("model -> reverse(orderer=sloan, relations=legacy)",
                "model -> reverse(relations=legacy, orderer=sloan(relations=configured))");
        testValid("model -> reverse(orderer=sloan, relations=linearized)",
                "model -> reverse(relations=linearized, orderer=sloan(relations=configured))");
        testValid("model -> reverse(orderer=sloan, relations=configured)",
                "model -> reverse(relations=configured, orderer=sloan(relations=configured))");
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testReverseOrdererInvalid() {
        testInvalid("model -> reverse", "Semantic error at line 1, column 10: "
                + "The \"reverse\" orderer is missing its mandatory \"orderer\" argument.");
        testInvalid("model -> reverse()", "Semantic error at line 1, column 10: "
                + "The \"reverse\" orderer is missing its mandatory \"orderer\" argument.");
        testInvalid("model -> reverse(orderer=[force, dcsh])",
                "Semantic error at line 1, column 18: "
                        + "The \"reverse\" orderer has an unsupported value for the \"orderer\" argument: "
                        + "the value must be a variable orderer.");
        testInvalid("model -> reverse(orderer=1)",
                "Semantic error at line 1, column 18: "
                        + "The \"reverse\" orderer has an unsupported value for the \"orderer\" argument: "
                        + "the value must be a variable orderer.");
        testInvalid("model -> reverse(orderer=force, orderer=force)",
                "Semantic error at line 1, column 33: The \"reverse\" orderer has a duplicate \"orderer\" argument.");
        testInvalid("model -> reverse(relations=legacy, relations=linearized)",
                "Semantic error at line 1, column 36: The \"reverse\" orderer has a duplicate \"relations\" argument.");
        testInvalid("model -> reverse(relations=1)",
                "Semantic error at line 1, column 18: "
                        + "The \"reverse\" orderer has an unsupported value for the \"relations\" argument: "
                        + "the value must be a kind of relations.");
        testInvalid("model -> reverse(a=1)",
                "Semantic error at line 1, column 18: The \"reverse\" orderer does not support the \"a\" argument.");
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testExtraComma() {
        testValid("random(seed=5,)", "random(seed=5)");
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testParentheses() {
        testValid("sorted -> dcsh -> force -> slidwin",
                "sorted -> dcsh(node-finder=george-liu, metric=wes, relations=configured) -> "
                        + "force(metric=total-span, relations=configured) -> "
                        + "slidwin(size=4, metric=total-span, relations=configured)");
        testValid("(sorted -> dcsh -> force -> slidwin)",
                "sorted -> dcsh(node-finder=george-liu, metric=wes, relations=configured) -> "
                        + "force(metric=total-span, relations=configured) -> "
                        + "slidwin(size=4, metric=total-span, relations=configured)");
        testValid("sorted -> (dcsh -> force) -> slidwin",
                "sorted -> dcsh(node-finder=george-liu, metric=wes, relations=configured) -> "
                        + "force(metric=total-span, relations=configured) -> "
                        + "slidwin(size=4, metric=total-span, relations=configured)");
        testValid("(sorted) -> (dcsh) -> (force) -> (slidwin)",
                "sorted -> dcsh(node-finder=george-liu, metric=wes, relations=configured) -> "
                        + "force(metric=total-span, relations=configured) -> "
                        + "slidwin(size=4, metric=total-span, relations=configured)");
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testMixSimpleAdvancedOptionsInitialOrder() {
        Options.set(BddVariableOrderOption.class, "random");
        Options.set(BddAdvancedVariableOrderOption.class, "random");
        testInvalid("random", "The BDD variable order is configured through simple and advanced options, "
                + "which is not supported. Use only simple or only advanced options.");
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testMixSimpleAdvancedOptionsDcsh() {
        Options.set(BddDcshVarOrderOption.class, true);
        Options.set(BddAdvancedVariableOrderOption.class, "random");
        testInvalid("random", "The BDD variable order is configured through simple and advanced options, "
                + "which is not supported. Use only simple or only advanced options.");
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testMixSimpleAdvancedOptionsForce() {
        Options.set(BddForceVarOrderOption.class, false);
        Options.set(BddAdvancedVariableOrderOption.class, "random");
        testInvalid("random", "The BDD variable order is configured through simple and advanced options, "
                + "which is not supported. Use only simple or only advanced options.");
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testMixSimpleAdvancedOptionsSlidWin() {
        Options.set(BddSlidingWindowVarOrderOption.class, false);
        Options.set(BddAdvancedVariableOrderOption.class, "random");
        testInvalid("random", "The BDD variable order is configured through simple and advanced options, "
                + "which is not supported. Use only simple or only advanced options.");
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testMixSimpleAdvancedOptionsSlidWinSize() {
        Options.set(BddSlidingWindowSizeOption.class, 2);
        Options.set(BddAdvancedVariableOrderOption.class, "random");
        testInvalid("random", "The BDD variable order is configured through simple and advanced options, "
                + "which is not supported. Use only simple or only advanced options.");
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testMixSimpleAdvancedOptionsRelations() {
        Options.set(BddHyperEdgeAlgoOption.class, BddHyperEdgeAlgo.LINEARIZED);
        Options.set(BddAdvancedVariableOrderOption.class, "random");
        testInvalid("random", "The BDD variable order is configured through simple and advanced options, "
                + "which is not supported. Use only simple or only advanced options.");
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testMixSimpleAdvancedOptionsMultiple() {
        Options.set(BddSlidingWindowSizeOption.class, 2);
        Options.set(BddHyperEdgeAlgoOption.class, BddHyperEdgeAlgo.LINEARIZED);
        Options.set(BddAdvancedVariableOrderOption.class, "random");
        testInvalid("random", "The BDD variable order is configured through simple and advanced options, "
                + "which is not supported. Use only simple or only advanced options.");
    }

    /**
     * Test a valid order, without variables given to the type checker.
     *
     * @param orderTxt The order to test.
     * @param expectedOrder The expected textual representation of the order.
     */
    private void testValid(String orderTxt, String expectedOrder) {
        testValid(orderTxt, Collections.emptyList(), expectedOrder);
    }

    /**
     * Test a valid order.
     *
     * @param orderTxt The order to test.
     * @param variables The synthesis variables.
     * @param expectedOrder The expected textual representation of the order.
     */
    private void testValid(String orderTxt, List<SynthesisVariable> variables, String expectedOrder) {
        // Parse.
        VarOrderParser parser = new VarOrderParser();
        List<VarOrderOrOrdererInstance> parseResult = parser.parseString(orderTxt, "/dummy", null, DebugMode.NONE);

        // Type check.
        VarOrderTypeChecker tchecker = new VarOrderTypeChecker(variables);
        VarOrder order = tchecker.typeCheck(parseResult);
        assertFalse("Type check warnings found.", tchecker.hasWarning());
        if (tchecker.hasError()) {
            assertEquals("Expected one type check problem.", tchecker.getProblems().size(), 1);
            assertTrue(tchecker.getProblems().get(0).toString(), false);
        }
        assertTrue("Type checker produced no result.", order != null);
        assertEquals(expectedOrder, order.toString());
    }

    /**
     * Test an invalid order, without variables given to the type checker.
     *
     * @param orderTxt The order to test.
     * @param expectedMsg The error message.
     */
    private void testInvalid(String orderTxt, String expectedMsg) {
        testInvalid(orderTxt, Collections.emptyList(), expectedMsg);
    }

    /**
     * Test an invalid order.
     *
     * @param orderTxt The order to test.
     * @param variables The synthesis variables.
     * @param expectedMsg The error message.
     */
    private void testInvalid(String orderTxt, List<SynthesisVariable> variables, String expectedMsg) {
        // Parse.
        VarOrderParser parser = new VarOrderParser();
        List<VarOrderOrOrdererInstance> parseResult;
        try {
            parseResult = parser.parseString(orderTxt, "/dummy", null, DebugMode.NONE);
        } catch (SyntaxException e) {
            String actualMsg = e.getMessage();
            assertEquals(expectedMsg, actualMsg);
            return;
        }

        // Type check.
        try {
            VarOrderTypeChecker tchecker = new VarOrderTypeChecker(variables);
            VarOrder order = tchecker.typeCheck(parseResult);
            assertEquals("Type checker produced result.", order, null);
            assertFalse("Type check warnings found.", tchecker.hasWarning());
            assertTrue("Type check no error found.", tchecker.hasError());
            assertEquals("Expected one type check problem.", tchecker.getProblems().size(), 1);
            assertEquals(expectedMsg, tchecker.getProblems().get(0).toString());
        } catch (InvalidOptionException e) {
            assertEquals(expectedMsg, e.getMessage());
        }
    }
}
