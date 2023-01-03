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

package org.eclipse.escet.chi.typecheck;

import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newAssignmentStatement;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newBreakStatement;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newCloseStatement;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newContinueStatement;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newDelayStatement;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newExitStatement;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newFinishStatement;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newForStatement;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newIfCase;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newIfStatement;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newIteratedCreateCase;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newIteratedSelectCase;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newPassStatement;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newProcessInstance;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newReceiveStatement;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newReturnStatement;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newRunStatement;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newSelectCase;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newSelectStatement;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newSendStatement;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newTupleField;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newTupleType;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newUnwind;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newVariableDeclaration;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newVoidType;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newWhileStatement;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newWriteStatement;
import static org.eclipse.escet.chi.typecheck.CheckExpression.transExpression;
import static org.eclipse.escet.chi.typecheck.CheckExpression.transExpressionList;
import static org.eclipse.escet.chi.typecheck.CheckType.copyType;
import static org.eclipse.escet.chi.typecheck.CheckType.dropReferences;
import static org.eclipse.escet.chi.typecheck.CheckType.isPrintable;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.position.common.PositionUtils.copyPosition;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.escet.chi.metamodel.chi.AssignmentStatement;
import org.eclipse.escet.chi.metamodel.chi.BehaviourDeclaration;
import org.eclipse.escet.chi.metamodel.chi.BinaryExpression;
import org.eclipse.escet.chi.metamodel.chi.BinaryOperators;
import org.eclipse.escet.chi.metamodel.chi.BoolType;
import org.eclipse.escet.chi.metamodel.chi.BreakStatement;
import org.eclipse.escet.chi.metamodel.chi.CallExpression;
import org.eclipse.escet.chi.metamodel.chi.ChannelOps;
import org.eclipse.escet.chi.metamodel.chi.ChannelType;
import org.eclipse.escet.chi.metamodel.chi.CloseStatement;
import org.eclipse.escet.chi.metamodel.chi.ContinueStatement;
import org.eclipse.escet.chi.metamodel.chi.CreateCase;
import org.eclipse.escet.chi.metamodel.chi.DelayStatement;
import org.eclipse.escet.chi.metamodel.chi.DictType;
import org.eclipse.escet.chi.metamodel.chi.ExitStatement;
import org.eclipse.escet.chi.metamodel.chi.Expression;
import org.eclipse.escet.chi.metamodel.chi.FileType;
import org.eclipse.escet.chi.metamodel.chi.FinishStatement;
import org.eclipse.escet.chi.metamodel.chi.ForStatement;
import org.eclipse.escet.chi.metamodel.chi.IfCase;
import org.eclipse.escet.chi.metamodel.chi.IfStatement;
import org.eclipse.escet.chi.metamodel.chi.InstanceType;
import org.eclipse.escet.chi.metamodel.chi.IntType;
import org.eclipse.escet.chi.metamodel.chi.IteratedCreateCase;
import org.eclipse.escet.chi.metamodel.chi.IteratedSelectCase;
import org.eclipse.escet.chi.metamodel.chi.ListType;
import org.eclipse.escet.chi.metamodel.chi.PassStatement;
import org.eclipse.escet.chi.metamodel.chi.ProcessInstance;
import org.eclipse.escet.chi.metamodel.chi.ProcessType;
import org.eclipse.escet.chi.metamodel.chi.RealType;
import org.eclipse.escet.chi.metamodel.chi.ReceiveStatement;
import org.eclipse.escet.chi.metamodel.chi.ReturnStatement;
import org.eclipse.escet.chi.metamodel.chi.RunStatement;
import org.eclipse.escet.chi.metamodel.chi.SelectCase;
import org.eclipse.escet.chi.metamodel.chi.SelectStatement;
import org.eclipse.escet.chi.metamodel.chi.SendStatement;
import org.eclipse.escet.chi.metamodel.chi.SetType;
import org.eclipse.escet.chi.metamodel.chi.Statement;
import org.eclipse.escet.chi.metamodel.chi.StringLiteral;
import org.eclipse.escet.chi.metamodel.chi.StringType;
import org.eclipse.escet.chi.metamodel.chi.TupleExpression;
import org.eclipse.escet.chi.metamodel.chi.TupleField;
import org.eclipse.escet.chi.metamodel.chi.TupleType;
import org.eclipse.escet.chi.metamodel.chi.Type;
import org.eclipse.escet.chi.metamodel.chi.Unwind;
import org.eclipse.escet.chi.metamodel.chi.VariableDeclaration;
import org.eclipse.escet.chi.metamodel.chi.VariableReference;
import org.eclipse.escet.chi.metamodel.chi.VoidType;
import org.eclipse.escet.chi.metamodel.chi.WhileStatement;
import org.eclipse.escet.chi.metamodel.chi.WriteStatement;
import org.eclipse.escet.chi.typecheck.CheckContext.ContextItem;
import org.eclipse.escet.chi.typecheck.symbols.SymbolEntry;
import org.eclipse.escet.chi.typecheck.symbols.VariableSymbolEntry;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.FormatDecoder;
import org.eclipse.escet.common.java.FormatDescription;
import org.eclipse.escet.common.position.common.PositionUtils;
import org.eclipse.escet.common.position.metamodel.position.Position;

