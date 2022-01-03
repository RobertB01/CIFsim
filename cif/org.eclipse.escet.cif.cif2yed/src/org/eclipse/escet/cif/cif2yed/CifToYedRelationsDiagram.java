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

package org.eclipse.escet.cif.cif2yed;

import static org.eclipse.escet.cif.cif2yed.CifToYedColors.COMP_BG_CLOSED_COLOR;
import static org.eclipse.escet.cif.cif2yed.CifToYedColors.COMP_BG_OPENED_COLOR;
import static org.eclipse.escet.cif.cif2yed.CifToYedColors.COMP_HEADER_COLOR;
import static org.eclipse.escet.cif.cif2yed.CifToYedColors.DATA_DECL_COLOR;
import static org.eclipse.escet.cif.cif2yed.CifToYedColors.DATA_PARAM_COLOR;
import static org.eclipse.escet.cif.cif2yed.CifToYedColors.DATA_REF_COLOR;
import static org.eclipse.escet.cif.cif2yed.CifToYedColors.DEF_HEADER_COLOR;
import static org.eclipse.escet.cif.cif2yed.CifToYedColors.EVENT_DECL_COLOR;
import static org.eclipse.escet.cif.cif2yed.CifToYedColors.EVENT_PARAM_COLOR;
import static org.eclipse.escet.cif.cif2yed.CifToYedColors.EVENT_REF_COLOR;
import static org.eclipse.escet.cif.cif2yed.CifToYedColors.INST_HEADER_COLOR;
import static org.eclipse.escet.cif.cif2yed.CifToYedColors.WRAP_BOX_HEADER_COLOR;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.warn;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Pair.pair;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.str;

import java.awt.geom.Rectangle2D;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.escet.cif.cif2yed.options.RelationKind;
import org.eclipse.escet.cif.cif2yed.options.RelationKindsOption;
import org.eclipse.escet.cif.common.CifEquationUtils;
import org.eclipse.escet.cif.common.CifScopeUtils;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.metamodel.cif.AlgParameter;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.ComponentDef;
import org.eclipse.escet.cif.metamodel.cif.ComponentInst;
import org.eclipse.escet.cif.metamodel.cif.ComponentParameter;
import org.eclipse.escet.cif.metamodel.cif.Equation;
import org.eclipse.escet.cif.metamodel.cif.EventParameter;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.LocationParameter;
import org.eclipse.escet.cif.metamodel.cif.Parameter;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.SupKind;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeReceive;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeSend;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.cif.metamodel.cif.expressions.AlgVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CompInstWrapExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CompParamWrapExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ContVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.InputVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.LocationExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TauExpression;
import org.eclipse.escet.cif.metamodel.cif.functions.Function;
import org.eclipse.escet.cif.metamodel.java.CifWalker;
import org.eclipse.escet.cif.prettyprinter.CifPrettyPrinter;
import org.eclipse.escet.common.app.framework.exceptions.UnsupportedException;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Pair;
import org.eclipse.escet.common.java.Strings;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/** CIF to yEd 'relations' diagram transformation. */
public class CifToYedRelationsDiagram extends CifToYedDiagram {
    /** The kinds of relations to include in the diagram. */
    private EnumSet<RelationKind> relations;

    /** The 'ids' of the nodes created for component instantiations. */
    private Set<String> instIds;

    /** The 'ids' of the nodes created for components, component definitions, and component instantiations. */
    private Set<String> compIds;

    /** The 'ids' of 'boxes' used to separate components from component definitions. */
    private Set<String> boxIds;

    /** Pairs of 'ids' of the source and target nodes for which parameter reference edges have been created. */
    private Set<Pair<String, String>> paramRefIds;

    /** Pairs of parent elements and 'ids' of the nodes created for data references in those parents. */
    private Set<Pair<Element, String>> dataRefIds;

    /**
     * Mapping from pairs of 'ids' of source and target nodes of data relation edges to the 'edge' elements created for
     * them.
     */
    private Map<Pair<String, String>, Element> dataEdgeElems;

    /** The next free unique number for an event reference id. */
    private long nextFreeEvtRefId;

    /** The next free unique number for a data reference id. */
    private long nextFreeDataRefId;

    /**
     * Mapping from component definitions to the unique ids of the 'node' elements created for the component definitions
     * or actual components resulting from the instantiations of those component definitions, for all ancestor component
     * definitions and instantiations.
     *
     * <p>
     * Note that we can't have component definition/instantiation cycles, and therefore we can't have a definition
     * twice. That is, all definitions are unique, and can't instantiate a definition directly or indirectly in itself.
     * As such, component definitions can be used as unique keys.
     * </p>
     */
    private Map<ComponentDef, String> compDefMap;

    @Override
    protected void addSpec(Specification spec, Element root) {
        // Precondition checking.
        preCheck(spec);

        // Initialize.
        relations = RelationKindsOption.getKinds();
        rootGraph = null;
        instIds = set();
        compIds = set();
        boxIds = set();
        paramRefIds = set();
        dataRefIds = set();
        dataEdgeElems = map();
        nextFreeEvtRefId = 0;
        nextFreeDataRefId = 0;
        compDefMap = map();

        // If we don't include any kind of relations, the diagram is
        // practically useless.
        if (relations.isEmpty()) {
            warn("Relation diagram will not include any relations, as no relations have been specified.");
        }

        // Transform.
        addComponent(spec, false, false, false, root, "cif");
        removeInstInternalEdges(root);
        Set<String> edgeSrcTgtIds = set();
        collectEdgeSrcTgtIds(root, edgeSrcTgtIds);
        removeInstInternalNodes(root, edgeSrcTgtIds);
        removeEmptyComps(root);
    }

    /**
     * Check preconditions for the given component, recursively.
     *
     * @param comp The component to check, recursively.
     * @throws UnsupportedException If an unsupported feature is found.
     */
    private void preCheck(ComplexComponent comp) {
        // Skip automata, as they don't contain component definitions.
        if (comp instanceof Automaton) {
            return;
        }
        Group group = (Group)comp;

        // Check component definitions.
        for (ComponentDef cdef: group.getDefinitions()) {
            preCheck(cdef);
        }

        // Check recursively. Skip component instantiations as we already check
        // the component definitions and their bodies.
        for (Component child: group.getComponents()) {
            if (child instanceof ComponentInst) {
                continue;
            }
            preCheck((ComplexComponent)child);
        }
    }

    /**
     * Check preconditions for the given component definition, recursively.
     *
     * @param cdef The component definition to check, recursively.
     * @throws UnsupportedException If an unsupported feature is found.
     */
    private void preCheck(ComponentDef cdef) {
        // Check parameters.
        for (Parameter param: cdef.getParameters()) {
            if (param instanceof ComponentParameter) {
                String msg = fmt("Component parameter \"%s\" is not supported.", CifTextUtils.getAbsName(param));
                throw new UnsupportedException(msg);
            }
        }

        // Check body.
        preCheck(cdef.getBody());
    }

