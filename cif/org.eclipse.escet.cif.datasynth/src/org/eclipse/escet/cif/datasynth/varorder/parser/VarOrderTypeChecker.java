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

package org.eclipse.escet.cif.datasynth.varorder.parser;

import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Lists.slice;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.eclipse.escet.cif.datasynth.options.BddAdvancedVariableOrderOption;
import org.eclipse.escet.cif.datasynth.options.BddDcshVarOrderOption;
import org.eclipse.escet.cif.datasynth.options.BddForceVarOrderOption;
import org.eclipse.escet.cif.datasynth.options.BddHyperEdgeAlgoOption;
import org.eclipse.escet.cif.datasynth.options.BddSlidingWindowSizeOption;
import org.eclipse.escet.cif.datasynth.options.BddSlidingWindowVarOrderOption;
import org.eclipse.escet.cif.datasynth.options.BddVariableOrderOption;
import org.eclipse.escet.cif.datasynth.spec.SynthesisVariable;
import org.eclipse.escet.cif.datasynth.varorder.graph.algos.PseudoPeripheralNodeFinderKind;
import org.eclipse.escet.cif.datasynth.varorder.helper.RelationsKind;
import org.eclipse.escet.cif.datasynth.varorder.metrics.VarOrderMetricKind;
import org.eclipse.escet.cif.datasynth.varorder.orderers.ChoiceVarOrderer;
import org.eclipse.escet.cif.datasynth.varorder.orderers.DcshVarOrderer;
import org.eclipse.escet.cif.datasynth.varorder.orderers.ForceVarOrderer;
import org.eclipse.escet.cif.datasynth.varorder.orderers.ReverseVarOrderer;
import org.eclipse.escet.cif.datasynth.varorder.orderers.SequentialVarOrderer;
import org.eclipse.escet.cif.datasynth.varorder.orderers.SlidingWindowVarOrderer;
import org.eclipse.escet.cif.datasynth.varorder.orderers.SloanVarOrderer;
import org.eclipse.escet.cif.datasynth.varorder.orderers.VarOrderer;
import org.eclipse.escet.cif.datasynth.varorder.orderers.WeightedCuthillMcKeeVarOrderer;
import org.eclipse.escet.cif.datasynth.varorder.orders.CustomVarOrder;
import org.eclipse.escet.cif.datasynth.varorder.orders.ModelVarOrder;
import org.eclipse.escet.cif.datasynth.varorder.orders.OrdererVarOrder;
import org.eclipse.escet.cif.datasynth.varorder.orders.RandomVarOrder;
import org.eclipse.escet.cif.datasynth.varorder.orders.ReverseVarOrder;
import org.eclipse.escet.cif.datasynth.varorder.orders.SortedVarOrder;
import org.eclipse.escet.cif.datasynth.varorder.orders.VarOrder;
import org.eclipse.escet.cif.datasynth.varorder.parser.ast.VarOrderOrOrdererArg;
import org.eclipse.escet.cif.datasynth.varorder.parser.ast.VarOrderOrOrdererInstance;
import org.eclipse.escet.cif.datasynth.varorder.parser.ast.VarOrderOrOrdererKind;
import org.eclipse.escet.cif.datasynth.varorder.parser.ast.VarOrderOrOrdererListOrdersArg;
import org.eclipse.escet.cif.datasynth.varorder.parser.ast.VarOrderOrOrdererMultiInstance;
import org.eclipse.escet.cif.datasynth.varorder.parser.ast.VarOrderOrOrdererNumberArg;
import org.eclipse.escet.cif.datasynth.varorder.parser.ast.VarOrderOrOrdererOrderArg;
import org.eclipse.escet.cif.datasynth.varorder.parser.ast.VarOrderOrOrdererSingleInstance;
import org.eclipse.escet.cif.datasynth.varorder.parser.ast.VarOrderOrOrdererStringArg;
import org.eclipse.escet.common.app.framework.exceptions.InvalidOptionException;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Pair;
import org.eclipse.escet.common.typechecker.SemanticException;
import org.eclipse.escet.common.typechecker.TypeChecker;
import org.eclipse.escet.setext.runtime.Token;

/** Variable order type checker. */
public class VarOrderTypeChecker extends TypeChecker<List<VarOrderOrOrdererInstance>, VarOrder> {
    /** The synthesis variables to order. */
    private final List<SynthesisVariable> variables;

    /**
     * Constructor for the {@link VarOrderTypeChecker} class.
     *
     * @param variables The synthesis variables to order.
     */
    public VarOrderTypeChecker(List<SynthesisVariable> variables) {
        this.variables = variables;
    }

