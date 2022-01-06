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

import static org.eclipse.escet.cif.cif2yed.CifToYedColors.CODE_BG_COLOR;
import static org.eclipse.escet.cif.cif2yed.CifToYedColors.CODE_HEADER_COLOR;
import static org.eclipse.escet.cif.cif2yed.CifToYedColors.COMP_BG_CLOSED_COLOR;
import static org.eclipse.escet.cif.cif2yed.CifToYedColors.COMP_BG_OPENED_COLOR;
import static org.eclipse.escet.cif.cif2yed.CifToYedColors.COMP_HEADER_COLOR;
import static org.eclipse.escet.cif.cif2yed.CifToYedColors.DEF_HEADER_COLOR;
import static org.eclipse.escet.cif.cif2yed.CifToYedColors.INST_HEADER_COLOR;
import static org.eclipse.escet.cif.cif2yed.CifToYedColors.LOC_BG_COLOR;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.str;

import java.awt.geom.Rectangle2D;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.eclipse.escet.cif.cif2yed.options.ModelFilter;
import org.eclipse.escet.cif.cif2yed.options.ModelFiltersOption;
import org.eclipse.escet.cif.common.CifEdgeUtils;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifValueUtils;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.ComponentDef;
import org.eclipse.escet.cif.metamodel.cif.ComponentInst;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.IoDecl;
import org.eclipse.escet.cif.metamodel.cif.Parameter;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.SupKind;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.automata.Update;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.prettyprinter.CifPrettyPrinter;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.box.MemoryCodeBox;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Strings;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;
import org.w3c.dom.Element;

/** CIF to yEd 'model' diagram transformation. */
public class CifToYedModelDiagram extends CifToYedDiagram {
    /** The model filters, the model features to include in the diagram. */
    private Set<ModelFilter> filters;

    @Override
    protected void addSpec(Specification spec, Element root) {
        // Initialize options.
        filters = ModelFiltersOption.getFilters();

        // Add root 'graph' element.
        rootGraph = doc.createElement("graph");
        root.appendChild(rootGraph);
        rootGraph.setAttribute("id", getId(spec));
        rootGraph.setAttribute("edgedefault", "directed");

        // Add body.
        addCompBody(spec, rootGraph);
    }

    /**
     * Add a component.
     *
     * @param comp The component.
     * @param parent The parent XML element to which to add new elements.
     */
    private void addComp(Component comp, Element parent) {
        if (comp instanceof Group) {
            addGroup((Group)comp, parent);
        } else if (comp instanceof Automaton) {
            addAutomaton((Automaton)comp, parent);
        } else if (comp instanceof ComponentInst) {
            // Should already have been handled as a special case by
            // 'addCompBody'.
            throw new RuntimeException("Should not get here.");
        } else {
            throw new RuntimeException("Unknown component: " + comp);
        }
    }

    /**
     * Add a group.
     *
     * @param group The group.
     * @param parent The parent XML element to which to add new elements.
     */
    private void addGroup(Group group, Element parent) {
        // Add the group.
        parent = addCompNode(group, parent);

        // Add the body.
        addCompBody(group, parent);
    }

    /**
     * Add an automaton.
     *
     * @param aut The automaton.
     * @param parent The parent XML element to which to add new elements.
     */
    private void addAutomaton(Automaton aut, Element parent) {
        // Add the automaton.
        parent = addCompNode(aut, parent);

        // Add the body.
        addCompBody(aut, parent);
    }

    /**
     * Add a component definition.
     *
     * @param cdef The component definition.
     * @param parent The parent XML element to which to add new elements.
     */
    private void addCompDef(ComponentDef cdef, Element parent) {
        // Add the component definition.
        Component body = cdef.getBody();
        parent = addCompNode(body, parent);

        // Add the body.
        addCompBody((ComplexComponent)body, parent);
    }