/** Type check statements, and decorate their expressions with type information. */
public abstract class CheckStatement {
    /**
     * Verify that the expression is addressable (can be assigned a value).
     *
     * @param e Expression to check.
     * @param ctxt Type checker context.
     */
    private static void checkAddressableExpression(Expression e, CheckContext ctxt) {
        if (e instanceof VariableReference) {
            VariableReference ref = (VariableReference)e;
            EObject obj = ref.getVariable().eContainer();
            if (obj instanceof BehaviourDeclaration) {
                return;
            }
            Assert.check(obj instanceof Unwind || obj instanceof ForStatement);
            /* Fall through to the exception. */
        }

        if (e instanceof TupleExpression) {
            TupleExpression tuple = (TupleExpression)e;
            for (Expression expr: tuple.getFields()) {
                checkAddressableExpression(expr, ctxt);
            }
            return;
        }
        if (e instanceof BinaryExpression) {
            BinaryExpression be = (BinaryExpression)e;
            if (be.getOp() == BinaryOperators.PROJECTION) {
                Expression left = be.getLeft();
                Type ltp = dropReferences(left.getType());
                if (!(ltp instanceof StringType)) {
                    checkAddressableExpression(left, ctxt);
                    return;
                }
                /* Fall through to the exception if LHS has a string-type. */
            }
        }
        ctxt.throwError(Message.NOT_ADDRESSABLE, e.getPosition());
    }

    /** Intermediate storage to return type checking and computation results. */
    private static class LocalVarsResult {
        /** Converted expression. */
        public final Expression newSource;

        /** Created variables. */
        public final List<VariableDeclaration> newVars;

        /** Updated symbol table. */
        public final CheckContext newCtxt;

        /**
         * Constructor of the {@link LocalVarsResult} class.
         *
         * @param newSource Converted expression.
         * @param newVars Created variables.
         * @param newCtxt Updated checking context.
         */
        public LocalVarsResult(Expression newSource, List<VariableDeclaration> newVars, CheckContext newCtxt) {
            this.newSource = newSource;
            this.newVars = newVars;
            this.newCtxt = newCtxt;
        }
    }

    /**
     * Check whether the local iterator variables in for/select/run/create are sane.
     *
     * @param source Supplied untyped source expression.
     * @param vars Supplied untyped local variables.
     * @param ctxt Type-checking context.
     * @return Type-checked source expression result.
     */
    private static LocalVarsResult checkLocalVariables(Expression source, List<VariableDeclaration> vars,
            CheckContext ctxt)
    {
        Expression newSource = transExpression(source, ctxt);
        Type elmType = dropReferences(newSource.getType());
        if (elmType instanceof ListType) {
            elmType = dropReferences(((ListType)elmType).getElementType());
            elmType = copyType(elmType);
        } else if (elmType instanceof SetType) {
            elmType = dropReferences(((SetType)elmType).getElementType());
            elmType = copyType(elmType);
        } else if (elmType instanceof DictType) {
            DictType dt = (DictType)elmType;
            TupleField tf0, tf1;
            tf0 = newTupleField(null, null, copyType(dt.getKeyType()));
            tf1 = newTupleField(null, null, copyType(dt.getValueType()));
            elmType = newTupleType(list(tf0, tf1), null);
        } else {
            ctxt.throwError(Message.LIST_SET_DICT_EXPECTED, source.getPosition(), CheckType.toString(elmType));
        }

        List<VariableDeclaration> newVars;
        newVars = list();
        if (vars.size() == 1) {
            // One identifier in the for statement.
            VariableDeclaration varDecl = vars.get(0);
            VariableDeclaration newVarDecl = newVariableDeclaration(null, varDecl.getName(), false,
                    copyPosition(varDecl), elmType);
            newVars.add(newVarDecl);
        } else {
            // Multiple identifiers in the for statement.
            boolean cond = elmType instanceof TupleType;
            ctxt.checkThrowError(cond, Message.TUPLE_N_EXPECTED, source.getPosition(), String.valueOf(vars.size()),
                    CheckType.toString(elmType));

            TupleType tt = (TupleType)elmType;

            cond = tt.getFields().size() == vars.size();
            ctxt.checkThrowError(cond, Message.TUPLE_N_EXPECTED_FOUND_M, source.getPosition(),
                    String.valueOf(tt.getFields().size()), String.valueOf(vars.size()));

            for (int i = 0; i < vars.size(); i++) {
                VariableDeclaration varDecl = vars.get(i);
                Type varType = tt.getFields().get(i).getType();
                VariableDeclaration newVarDecl = newVariableDeclaration(null, varDecl.getName(), false,
                        copyPosition(varDecl), copyType(varType));
                newVars.add(newVarDecl);
            }
        }

        CheckContext newCtxt = ctxt.newSymbolContext();
        for (VariableDeclaration vd: newVars) {
            SymbolEntry se = new VariableSymbolEntry(false, vd, ctxt);
            newCtxt.addSymbol(se);
        }
        return new LocalVarsResult(newSource, newVars, newCtxt);
    }

