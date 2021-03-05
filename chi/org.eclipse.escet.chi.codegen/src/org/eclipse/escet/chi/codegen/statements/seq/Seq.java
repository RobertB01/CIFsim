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

package org.eclipse.escet.chi.codegen.statements.seq;

import static org.eclipse.escet.chi.codegen.classes.SelectAlternativeClass.createSelectCaseClass;
import static org.eclipse.escet.chi.codegen.expressions.ExpressionBase.convertExpression;
import static org.eclipse.escet.chi.codegen.statements.seq.AssignmentConversions.convertAssignment;
import static org.eclipse.escet.chi.codegen.statements.seq.ForLoopConversion.convertForLoop;
import static org.eclipse.escet.chi.codegen.types.TypeIDCreation.createTypeID;
import static org.eclipse.escet.chi.codegen.types.TypeIDCreation.dropTypeReferences;
import static org.eclipse.escet.chi.codegen.types.TypeIDCreation.makeStringTypeID;
import static org.eclipse.escet.chi.typecheck.CheckType.dropReferences;
import static org.eclipse.escet.common.java.Lists.copy;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.stringToJava;

import java.util.List;

import org.eclipse.escet.chi.codegen.CodeGeneratorContext;
import org.eclipse.escet.chi.codegen.Constants;
import org.eclipse.escet.chi.codegen.OutputPosition;
import org.eclipse.escet.chi.codegen.expressions.BehaviorHelper;
import org.eclipse.escet.chi.codegen.expressions.BehaviorHelper.ExprBehavior;
import org.eclipse.escet.chi.codegen.expressions.CodeExpression;
import org.eclipse.escet.chi.codegen.expressions.ExpressionBase;
import org.eclipse.escet.chi.codegen.java.JavaFile;
import org.eclipse.escet.chi.codegen.statements.seq.ForLoopConversion.ForLoopSeqResult;
import org.eclipse.escet.chi.codegen.statements.seq.SeqSelect.SelectAlternative;
import org.eclipse.escet.chi.codegen.types.TypeID;
import org.eclipse.escet.chi.metamodel.chi.AssignmentStatement;
import org.eclipse.escet.chi.metamodel.chi.BehaviourDeclaration;
import org.eclipse.escet.chi.metamodel.chi.BreakStatement;
import org.eclipse.escet.chi.metamodel.chi.CloseStatement;
import org.eclipse.escet.chi.metamodel.chi.CommunicationStatement;
import org.eclipse.escet.chi.metamodel.chi.ContinueStatement;
import org.eclipse.escet.chi.metamodel.chi.CreateCase;
import org.eclipse.escet.chi.metamodel.chi.DelayStatement;
import org.eclipse.escet.chi.metamodel.chi.ExitStatement;
import org.eclipse.escet.chi.metamodel.chi.Expression;
import org.eclipse.escet.chi.metamodel.chi.FileType;
import org.eclipse.escet.chi.metamodel.chi.FinishStatement;
import org.eclipse.escet.chi.metamodel.chi.ForStatement;
import org.eclipse.escet.chi.metamodel.chi.IfCase;
import org.eclipse.escet.chi.metamodel.chi.IfStatement;
import org.eclipse.escet.chi.metamodel.chi.IteratedCreateCase;
import org.eclipse.escet.chi.metamodel.chi.IteratedSelectCase;
import org.eclipse.escet.chi.metamodel.chi.PassStatement;
import org.eclipse.escet.chi.metamodel.chi.ProcessInstance;
import org.eclipse.escet.chi.metamodel.chi.ReceiveStatement;
import org.eclipse.escet.chi.metamodel.chi.ReturnStatement;
import org.eclipse.escet.chi.metamodel.chi.RunStatement;
import org.eclipse.escet.chi.metamodel.chi.SelectCase;
import org.eclipse.escet.chi.metamodel.chi.SelectStatement;
import org.eclipse.escet.chi.metamodel.chi.SendStatement;
import org.eclipse.escet.chi.metamodel.chi.Statement;
import org.eclipse.escet.chi.metamodel.chi.StringLiteral;
import org.eclipse.escet.chi.metamodel.chi.Type;
import org.eclipse.escet.chi.metamodel.chi.Unwind;
import org.eclipse.escet.chi.metamodel.chi.VariableDeclaration;
import org.eclipse.escet.chi.metamodel.chi.WhileStatement;
import org.eclipse.escet.chi.metamodel.chi.WriteStatement;
import org.eclipse.escet.common.box.Box;
import org.eclipse.escet.common.box.VBox;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.FormatDecoder;
import org.eclipse.escet.common.java.FormatDescription;
import org.eclipse.escet.common.java.FormatDescription.Conversion;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** Sequential language base class. */
public abstract class Seq extends OutputPosition {
    /**
     * Constructor.
     *
     * @param chiobj Chi statement being translated (used for deriving position information).
     */
    public Seq(PositionObject chiobj) {
        super(chiobj);
    }

