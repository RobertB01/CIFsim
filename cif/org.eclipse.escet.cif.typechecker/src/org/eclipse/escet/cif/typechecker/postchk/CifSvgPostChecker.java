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

package org.eclipse.escet.cif.typechecker.postchk;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Pair.pair;
import static org.eclipse.escet.common.java.Sets.set;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.eclipse.escet.cif.common.CifEvalException;
import org.eclipse.escet.cif.common.CifEvalUtils;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifValueUtils;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.IoDecl;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgCopy;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgFile;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgIn;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgMove;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgOut;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.typechecker.ErrMsg;
import org.eclipse.escet.cif.typechecker.SourceFile;
import org.eclipse.escet.common.java.Pair;
import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.common.svg.SvgNameUtils;
import org.eclipse.escet.common.svg.SvgUtils;
import org.eclipse.escet.common.typechecker.SemanticException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/** CIF/SVG type checker, during 'post' type checking phase. */
public class CifSvgPostChecker {
    /** The post check environment to use. */
    private final CifPostCheckEnv env;

    /** The collected SVG file declarations. */
    private List<SvgFile> fileDecls = list();

    /**
     * The collected SVG copy declarations, with the SVG file declaration that applies to it (from a scope, not from the
     * declaration itself), or {@code null} if no SVG file declaration (from a scope) applies to it.
     */
    private List<Pair<SvgCopy, SvgFile>> copyDecls = list();

    /**
     * The collected SVG move declarations, with the SVG file declaration that applies to it (from a scope, not from the
     * declaration itself), or {@code null} if no SVG file declaration (from a scope) applies to it.
     */
    private List<Pair<SvgMove, SvgFile>> moveDecls = list();

    /**
     * The collected SVG output mappings, with the SVG file declaration that applies to it (from a scope, not from the
     * declaration itself), or {@code null} if no SVG file declaration (from a scope) applies to it.
     */
    private List<Pair<SvgOut, SvgFile>> outDecls = list();

    /**
     * The collected SVG input mappings, with the SVG file declaration that applies to it (from a scope, not from the
     * declaration itself), or {@code null} if no SVG file declaration (from a scope) applies to it.
     */
    private List<Pair<SvgIn, SvgFile>> inDecls = list();

    /** Mapping from {@link SourceFile#absPath absolute paths} of SVG files to their loaded XML/SVG documents. */
    private Map<String, Document> svgFilesDocMap = map();

    /**
     * Mapping from {@link SourceFile#absPath absolute paths} of SVG files to mappings from SVG elements to position
     * information for the copy declaration that copies the element. Note that only the root elements of the copied
     * trees are added, not all elements of the copied trees.
     */
    private Map<String, Map<Element, Position>> copyFileElemMap = map();

    /**
     * Mapping from {@link SourceFile#absPath absolute paths} of SVG files to mappings from SVG element ids to position
     * information for the move declaration that moves the element.
     */
    private Map<String, Map<String, Position>> moveFileIdsMap = map();

    /**
     * Mapping from {@link SourceFile#absPath absolute paths} of SVG files to mappings from pairs of SVG element ids and
     * attribute names, to position information for the output mapping that outputs to that pair.
     */
    private Map<String, Map<Pair<String, String>, Position>> outFileIdAttrsMap = map();

    /**
     * Mapping from {@link SourceFile#absPath absolute paths} of SVG files to mappings from text nodes to pairs of SVG
     * element ids and the position information for the output mapping that outputs to that text node.
     */
    private Map<String, Map<Text, Pair<String, Position>>> outFileIdTextsMap = map();

    /**
     * Mapping from {@link SourceFile#absPath absolute paths} of SVG files to mappings from SVG element ids to position
     * information for the input mapping that inputs from that element (for that SVG file).
     */
    private Map<String, Map<String, Position>> inFileIdsMap = map();

    /**
     * Constructor for the {@link CifSvgPostChecker} class.
     *
     * @param env The post check environment to use.
     */
    public CifSvgPostChecker(CifPostCheckEnv env) {
        this.env = env;
    }

    /**
     * Checks the given specification for SVG constraints that can only be checked during the 'post' type checking
     * phase.
     *
     * @param spec The specification to check.
     */
    public void check(Specification spec) {
        // Collect all CIF/SVG declarations.
        collect(spec, null);

        // First phase, load SVG files.
        checkSvgFileDecls();

        // Second phase, process copy declarations. This modifies the loaded
        // SVG documents, making them 'complete'.
        checkSvgCopyDecls();

        // Third phase, process move declarations. We need to detect duplicates
        // between moves and outputs for 'transform', and as such moves are
        // processed first.
        checkSvgMoveDecls();

        // Fourth phase, process input/output mappings.
        checkSvgInDecls();
        checkSvgOutDecls();
    }

