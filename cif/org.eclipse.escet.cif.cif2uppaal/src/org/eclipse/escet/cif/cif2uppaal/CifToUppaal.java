//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.cif2uppaal;

import static org.eclipse.escet.cif.common.CifLocationUtils.getPossibleInitialLocs;
import static org.eclipse.escet.cif.common.CifTextUtils.getAbsName;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newAutomaton;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newSpecification;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.warn;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Pair.pair;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.str;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.eclipse.escet.cif.cif2cif.ElimAlgVariables;
import org.eclipse.escet.cif.cif2cif.ElimComponentDefInst;
import org.eclipse.escet.cif.cif2cif.ElimMonitors;
import org.eclipse.escet.cif.cif2cif.ElimStateEvtExclInvs;
import org.eclipse.escet.cif.cif2cif.ElimTauEvent;
import org.eclipse.escet.cif.cif2cif.EnumsToInts;
import org.eclipse.escet.cif.cif2cif.RemoveIoDecls;
import org.eclipse.escet.cif.common.CifEdgeUtils;
import org.eclipse.escet.cif.common.CifEvalException;
import org.eclipse.escet.cif.common.CifEvalUtils;
import org.eclipse.escet.cif.common.CifEventUtils;
import org.eclipse.escet.cif.common.CifGuardUtils;
import org.eclipse.escet.cif.common.CifGuardUtils.LocRefExprCreator;
import org.eclipse.escet.cif.common.CifLocationUtils;
import org.eclipse.escet.cif.common.CifScopeUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.CifValueUtils;
import org.eclipse.escet.cif.common.ConstantOrderer;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.InvKind;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Assignment;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.automata.Update;
import org.eclipse.escet.cif.metamodel.cif.declarations.Constant;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.BoolExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CastExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ConstantExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ElifExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IfExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IntExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.LocationExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryOperator;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.cif.metamodel.cif.types.TypeRef;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.box.MemoryCodeBox;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Pair;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;

/** CIF to UPPAAL transformation. */
public class CifToUppaal {
    /** Reserved keywords of UPPAAL. Taken from the UPPAAL help file 'Reserved Keywords' section. */
    private static final Set<String> RESERVED_KEYWORDS = set(
            // UPPAAL keywords.
            "chan", "clock", "bool", "int", "commit", "const", "urgent", "broadcast", "init", "process", "state",
            "guard", "sync", "assign", "system", "trans", "deadlock", "and", "or", "not", "imply", "true", "false",
            "for", "forall", "exists", "while", "do", "if", "else", "return", "typedef", "struct", "rate",
            "before_update", "after_update", "meta", "priority", "progress", "scalar", "select", "void", "default",
            // Reserved for future use.
            "switch", "case", "continue", "break");

    /** The UPPAAL XML document. */
    private Document doc;

    /** The UPPAAL XML document root 'nta' element. */
    private Element ntaElem;

    /** The next available unique location id. */
    private int nextLocId;

    /** Mapping from CIF objects and prefixes (may be {@code null}) to UPPAAL names. Does not apply to locations. */
    private Map<Pair<PositionObject, String>, String> nameMap = map();

    /** Used UPPAAL names. Does not apply to locations. */
    private Set<String> names = set();

