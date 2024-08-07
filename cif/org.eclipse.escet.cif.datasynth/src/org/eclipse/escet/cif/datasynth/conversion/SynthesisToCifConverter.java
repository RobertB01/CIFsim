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

package org.eclipse.escet.cif.datasynth.conversion;

import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newAlgVariable;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newAlgVariableExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newAlphabet;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newAssignmentFuncStatement;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newAutomaton;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBinaryExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBoolType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newConstant;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newConstantExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newDiscVariable;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newDiscVariableExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEdge;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEdgeEvent;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEventExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newField;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newFieldExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newFunctionCallExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newFunctionExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newFunctionParameter;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newGroup;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newIfExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newIntType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newInternalFunction;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newListExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newListType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newLocation;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newProjectionExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newReturnFuncStatement;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newSpecification;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newTupleExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newTupleType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newTypeDecl;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newTypeRef;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newWhileFuncStatement;
import static org.eclipse.escet.common.emf.EMFHelper.deepclone;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Maps.mapc;
import static org.eclipse.escet.common.java.Sets.setc;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.str;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.escet.cif.bdd.conversion.BddToCif;
import org.eclipse.escet.cif.bdd.conversion.CifToBddConverter;
import org.eclipse.escet.cif.bdd.spec.CifBddSpec;
import org.eclipse.escet.cif.bdd.spec.CifBddVariable;
import org.eclipse.escet.cif.cif2cif.CifToCifPreconditionException;
import org.eclipse.escet.cif.cif2cif.RemoveRequirements;
import org.eclipse.escet.cif.common.CifControllerPropertiesAnnotationUtils;
import org.eclipse.escet.cif.common.CifScopeUtils;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.CifValidationUtils;
import org.eclipse.escet.cif.common.CifValueUtils;
import org.eclipse.escet.cif.datasynth.CifDataSynthesisResult;
import org.eclipse.escet.cif.datasynth.settings.BddOutputMode;
import org.eclipse.escet.cif.datasynth.settings.BddSimplify;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.SupKind;
import org.eclipse.escet.cif.metamodel.cif.automata.Alphabet;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Constant;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.declarations.TypeDecl;
import org.eclipse.escet.cif.metamodel.cif.expressions.AlgVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.ConstantExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.FieldExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.FunctionCallExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.FunctionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IfExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ListExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ProjectionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TupleExpression;
import org.eclipse.escet.cif.metamodel.cif.functions.AssignmentFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.FunctionParameter;
import org.eclipse.escet.cif.metamodel.cif.functions.InternalFunction;
import org.eclipse.escet.cif.metamodel.cif.functions.ReturnFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.WhileFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.Field;
import org.eclipse.escet.cif.metamodel.cif.types.ListType;
import org.eclipse.escet.cif.metamodel.cif.types.TupleType;
import org.eclipse.escet.common.emf.EMFHelper;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Sets;
import org.eclipse.escet.common.java.Strings;
import org.eclipse.escet.common.java.exceptions.InvalidOptionException;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

import com.github.javabdd.BDD;

/** Converter to convert synthesis result back to CIF. */
public class SynthesisToCifConverter {
    /** The CIF/BDD specification on which synthesis was performed, or {@code null} if not available. */
    private CifBddSpec cifBddSpec;

    /** The input CIF specification, or {@code null} if not available. May be modified in-place. */
    private Specification spec;

    /** The new CIF supervisor automaton, or {@code null} if not available. */
    private Automaton supervisor;

    /** The BDD output mode, or {@code null} if not available. */
    private BddOutputMode outputMode;

    /** The prefix to use for BDD related names in the output, or {@code null} if not available. */
    private String bddNamePrefix;

    /**
     * Mapping from BDD nodes to their array indices, or {@code null} if not available. The zero/one nodes are not
     * included.
     */
    private Map<BDD, Integer> bddNodeMap;

    /**
     * Mapping from BDD variable indices to the indices of the algebraic variables created to represent them. Is
     * {@code null} if not available. Only 'old' variable indices are included; 'new' variable indices are omitted.
     */
    private Map<Integer, Integer> bddVarIdxMap;

    /** BDD nodes type, or {@code null} if not available. */
    private ListType bddNodesType;