    /**
     * Add unwind variables to the symbol table after checking.
     *
     * @param unwinds Local variables to add.
     * @param newUnwinds Converted local variables.
     * @param ctxt Type-checking context.
     * @return New type checking context with added local variables.
     */
    private static CheckContext transUnwinds(List<Unwind> unwinds, List<Unwind> newUnwinds, CheckContext ctxt) {
        for (Unwind unw: unwinds) {
            LocalVarsResult lvr = checkLocalVariables(unw.getSource(), unw.getVariables(), ctxt);
            newUnwinds.add(newUnwind(copyPosition(unw), lvr.newSource, lvr.newVars));
            ctxt = lvr.newCtxt;
        }
        return ctxt;
    }

    /**
     * Type check and copy a sequence of statements.
     *
     * @param stats Statements to check.
     * @param ctxt Type-checking context.
     * @return Type-checked statements (without parent).
     */
    public static List<Statement> transStatementList(List<Statement> stats, CheckContext ctxt) {
        List<Statement> newStats = list();
        for (Statement s: stats) {
            newStats.add(transStatement(s, ctxt));
        }
        return newStats;
    }

    /**
     * Type check and copy a statement.
     *
     * @param stat Statement to check.
     * @param ctxt Type-checking context.
     * @return Type-checked statement (without parent).
     */
    protected static Statement transStatement(Statement stat, CheckContext ctxt) {
        if (stat instanceof AssignmentStatement) {
            return transAssignmentStatement((AssignmentStatement)stat, ctxt);
        } else if (stat instanceof BreakStatement) {
            return transBreakStatement((BreakStatement)stat, ctxt);
        } else if (stat instanceof CloseStatement) {
            return transCloseStatement((CloseStatement)stat, ctxt);
        } else if (stat instanceof ContinueStatement) {
            return transContinueStatement((ContinueStatement)stat, ctxt);
        } else if (stat instanceof DelayStatement) {
            return transDelayStatement((DelayStatement)stat, ctxt);
        } else if (stat instanceof FinishStatement) {
            return transFinishStatement((FinishStatement)stat, ctxt);
        } else if (stat instanceof ForStatement) {
            return transForStatement((ForStatement)stat, ctxt);
        } else if (stat instanceof IfStatement) {
            return transIfStatement((IfStatement)stat, ctxt);
        } else if (stat instanceof PassStatement) {
            return transPassStatement((PassStatement)stat);
        } else if (stat instanceof ReceiveStatement) {
            return transReceiveStatement((ReceiveStatement)stat, ctxt);
        } else if (stat instanceof ReturnStatement) {
            return transReturnStatement((ReturnStatement)stat, ctxt);
        } else if (stat instanceof RunStatement) {
            return transRunStatement((RunStatement)stat, ctxt);
        } else if (stat instanceof SelectStatement) {
            return transSelectStatement((SelectStatement)stat, ctxt);
        } else if (stat instanceof SendStatement) {
            return transSendStatement((SendStatement)stat, ctxt);
        } else if (stat instanceof ExitStatement) {
            return transExitStatement((ExitStatement)stat, ctxt);
        } else if (stat instanceof WhileStatement) {
            return transWhileStatement((WhileStatement)stat, ctxt);
        } else if (stat instanceof WriteStatement) {
            return transWriteStatement((WriteStatement)stat, ctxt);
        }

        Assert.fail("Unknown statement type encountered");
        return null;
    }

    /**
     * Type check an assignment statement, and decorate its expressions with type information.
     *
     * @param stat Statement to check.
     * @param ctxt Type-checking context.
     * @return The converted statement.
     */
    private static Statement transAssignmentStatement(AssignmentStatement stat, CheckContext ctxt) {
        // lhs expression that changes in time or randomly doesn't seem
        // particularly useful. Also, it may break in-place updates if we ever
        // introduce those.
        CheckContext lhsCtxt = ctxt.add(ContextItem.NO_READ, ContextItem.NO_TIME, ContextItem.NO_CHANNEL,
                ContextItem.NO_REAL_TIMER_CAST, ContextItem.NO_SAMPLE);
        Expression newLhs = transExpression(stat.getLhs(), lhsCtxt);
        Expression newRhs = transExpression(stat.getRhs(), ctxt);

        boolean cond = CheckType.matchType(newRhs.getType(), newLhs.getType());
        ctxt.checkThrowError(cond, Message.CANNOT_ASSIGN, stat.getPosition(), CheckType.toString(newRhs.getType()),
                CheckType.toString(newLhs.getType()));

        checkAddressableExpression(newLhs, ctxt);
        return newAssignmentStatement(newLhs, copyPosition(stat), newRhs);
    }

