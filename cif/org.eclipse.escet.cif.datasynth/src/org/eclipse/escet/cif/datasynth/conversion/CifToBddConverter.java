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

package org.eclipse.escet.cif.datasynth.conversion;

import static org.eclipse.escet.cif.common.CifTextUtils.getAbsName;
import static org.eclipse.escet.cif.common.CifTypeUtils.normalizeType;
import static org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator.BI_CONDITIONAL;
import static org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator.CONJUNCTION;
import static org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator.DISJUNCTION;
import static org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator.IMPLICATION;
import static org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator.INTEGER_DIVISION;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newAssignment;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBinaryExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBoolType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEvent;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newInputVariableExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newMonitors;
import static org.eclipse.escet.common.emf.EMFHelper.deepclone;
import static org.eclipse.escet.common.java.Lists.concat;
import static org.eclipse.escet.common.java.Lists.copy;
import static org.eclipse.escet.common.java.Lists.filter;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.last;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Lists.reverse;
import static org.eclipse.escet.common.java.Lists.set2list;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Maps.mapc;
import static org.eclipse.escet.common.java.Pair.pair;
import static org.eclipse.escet.common.java.Sets.copy;
import static org.eclipse.escet.common.java.Sets.difference;
import static org.eclipse.escet.common.java.Sets.list2set;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Sets.setc;
import static org.eclipse.escet.common.java.Sets.sortedstrings;
import static org.eclipse.escet.common.java.Sets.union;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.str;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.escet.cif.cif2cif.LinearizeProduct;
import org.eclipse.escet.cif.cif2cif.LocationPointerManager;
import org.eclipse.escet.cif.common.CifEnumLiteral;
import org.eclipse.escet.cif.common.CifEquationUtils;
import org.eclipse.escet.cif.common.CifEvalException;
import org.eclipse.escet.cif.common.CifEvalUtils;
import org.eclipse.escet.cif.common.CifEventUtils;
import org.eclipse.escet.cif.common.CifEventUtils.Alphabets;
import org.eclipse.escet.cif.common.CifGuardUtils;
import org.eclipse.escet.cif.common.CifGuardUtils.LocRefExprCreator;
import org.eclipse.escet.cif.common.CifLocationUtils;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.CifValueUtils;
import org.eclipse.escet.cif.datasynth.bdd.BddUtils;
import org.eclipse.escet.cif.datasynth.bdd.CifBddBitVector;
import org.eclipse.escet.cif.datasynth.bdd.CifBddBitVectorAndCarry;
import org.eclipse.escet.cif.datasynth.settings.CifDataSynthesisSettings;
import org.eclipse.escet.cif.datasynth.settings.EdgeGranularity;
import org.eclipse.escet.cif.datasynth.settings.EdgeOrderDuplicateEventAllowance;
import org.eclipse.escet.cif.datasynth.spec.CifBddDiscVariable;
import org.eclipse.escet.cif.datasynth.spec.CifBddEdge;
import org.eclipse.escet.cif.datasynth.spec.CifBddInputVariable;
import org.eclipse.escet.cif.datasynth.spec.CifBddLocPtrVariable;
import org.eclipse.escet.cif.datasynth.spec.CifBddSpec;
import org.eclipse.escet.cif.datasynth.spec.CifBddTypedVariable;
import org.eclipse.escet.cif.datasynth.spec.CifBddVariable;
import org.eclipse.escet.cif.datasynth.varorder.helper.VarOrder;
import org.eclipse.escet.cif.datasynth.varorder.helper.VarOrderHelper;
import org.eclipse.escet.cif.datasynth.varorder.helper.VarOrdererData;
import org.eclipse.escet.cif.datasynth.varorder.orderers.VarOrderer;
import org.eclipse.escet.cif.datasynth.varorder.parser.VarOrdererParser;
import org.eclipse.escet.cif.datasynth.varorder.parser.VarOrdererTypeChecker;
import org.eclipse.escet.cif.datasynth.varorder.parser.ast.VarOrdererInstance;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.InvKind;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.SupKind;
import org.eclipse.escet.cif.metamodel.cif.automata.Assignment;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent;
import org.eclipse.escet.cif.metamodel.cif.automata.IfUpdate;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.automata.Monitors;
import org.eclipse.escet.cif.metamodel.cif.automata.Update;
import org.eclipse.escet.cif.metamodel.cif.automata.impl.EdgeEventImpl;
import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Constant;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumLiteral;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.cif.metamodel.cif.expressions.AlgVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.BoolExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ConstantExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ContVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ElifExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IfExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.InputVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IntExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.LocationExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ProjectionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SwitchCase;
import org.eclipse.escet.cif.metamodel.cif.expressions.SwitchExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TauExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TupleExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryOperator;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.EnumType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.common.box.GridBox;
import org.eclipse.escet.common.box.GridBox.GridBoxLayout;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Pair;
import org.eclipse.escet.common.java.Sets;
import org.eclipse.escet.common.java.Strings;
import org.eclipse.escet.common.java.exceptions.InvalidInputException;
import org.eclipse.escet.common.java.exceptions.InvalidOptionException;
import org.eclipse.escet.common.java.exceptions.UnsupportedException;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;
import org.eclipse.escet.setext.runtime.DebugMode;
import org.eclipse.escet.setext.runtime.exceptions.SyntaxException;

import com.github.javabdd.BDD;
import com.github.javabdd.BDDDomain;
import com.github.javabdd.BDDFactory;

/** Converter to convert CIF a specification to a CIF/BDD representation. */
public class CifToBddConverter {
    /** Precondition violations found so far. */
    private final Set<String> problems = set();

    /**
     * Per requirement automaton, the monitors as specified in the original specification. They are replaced by monitors
     * that monitor the entire alphabet of the automaton, in order to be able to treat requirement automata as plants.
     * This mapping can be used to later restore the original monitors. The mapping is {@code null} if not yet or no
     * longer available.
     */
    private Map<Automaton, Monitors> originalMonitors;

    /**
     * Converts a CIF specification to a CIF/BDD representation, checking for precondition violations along the way.
     *
     * @param spec The CIF specification to convert. Must not have any component definitions or instantiations.
     * @param settings The settings to use.
     * @param factory The BDD factory to use.
     * @return The CIF/BDD representation of the CIF specification.
     */
    public CifBddSpec convert(Specification spec, CifDataSynthesisSettings settings, BDDFactory factory) {
        // Convert CIF specification and return the resulting CIF/BDD specification, but only if no precondition
        // violations.
        CifBddSpec cifBddSpec = convertSpec(spec, settings, factory);
        if (problems.isEmpty()) {
            return cifBddSpec;
        }

        // Precondition violations found.
        String msg = "Data-based supervisory controller synthesis failed due to unsatisfied preconditions:\n - "
                + String.join("\n - ", sortedstrings(problems));
        throw new UnsupportedException(msg);
    }

    /**
     * Converts a CIF specification to a CIF/BDD representation, checking for precondition violations along the way.
     *
     * @param spec The CIF specification to convert. Must not have any component definitions or instantiations.
     * @param settings The settings to use.
     * @param factory The BDD factory to use.
     * @return The CIF/BDD representation of the CIF specification.
     */
    private CifBddSpec convertSpec(Specification spec, CifDataSynthesisSettings settings, BDDFactory factory) {
        // Initialize CIF/BDD specification.
        CifBddSpec cifBddSpec = new CifBddSpec(settings);
        cifBddSpec.factory = factory;

        if (cifBddSpec.settings.shouldTerminate.get()) {
            return cifBddSpec;
        }

        // Collect and check events. May collect more than union of alphabets.
        List<Event> events = list();
        collectEvents(spec, events);
        for (Event event: events) {
            if (event.getControllable() == null) {
                String msg = fmt("Unsupported event \"%s\": event is not declared as controllable or uncontrollable.",
                        getAbsName(event));
                problems.add(msg);
            }
        }

        if (cifBddSpec.settings.shouldTerminate.get()) {
            return cifBddSpec;
        }

        // Collect automata.
        List<Automaton> automata = list();
        collectAutomata(spec, automata);

        if (cifBddSpec.settings.shouldTerminate.get()) {
            return cifBddSpec;
        }

        // Check automata, and partition into plants/requirements.
        List<Automaton> plants = list();
        List<Automaton> requirements = list();
        for (Automaton cifAut: automata) {
            switch (cifAut.getKind()) {
                case PLANT:
                    plants.add(cifAut);
                    break;
                case REQUIREMENT:
                    requirements.add(cifAut);
                    break;
                default: {
                    String msg = fmt("Unsupported automaton \"%s\": only plant and requirement automata are supported.",
                            getAbsName(cifAut));
                    problems.add(msg);
                }
            }
        }

        if (cifBddSpec.settings.shouldTerminate.get()) {
            return cifBddSpec;
        }

        // Need at least one plant automaton.
        if (plants.isEmpty()) {
            String msg = "Unsupported specification: no plant automata found.";
            problems.add(msg);
        }

        if (cifBddSpec.settings.shouldTerminate.get()) {
            return cifBddSpec;
        }

        // Update automata for partitioned ordering.
        automata = concat(plants, requirements);

        if (cifBddSpec.settings.shouldTerminate.get()) {
            return cifBddSpec;
        }

        // Get plant and requirement alphabets.
        List<Alphabets> plantAlphabets = listc(plants.size());
        List<Alphabets> reqAlphabets = listc(requirements.size());
        Set<Event> plantAlphabet = set();
        Set<Event> reqAlphabet = set();
        for (Automaton plant: plants) {
            Alphabets alphabets = CifEventUtils.getAllAlphabets(plant, null);
            plantAlphabets.add(alphabets);
            plantAlphabet.addAll(alphabets.syncAlphabet);
            plantAlphabet.addAll(alphabets.sendAlphabet);
            plantAlphabet.addAll(alphabets.recvAlphabet);
        }
        for (Automaton req: requirements) {
            Alphabets alphabets = CifEventUtils.getAllAlphabets(req, null);
            reqAlphabets.add(alphabets);
            reqAlphabet.addAll(alphabets.syncAlphabet);
            reqAlphabet.addAll(alphabets.sendAlphabet);
            reqAlphabet.addAll(alphabets.recvAlphabet);
        }

        if (cifBddSpec.settings.shouldTerminate.get()) {
            return cifBddSpec;
        }

        // Get alphabet for (un)controlled system. We allow events that are only in the alphabet of the requirements, by
        // considering them always enabled in the plant. Since the requirement automata are used as fully monitored
        // plants (full observers) during linearization, the combined linearized edges for such an event always allow
        // that event, in the uncontrolled system.
        cifBddSpec.alphabet = union(plantAlphabet, reqAlphabet);

        if (cifBddSpec.settings.shouldTerminate.get()) {
            return cifBddSpec;
        }

        // Check use of channels in requirements.
        for (int i = 0; i < requirements.size(); i++) {
            Set<Event> commEvents = union(reqAlphabets.get(i).sendAlphabet, reqAlphabets.get(i).recvAlphabet);
            if (commEvents.isEmpty()) {
                continue;
            }

            List<String> names = listc(commEvents.size());
            for (Event extra: commEvents) {
                names.add("\"" + getAbsName(extra) + "\"");
            }

            String msg = fmt("Unsupported %s: requirement uses channel%s: %s.",
                    CifTextUtils.getComponentText1(requirements.get(i)), (commEvents.size() == 1) ? "" : "s",
                    String.join(", ", names));
            problems.add(msg);
        }

        if (cifBddSpec.settings.shouldTerminate.get()) {
            return cifBddSpec;
        }

        // Get controllable events subset of the alphabet.
        cifBddSpec.controllables = set();
        for (Event event: cifBddSpec.alphabet) {
            if (event.getControllable() == null) {
                continue;
            }
            if (event.getControllable()) {
                cifBddSpec.controllables.add(event);
            }
        }

        if (cifBddSpec.settings.shouldTerminate.get()) {
            return cifBddSpec;
        }

        // Collect variables, and automata for which we need to create location pointer variables, i.e. the automata
        // with more than one location.
        List<PositionObject> cifVarObjs = list();
        collectVariableObjects(spec, cifVarObjs);

        if (cifBddSpec.settings.shouldTerminate.get()) {
            return cifBddSpec;
        }

        // Create location pointer manager.
        List<Automaton> lpAuts = filter(cifVarObjs, Automaton.class);
        CifBddLocationPointerManager locPtrManager = new CifBddLocationPointerManager(lpAuts);

        if (cifBddSpec.settings.shouldTerminate.get()) {
            return cifBddSpec;
        }

        // Check and convert variables. Create location pointers.
        cifBddSpec.variables = new CifBddVariable[cifVarObjs.size()];
        for (int i = 0; i < cifBddSpec.variables.length; i++) {
            PositionObject cifVarObj = cifVarObjs.get(i);
            if (cifVarObj instanceof Automaton) {
                Automaton lpAut = (Automaton)cifVarObj;
                DiscVariable lpVar = locPtrManager.getLocationPointer(lpAut);
                cifBddSpec.variables[i] = createLpVar(lpAut, lpVar);
            } else {
                cifBddSpec.variables[i] = convertTypedVar((Declaration)cifVarObj);
            }
        }

        if (cifBddSpec.settings.shouldTerminate.get()) {
            return cifBddSpec;
        }

        // Order variables and create domains.
        orderVars(cifBddSpec, spec);
        if (cifBddSpec.settings.shouldTerminate.get()) {
            return cifBddSpec;
        }

        createVarDomains(cifBddSpec);
        if (cifBddSpec.settings.shouldTerminate.get()) {
            return cifBddSpec;
        }

        // Create auxiliary data for updates.
        createUpdateAuxiliaries(cifBddSpec);
        if (cifBddSpec.settings.shouldTerminate.get()) {
            return cifBddSpec;
        }

        // Check and convert initialization predicates.
        cifBddSpec.initialsVars = listc(cifBddSpec.variables.length);
        for (int i = 0; i < cifBddSpec.variables.length; i++) {
            cifBddSpec.initialsVars.add(null);
        }
        cifBddSpec.initialsComps = list();
        cifBddSpec.initialsLocs = list();
        cifBddSpec.initialVars = cifBddSpec.factory.one();
        cifBddSpec.initialComps = cifBddSpec.factory.one();
        cifBddSpec.initialLocs = cifBddSpec.factory.one();
        convertInit(spec, cifBddSpec, locPtrManager);
        BDD initialCompsAndLocs = cifBddSpec.initialComps.and(cifBddSpec.initialLocs);
        cifBddSpec.initialUnctrl = cifBddSpec.initialVars.and(initialCompsAndLocs);
        initialCompsAndLocs.free();

        if (cifBddSpec.settings.shouldTerminate.get()) {
            return cifBddSpec;
        }

        // Check and convert marker predicates.
        cifBddSpec.markedsComps = list();
        cifBddSpec.markedsLocs = list();
        cifBddSpec.markedComps = cifBddSpec.factory.one();
        cifBddSpec.markedLocs = cifBddSpec.factory.one();
        convertMarked(spec, cifBddSpec, locPtrManager);
        cifBddSpec.marked = cifBddSpec.markedComps.and(cifBddSpec.markedLocs);

        if (cifBddSpec.settings.shouldTerminate.get()) {
            return cifBddSpec;
        }

        // Initialize state plant invariants (predicates).
        cifBddSpec.plantInvsComps = list();
        cifBddSpec.plantInvsLocs = list();
        cifBddSpec.plantInvComps = cifBddSpec.factory.one();
        cifBddSpec.plantInvLocs = cifBddSpec.factory.one();

        // Initialize state requirement invariants (predicates).
        cifBddSpec.reqInvsComps = list();
        cifBddSpec.reqInvsLocs = list();
        cifBddSpec.reqInvComps = cifBddSpec.factory.one();
        cifBddSpec.reqInvLocs = cifBddSpec.factory.one();

        // Convert state invariants.
        convertStateInvs(spec, cifBddSpec, locPtrManager);

        // Determine state plant invariant for the system, combination of the state plant invariant for the components
        // and the state plant invariant for the locations of the automata.
        cifBddSpec.plantInv = cifBddSpec.plantInvComps.and(cifBddSpec.plantInvLocs);

        // Determine state requirement invariant for the system, combination of the state requirement invariant for the
        // components and the state requirement invariant for the locations of automata.
        cifBddSpec.reqInv = cifBddSpec.reqInvComps.and(cifBddSpec.reqInvLocs);

        if (cifBddSpec.settings.shouldTerminate.get()) {
            return cifBddSpec;
        }

        // Set combined predicate for initialization with state invariants.
        cifBddSpec.initialPlantInv = cifBddSpec.initialUnctrl.and(cifBddSpec.plantInv);
        cifBddSpec.initialInv = cifBddSpec.initialPlantInv.and(cifBddSpec.reqInv);

        // Set combined predicate for marking with state invariants.
        cifBddSpec.markedPlantInv = cifBddSpec.marked.and(cifBddSpec.plantInv);
        cifBddSpec.markedInv = cifBddSpec.markedPlantInv.and(cifBddSpec.reqInv);

        if (cifBddSpec.settings.shouldTerminate.get()) {
            return cifBddSpec;
        }

        // Initialize state/event plant exclusion conditions for events.
        cifBddSpec.stateEvtExclPlants = mapc(cifBddSpec.alphabet.size());
        cifBddSpec.stateEvtExclPlantLists = mapc(cifBddSpec.alphabet.size());
        for (Event event: cifBddSpec.alphabet) {
            cifBddSpec.stateEvtExclPlants.put(event, cifBddSpec.factory.one());
            cifBddSpec.stateEvtExclPlantLists.put(event, list());
        }

        // Initialize state/event requirement exclusion conditions for controllable events.
        cifBddSpec.stateEvtExclsReqAuts = mapc(cifBddSpec.controllables.size());
        cifBddSpec.stateEvtExclsReqInvs = mapc(cifBddSpec.controllables.size());
        for (Event event: cifBddSpec.controllables) {
            cifBddSpec.stateEvtExclsReqAuts.put(event, cifBddSpec.factory.one());
            cifBddSpec.stateEvtExclsReqInvs.put(event, cifBddSpec.factory.one());
        }

        // Initialize state/event requirement exclusion conditions for events.
        cifBddSpec.stateEvtExclReqs = mapc(cifBddSpec.alphabet.size());
        cifBddSpec.stateEvtExclReqLists = mapc(cifBddSpec.alphabet.size());
        for (Event event: cifBddSpec.alphabet) {
            cifBddSpec.stateEvtExclReqs.put(event, cifBddSpec.factory.one());
            cifBddSpec.stateEvtExclReqLists.put(event, list());
        }

        if (cifBddSpec.settings.shouldTerminate.get()) {
            return cifBddSpec;
        }

        // Convert state/event exclusion invariants.
        convertStateEvtExclInvs(spec, cifBddSpec, locPtrManager);
        if (cifBddSpec.settings.shouldTerminate.get()) {
            return cifBddSpec;
        }

        // Preconvert requirement automata, to enable treating them as plants from here on.
        preconvertReqAuts(requirements, reqAlphabets, cifBddSpec);
        if (cifBddSpec.settings.shouldTerminate.get()) {
            return cifBddSpec;
        }

        // Convert plant and requirement automata.
        convertPlantReqAuts(plants, requirements, plantAlphabets, reqAlphabets, locPtrManager, cifBddSpec);
        if (cifBddSpec.settings.shouldTerminate.get()) {
            return cifBddSpec;
        }

        // Restore original monitors of the requirement automata.
        for (Entry<Automaton, Monitors> entry: originalMonitors.entrySet()) {
            entry.getKey().setMonitors(entry.getValue());
        }
        originalMonitors = null;

        if (cifBddSpec.settings.shouldTerminate.get()) {
            return cifBddSpec;
        }

        // Add events and edges for input variables.
        addInputVariableEdges(cifBddSpec);
        if (cifBddSpec.settings.shouldTerminate.get()) {
            return cifBddSpec;
        }

        // Merge edges to the desired granularity.
        mergeEdges(cifBddSpec);
        if (cifBddSpec.settings.shouldTerminate.get()) {
            return cifBddSpec;
        }

        // Order the CIF/BDD edges.
        orderEdges(cifBddSpec);
        if (cifBddSpec.settings.shouldTerminate.get()) {
            return cifBddSpec;
        }

        // Check edge workset algorithm settings.
        checkEdgeWorksetAlgorithmSettings(cifBddSpec.settings);
        if (cifBddSpec.settings.shouldTerminate.get()) {
            return cifBddSpec;
        }

        // Return the conversion result, the CIF/BDD specification.
        return cifBddSpec;
    }

