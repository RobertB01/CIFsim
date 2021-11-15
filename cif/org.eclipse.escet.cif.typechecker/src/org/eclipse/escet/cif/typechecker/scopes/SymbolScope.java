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

package org.eclipse.escet.cif.typechecker.scopes;

import static org.eclipse.escet.cif.common.CifScopeUtils.isParamRefExpr;
import static org.eclipse.escet.cif.common.CifTextUtils.escapeIdentifier;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newAlgVariableExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBoolType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newCompInstWrapExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newCompInstWrapType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newCompParamWrapExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newCompParamWrapType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newComponentDefType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newComponentExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newComponentType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newConstantExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newContVariableExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newDiscVariableExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEnumLiteralExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEnumType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEventExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newFuncType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newFunctionExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newInputVariableExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newLocationExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newRealType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newTypeRef;
import static org.eclipse.escet.common.position.common.PositionUtils.copyPosition;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.escet.cif.common.CifScopeUtils;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.ComponentDef;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Constant;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumLiteral;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.TypeDecl;
import org.eclipse.escet.cif.metamodel.cif.expressions.AlgVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CompInstWrapExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CompParamWrapExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ComponentExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ConstantExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ContVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EnumLiteralExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.FunctionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.InputVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.LocationExpression;
import org.eclipse.escet.cif.metamodel.cif.functions.Function;
import org.eclipse.escet.cif.metamodel.cif.functions.FunctionParameter;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.CompInstWrapType;
import org.eclipse.escet.cif.metamodel.cif.types.CompParamWrapType;
import org.eclipse.escet.cif.metamodel.cif.types.ComponentDefType;
import org.eclipse.escet.cif.metamodel.cif.types.ComponentType;
import org.eclipse.escet.cif.metamodel.cif.types.EnumType;
import org.eclipse.escet.cif.metamodel.cif.types.FuncType;
import org.eclipse.escet.cif.metamodel.cif.types.RealType;
import org.eclipse.escet.cif.metamodel.cif.types.TypeRef;
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
import org.eclipse.escet.cif.typechecker.declwrap.LocationDeclWrap;
import org.eclipse.escet.cif.typechecker.declwrap.TypeDeclWrap;
import org.eclipse.escet.common.box.Boxable;
import org.eclipse.escet.common.emf.EMFHelper;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;
import org.eclipse.escet.common.typechecker.SemanticException;
import org.eclipse.escet.common.typechecker.SemanticProblem;

/**
 * A single scope in the symbol table of the CIF language.
 *
 * @param <T> The type of the CIF metamodel object that represents this scope.
 */
public abstract class SymbolScope<T extends PositionObject> extends SymbolTableEntry implements Boxable {
    /** The CIF metamodel object representing this scope. */
    protected final T obj;

    /** The parent scope, or {@code null} for root scopes. */
    protected ParentScope<?> parent;

    /**
     * Constructor for the {@link SymbolScope} class.
     *
     * @param obj The CIF metamodel object representing this scope.
     * @param parent The parent scope, or {@code null} for root scopes.
     * @param tchecker The CIF type checker to use.
     */
    public SymbolScope(T obj, ParentScope<?> parent, CifTypeChecker tchecker) {
        super(tchecker);
        this.obj = obj;
        this.parent = parent;

        // Add this child scope to the parent, if any.
        Assert.ifAndOnlyIf(isRootScope(), parent == null);
        if (parent != null) {
            parent.addChildScope(this);
        }
    }

    @Override
    public T getObject() {
        return obj;
    }

    @Override
    public Position getPosition() {
        return obj.getPosition();
    }

    /**
     * Returns the parent scope, or {@code null} for root scopes.
     *
     * @return The parent scope, or {@code null}.
     */
    public ParentScope<?> getParent() {
        return parent;
    }

    @Override
    public void changeParent(ParentScope<?> parent) {
        this.parent = parent;
    }

    /**
     * Is this scope a sub-scope?
     *
     * @return {@code true} if it is a sub-scope, {@code false} otherwise.
     */
    protected abstract boolean isSubScope();

    /**
     * Is this scope a root scope?
     *
     * @return {@code true} if it is a root scope, {@code false} otherwise.
     */
    protected abstract boolean isRootScope();

