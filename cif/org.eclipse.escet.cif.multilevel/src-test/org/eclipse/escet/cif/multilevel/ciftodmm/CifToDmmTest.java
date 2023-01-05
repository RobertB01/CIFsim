//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.multilevel.ciftodmm;

import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newAutomaton;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBoolExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBoolType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newDiscVariable;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newDiscVariableExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEdge;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEdgeEvent;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEvent;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEventExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newInputVariable;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newInputVariableExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newInvariant;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newLocation;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newLocationExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newSpecification;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.list;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.eclipse.escet.cif.metamodel.cif.InvKind;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.SupKind;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.junit.Before;
import org.junit.Test;

/** Tests for the CIF to DMM conversion. */
public class CifToDmmTest {
    /** Storage of the constructed test specification. */
    private Specification spec;

    @SuppressWarnings("javadoc")
    @Before
    public void setup() {
        spec = newSpecification();
    }

    /**
     * Construct a plant automaton.
     *
     * @param name Name of the new automaton.
     * @return The created plant automaton.
     */
    private Automaton makeAddPlantAut(String name) {
        Automaton aut = newAutomaton(null, null, null, null, null, null, SupKind.PLANT, null, null, null, name, null);
        spec.getComponents().add(aut);
        return aut;
    }

    /**
     * Construct a requirement automaton.
     *
     * @param name Name of the new automaton.
     * @return The created requirement automaton.
     */
    private Automaton makeAddRequirementAut(String name) {
        Automaton aut = newAutomaton(null, null, null, null, null, null, SupKind.REQUIREMENT, null, null, null, name,
                null);
        spec.getComponents().add(aut);
        return aut;
    }

    /**
     * Add a location to an automaton.
     *
     * @param aut Automaton to use.
     * @param name Name of the new location.
     * @return The created location.
     */
    private Location addLocation(Automaton aut, String name) {
        Location loc = newLocation(null, null, null, null, null, name, null, null);
        aut.getLocations().add(loc);
        return loc;
    }

    /**
     * Construct a new edge in the automaton.
     *
     * @param aut Automaton to use.
     * @param event Event to use at the edge.
     * @return The created edge.
     */
    private Edge addEdge(Automaton aut, Event event) {
        if (aut.getLocations().isEmpty()) {
            addLocation(aut, "addedLocation");
        }

        EdgeEvent ee = newEdgeEvent(newEventExpression(event, null, newBoolType()), null);
        Edge e = newEdge(list(ee), null, null, null, null, null);
        aut.getLocations().get(0).getEdges().add(e);
        return e;
    }

    /**
     * Use a disc variable or an input variable as guard in an edge.
     *
     * @param edge Edge to use.
     * @param decl Variable to add.
     */
    private void useDeclInEdge(Edge edge, Declaration decl) {
        if (decl instanceof DiscVariable) {
            DiscVariable dv = (DiscVariable)decl;
            edge.getGuards().add(newDiscVariableExpression(null, newBoolType(), dv));
        } else if (decl instanceof InputVariable) {
            InputVariable iv = (InputVariable)decl;
            edge.getGuards().add(newInputVariableExpression(null, newBoolType(), iv));
        } else {
            throw new AssertionError("Unexpected declaration.");
        }
    }

    /**
     * Use a location as guard in an edge.
     *
     * @param edge Edge to use
     * @param useLoc Location to reference as guard.
     */
    private void useLocInEdge(Edge edge, Location useLoc) {
        edge.getGuards().add(newLocationExpression(useLoc, null, newBoolType()));
    }

    /**
     * Construct an input variable.
     *
     * @param name Name of the new variable.
     * @return The created variable.
     */
    private InputVariable makeInputVar(String name) {
        return newInputVariable(name, null, newBoolType());
    }

    /**
     * Construct a discrete variable.
     *
     * @param name Name of the new variable.
     * @return The created variable.
     */
    private DiscVariable makeDiscVar(String name) {
        return newDiscVariable(name, null, newBoolType(), null);
    }

