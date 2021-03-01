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

package org.eclipse.escet.cif.io;

import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.parser.CifParser;
import org.eclipse.escet.cif.parser.ast.ASpecification;
import org.eclipse.escet.cif.typechecker.CifTypeChecker;
import org.eclipse.escet.setext.runtime.io.BaseReader;

/** CIF ASCII text file reader. */
public class CifReader extends BaseReader<CifReader, ASpecification, Specification, CifParser, CifTypeChecker> {
    @Override
    protected CifParser createParser() {
        return new CifParser();
    }

    @Override
    protected CifTypeChecker createTypeChecker() {
        return new CifTypeChecker();
    }

    @Override
    protected String getLangName() {
        return "CIF";
    }
}
