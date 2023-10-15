//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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
import org.eclipse.escet.cif.metamodel.cif.CifFactory;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.ComponentDef;
import org.eclipse.escet.cif.metamodel.cif.ComponentInst;
import org.eclipse.escet.cif.metamodel.cif.ComponentParameter;
import org.eclipse.escet.cif.metamodel.cif.Equation;
import org.eclipse.escet.cif.metamodel.cif.EventParameter;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.InvKind;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.IoDecl;
import org.eclipse.escet.cif.metamodel.cif.LocationParameter;
import org.eclipse.escet.cif.metamodel.cif.Parameter;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.SupKind;
import org.eclipse.escet.cif.metamodel.cif.annotations.Annotation;
import org.eclipse.escet.cif.metamodel.cif.annotations.AnnotationArgument;
import org.eclipse.escet.cif.metamodel.cif.annotations.AnnotationsFactory;
import org.eclipse.escet.cif.metamodel.cif.automata.Alphabet;
import org.eclipse.escet.cif.metamodel.cif.automata.Assignment;
import org.eclipse.escet.cif.metamodel.cif.automata.AutomataFactory;
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
import org.eclipse.escet.cif.metamodel.cif.cifsvg.CifsvgFactory;
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
import org.eclipse.escet.cif.metamodel.cif.declarations.DeclarationsFactory;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumLiteral;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.TypeDecl;
import org.eclipse.escet.cif.metamodel.cif.declarations.VariableValue;
import org.eclipse.escet.cif.metamodel.cif.expressions.AlgVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
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
import org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsFactory;
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
import org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunction;
import org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunctionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.StringExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SwitchCase;
import org.eclipse.escet.cif.metamodel.cif.expressions.SwitchExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TauExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TimeExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TupleExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryOperator;
import org.eclipse.escet.cif.metamodel.cif.functions.AssignmentFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.BreakFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.ContinueFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.ElifFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.ExternalFunction;
import org.eclipse.escet.cif.metamodel.cif.functions.Function;
import org.eclipse.escet.cif.metamodel.cif.functions.FunctionParameter;
import org.eclipse.escet.cif.metamodel.cif.functions.FunctionStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.FunctionsFactory;
import org.eclipse.escet.cif.metamodel.cif.functions.IfFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.InternalFunction;
import org.eclipse.escet.cif.metamodel.cif.functions.ReturnFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.WhileFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.print.Print;
import org.eclipse.escet.cif.metamodel.cif.print.PrintFactory;
import org.eclipse.escet.cif.metamodel.cif.print.PrintFile;
import org.eclipse.escet.cif.metamodel.cif.print.PrintFor;
import org.eclipse.escet.cif.metamodel.cif.print.PrintForKind;
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
import org.eclipse.escet.cif.metamodel.cif.types.TypesFactory;
import org.eclipse.escet.cif.metamodel.cif.types.VoidType;
import org.eclipse.escet.common.position.metamodel.position.Position;

/**
 * Helper class with static constructor methods for the "cif" language.
 */
public class CifConstructors {
    /** Constructor for the {@link CifConstructors} class. */
    private CifConstructors() {
        // Static class.
    }

    /**
     * Returns a new instance of the {@link AlgParameter} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link AlgParameter} class.
     */
    public static AlgParameter newAlgParameter() {
        return CifFactory.eINSTANCE.createAlgParameter();
    }

    /**
     * Returns a new instance of the {@link AlgParameter} class.
     *
     * @param position The "position" of the new "AlgParameter". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param variable The "variable" of the new "AlgParameter". Multiplicity [1..1]. May be {@code null} to set the "variable" later.
     * @return A new instance of the {@link AlgParameter} class.
     */
    public static AlgParameter newAlgParameter(Position position, AlgVariable variable) {
        AlgParameter rslt_ = newAlgParameter();
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (variable != null) {
            rslt_.setVariable(variable);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link AlgVariable} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link AlgVariable} class.
     */
    public static AlgVariable newAlgVariable() {
        return DeclarationsFactory.eINSTANCE.createAlgVariable();
    }

