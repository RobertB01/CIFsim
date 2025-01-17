//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.typechecker.scopes;

import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newAlgParameter;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newAlgVariable;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newAutomaton;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newComponentDef;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newComponentInst;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newComponentParameter;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newConstant;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newContVariable;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newDiscVariable;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEnumDecl;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEnumLiteral;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEvent;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEventParameter;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newExternalFunction;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newFunctionParameter;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newGroup;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newInputVariable;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newInternalFunction;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newInvariant;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newLocation;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newLocationParameter;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newSpecification;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newTypeDecl;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.position.common.PositionUtils.getSubRange;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.escet.cif.metamodel.cif.AlgParameter;
import org.eclipse.escet.cif.metamodel.cif.ComponentDef;
import org.eclipse.escet.cif.metamodel.cif.ComponentInst;
import org.eclipse.escet.cif.metamodel.cif.ComponentParameter;
import org.eclipse.escet.cif.metamodel.cif.EventParameter;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.InvKind;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.LocationParameter;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.SupKind;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Constant;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumLiteral;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.TypeDecl;
import org.eclipse.escet.cif.metamodel.cif.functions.ExternalFunction;
import org.eclipse.escet.cif.metamodel.cif.functions.FunctionParameter;
import org.eclipse.escet.cif.metamodel.cif.functions.InternalFunction;
import org.eclipse.escet.cif.parser.CifParser;
import org.eclipse.escet.cif.parser.ast.AAlgParameter;
import org.eclipse.escet.cif.parser.ast.ACompDecl;
import org.eclipse.escet.cif.parser.ast.ACompDefDecl;
import org.eclipse.escet.cif.parser.ast.ACompInstDecl;
import org.eclipse.escet.cif.parser.ast.AComponentParameter;
import org.eclipse.escet.cif.parser.ast.ADecl;
import org.eclipse.escet.cif.parser.ast.AEquation;
import org.eclipse.escet.cif.parser.ast.AEquationDecl;
import org.eclipse.escet.cif.parser.ast.AEventParameter;
import org.eclipse.escet.cif.parser.ast.AEventParameterPart;
import org.eclipse.escet.cif.parser.ast.AFuncDecl;
import org.eclipse.escet.cif.parser.ast.AImport;
import org.eclipse.escet.cif.parser.ast.AImportDecl;
import org.eclipse.escet.cif.parser.ast.AInitialDecl;
import org.eclipse.escet.cif.parser.ast.AInvariant;
import org.eclipse.escet.cif.parser.ast.AInvariantDecl;
import org.eclipse.escet.cif.parser.ast.ALocationParameter;
import org.eclipse.escet.cif.parser.ast.AMarkedDecl;
import org.eclipse.escet.cif.parser.ast.ANamespaceDecl;
import org.eclipse.escet.cif.parser.ast.AParameter;
import org.eclipse.escet.cif.parser.ast.ASpecification;
import org.eclipse.escet.cif.parser.ast.automata.AAlphabetDecl;
import org.eclipse.escet.cif.parser.ast.automata.AAutomatonBody;
import org.eclipse.escet.cif.parser.ast.automata.AEquationLocationElement;
import org.eclipse.escet.cif.parser.ast.automata.AInvariantLocationElement;
import org.eclipse.escet.cif.parser.ast.automata.ALocation;
import org.eclipse.escet.cif.parser.ast.automata.ALocationElement;
import org.eclipse.escet.cif.parser.ast.automata.AMonitorDecl;
import org.eclipse.escet.cif.parser.ast.declarations.AAlgVariable;
import org.eclipse.escet.cif.parser.ast.declarations.AAlgVariableDecl;
import org.eclipse.escet.cif.parser.ast.declarations.AConstDecl;
import org.eclipse.escet.cif.parser.ast.declarations.AConstant;
import org.eclipse.escet.cif.parser.ast.declarations.AContVariable;
import org.eclipse.escet.cif.parser.ast.declarations.AContVariableDecl;
import org.eclipse.escet.cif.parser.ast.declarations.ADiscVariable;
import org.eclipse.escet.cif.parser.ast.declarations.ADiscVariableDecl;
import org.eclipse.escet.cif.parser.ast.declarations.AEnumDecl;
import org.eclipse.escet.cif.parser.ast.declarations.AEventDecl;
import org.eclipse.escet.cif.parser.ast.declarations.AInputVariableDecl;
import org.eclipse.escet.cif.parser.ast.declarations.ATypeDef;
import org.eclipse.escet.cif.parser.ast.declarations.ATypeDefDecl;
import org.eclipse.escet.cif.parser.ast.functions.AExternalFuncBody;
import org.eclipse.escet.cif.parser.ast.functions.AFuncParam;
import org.eclipse.escet.cif.parser.ast.functions.AInternalFuncBody;
import org.eclipse.escet.cif.parser.ast.iodecls.AIoDecl;
import org.eclipse.escet.cif.parser.ast.tokens.AAnnotatedIdentifier;
import org.eclipse.escet.cif.parser.ast.tokens.AIdentifier;
import org.eclipse.escet.cif.parser.ast.tokens.AName;
import org.eclipse.escet.cif.typechecker.CifTypeChecker;
import org.eclipse.escet.cif.typechecker.ErrMsg;
import org.eclipse.escet.cif.typechecker.SourceFile;
import org.eclipse.escet.cif.typechecker.declwrap.AlgParamDeclWrap;
import org.eclipse.escet.cif.typechecker.declwrap.AlgVariableDeclWrap;
import org.eclipse.escet.cif.typechecker.declwrap.ConstDeclWrap;
import org.eclipse.escet.cif.typechecker.declwrap.ContVariableDeclWrap;
import org.eclipse.escet.cif.typechecker.declwrap.DiscVariableDeclWrap;
import org.eclipse.escet.cif.typechecker.declwrap.EnumDeclWrap;
import org.eclipse.escet.cif.typechecker.declwrap.EnumLiteralDeclWrap;
import org.eclipse.escet.cif.typechecker.declwrap.EventDeclWrap;
import org.eclipse.escet.cif.typechecker.declwrap.EventParamDeclWrap;
import org.eclipse.escet.cif.typechecker.declwrap.FuncParamDeclWrap;
import org.eclipse.escet.cif.typechecker.declwrap.FuncVariableDeclWrap;
import org.eclipse.escet.cif.typechecker.declwrap.InputVariableDeclWrap;
import org.eclipse.escet.cif.typechecker.declwrap.InvariantInfo;
import org.eclipse.escet.cif.typechecker.declwrap.LocationDeclWrap;
import org.eclipse.escet.cif.typechecker.declwrap.LocationParamDeclWrap;
import org.eclipse.escet.cif.typechecker.declwrap.TypeDeclWrap;
import org.eclipse.escet.common.app.framework.PlatformUriUtils;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.TextPosition;
import org.eclipse.escet.common.java.exceptions.InputOutputException;
import org.eclipse.escet.common.java.exceptions.InvalidInputException;
import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.common.typechecker.SemanticException;
import org.eclipse.escet.setext.runtime.Token;
import org.eclipse.escet.setext.runtime.exceptions.SyntaxException;

