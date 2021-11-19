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

package org.eclipse.escet.cif.typechecker;

import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.typechecker.SemanticProblemSeverity.ERROR;
import static org.eclipse.escet.common.typechecker.SemanticProblemSeverity.WARNING;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.typechecker.SemanticProblemSeverity;

/** CIF type checker error/problem message. */
@SuppressWarnings("javadoc")
public enum ErrMsg {
    // Unsupported, for now (can't represent this in metamodel).
    UNSUPPORTED_COMP_PARAM_USE("Use of a component parameter as %s is currently not supported.", 1, ERROR),

    // AST related constraint.
    IMPORT_NOT_FOUND("Imported CIF specification \"%s\" could not be found, is a directory, or for some other "
            + "reason could not be opened for reading.", 1, ERROR),

    // AST related constraint.
    IMPORT_IO_ERROR("Imported CIF specification \"%s\" could not be read.", 1, ERROR),

    // AST related constraint.
    IMPORT_SYNTAX_ERROR("Imported CIF specification \"%s\" has a syntax error.", 1, ERROR),

    // AST related constraint.
    IMPORT_SYNTAX_WARNING("Imported CIF specification \"%s\" has a syntax warning.", 1, WARNING),

    // AST related constraint.
    IMPORT_SELF("Importing a CIF specification into itself has no effect.", 0, WARNING),

    // AST related constraint.
    DUPL_IMPORT("Duplicate import of CIF specification \"%s\" from a single file.", 1, WARNING),

    // AST related constraint.
    DUPL_NAMESPACE("Duplicate namespace definition in a single file.", 0, ERROR),

    // AST related constraint.
    IMPORT_IN_GRP("Invalid import: imports in groups are not allowed.", 0, ERROR),

    // AST related constraint.
    NAMESPACE_IN_GRP("Invalid namespace declaration: namespace declarations in groups are not allowed.", 0, ERROR),

    // AST related constraint.
    CONVOLUTED_REF("Convoluted reference to %s. Use \"%s\" instead.", 2, WARNING),

    // Component.name
    // ComponentParameter.name
    // Declaration.name
    // EnumLiteral.name
    // Location.name
    RESERVED_NAME_PREFIX(
            "Name of \"%s\" is invalid, as names starting with \"e_\", \"c_\", or \"u_\" are reserved for events.", 1,
            WARNING),

    // Event.matchNameControllability
    EVENT_NAME_CONTR_MISMATCH("Name of event \"%s\" starts with \"%s\", which is a prefix reserved for %s.", 3,
            WARNING),

    // ComponentDef.uniqueDecls
    // Group.uniqueDecls
    // EnumDecl.uniqueLiterals
    // Automaton.uniqueDecls
    // ExternalFunction.uniqueParams
    // InternalFunction.uniqueDecls
    DUPLICATE_NAME("Duplicate name \"%s\" in %s.", 2, ERROR),

    // TupleType.uniqueFieldNames
    DUPLICATE_FIELD_NAME("Duplicate tuple type field name \"%s\".", 1, ERROR),

    // Warn user about accidentally creating a dubious specification.
    ALPHABET_DISABLED_EVENT("The alphabet of automaton \"%s\" globally disables event \"%s\", as the event is not "
            + "used on any of the edges of the automaton.", 2, WARNING),

    // Warn user about accidentally creating a dubious specification.
    MONITOR_EVENT_NO_EDGE("Automaton \"%s\" monitors event \"%s\", but the event is not used on any of the edges of "
            + "the automaton.", 2, WARNING),

    // ListExpression.type
    // SetExpression.type
    // DictExpression.type
    EXPR_UNKNOWN_TYPE("Could not determine the type of expression \"%s\". "
            + "Please use a cast expression to explicitly specify the type.", 1, ERROR),

    // IntExpression.type
    INT_VALUE_OVERFLOW("Integer value overflow (%s > 2,147,483,647 = 2^31 - 1).", 1, ERROR),

    // RealExpression.type
    REAL_VALUE_OVERFLOW("Real value overflow (%s).", 1, ERROR),

    // IntType.validRange
    // ListType.validRange
    TYPE_RANGE_BOUND_NON_INT("The %s bound value of %s type must be of type \"int\".", 2, ERROR),
    EMPTY_TYPE_RANGE("Empty %s type range [%s .. %s].", 3, ERROR),

    // ListType.nonNegativeRangeBounds
    TYPE_RANGE_BOUND_NEG("The %s bound value (%s) of %s type must not be negative.", 3, ERROR),

    // CastExpression.type
    CAST_INVALID_TYPES("Invalid cast from type \"%s\" to type \"%s\".", 2, ERROR),

    // AST related constraint.
    TUPLE_TYPE_ONE_FIELD("Tuple types must have at least two fields.", 0, ERROR),