    /**
     * Converts a CIF variable to a CIF/BDD variable.
     *
     * @param var The CIF variable. Must be a {@link DiscVariable} or a {@link InputVariable}.
     * @return The CIF/BDD variable, or {@code null} if conversion failed due to a precondition not being satisfied.
     */
    private CifBddVariable convertTypedVar(Declaration var) {
        // Get normalized type of the variable.
        CifType type;
        if (var instanceof DiscVariable) {
            type = ((DiscVariable)var).getType();
        } else if (var instanceof InputVariable) {
            type = ((InputVariable)var).getType();
        } else {
            throw new RuntimeException("Unexpected variable: " + var);
        }
        type = normalizeType(type);

        // Get variable information, based on type.
        int count;
        int lower;
        int upper;
        if (type instanceof BoolType) {
            // Represent as 'int[0..1]', with '0' and '1' for 'false' and 'true', respectively.
            count = 2;
            lower = 0;
            upper = 1;
        } else if (type instanceof IntType) {
            // Check ranged integer type.
            IntType intType = (IntType)type;
            if (CifTypeUtils.isRangeless(intType)) {
                String msg = fmt(
                        "Unsupported variable \"%s\": variables with rangeless integer types are not supported.",
                        getAbsName(var));
                problems.add(msg);
                return null;
            }

            // Check range.
            lower = intType.getLower();
            upper = intType.getUpper();
            if (lower < 0) {
                String msg = fmt("Unsupported variable \"%s\": variables with integer type ranges that include "
                        + "negative values are not supported.", getAbsName(var));
                problems.add(msg);
                return null;
            }

            // Determine number of values.
            count = upper - lower + 1;
        } else if (type instanceof EnumType) {
            // Represent as 'int[0..n-1]' for an enumeration with 'n' literals.
            EnumDecl enumDecl = ((EnumType)type).getEnum();
            count = enumDecl.getLiterals().size();
            lower = 0;
            upper = count - 1;
        } else {
            // Unsupported.
            String msg = fmt("Unsupported variable \"%s\": variables of type \"%s\" are not supported.",
                    getAbsName(var), CifTextUtils.typeToStr(type));
            problems.add(msg);
            return null;
        }

        // Construct CIF/BDD variable.
        if (var instanceof DiscVariable) {
            DiscVariable discVar = (DiscVariable)var;
            return new CifBddDiscVariable(discVar, type, count, lower, upper);
        } else if (var instanceof InputVariable) {
            InputVariable inputVar = (InputVariable)var;
            return new CifBddInputVariable(inputVar, type, count, lower, upper);
        } else {
            throw new RuntimeException("Unexpected variable: " + var);
        }
    }

    /**
     * Creates a CIF/BDD location pointer variable for a CIF automaton.
     *
     * @param aut The CIF automaton.
     * @param var The dummy, internally-created location pointer variable for the CIF automaton. Does not have a type.
     * @return The CIF/BDD variable.
     */
    private CifBddVariable createLpVar(Automaton aut, DiscVariable var) {
        int count = aut.getLocations().size();
        Assert.check(count > 1);
        return new CifBddLocPtrVariable(aut, var);
    }

    /**
     * Orders the CIF/BDD variables. Also sets the {@link CifBddVariable#group group} of each CIF/BDD variable.
     *
     * @param cifBddSpec The CIF/BDD specification.
     * @param spec The CIF specification.
     */
    private void orderVars(CifBddSpec cifBddSpec, Specification spec) {
        // Skip ordering, including settings processing and debug output printing, if any variables failed to convert.
        if (Arrays.asList(cifBddSpec.variables).contains(null)) {
            return;
        }

        // Configure variable orderer.
        String varOrderTxt = cifBddSpec.settings.bddVarOrderAdvanced;
        List<VarOrdererInstance> parseResult;
        try {
            parseResult = new VarOrdererParser().parseString(varOrderTxt, "/in-memory.varorder", null, DebugMode.NONE);
        } catch (SyntaxException ex) {
            throw new InvalidOptionException("Invalid BDD variable ordering configuration.", ex);
        }

        VarOrdererTypeChecker typeChecker = new VarOrdererTypeChecker(Arrays.asList(cifBddSpec.variables),
                cifBddSpec.settings);
        VarOrderer varOrderer = typeChecker.typeCheck(parseResult);
        Assert.check(!typeChecker.hasWarning());
        if (varOrderer == null) {
            Assert.check(typeChecker.hasError());
            Assert.check(typeChecker.getProblems().size() == 1);
            InvalidOptionException ex = new InvalidOptionException(typeChecker.getProblems().get(0).toString());
            throw new InvalidOptionException("Invalid BDD variable ordering configuration.", ex);
        }

        // Skip ordering, including debug output printing, if no variables are present.
        int varCnt = cifBddSpec.variables.length;
        if (varCnt == 0) {
            return;
        }

        // Initialize to model variable order without interleaving.
        for (int i = 0; i < cifBddSpec.variables.length; i++) {
            cifBddSpec.variables[i].group = i;
        }

        // Print variable debugging information, before ordering.
        boolean dbgEnabled = cifBddSpec.settings.debugOutput.isEnabled();
        if (dbgEnabled) {
            debugCifVars(cifBddSpec);
        }

        // Only apply variable ordering if there are at least two variables (to order).
        if (cifBddSpec.variables.length < 2) {
            if (dbgEnabled) {
                cifBddSpec.settings.debugOutput.line();
                cifBddSpec.settings.debugOutput.line("Skipping variable ordering: only one variable present.");
                cifBddSpec.settings.debugOutput.line();
            }
            return;
        }

        // Create variable order helper, based on model order.
        List<CifBddVariable> varsInModelOrder = Collections.unmodifiableList(Arrays.asList(cifBddSpec.variables));
        VarOrderHelper helper = new VarOrderHelper(spec, varsInModelOrder, cifBddSpec.settings.debugOutput);

        // Get current variable order, which is model order.
        VarOrder curOrder = VarOrder.createFromOrderedVars(varsInModelOrder);
        List<CifBddVariable> varsInCurOrder = curOrder.getOrderedVars();

        // Create variable order data for input to first orderer.
        VarOrdererData data = new VarOrdererData(varsInModelOrder, curOrder, helper);

        // Get new variable order.
        if (dbgEnabled) {
            cifBddSpec.settings.debugOutput.line();
            cifBddSpec.settings.debugOutput.line("Applying variable ordering:");
        }
        VarOrdererData orderingResult = varOrderer.order(data, dbgEnabled, 1);
        VarOrder newOrder = orderingResult.varOrder;
        List<CifBddVariable> varsInNewOrder = newOrder.getOrderedVars();

        // Check sanity of current and new variable orders.
        Assert.check(curOrder.getVarOrder().stream().allMatch(grp -> !grp.isEmpty())); // No empty groups.
        Assert.check(newOrder.getVarOrder().stream().allMatch(grp -> !grp.isEmpty())); // No empty groups.
        Assert.areEqual(varsInCurOrder.size(), list2set(varsInCurOrder).size()); // No duplicates.
        Assert.areEqual(varsInNewOrder.size(), list2set(varsInNewOrder).size()); // No duplicates.
        Assert.areEqual(varsInCurOrder.size(), varsInNewOrder.size()); // Same number of variables.
        Assert.areEqual(list2set(varsInCurOrder), list2set(varsInNewOrder)); // Same variables.

        // Update the variable order of the CIF/BDD specification.
        cifBddSpec.variables = varsInNewOrder.toArray(n -> new CifBddVariable[n]);
        for (int i = 0; i < newOrder.getVarOrder().size(); i++) {
            List<CifBddVariable> group = newOrder.getVarOrder().get(i);
            for (CifBddVariable var: group) {
                var.group = i;
            }
        }

        // If the new order differs from the current order, print updated variable debugging information.
        if (dbgEnabled) {
            boolean orderChanged = !curOrder.equals(newOrder);
            cifBddSpec.settings.debugOutput.line();
            cifBddSpec.settings.debugOutput.line("Variable order %schanged.", orderChanged ? "" : "un");
            if (orderChanged) {
                debugCifVars(cifBddSpec);
            }
            cifBddSpec.settings.debugOutput.line();
        }
    }

    /**
     * Prints CIF variable information. Should only be invoked if debug output is enabled.
     *
     * @param cifBddSpec The CIF/BDD specification.
     */
    private static void debugCifVars(CifBddSpec cifBddSpec) {
        // Get variable counts.
        int cifVarCnt = cifBddSpec.variables.length;

        // Initialize grid and header.
        GridBox grid = new GridBox(cifVarCnt + 4, 9, 0, 2);
        grid.set(0, 0, "Nr");
        grid.set(0, 1, "Kind");
        grid.set(0, 2, "Type");
        grid.set(0, 3, "Name");
        grid.set(0, 4, "Group");
        grid.set(0, 5, "BDD vars");
        grid.set(0, 6, "CIF values");
        grid.set(0, 7, "BDD values");
        grid.set(0, 8, "Values used");

        // Fill grid with variable information.
        Set<Integer> groups = set();
        int totalBddVarCnt = 0;
        int totalUsedCnt = 0;
        int totalReprCnt = 0;
        for (int i = 0; i < cifVarCnt; i++) {
            // Get CIF/BDD variable.
            CifBddVariable var = cifBddSpec.variables[i];

            // Get type text.
            String typeTxt = var.getTypeText();
            if (typeTxt == null) {
                typeTxt = "n/a";
            }

            // Get some counts.
            int bddCnt = var.getBddVarCount();
            int usedCnt = var.count;
            int reprCnt = 1 << bddCnt;

            // Fill grid row.
            grid.set(i + 2, 0, str(i + 1));
            grid.set(i + 2, 1, var.getKindText());
            grid.set(i + 2, 2, typeTxt);
            grid.set(i + 2, 3, var.name);
            grid.set(i + 2, 4, str(var.group));
            grid.set(i + 2, 5, str(bddCnt) + " * 2");
            grid.set(i + 2, 6, str(usedCnt) + " * 2");
            grid.set(i + 2, 7, str(reprCnt) + " * 2");
            grid.set(i + 2, 8, getPercentageText(usedCnt, reprCnt));

            // Update totals.
            groups.add(var.group);
            totalBddVarCnt += bddCnt * 2;
            totalUsedCnt += usedCnt * 2;
            totalReprCnt += reprCnt * 2;
        }

        // Fill grid with totals.
        grid.set(cifVarCnt + 3, 0, "Total");
        grid.set(cifVarCnt + 3, 1, "");
        grid.set(cifVarCnt + 3, 2, "");
        grid.set(cifVarCnt + 3, 3, "");
        grid.set(cifVarCnt + 3, 4, str(groups.size()));
        grid.set(cifVarCnt + 3, 5, str(totalBddVarCnt));
        grid.set(cifVarCnt + 3, 6, str(totalUsedCnt));
        grid.set(cifVarCnt + 3, 7, str(totalReprCnt));
        grid.set(cifVarCnt + 3, 8, getPercentageText(totalUsedCnt, totalReprCnt));

        // Fill separation rows.
        GridBoxLayout layout = grid.computeLayout();
        for (int i = 0; i < layout.numCols; i++) {
            String bar = Strings.duplicate("-", layout.widths[i]);
            grid.set(1, i, bar);
            grid.set(cifVarCnt + 2, i, bar);
        }

        // Print the variable information, for debugging.
        cifBddSpec.settings.debugOutput.line();
        cifBddSpec.settings.debugOutput.line("CIF variables and location pointers:");
        for (String line: grid.getLines()) {
            cifBddSpec.settings.debugOutput.line("  " + line);
        }
    }

    /**
     * Returns the percentage of a part with respect to a total, as end-user readable text.
     *
     * @param part The part value.
     * @param total The total value.
     * @return The percentage as integer number in the range [0..100], followed by a {@code "%"} sign, and preceded by a
     *     {@code "~"} if the integer percentage is rounded from a non-integer percentage. If the percentage can not be
     *     computed, e.g. because 'total' is zero, the result is {@code "n/a"}.
     */
    private static String getPercentageText(int part, int total) {
        double percentage = (100.0 * part) / total;
        if (Double.isNaN(percentage)) {
            return "n/a";
        }
        String txt = fmt("%.0f", percentage);
        if ((int)Math.floor(percentage) != percentage) {
            txt = "~" + txt;
        }
        txt += "%";
        return txt;
    }

