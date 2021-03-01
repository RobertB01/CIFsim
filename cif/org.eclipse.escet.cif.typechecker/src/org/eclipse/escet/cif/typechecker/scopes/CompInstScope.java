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

import static org.eclipse.escet.cif.typechecker.CifExprsTypeChecker.BOOL_TYPE_HINT;
import static org.eclipse.escet.cif.typechecker.CifExprsTypeChecker.transExpression;
import static org.eclipse.escet.cif.typechecker.ExprContext.DEFAULT_CTXT;
import static org.eclipse.escet.cif.typechecker.ExprContext.Condition.ALLOW_EVENT;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;

import org.eclipse.escet.cif.common.CifEventUtils;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.RangeCompat;
import org.eclipse.escet.cif.metamodel.cif.AlgParameter;
import org.eclipse.escet.cif.metamodel.cif.ComponentDef;
import org.eclipse.escet.cif.metamodel.cif.ComponentInst;
import org.eclipse.escet.cif.metamodel.cif.ComponentParameter;
import org.eclipse.escet.cif.metamodel.cif.EventParameter;
import org.eclipse.escet.cif.metamodel.cif.LocationParameter;
import org.eclipse.escet.cif.metamodel.cif.Parameter;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.LocationExpression;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.ComponentDefType;
import org.eclipse.escet.cif.parser.ast.ACompInstDecl;
import org.eclipse.escet.cif.parser.ast.expressions.AExpression;
import org.eclipse.escet.cif.parser.ast.tokens.AName;
import org.eclipse.escet.cif.typechecker.CheckStatus;
import org.eclipse.escet.cif.typechecker.CifTypeChecker;
import org.eclipse.escet.cif.typechecker.ErrMsg;
import org.eclipse.escet.cif.typechecker.ExprContext;
import org.eclipse.escet.cif.typechecker.SymbolTableEntry;
import org.eclipse.escet.common.box.Box;
import org.eclipse.escet.common.box.TextBox;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Numbers;
import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.common.typechecker.SemanticException;

/** Component instantiation scope. */
public class CompInstScope extends SymbolScope<ComponentInst> {
    /** The expression type checking context to use for event references. */
    private static final ExprContext EVT_REF_CTXT = ExprContext.DEFAULT_CTXT.add(ALLOW_EVENT);

    /** The CIF AST component instantiation object representing this scope. */
    private final ACompInstDecl compInstDecl;

    /** The resolved component definition scope, or {@code null} if not yet resolved. */
    private ParentScope<?> compDefScope;

    /**
     * Constructor for the {@link CompInstScope} class.
     *
     * @param obj The CIF metamodel component instantiation object representing this scope.
     * @param compInstDecl The CIF AST component instantiation object representing this scope.
     * @param parent The parent scope.
     * @param tchecker The CIF type checker to use.
     */
    public CompInstScope(ComponentInst obj, ACompInstDecl compInstDecl, ParentScope<?> parent,
            CifTypeChecker tchecker)
    {
        super(obj, parent, tchecker);
        this.compInstDecl = compInstDecl;
    }

    /**
     * Returns the scope of the component definition instantiated by this component instantiation.
     *
     * @return The scope of the component definition.
     */
    public ParentScope<?> getCompDefScope() {
        Assert.notNull(compDefScope);
        return compDefScope;
    }

    @Override
    protected boolean isSubScope() {
        return true;
    }

    @Override
    protected boolean isRootScope() {
        return false;
    }

    @Override
    public String getName() {
        return obj.getName();
    }

    @Override
    public String getAbsName() {
        return CifTextUtils.getAbsName(obj);
    }

    @Override
    public String getAbsText() {
        // Make sure component definition is resolved.
        tcheckForUse();

        // Return text.
        if (compDefScope instanceof AutDefScope) {
            return fmt("automaton \"%s\"", getAbsName());
        } else if (compDefScope instanceof GroupDefScope) {
            return fmt("group \"%s\"", getAbsName());
        } else {
            throw new RuntimeException("Unknown comp def: " + compDefScope);
        }
    }

    @Override
    public Box toBox() {
        return new TextBox(fmt("[ compinst scope \"%s\" for: %s ]", getName(), obj));
    }