    // Constant.constantValue
    // AST related constraint (integer type bound values, tuple projection
    // indices, ...)
    STATIC_EVAL_ALG_VAR("Cannot statically evaluate algebraic variable \"%s\".", 1, ERROR),
    STATIC_EVAL_INPUT_VAR("Cannot statically evaluate input variable \"%s\".", 1, ERROR),
    STATIC_EVAL_CONT_VAR("Cannot statically evaluate %scontinuous variable \"%s\".", 2, ERROR),
    STATIC_EVAL_LOC("Cannot statically evaluate location \"%s\".", 1, ERROR),
    STATIC_EVAL_DISC_VAR("Cannot statically evaluate %s \"%s\".", 2, ERROR),
    STATIC_EVAL_SAMPLE("Cannot statically evaluate unary operator \"sample\".", 0, ERROR),
    STATIC_EVAL_TIME("Cannot statically evaluate variable \"time\".", 0, ERROR),
    STATIC_EVAL_DIST("Cannot statically evaluate distribution standard library function \"%s\".", 1, ERROR),
    STATIC_EVAL_FCALL_USER_DEF_FUNC("Cannot statically evaluate user-defined function \"%s\".", 1, ERROR),
    STATIC_EVAL_RCV_VALUE("Cannot statically evaluate the value received by a communication.", 0, ERROR),
    STATIC_EVAL_AUT_REF("Cannot statically evaluate automaton \"%s\" to its active location.", 1, ERROR),
    STATIC_EVAL_SELF("Cannot statically evaluate an automaton \"self\" reference.", 0, ERROR),

    // AlgVariable.selfReference
    // Constant.selfReference
    // ContVariable.selfReference
    // TypeDecl.selfReference
    // DiscVariable.selfReference
    // Equation.selfReference
    DEF_USE_CYCLE("Definition/use cycle detected: \"%s\" is recursively defined in terms of itself: %s.", 2, ERROR),

    // ComponentDef.selfReference
    COMP_DEF_INST_CYCLE("Component definition \"%s\" is directly or indirectly instantiated in itself, "
            + "resulting in an infinite specification: %s.", 2, ERROR),

    // EdgeEvent.eventsInScope
    // CompInstWrapType.instantiationInScope
    // CompParamWrapType.parameterInScope
    // ComponentDefType.definitionInScope
    // ComponentType.componentInScope
    // EnumRef.enumInScope
    // TypeRef.typeInScope
    // AlgVariableExpression.variableInScope
    // CompInstWrapExpression.instantiationInScope
    // CompParamWrapExpression.parameterInScope
    // ComponentExpression.componentInScope
    // ConstantExpression.constantInScope
    // ContVariableExpression.variableInScope
    // EnumLiteralExpression.literalInScope
    // EventExpression.eventInScope
    // FunctionExpression.functionInScope
    // InputVariableExpression.variableInScope
    // LocationExpression.locationInScope
    // DiscVariableExpression.variableInScope
    // ComponentInst.compDefInScope
    // ComponentParameter.typeInScope
    // Alphabet.eventsInScope
    // Automaton.monitorsEventsInScope
    // ProjectionExpression.indexInScope
    // FieldExpression.fieldInScope
    // Invariant.eventInScope
    // PrintFor.eventInScope
    // SvgInEventIfEntry.eventInScope
    // SvgInEventSingle.eventInScope
    RESOLVE_NOT_FOUND("Could not find a declaration with name \"%s\" in %s%s.", 3, ERROR),
    RESOLVE_VIA_NON_SCOPE("Could not resolve \"%s\" via \"%s\", as the latter is not a scope.", 2, ERROR),
    RESOLVE_VIA_COMPDEF("Could not resolve \"%s\" via component definition \"%s\", "
            + "as the definition needs to be instantiated first.", 2, ERROR),
    RESOLVE_VIA_FUNC("Could not resolve \"%s\" via function \"%s\", "
            + "as declarations in functions are local to the function itself.", 2, ERROR),
    RESOLVE_NOT_IN_FUNC_SCOPE("\"%s\" may not be used inside function \"%s\".", 2, ERROR),

    // ComponentInst.compDefRef
    RESOLVE_NOT_COMP_DEF("\"%s\" is not a component definition.", 1, ERROR),

    // AST related constraint.
    COMP_INST_DEF_NOT_IN_SCOPE("Component definition \"%s\" is not in scope.", 1, ERROR),

    // AST related constraint.
    COMP_PARAM_NOT_IN_SCOPE("Component parameter \"%s\" is not in scope.", 1, ERROR),

    // AST related constraint.
    DER_OF_NON_CONT_VAR("Invalid derivative: %s\"%s\" is not a continuous variable.", 2, ERROR),

    // AlgVariable.typeValueMatch
    // Equation.varValueMatch
    ALG_VAR_TYPE_VALUE_MISMATCH("The type \"%s\" of the value of algebraic variable \"%s\" "
            + "is incompatible with the type \"%s\" of the algebraic variable.", 3, ERROR),

    // Constant.typeValueMatch
    CONST_TYPE_VALUE_MISMATCH("The type \"%s\" of the value of constant \"%s\" "
            + "is incompatible with the type \"%s\" of the constant.", 3, ERROR),

    // DiscVariable.typeValueMatch
    DISC_VAR_TYPE_VALUE_MISMATCH("The type \"%s\" of %s initial value of %svariable \"%s\" "
            + "is incompatible with the type \"%s\" of the variable.", 5, ERROR),

