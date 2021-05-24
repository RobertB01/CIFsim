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

// Generated using the "org.eclipse.escet.common.emf.ecore.codegen" project.

// Disable Eclipse Java formatter for generated code file:
// @formatter:off

package org.eclipse.escet.chi.metamodel.java;

import java.util.List;

import org.eclipse.escet.chi.metamodel.chi.AssignmentStatement;
import org.eclipse.escet.chi.metamodel.chi.BinaryExpression;
import org.eclipse.escet.chi.metamodel.chi.BinaryOperators;
import org.eclipse.escet.chi.metamodel.chi.BoolLiteral;
import org.eclipse.escet.chi.metamodel.chi.BoolType;
import org.eclipse.escet.chi.metamodel.chi.BreakStatement;
import org.eclipse.escet.chi.metamodel.chi.CallExpression;
import org.eclipse.escet.chi.metamodel.chi.CastExpression;
import org.eclipse.escet.chi.metamodel.chi.ChannelExpression;
import org.eclipse.escet.chi.metamodel.chi.ChannelOps;
import org.eclipse.escet.chi.metamodel.chi.ChannelType;
import org.eclipse.escet.chi.metamodel.chi.ChiFactory;
import org.eclipse.escet.chi.metamodel.chi.CloseStatement;
import org.eclipse.escet.chi.metamodel.chi.ConstantDeclaration;
import org.eclipse.escet.chi.metamodel.chi.ConstantReference;
import org.eclipse.escet.chi.metamodel.chi.ContinueStatement;
import org.eclipse.escet.chi.metamodel.chi.CreateCase;
import org.eclipse.escet.chi.metamodel.chi.Declaration;
import org.eclipse.escet.chi.metamodel.chi.DelayStatement;
import org.eclipse.escet.chi.metamodel.chi.DictType;
import org.eclipse.escet.chi.metamodel.chi.DictionaryExpression;
import org.eclipse.escet.chi.metamodel.chi.DictionaryPair;
import org.eclipse.escet.chi.metamodel.chi.DistributionType;
import org.eclipse.escet.chi.metamodel.chi.EnumDeclaration;
import org.eclipse.escet.chi.metamodel.chi.EnumTypeReference;
import org.eclipse.escet.chi.metamodel.chi.EnumValue;
import org.eclipse.escet.chi.metamodel.chi.EnumValueReference;
import org.eclipse.escet.chi.metamodel.chi.ExitStatement;
import org.eclipse.escet.chi.metamodel.chi.Expression;
import org.eclipse.escet.chi.metamodel.chi.FieldReference;
import org.eclipse.escet.chi.metamodel.chi.FileType;
import org.eclipse.escet.chi.metamodel.chi.FinishStatement;
import org.eclipse.escet.chi.metamodel.chi.ForStatement;
import org.eclipse.escet.chi.metamodel.chi.FunctionDeclaration;
import org.eclipse.escet.chi.metamodel.chi.FunctionReference;
import org.eclipse.escet.chi.metamodel.chi.FunctionType;
import org.eclipse.escet.chi.metamodel.chi.IfCase;
import org.eclipse.escet.chi.metamodel.chi.IfStatement;
import org.eclipse.escet.chi.metamodel.chi.InstanceType;
import org.eclipse.escet.chi.metamodel.chi.IntNumber;
import org.eclipse.escet.chi.metamodel.chi.IntType;
import org.eclipse.escet.chi.metamodel.chi.IteratedCreateCase;
import org.eclipse.escet.chi.metamodel.chi.IteratedSelectCase;
import org.eclipse.escet.chi.metamodel.chi.ListExpression;
import org.eclipse.escet.chi.metamodel.chi.ListType;
import org.eclipse.escet.chi.metamodel.chi.MatrixExpression;
import org.eclipse.escet.chi.metamodel.chi.MatrixRow;
import org.eclipse.escet.chi.metamodel.chi.MatrixType;
import org.eclipse.escet.chi.metamodel.chi.ModelDeclaration;
import org.eclipse.escet.chi.metamodel.chi.ModelReference;
import org.eclipse.escet.chi.metamodel.chi.ModelType;
import org.eclipse.escet.chi.metamodel.chi.PassStatement;
import org.eclipse.escet.chi.metamodel.chi.ProcessDeclaration;
import org.eclipse.escet.chi.metamodel.chi.ProcessInstance;
import org.eclipse.escet.chi.metamodel.chi.ProcessReference;
import org.eclipse.escet.chi.metamodel.chi.ProcessType;
import org.eclipse.escet.chi.metamodel.chi.ReadCallExpression;
import org.eclipse.escet.chi.metamodel.chi.RealNumber;
import org.eclipse.escet.chi.metamodel.chi.RealType;
import org.eclipse.escet.chi.metamodel.chi.ReceiveStatement;
import org.eclipse.escet.chi.metamodel.chi.ReturnStatement;
import org.eclipse.escet.chi.metamodel.chi.RunStatement;
import org.eclipse.escet.chi.metamodel.chi.SelectCase;
import org.eclipse.escet.chi.metamodel.chi.SelectStatement;
import org.eclipse.escet.chi.metamodel.chi.SendStatement;
import org.eclipse.escet.chi.metamodel.chi.SetExpression;
import org.eclipse.escet.chi.metamodel.chi.SetType;
import org.eclipse.escet.chi.metamodel.chi.SliceExpression;
import org.eclipse.escet.chi.metamodel.chi.Specification;
import org.eclipse.escet.chi.metamodel.chi.Statement;
import org.eclipse.escet.chi.metamodel.chi.StdLibFunctionReference;
import org.eclipse.escet.chi.metamodel.chi.StdLibFunctions;
import org.eclipse.escet.chi.metamodel.chi.StringLiteral;
import org.eclipse.escet.chi.metamodel.chi.StringType;
import org.eclipse.escet.chi.metamodel.chi.TimeLiteral;
import org.eclipse.escet.chi.metamodel.chi.TimerType;
import org.eclipse.escet.chi.metamodel.chi.TupleExpression;
import org.eclipse.escet.chi.metamodel.chi.TupleField;
import org.eclipse.escet.chi.metamodel.chi.TupleType;
import org.eclipse.escet.chi.metamodel.chi.Type;
import org.eclipse.escet.chi.metamodel.chi.TypeDeclaration;
import org.eclipse.escet.chi.metamodel.chi.TypeReference;
import org.eclipse.escet.chi.metamodel.chi.UnaryExpression;
import org.eclipse.escet.chi.metamodel.chi.UnaryOperators;
import org.eclipse.escet.chi.metamodel.chi.UnresolvedReference;
import org.eclipse.escet.chi.metamodel.chi.UnresolvedType;
import org.eclipse.escet.chi.metamodel.chi.Unwind;
import org.eclipse.escet.chi.metamodel.chi.VariableDeclaration;
import org.eclipse.escet.chi.metamodel.chi.VariableReference;
import org.eclipse.escet.chi.metamodel.chi.VoidType;
import org.eclipse.escet.chi.metamodel.chi.WhileStatement;
import org.eclipse.escet.chi.metamodel.chi.WriteStatement;
import org.eclipse.escet.chi.metamodel.chi.XperDeclaration;
import org.eclipse.escet.common.position.metamodel.position.Position;

/**
 * Helper class with static constructor methods for the "chi" language.
 */
public class ChiConstructors {
    /** Constructor for the {@link ChiConstructors} class. */
    private ChiConstructors() {
        // Static class.
    }

    /**
     * Returns a new instance of the {@link AssignmentStatement} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link AssignmentStatement} class.
     */
    public static AssignmentStatement newAssignmentStatement() {
        return ChiFactory.eINSTANCE.createAssignmentStatement();
    }

