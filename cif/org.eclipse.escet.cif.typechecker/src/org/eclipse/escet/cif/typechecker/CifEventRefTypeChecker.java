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

package org.eclipse.escet.cif.typechecker;

import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.parser.ast.tokens.AName;
import org.eclipse.escet.cif.typechecker.declwrap.EventDeclWrap;
import org.eclipse.escet.cif.typechecker.declwrap.EventParamDeclWrap;
import org.eclipse.escet.cif.typechecker.scopes.SymbolScope;
import org.eclipse.escet.common.typechecker.SemanticException;

/** Type checker for CIF event references. */
public class CifEventRefTypeChecker {
    /** Constructor for the {@link CifEventRefTypeChecker} class. */
    private CifEventRefTypeChecker() {
        // Static class.
    }

    /**
     * Performs type checking on an event reference.
     *
     * @param eventRef The AST representation of the event reference.
     * @param scope The scope to use to resolve the event.
     * @param tchecker The type checker to which to add problems, if any.
     * @return The resolved event, as an expression.
     * @throws SemanticException If the event could not be resolved.
     */
    public static Expression checkEventRef(AName eventRef, SymbolScope<?> scope, CifTypeChecker tchecker) {
        // Resolve to event.
        SymbolTableEntry entry = scope.resolve(eventRef.position, eventRef.name, tchecker, scope);

        if (entry instanceof EventDeclWrap) {
            // OK.
        } else if (entry instanceof EventParamDeclWrap) {
            // OK.
        } else {
            tchecker.addProblem(ErrMsg.RESOLVE_NON_EVENT, eventRef.position, entry.getAbsName());
            throw new SemanticException();
        }

        // Return event reference expression.
        return scope.resolveAsExpr(eventRef.name, eventRef.position, "", tchecker);
    }
}