    @Override
    public void tcheckForUseImpl() {
        // Check for reserved names.
        checkName();

        // Type check the scope 'for use', by adding the information needed to
        // resolve via the scope.

        // We are starting this instantiation, so add it for cycle detection.
        tchecker.addToCycle(this);

        try {
            // Resolve the component definition.
            AName compDefRef = compInstDecl.defName;
            SymbolTableEntry entry = parent.resolve(compDefRef.position, compDefRef.name, tchecker);
            if (!(entry instanceof AutDefScope) && !(entry instanceof GroupDefScope)) {
                tchecker.addProblem(ErrMsg.RESOLVE_NOT_COMP_DEF, compDefRef.position, entry.getAbsName());
                throw new SemanticException();
            }

            // We don't type check the resolved component definition 'for use',
            // as we just use it as a 'via' reference.
            compDefScope = (ParentScope<?>)entry;

            // Set the definition on the instantiation.
            CifType compdef = parent.resolveAsType(compDefRef.name, compDefRef.position, "", tchecker);
            obj.setDefinition(compdef);

            // Make sure we don't refer to a component definition via a
            // component instantiation or component parameter.
            if (!(compdef instanceof ComponentDefType)) {
                tchecker.addProblem(ErrMsg.COMP_INST_DEF_NOT_IN_SCOPE, compDefRef.position, compDefScope.getAbsName());
                throw new SemanticException();
            }
        } finally {
            // We checked this instantiation, so remove it from cycle
            // detection.
            tchecker.removeFromCycle(this);
        }

        // This scope is now 'via' checked, and thus checked 'for use'.
        status = CheckStatus.USE;
    }