    /**
     * Collects CIF/SVG declarations from the given component recursively.
     *
     * @param comp The component from which to collect recursively.
     * @param svgFile The currently active SVG file declaration (from the parent component), or {@code null} if not
     *     available/applicable.
     */
    private void collect(ComplexComponent comp, SvgFile svgFile) {
        // Collect SVG file declarations.
        for (IoDecl ioDecl: comp.getIoDecls()) {
            if (ioDecl instanceof SvgFile) {
                // Found SVG file declaration, so update currently active one.
                svgFile = (SvgFile)ioDecl;

                // Collect new SVG file declaration.
                fileDecls.add(svgFile);

                // We already checked before that we don't have multiple SVG
                // file declarations in a single scope, so we don't have to
                // continue looking for SVG file declarations in this scope.
                break;
            }
        }

        // Collect other CIF/SVG declarations of this scope.
        for (IoDecl ioDecl: comp.getIoDecls()) {
            if (ioDecl instanceof SvgCopy) {
                copyDecls.add(pair((SvgCopy)ioDecl, svgFile));
            } else if (ioDecl instanceof SvgMove) {
                moveDecls.add(pair((SvgMove)ioDecl, svgFile));
            } else if (ioDecl instanceof SvgOut) {
                outDecls.add(pair((SvgOut)ioDecl, svgFile));
            } else if (ioDecl instanceof SvgIn) {
                inDecls.add(pair((SvgIn)ioDecl, svgFile));
            }
        }

        // Recursively collect in child components.
        if (comp instanceof Group) {
            for (Component child: ((Group)comp).getComponents()) {
                collect((ComplexComponent)child, svgFile);
            }
        }
    }

    /**
     * Checks the collected SVG file declarations.
     *
     * @see #fileDecls
     */
    private void checkSvgFileDecls() {
        for (SvgFile decl: fileDecls) {
            checkSvgFile(decl);
        }
    }

    /**
     * Checks the collected SVG copy declarations.
     *
     * @see #copyDecls
     */
    private void checkSvgCopyDecls() {
        // No copy declaration has been checked yet.
        boolean[] checked = new boolean[copyDecls.size()];

        // We start by not considering an element that does not exist as an
        // error. In other words, we start by ignoring elements that do not
        // exist. The element may exist in a next iteration, after another
        // copy declaration has created it.
        boolean errNotExist = false;

        // Keep processing as long as there is still work to do.
        while (true) {
            // We have not yet made any progress this iteration.
            boolean progress = false;

            // Check all declarations that have not yet been checked.
            for (int i = 0; i < checked.length; i++) {
                // Skip if already checked.
                if (checked[i]) {
                    continue;
                }

                // Get declaration to check.
                Pair<SvgCopy, SvgFile> decl = copyDecls.get(i);

                // Check declaration.
                boolean exists;
                try {
                    // Check declaration and record whether the root element,
                    // of the tree to copy, exists.
                    exists = checkSvgCopy(decl.left, decl.right, errNotExist);

                    // If the root element exists, we have successfully checked
                    // the declaration (thus shouldn't check it again), and
                    // have made progress.
                    if (exists) {
                        checked[i] = true;
                        progress = true;
                    }
                } catch (SemanticException ex) {
                    // We found a problem while checking the declaration, so
                    // we have successfully checked the declaration (thus
                    // shouldn't check it again), and have made progress.
                    checked[i] = true;
                    progress = true;

                    // We continue checking the other copy declarations.
                }
            }

            // If we have progress (a new declaration was checked this
            // iteration), we should retry all the ones that failed before.
            if (progress) {
                continue;
            }

            // No more progress.
            if (!progress) {
                // If already reported remaining errors, we are done.
                if (errNotExist) {
                    break;
                }

                // Do one more iteration, but consider root elements that do
                // not exist as errors, to report all remaining errors.
                errNotExist = true;
            }
        }
    }

    /**
     * Checks the collected SVG move declarations.
     *
     * @see #moveDecls
     */
    private void checkSvgMoveDecls() {
        for (Pair<SvgMove, SvgFile> decl: moveDecls) {
            try {
                checkSvgMove(decl.left, decl.right);
            } catch (SemanticException ex) {
                // If type checking fails for a declaration, continue with the
                // next one. Declarations should be independent from each
                // other (i.e. we can't refer to CIF/SVG declarations or parts
                // of them).
            }
        }
    }

    /**
     * Checks the collected SVG output mappings.
     *
     * @see #outDecls
     */
    private void checkSvgOutDecls() {
        for (Pair<SvgOut, SvgFile> decl: outDecls) {
            try {
                checkSvgOut(decl.left, decl.right);
            } catch (SemanticException ex) {
                // If type checking fails for a declaration, continue with the
                // next one. Declarations should be independent from each
                // other (i.e. we can't refer to CIF/SVG declarations or parts
                // of them).
            }
        }
    }

