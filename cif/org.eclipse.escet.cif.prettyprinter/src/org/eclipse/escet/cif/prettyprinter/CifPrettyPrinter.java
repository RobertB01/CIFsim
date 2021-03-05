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

package org.eclipse.escet.cif.prettyprinter;

import static java.util.Collections.EMPTY_LIST;
import static org.eclipse.escet.cif.common.CifTextUtils.controllabilityToStr;
import static org.eclipse.escet.cif.common.CifTextUtils.escapeIdentifier;
import static org.eclipse.escet.cif.common.CifTextUtils.functionToStr;
import static org.eclipse.escet.cif.common.CifTextUtils.getAssociativity;
import static org.eclipse.escet.cif.common.CifTextUtils.getBindingStrength;
import static org.eclipse.escet.cif.common.CifTextUtils.kindToStr;
import static org.eclipse.escet.cif.common.CifTextUtils.operatorToStr;
import static org.eclipse.escet.common.emf.EMFHelper.deepclone;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Pair.pair;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.escet.cif.common.Associativity;
import org.eclipse.escet.cif.common.CifInvariantUtils;
import org.eclipse.escet.cif.common.CifMath;
import org.eclipse.escet.cif.common.CifScopeUtils;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.CifValueUtils;
import org.eclipse.escet.cif.common.RangeCompat;
import org.eclipse.escet.cif.common.ScopeCache;
import org.eclipse.escet.cif.metamodel.cif.AlgParameter;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.ComponentDef;
import org.eclipse.escet.cif.metamodel.cif.ComponentInst;
import org.eclipse.escet.cif.metamodel.cif.ComponentParameter;
import org.eclipse.escet.cif.metamodel.cif.Equation;
import org.eclipse.escet.cif.metamodel.cif.EventParameter;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.IoDecl;
import org.eclipse.escet.cif.metamodel.cif.LocationParameter;
import org.eclipse.escet.cif.metamodel.cif.Parameter;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.SupKind;
import org.eclipse.escet.cif.metamodel.cif.automata.Alphabet;
import org.eclipse.escet.cif.metamodel.cif.automata.Assignment;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeReceive;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeSend;
import org.eclipse.escet.cif.metamodel.cif.automata.ElifUpdate;
import org.eclipse.escet.cif.metamodel.cif.automata.IfUpdate;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.automata.Monitors;
import org.eclipse.escet.cif.metamodel.cif.automata.Update;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgCopy;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgFile;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgIn;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEvent;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEventIf;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEventIfEntry;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEventSingle;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgMove;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgOut;
import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Constant;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumLiteral;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.TypeDecl;
import org.eclipse.escet.cif.metamodel.cif.expressions.AlgVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.BoolExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CastExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CompInstWrapExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CompParamWrapExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ComponentExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ConstantExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ContVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DictExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DictPair;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ElifExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EnumLiteralExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.FieldExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.FunctionCallExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.FunctionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IfExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.InputVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IntExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ListExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.LocationExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ProjectionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.RealExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ReceivedExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SelfExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SetExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SliceExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunction;
import org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunctionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.StringExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SwitchCase;
import org.eclipse.escet.cif.metamodel.cif.expressions.SwitchExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TauExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TimeExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TupleExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression;
import org.eclipse.escet.cif.metamodel.cif.functions.AssignmentFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.BreakFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.ContinueFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.ElifFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.ExternalFunction;
import org.eclipse.escet.cif.metamodel.cif.functions.Function;
import org.eclipse.escet.cif.metamodel.cif.functions.FunctionParameter;
import org.eclipse.escet.cif.metamodel.cif.functions.FunctionStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.IfFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.InternalFunction;
import org.eclipse.escet.cif.metamodel.cif.functions.ReturnFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.WhileFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.print.Print;
import org.eclipse.escet.cif.metamodel.cif.print.PrintFile;
import org.eclipse.escet.cif.metamodel.cif.print.PrintFor;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.CompInstWrapType;
import org.eclipse.escet.cif.metamodel.cif.types.CompParamWrapType;
import org.eclipse.escet.cif.metamodel.cif.types.ComponentDefType;
import org.eclipse.escet.cif.metamodel.cif.types.ComponentType;
import org.eclipse.escet.cif.metamodel.cif.types.DictType;
import org.eclipse.escet.cif.metamodel.cif.types.DistType;
import org.eclipse.escet.cif.metamodel.cif.types.EnumType;
import org.eclipse.escet.cif.metamodel.cif.types.Field;
import org.eclipse.escet.cif.metamodel.cif.types.FuncType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.cif.metamodel.cif.types.ListType;
import org.eclipse.escet.cif.metamodel.cif.types.RealType;
import org.eclipse.escet.cif.metamodel.cif.types.SetType;
import org.eclipse.escet.cif.metamodel.cif.types.StringType;
import org.eclipse.escet.cif.metamodel.cif.types.TupleType;
import org.eclipse.escet.cif.metamodel.cif.types.TypeRef;
import org.eclipse.escet.cif.metamodel.cif.types.VoidType;
import org.eclipse.escet.common.box.Box;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.box.MemoryCodeBox;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Pair;
import org.eclipse.escet.common.java.Strings;

/**
 * CIF pretty printer.
 *
 * <p>
 * The CIF pretty printer is to be used to convert CIF specifications to their CIF ASCII representation. To convert CIF
 * expressions to a textual representation that closely resembles the CIF ASCII syntax, for instance for error message
 * or unsupported messages, see {@link CifTextUtils#exprToStr}. To convert CIF types to a textual representation that
 * closely resembles the CIF ASCII syntax, for instance for error message or unsupported messages, see
 * {@link CifTextUtils#typeToStr}.
 * </p>
 */
public final class CifPrettyPrinter {
    /** The indentation level (in number of spaces) to use. */
    public static final int INDENT = 2;

    /**
     * Cache results of the {@link CifScopeUtils#getSymbolNamesForScope} method. Is modified in-place during pretty
     * printing.
     */
    private final ScopeCache scopeCache = new ScopeCache();

    /**
     * Mapping from scopes and events to a reference text for the event, referred to from that scope. This cache is
     * mostly used to reduce the amount of work needed for events on edges, for synthesized event-based supervisors. Is
     * modified in-place during pretty printing.
     */
    private final Map<Pair<EObject, Event>, String> eventRefCache = map();

    /** The code box in which to generate CIF code. Is modified in-place. */
    private final CodeBox code;

    /**
     * Constructor of the {@link CifPrettyPrinter} class.
     *
     * <p>
     * It is generally recommended to use one of the {@link #boxSpec} methods instead of using this method.
     * </p>
     *
     * @param code The code box in which to generate CIF code. Is modified in-place.
     */
    public CifPrettyPrinter(CodeBox code) {
        this.code = code;
    }

    /**
     * Box the given specification.
     *
     * @param spec The specification.
     * @return The pretty printed result as a {@link MemoryCodeBox}.
     */
    public static Box boxSpec(Specification spec) {
        return boxSpec(spec, new MemoryCodeBox(INDENT));
    }

    /**
     * Box the given specification.
     *
     * @param spec The specification.
     * @param code The code box in which to generate CIF code. Is modified in-place.
     * @return The pretty printed result in the code box given by the 'code' parameter.
     */
    public static Box boxSpec(Specification spec, CodeBox code) {
        // Backup old indentation amount and set the default amount for CIF.
        int oldIndentAmount = code.getIndentAmount();
        code.setIndentAmount(INDENT);

        // Pretty print the CIF specification.
        CifPrettyPrinter cpp = new CifPrettyPrinter(code);
        cpp.add(spec);

        // Restore the indentation amount, and return the code.
        code.setIndentAmount(oldIndentAmount);
        return code;
    }

