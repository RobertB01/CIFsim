package org.eclipse.escet.cif.controllercheck;

import static org.eclipse.escet.cif.common.CifCollectUtils.collectDiscAndInputVariables;
import static org.eclipse.escet.cif.databased.multivaluesynthesis.MvSpecBuilder.convertVariables;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.escet.common.app.framework.exceptions.UnsupportedException;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.RangeCompat;
import org.eclipse.escet.cif.databased.multivaluesynthesis.MvSpecBuilder;
import org.eclipse.escet.cif.databased.multivaluetrees.Node;
import org.eclipse.escet.cif.databased.multivaluetrees.Tree;
import org.eclipse.escet.cif.databased.multivaluetrees.TreeVariable;
import org.eclipse.escet.cif.metamodel.cif.Equation;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Assignment;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.expressions.BaseFunctionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CastExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.LocationExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ProjectionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TauExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TupleExpression;
import org.eclipse.escet.cif.metamodel.cif.functions.Function;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.DictType;
import org.eclipse.escet.cif.metamodel.cif.types.DistType;
import org.eclipse.escet.cif.metamodel.cif.types.FuncType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.cif.metamodel.cif.types.ListType;
import org.eclipse.escet.cif.metamodel.cif.types.RealType;
import org.eclipse.escet.cif.metamodel.cif.types.SetType;
import org.eclipse.escet.cif.metamodel.cif.types.StringType;
import org.eclipse.escet.cif.metamodel.cif.types.TupleType;
import org.eclipse.escet.cif.metamodel.java.CifWalker;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Strings;
import nl.tue.position.v1x0x0.common.PositionUtils;
import nl.tue.position.v1x0x0.metamodel.position.PositionObject;

/** Pre-condition checker for the controller properties checker. */
public class ControllerCheckPreChecker extends CifWalker {
    /** Found problems in the specification. */
    public List<String> problems = list();

    /** Set of locations. */
    private Set<Location> locations = set();

    /**
     * Verify whether the provided specification fulfills the pre-conditions
     * of the controller properties check tool.
     *
     * <p>If problems are found, they are presented to the user, and execution is
     * terminated.</p>
     *
     * @param spec Specification to check.
     * @throws UnsupportedException If a precondition is violated.
     */
    public void check(Specification spec) {
        // Find precondition violations.
        walkSpecification(spec);

        // Determinism is checked via MDDs, it can only be verified if there are no problems.
        if (problems.isEmpty()) verifyDeterminsm(spec);

        // If no problems found, we're done.
        if (problems.isEmpty()) return;

        // Problem(s) found, the specification is unsupported.
        StringBuilder sb = new StringBuilder();
        sb.append("CIF 3 controller properties check application failed " +
                  "due to unsatisfied preconditions.\n" +
                  "Problems:");

        Collections.sort(problems, Strings.SORTER);
        for (String problem : problems) {
            sb.append("\n - ");
            sb.append(problem);
        }
        throw new UnsupportedException(sb.toString());
    }

    /**
     * Add the description of a newly found problem to the list of found problems.
     *
     * @param obj Object involved with the problem. May be {@code null}.
     * @param message Descriptive message of the problem to give to the user.
     */
    private void addProblem(PositionObject obj, String message) {
        if (obj == null) {
            problems.add("Error: " + message);
        } else {
            problems.add(fmt("Error at %s: %s", PositionUtils.pos2str(obj), message));
        }
    }

    /**
     * Verifies whether a specification is deterministic, that is automata that have locations with multiple outgoing
     * edges for the same event, with overlapping guards (e.g. x > 1 and x < 4), are not supported.
     *
     * @param spec The specification to check.
     */
    private void verifyDeterminsm(Specification spec) {
        List<Declaration> variables = list();
        collectDiscAndInputVariables(spec, variables);

        // Construct a MDD tree builder
        final int READINDEX = 0;
        final int WRITEINDEX = 1;

        TreeVariable[] treeVars = convertVariables(variables, READINDEX, WRITEINDEX);
        MvSpecBuilder builder = new MvSpecBuilder(treeVars, READINDEX, WRITEINDEX);

        // Verify determinism for each location.
        for (Location loc: locations) {
            verifyLocDeterminism(loc, builder);
        }
    }

    /**
     * Verifies whether a location is deterministic, that is a location with multiple outgoing edges for the same event,
     * with overlapping guards (e.g. x > 1 and x < 4), is not supported.
     *
     * @param loc The location to check.
     * @param builder The builder for the MDD tree.
     */
    private void verifyLocDeterminism(Location loc, MvSpecBuilder builder) {
        Map<Event, List<List<Expression>>> edgesPredsByEvent = map();
        for (Edge edge: loc.getEdges()) {
            // If there are edges, collect edges by event to check determinism.
            for (EdgeEvent ee: edge.getEvents()) {
                Expression eventExpr = ee.getEvent();
                Assert.check(eventExpr instanceof EventExpression);
                Event event = ((EventExpression)eventExpr).getEvent();
                List<List<Expression>> edgesPreds = edgesPredsByEvent.get(event);
                if (edgesPreds == null) {
                    edgesPreds = list();
                    edgesPredsByEvent.put(event, edgesPreds);
                }
                edgesPreds.add(edge.getGuards());
            }
        }

        // Verify collected edges.
        for (Entry<Event, List<List<Expression>>> entry: edgesPredsByEvent.entrySet()) {
            List<List<Expression>> edgeGuards = entry.getValue();
            if (edgeGuards.size() == 1) continue;

            // Convert guards to MDD tree and determine mutually exclusiveness.
            List<Node> edgeGuardNodes = listc(edgeGuards.size());
            for (List<Expression> edgeGuard: edgeGuards) {
                Node edgeGuardNode = builder.getPredicateConvertor().convert(edgeGuard).get(1);
                edgeGuardNodes.add(edgeGuardNode);
            }

            // Check mutually exclusiveness for every pair.
            for (int i = 0; i < edgeGuardNodes.size() - 1; i++) {
                for (int j = i + 1; j < edgeGuardNodes.size(); j++) {
                    Node n = builder.tree.conjunct(edgeGuardNodes.get(i), edgeGuardNodes.get(j));
                    if (n != Tree.ZERO) {
                        String msg = "Edges with event '%s' may be non-deterministic, which is not supported.";
                        addProblem(loc, fmt(msg, CifTextUtils.getAbsName(entry.getKey(), false)));
                    }
                }
            }
        }
    }