    @Override
    protected VarOrder transRoot(List<VarOrderOrOrdererInstance> astInstances) {
        // Make sure basic and advanced options are not mixed.
        checkBasicAndAdvancedOptionsMix();

        // Process the advanced option.
        return checkVarOrder(astInstances);
    }

    /**
     * Type check a variable order.
     *
     * @param astInstances The variable order instance AST objects.
     * @return The variable order.
     */
    private VarOrder checkVarOrder(List<VarOrderOrOrdererInstance> astInstances) {
        Assert.check(!astInstances.isEmpty());
        VarOrder order = checkVarOrder(first(astInstances));
        if (astInstances.size() == 1) {
            return order;
        } else {
            List<VarOrderer> orderers = checkVarOrderers(slice(astInstances, 1, null));
            VarOrderer orderer = (orderers.size() == 1) ? first(orderers) : new SequentialVarOrderer(orderers);
            return new OrdererVarOrder(order, orderer);
        }
    }

    /**
     * Type check a variable order.
     *
     * @param astInstance The variable order instance AST object.
     * @return The variable order.
     */
    private VarOrder checkVarOrder(VarOrderOrOrdererInstance astInstance) {
        // Handle multiple instances.
        if (astInstance instanceof VarOrderOrOrdererMultiInstance multiInstance) {
            if (multiInstance.instances.size() == 1) {
                return checkVarOrder(first(multiInstance.instances));
            } else {
                return checkVarOrder(multiInstance.instances);
            }
        }

        // Handle single instance.
        Assert.check(astInstance instanceof VarOrderOrOrdererSingleInstance);
        VarOrderOrOrdererSingleInstance astOrder = (VarOrderOrOrdererSingleInstance)astInstance;
        String name = astOrder.name.text;
        switch (name) {
            // Use basic variable order options.
            case "basic":
                return getBasicConfiguredOrder();

            // Basic variable orders.
            case "model":
                return checkModelOrder(astOrder);

            case "sorted":
                return checkSortedOrder(astOrder);

            case "random":
                return checkRandomOrder(astOrder);

            case "custom":
                return checkCustomOrder(astOrder);

            // Composite variable orders.
            case "reverse":
                return checkReverseOrder(astOrder);

            // Unknown.
            default:
                addError(fmt("Unknown variable order \"%s\".", name), astOrder.name.position);
                throw new SemanticException();
        }
    }

    /**
     * Type check a model variable order.
     *
     * @param astOrder The variable order instance AST object.
     * @return The variable order.
     */
    private VarOrder checkModelOrder(VarOrderOrOrdererSingleInstance astOrder) {
        String name = astOrder.name.text;
        if (!astOrder.arguments.isEmpty()) {
            reportUnsupportedArgumentName(name, VarOrderOrOrdererKind.ORDER, astOrder.arguments.get(0));
            throw new SemanticException();
        }
        return new ModelVarOrder();
    }

    /**
     * Type check a sorted variable order.
     *
     * @param astOrder The variable order instance AST object.
     * @return The variable order.
     */
    private VarOrder checkSortedOrder(VarOrderOrOrdererSingleInstance astOrder) {
        String name = astOrder.name.text;
        if (!astOrder.arguments.isEmpty()) {
            reportUnsupportedArgumentName(name, VarOrderOrOrdererKind.ORDER, astOrder.arguments.get(0));
            throw new SemanticException();
        }
        return new SortedVarOrder();
    }

    /**
     * Type check a random variable order.
     *
     * @param astOrder The variable order instance AST object.
     * @return The variable order.
     */
    private VarOrder checkRandomOrder(VarOrderOrOrdererSingleInstance astOrder) {
        String name = astOrder.name.text;
        Long seed = null;
        for (VarOrderOrOrdererArg arg: astOrder.arguments) {
            switch (arg.name.text) {
                case "seed":
                    checkDuplicateArg(name, VarOrderOrOrdererKind.ORDER, arg, seed);
                    seed = checkLongArg(name, VarOrderOrOrdererKind.ORDER, arg);
                    break;
                default:
                    reportUnsupportedArgumentName(name, VarOrderOrOrdererKind.ORDER, arg);
                    throw new SemanticException();
            }
        }
        return new RandomVarOrder(seed);
    }

