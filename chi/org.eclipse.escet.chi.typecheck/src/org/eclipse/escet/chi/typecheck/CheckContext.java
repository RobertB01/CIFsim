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

package org.eclipse.escet.chi.typecheck;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Sets.set;

import java.util.List;
import java.util.Set;

import org.eclipse.escet.chi.metamodel.chi.Type;
import org.eclipse.escet.chi.typecheck.symbols.SymbolEntry;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Sets;
import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.common.typechecker.SemanticException;
import org.eclipse.escet.common.typechecker.SemanticProblemSeverity;

/**
 * Context of the type checking process.
 *
 * <p>
 * The check context controls which language primitives are allowed. By making a new context, the set of allowed
 * primitives can be configured. The {@link #contains} method allows querying of existence of a check item in the
 * current context.
 * </p>
 *
 * <p>
 * The object has several operations that modify the check context:
 * <ul>
 * <li>{@link #CheckContext(ChiTypeChecker)} Creation of the root context.</li>
 * <li>{@link #add} Add new context items to disable or enable primitives.</li>
 * <li>{@link #remove} Remove some context items (opposite of add).</li>
 * <li>{@link #newFunctionContext} Setup a 'return' type.</li>
 * <li>{@link #newExitContext} Setup an exit type.</li>
 * </ul>
 * </p>
 *
 * <p>
 * The check context also contains a symbol table. New scopes are added with {@link #newSymbolContext}, methods
 * {@link #addSymbol}, {@link #getSymbol}, and {@link #checkSymbolUsage} forward calls to the table.
 * </p>
 *
 * <p>
 * The check context also keeps track of 'busy' symbols. Such symbols have an implementation body that should not be
 * needed recursively. The {@link #declareBusy} marks the symbol as 'body type check is in progress', while
 * {@link #declareFinished} denotes the symbols internals to be fully checked. This functionality is here rather than in
 * a symbol table, because dependencies may cross scope boundaries, or temporary scopes may be added during checking.
 * </p>
 *
 * <p>
 * Finally, it collects and reports type checking problems:
 * <ul>
 * <li>{@link #checkThrowError} Checks a condition, and if it fails, adds an error, and terminates the type checking
 * process.</li>
 * <li>{@link #throwError} Adds an error and terminates the type checking process.</li>
 * <li>{@link #addWarning} Adds a warning.</li>
 * <li>{@link #addError} Adds an error and continues type checking (usually to add more errors and terminate).</li>
 * </ul>
 * </p>
 */
public class CheckContext {
    /** Items in the context that can be enabled or disabled. */
    public enum ContextItem {
        /** Do not allow 'delay' statement. */
        NO_DELAY,

        /** Do not allow 'select' statement. */
        NO_SELECT,

        /** Do not allow 'run/create' statement. */
        NO_RUN,

        /** Do not allow 'exit' statements at all. */
        NO_EXIT,

        /** Do not allow channel communication. */
        NO_COMM,

        /** Inside loop, break/continue allowed. */
        INSIDE_LOOP,

        /** Do not allow 'time' in the expression. */
        NO_TIME,

        /** Do not allow 'read' function. */
        NO_READ,

        /** Do not allow 'eol/eof/newlines' functions. */
        NO_INPUT,

        /** Do not allow 'channel' function. */
        NO_CHANNEL,

        /** Do not allow 'real(timer-expr)' cast. */
        NO_REAL_TIMER_CAST,

        /** Do not allow 'sample' in the expression. */
        NO_SAMPLE,

        /** Do not allow variables in the expression. */
        NO_VARIABLES,

        /** Do not allow process references. */
        NO_PROCESSES,

        /** Do not allow model references. */
        NO_MODELS,

        /** Do not allow 'void' in a type. */
        NO_VOID;
    }

    /** Type checker object. */
    public final ChiTypeChecker tchk;

    /** Busy symbols. */
    private List<SymbolEntry> busySymbols;

    /** State of the expression context. */
    public final Set<ContextItem> state;

    /** Symbol table of the context. */
    private final SymTable sym;

    /** Return type of a function, or {@code null} if 'return' is not allowed. */
    public final Type funcReturnType;

    /**
     * Exit type of a process or model, or {@code null} if 'exit' is not allowed. The {@link ContextItem#NO_EXIT} is
     * only used to improve the error message.
     */
    public final Type exitType;

    /**
     * Constructor of the {@link CheckContext} class without restrictions.
     *
     * @param tchk Type checker object collecting problem reports.
     */
    public CheckContext(ChiTypeChecker tchk) {
        Assert.notNull(tchk);
        this.tchk = tchk;
        busySymbols = list();
        state = set();
        sym = null;
        funcReturnType = null;
        exitType = null;
    }

    /**
     * Creation of an expression context for a set of items.
     *
     * @param tchk Type checker object collecting problem reports.
     * @param busySymbols Symbols getting type checked.
     * @param state State to use for the context. Caller should not change the set afterwards.
     * @param sym Symbol table of the context.
     * @param funcReturnType Expected return type of the function being checked (or {@code null} if not allowed).
     * @param exitType Expected type of the exit statement (or {@code null} if not allowed).
     */
    private CheckContext(ChiTypeChecker tchk, List<SymbolEntry> busySymbols, Set<ContextItem> state, SymTable sym,
            Type funcReturnType, Type exitType)
    {
        Assert.notNull(tchk);
        this.tchk = tchk;
        this.busySymbols = busySymbols;
        this.state = state;
        this.sym = sym;
        this.funcReturnType = funcReturnType;
        this.exitType = exitType;
    }

