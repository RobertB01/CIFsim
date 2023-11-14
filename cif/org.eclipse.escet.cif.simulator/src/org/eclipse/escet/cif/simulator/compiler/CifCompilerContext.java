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

package org.eclipse.escet.cif.simulator.compiler;

import static org.eclipse.escet.cif.common.CifScopeUtils.getUniqueName;
import static org.eclipse.escet.cif.common.CifTextUtils.getAbsName;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEvent;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newSpecification;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.str;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.escet.cif.common.CifCollectUtils;
import org.eclipse.escet.cif.common.CifEnumUtils;
import org.eclipse.escet.cif.common.CifEventUtils;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.CifValueUtils;
import org.eclipse.escet.cif.common.TypeEqHashWrap;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Constant;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumLiteral;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.functions.Function;
import org.eclipse.escet.cif.metamodel.cif.functions.InternalFunction;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.DictType;
import org.eclipse.escet.cif.metamodel.cif.types.EnumType;
import org.eclipse.escet.cif.metamodel.cif.types.Field;
import org.eclipse.escet.cif.metamodel.cif.types.FuncType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.cif.metamodel.cif.types.ListType;
import org.eclipse.escet.cif.metamodel.cif.types.RealType;
import org.eclipse.escet.cif.metamodel.cif.types.SetType;
import org.eclipse.escet.cif.metamodel.cif.types.StringType;
import org.eclipse.escet.cif.metamodel.cif.types.TupleType;
import org.eclipse.escet.cif.simulator.options.CompileOnlyOption;
import org.eclipse.escet.cif.simulator.options.CompiledCodeFileOption;
import org.eclipse.escet.cif.simulator.output.DebugOutputOption;
import org.eclipse.escet.cif.simulator.output.DebugOutputType;
import org.eclipse.escet.cif.simulator.runtime.model.RuntimeSpec;
import org.eclipse.escet.common.app.framework.Application;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.exceptions.InputOutputException;
import org.eclipse.escet.common.app.framework.javacompiler.JavaCompilerOption;
import org.eclipse.escet.common.app.framework.javacompiler.JavaInputFileObject;
import org.eclipse.escet.common.app.framework.javacompiler.ResourceClassLoader;
import org.eclipse.escet.common.app.framework.javacompiler.RuntimeClassLoader;
import org.eclipse.escet.common.app.framework.javacompiler.RuntimeJavaCompiler;
import org.eclipse.escet.common.app.framework.javacompiler.RuntimeJavaCompilerException;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Pair;
import org.eclipse.escet.common.java.PairTextComparer;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;
import org.eclipse.ui.PlatformUI;

/** CIF compiler context. Maintains all state for the {@link CifCompiler}. */
public class CifCompilerContext {
    /** Version resource name. */
    public static final String VERSION_RES_NAME = "version.dat";

    /** Prefix for methods for algebraic variables. */
    public static final String ALG_MTHD_PREFIX = "a";

    /** Prefix for classes for automata. */
    public static final String AUT_CLS_PREFIX = "Aut";

    /** Prefix for fields for constants. */
    public static final String CONST_FLD_PREFIX = "C";

    /** Prefix for methods for derivatives. */
    public static final String DER_MTHD_PREFIX = "d";

    /** Prefix for classes for enumeration declarations. */
    public static final String ENUM_DECL_CLS_PREFIX = "E";

    /** Prefix for values for enumeration literals. */
    public static final String ENUM_LIT_CONST_PREFIX = "elit";

    /** Prefix for classes for events. */
    public static final String EVENT_CLS_PREFIX = "Event";

    /** Prefix for fields for events. */
    public static final String EVENT_FLD_PREFIX = "evt";

    /** Prefix for classes for functions. */
    public static final String FUNC_CLS_PREFIX = "F";

    /** Prefix for fields for functions. */
    public static final String FUNC_FLD_PREFIX = "f";

    /** Prefix for fields for fields (from tuple types). */
    public static final String TUPLE_TYPE_FLD_PREFIX = "field";

    /** Prefix for classes for function types. */
    public static final String FUNC_TYPE_CLS_PREFIX = "FuncType";

    /** Prefix for method names for default values for container types. */
    public static final String DEFAULT_VALUE_MTHD_PREFIX = "dv";

    /** Prefix for fields for location pointer variables. */
    public static final String LP_FLD_PREFIX = "lp";

    /** Prefix for method parameters for function parameters. */
    public static final String FUNC_PARAM_MTHD_PARAM_PREFIX = "p";

    /** Prefix for fields for automata sub-states. */
    public static final String AUT_SUB_STATE_FLD_PREFIX = "s";

    /** Prefix for classes for tuple types. */
    public static final String TUPLE_TYPE_CLS_PREFIX = "TupleType";

    /** Prefix for fields for continuous variables. */
    public static final String CONT_VAR_FLD_PREFIX = "v";

    /** Prefix for fields for discrete variables. */
    public static final String DISC_VAR_FLD_PREFIX = "v";

    /** Prefix for fields for local variables of functions. */
    public static final String FUNCVAR_VAR_PREFIX = "v";

    /** Prefix for fields for input variables. */
    public static final String INPUT_VAR_FLD_PREFIX = "v";

    /** The name of the literal reader class. */
    public static final String LITERAL_READER_CLS_NAME = "LiteralReader";

    /** Prefix for literal reader method names. */
    public static final String LITERAL_READER_MTHD_PREFIX = "read";

    /** Prefix for literal data files. */
    public static final String LITERAL_FILE_PREFIX = "literal";

    /** The name of the variable holding the received communication value. */
    public static final String RCVD_VALUE_VAR_NAME = "rcvd";

    /** The package in which to generate the Java code. Must not be empty. */
    public static final String PACKAGE = "cifcode";

    /** The Java type to use for location pointer variables. */
    public static final String LOC_POINTER_TYPE = "int";

    /**
     * The name to use for the sub-state field for the continuous variables declared outside of the automata (including
     * variable 'time').
     */
    public static final String CONT_SUB_STATE_FIELD_NAME = "s";

    /** The name to use for the sub-state field for the input variables. */
    public static final String INPUT_SUB_STATE_FIELD_NAME = "i";

    /** The name of the debug project. */
    public static final String DBG_PROJ_NAME = "org.eclipse.escet.cif.simulator.debug";

    /** The name of the debug simulator Java file. */
    public static final String DBG_SIM_CLS_NAME = "DebugSimulator";