    /**
     * Checks the collected SVG input mappings.
     *
     * @see #inDecls
     */
    private void checkSvgInDecls() {
        for (Pair<SvgIn, SvgFile> decl: inDecls) {
            try {
                checkSvgIn(decl.left, decl.right);
            } catch (SemanticException ex) {
                // If type checking fails for a declaration, continue with the
                // next one. Declarations should be independent from each
                // other (i.e. we can't refer to CIF/SVG declarations or parts
                // of them).
            }
        }
    }

    /**
     * Checks the given SVG file declaration.
     *
     * @param svgFile The SVG file declaration to check.
     */
    private void checkSvgFile(SvgFile svgFile) {
        // Get absolute path for the SVG file.
        String relSvgPath = svgFile.getPath();
        String absSvgPath = env.resolveImport(relSvgPath);

        // If SVG file already checked/loaded before, don't do it again.
        if (svgFilesDocMap.containsKey(absSvgPath)) {
            return;
        }

        // Load SVG document from SVG file.
        Document document;
        try {
            document = SvgUtils.loadSvgFileInternal(absSvgPath);
        } catch (FileNotFoundException e) {
            Position pos = svgFile.getPosition();
            env.addProblem(ErrMsg.SVG_FILE_NOT_FOUND, pos, env.getFileReportPath(absSvgPath, pos));
            throw new SemanticException();
        } catch (DOMException e) {
            Position pos = svgFile.getPosition();
            env.addProblem(ErrMsg.SVG_FILE_INVALID_FILE, pos, env.getFileReportPath(absSvgPath, pos));
            throw new SemanticException();
        } catch (IOException e) {
            Position pos = svgFile.getPosition();
            env.addProblem(ErrMsg.SVG_FILE_IO_ERROR, pos, env.getFileReportPath(absSvgPath, pos));
            throw new SemanticException();
        }

        // Store the loaded SVG document.
        svgFilesDocMap.put(absSvgPath, document);
    }

    /**
     * Checks the given CIF/SVG copy declaration.
     *
     * @param svgCopy The copy declaration to check.
     * @param svgFile The SVG file declaration that is active for the component in which the declaration is declared, or
     *     {@code null} if not available.
     * @param errNotExist Whether a non-existing (missing) root element is considered an error to be reported to the
     *     type checker.
     * @return Whether the element to copy exists.
     */
    private boolean checkSvgCopy(SvgCopy svgCopy, SvgFile svgFile, boolean errNotExist) {
        // Check SVG file, and get XML/SVG document.
        ComplexComponent comp = (ComplexComponent)svgCopy.eContainer();
        String absSvgPath = checkDeclSvgFile(comp, svgCopy.getPosition(), svgFile, svgCopy.getSvgFile());
        Document doc = svgFilesDocMap.get(absSvgPath);

        // Check SVG element 'id' and get the corresponding SVG element.
        Expression idExpr = svgCopy.getId();
        Element elem = checkSvgElementId(idExpr, absSvgPath, doc, errNotExist);
        if (elem == null) {
            return false;
        }
        String id = SvgUtils.getSvgElementId(elem);

        // Check for overlapping copies.
        checkOverlapCopyElems(absSvgPath, svgCopy, elem);

        // Check prefix.
        String pre = "";
        if (svgCopy.getPre() != null) {
            // Check static evaluability.
            Expression preExpr = svgCopy.getPre();
            if (!CifValueUtils.hasSingleValue(preExpr, false, true)) {
                env.addProblem(ErrMsg.SVG_NON_STATIC, preExpr.getPosition(), "CIF/SVG copy declaration prefix");
                throw new SemanticException();
            }

            // Evaluate the prefix.
            try {
                pre = (String)CifEvalUtils.eval(preExpr, false);
            } catch (CifEvalException e) {
                // Problem reported where the actual expression fails. This may
                // be reached via algebraic variables, etc. Since we only
                // evaluate statically constant expressions, it should be no
                // problem to report them outside of the 'pre' expression
                // (containment).
                env.addProblem(ErrMsg.EVAL_FAILURE, e.expr.getPosition(), e.getMessage());
                throw new SemanticException();
            }

            // Check the validity of the prefix itself.
            if (!SvgNameUtils.isValidSvgPrefixName(pre)) {
                env.addProblem(ErrMsg.SVG_NAME_INVALID, preExpr.getPosition(), "CIF/SVG copy declaration prefix", pre,
                        " prefix");
                throw new SemanticException();
            }
        }

        // Check postfix.
        String post = "";
        if (svgCopy.getPost() != null) {
            // Check static evaluability.
            Expression postExpr = svgCopy.getPost();
            if (!CifValueUtils.hasSingleValue(postExpr, false, true)) {
                env.addProblem(ErrMsg.SVG_NON_STATIC, postExpr.getPosition(), "CIF/SVG copy declaration postfix");
                throw new SemanticException();
            }

            // Evaluate the postfix.
            try {
                post = (String)CifEvalUtils.eval(postExpr, false);
            } catch (CifEvalException e) {
                // Problem reported where the actual expression fails. This may
                // be reached via algebraic variables, etc. Since we only
                // evaluate statically constant expressions, it should be no
                // problem to report them outside of the 'post' expression
                // (containment).
                env.addProblem(ErrMsg.EVAL_FAILURE, e.expr.getPosition(), e.getMessage());
                throw new SemanticException();
            }

            // Check the validity of the post itself.
            if (!SvgNameUtils.isValidSvgPostfixName(post)) {
                env.addProblem(ErrMsg.SVG_NAME_INVALID, postExpr.getPosition(), "CIF/SVG copy declaration postfix",
                        post, " postfix");
                throw new SemanticException();
            }
        }

        // Make sure we are copying a non-root element. Copying the root
        // element of an SVG image's XML tree is not allowed, as copies are
        // added as siblings of their originals, and there can only be one root
        // element.
        Node parent = elem.getParentNode();
        if (parent.getNodeType() != Node.ELEMENT_NODE) {
            Position reportPos = svgCopy.getPosition();
            env.addProblem(ErrMsg.SVG_COPY_ROOT, reportPos, id, env.getFileReportPath(absSvgPath, reportPos));
            throw new SemanticException();
        }

        // Copy the sub-tree.
        Element copy = (Element)elem.cloneNode(true);
        Pair<String, String> duplIds;

        // Rename the elements of the copied sub-tree.
        duplIds = SvgUtils.renameElements(copy, pre, post, doc);
        if (duplIds != null) {
            Position reportPos = svgCopy.getPosition();
            env.addProblem(ErrMsg.SVG_COPY_DUPL_ID, reportPos, id, env.getFileReportPath(absSvgPath, reportPos),
                    duplIds.left, duplIds.right);
            throw new SemanticException();
        }

        // Add the copied sub-tree as sibling of the original. Note that in the
        // simulator we add the copy as sibling before the original, but here
        // we don't care about the sibling/rendering order, and adding at the
        // end is more efficient performance wise.
        Element parentElement = (Element)parent;
        parentElement.appendChild(copy);

        // Element to copy exists.
        return true;
    }