    @SuppressWarnings("javadoc")
    @Test
    public void autsAccessOtherLocationTest() {
        // controllable pevt;
        //
        // plant p:
        // location loc0:
        // edge pevt when loc1;
        // end
        //
        // requirement q:
        // controllable revt;
        // location loc1:
        // edge revt when loc0;
        // end
        Automaton plantAut = makeAddPlantAut("p");
        Automaton reqAut = makeAddRequirementAut("q");
        Location ploc = addLocation(plantAut, "loc0");
        Location rloc = addLocation(reqAut, "loc1");

        // Unused event in the root of the specification.
        Event specEvt = newEvent(true, "specEvt", null, null);
        spec.getDeclarations().add(specEvt);

        // Add events for plant and requirement edge, add the events to the other automaton.
        Event pevt = newEvent(true, "pevt", null, null);
        Event revt = newEvent(true, "revt", null, null);
        plantAut.getDeclarations().add(revt); // Define requirement event in the plant.
        reqAut.getDeclarations().add(pevt); // Define plant event in the requirement.

        // Construct an edge in each automaton, using the location of the other automaton as guard.
        // Use different events to avoid finding new relations.
        Edge pedge = addEdge(plantAut, pevt);
        Edge redge = addEdge(reqAut, revt);
        useLocInEdge(pedge, rloc);
        useLocInEdge(redge, ploc);

        RelationsCollector collector = new RelationsCollector();
        collector.collect(spec);

        int plantIndex = collector.getIndex(plantAut);
        int reqIndex = collector.getIndex(reqAut);
        int plocIndex = collector.getIndex(ploc);
        int rlocIndex = collector.getIndex(rloc);
        int pevtIndex = collector.getIndex(pevt);
        int revtIndex = collector.getIndex(revt);

        assertTrue(collector.isPlantElement(plantIndex));
        assertTrue(collector.isRequirementElement(reqIndex));

        OwnedAndAccessedElements prels = collector.getGroupRelations(plantIndex);
        OwnedAndAccessedElements rrels = collector.getGroupRelations(reqIndex);
        assertEquals(2, prels.accessedElements.cardinality());
        assertTrue(prels.accessedElements.get(rlocIndex)); // Plant accesses req-location.
        assertTrue(prels.accessedElements.get(pevtIndex)); // Plant accesses plant event.
        assertEquals(2, rrels.accessedElements.cardinality());
        assertTrue(rrels.accessedElements.get(plocIndex)); // Requirement accesses plant-location.
        assertTrue(rrels.accessedElements.get(revtIndex)); // Requirement accesses requirement event.

        assertEquals(1, prels.ownedElements.cardinality());
        assertEquals(1, rrels.ownedElements.cardinality());
        assertTrue(prels.ownedElements.get(plocIndex)); // Plant owns plant location.
        assertTrue(rrels.ownedElements.get(rlocIndex)); // Requirement owns requirement location.
    }

    @SuppressWarnings("javadoc")
    @Test
    public void inputVarIsPlant() {
        // input bool pinput;
        InputVariable pinput = makeInputVar("pinput");
        spec.getDeclarations().add(pinput);

        RelationsCollector collector = new RelationsCollector();
        collector.collect(spec);
        int inputIndex = collector.getIndex(pinput);

        assertTrue(collector.isPlantElement(inputIndex));

        OwnedAndAccessedElements irels = collector.getGroupRelations(inputIndex);
        assertEquals(1, irels.accessedElements.cardinality());
        assertTrue(irels.accessedElements.get(inputIndex)); // Input variable accesses itself.
        assertEquals(0, irels.ownedElements.cardinality());
    }

