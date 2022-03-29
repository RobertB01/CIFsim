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

package org.eclipse.escet.cif.typechecker;

import java.util.List;

import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.IoDecl;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgFile;
import org.eclipse.escet.cif.metamodel.cif.print.PrintFile;
import org.eclipse.escet.cif.parser.ast.iodecls.AIoDecl;
import org.eclipse.escet.cif.parser.ast.iodecls.print.APrint;
import org.eclipse.escet.cif.parser.ast.iodecls.print.APrintFile;
import org.eclipse.escet.cif.parser.ast.iodecls.svg.ASvgCopy;
import org.eclipse.escet.cif.parser.ast.iodecls.svg.ASvgFile;
import org.eclipse.escet.cif.parser.ast.iodecls.svg.ASvgIn;
import org.eclipse.escet.cif.parser.ast.iodecls.svg.ASvgMove;
import org.eclipse.escet.cif.parser.ast.iodecls.svg.ASvgOut;
import org.eclipse.escet.cif.typechecker.scopes.ParentScope;
import org.eclipse.escet.common.java.TextPosition;
import org.eclipse.escet.common.typechecker.SemanticException;

/** CIF I/O declaration checker. */
public class IoDeclTypeChecker {
    /** The CIF type checker to use. */
    private final CifTypeChecker tchecker;

    /** The CIF/SVG type checker to use. */
    private final CifSvgTypeChecker cifSvgChecker;

    /** The CIF print type checker to use. */
    private final CifPrintTypeChecker cifPrintChecker;

    /**
     * Constructor for the {@link IoDeclTypeChecker} class.
     *
     * @param tchecker The CIF type checker to use.
     */
    public IoDeclTypeChecker(CifTypeChecker tchecker) {
        this.tchecker = tchecker;
        this.cifSvgChecker = new CifSvgTypeChecker(tchecker);
        this.cifPrintChecker = new CifPrintTypeChecker(tchecker);
    }

    /**
     * Performs type checking on the I/O declarations of a scope. Also adds them to the metamodel object.
     *
     * @param astIoDecls The AST representations of the I/O declarations of the given scope.
     * @param scope The scope in which the I/O declarations are declared.
     * @param comp The component to which to add the I/O declarations. This is the component of the scope.
     */
    public void check(List<AIoDecl> astIoDecls, ParentScope<?> scope, ComplexComponent comp) {
        TextPosition svgFilePos = null;
        TextPosition printFilePos = null;
        List<IoDecl> ioDecls = comp.getIoDecls();
        for (AIoDecl astIoDecl: astIoDecls) {
            // Process and add to metamodel.
            IoDecl ioDecl;
            try {
                ioDecl = check(astIoDecl, scope);
            } catch (SemanticException ex) {
                // If type checking fails for a mapping, continue with the next
                // one. Mappings should be completely independent from each
                // other. Since we don't add the mapping, we can proceed.
                continue;
            }
            ioDecls.add(ioDecl);

            // Check for I/O declaration that may not be used more than once
            // in a single scope.
            if (ioDecl instanceof SvgFile) {
                if (svgFilePos == null) {
                    svgFilePos = astIoDecl.position;
                } else {
                    // Duplicate.
                    tchecker.addProblem(ErrMsg.SVG_DUPL_FILE, svgFilePos, scope.getAbsText());
                    tchecker.addProblem(ErrMsg.SVG_DUPL_FILE, astIoDecl.position, scope.getAbsText());
                    throw new SemanticException();
                }
            }

            if (ioDecl instanceof PrintFile) {
                if (printFilePos == null) {
                    printFilePos = astIoDecl.position;
                } else {
                    // Duplicate.
                    tchecker.addProblem(ErrMsg.PRINT_DUPL_FILE, printFilePos, scope.getAbsText());
                    tchecker.addProblem(ErrMsg.PRINT_DUPL_FILE, astIoDecl.position, scope.getAbsText());
                    throw new SemanticException();
                }
            }
        }
    }

    /**
     * Performs type checking on the given I/O declaration.
     *
     * @param ioDecl The AST representation of the I/O declaration to check.
     * @param scope The scope in which the I/O declaration is declared.
     * @return The metamodel representation of the I/O declaration.
     */
    public IoDecl check(AIoDecl ioDecl, ParentScope<?> scope) {
        if (ioDecl instanceof ASvgFile) {
            return cifSvgChecker.checkSvgFile((ASvgFile)ioDecl, scope);
        } else if (ioDecl instanceof ASvgCopy) {
            return cifSvgChecker.checkSvgCopy((ASvgCopy)ioDecl, scope);
        } else if (ioDecl instanceof ASvgMove) {
            return cifSvgChecker.checkSvgMove((ASvgMove)ioDecl, scope);
        } else if (ioDecl instanceof ASvgOut) {
            return cifSvgChecker.checkSvgOut((ASvgOut)ioDecl, scope);
        } else if (ioDecl instanceof ASvgIn) {
            return cifSvgChecker.checkSvgIn((ASvgIn)ioDecl, scope);
        } else if (ioDecl instanceof APrintFile) {
            return cifPrintChecker.checkPrintFile((APrintFile)ioDecl);
        } else if (ioDecl instanceof APrint) {
            return cifPrintChecker.checkPrint((APrint)ioDecl, scope);
        } else {
            throw new RuntimeException("Unknown I/O decl: " + ioDecl);
        }
    }
}