    /**
     * Add a node for a component.
     *
     * @param comp The component. Must be an automaton, a group, or a component definition body. Must not be a component
     *     instantiation.
     * @param parent The parent XML element to which to add new elements.
     * @return The node created for the component.
     */
    private Element addCompNode(Component comp, Element parent) {
        // Get component definition and body, if applicable.
        boolean isBody = comp.eContainer() instanceof ComponentDef;
        ComponentDef cdef = null;
        Component body = comp;
        if (isBody) {
            cdef = (ComponentDef)comp.eContainer();
        }

        // Determine type of component.
        boolean isAut = body instanceof Automaton;
        boolean isGroup = body instanceof Group;
        Assert.check(isAut || isGroup);

        // Add GraphML 'node'.
        Element nodeElem = doc.createElement("node");
        parent.appendChild(nodeElem);
        String id = getId(comp);
        nodeElem.setAttribute("id", id);
        parent = nodeElem;

        // Add yEd styles.
        nodeElem.setAttribute("yfiles.foldertype", "group");

        Element dnDataElem = doc.createElement("data");
        nodeElem.appendChild(dnDataElem);
        dnDataElem.setAttribute("key", "dn");

        Element ngDataElem = doc.createElement("data");
        nodeElem.appendChild(ngDataElem);
        ngDataElem.setAttribute("key", "ng");

        Element pabnElem = doc.createElement("y:ProxyAutoBoundsNode");
        ngDataElem.appendChild(pabnElem);

        Element realElem = doc.createElement("y:Realizers");
        pabnElem.appendChild(realElem);
        realElem.setAttribute("active", "0");

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
            throw new RuntimeException("Unknown component: " + comp);
        }

        String title;
        CifToYedColors bgColor;
        if (cdef == null) {
            title = kindText + " " + comp.getName();
            bgColor = COMP_HEADER_COLOR;
        } else {
            CifPrettyPrinter pprinter = new CifPrettyPrinter(null);
            StringBuilder params = new StringBuilder();
            for (Parameter param: cdef.getParameters()) {
                if (params.length() > 0) {
                    params.append("; ");
                }
                params.append(pprinter.pprint(param));
            }
            title = fmt("%s def %s(%s)", kindText, comp.getName(), params);
            bgColor = DEF_HEADER_COLOR;
        }

        dnDataElem.setTextContent(title);

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
            CifToYedColors fillColor = closed ? COMP_BG_CLOSED_COLOR : COMP_BG_OPENED_COLOR;
            fillElem.setAttribute("color", fillColor.color);

            Element shapeElem = doc.createElement("y:Shape");
            gnElem.appendChild(shapeElem);
            shapeElem.setAttribute("type", "rectangle");