    @SuppressWarnings("javadoc")
    @Test
    public void discvarOwnerTest() {
        // plant p:
        // disc bool pdisc;
        // input bool pinput;
        // end
        //
        // requirement r:
        // disc bool rdisc;
        // input bool rinput;
        // end
        Automaton plantAut = makeAddPlantAut("p");
        Automaton reqAut = makeAddRequirementAut("r");
        DiscVariable pdisc = makeDiscVar("pdisc");
        DiscVariable rdisc = makeDiscVar("rdisc");
        InputVariable pinput = makeInputVar("pinput");
        InputVariable rinput = makeInputVar("rinput");

        plantAut.getDeclarations().add(pdisc);
        plantAut.getDeclarations().add(pinput);
        reqAut.getDeclarations().add(rdisc);
        reqAut.getDeclarations().add(rinput);

        RelationsCollector collector = new RelationsCollector();
        collector.collect(spec);

        int plantAutIndex = collector.getIndex(plantAut);
        int reqAutIndex = collector.getIndex(reqAut);
        int pdiscIndex = collector.getIndex(pdisc);
        int rdiscIndex = collector.getIndex(rdisc);
        int pinputIndex = collector.getIndex(pinput);
        int rinputIndex = collector.getIndex(rinput);

        assertTrue(collector.isPlantElement(plantAutIndex));
        assertTrue(collector.isRequirementElement(reqAutIndex));
        assertTrue(collector.isPlantElement(pinputIndex));
        assertTrue(collector.isPlantElement(rinputIndex));

        OwnedAndAccessedElements plantAutRels = collector.getGroupRelations(plantAutIndex);
        assertEquals(0, plantAutRels.accessedElements.cardinality());
        assertEquals(1, plantAutRels.ownedElements.cardinality());
        assertTrue(plantAutRels.ownedElements.get(pdiscIndex)); // Discvar in the plant is owned by it.

        OwnedAndAccessedElements reqAutRels = collector.getGroupRelations(reqAutIndex);
        assertEquals(0, reqAutRels.accessedElements.cardinality());
        assertEquals(1, reqAutRels.ownedElements.cardinality());
        assertTrue(reqAutRels.ownedElements.get(rdiscIndex)); // Discvar in the requirement is owned by it.

        OwnedAndAccessedElements pinputRels = collector.getGroupRelations(pinputIndex);
        assertEquals(1, pinputRels.accessedElements.cardinality()); // pinput accesses itself.
        assertEquals(0, pinputRels.ownedElements.cardinality());

        OwnedAndAccessedElements rinputRels = collector.getGroupRelations(rinputIndex);
        assertEquals(1, rinputRels.accessedElements.cardinality()); // rinput accesses itself.
        assertEquals(0, rinputRels.ownedElements.cardinality());
    }

    @SuppressWarnings("javadoc")
    @Test
    public void unrelatedPlantsReqsNotGroupedTest() {
        Automaton plantAut = makeAddPlantAut("p");
        Automaton reqAut = makeAddRequirementAut("r");
        InputVariable plantInput = makeInputVar("pinput");
        Event evt = newEvent(true, "evt", null, null);
        Invariant reqInv = newInvariant(newEventExpression(evt, null, null), InvKind.EVENT_DISABLES, "reqInv", null,
                newBoolExpression(null, newBoolType(), true), SupKind.REQUIREMENT);

        spec.getDeclarations().add(plantInput);
        spec.getDeclarations().add(evt);
        spec.getInvariants().add(reqInv);

        RelationsCollector collector = new RelationsCollector();
        collector.collect(spec);
        int plantAutIndex = collector.getIndex(plantAut);
        int reqAutIndex = collector.getIndex(reqAut);
        int plantInputIndex = collector.getIndex(plantInput);
        int reqInvIndex = collector.getIndex(reqInv);

        assertTrue(collector.isPlantElement(plantAutIndex));
        assertTrue(collector.isPlantElement(plantInputIndex));
        assertTrue(collector.isRequirementElement(reqAutIndex));
        assertTrue(collector.isRequirementElement(reqInvIndex));

        List<OwnedAndAccessedElements> plantGroups = collector.computePlantGroups();
        List<OwnedAndAccessedElements> reqGroups = collector.computeRequirementGroups();
        assertEquals(2, plantGroups.size());
        assertEquals(2, reqGroups.size());
    }