    /**
     * Add the given specification to the pretty printed code.
     *
     * @param spec The specification.
     */
    public void add(Specification spec) {
        addCompBody(spec.getDeclarations(), spec.getDefinitions(), spec.getComponents(), spec.getInitials(),
                spec.getInvariants(), spec.getEquations(), spec.getMarkeds(), spec.getIoDecls());
    }

    /**
     * Add the body of a component or component definition to the pretty printed code.
     *
     * @param decls The declarations of the body.
     * @param cdefs The child component definitions of the body.
     * @param comps The child components of the body.
     * @param initials The initialization predicates of the body.
     * @param invs The invariants of the body.
     * @param eqns The equations of the body.
     * @param markeds The marker predicates of the body.
     * @param ioDecls The I/O declarations of the body.
     */
    public void addCompBody(List<Declaration> decls, List<ComponentDef> cdefs, List<Component> comps,
            List<Expression> initials, List<Invariant> invs, List<Equation> eqns, List<Expression> markeds,
            List<IoDecl> ioDecls)
    {
        for (Declaration decl: decls) {
            add(decl);
        }

        for (ComponentDef cdef: cdefs) {
            add(cdef);
        }

        for (Component comp: comps) {
            add(comp);
        }

        addInitInvEqnsMarked(initials, invs, eqns, markeds, false);

        for (IoDecl ioDecl: ioDecls) {
            add(ioDecl);
        }
    }

    /**
     * Add initialization, invariant, and marker predicates to the pretty printed code.
     *
     * @param initials The initialization predicates.
     * @param invs The invariants.
     * @param eqns The equations.
     * @param markeds The marker predicates.
     * @param optInitMarked {@code true} if initials and markeds may be specified without predicates, and are thus
     *     optional.
     */
    public void addInitInvEqnsMarked(List<Expression> initials, List<Invariant> invs, List<Equation> eqns,
            List<Expression> markeds, boolean optInitMarked)
    {
        if (!initials.isEmpty()) {
            if (optInitMarked && initials.size() == 1 && CifValueUtils.isTriviallyTrue(initials.get(0), false, false)) {
                // The check for trivially true does not optimize for
                // declarations and initial state, to keep as much of the
                // specification intact as possible. Pretty printing should
                // not transform the specification.
                code.add("initial;");
            } else {
                for (int i = 0; i < initials.size(); i++) {
                    String prefix = (i == 0) ? "initial " : "        ";
                    String postfix = (i == initials.size() - 1) ? ";" : ",";
                    code.add(prefix + pprint(initials.get(i)) + postfix);
                }
            }
        }

        if (!markeds.isEmpty()) {
            if (optInitMarked && markeds.size() == 1 && CifValueUtils.isTriviallyTrue(markeds.get(0), false, false)) {
                // The check for trivially true does not optimize for
                // declarations, to keep as much of the specification intact
                // as possible. Pretty printing should not transform the
                // specification.
                code.add("marked;");
            } else {
                for (int i = 0; i < markeds.size(); i++) {
                    String prefix = (i == 0) ? "marked " : "       ";
                    String postfix = (i == markeds.size() - 1) ? ";" : ",";
                    code.add(prefix + pprint(markeds.get(i)) + postfix);
                }
            }
        }

        if (!invs.isEmpty()) {
            for (Invariant inv: invs) {
                StringBuilder line = new StringBuilder();
                SupKind kind = CifInvariantUtils.getSupKind(inv);
                if (kind != SupKind.NONE) {
                    // Use explicit kind, as implicit kind is deprecated.
                    line.append(kindToStr(kind));
                    line.append(" ");
                }
                line.append("invariant ");
                switch (inv.getInvKind()) {
                    case STATE:
                        line.append(pprint(inv.getPredicate()));
                        break;
                    case EVENT_DISABLES:
                        line.append(pprint(inv.getPredicate()));
                        line.append(" disables ");
                        line.append(pprint(inv.getEvent()));
                        break;
                    case EVENT_NEEDS:
                        line.append(pprint(inv.getEvent()));
                        line.append(" needs ");
                        line.append(pprint(inv.getPredicate()));
                        break;
                }
                line.append(";");
                code.add(line.toString());
            }
        }

        if (!eqns.isEmpty()) {
            for (int i = 0; i < eqns.size(); i++) {
                Equation eqn = eqns.get(i);
                StringBuilder line = new StringBuilder();
                line.append((i == 0) ? "equation " : "         ");
                line.append(escapeIdentifier(eqn.getVariable().getName()));
                if (eqn.isDerivative()) {
                    line.append("'");
                }
                line.append(" = ");
                line.append(pprint(eqn.getValue()));
                line.append((i == eqns.size() - 1) ? ";" : ",");
                code.add(line.toString());
            }
        }
    }

    /**
     * Add the given declaration to the pretty printed code.
     *
     * @param decl The declaration.
     */
    public void add(Declaration decl) {
        if (decl instanceof AlgVariable) {
            add((AlgVariable)decl);
        } else if (decl instanceof InputVariable) {
            add((InputVariable)decl);
        } else if (decl instanceof Constant) {
            add((Constant)decl);
        } else if (decl instanceof ContVariable) {
            add((ContVariable)decl);
        } else if (decl instanceof Function) {
            add((Function)decl);
        } else if (decl instanceof DiscVariable) {
            // Not a local variable of a function or a function parameter.
            add((DiscVariable)decl);
        } else if (decl instanceof EnumDecl) {
            add((EnumDecl)decl);
        } else if (decl instanceof Event) {
            add((Event)decl);
        } else if (decl instanceof TypeDecl) {
            add((TypeDecl)decl);
        } else {
            throw new RuntimeException("Unknown decl: " + decl);
        }
    }

    /**
     * Add the given algebraic variable to the pretty printed code.
     *
     * @param var The variable.
     */
    public void add(AlgVariable var) {
        StringBuilder line = new StringBuilder();
        line.append("alg ");
        line.append(pprint(var.getType()));
        line.append(" ");
        line.append(escapeIdentifier(var.getName()));
        if (var.getValue() != null) {
            line.append(" = ");
            line.append(pprint(var.getValue()));
        }
        line.append(";");
        code.add(line.toString());
    }

    /**
     * Add the given input variable to the pretty printed code.
     *
     * @param var The variable.
     */
    public void add(InputVariable var) {
        code.add("input %s %s;", pprint(var.getType()), escapeIdentifier(var.getName()));
    }

    /**
     * Add the given continuous variable to the pretty printed code.
     *
     * @param var The variable.
     */
    public void add(ContVariable var) {
        StringBuilder txt = new StringBuilder();
        txt.append("cont ");
        txt.append(escapeIdentifier(var.getName()));
        if (var.getValue() != null) {
            txt.append(" = ");
            txt.append(pprint(var.getValue()));
        }
        if (var.getDerivative() != null) {
            txt.append(" der ");
            txt.append(pprint(var.getDerivative()));
        }
        txt.append(";");
        code.add(txt.toString());
    }

    /**
     * Add the given constant to the pretty printed code.
     *
     * @param constant The constant.
     */
    public void add(Constant constant) {
        StringBuilder txt = new StringBuilder();
        txt.append("const ");
        txt.append(pprint(constant.getType()));
        txt.append(" ");
        txt.append(escapeIdentifier(constant.getName()));
        txt.append(" = ");
        txt.append(pprint(constant.getValue()));
        txt.append(";");
        code.add(txt.toString());
    }