            Element stateElem = doc.createElement("y:State");
            gnElem.appendChild(stateElem);
            stateElem.setAttribute("closed", str(closed));
        }

        // Add 'graph' element.
        Element graphElem = doc.createElement("graph");
        parent.appendChild(graphElem);
        graphElem.setAttribute("id", id + ":");
        graphElem.setAttribute("edgedefault", "directed");
        parent = graphElem;
        return parent;
    }

    /**
     * Add component instantiations.
     *
     * @param insts The component instantiations.
     * @param parent The parent XML element to which to add new elements.
     */
    private void addCompInsts(List<ComponentInst> insts, Element parent) {
        // Skip if no instantiations.
        if (insts.isEmpty()) {
            return;
        }

        // Add GraphML 'node'.
        Element nodeElem = doc.createElement("node");
        parent.appendChild(nodeElem);
        String id = parent.getAttribute("id");
        Assert.check(id.endsWith(":"));
        id += ":insts";
        nodeElem.setAttribute("id", id);

        // Get instantiations code.
        MemoryCodeBox code = new MemoryCodeBox(CifPrettyPrinter.INDENT);
        CifPrettyPrinter pprinter = new CifPrettyPrinter(code);
        for (ComponentInst inst: insts) {
            pprinter.add(inst);
        }
        String text = code.toString();

        // Add yEd styles.
        nodeElem.setAttribute("yfiles.foldertype", "group");

        String title = "Instantiations";

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
        realElem.setAttribute("active", "0");

        for (boolean closed: list(false, true)) {
            Element gnElem = doc.createElement("y:GroupNode");
            realElem.appendChild(gnElem);

            String labelHeader = title;
            labelHeader = Strings.spaces(5) + labelHeader; // Avoid label behind '-' icon.

            String labelText = closed ? "..." : text;

            Element geoElem = doc.createElement("y:Geometry");
            gnElem.appendChild(geoElem);
            Rectangle2D sizeHeader = guessTextSize(labelHeader, 5);
            Rectangle2D sizeText = guessTextSize(labelText, 5);
            double width = Math.max(sizeHeader.getWidth(), sizeText.getWidth());
            double height = sizeHeader.getHeight() + sizeText.getHeight() - 6;
            geoElem.setAttribute("width", str(width));
            geoElem.setAttribute("height", str(height));

            Element nlElemHeader = doc.createElement("y:NodeLabel");
            gnElem.appendChild(nlElemHeader);
            nlElemHeader.setAttribute("alignment", "left");
            nlElemHeader.setAttribute("autoSizePolicy", "node_width");
            nlElemHeader.setAttribute("backgroundColor", INST_HEADER_COLOR.color);
            nlElemHeader.setAttribute("modelName", "internal");
            nlElemHeader.setAttribute("modelPosition", "t");
            nlElemHeader.setTextContent(labelHeader);

            labelHeader = highlight(labelHeader);
            if (!closed) {
                labelText = highlight(labelText);
            }

            Element nlElemText = doc.createElement("y:NodeLabel");
            gnElem.appendChild(nlElemText);
            nlElemText.setAttribute("alignment", "left");
            nlElemText.setAttribute("autoSizePolicy", "content");
            nlElemText.setAttribute("modelName", "internal");
            nlElemText.setAttribute("modelPosition", "t");
            nlElemText.setAttribute("borderDistance", "18");
            nlElemText.setAttribute("topInset", "3");
            nlElemText.setAttribute("bottomInset", "3");
            nlElemText.setAttribute("leftInset", "3");
            nlElemText.setAttribute("rightInset", "3");
            nlElemText.setTextContent(labelText);

            Element fillElem = doc.createElement("y:Fill");
            gnElem.appendChild(fillElem);
            CifToYedColors fillColor = closed ? COMP_BG_CLOSED_COLOR : COMP_BG_OPENED_COLOR;
            fillElem.setAttribute("color", fillColor.color);

            Element shapeElem = doc.createElement("y:Shape");
            gnElem.appendChild(shapeElem);
            shapeElem.setAttribute("type", "rectangle");

            Element stateElem = doc.createElement("y:State");
            gnElem.appendChild(stateElem);
            stateElem.setAttribute("closed", str(closed));
        }
    }

    /**
     * Add a component body.
     *
     * @param comp The component.
     * @param parent The parent XML element to which to add new elements.
     */
    private void addCompBody(ComplexComponent comp, Element parent) {
        // Add child components and component definitions.
        if (comp instanceof Group) {
            // Add component definitions, separately.
            for (ComponentDef cdef: ((Group)comp).getDefinitions()) {
                addCompDef(cdef, parent);
            }

            // Get component instantiations and other child components.
            List<ComponentInst> insts = list();
            List<ComplexComponent> others = list();
            for (Component child: ((Group)comp).getComponents()) {
                if (child instanceof ComponentInst) {
                    insts.add((ComponentInst)child);
                } else {
                    boolean isAut = child instanceof Automaton;
                    boolean isGroup = child instanceof Group;
                    Assert.check(isAut || isGroup);
                    others.add((ComplexComponent)child);
                }
            }

            // Add other child components, separately.
            for (ComplexComponent child: others) {
                addComp(child, parent);
            }

            // Add component instantiations, together.
            addCompInsts(insts, parent);
        }

        // Get alphabet and monitors code.
        MemoryCodeBox code1 = new MemoryCodeBox(CifPrettyPrinter.INDENT);
        if (filters.contains(ModelFilter.DECLS)) {
            CifPrettyPrinter pprinter1 = new CifPrettyPrinter(code1);
            if (comp instanceof Automaton) {
                Automaton aut = (Automaton)comp;
                pprinter1.add(aut.getAlphabet());
                pprinter1.add(aut.getMonitors());
            }
        }

        // Get declarations code.
        MemoryCodeBox code2 = new MemoryCodeBox(CifPrettyPrinter.INDENT);
        if (filters.contains(ModelFilter.DECLS)) {
            CifPrettyPrinter pprinter2 = new CifPrettyPrinter(code2);
            for (Declaration decl: comp.getDeclarations()) {
                pprinter2.add(decl);
            }
        }

        // Get initialization, invariants, equations and marking code.
        MemoryCodeBox code3 = new MemoryCodeBox(CifPrettyPrinter.INDENT);
        if (filters.contains(ModelFilter.DECLS)) {
            CifPrettyPrinter pprinter3 = new CifPrettyPrinter(code3);
            pprinter3.addInitInvEqnsMarked(comp.getInitials(), comp.getInvariants(), comp.getEquations(),
                    comp.getMarkeds(), false);
        }

        // Get I/O declarations code.
        MemoryCodeBox code4 = new MemoryCodeBox(CifPrettyPrinter.INDENT);
        if (filters.contains(ModelFilter.IO)) {
            CifPrettyPrinter pprinter4 = new CifPrettyPrinter(code4);
            for (IoDecl ioDecl: comp.getIoDecls()) {
                pprinter4.add(ioDecl);
            }
        }

        // Get complete code.
        MemoryCodeBox code = new MemoryCodeBox(CifPrettyPrinter.INDENT);
        for (CodeBox c: new CodeBox[] {code1, code2, code3, code4}) {
            if (c.getLines().isEmpty()) {
                continue;
            }
            if (!code.getLines().isEmpty()) {
                code.add();
            }
            code.add(c.getLines());
        }

        // Add CIF code block.
        addCifCodeNode(code, parent);

        // Add locations with their outgoing edges.
        if (comp instanceof Automaton) {
            Automaton aut = (Automaton)comp;
            for (int i = 0; i < aut.getLocations().size(); i++) {
                Location loc = aut.getLocations().get(i);
                addLocation(loc, i, parent);
            }
        }
    }

    /**
     * Add a node for some CIF code. Does nothing if no code is provided.
     *
     * @param code The CIF code. Does not have to contain actual code.
     * @param parent The parent XML element to which to add new elements.
     */
    private void addCifCodeNode(CodeBox code, Element parent) {
        // Get code text. Skip adding node if no code.
        String text = code.toString();
        if (text.isEmpty()) {
            return;
        }

        // Add GraphML 'node'.
        Element nodeElem = doc.createElement("node");
        parent.appendChild(nodeElem);
        String id = parent.getAttribute("id");
        Assert.check(id.endsWith(":"));
        id += ":code";
        nodeElem.setAttribute("id", id);

        // Add yEd styles.
        nodeElem.setAttribute("yfiles.foldertype", "group");

        String title = "Declarations";

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
        realElem.setAttribute("active", "0");

        for (boolean closed: list(false, true)) {
            Element gnElem = doc.createElement("y:GroupNode");
            realElem.appendChild(gnElem);

            String labelHeader = title;
            labelHeader = Strings.spaces(5) + labelHeader; // Avoid label behind '-' icon.

            String labelText = closed ? "..." : text;

            Element geoElem = doc.createElement("y:Geometry");
            gnElem.appendChild(geoElem);
            Rectangle2D sizeHeader = guessTextSize(labelHeader, 5);
            Rectangle2D sizeText = guessTextSize(labelText, 5);
            double width = Math.max(sizeHeader.getWidth(), sizeText.getWidth());
            double height = sizeHeader.getHeight() + sizeText.getHeight() - 6;
            geoElem.setAttribute("width", str(width));
            geoElem.setAttribute("height", str(height));

            Element nlElemHeader = doc.createElement("y:NodeLabel");
            gnElem.appendChild(nlElemHeader);
            nlElemHeader.setAttribute("alignment", "left");
            nlElemHeader.setAttribute("autoSizePolicy", "node_width");
            nlElemHeader.setAttribute("backgroundColor", CODE_HEADER_COLOR.color);
            nlElemHeader.setAttribute("modelName", "internal");
            nlElemHeader.setAttribute("modelPosition", "t");
            nlElemHeader.setTextContent(labelHeader);

            labelHeader = highlight(labelHeader);
            if (!closed) {
                labelText = highlight(labelText);
            }

            Element nlElemText = doc.createElement("y:NodeLabel");
            gnElem.appendChild(nlElemText);
            nlElemText.setAttribute("alignment", "left");
            nlElemText.setAttribute("autoSizePolicy", "content");
            nlElemText.setAttribute("modelName", "internal");
            nlElemText.setAttribute("modelPosition", "t");
            nlElemText.setAttribute("borderDistance", "18");
            nlElemText.setAttribute("topInset", "3");
            nlElemText.setAttribute("bottomInset", "3");
            nlElemText.setAttribute("leftInset", "3");
            nlElemText.setAttribute("rightInset", "3");
            nlElemText.setTextContent(labelText);

            Element bsElem = doc.createElement("y:BorderStyle");
            gnElem.appendChild(bsElem);
            bsElem.setAttribute("lineType", "dashed");

            Element fillElem = doc.createElement("y:Fill");
            gnElem.appendChild(fillElem);
            fillElem.setAttribute("color", CODE_BG_COLOR.color);

            Element shapeElem = doc.createElement("y:Shape");
            gnElem.appendChild(shapeElem);
            shapeElem.setAttribute("type", "rectangle");

            Element stateElem = doc.createElement("y:State");
            gnElem.appendChild(stateElem);
            stateElem.setAttribute("closed", str(closed));
        }
    }

    /**
     * Add the locations and its outgoing edges.
     *
     * @param loc The location.
     * @param idx The 0-based index of the location in the automaton.
     * @param parent The parent XML element to which to add new elements.
     */
    private void addLocation(Location loc, int idx, Element parent) {
        // Add GraphML 'node'.
        Element nodeElem = doc.createElement("node");
        parent.appendChild(nodeElem);
        String parentId = parent.getAttribute("id");
        Assert.check(parentId.endsWith(":"));
        String locId = parentId + ":loc" + str(idx);
        nodeElem.setAttribute("id", locId);

        // Get name code.
        MemoryCodeBox code1 = new MemoryCodeBox(CifPrettyPrinter.INDENT);
        if (loc.getName() != null) {
            code1.add(loc.getName());
        }

        // Get invariants, equations, marking, and urgency code.
        MemoryCodeBox code2 = new MemoryCodeBox(CifPrettyPrinter.INDENT);
        if (filters.contains(ModelFilter.LOC_DECLS)) {
            CifPrettyPrinter pprinter2 = new CifPrettyPrinter(code2);
            pprinter2.addInitInvEqnsMarked(Collections.emptyList(), loc.getInvariants(), loc.getEquations(),
                    loc.getMarkeds(), true);
            if (loc.isUrgent()) {
                code2.add("urgent;");
            }
        }

        // Get description.
        String description = "location";
        if (loc.getName() != null) {
            description += " " + loc.getName();
        }

        // Get label.
        MemoryCodeBox code = new MemoryCodeBox(CifPrettyPrinter.INDENT);
        for (CodeBox c: new CodeBox[] {code1, code2}) {
            if (c.getLines().isEmpty()) {
                continue;
            }
            if (!code.getLines().isEmpty()) {
                code.add();
            }
            code.add(c.getLines());
        }

        String label = code.toString();

        // Add yEd styles.
        Element dnDataElem = doc.createElement("data");
        nodeElem.appendChild(dnDataElem);
        dnDataElem.setAttribute("key", "dn");
        dnDataElem.setTextContent(description);

        Element ngDataElem = doc.createElement("data");
        nodeElem.appendChild(ngDataElem);
        ngDataElem.setAttribute("key", "ng");

        Element snElem = doc.createElement("y:ShapeNode");
        ngDataElem.appendChild(snElem);

        Element geoElem = doc.createElement("y:Geometry");
        snElem.appendChild(geoElem);
        Rectangle2D size = guessTextSize(label, 5);
        double width = size.getWidth() + 30;
        double height = size.getHeight();
        geoElem.setAttribute("width", str(width));
        geoElem.setAttribute("height", str(height));

        label = highlight(label);

        Element nlElem = doc.createElement("y:NodeLabel");
        snElem.appendChild(nlElem);
        nlElem.setAttribute("alignment", "left");
        nlElem.setAttribute("autoSizePolicy", "content");
        nlElem.setAttribute("modelName", "internal");
        nlElem.setAttribute("modelPosition", "c");
        nlElem.setTextContent(label);

        Element fillElem = doc.createElement("y:Fill");
        snElem.appendChild(fillElem);
        fillElem.setAttribute("color", LOC_BG_COLOR.color);

        Element shapeElem = doc.createElement("y:Shape");
        snElem.appendChild(shapeElem);
        shapeElem.setAttribute("type", "roundrectangle");

        // Add initialization.
        addLocInit(loc, locId, parent);

        // Add outgoing edges.
        for (Edge edge: loc.getEdges()) {
            // Get target location node id.
            Automaton aut = (Automaton)loc.eContainer();
            Location tgtLoc = CifEdgeUtils.getTarget(edge);
            int tgtIdx = aut.getLocations().indexOf(tgtLoc);
            String tgtId = parentId + ":loc" + str(tgtIdx);

            // Add edge
            addEdge(edge, locId, tgtId, parent);
        }
    }

    /**
     * Add initialization of a location.
     *
     * @param loc The location.
     * @param locId The 'id' of the node of the location.
     * @param parent The parent XML element to which to add new elements.
     */
    private void addLocInit(Location loc, String locId, Element parent) {
        // Skip if not initial location.
        List<Expression> inits = loc.getInitials();
        boolean hasInit = !inits.isEmpty() && !CifValueUtils.isTriviallyFalse(inits, true, true);
        if (!hasInit) {
            return;
        }

        // Add GraphML 'node'.
        Element initNodeElem = doc.createElement("node");
        parent.appendChild(initNodeElem);
        String initId = locId + "::init";
        initNodeElem.setAttribute("id", initId);

        // Add yEd styles.
        Element dnDataElem = doc.createElement("data");
        initNodeElem.appendChild(dnDataElem);
        dnDataElem.setAttribute("key", "dn");
        dnDataElem.setTextContent("<init>");

        Element initNgDataElem = doc.createElement("data");
        initNodeElem.appendChild(initNgDataElem);
        initNgDataElem.setAttribute("key", "ng");

        Element initSnElem = doc.createElement("y:ShapeNode");
        initNgDataElem.appendChild(initSnElem);

        Element initGeoElem = doc.createElement("y:Geometry");
        initSnElem.appendChild(initGeoElem);
        initGeoElem.setAttribute("width", "1");
        initGeoElem.setAttribute("height", "1");

        Element initBsElem = doc.createElement("y:BorderStyle");
        initSnElem.appendChild(initBsElem);
        initBsElem.setAttribute("hasColor", "false");

        Element initFillElem = doc.createElement("y:Fill");
        initSnElem.appendChild(initFillElem);
        initFillElem.setAttribute("transparent", "true");

        // Add GraphML 'edge'. Note that according to the GraphML
        // specification: "The edges between two nodes in a nested graph
        // have to be declared in a graph, which is an ancestor of both
        // nodes in the hierarchy. [...] A good policy is to place the
        // edges at the least common ancestor of the nodes in the
        // hierarchy, or at the top level."
        Element initEdgeElem = doc.createElement("edge");
        parent.appendChild(initEdgeElem);
        initEdgeElem.setAttribute("source", initId);
        initEdgeElem.setAttribute("target", locId);

        // Add yEd styles.
        Element initEdgeEgDataElem = doc.createElement("data");
        initEdgeElem.appendChild(initEdgeEgDataElem);
        initEdgeEgDataElem.setAttribute("key", "eg");

        Element initEdgePleElem = doc.createElement("y:PolyLineEdge");
        initEdgeEgDataElem.appendChild(initEdgePleElem);

        Element initEdgeArrElem = doc.createElement("y:Arrows");
        initEdgePleElem.appendChild(initEdgeArrElem);
        initEdgeArrElem.setAttribute("source", "none");
        initEdgeArrElem.setAttribute("target", "arrow");

        Element initEdgeBsElem = doc.createElement("y:BendStyle");
        initEdgePleElem.appendChild(initEdgeBsElem);
        initEdgeBsElem.setAttribute("smoothed", "true");

        // Add edge label, if not trivially 'true'.
        if (!CifValueUtils.isTriviallyTrue(inits, true, true)) {
            CifPrettyPrinter pprinter = new CifPrettyPrinter(null);
            String label = pprinter.pprint(inits, ", ");

            Element edgeLblElem = doc.createElement("y:EdgeLabel");
            initEdgePleElem.appendChild(edgeLblElem);
            edgeLblElem.setAttribute("alignment", "center");
            edgeLblElem.setAttribute("modelName", "six_pos");
            edgeLblElem.setAttribute("modelPosition", "tail");
            addEdgeLabelBackground(edgeLblElem);

            label = highlight(label);
            edgeLblElem.setTextContent(label);
        }
    }

    /**
     * Add an edge.
     *
     * @param edge The edge. May be a 'tau' reference.
     * @param src The 'id' of the source location node.
     * @param tgt The 'id' of the target location node.
     * @param parent The parent XML element to which to add new elements.
     */
    private void addEdge(Edge edge, String src, String tgt, Element parent) {
        // Add GraphML 'edge'. Note that according to the GraphML
        // specification: "The edges between two nodes in a nested graph have
        // to be declared in a graph, which is an ancestor of both nodes in the
        // hierarchy. [...] A good policy is to place the edges at the least
        // common ancestor of the nodes in the hierarchy, or at the top level."
        Element edgeElem = doc.createElement("edge");
        parent.appendChild(edgeElem);
        edgeElem.setAttribute("source", src);
        edgeElem.setAttribute("target", tgt);

        // Add yEd styles.
        Element edgeEgDataElem = doc.createElement("data");
        edgeElem.appendChild(edgeEgDataElem);
        edgeEgDataElem.setAttribute("key", "eg");

        Element edgePleElem = doc.createElement("y:PolyLineEdge");
        edgeEgDataElem.appendChild(edgePleElem);

        Element edgeArrElem = doc.createElement("y:Arrows");
        edgePleElem.appendChild(edgeArrElem);
        edgeArrElem.setAttribute("source", "none");
        edgeArrElem.setAttribute("target", "arrow");

        Element edgeBsElem = doc.createElement("y:BendStyle");
        edgePleElem.appendChild(edgeBsElem);
        edgeBsElem.setAttribute("smoothed", "true");

        // Initialize label texts. Optimized for pure event-based models.
        List<String> texts = listc(1);
        CifPrettyPrinter pprinter = new CifPrettyPrinter(null);
        List<Expression> guards = edge.getGuards();
        List<Update> updates = edge.getUpdates();

        // Add events label text.
        List<EdgeEvent> edgeEvents = edge.getEvents();
        if (edgeEvents.isEmpty()) {
            if (guards.isEmpty() && !edge.isUrgent() && updates.isEmpty()) {
                texts.add("tau");
            }
        } else {
            texts.add(pprinter.pprintEdgeEvents(edgeEvents));
        }

        // Add guards label text.
        if (filters.contains(ModelFilter.GUARDS)) {
            if (!guards.isEmpty()) {
                texts.add("when " + pprinter.pprint(guards, ", "));
            }
        }

        // Add urgency label text.
        if (filters.contains(ModelFilter.GUARDS)) {
            if (edge.isUrgent()) {
                texts.add("now");
            }
        }

        // Add updates label text.
        if (filters.contains(ModelFilter.UPDATES)) {
            if (!updates.isEmpty()) {
                texts.add("do " + pprinter.pprintUpdates(updates));
            }
        }

        // Get full label text.
        String text = StringUtils.join(texts, "\n");

        // Add edge label.
        Element edgeLblElem = doc.createElement("y:EdgeLabel");
        edgePleElem.appendChild(edgeLblElem);
        edgeLblElem.setAttribute("alignment", "center");
        addEdgeLabelBackground(edgeLblElem);

        String label = highlight(text);
        edgeLblElem.setTextContent(label);
    }

    /**
     * Returns the unique diagram node id to use for a CIF object.
     *
     * @param obj The CIF object. Must be a {@link CifTextUtils#getName named object}.
     * @return The unique diagram node id.
     */
    private static String getId(PositionObject obj) {
        String name = CifTextUtils.getAbsName(obj, false);
        return "cif:" + name.replace('.', ':');
    }
}