    /**
     * Convert statement to box representation.
     *
     * @return The statement in box representation.
     */
    public abstract Box boxify();

    /**
     * Construct a list of sequential statements with one statement.
     *
     * @param seq The statement to add.
     * @return The list of sequential statements.
     */
    private static List<Seq> mkSeqs(Seq seq) {
        List<Seq> seqs = listc(1);
        Assert.notNull(seq);
        seqs.add(seq);
        return seqs;
    }

    /**
     * Generate variable assignments into the provided code box.
     *
     * @param vars Variables to initialize.
     * @param codeBox Box to deposit the code into.
     * @param ctxt Code generator context.
     * @param currentClass Target class for the code generation.
     */
    private static void initializeVariables(List<VariableDeclaration> vars, VBox codeBox, CodeGeneratorContext ctxt,
            JavaFile currentClass)
    {
        // Initialize local variables.
        for (VariableDeclaration vd: vars) {
            if (vd.isParameter()) {
                continue;
            }

            String name = ctxt.getDefinition(vd);

            if (vd.getInitialValue() != null) {
                // Local variable with an initial value.
                ExpressionBase initExpr = convertExpression(vd.getInitialValue(), ctxt, currentClass);
                String posStat = initExpr.genCurrentPositionStatement();
                if (posStat != null) {
                    codeBox.add(posStat);
                }
                codeBox.add(initExpr.getCode());
                String line = fmt("%s = %s;", name, initExpr.getValue());
                codeBox.add(line);
                continue;
            }

            // Local variable without initial value, assign default value.
            Type tp = dropTypeReferences(vd.getType());
            TypeID tid = createTypeID(tp, ctxt);
            tid.assignInitialValue(name, tp, codeBox, ctxt, currentClass);
        }
    }

    /**
     * Translate variable initializations (of processes, models, and experiments) to sequential language.
     *
     * @param bd Definition containing the variables.
     * @param ctxt Code generator context.
     * @param currentClass Java class targeted for code generation.
     * @return Translation result.
     */
    public static List<Seq> convertVarInitialization(BehaviourDeclaration bd, CodeGeneratorContext ctxt,
            JavaFile currentClass)
    {
        VBox b = new VBox(0);
        initializeVariables(bd.getVariables(), b, ctxt, currentClass);
        return mkSeqs(new SeqBox(b, null));
    }

    /**
     * Translate a Chi statement body to sequential language statements.
     *
     * @param stats Statement body.
     * @param ctxt Code generator context.
     * @param currentClass Java class targeted for code generation.
     * @return Translation result.
     */
    public static List<Seq> convertStatements(List<Statement> stats, CodeGeneratorContext ctxt, JavaFile currentClass) {
        List<Seq> seqs = list();
        for (Statement stat: stats) {
            List<Seq> result = convertStatement(stat, ctxt, currentClass);
            for (Seq s: result) {
                Assert.notNull(s);
            }
            seqs.addAll(result);
        }
        return seqs;
    }

    /**
     * Translate a Chi statement to a sequence of {@link #Seq} statements.
     *
     * @param stat Chi statement to translate.
     * @param ctxt Code generator context.
     * @param currentClass Java class targeted for code generation.
     * @return Translation result.
     */
    private static List<Seq> convertStatement(Statement stat, CodeGeneratorContext ctxt, JavaFile currentClass) {
        if (stat instanceof AssignmentStatement) {
            return convertAsgStatement((AssignmentStatement)stat, ctxt, currentClass);
        } else if (stat instanceof BreakStatement) {
            BreakStatement bs = (BreakStatement)stat;
            return mkSeqs(new SeqBreak(bs));
        } else if (stat instanceof ContinueStatement) {
            ContinueStatement cs = (ContinueStatement)stat;
            return mkSeqs(new SeqContinue(cs));
        } else if (stat instanceof DelayStatement) {
            return convertDelayStatement((DelayStatement)stat, ctxt, currentClass);
        } else if (stat instanceof ForStatement) {
            return convertForStatement((ForStatement)stat, ctxt, currentClass);
        } else if (stat instanceof IfStatement) {
            return convertIfStatement((IfStatement)stat, ctxt, currentClass);
        } else if (stat instanceof PassStatement) {
            return mkSeqs(new SeqCode(null, stat));
        } else if (stat instanceof ReceiveStatement) {
            return convertReceiveStatement((ReceiveStatement)stat, ctxt, currentClass);
        } else if (stat instanceof ReturnStatement) {
            return convertReturnStatement((ReturnStatement)stat, ctxt, currentClass);
        } else if (stat instanceof RunStatement) {
            return convertRunStatement((RunStatement)stat, ctxt, currentClass);
        } else if (stat instanceof SelectStatement) {
            return convertSelectStatement((SelectStatement)stat, ctxt, currentClass);
        } else if (stat instanceof SendStatement) {
            return convertSendStatement((SendStatement)stat, ctxt, currentClass);
        } else if (stat instanceof ExitStatement) {
            return convertExitStatement((ExitStatement)stat, ctxt, currentClass);
        } else if (stat instanceof WhileStatement) {
            return convertWhileStatement((WhileStatement)stat, ctxt, currentClass);
        } else if (stat instanceof WriteStatement) {
            return convertWriteStatement((WriteStatement)stat, ctxt, currentClass);
        } else if (stat instanceof CloseStatement) {
            return convertCloseStatement((CloseStatement)stat, ctxt, currentClass);
        } else if (stat instanceof FinishStatement) {
            return convertFinishStatement((FinishStatement)stat, ctxt, currentClass);
        }
        Assert.fail("Unknown Chi statement encountered.");
        return null;
    }