    /**
     * Type check a custom variable order.
     *
     * @param astOrder The variable order instance AST object.
     * @return The variable order.
     */
    private VarOrder checkCustomOrder(VarOrderOrOrdererSingleInstance astOrder) {
        String name = astOrder.name.text;
        List<Pair<SynthesisVariable, Integer>> order = null;
        for (VarOrderOrOrdererArg arg: astOrder.arguments) {
            switch (arg.name.text) {
                case "order":
                    checkDuplicateArg(name, VarOrderOrOrdererKind.ORDER, arg, order);

                    if (!(arg instanceof VarOrderOrOrdererStringArg)) {
                        reportUnsupportedArgumentValue(name, VarOrderOrOrdererKind.ORDER, arg,
                                "the value must be a string.");
                        throw new SemanticException();
                    }

                    Pair<List<Pair<SynthesisVariable, Integer>>, String> customVarOrderOrError = //
                            CustomVarOrderParser.parse(((VarOrderOrOrdererStringArg)arg).text, variables);
                    if (customVarOrderOrError.right != null) {
                        reportUnsupportedArgumentValue(name, VarOrderOrOrdererKind.ORDER, arg,
                                customVarOrderOrError.right);
                        throw new SemanticException();
                    }

                    order = customVarOrderOrError.left;
                    break;
                default:
                    reportUnsupportedArgumentName(name, VarOrderOrOrdererKind.ORDER, arg);
                    throw new SemanticException();
            }
        }
        if (order == null) {
            reportMissingArgument(astOrder.name, VarOrderOrOrdererKind.ORDER, "order");
            throw new SemanticException();
        }
        return new CustomVarOrder(order);
    }

    /**
     * Type check a reverse variable order.
     *
     * @param astOrder The variable order instance AST object.
     * @return The variable order.
     */
    private VarOrder checkReverseOrder(VarOrderOrOrdererSingleInstance astOrder) {
        String name = astOrder.name.text;
        VarOrder order = null;
        for (VarOrderOrOrdererArg arg: astOrder.arguments) {
            switch (arg.name.text) {
                case "order":
                    checkDuplicateArg(name, VarOrderOrOrdererKind.ORDER, arg, order);
                    if (!(arg instanceof VarOrderOrOrdererOrderArg)) {
                        reportUnsupportedArgumentValue(name, VarOrderOrOrdererKind.ORDER, arg,
                                "the value must be a variable order.");
                        throw new SemanticException();
                    }
                    order = checkVarOrder(((VarOrderOrOrdererOrderArg)arg).value);
                    break;
                default:
                    reportUnsupportedArgumentName(name, VarOrderOrOrdererKind.ORDER, arg);
                    throw new SemanticException();
            }
        }
        if (order == null) {
            reportMissingArgument(astOrder.name, VarOrderOrOrdererKind.ORDER, "order");
            throw new SemanticException();
        }
        return new ReverseVarOrder(order);
    }

    /**
     * Type check variable orderers.
     *
     * @param astInstances The variable orderer instance AST objects.
     * @return The variable orderers (at least one).
     */
    private List<VarOrderer> checkVarOrderers(List<VarOrderOrOrdererInstance> astInstances) {
        Assert.check(!astInstances.isEmpty());
        List<VarOrderer> orderers = listc(astInstances.size());
        for (VarOrderOrOrdererInstance astOrderer: astInstances) {
            orderers.add(checkVarOrderer(astOrderer));
        }
        return orderers;
    }

    /**
     * Type check variable orderer.
     *
     * @param astInstance The variable orderer instance AST object.
     * @return The variable orderer.
     */
    private VarOrderer checkVarOrderer(VarOrderOrOrdererInstance astInstance) {
        // Handle multiple instances.
        if (astInstance instanceof VarOrderOrOrdererMultiInstance multiInstance) {
            List<VarOrderer> orderers = checkVarOrderers(multiInstance.instances);
            VarOrderer orderer = (orderers.size() == 1) ? first(orderers) : new SequentialVarOrderer(orderers);
            return orderer;
        }

        // Handle single instance.
        Assert.check(astInstance instanceof VarOrderOrOrdererSingleInstance);
        VarOrderOrOrdererSingleInstance astOrderer = (VarOrderOrOrdererSingleInstance)astInstance;
        String name = astOrderer.name.text;
        switch (name) {
            // Variable orderer algorithms.
            case "dcsh":
                return checkDcshVarOrderer(astOrderer);

            case "force":
                return checkForceVarOrderer(astOrderer);

            case "slidwin":
                return checkSlidWinVarOrderer(astOrderer);

            case "sloan":
                return checkSloanVarOrderer(astOrderer);

            case "weighted-cm":
                return checkWeightedCmVarOrderer(astOrderer);

            // Composite variable orderers.
            case "or":
                return checkChoiceVarOrderer(astOrderer);

            case "reverse":
                return checkReverseVarOrderer(astOrderer);

            // Unknown.
            default:
                addError(fmt("Unknown variable orderer \"%s\".", name), astOrderer.name.position);
                throw new SemanticException();
        }
    }