    /** BDD nodes constant, or {@code null} if not available. */
    private Constant bddNodesConst;

    /** BDD values variable, or {@code null} if not available. */
    private AlgVariable bddValuesVar;

    /** BDD evaluation function, or {@code null} if not available. */
    private InternalFunction bddEvalFunc;

    /**
     * Converts a synthesis result back to CIF. The original CIF specification is extended with an external supervisor,
     * to obtain the controlled system.
     *
     * @param synthResult The synthesis result.
     * @param spec The input CIF specification. Is modified in-place.
     * @return The output CIF specification, i.e. the modified input CIF specification.
     */
    public Specification convert(CifDataSynthesisResult synthResult, Specification spec) {
        // Initialization.
        this.cifBddSpec = synthResult.cifBddSpec;
        this.spec = spec;
        this.supervisor = null;
        this.outputMode = synthResult.settings.getBddOutputMode();
        this.bddNamePrefix = synthResult.settings.getBddOutputNamePrefix();
        this.bddNodeMap = null;
        this.bddVarIdxMap = null;
        this.bddNodesConst = null;
        this.bddValuesVar = null;
        this.bddEvalFunc = null;

        // Remove temporary events created for input variables.
        for (Event event: cifBddSpec.inputVarEvents) {
            EMFHelper.removeFromParentContainment(event);
            cifBddSpec.alphabet.remove(event);
        }

        // Relabel requirement automata from input model to supervisors.
        relabelRequirementAutomata(spec);

        // Remove requirements.
        //
        // Note that requirement automata have already been relabeled as
        // supervisors, and are thus not removed.
        //
        // Whether it is allowed to remove the requirements depends on the BDD
        // predicate simplification setting.
        try {
            // If we simplify against something, the 'something' needs to
            // remain to ensure we don't loose that restriction.
            EnumSet<BddSimplify> simplifications = synthResult.settings.getBddSimplifications();
            RemoveRequirements remover = new RemoveRequirements();
            remover.removeReqAuts = true;
            remover.removeStateEvtExclReqInvs = !simplifications.contains(BddSimplify.GUARDS_SE_EXCL_REQ_INVS);
            remover.removeStateReqInvs = !simplifications.contains(BddSimplify.GUARDS_STATE_REQ_INVS);
            remover.transform(spec);
        } catch (CifToCifPreconditionException ex) {
            // Unexpected, as we do not have requirement automata, and it is impossible to refer to requirement
            // invariants.
            throw new RuntimeException("Unexpected error.", ex);
        }

        // Relabel the remaining requirement invariants from the input model to supervisors.
        relabelRequirementInvariants(spec);

        // Construct new supervisor automaton.
        supervisor = createSupervisorAutomaton(synthResult.settings.getSupervisorName());

        // Add the alphabet to the automaton. Only add controllable events, as
        // they may be restricted by the supervisor.
        Alphabet alphabet = newAlphabet();
        for (Event event: cifBddSpec.alphabet) {
            if (!event.getControllable()) {
                continue;
            }

            EventExpression eventRef = newEventExpression(event, null, newBoolType());
            alphabet.getEvents().add(eventRef);
        }
        supervisor.setAlphabet(alphabet);

        // Add single nameless location, that is both initial and marked.
        Location cifLoc = newLocation();
        cifLoc.getInitials().add(CifValueUtils.makeTrue());
        cifLoc.getMarkeds().add(CifValueUtils.makeTrue());
        supervisor.getLocations().add(cifLoc);

        // Get controllable events for which we have to add self loops.
        Set<Event> controllables = setc(alphabet.getEvents().size());
        for (Event event: cifBddSpec.alphabet) {
            if (event.getControllable()) {
                controllables.add(event);
            }
        }

        // Prepare for conversion of BDDs to CIF. No operations on BDDs are
        // allowed after this (this includes freeing BDDs), as it may lead to
        // re-allocation, breaking BDD equality/hashing and thus our internal
        // node mapping.
        prepareBddToCif();

        // Add edges for controllable events.
        List<Edge> edges = listc(controllables.size());
        for (Entry<Event, BDD> entry: synthResult.outputGuards.entrySet()) {
            Event event = entry.getKey();
            BDD guard = entry.getValue();

            // Convert the guard to CIF.
            Expression cifGuard = convertPred(guard);

            // Add self loop.
            edges.add(createSelfLoop(event, list(cifGuard)));
        }

        // Sort edges by event, in ascending alphabetical order of the names of
        // the events.
        Collections.sort(edges, new EdgeSorter());
        cifLoc.getEdges().addAll(edges);

        // Add initialization predicate, if any.
        if (synthResult.initialOutput != null) {
            Expression initialPred = convertPred(synthResult.initialOutput);
            supervisor.getInitials().add(initialPred);
        }

        // After all predicates have been converted, finalize BDD conversion.
        finalizeBddToCif();

        // Add namespace, if requested.
        if (synthResult.settings.getSupervisorNamespace() != null) {
            spec = addNamespace(synthResult.settings.getSupervisorNamespace());
            this.spec = spec;
        }

        // Remove controller properties annotation.
        CifControllerPropertiesAnnotationUtils.remove(spec);

        // Return the modified input CIF specification as output.
        return spec;
    }