    /**
     * Convert an assignment statement.
     *
     * @param stat Assignment statement to convert.
     * @param ctxt Code generation context.
     * @param currentClass Java class targeted for code generation.
     * @return The converted statement.
     */
    private static List<Seq> convertAsgStatement(AssignmentStatement stat, CodeGeneratorContext ctxt,
            JavaFile currentClass)
    {
        SeqCode code = convertAssignment(stat, ctxt, currentClass);
        return mkSeqs(code);
    }

    /**
     * Convert a delay statement.
     *
     * @param stat Delay statement to convert.
     * @param ctxt Code generator context.
     * @param currentClass Java class targeted for code generation.
     * @return The converted delay statement.
     */
    private static List<Seq> convertDelayStatement(DelayStatement stat, CodeGeneratorContext ctxt,
            JavaFile currentClass)
    {
        ExpressionBase lengthExpr = convertExpression(stat.getLength(), ctxt, currentClass);
        SeqCode code = new SeqCode(null, stat);
        code.genCurrentPositionStatement();
        code.lines.addAll(lengthExpr.getCode());
        code.lines.add(fmt("setDelayTimer(%s);", lengthExpr.getValue()));
        int delayAlternativeNumber = ctxt.getUniqueDeclNumber();
        String s = fmt("selectList.add(new SelectDelay(chiCoordinator, this, %d));", delayAlternativeNumber);
        currentClass.addImport(Constants.SELECT_DELAY_FQC, false);
        code.lines.add(s);
        SelectAlternative sa = new SelectAlternative(delayAlternativeNumber, mkSeqs(code), null, null);
        return mkSeqs(new SeqSelect(list(sa), stat));
    }

    /**
     * Convert a for statement.
     *
     * @param stat For statement to convert.
     * @param ctxt Code generator context.
     * @param currentClass Java class targeted for code generation.
     * @return The converted for statement.
     */
    private static List<Seq> convertForStatement(ForStatement stat, CodeGeneratorContext ctxt, JavaFile currentClass) {
        ForLoopSeqResult flsr = convertForLoop(stat, ctxt, currentClass);
        List<Seq> seqs = list();
        if (flsr.varInit != null) {
            seqs.add(flsr.varInit);
        }
        seqs.addAll(convertStatements(stat.getBody(), ctxt, currentClass));
        return mkSeqs(new SeqForLoop(flsr.loopInit, flsr.loopCond, flsr.loopIncr, new SeqList(seqs), stat));
    }

    /**
     * Convert an if statement.
     *
     * @param stat If statement to convert.
     * @param ctxt Code generator context.
     * @param currentClass Java class targeted for code generation.
     * @return The converted if statement.
     */
    private static List<Seq> convertIfStatement(IfStatement stat, CodeGeneratorContext ctxt, JavaFile currentClass) {
        List<Seq> prevCase = null;
        int idx = stat.getCases().size() - 1;
        while (idx >= 0) {
            IfCase ifc = stat.getCases().get(idx);
            if (ifc.getCondition() == null) {
                // 'true' condition. Forget about any previous cases.
                prevCase = convertStatements(ifc.getBody(), ctxt, currentClass);
            } else {
                // Regular 'if'
                ExpressionBase cond = convertExpression(ifc.getCondition(), ctxt, currentClass);
                List<Seq> ifsr;
                ifsr = convertStatements(ifc.getBody(), ctxt, currentClass);
                SeqList ifBranch = new SeqList(ifsr);
                SeqList elseBranch = new SeqList(prevCase);
                Seq ifElse = new SeqIfElse(cond, ifBranch, elseBranch, ifc);
                prevCase = list(ifElse);
            }
            idx--;
        }
        Assert.notNull(prevCase);
        return prevCase;
    }