    /**
     * Add the given user-defined function to the pretty printed code.
     *
     * @param function The function.
     */
    public void add(Function function) {
        // Create header.
        List<String> paramTxts = listc(function.getParameters().size());
        for (FunctionParameter param: function.getParameters()) {
            paramTxts.add(
                    pprint(param.getParameter().getType()) + " " + escapeIdentifier(param.getParameter().getName()));
        }
        String header = fmt("func %s %s(%s):", pprintTypes(function.getReturnTypes(), ", "),
                escapeIdentifier(function.getName()), StringUtils.join(paramTxts, "; "));

        // Add header and body.
        if (function instanceof ExternalFunction) {
            String funcRef = ((ExternalFunction)function).getFunction();
            funcRef = Strings.escape(funcRef);
            code.add("%s \"%s\";", header, funcRef);
        } else {
            InternalFunction ifunction = (InternalFunction)function;
            code.add(header);
            code.indent();

            for (DiscVariable var: ifunction.getVariables()) {
                addFunctionVar(var);
            }
            for (FunctionStatement stat: ifunction.getStatements()) {
                add(stat);
            }

            code.dedent();
            code.add("end");
        }
    }

    /**
     * Add the given local variable of an internal user-defined function to the pretty printed code.
     *
     * @param var The variable.
     */
    public void addFunctionVar(DiscVariable var) {
        StringBuilder txt = new StringBuilder();
        txt.append(pprint(var.getType()));
        txt.append(" ");
        txt.append(escapeIdentifier(var.getName()));
        if (var.getValue() != null) {
            Assert.check(var.getValue().getValues().size() == 1);
            txt.append(" = ");
            txt.append(pprint(var.getValue().getValues().get(0)));
        }
        txt.append(";");
        code.add(txt.toString());
    }

    /**
     * Add the given statement of an internal user-defined function to the pretty printed code.
     *
     * @param stat The statement.
     */
    public void add(FunctionStatement stat) {
        if (stat instanceof AssignmentFuncStatement) {
            AssignmentFuncStatement asgn = (AssignmentFuncStatement)stat;
            StringBuilder txt = new StringBuilder();

            if (asgn.getAddressable() instanceof TupleExpression) {
                TupleExpression tuple = (TupleExpression)asgn.getAddressable();
                txt.append(pprint(tuple.getFields(), ", "));
            } else {
                txt.append(pprint(asgn.getAddressable()));
            }

            txt.append(" := ");

            if (asgn.getValue() instanceof TupleExpression) {
                TupleExpression tuple = (TupleExpression)asgn.getValue();
                txt.append(pprint(tuple.getFields(), ", "));
            } else {
                txt.append(pprint(asgn.getValue()));
            }

            txt.append(";");
            code.add(txt.toString());
        } else if (stat instanceof BreakFuncStatement) {
            code.add("break;");
        } else if (stat instanceof ContinueFuncStatement) {
            code.add("continue;");
        } else if (stat instanceof IfFuncStatement) {
            IfFuncStatement ifStat = (IfFuncStatement)stat;

            code.add("if %s:", pprint(ifStat.getGuards(), ", "));
            code.indent();
            for (FunctionStatement bodyStat: ifStat.getThens()) {
                add(bodyStat);
            }
            code.dedent();

            for (ElifFuncStatement elif: ifStat.getElifs()) {
                code.add("elif %s:", pprint(elif.getGuards(), ", "));
                code.indent();
                for (FunctionStatement bodyStat: elif.getThens()) {
                    add(bodyStat);
                }
                code.dedent();
            }

            if (!ifStat.getElses().isEmpty()) {
                code.add("else");
                code.indent();
                for (FunctionStatement bodyStat: ifStat.getElses()) {
                    add(bodyStat);
                }
                code.dedent();
            }

            code.add("end");
        } else if (stat instanceof ReturnFuncStatement) {
            List<Expression> values = ((ReturnFuncStatement)stat).getValues();
            code.add("return %s;", pprint(values, ", "));
        } else if (stat instanceof WhileFuncStatement) {
            WhileFuncStatement wstat = (WhileFuncStatement)stat;

            code.add("while %s:", pprint(wstat.getGuards(), ", "));
            code.indent();
            for (FunctionStatement bodyStat: wstat.getStatements()) {
                add(bodyStat);
            }
            code.dedent();
            code.add("end");
        } else {
            throw new RuntimeException("Unknown function statement: " + stat);
        }
    }

    /**
     * Add the given enumeration declaration to the pretty printed code.
     *
     * @param enumDecl The enumeration declaration.
     */
    public void add(EnumDecl enumDecl) {
        List<String> names = listc(enumDecl.getLiterals().size());
        for (EnumLiteral lit: enumDecl.getLiterals()) {
            names.add(escapeIdentifier(lit.getName()));
        }

        code.add("enum %s = %s;", escapeIdentifier(enumDecl.getName()), StringUtils.join(names, ", "));
    }

    /**
     * Add the given event to the pretty printed code.
     *
     * @param event The event.
     */
    public void add(Event event) {
        String typeTxt = (event.getType() == null) ? "" : pprint(event.getType()) + " ";
        code.add("%s %s%s;", controllabilityToStr(event.getControllable()), typeTxt, escapeIdentifier(event.getName()));
    }

    /**
     * Add the given type declaration to the pretty printed code.
     *
     * @param typeDecl The type declaration.
     */
    public void add(TypeDecl typeDecl) {
        code.add("type %s = %s;", escapeIdentifier(typeDecl.getName()), pprint(typeDecl.getType()));
    }

    /**
     * Add the given discrete variable to the pretty printed code.
     *
     * @param var The discrete variable. Must not be a local variable of a function or a function parameter.
     */
    public void add(DiscVariable var) {
        StringBuilder txt = new StringBuilder();
        txt.append("disc ");
        txt.append(pprint(var.getType()));
        txt.append(" ");
        txt.append(escapeIdentifier(var.getName()));
        if (var.getValue() != null) {
            if (var.getValue().getValues().isEmpty()) {
                txt.append(" in any");
            } else if (var.getValue().getValues().size() == 1) {
                txt.append(" = ");
                txt.append(pprint(var.getValue().getValues().get(0)));
            } else {
                txt.append(" in ");
                txt.append(pprint(var.getValue().getValues(), "{", ", ", "}"));
            }
        }
        txt.append(";");
        code.add(txt.toString());
    }

    /**
     * Add the given component definition to the pretty printed code.
     *
     * @param cdef The component definition.
     */
    public void add(ComponentDef cdef) {
        ComplexComponent compBody = cdef.getBody();
        boolean isAut = compBody instanceof Automaton;

        // Add header.
        List<String> paramTxts = listc(cdef.getParameters().size());
        for (Parameter param: cdef.getParameters()) {
            paramTxts.add(pprint(param));
        }

        String kindTxt;
        if (isAut) {
            SupKind kind = ((Automaton)compBody).getKind();
            kindTxt = "automaton";
            if (kind != SupKind.NONE) {
                kindTxt = kindToStr(kind) + " " + kindTxt;
            }
        } else {
            kindTxt = "group";
        }
        code.add("%s def %s(%s):", kindTxt, escapeIdentifier(compBody.getName()), StringUtils.join(paramTxts, "; "));
        code.indent();

        // Add body.
        if (isAut) {
            Automaton aut = (Automaton)compBody;
            addAutBody(aut.getAlphabet(), aut.getMonitors(), aut.getLocations(), aut.getDeclarations(),
                    aut.getInitials(), aut.getInvariants(), aut.getEquations(), aut.getMarkeds(), aut.getIoDecls());
        } else {
            Assert.check(compBody instanceof Group);
            Group group = (Group)compBody;
            addCompBody(group.getDeclarations(), group.getDefinitions(), group.getComponents(), group.getInitials(),
                    group.getInvariants(), group.getEquations(), group.getMarkeds(), group.getIoDecls());
        }

        // Add end.
        code.dedent();
        code.add("end");
    }