    // ContVariable.valueType
    CONT_VAR_TYPE_VALUE_MISMATCH("The type \"%s\" of the initial value of continuous variable \"%s\" "
            + "is incompatible with the type \"real\" of the variable.", 2, ERROR),

    // ContVariable.derivativeType
    // Equation.varValueMatch
    CONT_VAR_DER_TYPE("The type \"%s\" of the derivative of continuous variable \"%s\" "
            + "is incompatible with the type \"real\" of the variable.", 2, ERROR),

    // Equation.variableType
    EQN_CONT_NON_DER("Specifying the value of continuous variable \"%s\" using an equation is not allowed.", 1, ERROR),

    // Equation.variableType
    EQN_ALG_DER("Specifying the derivative of algebraic variable \"%s\" (using an equation) is not allowed.", 1, ERROR),

    // Equation.variableInScope
    EQN_VAR_NOT_IN_SCOPE("Invalid equation: \"%s\" is not an algebraic or continuous variable declared in %s.", 2,
            ERROR),

    // ContVariable.uniqueDerivative
    DUPL_DER_FOR_CONT_VAR("Duplicate derivative for \"%s\".", 1, ERROR),

    // AlgVariable.uniqueValue
    DUPL_VALUE_FOR_ALG_VAR("Duplicate value for \"%s\".", 1, ERROR),

    // ContVariable.uniqueDerivative
    CONT_VAR_NO_DER("Missing derivative for continuous variable \"%s\".", 1, ERROR),

    // AlgVariable.uniqueValue
    ALG_VAR_NO_VALUE("Missing value for algebraic variable \"%s\".", 1, ERROR),

    // AST related constraint.
    EVENT_PARAM_DUPL_FLAG("Duplicate \"%s\" flag for event parameter \"%s\".", 2, ERROR),

    // AST related constraint.
    EVENT_PARAM_FLAG_ORDER("Flag \"%s\" should be before flag \"%s\", for event parameter \"%s\".", 3, WARNING),

    // EventParameter.flagChannelOnly
    EVENT_PARAM_FLAG_NON_CHAN("Illegal flag: event parameter \"%s\" is not a channel (has no data type).", 1, ERROR),

    // ComponentParameter.allowedTypes
    COMP_PARAM_INVALID_TYPE("The type of component parameter \"%s\" is not a component definition.", 1, ERROR),

    // AlgVariable.allowedTypes
    // Constant.allowedTypes
    // DiscVariable.allowedTypes
    // Event.allowedTypes
    // FunctionParameter.allowedTypes
    // InputVariable.allowedTypes
    // InternalFunction.allowedVarTypes
    // TypeDecl.allowedTypes
    DECL_INVALID_TYPE("%s \"%s\" is of type \"%s\", which is a component or component definition.", 3, ERROR),

    // ListType.allowedTypes
    // SetType.allowedTypes
    // FuncType.allowedParamTypes
    // FuncType.allowedReturnTypes
    // DictType.allowedKeyTypes
    // DictType.allowedValueTypes
    // Field.allowedTypes
    // DistType.allowedSampleTypes
    // ListExpression.type
    // SetExpression.type
    // TupleExpression.type
    // DictExpression.type
    // IfExpression.type
    // Function.allowedReturnTypes
    // SwitchExpression.valueType
    // SwitchCase.valueType
    TYPE_INVALID_TYPE("Type \"%s\" is not allowed for %s.", 2, ERROR),

    // AST related constraint.
    INVALID_TYPE_REF("\"%s\" is not a type.", 1, ERROR),

    // AST related constraint.
    COMPDEF_REF_IN_EXPR("Cannot use \"%s\" as a value, as it is a component definition.", 1, ERROR),

    // AST related constraint.
    TYPE_REF_IN_EXPR("Cannot use \"%s\" as a value, as it is a type.", 1, ERROR),

    // UnaryExpression.type
    UNOP_INVALID_CHILD_TYPE("Unary operator \"%s\" can not be applied to a value of type \"%s\".", 2, ERROR),

    // Implementation constraint.
    UNOP_NEGATE_OVERFLOW(
            "Unary operator \"-\" is applied to a value of type \"%s\" and this could result in integer overflow.", 1,
            ERROR),

    // FunctionCallExpression.type
    FCALL_NON_FUNC("Can't call a value of type \"%s\", as it is not a function.", 1, ERROR),

    // FunctionCallExpression.type
    FCALL_WRONG_ARG_COUNT("Function %s needs %s, but %s given.", 3, ERROR),

    // FunctionCallExpression.type
    // StdLibFunctionExpression.type
    FCALL_WRONG_ARG_TYPES("Function %s can not be called with arguments of types %s.", 2, ERROR),

    // Implementation constraint.
    FCALL_ABS_OVERFLOW("The \"abs\" function is applied to a value of type \"%s\", "
            + "and this could result in integer overflow.", 1, ERROR),

    // Implementation constraint.
    FCALL_POW_OVERFLOW("The \"pow\" function is applied to values of type \"%s\" and \"%s\", "
            + "and this could result in integer overflow.", 2, ERROR),

    // Implementation constraint.
    FCALL_DELETE_OUT_OF_BOUNDS("The \"del\" function is applied to values of type \"%s\" and \"%s\", "
            + "and this results in an index out of bounds error.", 2, ERROR),