    /**
     * Convert a receive statement.
     *
     * @param stat Receive statement to convert.
     * @param ctxt Code generator context.
     * @param currentClass Java class targeted for code generation.
     * @return The converted receive statement.
     */
    private static List<Seq> convertReceiveStatement(ReceiveStatement stat, CodeGeneratorContext ctxt,
            JavaFile currentClass)
    {
        int selChoiceNumber = ctxt.getUniqueDeclNumber();
        String selClassName = createSelectCaseClass(selChoiceNumber, null, "GuardKind.TRUE", null,
                "ChannelKind.RECEIVE", stat, ctxt, currentClass);

        ExpressionBase ch;
        String line;
        SelectAlternative sa;

        ch = convertExpression(stat.getChannel(), ctxt, currentClass);
        SeqCode code = new SeqCode(copy(ch.getCode()), stat);
        line = fmt("selectList.add(new %s(chiCoordinator, %s, spec, this));", selClassName, ch.getValue());
        code.lines.add(line);
        sa = new SelectAlternative(selChoiceNumber, mkSeqs(code), null, null);
        Seq result = new SeqSelect(list(sa), stat);
        return list(result);
    }

    /**
     * Convert a return statement.
     *
     * @param stat Return statement to convert.
     * @param ctxt Code generator context.
     * @param currentClass Java class targeted for code generation.
     * @return The converted return statement.
     */
    private static List<Seq> convertReturnStatement(ReturnStatement stat, CodeGeneratorContext ctxt,
            JavaFile currentClass)
    {
        ExpressionBase expr = convertExpression(stat.getValue(), ctxt, currentClass);
        TypeID tid = createTypeID(stat.getValue().getType(), ctxt);
        String res = ctxt.makeUniqueName("res");
        List<String> lines = list();
        lines.addAll(expr.getCode());
        lines.add(fmt("%s %s = %s;", tid.getJavaType(), res, expr.getValue()));
        expr = new CodeExpression(lines, res, stat);
        return mkSeqs(SeqReturn.funcReturn(expr, stat));
    }

    /**
     * Convert a process instantiation statement.
     *
     * @param cc Instantiation statement to convert.
     * @param startOnly If set, do not generate code to wait for child termination.
     * @param ctxt Code generator context.
     * @param currentClass Java class targeted for code generation.
     * @return The converted process instantiation statement.
     */
    private static List<Seq> convertCreateProcessInstance(ProcessInstance cc, boolean startOnly,
            CodeGeneratorContext ctxt, JavaFile currentClass)
    {
        ExpressionBase expr = convertExpression(cc.getCall(), ctxt, currentClass);
        List<String> lines = list();
        expr.setCurrentPositionStatement(lines);
        lines.addAll(expr.getCode());
        String procVar = ctxt.makeUniqueName("proc");
        lines.add(fmt("BaseProcess %s = %s;", procVar, expr.getValue()));
        lines.add(fmt("chiCoordinator.addProcess(%s);", procVar));
        if (!startOnly) {
            // Running the child processes to completion.
            Assert.check(cc.getVar() == null);
            lines.add(fmt("chiChilds.add(%s);", procVar));
        } else if (cc.getVar() != null) {
            ExpressionBase asgVar = convertExpression(cc.getVar(), ctxt, currentClass);
            lines.addAll(asgVar.getCode());
            lines.add(fmt("%s = %s;", asgVar.getValue(), procVar));
        } // Otherwise, the child is already running by itself.
        Seq seq = new SeqCode(lines, cc);
        return list(seq);
    }

    /**
     * Convert an iterative process instantiation statement.
     *
     * @param icc The iterative process creation statement.
     * @param startOnly If set, do not generate code to wait for child termination.
     * @param ctxt Code generation context.
     * @param currentClass Java class targeted for code generation.
     * @return The converted statement as a sequence of statements.
     */
    private static List<Seq> convertIteratedCreateCase(IteratedCreateCase icc, boolean startOnly,
            CodeGeneratorContext ctxt, JavaFile currentClass)
    {
        List<Seq> result = list();
        // Root case, do the process instances.
        for (CreateCase cc: icc.getInstances()) {
            List<Seq> instCode;
            instCode = convertCreateCase(cc, startOnly, ctxt, currentClass);
            result.addAll(instCode);
        }

        int idx = icc.getUnwinds().size() - 1;
        while (idx >= 0) {
            // Normal case, wrap an Unwind around the 'result'.
            for (Unwind unw: icc.getUnwinds()) {
                ForLoopSeqResult flsr = convertForLoop(unw, ctxt, currentClass);
                List<Seq> body = list();
                if (flsr.varInit != null) {
                    body.add(flsr.varInit);
                }
                Assert.notNull(result);
                body.addAll(result);
                Seq forloop = new SeqForLoop(flsr.loopInit, flsr.loopCond, flsr.loopIncr, new SeqList(body), unw);
                result = list(forloop);
            }
            idx--;
        }
        return result;
    }

    /**
     * Convert a process start/run statement.
     *
     * @param cc Process start/run statement to convert.
     * @param startOnly If set, do not generate code to wait for child termination.
     * @param ctxt Code generation context.
     * @param currentClass Java class targeted for code generation.
     * @return The converted statement.
     */
    private static List<Seq> convertCreateCase(CreateCase cc, boolean startOnly, CodeGeneratorContext ctxt,
            JavaFile currentClass)
    {
        if (cc instanceof ProcessInstance) {
            return convertCreateProcessInstance((ProcessInstance)cc, startOnly, ctxt, currentClass);
        }
        return convertIteratedCreateCase((IteratedCreateCase)cc, startOnly, ctxt, currentClass);
    }