    /**
     * Pretty print the given formal parameter.
     *
     * @param param The formal parameter.
     * @return The pretty printed result.
     */
    public String pprint(Parameter param) {
        if (param instanceof ComponentParameter) {
            return pprint((ComponentParameter)param);
        } else if (param instanceof EventParameter) {
            return pprint((EventParameter)param);
        } else if (param instanceof LocationParameter) {
            return pprint((LocationParameter)param);
        } else if (param instanceof AlgParameter) {
            return pprint((AlgParameter)param);
        } else {
            throw new RuntimeException("Unknown formal parameter: " + param);
        }
    }

    /**
     * Pretty print the given component parameter.
     *
     * @param param The component parameter.
     * @return The pretty printed result.
     */
    public String pprint(ComponentParameter param) {
        return pprint(param.getType()) + " " + escapeIdentifier(param.getName());
    }

    /**
     * Pretty print the given event parameter.
     *
     * @param param The event parameter.
     * @return The pretty printed result.
     */
    public String pprint(EventParameter param) {
        Event event = param.getEvent();
        String typeTxt = (event.getType() == null) ? "" : pprint(event.getType()) + " ";
        String flagsTxt = "";
        if (param.isSendFlag()) {
            flagsTxt += "!";
        }
        if (param.isRecvFlag()) {
            flagsTxt += "?";
        }
        if (param.isSyncFlag()) {
            flagsTxt += "~";
        }
        return fmt("%s %s%s%s", controllabilityToStr(event.getControllable()), typeTxt,
                escapeIdentifier(event.getName()), flagsTxt);
    }

    /**
     * Pretty print the given location parameter.
     *
     * @param param The location parameter.
     * @return The pretty printed result.
     */
    public String pprint(LocationParameter param) {
        Location loc = param.getLocation();
        return "location " + escapeIdentifier(loc.getName());
    }

    /**
     * Pretty print the given algebraic parameter.
     *
     * @param param The algebraic parameter.
     * @return The pretty printed result.
     */
    public String pprint(AlgParameter param) {
        AlgVariable var = param.getVariable();
        return fmt("alg %s %s", pprint(var.getType()), escapeIdentifier(var.getName()));
    }

    /**
     * Add the given component to the pretty printed code.
     *
     * @param comp The component.
     */
    public void add(Component comp) {
        if (comp instanceof ComponentInst) {
            add((ComponentInst)comp);
        } else if (comp instanceof Automaton) {
            add((Automaton)comp);
        } else if (comp instanceof Group) {
            Assert.check(!(comp instanceof Specification));
            add((Group)comp);
        } else {
            throw new RuntimeException("Unknown component: " + comp);
        }
    }

    /**
     * Add the given component instantiation to the pretty printed code.
     *
     * @param inst The component instantiation.
     */
    public void add(ComponentInst inst) {
        code.add("%s: %s%s;", escapeIdentifier(inst.getName()), pprint(inst.getDefinition()),
                pprint(inst.getParameters(), "(", ", ", ")"));
    }

    /**
     * Add the given automaton to the pretty printed code.
     *
     * @param aut The automaton.
     */
    public void add(Automaton aut) {
        SupKind kind = aut.getKind();
        String kindTxt = "automaton";
        if (kind != SupKind.NONE) {
            kindTxt = kindToStr(kind) + " " + kindTxt;
        }
        code.add("%s %s:", kindTxt, escapeIdentifier(aut.getName()));
        code.indent();

        addAutBody(aut.getAlphabet(), aut.getMonitors(), aut.getLocations(), aut.getDeclarations(), aut.getInitials(),
                aut.getInvariants(), aut.getEquations(), aut.getMarkeds(), aut.getIoDecls());

        code.dedent();
        code.add("end");
    }

    /**
     * Add the body of an automaton to the pretty printed code.
     *
     * @param alpha The alphabet of the automaton, or {@code null}.
     * @param monitors The monitor events of the automaton, or {@code null}.
     * @param locs The locations of the automaton.
     * @param decls The declarations of the automaton.
     * @param initials The initialization predicates of the automaton.
     * @param invs The invariants of the automaton.
     * @param eqns The equations of the automaton.
     * @param markeds The marker predicates of the automaton.
     * @param ioDecls The I/O declarations of the body.
     */
    public void addAutBody(Alphabet alpha, Monitors monitors, List<Location> locs, List<Declaration> decls,
            List<Expression> initials, List<Invariant> invs, List<Equation> eqns, List<Expression> markeds,
            List<IoDecl> ioDecls)
    {
        add(alpha);
        add(monitors);

        addCompBody(decls, EMPTY_LIST, EMPTY_LIST, initials, invs, eqns, markeds, ioDecls);

        for (Location loc: locs) {
            add(loc);
        }
    }

    /**
     * Add the alphabet of an automaton to the pretty printed code.
     *
     * @param alpha The alphabet of the automaton, or {@code null}.
     */
    public void add(Alphabet alpha) {
        if (alpha != null) {
            if (alpha.getEvents().isEmpty()) {
                code.add("alphabet;");
            } else {
                code.add("alphabet %s;", pprint(alpha.getEvents(), ", "));
            }
        }
    }

    /**
     * Add the monitor events of an automaton to the pretty printed code.
     *
     * @param monitors The monitor events of the automaton, or {@code null}.
     */
    public void add(Monitors monitors) {
        if (monitors != null) {
            if (monitors.getEvents().isEmpty()) {
                code.add("monitor;");
            } else {
                code.add("monitor %s;", pprint(monitors.getEvents(), ", "));
            }
        }
    }

    /**
     * Add the given group to the pretty printed code.
     *
     * @param group The group.
     */
    public void add(Group group) {
        code.add("group %s:", escapeIdentifier(group.getName()));
        code.indent();

        addCompBody(group.getDeclarations(), group.getDefinitions(), group.getComponents(), group.getInitials(),
                group.getInvariants(), group.getEquations(), group.getMarkeds(), group.getIoDecls());

        code.dedent();
        code.add("end");
    }

    /**
     * Add the given location to the pretty printed code.
     *
     * @param loc The location.
     */
    public void add(Location loc) {
        boolean isEmpty = loc.getEdges().isEmpty() && loc.getInitials().isEmpty() && loc.getInvariants().isEmpty()
                && loc.getEquations().isEmpty() && loc.getMarkeds().isEmpty() && !loc.isUrgent();

        String header;
        if (loc.getName() == null) {
            header = "location";
        } else {
            header = "location " + escapeIdentifier(loc.getName());
        }

        if (isEmpty) {
            code.add(header + ";");
            return;
        }

        code.add(header + ":");
        code.indent();

        addInitInvEqnsMarked(loc.getInitials(), loc.getInvariants(), loc.getEquations(), loc.getMarkeds(), true);

        if (loc.isUrgent()) {
            code.add("urgent;");
        }

        for (Edge edge: loc.getEdges()) {
            add(edge);
        }

        code.dedent();
    }

    /**
     * Add the given edge to the pretty printed code.
     *
     * @param edge The edge.
     */
    public void add(Edge edge) {
        StringBuilder txt = new StringBuilder();
        txt.append("edge ");

        boolean empty = true;

        if (!edge.getEvents().isEmpty()) {
            txt.append(pprintEdgeEvents(edge.getEvents()));
            empty = false;
        }

        if (!edge.getGuards().isEmpty()) {
            if (!empty) {
                txt.append(" ");
            }
            txt.append("when ");
            txt.append(pprint(edge.getGuards(), ", "));
            empty = false;
        }

        if (edge.isUrgent()) {
            if (!empty) {
                txt.append(" ");
            }
            txt.append("now");
            empty = false;
        }

        if (!edge.getUpdates().isEmpty()) {
            if (!empty) {
                txt.append(" ");
            }
            txt.append("do ");
            txt.append(pprintUpdates(edge.getUpdates()));
            empty = false;
        }

        if (empty) {
            txt.append("tau");
        }

        if (edge.getTarget() != null) {
            txt.append(" goto ");
            txt.append(escapeIdentifier(edge.getTarget().getName()));
        }

        txt.append(";");
        code.add(txt.toString());
    }