    /**
     * Add a component, or body of a component definition.
     *
     * @param comp The component or body of a component definition. If it is a body of a component definition, exactly
     *     one of the {@code *Body} parameters must be {@code true} and the others must be {@code false}.
     * @param notBody Whether the component definition body is to be added as the a non-instantiated component
     *     definition itself instead of its body, or {@code false} if the given component is not a body of a component
     *     definition at all.
     * @param defBody Whether the component definition body is to be added as the body of a non-instantiated component
     *     definition, or {@code false} if the given component is not a body of a component definition at all.
     * @param instBody Whether the component definition body is to be added as the body of an instantiated component
     *     definition, or {@code false} if the given component is not a body of a component definition at all.
     * @param parent The parent XML element to which to add new elements.
     * @param id The 'id' of the XML element to add for the component.
     */
    private void addComponent(Component comp, boolean notBody, boolean defBody, boolean instBody, Element parent,
            String id)
    {
        // Parameter checking.
        boolean isBody = comp.eContainer() instanceof ComponentDef;
        int cntBody = 0;
        if (notBody) {
            cntBody++;
        }
        if (defBody) {
            cntBody++;
        }
        if (instBody) {
            cntBody++;
        }
        Assert.check(cntBody == (isBody ? 1 : 0));

        // Is this a wrapper component?
        boolean asBody = defBody || instBody;
        boolean wrapper = (isBody && !asBody) || (!isBody && comp instanceof ComponentInst);

        // Add 'node' element.
        if (!asBody && !(comp instanceof Specification)) {
            // Add GraphML 'node'.
            Element nodeElem = doc.createElement("node");
            parent.appendChild(nodeElem);
            nodeElem.setAttribute("id", id);
            parent = nodeElem;

            // Add id.
            compIds.add(id);

            // Figure out whether the component (body) is an automaton or a
            // group.
            ComponentDef cdef = null;
            if (comp instanceof ComponentInst) {
                ComponentInst inst = (ComponentInst)comp;
                cdef = CifTypeUtils.getCompDefFromCompInst(inst);
            } else if (isBody) {
                cdef = (ComponentDef)comp.eContainer();
            }

            Component body = comp;
            if (cdef != null) {
                body = cdef.getBody();
            }
            boolean isAut = body instanceof Automaton;
            boolean isGroup = body instanceof Group;

            // Get kind text.
            String kindText;
            if (isAut) {
                kindText = "automaton";
                SupKind kind = ((Automaton)body).getKind();
                if (kind != SupKind.NONE) {
                    kindText = CifTextUtils.kindToStr(kind) + " " + kindText;
                }
            } else if (isGroup) {
                kindText = "group";
            } else {
                throw new RuntimeException("Unknown body: " + body);
            }

            // Get title and background color.
            String name = comp.getName();
            String title;
            CifToYedColors bgColor;
            if (cdef == null) {
                // Automaton or group (concrete component).
                title = kindText + " " + name;
                bgColor = COMP_HEADER_COLOR;
            } else if (comp instanceof ComponentInst) {
                // Automaton or group instantiation. Arguments are left out.
                ComponentInst inst = (ComponentInst)comp;
                CifPrettyPrinter pprinter = new CifPrettyPrinter(null);
                String cdefRefTxt = pprinter.pprint(inst.getDefinition());
                title = fmt("%s: %s()", name, cdefRefTxt);
                bgColor = INST_HEADER_COLOR;
            } else {
                // Automaton or group definition. Parameters are left out.
                title = fmt("%s def %s()", kindText, name);
                bgColor = DEF_HEADER_COLOR;
            }

            // Add yEd styles.
            nodeElem.setAttribute("yfiles.foldertype", "folder");

            Element dnDataElem = doc.createElement("data");
            nodeElem.appendChild(dnDataElem);
            dnDataElem.setAttribute("key", "dn");
            dnDataElem.setTextContent(title);

            Element ngDataElem = doc.createElement("data");
            nodeElem.appendChild(ngDataElem);
            ngDataElem.setAttribute("key", "ng");

            Element pabnElem = doc.createElement("y:ProxyAutoBoundsNode");
            ngDataElem.appendChild(pabnElem);

            Element realElem = doc.createElement("y:Realizers");
            pabnElem.appendChild(realElem);
            realElem.setAttribute("active", "1");

            for (boolean closed: list(false, true)) {
                Element gnElem = doc.createElement("y:GroupNode");
                realElem.appendChild(gnElem);

                if (closed) {
                    Element geoElem = doc.createElement("y:Geometry");
                    gnElem.appendChild(geoElem);
                    Rectangle2D size = guessTextSize(title, 5);
                    double width = size.getWidth() + 40;
                    double height = size.getHeight();
                    geoElem.setAttribute("width", str(width));
                    geoElem.setAttribute("height", str(height));
                }

                String label = title;
                label = Strings.spaces(5) + label; // Avoid label behind '-' icon.
                label = highlight(label);

                Element nlElem = doc.createElement("y:NodeLabel");
                gnElem.appendChild(nlElem);
                nlElem.setAttribute("alignment", "left");
                nlElem.setAttribute("autoSizePolicy", "node_width");
                nlElem.setAttribute("backgroundColor", bgColor.color);
                nlElem.setAttribute("modelName", "internal");
                nlElem.setAttribute("modelPosition", "t");
                nlElem.setAttribute("fontStyle", isGroup ? "italic" : "plain");
                nlElem.setTextContent(label);

                Element fillElem = doc.createElement("y:Fill");
                gnElem.appendChild(fillElem);
                CifToYedColors fc = closed ? COMP_BG_CLOSED_COLOR : COMP_BG_OPENED_COLOR;
                fillElem.setAttribute("color", fc.color);

                Element shapeElem = doc.createElement("y:Shape");
                gnElem.appendChild(shapeElem);
                shapeElem.setAttribute("type", "rectangle");

                Element stateElem = doc.createElement("y:State");
                gnElem.appendChild(stateElem);
                stateElem.setAttribute("closed", str(closed));
            }
        }

        // Add 'graph' element.
        if (!asBody) {
            Element graphElem = doc.createElement("graph");
            parent.appendChild(graphElem);
            graphElem.setAttribute("id", id + ":");
            graphElem.setAttribute("edgedefault", "undirected");
            parent = graphElem;
        }

        // Store root 'graph' element.
        if (comp instanceof Specification) {
            rootGraph = parent;
        }

        // Add components and component definitions. They are surrounded by
        // boxes, to separate them.
        if (!wrapper) {
            // Add boxes for content.
            final int BOX_DEFS = 0;
            final int BOX_COMPS = 1;
            String[] boxNames = {"Component definitions", "Components"};
            Element[] boxes = new Element[boxNames.length];
            for (int i = 0; i < boxes.length; i++) {
                // Add GraphML 'node'.
                Element nodeElem = doc.createElement("node");
                parent.appendChild(nodeElem);
                Assert.check(!id.endsWith(":"));

                // Add 'graph' element.
                Element graphElem = doc.createElement("graph");
                nodeElem.appendChild(graphElem);
                graphElem.setAttribute("edgedefault", "undirected");
                boxes[i] = graphElem;

                // Set dummy 'ids', to same as parent of the new box. This
                // ensures that children get the proper names, ignoring these
                // boxes.
                nodeElem.setAttribute("id", id);
                graphElem.setAttribute("id", id + ":");

                // Get title.
                String title = boxNames[i];

                // Add yEd styles.
                nodeElem.setAttribute("yfiles.foldertype", "folder");

                Element dnDataElem = doc.createElement("data");
                nodeElem.appendChild(dnDataElem);
                dnDataElem.setAttribute("key", "dn");
                dnDataElem.setTextContent(title);

                Element ngDataElem = doc.createElement("data");
                nodeElem.appendChild(ngDataElem);
                ngDataElem.setAttribute("key", "ng");

                Element pabnElem = doc.createElement("y:ProxyAutoBoundsNode");
                ngDataElem.appendChild(pabnElem);

                Element realElem = doc.createElement("y:Realizers");
                pabnElem.appendChild(realElem);
                realElem.setAttribute("active", "1");

                for (boolean closed: list(false, true)) {
                    Element gnElem = doc.createElement("y:GroupNode");
                    realElem.appendChild(gnElem);

                    if (closed) {
                        Element geoElem = doc.createElement("y:Geometry");
                        gnElem.appendChild(geoElem);
                        Rectangle2D size = guessTextSize(title, 5);
                        double width = size.getWidth() + 40;
                        double height = size.getHeight();
                        geoElem.setAttribute("width", str(width));
                        geoElem.setAttribute("height", str(height));
                    }

                    String label = title;
                    label = Strings.spaces(5) + label; // Avoid label behind '-' icon.

                    Element nlElem = doc.createElement("y:NodeLabel");
                    gnElem.appendChild(nlElem);
                    nlElem.setAttribute("alignment", "left");
                    nlElem.setAttribute("autoSizePolicy", "node_width");
                    nlElem.setAttribute("backgroundColor", WRAP_BOX_HEADER_COLOR.color);
                    nlElem.setAttribute("modelName", "internal");
                    nlElem.setAttribute("modelPosition", "t");
                    nlElem.setTextContent(label);

                    Element fillElem = doc.createElement("y:Fill");
                    gnElem.appendChild(fillElem);
                    CifToYedColors fc = closed ? COMP_BG_CLOSED_COLOR : COMP_BG_OPENED_COLOR;
                    fillElem.setAttribute("color", fc.color);

                    Element shapeElem = doc.createElement("y:Shape");
                    gnElem.appendChild(shapeElem);
                    shapeElem.setAttribute("type", "rectangle");

                    Element stateElem = doc.createElement("y:State");
                    gnElem.appendChild(stateElem);
                    stateElem.setAttribute("closed", str(closed));
                }
            }

            // Add child component definitions.
            if (!instBody && comp instanceof Group) {
                for (ComponentDef child: ((Group)comp).getDefinitions()) {
                    String childId = id + ":" + child.getBody().getName();
                    addComponent(child.getBody(), true, false, false, boxes[BOX_DEFS], childId);
                }
            }

            // Add child components.
            if (comp instanceof Group) {
                for (Component child: ((Group)comp).getComponents()) {
                    String childId = id + ":" + child.getName();
                    addComponent(child, false, false, false, boxes[BOX_COMPS], childId);
                }
            }

            // Add declarations.
            if (!wrapper && comp instanceof ComplexComponent) {
                for (Declaration decl: ((ComplexComponent)comp).getDeclarations()) {
                    if ((relations.contains(RelationKind.EVENT) && decl instanceof Event)
                            || (relations.contains(RelationKind.DATA)
                                    && (decl instanceof AlgVariable || decl instanceof ContVariable
                                            || decl instanceof DiscVariable || decl instanceof InputVariable)))
                    {
                        String declId = id + ":" + decl.getName();
                        addDecl(decl, boxes[BOX_COMPS], declId);
                    }
                }
            }

            // Add locations.
            if (relations.contains(RelationKind.DATA) && !wrapper && comp instanceof Automaton) {
                Automaton aut = (Automaton)comp;
                for (Location loc: aut.getLocations()) {
                    // Nameless locations are not added, as they can't be
                    // referenced. Since there is only one location then, we
                    // can merge it with the automaton scope.
                    if (loc.getName() == null) {
                        continue;
                    }

                    // Add location.
                    String declId = id + ":" + loc.getName();
                    addDecl(loc, boxes[BOX_COMPS], declId);
                }
            }

            // Add event references for the alphabet.
            if (relations.contains(RelationKind.EVENT) && !wrapper && comp instanceof Automaton) {
                // Get alphabets.
                Automaton aut = (Automaton)comp;
                List<Expression> syncs = getSyncAlphabet(aut);
                List<Expression> sends = getSendAlphabet(aut);
                List<Expression> recvs = getRecvAlphabet(aut);

                // Get unique resolved events, per alphabet. Skip 'tau' event.
                Set<String> syncIds = getEventRefIds(syncs);
                Set<String> sendIds = getEventRefIds(sends);
                Set<String> recvIds = getEventRefIds(recvs);

                // Add event references.
                for (String syncId: syncIds) {
                    addEventRef(syncId, boxes[BOX_COMPS], "");
                }
                for (String sendId: sendIds) {
                    addEventRef(sendId, boxes[BOX_COMPS], "!");
                }
                for (String recvId: recvIds) {
                    addEventRef(recvId, boxes[BOX_COMPS], "?");
                }
            }

            // Add data references for references in components.
            if (relations.contains(RelationKind.DATA) && !wrapper) {
                // Collect all relevant references.
                Assert.check(comp instanceof ComplexComponent);
                CompRefExprCollector collector = new CompRefExprCollector();
                collector.collectRefExprs((ComplexComponent)comp);

                // Add all collected references.
                for (Expression ref: collector.refs) {
                    addDeclRef(getObjRefId(ref), boxes[BOX_COMPS]);
                }
            }

            // Set proper unique 'ids' for the box nodes, now that content has
            // been added.
            for (int i = 0; i < boxes.length; i++) {
                Element graphElem = boxes[i];
                Element nodeElem = (Element)graphElem.getParentNode();

                String boxId = id + "::box" + str(i);
                boxIds.add(boxId);

                nodeElem.setAttribute("id", boxId);
                graphElem.setAttribute("id", boxId + ":");
            }
        }

        // Add component instantiation id.
        if (comp instanceof ComponentInst) {
            instIds.add(id);
        }

        // Add body of an instantiated for uninstantiated component definition.
        if (wrapper) {
            // Get component definition/instantiation.
            ComponentDef cdef;
            ComponentInst inst;
            if (comp instanceof ComponentInst) {
                // Get instantiated definition.
                inst = (ComponentInst)comp;
                cdef = CifTypeUtils.getCompDefFromCompInst(inst);
            } else {
                // Get parent component definition of this body.
                inst = null;
                cdef = (ComponentDef)comp.eContainer();
            }
            boolean isInst = (inst != null);

            // Add component definition to the mapping. Note that we can't have
            // a component definition twice, as we would then have a
            // component definition/instantiation cycle. Similar argument
            // applies to the parameters.
            String prev = compDefMap.put(cdef, id);
            Assert.check(prev == null);

            // Add event parameters.
            for (int i = 0; i < cdef.getParameters().size(); i++) {
                // Get formal parameter and actual argument.
                Parameter param = cdef.getParameters().get(i);
                Expression arg = null;
                if (inst != null) {
                    arg = inst.getParameters().get(i);
                }

                // Get parameter node id. Determine whether to collect
                // reference expressions from the argument.
                String paramId;
                boolean collect;
                if (relations.contains(RelationKind.DATA) && param instanceof AlgParameter) {
                    AlgParameter algParam = (AlgParameter)param;
                    AlgVariable var = algParam.getVariable();
                    paramId = id + ":" + var.getName();
                    collect = true;
                } else if (relations.contains(RelationKind.EVENT) && param instanceof EventParameter) {
                    EventParameter eventParam = (EventParameter)param;
                    Event event = eventParam.getEvent();
                    paramId = id + ":" + event.getName();
                    collect = false;
                } else if (relations.contains(RelationKind.DATA) && param instanceof LocationParameter) {
                    LocationParameter locParam = (LocationParameter)param;
                    Location loc = locParam.getLocation();
                    paramId = id + ":" + loc.getName();
                    collect = false;
                } else {
                    paramId = null;
                    collect = false;
                }

                // Add parameter, if applicable.
                if (paramId != null) {
                    // Add parameter.
                    addParam(param, parent, paramId);

                    // Get relations between parameter and argument.
                    if (arg != null) {
                        if (collect) {
                            // Argument is a value expression, with zero or
                            // more references.
                            ArgRefExprCollector collector = new ArgRefExprCollector();
                            collector.collectRefExprs(arg);
                            for (Expression argRef: collector.refs) {
                                addParamRelation(param, paramId, argRef);
                            }
                        } else {
                            // Entire argument is a reference.
                            addParamRelation(param, paramId, arg);
                        }
                    }
                }
            }

            // Add body.
            addComponent(cdef.getBody(), false, !isInst, isInst, parent, id);

            // Clean up.
            compDefMap.remove(cdef);
        }
    }

