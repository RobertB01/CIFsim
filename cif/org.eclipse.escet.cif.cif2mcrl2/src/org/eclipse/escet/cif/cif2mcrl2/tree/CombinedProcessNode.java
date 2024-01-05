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

package org.eclipse.escet.cif.cif2mcrl2.tree;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Sets.setc;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.escet.cif.cif2mcrl2.NameMaps;
import org.eclipse.escet.cif.cif2mcrl2.storage.EventVarUsage;
import org.eclipse.escet.cif.cif2mcrl2.storage.VarUsage;
import org.eclipse.escet.cif.cif2mcrl2.storage.VariableData;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.common.box.HBox;
import org.eclipse.escet.common.box.StreamCodeBox;
import org.eclipse.escet.common.box.VBox;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Pair;
import org.eclipse.escet.common.java.Strings;

/** Node in the instance tree that combines one or more children. */
public class CombinedProcessNode extends ProcessNode {
    /** Children of this node. */
    public final List<ProcessNode> children;

    /**
     * Constructor of the {@link CombinedProcessNode} class.
     *
     * @param name Name of the node.
     * @param children Children of this node.
     */
    public CombinedProcessNode(String name, List<ProcessNode> children) {
        super(name);
        Assert.check(!children.isEmpty());
        this.children = children;
    }

    @Override
    public void deriveActions(Set<VariableData> localVars) {
        // 1. Derive actions for the children.
        for (ProcessNode pn: children) {
            pn.deriveActions(localVars);
        }

        // 2. Merge available variables of all children.
        int count = 0;
        for (ProcessNode pn: children) {
            count += pn.availProcessVars.size();
        }
        availProcessVars = setc(count);
        for (ProcessNode pn: children) {
            availProcessVars.addAll(pn.availProcessVars);
        }

        // 3. Merge value variables of all children.
        count = 0;
        for (ProcessNode pn: children) {
            count += pn.valueVars.size();
        }
        valueVars = setc(count);
        for (ProcessNode pn: children) {
            valueVars.addAll(pn.valueVars);
        }

        // 4. Merge event variable uses.
        eventVarUse = map();
        for (Event evt: collectAlphabet()) {
            eventVarUse.put(evt, computeEventVarUsage(evt));
        }

        // 5. Make all 'availVars' available in 'eventVarUse'.
        // Processes higher in the tree may want to use the available variables
        // in an event, even though that does not happen in this sub-tree. By
        // adding them as "never used" variables for each event, that option
        // remains open.
        for (EventVarUsage evu: eventVarUse.values()) {
            for (VariableData vd: availProcessVars) {
                if (evu.varUses.containsKey(vd)) {
                    continue;
                }
                evu.varUses.put(vd, new VarUsage(vd));
            }
        }
    }

    /**
     * Collect the combined alphabet for this sub-tree (union of the alphabets of its children).
     *
     * @return The combined alphabet of all children.
     */
    private Set<Event> collectAlphabet() {
        Set<Event> events = set();
        for (ProcessNode pn: children) {
            events.addAll(pn.eventVarUse.keySet());
        }
        return events;
    }

    /**
     * Compute the variable usage for the given event by combining the usage from all children (for the given event).
     *
     * @param evt Event to use for combining usage.
     * @return Combined variable usage for the given event.
     */
    private EventVarUsage computeEventVarUsage(Event evt) {
        EventVarUsage evu = new EventVarUsage(evt);

        // Find the relevant usages of the children.
        List<EventVarUsage> childEventUsage = listc(children.size());
        for (ProcessNode pn: children) {
            EventVarUsage chEvu = pn.eventVarUse.get(evt);
            if (chEvu != null) {
                childEventUsage.add(chEvu);
            }
        }

        // Find child var usages for each variable.
        Map<VariableData, List<VarUsage>> varUsageMap = map();
        for (EventVarUsage chEvu: childEventUsage) {
            for (Entry<VariableData, VarUsage> entry: chEvu.varUses.entrySet()) {
                List<VarUsage> vu = varUsageMap.get(entry.getKey());
                if (vu == null) {
                    vu = list();
                    varUsageMap.put(entry.getKey(), vu);
                }
                vu.add(entry.getValue());
            }
        }

        // Derive combined variable use.
        for (Entry<VariableData, List<VarUsage>> entry: varUsageMap.entrySet()) {
            evu.varUses.put(entry.getKey(), computeCombinedVarUse(entry.getValue()));
        }
        return evu;
    }