/** CIF {@link SymbolScope} builder, for a single {@link SourceFile}. */
public class SymbolScopeBuilder {
    /** The CIF type checker to use. */
    private final CifTypeChecker tchecker;

    /** The CIF source file for which to build a symbol table. */
    private final SourceFile sourceFile;

    /** The CIF source file metadata for the main source file. */
    private final SourceFile mainFile;

    /**
     * Mapping from {@link SourceFile#absPath absolute paths} of source files imported from {@link #sourceFile}, to the
     * position information for their imports. Used to detect duplicate imports in {@link #sourceFile}.
     */
    private Map<String, TextPosition> imports = map();

    /**
     * Constructor for the {@link SymbolScopeBuilder} class.
     *
     * @param tchecker The CIF type checker to use.
     * @param sourceFile The CIF source file for which to build a symbol table.
     * @param mainFile The CIF source file metadata for the main source file.
     */
    private SymbolScopeBuilder(CifTypeChecker tchecker, SourceFile sourceFile, SourceFile mainFile) {
        this.tchecker = tchecker;
        this.sourceFile = sourceFile;
        this.mainFile = mainFile;
    }

    /**
     * Builds a symbol scope for a source file. For the main source file, the parse result should be provided. For
     * imported files, this method will perform the parsing.
     *
     * @param tchecker The CIF type checker to use.
     * @param sourceFile The source file metadata.
     * @param mainFile The CIF source file metadata for the main source file.
     * @param spec The CIF AST specification for which to build the symbol scope (for the main source file), or
     *     {@code null} (for imported source files).
     * @return The symbol scope for the main specification.
     */
    public static SpecScope build(CifTypeChecker tchecker, SourceFile sourceFile, SourceFile mainFile,
            ASpecification spec)
    {
        // Paranoia checking.
        Assert.ifAndOnlyIf(sourceFile.main, spec != null);

        // Parse source file to AST, for imported source files.
        if (spec == null) {
            String relPath = sourceFile.getRelativePathFrom(mainFile);
            try {
                CifParser parser = new CifParser();

                if (PlatformUriUtils.isPlatformUri(sourceFile.absPath)) {
                    // Platform resource. Make sure it exists.
                    if (!PlatformUriUtils.exists(sourceFile.absPath)) {
                        throw new InvalidInputException(null);
                    }

                    // Parse Eclipse platform URI. Can't fail, as otherwise the
                    // existence check above would have failed already.
                    URI uri = URI.createURI(sourceFile.absPath);

                    // Parse using stream.
                    URIConverter converter = URIConverter.INSTANCE;
                    try (InputStream stream = converter.createInputStream(uri)) {
                        String src = fmt("Resource \"%s\": ", relPath);
                        spec = parser.parseStream(stream, sourceFile.absPath, src);
                    }
                } else {
                    // Parse file, given absolute local file system path.
                    spec = parser.parseFile(sourceFile.absPath, relPath);
                }

                if (!parser.getWarnings().isEmpty()) {
                    tchecker.addProblem(ErrMsg.IMPORT_SYNTAX_WARNING, sourceFile.importingPos, relPath);
                }
            } catch (InvalidInputException e) {
                tchecker.addProblem(ErrMsg.IMPORT_NOT_FOUND, sourceFile.importingPos, relPath);
                throw new SemanticException();
            } catch (InputOutputException | IOException e) {
                tchecker.addProblem(ErrMsg.IMPORT_IO_ERROR, sourceFile.importingPos, relPath);
                throw new SemanticException();
            } catch (SyntaxException e) {
                tchecker.addProblem(ErrMsg.IMPORT_SYNTAX_ERROR, sourceFile.importingPos, relPath);
                throw new SemanticException();
            }
        }

        // Build symbol scope for AST.
        SymbolScopeBuilder builder = new SymbolScopeBuilder(tchecker, sourceFile, mainFile);
        return builder.buildSpecification(spec);
    }

