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

package org.eclipse.escet.cif.codegen.options;

import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.cif.common.CifValidationUtils;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.exceptions.InvalidOptionException;
import org.eclipse.escet.common.app.framework.options.InputFileOption;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.StringOption;

/** Code prefix option. */
public class CodePrefixOption extends StringOption {
    /** Constructor for the {@link CodePrefixOption} class. */
    public CodePrefixOption() {
        super(
                // name
                "Code prefix",

                // description
                "The code prefix, used to prefix file names, identifiers in the code, etc. "
                        + "If no prefix is given, the prefix is derived from the input filename, "
                        + "by removing the \".cif\" file extension, if present. This is also the default. "
                        + "The prefix must be a valid CIF identifier.",

                // cmdShort
                'p',

                // cmdLong
                "code-prefix",

                // cmdValue
                "PREFIX",

                // defaultValue
                null,

                // emptyAsNull
                true,

                // showInDialog
                true,

                // optDialogDescr
                "The code prefix, used to prefix file names, identifiers in the code, etc. "
                        + "If no prefix is given, the prefix is derived from the input filename, "
                        + "by removing the \".cif\" file extension, if present. "
                        + "The prefix must be a valid CIF identifier.",

                // optDialogLabelText
                "Prefix:");
    }

    /**
     * Returns the code prefix.
     *
     * @return The code prefix.
     */
    public static String getPrefix() {
        String prefix = Options.get(CodePrefixOption.class);
        if (prefix == null) {
            prefix = InputFileOption.getPath();
            prefix = Paths.getFileName(prefix);
            prefix = Paths.pathChangeExtension(prefix, "cif", null);
            if (!CifValidationUtils.isValidIdentifier(prefix)) {
                String msg = fmt("Code prefix \"%s\" derived from the input filename of the CIF code generator is not "
                        + "a valid CIF identifier. Please provide a custom code prefix using the appropriate option.",
                        prefix);
                throw new InvalidOptionException(msg);
            }
            return prefix;
        }

        if (!CifValidationUtils.isValidIdentifier(prefix)) {
            String msg = fmt("Code prefix \"%s\" specified using the code prefix option of the CIF code generator "
                    + "is not a valid CIF identifier.", prefix);
            throw new InvalidOptionException(msg);
        }
        return prefix;
    }
}