    /**
     * Pretty prints the given edge events.
     *
     * @param edgeEvents The edge events.
     * @return The pretty printed result.
     */
    public String pprintEdgeEvents(List<EdgeEvent> edgeEvents) {
        StringBuilder txt = new StringBuilder();
        boolean first = true;
        for (EdgeEvent edgeEvent: edgeEvents) {
            if (!first) {
                txt.append(", ");
            }
            first = false;
            txt.append(pprint(edgeEvent));
        }
        return txt.toString();
    }

    /**
     * Pretty prints the given edge event.
     *
     * @param edgeEvent The edge event.
     * @return The pretty printed result.
     */
    public String pprint(EdgeEvent edgeEvent) {
        StringBuilder txt = new StringBuilder();
        txt.append(pprint(edgeEvent.getEvent()));

        if (edgeEvent instanceof EdgeSend) {
            EdgeSend edgeSend = (EdgeSend)edgeEvent;
            txt.append("!");
            if (edgeSend.getValue() != null) {
                txt.append(pprint(edgeSend.getValue()));
            }
        } else if (edgeEvent instanceof EdgeReceive) {
            txt.append("?");
        }

        return txt.toString();
    }

    /**
     * Pretty prints the given updates.
     *
     * @param updates The updates.
     * @return The pretty printed result.
     */
    public String pprintUpdates(List<Update> updates) {
        StringBuilder txt = new StringBuilder();
        boolean first = true;
        for (Update update: updates) {
            if (!first) {
                txt.append(", ");
            }
            first = false;
            txt.append(pprint(update));
        }
        return txt.toString();
    }

    /**
     * Add the given I/O declaration to the pretty printed code.
     *
     * @param ioDecl The I/O declaration.
     */
    public void add(IoDecl ioDecl) {
        if (ioDecl instanceof SvgFile) {
            add((SvgFile)ioDecl);
        } else if (ioDecl instanceof SvgCopy) {
            add((SvgCopy)ioDecl);
        } else if (ioDecl instanceof SvgMove) {
            add((SvgMove)ioDecl);
        } else if (ioDecl instanceof SvgOut) {
            add((SvgOut)ioDecl);
        } else if (ioDecl instanceof SvgIn) {
            add((SvgIn)ioDecl);
        } else if (ioDecl instanceof PrintFile) {
            add((PrintFile)ioDecl);
        } else if (ioDecl instanceof Print) {
            add((Print)ioDecl);
        } else {
            throw new RuntimeException("Unknown I/O decl: " + ioDecl);
        }
    }

    /**
     * Add the given CIF/SVG file declaration to the pretty printed code. Must only be used for file declarations in
     * components, not for file declarations in other CIF/SVG declarations.
     *
     * @param svgFile The CIF/SVG file declaration.
     */
    public void add(SvgFile svgFile) {
        String path = svgFile.getPath();
        path = Strings.escape(path);
        code.add("svgfile \"%s\";", path);
    }

    /**
     * Add the given CIF/SVG copy declaration to the pretty printed code.
     *
     * @param svgCopy The CIF/SVG copy declaration.
     */
    public void add(SvgCopy svgCopy) {
        StringBuilder txt = new StringBuilder();
        txt.append("svgcopy id ");
        txt.append(pprint(svgCopy.getId()));
        if (svgCopy.getPre() != null) {
            txt.append(" pre ");
            txt.append(pprint(svgCopy.getPre()));
        }
        if (svgCopy.getPost() != null) {
            txt.append(" post ");
            txt.append(pprint(svgCopy.getPost()));
        }
        if (svgCopy.getSvgFile() != null) {
            String path = svgCopy.getSvgFile().getPath();
            path = Strings.escape(path);
            txt.append(fmt(" file \"%s\"", path));
        }
        txt.append(";");
        code.add(txt.toString());
    }

    /**
     * Add the given CIF/SVG move declaration to the pretty printed code.
     *
     * @param svgMove The CIF/SVG move declaration.
     */
    public void add(SvgMove svgMove) {
        StringBuilder txt = new StringBuilder();
        txt.append("svgmove id ");
        txt.append(pprint(svgMove.getId()));
        txt.append(" to ");
        txt.append(pprint(svgMove.getX()));
        txt.append(", ");
        txt.append(pprint(svgMove.getY()));
        if (svgMove.getSvgFile() != null) {
            String path = svgMove.getSvgFile().getPath();
            path = Strings.escape(path);
            txt.append(fmt(" file \"%s\"", path));
        }
        txt.append(";");
        code.add(txt.toString());
    }

    /**
     * Add the given CIF/SVG output mapping to the pretty printed code.
     *
     * @param svgOut The CIF/SVG output mapping.
     */
    public void add(SvgOut svgOut) {
        StringBuilder txt = new StringBuilder();
        txt.append("svgout id ");
        txt.append(pprint(svgOut.getId()));
        if (svgOut.getAttr() == null) {
            txt.append(" text");
        } else {
            txt.append(fmt(" attr \"%s\"", Strings.escape(svgOut.getAttr())));
        }
        txt.append(" value ");
        txt.append(pprint(svgOut.getValue()));
        if (svgOut.getSvgFile() != null) {
            String path = svgOut.getSvgFile().getPath();
            path = Strings.escape(path);
            txt.append(fmt(" file \"%s\"", path));
        }
        txt.append(";");
        code.add(txt.toString());
    }

    /**
     * Add the given CIF/SVG input mapping to the pretty printed code.
     *
     * @param svgIn The CIF/SVG input mapping.
     */
    public void add(SvgIn svgIn) {
        StringBuilder txt = new StringBuilder();
        txt.append("svgin id ");
        txt.append(pprint(svgIn.getId()));
        txt.append(" event ");

        SvgInEvent event = svgIn.getEvent();
        if (event instanceof SvgInEventSingle) {
            SvgInEventSingle single = (SvgInEventSingle)event;
            txt.append(pprint(single.getEvent()));
        } else if (event instanceof SvgInEventIf) {
            SvgInEventIf ifEvent = (SvgInEventIf)event;
            List<SvgInEventIfEntry> entries = ifEvent.getEntries();
            for (int i = 0; i < entries.size(); i++) {
                SvgInEventIfEntry entry = entries.get(i);
                if (i == 0) {
                    txt.append("if ");
                } else if (entry.getGuard() == null) {
                    txt.append("else ");
                } else {
                    txt.append("elif ");
                }
                if (entry.getGuard() != null) {
                    txt.append(pprint(entry.getGuard()));
                    txt.append(": ");
                }
                txt.append(pprint(entry.getEvent()));
                txt.append(" ");
            }
            txt.append("end");
        } else {
            throw new RuntimeException("Unknown SvgInEvent: " + event);
        }

        if (svgIn.getSvgFile() != null) {
            String path = svgIn.getSvgFile().getPath();
            path = Strings.escape(path);
            txt.append(fmt(" file \"%s\"", path));
        }
        txt.append(";");
        code.add(txt.toString());
    }

    /**
     * Add the given print file declaration to the pretty printed code.
     *
     * @param printFile The print file declaration.
     */
    public void add(PrintFile printFile) {
        String path = printFile.getPath();
        path = Strings.escape(path);
        code.add("printfile \"%s\";", path);
    }

