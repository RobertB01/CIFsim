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

package org.eclipse.escet.cif.cif2cif;

import static org.eclipse.escet.cif.common.CifValueUtils.hasSingleValue;
import static org.eclipse.escet.cif.common.CifValueUtils.isInitialExpr;
import static org.eclipse.escet.cif.common.CifValueUtils.isTriviallyFalse;
import static org.eclipse.escet.cif.common.CifValueUtils.isTriviallyTrue;
import static org.eclipse.escet.cif.common.CifValueUtils.makeFalse;
import static org.eclipse.escet.cif.common.CifValueUtils.makeTrue;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBinaryExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBoolType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newInvariant;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newUnaryExpression;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.escet.cif.common.CifEvalException;
import org.eclipse.escet.cif.common.CifEvalUtils;
import org.eclipse.escet.cif.common.CifScopeUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.CifValueUtils;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.SupKind;
import org.eclipse.escet.cif.metamodel.cif.automata.Assignment;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.ElifUpdate;
import org.eclipse.escet.cif.metamodel.cif.automata.IfUpdate;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.ContVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ElifExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.FieldExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IfExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.InputVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ProjectionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunctionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SwitchCase;
import org.eclipse.escet.cif.metamodel.cif.expressions.SwitchExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TauExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TupleExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryOperator;
import org.eclipse.escet.cif.metamodel.cif.functions.AssignmentFuncStatement;
import org.eclipse.escet.cif.metamodel.java.CifWalker;
import org.eclipse.escet.common.app.framework.exceptions.InvalidModelException;
import org.eclipse.escet.common.emf.EMFHelper;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.Position;

/**
 * In-place transformation that simplifies CIF specifications, by applying the following value-related simplifications:
 * <ul>
 * <li>Evaluate constant (sub-)expressions. For instance, {@code '1 + 1' -> '2'}.</li>
 * <li>Short-circuit simplification for boolean binary operators:
 * <ul>
 * <li>{@code true  and X     -> X}</li>
 * <li>{@code X     and true  -> X}</li>
 * <li>{@code false and X     -> false}</li>
 * <li>{@code X     and false -> false}</li>
 * <li>{@code true  or  X     -> true}</li>
 * <li>{@code X     or  true  -> true}</li>
 * <li>{@code false or  X     -> X}</li>
 * <li>{@code X     or  false -> X}</li>
 * <li>{@code true  =>  X     -> X}</li>
 * <li>{@code X     =>  true  -> true}</li>
 * <li>{@code false =>  X     -> true}</li>
 * <li>{@code X     =>  false -> not X}</li>
 * <li>{@code true  <=> X     -> X}</li>
 * <li>{@code X     <=> true  -> X}</li>
 * <li>{@code false <=> X     -> not X}</li>
 * <li>{@code X     <=> false -> not X}</li>
 * </ul>
 * </li>
 * <li>Simplification for boolean unary operators:
 * <ul>
 * <li>{@code not not X    -> X}</li>
 * <li>{@code not(X < Y)   -> X >= Y}</li>
 * <li>{@code not(X <= Y)  -> X >  Y}</li>
 * <li>{@code not(X = Y)   -> X != Y}</li>
 * <li>{@code not(X != Y)  -> X =  Y}</li>
 * <li>{@code not(X > Y)   -> X <= Y}</li>
 * <li>{@code not(X >= Y)  -> X <  Y}</li>
 * <li>{@code not(X => Y)  -> X and not Y}</li>
 * </ul>
 * </li>
 * <li>Remove default values. For instance, {@code true} invariants may be removed.</li>
 * </ul>
 *
 * <p>
 * Precondition: Specifications with component definitions/instantiations are currently not supported.
 * </p>
 *
 * @see SimplifyOthers
 */
public class SimplifyValues extends CifWalker implements CifToCifTransformation {
    /** Reusable dummy invariant, used to hold expressions being simplified. */
    private Invariant dummyInv = newInvariant();

