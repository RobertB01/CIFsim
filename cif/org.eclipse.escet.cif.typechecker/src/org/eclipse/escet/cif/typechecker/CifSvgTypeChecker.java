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

package org.eclipse.escet.cif.typechecker;

import static org.eclipse.escet.cif.common.CifTextUtils.typeToStr;
import static org.eclipse.escet.cif.common.CifTypeUtils.hasComponentLikeType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newSvgCopy;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newSvgFile;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newSvgIn;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newSvgInEventIf;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newSvgInEventIfEntry;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newSvgInEventSingle;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newSvgMove;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newSvgOut;
import static org.eclipse.escet.cif.typechecker.CifExprsTypeChecker.BOOL_TYPE_HINT;
import static org.eclipse.escet.cif.typechecker.CifExprsTypeChecker.NO_TYPE_HINT;
import static org.eclipse.escet.cif.typechecker.CifExprsTypeChecker.STRING_TYPE_HINT;
import static org.eclipse.escet.cif.typechecker.CifExprsTypeChecker.transExpression;

import java.util.List;

import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgCopy;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgFile;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgIn;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEvent;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEventIf;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEventIfEntry;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEventSingle;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgMove;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgOut;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.cif.metamodel.cif.types.RealType;
import org.eclipse.escet.cif.metamodel.cif.types.StringType;
import org.eclipse.escet.cif.parser.ast.iodecls.svg.ASvgCopy;
import org.eclipse.escet.cif.parser.ast.iodecls.svg.ASvgFile;
import org.eclipse.escet.cif.parser.ast.iodecls.svg.ASvgIn;
import org.eclipse.escet.cif.parser.ast.iodecls.svg.ASvgInEventIf;
import org.eclipse.escet.cif.parser.ast.iodecls.svg.ASvgInEventIfEntry;
import org.eclipse.escet.cif.parser.ast.iodecls.svg.ASvgInEventSingle;
import org.eclipse.escet.cif.parser.ast.iodecls.svg.ASvgMove;
import org.eclipse.escet.cif.parser.ast.iodecls.svg.ASvgOut;
import org.eclipse.escet.cif.typechecker.scopes.ParentScope;
import org.eclipse.escet.cif.typechecker.scopes.SymbolScope;
import org.eclipse.escet.common.typechecker.SemanticException;

/** CIF/SVG type checker, during 'normal' type checking phase. */
public class CifSvgTypeChecker {
    /** The CIF type checker to use. */
    private final CifTypeChecker tchecker;

    /**
     * Constructor for the {@link CifSvgTypeChecker} class.
     *
     * @param tchecker The CIF type checker to use.
     */
    public CifSvgTypeChecker(CifTypeChecker tchecker) {
        this.tchecker = tchecker;
    }

    /**
     * Performs type checking on the CIF/SVG file declaration.
     *
     * @param astSvgFile The AST representation of the CIF/SVG file declaration.
     * @param scope The scope in which the I/O declaration is declared.
     * @return The metamodel representation of the I/O declaration.
     */
    public SvgFile checkSvgFile(ASvgFile astSvgFile, ParentScope<?> scope) {
        SvgFile svgFile = newSvgFile();
        svgFile.setPath(astSvgFile.svgPath.txt);
        svgFile.setPosition(astSvgFile.svgPath.position);
        return svgFile;
    }

    /**
     * Performs type checking on the CIF/SVG copy declaration.
     *
     * @param astSvgCopy The AST representation of the CIF/SVG copy declaration.
     * @param scope The scope in which the I/O declaration is declared.
     * @return The metamodel representation of the I/O declaration.
     */
    public SvgCopy checkSvgCopy(ASvgCopy astSvgCopy, ParentScope<?> scope) {
        // Create metamodel representation.
        SvgCopy svgCopy = newSvgCopy();
        svgCopy.setPosition(astSvgCopy.position);

        // Check 'id' expression.
        Expression id = transExpression(astSvgCopy.svgId, STRING_TYPE_HINT, scope, null, tchecker);
        svgCopy.setId(id);

        CifType idtype = id.getType();
        CifType nidtype = CifTypeUtils.normalizeType(idtype);
        if (!(nidtype instanceof StringType)) {
            tchecker.addProblem(ErrMsg.SVG_ID_NON_STR, id.getPosition(), CifTextUtils.typeToStr(idtype));
            throw new SemanticException();
        }

        // We need at least a prefix or a postfix.
        if (astSvgCopy.pre == null && astSvgCopy.post == null) {
            tchecker.addProblem(ErrMsg.SVG_COPY_NO_PRE_POST, svgCopy.getPosition());
            throw new SemanticException();
        }

        // Check prefix.
        if (astSvgCopy.pre != null) {
            Expression pre = transExpression(astSvgCopy.pre, STRING_TYPE_HINT, scope, null, tchecker);
            svgCopy.setPre(pre);

            CifType type = pre.getType();
            CifType ntype = CifTypeUtils.normalizeType(type);
            if (!(ntype instanceof StringType)) {
                tchecker.addProblem(ErrMsg.SVG_COPY_NON_STR, pre.getPosition(), "prefix", CifTextUtils.typeToStr(type));
                throw new SemanticException();
            }
        }

        // Check postfix.
        if (astSvgCopy.post != null) {
            Expression post = transExpression(astSvgCopy.post, STRING_TYPE_HINT, scope, null, tchecker);
            svgCopy.setPost(post);

            CifType type = post.getType();
            CifType ntype = CifTypeUtils.normalizeType(type);
            if (!(ntype instanceof StringType)) {
                tchecker.addProblem(ErrMsg.SVG_COPY_NON_STR, post.getPosition(), "postfix",
                        CifTextUtils.typeToStr(type));
                throw new SemanticException();
            }
        }

        // Check SVG file, if any.
        if (astSvgCopy.svgFile != null) {
            svgCopy.setSvgFile(checkSvgFile(astSvgCopy.svgFile, scope));
        }

        // Return metamodel representation of the copy declaration.
        return svgCopy;
    }