    /**
     * Add a declaration.
     *
     * @param decl The declaration. May be an {@link Event}, {@link AlgVariable}, {@link ContVariable},
     *     {@link DiscVariable}, {@link InputVariable}, or a {@link Location}.
     * @param parent The parent XML element to which to add new elements.
     * @param id The 'id' of the XML element to add for the declaration.
     */
    private void addDecl(PositionObject decl, Element parent, String id) {
        // Skip nameless locations.
        if (decl instanceof Location) {
            if (((Location)decl).getName() == null) {
                return;
            }
        }

        // Add GraphML 'node'.
        Element nodeElem = doc.createElement("node");
        parent.appendChild(nodeElem);
        nodeElem.setAttribute("id", id);

        // Get label.
        String prefix;
        if (decl instanceof Event) {
            Assert.check(relations.contains(RelationKind.EVENT));
            prefix = "event";
        } else if (decl instanceof AlgVariable) {
            Assert.check(relations.contains(RelationKind.DATA));
            prefix = "alg";
        } else if (decl instanceof ContVariable) {
            Assert.check(relations.contains(RelationKind.DATA));
            prefix = "cont";
        } else if (decl instanceof DiscVariable) {
            Assert.check(relations.contains(RelationKind.DATA));
            prefix = "disc";
        } else if (decl instanceof InputVariable) {
            Assert.check(relations.contains(RelationKind.DATA));
            prefix = "input";
        } else if (decl instanceof Location) {
            Assert.check(relations.contains(RelationKind.DATA));
            prefix = "location";
        } else {
            throw new RuntimeException("Unexpected decl: " + decl);
        }

        String label = prefix + " " + CifTextUtils.getName(decl);
        if (decl instanceof Event) {
            if (((Event)decl).getType() != null) {
                label += " !?~";
            }
        }

        // Add yEd styles.
        Element dnDataElem = doc.createElement("data");
        nodeElem.appendChild(dnDataElem);
        dnDataElem.setAttribute("key", "dn");
        dnDataElem.setTextContent(label);

        Element ngDataElem = doc.createElement("data");
        nodeElem.appendChild(ngDataElem);
        ngDataElem.setAttribute("key", "ng");

        Element snElem = doc.createElement("y:ShapeNode");
        ngDataElem.appendChild(snElem);

        Element geoElem = doc.createElement("y:Geometry");
        snElem.appendChild(geoElem);
        geoElem.setAttribute("width", str(guessTextWidth(label, 5)));

        label = highlight(label);

        Element nlElem = doc.createElement("y:NodeLabel");
        snElem.appendChild(nlElem);
        nlElem.setAttribute("alignment", "center");
        nlElem.setTextContent(label);

        Element bsElem = doc.createElement("y:BorderStyle");
        snElem.appendChild(bsElem);
        bsElem.setAttribute("width", "2.0");

        Element fillElem = doc.createElement("y:Fill");
        snElem.appendChild(fillElem);
        if (decl instanceof Event) {
            fillElem.setAttribute("color", EVENT_DECL_COLOR.color);
        } else {
            fillElem.setAttribute("color", DATA_DECL_COLOR.color);
        }

        Element shapeElem = doc.createElement("y:Shape");
        snElem.appendChild(shapeElem);
        shapeElem.setAttribute("type", "rectangle");

        // Add relations.
        if (decl instanceof Event) {
            // Nothing to add.
        } else if (decl instanceof AlgVariable || decl instanceof ContVariable || decl instanceof DiscVariable
                || decl instanceof InputVariable)
        {
            DeclRefExprCollector collector = new DeclRefExprCollector();
            collector.collectRefExprs((Declaration)decl);
            for (Expression ref: collector.refs) {
                addDeclRef(getObjRefId(ref), id);
            }
        } else if (decl instanceof Location) {
            DeclRefExprCollector collector = new DeclRefExprCollector();
            collector.collectRefExprs((Location)decl);
            for (Expression ref: collector.refs) {
                addDeclRef(getObjRefId(ref), id);
            }
        } else {
            throw new RuntimeException("Unexpected decl: " + decl);
        }
    }

