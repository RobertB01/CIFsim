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

package org.eclipse.escet.setext.parser.ast.scanner;

import org.eclipse.escet.common.java.TextPosition;
import org.eclipse.escet.setext.parser.ast.Decl;
import org.eclipse.escet.setext.parser.ast.parser.JavaType;

/** Scanner class declaration. Specifies the class to generate for the scanner. */
public class ScannerDecl extends Decl {
    /** The scanner Java class to generate. */
    public final JavaType scannerClass;

    /**
     * Constructor for the {@link ScannerDecl} class.
     *
     * @param scannerClass The scanner Java class to generate.
     * @param position Position information.
     */
    public ScannerDecl(JavaType scannerClass, TextPosition position) {
        super(position);
        this.scannerClass = scannerClass;
    }
}