    /**
     * Creates and sets BDD {@link CifBddVariable#domain domains} for all the CIF/BDD variables.
     *
     * @param cifBddSpec The CIF/BDD specification.
     */
    private void createVarDomains(CifBddSpec cifBddSpec) {
        // Skip if no variables (due to earlier conversion error).
        int varCnt = cifBddSpec.variables.length;
        if (varCnt == 0) {
            return;
        }

        // If not ordered (due to earlier conversion error), set dummy domains, but of the correct size to prevent
        // errors later on.
        boolean ordered = true;
        for (int i = 0; i < varCnt; i++) {
            CifBddVariable var = cifBddSpec.variables[i];
            if (var == null || var.group == -1) {
                ordered = false;
                break;
            }
        }

        if (!ordered) {
            for (int i = 0; i < varCnt; i++) {
                CifBddVariable var = cifBddSpec.variables[i];
                if (var == null) {
                    continue;
                }
                int size = var.getDomainSize();
                var.domain = cifBddSpec.factory.extDomain(size);
                var.domainNew = cifBddSpec.factory.extDomain(size);
                var.group = i;
            }
            return;
        }

        // Make sure the CIF/BDD variable domain interleaving groups are ascending and continuous.
        int cur = 0;
        for (int i = 0; i < varCnt; i++) {
            int group = cifBddSpec.variables[i].group;
            if (group == cur) {
                continue;
            }
            if (group == cur + 1) {
                cur = group;
                continue;
            }
            Assert.fail(fmt("Invalid cur/group: %d/%d.", cur, group));
        }

        // Count number of CIF/BDD variables per group of interleaving domains.
        CifBddVariable lastVar = cifBddSpec.variables[varCnt - 1];
        int[] counts = new int[lastVar.group + 1];
        for (int i = 0; i < varCnt; i++) {
            counts[cifBddSpec.variables[i].group]++;
        }

        // Create and set domains, per group of interleaving domains.
        int offset = 0;
        for (int grpIdx = 0; grpIdx < counts.length; grpIdx++) {
            // Get domain sizes, for variables in the group.
            int grpVarCnt = counts[grpIdx];
            int[] sizes = new int[grpVarCnt * 2];
            for (int varIdx = 0; varIdx < grpVarCnt; varIdx++) {
                int size = cifBddSpec.variables[offset + varIdx].getDomainSize();
                sizes[(2 * varIdx) + 0] = size;
                sizes[(2 * varIdx) + 1] = size;
            }

            // Create domains.
            BDDDomain[] domains = cifBddSpec.factory.extDomain(sizes);

            // Set domains.
            for (int varIdx = 0; varIdx < grpVarCnt; varIdx++) {
                cifBddSpec.variables[offset + varIdx].domain = domains[(2 * varIdx) + 0];
                cifBddSpec.variables[offset + varIdx].domainNew = domains[(2 * varIdx) + 1];
            }

            // Proceed with next group of interleaving domains.
            offset += grpVarCnt;
        }
    }

    /**
     * Create auxiliary data for updates.
     *
     * @param cifBddSpec The CIF/BDD specification. Is modified in-place.
     * @see CifBddSpec#oldToNewVarsPairing
     * @see CifBddSpec#newToOldVarsPairing
     * @see CifBddSpec#varSetOld
     * @see CifBddSpec#varSetNew
     */
    private void createUpdateAuxiliaries(CifBddSpec cifBddSpec) {
        // Skip if earlier conversion failure.
        int domainCnt = cifBddSpec.factory.numberOfDomains();
        int cifVarCnt = cifBddSpec.variables.length;
        if (cifVarCnt * 2 != domainCnt) {
            return;
        }

        // oldToNewVarsPairing = 'x -> x+, y -> y+, z -> z+, ...'.
        // newToOldVarsPairing = 'x+ -> x, y+ -> y, z+ -> z, ...'.
        BDDDomain[] oldDomains = new BDDDomain[cifVarCnt];
        BDDDomain[] newDomains = new BDDDomain[cifVarCnt];
        for (int i = 0; i < cifVarCnt; i++) {
            oldDomains[i] = cifBddSpec.variables[i].domain;
            newDomains[i] = cifBddSpec.variables[i].domainNew;
        }
        cifBddSpec.oldToNewVarsPairing = cifBddSpec.factory.makePair();
        cifBddSpec.newToOldVarsPairing = cifBddSpec.factory.makePair();
        cifBddSpec.oldToNewVarsPairing.set(oldDomains, newDomains);
        cifBddSpec.newToOldVarsPairing.set(newDomains, oldDomains);

        if (cifBddSpec.settings.shouldTerminate.get()) {
            return;
        }

        // varSetOld = {x, y, z, ...}
        // varSetNew = {x+, y+, z+, ...}
        int bddVarCnt = cifBddSpec.factory.varNum();
        Assert.check(bddVarCnt % 2 == 0);
        int[] varIdxsOld = new int[bddVarCnt / 2];
        int[] varIdxsNew = new int[bddVarCnt / 2];
        int varIdx = 0;
        for (int i = 0; i < oldDomains.length; i++) {
            BDDDomain oldDomain = oldDomains[i];
            BDDDomain newDomain = newDomains[i];
            int[] domainVarIdxsOld = oldDomain.vars();
            int[] domainVarIdxsNew = newDomain.vars();
            System.arraycopy(domainVarIdxsOld, 0, varIdxsOld, varIdx, domainVarIdxsOld.length);
            System.arraycopy(domainVarIdxsNew, 0, varIdxsNew, varIdx, domainVarIdxsNew.length);
            varIdx += domainVarIdxsOld.length;
        }
        cifBddSpec.varSetOld = cifBddSpec.factory.makeSet(varIdxsOld);
        cifBddSpec.varSetNew = cifBddSpec.factory.makeSet(varIdxsNew);

        if (cifBddSpec.settings.shouldTerminate.get()) {
            return;
        }
    }

    /**
     * Converts initialization predicates from the components, initialization predicates from the locations of automata,
     * and the initial values of the variables.
     *
     * @param comp The component for which to convert initialization, recursively.
     * @param cifBddSpec The CIF/BDD specification to be updated with initialization information.
     * @param locPtrManager Location pointer manager.
     */
    private void convertInit(ComplexComponent comp, CifBddSpec cifBddSpec, LocationPointerManager locPtrManager) {
        // Initialization predicates of the component.
        for (Expression pred: comp.getInitials()) {
            // Convert.
            BDD initial;
            try {
                initial = convertPred(pred, true, cifBddSpec);
            } catch (UnsupportedPredicateException ex) {
                if (ex.expr != null) {
                    String msg = fmt("Unsupported %s: unsupported part \"%s\" of initialization predicate \"%s\": %s",
                            CifTextUtils.getComponentText1(comp), CifTextUtils.exprToStr(ex.expr),
                            CifTextUtils.exprToStr(pred), ex.getMessage());
                    problems.add(msg);
                }
                continue;
            }

            // Store.
            cifBddSpec.initialsComps.add(initial);
            cifBddSpec.initialComps = cifBddSpec.initialComps.andWith(initial.id());
        }

        // Initial values of discrete variables (automata only).
        if (comp instanceof Automaton) {
            for (Declaration cifDecl: comp.getDeclarations()) {
                // Skip all but discrete variables.
                if (!(cifDecl instanceof DiscVariable)) {
                    continue;
                }
                DiscVariable cifVar = (DiscVariable)cifDecl;

                // Get CIF/BDD variable. Skip if earlier precondition violation.
                int varIdx = getDiscVarIdx(cifBddSpec.variables, cifVar);
                if (varIdx == -1) {
                    continue;
                }
                CifBddVariable cifBddVar = cifBddSpec.variables[varIdx];
                Assert.check(cifBddVar instanceof CifBddDiscVariable);
                CifBddDiscVariable var = (CifBddDiscVariable)cifBddVar;

                // Get initial value expressions. Use 'null' to indicate any value in the CIF domain.
                List<Expression> values;
                if (cifVar.getValue() == null) {
                    // Default initial value.
                    CifType type = cifVar.getType();
                    values = list(CifValueUtils.getDefaultValue(type, null));
                } else if (cifVar.getValue().getValues().isEmpty()) {
                    // Any value in its domain.
                    values = null;
                } else {
                    // One or more specific initialization values.
                    values = cifVar.getValue().getValues();
                }

                // Create initialization predicate for the discrete variable.
                BDD pred;
                if (values == null) {
                    // Any value in its domain.
                    pred = BddUtils.getVarDomain(var, false, cifBddSpec.factory);
                } else {
                    // Specific values.
                    pred = cifBddSpec.factory.zero();
                    for (Expression value: values) {
                        // Case distinction on types of values.
                        if (var.type instanceof BoolType) {
                            // Convert right hand side (value to assign).
                            BDD valueBdd;
                            try {
                                valueBdd = convertPred(value, true, cifBddSpec);
                            } catch (UnsupportedPredicateException ex) {
                                // Add new problem, if not failed due to earlier problems.
                                if (ex.expr != null) {
                                    String msg = fmt(
                                            "Unsupported variable \"%s\": unsupported part \"%s\" of initial "
                                                    + "value \"%s\": %s",
                                            var.name, CifTextUtils.exprToStr(ex.expr), CifTextUtils.exprToStr(value),
                                            ex.getMessage());
                                    problems.add(msg);
                                }

                                // Set predicate to 'true' to prevent no initialization.
                                pred.free();
                                pred = cifBddSpec.factory.one();

                                // Proceed with next value to check it as well.
                                continue;
                            }

                            // Create BDD for the left hand side (variable to get a new value).
                            Assert.check(var.domain.varNum() == 1);
                            int varVar = var.domain.vars()[0];
                            BDD varBdd = cifBddSpec.factory.ithVar(varVar);

                            // Construct 'var = value' relation.
                            BDD relation = varBdd.biimpWith(valueBdd);

                            // Update initialization predicate for the variable.
                            pred = pred.orWith(relation);
                        } else {
                            // Convert value expression.
                            Supplier<String> partMsg = () -> fmt("initial value \"%s\" of variable \"%s\".",
                                    CifTextUtils.exprToStr(value), var.name);

                            CifBddBitVectorAndCarry valueRslt;
                            try {
                                valueRslt = convertExpr(value, true, cifBddSpec, false, partMsg);
                            } catch (UnsupportedPredicateException ex) {
                                // Add new problem, if not failed due to earlier problems.
                                if (ex.expr != null) {
                                    String msg = fmt(
                                            "Unsupported variable \"%s\": unsupported part \"%s\" of initial "
                                                    + "value \"%s\": %s",
                                            var.name, CifTextUtils.exprToStr(ex.expr), CifTextUtils.exprToStr(value),
                                            ex.getMessage());
                                    problems.add(msg);
                                }

                                // Set predicate to 'true' to prevent no initialization.
                                pred.free();
                                pred = cifBddSpec.factory.one();

                                // Proceed with next value to check it as well.
                                continue;
                            }
                            CifBddBitVector valueVec = valueRslt.vector;
                            Assert.check(valueRslt.carry.isZero());

                            // Create bit vector for the variable.
                            CifBddBitVector varVec = CifBddBitVector.createDomain(var.domain);

                            // Construct 'var = value' relation.
                            Assert.check(varVec.length() >= valueVec.length());
                            valueVec.resize(varVec.length());
                            BDD relation = varVec.equalTo(valueVec);
                            varVec.free();
                            valueVec.free();

                            // Update initialization predicate for the variable.
                            pred = pred.orWith(relation);
                        }
                    }
                }

                // Store initialization.
                cifBddSpec.initialsVars.set(varIdx, pred);
                cifBddSpec.initialVars = cifBddSpec.initialVars.andWith(pred.id());
            }
        }

        // Initialization predicates of locations (automata only).
        if (comp instanceof Automaton) {
            // Get automaton.
            Automaton aut = (Automaton)comp;

            // Combine initialization predicates from the locations.
            BDD autInit = cifBddSpec.factory.zero();
            for (Location loc: aut.getLocations()) {
                // Skip location without initialization predicates (implicit 'false').
                if (loc.getInitials().isEmpty()) {
                    continue;
                }

                // Convert initialization predicates of the location.
                BDD locInit = cifBddSpec.factory.one();
                List<Expression> locInits = loc.getInitials();
                try {
                    locInit = convertPreds(locInits, true, cifBddSpec);
                } catch (UnsupportedPredicateException ex) {
                    if (ex.expr != null) {
                        String msg = fmt(
                                "Unsupported %s: unsupported part \"%s\" of initialization predicate(s) \"%s\": %s",
                                CifTextUtils.getLocationText1(loc), CifTextUtils.exprToStr(ex.expr),
                                CifTextUtils.exprsToStr(locInits), ex.getMessage());
                        problems.add(msg);
                    }
                    continue;
                }

                // Add location predicate.
                BDD srcLocPred;
                try {
                    Expression srcLocRef = locPtrManager.createLocRef(loc);
                    srcLocPred = convertPred(srcLocRef, true, cifBddSpec);
                } catch (UnsupportedPredicateException ex) {
                    if (ex.expr != null) {
                        // Internally created predicate shouldn't fail conversion.
                        throw new RuntimeException(ex);
                    }
                    continue;
                }

                locInit = locInit.and(srcLocPred);

                // Combine with initialization predicates of other locations.
                autInit = autInit.or(locInit);
            }

            // Store.
            cifBddSpec.initialsLocs.add(autInit);
            cifBddSpec.initialLocs = cifBddSpec.initialLocs.andWith(autInit.id());
        }

        // Proceed recursively (groups only).
        if (comp instanceof Group) {
            for (Component child: ((Group)comp).getComponents()) {
                convertInit((ComplexComponent)child, cifBddSpec, locPtrManager);
            }
        }
    }

    /**
     * Converts marker predicates from the components and marker predicates from the locations of automata.
     *
     * @param comp The component for which to convert marking, recursively.
     * @param cifBddSpec The CIF/BDD specification to be updated with marking information.
     * @param locPtrManager Location pointer manager.
     */
    private void convertMarked(ComplexComponent comp, CifBddSpec cifBddSpec, LocationPointerManager locPtrManager) {
        // Marker predicates of the component.
        for (Expression pred: comp.getMarkeds()) {
            // Convert.
            BDD marked;
            try {
                marked = convertPred(pred, false, cifBddSpec);
            } catch (UnsupportedPredicateException ex) {
                if (ex.expr != null) {
                    String msg = fmt("Unsupported %s: unsupported part \"%s\" of marker predicate \"%s\": %s",
                            CifTextUtils.getComponentText1(comp), CifTextUtils.exprToStr(ex.expr),
                            CifTextUtils.exprToStr(pred), ex.getMessage());
                    problems.add(msg);
                }
                continue;
            }

            // Store.
            cifBddSpec.markedsComps.add(marked);
            cifBddSpec.markedComps = cifBddSpec.markedComps.andWith(marked.id());
        }

        // Marker predicates of locations (automata only).
        if (comp instanceof Automaton) {
            // Get automaton.
            Automaton aut = (Automaton)comp;

            // Combine marker predicates from the locations.
            BDD autMarked = cifBddSpec.factory.zero();
            for (Location loc: aut.getLocations()) {
                // Skip location without marker predicates (implicit 'false').
                if (loc.getMarkeds().isEmpty()) {
                    continue;
                }

                // Convert marker predicates of the location.
                BDD locMarked = cifBddSpec.factory.one();
                List<Expression> locMarkeds = loc.getMarkeds();
                try {
                    locMarked = convertPreds(locMarkeds, false, cifBddSpec);
                } catch (UnsupportedPredicateException ex) {
                    if (ex.expr != null) {
                        String msg = fmt("Unsupported %s: unsupported part \"%s\" of marker predicate(s) \"%s\": %s",
                                CifTextUtils.getLocationText1(loc), CifTextUtils.exprToStr(ex.expr),
                                CifTextUtils.exprsToStr(locMarkeds), ex.getMessage());
                        problems.add(msg);
                    }
                    continue;
                }

                // Add location predicate.
                BDD srcLocPred;
                try {
                    Expression srcLocRef = locPtrManager.createLocRef(loc);
                    srcLocPred = convertPred(srcLocRef, false, cifBddSpec);
                } catch (UnsupportedPredicateException ex) {
                    if (ex.expr != null) {
                        // Internally created predicate shouldn't fail conversion.
                        throw new RuntimeException(ex);
                    }
                    continue;
                }

                locMarked = locMarked.andWith(srcLocPred);

                // Combine with marker predicates of other locations.
                autMarked = autMarked.orWith(locMarked);
            }

            // Store.
            cifBddSpec.markedsLocs.add(autMarked);
            cifBddSpec.markedLocs = cifBddSpec.markedLocs.andWith(autMarked.id());
        }

        // Proceed recursively (groups only).
        if (comp instanceof Group) {
            for (Component child: ((Group)comp).getComponents()) {
                convertMarked((ComplexComponent)child, cifBddSpec, locPtrManager);
            }
        }
    }

