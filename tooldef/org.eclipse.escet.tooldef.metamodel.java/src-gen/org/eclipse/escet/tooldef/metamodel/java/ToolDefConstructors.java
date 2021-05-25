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

package org.eclipse.escet.tooldef.metamodel.java;

import java.lang.reflect.Method;
import java.util.List;

import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.setext.runtime.Token;
import org.eclipse.escet.tooldef.metamodel.tooldef.Declaration;
import org.eclipse.escet.tooldef.metamodel.tooldef.JavaImport;
import org.eclipse.escet.tooldef.metamodel.tooldef.JavaTool;
import org.eclipse.escet.tooldef.metamodel.tooldef.Script;
import org.eclipse.escet.tooldef.metamodel.tooldef.Tool;
import org.eclipse.escet.tooldef.metamodel.tooldef.ToolDefImport;
import org.eclipse.escet.tooldef.metamodel.tooldef.ToolDefTool;
import org.eclipse.escet.tooldef.metamodel.tooldef.ToolParameter;
import org.eclipse.escet.tooldef.metamodel.tooldef.TooldefFactory;
import org.eclipse.escet.tooldef.metamodel.tooldef.TypeDecl;
import org.eclipse.escet.tooldef.metamodel.tooldef.TypeParam;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.BoolExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.CastExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.DoubleExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.EmptySetMapExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.Expression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ExpressionsFactory;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ListExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.MapEntry;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.MapExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.NullExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.NumberExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ProjectionExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.SetExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.SliceExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.StringExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolArgument;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolInvokeExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolParamExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolRef;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.TupleExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.UnresolvedRefExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.VariableExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.AddressableDecl;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.AssignmentStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.BreakStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.ContinueStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.ElifStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.ExitStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.ForStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.IfStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.ReturnStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.Statement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.StatementsFactory;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.ToolInvokeStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.TupleAddressableDecl;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.Variable;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.VariableAddressableDecl;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.WhileStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.BoolType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.DoubleType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.IntType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.ListType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.LongType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.MapType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.ObjectType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.SetType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.StringType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.ToolDefType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.TupleType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.TypeParamRef;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.TypeRef;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.TypesFactory;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.UnresolvedType;

/**
 * Helper class with static constructor methods for the "tooldef" language.
 */
public class ToolDefConstructors {
    /** Constructor for the {@link ToolDefConstructors} class. */
    private ToolDefConstructors() {
        // Static class.
    }

    /**
     * Returns a new instance of the {@link AssignmentStatement} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link AssignmentStatement} class.
     */
    public static AssignmentStatement newAssignmentStatement() {
        return StatementsFactory.eINSTANCE.createAssignmentStatement();
    }