    /**
     * Generate a select alternative to wait for a child to terminate.
     *
     * @param stat Statement being generated.
     * @param ctxt Code generation context.
     * @param currentClass Java class targeted for code generation.
     * @return The generated code.
     */
    private static List<Seq> generateWaitChilds(PositionObject stat, CodeGeneratorContext ctxt, JavaFile currentClass) {
        // Body: Wait for a child process to terminate.
        SeqCode code = new SeqCode(null, stat);
        int choice = ctxt.getUniqueDeclNumber();
        String s = "selectList.add(new SelectWaitRunChilds(chiCoordinator, this, " + String.valueOf(choice) + "));";
        code.lines.add(s);
        currentClass.addImport(Constants.SELECT_WAIT_RUN_CHILDS_FQC, false);
        SelectAlternative sa = new SelectAlternative(choice, mkSeqs(code), null, null);
        Seq result = new SeqSelect(list(sa), stat);
        return list(result);
    }

    /**
     * Convert a run statement.
     *
     * @param stat Run statement to convert.
     * @param ctxt Code generation context.
     * @param currentClass Java class targeted for code generation.
     * @return The converted statement.
     */
    private static List<Seq> convertRunStatement(RunStatement stat, CodeGeneratorContext ctxt, JavaFile currentClass) {
        List<Seq> seqs = list();
        if (!stat.isStartOnly()) {
            // Prepare a list of child processes.
            SeqCode code = new SeqCode(null, stat);
            code.lines.add("clearChildProcesses();");
            seqs.add(code);
        }
        // Phase 1: Start childs.
        for (CreateCase cc: stat.getCases()) {
            List<Seq> caseCode;
            caseCode = convertCreateCase(cc, stat.isStartOnly(), ctxt, currentClass);
            seqs.addAll(caseCode);
        }
        if (!stat.isStartOnly()) {
            // Phase 2: Wait for child processes.
            List<Seq> waitCode = generateWaitChilds(stat, ctxt, currentClass);
            seqs.addAll(waitCode);
        }
        return seqs;
    }

    /**
     * Decide the behavior of the guard expression.
     *
     * @param guard Guard expression if available, else {@code null}.
     * @return Behavior of the guard expression at runtime.
     */
    private static ExprBehavior getGuardBehavior(Expression guard) {
        if (guard == null) {
            return ExprBehavior.CONSTANT;
        }
        return BehaviorHelper.getBehavior(guard);
    }

    /**
     * Generate code to add a select alternative.
     *
     * @param selChoiceNumber Selection number.
     * @param localVars Local variables that must be stored and reset during execution (if available, else
     *     {@code null}).
     * @param sc Select case being converted.
     * @param ctxt Code generation context.
     * @param currentClass Java class targeted for code generation.
     * @return The generated code.
     */
    private static List<Seq> addSingleSelect(int selChoiceNumber, List<VariableDeclaration> localVars, SelectCase sc,
            CodeGeneratorContext ctxt, JavaFile currentClass)
    {
        String guardKind;
        Expression guardExpr;
        switch (getGuardBehavior(sc.getGuard())) {
            case CONSTANT:
            case DISCRETE:
                guardKind = "GuardKind.TRUE";
                guardExpr = null;
                break;

            case TIME:
                guardKind = "GuardKind.FUNC";
                guardExpr = sc.getGuard();
                break;

            default:
                Assert.fail("Invalid kind of guard.");
                return null; // Not reached.
        }

        // What kind of first statement does it have?
        Statement stat = sc.getBody().get(0);
        CommunicationStatement commStat;
        String channelKind;
        if (stat == null) {
            commStat = null;
            channelKind = "ChannelKind.NONE";
        } else if (stat instanceof SendStatement) {
            commStat = (CommunicationStatement)stat;
            channelKind = "ChannelKind.SEND";
        } else if (stat instanceof ReceiveStatement) {
            commStat = (CommunicationStatement)stat;
            channelKind = "ChannelKind.RECEIVE";
        } else {
            Assert.check(stat instanceof PassStatement);
            commStat = null;
            channelKind = "ChannelKind.NONE";
        }

        String selClassName = createSelectCaseClass(selChoiceNumber, localVars, guardKind, guardExpr, channelKind,
                commStat, ctxt, currentClass);
        SeqCode code = new SeqCode(null, sc);
        String vars = "";
        if (localVars != null) {
            for (VariableDeclaration vd: localVars) {
                String name = ctxt.getDefinition(vd);
                vars += ", " + name;
            }
        }

        boolean hasCondition = guardExpr == null && sc.getGuard() != null;
        if (hasCondition) {
            // Above, we decided the runtime has no guard expression (it does
            // not exist or it is statically decidable), while at compile time
            // we do have one. => Generate code for it.
            ExpressionBase ge = convertExpression(sc.getGuard(), ctxt, currentClass);
            code.lines.addAll(ge.getCode());
            code.lines.add("if (" + ge.getValue() + ") {");
        }

        String line;
        // Compute value of the channel, if available.
        if (commStat == null) {
            line = "null";
        } else {
            ExpressionBase ch = convertExpression(commStat.getChannel(), ctxt, currentClass);
            ch.setCurrentPositionStatement(code.lines);
            code.lines.addAll(ch.getCode());
            line = ch.getValue();
        }

        // Construct the select alternative, and add it to the select list.
        line = fmt("selectList.add(new %s(chiCoordinator, %s, spec, this%s));", selClassName, line, vars);
        code.lines.add(line);

        if (hasCondition) {
            code.lines.add("}"); // Closing the "if".
        }

        List<Seq> seqs = listc(1);
        seqs.add(code);
        return seqs;
    }