    /**
     * Returns an end-user readable textual (reference) representation of the scope, mostly for use in error messages.
     *
     * <p>
     * Can for instance be used in {@code "... in %s."} messages.
     * </p>
     *
     * <p>
     * If the text is to be used at the beginning of a sentence, the first letter can be {@link StringUtils#capitalize
     * capitalized}. Implementations of this method should ensure that the result is valid.
     * </p>
     *
     * @return The textual representation, using absolute names, with escaping of identifiers.
     * @see #getAbsName
     */
    public abstract String getAbsText();

    /**
     * Resolves a textual reference against this scope. Note that {@code $} characters have already been removed by the
     * parser.
     *
     * @param position Position information for the textual reference.
     * @param name The textual reference to resolve.
     * @param tchecker The type checker to which to add 'resolve' failures, if any.
     * @param origScope The scope where the original textual reference is a part of. Used for checking convoluted
     *     references. May be {@code null} to skip checking.
     * @return The resolved symbol table entry.
     */
    public SymbolTableEntry resolve(Position position, String name, CifTypeChecker tchecker, SymbolScope<?> origScope) {
        // Root absolute name.
        if (name.startsWith("^")) {
            // If we are already at the root, resolve it relatively.
            if (isRootScope()) {
                // Resolve entry.
                SymbolTableEntry entry = resolve(position, name.substring(1), "^", tchecker, null);

                // Warn for convoluted reference. That is, a root name is used for a local declaration.
                warnIfConvolutedReference(position, tchecker, entry, origScope);
                return entry;
            }

            // Move up the hierarchy to the root. If we get a result, it is
            // 'in scope'.
            return parent.resolve(position, name, tchecker, origScope);
        }

        // Scope absolute name.
        if (name.startsWith(".")) {
            // If we are at the root of the current scope, resolve it
            // relatively.
            if (!isSubScope()) {
                // Resolve entry.
                SymbolTableEntry entry = resolve(position, name.substring(1), ".", tchecker, null);

                // Warn for convoluted reference. That is, an absolute name is used for a local declaration.
                warnIfConvolutedReference(position, tchecker, entry, origScope);
                return entry;
            }

            // Move up the hierarchy to the root of the scope. The result
            // should always be 'in scope', as we resolve it relative to the
            // root of the current scope.
            Assert.check(isSubScope());
            return parent.resolve(position, name, tchecker, origScope);
        }

        // Relative name. Resolve from this scope.
        return resolve(position, name, "", tchecker, this);
    }

    /**
     * Warn if the scope of the given entry is equal to the original scope. This can be used to warn in case a relative,
     * absolute, or root names is used to reference a local declaration.
     *
     * @param position Position information for the textual reference.
     * @param tchecker The type checker to which to add 'convoluted reference' warnings, if any.
     * @param entry The resolved symbol table entry for the reference.
     * @param origScope The scope where the original textual reference is a part of. May be {@code null} to not give a
     *     warning.
     */
    private void warnIfConvolutedReference(Position position, CifTypeChecker tchecker, SymbolTableEntry entry,
            SymbolScope<?> origScope)
    {
        // Skip if original scope is not supplied.
        if (origScope == null) {
            return;
        }

        // Skip automaton definition and group definition scope as these can result in false positive. That is, the
        // definition itself can reference different instantiations of itself. However, these will share the same scope.
        if (origScope instanceof AutDefScope || origScope instanceof GroupDefScope) {
            return;
        }

        // Only warnings for references to declarations.
        if (!(entry instanceof DeclWrap)) {
            return;
        }

        // Warn if the original scope is equal to the scope of the entry.
        ParentScope<?> entryParentScope = ((DeclWrap<?>)entry).getParent();
        if (entryParentScope.equals(origScope)) {
            tchecker.addProblem(ErrMsg.CONVOLUTED_REF, position, escapeIdentifier(entry.getName()));
        }
    }