    /**
     * The last expression that has been in-place replaced by the post-processing function of a derived class of
     * {@link Expression}. Is {@code null} until the first such replacement takes place.
     */
    private Expression lastExpr;

    /**
     * The expression by which {@link #lastExpr} was replaced. Is {@code null} as long as {@link #lastExpr} is
     * {@code null}.
     */
    private Expression newExpr;

    /**
     * Whether to allow simplification of reference expressions, by replacing them by the value of the referred
     * declaration.
     *
     * @see CifValueUtils#hasSingleValue(Expression, boolean, boolean)
     */
    private final boolean simplifyRefs;

    /**
     * Whether to optimize for literal expressions. If enabled, static analysis is used to detect literal expressions.
     * They are not simplified. This prevents expensive evaluation and (re)construction of the literal expression.
     *
     * <p>
     * This performance optimization may result in less than full simplification. For instance, set literals with
     * duplicate elements retain the duplicate elements. This is the result of doing a fast/static literal check. For
     * details, see {@link CifValueUtils#isLiteralExpr}.
     * </p>
     */
    private final boolean optimizeLits;

    /**
     * Constructor for the {@link SimplifyValues} class. Allows simplification of reference expressions and does not
     * optimize for literal expressions.
     */
    public SimplifyValues() {
        this(true, false);
    }

    /**
     * Constructor for the {@link SimplifyValues} class.
     *
     * @param simplifyRefs Whether to allow simplification of reference expressions. For details, see
     *     {@link #simplifyRefs}.
     * @param optimizeLits Whether to optimize for literal expressions. For details, see {@link #optimizeLits}.
     */
    public SimplifyValues(boolean simplifyRefs, boolean optimizeLits) {
        this.simplifyRefs = simplifyRefs;
        this.optimizeLits = optimizeLits;
    }

    @Override
    public void transform(Specification spec) {
        // Check no component definition/instantiation precondition.
        if (CifScopeUtils.hasCompDefInst(spec)) {
            String msg = "Simplifying values of a CIF specification with component definitions is currently not "
                    + "supported.";
            throw new CifToCifPreconditionException(msg);
        }

        // Initialization.
        lastExpr = null;
        newExpr = null;

        // Perform actual transformation.
        walkSpecification(spec);

        // Cleanup.
        lastExpr = null;
        newExpr = null;
    }

    /**
     * Performs value simplification on the given expression.
     *
     * <p>
     * Expressions with wrapping expressions are not supported.
     * </p>
     *
     * <p>
     * The given expression is modified in-place. It may also be replaced by a simplified expression, in its parent
     * containment, if the given expression is contained in a parent.
     * </p>
     *
     * @param expr The expression to simplify.
     * @return The result of simplification, if the given expression did not have a parent, or {@code null} if it did
     *     have a parent.
     */
    public Expression transform(Expression expr) {
        // Initialization.
        lastExpr = null;
        newExpr = null;

        // First put expression in a parent if it has no parent, to allow the
        // expression to be replaced in its parent containment.
        if (expr.eContainer() == null) {
            dummyInv.setPredicate(expr);
        } else {
            dummyInv.setPredicate(null);
        }

        // Perform actual transformation.
        walkExpression(expr);
        Expression rslt = dummyInv.getPredicate();

        // Cleanup.
        dummyInv.setPredicate(null);
        lastExpr = null;
        newExpr = null;

        // Return simplification result, if expression did not have a parent.
        return rslt;
    }

    @Override
    protected void walkAssignment(Assignment obj) {
        // Copied from CifWalker, but modified 'addressable' walking.
        precrawlAssignment(obj);
        Expression _addressable = obj.getAddressable();
        walkAddressable(_addressable); // Different walk method.
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        Expression _value = obj.getValue();
        walkExpression(_value);
        postcrawlAssignment(obj);
    }

