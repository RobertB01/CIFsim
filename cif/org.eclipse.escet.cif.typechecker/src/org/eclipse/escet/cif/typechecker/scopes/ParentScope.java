//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.typechecker.scopes;

import static org.eclipse.escet.cif.typechecker.CifExprsTypeChecker.BOOL_TYPE_HINT;
import static org.eclipse.escet.cif.typechecker.CifExprsTypeChecker.transExpression;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.Map;

import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.ComponentDef;
import org.eclipse.escet.cif.metamodel.cif.Equation;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.parser.ast.AEquation;
import org.eclipse.escet.cif.parser.ast.AInitialDecl;
import org.eclipse.escet.cif.parser.ast.AMarkedDecl;
import org.eclipse.escet.cif.parser.ast.automata.ALocation;
import org.eclipse.escet.cif.parser.ast.expressions.AExpression;
import org.eclipse.escet.cif.parser.ast.iodecls.AIoDecl;
import org.eclipse.escet.cif.typechecker.CheckStatus;
import org.eclipse.escet.cif.typechecker.CifTypeChecker;
import org.eclipse.escet.cif.typechecker.ErrMsg;
import org.eclipse.escet.cif.typechecker.SymbolTableEntry;
import org.eclipse.escet.cif.typechecker.declwrap.AlgVariableDeclWrap;
import org.eclipse.escet.cif.typechecker.declwrap.ConstDeclWrap;
import org.eclipse.escet.cif.typechecker.declwrap.ContVariableDeclWrap;
import org.eclipse.escet.cif.typechecker.declwrap.DeclWrap;
import org.eclipse.escet.cif.typechecker.declwrap.DiscVariableDeclWrap;
import org.eclipse.escet.cif.typechecker.declwrap.EnumDeclWrap;
import org.eclipse.escet.cif.typechecker.declwrap.EnumLiteralDeclWrap;
import org.eclipse.escet.cif.typechecker.declwrap.EventDeclWrap;
import org.eclipse.escet.cif.typechecker.declwrap.FormalAlgDeclWrap;
import org.eclipse.escet.cif.typechecker.declwrap.FormalEventDeclWrap;
import org.eclipse.escet.cif.typechecker.declwrap.FormalLocationDeclWrap;
import org.eclipse.escet.cif.typechecker.declwrap.FuncParamDeclWrap;
import org.eclipse.escet.cif.typechecker.declwrap.FuncVariableDeclWrap;
import org.eclipse.escet.cif.typechecker.declwrap.InputVariableDeclWrap;
import org.eclipse.escet.cif.typechecker.declwrap.InvDeclWrap;
import org.eclipse.escet.cif.typechecker.declwrap.InvariantInfo;
import org.eclipse.escet.cif.typechecker.declwrap.LocationDeclWrap;
import org.eclipse.escet.cif.typechecker.declwrap.TypeDeclWrap;
import org.eclipse.escet.common.box.Box;
import org.eclipse.escet.common.box.GridBox;
import org.eclipse.escet.common.box.TextBox;
import org.eclipse.escet.common.box.VBox;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;
import org.eclipse.escet.common.typechecker.SemanticException;

/**
 * A parent scope in the symbol table of the CIF language.
 *
 * @param <T> The type of the CIF metamodel object that represents this scope.
 */
public abstract class ParentScope<T extends PositionObject> extends SymbolScope<T> {
    /**
     * Mapping from names to local child scopes. The names are disjoint from the names of the {@link #declarations}
     * mapping.
     */
    protected final Map<String, SymbolScope<?>> children = map();

    /**
     * Mapping from local declaration names to the {@link DeclWrap wrapped} CIF metamodel objects they are associated
     * with. The names are disjoint from the names of the {@link #children} mapping.
     */
    protected final Map<String, DeclWrap<?>> declarations = map();

    /** The nameless invariants of this scope. */
    protected final List<InvariantInfo> namelessInvariants = list();

    /** Mapping from variable names to their equations. */
    public Map<String, List<AEquation>> astEquations = map();

    /**
     * Mapping from CIF AST representations of equations to the corresponding metamodel representations of those
     * equations. If {@code null}, further checking for missing and superfluous equations, as well as equations that
     * don't refer to a variable of this scope, should be omitted.
     */
    public Map<AEquation, Equation> mmEquations = map();

