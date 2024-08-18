//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBinaryExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBoolType;
import static org.eclipse.escet.common.emf.EMFHelper.deepclone;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.last;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.mapc;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.escet.cif.cif2cif.AddDefaultInitialValues;
import org.eclipse.escet.cif.cif2cif.ElimAlgVariables;
import org.eclipse.escet.cif.cif2cif.ElimComponentDefInst;
import org.eclipse.escet.cif.cif2cif.ElimConsts;
import org.eclipse.escet.cif.cif2cif.ElimIfUpdates;
import org.eclipse.escet.cif.cif2cif.ElimTypeDecls;
import org.eclipse.escet.cif.cif2cif.LinearizeProduct;
import org.eclipse.escet.cif.cif2cif.RemoveAnnotations;
import org.eclipse.escet.cif.cif2cif.RemoveIoDecls;
import org.eclipse.escet.cif.cif2cif.SimplifyValues;
import org.eclipse.escet.cif.cif2cif.SwitchesToIfs;
import org.eclipse.escet.cif.common.CifCollectUtils;
import org.eclipse.escet.cif.common.CifEnumUtils;
import org.eclipse.escet.cif.common.CifEvalException;
import org.eclipse.escet.cif.common.CifEvalUtils;
import org.eclipse.escet.cif.common.CifMarkedUtils;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.CifValueUtils;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Assignment;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.automata.Update;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumLiteral;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.declarations.VariableValue;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.BoolExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CastExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ElifExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EnumLiteralExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IfExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IntExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TauExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.EnumType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.box.MemoryCodeBox;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Lists;
import org.eclipse.escet.common.java.Sets;
import org.eclipse.escet.common.java.Termination;
import org.eclipse.escet.common.java.output.WarnOutput;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** CIF to mCRL2 transformer. */
public class CifToMcrl2Transformer {
    /** Cooperative termination query function. */
    private final Termination termination;

    /** Callback to send warnings to the user. */
    private final WarnOutput warnOutput;

    /** Per CIF enumeration, its representative. Is {@code null} if not yet initialized. */
    private Map<EnumDecl, EnumDecl> enums;

    /**
     * Per discrete variable of the original CIF specification, its original absolute name. Is {@code null} if not yet
     * initialized.
     */
    private Map<DiscVariable, String> origVarNames;

    /**
     * Per location pointer variable introduced during linearization, its original absolute automaton name. Is
     * {@code null} if not yet initialized.
     */
    private Map<DiscVariable, String> origAutNames;

    /**
     * Constructor for the {@link CifToMcrl2Transformer} class.
     *
     * @param termination Cooperative termination query function.
     * @param warnOutput Callback to send warnings to the user.
     */
    public CifToMcrl2Transformer(Termination termination, WarnOutput warnOutput) {
        this.termination = termination;
        this.warnOutput = warnOutput;
    }

    /**
     * Transform a CIF specification to an mCRL2 model.
     *
     * @param spec The CIF specification. The specification is modified in-place.
     * @param absSpecPath The absolute local file system path to the CIF file.
     * @param valueActionPatterns The 'value' action patterns.
     * @param addMarked Whether to add a 'marked' action.
     * @return The mCRL2 model.
     */
    public CodeBox transform(Specification spec, String absSpecPath, String valueActionPatterns, boolean addMarked) {
        // Perform preprocessing to increase the supported subset.
        preprocess1(spec);

        // Check preconditions.
        CifToMcrl2PreChecker checker = new CifToMcrl2PreChecker(termination);
        checker.reportPreconditionViolations(spec, absSpecPath, "CIF to mCRL2 transformation");

        // Get original discrete variables and their original absolute names.
        List<DiscVariable> origVars = CifCollectUtils.collectDiscVariables(spec, list());
        origVarNames = origVars.stream().collect(Collectors.toMap(v -> v, v -> CifTextUtils.getAbsName(v, false)));

        // Perform additional pre-processing to ease the transformation. Also collect location pointer variables and
        // their original absolute automata names.
        origAutNames = preprocess2(spec);

        // Collect relevant declarations from the specification.
        enums = CifEnumUtils.getEnumDeclReprs(CifCollectUtils.collectEnumDecls(spec, list()));
        List<DiscVariable> vars = CifCollectUtils.collectDiscVariables(spec, list());
        List<Event> events = CifCollectUtils.collectEvents(spec, list());

        // Collect linearized edges from the specification.
        List<Automaton> linearizedAuts = CifCollectUtils.collectAutomata(spec, list());
        Assert.areEqual(linearizedAuts.size(), 1);
        Automaton aut = first(linearizedAuts);
        Assert.areEqual(aut.getLocations().size(), 1);
        Location loc = first(aut.getLocations());
        List<Edge> edges = loc.getEdges();

        // Collect variables from linearized specification that require 'value' actions.
        Set<DiscVariable> valueActionVars = determineValueActionVars(vars, valueActionPatterns);

        // Collect simplified marker predicate, if applicable.
        Expression marked = addMarked ? getMarked(spec) : null;

        // Generate the mCRL2 code.
        MemoryCodeBox code = new MemoryCodeBox();
        code.add("% Generated by CIF to mCRL2.");
        code.add();
        addSortsForEnums(code);
        addActionsForEvents(events, code);
        addVarValueActions(valueActionVars, code);
        if (addMarked) {
            addMarkedAction(code);
        }
        addProcess(vars, valueActionVars, edges, marked, code);
        addInstantiationForInit(vars, code);

        // Return the generated model.
        return code;
    }