    /**
     * Add a parameter.
     *
     * @param param The parameter. May be an {@link EventParameter}, {@link LocationParameter}, or an
     *     {@link AlgParameter}.
     * @param parent The parent XML element to which to add new elements.
     * @param id The 'id' of the XML element to add for the parameter.
     */
    private void addParam(Parameter param, Element parent, String id) {
        // Add GraphML 'node'.
        Element nodeElem = doc.createElement("node");
        parent.appendChild(nodeElem);
        nodeElem.setAttribute("id", id);

        // Get label.
        String prefix;
        String name;
        if (param instanceof EventParameter) {
            Assert.check(relations.contains(RelationKind.EVENT));
            Event event = ((EventParameter)param).getEvent();
            prefix = "event";
            name = event.getName();
        } else if (param instanceof LocationParameter) {
            Assert.check(relations.contains(RelationKind.DATA));
            Location loc = ((LocationParameter)param).getLocation();
            prefix = "location";
            name = loc.getName();
        } else if (param instanceof AlgParameter) {
            Assert.check(relations.contains(RelationKind.DATA));
            AlgVariable var = ((AlgParameter)param).getVariable();
            prefix = "alg";
            name = var.getName();
        } else {
            throw new RuntimeException("Unexpected param: " + param);
        }

        String label = prefix + " " + name;
        if (param instanceof EventParameter) {
            EventParameter eventParam = (EventParameter)param;
            if (eventParam.getEvent().getType() != null) {
                label += " ";
                if (eventParam.isSendFlag()) {
                    label += "!";
                }
                if (eventParam.isRecvFlag()) {
                    label += "?";
                }
                if (eventParam.isSyncFlag()) {
                    label += "~";
                }
                if (!eventParam.isSendFlag() && !eventParam.isRecvFlag() && !eventParam.isSyncFlag()) {
                    label += "!?~";
                }
            }
        }

        // Add yEd styles.
        Element dnDataElem = doc.createElement("data");
        nodeElem.appendChild(dnDataElem);
        dnDataElem.setAttribute("key", "dn");
        dnDataElem.setTextContent(label);

        Element nodeNgDataElem = doc.createElement("data");
        nodeElem.appendChild(nodeNgDataElem);
        nodeNgDataElem.setAttribute("key", "ng");

        Element nodeSnElem = doc.createElement("y:ShapeNode");
        nodeNgDataElem.appendChild(nodeSnElem);

        Element nodeGeoElem = doc.createElement("y:Geometry");
        nodeSnElem.appendChild(nodeGeoElem);
        nodeGeoElem.setAttribute("width", str(guessTextWidth(label, 5)));

        label = highlight(label);

        Element nodeNlElem = doc.createElement("y:NodeLabel");
        nodeSnElem.appendChild(nodeNlElem);
        nodeNlElem.setAttribute("alignment", "center");
        nodeNlElem.setTextContent(label);

        Element nodeBsElem = doc.createElement("y:BorderStyle");
        nodeSnElem.appendChild(nodeBsElem);
        nodeBsElem.setAttribute("type", "dashed");

        Element nodeFillElem = doc.createElement("y:Fill");
        nodeSnElem.appendChild(nodeFillElem);
        if (param instanceof EventParameter) {
            nodeFillElem.setAttribute("color", EVENT_PARAM_COLOR.color);
        } else {
            nodeFillElem.setAttribute("color", DATA_PARAM_COLOR.color);
        }

        Element nodeShapeElem = doc.createElement("y:Shape");
        nodeSnElem.appendChild(nodeShapeElem);
        nodeShapeElem.setAttribute("type", "octagon");
    }

    /**
     * Add a relation between a parameter and an actual argument.
     *
     * @param param The parameter. May be an {@link EventParameter}, {@link LocationParameter}, or an
     *     {@link AlgParameter}.
     * @param id The 'id' of the XML element added for the parameter.
     * @param ref The reference expression provided as argument with the instantiation, for the parameter. Must be a
     *     reference expression, for a declaration or parameter for which a node is or will be added.
     */
    private void addParamRelation(Parameter param, String id, Expression ref) {
        // Decide on the direction of the edge.
        boolean directed;
        String sourceId;
        String targetId;
        String sourceArrowStyle;
        String targetArrowStyle;
        if (param instanceof EventParameter) {
            EventParameter eventParam = (EventParameter)param;
            // If it has explicit send or receive flags (only one of them),
            // use that for the direction. Otherwise, it is an undirected
            // edge.
            if (eventParam.isSendFlag() && !eventParam.isRecvFlag()) {
                directed = true;
                sourceId = id;
                targetId = getEventRefId(ref);
                sourceArrowStyle = "none";
                targetArrowStyle = "arrow";
            } else if (!eventParam.isSendFlag() && eventParam.isRecvFlag()) {
                directed = true;
                sourceId = getEventRefId(ref);
                targetId = id;
                sourceArrowStyle = "none";
                targetArrowStyle = "arrow";
            } else {
                // Undirected edge. Use event declaration as source and
                // parameter as target, as that is the 'instantiation
                // direction'. This means the declaration will often be
                // above the parameter in automatic yEd layouts.
                directed = false;
                sourceId = getEventRefId(ref);
                targetId = id;
                sourceArrowStyle = eventParam.isSendFlag() ? "arrow" : "none";
                targetArrowStyle = eventParam.isRecvFlag() ? "arrow" : "none";
            }
        } else {
            // Directed edge from the argument to the parameter, to
            // indicate data flow, from declaration to use.
            directed = true;
            sourceId = getObjRefId(ref);
            targetId = id;
            sourceArrowStyle = "none";
            targetArrowStyle = "arrow";
        }

        Assert.notNull(sourceId);
        Assert.notNull(targetId);

        // Don't add if already exists. Avoids duplicates.
        Pair<String, String> srcTgtIds = pair(sourceId, targetId);
        if (paramRefIds.contains(srcTgtIds)) {
            return;
        }
        paramRefIds.add(srcTgtIds);

        // Add GraphML 'edge'. Note that according to the GraphML
        // specification: "The edges between two nodes in a nested graph
        // have to be declared in a graph, which is an ancestor of both
        // nodes in the hierarchy. [...] A good policy is to place the
        // edges at the least common ancestor of the nodes in the
        // hierarchy, or at the top level."
        Element edgeElem = doc.createElement("edge");
        rootGraph.appendChild(edgeElem);
        edgeElem.setAttribute("source", sourceId);
        edgeElem.setAttribute("target", targetId);
        edgeElem.setAttribute("directed", str(directed));

        // Add yEd styles.
        Element edgeEgDataElem = doc.createElement("data");
        edgeElem.appendChild(edgeEgDataElem);
        edgeEgDataElem.setAttribute("key", "eg");

        Element edgePleElem = doc.createElement("y:PolyLineEdge");
        edgeEgDataElem.appendChild(edgePleElem);

        Element edgeArrElem = doc.createElement("y:Arrows");
        edgePleElem.appendChild(edgeArrElem);
        edgeArrElem.setAttribute("source", sourceArrowStyle);
        edgeArrElem.setAttribute("target", targetArrowStyle);

        Element edgeBsElem = doc.createElement("y:BendStyle");
        edgePleElem.appendChild(edgeBsElem);
        edgeBsElem.setAttribute("smoothed", "true");
    }