    /**
     * Builds a symbol scope for a specification.
     *
     * @param spec The CIF AST specification to build the symbol scope for.
     * @return The symbol scope for the specification.
     */
    private SpecScope buildSpecification(ASpecification spec) {
        // Construct object.
        Specification obj = newSpecification();
        obj.setName("specification");
        obj.setPosition(spec.createPosition());

        // Construct scope.
        SpecScope specScope = new SpecScope(obj, tchecker);

        // Process namespaces.
        ParentScope<?> bodyScope = processNamespaces(spec, specScope);

        // Add annotations.
        specScope.astAnnotations.addAll(spec.annotations);

        // Fill body scope.
        for (ADecl decl: spec.body.decls) {
            addGroupDecl(decl, specScope, bodyScope);
        }

        // Return final result (scope for the entire file/specification).
        return specScope;
    }

    /**
     * Processes the namespace declarations of the specification.
     *
     * @param spec The CIF AST specification.
     * @param specScope The specification symbol scope.
     * @return The specification symbol scope in case no namespace declaration was found, and the most inner group of
     *     the namespace otherwise. This is the scope to which the remaining contents of the file is to be added.
     */
    private ParentScope<?> processNamespaces(ASpecification spec, SpecScope specScope) {
        ParentScope<?> rslt = specScope;

        TextPosition prevNamespacePos = null;
        for (ADecl decl: spec.body.decls) {
            // Get namespace declaration.
            if (!(decl instanceof ANamespaceDecl)) {
                continue;
            }
            ANamespaceDecl nsDecl = (ANamespaceDecl)decl;

            // Check for duplicate namespace.
            if (prevNamespacePos != null) {
                tchecker.addProblem(ErrMsg.DUPL_NAMESPACE, prevNamespacePos);
                tchecker.addProblem(ErrMsg.DUPL_NAMESPACE, nsDecl.position);
                // Non-fatal problem.
            }
            prevNamespacePos = nsDecl.position;

            // Add groups.
            int offset = 0;
            for (String groupName: StringUtils.split(nsDecl.name.name, '.')) {
                // Construct position information for the group. We know that
                // the whole namespace name is on a single line, as the tokens
                // can't contain new line characters.
                int length = groupName.length();
                Position groupPos = nsDecl.name.createPosition();
                groupPos = getSubRange(groupPos, offset, groupName.length());
                offset += length + 1; // Next group name after the dot.

                // Construct group.
                Group group = newGroup();
                group.setName(groupName);
                group.setPosition(groupPos);

                // Construct group scope.
                GroupScope groupScope = new GroupScope(group, rslt, tchecker);

                // Add group to the parent object.
                rslt.getGroup().getComponents().add(group);

                // Switch result to new group.
                rslt = groupScope;
            }
        }

        // Return scope to which to add the remaining contents of the file.
        return rslt;
    }

    /**
     * Builds a symbol scope for a component (automaton or group), and adds it to the parent symbol scope.
     *
     * @param component The CIF AST component to build the symbol scope for.
     * @param parent The parent symbol scope to which to add the component.
     */
    private void addComponent(ACompDecl component, ParentScope<?> parent) {
        if (component.body instanceof AAutomatonBody) {
            addAutomaton(component, parent);
        } else {
            addGroup(component, parent);
        }
    }

    /**
     * Builds a symbol scope for a component definition (automaton definition or group definition), and adds it to the
     * parent symbol scope.
     *
     * @param compdef The CIF AST component definition to build the symbol scope for.
     * @param parent The parent symbol scope to which to add the component definition.
     */
    private void addCompDef(ACompDefDecl compdef, ParentScope<?> parent) {
        if (compdef.body instanceof AAutomatonBody) {
            addAutomaton(compdef, parent);
        } else {
            addGroup(compdef, parent);
        }
    }

    /**
     * Builds a symbol scope for a group (definition), and adds it to the parent symbol scope.
     *
     * @param decl The CIF AST group (definition) to build the symbol scope for. Must be an {@link ACompDecl} or an
     *     {@link ACompDefDecl}.
     * @param parent The parent symbol scope to which to add the group (definition).
     */
    private void addGroup(ADecl decl, ParentScope<?> parent) {
        // Construct scope.
        ParentScope<?> scope;
        List<ADecl> decls;

        if (decl instanceof ACompDecl) {
            // Construct object.
            ACompDecl group1 = (ACompDecl)decl;
            Group group2 = newGroup();
            group2.setName(group1.name.id);
            group2.setPosition(group1.createPosition());
            Assert.check(group1.kind == null);

            // Construct scope and store elements for later.
            scope = new GroupScope(group2, parent, tchecker);
            decls = group1.body.decls;

            // Add annotations.
            scope.astAnnotations.addAll(group1.annotations);

            // Add group to the parent object.
            parent.getGroup().getComponents().add(group2);
        } else {
            // Construct component definition.
            ACompDefDecl compdef1 = (ACompDefDecl)decl;
            ComponentDef compdef2 = newComponentDef();
            compdef2.setPosition(compdef1.createPosition());

            // Construct body.
            Group body = newGroup();
            body.setName(compdef1.name.id);
            body.setPosition(compdef1.createPosition());
            Assert.check(compdef1.kind == null);
            compdef2.setBody(body);

            // Construct scope and store elements for later.
            scope = new GroupDefScope(compdef2, parent, tchecker);
            decls = compdef1.body.decls;

            // Add annotations.
            scope.astAnnotations.addAll(compdef1.annotations);

            // Add group definition to the parent object.
            parent.getGroup().getDefinitions().add(compdef2);

            // Add parameters.
            addParameters(compdef1.parameters, scope);
        }

        // Fill scope.
        for (ADecl d: decls) {
            addGroupDecl(d, scope, scope);
        }
    }