    /**
     * Derive how a variable is being used by all relevant children.
     *
     * @param uses Usage of the variable by each child.
     * @return Combined usage.
     */
    private VarUsage computeCombinedVarUse(List<VarUsage> uses) {
        VarUsage result = null;
        for (VarUsage vu: uses) {
            if (result == null) {
                result = vu.copy();
            } else {
                result.merge(vu);
            }
        }
        Assert.notNull(result);
        return result;
    }

    @Override
    public void addDefinitions(NameMaps names, Set<VariableData> localVars, VBox code) {
        for (ProcessNode node: children) {
            node.addDefinitions(names, localVars, code);
        }
    }

    @Override
    public void addInstantiations(NameMaps names, Set<VariableData> localVars, VBox code) {
        // Given the shape of the tree enforced by InstanceTreeVerifier.checkProcessTreeShape,
        // decide which kind of node this is.
        // * 'false' means it is a node with only behavior processes,
        // * 'true' means there is a variable process here too.
        VariableProcessNode variableChild = null;
        for (ProcessNode node: children) {
            if (node instanceof VariableProcessNode) {
                variableChild = (VariableProcessNode)node;
                break;
            }
        }

        VBox vb = new VBox();
        int closingParens = 0;

        if (variableChild == null) {
            // Parallel composition of behaviour processes.

            // Add allows.
            //
            // Apparently, this code breaks mCRL2 verification due to semantics of 'allow' in the
            // case of variable process nodes near the top.
            // Check above reduces its use to behavior process tree only (at the 'bottom' of the
            // tree, due to the enforced tree shape).
            closingParens += addAllows(names, vb);
        } else {
            // Parallel composition of a variable and a behavior proces(-tree).

            closingParens += addReadWriteBlocks(variableChild, names, vb);
            closingParens += addReadWriteSync(variableChild, names, vb);
        }

        // Add variable access synchronization between behavior processes.
        Map<VariableData, Integer> maxVarReadAccess, maxVarWriteAccess;
        maxVarReadAccess = getMaxBehaviorProcessAccessCount(true);
        maxVarWriteAccess = getMaxBehaviorProcessAccessCount(false);
        closingParens += addBehProcessSync(names, vb, maxVarReadAccess, maxVarWriteAccess);

        // Add comm over events.
        closingParens += addEventSynchronization(names, vb);

        // Add parallel composition of children.
        boolean first = true;
        vb.add("(");
        closingParens++;
        for (ProcessNode node: children) {
            if (!first) {
                vb.add("||");
            }
            first = false;
            VBox childBox = new VBox();
            node.addInstantiations(names, localVars, childBox);
            vb.add(new HBox("  ", childBox));
        }

        if (closingParens > 0) {
            vb.add(Strings.duplicate(")", closingParens));
        }
        code.add(vb);
    }

    /**
     * Add a <code>block({vread, vwrite, aread, awrite, sync}, ..</code> line for the given variable.
     *
     * @param varProc Variable process with the variable to block.
     * @param names Names of the events.
     * @param code Generated code (appended in-place).
     * @return Number of additional closing parentheses to add at the end of the code.
     */
    private int addReadWriteBlocks(VariableProcessNode varProc, NameMaps names, VBox code) {
        DiscVariable var = varProc.variable.variable;
        String blockText = fmt("block({%s, %s, %s, %s},", names.getBehRead(var), names.getBehWrite(var),
                names.getVarRead(var), names.getVarWrite(var));
        HBox hb = new HBox();
        hb.add(blockText);
        code.add(hb);
        return 1;
    }