    /**
     * Transform a CIF specification into a UPPAAL XML document.
     *
     * @param spec The CIF specification. The specification is modified in-place.
     * @return The UPPAAL XML document.
     */
    public Document transform(Specification spec) {
        // This transformation from CIF to UPPAAL 4 is partially based on the
        // following paper: D.E. Nadales Agut, M.A. Reniers, R.R.H.
        // Schiffelers, K.Y. Jørgensen, D.A. van Beek, A Semantic-Preserving
        // Transformation from the Compositional Interchange Format to UPPAAL,
        // 18th IFAC World Congress, 2011.

        // Initialization.
        nextLocId = 0;
        nameMap = map();
        names = set();

        // Remove/ignore I/O declarations, to increase the supported subset.
        RemoveIoDecls removeIoDecls = new RemoveIoDecls();
        removeIoDecls.transform(spec);
        if (removeIoDecls.haveAnySvgInputDeclarationsBeenRemoved()) {
            warn("The specification contains CIF/SVG input declarations, these will be ignored.");
        }

        // Perform preprocessing.
        new ElimComponentDefInst().transform(spec);
        new ElimTauEvent().transform(spec);
        new ElimStateEvtExclInvs().transform(spec);
        new ElimAlgVariables().transform(spec);
        new EnumsToInts().transform(spec);

        // Check preconditions.
        new CifToUppaalPreChecker().check(spec);

        // Collect various things from the CIF specification.
        List<Event> events = list();
        List<Constant> constants = list();
        List<Automaton> automata = list();
        List<Expression> stateInvs = list();
        collectEvents(spec, events);
        collectConstants(spec, constants);
        collectAutomata(spec, automata);
        collectComponentStateInvs(spec, stateInvs);

        List<DiscVariable> variables = list();
        for (Automaton aut: automata) {
            for (Declaration decl: aut.getDeclarations()) {
                if (decl instanceof DiscVariable) {
                    variables.add((DiscVariable)decl);
                }
            }
        }

        // Get initial locations indices.
        List<Integer> initLocIdxs = getInitLocsIdxs(automata);

        // Create dummy CIF 'send' automaton. Put it in a dummy specification,
        // to allow getting its absolute name.
        Automaton sendAut = newAutomaton();
        sendAut.setName("SendAut");
        Specification sendSpec = newSpecification();
        sendSpec.getComponents().add(sendAut);

        // Create UPPAAL XML document.
        doc = createUppaalXmlDocument();
        ntaElem = doc.getDocumentElement();

        // Add declarations text.
        CodeBox declsCode = new MemoryCodeBox(4);
        addChannels(events, declsCode);
        addConstants(constants, declsCode);
        addVariables(variables, declsCode);
        addLocationPointers(automata, initLocIdxs, declsCode);

        Element declsElem = doc.createElement("declaration");
        ntaElem.appendChild(declsElem);
        declsElem.setTextContent(declsCode.toString());

        // Get alphabets, per automaton.
        List<Set<Event>> syncAlphabets;
        List<Set<Event>> sendAlphabets;
        List<Set<Event>> recvAlphabets;
        List<Set<Event>> moniAlphabets;
        syncAlphabets = CifEventUtils.getAlphabets(automata);
        sendAlphabets = CifEventUtils.getSendAlphabets(automata);
        recvAlphabets = CifEventUtils.getReceiveAlphabets(automata);
        moniAlphabets = CifEventUtils.getMonitors(automata, syncAlphabets);

        // Merge global guards per event, optimized for monitors.
        List<Expression> sendGuards;
        sendGuards = CifGuardUtils.mergeGuards(automata, syncAlphabets, sendAlphabets, recvAlphabets, moniAlphabets,
                events, LocRefExprCreator.DEFAULT);

        // Eliminate monitors so that we don't have to take them into account
        // in the remainder of the transformation.
        new ElimMonitors().transform(spec);

        // Add templates.
        addTemplates(automata, initLocIdxs, events, variables, stateInvs, sendAut, sendGuards);

        // Add system.
        addSystem(automata, sendAut);

        // Return UPPAAL XML document.
        return doc;
    }

    /**
     * Returns the 0-based indices of the initial locations of the given automata.
     *
     * @param automata The CIF automata.
     * @return Per automaton, the 0-based index of its initial location.
     */
    private List<Integer> getInitLocsIdxs(List<Automaton> automata) {
        List<Integer> indices = listc(automata.size());
        for (Automaton aut: automata) {
            Set<Location> locs = getPossibleInitialLocs(aut, true);
            Assert.check(locs.size() == 1);
            Location loc = locs.iterator().next();
            int locIdx = aut.getLocations().indexOf(loc);
            Assert.check(locIdx >= 0);
            indices.add(locIdx);
        }
        return indices;
    }

    /**
     * Constructs a new UPPAAL XML document, with an 'nta' root node.
     *
     * @return A new UPPAAL XML document.
     */
    private Document createUppaalXmlDocument() {
        // Create document builder.
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }

        // Create and return document.
        DOMImplementation domImpl = builder.getDOMImplementation();
        DocumentType docType = domImpl.createDocumentType("nta", "-//Uppaal Team//DTD Flat System 1.1//EN",
                "http://www.it.uu.se/research/group/darts/uppaal/flat-1_1.dtd");
        Document doc = domImpl.createDocument(null, "nta", docType);
        return doc;
    }

    /**
     * Adds UPPAAL broadcast actions for CIF events.
     *
     * @param events The CIF events.
     * @param txt The {@link CodeBox} to which to add the text.
     */
    private void addChannels(List<Event> events, CodeBox txt) {
        for (Event event: events) {
            txt.add("broadcast chan %s;", getUppaalName(event, null));
        }
    }

    /**
     * Adds UPPAAL constants for CIF constants.
     *
     * @param constants The CIF constants.
     * @param txt The {@link CodeBox} to which to add the text.
     */
    private void addConstants(List<Constant> constants, CodeBox txt) {
        // Constants must be declared before they are used.
        constants = new ConstantOrderer().computeOrder(constants);

        // Add the ordered constants.
        for (Constant constant: constants) {
            txt.add("const %s %s = %s;", getUppaalType(constant.getType()), getUppaalName(constant, null),
                    getUppaalExpr(constant.getValue(), false));
        }
    }

    /**
     * Adds UPPAAL variables for CIF discrete variables.
     *
     * @param variables The CIF discrete variables.
     * @param txt The {@link CodeBox} to which to add the text.
     */
    private void addVariables(List<DiscVariable> variables, CodeBox txt) {
        for (DiscVariable var: variables) {
            // Get initial value expression. Make sure it is does not contain
            // variable references, etc.
            Expression value;
            if (var.getValue() == null) {
                value = CifValueUtils.getDefaultValue(var.getType(), null);
            } else {
                value = var.getValue().getValues().get(0);
                try {
                    value = CifEvalUtils.evalAsExpr(value, true);
                } catch (CifEvalException e) {
                    throw new RuntimeException(e);
                }
            }

            // Add variable and 'old' variant.
            txt.add("%s %s = %s;", getUppaalType(var.getType()), getUppaalName(var, null), getUppaalExpr(value, false));
            txt.add("meta %s %s = %s;", getUppaalType(var.getType()), getUppaalName(var, "OLD_"),
                    getUppaalExpr(value, false));
        }
    }

    /**
     * Adds UPPAAL location pointer variables for CIF automata.
     *
     * @param automata The CIF automata.
     * @param initLocIdxs Per automaton, the 0-based index of its initial location.
     * @param txt The {@link CodeBox} to which to add the text.
     */
    private void addLocationPointers(List<Automaton> automata, List<Integer> initLocIdxs, CodeBox txt) {
        for (int i = 0; i < automata.size(); i++) {
            Automaton aut = automata.get(i);
            int locCount = aut.getLocations().size();
            int locIdx = initLocIdxs.get(i);
            txt.add("int[0,%d] %s = %d;", locCount - 1, getUppaalName(aut, "LP_"), locIdx);
            txt.add("meta int[0,%d] %s = %d;", locCount - 1, getUppaalName(aut, "OLDLP_"), locIdx);
        }
    }

    /**
     * Adds UPPAAL templates for CIF automata.
     *
     * @param automata The CIF automata.
     * @param initLocIdxs Per automaton, the 0-based index of its initial location.
     * @param events The CIF events.
     * @param variables The CIF discrete variables.
     * @param stateInvs The state invariants from the components.
     * @param sendAut The dummy/empty send automaton.
     * @param sendGuards Per event, the 'send' self loop guard.
     */
    private void addTemplates(List<Automaton> automata, List<Integer> initLocIdxs, List<Event> events,
            List<DiscVariable> variables, List<Expression> stateInvs, Automaton sendAut, List<Expression> sendGuards)
    {
        // Add templates for the automata.
        for (int i = 0; i < automata.size(); i++) {
            addTemplate(automata.get(i), initLocIdxs.get(i));
        }

        // Add template for the sender.
        addSendTemplate(sendAut, events, variables, automata, stateInvs, sendGuards);
    }

    /**
     * Adds UPPAAL template for CIF automaton.
     *
     * @param aut The CIF automaton.
     * @param initLocIdx The 0-based index of the initial location of the automaton.
     */
    private void addTemplate(Automaton aut, int initLocIdx) {
        // Add 'template' element.
        Element templateElem = doc.createElement("template");
        ntaElem.appendChild(templateElem);

        // Add 'name' element.
        Element templateNameElem = doc.createElement("name");
        templateElem.appendChild(templateNameElem);
        templateNameElem.setTextContent(getUppaalName(aut, "Template_"));

        // Add locations.
        int firstLocId = nextLocId;
        List<Location> locations = aut.getLocations();
        for (Location loc: locations) {
            // Add 'location' element.
            Element locElem = doc.createElement("location");
            templateElem.appendChild(locElem);
            locElem.setAttribute("id", "id" + str(nextLocId));
            nextLocId++;

            // Add 'name' element, if not a nameless location.
            String locName = loc.getName();
            if (locName != null) {
                Element locNameElem = doc.createElement("name");
                locElem.appendChild(locNameElem);
                locNameElem.setTextContent(locName);
            }

            // Add invariant 'label' element. Only state invariants
            // as state/event exclusion invariants have been eliminated.
            List<Invariant> invs = loc.getInvariants();
            List<Expression> invPreds = listc(invs.size());
            for (Invariant inv: invs) {
                Assert.check(inv.getInvKind() == InvKind.STATE);
                invPreds.add(inv.getPredicate());
            }
            if (!invPreds.isEmpty()) {
                Element invElem = doc.createElement("label");
                locElem.appendChild(invElem);
                invElem.setAttribute("kind", "invariant");
                invElem.setTextContent(getUppaalExpr(invPreds, false));
            }

            // Add 'urgent' element.
            if (loc.isUrgent()) {
                Element urgentElem = doc.createElement("urgent");
                locElem.appendChild(urgentElem);
            }
        }

        // Add 'init' element.
        Element initElem = doc.createElement("init");
        templateElem.appendChild(initElem);
        initElem.setAttribute("ref", "id" + str(firstLocId + initLocIdx));

        // Add transitions.
        for (int i = 0; i < locations.size(); i++) {
            Location loc = locations.get(i);
            int sourceId = firstLocId + i;

            for (Edge edge: loc.getEdges()) {
                Location target = CifEdgeUtils.getTarget(edge);
                int targetIdx = locations.indexOf(target);
                Assert.check(targetIdx >= 0);
                int targetId = firstLocId + targetIdx;

                List<EdgeEvent> edgeEvents = edge.getEvents();
                Assert.check(!edgeEvents.isEmpty());
                for (EdgeEvent edgeEvent: edgeEvents) {
                    // Add 'transition' element.
                    Element transitionElem = doc.createElement("transition");
                    templateElem.appendChild(transitionElem);

                    // Add 'source' element.
                    Element sourceElem = doc.createElement("source");
                    transitionElem.appendChild(sourceElem);
                    sourceElem.setAttribute("ref", "id" + str(sourceId));

                    // Add 'target' element.
                    Element targetElem = doc.createElement("target");
                    transitionElem.appendChild(targetElem);
                    targetElem.setAttribute("ref", "id" + str(targetId));

                    // Add guard 'label' element.
                    List<Expression> guards = edge.getGuards();
                    if (!guards.isEmpty()) {
                        Element guardElem = doc.createElement("label");
                        transitionElem.appendChild(guardElem);
                        guardElem.setAttribute("kind", "guard");
                        guardElem.setTextContent(getUppaalExpr(guards, false));
                    }

                    // Add synchronisation 'label' element.
                    Event event = CifEventUtils.getEventFromEdgeEvent(edgeEvent);
                    Element syncElem = doc.createElement("label");
                    transitionElem.appendChild(syncElem);
                    syncElem.setAttribute("kind", "synchronisation");
                    String chanName = getUppaalName(event, null);
                    syncElem.setTextContent(chanName + "?");

                    // Get assignment text.
                    List<Update> updates = edge.getUpdates();
                    List<String> assignmentTxts = listc(updates.size() + 1);
                    for (Update update: updates) {
                        String asgn = getUppaalAssignment((Assignment)update);
                        assignmentTxts.add(asgn);
                    }
                    assignmentTxts.add(fmt("%s = %d", getUppaalName(aut, "LP_"), targetIdx));
                    String asgnTxt = StringUtils.join(assignmentTxts, ", ");

                    // Add assignment 'label' element.
                    Element asgnElem = doc.createElement("label");
                    transitionElem.appendChild(asgnElem);
                    asgnElem.setAttribute("kind", "assignment");
                    asgnElem.setTextContent(asgnTxt);
                }
            }
        }
    }

    /**
     * Adds UPPAAL template for the 'send' automaton.
     *
     * <p>
     * The 'send' automaton ensures proper event synchronization. The UPPAAL template has a single location, and
     * self-loops for every event in the CIF specification (excluding 'tau'). The guards of the self loops express the
     * conditions under which the events are globally enabled (guard wise) in the CIF specification. Every CIF event is
     * a broadcast channel in UPPAAL. If the self loop is enabled (guard wise), the 'send' template broadcasts over the
     * channel. All the other templates (for the CIF automata) receive the event. They can actually receive, as the
     * guard of the self loop ensures that. Together the 'send' self loop and the receive edges form the
     * synchronization.
     * </p>
     *
     * <p>
     * As part of the broadcast, 'OLD_x = x' assignments are executed, such that the 'old' values are available for the
     * assignments of the 'receive' edges. In UPPAAL, the assignments of the send happen before the assignments of the
     * receive, which is crucial for this scenario. Since variables in CIF can only be assigned by the automata that
     * declare them, each automaton participates with at most one edge, and edges may assign each variable at most once,
     * each variable is assigned at most once.
     * </p>
     *
     * @param sendAut The dummy/empty send automaton.
     * @param events The CIF events.
     * @param variables The CIF discrete variables.
     * @param automata The CIF automata.
     * @param stateInvs The state invariants from the components.
     * @param sendGuards Per event, the 'send' self loop guard.
     */
    private void addSendTemplate(Automaton sendAut, List<Event> events, List<DiscVariable> variables,
            List<Automaton> automata, List<Expression> stateInvs, List<Expression> sendGuards)
    {
        // Add 'template' element.
        Element templateElem = doc.createElement("template");
        ntaElem.appendChild(templateElem);

        // Add 'name' element.
        Element templateNameElem = doc.createElement("name");
        templateElem.appendChild(templateNameElem);
        templateNameElem.setTextContent(getUppaalName(sendAut, "Template_"));

        // Add 'location' element.
        int locId = nextLocId;
        nextLocId++;

        Element locElem = doc.createElement("location");
        templateElem.appendChild(locElem);
        locElem.setAttribute("id", "id" + str(locId));

        // Add state invariant 'label' element.
        if (!stateInvs.isEmpty()) {
            Element invElem = doc.createElement("label");
            locElem.appendChild(invElem);
            invElem.setAttribute("kind", "invariant");
            invElem.setTextContent(getUppaalExpr(stateInvs, false));
        }

        // Add 'init' element.
        Element initElem = doc.createElement("init");
        templateElem.appendChild(initElem);
        initElem.setAttribute("ref", "id" + str(locId));

        // Add transitions for all events, except for 'tau'.
        String asgnTxt = getSendAssignments(variables, automata);
        for (int i = 0; i < events.size(); i++) {
            Event event = events.get(i);

            // Add 'transition' element.
            Element transitionElem = doc.createElement("transition");
            templateElem.appendChild(transitionElem);

            // Add 'source' element.
            Element sourceElem = doc.createElement("source");
            transitionElem.appendChild(sourceElem);
            sourceElem.setAttribute("ref", "id" + str(locId));

            // Add 'target' element.
            Element targetElem = doc.createElement("target");
            transitionElem.appendChild(targetElem);
            targetElem.setAttribute("ref", "id" + str(locId));

            // Add guard 'label' element.
            Expression guard = sendGuards.get(i);
            Element guardElem = doc.createElement("label");
            transitionElem.appendChild(guardElem);
            guardElem.setAttribute("kind", "guard");
            guardElem.setTextContent(getUppaalExpr(guard, false));

            // Add synchronisation 'label' element.
            Element syncElem = doc.createElement("label");
            transitionElem.appendChild(syncElem);
            syncElem.setAttribute("kind", "synchronisation");
            String chanName = getUppaalName(event, null);
            syncElem.setTextContent(chanName + "!");

            // Add assignment 'label' element.
            Element asgnElem = doc.createElement("label");
            transitionElem.appendChild(asgnElem);
            asgnElem.setAttribute("kind", "assignment");
            asgnElem.setTextContent(asgnTxt);
        }
    }

    /**
     * Creates a UPPAAL assignment expression text for a self loop of the UPPAAL 'send' template for the given variables
     * and automata.
     *
     * @param variables The CIF variables.
     * @param automata The CIF automata.
     * @return The UPPAAL assignment expression text.
     */
    private String getSendAssignments(List<DiscVariable> variables, List<Automaton> automata) {
        List<String> txts = listc(variables.size() + automata.size());
        for (DiscVariable var: variables) {
            txts.add(fmt("%s = %s", getUppaalName(var, "OLD_"), getUppaalName(var, null)));
        }
        for (Automaton aut: automata) {
            txts.add(fmt("%s = %s", getUppaalName(aut, "OLDLP_"), getUppaalName(aut, "LP_")));
        }
        return StringUtils.join(txts, ", ");
    }

    /**
     * Adds a UPPAAL system for CIF automata.
     *
     * @param automata The CIF automata.
     * @param sendAut The dummy/empty send automaton.
     */
    private void addSystem(List<Automaton> automata, Automaton sendAut) {
        // Get system text.
        List<String> instantiations = listc(automata.size());
        CodeBox txt = new MemoryCodeBox(4);
        for (Automaton aut: automata) {
            txt.add("%s = %s();", getUppaalName(aut, null), getUppaalName(aut, "Template_"));
            instantiations.add(getUppaalName(aut, null));
        }
        txt.add("%s = %s();", getUppaalName(sendAut, null), getUppaalName(sendAut, "Template_"));
        instantiations.add(getUppaalName(sendAut, null));

        txt.add();
        txt.add("system %s;", StringUtils.join(instantiations, ", "));

        // Add 'system' element.
        Element systemElem = doc.createElement("system");
        ntaElem.appendChild(systemElem);
        systemElem.setTextContent(txt.toString());
    }

    /**
     * Converts a CIF type to a textual representation of a UPPAAL type.
     *
     * @param type The CIF type.
     * @return A textual representation of the UPPAAL type.
     */
    private String getUppaalType(CifType type) {
        if (type instanceof BoolType) {
            // Boolean.
            return "bool";
        } else if (type instanceof IntType) {
            // Integer.
            IntType itype = (IntType)type;
            int lower = CifTypeUtils.getLowerBound(itype);
            int upper = CifTypeUtils.getUpperBound(itype);
            return fmt("int[%d,%d]", lower, upper);
        } else if (type instanceof TypeRef) {
            // Type reference.
            return getUppaalType(((TypeRef)type).getType().getType());
        } else {
            // Unexpected.
            throw new RuntimeException("Unexpected type: " + type);
        }
    }

    /**
     * Converts CIF predicates to a textual representation of a UPPAAL expression, assuming conjunction semantics for
     * the CIF predicates.
     *
     * @param preds The CIF predicates, with conjunction semantics.
     * @param old Whether to transform variable references as 'old' variables ({@code true}) or normal variables
     *     ({@code false}).
     * @return A textual representation of the UPPAAL expression.
     */
    private String getUppaalExpr(List<Expression> preds, boolean old) {
        if (preds.isEmpty()) {
            return "true";
        }
        List<String> txts = listc(preds.size());
        for (Expression pred: preds) {
            String txt = getUppaalExpr(pred, old);
            if (preds.size() > 1) {
                txt = "(" + txt + ")";
            }
            txts.add(txt);
        }
        return StringUtils.join(txts, " && ");
    }

    /**
     * Converts a CIF expression to a textual representation of a UPPAAL expression.
     *
     * @param expr The CIF expression.
     * @param old Whether to transform variable references as 'old' variables ({@code true}) or normal variables
     *     ({@code false}).
     * @return A textual representation of the UPPAAL expression.
     */
    private String getUppaalExpr(Expression expr, boolean old) {
        if (expr instanceof BoolExpression) {
            // Boolean.
            return ((BoolExpression)expr).isValue() ? "true" : "false";
        } else if (expr instanceof IntExpression) {
            // Integer.
            return str(((IntExpression)expr).getValue());
        } else if (expr instanceof CastExpression) {
            // Cast to child type. Ignore.
            return getUppaalExpr(((CastExpression)expr).getChild(), old);
        } else if (expr instanceof UnaryExpression) {
            // Unary expression.
            UnaryExpression uexpr = (UnaryExpression)expr;
            String child = getUppaalExpr(uexpr.getChild(), old);
            UnaryOperator op = uexpr.getOperator();
            switch (op) {
                case INVERSE:
                    return fmt("!(%s)", child);
                case NEGATE:
                    return fmt("-(%s)", child);
                case PLUS:
                    return child;

                default:
                    throw new RuntimeException("Unexpected unop: " + op);
            }
        } else if (expr instanceof BinaryExpression) {
            // Binary expression.
            BinaryExpression bexpr = (BinaryExpression)expr;
            String l = getUppaalExpr(bexpr.getLeft(), old);
            String r = getUppaalExpr(bexpr.getRight(), old);
            BinaryOperator op = bexpr.getOperator();
            switch (op) {
                case IMPLICATION:
                    return fmt("!(%s) || (%s)", l, r);
                case BI_CONDITIONAL:
                    return fmt("(%s) == (%s)", l, r);
                case CONJUNCTION:
                    return fmt("(%s) && (%s)", l, r);
                case DISJUNCTION:
                    return fmt("(%s) || (%s)", l, r);
                case GREATER_EQUAL:
                    return fmt("(%s) >= (%s)", l, r);
                case GREATER_THAN:
                    return fmt("(%s) > (%s)", l, r);
                case LESS_EQUAL:
                    return fmt("(%s) <= (%s)", l, r);
                case LESS_THAN:
                    return fmt("(%s) < (%s)", l, r);
                case EQUAL:
                    return fmt("(%s) == (%s)", l, r);
                case UNEQUAL:
                    return fmt("(%s) != (%s)", l, r);
                case ADDITION:
                    return fmt("(%s) + (%s)", l, r);
                case SUBTRACTION:
                    return fmt("(%s) - (%s)", l, r);
                case MULTIPLICATION:
                    return fmt("(%s) * (%s)", l, r);
                case INTEGER_DIVISION:
                    return fmt("(%s) / (%s)", l, r);
                case MODULUS:
                    return fmt("(%s) %% (%s)", l, r);

                default:
                    throw new RuntimeException("Unexpected binop: " + op);
            }
        } else if (expr instanceof IfExpression) {
            // 'if' expression. Turn
            // 'if g1: v1 elif g2: v2 ... elif gn: vn else vn+1 end' into
            // 'g1 ? v1 : (g2 ? v2 : (... ? vn : vn+1))'.
            IfExpression ifExpr = (IfExpression)expr;
            String rslt = getUppaalExpr(ifExpr.getElse(), old);
            for (int i = ifExpr.getElifs().size() - 1; i >= 0; i--) {
                ElifExpression elifExpr = ifExpr.getElifs().get(i);
                rslt = fmt("(%s) ? (%s) : (%s)", getUppaalExpr(elifExpr.getGuards(), old),
                        getUppaalExpr(elifExpr.getThen(), old), rslt);
            }
            rslt = fmt("(%s) ? (%s) : (%s)", getUppaalExpr(ifExpr.getGuards(), old),
                    getUppaalExpr(ifExpr.getThen(), old), rslt);
            return rslt;
        } else if (expr instanceof ConstantExpression) {
            // Constant reference.
            return getUppaalName(((ConstantExpression)expr).getConstant(), null);
        } else if (expr instanceof DiscVariableExpression) {
            // Variable reference.
            String prefix = old ? "OLD_" : null;
            return getUppaalName(((DiscVariableExpression)expr).getVariable(), prefix);
        } else if (expr instanceof LocationExpression) {
            // Location reference.
            Location loc = ((LocationExpression)expr).getLocation();
            Automaton aut = CifLocationUtils.getAutomaton(loc);
            int locIdx = aut.getLocations().indexOf(loc);
            Assert.check(locIdx >= 0);
            String prefix = old ? "OLDLP_" : "LP_";
            return fmt("%s == %d", getUppaalName(aut, prefix), locIdx);
        } else {
            // Unexpected.
            throw new RuntimeException("Unexpected expr: " + expr);
        }
    }

    /**
     * Converts a CIF assignment to a textual representation of a UPPAAL assignment expression.
     *
     * @param asgn The CIF assignment.
     * @return A textual representation of the UPPAAL assignment expression.
     */
    private String getUppaalAssignment(Assignment asgn) {
        // CIF and UPPAAL both give an error for out of range variable
        // assignments.

        // Get variable name.
        Expression addr = asgn.getAddressable();
        DiscVariable var = ((DiscVariableExpression)addr).getVariable();
        String name = getUppaalName(var, null);

        // Get value expression text using 'old' variables.
        String valueTxt = getUppaalExpr(asgn.getValue(), true);

        // Return assignment expression text.
        return fmt("%s = %s", name, valueTxt);
    }

    /**
     * Returns the name to use in UPPAAL for the given CIF object. The given CIF object must be a named object. This
     * method does not apply to locations.
     *
     * @param obj The CIF object for which to return the name. Must not be a location.
     * @param prefix The prefix to use, or {@code null} to not use a prefix.
     * @return The name to use in UPPAAL.
     */
    private String getUppaalName(PositionObject obj, String prefix) {
        // If we already have a name, return that name.
        Pair<PositionObject, String> pair = pair(obj, prefix);
        String name = nameMap.get(pair);
        if (name != null) {
            return name;
        }

        // No name yet. Base it on the absolute name. Both UPPAAL and CIF use
        // case sensitive identifiers, with the same syntax.
        String absName = getAbsName(obj);
        name = absName.replace('.', '_');
        if (prefix != null) {
            name = prefix + name;
        }
        if (names.contains(name) || RESERVED_KEYWORDS.contains(name)) {
            String oldName = name;
            name = CifScopeUtils.getUniqueName(name, names, RESERVED_KEYWORDS);
            warn("Using \"%s\" in UPPAAL for \"%s\" instead of \"%s\".", name, absName, oldName);
        }
        nameMap.put(pair, name);
        names.add(name);
        return name;
    }

    /**
     * Collects events, recursively.
     *
     * @param comp The CIF component in which to recursively look for events.
     * @param events The events collected so far. Is modified in-place.
     */
    private static void collectEvents(ComplexComponent comp, List<Event> events) {
        // Add all events of the component.
        for (Declaration decl: comp.getDeclarations()) {
            if (decl instanceof Event) {
                events.add((Event)decl);
            }
        }

        // Recurse into child components.
        if (comp instanceof Group) {
            for (Component child: ((Group)comp).getComponents()) {
                collectEvents((ComplexComponent)child, events);
            }
        }
    }

    /**
     * Collects constants, recursively.
     *
     * @param comp The CIF component in which to recursively look for constants.
     * @param constants The constants collected so far. Is modified in-place.
     */
    private static void collectConstants(ComplexComponent comp, List<Constant> constants) {
        // Add all constants of the component.
        for (Declaration decl: comp.getDeclarations()) {
            if (decl instanceof Constant) {
                constants.add((Constant)decl);
            }
        }

        // Recurse into child components.
        if (comp instanceof Group) {
            for (Component child: ((Group)comp).getComponents()) {
                collectConstants((ComplexComponent)child, constants);
            }
        }
    }

    /**
     * Collect automata, recursively.
     *
     * @param comp The CIF component in which to recursively look for automata.
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
     * Collects state invariant predicates from components, recursively.
     *
     * @param comp The CIF component in which to recursively look for state invariants.
     * @param invs The state invariant predicates collected so far. Is modified in-place.
     */
    private static void collectComponentStateInvs(ComplexComponent comp, List<Expression> invs) {
        // Add all state invariant predicates of the component.
        // State/event exclusion invariants have been eliminated.
        for (Invariant inv: comp.getInvariants()) {
            Assert.check(inv.getInvKind() == InvKind.STATE);
            invs.add(inv.getPredicate());
        }

        // Recurse into child components.
        if (comp instanceof Group) {
            for (Component child: ((Group)comp).getComponents()) {
                collectComponentStateInvs((ComplexComponent)child, invs);
            }
        }
    }
}