    /**
     * Resolves a textual reference against this scope. Note that:
     * <ul>
     * <li>{@code $} characters have already been removed by the parser.</li>
     * <li>{@code ^} and {@code .} prefixes have already been handled by the
     * {@link #resolve(Position, String, CifTypeChecker, SymbolScope)} method.</li>
     * </ul>
     *
     * @param position Position information for the textual reference.
     * @param name The textual reference to resolve.
     * @param done The prefix of {@code name} (which is not part of {@code name}) that is already done (has already been
     *     processed). This resolve is a 'via' resolve if and only if {@code !done.isEmpty()}.
     * @param tchecker The type checker to which to add 'resolve' failures, if any.
     * @param origScope The original scope, i.e. the scope from which we start resolving the first identifier of the
     *     reference, after absolute reference prefix symbols have already been processed. Is {@code null} if and only
     *     if this is a 'via' resolve.
     * @return The resolved symbol table entry.
     */
    private SymbolTableEntry resolve(Position position, String name, String done, CifTypeChecker tchecker,
            SymbolScope<?> origScope)
    {
        // Paranoia checking.
        boolean isViaResolve = !done.isEmpty();
        Assert.ifAndOnlyIf(origScope == null, isViaResolve);

        // Get first identifier from 'name'.
        int idx = name.indexOf('.');
        String id = (idx == -1) ? name : name.substring(0, idx);

        // Resolve the first identifier.
        SymbolTableEntry entry = resolve1(position, id, done, tchecker, origScope);

        // If there is a part of the textual reference left, we should resolve
        // that via the scope we just resolved.
        if (idx != -1) {
            // We processed one more identifier of the reference.
            name = name.substring(idx + 1);
            done += id + ".";

            // Make sure the resolved scope is actually a scope.
            if (!(entry instanceof SymbolScope<?>)) {
                tchecker.addProblem(ErrMsg.RESOLVE_VIA_NON_SCOPE, position, name, entry.getAbsName());
                throw new SemanticException();
            }

            // We are not allowed to walk into a component definition scope.
            if (entry instanceof AutDefScope || entry instanceof GroupDefScope) {
                tchecker.addProblem(ErrMsg.RESOLVE_VIA_COMPDEF, position, name, entry.getAbsName());
                throw new SemanticException();
            }

            // We are not allowed to walk into a function scope.
            if (entry instanceof FunctionScope) {
                tchecker.addProblem(ErrMsg.RESOLVE_VIA_FUNC, position, name, entry.getAbsName());
                throw new SemanticException();
            }

            // Further resolve via the resolved scope.
            SymbolScope<?> scope = (SymbolScope<?>)entry;
            entry = scope.resolve(position, name, done, tchecker, null);
        }

        // Warn for convoluted reference. That is, a relative name is used for a local declaration.
        if (done.contains(".")) {
            warnIfConvolutedReference(position, tchecker, entry, origScope);
        }

        // Return the fully resolved symbol table entry.
        return entry;
    }

    /**
     * Resolves a textual identifier reference against this scope. Note that:
     * <ul>
     * <li>{@code $} characters have already been removed by the parser.</li>
     * <li>{@code ^} and {@code .} prefixes have already been handled by the
     * {@link #resolve(Position, String, CifTypeChecker, SymbolScope)} method.</li>
     * </ul>
     *
     * <p>
     * This method resolves a single identifier, not an entire textual reference.
     * </p>
     *
     * @param position Position information for the textual reference.
     * @param id The textual identifier reference to resolve.
     * @param done The prefix of {@code id} (which is not part of {@code id}) that is already done (has already been
     *     processed). This resolve is a 'via' resolve if and only if {@code !done.isEmpty()}.
     * @param tchecker The type checker to which to add 'resolve' failures, if any.
     * @param origScope The original scope, i.e. the scope from which we start resolving the first identifier of the
     *     reference, after absolute reference prefix symbols have already been processed. Must be {@code null} if this
     *     is a 'via' resolve. May otherwise only be {@code null} if the caller is sure that resolving won't fail.
     * @return The resolved symbol table entry.
     */
    protected abstract SymbolTableEntry resolve1(Position position, String id, String done, CifTypeChecker tchecker,
            SymbolScope<?> origScope);