    /**
     * Checks the given CIF/SVG move declaration.
     *
     * @param svgMove The move declaration to check.
     * @param svgFile The SVG file declaration that is active for the component in which the declaration is declared, or
     *     {@code null} if not available.
     */
    private void checkSvgMove(SvgMove svgMove, SvgFile svgFile) {
        // Check SVG file, and get XML/SVG document.
        ComplexComponent comp = (ComplexComponent)svgMove.eContainer();
        String absSvgPath = checkDeclSvgFile(comp, svgMove.getPosition(), svgFile, svgMove.getSvgFile());
        Document doc = svgFilesDocMap.get(absSvgPath);

        // Check SVG element 'id' and get the corresponding SVG element.
        Expression idExpr = svgMove.getId();
        Element elem = checkSvgElementId(idExpr, absSvgPath, doc, true);
        String id = SvgUtils.getSvgElementId(elem);

        // Check static evaluability of the x coordinate.
        Expression xExpr = svgMove.getX();
        if (!CifValueUtils.hasSingleValue(xExpr, false, true)) {
            env.addProblem(ErrMsg.SVG_NON_STATIC, xExpr.getPosition(), "CIF/SVG move declaration x coordinate");
            throw new SemanticException();
        }

        // Evaluate the x coordinate.
        try {
            CifEvalUtils.eval(xExpr, false);
        } catch (CifEvalException e) {
            // Problem reported where the actual expression fails. This may
            // be reached via algebraic variables, etc. Since we only
            // evaluate statically constant expressions, it should be no
            // problem to report them outside of the 'x' expression
            // (containment).
            env.addProblem(ErrMsg.EVAL_FAILURE, e.expr.getPosition(), e.getMessage());
            throw new SemanticException();
        }

        // Check static evaluability of the y coordinate.
        Expression yExpr = svgMove.getY();
        if (!CifValueUtils.hasSingleValue(yExpr, false, true)) {
            env.addProblem(ErrMsg.SVG_NON_STATIC, yExpr.getPosition(), "CIF/SVG move declaration y coordinate");
            throw new SemanticException();
        }

        // Evaluate the y coordinate.
        try {
            CifEvalUtils.eval(yExpr, false);
        } catch (CifEvalException e) {
            // Problem reported where the actual expression fails. This may
            // be reached via algebraic variables, etc. Since we only
            // evaluate statically constant expressions, it should be no
            // problem to report them outside of the 'y' expression
            // (containment).
            env.addProblem(ErrMsg.EVAL_FAILURE, e.expr.getPosition(), e.getMessage());
            throw new SemanticException();
        }

        // Check for duplicate move declarations.
        checkDuplMoveIds(absSvgPath, svgMove, id);
    }

