//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.escet.cif.common.CifEvalException;
import org.eclipse.escet.cif.common.CifEvalUtils;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.CifValueUtils;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Assignment;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeReceive;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeSend;
import org.eclipse.escet.cif.metamodel.cif.automata.IfUpdate;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.automata.Update;
import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Constant;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.TypeDecl;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BoolExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IntExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TauExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression;
import org.eclipse.escet.cif.metamodel.cif.functions.Function;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.common.app.framework.exceptions.InvalidInputException;
import org.eclipse.escet.common.app.framework.exceptions.UnsupportedException;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Strings;

/** Class for performing checks whether the specification can be used as input for the CIF to mCRL2 transformation. */
public class Cif2Mcrl2PreChecker {
    /** Found problems in the specification. */
    public List<String> problems = null;

    /** Constructor of the {@link Cif2Mcrl2PreChecker} class. */
    public Cif2Mcrl2PreChecker() {
        // Nothing to do.
    }

    /**
     * Perform checks whether the specification is usable for performing a CIF to mCRL2 transformation.
     *
     * @param spec Specification to check.
     * @throws InvalidInputException If the specification violates the pre-conditions.
     */
    public void checkSpec(Specification spec) {
        problems = list();
        if (!checkGroup(spec)) {
            problems.add("Specification must have at least one automaton.");
        }

        if (problems.isEmpty()) {
            return;
        }
        // If we have any problems, the specification is unsupported.
        Collections.sort(problems, Strings.SORTER);
        if (!problems.isEmpty()) {
            String msg = "CIF to mCRL2 transformation failed due to unsatisfied preconditions:\n - "
                    + StringUtils.join(problems, "\n - ");
            throw new UnsupportedException(msg);
        }
    }

    /**
     * Unfold and check a group.
     *
     * @param grp Group to check and unfold.
     * @return Whether at least one automaton was found in the group.
     */
    private boolean checkGroup(Group grp) {
        // Definitions should be eliminated already.
        Assert.check(grp.getDefinitions().isEmpty());
        checkComponent(grp);

        boolean foundAut = false;
        for (Component c: grp.getComponents()) {
            if (c instanceof Automaton) {
                foundAut = true;
                Automaton aut = (Automaton)c;
                checkAutomaton(aut);
                continue;
            } else if (c instanceof Group) {
                Group g = (Group)c;
                foundAut |= checkGroup(g);
                continue;
            }

            // ComponentInst should not happen, as DefInst has been eliminated.
            throw new RuntimeException("Unexpected type of Component.");
        }
        return foundAut;
    }