    // Specification checks.
    @Override
    protected void preprocessEquation(Equation eqn) {
        addProblem(eqn, "Equations are not supported.");
    }

    @Override
    protected void preprocessInvariant(Invariant inv) {
        addProblem(inv, "Invariants are not supported.");
    }

    @Override
    protected void preprocessAssignment(Assignment asg) {
        // Only allow simple variable references in the LHS.
        // Partial assignment (v.foo := bar) should only be encountered when lists are allowed.
        // Multi-assignments ((x, y) := (1, 2)) are not allowed by elim-if-updates.
        Expression addressable = asg.getAddressable();
        if (addressable instanceof ProjectionExpression) {
            addProblem(addressable, "Partial assignment is not supported.");

        } else if (addressable instanceof TupleExpression) {
            addProblem(addressable, "Multi-assignment is not supported.");
        }
    }

    @Override
    protected void preprocessLocation(Location loc) {
        // Save the location, to check determinism later.
        locations.add(loc);

        // Verify no 'tau' event on edges.
        for (Edge edge: loc.getEdges()) {
            // Implied tau event is not allowed.
            if (edge.getEvents().isEmpty()) {
                addProblem(edge, "Tau event is not supported.");
            }

            for (EdgeEvent ee: edge.getEvents()) {
                Expression eventExpr = ee.getEvent();
                if (eventExpr instanceof TauExpression) {
                    addProblem(eventExpr, "Tau event is not supported.");
                    continue;
                }
            }
        }
    }

    // Declaration checks.

    @Override
    protected void preprocessEvent(Event event) {
        if (event.getType() != null) {
            addProblem(event, "Channels are not supported.");
        }
        if (event.getControllable() == null) {
            addProblem(event, "Events without controllability property are not supported.");
        }
    }

    @Override
    protected void preprocessEnumDecl(EnumDecl edecl) {
        addProblem(edecl, "Enumeration declarations are not supported.");
    }

    @Override
    protected void preprocessContVariable(ContVariable contVar) {
        addProblem(contVar, "Continuous variables are not supported.");
    }

    @Override
    protected void preprocessDiscVariable(DiscVariable dvar) {
        CifType dtype = CifTypeUtils.normalizeType(dvar.getType());

        if (dtype instanceof BoolType) return;
        if (!(dtype instanceof IntType)) {
            String varName = CifTextUtils.getAbsName(dvar, false);
            String msg = fmt("Discrete variable '%s' must have a boolean type, a ranged integer type, or an " +
                             "enumeration type.", varName);
            addProblem(dvar, msg);
            return;
        }

        if (CifTypeUtils.isRangeless((IntType)dtype)) {
            String varName = CifTextUtils.getAbsName(dvar, false);
            String msg = fmt("Discrete variable '%s' with a rangeless integer type is not supported.", varName);
            addProblem(dvar, msg);
        }
    }

    @Override
    protected void preprocessFunction(Function func) {
        String fnName = CifTextUtils.getAbsName(func, false);
        addProblem(func, fmt("Function '%s' is not supported.", fnName));
    }

    // Type checks.

    @Override
    protected void preprocessTupleType(TupleType tupleType) {
        addProblem(tupleType, "Tuples are not supported.");
    }

    @Override
    protected void preprocessListType(ListType listType) {
        addProblem(listType, "Lists are not supported.");
    }

    @Override
    protected void preprocessStringType(StringType strType) {
        addProblem(strType, "String type is not supported.");
    }

    @Override
    protected void preprocessRealType(RealType realType) {
        addProblem(realType, "Real type is not supported.");
    }

    @Override
    protected void preprocessSetType(SetType setType) {
        addProblem(setType, "Sets are not supported.");
    }

    @Override
    protected void preprocessFuncType(FuncType funcType) {
        addProblem(funcType, "Function types are not supported.");
    }

    @Override
    protected void preprocessDistType(DistType distType) {
        addProblem(distType, "Distributions are not supported.");
    }

    @Override
    protected void preprocessDictType(DictType dictType) {
        addProblem(dictType, "Dictionaries are not supported.");
    }

    // Expression checks.

    @Override
    protected void preprocessLocationExpression(LocationExpression locExpr) {
        addProblem(locExpr, "Location references are not supported.");
    }

    @Override
    protected void preprocessBaseFunctionExpression(BaseFunctionExpression funcExpr) {
        addProblem(funcExpr, "Functions are not supported.");
    }

    @Override
    protected void preprocessCastExpression(CastExpression castExpr) {
        CifType ctype = castExpr.getChild().getType();
        CifType rtype = castExpr.getType();
        if (CifTypeUtils.checkTypeCompat(ctype, rtype, RangeCompat.EQUAL)) {
            // Ignore casting to the child type.
            return;
        }

        addProblem(castExpr, "Cast expression are not supported.");
    }
}
