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

package org.eclipse.escet.cif.texteditor;

import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.parser.CifParser;
import org.eclipse.escet.cif.parser.ast.ASpecification;
import org.eclipse.escet.cif.typechecker.CifTypeChecker;
import org.eclipse.escet.setext.texteditorbase.GenericTextEditor;

/** CIF text editor for Eclipse. */
public class CifTextEditor extends GenericTextEditor<ASpecification, Specification> {
    /** Constructor for the {@link CifTextEditor} class. */
    public CifTextEditor() {
        super(new CifPartitionScanner(), new CifSourceViewerConfig(), CifParser.class, CifTypeChecker.class,
                "org.eclipse.escet.cif.texteditor.CifSyntaxProblem",
                "org.eclipse.escet.cif.texteditor.CifSemanticProblem", "//");
    }
}