    /**
     * Builds a symbol scope for an automaton (definition), and adds it to the parent symbol scope.
     *
     * @param decl The CIF AST automaton (definition) to build the symbol scope for. Must be an {@link ACompDecl} or an
     *     {@link ACompDefDecl}.
     * @param parent The parent symbol scope to which to add the automaton (definition).
     */
    private void addAutomaton(ADecl decl, ParentScope<?> parent) {
        // Construct scope.
        ParentScope<?> scope;
        List<ADecl> decls;
        List<ALocation> locs;

        if (decl instanceof ACompDecl) {
            // Construct object.
            ACompDecl aut1 = (ACompDecl)decl;
            Automaton aut2 = newAutomaton();
            aut2.setName(aut1.name.id);
            aut2.setPosition(aut1.createPosition());
            aut2.setKind(transAutKind(aut1.kind));

            // Construct scope and store elements for later.
            scope = new AutScope(aut2, aut1, parent, tchecker);
            decls = aut1.body.decls;
            locs = ((AAutomatonBody)aut1.body).locations;

            // Add annotations.
            scope.astAnnotations.addAll(aut1.annotations);

            // Add automaton to the parent object.
            parent.getGroup().getComponents().add(aut2);
        } else {
            // Construct component definition.
            ACompDefDecl compdef1 = (ACompDefDecl)decl;
            ComponentDef compdef2 = newComponentDef();
            compdef2.setPosition(compdef1.createPosition());

            // Construct body.
            Automaton body = newAutomaton();
            body.setName(compdef1.name.id);
            body.setPosition(compdef1.createPosition());
            body.setKind(transAutKind(compdef1.kind));
            compdef2.setBody(body);

            // Construct scope and store elements for later.
            scope = new AutDefScope(compdef2, compdef1, parent, tchecker);
            decls = compdef1.body.decls;
            locs = ((AAutomatonBody)compdef1.body).locations;

            // Add annotations.
            scope.astAnnotations.addAll(compdef1.annotations);

            // Add automaton definition to the parent object.
            parent.getGroup().getDefinitions().add(compdef2);

            // Add parameters.
            addParameters(compdef1.parameters, scope);
        }

        // Fill scope.
        for (ADecl d: decls) {
            addAutDecl(d, scope);
        }
        for (ALocation loc: locs) {
            addLocation(loc, scope);
        }
    }

    /**
     * Transforms a CIF AST automaton supervisory kind token to a CIF metamodel automaton supervisory kind.
     *
     * @param kind The CIF AST automaton supervisory kind token.
     * @return The CIF metamodel automaton supervisory kind.
     */
    private SupKind transAutKind(Token kind) {
        if (kind == null) {
            return SupKind.NONE;
        }
        return SupKind.valueOf(kind.text.toUpperCase(Locale.US));
    }

    /**
     * Builds a symbol scope for a component instantiation, and adds it to the parent symbol scope.
     *
     * @param compinst The CIF AST component instantiation to build the symbol scope for.
     * @param parent The parent symbol scope to which to add the component instantiation.
     */
    @SuppressWarnings("unused")
    private void addCompInst(ACompInstDecl compinst, ParentScope<?> parent) {
        // Construct object.
        ComponentInst obj = newComponentInst();
        obj.setName(compinst.instName.id);
        obj.setPosition(compinst.createPosition());

        // Construct scope.
        new CompInstScope(obj, compinst, parent, tchecker);

        // Add component instantiation to the parent object.
        parent.getGroup().getComponents().add(obj);
    }

    /**
     * Adds a declaration to the given parent symbol scope.
     *
     * @param decl The declaration to add.
     * @param parent The parent symbol scope to which to add the declaration.
     */
    private void addDecl(ADecl decl, ParentScope<?> parent) {
        if (decl instanceof AAlgVariableDecl algVarDecl) {
            addAlgVars(algVarDecl, parent);
        } else if (decl instanceof AConstDecl constDecl) {
            addConstants(constDecl, parent);
        } else if (decl instanceof AContVariableDecl contVarDecl) {
            addContVars(contVarDecl, parent);
        } else if (decl instanceof AEventDecl eventDecl) {
            addEvents(eventDecl, parent);
        } else if (decl instanceof AEnumDecl enumDecl) {
            addEnum(enumDecl, parent);
        } else if (decl instanceof ATypeDefDecl typeDefDecl) {
            addTypeDecls(typeDefDecl, parent);
        } else if (decl instanceof AInputVariableDecl inputVarDecl) {
            addInputVars(inputVarDecl, parent);
        } else if (decl instanceof AIoDecl ioDecl) {
            parent.astIoDecls.add(ioDecl);
        } else if (decl instanceof AInitialDecl initDecl) {
            parent.astInitPreds.add(initDecl);
        } else if (decl instanceof AInvariantDecl invDecl) {
            addInvariants(invDecl, parent, parent.getComplexComponent().getInvariants());
        } else if (decl instanceof AMarkedDecl markedDecl) {
            parent.astMarkerPreds.add(markedDecl);
        } else if (decl instanceof AEquationDecl equationDecl) {
            parent.addEquations(equationDecl.equations);
        } else {
            throw new RuntimeException("Unknown/unexpected decl: " + decl);
        }
    }

