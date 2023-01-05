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

package org.eclipse.escet.cif.multilevel.ciftodmm;

import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.common.dsm.Label;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** DMM labels representing CIF elements. */
public class Labels {
    /** String prefix for requirement group names. */
    private static final String REQUIREMENT_GROUP_PREFIX = "RG";

    /** String prefix for plant group names. */
    private static final String PLANT_GROUP_PREFIX = "PG";

    /** Constructor of the static {@link Labels} class. */
    private Labels() {
        // Static class.
    }

    /**
     * Construct a label for plant group {@code n}.
     *
     * @param n Number of the plant group to label.
     * @return The generated label.
     */
    public static Label makePlantGroupLabel(int n) {
        return new Label(PLANT_GROUP_PREFIX + Integer.toString(n + 1));
    }

    /**
     * Construct a label for requirement group {@code n}.
     *
     * @param n Number of the requirement group to label.
     * @return The generated label.
     */
    public static Label makeRequirementGroupLabel(int n) {
        return new Label(REQUIREMENT_GROUP_PREFIX + Integer.toString(n + 1));
    }

    /**
     * Wrap a label around an element in the CIF relations.
     *
     * @param element Element to wrap.
     * @return The constructed label.
     */
    public static Label makeLabel(PositionObject element) {
        if (element instanceof Automaton aut) {
            return new AutomatonLabel(aut);
        }
        if (element instanceof Invariant inv) {
            return new InvariantLabel(inv);
        }
        if (element instanceof InputVariable inpVar) {
            return new InputVarLabel(inpVar);
        }
        if (element instanceof DiscVariable discVar) {
            return new DiscVarLabel(discVar);
        }
        if (element instanceof Event event) {
            return new EventLabel(event);
        }
        if (element instanceof Location loc) {
            return new LocationLabel(loc);
        }
        throw new AssertionError("Unexpected position object class encountered.");
    }

    /** DMM label holding a CIF automaton. */
    public static class AutomatonLabel extends Label {
        /** Automaton stored in the label. */
        public final Automaton automaton;

        /**
         * Constructor of the {@link AutomatonLabel} class.
         *
         * @param automaton Automaton stored in the label.
         */
        public AutomatonLabel(Automaton automaton) {
            super(CifTextUtils.getAbsName(automaton, false));
            this.automaton = automaton;
        }
    }

    /** DMM label holding a CIF invariant. */
    public static class InvariantLabel extends Label {
        /** Invariant stored in the label. */
        public final Invariant invariant;

        /**
         * Constructor of the {@link InvariantLabel} class.
         *
         * @param invariant Invariant stored in the label.
         */
        public InvariantLabel(Invariant invariant) {
            super((invariant.getName() == null) ? CifTextUtils.invToStr(invariant, true)
                    : CifTextUtils.getAbsName(invariant, false));
            this.invariant = invariant;
        }
    }

    /** DMM label holding a CIF input variable. */
    public static class InputVarLabel extends Label {
        /** Input variable stored in the label. */
        public final InputVariable inputVar;

        /**
         * Constructor of the {@link InputVarLabel} class.
         *
         * @param inputVar Input variable stored in the label.
         */
        public InputVarLabel(InputVariable inputVar) {
            super(CifTextUtils.getAbsName(inputVar, false));
            this.inputVar = inputVar;
        }
    }

    /** DMM label holding a CIF discrete variable. */
    public static class DiscVarLabel extends Label {
        /** Discrete variable stored in the label. */
        public final DiscVariable discVar;

        /**
         * Constructor of the {@link DiscVarLabel} class.
         *
         * @param discVar Discrete variable stored in the label.
         */
        public DiscVarLabel(DiscVariable discVar) {
            super(CifTextUtils.getAbsName(discVar, false));
            this.discVar = discVar;
        }
    }

    /** DMM label holding a CIF event. */
    public static class EventLabel extends Label {
        /** Event stored in the label. */
        public final Event event;

        /**
         * Constructor of the {@link EventLabel} class.
         *
         * @param event Event stored in the label.
         */
        public EventLabel(Event event) {
            super(CifTextUtils.getAbsName(event, false));
            this.event = event;
        }
    }

    /** DMM label holding a CIF location. */
    public static class LocationLabel extends Label {
        /** Event stored in the label. */
        public final Location location;

        /**
         * Constructor of the {@link LocationLabel} class.
         *
         * @param location Location in the label.
         */
        public LocationLabel(Location location) {
            super(CifTextUtils.getAbsName(location, false));
            this.location = location;
        }
    }
}