    /** Prepare for the conversion of BDDs to CIF. */
    private void prepareBddToCif() {
        switch (outputMode) {
            case NORMAL, CNF, DNF:
                // Nothing to prepare.
                return;

            case NODES: {
                // Make sure there are no declarations in the root of the
                // specification with the BDD name prefix.
                Set<String> names = CifScopeUtils.getSymbolNamesForScope(spec, null);
                for (String name: names) {
                    if (name.startsWith(bddNamePrefix)) {
                        String msg = fmt("Can't create BDD output using BDD output name prefix \"%s\", as a "
                                + "declaration named \"%s\" already exists in the specification. Configure a "
                                + "different name prefix.", bddNamePrefix, name);
                        throw new InvalidOptionException(msg);
                    }
                }

                // Initialize node mapping.
                bddNodeMap = map();

                // Create node type.
                CifType nodeType0 = newIntType();
                CifType nodeType1 = newIntType();
                CifType nodeType2 = newIntType();
                Field nodeField0 = newField("var", null, nodeType0);
                Field nodeField1 = newField("low", null, nodeType1);
                Field nodeField2 = newField("high", null, nodeType2);
                List<Field> nodeFields = list(nodeField0, nodeField1, nodeField2);
                TupleType nodeType = newTupleType(nodeFields, null);

                TypeDecl nodeTypeDecl = newTypeDecl();
                nodeTypeDecl.setName(bddNamePrefix + "_node_type");
                nodeTypeDecl.setType(nodeType);
                spec.getDeclarations().add(nodeTypeDecl);

                // Create nodes type.
                bddNodesType = newListType();
                bddNodesType.setElementType(newTypeRef(null, nodeTypeDecl));

                TypeDecl nodesTypeDecl = newTypeDecl();
                nodesTypeDecl.setName(bddNamePrefix + "_nodes_type");
                nodesTypeDecl.setType(bddNodesType);
                spec.getDeclarations().add(nodesTypeDecl);

                // Create 'BDD nodes' constant.
                ListExpression nodesListExpr = newListExpression();
                nodesListExpr.setType(deepclone(bddNodesType));

                bddNodesConst = newConstant();
                bddNodesConst.setName(bddNamePrefix + "_nodes");
                bddNodesConst.setValue(nodesListExpr);
                bddNodesConst.setType(newTypeRef(null, nodesTypeDecl));
                spec.getDeclarations().add(bddNodesConst);

                // Get variables in sorted order.
                CifBddVariable[] sortedVars = cifBddSpec.variables.clone();
                Arrays.sort(sortedVars, (v, w) -> Strings.SORTER.compare(v.rawName, w.rawName));

                // Initialize BDD variable index mapping.
                int bddVarCnt = cifBddSpec.factory.varNum();
                Assert.check(bddVarCnt % 2 == 0); // #old = #new, so total is even.
                bddVarIdxMap = mapc(bddVarCnt / 2);

                // Create 'BDD variable value' algebraic variables. Fill BDD
                // variable index mapping.
                List<AlgVariable> valueVars = listc(cifBddSpec.factory.varNum());
                int cifVarIdx = 0;
                for (CifBddVariable cifBddVar: sortedVars) {
                    int[] varIdxs = cifBddVar.domain.vars();
                    for (int i = 0; i < varIdxs.length; i++) {
                        AlgVariable var = newAlgVariable();
                        int bddVarIdx = varIdxs[i];
                        Assert.check(bddVarIdx % 2 == 0); // Is an old variable.
                        var.setName(bddNamePrefix + "_value" + str(cifVarIdx));
                        var.setType(newBoolType());
                        var.setValue(BddToCif.getBddVarPred(cifBddVar, i));
                        spec.getDeclarations().add(var);
                        valueVars.add(var);
                        bddVarIdxMap.put(bddVarIdx, cifVarIdx);
                        cifVarIdx++;
                    }
                }

                // Create 'BDD variable values' algebraic variable.
                ListType valuesType = newListType();
                bddVarCnt /= 2; // Skip new variables.
                valuesType.setLower(bddVarCnt);
                valuesType.setUpper(bddVarCnt);
                valuesType.setElementType(newBoolType());

                ListExpression valuesExpr = newListExpression();
                valuesExpr.setType(valuesType);
                for (AlgVariable valueVar: valueVars) {
                    AlgVariableExpression valueVarRef = newAlgVariableExpression();
                    valueVarRef.setVariable(valueVar);
                    valueVarRef.setType(newBoolType());
                    valuesExpr.getElements().add(valueVarRef);
                }

                bddValuesVar = newAlgVariable();
                bddValuesVar.setName(bddNamePrefix + "_values");
                bddValuesVar.setType(deepclone(valuesType));
                bddValuesVar.setValue(valuesExpr);
                spec.getDeclarations().add(bddValuesVar);

                // Create BDD evaluation function.
                bddEvalFunc = newInternalFunction();
                bddEvalFunc.setName(bddNamePrefix + "_eval");
                spec.getDeclarations().add(bddEvalFunc);

                bddEvalFunc.getReturnTypes().add(newBoolType());

                DiscVariable pvar1 = newDiscVariable();
                DiscVariable pvar2 = newDiscVariable();
                pvar1.setName("idx");
                pvar2.setName("values");
                pvar1.setType(newIntType());
                pvar2.setType(deepclone(valuesType));

                FunctionParameter param1 = newFunctionParameter(pvar1, null);
                FunctionParameter param2 = newFunctionParameter(pvar2, null);
                bddEvalFunc.getParameters().add(param1);
                bddEvalFunc.getParameters().add(param2);

                DiscVariable varNode = newDiscVariable();
                DiscVariable varVal = newDiscVariable();
                varNode.setName("node");
                varVal.setName("val");
                varNode.setType(newTypeRef(null, nodeTypeDecl));
                varVal.setType(newBoolType());
                bddEvalFunc.getVariables().add(varNode);
                bddEvalFunc.getVariables().add(varVal);

                DiscVariableExpression idxRef = newDiscVariableExpression();
                idxRef.setVariable(pvar1);
                idxRef.setType(deepclone(pvar1.getType()));

                BinaryExpression whileCond = newBinaryExpression();
                whileCond.setOperator(BinaryOperator.GREATER_EQUAL);
                whileCond.setLeft(idxRef);
                whileCond.setRight(CifValueUtils.makeInt(0));
                whileCond.setType(newBoolType());

                WhileFuncStatement whileStat = newWhileFuncStatement();
                bddEvalFunc.getStatements().add(whileStat);
                whileStat.getGuards().add(whileCond);

                DiscVariableExpression nodeRef = newDiscVariableExpression();
                nodeRef.setVariable(varNode);
                nodeRef.setType(deepclone(varNode.getType()));

                ConstantExpression nodesRef = newConstantExpression();
                nodesRef.setConstant(bddNodesConst);
                nodesRef.setType(deepclone(bddNodesConst.getType()));

                ProjectionExpression nodeIdxProj = newProjectionExpression();
                nodeIdxProj.setChild(nodesRef);
                nodeIdxProj.setIndex(deepclone(idxRef));
                nodeIdxProj.setType(newTypeRef(null, nodeTypeDecl));

                AssignmentFuncStatement asgn1 = newAssignmentFuncStatement();
                asgn1.setAddressable(nodeRef);
                asgn1.setValue(nodeIdxProj);
                whileStat.getStatements().add(asgn1);

                DiscVariableExpression valRef = newDiscVariableExpression();
                valRef.setVariable(varVal);
                valRef.setType(deepclone(varVal.getType()));

                FieldExpression varFieldRef = newFieldExpression();
                varFieldRef.setField(nodeField0);
                varFieldRef.setType(newIntType(0, null, 0));

                ProjectionExpression nodeVarProj = newProjectionExpression();
                nodeVarProj.setChild(deepclone(nodeRef));
                nodeVarProj.setIndex(varFieldRef);
                nodeVarProj.setType(deepclone(nodeType0));

                DiscVariableExpression valuesRef = newDiscVariableExpression();
                valuesRef.setVariable(pvar2);
                valuesRef.setType(deepclone(pvar2.getType()));

                ProjectionExpression valuesNodeVarProj = newProjectionExpression();
                valuesNodeVarProj.setChild(valuesRef);
                valuesNodeVarProj.setIndex(nodeVarProj);
                valuesNodeVarProj.setType(newBoolType());

                AssignmentFuncStatement asgn2 = newAssignmentFuncStatement();
                asgn2.setAddressable(valRef);
                asgn2.setValue(valuesNodeVarProj);
                whileStat.getStatements().add(asgn2);

                FieldExpression lowFieldRef = newFieldExpression();
                lowFieldRef.setField(nodeField1);
                lowFieldRef.setType(newIntType(1, null, 1));

                ProjectionExpression nodeLowProj = newProjectionExpression();
                nodeLowProj.setChild(deepclone(nodeRef));
                nodeLowProj.setIndex(lowFieldRef);
                nodeLowProj.setType(deepclone(nodeType1));

                FieldExpression highFieldRef = newFieldExpression();
                highFieldRef.setField(nodeField2);
                highFieldRef.setType(newIntType(2, null, 2));

                ProjectionExpression nodeHighProj = newProjectionExpression();
                nodeHighProj.setChild(deepclone(nodeRef));
                nodeHighProj.setIndex(highFieldRef);
                nodeHighProj.setType(deepclone(nodeType2));

                IfExpression ifExpr = newIfExpression();
                ifExpr.getGuards().add(deepclone(valRef));
                ifExpr.setThen(nodeHighProj);
                ifExpr.setElse(nodeLowProj);
                ifExpr.setType(newIntType());

                AssignmentFuncStatement asgn3 = newAssignmentFuncStatement();
                asgn3.setAddressable(deepclone(idxRef));
                asgn3.setValue(ifExpr);
                whileStat.getStatements().add(asgn3);

                BinaryExpression returnValue = newBinaryExpression();
                returnValue.setOperator(BinaryOperator.EQUAL);
                returnValue.setLeft(deepclone(idxRef));
                returnValue.setRight(CifValueUtils.makeInt(-1));
                returnValue.setType(newBoolType());

                ReturnFuncStatement returnStat = newReturnFuncStatement();
                bddEvalFunc.getStatements().add(returnStat);
                returnStat.getValues().add(returnValue);

                return;
            }
        }

        throw new RuntimeException("Unknown output mode: " + outputMode);
    }