    /**
     * Generate code to add an iterative select alternative statement.
     *
     * @param selChoiceNumber Selection number.
     * @param sc Select case being converted.
     * @param localVars Local variables that must be stored and reset during execution.
     * @param ctxt Code generation context.
     * @param currentClass Java class targeted for code generation.
     * @return The generated code.
     */
    private static List<Seq> addIteratedSelect(int selChoiceNumber, IteratedSelectCase sc,
            List<VariableDeclaration> localVars, CodeGeneratorContext ctxt, JavaFile currentClass)
    {
        // Generate for-loops over the local variables to generate the select
        // alternatives at run-time. To make it easier, start from the
        // innermost body (the select alternative generation, and wrap loops
        // around it each time.
        List<Seq> result = null;
        int idx = sc.getUnwinds().size();
        while (idx >= 0) {
            if (idx == sc.getUnwinds().size()) {
                // Innermost case, generate code to add a select-alternative.
                result = addSingleSelect(selChoiceNumber, localVars, sc, ctxt, currentClass);
            } else {
                // Fold an Unwind around the existing body.
                Unwind unw = sc.getUnwinds().get(idx);
                ForLoopSeqResult flsr = convertForLoop(unw, ctxt, currentClass);
                List<Seq> seqs = list();
                if (flsr.varInit != null) {
                    seqs.add(flsr.varInit);
                }
                seqs.addAll(result);
                SeqForLoop sfl = new SeqForLoop(flsr.loopInit, flsr.loopCond, flsr.loopIncr, new SeqList(seqs), unw);
                result = mkSeqs(sfl);
            }
            idx--;
        }
        Assert.notNull(result);
        return result;
    }

    /**
     * Convert a select Chi statement to sequential language code.
     *
     * @param stat Select statement to convert.
     * @param ctxt Code generator context.
     * @param currentClass Java class targeted for code generation.
     * @return Result of the conversion.
     */
    private static List<Seq> convertSelectStatement(SelectStatement stat, CodeGeneratorContext ctxt,
            JavaFile currentClass)
    {
        List<SelectAlternative> alternatives;
        alternatives = listc(stat.getCases().size());

        for (SelectCase sc: stat.getCases()) {
            int selChoiceNumber = ctxt.getUniqueDeclNumber();
            if (sc instanceof IteratedSelectCase) {
                // Select-case with unwinds.
                IteratedSelectCase isc = (IteratedSelectCase)sc;

                // Collect variables to take as parameter.
                List<VariableDeclaration> localVars;
                localVars = list();
                for (Unwind unw: isc.getUnwinds()) {
                    localVars.addAll(unw.getVariables());
                }
                List<Seq> sr;
                sr = addIteratedSelect(selChoiceNumber, isc, localVars, ctxt, currentClass);

                List<Statement> stats = sc.getBody();
                stats = stats.subList(1, stats.size());
                List<Seq> body = convertStatements(stats, ctxt, currentClass);
                SelectAlternative sa = new SelectAlternative(selChoiceNumber, sr, new SeqList(body), localVars);
                alternatives.add(sa);
            } else {
                // 'simple' select case.
                List<Seq> sr = addSingleSelect(selChoiceNumber, null, sc, ctxt, currentClass);

                List<Statement> stats = sc.getBody();
                stats = stats.subList(1, stats.size());
                List<Seq> body = convertStatements(stats, ctxt, currentClass);
                SelectAlternative sa = new SelectAlternative(selChoiceNumber, sr, new SeqList(body), null);

                alternatives.add(sa);
            }
        }
        Seq result = new SeqSelect(alternatives, stat);
        return list(result);
    }

