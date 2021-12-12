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

package org.eclipse.escet.cif.cif2mcrl2;

import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.set;

import java.util.Map;
import java.util.Set;

import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumLiteral;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.EnumType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;

/** Mappings of elements (variables, automata, and events) to names in mCRL2. */
public class NameMaps {
    /** Collection of handed out names, used for duplicate checking. */
    private Set<String> names;

    /** Set of words reserved by mCRL2. See also http://www.mcrl2.org/release/user_manual/language_reference/lex.html */
    private static final String[] RESERVED = {"act", "allow", "block", "comm", "cons", "delay", "div", "end", "eqn",
            "exists", "forall", "glob", "hide", "if", "in", "init", "lambda", "map", "mod", "mu", "nu", "pbes", "proc",
            "rename", "sort", "struct", "sum", "val", "var", "whr", "yaled", "Bag", "Bool", "Int", "List", "Nat", "Pos",
            "Real", "Set", "delta", "false", "nil", "tau", "true"};

    /** Constructor of the {@link NameMaps} class. */
    public NameMaps() {
        names = set();
        for (String r: RESERVED) {
            names.add(r);
        }
    }

    //
    // Overall collection of names.
    //

    /**
     * Test whether the given name is unique for all prefixes.
     *
     * @param name Name to test.
     * @param prefixes Prefixes that should all lead to a unique name.
     * @return Whether the name leads to unique prefixed names for all prefixes.
     */
    private boolean testNames(String name, String[] prefixes) {
        for (String prefix: prefixes) {
            if (names.contains(prefix + name)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Add all prefixed names.
     *
     * @param name Base name.
     * @param prefixes Collection of prefixes for the base name.
     */
    private void addNames(String name, String[] prefixes) {
        for (String prefix: prefixes) {
            names.add(prefix + name);
        }
    }

    /**
     * Add a set of unique names derived from a suggested base name.
     *
     * @param baseName Suggested basename to use.
     * @param prefixes Prefixes that should all lead to a unique name.
     * @return Basename that leads to unique names for all prefixes.
     */
    private String makeName(String baseName, String[] prefixes) {
        if (testNames(baseName, prefixes)) {
            addNames(baseName, prefixes);
            return baseName;
        }
        int i = 2;
        while (true) {
            String name = baseName + String.valueOf(i);
            if (testNames(name, prefixes)) {
                addNames(name, prefixes);
                return name;
            }
            i++;
        }
    }

    //
    // Behavior process names.
    //

    /** Prefix of behavior process. */
    private static final String AUT_EFSM = "BehProc_";

    /** Prefix of the location sort of behavior processes. */
    private static final String AUT_LOCSORT = "LocSort_";

    /** Prefix of the location variable of a behavior processes. */
    private static final String AUT_LOCVAR = "Locvar_";

    /** Prefix of the location names of behavior processes. */
    private static final String AUT_LOCATION = "loc_";

    /** All prefixes in use for behavior processes. */
    private static final String[] AUT_PREFIXES = {AUT_EFSM, AUT_LOCSORT, AUT_LOCVAR, AUT_LOCATION};

    /** Mapping of behavior processes to their unique names. */
    private Map<Automaton, String> automatonMap = map();

    /**
     * Get the base name of a behavior process.
     *
     * @param aut Automaton to name.
     * @return Basename (without prefix) of the given process.
     */
    private String getAutomaton(Automaton aut) {
        String name = automatonMap.get(aut);
        if (name != null) {
            return name;
        }
        name = makeName(aut.getName(), AUT_PREFIXES);
        automatonMap.put(aut, name);
        return name;
    }

    /**
     * Get the mCRL2 name of a behavior process.
     *
     * @param behavior Automaton to name.
     * @return Name of the behavior process.
     */
    public String getBehaviorProcess(Automaton behavior) {
        return AUT_EFSM + getAutomaton(behavior);
    }

    /**
     * Get the mCRL2 sort name of locations of a behavior process.
     *
     * @param behavior Automaton to name.
     * @return Name of the location sort of a behavior process.
     */
    public String getLocationSortName(Automaton behavior) {
        return AUT_LOCSORT + getAutomaton(behavior);
    }

    /**
     * Get the mCRL2 name of the location variable of a behavior process.
     *
     * @param behavior Automaton to name.
     * @return Name of the location variable of a behavior process.
     */
    public String getLocationVariableName(Automaton behavior) {
        return AUT_LOCVAR + getAutomaton(behavior);
    }

    /**
     * Get the mCRL2 name for a location in an process.
     *
     * @param loc Location to name.
     * @param aut Automaton containing the location.
     * @return Name of the location.
     */
    public String getLocationName(Location loc, Automaton aut) {
        String prefix = AUT_LOCATION + getAutomaton(aut);
        if (loc.getName() == null) {
            return prefix;
        }
        return prefix + "_" + loc.getName();
    }

    //
    // Variable processes.
    //

    /** Prefix of the variable process. */
    private static final String VAR_EFSM = "VarProc_";

    /** Prefix of the behavior process read action of the variable. */
    private static final String VAR_AREAD = "aread_";

    /** Prefix of the behavior process write action of the variable. */
    private static final String VAR_AWRITE = "awrite_";

    /** Prefix of the variable process read action of the variable. */
    private static final String VAR_VREAD = "vread_";

    /** Prefix of the variable process write action of the variable. */
    private static final String VAR_VWRITE = "vwrite_";

    /** Prefix of the sync action between write action of the variable and the process(es). */
    private static final String VAR_SYNC = "sync_";

    /** Prefix of the variable get-value action. */
    private static final String VAR_VALUE = "value_";

    /** Prefix of the variable name. */
    private static final String VAR_NAME = "";

    /** All prefixes in use with a variable. */
    private static final String[] VAR_PREFIXES = {VAR_EFSM, VAR_NAME, VAR_SYNC, VAR_VALUE, VAR_AREAD, VAR_AWRITE,
            VAR_VREAD, VAR_VWRITE};

    /** Mapping of variables to their unique names. */
    private Map<DiscVariable, String> variableMap = map();

    /**
     * Get the base name of a variable.
     *
     * @param var Variable to name.
     * @return Basename (without prefix) of the given variable.
     */
    private String getVariable(DiscVariable var) {
        String name = variableMap.get(var);
        if (name != null) {
            return name;
        }
        name = makeName(var.getName(), VAR_PREFIXES);
        variableMap.put(var, name);
        return name;
    }

    /**
     * Get the mCRL2 name of the variable.
     *
     * @param var Variable to name.
     * @return Name of the variable.
     */
    public String getVariableName(DiscVariable var) {
        return VAR_NAME + getVariable(var);
    }

    /**
     * Get the mCRL2 action name for reading the given variable by a behavior process.
     *
     * @param var Variable to read.
     * @return Action name to use for reading the variable.
     */
    public String getBehRead(DiscVariable var) {
        return VAR_AREAD + getVariable(var);
    }

    /**
     * Get the mCRL2 action name for writing the given variable by a behavior process.
     *
     * @param var Variable to write.
     * @return Action name to use for writing the variable.
     */
    public String getBehWrite(DiscVariable var) {
        return VAR_AWRITE + getVariable(var);
    }

    /**
     * Get the mCRL2 action name for reading the given variable by a variable process.
     *
     * @param var Variable to read.
     * @return Action name to use for reading the variable.
     */
    public String getVarRead(DiscVariable var) {
        return VAR_VREAD + getVariable(var);
    }

    /**
     * Get the mCRL2 action name for writing the given variable by a variable process.
     *
     * @param var Variable to write.
     * @return Action name to use for writing the variable.
     */
    public String getVarWrite(DiscVariable var) {
        return VAR_VWRITE + getVariable(var);
    }

    /**
     * Get the mCRL2 action name for synchronized write between the variable process and the behavior process(es).
     *
     * @param var Variable to write.
     * @return Action name to use for writing the variable.
     */
    public String getVarSync(DiscVariable var) {
        return VAR_SYNC + getVariable(var);
    }

    /**
     * Get the mCRL2 name of a variable process.
     *
     * @param var Variable to convert to process name.
     * @return Name of the variable process.
     */
    public String getVariableProcess(DiscVariable var) {
        return VAR_EFSM + getVariable(var);
    }

    /**
     * Get the mCRL2 name of a variable get-value action.
     *
     * @param var Variable to convert to process name.
     * @return Name of the variable get-value action.
     */
    public String getVariableValue(DiscVariable var) {
        return VAR_VALUE + getVariable(var);
    }

    //
    // Event names.
    //

    /** Prefix of an action denoting an event. */
    private static final String EVT_NAME = "";

    /** Prefix of an action denoting a renamed event. */
    private static final String EVT_RENAMED = "renamed_";

    /** All prefixes of events. */
    private static final String[] EVT_PREFIXES = {EVT_NAME, EVT_RENAMED};

    /** Mapping of events to their unique names. */
    private Map<Event, String> eventMap = map();

    /**
     * Get the base name of an event.
     *
     * @param evt Event to name.
     * @return Basename (without prefix) of the given event.
     */
    private String getEvent(Event evt) {
        String name = eventMap.get(evt);
        if (name != null) {
            return name;
        }
        name = makeName(evt.getName(), EVT_PREFIXES);
        eventMap.put(evt, name);
        return name;
    }

    /**
     * Get the mCRL2 name of an event.
     *
     * @param evt Event to get name of.
     * @return Name of the provided event.
     */
    public String getEventName(Event evt) {
        return EVT_NAME + getEvent(evt);
    }

    /**
     * Get the mCRL2 name of a renamed event.
     *
     * @param evt Event to get name of.
     * @return Name of the provided event when renamed.
     */
    public String getRenamedEventName(Event evt) {
        return EVT_RENAMED + getEvent(evt);
    }

    //
    // Enumeration and literal names.
    //

    /** Prefix of the names of enumeration sorts. */
    private static final String ENUM_NAME = "enum_";

    /** All prefixes in use for enumeration sorts. */
    private static final String[] ENUM_PREFIXES = {ENUM_NAME};

    /** Mapping of enumerations to their unique names. */
    private Map<EnumDecl, String> enumsMap = map();

    /** Prefix of the names of enumeration literal constructors. */
    private static final String ENUM_LIT_NAME = "enumlit_";

    /** All prefixes in use for enumeration literal constructors. */
    private static final String[] ENUM_LIT_PREFIXES = {ENUM_LIT_NAME};

    /** Mapping of enumeration literals to their unique names. */
    private Map<EnumLiteral, String> enumLitsMap = map();

    /**
     * Get the base name of an enumeration.
     *
     * @param enumDecl Enumeration to name.
     * @return Basename (without prefix) of the given enumeration.
     */
    private String getEnum(EnumDecl enumDecl) {
        String name = enumsMap.get(enumDecl);
        if (name != null) {
            return name;
        }
        name = makeName(enumDecl.getName(), ENUM_PREFIXES);
        enumsMap.put(enumDecl, name);
        return name;
    }

    /**
     * Get the mCRL2 name of an enumeration.
     *
     * @param enumDecl Enumeration to get name of.
     * @return Name of the provided enumeration.
     */
    public String getEnumName(EnumDecl enumDecl) {
        return ENUM_NAME + getEnum(enumDecl);
    }

    /**
     * Get the base name of an enumeration literal.
     *
     * @param enumLit Enumeration literal to name.
     * @return Basename (without prefix) of the given enumeration literal.
     */
    private String getEnumLit(EnumLiteral enumLit) {
        String name = enumLitsMap.get(enumLit);
        if (name != null) {
            return name;
        }
        name = makeName(enumLit.getName(), ENUM_LIT_PREFIXES);
        enumLitsMap.put(enumLit, name);
        return name;
    }

    /**
     * Get the mCRL2 name of an enumeration literal.
     *
     * @param enumLit Enumeration literal to get name of.
     * @return Name of the provided enumeration literal.
     */
    public String getEnumLitName(EnumLiteral enumLit) {
        return ENUM_LIT_NAME + getEnumLit(enumLit);
    }

    //
    // Type names.
    //

    /**
     * Return the name of a type for mCRL2.
     *
     * @param tp Type to name.
     * @return Name of the type in mCRL2.
     */
    public String getTypeName(CifType tp) {
        tp = CifTypeUtils.normalizeType(tp);
        if (tp instanceof BoolType) {
            return "Bool";
        } else if (tp instanceof IntType) {
            return "Int";
        } else if (tp instanceof EnumType) {
            EnumDecl enumDecl = ((EnumType)tp).getEnum();
            return getEnumName(enumDecl);
        } else {
            throw new RuntimeException("Unexpected type: " + tp);
        }
    }
}