    /** Initialization predicates of this scope. */
    protected List<AInitialDecl> astInitPreds = list();

    /** Marker predicates of this scope. */
    protected List<AMarkedDecl> astMarkerPreds = list();

    /** I/O declarations of this scope. */
    protected List<AIoDecl> astIoDecls = list();

    /**
     * Constructor for the {@link ParentScope} class.
     *
     * @param obj The CIF metamodel object representing this scope.
     * @param parent The parent scope, or {@code null} for root scopes.
     * @param tchecker The CIF type checker to use.
     */
    public ParentScope(T obj, ParentScope<?> parent, CifTypeChecker tchecker) {
        super(obj, parent, tchecker);
    }

    /**
     * Returns the type name of the scope. Used in debugging output.
     *
     * @return The type name of the scope.
     */
    protected abstract String getScopeTypeName();

    /**
     * Returns the {@link ComplexComponent} metamodel object for this parent scope.
     *
     * @return The {@link ComplexComponent} metamodel object for this parent scope.
     * @throws UnsupportedOperationException If this scope does not represent a complex component.
     */
    protected abstract ComplexComponent getComplexComponent();

    /**
     * Returns the {@link Group} metamodel object for this parent scope.
     *
     * @return The {@link Group} metamodel object for this parent scope.
     * @throws UnsupportedOperationException If this scope does not represent a group.
     */
    protected abstract Group getGroup();

    /**
     * Returns the {@link ComponentDef} metamodel object for this parent scope.
     *
     * @return The {@link ComponentDef} metamodel object for this parent scope.
     * @throws UnsupportedOperationException If this scope does not represent a component definition.
     */
    protected abstract ComponentDef getComponentDef();

    /**
     * Returns the {@link Automaton} metamodel object for this parent scope.
     *
     * @return The {@link Automaton} metamodel object for this parent scope.
     * @throws UnsupportedOperationException If this scope does not represent an automaton.
     */
    protected abstract Automaton getAutomaton();

    /**
     * Returns the CIF AST locations of this automaton, or {@code null} if not applicable.
     *
     * @return The CIF AST locations of this automaton, or {@code null}.
     */
    public abstract List<ALocation> getAstLocs();

    /**
     * Adds equations to this scope.
     *
     * @param eqns The equations to add.
     * @see #astEquations
     */
    public void addEquations(List<AEquation> eqns) {
        for (AEquation eqn: eqns) {
            // Add to mapping from variable names to AST equations.
            List<AEquation> prev = astEquations.get(eqn.variable.id);
            if (prev == null) {
                prev = list();
                astEquations.put(eqn.variable.id, prev);
            }
            prev.add(eqn);
        }
    }

    /**
     * Adds a declaration to this scope.
     *
     * @param decl The symbol table entry of the declaration to add.
     */
    public void addDeclaration(DeclWrap<?> decl) {
        // Check name uniqueness.
        checkUniqueName(decl);

        // Store declaration.
        declarations.put(decl.getName(), decl);
    }

    /**
     * Adds an invariant to this scope.
     *
     * @param invariantInfo The type check info of the invariant to add.
     */
    public void addInvariant(InvariantInfo invariantInfo) {
        if (invariantInfo.astInv.name != null) {
            // For named invariants, create a symbol table entry.
            InvDeclWrap wrapper = new InvDeclWrap(tchecker, this, invariantInfo);

            // Check name uniqueness.
            checkUniqueName(wrapper);

            // Store invariant.
            declarations.put(wrapper.getName(), wrapper);

        } else {
            // For nameless invariants, add it to the nameless invariants of this scope.
            namelessInvariants.add(invariantInfo);
        }
    }

    /**
     * Adds a child scope to this scope.
     *
     * @param scope The symbol table entry of the child scope to add.
     * @throws UnsupportedOperationException If this scope does not support child scopes.
     */
    public void addChildScope(SymbolScope<?> scope) {
        // Check name uniqueness.
        checkUniqueName(scope);

        // Store child scope.
        children.put(scope.getName(), scope);
    }

