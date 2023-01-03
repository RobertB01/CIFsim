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

package org.eclipse.escet.tooldef.typechecker;

import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.typechecker.SemanticProblemSeverity.ERROR;
import static org.eclipse.escet.common.typechecker.SemanticProblemSeverity.WARNING;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.typechecker.SemanticProblemSeverity;

/** Problem messages of the ToolDef type checker. */
@SuppressWarnings("javadoc")
public enum Message {
    ASGN_NON_ASSIGNABLE("Cannot assign a new value to %s.", ERROR),
    ASGN_DUPL_VAR("Duplicate assignment to %s in a single multi-assignment.", ERROR),
    ASGN_STRING_PROJ("Cannot assign a part of %s, as partial updates of \"string\" typed variables are not supported.",
            ERROR),
    ASGN_TYPE_VALUE_MISMATCH(
            "The type \"%s\" of the value of the assignment is incompatible with type \"%s\" of the addressable.",
            ERROR),
    COND_NON_BOOL("Condition must be of type \"bool\", but is of type \"%s\".", ERROR),
    DUPL_NAME("Duplicate name \"%s\" for %s and %s in %s.", ERROR),
    EXIT_CODE_NON_INT("Exit code must be of type \"int\", but is of type \"%s\".", ERROR),
    EXPR_UNKNOWN_TYPE("Cannot determine the type of expression \"%s\". Please use a cast expression to explicitly "
            + "specify the type.", ERROR),
    FMT_PAT_DECODE_ERR("%s", ERROR),
    FMT_PAT_IDX_OVERFLOW("Invalid format specifier: the explicit index causes integer overflow.", ERROR),
    FMT_PAT_IDX_OUT_OF_RANGE("Invalid format specifier: the %s value is used, which does not exist.", ERROR),
    FMT_PAT_WRONG_TYPE("Invalid \"%%%s\" format specifier: a value of type \"%s\" is required, "
            + "but the %s value of type \"%s\" is used.", ERROR),
    FMT_PAT_UNUSED_VALUE("The %s value is not used in the format pattern.", WARNING),
    FOR_ADDRS_CNT("Found %s addressables, but expected 1 or %s addressables (for type \"%s\").", ERROR),
    FOR_ADDRS_TOO_MANY("Found %s addressables, but expected only a single one (for type \"%s\").", ERROR),
    FOR_SOURCE_TYPE("Cannot iterate over a value of type \"%s\", as a list, set, or map type is expected.", ERROR),
    FOR_SOURCE_NULL("Cannot iterate over a value of type \"%s\", as a non-nullable type is expected.", ERROR),
    IMPORT_FILE_CYCLE("Import cycle detected: %s.", ERROR),
    IMPORT_FILE_INVALID_NAME("Import name \"%s\", derived from \"%s\" is not a valid ToolDef identifier.", ERROR),
    IMPORT_FILE_IO_ERROR("Imported ToolDef script \"%s\" could not be read.", ERROR),
    IMPORT_FILE_NO_IMPORTABLE_OBJS("No tools or type declarations found in imported ToolDef script.", WARNING),
    IMPORT_FILE_NOT_FOUND("Imported ToolDef script \"%s\" could not be found, is a directory, "
            + "or for some other reason could not be opened for reading.", ERROR),
    IMPORT_FILE_OBJ_NOT_FOUND("Cannot find a declaration with name \"%s\" in the imported ToolDef script.", ERROR),
    IMPORT_FILE_OBJ_UNSUPPORTED("Cannot import %s from the imported ToolDef script, "
            + "as only tools and type declarations can be imported.", ERROR),
    IMPORT_FILE_RESOURCE_NOT_FOUND("Imported ToolDef script \"%s\" could not be found %s.", ERROR),
    IMPORT_FILE_SEMANTIC_ERROR("Imported ToolDef script \"%s\" has an error: %s", ERROR),
    IMPORT_FILE_SYNTAX_ERROR("Imported ToolDef script \"%s\" has a syntax error.", ERROR),
    IMPORT_FILE_SYNTAX_WARNING("Imported ToolDef script \"%s\" has a syntax warning.", WARNING),
    IMPORT_JAVA_CLASS_NOT_FOUND("Cannot load Java class \"%s\" as the class could not be found %s.", ERROR),
    IMPORT_JAVA_METHOD_NOT_FOUND("Cannot find a %smethod named \"%s\" in class \"%s\".", ERROR),
    IMPORT_JAVA_TYPE_ARRAY("Unsupported use of a Java array type in a %s type of a Java method.", ERROR),
    IMPORT_JAVA_TYPE_NON_GENERIC("Unsupported non-generic use of Java type \"%s\" in a %s type of a Java method.",
            ERROR),
    IMPORT_JAVA_TYPE_PARAM_BOUNDED("Java method \"%s\" is not supported, as its type parameter \"%s\" is bounded.",
            ERROR),
    IMPORT_JAVA_TYPE_UNSUPPORTED("Unsupported use of Java type \"%s\" in a %s type of a Java method.", ERROR),
    IMPORT_JAVA_TYPE_WILDCARD("Unsupported use of a Java wildcard type in a %s type of a Java method.", ERROR),
    IMPORT_LIB_DUPL("Multiple libraries with name \"%s\" found, in plug-ins (OSGi bundles) \"%s\" and \"%s\".", ERROR),
    IMPORT_LIB_NOT_FOUND("Cannot find a library with name \"%s\".", ERROR),
    IMPORT_PLUGIN_NOT_PLUGIN(
            "Cannot load %s from plug-in (Eclipse project) \"%s\", as the project is not a plug-in project, "
                    + "or it contains a manifest file that is malformed or missing vital information.",
            ERROR),
    IMPORT_PLUGIN_CLASSIC_FORMAT("Cannot load %s from plug-in (Eclipse project) \"%s\", as the plug-in project "
            + "is in a classic format (does not use the new OSGi bundle layout).", ERROR),
    IMPORT_PLUGIN_NO_JAVA_NATURE("Cannot load %s from plug-in (Eclipse project) \"%s\", as the class path "
            + "of the project could not be computed. The plug-in may not have a Java nature. Cause: %s", ERROR),
    IMPORT_PLUGIN_MALFORMED_URL("Cannot load %s from plug-in (Eclipse project) \"%s\", as an URL from the "
            + "project's class path is malformed. Cause: %s", ERROR),
    IMPORT_PLUGIN_OPEN_URL("Cannot load %s from plug-in (Eclipse project) \"%s\", as URL \"%s\" from the "
            + "project's class path could not be opened. Cause: %s", ERROR),
    IMPORT_PLUGIN_ADAPT("Cannot load %s from plug-in (OSGi bundle) \"%s\", as a class loader could not be "
            + "obtained for the plug-in.", ERROR),
    IMPORT_PLUGIN_NOT_FOUND("Cannot find a plug-in (OSGi bundle or Eclipse project) with name \"%s\".", ERROR),
    IMPORT_PLUGIN_STATE("Cannot load %s from plug-in (OSGi bundle) \"%s\", as the plug-in is in a wrong state (%s), "
            + "as it should be in state RESOLVED, STARTING, or ACTIVE.", ERROR),
    INVALID_CAST("Cannot cast from type \"%s\" to type \"%s\".", ERROR),
    INVALID_REF("Could not resolve \"%s\" as a %s (it resolves to %s, a %s rather than a %s).", ERROR),
    INVOKE_DUPL_NAMED_ARG("Duplicated named argument \"%s\".", ERROR),
    INVOKE_NO_MATCH("Cannot invoke tool \"%s\" (%s overload%s) for the given arguments: %s.", ERROR),
    INVOKE_POS_ARG_AFTER_NAMED_ARG("Positional argument may not follow named argument \"%s\".", ERROR),
    NOT_IN_LOOP("A \"%s\" statement can only be used inside a loop (\"for\" or \"while\" statement).", ERROR),
    NOT_IN_TOOL("A \"return\" statement can only be used inside a tool definition.", ERROR),
    PROJ_CHILD_TYPE("Cannot project a value of type \"%s\".", ERROR),
    PROJ_INDEX_TYPE("Cannot project a value of type \"%s\", with an index value of type \"%s\", "
            + "as an index value of type \"%s\" is expected.", ERROR),
    PROJ_TUPLE_BOUND("Cannot project a tuple of type \"%s\", with index %s, as the index is out of bounds.", ERROR),
    PROJ_TUPLE_NON_LIT("Can only project a tuple with an integer number literal.", ERROR),
    RESOLVE_NOT_FOUND("Could not find a declaration with name \"%s\" in %s%s.", ERROR),
    RESOLVE_VIA_NON_SCRIPT("Could not resolve \"%s\" via %s, as the latter is not a imported script.", ERROR),
    RETURN_NO_TYPES("Cannot return values from tool \"%s\" as the tool has no return types.", ERROR),
    RETURN_NO_VALUES("Tool \"%s\" has return types, but no values to return are given.", ERROR),
    RETURN_VALUE_TYPE("Cannot return values of type \"%s\", as tool \"%s\" expects values of type \"%s\".", ERROR),
    SLICE_CHILD_TYPE("Cannot slice a value of type \"%s\".", ERROR),
    SLICE_IDX_NON_INT("Slice %s index must be of type \"int\" but is of type \"%s\".", ERROR),
    STAT_UNREACHABLE("Unreachable statement.", WARNING),
    TOOL_DUPL_OVERLOAD("Duplicate tool overload for %s and %s.", ERROR),
    TOOL_PARAM_ORDER("Mandatory tool parameter \"%s\" may not follow a variadic or optional tool parameter.", ERROR),
    TOOL_PARAM_VALUE_TYPE("The default value of tool parameter \"%s\" is of type \"%s\", "
            + "which doesn't fit in the type \"%s\" of the parameter.", ERROR),
    TOOL_PARAM_VARIADIC_OPTIONAL("Tool parameter \"%s\" may not be both variadic and optional.", ERROR),
    TOOL_MULTIPLE_VARIADIC("Tool \"%s\" has multiple variadic parameters.", ERROR),
    TOOL_RETURN_MISSING("No execution of tool \"%s\" can reach a \"return\" or \"exit\" statement.", ERROR),
    UNUSED_DECL("%s is not used.", WARNING), VALUE_OVERFLOW("%s value overflow (%s > 2^63 - 1).", ERROR),
    VAR_NO_INITIAL_VALUE("Variable \"%s\" is of type \"%s\", which does not have a default value.", ERROR),
    VAR_VALUE_TYPE("The value of variable \"%s\" is of type \"%s\", which doesn't fit in the type \"%s\" of the "
            + "variable.", ERROR);

    /** Text pattern of the message. */
    private final String text;

    /** Severity of the message. */
    public final SemanticProblemSeverity severity;

    /** Cached expected number of arguments (computed at first use). */
    private int count = -1;

    /**
     * Constructor of the {@link Message} enumeration.
     *
     * @param text Text pattern of the message.
     * @param severity Severity of the message.
     */
    private Message(String text, SemanticProblemSeverity severity) {
        this.text = text;
        this.severity = severity;
    }

    /**
     * Count the number of expected arguments in the text pattern of the message.
     *
     * @return The expected number of arguments.
     */
    private int getArgcount() {
        int argCount = 0;
        int index = 0;
        while (true) {
            index = text.indexOf("%s", index);
            if (index == -1) {
                break;
            }

            argCount++;
            index += 2; // "%s" has length 2.
        }
        return argCount;
    }

    /**
     * Format a problem message.
     *
     * @param args The arguments of the message.
     * @return The formatted message text.
     */
    public String format(String... args) {
        if (count < 0) {
            count = getArgcount();
        }
        Assert.check(args.length == count);

        return fmt(text, (Object[])args);
    }
}