    /** Finalize the conversion of BDDs to CIF. */
    private void finalizeBddToCif() {
        switch (outputMode) {
            case NORMAL, CNF, DNF:
                // Nothing to finalize.
                return;

            case NODES:
                bddNodesType.setLower(bddNodeMap.size());
                bddNodesType.setUpper(bddNodeMap.size());
                return;
        }

        throw new RuntimeException("Unknown output mode: " + outputMode);
    }

    /**
     * Converts a BDD/predicate to CIF. May modify {@link #spec}.
     *
     * @param bdd The BDD to convert.
     * @return The expression to use in the CIF model to represent the BDD.
     */
    private Expression convertPred(BDD bdd) {
        switch (outputMode) {
            case NORMAL:
                return BddToCif.bddToCifPred(bdd, cifBddSpec);

            case CNF:
                return BddToCif.bddToCifPred(bdd, cifBddSpec, false);

            case DNF:
                return BddToCif.bddToCifPred(bdd, cifBddSpec, true);

            case NODES: {
                // Add to node map, and get index.
                int idx = bddToNodeMap(bdd);
                if (idx == -1) {
                    return CifValueUtils.makeTrue();
                }
                if (idx == -2) {
                    return CifValueUtils.makeFalse();
                }
                Assert.check(idx >= 0);

                // Return function call to evaluate the BDD.
                FunctionExpression funcRef = newFunctionExpression();
                funcRef.setFunction(bddEvalFunc);
                funcRef.setType(CifTypeUtils.makeFunctionType(bddEvalFunc, null));

                Expression arg0 = CifValueUtils.makeInt(idx);

                AlgVariableExpression arg1 = newAlgVariableExpression();
                arg1.setVariable(bddValuesVar);
                arg1.setType(deepclone(bddValuesVar.getType()));

                FunctionCallExpression callExpr = newFunctionCallExpression();
                callExpr.setFunction(funcRef);
                callExpr.getArguments().add(arg0);
                callExpr.getArguments().add(arg1);
                callExpr.setType(newBoolType());

                return callExpr;
            }
        }

        throw new RuntimeException("Unknown output mode: " + outputMode);
    }