    // StdLibFunctionExpression.formatPatternLiteral
    FCALL_FMT_NOT_PATTERN("The first argument for the \"fmt\" standard library function "
            + "must be a format pattern (string literal).", 0, ERROR),

    // StdLibFunctionExpression.occurrenceDist
    STDLIB_OCCURRENCE("Distribution standard library function \"%s\" may only be used in the initial values of "
            + "discrete variables, declared in automata.", 1, ERROR),

    // Edge.guardTypes
    // ElifExpression.guardTypes
    // ElifFuncStatement.guardTypes
    // ElifUpdate.guardTypes
    // IfExpression.guardTypes
    // IfFuncStatement.guardTypes
    // IfUpdate.guardTypes
    // WhileFuncStatement.guardTypes
    // SvgInEventIfEntry.guardType
    GUARD_NON_BOOL("Guard must be of type \"bool\", but is of type \"%s\".", 1, ERROR),

    // ComplexComponent.initialTypes
    // Location.initialTypes
    INIT_NON_BOOL("Initialization predicate must be of type \"bool\", but is of type \"%s\".", 1, ERROR),

    // Invariant.type
    INV_NON_BOOL("Invariant predicate must be of type \"bool\", but is of type \"%s\".", 1, ERROR),

    // ComplexComponent.markedTypes
    // Location.markedTypes
    MARKED_NON_BOOL("Marker predicate must be of type \"bool\", but is of type \"%s\".", 1, ERROR),

    // Print.txtPreType
    // Print.txtPostType
    PRINT_TXT_COMP_TYPE(
            "The print declaration %s text is of type \"%s\", which is a component or component definition.", 2, ERROR),

    // Print.whenPreType
    // Print.whenPostType
    PRINT_WHEN_NON_BOOL("Print declaration \"when %s\" filter must be of type \"bool\", but is of type \"%s\".", 2,
            ERROR),

    // IfExpression.type
    IFEXPR_INCOMPAT_TYPES(
            "Incompatible result types for if expression: then type \"%s\" is incompatible with %s type \"%s\".", 3,
            ERROR),

    // SwitchExpression.valueEquality
    SWITCH_NO_VALUE_EQ("Values of type \"%s\" do not support value equality, and may not be used as a control value "
            + "of a switch.", 1, ERROR),

    // SwitchCase.keyType
    SWITCH_CASE_KEY_TYPE("The switch \"case\" key is of type \"%s\", which is not compatible with type \"%s\" of "
            + "the control value of the switch.", 2, ERROR),

    // SwitchCase.keyLocRef
    SWITCH_CASE_LOC_ID("The switch \"case\" key must be a name of a location of automaton \"%s\".", 1, ERROR),

    // SwitchCase.keyLocRef
    SWITCH_CASE_LOC_REF("The switch \"case\" key refers to \"%s\", which is not a location of automaton \"%s\".", 2,
            ERROR),

    // SwitchExpression.type
    SWITCH_EXPR_INCOMPAT_TYPES(
            "Incompatible result types for switch expression: type \"%s\" is incompatible with type \"%s\".", 2, ERROR),

    // SwitchExpression.elseMandatory
    SWITCH_MISSING_ELSE("The switch is missing an \"else\".", 0, ERROR),

    // SwitchExpression.overspecified
    SWITCH_AUT_DUPL_LOC("Duplicate switch \"case\" for location \"%s\".", 1, ERROR),

    // SwitchExpression.complete
    SWITCH_AUT_MISSING_LOC("Missing switch \"case\" for location \"%s\".", 1, ERROR),

    // SwitchExpression.superfluousElse
    SWITCH_AUT_SUPERFLUOUS_ELSE("Switch \"else\" is superfluous, as all locations already have a \"case\".", 0,
            WARNING),

    // ListExpression.type
    // SetExpression.type
    // DictExpression.type
    CONTAINER_EXPR_INCOMPAT_TYPES("Incompatible types \"%s\" and \"%s\" for %s.", 3, ERROR),

    // ProjectionExpression.type
    PROJ_CHILD_TYPE("Can't project a value of type \"%s\".", 1, ERROR),

    // ProjectionExpression.type
    PROJ_INDEX_TYPE("Can't project a value of type \"%s\", with an index value of type \"%s\", "
            + "as an index value of type \"%s\"%s is expected.", 4, ERROR),

    // Implementation constraint.
    PROJ_LIST_OUT_OF_BOUNDS("Can't project a list of type \"%s\", with an index of type \"%s\", "
            + "as the index is out of bounds for the list.", 2, ERROR),

    // ProjectionExpression.type
    PROJ_TUPLE_INDEX_BOUNDS("Can't project a tuple of type \"%s\", with index %s, as the index is out of bounds.", 2,
            ERROR),

    // SliceExpression.type
    SLICE_CHILD_TYPE("Can't slice a value of type \"%s\".", 1, ERROR),

    // SliceExpression.type
    SLICE_IDX_NON_INT("Slice %s index must be of type \"int\".", 1, ERROR),

    // BinaryExpression.type
    // BinaryExpression.divideByZero
    BINOP_INVALID_TYPES("Binary operator \"%s\" can not be applied to values of type \"%s\" and \"%s\".", 3, ERROR),

