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

package org.eclipse.escet.chi.typecheck;

import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.typechecker.SemanticProblemSeverity.ERROR;
import static org.eclipse.escet.common.typechecker.SemanticProblemSeverity.WARNING;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.typechecker.SemanticProblemSeverity;

/** Error messages of the Chi type checker. */
@SuppressWarnings("javadoc")
public enum Message {
    //
    // Typing errors.
    //
    BAD_ARG_COUNT(2, ERROR, "Number of arguments does not match (expected %s, found %s)."),

    ARGUMENT_TYPE_MISMATCH(3, ERROR, "Type mismatch in argument %s (expected type \"%s\", found type \"%s\")."),

    NUMBER_OUT_OF_RANGE(0, ERROR, "Number is out of range (too large or too small)."),

    EVALUATE_ERROR(0, ERROR, "Cannot evaluate expression to integer constant."),

    ROW_EXPR_WRONG_TYPE(1, ERROR, "Row expression must be an integer expression (found \"%s\")."),
    ROW_EXPR_WRONG_VALUE(1, ERROR, "Row expression must be a non-zero positive number (found \"%s\")."),

    COLUMN_EXPR_WRONG_TYPE(1, ERROR, "Column expression must be an integer expression (found \"%s\")."),
    COLUMN_EXPR_WRONG_VALUE(1, ERROR, "Column expression must be a non-zero positive number (found \"%s\")."),

    DUPLICATE_FIELD(1, ERROR, "There is more than one tuple field named \"%s\"."),

    SET_OF_TIMERS(0, ERROR, "A set of timers is not allowed."),
    DICT_OF_TIMERS(0, ERROR, "A dictionary with timer keys is not allowed."),

    VOID_NOT_ALLOWED(0, ERROR, "Void type not allowed here."),

    SLICE_EXPR_MUST_BE_STRING_OR_LIST(1, ERROR,
            "Slice source expression must be a string or a list type (found \"%s\")."),

    START_EXPR_MUST_BE_INT(1, ERROR, "Start expression must have int type (found \"%s\")."),
    STEP_EXPR_MUST_BE_INT(1, ERROR, "Step expression must have int type (found \"%s\")."),
    END_EXPR_MUST_BE_INT(1, ERROR, "End expression must have int type (found \"%s\")."),

    INDEX_EXPR_MUST_BE_INT(1, ERROR, "Index expression must have int type (found \"%s\")."),
    TYPE_HAS_NO_PROJECTION(1, ERROR, "Type \"%s\" cannot be indexed."),

    LHS_OPERAND_MUST_HAVE_BOOL_TYPE(1, ERROR, "Left operand must have a boolean type (found \"%s\")."),
    RHS_OPERAND_MUST_HAVE_BOOL_TYPE(1, ERROR, "Right operand must have a boolean type (found \"%s\")."),

    LHS_OPERAND_MUST_HAVE_INT_TYPE(1, ERROR, "Left operand must have an int type (found \"%s\")."),
    RHS_OPERAND_MUST_HAVE_INT_TYPE(1, ERROR, "Right operand must have an int type (found \"%s\")."),

    LHS_OPERAND_MUST_HAVE_NUMERIC_TYPE(1, ERROR, "Left operand must have a numeric type (found \"%s\")."),
    RHS_OPERAND_MUST_HAVE_NUMERIC_TYPE(1, ERROR, "Right operand must have a numeric type (found \"%s\")."),
    RHS_OPERAND_MUST_HAVE_STRING_TYPE(1, ERROR, "Right operand must have a string type (found \"%s\")."),

    LHS_MUST_NUMERIC_STRING_TYPE(1, ERROR, "Left operand must have a string or a numeric type (found \"%s\")."),

    OPERANDS_EQUAL(2, ERROR, "Operands should have the same type (found \"%s\" and \"%s\")."),
    OPERANDS_EQUAL_OR_NUMERIC(2, ERROR,
            "Operands should have the same type, or be both numeric (found \"%s\" and \"%s\")."),

    DICT_PROJ_TYPE_MATCH(2, ERROR, "Expression does not match with key type (expected \"%s\", found \"%s\")."),