    /**
     * Type check a break statement, and decorate its expressions with type information.
     *
     * @param stat Statement to check.
     * @param ctxt Type-checking context.
     * @return The converted statement.
     */
    private static Statement transBreakStatement(BreakStatement stat, CheckContext ctxt) {
        ctxt.checkThrowError(ctxt.contains(ContextItem.INSIDE_LOOP), Message.BREAK_IN_LOOP, stat.getPosition());

        return newBreakStatement(copyPosition(stat));
    }

    /**
     * Type check a close statement, and decorate its expressions with type information.
     *
     * @param stat Statement to check.
     * @param ctxt Type-checking context.
     * @return The converted statement.
     */
    private static Statement transCloseStatement(CloseStatement stat, CheckContext ctxt) {
        Expression handle = transExpression(stat.getHandle(), ctxt);
        Type tp = dropReferences(handle.getType());

        ctxt.checkThrowError(tp instanceof FileType, Message.FILE_TYPE_EXPECTED, stat.getPosition(),
                CheckType.toString(tp));

        return newCloseStatement(handle, copyPosition(stat));
    }

    /**
     * Type check a continue statement, and decorate its expressions with type information.
     *
     * @param stat Statement to check.
     * @param ctxt Type-checking context.
     * @return The converted statement.
     */
    private static Statement transContinueStatement(ContinueStatement stat, CheckContext ctxt) {
        ctxt.checkThrowError(ctxt.contains(ContextItem.INSIDE_LOOP), Message.CONTINUE_IN_LOOP, stat.getPosition());

        return newContinueStatement(copyPosition(stat));
    }

    /**
     * Type check a delay statement, and decorate its expressions with type information.
     *
     * @param stat Statement to check.
     * @param ctxt Type-checking context.
     * @return The converted statement.
     */
    private static Statement transDelayStatement(DelayStatement stat, CheckContext ctxt) {
        ctxt.checkThrowError(!ctxt.contains(ContextItem.NO_DELAY), Message.STATEMENT_NOT_IN_FUNCTION,
                stat.getPosition(), "delay");

        Expression newLength = transExpression(stat.getLength(), ctxt);
        Type tp = dropReferences(newLength.getType());
        boolean cond = tp instanceof IntType || tp instanceof RealType;
        ctxt.checkThrowError(cond, Message.NUMERIC_TYPE_EXPECTED, stat.getPosition(), CheckType.toString(tp));

        return newDelayStatement(newLength, copyPosition(stat));
    }

    /**
     * Type check a finish statement, and decorate its expressions with type information.
     *
     * @param stat Statement to check.
     * @param ctxt Type-checking context.
     * @return The converted statement.
     */
    private static Statement transFinishStatement(FinishStatement stat, CheckContext ctxt) {
        List<Expression> newInsts;
        newInsts = transExpressionList(stat.getInstances(), ctxt);
        for (Expression e: newInsts) {
            Type tp = dropReferences(e.getType());

            ctxt.checkThrowError(tp instanceof InstanceType, Message.INSTANCE_TYPE_EXPECTED, e.getPosition(),
                    CheckType.toString(tp));
        }
        return newFinishStatement(newInsts, copyPosition(stat));
    }

    /**
     * Type check a for statement, and decorate its expressions with type information.
     *
     * @param stat Statement to check.
     * @param ctxt Type-checking context.
     * @return The converted statement.
     */
    private static Statement transForStatement(ForStatement stat, CheckContext ctxt) {
        List<Unwind> newUnwinds = list();
        CheckContext loopCtxt = ctxt.add(ContextItem.INSIDE_LOOP);
        loopCtxt = transUnwinds(stat.getUnwinds(), newUnwinds, loopCtxt);
        List<Statement> newBody = transStatementList(stat.getBody(), loopCtxt);
        loopCtxt.checkSymbolUsage();
        return newForStatement(newBody, copyPosition(stat), newUnwinds);
    }

    /**
     * Type check an if statement, and decorate its expressions with type information.
     *
     * @param stat Statement to check.
     * @param ctxt Type-checking context.
     * @return The converted statement.
     */
    private static Statement transIfStatement(IfStatement stat, CheckContext ctxt) {
        List<IfCase> newCases = list();
        for (IfCase ifc: stat.getCases()) {
            Expression cond = null;
            if (ifc.getCondition() != null) {
                cond = transExpression(ifc.getCondition(), ctxt);
                Type tp = dropReferences(cond.getType());

                ctxt.checkThrowError(tp instanceof BoolType, Message.BOOLEAN_TYPE_EXPECTED,
                        ifc.getCondition().getPosition(), CheckType.toString(tp));
            }
            List<Statement> newBody = transStatementList(ifc.getBody(), ctxt);
            newCases.add(newIfCase(newBody, cond, copyPosition(ifc)));
        }
        return newIfStatement(newCases, copyPosition(stat));
    }

