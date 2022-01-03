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

import static org.eclipse.escet.cif.common.CifTextUtils.typeToStr;
import static org.eclipse.escet.cif.common.CifTypeUtils.hasComponentLikeType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newPrint;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newPrintFile;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newPrintFor;
import static org.eclipse.escet.cif.typechecker.CifExprsTypeChecker.BOOL_TYPE_HINT;
import static org.eclipse.escet.cif.typechecker.CifExprsTypeChecker.NO_TYPE_HINT;
import static org.eclipse.escet.cif.typechecker.CifExprsTypeChecker.transExpression;

import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.print.Print;
import org.eclipse.escet.cif.metamodel.cif.print.PrintFile;
import org.eclipse.escet.cif.metamodel.cif.print.PrintFor;
import org.eclipse.escet.cif.metamodel.cif.print.PrintForKind;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.parser.ast.iodecls.print.APrint;
import org.eclipse.escet.cif.parser.ast.iodecls.print.APrintFile;
import org.eclipse.escet.cif.parser.ast.iodecls.print.APrintFor;
import org.eclipse.escet.cif.parser.ast.tokens.AName;
import org.eclipse.escet.cif.typechecker.scopes.ParentScope;
import org.eclipse.escet.common.typechecker.SemanticException;

/** Print I/O declaration type checker, during 'normal' type checking phase. */
public class CifPrintTypeChecker {
    /** The CIF type checker to use. */
    private final CifTypeChecker tchecker;

    /**
     * Constructor for the {@link CifPrintTypeChecker} class.
     *
     * @param tchecker The CIF type checker to use.
     */
    public CifPrintTypeChecker(CifTypeChecker tchecker) {
        this.tchecker = tchecker;
    }

    /**
     * Performs type checking on a print file I/O declaration.
     *
     * @param astPrintFile The AST representation of the print file I/O declaration.
     * @return The metamodel representation of the I/O declaration.
     */
    public PrintFile checkPrintFile(APrintFile astPrintFile) {
        PrintFile printFile = newPrintFile();
        printFile.setPath(astPrintFile.path.txt);
        printFile.setPosition(astPrintFile.position);
        return printFile;
    }

    /**
     * Performs type checking on a print I/O declaration.
     *
     * @param astPrint The AST representation of the print I/O declaration.
     * @param scope The scope in which the I/O declaration is declared.
     * @return The metamodel representation of the I/O declaration.
     */
    public Print checkPrint(APrint astPrint, ParentScope<?> scope) {
        // Create metamodel representation.
        Print print = newPrint();
        print.setPosition(astPrint.position);

        // Check texts to print.
        if (astPrint.txt.pre != null) {
            Expression pre = transExpression(astPrint.txt.pre, NO_TYPE_HINT, scope, null, tchecker);
            print.setTxtPre(pre);

            if (hasComponentLikeType(pre.getType())) {
                tchecker.addProblem(ErrMsg.PRINT_TXT_COMP_TYPE, astPrint.txt.pre.position, "pre",
                        typeToStr(pre.getType()));
                throw new SemanticException();
            }
        }

        if (astPrint.txt.post != null) {
            Expression post = transExpression(astPrint.txt.post, NO_TYPE_HINT, scope, null, tchecker);
            print.setTxtPost(post);

            if (hasComponentLikeType(post.getType())) {
                tchecker.addProblem(ErrMsg.PRINT_TXT_COMP_TYPE, astPrint.txt.post.position, "post",
                        typeToStr(post.getType()));
                throw new SemanticException();
            }
        }

        // Check 'for' filters.
        for (APrintFor astPrintFor: astPrint.fors) {
            // Check 'for' filter.
            PrintFor printFor = checkPrintFor(astPrintFor, scope);
            print.getFors().add(printFor);
        }

        // Check 'when' filters.
        if (astPrint.when != null && astPrint.when.pre != null) {
            // Check expression.
            Expression pre = transExpression(astPrint.when.pre, BOOL_TYPE_HINT, scope, null, tchecker);
            print.setWhenPre(pre);

            // Check predicate.
            CifType t = pre.getType();
            CifType nt = CifTypeUtils.normalizeType(t);
            if (!(nt instanceof BoolType)) {
                tchecker.addProblem(ErrMsg.PRINT_WHEN_NON_BOOL, pre.getPosition(), "pre", CifTextUtils.typeToStr(t));
                // Non-fatal problem.
            }
        }

        if (astPrint.when != null && astPrint.when.post != null) {
            // Check expression.
            Expression post = transExpression(astPrint.when.post, BOOL_TYPE_HINT, scope, null, tchecker);
            print.setWhenPost(post);

            // Check predicate.
            CifType t = post.getType();
            CifType nt = CifTypeUtils.normalizeType(t);
            if (!(nt instanceof BoolType)) {
                tchecker.addProblem(ErrMsg.PRINT_WHEN_NON_BOOL, post.getPosition(), "post", CifTextUtils.typeToStr(t));
                // Non-fatal problem.
            }
        }

        // Check 'file'.
        if (astPrint.file != null) {
            PrintFile printFile = newPrintFile();
            printFile.setPath(astPrint.file.path.txt);
            printFile.setPosition(astPrint.file.path.position);
            print.setFile(printFile);
        }

        // Return metamodel representation.
        return print;
    }

    /**
     * Performs type checking on a 'for' filter of a print I/O declaration.
     *
     * @param astPrintFor The AST representation of the 'for' filter of a print I/O declaration.
     * @param scope The scope in which the I/O declaration is declared.
     * @return The metamodel representation of the I/O declaration.
     */
    public PrintFor checkPrintFor(APrintFor astPrintFor, ParentScope<?> scope) {
        PrintFor printFor = newPrintFor();
        printFor.setPosition(astPrintFor.position);
        printFor.setKind(Enum.valueOf(PrintForKind.class, astPrintFor.kind.name()));
        if (astPrintFor.name != null) {
            AName name = new AName(astPrintFor.name, astPrintFor.position);
            Expression eventRef = CifEventRefTypeChecker.checkEventRef(name, scope, tchecker);
            printFor.setEvent(eventRef);
        }
        return printFor;
    }
}