    /**
     * Check whether the automaton satisfies all pre-conditions of the CIF to mCRL2 transformation.
     *
     * @param aut Automaton to check.
     */
    private void checkAutomaton(Automaton aut) {
        String msg;

        checkComponent(aut);

        // Check type of the discrete variables.
        for (Declaration decl: aut.getDeclarations()) {
            if (decl instanceof DiscVariable) {
                DiscVariable dv = (DiscVariable)decl;
                CifType tp = CifTypeUtils.normalizeType(dv.getType());
                if (!(tp instanceof BoolType) && !(tp instanceof IntType)) {
                    msg = fmt("Discrete variable \"%s\" does not have a boolean or integer type.",
                            CifTextUtils.getAbsName(dv));
                    problems.add(msg);
                    continue;
                }

                if (dv.getValue().getValues().size() != 1) {
                    msg = fmt("Discrete variable \"%s\" does not have a single initial value.",
                            CifTextUtils.getAbsName(dv));
                    problems.add(msg);
                    continue;
                }

                try {
                    CifEvalUtils.eval(dv.getValue().getValues().get(0), true);
                } catch (CifEvalException err) {
                    msg = fmt("Initial value of discrete variable \"%s\" cannot be evaluated.",
                            CifTextUtils.getAbsName(dv));
                    problems.add(msg);
                    continue;
                }
            }
        }

        // Check locations.
        for (Location loc: aut.getLocations()) {
            String locTextMid = CifTextUtils.getLocationText2(loc);
            String locTextStart = StringUtils.capitalize(locTextMid);

            if (!loc.getInvariants().isEmpty()) {
                msg = locTextStart + " has invariants.";
                problems.add(msg);
            }
            if (!loc.getEquations().isEmpty()) {
                msg = locTextStart + " has equations.";
                problems.add(msg);
            }
            for (Edge edge: loc.getEdges()) {
                for (Update upd: edge.getUpdates()) {
                    if (upd instanceof IfUpdate) {
                        msg = locTextStart + " has conditional updates.";
                        problems.add(msg);
                        continue;
                    }

                    Assignment asg = (Assignment)upd;
                    msg = checkExpression(asg.getValue());
                    if (msg != null) {
                        msg = fmt("A value in %s %s", locTextMid, msg);
                        problems.add(msg);
                    }

                    Expression e = asg.getAddressable();
                    if (!(e instanceof DiscVariableExpression)) {
                        msg = fmt("An assignment in %s assigns to unsupported addressable form \"%s\".", locTextMid,
                                CifTextUtils.exprToStr(e));
                        problems.add(msg);
                    }
                }
                for (Expression e: edge.getGuards()) {
                    msg = checkExpression(e);
                    if (msg != null) {
                        msg = fmt("A guard in %s %s", locTextMid, msg);
                        problems.add(msg);
                    }
                }

                if (edge.getEvents().isEmpty()) {
                    msg = locTextStart + " has a \"tau\" event.";
                    problems.add(msg);
                    continue;
                }
                for (EdgeEvent ee: edge.getEvents()) {
                    if (ee instanceof EdgeSend) {
                        msg = locTextStart + " has a send edge.";
                        problems.add(msg);
                        continue;
                    } else if (ee instanceof EdgeReceive) {
                        msg = locTextStart + " has a receive edge.";
                        problems.add(msg);
                        continue;
                    } else if (ee.getEvent() instanceof TauExpression) {
                        msg = locTextStart + " has a \"tau\" event.";
                        problems.add(msg);
                        continue;
                    }
                    Assert.check(ee.getEvent() instanceof EventExpression);
                }
            }
        }

        boolean foundInitialLocation = false;
        for (Location loc: aut.getLocations()) {
            if (!loc.getInitials().isEmpty() && CifValueUtils.isTriviallyTrue(loc.getInitials(), true, true)) {
                if (foundInitialLocation) {
                    msg = fmt("Automaton \"%s\" has more than one initial location.", CifTextUtils.getAbsName(aut));
                    problems.add(msg);
                }
                foundInitialLocation = true;
            }
        }
        if (!foundInitialLocation) {
            msg = fmt("Automaton \"%s\" has no initial location.", CifTextUtils.getAbsName(aut));
            problems.add(msg);
        }
    }

