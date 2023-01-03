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

package org.eclipse.escet.cif.cif2mcrl2;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Sets.setc;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.escet.cif.cif2mcrl2.options.GenerateValueActionsOption;
import org.eclipse.escet.cif.cif2mcrl2.storage.AutomatonData;
import org.eclipse.escet.cif.cif2mcrl2.storage.VariableData;
import org.eclipse.escet.cif.common.CifEnumLiteral;
import org.eclipse.escet.cif.common.CifEvalException;
import org.eclipse.escet.cif.common.CifEvalUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.CifValueUtils;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.EnumType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.common.java.Assert;

/**
 * Inspector for extracting CIF elements from the specification and collecting them in {@link #varDatas} and
 * {@link #autDatas}.
 */
public class AutomatonExtractor {
    /** Found variables in the specification. */
    private List<VariableData> varDatas = null;

    /** Found automata in the specification. */
    private List<AutomatonData> autDatas = null;

    /**
     * Extract the automata and variables from the specification.
     *
     * @param spec Specification to search.
     * @param names Mapping of CIF elements to unique mCRL2 names.
     */
    public void findElements(Specification spec, NameMaps names) {
        varDatas = list();
        unfoldForVariables(spec, names); // Collect variables from the specification.

        // Set the 'has value action' flag for each variable.
        Set<String> varNames = setc(varDatas.size());
        for (VariableData vd: varDatas) {
            varNames.add(vd.name);
        }
        Set<String> valueNames = GenerateValueActionsOption.matchNames(varNames);
        for (VariableData vd: varDatas) {
            vd.setValueAction(valueNames.contains(vd.name));
        }

        // Construct reverse map of the collected variables for adding them to their automaton.
        Map<DiscVariable, VariableData> variableMap = map();
        for (VariableData vd: varDatas) {
            variableMap.put(vd.variable, vd);
        }

        autDatas = list();
        unfoldForAutomata(spec, variableMap);
    }

    /**
     * Unfold and extract variables from a group.
     *
     * @param group Group to search.
     * @param names Mapping of CIF elements to unique mCRL2 names.
     */
    private void unfoldForVariables(Group group, NameMaps names) {
        for (Component comp: group.getComponents()) {
            if (comp instanceof Automaton) {
                Automaton aut = (Automaton)comp;
                getVariableElements(aut, names);
                continue;
            }
            Assert.check(comp instanceof Group);
            Group g = (Group)comp;
            unfoldForVariables(g, names);
        }
    }

    /**
     * Extract the variable elements from the automaton.
     *
     * @param aut Automaton to inspect.
     * @param names Mapping of CIF elements to unique mCRL2 names.
     */
    private void getVariableElements(Automaton aut, NameMaps names) {
        for (Declaration decl: aut.getDeclarations()) {
            if (decl instanceof DiscVariable) {
                DiscVariable dv = (DiscVariable)decl;
                CifType tp = CifTypeUtils.normalizeType(dv.getType());
                String initialValue = null;
                try {
                    Object val = CifEvalUtils.eval(dv.getValue().getValues().get(0), true);
                    if (tp instanceof BoolType) {
                        initialValue = ((Boolean)val) ? "true" : "false";
                    } else if (tp instanceof IntType) {
                        initialValue = Integer.toString((int)val);
                    } else if (tp instanceof EnumType) {
                        initialValue = names.getEnumLitName(((CifEnumLiteral)val).literal);
                    } else {
                        throw new RuntimeException("Unexpected type: " + tp);
                    }
                } catch (CifEvalException e) {
                    Assert.fail("Unexpected eval failure");
                }
                varDatas.add(new VariableData(dv, initialValue));
            }
        }
    }

    /**
     * Unfold and extract variables from a group.
     *
     * @param group Group to search.
     * @param variableMap Mapping of variables in the meta model to their representation in the translation.
     */
    private void unfoldForAutomata(Group group, Map<DiscVariable, VariableData> variableMap) {
        for (Component comp: group.getComponents()) {
            if (comp instanceof Automaton) {
                Automaton aut = (Automaton)comp;
                getAutomatonElements(aut, variableMap);
                continue;
            }
            Assert.check(comp instanceof Group);
            Group g = (Group)comp;
            unfoldForAutomata(g, variableMap);
        }
    }

    /**
     * Extract elements from the automaton, and add them to the global collections.
     *
     * @param aut Automaton to inspect.
     * @param variableMap Mapping of variables in the meta model to their representation in the translation.
     */
    private void getAutomatonElements(Automaton aut, Map<DiscVariable, VariableData> variableMap) {
        Location initialLocation = null;
        for (Location loc: aut.getLocations()) {
            if (!loc.getInitials().isEmpty() && CifValueUtils.isTriviallyTrue(loc.getInitials(), true, true)) {
                initialLocation = loc;
                break;
            }
        }
        AutomatonData ad = new AutomatonData(aut, initialLocation);
        ad.addAutomatonVars(variableMap);

        autDatas.add(ad);
    }

    /**
     * Get the found automata of the specification.
     *
     * @return Found automata of the specification.
     */
    public List<AutomatonData> getAutDatas() {
        return autDatas;
    }

    /**
     * Retrieve how many automata use each variable.
     *
     * @return Mapping of each variable to the number of automata that use it.
     */
    private Map<VariableData, Integer> getVarCounts() {
        Map<VariableData, Integer> varCounts = map();
        for (AutomatonData ad: autDatas) {
            for (VariableData vd: ad.vars.values()) {
                Integer c = varCounts.get(vd);
                varCounts.put(vd, (c == null) ? 1 : c + 1);
            }
        }
        return varCounts;
    }

    /**
     * Get the set of variables that are used in exactly one automaton.
     *
     * @return The set of variables that are used in exactly one automaton.
     */
    public Set<VariableData> getSingleUseVariables() {
        Set<VariableData> vds = set();
        for (Entry<VariableData, Integer> entry: getVarCounts().entrySet()) {
            if (entry.getValue() == 1) {
                vds.add(entry.getKey());
            }
        }
        return vds;
    }

    /**
     * Get the set of variables that are used in more than one automaton.
     *
     * @return The set of variables that are used in more than one automaton.
     */
    public Set<VariableData> getSharedVariables() {
        Set<VariableData> vds = set();
        for (Entry<VariableData, Integer> entry: getVarCounts().entrySet()) {
            if (entry.getValue() > 1) {
                vds.add(entry.getKey());
            }
        }
        return vds;
    }
}