    /**
     * Type check a pass statement, and decorate its expressions with type information.
     *
     * @param stat Statement to check.
     * @return The converted statement.
     */
    private static Statement transPassStatement(PassStatement stat) {
        return newPassStatement(copyPosition(stat));
    }

    /**
     * Type check a receive statement, and decorate its expressions with type information.
     *
     * @param stat Statement to check.
     * @param ctxt Type-checking context.
     * @return The converted statement.
     */
    private static Statement transReceiveStatement(ReceiveStatement stat, CheckContext ctxt) {
        ctxt.checkThrowError(!ctxt.contains(ContextItem.NO_COMM), Message.STATEMENT_NOT_IN_FUNCTION, stat.getPosition(),
                "receive");

        CheckContext chanCtxt = ctxt.add(ContextItem.NO_TIME, ContextItem.NO_READ, ContextItem.NO_INPUT,
                ContextItem.NO_CHANNEL, ContextItem.NO_REAL_TIMER_CAST, ContextItem.NO_SAMPLE);
        Expression newChan = transExpression(stat.getChannel(), chanCtxt);
        Type tp = dropReferences(newChan.getType());

        ctxt.checkThrowError(tp instanceof ChannelType, Message.CHANNEL_TYPE_EXPECTED, stat.getChannel().getPosition(),
                CheckType.toString(tp));

        ChannelType chanType = (ChannelType)tp;
        if (chanType.getOps() == ChannelOps.SEND) {
            ctxt.throwError(Message.CANNOT_RECEIVE_ON_SEND_ONLY_CHANNEL, stat.getPosition());
        }
        tp = dropReferences(chanType.getElementType());
        Expression newData;
        if (tp instanceof VoidType) {
            ctxt.checkThrowError(stat.getData() == null, Message.SYNC_CHANNELS_NO_DATA, stat.getPosition());
            newData = null;
        } else {
            ctxt.checkThrowError(stat.getData() != null, Message.VARIABLES_MISSING_WITH_RECEIVE, stat.getPosition(),
                    CheckType.toString(tp));

            // Consistency with normal assignment, where this is not allowed.
            CheckContext lhsCtxt = ctxt.add(ContextItem.NO_READ, ContextItem.NO_TIME, ContextItem.NO_CHANNEL,
                    ContextItem.NO_REAL_TIMER_CAST, ContextItem.NO_SAMPLE);
            newData = transExpression(stat.getData(), lhsCtxt);

            boolean cond = CheckType.matchType(tp, newData.getType());
            ctxt.checkThrowError(cond, Message.RECV_DATA_NOT_MATCH_CHANNEL, stat.getPosition(),
                    CheckType.toString(newData.getType()), CheckType.toString(tp));

            checkAddressableExpression(newData, ctxt);
        }
        return newReceiveStatement(newChan, newData, copyPosition(stat));
    }

    /**
     * Type check a return statement, and decorate its expressions with type information.
     *
     * @param stat Statement to check.
     * @param ctxt Type-checking context.
     * @return The converted statement.
     */
    private static Statement transReturnStatement(ReturnStatement stat, CheckContext ctxt) {
        if (ctxt.funcReturnType == null) {
            ctxt.throwError(Message.STATEMENT_NOT_OUTSIDE_FUNCTION, stat.getPosition(), "return");
        }

        Expression newRet = transExpression(stat.getValue(), ctxt);

        boolean cond = CheckType.matchType(newRet.getType(), ctxt.funcReturnType);
        ctxt.checkThrowError(cond, Message.RETURNED_DATA_NOT_MATCH_FUNC_RET_TYPE, stat.getPosition(),
                CheckType.toString(newRet.getType()), CheckType.toString(ctxt.funcReturnType));

        return newReturnStatement(copyPosition(stat), newRet);
    }