    /**
     * Type check a DCSH variable orderer.
     *
     * @param astOrderer The variable orderer instance AST object.
     * @return The variable orderer.
     */
    private VarOrderer checkDcshVarOrderer(VarOrderOrOrdererSingleInstance astOrderer) {
        String name = astOrderer.name.text;
        PseudoPeripheralNodeFinderKind nodeFinder = null;
        VarOrderMetricKind metric = null;
        RelationsKind relations = null;
        for (VarOrderOrOrdererArg arg: astOrderer.arguments) {
            switch (arg.name.text) {
                case "node-finder":
                    checkDuplicateArg(name, VarOrderOrOrdererKind.ORDERER, arg, nodeFinder);
                    nodeFinder = checkEnumArg(name, VarOrderOrOrdererKind.ORDERER, arg,
                            PseudoPeripheralNodeFinderKind.class, "a node finder algorithm");
                    break;
                case "metric":
                    checkDuplicateArg(name, VarOrderOrOrdererKind.ORDERER, arg, metric);
                    metric = checkEnumArg(name, VarOrderOrOrdererKind.ORDERER, arg, VarOrderMetricKind.class,
                            "a metric");
                    break;
                case "relations":
                    checkDuplicateArg(name, VarOrderOrOrdererKind.ORDERER, arg, relations);
                    relations = checkEnumArg(name, VarOrderOrOrdererKind.ORDERER, arg, RelationsKind.class,
                            "a kind of relations");
                    break;
                default:
                    reportUnsupportedArgumentName(name, VarOrderOrOrdererKind.ORDERER, arg);
                    throw new SemanticException();
            }
        }
        if (nodeFinder == null) {
            nodeFinder = PseudoPeripheralNodeFinderKind.GEORGE_LIU;
        }
        if (metric == null) {
            metric = VarOrderMetricKind.WES;
        }
        if (relations == null) {
            relations = RelationsKind.CONFIGURED;
        }
        return new DcshVarOrderer(nodeFinder, metric, relations);
    }

    /**
     * Type check a FORCE variable orderer.
     *
     * @param astOrderer The variable orderer instance AST object.
     * @return The variable orderer.
     */
    private VarOrderer checkForceVarOrderer(VarOrderOrOrdererSingleInstance astOrderer) {
        String name = astOrderer.name.text;
        VarOrderMetricKind metric = null;
        RelationsKind relations = null;
        for (VarOrderOrOrdererArg arg: astOrderer.arguments) {
            switch (arg.name.text) {
                case "metric":
                    checkDuplicateArg(name, VarOrderOrOrdererKind.ORDERER, arg, metric);
                    metric = checkEnumArg(name, VarOrderOrOrdererKind.ORDERER, arg, VarOrderMetricKind.class,
                            "a metric");
                    break;
                case "relations":
                    checkDuplicateArg(name, VarOrderOrOrdererKind.ORDERER, arg, relations);
                    relations = checkEnumArg(name, VarOrderOrOrdererKind.ORDERER, arg, RelationsKind.class,
                            "a kind of relations");
                    break;
                default:
                    reportUnsupportedArgumentName(name, VarOrderOrOrdererKind.ORDERER, arg);
                    throw new SemanticException();
            }
        }
        if (metric == null) {
            metric = VarOrderMetricKind.TOTAL_SPAN;
        }
        if (relations == null) {
            relations = RelationsKind.CONFIGURED;
        }
        return new ForceVarOrderer(metric, relations);
    }