    /**
     * Check whether the expression supported.
     *
     * @param e Expression to check.
     * @return The last part of an error message if an error was found, else {@code null}.
     */
    private String checkExpression(Expression e) {
        CifType t = CifTypeUtils.normalizeType(e.getType());

        if (t instanceof BoolType) {
            if (e instanceof BoolExpression) {
                return null;
            } else if (e instanceof BinaryExpression) {
                BinaryExpression be = (BinaryExpression)e;
                switch (be.getOperator()) {
                    case CONJUNCTION:
                    case DISJUNCTION:
                    case EQUAL:
                    case GREATER_EQUAL:
                    case GREATER_THAN:
                    case LESS_EQUAL:
                    case LESS_THAN:
                    case UNEQUAL:
                        break;
                    default: {
                        String msg = fmt("has unsupported boolean binary operator \"%s\".",
                                CifTextUtils.operatorToStr(be.getOperator()));
                        return msg;
                    }
                }
                String msg = checkExpression(be.getLeft());
                if (msg == null) {
                    msg = checkExpression(be.getRight());
                }
                return msg;
            } else if (e instanceof UnaryExpression) {
                UnaryExpression ue = (UnaryExpression)e;
                switch (ue.getOperator()) {
                    case INVERSE:
                        break;
                    default: {
                        String msg = fmt("has unsupported boolean unary operator \"%s\".",
                                CifTextUtils.operatorToStr(ue.getOperator()));
                        return msg;
                    }
                }
                return checkExpression(ue.getChild());
            } else if (e instanceof DiscVariableExpression) {
                return null;
            }

            return fmt("has unsupported boolean expression \"%s\".", CifTextUtils.exprToStr(e));
        }

        if (t instanceof IntType) {
            if (e instanceof IntExpression) {
                return null;
            } else if (e instanceof BinaryExpression) {
                BinaryExpression be = (BinaryExpression)e;
                switch (be.getOperator()) {
                    case ADDITION:
                    case MULTIPLICATION:
                    case SUBTRACTION:
                        break;
                    default: {
                        String msg = fmt("has unsupported integer binary operator \"%s\".",
                                CifTextUtils.operatorToStr(be.getOperator()));
                        return msg;
                    }
                }
                String msg = checkExpression(be.getLeft());
                if (msg == null) {
                    msg = checkExpression(be.getRight());
                }
                return msg;
            } else if (e instanceof UnaryExpression) {
                UnaryExpression ue = (UnaryExpression)e;
                switch (ue.getOperator()) {
                    case NEGATE:
                    case PLUS:
                        break;
                    default: {
                        String msg = fmt("has unsupported integer unary operator \"%s\".",
                                CifTextUtils.operatorToStr(ue.getOperator()));
                        return msg;
                    }
                }
                return checkExpression(ue.getChild());
            } else if (e instanceof DiscVariableExpression) {
                return null;
            }

            return fmt("has unsupported integer expression \"%s\".", CifTextUtils.exprToStr(e));
        }

        String msg = CifTextUtils.typeToStr(t);
        return fmt("has an unsupported type \"%s\" in expression \"%s\".", msg, CifTextUtils.exprToStr(e));
    }

    /**
     * Check that only supported declarations exist.
     *
     * @param decls Declarations to inspect.
     */
    private void checkDeclarations(List<Declaration> decls) {
        String msg;

        for (Declaration decl: decls) {
            if (decl instanceof AlgVariable) {
                msg = fmt("Algebraic variable \"%s\" is unsupported in the transformation.",
                        CifTextUtils.getAbsName(decl));
                problems.add(msg);
                continue;
            } else if (decl instanceof Constant) {
                continue;
            } else if (decl instanceof Event) {
                continue;
            } else if (decl instanceof ContVariable) {
                msg = fmt("Continuous variable \"%s\" is unsupported in the transformation.",
                        CifTextUtils.getAbsName(decl));
                problems.add(msg);
                continue;
            } else if (decl instanceof Function) {
                continue;
            } else if (decl instanceof TypeDecl) {
                continue;
            } else if (decl instanceof DiscVariable) {
                continue;
            }

            if (decl instanceof InputVariable) {
                msg = fmt("Input variable \"%s\" is unsupported in the transformation.", CifTextUtils.getAbsName(decl));
                problems.add(msg);
                continue;
            }

            throw new RuntimeException("Unexpected type of CIF declaration.");
        }
    }

    /**
     * Verify that the given component does not have elements that are not supported in the translation.
     *
     * @param comp Component to check.
     */
    private void checkComponent(ComplexComponent comp) {
        String msg;

        // IO declarations should be eliminated already.
        Assert.check(comp.getIoDecls().isEmpty());

        checkDeclarations(comp.getDeclarations());

        if (!comp.getEquations().isEmpty()) {
            msg = fmt("Equations are not supported in %s.", CifTextUtils.getComponentText2(comp));
            problems.add(msg);
        }
        if (!comp.getInitials().isEmpty()) {
            msg = fmt("Initialization predicates are not supported in %s.", CifTextUtils.getComponentText2(comp));
            problems.add(msg);
        }
        if (!comp.getInvariants().isEmpty()) {
            msg = fmt("Invariants are not supported in %s.", CifTextUtils.getComponentText2(comp));
            problems.add(msg);
        }
    }
}