    /**
     * Rewrite read and write between variable and process to sync events.
     *
     * @param varProc Variable process with the variable to block.
     * @param names Names of the events.
     * @param code Generated code (appended in-place).
     * @return Number of additional closing parentheses to add at the end of the code.
     */
    private int addReadWriteSync(VariableProcessNode varProc, NameMaps names, VBox code) {
        DiscVariable var = varProc.variable.variable;
        HBox hb = new HBox();
        hb.add(fmt("hide({%s},", names.getVarSync(var)));
        code.add(hb);
        String rdSync = fmt("%s | %s -> %s", names.getBehRead(var), names.getVarRead(var), names.getVarSync(var));
        String wtSync = fmt("%s | %s -> %s", names.getBehWrite(var), names.getVarWrite(var), names.getVarSync(var));
        code.add(new HBox("comm(", vertBoxify(list(rdSync, wtSync)), ","));
        return 2;
    }

    /**
     * Add 'allow' wrapper around the process term of this node.
     *
     * @param names Mapping of CIF elements to mCRL2 names.
     * @param code Output stream with code.
     * @return Number of additional closing parentheses to add at the end of the code.
     */
    private int addAllows(NameMaps names, VBox code) {
        List<String> allows = list();

        // Add allowing reading values from the available values.
        for (VariableData val: valueVars) {
            if (!val.getHasValueAction()) {
                continue;
            }
            allows.add(names.getVariableValue(val.variable));
        }

        // Add action combinations for the events with their variable accesses.
        List<VarUsage> vus = list();
        for (EventVarUsage evu: eventVarUse.values()) {
            vus.clear();
            for (VarUsage vu: evu.varUses.values()) {
                vus.add(vu);
            }
            addActions(names.getEventName(evu.event), vus, 0, false, allows, names);
        }
        if (allows.isEmpty()) {
            return 0;
        }

        code.add(new HBox("allow(", vertBoxify(allows), ","));
        return 1;
    }

    /**
     * Recursively expand 'varUsages' to a sequence of actions, both for reading and writing. For the 'never use' or
     * 'always use' case there is one action to generate, for the 'sometimes use' there are two actions, automaton
     * process access of the variable, and variable process access (or the empty action if no variable process is
     * available at this point).
     *
     * @param prefix Action prefix constructed so far.
     * @param varUsages Variable usages to expand.
     * @param index Current variable in 'varUsages'.
     * @param readAccess Generate action for write access or for read access of the variable.
     * @param actions Collected complete allow actions.
     * @param names Mapping of CIF element to mCRL2 names.
     */
    private void addActions(String prefix, List<VarUsage> varUsages, int index, boolean readAccess,
            List<String> actions, NameMaps names)
    {
        if (index == varUsages.size()) {
            actions.add(prefix);
            return;
        }

        VarUsage vu = varUsages.get(index);
        VariableData varData = vu.var;
        DiscVariable dv = varData.variable;
        // Add var usage for 'index'
        if (readAccess) {
            // Add read access for var 'index'
            if (!vu.readAccess.everUsed || !vu.readAccess.alwaysUsed) {
                // Never used, or sometimes used in behavior process.

                // If there is a variable process, use its action instead.
                if (availProcessVars.contains(varData)) {
                    addActions(prefix + " | " + names.getVarRead(dv), varUsages, index + 1, false, actions, names);
                } else {
                    // No variable process either, skip this variable.
                    addActions(prefix, varUsages, index + 1, false, actions, names);
                }
            }
            if (vu.readAccess.everUsed) {
                // Behavior process reads this variable.
                addActions(prefix + " | " + names.getBehRead(dv), varUsages, index + 1, false, actions, names);
            }
        } else {
            // Add write access for var 'index'
            if (!vu.writeAccess.everUsed || !vu.writeAccess.alwaysUsed) {
                // Never used, or sometimes used in behavior process.

                // If there is a variable process, use its action instead.
                if (availProcessVars.contains(varData)) {
                    addActions(prefix + " | " + names.getVarWrite(dv), varUsages, index, true, actions, names);
                } else {
                    // No variable process either, skip this variable.
                    addActions(prefix, varUsages, index, true, actions, names);
                }
            }
            if (vu.writeAccess.everUsed) {
                // Behavior process writes this variable.
                addActions(prefix + " | " + names.getBehWrite(dv), varUsages, index, true, actions, names);
            }
        }
    }