    /**
     * Type check a number of cases in the create/run statement, and decorate the expressions with type information.
     *
     * @param cases Cases to check.
     * @param startOnly Process instances are started only (and not 'run').
     * @param ctxt Type-checking context.
     * @return The converted create cases.
     */
    private static List<CreateCase> transCreateCase(List<CreateCase> cases, boolean startOnly, CheckContext ctxt) {
        List<CreateCase> newCases = list();
        for (CreateCase cc: cases) {
            if (cc instanceof ProcessInstance) {
                ProcessInstance pi = (ProcessInstance)cc;
                Expression newVar = null;
                // Statement has an instance variable.
                if (pi.getVar() != null) {
                    ctxt.checkThrowError(startOnly, Message.INST_USELESS_WITH_RUN, pi.getVar().getPosition());
                    newVar = transExpression(pi.getVar(), ctxt);
                    Type tp = dropReferences(newVar.getType());

                    ctxt.checkThrowError(tp instanceof InstanceType, Message.VAR_INST_TYPE, pi.getVar().getPosition(),
                            CheckType.toString(tp));
                }
                Expression newCall = transExpression(pi.getCall(), ctxt);
                Type tp = dropReferences(newCall.getType());
                ctxt.checkThrowError(tp instanceof InstanceType, Message.NEED_RUN_START_STAT,
                        pi.getCall().getPosition());

                // Verify exit type
                CallExpression ce = (CallExpression)newCall;
                ProcessType pt = (ProcessType)ce.getFunction().getType();
                if (pt.getExitType() != null) {
                    // Child process declares exit, verify with the current process.
                    if (ctxt.exitType == null) {
                        ctxt.throwError(Message.CANNOT_START_PROCESS_WITH_EXIT_IN_NO_EXIT, newCall.getPosition(),
                                CheckType.toString(pt.getExitType()));
                    } else if (!CheckType.matchType(pt.getExitType(), ctxt.exitType)) {
                        ctxt.throwError(Message.EXIT_TYPES_DO_NOT_MATCH, newCall.getPosition(),
                                CheckType.toString(pt.getExitType()), CheckType.toString(ctxt.exitType));
                    }
                }

                newCases.add(newProcessInstance(newCall, copyPosition(pi), newVar));
            } else if (cc instanceof IteratedCreateCase) {
                IteratedCreateCase icc = (IteratedCreateCase)cc;
                List<Unwind> newUnwinds = list();
                CheckContext loopCtxt = ctxt.add(ContextItem.INSIDE_LOOP);
                loopCtxt = transUnwinds(icc.getUnwinds(), newUnwinds, loopCtxt);
                List<CreateCase> newInstances;
                newInstances = transCreateCase(icc.getInstances(), startOnly, loopCtxt);
                newCases.add(newIteratedCreateCase(newInstances, copyPosition(cc), newUnwinds));
                loopCtxt.checkSymbolUsage();
            } else {
                Assert.fail("Unknown create case");
            }
        }
        return newCases;
    }

    /**
     * Type check a run statement, and decorate its expressions with type information.
     *
     * @param stat Statement to check.
     * @param ctxt Type-checking context.
     * @return The converted statement.
     */
    private static Statement transRunStatement(RunStatement stat, CheckContext ctxt) {
        ctxt.checkThrowError(!ctxt.contains(ContextItem.NO_RUN), Message.STATEMENT_NOT_IN_FUNCTION, stat.getPosition(),
                "run/create");

        List<CreateCase> newCases = transCreateCase(stat.getCases(), stat.isStartOnly(), ctxt);
        return newRunStatement(newCases, copyPosition(stat), stat.isStartOnly());
    }

    /**
     * Check guard expression of a select/alt statement.
     *
     * @param guard Guard expression to check.
     * @param ctxt Context of the expression.
     * @return The checked and converted guard expression.
     */
    private static Expression checkGuardExpression(Expression guard, CheckContext ctxt) {
        if (guard == null) {
            return null;
        }

        CheckContext gCtxt = ctxt.add(ContextItem.NO_TIME, ContextItem.NO_READ, ContextItem.NO_INPUT,
                ContextItem.NO_CHANNEL, ContextItem.NO_REAL_TIMER_CAST, ContextItem.NO_SAMPLE);
        Expression newGuard = transExpression(guard, gCtxt);
        Type tp = dropReferences(newGuard.getType());

        ctxt.checkThrowError(tp instanceof BoolType, Message.BOOLEAN_TYPE_EXPECTED, guard.getPosition(),
                CheckType.toString(tp));
        return newGuard;
    }

    /**
     * Type check a select statement, and decorate its expressions with type information.
     *
     * @param stat Statement to check.
     * @param ctxt Type-checking context.
     * @return The converted statement.
     */
    private static Statement transSelectStatement(SelectStatement stat, CheckContext ctxt) {
        ctxt.checkThrowError(!ctxt.contains(ContextItem.NO_SELECT), Message.STATEMENT_NOT_IN_FUNCTION,
                stat.getPosition(), "select");

        List<SelectCase> newCases = list();
        for (SelectCase sc: stat.getCases()) {
            if (sc instanceof IteratedSelectCase) {
                // Iterated case
                IteratedSelectCase isc = (IteratedSelectCase)sc;
                List<Unwind> newUnwinds = list();
                CheckContext loopCtxt = ctxt.add(ContextItem.INSIDE_LOOP);
                loopCtxt = transUnwinds(isc.getUnwinds(), newUnwinds, loopCtxt);
                Expression newGuard;
                CheckContext guardCtxt = loopCtxt.add(ContextItem.NO_TIME, ContextItem.NO_REAL_TIMER_CAST,
                        ContextItem.NO_SAMPLE);
                newGuard = checkGuardExpression(sc.getGuard(), guardCtxt);
                List<Statement> newBody;
                newBody = transStatementList(sc.getBody(), loopCtxt);
                newCases.add(newIteratedSelectCase(newBody, newGuard, copyPosition(sc), newUnwinds));
                loopCtxt.checkSymbolUsage();
            } else {
                // Non-iterated case
                Expression newGuard;
                CheckContext guardCtxt = ctxt.add(ContextItem.NO_TIME, ContextItem.NO_REAL_TIMER_CAST,
                        ContextItem.NO_SAMPLE);
                newGuard = checkGuardExpression(sc.getGuard(), guardCtxt);
                List<Statement> newBody;
                newBody = transStatementList(sc.getBody(), ctxt);
                newCases.add(newSelectCase(newBody, newGuard, copyPosition(sc)));
            }
        }
        return newSelectStatement(newCases, copyPosition(stat));
    }