    /**
     * Returns a new instance of the {@link AlgVariable} class.
     *
     * @param annotations The "annotations" of the new "AlgVariable". Multiplicity [0..*]. May be {@code null} to skip setting the "annotations", or to set it later.
     * @param name The "name" of the new "AlgVariable". Multiplicity [1..1]. May be {@code null} to set the "name" later.
     * @param position The "position" of the new "AlgVariable". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "AlgVariable". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @param value The "value" of the new "AlgVariable". Multiplicity [0..1]. May be {@code null} to skip setting the "value", or to set it later.
     * @return A new instance of the {@link AlgVariable} class.
     */
    public static AlgVariable newAlgVariable(List<Annotation> annotations, String name, Position position, CifType type, Expression value) {
        AlgVariable rslt_ = newAlgVariable();
        if (annotations != null) {
            rslt_.getAnnotations().addAll(annotations);
        }
        if (name != null) {
            rslt_.setName(name);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (type != null) {
            rslt_.setType(type);
        }
        if (value != null) {
            rslt_.setValue(value);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link AlgVariableExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link AlgVariableExpression} class.
     */
    public static AlgVariableExpression newAlgVariableExpression() {
        return ExpressionsFactory.eINSTANCE.createAlgVariableExpression();
    }

    /**
     * Returns a new instance of the {@link AlgVariableExpression} class.
     *
     * @param position The "position" of the new "AlgVariableExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "AlgVariableExpression". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @param variable The "variable" of the new "AlgVariableExpression". Multiplicity [1..1]. May be {@code null} to set the "variable" later.
     * @return A new instance of the {@link AlgVariableExpression} class.
     */
    public static AlgVariableExpression newAlgVariableExpression(Position position, CifType type, AlgVariable variable) {
        AlgVariableExpression rslt_ = newAlgVariableExpression();
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (type != null) {
            rslt_.setType(type);
        }
        if (variable != null) {
            rslt_.setVariable(variable);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link Alphabet} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link Alphabet} class.
     */
    public static Alphabet newAlphabet() {
        return AutomataFactory.eINSTANCE.createAlphabet();
    }

    /**
     * Returns a new instance of the {@link Alphabet} class.
     *
     * @param events The "events" of the new "Alphabet". Multiplicity [0..*]. May be {@code null} to skip setting the "events", or to set it later.
     * @param position The "position" of the new "Alphabet". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link Alphabet} class.
     */
    public static Alphabet newAlphabet(List<Expression> events, Position position) {
        Alphabet rslt_ = newAlphabet();
        if (events != null) {
            rslt_.getEvents().addAll(events);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link Annotation} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link Annotation} class.
     */
    public static Annotation newAnnotation() {
        return AnnotationsFactory.eINSTANCE.createAnnotation();
    }

    /**
     * Returns a new instance of the {@link Annotation} class.
     *
     * @param arguments The "arguments" of the new "Annotation". Multiplicity [0..*]. May be {@code null} to skip setting the "arguments", or to set it later.
     * @param name The "name" of the new "Annotation". Multiplicity [1..1]. May be {@code null} to set the "name" later.
     * @param position The "position" of the new "Annotation". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link Annotation} class.
     */
    public static Annotation newAnnotation(List<AnnotationArgument> arguments, String name, Position position) {
        Annotation rslt_ = newAnnotation();
        if (arguments != null) {
            rslt_.getArguments().addAll(arguments);
        }
        if (name != null) {
            rslt_.setName(name);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link AnnotationArgument} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link AnnotationArgument} class.
     */
    public static AnnotationArgument newAnnotationArgument() {
        return AnnotationsFactory.eINSTANCE.createAnnotationArgument();
    }

    /**
     * Returns a new instance of the {@link AnnotationArgument} class.
     *
     * @param name The "name" of the new "AnnotationArgument". Multiplicity [1..1]. May be {@code null} to set the "name" later.
     * @param position The "position" of the new "AnnotationArgument". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param value The "value" of the new "AnnotationArgument". Multiplicity [1..1]. May be {@code null} to set the "value" later.
     * @return A new instance of the {@link AnnotationArgument} class.
     */
    public static AnnotationArgument newAnnotationArgument(String name, Position position, Expression value) {
        AnnotationArgument rslt_ = newAnnotationArgument();
        if (name != null) {
            rslt_.setName(name);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (value != null) {
            rslt_.setValue(value);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link Assignment} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link Assignment} class.
     */
    public static Assignment newAssignment() {
        return AutomataFactory.eINSTANCE.createAssignment();
    }

    /**
     * Returns a new instance of the {@link Assignment} class.
     *
     * @param addressable The "addressable" of the new "Assignment". Multiplicity [1..1]. May be {@code null} to set the "addressable" later.
     * @param position The "position" of the new "Assignment". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param value The "value" of the new "Assignment". Multiplicity [1..1]. May be {@code null} to set the "value" later.
     * @return A new instance of the {@link Assignment} class.
     */
    public static Assignment newAssignment(Expression addressable, Position position, Expression value) {
        Assignment rslt_ = newAssignment();
        if (addressable != null) {
            rslt_.setAddressable(addressable);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (value != null) {
            rslt_.setValue(value);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link AssignmentFuncStatement} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link AssignmentFuncStatement} class.
     */
    public static AssignmentFuncStatement newAssignmentFuncStatement() {
        return FunctionsFactory.eINSTANCE.createAssignmentFuncStatement();
    }

    /**
     * Returns a new instance of the {@link AssignmentFuncStatement} class.
     *
     * @param addressable The "addressable" of the new "AssignmentFuncStatement". Multiplicity [1..1]. May be {@code null} to set the "addressable" later.
     * @param position The "position" of the new "AssignmentFuncStatement". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param value The "value" of the new "AssignmentFuncStatement". Multiplicity [1..1]. May be {@code null} to set the "value" later.
     * @return A new instance of the {@link AssignmentFuncStatement} class.
     */
    public static AssignmentFuncStatement newAssignmentFuncStatement(Expression addressable, Position position, Expression value) {
        AssignmentFuncStatement rslt_ = newAssignmentFuncStatement();
        if (addressable != null) {
            rslt_.setAddressable(addressable);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (value != null) {
            rslt_.setValue(value);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link Automaton} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link Automaton} class.
     */
    public static Automaton newAutomaton() {
        return AutomataFactory.eINSTANCE.createAutomaton();
    }

    /**
     * Returns a new instance of the {@link Automaton} class.
     *
     * @param alphabet The "alphabet" of the new "Automaton". Multiplicity [0..1]. May be {@code null} to skip setting the "alphabet", or to set it later.
     * @param declarations The "declarations" of the new "Automaton". Multiplicity [0..*]. May be {@code null} to skip setting the "declarations", or to set it later.
     * @param equations The "equations" of the new "Automaton". Multiplicity [0..*]. May be {@code null} to skip setting the "equations", or to set it later.
     * @param initials The "initials" of the new "Automaton". Multiplicity [0..*]. May be {@code null} to skip setting the "initials", or to set it later.
     * @param invariants The "invariants" of the new "Automaton". Multiplicity [0..*]. May be {@code null} to skip setting the "invariants", or to set it later.
     * @param ioDecls The "ioDecls" of the new "Automaton". Multiplicity [0..*]. May be {@code null} to skip setting the "ioDecls", or to set it later.
     * @param kind The "kind" of the new "Automaton". Multiplicity [1..1]. May be {@code null} to set the "kind" later.
     * @param locations The "locations" of the new "Automaton". Multiplicity [1..*]. May be {@code null} to set the "locations" later.
     * @param markeds The "markeds" of the new "Automaton". Multiplicity [0..*]. May be {@code null} to skip setting the "markeds", or to set it later.
     * @param monitors The "monitors" of the new "Automaton". Multiplicity [0..1]. May be {@code null} to skip setting the "monitors", or to set it later.
     * @param name The "name" of the new "Automaton". Multiplicity [1..1]. May be {@code null} to set the "name" later.
     * @param position The "position" of the new "Automaton". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link Automaton} class.
     */
    public static Automaton newAutomaton(Alphabet alphabet, List<Declaration> declarations, List<Equation> equations, List<Expression> initials, List<Invariant> invariants, List<IoDecl> ioDecls, SupKind kind, List<Location> locations, List<Expression> markeds, Monitors monitors, String name, Position position) {
        Automaton rslt_ = newAutomaton();
        if (alphabet != null) {
            rslt_.setAlphabet(alphabet);
        }
        if (declarations != null) {
            rslt_.getDeclarations().addAll(declarations);
        }
        if (equations != null) {
            rslt_.getEquations().addAll(equations);
        }
        if (initials != null) {
            rslt_.getInitials().addAll(initials);
        }
        if (invariants != null) {
            rslt_.getInvariants().addAll(invariants);
        }
        if (ioDecls != null) {
            rslt_.getIoDecls().addAll(ioDecls);
        }
        if (kind != null) {
            rslt_.setKind(kind);
        }
        if (locations != null) {
            rslt_.getLocations().addAll(locations);
        }
        if (markeds != null) {
            rslt_.getMarkeds().addAll(markeds);
        }
        if (monitors != null) {
            rslt_.setMonitors(monitors);
        }
        if (name != null) {
            rslt_.setName(name);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link BinaryExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link BinaryExpression} class.
     */
    public static BinaryExpression newBinaryExpression() {
        return ExpressionsFactory.eINSTANCE.createBinaryExpression();
    }

    /**
     * Returns a new instance of the {@link BinaryExpression} class.
     *
     * @param left The "left" of the new "BinaryExpression". Multiplicity [1..1]. May be {@code null} to set the "left" later.
     * @param operator The "operator" of the new "BinaryExpression". Multiplicity [1..1]. May be {@code null} to set the "operator" later.
     * @param position The "position" of the new "BinaryExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param right The "right" of the new "BinaryExpression". Multiplicity [1..1]. May be {@code null} to set the "right" later.
     * @param type The "type" of the new "BinaryExpression". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @return A new instance of the {@link BinaryExpression} class.
     */
    public static BinaryExpression newBinaryExpression(Expression left, BinaryOperator operator, Position position, Expression right, CifType type) {
        BinaryExpression rslt_ = newBinaryExpression();
        if (left != null) {
            rslt_.setLeft(left);
        }
        if (operator != null) {
            rslt_.setOperator(operator);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (right != null) {
            rslt_.setRight(right);
        }
        if (type != null) {
            rslt_.setType(type);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link BoolExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link BoolExpression} class.
     */
    public static BoolExpression newBoolExpression() {
        return ExpressionsFactory.eINSTANCE.createBoolExpression();
    }

    /**
     * Returns a new instance of the {@link BoolExpression} class.
     *
     * @param position The "position" of the new "BoolExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "BoolExpression". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @param value The "value" of the new "BoolExpression". Multiplicity [1..1]. May be {@code null} to set the "value" later.
     * @return A new instance of the {@link BoolExpression} class.
     */
    public static BoolExpression newBoolExpression(Position position, CifType type, Boolean value) {
        BoolExpression rslt_ = newBoolExpression();
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (type != null) {
            rslt_.setType(type);
        }
        if (value != null) {
            rslt_.setValue(value);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link BoolType} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link BoolType} class.
     */
    public static BoolType newBoolType() {
        return TypesFactory.eINSTANCE.createBoolType();
    }

    /**
     * Returns a new instance of the {@link BoolType} class.
     *
     * @param position The "position" of the new "BoolType". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link BoolType} class.
     */
    public static BoolType newBoolType(Position position) {
        BoolType rslt_ = newBoolType();
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link BreakFuncStatement} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link BreakFuncStatement} class.
     */
    public static BreakFuncStatement newBreakFuncStatement() {
        return FunctionsFactory.eINSTANCE.createBreakFuncStatement();
    }

    /**
     * Returns a new instance of the {@link BreakFuncStatement} class.
     *
     * @param position The "position" of the new "BreakFuncStatement". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link BreakFuncStatement} class.
     */
    public static BreakFuncStatement newBreakFuncStatement(Position position) {
        BreakFuncStatement rslt_ = newBreakFuncStatement();
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link CastExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link CastExpression} class.
     */
    public static CastExpression newCastExpression() {
        return ExpressionsFactory.eINSTANCE.createCastExpression();
    }

    /**
     * Returns a new instance of the {@link CastExpression} class.
     *
     * @param child The "child" of the new "CastExpression". Multiplicity [1..1]. May be {@code null} to set the "child" later.
     * @param position The "position" of the new "CastExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "CastExpression". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @return A new instance of the {@link CastExpression} class.
     */
    public static CastExpression newCastExpression(Expression child, Position position, CifType type) {
        CastExpression rslt_ = newCastExpression();
        if (child != null) {
            rslt_.setChild(child);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (type != null) {
            rslt_.setType(type);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link CompInstWrapExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link CompInstWrapExpression} class.
     */
    public static CompInstWrapExpression newCompInstWrapExpression() {
        return ExpressionsFactory.eINSTANCE.createCompInstWrapExpression();
    }

    /**
     * Returns a new instance of the {@link CompInstWrapExpression} class.
     *
     * @param instantiation The "instantiation" of the new "CompInstWrapExpression". Multiplicity [1..1]. May be {@code null} to set the "instantiation" later.
     * @param position The "position" of the new "CompInstWrapExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param reference The "reference" of the new "CompInstWrapExpression". Multiplicity [1..1]. May be {@code null} to set the "reference" later.
     * @param type The "type" of the new "CompInstWrapExpression". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @return A new instance of the {@link CompInstWrapExpression} class.
     */
    public static CompInstWrapExpression newCompInstWrapExpression(ComponentInst instantiation, Position position, Expression reference, CifType type) {
        CompInstWrapExpression rslt_ = newCompInstWrapExpression();
        if (instantiation != null) {
            rslt_.setInstantiation(instantiation);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (reference != null) {
            rslt_.setReference(reference);
        }
        if (type != null) {
            rslt_.setType(type);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link CompInstWrapType} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link CompInstWrapType} class.
     */
    public static CompInstWrapType newCompInstWrapType() {
        return TypesFactory.eINSTANCE.createCompInstWrapType();
    }

    /**
     * Returns a new instance of the {@link CompInstWrapType} class.
     *
     * @param instantiation The "instantiation" of the new "CompInstWrapType". Multiplicity [1..1]. May be {@code null} to set the "instantiation" later.
     * @param position The "position" of the new "CompInstWrapType". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param reference The "reference" of the new "CompInstWrapType". Multiplicity [1..1]. May be {@code null} to set the "reference" later.
     * @return A new instance of the {@link CompInstWrapType} class.
     */
    public static CompInstWrapType newCompInstWrapType(ComponentInst instantiation, Position position, CifType reference) {
        CompInstWrapType rslt_ = newCompInstWrapType();
        if (instantiation != null) {
            rslt_.setInstantiation(instantiation);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (reference != null) {
            rslt_.setReference(reference);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link CompParamExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link CompParamExpression} class.
     */
    public static CompParamExpression newCompParamExpression() {
        return ExpressionsFactory.eINSTANCE.createCompParamExpression();
    }

    /**
     * Returns a new instance of the {@link CompParamExpression} class.
     *
     * @param parameter The "parameter" of the new "CompParamExpression". Multiplicity [1..1]. May be {@code null} to set the "parameter" later.
     * @param position The "position" of the new "CompParamExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "CompParamExpression". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @return A new instance of the {@link CompParamExpression} class.
     */
    public static CompParamExpression newCompParamExpression(ComponentParameter parameter, Position position, CifType type) {
        CompParamExpression rslt_ = newCompParamExpression();
        if (parameter != null) {
            rslt_.setParameter(parameter);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (type != null) {
            rslt_.setType(type);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link CompParamWrapExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link CompParamWrapExpression} class.
     */
    public static CompParamWrapExpression newCompParamWrapExpression() {
        return ExpressionsFactory.eINSTANCE.createCompParamWrapExpression();
    }

    /**
     * Returns a new instance of the {@link CompParamWrapExpression} class.
     *
     * @param parameter The "parameter" of the new "CompParamWrapExpression". Multiplicity [1..1]. May be {@code null} to set the "parameter" later.
     * @param position The "position" of the new "CompParamWrapExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param reference The "reference" of the new "CompParamWrapExpression". Multiplicity [1..1]. May be {@code null} to set the "reference" later.
     * @param type The "type" of the new "CompParamWrapExpression". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @return A new instance of the {@link CompParamWrapExpression} class.
     */
    public static CompParamWrapExpression newCompParamWrapExpression(ComponentParameter parameter, Position position, Expression reference, CifType type) {
        CompParamWrapExpression rslt_ = newCompParamWrapExpression();
        if (parameter != null) {
            rslt_.setParameter(parameter);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (reference != null) {
            rslt_.setReference(reference);
        }
        if (type != null) {
            rslt_.setType(type);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link CompParamWrapType} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link CompParamWrapType} class.
     */
    public static CompParamWrapType newCompParamWrapType() {
        return TypesFactory.eINSTANCE.createCompParamWrapType();
    }

    /**
     * Returns a new instance of the {@link CompParamWrapType} class.
     *
     * @param parameter The "parameter" of the new "CompParamWrapType". Multiplicity [1..1]. May be {@code null} to set the "parameter" later.
     * @param position The "position" of the new "CompParamWrapType". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param reference The "reference" of the new "CompParamWrapType". Multiplicity [1..1]. May be {@code null} to set the "reference" later.
     * @return A new instance of the {@link CompParamWrapType} class.
     */
    public static CompParamWrapType newCompParamWrapType(ComponentParameter parameter, Position position, CifType reference) {
        CompParamWrapType rslt_ = newCompParamWrapType();
        if (parameter != null) {
            rslt_.setParameter(parameter);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (reference != null) {
            rslt_.setReference(reference);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link ComponentDef} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link ComponentDef} class.
     */
    public static ComponentDef newComponentDef() {
        return CifFactory.eINSTANCE.createComponentDef();
    }

    /**
     * Returns a new instance of the {@link ComponentDef} class.
     *
     * @param body The "body" of the new "ComponentDef". Multiplicity [1..1]. May be {@code null} to set the "body" later.
     * @param parameters The "parameters" of the new "ComponentDef". Multiplicity [0..*]. May be {@code null} to skip setting the "parameters", or to set it later.
     * @param position The "position" of the new "ComponentDef". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link ComponentDef} class.
     */
    public static ComponentDef newComponentDef(ComplexComponent body, List<Parameter> parameters, Position position) {
        ComponentDef rslt_ = newComponentDef();
        if (body != null) {
            rslt_.setBody(body);
        }
        if (parameters != null) {
            rslt_.getParameters().addAll(parameters);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link ComponentDefType} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link ComponentDefType} class.
     */
    public static ComponentDefType newComponentDefType() {
        return TypesFactory.eINSTANCE.createComponentDefType();
    }

    /**
     * Returns a new instance of the {@link ComponentDefType} class.
     *
     * @param definition The "definition" of the new "ComponentDefType". Multiplicity [1..1]. May be {@code null} to set the "definition" later.
     * @param position The "position" of the new "ComponentDefType". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link ComponentDefType} class.
     */
    public static ComponentDefType newComponentDefType(ComponentDef definition, Position position) {
        ComponentDefType rslt_ = newComponentDefType();
        if (definition != null) {
            rslt_.setDefinition(definition);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link ComponentExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link ComponentExpression} class.
     */
    public static ComponentExpression newComponentExpression() {
        return ExpressionsFactory.eINSTANCE.createComponentExpression();
    }

    /**
     * Returns a new instance of the {@link ComponentExpression} class.
     *
     * @param component The "component" of the new "ComponentExpression". Multiplicity [1..1]. May be {@code null} to set the "component" later.
     * @param position The "position" of the new "ComponentExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "ComponentExpression". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @return A new instance of the {@link ComponentExpression} class.
     */
    public static ComponentExpression newComponentExpression(Component component, Position position, CifType type) {
        ComponentExpression rslt_ = newComponentExpression();
        if (component != null) {
            rslt_.setComponent(component);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (type != null) {
            rslt_.setType(type);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link ComponentInst} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link ComponentInst} class.
     */
    public static ComponentInst newComponentInst() {
        return CifFactory.eINSTANCE.createComponentInst();
    }

    /**
     * Returns a new instance of the {@link ComponentInst} class.
     *
     * @param arguments The "arguments" of the new "ComponentInst". Multiplicity [0..*]. May be {@code null} to skip setting the "arguments", or to set it later.
     * @param definition The "definition" of the new "ComponentInst". Multiplicity [1..1]. May be {@code null} to set the "definition" later.
     * @param name The "name" of the new "ComponentInst". Multiplicity [1..1]. May be {@code null} to set the "name" later.
     * @param position The "position" of the new "ComponentInst". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link ComponentInst} class.
     */
    public static ComponentInst newComponentInst(List<Expression> arguments, CifType definition, String name, Position position) {
        ComponentInst rslt_ = newComponentInst();
        if (arguments != null) {
            rslt_.getArguments().addAll(arguments);
        }
        if (definition != null) {
            rslt_.setDefinition(definition);
        }
        if (name != null) {
            rslt_.setName(name);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link ComponentParameter} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link ComponentParameter} class.
     */
    public static ComponentParameter newComponentParameter() {
        return CifFactory.eINSTANCE.createComponentParameter();
    }

    /**
     * Returns a new instance of the {@link ComponentParameter} class.
     *
     * @param name The "name" of the new "ComponentParameter". Multiplicity [1..1]. May be {@code null} to set the "name" later.
     * @param position The "position" of the new "ComponentParameter". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "ComponentParameter". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @return A new instance of the {@link ComponentParameter} class.
     */
    public static ComponentParameter newComponentParameter(String name, Position position, CifType type) {
        ComponentParameter rslt_ = newComponentParameter();
        if (name != null) {
            rslt_.setName(name);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (type != null) {
            rslt_.setType(type);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link ComponentType} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link ComponentType} class.
     */
    public static ComponentType newComponentType() {
        return TypesFactory.eINSTANCE.createComponentType();
    }

    /**
     * Returns a new instance of the {@link ComponentType} class.
     *
     * @param component The "component" of the new "ComponentType". Multiplicity [1..1]. May be {@code null} to set the "component" later.
     * @param position The "position" of the new "ComponentType". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link ComponentType} class.
     */
    public static ComponentType newComponentType(Component component, Position position) {
        ComponentType rslt_ = newComponentType();
        if (component != null) {
            rslt_.setComponent(component);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link Constant} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link Constant} class.
     */
    public static Constant newConstant() {
        return DeclarationsFactory.eINSTANCE.createConstant();
    }

    /**
     * Returns a new instance of the {@link Constant} class.
     *
     * @param annotations The "annotations" of the new "Constant". Multiplicity [0..*]. May be {@code null} to skip setting the "annotations", or to set it later.
     * @param name The "name" of the new "Constant". Multiplicity [1..1]. May be {@code null} to set the "name" later.
     * @param position The "position" of the new "Constant". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "Constant". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @param value The "value" of the new "Constant". Multiplicity [1..1]. May be {@code null} to set the "value" later.
     * @return A new instance of the {@link Constant} class.
     */
    public static Constant newConstant(List<Annotation> annotations, String name, Position position, CifType type, Expression value) {
        Constant rslt_ = newConstant();
        if (annotations != null) {
            rslt_.getAnnotations().addAll(annotations);
        }
        if (name != null) {
            rslt_.setName(name);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (type != null) {
            rslt_.setType(type);
        }
        if (value != null) {
            rslt_.setValue(value);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link ConstantExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link ConstantExpression} class.
     */
    public static ConstantExpression newConstantExpression() {
        return ExpressionsFactory.eINSTANCE.createConstantExpression();
    }

    /**
     * Returns a new instance of the {@link ConstantExpression} class.
     *
     * @param constant The "constant" of the new "ConstantExpression". Multiplicity [1..1]. May be {@code null} to set the "constant" later.
     * @param position The "position" of the new "ConstantExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "ConstantExpression". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @return A new instance of the {@link ConstantExpression} class.
     */
    public static ConstantExpression newConstantExpression(Constant constant, Position position, CifType type) {
        ConstantExpression rslt_ = newConstantExpression();
        if (constant != null) {
            rslt_.setConstant(constant);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (type != null) {
            rslt_.setType(type);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link ContVariable} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link ContVariable} class.
     */
    public static ContVariable newContVariable() {
        return DeclarationsFactory.eINSTANCE.createContVariable();
    }

    /**
     * Returns a new instance of the {@link ContVariable} class.
     *
     * @param annotations The "annotations" of the new "ContVariable". Multiplicity [0..*]. May be {@code null} to skip setting the "annotations", or to set it later.
     * @param derivative The "derivative" of the new "ContVariable". Multiplicity [0..1]. May be {@code null} to skip setting the "derivative", or to set it later.
     * @param name The "name" of the new "ContVariable". Multiplicity [1..1]. May be {@code null} to set the "name" later.
     * @param position The "position" of the new "ContVariable". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param value The "value" of the new "ContVariable". Multiplicity [0..1]. May be {@code null} to skip setting the "value", or to set it later.
     * @return A new instance of the {@link ContVariable} class.
     */
    public static ContVariable newContVariable(List<Annotation> annotations, Expression derivative, String name, Position position, Expression value) {
        ContVariable rslt_ = newContVariable();
        if (annotations != null) {
            rslt_.getAnnotations().addAll(annotations);
        }
        if (derivative != null) {
            rslt_.setDerivative(derivative);
        }
        if (name != null) {
            rslt_.setName(name);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (value != null) {
            rslt_.setValue(value);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link ContVariableExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link ContVariableExpression} class.
     */
    public static ContVariableExpression newContVariableExpression() {
        return ExpressionsFactory.eINSTANCE.createContVariableExpression();
    }

    /**
     * Returns a new instance of the {@link ContVariableExpression} class.
     *
     * @param derivative The "derivative" of the new "ContVariableExpression". Multiplicity [1..1]. May be {@code null} to set the "derivative" later.
     * @param position The "position" of the new "ContVariableExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "ContVariableExpression". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @param variable The "variable" of the new "ContVariableExpression". Multiplicity [1..1]. May be {@code null} to set the "variable" later.
     * @return A new instance of the {@link ContVariableExpression} class.
     */
    public static ContVariableExpression newContVariableExpression(Boolean derivative, Position position, CifType type, ContVariable variable) {
        ContVariableExpression rslt_ = newContVariableExpression();
        if (derivative != null) {
            rslt_.setDerivative(derivative);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (type != null) {
            rslt_.setType(type);
        }
        if (variable != null) {
            rslt_.setVariable(variable);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link ContinueFuncStatement} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link ContinueFuncStatement} class.
     */
    public static ContinueFuncStatement newContinueFuncStatement() {
        return FunctionsFactory.eINSTANCE.createContinueFuncStatement();
    }

    /**
     * Returns a new instance of the {@link ContinueFuncStatement} class.
     *
     * @param position The "position" of the new "ContinueFuncStatement". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link ContinueFuncStatement} class.
     */
    public static ContinueFuncStatement newContinueFuncStatement(Position position) {
        ContinueFuncStatement rslt_ = newContinueFuncStatement();
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link DictExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link DictExpression} class.
     */
    public static DictExpression newDictExpression() {
        return ExpressionsFactory.eINSTANCE.createDictExpression();
    }

    /**
     * Returns a new instance of the {@link DictExpression} class.
     *
     * @param pairs The "pairs" of the new "DictExpression". Multiplicity [0..*]. May be {@code null} to skip setting the "pairs", or to set it later.
     * @param position The "position" of the new "DictExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "DictExpression". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @return A new instance of the {@link DictExpression} class.
     */
    public static DictExpression newDictExpression(List<DictPair> pairs, Position position, CifType type) {
        DictExpression rslt_ = newDictExpression();
        if (pairs != null) {
            rslt_.getPairs().addAll(pairs);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (type != null) {
            rslt_.setType(type);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link DictPair} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link DictPair} class.
     */
    public static DictPair newDictPair() {
        return ExpressionsFactory.eINSTANCE.createDictPair();
    }

    /**
     * Returns a new instance of the {@link DictPair} class.
     *
     * @param key The "key" of the new "DictPair". Multiplicity [1..1]. May be {@code null} to set the "key" later.
     * @param position The "position" of the new "DictPair". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param value The "value" of the new "DictPair". Multiplicity [1..1]. May be {@code null} to set the "value" later.
     * @return A new instance of the {@link DictPair} class.
     */
    public static DictPair newDictPair(Expression key, Position position, Expression value) {
        DictPair rslt_ = newDictPair();
        if (key != null) {
            rslt_.setKey(key);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (value != null) {
            rslt_.setValue(value);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link DictType} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link DictType} class.
     */
    public static DictType newDictType() {
        return TypesFactory.eINSTANCE.createDictType();
    }

    /**
     * Returns a new instance of the {@link DictType} class.
     *
     * @param keyType The "keyType" of the new "DictType". Multiplicity [1..1]. May be {@code null} to set the "keyType" later.
     * @param position The "position" of the new "DictType". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param valueType The "valueType" of the new "DictType". Multiplicity [1..1]. May be {@code null} to set the "valueType" later.
     * @return A new instance of the {@link DictType} class.
     */
    public static DictType newDictType(CifType keyType, Position position, CifType valueType) {
        DictType rslt_ = newDictType();
        if (keyType != null) {
            rslt_.setKeyType(keyType);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (valueType != null) {
            rslt_.setValueType(valueType);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link DiscVariable} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link DiscVariable} class.
     */
    public static DiscVariable newDiscVariable() {
        return DeclarationsFactory.eINSTANCE.createDiscVariable();
    }

    /**
     * Returns a new instance of the {@link DiscVariable} class.
     *
     * @param annotations The "annotations" of the new "DiscVariable". Multiplicity [0..*]. May be {@code null} to skip setting the "annotations", or to set it later.
     * @param name The "name" of the new "DiscVariable". Multiplicity [1..1]. May be {@code null} to set the "name" later.
     * @param position The "position" of the new "DiscVariable". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "DiscVariable". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @param value The "value" of the new "DiscVariable". Multiplicity [0..1]. May be {@code null} to skip setting the "value", or to set it later.
     * @return A new instance of the {@link DiscVariable} class.
     */
    public static DiscVariable newDiscVariable(List<Annotation> annotations, String name, Position position, CifType type, VariableValue value) {
        DiscVariable rslt_ = newDiscVariable();
        if (annotations != null) {
            rslt_.getAnnotations().addAll(annotations);
        }
        if (name != null) {
            rslt_.setName(name);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (type != null) {
            rslt_.setType(type);
        }
        if (value != null) {
            rslt_.setValue(value);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link DiscVariableExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link DiscVariableExpression} class.
     */
    public static DiscVariableExpression newDiscVariableExpression() {
        return ExpressionsFactory.eINSTANCE.createDiscVariableExpression();
    }

    /**
     * Returns a new instance of the {@link DiscVariableExpression} class.
     *
     * @param position The "position" of the new "DiscVariableExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "DiscVariableExpression". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @param variable The "variable" of the new "DiscVariableExpression". Multiplicity [1..1]. May be {@code null} to set the "variable" later.
     * @return A new instance of the {@link DiscVariableExpression} class.
     */
    public static DiscVariableExpression newDiscVariableExpression(Position position, CifType type, DiscVariable variable) {
        DiscVariableExpression rslt_ = newDiscVariableExpression();
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (type != null) {
            rslt_.setType(type);
        }
        if (variable != null) {
            rslt_.setVariable(variable);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link DistType} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link DistType} class.
     */
    public static DistType newDistType() {
        return TypesFactory.eINSTANCE.createDistType();
    }

    /**
     * Returns a new instance of the {@link DistType} class.
     *
     * @param position The "position" of the new "DistType". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param sampleType The "sampleType" of the new "DistType". Multiplicity [1..1]. May be {@code null} to set the "sampleType" later.
     * @return A new instance of the {@link DistType} class.
     */
    public static DistType newDistType(Position position, CifType sampleType) {
        DistType rslt_ = newDistType();
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (sampleType != null) {
            rslt_.setSampleType(sampleType);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link Edge} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link Edge} class.
     */
    public static Edge newEdge() {
        return AutomataFactory.eINSTANCE.createEdge();
    }

    /**
     * Returns a new instance of the {@link Edge} class.
     *
     * @param events The "events" of the new "Edge". Multiplicity [0..*]. May be {@code null} to skip setting the "events", or to set it later.
     * @param guards The "guards" of the new "Edge". Multiplicity [0..*]. May be {@code null} to skip setting the "guards", or to set it later.
     * @param position The "position" of the new "Edge". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param target The "target" of the new "Edge". Multiplicity [0..1]. May be {@code null} to skip setting the "target", or to set it later.
     * @param updates The "updates" of the new "Edge". Multiplicity [0..*]. May be {@code null} to skip setting the "updates", or to set it later.
     * @param urgent The "urgent" of the new "Edge". Multiplicity [1..1]. May be {@code null} to set the "urgent" later.
     * @return A new instance of the {@link Edge} class.
     */
    public static Edge newEdge(List<EdgeEvent> events, List<Expression> guards, Position position, Location target, List<Update> updates, Boolean urgent) {
        Edge rslt_ = newEdge();
        if (events != null) {
            rslt_.getEvents().addAll(events);
        }
        if (guards != null) {
            rslt_.getGuards().addAll(guards);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (target != null) {
            rslt_.setTarget(target);
        }
        if (updates != null) {
            rslt_.getUpdates().addAll(updates);
        }
        if (urgent != null) {
            rslt_.setUrgent(urgent);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link EdgeEvent} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link EdgeEvent} class.
     */
    public static EdgeEvent newEdgeEvent() {
        return AutomataFactory.eINSTANCE.createEdgeEvent();
    }

    /**
     * Returns a new instance of the {@link EdgeEvent} class.
     *
     * @param event The "event" of the new "EdgeEvent". Multiplicity [1..1]. May be {@code null} to set the "event" later.
     * @param position The "position" of the new "EdgeEvent". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link EdgeEvent} class.
     */
    public static EdgeEvent newEdgeEvent(Expression event, Position position) {
        EdgeEvent rslt_ = newEdgeEvent();
        if (event != null) {
            rslt_.setEvent(event);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link EdgeReceive} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link EdgeReceive} class.
     */
    public static EdgeReceive newEdgeReceive() {
        return AutomataFactory.eINSTANCE.createEdgeReceive();
    }

    /**
     * Returns a new instance of the {@link EdgeReceive} class.
     *
     * @param event The "event" of the new "EdgeReceive". Multiplicity [1..1]. May be {@code null} to set the "event" later.
     * @param position The "position" of the new "EdgeReceive". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link EdgeReceive} class.
     */
    public static EdgeReceive newEdgeReceive(Expression event, Position position) {
        EdgeReceive rslt_ = newEdgeReceive();
        if (event != null) {
            rslt_.setEvent(event);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link EdgeSend} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link EdgeSend} class.
     */
    public static EdgeSend newEdgeSend() {
        return AutomataFactory.eINSTANCE.createEdgeSend();
    }

    /**
     * Returns a new instance of the {@link EdgeSend} class.
     *
     * @param event The "event" of the new "EdgeSend". Multiplicity [1..1]. May be {@code null} to set the "event" later.
     * @param position The "position" of the new "EdgeSend". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param value The "value" of the new "EdgeSend". Multiplicity [0..1]. May be {@code null} to skip setting the "value", or to set it later.
     * @return A new instance of the {@link EdgeSend} class.
     */
    public static EdgeSend newEdgeSend(Expression event, Position position, Expression value) {
        EdgeSend rslt_ = newEdgeSend();
        if (event != null) {
            rslt_.setEvent(event);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (value != null) {
            rslt_.setValue(value);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link ElifExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link ElifExpression} class.
     */
    public static ElifExpression newElifExpression() {
        return ExpressionsFactory.eINSTANCE.createElifExpression();
    }

    /**
     * Returns a new instance of the {@link ElifExpression} class.
     *
     * @param guards The "guards" of the new "ElifExpression". Multiplicity [1..*]. May be {@code null} to set the "guards" later.
     * @param position The "position" of the new "ElifExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param then The "then" of the new "ElifExpression". Multiplicity [1..1]. May be {@code null} to set the "then" later.
     * @return A new instance of the {@link ElifExpression} class.
     */
    public static ElifExpression newElifExpression(List<Expression> guards, Position position, Expression then) {
        ElifExpression rslt_ = newElifExpression();
        if (guards != null) {
            rslt_.getGuards().addAll(guards);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (then != null) {
            rslt_.setThen(then);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link ElifFuncStatement} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link ElifFuncStatement} class.
     */
    public static ElifFuncStatement newElifFuncStatement() {
        return FunctionsFactory.eINSTANCE.createElifFuncStatement();
    }

    /**
     * Returns a new instance of the {@link ElifFuncStatement} class.
     *
     * @param guards The "guards" of the new "ElifFuncStatement". Multiplicity [1..*]. May be {@code null} to set the "guards" later.
     * @param position The "position" of the new "ElifFuncStatement". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param thens The "thens" of the new "ElifFuncStatement". Multiplicity [1..*]. May be {@code null} to set the "thens" later.
     * @return A new instance of the {@link ElifFuncStatement} class.
     */
    public static ElifFuncStatement newElifFuncStatement(List<Expression> guards, Position position, List<FunctionStatement> thens) {
        ElifFuncStatement rslt_ = newElifFuncStatement();
        if (guards != null) {
            rslt_.getGuards().addAll(guards);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (thens != null) {
            rslt_.getThens().addAll(thens);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link ElifUpdate} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link ElifUpdate} class.
     */
    public static ElifUpdate newElifUpdate() {
        return AutomataFactory.eINSTANCE.createElifUpdate();
    }

    /**
     * Returns a new instance of the {@link ElifUpdate} class.
     *
     * @param guards The "guards" of the new "ElifUpdate". Multiplicity [1..*]. May be {@code null} to set the "guards" later.
     * @param position The "position" of the new "ElifUpdate". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param thens The "thens" of the new "ElifUpdate". Multiplicity [1..*]. May be {@code null} to set the "thens" later.
     * @return A new instance of the {@link ElifUpdate} class.
     */
    public static ElifUpdate newElifUpdate(List<Expression> guards, Position position, List<Update> thens) {
        ElifUpdate rslt_ = newElifUpdate();
        if (guards != null) {
            rslt_.getGuards().addAll(guards);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (thens != null) {
            rslt_.getThens().addAll(thens);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link EnumDecl} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link EnumDecl} class.
     */
    public static EnumDecl newEnumDecl() {
        return DeclarationsFactory.eINSTANCE.createEnumDecl();
    }

    /**
     * Returns a new instance of the {@link EnumDecl} class.
     *
     * @param annotations The "annotations" of the new "EnumDecl". Multiplicity [0..*]. May be {@code null} to skip setting the "annotations", or to set it later.
     * @param literals The "literals" of the new "EnumDecl". Multiplicity [1..*]. May be {@code null} to set the "literals" later.
     * @param name The "name" of the new "EnumDecl". Multiplicity [1..1]. May be {@code null} to set the "name" later.
     * @param position The "position" of the new "EnumDecl". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link EnumDecl} class.
     */
    public static EnumDecl newEnumDecl(List<Annotation> annotations, List<EnumLiteral> literals, String name, Position position) {
        EnumDecl rslt_ = newEnumDecl();
        if (annotations != null) {
            rslt_.getAnnotations().addAll(annotations);
        }
        if (literals != null) {
            rslt_.getLiterals().addAll(literals);
        }
        if (name != null) {
            rslt_.setName(name);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link EnumLiteral} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link EnumLiteral} class.
     */
    public static EnumLiteral newEnumLiteral() {
        return DeclarationsFactory.eINSTANCE.createEnumLiteral();
    }

    /**
     * Returns a new instance of the {@link EnumLiteral} class.
     *
     * @param name The "name" of the new "EnumLiteral". Multiplicity [1..1]. May be {@code null} to set the "name" later.
     * @param position The "position" of the new "EnumLiteral". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link EnumLiteral} class.
     */
    public static EnumLiteral newEnumLiteral(String name, Position position) {
        EnumLiteral rslt_ = newEnumLiteral();
        if (name != null) {
            rslt_.setName(name);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link EnumLiteralExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link EnumLiteralExpression} class.
     */
    public static EnumLiteralExpression newEnumLiteralExpression() {
        return ExpressionsFactory.eINSTANCE.createEnumLiteralExpression();
    }

    /**
     * Returns a new instance of the {@link EnumLiteralExpression} class.
     *
     * @param literal The "literal" of the new "EnumLiteralExpression". Multiplicity [1..1]. May be {@code null} to set the "literal" later.
     * @param position The "position" of the new "EnumLiteralExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "EnumLiteralExpression". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @return A new instance of the {@link EnumLiteralExpression} class.
     */
    public static EnumLiteralExpression newEnumLiteralExpression(EnumLiteral literal, Position position, CifType type) {
        EnumLiteralExpression rslt_ = newEnumLiteralExpression();
        if (literal != null) {
            rslt_.setLiteral(literal);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (type != null) {
            rslt_.setType(type);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link EnumType} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link EnumType} class.
     */
    public static EnumType newEnumType() {
        return TypesFactory.eINSTANCE.createEnumType();
    }

    /**
     * Returns a new instance of the {@link EnumType} class.
     *
     * @param _enum The "enum" of the new "EnumType". Multiplicity [1..1]. May be {@code null} to set the "enum" later.
     * @param position The "position" of the new "EnumType". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link EnumType} class.
     */
    public static EnumType newEnumType(EnumDecl _enum, Position position) {
        EnumType rslt_ = newEnumType();
        if (_enum != null) {
            rslt_.setEnum(_enum);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link Equation} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link Equation} class.
     */
    public static Equation newEquation() {
        return CifFactory.eINSTANCE.createEquation();
    }

    /**
     * Returns a new instance of the {@link Equation} class.
     *
     * @param derivative The "derivative" of the new "Equation". Multiplicity [1..1]. May be {@code null} to set the "derivative" later.
     * @param position The "position" of the new "Equation". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param value The "value" of the new "Equation". Multiplicity [1..1]. May be {@code null} to set the "value" later.
     * @param variable The "variable" of the new "Equation". Multiplicity [1..1]. May be {@code null} to set the "variable" later.
     * @return A new instance of the {@link Equation} class.
     */
    public static Equation newEquation(Boolean derivative, Position position, Expression value, Declaration variable) {
        Equation rslt_ = newEquation();
        if (derivative != null) {
            rslt_.setDerivative(derivative);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (value != null) {
            rslt_.setValue(value);
        }
        if (variable != null) {
            rslt_.setVariable(variable);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link Event} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link Event} class.
     */
    public static Event newEvent() {
        return DeclarationsFactory.eINSTANCE.createEvent();
    }

    /**
     * Returns a new instance of the {@link Event} class.
     *
     * @param annotations The "annotations" of the new "Event". Multiplicity [0..*]. May be {@code null} to skip setting the "annotations", or to set it later.
     * @param controllable The "controllable" of the new "Event". Multiplicity [0..1]. May be {@code null} to skip setting the "controllable", or to set it later.
     * @param name The "name" of the new "Event". Multiplicity [1..1]. May be {@code null} to set the "name" later.
     * @param position The "position" of the new "Event". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "Event". Multiplicity [0..1]. May be {@code null} to skip setting the "type", or to set it later.
     * @return A new instance of the {@link Event} class.
     */
    public static Event newEvent(List<Annotation> annotations, Boolean controllable, String name, Position position, CifType type) {
        Event rslt_ = newEvent();
        if (annotations != null) {
            rslt_.getAnnotations().addAll(annotations);
        }
        if (controllable != null) {
            rslt_.setControllable(controllable);
        }
        if (name != null) {
            rslt_.setName(name);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (type != null) {
            rslt_.setType(type);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link EventExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link EventExpression} class.
     */
    public static EventExpression newEventExpression() {
        return ExpressionsFactory.eINSTANCE.createEventExpression();
    }

    /**
     * Returns a new instance of the {@link EventExpression} class.
     *
     * @param event The "event" of the new "EventExpression". Multiplicity [1..1]. May be {@code null} to set the "event" later.
     * @param position The "position" of the new "EventExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "EventExpression". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @return A new instance of the {@link EventExpression} class.
     */
    public static EventExpression newEventExpression(Event event, Position position, CifType type) {
        EventExpression rslt_ = newEventExpression();
        if (event != null) {
            rslt_.setEvent(event);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (type != null) {
            rslt_.setType(type);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link EventParameter} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link EventParameter} class.
     */
    public static EventParameter newEventParameter() {
        return CifFactory.eINSTANCE.createEventParameter();
    }

    /**
     * Returns a new instance of the {@link EventParameter} class.
     *
     * @param event The "event" of the new "EventParameter". Multiplicity [1..1]. May be {@code null} to set the "event" later.
     * @param position The "position" of the new "EventParameter". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param recvFlag The "recvFlag" of the new "EventParameter". Multiplicity [1..1]. May be {@code null} to set the "recvFlag" later.
     * @param sendFlag The "sendFlag" of the new "EventParameter". Multiplicity [1..1]. May be {@code null} to set the "sendFlag" later.
     * @param syncFlag The "syncFlag" of the new "EventParameter". Multiplicity [1..1]. May be {@code null} to set the "syncFlag" later.
     * @return A new instance of the {@link EventParameter} class.
     */
    public static EventParameter newEventParameter(Event event, Position position, Boolean recvFlag, Boolean sendFlag, Boolean syncFlag) {
        EventParameter rslt_ = newEventParameter();
        if (event != null) {
            rslt_.setEvent(event);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (recvFlag != null) {
            rslt_.setRecvFlag(recvFlag);
        }
        if (sendFlag != null) {
            rslt_.setSendFlag(sendFlag);
        }
        if (syncFlag != null) {
            rslt_.setSyncFlag(syncFlag);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link ExternalFunction} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link ExternalFunction} class.
     */
    public static ExternalFunction newExternalFunction() {
        return FunctionsFactory.eINSTANCE.createExternalFunction();
    }

    /**
     * Returns a new instance of the {@link ExternalFunction} class.
     *
     * @param annotations The "annotations" of the new "ExternalFunction". Multiplicity [0..*]. May be {@code null} to skip setting the "annotations", or to set it later.
     * @param function The "function" of the new "ExternalFunction". Multiplicity [1..1]. May be {@code null} to set the "function" later.
     * @param name The "name" of the new "ExternalFunction". Multiplicity [1..1]. May be {@code null} to set the "name" later.
     * @param parameters The "parameters" of the new "ExternalFunction". Multiplicity [0..*]. May be {@code null} to skip setting the "parameters", or to set it later.
     * @param position The "position" of the new "ExternalFunction". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param returnTypes The "returnTypes" of the new "ExternalFunction". Multiplicity [1..*]. May be {@code null} to set the "returnTypes" later.
     * @return A new instance of the {@link ExternalFunction} class.
     */
    public static ExternalFunction newExternalFunction(List<Annotation> annotations, String function, String name, List<FunctionParameter> parameters, Position position, List<CifType> returnTypes) {
        ExternalFunction rslt_ = newExternalFunction();
        if (annotations != null) {
            rslt_.getAnnotations().addAll(annotations);
        }
        if (function != null) {
            rslt_.setFunction(function);
        }
        if (name != null) {
            rslt_.setName(name);
        }
        if (parameters != null) {
            rslt_.getParameters().addAll(parameters);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (returnTypes != null) {
            rslt_.getReturnTypes().addAll(returnTypes);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link Field} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link Field} class.
     */
    public static Field newField() {
        return TypesFactory.eINSTANCE.createField();
    }

    /**
     * Returns a new instance of the {@link Field} class.
     *
     * @param name The "name" of the new "Field". Multiplicity [0..1]. May be {@code null} to skip setting the "name", or to set it later.
     * @param position The "position" of the new "Field". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "Field". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @return A new instance of the {@link Field} class.
     */
    public static Field newField(String name, Position position, CifType type) {
        Field rslt_ = newField();
        if (name != null) {
            rslt_.setName(name);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (type != null) {
            rslt_.setType(type);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link FieldExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link FieldExpression} class.
     */
    public static FieldExpression newFieldExpression() {
        return ExpressionsFactory.eINSTANCE.createFieldExpression();
    }

    /**
     * Returns a new instance of the {@link FieldExpression} class.
     *
     * @param field The "field" of the new "FieldExpression". Multiplicity [1..1]. May be {@code null} to set the "field" later.
     * @param position The "position" of the new "FieldExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "FieldExpression". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @return A new instance of the {@link FieldExpression} class.
     */
    public static FieldExpression newFieldExpression(Field field, Position position, CifType type) {
        FieldExpression rslt_ = newFieldExpression();
        if (field != null) {
            rslt_.setField(field);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (type != null) {
            rslt_.setType(type);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link FuncType} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link FuncType} class.
     */
    public static FuncType newFuncType() {
        return TypesFactory.eINSTANCE.createFuncType();
    }

    /**
     * Returns a new instance of the {@link FuncType} class.
     *
     * @param paramTypes The "paramTypes" of the new "FuncType". Multiplicity [0..*]. May be {@code null} to skip setting the "paramTypes", or to set it later.
     * @param position The "position" of the new "FuncType". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param returnType The "returnType" of the new "FuncType". Multiplicity [1..1]. May be {@code null} to set the "returnType" later.
     * @return A new instance of the {@link FuncType} class.
     */
    public static FuncType newFuncType(List<CifType> paramTypes, Position position, CifType returnType) {
        FuncType rslt_ = newFuncType();
        if (paramTypes != null) {
            rslt_.getParamTypes().addAll(paramTypes);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (returnType != null) {
            rslt_.setReturnType(returnType);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link FunctionCallExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link FunctionCallExpression} class.
     */
    public static FunctionCallExpression newFunctionCallExpression() {
        return ExpressionsFactory.eINSTANCE.createFunctionCallExpression();
    }

    /**
     * Returns a new instance of the {@link FunctionCallExpression} class.
     *
     * @param arguments The "arguments" of the new "FunctionCallExpression". Multiplicity [0..*]. May be {@code null} to skip setting the "arguments", or to set it later.
     * @param function The "function" of the new "FunctionCallExpression". Multiplicity [1..1]. May be {@code null} to set the "function" later.
     * @param position The "position" of the new "FunctionCallExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "FunctionCallExpression". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @return A new instance of the {@link FunctionCallExpression} class.
     */
    public static FunctionCallExpression newFunctionCallExpression(List<Expression> arguments, Expression function, Position position, CifType type) {
        FunctionCallExpression rslt_ = newFunctionCallExpression();
        if (arguments != null) {
            rslt_.getArguments().addAll(arguments);
        }
        if (function != null) {
            rslt_.setFunction(function);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (type != null) {
            rslt_.setType(type);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link FunctionExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link FunctionExpression} class.
     */
    public static FunctionExpression newFunctionExpression() {
        return ExpressionsFactory.eINSTANCE.createFunctionExpression();
    }

    /**
     * Returns a new instance of the {@link FunctionExpression} class.
     *
     * @param function The "function" of the new "FunctionExpression". Multiplicity [1..1]. May be {@code null} to set the "function" later.
     * @param position The "position" of the new "FunctionExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "FunctionExpression". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @return A new instance of the {@link FunctionExpression} class.
     */
    public static FunctionExpression newFunctionExpression(Function function, Position position, CifType type) {
        FunctionExpression rslt_ = newFunctionExpression();
        if (function != null) {
            rslt_.setFunction(function);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (type != null) {
            rslt_.setType(type);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link FunctionParameter} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link FunctionParameter} class.
     */
    public static FunctionParameter newFunctionParameter() {
        return FunctionsFactory.eINSTANCE.createFunctionParameter();
    }

    /**
     * Returns a new instance of the {@link FunctionParameter} class.
     *
     * @param parameter The "parameter" of the new "FunctionParameter". Multiplicity [1..1]. May be {@code null} to set the "parameter" later.
     * @param position The "position" of the new "FunctionParameter". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link FunctionParameter} class.
     */
    public static FunctionParameter newFunctionParameter(DiscVariable parameter, Position position) {
        FunctionParameter rslt_ = newFunctionParameter();
        if (parameter != null) {
            rslt_.setParameter(parameter);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link Group} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link Group} class.
     */
    public static Group newGroup() {
        return CifFactory.eINSTANCE.createGroup();
    }

    /**
     * Returns a new instance of the {@link Group} class.
     *
     * @param components The "components" of the new "Group". Multiplicity [0..*]. May be {@code null} to skip setting the "components", or to set it later.
     * @param declarations The "declarations" of the new "Group". Multiplicity [0..*]. May be {@code null} to skip setting the "declarations", or to set it later.
     * @param definitions The "definitions" of the new "Group". Multiplicity [0..*]. May be {@code null} to skip setting the "definitions", or to set it later.
     * @param equations The "equations" of the new "Group". Multiplicity [0..*]. May be {@code null} to skip setting the "equations", or to set it later.
     * @param initials The "initials" of the new "Group". Multiplicity [0..*]. May be {@code null} to skip setting the "initials", or to set it later.
     * @param invariants The "invariants" of the new "Group". Multiplicity [0..*]. May be {@code null} to skip setting the "invariants", or to set it later.
     * @param ioDecls The "ioDecls" of the new "Group". Multiplicity [0..*]. May be {@code null} to skip setting the "ioDecls", or to set it later.
     * @param markeds The "markeds" of the new "Group". Multiplicity [0..*]. May be {@code null} to skip setting the "markeds", or to set it later.
     * @param name The "name" of the new "Group". Multiplicity [1..1]. May be {@code null} to set the "name" later.
     * @param position The "position" of the new "Group". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link Group} class.
     */
    public static Group newGroup(List<Component> components, List<Declaration> declarations, List<ComponentDef> definitions, List<Equation> equations, List<Expression> initials, List<Invariant> invariants, List<IoDecl> ioDecls, List<Expression> markeds, String name, Position position) {
        Group rslt_ = newGroup();
        if (components != null) {
            rslt_.getComponents().addAll(components);
        }
        if (declarations != null) {
            rslt_.getDeclarations().addAll(declarations);
        }
        if (definitions != null) {
            rslt_.getDefinitions().addAll(definitions);
        }
        if (equations != null) {
            rslt_.getEquations().addAll(equations);
        }
        if (initials != null) {
            rslt_.getInitials().addAll(initials);
        }
        if (invariants != null) {
            rslt_.getInvariants().addAll(invariants);
        }
        if (ioDecls != null) {
            rslt_.getIoDecls().addAll(ioDecls);
        }
        if (markeds != null) {
            rslt_.getMarkeds().addAll(markeds);
        }
        if (name != null) {
            rslt_.setName(name);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link IfExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link IfExpression} class.
     */
    public static IfExpression newIfExpression() {
        return ExpressionsFactory.eINSTANCE.createIfExpression();
    }

    /**
     * Returns a new instance of the {@link IfExpression} class.
     *
     * @param elifs The "elifs" of the new "IfExpression". Multiplicity [0..*]. May be {@code null} to skip setting the "elifs", or to set it later.
     * @param _else The "else" of the new "IfExpression". Multiplicity [1..1]. May be {@code null} to set the "else" later.
     * @param guards The "guards" of the new "IfExpression". Multiplicity [1..*]. May be {@code null} to set the "guards" later.
     * @param position The "position" of the new "IfExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param then The "then" of the new "IfExpression". Multiplicity [1..1]. May be {@code null} to set the "then" later.
     * @param type The "type" of the new "IfExpression". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @return A new instance of the {@link IfExpression} class.
     */
    public static IfExpression newIfExpression(List<ElifExpression> elifs, Expression _else, List<Expression> guards, Position position, Expression then, CifType type) {
        IfExpression rslt_ = newIfExpression();
        if (elifs != null) {
            rslt_.getElifs().addAll(elifs);
        }
        if (_else != null) {
            rslt_.setElse(_else);
        }
        if (guards != null) {
            rslt_.getGuards().addAll(guards);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (then != null) {
            rslt_.setThen(then);
        }
        if (type != null) {
            rslt_.setType(type);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link IfFuncStatement} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link IfFuncStatement} class.
     */
    public static IfFuncStatement newIfFuncStatement() {
        return FunctionsFactory.eINSTANCE.createIfFuncStatement();
    }

    /**
     * Returns a new instance of the {@link IfFuncStatement} class.
     *
     * @param elifs The "elifs" of the new "IfFuncStatement". Multiplicity [0..*]. May be {@code null} to skip setting the "elifs", or to set it later.
     * @param elses The "elses" of the new "IfFuncStatement". Multiplicity [0..*]. May be {@code null} to skip setting the "elses", or to set it later.
     * @param guards The "guards" of the new "IfFuncStatement". Multiplicity [1..*]. May be {@code null} to set the "guards" later.
     * @param position The "position" of the new "IfFuncStatement". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param thens The "thens" of the new "IfFuncStatement". Multiplicity [1..*]. May be {@code null} to set the "thens" later.
     * @return A new instance of the {@link IfFuncStatement} class.
     */
    public static IfFuncStatement newIfFuncStatement(List<ElifFuncStatement> elifs, List<FunctionStatement> elses, List<Expression> guards, Position position, List<FunctionStatement> thens) {
        IfFuncStatement rslt_ = newIfFuncStatement();
        if (elifs != null) {
            rslt_.getElifs().addAll(elifs);
        }
        if (elses != null) {
            rslt_.getElses().addAll(elses);
        }
        if (guards != null) {
            rslt_.getGuards().addAll(guards);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (thens != null) {
            rslt_.getThens().addAll(thens);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link IfUpdate} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link IfUpdate} class.
     */
    public static IfUpdate newIfUpdate() {
        return AutomataFactory.eINSTANCE.createIfUpdate();
    }

    /**
     * Returns a new instance of the {@link IfUpdate} class.
     *
     * @param elifs The "elifs" of the new "IfUpdate". Multiplicity [0..*]. May be {@code null} to skip setting the "elifs", or to set it later.
     * @param elses The "elses" of the new "IfUpdate". Multiplicity [0..*]. May be {@code null} to skip setting the "elses", or to set it later.
     * @param guards The "guards" of the new "IfUpdate". Multiplicity [1..*]. May be {@code null} to set the "guards" later.
     * @param position The "position" of the new "IfUpdate". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param thens The "thens" of the new "IfUpdate". Multiplicity [1..*]. May be {@code null} to set the "thens" later.
     * @return A new instance of the {@link IfUpdate} class.
     */
    public static IfUpdate newIfUpdate(List<ElifUpdate> elifs, List<Update> elses, List<Expression> guards, Position position, List<Update> thens) {
        IfUpdate rslt_ = newIfUpdate();
        if (elifs != null) {
            rslt_.getElifs().addAll(elifs);
        }
        if (elses != null) {
            rslt_.getElses().addAll(elses);
        }
        if (guards != null) {
            rslt_.getGuards().addAll(guards);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (thens != null) {
            rslt_.getThens().addAll(thens);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link InputVariable} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link InputVariable} class.
     */
    public static InputVariable newInputVariable() {
        return DeclarationsFactory.eINSTANCE.createInputVariable();
    }

    /**
     * Returns a new instance of the {@link InputVariable} class.
     *
     * @param annotations The "annotations" of the new "InputVariable". Multiplicity [0..*]. May be {@code null} to skip setting the "annotations", or to set it later.
     * @param name The "name" of the new "InputVariable". Multiplicity [1..1]. May be {@code null} to set the "name" later.
     * @param position The "position" of the new "InputVariable". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "InputVariable". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @return A new instance of the {@link InputVariable} class.
     */
    public static InputVariable newInputVariable(List<Annotation> annotations, String name, Position position, CifType type) {
        InputVariable rslt_ = newInputVariable();
        if (annotations != null) {
            rslt_.getAnnotations().addAll(annotations);
        }
        if (name != null) {
            rslt_.setName(name);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (type != null) {
            rslt_.setType(type);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link InputVariableExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link InputVariableExpression} class.
     */
    public static InputVariableExpression newInputVariableExpression() {
        return ExpressionsFactory.eINSTANCE.createInputVariableExpression();
    }

    /**
     * Returns a new instance of the {@link InputVariableExpression} class.
     *
     * @param position The "position" of the new "InputVariableExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "InputVariableExpression". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @param variable The "variable" of the new "InputVariableExpression". Multiplicity [1..1]. May be {@code null} to set the "variable" later.
     * @return A new instance of the {@link InputVariableExpression} class.
     */
    public static InputVariableExpression newInputVariableExpression(Position position, CifType type, InputVariable variable) {
        InputVariableExpression rslt_ = newInputVariableExpression();
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (type != null) {
            rslt_.setType(type);
        }
        if (variable != null) {
            rslt_.setVariable(variable);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link IntExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link IntExpression} class.
     */
    public static IntExpression newIntExpression() {
        return ExpressionsFactory.eINSTANCE.createIntExpression();
    }

    /**
     * Returns a new instance of the {@link IntExpression} class.
     *
     * @param position The "position" of the new "IntExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "IntExpression". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @param value The "value" of the new "IntExpression". Multiplicity [1..1]. May be {@code null} to set the "value" later.
     * @return A new instance of the {@link IntExpression} class.
     */
    public static IntExpression newIntExpression(Position position, CifType type, Integer value) {
        IntExpression rslt_ = newIntExpression();
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (type != null) {
            rslt_.setType(type);
        }
        if (value != null) {
            rslt_.setValue(value);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link IntType} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link IntType} class.
     */
    public static IntType newIntType() {
        return TypesFactory.eINSTANCE.createIntType();
    }

    /**
     * Returns a new instance of the {@link IntType} class.
     *
     * @param lower The "lower" of the new "IntType". Multiplicity [0..1]. May be {@code null} to skip setting the "lower", or to set it later.
     * @param position The "position" of the new "IntType". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param upper The "upper" of the new "IntType". Multiplicity [0..1]. May be {@code null} to skip setting the "upper", or to set it later.
     * @return A new instance of the {@link IntType} class.
     */
    public static IntType newIntType(Integer lower, Position position, Integer upper) {
        IntType rslt_ = newIntType();
        if (lower != null) {
            rslt_.setLower(lower);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (upper != null) {
            rslt_.setUpper(upper);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link InternalFunction} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link InternalFunction} class.
     */
    public static InternalFunction newInternalFunction() {
        return FunctionsFactory.eINSTANCE.createInternalFunction();
    }

    /**
     * Returns a new instance of the {@link InternalFunction} class.
     *
     * @param annotations The "annotations" of the new "InternalFunction". Multiplicity [0..*]. May be {@code null} to skip setting the "annotations", or to set it later.
     * @param name The "name" of the new "InternalFunction". Multiplicity [1..1]. May be {@code null} to set the "name" later.
     * @param parameters The "parameters" of the new "InternalFunction". Multiplicity [0..*]. May be {@code null} to skip setting the "parameters", or to set it later.
     * @param position The "position" of the new "InternalFunction". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param returnTypes The "returnTypes" of the new "InternalFunction". Multiplicity [1..*]. May be {@code null} to set the "returnTypes" later.
     * @param statements The "statements" of the new "InternalFunction". Multiplicity [1..*]. May be {@code null} to set the "statements" later.
     * @param variables The "variables" of the new "InternalFunction". Multiplicity [0..*]. May be {@code null} to skip setting the "variables", or to set it later.
     * @return A new instance of the {@link InternalFunction} class.
     */
    public static InternalFunction newInternalFunction(List<Annotation> annotations, String name, List<FunctionParameter> parameters, Position position, List<CifType> returnTypes, List<FunctionStatement> statements, List<DiscVariable> variables) {
        InternalFunction rslt_ = newInternalFunction();
        if (annotations != null) {
            rslt_.getAnnotations().addAll(annotations);
        }
        if (name != null) {
            rslt_.setName(name);
        }
        if (parameters != null) {
            rslt_.getParameters().addAll(parameters);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (returnTypes != null) {
            rslt_.getReturnTypes().addAll(returnTypes);
        }
        if (statements != null) {
            rslt_.getStatements().addAll(statements);
        }
        if (variables != null) {
            rslt_.getVariables().addAll(variables);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link Invariant} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link Invariant} class.
     */
    public static Invariant newInvariant() {
        return CifFactory.eINSTANCE.createInvariant();
    }

    /**
     * Returns a new instance of the {@link Invariant} class.
     *
     * @param event The "event" of the new "Invariant". Multiplicity [0..1]. May be {@code null} to skip setting the "event", or to set it later.
     * @param invKind The "invKind" of the new "Invariant". Multiplicity [1..1]. May be {@code null} to set the "invKind" later.
     * @param name The "name" of the new "Invariant". Multiplicity [0..1]. May be {@code null} to skip setting the "name", or to set it later.
     * @param position The "position" of the new "Invariant". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param predicate The "predicate" of the new "Invariant". Multiplicity [1..1]. May be {@code null} to set the "predicate" later.
     * @param supKind The "supKind" of the new "Invariant". Multiplicity [1..1]. May be {@code null} to set the "supKind" later.
     * @return A new instance of the {@link Invariant} class.
     */
    public static Invariant newInvariant(Expression event, InvKind invKind, String name, Position position, Expression predicate, SupKind supKind) {
        Invariant rslt_ = newInvariant();
        if (event != null) {
            rslt_.setEvent(event);
        }
        if (invKind != null) {
            rslt_.setInvKind(invKind);
        }
        if (name != null) {
            rslt_.setName(name);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (predicate != null) {
            rslt_.setPredicate(predicate);
        }
        if (supKind != null) {
            rslt_.setSupKind(supKind);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link ListExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link ListExpression} class.
     */
    public static ListExpression newListExpression() {
        return ExpressionsFactory.eINSTANCE.createListExpression();
    }

    /**
     * Returns a new instance of the {@link ListExpression} class.
     *
     * @param elements The "elements" of the new "ListExpression". Multiplicity [0..*]. May be {@code null} to skip setting the "elements", or to set it later.
     * @param position The "position" of the new "ListExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "ListExpression". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @return A new instance of the {@link ListExpression} class.
     */
    public static ListExpression newListExpression(List<Expression> elements, Position position, CifType type) {
        ListExpression rslt_ = newListExpression();
        if (elements != null) {
            rslt_.getElements().addAll(elements);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (type != null) {
            rslt_.setType(type);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link ListType} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link ListType} class.
     */
    public static ListType newListType() {
        return TypesFactory.eINSTANCE.createListType();
    }

    /**
     * Returns a new instance of the {@link ListType} class.
     *
     * @param elementType The "elementType" of the new "ListType". Multiplicity [1..1]. May be {@code null} to set the "elementType" later.
     * @param lower The "lower" of the new "ListType". Multiplicity [0..1]. May be {@code null} to skip setting the "lower", or to set it later.
     * @param position The "position" of the new "ListType". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param upper The "upper" of the new "ListType". Multiplicity [0..1]. May be {@code null} to skip setting the "upper", or to set it later.
     * @return A new instance of the {@link ListType} class.
     */
    public static ListType newListType(CifType elementType, Integer lower, Position position, Integer upper) {
        ListType rslt_ = newListType();
        if (elementType != null) {
            rslt_.setElementType(elementType);
        }
        if (lower != null) {
            rslt_.setLower(lower);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (upper != null) {
            rslt_.setUpper(upper);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link Location} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link Location} class.
     */
    public static Location newLocation() {
        return AutomataFactory.eINSTANCE.createLocation();
    }

    /**
     * Returns a new instance of the {@link Location} class.
     *
     * @param edges The "edges" of the new "Location". Multiplicity [0..*]. May be {@code null} to skip setting the "edges", or to set it later.
     * @param equations The "equations" of the new "Location". Multiplicity [0..*]. May be {@code null} to skip setting the "equations", or to set it later.
     * @param initials The "initials" of the new "Location". Multiplicity [0..*]. May be {@code null} to skip setting the "initials", or to set it later.
     * @param invariants The "invariants" of the new "Location". Multiplicity [0..*]. May be {@code null} to skip setting the "invariants", or to set it later.
     * @param markeds The "markeds" of the new "Location". Multiplicity [0..*]. May be {@code null} to skip setting the "markeds", or to set it later.
     * @param name The "name" of the new "Location". Multiplicity [0..1]. May be {@code null} to skip setting the "name", or to set it later.
     * @param position The "position" of the new "Location". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param urgent The "urgent" of the new "Location". Multiplicity [1..1]. May be {@code null} to set the "urgent" later.
     * @return A new instance of the {@link Location} class.
     */
    public static Location newLocation(List<Edge> edges, List<Equation> equations, List<Expression> initials, List<Invariant> invariants, List<Expression> markeds, String name, Position position, Boolean urgent) {
        Location rslt_ = newLocation();
        if (edges != null) {
            rslt_.getEdges().addAll(edges);
        }
        if (equations != null) {
            rslt_.getEquations().addAll(equations);
        }
        if (initials != null) {
            rslt_.getInitials().addAll(initials);
        }
        if (invariants != null) {
            rslt_.getInvariants().addAll(invariants);
        }
        if (markeds != null) {
            rslt_.getMarkeds().addAll(markeds);
        }
        if (name != null) {
            rslt_.setName(name);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (urgent != null) {
            rslt_.setUrgent(urgent);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link LocationExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link LocationExpression} class.
     */
    public static LocationExpression newLocationExpression() {
        return ExpressionsFactory.eINSTANCE.createLocationExpression();
    }

    /**
     * Returns a new instance of the {@link LocationExpression} class.
     *
     * @param location The "location" of the new "LocationExpression". Multiplicity [1..1]. May be {@code null} to set the "location" later.
     * @param position The "position" of the new "LocationExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "LocationExpression". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @return A new instance of the {@link LocationExpression} class.
     */
    public static LocationExpression newLocationExpression(Location location, Position position, CifType type) {
        LocationExpression rslt_ = newLocationExpression();
        if (location != null) {
            rslt_.setLocation(location);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (type != null) {
            rslt_.setType(type);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link LocationParameter} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link LocationParameter} class.
     */
    public static LocationParameter newLocationParameter() {
        return CifFactory.eINSTANCE.createLocationParameter();
    }

    /**
     * Returns a new instance of the {@link LocationParameter} class.
     *
     * @param location The "location" of the new "LocationParameter". Multiplicity [1..1]. May be {@code null} to set the "location" later.
     * @param position The "position" of the new "LocationParameter". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link LocationParameter} class.
     */
    public static LocationParameter newLocationParameter(Location location, Position position) {
        LocationParameter rslt_ = newLocationParameter();
        if (location != null) {
            rslt_.setLocation(location);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link Monitors} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link Monitors} class.
     */
    public static Monitors newMonitors() {
        return AutomataFactory.eINSTANCE.createMonitors();
    }

    /**
     * Returns a new instance of the {@link Monitors} class.
     *
     * @param events The "events" of the new "Monitors". Multiplicity [0..*]. May be {@code null} to skip setting the "events", or to set it later.
     * @param position The "position" of the new "Monitors". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link Monitors} class.
     */
    public static Monitors newMonitors(List<Expression> events, Position position) {
        Monitors rslt_ = newMonitors();
        if (events != null) {
            rslt_.getEvents().addAll(events);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link Print} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link Print} class.
     */
    public static Print newPrint() {
        return PrintFactory.eINSTANCE.createPrint();
    }

    /**
     * Returns a new instance of the {@link Print} class.
     *
     * @param file The "file" of the new "Print". Multiplicity [0..1]. May be {@code null} to skip setting the "file", or to set it later.
     * @param fors The "fors" of the new "Print". Multiplicity [0..*]. May be {@code null} to skip setting the "fors", or to set it later.
     * @param position The "position" of the new "Print". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param txtPost The "txtPost" of the new "Print". Multiplicity [0..1]. May be {@code null} to skip setting the "txtPost", or to set it later.
     * @param txtPre The "txtPre" of the new "Print". Multiplicity [0..1]. May be {@code null} to skip setting the "txtPre", or to set it later.
     * @param whenPost The "whenPost" of the new "Print". Multiplicity [0..1]. May be {@code null} to skip setting the "whenPost", or to set it later.
     * @param whenPre The "whenPre" of the new "Print". Multiplicity [0..1]. May be {@code null} to skip setting the "whenPre", or to set it later.
     * @return A new instance of the {@link Print} class.
     */
    public static Print newPrint(PrintFile file, List<PrintFor> fors, Position position, Expression txtPost, Expression txtPre, Expression whenPost, Expression whenPre) {
        Print rslt_ = newPrint();
        if (file != null) {
            rslt_.setFile(file);
        }
        if (fors != null) {
            rslt_.getFors().addAll(fors);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (txtPost != null) {
            rslt_.setTxtPost(txtPost);
        }
        if (txtPre != null) {
            rslt_.setTxtPre(txtPre);
        }
        if (whenPost != null) {
            rslt_.setWhenPost(whenPost);
        }
        if (whenPre != null) {
            rslt_.setWhenPre(whenPre);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link PrintFile} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link PrintFile} class.
     */
    public static PrintFile newPrintFile() {
        return PrintFactory.eINSTANCE.createPrintFile();
    }

    /**
     * Returns a new instance of the {@link PrintFile} class.
     *
     * @param path The "path" of the new "PrintFile". Multiplicity [1..1]. May be {@code null} to set the "path" later.
     * @param position The "position" of the new "PrintFile". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link PrintFile} class.
     */
    public static PrintFile newPrintFile(String path, Position position) {
        PrintFile rslt_ = newPrintFile();
        if (path != null) {
            rslt_.setPath(path);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link PrintFor} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link PrintFor} class.
     */
    public static PrintFor newPrintFor() {
        return PrintFactory.eINSTANCE.createPrintFor();
    }

    /**
     * Returns a new instance of the {@link PrintFor} class.
     *
     * @param event The "event" of the new "PrintFor". Multiplicity [0..1]. May be {@code null} to skip setting the "event", or to set it later.
     * @param kind The "kind" of the new "PrintFor". Multiplicity [1..1]. May be {@code null} to set the "kind" later.
     * @param position The "position" of the new "PrintFor". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link PrintFor} class.
     */
    public static PrintFor newPrintFor(Expression event, PrintForKind kind, Position position) {
        PrintFor rslt_ = newPrintFor();
        if (event != null) {
            rslt_.setEvent(event);
        }
        if (kind != null) {
            rslt_.setKind(kind);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link ProjectionExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link ProjectionExpression} class.
     */
    public static ProjectionExpression newProjectionExpression() {
        return ExpressionsFactory.eINSTANCE.createProjectionExpression();
    }

    /**
     * Returns a new instance of the {@link ProjectionExpression} class.
     *
     * @param child The "child" of the new "ProjectionExpression". Multiplicity [1..1]. May be {@code null} to set the "child" later.
     * @param index The "index" of the new "ProjectionExpression". Multiplicity [1..1]. May be {@code null} to set the "index" later.
     * @param position The "position" of the new "ProjectionExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "ProjectionExpression". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @return A new instance of the {@link ProjectionExpression} class.
     */
    public static ProjectionExpression newProjectionExpression(Expression child, Expression index, Position position, CifType type) {
        ProjectionExpression rslt_ = newProjectionExpression();
        if (child != null) {
            rslt_.setChild(child);
        }
        if (index != null) {
            rslt_.setIndex(index);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (type != null) {
            rslt_.setType(type);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link RealExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link RealExpression} class.
     */
    public static RealExpression newRealExpression() {
        return ExpressionsFactory.eINSTANCE.createRealExpression();
    }

    /**
     * Returns a new instance of the {@link RealExpression} class.
     *
     * @param position The "position" of the new "RealExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "RealExpression". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @param value The "value" of the new "RealExpression". Multiplicity [1..1]. May be {@code null} to set the "value" later.
     * @return A new instance of the {@link RealExpression} class.
     */
    public static RealExpression newRealExpression(Position position, CifType type, String value) {
        RealExpression rslt_ = newRealExpression();
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (type != null) {
            rslt_.setType(type);
        }
        if (value != null) {
            rslt_.setValue(value);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link RealType} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link RealType} class.
     */
    public static RealType newRealType() {
        return TypesFactory.eINSTANCE.createRealType();
    }

    /**
     * Returns a new instance of the {@link RealType} class.
     *
     * @param position The "position" of the new "RealType". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link RealType} class.
     */
    public static RealType newRealType(Position position) {
        RealType rslt_ = newRealType();
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link ReceivedExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link ReceivedExpression} class.
     */
    public static ReceivedExpression newReceivedExpression() {
        return ExpressionsFactory.eINSTANCE.createReceivedExpression();
    }

    /**
     * Returns a new instance of the {@link ReceivedExpression} class.
     *
     * @param position The "position" of the new "ReceivedExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "ReceivedExpression". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @return A new instance of the {@link ReceivedExpression} class.
     */
    public static ReceivedExpression newReceivedExpression(Position position, CifType type) {
        ReceivedExpression rslt_ = newReceivedExpression();
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (type != null) {
            rslt_.setType(type);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link ReturnFuncStatement} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link ReturnFuncStatement} class.
     */
    public static ReturnFuncStatement newReturnFuncStatement() {
        return FunctionsFactory.eINSTANCE.createReturnFuncStatement();
    }

    /**
     * Returns a new instance of the {@link ReturnFuncStatement} class.
     *
     * @param position The "position" of the new "ReturnFuncStatement". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param values The "values" of the new "ReturnFuncStatement". Multiplicity [1..*]. May be {@code null} to set the "values" later.
     * @return A new instance of the {@link ReturnFuncStatement} class.
     */
    public static ReturnFuncStatement newReturnFuncStatement(Position position, List<Expression> values) {
        ReturnFuncStatement rslt_ = newReturnFuncStatement();
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (values != null) {
            rslt_.getValues().addAll(values);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link SelfExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link SelfExpression} class.
     */
    public static SelfExpression newSelfExpression() {
        return ExpressionsFactory.eINSTANCE.createSelfExpression();
    }

    /**
     * Returns a new instance of the {@link SelfExpression} class.
     *
     * @param position The "position" of the new "SelfExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "SelfExpression". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @return A new instance of the {@link SelfExpression} class.
     */
    public static SelfExpression newSelfExpression(Position position, CifType type) {
        SelfExpression rslt_ = newSelfExpression();
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (type != null) {
            rslt_.setType(type);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link SetExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link SetExpression} class.
     */
    public static SetExpression newSetExpression() {
        return ExpressionsFactory.eINSTANCE.createSetExpression();
    }

    /**
     * Returns a new instance of the {@link SetExpression} class.
     *
     * @param elements The "elements" of the new "SetExpression". Multiplicity [0..*]. May be {@code null} to skip setting the "elements", or to set it later.
     * @param position The "position" of the new "SetExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "SetExpression". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @return A new instance of the {@link SetExpression} class.
     */
    public static SetExpression newSetExpression(List<Expression> elements, Position position, CifType type) {
        SetExpression rslt_ = newSetExpression();
        if (elements != null) {
            rslt_.getElements().addAll(elements);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (type != null) {
            rslt_.setType(type);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link SetType} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link SetType} class.
     */
    public static SetType newSetType() {
        return TypesFactory.eINSTANCE.createSetType();
    }

    /**
     * Returns a new instance of the {@link SetType} class.
     *
     * @param elementType The "elementType" of the new "SetType". Multiplicity [1..1]. May be {@code null} to set the "elementType" later.
     * @param position The "position" of the new "SetType". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link SetType} class.
     */
    public static SetType newSetType(CifType elementType, Position position) {
        SetType rslt_ = newSetType();
        if (elementType != null) {
            rslt_.setElementType(elementType);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link SliceExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link SliceExpression} class.
     */
    public static SliceExpression newSliceExpression() {
        return ExpressionsFactory.eINSTANCE.createSliceExpression();
    }

    /**
     * Returns a new instance of the {@link SliceExpression} class.
     *
     * @param begin The "begin" of the new "SliceExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "begin", or to set it later.
     * @param child The "child" of the new "SliceExpression". Multiplicity [1..1]. May be {@code null} to set the "child" later.
     * @param end The "end" of the new "SliceExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "end", or to set it later.
     * @param position The "position" of the new "SliceExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "SliceExpression". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @return A new instance of the {@link SliceExpression} class.
     */
    public static SliceExpression newSliceExpression(Expression begin, Expression child, Expression end, Position position, CifType type) {
        SliceExpression rslt_ = newSliceExpression();
        if (begin != null) {
            rslt_.setBegin(begin);
        }
        if (child != null) {
            rslt_.setChild(child);
        }
        if (end != null) {
            rslt_.setEnd(end);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (type != null) {
            rslt_.setType(type);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link Specification} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link Specification} class.
     */
    public static Specification newSpecification() {
        return CifFactory.eINSTANCE.createSpecification();
    }

    /**
     * Returns a new instance of the {@link Specification} class.
     *
     * @param components The "components" of the new "Specification". Multiplicity [0..*]. May be {@code null} to skip setting the "components", or to set it later.
     * @param declarations The "declarations" of the new "Specification". Multiplicity [0..*]. May be {@code null} to skip setting the "declarations", or to set it later.
     * @param definitions The "definitions" of the new "Specification". Multiplicity [0..*]. May be {@code null} to skip setting the "definitions", or to set it later.
     * @param equations The "equations" of the new "Specification". Multiplicity [0..*]. May be {@code null} to skip setting the "equations", or to set it later.
     * @param initials The "initials" of the new "Specification". Multiplicity [0..*]. May be {@code null} to skip setting the "initials", or to set it later.
     * @param invariants The "invariants" of the new "Specification". Multiplicity [0..*]. May be {@code null} to skip setting the "invariants", or to set it later.
     * @param ioDecls The "ioDecls" of the new "Specification". Multiplicity [0..*]. May be {@code null} to skip setting the "ioDecls", or to set it later.
     * @param markeds The "markeds" of the new "Specification". Multiplicity [0..*]. May be {@code null} to skip setting the "markeds", or to set it later.
     * @param name The "name" of the new "Specification". Multiplicity [1..1]. May be {@code null} to set the "name" later.
     * @param position The "position" of the new "Specification". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link Specification} class.
     */
    public static Specification newSpecification(List<Component> components, List<Declaration> declarations, List<ComponentDef> definitions, List<Equation> equations, List<Expression> initials, List<Invariant> invariants, List<IoDecl> ioDecls, List<Expression> markeds, String name, Position position) {
        Specification rslt_ = newSpecification();
        if (components != null) {
            rslt_.getComponents().addAll(components);
        }
        if (declarations != null) {
            rslt_.getDeclarations().addAll(declarations);
        }
        if (definitions != null) {
            rslt_.getDefinitions().addAll(definitions);
        }
        if (equations != null) {
            rslt_.getEquations().addAll(equations);
        }
        if (initials != null) {
            rslt_.getInitials().addAll(initials);
        }
        if (invariants != null) {
            rslt_.getInvariants().addAll(invariants);
        }
        if (ioDecls != null) {
            rslt_.getIoDecls().addAll(ioDecls);
        }
        if (markeds != null) {
            rslt_.getMarkeds().addAll(markeds);
        }
        if (name != null) {
            rslt_.setName(name);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link StdLibFunctionExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link StdLibFunctionExpression} class.
     */
    public static StdLibFunctionExpression newStdLibFunctionExpression() {
        return ExpressionsFactory.eINSTANCE.createStdLibFunctionExpression();
    }

    /**
     * Returns a new instance of the {@link StdLibFunctionExpression} class.
     *
     * @param function The "function" of the new "StdLibFunctionExpression". Multiplicity [1..1]. May be {@code null} to set the "function" later.
     * @param position The "position" of the new "StdLibFunctionExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "StdLibFunctionExpression". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @return A new instance of the {@link StdLibFunctionExpression} class.
     */
    public static StdLibFunctionExpression newStdLibFunctionExpression(StdLibFunction function, Position position, CifType type) {
        StdLibFunctionExpression rslt_ = newStdLibFunctionExpression();
        if (function != null) {
            rslt_.setFunction(function);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (type != null) {
            rslt_.setType(type);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link StringExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link StringExpression} class.
     */
    public static StringExpression newStringExpression() {
        return ExpressionsFactory.eINSTANCE.createStringExpression();
    }

    /**
     * Returns a new instance of the {@link StringExpression} class.
     *
     * @param position The "position" of the new "StringExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "StringExpression". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @param value The "value" of the new "StringExpression". Multiplicity [1..1]. May be {@code null} to set the "value" later.
     * @return A new instance of the {@link StringExpression} class.
     */
    public static StringExpression newStringExpression(Position position, CifType type, String value) {
        StringExpression rslt_ = newStringExpression();
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (type != null) {
            rslt_.setType(type);
        }
        if (value != null) {
            rslt_.setValue(value);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link StringType} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link StringType} class.
     */
    public static StringType newStringType() {
        return TypesFactory.eINSTANCE.createStringType();
    }

    /**
     * Returns a new instance of the {@link StringType} class.
     *
     * @param position The "position" of the new "StringType". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link StringType} class.
     */
    public static StringType newStringType(Position position) {
        StringType rslt_ = newStringType();
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link SvgCopy} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link SvgCopy} class.
     */
    public static SvgCopy newSvgCopy() {
        return CifsvgFactory.eINSTANCE.createSvgCopy();
    }

    /**
     * Returns a new instance of the {@link SvgCopy} class.
     *
     * @param id The "id" of the new "SvgCopy". Multiplicity [1..1]. May be {@code null} to set the "id" later.
     * @param position The "position" of the new "SvgCopy". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param post The "post" of the new "SvgCopy". Multiplicity [0..1]. May be {@code null} to skip setting the "post", or to set it later.
     * @param pre The "pre" of the new "SvgCopy". Multiplicity [0..1]. May be {@code null} to skip setting the "pre", or to set it later.
     * @param svgFile The "svgFile" of the new "SvgCopy". Multiplicity [0..1]. May be {@code null} to skip setting the "svgFile", or to set it later.
     * @return A new instance of the {@link SvgCopy} class.
     */
    public static SvgCopy newSvgCopy(Expression id, Position position, Expression post, Expression pre, SvgFile svgFile) {
        SvgCopy rslt_ = newSvgCopy();
        if (id != null) {
            rslt_.setId(id);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (post != null) {
            rslt_.setPost(post);
        }
        if (pre != null) {
            rslt_.setPre(pre);
        }
        if (svgFile != null) {
            rslt_.setSvgFile(svgFile);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link SvgFile} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link SvgFile} class.
     */
    public static SvgFile newSvgFile() {
        return CifsvgFactory.eINSTANCE.createSvgFile();
    }

    /**
     * Returns a new instance of the {@link SvgFile} class.
     *
     * @param path The "path" of the new "SvgFile". Multiplicity [1..1]. May be {@code null} to set the "path" later.
     * @param position The "position" of the new "SvgFile". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link SvgFile} class.
     */
    public static SvgFile newSvgFile(String path, Position position) {
        SvgFile rslt_ = newSvgFile();
        if (path != null) {
            rslt_.setPath(path);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link SvgIn} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link SvgIn} class.
     */
    public static SvgIn newSvgIn() {
        return CifsvgFactory.eINSTANCE.createSvgIn();
    }

    /**
     * Returns a new instance of the {@link SvgIn} class.
     *
     * @param event The "event" of the new "SvgIn". Multiplicity [1..1]. May be {@code null} to set the "event" later.
     * @param id The "id" of the new "SvgIn". Multiplicity [1..1]. May be {@code null} to set the "id" later.
     * @param position The "position" of the new "SvgIn". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param svgFile The "svgFile" of the new "SvgIn". Multiplicity [0..1]. May be {@code null} to skip setting the "svgFile", or to set it later.
     * @return A new instance of the {@link SvgIn} class.
     */
    public static SvgIn newSvgIn(SvgInEvent event, Expression id, Position position, SvgFile svgFile) {
        SvgIn rslt_ = newSvgIn();
        if (event != null) {
            rslt_.setEvent(event);
        }
        if (id != null) {
            rslt_.setId(id);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (svgFile != null) {
            rslt_.setSvgFile(svgFile);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link SvgInEventIf} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link SvgInEventIf} class.
     */
    public static SvgInEventIf newSvgInEventIf() {
        return CifsvgFactory.eINSTANCE.createSvgInEventIf();
    }

    /**
     * Returns a new instance of the {@link SvgInEventIf} class.
     *
     * @param entries The "entries" of the new "SvgInEventIf". Multiplicity [2..*]. May be {@code null} to set the "entries" later.
     * @param position The "position" of the new "SvgInEventIf". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link SvgInEventIf} class.
     */
    public static SvgInEventIf newSvgInEventIf(List<SvgInEventIfEntry> entries, Position position) {
        SvgInEventIf rslt_ = newSvgInEventIf();
        if (entries != null) {
            rslt_.getEntries().addAll(entries);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link SvgInEventIfEntry} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link SvgInEventIfEntry} class.
     */
    public static SvgInEventIfEntry newSvgInEventIfEntry() {
        return CifsvgFactory.eINSTANCE.createSvgInEventIfEntry();
    }

    /**
     * Returns a new instance of the {@link SvgInEventIfEntry} class.
     *
     * @param event The "event" of the new "SvgInEventIfEntry". Multiplicity [1..1]. May be {@code null} to set the "event" later.
     * @param guard The "guard" of the new "SvgInEventIfEntry". Multiplicity [0..1]. May be {@code null} to skip setting the "guard", or to set it later.
     * @param position The "position" of the new "SvgInEventIfEntry". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link SvgInEventIfEntry} class.
     */
    public static SvgInEventIfEntry newSvgInEventIfEntry(Expression event, Expression guard, Position position) {
        SvgInEventIfEntry rslt_ = newSvgInEventIfEntry();
        if (event != null) {
            rslt_.setEvent(event);
        }
        if (guard != null) {
            rslt_.setGuard(guard);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link SvgInEventSingle} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link SvgInEventSingle} class.
     */
    public static SvgInEventSingle newSvgInEventSingle() {
        return CifsvgFactory.eINSTANCE.createSvgInEventSingle();
    }

    /**
     * Returns a new instance of the {@link SvgInEventSingle} class.
     *
     * @param event The "event" of the new "SvgInEventSingle". Multiplicity [1..1]. May be {@code null} to set the "event" later.
     * @param position The "position" of the new "SvgInEventSingle". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link SvgInEventSingle} class.
     */
    public static SvgInEventSingle newSvgInEventSingle(Expression event, Position position) {
        SvgInEventSingle rslt_ = newSvgInEventSingle();
        if (event != null) {
            rslt_.setEvent(event);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link SvgMove} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link SvgMove} class.
     */
    public static SvgMove newSvgMove() {
        return CifsvgFactory.eINSTANCE.createSvgMove();
    }

    /**
     * Returns a new instance of the {@link SvgMove} class.
     *
     * @param id The "id" of the new "SvgMove". Multiplicity [1..1]. May be {@code null} to set the "id" later.
     * @param position The "position" of the new "SvgMove". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param svgFile The "svgFile" of the new "SvgMove". Multiplicity [0..1]. May be {@code null} to skip setting the "svgFile", or to set it later.
     * @param x The "x" of the new "SvgMove". Multiplicity [1..1]. May be {@code null} to set the "x" later.
     * @param y The "y" of the new "SvgMove". Multiplicity [1..1]. May be {@code null} to set the "y" later.
     * @return A new instance of the {@link SvgMove} class.
     */
    public static SvgMove newSvgMove(Expression id, Position position, SvgFile svgFile, Expression x, Expression y) {
        SvgMove rslt_ = newSvgMove();
        if (id != null) {
            rslt_.setId(id);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (svgFile != null) {
            rslt_.setSvgFile(svgFile);
        }
        if (x != null) {
            rslt_.setX(x);
        }
        if (y != null) {
            rslt_.setY(y);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link SvgOut} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link SvgOut} class.
     */
    public static SvgOut newSvgOut() {
        return CifsvgFactory.eINSTANCE.createSvgOut();
    }

    /**
     * Returns a new instance of the {@link SvgOut} class.
     *
     * @param attr The "attr" of the new "SvgOut". Multiplicity [0..1]. May be {@code null} to skip setting the "attr", or to set it later.
     * @param attrTextPos The "attrTextPos" of the new "SvgOut". Multiplicity [1..1]. May be {@code null} to set the "attrTextPos" later.
     * @param id The "id" of the new "SvgOut". Multiplicity [1..1]. May be {@code null} to set the "id" later.
     * @param position The "position" of the new "SvgOut". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param svgFile The "svgFile" of the new "SvgOut". Multiplicity [0..1]. May be {@code null} to skip setting the "svgFile", or to set it later.
     * @param value The "value" of the new "SvgOut". Multiplicity [1..1]. May be {@code null} to set the "value" later.
     * @return A new instance of the {@link SvgOut} class.
     */
    public static SvgOut newSvgOut(String attr, Position attrTextPos, Expression id, Position position, SvgFile svgFile, Expression value) {
        SvgOut rslt_ = newSvgOut();
        if (attr != null) {
            rslt_.setAttr(attr);
        }
        if (attrTextPos != null) {
            rslt_.setAttrTextPos(attrTextPos);
        }
        if (id != null) {
            rslt_.setId(id);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (svgFile != null) {
            rslt_.setSvgFile(svgFile);
        }
        if (value != null) {
            rslt_.setValue(value);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link SwitchCase} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link SwitchCase} class.
     */
    public static SwitchCase newSwitchCase() {
        return ExpressionsFactory.eINSTANCE.createSwitchCase();
    }

    /**
     * Returns a new instance of the {@link SwitchCase} class.
     *
     * @param key The "key" of the new "SwitchCase". Multiplicity [0..1]. May be {@code null} to skip setting the "key", or to set it later.
     * @param position The "position" of the new "SwitchCase". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param value The "value" of the new "SwitchCase". Multiplicity [1..1]. May be {@code null} to set the "value" later.
     * @return A new instance of the {@link SwitchCase} class.
     */
    public static SwitchCase newSwitchCase(Expression key, Position position, Expression value) {
        SwitchCase rslt_ = newSwitchCase();
        if (key != null) {
            rslt_.setKey(key);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (value != null) {
            rslt_.setValue(value);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link SwitchExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link SwitchExpression} class.
     */
    public static SwitchExpression newSwitchExpression() {
        return ExpressionsFactory.eINSTANCE.createSwitchExpression();
    }

    /**
     * Returns a new instance of the {@link SwitchExpression} class.
     *
     * @param cases The "cases" of the new "SwitchExpression". Multiplicity [1..*]. May be {@code null} to set the "cases" later.
     * @param position The "position" of the new "SwitchExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "SwitchExpression". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @param value The "value" of the new "SwitchExpression". Multiplicity [1..1]. May be {@code null} to set the "value" later.
     * @return A new instance of the {@link SwitchExpression} class.
     */
    public static SwitchExpression newSwitchExpression(List<SwitchCase> cases, Position position, CifType type, Expression value) {
        SwitchExpression rslt_ = newSwitchExpression();
        if (cases != null) {
            rslt_.getCases().addAll(cases);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (type != null) {
            rslt_.setType(type);
        }
        if (value != null) {
            rslt_.setValue(value);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link TauExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link TauExpression} class.
     */
    public static TauExpression newTauExpression() {
        return ExpressionsFactory.eINSTANCE.createTauExpression();
    }

    /**
     * Returns a new instance of the {@link TauExpression} class.
     *
     * @param position The "position" of the new "TauExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "TauExpression". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @return A new instance of the {@link TauExpression} class.
     */
    public static TauExpression newTauExpression(Position position, CifType type) {
        TauExpression rslt_ = newTauExpression();
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (type != null) {
            rslt_.setType(type);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link TimeExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link TimeExpression} class.
     */
    public static TimeExpression newTimeExpression() {
        return ExpressionsFactory.eINSTANCE.createTimeExpression();
    }

    /**
     * Returns a new instance of the {@link TimeExpression} class.
     *
     * @param position The "position" of the new "TimeExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "TimeExpression". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @return A new instance of the {@link TimeExpression} class.
     */
    public static TimeExpression newTimeExpression(Position position, CifType type) {
        TimeExpression rslt_ = newTimeExpression();
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (type != null) {
            rslt_.setType(type);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link TupleExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link TupleExpression} class.
     */
    public static TupleExpression newTupleExpression() {
        return ExpressionsFactory.eINSTANCE.createTupleExpression();
    }

    /**
     * Returns a new instance of the {@link TupleExpression} class.
     *
     * @param fields The "fields" of the new "TupleExpression". Multiplicity [2..*]. May be {@code null} to set the "fields" later.
     * @param position The "position" of the new "TupleExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "TupleExpression". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @return A new instance of the {@link TupleExpression} class.
     */
    public static TupleExpression newTupleExpression(List<Expression> fields, Position position, CifType type) {
        TupleExpression rslt_ = newTupleExpression();
        if (fields != null) {
            rslt_.getFields().addAll(fields);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (type != null) {
            rslt_.setType(type);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link TupleType} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link TupleType} class.
     */
    public static TupleType newTupleType() {
        return TypesFactory.eINSTANCE.createTupleType();
    }

    /**
     * Returns a new instance of the {@link TupleType} class.
     *
     * @param fields The "fields" of the new "TupleType". Multiplicity [2..*]. May be {@code null} to set the "fields" later.
     * @param position The "position" of the new "TupleType". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link TupleType} class.
     */
    public static TupleType newTupleType(List<Field> fields, Position position) {
        TupleType rslt_ = newTupleType();
        if (fields != null) {
            rslt_.getFields().addAll(fields);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link TypeDecl} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link TypeDecl} class.
     */
    public static TypeDecl newTypeDecl() {
        return DeclarationsFactory.eINSTANCE.createTypeDecl();
    }

    /**
     * Returns a new instance of the {@link TypeDecl} class.
     *
     * @param annotations The "annotations" of the new "TypeDecl". Multiplicity [0..*]. May be {@code null} to skip setting the "annotations", or to set it later.
     * @param name The "name" of the new "TypeDecl". Multiplicity [1..1]. May be {@code null} to set the "name" later.
     * @param position The "position" of the new "TypeDecl". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "TypeDecl". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @return A new instance of the {@link TypeDecl} class.
     */
    public static TypeDecl newTypeDecl(List<Annotation> annotations, String name, Position position, CifType type) {
        TypeDecl rslt_ = newTypeDecl();
        if (annotations != null) {
            rslt_.getAnnotations().addAll(annotations);
        }
        if (name != null) {
            rslt_.setName(name);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (type != null) {
            rslt_.setType(type);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link TypeRef} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link TypeRef} class.
     */
    public static TypeRef newTypeRef() {
        return TypesFactory.eINSTANCE.createTypeRef();
    }

    /**
     * Returns a new instance of the {@link TypeRef} class.
     *
     * @param position The "position" of the new "TypeRef". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "TypeRef". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @return A new instance of the {@link TypeRef} class.
     */
    public static TypeRef newTypeRef(Position position, TypeDecl type) {
        TypeRef rslt_ = newTypeRef();
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (type != null) {
            rslt_.setType(type);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link UnaryExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link UnaryExpression} class.
     */
    public static UnaryExpression newUnaryExpression() {
        return ExpressionsFactory.eINSTANCE.createUnaryExpression();
    }

    /**
     * Returns a new instance of the {@link UnaryExpression} class.
     *
     * @param child The "child" of the new "UnaryExpression". Multiplicity [1..1]. May be {@code null} to set the "child" later.
     * @param operator The "operator" of the new "UnaryExpression". Multiplicity [1..1]. May be {@code null} to set the "operator" later.
     * @param position The "position" of the new "UnaryExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "UnaryExpression". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @return A new instance of the {@link UnaryExpression} class.
     */
    public static UnaryExpression newUnaryExpression(Expression child, UnaryOperator operator, Position position, CifType type) {
        UnaryExpression rslt_ = newUnaryExpression();
        if (child != null) {
            rslt_.setChild(child);
        }
        if (operator != null) {
            rslt_.setOperator(operator);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (type != null) {
            rslt_.setType(type);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link VariableValue} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link VariableValue} class.
     */
    public static VariableValue newVariableValue() {
        return DeclarationsFactory.eINSTANCE.createVariableValue();
    }

    /**
     * Returns a new instance of the {@link VariableValue} class.
     *
     * @param position The "position" of the new "VariableValue". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param values The "values" of the new "VariableValue". Multiplicity [0..*]. May be {@code null} to skip setting the "values", or to set it later.
     * @return A new instance of the {@link VariableValue} class.
     */
    public static VariableValue newVariableValue(Position position, List<Expression> values) {
        VariableValue rslt_ = newVariableValue();
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (values != null) {
            rslt_.getValues().addAll(values);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link VoidType} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link VoidType} class.
     */
    public static VoidType newVoidType() {
        return TypesFactory.eINSTANCE.createVoidType();
    }

    /**
     * Returns a new instance of the {@link VoidType} class.
     *
     * @param position The "position" of the new "VoidType". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link VoidType} class.
     */
    public static VoidType newVoidType(Position position) {
        VoidType rslt_ = newVoidType();
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link WhileFuncStatement} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link WhileFuncStatement} class.
     */
    public static WhileFuncStatement newWhileFuncStatement() {
        return FunctionsFactory.eINSTANCE.createWhileFuncStatement();
    }

    /**
     * Returns a new instance of the {@link WhileFuncStatement} class.
     *
     * @param guards The "guards" of the new "WhileFuncStatement". Multiplicity [1..*]. May be {@code null} to set the "guards" later.
     * @param position The "position" of the new "WhileFuncStatement". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param statements The "statements" of the new "WhileFuncStatement". Multiplicity [1..*]. May be {@code null} to set the "statements" later.
     * @return A new instance of the {@link WhileFuncStatement} class.
     */
    public static WhileFuncStatement newWhileFuncStatement(List<Expression> guards, Position position, List<FunctionStatement> statements) {
        WhileFuncStatement rslt_ = newWhileFuncStatement();
        if (guards != null) {
            rslt_.getGuards().addAll(guards);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (statements != null) {
            rslt_.getStatements().addAll(statements);
        }
        return rslt_;
    }
}