    /**
     * Perform preprocessing to increase the supported subset of models.
     *
     * @param spec The CIF specification to preprocess. Is modified in-place.
     */
    private void preprocess1(Specification spec) {
        // Remove/ignore I/O declarations, to increase the supported subset.
        RemoveIoDecls removeIoDecls = new RemoveIoDecls();
        removeIoDecls.transform(spec);
        if (removeIoDecls.haveAnySvgInputDeclarationsBeenRemoved()) {
            warnOutput.line("The specification contains CIF/SVG input declarations. These will be ignored.");
        }

        // Perform preprocessing on the specification. The most expensive variant of value simplification is used, to
        // inline (and thus support) constants, and get the most simple result.
        new RemoveAnnotations().transform(spec);
        new ElimComponentDefInst().transform(spec);
        new SimplifyValues().transform(spec);
    }

    /**
     * Perform additional pre-processing to ease the transformation.
     *
     * @param spec The CIF specification to pre-process. Is modified in-place.
     * @return Per location pointer variable introduced during linearization, its original absolute automaton name.
     */
    private Map<DiscVariable, String> preprocess2(Specification spec) {
        // Inline type declarations.
        new ElimTypeDecls().transform(spec);

        // Inline algebraic constants and algebraic variables.
        new ElimConsts().transform(spec);
        new ElimAlgVariables().transform(spec);

        // Add default initial values to variables.
        new AddDefaultInitialValues().transform(spec);

        // Linearize the specification.
        LinearizeProduct linearize = new LinearizeProduct(true);
        linearize.transform(spec);

        // Convert 'switch' expressions to 'if' expressions.
        new SwitchesToIfs().transform(spec);

        // Convert 'if' updates to 'if' expressions.
        new ElimIfUpdates().transform(spec);

        // Return location pointer information.
        return linearize.getLpVarToAbsAutNameMap();
    }

    /**
     * Determine the variables of the linearized specification for which 'value' actions are to be generated.
     *
     * @param vars The variables of the linearized CIF specification.
     * @param valueActionPatterns Patterns for selecting variables and automata that get 'value' actions.
     * @return The variables of the linearized CIF specification that get 'value' actions.
     */
    private Set<DiscVariable> determineValueActionVars(List<DiscVariable> vars, String valueActionPatterns) {
        // Get name to discrete variable mapping.
        Map<String, DiscVariable> namesMap = mapc(origVarNames.size() + origAutNames.size());
        for (Entry<DiscVariable, String> entry: origVarNames.entrySet()) {
            DiscVariable prev = namesMap.put(entry.getValue(), entry.getKey());
            Assert.check(prev == null); // No duplicates.
        }
        for (Entry<DiscVariable, String> entry: origAutNames.entrySet()) {
            DiscVariable prev = namesMap.put(entry.getValue(), entry.getKey());
            Assert.check(prev == null); // No duplicates.
        }

        // Get set of all names to match against.
        Set<String> names = set();
        names.addAll(origVarNames.values());
        names.addAll(origAutNames.values());
        names = Sets.list2set(Sets.sortedstrings(names));

        // Match names against the patterns to determine the names to include.
        Set<String> includeNames = ProcessValueActions.matchNames(valueActionPatterns, names, warnOutput);

        // Get the variables of the linearized specification that should get 'value' actions.
        Set<DiscVariable> result = includeNames.stream().map(n -> namesMap.get(n)).collect(Sets.toSet());
        Assert.check(vars.containsAll(result)); // Select a subset of the variables from the linearized specification.
        return result;
    }