    /**
     * Checks the given CIF/SVG output mapping.
     *
     * @param svgOut The output mapping to check.
     * @param svgFile The SVG file declaration that is active for the component in which the mapping is declared, or
     *     {@code null} if not available.
     */
    private void checkSvgOut(SvgOut svgOut, SvgFile svgFile) {
        // Check SVG file, and get XML/SVG document.
        ComplexComponent comp = (ComplexComponent)svgOut.eContainer();
        String absSvgPath = checkDeclSvgFile(comp, svgOut.getPosition(), svgFile, svgOut.getSvgFile());
        Document doc = svgFilesDocMap.get(absSvgPath);

        // Check SVG element 'id' and get the corresponding SVG element.
        Expression idExpr = svgOut.getId();
        Element elem = checkSvgElementId(idExpr, absSvgPath, doc, true);
        String id = SvgUtils.getSvgElementId(elem);

        // Type check the SVG attribute/text.
        String attrName = svgOut.getAttr();
        Position attrTextPos = svgOut.getAttrTextPos();
        Text textNode = checkMappingAttr(elem, id, attrName, idExpr.getPosition(), attrTextPos, absSvgPath);

        // Check for duplicate output mappings (per SVG file).
        if (textNode == null) {
            // Check for duplicate id/attribute pair.
            checkDuplOutputIdAttrPair(absSvgPath, svgOut, id, attrName);
        } else {
            // Check for duplicate text node.
            checkDuplOutputTextNode(absSvgPath, svgOut, id, textNode);
        }

        // Check for duplicate move/transform.
        if (textNode == null && attrName.equals("transform")) {
            // Get entry for SVG file.
            Map<String, Position> moveIdsMap = moveFileIdsMap.get(absSvgPath);
            if (moveIdsMap != null) {
                // Get position for move.
                Position movePos = moveIdsMap.get(id);
                if (movePos != null) {
                    // Move/transform found.
                    String movePath = env.getFileReportPath(absSvgPath, movePos);
                    String outPath = env.getFileReportPath(absSvgPath, svgOut.getPosition());
                    env.addProblem(ErrMsg.SVG_DUPL_MOVE_TRANSFORM, movePos, id, movePath);
                    env.addProblem(ErrMsg.SVG_DUPL_MOVE_TRANSFORM, svgOut.getPosition(), id, outPath);
                    // Non-fatal problem.
                }
            }
        }
    }

    /**
     * Checks the given CIF/SVG input mapping.
     *
     * @param svgIn The input mapping to check.
     * @param svgFile The SVG file declaration that is active for the component in which the mapping is declared, or
     *     {@code null} if not available.
     */
    private void checkSvgIn(SvgIn svgIn, SvgFile svgFile) {
        // Check SVG file, and get XML/SVG document.
        ComplexComponent comp = (ComplexComponent)svgIn.eContainer();
        String absSvgPath = checkDeclSvgFile(comp, svgIn.getPosition(), svgFile, svgIn.getSvgFile());
        Document doc = svgFilesDocMap.get(absSvgPath);

        // Check SVG element 'id'.
        Expression idExpr = svgIn.getId();
        Element elem = checkSvgElementId(idExpr, absSvgPath, doc, true);
        String id = SvgUtils.getSvgElementId(elem);

        // Check for duplicate input mappings.
        checkDuplInputIds(absSvgPath, svgIn, id);
    }

    /**
     * Checks the SVG files that apply to an CIF/SVG declaration, other than an SVG file declaration.
     *
     * @param comp The component in which the CIF/SVG declaration is declared.
     * @param declPos Position information for the CIF/SVG declaration.
     * @param compSvgFile The SVG file declaration that is active for the component in which the CIF/SVG declaration is
     *     declared, or {@code null} if not available.
     * @param declSvgFile The SVG file declaration in the CIF/SVG declaration itself, or {@code null} if not available.
     * @return The {@link SourceFile#absPath absolute path} of the SVG image file to use for the CIF/SVG declaration.
     */
    private String checkDeclSvgFile(ComplexComponent comp, Position declPos, SvgFile compSvgFile, SvgFile declSvgFile) {
        // Get SVG file to which CIF/SVG declaration applies.
        SvgFile svgFile = compSvgFile;
        if (declSvgFile != null) {
            checkSvgFile(declSvgFile);
            svgFile = declSvgFile;
        }

        // Check for missing SVG file declaration.
        if (svgFile == null) {
            env.addProblem(ErrMsg.SVG_DECL_NO_FILE, declPos, CifTextUtils.getComponentText2(comp));
            throw new SemanticException();
        }

        // Get absolute path for the SVG file.
        String relPath = svgFile.getPath();
        String absPath = env.resolveImport(relPath);
        return absPath;
    }