    // Implementation constraint.
    BINOP_OVERFLOW("Binary operator \"%s\" is applied to values of type \"%s\" and \"%s\", "
            + "and this could result in integer overflow.", 3, ERROR),

    // ComponentInst.parameterCount
    COMP_INST_PARAM_COUNT("The number of arguments (%s) of component instantiation \"%s\" does not match "
            + "the number of parameters (%s) of component definition \"%s\".", 4, ERROR),

    // ComponentInst.parameterTypes
    COMP_INST_PARAM_TYPE(
            "Definition/instantiation parameter mismatch for the %s parameter of \"%s\": %s argument required.", 3,
            ERROR),

    // ComponentInst.parameterTypes
    COMP_INST_PARAM_ALG_TYPES("Definition/instantiation parameter mismatch for the %s parameter of \"%s\": "
            + "the algebraic parameter is of type \"%s\", while the argument is of type \"%s\".", 4, ERROR),

    // ComponentInst.parameterTypes
    COMP_INST_PARAM_COMP_TYPES("Definition/instantiation parameter mismatch for the %s parameter of \"%s\": "
            + "the component parameter is of type \"%s\", while the argument is of type \"%s\".", 4, ERROR),

    // ComponentInst.parameterTypes
    COMP_INST_CONTR_MISMATCH("Definition/instantiation parameter mismatch for the %s parameter of \"%s\": "
            + "the event parameter is \"%s\", while the argument is \"%s\".", 4, ERROR),

    // ComponentInst.parameterTypes
    COMP_INST_PARAM_EVENT_TYPES("Definition/instantiation parameter mismatch for the %s parameter of \"%s\": "
            + "the event parameter %s, while the event used as argument %s.", 4, ERROR),

    // ComponentInst.parameterTypes
    COMP_INST_PARAM_EVENT_FLAG("Definition/instantiation parameter mismatch for the %s parameter of \"%s\": "
            + "the event parameter requires an event with %s usage, "
            + "while the event used as argument doesn't allow that usage.", 3, ERROR),

    // Warn user about accidentally creating a dubious specification.
    AUT_NO_INIT_LOC("Automaton \"%s\" has no initial location.", 1, WARNING),

    // Warn user about accidentally creating a dubious specification.
    LOC_UNREACHABLE("Location \"%s\" is not reachable from any of the initial locations of automaton \"%s\".", 2,
            WARNING),

    // AST related constraint.
    AUT_DUPL_ALPHABET("Duplicate alphabet definition in automaton \"%s\".", 1, ERROR),

    // AST related constraint.
    AUT_DUPL_MONITOR("Duplicate monitor definition in automaton \"%s\".", 1, ERROR),

    // AST related constraint.
    LOC_DUPL_URGENT("Duplicate urgent property for %s.", 1, WARNING),

    // Location.nameless
    NAMELESS_LOC_NOT_ALONE(
            "Nameless location in automaton \"%s\" cannot coexist with other locations in the same automaton.", 1,
            ERROR),

    // Alphabet.eventRefsOnly
    // Automaton.monitorsEventRefsOnly
    // EdgeEvent.eventRefsOnly
    // Invariant.eventRef
    // PrintFor.event
    // SvgInEventIfEntry.event
    // SvgInEventSingle.event
    RESOLVE_NON_EVENT("\"%s\" is not an event.", 1, ERROR),

    // Assignment.variablesInScope
    RESOLVE_NON_ASGN_VAR("\"%s\" is not a discrete or continuous variable.", 1, ERROR),

    // AssignmentFuncStatement.variablesInScope
    RESOLVE_NON_FUNC_VAR("\"%s\" is not a variable or parameter of function \"%s\".", 2, ERROR),

    // Alphabet.eventParamSync
    ALPHABET_NON_SYNC_PARAM("Event parameter \"%s\" does not allow synchronization, "
            + "and may thus not be in the alphabet of automaton \"%s\".", 2, ERROR),

    // Alphabet.uniqueEvents
    ALPHABET_DUPL_EVENT("Duplicate event \"%s\" in alphabet of automaton \"%s\".", 2, WARNING),

    // Automaton.uniqueUsagePerEvent
    EVENT_AUT_USAGE_CONFLICT("Event \"%s\" is used in automaton \"%s\" to %s and to %s.", 4, ERROR),

    // Automaton.monitorsUniqueEvents
    MONITORS_DUPL_EVENT("Duplicate monitor event \"%s\" in automaton \"%s\".", 2, WARNING),

    // Automaton.monitorsSubsetAlphabet
    MONITOR_EVENT_NOT_IN_ALPHABET("Monitor event \"%s\" is not in the alphabet of automaton \"%s\".", 2, ERROR),

    // Warn user about accidentally creating a dubious specification.
    MONITOR_EMPTY_ALPHABET(
            "Monitoring all events in the alphabet of automaton \"%s\" has no effect, as its alphabet is empty.", 1,
            WARNING),

    // Edge.uniqueEvents
    EDGE_DUPL_EVENT("Duplicate event \"%s\" on a single edge.", 1, WARNING),

    // AST related constraint.
    EDGE_NON_LOC_TARGET("\"%s\" is not a location.", 1, ERROR),

