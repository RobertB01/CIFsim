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

package org.eclipse.escet.cif.cif2supremica;

import static java.util.Collections.EMPTY_SET;
import static org.eclipse.escet.cif.common.CifAddressableUtils.collectAddrVars;
import static org.eclipse.escet.cif.common.CifEvalUtils.evalPreds;
import static org.eclipse.escet.cif.common.CifScopeUtils.getSymbolNamesForScope;
import static org.eclipse.escet.cif.common.CifScopeUtils.getUniqueName;
import static org.eclipse.escet.cif.common.CifTextUtils.getAbsName;
import static org.eclipse.escet.cif.common.CifValueUtils.makeFalse;
import static org.eclipse.escet.cif.common.CifValueUtils.makeTrue;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newAssignment;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newAutomaton;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBoolType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newDiscVariableExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEdge;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEdgeEvent;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEvent;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEventExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newLocation;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newUnaryExpression;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.warn;
import static org.eclipse.escet.common.emf.EMFHelper.deepclone;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Sets.setc;
import static org.eclipse.escet.common.java.Strings.str;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.escet.cif.cif2cif.AddDefaultInitialValues;
import org.eclipse.escet.cif.cif2cif.ElimAlgVariables;
import org.eclipse.escet.cif.cif2cif.ElimComponentDefInst;
import org.eclipse.escet.cif.cif2cif.ElimEnums;
import org.eclipse.escet.cif.cif2cif.ElimLocRefExprs;
import org.eclipse.escet.cif.cif2cif.ElimMonitors;
import org.eclipse.escet.cif.cif2cif.ElimSelf;
import org.eclipse.escet.cif.cif2cif.ElimTypeDecls;
import org.eclipse.escet.cif.cif2cif.RemoveIoDecls;
import org.eclipse.escet.cif.cif2cif.RemovePositionInfo;
import org.eclipse.escet.cif.cif2cif.SimplifyOthers;
import org.eclipse.escet.cif.cif2cif.SimplifyValues;
import org.eclipse.escet.cif.common.CifEvalException;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifValueUtils;
import org.eclipse.escet.cif.common.ConstantOrderer;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.SupKind;
import org.eclipse.escet.cif.metamodel.cif.automata.Alphabet;
import org.eclipse.escet.cif.metamodel.cif.automata.Assignment;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.automata.Update;
import org.eclipse.escet.cif.metamodel.cif.declarations.Constant;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumLiteral;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.BoolExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CastExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ConstantExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EnumLiteralExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IntExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryOperator;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.EnumType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * CIF to Supremica transformation. See {@link CifToSupremicaPreChecker} for the preconditions for this transformation.
 */
public class CifToSupremica {
    /** XMLNS namespace URI. */
    private static final String XMLNS_NS = "http://www.w3.org/2000/xmlns/";

    /** Supremica 'base' namespace URI. */
    private static final String SUPREMICA_BASE_NS = "http://waters.sourceforge.net/xsd/base";

    /** Supremica 'module' namespace URI. */
    private static final String SUPREMICA_MODULE_NS = "http://waters.sourceforge.net/xsd/module";

    /** Constructor for the {@link CifToSupremica} class. */
    private CifToSupremica() {
        // Static class.
    }

    /**
     * Transform a CIF specification into a Supremica XML document.
     *
     * @param spec The CIF specification. The specification is modified in-place during preprocessing.
     * @param moduleName The name of the Supremica module to generate.
     * @param elimEnums Should enumerations be eliminated?
     * @return The Supremica XML document.
     */
    public static Document transform(Specification spec, String moduleName, boolean elimEnums) {
        // Remove/ignore I/O declarations, to increase the supported subset.
        new RemoveIoDecls().transform(spec);

        // Check preconditions and perform further preprocessing.
        new CifToSupremicaPreChecker().check(spec);
        preprocess(spec, elimEnums);

        // Modify the CIF specification for invariants.
        modifyInvariants(spec);

        // Modify the CIF specification to ensure proper jumping behavior.
        modifyJumping(spec);

        // Create Supremica XML document, with a module.
        Document supremicaDoc = createSupremicaXmlDocument();
        Element moduleElem = supremicaDoc.getDocumentElement();
        moduleElem.setAttribute("Name", moduleName);

        // Add constants.
        List<Constant> constants = list();
        collectConstants(spec, constants);
        if (!constants.isEmpty()) {
            Element constsElem = supremicaDoc.createElement("ConstantAliasList");
            moduleElem.appendChild(constsElem);
            addConstants(supremicaDoc, constsElem, constants);
        }

        // Add events.
        Element eventsElem = supremicaDoc.createElement("EventDeclList");
        moduleElem.appendChild(eventsElem);
        addEvents(supremicaDoc, eventsElem, spec);

        // Add ':accepting' event for marking.
        Element acceptElem = supremicaDoc.createElement("EventDecl");
        eventsElem.appendChild(acceptElem);
        acceptElem.setAttribute("Kind", "PROPOSITION");
        acceptElem.setAttribute("Name", ":accepting");

        // Collect marker predicates of components.
        Map<DiscVariable, Expression> markeds = map();
        collectComponentMarkeds(spec, markeds);

        // Add components (automata and variables).
        Element compsElem = supremicaDoc.createElement("ComponentList");
        moduleElem.appendChild(compsElem);
        addComponents(supremicaDoc, compsElem, spec, markeds);
        if (!compsElem.hasChildNodes()) {
            moduleElem.removeChild(compsElem);
        }

        // Make sure all marker predicates have been used.
        Assert.check(markeds.isEmpty());

        // Return Supremica XML document.
        return supremicaDoc;
    }