    /**
     * Checks a new symbol table entry, that is to be added to this scope, for uniqueness against the already present
     * child scope names and local declaration names.
     *
     * @param newEntry The new entry, to check for uniqueness.
     */
    private void checkUniqueName(SymbolTableEntry newEntry) {
        // Get name of the entry.
        String name = newEntry.getName();

        // Check against child scopes.
        SymbolScope<?> prevScope = children.get(name);
        if (prevScope != null) {
            // Name conflicts with existing scope.
            tchecker.addProblem(ErrMsg.DUPLICATE_NAME, newEntry.getPosition(), name, getAbsText());
            tchecker.addProblem(ErrMsg.DUPLICATE_NAME, prevScope.getPosition(), name, getAbsText());
            throw new SemanticException();
        }

        // Check against local declarations.
        DeclWrap<?> prevDecl = declarations.get(name);
        if (prevDecl != null) {
            // Name conflicts with existing declaration.
            tchecker.addProblem(ErrMsg.DUPLICATE_NAME, newEntry.getPosition(), name, getAbsText());
            tchecker.addProblem(ErrMsg.DUPLICATE_NAME, prevDecl.getPosition(), name, getAbsText());
            throw new SemanticException();
        }
    }

    /**
     * Is there a definition (declaration or child) with the given name in this scope?
     *
     * @param name The name of the declaration or child.
     * @return {@code true} if there is a definition with the given name in this scope, {@code false} otherwise.
     * @see #getEntry
     */
    public boolean defines(String name) {
        return children.containsKey(name) || declarations.containsKey(name);
    }

    @Override
    public final void tcheckForUseImpl() {
        // Check for reserved names.
        checkName();

        // Perform additional tasks to type check this scope 'for use'.
        tcheckScopeForUse();

        // We still need to process the declarations and children, but as
        // far the type of the scope is concerned, this scope is now checked
        // 'for use'.
        status = CheckStatus.USE;
    }

    @Override
    public final void tcheckFull() {
        // First, check 'for use', and make sure we haven't checked it before.
        tcheckForUse();
        if (isCheckedFull()) {
            return;
        }

        // Check all child declarations and scopes.
        boolean failed = false;

        // Type check declarations. For invariants, this includes only the named ones.
        for (DeclWrap<?> decl: declarations.values()) {
            try {
                decl.tcheckFull();
            } catch (SemanticException ex) {
                // Ignore and continue.
                failed = true;
            }
        }

        // Type check nameless invariants.
        for (InvariantInfo namelessInvariant: namelessInvariants) {
            try {
                InvDeclWrap.tcheckFull(tchecker, this, namelessInvariant);
            } catch (SemanticException ex) {
                // Ignore and continue.
                failed = true;
            }
        }

        // Type check child scopes.
        for (SymbolScope<?> child: children.values()) {
            try {
                child.tcheckFull();
            } catch (SemanticException ex) {
                // Ignore and continue.
                failed = true;
            }
        }

        // Type check initialization predicates and marker predicates.
        tcheckInitMarked();

        // Complete the type checking of equations. Don't report that variables
        // are not in scope, if the equations was not added due to failure to
        // check the variable to which the equation belongs. So, if any of
        // them failed, skip this check.
        if (failed) {
            // Skip further checking of equations.
            mmEquations = null;
        } else {
            for (List<AEquation> eqns: astEquations.values()) {
                for (AEquation astEqn: eqns) {
                    Equation eqn = mmEquations.get(astEqn);
                    if (eqn != null) {
                        getComplexComponent().getEquations().add(eqn);
                    } else {
                        tchecker.addProblem(ErrMsg.EQN_VAR_NOT_IN_SCOPE, astEqn.position, astEqn.variable.id,
                                getAbsText());
                        // Non-fatal error.
                    }
                }
            }
        }

        // Type check the I/O declarations. Skip scopes that are not complex
        // components: since they should also not have any I/O declarations,
        // we skip scopes without I/O declarations.
        if (!astIoDecls.isEmpty()) {
            tchecker.ioDeclChecker.check(astIoDecls, this, this.getComplexComponent());
        }

        // Perform additional tasks to fully type check this scope.
        tcheckScopeFull();

        // This scope is now fully checked.
        status = CheckStatus.FULL;
    }