    @Override
    protected void walkAssignmentFuncStatement(AssignmentFuncStatement obj) {
        // Copied from CifWalker, but modified 'addressable' walking.
        precrawlAssignmentFuncStatement(obj);
        Expression _addressable = obj.getAddressable();
        walkAddressable(_addressable); // Different walk method.
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        Expression _value = obj.getValue();
        walkExpression(_value);
        postcrawlAssignmentFuncStatement(obj);
    }

    /**
     * Walks over the given addressable expression. This differs from {@link #walkExpression}, since this method does
     * not walk over the references to variables in the expression, as they should remain.
     *
     * @param addr The addressable expression.
     */
    private void walkAddressable(Expression addr) {
        // Handle tuples recursively.
        if (addr instanceof TupleExpression) {
            for (Expression elem: ((TupleExpression)addr).getFields()) {
                walkAddressable(elem);
            }
            return;
        }

        // Walk over index expression of the projections.
        while (addr instanceof ProjectionExpression) {
            ProjectionExpression proj = (ProjectionExpression)addr;
            walkExpression(proj.getIndex());
            addr = proj.getChild();
        }

        // Don't walk over the actual addressable expression.
        Assert.check(addr instanceof DiscVariableExpression || addr instanceof ContVariableExpression
                || addr instanceof InputVariableExpression);
    }

    @Override
    protected void walkSwitchCase(SwitchCase cse) {
        // Detect normal/special case.
        SwitchExpression switchExpr = (SwitchExpression)cse.eContainer();
        Expression value = switchExpr.getValue();
        if (!CifTypeUtils.isAutRefExpr(value)) {
            // Normal case.
            super.walkSwitchCase(cse);
            return;
        }

        // Special case: the key of the case (if not 'null' for 'else'), is a
        // location reference, and must not be 'simplified' for automata with
        // a single location.
        SwitchCase obj = cse;

        // Copied from CifWalker, but disabled 'key' walking.
        precrawlSwitchCase(obj);
        // Expression _key = obj.getKey();
        // if (_key != null) walkExpression(_key);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        Expression _value = obj.getValue();
        walkExpression(_value);
        postcrawlSwitchCase(obj);
    }

    @Override
    protected void postprocessExpression(Expression expr) {
        // If 'expr' was already replaced during other post-processing,
        // by one of the derived classes of Expression, continue with that
        // result instead.
        if (expr == lastExpr) {
            expr = newExpr;
        }

        // Skip expressions not supported by the 'hasSingleValue' function.
        if (expr instanceof FieldExpression) {
            return;
        } else if (expr instanceof StdLibFunctionExpression) {
            return;
        } else if (expr instanceof TauExpression) {
            return;
        } else if (expr instanceof EventExpression) {
            return;
        }

        // Is the expression an 'initial expression'?
        boolean initial = isInitialExpr(expr);

        // Performance optimization for literal expressions. Prevents expensive
        // evaluation and (re)construction of the literal expression. Does
        // however differ from actual evaluation/simplification in some cases,
        // as the literal check is performed statically. For instance, set
        // literals with duplicate elements retain the duplicate elements.
        if (optimizeLits && CifValueUtils.isLiteralExpr(expr)) {
            return;
        }

        // We may only evaluate it, if it has a single value.
        boolean singleValue = hasSingleValue(expr, initial, simplifyRefs);
        if (!singleValue) {
            return;
        }

        // Evaluate as expression.
        Expression rslt;
        try {
            rslt = CifEvalUtils.evalAsExpr(expr, initial);
        } catch (CifEvalException e) {
            String msg = "Failed to simplify an invalid CIF specification.";
            throw new InvalidModelException(msg, e);
        }

        // NOTE: since we have no component definitions/instantiations
        // (transformation precondition), all references are valid, and we
        // don't need to add wrapping expressions/types.

        // Replace expression in parent containment.
        EMFHelper.updateParentContainment(expr, rslt);
    }