    /**
     * Adds the given BDD to the {@link #bddNodeMap} and {@link #bddNodesConst}.
     *
     * @param bdd The BDD node.
     * @return The 0-based index into the node array (index in the value of {@link #bddNodesConst} and the value that
     *     the BDD maps to in {@link #bddNodeMap}), or {@code -1} for the one/true node, or {@code -2} for the
     *     zero/false node.
     */
    private int bddToNodeMap(BDD bdd) {
        // Special case for true/false.
        if (bdd.isOne()) {
            return -1;
        }
        if (bdd.isZero()) {
            return -2;
        }

        // Convert a node only once.
        Integer idx = bddNodeMap.get(bdd);
        if (idx != null) {
            return idx;
        }

        // New node. Add to the mapping.
        idx = bddNodeMap.size();
        bddNodeMap.put(bdd, idx);

        // Add empty tuple expression to the list of nodes, to be filled later.
        ListExpression nodesExpr = (ListExpression)bddNodesConst.getValue();
        TupleExpression tupleExpr = newTupleExpression();
        nodesExpr.getElements().add(tupleExpr);

        // Recursively convert low/high edges of the BDD node.
        int lowIdx = bddToNodeMap(bdd.low());
        int highIdx = bddToNodeMap(bdd.high());

        // Fill the tuple expression.
        int bddVarIdx = bdd.var();
        Assert.check(bddVarIdx % 2 == 0); // Is an old variable.
        Integer cifVarIdx = bddVarIdxMap.get(bddVarIdx);
        Assert.notNull(cifVarIdx);
        tupleExpr.getFields().add(CifValueUtils.makeInt(cifVarIdx));
        tupleExpr.getFields().add(CifValueUtils.makeInt(lowIdx));
        tupleExpr.getFields().add(CifValueUtils.makeInt(highIdx));

        CifType fieldType0 = tupleExpr.getFields().get(0).getType();
        CifType fieldType1 = tupleExpr.getFields().get(1).getType();
        CifType fieldType2 = tupleExpr.getFields().get(2).getType();
        List<CifType> fieldTypes = list(fieldType0, fieldType1, fieldType2);
        CifType tupleType = CifTypeUtils.makeTupleType(fieldTypes, null);
        tupleExpr.setType(tupleType);

        // Return the index.
        return idx;
    }