    /**
     * Performs additional tasks to type check this scope 'for use'. It should be assumed that the {@link #declarations}
     * and {@link #children} have not yet been checked at all. This method allow for additional 'for use' checking, on
     * top of what the {@link ParentScope} class provides, prior to checking the declarations and children.
     *
     * <p>
     * Must only be called by the {@link #tcheckForUse} method. This method is guaranteed to be called at most once for
     * each scope.
     * </p>
     *
     * <p>
     * The default implementation of this method does nothing. Derived classes may override it to perform additional
     * tasks.
     * </p>
     */
    protected void tcheckScopeForUse() {
        // By default, do nothing.
    }

    /**
     * Performs additional tasks to fully type check this scope. The {@link #declarations} and {@link #children} have
     * already been fully checked. This method allow for additional checking on the scope, on top of what the
     * {@link ParentScope} class provides for all scopes.
     *
     * <p>
     * Must only be called by the {@link #tcheckFull} method. This method is guaranteed to be called at most once for
     * each scope.
     * </p>
     *
     * <p>
     * The default implementation of this method does nothing. Derived classes may override it to perform additional
     * tasks.
     * </p>
     */
    protected void tcheckScopeFull() {
        // By default, do nothing.
    }

    /**
     * Performs type checking on the initialization predicates and marker predicates of this scope. Also adds them to
     * the metamodel object.
     */
    private void tcheckInitMarked() {
        // Skip scopes that do not have initialization predicates and marker predicates.
        if (astInitPreds.isEmpty() && astMarkerPreds.isEmpty()) {
            return;
        }

        // Get component to which to add the predicates.
        ComplexComponent ccomp = getComplexComponent();

        // Process initialization predicates.
        for (AInitialDecl astPreds: astInitPreds) {
            for (AExpression pred: astPreds.preds) {
                Expression pred2 = transExpression(pred, BOOL_TYPE_HINT, this, null, tchecker);
                ccomp.getInitials().add(pred2);

                CifType t = pred2.getType();
                CifType nt = CifTypeUtils.normalizeType(t);
                if (!(nt instanceof BoolType)) {
                    tchecker.addProblem(ErrMsg.INIT_NON_BOOL, pred.position, CifTextUtils.typeToStr(t));
                    // Non-fatal error.
                }
            }
        }

        // Process marker predicates.
        for (AMarkedDecl astPreds: astMarkerPreds) {
            for (AExpression pred: astPreds.preds) {
                Expression pred2 = transExpression(pred, BOOL_TYPE_HINT, this, null, tchecker);
                ccomp.getMarkeds().add(pred2);

                CifType t = pred2.getType();
                CifType nt = CifTypeUtils.normalizeType(t);
                if (!(nt instanceof BoolType)) {
                    tchecker.addProblem(ErrMsg.MARKED_NON_BOOL, pred.position, CifTextUtils.typeToStr(t));
                    // Non-fatal error.
                }
            }
        }
    }

    @Override
    public Box toBox() {
        // Special case for empty scope.
        if (children.isEmpty() && declarations.isEmpty()) {
            return new TextBox(fmt("[ %s scope \"%s\" for: %s ]", getScopeTypeName(), getName(), obj));
        }

        // Generic case.
        int rowCount = children.size() + declarations.size() + 1;
        GridBox grid = new GridBox(rowCount, 4);
        int rowNr = 0;
        for (SymbolScope<?> child: children.values()) {
            grid.set(rowNr, 0, new TextBox(", "));
            grid.set(rowNr, 1, new TextBox(child.getName()));
            grid.set(rowNr, 2, new TextBox(" : "));
            grid.set(rowNr, 3, child.toBox());
            rowNr++;
        }
        for (DeclWrap<?> decl: declarations.values()) {
            grid.set(rowNr, 0, new TextBox(", "));
            grid.set(rowNr, 1, new TextBox(decl.getName()));
            grid.set(rowNr, 2, new TextBox(" : "));
            grid.set(rowNr, 3, new TextBox(decl.toString()));
            rowNr++;
        }
        grid.set(rowCount - 1, 0, new TextBox("]"));

        return new VBox(fmt("[ %s scope \"%s\" for: %s", getScopeTypeName(), getName(), obj), grid);
    }