    @Override
    protected void postprocessComplexComponent(ComplexComponent comp) {
        simplifyInvs(comp.getInvariants());
        simplifyPreds(comp.getInitials(), true, true);
        simplifyPreds(comp.getMarkeds(), true, true);
    }

    @Override
    protected void postprocessLocation(Location loc) {
        simplifyInvs(loc.getInvariants());
        simplifyPreds(loc.getInitials(), false, true);
        simplifyPreds(loc.getMarkeds(), false, true);
    }

    @Override
    protected void postprocessEdge(Edge edge) {
        simplifyPreds(edge.getGuards(), true, true);
    }

    @Override
    protected void postprocessElifExpression(ElifExpression elif) {
        simplifyPreds(elif.getGuards(), true, false);
    }

    @Override
    protected void postprocessElifUpdate(ElifUpdate elif) {
        simplifyPreds(elif.getGuards(), true, false);
    }

    @Override
    protected void postprocessIfExpression(IfExpression ifExpr) {
        simplifyPreds(ifExpr.getGuards(), true, false);
    }

    @Override
    protected void postprocessIfUpdate(IfUpdate ifUpd) {
        simplifyPreds(ifUpd.getGuards(), true, false);
    }

    /**
     * Simplifies a sequence of predicates with conjunction semantics.
     *
     * @param preds The sequence of predicates to simplify.
     * @param defaultTrue Whether {@code true} is the default value of the feature, in case the feature is an empty
     *     sequence.
     * @param allowEmpty Whether an empty sequence is allowed for the feature.
     */
    private void simplifyPreds(EList<Expression> preds, boolean defaultTrue, boolean allowEmpty) {
        // Remove trivially 'true' predicates from the list.
        Iterator<Expression> predIter = preds.iterator();
        boolean trueRemoved = false;
        while (predIter.hasNext()) {
            // Get expression. Is it an 'initial expression'.
            Expression expr = predIter.next();
            boolean initial = isInitialExpr(expr);

            // Stop on 'false'.
            if (isTriviallyFalse(expr, initial, simplifyRefs)) {
                preds.clear();

                if (allowEmpty && !defaultTrue) {
                    // Empty allowed, and empty is 'false'.
                    return;
                }

                // Empty not allowed, or empty is 'true'.
                preds.add(makeFalse());
                return;
            }

            // Remove 'true' predicates from conjunction.
            if (isTriviallyTrue(expr, initial, simplifyRefs)) {
                trueRemoved = true;
                predIter.remove();
            }
        }

        // Add back 'true', if needed.
        if (trueRemoved && preds.isEmpty()) {
            if (!allowEmpty || !defaultTrue) {
                // Empty not allowed, or empty is allowed but defaults to
                // 'false', so put back single 'true' value.
                preds.add(makeTrue());
            }
        }
    }