    /**
     * Recursively relabel requirement automata to supervisors.
     *
     * @param component The component in which to recursively apply the relabeling.
     */
    private static void relabelRequirementAutomata(ComplexComponent component) {
        // Relabel requirement automata as supervisor.
        if (component instanceof Automaton) {
            Automaton aut = (Automaton)component;
            if (aut.getKind() == SupKind.REQUIREMENT) {
                aut.setKind(SupKind.SUPERVISOR);
            }
            return;
        }

        // Recursively relabel for groups.
        Group group = (Group)component;
        for (Component child: group.getComponents()) {
            relabelRequirementAutomata((ComplexComponent)child);
        }
    }

    /**
     * Recursively relabel requirement invariants to supervisors.
     *
     * @param component The component in which to recursively apply the relabeling.
     */
    private static void relabelRequirementInvariants(ComplexComponent component) {
        // Relabel requirement invariants in automata.
        if (component instanceof Automaton) {
            Automaton aut = (Automaton)component;
            relabelRequirementInvariants(aut.getInvariants());
            for (Location loc: aut.getLocations()) {
                relabelRequirementInvariants(loc.getInvariants());
            }
            return;
        }

        // Relabel invariants in the group.
        Group group = (Group)component;
        relabelRequirementInvariants(group.getInvariants());

        // Recursively relabel for groups.
        for (Component child: group.getComponents()) {
            relabelRequirementInvariants((ComplexComponent)child);
        }
    }