    /**
     * Converts state invariants (predicates) from the components and the locations of automata.
     *
     * @param comp The component for which to convert state invariants (predicates), recursively.
     * @param cifBddSpec The CIF/BDD specification to be updated with state invariants (predicates) information.
     * @param locPtrManager Location pointer manager.
     */
    private void convertStateInvs(ComplexComponent comp, CifBddSpec cifBddSpec, LocationPointerManager locPtrManager) {
        // State invariants (predicates) of the component.
        for (Invariant inv: comp.getInvariants()) {
            // Skip non-state invariants.
            if (inv.getInvKind() != InvKind.STATE) {
                continue;
            }

            // Check kind.
            if (inv.getSupKind() != SupKind.PLANT && inv.getSupKind() != SupKind.REQUIREMENT) {
                String msg = fmt(
                        "Unsupported %s: for state invariants, only plant and requirement invariants are supported.",
                        CifTextUtils.getComponentText1(comp));
                problems.add(msg);
                continue;
            }

            // Convert.
            Expression pred = inv.getPredicate();
            BDD invComp;
            try {
                invComp = convertPred(pred, false, cifBddSpec);
            } catch (UnsupportedPredicateException ex) {
                if (ex.expr != null) {
                    String msg = fmt("Unsupported %s: unsupported part \"%s\" of state invariant \"%s\": %s",
                            CifTextUtils.getComponentText1(comp), CifTextUtils.exprToStr(ex.expr),
                            CifTextUtils.exprToStr(pred), ex.getMessage());
                    problems.add(msg);
                }
                continue;
            }

            // Store.
            switch (inv.getSupKind()) {
                case PLANT:
                    cifBddSpec.plantInvsComps.add(invComp);
                    cifBddSpec.plantInvComps = cifBddSpec.plantInvComps.andWith(invComp.id());
                    break;
                case REQUIREMENT:
                    cifBddSpec.reqInvsComps.add(invComp);
                    cifBddSpec.reqInvComps = cifBddSpec.reqInvComps.andWith(invComp.id());
                    break;
                default:
                    throw new RuntimeException("Unexpected kind: " + inv.getSupKind());
            }
        }

        // State invariants (predicates) of locations (automata only).
        if (comp instanceof Automaton) {
            // Get automaton.
            Automaton aut = (Automaton)comp;

            // Add state invariants (predicates) from the locations.
            for (Location loc: aut.getLocations()) {
                for (Invariant inv: loc.getInvariants()) {
                    // Skip non-state invariants.
                    if (inv.getInvKind() != InvKind.STATE) {
                        continue;
                    }

                    // Check kind.
                    if (inv.getSupKind() != SupKind.PLANT && inv.getSupKind() != SupKind.REQUIREMENT) {
                        String msg = fmt(
                                "Unsupported %s: for state invariants, only plant and requirement invariants are "
                                        + "supported.",
                                CifTextUtils.getLocationText1(loc));
                        problems.add(msg);
                        continue;
                    }

                    // Convert.
                    Expression pred = inv.getPredicate();
                    BDD invLoc;
                    try {
                        invLoc = convertPred(pred, false, cifBddSpec);
                    } catch (UnsupportedPredicateException ex) {
                        if (ex.expr != null) {
                            String msg = fmt("Unsupported %s: unsupported part \"%s\" of state invariant \"%s\": %s",
                                    CifTextUtils.getLocationText1(loc), CifTextUtils.exprToStr(ex.expr),
                                    CifTextUtils.exprToStr(pred), ex.getMessage());
                            problems.add(msg);
                        }
                        continue;
                    }

                    // Add location predicate (srcLocPred => locInv).
                    BDD srcLocPred;
                    try {
                        Expression srcLocRef = locPtrManager.createLocRef(loc);
                        srcLocPred = convertPred(srcLocRef, false, cifBddSpec);
                    } catch (UnsupportedPredicateException ex) {
                        if (ex.expr != null) {
                            // Internally created predicate shouldn't fail conversion.
                            throw new RuntimeException(ex);
                        }
                        continue;
                    }

                    invLoc = srcLocPred.not().orWith(invLoc);
                    srcLocPred.free();

                    // Store.
                    switch (inv.getSupKind()) {
                        case PLANT:
                            cifBddSpec.plantInvsLocs.add(invLoc);
                            cifBddSpec.plantInvLocs = cifBddSpec.plantInvLocs.andWith(invLoc.id());
                            break;
                        case REQUIREMENT:
                            cifBddSpec.reqInvsLocs.add(invLoc);
                            cifBddSpec.reqInvLocs = cifBddSpec.reqInvLocs.andWith(invLoc.id());
                            break;
                        default:
                            throw new RuntimeException("Unexpected kind: " + inv.getSupKind());
                    }
                }
            }
        }

        // Proceed recursively (groups only).
        if (comp instanceof Group) {
            for (Component child: ((Group)comp).getComponents()) {
                convertStateInvs((ComplexComponent)child, cifBddSpec, locPtrManager);
            }
        }
    }

    /**
     * Converts state/event exclusion invariants (both plant and requirement) from the components and the locations of
     * automata.
     *
     * @param comp The component for which to convert state/event exclusion invariants, recursively.
     * @param cifBddSpec The CIF/BDD specification to be updated with state/event exclusion invariants information.
     * @param locPtrManager Location pointer manager.
     */
    private void convertStateEvtExclInvs(ComplexComponent comp, CifBddSpec cifBddSpec,
            LocationPointerManager locPtrManager)
    {
        // State/event exclusion invariants of the component.
        for (Invariant inv: comp.getInvariants()) {
            // Skip state invariants.
            if (inv.getInvKind() == InvKind.STATE) {
                continue;
            }

            // Check kind.
            if (inv.getSupKind() != SupKind.PLANT && inv.getSupKind() != SupKind.REQUIREMENT) {
                String msg = fmt("Unsupported %s: for state/event exclusion invariants, only plant and requirement "
                        + "invariants are supported.", CifTextUtils.getComponentText1(comp));
                problems.add(msg);
                continue;
            }

            // Check that event is in the alphabet.
            Event event = ((EventExpression)inv.getEvent()).getEvent();
            if (!cifBddSpec.alphabet.contains(event)) {
                String msg = fmt(
                        "State/event exclusion invariant \"%s\" of %s has no effect, as event \"%s\" is not "
                                + "in the alphabet of any automaton.",
                        CifTextUtils.invToStr(inv, false), CifTextUtils.getComponentText2(comp),
                        CifTextUtils.getAbsName(event));
                cifBddSpec.settings.warnOutput.line(msg);

                // Skip the rest as we won't use this invariant any further.
                continue;
            }

            // Convert predicate.
            Expression pred = inv.getPredicate();
            BDD compInv;
            try {
                compInv = convertPred(pred, false, cifBddSpec);
            } catch (UnsupportedPredicateException ex) {
                if (ex.expr != null) {
                    String msg = fmt("Unsupported %s: unsupported part \"%s\" of invariant \"%s\": %s",
                            CifTextUtils.getComponentText1(comp), CifTextUtils.exprToStr(ex.expr),
                            CifTextUtils.invToStr(inv, false), ex.getMessage());
                    problems.add(msg);
                }
                continue;
            }

            // Adapt predicate for the kind of invariant.
            if (inv.getInvKind() == InvKind.EVENT_DISABLES) {
                BDD compInvNot = compInv.not();
                compInv.free();
                compInv = compInvNot;
            }

            // Store copies of the BDD.
            switch (inv.getSupKind()) {
                case PLANT:
                    storeStateEvtExclInv(cifBddSpec.stateEvtExclPlantLists, event, compInv.id());
                    conjunctAndStoreStateEvtExclInv(cifBddSpec.stateEvtExclPlants, event, compInv.id());
                    break;
                case REQUIREMENT:
                    storeStateEvtExclInv(cifBddSpec.stateEvtExclReqLists, event, compInv.id());
                    conjunctAndStoreStateEvtExclInv(cifBddSpec.stateEvtExclReqs, event, compInv.id());
                    if (Boolean.TRUE.equals(event.getControllable())) {
                        conjunctAndStoreStateEvtExclInv(cifBddSpec.stateEvtExclsReqInvs, event, compInv.id());
                    }
                    break;
                default:
                    throw new RuntimeException("Unexpected kind: " + inv.getSupKind());
            }

            // Free the original BDD.
            compInv.free();
        }

        // State/event exclusion requirement invariants of locations (automata only).
        if (comp instanceof Automaton) {
            // Get automaton.
            Automaton aut = (Automaton)comp;

            // Add state/event exclusion invariants from the locations.
            for (Location loc: aut.getLocations()) {
                for (Invariant inv: loc.getInvariants()) {
                    // Skip state invariants.
                    if (inv.getInvKind() == InvKind.STATE) {
                        continue;
                    }

                    // Check kind.
                    if (inv.getSupKind() != SupKind.PLANT && inv.getSupKind() != SupKind.REQUIREMENT) {
                        String msg = fmt("Unsupported %s: for state/event exclusion invariants, only plant and "
                                + "requirement invariants are supported.", CifTextUtils.getLocationText1(loc));
                        problems.add(msg);
                        continue;
                    }

                    // Check that event is in the alphabet.
                    Event event = ((EventExpression)inv.getEvent()).getEvent();
                    if (!cifBddSpec.alphabet.contains(event)) {
                        String msg = fmt(
                                "State/event exclusion invariant \"%s\" of %s has no effect, as event \"%s\" is not in "
                                        + "the alphabet of any automaton.",
                                CifTextUtils.invToStr(inv, false), CifTextUtils.getLocationText2(loc),
                                CifTextUtils.getAbsName(event));
                        cifBddSpec.settings.warnOutput.line(msg);

                        // Skip the rest as we won't use this invariant any further.
                        continue;
                    }

                    // Convert predicate.
                    Expression pred = inv.getPredicate();
                    BDD locInv;
                    try {
                        locInv = convertPred(pred, false, cifBddSpec);
                    } catch (UnsupportedPredicateException ex) {
                        if (ex.expr != null) {
                            String msg = fmt("Unsupported %s: unsupported part \"%s\" of invariant \"%s\": %s",
                                    CifTextUtils.getLocationText1(loc), CifTextUtils.exprToStr(ex.expr),
                                    CifTextUtils.invToStr(inv, false), ex.getMessage());
                            problems.add(msg);
                        }
                        continue;
                    }

                    // Get location predicate (srcLocPred => locInv).
                    BDD srcLocPred;
                    try {
                        Expression srcLocRef = locPtrManager.createLocRef(loc);
                        srcLocPred = convertPred(srcLocRef, false, cifBddSpec);
                    } catch (UnsupportedPredicateException ex) {
                        if (ex.expr != null) {
                            // Internally created predicate shouldn't fail conversion.
                            throw new RuntimeException(ex);
                        }
                        continue;
                    }

                    locInv = srcLocPred.not().orWith(locInv);
                    srcLocPred.free();

                    // Adapt predicate for the kind of invariant.
                    if (inv.getInvKind() == InvKind.EVENT_DISABLES) {
                        BDD locInvNot = locInv.not();
                        locInv.free();
                        locInv = locInvNot;
                    }

                    // Store copies of the BDD.
                    switch (inv.getSupKind()) {
                        case PLANT:
                            storeStateEvtExclInv(cifBddSpec.stateEvtExclPlantLists, event, locInv.id());
                            conjunctAndStoreStateEvtExclInv(cifBddSpec.stateEvtExclPlants, event, locInv.id());
                            break;
                        case REQUIREMENT:
                            storeStateEvtExclInv(cifBddSpec.stateEvtExclReqLists, event, locInv.id());
                            conjunctAndStoreStateEvtExclInv(cifBddSpec.stateEvtExclReqs, event, locInv.id());
                            if (Boolean.TRUE.equals(event.getControllable())) {
                                conjunctAndStoreStateEvtExclInv(cifBddSpec.stateEvtExclsReqInvs, event, locInv.id());
                            }
                            break;
                        default:
                            throw new RuntimeException("Unexpected kind: " + inv.getSupKind());
                    }

                    // Free the original BDD.
                    locInv.free();
                }
            }
        }

        // Proceed recursively (groups only).
        if (comp instanceof Group) {
            for (Component child: ((Group)comp).getComponents()) {
                convertStateEvtExclInvs((ComplexComponent)child, cifBddSpec, locPtrManager);
            }
        }
    }

    /**
     * Adds the given state/event exclusion invariant to the state/event exclusion invariants collected so far for the
     * supplied event. The invariants are retrieved from the supplied mapping.
     *
     * @param eventInvs Mapping from events to their corresponding invariants.
     * @param event The event to use as a key.
     * @param inv The invariant to add.
     */
    private void storeStateEvtExclInv(Map<Event, List<BDD>> eventInvs, Event event, BDD inv) {
        List<BDD> invs = eventInvs.get(event);
        invs.add(inv);
    }

    /**
     * Combines the given state/event exclusion invariant with the state/event exclusion invariants collected so far for
     * the supplied event. The invariants are combined, using conjunction. The invariants (as a BDD) are retrieved from
     * the supplied mapping.
     *
     * @param eventInvs Mapping from events to their corresponding invariants.
     * @param event The event to use as a key.
     * @param inv The invariant to combine.
     */
    private void conjunctAndStoreStateEvtExclInv(Map<Event, BDD> eventInvs, Event event, BDD inv) {
        BDD invs = eventInvs.get(event);
        invs = invs.andWith(inv);
        eventInvs.put(event, invs);
    }

    /**
     * Preconvert requirement automata, to enable treating them as monitor plants in the CIF/BDD representation.
     *
     * @param requirements The requirement automata.
     * @param alphabets Per requirement automaton, all the alphabets.
     * @param cifBddSpec The CIF/BDD specification.
     */
    private void preconvertReqAuts(List<Automaton> requirements, List<Alphabets> alphabets, CifBddSpec cifBddSpec) {
        // Initialization.
        originalMonitors = mapc(requirements.size());

        // For CIF/BDD, requirement automata are treated as plants that monitor the entire alphabet. They thus don't
        // restrict anything guard-wise. We add additional state/event exclusion requirements to restrict the behavior
        // to what the original requirement automaton allowed.
        for (int i = 0; i < requirements.size(); i++) {
            // Get requirement automaton and alphabets.
            Automaton requirement = requirements.get(i);
            Alphabets reqAlphabets = alphabets.get(i);

            // Add state/event exclusion requirements for non-monitored events. Problems have already been reported in
            // case of send/receive usage in the requirements.
            for (Event event: reqAlphabets.syncAlphabet) {
                // Skip events that are already monitored.
                if (reqAlphabets.moniAlphabet.contains(event)) {
                    continue;
                }

                // Get combined guard.
                Expression cifGuard = CifGuardUtils.mergeGuards(requirement, event, EdgeEventImpl.class,
                        LocRefExprCreator.DEFAULT);

                // Convert guard.
                BDD cifBddGuard;
                try {
                    cifBddGuard = convertPred(cifGuard, false, cifBddSpec);
                } catch (UnsupportedPredicateException ex) {
                    if (ex.expr != null) {
                        String msg = fmt("Unsupported %s: unsupported part \"%s\" of combined guard \"%s\": %s",
                                CifTextUtils.getComponentText1(requirement), CifTextUtils.exprToStr(ex.expr),
                                CifTextUtils.exprToStr(cifGuard), ex.getMessage());
                        problems.add(msg);
                    }
                    continue;
                }

                // Add guard as state/event exclusion requirement for the event.
                storeStateEvtExclInv(cifBddSpec.stateEvtExclReqLists, event, cifBddGuard.id());
                conjunctAndStoreStateEvtExclInv(cifBddSpec.stateEvtExclReqs, event, cifBddGuard.id());

                if (Boolean.TRUE.equals(event.getControllable())) {
                    conjunctAndStoreStateEvtExclInv(cifBddSpec.stateEvtExclsReqAuts, event, cifBddGuard.id());
                }

                cifBddGuard.free();
            }

            // Change requirement automaton to monitor all events. Skip this if the alphabet is empty, as we then get a
            // warning that we monitor an empty alphabet, when the input specification is converted to the output
            // specification, saved on disk, and used with other tools.
            if (reqAlphabets.syncAlphabet.isEmpty()) {
                // No alphabet, so shouldn't monitor anything. It may however already monitor the entire (empty)
                // alphabet. If so, just leave that as is.
            } else {
                // Store the original monitors, to be able to restore them later on.
                originalMonitors.put(requirement, requirement.getMonitors());

                // Monitor all events in the alphabet.
                requirement.setMonitors(newMonitors());
                reqAlphabets.moniAlphabet = copy(reqAlphabets.syncAlphabet);
            }
        }
    }