    @Override
    public void tcheckFull() {
        // First, check 'for use', and make sure we haven't checked it before.
        tcheckForUse();
        if (isCheckedFull()) {
            return;
        }

        // Make sure the parameters of the component definition scope are
        // checked, so that we can get the types of parameters, etc. Don't
        // check the body, as this component instantiation may be in that body.
        if (compDefScope instanceof AutDefScope) {
            ((AutDefScope)compDefScope).tcheckFullParams();
        } else if (compDefScope instanceof GroupDefScope) {
            ((GroupDefScope)compDefScope).tcheckFullParams();
        } else {
            String msg = "Unknown component def scope: " + compDefScope;
            throw new RuntimeException(msg);
        }

        // Get component definition.
        ComponentDef compDef = (ComponentDef)compDefScope.obj;

        // Parameter count.
        int paramCountInst = compInstDecl.parameters.size();
        int paramCountDef = compDef.getParameters().size();
        if (paramCountInst != paramCountDef) {
            tchecker.addProblem(ErrMsg.COMP_INST_PARAM_COUNT, obj.getPosition(), String.valueOf(paramCountInst),
                    getAbsName(), String.valueOf(paramCountDef), CifTextUtils.getAbsName(compDef));
            throw new SemanticException();
        }

        // Parameters.
        List<Expression> params = obj.getParameters();
        for (int i = 0; i < paramCountInst; i++) {
            // Textual representation of parameter index.
            String paramIdxTxt = Numbers.toOrdinal(i + 1);

            // Get formal parameter.
            Parameter formal = compDef.getParameters().get(i);

            // Get type hint for parameter.
            CifType paramHint;
            if (formal instanceof AlgParameter) {
                paramHint = ((AlgParameter)formal).getVariable().getType();
            } else if (formal instanceof EventParameter) {
                paramHint = BOOL_TYPE_HINT;
            } else if (formal instanceof LocationParameter) {
                paramHint = BOOL_TYPE_HINT;
            } else if (formal instanceof ComponentParameter) {
                paramHint = ((ComponentParameter)formal).getType();
            } else {
                throw new RuntimeException("Unknown formal param: " + formal);
            }

            // Transform actual parameter, and add it. Resolve any references
            // in the parent scope of the instantiation.
            ExprContext context = (formal instanceof EventParameter) ? EVT_REF_CTXT : DEFAULT_CTXT;
            AExpression param = compInstDecl.parameters.get(i);
            Expression actual = transExpression(param, paramHint, parent, context, tchecker);
            params.add(actual);

            // Check against formal parameter.
            if (formal instanceof AlgParameter) {
                CifType formalType = ((AlgParameter)formal).getVariable().getType();
                CifType actualType = actual.getType();

                if (!CifTypeUtils.checkTypeCompat(formalType, actualType, RangeCompat.CONTAINED)) {
                    tchecker.addProblem(ErrMsg.COMP_INST_PARAM_ALG_TYPES, actual.getPosition(), paramIdxTxt,
                            CifTextUtils.getAbsName(compDef), CifTextUtils.typeToStr(formalType),
                            CifTextUtils.typeToStr(actualType));
                    // Non-fatal error.
                }
            } else if (formal instanceof EventParameter) {
                // Make sure the actual parameter is an event.
                Expression unwrap = CifTypeUtils.unwrapExpression(actual);
                if (!(unwrap instanceof EventExpression)) {
                    tchecker.addProblem(ErrMsg.COMP_INST_PARAM_TYPE, actual.getPosition(), paramIdxTxt,
                            CifTextUtils.getAbsName(compDef), "event");
                    throw new SemanticException();
                }

                // Get events.
                Event formalEvent = ((EventParameter)formal).getEvent();
                Event actualEvent = ((EventExpression)unwrap).getEvent();

                // Check controllability match for formal/actual.
                Boolean formalContr = formalEvent.getControllable();
                Boolean actualContr = actualEvent.getControllable();
                if (formalContr != null && !formalContr.equals(actualContr)) {
                    tchecker.addProblem(ErrMsg.COMP_INST_CONTR_MISMATCH, actual.getPosition(), paramIdxTxt,
                            CifTextUtils.getAbsName(compDef), controllableToStr(formalContr),
                            controllableToStr(actualContr));
                    // Non-fatal error.
                }

                // Check type match for formal/actual.
                CifType formalType = formalEvent.getType();
                CifType actualType = actualEvent.getType();

                if ((formalType != null) && (actualType == null)) {
                    String formalTxt = fmt("is of type \"%s\"", CifTextUtils.typeToStr(formalType));
                    String actualTxt = "has no type";
                    tchecker.addProblem(ErrMsg.COMP_INST_PARAM_EVENT_TYPES, actual.getPosition(), paramIdxTxt,
                            CifTextUtils.getAbsName(compDef), formalTxt, actualTxt);
                    // Non-fatal error.
                }

                if (formalType != null && actualType != null) {
                    if (!CifTypeUtils.checkTypeCompat(formalEvent.getType(), actualEvent.getType(),
                            RangeCompat.EQUAL))
                    {
                        tchecker.addProblem(ErrMsg.COMP_INST_PARAM_EVENT_TYPES, actual.getPosition(), paramIdxTxt,
                                CifTextUtils.getAbsName(compDef),
                                fmt("is of type \"%s\"", CifTextUtils.typeToStr(formalType)),
                                fmt("is of type \"%s\"", CifTextUtils.typeToStr(actualType)));
                        // Non-fatal error.
                    }
                }

                // Get event parameters, or 'null' for actual argument if
                // reference to concrete event rather than event parameter.
                EventParameter formalParam = (EventParameter)formal;
                EventParameter actualParam = null;
                if (actualEvent.eContainer() instanceof EventParameter) {
                    actualParam = (EventParameter)actualEvent.eContainer();
                }

                // Check usage restrictions match for formal/actual. If actual
                // is concrete event, all is allowed. If formal has no flags,
                // all is allowed. Otherwise, check that all formal flags also
                // present for actual.
                if (actualParam != null) {
                    checkEventUsage(formalParam, actualParam, param.position, paramIdxTxt, compDef);
                }
            } else if (formal instanceof LocationParameter) {
                Expression unwrap = CifTypeUtils.unwrapExpression(actual);
                if (!(unwrap instanceof LocationExpression)) {
                    tchecker.addProblem(ErrMsg.COMP_INST_PARAM_TYPE, actual.getPosition(), paramIdxTxt,
                            CifTextUtils.getAbsName(compDef), "location");
                    // Non-fatal error.
                }
            } else if (formal instanceof ComponentParameter) {
                CifType formalType = ((ComponentParameter)formal).getType();
                CifType actualType = actual.getType();

                if (!CifTypeUtils.checkTypeCompat(formalType, actualType, null)) {
                    tchecker.addProblem(ErrMsg.COMP_INST_PARAM_COMP_TYPES, actual.getPosition(), paramIdxTxt,
                            CifTextUtils.getAbsName(compDef), CifTextUtils.typeToStr(formalType),
                            CifTextUtils.typeToStr(actualType));
                    // Non-fatal error.
                }
            } else {
                throw new RuntimeException("Unknown formal param: " + formal);
            }
        }

        // Scope is now fully checked.
        status = CheckStatus.FULL;
    }