    /** File extension (excluding {@code "."}) of location names resource files. */
    public static final String FILE_EXT_LOC_NAMES = "locnames";

    /** File extension (excluding {@code "."}) of edge data resource files. */
    public static final String FILE_EXT_EDGE_DATA = "edgedata";

    /** The 'tau' event. */
    public final Event tauEvent = newEvent(null, null, "tau", null, null);

    /** The application compiling the CIF specification. Is {@code null} if not yet or no longer available. */
    public Application<?> app;

    /**
     * The specification being compiled. Component definitions/instantiations are not supported. Is {@code null} until
     * set via the {@link #setSpecification} method.
     */
    private Specification spec;

    /**
     * The absolute local file system path of the directory that contains the CIF specification for which code is
     * generated. Is {@code null} until set via the {@link #setSpecification} method.
     */
    private String specFileDir;

    /**
     * The events of the specification. Is {@code null} until computed (when needed). The events are sorted on their
     * absolute names. The last event is always the {@link #tauEvent tau event}.
     *
     * @see #getEvents
     */
    private List<Event> events = null;

    /**
     * The input variables of the specification. Is {@code null} until computed (when needed). The variables are sorted
     * on their absolute names.
     *
     * @see #getInputVariables
     */
    private List<InputVariable> inputVariables = null;

    /**
     * The algebraic variables of the specification. Is {@code null} until computed (when needed). The variables are
     * sorted on their absolute names.
     *
     * @see #getAlgVars
     */
    private List<AlgVariable> algVars = null;

    /**
     * The discrete, input and continuous variables that are part of the state. Is {@code null} until computed (when
     * needed). The variables are sorted on their absolute names.
     *
     * @see #getStateVars
     */
    private List<Declaration> stateVars = null;

    /**
     * The automata of the specification. Is {@code null} until computed (when needed). The automata are sorted on their
     * absolute names.
     *
     * @see #getAutomata
     */
    private List<Automaton> automata = null;

    /**
     * Mapping from automata to their alphabets. Entries are added as needed.
     *
     * @see #getAlphabet
     */
    private Map<Automaton, Set<Event>> alphabets = map();

    /**
     * Mapping from automata to their send alphabets. Entries are added as needed.
     *
     * @see #getSendAlphabet
     */
    private Map<Automaton, Set<Event>> sendAlphabets = map();

    /**
     * Mapping from automata to their receive alphabets. Entries are added as needed.
     *
     * @see #getReceiveAlphabet
     */
    private Map<Automaton, Set<Event>> receiveAlphabets = map();

    /**
     * Mapping from automata to their monitors. Entries are added as needed.
     *
     * @see #getMonitors
     */
    private Map<Automaton, Set<Event>> monitors = map();

    /**
     * Mapping from events to the automata that synchronize over them. Does not contain the 'tau' event. Entries are
     * added as needed.
     *
     * @see #getSyncAuts
     */
    private Map<Event, List<Automaton>> syncAuts = map();

    /**
     * Mapping from events to the automata that send over them. Only contains the communication events. Entries are
     * added as needed.
     *
     * @see #getSendAuts
     */
    private Map<Event, List<Automaton>> sendAuts = map();

    /**
     * Mapping from events to the automata that receive over them. Only contains the communication events. Entries are
     * added as needed.
     *
     * @see #getRecvAuts
     */
    private Map<Event, List<Automaton>> recvAuts = map();

    /** Whether the specification has a 'tau' edge. See also {@link RuntimeSpec#hasTauEdge}. */
    public boolean hasTauEdge = false;

    /**
     * Mapping from enumeration declarations to their representatives. Is {@code null} until computed (when needed).
     *
     * @see #getEnumDeclReprs
     */
    private Map<EnumDecl, EnumDecl> enumDeclReprs = null;

    /**
     * Mapping from {@link CifTypeUtils#normalizeType normalized} container types to their unique generated default
     * value method names. Entries are added as needed.
     *
     * @see #getDefaultValueMethodName
     */
    private Map<TypeEqHashWrap, String> defaultValueNames = map();

    /**
     * The current number of method names generated for default values of container types. If a container tuple type is
     * encountered for which the default value is needed, this count is incremented by one, and that new count is used
     * as unique number.
     *
     * @see #getDefaultValueMethodName
     */
    private int defaultValueNamesCount = 0;

    /**
     * The current number of literal data files that have been generated. If a literal data file is generated by
     * {@link LiteralCodeGenerator#gencodeLiteral}, this count is incremented by one, and that new count is used as
     * unique number.
     *
     * @see #getLiteralDataFileName
     */
    private int literalDataFileCount = 0;

    /**
     * Mapping from function types to their unique generated names. Entries are added as needed.
     *
     * @see #getFuncTypeClassName
     */
    private Map<TypeEqHashWrap, String> funcTypes = map();

    /**
     * Mapping from literal types to their unique read method names. Entries are added as needed.
     *
     * @see #getLiteralReadMethodName
     */
    public Map<TypeEqHashWrap, String> literalTypes = map();

    /**
     * Mapping from tuple types to their unique generated names. Entries are added as needed.
     *
     * @see #getTupleTypeClassName
     */
    private Map<TypeEqHashWrap, String> tupleTypes = map();

    /**
     * The current number of function types for which unique names are generated. If a new function type is encountered,
     * this count is incremented by one, and that new count is used as unique number.
     *
     * @see #getFuncTypeClassName
     */
    private int funcTypeCount = 0;

    /**
     * The current number of tuple types for which unique names are generated. If a new tuple type is encountered, this
     * count is incremented by one, and that new count is used as unique number.
     *
     * @see #getTupleTypeClassName
     */
    private int tupleTypeCount = 0;

    /** List of functions generated for the default values of function types. Entries are added as needed. */
    private List<InternalFunction> funcTypeDefaultValueFuncs = list();

    /**
     * Dummy specification, to use as the root of all functions generated for the default values of function types. See
     * {@link #funcTypeDefaultValueFuncs} and {@link #getDefaultValue}. The specification is modified in-place.
     */
    private final Specification dummyFuncSpec = newSpecification();

    /** The number of SVG files, or {@code -1} if not yet set. */
    public int svgFileCount = -1;

    /** The number of print files, or {@code -1} if not yet set. */
    public int printFileCount = -1;