    /**
     * Converts the plant and requirement automata, to a single linearized CIF/BDD specification.
     *
     * @param plants The plant automata of the specification.
     * @param requirements The requirement automata.
     * @param plantAlphabets Per plant automaton, all the alphabets.
     * @param reqAlphabets Per requirement automaton, all the alphabets.
     * @param locPtrManager Location pointer manager.
     * @param cifBddSpec The CIF/BDD specification to be updated.
     */
    private void convertPlantReqAuts(List<Automaton> plants, List<Automaton> requirements,
            List<Alphabets> plantAlphabets, List<Alphabets> reqAlphabets, CifBddLocationPointerManager locPtrManager,
            CifBddSpec cifBddSpec)
    {
        // Combine information about plants and requirements.
        List<Automaton> automata = concat(plants, requirements);
        List<Alphabets> alphabets = concat(plantAlphabets, reqAlphabets);

        // Check no 'tau' edges.
        boolean tauOk = checkNoTauEdges(automata);

        // Add linearized edges.
        if (tauOk) {
            // Linearize edges for all events in the alphabet.
            // Must match a similar call to linearize edges in `LinearizedHyperEdgeCreator'.
            List<Edge> cifEdges = list();
            LinearizeProduct.linearizeEdges(automata, alphabets, set2list(cifBddSpec.alphabet), locPtrManager, false,
                    true, cifEdges);

            // Create and add CIF/BDD edges.
            cifBddSpec.edges = listc(cifEdges.size());
            cifBddSpec.eventEdges = mapc(cifBddSpec.alphabet.size());
            for (Edge cifEdge: cifEdges) {
                // Check for termination.
                if (cifBddSpec.settings.shouldTerminate.get()) {
                    break;
                }

                // Create edge.
                CifBddEdge cifBddEdge = new CifBddEdge(cifBddSpec);
                cifBddEdge.edges = list(cifEdge);

                // Set event.
                Assert.check(cifEdge.getEvents().size() == 1);
                EdgeEvent edgeEvent = first(cifEdge.getEvents());
                Event event = CifEventUtils.getEventFromEdgeEvent(edgeEvent);
                cifBddEdge.event = event;

                // Add edge.
                cifBddSpec.edges.add(cifBddEdge);
                List<CifBddEdge> cifBddEdges = cifBddSpec.eventEdges.get(event);
                if (cifBddEdges == null) {
                    cifBddEdges = list();
                    cifBddSpec.eventEdges.put(event, cifBddEdges);
                }
                cifBddEdges.add(cifBddEdge);

                // Convert and set guards.
                BDD guard;
                try {
                    guard = convertPreds(cifEdge.getGuards(), false, cifBddSpec);
                } catch (UnsupportedPredicateException ex) {
                    if (ex.expr != null) {
                        String msg = fmt("Unsupported linearized guard: unsupported part \"%s\" of guard(s) \"%s\": %s",
                                CifTextUtils.exprToStr(ex.expr), CifTextUtils.exprsToStr(cifEdge.getGuards()),
                                ex.getMessage());
                        problems.add(msg);
                    }

                    // Set dummy guard to allow continuing. Use 'false' to avoid non-determinism check to give false
                    // positives.
                    guard = cifBddSpec.factory.zero();
                }

                cifBddEdge.guard = guard;
                cifBddEdge.origGuard = guard.id();

                // Convert and set assignments.
                List<Update> updates = cifEdge.getUpdates();
                convertUpdates(updates, cifBddEdge, locPtrManager, cifBddSpec);
            }

            if (cifBddSpec.settings.shouldTerminate.get()) {
                return;
            }

            // Check for non-determinism of controllable events.
            checkNonDeterminism(cifBddSpec.edges);
        }
    }

    /**
     * Checks the given automata, to make sure they don't have any 'tau' edges. Any 'tau' edges are reported as
     * {@link #problems}.
     *
     * @param automata The automata to check.
     * @return Whether the automata have no 'tau' edges ({@code true}) or do have 'tau' edges ({@code false}).
     */
    private boolean checkNoTauEdges(List<Automaton> automata) {
        boolean tauOk = true;
        for (Automaton aut: automata) {
            for (Location loc: aut.getLocations()) {
                for (Edge edge: loc.getEdges()) {
                    if (edge.getEvents().isEmpty()) {
                        String msg = fmt("Unsupported %s: edges without events (implicitly event \"tau\") "
                                + "are not supported.", CifTextUtils.getLocationText1(loc));
                        problems.add(msg);
                        tauOk = false;
                    }

                    for (EdgeEvent edgeEvent: edge.getEvents()) {
                        Expression eventRef = edgeEvent.getEvent();
                        if (eventRef instanceof TauExpression) {
                            String msg = fmt("Unsupported %s: edges with \"tau\" events are not supported.",
                                    CifTextUtils.getLocationText1(loc));
                            problems.add(msg);
                            tauOk = false;
                        }
                    }
                }
            }
        }
        return tauOk;
    }

    /**
     * Check CIF/BDD edges to make sure there is no non-determinism for controllable events. Non-determinism by means of
     * multiple outgoing edges for the same event, with overlapping guards, is not supported. An external supervisor
     * can't force the correct edge to be taken, if only the updates (includes location pointer variable assignment for
     * target location) are different. For uncontrollable events non-determinism is not a problem, as the supervisor
     * won't restrict edges for uncontrollable events.
     *
     * @param edges The CIF/BDD edges (self loops). May include edges for both controllable and uncontrollable events.
     */
    private void checkNonDeterminism(List<CifBddEdge> edges) {
        // Initialize conflict information.
        Map<Event, BDD> eventGuards = map();
        Set<Event> conflicts = setc(0);

        // Check edges for conflicts (non-determinism).
        for (CifBddEdge edge: edges) {
            // Skip uncontrollable events. Also skip events without controllability (is already previously reported).
            Event evt = edge.event;
            Boolean controllable = evt.getControllable();
            if (controllable == null || !controllable) {
                continue;
            }

            // Skip events already found to have conflicting edges.
            if (conflicts.contains(evt)) {
                continue;
            }

            // Get guards so far and new guard of the edge.
            BDD curGuard = eventGuards.get(evt);
            BDD newGuard = edge.guard;

            // Check for overlap.
            if (curGuard == null) {
                // First edge for this event.
                eventGuards.put(evt, newGuard.id());
            } else {
                BDD overlap = curGuard.and(newGuard);
                if (overlap.isZero()) {
                    // No overlap, update guard so far.
                    eventGuards.put(evt, curGuard.orWith(newGuard.id()));
                } else {
                    // Overlap. Conflict found.
                    conflicts.add(evt);
                }
                overlap.free();
            }
        }

        // Clean up.
        for (BDD guard: eventGuards.values()) {
            guard.free();
        }

        // Report conflicts.
        for (Event conflict: conflicts) {
            // Get edges for the event.
            List<CifBddEdge> eventEdges = list();
            for (CifBddEdge edge: edges) {
                if (edge.event != conflict) {
                    continue;
                }
                eventEdges.add(edge);
            }

            // Partition guards into non-overlapping groups.
            List<List<CifBddEdge>> groups = groupOnGuardOverlap(eventEdges);

            // Get guard texts per group with overlap.
            List<String> guardsTxts = list();
            for (List<CifBddEdge> group: groups) {
                // Only overlap in the group if at least two edges.
                if (group.size() < 2) {
                    continue;
                }

                // Add guards text for this group.
                List<String> guardTxts = list();
                for (CifBddEdge edge: group) {
                    Assert.check(edge.edges.size() == 1); // No CIF/BDD edges have been merged yet.
                    List<Expression> guards = first(edge.edges).getGuards();
                    String guardsTxt;
                    if (guards.isEmpty()) {
                        guardsTxt = "true";
                    } else {
                        guardsTxt = CifTextUtils.exprsToStr(guards);
                    }
                    guardTxts.add("\"" + guardsTxt + "\"");
                }
                Assert.check(!guardTxts.isEmpty());
                guardsTxts.add(String.join(", ", guardTxts));
            }
            Assert.check(!guardsTxts.isEmpty());

            // Get groups text.
            String groupsTxt;
            if (guardsTxts.size() == 1) {
                groupsTxt = " " + guardsTxts.get(0) + ".";
            } else {
                for (int i = 0; i < guardsTxts.size(); i++) {
                    String txt = guardsTxts.get(i);
                    txt = fmt("\n    Group %d: %s", i + 1, txt);
                    guardsTxts.set(i, txt);
                }
                groupsTxt = String.join("", guardsTxts);
            }

            // Report conflict.
            String msg = fmt("Unsupported linearized edges: non-determinism detected for edges of controllable "
                    + "event \"%s\" with overlapping guards:%s", getAbsName(conflict), groupsTxt);
            problems.add(msg);
        }
    }

    /**
     * Group the edges with overlapping guards. That is, partition the edges into groups with non-overlapping guards.
     *
     * @param edges The edges.
     * @return The groups of edges.
     */
    private static List<List<CifBddEdge>> groupOnGuardOverlap(List<CifBddEdge> edges) {
        // Initialize to one edge per group.
        List<List<CifBddEdge>> groups = listc(edges.size());
        for (int i = 0; i < edges.size(); i++) {
            groups.add(list(edges.get(i)));
        }

        // Merge groups with overlapping guards. For each group, we merge with all overlapping groups that come after
        // it.
        for (int i = 0; i < groups.size(); i++) {
            // All groups start with exactly one edge, so get that guard.
            Assert.check(groups.get(i).size() == 1);
            BDD curGuard = groups.get(i).get(0).guard.id();

            // Process all groups that come after the current one.
            boolean changed = true;
            while (changed) {
                changed = false;

                for (int j = i + 1; j < groups.size(); j++) {
                    // All groups start with exactly one edge, so get that guard.
                    Assert.check(groups.get(j).size() == 1);
                    BDD newGuard = groups.get(j).get(0).guard;

                    // If disjoint (no overlap), groups don't need to be merged.
                    BDD overlapPred = curGuard.and(newGuard);
                    boolean disjoint = overlapPred.isZero();
                    overlapPred.free();
                    if (disjoint) {
                        continue;
                    }

                    // Overlap detected. Merge groups.
                    changed = true;
                    groups.get(i).add(groups.get(j).get(0));
                    groups.remove(j);

                    curGuard = curGuard.andWith(newGuard.id());
                }
            }

            // Cleanup.
            curGuard.free();
        }

        // Return the disjoint groups.
        return groups;
    }

    /**
     * Converts CIF updates to CIF/BDD updates.
     *
     * @param updates The CIF updates.
     * @param cifBddEdge The CIF/BDD edge in which to store the CIF/BDD updates. Is modified in-place.
     * @param locPtrManager Location pointer manager.
     * @param cifBddSpec The CIF/BDD specification.
     */
    private void convertUpdates(List<Update> updates, CifBddEdge cifBddEdge, CifBddLocationPointerManager locPtrManager,
            CifBddSpec cifBddSpec)
    {
        // Initialization.
        List<Assignment> assignments = listc(updates.size());
        boolean[] assigned = new boolean[cifBddSpec.variables.length];

        // Convert separate updates, and merge to form the update relation and runtime error predicate.
        BDD relation = cifBddSpec.factory.one();
        BDD error = cifBddSpec.factory.zero();
        for (Update update: updates) {
            Pair<BDD, BDD> rslt = convertUpdate(update, assignments, assigned, locPtrManager, cifBddSpec);
            if (cifBddSpec.settings.shouldTerminate.get()) {
                return;
            }

            if (rslt != null) {
                BDD updateRelation = rslt.left;
                relation = relation.andWith(updateRelation);

                BDD updateError = rslt.right;
                error = error.orWith(updateError);
            }

            if (cifBddSpec.settings.shouldTerminate.get()) {
                return;
            }
        }

        // Add relations to assure variables not being assigned don't change, i.e. won't jump arbitrarily.
        //
        // We go through the variables in the reverse order of the variable order. This generally ensures the best
        // performance, as new updates are added 'on top' of existing updates, allowing reuse of the existing BDDs,
        // rather than needing to recreate them.
        for (int i = assigned.length - 1; i >= 0; i--) {
            // If assigned, skip variable.
            if (assigned[i]) {
                continue;
            }

            // If conversion of variable failed, skip it.
            CifBddVariable var = cifBddSpec.variables[i];
            if (var == null) {
                continue;
            }

            // Unassigned, add 'x = x+' predicate.
            CifBddBitVector vectorOld = CifBddBitVector.createDomain(var.domain);
            CifBddBitVector vectorNew = CifBddBitVector.createDomain(var.domainNew);
            BDD unchangedRelation = vectorOld.equalTo(vectorNew);
            relation = relation.andWith(unchangedRelation);
            vectorOld.free();
            vectorNew.free();
        }

        // Store data for the updates.
        cifBddEdge.assignments = list(assignments);
        cifBddEdge.update = relation;
        cifBddEdge.error = error;
    }