    /**
     * Checks the 'id' of an SVG element.
     *
     * @param idExpr The 'id' expression that evaluates to the 'id'.
     * @param svgAbsPath The {@link SourceFile#absPath absolute path} to the SVG file in which to resolve the element
     *     with the given 'id'.
     * @param doc The SVG/XML document of the SVG file in which to resolve the element with the given 'id'.
     * @param errNotExist Whether a non-existing (missing) element is considered an error to be reported to the type
     *     checker.
     * @return The resolved SVG element. If multiple elements have the same id, only one of them is returned. If the
     *     element does not exist and {@code errNotExist} is {@code false}, {@code null} is returned.
     */
    private Element checkSvgElementId(Expression idExpr, String svgAbsPath, Document doc, boolean errNotExist) {
        // Check static evaluability of the 'id' expression.
        if (!CifValueUtils.hasSingleValue(idExpr, false, true)) {
            env.addProblem(ErrMsg.SVG_NON_STATIC, idExpr.getPosition(), "SVG element id");
            throw new SemanticException();
        }

        // Evaluate the 'id'.
        String id;
        try {
            id = (String)CifEvalUtils.eval(idExpr, false);
        } catch (CifEvalException e) {
            // Problem reported where the actual expression fails. This may
            // be reached via algebraic variables, etc. Since we only evaluate
            // statically constant expressions, it should be no problem to
            // report them outside of the 'id' expression (containment).
            env.addProblem(ErrMsg.EVAL_FAILURE, e.expr.getPosition(), e.getMessage());
            throw new SemanticException();
        }

        // Check the validity of the id itself.
        if (!SvgNameUtils.isValidSvgName(id)) {
            env.addProblem(ErrMsg.SVG_NAME_INVALID, idExpr.getPosition(), "SVG element id", id, "");
            throw new SemanticException();
        }

        // Resolve the element for the id.
        Element elem = doc.getElementById(id);
        if (elem == null && errNotExist) {
            Position pos = idExpr.getPosition();
            env.addProblem(ErrMsg.SVG_ELEM_ID_NOT_FOUND, pos, id, env.getFileReportPath(svgAbsPath, pos));
            throw new SemanticException();
        }

        // Return the element with the given 'id', or 'null' if not exist.
        return elem;
    }

    /**
     * Type check an SVG attribute name or text node of an output mapping.
     *
     * @param elem The SVG element to check.
     * @param id The SVG element 'id'.
     * @param attrName The SVG element attribute name, or {@code null} to set the text of the element.
     * @param idPos Position information for the SVG element id.
     * @param attrTextPos Position information for the SVG attribute name or 'text' keyword.
     * @param absSvgPath The {@link SourceFile#absPath absolute path} of the SVG file to which the mapping applies.
     * @return The SVG text node if text is being set, or {@code null} if an attribute is being set.
     */
    private Text checkMappingAttr(Element elem, String id, String attrName, Position idPos, Position attrTextPos,
            String absSvgPath)
    {
        if (attrName == null) {
            // Get the text node child of the element.
            Text textNode = SvgUtils.getTextNode(elem);
            if (textNode == null) {
                String path = env.getFileReportPath(absSvgPath, attrTextPos);
                env.addProblem(ErrMsg.SVG_ELEM_NO_TEXT, attrTextPos, id, path);
                throw new SemanticException();
            }

            // Return the text node.
            return textNode;
        } else {
            // Check the validity of the attribute name.
            if (!SvgNameUtils.isValidSvgName(attrName)) {
                env.addProblem(ErrMsg.SVG_NAME_INVALID, attrTextPos, "SVG attribute name", attrName, "");
                throw new SemanticException();
            }

            // Check element/attribute names against the SVG standard.
            String elemName = elem.getNodeName();
            Set<String> elemAttrNames = SvgNameUtils.getDefinedAttrs(elemName);
            if (elemAttrNames == null) {
                // Unknown element name.
                String path = env.getFileReportPath(absSvgPath, idPos);
                env.addProblem(ErrMsg.SVG_UNKNOWN_NAME_ELEM, idPos, id, path, elemName);
                // Non-fatal problem.
            } else if (!elemAttrNames.contains(attrName)) {
                // Unknown attribute name.
                String path = env.getFileReportPath(absSvgPath, idPos);
                env.addProblem(ErrMsg.SVG_UNKNOWN_NAME_ATTR, attrTextPos, id, path, elemName, attrName);
                // Non-fatal problem.
            }

            // Attribute 'id' is unsupported.
            if (attrName.toLowerCase(Locale.US).equals("id")) {
                env.addProblem(ErrMsg.SVG_ATTR_UNSUPPORTED, attrTextPos, "id");
                // Non-fatal problem.
            }

            // Attribute 'style' is unsupported.
            if (attrName.toLowerCase(Locale.US).equals("style")) {
                env.addProblem(ErrMsg.SVG_ATTR_UNSUPPORTED, attrTextPos, "style");
                // Non-fatal problem.
            }

            // We don't check whether attributes exist. Attributes may not yet
            // exist in the SVG image, as they may be constructed when they are
            // first set.
            return null;
        }
    }

