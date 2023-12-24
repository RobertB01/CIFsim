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
 *
 * @param <T> The type of the extra argument provided to the walking, crawling
 *     and processing methods.
 */
public abstract class CifWithArgWalker<T> {
    /**
     * Walking function for the {@link AlgParameter} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkAlgParameter(AlgParameter obj, T arg) {
        precrawlAlgParameter(obj, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        AlgVariable _variable = obj.getVariable();
        walkAlgVariable(_variable, arg);
        postcrawlAlgParameter(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link AlgParameter} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlAlgParameter(AlgParameter obj, T arg) {
        precrawlParameter(obj, arg);
        preprocessAlgParameter(obj, arg);
    }

    /**
     * Post-crawling function for the {@link AlgParameter} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlAlgParameter(AlgParameter obj, T arg) {
        postprocessAlgParameter(obj, arg);
        postcrawlParameter(obj, arg);
    }

    /**
     * Pre-processing function for the {@link AlgParameter} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessAlgParameter(AlgParameter obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link AlgParameter} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessAlgParameter(AlgParameter obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link AlgVariable} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkAlgVariable(AlgVariable obj, T arg) {
        precrawlAlgVariable(obj, arg);
        List<Annotation> _annotations = obj.getAnnotations();
        for (Annotation x: _annotations) {
            walkAnnotation(x, arg);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        CifType _type = obj.getType();
        walkCifType(_type, arg);
        Expression _value = obj.getValue();
        if (_value != null) {
            walkExpression(_value, arg);
        }
        postcrawlAlgVariable(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link AlgVariable} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlAlgVariable(AlgVariable obj, T arg) {
        precrawlDeclaration(obj, arg);
        preprocessAlgVariable(obj, arg);
    }

    /**
     * Post-crawling function for the {@link AlgVariable} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlAlgVariable(AlgVariable obj, T arg) {
        postprocessAlgVariable(obj, arg);
        postcrawlDeclaration(obj, arg);
    }

    /**
     * Pre-processing function for the {@link AlgVariable} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessAlgVariable(AlgVariable obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link AlgVariable} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessAlgVariable(AlgVariable obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link AlgVariableExpression} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkAlgVariableExpression(AlgVariableExpression obj, T arg) {
        precrawlAlgVariableExpression(obj, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        CifType _type = obj.getType();
        walkCifType(_type, arg);
        postcrawlAlgVariableExpression(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link AlgVariableExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlAlgVariableExpression(AlgVariableExpression obj, T arg) {
        precrawlExpression(obj, arg);
        preprocessAlgVariableExpression(obj, arg);
    }

    /**
     * Post-crawling function for the {@link AlgVariableExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlAlgVariableExpression(AlgVariableExpression obj, T arg) {
        postprocessAlgVariableExpression(obj, arg);
        postcrawlExpression(obj, arg);
    }

    /**
     * Pre-processing function for the {@link AlgVariableExpression} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessAlgVariableExpression(AlgVariableExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link AlgVariableExpression} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessAlgVariableExpression(AlgVariableExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link Alphabet} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkAlphabet(Alphabet obj, T arg) {
        precrawlAlphabet(obj, arg);
        List<Expression> _events = obj.getEvents();
        for (Expression x: _events) {
            walkExpression(x, arg);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        postcrawlAlphabet(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link Alphabet} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlAlphabet(Alphabet obj, T arg) {
        precrawlPositionObject(obj, arg);
        preprocessAlphabet(obj, arg);
    }

    /**
     * Post-crawling function for the {@link Alphabet} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlAlphabet(Alphabet obj, T arg) {
        postprocessAlphabet(obj, arg);
        postcrawlPositionObject(obj, arg);
    }

    /**
     * Pre-processing function for the {@link Alphabet} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessAlphabet(Alphabet obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link Alphabet} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessAlphabet(Alphabet obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link AnnotatedObject} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkAnnotatedObject(AnnotatedObject obj, T arg) {
        if (obj instanceof Declaration) {
            walkDeclaration((Declaration)obj, arg);
            return;
        }
        if (obj instanceof Location) {
            walkLocation((Location)obj, arg);
            return;
        }
        String msg = "No redirect; unexpected object type: " + obj;
        throw new IllegalArgumentException(msg);
    }

    /**
     * Pre-crawling function for the {@link AnnotatedObject} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlAnnotatedObject(AnnotatedObject obj, T arg) {
        precrawlPositionObject(obj, arg);
        preprocessAnnotatedObject(obj, arg);
    }

    /**
     * Post-crawling function for the {@link AnnotatedObject} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlAnnotatedObject(AnnotatedObject obj, T arg) {
        postprocessAnnotatedObject(obj, arg);
        postcrawlPositionObject(obj, arg);
    }

    /**
     * Pre-processing function for the {@link AnnotatedObject} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessAnnotatedObject(AnnotatedObject obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link AnnotatedObject} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessAnnotatedObject(AnnotatedObject obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link Annotation} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkAnnotation(Annotation obj, T arg) {
        precrawlAnnotation(obj, arg);
        List<AnnotationArgument> _arguments = obj.getArguments();
        for (AnnotationArgument x: _arguments) {
            walkAnnotationArgument(x, arg);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        postcrawlAnnotation(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link Annotation} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlAnnotation(Annotation obj, T arg) {
        precrawlPositionObject(obj, arg);
        preprocessAnnotation(obj, arg);
    }

    /**
     * Post-crawling function for the {@link Annotation} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlAnnotation(Annotation obj, T arg) {
        postprocessAnnotation(obj, arg);
        postcrawlPositionObject(obj, arg);
    }

    /**
     * Pre-processing function for the {@link Annotation} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessAnnotation(Annotation obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link Annotation} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessAnnotation(Annotation obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link AnnotationArgument} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkAnnotationArgument(AnnotationArgument obj, T arg) {
        precrawlAnnotationArgument(obj, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        Expression _value = obj.getValue();
        walkExpression(_value, arg);
        postcrawlAnnotationArgument(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link AnnotationArgument} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlAnnotationArgument(AnnotationArgument obj, T arg) {
        precrawlPositionObject(obj, arg);
        preprocessAnnotationArgument(obj, arg);
    }

    /**
     * Post-crawling function for the {@link AnnotationArgument} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlAnnotationArgument(AnnotationArgument obj, T arg) {
        postprocessAnnotationArgument(obj, arg);
        postcrawlPositionObject(obj, arg);
    }

    /**
     * Pre-processing function for the {@link AnnotationArgument} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessAnnotationArgument(AnnotationArgument obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link AnnotationArgument} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessAnnotationArgument(AnnotationArgument obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link Assignment} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkAssignment(Assignment obj, T arg) {
        precrawlAssignment(obj, arg);
        Expression _addressable = obj.getAddressable();
        walkExpression(_addressable, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        Expression _value = obj.getValue();
        walkExpression(_value, arg);
        postcrawlAssignment(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link Assignment} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlAssignment(Assignment obj, T arg) {
        precrawlUpdate(obj, arg);
        preprocessAssignment(obj, arg);
    }

    /**
     * Post-crawling function for the {@link Assignment} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlAssignment(Assignment obj, T arg) {
        postprocessAssignment(obj, arg);
        postcrawlUpdate(obj, arg);
    }

    /**
     * Pre-processing function for the {@link Assignment} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessAssignment(Assignment obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link Assignment} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessAssignment(Assignment obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link AssignmentFuncStatement} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkAssignmentFuncStatement(AssignmentFuncStatement obj, T arg) {
        precrawlAssignmentFuncStatement(obj, arg);
        Expression _addressable = obj.getAddressable();
        walkExpression(_addressable, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        Expression _value = obj.getValue();
        walkExpression(_value, arg);
        postcrawlAssignmentFuncStatement(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link AssignmentFuncStatement} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlAssignmentFuncStatement(AssignmentFuncStatement obj, T arg) {
        precrawlFunctionStatement(obj, arg);
        preprocessAssignmentFuncStatement(obj, arg);
    }

    /**
     * Post-crawling function for the {@link AssignmentFuncStatement} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlAssignmentFuncStatement(AssignmentFuncStatement obj, T arg) {
        postprocessAssignmentFuncStatement(obj, arg);
        postcrawlFunctionStatement(obj, arg);
    }

    /**
     * Pre-processing function for the {@link AssignmentFuncStatement} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessAssignmentFuncStatement(AssignmentFuncStatement obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link AssignmentFuncStatement} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessAssignmentFuncStatement(AssignmentFuncStatement obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link Automaton} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkAutomaton(Automaton obj, T arg) {
        precrawlAutomaton(obj, arg);
        Alphabet _alphabet = obj.getAlphabet();
        if (_alphabet != null) {
            walkAlphabet(_alphabet, arg);
        }
        List<Declaration> _declarations = obj.getDeclarations();
        for (Declaration x: _declarations) {
            walkDeclaration(x, arg);
        }
        List<Equation> _equations = obj.getEquations();
        for (Equation x: _equations) {
            walkEquation(x, arg);
        }
        List<Expression> _initials = obj.getInitials();
        for (Expression x: _initials) {
            walkExpression(x, arg);
        }
        List<Invariant> _invariants = obj.getInvariants();
        for (Invariant x: _invariants) {
            walkInvariant(x, arg);
        }
        List<IoDecl> _ioDecls = obj.getIoDecls();
        for (IoDecl x: _ioDecls) {
            walkIoDecl(x, arg);
        }
        List<Location> _locations = obj.getLocations();
        for (Location x: _locations) {
            walkLocation(x, arg);
        }
        List<Expression> _markeds = obj.getMarkeds();
        for (Expression x: _markeds) {
            walkExpression(x, arg);
        }
        Monitors _monitors = obj.getMonitors();
        if (_monitors != null) {
            walkMonitors(_monitors, arg);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        postcrawlAutomaton(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link Automaton} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlAutomaton(Automaton obj, T arg) {
        precrawlComplexComponent(obj, arg);
        preprocessAutomaton(obj, arg);
    }

    /**
     * Post-crawling function for the {@link Automaton} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlAutomaton(Automaton obj, T arg) {
        postprocessAutomaton(obj, arg);
        postcrawlComplexComponent(obj, arg);
    }

    /**
     * Pre-processing function for the {@link Automaton} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessAutomaton(Automaton obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link Automaton} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessAutomaton(Automaton obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link BaseFunctionExpression} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkBaseFunctionExpression(BaseFunctionExpression obj, T arg) {
        if (obj instanceof FunctionExpression) {
            walkFunctionExpression((FunctionExpression)obj, arg);
            return;
        }
        if (obj instanceof StdLibFunctionExpression) {
            walkStdLibFunctionExpression((StdLibFunctionExpression)obj, arg);
            return;
        }
        String msg = "No redirect; unexpected object type: " + obj;
        throw new IllegalArgumentException(msg);
    }

    /**
     * Pre-crawling function for the {@link BaseFunctionExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlBaseFunctionExpression(BaseFunctionExpression obj, T arg) {
        precrawlExpression(obj, arg);
        preprocessBaseFunctionExpression(obj, arg);
    }

    /**
     * Post-crawling function for the {@link BaseFunctionExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlBaseFunctionExpression(BaseFunctionExpression obj, T arg) {
        postprocessBaseFunctionExpression(obj, arg);
        postcrawlExpression(obj, arg);
    }

    /**
     * Pre-processing function for the {@link BaseFunctionExpression} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessBaseFunctionExpression(BaseFunctionExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link BaseFunctionExpression} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessBaseFunctionExpression(BaseFunctionExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link BinaryExpression} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkBinaryExpression(BinaryExpression obj, T arg) {
        precrawlBinaryExpression(obj, arg);
        Expression _left = obj.getLeft();
        walkExpression(_left, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        Expression _right = obj.getRight();
        walkExpression(_right, arg);
        CifType _type = obj.getType();
        walkCifType(_type, arg);
        postcrawlBinaryExpression(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link BinaryExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlBinaryExpression(BinaryExpression obj, T arg) {
        precrawlExpression(obj, arg);
        preprocessBinaryExpression(obj, arg);
    }

    /**
     * Post-crawling function for the {@link BinaryExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlBinaryExpression(BinaryExpression obj, T arg) {
        postprocessBinaryExpression(obj, arg);
        postcrawlExpression(obj, arg);
    }

    /**
     * Pre-processing function for the {@link BinaryExpression} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessBinaryExpression(BinaryExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link BinaryExpression} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessBinaryExpression(BinaryExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link BoolExpression} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkBoolExpression(BoolExpression obj, T arg) {
        precrawlBoolExpression(obj, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        CifType _type = obj.getType();
        walkCifType(_type, arg);
        postcrawlBoolExpression(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link BoolExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlBoolExpression(BoolExpression obj, T arg) {
        precrawlExpression(obj, arg);
        preprocessBoolExpression(obj, arg);
    }

    /**
     * Post-crawling function for the {@link BoolExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlBoolExpression(BoolExpression obj, T arg) {
        postprocessBoolExpression(obj, arg);
        postcrawlExpression(obj, arg);
    }

    /**
     * Pre-processing function for the {@link BoolExpression} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessBoolExpression(BoolExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link BoolExpression} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessBoolExpression(BoolExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link BoolType} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkBoolType(BoolType obj, T arg) {
        precrawlBoolType(obj, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        postcrawlBoolType(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link BoolType} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlBoolType(BoolType obj, T arg) {
        precrawlCifType(obj, arg);
        preprocessBoolType(obj, arg);
    }

    /**
     * Post-crawling function for the {@link BoolType} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlBoolType(BoolType obj, T arg) {
        postprocessBoolType(obj, arg);
        postcrawlCifType(obj, arg);
    }

    /**
     * Pre-processing function for the {@link BoolType} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessBoolType(BoolType obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link BoolType} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessBoolType(BoolType obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link BreakFuncStatement} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkBreakFuncStatement(BreakFuncStatement obj, T arg) {
        precrawlBreakFuncStatement(obj, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        postcrawlBreakFuncStatement(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link BreakFuncStatement} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlBreakFuncStatement(BreakFuncStatement obj, T arg) {
        precrawlFunctionStatement(obj, arg);
        preprocessBreakFuncStatement(obj, arg);
    }

    /**
     * Post-crawling function for the {@link BreakFuncStatement} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlBreakFuncStatement(BreakFuncStatement obj, T arg) {
        postprocessBreakFuncStatement(obj, arg);
        postcrawlFunctionStatement(obj, arg);
    }

    /**
     * Pre-processing function for the {@link BreakFuncStatement} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessBreakFuncStatement(BreakFuncStatement obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link BreakFuncStatement} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessBreakFuncStatement(BreakFuncStatement obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link CastExpression} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkCastExpression(CastExpression obj, T arg) {
        precrawlCastExpression(obj, arg);
        Expression _child = obj.getChild();
        walkExpression(_child, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        CifType _type = obj.getType();
        walkCifType(_type, arg);
        postcrawlCastExpression(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link CastExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlCastExpression(CastExpression obj, T arg) {
        precrawlExpression(obj, arg);
        preprocessCastExpression(obj, arg);
    }

    /**
     * Post-crawling function for the {@link CastExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlCastExpression(CastExpression obj, T arg) {
        postprocessCastExpression(obj, arg);
        postcrawlExpression(obj, arg);
    }

    /**
     * Pre-processing function for the {@link CastExpression} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessCastExpression(CastExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link CastExpression} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessCastExpression(CastExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link CifType} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkCifType(CifType obj, T arg) {
        if (obj instanceof BoolType) {
            walkBoolType((BoolType)obj, arg);
            return;
        }
        if (obj instanceof CompInstWrapType) {
            walkCompInstWrapType((CompInstWrapType)obj, arg);
            return;
        }
        if (obj instanceof CompParamWrapType) {
            walkCompParamWrapType((CompParamWrapType)obj, arg);
            return;
        }
        if (obj instanceof ComponentDefType) {
            walkComponentDefType((ComponentDefType)obj, arg);
            return;
        }
        if (obj instanceof ComponentType) {
            walkComponentType((ComponentType)obj, arg);
            return;
        }
        if (obj instanceof DictType) {
            walkDictType((DictType)obj, arg);
            return;
        }
        if (obj instanceof DistType) {
            walkDistType((DistType)obj, arg);
            return;
        }
        if (obj instanceof EnumType) {
            walkEnumType((EnumType)obj, arg);
            return;
        }
        if (obj instanceof FuncType) {
            walkFuncType((FuncType)obj, arg);
            return;
        }
        if (obj instanceof IntType) {
            walkIntType((IntType)obj, arg);
            return;
        }
        if (obj instanceof ListType) {
            walkListType((ListType)obj, arg);
            return;
        }
        if (obj instanceof RealType) {
            walkRealType((RealType)obj, arg);
            return;
        }
        if (obj instanceof SetType) {
            walkSetType((SetType)obj, arg);
            return;
        }
        if (obj instanceof StringType) {
            walkStringType((StringType)obj, arg);
            return;
        }
        if (obj instanceof TupleType) {
            walkTupleType((TupleType)obj, arg);
            return;
        }
        if (obj instanceof TypeRef) {
            walkTypeRef((TypeRef)obj, arg);
            return;
        }
        if (obj instanceof VoidType) {
            walkVoidType((VoidType)obj, arg);
            return;
        }
        String msg = "No redirect; unexpected object type: " + obj;
        throw new IllegalArgumentException(msg);
    }

    /**
     * Pre-crawling function for the {@link CifType} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlCifType(CifType obj, T arg) {
        precrawlPositionObject(obj, arg);
        preprocessCifType(obj, arg);
    }

    /**
     * Post-crawling function for the {@link CifType} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlCifType(CifType obj, T arg) {
        postprocessCifType(obj, arg);
        postcrawlPositionObject(obj, arg);
    }

    /**
     * Pre-processing function for the {@link CifType} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessCifType(CifType obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link CifType} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessCifType(CifType obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link CompInstWrapExpression} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkCompInstWrapExpression(CompInstWrapExpression obj, T arg) {
        precrawlCompInstWrapExpression(obj, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        Expression _reference = obj.getReference();
        walkExpression(_reference, arg);
        CifType _type = obj.getType();
        walkCifType(_type, arg);
        postcrawlCompInstWrapExpression(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link CompInstWrapExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlCompInstWrapExpression(CompInstWrapExpression obj, T arg) {
        precrawlExpression(obj, arg);
        preprocessCompInstWrapExpression(obj, arg);
    }

    /**
     * Post-crawling function for the {@link CompInstWrapExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlCompInstWrapExpression(CompInstWrapExpression obj, T arg) {
        postprocessCompInstWrapExpression(obj, arg);
        postcrawlExpression(obj, arg);
    }

    /**
     * Pre-processing function for the {@link CompInstWrapExpression} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessCompInstWrapExpression(CompInstWrapExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link CompInstWrapExpression} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessCompInstWrapExpression(CompInstWrapExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link CompInstWrapType} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkCompInstWrapType(CompInstWrapType obj, T arg) {
        precrawlCompInstWrapType(obj, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        CifType _reference = obj.getReference();
        walkCifType(_reference, arg);
        postcrawlCompInstWrapType(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link CompInstWrapType} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlCompInstWrapType(CompInstWrapType obj, T arg) {
        precrawlCifType(obj, arg);
        preprocessCompInstWrapType(obj, arg);
    }

    /**
     * Post-crawling function for the {@link CompInstWrapType} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlCompInstWrapType(CompInstWrapType obj, T arg) {
        postprocessCompInstWrapType(obj, arg);
        postcrawlCifType(obj, arg);
    }

    /**
     * Pre-processing function for the {@link CompInstWrapType} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessCompInstWrapType(CompInstWrapType obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link CompInstWrapType} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessCompInstWrapType(CompInstWrapType obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link CompParamExpression} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkCompParamExpression(CompParamExpression obj, T arg) {
        precrawlCompParamExpression(obj, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        CifType _type = obj.getType();
        walkCifType(_type, arg);
        postcrawlCompParamExpression(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link CompParamExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlCompParamExpression(CompParamExpression obj, T arg) {
        precrawlExpression(obj, arg);
        preprocessCompParamExpression(obj, arg);
    }

    /**
     * Post-crawling function for the {@link CompParamExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlCompParamExpression(CompParamExpression obj, T arg) {
        postprocessCompParamExpression(obj, arg);
        postcrawlExpression(obj, arg);
    }

    /**
     * Pre-processing function for the {@link CompParamExpression} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessCompParamExpression(CompParamExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link CompParamExpression} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessCompParamExpression(CompParamExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link CompParamWrapExpression} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkCompParamWrapExpression(CompParamWrapExpression obj, T arg) {
        precrawlCompParamWrapExpression(obj, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        Expression _reference = obj.getReference();
        walkExpression(_reference, arg);
        CifType _type = obj.getType();
        walkCifType(_type, arg);
        postcrawlCompParamWrapExpression(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link CompParamWrapExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlCompParamWrapExpression(CompParamWrapExpression obj, T arg) {
        precrawlExpression(obj, arg);
        preprocessCompParamWrapExpression(obj, arg);
    }

    /**
     * Post-crawling function for the {@link CompParamWrapExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlCompParamWrapExpression(CompParamWrapExpression obj, T arg) {
        postprocessCompParamWrapExpression(obj, arg);
        postcrawlExpression(obj, arg);
    }

    /**
     * Pre-processing function for the {@link CompParamWrapExpression} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessCompParamWrapExpression(CompParamWrapExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link CompParamWrapExpression} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessCompParamWrapExpression(CompParamWrapExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link CompParamWrapType} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkCompParamWrapType(CompParamWrapType obj, T arg) {
        precrawlCompParamWrapType(obj, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        CifType _reference = obj.getReference();
        walkCifType(_reference, arg);
        postcrawlCompParamWrapType(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link CompParamWrapType} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlCompParamWrapType(CompParamWrapType obj, T arg) {
        precrawlCifType(obj, arg);
        preprocessCompParamWrapType(obj, arg);
    }

    /**
     * Post-crawling function for the {@link CompParamWrapType} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlCompParamWrapType(CompParamWrapType obj, T arg) {
        postprocessCompParamWrapType(obj, arg);
        postcrawlCifType(obj, arg);
    }

    /**
     * Pre-processing function for the {@link CompParamWrapType} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessCompParamWrapType(CompParamWrapType obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link CompParamWrapType} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessCompParamWrapType(CompParamWrapType obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link ComplexComponent} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkComplexComponent(ComplexComponent obj, T arg) {
        if (obj instanceof Automaton) {
            walkAutomaton((Automaton)obj, arg);
            return;
        }
        if (obj instanceof Group) {
            walkGroup((Group)obj, arg);
            return;
        }
        String msg = "No redirect; unexpected object type: " + obj;
        throw new IllegalArgumentException(msg);
    }

    /**
     * Pre-crawling function for the {@link ComplexComponent} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlComplexComponent(ComplexComponent obj, T arg) {
        precrawlComponent(obj, arg);
        preprocessComplexComponent(obj, arg);
    }

    /**
     * Post-crawling function for the {@link ComplexComponent} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlComplexComponent(ComplexComponent obj, T arg) {
        postprocessComplexComponent(obj, arg);
        postcrawlComponent(obj, arg);
    }

    /**
     * Pre-processing function for the {@link ComplexComponent} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessComplexComponent(ComplexComponent obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link ComplexComponent} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessComplexComponent(ComplexComponent obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link Component} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkComponent(Component obj, T arg) {
        if (obj instanceof ComplexComponent) {
            walkComplexComponent((ComplexComponent)obj, arg);
            return;
        }
        if (obj instanceof ComponentInst) {
            walkComponentInst((ComponentInst)obj, arg);
            return;
        }
        String msg = "No redirect; unexpected object type: " + obj;
        throw new IllegalArgumentException(msg);
    }

    /**
     * Pre-crawling function for the {@link Component} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlComponent(Component obj, T arg) {
        precrawlPositionObject(obj, arg);
        preprocessComponent(obj, arg);
    }

    /**
     * Post-crawling function for the {@link Component} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlComponent(Component obj, T arg) {
        postprocessComponent(obj, arg);
        postcrawlPositionObject(obj, arg);
    }

    /**
     * Pre-processing function for the {@link Component} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessComponent(Component obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link Component} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessComponent(Component obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link ComponentDef} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkComponentDef(ComponentDef obj, T arg) {
        precrawlComponentDef(obj, arg);
        ComplexComponent _body = obj.getBody();
        walkComplexComponent(_body, arg);
        List<Parameter> _parameters = obj.getParameters();
        for (Parameter x: _parameters) {
            walkParameter(x, arg);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        postcrawlComponentDef(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link ComponentDef} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlComponentDef(ComponentDef obj, T arg) {
        precrawlPositionObject(obj, arg);
        preprocessComponentDef(obj, arg);
    }

    /**
     * Post-crawling function for the {@link ComponentDef} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlComponentDef(ComponentDef obj, T arg) {
        postprocessComponentDef(obj, arg);
        postcrawlPositionObject(obj, arg);
    }

    /**
     * Pre-processing function for the {@link ComponentDef} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessComponentDef(ComponentDef obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link ComponentDef} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessComponentDef(ComponentDef obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link ComponentDefType} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkComponentDefType(ComponentDefType obj, T arg) {
        precrawlComponentDefType(obj, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        postcrawlComponentDefType(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link ComponentDefType} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlComponentDefType(ComponentDefType obj, T arg) {
        precrawlCifType(obj, arg);
        preprocessComponentDefType(obj, arg);
    }

    /**
     * Post-crawling function for the {@link ComponentDefType} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlComponentDefType(ComponentDefType obj, T arg) {
        postprocessComponentDefType(obj, arg);
        postcrawlCifType(obj, arg);
    }

    /**
     * Pre-processing function for the {@link ComponentDefType} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessComponentDefType(ComponentDefType obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link ComponentDefType} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessComponentDefType(ComponentDefType obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link ComponentExpression} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkComponentExpression(ComponentExpression obj, T arg) {
        precrawlComponentExpression(obj, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        CifType _type = obj.getType();
        walkCifType(_type, arg);
        postcrawlComponentExpression(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link ComponentExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlComponentExpression(ComponentExpression obj, T arg) {
        precrawlExpression(obj, arg);
        preprocessComponentExpression(obj, arg);
    }

    /**
     * Post-crawling function for the {@link ComponentExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlComponentExpression(ComponentExpression obj, T arg) {
        postprocessComponentExpression(obj, arg);
        postcrawlExpression(obj, arg);
    }

    /**
     * Pre-processing function for the {@link ComponentExpression} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessComponentExpression(ComponentExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link ComponentExpression} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessComponentExpression(ComponentExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link ComponentInst} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkComponentInst(ComponentInst obj, T arg) {
        precrawlComponentInst(obj, arg);
        List<Expression> _arguments = obj.getArguments();
        for (Expression x: _arguments) {
            walkExpression(x, arg);
        }
        CifType _definition = obj.getDefinition();
        walkCifType(_definition, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        postcrawlComponentInst(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link ComponentInst} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlComponentInst(ComponentInst obj, T arg) {
        precrawlComponent(obj, arg);
        preprocessComponentInst(obj, arg);
    }

    /**
     * Post-crawling function for the {@link ComponentInst} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlComponentInst(ComponentInst obj, T arg) {
        postprocessComponentInst(obj, arg);
        postcrawlComponent(obj, arg);
    }

    /**
     * Pre-processing function for the {@link ComponentInst} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessComponentInst(ComponentInst obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link ComponentInst} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessComponentInst(ComponentInst obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link ComponentParameter} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkComponentParameter(ComponentParameter obj, T arg) {
        precrawlComponentParameter(obj, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        CifType _type = obj.getType();
        walkCifType(_type, arg);
        postcrawlComponentParameter(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link ComponentParameter} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlComponentParameter(ComponentParameter obj, T arg) {
        precrawlParameter(obj, arg);
        preprocessComponentParameter(obj, arg);
    }

    /**
     * Post-crawling function for the {@link ComponentParameter} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlComponentParameter(ComponentParameter obj, T arg) {
        postprocessComponentParameter(obj, arg);
        postcrawlParameter(obj, arg);
    }

    /**
     * Pre-processing function for the {@link ComponentParameter} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessComponentParameter(ComponentParameter obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link ComponentParameter} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessComponentParameter(ComponentParameter obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link ComponentType} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkComponentType(ComponentType obj, T arg) {
        precrawlComponentType(obj, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        postcrawlComponentType(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link ComponentType} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlComponentType(ComponentType obj, T arg) {
        precrawlCifType(obj, arg);
        preprocessComponentType(obj, arg);
    }

    /**
     * Post-crawling function for the {@link ComponentType} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlComponentType(ComponentType obj, T arg) {
        postprocessComponentType(obj, arg);
        postcrawlCifType(obj, arg);
    }

    /**
     * Pre-processing function for the {@link ComponentType} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessComponentType(ComponentType obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link ComponentType} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessComponentType(ComponentType obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link Constant} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkConstant(Constant obj, T arg) {
        precrawlConstant(obj, arg);
        List<Annotation> _annotations = obj.getAnnotations();
        for (Annotation x: _annotations) {
            walkAnnotation(x, arg);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        CifType _type = obj.getType();
        walkCifType(_type, arg);
        Expression _value = obj.getValue();
        walkExpression(_value, arg);
        postcrawlConstant(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link Constant} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlConstant(Constant obj, T arg) {
        precrawlDeclaration(obj, arg);
        preprocessConstant(obj, arg);
    }

    /**
     * Post-crawling function for the {@link Constant} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlConstant(Constant obj, T arg) {
        postprocessConstant(obj, arg);
        postcrawlDeclaration(obj, arg);
    }

    /**
     * Pre-processing function for the {@link Constant} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessConstant(Constant obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link Constant} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessConstant(Constant obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link ConstantExpression} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkConstantExpression(ConstantExpression obj, T arg) {
        precrawlConstantExpression(obj, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        CifType _type = obj.getType();
        walkCifType(_type, arg);
        postcrawlConstantExpression(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link ConstantExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlConstantExpression(ConstantExpression obj, T arg) {
        precrawlExpression(obj, arg);
        preprocessConstantExpression(obj, arg);
    }

    /**
     * Post-crawling function for the {@link ConstantExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlConstantExpression(ConstantExpression obj, T arg) {
        postprocessConstantExpression(obj, arg);
        postcrawlExpression(obj, arg);
    }

    /**
     * Pre-processing function for the {@link ConstantExpression} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessConstantExpression(ConstantExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link ConstantExpression} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessConstantExpression(ConstantExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link ContVariable} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkContVariable(ContVariable obj, T arg) {
        precrawlContVariable(obj, arg);
        List<Annotation> _annotations = obj.getAnnotations();
        for (Annotation x: _annotations) {
            walkAnnotation(x, arg);
        }
        Expression _derivative = obj.getDerivative();
        if (_derivative != null) {
            walkExpression(_derivative, arg);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        Expression _value = obj.getValue();
        if (_value != null) {
            walkExpression(_value, arg);
        }
        postcrawlContVariable(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link ContVariable} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlContVariable(ContVariable obj, T arg) {
        precrawlDeclaration(obj, arg);
        preprocessContVariable(obj, arg);
    }

    /**
     * Post-crawling function for the {@link ContVariable} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlContVariable(ContVariable obj, T arg) {
        postprocessContVariable(obj, arg);
        postcrawlDeclaration(obj, arg);
    }

    /**
     * Pre-processing function for the {@link ContVariable} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessContVariable(ContVariable obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link ContVariable} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessContVariable(ContVariable obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link ContVariableExpression} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkContVariableExpression(ContVariableExpression obj, T arg) {
        precrawlContVariableExpression(obj, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        CifType _type = obj.getType();
        walkCifType(_type, arg);
        postcrawlContVariableExpression(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link ContVariableExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlContVariableExpression(ContVariableExpression obj, T arg) {
        precrawlExpression(obj, arg);
        preprocessContVariableExpression(obj, arg);
    }

    /**
     * Post-crawling function for the {@link ContVariableExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlContVariableExpression(ContVariableExpression obj, T arg) {
        postprocessContVariableExpression(obj, arg);
        postcrawlExpression(obj, arg);
    }

    /**
     * Pre-processing function for the {@link ContVariableExpression} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessContVariableExpression(ContVariableExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link ContVariableExpression} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessContVariableExpression(ContVariableExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link ContinueFuncStatement} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkContinueFuncStatement(ContinueFuncStatement obj, T arg) {
        precrawlContinueFuncStatement(obj, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        postcrawlContinueFuncStatement(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link ContinueFuncStatement} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlContinueFuncStatement(ContinueFuncStatement obj, T arg) {
        precrawlFunctionStatement(obj, arg);
        preprocessContinueFuncStatement(obj, arg);
    }

    /**
     * Post-crawling function for the {@link ContinueFuncStatement} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlContinueFuncStatement(ContinueFuncStatement obj, T arg) {
        postprocessContinueFuncStatement(obj, arg);
        postcrawlFunctionStatement(obj, arg);
    }

    /**
     * Pre-processing function for the {@link ContinueFuncStatement} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessContinueFuncStatement(ContinueFuncStatement obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link ContinueFuncStatement} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessContinueFuncStatement(ContinueFuncStatement obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link Declaration} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkDeclaration(Declaration obj, T arg) {
        if (obj instanceof AlgVariable) {
            walkAlgVariable((AlgVariable)obj, arg);
            return;
        }
        if (obj instanceof Constant) {
            walkConstant((Constant)obj, arg);
            return;
        }
        if (obj instanceof ContVariable) {
            walkContVariable((ContVariable)obj, arg);
            return;
        }
        if (obj instanceof DiscVariable) {
            walkDiscVariable((DiscVariable)obj, arg);
            return;
        }
        if (obj instanceof EnumDecl) {
            walkEnumDecl((EnumDecl)obj, arg);
            return;
        }
        if (obj instanceof Event) {
            walkEvent((Event)obj, arg);
            return;
        }
        if (obj instanceof Function) {
            walkFunction((Function)obj, arg);
            return;
        }
        if (obj instanceof InputVariable) {
            walkInputVariable((InputVariable)obj, arg);
            return;
        }
        if (obj instanceof TypeDecl) {
            walkTypeDecl((TypeDecl)obj, arg);
            return;
        }
        String msg = "No redirect; unexpected object type: " + obj;
        throw new IllegalArgumentException(msg);
    }

    /**
     * Pre-crawling function for the {@link Declaration} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlDeclaration(Declaration obj, T arg) {
        precrawlAnnotatedObject(obj, arg);
        preprocessDeclaration(obj, arg);
    }

    /**
     * Post-crawling function for the {@link Declaration} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlDeclaration(Declaration obj, T arg) {
        postprocessDeclaration(obj, arg);
        postcrawlAnnotatedObject(obj, arg);
    }

    /**
     * Pre-processing function for the {@link Declaration} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessDeclaration(Declaration obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link Declaration} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessDeclaration(Declaration obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link DictExpression} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkDictExpression(DictExpression obj, T arg) {
        precrawlDictExpression(obj, arg);
        List<DictPair> _pairs = obj.getPairs();
        for (DictPair x: _pairs) {
            walkDictPair(x, arg);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        CifType _type = obj.getType();
        walkCifType(_type, arg);
        postcrawlDictExpression(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link DictExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlDictExpression(DictExpression obj, T arg) {
        precrawlExpression(obj, arg);
        preprocessDictExpression(obj, arg);
    }

    /**
     * Post-crawling function for the {@link DictExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlDictExpression(DictExpression obj, T arg) {
        postprocessDictExpression(obj, arg);
        postcrawlExpression(obj, arg);
    }

    /**
     * Pre-processing function for the {@link DictExpression} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessDictExpression(DictExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link DictExpression} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessDictExpression(DictExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link DictPair} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkDictPair(DictPair obj, T arg) {
        precrawlDictPair(obj, arg);
        Expression _key = obj.getKey();
        walkExpression(_key, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        Expression _value = obj.getValue();
        walkExpression(_value, arg);
        postcrawlDictPair(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link DictPair} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlDictPair(DictPair obj, T arg) {
        precrawlPositionObject(obj, arg);
        preprocessDictPair(obj, arg);
    }

    /**
     * Post-crawling function for the {@link DictPair} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlDictPair(DictPair obj, T arg) {
        postprocessDictPair(obj, arg);
        postcrawlPositionObject(obj, arg);
    }

    /**
     * Pre-processing function for the {@link DictPair} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessDictPair(DictPair obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link DictPair} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessDictPair(DictPair obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link DictType} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkDictType(DictType obj, T arg) {
        precrawlDictType(obj, arg);
        CifType _keyType = obj.getKeyType();
        walkCifType(_keyType, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        CifType _valueType = obj.getValueType();
        walkCifType(_valueType, arg);
        postcrawlDictType(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link DictType} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlDictType(DictType obj, T arg) {
        precrawlCifType(obj, arg);
        preprocessDictType(obj, arg);
    }

    /**
     * Post-crawling function for the {@link DictType} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlDictType(DictType obj, T arg) {
        postprocessDictType(obj, arg);
        postcrawlCifType(obj, arg);
    }

    /**
     * Pre-processing function for the {@link DictType} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessDictType(DictType obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link DictType} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessDictType(DictType obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link DiscVariable} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkDiscVariable(DiscVariable obj, T arg) {
        precrawlDiscVariable(obj, arg);
        List<Annotation> _annotations = obj.getAnnotations();
        for (Annotation x: _annotations) {
            walkAnnotation(x, arg);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        CifType _type = obj.getType();
        walkCifType(_type, arg);
        VariableValue _value = obj.getValue();
        if (_value != null) {
            walkVariableValue(_value, arg);
        }
        postcrawlDiscVariable(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link DiscVariable} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlDiscVariable(DiscVariable obj, T arg) {
        precrawlDeclaration(obj, arg);
        preprocessDiscVariable(obj, arg);
    }

    /**
     * Post-crawling function for the {@link DiscVariable} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlDiscVariable(DiscVariable obj, T arg) {
        postprocessDiscVariable(obj, arg);
        postcrawlDeclaration(obj, arg);
    }

    /**
     * Pre-processing function for the {@link DiscVariable} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessDiscVariable(DiscVariable obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link DiscVariable} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessDiscVariable(DiscVariable obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link DiscVariableExpression} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkDiscVariableExpression(DiscVariableExpression obj, T arg) {
        precrawlDiscVariableExpression(obj, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        CifType _type = obj.getType();
        walkCifType(_type, arg);
        postcrawlDiscVariableExpression(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link DiscVariableExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlDiscVariableExpression(DiscVariableExpression obj, T arg) {
        precrawlExpression(obj, arg);
        preprocessDiscVariableExpression(obj, arg);
    }

    /**
     * Post-crawling function for the {@link DiscVariableExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlDiscVariableExpression(DiscVariableExpression obj, T arg) {
        postprocessDiscVariableExpression(obj, arg);
        postcrawlExpression(obj, arg);
    }

    /**
     * Pre-processing function for the {@link DiscVariableExpression} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessDiscVariableExpression(DiscVariableExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link DiscVariableExpression} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessDiscVariableExpression(DiscVariableExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link DistType} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkDistType(DistType obj, T arg) {
        precrawlDistType(obj, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        CifType _sampleType = obj.getSampleType();
        walkCifType(_sampleType, arg);
        postcrawlDistType(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link DistType} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlDistType(DistType obj, T arg) {
        precrawlCifType(obj, arg);
        preprocessDistType(obj, arg);
    }

    /**
     * Post-crawling function for the {@link DistType} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlDistType(DistType obj, T arg) {
        postprocessDistType(obj, arg);
        postcrawlCifType(obj, arg);
    }

    /**
     * Pre-processing function for the {@link DistType} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessDistType(DistType obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link DistType} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessDistType(DistType obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link Edge} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkEdge(Edge obj, T arg) {
        precrawlEdge(obj, arg);
        List<EdgeEvent> _events = obj.getEvents();
        for (EdgeEvent x: _events) {
            walkEdgeEvent(x, arg);
        }
        List<Expression> _guards = obj.getGuards();
        for (Expression x: _guards) {
            walkExpression(x, arg);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        List<Update> _updates = obj.getUpdates();
        for (Update x: _updates) {
            walkUpdate(x, arg);
        }
        postcrawlEdge(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link Edge} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlEdge(Edge obj, T arg) {
        precrawlPositionObject(obj, arg);
        preprocessEdge(obj, arg);
    }

    /**
     * Post-crawling function for the {@link Edge} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlEdge(Edge obj, T arg) {
        postprocessEdge(obj, arg);
        postcrawlPositionObject(obj, arg);
    }

    /**
     * Pre-processing function for the {@link Edge} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessEdge(Edge obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link Edge} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessEdge(Edge obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link EdgeEvent} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkEdgeEvent(EdgeEvent obj, T arg) {
        if (obj instanceof EdgeReceive) {
            walkEdgeReceive((EdgeReceive)obj, arg);
            return;
        }
        if (obj instanceof EdgeSend) {
            walkEdgeSend((EdgeSend)obj, arg);
            return;
        }
        Assert.check(obj.getClass() == EdgeEventImpl.class);
        precrawlEdgeEvent(obj, arg);
        Expression _event = obj.getEvent();
        walkExpression(_event, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        postcrawlEdgeEvent(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link EdgeEvent} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlEdgeEvent(EdgeEvent obj, T arg) {
        precrawlPositionObject(obj, arg);
        preprocessEdgeEvent(obj, arg);
    }

    /**
     * Post-crawling function for the {@link EdgeEvent} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlEdgeEvent(EdgeEvent obj, T arg) {
        postprocessEdgeEvent(obj, arg);
        postcrawlPositionObject(obj, arg);
    }

    /**
     * Pre-processing function for the {@link EdgeEvent} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessEdgeEvent(EdgeEvent obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link EdgeEvent} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessEdgeEvent(EdgeEvent obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link EdgeReceive} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkEdgeReceive(EdgeReceive obj, T arg) {
        precrawlEdgeReceive(obj, arg);
        Expression _event = obj.getEvent();
        walkExpression(_event, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        postcrawlEdgeReceive(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link EdgeReceive} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlEdgeReceive(EdgeReceive obj, T arg) {
        precrawlEdgeEvent(obj, arg);
        preprocessEdgeReceive(obj, arg);
    }

    /**
     * Post-crawling function for the {@link EdgeReceive} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlEdgeReceive(EdgeReceive obj, T arg) {
        postprocessEdgeReceive(obj, arg);
        postcrawlEdgeEvent(obj, arg);
    }

    /**
     * Pre-processing function for the {@link EdgeReceive} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessEdgeReceive(EdgeReceive obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link EdgeReceive} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessEdgeReceive(EdgeReceive obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link EdgeSend} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkEdgeSend(EdgeSend obj, T arg) {
        precrawlEdgeSend(obj, arg);
        Expression _event = obj.getEvent();
        walkExpression(_event, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        Expression _value = obj.getValue();
        if (_value != null) {
            walkExpression(_value, arg);
        }
        postcrawlEdgeSend(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link EdgeSend} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlEdgeSend(EdgeSend obj, T arg) {
        precrawlEdgeEvent(obj, arg);
        preprocessEdgeSend(obj, arg);
    }

    /**
     * Post-crawling function for the {@link EdgeSend} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlEdgeSend(EdgeSend obj, T arg) {
        postprocessEdgeSend(obj, arg);
        postcrawlEdgeEvent(obj, arg);
    }

    /**
     * Pre-processing function for the {@link EdgeSend} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessEdgeSend(EdgeSend obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link EdgeSend} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessEdgeSend(EdgeSend obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link ElifExpression} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkElifExpression(ElifExpression obj, T arg) {
        precrawlElifExpression(obj, arg);
        List<Expression> _guards = obj.getGuards();
        for (Expression x: _guards) {
            walkExpression(x, arg);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        Expression _then = obj.getThen();
        walkExpression(_then, arg);
        postcrawlElifExpression(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link ElifExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlElifExpression(ElifExpression obj, T arg) {
        precrawlPositionObject(obj, arg);
        preprocessElifExpression(obj, arg);
    }

    /**
     * Post-crawling function for the {@link ElifExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlElifExpression(ElifExpression obj, T arg) {
        postprocessElifExpression(obj, arg);
        postcrawlPositionObject(obj, arg);
    }

    /**
     * Pre-processing function for the {@link ElifExpression} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessElifExpression(ElifExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link ElifExpression} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessElifExpression(ElifExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link ElifFuncStatement} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkElifFuncStatement(ElifFuncStatement obj, T arg) {
        precrawlElifFuncStatement(obj, arg);
        List<Expression> _guards = obj.getGuards();
        for (Expression x: _guards) {
            walkExpression(x, arg);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        List<FunctionStatement> _thens = obj.getThens();
        for (FunctionStatement x: _thens) {
            walkFunctionStatement(x, arg);
        }
        postcrawlElifFuncStatement(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link ElifFuncStatement} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlElifFuncStatement(ElifFuncStatement obj, T arg) {
        precrawlPositionObject(obj, arg);
        preprocessElifFuncStatement(obj, arg);
    }

    /**
     * Post-crawling function for the {@link ElifFuncStatement} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlElifFuncStatement(ElifFuncStatement obj, T arg) {
        postprocessElifFuncStatement(obj, arg);
        postcrawlPositionObject(obj, arg);
    }

    /**
     * Pre-processing function for the {@link ElifFuncStatement} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessElifFuncStatement(ElifFuncStatement obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link ElifFuncStatement} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessElifFuncStatement(ElifFuncStatement obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link ElifUpdate} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkElifUpdate(ElifUpdate obj, T arg) {
        precrawlElifUpdate(obj, arg);
        List<Expression> _guards = obj.getGuards();
        for (Expression x: _guards) {
            walkExpression(x, arg);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        List<Update> _thens = obj.getThens();
        for (Update x: _thens) {
            walkUpdate(x, arg);
        }
        postcrawlElifUpdate(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link ElifUpdate} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlElifUpdate(ElifUpdate obj, T arg) {
        precrawlPositionObject(obj, arg);
        preprocessElifUpdate(obj, arg);
    }

    /**
     * Post-crawling function for the {@link ElifUpdate} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlElifUpdate(ElifUpdate obj, T arg) {
        postprocessElifUpdate(obj, arg);
        postcrawlPositionObject(obj, arg);
    }

    /**
     * Pre-processing function for the {@link ElifUpdate} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessElifUpdate(ElifUpdate obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link ElifUpdate} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessElifUpdate(ElifUpdate obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link EnumDecl} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkEnumDecl(EnumDecl obj, T arg) {
        precrawlEnumDecl(obj, arg);
        List<Annotation> _annotations = obj.getAnnotations();
        for (Annotation x: _annotations) {
            walkAnnotation(x, arg);
        }
        List<EnumLiteral> _literals = obj.getLiterals();
        for (EnumLiteral x: _literals) {
            walkEnumLiteral(x, arg);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        postcrawlEnumDecl(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link EnumDecl} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlEnumDecl(EnumDecl obj, T arg) {
        precrawlDeclaration(obj, arg);
        preprocessEnumDecl(obj, arg);
    }

    /**
     * Post-crawling function for the {@link EnumDecl} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlEnumDecl(EnumDecl obj, T arg) {
        postprocessEnumDecl(obj, arg);
        postcrawlDeclaration(obj, arg);
    }

    /**
     * Pre-processing function for the {@link EnumDecl} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessEnumDecl(EnumDecl obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link EnumDecl} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessEnumDecl(EnumDecl obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link EnumLiteral} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkEnumLiteral(EnumLiteral obj, T arg) {
        precrawlEnumLiteral(obj, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        postcrawlEnumLiteral(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link EnumLiteral} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlEnumLiteral(EnumLiteral obj, T arg) {
        precrawlPositionObject(obj, arg);
        preprocessEnumLiteral(obj, arg);
    }

    /**
     * Post-crawling function for the {@link EnumLiteral} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlEnumLiteral(EnumLiteral obj, T arg) {
        postprocessEnumLiteral(obj, arg);
        postcrawlPositionObject(obj, arg);
    }

    /**
     * Pre-processing function for the {@link EnumLiteral} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessEnumLiteral(EnumLiteral obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link EnumLiteral} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessEnumLiteral(EnumLiteral obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link EnumLiteralExpression} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkEnumLiteralExpression(EnumLiteralExpression obj, T arg) {
        precrawlEnumLiteralExpression(obj, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        CifType _type = obj.getType();
        walkCifType(_type, arg);
        postcrawlEnumLiteralExpression(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link EnumLiteralExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlEnumLiteralExpression(EnumLiteralExpression obj, T arg) {
        precrawlExpression(obj, arg);
        preprocessEnumLiteralExpression(obj, arg);
    }

    /**
     * Post-crawling function for the {@link EnumLiteralExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlEnumLiteralExpression(EnumLiteralExpression obj, T arg) {
        postprocessEnumLiteralExpression(obj, arg);
        postcrawlExpression(obj, arg);
    }

    /**
     * Pre-processing function for the {@link EnumLiteralExpression} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessEnumLiteralExpression(EnumLiteralExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link EnumLiteralExpression} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessEnumLiteralExpression(EnumLiteralExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link EnumType} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkEnumType(EnumType obj, T arg) {
        precrawlEnumType(obj, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        postcrawlEnumType(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link EnumType} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlEnumType(EnumType obj, T arg) {
        precrawlCifType(obj, arg);
        preprocessEnumType(obj, arg);
    }

    /**
     * Post-crawling function for the {@link EnumType} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlEnumType(EnumType obj, T arg) {
        postprocessEnumType(obj, arg);
        postcrawlCifType(obj, arg);
    }

    /**
     * Pre-processing function for the {@link EnumType} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessEnumType(EnumType obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link EnumType} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessEnumType(EnumType obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link Equation} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkEquation(Equation obj, T arg) {
        precrawlEquation(obj, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        Expression _value = obj.getValue();
        walkExpression(_value, arg);
        postcrawlEquation(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link Equation} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlEquation(Equation obj, T arg) {
        precrawlPositionObject(obj, arg);
        preprocessEquation(obj, arg);
    }

    /**
     * Post-crawling function for the {@link Equation} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlEquation(Equation obj, T arg) {
        postprocessEquation(obj, arg);
        postcrawlPositionObject(obj, arg);
    }

    /**
     * Pre-processing function for the {@link Equation} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessEquation(Equation obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link Equation} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessEquation(Equation obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link Event} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkEvent(Event obj, T arg) {
        precrawlEvent(obj, arg);
        List<Annotation> _annotations = obj.getAnnotations();
        for (Annotation x: _annotations) {
            walkAnnotation(x, arg);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        CifType _type = obj.getType();
        if (_type != null) {
            walkCifType(_type, arg);
        }
        postcrawlEvent(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link Event} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlEvent(Event obj, T arg) {
        precrawlDeclaration(obj, arg);
        preprocessEvent(obj, arg);
    }

    /**
     * Post-crawling function for the {@link Event} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlEvent(Event obj, T arg) {
        postprocessEvent(obj, arg);
        postcrawlDeclaration(obj, arg);
    }

    /**
     * Pre-processing function for the {@link Event} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessEvent(Event obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link Event} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessEvent(Event obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link EventExpression} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkEventExpression(EventExpression obj, T arg) {
        precrawlEventExpression(obj, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        CifType _type = obj.getType();
        walkCifType(_type, arg);
        postcrawlEventExpression(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link EventExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlEventExpression(EventExpression obj, T arg) {
        precrawlExpression(obj, arg);
        preprocessEventExpression(obj, arg);
    }

    /**
     * Post-crawling function for the {@link EventExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlEventExpression(EventExpression obj, T arg) {
        postprocessEventExpression(obj, arg);
        postcrawlExpression(obj, arg);
    }

    /**
     * Pre-processing function for the {@link EventExpression} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessEventExpression(EventExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link EventExpression} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessEventExpression(EventExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link EventParameter} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkEventParameter(EventParameter obj, T arg) {
        precrawlEventParameter(obj, arg);
        Event _event = obj.getEvent();
        walkEvent(_event, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        postcrawlEventParameter(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link EventParameter} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlEventParameter(EventParameter obj, T arg) {
        precrawlParameter(obj, arg);
        preprocessEventParameter(obj, arg);
    }

    /**
     * Post-crawling function for the {@link EventParameter} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlEventParameter(EventParameter obj, T arg) {
        postprocessEventParameter(obj, arg);
        postcrawlParameter(obj, arg);
    }

    /**
     * Pre-processing function for the {@link EventParameter} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessEventParameter(EventParameter obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link EventParameter} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessEventParameter(EventParameter obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link Expression} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkExpression(Expression obj, T arg) {
        if (obj instanceof AlgVariableExpression) {
            walkAlgVariableExpression((AlgVariableExpression)obj, arg);
            return;
        }
        if (obj instanceof BaseFunctionExpression) {
            walkBaseFunctionExpression((BaseFunctionExpression)obj, arg);
            return;
        }
        if (obj instanceof BinaryExpression) {
            walkBinaryExpression((BinaryExpression)obj, arg);
            return;
        }
        if (obj instanceof BoolExpression) {
            walkBoolExpression((BoolExpression)obj, arg);
            return;
        }
        if (obj instanceof CastExpression) {
            walkCastExpression((CastExpression)obj, arg);
            return;
        }
        if (obj instanceof CompInstWrapExpression) {
            walkCompInstWrapExpression((CompInstWrapExpression)obj, arg);
            return;
        }
        if (obj instanceof CompParamExpression) {
            walkCompParamExpression((CompParamExpression)obj, arg);
            return;
        }
        if (obj instanceof CompParamWrapExpression) {
            walkCompParamWrapExpression((CompParamWrapExpression)obj, arg);
            return;
        }
        if (obj instanceof ComponentExpression) {
            walkComponentExpression((ComponentExpression)obj, arg);
            return;
        }
        if (obj instanceof ConstantExpression) {
            walkConstantExpression((ConstantExpression)obj, arg);
            return;
        }
        if (obj instanceof ContVariableExpression) {
            walkContVariableExpression((ContVariableExpression)obj, arg);
            return;
        }
        if (obj instanceof DictExpression) {
            walkDictExpression((DictExpression)obj, arg);
            return;
        }
        if (obj instanceof DiscVariableExpression) {
            walkDiscVariableExpression((DiscVariableExpression)obj, arg);
            return;
        }
        if (obj instanceof EnumLiteralExpression) {
            walkEnumLiteralExpression((EnumLiteralExpression)obj, arg);
            return;
        }
        if (obj instanceof EventExpression) {
            walkEventExpression((EventExpression)obj, arg);
            return;
        }
        if (obj instanceof FieldExpression) {
            walkFieldExpression((FieldExpression)obj, arg);
            return;
        }
        if (obj instanceof FunctionCallExpression) {
            walkFunctionCallExpression((FunctionCallExpression)obj, arg);
            return;
        }
        if (obj instanceof IfExpression) {
            walkIfExpression((IfExpression)obj, arg);
            return;
        }
        if (obj instanceof InputVariableExpression) {
            walkInputVariableExpression((InputVariableExpression)obj, arg);
            return;
        }
        if (obj instanceof IntExpression) {
            walkIntExpression((IntExpression)obj, arg);
            return;
        }
        if (obj instanceof ListExpression) {
            walkListExpression((ListExpression)obj, arg);
            return;
        }
        if (obj instanceof LocationExpression) {
            walkLocationExpression((LocationExpression)obj, arg);
            return;
        }
        if (obj instanceof ProjectionExpression) {
            walkProjectionExpression((ProjectionExpression)obj, arg);
            return;
        }
        if (obj instanceof RealExpression) {
            walkRealExpression((RealExpression)obj, arg);
            return;
        }
        if (obj instanceof ReceivedExpression) {
            walkReceivedExpression((ReceivedExpression)obj, arg);
            return;
        }
        if (obj instanceof SelfExpression) {
            walkSelfExpression((SelfExpression)obj, arg);
            return;
        }
        if (obj instanceof SetExpression) {
            walkSetExpression((SetExpression)obj, arg);
            return;
        }
        if (obj instanceof SliceExpression) {
            walkSliceExpression((SliceExpression)obj, arg);
            return;
        }
        if (obj instanceof StringExpression) {
            walkStringExpression((StringExpression)obj, arg);
            return;
        }
        if (obj instanceof SwitchExpression) {
            walkSwitchExpression((SwitchExpression)obj, arg);
            return;
        }
        if (obj instanceof TauExpression) {
            walkTauExpression((TauExpression)obj, arg);
            return;
        }
        if (obj instanceof TimeExpression) {
            walkTimeExpression((TimeExpression)obj, arg);
            return;
        }
        if (obj instanceof TupleExpression) {
            walkTupleExpression((TupleExpression)obj, arg);
            return;
        }
        if (obj instanceof UnaryExpression) {
            walkUnaryExpression((UnaryExpression)obj, arg);
            return;
        }
        String msg = "No redirect; unexpected object type: " + obj;
        throw new IllegalArgumentException(msg);
    }

    /**
     * Pre-crawling function for the {@link Expression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlExpression(Expression obj, T arg) {
        precrawlPositionObject(obj, arg);
        preprocessExpression(obj, arg);
    }

    /**
     * Post-crawling function for the {@link Expression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlExpression(Expression obj, T arg) {
        postprocessExpression(obj, arg);
        postcrawlPositionObject(obj, arg);
    }

    /**
     * Pre-processing function for the {@link Expression} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessExpression(Expression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link Expression} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessExpression(Expression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link ExternalFunction} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkExternalFunction(ExternalFunction obj, T arg) {
        precrawlExternalFunction(obj, arg);
        List<Annotation> _annotations = obj.getAnnotations();
        for (Annotation x: _annotations) {
            walkAnnotation(x, arg);
        }
        List<FunctionParameter> _parameters = obj.getParameters();
        for (FunctionParameter x: _parameters) {
            walkFunctionParameter(x, arg);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        List<CifType> _returnTypes = obj.getReturnTypes();
        for (CifType x: _returnTypes) {
            walkCifType(x, arg);
        }
        postcrawlExternalFunction(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link ExternalFunction} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlExternalFunction(ExternalFunction obj, T arg) {
        precrawlFunction(obj, arg);
        preprocessExternalFunction(obj, arg);
    }

    /**
     * Post-crawling function for the {@link ExternalFunction} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlExternalFunction(ExternalFunction obj, T arg) {
        postprocessExternalFunction(obj, arg);
        postcrawlFunction(obj, arg);
    }

    /**
     * Pre-processing function for the {@link ExternalFunction} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessExternalFunction(ExternalFunction obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link ExternalFunction} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessExternalFunction(ExternalFunction obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link Field} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkField(Field obj, T arg) {
        precrawlField(obj, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        CifType _type = obj.getType();
        walkCifType(_type, arg);
        postcrawlField(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link Field} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlField(Field obj, T arg) {
        precrawlPositionObject(obj, arg);
        preprocessField(obj, arg);
    }

    /**
     * Post-crawling function for the {@link Field} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlField(Field obj, T arg) {
        postprocessField(obj, arg);
        postcrawlPositionObject(obj, arg);
    }

    /**
     * Pre-processing function for the {@link Field} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessField(Field obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link Field} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessField(Field obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link FieldExpression} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkFieldExpression(FieldExpression obj, T arg) {
        precrawlFieldExpression(obj, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        CifType _type = obj.getType();
        walkCifType(_type, arg);
        postcrawlFieldExpression(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link FieldExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlFieldExpression(FieldExpression obj, T arg) {
        precrawlExpression(obj, arg);
        preprocessFieldExpression(obj, arg);
    }

    /**
     * Post-crawling function for the {@link FieldExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlFieldExpression(FieldExpression obj, T arg) {
        postprocessFieldExpression(obj, arg);
        postcrawlExpression(obj, arg);
    }

    /**
     * Pre-processing function for the {@link FieldExpression} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessFieldExpression(FieldExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link FieldExpression} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessFieldExpression(FieldExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link FuncType} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkFuncType(FuncType obj, T arg) {
        precrawlFuncType(obj, arg);
        List<CifType> _paramTypes = obj.getParamTypes();
        for (CifType x: _paramTypes) {
            walkCifType(x, arg);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        CifType _returnType = obj.getReturnType();
        walkCifType(_returnType, arg);
        postcrawlFuncType(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link FuncType} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlFuncType(FuncType obj, T arg) {
        precrawlCifType(obj, arg);
        preprocessFuncType(obj, arg);
    }

    /**
     * Post-crawling function for the {@link FuncType} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlFuncType(FuncType obj, T arg) {
        postprocessFuncType(obj, arg);
        postcrawlCifType(obj, arg);
    }

    /**
     * Pre-processing function for the {@link FuncType} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessFuncType(FuncType obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link FuncType} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessFuncType(FuncType obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link Function} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkFunction(Function obj, T arg) {
        if (obj instanceof ExternalFunction) {
            walkExternalFunction((ExternalFunction)obj, arg);
            return;
        }
        if (obj instanceof InternalFunction) {
            walkInternalFunction((InternalFunction)obj, arg);
            return;
        }
        String msg = "No redirect; unexpected object type: " + obj;
        throw new IllegalArgumentException(msg);
    }

    /**
     * Pre-crawling function for the {@link Function} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlFunction(Function obj, T arg) {
        precrawlDeclaration(obj, arg);
        preprocessFunction(obj, arg);
    }

    /**
     * Post-crawling function for the {@link Function} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlFunction(Function obj, T arg) {
        postprocessFunction(obj, arg);
        postcrawlDeclaration(obj, arg);
    }

    /**
     * Pre-processing function for the {@link Function} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessFunction(Function obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link Function} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessFunction(Function obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link FunctionCallExpression} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkFunctionCallExpression(FunctionCallExpression obj, T arg) {
        precrawlFunctionCallExpression(obj, arg);
        List<Expression> _arguments = obj.getArguments();
        for (Expression x: _arguments) {
            walkExpression(x, arg);
        }
        Expression _function = obj.getFunction();
        walkExpression(_function, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        CifType _type = obj.getType();
        walkCifType(_type, arg);
        postcrawlFunctionCallExpression(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link FunctionCallExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlFunctionCallExpression(FunctionCallExpression obj, T arg) {
        precrawlExpression(obj, arg);
        preprocessFunctionCallExpression(obj, arg);
    }

    /**
     * Post-crawling function for the {@link FunctionCallExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlFunctionCallExpression(FunctionCallExpression obj, T arg) {
        postprocessFunctionCallExpression(obj, arg);
        postcrawlExpression(obj, arg);
    }

    /**
     * Pre-processing function for the {@link FunctionCallExpression} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessFunctionCallExpression(FunctionCallExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link FunctionCallExpression} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessFunctionCallExpression(FunctionCallExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link FunctionExpression} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkFunctionExpression(FunctionExpression obj, T arg) {
        precrawlFunctionExpression(obj, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        CifType _type = obj.getType();
        walkCifType(_type, arg);
        postcrawlFunctionExpression(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link FunctionExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlFunctionExpression(FunctionExpression obj, T arg) {
        precrawlBaseFunctionExpression(obj, arg);
        preprocessFunctionExpression(obj, arg);
    }

    /**
     * Post-crawling function for the {@link FunctionExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlFunctionExpression(FunctionExpression obj, T arg) {
        postprocessFunctionExpression(obj, arg);
        postcrawlBaseFunctionExpression(obj, arg);
    }

    /**
     * Pre-processing function for the {@link FunctionExpression} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessFunctionExpression(FunctionExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link FunctionExpression} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessFunctionExpression(FunctionExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link FunctionParameter} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkFunctionParameter(FunctionParameter obj, T arg) {
        precrawlFunctionParameter(obj, arg);
        DiscVariable _parameter = obj.getParameter();
        walkDiscVariable(_parameter, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        postcrawlFunctionParameter(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link FunctionParameter} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlFunctionParameter(FunctionParameter obj, T arg) {
        precrawlPositionObject(obj, arg);
        preprocessFunctionParameter(obj, arg);
    }

    /**
     * Post-crawling function for the {@link FunctionParameter} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlFunctionParameter(FunctionParameter obj, T arg) {
        postprocessFunctionParameter(obj, arg);
        postcrawlPositionObject(obj, arg);
    }

    /**
     * Pre-processing function for the {@link FunctionParameter} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessFunctionParameter(FunctionParameter obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link FunctionParameter} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessFunctionParameter(FunctionParameter obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link FunctionStatement} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkFunctionStatement(FunctionStatement obj, T arg) {
        if (obj instanceof AssignmentFuncStatement) {
            walkAssignmentFuncStatement((AssignmentFuncStatement)obj, arg);
            return;
        }
        if (obj instanceof BreakFuncStatement) {
            walkBreakFuncStatement((BreakFuncStatement)obj, arg);
            return;
        }
        if (obj instanceof ContinueFuncStatement) {
            walkContinueFuncStatement((ContinueFuncStatement)obj, arg);
            return;
        }
        if (obj instanceof IfFuncStatement) {
            walkIfFuncStatement((IfFuncStatement)obj, arg);
            return;
        }
        if (obj instanceof ReturnFuncStatement) {
            walkReturnFuncStatement((ReturnFuncStatement)obj, arg);
            return;
        }
        if (obj instanceof WhileFuncStatement) {
            walkWhileFuncStatement((WhileFuncStatement)obj, arg);
            return;
        }
        String msg = "No redirect; unexpected object type: " + obj;
        throw new IllegalArgumentException(msg);
    }

    /**
     * Pre-crawling function for the {@link FunctionStatement} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlFunctionStatement(FunctionStatement obj, T arg) {
        precrawlPositionObject(obj, arg);
        preprocessFunctionStatement(obj, arg);
    }

    /**
     * Post-crawling function for the {@link FunctionStatement} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlFunctionStatement(FunctionStatement obj, T arg) {
        postprocessFunctionStatement(obj, arg);
        postcrawlPositionObject(obj, arg);
    }

    /**
     * Pre-processing function for the {@link FunctionStatement} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessFunctionStatement(FunctionStatement obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link FunctionStatement} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessFunctionStatement(FunctionStatement obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link Group} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkGroup(Group obj, T arg) {
        if (obj instanceof Specification) {
            walkSpecification((Specification)obj, arg);
            return;
        }
        Assert.check(obj.getClass() == GroupImpl.class);
        precrawlGroup(obj, arg);
        List<Component> _components = obj.getComponents();
        for (Component x: _components) {
            walkComponent(x, arg);
        }
        List<Declaration> _declarations = obj.getDeclarations();
        for (Declaration x: _declarations) {
            walkDeclaration(x, arg);
        }
        List<ComponentDef> _definitions = obj.getDefinitions();
        for (ComponentDef x: _definitions) {
            walkComponentDef(x, arg);
        }
        List<Equation> _equations = obj.getEquations();
        for (Equation x: _equations) {
            walkEquation(x, arg);
        }
        List<Expression> _initials = obj.getInitials();
        for (Expression x: _initials) {
            walkExpression(x, arg);
        }
        List<Invariant> _invariants = obj.getInvariants();
        for (Invariant x: _invariants) {
            walkInvariant(x, arg);
        }
        List<IoDecl> _ioDecls = obj.getIoDecls();
        for (IoDecl x: _ioDecls) {
            walkIoDecl(x, arg);
        }
        List<Expression> _markeds = obj.getMarkeds();
        for (Expression x: _markeds) {
            walkExpression(x, arg);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        postcrawlGroup(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link Group} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlGroup(Group obj, T arg) {
        precrawlComplexComponent(obj, arg);
        preprocessGroup(obj, arg);
    }

    /**
     * Post-crawling function for the {@link Group} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlGroup(Group obj, T arg) {
        postprocessGroup(obj, arg);
        postcrawlComplexComponent(obj, arg);
    }

    /**
     * Pre-processing function for the {@link Group} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessGroup(Group obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link Group} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessGroup(Group obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link IfExpression} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkIfExpression(IfExpression obj, T arg) {
        precrawlIfExpression(obj, arg);
        List<ElifExpression> _elifs = obj.getElifs();
        for (ElifExpression x: _elifs) {
            walkElifExpression(x, arg);
        }
        Expression _else = obj.getElse();
        walkExpression(_else, arg);
        List<Expression> _guards = obj.getGuards();
        for (Expression x: _guards) {
            walkExpression(x, arg);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        Expression _then = obj.getThen();
        walkExpression(_then, arg);
        CifType _type = obj.getType();
        walkCifType(_type, arg);
        postcrawlIfExpression(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link IfExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlIfExpression(IfExpression obj, T arg) {
        precrawlExpression(obj, arg);
        preprocessIfExpression(obj, arg);
    }

    /**
     * Post-crawling function for the {@link IfExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlIfExpression(IfExpression obj, T arg) {
        postprocessIfExpression(obj, arg);
        postcrawlExpression(obj, arg);
    }

    /**
     * Pre-processing function for the {@link IfExpression} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessIfExpression(IfExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link IfExpression} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessIfExpression(IfExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link IfFuncStatement} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkIfFuncStatement(IfFuncStatement obj, T arg) {
        precrawlIfFuncStatement(obj, arg);
        List<ElifFuncStatement> _elifs = obj.getElifs();
        for (ElifFuncStatement x: _elifs) {
            walkElifFuncStatement(x, arg);
        }
        List<FunctionStatement> _elses = obj.getElses();
        for (FunctionStatement x: _elses) {
            walkFunctionStatement(x, arg);
        }
        List<Expression> _guards = obj.getGuards();
        for (Expression x: _guards) {
            walkExpression(x, arg);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        List<FunctionStatement> _thens = obj.getThens();
        for (FunctionStatement x: _thens) {
            walkFunctionStatement(x, arg);
        }
        postcrawlIfFuncStatement(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link IfFuncStatement} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlIfFuncStatement(IfFuncStatement obj, T arg) {
        precrawlFunctionStatement(obj, arg);
        preprocessIfFuncStatement(obj, arg);
    }

    /**
     * Post-crawling function for the {@link IfFuncStatement} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlIfFuncStatement(IfFuncStatement obj, T arg) {
        postprocessIfFuncStatement(obj, arg);
        postcrawlFunctionStatement(obj, arg);
    }

    /**
     * Pre-processing function for the {@link IfFuncStatement} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessIfFuncStatement(IfFuncStatement obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link IfFuncStatement} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessIfFuncStatement(IfFuncStatement obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link IfUpdate} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkIfUpdate(IfUpdate obj, T arg) {
        precrawlIfUpdate(obj, arg);
        List<ElifUpdate> _elifs = obj.getElifs();
        for (ElifUpdate x: _elifs) {
            walkElifUpdate(x, arg);
        }
        List<Update> _elses = obj.getElses();
        for (Update x: _elses) {
            walkUpdate(x, arg);
        }
        List<Expression> _guards = obj.getGuards();
        for (Expression x: _guards) {
            walkExpression(x, arg);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        List<Update> _thens = obj.getThens();
        for (Update x: _thens) {
            walkUpdate(x, arg);
        }
        postcrawlIfUpdate(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link IfUpdate} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlIfUpdate(IfUpdate obj, T arg) {
        precrawlUpdate(obj, arg);
        preprocessIfUpdate(obj, arg);
    }

    /**
     * Post-crawling function for the {@link IfUpdate} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlIfUpdate(IfUpdate obj, T arg) {
        postprocessIfUpdate(obj, arg);
        postcrawlUpdate(obj, arg);
    }

    /**
     * Pre-processing function for the {@link IfUpdate} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessIfUpdate(IfUpdate obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link IfUpdate} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessIfUpdate(IfUpdate obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link InputVariable} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkInputVariable(InputVariable obj, T arg) {
        precrawlInputVariable(obj, arg);
        List<Annotation> _annotations = obj.getAnnotations();
        for (Annotation x: _annotations) {
            walkAnnotation(x, arg);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        CifType _type = obj.getType();
        walkCifType(_type, arg);
        postcrawlInputVariable(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link InputVariable} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlInputVariable(InputVariable obj, T arg) {
        precrawlDeclaration(obj, arg);
        preprocessInputVariable(obj, arg);
    }

    /**
     * Post-crawling function for the {@link InputVariable} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlInputVariable(InputVariable obj, T arg) {
        postprocessInputVariable(obj, arg);
        postcrawlDeclaration(obj, arg);
    }

    /**
     * Pre-processing function for the {@link InputVariable} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessInputVariable(InputVariable obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link InputVariable} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessInputVariable(InputVariable obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link InputVariableExpression} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkInputVariableExpression(InputVariableExpression obj, T arg) {
        precrawlInputVariableExpression(obj, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        CifType _type = obj.getType();
        walkCifType(_type, arg);
        postcrawlInputVariableExpression(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link InputVariableExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlInputVariableExpression(InputVariableExpression obj, T arg) {
        precrawlExpression(obj, arg);
        preprocessInputVariableExpression(obj, arg);
    }

    /**
     * Post-crawling function for the {@link InputVariableExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlInputVariableExpression(InputVariableExpression obj, T arg) {
        postprocessInputVariableExpression(obj, arg);
        postcrawlExpression(obj, arg);
    }

    /**
     * Pre-processing function for the {@link InputVariableExpression} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessInputVariableExpression(InputVariableExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link InputVariableExpression} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessInputVariableExpression(InputVariableExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link IntExpression} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkIntExpression(IntExpression obj, T arg) {
        precrawlIntExpression(obj, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        CifType _type = obj.getType();
        walkCifType(_type, arg);
        postcrawlIntExpression(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link IntExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlIntExpression(IntExpression obj, T arg) {
        precrawlExpression(obj, arg);
        preprocessIntExpression(obj, arg);
    }

    /**
     * Post-crawling function for the {@link IntExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlIntExpression(IntExpression obj, T arg) {
        postprocessIntExpression(obj, arg);
        postcrawlExpression(obj, arg);
    }

    /**
     * Pre-processing function for the {@link IntExpression} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessIntExpression(IntExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link IntExpression} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessIntExpression(IntExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link IntType} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkIntType(IntType obj, T arg) {
        precrawlIntType(obj, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        postcrawlIntType(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link IntType} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlIntType(IntType obj, T arg) {
        precrawlCifType(obj, arg);
        preprocessIntType(obj, arg);
    }

    /**
     * Post-crawling function for the {@link IntType} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlIntType(IntType obj, T arg) {
        postprocessIntType(obj, arg);
        postcrawlCifType(obj, arg);
    }

    /**
     * Pre-processing function for the {@link IntType} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessIntType(IntType obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link IntType} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessIntType(IntType obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link InternalFunction} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkInternalFunction(InternalFunction obj, T arg) {
        precrawlInternalFunction(obj, arg);
        List<Annotation> _annotations = obj.getAnnotations();
        for (Annotation x: _annotations) {
            walkAnnotation(x, arg);
        }
        List<FunctionParameter> _parameters = obj.getParameters();
        for (FunctionParameter x: _parameters) {
            walkFunctionParameter(x, arg);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        List<CifType> _returnTypes = obj.getReturnTypes();
        for (CifType x: _returnTypes) {
            walkCifType(x, arg);
        }
        List<FunctionStatement> _statements = obj.getStatements();
        for (FunctionStatement x: _statements) {
            walkFunctionStatement(x, arg);
        }
        List<DiscVariable> _variables = obj.getVariables();
        for (DiscVariable x: _variables) {
            walkDiscVariable(x, arg);
        }
        postcrawlInternalFunction(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link InternalFunction} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlInternalFunction(InternalFunction obj, T arg) {
        precrawlFunction(obj, arg);
        preprocessInternalFunction(obj, arg);
    }

    /**
     * Post-crawling function for the {@link InternalFunction} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlInternalFunction(InternalFunction obj, T arg) {
        postprocessInternalFunction(obj, arg);
        postcrawlFunction(obj, arg);
    }

    /**
     * Pre-processing function for the {@link InternalFunction} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessInternalFunction(InternalFunction obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link InternalFunction} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessInternalFunction(InternalFunction obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link Invariant} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkInvariant(Invariant obj, T arg) {
        precrawlInvariant(obj, arg);
        Expression _event = obj.getEvent();
        if (_event != null) {
            walkExpression(_event, arg);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        Expression _predicate = obj.getPredicate();
        walkExpression(_predicate, arg);
        postcrawlInvariant(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link Invariant} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlInvariant(Invariant obj, T arg) {
        precrawlPositionObject(obj, arg);
        preprocessInvariant(obj, arg);
    }

    /**
     * Post-crawling function for the {@link Invariant} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlInvariant(Invariant obj, T arg) {
        postprocessInvariant(obj, arg);
        postcrawlPositionObject(obj, arg);
    }

    /**
     * Pre-processing function for the {@link Invariant} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessInvariant(Invariant obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link Invariant} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessInvariant(Invariant obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link IoDecl} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkIoDecl(IoDecl obj, T arg) {
        if (obj instanceof Print) {
            walkPrint((Print)obj, arg);
            return;
        }
        if (obj instanceof PrintFile) {
            walkPrintFile((PrintFile)obj, arg);
            return;
        }
        if (obj instanceof SvgCopy) {
            walkSvgCopy((SvgCopy)obj, arg);
            return;
        }
        if (obj instanceof SvgFile) {
            walkSvgFile((SvgFile)obj, arg);
            return;
        }
        if (obj instanceof SvgIn) {
            walkSvgIn((SvgIn)obj, arg);
            return;
        }
        if (obj instanceof SvgMove) {
            walkSvgMove((SvgMove)obj, arg);
            return;
        }
        if (obj instanceof SvgOut) {
            walkSvgOut((SvgOut)obj, arg);
            return;
        }
        String msg = "No redirect; unexpected object type: " + obj;
        throw new IllegalArgumentException(msg);
    }

    /**
     * Pre-crawling function for the {@link IoDecl} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlIoDecl(IoDecl obj, T arg) {
        precrawlPositionObject(obj, arg);
        preprocessIoDecl(obj, arg);
    }

    /**
     * Post-crawling function for the {@link IoDecl} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlIoDecl(IoDecl obj, T arg) {
        postprocessIoDecl(obj, arg);
        postcrawlPositionObject(obj, arg);
    }

    /**
     * Pre-processing function for the {@link IoDecl} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessIoDecl(IoDecl obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link IoDecl} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessIoDecl(IoDecl obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link ListExpression} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkListExpression(ListExpression obj, T arg) {
        precrawlListExpression(obj, arg);
        List<Expression> _elements = obj.getElements();
        for (Expression x: _elements) {
            walkExpression(x, arg);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        CifType _type = obj.getType();
        walkCifType(_type, arg);
        postcrawlListExpression(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link ListExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlListExpression(ListExpression obj, T arg) {
        precrawlExpression(obj, arg);
        preprocessListExpression(obj, arg);
    }

    /**
     * Post-crawling function for the {@link ListExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlListExpression(ListExpression obj, T arg) {
        postprocessListExpression(obj, arg);
        postcrawlExpression(obj, arg);
    }

    /**
     * Pre-processing function for the {@link ListExpression} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessListExpression(ListExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link ListExpression} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessListExpression(ListExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link ListType} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkListType(ListType obj, T arg) {
        precrawlListType(obj, arg);
        CifType _elementType = obj.getElementType();
        walkCifType(_elementType, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        postcrawlListType(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link ListType} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlListType(ListType obj, T arg) {
        precrawlCifType(obj, arg);
        preprocessListType(obj, arg);
    }

    /**
     * Post-crawling function for the {@link ListType} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlListType(ListType obj, T arg) {
        postprocessListType(obj, arg);
        postcrawlCifType(obj, arg);
    }

    /**
     * Pre-processing function for the {@link ListType} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessListType(ListType obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link ListType} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessListType(ListType obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link Location} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkLocation(Location obj, T arg) {
        precrawlLocation(obj, arg);
        List<Annotation> _annotations = obj.getAnnotations();
        for (Annotation x: _annotations) {
            walkAnnotation(x, arg);
        }
        List<Edge> _edges = obj.getEdges();
        for (Edge x: _edges) {
            walkEdge(x, arg);
        }
        List<Equation> _equations = obj.getEquations();
        for (Equation x: _equations) {
            walkEquation(x, arg);
        }
        List<Expression> _initials = obj.getInitials();
        for (Expression x: _initials) {
            walkExpression(x, arg);
        }
        List<Invariant> _invariants = obj.getInvariants();
        for (Invariant x: _invariants) {
            walkInvariant(x, arg);
        }
        List<Expression> _markeds = obj.getMarkeds();
        for (Expression x: _markeds) {
            walkExpression(x, arg);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        postcrawlLocation(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link Location} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlLocation(Location obj, T arg) {
        precrawlAnnotatedObject(obj, arg);
        preprocessLocation(obj, arg);
    }

    /**
     * Post-crawling function for the {@link Location} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlLocation(Location obj, T arg) {
        postprocessLocation(obj, arg);
        postcrawlAnnotatedObject(obj, arg);
    }

    /**
     * Pre-processing function for the {@link Location} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessLocation(Location obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link Location} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessLocation(Location obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link LocationExpression} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkLocationExpression(LocationExpression obj, T arg) {
        precrawlLocationExpression(obj, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        CifType _type = obj.getType();
        walkCifType(_type, arg);
        postcrawlLocationExpression(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link LocationExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlLocationExpression(LocationExpression obj, T arg) {
        precrawlExpression(obj, arg);
        preprocessLocationExpression(obj, arg);
    }

    /**
     * Post-crawling function for the {@link LocationExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlLocationExpression(LocationExpression obj, T arg) {
        postprocessLocationExpression(obj, arg);
        postcrawlExpression(obj, arg);
    }

    /**
     * Pre-processing function for the {@link LocationExpression} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessLocationExpression(LocationExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link LocationExpression} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessLocationExpression(LocationExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link LocationParameter} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkLocationParameter(LocationParameter obj, T arg) {
        precrawlLocationParameter(obj, arg);
        Location _location = obj.getLocation();
        walkLocation(_location, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        postcrawlLocationParameter(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link LocationParameter} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlLocationParameter(LocationParameter obj, T arg) {
        precrawlParameter(obj, arg);
        preprocessLocationParameter(obj, arg);
    }

    /**
     * Post-crawling function for the {@link LocationParameter} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlLocationParameter(LocationParameter obj, T arg) {
        postprocessLocationParameter(obj, arg);
        postcrawlParameter(obj, arg);
    }

    /**
     * Pre-processing function for the {@link LocationParameter} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessLocationParameter(LocationParameter obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link LocationParameter} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessLocationParameter(LocationParameter obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link Monitors} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkMonitors(Monitors obj, T arg) {
        precrawlMonitors(obj, arg);
        List<Expression> _events = obj.getEvents();
        for (Expression x: _events) {
            walkExpression(x, arg);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        postcrawlMonitors(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link Monitors} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlMonitors(Monitors obj, T arg) {
        precrawlPositionObject(obj, arg);
        preprocessMonitors(obj, arg);
    }

    /**
     * Post-crawling function for the {@link Monitors} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlMonitors(Monitors obj, T arg) {
        postprocessMonitors(obj, arg);
        postcrawlPositionObject(obj, arg);
    }

    /**
     * Pre-processing function for the {@link Monitors} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessMonitors(Monitors obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link Monitors} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessMonitors(Monitors obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link Parameter} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkParameter(Parameter obj, T arg) {
        if (obj instanceof AlgParameter) {
            walkAlgParameter((AlgParameter)obj, arg);
            return;
        }
        if (obj instanceof ComponentParameter) {
            walkComponentParameter((ComponentParameter)obj, arg);
            return;
        }
        if (obj instanceof EventParameter) {
            walkEventParameter((EventParameter)obj, arg);
            return;
        }
        if (obj instanceof LocationParameter) {
            walkLocationParameter((LocationParameter)obj, arg);
            return;
        }
        String msg = "No redirect; unexpected object type: " + obj;
        throw new IllegalArgumentException(msg);
    }

    /**
     * Pre-crawling function for the {@link Parameter} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlParameter(Parameter obj, T arg) {
        precrawlPositionObject(obj, arg);
        preprocessParameter(obj, arg);
    }

    /**
     * Post-crawling function for the {@link Parameter} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlParameter(Parameter obj, T arg) {
        postprocessParameter(obj, arg);
        postcrawlPositionObject(obj, arg);
    }

    /**
     * Pre-processing function for the {@link Parameter} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessParameter(Parameter obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link Parameter} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessParameter(Parameter obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link Position} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkPosition(Position obj, T arg) {
        precrawlPosition(obj, arg);
        postcrawlPosition(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link Position} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlPosition(Position obj, T arg) {
        preprocessPosition(obj, arg);
    }

    /**
     * Post-crawling function for the {@link Position} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlPosition(Position obj, T arg) {
        postprocessPosition(obj, arg);
    }

    /**
     * Pre-processing function for the {@link Position} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessPosition(Position obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link Position} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessPosition(Position obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link PositionObject} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkPositionObject(PositionObject obj, T arg) {
        if (obj instanceof Alphabet) {
            walkAlphabet((Alphabet)obj, arg);
            return;
        }
        if (obj instanceof AnnotatedObject) {
            walkAnnotatedObject((AnnotatedObject)obj, arg);
            return;
        }
        if (obj instanceof Annotation) {
            walkAnnotation((Annotation)obj, arg);
            return;
        }
        if (obj instanceof AnnotationArgument) {
            walkAnnotationArgument((AnnotationArgument)obj, arg);
            return;
        }
        if (obj instanceof CifType) {
            walkCifType((CifType)obj, arg);
            return;
        }
        if (obj instanceof Component) {
            walkComponent((Component)obj, arg);
            return;
        }
        if (obj instanceof ComponentDef) {
            walkComponentDef((ComponentDef)obj, arg);
            return;
        }
        if (obj instanceof DictPair) {
            walkDictPair((DictPair)obj, arg);
            return;
        }
        if (obj instanceof Edge) {
            walkEdge((Edge)obj, arg);
            return;
        }
        if (obj instanceof EdgeEvent) {
            walkEdgeEvent((EdgeEvent)obj, arg);
            return;
        }
        if (obj instanceof ElifExpression) {
            walkElifExpression((ElifExpression)obj, arg);
            return;
        }
        if (obj instanceof ElifFuncStatement) {
            walkElifFuncStatement((ElifFuncStatement)obj, arg);
            return;
        }
        if (obj instanceof ElifUpdate) {
            walkElifUpdate((ElifUpdate)obj, arg);
            return;
        }
        if (obj instanceof EnumLiteral) {
            walkEnumLiteral((EnumLiteral)obj, arg);
            return;
        }
        if (obj instanceof Equation) {
            walkEquation((Equation)obj, arg);
            return;
        }
        if (obj instanceof Expression) {
            walkExpression((Expression)obj, arg);
            return;
        }
        if (obj instanceof Field) {
            walkField((Field)obj, arg);
            return;
        }
        if (obj instanceof FunctionParameter) {
            walkFunctionParameter((FunctionParameter)obj, arg);
            return;
        }
        if (obj instanceof FunctionStatement) {
            walkFunctionStatement((FunctionStatement)obj, arg);
            return;
        }
        if (obj instanceof Invariant) {
            walkInvariant((Invariant)obj, arg);
            return;
        }
        if (obj instanceof IoDecl) {
            walkIoDecl((IoDecl)obj, arg);
            return;
        }
        if (obj instanceof Monitors) {
            walkMonitors((Monitors)obj, arg);
            return;
        }
        if (obj instanceof Parameter) {
            walkParameter((Parameter)obj, arg);
            return;
        }
        if (obj instanceof PrintFor) {
            walkPrintFor((PrintFor)obj, arg);
            return;
        }
        if (obj instanceof SvgInEvent) {
            walkSvgInEvent((SvgInEvent)obj, arg);
            return;
        }
        if (obj instanceof SvgInEventIfEntry) {
            walkSvgInEventIfEntry((SvgInEventIfEntry)obj, arg);
            return;
        }
        if (obj instanceof SwitchCase) {
            walkSwitchCase((SwitchCase)obj, arg);
            return;
        }
        if (obj instanceof Update) {
            walkUpdate((Update)obj, arg);
            return;
        }
        if (obj instanceof VariableValue) {
            walkVariableValue((VariableValue)obj, arg);
            return;
        }
        String msg = "No redirect; unexpected object type: " + obj;
        throw new IllegalArgumentException(msg);
    }

    /**
     * Pre-crawling function for the {@link PositionObject} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlPositionObject(PositionObject obj, T arg) {
        preprocessPositionObject(obj, arg);
    }

    /**
     * Post-crawling function for the {@link PositionObject} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlPositionObject(PositionObject obj, T arg) {
        postprocessPositionObject(obj, arg);
    }

    /**
     * Pre-processing function for the {@link PositionObject} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessPositionObject(PositionObject obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link PositionObject} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessPositionObject(PositionObject obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link Print} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkPrint(Print obj, T arg) {
        precrawlPrint(obj, arg);
        PrintFile _file = obj.getFile();
        if (_file != null) {
            walkPrintFile(_file, arg);
        }
        List<PrintFor> _fors = obj.getFors();
        for (PrintFor x: _fors) {
            walkPrintFor(x, arg);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        Expression _txtPost = obj.getTxtPost();
        if (_txtPost != null) {
            walkExpression(_txtPost, arg);
        }
        Expression _txtPre = obj.getTxtPre();
        if (_txtPre != null) {
            walkExpression(_txtPre, arg);
        }
        Expression _whenPost = obj.getWhenPost();
        if (_whenPost != null) {
            walkExpression(_whenPost, arg);
        }
        Expression _whenPre = obj.getWhenPre();
        if (_whenPre != null) {
            walkExpression(_whenPre, arg);
        }
        postcrawlPrint(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link Print} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlPrint(Print obj, T arg) {
        precrawlIoDecl(obj, arg);
        preprocessPrint(obj, arg);
    }

    /**
     * Post-crawling function for the {@link Print} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlPrint(Print obj, T arg) {
        postprocessPrint(obj, arg);
        postcrawlIoDecl(obj, arg);
    }

    /**
     * Pre-processing function for the {@link Print} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessPrint(Print obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link Print} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessPrint(Print obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link PrintFile} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkPrintFile(PrintFile obj, T arg) {
        precrawlPrintFile(obj, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        postcrawlPrintFile(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link PrintFile} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlPrintFile(PrintFile obj, T arg) {
        precrawlIoDecl(obj, arg);
        preprocessPrintFile(obj, arg);
    }

    /**
     * Post-crawling function for the {@link PrintFile} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlPrintFile(PrintFile obj, T arg) {
        postprocessPrintFile(obj, arg);
        postcrawlIoDecl(obj, arg);
    }

    /**
     * Pre-processing function for the {@link PrintFile} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessPrintFile(PrintFile obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link PrintFile} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessPrintFile(PrintFile obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link PrintFor} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkPrintFor(PrintFor obj, T arg) {
        precrawlPrintFor(obj, arg);
        Expression _event = obj.getEvent();
        if (_event != null) {
            walkExpression(_event, arg);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        postcrawlPrintFor(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link PrintFor} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlPrintFor(PrintFor obj, T arg) {
        precrawlPositionObject(obj, arg);
        preprocessPrintFor(obj, arg);
    }

    /**
     * Post-crawling function for the {@link PrintFor} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlPrintFor(PrintFor obj, T arg) {
        postprocessPrintFor(obj, arg);
        postcrawlPositionObject(obj, arg);
    }

    /**
     * Pre-processing function for the {@link PrintFor} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessPrintFor(PrintFor obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link PrintFor} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessPrintFor(PrintFor obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link ProjectionExpression} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkProjectionExpression(ProjectionExpression obj, T arg) {
        precrawlProjectionExpression(obj, arg);
        Expression _child = obj.getChild();
        walkExpression(_child, arg);
        Expression _index = obj.getIndex();
        walkExpression(_index, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        CifType _type = obj.getType();
        walkCifType(_type, arg);
        postcrawlProjectionExpression(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link ProjectionExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlProjectionExpression(ProjectionExpression obj, T arg) {
        precrawlExpression(obj, arg);
        preprocessProjectionExpression(obj, arg);
    }

    /**
     * Post-crawling function for the {@link ProjectionExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlProjectionExpression(ProjectionExpression obj, T arg) {
        postprocessProjectionExpression(obj, arg);
        postcrawlExpression(obj, arg);
    }

    /**
     * Pre-processing function for the {@link ProjectionExpression} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessProjectionExpression(ProjectionExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link ProjectionExpression} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessProjectionExpression(ProjectionExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link RealExpression} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkRealExpression(RealExpression obj, T arg) {
        precrawlRealExpression(obj, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        CifType _type = obj.getType();
        walkCifType(_type, arg);
        postcrawlRealExpression(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link RealExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlRealExpression(RealExpression obj, T arg) {
        precrawlExpression(obj, arg);
        preprocessRealExpression(obj, arg);
    }

    /**
     * Post-crawling function for the {@link RealExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlRealExpression(RealExpression obj, T arg) {
        postprocessRealExpression(obj, arg);
        postcrawlExpression(obj, arg);
    }

    /**
     * Pre-processing function for the {@link RealExpression} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessRealExpression(RealExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link RealExpression} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessRealExpression(RealExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link RealType} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkRealType(RealType obj, T arg) {
        precrawlRealType(obj, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        postcrawlRealType(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link RealType} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlRealType(RealType obj, T arg) {
        precrawlCifType(obj, arg);
        preprocessRealType(obj, arg);
    }

    /**
     * Post-crawling function for the {@link RealType} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlRealType(RealType obj, T arg) {
        postprocessRealType(obj, arg);
        postcrawlCifType(obj, arg);
    }

    /**
     * Pre-processing function for the {@link RealType} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessRealType(RealType obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link RealType} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessRealType(RealType obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link ReceivedExpression} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkReceivedExpression(ReceivedExpression obj, T arg) {
        precrawlReceivedExpression(obj, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        CifType _type = obj.getType();
        walkCifType(_type, arg);
        postcrawlReceivedExpression(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link ReceivedExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlReceivedExpression(ReceivedExpression obj, T arg) {
        precrawlExpression(obj, arg);
        preprocessReceivedExpression(obj, arg);
    }

    /**
     * Post-crawling function for the {@link ReceivedExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlReceivedExpression(ReceivedExpression obj, T arg) {
        postprocessReceivedExpression(obj, arg);
        postcrawlExpression(obj, arg);
    }

    /**
     * Pre-processing function for the {@link ReceivedExpression} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessReceivedExpression(ReceivedExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link ReceivedExpression} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessReceivedExpression(ReceivedExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link ReturnFuncStatement} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkReturnFuncStatement(ReturnFuncStatement obj, T arg) {
        precrawlReturnFuncStatement(obj, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        List<Expression> _values = obj.getValues();
        for (Expression x: _values) {
            walkExpression(x, arg);
        }
        postcrawlReturnFuncStatement(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link ReturnFuncStatement} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlReturnFuncStatement(ReturnFuncStatement obj, T arg) {
        precrawlFunctionStatement(obj, arg);
        preprocessReturnFuncStatement(obj, arg);
    }

    /**
     * Post-crawling function for the {@link ReturnFuncStatement} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlReturnFuncStatement(ReturnFuncStatement obj, T arg) {
        postprocessReturnFuncStatement(obj, arg);
        postcrawlFunctionStatement(obj, arg);
    }

    /**
     * Pre-processing function for the {@link ReturnFuncStatement} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessReturnFuncStatement(ReturnFuncStatement obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link ReturnFuncStatement} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessReturnFuncStatement(ReturnFuncStatement obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link SelfExpression} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkSelfExpression(SelfExpression obj, T arg) {
        precrawlSelfExpression(obj, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        CifType _type = obj.getType();
        walkCifType(_type, arg);
        postcrawlSelfExpression(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link SelfExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlSelfExpression(SelfExpression obj, T arg) {
        precrawlExpression(obj, arg);
        preprocessSelfExpression(obj, arg);
    }

    /**
     * Post-crawling function for the {@link SelfExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlSelfExpression(SelfExpression obj, T arg) {
        postprocessSelfExpression(obj, arg);
        postcrawlExpression(obj, arg);
    }

    /**
     * Pre-processing function for the {@link SelfExpression} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessSelfExpression(SelfExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link SelfExpression} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessSelfExpression(SelfExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link SetExpression} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkSetExpression(SetExpression obj, T arg) {
        precrawlSetExpression(obj, arg);
        List<Expression> _elements = obj.getElements();
        for (Expression x: _elements) {
            walkExpression(x, arg);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        CifType _type = obj.getType();
        walkCifType(_type, arg);
        postcrawlSetExpression(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link SetExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlSetExpression(SetExpression obj, T arg) {
        precrawlExpression(obj, arg);
        preprocessSetExpression(obj, arg);
    }

    /**
     * Post-crawling function for the {@link SetExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlSetExpression(SetExpression obj, T arg) {
        postprocessSetExpression(obj, arg);
        postcrawlExpression(obj, arg);
    }

    /**
     * Pre-processing function for the {@link SetExpression} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessSetExpression(SetExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link SetExpression} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessSetExpression(SetExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link SetType} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkSetType(SetType obj, T arg) {
        precrawlSetType(obj, arg);
        CifType _elementType = obj.getElementType();
        walkCifType(_elementType, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        postcrawlSetType(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link SetType} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlSetType(SetType obj, T arg) {
        precrawlCifType(obj, arg);
        preprocessSetType(obj, arg);
    }

    /**
     * Post-crawling function for the {@link SetType} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlSetType(SetType obj, T arg) {
        postprocessSetType(obj, arg);
        postcrawlCifType(obj, arg);
    }

    /**
     * Pre-processing function for the {@link SetType} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessSetType(SetType obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link SetType} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessSetType(SetType obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link SliceExpression} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkSliceExpression(SliceExpression obj, T arg) {
        precrawlSliceExpression(obj, arg);
        Expression _begin = obj.getBegin();
        if (_begin != null) {
            walkExpression(_begin, arg);
        }
        Expression _child = obj.getChild();
        walkExpression(_child, arg);
        Expression _end = obj.getEnd();
        if (_end != null) {
            walkExpression(_end, arg);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        CifType _type = obj.getType();
        walkCifType(_type, arg);
        postcrawlSliceExpression(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link SliceExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlSliceExpression(SliceExpression obj, T arg) {
        precrawlExpression(obj, arg);
        preprocessSliceExpression(obj, arg);
    }

    /**
     * Post-crawling function for the {@link SliceExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlSliceExpression(SliceExpression obj, T arg) {
        postprocessSliceExpression(obj, arg);
        postcrawlExpression(obj, arg);
    }

    /**
     * Pre-processing function for the {@link SliceExpression} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessSliceExpression(SliceExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link SliceExpression} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessSliceExpression(SliceExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link Specification} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkSpecification(Specification obj, T arg) {
        precrawlSpecification(obj, arg);
        List<Component> _components = obj.getComponents();
        for (Component x: _components) {
            walkComponent(x, arg);
        }
        List<Declaration> _declarations = obj.getDeclarations();
        for (Declaration x: _declarations) {
            walkDeclaration(x, arg);
        }
        List<ComponentDef> _definitions = obj.getDefinitions();
        for (ComponentDef x: _definitions) {
            walkComponentDef(x, arg);
        }
        List<Equation> _equations = obj.getEquations();
        for (Equation x: _equations) {
            walkEquation(x, arg);
        }
        List<Expression> _initials = obj.getInitials();
        for (Expression x: _initials) {
            walkExpression(x, arg);
        }
        List<Invariant> _invariants = obj.getInvariants();
        for (Invariant x: _invariants) {
            walkInvariant(x, arg);
        }
        List<IoDecl> _ioDecls = obj.getIoDecls();
        for (IoDecl x: _ioDecls) {
            walkIoDecl(x, arg);
        }
        List<Expression> _markeds = obj.getMarkeds();
        for (Expression x: _markeds) {
            walkExpression(x, arg);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        postcrawlSpecification(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link Specification} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlSpecification(Specification obj, T arg) {
        precrawlGroup(obj, arg);
        preprocessSpecification(obj, arg);
    }

    /**
     * Post-crawling function for the {@link Specification} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlSpecification(Specification obj, T arg) {
        postprocessSpecification(obj, arg);
        postcrawlGroup(obj, arg);
    }

    /**
     * Pre-processing function for the {@link Specification} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessSpecification(Specification obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link Specification} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessSpecification(Specification obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link StdLibFunctionExpression} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkStdLibFunctionExpression(StdLibFunctionExpression obj, T arg) {
        precrawlStdLibFunctionExpression(obj, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        CifType _type = obj.getType();
        walkCifType(_type, arg);
        postcrawlStdLibFunctionExpression(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link StdLibFunctionExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlStdLibFunctionExpression(StdLibFunctionExpression obj, T arg) {
        precrawlBaseFunctionExpression(obj, arg);
        preprocessStdLibFunctionExpression(obj, arg);
    }

    /**
     * Post-crawling function for the {@link StdLibFunctionExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlStdLibFunctionExpression(StdLibFunctionExpression obj, T arg) {
        postprocessStdLibFunctionExpression(obj, arg);
        postcrawlBaseFunctionExpression(obj, arg);
    }

    /**
     * Pre-processing function for the {@link StdLibFunctionExpression} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessStdLibFunctionExpression(StdLibFunctionExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link StdLibFunctionExpression} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessStdLibFunctionExpression(StdLibFunctionExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link StringExpression} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkStringExpression(StringExpression obj, T arg) {
        precrawlStringExpression(obj, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        CifType _type = obj.getType();
        walkCifType(_type, arg);
        postcrawlStringExpression(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link StringExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlStringExpression(StringExpression obj, T arg) {
        precrawlExpression(obj, arg);
        preprocessStringExpression(obj, arg);
    }

    /**
     * Post-crawling function for the {@link StringExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlStringExpression(StringExpression obj, T arg) {
        postprocessStringExpression(obj, arg);
        postcrawlExpression(obj, arg);
    }

    /**
     * Pre-processing function for the {@link StringExpression} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessStringExpression(StringExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link StringExpression} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessStringExpression(StringExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link StringType} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkStringType(StringType obj, T arg) {
        precrawlStringType(obj, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        postcrawlStringType(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link StringType} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlStringType(StringType obj, T arg) {
        precrawlCifType(obj, arg);
        preprocessStringType(obj, arg);
    }

    /**
     * Post-crawling function for the {@link StringType} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlStringType(StringType obj, T arg) {
        postprocessStringType(obj, arg);
        postcrawlCifType(obj, arg);
    }

    /**
     * Pre-processing function for the {@link StringType} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessStringType(StringType obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link StringType} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessStringType(StringType obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link SvgCopy} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkSvgCopy(SvgCopy obj, T arg) {
        precrawlSvgCopy(obj, arg);
        Expression _id = obj.getId();
        walkExpression(_id, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        Expression _post = obj.getPost();
        if (_post != null) {
            walkExpression(_post, arg);
        }
        Expression _pre = obj.getPre();
        if (_pre != null) {
            walkExpression(_pre, arg);
        }
        SvgFile _svgFile = obj.getSvgFile();
        if (_svgFile != null) {
            walkSvgFile(_svgFile, arg);
        }
        postcrawlSvgCopy(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link SvgCopy} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlSvgCopy(SvgCopy obj, T arg) {
        precrawlIoDecl(obj, arg);
        preprocessSvgCopy(obj, arg);
    }

    /**
     * Post-crawling function for the {@link SvgCopy} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlSvgCopy(SvgCopy obj, T arg) {
        postprocessSvgCopy(obj, arg);
        postcrawlIoDecl(obj, arg);
    }

    /**
     * Pre-processing function for the {@link SvgCopy} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessSvgCopy(SvgCopy obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link SvgCopy} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessSvgCopy(SvgCopy obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link SvgFile} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkSvgFile(SvgFile obj, T arg) {
        precrawlSvgFile(obj, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        postcrawlSvgFile(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link SvgFile} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlSvgFile(SvgFile obj, T arg) {
        precrawlIoDecl(obj, arg);
        preprocessSvgFile(obj, arg);
    }

    /**
     * Post-crawling function for the {@link SvgFile} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlSvgFile(SvgFile obj, T arg) {
        postprocessSvgFile(obj, arg);
        postcrawlIoDecl(obj, arg);
    }

    /**
     * Pre-processing function for the {@link SvgFile} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessSvgFile(SvgFile obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link SvgFile} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessSvgFile(SvgFile obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link SvgIn} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkSvgIn(SvgIn obj, T arg) {
        precrawlSvgIn(obj, arg);
        SvgInEvent _event = obj.getEvent();
        if (_event != null) {
            walkSvgInEvent(_event, arg);
        }
        Expression _id = obj.getId();
        walkExpression(_id, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        SvgFile _svgFile = obj.getSvgFile();
        if (_svgFile != null) {
            walkSvgFile(_svgFile, arg);
        }
        List<Update> _updates = obj.getUpdates();
        for (Update x: _updates) {
            walkUpdate(x, arg);
        }
        postcrawlSvgIn(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link SvgIn} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlSvgIn(SvgIn obj, T arg) {
        precrawlIoDecl(obj, arg);
        preprocessSvgIn(obj, arg);
    }

    /**
     * Post-crawling function for the {@link SvgIn} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlSvgIn(SvgIn obj, T arg) {
        postprocessSvgIn(obj, arg);
        postcrawlIoDecl(obj, arg);
    }

    /**
     * Pre-processing function for the {@link SvgIn} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessSvgIn(SvgIn obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link SvgIn} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessSvgIn(SvgIn obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link SvgInEvent} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkSvgInEvent(SvgInEvent obj, T arg) {
        if (obj instanceof SvgInEventIf) {
            walkSvgInEventIf((SvgInEventIf)obj, arg);
            return;
        }
        if (obj instanceof SvgInEventSingle) {
            walkSvgInEventSingle((SvgInEventSingle)obj, arg);
            return;
        }
        String msg = "No redirect; unexpected object type: " + obj;
        throw new IllegalArgumentException(msg);
    }

    /**
     * Pre-crawling function for the {@link SvgInEvent} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlSvgInEvent(SvgInEvent obj, T arg) {
        precrawlPositionObject(obj, arg);
        preprocessSvgInEvent(obj, arg);
    }

    /**
     * Post-crawling function for the {@link SvgInEvent} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlSvgInEvent(SvgInEvent obj, T arg) {
        postprocessSvgInEvent(obj, arg);
        postcrawlPositionObject(obj, arg);
    }

    /**
     * Pre-processing function for the {@link SvgInEvent} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessSvgInEvent(SvgInEvent obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link SvgInEvent} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessSvgInEvent(SvgInEvent obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link SvgInEventIf} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkSvgInEventIf(SvgInEventIf obj, T arg) {
        precrawlSvgInEventIf(obj, arg);
        List<SvgInEventIfEntry> _entries = obj.getEntries();
        for (SvgInEventIfEntry x: _entries) {
            walkSvgInEventIfEntry(x, arg);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        postcrawlSvgInEventIf(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link SvgInEventIf} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlSvgInEventIf(SvgInEventIf obj, T arg) {
        precrawlSvgInEvent(obj, arg);
        preprocessSvgInEventIf(obj, arg);
    }

    /**
     * Post-crawling function for the {@link SvgInEventIf} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlSvgInEventIf(SvgInEventIf obj, T arg) {
        postprocessSvgInEventIf(obj, arg);
        postcrawlSvgInEvent(obj, arg);
    }

    /**
     * Pre-processing function for the {@link SvgInEventIf} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessSvgInEventIf(SvgInEventIf obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link SvgInEventIf} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessSvgInEventIf(SvgInEventIf obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link SvgInEventIfEntry} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkSvgInEventIfEntry(SvgInEventIfEntry obj, T arg) {
        precrawlSvgInEventIfEntry(obj, arg);
        Expression _event = obj.getEvent();
        walkExpression(_event, arg);
        Expression _guard = obj.getGuard();
        if (_guard != null) {
            walkExpression(_guard, arg);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        postcrawlSvgInEventIfEntry(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link SvgInEventIfEntry} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlSvgInEventIfEntry(SvgInEventIfEntry obj, T arg) {
        precrawlPositionObject(obj, arg);
        preprocessSvgInEventIfEntry(obj, arg);
    }

    /**
     * Post-crawling function for the {@link SvgInEventIfEntry} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlSvgInEventIfEntry(SvgInEventIfEntry obj, T arg) {
        postprocessSvgInEventIfEntry(obj, arg);
        postcrawlPositionObject(obj, arg);
    }

    /**
     * Pre-processing function for the {@link SvgInEventIfEntry} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessSvgInEventIfEntry(SvgInEventIfEntry obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link SvgInEventIfEntry} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessSvgInEventIfEntry(SvgInEventIfEntry obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link SvgInEventSingle} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkSvgInEventSingle(SvgInEventSingle obj, T arg) {
        precrawlSvgInEventSingle(obj, arg);
        Expression _event = obj.getEvent();
        walkExpression(_event, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        postcrawlSvgInEventSingle(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link SvgInEventSingle} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlSvgInEventSingle(SvgInEventSingle obj, T arg) {
        precrawlSvgInEvent(obj, arg);
        preprocessSvgInEventSingle(obj, arg);
    }

    /**
     * Post-crawling function for the {@link SvgInEventSingle} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlSvgInEventSingle(SvgInEventSingle obj, T arg) {
        postprocessSvgInEventSingle(obj, arg);
        postcrawlSvgInEvent(obj, arg);
    }

    /**
     * Pre-processing function for the {@link SvgInEventSingle} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessSvgInEventSingle(SvgInEventSingle obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link SvgInEventSingle} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessSvgInEventSingle(SvgInEventSingle obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link SvgMove} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkSvgMove(SvgMove obj, T arg) {
        precrawlSvgMove(obj, arg);
        Expression _id = obj.getId();
        walkExpression(_id, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        SvgFile _svgFile = obj.getSvgFile();
        if (_svgFile != null) {
            walkSvgFile(_svgFile, arg);
        }
        Expression _x = obj.getX();
        walkExpression(_x, arg);
        Expression _y = obj.getY();
        walkExpression(_y, arg);
        postcrawlSvgMove(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link SvgMove} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlSvgMove(SvgMove obj, T arg) {
        precrawlIoDecl(obj, arg);
        preprocessSvgMove(obj, arg);
    }

    /**
     * Post-crawling function for the {@link SvgMove} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlSvgMove(SvgMove obj, T arg) {
        postprocessSvgMove(obj, arg);
        postcrawlIoDecl(obj, arg);
    }

    /**
     * Pre-processing function for the {@link SvgMove} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessSvgMove(SvgMove obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link SvgMove} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessSvgMove(SvgMove obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link SvgOut} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkSvgOut(SvgOut obj, T arg) {
        precrawlSvgOut(obj, arg);
        Position _attrTextPos = obj.getAttrTextPos();
        walkPosition(_attrTextPos, arg);
        Expression _id = obj.getId();
        walkExpression(_id, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        SvgFile _svgFile = obj.getSvgFile();
        if (_svgFile != null) {
            walkSvgFile(_svgFile, arg);
        }
        Expression _value = obj.getValue();
        walkExpression(_value, arg);
        postcrawlSvgOut(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link SvgOut} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlSvgOut(SvgOut obj, T arg) {
        precrawlIoDecl(obj, arg);
        preprocessSvgOut(obj, arg);
    }

    /**
     * Post-crawling function for the {@link SvgOut} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlSvgOut(SvgOut obj, T arg) {
        postprocessSvgOut(obj, arg);
        postcrawlIoDecl(obj, arg);
    }

    /**
     * Pre-processing function for the {@link SvgOut} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessSvgOut(SvgOut obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link SvgOut} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessSvgOut(SvgOut obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link SwitchCase} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkSwitchCase(SwitchCase obj, T arg) {
        precrawlSwitchCase(obj, arg);
        Expression _key = obj.getKey();
        if (_key != null) {
            walkExpression(_key, arg);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        Expression _value = obj.getValue();
        walkExpression(_value, arg);
        postcrawlSwitchCase(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link SwitchCase} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlSwitchCase(SwitchCase obj, T arg) {
        precrawlPositionObject(obj, arg);
        preprocessSwitchCase(obj, arg);
    }

    /**
     * Post-crawling function for the {@link SwitchCase} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlSwitchCase(SwitchCase obj, T arg) {
        postprocessSwitchCase(obj, arg);
        postcrawlPositionObject(obj, arg);
    }

    /**
     * Pre-processing function for the {@link SwitchCase} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessSwitchCase(SwitchCase obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link SwitchCase} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessSwitchCase(SwitchCase obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link SwitchExpression} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkSwitchExpression(SwitchExpression obj, T arg) {
        precrawlSwitchExpression(obj, arg);
        List<SwitchCase> _cases = obj.getCases();
        for (SwitchCase x: _cases) {
            walkSwitchCase(x, arg);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        CifType _type = obj.getType();
        walkCifType(_type, arg);
        Expression _value = obj.getValue();
        walkExpression(_value, arg);
        postcrawlSwitchExpression(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link SwitchExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlSwitchExpression(SwitchExpression obj, T arg) {
        precrawlExpression(obj, arg);
        preprocessSwitchExpression(obj, arg);
    }

    /**
     * Post-crawling function for the {@link SwitchExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlSwitchExpression(SwitchExpression obj, T arg) {
        postprocessSwitchExpression(obj, arg);
        postcrawlExpression(obj, arg);
    }

    /**
     * Pre-processing function for the {@link SwitchExpression} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessSwitchExpression(SwitchExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link SwitchExpression} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessSwitchExpression(SwitchExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link TauExpression} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkTauExpression(TauExpression obj, T arg) {
        precrawlTauExpression(obj, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        CifType _type = obj.getType();
        walkCifType(_type, arg);
        postcrawlTauExpression(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link TauExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlTauExpression(TauExpression obj, T arg) {
        precrawlExpression(obj, arg);
        preprocessTauExpression(obj, arg);
    }

    /**
     * Post-crawling function for the {@link TauExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlTauExpression(TauExpression obj, T arg) {
        postprocessTauExpression(obj, arg);
        postcrawlExpression(obj, arg);
    }

    /**
     * Pre-processing function for the {@link TauExpression} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessTauExpression(TauExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link TauExpression} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessTauExpression(TauExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link TimeExpression} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkTimeExpression(TimeExpression obj, T arg) {
        precrawlTimeExpression(obj, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        CifType _type = obj.getType();
        walkCifType(_type, arg);
        postcrawlTimeExpression(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link TimeExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlTimeExpression(TimeExpression obj, T arg) {
        precrawlExpression(obj, arg);
        preprocessTimeExpression(obj, arg);
    }

    /**
     * Post-crawling function for the {@link TimeExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlTimeExpression(TimeExpression obj, T arg) {
        postprocessTimeExpression(obj, arg);
        postcrawlExpression(obj, arg);
    }

    /**
     * Pre-processing function for the {@link TimeExpression} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessTimeExpression(TimeExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link TimeExpression} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessTimeExpression(TimeExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link TupleExpression} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkTupleExpression(TupleExpression obj, T arg) {
        precrawlTupleExpression(obj, arg);
        List<Expression> _fields = obj.getFields();
        for (Expression x: _fields) {
            walkExpression(x, arg);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        CifType _type = obj.getType();
        walkCifType(_type, arg);
        postcrawlTupleExpression(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link TupleExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlTupleExpression(TupleExpression obj, T arg) {
        precrawlExpression(obj, arg);
        preprocessTupleExpression(obj, arg);
    }

    /**
     * Post-crawling function for the {@link TupleExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlTupleExpression(TupleExpression obj, T arg) {
        postprocessTupleExpression(obj, arg);
        postcrawlExpression(obj, arg);
    }

    /**
     * Pre-processing function for the {@link TupleExpression} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessTupleExpression(TupleExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link TupleExpression} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessTupleExpression(TupleExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link TupleType} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkTupleType(TupleType obj, T arg) {
        precrawlTupleType(obj, arg);
        List<Field> _fields = obj.getFields();
        for (Field x: _fields) {
            walkField(x, arg);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        postcrawlTupleType(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link TupleType} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlTupleType(TupleType obj, T arg) {
        precrawlCifType(obj, arg);
        preprocessTupleType(obj, arg);
    }

    /**
     * Post-crawling function for the {@link TupleType} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlTupleType(TupleType obj, T arg) {
        postprocessTupleType(obj, arg);
        postcrawlCifType(obj, arg);
    }

    /**
     * Pre-processing function for the {@link TupleType} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessTupleType(TupleType obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link TupleType} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessTupleType(TupleType obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link TypeDecl} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkTypeDecl(TypeDecl obj, T arg) {
        precrawlTypeDecl(obj, arg);
        List<Annotation> _annotations = obj.getAnnotations();
        for (Annotation x: _annotations) {
            walkAnnotation(x, arg);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        CifType _type = obj.getType();
        walkCifType(_type, arg);
        postcrawlTypeDecl(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link TypeDecl} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlTypeDecl(TypeDecl obj, T arg) {
        precrawlDeclaration(obj, arg);
        preprocessTypeDecl(obj, arg);
    }

    /**
     * Post-crawling function for the {@link TypeDecl} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlTypeDecl(TypeDecl obj, T arg) {
        postprocessTypeDecl(obj, arg);
        postcrawlDeclaration(obj, arg);
    }

    /**
     * Pre-processing function for the {@link TypeDecl} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessTypeDecl(TypeDecl obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link TypeDecl} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessTypeDecl(TypeDecl obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link TypeRef} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkTypeRef(TypeRef obj, T arg) {
        precrawlTypeRef(obj, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        postcrawlTypeRef(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link TypeRef} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlTypeRef(TypeRef obj, T arg) {
        precrawlCifType(obj, arg);
        preprocessTypeRef(obj, arg);
    }

    /**
     * Post-crawling function for the {@link TypeRef} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlTypeRef(TypeRef obj, T arg) {
        postprocessTypeRef(obj, arg);
        postcrawlCifType(obj, arg);
    }

    /**
     * Pre-processing function for the {@link TypeRef} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessTypeRef(TypeRef obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link TypeRef} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessTypeRef(TypeRef obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link UnaryExpression} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkUnaryExpression(UnaryExpression obj, T arg) {
        precrawlUnaryExpression(obj, arg);
        Expression _child = obj.getChild();
        walkExpression(_child, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        CifType _type = obj.getType();
        walkCifType(_type, arg);
        postcrawlUnaryExpression(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link UnaryExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlUnaryExpression(UnaryExpression obj, T arg) {
        precrawlExpression(obj, arg);
        preprocessUnaryExpression(obj, arg);
    }

    /**
     * Post-crawling function for the {@link UnaryExpression} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlUnaryExpression(UnaryExpression obj, T arg) {
        postprocessUnaryExpression(obj, arg);
        postcrawlExpression(obj, arg);
    }

    /**
     * Pre-processing function for the {@link UnaryExpression} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessUnaryExpression(UnaryExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link UnaryExpression} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessUnaryExpression(UnaryExpression obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link Update} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkUpdate(Update obj, T arg) {
        if (obj instanceof Assignment) {
            walkAssignment((Assignment)obj, arg);
            return;
        }
        if (obj instanceof IfUpdate) {
            walkIfUpdate((IfUpdate)obj, arg);
            return;
        }
        String msg = "No redirect; unexpected object type: " + obj;
        throw new IllegalArgumentException(msg);
    }

    /**
     * Pre-crawling function for the {@link Update} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlUpdate(Update obj, T arg) {
        precrawlPositionObject(obj, arg);
        preprocessUpdate(obj, arg);
    }

    /**
     * Post-crawling function for the {@link Update} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlUpdate(Update obj, T arg) {
        postprocessUpdate(obj, arg);
        postcrawlPositionObject(obj, arg);
    }

    /**
     * Pre-processing function for the {@link Update} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessUpdate(Update obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link Update} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessUpdate(Update obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link VariableValue} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkVariableValue(VariableValue obj, T arg) {
        precrawlVariableValue(obj, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        List<Expression> _values = obj.getValues();
        for (Expression x: _values) {
            walkExpression(x, arg);
        }
        postcrawlVariableValue(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link VariableValue} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlVariableValue(VariableValue obj, T arg) {
        precrawlPositionObject(obj, arg);
        preprocessVariableValue(obj, arg);
    }

    /**
     * Post-crawling function for the {@link VariableValue} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlVariableValue(VariableValue obj, T arg) {
        postprocessVariableValue(obj, arg);
        postcrawlPositionObject(obj, arg);
    }

    /**
     * Pre-processing function for the {@link VariableValue} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessVariableValue(VariableValue obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link VariableValue} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessVariableValue(VariableValue obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link VoidType} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkVoidType(VoidType obj, T arg) {
        precrawlVoidType(obj, arg);
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        postcrawlVoidType(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link VoidType} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlVoidType(VoidType obj, T arg) {
        precrawlCifType(obj, arg);
        preprocessVoidType(obj, arg);
    }

    /**
     * Post-crawling function for the {@link VoidType} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlVoidType(VoidType obj, T arg) {
        postprocessVoidType(obj, arg);
        postcrawlCifType(obj, arg);
    }

    /**
     * Pre-processing function for the {@link VoidType} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessVoidType(VoidType obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link VoidType} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessVoidType(VoidType obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Walking function for the {@link WhileFuncStatement} class.
     *
     * @param obj The object to walk over.
     * @param arg The extra argument provided to the walking method.
     */
    protected void walkWhileFuncStatement(WhileFuncStatement obj, T arg) {
        precrawlWhileFuncStatement(obj, arg);
        List<Expression> _guards = obj.getGuards();
        for (Expression x: _guards) {
            walkExpression(x, arg);
        }
        Position _position = obj.getPosition();
        if (_position != null) {
            walkPosition(_position, arg);
        }
        List<FunctionStatement> _statements = obj.getStatements();
        for (FunctionStatement x: _statements) {
            walkFunctionStatement(x, arg);
        }
        postcrawlWhileFuncStatement(obj, arg);
    }

