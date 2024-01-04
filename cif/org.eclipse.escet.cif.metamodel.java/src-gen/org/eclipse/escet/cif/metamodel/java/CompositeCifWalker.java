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

// Generated using the "org.eclipse.escet.common.emf.ecore.codegen" project.

// Disable Eclipse Java formatter for generated code file:
// @formatter:off

package org.eclipse.escet.cif.metamodel.java;

import org.eclipse.escet.cif.metamodel.cif.AlgParameter;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.ComponentDef;
import org.eclipse.escet.cif.metamodel.cif.ComponentInst;
import org.eclipse.escet.cif.metamodel.cif.ComponentParameter;
import org.eclipse.escet.cif.metamodel.cif.Equation;
import org.eclipse.escet.cif.metamodel.cif.EventParameter;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.IoDecl;
import org.eclipse.escet.cif.metamodel.cif.LocationParameter;
import org.eclipse.escet.cif.metamodel.cif.Parameter;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.annotations.AnnotatedObject;
import org.eclipse.escet.cif.metamodel.cif.annotations.Annotation;
import org.eclipse.escet.cif.metamodel.cif.annotations.AnnotationArgument;
import org.eclipse.escet.cif.metamodel.cif.automata.Alphabet;
import org.eclipse.escet.cif.metamodel.cif.automata.Assignment;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeReceive;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeSend;
import org.eclipse.escet.cif.metamodel.cif.automata.ElifUpdate;
import org.eclipse.escet.cif.metamodel.cif.automata.IfUpdate;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.automata.Monitors;
import org.eclipse.escet.cif.metamodel.cif.automata.Update;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgCopy;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgFile;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgIn;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEvent;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEventIf;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEventIfEntry;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEventSingle;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgMove;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgOut;
import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Constant;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumLiteral;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.TypeDecl;
import org.eclipse.escet.cif.metamodel.cif.declarations.VariableValue;
import org.eclipse.escet.cif.metamodel.cif.expressions.AlgVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BaseFunctionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BoolExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CastExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CompInstWrapExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CompParamExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CompParamWrapExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ComponentExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ConstantExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ContVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DictExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DictPair;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ElifExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EnumLiteralExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.FieldExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.FunctionCallExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.FunctionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IfExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.InputVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IntExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ListExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.LocationExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ProjectionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.RealExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ReceivedExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SelfExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SetExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SliceExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunctionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.StringExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SwitchCase;
import org.eclipse.escet.cif.metamodel.cif.expressions.SwitchExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TauExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TimeExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TupleExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression;
import org.eclipse.escet.cif.metamodel.cif.functions.AssignmentFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.BreakFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.ContinueFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.ElifFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.ExternalFunction;
import org.eclipse.escet.cif.metamodel.cif.functions.Function;
import org.eclipse.escet.cif.metamodel.cif.functions.FunctionParameter;
import org.eclipse.escet.cif.metamodel.cif.functions.FunctionStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.IfFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.InternalFunction;
import org.eclipse.escet.cif.metamodel.cif.functions.ReturnFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.WhileFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.print.Print;
import org.eclipse.escet.cif.metamodel.cif.print.PrintFile;
import org.eclipse.escet.cif.metamodel.cif.print.PrintFor;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.CompInstWrapType;
import org.eclipse.escet.cif.metamodel.cif.types.CompParamWrapType;
import org.eclipse.escet.cif.metamodel.cif.types.ComponentDefType;
import org.eclipse.escet.cif.metamodel.cif.types.ComponentType;
import org.eclipse.escet.cif.metamodel.cif.types.DictType;
import org.eclipse.escet.cif.metamodel.cif.types.DistType;
import org.eclipse.escet.cif.metamodel.cif.types.EnumType;
import org.eclipse.escet.cif.metamodel.cif.types.Field;
import org.eclipse.escet.cif.metamodel.cif.types.FuncType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.cif.metamodel.cif.types.ListType;
import org.eclipse.escet.cif.metamodel.cif.types.RealType;
import org.eclipse.escet.cif.metamodel.cif.types.SetType;
import org.eclipse.escet.cif.metamodel.cif.types.StringType;
import org.eclipse.escet.cif.metamodel.cif.types.TupleType;
import org.eclipse.escet.cif.metamodel.cif.types.TypeRef;
import org.eclipse.escet.cif.metamodel.cif.types.VoidType;
import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/**
 * A composite walker for "cif" models.
 *
 * <p>The composite walker works as follows:
 * <ul>
 *  <li>Performs a top-down depth-first walk over the object tree, using the
 *   containment hierarchy.</li>
 *  <li>No particular order should be assumed for walking over the child
 *    features.</li>
 *  <li>For each object encountered, pre-processing is performed before walking
 *    over the children, and post-processing is performed after walking over
 *    the children.</li>
 *  <li>Pre-processing for objects is done by crawling up the inheritance
 *    hierarchy of the object, performing pre-processing for each of the
 *    types encountered in the type hierarchy. The pre-processing methods are
 *    invoked from most general to most specific class (super classes before
 *    base classes).</li>
 *  <li>Post-processing for objects is done by crawling up the inheritance
 *    hierarchy of the object, performing post-processing for each of the
 *    types encountered in the type hierarchy. The post-processing methods are
 *    invoked from most specific to most general class (base classes before
 *    super classes).</li>
 * </ul>
 * </p>
 *
 * <p>A composite walking does not perform any pre-processing or post-processing
 * by itself. It can be configured with any number of other (composite or
 * non-composite) model walkers. As pre-processing and post-processing the
 * composite model walker invokes pre-processing and post-processing on each
 * 'other' model walker, in the order they are supplied. The walking and
 * crawling methods of the non-composite of the 'other' model walkers are not
 * used.</p>
 *
 * <p>This abstract walker class has no public methods. It is up to the derived
 * classes to add a public method as entry method. They can decide which
 * classes are to be used as starting point, and they can give the public
 * method a proper name, parameters, etc. They may even allow multiple public
 * methods to allow starting from multiple classes.</p>
 */