    /**
     * Performs preprocessing to get the CIF specification in a form that can be transformed to Supremica.
     *
     * @param spec The CIF specification to preprocess.
     * @param elimEnums Should enumerations be eliminated?
     */
    private static void preprocess(Specification spec, boolean elimEnums) {
        // Remove position information, for performance.
        new RemovePositionInfo().transform(spec);

        // Eliminate component definitions/instantiations, as Supremica doesn't
        // have them.
        new ElimComponentDefInst().transform(spec);

        // Add default initial values, to ease transformation to Supremica.
        new AddDefaultInitialValues().transform(spec);

        // Eliminate automaton 'self' references. This must be done before
        // elimination of algebraic variables.
        new ElimSelf().transform(spec);

        // Eliminate algebraic variables, as Supremica does not have a
        // compatible concept in which they can be expressed (relatively)
        // directly.
        new ElimAlgVariables().transform(spec);

        // Eliminate location references in expressions, as Supremica does not
        // current support this. The 'aut.curr' variable may be introduced in
        // future versions, but doesn't currently work.
        new ElimLocRefExprs().transform(spec);

        // Eliminate type declarations, as Supremica does not have a compatible
        // concept in which they can be expressed (relatively) directly.
        new ElimTypeDecls().transform(spec);

        // Eliminate monitors, as Supremica does not have a compatible concept
        // in which they can be expressed (relatively) directly.
        new ElimMonitors().transform(spec);

        // Eliminate enumerations, if requested. Supremica supports
        // enumerations in the language, but not in all its algorithms. We
        // eliminate them as late as possible, as other CIF to CIF
        // preprocessing transformations (such as the elimination of location
        // reference expressions) may introduce additional enumerations. We
        // eliminate them before we simplify values, to allow simplification
        // of the result of this elimination.
        if (elimEnums) {
            new ElimEnums().transform(spec);
        }

        // Simplify values, to simplify the resulting Supremica model. Note
        // that this inlines constants, and may lead to performance issues.
        // We may want to consider using a more optimized variant of value
        // simplification.
        new SimplifyValues().transform(spec);

        // Simplify other things, to simplify the resulting Supremica model.
        new SimplifyOthers().transform(spec);
    }