    /**
     * Resolves a textual reference against this scope. Note that {@code $} characters have already been removed by the
     * parser.
     *
     * <p>
     * This method assumes that the resolving will succeed. As such, always use the {@link #resolve} method prior to
     * using this method.
     * </p>
     *
     * <p>
     * For the top level invocation (that is, a non-recursive call), the {@code done} parameter must be {@code ""}.
     * </p>
     *
     * <p>
     * Note that tuple fields have special scoping rules and are not handled by this method.
     * </p>
     *
     * @param name The textual reference to resolve.
     * @param position Position information for the textual reference.
     * @param done The prefix of {@code name} (which is not part of {@code name}) that is already done (has already been
     *     processed).
     * @param tchecker The type checker to which to add 'resolve' failures, if any.
     * @return The resolved object, as a possibly wrapped reference expression.
     */
    public Expression resolveAsExpr(String name, Position position, String done, CifTypeChecker tchecker) {
        // Root absolute name.
        if (name.startsWith("^")) {
            Assert.check(done.isEmpty());

            // If we are already at the root, resolve it relatively.
            if (isRootScope()) {
                return resolveAsExpr(name.substring(1), position, "^", tchecker);
            }

            // Move up the hierarchy to the root.
            return parent.resolveAsExpr(name, position, "", tchecker);
        }

        // Scope absolute name.
        if (name.startsWith(".")) {
            Assert.check(done.isEmpty());

            // If we are at the root of the current scope, resolve it
            // relatively.
            if (!isSubScope()) {
                return resolveAsExpr(name.substring(1), position, ".", tchecker);
            }

            // Move up the hierarchy to the root of the scope.
            return parent.resolveAsExpr(name, position, "", tchecker);
        }

        // Relative name. Resolve from this scope.
        // Get first identifier from 'name'.
        int idx = name.indexOf('.');
        String id = (idx == -1) ? name : name.substring(0, idx);

        // Resolve the first identifier.
        SymbolTableEntry entry = resolve1(null, id, done, tchecker, null);

        // If there is a part of the textual reference left, we should resolve
        // that via the scope we just resolved.
        if (idx != -1) {
            // We processed one more identifier of the reference.
            name = name.substring(idx + 1);
            done += id + ".";

            // Further resolve via the resolved scope.
            SymbolScope<?> scope = (SymbolScope<?>)entry;
            Expression rslt = scope.resolveAsExpr(name, position, done, tchecker);

            // Make sure we don't refer to a formal parameter via a component
            // instantiation or via a component parameter.
            if (scope instanceof CompInstScope || scope instanceof CompParamScope) {
                if (isParamRefExpr(rslt)) {
                    int nextIdx = name.indexOf('.');
                    String nextId = (nextIdx == -1) ? name : name.substring(0, nextIdx);
                    tchecker.addProblem(ErrMsg.COMP_PARAM_NOT_IN_SCOPE, position, id + '.' + nextId);
                    throw new SemanticException();
                }
            }

            // If the scope via which we further resolved needs expression and
            // type wrapping, add it.
            if (scope instanceof CompInstScope || scope instanceof CompParamScope) {
                // Get original type of the result.
                CifType rsltType = rslt.getType();

                // Get original scope, add wrapping expression to result (type
                // is set below), and create skeleton type wrapper instance.
                CifType wrapType;
                ParentScope<?> origScope;
                if (scope instanceof CompInstScope) {
                    // Get original scope.
                    CompInstScope iscope = (CompInstScope)scope;
                    origScope = iscope.getCompDefScope();

                    // Add wrapping expression to result (type is set below).
                    CompInstWrapExpression wrap = newCompInstWrapExpression();
                    wrap.setPosition(copyPosition(position));
                    wrap.setInstantiation(iscope.getObject());
                    wrap.setReference(rslt);
                    rslt = wrap;

                    // Create skeleton type wrapper instance.
                    CompInstWrapType instWrapType = newCompInstWrapType();
                    instWrapType.setPosition(copyPosition(position));
                    instWrapType.setInstantiation(iscope.getObject());
                    wrapType = instWrapType;
                } else {
                    // Get original scope.
                    Assert.check(scope instanceof CompParamScope);
                    CompParamScope pscope = (CompParamScope)scope;
                    origScope = pscope.getCompDefScope();

                    // Add wrapping expression to result (type is set below).
                    CompParamWrapExpression wrap = newCompParamWrapExpression();
                    wrap.setPosition(copyPosition(position));
                    wrap.setParameter(pscope.getObject());
                    wrap.setReference(rslt);
                    rslt = wrap;

                    // Create skeleton type wrapper instance.
                    CompParamWrapType paramWrapType = newCompParamWrapType();
                    paramWrapType.setPosition(copyPosition(position));
                    paramWrapType.setParameter(pscope.getObject());
                    wrapType = paramWrapType;
                }

                // Get new scope.
                SymbolScope<?> newScope = this;
                if (newScope instanceof CompInstScope) {
                    newScope = ((CompInstScope)newScope).getCompDefScope();
                } else if (newScope instanceof CompParamScope) {
                    newScope = ((CompParamScope)newScope).getCompDefScope();
                }

                // Set result type for the new wrapper expression. The original
                // result type (no deep cloning is performed) is used. This
                // means the the original result expression no longer has a
                // type.
                rslt.setType(rsltType);

                // Change the scope of the result type, in-place.
                CifScopeUtils.changeTypeScope(rsltType, wrapType, origScope.obj, newScope.obj);

                // Obtain new result type, as it may have been wrapped.
                rsltType = rslt.getType();

                // Set same updated type for reference expression, and all its
                // wrapper expressions. This excludes the just added wrapper
                // expression, as we set the type just above. We clone to get
                // unique copies. The new wrapper expression did not use
                // cloning, as we can use the original exactly once.
                Expression expr = rslt;
                while (expr instanceof CompParamWrapExpression || expr instanceof CompInstWrapExpression) {
                    // Move one level down.
                    if (expr instanceof CompParamWrapExpression) {
                        expr = ((CompParamWrapExpression)expr).getReference();
                    } else {
                        Assert.check(expr instanceof CompInstWrapExpression);
                        expr = ((CompInstWrapExpression)expr).getReference();
                    }

                    // Set updated type.
                    expr.setType(EMFHelper.deepclone(rsltType));
                }
            }

            // Return the result for this scope.
            return rslt;
        }

        // We just resolved the final part of the textual reference.
        if (entry instanceof AlgVariableDeclWrap) {
            AlgVariable a = ((AlgVariableDeclWrap)entry).getObject();

            AlgVariableExpression rslt = newAlgVariableExpression();
            rslt.setPosition(copyPosition(position));
            rslt.setVariable(a);
            rslt.setType(EMFHelper.deepclone(a.getType()));

            return rslt;
        } else if (entry instanceof ConstDeclWrap) {
            Constant c = ((ConstDeclWrap)entry).getObject();

            ConstantExpression rslt = newConstantExpression();
            rslt.setPosition(copyPosition(position));
            rslt.setConstant(c);
            rslt.setType(EMFHelper.deepclone(c.getType()));

            return rslt;
        } else if (entry instanceof ContVariableDeclWrap) {
            // Derivative boolean is not set here, as we only resolve the
            // variable itself. If this is a derivative reference, the caller
            // should set the boolean.
            ContVariable v = ((ContVariableDeclWrap)entry).getObject();

            RealType t = newRealType();
            t.setPosition(copyPosition(position));

            ContVariableExpression rslt = newContVariableExpression();
            rslt.setPosition(copyPosition(position));
            rslt.setVariable(v);
            rslt.setType(t);
            return rslt;
        } else if (entry instanceof DiscVariableDeclWrap) {
            DiscVariable v = ((DiscVariableDeclWrap)entry).getObject();

            DiscVariableExpression rslt = newDiscVariableExpression();
            rslt.setPosition(copyPosition(position));
            rslt.setVariable(v);
            rslt.setType(EMFHelper.deepclone(v.getType()));

            return rslt;
        } else if (entry instanceof EnumDeclWrap) {
            throw new RuntimeException("Can't ref enum decl in expr.");
        } else if (entry instanceof EnumLiteralDeclWrap) {
            EnumLiteral l = ((EnumLiteralDeclWrap)entry).getObject();
            EnumDecl e = (EnumDecl)l.eContainer();

            EnumType t = newEnumType();
            t.setEnum(e);
            t.setPosition(copyPosition(position));

            EnumLiteralExpression rslt = newEnumLiteralExpression();
            rslt.setPosition(copyPosition(position));
            rslt.setLiteral(l);
            rslt.setType(t);

            return rslt;
        } else if (entry instanceof EventDeclWrap) {
            Event e = ((EventDeclWrap)entry).getObject();

            BoolType t = newBoolType();
            t.setPosition(copyPosition(position));

            EventExpression rslt = newEventExpression();
            rslt.setPosition(copyPosition(position));
            rslt.setEvent(e);
            rslt.setType(t);

            return rslt;
        } else if (entry instanceof FormalAlgDeclWrap) {
            AlgVariable a = ((FormalAlgDeclWrap)entry).getObject().getVariable();

            AlgVariableExpression rslt = newAlgVariableExpression();
            rslt.setPosition(copyPosition(position));
            rslt.setVariable(a);
            rslt.setType(EMFHelper.deepclone(a.getType()));

            return rslt;
        } else if (entry instanceof FormalEventDeclWrap) {
            Event e = ((FormalEventDeclWrap)entry).getObject().getEvent();

            BoolType t = newBoolType();
            t.setPosition(copyPosition(position));

            EventExpression rslt = newEventExpression();
            rslt.setPosition(copyPosition(position));
            rslt.setEvent(e);
            rslt.setType(t);

            return rslt;
        } else if (entry instanceof FormalLocationDeclWrap) {
            Location l = ((FormalLocationDeclWrap)entry).getObject().getLocation();

            BoolType t = newBoolType();
            t.setPosition(copyPosition(position));

            LocationExpression rslt = newLocationExpression();
            rslt.setPosition(copyPosition(position));
            rslt.setLocation(l);
            rslt.setType(t);

            return rslt;
        } else if (entry instanceof FuncParamDeclWrap) {
            DiscVariable v = ((FuncParamDeclWrap)entry).getObject().getParameter();

            DiscVariableExpression rslt = newDiscVariableExpression();
            rslt.setPosition(copyPosition(position));
            rslt.setVariable(v);
            rslt.setType(EMFHelper.deepclone(v.getType()));

            return rslt;
        } else if (entry instanceof FuncVariableDeclWrap) {
            DiscVariable v = ((FuncVariableDeclWrap)entry).getObject();

            DiscVariableExpression rslt = newDiscVariableExpression();
            rslt.setPosition(copyPosition(position));
            rslt.setVariable(v);
            rslt.setType(EMFHelper.deepclone(v.getType()));

            return rslt;
        } else if (entry instanceof InputVariableDeclWrap) {
            InputVariable v = ((InputVariableDeclWrap)entry).getObject();

            InputVariableExpression rslt = newInputVariableExpression();
            rslt.setPosition(copyPosition(position));
            rslt.setVariable(v);
            rslt.setType(EMFHelper.deepclone(v.getType()));

            return rslt;
        } else if (entry instanceof LocationDeclWrap) {
            Location l = ((LocationDeclWrap)entry).getObject();

            BoolType t = newBoolType();
            t.setPosition(copyPosition(position));

            LocationExpression rslt = newLocationExpression();
            rslt.setPosition(copyPosition(position));
            rslt.setLocation(l);
            rslt.setType(t);

            return rslt;
        } else if (entry instanceof TypeDeclWrap) {
            throw new RuntimeException("Can't ref type decl in expr.");
        } else if (entry instanceof AutDefScope) {
            throw new RuntimeException("Can't ref aut def in expr.");
        } else if (entry instanceof AutScope) {
            Component c = ((AutScope)entry).getObject();

            ComponentType t = newComponentType();
            t.setComponent(c);
            t.setPosition(copyPosition(position));

            ComponentExpression rslt = newComponentExpression();
            rslt.setPosition(copyPosition(position));
            rslt.setComponent(c);
            rslt.setType(t);

            return rslt;
        } else if (entry instanceof CompInstScope) {
            Component c = ((CompInstScope)entry).getObject();

            ComponentType t = newComponentType();
            t.setComponent(c);
            t.setPosition(copyPosition(position));

            ComponentExpression rslt = newComponentExpression();
            rslt.setPosition(copyPosition(position));
            rslt.setComponent(c);
            rslt.setType(t);

            return rslt;
        } else if (entry instanceof GroupDefScope) {
            throw new RuntimeException("Can't ref group def in expr.");
        } else if (entry instanceof GroupScope) {
            Component c = ((GroupScope)entry).getObject();

            ComponentType t = newComponentType();
            t.setComponent(c);
            t.setPosition(copyPosition(position));

            ComponentExpression rslt = newComponentExpression();
            rslt.setPosition(copyPosition(position));
            rslt.setComponent(c);
            rslt.setType(t);

            return rslt;
        } else if (entry instanceof CompParamScope) {
            throw new RuntimeException("Can't ref comp param in expr.");
        } else if (entry instanceof FunctionScope) {
            Function f = ((FunctionScope)entry).getObject();

            CifType returnType = ((FunctionScope)entry).getReturnType();

            FuncType t = newFuncType();
            t.setReturnType(EMFHelper.deepclone(returnType));
            t.setPosition(copyPosition(position));
            for (FunctionParameter param: f.getParameters()) {
                CifType paramType = param.getParameter().getType();
                t.getParamTypes().add(EMFHelper.deepclone(paramType));
            }

            FunctionExpression rslt = newFunctionExpression();
            rslt.setPosition(copyPosition(position));
            rslt.setFunction(f);
            rslt.setType(t);

            return rslt;
        } else if (entry instanceof SpecScope) {
            throw new RuntimeException("Can't ref spec scope anywhere.");
        } else {
            throw new RuntimeException("Unknown symbol table entry: " + entry);
        }
    }