    /**
     * Type check a sliding window variable orderer.
     *
     * @param astOrderer The variable orderer instance AST object.
     * @return The variable orderer.
     */
    private VarOrderer checkSlidWinVarOrderer(VarOrderOrOrdererSingleInstance astOrderer) {
        String name = astOrderer.name.text;
        Integer size = null;
        VarOrderMetricKind metric = null;
        RelationsKind relations = null;
        for (VarOrderOrOrdererArg arg: astOrderer.arguments) {
            switch (arg.name.text) {
                case "size":
                    checkDuplicateArg(name, VarOrderOrOrdererKind.ORDERER, arg, size);
                    size = checkIntArg(name, VarOrderOrOrdererKind.ORDERER, arg);
                    if (size < 1 || size > 12) {
                        reportUnsupportedArgumentValue(name, VarOrderOrOrdererKind.ORDERER, arg,
                                "the value must be in the range [1..12].");
                        throw new SemanticException();
                    }
                    break;
                case "metric":
                    checkDuplicateArg(name, VarOrderOrOrdererKind.ORDERER, arg, metric);
                    metric = checkEnumArg(name, VarOrderOrOrdererKind.ORDERER, arg, VarOrderMetricKind.class,
                            "a metric");
                    break;
                case "relations":
                    checkDuplicateArg(name, VarOrderOrOrdererKind.ORDERER, arg, relations);
                    relations = checkEnumArg(name, VarOrderOrOrdererKind.ORDERER, arg, RelationsKind.class,
                            "a kind of relations");
                    break;
                default:
                    reportUnsupportedArgumentName(name, VarOrderOrOrdererKind.ORDERER, arg);
                    throw new SemanticException();
            }
        }
        if (size == null) {
            size = BddSlidingWindowSizeOption.getMaxLen();
        }
        if (metric == null) {
            metric = VarOrderMetricKind.TOTAL_SPAN;
        }
        if (relations == null) {
            relations = RelationsKind.CONFIGURED;
        }
        return new SlidingWindowVarOrderer(size, metric, relations);
    }

    /**
     * Type check a Sloan variable orderer.
     *
     * @param astOrderer The variable orderer instance AST object.
     * @return The variable orderer.
     */
    private VarOrderer checkSloanVarOrderer(VarOrderOrOrdererSingleInstance astOrderer) {
        String name = astOrderer.name.text;
        RelationsKind relations = null;
        for (VarOrderOrOrdererArg arg: astOrderer.arguments) {
            switch (arg.name.text) {
                case "relations":
                    checkDuplicateArg(name, VarOrderOrOrdererKind.ORDERER, arg, relations);
                    relations = checkEnumArg(name, VarOrderOrOrdererKind.ORDERER, arg, RelationsKind.class,
                            "a kind of relations");
                    break;
                default:
                    reportUnsupportedArgumentName(name, VarOrderOrOrdererKind.ORDERER, arg);
                    throw new SemanticException();
            }
        }
        if (relations == null) {
            relations = RelationsKind.CONFIGURED;
        }
        return new SloanVarOrderer(relations);
    }

    /**
     * Type check a Weighted Cuthill-McKee variable orderer.
     *
     * @param astOrderer The variable orderer instance AST object.
     * @return The variable orderer.
     */
    private VarOrderer checkWeightedCmVarOrderer(VarOrderOrOrdererSingleInstance astOrderer) {
        String name = astOrderer.name.text;
        PseudoPeripheralNodeFinderKind nodeFinder = null;
        RelationsKind relations = null;
        for (VarOrderOrOrdererArg arg: astOrderer.arguments) {
            switch (arg.name.text) {
                case "node-finder":
                    checkDuplicateArg(name, VarOrderOrOrdererKind.ORDERER, arg, nodeFinder);
                    nodeFinder = checkEnumArg(name, VarOrderOrOrdererKind.ORDERER, arg,
                            PseudoPeripheralNodeFinderKind.class, "a node finder algorithm");
                    break;
                case "relations":
                    checkDuplicateArg(name, VarOrderOrOrdererKind.ORDERER, arg, relations);
                    relations = checkEnumArg(name, VarOrderOrOrdererKind.ORDERER, arg, RelationsKind.class,
                            "a kind of relations");
                    break;
                default:
                    reportUnsupportedArgumentName(name, VarOrderOrOrdererKind.ORDERER, arg);
                    throw new SemanticException();
            }
        }
        if (nodeFinder == null) {
            nodeFinder = PseudoPeripheralNodeFinderKind.GEORGE_LIU;
        }
        if (relations == null) {
            relations = RelationsKind.CONFIGURED;
        }
        return new WeightedCuthillMcKeeVarOrderer(nodeFinder, relations);
    }

