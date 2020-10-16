//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.tooldef.io;

import org.eclipse.escet.setext.runtime.io.BaseReader;
import org.eclipse.escet.tooldef.metamodel.tooldef.Script;
import org.eclipse.escet.tooldef.parser.ToolDefParser;
import org.eclipse.escet.tooldef.typechecker.ToolDefTypeChecker;

/** ToolDef ASCII text file reader. */
public class ToolDefReader extends BaseReader<ToolDefReader, Script, Script, ToolDefParser, ToolDefTypeChecker> {
    @Override
    protected ToolDefParser createParser() {
        return new ToolDefParser();
    }

    @Override
    protected ToolDefTypeChecker createTypeChecker() {
        return new ToolDefTypeChecker();
    }

    @Override
    protected String getLangName() {
        return "ToolDef";
    }
}