    /**
     * Converts a CIF update to an update predicate and runtime error predicate.
     *
     * @param update The CIF update to convert.
     * @param assignments The assignments converted so far. Is modified in-place.
     * @param assigned Per CIF/BDD variable, whether it has been assigned on this edge. Is modified in-place.
     * @param locPtrManager Location pointer manager.
     * @param cifBddSpec The CIF/BDD specification.
     * @return The update relation and runtime error predicate. May be {@code null} if the update can't be converted due
     *     to a precondition violation.
     */
    private Pair<BDD, BDD> convertUpdate(Update update, List<Assignment> assignments, boolean[] assigned,
            CifBddLocationPointerManager locPtrManager, CifBddSpec cifBddSpec)
    {
        // Make sure it is not a conditional update ('if' update).
        if (update instanceof IfUpdate) {
            String msg = "Unsupported update: conditional updates ('if' updates) are not supported.";
            problems.add(msg);
            return null;
        }
        Assignment asgn = (Assignment)update;

        // Store assignment.
        assignments.add(asgn);

        // Make sure a discrete variable is assigned.
        Expression addr = asgn.getAddressable();
        if (addr instanceof TupleExpression) {
            String msg = "Unsupported update: multi-assignments are not supported.";
            problems.add(msg);
            return null;
        } else if (addr instanceof ProjectionExpression) {
            String msg = "Unsupported update: partial variable assignments are not supported.";
            problems.add(msg);
            return null;
        } else if (addr instanceof ContVariableExpression) {
            String msg = "Unsupported update: assignments to continuous variables are not supported.";
            problems.add(msg);
            return null;
        }

        // Get assigned variable.
        DiscVariable cifVar = ((DiscVariableExpression)addr).getVariable();

        // Special case for location pointer variable assignments created during linearization. Note that location
        // pointers are only created for automata with more than one location, and updates are only created for non self
        // loop edges. Since automata with one location have only self loops, automata for which location pointer
        // updates are created also have location pointer variables.
        Automaton cifAut = locPtrManager.getAutomaton(cifVar);
        if (cifAut != null) {
            // Get CIF/BDD variable.
            int varIdx = getLpVarIdx(cifBddSpec.variables, cifAut);
            if (varIdx == -1) {
                return null;
            }
            CifBddVariable var = cifBddSpec.variables[varIdx];
            Assert.check(var instanceof CifBddLocPtrVariable);

            // Mark variable as assigned.
            Assert.check(!assigned[varIdx]);
            assigned[varIdx] = true;

            // Get 0-based location index, which is also the bit index.
            Assert.check(asgn.getValue() instanceof IntExpression);
            int locIdx = ((IntExpression)asgn.getValue()).getValue();

            // Create and return assignment relation 'lp+ = locIdx'. The location always fits within the domain of the
            // location pointer variable, so there is no error predicate.
            CifBddBitVector varVector = CifBddBitVector.createDomain(var.domainNew);
            CifBddBitVector locVector = CifBddBitVector.createInt(cifBddSpec.factory, locIdx);
            Assert.check(locVector.length() <= varVector.length());
            locVector.resize(varVector.length());
            BDD relation = varVector.equalTo(locVector);
            varVector.free();
            locVector.free();
            return pair(relation, cifBddSpec.factory.zero());
        }

        // Normal case: assignment originating from original CIF model.
        int varIdx = getTypedVarIdx(cifBddSpec.variables, cifVar);
        if (varIdx == -1) {
            return null;
        }
        CifBddVariable cifBddVar = cifBddSpec.variables[varIdx];
        Assert.check(cifBddVar instanceof CifBddTypedVariable);
        CifBddTypedVariable var = (CifBddTypedVariable)cifBddVar;

        // Mark variable as assigned.
        Assert.check(!assigned[varIdx]);
        assigned[varIdx] = true;

        // Case distinction on types of values.
        if (var.type instanceof BoolType) {
            // Convert right hand side (value to assign).
            Expression rhsExpr = asgn.getValue();
            BDD rhsBdd;
            try {
                rhsBdd = convertPred(rhsExpr, false, cifBddSpec);
            } catch (UnsupportedPredicateException ex) {
                // Add new problem, if not failed due to earlier problems.
                if (ex.expr != null) {
                    String msg = fmt("Unsupported assignment: unsupported part \"%s\" of assignment \"%s := %s\": %s",
                            CifTextUtils.exprToStr(ex.expr), CifTextUtils.exprToStr(addr),
                            CifTextUtils.exprToStr(rhsExpr), ex.getMessage());
                    problems.add(msg);
                }

                // Return identity values.
                return pair(cifBddSpec.factory.one(), cifBddSpec.factory.zero());
            }

            // Create BDD for the left hand side (variable to get a new value).
            Assert.check(var.domainNew.varNum() == 1);
            int lhsVar = var.domainNew.vars()[0];
            BDD lhsBdd = cifBddSpec.factory.ithVar(lhsVar);

            // Construct 'lhs+ = rhs' relation.
            BDD relation = lhsBdd.biimpWith(rhsBdd);

            // Return the full relation.
            return pair(relation, cifBddSpec.factory.zero());
        } else {
            // Convert right hand side (value to assign).
            Expression rhsExpr = asgn.getValue();
            Supplier<String> partMsg = () -> fmt("assignment \"%s := %s\"", CifTextUtils.exprToStr(addr),
                    CifTextUtils.exprToStr(rhsExpr));

            CifBddBitVectorAndCarry rhsRslt;
            try {
                rhsRslt = convertExpr(rhsExpr, false, cifBddSpec, true, partMsg);
            } catch (UnsupportedPredicateException ex) {
                // Add new problem, if not failed due to earlier problems.
                if (ex.expr != null) {
                    String msg = fmt("Unsupported assignment: unsupported part \"%s\" of assignment \"%s := %s\": %s",
                            CifTextUtils.exprToStr(ex.expr), CifTextUtils.exprToStr(addr),
                            CifTextUtils.exprToStr(rhsExpr), ex.getMessage());
                    problems.add(msg);
                }

                // Return identity values.
                return pair(cifBddSpec.factory.one(), cifBddSpec.factory.zero());
            }
            CifBddBitVector rhsVec = rhsRslt.vector;

            // The runtime error predicate resulting from the right hand side is used to initialize the runtime error
            // predicate of the assignment.
            BDD error = rhsRslt.carry;

            // Create bit vector for the left hand side (variable to get a new value).
            CifBddBitVector lhsVec = CifBddBitVector.createDomain(var.domainNew);

            // Construct 'lhs+ = rhs' relation. By resizing both vectors to be equal size, a smaller rhs (in number of
            // bits) is properly handled, as 'false' bits are added for the part of the rhs that is missing with respect
            // to the lhs.
            int lhsLen = lhsVec.length();
            int len = Math.max(lhsVec.length(), rhsVec.length());
            lhsVec.resize(len);
            rhsVec.resize(len);
            BDD relation = lhsVec.equalTo(rhsVec);
            lhsVec.free();

            // Prevent out of bounds assignment. We only need to prevent values that can be represented by the rhs, that
            // can not be represented by the lhs. The values that the rhs can have that the lhs can't have, but that can
            // be represented by the lhs, are already prevented elsewhere by means of the range invariants.
            for (int i = lhsLen; i < len; i++) {
                error = error.orWith(rhsVec.getBit(i).id());
            }
            rhsVec.free();

            // Return the full relation.
            return pair(relation, error);
        }
    }

    /**
     * Adds and event and edge for each input variable.
     *
     * @param cifBddSpec The CIF/BDD specification. Is modified in-place.
     */
    private void addInputVariableEdges(CifBddSpec cifBddSpec) {
        // Initialization.
        cifBddSpec.inputVarEvents = set();

        // Add for each input variable.
        for (CifBddVariable var: cifBddSpec.variables) {
            // Handle only input variables.
            if (var == null) {
                continue;
            }
            if (!(var instanceof CifBddInputVariable)) {
                continue;
            }
            CifBddInputVariable cifBddInputVar = (CifBddInputVariable)var;

            // Create uncontrollable event for the input variable.
            Event event = newEvent();
            event.setControllable(false);
            event.setName(cifBddInputVar.var.getName());

            // Add new event to the alphabet.
            cifBddSpec.alphabet.add(event);

            // Add new event to the original specification, for proper absolute naming. Also store it in the CIF/BDD
            // specification, to allow for later removal of the temporary event, for instance after synthesis.
            ComplexComponent comp = (ComplexComponent)cifBddInputVar.var.eContainer();
            comp.getDeclarations().add(event);
            cifBddSpec.inputVarEvents.add(event);

            // Add edge that allows input variable to change to any other value.
            CifBddEdge edge = new CifBddEdge(cifBddSpec);
            edge.edges = list((Edge)null);
            edge.event = event;
            edge.origGuard = cifBddSpec.factory.one();
            edge.guard = cifBddSpec.factory.one();
            edge.error = cifBddSpec.factory.zero();
            cifBddSpec.edges.add(edge);
            cifBddSpec.eventEdges.put(event, list(edge));

            // Add CIF assignment to edge. Right hand side not filled, as it is not a 'normal' assignment. Also,
            // technically in CIF an input variable can not be assigned.
            InputVariableExpression addr = newInputVariableExpression();
            addr.setVariable(cifBddInputVar.var);
            Assignment asgn = newAssignment();
            asgn.setAddressable(addr);
            edge.assignments = list(list(asgn));

            // Add update relation.
            edge.update = cifBddSpec.factory.one();
            for (CifBddVariable updVar: cifBddSpec.variables) {
                // If conversion of variable failed, skip it.
                if (updVar == null) {
                    continue;
                }

                // Get lhs and rhs.
                CifBddBitVector vectorOld = CifBddBitVector.createDomain(updVar.domain);
                CifBddBitVector vectorNew = CifBddBitVector.createDomain(updVar.domainNew);

                // Create update predicate for this variable, and add it.
                BDD varUpdate;
                if (updVar == var) {
                    // The input variable: add 'input != input+' to allow it to change to any other value. Also keep the
                    // new value in the domain.
                    BDD newInDomain = BddUtils.getVarDomain(updVar, true, cifBddSpec.factory);
                    varUpdate = vectorOld.unequalTo(vectorNew);
                    varUpdate = varUpdate.andWith(newInDomain);
                } else {
                    // Any other variable: add 'var = var+'.
                    varUpdate = vectorOld.equalTo(vectorNew);
                }

                edge.update = edge.update.andWith(varUpdate);

                // Cleanup.
                vectorOld.free();
                vectorNew.free();
            }
        }
    }

    /**
     * Merges CIF/BDD edges to the desired granularity.
     *
     * @param cifBddSpec The CIF/BDD specification. Is modified in-place.
     */
    private void mergeEdges(CifBddSpec cifBddSpec) {
        // Skip in case of conversion failure.
        if (cifBddSpec.eventEdges == null) {
            return;
        }

        // Merge the edges, if needed.
        switch (cifBddSpec.settings.edgeGranularity) {
            case PER_EDGE:
                // Nothing to do, as already at per-edge granularity.
                return;
            case PER_EVENT: {
                // Merge edges per event.
                int eventCount = cifBddSpec.eventEdges.size();
                cifBddSpec.edges = listc(eventCount);
                for (Entry<Event, List<CifBddEdge>> entry: cifBddSpec.eventEdges.entrySet()) {
                    CifBddEdge mergedEdge = entry.getValue().stream().reduce(CifBddEdge::mergeEdges).get();
                    cifBddSpec.edges.add(mergedEdge);
                    entry.setValue(list(mergedEdge));
                }
                return;
            }
        }
        throw new RuntimeException("Unknown granularity: " + cifBddSpec.settings.edgeGranularity);
    }

    /**
     * Orders the CIF/BDD edges, for reachability computations.
     *
     * @param cifBddSpec The CIF/BDD specification. Is modified in-place.
     */
    private void orderEdges(CifBddSpec cifBddSpec) {
        cifBddSpec.orderedEdgesBackward = orderEdgesForDirection(cifBddSpec.edges,
                cifBddSpec.settings.edgeOrderBackward, cifBddSpec.settings.edgeOrderAllowDuplicateEvents, false);
        cifBddSpec.orderedEdgesForward = orderEdgesForDirection(cifBddSpec.edges, cifBddSpec.settings.edgeOrderForward,
                cifBddSpec.settings.edgeOrderAllowDuplicateEvents, true);
    }

    /**
     * Orders the CIF/BDD edges, for a single direction, i.e., for forward or backward reachability computations.
     *
     * @param edges The edges in linearized model order.
     * @param orderTxt The order as textual value from the settings, for the given direction.
     * @param edgeOrderAllowDuplicateEvents Whether duplicate events are allowed for custom edge orders.
     * @param forForwardReachability Order for forward reachability ({@code true}) or backward reachability
     *     ({@code false}).
     * @return The ordered edges.
     */
    private static List<CifBddEdge> orderEdgesForDirection(List<CifBddEdge> edges, String orderTxt,
            EdgeOrderDuplicateEventAllowance edgeOrderAllowDuplicateEvents, boolean forForwardReachability)
    {
        if (orderTxt.toLowerCase(Locale.US).equals("model")) {
            // No reordering. Keep linearized model order.
            return edges;
        } else if (orderTxt.toLowerCase(Locale.US).equals("reverse-model")) {
            // Reorder to the reverse of the linearized model order.
            return reverse(edges);
        } else if (orderTxt.toLowerCase(Locale.US).equals("sorted")) {
            // Sort based on absolute event names.
            return edges.stream()
                    .sorted((v, w) -> Strings.SORTER.compare(getAbsName(v.event, false), getAbsName(w.event, false)))
                    .toList();
        } else if (orderTxt.toLowerCase(Locale.US).equals("reverse-sorted")) {
            // Sort based on absolute event names, and reverse.
            return reverse(edges.stream()
                    .sorted((v, w) -> Strings.SORTER.compare(getAbsName(v.event, false), getAbsName(w.event, false)))
                    .toList());
        } else if (orderTxt.toLowerCase(Locale.US).equals("random")
                || orderTxt.toLowerCase(Locale.US).startsWith("random:"))
        {
            // Get seed, if specified.
            Long seed = null;
            if (orderTxt.contains(":")) {
                int idx = orderTxt.indexOf(":");
                String seedTxt = orderTxt.substring(idx + 1);
                try {
                    seed = Long.parseUnsignedLong(seedTxt);
                } catch (NumberFormatException ex) {
                    String msg = fmt("Invalid random %s edge order seed number: \"%s\".",
                            forForwardReachability ? "forward" : "backward", orderTxt);
                    throw new InvalidOptionException(msg, ex);
                }
            }

            // Shuffle to random order.
            List<CifBddEdge> orderedEdges = copy(edges);
            if (seed == null) {
                Collections.shuffle(orderedEdges);
            } else {
                Collections.shuffle(orderedEdges, new Random(seed));
            }
            return orderedEdges;
        } else {
            // Sort based on supplied custom event order.
            List<CifBddEdge> orderedEdges = listc(edges.size());
            Set<CifBddEdge> processedEdges = set();

            // Process elements.
            for (String elemTxt: StringUtils.split(orderTxt, ",")) {
                // Skip empty.
                elemTxt = elemTxt.trim();
                if (elemTxt.isEmpty()) {
                    continue;
                }

                // Create regular expression for matching.
                String regEx = elemTxt.replace(".", "\\.");
                regEx = regEx.replace("*", ".*");
                Pattern pattern = Pattern.compile("^" + regEx + "$");

                // Found actual element. Look up matching CIF/BDD edges.
                List<CifBddEdge> matches = list();
                for (CifBddEdge edge: edges) {
                    String name = getAbsName(edge.event, false);
                    if (pattern.matcher(name).matches()) {
                        matches.add(edge);
                    }
                }

                // Need a least one match.
                if (matches.isEmpty()) {
                    String msg = fmt(
                            "Invalid custom %s edge order: can't find a match for \"%s\". There is no supported event "
                                    + "or input variable in the specification that matches the given name pattern.",
                            forForwardReachability ? "forward" : "backward", elemTxt);
                    throw new InvalidOptionException(msg);
                }

                // Sort matches.
                Collections.sort(matches,
                        (v, w) -> Strings.SORTER.compare(getAbsName(v.event, false), getAbsName(w.event, false)));

                // Check for duplicate events, if duplicates are disallowed.
                if (edgeOrderAllowDuplicateEvents == EdgeOrderDuplicateEventAllowance.DISALLOWED) {
                    for (CifBddEdge edge: matches) {
                        if (processedEdges.contains(edge)) {
                            String msg = fmt(
                                    "Invalid custom %s edge order: event \"%s\" is included more than once. "
                                            + "If the duplicate event is intentional, enable allowing duplicate events "
                                            + "in the custom event order.",
                                    forForwardReachability ? "forward" : "backward", getAbsName(edge.event, false));
                            throw new InvalidOptionException(msg);
                        }
                    }
                }

                // Update for matched edges.
                processedEdges.addAll(matches);
                orderedEdges.addAll(matches);
            }

            // Check completeness.
            Set<CifBddEdge> missingEdges = difference(edges, processedEdges);
            if (!missingEdges.isEmpty()) {
                Set<String> names = set();
                for (CifBddEdge edge: missingEdges) {
                    names.add("\"" + getAbsName(edge.event, false) + "\"");
                }
                List<String> sortedNames = Sets.sortedgeneric(names, Strings.SORTER);
                String msg = fmt(
                        "Invalid custom %s edge order: "
                                + "the following event(s) are missing from the specified order: %s.",
                        forForwardReachability ? "forward" : "backward", String.join(", ", sortedNames));
                throw new InvalidOptionException(msg);
            }

            // Return the custom edge order.
            return orderedEdges;
        }
    }

    /**
     * Check edge workset algorithm settings.
     *
     * @param settings The settings.
     */
    private void checkEdgeWorksetAlgorithmSettings(CifDataSynthesisSettings settings) {
        // Skip if workset algorithm is disabled.
        if (!settings.doUseEdgeWorksetAlgo) {
            return;
        }

        // Edge workset algorithm requires per-event edge granularity, and no duplicate edges in the edge order.
        if (settings.edgeGranularity != EdgeGranularity.PER_EVENT) {
            throw new InvalidOptionException(
                    "The edge workset algorithm can only be used with per-event edge granularity. "
                            + "Either disable the edge workset algorithm, or configure per-event edge granularity.");
        }
        if (settings.edgeOrderAllowDuplicateEvents == EdgeOrderDuplicateEventAllowance.ALLOWED) {
            throw new InvalidOptionException(
                    "The edge workset algorithm can not be used with duplicate events in the edge order. "
                            + "Either disable the edge workset algorithm, or disable duplicates for custom edge orders.");
        }
    }

    /**
     * Converts CIF predicates to a BDD predicate, assuming conjunction between the CIF predicates.
     *
     * @param preds The CIF predicates.
     * @param initial Whether the predicates apply only to the initial state ({@code true}) or any state ({@code false},
     *     includes the initial state).
     * @param cifBddSpec The CIF/BDD specification.
     * @return The BDD predicate.
     * @throws UnsupportedPredicateException If one of the predicates is not supported.
     */
    private static BDD convertPreds(List<Expression> preds, boolean initial, CifBddSpec cifBddSpec)
            throws UnsupportedPredicateException
    {
        BDD rslt = cifBddSpec.factory.one();
        for (Expression pred: preds) {
            rslt = rslt.andWith(convertPred(pred, initial, cifBddSpec));
        }
        return rslt;
    }