    BOOLEAN_TYPE_EXPECTED(1, ERROR, "Boolean type expected (found \"%s\")."),
    NUMERIC_TYPE_EXPECTED(1, ERROR, "Numeric type expected (either int or real, found \"%s\")."),
    TUPLE_TYPE_WRONG_SIZE(0, ERROR, "Tuple type must have at least two fields."),
    TUPLE_N_EXPECTED(2, ERROR, "Tuple type of length %s expected (found \"%s\")."),
    TUPLE_N_EXPECTED_FOUND_M(2, ERROR, "Tuple type of length %s expected, found length %s."),
    FILE_TYPE_EXPECTED(1, ERROR, "File type expected (found \"%s\")."),
    STRING_INT_TYPE_EXPECTED(1, ERROR, "String or int type expected (found \"%s\")."),
    DISTRIBUTION_TYPE_EXPECTED(1, ERROR, "Distribution type expected (found \"%s\")."),
    WRONG_DIST_TYPE(1, ERROR, "Incorrect distribution element type \"%s\" found (expected bool, int, or real type)."),
    INSTANCE_TYPE_EXPECTED(1, ERROR, "Instance type expected (found \"%s\")."),
    CHANNEL_TYPE_EXPECTED(1, ERROR, "Channel type expected (found \"%s\")."),

    INST_USELESS_WITH_RUN(0, ERROR, "Assigning a process to an instance variable in a 'run' statement is not allowed."),
    VAR_INST_TYPE(1, ERROR, "Variable should have instance type (found \"%s\")."),
    NEED_RUN_START_STAT(0, ERROR, "Instantiating a process must be done with a run or start statement."),

    SET_EXPECTED(1, ERROR, "Set type expected (found type \"%s\")."),
    LIST_SET_DICT_EXPECTED(1, ERROR, "List, set, or dictionary type expected (found \"%s\")."),
    LHS_MATCH_NOT_ELEMENT_TYPE(2, ERROR,
            "Type of left operand does not match with the elements of the right operand (expected \"%s\", found \"%s\")."),

    DISTRIBUTION_ELEMENT_TYPE_WRONG(1, ERROR,
            "Distribution of boolean or numeric type expected (found distribution of \"%s\")."),
    SAMPLING_NOT_ALLOWED_HERE(0, ERROR, "It is not allowed to sample distributions here."),

    STDLIB_FUNC_MUST_BE_CALLED(0, ERROR, "A standard library function cannot be used without calling it."),

    FUNC_CALL_WITHOUT_PARAMETERS(1, ERROR, "Calling a function without arguments, this function needs %s."),

    FUNC_CALL_TOO_FEW_PARAMETERS(3, ERROR, "Calling a function with too few arguments (found %s, need %s %s)."),

    FUNC_CALL_TOO_MANY_PARAMETERS(3, ERROR, "Calling a function with too many arguments (found %s, need %s %s)."),

    FUNC_CALL_INCORRECT_NUMBER_PARAMETERS(3, ERROR,
            "Calling a function with incorrect number of arguments (found %s, need %s or %s)."),

    FUNC_CALL_WRONG_PARAMETER_TYPE(3, ERROR, "Type mismatch at the %s argument (found type \"%s\"%s)."),

    NOT_CALLABLE(0, ERROR, "Expression is not a function that can be called nor a process that can be run or started."),

    USE_OF_TIME_NOT_ALLOWED(0, ERROR, "Using \"time\" is not allowed here."),
    SYNC_CHANNELS_NO_DATA(0, ERROR, "Synchronisation channels do not transport data."),
    CANNOT_SEND_ON_RECEIVE_ONLY_CHANNEL(0, ERROR, "Cannot send on a receive-only channel."),
    CANNOT_RECEIVE_ON_SEND_ONLY_CHANNEL(0, ERROR, "Cannot receive on a send-only channel."),

    SENT_DATA_NOT_MATCH_CHANNEL(2, ERROR,
            "Type of sent data does not match with type of channel data (sent \"%s\", channel transports \"%s\")."),
    DATA_MISSING_WITH_SEND(1, ERROR, "Channel transports type \"%s\", but no data provided to send."),
    VARIABLES_MISSING_WITH_RECEIVE(1, ERROR, "Channel transports type \"%s\", but no variables provided to receive."),
    RECV_DATA_NOT_MATCH_CHANNEL(2, ERROR,
            "Type of received data does not match with type of channel data (received \"%s\", channel transports \"%s\")."),
    RETURNED_DATA_NOT_MATCH_FUNC_RET_TYPE(2, ERROR,
            "Type of returned data does not match with return type of the function (returned \"%s\", return type \"%s\")."),