    /**
     * Convert a send Chi statement to sequential language code.
     *
     * @param stat Send statement to convert.
     * @param ctxt Code generator context.
     * @param currentClass Java class targeted for code generation.
     * @return Result of the conversion.
     */
    private static List<Seq> convertSendStatement(SendStatement stat, CodeGeneratorContext ctxt,
            JavaFile currentClass)
    {
        int selChoiceNumber = ctxt.getUniqueDeclNumber();
        String selClassName = createSelectCaseClass(selChoiceNumber, null, "GuardKind.TRUE", null, "ChannelKind.SEND",
                stat, ctxt, currentClass);
        ExpressionBase ch;
        String line;
        SelectAlternative sa;

        ch = convertExpression(stat.getChannel(), ctxt, currentClass);
        SeqCode code = new SeqCode(copy(ch.getCode()), stat);
        line = fmt("selectList.add(new %s(chiCoordinator, %s, spec, this));", selClassName, ch.getValue());
        code.lines.add(line);
        sa = new SelectAlternative(selChoiceNumber, mkSeqs(code), null, null);
        Seq result = new SeqSelect(list(sa), stat);
        return list(result);
    }

    /**
     * Convert a Chi exit statement to sequential language code.
     *
     * @param stat Exit statement to convert.
     * @param ctxt Code generator context.
     * @param currentClass Java class targeted for code generation.
     * @return Result of the conversion.
     */
    private static List<Seq> convertExitStatement(ExitStatement stat, CodeGeneratorContext ctxt,
            JavaFile currentClass)
    {
        ExpressionBase exitVal = null;
        if (stat.getValue() != null) {
            exitVal = convertExpression(stat.getValue(), ctxt, currentClass);
        }
        return mkSeqs(new SeqExit(exitVal, stat));
    }

    /**
     * Convert a Chi while statement to sequential language code.
     *
     * @param stat While statement to convert.
     * @param ctxt Code generator context.
     * @param currentClass Java class targeted for code generation.
     * @return Result of the conversion.
     */
    private static List<Seq> convertWhileStatement(WhileStatement stat, CodeGeneratorContext ctxt,
            JavaFile currentClass)
    {
        List<Seq> srBody;
        srBody = convertStatements(stat.getBody(), ctxt, currentClass);
        SeqList body = new SeqList(srBody);
        ExpressionBase cond = convertExpression(stat.getCondition(), ctxt, currentClass);
        return mkSeqs(new SeqWhile(cond, body, stat));
    }

    /**
     * Convert the Chi close statement to sequential language code.
     *
     * @param stat Statement to convert.
     * @param ctxt Code generator context.
     * @param currentClass Java class targeted for code generation.
     * @return Result of the conversion.
     */
    private static List<Seq> convertCloseStatement(CloseStatement stat, CodeGeneratorContext ctxt,
            JavaFile currentClass)
    {
        ExpressionBase handle = convertExpression(stat.getHandle(), ctxt, currentClass);
        SeqCode code = new SeqCode(null, stat);
        handle.setCurrentPositionStatement(code.lines);
        code.lines.addAll(handle.getCode());
        code.lines.add(fmt("chiCoordinator.closeFile(%s);", handle.getValue()));
        return list(code);
    }

    /**
     * Convert the Chi finish statement to sequential language code.
     *
     * @param stat Statement to convert.
     * @param ctxt Code generator context.
     * @param currentClass Java class targeted for code generation.
     * @return Result of the conversion.
     */
    private static List<Seq> convertFinishStatement(FinishStatement stat, CodeGeneratorContext ctxt,
            JavaFile currentClass)
    {
        List<Seq> seqs = list();
        SeqCode code = new SeqCode(null, stat);
        code.lines.add("clearChildProcesses();");
        seqs.add(code);

        for (Expression inst: stat.getInstances()) {
            ExpressionBase expr = convertExpression(inst, ctxt, currentClass);
            code = new SeqCode(null, inst);
            expr.setCurrentPositionStatement(code.lines);
            code.lines.addAll(expr.getCode());
            String line = fmt("chiChilds.add(%s);", expr.getValue());
            code.lines.add(line);
            seqs.add(code);
        }

        seqs.addAll(generateWaitChilds(stat, ctxt, currentClass));
        return seqs;
    }