    @Override
    @SuppressWarnings("null")
    protected SymbolTableEntry resolve1(Position position, String id, String done, CifTypeChecker tchecker,
            SymbolScope<?> origScope)
    {
        SymbolTableEntry child = children.get(id);
        if (child != null) {
            if (child instanceof CompParamScope) {
                ((CompParamScope)child).used = true;
            }
            if (child instanceof FunctionScope) {
                ((FunctionScope)child).used = true;
            }
            return child;
        }

        DeclWrap<?> decl = declarations.get(id);
        if (decl != null) {
            decl.used = true;
            return decl;
        }

        if (done.isEmpty() && !isRootScope()) {
            // Resolve via parent. If we get a result, it is 'in scope'.
            SymbolTableEntry entry = parent.resolve1(position, id, done, tchecker, origScope);
            return entry;
        }

        // Failed to resolve 'id'.
        boolean isVia = !done.isEmpty();
        Assert.ifAndOnlyIf(isVia, origScope == null);

        String scopeTxt;
        if (isVia) {
            // Use absolute text of this scope, as we resolve 'via' this scope,
            // and don't defer to higher levels.
            scopeTxt = getAbsText();
        } else {
            // Get the first 'parent' scope in the ancestor hierarchy of the
            // original scope, including the original scope itself, so that we
            // can ask for an absolute reference text.
            SymbolScope<?> namedScope = origScope;
            while (!(namedScope instanceof ParentScope<?>)) {
                namedScope = namedScope.parent;
            }
            scopeTxt = ((ParentScope<?>)namedScope).getAbsText();
        }

        String postfixTxt;
        if (isVia) {
            // 'Via' references don't look at higher levels.
            postfixTxt = "";
        } else {
            // Not a 'via' reference: could have originated at lower level.
            Assert.check(isRootScope());
            if (this == origScope) {
                // The specification has no higher level.
                postfixTxt = "";
            } else {
                // We also looked at higher levels.
                postfixTxt = " or at a higher level";
            }
        }

        tchecker.addProblem(ErrMsg.RESOLVE_NOT_FOUND, position, id, scopeTxt, postfixTxt);
        throw new SemanticException();
    }

    /**
     * Returns the symbol table entry for the child or declaration with the given name, from this scope (and not from
     * its ancestors, etc).
     *
     * @param name The name of the child or declaration. Such a child or declaration must exist in this scope.
     * @return The symbol table entry for the child or declaration.
     * @see #defines
     */
    public SymbolTableEntry getEntry(String name) {
        SymbolTableEntry entry = children.get(name);
        if (entry == null) {
            entry = declarations.get(name);
        }
        Assert.notNull(entry);
        return entry;
    }

    @Override
    public void detectCompDefInstCycles(List<ParentScope<?>> cycle) {
        boolean isCompDef = this instanceof AutDefScope || this instanceof GroupDefScope;

        // Add component definition to cycle detection, and detect cycles.
        if (isCompDef) {
            if (!cycle.contains(this)) {
                // Not yet present: just add it.
                cycle.add(this);
            } else {
                // Already present: cycle detected. Find ourselves in the
                // cycle.
                int idx = cycle.indexOf(this);
                Assert.check(idx >= 0);

                // Add error for each element in the found cycle.
                for (int i = idx; i < cycle.size(); i++) {
                    ParentScope<?> startEntry = cycle.get(i);
                    StringBuilder txt = new StringBuilder();
                    for (int j = i; j < cycle.size(); j++) {
                        if (txt.length() > 0) {
                            txt.append(" -> ");
                        }
                        txt.append(CifTextUtils.getAbsName(cycle.get(j).getObject()));
                    }
                    for (int j = idx; j <= i; j++) {
                        if (txt.length() > 0) {
                            txt.append(" -> ");
                        }
                        txt.append(CifTextUtils.getAbsName(cycle.get(j).getObject()));
                    }
                    tchecker.addProblem(ErrMsg.COMP_DEF_INST_CYCLE, startEntry.getPosition(),
                            CifTextUtils.getAbsName(startEntry.getObject()), txt.toString());
                }
                throw new SemanticException();
            }
        }

        // Process child scopes.
        for (SymbolScope<?> child: children.values()) {
            child.detectCompDefInstCycles(cycle);
        }

        // Remove component definition from cycle detection.
        if (isCompDef) {
            ParentScope<?> top = cycle.remove(cycle.size() - 1);
            if (top != this) {
                throw new IllegalStateException("top of cycle != this");
            }
        }
    }

