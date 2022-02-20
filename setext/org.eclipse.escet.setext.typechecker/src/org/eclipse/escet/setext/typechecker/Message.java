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

package org.eclipse.escet.setext.typechecker;

import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.typechecker.SemanticProblemSeverity.ERROR;
import static org.eclipse.escet.common.typechecker.SemanticProblemSeverity.WARNING;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.typechecker.SemanticProblemSeverity;

/** SeText type checker problem message. */
@SuppressWarnings("javadoc")
public enum Message {
    SYMTABLE_DUPL_DECL("Duplicate declaration of \"%s\".", 1, ERROR),

    UNSUPPORTED_NON_ASCII_CHAR("Unsupported character \"%s\" (unicode code point %s).", 2, ERROR),

    CHAR_CLS_EMPTY("The character class is empty, and thus invalid.", 0, ERROR),

    CHAR_SEQ_INVALID("The character sequence \"%s\" is empty, and thus invalid.", 1, ERROR),

    CHAR_SEQ_SINGLE("The character sequence \"%s\" contains only a single character.", 1, WARNING),

    SHORTCUT_UNDEFINED("Undefined shortcut \"%s\".", 1, ERROR),

    SHORTCUT_UNDEFINED_ORDER("Undefined shortcut \"%s\". Note that a shortcut can only reference "
            + "another shortcut, if the former shortcut is defined after the latter shortcut.", 1, ERROR),

    SHORTCUT_INVALID_REF("Definition \"%s\" does not define a shortcut.", 1, ERROR),

    SHORTCUT_UNUSED("Shortcut \"%s\" is not used.", 1, WARNING),

    STATE_DOES_NOT_EXIST("Scanner state \"%s\" does not exist.", 1, ERROR),

    STATE_UNREACHABLE("Scanner state \"%s\" is unreachable.", 1, WARNING),

    STATE_SAME_AS_SOURCE("Superfluous target scanner state (\"%s\") should be omitted.", 1, WARNING),

    STATE_DUPL_EOF("Duplicate end-of-file acceptance for scanner state \"%s\".", 1, ERROR),

    NO_STATE_EOF("No scanner state accepts end-of-file.", 0, ERROR),

    DEFAULT_STATE_NO_EOF("The default scanner state does not accept end-of-file.", 0, WARNING),

    IMPORT_INVALID_REF("Definition \"%s\" does not define an import.", 1, ERROR),

    IMPORT_UNUSED("Import \"%s\" is not used.", 1, WARNING),

    DUPL_GENERIC_TYPE_PARAMS("Both this Java type and the imported Java type \"%s\" specify generic type parameters.",
            1, ERROR),

    REL_REF_ON_COMPLETE_TYPE("Invalid relative name \"%s\" for non-package import \"%s\".", 2, ERROR),

    SYMBOL_UNDEFINED("Undefined symbol \"%s\".", 1, ERROR),

    SYMBOL_INVALID_REF("Definition \"%s\" does not define a symbol.", 1, ERROR),

    TERM_ACCEPTS_EMPTY_STR("Terminal%s accepts the empty string.", 1, ERROR),

    TERM_UNUSED("Terminal \"%s\" is not used.", 1, WARNING),

    TERM_DESCR_MISSING("Missing end-user readable description for terminal \"%s\".", 1, WARNING),

    TERM_DESCR_UNNECESSARY("Terminal description unnecessary for %s.", 1, WARNING),

    TERM_DESCR_OVERRIDE("Description for terminal \"%s\" overrides default description \"%s\".", 2, WARNING),

    NONTERM_UNUSED("Non-terminal \"%s\" is not used.", 1, WARNING),

    NONTERM_UNREACHABLE("Non-terminal \"%s\" can not be reached from any of the main/start symbols.", 1, WARNING),

    NONTERM_UNDEFINED("Undefined non-terminal \"%s\".", 1, ERROR),

    NONTERM_INVALID_REF("Definition \"%s\" does not define a non-terminal.", 1, ERROR),

    CALLBACK_BY_DEF("Symbol \"%s\" is a non-terminal and is superfluously annotated with an \"@\" annotation.", 1,
            WARNING),

    NO_START_SYMBOL("The specification is missing a start/main symbol.", 0, ERROR),

    START_DUPL("Duplicate main/start non-terminal \"%s\".", 1, WARNING),

    PARSER_CLASS_GENERIC("Generic type \"%s\" is currently not supported for %s symbol \"%s\".", 3, ERROR),

    GEN_CLASS_DUPL("Duplicate generated class \"%s\".", 1, ERROR),

    MAIN_UNREACHABLES("One or more non-terminals are unreachable from main symbol \"%s\": %s.", 2, ERROR),

    HOOKS_DECL_DUPL("Duplicate hooks class.", 0, ERROR),

    HOOKS_DECL_MISSING("The specification is missing a hooks class.", 0, ERROR),

    HOOKS_DECL_GENERIC("Generic types are currently not supported for hooks classes.", 0, ERROR),

    SCANNER_DECL_DUPL("Duplicate scanner class.", 0, ERROR),

    SCANNER_DECL_MISSING("The specification is missing a scanner class.", 0, ERROR),

    SCANNER_DECL_GENERIC("Generic types are currently not supported for scanner classes.", 0, ERROR);

    /** The problem message pattern. */
    private final String message;

    /** The number of arguments required by the problem message pattern. */
    private final int argCount;

    /** The severity of the problem. */
    private final SemanticProblemSeverity severity;

    /**
     * Constructor for the {@link Message} enumeration.
     *
     * @param message The problem message pattern.
     * @param argCount The number of arguments required by the problem message pattern.
     * @param severity The severity of the problem.
     */
    private Message(String message, int argCount, SemanticProblemSeverity severity) {
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
