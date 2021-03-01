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
import static org.eclipse.escet.common.java.Lists.first;

import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.metamodel.cif.EventParameter;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.parser.ast.AFormalEventParameter;
import org.eclipse.escet.cif.parser.ast.AFormalEventParameterPart;
import org.eclipse.escet.cif.parser.ast.tokens.AEventParamFlag;
import org.eclipse.escet.cif.typechecker.CheckStatus;
import org.eclipse.escet.cif.typechecker.CifTypeChecker;
import org.eclipse.escet.cif.typechecker.ErrMsg;
import org.eclipse.escet.cif.typechecker.scopes.ParentScope;
import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.common.typechecker.SemanticException;

/** Formal event parameter declaration wrapper. */
public class FormalEventDeclWrap extends DeclWrap<EventParameter> {
    /** The CIF AST representation of the event parameter. */
    private final AFormalEventParameter astDecl;

    /** The CIF AST representation of the event parameter part. */
    private final AFormalEventParameterPart astPartDecl;

    /**
     * Constructor for the {@link FormalEventDeclWrap} class.
     *
     * @param tchecker The CIF type checker to use.
     * @param scope The parent scope of this declaration.
     * @param astDecl The CIF AST representation of the event parameter.
     * @param astPartDecl The CIF AST representation of the event parameter part.
     * @param mmDecl The CIF metamodel representation of the formal event parameter.
     */
    public FormalEventDeclWrap(CifTypeChecker tchecker, ParentScope<?> scope, AFormalEventParameter astDecl,
            AFormalEventParameterPart astPartDecl, EventParameter mmDecl)
    {
        super(tchecker, scope, mmDecl);
        this.astDecl = astDecl;
        this.astPartDecl = astPartDecl;
    }

    @Override
    public String getName() {
        return mmDecl.getEvent().getName();
    }

    @Override
    public String getAbsName() {
        return CifTextUtils.getAbsName(mmDecl.getEvent());
    }

    @Override
    public void tcheckForUseImpl() {
        // Check for reserved names.
        checkName();

        // Process event type, if specified.
        if (astDecl.type != null) {
            // Get the type of the parameter. Resolve it in the parent scope
            // of the component definition, not in the parent scope of the
            // event parameter.
            tchecker.addToCycle(this);

            CifType type;
            try {
                type = transCifType(astDecl.type, scope.getParent(), tchecker);
            } finally {
                tchecker.removeFromCycle(this);
            }

            // Check for allowed types.
            if (CifTypeUtils.hasComponentLikeType(type)) {
                tchecker.addProblem(ErrMsg.DECL_INVALID_TYPE, type.getPosition(), "Event parameter", getAbsName(),
                        CifTextUtils.typeToStr(type));
                throw new SemanticException();
            }

            // Set the type.
            mmDecl.getEvent().setType(type);
        }

        // Process flags, if specified.
        boolean[] flags = new boolean[3];
        Position[] flagsPos = new Position[3];
        for (AEventParamFlag flag: astPartDecl.flags) {
            // Get flag index.
            int flagIdx;
            switch (flag.flag) {
                case "!":
                    flagIdx = 0;
                    break;
                case "?":
                    flagIdx = 1;
                    break;
                case "~":
                    flagIdx = 2;
                    break;
                default:
                    throw new RuntimeException("Unknown flag: " + flag.flag);
            }

            // Check for duplicate.
            if (flags[flagIdx]) {
                tchecker.addProblem(ErrMsg.EVENT_PARAM_DUPL_FLAG, flag.position, flag.flag, getAbsName());
                throw new SemanticException();
            }

            // Check order.
            if (flagIdx == 0 && flags[1]) { // ! after ?
                tchecker.addProblem(ErrMsg.EVENT_PARAM_FLAG_ORDER, flag.position, flag.flag, "?", getAbsName());
                // Non-fatal problem.
            }

            if (flagIdx == 0 && flags[2]) { // ! after ~
                tchecker.addProblem(ErrMsg.EVENT_PARAM_FLAG_ORDER, flag.position, flag.flag, "~", getAbsName());
                // Non-fatal problem.
            }

            if (flagIdx == 1 && flags[2]) { // ? after ~
                tchecker.addProblem(ErrMsg.EVENT_PARAM_FLAG_ORDER, flag.position, flag.flag, "~", getAbsName());
                // Non-fatal problem.
            }

            // Update flag information.
            flags[flagIdx] = true;
            flagsPos[flagIdx] = flag.position;
        }

        mmDecl.setSendFlag(flags[0]);
        mmDecl.setRecvFlag(flags[1]);
        mmDecl.setSyncFlag(flags[2]);

        // Flags only allowed for channels.
        if (astDecl.type == null && !astPartDecl.flags.isEmpty()) {
            tchecker.addProblem(ErrMsg.EVENT_PARAM_FLAG_NON_CHAN, first(astPartDecl.flags).position, getAbsName());
            throw new SemanticException();
        }

        // This declaration is now fully checked.
        status = CheckStatus.FULL;
    }

    @Override
    public void tcheckFull() {
        // The 'for use' check already fully checks the event parameter.
        tcheckForUse();
    }

    @Override
    protected void checkName() {
        EventDeclWrap.checkEventName(getName(), mmDecl.getEvent(), getPosition(), tchecker);
    }
}