    /**
     * Mark a symbol as busy performing type checking itself.
     *
     * @param se Symbol to mark.
     * @param oldBusy Previous busy state of the symbol.
     */
    public void declareBusy(SymbolEntry se, boolean oldBusy) {
        if (oldBusy) {
            int index = busySymbols.size() - 1;
            SymbolEntry s;
            do {
                s = busySymbols.get(index);
                addError(Message.CYCLE_IN_DECLARATION, s.getPosition(), s.getName());
                index--;
            } while (s != se);
            throw new SemanticException();
        }
        busySymbols.add(se);
    }

    /**
     * A symbol has finished type checking itself, remove it from the busy declarations.
     *
     * @param se Symbols that finished.
     */
    public void declareFinished(SymbolEntry se) {
        int lastIndex = busySymbols.size() - 1;
        Assert.check(busySymbols.get(lastIndex) == se);
        busySymbols.remove(lastIndex);
    }

    /**
     * Test whether the context contains a given item.
     *
     * @param item Context item to test against.
     * @return {@code true} if the item is in the context, else {@code false}.
     */
    public boolean contains(ContextItem item) {
        return state.contains(item);
    }

    /**
     * Construct a new check context derived from an old one, by adding the provided extra items.
     *
     * @param additions Additional items.
     * @return New check context.
     */
    public CheckContext add(ContextItem... additions) {
        Set<ContextItem> items = Sets.copy(state);
        for (ContextItem item: additions) {
            items.add(item);
        }
        return new CheckContext(tchk, busySymbols, items, sym, funcReturnType, exitType);
    }

    /**
     * Construct a new check context derived from an old one, by removing the provided items.
     *
     * @param removals Items to remove.
     * @return New check context.
     */
    public CheckContext remove(ContextItem... removals) {
        Set<ContextItem> items = Sets.copy(state);
        for (ContextItem item: removals) {
            items.remove(item);
        }
        return new CheckContext(tchk, busySymbols, items, sym, funcReturnType, exitType);
    }

    /**
     * Construct a new check context derived from an old one, by setting the expected function return type.
     *
     * @param funcReturnType Expected function return type.
     * @return New check context.
     */
    public CheckContext newFunctionContext(Type funcReturnType) {
        Assert.check(this.funcReturnType == null);
        return new CheckContext(tchk, busySymbols, state, sym, funcReturnType, exitType);
    }

    /**
     * Construct a new check context derived from an old one, by setting the expected exit type.
     *
     * @param exitType Expected exit type.
     * @return New check context.
     */
    public CheckContext newExitContext(Type exitType) {
        Assert.check(this.exitType == null);
        return new CheckContext(tchk, busySymbols, state, sym, funcReturnType, exitType);
    }

    /**
     * Construct a new check context derived from an old one, by adding a symbol table.
     *
     * @return New check context.
     */
    public CheckContext newSymbolContext() {
        SymTable sym = new SymTable(this.sym);
        return new CheckContext(tchk, busySymbols, state, sym, funcReturnType, exitType);
    }

    /**
     * Add a symbol to the symbol table.
     *
     * @param se Symbol to add.
     */
    public void addSymbol(SymbolEntry se) {
        sym.addSymbol(se, this);
    }

    /**
     * Get a symbol from the table by name.
     *
     * @param name Name to retrieve.
     * @return Symbol information of the queried symbol if available, else {@code null}.
     */
    public SymbolEntry getSymbol(String name) {
        return sym.getSymbol(name);
    }

    /** Verify whether all symbols are used, report a warning for each unused symbol. */
    public void checkSymbolUsage() {
        sym.checkSymbolUsage(this);
    }

    /**
     * Check that the condition holds, and terminate the type checker if not.
     *
     * @param cond Condition value to verify.
     * @param msg Message to throw in case the condition does not hold.
     * @param pos Position of the exception.
     * @param args Arguments of the message.
     */
    public void checkThrowError(boolean cond, Message msg, Position pos, String... args) {
        if (cond) {
            return;
        }
        throwError(msg, pos, args);
    }

    /**
     * Add an error to the list of problems, and terminate type checking (at this point).
     *
     * @param msg Message to throw.
     * @param pos Position of the message.
     * @param args Arguments of the message.
     */
    public void throwError(Message msg, Position pos, String... args) {
        addError(msg, pos, args);
        throw new SemanticException();
    }

    /**
     * Add a warning to the list of problems.
     *
     * @param msg Message to add.
     * @param pos Position of the message.
     * @param args Arguments of the message.
     */
    public void addWarning(Message msg, Position pos, String... args) {
        Assert.check(msg.getSeverity() == SemanticProblemSeverity.WARNING);
        tchk.addProblem(msg.format(args), msg.getSeverity(), pos);
    }

    /**
     * Add an error to the list of problems.
     *
     * @param msg Message to add.
     * @param pos Position of the message.
     * @param args Arguments of the message.
     */
    public void addError(Message msg, Position pos, String... args) {
        Assert.check(msg.getSeverity() == SemanticProblemSeverity.ERROR);
        tchk.addProblem(msg.format(args), msg.getSeverity(), pos);
    }
}