    EXIT_STATEMENT_NOT_ALLOWED(0, ERROR, "Exit statement not allowed in a function or an experiment."),
    PROC_MODEL_NO_EXIT_TYPE(0, ERROR, "Cannot use the exit statement in a process or model without exit type."),
    EXIT_TYPE_IS_NOT_A_PRINTABLE_VALUE(1, ERROR, "Exit type \"%s\" does not have printable values."),
    RETURNED_DATA_NOT_MATCH_EXIT_TYPE(2, ERROR,
            "Type of returned data does not match with the exit type (returned \"%s\", exit type \"%s\")."),
    CANNOT_START_PROCESS_WITH_EXIT_IN_NO_EXIT(1, ERROR,
            "Cannot start a process with exit type \"%s\" in a process or model without exit type declaration."),
    EXIT_TYPES_DO_NOT_MATCH(2, ERROR,
            "Cannot start a process with exit type \"%s\" in a process or model with exit type \"%s\"."),
    CANNOT_CALL_NONVOID_MODEL(0, ERROR, "Cannot call a model with a missing or void exit type."),

    CANNOT_CAST(2, ERROR, "Cannot cast type \"%s\" to type \"%s\"."),
    NO_INT_REAL_CAST(0, ERROR,
            "Cannot cast from real to an integer, use a \"floor\", \"round\", or \"ceil\" function instead."),

    MATRIX_LITERAL_REAL_ELEMENTS(1, ERROR, "Matrix elements must have real type (found \"%s\")."),
    MATRIX_LITERAL_ROW_LENGTH(2, ERROR, "Every row should have the same length (expected \"%s\", found \"%s\")."),

    SET_LITERAL_ELEMENT_TYPE(2, ERROR,
            "All elements of a set must have the same type (expected \"%s\", found \"%s\")."),
    LIST_LITERAL_ELEMENT_TYPE(2, ERROR,
            "All elements of a list must have the same type (expected \"%s\", found \"%s\")."),
    DICT_LITERAL_KEY_TYPE(2, ERROR,
            "All keys of a dictionary must have the same type (expected \"%s\", found \"%s\")."),
    DICT_LITERAL_VALUE_TYPE(2, ERROR,
            "All values of a dictionary must have the same type (expected \"%s\", found \"%s\")."),

    RHS_TUPLE_PROJ_FIELD_NAME(0, ERROR, "Field selection of a tuple must be a name."),
    RHS_TUPLE_PROJ_UNKNOWN_NAME(2, ERROR, "Tuple \"%s\" has no field with name \"%s\"."),
    LHS_TUPLE_PROJ_NOT_A_TUPLE(1, ERROR, "Left side of tuple projection (.) is not a tuple type (found type \"%s\")."),

    FUNCTION_CALL_NO_NAME(0, ERROR, "Function call does not take a name."),

    NO_TYPE_NAME(1, ERROR, "Name \"%s\" does not refer to a type."),
    NO_VALUE_NAME(1, ERROR, "Name \"%s\" does not refer to a value."),

    BAD_ADDITION(2, ERROR, "Incorrectly typed addition (cannot add types \"%s\" and \"%s\")."),
    BAD_SUBTRACTION(2, ERROR, "Incorrectly typed subtraction (cannot subtract types \"%s\" and \"%s\")."),
    BAD_MULTIPLICATION(2, ERROR, "Incorrectly typed multiplication (cannot multiply types \"%s\" and \"%s\")."),

    UNEXPECTED_TYPE_MINMAX(1, ERROR,
            "Expected an numeric type or string type in \"min\" or \"max\" (found type \"%s\")."),
    STRING_TYPE_EXPECTED(1, ERROR, "Expected an argument with \"string\" type (found type \"%s\")."),

    //
    // Statement errors.
    //
    NOT_ADDRESSABLE(0, ERROR, "Expression cannot be assigned a value."),

    STATEMENT_NOT_IN_FUNCTION(1, ERROR, "The \"%s\" statement is not allowed in a function."),
    STATEMENT_NOT_OUTSIDE_FUNCTION(1, ERROR, "The \"%s\" statement is not allowed outside a function."),
    BREAK_IN_LOOP(0, ERROR, "Break statement may only be used inside a loop statement."),
    CONTINUE_IN_LOOP(0, ERROR, "Continue statement may only be used inside a loop statement."),

