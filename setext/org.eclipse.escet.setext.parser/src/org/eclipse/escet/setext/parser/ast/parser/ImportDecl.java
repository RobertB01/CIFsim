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

package org.eclipse.escet.setext.parser.ast.parser;

import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.setext.parser.ast.Decl;

/** Import declaration, to import a Java type or package. This class is no longer used after type checking. */
public class ImportDecl extends Decl {
    /** The imported Java type or package. */
    public final JavaType javaType;

    /**
     * Local name for the imported java type or package. May be {@code null} to indicate that the local name should be
     * derived from the type or package name. In such a case, the name is set by the type checker.
     */
    public String name;

    /** Is this import used? This information is used internally by the type checker only. */
    public boolean used = false;

    /**
     * Constructor for the {@link ImportDecl} class.
     *
     * @param javaType The imported Java type or package.
     * @param name Local name for the imported java type or package. May be {@code null} to indicate that the local name
     *     should be derived from the type or package name.
     * @param position Position information.
     */
    public ImportDecl(JavaType javaType, String name, Position position) {
        super(position);
        this.javaType = javaType;
        this.name = name;
    }
}