    /**
     * Add an event reference node and edge.
     *
     * @param eventRefId The 'id' of the 'node' that the event reference refers to.
     * @param parent The parent XML element to which to add new elements.
     * @param direction The channel direction of the event reference. Is {@code ""} for synchronizing use, {@code "!"}
     *     for send use, or {@code "?"} for receive use.
     */
    private void addEventRef(String eventRefId, Element parent, String direction) {
        Assert.check(relations.contains(RelationKind.EVENT));

        // Add GraphML 'node'.
        Element nodeElem = doc.createElement("node");
        parent.appendChild(nodeElem);
        String nodeId = parent.getAttribute("id");
        Assert.check(nodeId.endsWith(":"));
        nodeId += ":evtref" + str(nextFreeEvtRefId);
        nodeElem.setAttribute("id", nodeId);
        nextFreeEvtRefId++;

        // Add yEd styles.
        int idx = eventRefId.lastIndexOf(':');
        String eventName = eventRefId.substring(idx + 1);
        String label = eventName;
        if (!direction.isEmpty()) {
            label += " " + direction;
        }

        Element dnDataElem = doc.createElement("data");
        nodeElem.appendChild(dnDataElem);
        dnDataElem.setAttribute("key", "dn");
        dnDataElem.setTextContent(label);

        Element nodeNgDataElem = doc.createElement("data");
        nodeElem.appendChild(nodeNgDataElem);
        nodeNgDataElem.setAttribute("key", "ng");

        Element nodeSnElem = doc.createElement("y:ShapeNode");
        nodeNgDataElem.appendChild(nodeSnElem);

        label = direction;
        label = highlight(label);

        Element nodeGeoElem = doc.createElement("y:Geometry");
        nodeSnElem.appendChild(nodeGeoElem);
        nodeGeoElem.setAttribute("width", "30");
        nodeGeoElem.setAttribute("height", "30");

        if (!label.isEmpty()) {
            Element nodeNlElem = doc.createElement("y:NodeLabel");
            nodeSnElem.appendChild(nodeNlElem);
            nodeNlElem.setAttribute("alignment", "center");
            nodeNlElem.setAttribute("fontSize", "20");
            nodeNlElem.setTextContent(label);
        }

        Element nodeFillElem = doc.createElement("y:Fill");
        nodeSnElem.appendChild(nodeFillElem);
        nodeFillElem.setAttribute("color", EVENT_REF_COLOR.color);

        Element nodeShapeElem = doc.createElement("y:Shape");
        nodeSnElem.appendChild(nodeShapeElem);
        nodeShapeElem.setAttribute("type", "ellipse");

        // Get source/target ids and arrow style.
        String sourceId;
        String targetId;
        String sourceArrowStyle;
        String targetArrowStyle;
        if (direction.equals("!")) {
            // Send, so local node is source.
            sourceId = nodeId;
            targetId = eventRefId;
            sourceArrowStyle = "none";
            targetArrowStyle = "arrow";
        } else if (direction.equals("?")) {
            // Receive, so local node is target.
            sourceId = eventRefId;
            targetId = nodeId;
            sourceArrowStyle = "none";
            targetArrowStyle = "arrow";
        } else {
            // Synchronization. Can choose arbitrary source/target as edge is
            // undirected. However, we use the event declaration/parameter as
            // source and the reference as target, as that is the
            // 'declaration/parameter to use direction'. This means the
            // declaration/parameter will often be above the use in automatic
            // yEd layouts.
            Assert.check(direction.isEmpty());
            sourceId = eventRefId;
            targetId = nodeId;
            sourceArrowStyle = "none";
            targetArrowStyle = "none";
        }

        // Add GraphML 'edge'. Note that according to the GraphML
        // specification: "The edges between two nodes in a nested graph have
        // to be declared in a graph, which is an ancestor of both nodes in the
        // hierarchy. [...] A good policy is to place the edges at the least
        // common ancestor of the nodes in the hierarchy, or at the top level."
        Element edgeElem = doc.createElement("edge");
        rootGraph.appendChild(edgeElem);
        edgeElem.setAttribute("source", sourceId);
        edgeElem.setAttribute("target", targetId);
        edgeElem.setAttribute("directed", str(!direction.isEmpty()));

        // Add yEd styles.
        Element edgeEgDataElem = doc.createElement("data");
        edgeElem.appendChild(edgeEgDataElem);
        edgeEgDataElem.setAttribute("key", "eg");

        Element edgePleElem = doc.createElement("y:PolyLineEdge");
        edgeEgDataElem.appendChild(edgePleElem);

        Element edgeArrElem = doc.createElement("y:Arrows");
        edgePleElem.appendChild(edgeArrElem);
        edgeArrElem.setAttribute("source", sourceArrowStyle);
        edgeArrElem.setAttribute("target", targetArrowStyle);

        Element edgeBsElem = doc.createElement("y:BendStyle");
        edgePleElem.appendChild(edgeBsElem);
        edgeBsElem.setAttribute("smoothed", "true");
    }

    /**
     * Add a declaration reference node and edge.
     *
     * @param refId The 'id' of the 'node' that the reference refers to.
     * @param parent The parent XML element to which to add the reference node.
     */
    private void addDeclRef(String refId, Element parent) {
        Assert.check(relations.contains(RelationKind.DATA));

        // Add previously added, don't add a duplicate.
        Pair<Element, String> info = pair(parent, refId);
        boolean present = dataRefIds.contains(info);
        if (present) {
            return;
        }
        dataRefIds.add(info);

        // Add GraphML 'node'.
        Element nodeElem = doc.createElement("node");
        parent.appendChild(nodeElem);
        String nodeId = parent.getAttribute("id");
        Assert.check(nodeId.endsWith(":"));
        nodeId += ":dataref" + nextFreeDataRefId;
        nodeElem.setAttribute("id", nodeId);
        nextFreeDataRefId++;

        // Add yEd styles.
        int idx = refId.lastIndexOf(':');
        String declName = refId.substring(idx + 1);

        Element dnDataElem = doc.createElement("data");
        nodeElem.appendChild(dnDataElem);
        dnDataElem.setAttribute("key", "dn");
        dnDataElem.setTextContent(declName);

        Element nodeNgDataElem = doc.createElement("data");
        nodeElem.appendChild(nodeNgDataElem);
        nodeNgDataElem.setAttribute("key", "ng");

        Element nodeSnElem = doc.createElement("y:ShapeNode");
        nodeNgDataElem.appendChild(nodeSnElem);

        Element nodeGeoElem = doc.createElement("y:Geometry");
        nodeSnElem.appendChild(nodeGeoElem);
        nodeGeoElem.setAttribute("width", "30");
        nodeGeoElem.setAttribute("height", "30");

        Element nodeFillElem = doc.createElement("y:Fill");
        nodeSnElem.appendChild(nodeFillElem);
        nodeFillElem.setAttribute("color", DATA_REF_COLOR.color);

        Element nodeShapeElem = doc.createElement("y:Shape");
        nodeSnElem.appendChild(nodeShapeElem);
        nodeShapeElem.setAttribute("type", "ellipse");

        // Add edge.
        addDeclRef(refId, nodeId);
    }