    /**
     * Get the marker predicate of the specification, and simplify it.
     *
     * @param spec The CIF specification.
     * @return The simplified marker predicate.
     */
    private Expression getMarked(Specification spec) {
        // Get marker predicate of the specification.
        Expression marked = CifMarkedUtils.getMarked(spec);

        // Simplify the marker predicate. We temporarily put the predicate in the specification, to ensure value
        // simplification won't crash. Then we simplify the predicate, and take it out of the specification again.
        // Note that simplification may completely replace the predicate, so we need to get it from the specification
        // after simplification.
        spec.getMarkeds().add(marked);
        new SimplifyValues().transform(marked);
        marked = last(spec.getMarkeds());
        spec.getMarkeds().remove(marked);
        return marked;
    }

    /**
     * Add mCRL2 sorts for the representative CIF enumerations.
     *
     * @param code The mCRL2 code generated so far. Is extended in-place.
     */
    private void addSortsForEnums(MemoryCodeBox code) {
        // Skip if no enums.
        if (enums.isEmpty()) {
            return;
        }

        // Generate a sort for each unique representative enumeration.
        code.add("% Sorts for CIF enumerations.");
        for (EnumDecl enumDecl: enums.values().stream().collect(Sets.toSet())) {
            String litNames = enumDecl.getLiterals().stream().map(l -> getName(l)).collect(Collectors.joining(" | "));
            code.add("sort %s = struct %s;", getName(enumDecl), litNames);
        }
        code.add();
    }

    /**
     * Add mCRL2 actions for the CIF events.
     *
     * @param events The CIF events.
     * @param code The mCRL2 code generated so far. Is extended in-place.
     */
    private void addActionsForEvents(List<Event> events, MemoryCodeBox code) {
        // Skip if no events.
        if (events.isEmpty()) {
            return;
        }

        // Generate an action for each event.
        code.add("% Actions for CIF events.");
        for (Event event: events) {
            code.add("act %s;", getName(event));
        }
        code.add();
    }

    /**
     * Add mCRL2 actions for the variable 'value' actions.
     *
     * @param vars The CIF variables that should get a variable 'value' action.
     * @param code The mCRL2 code generated so far. Is extended in-place.
     */
    private void addVarValueActions(Set<DiscVariable> vars, MemoryCodeBox code) {
        // Skip if no variables.
        if (vars.isEmpty()) {
            return;
        }

        // Generate an action for each variable.
        code.add("% Actions for CIF variables having certain values.");
        for (DiscVariable var: vars) {
            code.add("act %s'varvalue: %s;", getName(var), generateSortExprForType(var.getType()));
        }
        code.add();
    }

    /**
     * Add an mCRL2 'marked' action.
     *
     * @param code The mCRL2 code generated so far. Is extended in-place.
     */
    private void addMarkedAction(MemoryCodeBox code) {
        code.add("% Action for CIF marker predicate.");
        code.add("act marked;");
        code.add();
    }