    /**
     * Find unused declarations, recursively over the component structure.
     *
     * <p>
     * Components resulting from imports are skipped, as unused declarations from the imported specification already
     * resulted in a warning on the import. If we were to include the warnings from imported declarations, their
     * position information would be invalid for the current specification.
     * </p>
     */
    public void findUnusedDecls() {
        // Process all child scopes.
        for (SymbolScope<?> child: children.values()) {
            // Check for unused component parameters.
            if (child instanceof CompParamScope) {
                CompParamScope paramScope = (CompParamScope)child;
                if (!paramScope.used) {
                    tchecker.addProblem(ErrMsg.UNUSED_DECL, paramScope.getPosition(), "Component parameter",
                            paramScope.getAbsName());
                    // Non-fatal error.
                }
            }

            // Check for unused user-defined functions.
            // NOTE: Unused checks temporarily disabled to avoid false positives.
            //
            // if (child instanceof FunctionScope) {
            // FunctionScope funcScope = (FunctionScope)child;
            // if (!funcScope.used) {
            // getTypeChecker().addProblem(ErrMsg.UNUSED_DECL,
            // funcScope.getPosition(),
            // "Function",
            // funcScope.getName());
            // // Non-fatal error.
            // }
            // }

            // Process child scopes.
            if (!(child instanceof ParentScope<?>)) {
                continue;
            }
            ((ParentScope<?>)child).findUnusedDecls();
        }

        // Process all declarations.
        for (DeclWrap<?> decl: declarations.values()) {
            if (!decl.used) {
                // Get description of the type of declaration.
                String description;
                if (decl instanceof AlgVariableDeclWrap) {
                    // NOTE: Unused checks temporarily disabled to avoid false positives.
                    //
                    // description = "Algebraic variable";
                    continue;
                } else if (decl instanceof ConstDeclWrap) {
                    // NOTE: Unused checks temporarily disabled to avoid false positives.
                    //
                    // description = "Constant";
                    continue;
                } else if (decl instanceof ContVariableDeclWrap) {
                    // NOTE: Unused checks temporarily disabled to avoid false positives.
                    //
                    // description = "Continuous variable";
                    continue;
                } else if (decl instanceof DiscVariableDeclWrap) {
                    description = "Discrete variable";
                } else if (decl instanceof EnumDeclWrap) {
                    // NOTE: Unused checks temporarily disabled to avoid false positives.
                    //
                    // description = "Enumeration";
                    continue;
                } else if (decl instanceof EnumLiteralDeclWrap) {
                    // NOTE: Unused checks temporarily disabled to avoid false positives.
                    //
                    // description = "Enumeration literal";
                    continue;
                } else if (decl instanceof EventDeclWrap) {
                    description = "Event";
                } else if (decl instanceof FormalAlgDeclWrap) {
                    description = "Algebraic parameter";
                } else if (decl instanceof FormalEventDeclWrap) {
                    description = "Event parameter";
                } else if (decl instanceof FormalLocationDeclWrap) {
                    description = "Location parameter";
                } else if (decl instanceof FuncParamDeclWrap) {
                    description = "Function parameter";
                } else if (decl instanceof FuncVariableDeclWrap) {
                    description = "Variable";
                } else if (decl instanceof InputVariableDeclWrap) {
                    // NOTE: Unused checks temporarily disabled to avoid false positives.
                    //
                    // description = "Input variable";
                    continue;
                } else if (decl instanceof LocationDeclWrap) {
                    // Skip unused locations.
                    continue;
                } else if (decl instanceof TypeDeclWrap) {
                    // NOTE: Unused checks temporarily disabled to avoid false positives.
                    //
                    // description = "Type";
                    continue;
                } else if (decl instanceof InvDeclWrap) {
                    // Invariants are never unused.
                    continue;
                } else {
                    throw new RuntimeException("Unknown decl: " + decl);
                }

                // Add problem.
                tchecker.addProblem(ErrMsg.UNUSED_DECL, decl.getPosition(), description, decl.getAbsName());
                // Non-fatal error.
            }
        }
    }
}