    /**
     * Add a declaration reference edge.
     *
     * @param srcId The 'id' of the source 'node' of the edge.
     * @param tgtId The 'id' of the target 'node' of the edge.
     */
    private void addDeclRef(String srcId, String tgtId) {
        Assert.check(relations.contains(RelationKind.DATA));

        // Get edge and reverse edge, if they exist already. Shouldn't have
        // both of them (unless self loop, because then they are the same
        // edge).
        Pair<String, String> srcTgtIds = pair(srcId, tgtId);
        Pair<String, String> tgtSrcIds = pair(tgtId, srcId);
        Element edgeElem = dataEdgeElems.get(srcTgtIds);
        Element edgeElemRev = dataEdgeElems.get(tgtSrcIds);
        boolean selfLoop = srcTgtIds.equals(tgtSrcIds);
        if (selfLoop) {
            Assert.check(edgeElem == edgeElemRev);
        } else {
            Assert.check(edgeElem == null || edgeElemRev == null);
        }
        boolean reverse = !selfLoop && (edgeElemRev != null);
        if (reverse) {
            edgeElem = edgeElemRev;
        }

        // Create edge, if not yet created.
        if (edgeElem == null) {
            // Add GraphML 'edge'. Note that according to the GraphML
            // specification: "The edges between two nodes in a nested graph
            // have to be declared in a graph, which is an ancestor of both
            // nodes in the hierarchy. [...] A good policy is to place the
            // edges at the least common ancestor of the nodes in the
            // hierarchy, or at the top level."
            edgeElem = doc.createElement("edge");
            rootGraph.appendChild(edgeElem);
            edgeElem.setAttribute("source", srcId);
            edgeElem.setAttribute("target", tgtId);
            edgeElem.setAttribute("directed", "true");

            // Store information on this edge.
            dataEdgeElems.put(srcTgtIds, edgeElem);

            // Add yEd styles.
            Element edgeEgDataElem = doc.createElement("data");
            edgeElem.appendChild(edgeEgDataElem);
            edgeEgDataElem.setAttribute("key", "eg");

            Element edgePleElem = doc.createElement("y:PolyLineEdge");
            edgeEgDataElem.appendChild(edgePleElem);

            Element edgeArrElem = doc.createElement("y:Arrows");
            edgePleElem.appendChild(edgeArrElem);
            edgeArrElem.setAttribute("source", "none");
            edgeArrElem.setAttribute("target", "none");

            Element edgeBsElem = doc.createElement("y:BendStyle");
            edgePleElem.appendChild(edgeBsElem);
            edgeBsElem.setAttribute("smoothed", "true");
        }

        // Set edge to undirected, if it already existed, and now we're doing
        // the reverse direction.
        if (reverse) {
            edgeElem.setAttribute("directed", "false");
        }

        // Set arrow head.
        Element dataElem = getChildElem(edgeElem, "data");
        Assert.notNull(dataElem);
        Element pleElem = getChildElem(dataElem, "y:PolyLineEdge");
        Assert.notNull(pleElem);
        Element arrElem = getChildElem(pleElem, "y:Arrows");
        Assert.notNull(arrElem);
        arrElem.setAttribute(reverse ? "source" : "target", "arrow");
    }

    /**
     * Returns the 'id' of the 'node' elements created for the objects referred to by the given event references.
     *
     * @param eventRefs The event reference expressions. All 'tau' references are ignored.
     * @return The 'id' of the 'node' elements, or {@code null} for 'tau' references.
     */
    private Set<String> getEventRefIds(List<Expression> eventRefs) {
        Set<String> ids = set();
        for (Expression eventRef: eventRefs) {
            String id = getEventRefId(eventRef);
            if (id == null) {
                continue;
            }
            ids.add(id);
        }
        return ids;
    }

    /**
     * Returns the 'id' of the 'node' element created or to be created for the object referred to by the given event
     * reference.
     *
     * @param eventRef The event reference expression.
     * @return The 'id' of the 'node' element, or {@code null} for 'tau' references.
     */
    private String getEventRefId(Expression eventRef) {
        if (eventRef instanceof TauExpression) {
            return null;
        }
        return getObjRefId(eventRef);
    }

    /**
     * Returns the 'id' of a 'node' element created or to be created for the object referred to by the given component
     * reference.
     *
     * @param ref The object reference expression.
     * @return The 'id' of the 'node' element.
     */
    private String getObjRefId(Expression ref) {
        if (ref instanceof CompInstWrapExpression) {
            // Get 'via' instantiation 'id'.
            CompInstWrapExpression wrap = (CompInstWrapExpression)ref;
            ComponentInst inst = wrap.getInstantiation();
            String instId = getAbsObjRefId(inst);

            // Get 'via' component definition.
            ComponentDef cdef = CifTypeUtils.getCompDefFromCompInst(inst);

            // Get full 'id'.
            return getViaRefObjId(wrap.getReference(), cdef, instId);
        } else if (ref instanceof CompParamWrapExpression) {
            // Unsupported, so shouldn't get here.
            throw new RuntimeException("Unsupported: shouldn't get here.");
        } else {
            // Direct reference.
            PositionObject obj = CifScopeUtils.getRefObjFromRef(ref);
            return getAbsObjRefId(obj);
        }
    }

    /**
     * Returns the absolute 'id' of the 'node' element created for the given object.
     *
     * @param obj The object. Must be a {@link CifTextUtils#getName named object}. Must be referred to from the current
     *     scope that is being transformed.
     * @return The 'id' of the 'node' element.
     */
    private String getAbsObjRefId(PositionObject obj) {
        PositionObject objScope = CifScopeUtils.getScope(obj);
        PositionObject objRoot = CifScopeUtils.getScopeRoot(objScope);

        if (objRoot instanceof Specification) {
            // Use absolute name.
            String name = CifTextUtils.getAbsName(obj, false);
            return "cif:" + name.replace('.', ':');
        } else {
            // Component definition root scope.
            Assert.check(objRoot instanceof ComponentDef);
            ComponentDef cdef = (ComponentDef)objRoot;

            // Get 'id' for 'node' element of actual component created for
            // instantiated component definition.
            String compId = compDefMap.get(cdef);
            Assert.notNull(compId);

            // Construct 'id' for the object declaration/parameter.
            return compId + ":" + getRelObjId(objRoot, obj);
        }
    }

    /**
     * Returns the relative 'id' from the given root object to the given named object.
     *
     * @param root The root object. Must be the {@link CifScopeUtils#getScopeRoot root scope} of the given object.
     * @param obj The {@link CifTextUtils#getName named object}. Must not be the root object itself.
     * @return The relative 'id'.
     */
    private String getRelObjId(PositionObject root, PositionObject obj) {
        // Precondition check.
        Assert.check(root != obj);

        // Start with name of the object itself.
        String rslt = CifTextUtils.getName(obj);

        // Add intermediate scopes.
        PositionObject scope = CifScopeUtils.getScope(obj);
        while (scope != root) {
            rslt = CifTextUtils.getName(scope) + ":" + rslt;
            scope = CifScopeUtils.getScope(scope);
        }

        // Return relative 'id'.
        return rslt;
    }

    /**
     * Returns the absolute 'id' of the 'node' element created for the object referred to by the given object reference.
     *
     * @param ref The object reference expression.
     * @param cdef The component definition 'via' which we are referencing the reference expression. Sort of the current
     *     scope.
     * @param instId The absolute 'id' of the 'node' element created for the component instantiation that got us into
     *     the given component definition (into the current scope).
     * @return The absolute 'id' of the 'node' element.
     */
    private String getViaRefObjId(Expression ref, ComponentDef cdef, String instId) {
        if (ref instanceof CompInstWrapExpression) {
            // Get 'via' instantiation 'id'.
            CompInstWrapExpression wrap = (CompInstWrapExpression)ref;
            ComponentInst inst = wrap.getInstantiation();
            String childInstId = instId + ":" + getRelObjId(cdef, inst);

            // Get full 'id'.
            ComponentDef childCdef = CifTypeUtils.getCompDefFromCompInst(inst);
            return getViaRefObjId(wrap.getReference(), childCdef, childInstId);
        } else if (ref instanceof CompParamWrapExpression) {
            // We are not allowed to refer via an instantiation or parameter,
            // and then again via a parameter, as the latter parameter is not
            // visible from the outside. That is, this case shouldn't occur,
            // as it violates a language constraint.
            throw new RuntimeException("via something -> via param -> ref");
        } else {
            // Direct reference.
            PositionObject obj = CifScopeUtils.getRefObjFromRef(ref);
            return instId + ":" + getRelObjId(cdef, obj);
        }
    }