    /**
     * Adds a group declaration to the given parent symbol scope.
     *
     * @param decl The group declaration to add.
     * @param sourceParent The parent symbol scope in which the group declaration is declared. For namespaces, the
     *     'source parent scope' is the specification, and the 'add parent scope' is the inner most group of the
     *     namespace. For other uses, the source and add parent scopes are identical.
     * @param addParent The parent symbol scope to which to add the group declaration.
     */
    private void addGroupDecl(ADecl decl, ParentScope<?> sourceParent, ParentScope<?> addParent) {
        // Paranoia checking.
        if (sourceParent instanceof SpecScope) {
            Assert.check(sourceParent == addParent || addParent instanceof GroupScope);
        } else {
            Assert.check(sourceParent == addParent);
        }

        // Actual work based on type of declaration.
        if (decl instanceof ANamespaceDecl) {
            // Already processed for the specification. Not allowed elsewhere.
            if (!(sourceParent instanceof SpecScope)) {
                tchecker.addProblem(ErrMsg.NAMESPACE_IN_GRP, decl.position);
                // Non-fatal problem.
            }
        } else if (decl instanceof ACompDecl) {
            addComponent((ACompDecl)decl, addParent);
        } else if (decl instanceof ACompDefDecl) {
            addCompDef((ACompDefDecl)decl, addParent);
        } else if (decl instanceof ACompInstDecl) {
            addCompInst((ACompInstDecl)decl, addParent);
        } else if (decl instanceof AFuncDecl) {
            addFunction((AFuncDecl)decl, addParent);
        } else if (decl instanceof AImportDecl) {
            // Only allowed in the specification.
            if (!(sourceParent instanceof SpecScope)) {
                tchecker.addProblem(ErrMsg.IMPORT_IN_GRP, decl.position);
                // Non-fatal problem.
                return;
            }

            // Add import to the specification.
            Assert.check(sourceParent instanceof SpecScope);
            addImports((AImportDecl)decl);
        } else {
            addDecl(decl, addParent);
        }
    }

    /**
     * Adds an automaton declaration to the given parent symbol scope.
     *
     * @param decl The automaton declaration to add.
     * @param parent The parent symbol scope to which to add the automaton declaration.
     */
    private void addAutDecl(ADecl decl, ParentScope<?> parent) {
        if (decl instanceof AAlphabetDecl) {
            // Skip (nameless set of events).
        } else if (decl instanceof AMonitorDecl) {
            // Skip (nameless set of events).
        } else if (decl instanceof ALocation) {
            addLocation((ALocation)decl, parent);
        } else if (decl instanceof ADiscVariableDecl) {
            addDiscVariables((ADiscVariableDecl)decl, parent);
        } else {
            addDecl(decl, parent);
        }
    }

    /**
     * Adds a location to the given parent symbol scope.
     *
     * @param loc The location to add.
     * @param parent The parent symbol scope to which to add the location.
     */
    private void addLocation(ALocation loc, ParentScope<?> parent) {
        Assert.check(parent instanceof AutScope || parent instanceof AutDefScope);

        // Add location to the automaton scope.
        Location loc2 = newLocation();
        if (loc.name != null) {
            loc2.setName(loc.name.id);
            loc2.setPosition(loc.name.createPosition());
        } else {
            loc2.setPosition(loc.createPosition());
        }

        // Add location to parent object.
        parent.getAutomaton().getLocations().add(loc2);

        // Add invariants and equations from the location to the automaton scope.
        if (loc.elements != null) {
            for (ALocationElement lelem: loc.elements) {
                if (lelem instanceof AEquationLocationElement) {
                    List<AEquation> eqns;
                    eqns = ((AEquationLocationElement)lelem).equations;
                    parent.addEquations(eqns);
                } else if (lelem instanceof AInvariantLocationElement invLocElem) {
                    addInvariants(invLocElem.invariantDecl, parent, loc2.getInvariants());
                }
            }
        }

        // Add named locations to the symbol table.
        if (loc.name != null) {
            LocationDeclWrap wrapper = new LocationDeclWrap(tchecker, parent, loc, loc2);
            parent.addDeclaration(wrapper);
        }
    }

    /**
     * Adds algebraic variable declarations to the given parent symbol scope.
     *
     * @param decls The declarations to add.
     * @param parent The parent symbol scope to which to add the declarations.
     */
    private void addAlgVars(AAlgVariableDecl decls, ParentScope<?> parent) {
        for (AAlgVariable var1: decls.variables) {
            AlgVariable var2 = newAlgVariable();
            var2.setName(var1.name.id);
            var2.setPosition(var1.createPosition());

            AlgVariableDeclWrap wrapper = new AlgVariableDeclWrap(tchecker, parent, decls, var1, var2);
            parent.addDeclaration(wrapper);

            parent.getComplexComponent().getDeclarations().add(var2);
        }
    }

    /**
     * Adds input variable declarations to the given parent symbol scope.
     *
     * @param decls The declarations to add.
     * @param parent The parent symbol scope to which to add the declarations.
     */
    private void addInputVars(AInputVariableDecl decls, ParentScope<?> parent) {
        for (AIdentifier var1: decls.names) {
            InputVariable var2 = newInputVariable();
            var2.setName(var1.id);
            var2.setPosition(var1.createPosition());

            InputVariableDeclWrap wrapper = new InputVariableDeclWrap(tchecker, parent, decls, var2);
            parent.addDeclaration(wrapper);

            parent.getComplexComponent().getDeclarations().add(var2);
        }
    }