    /**
     * Returns a new instance of the {@link AssignmentStatement} class.
     *
     * @param lhs The "lhs" of the new "AssignmentStatement". Multiplicity [1..1]. May be {@code null} to set the "lhs" later.
     * @param position The "position" of the new "AssignmentStatement". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param rhs The "rhs" of the new "AssignmentStatement". Multiplicity [1..1]. May be {@code null} to set the "rhs" later.
     * @return A new instance of the {@link AssignmentStatement} class.
     */
    public static AssignmentStatement newAssignmentStatement(Expression lhs, Position position, Expression rhs) {
        AssignmentStatement rslt_ = newAssignmentStatement();
        if (lhs != null) {
            rslt_.setLhs(lhs);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (rhs != null) {
            rslt_.setRhs(rhs);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link BinaryExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link BinaryExpression} class.
     */
    public static BinaryExpression newBinaryExpression() {
        return ChiFactory.eINSTANCE.createBinaryExpression();
    }

    /**
     * Returns a new instance of the {@link BinaryExpression} class.
     *
     * @param left The "left" of the new "BinaryExpression". Multiplicity [1..1]. May be {@code null} to set the "left" later.
     * @param op The "op" of the new "BinaryExpression". Multiplicity [1..1]. May be {@code null} to set the "op" later.
     * @param position The "position" of the new "BinaryExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param right The "right" of the new "BinaryExpression". Multiplicity [1..1]. May be {@code null} to set the "right" later.
     * @param type The "type" of the new "BinaryExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "type", or to set it later.
     * @return A new instance of the {@link BinaryExpression} class.
     */
    public static BinaryExpression newBinaryExpression(Expression left, BinaryOperators op, Position position, Expression right, Type type) {
        BinaryExpression rslt_ = newBinaryExpression();
        if (left != null) {
            rslt_.setLeft(left);
        }
        if (op != null) {
            rslt_.setOp(op);
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
     * Returns a new instance of the {@link BoolLiteral} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link BoolLiteral} class.
     */
    public static BoolLiteral newBoolLiteral() {
        return ChiFactory.eINSTANCE.createBoolLiteral();
    }

    /**
     * Returns a new instance of the {@link BoolLiteral} class.
     *
     * @param position The "position" of the new "BoolLiteral". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "BoolLiteral". Multiplicity [0..1]. May be {@code null} to skip setting the "type", or to set it later.
     * @param value The "value" of the new "BoolLiteral". Multiplicity [1..1]. May be {@code null} to set the "value" later.
     * @return A new instance of the {@link BoolLiteral} class.
     */
    public static BoolLiteral newBoolLiteral(Position position, Type type, Boolean value) {
        BoolLiteral rslt_ = newBoolLiteral();
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
        return ChiFactory.eINSTANCE.createBoolType();
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
     * Returns a new instance of the {@link BreakStatement} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link BreakStatement} class.
     */
    public static BreakStatement newBreakStatement() {
        return ChiFactory.eINSTANCE.createBreakStatement();
    }

    /**
     * Returns a new instance of the {@link BreakStatement} class.
     *
     * @param position The "position" of the new "BreakStatement". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link BreakStatement} class.
     */
    public static BreakStatement newBreakStatement(Position position) {
        BreakStatement rslt_ = newBreakStatement();
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link CallExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link CallExpression} class.
     */
    public static CallExpression newCallExpression() {
        return ChiFactory.eINSTANCE.createCallExpression();
    }

    /**
     * Returns a new instance of the {@link CallExpression} class.
     *
     * @param arguments The "arguments" of the new "CallExpression". Multiplicity [0..*]. May be {@code null} to skip setting the "arguments", or to set it later.
     * @param function The "function" of the new "CallExpression". Multiplicity [1..1]. May be {@code null} to set the "function" later.
     * @param name The "name" of the new "CallExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "name", or to set it later.
     * @param position The "position" of the new "CallExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "CallExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "type", or to set it later.
     * @return A new instance of the {@link CallExpression} class.
     */
    public static CallExpression newCallExpression(List<Expression> arguments, Expression function, Expression name, Position position, Type type) {
        CallExpression rslt_ = newCallExpression();
        if (arguments != null) {
            rslt_.getArguments().addAll(arguments);
        }
        if (function != null) {
            rslt_.setFunction(function);
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
     * Returns a new instance of the {@link CastExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link CastExpression} class.
     */
    public static CastExpression newCastExpression() {
        return ChiFactory.eINSTANCE.createCastExpression();
    }

    /**
     * Returns a new instance of the {@link CastExpression} class.
     *
     * @param castType The "castType" of the new "CastExpression". Multiplicity [1..1]. May be {@code null} to set the "castType" later.
     * @param expression The "expression" of the new "CastExpression". Multiplicity [1..1]. May be {@code null} to set the "expression" later.
     * @param position The "position" of the new "CastExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "CastExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "type", or to set it later.
     * @return A new instance of the {@link CastExpression} class.
     */
    public static CastExpression newCastExpression(Type castType, Expression expression, Position position, Type type) {
        CastExpression rslt_ = newCastExpression();
        if (castType != null) {
            rslt_.setCastType(castType);
        }
        if (expression != null) {
            rslt_.setExpression(expression);
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
     * Returns a new instance of the {@link ChannelExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link ChannelExpression} class.
     */
    public static ChannelExpression newChannelExpression() {
        return ChiFactory.eINSTANCE.createChannelExpression();
    }

    /**
     * Returns a new instance of the {@link ChannelExpression} class.
     *
     * @param elementType The "elementType" of the new "ChannelExpression". Multiplicity [1..1]. May be {@code null} to set the "elementType" later.
     * @param position The "position" of the new "ChannelExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "ChannelExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "type", or to set it later.
     * @return A new instance of the {@link ChannelExpression} class.
     */
    public static ChannelExpression newChannelExpression(Type elementType, Position position, Type type) {
        ChannelExpression rslt_ = newChannelExpression();
        if (elementType != null) {
            rslt_.setElementType(elementType);
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
     * Returns a new instance of the {@link ChannelType} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link ChannelType} class.
     */
    public static ChannelType newChannelType() {
        return ChiFactory.eINSTANCE.createChannelType();
    }

    /**
     * Returns a new instance of the {@link ChannelType} class.
     *
     * @param elementType The "elementType" of the new "ChannelType". Multiplicity [1..1]. May be {@code null} to set the "elementType" later.
     * @param ops The "ops" of the new "ChannelType". Multiplicity [1..1]. May be {@code null} to set the "ops" later.
     * @param position The "position" of the new "ChannelType". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link ChannelType} class.
     */
    public static ChannelType newChannelType(Type elementType, ChannelOps ops, Position position) {
        ChannelType rslt_ = newChannelType();
        if (elementType != null) {
            rslt_.setElementType(elementType);
        }
        if (ops != null) {
            rslt_.setOps(ops);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link CloseStatement} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link CloseStatement} class.
     */
    public static CloseStatement newCloseStatement() {
        return ChiFactory.eINSTANCE.createCloseStatement();
    }

    /**
     * Returns a new instance of the {@link CloseStatement} class.
     *
     * @param handle The "handle" of the new "CloseStatement". Multiplicity [1..1]. May be {@code null} to set the "handle" later.
     * @param position The "position" of the new "CloseStatement". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link CloseStatement} class.
     */
    public static CloseStatement newCloseStatement(Expression handle, Position position) {
        CloseStatement rslt_ = newCloseStatement();
        if (handle != null) {
            rslt_.setHandle(handle);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link ConstantDeclaration} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link ConstantDeclaration} class.
     */
    public static ConstantDeclaration newConstantDeclaration() {
        return ChiFactory.eINSTANCE.createConstantDeclaration();
    }

    /**
     * Returns a new instance of the {@link ConstantDeclaration} class.
     *
     * @param name The "name" of the new "ConstantDeclaration". Multiplicity [1..1]. May be {@code null} to set the "name" later.
     * @param position The "position" of the new "ConstantDeclaration". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "ConstantDeclaration". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @param value The "value" of the new "ConstantDeclaration". Multiplicity [1..1]. May be {@code null} to set the "value" later.
     * @return A new instance of the {@link ConstantDeclaration} class.
     */
    public static ConstantDeclaration newConstantDeclaration(String name, Position position, Type type, Expression value) {
        ConstantDeclaration rslt_ = newConstantDeclaration();
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
     * Returns a new instance of the {@link ConstantReference} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link ConstantReference} class.
     */
    public static ConstantReference newConstantReference() {
        return ChiFactory.eINSTANCE.createConstantReference();
    }

    /**
     * Returns a new instance of the {@link ConstantReference} class.
     *
     * @param constant The "constant" of the new "ConstantReference". Multiplicity [1..1]. May be {@code null} to set the "constant" later.
     * @param position The "position" of the new "ConstantReference". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "ConstantReference". Multiplicity [0..1]. May be {@code null} to skip setting the "type", or to set it later.
     * @return A new instance of the {@link ConstantReference} class.
     */
    public static ConstantReference newConstantReference(ConstantDeclaration constant, Position position, Type type) {
        ConstantReference rslt_ = newConstantReference();
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
     * Returns a new instance of the {@link ContinueStatement} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link ContinueStatement} class.
     */
    public static ContinueStatement newContinueStatement() {
        return ChiFactory.eINSTANCE.createContinueStatement();
    }

    /**
     * Returns a new instance of the {@link ContinueStatement} class.
     *
     * @param position The "position" of the new "ContinueStatement". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link ContinueStatement} class.
     */
    public static ContinueStatement newContinueStatement(Position position) {
        ContinueStatement rslt_ = newContinueStatement();
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link DelayStatement} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link DelayStatement} class.
     */
    public static DelayStatement newDelayStatement() {
        return ChiFactory.eINSTANCE.createDelayStatement();
    }

    /**
     * Returns a new instance of the {@link DelayStatement} class.
     *
     * @param length The "length" of the new "DelayStatement". Multiplicity [1..1]. May be {@code null} to set the "length" later.
     * @param position The "position" of the new "DelayStatement". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link DelayStatement} class.
     */
    public static DelayStatement newDelayStatement(Expression length, Position position) {
        DelayStatement rslt_ = newDelayStatement();
        if (length != null) {
            rslt_.setLength(length);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link DictType} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link DictType} class.
     */
    public static DictType newDictType() {
        return ChiFactory.eINSTANCE.createDictType();
    }

    /**
     * Returns a new instance of the {@link DictType} class.
     *
     * @param keyType The "keyType" of the new "DictType". Multiplicity [1..1]. May be {@code null} to set the "keyType" later.
     * @param position The "position" of the new "DictType". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param valueType The "valueType" of the new "DictType". Multiplicity [1..1]. May be {@code null} to set the "valueType" later.
     * @return A new instance of the {@link DictType} class.
     */
    public static DictType newDictType(Type keyType, Position position, Type valueType) {
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
     * Returns a new instance of the {@link DictionaryExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link DictionaryExpression} class.
     */
    public static DictionaryExpression newDictionaryExpression() {
        return ChiFactory.eINSTANCE.createDictionaryExpression();
    }

    /**
     * Returns a new instance of the {@link DictionaryExpression} class.
     *
     * @param pairs The "pairs" of the new "DictionaryExpression". Multiplicity [0..*]. May be {@code null} to skip setting the "pairs", or to set it later.
     * @param position The "position" of the new "DictionaryExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "DictionaryExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "type", or to set it later.
     * @return A new instance of the {@link DictionaryExpression} class.
     */
    public static DictionaryExpression newDictionaryExpression(List<DictionaryPair> pairs, Position position, Type type) {
        DictionaryExpression rslt_ = newDictionaryExpression();
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
     * Returns a new instance of the {@link DictionaryPair} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link DictionaryPair} class.
     */
    public static DictionaryPair newDictionaryPair() {
        return ChiFactory.eINSTANCE.createDictionaryPair();
    }

    /**
     * Returns a new instance of the {@link DictionaryPair} class.
     *
     * @param key The "key" of the new "DictionaryPair". Multiplicity [1..1]. May be {@code null} to set the "key" later.
     * @param position The "position" of the new "DictionaryPair". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param value The "value" of the new "DictionaryPair". Multiplicity [1..1]. May be {@code null} to set the "value" later.
     * @return A new instance of the {@link DictionaryPair} class.
     */
    public static DictionaryPair newDictionaryPair(Expression key, Position position, Expression value) {
        DictionaryPair rslt_ = newDictionaryPair();
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
     * Returns a new instance of the {@link DistributionType} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link DistributionType} class.
     */
    public static DistributionType newDistributionType() {
        return ChiFactory.eINSTANCE.createDistributionType();
    }

    /**
     * Returns a new instance of the {@link DistributionType} class.
     *
     * @param position The "position" of the new "DistributionType". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param resultType The "resultType" of the new "DistributionType". Multiplicity [1..1]. May be {@code null} to set the "resultType" later.
     * @return A new instance of the {@link DistributionType} class.
     */
    public static DistributionType newDistributionType(Position position, Type resultType) {
        DistributionType rslt_ = newDistributionType();
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (resultType != null) {
            rslt_.setResultType(resultType);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link EnumDeclaration} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link EnumDeclaration} class.
     */
    public static EnumDeclaration newEnumDeclaration() {
        return ChiFactory.eINSTANCE.createEnumDeclaration();
    }

    /**
     * Returns a new instance of the {@link EnumDeclaration} class.
     *
     * @param name The "name" of the new "EnumDeclaration". Multiplicity [1..1]. May be {@code null} to set the "name" later.
     * @param position The "position" of the new "EnumDeclaration". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param values The "values" of the new "EnumDeclaration". Multiplicity [1..*]. May be {@code null} to set the "values" later.
     * @return A new instance of the {@link EnumDeclaration} class.
     */
    public static EnumDeclaration newEnumDeclaration(String name, Position position, List<EnumValue> values) {
        EnumDeclaration rslt_ = newEnumDeclaration();
        if (name != null) {
            rslt_.setName(name);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (values != null) {
            rslt_.getValues().addAll(values);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link EnumTypeReference} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link EnumTypeReference} class.
     */
    public static EnumTypeReference newEnumTypeReference() {
        return ChiFactory.eINSTANCE.createEnumTypeReference();
    }

    /**
     * Returns a new instance of the {@link EnumTypeReference} class.
     *
     * @param position The "position" of the new "EnumTypeReference". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "EnumTypeReference". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @return A new instance of the {@link EnumTypeReference} class.
     */
    public static EnumTypeReference newEnumTypeReference(Position position, EnumDeclaration type) {
        EnumTypeReference rslt_ = newEnumTypeReference();
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (type != null) {
            rslt_.setType(type);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link EnumValue} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link EnumValue} class.
     */
    public static EnumValue newEnumValue() {
        return ChiFactory.eINSTANCE.createEnumValue();
    }

    /**
     * Returns a new instance of the {@link EnumValue} class.
     *
     * @param name The "name" of the new "EnumValue". Multiplicity [1..1]. May be {@code null} to set the "name" later.
     * @param position The "position" of the new "EnumValue". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link EnumValue} class.
     */
    public static EnumValue newEnumValue(String name, Position position) {
        EnumValue rslt_ = newEnumValue();
        if (name != null) {
            rslt_.setName(name);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link EnumValueReference} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link EnumValueReference} class.
     */
    public static EnumValueReference newEnumValueReference() {
        return ChiFactory.eINSTANCE.createEnumValueReference();
    }

    /**
     * Returns a new instance of the {@link EnumValueReference} class.
     *
     * @param position The "position" of the new "EnumValueReference". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "EnumValueReference". Multiplicity [0..1]. May be {@code null} to skip setting the "type", or to set it later.
     * @param value The "value" of the new "EnumValueReference". Multiplicity [1..1]. May be {@code null} to set the "value" later.
     * @return A new instance of the {@link EnumValueReference} class.
     */
    public static EnumValueReference newEnumValueReference(Position position, Type type, EnumValue value) {
        EnumValueReference rslt_ = newEnumValueReference();
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
     * Returns a new instance of the {@link ExitStatement} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link ExitStatement} class.
     */
    public static ExitStatement newExitStatement() {
        return ChiFactory.eINSTANCE.createExitStatement();
    }

    /**
     * Returns a new instance of the {@link ExitStatement} class.
     *
     * @param position The "position" of the new "ExitStatement". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param value The "value" of the new "ExitStatement". Multiplicity [0..1]. May be {@code null} to skip setting the "value", or to set it later.
     * @return A new instance of the {@link ExitStatement} class.
     */
    public static ExitStatement newExitStatement(Position position, Expression value) {
        ExitStatement rslt_ = newExitStatement();
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (value != null) {
            rslt_.setValue(value);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link FieldReference} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link FieldReference} class.
     */
    public static FieldReference newFieldReference() {
        return ChiFactory.eINSTANCE.createFieldReference();
    }

    /**
     * Returns a new instance of the {@link FieldReference} class.
     *
     * @param field The "field" of the new "FieldReference". Multiplicity [1..1]. May be {@code null} to set the "field" later.
     * @param position The "position" of the new "FieldReference". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "FieldReference". Multiplicity [0..1]. May be {@code null} to skip setting the "type", or to set it later.
     * @return A new instance of the {@link FieldReference} class.
     */
    public static FieldReference newFieldReference(TupleField field, Position position, Type type) {
        FieldReference rslt_ = newFieldReference();
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
     * Returns a new instance of the {@link FileType} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link FileType} class.
     */
    public static FileType newFileType() {
        return ChiFactory.eINSTANCE.createFileType();
    }

    /**
     * Returns a new instance of the {@link FileType} class.
     *
     * @param position The "position" of the new "FileType". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link FileType} class.
     */
    public static FileType newFileType(Position position) {
        FileType rslt_ = newFileType();
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link FinishStatement} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link FinishStatement} class.
     */
    public static FinishStatement newFinishStatement() {
        return ChiFactory.eINSTANCE.createFinishStatement();
    }

    /**
     * Returns a new instance of the {@link FinishStatement} class.
     *
     * @param instances The "instances" of the new "FinishStatement". Multiplicity [1..*]. May be {@code null} to set the "instances" later.
     * @param position The "position" of the new "FinishStatement". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link FinishStatement} class.
     */
    public static FinishStatement newFinishStatement(List<Expression> instances, Position position) {
        FinishStatement rslt_ = newFinishStatement();
        if (instances != null) {
            rslt_.getInstances().addAll(instances);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link ForStatement} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link ForStatement} class.
     */
    public static ForStatement newForStatement() {
        return ChiFactory.eINSTANCE.createForStatement();
    }

    /**
     * Returns a new instance of the {@link ForStatement} class.
     *
     * @param body The "body" of the new "ForStatement". Multiplicity [1..*]. May be {@code null} to set the "body" later.
     * @param position The "position" of the new "ForStatement". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param unwinds The "unwinds" of the new "ForStatement". Multiplicity [1..*]. May be {@code null} to set the "unwinds" later.
     * @return A new instance of the {@link ForStatement} class.
     */
    public static ForStatement newForStatement(List<Statement> body, Position position, List<Unwind> unwinds) {
        ForStatement rslt_ = newForStatement();
        if (body != null) {
            rslt_.getBody().addAll(body);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (unwinds != null) {
            rslt_.getUnwinds().addAll(unwinds);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link FunctionDeclaration} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link FunctionDeclaration} class.
     */
    public static FunctionDeclaration newFunctionDeclaration() {
        return ChiFactory.eINSTANCE.createFunctionDeclaration();
    }

    /**
     * Returns a new instance of the {@link FunctionDeclaration} class.
     *
     * @param name The "name" of the new "FunctionDeclaration". Multiplicity [1..1]. May be {@code null} to set the "name" later.
     * @param position The "position" of the new "FunctionDeclaration". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param returnType The "returnType" of the new "FunctionDeclaration". Multiplicity [1..1]. May be {@code null} to set the "returnType" later.
     * @param statements The "statements" of the new "FunctionDeclaration". Multiplicity [1..*]. May be {@code null} to set the "statements" later.
     * @param variables The "variables" of the new "FunctionDeclaration". Multiplicity [0..*]. May be {@code null} to skip setting the "variables", or to set it later.
     * @return A new instance of the {@link FunctionDeclaration} class.
     */
    public static FunctionDeclaration newFunctionDeclaration(String name, Position position, Type returnType, List<Statement> statements, List<VariableDeclaration> variables) {
        FunctionDeclaration rslt_ = newFunctionDeclaration();
        if (name != null) {
            rslt_.setName(name);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (returnType != null) {
            rslt_.setReturnType(returnType);
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
     * Returns a new instance of the {@link FunctionReference} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link FunctionReference} class.
     */
    public static FunctionReference newFunctionReference() {
        return ChiFactory.eINSTANCE.createFunctionReference();
    }

    /**
     * Returns a new instance of the {@link FunctionReference} class.
     *
     * @param function The "function" of the new "FunctionReference". Multiplicity [1..1]. May be {@code null} to set the "function" later.
     * @param position The "position" of the new "FunctionReference". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "FunctionReference". Multiplicity [0..1]. May be {@code null} to skip setting the "type", or to set it later.
     * @return A new instance of the {@link FunctionReference} class.
     */
    public static FunctionReference newFunctionReference(FunctionDeclaration function, Position position, Type type) {
        FunctionReference rslt_ = newFunctionReference();
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
     * Returns a new instance of the {@link FunctionType} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link FunctionType} class.
     */
    public static FunctionType newFunctionType() {
        return ChiFactory.eINSTANCE.createFunctionType();
    }

    /**
     * Returns a new instance of the {@link FunctionType} class.
     *
     * @param parameterTypes The "parameterTypes" of the new "FunctionType". Multiplicity [0..*]. May be {@code null} to skip setting the "parameterTypes", or to set it later.
     * @param position The "position" of the new "FunctionType". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param resultType The "resultType" of the new "FunctionType". Multiplicity [1..1]. May be {@code null} to set the "resultType" later.
     * @return A new instance of the {@link FunctionType} class.
     */
    public static FunctionType newFunctionType(List<Type> parameterTypes, Position position, Type resultType) {
        FunctionType rslt_ = newFunctionType();
        if (parameterTypes != null) {
            rslt_.getParameterTypes().addAll(parameterTypes);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (resultType != null) {
            rslt_.setResultType(resultType);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link IfCase} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link IfCase} class.
     */
    public static IfCase newIfCase() {
        return ChiFactory.eINSTANCE.createIfCase();
    }

    /**
     * Returns a new instance of the {@link IfCase} class.
     *
     * @param body The "body" of the new "IfCase". Multiplicity [1..*]. May be {@code null} to set the "body" later.
     * @param condition The "condition" of the new "IfCase". Multiplicity [0..1]. May be {@code null} to skip setting the "condition", or to set it later.
     * @param position The "position" of the new "IfCase". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link IfCase} class.
     */
    public static IfCase newIfCase(List<Statement> body, Expression condition, Position position) {
        IfCase rslt_ = newIfCase();
        if (body != null) {
            rslt_.getBody().addAll(body);
        }
        if (condition != null) {
            rslt_.setCondition(condition);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link IfStatement} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link IfStatement} class.
     */
    public static IfStatement newIfStatement() {
        return ChiFactory.eINSTANCE.createIfStatement();
    }

    /**
     * Returns a new instance of the {@link IfStatement} class.
     *
     * @param cases The "cases" of the new "IfStatement". Multiplicity [1..*]. May be {@code null} to set the "cases" later.
     * @param position The "position" of the new "IfStatement". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link IfStatement} class.
     */
    public static IfStatement newIfStatement(List<IfCase> cases, Position position) {
        IfStatement rslt_ = newIfStatement();
        if (cases != null) {
            rslt_.getCases().addAll(cases);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link InstanceType} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link InstanceType} class.
     */
    public static InstanceType newInstanceType() {
        return ChiFactory.eINSTANCE.createInstanceType();
    }

    /**
     * Returns a new instance of the {@link InstanceType} class.
     *
     * @param position The "position" of the new "InstanceType". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link InstanceType} class.
     */
    public static InstanceType newInstanceType(Position position) {
        InstanceType rslt_ = newInstanceType();
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link IntNumber} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link IntNumber} class.
     */
    public static IntNumber newIntNumber() {
        return ChiFactory.eINSTANCE.createIntNumber();
    }

    /**
     * Returns a new instance of the {@link IntNumber} class.
     *
     * @param position The "position" of the new "IntNumber". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "IntNumber". Multiplicity [0..1]. May be {@code null} to skip setting the "type", or to set it later.
     * @param value The "value" of the new "IntNumber". Multiplicity [1..1]. May be {@code null} to set the "value" later.
     * @return A new instance of the {@link IntNumber} class.
     */
    public static IntNumber newIntNumber(Position position, Type type, String value) {
        IntNumber rslt_ = newIntNumber();
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
        return ChiFactory.eINSTANCE.createIntType();
    }

    /**
     * Returns a new instance of the {@link IntType} class.
     *
     * @param position The "position" of the new "IntType". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link IntType} class.
     */
    public static IntType newIntType(Position position) {
        IntType rslt_ = newIntType();
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link IteratedCreateCase} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link IteratedCreateCase} class.
     */
    public static IteratedCreateCase newIteratedCreateCase() {
        return ChiFactory.eINSTANCE.createIteratedCreateCase();
    }

    /**
     * Returns a new instance of the {@link IteratedCreateCase} class.
     *
     * @param instances The "instances" of the new "IteratedCreateCase". Multiplicity [1..*]. May be {@code null} to set the "instances" later.
     * @param position The "position" of the new "IteratedCreateCase". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param unwinds The "unwinds" of the new "IteratedCreateCase". Multiplicity [1..*]. May be {@code null} to set the "unwinds" later.
     * @return A new instance of the {@link IteratedCreateCase} class.
     */
    public static IteratedCreateCase newIteratedCreateCase(List<CreateCase> instances, Position position, List<Unwind> unwinds) {
        IteratedCreateCase rslt_ = newIteratedCreateCase();
        if (instances != null) {
            rslt_.getInstances().addAll(instances);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (unwinds != null) {
            rslt_.getUnwinds().addAll(unwinds);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link IteratedSelectCase} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link IteratedSelectCase} class.
     */
    public static IteratedSelectCase newIteratedSelectCase() {
        return ChiFactory.eINSTANCE.createIteratedSelectCase();
    }

    /**
     * Returns a new instance of the {@link IteratedSelectCase} class.
     *
     * @param body The "body" of the new "IteratedSelectCase". Multiplicity [1..*]. May be {@code null} to set the "body" later.
     * @param guard The "guard" of the new "IteratedSelectCase". Multiplicity [0..1]. May be {@code null} to skip setting the "guard", or to set it later.
     * @param position The "position" of the new "IteratedSelectCase". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param unwinds The "unwinds" of the new "IteratedSelectCase". Multiplicity [1..*]. May be {@code null} to set the "unwinds" later.
     * @return A new instance of the {@link IteratedSelectCase} class.
     */
    public static IteratedSelectCase newIteratedSelectCase(List<Statement> body, Expression guard, Position position, List<Unwind> unwinds) {
        IteratedSelectCase rslt_ = newIteratedSelectCase();
        if (body != null) {
            rslt_.getBody().addAll(body);
        }
        if (guard != null) {
            rslt_.setGuard(guard);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (unwinds != null) {
            rslt_.getUnwinds().addAll(unwinds);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link ListExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link ListExpression} class.
     */
    public static ListExpression newListExpression() {
        return ChiFactory.eINSTANCE.createListExpression();
    }

    /**
     * Returns a new instance of the {@link ListExpression} class.
     *
     * @param elements The "elements" of the new "ListExpression". Multiplicity [0..*]. May be {@code null} to skip setting the "elements", or to set it later.
     * @param position The "position" of the new "ListExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "ListExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "type", or to set it later.
     * @return A new instance of the {@link ListExpression} class.
     */
    public static ListExpression newListExpression(List<Expression> elements, Position position, Type type) {
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
        return ChiFactory.eINSTANCE.createListType();
    }

    /**
     * Returns a new instance of the {@link ListType} class.
     *
     * @param elementType The "elementType" of the new "ListType". Multiplicity [1..1]. May be {@code null} to set the "elementType" later.
     * @param initialLength The "initialLength" of the new "ListType". Multiplicity [0..1]. May be {@code null} to skip setting the "initialLength", or to set it later.
     * @param position The "position" of the new "ListType". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link ListType} class.
     */
    public static ListType newListType(Type elementType, Expression initialLength, Position position) {
        ListType rslt_ = newListType();
        if (elementType != null) {
            rslt_.setElementType(elementType);
        }
        if (initialLength != null) {
            rslt_.setInitialLength(initialLength);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link MatrixExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link MatrixExpression} class.
     */
    public static MatrixExpression newMatrixExpression() {
        return ChiFactory.eINSTANCE.createMatrixExpression();
    }

    /**
     * Returns a new instance of the {@link MatrixExpression} class.
     *
     * @param position The "position" of the new "MatrixExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param rows The "rows" of the new "MatrixExpression". Multiplicity [1..*]. May be {@code null} to set the "rows" later.
     * @param type The "type" of the new "MatrixExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "type", or to set it later.
     * @return A new instance of the {@link MatrixExpression} class.
     */
    public static MatrixExpression newMatrixExpression(Position position, List<MatrixRow> rows, Type type) {
        MatrixExpression rslt_ = newMatrixExpression();
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (rows != null) {
            rslt_.getRows().addAll(rows);
        }
        if (type != null) {
            rslt_.setType(type);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link MatrixRow} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link MatrixRow} class.
     */
    public static MatrixRow newMatrixRow() {
        return ChiFactory.eINSTANCE.createMatrixRow();
    }

    /**
     * Returns a new instance of the {@link MatrixRow} class.
     *
     * @param elements The "elements" of the new "MatrixRow". Multiplicity [1..*]. May be {@code null} to set the "elements" later.
     * @param position The "position" of the new "MatrixRow". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link MatrixRow} class.
     */
    public static MatrixRow newMatrixRow(List<Expression> elements, Position position) {
        MatrixRow rslt_ = newMatrixRow();
        if (elements != null) {
            rslt_.getElements().addAll(elements);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link MatrixType} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link MatrixType} class.
     */
    public static MatrixType newMatrixType() {
        return ChiFactory.eINSTANCE.createMatrixType();
    }

    /**
     * Returns a new instance of the {@link MatrixType} class.
     *
     * @param columnSize The "columnSize" of the new "MatrixType". Multiplicity [1..1]. May be {@code null} to set the "columnSize" later.
     * @param position The "position" of the new "MatrixType". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param rowSize The "rowSize" of the new "MatrixType". Multiplicity [1..1]. May be {@code null} to set the "rowSize" later.
     * @return A new instance of the {@link MatrixType} class.
     */
    public static MatrixType newMatrixType(Expression columnSize, Position position, Expression rowSize) {
        MatrixType rslt_ = newMatrixType();
        if (columnSize != null) {
            rslt_.setColumnSize(columnSize);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (rowSize != null) {
            rslt_.setRowSize(rowSize);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link ModelDeclaration} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link ModelDeclaration} class.
     */
    public static ModelDeclaration newModelDeclaration() {
        return ChiFactory.eINSTANCE.createModelDeclaration();
    }

    /**
     * Returns a new instance of the {@link ModelDeclaration} class.
     *
     * @param name The "name" of the new "ModelDeclaration". Multiplicity [1..1]. May be {@code null} to set the "name" later.
     * @param position The "position" of the new "ModelDeclaration". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param returnType The "returnType" of the new "ModelDeclaration". Multiplicity [0..1]. May be {@code null} to skip setting the "returnType", or to set it later.
     * @param statements The "statements" of the new "ModelDeclaration". Multiplicity [1..*]. May be {@code null} to set the "statements" later.
     * @param variables The "variables" of the new "ModelDeclaration". Multiplicity [0..*]. May be {@code null} to skip setting the "variables", or to set it later.
     * @return A new instance of the {@link ModelDeclaration} class.
     */
    public static ModelDeclaration newModelDeclaration(String name, Position position, Type returnType, List<Statement> statements, List<VariableDeclaration> variables) {
        ModelDeclaration rslt_ = newModelDeclaration();
        if (name != null) {
            rslt_.setName(name);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (returnType != null) {
            rslt_.setReturnType(returnType);
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
     * Returns a new instance of the {@link ModelReference} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link ModelReference} class.
     */
    public static ModelReference newModelReference() {
        return ChiFactory.eINSTANCE.createModelReference();
    }

    /**
     * Returns a new instance of the {@link ModelReference} class.
     *
     * @param model The "model" of the new "ModelReference". Multiplicity [1..1]. May be {@code null} to set the "model" later.
     * @param position The "position" of the new "ModelReference". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "ModelReference". Multiplicity [0..1]. May be {@code null} to skip setting the "type", or to set it later.
     * @return A new instance of the {@link ModelReference} class.
     */
    public static ModelReference newModelReference(ModelDeclaration model, Position position, Type type) {
        ModelReference rslt_ = newModelReference();
        if (model != null) {
            rslt_.setModel(model);
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
     * Returns a new instance of the {@link ModelType} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link ModelType} class.
     */
    public static ModelType newModelType() {
        return ChiFactory.eINSTANCE.createModelType();
    }

    /**
     * Returns a new instance of the {@link ModelType} class.
     *
     * @param exitType The "exitType" of the new "ModelType". Multiplicity [0..1]. May be {@code null} to skip setting the "exitType", or to set it later.
     * @param parameterTypes The "parameterTypes" of the new "ModelType". Multiplicity [0..*]. May be {@code null} to skip setting the "parameterTypes", or to set it later.
     * @param position The "position" of the new "ModelType". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link ModelType} class.
     */
    public static ModelType newModelType(Type exitType, List<Type> parameterTypes, Position position) {
        ModelType rslt_ = newModelType();
        if (exitType != null) {
            rslt_.setExitType(exitType);
        }
        if (parameterTypes != null) {
            rslt_.getParameterTypes().addAll(parameterTypes);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link PassStatement} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link PassStatement} class.
     */
    public static PassStatement newPassStatement() {
        return ChiFactory.eINSTANCE.createPassStatement();
    }

    /**
     * Returns a new instance of the {@link PassStatement} class.
     *
     * @param position The "position" of the new "PassStatement". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link PassStatement} class.
     */
    public static PassStatement newPassStatement(Position position) {
        PassStatement rslt_ = newPassStatement();
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link ProcessDeclaration} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link ProcessDeclaration} class.
     */
    public static ProcessDeclaration newProcessDeclaration() {
        return ChiFactory.eINSTANCE.createProcessDeclaration();
    }

    /**
     * Returns a new instance of the {@link ProcessDeclaration} class.
     *
     * @param name The "name" of the new "ProcessDeclaration". Multiplicity [1..1]. May be {@code null} to set the "name" later.
     * @param position The "position" of the new "ProcessDeclaration". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param returnType The "returnType" of the new "ProcessDeclaration". Multiplicity [0..1]. May be {@code null} to skip setting the "returnType", or to set it later.
     * @param statements The "statements" of the new "ProcessDeclaration". Multiplicity [1..*]. May be {@code null} to set the "statements" later.
     * @param variables The "variables" of the new "ProcessDeclaration". Multiplicity [0..*]. May be {@code null} to skip setting the "variables", or to set it later.
     * @return A new instance of the {@link ProcessDeclaration} class.
     */
    public static ProcessDeclaration newProcessDeclaration(String name, Position position, Type returnType, List<Statement> statements, List<VariableDeclaration> variables) {
        ProcessDeclaration rslt_ = newProcessDeclaration();
        if (name != null) {
            rslt_.setName(name);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (returnType != null) {
            rslt_.setReturnType(returnType);
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
     * Returns a new instance of the {@link ProcessInstance} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link ProcessInstance} class.
     */
    public static ProcessInstance newProcessInstance() {
        return ChiFactory.eINSTANCE.createProcessInstance();
    }

    /**
     * Returns a new instance of the {@link ProcessInstance} class.
     *
     * @param call The "call" of the new "ProcessInstance". Multiplicity [1..1]. May be {@code null} to set the "call" later.
     * @param position The "position" of the new "ProcessInstance". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param var The "var" of the new "ProcessInstance". Multiplicity [0..1]. May be {@code null} to skip setting the "var", or to set it later.
     * @return A new instance of the {@link ProcessInstance} class.
     */
    public static ProcessInstance newProcessInstance(Expression call, Position position, Expression var) {
        ProcessInstance rslt_ = newProcessInstance();
        if (call != null) {
            rslt_.setCall(call);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (var != null) {
            rslt_.setVar(var);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link ProcessReference} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link ProcessReference} class.
     */
    public static ProcessReference newProcessReference() {
        return ChiFactory.eINSTANCE.createProcessReference();
    }

    /**
     * Returns a new instance of the {@link ProcessReference} class.
     *
     * @param position The "position" of the new "ProcessReference". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param process The "process" of the new "ProcessReference". Multiplicity [1..1]. May be {@code null} to set the "process" later.
     * @param type The "type" of the new "ProcessReference". Multiplicity [0..1]. May be {@code null} to skip setting the "type", or to set it later.
     * @return A new instance of the {@link ProcessReference} class.
     */
    public static ProcessReference newProcessReference(Position position, ProcessDeclaration process, Type type) {
        ProcessReference rslt_ = newProcessReference();
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (process != null) {
            rslt_.setProcess(process);
        }
        if (type != null) {
            rslt_.setType(type);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link ProcessType} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link ProcessType} class.
     */
    public static ProcessType newProcessType() {
        return ChiFactory.eINSTANCE.createProcessType();
    }

    /**
     * Returns a new instance of the {@link ProcessType} class.
     *
     * @param exitType The "exitType" of the new "ProcessType". Multiplicity [0..1]. May be {@code null} to skip setting the "exitType", or to set it later.
     * @param parameterTypes The "parameterTypes" of the new "ProcessType". Multiplicity [0..*]. May be {@code null} to skip setting the "parameterTypes", or to set it later.
     * @param position The "position" of the new "ProcessType". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link ProcessType} class.
     */
    public static ProcessType newProcessType(Type exitType, List<Type> parameterTypes, Position position) {
        ProcessType rslt_ = newProcessType();
        if (exitType != null) {
            rslt_.setExitType(exitType);
        }
        if (parameterTypes != null) {
            rslt_.getParameterTypes().addAll(parameterTypes);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link ReadCallExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link ReadCallExpression} class.
     */
    public static ReadCallExpression newReadCallExpression() {
        return ChiFactory.eINSTANCE.createReadCallExpression();
    }

    /**
     * Returns a new instance of the {@link ReadCallExpression} class.
     *
     * @param file The "file" of the new "ReadCallExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "file", or to set it later.
     * @param loadType The "loadType" of the new "ReadCallExpression". Multiplicity [1..1]. May be {@code null} to set the "loadType" later.
     * @param position The "position" of the new "ReadCallExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "ReadCallExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "type", or to set it later.
     * @return A new instance of the {@link ReadCallExpression} class.
     */
    public static ReadCallExpression newReadCallExpression(Expression file, Type loadType, Position position, Type type) {
        ReadCallExpression rslt_ = newReadCallExpression();
        if (file != null) {
            rslt_.setFile(file);
        }
        if (loadType != null) {
            rslt_.setLoadType(loadType);
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
     * Returns a new instance of the {@link RealNumber} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link RealNumber} class.
     */
    public static RealNumber newRealNumber() {
        return ChiFactory.eINSTANCE.createRealNumber();
    }

    /**
     * Returns a new instance of the {@link RealNumber} class.
     *
     * @param position The "position" of the new "RealNumber". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "RealNumber". Multiplicity [0..1]. May be {@code null} to skip setting the "type", or to set it later.
     * @param value The "value" of the new "RealNumber". Multiplicity [1..1]. May be {@code null} to set the "value" later.
     * @return A new instance of the {@link RealNumber} class.
     */
    public static RealNumber newRealNumber(Position position, Type type, String value) {
        RealNumber rslt_ = newRealNumber();
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
        return ChiFactory.eINSTANCE.createRealType();
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
     * Returns a new instance of the {@link ReceiveStatement} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link ReceiveStatement} class.
     */
    public static ReceiveStatement newReceiveStatement() {
        return ChiFactory.eINSTANCE.createReceiveStatement();
    }

    /**
     * Returns a new instance of the {@link ReceiveStatement} class.
     *
     * @param channel The "channel" of the new "ReceiveStatement". Multiplicity [1..1]. May be {@code null} to set the "channel" later.
     * @param data The "data" of the new "ReceiveStatement". Multiplicity [0..1]. May be {@code null} to skip setting the "data", or to set it later.
     * @param position The "position" of the new "ReceiveStatement". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link ReceiveStatement} class.
     */
    public static ReceiveStatement newReceiveStatement(Expression channel, Expression data, Position position) {
        ReceiveStatement rslt_ = newReceiveStatement();
        if (channel != null) {
            rslt_.setChannel(channel);
        }
        if (data != null) {
            rslt_.setData(data);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link ReturnStatement} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link ReturnStatement} class.
     */
    public static ReturnStatement newReturnStatement() {
        return ChiFactory.eINSTANCE.createReturnStatement();
    }

    /**
     * Returns a new instance of the {@link ReturnStatement} class.
     *
     * @param position The "position" of the new "ReturnStatement". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param value The "value" of the new "ReturnStatement". Multiplicity [1..1]. May be {@code null} to set the "value" later.
     * @return A new instance of the {@link ReturnStatement} class.
     */
    public static ReturnStatement newReturnStatement(Position position, Expression value) {
        ReturnStatement rslt_ = newReturnStatement();
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (value != null) {
            rslt_.setValue(value);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link RunStatement} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link RunStatement} class.
     */
    public static RunStatement newRunStatement() {
        return ChiFactory.eINSTANCE.createRunStatement();
    }

    /**
     * Returns a new instance of the {@link RunStatement} class.
     *
     * @param cases The "cases" of the new "RunStatement". Multiplicity [1..*]. May be {@code null} to set the "cases" later.
     * @param position The "position" of the new "RunStatement". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param startOnly The "startOnly" of the new "RunStatement". Multiplicity [1..1]. May be {@code null} to set the "startOnly" later.
     * @return A new instance of the {@link RunStatement} class.
     */
    public static RunStatement newRunStatement(List<CreateCase> cases, Position position, Boolean startOnly) {
        RunStatement rslt_ = newRunStatement();
        if (cases != null) {
            rslt_.getCases().addAll(cases);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (startOnly != null) {
            rslt_.setStartOnly(startOnly);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link SelectCase} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link SelectCase} class.
     */
    public static SelectCase newSelectCase() {
        return ChiFactory.eINSTANCE.createSelectCase();
    }

    /**
     * Returns a new instance of the {@link SelectCase} class.
     *
     * @param body The "body" of the new "SelectCase". Multiplicity [1..*]. May be {@code null} to set the "body" later.
     * @param guard The "guard" of the new "SelectCase". Multiplicity [0..1]. May be {@code null} to skip setting the "guard", or to set it later.
     * @param position The "position" of the new "SelectCase". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link SelectCase} class.
     */
    public static SelectCase newSelectCase(List<Statement> body, Expression guard, Position position) {
        SelectCase rslt_ = newSelectCase();
        if (body != null) {
            rslt_.getBody().addAll(body);
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
     * Returns a new instance of the {@link SelectStatement} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link SelectStatement} class.
     */
    public static SelectStatement newSelectStatement() {
        return ChiFactory.eINSTANCE.createSelectStatement();
    }

    /**
     * Returns a new instance of the {@link SelectStatement} class.
     *
     * @param cases The "cases" of the new "SelectStatement". Multiplicity [1..*]. May be {@code null} to set the "cases" later.
     * @param position The "position" of the new "SelectStatement". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link SelectStatement} class.
     */
    public static SelectStatement newSelectStatement(List<SelectCase> cases, Position position) {
        SelectStatement rslt_ = newSelectStatement();
        if (cases != null) {
            rslt_.getCases().addAll(cases);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link SendStatement} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link SendStatement} class.
     */
    public static SendStatement newSendStatement() {
        return ChiFactory.eINSTANCE.createSendStatement();
    }

    /**
     * Returns a new instance of the {@link SendStatement} class.
     *
     * @param channel The "channel" of the new "SendStatement". Multiplicity [1..1]. May be {@code null} to set the "channel" later.
     * @param data The "data" of the new "SendStatement". Multiplicity [0..1]. May be {@code null} to skip setting the "data", or to set it later.
     * @param position The "position" of the new "SendStatement". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link SendStatement} class.
     */
    public static SendStatement newSendStatement(Expression channel, Expression data, Position position) {
        SendStatement rslt_ = newSendStatement();
        if (channel != null) {
            rslt_.setChannel(channel);
        }
        if (data != null) {
            rslt_.setData(data);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link SetExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link SetExpression} class.
     */
    public static SetExpression newSetExpression() {
        return ChiFactory.eINSTANCE.createSetExpression();
    }

    /**
     * Returns a new instance of the {@link SetExpression} class.
     *
     * @param elements The "elements" of the new "SetExpression". Multiplicity [0..*]. May be {@code null} to skip setting the "elements", or to set it later.
     * @param position The "position" of the new "SetExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "SetExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "type", or to set it later.
     * @return A new instance of the {@link SetExpression} class.
     */
    public static SetExpression newSetExpression(List<Expression> elements, Position position, Type type) {
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
        return ChiFactory.eINSTANCE.createSetType();
    }

    /**
     * Returns a new instance of the {@link SetType} class.
     *
     * @param elementType The "elementType" of the new "SetType". Multiplicity [1..1]. May be {@code null} to set the "elementType" later.
     * @param position The "position" of the new "SetType". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link SetType} class.
     */
    public static SetType newSetType(Type elementType, Position position) {
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
        return ChiFactory.eINSTANCE.createSliceExpression();
    }

    /**
     * Returns a new instance of the {@link SliceExpression} class.
     *
     * @param end The "end" of the new "SliceExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "end", or to set it later.
     * @param position The "position" of the new "SliceExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param source The "source" of the new "SliceExpression". Multiplicity [1..1]. May be {@code null} to set the "source" later.
     * @param start The "start" of the new "SliceExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "start", or to set it later.
     * @param step The "step" of the new "SliceExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "step", or to set it later.
     * @param type The "type" of the new "SliceExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "type", or to set it later.
     * @return A new instance of the {@link SliceExpression} class.
     */
    public static SliceExpression newSliceExpression(Expression end, Position position, Expression source, Expression start, Expression step, Type type) {
        SliceExpression rslt_ = newSliceExpression();
        if (end != null) {
            rslt_.setEnd(end);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (source != null) {
            rslt_.setSource(source);
        }
        if (start != null) {
            rslt_.setStart(start);
        }
        if (step != null) {
            rslt_.setStep(step);
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
        return ChiFactory.eINSTANCE.createSpecification();
    }

    /**
     * Returns a new instance of the {@link Specification} class.
     *
     * @param declarations The "declarations" of the new "Specification". Multiplicity [0..*]. May be {@code null} to skip setting the "declarations", or to set it later.
     * @return A new instance of the {@link Specification} class.
     */
    public static Specification newSpecification(List<Declaration> declarations) {
        Specification rslt_ = newSpecification();
        if (declarations != null) {
            rslt_.getDeclarations().addAll(declarations);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link StdLibFunctionReference} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link StdLibFunctionReference} class.
     */
    public static StdLibFunctionReference newStdLibFunctionReference() {
        return ChiFactory.eINSTANCE.createStdLibFunctionReference();
    }

    /**
     * Returns a new instance of the {@link StdLibFunctionReference} class.
     *
     * @param function The "function" of the new "StdLibFunctionReference". Multiplicity [1..1]. May be {@code null} to set the "function" later.
     * @param position The "position" of the new "StdLibFunctionReference". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "StdLibFunctionReference". Multiplicity [0..1]. May be {@code null} to skip setting the "type", or to set it later.
     * @return A new instance of the {@link StdLibFunctionReference} class.
     */
    public static StdLibFunctionReference newStdLibFunctionReference(StdLibFunctions function, Position position, Type type) {
        StdLibFunctionReference rslt_ = newStdLibFunctionReference();
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
     * Returns a new instance of the {@link StringLiteral} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link StringLiteral} class.
     */
    public static StringLiteral newStringLiteral() {
        return ChiFactory.eINSTANCE.createStringLiteral();
    }

    /**
     * Returns a new instance of the {@link StringLiteral} class.
     *
     * @param position The "position" of the new "StringLiteral". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "StringLiteral". Multiplicity [0..1]. May be {@code null} to skip setting the "type", or to set it later.
     * @param value The "value" of the new "StringLiteral". Multiplicity [1..1]. May be {@code null} to set the "value" later.
     * @return A new instance of the {@link StringLiteral} class.
     */
    public static StringLiteral newStringLiteral(Position position, Type type, String value) {
        StringLiteral rslt_ = newStringLiteral();
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
        return ChiFactory.eINSTANCE.createStringType();
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
     * Returns a new instance of the {@link TimeLiteral} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link TimeLiteral} class.
     */
    public static TimeLiteral newTimeLiteral() {
        return ChiFactory.eINSTANCE.createTimeLiteral();
    }

    /**
     * Returns a new instance of the {@link TimeLiteral} class.
     *
     * @param position The "position" of the new "TimeLiteral". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "TimeLiteral". Multiplicity [0..1]. May be {@code null} to skip setting the "type", or to set it later.
     * @return A new instance of the {@link TimeLiteral} class.
     */
    public static TimeLiteral newTimeLiteral(Position position, Type type) {
        TimeLiteral rslt_ = newTimeLiteral();
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (type != null) {
            rslt_.setType(type);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link TimerType} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link TimerType} class.
     */
    public static TimerType newTimerType() {
        return ChiFactory.eINSTANCE.createTimerType();
    }

    /**
     * Returns a new instance of the {@link TimerType} class.
     *
     * @param position The "position" of the new "TimerType". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link TimerType} class.
     */
    public static TimerType newTimerType(Position position) {
        TimerType rslt_ = newTimerType();
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link TupleExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link TupleExpression} class.
     */
    public static TupleExpression newTupleExpression() {
        return ChiFactory.eINSTANCE.createTupleExpression();
    }

    /**
     * Returns a new instance of the {@link TupleExpression} class.
     *
     * @param fields The "fields" of the new "TupleExpression". Multiplicity [2..*]. May be {@code null} to set the "fields" later.
     * @param position The "position" of the new "TupleExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "TupleExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "type", or to set it later.
     * @return A new instance of the {@link TupleExpression} class.
     */
    public static TupleExpression newTupleExpression(List<Expression> fields, Position position, Type type) {
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
     * Returns a new instance of the {@link TupleField} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link TupleField} class.
     */
    public static TupleField newTupleField() {
        return ChiFactory.eINSTANCE.createTupleField();
    }

    /**
     * Returns a new instance of the {@link TupleField} class.
     *
     * @param name The "name" of the new "TupleField". Multiplicity [0..1]. May be {@code null} to skip setting the "name", or to set it later.
     * @param position The "position" of the new "TupleField". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "TupleField". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @return A new instance of the {@link TupleField} class.
     */
    public static TupleField newTupleField(String name, Position position, Type type) {
        TupleField rslt_ = newTupleField();
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
     * Returns a new instance of the {@link TupleType} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link TupleType} class.
     */
    public static TupleType newTupleType() {
        return ChiFactory.eINSTANCE.createTupleType();
    }

    /**
     * Returns a new instance of the {@link TupleType} class.
     *
     * @param fields The "fields" of the new "TupleType". Multiplicity [2..*]. May be {@code null} to set the "fields" later.
     * @param position The "position" of the new "TupleType". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link TupleType} class.
     */
    public static TupleType newTupleType(List<TupleField> fields, Position position) {
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
     * Returns a new instance of the {@link TypeDeclaration} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link TypeDeclaration} class.
     */
    public static TypeDeclaration newTypeDeclaration() {
        return ChiFactory.eINSTANCE.createTypeDeclaration();
    }

    /**
     * Returns a new instance of the {@link TypeDeclaration} class.
     *
     * @param name The "name" of the new "TypeDeclaration". Multiplicity [1..1]. May be {@code null} to set the "name" later.
     * @param position The "position" of the new "TypeDeclaration". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "TypeDeclaration". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @return A new instance of the {@link TypeDeclaration} class.
     */
    public static TypeDeclaration newTypeDeclaration(String name, Position position, Type type) {
        TypeDeclaration rslt_ = newTypeDeclaration();
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
     * Returns a new instance of the {@link TypeReference} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link TypeReference} class.
     */
    public static TypeReference newTypeReference() {
        return ChiFactory.eINSTANCE.createTypeReference();
    }

    /**
     * Returns a new instance of the {@link TypeReference} class.
     *
     * @param position The "position" of the new "TypeReference". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "TypeReference". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @return A new instance of the {@link TypeReference} class.
     */
    public static TypeReference newTypeReference(Position position, TypeDeclaration type) {
        TypeReference rslt_ = newTypeReference();
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
        return ChiFactory.eINSTANCE.createUnaryExpression();
    }

    /**
     * Returns a new instance of the {@link UnaryExpression} class.
     *
     * @param child The "child" of the new "UnaryExpression". Multiplicity [1..1]. May be {@code null} to set the "child" later.
     * @param op The "op" of the new "UnaryExpression". Multiplicity [1..1]. May be {@code null} to set the "op" later.
     * @param position The "position" of the new "UnaryExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "UnaryExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "type", or to set it later.
     * @return A new instance of the {@link UnaryExpression} class.
     */
    public static UnaryExpression newUnaryExpression(Expression child, UnaryOperators op, Position position, Type type) {
        UnaryExpression rslt_ = newUnaryExpression();
        if (child != null) {
            rslt_.setChild(child);
        }
        if (op != null) {
            rslt_.setOp(op);
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
     * Returns a new instance of the {@link UnresolvedReference} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link UnresolvedReference} class.
     */
    public static UnresolvedReference newUnresolvedReference() {
        return ChiFactory.eINSTANCE.createUnresolvedReference();
    }

    /**
     * Returns a new instance of the {@link UnresolvedReference} class.
     *
     * @param name The "name" of the new "UnresolvedReference". Multiplicity [1..1]. May be {@code null} to set the "name" later.
     * @param position The "position" of the new "UnresolvedReference". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "UnresolvedReference". Multiplicity [0..1]. May be {@code null} to skip setting the "type", or to set it later.
     * @return A new instance of the {@link UnresolvedReference} class.
     */
    public static UnresolvedReference newUnresolvedReference(String name, Position position, Type type) {
        UnresolvedReference rslt_ = newUnresolvedReference();
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
     * Returns a new instance of the {@link UnresolvedType} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link UnresolvedType} class.
     */
    public static UnresolvedType newUnresolvedType() {
        return ChiFactory.eINSTANCE.createUnresolvedType();
    }

    /**
     * Returns a new instance of the {@link UnresolvedType} class.
     *
     * @param name The "name" of the new "UnresolvedType". Multiplicity [1..1]. May be {@code null} to set the "name" later.
     * @param position The "position" of the new "UnresolvedType". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link UnresolvedType} class.
     */
    public static UnresolvedType newUnresolvedType(String name, Position position) {
        UnresolvedType rslt_ = newUnresolvedType();
        if (name != null) {
            rslt_.setName(name);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link Unwind} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link Unwind} class.
     */
    public static Unwind newUnwind() {
        return ChiFactory.eINSTANCE.createUnwind();
    }

    /**
     * Returns a new instance of the {@link Unwind} class.
     *
     * @param position The "position" of the new "Unwind". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param source The "source" of the new "Unwind". Multiplicity [1..1]. May be {@code null} to set the "source" later.
     * @param variables The "variables" of the new "Unwind". Multiplicity [1..*]. May be {@code null} to set the "variables" later.
     * @return A new instance of the {@link Unwind} class.
     */
    public static Unwind newUnwind(Position position, Expression source, List<VariableDeclaration> variables) {
        Unwind rslt_ = newUnwind();
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (source != null) {
            rslt_.setSource(source);
        }
        if (variables != null) {
            rslt_.getVariables().addAll(variables);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link VariableDeclaration} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link VariableDeclaration} class.
     */
    public static VariableDeclaration newVariableDeclaration() {
        return ChiFactory.eINSTANCE.createVariableDeclaration();
    }

    /**
     * Returns a new instance of the {@link VariableDeclaration} class.
     *
     * @param initialValue The "initialValue" of the new "VariableDeclaration". Multiplicity [0..1]. May be {@code null} to skip setting the "initialValue", or to set it later.
     * @param name The "name" of the new "VariableDeclaration". Multiplicity [1..1]. May be {@code null} to set the "name" later.
     * @param parameter The "parameter" of the new "VariableDeclaration". Multiplicity [1..1]. May be {@code null} to set the "parameter" later.
     * @param position The "position" of the new "VariableDeclaration". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "VariableDeclaration". Multiplicity [0..1]. May be {@code null} to skip setting the "type", or to set it later.
     * @return A new instance of the {@link VariableDeclaration} class.
     */
    public static VariableDeclaration newVariableDeclaration(Expression initialValue, String name, Boolean parameter, Position position, Type type) {
        VariableDeclaration rslt_ = newVariableDeclaration();
        if (initialValue != null) {
            rslt_.setInitialValue(initialValue);
        }
        if (name != null) {
            rslt_.setName(name);
        }
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
     * Returns a new instance of the {@link VariableReference} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link VariableReference} class.
     */
    public static VariableReference newVariableReference() {
        return ChiFactory.eINSTANCE.createVariableReference();
    }

    /**
     * Returns a new instance of the {@link VariableReference} class.
     *
     * @param position The "position" of the new "VariableReference". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "VariableReference". Multiplicity [0..1]. May be {@code null} to skip setting the "type", or to set it later.
     * @param variable The "variable" of the new "VariableReference". Multiplicity [1..1]. May be {@code null} to set the "variable" later.
     * @return A new instance of the {@link VariableReference} class.
     */
    public static VariableReference newVariableReference(Position position, Type type, VariableDeclaration variable) {
        VariableReference rslt_ = newVariableReference();
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
     * Returns a new instance of the {@link VoidType} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link VoidType} class.
     */
    public static VoidType newVoidType() {
        return ChiFactory.eINSTANCE.createVoidType();
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
     * Returns a new instance of the {@link WhileStatement} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link WhileStatement} class.
     */
    public static WhileStatement newWhileStatement() {
        return ChiFactory.eINSTANCE.createWhileStatement();
    }

    /**
     * Returns a new instance of the {@link WhileStatement} class.
     *
     * @param body The "body" of the new "WhileStatement". Multiplicity [1..*]. May be {@code null} to set the "body" later.
     * @param condition The "condition" of the new "WhileStatement". Multiplicity [1..1]. May be {@code null} to set the "condition" later.
     * @param position The "position" of the new "WhileStatement". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link WhileStatement} class.
     */
    public static WhileStatement newWhileStatement(List<Statement> body, Expression condition, Position position) {
        WhileStatement rslt_ = newWhileStatement();
        if (body != null) {
            rslt_.getBody().addAll(body);
        }
        if (condition != null) {
            rslt_.setCondition(condition);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link WriteStatement} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link WriteStatement} class.
     */
    public static WriteStatement newWriteStatement() {
        return ChiFactory.eINSTANCE.createWriteStatement();
    }

    /**
     * Returns a new instance of the {@link WriteStatement} class.
     *
     * @param addNewline The "addNewline" of the new "WriteStatement". Multiplicity [1..1]. May be {@code null} to set the "addNewline" later.
     * @param position The "position" of the new "WriteStatement". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param values The "values" of the new "WriteStatement". Multiplicity [1..*]. May be {@code null} to set the "values" later.
     * @return A new instance of the {@link WriteStatement} class.
     */
    public static WriteStatement newWriteStatement(Boolean addNewline, Position position, List<Expression> values) {
        WriteStatement rslt_ = newWriteStatement();
        if (addNewline != null) {
            rslt_.setAddNewline(addNewline);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (values != null) {
            rslt_.getValues().addAll(values);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link XperDeclaration} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link XperDeclaration} class.
     */
    public static XperDeclaration newXperDeclaration() {
        return ChiFactory.eINSTANCE.createXperDeclaration();
    }

    /**
     * Returns a new instance of the {@link XperDeclaration} class.
     *
     * @param name The "name" of the new "XperDeclaration". Multiplicity [1..1]. May be {@code null} to set the "name" later.
     * @param position The "position" of the new "XperDeclaration". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param statements The "statements" of the new "XperDeclaration". Multiplicity [1..*]. May be {@code null} to set the "statements" later.
     * @param variables The "variables" of the new "XperDeclaration". Multiplicity [0..*]. May be {@code null} to skip setting the "variables", or to set it later.
     * @return A new instance of the {@link XperDeclaration} class.
     */
    public static XperDeclaration newXperDeclaration(String name, Position position, List<Statement> statements, List<VariableDeclaration> variables) {
        XperDeclaration rslt_ = newXperDeclaration();
        if (name != null) {
            rslt_.setName(name);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (statements != null) {
            rslt_.getStatements().addAll(statements);
        }
        if (variables != null) {
            rslt_.getVariables().addAll(variables);
        }
        return rslt_;
    }
}