    /**
     * Add an mCRL2 process for the CIF variables and edges.
     *
     * @param vars The CIF variables.
     * @param valueActVars The CIF variables that should get a variable 'value' action.
     * @param edges The CIF edges.
     * @param marked If non-{@code null}, the marker predicate of the specification for which to add a 'marked' action.
     *     If {@code null}, no such action is added.
     * @param code The mCRL2 code generated so far. Is extended in-place.
     */
    private void addProcess(List<DiscVariable> vars, Set<DiscVariable> valueActVars, List<Edge> edges,
            Expression marked, MemoryCodeBox code)
    {
        // Generate header.
        code.add("% Process for behavior of the CIF specification.");
        if (vars.isEmpty()) {
            code.add("proc P =");
        } else {
            code.add("proc P(");
            code.indent();
            for (int i = 0; i < vars.size(); i++) {
                DiscVariable var = vars.get(i);
                boolean isLast = i == vars.size() - 1;
                code.add("%s: %s%s", getName(var), generateSortExprForType(var.getType()), isLast ? "" : ",");
            }
            code.dedent();
            code.add(") =");
        }

        // Generate process body.
        code.indent();
        boolean first = true;

        // Add process expressions to body, for edges.
        boolean firstEdge = true;
        for (Edge edge: edges) {
            if (!first) {
                code.add("+");
            }
            first = false;
            if (firstEdge) {
                code.add("% CIF linearized edges.");
                firstEdge = false;
            }
            addProcessExprForEdge(edge, vars, code);
        }

        // Add process expressions to body, for variable 'value' actions.
        boolean firstValueAct = true;
        for (DiscVariable valueActVar: valueActVars) {
            if (!first) {
                code.add("+");
            }
            first = false;
            if (firstValueAct) {
                code.add("% CIF variable value actions.");
                firstValueAct = false;
            }
            addProcessExprForValueActVar(valueActVar, code);
        }

        // Add process expression to body, for 'marked' action.
        if (marked != null) {
            if (!first) {
                code.add("+");
            }
            first = false;
            code.add("% CIF 'marked' action.");
            addProcessExprForMarked(marked, code);
        }

        // If the process body is empty, add a 'delta' process expression, to ensure a valid process without behavior.
        if (first) {
            code.add("delta");
        }

        // Done with body.
        code.dedent();
        code.add(";");
    }

    /**
     * Generate an mCRL2 sort expressions for a CIF type.
     *
     * @param type The CIF type.
     * @return The mCRL2 sort expression.
     */
    private String generateSortExprForType(CifType type) {
        if (type instanceof BoolType) {
            return "Bool";
        } else if (type instanceof IntType) {
            return "Int";
        } else if (type instanceof EnumType enumType) {
            EnumDecl representative = enums.get(enumType.getEnum());
            return getName(representative);
        } else {
            throw new RuntimeException("Unexpected type: " + type);
        }
    }

    /**
     * Add an mCRL2 process expression for a CIF edge.
     *
     * @param edge The CIF edge.
     * @param vars The CIF variables of the linearized specification.
     * @param code The mCRL2 code generated so far. Is extended in-place.
     */
    private void addProcessExprForEdge(Edge edge, List<DiscVariable> vars, MemoryCodeBox code) {
        // Copy guards of the linearized edge.
        List<Expression> guards = list();
        guards.addAll(deepclone(edge.getGuards()));

        // Add additional guards to ensure that assigned integer variables don't go out of range. This prevents runtime
        // errors.
        Map<DiscVariable, Expression> assignments = getAssignments(edge.getUpdates());
        List<DiscVariable> intVars = vars.stream().filter(v -> v.getType() instanceof IntType).toList();
        for (DiscVariable var: intVars) {
            Expression newValue = assignments.get(var);
            if (newValue != null) {
                int lower = CifTypeUtils.getLowerBound((IntType)var.getType());
                int upper = CifTypeUtils.getUpperBound((IntType)var.getType());
                guards.addAll(createRangeGuards(newValue, lower, upper));
            }
        }

        // Get a single CIF guard predicate.
        Expression guardExpr = CifValueUtils.createConjunction(guards, true);

        // Simplify the guard predicate. We temporarily put the predicate in the specification, to ensure value
        // simplification won't crash. Then we simplify the predicate, and take it out of the specification again.
        // Note that simplification may completely replace the predicate, so we need to get it from the specification
        // after simplification.
        edge.getGuards().add(guardExpr);
        new SimplifyValues().transform(guardExpr);
        guardExpr = last(edge.getGuards());
        edge.getGuards().remove(guardExpr);

        // Get the mCRL2 guard expression.
        String guard = generateExpr(guardExpr);

        // Get event.
        Assert.areEqual(edge.getEvents().size(), 1);
        EdgeEvent edgeEvent = first(edge.getEvents());
        Expression eventRef = edgeEvent.getEvent();
        String event;
        if (eventRef instanceof EventExpression eventExpr) {
            event = getName(eventExpr.getEvent());
        } else if (eventRef instanceof TauExpression) {
            event = "tau";
        } else {
            throw new RuntimeException("Unexpected event reference: " + eventRef);
        }

        // Get updates.
        String updates = assignments.entrySet().stream().map(
                (Entry<DiscVariable, Expression> e) -> fmt("%s = %s", getName(e.getKey()), generateExpr(e.getValue())))
                .collect(Collectors.joining(", "));

        // Generate process expression.
        code.add("(%s) -> %s . P(%s)", guard, event, updates);
    }

