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

package org.eclipse.escet.cif.typechecker.declwrap;

import static org.eclipse.escet.cif.typechecker.CifTypesTypeChecker.transCifType;

import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.parser.ast.declarations.AEventDecl;
import org.eclipse.escet.cif.typechecker.CheckStatus;
import org.eclipse.escet.cif.typechecker.CifTypeChecker;
import org.eclipse.escet.cif.typechecker.ErrMsg;
import org.eclipse.escet.cif.typechecker.scopes.ParentScope;
import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.common.typechecker.SemanticException;

/** Event declaration wrapper. */
public class EventDeclWrap extends DeclWrap<Event> {
    /** The CIF AST representation of the event. */
    private final AEventDecl astDecl;

    /**
     * Constructor for the {@link EventDeclWrap} class.
     *
     * @param tchecker The CIF type checker to use.
     * @param scope The parent scope of this declaration.
     * @param astDecl The CIF AST representation of the event.
     * @param mmDecl The CIF metamodel representation of the event.
     */
    public EventDeclWrap(CifTypeChecker tchecker, ParentScope<?> scope, AEventDecl astDecl, Event mmDecl) {
        super(tchecker, scope, mmDecl);
        this.astDecl = astDecl;
    }

    @Override
    public String getName() {
        return mmDecl.getName();
    }

    @Override
    public String getAbsName() {
        return CifTextUtils.getAbsName(mmDecl);
    }

    @Override
    public void tcheckForUseImpl() {
        // Check for reserved names.
        checkName();

        // Process event type, if specified.
        if (astDecl.type != null) {
            // Get the type of the declaration.
            tchecker.addToCycle(this);

            CifType type;
            try {
                type = transCifType(astDecl.type, scope, tchecker);
            } finally {
                tchecker.removeFromCycle(this);
            }

            // Check for allowed types.
            if (CifTypeUtils.hasComponentLikeType(type)) {
                tchecker.addProblem(ErrMsg.DECL_INVALID_TYPE, type.getPosition(), "Event", getAbsName(),
                        CifTextUtils.typeToStr(type));
                throw new SemanticException();
            }

            // Set the type.
            mmDecl.setType(type);
        }

        // This declaration is now fully checked.
        status = CheckStatus.FULL;
    }

    @Override
    public void tcheckFull() {
        // The 'for use' check already fully checks the event.
        tcheckForUse();
    }

    @Override
    protected void checkName() {
        checkEventName(getName(), mmDecl, getPosition(), tchecker);
    }

    /**
     * Checks the name of an event for validity.
     *
     * @param name The name of the event.
     * @param event The metamodel representation of the event, used to get the absolute name and controllability.
     * @param position The position of the event declaration.
     * @param tchecker The CIF type checker to use.
     */
    public static void checkEventName(String name, Event event, Position position, CifTypeChecker tchecker) {
        Boolean controllable = event.getControllable();
        if (name.startsWith("e_")) {
            if (controllable != null) {
                tchecker.addProblem(ErrMsg.EVENT_NAME_CONTR_MISMATCH, position, CifTextUtils.getAbsName(event), "e_",
                        "events not declared as controllable or uncontrollable");
                // Non-fatal error.
            }
        } else if (name.startsWith("c_")) {
            if (controllable == null || !controllable) {
                tchecker.addProblem(ErrMsg.EVENT_NAME_CONTR_MISMATCH, position, CifTextUtils.getAbsName(event), "c_",
                        "controllable events");
                // Non-fatal error.
            }
        } else if (name.startsWith("u_")) {
            if (controllable == null || controllable) {
                tchecker.addProblem(ErrMsg.EVENT_NAME_CONTR_MISMATCH, position, CifTextUtils.getAbsName(event), "u_",
                        "uncontrollable events");
                // Non-fatal error.
            }
        }
    }
}