    /**
     * Checks a copy declaration for overlap, related to other copy declarations.
     *
     * <p>
     * Copying the same element twice is not a problem, but copying an element and one of its ancestors or descendants
     * is reported. The latter has different outcomes depending on the order of application of the copy declarations.
     * </p>
     *
     * @param svgAbsPath The {@link SourceFile#absPath absolute path} to the SVG file to which the mapping applies.
     * @param svgCopy The copy mapping to check.
     * @param rootElem The SVG element that is the root of the tree to copy.
     */
    private void checkOverlapCopyElems(String svgAbsPath, SvgCopy svgCopy, Element rootElem) {
        // Get entry for SVG file.
        Map<Element, Position> copyElemMap = copyFileElemMap.get(svgAbsPath);
        if (copyElemMap == null) {
            // No entry for SVG file: add it. No duplicate yet.
            copyElemMap = map();
            copyElemMap.put(rootElem, svgCopy.getPosition());
            copyFileElemMap.put(svgAbsPath, copyElemMap);
            return;
        }

        // Initialize collection of ancestors/descendants. We allow copying the
        // same element multiple times, so we don't add the element itself
        // here.
        Set<Element> elems = set();

        // Additionally collect the ancestors.
        Node current = rootElem;
        while (current.getParentNode() != null) {
            current = current.getParentNode();
            if (current instanceof Element) {
                elems.add((Element)current);
            }
        }

        // Additionally collect the descendants.
        NodeList descendants = rootElem.getElementsByTagName("*");
        for (int i = 0; i < descendants.getLength(); i++) {
            elems.add((Element)descendants.item(i));
        }

        // Check for duplicate against all collected elements.
        for (Element elem: elems) {
            Position prevPos = copyElemMap.get(elem);
            if (prevPos != null) {
                // Duplicate input mapping.
                String prevPath = env.getFileReportPath(svgAbsPath, prevPos);
                String curPath = env.getFileReportPath(svgAbsPath, svgCopy.getPosition());
                env.addProblem(ErrMsg.SVG_COPY_OVERLAP, prevPos, SvgUtils.getSvgElementId(elem), prevPath,
                        SvgUtils.getSvgElementId(rootElem));
                env.addProblem(ErrMsg.SVG_COPY_OVERLAP, svgCopy.getPosition(), SvgUtils.getSvgElementId(rootElem),
                        curPath, SvgUtils.getSvgElementId(elem));
                // Non-fatal problem.
            }
        }

        if (!copyElemMap.containsKey(rootElem)) {
            // Add the new element.
            copyElemMap.put(rootElem, svgCopy.getPosition());
        }
    }

    /**
     * Checks a move declaration for duplicate ids, related to other move declarations.
     *
     * @param svgAbsPath The {@link SourceFile#absPath absolute path} to the SVG file to which the declaration applies.
     * @param svgMove The move declaration to check.
     * @param id The SVG element 'id'.
     */
    private void checkDuplMoveIds(String svgAbsPath, SvgMove svgMove, String id) {
        // Get entry for SVG file.
        Map<String, Position> moveIdsMap = moveFileIdsMap.get(svgAbsPath);
        if (moveIdsMap == null) {
            // No entry for SVG file: add it. No duplicate yet.
            moveIdsMap = map();
            moveIdsMap.put(id, svgMove.getPosition());
            moveFileIdsMap.put(svgAbsPath, moveIdsMap);
            return;
        }

        // Get entry for 'id'.
        Position prevPos = moveIdsMap.get(id);
        if (prevPos != null) {
            // Duplicate move declaration.
            String prevPath = env.getFileReportPath(svgAbsPath, prevPos);
            String curPath = env.getFileReportPath(svgAbsPath, svgMove.getPosition());
            env.addProblem(ErrMsg.SVG_DUPL_MOVE_ID, prevPos, id, prevPath);
            env.addProblem(ErrMsg.SVG_DUPL_MOVE_ID, svgMove.getPosition(), id, curPath);
            // Non-fatal problem.
        } else {
            // Add the new 'id'.
            moveIdsMap.put(id, svgMove.getPosition());
        }
    }