    /** Whether a 'Sampler' class should be generated by the {@link SamplerCodeGenerator}. */
    public boolean needSampler = false;

    /** Mapping from class names to their generated Java code file. */
    private Map<String, JavaCodeFile> code = map();

    /** Mapping from absolute resource paths to the data for the resource. */
    private Map<String, ByteArrayOutputStream> resources = map();

    /** Generated names already in use. */
    private Set<String> usedNames = set();

    /**
     * Mapping from prefixes to mappings of CIF metamodel objects to their unique generated names. Entries are added as
     * needed.
     */
    private Map<String, Map<PositionObject, String>> objects = map();

    /** The Java compiler to use to compile the generated code. */
    private RuntimeJavaCompiler compiler;

    /**
     * Generator used to create successive unique numbers to use as postfixes for the names of extra methods in
     * {@link ExprCodeGeneratorResult}.
     */
    public final AtomicInteger exprCodeGenExtraMethodCounter = new AtomicInteger();

    /**
     * Sets the specification being compiled.
     *
     * @param spec The specification being compiled. Component definitions/instantiations are not supported.
     * @param specFileDir The absolute local file system path of the directory that contains the CIF specification for
     *     which code is generated.
     */
    public void setSpecification(Specification spec, String specFileDir) {
        Assert.check(this.spec == null);
        Assert.notNull(spec);
        this.spec = spec;
        this.specFileDir = specFileDir;
    }

    /**
     * Returns the absolute local file system path of the directory that contains the CIF specification for which code
     * is generated.
     *
     * @return The absolute local file system path of the directory that contains the CIF specification for which code
     *     is generated.
     * @throws IllegalStateException If the path is not yet available.
     * @see #specFileDir
     */
    public String getSpecFileDir() {
        if (specFileDir == null) {
            throw new IllegalStateException();
        }
        return specFileDir;
    }

    /**
     * Adds a new code file.
     *
     * @param name The unique Java type name, without package name.
     * @return The newly created (empty) Java code file.
     */
    public JavaCodeFile addCodeFile(String name) {
        JavaCodeFile file = new JavaCodeFile(PACKAGE, name);
        file.imports.addAll(getImports());
        JavaCodeFile previous = code.put(name, file);
        Assert.check(previous == null);
        return file;
    }