    /**
     * Check usage of an actual event parameter being passed to an instantiation for a formal event parameter, based on
     * event usage restriction flags.
     *
     * @param formal The event parameter that is a formal parameter of the component definition being instantiated.
     * @param actual The event parameter used as actual parameter for the instantiation.
     * @param position The position on which to report problems.
     * @param paramIdxTxt The text to use in error messages to identify the actual parameter by index.
     * @param compDef The component definition that is being instantiated.
     */
    private void checkEventUsage(EventParameter formal, EventParameter actual, Position position, String paramIdxTxt,
            ComponentDef compDef)
    {
        // Interpret flags of both event parameters.
        boolean formalSend = CifEventUtils.eventParamSupportsSend(formal);
        boolean formalRecv = CifEventUtils.eventParamSupportsRecv(formal);
        boolean formalSync = CifEventUtils.eventParamSupportsSync(formal);
        boolean actualSend = CifEventUtils.eventParamSupportsSend(actual);
        boolean actualRecv = CifEventUtils.eventParamSupportsRecv(actual);
        boolean actualSync = CifEventUtils.eventParamSupportsSync(actual);

        // All formal parameter usages must be supported by the actual
        // parameter.
        if (formalSend && !actualSend) {
            tchecker.addProblem(ErrMsg.COMP_INST_PARAM_EVENT_FLAG, position, paramIdxTxt,
                    CifTextUtils.getAbsName(compDef), "send (!)");
            // Non-fatal problem.
        }

        if (formalRecv && !actualRecv) {
            tchecker.addProblem(ErrMsg.COMP_INST_PARAM_EVENT_FLAG, position, paramIdxTxt,
                    CifTextUtils.getAbsName(compDef), "receive (?)");
            // Non-fatal problem.
        }

        if (formalSync && !actualSync) {
            tchecker.addProblem(ErrMsg.COMP_INST_PARAM_EVENT_FLAG, position, paramIdxTxt,
                    CifTextUtils.getAbsName(compDef), "synchronization (~)");
            // Non-fatal problem.
        }
    }

    /**
     * Returns a textual representation of the given controllability.
     *
     * @param controllable The controllability value.
     * @return {@code "controllable"}, {@code "uncontrollable"}, or {@code "(unspecified)"}.
     */
    public static String controllableToStr(Boolean controllable) {
        return (controllable == null) ? "(unspecified)" : controllable ? "controllable" : "uncontrollable";
    }

    @Override
    protected SymbolTableEntry resolve1(Position position, String id, String done, CifTypeChecker tchecker,
            SymbolScope<?> origScope)
    {
        // Check scope enough to be able to resolve 'via' this scope.
        tcheckForUse();

        // Paranoia checking.
        if (done.isEmpty()) {
            // We can only reference first things VIA this scope, not in it.
            throw new IllegalArgumentException("done");
        }

        if (origScope != null) {
            // We can only reference first things VIA this scope, not in it.
            throw new IllegalArgumentException("origScope");
        }

        // Resolve via the component definition. Any result will be in scope.
        return compDefScope.resolve1(position, id, done, tchecker, origScope);
    }

    @Override
    public void detectCompDefInstCycles(List<ParentScope<?>> cycle) {
        // Continue detection from the component definition that is
        // instantiated by this component instantiation.
        compDefScope.detectCompDefInstCycles(cycle);
    }
}