    // Edge.targetInScope
    EDGE_TGT_FORMAL_PARAM(
            "Target location \"%s\" is a location parameter, while a location of automaton \"%s\" is expected.", 2,
            ERROR),

    // EdgeEvent.eventParamUse
    EDGE_EVT_PARAM_ILLEGAL_USE("Event parameter \"%s\" does not allow %s usage.", 2, ERROR),

    // EdgeSend.commEvent
    // EdgeReceive.commEvent
    CHANNEL_COMM_NON_CHAN("Can't %s over event \"%s\", as the event is not a channel (it has no data type).", 2, ERROR),

    // EdgeSend.allowedValue
    CHANNEL_VOID_WITH_VALUE("Can't send a value over channel \"%s\" of type \"void\".", 1, ERROR),

    // EdgeEvent.valueType
    CHANNEL_SEND_TYPE_MISMATCH("Can't send a value of type \"%s\" over channel \"%s\" of type \"%s\".", 3, ERROR),

    // EdgeSend.allowedValue
    CHANNEL_NON_VOID_NEED_VALUE("A value to send over the channel is required for channel \"%s\" of type \"%s\".", 2,
            ERROR),

    // Edge.oneReceiveAllReceive
    EDGE_RCV_EXPECTED("%s may not be combined with receiving over a channel, on the same edge.", 1, ERROR),

    // Edge.oneReceiveAllReceive
    CHANNEL_RCVS_TYPE_MISMATCH(
            "Incompatible types \"%s\" and \"%s\" for received values of channel communications on a single edge.", 2,
            ERROR),

    // Automaton.validAlphabet
    EVENT_NOT_IN_ALPHABET("Event \"%s\" is missing in the alphabet of automaton \"%s\".", 2, ERROR),

    // Assignment.variablesInScope
    ASGN_NON_LOCAL_VAR("Cannot assign variable \"%s\", as it is not declared in automaton \"%s\".", 2, ERROR),

    // Assignment.addressableSyntax
    // AssignmentFuncStatement.addressableSyntax
    ASGN_STRING_PROJ("Cannot assign a part of variable \"%s\", as partial updates of \"string\" typed variables "
            + "are not allowed.", 1, ERROR),

    // Assignment.types
    // AssignmentFuncStatement.types
    ASGN_TYPE_VALUE_MISMATCH(
            "The type \"%s\" of the value of the assignment is incompatible with type \"%s\" of the addressable.", 2,
            ERROR),

    // Edge.uniqueVariables
    DUPL_VAR_ASGN_EDGE("Variable \"%s\", or a part of it, may have been assigned twice on a single edge, "
            + "for assignments to \"%s\" and \"%s\".", 3, ERROR),

    // AssignmentFuncStatement.uniqueVariables
    DUPL_VAR_ASGN_FUNC("Variable \"%s\", or a part of it, may have been assigned twice in a single assignment, "
            + "for assignments to \"%s\" and \"%s\"", 3, ERROR),

    // Edge.urgWhenLocUrg
    EDGE_URG_LOC_URG("Edge is redundantly urgent, since its source location (%s) is also urgent.", 1, WARNING),

    // Run-time related constraint.
    EVAL_FAILURE("Evaluation failure: %s", 1, ERROR),

    // Warn user about accidentally creating a dubious specification.
    UNUSED_DECL("%s \"%s\" is not used anywhere in the specification.", 2, WARNING),

    // InternalFunction.unreachable
    STAT_UNREACHABLE("Statement is unreachable.", 0, WARNING),

    // BreakFuncStatement.occurrence
    // ContinueFuncStatement.occurrence
    STAT_NOT_IN_WHILE("The \"%s\" statement may only be used inside \"while\" statements.", 1, ERROR),

    // ReturnFuncStatement.types
    STAT_RETURN_TYPE("The return value of type \"%s\" is incompatible with the return type \"%s\" of function \"%s\".",
            3, ERROR),

    // InternalFunction.endWithReturn
    FUNC_NOT_END_RETURN("Function \"%s\" does not end with a return statement.", 1, ERROR),

    // Function.sideEffectFree
    TIME_IN_FUNC("Invalid use of time dependent variable \"time\" in a function.", 0, ERROR),

    // ReceivedExpression.scope
    RCVD_VALUE_OCCURRENCE(
            "The received value of a communication is only available in updates of edges that receive a value.", 0,
            ERROR),

    // ReceivedExpression.type
    RCVD_VALUE_VOID("The received value of a communication is not available for channels of type \"void\".", 0, ERROR),

    // SelfExpression.scope
    SELF_OCCURRENCE("Using \"self\" to refer to the automaton itself is not allowed in %s, "
            + "as it's not an automaton or automaton definition.", 1, ERROR),

    // EventExpression.occurrence
    EVENT_OCCURRENCE("Invalid use of event \"%s\" as a variable or a value.", 1, ERROR),

    // Implementation constraint.
    UNSUPPORTED_EXT_FUNC_LANG("Language \"%s\" is not supported for external user-defined functions.", 1, ERROR),

    // Implementation constraint.
    CLS_PATH_NOT_FOUND("Class path entry \"%s\" could not be found.", 1, ERROR),