    /**
     * For each variable, the largest number of children that access it concurrently (for an event).
     *
     * @param names Map of CIF elements to mCRL2 names.
     * @param code Output stream.
     * @param mvra For each variable, the largest number of children that access it for reading concurrently (for any
     *     event).
     * @param mvwa For each variable, the largest number of children that access it for writing concurrently (for any
     *     event).
     * @return Number of additional closing parentheses needed at the end of the code.
     */
    private int addBehProcessSync(NameMaps names, VBox code, Map<VariableData, Integer> mvra,
            Map<VariableData, Integer> mvwa)
    {
        // Sequence of action rewrites, "inside-out", innermost element is at
        // index 0. Each element contains several independent rewrites.
        List<List<String>> rewrites = list();

        for (Entry<VariableData, Integer> entry: mvra.entrySet()) {
            DiscVariable dv = entry.getKey().variable;
            String behAction = names.getBehRead(dv);
            addBehActionSync(rewrites, behAction, entry.getValue());
        }
        for (Entry<VariableData, Integer> entry: mvwa.entrySet()) {
            DiscVariable dv = entry.getKey().variable;
            String behAction = names.getBehWrite(dv);
            addBehActionSync(rewrites, behAction, entry.getValue());
        }

        if (rewrites.isEmpty()) {
            return 0;
        }

        for (int i = rewrites.size() - 1; i >= 0; i--) {
            code.add(new HBox("comm(", vertBoxify(rewrites.get(i)), ","));
        }
        return rewrites.size();
    }

    /**
     * Add an inside-out sequence of rewrite elements for synchronizing read or write actions of a variable by
     * participating behavior processes.
     *
     * <p>
     * Reduce the number of actions by iteratively mapping (count/2 + 1) actions to one, and updating the count.
     * </p>
     *
     * @param rewrites Sequence of already generated rewrites.
     * @param actName Action name to synchronize on.
     * @param count Number of actions that should be reduced to 1.
     */
    private void addBehActionSync(List<List<String>> rewrites, String actName, int count) {
        int index = 0;
        while (true) {
            if (count < 2) {
                return; // 0 or 1 actions left -> no sync needed.
            }

            // Construct rewrite with floor(count / 2) + 1 actions to 1 action.
            String s = "";
            for (int i = 0; i < count / 2 + 1; i++) {
                if (!s.isEmpty()) {
                    s += " | ";
                }
                s += actName;
            }
            s += " -> " + actName;

            // Add to list of rewrites, extending it if needed.
            if (rewrites.size() == index) {
                List<String> elm = list();
                rewrites.add(elm);
            }
            rewrites.get(index).add(s);

            count = count - (count / 2 + 1) + 1;
            index++;
        }
    }