    /**
     * Add the given print I/O declaration to the pretty printed code.
     *
     * @param print The print I/O declaration.
     */
    public void add(Print print) {
        // Start.
        StringBuilder txt = new StringBuilder();
        txt.append("print");

        // Txt.
        if (print.getTxtPre() == null && print.getTxtPost() != null) {
            txt.append(" ");
            txt.append(pprint(print.getTxtPost()));
        } else {
            if (print.getTxtPre() != null) {
                txt.append(" pre ");
                txt.append(pprint(print.getTxtPre()));
            }
            if (print.getTxtPost() != null) {
                txt.append(" post ");
                txt.append(pprint(print.getTxtPost()));
            }
        }

        // For.
        if (!print.getFors().isEmpty()) {
            txt.append(" for ");

            boolean first = true;
            for (PrintFor printFor: print.getFors()) {
                if (!first) {
                    txt.append(", ");
                }
                first = false;
                switch (printFor.getKind()) {
                    case EVENT:
                        txt.append("event");
                        break;
                    case TIME:
                        txt.append("time");
                        break;
                    case NAME:
                        txt.append(pprint(printFor.getEvent()));
                        break;
                    case INITIAL:
                        txt.append("initial");
                        break;
                    case FINAL:
                        txt.append("final");
                        break;
                }
            }
        }

        // When.
        if (print.getWhenPre() != null || print.getWhenPost() != null) {
            txt.append(" when");

            if (print.getWhenPre() == null && print.getWhenPost() != null) {
                txt.append(" ");
                txt.append(pprint(print.getWhenPost()));
            } else {
                if (print.getWhenPre() != null) {
                    txt.append(" pre ");
                    txt.append(pprint(print.getWhenPre()));
                }
                if (print.getWhenPost() != null) {
                    txt.append(" post ");
                    txt.append(pprint(print.getWhenPost()));
                }
            }
        }

        // File.
        if (print.getFile() != null) {
            String path = print.getFile().getPath();
            path = Strings.escape(path);
            txt.append(fmt(" file \"%s\"", path));
        }

        // Done.
        txt.append(";");
        code.add(txt.toString());
    }

    /**
     * Pretty print the given update.
     *
     * @param upd The update.
     * @return The pretty printed result.
     */
    public String pprint(Update upd) {
        if (upd instanceof Assignment) {
            Assignment asgn = (Assignment)upd;
            return fmt("%s := %s", pprint(asgn.getAddressable()), pprint(asgn.getValue()));
        } else if (upd instanceof IfUpdate) {
            IfUpdate ifUpd = (IfUpdate)upd;
            List<ElifUpdate> elifs = ifUpd.getElifs();
            StringBuilder txt = new StringBuilder();

            txt.append("if ");
            txt.append(pprint(ifUpd.getGuards(), ", "));
            txt.append(": ");
            txt.append(pprintUpdates(ifUpd.getThens()));
            txt.append(" ");

            for (int i = 0; i < elifs.size(); i++) {
                ElifUpdate elif = elifs.get(i);
                txt.append("elif ");
                txt.append(pprint(elif.getGuards(), ", "));
                txt.append(": ");
                txt.append(pprintUpdates(elif.getThens()));
                txt.append(" ");
            }

            if (!ifUpd.getElses().isEmpty()) {
                txt.append("else ");
                txt.append(pprintUpdates(ifUpd.getElses()));
                txt.append(" ");
            }

            txt.append("end");
            return txt.toString();
        } else {
            throw new RuntimeException("Unknown update: " + upd);
        }
    }

    /**
     * Pretty print the given types.
     *
     * @param types The types.
     * @param separator The separator to use between the types.
     * @return The pretty printed result.
     */
    public String pprintTypes(List<CifType> types, String separator) {
        List<String> typeTxts = listc(types.size());
        for (CifType type: types) {
            typeTxts.add(pprint(type));
        }
        return StringUtils.join(typeTxts, separator);
    }

    /**
     * Pretty print the given type.
     *
     * @param type The type.
     * @return The pretty printed result.
     * @see CifTextUtils#typeToStr
     */
    public String pprint(CifType type) {
        if (type instanceof BoolType) {
            return "bool";
        } else if (type instanceof IntType) {
            IntType itype = (IntType)type;
            if (CifTypeUtils.isRangeless(itype)) {
                return "int";
            }

            // Handle a lower bound of -2147483648 special, as 2147483648 is
            // not allowed as integer literal (due to overflow).
            int lower = itype.getLower();
            String lowerTxt = (lower == Integer.MIN_VALUE) ? "-2147483647-1" : Integer.toString(lower);
            return fmt("int[%s..%d]", lowerTxt, itype.getUpper());
        } else if (type instanceof RealType) {
            return "real";
        } else if (type instanceof StringType) {
            return "string";
        } else if (type instanceof TypeRef) {
            TypeDecl refObj = ((TypeRef)type).getType();
            return CifScopeUtils.getRefTxtFromObj(type, refObj, scopeCache);
        } else if (type instanceof EnumType) {
            EnumDecl refObj = ((EnumType)type).getEnum();
            return CifScopeUtils.getRefTxtFromObj(type, refObj, scopeCache);
        } else if (type instanceof ListType) {
            ListType ltype = (ListType)type;
            String elemTxt = pprint(ltype.getElementType());
            if (CifTypeUtils.isRangeless(ltype)) {
                return "list " + elemTxt;
            }
            if (ltype.getLower().equals(ltype.getUpper())) {
                return fmt("list[%d] %s", ltype.getLower(), elemTxt);
            }
            return fmt("list[%d..%d] %s", ltype.getLower(), ltype.getUpper(), elemTxt);
        } else if (type instanceof SetType) {
            SetType stype = (SetType)type;
            return "set " + pprint(stype.getElementType());
        } else if (type instanceof DictType) {
            DictType dtype = (DictType)type;
            return fmt("dict(%s:%s)", pprint(dtype.getKeyType()), pprint(dtype.getValueType()));
        } else if (type instanceof TupleType) {
            TupleType ttype = (TupleType)type;
            List<String> fieldTxts = listc(ttype.getFields().size());
            for (Field field: ttype.getFields()) {
                Assert.notNull(field.getName());
                fieldTxts.add(pprint(field.getType()) + " " + escapeIdentifier(field.getName()));
            }
            return fmt("tuple(%s)", StringUtils.join(fieldTxts, "; "));
        } else if (type instanceof FuncType) {
            FuncType ftype = (FuncType)type;
            List<CifType> paramTypes = ftype.getParamTypes();
            return fmt("func %s(%s)", pprint(ftype.getReturnType()), pprintTypes(paramTypes, ", "));
        } else if (type instanceof DistType) {
            DistType dtype = (DistType)type;
            return "dist " + pprint(dtype.getSampleType());
        } else if (type instanceof ComponentType) {
            Component refObj = ((ComponentType)type).getComponent();
            return CifScopeUtils.getRefTxtFromObj(type, refObj, scopeCache);
        } else if (type instanceof ComponentDefType) {
            ComponentDef refObj = ((ComponentDefType)type).getDefinition();
            return CifScopeUtils.getRefTxtFromObj(type, refObj, scopeCache);
        } else if (type instanceof CompParamWrapType) {
            return CifScopeUtils.getViaRefTxt(type, scopeCache);
        } else if (type instanceof CompInstWrapType) {
            return CifScopeUtils.getViaRefTxt(type, scopeCache);
        } else if (type instanceof VoidType) {
            return "void";
        } else {
            throw new RuntimeException("Unknown type: " + type);
        }
    }

    /**
     * Pretty print the given expressions.
     *
     * @param exprs The expressions.
     * @param separator The separator to use between expressions. Must not contain new line characters.
     * @return The pretty printed result.
     */
    public String pprint(List<Expression> exprs, String separator) {
        return pprint(exprs, "", separator, "");
    }