    /**
     * Simplifies a sequence of invariants with conjunction semantics.
     *
     * @param invs The sequence of invariants to simplify.
     */
    private void simplifyInvs(EList<Invariant> invs) {
        // Optimize for no invariants.
        if (invs.isEmpty()) {
            return;
        }

        // Remove trivially 'true' invariants from the list, regardless of
        // their supervisory kind.
        Iterator<Invariant> invIter = invs.iterator();
        while (invIter.hasNext()) {
            // Get invariant and predicate.
            Invariant inv = invIter.next();
            Expression pred = inv.getPredicate();

            // Remove 'true' invariants from conjunction.
            switch (inv.getInvKind()) {
                case STATE:
                case EVENT_NEEDS:
                    // invariant true
                    // invariant evt needs true
                    if (isTriviallyTrue(pred, false, simplifyRefs)) {
                        invIter.remove();
                    }
                    break;

                case EVENT_DISABLES:
                    // invariant false disables evt
                    if (isTriviallyFalse(pred, false, simplifyRefs)) {
                        invIter.remove();
                    }
                    break;
            }
        }

        // Create event (or 'null' for state) to supervisory kind to invariants
        // mapping.
        Map<Event, Map<SupKind, List<Invariant>>> evtToSupKindToInvs = map();
        for (Invariant inv: invs) {
            // Get entry for event (or 'null' for state invariant).
            EventExpression eventRef = (EventExpression)inv.getEvent();
            Event event = (eventRef == null) ? null : eventRef.getEvent();
            Map<SupKind, List<Invariant>> supKindToInvs = evtToSupKindToInvs.get(event);
            if (supKindToInvs == null) {
                supKindToInvs = map();
                evtToSupKindToInvs.put(event, supKindToInvs);
            }

            // Get entry for supervisory kind.
            List<Invariant> entryInvs = supKindToInvs.get(inv.getSupKind());
            if (entryInvs == null) {
                entryInvs = list();
                supKindToInvs.put(inv.getSupKind(), entryInvs);
            }
            entryInvs.add(inv);
        }

        // Per event, per supervisory kind, if one of the invariants is
        // trivially 'false', replace all invariants for that event and
        // supervisory kind with a single 'false' invariant.
        boolean changed = false;
        for (Map<SupKind, List<Invariant>> supKindToInvs: evtToSupKindToInvs.values()) {
            for (List<Invariant> supKindInvs: supKindToInvs.values()) {
                INVS:
                for (Invariant inv: supKindInvs) {
                    Expression pred = inv.getPredicate();
                    switch (inv.getInvKind()) {
                        case STATE:
                        case EVENT_NEEDS:
                            // invariant false
                            // invariant evt needs false

                            if (isTriviallyFalse(pred, false, simplifyRefs)) {
                                // Replace invariants by single 'false'.
                                changed = true;

                                inv.setPredicate(makeFalse());

                                supKindInvs.clear();
                                supKindInvs.add(inv);

                                // Proceed with next supervisory kind.
                                break INVS;
                            }

                            // Proceed with next invariant.
                            break;

                        case EVENT_DISABLES:
                            // invariant true disables evt

                            if (isTriviallyTrue(pred, false, simplifyRefs)) {
                                // Replace invariants by single 'true'.
                                changed = true;

                                inv.setPredicate(makeTrue());

                                supKindInvs.clear();
                                supKindInvs.add(inv);

                                // Proceed with next supervisory kind.
                                break INVS;
                            }

                            // Proceed with next invariant.
                            break;
                    }
                }
            }
        }

        // Replace original invariants with new ones. Optimize for no changes.
        if (!changed) {
            return;
        }

        invs.clear();
        for (Map<SupKind, List<Invariant>> supKindToInvs: evtToSupKindToInvs.values()) {
            for (List<Invariant> supKindInvs: supKindToInvs.values()) {
                invs.addAll(supKindInvs);
            }
        }
    }