    /**
     * Type check a choice variable orderer.
     *
     * @param astOrderer The variable orderer instance AST object.
     * @return The variable orderer.
     */
    private VarOrderer checkChoiceVarOrderer(VarOrderOrOrdererSingleInstance astOrderer) {
        String name = astOrderer.name.text;
        List<VarOrderer> orderers = null;
        VarOrderMetricKind metric = null;
        RelationsKind relations = null;
        for (VarOrderOrOrdererArg arg: astOrderer.arguments) {
            switch (arg.name.text) {
                case "choices":
                    checkDuplicateArg(name, VarOrderOrOrdererKind.ORDERER, arg, orderers);
                    if (!(arg instanceof VarOrderOrOrdererListOrdersArg)) {
                        reportUnsupportedArgumentValue(name, VarOrderOrOrdererKind.ORDERER, arg,
                                "the value must be a list of variable orderers.");
                        throw new SemanticException();
                    }
                    orderers = checkVarOrderers(((VarOrderOrOrdererListOrdersArg)arg).value);
                    if (orderers.size() < 2) {
                        reportUnsupportedArgumentValue(name, VarOrderOrOrdererKind.ORDERER, arg,
                                "the value must be a list with at least two variable orderers.");
                        throw new SemanticException();
                    }
                    break;
                case "metric":
                    checkDuplicateArg(name, VarOrderOrOrdererKind.ORDERER, arg, metric);
                    metric = checkEnumArg(name, VarOrderOrOrdererKind.ORDERER, arg, VarOrderMetricKind.class,
                            "a metric");
                    break;
                case "relations":
                    checkDuplicateArg(name, VarOrderOrOrdererKind.ORDERER, arg, relations);
                    relations = checkEnumArg(name, VarOrderOrOrdererKind.ORDERER, arg, RelationsKind.class,
                            "a kind of relations");
                    break;
                default:
                    reportUnsupportedArgumentName(name, VarOrderOrOrdererKind.ORDERER, arg);
                    throw new SemanticException();
            }
        }
        if (orderers == null) {
            reportMissingArgument(astOrderer.name, VarOrderOrOrdererKind.ORDERER, "orderers");
            throw new SemanticException();
        }
        if (metric == null) {
            metric = VarOrderMetricKind.WES;
        }
        if (relations == null) {
            relations = RelationsKind.CONFIGURED;
        }
        return new ChoiceVarOrderer(orderers, metric, relations);
    }

    /**
     * Type check a reverse variable orderer.
     *
     * @param astOrderer The variable orderer instance AST object.
     * @return The variable orderer.
     */
    private VarOrderer checkReverseVarOrderer(VarOrderOrOrdererSingleInstance astOrderer) {
        String name = astOrderer.name.text;
        VarOrderer orderer = null;
        RelationsKind relations = null;
        for (VarOrderOrOrdererArg arg: astOrderer.arguments) {
            switch (arg.name.text) {
                case "orderer":
                    checkDuplicateArg(name, VarOrderOrOrdererKind.ORDERER, arg, orderer);
                    if (!(arg instanceof VarOrderOrOrdererOrderArg)) {
                        reportUnsupportedArgumentValue(name, VarOrderOrOrdererKind.ORDERER, arg,
                                "the value must be a variable orderer.");
                        throw new SemanticException();
                    }
                    orderer = checkVarOrderer(((VarOrderOrOrdererOrderArg)arg).value);
                    break;
                case "relations":
                    checkDuplicateArg(name, VarOrderOrOrdererKind.ORDERER, arg, relations);
                    relations = checkEnumArg(name, VarOrderOrOrdererKind.ORDERER, arg, RelationsKind.class,
                            "a kind of relations");
                    break;
                default:
                    reportUnsupportedArgumentName(name, VarOrderOrOrdererKind.ORDERER, arg);
                    throw new SemanticException();
            }
        }
        if (orderer == null) {
            reportMissingArgument(astOrderer.name, VarOrderOrOrdererKind.ORDERER, "orderer");
            throw new SemanticException();
        }
        if (relations == null) {
            relations = RelationsKind.CONFIGURED;
        }
        return new ReverseVarOrderer(orderer, relations);
    }

    /**
     * Check whether basic options and advanced options for configuring the BDD variable order are mixed.
     *
     * @throws InvalidOptionException If the options are mixed.
     */
    private void checkBasicAndAdvancedOptionsMix() {
        boolean basicDefault = //
                BddVariableOrderOption.isDefault() && //
                        BddDcshVarOrderOption.isDefault() && //
                        BddForceVarOrderOption.isDefault() && // \
                        BddSlidingWindowVarOrderOption.isDefault() && //
                        BddSlidingWindowSizeOption.isDefault() && //
                        BddHyperEdgeAlgoOption.isDefault();
        boolean advancedDefault = BddAdvancedVariableOrderOption.isDefault();

        if (!basicDefault && !advancedDefault) {
            throw new InvalidOptionException("The BDD variable order is configured through basic and advanced options, "
                    + "which is not supported. Use only basic or only advanced options.");
        }
    }