    /**
     * Returns a new instance of the {@link AssignmentStatement} class.
     *
     * @param addressables The "addressables" of the new "AssignmentStatement". Multiplicity [1..*]. May be {@code null} to set the "addressables" later.
     * @param position The "position" of the new "AssignmentStatement". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param values The "values" of the new "AssignmentStatement". Multiplicity [1..*]. May be {@code null} to set the "values" later.
     * @return A new instance of the {@link AssignmentStatement} class.
     */
    public static AssignmentStatement newAssignmentStatement(List<Expression> addressables, Position position, List<Expression> values) {
        AssignmentStatement rslt_ = newAssignmentStatement();
        if (addressables != null) {
            rslt_.getAddressables().addAll(addressables);
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
    public static BoolExpression newBoolExpression(Position position, ToolDefType type, Boolean value) {
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
     * @param nullable The "nullable" of the new "BoolType". Multiplicity [1..1]. May be {@code null} to set the "nullable" later.
     * @param position The "position" of the new "BoolType". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link BoolType} class.
     */
    public static BoolType newBoolType(Boolean nullable, Position position) {
        BoolType rslt_ = newBoolType();
        if (nullable != null) {
            rslt_.setNullable(nullable);
        }
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
        return StatementsFactory.eINSTANCE.createBreakStatement();
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
    public static CastExpression newCastExpression(Expression child, Position position, ToolDefType type) {
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
     * Returns a new instance of the {@link ContinueStatement} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link ContinueStatement} class.
     */
    public static ContinueStatement newContinueStatement() {
        return StatementsFactory.eINSTANCE.createContinueStatement();
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
     * Returns a new instance of the {@link DoubleExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link DoubleExpression} class.
     */
    public static DoubleExpression newDoubleExpression() {
        return ExpressionsFactory.eINSTANCE.createDoubleExpression();
    }

    /**
     * Returns a new instance of the {@link DoubleExpression} class.
     *
     * @param position The "position" of the new "DoubleExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "DoubleExpression". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @param value The "value" of the new "DoubleExpression". Multiplicity [1..1]. May be {@code null} to set the "value" later.
     * @return A new instance of the {@link DoubleExpression} class.
     */
    public static DoubleExpression newDoubleExpression(Position position, ToolDefType type, String value) {
        DoubleExpression rslt_ = newDoubleExpression();
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
     * Returns a new instance of the {@link DoubleType} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link DoubleType} class.
     */
    public static DoubleType newDoubleType() {
        return TypesFactory.eINSTANCE.createDoubleType();
    }

    /**
     * Returns a new instance of the {@link DoubleType} class.
     *
     * @param nullable The "nullable" of the new "DoubleType". Multiplicity [1..1]. May be {@code null} to set the "nullable" later.
     * @param position The "position" of the new "DoubleType". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link DoubleType} class.
     */
    public static DoubleType newDoubleType(Boolean nullable, Position position) {
        DoubleType rslt_ = newDoubleType();
        if (nullable != null) {
            rslt_.setNullable(nullable);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link ElifStatement} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link ElifStatement} class.
     */
    public static ElifStatement newElifStatement() {
        return StatementsFactory.eINSTANCE.createElifStatement();
    }

    /**
     * Returns a new instance of the {@link ElifStatement} class.
     *
     * @param condition The "condition" of the new "ElifStatement". Multiplicity [1..1]. May be {@code null} to set the "condition" later.
     * @param position The "position" of the new "ElifStatement". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param thens The "thens" of the new "ElifStatement". Multiplicity [0..*]. May be {@code null} to skip setting the "thens", or to set it later.
     * @return A new instance of the {@link ElifStatement} class.
     */
    public static ElifStatement newElifStatement(Expression condition, Position position, List<Statement> thens) {
        ElifStatement rslt_ = newElifStatement();
        if (condition != null) {
            rslt_.setCondition(condition);
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
     * Returns a new instance of the {@link EmptySetMapExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link EmptySetMapExpression} class.
     */
    public static EmptySetMapExpression newEmptySetMapExpression() {
        return ExpressionsFactory.eINSTANCE.createEmptySetMapExpression();
    }

    /**
     * Returns a new instance of the {@link EmptySetMapExpression} class.
     *
     * @param position The "position" of the new "EmptySetMapExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "EmptySetMapExpression". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @return A new instance of the {@link EmptySetMapExpression} class.
     */
    public static EmptySetMapExpression newEmptySetMapExpression(Position position, ToolDefType type) {
        EmptySetMapExpression rslt_ = newEmptySetMapExpression();
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (type != null) {
            rslt_.setType(type);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link ExitStatement} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link ExitStatement} class.
     */
    public static ExitStatement newExitStatement() {
        return StatementsFactory.eINSTANCE.createExitStatement();
    }

    /**
     * Returns a new instance of the {@link ExitStatement} class.
     *
     * @param exitCode The "exitCode" of the new "ExitStatement". Multiplicity [0..1]. May be {@code null} to skip setting the "exitCode", or to set it later.
     * @param position The "position" of the new "ExitStatement". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link ExitStatement} class.
     */
    public static ExitStatement newExitStatement(Expression exitCode, Position position) {
        ExitStatement rslt_ = newExitStatement();
        if (exitCode != null) {
            rslt_.setExitCode(exitCode);
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
        return StatementsFactory.eINSTANCE.createForStatement();
    }

    /**
     * Returns a new instance of the {@link ForStatement} class.
     *
     * @param addressables The "addressables" of the new "ForStatement". Multiplicity [1..*]. May be {@code null} to set the "addressables" later.
     * @param position The "position" of the new "ForStatement". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param source The "source" of the new "ForStatement". Multiplicity [1..1]. May be {@code null} to set the "source" later.
     * @param statements The "statements" of the new "ForStatement". Multiplicity [0..*]. May be {@code null} to skip setting the "statements", or to set it later.
     * @return A new instance of the {@link ForStatement} class.
     */
    public static ForStatement newForStatement(List<AddressableDecl> addressables, Position position, Expression source, List<Statement> statements) {
        ForStatement rslt_ = newForStatement();
        if (addressables != null) {
            rslt_.getAddressables().addAll(addressables);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (source != null) {
            rslt_.setSource(source);
        }
        if (statements != null) {
            rslt_.getStatements().addAll(statements);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link IfStatement} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link IfStatement} class.
     */
    public static IfStatement newIfStatement() {
        return StatementsFactory.eINSTANCE.createIfStatement();
    }

    /**
     * Returns a new instance of the {@link IfStatement} class.
     *
     * @param condition The "condition" of the new "IfStatement". Multiplicity [1..1]. May be {@code null} to set the "condition" later.
     * @param elifs The "elifs" of the new "IfStatement". Multiplicity [0..*]. May be {@code null} to skip setting the "elifs", or to set it later.
     * @param elses The "elses" of the new "IfStatement". Multiplicity [0..*]. May be {@code null} to skip setting the "elses", or to set it later.
     * @param position The "position" of the new "IfStatement". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param thens The "thens" of the new "IfStatement". Multiplicity [0..*]. May be {@code null} to skip setting the "thens", or to set it later.
     * @return A new instance of the {@link IfStatement} class.
     */
    public static IfStatement newIfStatement(Expression condition, List<ElifStatement> elifs, List<Statement> elses, Position position, List<Statement> thens) {
        IfStatement rslt_ = newIfStatement();
        if (condition != null) {
            rslt_.setCondition(condition);
        }
        if (elifs != null) {
            rslt_.getElifs().addAll(elifs);
        }
        if (elses != null) {
            rslt_.getElses().addAll(elses);
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
     * @param nullable The "nullable" of the new "IntType". Multiplicity [1..1]. May be {@code null} to set the "nullable" later.
     * @param position The "position" of the new "IntType". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link IntType} class.
     */
    public static IntType newIntType(Boolean nullable, Position position) {
        IntType rslt_ = newIntType();
        if (nullable != null) {
            rslt_.setNullable(nullable);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link JavaImport} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link JavaImport} class.
     */
    public static JavaImport newJavaImport() {
        return TooldefFactory.eINSTANCE.createJavaImport();
    }

    /**
     * Returns a new instance of the {@link JavaImport} class.
     *
     * @param asName The "asName" of the new "JavaImport". Multiplicity [0..1]. May be {@code null} to skip setting the "asName", or to set it later.
     * @param methodName The "methodName" of the new "JavaImport". Multiplicity [1..1]. May be {@code null} to set the "methodName" later.
     * @param pluginName The "pluginName" of the new "JavaImport". Multiplicity [0..1]. May be {@code null} to skip setting the "pluginName", or to set it later.
     * @param position The "position" of the new "JavaImport". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link JavaImport} class.
     */
    public static JavaImport newJavaImport(Token asName, Token methodName, Token pluginName, Position position) {
        JavaImport rslt_ = newJavaImport();
        if (asName != null) {
            rslt_.setAsName(asName);
        }
        if (methodName != null) {
            rslt_.setMethodName(methodName);
        }
        if (pluginName != null) {
            rslt_.setPluginName(pluginName);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link JavaTool} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link JavaTool} class.
     */
    public static JavaTool newJavaTool() {
        return TooldefFactory.eINSTANCE.createJavaTool();
    }

    /**
     * Returns a new instance of the {@link JavaTool} class.
     *
     * @param method The "method" of the new "JavaTool". Multiplicity [1..1]. May be {@code null} to set the "method" later.
     * @param methodName The "methodName" of the new "JavaTool". Multiplicity [1..1]. May be {@code null} to set the "methodName" later.
     * @param name The "name" of the new "JavaTool". Multiplicity [1..1]. May be {@code null} to set the "name" later.
     * @param parameters The "parameters" of the new "JavaTool". Multiplicity [0..*]. May be {@code null} to skip setting the "parameters", or to set it later.
     * @param pluginName The "pluginName" of the new "JavaTool". Multiplicity [0..1]. May be {@code null} to skip setting the "pluginName", or to set it later.
     * @param position The "position" of the new "JavaTool". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param returnTypes The "returnTypes" of the new "JavaTool". Multiplicity [0..*]. May be {@code null} to skip setting the "returnTypes", or to set it later.
     * @param typeParams The "typeParams" of the new "JavaTool". Multiplicity [0..*]. May be {@code null} to skip setting the "typeParams", or to set it later.
     * @return A new instance of the {@link JavaTool} class.
     */
    public static JavaTool newJavaTool(Method method, String methodName, String name, List<ToolParameter> parameters, String pluginName, Position position, List<ToolDefType> returnTypes, List<TypeParam> typeParams) {
        JavaTool rslt_ = newJavaTool();
        if (method != null) {
            rslt_.setMethod(method);
        }
        if (methodName != null) {
            rslt_.setMethodName(methodName);
        }
        if (name != null) {
            rslt_.setName(name);
        }
        if (parameters != null) {
            rslt_.getParameters().addAll(parameters);
        }
        if (pluginName != null) {
            rslt_.setPluginName(pluginName);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (returnTypes != null) {
            rslt_.getReturnTypes().addAll(returnTypes);
        }
        if (typeParams != null) {
            rslt_.getTypeParams().addAll(typeParams);
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
    public static ListExpression newListExpression(List<Expression> elements, Position position, ToolDefType type) {
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
     * @param elemType The "elemType" of the new "ListType". Multiplicity [1..1]. May be {@code null} to set the "elemType" later.
     * @param nullable The "nullable" of the new "ListType". Multiplicity [1..1]. May be {@code null} to set the "nullable" later.
     * @param position The "position" of the new "ListType". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link ListType} class.
     */
    public static ListType newListType(ToolDefType elemType, Boolean nullable, Position position) {
        ListType rslt_ = newListType();
        if (elemType != null) {
            rslt_.setElemType(elemType);
        }
        if (nullable != null) {
            rslt_.setNullable(nullable);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link LongType} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link LongType} class.
     */
    public static LongType newLongType() {
        return TypesFactory.eINSTANCE.createLongType();
    }

    /**
     * Returns a new instance of the {@link LongType} class.
     *
     * @param nullable The "nullable" of the new "LongType". Multiplicity [1..1]. May be {@code null} to set the "nullable" later.
     * @param position The "position" of the new "LongType". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link LongType} class.
     */
    public static LongType newLongType(Boolean nullable, Position position) {
        LongType rslt_ = newLongType();
        if (nullable != null) {
            rslt_.setNullable(nullable);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link MapEntry} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link MapEntry} class.
     */
    public static MapEntry newMapEntry() {
        return ExpressionsFactory.eINSTANCE.createMapEntry();
    }

    /**
     * Returns a new instance of the {@link MapEntry} class.
     *
     * @param key The "key" of the new "MapEntry". Multiplicity [1..1]. May be {@code null} to set the "key" later.
     * @param position The "position" of the new "MapEntry". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param value The "value" of the new "MapEntry". Multiplicity [1..1]. May be {@code null} to set the "value" later.
     * @return A new instance of the {@link MapEntry} class.
     */
    public static MapEntry newMapEntry(Expression key, Position position, Expression value) {
        MapEntry rslt_ = newMapEntry();
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
     * Returns a new instance of the {@link MapExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link MapExpression} class.
     */
    public static MapExpression newMapExpression() {
        return ExpressionsFactory.eINSTANCE.createMapExpression();
    }

    /**
     * Returns a new instance of the {@link MapExpression} class.
     *
     * @param entries The "entries" of the new "MapExpression". Multiplicity [0..*]. May be {@code null} to skip setting the "entries", or to set it later.
     * @param position The "position" of the new "MapExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "MapExpression". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @return A new instance of the {@link MapExpression} class.
     */
    public static MapExpression newMapExpression(List<MapEntry> entries, Position position, ToolDefType type) {
        MapExpression rslt_ = newMapExpression();
        if (entries != null) {
            rslt_.getEntries().addAll(entries);
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
     * Returns a new instance of the {@link MapType} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link MapType} class.
     */
    public static MapType newMapType() {
        return TypesFactory.eINSTANCE.createMapType();
    }

    /**
     * Returns a new instance of the {@link MapType} class.
     *
     * @param keyType The "keyType" of the new "MapType". Multiplicity [1..1]. May be {@code null} to set the "keyType" later.
     * @param nullable The "nullable" of the new "MapType". Multiplicity [1..1]. May be {@code null} to set the "nullable" later.
     * @param position The "position" of the new "MapType". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param valueType The "valueType" of the new "MapType". Multiplicity [1..1]. May be {@code null} to set the "valueType" later.
     * @return A new instance of the {@link MapType} class.
     */
    public static MapType newMapType(ToolDefType keyType, Boolean nullable, Position position, ToolDefType valueType) {
        MapType rslt_ = newMapType();
        if (keyType != null) {
            rslt_.setKeyType(keyType);
        }
        if (nullable != null) {
            rslt_.setNullable(nullable);
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
     * Returns a new instance of the {@link NullExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link NullExpression} class.
     */
    public static NullExpression newNullExpression() {
        return ExpressionsFactory.eINSTANCE.createNullExpression();
    }

    /**
     * Returns a new instance of the {@link NullExpression} class.
     *
     * @param position The "position" of the new "NullExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "NullExpression". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @return A new instance of the {@link NullExpression} class.
     */
    public static NullExpression newNullExpression(Position position, ToolDefType type) {
        NullExpression rslt_ = newNullExpression();
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (type != null) {
            rslt_.setType(type);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link NumberExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link NumberExpression} class.
     */
    public static NumberExpression newNumberExpression() {
        return ExpressionsFactory.eINSTANCE.createNumberExpression();
    }

    /**
     * Returns a new instance of the {@link NumberExpression} class.
     *
     * @param position The "position" of the new "NumberExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "NumberExpression". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @param value The "value" of the new "NumberExpression". Multiplicity [1..1]. May be {@code null} to set the "value" later.
     * @return A new instance of the {@link NumberExpression} class.
     */
    public static NumberExpression newNumberExpression(Position position, ToolDefType type, String value) {
        NumberExpression rslt_ = newNumberExpression();
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
     * Returns a new instance of the {@link ObjectType} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link ObjectType} class.
     */
    public static ObjectType newObjectType() {
        return TypesFactory.eINSTANCE.createObjectType();
    }

    /**
     * Returns a new instance of the {@link ObjectType} class.
     *
     * @param nullable The "nullable" of the new "ObjectType". Multiplicity [1..1]. May be {@code null} to set the "nullable" later.
     * @param position The "position" of the new "ObjectType". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link ObjectType} class.
     */
    public static ObjectType newObjectType(Boolean nullable, Position position) {
        ObjectType rslt_ = newObjectType();
        if (nullable != null) {
            rslt_.setNullable(nullable);
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
    public static ProjectionExpression newProjectionExpression(Expression child, Expression index, Position position, ToolDefType type) {
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
     * Returns a new instance of the {@link ReturnStatement} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link ReturnStatement} class.
     */
    public static ReturnStatement newReturnStatement() {
        return StatementsFactory.eINSTANCE.createReturnStatement();
    }

    /**
     * Returns a new instance of the {@link ReturnStatement} class.
     *
     * @param position The "position" of the new "ReturnStatement". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param values The "values" of the new "ReturnStatement". Multiplicity [0..*]. May be {@code null} to skip setting the "values", or to set it later.
     * @return A new instance of the {@link ReturnStatement} class.
     */
    public static ReturnStatement newReturnStatement(Position position, List<Expression> values) {
        ReturnStatement rslt_ = newReturnStatement();
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (values != null) {
            rslt_.getValues().addAll(values);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link Script} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link Script} class.
     */
    public static Script newScript() {
        return TooldefFactory.eINSTANCE.createScript();
    }

    /**
     * Returns a new instance of the {@link Script} class.
     *
     * @param declarations The "declarations" of the new "Script". Multiplicity [0..*]. May be {@code null} to skip setting the "declarations", or to set it later.
     * @param name The "name" of the new "Script". Multiplicity [0..1]. May be {@code null} to skip setting the "name", or to set it later.
     * @param position The "position" of the new "Script". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link Script} class.
     */
    public static Script newScript(List<Declaration> declarations, String name, Position position) {
        Script rslt_ = newScript();
        if (declarations != null) {
            rslt_.getDeclarations().addAll(declarations);
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
    public static SetExpression newSetExpression(List<Expression> elements, Position position, ToolDefType type) {
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
     * @param elemType The "elemType" of the new "SetType". Multiplicity [1..1]. May be {@code null} to set the "elemType" later.
     * @param nullable The "nullable" of the new "SetType". Multiplicity [1..1]. May be {@code null} to set the "nullable" later.
     * @param position The "position" of the new "SetType". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link SetType} class.
     */
    public static SetType newSetType(ToolDefType elemType, Boolean nullable, Position position) {
        SetType rslt_ = newSetType();
        if (elemType != null) {
            rslt_.setElemType(elemType);
        }
        if (nullable != null) {
            rslt_.setNullable(nullable);
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
    public static SliceExpression newSliceExpression(Expression begin, Expression child, Expression end, Position position, ToolDefType type) {
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
    public static StringExpression newStringExpression(Position position, ToolDefType type, String value) {
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
     * @param nullable The "nullable" of the new "StringType". Multiplicity [1..1]. May be {@code null} to set the "nullable" later.
     * @param position The "position" of the new "StringType". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link StringType} class.
     */
    public static StringType newStringType(Boolean nullable, Position position) {
        StringType rslt_ = newStringType();
        if (nullable != null) {
            rslt_.setNullable(nullable);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link ToolArgument} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link ToolArgument} class.
     */
    public static ToolArgument newToolArgument() {
        return ExpressionsFactory.eINSTANCE.createToolArgument();
    }

    /**
     * Returns a new instance of the {@link ToolArgument} class.
     *
     * @param name The "name" of the new "ToolArgument". Multiplicity [1..1]. May be {@code null} to set the "name" later.
     * @param position The "position" of the new "ToolArgument". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param value The "value" of the new "ToolArgument". Multiplicity [1..1]. May be {@code null} to set the "value" later.
     * @return A new instance of the {@link ToolArgument} class.
     */
    public static ToolArgument newToolArgument(String name, Position position, Expression value) {
        ToolArgument rslt_ = newToolArgument();
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
     * Returns a new instance of the {@link ToolDefImport} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link ToolDefImport} class.
     */
    public static ToolDefImport newToolDefImport() {
        return TooldefFactory.eINSTANCE.createToolDefImport();
    }

    /**
     * Returns a new instance of the {@link ToolDefImport} class.
     *
     * @param asName The "asName" of the new "ToolDefImport". Multiplicity [0..1]. May be {@code null} to skip setting the "asName", or to set it later.
     * @param origName The "origName" of the new "ToolDefImport". Multiplicity [0..1]. May be {@code null} to skip setting the "origName", or to set it later.
     * @param position The "position" of the new "ToolDefImport". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param source The "source" of the new "ToolDefImport". Multiplicity [1..1]. May be {@code null} to set the "source" later.
     * @return A new instance of the {@link ToolDefImport} class.
     */
    public static ToolDefImport newToolDefImport(Token asName, Token origName, Position position, Token source) {
        ToolDefImport rslt_ = newToolDefImport();
        if (asName != null) {
            rslt_.setAsName(asName);
        }
        if (origName != null) {
            rslt_.setOrigName(origName);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (source != null) {
            rslt_.setSource(source);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link ToolDefTool} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link ToolDefTool} class.
     */
    public static ToolDefTool newToolDefTool() {
        return TooldefFactory.eINSTANCE.createToolDefTool();
    }

    /**
     * Returns a new instance of the {@link ToolDefTool} class.
     *
     * @param name The "name" of the new "ToolDefTool". Multiplicity [1..1]. May be {@code null} to set the "name" later.
     * @param parameters The "parameters" of the new "ToolDefTool". Multiplicity [0..*]. May be {@code null} to skip setting the "parameters", or to set it later.
     * @param position The "position" of the new "ToolDefTool". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param returnTypes The "returnTypes" of the new "ToolDefTool". Multiplicity [0..*]. May be {@code null} to skip setting the "returnTypes", or to set it later.
     * @param statements The "statements" of the new "ToolDefTool". Multiplicity [0..*]. May be {@code null} to skip setting the "statements", or to set it later.
     * @param typeParams The "typeParams" of the new "ToolDefTool". Multiplicity [0..*]. May be {@code null} to skip setting the "typeParams", or to set it later.
     * @return A new instance of the {@link ToolDefTool} class.
     */
    public static ToolDefTool newToolDefTool(String name, List<ToolParameter> parameters, Position position, List<ToolDefType> returnTypes, List<Statement> statements, List<TypeParam> typeParams) {
        ToolDefTool rslt_ = newToolDefTool();
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
        if (typeParams != null) {
            rslt_.getTypeParams().addAll(typeParams);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link ToolInvokeExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link ToolInvokeExpression} class.
     */
    public static ToolInvokeExpression newToolInvokeExpression() {
        return ExpressionsFactory.eINSTANCE.createToolInvokeExpression();
    }

    /**
     * Returns a new instance of the {@link ToolInvokeExpression} class.
     *
     * @param arguments The "arguments" of the new "ToolInvokeExpression". Multiplicity [0..*]. May be {@code null} to skip setting the "arguments", or to set it later.
     * @param position The "position" of the new "ToolInvokeExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param tool The "tool" of the new "ToolInvokeExpression". Multiplicity [1..1]. May be {@code null} to set the "tool" later.
     * @param type The "type" of the new "ToolInvokeExpression". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @return A new instance of the {@link ToolInvokeExpression} class.
     */
    public static ToolInvokeExpression newToolInvokeExpression(List<ToolArgument> arguments, Position position, ToolRef tool, ToolDefType type) {
        ToolInvokeExpression rslt_ = newToolInvokeExpression();
        if (arguments != null) {
            rslt_.getArguments().addAll(arguments);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (tool != null) {
            rslt_.setTool(tool);
        }
        if (type != null) {
            rslt_.setType(type);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link ToolInvokeStatement} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link ToolInvokeStatement} class.
     */
    public static ToolInvokeStatement newToolInvokeStatement() {
        return StatementsFactory.eINSTANCE.createToolInvokeStatement();
    }

    /**
     * Returns a new instance of the {@link ToolInvokeStatement} class.
     *
     * @param invocation The "invocation" of the new "ToolInvokeStatement". Multiplicity [1..1]. May be {@code null} to set the "invocation" later.
     * @param position The "position" of the new "ToolInvokeStatement". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link ToolInvokeStatement} class.
     */
    public static ToolInvokeStatement newToolInvokeStatement(ToolInvokeExpression invocation, Position position) {
        ToolInvokeStatement rslt_ = newToolInvokeStatement();
        if (invocation != null) {
            rslt_.setInvocation(invocation);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link ToolParamExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link ToolParamExpression} class.
     */
    public static ToolParamExpression newToolParamExpression() {
        return ExpressionsFactory.eINSTANCE.createToolParamExpression();
    }

    /**
     * Returns a new instance of the {@link ToolParamExpression} class.
     *
     * @param param The "param" of the new "ToolParamExpression". Multiplicity [1..1]. May be {@code null} to set the "param" later.
     * @param position The "position" of the new "ToolParamExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "ToolParamExpression". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @return A new instance of the {@link ToolParamExpression} class.
     */
    public static ToolParamExpression newToolParamExpression(ToolParameter param, Position position, ToolDefType type) {
        ToolParamExpression rslt_ = newToolParamExpression();
        if (param != null) {
            rslt_.setParam(param);
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
     * Returns a new instance of the {@link ToolParameter} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link ToolParameter} class.
     */
    public static ToolParameter newToolParameter() {
        return TooldefFactory.eINSTANCE.createToolParameter();
    }

    /**
     * Returns a new instance of the {@link ToolParameter} class.
     *
     * @param name The "name" of the new "ToolParameter". Multiplicity [1..1]. May be {@code null} to set the "name" later.
     * @param position The "position" of the new "ToolParameter". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "ToolParameter". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @param value The "value" of the new "ToolParameter". Multiplicity [0..1]. May be {@code null} to skip setting the "value", or to set it later.
     * @param variadic The "variadic" of the new "ToolParameter". Multiplicity [1..1]. May be {@code null} to set the "variadic" later.
     * @return A new instance of the {@link ToolParameter} class.
     */
    public static ToolParameter newToolParameter(String name, Position position, ToolDefType type, Expression value, Boolean variadic) {
        ToolParameter rslt_ = newToolParameter();
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
        if (variadic != null) {
            rslt_.setVariadic(variadic);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link ToolRef} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link ToolRef} class.
     */
    public static ToolRef newToolRef() {
        return ExpressionsFactory.eINSTANCE.createToolRef();
    }

    /**
     * Returns a new instance of the {@link ToolRef} class.
     *
     * @param builtin The "builtin" of the new "ToolRef". Multiplicity [1..1]. May be {@code null} to set the "builtin" later.
     * @param name The "name" of the new "ToolRef". Multiplicity [1..1]. May be {@code null} to set the "name" later.
     * @param position The "position" of the new "ToolRef". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param tool The "tool" of the new "ToolRef". Multiplicity [0..1]. May be {@code null} to skip setting the "tool", or to set it later.
     * @return A new instance of the {@link ToolRef} class.
     */
    public static ToolRef newToolRef(Boolean builtin, String name, Position position, Tool tool) {
        ToolRef rslt_ = newToolRef();
        if (builtin != null) {
            rslt_.setBuiltin(builtin);
        }
        if (name != null) {
            rslt_.setName(name);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (tool != null) {
            rslt_.setTool(tool);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link TupleAddressableDecl} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link TupleAddressableDecl} class.
     */
    public static TupleAddressableDecl newTupleAddressableDecl() {
        return StatementsFactory.eINSTANCE.createTupleAddressableDecl();
    }

    /**
     * Returns a new instance of the {@link TupleAddressableDecl} class.
     *
     * @param elements The "elements" of the new "TupleAddressableDecl". Multiplicity [2..*]. May be {@code null} to set the "elements" later.
     * @param position The "position" of the new "TupleAddressableDecl". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link TupleAddressableDecl} class.
     */
    public static TupleAddressableDecl newTupleAddressableDecl(List<AddressableDecl> elements, Position position) {
        TupleAddressableDecl rslt_ = newTupleAddressableDecl();
        if (elements != null) {
            rslt_.getElements().addAll(elements);
        }
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
        return ExpressionsFactory.eINSTANCE.createTupleExpression();
    }

    /**
     * Returns a new instance of the {@link TupleExpression} class.
     *
     * @param elements The "elements" of the new "TupleExpression". Multiplicity [0..*]. May be {@code null} to skip setting the "elements", or to set it later.
     * @param position The "position" of the new "TupleExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "TupleExpression". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @return A new instance of the {@link TupleExpression} class.
     */
    public static TupleExpression newTupleExpression(List<Expression> elements, Position position, ToolDefType type) {
        TupleExpression rslt_ = newTupleExpression();
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
     * @param nullable The "nullable" of the new "TupleType". Multiplicity [1..1]. May be {@code null} to set the "nullable" later.
     * @param position The "position" of the new "TupleType". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link TupleType} class.
     */
    public static TupleType newTupleType(List<ToolDefType> fields, Boolean nullable, Position position) {
        TupleType rslt_ = newTupleType();
        if (fields != null) {
            rslt_.getFields().addAll(fields);
        }
        if (nullable != null) {
            rslt_.setNullable(nullable);
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
        return TooldefFactory.eINSTANCE.createTypeDecl();
    }

    /**
     * Returns a new instance of the {@link TypeDecl} class.
     *
     * @param name The "name" of the new "TypeDecl". Multiplicity [1..1]. May be {@code null} to set the "name" later.
     * @param position The "position" of the new "TypeDecl". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "TypeDecl". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @return A new instance of the {@link TypeDecl} class.
     */
    public static TypeDecl newTypeDecl(String name, Position position, ToolDefType type) {
        TypeDecl rslt_ = newTypeDecl();
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
     * Returns a new instance of the {@link TypeParam} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link TypeParam} class.
     */
    public static TypeParam newTypeParam() {
        return TooldefFactory.eINSTANCE.createTypeParam();
    }

    /**
     * Returns a new instance of the {@link TypeParam} class.
     *
     * @param name The "name" of the new "TypeParam". Multiplicity [1..1]. May be {@code null} to set the "name" later.
     * @param position The "position" of the new "TypeParam". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link TypeParam} class.
     */
    public static TypeParam newTypeParam(String name, Position position) {
        TypeParam rslt_ = newTypeParam();
        if (name != null) {
            rslt_.setName(name);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link TypeParamRef} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link TypeParamRef} class.
     */
    public static TypeParamRef newTypeParamRef() {
        return TypesFactory.eINSTANCE.createTypeParamRef();
    }

    /**
     * Returns a new instance of the {@link TypeParamRef} class.
     *
     * @param nullable The "nullable" of the new "TypeParamRef". Multiplicity [1..1]. May be {@code null} to set the "nullable" later.
     * @param position The "position" of the new "TypeParamRef". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "TypeParamRef". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @return A new instance of the {@link TypeParamRef} class.
     */
    public static TypeParamRef newTypeParamRef(Boolean nullable, Position position, TypeParam type) {
        TypeParamRef rslt_ = newTypeParamRef();
        if (nullable != null) {
            rslt_.setNullable(nullable);
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
     * @param nullable The "nullable" of the new "TypeRef". Multiplicity [1..1]. May be {@code null} to set the "nullable" later.
     * @param position The "position" of the new "TypeRef". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "TypeRef". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @return A new instance of the {@link TypeRef} class.
     */
    public static TypeRef newTypeRef(Boolean nullable, Position position, TypeDecl type) {
        TypeRef rslt_ = newTypeRef();
        if (nullable != null) {
            rslt_.setNullable(nullable);
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
     * Returns a new instance of the {@link UnresolvedRefExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link UnresolvedRefExpression} class.
     */
    public static UnresolvedRefExpression newUnresolvedRefExpression() {
        return ExpressionsFactory.eINSTANCE.createUnresolvedRefExpression();
    }

    /**
     * Returns a new instance of the {@link UnresolvedRefExpression} class.
     *
     * @param name The "name" of the new "UnresolvedRefExpression". Multiplicity [1..1]. May be {@code null} to set the "name" later.
     * @param position The "position" of the new "UnresolvedRefExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "UnresolvedRefExpression". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @return A new instance of the {@link UnresolvedRefExpression} class.
     */
    public static UnresolvedRefExpression newUnresolvedRefExpression(String name, Position position, ToolDefType type) {
        UnresolvedRefExpression rslt_ = newUnresolvedRefExpression();
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
        return TypesFactory.eINSTANCE.createUnresolvedType();
    }

    /**
     * Returns a new instance of the {@link UnresolvedType} class.
     *
     * @param name The "name" of the new "UnresolvedType". Multiplicity [1..1]. May be {@code null} to set the "name" later.
     * @param nullable The "nullable" of the new "UnresolvedType". Multiplicity [1..1]. May be {@code null} to set the "nullable" later.
     * @param position The "position" of the new "UnresolvedType". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @return A new instance of the {@link UnresolvedType} class.
     */
    public static UnresolvedType newUnresolvedType(String name, Boolean nullable, Position position) {
        UnresolvedType rslt_ = newUnresolvedType();
        if (name != null) {
            rslt_.setName(name);
        }
        if (nullable != null) {
            rslt_.setNullable(nullable);
        }
        if (position != null) {
            rslt_.setPosition(position);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link Variable} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link Variable} class.
     */
    public static Variable newVariable() {
        return StatementsFactory.eINSTANCE.createVariable();
    }

    /**
     * Returns a new instance of the {@link Variable} class.
     *
     * @param name The "name" of the new "Variable". Multiplicity [1..1]. May be {@code null} to set the "name" later.
     * @param position The "position" of the new "Variable". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "Variable". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @param value The "value" of the new "Variable". Multiplicity [0..1]. May be {@code null} to skip setting the "value", or to set it later.
     * @return A new instance of the {@link Variable} class.
     */
    public static Variable newVariable(String name, Position position, ToolDefType type, Expression value) {
        Variable rslt_ = newVariable();
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
     * Returns a new instance of the {@link VariableAddressableDecl} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link VariableAddressableDecl} class.
     */
    public static VariableAddressableDecl newVariableAddressableDecl() {
        return StatementsFactory.eINSTANCE.createVariableAddressableDecl();
    }

    /**
     * Returns a new instance of the {@link VariableAddressableDecl} class.
     *
     * @param position The "position" of the new "VariableAddressableDecl". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param variable The "variable" of the new "VariableAddressableDecl". Multiplicity [1..1]. May be {@code null} to set the "variable" later.
     * @return A new instance of the {@link VariableAddressableDecl} class.
     */
    public static VariableAddressableDecl newVariableAddressableDecl(Position position, Variable variable) {
        VariableAddressableDecl rslt_ = newVariableAddressableDecl();
        if (position != null) {
            rslt_.setPosition(position);
        }
        if (variable != null) {
            rslt_.setVariable(variable);
        }
        return rslt_;
    }

    /**
     * Returns a new instance of the {@link VariableExpression} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link VariableExpression} class.
     */
    public static VariableExpression newVariableExpression() {
        return ExpressionsFactory.eINSTANCE.createVariableExpression();
    }

    /**
     * Returns a new instance of the {@link VariableExpression} class.
     *
     * @param position The "position" of the new "VariableExpression". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param type The "type" of the new "VariableExpression". Multiplicity [1..1]. May be {@code null} to set the "type" later.
     * @param variable The "variable" of the new "VariableExpression". Multiplicity [1..1]. May be {@code null} to set the "variable" later.
     * @return A new instance of the {@link VariableExpression} class.
     */
    public static VariableExpression newVariableExpression(Position position, ToolDefType type, Variable variable) {
        VariableExpression rslt_ = newVariableExpression();
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
     * Returns a new instance of the {@link WhileStatement} class. This constructs a new object, without setting any of its features.
     *
     * @return A new instance of the {@link WhileStatement} class.
     */
    public static WhileStatement newWhileStatement() {
        return StatementsFactory.eINSTANCE.createWhileStatement();
    }

    /**
     * Returns a new instance of the {@link WhileStatement} class.
     *
     * @param condition The "condition" of the new "WhileStatement". Multiplicity [1..1]. May be {@code null} to set the "condition" later.
     * @param position The "position" of the new "WhileStatement". Multiplicity [0..1]. May be {@code null} to skip setting the "position", or to set it later.
     * @param statements The "statements" of the new "WhileStatement". Multiplicity [0..*]. May be {@code null} to skip setting the "statements", or to set it later.
     * @return A new instance of the {@link WhileStatement} class.
     */
    public static WhileStatement newWhileStatement(Expression condition, Position position, List<Statement> statements) {
        WhileStatement rslt_ = newWhileStatement();
        if (condition != null) {
            rslt_.setCondition(condition);
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