    @Override
    protected void postprocessBinaryExpression(BinaryExpression bexpr) {
        // Get children. Figure out whether they are 'initial' expressions.
        Expression l = bexpr.getLeft();
        Expression r = bexpr.getRight();

        boolean linit = isInitialExpr(l);
        boolean rinit = isInitialExpr(r);

        // Is any of the children trivially true/false?
        boolean lt = isTriviallyTrue(l, linit, simplifyRefs);
        boolean lf = isTriviallyFalse(l, linit, simplifyRefs);
        boolean rt = isTriviallyTrue(r, rinit, simplifyRefs);
        boolean rf = isTriviallyFalse(r, rinit, simplifyRefs);
        if (!lt && !lf && !rt && !rf) {
            return;
        }

        // Find replacement for this binary expression. Allow for both children
        // to be trivial, and prefer the simplest result in such cases.
        Expression replacement = null;
        switch (bexpr.getOperator()) {
            case CONJUNCTION:
                if (lf) {
                    replacement = makeFalse();
                } else if (rf) {
                    replacement = makeFalse();
                } else if (lt) {
                    replacement = r;
                } else if (rt) {
                    replacement = l;
                }

                break;

            case DISJUNCTION:
                if (lt) {
                    replacement = makeTrue();
                } else if (rt) {
                    replacement = makeTrue();
                } else if (lf) {
                    replacement = r;
                } else if (rf) {
                    replacement = l;
                }

                break;

            case IMPLICATION:
                if (rt) {
                    replacement = makeTrue();
                } else if (lf) {
                    replacement = makeTrue();
                } else if (lt) {
                    replacement = r;
                } else if (rf) {
                    replacement = newUnaryExpression(l, UnaryOperator.INVERSE, null, newBoolType());
                }

                break;

            case BI_CONDITIONAL:
                if (lt) {
                    replacement = r;
                } else if (rt) {
                    replacement = l;
                } else if (lf) {
                    replacement = newUnaryExpression(r, UnaryOperator.INVERSE, null, newBoolType());
                } else if (rf) {
                    replacement = newUnaryExpression(l, UnaryOperator.INVERSE, null, newBoolType());
                }

                break;

            default:
                // Not a boolean binary operator.
                return;
        }

        // Replace binary expression.
        Assert.notNull(replacement);
        EMFHelper.updateParentContainment(bexpr, replacement);

        // Keep track of the replacement, for general expression
        // post-processing in the 'postprocessExpression' method.
        lastExpr = bexpr;
        newExpr = replacement;
    }

    @Override
    protected void postprocessUnaryExpression(UnaryExpression uexpr) {
        Expression replacement = null;

        if (uexpr.getOperator() == UnaryOperator.INVERSE) {
            if (uexpr.getChild() instanceof UnaryExpression) {
                UnaryExpression child = (UnaryExpression)uexpr.getChild();
                if (child.getOperator() == UnaryOperator.INVERSE) {
                    // not not X -> X
                    replacement = child.getChild();
                }
            }

            if (uexpr.getChild() instanceof BinaryExpression) {
                BinaryExpression child = (BinaryExpression)uexpr.getChild();
                switch (child.getOperator()) {
                    case IMPLICATION: {
                        // not(x => y) = not(not x or y) = x and not y
                        UnaryExpression nexpr = newUnaryExpression();
                        nexpr.setOperator(UnaryOperator.INVERSE);
                        nexpr.setType(newBoolType());
                        nexpr.setChild(child.getRight());

                        BinaryExpression bexpr = newBinaryExpression();
                        bexpr.setOperator(BinaryOperator.CONJUNCTION);
                        bexpr.setType(newBoolType());
                        bexpr.setLeft(child.getLeft());
                        bexpr.setRight(nexpr);

                        replacement = bexpr;
                        break;
                    }

                    case LESS_THAN:
                        child.setOperator(BinaryOperator.GREATER_EQUAL);
                        replacement = child;
                        break;

                    case LESS_EQUAL:
                        child.setOperator(BinaryOperator.GREATER_THAN);
                        replacement = child;
                        break;

                    case EQUAL:
                        child.setOperator(BinaryOperator.UNEQUAL);
                        replacement = child;
                        break;

                    case UNEQUAL:
                        child.setOperator(BinaryOperator.EQUAL);
                        replacement = child;
                        break;

                    case GREATER_THAN:
                        child.setOperator(BinaryOperator.LESS_EQUAL);
                        replacement = child;
                        break;

                    case GREATER_EQUAL:
                        child.setOperator(BinaryOperator.LESS_THAN);
                        replacement = child;
                        break;

                    default:
                        // Not a special case.
                }
            }
        }

        // Perform replacement, if any, and keep track of that replacement, for
        // general expression post-processing in the 'postprocessExpression'
        // method.
        if (replacement != null) {
            EMFHelper.updateParentContainment(uexpr, replacement);

            lastExpr = uexpr;
            newExpr = replacement;
        }
    }
}