    /**
     * Reference expression collector. Only collects references to objects for which nodes are or will be created in the
     * diagram.
     */
    private abstract static class BaseRefExprCollector extends CifWalker {
        /** The collected reference expressions. */
        public List<Expression> refs = listc(1);

        @Override
        protected void walkAlgVariableExpression(AlgVariableExpression ref) {
            addRef(ref);
        }

        @Override
        protected void walkContVariableExpression(ContVariableExpression ref) {
            addRef(ref);
        }

        @Override
        protected void walkDiscVariableExpression(DiscVariableExpression ref) {
            addRef(ref);
        }

        @Override
        protected void walkInputVariableExpression(InputVariableExpression ref) {
            addRef(ref);
        }

        @Override
        protected void walkLocationExpression(LocationExpression ref) {
            addRef(ref);
        }

        /**
         * Adds a reference expression. Makes sure any ancestor wrapping expressions are included.
         *
         * @param ref The reference or wrapping expression.
         */
        private void addRef(Expression ref) {
            EObject parent = ref.eContainer();
            if (parent instanceof CompParamWrapExpression) {
                addRef((CompParamWrapExpression)parent);
            } else if (parent instanceof CompInstWrapExpression) {
                addRef((CompInstWrapExpression)parent);
            } else {
                refs.add(ref);
            }
        }
    }

    /**
     * Reference expression collector for arguments provided with component instantiations for algebraic parameters.
     * Collects reference expressions from a given expression, recursively. Only collects references to objects for
     * which nodes are or will be created in the diagram.
     */
    private static class ArgRefExprCollector extends BaseRefExprCollector {
        /**
         * Collect reference expressions.
         *
         * @param expr The expression from which to collect references.
         */
        public void collectRefExprs(Expression expr) {
            walkExpression(expr);
        }
    }

    /**
     * Reference expression collector for components. Collects reference expressions from the given component,
     * recursively. Excludes children that are separately collected. Only collects references to objects for which nodes
     * are or will be created in the diagram.
     */
    private static class CompRefExprCollector extends BaseRefExprCollector {
        /** Has the root component of the collection been encountered? */
        private boolean rootEncountered;

        /**
         * Collect reference expressions.
         *
         * @param comp The component from which to collect references.
         */
        public void collectRefExprs(ComplexComponent comp) {
            rootEncountered = false;
            walkComplexComponent(comp);
        }

        @Override
        protected void walkAlgVariable(AlgVariable var) {
            // Don't collect in child declaration.
        }

        @Override
        protected void walkContVariable(ContVariable var) {
            // Don't collect in child declaration.
        }

        @Override
        protected void walkDiscVariable(DiscVariable var) {
            // Don't collect in child declaration.
        }

        @Override
        protected void walkInputVariable(InputVariable var) {
            // Don't collect in child declaration.
        }

        @Override
        protected void walkLocation(Location loc) {
            // Don't collect in child declaration. However, if the location is
            // nameless, it is considered part of the automaton.
            if (loc.getName() == null) {
                super.walkLocation(loc);
            }
        }

        @Override
        protected void walkEquation(Equation equation) {
            // Don't collect in equations.
        }

        @Override
        protected void walkComponentDef(ComponentDef cdef) {
            // Don't collect in child component definitions.
        }

        @Override
        protected void walkComponent(Component comp) {
            // Don't collect in child components.
        }

        @Override
        protected void walkComplexComponent(ComplexComponent comp) {
            // Collect in root component of the collection.
            if (!rootEncountered) {
                super.walkComplexComponent(comp);
                return;
            }

            // Don't collect in child components.
        }

        @Override
        protected void walkGroup(Group grp) {
            // Collect in root component of the collection.
            if (!rootEncountered) {
                rootEncountered = true;
                super.walkGroup(grp);
                return;
            }

            // Don't collect in child components.
        }

        @Override
        protected void walkAutomaton(Automaton aut) {
            // Collect in root component of the collection.
            if (!rootEncountered) {
                rootEncountered = true;
                super.walkAutomaton(aut);
                return;
            }

            // Don't collect in child components.
        }

        @Override
        protected void walkComponentInst(ComponentInst inst) {
            // Don't collect in child components.
        }

        @Override
        protected void walkFunction(Function obj) {
            // Don't collect in a functions. We don't care about them. Also,
            // we don't want to collect references to their parameters and
            // local variables. Also, we can't refer to them from outside the
            // function, and we can't refer to interesting declarations outside
            // of the function from inside the function.
        }
    }

    /**
     * Reference expression collector for declarations (variables, locations, etc).Collects reference expressions from
     * the given declaration, recursively. Only collects references to objects for which nodes are or will be created in
     * the diagram.
     */
    private static class DeclRefExprCollector extends BaseRefExprCollector {
        /** Whether to skip equations. */
        private boolean skipEquations;

        /**
         * Collect reference expressions.
         *
         * @param decl The declaration from which to collect references.
         */
        public void collectRefExprs(Declaration decl) {
            skipEquations = false;

            // Walk declaration itself.
            walkDeclaration(decl);

            // Walk equations for algebraic variables and derivatives of
            // continuous variables.
            if (decl instanceof AlgVariable || decl instanceof ContVariable) {
                List<Equation> eqns = CifEquationUtils.getEquations(decl);
                for (Equation eqn: eqns) {
                    walkEquation(eqn);
                }
            }
        }

        /**
         * Collect reference expressions.
         *
         * @param loc The location declaration from which to collect references.
         */
        public void collectRefExprs(Location loc) {
            skipEquations = true;
            walkLocation(loc);
        }

        @Override
        public void walkEquation(Equation equation) {
            if (!skipEquations) {
                super.walkEquation(equation);
            }
        }
    }

    /**
     * Returns the synchronization alphabet of the given automaton. Can handle automaton definition/instantiation, and
     * both explicit and implicit alphabets.
     *
     * <p>
     * Since the result is a list of reference expressions, it may contain duplicates. It may contain structurally
     * identical reference expressions. It may also contain structurally different reference expressions that via
     * different parameters refer to the same actual objects, given the available actual arguments.
     * </p>
     *
     * @param aut The automaton.
     * @return The synchronization alphabet.
     */
    private List<Expression> getSyncAlphabet(Automaton aut) {
        // Explicit alphabet.
        if (aut.getAlphabet() != null) {
            return aut.getAlphabet().getEvents();
        }

        // Implicit alphabet.
        List<Expression> alphabet = list();
        for (Location loc: aut.getLocations()) {
            for (Edge edge: loc.getEdges()) {
                for (EdgeEvent edgeEvent: edge.getEvents()) {
                    if (edgeEvent instanceof EdgeSend) {
                        continue;
                    } else if (edgeEvent instanceof EdgeReceive) {
                        continue;
                    }
                    alphabet.add(edgeEvent.getEvent());
                }
            }
        }
        return alphabet;
    }

    /**
     * Returns the send alphabet of the given automaton. Can handle automaton definition/instantiation.
     *
     * <p>
     * Since the result is a list of reference expressions, it may contain duplicates. It may contain structurally
     * identical reference expressions. It may also contain structurally different reference expressions that via
     * different parameters refer to the same actual objects, given the available actual arguments.
     * </p>
     *
     * @param aut The automaton.
     * @return The send alphabet.
     */
    private List<Expression> getSendAlphabet(Automaton aut) {
        List<Expression> alphabet = list();
        for (Location loc: aut.getLocations()) {
            for (Edge edge: loc.getEdges()) {
                for (EdgeEvent edgeEvent: edge.getEvents()) {
                    if (edgeEvent instanceof EdgeSend) {
                        alphabet.add(edgeEvent.getEvent());
                    }
                }
            }
        }
        return alphabet;
    }