    /**
     * Pre-crawling function for the {@link WhileFuncStatement} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the pre-crawling method.
     */
    protected void precrawlWhileFuncStatement(WhileFuncStatement obj, T arg) {
        precrawlFunctionStatement(obj, arg);
        preprocessWhileFuncStatement(obj, arg);
    }

    /**
     * Post-crawling function for the {@link WhileFuncStatement} class.
     *
     * @param obj The object to crawl over.
     * @param arg The extra argument provided to the post-crawling method.
     */
    protected void postcrawlWhileFuncStatement(WhileFuncStatement obj, T arg) {
        postprocessWhileFuncStatement(obj, arg);
        postcrawlFunctionStatement(obj, arg);
    }

    /**
     * Pre-processing function for the {@link WhileFuncStatement} class.
     *
     * @param obj The object to pre-process.
     * @param arg The extra argument provided to the pre-processing method.
     */
    protected void preprocessWhileFuncStatement(WhileFuncStatement obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }

    /**
     * Post-processing function for the {@link WhileFuncStatement} class.
     *
     * @param obj The object to post-process.
     * @param arg The extra argument provided to the post-processing method.
     */
    protected void postprocessWhileFuncStatement(WhileFuncStatement obj, T arg) {
        // Derived classes may override this method to do actual processing.
    }
}