    /**
     * Resolves a textual reference against this scope. Note that {@code $} characters have already been removed by the
     * parser.
     *
     * <p>
     * This method assumes that the resolving will succeed. As such, always use the {@link #resolve} method prior to
     * using this method.
     * </p>
     *
     * <p>
     * For the top level invocation (that is, a non-recursive call), the {@code done} parameter must be {@code ""}.
     * </p>
     *
     * @param name The textual reference to resolve.
     * @param position Position information for the textual reference.
     * @param done The prefix of {@code name} (which is not part of {@code name}) that is already done (has already been
     *     processed).
     * @param tchecker The type checker to which to add 'resolve' failures, if any.
     * @return The resolved object, as a possibly wrapped reference type.
     */
    public CifType resolveAsType(String name, Position position, String done, CifTypeChecker tchecker) {
        // Root absolute name.
        if (name.startsWith("^")) {
            Assert.check(done.isEmpty());

            // If we are already at the root, resolve it relatively.
            if (isRootScope()) {
                return resolveAsType(name.substring(1), position, "^", tchecker);
            }

            // Move up the hierarchy to the root.
            return parent.resolveAsType(name, position, "", tchecker);
        }

        // Scope absolute name.
        if (name.startsWith(".")) {
            Assert.check(done.isEmpty());

            // If we are at the root of the current scope, resolve it
            // relatively.
            if (!isSubScope()) {
                return resolveAsType(name.substring(1), position, ".", tchecker);
            }

            // Move up the hierarchy to the root of the scope.
            return parent.resolveAsType(name, position, "", tchecker);
        }

        // Relative name. Resolve from this scope.
        // Get first identifier from 'name'.
        int idx = name.indexOf('.');
        String id = (idx == -1) ? name : name.substring(0, idx);

        // Resolve the first identifier.
        SymbolTableEntry entry = resolve1(null, id, done, tchecker, null);

        // If there is a part of the textual reference left, we should resolve
        // that via the scope we just resolved.
        if (idx != -1) {
            // We processed one more identifier of the reference.
            name = name.substring(idx + 1);
            done += id + ".";

            // Further resolve via the resolved scope.
            SymbolScope<?> scope = (SymbolScope<?>)entry;
            CifType rslt = scope.resolveAsType(name, position, done, tchecker);

            // Make sure we don't refer to a formal parameter via a component
            // instantiation or via a component parameter.
            if (scope instanceof CompInstScope || scope instanceof CompParamScope) {
                if (rslt instanceof CompParamWrapType) {
                    int nextIdx = name.indexOf('.');
                    String nextId = (nextIdx == -1) ? name : name.substring(0, nextIdx);
                    tchecker.addProblem(ErrMsg.COMP_PARAM_NOT_IN_SCOPE, position, id + '.' + nextId);
                    throw new SemanticException();
                }
            }

            // If the scope via which we further resolved needs a wrapping
            // type, add it.
            if (scope instanceof CompInstScope) {
                CompInstScope iscope = (CompInstScope)scope;

                CompInstWrapType wrap = newCompInstWrapType();
                wrap.setPosition(copyPosition(position));
                wrap.setInstantiation(iscope.getObject());
                wrap.setReference(rslt);

                rslt = wrap;
            }

            if (scope instanceof CompParamScope) {
                CompParamScope pscope = (CompParamScope)scope;

                CompParamWrapType wrap = newCompParamWrapType();
                wrap.setPosition(copyPosition(position));
                wrap.setParameter(pscope.getObject());
                wrap.setReference(rslt);

                rslt = wrap;
            }

            // Return the result for this scope.
            return rslt;
        }

        // We just resolved the final part of the textual reference.
        if (entry instanceof AlgVariableDeclWrap) {
            throw new RuntimeException("Can't ref alg variable in type.");
        } else if (entry instanceof ConstDeclWrap) {
            throw new RuntimeException("Can't ref constant in type.");
        } else if (entry instanceof ContVariableDeclWrap) {
            throw new RuntimeException("Can't ref cont variable in type.");
        } else if (entry instanceof DiscVariableDeclWrap) {
            throw new RuntimeException("Can't ref disc variable in type.");
        } else if (entry instanceof EnumDeclWrap) {
            EnumDecl e = ((EnumDeclWrap)entry).getObject();

            EnumType rslt = newEnumType();
            rslt.setPosition(copyPosition(position));
            rslt.setEnum(e);

            return rslt;
        } else if (entry instanceof EnumLiteralDeclWrap) {
            throw new RuntimeException("Can't ref enum literal in type.");
        } else if (entry instanceof EventDeclWrap) {
            throw new RuntimeException("Can't ref event in type.");
        } else if (entry instanceof FormalAlgDeclWrap) {
            throw new RuntimeException("Can't ref formal alg in type.");
        } else if (entry instanceof FormalEventDeclWrap) {
            throw new RuntimeException("Can't ref formal event in type.");
        } else if (entry instanceof FormalLocationDeclWrap) {
            throw new RuntimeException("Can't ref formal location in type.");
        } else if (entry instanceof FuncParamDeclWrap) {
            throw new RuntimeException("Can't ref func param in type.");
        } else if (entry instanceof FuncVariableDeclWrap) {
            throw new RuntimeException("Can't ref func local var in type.");
        } else if (entry instanceof InputVariableDeclWrap) {
            throw new RuntimeException("Can't ref input var in type.");
        } else if (entry instanceof LocationDeclWrap) {
            throw new RuntimeException("Can't ref location in type.");
        } else if (entry instanceof TypeDeclWrap) {
            TypeDecl t = ((TypeDeclWrap)entry).getObject();

            TypeRef rslt = newTypeRef();
            rslt.setPosition(copyPosition(position));
            rslt.setType(t);

            return rslt;
        } else if (entry instanceof AutDefScope) {
            ComponentDef d = ((AutDefScope)entry).getObject();

            ComponentDefType rslt = newComponentDefType();
            rslt.setDefinition(d);
            rslt.setPosition(copyPosition(position));

            return rslt;
        } else if (entry instanceof AutScope) {
            Component c = ((AutScope)entry).getObject();

            ComponentType rslt = newComponentType();
            rslt.setComponent(c);
            rslt.setPosition(copyPosition(position));

            return rslt;
        } else if (entry instanceof CompInstScope) {
            Component c = ((CompInstScope)entry).getObject();

            ComponentType rslt = newComponentType();
            rslt.setComponent(c);
            rslt.setPosition(copyPosition(position));

            return rslt;
        } else if (entry instanceof GroupDefScope) {
            ComponentDef d = ((GroupDefScope)entry).getObject();

            ComponentDefType rslt = newComponentDefType();
            rslt.setDefinition(d);
            rslt.setPosition(copyPosition(position));

            return rslt;
        } else if (entry instanceof GroupScope) {
            Component c = ((GroupScope)entry).getObject();

            ComponentType rslt = newComponentType();
            rslt.setComponent(c);
            rslt.setPosition(copyPosition(position));

            return rslt;
        } else if (entry instanceof CompParamScope) {
            throw new RuntimeException("Can't ref comp param in type.");
        } else if (entry instanceof FunctionScope) {
            throw new RuntimeException("Can't ref user-defined func in type.");
        } else if (entry instanceof SpecScope) {
            throw new RuntimeException("Can't ref spec scope anywhere.");
        } else {
            throw new RuntimeException("Unknown symbol table entry: " + entry);
        }
    }

    /**
     * Checks the name for validity. Names starting with {@code "e_"}, {@code "c_"}, or {@code "u_"} are reserved for
     * events.
     *
     * @see DeclWrap#checkName
     */
    protected void checkName() {
        // Skip nameless scopes.
        if (this instanceof SpecScope) {
            return;
        }

        // Check scope name.
        String name = getName();
        if (name.startsWith("e_") || name.startsWith("c_") || name.startsWith("u_")) {
            tchecker.addProblem(ErrMsg.RESERVED_NAME_PREFIX, getPosition(), getAbsName());
            // Non-fatal error.
        }
    }

    /**
     * Detect component definition/instantiation cycles.
     *
     * @param cycle The cycle detection list, which contains the component definition scopes that are in progress of
     *     being checked. If an item that is to be checked, is already present in this list, a cycle is detected. In
     *     that case, a {@link SemanticProblem} should be added to the type checker. The list is modified in-place.
     */
    public abstract void detectCompDefInstCycles(List<ParentScope<?>> cycle);
}