    READ_NOT_ALLOWED(0, ERROR, "\"read\" is not allowed here, the expression may not have side-effects."),
    CHANNEL_NOT_ALLOWED(0, ERROR, "\"channel\" is not allowed here, the expression may not have side-effects."),
    REAL_TIMER_CAST_NOT_ALLOWED(0, ERROR,
            "Casting a timer to its value is not allowed here, the expression must be constant in time."),
    INPUT_FUNC_NOT_ALLOWED(1, ERROR, "\"%s\" is not allowed here, the expression must be constant."),

    CANNOT_ASSIGN(2, ERROR, "Cannot assign value with type \"%s\" to variables with type \"%s\"."),

    WRITE_NEEDS_FORMATTING_STRING(0, ERROR, "Write cannot be empty, it must have at least a formatting string."),
    WRITE_STRING_LITERAL_FORMATTING(0, ERROR, "Formatting string must be a string literal."),
    FORMAT_EXPLICIT_IDX_OVERFLOW(1, ERROR, "Invalid format specifier: explicit index \"%s\" is too large."),
    FORMAT_NOT_ENOUGH_SPECIFIERS(0, ERROR, "Formatting string does not have enough format specifiers."),
    FORMAT_N_WRONG_TYPE(3, ERROR, "Argument %s is of type \"%s\" (expected \"%s\")."),
    FORMAT_N_NON_PRINTABLE(2, ERROR, "Argument %s of type \"%s\" cannot be printed."),
    FORMAT_STRING_TOO_MANY_SPECIFIERS(0, ERROR, "Formatting string has too many format specifiers."),
    FORMAT_ERROR(1, ERROR, "%s"),

    //
    // Referencing errors.
    //
    VARIABLE_NOT_ALLOWED(1, ERROR, "Variable \"%s\" may not be used here."),
    PROCESS_REF_NOT_ALLOWED(1, ERROR, "Process definition \"%s\" may not be used here."),
    MODEL_REF_NOT_ALLOWED(1, ERROR, "Model definition \"%s\" may not be used here."),

    //
    // Top-level errors.
    //
    CONST_HAS_INVALID_VALUE(3, ERROR, "Value of constant \"%s\" has type \"%s\" instead of the expected type \"%s\"."),
    INCORRECT_MODEL_PARAM_TYPE(1, ERROR,
            "Model parameter has type \"%s\" that cannot be created with a model instance."),
    DUPLICATE_DECLARATION(1, ERROR, "There is more than one declaration named \"%s\"."),
    ENUM_VALUE_DOUBLE_DEFINED(1, ERROR, "There is more than one enumeration value named \"%s\"."),

    CYCLE_IN_DECLARATION(1, ERROR, "Declaration \"%s\" uses itself in its definition."),

    //
    // Warnings.
    //

    UNUSED_TYPE(1, WARNING, "Unused type \"%s\"."), UNUSED_CONSTANT(1, WARNING, "Unused constant \"%s\"."),
    UNUSED_ENUM_VALUE(1, WARNING, "Unused enum value \"%s\"."), UNUSED_VARIABLE(1, WARNING, "Unused variable \"%s\"."),
    UNUSED_PROCESS(1, WARNING, "Unused process \"%s\"."), UNUSED_FUNCTION(1, WARNING, "Unused function \"%s\".");

    //
    // Code of the enumeration.
    //

    /** Error text template. */
    private final String msg;

    /** Expected number of parameters of the template string. */
    private final int argCount;

    /** Severity of the problem. */
    private final SemanticProblemSeverity sev;

    /**
     * Constructor of the {@link Message} class.
     *
     * @param argCount Expected number of arguments.
     * @param sev Severity of the reported problem.
     * @param msg String template describing the problem.
     */
    private Message(int argCount, SemanticProblemSeverity sev, String msg) {
        this.msg = msg;
        this.argCount = argCount;
        this.sev = sev;
    }

    /**
     * Get the severity of the error.
     *
     * @return The severity of the problem.
     */
    public SemanticProblemSeverity getSeverity() {
        return sev;
    }

    /**
     * Construct the error message from the template.
     *
     * @param args Template arguments.
     * @return The constructed error message.
     */
    public String format(String... args) {
        Assert.check(args.length == argCount);
        return fmt(msg, (Object[])args);
    }
}