    @SuppressWarnings("javadoc")
    @Test
    public void mergePlantAutsOnEventTest() {
        Automaton plantAut1 = makeAddPlantAut("p1");
        Automaton plantAut2 = makeAddPlantAut("p2");
        Event evt = newEvent(true, "evt", null, null);
        spec.getDeclarations().add(evt);
        addEdge(plantAut1, evt);
        addEdge(plantAut2, evt);

        RelationsCollector collector = new RelationsCollector();
        collector.collect(spec);
        List<OwnedAndAccessedElements> plantGroups = collector.computePlantGroups();
        assertEquals(1, plantGroups.size());
        assertEquals(2, first(plantGroups).groupElements.cardinality());
    }

    @SuppressWarnings("javadoc")
    @Test
    public void mergePlantAutAndInputVarTest() {
        InputVariable inpVar = makeInputVar("inputvar");
        spec.getDeclarations().add(inpVar);

        Event evt = newEvent(true, "evt", null, null);
        spec.getDeclarations().add(evt);

        Automaton plantAut1 = makeAddPlantAut("p1");
        Edge edge = addEdge(plantAut1, evt);
        useDeclInEdge(edge, inpVar);

        RelationsCollector collector = new RelationsCollector();
        collector.collect(spec);
        List<OwnedAndAccessedElements> plantGroups = collector.computePlantGroups();
        assertEquals(1, plantGroups.size());
        assertEquals(2, first(plantGroups).groupElements.cardinality());
    }

    @SuppressWarnings("javadoc")
    @Test
    public void mergePlantAutsOnAccessedDiscVarTest() {
        // Requirement automaton with a discvar.
        Automaton reqAut = makeAddRequirementAut("req");
        DiscVariable discVar = makeDiscVar("discvar");
        reqAut.getDeclarations().add(discVar);

        // 2 plants with different events accessing the discvar.
        Automaton plantAut1 = makeAddPlantAut("p1");
        Automaton plantAut2 = makeAddPlantAut("p2");
        Event evt1 = newEvent(true, "evt1", null, null);
        Event evt2 = newEvent(true, "evt2", null, null);
        spec.getDeclarations().add(evt1);
        spec.getDeclarations().add(evt2);

        Edge edge1 = addEdge(plantAut1, evt1);
        Edge edge2 = addEdge(plantAut2, evt2);
        useDeclInEdge(edge1, discVar);
        useDeclInEdge(edge2, discVar);

        RelationsCollector collector = new RelationsCollector();
        collector.collect(spec);
        int plant1Index = collector.getIndex(plantAut1);
        int plant2Index = collector.getIndex(plantAut2);

        List<OwnedAndAccessedElements> plantGroups = collector.computePlantGroups();
        assertEquals(1, plantGroups.size());
        assertEquals(2, first(plantGroups).groupElements.cardinality());
        assertTrue(first(plantGroups).groupElements.get(plant1Index));
        assertTrue(first(plantGroups).groupElements.get(plant2Index));
    }

    @SuppressWarnings("javadoc")
    @Test
    public void mergePlantAutsOnOwnedAndAccessedDiscVarTest() {
        DiscVariable discVar = makeDiscVar("discvar");

        // Plant1 owns but not accesses the variable.
        Automaton plantAut1 = makeAddPlantAut("p1");
        plantAut1.getDeclarations().add(discVar);

        // Plant2 accesses the variable.
        Automaton plantAut2 = makeAddPlantAut("p2");
        Event evt2 = newEvent(true, "evt2", null, null);
        spec.getDeclarations().add(evt2);

        Edge edge2 = addEdge(plantAut2, evt2);
        useDeclInEdge(edge2, discVar);

        RelationsCollector collector = new RelationsCollector();
        collector.collect(spec);
        int plant1Index = collector.getIndex(plantAut1);
        int plant2Index = collector.getIndex(plantAut2);

        List<OwnedAndAccessedElements> plantGroups = collector.computePlantGroups();
        assertEquals(1, plantGroups.size());
        assertEquals(2, first(plantGroups).groupElements.cardinality());
        assertTrue(first(plantGroups).groupElements.get(plant1Index));
        assertTrue(first(plantGroups).groupElements.get(plant2Index));
    }