    /**
     * Adds continuous variable declarations to the given parent symbol scope.
     *
     * @param decls The declarations to add.
     * @param parent The parent symbol scope to which to add the declarations.
     */
    private void addContVars(AContVariableDecl decls, ParentScope<?> parent) {
        for (AContVariable var1: decls.variables) {
            ContVariable var2 = newContVariable();
            var2.setName(var1.name.id);
            var2.setPosition(var1.createPosition());

            ContVariableDeclWrap wrapper = new ContVariableDeclWrap(tchecker, parent, decls, var1, var2);
            parent.addDeclaration(wrapper);

            parent.getComplexComponent().getDeclarations().add(var2);
        }
    }

    /**
     * Adds constant declarations to the given parent symbol scope.
     *
     * @param decls The declarations to add.
     * @param parent The parent symbol scope to which to add the declarations.
     */
    private void addConstants(AConstDecl decls, ParentScope<?> parent) {
        for (AConstant const1: decls.constants) {
            Constant const2 = newConstant();
            const2.setName(const1.name.id);
            const2.setPosition(const1.createPosition());

            ConstDeclWrap wrapper = new ConstDeclWrap(tchecker, parent, decls, const1, const2);
            parent.addDeclaration(wrapper);

            parent.getComplexComponent().getDeclarations().add(const2);
        }
    }

    /**
     * Adds event declarations to the given parent symbol scope.
     *
     * @param decls The declarations to add.
     * @param parent The parent symbol scope to which to add the declarations.
     */
    private void addEvents(AEventDecl decls, ParentScope<?> parent) {
        Boolean controllable = null;
        if (decls.controllability != null) {
            controllable = decls.controllability.text.equals("controllable");
        }

        for (AIdentifier event1: decls.names) {
            Event event2 = newEvent();
            event2.setName(event1.id);
            event2.setPosition(event1.createPosition());
            event2.setControllable(controllable);

            EventDeclWrap wrapper = new EventDeclWrap(tchecker, parent, decls, event2);
            parent.addDeclaration(wrapper);

            parent.getComplexComponent().getDeclarations().add(event2);
        }
    }

    /**
     * Adds an enumeration declaration to the given parent symbol scope.
     *
     * @param astDecl The declaration to add.
     * @param parent The parent symbol scope to which to add the declaration.
     */
    private void addEnum(AEnumDecl astDecl, ParentScope<?> parent) {
        // Add enumeration declaration.
        EnumDecl mmDecl = newEnumDecl();
        mmDecl.setName(astDecl.name);
        mmDecl.setPosition(astDecl.createPosition());

        EnumDeclWrap wrapper = new EnumDeclWrap(tchecker, parent, astDecl, mmDecl);
        parent.addDeclaration(wrapper);

        parent.getComplexComponent().getDeclarations().add(mmDecl);

        // Add enumeration literals to same parent as enumeration declaration.
        List<EnumLiteral> mmLiterals = mmDecl.getLiterals();
        for (AAnnotatedIdentifier astLiteral: astDecl.literals) {
            EnumLiteral mmLiteral = newEnumLiteral();
            mmLiteral.setName(astLiteral.id);
            mmLiteral.setPosition(astLiteral.createPosition());

            EnumLiteralDeclWrap literalWrapper = new EnumLiteralDeclWrap(tchecker, parent, astLiteral, mmLiteral);
            parent.addDeclaration(literalWrapper);

            mmLiterals.add(mmLiteral);
        }
    }

    /**
     * Adds type declarations to the given parent symbol scope.
     *
     * @param decls The declarations to add.
     * @param parent The parent symbol scope to which to add the declarations.
     */
    private void addTypeDecls(ATypeDefDecl decls, ParentScope<?> parent) {
        for (ATypeDef tdef: decls.typeDefs) {
            TypeDecl tdecl = newTypeDecl();
            tdecl.setName(tdef.name.id);
            tdecl.setPosition(tdef.createPosition());

            TypeDeclWrap wrapper = new TypeDeclWrap(tchecker, parent, decls, tdef, tdecl);
            parent.addDeclaration(wrapper);

            parent.getComplexComponent().getDeclarations().add(tdecl);
        }
    }

    /**
     * Adds discrete variable declarations to the given parent symbol scope. This method must not be used to add local
     * variables to functions, or to add function parameters to functions.
     *
     * @param decls The declarations to add.
     * @param parent The parent symbol scope to which to add the declarations.
     */
    private void addDiscVariables(ADiscVariableDecl decls, ParentScope<?> parent) {
        Assert.check(parent instanceof AutScope || parent instanceof AutDefScope);
        for (ADiscVariable var1: decls.variables) {
            DiscVariable var2 = newDiscVariable();
            var2.setName(var1.name.id);
            var2.setPosition(var1.createPosition());

            DiscVariableDeclWrap wrapper = new DiscVariableDeclWrap(tchecker, parent, decls, var1, var2);
            parent.addDeclaration(wrapper);

            parent.getComplexComponent().getDeclarations().add(var2);
        }
    }