    /**
     * Creates guards to ensure that an integer-typed variable doesn't go out of range.
     *
     * @param value The value of the variable.
     * @param lower The lower bound of the integer type.
     * @param upper The upper bound of the integer type.
     * @return The newly created guard predicates.
     */
    private List<Expression> createRangeGuards(Expression value, int lower, int upper) {
        // Create 'lower <= value'.
        Expression left1 = CifValueUtils.makeInt(lower);
        Expression right1 = deepclone(value);
        BinaryExpression bexpr1 = newBinaryExpression();
        bexpr1.setLeft(left1);
        bexpr1.setOperator(BinaryOperator.LESS_EQUAL);
        bexpr1.setRight(right1);
        bexpr1.setType(newBoolType());

        // Create 'value <= upper'.
        Expression left2 = deepclone(value);
        Expression right2 = CifValueUtils.makeInt(upper);
        BinaryExpression bexpr2 = newBinaryExpression();
        bexpr2.setLeft(left2);
        bexpr2.setOperator(BinaryOperator.LESS_EQUAL);
        bexpr2.setRight(right2);
        bexpr2.setType(newBoolType());

        // Return both.
        return List.of(bexpr1, bexpr2);
    }

    /**
     * Get a mapping from assigned variables to their assigned expressions.
     *
     * @param updates The CIF updates to consider.
     * @return The mapping.
     */
    private Map<DiscVariable, Expression> getAssignments(List<Update> updates) {
        Map<DiscVariable, Expression> result = mapc(updates.size());
        for (Update update: updates) {
            // Get assignment.
            Assert.check(update instanceof Assignment);
            Assignment asgn = (Assignment)update;

            // Get variable and value.
            Assert.check(asgn.getAddressable() instanceof DiscVariableExpression);
            DiscVariable var = ((DiscVariableExpression)asgn.getAddressable()).getVariable();
            Expression value = asgn.getValue();

            // Add to mapping.
            Expression prev = result.put(var, value);
            Assert.check(prev == null);
        }
        return result;
    }

    /**
     * Add an mCRL2 process expression for a variable 'value' action.
     *
     * @param var The CIF variable for which to add the variable 'value' action.
     * @param code The mCRL2 code generated so far. Is extended in-place.
     */
    private void addProcessExprForValueActVar(DiscVariable var, MemoryCodeBox code) {
        code.add("%s'varvalue(%s) . P()", getName(var), getName(var));
    }

    /**
     * Add an mCRL2 process expression for the 'marked' action.
     *
     * @param marked The CIF marker predicate.
     * @param code The mCRL2 code generated so far. Is extended in-place.
     */
    private void addProcessExprForMarked(Expression marked, MemoryCodeBox code) {
        code.add("(%s) -> marked . P()", generateExpr(marked));
    }

    /**
     * Generate an mCRL2 data expression for conjunction of some CIF predicates.
     *
     * @param preds The CIF predicates.
     * @return The mCRL2 data expression.
     */
    private String generatePreds(List<Expression> preds) {
        if (preds.isEmpty()) {
            return "true";
        } else if (preds.size() == 1) {
            return (generateExpr(first(preds)));
        } else {
            return preds.stream().map(p -> generateExpr(p)).collect(Collectors.joining(" && ", "(", ")"));
        }
    }