    /**
     * Get the variable order configured via the basic (non-advanced) options.
     *
     * @return The variable order.
     */
    private VarOrder getBasicConfiguredOrder() {
        VarOrder initialVarOrder = getBasicConfiguredInitialOrder();
        List<VarOrderer> orderers = list();
        if (BddDcshVarOrderOption.isEnabled()) {
            orderers.add(new DcshVarOrderer(PseudoPeripheralNodeFinderKind.GEORGE_LIU, VarOrderMetricKind.WES,
                    RelationsKind.CONFIGURED));
        }
        if (BddForceVarOrderOption.isEnabled()) {
            orderers.add(new ForceVarOrderer(VarOrderMetricKind.TOTAL_SPAN, RelationsKind.CONFIGURED));
        }
        if (BddSlidingWindowVarOrderOption.isEnabled()) {
            int maxLen = BddSlidingWindowSizeOption.getMaxLen();
            orderers.add(new SlidingWindowVarOrderer(maxLen, VarOrderMetricKind.TOTAL_SPAN, RelationsKind.CONFIGURED));
        }
        VarOrder varOrder = initialVarOrder;
        if (!orderers.isEmpty()) {
            varOrder = new OrdererVarOrder(varOrder,
                    (orderers.size() == 1) ? first(orderers) : new SequentialVarOrderer(orderers));
        }
        return varOrder;
    }

    /**
     * Get the initial variable order configured via the basic (non-advanced) option.
     *
     * @return The initial variable order.
     */
    private VarOrder getBasicConfiguredInitialOrder() {
        String orderTxt = BddVariableOrderOption.getOrder().trim();
        String orderTxtLower = orderTxt.toLowerCase(Locale.US);
        if (orderTxtLower.equals("model")) {
            return new ModelVarOrder();
        } else if (orderTxtLower.equals("reverse-model")) {
            return new ReverseVarOrder(new ModelVarOrder());
        } else if (orderTxtLower.equals("sorted")) {
            return new SortedVarOrder();
        } else if (orderTxtLower.equals("reverse-sorted")) {
            return new ReverseVarOrder(new SortedVarOrder());
        } else if (orderTxtLower.equals("random")) {
            return new RandomVarOrder(null);
        } else if (orderTxtLower.startsWith("random:")) {
            int idx = orderTxt.indexOf(":");
            String seedTxt = orderTxt.substring(idx + 1).trim();
            long seed;
            try {
                seed = Long.parseUnsignedLong(seedTxt);
            } catch (NumberFormatException ex) {
                String msg = fmt("Invalid BDD variable random order seed number: \"%s\".", orderTxt);
                throw new InvalidOptionException(msg, ex);
            }
            return new RandomVarOrder(seed);
        } else {
            Pair<List<Pair<SynthesisVariable, Integer>>, String> customVarOrderOrError = CustomVarOrderParser
                    .parse(orderTxt, variables);
            if (customVarOrderOrError.right != null) {
                throw new InvalidOptionException("Invalid BDD variable order: " + customVarOrderOrError.right);
            }
            return new CustomVarOrder(customVarOrderOrError.left);
        }
    }

    /**
     * Check a value of an argument for being a duplicate.
     *
     * @param name The name of the variable order(er).
     * @param kind The variable order(er) kind.
     * @param arg The argument.
     * @param curValue The current value of the argument. If not {@code null}, this argument provides a second value.
     */
    private void checkDuplicateArg(String name, VarOrderOrOrdererKind kind, VarOrderOrOrdererArg arg, Object curValue) {
        if (curValue != null) {
            reportDuplicateArgument(name, kind, arg);
            throw new SemanticException();
        }
    }

    /**
     * Check a value of an int-typed argument.
     *
     * @param name The name of the variable order(er).
     * @param kind The variable order(er) kind.
     * @param arg The argument.
     * @return The value of the argument.
     */
    private int checkIntArg(String name, VarOrderOrOrdererKind kind, VarOrderOrOrdererArg arg) {
        // Check for right kind of value.
        if (!(arg instanceof VarOrderOrOrdererNumberArg)) {
            reportUnsupportedArgumentValue(name, kind, arg, "the value must be a number.");
            throw new SemanticException();
        }

        // Parse the value.
        int value;
        try {
            value = Integer.parseInt(((VarOrderOrOrdererNumberArg)arg).value.text);
        } catch (NumberFormatException e) {
            reportUnsupportedArgumentValue(name, VarOrderOrOrdererKind.ORDER, arg, "the value is out of range.");
            throw new SemanticException();
        }

        // Return the value.
        return value;
    }

    /**
     * Check a value of a long-typed argument.
     *
     * @param name The name of the variable order(er).
     * @param kind The variable order(er) kind.
     * @param arg The argument.
     * @return The value of the argument.
     */
    private long checkLongArg(String name, VarOrderOrOrdererKind kind, VarOrderOrOrdererArg arg) {
        // Check for right kind of value.
        if (!(arg instanceof VarOrderOrOrdererNumberArg)) {
            reportUnsupportedArgumentValue(name, kind, arg, "the value must be a number.");
            throw new SemanticException();
        }

        // Parse the value.
        long value;
        try {
            value = Long.parseLong(((VarOrderOrOrdererNumberArg)arg).value.text);
        } catch (NumberFormatException e) {
            reportUnsupportedArgumentValue(name, VarOrderOrOrdererKind.ORDER, arg, "the value is out of range.");
            throw new SemanticException();
        }

        // Return the value.
        return value;
    }