    /**
     * Adds a new resource file.
     *
     * @param resPath The absolute resource path.
     * @return The newly created (empty) output stream for the resource.
     */
    public ByteArrayOutputStream addResourceFile(String resPath) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ByteArrayOutputStream previous = resources.put(resPath, stream);
        Assert.check(previous == null);
        return stream;
    }

    /**
     * Returns the path to the location names resource for an automaton.
     *
     * @param className The absolute name of the class generated for the automaton.
     * @return The absolute resource path.
     */
    public static String getLocNamesResourcePath(String className) {
        return className.replace('.', '/') + "." + FILE_EXT_LOC_NAMES;
    }

    /**
     * Returns the path to the edge data resource for an automaton.
     *
     * @param className The absolute name of the class generated for the automaton.
     * @return The absolute resource path.
     */
    public static String getEdgeDataResourcePath(String className) {
        return className.replace('.', '/') + "." + FILE_EXT_EDGE_DATA;
    }

    /**
     * Returns the unique generated name for the given object. If the object was not previously added to this context,
     * it is given a unique name, and is added. Unique names are based on the absolute name of the object. If the object
     * was previously added, the unique name is returned.
     *
     * @param obj The object for which to get the unique name. Must be an object with a name.
     * @param prefix The prefix to use for the name. One object can have multiple names, for different prefixes.
     * @param abs Whether to use the absolute name of the object.
     * @return The unique generated name of the object.
     */
    private String getName(PositionObject obj, String prefix, boolean abs) {
        // Get mapping for the prefix, and add if not yet present.
        Map<PositionObject, String> prefixMap = objects.get(prefix);
        if (prefixMap == null) {
            prefixMap = map();
            objects.put(prefix, prefixMap);
        }

        // Get unique name for the object, if present.
        String name = prefixMap.get(obj);

        // If object not yet present, add it.
        if (name == null) {
            // Get candidate name.
            if (obj == tauEvent) {
                name = "tau";
            } else if (abs) {
                name = getAbsName(obj, false).replace('.', '_');
            } else {
                name = CifTextUtils.getName(obj);
            }
            name = prefix + "_" + name;

            // Get final name.
            if (usedNames.contains(name)) {
                name = getUniqueName(name, usedNames, Collections.emptySet());
            }

            // Add name.
            usedNames.add(name);

            // Add object.
            prefixMap.put(obj, name);
        }

        // Return the unique name.
        return name;
    }

    /**
     * Returns the unique generated name for the enumeration class that is generated for the given enumeration
     * declaration.
     *
     * @param enumDecl The enumeration declaration for which to get the unique name.
     * @return The unique generated name of the enumeration class.
     */
    public String getEnumClassName(EnumDecl enumDecl) {
        // Not all enumeration declarations have a class, only the representatives do.
        return getName(getEnumDeclReprs().get(enumDecl), ENUM_DECL_CLS_PREFIX, true);
    }

    /**
     * Returns the generated name for the enumeration constant that is generated for the given enumeration literal.
     *
     * @param enumLit The enumeration literal for which to get the name.
     * @return The generated name of the enumeration constant.
     */
    public String getEnumConstName(EnumLiteral enumLit) {
        // We generate a name that is locally unique within the enumeration class. All references to the enumeration
        // constant should be prefixed with the enumeration class name.
        return ENUM_LIT_CONST_PREFIX + "_" + enumLit.getName();
    }

    /**
     * Returns the unique generated name for the event class that is generated for the given event.
     *
     * @param event The event for which to get the unique name.
     * @return The unique generated name of the event class.
     */
    public String getEventClassName(Event event) {
        return getName(event, EVENT_CLS_PREFIX, true);
    }

    /**
     * Returns the unique generated name for the event field that is generated for the given event, in the generated
     * specification class.
     *
     * @param event The event for which to get the unique name.
     * @return The unique generated name of the event field.
     */
    public String getEventFieldName(Event event) {
        return getName(event, EVENT_FLD_PREFIX, true);
    }

    /**
     * Returns the unique generated name for the automaton class that is generated for the given automaton.
     *
     * @param aut The automaton for which to get the unique name.
     * @return The unique generated name of the automaton class.
     */
    public String getAutClassName(Automaton aut) {
        return getName(aut, AUT_CLS_PREFIX, true);
    }

    /**
     * Returns the unique generated name for the field that is generated for the sub-state (as part of the global
     * state), for the given automaton.
     *
     * @param aut The automaton for which to get the unique name.
     * @return The unique generated name of the sub-state field.
     */
    public String getAutSubStateFieldName(Automaton aut) {
        return getName(aut, AUT_SUB_STATE_FLD_PREFIX, true);
    }

    /**
     * Returns the unique generated name for the field that is generated for the location pointer variable of the given
     * automaton.
     *
     * @param aut The automaton for which to get the unique name.
     * @return The unique generated name of the location pointer variable field.
     */
    public String getLocationPointerFieldName(Automaton aut) {
        return getName(aut, LP_FLD_PREFIX, true);
    }

    /**
     * Returns the unique generated name for the field that is generated for the given discrete variable.
     *
     * @param var The discrete variable for which to get the unique name.
     * @return The unique generated name of the discrete variable field.
     */
    public String getDiscVarFieldName(DiscVariable var) {
        return getName(var, DISC_VAR_FLD_PREFIX, false);
    }

    /**
     * Returns the unique generated name for the field that is generated for the given input variable.
     *
     * @param var The input variable for which to get the unique name.
     * @return The unique generated name of the input variable field.
     */
    public String getInputVarFieldName(InputVariable var) {
        // The absolute name is used to avoid conflicts, since all input variables are placed in the same sub state.
        return getName(var, INPUT_VAR_FLD_PREFIX, true);
    }

    /**
     * Returns the unique generated name for the field that is generated for the given continuous variable. This applies
     * only to continuous variables declared in automata.
     *
     * @param var The continuous variable for which to get the unique name.
     * @return The unique generated name of the continuous variable field.
     */
    public String getContVarFieldName(ContVariable var) {
        EObject parent = var.eContainer();
        if (parent instanceof Automaton) {
            // For automata, the local name is unique enough, as we always
            // prefix with the sub-state name.
            return getName(var, CONT_VAR_FLD_PREFIX, false);
        } else {
            // For continuous variables declared outside the automata, the
            // absolute name is used to avoid conflicts.
            return getName(var, CONT_VAR_FLD_PREFIX, true);
        }
    }

    /**
     * Returns the unique generated name for the field that is generated for the sub state that contains the given
     * continuous variable.
     *
     * @param var The continuous variable for which to get the unique name.
     * @return The unique generated name of the sub state field.
     */
    public String getContVarSubStateName(ContVariable var) {
        EObject parent = var.eContainer();
        if (parent instanceof Automaton) {
            return getAutSubStateFieldName((Automaton)parent);
        } else {
            return CONT_SUB_STATE_FIELD_NAME;
        }
    }

    /**
     * Returns the unique generated name for the method that is generated for the derivative of the given continuous
     * variable. This applies to both continuous variables declared in automata, as well as those declared outside
     * automata.
     *
     * @param var The continuous variable for which to get the unique name.
     * @return The unique generated name of the derivative method.
     */
    public String getDerivativeMethodName(ContVariable var) {
        return getName(var, DER_MTHD_PREFIX, true);
    }

    /**
     * Returns the unique generated name for the method that is generated for the given algebraic variable.
     *
     * @param var The algebraic variable for which to get the unique name.
     * @return The unique generated name of the algebraic variable method.
     */
    public String getAlgVarMethodName(AlgVariable var) {
        return getName(var, ALG_MTHD_PREFIX, true);
    }

    /**
     * Returns the unique generated name for the field that is generated for the given constant.
     *
     * @param constant The constant for which to get the unique name.
     * @return The unique generated name of the constant field.
     */
    public String getConstFieldName(Constant constant) {
        return getName(constant, CONST_FLD_PREFIX, true);
    }

    /**
     * Returns the unique generated name for the class that is generated for the given function.
     *
     * @param function The function for which to get the unique name.
     * @return The unique generated name of the function class.
     */
    public String getFuncClassName(Function function) {
        return getName(function, FUNC_CLS_PREFIX, true);
    }

    /**
     * Returns the unique generated name for the field that is generated for the given function.
     *
     * @param function The function for which to get the unique name.
     * @return The unique generated name of the function field.
     */
    public String getFuncFieldName(Function function) {
        return getName(function, FUNC_FLD_PREFIX, true);
    }

    /**
     * Returns the unique generated name for the method parameter that is generated for the given function parameter.
     *
     * @param param The function parameter for which to get the unique name.
     * @return The unique generated name of the method parameter.
     */
    public String getFuncParamMethodParamName(DiscVariable param) {
        // Locally, this is unique enough.
        return FUNC_PARAM_MTHD_PARAM_PREFIX + "_" + param.getName();
    }

    /**
     * Returns the unique generated name for the local variable that is generated for the given function variable.
     *
     * @param var The function variable for which to get the unique name.
     * @return The unique generated name of the local variable.
     */
    public String getFuncLocalVarName(DiscVariable var) {
        // Locally, this is unique enough.
        return FUNCVAR_VAR_PREFIX + "_" + var.getName();
    }

    /**
     * Returns the unique generated name for the class that is generated for the given function type.
     *
     * <p>
     * As a side effect, this method generates a function type class, for each new function type that is passed to this
     * method.
     * </p>
     *
     * @param funcType The function type for which to get the unique name.
     * @return The unique generated name of the function type class.
     */
    public String getFuncTypeClassName(FuncType funcType) {
        TypeEqHashWrap wrap = new TypeEqHashWrap(funcType, true);
        String name = funcTypes.get(wrap);
        if (name == null) {
            // Construct name for this new function type.
            funcTypeCount++;
            name = FUNC_TYPE_CLS_PREFIX + "_" + funcTypeCount;

            // Store name.
            funcTypes.put(wrap, name);

            // Generate a class for this function type.
            FuncCodeGenerator.gencodeFuncType(funcType, name, this);
        }
        return name;
    }

    /**
     * Returns the name for the method to use for reading literals of the given literal type.
     *
     * @param type The literal type for which to get the read method name.
     * @return The name of the read method for the literal type.
     */
    public String getLiteralReadMethodName(CifType type) {
        // No generated method for simple types.
        type = CifTypeUtils.normalizeType(type);
        if (type instanceof BoolType) {
            return "RuntimeLiteralReader.readBoolLiteral";
        } else if (type instanceof RealType) {
            return "RuntimeLiteralReader.readRealLiteral";
        } else if (type instanceof StringType) {
            return "RuntimeLiteralReader.readStringLiteral";
        } else if (type instanceof IntType) {
            if (CifTypeUtils.isRangeless((IntType)type)) {
                return "RuntimeLiteralReader.readIntLiteral";
            }
        }

        // Need generated method for container types.
        TypeEqHashWrap wrap = new TypeEqHashWrap(type, false);
        String name = literalTypes.get(wrap);
        if (name == null) {
            // Construct read method name for this new literal type.
            name = LITERAL_READER_MTHD_PREFIX + literalTypes.size();

            // Store name.
            literalTypes.put(wrap, name);

            // Process recursively.
            if (type instanceof IntType) {
                // Nothing to do.
            } else if (type instanceof EnumType) {
                // Nothing to do.
            } else if (type instanceof ListType) {
                getLiteralReadMethodName(((ListType)type).getElementType());
            } else if (type instanceof SetType) {
                getLiteralReadMethodName(((SetType)type).getElementType());
            } else if (type instanceof TupleType) {
                for (Field field: ((TupleType)type).getFields()) {
                    getLiteralReadMethodName(field.getType());
                }
            } else if (type instanceof DictType) {
                getLiteralReadMethodName(((DictType)type).getKeyType());
                getLiteralReadMethodName(((DictType)type).getValueType());
            } else {
                throw new RuntimeException("Unexpected literal type: " + type);
            }
        }
        return LITERAL_READER_CLS_NAME + "." + name;
    }

    /**
     * Returns the unique generated name for the class that is generated for the given tuple type.
     *
     * <p>
     * As a side effect, this method generates a tuple type class, for each new tuple type that is passed to this
     * method.
     * </p>
     *
     * @param tupleType The tuple type for which to get the unique name.
     * @return The unique generated name of the tuple type class.
     */
    public String getTupleTypeClassName(TupleType tupleType) {
        TypeEqHashWrap wrap = new TypeEqHashWrap(tupleType, true);
        String name = tupleTypes.get(wrap);
        if (name == null) {
            // Construct name for this new tuple type.
            tupleTypeCount++;
            name = TUPLE_TYPE_CLS_PREFIX + "_" + tupleTypeCount;

            // Store name.
            tupleTypes.put(wrap, name);

            // Generate a class for this tuple type.
            TupleTypeCodeGenerator.gencodeTupleType(tupleType, name, this);
        }
        return name;
    }

    /**
     * Returns the unique generated name for the field that is generated for the given tuple type field.
     *
     * <p>
     * If possible, use {@link #getTupleTypeFieldFieldName(TupleType, int)} instead, as it has better performance.
     * </p>
     *
     * @param field The tuple type field for which to get the unique name.
     * @return The unique generated name of the tuple type field field.
     */
    public String getTupleTypeFieldFieldName(Field field) {
        TupleType tupleType = (TupleType)field.eContainer();
        int idx = tupleType.getFields().indexOf(field);
        return getTupleTypeFieldFieldName(tupleType, idx);
    }

    /**
     * Returns the unique generated name for the field that is generated for the given tuple type field.
     *
     * @param tupleType The tuple type that contains the field for which to get the unique name.
     * @param idx The 0-based index of the field in the tuple type.
     * @return The unique generated name of the tuple type field field.
     */
    public String getTupleTypeFieldFieldName(TupleType tupleType, int idx) {
        // Note that we don't use the names of tuple fields for the Java
        // fields, as different tuple types that are compatible may have
        // differently named fields, but we generate only one class.
        return TUPLE_TYPE_FLD_PREFIX + "_" + idx;
    }

    /**
     * Returns the textual representation of the value that indicates the given location, in the generated Java code.
     *
     * <p>
     * If the returned Java expression code is complex, the returned code should already be surrounded by parentheses.
     * </p>
     *
     * @param loc The location.
     * @return The textual representation of the value that indicates the given location, in the generated Java code.
     */
    public String getLocationValueText(Location loc) {
        Automaton aut = (Automaton)loc.eContainer();
        int idx = aut.getLocations().indexOf(loc);
        return getLocationValueText(loc, idx);
    }

    /**
     * Returns the textual representation of the value that indicates the given location, in the generated Java code.
     *
     * <p>
     * If the returned Java expression code is complex, the returned code should already be surrounded by parentheses.
     * </p>
     *
     * @param loc The location.
     * @param idx The 0-based index of the location into its automaton.
     * @return The textual representation of the value that indicates the given location, in the generated Java code.
     */
    public String getLocationValueText(Location loc, int idx) {
        return Integer.toString(idx);
    }

    /**
     * Returns the events of the specification, sorted on their absolute names. The last event is always the
     * {@link #tauEvent tau event}.
     *
     * @return The automata of the specification.
     */
    public List<Event> getEvents() {
        if (events == null) {
            List<Pair<String, Event>> eventTuples = list();
            EventCodeGenerator.collectEvents(spec, eventTuples);
            Collections.sort(eventTuples, new PairTextComparer<Event>());
            events = listc(eventTuples.size() + 1);
            for (Pair<String, Event> eventTuple: eventTuples) {
                events.add(eventTuple.right);
            }
            events.add(tauEvent);
        }
        return events;
    }

    /**
     * Returns the algebraic variables of the specification, sorted on their absolute names.
     *
     * @return The algebraic variables of the specification.
     */
    public List<AlgVariable> getAlgVars() {
        if (algVars == null) {
            List<Pair<String, AlgVariable>> algTuples = list();
            AlgVarCodeGenerator.collectAlgVars(spec, algTuples);
            Collections.sort(algTuples, new PairTextComparer<AlgVariable>());
            algVars = listc(algTuples.size());
            for (Pair<String, AlgVariable> algTuple: algTuples) {
                algVars.add(algTuple.right);
            }
        }
        return algVars;
    }

    /**
     * Returns the input variables of the specification, sorted on their absolute names.
     *
     * @return The input variables of the specification.
     */
    public List<InputVariable> getInputVariables() {
        if (inputVariables == null) {
            List<Pair<String, InputVariable>> iVarTuples = list();
            StateCodeGenerator.collectInputVars(spec, iVarTuples);
            Collections.sort(iVarTuples, new PairTextComparer<InputVariable>());
            inputVariables = listc(iVarTuples.size());
            for (Pair<String, InputVariable> ivarTuple: iVarTuples) {
                inputVariables.add(ivarTuple.right);
            }
        }
        return inputVariables;
    }

    /**
     * Returns the discrete, input and continuous variables that are part of the state, sorted on their absolute names.
     * Variable 'time' is not included.
     *
     * @return The discrete, input and continuous variables that are part of the state.
     */
    public List<Declaration> getStateVars() {
        if (stateVars == null) {
            List<Pair<String, Declaration>> varTuples = list();
            StateCodeGenerator.collectStateVars(spec, varTuples);
            Collections.sort(varTuples, new PairTextComparer<Declaration>());
            stateVars = listc(varTuples.size());
            for (Pair<String, Declaration> varTuple: varTuples) {
                stateVars.add(varTuple.right);
            }
        }
        return stateVars;
    }

    /**
     * Returns the automata of the specification, sorted on their absolute names.
     *
     * @return The automata of the specification.
     */
    public List<Automaton> getAutomata() {
        if (automata == null) {
            List<Pair<String, Automaton>> autTuples = list();
            AutomatonCodeGenerator.collectAutomata(spec, autTuples);
            Collections.sort(autTuples, new PairTextComparer<Automaton>());
            automata = listc(autTuples.size());
            for (Pair<String, Automaton> autTuple: autTuples) {
                automata.add(autTuple.right);
            }
        }
        return automata;
    }

    /**
     * Returns the alphabet of the given automaton.
     *
     * @param aut The automaton for which to return the alphabet.
     * @return The alphabet of the given automaton.
     */
    public Set<Event> getAlphabet(Automaton aut) {
        Set<Event> alphabet = alphabets.get(aut);
        if (alphabet == null) {
            alphabet = CifEventUtils.getAlphabet(aut);
            alphabets.put(aut, alphabet);
        }
        return alphabet;
    }

    /**
     * Returns the send alphabet of the given automaton.
     *
     * @param aut The automaton for which to return the send alphabet.
     * @return The send alphabet of the given automaton.
     */
    public Set<Event> getSendAlphabet(Automaton aut) {
        Set<Event> alphabet = sendAlphabets.get(aut);
        if (alphabet == null) {
            alphabet = CifEventUtils.getSendAlphabet(aut);
            sendAlphabets.put(aut, alphabet);
        }
        return alphabet;
    }

    /**
     * Returns the receive alphabet of the given automaton.
     *
     * @param aut The automaton for which to return the receive alphabet.
     * @return The receive alphabet of the given automaton.
     */
    public Set<Event> getReceiveAlphabet(Automaton aut) {
        Set<Event> alphabet = receiveAlphabets.get(aut);
        if (alphabet == null) {
            alphabet = CifEventUtils.getReceiveAlphabet(aut);
            receiveAlphabets.put(aut, alphabet);
        }
        return alphabet;
    }

    /**
     * Returns the monitors of the given automaton.
     *
     * @param aut The automaton for which to return the monitors.
     * @return The monitors of the given automaton.
     */
    public Set<Event> getMonitors(Automaton aut) {
        Set<Event> monitorsAut = monitors.get(aut);
        if (monitorsAut == null) {
            Set<Event> alphabet = getAlphabet(aut);
            monitorsAut = CifEventUtils.getMonitors(aut, alphabet);
            monitors.put(aut, monitorsAut);
        }
        return monitorsAut;
    }

    /**
     * Returns the automata that synchronize over the given event.
     *
     * @param event The event. Must not be the 'tau' event.
     * @return The automata that synchronize over the given event.
     */
    public List<Automaton> getSyncAuts(Event event) {
        List<Automaton> rslt = syncAuts.get(event);
        if (rslt == null) {
            rslt = list();
            List<Automaton> automata = getAutomata();
            for (Automaton aut: automata) {
                Set<Event> alphabet = getAlphabet(aut);
                if (alphabet.contains(event)) {
                    rslt.add(aut);
                }
            }
            syncAuts.put(event, rslt);
        }
        return rslt;
    }

    /**
     * Returns the automata that send over the given event.
     *
     * @param event The event. Must be a channel.
     * @return The automata that send over the given event.
     */
    public List<Automaton> getSendAuts(Event event) {
        List<Automaton> rslt = sendAuts.get(event);
        if (rslt == null) {
            rslt = list();
            List<Automaton> automata = getAutomata();
            for (Automaton aut: automata) {
                Set<Event> alphabet = getSendAlphabet(aut);
                if (alphabet.contains(event)) {
                    rslt.add(aut);
                }
            }
            sendAuts.put(event, rslt);
        }
        return rslt;
    }

    /**
     * Returns the automata that receive over the given event.
     *
     * @param event The event. Must be a channel.
     * @return The automata that receive over the given event.
     */
    public List<Automaton> getRecvAuts(Event event) {
        List<Automaton> rslt = recvAuts.get(event);
        if (rslt == null) {
            rslt = list();
            List<Automaton> automata = getAutomata();
            for (Automaton aut: automata) {
                Set<Event> alphabet = getReceiveAlphabet(aut);
                if (alphabet.contains(event)) {
                    rslt.add(aut);
                }
            }
            recvAuts.put(event, rslt);
        }
        return rslt;
    }

    /**
     * Returns the mapping from enumeration declarations to their representatives.
     *
     * @return The mapping from enumeration declarations to their representatives.
     */
    public Map<EnumDecl, EnumDecl> getEnumDeclReprs() {
        if (enumDeclReprs == null) {
            List<EnumDecl> enumDecls = list();
            CifCollectUtils.collectEnumDecls(spec, enumDecls);
            enumDeclReprs = CifEnumUtils.getEnumDeclReprs(enumDecls);
        }
        return enumDeclReprs;
    }

    /**
     * Returns the mapping from {@link CifTypeUtils#normalizeType normalized} container types to their unique generated
     * default value method names.
     *
     * @return The mapping from normalized container types to their unique generated default value method names.
     */
    public Map<TypeEqHashWrap, String> getDefaultMethodNames() {
        return defaultValueNames;
    }

    /**
     * Returns the unique generated name for the method that is generated for the given container type, or {@code null}
     * if the type is not a container type.
     *
     * <p>
     * As a side effect, this method may add new container types to the {@link #defaultValueNames} mapping, for which
     * the {@link DefaultValueCodeGenerator#gencodeDefaultValues} method will later generate code.
     * </p>
     *
     * @param type The type for which to get the unique method name.
     * @return The unique generated method name, or {@code null}.
     */
    public String getDefaultValueMethodName(CifType type) {
        // Handle non-container types.
        type = CifTypeUtils.normalizeType(type);
        if (!CifTypeUtils.isContainerType(type)) {
            return null;
        }

        // Handle container types.
        TypeEqHashWrap wrap = new TypeEqHashWrap(type, false);
        String name = defaultValueNames.get(wrap);
        if (name == null) {
            // Construct method name for this new container type.
            defaultValueNamesCount++;
            name = DEFAULT_VALUE_MTHD_PREFIX + defaultValueNamesCount;

            // Store name. Code is generated later.
            defaultValueNames.put(wrap, name);

            // Handle child types. Make sure method names are generated for
            // them as well, if needed.
            if (type instanceof DictType) {
                DictType dtype = (DictType)type;
                getDefaultValueMethodName(dtype.getKeyType());
                getDefaultValueMethodName(dtype.getValueType());
            } else if (type instanceof ListType) {
                ListType ltype = (ListType)type;
                getDefaultValueMethodName(ltype.getElementType());
            } else if (type instanceof SetType) {
                SetType stype = (SetType)type;
                getDefaultValueMethodName(stype.getElementType());
            } else if (type instanceof TupleType) {
                TupleType ttype = (TupleType)type;
                for (Field field: ttype.getFields()) {
                    getDefaultValueMethodName(field.getType());
                }
            } else {
                throw new RuntimeException("Unknown container type: " + type);
            }
        }
        return name;
    }

    /**
     * Returns the unique file name to use for the next literal data file.
     *
     * <p>
     * As a side effect, this method increases {@link #literalDataFileCount}.
     * </p>
     *
     * @return The unique generated literal data file name.
     */
    public String getLiteralDataFileName() {
        literalDataFileCount++;
        return LITERAL_FILE_PREFIX + str(literalDataFileCount) + ".dat";
    }

    /**
     * Returns the default value for a given type.
     *
     * @param type The type for which to return the default value.
     * @return The default value for a given type.
     */
    public Expression getDefaultValue(CifType type) {
        // Get default value for the type.
        int countBefore = funcTypeDefaultValueFuncs.size();
        Expression rslt = CifValueUtils.getDefaultValue(type, funcTypeDefaultValueFuncs);
        int countAfter = funcTypeDefaultValueFuncs.size();

        // If new functions were generated for the default values of function
        // types, generate code for those functions.
        if (countBefore != countAfter) {
            for (int i = countBefore; i < countAfter; i++) {
                // Get function, set name, and put it in the dummy
                // specification (to allow getting the absolute name of the
                // function).
                InternalFunction func = funcTypeDefaultValueFuncs.get(i);
                Assert.check(func.getName() == null);
                func.setName("defaultValueFunc");
                dummyFuncSpec.getDeclarations().add(func);
            }
            for (int i = countBefore; i < countAfter; i++) {
                // Generate code for the function, now that all new functions
                // have a name, and are properly rooted in a specification.
                InternalFunction func = funcTypeDefaultValueFuncs.get(i);
                FuncCodeGenerator.gencodeFunc(func, this);
            }
            Assert.check(funcTypeDefaultValueFuncs.size() == countAfter);
        }

        // Return the default value for the type.
        return rslt;
    }

    /**
     * Returns the imports to use for all generic Java code files.
     *
     * @return The imports to use for all generic Java code files.
     */
    public List<String> getImports() {
        return list(
                // Java.
                "java.util.*", //
                "java.util.Map.Entry", //

                // ESCET common. Never use CifMath, always use CifSimulatorMath!
                "org.eclipse.escet.common.app.framework.exceptions.InputOutputException", //
                "org.eclipse.escet.common.app.framework.exceptions.UnsupportedException", //
                "static org.eclipse.escet.common.app.framework.output.OutputProvider.warn", //
                "org.eclipse.escet.common.java.Assert", //
                "org.eclipse.escet.common.java.Lists", //
                "org.eclipse.escet.common.java.Sets", //
                "static org.eclipse.escet.common.java.ArrayUtils.array", //
                "static org.eclipse.escet.common.java.Lists.list", //
                "static org.eclipse.escet.common.java.Lists.listc", //
                "static org.eclipse.escet.common.java.Maps.mapc", //
                "static org.eclipse.escet.common.java.Sets.set", //
                "static org.eclipse.escet.common.java.Strings.fmt", //
                "org.eclipse.escet.common.svg.SvgUtils", //

                // Apache Commons Lang.
                "org.apache.commons.lang3.StringUtils", //

                // Simulator runtime.
                "org.eclipse.escet.cif.simulator.input.*", //
                "org.eclipse.escet.cif.simulator.runtime.*", //
                "org.eclipse.escet.cif.simulator.runtime.distributions.*", //
                "org.eclipse.escet.cif.simulator.runtime.io.*", //
                "org.eclipse.escet.cif.simulator.runtime.meta.*", //
                "org.eclipse.escet.cif.simulator.runtime.model.*", //
                "org.eclipse.escet.cif.simulator.runtime.ode.*", //
                "org.eclipse.escet.cif.simulator.runtime.transitions.*", //
                "org.eclipse.escet.cif.simulator.output.print.*", //
                "org.eclipse.escet.cif.simulator.output.svgviz.*", //
                "static org.eclipse.escet.cif.simulator.runtime.CifSimulatorMath.*", //
                "static org.eclipse.escet.cif.simulator.runtime.io.RuntimeValueToString.runtimeToString", //

                // Generated code.
                "static " + PACKAGE + ".Spec.SPEC", //
                "static " + PACKAGE + ".Spec.MONITOR_EDGE", //
                "static " + PACKAGE + ".AlgVars.*", //
                "static " + PACKAGE + ".Constants.*", //
                "static " + PACKAGE + ".Derivatives.*" //
        );
    }

    /**
     * Returns the full name of the class with the given name, in the package of the generated code.
     *
     * @param className The name of the class, without a package.
     * @return The full name of the class with the given name, in the package of the generated code.
     */
    public static String getClassName(String className) {
        Assert.check(!PACKAGE.isEmpty());
        return PACKAGE + "." + className;
    }

    /**
     * Writes the generated source code to disk, if debugging of the generated code is enabled. Also writes the resource
     * files, and a Java file needed for debugging.
     */
    public void writeSourceCode() {
        // Make sure the debug option is enabled.
        if (!DebugOutputOption.doPrint(DebugOutputType.GEN_CODE)) {
            return;
        }

        // Get the path for the package of the generated code.
        Assert.check(!PACKAGE.isEmpty());
        String pkgPath = PACKAGE.replace('.', '/');

        // Get the path for the output directory.
        String dirPath = null;
        if (Platform.isRunning() && PlatformUI.isWorkbenchRunning()) {
            // Look for the debug project, and use it.
            IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
            IProject project = root.getProject(DBG_PROJ_NAME);
            if (project.exists()) {
                IFolder folder = project.getFolder("src");
                if (folder.exists()) {
                    IPath path = folder.getLocation();
                    if (path != null) {
                        Assert.check(path.isAbsolute());
                        dirPath = path.toString();
                    }
                }
            }
        }
        if (dirPath == null) {
            // If not running in Eclipse, or no debug project found.
            dirPath = Paths.resolve(".");
        }
        pkgPath = Paths.join(dirPath, pkgPath);

        // Create output directory.
        File pkgFile = new File(pkgPath);
        pkgFile.mkdirs();
        if (!pkgFile.exists() || !pkgFile.isDirectory()) {
            String msg = fmt("Failed to create directory \"%s\", to which the generated code is to be written, for "
                    + "debugging.", pkgPath);
            throw new InputOutputException(msg);
        }

        // Clean the output directory, by removing all generated files.
        String[] filters = {"*.java", "*.dat", "*." + FILE_EXT_LOC_NAMES, "*." + FILE_EXT_EDGE_DATA};
        FilenameFilter filter = new WildcardFileFilter(filters);
        File[] files = pkgFile.listFiles(filter);
        if (files == null) {
            String msg = fmt("Failed to list the files in directory \"%s\", to which the generated code is to be "
                    + "written, for debugging.", pkgPath);
            throw new InputOutputException(msg);
        }
        for (File file: files) {
            if (file.isDirectory()) {
                continue;
            }
            file.delete();
        }

        // Generate Java code for debugging the simulator.
        String classesPath = Paths.resolve("../../target/classes", pkgPath);
        DebugSimulatorCodeGenerator.gencodeDebugSimulator(classesPath, this);

        // Write the generated code files.
        for (JavaCodeFile file: code.values()) {
            String filePath = Paths.join(pkgPath, file.name + ".java");
            try {
                file.toBox().writeToFile(filePath);
            } catch (InputOutputException e) {
                String msg = fmt("Failed to write generated code file \"%s\", for debugging.", filePath);
                throw new InputOutputException(msg, e);
            }
        }

        // Write the generated resource files.
        for (Entry<String, ByteArrayOutputStream> res: resources.entrySet()) {
            String filePath = Paths.join(dirPath, res.getKey());
            ByteArrayOutputStream resStream = res.getValue();
            FileOutputStream fileStream = null;
            try {
                fileStream = new FileOutputStream(filePath);
                resStream.writeTo(fileStream);
            } catch (IOException e) {
                String msg = fmt("Failed to write generated resource file \"%s\", for debugging.", filePath);
                throw new InputOutputException(msg, e);
            } finally {
                try {
                    if (fileStream != null) {
                        fileStream.close();
                    }
                } catch (IOException e) {
                    String msg = fmt("Failed to close file \"%s\".", filePath);
                    throw new InputOutputException(msg, e);
                }
            }
        }

        // Remove the code for debugging the simulator, as that doesn't need to be compiled for simulation.
        JavaCodeFile removed = code.remove(DBG_SIM_CLS_NAME);
        Assert.notNull(removed);

        // Refresh the debug project.
        if (Platform.isRunning() && PlatformUI.isWorkbenchRunning()) {
            // Look for the debug project, and refresh it.
            IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
            IProject project = root.getProject(DBG_PROJ_NAME);
            if (project.exists()) {
                try {
                    project.refreshLocal(IResource.DEPTH_INFINITE, null);
                } catch (CoreException e) {
                    // Ignore errors here. Use will have to refresh manually.
                }
            }
        }
    }

    /**
     * Writes the compiled code and resource files to a JAR file ('.cifcode' file), if asked to compile only.
     *
     * <p>
     * This method should only be invoked after compilation has finished without errors.
     * </p>
     */
    public void writeCompiledCode() {
        // Make sure the option is enabled.
        if (!CompileOnlyOption.isEnabled()) {
            return;
        }

        // Write JAR file.
        String path = CompiledCodeFileOption.getPath();
        String absPath = Paths.resolve(path);
        compiler.writeJarFile(absPath, null, resources);
    }

    /**
     * Returns a class loader that can be used to load the compiled classes, as well as the generated resources.
     *
     * <p>
     * This method should only be invoked after compilation has finished without errors.
     * </p>
     *
     * @return The class loader.
     */
    public ClassLoader getClassLoader() {
        // Make sure compiler is available.
        Assert.notNull(compiler);

        // Return class loader for loading of the classes, if no resources.
        RuntimeClassLoader classLoader = compiler.getClassLoader();
        if (resources.isEmpty()) {
            return classLoader;
        }

        // Resources available. Wrap with resource class loader.
        ResourceClassLoader resLoader = new ResourceClassLoader(classLoader);
        for (Entry<String, ByteArrayOutputStream> res: resources.entrySet()) {
            String resPath = res.getKey();
            byte[] resData = res.getValue().toByteArray();
            resLoader.resources.put(resPath, resData);
        }
        return resLoader;
    }

    /** Compiles the generated code. */
    protected void compile() {
        // Make sure compiler is available.
        if (compiler == null) {
            String name = JavaCompilerOption.getCompilerName();
            ClassLoader classLoader = getClass().getClassLoader();
            compiler = new RuntimeJavaCompiler(name, classLoader);
        }

        // Get sources.
        Map<String, JavaInputFileObject> sources = map();
        for (Entry<String, JavaCodeFile> entry: code.entrySet()) {
            JavaCodeFile file = entry.getValue();
            sources.put(file.getAbsClassName(), file);
        }

        // Compile the generated Java code files.
        try {
            compiler.compile(sources);
        } catch (RuntimeJavaCompilerException e) {
            throw new RuntimeException("Compilation error.", e);
        }
    }
}