    /**
     * Returns the receive alphabet of the given automaton. Can handle automaton definition/instantiation.
     *
     * <p>
     * Since the result is a list of reference expressions, it may contain duplicates. It may contain structurally
     * identical reference expressions. It may also contain structurally different reference expressions that via
     * different parameters refer to the same actual objects, given the available actual arguments.
     * </p>
     *
     * @param aut The automaton.
     * @return The receive alphabet.
     */
    private List<Expression> getRecvAlphabet(Automaton aut) {
        List<Expression> alphabet = list();
        for (Location loc: aut.getLocations()) {
            for (Edge edge: loc.getEdges()) {
                for (EdgeEvent edgeEvent: edge.getEvents()) {
                    if (edgeEvent instanceof EdgeReceive) {
                        alphabet.add(edgeEvent.getEvent());
                    }
                }
            }
        }
        return alphabet;
    }

    /**
     * Removes the internal edges of component instantiations, i.e. the edges without a target node outside the
     * instantiation node.
     *
     * @param elem The element to which to perform the operation, recursively.
     */
    private void removeInstInternalEdges(Element elem) {
        // Remove edge, if applicable.
        if (elem.getTagName().equals("edge")) {
            String srcId = elem.getAttribute("source");
            String tgtId = elem.getAttribute("target");
            Assert.check(!srcId.isEmpty());
            Assert.check(!tgtId.isEmpty());
            List<String> commonPrefix = getCommonPrefix(srcId, tgtId);
            for (int i = commonPrefix.size(); i > 0; i--) {
                List<String> prefix = commonPrefix.subList(0, i);
                String instId = StringUtils.join(prefix, ":");
                if (instIds.contains(instId)) {
                    elem.getParentNode().removeChild(elem);
                    return;
                }
            }
        }

        // Apply recursively.
        NodeList children = elem.getChildNodes();
        for (int i = children.getLength() - 1; i >= 0; i--) {
            Node child = children.item(i);
            if (child instanceof Element) {
                removeInstInternalEdges((Element)child);
            }
        }
    }

    /**
     * Given two ids, with {@code ":"} separated parts, returns the parts of the common prefix of the two ids. Only
     * whole parts are returned.
     *
     * @param id1 The first id.
     * @param id2 The second id.
     * @return The parts of the common prefix of the two ids.
     */
    private List<String> getCommonPrefix(String id1, String id2) {
        String[] parts1 = StringUtils.split(id1, ":");
        String[] parts2 = StringUtils.split(id2, ":");
        List<String> rslt = listc(Math.max(parts1.length, parts2.length));
        for (int i = 0; i < Math.min(parts1.length, parts2.length); i++) {
            if (!parts1[i].equals(parts2[i])) {
                break;
            }
            rslt.add(parts1[i]);
        }
        return rslt;
    }

    /**
     * Collect the source/target node 'ids' of all edges.
     *
     * @param elem The element in which to look for edges, recursively.
     * @param ids The 'ids' collected so far. Is modified in-place.
     */
    private void collectEdgeSrcTgtIds(Element elem, Set<String> ids) {
        // Collects edge source/target node 'ids'.
        if (elem.getTagName().equals("edge")) {
            String srcId = elem.getAttribute("source");
            String tgtId = elem.getAttribute("target");
            Assert.check(!srcId.isEmpty());
            Assert.check(!tgtId.isEmpty());
            ids.add(srcId);
            ids.add(tgtId);
        }

        // Collect recursively.
        NodeList children = elem.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child instanceof Element) {
                collectEdgeSrcTgtIds((Element)child, ids);
            }
        }
    }

    /**
     * Removes the internal nodes of component instantiations, i.e. the nodes in component instantiation nodes that have
     * no edges at all. This applies only to declaration, parameter, and reference nodes, i.e. nodes without sub-graphs.
     *
     * @param elem The element to which to perform the operation, recursively.
     * @param nodeIds The 'ids' of all nodes with edges.
     */
    private void removeInstInternalNodes(Element elem, Set<String> nodeIds) {
        // Remove node, if applicable.
        if (elem.getTagName().equals("node")) {
            String id = elem.getAttribute("id");
            Assert.check(!id.isEmpty());
            if (!nodeIds.contains(id) && getChildElem(elem, "graph") == null) {
                String[] parts = StringUtils.split(id, ":");
                for (int i = parts.length - 1; i > 0; i--) {
                    String[] prefix = (String[])ArrayUtils.subarray(parts, 0, i);
                    String instId = StringUtils.join(prefix, ":");
                    if (instIds.contains(instId)) {
                        elem.getParentNode().removeChild(elem);
                        return;
                    }
                }
            }
        }

        // Apply recursively.
        NodeList children = elem.getChildNodes();
        for (int i = children.getLength() - 1; i >= 0; i--) {
            Node child = children.item(i);
            if (child instanceof Element) {
                removeInstInternalNodes((Element)child, nodeIds);
            }
        }
    }

    /**
     * Returns a child element of an element, with a given child element name.
     *
     * @param elem The element.
     * @param name The child element name.
     * @return The child element, or {@code null} if not found.
     */
    private Element getChildElem(Element elem, String name) {
        NodeList children = elem.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child instanceof Element) {
                Element childElem = (Element)child;
                if (childElem.getTagName().equals(name)) {
                    return childElem;
                }
            }
        }
        return null;
    }

    /**
     * Remove all empty components, component definitions, and component instantiations. Also removes unnecessary
     * {@link #boxIds boxes}, used to separate components from component definitions.
     *
     * @param elem The element to which to perform the operation, recursively.
     */
    private void removeEmptyComps(Element elem) {
        // Apply recursively.
        NodeList children = elem.getChildNodes();
        for (int i = children.getLength() - 1; i >= 0; i--) {
            Node child = children.item(i);
            if (child instanceof Element) {
                removeEmptyComps((Element)child);
            }
        }

        // Process nodes.
        Assert.check(elem.getParentNode() != null);
        boolean isRoot = elem.getParentNode() instanceof Document;
        if (isRoot || elem.getTagName().equals("node")) {
            String id = elem.getAttribute("id");
            if (!isRoot) {
                Assert.check(!id.isEmpty());
            }

            // Remove wrapper 'box' if no sibling 'box'.
            if (isRoot || compIds.contains(id)) {
                // Get child 'graph'.
                Element graphElem = getChildElem(elem, "graph");
                if (graphElem == null) {
                    throw new RuntimeException("no graph");
                }

                // Count box elements.
                List<Element> boxElems = list();
                children = graphElem.getChildNodes();
                for (int i = 0; i < children.getLength(); i++) {
                    Node child = children.item(i);
                    if (child instanceof Element) {
                        Element childElem = (Element)child;
                        if (!childElem.getTagName().equals("node")) {
                            continue;
                        }
                        String boxId = childElem.getAttribute("id");
                        if (boxIds.contains(boxId)) {
                            boxElems.add(childElem);
                        }
                    }
                }

                // Unwrap, if applicable.
                if (boxElems.size() == 1) {
                    Element boxElem = boxElems.get(0);
                    Element boxGraphElem = getChildElem(boxElem, "graph");

                    // Move children of 'graph' of the 'box' out of the 'box'.
                    children = boxGraphElem.getChildNodes();
                    for (int i = children.getLength() - 1; i >= 0; i--) {
                        Node child = children.item(i);
                        if (child instanceof Element) {
                            Element childElem = (Element)child;
                            if (childElem.getTagName().equals("node")) {
                                graphElem.appendChild(childElem);
                            }
                        }
                    }

                    // Remove 'box' and its empty 'graph'.
                    boxElem.getParentNode().removeChild(boxElem);
                }
            }

            // Remove node, if no sub-nodes.
            if (compIds.contains(id) || boxIds.contains(id)) {
                // Get child 'graph'.
                Element graphElem = getChildElem(elem, "graph");
                if (graphElem == null) {
                    throw new RuntimeException("no graph");
                }

                // Count 'node' elements.
                int nodeCnt = 0;
                children = graphElem.getChildNodes();
                for (int i = 0; i < children.getLength(); i++) {
                    Node child = children.item(i);
                    if (child instanceof Element) {
                        Element childElem = (Element)child;
                        if (childElem.getTagName().equals("node")) {
                            nodeCnt++;
                        }
                    }
                }

                // Remove if empty.
                if (nodeCnt == 0) {
                    elem.getParentNode().removeChild(elem);
                }
            }
        }
    }
}