    /**
     * Converts a CIF predicate to a BDD predicate.
     *
     * @param pred The CIF predicate.
     * @param initial Whether the predicate applies only to the initial state ({@code true}) or any state
     *     ({@code false}, includes the initial state).
     * @param cifBddSpec The CIF/BDD specification.
     * @return The BDD predicate.
     * @throws UnsupportedPredicateException If the predicate is not supported.
     */
    private static BDD convertPred(Expression pred, boolean initial, CifBddSpec cifBddSpec)
            throws UnsupportedPredicateException
    {
        if (pred instanceof BoolExpression) {
            // Boolean literal.
            boolean value = ((BoolExpression)pred).isValue();
            return value ? cifBddSpec.factory.one() : cifBddSpec.factory.zero();
        } else if (pred instanceof DiscVariableExpression) {
            // Boolean variable reference.
            DiscVariable cifVar = ((DiscVariableExpression)pred).getVariable();
            Assert.check(normalizeType(cifVar.getType()) instanceof BoolType);
            int varIdx = getDiscVarIdx(cifBddSpec.variables, cifVar);
            if (varIdx == -1) {
                throw new UnsupportedPredicateException();
            }

            // Create BDD predicate for 'x' or 'x = true'.
            CifBddVariable var = cifBddSpec.variables[varIdx];
            return var.domain.ithVar(1);
        } else if (pred instanceof InputVariableExpression) {
            // Boolean variable reference.
            InputVariable cifVar = ((InputVariableExpression)pred).getVariable();
            Assert.check(normalizeType(cifVar.getType()) instanceof BoolType);
            int varIdx = getInputVarIdx(cifBddSpec.variables, cifVar);
            if (varIdx == -1) {
                throw new UnsupportedPredicateException();
            }

            // Create BDD predicate for 'x' or 'x = true'.
            CifBddVariable var = cifBddSpec.variables[varIdx];
            return var.domain.ithVar(1);
        } else if (pred instanceof AlgVariableExpression) {
            // Algebraic variable reference. Get the single defining value expression, representing the value of the
            // variable. It is in an 'if' expression if an equation is provided per location of an automaton with more
            // than one location.
            AlgVariable var = ((AlgVariableExpression)pred).getVariable();
            Assert.check(normalizeType(var.getType()) instanceof BoolType);
            Expression value = CifEquationUtils.getSingleValueForAlgVar(var);

            // Convert the defining value expression instead.
            return convertPred(value, initial, cifBddSpec);
        } else if (pred instanceof LocationExpression) {
            // Location reference.
            Location loc = ((LocationExpression)pred).getLocation();
            Automaton aut = CifLocationUtils.getAutomaton(loc);
            int varIdx = getLpVarIdx(cifBddSpec.variables, aut);
            if (varIdx == -1) {
                // Automata with only one location have no location pointer, but are always the active location. So,
                // referring to them is as using a 'true' predicate.
                if (aut.getLocations().size() == 1) {
                    return cifBddSpec.factory.one();
                }

                // Unsupported automaton, probably due to wrong kind.
                throw new UnsupportedPredicateException();
            }
            Assert.check(varIdx >= 0);
            CifBddVariable var = cifBddSpec.variables[varIdx];

            // Create BDD predicate for location pointer being equal to value that represents the location.
            int locIdx = aut.getLocations().indexOf(loc);
            Assert.check(locIdx >= 0);
            return var.domain.ithVar(locIdx);
        } else if (pred instanceof ConstantExpression) {
            // Boolean constant reference.
            Constant constant = ((ConstantExpression)pred).getConstant();
            Assert.check(normalizeType(constant.getType()) instanceof BoolType);

            // Evaluate the constant's value.
            Object valueObj;
            try {
                valueObj = CifEvalUtils.eval(constant.getValue(), initial);
            } catch (CifEvalException ex) {
                String msg = fmt("Failed to statically evaluate the value of constant \"%s\".", getAbsName(constant));
                throw new InvalidInputException(msg, ex);
            }

            return (boolean)valueObj ? cifBddSpec.factory.one() : cifBddSpec.factory.zero();
        } else if (pred instanceof UnaryExpression) {
            // Inverse unary expression.
            UnaryExpression upred = (UnaryExpression)pred;
            UnaryOperator op = upred.getOperator();
            if (op != UnaryOperator.INVERSE) {
                String msg = fmt("unary operator \"%s\" is not supported.", CifTextUtils.operatorToStr(op));
                throw new UnsupportedPredicateException(msg, upred);
            }

            // not x
            BDD child = convertPred(upred.getChild(), initial, cifBddSpec);
            BDD rslt = child.not();
            child.free();
            return rslt;
        } else if (pred instanceof BinaryExpression) {
            // Various binary expressions.
            BinaryExpression bpred = (BinaryExpression)pred;
            BinaryOperator op = (((BinaryExpression)pred).getOperator());
            Expression lhs = bpred.getLeft();
            Expression rhs = bpred.getRight();

            // a and b
            if (op == CONJUNCTION) {
                CifType ltype = normalizeType(lhs.getType());
                CifType rtype = normalizeType(rhs.getType());
                if (!(ltype instanceof BoolType) || !(rtype instanceof BoolType)) {
                    String msg = fmt("binary operator \"%s\" on values of types \"%s\" and \"%s\" is not supported.",
                            CifTextUtils.operatorToStr(op), CifTextUtils.typeToStr(ltype),
                            CifTextUtils.typeToStr(rtype));
                    throw new UnsupportedPredicateException(msg, bpred);
                }

                BDD left = convertPred(lhs, initial, cifBddSpec);
                BDD right = convertPred(rhs, initial, cifBddSpec);
                return left.andWith(right);
            }

            // a or b
            if (op == DISJUNCTION) {
                CifType ltype = normalizeType(lhs.getType());
                CifType rtype = normalizeType(rhs.getType());
                if (!(ltype instanceof BoolType) || !(rtype instanceof BoolType)) {
                    String msg = fmt("binary operator \"%s\" on values of types \"%s\" and \"%s\" is not supported.",
                            CifTextUtils.operatorToStr(op), CifTextUtils.typeToStr(ltype),
                            CifTextUtils.typeToStr(rtype));
                    throw new UnsupportedPredicateException(msg, bpred);
                }

                BDD left = convertPred(lhs, initial, cifBddSpec);
                BDD right = convertPred(rhs, initial, cifBddSpec);
                return left.orWith(right);
            }

            // a => b
            if (op == IMPLICATION) {
                BDD left = convertPred(lhs, initial, cifBddSpec);
                BDD right = convertPred(rhs, initial, cifBddSpec);
                return left.impWith(right);
            }

            // a <=> b
            if (op == BI_CONDITIONAL) {
                BDD left = convertPred(lhs, initial, cifBddSpec);
                BDD right = convertPred(rhs, initial, cifBddSpec);
                return left.biimpWith(right);
            }

            // Check supported operator.
            switch (op) {
                case EQUAL:
                case GREATER_EQUAL:
                case GREATER_THAN:
                case LESS_EQUAL:
                case LESS_THAN:
                case UNEQUAL:
                    break;

                default: {
                    String msg = fmt("binary operator \"%s\" is not supported.", CifTextUtils.operatorToStr(op));
                    throw new UnsupportedPredicateException(msg, bpred);
                }
            }

            return convertCmpPred(lhs, rhs, op, pred, bpred, initial, cifBddSpec);
        } else if (pred instanceof IfExpression) {
            // Condition expression with boolean result values.
            IfExpression ifPred = (IfExpression)pred;

            // Convert else.
            BDD rslt = convertPred(ifPred.getElse(), initial, cifBddSpec);

            // Convert elifs/thens.
            for (int i = ifPred.getElifs().size() - 1; i >= 0; i--) {
                ElifExpression elifPred = ifPred.getElifs().get(i);
                BDD elifGuards = convertPreds(elifPred.getGuards(), initial, cifBddSpec);
                BDD elifThen = convertPred(elifPred.getThen(), initial, cifBddSpec);
                BDD elifRslt = elifGuards.ite(elifThen, rslt);
                elifGuards.free();
                elifThen.free();
                rslt.free();
                rslt = elifRslt;
            }

            // Convert if/then.
            BDD ifGuards = convertPreds(ifPred.getGuards(), initial, cifBddSpec);
            BDD ifThen = convertPred(ifPred.getThen(), initial, cifBddSpec);
            BDD elifRslt = ifGuards.ite(ifThen, rslt);
            ifGuards.free();
            ifThen.free();
            rslt.free();
            rslt = elifRslt;

            // Return converted conditional expression.
            return rslt;
        } else if (pred instanceof SwitchExpression) {
            // Switch expression with boolean result values.
            SwitchExpression switchPred = (SwitchExpression)pred;
            Expression value = switchPred.getValue();
            List<SwitchCase> cases = switchPred.getCases();

            // Convert else.
            BDD rslt = convertPred(last(cases).getValue(), initial, cifBddSpec);

            // Convert cases.
            for (int i = cases.size() - 2; i >= 0; i--) {
                SwitchCase cse = cases.get(i);
                Expression caseGuardExpr = CifTypeUtils.isAutRefExpr(value) ? cse.getKey() : newBinaryExpression(
                        deepclone(value), BinaryOperator.EQUAL, null, deepclone(cse.getKey()), newBoolType());
                BDD caseGuard = convertPred(caseGuardExpr, initial, cifBddSpec);
                BDD caseThen = convertPred(cse.getValue(), initial, cifBddSpec);
                BDD caseRslt = caseGuard.ite(caseThen, rslt);
                caseGuard.free();
                caseThen.free();
                rslt.free();
                rslt = caseRslt;
            }

            return rslt;
        } else {
            // Others: unsupported.
            String msg = fmt("predicate is not supported.");
            throw new UnsupportedPredicateException(msg, pred);
        }
    }

    /**
     * Converts a CIF comparison predicate to a BDD predicate.
     *
     * @param lhs The left hand side of the comparison predicate.
     * @param rhs The right hand side of the comparison predicate.
     * @param op The comparison operator ({@code =}, {@code !=}, {@code <}, {@code <=}, {@code >}, or {@code >=}).
     * @param pred The whole CIF predicate, of which the comparison predicate is a part.
     * @param bpred The binary expression of the comparison predicate. Essentially '{@code lhs op rhs}'.
     * @param initial Whether the predicate applies only to the initial state ({@code true}) or any state
     *     ({@code false}, includes the initial state).
     * @param cifBddSpec The CIF/BDD specification.
     * @return The BDD predicate.
     * @throws UnsupportedPredicateException If the predicate is not supported.
     */
    private static BDD convertCmpPred(Expression lhs, Expression rhs, BinaryOperator op, Expression pred,
            BinaryExpression bpred, boolean initial, CifBddSpec cifBddSpec) throws UnsupportedPredicateException
    {
        // Check lhs and rhs types.
        CifType ltype = normalizeType(lhs.getType());
        CifType rtype = normalizeType(rhs.getType());
        if (!(ltype instanceof BoolType && rtype instanceof BoolType)
                && !(ltype instanceof EnumType && rtype instanceof EnumType)
                && !(ltype instanceof IntType && rtype instanceof IntType))
        {
            String msg = fmt("binary operator \"%s\" on values of types \"%s\" and \"%s\" is not supported.",
                    CifTextUtils.operatorToStr(op), CifTextUtils.typeToStr(ltype), CifTextUtils.typeToStr(rtype));
            throw new UnsupportedPredicateException(msg, bpred);
        }

        // Special handling of boolean values.
        if (ltype instanceof BoolType && rtype instanceof BoolType) {
            BDD lbdd = convertPred(lhs, initial, cifBddSpec);
            BDD rbdd = convertPred(rhs, initial, cifBddSpec);
            switch (op) {
                case EQUAL:
                    return lbdd.biimpWith(rbdd);

                case UNEQUAL: {
                    BDD eq = lbdd.biimpWith(rbdd);
                    BDD rslt = eq.not();
                    eq.free();
                    return rslt;
                }

                default:
                    throw new RuntimeException("Unexpected op: " + op);
            }
        }

        // Convert lhs and rhs to bit vectors.
        Supplier<String> partMsg = () -> fmt("predicate \"%s\"", CifTextUtils.exprToStr(pred));
        CifBddBitVectorAndCarry lrslt = convertExpr(lhs, initial, cifBddSpec, false, partMsg);
        CifBddBitVectorAndCarry rrslt = convertExpr(rhs, initial, cifBddSpec, false, partMsg);
        Assert.check(lrslt.carry.isZero());
        Assert.check(rrslt.carry.isZero());
        CifBddBitVector lvec = lrslt.vector;
        CifBddBitVector rvec = rrslt.vector;

        // Make bit vectors equal size.
        int length = Math.max(lvec.length(), rvec.length());
        lvec.resize(length);
        rvec.resize(length);

        // Apply comparison operator.
        BDD rslt;
        switch (op) {
            case LESS_THAN:
                rslt = lvec.lessThan(rvec);
                break;
            case LESS_EQUAL:
                rslt = lvec.lessOrEqual(rvec);
                break;
            case GREATER_THAN:
                rslt = lvec.greaterThan(rvec);
                break;
            case GREATER_EQUAL:
                rslt = lvec.greaterOrEqual(rvec);
                break;
            case EQUAL:
                rslt = lvec.equalTo(rvec);
                break;
            case UNEQUAL:
                rslt = lvec.unequalTo(rvec);
                break;

            default:
                throw new RuntimeException("Unexpected op: " + op);
        }
        lvec.free();
        rvec.free();
        return rslt;
    }