    /**
     * Performs type checking on the CIF/SVG move declaration.
     *
     * @param astSvgMove The AST representation of the CIF/SVG move declaration.
     * @param scope The scope in which the I/O declaration is declared.
     * @return The metamodel representation of the I/O declaration.
     */
    public SvgMove checkSvgMove(ASvgMove astSvgMove, ParentScope<?> scope) {
        // Create metamodel representation.
        SvgMove svgMove = newSvgMove();
        svgMove.setPosition(astSvgMove.position);

        // Check 'id' expression.
        Expression id = transExpression(astSvgMove.svgId, STRING_TYPE_HINT, scope, null, tchecker);
        svgMove.setId(id);

        CifType idtype = id.getType();
        CifType nidtype = CifTypeUtils.normalizeType(idtype);
        if (!(nidtype instanceof StringType)) {
            tchecker.addProblem(ErrMsg.SVG_ID_NON_STR, id.getPosition(), CifTextUtils.typeToStr(idtype));
            throw new SemanticException();
        }

        // Check x.
        Expression x = transExpression(astSvgMove.x, NO_TYPE_HINT, scope, null, tchecker);
        svgMove.setX(x);

        CifType xtype = x.getType();
        CifType nxtype = CifTypeUtils.normalizeType(xtype);
        if (!(nxtype instanceof IntType) && !(nxtype instanceof RealType)) {
            tchecker.addProblem(ErrMsg.SVG_MOVE_NON_NUM, x.getPosition(), "x", CifTextUtils.typeToStr(xtype));
            throw new SemanticException();
        }

        // Check y.
        Expression y = transExpression(astSvgMove.y, NO_TYPE_HINT, scope, null, tchecker);
        svgMove.setY(y);

        CifType ytype = y.getType();
        CifType nytype = CifTypeUtils.normalizeType(ytype);
        if (!(nytype instanceof IntType) && !(nytype instanceof RealType)) {
            tchecker.addProblem(ErrMsg.SVG_MOVE_NON_NUM, y.getPosition(), "y", CifTextUtils.typeToStr(ytype));
            throw new SemanticException();
        }

        // Check SVG file, if any.
        if (astSvgMove.svgFile != null) {
            svgMove.setSvgFile(checkSvgFile(astSvgMove.svgFile, scope));
        }

        // Return metamodel representation of the move declaration.
        return svgMove;
    }

    /**
     * Performs type checking on the CIF/SVG output mapping.
     *
     * @param astSvgOut The AST representation of the CIF/SVG output mapping.
     * @param scope The scope in which the I/O declaration is declared.
     * @return The metamodel representation of the I/O declaration.
     */
    public SvgOut checkSvgOut(ASvgOut astSvgOut, ParentScope<?> scope) {
        // Create metamodel representation.
        SvgOut svgOut = newSvgOut();
        svgOut.setPosition(astSvgOut.position);

        // Check 'id' expression.
        Expression id = transExpression(astSvgOut.svgId, STRING_TYPE_HINT, scope, null, tchecker);
        svgOut.setId(id);

        CifType idtype = id.getType();
        CifType nidtype = CifTypeUtils.normalizeType(idtype);
        if (!(nidtype instanceof StringType)) {
            tchecker.addProblem(ErrMsg.SVG_ID_NON_STR, id.getPosition(), CifTextUtils.typeToStr(idtype));
            throw new SemanticException();
        }

        // Set attribute/text.
        if (astSvgOut.svgAttr != null) {
            svgOut.setAttr(astSvgOut.svgAttr.txt);
            svgOut.setAttrTextPos(astSvgOut.svgAttr.position);
        } else {
            svgOut.setAttrTextPos(astSvgOut.svgTextPos);
        }

        // Check value.
        Expression value = transExpression(astSvgOut.value, NO_TYPE_HINT, scope, null, tchecker);
        svgOut.setValue(value);

        if (hasComponentLikeType(value.getType())) {
            tchecker.addProblem(ErrMsg.SVG_OUT_VALUE_COMP_TYPE, astSvgOut.value.position, typeToStr(value.getType()));
            throw new SemanticException();
        }

        // Check SVG file, if any.
        if (astSvgOut.svgFile != null) {
            svgOut.setSvgFile(checkSvgFile(astSvgOut.svgFile, scope));
        }

        // Return metamodel representation of the output mapping.
        return svgOut;
    }

