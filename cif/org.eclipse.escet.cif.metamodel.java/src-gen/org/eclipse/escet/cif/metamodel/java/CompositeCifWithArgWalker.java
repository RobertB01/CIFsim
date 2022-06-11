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
 *
 * @param <T> The type of the extra argument provided to the walking, crawling
 *     and processing methods.
 */
public abstract class CompositeCifWithArgWalker<T> extends CifWithArgWalker<T> {
    /** The walkers to be composed by this composite walker. */
    private final CifWithArgWalker<T>[] walkers;

    /**
     * Constructor of the {@link CompositeCifWithArgWalker} class.
     *
     * @param walkers The walkers to be composed by this composite walker.
     */
    public CompositeCifWithArgWalker(CifWithArgWalker<T>[] walkers) {
        this.walkers = walkers;
    }

    @Override
    protected void preprocessAlgParameter(AlgParameter obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessAlgParameter(obj, arg);
        }
    }

    @Override
    protected void postprocessAlgParameter(AlgParameter obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessAlgParameter(obj, arg);
        }
    }

    @Override
    protected void preprocessAlgVariable(AlgVariable obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessAlgVariable(obj, arg);
        }
    }

    @Override
    protected void postprocessAlgVariable(AlgVariable obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessAlgVariable(obj, arg);
        }
    }

    @Override
    protected void preprocessAlgVariableExpression(AlgVariableExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessAlgVariableExpression(obj, arg);
        }
    }

    @Override
    protected void postprocessAlgVariableExpression(AlgVariableExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessAlgVariableExpression(obj, arg);
        }
    }

    @Override
    protected void preprocessAlphabet(Alphabet obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessAlphabet(obj, arg);
        }
    }

    @Override
    protected void postprocessAlphabet(Alphabet obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessAlphabet(obj, arg);
        }
    }

    @Override
    protected void preprocessAssignment(Assignment obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessAssignment(obj, arg);
        }
    }

    @Override
    protected void postprocessAssignment(Assignment obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessAssignment(obj, arg);
        }
    }

    @Override
    protected void preprocessAssignmentFuncStatement(AssignmentFuncStatement obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessAssignmentFuncStatement(obj, arg);
        }
    }

    @Override
    protected void postprocessAssignmentFuncStatement(AssignmentFuncStatement obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessAssignmentFuncStatement(obj, arg);
        }
    }

    @Override
    protected void preprocessAutomaton(Automaton obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessAutomaton(obj, arg);
        }
    }

    @Override
    protected void postprocessAutomaton(Automaton obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessAutomaton(obj, arg);
        }
    }

    @Override
    protected void preprocessBaseFunctionExpression(BaseFunctionExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessBaseFunctionExpression(obj, arg);
        }
    }

    @Override
    protected void postprocessBaseFunctionExpression(BaseFunctionExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessBaseFunctionExpression(obj, arg);
        }
    }

    @Override
    protected void preprocessBinaryExpression(BinaryExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessBinaryExpression(obj, arg);
        }
    }

    @Override
    protected void postprocessBinaryExpression(BinaryExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessBinaryExpression(obj, arg);
        }
    }

    @Override
    protected void preprocessBoolExpression(BoolExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessBoolExpression(obj, arg);
        }
    }

    @Override
    protected void postprocessBoolExpression(BoolExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessBoolExpression(obj, arg);
        }
    }

    @Override
    protected void preprocessBoolType(BoolType obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessBoolType(obj, arg);
        }
    }

    @Override
    protected void postprocessBoolType(BoolType obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessBoolType(obj, arg);
        }
    }

    @Override
    protected void preprocessBreakFuncStatement(BreakFuncStatement obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessBreakFuncStatement(obj, arg);
        }
    }

    @Override
    protected void postprocessBreakFuncStatement(BreakFuncStatement obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessBreakFuncStatement(obj, arg);
        }
    }

    @Override
    protected void preprocessCastExpression(CastExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessCastExpression(obj, arg);
        }
    }

    @Override
    protected void postprocessCastExpression(CastExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessCastExpression(obj, arg);
        }
    }

    @Override
    protected void preprocessCifType(CifType obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessCifType(obj, arg);
        }
    }

    @Override
    protected void postprocessCifType(CifType obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessCifType(obj, arg);
        }
    }

    @Override
    protected void preprocessCompInstWrapExpression(CompInstWrapExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessCompInstWrapExpression(obj, arg);
        }
    }

    @Override
    protected void postprocessCompInstWrapExpression(CompInstWrapExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessCompInstWrapExpression(obj, arg);
        }
    }

    @Override
    protected void preprocessCompInstWrapType(CompInstWrapType obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessCompInstWrapType(obj, arg);
        }
    }

    @Override
    protected void postprocessCompInstWrapType(CompInstWrapType obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessCompInstWrapType(obj, arg);
        }
    }

    @Override
    protected void preprocessCompParamExpression(CompParamExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessCompParamExpression(obj, arg);
        }
    }

    @Override
    protected void postprocessCompParamExpression(CompParamExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessCompParamExpression(obj, arg);
        }
    }

    @Override
    protected void preprocessCompParamWrapExpression(CompParamWrapExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessCompParamWrapExpression(obj, arg);
        }
    }

    @Override
    protected void postprocessCompParamWrapExpression(CompParamWrapExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessCompParamWrapExpression(obj, arg);
        }
    }

    @Override
    protected void preprocessCompParamWrapType(CompParamWrapType obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessCompParamWrapType(obj, arg);
        }
    }

    @Override
    protected void postprocessCompParamWrapType(CompParamWrapType obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessCompParamWrapType(obj, arg);
        }
    }

    @Override
    protected void preprocessComplexComponent(ComplexComponent obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessComplexComponent(obj, arg);
        }
    }

    @Override
    protected void postprocessComplexComponent(ComplexComponent obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessComplexComponent(obj, arg);
        }
    }

    @Override
    protected void preprocessComponent(Component obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessComponent(obj, arg);
        }
    }

    @Override
    protected void postprocessComponent(Component obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessComponent(obj, arg);
        }
    }

    @Override
    protected void preprocessComponentDef(ComponentDef obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessComponentDef(obj, arg);
        }
    }

    @Override
    protected void postprocessComponentDef(ComponentDef obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessComponentDef(obj, arg);
        }
    }

    @Override
    protected void preprocessComponentDefType(ComponentDefType obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessComponentDefType(obj, arg);
        }
    }

    @Override
    protected void postprocessComponentDefType(ComponentDefType obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessComponentDefType(obj, arg);
        }
    }

    @Override
    protected void preprocessComponentExpression(ComponentExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessComponentExpression(obj, arg);
        }
    }

    @Override
    protected void postprocessComponentExpression(ComponentExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessComponentExpression(obj, arg);
        }
    }

    @Override
    protected void preprocessComponentInst(ComponentInst obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessComponentInst(obj, arg);
        }
    }

    @Override
    protected void postprocessComponentInst(ComponentInst obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessComponentInst(obj, arg);
        }
    }

    @Override
    protected void preprocessComponentParameter(ComponentParameter obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessComponentParameter(obj, arg);
        }
    }

    @Override
    protected void postprocessComponentParameter(ComponentParameter obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessComponentParameter(obj, arg);
        }
    }

    @Override
    protected void preprocessComponentType(ComponentType obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessComponentType(obj, arg);
        }
    }

    @Override
    protected void postprocessComponentType(ComponentType obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessComponentType(obj, arg);
        }
    }

    @Override
    protected void preprocessConstant(Constant obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessConstant(obj, arg);
        }
    }

    @Override
    protected void postprocessConstant(Constant obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessConstant(obj, arg);
        }
    }

    @Override
    protected void preprocessConstantExpression(ConstantExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessConstantExpression(obj, arg);
        }
    }

    @Override
    protected void postprocessConstantExpression(ConstantExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessConstantExpression(obj, arg);
        }
    }

    @Override
    protected void preprocessContVariable(ContVariable obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessContVariable(obj, arg);
        }
    }

    @Override
    protected void postprocessContVariable(ContVariable obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessContVariable(obj, arg);
        }
    }

    @Override
    protected void preprocessContVariableExpression(ContVariableExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessContVariableExpression(obj, arg);
        }
    }

    @Override
    protected void postprocessContVariableExpression(ContVariableExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessContVariableExpression(obj, arg);
        }
    }

    @Override
    protected void preprocessContinueFuncStatement(ContinueFuncStatement obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessContinueFuncStatement(obj, arg);
        }
    }

    @Override
    protected void postprocessContinueFuncStatement(ContinueFuncStatement obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessContinueFuncStatement(obj, arg);
        }
    }

    @Override
    protected void preprocessDeclaration(Declaration obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessDeclaration(obj, arg);
        }
    }

    @Override
    protected void postprocessDeclaration(Declaration obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessDeclaration(obj, arg);
        }
    }

    @Override
    protected void preprocessDictExpression(DictExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessDictExpression(obj, arg);
        }
    }

    @Override
    protected void postprocessDictExpression(DictExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessDictExpression(obj, arg);
        }
    }

    @Override
    protected void preprocessDictPair(DictPair obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessDictPair(obj, arg);
        }
    }

    @Override
    protected void postprocessDictPair(DictPair obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessDictPair(obj, arg);
        }
    }

    @Override
    protected void preprocessDictType(DictType obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessDictType(obj, arg);
        }
    }

    @Override
    protected void postprocessDictType(DictType obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessDictType(obj, arg);
        }
    }

    @Override
    protected void preprocessDiscVariable(DiscVariable obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessDiscVariable(obj, arg);
        }
    }

    @Override
    protected void postprocessDiscVariable(DiscVariable obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessDiscVariable(obj, arg);
        }
    }

    @Override
    protected void preprocessDiscVariableExpression(DiscVariableExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessDiscVariableExpression(obj, arg);
        }
    }

    @Override
    protected void postprocessDiscVariableExpression(DiscVariableExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessDiscVariableExpression(obj, arg);
        }
    }

    @Override
    protected void preprocessDistType(DistType obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessDistType(obj, arg);
        }
    }

    @Override
    protected void postprocessDistType(DistType obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessDistType(obj, arg);
        }
    }

    @Override
    protected void preprocessEdge(Edge obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessEdge(obj, arg);
        }
    }

    @Override
    protected void postprocessEdge(Edge obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessEdge(obj, arg);
        }
    }

    @Override
    protected void preprocessEdgeEvent(EdgeEvent obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessEdgeEvent(obj, arg);
        }
    }

    @Override
    protected void postprocessEdgeEvent(EdgeEvent obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessEdgeEvent(obj, arg);
        }
    }

    @Override
    protected void preprocessEdgeReceive(EdgeReceive obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessEdgeReceive(obj, arg);
        }
    }

    @Override
    protected void postprocessEdgeReceive(EdgeReceive obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessEdgeReceive(obj, arg);
        }
    }

    @Override
    protected void preprocessEdgeSend(EdgeSend obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessEdgeSend(obj, arg);
        }
    }

    @Override
    protected void postprocessEdgeSend(EdgeSend obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessEdgeSend(obj, arg);
        }
    }

    @Override
    protected void preprocessElifExpression(ElifExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessElifExpression(obj, arg);
        }
    }

    @Override
    protected void postprocessElifExpression(ElifExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessElifExpression(obj, arg);
        }
    }

    @Override
    protected void preprocessElifFuncStatement(ElifFuncStatement obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessElifFuncStatement(obj, arg);
        }
    }

    @Override
    protected void postprocessElifFuncStatement(ElifFuncStatement obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessElifFuncStatement(obj, arg);
        }
    }

    @Override
    protected void preprocessElifUpdate(ElifUpdate obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessElifUpdate(obj, arg);
        }
    }

    @Override
    protected void postprocessElifUpdate(ElifUpdate obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessElifUpdate(obj, arg);
        }
    }

    @Override
    protected void preprocessEnumDecl(EnumDecl obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessEnumDecl(obj, arg);
        }
    }

    @Override
    protected void postprocessEnumDecl(EnumDecl obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessEnumDecl(obj, arg);
        }
    }

    @Override
    protected void preprocessEnumLiteral(EnumLiteral obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessEnumLiteral(obj, arg);
        }
    }

    @Override
    protected void postprocessEnumLiteral(EnumLiteral obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessEnumLiteral(obj, arg);
        }
    }

    @Override
    protected void preprocessEnumLiteralExpression(EnumLiteralExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessEnumLiteralExpression(obj, arg);
        }
    }

    @Override
    protected void postprocessEnumLiteralExpression(EnumLiteralExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessEnumLiteralExpression(obj, arg);
        }
    }

    @Override
    protected void preprocessEnumType(EnumType obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessEnumType(obj, arg);
        }
    }

    @Override
    protected void postprocessEnumType(EnumType obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessEnumType(obj, arg);
        }
    }

    @Override
    protected void preprocessEquation(Equation obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessEquation(obj, arg);
        }
    }

    @Override
    protected void postprocessEquation(Equation obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessEquation(obj, arg);
        }
    }

    @Override
    protected void preprocessEvent(Event obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessEvent(obj, arg);
        }
    }

    @Override
    protected void postprocessEvent(Event obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessEvent(obj, arg);
        }
    }

    @Override
    protected void preprocessEventExpression(EventExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessEventExpression(obj, arg);
        }
    }

    @Override
    protected void postprocessEventExpression(EventExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessEventExpression(obj, arg);
        }
    }

    @Override
    protected void preprocessEventParameter(EventParameter obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessEventParameter(obj, arg);
        }
    }

    @Override
    protected void postprocessEventParameter(EventParameter obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessEventParameter(obj, arg);
        }
    }

    @Override
    protected void preprocessExpression(Expression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessExpression(obj, arg);
        }
    }

    @Override
    protected void postprocessExpression(Expression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessExpression(obj, arg);
        }
    }

    @Override
    protected void preprocessExternalFunction(ExternalFunction obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessExternalFunction(obj, arg);
        }
    }

    @Override
    protected void postprocessExternalFunction(ExternalFunction obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessExternalFunction(obj, arg);
        }
    }

    @Override
    protected void preprocessField(Field obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessField(obj, arg);
        }
    }

    @Override
    protected void postprocessField(Field obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessField(obj, arg);
        }
    }

    @Override
    protected void preprocessFieldExpression(FieldExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessFieldExpression(obj, arg);
        }
    }

    @Override
    protected void postprocessFieldExpression(FieldExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessFieldExpression(obj, arg);
        }
    }

    @Override
    protected void preprocessFuncType(FuncType obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessFuncType(obj, arg);
        }
    }

    @Override
    protected void postprocessFuncType(FuncType obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessFuncType(obj, arg);
        }
    }

    @Override
    protected void preprocessFunction(Function obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessFunction(obj, arg);
        }
    }

    @Override
    protected void postprocessFunction(Function obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessFunction(obj, arg);
        }
    }

    @Override
    protected void preprocessFunctionCallExpression(FunctionCallExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessFunctionCallExpression(obj, arg);
        }
    }

    @Override
    protected void postprocessFunctionCallExpression(FunctionCallExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessFunctionCallExpression(obj, arg);
        }
    }

    @Override
    protected void preprocessFunctionExpression(FunctionExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessFunctionExpression(obj, arg);
        }
    }

    @Override
    protected void postprocessFunctionExpression(FunctionExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessFunctionExpression(obj, arg);
        }
    }

    @Override
    protected void preprocessFunctionParameter(FunctionParameter obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessFunctionParameter(obj, arg);
        }
    }

    @Override
    protected void postprocessFunctionParameter(FunctionParameter obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessFunctionParameter(obj, arg);
        }
    }

    @Override
    protected void preprocessFunctionStatement(FunctionStatement obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessFunctionStatement(obj, arg);
        }
    }

    @Override
    protected void postprocessFunctionStatement(FunctionStatement obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessFunctionStatement(obj, arg);
        }
    }

    @Override
    protected void preprocessGroup(Group obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessGroup(obj, arg);
        }
    }

    @Override
    protected void postprocessGroup(Group obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessGroup(obj, arg);
        }
    }

    @Override
    protected void preprocessIfExpression(IfExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessIfExpression(obj, arg);
        }
    }

    @Override
    protected void postprocessIfExpression(IfExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessIfExpression(obj, arg);
        }
    }

    @Override
    protected void preprocessIfFuncStatement(IfFuncStatement obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessIfFuncStatement(obj, arg);
        }
    }

    @Override
    protected void postprocessIfFuncStatement(IfFuncStatement obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessIfFuncStatement(obj, arg);
        }
    }

    @Override
    protected void preprocessIfUpdate(IfUpdate obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessIfUpdate(obj, arg);
        }
    }

    @Override
    protected void postprocessIfUpdate(IfUpdate obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessIfUpdate(obj, arg);
        }
    }

    @Override
    protected void preprocessInputVariable(InputVariable obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessInputVariable(obj, arg);
        }
    }

    @Override
    protected void postprocessInputVariable(InputVariable obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessInputVariable(obj, arg);
        }
    }

    @Override
    protected void preprocessInputVariableExpression(InputVariableExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessInputVariableExpression(obj, arg);
        }
    }

    @Override
    protected void postprocessInputVariableExpression(InputVariableExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessInputVariableExpression(obj, arg);
        }
    }

    @Override
    protected void preprocessIntExpression(IntExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessIntExpression(obj, arg);
        }
    }

    @Override
    protected void postprocessIntExpression(IntExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessIntExpression(obj, arg);
        }
    }

    @Override
    protected void preprocessIntType(IntType obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessIntType(obj, arg);
        }
    }

    @Override
    protected void postprocessIntType(IntType obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessIntType(obj, arg);
        }
    }

    @Override
    protected void preprocessInternalFunction(InternalFunction obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessInternalFunction(obj, arg);
        }
    }

    @Override
    protected void postprocessInternalFunction(InternalFunction obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessInternalFunction(obj, arg);
        }
    }

    @Override
    protected void preprocessInvariant(Invariant obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessInvariant(obj, arg);
        }
    }

    @Override
    protected void postprocessInvariant(Invariant obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessInvariant(obj, arg);
        }
    }

    @Override
    protected void preprocessIoDecl(IoDecl obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessIoDecl(obj, arg);
        }
    }

    @Override
    protected void postprocessIoDecl(IoDecl obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessIoDecl(obj, arg);
        }
    }

    @Override
    protected void preprocessListExpression(ListExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessListExpression(obj, arg);
        }
    }

    @Override
    protected void postprocessListExpression(ListExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessListExpression(obj, arg);
        }
    }

    @Override
    protected void preprocessListType(ListType obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessListType(obj, arg);
        }
    }

    @Override
    protected void postprocessListType(ListType obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessListType(obj, arg);
        }
    }

    @Override
    protected void preprocessLocation(Location obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessLocation(obj, arg);
        }
    }

    @Override
    protected void postprocessLocation(Location obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessLocation(obj, arg);
        }
    }

    @Override
    protected void preprocessLocationExpression(LocationExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessLocationExpression(obj, arg);
        }
    }

    @Override
    protected void postprocessLocationExpression(LocationExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessLocationExpression(obj, arg);
        }
    }

    @Override
    protected void preprocessLocationParameter(LocationParameter obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessLocationParameter(obj, arg);
        }
    }

    @Override
    protected void postprocessLocationParameter(LocationParameter obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessLocationParameter(obj, arg);
        }
    }

    @Override
    protected void preprocessMonitors(Monitors obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessMonitors(obj, arg);
        }
    }

    @Override
    protected void postprocessMonitors(Monitors obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessMonitors(obj, arg);
        }
    }

    @Override
    protected void preprocessParameter(Parameter obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessParameter(obj, arg);
        }
    }

    @Override
    protected void postprocessParameter(Parameter obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessParameter(obj, arg);
        }
    }

    @Override
    protected void preprocessPosition(Position obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessPosition(obj, arg);
        }
    }

    @Override
    protected void postprocessPosition(Position obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessPosition(obj, arg);
        }
    }

    @Override
    protected void preprocessPositionObject(PositionObject obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessPositionObject(obj, arg);
        }
    }

    @Override
    protected void postprocessPositionObject(PositionObject obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessPositionObject(obj, arg);
        }
    }

    @Override
    protected void preprocessPrint(Print obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessPrint(obj, arg);
        }
    }

    @Override
    protected void postprocessPrint(Print obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessPrint(obj, arg);
        }
    }

    @Override
    protected void preprocessPrintFile(PrintFile obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessPrintFile(obj, arg);
        }
    }

    @Override
    protected void postprocessPrintFile(PrintFile obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessPrintFile(obj, arg);
        }
    }

    @Override
    protected void preprocessPrintFor(PrintFor obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessPrintFor(obj, arg);
        }
    }

    @Override
    protected void postprocessPrintFor(PrintFor obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessPrintFor(obj, arg);
        }
    }

    @Override
    protected void preprocessProjectionExpression(ProjectionExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessProjectionExpression(obj, arg);
        }
    }

    @Override
    protected void postprocessProjectionExpression(ProjectionExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessProjectionExpression(obj, arg);
        }
    }

    @Override
    protected void preprocessRealExpression(RealExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessRealExpression(obj, arg);
        }
    }

    @Override
    protected void postprocessRealExpression(RealExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessRealExpression(obj, arg);
        }
    }

    @Override
    protected void preprocessRealType(RealType obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessRealType(obj, arg);
        }
    }

    @Override
    protected void postprocessRealType(RealType obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessRealType(obj, arg);
        }
    }

    @Override
    protected void preprocessReceivedExpression(ReceivedExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessReceivedExpression(obj, arg);
        }
    }

    @Override
    protected void postprocessReceivedExpression(ReceivedExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessReceivedExpression(obj, arg);
        }
    }

    @Override
    protected void preprocessReturnFuncStatement(ReturnFuncStatement obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessReturnFuncStatement(obj, arg);
        }
    }

    @Override
    protected void postprocessReturnFuncStatement(ReturnFuncStatement obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessReturnFuncStatement(obj, arg);
        }
    }

    @Override
    protected void preprocessSelfExpression(SelfExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessSelfExpression(obj, arg);
        }
    }

    @Override
    protected void postprocessSelfExpression(SelfExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessSelfExpression(obj, arg);
        }
    }

    @Override
    protected void preprocessSetExpression(SetExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessSetExpression(obj, arg);
        }
    }

    @Override
    protected void postprocessSetExpression(SetExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessSetExpression(obj, arg);
        }
    }

    @Override
    protected void preprocessSetType(SetType obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessSetType(obj, arg);
        }
    }

    @Override
    protected void postprocessSetType(SetType obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessSetType(obj, arg);
        }
    }

    @Override
    protected void preprocessSliceExpression(SliceExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessSliceExpression(obj, arg);
        }
    }

    @Override
    protected void postprocessSliceExpression(SliceExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessSliceExpression(obj, arg);
        }
    }

    @Override
    protected void preprocessSpecification(Specification obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessSpecification(obj, arg);
        }
    }

    @Override
    protected void postprocessSpecification(Specification obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessSpecification(obj, arg);
        }
    }

    @Override
    protected void preprocessStdLibFunctionExpression(StdLibFunctionExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessStdLibFunctionExpression(obj, arg);
        }
    }

    @Override
    protected void postprocessStdLibFunctionExpression(StdLibFunctionExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessStdLibFunctionExpression(obj, arg);
        }
    }

    @Override
    protected void preprocessStringExpression(StringExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessStringExpression(obj, arg);
        }
    }

    @Override
    protected void postprocessStringExpression(StringExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessStringExpression(obj, arg);
        }
    }

    @Override
    protected void preprocessStringType(StringType obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessStringType(obj, arg);
        }
    }

    @Override
    protected void postprocessStringType(StringType obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessStringType(obj, arg);
        }
    }

    @Override
    protected void preprocessSvgCopy(SvgCopy obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessSvgCopy(obj, arg);
        }
    }

    @Override
    protected void postprocessSvgCopy(SvgCopy obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessSvgCopy(obj, arg);
        }
    }

    @Override
    protected void preprocessSvgFile(SvgFile obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessSvgFile(obj, arg);
        }
    }

    @Override
    protected void postprocessSvgFile(SvgFile obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessSvgFile(obj, arg);
        }
    }

    @Override
    protected void preprocessSvgIn(SvgIn obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessSvgIn(obj, arg);
        }
    }

    @Override
    protected void postprocessSvgIn(SvgIn obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessSvgIn(obj, arg);
        }
    }

    @Override
    protected void preprocessSvgInEvent(SvgInEvent obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessSvgInEvent(obj, arg);
        }
    }

    @Override
    protected void postprocessSvgInEvent(SvgInEvent obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessSvgInEvent(obj, arg);
        }
    }

    @Override
    protected void preprocessSvgInEventIf(SvgInEventIf obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessSvgInEventIf(obj, arg);
        }
    }

    @Override
    protected void postprocessSvgInEventIf(SvgInEventIf obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessSvgInEventIf(obj, arg);
        }
    }

    @Override
    protected void preprocessSvgInEventIfEntry(SvgInEventIfEntry obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessSvgInEventIfEntry(obj, arg);
        }
    }

    @Override
    protected void postprocessSvgInEventIfEntry(SvgInEventIfEntry obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessSvgInEventIfEntry(obj, arg);
        }
    }

    @Override
    protected void preprocessSvgInEventSingle(SvgInEventSingle obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessSvgInEventSingle(obj, arg);
        }
    }

    @Override
    protected void postprocessSvgInEventSingle(SvgInEventSingle obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessSvgInEventSingle(obj, arg);
        }
    }

    @Override
    protected void preprocessSvgMove(SvgMove obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessSvgMove(obj, arg);
        }
    }

    @Override
    protected void postprocessSvgMove(SvgMove obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessSvgMove(obj, arg);
        }
    }

    @Override
    protected void preprocessSvgOut(SvgOut obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessSvgOut(obj, arg);
        }
    }

    @Override
    protected void postprocessSvgOut(SvgOut obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessSvgOut(obj, arg);
        }
    }

    @Override
    protected void preprocessSwitchCase(SwitchCase obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessSwitchCase(obj, arg);
        }
    }

    @Override
    protected void postprocessSwitchCase(SwitchCase obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessSwitchCase(obj, arg);
        }
    }

    @Override
    protected void preprocessSwitchExpression(SwitchExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessSwitchExpression(obj, arg);
        }
    }

    @Override
    protected void postprocessSwitchExpression(SwitchExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessSwitchExpression(obj, arg);
        }
    }

    @Override
    protected void preprocessTauExpression(TauExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessTauExpression(obj, arg);
        }
    }

    @Override
    protected void postprocessTauExpression(TauExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessTauExpression(obj, arg);
        }
    }

    @Override
    protected void preprocessTimeExpression(TimeExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessTimeExpression(obj, arg);
        }
    }

    @Override
    protected void postprocessTimeExpression(TimeExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessTimeExpression(obj, arg);
        }
    }

    @Override
    protected void preprocessTupleExpression(TupleExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessTupleExpression(obj, arg);
        }
    }

    @Override
    protected void postprocessTupleExpression(TupleExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessTupleExpression(obj, arg);
        }
    }

    @Override
    protected void preprocessTupleType(TupleType obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessTupleType(obj, arg);
        }
    }

    @Override
    protected void postprocessTupleType(TupleType obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessTupleType(obj, arg);
        }
    }

    @Override
    protected void preprocessTypeDecl(TypeDecl obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessTypeDecl(obj, arg);
        }
    }

    @Override
    protected void postprocessTypeDecl(TypeDecl obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessTypeDecl(obj, arg);
        }
    }

    @Override
    protected void preprocessTypeRef(TypeRef obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessTypeRef(obj, arg);
        }
    }

    @Override
    protected void postprocessTypeRef(TypeRef obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessTypeRef(obj, arg);
        }
    }

    @Override
    protected void preprocessUnaryExpression(UnaryExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessUnaryExpression(obj, arg);
        }
    }

    @Override
    protected void postprocessUnaryExpression(UnaryExpression obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessUnaryExpression(obj, arg);
        }
    }

    @Override
    protected void preprocessUpdate(Update obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessUpdate(obj, arg);
        }
    }

    @Override
    protected void postprocessUpdate(Update obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessUpdate(obj, arg);
        }
    }

    @Override
    protected void preprocessVariableValue(VariableValue obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessVariableValue(obj, arg);
        }
    }

    @Override
    protected void postprocessVariableValue(VariableValue obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessVariableValue(obj, arg);
        }
    }

    @Override
    protected void preprocessVoidType(VoidType obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessVoidType(obj, arg);
        }
    }

    @Override
    protected void postprocessVoidType(VoidType obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessVoidType(obj, arg);
        }
    }

    @Override
    protected void preprocessWhileFuncStatement(WhileFuncStatement obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.preprocessWhileFuncStatement(obj, arg);
        }
    }

    @Override
    protected void postprocessWhileFuncStatement(WhileFuncStatement obj, T arg) {
        for (CifWithArgWalker<T> walker: walkers) {
            walker.postprocessWhileFuncStatement(obj, arg);
        }
    }
}