    /**
     * For each variable accessed by behavior processes, decide the largest number of participating processes (for all
     * events).
     *
     * @param readAccess If set, inspect read access behavior, else inspect write access behavior.
     * @return Largest number of participating behavior processes for each accessed variable.
     */
    private Map<VariableData, Integer> getMaxBehaviorProcessAccessCount(boolean readAccess) {
        Set<VariableData> accessedVars = set();
        for (ProcessNode node: children) {
            for (EventVarUsage evu: node.eventVarUse.values()) {
                accessedVars.addAll(evu.varUses.keySet());
            }
        }
        Map<VariableData, Integer> maxCount = map();
        for (VariableData vd: accessedVars) {
            int largest = 0;
            for (Event event: eventVarUse.keySet()) {
                int total = 0;
                for (ProcessNode node: children) {
                    EventVarUsage evu = node.eventVarUse.get(event);
                    if (evu == null) {
                        continue;
                    }

                    VarUsage vu = evu.varUses.get(vd);
                    if (vu == null) {
                        continue;
                    }

                    if (readAccess) {
                        if (vu.readAccess.everUsed) {
                            total++;
                        }
                    } else {
                        if (vu.writeAccess.everUsed) {
                            total++;
                        }
                    }
                }
                if (largest < total) {
                    largest = total;
                }
            }
            maxCount.put(vd, largest);
        }
        return maxCount;
    }

    /**
     * Perform synchronization on the events of the automata.
     *
     * @param names Mapping of CIF elements to mCRL2 names.
     * @param code Output stream to write to.
     * @return Number of additional closing parentheses to add at the end.
     */
    private int addEventSynchronization(NameMaps names, VBox code) {
        List<Pair<Event, Integer>> eventCounts = list();

        // Find events that need to be synchronized.
        for (Event event: eventVarUse.keySet()) {
            int count = 0;
            for (ProcessNode node: children) {
                if (node.eventVarUse.containsKey(event)) {
                    count++;
                }
            }
            if (count > 1) {
                eventCounts.add(Pair.pair(event, count));
            }
        }
        if (eventCounts.isEmpty()) {
            return 0;
        }

        // Add renamed -> original rewrite.
        {
            List<String> lines = listc(eventCounts.size());
            for (Pair<Event, Integer> evtCount: eventCounts) {
                lines.add(fmt("%s -> %s", names.getRenamedEventName(evtCount.left), names.getEventName(evtCount.left)));
            }
            code.add(new HBox("rename(", vertBoxify(lines), ","));
        }

        // Forbid unrenamed events.
        {
            boolean first = true;
            HBox line = new HBox();
            for (Pair<Event, Integer> evtCount: eventCounts) {
                if (!first) {
                    line.add(", ");
                }
                first = false;
                line.add(names.getEventName(evtCount.left));
            }
            code.add(new HBox("block({", line, "},"));
        }

        // Synchronize events + rename them.
        {
            List<String> lines = listc(eventCounts.size());
            for (Pair<Event, Integer> evtCount: eventCounts) {
                String eventName = names.getEventName(evtCount.left);

                String line = "";
                for (int count = 0; count < evtCount.right; count++) {
                    if (!line.isEmpty()) {
                        line += " | ";
                    }
                    line += eventName;
                }
                lines.add(fmt("%s -> %s", line, names.getRenamedEventName(evtCount.left)));
            }
            code.add(new HBox("comm(", vertBoxify(lines), ","));
        }
        return 3;
    }

    @Override
    public void dumpActions(StreamCodeBox code) {
        super.dumpActions(code);

        code.add();
        code.add("Children:");
        code.indent();
        for (ProcessNode pn: children) {
            code.add("node " + pn.name);
        }
        code.dedent();

        for (ProcessNode pn: children) {
            code.add();
            pn.dumpActions(code);
        }
    }

    /**
     * Construct a vertically aligned list of lines in a set-like notation.
     *
     * <p>
     * For instance:
     *
     * <pre>
     * {line1,
     * line2,
     * line3,
     * line4}
     * </pre>
     * </p>
     *
     * @param lines Lines to vertically align.
     * @return Box with the aligned lines.
     */
    private static VBox vertBoxify(List<String> lines) {
        VBox vb = new VBox();
        int last = lines.size() - 1;
        for (int idx = 0; idx <= last; idx++) {
            String line = fmt("%s%s%s", (idx == 0) ? "{" : " ", lines.get(idx), (idx == last) ? "}" : ",");
            vb.add(line);
        }
        return vb;
    }
}