    // Implementation constraint.
    EXT_FUNC_PARAM_RET_TYPE("The %s %s of function \"%s\" has type \"%s\", which is not supported for external "
            + "user-defined %s functions.", 5, ERROR),

    // ComplexComponent.maxOneSvgFile
    SVG_DUPL_FILE("Duplicate SVG image file declared in %s.", 1, ERROR),

    // SvgCopy.svgFileDefined
    // SvgMove.svgFileDefined
    // SvgIn.svgFileDefined
    // SvgOut.svgFileDefined
    SVG_DECL_NO_FILE("Missing SVG file declaration for a CIF/SVG declaration in %s.", 1, ERROR),

    // SvgFile.validSvgFile
    SVG_FILE_NOT_FOUND("SVG image file \"%s\" could not be found, is a directory, "
            + "or for some other reason could not be opened for reading.", 1, ERROR),

    // SvgFile.validSvgFile
    SVG_FILE_IO_ERROR("SVG image file \"%s\" could not be read, is empty, is incomplete, "
            + "is not an SVG file, or is an invalid SVG file.", 1, ERROR),

    // SvgFile.validSvgFile
    SVG_FILE_INVALID_FILE("SVG image file \"%s\" is not an SVG file, is an invalid SVG file, "
            + "or contains unsupported SVG features.", 1, ERROR),

    // SvgCopy.idType
    // SvgMove.idType
    // SvgIn.idType
    // SvgOut.idType
    SVG_ID_NON_STR("SVG element id must be of type \"string\", but is of type \"%s\".", 1, ERROR),

    // SvgCopy.idStaticEval
    // SvgCopy.preStaticEval
    // SvgCopy.postStaticEval
    // SvgMove.idStaticEval
    // SvgMove.xStaticEval
    // SvgMove.yStaticEval
    // SvgIn.idStaticEval
    // SvgOut.idStaticEval
    SVG_NON_STATIC("%s cannot be evaluated statically.", 1, ERROR),

    // SvgCopy.idValidName
    // SvgCopy.preValidName
    // SvgCopy.postValidName
    // SvgMove.idValidName
    // SvgIn.idValidName
    // SvgOut.idValidName
    // SvgOut.attrValidName
    SVG_NAME_INVALID("%s \"%s\" is not a valid SVG name%s.", 3, ERROR),

    // SvgOut.elemNameInSvg11
    SVG_UNKNOWN_NAME_ELEM("The SVG element with id \"%s\" in SVG file \"%s\" has name \"%s\", "
            + "which is not part of the SVG 1.1 standard.", 3, WARNING),

    // SvgOut.attrNameInSvg11
    SVG_UNKNOWN_NAME_ATTR("The SVG element with id \"%s\" in SVG file \"%s\" has name \"%s\", "
            + "which according to the SVG 1.1 standard does not have a \"%s\" attribute.", 4, WARNING),

    // SvgOut.attrId
    // SvgOut.attrStyle
    SVG_ATTR_UNSUPPORTED("Changing the \"%s\" attribute of an SVG element is not supported.", 1, ERROR),

    // SvgCopy.idExists
    // SvgMove.idExists
    // SvgIn.idExists
    // SvgOut.idExists
    SVG_ELEM_ID_NOT_FOUND("Could not find an SVG element with id \"%s\" in SVG file \"%s\".", 2, ERROR),

    // SvgOut.textNode
    SVG_ELEM_NO_TEXT("The SVG element with id \"%s\" in SVG file \"%s\" is not an element for which text can be set.",
            2, ERROR),

    // SvgMove.unique
    SVG_DUPL_MOVE_ID("Duplicate move declaration for SVG element with id \"%s\" in SVG file \"%s\".", 2, ERROR),

    // SvgMove.noTransform
    SVG_DUPL_MOVE_TRANSFORM("The SVG element with id \"%s\" in SVG file \"%s\" is moved, and must therefore not also "
            + "have an output mapping for its \"transform\" attribute.", 2, ERROR),

    // SvgOut.unique
    SVG_DUPL_OUTPUT_ID_ATTR(
            "Duplicate output mapping for attribute \"%s\" of the SVG element with id \"%s\" in SVG file \"%s\".", 3,
            ERROR),

    // SvgOut.unique
    SVG_DUPL_OUTPUT_TEXT("Duplicate text output for SVG elements with ids \"%s\" and \"%s\" in SVG file \"%s\".", 3,
            ERROR),

    // SvgIn.unique
    SVG_DUPL_INPUT_ID("Duplicate input mapping for SVG element with id \"%s\" in SVG file \"%s\".", 2, ERROR),

    // SvgOut.valueType
    SVG_OUT_VALUE_COMP_TYPE(
            "The SVG output mapping value is of type \"%s\", which is a component or component definition.", 1, ERROR),

    // StdLibFunctionExpression.formatPattern
    FMT_PAT_DECODE_ERR("%s", 1, ERROR),

    // StdLibFunctionExpression.formatPattern
    FMT_PAT_IDX_OVERFLOW("Invalid format specifier: the explicit index causes integer overflow.", 0, ERROR),