    /**
     * Relabel requirement invariants to supervisor.
     *
     * @param invs The invariants to relabel.
     */
    private static void relabelRequirementInvariants(List<Invariant> invs) {
        for (Invariant inv: invs) {
            if (inv.getSupKind() == SupKind.REQUIREMENT) {
                inv.setSupKind(SupKind.SUPERVISOR);
            }
        }
    }

    /**
     * Creates a supervisor automaton with the given name, in the root of the specification.
     *
     * @param supName The name of the supervisor automaton.
     * @return The newly created supervisor automaton.
     */
    private Automaton createSupervisorAutomaton(String supName) {
        // Create automaton.
        Automaton aut = newAutomaton();
        aut.setKind(SupKind.SUPERVISOR);

        // Give new supervisor automaton a unique name.
        String name = supName;
        Set<String> curNames;
        curNames = CifScopeUtils.getSymbolNamesForScope(spec, null);
        if (curNames.contains(supName)) {
            name = CifScopeUtils.getUniqueName(name, curNames, Collections.emptySet());
            cifBddSpec.settings.getWarnOutput().line(
                    "Supervisor automaton is named \"%s\" instead of \"%s\" to avoid a naming conflict.", name,
                    supName);
        }
        aut.setName(name);

        // Add automaton to the specification.
        spec.getComponents().add(aut);
        return aut;
    }

    /**
     * Creates a self loop edge.
     *
     * @param event The event on the edge.
     * @param guards The guards of the edge.
     * @return The newly created self loop edge.
     */
    private static Edge createSelfLoop(Event event, List<Expression> guards) {
        EventExpression eventRef = newEventExpression();
        eventRef.setEvent(event);
        eventRef.setType(newBoolType());

        EdgeEvent edgeEvent = newEdgeEvent();
        edgeEvent.setEvent(eventRef);

        Edge edge = newEdge();
        edge.getGuards().addAll(guards);
        edge.getEvents().add(edgeEvent);

        return edge;
    }

    /**
     * Add a namespace around the resulting supervisor. With 'supervisor', we here mean the entire specification,
     * including plants and former requirements, both of which can be seen as observers for the added supervisor
     * automaton.
     *
     * @param namespace The (absolute) namespace name. Is not {@code null} and has already been
     *     {@link CifValidationUtils#isValidName validated}.
     * @return The new specification.
     */
    private Specification addNamespace(String namespace) {
        // Create new specification.
        Specification newSpec = newSpecification();
        newSpec.getAnnotations().addAll(spec.getAnnotations());

        // Collect the events from original specification.
        List<Event> events = list();
        CifToBddConverter.collectEvents(spec, events);

        // Move events to new specification, in proper groups, to maintain
        // their original identity.
        for (Event event: events) {
            addEvent(newSpec, event);
        }

        // Create namespace.
        String[] namespaceParts = namespace.split("\\.");
        Group namespaceGroup;
        try {
            namespaceGroup = getGroup(newSpec, namespaceParts, 0);
        } catch (DuplicateNameException ex) {
            // The new specification only has groups and events. To a conflict
            // must be for an event.
            PositionObject obj = CifScopeUtils.getObject(ex.group, ex.name);
            Assert.check(obj instanceof Event);

            // Report error to user.
            String msg = fmt("Can't create supervisor namespace \"%s\": an event named \"%s\" already exists in %s.",
                    namespace, ex.name, CifTextUtils.getComponentText2(ex.group));
            throw new InvalidOptionException(msg);
        }

        // Make sure namespace is empty (no named declarations).
        Set<String> names = CifScopeUtils.getSymbolNamesForScope(namespaceGroup, null);
        if (!names.isEmpty()) {
            List<String> sortedNames = Sets.sortedstrings(names);
            for (int i = 0; i < sortedNames.size(); i++) {
                sortedNames.set(i, fmt("\"%s\"", sortedNames.get(i)));
            }
            String msg1 = fmt("Can't put supervisor in namespace \"%s\".", namespace);
            String msg2 = fmt("The namespace is not empty, as it contains the following declarations: %s.",
                    String.join(", ", sortedNames));
            Exception ex = new InvalidOptionException(msg2);
            throw new InvalidOptionException(msg1, ex);
        }

        // Move contents of the old specification to the new specification.
        namespaceGroup.getComponents().addAll(spec.getComponents());
        namespaceGroup.getDefinitions().addAll(spec.getDefinitions());
        namespaceGroup.getDeclarations().addAll(spec.getDeclarations());
        namespaceGroup.getInitials().addAll(spec.getInitials());
        namespaceGroup.getInvariants().addAll(spec.getInvariants());
        namespaceGroup.getMarkeds().addAll(spec.getMarkeds());
        namespaceGroup.getEquations().addAll(spec.getEquations());
        namespaceGroup.getIoDecls().addAll(spec.getIoDecls());

        // Return the new specification.
        return newSpec;
    }

