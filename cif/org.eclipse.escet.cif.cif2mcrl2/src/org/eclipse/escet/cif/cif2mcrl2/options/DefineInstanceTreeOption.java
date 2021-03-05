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

package org.eclipse.escet.cif.cif2mcrl2.options;

import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.StringOption;

/** Option to define the instance tree of mCRL2 processes. */
public class DefineInstanceTreeOption extends StringOption {
    /**
     * Constructor for the {@link DefineInstanceTreeOption} class. Don't directly create instances of this class. Use
     * the {@link Options#getInstance} method instead.
     */
    public DefineInstanceTreeOption() {
        super("Instance tree", // name.
                "Comma and/or whitespace separated tree of absolute names of automata and discrete variables that "
                        + "should be translated. Use parentheses to express sub-tree nodes.", // description.
                't', // cmdShort.
                "tree", // cmdLong.
                "TREE", // cmdValue.
                "", // defaultValue.
                false, // emptyAsNull.
                true, // showInDialog.
                "Comma and/or whitespace separated tree of processes to convert.", // optDialogDescr.
                "Tree:"); // optDialogLabelText.
    }

    /**
     * Get the instance tree text.
     *
     * @return The instance tree text.
     */
    public static String getTreeText() {
        return Options.get(DefineInstanceTreeOption.class);
    }
}