public abstract class CompositeCifWalker extends CifWalker {
    /** The walkers to be composed by this composite walker. */
    private final CifWalker[] walkers;

    /**
     * Constructor of the {@link CompositeCifWalker} class.
     *
     * @param walkers The walkers to be composed by this composite walker.
     */
    public CompositeCifWalker(CifWalker[] walkers) {
        this.walkers = walkers;
    }

    @Override
    protected void preprocessAlgParameter(AlgParameter obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessAlgParameter(obj);
        }
    }

    @Override
    protected void postprocessAlgParameter(AlgParameter obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessAlgParameter(obj);
        }
    }

    @Override
    protected void preprocessAlgVariable(AlgVariable obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessAlgVariable(obj);
        }
    }

    @Override
    protected void postprocessAlgVariable(AlgVariable obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessAlgVariable(obj);
        }
    }

    @Override
    protected void preprocessAlgVariableExpression(AlgVariableExpression obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessAlgVariableExpression(obj);
        }
    }

    @Override
    protected void postprocessAlgVariableExpression(AlgVariableExpression obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessAlgVariableExpression(obj);
        }
    }

    @Override
    protected void preprocessAlphabet(Alphabet obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessAlphabet(obj);
        }
    }

    @Override
    protected void postprocessAlphabet(Alphabet obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessAlphabet(obj);
        }
    }

    @Override
    protected void preprocessAnnotatedObject(AnnotatedObject obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessAnnotatedObject(obj);
        }
    }

    @Override
    protected void postprocessAnnotatedObject(AnnotatedObject obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessAnnotatedObject(obj);
        }
    }

    @Override
    protected void preprocessAnnotation(Annotation obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessAnnotation(obj);
        }
    }

    @Override
    protected void postprocessAnnotation(Annotation obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessAnnotation(obj);
        }
    }

    @Override
    protected void preprocessAnnotationArgument(AnnotationArgument obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessAnnotationArgument(obj);
        }
    }

    @Override
    protected void postprocessAnnotationArgument(AnnotationArgument obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessAnnotationArgument(obj);
        }
    }

    @Override
    protected void preprocessAssignment(Assignment obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessAssignment(obj);
        }
    }

    @Override
    protected void postprocessAssignment(Assignment obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessAssignment(obj);
        }
    }

    @Override
    protected void preprocessAssignmentFuncStatement(AssignmentFuncStatement obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessAssignmentFuncStatement(obj);
        }
    }

    @Override
    protected void postprocessAssignmentFuncStatement(AssignmentFuncStatement obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessAssignmentFuncStatement(obj);
        }
    }

    @Override
    protected void preprocessAutomaton(Automaton obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessAutomaton(obj);
        }
    }

    @Override
    protected void postprocessAutomaton(Automaton obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessAutomaton(obj);
        }
    }

    @Override
    protected void preprocessBaseFunctionExpression(BaseFunctionExpression obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessBaseFunctionExpression(obj);
        }
    }

    @Override
    protected void postprocessBaseFunctionExpression(BaseFunctionExpression obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessBaseFunctionExpression(obj);
        }
    }

    @Override
    protected void preprocessBinaryExpression(BinaryExpression obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessBinaryExpression(obj);
        }
    }

    @Override
    protected void postprocessBinaryExpression(BinaryExpression obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessBinaryExpression(obj);
        }
    }

    @Override
    protected void preprocessBoolExpression(BoolExpression obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessBoolExpression(obj);
        }
    }

    @Override
    protected void postprocessBoolExpression(BoolExpression obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessBoolExpression(obj);
        }
    }

    @Override
    protected void preprocessBoolType(BoolType obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessBoolType(obj);
        }
    }

    @Override
    protected void postprocessBoolType(BoolType obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessBoolType(obj);
        }
    }

    @Override
    protected void preprocessBreakFuncStatement(BreakFuncStatement obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessBreakFuncStatement(obj);
        }
    }

    @Override
    protected void postprocessBreakFuncStatement(BreakFuncStatement obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessBreakFuncStatement(obj);
        }
    }

    @Override
    protected void preprocessCastExpression(CastExpression obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessCastExpression(obj);
        }
    }

    @Override
    protected void postprocessCastExpression(CastExpression obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessCastExpression(obj);
        }
    }

    @Override
    protected void preprocessCifType(CifType obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessCifType(obj);
        }
    }

    @Override
    protected void postprocessCifType(CifType obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessCifType(obj);
        }
    }

    @Override
    protected void preprocessCompInstWrapExpression(CompInstWrapExpression obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessCompInstWrapExpression(obj);
        }
    }

    @Override
    protected void postprocessCompInstWrapExpression(CompInstWrapExpression obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessCompInstWrapExpression(obj);
        }
    }

    @Override
    protected void preprocessCompInstWrapType(CompInstWrapType obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessCompInstWrapType(obj);
        }
    }

    @Override
    protected void postprocessCompInstWrapType(CompInstWrapType obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessCompInstWrapType(obj);
        }
    }

    @Override
    protected void preprocessCompParamExpression(CompParamExpression obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessCompParamExpression(obj);
        }
    }

    @Override
    protected void postprocessCompParamExpression(CompParamExpression obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessCompParamExpression(obj);
        }
    }

    @Override
    protected void preprocessCompParamWrapExpression(CompParamWrapExpression obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessCompParamWrapExpression(obj);
        }
    }

    @Override
    protected void postprocessCompParamWrapExpression(CompParamWrapExpression obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessCompParamWrapExpression(obj);
        }
    }

    @Override
    protected void preprocessCompParamWrapType(CompParamWrapType obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessCompParamWrapType(obj);
        }
    }

    @Override
    protected void postprocessCompParamWrapType(CompParamWrapType obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessCompParamWrapType(obj);
        }
    }

    @Override
    protected void preprocessComplexComponent(ComplexComponent obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessComplexComponent(obj);
        }
    }

    @Override
    protected void postprocessComplexComponent(ComplexComponent obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessComplexComponent(obj);
        }
    }

    @Override
    protected void preprocessComponent(Component obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessComponent(obj);
        }
    }

    @Override
    protected void postprocessComponent(Component obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessComponent(obj);
        }
    }

    @Override
    protected void preprocessComponentDef(ComponentDef obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessComponentDef(obj);
        }
    }

    @Override
    protected void postprocessComponentDef(ComponentDef obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessComponentDef(obj);
        }
    }

    @Override
    protected void preprocessComponentDefType(ComponentDefType obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessComponentDefType(obj);
        }
    }

    @Override
    protected void postprocessComponentDefType(ComponentDefType obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessComponentDefType(obj);
        }
    }

    @Override
    protected void preprocessComponentExpression(ComponentExpression obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessComponentExpression(obj);
        }
    }

    @Override
    protected void postprocessComponentExpression(ComponentExpression obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessComponentExpression(obj);
        }
    }

    @Override
    protected void preprocessComponentInst(ComponentInst obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessComponentInst(obj);
        }
    }

    @Override
    protected void postprocessComponentInst(ComponentInst obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessComponentInst(obj);
        }
    }

    @Override
    protected void preprocessComponentParameter(ComponentParameter obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessComponentParameter(obj);
        }
    }

    @Override
    protected void postprocessComponentParameter(ComponentParameter obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessComponentParameter(obj);
        }
    }

    @Override
    protected void preprocessComponentType(ComponentType obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessComponentType(obj);
        }
    }

    @Override
    protected void postprocessComponentType(ComponentType obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessComponentType(obj);
        }
    }

    @Override
    protected void preprocessConstant(Constant obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessConstant(obj);
        }
    }

    @Override
    protected void postprocessConstant(Constant obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessConstant(obj);
        }
    }

    @Override
    protected void preprocessConstantExpression(ConstantExpression obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessConstantExpression(obj);
        }
    }

    @Override
    protected void postprocessConstantExpression(ConstantExpression obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessConstantExpression(obj);
        }
    }

    @Override
    protected void preprocessContVariable(ContVariable obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessContVariable(obj);
        }
    }

    @Override
    protected void postprocessContVariable(ContVariable obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessContVariable(obj);
        }
    }

    @Override
    protected void preprocessContVariableExpression(ContVariableExpression obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessContVariableExpression(obj);
        }
    }

    @Override
    protected void postprocessContVariableExpression(ContVariableExpression obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessContVariableExpression(obj);
        }
    }

    @Override
    protected void preprocessContinueFuncStatement(ContinueFuncStatement obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessContinueFuncStatement(obj);
        }
    }

    @Override
    protected void postprocessContinueFuncStatement(ContinueFuncStatement obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessContinueFuncStatement(obj);
        }
    }

    @Override
    protected void preprocessDeclaration(Declaration obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessDeclaration(obj);
        }
    }

    @Override
    protected void postprocessDeclaration(Declaration obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessDeclaration(obj);
        }
    }

    @Override
    protected void preprocessDictExpression(DictExpression obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessDictExpression(obj);
        }
    }

    @Override
    protected void postprocessDictExpression(DictExpression obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessDictExpression(obj);
        }
    }

    @Override
    protected void preprocessDictPair(DictPair obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessDictPair(obj);
        }
    }

    @Override
    protected void postprocessDictPair(DictPair obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessDictPair(obj);
        }
    }

    @Override
    protected void preprocessDictType(DictType obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessDictType(obj);
        }
    }

    @Override
    protected void postprocessDictType(DictType obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessDictType(obj);
        }
    }

    @Override
    protected void preprocessDiscVariable(DiscVariable obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessDiscVariable(obj);
        }
    }

    @Override
    protected void postprocessDiscVariable(DiscVariable obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessDiscVariable(obj);
        }
    }

    @Override
    protected void preprocessDiscVariableExpression(DiscVariableExpression obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessDiscVariableExpression(obj);
        }
    }

    @Override
    protected void postprocessDiscVariableExpression(DiscVariableExpression obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessDiscVariableExpression(obj);
        }
    }

    @Override
    protected void preprocessDistType(DistType obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessDistType(obj);
        }
    }

    @Override
    protected void postprocessDistType(DistType obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessDistType(obj);
        }
    }

    @Override
    protected void preprocessEdge(Edge obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessEdge(obj);
        }
    }

    @Override
    protected void postprocessEdge(Edge obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessEdge(obj);
        }
    }

    @Override
    protected void preprocessEdgeEvent(EdgeEvent obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessEdgeEvent(obj);
        }
    }

    @Override
    protected void postprocessEdgeEvent(EdgeEvent obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessEdgeEvent(obj);
        }
    }

    @Override
    protected void preprocessEdgeReceive(EdgeReceive obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessEdgeReceive(obj);
        }
    }

    @Override
    protected void postprocessEdgeReceive(EdgeReceive obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessEdgeReceive(obj);
        }
    }

    @Override
    protected void preprocessEdgeSend(EdgeSend obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessEdgeSend(obj);
        }
    }

    @Override
    protected void postprocessEdgeSend(EdgeSend obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessEdgeSend(obj);
        }
    }

    @Override
    protected void preprocessElifExpression(ElifExpression obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessElifExpression(obj);
        }
    }

    @Override
    protected void postprocessElifExpression(ElifExpression obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessElifExpression(obj);
        }
    }

    @Override
    protected void preprocessElifFuncStatement(ElifFuncStatement obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessElifFuncStatement(obj);
        }
    }

    @Override
    protected void postprocessElifFuncStatement(ElifFuncStatement obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessElifFuncStatement(obj);
        }
    }

    @Override
    protected void preprocessElifUpdate(ElifUpdate obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessElifUpdate(obj);
        }
    }

    @Override
    protected void postprocessElifUpdate(ElifUpdate obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessElifUpdate(obj);
        }
    }

    @Override
    protected void preprocessEnumDecl(EnumDecl obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessEnumDecl(obj);
        }
    }

    @Override
    protected void postprocessEnumDecl(EnumDecl obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessEnumDecl(obj);
        }
    }

    @Override
    protected void preprocessEnumLiteral(EnumLiteral obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessEnumLiteral(obj);
        }
    }

    @Override
    protected void postprocessEnumLiteral(EnumLiteral obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessEnumLiteral(obj);
        }
    }

    @Override
    protected void preprocessEnumLiteralExpression(EnumLiteralExpression obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessEnumLiteralExpression(obj);
        }
    }

    @Override
    protected void postprocessEnumLiteralExpression(EnumLiteralExpression obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessEnumLiteralExpression(obj);
        }
    }

    @Override
    protected void preprocessEnumType(EnumType obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessEnumType(obj);
        }
    }

    @Override
    protected void postprocessEnumType(EnumType obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessEnumType(obj);
        }
    }

    @Override
    protected void preprocessEquation(Equation obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessEquation(obj);
        }
    }

    @Override
    protected void postprocessEquation(Equation obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessEquation(obj);
        }
    }

    @Override
    protected void preprocessEvent(Event obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessEvent(obj);
        }
    }

    @Override
    protected void postprocessEvent(Event obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessEvent(obj);
        }
    }

    @Override
    protected void preprocessEventExpression(EventExpression obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessEventExpression(obj);
        }
    }

    @Override
    protected void postprocessEventExpression(EventExpression obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessEventExpression(obj);
        }
    }

    @Override
    protected void preprocessEventParameter(EventParameter obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessEventParameter(obj);
        }
    }

    @Override
    protected void postprocessEventParameter(EventParameter obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessEventParameter(obj);
        }
    }

    @Override
    protected void preprocessExpression(Expression obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessExpression(obj);
        }
    }

    @Override
    protected void postprocessExpression(Expression obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessExpression(obj);
        }
    }

    @Override
    protected void preprocessExternalFunction(ExternalFunction obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessExternalFunction(obj);
        }
    }

    @Override
    protected void postprocessExternalFunction(ExternalFunction obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessExternalFunction(obj);
        }
    }

    @Override
    protected void preprocessField(Field obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessField(obj);
        }
    }

    @Override
    protected void postprocessField(Field obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessField(obj);
        }
    }

    @Override
    protected void preprocessFieldExpression(FieldExpression obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessFieldExpression(obj);
        }
    }

    @Override
    protected void postprocessFieldExpression(FieldExpression obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessFieldExpression(obj);
        }
    }

    @Override
    protected void preprocessFuncType(FuncType obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessFuncType(obj);
        }
    }

    @Override
    protected void postprocessFuncType(FuncType obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessFuncType(obj);
        }
    }

    @Override
    protected void preprocessFunction(Function obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessFunction(obj);
        }
    }

    @Override
    protected void postprocessFunction(Function obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessFunction(obj);
        }
    }

    @Override
    protected void preprocessFunctionCallExpression(FunctionCallExpression obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessFunctionCallExpression(obj);
        }
    }

    @Override
    protected void postprocessFunctionCallExpression(FunctionCallExpression obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessFunctionCallExpression(obj);
        }
    }

    @Override
    protected void preprocessFunctionExpression(FunctionExpression obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessFunctionExpression(obj);
        }
    }

    @Override
    protected void postprocessFunctionExpression(FunctionExpression obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessFunctionExpression(obj);
        }
    }

    @Override
    protected void preprocessFunctionParameter(FunctionParameter obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessFunctionParameter(obj);
        }
    }

    @Override
    protected void postprocessFunctionParameter(FunctionParameter obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessFunctionParameter(obj);
        }
    }

    @Override
    protected void preprocessFunctionStatement(FunctionStatement obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessFunctionStatement(obj);
        }
    }

    @Override
    protected void postprocessFunctionStatement(FunctionStatement obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessFunctionStatement(obj);
        }
    }

    @Override
    protected void preprocessGroup(Group obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessGroup(obj);
        }
    }

    @Override
    protected void postprocessGroup(Group obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessGroup(obj);
        }
    }

    @Override
    protected void preprocessIfExpression(IfExpression obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessIfExpression(obj);
        }
    }

    @Override
    protected void postprocessIfExpression(IfExpression obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessIfExpression(obj);
        }
    }

    @Override
    protected void preprocessIfFuncStatement(IfFuncStatement obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessIfFuncStatement(obj);
        }
    }

    @Override
    protected void postprocessIfFuncStatement(IfFuncStatement obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessIfFuncStatement(obj);
        }
    }

    @Override
    protected void preprocessIfUpdate(IfUpdate obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessIfUpdate(obj);
        }
    }

    @Override
    protected void postprocessIfUpdate(IfUpdate obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessIfUpdate(obj);
        }
    }

    @Override
    protected void preprocessInputVariable(InputVariable obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessInputVariable(obj);
        }
    }

    @Override
    protected void postprocessInputVariable(InputVariable obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessInputVariable(obj);
        }
    }

    @Override
    protected void preprocessInputVariableExpression(InputVariableExpression obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessInputVariableExpression(obj);
        }
    }

    @Override
    protected void postprocessInputVariableExpression(InputVariableExpression obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessInputVariableExpression(obj);
        }
    }

    @Override
    protected void preprocessIntExpression(IntExpression obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessIntExpression(obj);
        }
    }

    @Override
    protected void postprocessIntExpression(IntExpression obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessIntExpression(obj);
        }
    }

    @Override
    protected void preprocessIntType(IntType obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessIntType(obj);
        }
    }

    @Override
    protected void postprocessIntType(IntType obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessIntType(obj);
        }
    }

    @Override
    protected void preprocessInternalFunction(InternalFunction obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessInternalFunction(obj);
        }
    }

    @Override
    protected void postprocessInternalFunction(InternalFunction obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessInternalFunction(obj);
        }
    }

    @Override
    protected void preprocessInvariant(Invariant obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessInvariant(obj);
        }
    }

    @Override
    protected void postprocessInvariant(Invariant obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessInvariant(obj);
        }
    }

    @Override
    protected void preprocessIoDecl(IoDecl obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessIoDecl(obj);
        }
    }

    @Override
    protected void postprocessIoDecl(IoDecl obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessIoDecl(obj);
        }
    }

    @Override
    protected void preprocessListExpression(ListExpression obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessListExpression(obj);
        }
    }

    @Override
    protected void postprocessListExpression(ListExpression obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessListExpression(obj);
        }
    }

    @Override
    protected void preprocessListType(ListType obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessListType(obj);
        }
    }

    @Override
    protected void postprocessListType(ListType obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessListType(obj);
        }
    }

    @Override
    protected void preprocessLocation(Location obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessLocation(obj);
        }
    }

    @Override
    protected void postprocessLocation(Location obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessLocation(obj);
        }
    }

    @Override
    protected void preprocessLocationExpression(LocationExpression obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessLocationExpression(obj);
        }
    }

    @Override
    protected void postprocessLocationExpression(LocationExpression obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessLocationExpression(obj);
        }
    }

    @Override
    protected void preprocessLocationParameter(LocationParameter obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessLocationParameter(obj);
        }
    }

    @Override
    protected void postprocessLocationParameter(LocationParameter obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessLocationParameter(obj);
        }
    }

    @Override
    protected void preprocessMonitors(Monitors obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessMonitors(obj);
        }
    }

    @Override
    protected void postprocessMonitors(Monitors obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessMonitors(obj);
        }
    }

    @Override
    protected void preprocessParameter(Parameter obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessParameter(obj);
        }
    }

    @Override
    protected void postprocessParameter(Parameter obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessParameter(obj);
        }
    }

    @Override
    protected void preprocessPosition(Position obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessPosition(obj);
        }
    }

    @Override
    protected void postprocessPosition(Position obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessPosition(obj);
        }
    }

    @Override
    protected void preprocessPositionObject(PositionObject obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessPositionObject(obj);
        }
    }

    @Override
    protected void postprocessPositionObject(PositionObject obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessPositionObject(obj);
        }
    }

    @Override
    protected void preprocessPrint(Print obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessPrint(obj);
        }
    }

    @Override
    protected void postprocessPrint(Print obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessPrint(obj);
        }
    }

    @Override
    protected void preprocessPrintFile(PrintFile obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessPrintFile(obj);
        }
    }

    @Override
    protected void postprocessPrintFile(PrintFile obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessPrintFile(obj);
        }
    }

    @Override
    protected void preprocessPrintFor(PrintFor obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessPrintFor(obj);
        }
    }

    @Override
    protected void postprocessPrintFor(PrintFor obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessPrintFor(obj);
        }
    }

    @Override
    protected void preprocessProjectionExpression(ProjectionExpression obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessProjectionExpression(obj);
        }
    }

    @Override
    protected void postprocessProjectionExpression(ProjectionExpression obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessProjectionExpression(obj);
        }
    }

    @Override
    protected void preprocessRealExpression(RealExpression obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessRealExpression(obj);
        }
    }

    @Override
    protected void postprocessRealExpression(RealExpression obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessRealExpression(obj);
        }
    }

    @Override
    protected void preprocessRealType(RealType obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessRealType(obj);
        }
    }

    @Override
    protected void postprocessRealType(RealType obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessRealType(obj);
        }
    }

    @Override
    protected void preprocessReceivedExpression(ReceivedExpression obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessReceivedExpression(obj);
        }
    }

    @Override
    protected void postprocessReceivedExpression(ReceivedExpression obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessReceivedExpression(obj);
        }
    }

    @Override
    protected void preprocessReturnFuncStatement(ReturnFuncStatement obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessReturnFuncStatement(obj);
        }
    }

    @Override
    protected void postprocessReturnFuncStatement(ReturnFuncStatement obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessReturnFuncStatement(obj);
        }
    }

    @Override
    protected void preprocessSelfExpression(SelfExpression obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessSelfExpression(obj);
        }
    }

    @Override
    protected void postprocessSelfExpression(SelfExpression obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessSelfExpression(obj);
        }
    }

    @Override
    protected void preprocessSetExpression(SetExpression obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessSetExpression(obj);
        }
    }

    @Override
    protected void postprocessSetExpression(SetExpression obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessSetExpression(obj);
        }
    }

    @Override
    protected void preprocessSetType(SetType obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessSetType(obj);
        }
    }

    @Override
    protected void postprocessSetType(SetType obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessSetType(obj);
        }
    }

    @Override
    protected void preprocessSliceExpression(SliceExpression obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessSliceExpression(obj);
        }
    }

    @Override
    protected void postprocessSliceExpression(SliceExpression obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessSliceExpression(obj);
        }
    }

    @Override
    protected void preprocessSpecification(Specification obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessSpecification(obj);
        }
    }

    @Override
    protected void postprocessSpecification(Specification obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessSpecification(obj);
        }
    }

    @Override
    protected void preprocessStdLibFunctionExpression(StdLibFunctionExpression obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessStdLibFunctionExpression(obj);
        }
    }

    @Override
    protected void postprocessStdLibFunctionExpression(StdLibFunctionExpression obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessStdLibFunctionExpression(obj);
        }
    }

    @Override
    protected void preprocessStringExpression(StringExpression obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessStringExpression(obj);
        }
    }

    @Override
    protected void postprocessStringExpression(StringExpression obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessStringExpression(obj);
        }
    }

    @Override
    protected void preprocessStringType(StringType obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessStringType(obj);
        }
    }

    @Override
    protected void postprocessStringType(StringType obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessStringType(obj);
        }
    }

    @Override
    protected void preprocessSvgCopy(SvgCopy obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessSvgCopy(obj);
        }
    }

    @Override
    protected void postprocessSvgCopy(SvgCopy obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessSvgCopy(obj);
        }
    }

    @Override
    protected void preprocessSvgFile(SvgFile obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessSvgFile(obj);
        }
    }

    @Override
    protected void postprocessSvgFile(SvgFile obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessSvgFile(obj);
        }
    }

    @Override
    protected void preprocessSvgIn(SvgIn obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessSvgIn(obj);
        }
    }

    @Override
    protected void postprocessSvgIn(SvgIn obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessSvgIn(obj);
        }
    }

    @Override
    protected void preprocessSvgInEvent(SvgInEvent obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessSvgInEvent(obj);
        }
    }

    @Override
    protected void postprocessSvgInEvent(SvgInEvent obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessSvgInEvent(obj);
        }
    }

    @Override
    protected void preprocessSvgInEventIf(SvgInEventIf obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessSvgInEventIf(obj);
        }
    }

    @Override
    protected void postprocessSvgInEventIf(SvgInEventIf obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessSvgInEventIf(obj);
        }
    }

    @Override
    protected void preprocessSvgInEventIfEntry(SvgInEventIfEntry obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessSvgInEventIfEntry(obj);
        }
    }

    @Override
    protected void postprocessSvgInEventIfEntry(SvgInEventIfEntry obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessSvgInEventIfEntry(obj);
        }
    }

    @Override
    protected void preprocessSvgInEventSingle(SvgInEventSingle obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessSvgInEventSingle(obj);
        }
    }

    @Override
    protected void postprocessSvgInEventSingle(SvgInEventSingle obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessSvgInEventSingle(obj);
        }
    }

    @Override
    protected void preprocessSvgMove(SvgMove obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessSvgMove(obj);
        }
    }

    @Override
    protected void postprocessSvgMove(SvgMove obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessSvgMove(obj);
        }
    }

    @Override
    protected void preprocessSvgOut(SvgOut obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessSvgOut(obj);
        }
    }

    @Override
    protected void postprocessSvgOut(SvgOut obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessSvgOut(obj);
        }
    }

    @Override
    protected void preprocessSwitchCase(SwitchCase obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessSwitchCase(obj);
        }
    }

    @Override
    protected void postprocessSwitchCase(SwitchCase obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessSwitchCase(obj);
        }
    }

    @Override
    protected void preprocessSwitchExpression(SwitchExpression obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessSwitchExpression(obj);
        }
    }

    @Override
    protected void postprocessSwitchExpression(SwitchExpression obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessSwitchExpression(obj);
        }
    }

    @Override
    protected void preprocessTauExpression(TauExpression obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessTauExpression(obj);
        }
    }

    @Override
    protected void postprocessTauExpression(TauExpression obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessTauExpression(obj);
        }
    }

    @Override
    protected void preprocessTimeExpression(TimeExpression obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessTimeExpression(obj);
        }
    }

    @Override
    protected void postprocessTimeExpression(TimeExpression obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessTimeExpression(obj);
        }
    }

    @Override
    protected void preprocessTupleExpression(TupleExpression obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessTupleExpression(obj);
        }
    }

    @Override
    protected void postprocessTupleExpression(TupleExpression obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessTupleExpression(obj);
        }
    }

    @Override
    protected void preprocessTupleType(TupleType obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessTupleType(obj);
        }
    }

    @Override
    protected void postprocessTupleType(TupleType obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessTupleType(obj);
        }
    }

    @Override
    protected void preprocessTypeDecl(TypeDecl obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessTypeDecl(obj);
        }
    }

    @Override
    protected void postprocessTypeDecl(TypeDecl obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessTypeDecl(obj);
        }
    }

    @Override
    protected void preprocessTypeRef(TypeRef obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessTypeRef(obj);
        }
    }

    @Override
    protected void postprocessTypeRef(TypeRef obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessTypeRef(obj);
        }
    }

    @Override
    protected void preprocessUnaryExpression(UnaryExpression obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessUnaryExpression(obj);
        }
    }

    @Override
    protected void postprocessUnaryExpression(UnaryExpression obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessUnaryExpression(obj);
        }
    }

    @Override
    protected void preprocessUpdate(Update obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessUpdate(obj);
        }
    }

    @Override
    protected void postprocessUpdate(Update obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessUpdate(obj);
        }
    }

    @Override
    protected void preprocessVariableValue(VariableValue obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessVariableValue(obj);
        }
    }

    @Override
    protected void postprocessVariableValue(VariableValue obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessVariableValue(obj);
        }
    }

    @Override
    protected void preprocessVoidType(VoidType obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessVoidType(obj);
        }
    }

    @Override
    protected void postprocessVoidType(VoidType obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessVoidType(obj);
        }
    }

    @Override
    protected void preprocessWhileFuncStatement(WhileFuncStatement obj) {
        for (CifWalker walker: walkers) {
            walker.preprocessWhileFuncStatement(obj);
        }
    }

    @Override
    protected void postprocessWhileFuncStatement(WhileFuncStatement obj) {
        for (CifWalker walker: walkers) {
            walker.postprocessWhileFuncStatement(obj);
        }
    }
}