    @SuppressWarnings("javadoc")
    @Test
    public void mergePlantAutsOnAccessedLocationsTest() {
        // requirement r1:
        // location rloc;
        // end
        //
        // controllable evt1;
        // controllable evt21;
        //
        // plant p1:
        // location loc1;
        // edge evt1 when rloc;
        // end
        //
        // plant p2:
        // location loc2:
        // edge evt2 when rloc;
        // end
        Automaton reqAut = makeAddRequirementAut("r1");
        Location rloc = addLocation(reqAut, "rloc");
        spec.getComponents().add(reqAut);

        Event evt1 = newEvent(true, "evt1", null, null);
        Event evt2 = newEvent(true, "evt2", null, null);
        spec.getDeclarations().add(evt2);
        spec.getDeclarations().add(evt1);

        Automaton plantAut1 = makeAddPlantAut("p1");
        addLocation(plantAut1, "loc1");
        spec.getComponents().add(plantAut1);
        Edge edge1 = addEdge(plantAut1, evt1);
        useLocInEdge(edge1, rloc);

        Automaton plantAut2 = makeAddPlantAut("p2");
        addLocation(plantAut2, "loc2");
        spec.getComponents().add(plantAut2);
        Edge edge2 = addEdge(plantAut2, evt2);
        useLocInEdge(edge2, rloc);

        RelationsCollector collector = new RelationsCollector();
        collector.collect(spec);
        List<OwnedAndAccessedElements> plantGroups = collector.computePlantGroups();
        assertEquals(1, plantGroups.size());
        assertEquals(2, first(plantGroups).groupElements.cardinality());
    }

    @SuppressWarnings("javadoc")
    @Test
    public void mergePlantAutsOnOwnedAndAccessedLocationTest() {
        // plant p1:
        // location loc1;
        // end
        //
        // controllable evt;
        //
        // plant p2:
        // location loc2:
        // edge evt when loc1;
        // end
        Automaton plantAut1 = makeAddPlantAut("p1");
        Location loc1 = addLocation(plantAut1, "loc1");
        spec.getComponents().add(plantAut1);

        Event evt2 = newEvent(true, "evt2", null, null);
        spec.getDeclarations().add(evt2);

        Automaton plantAut2 = makeAddPlantAut("p2");
        addLocation(plantAut2, "loc2");
        spec.getComponents().add(plantAut2);
        Edge edge2 = addEdge(plantAut2, evt2);
        useLocInEdge(edge2, loc1);

        RelationsCollector collector = new RelationsCollector();
        collector.collect(spec);
        List<OwnedAndAccessedElements> plantGroups = collector.computePlantGroups();
        assertEquals(1, plantGroups.size());
        assertEquals(2, first(plantGroups).groupElements.cardinality());
        assertTrue(first(plantGroups).groupElements.get(collector.getIndex(plantAut1)));
        assertTrue(first(plantGroups).groupElements.get(collector.getIndex(plantAut2)));
    }

    @SuppressWarnings("javadoc")
    @Test
    public void mergeReqsOnEventNotUsedByPlant() {
        // controllable evt;
        //
        // requirement r1:
        // location loc1:
        // edge evt;
        // end
        //
        // requirement r2:
        // location loc2:
        // edge evt;
        // end
        Event evt = newEvent(true, "evt", null, null);
        spec.getDeclarations().add(evt);

        Automaton req1 = makeAddRequirementAut("req1");
        spec.getComponents().add(req1);
        addEdge(req1, evt);

        Automaton req2 = makeAddRequirementAut("req2");
        spec.getComponents().add(req2);
        addEdge(req2, evt);

        RelationsCollector collector = new RelationsCollector();
        collector.collect(spec);

        List<OwnedAndAccessedElements> reqGroups = collector.computeRequirementGroups();
        assertEquals(1, reqGroups.size());
        assertEquals(2, first(reqGroups).groupElements.cardinality());
        assertTrue(first(reqGroups).groupElements.get(collector.getIndex(req1)));
        assertTrue(first(reqGroups).groupElements.get(collector.getIndex(req2)));
    }