    /**
     * Convert a write Chi statement to sequential language code.
     *
     * @param stat Write statement to convert.
     * @param ctxt Code generator context.
     * @param currentClass Java class targeted for code generation.
     * @return Result of the conversion.
     */
    private static List<Seq> convertWriteStatement(WriteStatement stat, CodeGeneratorContext ctxt,
            JavaFile currentClass)
    {
        SeqCode code = new SeqCode(null, stat);
        int valueIdx = 0;
        // Get value of the handle to use.
        String handle = "chiCoordinator.getStdout()";
        Expression expr0 = stat.getValues().get(valueIdx);
        Type tp = dropReferences(expr0.getType());
        if (tp instanceof FileType) {
            ExpressionBase fexpr = convertExpression(expr0, ctxt, currentClass);
            fexpr.setCurrentPositionStatement(code.lines);
            fexpr.setCurrentPositionStatement(code.lines);
            code.lines.addAll(fexpr.getCode());
            handle = fexpr.getValue();
            valueIdx++;
        }
        // Generate printing of values.
        StringLiteral fmtExpr = (StringLiteral)stat.getValues().get(valueIdx);
        String fmtPattern = fmtExpr.getValue();
        valueIdx++;
        int offset = valueIdx;
        int implicitIdx = 0;
        List<String> writeStatements = list();
        FormatDecoder decoder = new FormatDecoder();
        for (FormatDescription fd: decoder.decode(fmtPattern)) {
            String line;
            TypeID tid;
            if (fd.conversion == Conversion.LITERAL) {
                line = stringToJava(fd.toString());
                tid = makeStringTypeID();
            } else {
                valueIdx = offset;
                if (fd.index.isEmpty()) {
                    valueIdx += implicitIdx;
                    implicitIdx++;
                } else {
                    valueIdx += fd.getExplicitIndex() - 1;
                }

                Expression expr = stat.getValues().get(valueIdx);
                ExpressionBase vexpr = convertExpression(expr, ctxt, currentClass);

                vexpr.setCurrentPositionStatement(code.lines);
                code.lines.addAll(vexpr.getCode());
                tid = createTypeID(expr.getType(), ctxt);
                line = vexpr.getValue();

                if (!fd.isSimple()) {
                    // Convert value to string instead of writing it directly.
                    String dest = ctxt.makeUniqueName("val");
                    line = fmt("%s %s = %s;\n", tid.getJavaType(), dest, line);
                    code.lines.add(line);

                    if (fd.conversion == Conversion.STRING) {
                        line = fmt("fmt(\"%s\", %s)", fd.toString(false), tid.getToString(dest, currentClass));
                    } else {
                        line = fmt("fmt(\"%s\", %s)", fd.toString(false), dest);
                    }
                    tid = makeStringTypeID();
                    currentClass.addImport("org.eclipse.escet.common.java.Strings.fmt", true);
                }
            }

            String posStat = genCurrentPositionStatement(stat);
            if (posStat != null) {
                writeStatements.add(posStat);
            }
            writeStatements.add(tid.getWriteName(handle, line, currentClass));
        }
        code.lines.addAll(writeStatements);

        // Optionally append a \n text.
        if (stat.isAddNewline()) {
            TypeID tid = makeStringTypeID();
            code.lines.add(tid.getWriteName(handle, "\"\\n\"", currentClass));
        }
        return mkSeqs(code);
    }

    /**
     * Add the unwind variables of the create cases.
     *
     * @param cases Create cases to examine.
     * @param vars Found variables (so far).
     */
    private static void addRunUnwindVariables(List<CreateCase> cases, List<PositionObject> vars) {
        for (CreateCase cCase: cases) {
            if (!(cCase instanceof IteratedCreateCase)) {
                continue;
            }

            IteratedCreateCase iterCase = (IteratedCreateCase)cCase;
            for (Unwind unw: iterCase.getUnwinds()) {
                vars.add(unw); // Represents iterator variable.
                vars.addAll(unw.getVariables());
            }
            addRunUnwindVariables(iterCase.getInstances(), vars);
        }
    }

    /**
     * Collect local variables hidden in the code (iterators in {@link RunStatement}, {@link ForStatement}, and
     * {@link SelectStatement}).
     *
     * @param body Statement body to search.
     * @return The collected 'hidden' variable declarations in the body.
     */
    public static List<PositionObject> getIteratorVariables(List<Statement> body) {
        List<PositionObject> vars = list();
        for (Statement stat: body) {
            if (stat instanceof ForStatement) {
                ForStatement forStat = (ForStatement)stat;
                vars.add(forStat); // Represents iterator variable.
                for (Unwind unw: forStat.getUnwinds()) {
                    vars.add(unw); // Represents iterator variable.
                    vars.addAll(unw.getVariables());
                }
                vars.addAll(getIteratorVariables(forStat.getBody()));
            } else if (stat instanceof RunStatement) {
                RunStatement runStat = (RunStatement)stat;
                addRunUnwindVariables(runStat.getCases(), vars);
            } else if (stat instanceof SelectStatement) {
                SelectStatement selStat = (SelectStatement)stat;
                for (SelectCase selCase: selStat.getCases()) {
                    vars.addAll(getIteratorVariables(selCase.getBody()));
                    if (!(selCase instanceof IteratedSelectCase)) {
                        continue;
                    }
                    IteratedSelectCase iterSel = (IteratedSelectCase)selCase;
                    for (Unwind unw: iterSel.getUnwinds()) {
                        vars.add(unw); // Represents iterator variable.
                        vars.addAll(unw.getVariables());
                    }
                }
            } else if (stat instanceof WhileStatement) {
                WhileStatement wStat = (WhileStatement)stat;
                vars.addAll(getIteratorVariables(wStat.getBody()));
            } else if (stat instanceof IfStatement) {
                IfStatement ifs = (IfStatement)stat;
                for (IfCase ifc: ifs.getCases()) {
                    vars.addAll(getIteratorVariables(ifc.getBody()));
                }
            }
        }
        return vars;
    }
}