    /**
     * Checks an output mapping for duplicate id/attribute pairs, related to other output mappings, per SVG file.
     *
     * @param svgAbsPath The {@link SourceFile#absPath absolute path} to the SVG file to which the mapping applies.
     * @param svgOut The output mapping to check. Must be an output mapping for an attribute of an SVG element.
     * @param id The 'id' of the SVG element for which an attribute is changed.
     * @param attrName The name of the attribute that is being changed.
     */
    private void checkDuplOutputIdAttrPair(String svgAbsPath, SvgOut svgOut, String id, String attrName) {
        // Get entry for SVG file.
        Map<Pair<String, String>, Position> outIdAttrsMap;
        outIdAttrsMap = outFileIdAttrsMap.get(svgAbsPath);
        if (outIdAttrsMap == null) {
            // No entry for SVG file: add it. No duplicate yet.
            outIdAttrsMap = map();
            outIdAttrsMap.put(pair(id, attrName), svgOut.getPosition());
            outFileIdAttrsMap.put(svgAbsPath, outIdAttrsMap);
            return;
        }

        // Get entry for 'id' and attribute name.
        Pair<String, String> pair = pair(id, attrName);
        Position prevPos = outIdAttrsMap.get(pair);
        if (prevPos != null) {
            // Duplicate output mapping.
            String prevPath = env.getFileReportPath(svgAbsPath, prevPos);
            String curPath = env.getFileReportPath(svgAbsPath, svgOut.getPosition());
            env.addProblem(ErrMsg.SVG_DUPL_OUTPUT_ID_ATTR, prevPos, attrName, id, prevPath);
            env.addProblem(ErrMsg.SVG_DUPL_OUTPUT_ID_ATTR, svgOut.getPosition(), attrName, id, curPath);
            // Non-fatal problem.
        } else {
            // Add the new pair.
            outIdAttrsMap.put(pair, svgOut.getPosition());
        }
    }

    /**
     * Checks an output mapping for duplicate text nodes, related to other output mappings, per SVG file.
     *
     * <p>
     * Note that even if the element ids are different, we can still have the same text node, as the text node of an
     * element may not be a direct child, but instead could be one of its descendants. That is, if we have a
     * {@code text} element with id {@code x}, with a child element {@code tspan} with id {@code y}, with a text node,
     * then both elements {@code x} and {@code y} will use that same text node.
     * </p>
     *
     * @param svgAbsPath The {@link SourceFile#absPath absolute path} to the SVG file to which the mapping applies.
     * @param svgOut The output mapping to check. Must be an output mapping for the text of an SVG element.
     * @param id The 'id' of the SVG element for which text is changed.
     * @param textNode The text node for which text is to be set by the mapping.
     */
    private void checkDuplOutputTextNode(String svgAbsPath, SvgOut svgOut, String id, Text textNode) {
        // Get entry for SVG file.
        Map<Text, Pair<String, Position>> outIdTextsMap;
        outIdTextsMap = outFileIdTextsMap.get(svgAbsPath);
        if (outIdTextsMap == null) {
            // No entry for SVG file: add it. No duplicate yet.
            outIdTextsMap = map();
            outIdTextsMap.put(textNode, pair(id, svgOut.getPosition()));
            outFileIdTextsMap.put(svgAbsPath, outIdTextsMap);
            return;
        }

        // Get entry for text node.
        Pair<String, Position> prevPair = outIdTextsMap.get(textNode);
        if (prevPair != null) {
            // Duplicate text node.
            String prevPath = env.getFileReportPath(svgAbsPath, prevPair.right);
            String curPath = env.getFileReportPath(svgAbsPath, svgOut.getPosition());
            env.addProblem(ErrMsg.SVG_DUPL_OUTPUT_TEXT, prevPair.right, prevPair.left, id, prevPath);
            env.addProblem(ErrMsg.SVG_DUPL_OUTPUT_TEXT, svgOut.getPosition(), prevPair.left, id, curPath);
            // Non-fatal problem.
        } else {
            // Add the new text node.
            outIdTextsMap.put(textNode, pair(id, svgOut.getPosition()));
        }
    }

    /**
     * Checks an input mapping for duplicate ids, related to other input mappings.
     *
     * @param svgAbsPath The {@link SourceFile#absPath absolute path} to the SVG file to which the mapping applies.
     * @param svgIn The input mapping to check.
     * @param id The SVG element 'id'.
     */
    private void checkDuplInputIds(String svgAbsPath, SvgIn svgIn, String id) {
        // Get entry for SVG file.
        Map<String, Position> inIdsMap = inFileIdsMap.get(svgAbsPath);
        if (inIdsMap == null) {
            // No entry for SVG file: add it. No duplicate yet.
            inIdsMap = map();
            inIdsMap.put(id, svgIn.getPosition());
            inFileIdsMap.put(svgAbsPath, inIdsMap);
            return;
        }

        // Get entry for 'id'.
        Position prevPos = inIdsMap.get(id);
        if (prevPos != null) {
            // Duplicate input mapping.
            String prevPath = env.getFileReportPath(svgAbsPath, prevPos);
            String curPath = env.getFileReportPath(svgAbsPath, svgIn.getPosition());
            env.addProblem(ErrMsg.SVG_DUPL_INPUT_ID, prevPos, id, prevPath);
            env.addProblem(ErrMsg.SVG_DUPL_INPUT_ID, svgIn.getPosition(), id, curPath);
            // Non-fatal problem.
        } else {
            // Add the new 'id'.
            inIdsMap.put(id, svgIn.getPosition());
        }
    }
}