    /**
     * Converts a CIF expression to a BDD bit vector.
     *
     * @param expr The CIF expression. Has an integer or enumeration type.
     * @param initial Whether the predicate applies only to the initial state ({@code true}) or any state
     *     ({@code false}, includes the initial state).
     * @param cifBddSpec The CIF/BDD specification.
     * @param allowSubtract Whether a subtraction is allowed ({@code true}) or not ({@code false}). It must only be
     *     allowed for top level expressions, if the caller can handle the potential unrepresentable results. For sub
     *     expressions, subtraction is never allowed.
     * @param partMsg A supplier of a part of an error message. In case the expression is invalid (e.g. static and can't
     *     be evaluated), the supplied error message part indicates of what larger thing the invalid expression is a
     *     part. The partial error message obtained from this supplier will be inserted for {@code "[supplied]"} in
     *     error messages of the form {@code "Failed to ... the ... part of [supplied]."}. The supplier is not used in
     *     case the expression is unsupported.
     * @return The BDD bit vector, and a carry indicating situations in which the expression results in an
     *     unrepresentable value. Currently, the only unrepresentable values are negative values resulting from a
     *     subtraction. If subtraction is not allowed, the carry is always {@link BDD#isZero() zero}.
     * @throws UnsupportedPredicateException If the predicate is not supported.
     * @throws InvalidInputException If a static part of the given expression can't be evaluated.
     */
    private static CifBddBitVectorAndCarry convertExpr(Expression expr, boolean initial, CifBddSpec cifBddSpec,
            boolean allowSubtract, Supplier<String> partMsg) throws UnsupportedPredicateException
    {
        // Variable references.
        if (expr instanceof DiscVariableExpression) {
            // Get variable.
            DiscVariable cifVar = ((DiscVariableExpression)expr).getVariable();
            int varIdx = getDiscVarIdx(cifBddSpec.variables, cifVar);
            if (varIdx == -1) {
                throw new UnsupportedPredicateException();
            }
            CifBddVariable var = cifBddSpec.variables[varIdx];

            // Create bit vector for the domain of the variable.
            CifBddBitVector vector = CifBddBitVector.createDomain(var.domain);
            return new CifBddBitVectorAndCarry(vector, cifBddSpec.factory.zero());
        } else if (expr instanceof InputVariableExpression) {
            // Get variable.
            InputVariable cifVar = ((InputVariableExpression)expr).getVariable();
            int varIdx = getInputVarIdx(cifBddSpec.variables, cifVar);
            if (varIdx == -1) {
                throw new UnsupportedPredicateException();
            }
            CifBddVariable var = cifBddSpec.variables[varIdx];

            // Create bit vector for the domain of the variable.
            CifBddBitVector vector = CifBddBitVector.createDomain(var.domain);
            return new CifBddBitVectorAndCarry(vector, cifBddSpec.factory.zero());
        } else if (expr instanceof AlgVariableExpression) {
            // Algebraic variable reference. Get the single defining value expression, representing the value of the
            // variable. It is in an 'if' expression if an equation is provided per location of an automaton with more
            // than one location.
            AlgVariable var = ((AlgVariableExpression)expr).getVariable();
            Expression value = CifEquationUtils.getSingleValueForAlgVar(var);

            // Convert the defining value expression instead.
            return convertExpr(value, initial, cifBddSpec, allowSubtract, partMsg);
        }

        // Unary operators.
        if (expr instanceof UnaryExpression) {
            UnaryExpression uexpr = (UnaryExpression)expr;
            switch (uexpr.getOperator()) {
                case PLUS:
                    return convertExpr(uexpr.getChild(), initial, cifBddSpec, false, partMsg);

                default:
                    break;
            }
        }

        // Binary operators.
        if (expr instanceof BinaryExpression) {
            BinaryExpression bexpr = (BinaryExpression)expr;
            Expression lhs = bexpr.getLeft();
            Expression rhs = bexpr.getRight();

            switch (bexpr.getOperator()) {
                case ADDITION: {
                    // Get lhs and rhs vectors.
                    CifBddBitVectorAndCarry lrslt = convertExpr(lhs, initial, cifBddSpec, false, partMsg);
                    CifBddBitVectorAndCarry rrslt = convertExpr(rhs, initial, cifBddSpec, false, partMsg);
                    Assert.check(lrslt.carry.isZero());
                    Assert.check(rrslt.carry.isZero());
                    CifBddBitVector lvec = lrslt.vector;
                    CifBddBitVector rvec = rrslt.vector;

                    // Calculate minimum needed vector length, taking into account the final carry bit.
                    int length = Math.max(lvec.length(), rvec.length()) + 1;

                    // Resize lhs and rhs vectors.
                    lvec.resize(length);
                    rvec.resize(length);

                    // Apply addition.
                    CifBddBitVectorAndCarry rslt = lvec.add(rvec);
                    Assert.check(rslt.carry.isZero());
                    lvec.free();
                    rvec.free();
                    return rslt;
                }

                case SUBTRACTION: {
                    // Handle subtraction only if allowed.
                    if (!allowSubtract) {
                        break;
                    }

                    // Get lhs and rhs vectors.
                    CifBddBitVectorAndCarry lrslt = convertExpr(lhs, initial, cifBddSpec, false, partMsg);
                    CifBddBitVectorAndCarry rrslt = convertExpr(rhs, initial, cifBddSpec, false, partMsg);
                    Assert.check(lrslt.carry.isZero());
                    Assert.check(rrslt.carry.isZero());
                    CifBddBitVector lvec = lrslt.vector;
                    CifBddBitVector rvec = rrslt.vector;

                    // Resize lhs and rhs vectors to be of equal length.
                    int length = Math.max(lvec.length(), rvec.length());
                    lvec.resize(length);
                    rvec.resize(length);

                    // Apply subtraction.
                    CifBddBitVectorAndCarry rslt = lvec.subtract(rvec);
                    lvec.free();
                    rvec.free();
                    return rslt;
                }

                case INTEGER_DIVISION:
                case MODULUS: {
                    // Convert lhs.
                    CifBddBitVectorAndCarry lrslt = convertExpr(lhs, initial, cifBddSpec, false, partMsg);
                    Assert.check(lrslt.carry.isZero());
                    CifBddBitVector lvec = lrslt.vector;

                    // Evaluate rhs.
                    if (!CifValueUtils.hasSingleValue(rhs, initial, true)) {
                        String msg = "value is too complex to be statically evaluated.";
                        throw new UnsupportedPredicateException(msg, rhs);
                    }

                    Object rhsValueObj;
                    try {
                        rhsValueObj = CifEvalUtils.eval(rhs, initial);
                    } catch (CifEvalException ex) {
                        // It would be rather complex to provide more context in this error message.
                        String msg = fmt("Failed to statically evaluate the \"%s\" part of %s.",
                                CifTextUtils.exprToStr(rhs), partMsg.get());
                        throw new InvalidInputException(msg, ex);
                    }

                    // Check divisor (rhs value).
                    int divisor = (int)rhsValueObj;
                    if (divisor == 0) {
                        // Unsupported: always division by zero.
                        String msg = fmt("\"%s\" always results in division by zero.", CifTextUtils.exprToStr(expr));
                        throw new UnsupportedPredicateException(msg, expr);
                    } else if (divisor < 0) {
                        // Unsupported: can't represent negative integers.
                        String msg = fmt(
                                "\"%s\" performs division/modulus by a negative value, which is not supported.",
                                CifTextUtils.exprToStr(expr));
                        throw new UnsupportedPredicateException(msg, expr);
                    }

                    // Resize lhs vector if needed. The rhs needs to fit. For 'mod', the highest bit of the lhs needs to
                    // be 'false' as well.
                    boolean isDiv = bexpr.getOperator() == INTEGER_DIVISION;
                    int lhsLen = lvec.length();
                    if (!isDiv) {
                        lhsLen++;
                    }
                    int rhsLen = BddUtils.getMinimumBits(divisor);
                    int length = Math.max(lhsLen, rhsLen);
                    lvec.resize(length);

                    // Apply div/mod.
                    CifBddBitVector rslt = lvec.divmod(divisor, isDiv);
                    lvec.free();
                    return new CifBddBitVectorAndCarry(rslt, cifBddSpec.factory.zero());
                }

                default:
                    break;
            }
        }

        // Conditional expression.
        if (expr instanceof IfExpression) {
            // Condition expression.
            IfExpression ifExpr = (IfExpression)expr;

            // Convert else.
            CifBddBitVectorAndCarry elseRslt = convertExpr(ifExpr.getElse(), initial, cifBddSpec, false, partMsg);
            Assert.check(elseRslt.carry.isZero());
            CifBddBitVector rslt = elseRslt.vector;

            // Convert elifs/thens.
            for (int i = ifExpr.getElifs().size() - 1; i >= 0; i--) {
                ElifExpression elifExpr = ifExpr.getElifs().get(i);
                BDD elifGuards = convertPreds(elifExpr.getGuards(), initial, cifBddSpec);
                CifBddBitVectorAndCarry elifThen = convertExpr(elifExpr.getThen(), initial, cifBddSpec, false, partMsg);
                Assert.check(elifThen.carry.isZero());
                CifBddBitVector elifVector = elifThen.vector;
                int len = Math.max(rslt.length(), elifVector.length());
                rslt.resize(len);
                elifVector.resize(len);
                CifBddBitVector elifRslt = elifVector.ifThenElse(rslt, elifGuards);
                elifGuards.free();
                elifVector.free();
                rslt.free();
                rslt = elifRslt;
            }

            // Convert if/then.
            BDD ifGuards = convertPreds(ifExpr.getGuards(), initial, cifBddSpec);
            CifBddBitVectorAndCarry ifThen = convertExpr(ifExpr.getThen(), initial, cifBddSpec, false, partMsg);
            Assert.check(ifThen.carry.isZero());
            CifBddBitVector ifVector = ifThen.vector;
            int len = Math.max(rslt.length(), ifVector.length());
            rslt.resize(len);
            ifVector.resize(len);
            CifBddBitVector ifRslt = ifVector.ifThenElse(rslt, ifGuards);
            ifGuards.free();
            ifVector.free();
            rslt.free();
            rslt = ifRslt;

            // Return converted conditional expression.
            return new CifBddBitVectorAndCarry(rslt, cifBddSpec.factory.zero());
        }

        // Switch expression.
        if (expr instanceof SwitchExpression) {
            SwitchExpression switchExpr = (SwitchExpression)expr;
            Expression value = switchExpr.getValue();
            List<SwitchCase> cases = switchExpr.getCases();

            // Convert else.
            CifBddBitVectorAndCarry elseRslt = convertExpr(last(cases).getValue(), initial, cifBddSpec, false, partMsg);
            Assert.check(elseRslt.carry.isZero());
            CifBddBitVector rslt = elseRslt.vector;

            // Convert cases.
            for (int i = cases.size() - 2; i >= 0; i--) {
                SwitchCase cse = cases.get(i);
                Expression caseGuardExpr = CifTypeUtils.isAutRefExpr(value) ? cse.getKey() : newBinaryExpression(
                        deepclone(value), BinaryOperator.EQUAL, null, deepclone(cse.getKey()), newBoolType());
                BDD caseGuard = convertPred(caseGuardExpr, initial, cifBddSpec);
                CifBddBitVectorAndCarry caseThen = convertExpr(cse.getValue(), initial, cifBddSpec, false, partMsg);
                Assert.check(caseThen.carry.isZero());
                CifBddBitVector caseVector = caseThen.vector;
                int len = Math.max(rslt.length(), caseVector.length());
                rslt.resize(len);
                caseVector.resize(len);
                CifBddBitVector caseRslt = caseVector.ifThenElse(rslt, caseGuard);
                caseGuard.free();
                caseVector.free();
                rslt.free();
                rslt = caseRslt;
            }

            // Return converted switch expression.
            return new CifBddBitVectorAndCarry(rslt, cifBddSpec.factory.zero());
        }

        // Static evaluable expression.
        if (!CifValueUtils.hasSingleValue(expr, initial, true)) {
            String msg = "value is too complex to be statically evaluated.";
            throw new UnsupportedPredicateException(msg, expr);
        }

        // Evaluate expression.
        Object valueObj;
        try {
            valueObj = CifEvalUtils.eval(expr, initial);
        } catch (CifEvalException ex) {
            // It would be rather complex to provide more context in this error message.
            String msg = fmt("Failed to statically evaluate the \"%s\" part of %s.", CifTextUtils.exprToStr(expr),
                    partMsg.get());
            throw new InvalidInputException(msg, ex);
        }

        // Create bit vector.
        if (valueObj instanceof Integer) {
            // Get integer value.
            int value = (Integer)valueObj;

            // Negative integer values not supported.
            if (value < 0) {
                String msg = fmt("value \"%d\" is unsupported, as it is negative.", value);
                throw new UnsupportedPredicateException(msg, expr);
            }

            // Create BDD bit vector for constant value.
            CifBddBitVector vector = CifBddBitVector.createInt(cifBddSpec.factory, value);
            return new CifBddBitVectorAndCarry(vector, cifBddSpec.factory.zero());
        } else {
            // Get enumeration declaration and literal.
            Assert.check(valueObj instanceof CifEnumLiteral);
            EnumLiteral lit = ((CifEnumLiteral)valueObj).literal;
            EnumDecl enumDecl = (EnumDecl)lit.eContainer();

            // Create bit vector.
            int litIdx = enumDecl.getLiterals().indexOf(lit);
            CifBddBitVector vector = CifBddBitVector.createInt(cifBddSpec.factory, litIdx);
            return new CifBddBitVectorAndCarry(vector, cifBddSpec.factory.zero());
        }
    }

    /**
     * Collect the events declared in the given component (recursively).
     *
     * @param comp The component.
     * @param events The events collected so far. Is modified in-place.
     */
    public static void collectEvents(ComplexComponent comp, List<Event> events) {
        // Collect locally.
        for (Declaration decl: comp.getDeclarations()) {
            if (decl instanceof Event) {
                events.add((Event)decl);
            }
        }

        // Collect recursively.
        if (comp instanceof Group) {
            for (Component child: ((Group)comp).getComponents()) {
                collectEvents((ComplexComponent)child, events);
            }
        }
    }

    /**
     * Collect the automata of the given component (recursively).
     *
     * @param comp The component.
     * @param automata The automata collected so far. Is modified in-place.
     */
    private static void collectAutomata(ComplexComponent comp, List<Automaton> automata) {
        if (comp instanceof Automaton) {
            // Add automaton.
            automata.add((Automaton)comp);
        } else {
            // Collect recursively.
            for (Component child: ((Group)comp).getComponents()) {
                collectAutomata((ComplexComponent)child, automata);
            }
        }
    }

    /**
     * Collect CIF objects for which CIF/BDD variables need to be constructed.
     *
     * @param comp The component in which to collect (recursively).
     * @param objs The objects collected so far. Is modified in-place.
     */
    private static void collectVariableObjects(ComplexComponent comp, List<PositionObject> objs) {
        // Collect automaton locally.
        if (comp instanceof Automaton) {
            Automaton aut = (Automaton)comp;
            if (aut.getLocations().size() > 1) {
                objs.add(aut);
            }
        }

        // Collect variables locally.
        for (Declaration decl: comp.getDeclarations()) {
            if (decl instanceof DiscVariable) {
                objs.add(decl);
            }
            if (decl instanceof InputVariable) {
                objs.add(decl);
            }
        }

        // Collect recursively.
        if (comp instanceof Group) {
            for (Component child: ((Group)comp).getComponents()) {
                collectVariableObjects((ComplexComponent)child, objs);
            }
        }
    }

    /**
     * Returns the CIF/BDD variable index of the given CIF discrete variable.
     *
     * @param vars The CIF/BDD variables, in which to look for the given CIF discrete variable. May not be a
     *     dummy/internal location pointer variable created for an automaton with two or more locations.
     * @param var The CIF discrete variable to look for, and for which the index is to be returned.
     * @return The 0-based index of the CIF/BDD variable, or {@code -1} if it is not found, due to it not being
     *     available, due to a precondition violation.
     */
    private static int getDiscVarIdx(CifBddVariable[] vars, DiscVariable var) {
        // Make sure the given discrete variable is an actual discrete variable, and not a dummy one created for a
        // location pointer of an automaton.
        Assert.check(var.getType() != null);

        // Look up the discrete variable.
        return getTypedVarIdx(vars, var);
    }

    /**
     * Returns the CIF/BDD variable index of the given CIF input variable.
     *
     * @param vars The CIF/BDD variables, in which to look for the given CIF input variable.
     * @param var The CIF input variable to look for, and for which the index is to be returned.
     * @return The 0-based index of the CIF/BDD variable, or {@code -1} if it is not found, due to it not being
     *     available, due to a precondition violation.
     */
    private static int getInputVarIdx(CifBddVariable[] vars, InputVariable var) {
        return getTypedVarIdx(vars, var);
    }

    /**
     * Returns the CIF/BDD variable index of the given CIF typed object, i.e. a discrete variable or an input variable.
     *
     * @param vars The CIF/BDD variables, in which to look for the given CIF typed object. May not be a dummy/internal
     *     location pointer variable created for an automaton with two or more locations.
     * @param var The CIF variable to look for, and for which the index is to be returned.
     * @return The 0-based index of the CIF/BDD variable, or {@code -1} if it is not found, due to it not being
     *     available, due to a precondition violation.
     */
    private static int getTypedVarIdx(CifBddVariable[] vars, Declaration var) {
        for (int i = 0; i < vars.length; i++) {
            CifBddVariable cifBddVar = vars[i];
            if (cifBddVar == null) {
                continue;
            }
            if (!(cifBddVar instanceof CifBddTypedVariable)) {
                continue;
            }
            CifBddTypedVariable cifBddVarTypedVar = (CifBddTypedVariable)cifBddVar;
            if (cifBddVarTypedVar.obj == var) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns the CIF/BDD variable index of the given CIF automaton, or rather the location pointer variable for that
     * automaton.
     *
     * @param vars The CIF/BDD variables, in which to look for the given CIF automaton.
     * @param aut The CIF automaton to look for, and for which the index is to be returned.
     * @return The 0-based index of the CIF/BDD variable, or {@code -1} if it is not found. If not found, no location
     *     pointer was created for the automaton, or the location pointer is not available, due to a precondition
     *     violation.
     */
    private static int getLpVarIdx(CifBddVariable[] vars, Automaton aut) {
        for (int i = 0; i < vars.length; i++) {
            CifBddVariable cifBddVar = vars[i];
            if (cifBddVar == null) {
                continue;
            }
            if (!(cifBddVar instanceof CifBddLocPtrVariable)) {
                continue;
            }
            CifBddLocPtrVariable cifBddLpVar = (CifBddLocPtrVariable)cifBddVar;
            if (cifBddLpVar.aut == aut) {
                return i;
            }
        }
        return -1;
    }

    /** Exception to indicate an unsupported predicate. */
    private static class UnsupportedPredicateException extends Exception {
        /**
         * The (part of the) predicate that is not supported. May be {@code null} to indicate that the predicate is not
         * supported due to earlier precondition violations.
         */
        public final Expression expr;

        /**
         * Constructor for the {@link UnsupportedPredicateException} class, in case a predicate is unsupported due to
         * earlier precondition violations.
         */
        public UnsupportedPredicateException() {
            this(null, null);
        }

        /**
         * Constructor for the {@link UnsupportedPredicateException} class.
         *
         * @param msg A message describing the problem.
         * @param expr The (part of the) predicate that is not supported.
         */
        public UnsupportedPredicateException(String msg, Expression expr) {
            super(msg);
            this.expr = expr;
        }
    }
}