    /**
     * Constructs a new Supremica XML document, with a 'Module' root node.
     *
     * @return A new Supremica XML document.
     */
    private static Document createSupremicaXmlDocument() {
        // Create document builder.
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);

        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }

        // Create document.
        DOMImplementation domImpl = builder.getDOMImplementation();
        Document doc = domImpl.createDocument(SUPREMICA_MODULE_NS, "Module", null);
        doc.setXmlStandalone(true);

        // Add namespaces.
        Element root = doc.getDocumentElement();
        root.setAttributeNS(XMLNS_NS, "xmlns:ns2", SUPREMICA_BASE_NS);
        root.setAttributeNS(XMLNS_NS, "xmlns", SUPREMICA_MODULE_NS);

        // Return document.
        return doc;
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
     * Adds constant aliases.
     *
     * @param doc The Supremica XML document that contains the parent.
     * @param parent The parent to which to add the new Supremica constant aliases.
     * @param constants The CIF constants to add.
     */
    private static void addConstants(Document doc, Element parent, List<Constant> constants) {
        // Order the constants by their dependencies.
        constants = new ConstantOrderer().computeOrder(constants);

        // Add constant aliases.
        for (Constant constant: constants) {
            // Add constant alias.
            Element constElem = doc.createElement("ConstantAlias");
            parent.appendChild(constElem);
            constElem.setAttribute("Name", getSupremicaName(constant));

            // Add value expression.
            Element exprElem = doc.createElement("ConstantAliasExpression");
            constElem.appendChild(exprElem);
            addExpr(doc, exprElem, constant.getValue());
        }
    }

    /**
     * Adds events declarations, recursively.
     *
     * @param doc The Supremica XML document that contains the parent.
     * @param parent The parent to which to add the new Supremica event declarations.
     * @param comp The CIF component in which to recursively look for events to add.
     */
    private static void addEvents(Document doc, Element parent, ComplexComponent comp) {
        // Add all events of the component.
        for (Declaration decl: comp.getDeclarations()) {
            if (!(decl instanceof Event)) {
                continue;
            }

            Event event = (Event)decl;
            addEvent(doc, parent, event);
        }

        // Recurse into child components.
        if (comp instanceof Group) {
            for (Component child: ((Group)comp).getComponents()) {
                addEvents(doc, parent, (ComplexComponent)child);
            }
        }
    }

    /**
     * Adds an event declarations.
     *
     * @param doc The Supremica XML document that contains the parent.
     * @param parent The parent to which to add the new Supremica event declaration.
     * @param event The CIF event to add.
     */
    private static void addEvent(Document doc, Element parent, Event event) {
        Element eventElem = doc.createElement("EventDecl");
        parent.appendChild(eventElem);
        if (event.getControllable()) {
            eventElem.setAttribute("Kind", "CONTROLLABLE");
        } else {
            eventElem.setAttribute("Kind", "UNCONTROLLABLE");
        }
        eventElem.setAttribute("Name", getSupremicaName(event));
    }

    /**
     * Adds components (automata and variables), recursively.
     *
     * @param doc The Supremica XML document that contains the parent.
     * @param parent The parent to which to add the new Supremica components.
     * @param comp The CIF component in which to recursively look for automata and variables.
     * @param markeds The mapping from discrete variables to their marked values. The mapping may be incomplete. The
     *     mapping is modified in-place by removing used entries.
     */
    private static void addComponents(Document doc, Element parent, ComplexComponent comp,
            Map<DiscVariable, Expression> markeds)
    {
        // Add variables.
        for (Declaration decl: comp.getDeclarations()) {
            if (decl instanceof DiscVariable) {
                addVariable(doc, parent, (DiscVariable)decl, markeds);
            }
        }

        // Groups and automata.
        if (comp instanceof Automaton) {
            addAutomaton(doc, parent, (Automaton)comp);
        } else {
            Assert.check(comp instanceof Group);
            for (Component child: ((Group)comp).getComponents()) {
                addComponents(doc, parent, (ComplexComponent)child, markeds);
            }
        }
    }

    /**
     * Adds a discrete variable.
     *
     * @param doc The Supremica XML document that contains the parent.
     * @param parent The parent to which to add the new Supremica variable.
     * @param var The CIF variable to add.
     * @param markeds The mapping from discrete variables to their marked values. The mapping may be incomplete. The
     *     mapping is modified in-place by removing used entries.
     */
    private static void addVariable(Document doc, Element parent, DiscVariable var,
            Map<DiscVariable, Expression> markeds)
    {
        // Add component.
        Element varElem = doc.createElement("VariableComponent");
        parent.appendChild(varElem);
        varElem.setAttribute("Name", getSupremicaName(var));

        // Add range/type.
        Element rangeElem = doc.createElement("VariableRange");
        varElem.appendChild(rangeElem);
        addType(doc, rangeElem, var.getType());

        // Add initial value, as 'var == value'.
        Element initElem = doc.createElement("VariableInitial");
        varElem.appendChild(initElem);

        Element initEqElem = doc.createElement("BinaryExpression");
        initElem.appendChild(initEqElem);
        initEqElem.setAttribute("Operator", "==");

        Element initIdElem = doc.createElement("SimpleIdentifier");
        initEqElem.appendChild(initIdElem);
        initIdElem.setAttribute("Name", getSupremicaName(var));

        addExpr(doc, initEqElem, var.getValue().getValues().get(0));

        // Get marking. Also removes the entry from the mapping, as it is used.
        Expression marked = markeds.remove(var);
        if (marked == null) {
            // Warn about missing marking.
            warn("None of the values of variable \"%s\" is marked. In Supremica they will implicitly all be marked.",
                    getAbsName(var));
        } else {
            // Add marking.
            Element markElem = doc.createElement("VariableMarking");
            varElem.appendChild(markElem);

            Element acceptElem = doc.createElement("SimpleIdentifier");
            markElem.appendChild(acceptElem);
            acceptElem.setAttribute("Name", ":accepting");

            Element markEqElem = doc.createElement("BinaryExpression");
            markElem.appendChild(markEqElem);
            markEqElem.setAttribute("Operator", "==");

            Element markIdElem = doc.createElement("SimpleIdentifier");
            markEqElem.appendChild(markIdElem);
            markIdElem.setAttribute("Name", getSupremicaName(var));

            addExpr(doc, markEqElem, marked);
        }
    }

    /**
     * Adds an automaton.
     *
     * @param doc The Supremica XML document that contains the parent.
     * @param parent The parent to which to add the new Supremica automaton.
     * @param aut The CIF automaton to add.
     */
    private static void addAutomaton(Document doc, Element parent, Automaton aut) {
        // Add component.
        Element compElem = doc.createElement("SimpleComponent");
        parent.appendChild(compElem);
        compElem.setAttribute("Name", getSupremicaName(aut));
        switch (aut.getKind()) {
            case NONE:
                throw new RuntimeException("Precond error: kindless aut.");

            case PLANT:
                compElem.setAttribute("Kind", "PLANT");
                break;

            case REQUIREMENT:
                compElem.setAttribute("Kind", "SPEC");
                break;

            case SUPERVISOR:
                compElem.setAttribute("Kind", "SUPERVISOR");
                break;
        }

        // Get alphabet, if specified, to detect disabled events.
        Alphabet alphabet = aut.getAlphabet();
        Set<Event> alphaSet = null;
        if (alphabet != null) {
            int count = alphabet.getEvents().size();
            if (count == 0) {
                // Empty alphabet, can't be larger than what is used.
            } else {
                alphaSet = setc(count);
                for (Expression eventRef: alphabet.getEvents()) {
                    alphaSet.add(((EventExpression)eventRef).getEvent());
                }
            }
        }

        // Add graph, non-deterministic, as CIF automata may be
        // non-deterministic.
        Element graphElem = doc.createElement("Graph");
        compElem.appendChild(graphElem);
        graphElem.setAttribute("Deterministic", "false");

        // Add nodes.
        Element nodesElem = doc.createElement("NodeList");
        graphElem.appendChild(nodesElem);

        boolean anyMarked = false;
        Location initLoc = null;
        for (Location loc: aut.getLocations()) {
            // Add node.
            Element nodeElem = doc.createElement("SimpleNode");
            nodesElem.appendChild(nodeElem);

            // Set name.
            String name = loc.getName();
            if (name == null) {
                name = "X";
            }
            nodeElem.setAttribute("Name", name);

            // Set initial.
            boolean initial;
            try {
                initial = loc.getInitials().isEmpty() ? false : evalPreds(loc.getInitials(), true, true);
            } catch (CifEvalException e) {
                // Already evaluated by precondition checker.
                throw new RuntimeException("Precond error.", e);
            }
            if (initial) {
                Assert.check(initLoc == null);
                initLoc = loc;
                nodeElem.setAttribute("Initial", "true");
            }

            // Set marked.
            if (!loc.getMarkeds().isEmpty()) {
                boolean marked;
                try {
                    marked = evalPreds(loc.getMarkeds(), false, true);
                } catch (CifEvalException e) {
                    // Already evaluated by precondition checker.
                    throw new RuntimeException("Precond error.", e);
                }
                if (marked) {
                    anyMarked = true;
                    Element eventsElem = doc.createElement("EventList");
                    nodeElem.appendChild(eventsElem);

                    Element idElem = doc.createElement("SimpleIdentifier");
                    eventsElem.appendChild(idElem);
                    idElem.setAttribute("Name", ":accepting");
                }
            }
        }

        // Warn about no marked locations.
        if (!anyMarked) {
            warn("None of the locations of CIF automaton \"%s\" is marked. In Supremica they will implicitly all be "
                    + "marked.", getAbsName(aut));
        }

        // Add edges.
        Element edgesElem = doc.createElement("EdgeList");
        graphElem.appendChild(edgesElem);
        for (Location loc: aut.getLocations()) {
            for (Edge edge: loc.getEdges()) {
                // Get source and target location names.
                Location tgtLoc = edge.getTarget();
                if (tgtLoc == null) {
                    tgtLoc = loc;
                }

                String src = loc.getName();
                String tgt = tgtLoc.getName();
                if (src == null) {
                    src = "X";
                }
                if (tgt == null) {
                    tgt = "X";
                }

                // Add an edge in Supremica for each edge label. Supremica
                // supports multiple events on an edge, but it doesn't always
                // work as expected.
                Assert.check(!edge.getEvents().isEmpty());
                for (EdgeEvent edgeEvent: edge.getEvents()) {
                    // Add edge.
                    Element edgeElem = doc.createElement("Edge");
                    edgesElem.appendChild(edgeElem);

                    // Set source and target.
                    edgeElem.setAttribute("Source", src);
                    edgeElem.setAttribute("Target", tgt);

                    // Add event.
                    EventExpression eventRef = (EventExpression)edgeEvent.getEvent();
                    Event event = eventRef.getEvent();

                    Element lblsElem = doc.createElement("LabelBlock");
                    edgeElem.appendChild(lblsElem);

                    Element lblIdElem = doc.createElement("SimpleIdentifier");
                    lblsElem.appendChild(lblIdElem);
                    lblIdElem.setAttribute("Name", getSupremicaName(event));

                    // Event occur on at least one edge.
                    if (alphaSet != null) {
                        alphaSet.remove(event);
                    }

                    // Add guards/updates.
                    Element blockElem;
                    boolean hasGuard = !edge.getGuards().isEmpty();
                    boolean hasUpdate = !edge.getUpdates().isEmpty();
                    if (hasGuard || hasUpdate) {
                        blockElem = doc.createElement("GuardActionBlock");
                        edgeElem.appendChild(blockElem);

                        // Add guards.
                        if (hasGuard) {
                            Element guardsElem = doc.createElement("Guards");
                            blockElem.appendChild(guardsElem);
                            for (Expression guard: edge.getGuards()) {
                                addExpr(doc, guardsElem, guard);
                            }
                        }

                        // Add updates.
                        if (hasUpdate) {
                            Element actsElem = doc.createElement("Actions");
                            blockElem.appendChild(actsElem);
                            for (Update update: edge.getUpdates()) {
                                addUpdate(doc, actsElem, update);
                            }
                        }
                    }
                }
            }
        }

        // Disable globally disabled events (that don't occur on any edges).
        // In Supremica the alphabet is determined from the events on the
        // edges, so for events in the alphabet that don't occur on any edges,
        // we need to add them. We add a self loop to the initial location
        // with a '0' (false) guard.
        if (alphaSet != null) {
            // Get location name.
            if (initLoc == null) {
                throw new AssertionError();
            }
            String locName = initLoc.getName();
            if (locName == null) {
                locName = "X";
            }

            // Add self loop for each disabled event.
            for (Event event: alphaSet) {
                // Add edge.
                Element edgeElem = doc.createElement("Edge");
                edgesElem.appendChild(edgeElem);
                edgeElem.setAttribute("Source", locName);
                edgeElem.setAttribute("Target", locName);

                // Add event.
                Element lblsElem = doc.createElement("LabelBlock");
                edgeElem.appendChild(lblsElem);

                Element lblIdElem = doc.createElement("SimpleIdentifier");
                lblsElem.appendChild(lblIdElem);
                lblIdElem.setAttribute("Name", getSupremicaName(event));

                // Add guard.
                Element blockElem = doc.createElement("GuardActionBlock");
                edgeElem.appendChild(blockElem);

                Element guardsElem = doc.createElement("Guards");
                blockElem.appendChild(guardsElem);

                Element guardElem = doc.createElement("IntConstant");
                guardsElem.appendChild(guardElem);
                guardElem.setAttribute("Value", "0");
            }
        }

        // If no edges, then no 'EdgeList'.
        if (!edgesElem.hasChildNodes()) {
            graphElem.removeChild(edgesElem);
        }
    }

    /**
     * Adds a type.
     *
     * @param doc The Supremica XML document that contains the parent.
     * @param parent The parent to which to add the type.
     * @param type The CIF type to add.
     */
    private static void addType(Document doc, Element parent, CifType type) {
        if (type instanceof BoolType) {
            // Supremica has boolean types internally, but they don't appear to
            // be available in the syntax for end users.
            Element rangeElem = doc.createElement("BinaryExpression");
            parent.appendChild(rangeElem);
            rangeElem.setAttribute("Operator", "..");

            Element lowerElem = doc.createElement("IntConstant");
            rangeElem.appendChild(lowerElem);
            lowerElem.setAttribute("Value", "0");

            Element upperElem = doc.createElement("IntConstant");
            rangeElem.appendChild(upperElem);
            upperElem.setAttribute("Value", "1");
        } else if (type instanceof IntType) {
            IntType itype = (IntType)type;

            Element rangeElem = doc.createElement("BinaryExpression");
            parent.appendChild(rangeElem);
            rangeElem.setAttribute("Operator", "..");

            Element lowerElem = doc.createElement("IntConstant");
            rangeElem.appendChild(lowerElem);
            lowerElem.setAttribute("Value", str(itype.getLower()));

            Element upperElem = doc.createElement("IntConstant");
            rangeElem.appendChild(upperElem);
            upperElem.setAttribute("Value", str(itype.getUpper()));
        } else if (type instanceof EnumType) {
            EnumType etype = (EnumType)type;

            Element enumElem = doc.createElement("EnumSetExpression");
            parent.appendChild(enumElem);

            for (EnumLiteral literal: etype.getEnum().getLiterals()) {
                // Just add the literals. See 'getSupremicaName' for details.
                Element litElem = doc.createElement("SimpleIdentifier");
                enumElem.appendChild(litElem);
                litElem.setAttribute("Name", getSupremicaName(literal));
            }
        } else {
            throw new RuntimeException("Precond error: " + type);
        }
    }

    /**
     * Adds an expression.
     *
     * @param doc The Supremica XML document that contains the parent.
     * @param parent The parent to which to add the new expression.
     * @param expr The CIF expression to add.
     */
    private static void addExpr(Document doc, Element parent, Expression expr) {
        if (expr instanceof BoolExpression) {
            // Supremica has boolean types internally, but they don't appear to
            // be available in the syntax for end users.
            BoolExpression bexpr = (BoolExpression)expr;
            Element elem = doc.createElement("IntConstant");
            parent.appendChild(elem);
            elem.setAttribute("Value", bexpr.isValue() ? "1" : "0");
        } else if (expr instanceof IntExpression) {
            IntExpression iexpr = (IntExpression)expr;
            Element elem = doc.createElement("IntConstant");
            parent.appendChild(elem);
            elem.setAttribute("Value", str(iexpr.getValue()));
        } else if (expr instanceof ConstantExpression) {
            Constant constant = ((ConstantExpression)expr).getConstant();
            Element elem = doc.createElement("SimpleIdentifier");
            parent.appendChild(elem);
            elem.setAttribute("Name", getSupremicaName(constant));
        } else if (expr instanceof DiscVariableExpression) {
            DiscVariable var = ((DiscVariableExpression)expr).getVariable();
            Element elem = doc.createElement("SimpleIdentifier");
            parent.appendChild(elem);
            elem.setAttribute("Name", getSupremicaName(var));
        } else if (expr instanceof EnumLiteralExpression) {
            EnumLiteral lit = ((EnumLiteralExpression)expr).getLiteral();
            Element elem = doc.createElement("SimpleIdentifier");
            parent.appendChild(elem);
            elem.setAttribute("Name", getSupremicaName(lit));
        } else if (expr instanceof BinaryExpression) {
            // Binary operators of Supremica and CIF both map to Java
            // operators, and thus have same semantics.
            BinaryExpression bexpr = (BinaryExpression)expr;
            BinaryOperator op = bexpr.getOperator();

            Element elem = doc.createElement("BinaryExpression");
            parent.appendChild(elem);

            String opTxt;
            switch (op) {
                case BI_CONDITIONAL:
                    opTxt = "==";
                    break; // left == right
                case IMPLICATION:
                    opTxt = "|";
                    break; // !left | right
                case CONJUNCTION:
                    opTxt = "&";
                    break;
                case DISJUNCTION:
                    opTxt = "|";
                    break;
                case ADDITION:
                    opTxt = "+";
                    break;
                case SUBTRACTION:
                    opTxt = "-";
                    break;
                case MULTIPLICATION:
                    opTxt = "*";
                    break;
                case INTEGER_DIVISION:
                    opTxt = "/";
                    break;
                case MODULUS:
                    opTxt = "%";
                    break;
                case EQUAL:
                    opTxt = "==";
                    break;
                case UNEQUAL:
                    opTxt = "!=";
                    break;
                case GREATER_EQUAL:
                    opTxt = ">=";
                    break;
                case GREATER_THAN:
                    opTxt = ">";
                    break;
                case LESS_EQUAL:
                    opTxt = "<=";
                    break;
                case LESS_THAN:
                    opTxt = "<";
                    break;
                default:
                    throw new RuntimeException("Precond error: " + op);
            }
            elem.setAttribute("Operator", opTxt);

            if (op == BinaryOperator.IMPLICATION) {
                Element elem2 = doc.createElement("UnaryExpression");
                elem.appendChild(elem2);
                elem2.setAttribute("Operator", "!");
                addExpr(doc, elem2, bexpr.getLeft());
            } else {
                addExpr(doc, elem, bexpr.getLeft());
            }

            addExpr(doc, elem, bexpr.getRight());
        } else if (expr instanceof UnaryExpression) {
            // Unary operators of Supremica and CIF both map to Java operators,
            // and thus have same semantics.
            UnaryExpression uexpr = (UnaryExpression)expr;
            UnaryOperator op = uexpr.getOperator();

            if (op == UnaryOperator.PLUS) {
                // Special case for unary '+'.
                addExpr(doc, parent, uexpr.getChild());
            } else {
                // Generic case.
                Element elem = doc.createElement("UnaryExpression");
                parent.appendChild(elem);

                String opTxt;
                switch (op) {
                    case INVERSE:
                        opTxt = "!";
                        break;
                    case NEGATE:
                        opTxt = "-";
                        break;
                    default:
                        throw new RuntimeException("Precond error: " + op);
                }
                elem.setAttribute("Operator", opTxt);
                addExpr(doc, elem, uexpr.getChild());
            }
        } else if (expr instanceof CastExpression) {
            // Ignore casting to child type.
            addExpr(doc, parent, ((CastExpression)expr).getChild());
        } else {
            throw new RuntimeException("Precond error: " + expr);
        }
    }

    /**
     * Adds an edge update.
     *
     * @param doc The Supremica XML document that contains the parent.
     * @param parent The parent to which to add the new update.
     * @param update The CIF update to add.
     */
    private static void addUpdate(Document doc, Element parent, Update update) {
        // Only assignments, per precondition.
        Assignment asgn = (Assignment)update;

        // Add assignment.
        Element asgnElem = doc.createElement("BinaryExpression");
        parent.appendChild(asgnElem);
        asgnElem.setAttribute("Operator", "=");
        addExpr(doc, asgnElem, asgn.getAddressable());
        addExpr(doc, asgnElem, asgn.getValue());
    }

    /**
     * Collects state invariants, recursively. Only invariants in components are collected, as it is a precondition of
     * the transformation that locations don't have any invariants. Also, only state invariants are collected, as it is
     * a precondition that no other invariants are present.
     *
     * @param comp The CIF component in which to recursively look for state invariants.
     * @param invs The state invariants collected so far. Is modified in-place.
     */
    private static void collectStateInvs(ComplexComponent comp, List<Invariant> invs) {
        // Add all state invariants of the component.
        invs.addAll(comp.getInvariants());

        // Recurse into child components.
        if (comp instanceof Group) {
            for (Component child: ((Group)comp).getComponents()) {
                collectStateInvs((ComplexComponent)child, invs);
            }
        }
    }

    /**
     * Collects marker predicates, recursively. Only marker predicate in components are collected.
     *
     * @param comp The CIF component in which to recursively look for marker predicates.
     * @param markeds Mapping from discrete variables to their marked values, as collected so far. Is modified in-place.
     */
    private static void collectComponentMarkeds(ComplexComponent comp, Map<DiscVariable, Expression> markeds) {
        // Add all marker predicates of the component.
        for (Expression marked: comp.getMarkeds()) {
            BinaryExpression bexpr = (BinaryExpression)marked;
            DiscVariableExpression vref = (DiscVariableExpression)bexpr.getLeft();
            DiscVariable var = vref.getVariable();
            Expression prev = markeds.put(var, bexpr.getRight());
            Assert.check(prev == null);
        }

        // Recurse into child components.
        if (comp instanceof Group) {
            for (Component child: ((Group)comp).getComponents()) {
                collectComponentMarkeds((ComplexComponent)child, markeds);
            }
        }
    }

    /**
     * Modifies the CIF specification so that subsequent conversions will correctly handle state invariants. Assumes
     * that all invariants are to be interpreted as requirements, as guaranteed by the precondition checker. Also
     * assumes that all invariants are state invariants, as guaranteed by the precondition checker.
     *
     * <p>
     * A new uncontrollable event {@code u_inv_bad} is added to the specification. It is renamed if the name is not
     * unique. A plant is added where the event is possible if one of the state invariants doesn't hold. A requirement
     * is added that disables the event. Since blocking an uncontrollable plant event in a requirement is forbidden,
     * synthesis will prevent the blockage, by disabling controllable events, thereby ensuring that the state invariants
     * hold.
     * </p>
     *
     * @param spec The CIF specification. Is modified in-place.
     */
    private static void modifyInvariants(Specification spec) {
        // Collect state invariants, and skip if none found.
        List<Invariant> stateInvs = list();
        collectStateInvs(spec, stateInvs);
        if (stateInvs.isEmpty()) {
            return;
        }

        // Get state invariant predicates.
        List<Expression> invPreds = listc(stateInvs.size());
        for (Invariant inv: stateInvs) {
            invPreds.add(inv.getPredicate());
        }

        // Get name for 'u_inv_bad' event.
        Set<String> names = getSymbolNamesForScope(spec, null);
        String evtName = "u_inv_bad";
        if (names.contains(evtName)) {
            evtName = getUniqueName(evtName, names, EMPTY_SET);
        }
        names.add(evtName);

        // Get name for 'inv_req' automaton.
        String reqName = "inv_req";
        if (names.contains(reqName)) {
            reqName = getUniqueName(reqName, names, EMPTY_SET);
        }
        names.add(reqName);

        // Get name for 'inv_plant' automaton.
        String plantName = "inv_plant";
        if (names.contains(plantName)) {
            plantName = getUniqueName(plantName, names, EMPTY_SET);
        }
        names.add(plantName);

        // Add 'u_inv_bad' event to the specification.
        Event event = newEvent();
        event.setName(evtName);
        event.setControllable(false);
        spec.getDeclarations().add(event);

        // Add requirement automaton to the specification.
        Automaton reqAut = newAutomaton();
        spec.getComponents().add(reqAut);
        reqAut.setName(reqName);
        reqAut.setKind(SupKind.REQUIREMENT);

        Location reqLoc = newLocation();
        reqAut.getLocations().add(reqLoc);
        reqLoc.getInitials().add(makeTrue());
        reqLoc.getMarkeds().add(makeTrue());

        Edge reqEdge = newEdge();
        reqLoc.getEdges().add(reqEdge);
        reqEdge.getGuards().add(makeFalse());

        EdgeEvent reqEdgeEvent = newEdgeEvent();
        reqEdge.getEvents().add(reqEdgeEvent);

        EventExpression reqEventRef = newEventExpression();
        reqEdgeEvent.setEvent(reqEventRef);

        reqEventRef.setEvent(event);
        reqEventRef.setType(newBoolType());

        // Add plant automaton to the specification.
        Automaton plantAut = newAutomaton();
        spec.getComponents().add(plantAut);
        plantAut.setName(plantName);
        plantAut.setKind(SupKind.PLANT);

        Location plantLoc = newLocation();
        plantAut.getLocations().add(plantLoc);
        plantLoc.getInitials().add(makeTrue());
        plantLoc.getMarkeds().add(makeTrue());

        Edge plantEdge = newEdge();
        plantLoc.getEdges().add(plantEdge);

        UnaryExpression notGuard = newUnaryExpression();
        plantEdge.getGuards().add(notGuard);
        notGuard.setOperator(UnaryOperator.INVERSE);
        notGuard.setType(newBoolType());
        notGuard.setChild(CifValueUtils.createConjunction(invPreds));

        EdgeEvent plantEdgeEvent = newEdgeEvent();
        plantEdge.getEvents().add(plantEdgeEvent);

        EventExpression plantEventRef = newEventExpression();
        plantEdgeEvent.setEvent(plantEventRef);

        plantEventRef.setEvent(event);
        plantEventRef.setType(newBoolType());
    }

    /**
     * Modifies the specification so that subsequent conversions will generate a Supremica module with proper jumping
     * behavior of variables.
     *
     * @param comp The CIF component to modify, recursively. Is modified in-place.
     * @see #modifyJumping(Automaton)
     */
    private static void modifyJumping(ComplexComponent comp) {
        // Recurse into child components.
        if (comp instanceof Group) {
            for (Component child: ((Group)comp).getComponents()) {
                modifyJumping((ComplexComponent)child);
            }
            return;
        }

        // Automaton.
        modifyJumping((Automaton)comp);
    }

    /**
     * Modifies the automaton so that subsequent conversions will generate a Supremica automaton with proper jumping
     * behavior of variables.
     *
     * <p>
     * Consider:
     *
     * <pre>
     * plant automaton aut:
     *   disc int[0..3] x = 1;
     *   event evt;
     *
     *   location loc1:
     *     initial;
     *     edge evt do x := x + 1 goto loc2;
     *
     *   location loc2:
     *     edge evt goto loc1;
     * end
     * </pre>
     *
     * When translated to Supremica as is, Supremica will add self loops to all locations of the automaton for variable
     * 'aut.x'. When those self loops synchronize with the edge from 'loc1' to 'loc2', the assignment ensures that
     * 'aut.x' can only get proper new values. If however the self loops synchronize with the edge from 'loc2' to
     * 'loc1', neither those self loops, nor the edge from 'aut' restricts the new value of variable 'aut.x', and
     * 'aut.x' can jump freely. To prevent this, we need to add 'x := x' to the edge from 'loc2' to 'loc1'.
     * </p>
     *
     * <p>
     * For all edges where a variable is assigned for an edge with a certain event, this method ensures that the
     * variable is assigned on all edges of that same automaton, for that same event, by adding dummy assignments as
     * needed.
     * </p>
     *
     * @param aut The CIF automaton to modify. Is modified in-place.
     */
    private static void modifyJumping(Automaton aut) {
        // Make mapping from events to variables that are assigned on edges
        // with those events.
        Map<Event, Set<DiscVariable>> mapping = map();
        for (Location loc: aut.getLocations()) {
            for (Edge edge: loc.getEdges()) {
                // Get variables.
                Set<Declaration> vars = set();
                collectAddrVars(edge.getUpdates(), vars);

                // Update mapping.
                for (EdgeEvent edgeEvent: edge.getEvents()) {
                    Expression eventRef = edgeEvent.getEvent();
                    Event event = ((EventExpression)eventRef).getEvent();
                    Set<DiscVariable> entry = mapping.get(event);
                    if (entry == null) {
                        entry = set();
                        mapping.put(event, entry);
                    }
                    for (Declaration var: vars) {
                        entry.add((DiscVariable)var);
                    }
                }
            }
        }

        // Add dummy assignments where needed.
        for (Location loc: aut.getLocations()) {
            for (Edge edge: loc.getEdges()) {
                // Get variables that should be assigned, for all the events on
                // this edge.
                Set<DiscVariable> todoVars = set();
                for (EdgeEvent edgeEvent: edge.getEvents()) {
                    Expression eventRef = edgeEvent.getEvent();
                    Event event = ((EventExpression)eventRef).getEvent();
                    Set<DiscVariable> entry = mapping.get(event);
                    todoVars.addAll(entry);
                }

                // Get variables not yet assigned, by removing assigned
                // variables from the set.
                Set<Declaration> asgnVars = set();
                collectAddrVars(edge.getUpdates(), asgnVars);
                for (Declaration var: asgnVars) {
                    todoVars.remove(var);
                }

                // For the remaining (unassigned variables), add a dummy
                // assignment.
                for (DiscVariable var: todoVars) {
                    DiscVariableExpression varRef = newDiscVariableExpression();
                    varRef.setVariable(var);
                    varRef.setType(deepclone(var.getType()));

                    Assignment asgn = newAssignment();
                    asgn.setAddressable(varRef);
                    asgn.setValue(deepclone(varRef));

                    edge.getUpdates().add(asgn);
                }
            }
        }
    }

    /**
     * Returns the name to use in Supremica for the given CIF object. The given CIF object must be a named object.
     *
     * @param obj The CIF object for which to return the name.
     * @return The name to use in Supremica.
     * @see CifTextUtils#getAbsName
     */
    private static String getSupremicaName(PositionObject obj) {
        // Special case for enumeration literals.
        if (obj instanceof EnumLiteral) {
            // Enumeration literals must be unique in Supremica (may not clash
            // with variable names, etc).
            //
            // We can't use absolute names for enumeration literals, as two
            // enumerations with compatible literals (same names, in same
            // order) can be declared in different components. We use a ':lit:'
            // prefix. This can't conflict with other names that we generated,
            // as absolute CIF names can't start with a '.', and thus their
            // Supremica names can't start with ':'.
            //
            // Note that literals with the same name, from non-compatible
            // enumerations get the same name as well. However, for the subset
            // of valid CIF specifications, this should not be a problem.
            EnumLiteral literal = (EnumLiteral)obj;
            return ":lit:" + literal.getName();
        }

        // General case. Supremica seems to be case sensitive, like CIF. We
        // use absolute names of CIF objects, to avoid duplicate names. CIF
        // uses '.' between the parts of the absolute names, which Supremica
        // doesn't allow in names. However, Supremica allows ':', which can
        // not occur in absolute CIF names.
        return getAbsName(obj, false).replace('.', ':');
    }
}