    /**
     * Adds the given event to the given specification. The event is added in groups if necessary, to ensure the proper
     * identity (absolute name).
     *
     * @param spec The specification to which to add the event.
     * @param event The event to add.
     */
    private static void addEvent(Specification spec, Event event) {
        // Get absolute name of event.
        String[] names = CifTextUtils.getAbsName(event, false).split("\\.");

        // Get/create parent group.
        Group group;
        try {
            group = getGroup(spec, names, 1);
        } catch (DuplicateNameException ex) {
            // Can't occur: no naming conflicts in original specification.
            throw new RuntimeException("Can't occur.", ex);
        }

        // Add event. This moves it out of the original specification.
        group.getDeclarations().add(event);
    }

    /**
     * Obtains the group from the given specification that has the given names (excluding the last part, if applicable)
     * as its absolute name. If no such group exists, groups are created as necessary.
     *
     * @param spec The specification from which to obtain the groups. Is modified in-place if necessary.
     * @param names The absolute name of the group. Names must not be escaped for keywords.
     * @param cnt The number of parts of the absolute name to exclude/ignore. If zero, no parts are ignored, if one, the
     *     last part is ignored, if two, the last two parts are ignored, etc.
     * @return The obtained or created group.
     * @throws DuplicateNameException If creating a new group resulted in a duplicate name.
     */
    private static Group getGroup(Specification spec, String[] names, int cnt) throws DuplicateNameException {
        // Start at the specification.
        Group current = spec;

        // Process all but last part of the absolute name.
        NAMES:
        for (int i = 0; i < names.length - cnt; i++) {
            String name = names[i];

            // Get existing group.
            for (Component comp: current.getComponents()) {
                if (comp.getName().equals(name)) {
                    current = (Group)comp;
                    continue NAMES;
                }
            }

            // Check that creating a new group won't lead to duplicate names.
            Set<String> curNames;
            curNames = CifScopeUtils.getSymbolNamesForScope(current, null);
            if (curNames.contains(name)) {
                throw new DuplicateNameException(current, name);
            }

            // Create new group.
            Group group = newGroup();
            group.setName(name);
            current.getComponents().add(group);
            current = group;
        }
        return current;
    }

    /** Duplicate name within a group exception. */
    private static class DuplicateNameException extends Exception {
        /** The group that contains the duplicate name. */
        public final Group group;

        /** The duplicate name. */
        public final String name;

        /**
         * Constructor for the {@link DuplicateNameException} class.
         *
         * @param group The group that contains the duplicate name.
         * @param name The duplicate name.
         */
        public DuplicateNameException(Group group, String name) {
            this.group = group;
            this.name = name;
        }
    }

    /** Sorter to sort edges in ascending alphabetical order based on their absolute names. */
    private static class EdgeSorter implements Comparator<Edge> {
        @Override
        public int compare(Edge edge1, Edge edge2) {
            Expression eventRef1 = first(edge1.getEvents()).getEvent();
            Expression eventRef2 = first(edge2.getEvents()).getEvent();
            Event event1 = ((EventExpression)eventRef1).getEvent();
            Event event2 = ((EventExpression)eventRef2).getEvent();
            String name1 = CifTextUtils.getAbsName(event1, false);
            String name2 = CifTextUtils.getAbsName(event2, false);
            return Strings.SORTER.compare(name1, name2);
        }
    }
}