    @SuppressWarnings("javadoc")
    @Test
    public void notMergeReqsOnEventUsedByPlant() {
        // controllable evt;
        //
        // plant p:
        // location ploc:
        // edge evt;
        // end
        //
        // requirement r1:
        // location loc1:
        // edge evt;
        // end
        //
        // requirement r2:
        // location loc2:
        // edge evt;
        // end
        Event evt = newEvent(true, "evt", null, null);
        spec.getDeclarations().add(evt);

        Automaton plant = makeAddPlantAut("plant");
        spec.getComponents().add(plant);
        addEdge(plant, evt);

        Automaton req1 = makeAddRequirementAut("req1");
        spec.getComponents().add(req1);
        addEdge(req1, evt);

        Automaton req2 = makeAddRequirementAut("req2");
        spec.getComponents().add(req2);
        addEdge(req2, evt);

        RelationsCollector collector = new RelationsCollector();
        collector.collect(spec);
        List<OwnedAndAccessedElements> reqGroups = collector.computeRequirementGroups();
        assertEquals(2, reqGroups.size());
    }

    @SuppressWarnings("javadoc")
    @Test
    public void notMergeReqsOnInputVar() {
        // controllable evt1;
        // controllable evt2;
        //
        // input bool inp;
        //
        // requirement r1:
        // location loc1:
        // edge evt1 when inp;
        // end
        //
        // requirement r2:
        // location loc2:
        // edge evt2 when inp;
        // end
        Event evt1 = newEvent(true, "evt1", null, null);
        Event evt2 = newEvent(true, "evt2", null, null);
        spec.getDeclarations().add(evt1);
        spec.getDeclarations().add(evt2);

        InputVariable inp = makeInputVar("inp");
        spec.getDeclarations().add(inp);

        Automaton req1 = makeAddRequirementAut("req1");
        spec.getComponents().add(req1);
        useDeclInEdge(addEdge(req1, evt1), inp);

        Automaton req2 = makeAddRequirementAut("req2");
        spec.getComponents().add(req2);
        useDeclInEdge(addEdge(req2, evt2), inp);

        RelationsCollector collector = new RelationsCollector();
        collector.collect(spec);
        List<OwnedAndAccessedElements> reqGroups = collector.computeRequirementGroups();
        assertEquals(2, reqGroups.size());
    }

    @SuppressWarnings("javadoc")
    @Test
    public void notMergeReqsOnAccessDiscVar() {
        // controllable evt1;
        // controllable evt2;
        //
        // plant p:
        // disc bool dvar;
        // end
        //
        // requirement r1:
        // location loc1:
        // edge evt1 when dvar;
        // end
        //
        // requirement r2:
        // location loc2:
        // edge evt2 when dvar;
        // end
        Event evt1 = newEvent(true, "evt1", null, null);
        Event evt2 = newEvent(true, "evt2", null, null);
        spec.getDeclarations().add(evt1);
        spec.getDeclarations().add(evt2);

        Automaton plant = makeAddPlantAut("p");
        DiscVariable dvar = makeDiscVar("dvar");
        plant.getDeclarations().add(dvar);

        Automaton req1 = makeAddRequirementAut("req1");
        spec.getComponents().add(req1);
        useDeclInEdge(addEdge(req1, evt1), dvar);

        Automaton req2 = makeAddRequirementAut("req2");
        spec.getComponents().add(req2);
        useDeclInEdge(addEdge(req2, evt2), dvar);

        RelationsCollector collector = new RelationsCollector();
        collector.collect(spec);
        List<OwnedAndAccessedElements> reqGroups = collector.computeRequirementGroups();
        assertEquals(2, reqGroups.size());
    }

