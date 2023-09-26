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

package org.eclipse.escet.cif.codegen.options;

import org.eclipse.escet.common.app.framework.options.EnumOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** Target language option. */
public class TargetLanguageOption extends EnumOption<TargetLanguage> {
    /** Constructor for the {@link TargetLanguageOption} class. */
    public TargetLanguageOption() {
        super(
                // name
                "Target language",

                // description
                "The target language of the code generator. Specify \"java\" for Java code, \"c89\" for C89 code, "
                        + "\"c99\" for C99 code, \"javascript\" for Javascript code, or \"simulink\" for Simulink S-Function code. [DEFAULT=simulink]",

                // cmdShort
                'l',

                // cmdLong
                "target-language",

                // cmdValue
                "LANG",

                // defaultValue
                TargetLanguage.SIMULINK,

                // showInDialog
                true,

                // optDialogDescr
                "The target language of the code generator.");
    }

    @Override
    protected String getDialogText(TargetLanguage lang) {
        return lang.readableName;
    }

    /**
     * Returns the target language.
     *
     * @return The target language.
     */
    public static TargetLanguage getLanguage() {
        return Options.get(TargetLanguageOption.class);
    }
}
