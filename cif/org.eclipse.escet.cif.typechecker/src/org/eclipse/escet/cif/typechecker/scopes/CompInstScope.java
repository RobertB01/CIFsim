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
import org.eclipse.escet.common.java.TextPosition;
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
            SymbolTableEntry entry = parent.resolve(compDefRef.position, compDefRef.name, tchecker, parent);
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

        // Parameter/argument count.
        int argCountInst = compInstDecl.arguments.size();
        int paramCountDef = compDef.getParameters().size();
        if (argCountInst != paramCountDef) {
            tchecker.addProblem(ErrMsg.COMP_INST_ARG_COUNT, obj.getPosition(), String.valueOf(argCountInst),
                    getAbsName(), String.valueOf(paramCountDef), CifTextUtils.getAbsName(compDef));
            throw new SemanticException();
        }

        // Arguments.
        List<Expression> args = obj.getArguments();
        for (int i = 0; i < argCountInst; i++) {
            // Textual representation of parameter index.
            String paramIdxTxt = Numbers.toOrdinal(i + 1);

            // Get parameter.
            Parameter param = compDef.getParameters().get(i);

            // Get type hint for argument.
            CifType argHint;
            if (param instanceof AlgParameter) {
                argHint = ((AlgParameter)param).getVariable().getType();
            } else if (param instanceof EventParameter) {
                argHint = BOOL_TYPE_HINT;
            } else if (param instanceof LocationParameter) {
                argHint = BOOL_TYPE_HINT;
            } else if (param instanceof ComponentParameter) {
                argHint = ((ComponentParameter)param).getType();
            } else {
                throw new RuntimeException("Unknown param: " + param);
            }

            // Transform argument, and add it. Resolve any references
            // in the parent scope of the instantiation.
            ExprContext context = (param instanceof EventParameter) ? EVT_REF_CTXT : DEFAULT_CTXT;
            AExpression astArg = compInstDecl.arguments.get(i);
            Expression arg = transExpression(astArg, argHint, parent, context, tchecker);
            args.add(arg);

            // Check against parameter.
            if (param instanceof AlgParameter algParam) {
                CifType paramType = algParam.getVariable().getType();
                CifType argType = arg.getType();

                if (!CifTypeUtils.checkTypeCompat(paramType, argType, RangeCompat.CONTAINED)) {
                    tchecker.addProblem(ErrMsg.COMP_INST_ARG_ALG_TYPES, arg.getPosition(), paramIdxTxt,
                            CifTextUtils.escapeIdentifier(algParam.getVariable().getName()),
                            CifTextUtils.getAbsName(compDef), CifTextUtils.typeToStr(paramType),
                            CifTextUtils.getAbsName(obj), CifTextUtils.typeToStr(argType));
                    // Non-fatal error.
                }
            } else if (param instanceof EventParameter evtParam) {
                // Make sure the argument is an event.
                Expression unwrap = CifTypeUtils.unwrapExpression(arg);
                if (!(unwrap instanceof EventExpression)) {
                    String requiredTxt = "an event";
                    tchecker.addProblem(ErrMsg.COMP_INST_ARG_TYPE, arg.getPosition(), paramIdxTxt,
                            CifTextUtils.escapeIdentifier(evtParam.getEvent().getName()),
                            CifTextUtils.getAbsName(compDef), requiredTxt, CifTextUtils.getAbsName(obj), requiredTxt);
                    throw new SemanticException();
                }

                // Get events.
                Event paramEvent = ((EventParameter)param).getEvent();
                Event argEvent = ((EventExpression)unwrap).getEvent();

                // Check controllability match for parameter/argument.
                Boolean paramContr = paramEvent.getControllable();
                Boolean argContr = argEvent.getControllable();
                if (paramContr != null && !paramContr.equals(argContr)) {
                    String paramContrText = paramContr ? "a \"controllable\"" : "an \"uncontrollable\"";
                    tchecker.addProblem(ErrMsg.COMP_INST_ARG_CONTR_MISMATCH, arg.getPosition(), paramIdxTxt,
                            CifTextUtils.escapeIdentifier(paramEvent.getName()), CifTextUtils.getAbsName(compDef),
                            paramContrText, CifTextUtils.getAbsName(obj), controllableToStr(argContr));
                    // Non-fatal error.
                }

                // Check type match for parameter/argument.
                CifType paramType = paramEvent.getType();
                CifType argType = argEvent.getType();

                if ((paramType != null) && (argType == null)) {
                    String paramTxt = fmt("of type \"%s\"", CifTextUtils.typeToStr(paramType));
                    String argTxt = "has no type";
                    tchecker.addProblem(ErrMsg.COMP_INST_ARG_EVENT_TYPES, arg.getPosition(), paramIdxTxt,
                            CifTextUtils.escapeIdentifier(paramEvent.getName()), CifTextUtils.getAbsName(compDef),
                            paramTxt, CifTextUtils.getAbsName(obj), argTxt);
                    // Non-fatal error.
                }

                if (paramType != null && argType != null) {
                    if (!CifTypeUtils.checkTypeCompat(paramEvent.getType(), argEvent.getType(), RangeCompat.EQUAL)) {
                        tchecker.addProblem(ErrMsg.COMP_INST_ARG_EVENT_TYPES, arg.getPosition(), paramIdxTxt,
                                CifTextUtils.escapeIdentifier(paramEvent.getName()), CifTextUtils.getAbsName(compDef),
                                fmt("of type \"%s\"", CifTextUtils.typeToStr(paramType)), CifTextUtils.getAbsName(obj),
                                fmt("is of type \"%s\"", CifTextUtils.typeToStr(argType)));
                        // Non-fatal error.
                    }
                }

                // Get event parameters, or 'null' for arguments if
                // reference to concrete event rather than event parameter.
                EventParameter paramParam = (EventParameter)param;
                EventParameter argParam = null;
                if (argEvent.eContainer() instanceof EventParameter) {
                    argParam = (EventParameter)argEvent.eContainer();
                }

                // Check usage restrictions match for parameter/argument. If argument
                // is concrete event, all is allowed. If parameter has no flags,
                // all is allowed. Otherwise, check that all parameter flags also
                // present for argument.
                if (argParam != null) {
                    checkEventUsage(paramParam, argParam, astArg.position, paramIdxTxt, compDef);
                }
            } else if (param instanceof LocationParameter locParam) {
                Expression unwrap = CifTypeUtils.unwrapExpression(arg);
                if (!(unwrap instanceof LocationExpression)) {
                    String requiredTxt = "a location";
                    tchecker.addProblem(ErrMsg.COMP_INST_ARG_TYPE, arg.getPosition(), paramIdxTxt,
                            CifTextUtils.escapeIdentifier(locParam.getLocation().getName()),
                            CifTextUtils.getAbsName(compDef), requiredTxt, CifTextUtils.getAbsName(obj), requiredTxt);
                    // Non-fatal error.
                }
            } else if (param instanceof ComponentParameter) {
                CifType paramType = ((ComponentParameter)param).getType();
                CifType argType = arg.getType();

                if (!CifTypeUtils.checkTypeCompat(paramType, argType, null)) {
                    tchecker.addProblem(ErrMsg.COMP_INST_ARG_COMP_TYPES, arg.getPosition(), paramIdxTxt,
                            CifTextUtils.escapeIdentifier(((ComponentParameter)param).getName()),
                            CifTextUtils.getAbsName(compDef), CifTextUtils.typeToStr(paramType),
                            CifTextUtils.getAbsName(obj), CifTextUtils.typeToStr(argType));
                    // Non-fatal error.
                }
            } else {
                throw new RuntimeException("Unknown param: " + param);
            }
        }

        // Scope is now fully checked.
        status = CheckStatus.FULL;
    }

    /**
     * Check usage of an event parameter being passed as argument to an instantiation for an event parameter, based on
     * event usage restriction flags.
     *
     * @param param The event parameter that is a parameter of the component definition being instantiated.
     * @param arg The event parameter used as argument for the instantiation.
     * @param position The position on which to report problems.
     * @param paramIdxTxt The text to use in error messages to identify the parameter and argument by index.
     * @param compDef The component definition that is being instantiated.
     */
    private void checkEventUsage(EventParameter param, EventParameter arg, TextPosition position, String paramIdxTxt,
            ComponentDef compDef)
    {
        // Interpret flags of both event parameters.
        boolean paramSend = CifEventUtils.eventParamSupportsSend(param);
        boolean paramRecv = CifEventUtils.eventParamSupportsRecv(param);
        boolean paramSync = CifEventUtils.eventParamSupportsSync(param);
        boolean argSend = CifEventUtils.eventParamSupportsSend(arg);
        boolean argRecv = CifEventUtils.eventParamSupportsRecv(arg);
        boolean argSync = CifEventUtils.eventParamSupportsSync(arg);

        // All parameter usages must be supported by the argument.
        if (paramSend && !argSend) {
            tchecker.addProblem(ErrMsg.COMP_INST_ARG_EVENT_FLAG, position, paramIdxTxt,
                    CifTextUtils.escapeIdentifier(param.getEvent().getName()), CifTextUtils.getAbsName(compDef),
                    "send (!)", CifTextUtils.getAbsName(obj));
            // Non-fatal problem.
        }

        if (paramRecv && !argRecv) {
            tchecker.addProblem(ErrMsg.COMP_INST_ARG_EVENT_FLAG, position, paramIdxTxt,
                    CifTextUtils.escapeIdentifier(param.getEvent().getName()), CifTextUtils.getAbsName(compDef),
                    "receive (?)", CifTextUtils.getAbsName(obj));
            // Non-fatal problem.
        }

        if (paramSync && !argSync) {
            tchecker.addProblem(ErrMsg.COMP_INST_ARG_EVENT_FLAG, position, paramIdxTxt,
                    CifTextUtils.escapeIdentifier(param.getEvent().getName()), CifTextUtils.getAbsName(compDef),
                    "synchronization (~)", CifTextUtils.getAbsName(obj));
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
    protected SymbolTableEntry resolve1(TextPosition position, String id, String done, CifTypeChecker tchecker,
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