    /**
     * Performs type checking on the CIF/SVG input mapping.
     *
     * @param astSvgIn The AST representation of the CIF/SVG input mapping.
     * @param scope The scope in which the I/O declaration is declared.
     * @return The metamodel representation of the I/O declaration.
     */
    public SvgIn checkSvgIn(ASvgIn astSvgIn, ParentScope<?> scope) {
        // Create metamodel representation.
        SvgIn svgIn = newSvgIn();
        svgIn.setPosition(astSvgIn.position);

        // Check 'id' expression.
        Expression id = transExpression(astSvgIn.svgId, STRING_TYPE_HINT, scope, null, tchecker);
        svgIn.setId(id);

        CifType idtype = id.getType();
        CifType nidtype = CifTypeUtils.normalizeType(idtype);
        if (!(nidtype instanceof StringType)) {
            tchecker.addProblem(ErrMsg.SVG_ID_NON_STR, id.getPosition(), CifTextUtils.typeToStr(idtype));
            throw new SemanticException();
        }

        // Check event.
        svgIn.setEvent(checkSvgInEvent(astSvgIn, scope));

        // Check SVG file, if any.
        if (astSvgIn.svgFile != null) {
            svgIn.setSvgFile(checkSvgFile(astSvgIn.svgFile, scope));
        }

        // Return metamodel representation of the input mapping.
        return svgIn;
    }

    /**
     * Performs type checking on the CIF/SVG input mapping event choice.
     *
     * @param astSvgIn The AST representation of the CIF/SVG input mapping.
     * @param scope The scope in which the I/O declaration is declared.
     * @return The metamodel representation of the event choice.
     */
    private SvgInEvent checkSvgInEvent(ASvgIn astSvgIn, ParentScope<?> scope) {
        if (astSvgIn.event instanceof ASvgInEventSingle) {
            return checkSvgInEvent((ASvgInEventSingle)astSvgIn.event, scope);
        } else if (astSvgIn.event instanceof ASvgInEventIf) {
            return checkSvgInEvent((ASvgInEventIf)astSvgIn.event, scope);
        } else {
            String msg = "Unknown svgin event choice: " + astSvgIn.event;
            throw new RuntimeException(msg);
        }
    }

    /**
     * Performs type checking on the CIF/SVG input mapping single event choice.
     *
     * @param astSingle The AST representation of the CIF/SVG input mapping single event choice.
     * @param scope The scope in which the I/O declaration is declared.
     * @return The metamodel representation of the CIF/SVG input mapping single event choice.
     */
    private SvgInEvent checkSvgInEvent(ASvgInEventSingle astSingle, SymbolScope<?> scope) {
        SvgInEventSingle single = newSvgInEventSingle();
        Expression eventRef = CifEventRefTypeChecker.checkEventRef(astSingle.name, scope, tchecker);
        single.setEvent(eventRef);
        return single;
    }

    /**
     * Performs type checking on the CIF/SVG input mapping 'if' event choice.
     *
     * @param astIf The AST representation of the CIF/SVG input mapping 'if' event choice.
     * @param scope The scope in which the I/O declaration is declared.
     * @return The metamodel representation of the CIF/SVG input mapping 'if' event choice.
     */
    private SvgInEvent checkSvgInEvent(ASvgInEventIf astIf, SymbolScope<?> scope) {
        SvgInEventIf mmIf = newSvgInEventIf();
        List<SvgInEventIfEntry> entries = mmIf.getEntries();
        for (ASvgInEventIfEntry astEntry: astIf.entries) {
            SvgInEventIfEntry entry;
            try {
                // Create metamodel representation of the entry.
                entry = newSvgInEventIfEntry();

                // Check guard/source predicate, if not 'else'.
                if (astEntry.guard != null) {
                    // Check expression.
                    Expression guard = transExpression(astEntry.guard, BOOL_TYPE_HINT, scope, null, tchecker);
                    entry.setGuard(guard);

                    // Check predicate.
                    CifType t = entry.getGuard().getType();
                    CifType nt = CifTypeUtils.normalizeType(t);
                    if (!(nt instanceof BoolType)) {
                        tchecker.addProblem(ErrMsg.GUARD_NON_BOOL, entry.getGuard().getPosition(),
                                CifTextUtils.typeToStr(t));
                        // Non-fatal problem.
                    }
                }

                // Check event reference.
                Expression eventRef = CifEventRefTypeChecker.checkEventRef(astEntry.name, scope, tchecker);
                entry.setEvent(eventRef);
            } catch (SemanticException ex) {
                // If type checking fails for an entry, continue with the next
                // one. Entries should be completely independent from each
                // other. Note that we skip adding the entry as well.
                continue;
            }

            // Add the entry, since we had no errors.
            entries.add(entry);
        }
        return mmIf;
    }
}
