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

package org.eclipse.escet.setext.generator.parser;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.escet.setext.parser.ast.parser.NonTerminal;
import org.eclipse.escet.setext.parser.ast.parser.ParserRule;

/**
 * Mapping from non-terminals to mappings from their rules (productions) to the grammar items for all the positions of
 * those rules.
 */
public class GrammarItemsMap extends LinkedHashMap<NonTerminal, Map<ParserRule, List<GrammarItem>>> {
    // Nothing here.
}