    @SuppressWarnings("javadoc")
    @Test
    public void mergeReqsOnOwnedAndAccessedDiscVar() {
        // controllable evt1;
        // controllable evt2;
        //
        // requirement r1:
        // disc bool dvar;
        // location loc1:
        // edge evt1 when dvar;
        // end
        //
        // requirement r2:
        // location loc2:
        // edge evt2 when dvar;
        // end
        Event evt1 = newEvent(true, "evt1", null, null);
        Event evt2 = newEvent(true, "evt2", null, null);
        spec.getDeclarations().add(evt1);
        spec.getDeclarations().add(evt2);

        DiscVariable dvar = makeDiscVar("dvar");

        Automaton req1 = makeAddRequirementAut("req1");
        req1.getDeclarations().add(dvar);
        spec.getComponents().add(req1);
        useDeclInEdge(addEdge(req1, evt1), dvar);

        Automaton req2 = makeAddRequirementAut("req2");
        spec.getComponents().add(req2);
        useDeclInEdge(addEdge(req2, evt2), dvar);

        RelationsCollector collector = new RelationsCollector();
        collector.collect(spec);

        List<OwnedAndAccessedElements> reqGroups = collector.computeRequirementGroups();
        assertEquals(1, reqGroups.size());
        assertEquals(2, first(reqGroups).groupElements.cardinality());
        assertTrue(first(reqGroups).groupElements.get(collector.getIndex(req1)));
        assertTrue(first(reqGroups).groupElements.get(collector.getIndex(req2)));
    }

    @SuppressWarnings("javadoc")
    @Test
    public void notMergeReqsOnAccessLocation() {
        // controllable evt1;
        // controllable evt2;
        //
        // plant p:
        // location ploc;
        // end
        //
        // requirement r1:
        // location loc1:
        // edge evt1 when ploc;
        // end
        //
        // requirement r2:
        // location loc2:
        // edge evt2 when ploc;
        // end
        Event evt1 = newEvent(true, "evt1", null, null);
        Event evt2 = newEvent(true, "evt2", null, null);
        spec.getDeclarations().add(evt1);
        spec.getDeclarations().add(evt2);

        Automaton plant = makeAddPlantAut("p");
        Location ploc = addLocation(plant, "ploc");

        Automaton req1 = makeAddRequirementAut("req1");
        spec.getComponents().add(req1);
        useLocInEdge(addEdge(req1, evt1), ploc);

        Automaton req2 = makeAddRequirementAut("req2");
        spec.getComponents().add(req2);
        useLocInEdge(addEdge(req2, evt2), ploc);

        RelationsCollector collector = new RelationsCollector();
        collector.collect(spec);
        List<OwnedAndAccessedElements> reqGroups = collector.computeRequirementGroups();
        assertEquals(2, reqGroups.size());
    }

    @SuppressWarnings("javadoc")
    @Test
    public void mergeReqsOnOwnedAndAccessedLocation() {
        // controllable evt2;
        //
        // requirement r1:
        // location loc1:
        // end
        //
        // requirement r2: loc1 disables evt2;
        Event evt2 = newEvent(true, "evt2", null, null);
        spec.getDeclarations().add(evt2);

        Automaton req1 = makeAddRequirementAut("req1");
        Location loc1 = addLocation(req1, "loc1");
        spec.getComponents().add(req1);

        Invariant reqInv = newInvariant(newEventExpression(evt2, null, null), InvKind.EVENT_DISABLES, "reqInv", null,
                newLocationExpression(loc1, null, newBoolType()), SupKind.REQUIREMENT);
        spec.getInvariants().add(reqInv);

        RelationsCollector collector = new RelationsCollector();
        collector.collect(spec);
        List<OwnedAndAccessedElements> reqGroups = collector.computeRequirementGroups();
        assertEquals(1, reqGroups.size());
    }
}