    /**
     * Type check a send statement, and decorate its expressions with type information.
     *
     * @param stat Statement to check.
     * @param ctxt Type-checking context.
     * @return The converted statement.
     */
    private static Statement transSendStatement(SendStatement stat, CheckContext ctxt) {
        ctxt.checkThrowError(!ctxt.contains(ContextItem.NO_COMM), Message.STATEMENT_NOT_IN_FUNCTION, stat.getPosition(),
                "send");

        CheckContext chanCtxt = ctxt.add(ContextItem.NO_TIME, ContextItem.NO_READ, ContextItem.NO_INPUT,
                ContextItem.NO_CHANNEL, ContextItem.NO_REAL_TIMER_CAST, ContextItem.NO_SAMPLE);
        Expression newChan = transExpression(stat.getChannel(), chanCtxt);
        Type elementType = dropReferences(newChan.getType());

        ctxt.checkThrowError(elementType instanceof ChannelType, Message.CHANNEL_TYPE_EXPECTED,
                stat.getChannel().getPosition(), CheckType.toString(elementType));

        ChannelType chanType = (ChannelType)elementType;
        if (chanType.getOps() == ChannelOps.RECEIVE) {
            ctxt.throwError(Message.CANNOT_SEND_ON_RECEIVE_ONLY_CHANNEL, stat.getPosition());
        }
        elementType = chanType.getElementType();
        elementType = dropReferences(elementType);
        Expression newData;
        if (elementType instanceof VoidType) {
            ctxt.checkThrowError(stat.getData() == null, Message.SYNC_CHANNELS_NO_DATA, stat.getPosition());

            newData = null;
        } else {
            ctxt.checkThrowError(stat.getData() != null, Message.DATA_MISSING_WITH_SEND, stat.getPosition(),
                    CheckType.toString(elementType));

            newData = transExpression(stat.getData(), ctxt);

            boolean cond = CheckType.matchType(newData.getType(), elementType);
            ctxt.checkThrowError(cond, Message.SENT_DATA_NOT_MATCH_CHANNEL, stat.getPosition(),
                    CheckType.toString(newData.getType()), CheckType.toString(elementType));
        }
        return newSendStatement(newChan, newData, copyPosition(stat));
    }

    /**
     * Type check an exit statement, and decorate its expressions with type information.
     *
     * @param stat Statement to check.
     * @param ctxt Type-checking context.
     * @return The converted statement.
     */
    private static Statement transExitStatement(ExitStatement stat, CheckContext ctxt) {
        if (ctxt.exitType == null) {
            if (ctxt.contains(ContextItem.NO_EXIT)) {
                ctxt.throwError(Message.EXIT_STATEMENT_NOT_ALLOWED, stat.getPosition());
            } else {
                ctxt.throwError(Message.PROC_MODEL_NO_EXIT_TYPE, stat.getPosition());
            }
        }

        Expression newExit;
        Type actualType;
        if (stat.getValue() == null) {
            newExit = null;
            actualType = newVoidType();
        } else {
            newExit = transExpression(stat.getValue(), ctxt);
            actualType = newExit.getType();
        }

        ctxt.checkThrowError(CheckType.matchType(actualType, ctxt.exitType), Message.RETURNED_DATA_NOT_MATCH_EXIT_TYPE,
                stat.getPosition(), CheckType.toString(actualType), CheckType.toString(ctxt.exitType));
        return newExitStatement(copyPosition(stat), newExit);
    }

    /**
     * Type check a while statement, and decorate its expressions with type information.
     *
     * @param stat Statement to check.
     * @param ctxt Type-checking context.
     * @return The converted statement.
     */
    private static Statement transWhileStatement(WhileStatement stat, CheckContext ctxt) {
        Expression cond = transExpression(stat.getCondition(), ctxt);
        Type tp = dropReferences(cond.getType());

        ctxt.checkThrowError(tp instanceof BoolType, Message.BOOLEAN_TYPE_EXPECTED, stat.getCondition().getPosition(),
                CheckType.toString(tp));

        CheckContext loopCtxt = ctxt.add(ContextItem.INSIDE_LOOP);
        List<Statement> body;
        body = transStatementList(stat.getBody(), loopCtxt);

        return newWhileStatement(body, cond, copyPosition(stat));
    }