    /**
     * Generate an mCRL2 data expression for a CIF expression.
     *
     * @param expr The CIF expression.
     * @return The mCRL2 data expression.
     */
    private String generateExpr(Expression expr) {
        if (expr instanceof BoolExpression boolExpr) {
            return boolExpr.isValue() ? "true" : "false";
        } else if (expr instanceof BinaryExpression binExpr) {
            String left = generateExpr(binExpr.getLeft());
            String right = generateExpr(binExpr.getRight());
            String op = switch (binExpr.getOperator()) {
                case ADDITION -> "+";
                case BI_CONDITIONAL -> "==";
                case CONJUNCTION -> "&&";
                case DISJUNCTION -> "||";
                case EQUAL -> "==";
                case GREATER_EQUAL -> ">=";
                case GREATER_THAN -> ">";
                case IMPLICATION -> "=>";
                case INTEGER_DIVISION -> "div";
                case LESS_EQUAL -> "<=";
                case LESS_THAN -> "<";
                case MODULUS -> "mod";
                case MULTIPLICATION -> "*";
                case SUBTRACTION -> "-";
                case UNEQUAL -> "!=";
                default -> throw new RuntimeException("Unexpected operator: " + binExpr.getOperator());
            };
            return fmt("(%s %s %s)", left, op, right);
        } else if (expr instanceof UnaryExpression unExpr) {
            String child = generateExpr(unExpr.getChild());
            String op = switch (unExpr.getOperator()) {
                case INVERSE -> "!";
                case NEGATE -> "-";
                case PLUS -> "";
                default -> throw new RuntimeException("Unexpected operator: " + unExpr.getOperator());
            };
            return fmt("%s%s", op, child);
        } else if (expr instanceof DiscVariableExpression discRefExpr) {
            return getName(discRefExpr.getVariable());
        } else if (expr instanceof IntExpression intLitExpr) {
            return Integer.toString(intLitExpr.getValue());
        } else if (expr instanceof EnumLiteralExpression enumLitRefExpr) {
            EnumLiteral refEnumLit = enumLitRefExpr.getLiteral();
            EnumDecl refEnumDecl = (EnumDecl)refEnumLit.eContainer();
            int litIdx = refEnumDecl.getLiterals().indexOf(refEnumLit);
            EnumDecl representativeEnumDecl = enums.get(refEnumDecl);
            EnumLiteral representativeEnumLit = representativeEnumDecl.getLiterals().get(litIdx);
            return getName(representativeEnumLit);
        } else if (expr instanceof IfExpression ifExpr) {
            String result = generateExpr(ifExpr.getElse());
            for (ElifExpression elifExpr: Lists.reverse(ifExpr.getElifs())) {
                String elifGuard = generatePreds(elifExpr.getGuards());
                String elifThen = generateExpr(elifExpr.getThen());
                result = fmt("if(%s, %s, %s)", elifGuard, elifThen, result);
            }
            String ifGuard = generatePreds(ifExpr.getGuards());
            String ifThen = generateExpr(ifExpr.getThen());
            result = fmt("if(%s, %s, %s)", ifGuard, ifThen, result);
            return result;
        } else if (expr instanceof CastExpression castExpr) {
            return generateExpr(castExpr.getChild());
        }
        throw new RuntimeException("Unexpected expression: " + expr);
    }

    /**
     * Add an mCRL2 instantiation for the initial values of the variables.
     *
     * @param vars The variables.
     * @param code The code generated so far. Is extended in-place.
     */
    private void addInstantiationForInit(List<DiscVariable> vars, MemoryCodeBox code) {
        List<String> initExprs = listc(vars.size());
        for (DiscVariable var: vars) {
            // Get initial value.
            VariableValue varValue = var.getValue();
            Assert.areEqual(varValue.getValues().size(), 1);
            Expression value = first(varValue.getValues());

            // Evaluate statically. For negative integer values, this may not be a literal, but still an expression.
            try {
                value = CifEvalUtils.evalAsExpr(value, true);
            } catch (CifEvalException e) {
                throw new RuntimeException("Precondition violation.", e);
            }

            // Add initial value.
            initExprs.add(generateExpr(value));
        }

        code.add();
        code.add("% Initialization.");
        code.add("init P(%s);", String.join(", ", initExprs));
    }

    /**
     * Get the mCRL2 name for a CIF object.
     *
     * @param cifObject The CIF object.
     * @return The mCRL2 name.
     */
    private String getName(PositionObject cifObject) {
        // Get absolute names. Since discrete variables are moved to the single linearized automaton during
        // linearization, use their original variable/automaton names.
        String name;
        if (cifObject instanceof DiscVariable dvar && origVarNames.containsKey(dvar)) {
            name = origVarNames.get(dvar);
        } else if (cifObject instanceof DiscVariable dvar && origAutNames.containsKey(dvar)) {
            name = origAutNames.get(dvar);
        } else {
            name = CifTextUtils.getAbsName(cifObject, false);
        }

        // Make an mCRL2 name. Replace dots by apostrophes. Add one at the end to avoid clashes with mCRL2 keywords.
        name = name.replace('.', '\'');
        name += "'";
        return name;
    }
}