    /**
     * Pretty print the given expressions.
     *
     * @param exprs The expressions.
     * @param prefix The prefix to use. Must not contain new line characters.
     * @param separator The separator to use between expressions. Must not contain new line characters.
     * @param postfix The postfix to use. Must not contain new line characters.
     * @return The pretty printed result.
     */
    public String pprint(List<Expression> exprs, String prefix, String separator, String postfix) {
        StringBuilder txt = new StringBuilder();
        if (!prefix.isEmpty()) {
            txt.append(prefix);
        }
        boolean first = true;
        for (Expression expr: exprs) {
            if (!first) {
                txt.append(separator);
            }
            first = false;
            txt.append(pprint(expr));
        }
        if (!postfix.isEmpty()) {
            txt.append(postfix);
        }
        return txt.toString();
    }

    /**
     * Pretty print the given expression.
     *
     * @param expr The expression.
     * @return The pretty printed result.
     * @see CifTextUtils#exprToStr
     */
    public String pprint(Expression expr) {
        if (expr instanceof BoolExpression) {
            boolean value = ((BoolExpression)expr).isValue();
            return CifMath.boolToStr(value);
        } else if (expr instanceof IntExpression) {
            int value = ((IntExpression)expr).getValue();
            return CifMath.intToStr(value);
        } else if (expr instanceof RealExpression) {
            String value = ((RealExpression)expr).getValue();
            return value;
        } else if (expr instanceof StringExpression) {
            String value = ((StringExpression)expr).getValue();
            return "\"" + Strings.escape(value) + "\"";
        } else if (expr instanceof TimeExpression) {
            return "time";
        } else if (expr instanceof CastExpression) {
            CastExpression cexpr = (CastExpression)expr;
            String childTxt = pprint(cexpr.getChild());

            int castStrength = getBindingStrength(expr);
            int childStrength = getBindingStrength(cexpr.getChild());

            if (castStrength > childStrength) {
                childTxt = "(" + childTxt + ")";
            }

            return fmt("<%s>%s", pprint(cexpr.getType()), childTxt);
        } else if (expr instanceof UnaryExpression) {
            UnaryExpression uexpr = (UnaryExpression)expr;
            String childTxt = pprint(uexpr.getChild());

            String opTxt = operatorToStr(uexpr.getOperator());

            int opStrength = getBindingStrength(expr);
            int childStrength = getBindingStrength(uexpr.getChild());
            if (opStrength > childStrength) {
                childTxt = "(" + childTxt + ")";
            } else if (StringUtils.isAlpha(StringUtils.right(opTxt, 1))) {
                // No parentheses, so add a space, if the operator ends with
                // a letter.
                opTxt += " ";
            }

            return opTxt + childTxt;
        } else if (expr instanceof BinaryExpression) {
            BinaryExpression bexpr = (BinaryExpression)expr;
            BinaryOperator op = bexpr.getOperator();

            String opTxt = operatorToStr(op);

            int opStrength = getBindingStrength(expr);
            int leftStrength = getBindingStrength(bexpr.getLeft());
            int rightStrength = getBindingStrength(bexpr.getRight());

            String leftTxt = pprint(bexpr.getLeft());
            if (opStrength > leftStrength
                    || (opStrength == leftStrength && getAssociativity(op) != Associativity.LEFT))
            {
                // Local operator has higher binding strength, or they are
                // equal and it is not left-associative.
                leftTxt = "(" + leftTxt + ")";
            }

            String rightTxt = pprint(bexpr.getRight());
            if (opStrength > rightStrength
                    || (opStrength == rightStrength && getAssociativity(op) != Associativity.RIGHT))
            {
                // Local operator has higher binding strength, or they are
                // equal and it is not right-associative.
                rightTxt = "(" + rightTxt + ")";
            }

            return leftTxt + " " + opTxt + " " + rightTxt;
        } else if (expr instanceof IfExpression) {
            IfExpression ifExpr = (IfExpression)expr;
            List<ElifExpression> elifs = ifExpr.getElifs();
            StringBuilder txt = new StringBuilder();

            txt.append("if ");
            txt.append(pprint(ifExpr.getGuards(), ", "));
            txt.append(": ");
            txt.append(pprint(ifExpr.getThen()));
            txt.append(" ");

            for (int i = 0; i < elifs.size(); i++) {
                ElifExpression elif = elifs.get(i);
                txt.append("elif ");
                txt.append(pprint(elif.getGuards(), ", "));
                txt.append(": ");
                txt.append(pprint(elif.getThen()));
                txt.append(" ");
            }

            txt.append("else ");
            txt.append(pprint(ifExpr.getElse()));
            txt.append(" ");
            txt.append("end");

            return txt.toString();
        } else if (expr instanceof SwitchExpression) {
            SwitchExpression switchExpr = (SwitchExpression)expr;
            Expression value = switchExpr.getValue();
            List<SwitchCase> cases = switchExpr.getCases();
            boolean autRef = CifTypeUtils.isAutRefExpr(value);

            StringBuilder txt = new StringBuilder();
            txt.append("switch ");
            txt.append(pprint(value));
            txt.append(": ");

            for (int i = 0; i < cases.size(); i++) {
                SwitchCase cse = cases.get(i);
                Expression key = cse.getKey();

                if (key == null) {
                    txt.append("else ");
                } else if (autRef) {
                    txt.append("case ");
                    Expression locRef = CifTypeUtils.unwrapExpression(key);
                    Location loc = ((LocationExpression)locRef).getLocation();
                    String locName = loc.getName();
                    Assert.notNull(locName);
                    txt.append(escapeIdentifier(locName));
                    txt.append(": ");
                } else {
                    txt.append("case ");
                    txt.append(pprint(key));
                    txt.append(": ");
                }

                txt.append(pprint(cse.getValue()));
                txt.append(" ");
            }

            txt.append("end");
            return txt.toString();
        } else if (expr instanceof ProjectionExpression) {
            ProjectionExpression pexpr = (ProjectionExpression)expr;

            String childTxt = pprint(pexpr.getChild());

            int projStrength = getBindingStrength(expr);
            int childStrength = getBindingStrength(pexpr.getChild());

            if (projStrength > childStrength) {
                childTxt = "(" + childTxt + ")";
            }

            return fmt("%s[%s]", childTxt, pprint(pexpr.getIndex()));
        } else if (expr instanceof SliceExpression) {
            SliceExpression sexpr = (SliceExpression)expr;

            String childTxt = pprint(sexpr.getChild());

            int sliceStrength = getBindingStrength(expr);
            int childStrength = getBindingStrength(sexpr.getChild());

            if (sliceStrength > childStrength) {
                childTxt = "(" + childTxt + ")";
            }

            String beginTxt;
            if (sexpr.getBegin() == null) {
                beginTxt = "";
            } else {
                beginTxt = pprint(sexpr.getBegin());
            }

            String endTxt;
            if (sexpr.getEnd() == null) {
                endTxt = "";
            } else {
                endTxt = pprint(sexpr.getEnd());
            }

            return fmt("%s[%s:%s]", childTxt, beginTxt, endTxt);
        } else if (expr instanceof FunctionCallExpression) {
            FunctionCallExpression fexpr = (FunctionCallExpression)expr;

            String funcTxt = pprint(fexpr.getFunction());

            int callStrength = getBindingStrength(expr);
            int funcStrength = getBindingStrength(fexpr.getFunction());

            if (callStrength > funcStrength) {
                funcTxt = "(" + funcTxt + ")";
            }

            return funcTxt + pprint(fexpr.getParams(), "(", ", ", ")");
        } else if (expr instanceof ListExpression) {
            ListExpression lexpr = (ListExpression)expr;
            String castTxt = null;
            if (lexpr.getElements().isEmpty()) {
                // Add explicit cast, to ensure we can always type check it,
                // regardless of context.

                // Get cast type. Needs to be an array type with zero length.
                CifType type = expr.getType();
                ListType ltype = (ListType)CifTypeUtils.normalizeType(type);
                if (CifTypeUtils.isRangeless(ltype) || !ltype.getLower().equals(0) || !ltype.getUpper().equals(0)) {
                    ltype = deepclone(ltype);
                    ltype.setLower(0);
                    ltype.setUpper(0);
                }

                // Is the additional cast needed? Don't add if already present,
                // to avoid duplicate identical casts.
                boolean addCast = true;
                EObject parent = expr.eContainer();
                if (parent instanceof CastExpression) {
                    CifType castType = ((CastExpression)parent).getType();
                    if (CifTypeUtils.checkTypeCompat(castType, ltype, RangeCompat.EQUAL)) {
                        addCast = false;
                    }
                }

                // Add additional cast, if needed.
                if (addCast) {
                    // In order to pretty print a type with references, the
                    // type must be contained. If it is deep cloned above, it
                    // is not contained. Temporarily replace the type of the
                    // expression, if needed.
                    if (type != ltype) {
                        expr.setType(ltype);
                    }

                    // Get cast text.
                    castTxt = "<" + pprint(ltype) + ">";

                    // Restore the type of the expression.
                    if (type != ltype) {
                        expr.setType(type);
                    }
                }
            }
            if (castTxt == null) {
                castTxt = "";
            }
            String elemTxt = pprint(lexpr.getElements(), "[", ", ", "]");
            return castTxt + elemTxt;
        } else if (expr instanceof SetExpression) {
            SetExpression sexpr = (SetExpression)expr;
            String castTxt = null;
            if (sexpr.getElements().isEmpty()) {
                // Add explicit cast, to ensure we can always type check it,
                // regardless of context. Unless it already has a cast to
                // that type around it.
                boolean addCast = true;
                EObject parent = expr.eContainer();
                if (parent instanceof CastExpression) {
                    CifType castType = ((CastExpression)parent).getType();
                    if (CifTypeUtils.checkTypeCompat(castType, expr.getType(), RangeCompat.EQUAL)) {
                        addCast = false;
                    }
                }
                if (addCast) {
                    castTxt = "<" + pprint(expr.getType()) + ">";
                }
            }
            if (castTxt == null) {
                castTxt = "";
            }
            String elemTxt = pprint(sexpr.getElements(), "{", ", ", "}");
            return castTxt + elemTxt;
        } else if (expr instanceof TupleExpression) {
            TupleExpression texpr = (TupleExpression)expr;
            return pprint(texpr.getFields(), "(", ", ", ")");
        } else if (expr instanceof DictExpression) {
            DictExpression dexpr = (DictExpression)expr;
            String castTxt = null;
            if (dexpr.getPairs().isEmpty()) {
                // Add explicit cast, to ensure we can always type check it,
                // regardless of context. Unless it already has a cast to
                // that type around it.
                boolean addCast = true;
                EObject parent = expr.eContainer();
                if (parent instanceof CastExpression) {
                    CifType castType = ((CastExpression)parent).getType();
                    if (CifTypeUtils.checkTypeCompat(castType, expr.getType(), RangeCompat.EQUAL)) {
                        addCast = false;
                    }
                }
                if (addCast) {
                    castTxt = "<" + pprint(expr.getType()) + ">";
                }
            }

            StringBuilder txt = new StringBuilder();
            if (castTxt != null) {
                txt.append(castTxt);
            }
            txt.append("{");
            boolean first = true;
            for (DictPair pair: dexpr.getPairs()) {
                if (!first) {
                    txt.append(", ");
                }
                first = false;
                txt.append(pprint(pair.getKey()));
                txt.append(": ");
                txt.append(pprint(pair.getValue()));
            }
            txt.append("}");
            return txt.toString();
        } else if (expr instanceof ConstantExpression) {
            Constant refObj = ((ConstantExpression)expr).getConstant();
            return CifScopeUtils.getRefTxtFromObj(expr, refObj, scopeCache);
        } else if (expr instanceof DiscVariableExpression) {
            DiscVariable refObj = ((DiscVariableExpression)expr).getVariable();
            return CifScopeUtils.getRefTxtFromObj(expr, refObj, scopeCache);
        } else if (expr instanceof AlgVariableExpression) {
            AlgVariable refObj = ((AlgVariableExpression)expr).getVariable();
            return CifScopeUtils.getRefTxtFromObj(expr, refObj, scopeCache);
        } else if (expr instanceof ContVariableExpression) {
            ContVariableExpression cvexpr = (ContVariableExpression)expr;
            ContVariable refObj = cvexpr.getVariable();
            String txt = CifScopeUtils.getRefTxtFromObj(expr, refObj, scopeCache);
            if (cvexpr.isDerivative()) {
                txt += "'";
            }
            return txt;
        } else if (expr instanceof TauExpression) {
            return "tau";
        } else if (expr instanceof LocationExpression) {
            Location refObj = ((LocationExpression)expr).getLocation();
            return CifScopeUtils.getRefTxtFromObj(expr, refObj, scopeCache);
        } else if (expr instanceof EnumLiteralExpression) {
            EnumLiteral refObj = ((EnumLiteralExpression)expr).getLiteral();
            return CifScopeUtils.getRefTxtFromObj(expr, refObj, scopeCache);
        } else if (expr instanceof EventExpression) {
            // Unlike the other references, we cache event references, to
            // optimize events on edges for synthesized event-based
            // supervisors.
            Event refObj = ((EventExpression)expr).getEvent();
            EObject scope = CifScopeUtils.getScope(expr);

            Pair<EObject, Event> key = pair(scope, refObj);
            String txt = eventRefCache.get(key);
            if (txt == null) {
                txt = CifScopeUtils.getRefTxtFromObj(expr, refObj, scopeCache);
                eventRefCache.put(key, txt);
            }
            return txt;
        } else if (expr instanceof FieldExpression) {
            // Due to the scoping rules, we can use the name of the field
            // directly.
            Field field = ((FieldExpression)expr).getField();
            Assert.notNull(field.getName());
            return escapeIdentifier(field.getName());
        } else if (expr instanceof StdLibFunctionExpression) {
            StdLibFunctionExpression fexpr = (StdLibFunctionExpression)expr;
            StdLibFunction func = fexpr.getFunction();
            return functionToStr(func);
        } else if (expr instanceof FunctionExpression) {
            Function refObj = ((FunctionExpression)expr).getFunction();
            return CifScopeUtils.getRefTxtFromObj(expr, refObj, scopeCache);
        } else if (expr instanceof InputVariableExpression) {
            InputVariable refObj = ((InputVariableExpression)expr).getVariable();
            return CifScopeUtils.getRefTxtFromObj(expr, refObj, scopeCache);
        } else if (expr instanceof ComponentExpression) {
            Component refObj = ((ComponentExpression)expr).getComponent();
            return CifScopeUtils.getRefTxtFromObj(expr, refObj, scopeCache);
        } else if (expr instanceof CompInstWrapExpression) {
            return CifScopeUtils.getViaRefTxt(expr, scopeCache);
        } else if (expr instanceof CompParamWrapExpression) {
            return CifScopeUtils.getViaRefTxt(expr, scopeCache);
        } else if (expr instanceof ReceivedExpression) {
            return "?";
        } else if (expr instanceof SelfExpression) {
            return "self";
        } else {
            throw new RuntimeException("Unknown expr: " + expr);
        }
    }
}