    /**
     * Check a value of a enum-typed argument.
     *
     * @param <T> The enum type.
     * @param name The name of the variable order(er).
     * @param kind The variable order(er) kind.
     * @param arg The argument.
     * @param enumClass The class of the enum.
     * @param valueDescription A description of the value.
     * @return The value of the argument.
     */
    private <T extends Enum<T>> T checkEnumArg(String name, VarOrderOrOrdererKind kind, VarOrderOrOrdererArg arg,
            Class<T> enumClass, String valueDescription)
    {
        // Check for right kind of value.
        if (!(arg instanceof VarOrderOrOrdererOrderArg)) {
            reportUnsupportedArgumentValue(name, kind, arg, fmt("the value must be %s.", valueDescription));
            throw new SemanticException();
        }
        VarOrderOrOrdererInstance order = ((VarOrderOrOrdererOrderArg)arg).value;

        // Check for single.
        if (order instanceof VarOrderOrOrdererMultiInstance) {
            reportUnsupportedArgumentValue(name, kind, arg, fmt("the value must be %s.", valueDescription));
            throw new SemanticException();
        }
        VarOrderOrOrdererSingleInstance single = (VarOrderOrOrdererSingleInstance)order;

        // Check for no arguments.
        if (single.hasArgs) {
            reportUnsupportedArgumentValue(name, kind, arg, fmt("the value must be %s.", valueDescription));
            throw new SemanticException();
        }

        // Parse the value.
        String constantName = single.name.text.replace("-", "_").toUpperCase(Locale.US);
        T[] values = enumClass.getEnumConstants();
        List<T> matches = Arrays.stream(values).filter(v -> v.name().equals(constantName)).collect(Collectors.toList());
        Assert.check(matches.size() <= 2);
        if (matches.size() == 1) {
            return first(matches);
        }

        // No matching enum constant found.
        reportUnsupportedArgumentValue(name, kind, arg, fmt("the value must be %s.", valueDescription));
        throw new SemanticException();
    }

    /**
     * Report an unsupported name of an variable order(er) argument.
     *
     * @param name The name of the variable order(er).
     * @param kind The variable order(er) kind.
     * @param arg The unsupported argument.
     */
    private void reportUnsupportedArgumentName(String name, VarOrderOrOrdererKind kind, VarOrderOrOrdererArg arg) {
        addError(fmt("The \"%s\" %s does not support the \"%s\" argument.", name,
                kind.toString().toLowerCase(Locale.US), arg.name.text), arg.name.position);
    }

    /**
     * Report a duplicate variable order(er) argument.
     *
     * @param name The name of the variable order(er).
     * @param kind The variable order(er) kind.
     * @param arg The duplicate argument.
     */
    private void reportDuplicateArgument(String name, VarOrderOrOrdererKind kind, VarOrderOrOrdererArg arg) {
        addError(fmt("The \"%s\" %s has a duplicate \"%s\" argument.", name, kind.toString().toLowerCase(Locale.US),
                arg.name.text), arg.name.position);
    }

    /**
     * Report a missing mandatory variable order(er) argument.
     *
     * @param name The name of the variable order(er).
     * @param kind The variable order(er) kind.
     * @param missingArgName The name of the missing argument.
     */
    private void reportMissingArgument(Token name, VarOrderOrOrdererKind kind, String missingArgName) {
        addError(fmt("The \"%s\" %s is missing its mandatory \"%s\" argument.", name.text,
                kind.toString().toLowerCase(Locale.US), missingArgName), name.position);
    }

    /**
     * Report an unsupported value of an variable order(er) argument.
     *
     * @param name The name of the variable order(er).
     * @param kind The variable order(er) kind.
     * @param arg The unsupported argument.
     * @param details A detail message describing why the value is not supported. Must end with a period.
     */
    private void reportUnsupportedArgumentValue(String name, VarOrderOrOrdererKind kind, VarOrderOrOrdererArg arg,
            String details)
    {
        addError(fmt("The \"%s\" %s has an unsupported value for the \"%s\" argument: %s", name,
                kind.toString().toLowerCase(Locale.US), arg.name.text, details), arg.name.position);
    }
}