    /**
     * Type check a write statement, and decorate its expressions with type information.
     *
     * @param stat Statement to check.
     * @param ctxt Type-checking context.
     * @return The converted statement.
     */
    private static Statement transWriteStatement(WriteStatement stat, CheckContext ctxt) {
        List<Expression> newValues;
        newValues = transExpressionList(stat.getValues(), ctxt);

        ctxt.checkThrowError(!newValues.isEmpty(), Message.WRITE_NEEDS_FORMATTING_STRING, stat.getPosition());

        // File output?
        Type ftp = dropReferences(newValues.get(0).getType());
        int start = (ftp instanceof FileType) ? 1 : 0;
        // Format string checking.
        ctxt.checkThrowError(newValues.size() > start, Message.WRITE_STRING_LITERAL_FORMATTING, stat.getPosition());

        boolean cond = newValues.get(start) instanceof StringLiteral;
        ctxt.checkThrowError(cond, Message.WRITE_STRING_LITERAL_FORMATTING, newValues.get(start).getPosition());

        StringLiteral fmtExpr = ((StringLiteral)newValues.get(start));
        Position fmtPos = fmtExpr.getPosition();
        String fmtPattern = fmtExpr.getValue();
        // Check the format string against its actual parameters.
        int offset = start + 1;
        Type tp;
        FormatDecoder decoder = new FormatDecoder();
        int implicitIdx = 0;
        boolean[] used = new boolean[newValues.size()];
        for (int i = 0; i < offset; i++) {
            used[i] = true;
        }

        for (FormatDescription fd: decoder.decode(fmtPattern)) {
            Position fdPos = makeSubPosition(fmtPos, fd);

            int idx = offset;
            if (fd.conversion != FormatDescription.Conversion.LITERAL
                    && fd.conversion != FormatDescription.Conversion.ERROR)
            {
                if (fd.index.isEmpty()) {
                    idx += implicitIdx;
                    implicitIdx++;
                } else {
                    int explicitIdx = fd.getExplicitIndex();
                    ctxt.checkThrowError(explicitIdx != -1, Message.FORMAT_EXPLICIT_IDX_OVERFLOW, fdPos, fd.index);
                    idx += explicitIdx - 1;
                }
                ctxt.checkThrowError(idx < newValues.size(), Message.FORMAT_STRING_TOO_MANY_SPECIFIERS, fdPos);
                used[idx] = true;
            }

            switch (fd.conversion) {
                case LITERAL:
                    break;

                case BOOLEAN: {
                    Expression val = newValues.get(idx);
                    tp = dropReferences(val.getType());

                    ctxt.checkThrowError(tp instanceof BoolType, Message.FORMAT_N_WRONG_TYPE, fdPos,
                            String.valueOf(idx), CheckType.toString(tp), "bool");
                    break;
                }

                case INTEGER: {
                    Expression val = newValues.get(idx);
                    tp = dropReferences(val.getType());

                    ctxt.checkThrowError(tp instanceof IntType, Message.FORMAT_N_WRONG_TYPE, fdPos, String.valueOf(idx),
                            CheckType.toString(tp), "int");
                    break;
                }

                case REAL: {
                    Expression val = newValues.get(idx);
                    tp = dropReferences(val.getType());

                    ctxt.checkThrowError(tp instanceof RealType, Message.FORMAT_N_WRONG_TYPE, fdPos,
                            String.valueOf(idx), CheckType.toString(tp), "real");
                    break;
                }

                case STRING: {
                    // Accept any printable type.
                    Expression val = newValues.get(idx);
                    tp = val.getType();

                    ctxt.checkThrowError(isPrintable(tp), Message.FORMAT_N_NON_PRINTABLE, fdPos, String.valueOf(idx),
                            CheckType.toString(tp));
                    break;
                }

                case ERROR:
                    ctxt.throwError(Message.FORMAT_ERROR, fdPos, fd.toString());
            }
        }

        boolean unused = false;
        for (boolean b: used) {
            if (!b) {
                unused = true;
            }
        }
        ctxt.checkThrowError(!unused, Message.FORMAT_NOT_ENOUGH_SPECIFIERS, newValues.get(start).getPosition());

        return newWriteStatement(stat.isAddNewline(), copyPosition(stat), newValues);
    }

    /**
     * Creates a sub position of the position information from a string literal, for the given format description.
     *
     * @param origPos The original position for the entire string literal.
     * @param fd The format description that was decoded from the string literal.
     * @return The sub position.
     */
    private static Position makeSubPosition(Position origPos, FormatDescription fd) {
        return PositionUtils.getSubRange(origPos, fd.offset + 1, fd.length);
    }
}