    /**
     * Add invariants to the given parent symbol scope.
     *
     * @param invariantDecl The invariants to add.
     * @param parent The parent symbol scope to which to add the invariants.
     * @param mmInvs The list to which to add the metamodel invariants. Can be the invariants in a component or in a
     *     location.
     */
    private void addInvariants(AInvariantDecl invariantDecl, ParentScope<?> parent, EList<Invariant> mmInvs) {
        // Process supervisory kind of the invariant.
        SupKind supKind = transInvSupKind(invariantDecl.kind);

        // Add the individual invariants.
        for (AInvariant astInv: invariantDecl.invariants) {
            // Get the invariant name.
            AIdentifier invName = astInv.name;

            // Process invariant kind.
            InvKind invKind = transInvKind(astInv.invKind);

            // Consider (multiple) event references.
            int numberOfInvariants;
            if (invKind == InvKind.STATE) {
                // State (exclusion) invariant, so no event reference.
                numberOfInvariants = 1;
            } else {
                // One or more event references.
                numberOfInvariants = astInv.events.size();

                // Syntax does not allow an invariant with multiple event references to be named. This assures there
                // won't be metamodel invariants with the same name.
                Assert.implies(numberOfInvariants > 1, invName == null);
            }

            // Create and add invariants.
            for (int i = 0; i < numberOfInvariants; i++) {
                // Get position information.
                Position invPos = invName != null ? invName.createPosition() : invariantDecl.createPosition();

                // Create invariant.
                Invariant mmInv = newInvariant();
                if (invName != null) {
                    mmInv.setName(invName.id);
                }
                mmInv.setInvKind(invKind);
                mmInv.setSupKind(supKind);
                mmInv.setPosition(invPos);

                // Add invariant to metamodel.
                mmInvs.add(mmInv);

                // Add invariant type check information to the parent scope, to be checked later.
                AName event = astInv.events == null ? null : astInv.events.get(i);
                InvariantInfo invariantInfo = new InvariantInfo(invariantDecl, astInv, event, mmInv);
                parent.addInvariant(invariantInfo);
            }
        }
    }

    /**
     * Adds parameters of a component definition to the given parent symbol scope.
     *
     * @param params The parameters to add.
     * @param parent The parent symbol scope to which to add the parameters.
     */
    private void addParameters(List<AParameter> params, ParentScope<?> parent) {
        for (AParameter param: params) {
            if (param instanceof AAlgParameter) {
                addAlgParameters((AAlgParameter)param, parent);
            } else if (param instanceof AComponentParameter) {
                addComponentParameters((AComponentParameter)param, parent);
            } else if (param instanceof AEventParameter) {
                addEventParameters((AEventParameter)param, parent);
            } else if (param instanceof ALocationParameter) {
                addLocationParameters((ALocationParameter)param, parent);
            } else {
                throw new RuntimeException("Unknown param: " + param);
            }
        }
    }

    /**
     * Adds algebraic parameters to the given parent symbol scope.
     *
     * @param params The parameters to add.
     * @param parent The parent symbol scope to which to add the parameters.
     */
    private void addAlgParameters(AAlgParameter params, ParentScope<?> parent) {
        for (AIdentifier id: params.names) {
            AlgVariable var = newAlgVariable();
            var.setName(id.id);
            var.setPosition(id.createPosition());

            AlgParameter algParam = newAlgParameter();
            algParam.setVariable(var);
            algParam.setPosition(id.createPosition());

            AlgParamDeclWrap wrapper = new AlgParamDeclWrap(tchecker, parent, params, algParam);
            parent.addDeclaration(wrapper);

            parent.getComponentDef().getParameters().add(algParam);
        }
    }

    /**
     * Adds component parameters to the given parent symbol scope.
     *
     * @param params The parameters to add.
     * @param parent The parent symbol scope to which to add the parameters.
     */
    @SuppressWarnings("unused")
    private void addComponentParameters(AComponentParameter params, ParentScope<?> parent) {
        for (AIdentifier id: params.names) {
            // Construct object.
            ComponentParameter compParam = newComponentParameter();
            compParam.setName(id.id);
            compParam.setPosition(id.createPosition());

            // Construct scope.
            new CompParamScope(compParam, params.type, parent, tchecker);

            // Add parameter to component definition.
            parent.getComponentDef().getParameters().add(compParam);
        }
    }

    /**
     * Adds event parameters to the given parent symbol scope.
     *
     * @param params The parameters to add.
     * @param parent The parent symbol scope to which to add the parameters.
     */
    private void addEventParameters(AEventParameter params, ParentScope<?> parent) {
        Boolean controllable = null;
        if (params.controllability != null) {
            controllable = params.controllability.text.equals("controllable");
        }

        for (AEventParameterPart part: params.parts) {
            // Create event declaration.
            Event event = newEvent();
            event.setName(part.name.id);
            event.setPosition(part.name.createPosition());
            event.setControllable(controllable);

            // Create event parameter.
            EventParameter eventParam = newEventParameter();
            eventParam.setEvent(event);
            eventParam.setPosition(part.name.createPosition());

            // Create symbol table entry.
            EventParamDeclWrap wrapper = new EventParamDeclWrap(tchecker, parent, params, part, eventParam);

            // Add to symbol table and metamodel.
            parent.addDeclaration(wrapper);
            parent.getComponentDef().getParameters().add(eventParam);
        }
    }

    /**
     * Adds location parameters to the given parent symbol scope.
     *
     * @param params The parameters to add.
     * @param parent The parent symbol scope to which to add the parameters.
     */
    private void addLocationParameters(ALocationParameter params, ParentScope<?> parent) {
        for (AIdentifier id: params.names) {
            Location loc = newLocation();
            loc.setName(id.id);
            loc.setPosition(id.createPosition());

            LocationParameter locParam = newLocationParameter();
            locParam.setLocation(loc);
            locParam.setPosition(id.createPosition());

            LocationParamDeclWrap wrapper = new LocationParamDeclWrap(tchecker, parent, locParam);
            parent.addDeclaration(wrapper);

            parent.getComponentDef().getParameters().add(locParam);
        }
    }

