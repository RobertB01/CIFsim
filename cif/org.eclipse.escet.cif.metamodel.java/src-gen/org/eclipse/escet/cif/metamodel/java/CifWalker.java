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

import java.util.List;

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
import org.eclipse.escet.cif.metamodel.cif.automata.impl.EdgeEventImpl;
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
import org.eclipse.escet.cif.metamodel.cif.impl.GroupImpl;
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
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/**
 * A walker for "cif" models.
 *
 * <p>The walker works as follows:
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
 * <p>By default, the pre-processing and post-processing methods do nothing
 * (they have an empty implementation). It is up to derived classes to
 * override methods and provide actual implementations. They may also override
 * walk and crawl methods, if desired.</p>
 *
 * <p>This abstract walker class has no public methods. It is up to the derived
 * classes to add a public method as entry method. They can decide which
 * classes are to be used as starting point, and they can give the public
 * method a proper name, parameters, etc. They may even allow multiple public
 * methods to allow starting from multiple classes.</p>
 */
public abstract class CifWalker {
    /**
     * Walking function for the {@link AlgParameter} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkAlgParameter(AlgParameter obj) {
        precrawlAlgParameter(obj);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        AlgVariable _variable = obj.getVariable();
        walkAlgVariable(_variable);
        postcrawlAlgParameter(obj);
    }

    /**
     * Pre-crawling function for the {@link AlgParameter} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlAlgParameter(AlgParameter obj) {
        precrawlParameter(obj);
        preprocessAlgParameter(obj);
    }

    /**
     * Post-crawling function for the {@link AlgParameter} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlAlgParameter(AlgParameter obj) {
        postprocessAlgParameter(obj);
        postcrawlParameter(obj);
    }

    /**
     * Pre-processing function for the {@link AlgParameter} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessAlgParameter(AlgParameter obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link AlgParameter} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessAlgParameter(AlgParameter obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link AlgVariable} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkAlgVariable(AlgVariable obj) {
        precrawlAlgVariable(obj);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        CifType _type = obj.getType();
        walkCifType(_type);
        Expression _value = obj.getValue();
        if (_value != null) {
            walkExpression(_value);
        }
        postcrawlAlgVariable(obj);
    }

    /**
     * Pre-crawling function for the {@link AlgVariable} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlAlgVariable(AlgVariable obj) {
        precrawlDeclaration(obj);
        preprocessAlgVariable(obj);
    }

    /**
     * Post-crawling function for the {@link AlgVariable} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlAlgVariable(AlgVariable obj) {
        postprocessAlgVariable(obj);
        postcrawlDeclaration(obj);
    }

    /**
     * Pre-processing function for the {@link AlgVariable} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessAlgVariable(AlgVariable obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link AlgVariable} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessAlgVariable(AlgVariable obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link AlgVariableExpression} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkAlgVariableExpression(AlgVariableExpression obj) {
        precrawlAlgVariableExpression(obj);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        CifType _type = obj.getType();
        walkCifType(_type);
        postcrawlAlgVariableExpression(obj);
    }

    /**
     * Pre-crawling function for the {@link AlgVariableExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlAlgVariableExpression(AlgVariableExpression obj) {
        precrawlExpression(obj);
        preprocessAlgVariableExpression(obj);
    }

    /**
     * Post-crawling function for the {@link AlgVariableExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlAlgVariableExpression(AlgVariableExpression obj) {
        postprocessAlgVariableExpression(obj);
        postcrawlExpression(obj);
    }

    /**
     * Pre-processing function for the {@link AlgVariableExpression} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessAlgVariableExpression(AlgVariableExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link AlgVariableExpression} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessAlgVariableExpression(AlgVariableExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link Alphabet} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkAlphabet(Alphabet obj) {
        precrawlAlphabet(obj);
        List<Expression> _events = obj.getEvents();
        for (Expression x: _events) {
            walkExpression(x);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        postcrawlAlphabet(obj);
    }

    /**
     * Pre-crawling function for the {@link Alphabet} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlAlphabet(Alphabet obj) {
        precrawlPositionObject(obj);
        preprocessAlphabet(obj);
    }

    /**
     * Post-crawling function for the {@link Alphabet} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlAlphabet(Alphabet obj) {
        postprocessAlphabet(obj);
        postcrawlPositionObject(obj);
    }

    /**
     * Pre-processing function for the {@link Alphabet} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessAlphabet(Alphabet obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link Alphabet} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessAlphabet(Alphabet obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link Assignment} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkAssignment(Assignment obj) {
        precrawlAssignment(obj);
        Expression _addressable = obj.getAddressable();
        walkExpression(_addressable);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        Expression _value = obj.getValue();
        walkExpression(_value);
        postcrawlAssignment(obj);
    }

    /**
     * Pre-crawling function for the {@link Assignment} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlAssignment(Assignment obj) {
        precrawlUpdate(obj);
        preprocessAssignment(obj);
    }

    /**
     * Post-crawling function for the {@link Assignment} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlAssignment(Assignment obj) {
        postprocessAssignment(obj);
        postcrawlUpdate(obj);
    }

    /**
     * Pre-processing function for the {@link Assignment} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessAssignment(Assignment obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link Assignment} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessAssignment(Assignment obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link AssignmentFuncStatement} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkAssignmentFuncStatement(AssignmentFuncStatement obj) {
        precrawlAssignmentFuncStatement(obj);
        Expression _addressable = obj.getAddressable();
        walkExpression(_addressable);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        Expression _value = obj.getValue();
        walkExpression(_value);
        postcrawlAssignmentFuncStatement(obj);
    }

    /**
     * Pre-crawling function for the {@link AssignmentFuncStatement} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlAssignmentFuncStatement(AssignmentFuncStatement obj) {
        precrawlFunctionStatement(obj);
        preprocessAssignmentFuncStatement(obj);
    }

    /**
     * Post-crawling function for the {@link AssignmentFuncStatement} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlAssignmentFuncStatement(AssignmentFuncStatement obj) {
        postprocessAssignmentFuncStatement(obj);
        postcrawlFunctionStatement(obj);
    }

    /**
     * Pre-processing function for the {@link AssignmentFuncStatement} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessAssignmentFuncStatement(AssignmentFuncStatement obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link AssignmentFuncStatement} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessAssignmentFuncStatement(AssignmentFuncStatement obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link Automaton} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkAutomaton(Automaton obj) {
        precrawlAutomaton(obj);
        Alphabet _alphabet = obj.getAlphabet();
        if (_alphabet != null) {
            walkAlphabet(_alphabet);
        }
        List<Declaration> _declarations = obj.getDeclarations();
        for (Declaration x: _declarations) {
            walkDeclaration(x);
        }
        List<Equation> _equations = obj.getEquations();
        for (Equation x: _equations) {
            walkEquation(x);
        }
        List<Expression> _initials = obj.getInitials();
        for (Expression x: _initials) {
            walkExpression(x);
        }
        List<Invariant> _invariants = obj.getInvariants();
        for (Invariant x: _invariants) {
            walkInvariant(x);
        }
        List<IoDecl> _ioDecls = obj.getIoDecls();
        for (IoDecl x: _ioDecls) {
            walkIoDecl(x);
        }
        List<Location> _locations = obj.getLocations();
        for (Location x: _locations) {
            walkLocation(x);
        }
        List<Expression> _markeds = obj.getMarkeds();
        for (Expression x: _markeds) {
            walkExpression(x);
        }
        Monitors _monitors = obj.getMonitors();
        if (_monitors != null) {
            walkMonitors(_monitors);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        postcrawlAutomaton(obj);
    }

    /**
     * Pre-crawling function for the {@link Automaton} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlAutomaton(Automaton obj) {
        precrawlComplexComponent(obj);
        preprocessAutomaton(obj);
    }

    /**
     * Post-crawling function for the {@link Automaton} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlAutomaton(Automaton obj) {
        postprocessAutomaton(obj);
        postcrawlComplexComponent(obj);
    }

    /**
     * Pre-processing function for the {@link Automaton} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessAutomaton(Automaton obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link Automaton} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessAutomaton(Automaton obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link BaseFunctionExpression} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkBaseFunctionExpression(BaseFunctionExpression obj) {
        if (obj instanceof FunctionExpression) {
            walkFunctionExpression((FunctionExpression)obj);
            return;
        }
        if (obj instanceof StdLibFunctionExpression) {
            walkStdLibFunctionExpression((StdLibFunctionExpression)obj);
            return;
        }
        String msg = "No redirect; unexpected object type: " + obj;
        throw new IllegalArgumentException(msg);
    }

    /**
     * Pre-crawling function for the {@link BaseFunctionExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlBaseFunctionExpression(BaseFunctionExpression obj) {
        precrawlExpression(obj);
        preprocessBaseFunctionExpression(obj);
    }

    /**
     * Post-crawling function for the {@link BaseFunctionExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlBaseFunctionExpression(BaseFunctionExpression obj) {
        postprocessBaseFunctionExpression(obj);
        postcrawlExpression(obj);
    }

    /**
     * Pre-processing function for the {@link BaseFunctionExpression} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessBaseFunctionExpression(BaseFunctionExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link BaseFunctionExpression} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessBaseFunctionExpression(BaseFunctionExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link BinaryExpression} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkBinaryExpression(BinaryExpression obj) {
        precrawlBinaryExpression(obj);
        Expression _left = obj.getLeft();
        walkExpression(_left);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        Expression _right = obj.getRight();
        walkExpression(_right);
        CifType _type = obj.getType();
        walkCifType(_type);
        postcrawlBinaryExpression(obj);
    }

    /**
     * Pre-crawling function for the {@link BinaryExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlBinaryExpression(BinaryExpression obj) {
        precrawlExpression(obj);
        preprocessBinaryExpression(obj);
    }

    /**
     * Post-crawling function for the {@link BinaryExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlBinaryExpression(BinaryExpression obj) {
        postprocessBinaryExpression(obj);
        postcrawlExpression(obj);
    }

    /**
     * Pre-processing function for the {@link BinaryExpression} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessBinaryExpression(BinaryExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link BinaryExpression} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessBinaryExpression(BinaryExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link BoolExpression} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkBoolExpression(BoolExpression obj) {
        precrawlBoolExpression(obj);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        CifType _type = obj.getType();
        walkCifType(_type);
        postcrawlBoolExpression(obj);
    }

    /**
     * Pre-crawling function for the {@link BoolExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlBoolExpression(BoolExpression obj) {
        precrawlExpression(obj);
        preprocessBoolExpression(obj);
    }

    /**
     * Post-crawling function for the {@link BoolExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlBoolExpression(BoolExpression obj) {
        postprocessBoolExpression(obj);
        postcrawlExpression(obj);
    }

    /**
     * Pre-processing function for the {@link BoolExpression} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessBoolExpression(BoolExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link BoolExpression} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessBoolExpression(BoolExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link BoolType} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkBoolType(BoolType obj) {
        precrawlBoolType(obj);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        postcrawlBoolType(obj);
    }

    /**
     * Pre-crawling function for the {@link BoolType} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlBoolType(BoolType obj) {
        precrawlCifType(obj);
        preprocessBoolType(obj);
    }

    /**
     * Post-crawling function for the {@link BoolType} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlBoolType(BoolType obj) {
        postprocessBoolType(obj);
        postcrawlCifType(obj);
    }

    /**
     * Pre-processing function for the {@link BoolType} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessBoolType(BoolType obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link BoolType} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessBoolType(BoolType obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link BreakFuncStatement} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkBreakFuncStatement(BreakFuncStatement obj) {
        precrawlBreakFuncStatement(obj);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        postcrawlBreakFuncStatement(obj);
    }

    /**
     * Pre-crawling function for the {@link BreakFuncStatement} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlBreakFuncStatement(BreakFuncStatement obj) {
        precrawlFunctionStatement(obj);
        preprocessBreakFuncStatement(obj);
    }

    /**
     * Post-crawling function for the {@link BreakFuncStatement} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlBreakFuncStatement(BreakFuncStatement obj) {
        postprocessBreakFuncStatement(obj);
        postcrawlFunctionStatement(obj);
    }

    /**
     * Pre-processing function for the {@link BreakFuncStatement} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessBreakFuncStatement(BreakFuncStatement obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link BreakFuncStatement} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessBreakFuncStatement(BreakFuncStatement obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link CastExpression} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkCastExpression(CastExpression obj) {
        precrawlCastExpression(obj);
        Expression _child = obj.getChild();
        walkExpression(_child);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        CifType _type = obj.getType();
        walkCifType(_type);
        postcrawlCastExpression(obj);
    }

    /**
     * Pre-crawling function for the {@link CastExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlCastExpression(CastExpression obj) {
        precrawlExpression(obj);
        preprocessCastExpression(obj);
    }

    /**
     * Post-crawling function for the {@link CastExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlCastExpression(CastExpression obj) {
        postprocessCastExpression(obj);
        postcrawlExpression(obj);
    }

    /**
     * Pre-processing function for the {@link CastExpression} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessCastExpression(CastExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link CastExpression} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessCastExpression(CastExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link CifType} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkCifType(CifType obj) {
        if (obj instanceof BoolType) {
            walkBoolType((BoolType)obj);
            return;
        }
        if (obj instanceof CompInstWrapType) {
            walkCompInstWrapType((CompInstWrapType)obj);
            return;
        }
        if (obj instanceof CompParamWrapType) {
            walkCompParamWrapType((CompParamWrapType)obj);
            return;
        }
        if (obj instanceof ComponentDefType) {
            walkComponentDefType((ComponentDefType)obj);
            return;
        }
        if (obj instanceof ComponentType) {
            walkComponentType((ComponentType)obj);
            return;
        }
        if (obj instanceof DictType) {
            walkDictType((DictType)obj);
            return;
        }
        if (obj instanceof DistType) {
            walkDistType((DistType)obj);
            return;
        }
        if (obj instanceof EnumType) {
            walkEnumType((EnumType)obj);
            return;
        }
        if (obj instanceof FuncType) {
            walkFuncType((FuncType)obj);
            return;
        }
        if (obj instanceof IntType) {
            walkIntType((IntType)obj);
            return;
        }
        if (obj instanceof ListType) {
            walkListType((ListType)obj);
            return;
        }
        if (obj instanceof RealType) {
            walkRealType((RealType)obj);
            return;
        }
        if (obj instanceof SetType) {
            walkSetType((SetType)obj);
            return;
        }
        if (obj instanceof StringType) {
            walkStringType((StringType)obj);
            return;
        }
        if (obj instanceof TupleType) {
            walkTupleType((TupleType)obj);
            return;
        }
        if (obj instanceof TypeRef) {
            walkTypeRef((TypeRef)obj);
            return;
        }
        if (obj instanceof VoidType) {
            walkVoidType((VoidType)obj);
            return;
        }
        String msg = "No redirect; unexpected object type: " + obj;
        throw new IllegalArgumentException(msg);
    }

    /**
     * Pre-crawling function for the {@link CifType} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlCifType(CifType obj) {
        precrawlPositionObject(obj);
        preprocessCifType(obj);
    }

    /**
     * Post-crawling function for the {@link CifType} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlCifType(CifType obj) {
        postprocessCifType(obj);
        postcrawlPositionObject(obj);
    }

    /**
     * Pre-processing function for the {@link CifType} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessCifType(CifType obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link CifType} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessCifType(CifType obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link CompInstWrapExpression} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkCompInstWrapExpression(CompInstWrapExpression obj) {
        precrawlCompInstWrapExpression(obj);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        Expression _reference = obj.getReference();
        walkExpression(_reference);
        CifType _type = obj.getType();
        walkCifType(_type);
        postcrawlCompInstWrapExpression(obj);
    }

    /**
     * Pre-crawling function for the {@link CompInstWrapExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlCompInstWrapExpression(CompInstWrapExpression obj) {
        precrawlExpression(obj);
        preprocessCompInstWrapExpression(obj);
    }

    /**
     * Post-crawling function for the {@link CompInstWrapExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlCompInstWrapExpression(CompInstWrapExpression obj) {
        postprocessCompInstWrapExpression(obj);
        postcrawlExpression(obj);
    }

    /**
     * Pre-processing function for the {@link CompInstWrapExpression} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessCompInstWrapExpression(CompInstWrapExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link CompInstWrapExpression} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessCompInstWrapExpression(CompInstWrapExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link CompInstWrapType} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkCompInstWrapType(CompInstWrapType obj) {
        precrawlCompInstWrapType(obj);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        CifType _reference = obj.getReference();
        walkCifType(_reference);
        postcrawlCompInstWrapType(obj);
    }

    /**
     * Pre-crawling function for the {@link CompInstWrapType} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlCompInstWrapType(CompInstWrapType obj) {
        precrawlCifType(obj);
        preprocessCompInstWrapType(obj);
    }

    /**
     * Post-crawling function for the {@link CompInstWrapType} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlCompInstWrapType(CompInstWrapType obj) {
        postprocessCompInstWrapType(obj);
        postcrawlCifType(obj);
    }

    /**
     * Pre-processing function for the {@link CompInstWrapType} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessCompInstWrapType(CompInstWrapType obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link CompInstWrapType} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessCompInstWrapType(CompInstWrapType obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link CompParamExpression} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkCompParamExpression(CompParamExpression obj) {
        precrawlCompParamExpression(obj);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        CifType _type = obj.getType();
        walkCifType(_type);
        postcrawlCompParamExpression(obj);
    }

    /**
     * Pre-crawling function for the {@link CompParamExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlCompParamExpression(CompParamExpression obj) {
        precrawlExpression(obj);
        preprocessCompParamExpression(obj);
    }

    /**
     * Post-crawling function for the {@link CompParamExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlCompParamExpression(CompParamExpression obj) {
        postprocessCompParamExpression(obj);
        postcrawlExpression(obj);
    }

    /**
     * Pre-processing function for the {@link CompParamExpression} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessCompParamExpression(CompParamExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link CompParamExpression} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessCompParamExpression(CompParamExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link CompParamWrapExpression} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkCompParamWrapExpression(CompParamWrapExpression obj) {
        precrawlCompParamWrapExpression(obj);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        Expression _reference = obj.getReference();
        walkExpression(_reference);
        CifType _type = obj.getType();
        walkCifType(_type);
        postcrawlCompParamWrapExpression(obj);
    }

    /**
     * Pre-crawling function for the {@link CompParamWrapExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlCompParamWrapExpression(CompParamWrapExpression obj) {
        precrawlExpression(obj);
        preprocessCompParamWrapExpression(obj);
    }

    /**
     * Post-crawling function for the {@link CompParamWrapExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlCompParamWrapExpression(CompParamWrapExpression obj) {
        postprocessCompParamWrapExpression(obj);
        postcrawlExpression(obj);
    }

    /**
     * Pre-processing function for the {@link CompParamWrapExpression} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessCompParamWrapExpression(CompParamWrapExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link CompParamWrapExpression} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessCompParamWrapExpression(CompParamWrapExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link CompParamWrapType} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkCompParamWrapType(CompParamWrapType obj) {
        precrawlCompParamWrapType(obj);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        CifType _reference = obj.getReference();
        walkCifType(_reference);
        postcrawlCompParamWrapType(obj);
    }

    /**
     * Pre-crawling function for the {@link CompParamWrapType} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlCompParamWrapType(CompParamWrapType obj) {
        precrawlCifType(obj);
        preprocessCompParamWrapType(obj);
    }

    /**
     * Post-crawling function for the {@link CompParamWrapType} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlCompParamWrapType(CompParamWrapType obj) {
        postprocessCompParamWrapType(obj);
        postcrawlCifType(obj);
    }

    /**
     * Pre-processing function for the {@link CompParamWrapType} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessCompParamWrapType(CompParamWrapType obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link CompParamWrapType} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessCompParamWrapType(CompParamWrapType obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link ComplexComponent} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkComplexComponent(ComplexComponent obj) {
        if (obj instanceof Automaton) {
            walkAutomaton((Automaton)obj);
            return;
        }
        if (obj instanceof Group) {
            walkGroup((Group)obj);
            return;
        }
        String msg = "No redirect; unexpected object type: " + obj;
        throw new IllegalArgumentException(msg);
    }

    /**
     * Pre-crawling function for the {@link ComplexComponent} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlComplexComponent(ComplexComponent obj) {
        precrawlComponent(obj);
        preprocessComplexComponent(obj);
    }

    /**
     * Post-crawling function for the {@link ComplexComponent} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlComplexComponent(ComplexComponent obj) {
        postprocessComplexComponent(obj);
        postcrawlComponent(obj);
    }

    /**
     * Pre-processing function for the {@link ComplexComponent} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessComplexComponent(ComplexComponent obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link ComplexComponent} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessComplexComponent(ComplexComponent obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link Component} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkComponent(Component obj) {
        if (obj instanceof ComplexComponent) {
            walkComplexComponent((ComplexComponent)obj);
            return;
        }
        if (obj instanceof ComponentInst) {
            walkComponentInst((ComponentInst)obj);
            return;
        }
        String msg = "No redirect; unexpected object type: " + obj;
        throw new IllegalArgumentException(msg);
    }

    /**
     * Pre-crawling function for the {@link Component} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlComponent(Component obj) {
        precrawlPositionObject(obj);
        preprocessComponent(obj);
    }

    /**
     * Post-crawling function for the {@link Component} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlComponent(Component obj) {
        postprocessComponent(obj);
        postcrawlPositionObject(obj);
    }

    /**
     * Pre-processing function for the {@link Component} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessComponent(Component obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link Component} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessComponent(Component obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link ComponentDef} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkComponentDef(ComponentDef obj) {
        precrawlComponentDef(obj);
        ComplexComponent _body = obj.getBody();
        walkComplexComponent(_body);
        List<Parameter> _parameters = obj.getParameters();
        for (Parameter x: _parameters) {
            walkParameter(x);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        postcrawlComponentDef(obj);
    }

    /**
     * Pre-crawling function for the {@link ComponentDef} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlComponentDef(ComponentDef obj) {
        precrawlPositionObject(obj);
        preprocessComponentDef(obj);
    }

    /**
     * Post-crawling function for the {@link ComponentDef} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlComponentDef(ComponentDef obj) {
        postprocessComponentDef(obj);
        postcrawlPositionObject(obj);
    }

    /**
     * Pre-processing function for the {@link ComponentDef} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessComponentDef(ComponentDef obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link ComponentDef} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessComponentDef(ComponentDef obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link ComponentDefType} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkComponentDefType(ComponentDefType obj) {
        precrawlComponentDefType(obj);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        postcrawlComponentDefType(obj);
    }

    /**
     * Pre-crawling function for the {@link ComponentDefType} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlComponentDefType(ComponentDefType obj) {
        precrawlCifType(obj);
        preprocessComponentDefType(obj);
    }

    /**
     * Post-crawling function for the {@link ComponentDefType} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlComponentDefType(ComponentDefType obj) {
        postprocessComponentDefType(obj);
        postcrawlCifType(obj);
    }

    /**
     * Pre-processing function for the {@link ComponentDefType} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessComponentDefType(ComponentDefType obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link ComponentDefType} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessComponentDefType(ComponentDefType obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link ComponentExpression} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkComponentExpression(ComponentExpression obj) {
        precrawlComponentExpression(obj);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        CifType _type = obj.getType();
        walkCifType(_type);
        postcrawlComponentExpression(obj);
    }

    /**
     * Pre-crawling function for the {@link ComponentExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlComponentExpression(ComponentExpression obj) {
        precrawlExpression(obj);
        preprocessComponentExpression(obj);
    }

    /**
     * Post-crawling function for the {@link ComponentExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlComponentExpression(ComponentExpression obj) {
        postprocessComponentExpression(obj);
        postcrawlExpression(obj);
    }

    /**
     * Pre-processing function for the {@link ComponentExpression} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessComponentExpression(ComponentExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link ComponentExpression} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessComponentExpression(ComponentExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link ComponentInst} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkComponentInst(ComponentInst obj) {
        precrawlComponentInst(obj);
        CifType _definition = obj.getDefinition();
        walkCifType(_definition);
        List<Expression> _parameters = obj.getParameters();
        for (Expression x: _parameters) {
            walkExpression(x);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        postcrawlComponentInst(obj);
    }

    /**
     * Pre-crawling function for the {@link ComponentInst} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlComponentInst(ComponentInst obj) {
        precrawlComponent(obj);
        preprocessComponentInst(obj);
    }

    /**
     * Post-crawling function for the {@link ComponentInst} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlComponentInst(ComponentInst obj) {
        postprocessComponentInst(obj);
        postcrawlComponent(obj);
    }

    /**
     * Pre-processing function for the {@link ComponentInst} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessComponentInst(ComponentInst obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link ComponentInst} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessComponentInst(ComponentInst obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link ComponentParameter} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkComponentParameter(ComponentParameter obj) {
        precrawlComponentParameter(obj);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        CifType _type = obj.getType();
        walkCifType(_type);
        postcrawlComponentParameter(obj);
    }

    /**
     * Pre-crawling function for the {@link ComponentParameter} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlComponentParameter(ComponentParameter obj) {
        precrawlParameter(obj);
        preprocessComponentParameter(obj);
    }

    /**
     * Post-crawling function for the {@link ComponentParameter} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlComponentParameter(ComponentParameter obj) {
        postprocessComponentParameter(obj);
        postcrawlParameter(obj);
    }

    /**
     * Pre-processing function for the {@link ComponentParameter} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessComponentParameter(ComponentParameter obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link ComponentParameter} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessComponentParameter(ComponentParameter obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link ComponentType} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkComponentType(ComponentType obj) {
        precrawlComponentType(obj);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        postcrawlComponentType(obj);
    }

    /**
     * Pre-crawling function for the {@link ComponentType} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlComponentType(ComponentType obj) {
        precrawlCifType(obj);
        preprocessComponentType(obj);
    }

    /**
     * Post-crawling function for the {@link ComponentType} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlComponentType(ComponentType obj) {
        postprocessComponentType(obj);
        postcrawlCifType(obj);
    }

    /**
     * Pre-processing function for the {@link ComponentType} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessComponentType(ComponentType obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link ComponentType} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessComponentType(ComponentType obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link Constant} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkConstant(Constant obj) {
        precrawlConstant(obj);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        CifType _type = obj.getType();
        walkCifType(_type);
        Expression _value = obj.getValue();
        walkExpression(_value);
        postcrawlConstant(obj);
    }

    /**
     * Pre-crawling function for the {@link Constant} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlConstant(Constant obj) {
        precrawlDeclaration(obj);
        preprocessConstant(obj);
    }

    /**
     * Post-crawling function for the {@link Constant} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlConstant(Constant obj) {
        postprocessConstant(obj);
        postcrawlDeclaration(obj);
    }

    /**
     * Pre-processing function for the {@link Constant} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessConstant(Constant obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link Constant} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessConstant(Constant obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link ConstantExpression} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkConstantExpression(ConstantExpression obj) {
        precrawlConstantExpression(obj);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        CifType _type = obj.getType();
        walkCifType(_type);
        postcrawlConstantExpression(obj);
    }

    /**
     * Pre-crawling function for the {@link ConstantExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlConstantExpression(ConstantExpression obj) {
        precrawlExpression(obj);
        preprocessConstantExpression(obj);
    }

    /**
     * Post-crawling function for the {@link ConstantExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlConstantExpression(ConstantExpression obj) {
        postprocessConstantExpression(obj);
        postcrawlExpression(obj);
    }

    /**
     * Pre-processing function for the {@link ConstantExpression} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessConstantExpression(ConstantExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link ConstantExpression} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessConstantExpression(ConstantExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link ContVariable} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkContVariable(ContVariable obj) {
        precrawlContVariable(obj);
        Expression _derivative = obj.getDerivative();
        if (_derivative != null) {
            walkExpression(_derivative);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        Expression _value = obj.getValue();
        if (_value != null) {
            walkExpression(_value);
        }
        postcrawlContVariable(obj);
    }

    /**
     * Pre-crawling function for the {@link ContVariable} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlContVariable(ContVariable obj) {
        precrawlDeclaration(obj);
        preprocessContVariable(obj);
    }

    /**
     * Post-crawling function for the {@link ContVariable} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlContVariable(ContVariable obj) {
        postprocessContVariable(obj);
        postcrawlDeclaration(obj);
    }

    /**
     * Pre-processing function for the {@link ContVariable} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessContVariable(ContVariable obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link ContVariable} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessContVariable(ContVariable obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link ContVariableExpression} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkContVariableExpression(ContVariableExpression obj) {
        precrawlContVariableExpression(obj);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        CifType _type = obj.getType();
        walkCifType(_type);
        postcrawlContVariableExpression(obj);
    }

    /**
     * Pre-crawling function for the {@link ContVariableExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlContVariableExpression(ContVariableExpression obj) {
        precrawlExpression(obj);
        preprocessContVariableExpression(obj);
    }

    /**
     * Post-crawling function for the {@link ContVariableExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlContVariableExpression(ContVariableExpression obj) {
        postprocessContVariableExpression(obj);
        postcrawlExpression(obj);
    }

    /**
     * Pre-processing function for the {@link ContVariableExpression} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessContVariableExpression(ContVariableExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link ContVariableExpression} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessContVariableExpression(ContVariableExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link ContinueFuncStatement} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkContinueFuncStatement(ContinueFuncStatement obj) {
        precrawlContinueFuncStatement(obj);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        postcrawlContinueFuncStatement(obj);
    }

    /**
     * Pre-crawling function for the {@link ContinueFuncStatement} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlContinueFuncStatement(ContinueFuncStatement obj) {
        precrawlFunctionStatement(obj);
        preprocessContinueFuncStatement(obj);
    }

    /**
     * Post-crawling function for the {@link ContinueFuncStatement} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlContinueFuncStatement(ContinueFuncStatement obj) {
        postprocessContinueFuncStatement(obj);
        postcrawlFunctionStatement(obj);
    }

    /**
     * Pre-processing function for the {@link ContinueFuncStatement} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessContinueFuncStatement(ContinueFuncStatement obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link ContinueFuncStatement} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessContinueFuncStatement(ContinueFuncStatement obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link Declaration} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkDeclaration(Declaration obj) {
        if (obj instanceof AlgVariable) {
            walkAlgVariable((AlgVariable)obj);
            return;
        }
        if (obj instanceof Constant) {
            walkConstant((Constant)obj);
            return;
        }
        if (obj instanceof ContVariable) {
            walkContVariable((ContVariable)obj);
            return;
        }
        if (obj instanceof DiscVariable) {
            walkDiscVariable((DiscVariable)obj);
            return;
        }
        if (obj instanceof EnumDecl) {
            walkEnumDecl((EnumDecl)obj);
            return;
        }
        if (obj instanceof Event) {
            walkEvent((Event)obj);
            return;
        }
        if (obj instanceof Function) {
            walkFunction((Function)obj);
            return;
        }
        if (obj instanceof InputVariable) {
            walkInputVariable((InputVariable)obj);
            return;
        }
        if (obj instanceof TypeDecl) {
            walkTypeDecl((TypeDecl)obj);
            return;
        }
        String msg = "No redirect; unexpected object type: " + obj;
        throw new IllegalArgumentException(msg);
    }

    /**
     * Pre-crawling function for the {@link Declaration} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlDeclaration(Declaration obj) {
        precrawlPositionObject(obj);
        preprocessDeclaration(obj);
    }

    /**
     * Post-crawling function for the {@link Declaration} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlDeclaration(Declaration obj) {
        postprocessDeclaration(obj);
        postcrawlPositionObject(obj);
    }

    /**
     * Pre-processing function for the {@link Declaration} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessDeclaration(Declaration obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link Declaration} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessDeclaration(Declaration obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link DictExpression} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkDictExpression(DictExpression obj) {
        precrawlDictExpression(obj);
        List<DictPair> _pairs = obj.getPairs();
        for (DictPair x: _pairs) {
            walkDictPair(x);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        CifType _type = obj.getType();
        walkCifType(_type);
        postcrawlDictExpression(obj);
    }

    /**
     * Pre-crawling function for the {@link DictExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlDictExpression(DictExpression obj) {
        precrawlExpression(obj);
        preprocessDictExpression(obj);
    }

    /**
     * Post-crawling function for the {@link DictExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlDictExpression(DictExpression obj) {
        postprocessDictExpression(obj);
        postcrawlExpression(obj);
    }

    /**
     * Pre-processing function for the {@link DictExpression} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessDictExpression(DictExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link DictExpression} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessDictExpression(DictExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link DictPair} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkDictPair(DictPair obj) {
        precrawlDictPair(obj);
        Expression _key = obj.getKey();
        walkExpression(_key);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        Expression _value = obj.getValue();
        walkExpression(_value);
        postcrawlDictPair(obj);
    }

    /**
     * Pre-crawling function for the {@link DictPair} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlDictPair(DictPair obj) {
        precrawlPositionObject(obj);
        preprocessDictPair(obj);
    }

    /**
     * Post-crawling function for the {@link DictPair} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlDictPair(DictPair obj) {
        postprocessDictPair(obj);
        postcrawlPositionObject(obj);
    }

    /**
     * Pre-processing function for the {@link DictPair} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessDictPair(DictPair obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link DictPair} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessDictPair(DictPair obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link DictType} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkDictType(DictType obj) {
        precrawlDictType(obj);
        CifType _keyType = obj.getKeyType();
        walkCifType(_keyType);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        CifType _valueType = obj.getValueType();
        walkCifType(_valueType);
        postcrawlDictType(obj);
    }

    /**
     * Pre-crawling function for the {@link DictType} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlDictType(DictType obj) {
        precrawlCifType(obj);
        preprocessDictType(obj);
    }

    /**
     * Post-crawling function for the {@link DictType} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlDictType(DictType obj) {
        postprocessDictType(obj);
        postcrawlCifType(obj);
    }

    /**
     * Pre-processing function for the {@link DictType} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessDictType(DictType obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link DictType} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessDictType(DictType obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link DiscVariable} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkDiscVariable(DiscVariable obj) {
        precrawlDiscVariable(obj);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        CifType _type = obj.getType();
        walkCifType(_type);
        VariableValue _value = obj.getValue();
        if (_value != null) {
            walkVariableValue(_value);
        }
        postcrawlDiscVariable(obj);
    }

    /**
     * Pre-crawling function for the {@link DiscVariable} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlDiscVariable(DiscVariable obj) {
        precrawlDeclaration(obj);
        preprocessDiscVariable(obj);
    }

    /**
     * Post-crawling function for the {@link DiscVariable} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlDiscVariable(DiscVariable obj) {
        postprocessDiscVariable(obj);
        postcrawlDeclaration(obj);
    }

    /**
     * Pre-processing function for the {@link DiscVariable} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessDiscVariable(DiscVariable obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link DiscVariable} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessDiscVariable(DiscVariable obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link DiscVariableExpression} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkDiscVariableExpression(DiscVariableExpression obj) {
        precrawlDiscVariableExpression(obj);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        CifType _type = obj.getType();
        walkCifType(_type);
        postcrawlDiscVariableExpression(obj);
    }

    /**
     * Pre-crawling function for the {@link DiscVariableExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlDiscVariableExpression(DiscVariableExpression obj) {
        precrawlExpression(obj);
        preprocessDiscVariableExpression(obj);
    }

    /**
     * Post-crawling function for the {@link DiscVariableExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlDiscVariableExpression(DiscVariableExpression obj) {
        postprocessDiscVariableExpression(obj);
        postcrawlExpression(obj);
    }

    /**
     * Pre-processing function for the {@link DiscVariableExpression} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessDiscVariableExpression(DiscVariableExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link DiscVariableExpression} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessDiscVariableExpression(DiscVariableExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link DistType} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkDistType(DistType obj) {
        precrawlDistType(obj);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        CifType _sampleType = obj.getSampleType();
        walkCifType(_sampleType);
        postcrawlDistType(obj);
    }

    /**
     * Pre-crawling function for the {@link DistType} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlDistType(DistType obj) {
        precrawlCifType(obj);
        preprocessDistType(obj);
    }

    /**
     * Post-crawling function for the {@link DistType} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlDistType(DistType obj) {
        postprocessDistType(obj);
        postcrawlCifType(obj);
    }

    /**
     * Pre-processing function for the {@link DistType} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessDistType(DistType obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link DistType} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessDistType(DistType obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link Edge} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkEdge(Edge obj) {
        precrawlEdge(obj);
        List<EdgeEvent> _events = obj.getEvents();
        for (EdgeEvent x: _events) {
            walkEdgeEvent(x);
        }
        List<Expression> _guards = obj.getGuards();
        for (Expression x: _guards) {
            walkExpression(x);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        List<Update> _updates = obj.getUpdates();
        for (Update x: _updates) {
            walkUpdate(x);
        }
        postcrawlEdge(obj);
    }

    /**
     * Pre-crawling function for the {@link Edge} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlEdge(Edge obj) {
        precrawlPositionObject(obj);
        preprocessEdge(obj);
    }

    /**
     * Post-crawling function for the {@link Edge} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlEdge(Edge obj) {
        postprocessEdge(obj);
        postcrawlPositionObject(obj);
    }

    /**
     * Pre-processing function for the {@link Edge} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessEdge(Edge obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link Edge} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessEdge(Edge obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link EdgeEvent} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkEdgeEvent(EdgeEvent obj) {
        if (obj instanceof EdgeReceive) {
            walkEdgeReceive((EdgeReceive)obj);
            return;
        }
        if (obj instanceof EdgeSend) {
            walkEdgeSend((EdgeSend)obj);
            return;
        }
        Assert.check(obj.getClass() == EdgeEventImpl.class);
        precrawlEdgeEvent(obj);
        Expression _event = obj.getEvent();
        walkExpression(_event);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        postcrawlEdgeEvent(obj);
    }

    /**
     * Pre-crawling function for the {@link EdgeEvent} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlEdgeEvent(EdgeEvent obj) {
        precrawlPositionObject(obj);
        preprocessEdgeEvent(obj);
    }

    /**
     * Post-crawling function for the {@link EdgeEvent} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlEdgeEvent(EdgeEvent obj) {
        postprocessEdgeEvent(obj);
        postcrawlPositionObject(obj);
    }

    /**
     * Pre-processing function for the {@link EdgeEvent} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessEdgeEvent(EdgeEvent obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link EdgeEvent} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessEdgeEvent(EdgeEvent obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link EdgeReceive} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkEdgeReceive(EdgeReceive obj) {
        precrawlEdgeReceive(obj);
        Expression _event = obj.getEvent();
        walkExpression(_event);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        postcrawlEdgeReceive(obj);
    }

    /**
     * Pre-crawling function for the {@link EdgeReceive} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlEdgeReceive(EdgeReceive obj) {
        precrawlEdgeEvent(obj);
        preprocessEdgeReceive(obj);
    }

    /**
     * Post-crawling function for the {@link EdgeReceive} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlEdgeReceive(EdgeReceive obj) {
        postprocessEdgeReceive(obj);
        postcrawlEdgeEvent(obj);
    }

    /**
     * Pre-processing function for the {@link EdgeReceive} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessEdgeReceive(EdgeReceive obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link EdgeReceive} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessEdgeReceive(EdgeReceive obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link EdgeSend} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkEdgeSend(EdgeSend obj) {
        precrawlEdgeSend(obj);
        Expression _event = obj.getEvent();
        walkExpression(_event);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        Expression _value = obj.getValue();
        if (_value != null) {
            walkExpression(_value);
        }
        postcrawlEdgeSend(obj);
    }

    /**
     * Pre-crawling function for the {@link EdgeSend} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlEdgeSend(EdgeSend obj) {
        precrawlEdgeEvent(obj);
        preprocessEdgeSend(obj);
    }

    /**
     * Post-crawling function for the {@link EdgeSend} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlEdgeSend(EdgeSend obj) {
        postprocessEdgeSend(obj);
        postcrawlEdgeEvent(obj);
    }

    /**
     * Pre-processing function for the {@link EdgeSend} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessEdgeSend(EdgeSend obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link EdgeSend} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessEdgeSend(EdgeSend obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link ElifExpression} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkElifExpression(ElifExpression obj) {
        precrawlElifExpression(obj);
        List<Expression> _guards = obj.getGuards();
        for (Expression x: _guards) {
            walkExpression(x);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        Expression _then = obj.getThen();
        walkExpression(_then);
        postcrawlElifExpression(obj);
    }

    /**
     * Pre-crawling function for the {@link ElifExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlElifExpression(ElifExpression obj) {
        precrawlPositionObject(obj);
        preprocessElifExpression(obj);
    }

    /**
     * Post-crawling function for the {@link ElifExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlElifExpression(ElifExpression obj) {
        postprocessElifExpression(obj);
        postcrawlPositionObject(obj);
    }

    /**
     * Pre-processing function for the {@link ElifExpression} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessElifExpression(ElifExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link ElifExpression} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessElifExpression(ElifExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link ElifFuncStatement} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkElifFuncStatement(ElifFuncStatement obj) {
        precrawlElifFuncStatement(obj);
        List<Expression> _guards = obj.getGuards();
        for (Expression x: _guards) {
            walkExpression(x);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        List<FunctionStatement> _thens = obj.getThens();
        for (FunctionStatement x: _thens) {
            walkFunctionStatement(x);
        }
        postcrawlElifFuncStatement(obj);
    }

    /**
     * Pre-crawling function for the {@link ElifFuncStatement} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlElifFuncStatement(ElifFuncStatement obj) {
        precrawlPositionObject(obj);
        preprocessElifFuncStatement(obj);
    }

    /**
     * Post-crawling function for the {@link ElifFuncStatement} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlElifFuncStatement(ElifFuncStatement obj) {
        postprocessElifFuncStatement(obj);
        postcrawlPositionObject(obj);
    }

    /**
     * Pre-processing function for the {@link ElifFuncStatement} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessElifFuncStatement(ElifFuncStatement obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link ElifFuncStatement} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessElifFuncStatement(ElifFuncStatement obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link ElifUpdate} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkElifUpdate(ElifUpdate obj) {
        precrawlElifUpdate(obj);
        List<Expression> _guards = obj.getGuards();
        for (Expression x: _guards) {
            walkExpression(x);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        List<Update> _thens = obj.getThens();
        for (Update x: _thens) {
            walkUpdate(x);
        }
        postcrawlElifUpdate(obj);
    }

    /**
     * Pre-crawling function for the {@link ElifUpdate} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlElifUpdate(ElifUpdate obj) {
        precrawlPositionObject(obj);
        preprocessElifUpdate(obj);
    }

    /**
     * Post-crawling function for the {@link ElifUpdate} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlElifUpdate(ElifUpdate obj) {
        postprocessElifUpdate(obj);
        postcrawlPositionObject(obj);
    }

    /**
     * Pre-processing function for the {@link ElifUpdate} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessElifUpdate(ElifUpdate obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link ElifUpdate} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessElifUpdate(ElifUpdate obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link EnumDecl} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkEnumDecl(EnumDecl obj) {
        precrawlEnumDecl(obj);
        List<EnumLiteral> _literals = obj.getLiterals();
        for (EnumLiteral x: _literals) {
            walkEnumLiteral(x);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        postcrawlEnumDecl(obj);
    }

    /**
     * Pre-crawling function for the {@link EnumDecl} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlEnumDecl(EnumDecl obj) {
        precrawlDeclaration(obj);
        preprocessEnumDecl(obj);
    }

    /**
     * Post-crawling function for the {@link EnumDecl} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlEnumDecl(EnumDecl obj) {
        postprocessEnumDecl(obj);
        postcrawlDeclaration(obj);
    }

    /**
     * Pre-processing function for the {@link EnumDecl} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessEnumDecl(EnumDecl obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link EnumDecl} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessEnumDecl(EnumDecl obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link EnumLiteral} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkEnumLiteral(EnumLiteral obj) {
        precrawlEnumLiteral(obj);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        postcrawlEnumLiteral(obj);
    }

    /**
     * Pre-crawling function for the {@link EnumLiteral} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlEnumLiteral(EnumLiteral obj) {
        precrawlPositionObject(obj);
        preprocessEnumLiteral(obj);
    }

    /**
     * Post-crawling function for the {@link EnumLiteral} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlEnumLiteral(EnumLiteral obj) {
        postprocessEnumLiteral(obj);
        postcrawlPositionObject(obj);
    }

    /**
     * Pre-processing function for the {@link EnumLiteral} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessEnumLiteral(EnumLiteral obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link EnumLiteral} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessEnumLiteral(EnumLiteral obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link EnumLiteralExpression} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkEnumLiteralExpression(EnumLiteralExpression obj) {
        precrawlEnumLiteralExpression(obj);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        CifType _type = obj.getType();
        walkCifType(_type);
        postcrawlEnumLiteralExpression(obj);
    }

    /**
     * Pre-crawling function for the {@link EnumLiteralExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlEnumLiteralExpression(EnumLiteralExpression obj) {
        precrawlExpression(obj);
        preprocessEnumLiteralExpression(obj);
    }

    /**
     * Post-crawling function for the {@link EnumLiteralExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlEnumLiteralExpression(EnumLiteralExpression obj) {
        postprocessEnumLiteralExpression(obj);
        postcrawlExpression(obj);
    }

    /**
     * Pre-processing function for the {@link EnumLiteralExpression} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessEnumLiteralExpression(EnumLiteralExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link EnumLiteralExpression} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessEnumLiteralExpression(EnumLiteralExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link EnumType} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkEnumType(EnumType obj) {
        precrawlEnumType(obj);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        postcrawlEnumType(obj);
    }

    /**
     * Pre-crawling function for the {@link EnumType} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlEnumType(EnumType obj) {
        precrawlCifType(obj);
        preprocessEnumType(obj);
    }

    /**
     * Post-crawling function for the {@link EnumType} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlEnumType(EnumType obj) {
        postprocessEnumType(obj);
        postcrawlCifType(obj);
    }

    /**
     * Pre-processing function for the {@link EnumType} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessEnumType(EnumType obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link EnumType} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessEnumType(EnumType obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link Equation} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkEquation(Equation obj) {
        precrawlEquation(obj);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        Expression _value = obj.getValue();
        walkExpression(_value);
        postcrawlEquation(obj);
    }

    /**
     * Pre-crawling function for the {@link Equation} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlEquation(Equation obj) {
        precrawlPositionObject(obj);
        preprocessEquation(obj);
    }

    /**
     * Post-crawling function for the {@link Equation} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlEquation(Equation obj) {
        postprocessEquation(obj);
        postcrawlPositionObject(obj);
    }

    /**
     * Pre-processing function for the {@link Equation} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessEquation(Equation obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link Equation} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessEquation(Equation obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link Event} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkEvent(Event obj) {
        precrawlEvent(obj);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        CifType _type = obj.getType();
        if (_type != null) {
            walkCifType(_type);
        }
        postcrawlEvent(obj);
    }

    /**
     * Pre-crawling function for the {@link Event} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlEvent(Event obj) {
        precrawlDeclaration(obj);
        preprocessEvent(obj);
    }

    /**
     * Post-crawling function for the {@link Event} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlEvent(Event obj) {
        postprocessEvent(obj);
        postcrawlDeclaration(obj);
    }

    /**
     * Pre-processing function for the {@link Event} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessEvent(Event obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link Event} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessEvent(Event obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link EventExpression} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkEventExpression(EventExpression obj) {
        precrawlEventExpression(obj);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        CifType _type = obj.getType();
        walkCifType(_type);
        postcrawlEventExpression(obj);
    }

    /**
     * Pre-crawling function for the {@link EventExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlEventExpression(EventExpression obj) {
        precrawlExpression(obj);
        preprocessEventExpression(obj);
    }

    /**
     * Post-crawling function for the {@link EventExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlEventExpression(EventExpression obj) {
        postprocessEventExpression(obj);
        postcrawlExpression(obj);
    }

    /**
     * Pre-processing function for the {@link EventExpression} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessEventExpression(EventExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link EventExpression} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessEventExpression(EventExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link EventParameter} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkEventParameter(EventParameter obj) {
        precrawlEventParameter(obj);
        Event _event = obj.getEvent();
        walkEvent(_event);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        postcrawlEventParameter(obj);
    }

    /**
     * Pre-crawling function for the {@link EventParameter} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlEventParameter(EventParameter obj) {
        precrawlParameter(obj);
        preprocessEventParameter(obj);
    }

    /**
     * Post-crawling function for the {@link EventParameter} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlEventParameter(EventParameter obj) {
        postprocessEventParameter(obj);
        postcrawlParameter(obj);
    }

    /**
     * Pre-processing function for the {@link EventParameter} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessEventParameter(EventParameter obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link EventParameter} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessEventParameter(EventParameter obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link Expression} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkExpression(Expression obj) {
        if (obj instanceof AlgVariableExpression) {
            walkAlgVariableExpression((AlgVariableExpression)obj);
            return;
        }
        if (obj instanceof BaseFunctionExpression) {
            walkBaseFunctionExpression((BaseFunctionExpression)obj);
            return;
        }
        if (obj instanceof BinaryExpression) {
            walkBinaryExpression((BinaryExpression)obj);
            return;
        }
        if (obj instanceof BoolExpression) {
            walkBoolExpression((BoolExpression)obj);
            return;
        }
        if (obj instanceof CastExpression) {
            walkCastExpression((CastExpression)obj);
            return;
        }
        if (obj instanceof CompInstWrapExpression) {
            walkCompInstWrapExpression((CompInstWrapExpression)obj);
            return;
        }
        if (obj instanceof CompParamExpression) {
            walkCompParamExpression((CompParamExpression)obj);
            return;
        }
        if (obj instanceof CompParamWrapExpression) {
            walkCompParamWrapExpression((CompParamWrapExpression)obj);
            return;
        }
        if (obj instanceof ComponentExpression) {
            walkComponentExpression((ComponentExpression)obj);
            return;
        }
        if (obj instanceof ConstantExpression) {
            walkConstantExpression((ConstantExpression)obj);
            return;
        }
        if (obj instanceof ContVariableExpression) {
            walkContVariableExpression((ContVariableExpression)obj);
            return;
        }
        if (obj instanceof DictExpression) {
            walkDictExpression((DictExpression)obj);
            return;
        }
        if (obj instanceof DiscVariableExpression) {
            walkDiscVariableExpression((DiscVariableExpression)obj);
            return;
        }
        if (obj instanceof EnumLiteralExpression) {
            walkEnumLiteralExpression((EnumLiteralExpression)obj);
            return;
        }
        if (obj instanceof EventExpression) {
            walkEventExpression((EventExpression)obj);
            return;
        }
        if (obj instanceof FieldExpression) {
            walkFieldExpression((FieldExpression)obj);
            return;
        }
        if (obj instanceof FunctionCallExpression) {
            walkFunctionCallExpression((FunctionCallExpression)obj);
            return;
        }
        if (obj instanceof IfExpression) {
            walkIfExpression((IfExpression)obj);
            return;
        }
        if (obj instanceof InputVariableExpression) {
            walkInputVariableExpression((InputVariableExpression)obj);
            return;
        }
        if (obj instanceof IntExpression) {
            walkIntExpression((IntExpression)obj);
            return;
        }
        if (obj instanceof ListExpression) {
            walkListExpression((ListExpression)obj);
            return;
        }
        if (obj instanceof LocationExpression) {
            walkLocationExpression((LocationExpression)obj);
            return;
        }
        if (obj instanceof ProjectionExpression) {
            walkProjectionExpression((ProjectionExpression)obj);
            return;
        }
        if (obj instanceof RealExpression) {
            walkRealExpression((RealExpression)obj);
            return;
        }
        if (obj instanceof ReceivedExpression) {
            walkReceivedExpression((ReceivedExpression)obj);
            return;
        }
        if (obj instanceof SelfExpression) {
            walkSelfExpression((SelfExpression)obj);
            return;
        }
        if (obj instanceof SetExpression) {
            walkSetExpression((SetExpression)obj);
            return;
        }
        if (obj instanceof SliceExpression) {
            walkSliceExpression((SliceExpression)obj);
            return;
        }
        if (obj instanceof StringExpression) {
            walkStringExpression((StringExpression)obj);
            return;
        }
        if (obj instanceof SwitchExpression) {
            walkSwitchExpression((SwitchExpression)obj);
            return;
        }
        if (obj instanceof TauExpression) {
            walkTauExpression((TauExpression)obj);
            return;
        }
        if (obj instanceof TimeExpression) {
            walkTimeExpression((TimeExpression)obj);
            return;
        }
        if (obj instanceof TupleExpression) {
            walkTupleExpression((TupleExpression)obj);
            return;
        }
        if (obj instanceof UnaryExpression) {
            walkUnaryExpression((UnaryExpression)obj);
            return;
        }
        String msg = "No redirect; unexpected object type: " + obj;
        throw new IllegalArgumentException(msg);
    }

    /**
     * Pre-crawling function for the {@link Expression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlExpression(Expression obj) {
        precrawlPositionObject(obj);
        preprocessExpression(obj);
    }

    /**
     * Post-crawling function for the {@link Expression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlExpression(Expression obj) {
        postprocessExpression(obj);
        postcrawlPositionObject(obj);
    }

    /**
     * Pre-processing function for the {@link Expression} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessExpression(Expression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link Expression} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessExpression(Expression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link ExternalFunction} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkExternalFunction(ExternalFunction obj) {
        precrawlExternalFunction(obj);
        List<FunctionParameter> _parameters = obj.getParameters();
        for (FunctionParameter x: _parameters) {
            walkFunctionParameter(x);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        List<CifType> _returnTypes = obj.getReturnTypes();
        for (CifType x: _returnTypes) {
            walkCifType(x);
        }
        postcrawlExternalFunction(obj);
    }

    /**
     * Pre-crawling function for the {@link ExternalFunction} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlExternalFunction(ExternalFunction obj) {
        precrawlFunction(obj);
        preprocessExternalFunction(obj);
    }

    /**
     * Post-crawling function for the {@link ExternalFunction} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlExternalFunction(ExternalFunction obj) {
        postprocessExternalFunction(obj);
        postcrawlFunction(obj);
    }

    /**
     * Pre-processing function for the {@link ExternalFunction} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessExternalFunction(ExternalFunction obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link ExternalFunction} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessExternalFunction(ExternalFunction obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link Field} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkField(Field obj) {
        precrawlField(obj);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        CifType _type = obj.getType();
        walkCifType(_type);
        postcrawlField(obj);
    }

    /**
     * Pre-crawling function for the {@link Field} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlField(Field obj) {
        precrawlPositionObject(obj);
        preprocessField(obj);
    }

    /**
     * Post-crawling function for the {@link Field} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlField(Field obj) {
        postprocessField(obj);
        postcrawlPositionObject(obj);
    }

    /**
     * Pre-processing function for the {@link Field} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessField(Field obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link Field} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessField(Field obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link FieldExpression} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkFieldExpression(FieldExpression obj) {
        precrawlFieldExpression(obj);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        CifType _type = obj.getType();
        walkCifType(_type);
        postcrawlFieldExpression(obj);
    }

    /**
     * Pre-crawling function for the {@link FieldExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlFieldExpression(FieldExpression obj) {
        precrawlExpression(obj);
        preprocessFieldExpression(obj);
    }

    /**
     * Post-crawling function for the {@link FieldExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlFieldExpression(FieldExpression obj) {
        postprocessFieldExpression(obj);
        postcrawlExpression(obj);
    }

    /**
     * Pre-processing function for the {@link FieldExpression} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessFieldExpression(FieldExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link FieldExpression} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessFieldExpression(FieldExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link FuncType} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkFuncType(FuncType obj) {
        precrawlFuncType(obj);
        List<CifType> _paramTypes = obj.getParamTypes();
        for (CifType x: _paramTypes) {
            walkCifType(x);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        CifType _returnType = obj.getReturnType();
        walkCifType(_returnType);
        postcrawlFuncType(obj);
    }

    /**
     * Pre-crawling function for the {@link FuncType} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlFuncType(FuncType obj) {
        precrawlCifType(obj);
        preprocessFuncType(obj);
    }

    /**
     * Post-crawling function for the {@link FuncType} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlFuncType(FuncType obj) {
        postprocessFuncType(obj);
        postcrawlCifType(obj);
    }

    /**
     * Pre-processing function for the {@link FuncType} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessFuncType(FuncType obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link FuncType} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessFuncType(FuncType obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link Function} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkFunction(Function obj) {
        if (obj instanceof ExternalFunction) {
            walkExternalFunction((ExternalFunction)obj);
            return;
        }
        if (obj instanceof InternalFunction) {
            walkInternalFunction((InternalFunction)obj);
            return;
        }
        String msg = "No redirect; unexpected object type: " + obj;
        throw new IllegalArgumentException(msg);
    }

    /**
     * Pre-crawling function for the {@link Function} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlFunction(Function obj) {
        precrawlDeclaration(obj);
        preprocessFunction(obj);
    }

    /**
     * Post-crawling function for the {@link Function} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlFunction(Function obj) {
        postprocessFunction(obj);
        postcrawlDeclaration(obj);
    }

    /**
     * Pre-processing function for the {@link Function} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessFunction(Function obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link Function} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessFunction(Function obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link FunctionCallExpression} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkFunctionCallExpression(FunctionCallExpression obj) {
        precrawlFunctionCallExpression(obj);
        Expression _function = obj.getFunction();
        walkExpression(_function);
        List<Expression> _params = obj.getParams();
        for (Expression x: _params) {
            walkExpression(x);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        CifType _type = obj.getType();
        walkCifType(_type);
        postcrawlFunctionCallExpression(obj);
    }

    /**
     * Pre-crawling function for the {@link FunctionCallExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlFunctionCallExpression(FunctionCallExpression obj) {
        precrawlExpression(obj);
        preprocessFunctionCallExpression(obj);
    }

    /**
     * Post-crawling function for the {@link FunctionCallExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlFunctionCallExpression(FunctionCallExpression obj) {
        postprocessFunctionCallExpression(obj);
        postcrawlExpression(obj);
    }

    /**
     * Pre-processing function for the {@link FunctionCallExpression} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessFunctionCallExpression(FunctionCallExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link FunctionCallExpression} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessFunctionCallExpression(FunctionCallExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link FunctionExpression} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkFunctionExpression(FunctionExpression obj) {
        precrawlFunctionExpression(obj);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        CifType _type = obj.getType();
        walkCifType(_type);
        postcrawlFunctionExpression(obj);
    }

    /**
     * Pre-crawling function for the {@link FunctionExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlFunctionExpression(FunctionExpression obj) {
        precrawlBaseFunctionExpression(obj);
        preprocessFunctionExpression(obj);
    }

    /**
     * Post-crawling function for the {@link FunctionExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlFunctionExpression(FunctionExpression obj) {
        postprocessFunctionExpression(obj);
        postcrawlBaseFunctionExpression(obj);
    }

    /**
     * Pre-processing function for the {@link FunctionExpression} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessFunctionExpression(FunctionExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link FunctionExpression} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessFunctionExpression(FunctionExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link FunctionParameter} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkFunctionParameter(FunctionParameter obj) {
        precrawlFunctionParameter(obj);
        DiscVariable _parameter = obj.getParameter();
        walkDiscVariable(_parameter);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        postcrawlFunctionParameter(obj);
    }

    /**
     * Pre-crawling function for the {@link FunctionParameter} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlFunctionParameter(FunctionParameter obj) {
        precrawlPositionObject(obj);
        preprocessFunctionParameter(obj);
    }

    /**
     * Post-crawling function for the {@link FunctionParameter} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlFunctionParameter(FunctionParameter obj) {
        postprocessFunctionParameter(obj);
        postcrawlPositionObject(obj);
    }

    /**
     * Pre-processing function for the {@link FunctionParameter} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessFunctionParameter(FunctionParameter obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link FunctionParameter} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessFunctionParameter(FunctionParameter obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link FunctionStatement} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkFunctionStatement(FunctionStatement obj) {
        if (obj instanceof AssignmentFuncStatement) {
            walkAssignmentFuncStatement((AssignmentFuncStatement)obj);
            return;
        }
        if (obj instanceof BreakFuncStatement) {
            walkBreakFuncStatement((BreakFuncStatement)obj);
            return;
        }
        if (obj instanceof ContinueFuncStatement) {
            walkContinueFuncStatement((ContinueFuncStatement)obj);
            return;
        }
        if (obj instanceof IfFuncStatement) {
            walkIfFuncStatement((IfFuncStatement)obj);
            return;
        }
        if (obj instanceof ReturnFuncStatement) {
            walkReturnFuncStatement((ReturnFuncStatement)obj);
            return;
        }
        if (obj instanceof WhileFuncStatement) {
            walkWhileFuncStatement((WhileFuncStatement)obj);
            return;
        }
        String msg = "No redirect; unexpected object type: " + obj;
        throw new IllegalArgumentException(msg);
    }

    /**
     * Pre-crawling function for the {@link FunctionStatement} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlFunctionStatement(FunctionStatement obj) {
        precrawlPositionObject(obj);
        preprocessFunctionStatement(obj);
    }

    /**
     * Post-crawling function for the {@link FunctionStatement} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlFunctionStatement(FunctionStatement obj) {
        postprocessFunctionStatement(obj);
        postcrawlPositionObject(obj);
    }

    /**
     * Pre-processing function for the {@link FunctionStatement} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessFunctionStatement(FunctionStatement obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link FunctionStatement} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessFunctionStatement(FunctionStatement obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link Group} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkGroup(Group obj) {
        if (obj instanceof Specification) {
            walkSpecification((Specification)obj);
            return;
        }
        Assert.check(obj.getClass() == GroupImpl.class);
        precrawlGroup(obj);
        List<Component> _components = obj.getComponents();
        for (Component x: _components) {
            walkComponent(x);
        }
        List<Declaration> _declarations = obj.getDeclarations();
        for (Declaration x: _declarations) {
            walkDeclaration(x);
        }
        List<ComponentDef> _definitions = obj.getDefinitions();
        for (ComponentDef x: _definitions) {
            walkComponentDef(x);
        }
        List<Equation> _equations = obj.getEquations();
        for (Equation x: _equations) {
            walkEquation(x);
        }
        List<Expression> _initials = obj.getInitials();
        for (Expression x: _initials) {
            walkExpression(x);
        }
        List<Invariant> _invariants = obj.getInvariants();
        for (Invariant x: _invariants) {
            walkInvariant(x);
        }
        List<IoDecl> _ioDecls = obj.getIoDecls();
        for (IoDecl x: _ioDecls) {
            walkIoDecl(x);
        }
        List<Expression> _markeds = obj.getMarkeds();
        for (Expression x: _markeds) {
            walkExpression(x);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        postcrawlGroup(obj);
    }

    /**
     * Pre-crawling function for the {@link Group} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlGroup(Group obj) {
        precrawlComplexComponent(obj);
        preprocessGroup(obj);
    }

    /**
     * Post-crawling function for the {@link Group} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlGroup(Group obj) {
        postprocessGroup(obj);
        postcrawlComplexComponent(obj);
    }

    /**
     * Pre-processing function for the {@link Group} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessGroup(Group obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link Group} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessGroup(Group obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link IfExpression} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkIfExpression(IfExpression obj) {
        precrawlIfExpression(obj);
        List<ElifExpression> _elifs = obj.getElifs();
        for (ElifExpression x: _elifs) {
            walkElifExpression(x);
        }
        Expression _else = obj.getElse();
        walkExpression(_else);
        List<Expression> _guards = obj.getGuards();
        for (Expression x: _guards) {
            walkExpression(x);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        Expression _then = obj.getThen();
        walkExpression(_then);
        CifType _type = obj.getType();
        walkCifType(_type);
        postcrawlIfExpression(obj);
    }

    /**
     * Pre-crawling function for the {@link IfExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlIfExpression(IfExpression obj) {
        precrawlExpression(obj);
        preprocessIfExpression(obj);
    }

    /**
     * Post-crawling function for the {@link IfExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlIfExpression(IfExpression obj) {
        postprocessIfExpression(obj);
        postcrawlExpression(obj);
    }

    /**
     * Pre-processing function for the {@link IfExpression} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessIfExpression(IfExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link IfExpression} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessIfExpression(IfExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link IfFuncStatement} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkIfFuncStatement(IfFuncStatement obj) {
        precrawlIfFuncStatement(obj);
        List<ElifFuncStatement> _elifs = obj.getElifs();
        for (ElifFuncStatement x: _elifs) {
            walkElifFuncStatement(x);
        }
        List<FunctionStatement> _elses = obj.getElses();
        for (FunctionStatement x: _elses) {
            walkFunctionStatement(x);
        }
        List<Expression> _guards = obj.getGuards();
        for (Expression x: _guards) {
            walkExpression(x);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        List<FunctionStatement> _thens = obj.getThens();
        for (FunctionStatement x: _thens) {
            walkFunctionStatement(x);
        }
        postcrawlIfFuncStatement(obj);
    }

    /**
     * Pre-crawling function for the {@link IfFuncStatement} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlIfFuncStatement(IfFuncStatement obj) {
        precrawlFunctionStatement(obj);
        preprocessIfFuncStatement(obj);
    }

    /**
     * Post-crawling function for the {@link IfFuncStatement} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlIfFuncStatement(IfFuncStatement obj) {
        postprocessIfFuncStatement(obj);
        postcrawlFunctionStatement(obj);
    }

    /**
     * Pre-processing function for the {@link IfFuncStatement} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessIfFuncStatement(IfFuncStatement obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link IfFuncStatement} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessIfFuncStatement(IfFuncStatement obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link IfUpdate} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkIfUpdate(IfUpdate obj) {
        precrawlIfUpdate(obj);
        List<ElifUpdate> _elifs = obj.getElifs();
        for (ElifUpdate x: _elifs) {
            walkElifUpdate(x);
        }
        List<Update> _elses = obj.getElses();
        for (Update x: _elses) {
            walkUpdate(x);
        }
        List<Expression> _guards = obj.getGuards();
        for (Expression x: _guards) {
            walkExpression(x);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        List<Update> _thens = obj.getThens();
        for (Update x: _thens) {
            walkUpdate(x);
        }
        postcrawlIfUpdate(obj);
    }

    /**
     * Pre-crawling function for the {@link IfUpdate} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlIfUpdate(IfUpdate obj) {
        precrawlUpdate(obj);
        preprocessIfUpdate(obj);
    }

    /**
     * Post-crawling function for the {@link IfUpdate} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlIfUpdate(IfUpdate obj) {
        postprocessIfUpdate(obj);
        postcrawlUpdate(obj);
    }

    /**
     * Pre-processing function for the {@link IfUpdate} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessIfUpdate(IfUpdate obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link IfUpdate} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessIfUpdate(IfUpdate obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link InputVariable} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkInputVariable(InputVariable obj) {
        precrawlInputVariable(obj);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        CifType _type = obj.getType();
        walkCifType(_type);
        postcrawlInputVariable(obj);
    }

    /**
     * Pre-crawling function for the {@link InputVariable} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlInputVariable(InputVariable obj) {
        precrawlDeclaration(obj);
        preprocessInputVariable(obj);
    }

    /**
     * Post-crawling function for the {@link InputVariable} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlInputVariable(InputVariable obj) {
        postprocessInputVariable(obj);
        postcrawlDeclaration(obj);
    }

    /**
     * Pre-processing function for the {@link InputVariable} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessInputVariable(InputVariable obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link InputVariable} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessInputVariable(InputVariable obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link InputVariableExpression} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkInputVariableExpression(InputVariableExpression obj) {
        precrawlInputVariableExpression(obj);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        CifType _type = obj.getType();
        walkCifType(_type);
        postcrawlInputVariableExpression(obj);
    }

    /**
     * Pre-crawling function for the {@link InputVariableExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlInputVariableExpression(InputVariableExpression obj) {
        precrawlExpression(obj);
        preprocessInputVariableExpression(obj);
    }

    /**
     * Post-crawling function for the {@link InputVariableExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlInputVariableExpression(InputVariableExpression obj) {
        postprocessInputVariableExpression(obj);
        postcrawlExpression(obj);
    }

    /**
     * Pre-processing function for the {@link InputVariableExpression} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessInputVariableExpression(InputVariableExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link InputVariableExpression} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessInputVariableExpression(InputVariableExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link IntExpression} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkIntExpression(IntExpression obj) {
        precrawlIntExpression(obj);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        CifType _type = obj.getType();
        walkCifType(_type);
        postcrawlIntExpression(obj);
    }

    /**
     * Pre-crawling function for the {@link IntExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlIntExpression(IntExpression obj) {
        precrawlExpression(obj);
        preprocessIntExpression(obj);
    }

    /**
     * Post-crawling function for the {@link IntExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlIntExpression(IntExpression obj) {
        postprocessIntExpression(obj);
        postcrawlExpression(obj);
    }

    /**
     * Pre-processing function for the {@link IntExpression} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessIntExpression(IntExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link IntExpression} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessIntExpression(IntExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link IntType} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkIntType(IntType obj) {
        precrawlIntType(obj);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        postcrawlIntType(obj);
    }

    /**
     * Pre-crawling function for the {@link IntType} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlIntType(IntType obj) {
        precrawlCifType(obj);
        preprocessIntType(obj);
    }

    /**
     * Post-crawling function for the {@link IntType} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlIntType(IntType obj) {
        postprocessIntType(obj);
        postcrawlCifType(obj);
    }

    /**
     * Pre-processing function for the {@link IntType} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessIntType(IntType obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link IntType} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessIntType(IntType obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link InternalFunction} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkInternalFunction(InternalFunction obj) {
        precrawlInternalFunction(obj);
        List<FunctionParameter> _parameters = obj.getParameters();
        for (FunctionParameter x: _parameters) {
            walkFunctionParameter(x);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        List<CifType> _returnTypes = obj.getReturnTypes();
        for (CifType x: _returnTypes) {
            walkCifType(x);
        }
        List<FunctionStatement> _statements = obj.getStatements();
        for (FunctionStatement x: _statements) {
            walkFunctionStatement(x);
        }
        List<DiscVariable> _variables = obj.getVariables();
        for (DiscVariable x: _variables) {
            walkDiscVariable(x);
        }
        postcrawlInternalFunction(obj);
    }

    /**
     * Pre-crawling function for the {@link InternalFunction} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlInternalFunction(InternalFunction obj) {
        precrawlFunction(obj);
        preprocessInternalFunction(obj);
    }

    /**
     * Post-crawling function for the {@link InternalFunction} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlInternalFunction(InternalFunction obj) {
        postprocessInternalFunction(obj);
        postcrawlFunction(obj);
    }

    /**
     * Pre-processing function for the {@link InternalFunction} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessInternalFunction(InternalFunction obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link InternalFunction} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessInternalFunction(InternalFunction obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link Invariant} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkInvariant(Invariant obj) {
        precrawlInvariant(obj);
        Expression _event = obj.getEvent();
        if (_event != null) {
            walkExpression(_event);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        Expression _predicate = obj.getPredicate();
        walkExpression(_predicate);
        postcrawlInvariant(obj);
    }

    /**
     * Pre-crawling function for the {@link Invariant} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlInvariant(Invariant obj) {
        precrawlPositionObject(obj);
        preprocessInvariant(obj);
    }

    /**
     * Post-crawling function for the {@link Invariant} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlInvariant(Invariant obj) {
        postprocessInvariant(obj);
        postcrawlPositionObject(obj);
    }

    /**
     * Pre-processing function for the {@link Invariant} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessInvariant(Invariant obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link Invariant} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessInvariant(Invariant obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link IoDecl} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkIoDecl(IoDecl obj) {
        if (obj instanceof Print) {
            walkPrint((Print)obj);
            return;
        }
        if (obj instanceof PrintFile) {
            walkPrintFile((PrintFile)obj);
            return;
        }
        if (obj instanceof SvgCopy) {
            walkSvgCopy((SvgCopy)obj);
            return;
        }
        if (obj instanceof SvgFile) {
            walkSvgFile((SvgFile)obj);
            return;
        }
        if (obj instanceof SvgIn) {
            walkSvgIn((SvgIn)obj);
            return;
        }
        if (obj instanceof SvgMove) {
            walkSvgMove((SvgMove)obj);
            return;
        }
        if (obj instanceof SvgOut) {
            walkSvgOut((SvgOut)obj);
            return;
        }
        String msg = "No redirect; unexpected object type: " + obj;
        throw new IllegalArgumentException(msg);
    }

    /**
     * Pre-crawling function for the {@link IoDecl} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlIoDecl(IoDecl obj) {
        precrawlPositionObject(obj);
        preprocessIoDecl(obj);
    }

    /**
     * Post-crawling function for the {@link IoDecl} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlIoDecl(IoDecl obj) {
        postprocessIoDecl(obj);
        postcrawlPositionObject(obj);
    }

    /**
     * Pre-processing function for the {@link IoDecl} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessIoDecl(IoDecl obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link IoDecl} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessIoDecl(IoDecl obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link ListExpression} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkListExpression(ListExpression obj) {
        precrawlListExpression(obj);
        List<Expression> _elements = obj.getElements();
        for (Expression x: _elements) {
            walkExpression(x);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        CifType _type = obj.getType();
        walkCifType(_type);
        postcrawlListExpression(obj);
    }

    /**
     * Pre-crawling function for the {@link ListExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlListExpression(ListExpression obj) {
        precrawlExpression(obj);
        preprocessListExpression(obj);
    }

    /**
     * Post-crawling function for the {@link ListExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlListExpression(ListExpression obj) {
        postprocessListExpression(obj);
        postcrawlExpression(obj);
    }

    /**
     * Pre-processing function for the {@link ListExpression} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessListExpression(ListExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link ListExpression} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessListExpression(ListExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link ListType} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkListType(ListType obj) {
        precrawlListType(obj);
        CifType _elementType = obj.getElementType();
        walkCifType(_elementType);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        postcrawlListType(obj);
    }

    /**
     * Pre-crawling function for the {@link ListType} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlListType(ListType obj) {
        precrawlCifType(obj);
        preprocessListType(obj);
    }

    /**
     * Post-crawling function for the {@link ListType} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlListType(ListType obj) {
        postprocessListType(obj);
        postcrawlCifType(obj);
    }

    /**
     * Pre-processing function for the {@link ListType} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessListType(ListType obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link ListType} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessListType(ListType obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link Location} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkLocation(Location obj) {
        precrawlLocation(obj);
        List<Edge> _edges = obj.getEdges();
        for (Edge x: _edges) {
            walkEdge(x);
        }
        List<Equation> _equations = obj.getEquations();
        for (Equation x: _equations) {
            walkEquation(x);
        }
        List<Expression> _initials = obj.getInitials();
        for (Expression x: _initials) {
            walkExpression(x);
        }
        List<Invariant> _invariants = obj.getInvariants();
        for (Invariant x: _invariants) {
            walkInvariant(x);
        }
        List<Expression> _markeds = obj.getMarkeds();
        for (Expression x: _markeds) {
            walkExpression(x);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        postcrawlLocation(obj);
    }

    /**
     * Pre-crawling function for the {@link Location} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlLocation(Location obj) {
        precrawlPositionObject(obj);
        preprocessLocation(obj);
    }

    /**
     * Post-crawling function for the {@link Location} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlLocation(Location obj) {
        postprocessLocation(obj);
        postcrawlPositionObject(obj);
    }

    /**
     * Pre-processing function for the {@link Location} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessLocation(Location obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link Location} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessLocation(Location obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link LocationExpression} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkLocationExpression(LocationExpression obj) {
        precrawlLocationExpression(obj);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        CifType _type = obj.getType();
        walkCifType(_type);
        postcrawlLocationExpression(obj);
    }

    /**
     * Pre-crawling function for the {@link LocationExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlLocationExpression(LocationExpression obj) {
        precrawlExpression(obj);
        preprocessLocationExpression(obj);
    }

    /**
     * Post-crawling function for the {@link LocationExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlLocationExpression(LocationExpression obj) {
        postprocessLocationExpression(obj);
        postcrawlExpression(obj);
    }

    /**
     * Pre-processing function for the {@link LocationExpression} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessLocationExpression(LocationExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link LocationExpression} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessLocationExpression(LocationExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link LocationParameter} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkLocationParameter(LocationParameter obj) {
        precrawlLocationParameter(obj);
        Location _location = obj.getLocation();
        walkLocation(_location);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        postcrawlLocationParameter(obj);
    }

    /**
     * Pre-crawling function for the {@link LocationParameter} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlLocationParameter(LocationParameter obj) {
        precrawlParameter(obj);
        preprocessLocationParameter(obj);
    }

    /**
     * Post-crawling function for the {@link LocationParameter} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlLocationParameter(LocationParameter obj) {
        postprocessLocationParameter(obj);
        postcrawlParameter(obj);
    }

    /**
     * Pre-processing function for the {@link LocationParameter} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessLocationParameter(LocationParameter obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link LocationParameter} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessLocationParameter(LocationParameter obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link Monitors} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkMonitors(Monitors obj) {
        precrawlMonitors(obj);
        List<Expression> _events = obj.getEvents();
        for (Expression x: _events) {
            walkExpression(x);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        postcrawlMonitors(obj);
    }

    /**
     * Pre-crawling function for the {@link Monitors} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlMonitors(Monitors obj) {
        precrawlPositionObject(obj);
        preprocessMonitors(obj);
    }

    /**
     * Post-crawling function for the {@link Monitors} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlMonitors(Monitors obj) {
        postprocessMonitors(obj);
        postcrawlPositionObject(obj);
    }

    /**
     * Pre-processing function for the {@link Monitors} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessMonitors(Monitors obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link Monitors} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessMonitors(Monitors obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link Parameter} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkParameter(Parameter obj) {
        if (obj instanceof AlgParameter) {
            walkAlgParameter((AlgParameter)obj);
            return;
        }
        if (obj instanceof ComponentParameter) {
            walkComponentParameter((ComponentParameter)obj);
            return;
        }
        if (obj instanceof EventParameter) {
            walkEventParameter((EventParameter)obj);
            return;
        }
        if (obj instanceof LocationParameter) {
            walkLocationParameter((LocationParameter)obj);
            return;
        }
        String msg = "No redirect; unexpected object type: " + obj;
        throw new IllegalArgumentException(msg);
    }

    /**
     * Pre-crawling function for the {@link Parameter} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlParameter(Parameter obj) {
        precrawlPositionObject(obj);
        preprocessParameter(obj);
    }

    /**
     * Post-crawling function for the {@link Parameter} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlParameter(Parameter obj) {
        postprocessParameter(obj);
        postcrawlPositionObject(obj);
    }

    /**
     * Pre-processing function for the {@link Parameter} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessParameter(Parameter obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link Parameter} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessParameter(Parameter obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link Position} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkPosition(Position obj) {
        precrawlPosition(obj);
        postcrawlPosition(obj);
    }

    /**
     * Pre-crawling function for the {@link Position} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlPosition(Position obj) {
        preprocessPosition(obj);
    }

    /**
     * Post-crawling function for the {@link Position} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlPosition(Position obj) {
        postprocessPosition(obj);
    }

    /**
     * Pre-processing function for the {@link Position} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessPosition(Position obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link Position} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessPosition(Position obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link PositionObject} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkPositionObject(PositionObject obj) {
        if (obj instanceof Alphabet) {
            walkAlphabet((Alphabet)obj);
            return;
        }
        if (obj instanceof CifType) {
            walkCifType((CifType)obj);
            return;
        }
        if (obj instanceof Component) {
            walkComponent((Component)obj);
            return;
        }
        if (obj instanceof ComponentDef) {
            walkComponentDef((ComponentDef)obj);
            return;
        }
        if (obj instanceof Declaration) {
            walkDeclaration((Declaration)obj);
            return;
        }
        if (obj instanceof DictPair) {
            walkDictPair((DictPair)obj);
            return;
        }
        if (obj instanceof Edge) {
            walkEdge((Edge)obj);
            return;
        }
        if (obj instanceof EdgeEvent) {
            walkEdgeEvent((EdgeEvent)obj);
            return;
        }
        if (obj instanceof ElifExpression) {
            walkElifExpression((ElifExpression)obj);
            return;
        }
        if (obj instanceof ElifFuncStatement) {
            walkElifFuncStatement((ElifFuncStatement)obj);
            return;
        }
        if (obj instanceof ElifUpdate) {
            walkElifUpdate((ElifUpdate)obj);
            return;
        }
        if (obj instanceof EnumLiteral) {
            walkEnumLiteral((EnumLiteral)obj);
            return;
        }
        if (obj instanceof Equation) {
            walkEquation((Equation)obj);
            return;
        }
        if (obj instanceof Expression) {
            walkExpression((Expression)obj);
            return;
        }
        if (obj instanceof Field) {
            walkField((Field)obj);
            return;
        }
        if (obj instanceof FunctionParameter) {
            walkFunctionParameter((FunctionParameter)obj);
            return;
        }
        if (obj instanceof FunctionStatement) {
            walkFunctionStatement((FunctionStatement)obj);
            return;
        }
        if (obj instanceof Invariant) {
            walkInvariant((Invariant)obj);
            return;
        }
        if (obj instanceof IoDecl) {
            walkIoDecl((IoDecl)obj);
            return;
        }
        if (obj instanceof Location) {
            walkLocation((Location)obj);
            return;
        }
        if (obj instanceof Monitors) {
            walkMonitors((Monitors)obj);
            return;
        }
        if (obj instanceof Parameter) {
            walkParameter((Parameter)obj);
            return;
        }
        if (obj instanceof PrintFor) {
            walkPrintFor((PrintFor)obj);
            return;
        }
        if (obj instanceof SvgInEvent) {
            walkSvgInEvent((SvgInEvent)obj);
            return;
        }
        if (obj instanceof SvgInEventIfEntry) {
            walkSvgInEventIfEntry((SvgInEventIfEntry)obj);
            return;
        }
        if (obj instanceof SwitchCase) {
            walkSwitchCase((SwitchCase)obj);
            return;
        }
        if (obj instanceof Update) {
            walkUpdate((Update)obj);
            return;
        }
        if (obj instanceof VariableValue) {
            walkVariableValue((VariableValue)obj);
            return;
        }
        String msg = "No redirect; unexpected object type: " + obj;
        throw new IllegalArgumentException(msg);
    }

    /**
     * Pre-crawling function for the {@link PositionObject} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlPositionObject(PositionObject obj) {
        preprocessPositionObject(obj);
    }

    /**
     * Post-crawling function for the {@link PositionObject} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlPositionObject(PositionObject obj) {
        postprocessPositionObject(obj);
    }

    /**
     * Pre-processing function for the {@link PositionObject} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessPositionObject(PositionObject obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link PositionObject} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessPositionObject(PositionObject obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link Print} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkPrint(Print obj) {
        precrawlPrint(obj);
        PrintFile _file = obj.getFile();
        if (_file != null) {
            walkPrintFile(_file);
        }
        List<PrintFor> _fors = obj.getFors();
        for (PrintFor x: _fors) {
            walkPrintFor(x);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        Expression _txtPost = obj.getTxtPost();
        if (_txtPost != null) {
            walkExpression(_txtPost);
        }
        Expression _txtPre = obj.getTxtPre();
        if (_txtPre != null) {
            walkExpression(_txtPre);
        }
        Expression _whenPost = obj.getWhenPost();
        if (_whenPost != null) {
            walkExpression(_whenPost);
        }
        Expression _whenPre = obj.getWhenPre();
        if (_whenPre != null) {
            walkExpression(_whenPre);
        }
        postcrawlPrint(obj);
    }

    /**
     * Pre-crawling function for the {@link Print} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlPrint(Print obj) {
        precrawlIoDecl(obj);
        preprocessPrint(obj);
    }

    /**
     * Post-crawling function for the {@link Print} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlPrint(Print obj) {
        postprocessPrint(obj);
        postcrawlIoDecl(obj);
    }

    /**
     * Pre-processing function for the {@link Print} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessPrint(Print obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link Print} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessPrint(Print obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link PrintFile} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkPrintFile(PrintFile obj) {
        precrawlPrintFile(obj);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        postcrawlPrintFile(obj);
    }

    /**
     * Pre-crawling function for the {@link PrintFile} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlPrintFile(PrintFile obj) {
        precrawlIoDecl(obj);
        preprocessPrintFile(obj);
    }

    /**
     * Post-crawling function for the {@link PrintFile} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlPrintFile(PrintFile obj) {
        postprocessPrintFile(obj);
        postcrawlIoDecl(obj);
    }

    /**
     * Pre-processing function for the {@link PrintFile} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessPrintFile(PrintFile obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link PrintFile} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessPrintFile(PrintFile obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link PrintFor} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkPrintFor(PrintFor obj) {
        precrawlPrintFor(obj);
        Expression _event = obj.getEvent();
        if (_event != null) {
            walkExpression(_event);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        postcrawlPrintFor(obj);
    }

    /**
     * Pre-crawling function for the {@link PrintFor} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlPrintFor(PrintFor obj) {
        precrawlPositionObject(obj);
        preprocessPrintFor(obj);
    }

    /**
     * Post-crawling function for the {@link PrintFor} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlPrintFor(PrintFor obj) {
        postprocessPrintFor(obj);
        postcrawlPositionObject(obj);
    }

    /**
     * Pre-processing function for the {@link PrintFor} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessPrintFor(PrintFor obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link PrintFor} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessPrintFor(PrintFor obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link ProjectionExpression} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkProjectionExpression(ProjectionExpression obj) {
        precrawlProjectionExpression(obj);
        Expression _child = obj.getChild();
        walkExpression(_child);
        Expression _index = obj.getIndex();
        walkExpression(_index);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        CifType _type = obj.getType();
        walkCifType(_type);
        postcrawlProjectionExpression(obj);
    }

    /**
     * Pre-crawling function for the {@link ProjectionExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlProjectionExpression(ProjectionExpression obj) {
        precrawlExpression(obj);
        preprocessProjectionExpression(obj);
    }

    /**
     * Post-crawling function for the {@link ProjectionExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlProjectionExpression(ProjectionExpression obj) {
        postprocessProjectionExpression(obj);
        postcrawlExpression(obj);
    }

    /**
     * Pre-processing function for the {@link ProjectionExpression} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessProjectionExpression(ProjectionExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link ProjectionExpression} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessProjectionExpression(ProjectionExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link RealExpression} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkRealExpression(RealExpression obj) {
        precrawlRealExpression(obj);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        CifType _type = obj.getType();
        walkCifType(_type);
        postcrawlRealExpression(obj);
    }

    /**
     * Pre-crawling function for the {@link RealExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlRealExpression(RealExpression obj) {
        precrawlExpression(obj);
        preprocessRealExpression(obj);
    }

    /**
     * Post-crawling function for the {@link RealExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlRealExpression(RealExpression obj) {
        postprocessRealExpression(obj);
        postcrawlExpression(obj);
    }

    /**
     * Pre-processing function for the {@link RealExpression} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessRealExpression(RealExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link RealExpression} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessRealExpression(RealExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link RealType} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkRealType(RealType obj) {
        precrawlRealType(obj);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        postcrawlRealType(obj);
    }

    /**
     * Pre-crawling function for the {@link RealType} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlRealType(RealType obj) {
        precrawlCifType(obj);
        preprocessRealType(obj);
    }

    /**
     * Post-crawling function for the {@link RealType} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlRealType(RealType obj) {
        postprocessRealType(obj);
        postcrawlCifType(obj);
    }

    /**
     * Pre-processing function for the {@link RealType} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessRealType(RealType obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link RealType} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessRealType(RealType obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link ReceivedExpression} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkReceivedExpression(ReceivedExpression obj) {
        precrawlReceivedExpression(obj);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        CifType _type = obj.getType();
        walkCifType(_type);
        postcrawlReceivedExpression(obj);
    }

    /**
     * Pre-crawling function for the {@link ReceivedExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlReceivedExpression(ReceivedExpression obj) {
        precrawlExpression(obj);
        preprocessReceivedExpression(obj);
    }

    /**
     * Post-crawling function for the {@link ReceivedExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlReceivedExpression(ReceivedExpression obj) {
        postprocessReceivedExpression(obj);
        postcrawlExpression(obj);
    }

    /**
     * Pre-processing function for the {@link ReceivedExpression} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessReceivedExpression(ReceivedExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link ReceivedExpression} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessReceivedExpression(ReceivedExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link ReturnFuncStatement} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkReturnFuncStatement(ReturnFuncStatement obj) {
        precrawlReturnFuncStatement(obj);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        List<Expression> _values = obj.getValues();
        for (Expression x: _values) {
            walkExpression(x);
        }
        postcrawlReturnFuncStatement(obj);
    }

    /**
     * Pre-crawling function for the {@link ReturnFuncStatement} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlReturnFuncStatement(ReturnFuncStatement obj) {
        precrawlFunctionStatement(obj);
        preprocessReturnFuncStatement(obj);
    }

    /**
     * Post-crawling function for the {@link ReturnFuncStatement} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlReturnFuncStatement(ReturnFuncStatement obj) {
        postprocessReturnFuncStatement(obj);
        postcrawlFunctionStatement(obj);
    }

    /**
     * Pre-processing function for the {@link ReturnFuncStatement} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessReturnFuncStatement(ReturnFuncStatement obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link ReturnFuncStatement} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessReturnFuncStatement(ReturnFuncStatement obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link SelfExpression} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkSelfExpression(SelfExpression obj) {
        precrawlSelfExpression(obj);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        CifType _type = obj.getType();
        walkCifType(_type);
        postcrawlSelfExpression(obj);
    }

    /**
     * Pre-crawling function for the {@link SelfExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlSelfExpression(SelfExpression obj) {
        precrawlExpression(obj);
        preprocessSelfExpression(obj);
    }

    /**
     * Post-crawling function for the {@link SelfExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlSelfExpression(SelfExpression obj) {
        postprocessSelfExpression(obj);
        postcrawlExpression(obj);
    }

    /**
     * Pre-processing function for the {@link SelfExpression} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessSelfExpression(SelfExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link SelfExpression} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessSelfExpression(SelfExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link SetExpression} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkSetExpression(SetExpression obj) {
        precrawlSetExpression(obj);
        List<Expression> _elements = obj.getElements();
        for (Expression x: _elements) {
            walkExpression(x);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        CifType _type = obj.getType();
        walkCifType(_type);
        postcrawlSetExpression(obj);
    }

    /**
     * Pre-crawling function for the {@link SetExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlSetExpression(SetExpression obj) {
        precrawlExpression(obj);
        preprocessSetExpression(obj);
    }

    /**
     * Post-crawling function for the {@link SetExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlSetExpression(SetExpression obj) {
        postprocessSetExpression(obj);
        postcrawlExpression(obj);
    }

    /**
     * Pre-processing function for the {@link SetExpression} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessSetExpression(SetExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link SetExpression} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessSetExpression(SetExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link SetType} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkSetType(SetType obj) {
        precrawlSetType(obj);
        CifType _elementType = obj.getElementType();
        walkCifType(_elementType);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        postcrawlSetType(obj);
    }

    /**
     * Pre-crawling function for the {@link SetType} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlSetType(SetType obj) {
        precrawlCifType(obj);
        preprocessSetType(obj);
    }

    /**
     * Post-crawling function for the {@link SetType} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlSetType(SetType obj) {
        postprocessSetType(obj);
        postcrawlCifType(obj);
    }

    /**
     * Pre-processing function for the {@link SetType} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessSetType(SetType obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link SetType} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessSetType(SetType obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link SliceExpression} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkSliceExpression(SliceExpression obj) {
        precrawlSliceExpression(obj);
        Expression _begin = obj.getBegin();
        if (_begin != null) {
            walkExpression(_begin);
        }
        Expression _child = obj.getChild();
        walkExpression(_child);
        Expression _end = obj.getEnd();
        if (_end != null) {
            walkExpression(_end);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        CifType _type = obj.getType();
        walkCifType(_type);
        postcrawlSliceExpression(obj);
    }

    /**
     * Pre-crawling function for the {@link SliceExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlSliceExpression(SliceExpression obj) {
        precrawlExpression(obj);
        preprocessSliceExpression(obj);
    }

    /**
     * Post-crawling function for the {@link SliceExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlSliceExpression(SliceExpression obj) {
        postprocessSliceExpression(obj);
        postcrawlExpression(obj);
    }

    /**
     * Pre-processing function for the {@link SliceExpression} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessSliceExpression(SliceExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link SliceExpression} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessSliceExpression(SliceExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link Specification} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkSpecification(Specification obj) {
        precrawlSpecification(obj);
        List<Component> _components = obj.getComponents();
        for (Component x: _components) {
            walkComponent(x);
        }
        List<Declaration> _declarations = obj.getDeclarations();
        for (Declaration x: _declarations) {
            walkDeclaration(x);
        }
        List<ComponentDef> _definitions = obj.getDefinitions();
        for (ComponentDef x: _definitions) {
            walkComponentDef(x);
        }
        List<Equation> _equations = obj.getEquations();
        for (Equation x: _equations) {
            walkEquation(x);
        }
        List<Expression> _initials = obj.getInitials();
        for (Expression x: _initials) {
            walkExpression(x);
        }
        List<Invariant> _invariants = obj.getInvariants();
        for (Invariant x: _invariants) {
            walkInvariant(x);
        }
        List<IoDecl> _ioDecls = obj.getIoDecls();
        for (IoDecl x: _ioDecls) {
            walkIoDecl(x);
        }
        List<Expression> _markeds = obj.getMarkeds();
        for (Expression x: _markeds) {
            walkExpression(x);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        postcrawlSpecification(obj);
    }

    /**
     * Pre-crawling function for the {@link Specification} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlSpecification(Specification obj) {
        precrawlGroup(obj);
        preprocessSpecification(obj);
    }

    /**
     * Post-crawling function for the {@link Specification} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlSpecification(Specification obj) {
        postprocessSpecification(obj);
        postcrawlGroup(obj);
    }

    /**
     * Pre-processing function for the {@link Specification} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessSpecification(Specification obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link Specification} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessSpecification(Specification obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link StdLibFunctionExpression} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkStdLibFunctionExpression(StdLibFunctionExpression obj) {
        precrawlStdLibFunctionExpression(obj);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        CifType _type = obj.getType();
        walkCifType(_type);
        postcrawlStdLibFunctionExpression(obj);
    }

    /**
     * Pre-crawling function for the {@link StdLibFunctionExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlStdLibFunctionExpression(StdLibFunctionExpression obj) {
        precrawlBaseFunctionExpression(obj);
        preprocessStdLibFunctionExpression(obj);
    }

    /**
     * Post-crawling function for the {@link StdLibFunctionExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlStdLibFunctionExpression(StdLibFunctionExpression obj) {
        postprocessStdLibFunctionExpression(obj);
        postcrawlBaseFunctionExpression(obj);
    }

    /**
     * Pre-processing function for the {@link StdLibFunctionExpression} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessStdLibFunctionExpression(StdLibFunctionExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link StdLibFunctionExpression} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessStdLibFunctionExpression(StdLibFunctionExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link StringExpression} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkStringExpression(StringExpression obj) {
        precrawlStringExpression(obj);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        CifType _type = obj.getType();
        walkCifType(_type);
        postcrawlStringExpression(obj);
    }

    /**
     * Pre-crawling function for the {@link StringExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlStringExpression(StringExpression obj) {
        precrawlExpression(obj);
        preprocessStringExpression(obj);
    }

    /**
     * Post-crawling function for the {@link StringExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlStringExpression(StringExpression obj) {
        postprocessStringExpression(obj);
        postcrawlExpression(obj);
    }

    /**
     * Pre-processing function for the {@link StringExpression} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessStringExpression(StringExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link StringExpression} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessStringExpression(StringExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link StringType} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkStringType(StringType obj) {
        precrawlStringType(obj);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        postcrawlStringType(obj);
    }

    /**
     * Pre-crawling function for the {@link StringType} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlStringType(StringType obj) {
        precrawlCifType(obj);
        preprocessStringType(obj);
    }

    /**
     * Post-crawling function for the {@link StringType} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlStringType(StringType obj) {
        postprocessStringType(obj);
        postcrawlCifType(obj);
    }

    /**
     * Pre-processing function for the {@link StringType} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessStringType(StringType obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link StringType} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessStringType(StringType obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link SvgCopy} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkSvgCopy(SvgCopy obj) {
        precrawlSvgCopy(obj);
        Expression _id = obj.getId();
        walkExpression(_id);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        Expression _post = obj.getPost();
        if (_post != null) {
            walkExpression(_post);
        }
        Expression _pre = obj.getPre();
        if (_pre != null) {
            walkExpression(_pre);
        }
        SvgFile _svgFile = obj.getSvgFile();
        if (_svgFile != null) {
            walkSvgFile(_svgFile);
        }
        postcrawlSvgCopy(obj);
    }

    /**
     * Pre-crawling function for the {@link SvgCopy} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlSvgCopy(SvgCopy obj) {
        precrawlIoDecl(obj);
        preprocessSvgCopy(obj);
    }

    /**
     * Post-crawling function for the {@link SvgCopy} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlSvgCopy(SvgCopy obj) {
        postprocessSvgCopy(obj);
        postcrawlIoDecl(obj);
    }

    /**
     * Pre-processing function for the {@link SvgCopy} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessSvgCopy(SvgCopy obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link SvgCopy} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessSvgCopy(SvgCopy obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link SvgFile} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkSvgFile(SvgFile obj) {
        precrawlSvgFile(obj);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        postcrawlSvgFile(obj);
    }

    /**
     * Pre-crawling function for the {@link SvgFile} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlSvgFile(SvgFile obj) {
        precrawlIoDecl(obj);
        preprocessSvgFile(obj);
    }

    /**
     * Post-crawling function for the {@link SvgFile} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlSvgFile(SvgFile obj) {
        postprocessSvgFile(obj);
        postcrawlIoDecl(obj);
    }

    /**
     * Pre-processing function for the {@link SvgFile} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessSvgFile(SvgFile obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link SvgFile} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessSvgFile(SvgFile obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link SvgIn} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkSvgIn(SvgIn obj) {
        precrawlSvgIn(obj);
        SvgInEvent _event = obj.getEvent();
        walkSvgInEvent(_event);
        Expression _id = obj.getId();
        walkExpression(_id);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        SvgFile _svgFile = obj.getSvgFile();
        if (_svgFile != null) {
            walkSvgFile(_svgFile);
        }
        postcrawlSvgIn(obj);
    }

    /**
     * Pre-crawling function for the {@link SvgIn} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlSvgIn(SvgIn obj) {
        precrawlIoDecl(obj);
        preprocessSvgIn(obj);
    }

    /**
     * Post-crawling function for the {@link SvgIn} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlSvgIn(SvgIn obj) {
        postprocessSvgIn(obj);
        postcrawlIoDecl(obj);
    }

    /**
     * Pre-processing function for the {@link SvgIn} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessSvgIn(SvgIn obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link SvgIn} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessSvgIn(SvgIn obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link SvgInEvent} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkSvgInEvent(SvgInEvent obj) {
        if (obj instanceof SvgInEventIf) {
            walkSvgInEventIf((SvgInEventIf)obj);
            return;
        }
        if (obj instanceof SvgInEventSingle) {
            walkSvgInEventSingle((SvgInEventSingle)obj);
            return;
        }
        String msg = "No redirect; unexpected object type: " + obj;
        throw new IllegalArgumentException(msg);
    }

    /**
     * Pre-crawling function for the {@link SvgInEvent} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlSvgInEvent(SvgInEvent obj) {
        precrawlPositionObject(obj);
        preprocessSvgInEvent(obj);
    }

    /**
     * Post-crawling function for the {@link SvgInEvent} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlSvgInEvent(SvgInEvent obj) {
        postprocessSvgInEvent(obj);
        postcrawlPositionObject(obj);
    }

    /**
     * Pre-processing function for the {@link SvgInEvent} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessSvgInEvent(SvgInEvent obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link SvgInEvent} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessSvgInEvent(SvgInEvent obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link SvgInEventIf} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkSvgInEventIf(SvgInEventIf obj) {
        precrawlSvgInEventIf(obj);
        List<SvgInEventIfEntry> _entries = obj.getEntries();
        for (SvgInEventIfEntry x: _entries) {
            walkSvgInEventIfEntry(x);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        postcrawlSvgInEventIf(obj);
    }

    /**
     * Pre-crawling function for the {@link SvgInEventIf} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlSvgInEventIf(SvgInEventIf obj) {
        precrawlSvgInEvent(obj);
        preprocessSvgInEventIf(obj);
    }

    /**
     * Post-crawling function for the {@link SvgInEventIf} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlSvgInEventIf(SvgInEventIf obj) {
        postprocessSvgInEventIf(obj);
        postcrawlSvgInEvent(obj);
    }

    /**
     * Pre-processing function for the {@link SvgInEventIf} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessSvgInEventIf(SvgInEventIf obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link SvgInEventIf} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessSvgInEventIf(SvgInEventIf obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link SvgInEventIfEntry} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkSvgInEventIfEntry(SvgInEventIfEntry obj) {
        precrawlSvgInEventIfEntry(obj);
        Expression _event = obj.getEvent();
        walkExpression(_event);
        Expression _guard = obj.getGuard();
        if (_guard != null) {
            walkExpression(_guard);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        postcrawlSvgInEventIfEntry(obj);
    }

    /**
     * Pre-crawling function for the {@link SvgInEventIfEntry} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlSvgInEventIfEntry(SvgInEventIfEntry obj) {
        precrawlPositionObject(obj);
        preprocessSvgInEventIfEntry(obj);
    }

    /**
     * Post-crawling function for the {@link SvgInEventIfEntry} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlSvgInEventIfEntry(SvgInEventIfEntry obj) {
        postprocessSvgInEventIfEntry(obj);
        postcrawlPositionObject(obj);
    }

    /**
     * Pre-processing function for the {@link SvgInEventIfEntry} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessSvgInEventIfEntry(SvgInEventIfEntry obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link SvgInEventIfEntry} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessSvgInEventIfEntry(SvgInEventIfEntry obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link SvgInEventSingle} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkSvgInEventSingle(SvgInEventSingle obj) {
        precrawlSvgInEventSingle(obj);
        Expression _event = obj.getEvent();
        walkExpression(_event);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        postcrawlSvgInEventSingle(obj);
    }

    /**
     * Pre-crawling function for the {@link SvgInEventSingle} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlSvgInEventSingle(SvgInEventSingle obj) {
        precrawlSvgInEvent(obj);
        preprocessSvgInEventSingle(obj);
    }

    /**
     * Post-crawling function for the {@link SvgInEventSingle} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlSvgInEventSingle(SvgInEventSingle obj) {
        postprocessSvgInEventSingle(obj);
        postcrawlSvgInEvent(obj);
    }

    /**
     * Pre-processing function for the {@link SvgInEventSingle} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessSvgInEventSingle(SvgInEventSingle obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link SvgInEventSingle} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessSvgInEventSingle(SvgInEventSingle obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link SvgMove} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkSvgMove(SvgMove obj) {
        precrawlSvgMove(obj);
        Expression _id = obj.getId();
        walkExpression(_id);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        SvgFile _svgFile = obj.getSvgFile();
        if (_svgFile != null) {
            walkSvgFile(_svgFile);
        }
        Expression _x = obj.getX();
        walkExpression(_x);
        Expression _y = obj.getY();
        walkExpression(_y);
        postcrawlSvgMove(obj);
    }

    /**
     * Pre-crawling function for the {@link SvgMove} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlSvgMove(SvgMove obj) {
        precrawlIoDecl(obj);
        preprocessSvgMove(obj);
    }

    /**
     * Post-crawling function for the {@link SvgMove} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlSvgMove(SvgMove obj) {
        postprocessSvgMove(obj);
        postcrawlIoDecl(obj);
    }

    /**
     * Pre-processing function for the {@link SvgMove} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessSvgMove(SvgMove obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link SvgMove} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessSvgMove(SvgMove obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link SvgOut} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkSvgOut(SvgOut obj) {
        precrawlSvgOut(obj);
        Position _attrTextPos = obj.getAttrTextPos();
        walkPosition(_attrTextPos);
        Expression _id = obj.getId();
        walkExpression(_id);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        SvgFile _svgFile = obj.getSvgFile();
        if (_svgFile != null) {
            walkSvgFile(_svgFile);
        }
        Expression _value = obj.getValue();
        walkExpression(_value);
        postcrawlSvgOut(obj);
    }

    /**
     * Pre-crawling function for the {@link SvgOut} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlSvgOut(SvgOut obj) {
        precrawlIoDecl(obj);
        preprocessSvgOut(obj);
    }

    /**
     * Post-crawling function for the {@link SvgOut} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlSvgOut(SvgOut obj) {
        postprocessSvgOut(obj);
        postcrawlIoDecl(obj);
    }

    /**
     * Pre-processing function for the {@link SvgOut} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessSvgOut(SvgOut obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link SvgOut} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessSvgOut(SvgOut obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link SwitchCase} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkSwitchCase(SwitchCase obj) {
        precrawlSwitchCase(obj);
        Expression _key = obj.getKey();
        if (_key != null) {
            walkExpression(_key);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        Expression _value = obj.getValue();
        walkExpression(_value);
        postcrawlSwitchCase(obj);
    }

    /**
     * Pre-crawling function for the {@link SwitchCase} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlSwitchCase(SwitchCase obj) {
        precrawlPositionObject(obj);
        preprocessSwitchCase(obj);
    }

    /**
     * Post-crawling function for the {@link SwitchCase} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlSwitchCase(SwitchCase obj) {
        postprocessSwitchCase(obj);
        postcrawlPositionObject(obj);
    }

    /**
     * Pre-processing function for the {@link SwitchCase} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessSwitchCase(SwitchCase obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link SwitchCase} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessSwitchCase(SwitchCase obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link SwitchExpression} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkSwitchExpression(SwitchExpression obj) {
        precrawlSwitchExpression(obj);
        List<SwitchCase> _cases = obj.getCases();
        for (SwitchCase x: _cases) {
            walkSwitchCase(x);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        CifType _type = obj.getType();
        walkCifType(_type);
        Expression _value = obj.getValue();
        walkExpression(_value);
        postcrawlSwitchExpression(obj);
    }

    /**
     * Pre-crawling function for the {@link SwitchExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlSwitchExpression(SwitchExpression obj) {
        precrawlExpression(obj);
        preprocessSwitchExpression(obj);
    }

    /**
     * Post-crawling function for the {@link SwitchExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlSwitchExpression(SwitchExpression obj) {
        postprocessSwitchExpression(obj);
        postcrawlExpression(obj);
    }

    /**
     * Pre-processing function for the {@link SwitchExpression} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessSwitchExpression(SwitchExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link SwitchExpression} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessSwitchExpression(SwitchExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link TauExpression} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkTauExpression(TauExpression obj) {
        precrawlTauExpression(obj);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        CifType _type = obj.getType();
        walkCifType(_type);
        postcrawlTauExpression(obj);
    }

    /**
     * Pre-crawling function for the {@link TauExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlTauExpression(TauExpression obj) {
        precrawlExpression(obj);
        preprocessTauExpression(obj);
    }

    /**
     * Post-crawling function for the {@link TauExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlTauExpression(TauExpression obj) {
        postprocessTauExpression(obj);
        postcrawlExpression(obj);
    }

    /**
     * Pre-processing function for the {@link TauExpression} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessTauExpression(TauExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link TauExpression} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessTauExpression(TauExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link TimeExpression} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkTimeExpression(TimeExpression obj) {
        precrawlTimeExpression(obj);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        CifType _type = obj.getType();
        walkCifType(_type);
        postcrawlTimeExpression(obj);
    }

    /**
     * Pre-crawling function for the {@link TimeExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlTimeExpression(TimeExpression obj) {
        precrawlExpression(obj);
        preprocessTimeExpression(obj);
    }

    /**
     * Post-crawling function for the {@link TimeExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlTimeExpression(TimeExpression obj) {
        postprocessTimeExpression(obj);
        postcrawlExpression(obj);
    }

    /**
     * Pre-processing function for the {@link TimeExpression} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessTimeExpression(TimeExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link TimeExpression} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessTimeExpression(TimeExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link TupleExpression} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkTupleExpression(TupleExpression obj) {
        precrawlTupleExpression(obj);
        List<Expression> _fields = obj.getFields();
        for (Expression x: _fields) {
            walkExpression(x);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        CifType _type = obj.getType();
        walkCifType(_type);
        postcrawlTupleExpression(obj);
    }

    /**
     * Pre-crawling function for the {@link TupleExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlTupleExpression(TupleExpression obj) {
        precrawlExpression(obj);
        preprocessTupleExpression(obj);
    }

    /**
     * Post-crawling function for the {@link TupleExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlTupleExpression(TupleExpression obj) {
        postprocessTupleExpression(obj);
        postcrawlExpression(obj);
    }

    /**
     * Pre-processing function for the {@link TupleExpression} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessTupleExpression(TupleExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link TupleExpression} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessTupleExpression(TupleExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link TupleType} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkTupleType(TupleType obj) {
        precrawlTupleType(obj);
        List<Field> _fields = obj.getFields();
        for (Field x: _fields) {
            walkField(x);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        postcrawlTupleType(obj);
    }

    /**
     * Pre-crawling function for the {@link TupleType} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlTupleType(TupleType obj) {
        precrawlCifType(obj);
        preprocessTupleType(obj);
    }

    /**
     * Post-crawling function for the {@link TupleType} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlTupleType(TupleType obj) {
        postprocessTupleType(obj);
        postcrawlCifType(obj);
    }

    /**
     * Pre-processing function for the {@link TupleType} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessTupleType(TupleType obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link TupleType} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessTupleType(TupleType obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link TypeDecl} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkTypeDecl(TypeDecl obj) {
        precrawlTypeDecl(obj);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        CifType _type = obj.getType();
        walkCifType(_type);
        postcrawlTypeDecl(obj);
    }

    /**
     * Pre-crawling function for the {@link TypeDecl} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlTypeDecl(TypeDecl obj) {
        precrawlDeclaration(obj);
        preprocessTypeDecl(obj);
    }

    /**
     * Post-crawling function for the {@link TypeDecl} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlTypeDecl(TypeDecl obj) {
        postprocessTypeDecl(obj);
        postcrawlDeclaration(obj);
    }

    /**
     * Pre-processing function for the {@link TypeDecl} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessTypeDecl(TypeDecl obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link TypeDecl} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessTypeDecl(TypeDecl obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link TypeRef} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkTypeRef(TypeRef obj) {
        precrawlTypeRef(obj);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        postcrawlTypeRef(obj);
    }

    /**
     * Pre-crawling function for the {@link TypeRef} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlTypeRef(TypeRef obj) {
        precrawlCifType(obj);
        preprocessTypeRef(obj);
    }

    /**
     * Post-crawling function for the {@link TypeRef} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlTypeRef(TypeRef obj) {
        postprocessTypeRef(obj);
        postcrawlCifType(obj);
    }

    /**
     * Pre-processing function for the {@link TypeRef} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessTypeRef(TypeRef obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link TypeRef} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessTypeRef(TypeRef obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link UnaryExpression} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkUnaryExpression(UnaryExpression obj) {
        precrawlUnaryExpression(obj);
        Expression _child = obj.getChild();
        walkExpression(_child);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        CifType _type = obj.getType();
        walkCifType(_type);
        postcrawlUnaryExpression(obj);
    }

    /**
     * Pre-crawling function for the {@link UnaryExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlUnaryExpression(UnaryExpression obj) {
        precrawlExpression(obj);
        preprocessUnaryExpression(obj);
    }

    /**
     * Post-crawling function for the {@link UnaryExpression} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlUnaryExpression(UnaryExpression obj) {
        postprocessUnaryExpression(obj);
        postcrawlExpression(obj);
    }

    /**
     * Pre-processing function for the {@link UnaryExpression} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessUnaryExpression(UnaryExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link UnaryExpression} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessUnaryExpression(UnaryExpression obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link Update} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkUpdate(Update obj) {
        if (obj instanceof Assignment) {
            walkAssignment((Assignment)obj);
            return;
        }
        if (obj instanceof IfUpdate) {
            walkIfUpdate((IfUpdate)obj);
            return;
        }
        String msg = "No redirect; unexpected object type: " + obj;
        throw new IllegalArgumentException(msg);
    }

    /**
     * Pre-crawling function for the {@link Update} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlUpdate(Update obj) {
        precrawlPositionObject(obj);
        preprocessUpdate(obj);
    }

    /**
     * Post-crawling function for the {@link Update} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlUpdate(Update obj) {
        postprocessUpdate(obj);
        postcrawlPositionObject(obj);
    }

    /**
     * Pre-processing function for the {@link Update} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessUpdate(Update obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link Update} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessUpdate(Update obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link VariableValue} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkVariableValue(VariableValue obj) {
        precrawlVariableValue(obj);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        List<Expression> _values = obj.getValues();
        for (Expression x: _values) {
            walkExpression(x);
        }
        postcrawlVariableValue(obj);
    }

    /**
     * Pre-crawling function for the {@link VariableValue} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlVariableValue(VariableValue obj) {
        precrawlPositionObject(obj);
        preprocessVariableValue(obj);
    }

    /**
     * Post-crawling function for the {@link VariableValue} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlVariableValue(VariableValue obj) {
        postprocessVariableValue(obj);
        postcrawlPositionObject(obj);
    }

    /**
     * Pre-processing function for the {@link VariableValue} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessVariableValue(VariableValue obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link VariableValue} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessVariableValue(VariableValue obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link VoidType} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkVoidType(VoidType obj) {
        precrawlVoidType(obj);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        postcrawlVoidType(obj);
    }

    /**
     * Pre-crawling function for the {@link VoidType} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlVoidType(VoidType obj) {
        precrawlCifType(obj);
        preprocessVoidType(obj);
    }

    /**
     * Post-crawling function for the {@link VoidType} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlVoidType(VoidType obj) {
        postprocessVoidType(obj);
        postcrawlCifType(obj);
    }

    /**
     * Pre-processing function for the {@link VoidType} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessVoidType(VoidType obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link VoidType} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessVoidType(VoidType obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link WhileFuncStatement} class.
     *
     * @param obj The object to walk over.
     */
    protected void walkWhileFuncStatement(WhileFuncStatement obj) {
        precrawlWhileFuncStatement(obj);
        List<Expression> _guards = obj.getGuards();
        for (Expression x: _guards) {
            walkExpression(x);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position);
        }
        List<FunctionStatement> _statements = obj.getStatements();
        for (FunctionStatement x: _statements) {
            walkFunctionStatement(x);
        }
        postcrawlWhileFuncStatement(obj);
    }

    /**
     * Pre-crawling function for the {@link WhileFuncStatement} class.
     *
     * @param obj The object to crawl over.
     */
    protected void precrawlWhileFuncStatement(WhileFuncStatement obj) {
        precrawlFunctionStatement(obj);
        preprocessWhileFuncStatement(obj);
    }

    /**
     * Post-crawling function for the {@link WhileFuncStatement} class.
     *
     * @param obj The object to crawl over.
     */
    protected void postcrawlWhileFuncStatement(WhileFuncStatement obj) {
        postprocessWhileFuncStatement(obj);
        postcrawlFunctionStatement(obj);
    }

    /**
     * Pre-processing function for the {@link WhileFuncStatement} class.
     *
     * @param obj The object to pre-process.
     */
    protected void preprocessWhileFuncStatement(WhileFuncStatement obj) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link WhileFuncStatement} class.
     *
     * @param obj The object to post-process.
     */
    protected void postprocessWhileFuncStatement(WhileFuncStatement obj) {
        // Derived classes may override this method to do actual processing.
    }
}