    // StdLibFunctionExpression.formatPattern
    FMT_PAT_IDX_OUT_OF_RANGE("Invalid format specifier: the %s value is used, which does not exist.", 1, ERROR),

    // StdLibFunctionExpression.formatPattern
    FMT_PAT_WRONG_TYPE("Invalid \"%%%s\" format specifier: a value of type \"%s\" is required, "
            + "but the %s value of type \"%s\" is used.", 4, ERROR),

    // StdLibFunctionExpression.formatPattern
    FMT_PAT_COMP_TYPE("Invalid \"%%%s\" format specifier: the %s value of type \"%s\" is used, "
            + "which is a component or component definition.", 3, ERROR),

    // StdLibFunctionExpression.formatUsed
    FMT_PAT_UNUSED_VALUE("The %s value is not used in the format pattern.", 1, WARNING),

    // SvgCopy.prePost
    SVG_COPY_NO_PRE_POST("The SVG copy declaration must specify a prefix, a postfix, or both.", 0, ERROR),

    // SvgCopy.preType
    // SvgCopy.postType
    SVG_COPY_NON_STR("The SVG copy declaration %s must have a \"string\" type, but is of type \"%s\".", 2, ERROR),

    // SvgCopy.nonRoot
    SVG_COPY_ROOT("Copying the SVG element with id \"%s\" in SVG file \"%s\" is not supported, "
            + "as the element is the root element of the SVG file.", 2, ERROR),

    // SvgCopy.overlap
    SVG_COPY_OVERLAP("Copying the SVG element with id \"%s\" in SVG file \"%s\" overlaps with copying the SVG "
            + "element with id \"%s\".", 3, WARNING),

    // SvgCopy.unique
    SVG_COPY_DUPL_ID("Copying the SVG element with id \"%s\" in SVG file \"%s\" resulted in a copy of the SVG "
            + "element with id \"%s\" to id \"%s\", which already exists.", 4, ERROR),

    // SvgMove.xType
    // SvgMove.yType
    SVG_MOVE_NON_NUM("The SVG move declaration %s coordinate must have a numeric type, but is of type \"%s\".", 2,
            ERROR),

    // ComplexComponent.maxOnePrintFile
    PRINT_DUPL_FILE("Duplicate print file declared in %s.", 1, ERROR),

    // Print.duplFor
    PRINT_DUPL_FOR("Duplicate print declaration \"for\" filter: %s.", 1, WARNING);

    // The following metamodel constraints are satisfied by construction:
    // - AlgVariableExpression.type
    // - AlgParameter.noValue
    // - Automaton.noFuncDecl
    // - BoolExpression.type
    // - CompInstWrapExpression.noCompDefBody
    // - CompInstWrapExpression.reference
    // - CompInstWrapExpression.type
    // - CompInstWrapType.noCompDefBody
    // - CompInstWrapType.reference
    // - ComponentExpression.noCompDefBody
    // - ComponentExpression.noSpec
    // - ComponentExpression.type
    // - ComponentType.noCompDefBody
    // - ComponentType.noSpec
    // - CompParamWrapExpression.reference
    // - CompParamWrapExpression.type
    // - CompParamWrapType.reference
    // - ConstantExpression.type
    // - ContVariableExpression.type
    // - DiscVariableExpression.type
    // - DiscVariable.occurrence
    // - Edge.targetInScope
    // - EnumLiteralExpression.type
    // - EventExpression.type
    // - FieldExpression.occurrence
    // - FieldExpression.type
    // - FunctionExpression.type
    // - FunctionParameter.noValue
    // - InputVariableExpression.type
    // - InternalFunction.deterministicVarInit
    // - IntType.neitherOrBoth
    // - Invariant.eventOccurrence
    // - ListType.neitherOrBoth
    // - LocationExpression.type
    // - LocationParameter.nameOnly
    // - Print.txtPrePost
    // - PrintFor.eventSpecified
    // - SelfExpression.type
    // - Specification.name
    // - Specification.root
    // - StdLibFunctionExpression.occurrence
    // - StringExpression.type
    // - SvgInEventIf.else
    // - SwitchExpression.elseOccurrence
    // - TauExpression.occurrence
    // - TauExpression.type
    // - TimeExpression.type
    // - VoidType.occurrence

    /** The problem message pattern. */
    private final String message;

    /** The number of arguments required by the problem message pattern. */
    private final int argCount;

    /** The severity of the problem. */
    private final SemanticProblemSeverity severity;

    /**
     * Constructor for the {@link ErrMsg} enumeration.
     *
     * @param message The problem message pattern.
     * @param argCount The number of arguments required by the problem message pattern.
     * @param severity The severity of the problem.
     */
    private ErrMsg(String message, int argCount, SemanticProblemSeverity severity) {
        this.message = message;
        this.argCount = argCount;
        this.severity = severity;
    }

    /**
     * Returns the severity of the problem.
     *
     * @return The severity of the problem.
     */
    public SemanticProblemSeverity getSeverity() {
        return severity;
    }

    /**
     * Formats the problem message for the given arguments.
     *
     * @param args The arguments to use when formatting the problem message.
     * @return The formatted problem message.
     */
    public String format(String... args) {
        Assert.check(args.length == argCount);
        return fmt(message, (Object[])args);
    }
}