    /**
     * Builds a symbol scope for a function (definition), and adds it to the parent symbol scope.
     *
     * @param func The function to add.
     * @param parent The parent symbol scope to which to add the function.
     */
    private void addFunction(AFuncDecl func, ParentScope<?> parent) {
        if (func.body instanceof AInternalFuncBody) {
            AInternalFuncBody body = (AInternalFuncBody)func.body;

            // Construct function object.
            InternalFunction ifunc = newInternalFunction();
            ifunc.setName(func.name.id);
            ifunc.setPosition(func.createPosition());

            // Construct scope.
            FunctionScope scope = new FunctionScope(ifunc, func, parent, tchecker);

            // Add function to the parent object.
            parent.getComplexComponent().getDeclarations().add(ifunc);

            // Add parameters.
            for (AFuncParam param: func.parameters) {
                addFunctionParams(param, scope);
            }

            // Add local variables.
            for (ADiscVariableDecl var: body.variables) {
                addFunctionLocalVars(var, scope);
            }
        } else {
            Assert.check(func.body instanceof AExternalFuncBody);
            AExternalFuncBody body = (AExternalFuncBody)func.body;

            // Construct function object.
            ExternalFunction efunc = newExternalFunction();
            efunc.setName(func.name.id);
            efunc.setPosition(func.createPosition());

            // Add external function reference. We need this to be available,
            // to be able to correct paths for imports, in the symbol scope
            // merger.
            efunc.setFunction(body.functionRef);

            // Construct scope.
            FunctionScope scope = new FunctionScope(efunc, func, parent, tchecker);

            // Add function to the parent object.
            parent.getComplexComponent().getDeclarations().add(efunc);

            // Add parameters.
            for (AFuncParam param: func.parameters) {
                addFunctionParams(param, scope);
            }
        }
    }

    /**
     * Adds function parameters to the given parent function scope.
     *
     * @param params The parameters to add.
     * @param parent The parent function scope to which to add the parameters.
     */
    private void addFunctionParams(AFuncParam params, FunctionScope parent) {
        for (AIdentifier id: params.names) {
            DiscVariable var = newDiscVariable();
            var.setName(id.id);
            var.setPosition(id.createPosition());

            FunctionParameter param = newFunctionParameter();
            param.setPosition(id.createPosition());
            param.setParameter(var);

            FuncParamDeclWrap wrapper = new FuncParamDeclWrap(tchecker, parent, params, param);
            parent.addDeclaration(wrapper);

            parent.getObject().getParameters().add(param);
        }
    }

    /**
     * Adds local variable declarations to the given parent function scope.
     *
     * @param vars The variables to add.
     * @param parent The parent function scope to which to add the variables.
     */
    private void addFunctionLocalVars(ADiscVariableDecl vars, FunctionScope parent) {
        for (ADiscVariable var1: vars.variables) {
            DiscVariable var2 = newDiscVariable();
            var2.setName(var1.name.id);
            var2.setPosition(var1.createPosition());

            FuncVariableDeclWrap wrapper = new FuncVariableDeclWrap(tchecker, parent, vars, var1, var2);
            parent.addDeclaration(wrapper);

            InternalFunction ifunc = (InternalFunction)parent.getObject();
            ifunc.getVariables().add(var2);
        }
    }

    /**
     * Checks the imports, and adds them to the queue for further processing.
     *
     * @param imports The imports to process.
     */
    private void addImports(AImportDecl imports) {
        for (AImport imp: imports.imports) {
            addImport(imp);
        }
    }

    /**
     * Checks the import, and adds it to the queue for further processing.
     *
     * @param imp The import to process.
     */
    private void addImport(AImport imp) {
        // Get absolute path for the import.
        String impPath = imp.source.txt;
        String impAbsPath = sourceFile.resolve(impPath);

        // Create new source file metadata.
        TextPosition problemPos = sourceFile.main ? imp.position : sourceFile.problemPos;
        SourceFile newSourceFile = new SourceFile(impAbsPath, false, imp.position, problemPos);

        // Check for self-import.
        if (impAbsPath.equals(sourceFile.absPath)) {
            tchecker.addProblem(ErrMsg.IMPORT_SELF, imp.position);
            // Non-fatal problem.
        }

        // Check for duplicate import in a single file.
        TextPosition prevPos = imports.put(impAbsPath, imp.position);
        if (prevPos != null) {
            String relPath = newSourceFile.getRelativePathFrom(mainFile);

            tchecker.addProblem(ErrMsg.DUPL_IMPORT, prevPos, relPath);
            tchecker.addProblem(ErrMsg.DUPL_IMPORT, imp.position, relPath);
            // Non-fatal problem.
        }

        // If source file encountered before, don't process it again.
        SourceFile prevSourceFile = tchecker.sourceFiles.get(impAbsPath);
        if (prevSourceFile != null) {
            return;
        }

        // New source file. Add it to the queue for further processing.
        tchecker.sourceFiles.add(newSourceFile);
    }

    /**
     * Transforms a CIF AST invariant kind token to a CIF metamodel invariant kind.
     *
     * @param kind The CIF AST invariant kind. Is either a state/event exclusion kind token, or {@code null} for state
     *     invariants.
     * @return The CIF metamodel invariant kind.
     */
    private static InvKind transInvKind(Token kind) {
        if (kind == null) {
            return InvKind.STATE;
        }
        return InvKind.valueOf("EVENT_" + kind.text.toUpperCase(Locale.US));
    }

    /**
     * Transforms a CIF AST invariant supervisory kind token to a CIF metamodel invariant supervisory kind.
     *
     * @param kind The CIF AST invariant supervisory kind token, or {@code null} if not specified.
     * @return The CIF metamodel invariant supervisory kind.
     */
    private static SupKind transInvSupKind(Token kind) {
        if (kind == null) {
            return SupKind.NONE;
        }
        return SupKind.valueOf(kind.text.toUpperCase(Locale.US));
    }
}
