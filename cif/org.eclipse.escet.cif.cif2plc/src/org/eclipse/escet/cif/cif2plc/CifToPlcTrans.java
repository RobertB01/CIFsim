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

package org.eclipse.escet.cif.cif2plc;

import static org.eclipse.escet.cif.cif2plc.options.PlcOutputType.S7_1200;
import static org.eclipse.escet.cif.cif2plc.options.PlcOutputType.S7_1500;
import static org.eclipse.escet.cif.cif2plc.options.PlcOutputType.S7_300;
import static org.eclipse.escet.cif.cif2plc.options.PlcOutputType.S7_400;
import static org.eclipse.escet.cif.cif2plc.options.PlcOutputTypeOption.getPlcOutputType;
import static org.eclipse.escet.cif.cif2plc.plcdata.PlcDerivedType.STATE_TYPE;
import static org.eclipse.escet.cif.cif2plc.plcdata.PlcElementaryType.BOOL_TYPE;
import static org.eclipse.escet.cif.cif2plc.plcdata.PlcElementaryType.DINT_TYPE;
import static org.eclipse.escet.cif.cif2plc.plcdata.PlcElementaryType.INT_TYPE;
import static org.eclipse.escet.cif.cif2plc.plcdata.PlcElementaryType.LINT_TYPE;
import static org.eclipse.escet.cif.cif2plc.plcdata.PlcElementaryType.LREAL_TYPE;
import static org.eclipse.escet.cif.cif2plc.plcdata.PlcElementaryType.REAL_TYPE;
import static org.eclipse.escet.cif.cif2plc.plcdata.PlcElementaryType.TIME_TYPE;
import static org.eclipse.escet.cif.common.CifIntFuncUtils.getAssignedParameters;
import static org.eclipse.escet.cif.common.CifTextUtils.getAbsName;
import static org.eclipse.escet.cif.common.CifTypeUtils.isRangeless;
import static org.eclipse.escet.cif.common.CifTypeUtils.makeTupleType;
import static org.eclipse.escet.cif.common.CifTypeUtils.normalizeType;
import static org.eclipse.escet.cif.common.CifValueUtils.makeTuple;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newRealType;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.warn;
import static org.eclipse.escet.common.emf.EMFHelper.deepclone;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.slice;
import static org.eclipse.escet.common.java.Strings.str;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.escet.cif.cif2cif.AddDefaultInitialValues;
import org.eclipse.escet.cif.cif2cif.ElimComponentDefInst;
import org.eclipse.escet.cif.cif2cif.ElimConsts;
import org.eclipse.escet.cif.cif2cif.ElimStateEvtExclInvs;
import org.eclipse.escet.cif.cif2cif.EnumsToConsts;
import org.eclipse.escet.cif.cif2cif.EnumsToInts;
import org.eclipse.escet.cif.cif2cif.LinearizeMerge;
import org.eclipse.escet.cif.cif2cif.MergeEnums;
import org.eclipse.escet.cif.cif2cif.RemoveIoDecls;
import org.eclipse.escet.cif.cif2cif.SimplifyOthers;
import org.eclipse.escet.cif.cif2cif.SimplifyValues;
import org.eclipse.escet.cif.cif2plc.NaryExpressionConverter.NaryExpression;
import org.eclipse.escet.cif.cif2plc.options.ConvertEnums;
import org.eclipse.escet.cif.cif2plc.options.ConvertEnumsOption;
import org.eclipse.escet.cif.cif2plc.options.ElimEnumsOption;
import org.eclipse.escet.cif.cif2plc.options.PlcConfigurationNameOption;
import org.eclipse.escet.cif.cif2plc.options.PlcFormalFuncInvokeArg;
import org.eclipse.escet.cif.cif2plc.options.PlcFormalFuncInvokeArgOption;
import org.eclipse.escet.cif.cif2plc.options.PlcFormalFuncInvokeFunc;
import org.eclipse.escet.cif.cif2plc.options.PlcFormalFuncInvokeFuncOption;
import org.eclipse.escet.cif.cif2plc.options.PlcMaxIterOption;
import org.eclipse.escet.cif.cif2plc.options.PlcNumberBitsOption;
import org.eclipse.escet.cif.cif2plc.options.PlcOutputTypeOption;
import org.eclipse.escet.cif.cif2plc.options.PlcProjectNameOption;
import org.eclipse.escet.cif.cif2plc.options.PlcResourceNameOption;
import org.eclipse.escet.cif.cif2plc.options.PlcTaskCycleTimeOption;
import org.eclipse.escet.cif.cif2plc.options.PlcTaskNameOption;
import org.eclipse.escet.cif.cif2plc.options.PlcTaskPriorityOption;
import org.eclipse.escet.cif.cif2plc.options.RenameWarningsOption;
import org.eclipse.escet.cif.cif2plc.options.SimplifyValuesOption;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcArrayType;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcConfiguration;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcDerivedType;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcElementaryType;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcEnumType;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcGlobalVarList;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcPou;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcPouInstance;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcPouType;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcProject;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcResource;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcStructType;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcTask;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcType;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcTypeDecl;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcValue;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcVariable;
import org.eclipse.escet.cif.common.CifAddressableUtils;
import org.eclipse.escet.cif.common.CifAddressableUtils.DuplVarAsgnException;
import org.eclipse.escet.cif.common.CifCollectUtils;
import org.eclipse.escet.cif.common.CifScopeUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.CifValueUtils;
import org.eclipse.escet.cif.common.ConstantOrderer;
import org.eclipse.escet.cif.common.FuncLocalVarOrderer;
import org.eclipse.escet.cif.common.RangeCompat;
import org.eclipse.escet.cif.common.StateInitVarOrderer;
import org.eclipse.escet.cif.common.TypeEqHashWrap;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Assignment;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.ElifUpdate;
import org.eclipse.escet.cif.metamodel.cif.automata.IfUpdate;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.automata.Update;
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
import org.eclipse.escet.cif.metamodel.cif.expressions.AlgVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.BoolExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CastExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ConstantExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ContVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DictExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ElifExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EnumLiteralExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.FunctionCallExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.FunctionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IfExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.InputVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IntExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ListExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.LocationExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ProjectionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.RealExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SetExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SliceExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunction;
import org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunctionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.StringExpression;
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
import org.eclipse.escet.cif.metamodel.cif.functions.IfFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.InternalFunction;
import org.eclipse.escet.cif.metamodel.cif.functions.ReturnFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.WhileFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
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
import org.eclipse.escet.common.app.framework.exceptions.InvalidInputException;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Pair;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** CIF PLC code generator. */
public class CifToPlcTrans {
    /**
     * For which functions (arguments based) should formal invocation syntax be used?
     *
     * @see PlcFormalFuncInvokeArgOption
     */
    private final PlcFormalFuncInvokeArg formalInvokeArg;

    /**
     * For which functions (function kind based) should formal invocation syntax be used?
     *
     * @see PlcFormalFuncInvokeFuncOption
     */
    private final PlcFormalFuncInvokeFunc formalInvokeFunc;

    /** Should value simplification be performed, and constants be inlined and removed? */
    private final boolean simplifyValues;

    /** Is it allowed for constants to appear in the specification? */
    private final boolean constantsAllowed;

    /** The PLC project, or {@code null} until created. */
    private PlcProject project;

    /** The PLC configuration, or {@code null} until created. */
    private PlcConfiguration config;

    /** The PLC resource, or {@code null} until created. */
    private PlcResource resource;

    /** The PLC task, or {@code null} until created. */
    private PlcTask task;

    /** The PLC global variable list for the CIF constants. */
    private PlcGlobalVarList globalConsts;

    /** The PLC global variable list for the CIF input variables. */
    private PlcGlobalVarList globalInputs;

    /** The PLC struct type for the 'STATE' data type. */
    private PlcStructType stateStruct;

    /** The optional prefix to use for static (persistent) variables. */
    private final String staticVarPrefix;

    /** Mapping from named CIF objects to their PLC names, for global names only. */
    private Map<PositionObject, String> objNames = map();

    /**
     * The set of names already in use in the PLC code, for global names only. All names in this set are converted to
     * lower case, to account for case insensitive identifiers in IEC 61131-3.
     */
    private Set<String> names = set();

    /**
     * Mapping from internal user-defined functions to their generated POUs. Entries are added when the header has been
     * transformed, to allow cycling calling of functions.
     */
    private Map<InternalFunction, PlcPou> internalFuncs = map();

    /**
     * Mapping from CIF tuple types, wrapped in {@link TypeEqHashWrap} instances, to Structured Text structure names.
     */
    private Map<TypeEqHashWrap, String> structNames = map();

    /**
     * Mapping from CIF array types, wrapped in {@link TypeEqHashWrap} instances, to Structured Text array unique
     * numbers.
     */
    private Map<TypeEqHashWrap, Integer> arrayNrs = map();

    /**
     * The Structured Text array unique numbers (see {@link #arrayNrs}) of the unique array types for which a literal
     * creation function has been generated.
     */
    private Set<Integer> arrayLitCreateFuncGenerated = set();

    /**
     * The Structured Text array unique numbers (see {@link #arrayNrs}) of the unique array types for which a projection
     * function has been generated.
     */
    private Set<Integer> arrayProjFuncGenerated = set();

    /** Has a 'normProjIdx' function been generated? */
    private boolean normProjIdxGenerated = false;

    /** The next unique number to use to generate structure names. */
    private int nextStructNr = 1;

    /** The next unique number to use to generate an if expression function. */
    private int nextIfFuncNr = 1;

    /**
     * The maximum 'level' encountered by {@link #transGuard}, for an n-ary expression. If zero, no n-ary expressions
     * were encountered.
     */
    private int maxNaryLevel = 0;

    /** The largest real type {@link PlcNumberBitsOption available}. */
    private PlcElementaryType largeRealType;

    /** The largest integer type {@link PlcNumberBitsOption available}. */
    private PlcElementaryType largeIntType;

    /** Constructor for the {@link CifToPlcTrans} class. */
    private CifToPlcTrans() {
        // Private constructor, to force use of public static method.
        simplifyValues = SimplifyValuesOption.simplifyValues();
        constantsAllowed = !simplifyValues || ConvertEnumsOption.getValue() == ConvertEnums.CONSTS;
        formalInvokeArg = PlcFormalFuncInvokeArgOption.getValue();
        formalInvokeFunc = PlcFormalFuncInvokeFuncOption.getValue();
        staticVarPrefix = PlcOutputTypeOption.isS7Output() ? "\"DB\"." : "";

        if (PlcOutputTypeOption.isS7Output()
                && (formalInvokeArg == PlcFormalFuncInvokeArg.NONE || formalInvokeFunc != PlcFormalFuncInvokeFunc.ALL))
        {
            // S7 requires formal function invocation for all functions with more than two arguments.
            String msg = fmt("Formal function invocation is not enabled for all functions, while this is required for "
                    + "%s code. Please set the \"Formal function invocation (arguments based)\" and \"Formal function "
                    + "invocation (function kind based)\" options accordingly.", getPlcOutputType().dialogText);
            throw new InvalidInputException(msg);
        }

        // Determine largest int/real types based on option value.
        switch (PlcNumberBitsOption.getNumberBits()) {
            case AUTO:
                largeIntType = getPlcOutputType().largeIntType;
                largeRealType = getPlcOutputType().largeRealType;
                break;

            case BITS_32:
                largeIntType = DINT_TYPE;
                largeRealType = REAL_TYPE;
                break;

            case BITS_64:
                largeIntType = LINT_TYPE;
                largeRealType = LREAL_TYPE;
                break;

            default:
                throw new RuntimeException("Unknown number of bits: " + PlcNumberBitsOption.getNumberBits());
        }
    }

    /**
     * Transforms a CIF specification to PLC code.
     *
     * @param spec The CIF specification. Is modified in-place as a side-effect of preprocessing.
     * @return The PLC project resulting from the transformation.
     */
    public static PlcProject transform(Specification spec) {
        // Initialize transformation.
        CifToPlcTrans trans = new CifToPlcTrans();

        // Eliminate component definition/instantiation, to avoid having to
        // handle them.
        new ElimComponentDefInst().transform(spec);

        // Eliminate state/event exclusion invariants, to avoid having to
        // handle them.
        new ElimStateEvtExclInvs().transform(spec);

        // Simplify the specification, to increase the supported subset. Since
        // simplification of values fills in all constants, we can also remove
        // the constants. However, this may lead to large amounts of
        // duplication for constants with large literal values. Therefore, it
        // is an option. We could always use less expensive variants of value
        // simplification, in the future.
        if (trans.simplifyValues) {
            new SimplifyValues().transform(spec);
            new ElimConsts().transform(spec);
        }

        // Simplify the specification, to reduce PLC code size.
        new SimplifyOthers().transform(spec);

        // Remove/ignore I/O declarations, to increase the supported subset.
        RemoveIoDecls removeIoDecls = new RemoveIoDecls();
        removeIoDecls.transform(spec);
        if (removeIoDecls.haveAnySvgInputDeclarationsBeenRemoved()) {
            warn("The specification contains CIF/SVG input declarations. These will be ignored.");
        }

        // Check the specification, now that it is still intact (absolute names
        // still make sense in the input specification). Note that even though
        // linearization etc may change the specification, the precondition
        // checker should be enough to ensure only supported features are
        // encountered during transformation.
        new CifToPlcPreChecker().check(spec);

        // Linearize the specification, to get rid of parallel composition,
        // event synchronization, and channels. We choose the variant that
        // eliminates choice, as choice is useless in PLC code, and this
        // results in the least amount of PLC code.
        new LinearizeMerge().transform(spec);

        // Merge enumerations into a single enumeration for easier code generation.
        new MergeEnums().transform(spec);

        // Simplify the values, to get rid of useless values introduced by
        // the linearization.
        if (trans.simplifyValues) {
            new SimplifyValues().transform(spec);
        }

        // Add default initial values, to simplify the code generation.
        new AddDefaultInitialValues().transform(spec);

        // If requested, convert enumerations.
        if (ElimEnumsOption.elimEnums()) {
            warn("The \"elim-enums\" option is deprecated. Use the \"convert-enums\" option instead.");
            new EnumsToInts().transform(spec);
        } else if (ConvertEnumsOption.getValue() == ConvertEnums.INTS) {
            new EnumsToInts().transform(spec);
        } else if (ConvertEnumsOption.getValue() == ConvertEnums.CONSTS) {
            // This transformation introduces new constants that are intentionally not removed if simplify values is
            // enabled.
            new EnumsToConsts().transform(spec);
        } else if (PlcOutputTypeOption.isS7Output()) {
            // Enumerations are not converted.
            String msg = fmt("Enumerations are not converted, while this is required for %s code. Please set the "
                    + "\"Convert enumerations\" option accordingly.", getPlcOutputType().dialogText);
            throw new InvalidInputException(msg);
        }

        // Generate code.
        return trans.transformSpec(spec);
    }

    /**
     * Transforms a CIF specification to PLC code.
     *
     * @param spec The CIF specification. Must not contain component definitions/instantiations.
     * @return The PLC project resulting from the transformation.
     */
    private PlcProject transformSpec(Specification spec) {
        // Initialization of the project.
        initProject();

        // Get automaton. We ignore the alphabet, monitors (no longer exist),
        // initialization predicates and state invariants (should not exist,
        // or trivially 'true'), state/event exclusion invariants (no longer
        // exist), and marker predicates (have no effect).
        List<Automaton> automata = listc(1);
        CifCollectUtils.collectAutomata(spec, automata);
        Assert.check(automata.size() == 1);
        Automaton aut = first(automata);

        // Transform the declarations. Collect some declarations as well.
        List<Declaration> declarations = list();
        CifCollectUtils.collectDeclarations(spec, declarations);

        List<Declaration> stateVars = list();
        List<Constant> constants = list();

        for (Declaration decl: declarations) {
            if (decl instanceof DiscVariable) {
                stateVars.add(decl);
            }
            if (decl instanceof ContVariable) {
                stateVars.add(decl);
            }

            if (decl instanceof Constant) {
                constants.add((Constant)decl);
            }

            transDecl(decl);
        }

        // Check for input and state variables.
        if (globalInputs.variables.isEmpty()) {
            warn("Generating PLC code for a specification without input variables may make it impossible to connect "
                    + "to input ports.");
        }
        if (stateVars.isEmpty()) {
            // Happens in situations where all automata have exactly one location and no variables.
            warn("Generating PLC code for a specification without state variables may make it impossible to connect "
                    + "to output ports.");
        }

        // Add the constants.
        transConstants(constants);

        // Generate program.
        generateProgram(aut, stateVars);

        // Return the project.
        return project;
    }

    /** Initializes an empty project. */
    private void initProject() {
        // Create project, configuration, resource, and task.
        project = new PlcProject(PlcProjectNameOption.getProjName());
        config = new PlcConfiguration(PlcConfigurationNameOption.getCfgName());
        resource = new PlcResource(PlcResourceNameOption.getResName());
        task = new PlcTask(PlcTaskNameOption.getTaskName(), PlcTaskCycleTimeOption.getTaskCycleTime(),
                PlcTaskPriorityOption.getTaskPrio());

        project.configurations.add(config);
        config.resources.add(resource);
        resource.tasks.add(task);

        // Global variable/constant lists.
        globalInputs = new PlcGlobalVarList("INPUTS", false);
        resource.globalVarLists.add(globalInputs);

        if (constantsAllowed) {
            globalConsts = new PlcGlobalVarList("CONSTS", true);
            resource.globalVarLists.add(globalConsts);
        }

        // Timer global variable list.
        PlcGlobalVarList globalTimers = new PlcGlobalVarList("TIMERS", false);
        resource.globalVarLists.add(globalTimers);
        PlcType tonType = new PlcDerivedType("TON");
        globalTimers.variables.add(new PlcVariable("timer0", tonType));
        globalTimers.variables.add(new PlcVariable("timer1", tonType));
        globalTimers.variables.add(new PlcVariable("curTimer", INT_TYPE, null, new PlcValue("0")));

        // Type declarations.
        stateStruct = new PlcStructType();
        PlcTypeDecl stateType = new PlcTypeDecl("STATE", stateStruct);
        project.typeDecls.add(stateType);
        PlcVariable stateTimeVar = new PlcVariable("curTime", largeRealType);
        stateStruct.fields.add(stateTimeVar);
    }

    /**
     * Generates code for the program.
     *
     * @param aut The automaton from the linearized CIF specification.
     * @param stateVars The state variables.
     */
    private void generateProgram(Automaton aut, List<Declaration> stateVars) {
        // Create code file for program, with header etc.
        PlcPou main = new PlcPou("MAIN", PlcPouType.PROGRAM, null);
        project.pous.add(main);

        // Add program to task.
        task.pouInstances.add(new PlcPouInstance("MAIN", main));

        // Add local (persistent) variables.
        main.localVars.add(new PlcVariable("cnt", largeIntType));
        main.localVars.add(new PlcVariable("first", BOOL_TYPE, null, new PlcValue("TRUE")));
        main.localVars.add(new PlcVariable("curTimerValue", TIME_TYPE));
        main.localVars.add(new PlcVariable("state0", STATE_TYPE));
        main.localVars.add(new PlcVariable("curTime", largeRealType));
        main.localVars.add(new PlcVariable("loopsKilled", largeIntType));

        // Add temporary (non-persistent) variables.
        main.tempVars.add(new PlcVariable("lastTimerValue", TIME_TYPE));
        main.tempVars.add(new PlcVariable("curDeltaTime", TIME_TYPE));
        main.tempVars.add(new PlcVariable("curDeltaSecs", largeRealType));
        main.tempVars.add(new PlcVariable("state1", STATE_TYPE));
        main.tempVars.add(new PlcVariable("progress", BOOL_TYPE));
        main.tempVars.add(new PlcVariable("loopCount", INT_TYPE));

        // Add output variables. They are required for the 'TON' timers.
        main.outputVars.add(new PlcVariable("timerValue0", TIME_TYPE));
        main.outputVars.add(new PlcVariable("timerValue1", TIME_TYPE));

        // Generate body code. By using a 'TON' clock, we can measure the
        // time since the last cycle, and thus the amount of time that has
        // past (the cycle time), independent of any implementation variables
        // for cycle times, etc. We use two clocks, to avoid overflow over
        // long running times, by switching between them, every some many
        // cycles. We can't use a single clock, as we need to disable the
        // clock in one cycle, and re-activate it in a future cycle, in order
        // for the clock to be reset to zero.
        main.body.add();
        main.body.add("// Handle 'time' and cycle time.");
        main.body.add("%s := %s + 1;", genStaticVarRef("cnt"), genStaticVarRef("cnt"));
        main.body.add();

        // Get timer text.
        String timer0Txt = PlcOutputTypeOption.isS7Output() ? "timer0.TON" : "timer0";
        String timer1Txt = PlcOutputTypeOption.isS7Output() ? "timer1.TON" : "timer1";

        // Start the timer.
        main.body.add("%s(IN := %s = 0, PT := T#1D);", timer0Txt, genStaticVarRef("curTimer"));
        main.body.add("%s(IN := %s = 1, PT := T#1D);", timer1Txt, genStaticVarRef("curTimer"));

        // Get the elapsed time of the timers.
        main.body.add("timerValue0 := timer0.ET;");
        main.body.add("timerValue1 := timer1.ET;");
        main.body.add();

        // Calculate the last cycle time.
        main.body.add("lastTimerValue := %s;", genStaticVarRef("curTimerValue"));
        main.body.add("IF %s = 0 THEN", genStaticVarRef("curTimer"));
        main.body.indent();
        main.body.add("%s := timerValue0;", genStaticVarRef("curTimerValue"));
        main.body.dedent();
        main.body.add("ELSE");
        main.body.indent();
        main.body.add("%s := timerValue1;", genStaticVarRef("curTimerValue"));
        main.body.dedent();
        main.body.add("END_IF;");
        main.body.add("curDeltaTime := %s - lastTimerValue;", genStaticVarRef("curTimerValue"));

        // Convert time type to real type.
        String conversionTxt;
        if (PlcOutputTypeOption.isS7Output()) {
            // S7 doesn't have time to real conversion directly.
            String timeToInt = genFuncCall(fmt("TIME_TO_%s", largeIntType.name), true, "IN", "curDeltaTime");
            conversionTxt = genFuncCall(fmt("%s_TO_%s", largeIntType.name, largeRealType.name), true, "IN", timeToInt);
        } else {
            conversionTxt = genFuncCall(fmt("TIME_TO_%s", largeRealType.name), true, "IN", "curDeltaTime");
        }

        // Calculate the last cycle time in seconds.
        main.body.add("curDeltaSecs := %s / 1000;", conversionTxt);
        main.body.add("%s := %s + curDeltaSecs;", genStaticVarRef("curTime"), genStaticVarRef("curTime"));
        main.body.add();

        // Switch timers every 10 cycles.
        main.body.add("IF %s MOD 10 = 0 THEN", genStaticVarRef("cnt"));
        main.body.indent();
        main.body.add("%s := 1 - %s;", genStaticVarRef("curTimer"), genStaticVarRef("curTimer"));
        main.body.add("%s := T#0S;", genStaticVarRef("curTimerValue"));
        main.body.add("%s(IN := %s = 0, PT := T#1D);", timer0Txt, genStaticVarRef("curTimer"));
        main.body.add("%s(IN := %s = 1, PT := T#1D);", timer1Txt, genStaticVarRef("curTimer"));
        main.body.add("timerValue0 := timer0.ET;");
        main.body.add("timerValue1 := timer1.ET;");
        main.body.dedent();
        main.body.add("END_IF;");

        // Initialization and time delay.
        main.body.add();
        main.body.add("IF %s THEN", genStaticVarRef("first"));
        main.body.indent();
        main.body.add("%s := FALSE;", genStaticVarRef("first"));
        main.body.add();
        main.body.add("// Initialize state variables for initial state.");
        stateVars = new StateInitVarOrderer().computeOrder(stateVars);
        for (Declaration stateVar: stateVars) {
            // Initialize state variable.
            if (stateVar instanceof DiscVariable) {
                DiscVariable var = (DiscVariable)stateVar;
                Expression value = first(var.getValue().getValues());
                main.body.add("%s.%s := %s;", genStaticVarRef("state0"), getPlcName(stateVar),
                        transExpr(value, genStaticVarRef("state0"), false));
            } else {
                Assert.check(stateVar instanceof ContVariable);
                ContVariable var = (ContVariable)stateVar;
                Expression value = var.getValue();
                main.body.add("%s.%s := %s;", genStaticVarRef("state0"), getPlcName(stateVar),
                        transExpr(value, genStaticVarRef("state0"), false));
            }
        }
        main.body.dedent();
        main.body.add("ELSE");
        main.body.indent();
        main.body.add("// Update continuous variables for time delay.");
        for (Declaration stateVar: stateVars) {
            // Evaluate in state0, and store in state1, to keep the values of
            // state0 intact for subsequent derivatives.
            if (!(stateVar instanceof ContVariable)) {
                continue;
            }
            ContVariable var = (ContVariable)stateVar;
            String name = getPlcName(var);
            String fc = genFuncCall("deriv" + name, false, "state", genStaticVarRef("state0"));
            main.body.add("state1.%s := %s.%s + curDeltaSecs * %s;", name, genStaticVarRef("state0"), name, fc);
        }
        main.body.add();
        main.body.add("%s.curTime := %s;", genStaticVarRef("state0"), genStaticVarRef("curTime"));
        for (Declaration stateVar: stateVars) {
            // Copy continuous variable values from state1 to state0.
            if (!(stateVar instanceof ContVariable)) {
                continue;
            }
            ContVariable var = (ContVariable)stateVar;
            String name = getPlcName(var);
            main.body.add("%s.%s := state1.%s;", genStaticVarRef("state0"), name, name);
        }
        main.body.dedent();
        main.body.add("END_IF;");

        // Add start of main loop for events.
        main.body.add();
        main.body.add("// Event loop.");
        main.body.add("WHILE TRUE DO");
        main.body.indent();

        // Copy state.
        main.body.add("progress := FALSE;");

        // Get single linearized location. We ignore the initialization
        // predicates (should be trivially 'true'), state invariants
        // (should not exist, or trivially 'true'), state/event
        // exclusion invariants (no longer exist), and marker predicates
        // (have no effect).
        Assert.check(aut.getLocations().size() == 1);
        Location loc = first(aut.getLocations());

        // Transform the edges.
        for (Edge edge: loc.getEdges()) {
            transEdge(edge, main);
        }

        for (int i = 1; i <= maxNaryLevel; i++) {
            main.tempVars.add(new PlcVariable("b" + i, BOOL_TYPE));
        }

        // End of main loop.
        main.body.add();
        main.body.add("// Done with events?");
        main.body.add("IF NOT progress THEN");
        main.body.indent();
        main.body.add("EXIT;");
        main.body.dedent();
        main.body.add("END_IF;");

        Integer maxIter = PlcMaxIterOption.getMaxIter();
        if (maxIter != null) {
            main.body.add();
            main.body.add("// Protect against events that are always enabled.");
            main.body.add("loopCount := loopCount + 1;");
            main.body.add("IF loopCount >= %d THEN", maxIter);
            main.body.indent();
            main.body.add("%s := %s + 1;", genStaticVarRef("loopsKilled"), genStaticVarRef("loopsKilled"));
            main.body.add("EXIT;");
            main.body.dedent();
            main.body.add("END_IF;");
        }

        main.body.dedent();
        main.body.add("END_WHILE;");
    }

    /**
     * Transforms a declaration to PLC code.
     *
     * @param decl The declaration.
     */
    private void transDecl(Declaration decl) {
        if (decl instanceof AlgVariable) {
            transAlgVar((AlgVariable)decl);
        } else if (decl instanceof Constant) {
            // Ignore. Generated elsewhere, in the correct order.
            Assert.check(constantsAllowed);
        } else if (decl instanceof ContVariable) {
            transContVar((ContVariable)decl);
        } else if (decl instanceof DiscVariable) {
            transDiscVar((DiscVariable)decl);
        } else if (decl instanceof EnumDecl) {
            transEnum((EnumDecl)decl);
        } else if (decl instanceof Event) {
            // Ignore. Events are irrelevant after linearization.
        } else if (decl instanceof InternalFunction) {
            transFunction((InternalFunction)decl);
        } else if (decl instanceof ExternalFunction) {
            throw new RuntimeException("precond violation: " + decl);
        } else if (decl instanceof InputVariable) {
            transInputVar((InputVariable)decl);
        } else if (decl instanceof TypeDecl) {
            // Ignore. Types are normalized when needed.
        } else {
            throw new RuntimeException("Unknown decl: " + decl);
        }
    }

    /**
     * Transforms an algebraic variable to PLC code. In the PLC code, the variable is represented as a function with a
     * 'state' argument.
     *
     * @param var The algebraic variable.
     */
    private void transAlgVar(AlgVariable var) {
        // Create function for algebraic variable.
        String name = getPlcName(var);
        PlcPou func = new PlcPou(name, PlcPouType.FUNCTION, transType(var.getType()));
        project.pous.add(func);

        // Add parameters.
        func.inputVars.add(new PlcVariable("state", STATE_TYPE));

        // Add body.
        String valueTxt = transExpr(var.getValue(), "state", false);
        func.body.add("%s := %s;", name, valueTxt);
    }

    /**
     * Transforms constants to PLC code, taking into account their interdependencies.
     *
     * @param constants The constants.
     */
    private void transConstants(List<Constant> constants) {
        // Order the constants by their interdependencies.
        constants = new ConstantOrderer().computeOrder(constants);

        // Transform the constants.
        for (Constant constant: constants) {
            transConstant(constant);
        }
    }

    /**
     * Transforms a constant to PLC code. All constants used in the value of this constant must have already been
     * transformed to PLC code.
     *
     * @param constant The constant.
     */
    private void transConstant(Constant constant) {
        String name = getPlcName(constant);
        PlcType type = transType(constant.getType());
        String valueTxt = transExpr(constant.getValue(), null, true);
        PlcValue value = new PlcValue(valueTxt);
        PlcVariable plcVar = new PlcVariable(name, type, null, value);
        globalConsts.variables.add(plcVar);
    }

    /**
     * Transforms a continuous variable to PLC code.
     *
     * @param var The continuous variable.
     */
    private void transContVar(ContVariable var) {
        // Generate the continuous variable in the state.
        String vname = getPlcName(var);
        PlcType plcType = largeRealType;
        PlcVariable plcVar = new PlcVariable(vname, plcType, "%Q*", null);
        stateStruct.fields.add(plcVar);

        // Create function for derivative.
        String dname = "deriv" + vname;
        PlcPou func = new PlcPou(dname, PlcPouType.FUNCTION, plcType);
        project.pous.add(func);

        // Add parameters.
        func.inputVars.add(new PlcVariable("state", STATE_TYPE));

        // Add body.
        func.body.add("%s := %s;", dname, transExpr(var.getDerivative(), "state", false));
    }

    /**
     * Transforms a discrete variable to PLC code.
     *
     * @param var The discrete variable.
     */
    private void transDiscVar(DiscVariable var) {
        String name = getPlcName(var);
        PlcVariable plcVar = new PlcVariable(name, transType(var.getType()), "%Q*", null);
        stateStruct.fields.add(plcVar);
    }

    /**
     * Transforms an input variable to PLC code.
     *
     * @param var The input variable.
     */
    private void transInputVar(InputVariable var) {
        String name = getPlcName(var);
        PlcVariable plcVar = new PlcVariable(name, transType(var.getType()), "%I*", null);
        globalInputs.variables.add(plcVar);
    }

    /**
     * Transforms an enumeration declaration to PLC code.
     *
     * @param enumDecl The enumeration declaration.
     */
    private void transEnum(EnumDecl enumDecl) {
        // Note that after linearization we have at most one enumeration.
        List<String> litNames = listc(enumDecl.getLiterals().size());
        for (EnumLiteral lit: enumDecl.getLiterals()) {
            litNames.add(getPlcName(lit));
        }
        PlcEnumType plcEnumType = new PlcEnumType(litNames);

        String name = getPlcName(enumDecl);
        project.typeDecls.add(new PlcTypeDecl(name, plcEnumType));
    }

    /**
     * Transforms an internal user-defined function to PLC code. If the function has been transformed previously, it is
     * not transformed again.
     *
     * @param func The internal user-defined function.
     * @return The POU for the function.
     */
    private PlcPou transFunction(InternalFunction func) {
        // If already transformed, no need to transform again.
        PlcPou pou = internalFuncs.get(func);
        if (pou != null) {
            return pou;
        }

        // Add function.
        String name = getPlcName(func);
        CifType rtype = makeTupleType(deepclone(func.getReturnTypes()));
        pou = new PlcPou(name, PlcPouType.FUNCTION, transType(rtype));
        project.pous.add(pou);

        // Add parameters.
        Set<DiscVariable> assignedParams = getAssignedParameters(func);
        for (FunctionParameter param: func.getParameters()) {
            // Is the parameter assigned in the body of the function?
            boolean assigned = assignedParams.contains(param.getParameter());

            // Add parameter or local variable.
            String pname = getPlcName(param.getParameter());
            PlcType ptype = transType(param.getParameter().getType());
            PlcVariable pvar = new PlcVariable(pname, ptype);
            if (assigned) {
                pou.localVars.add(pvar);
            } else {
                pou.inputVars.add(pvar);
            }

            // If assigned, add parameter and assignment of parameter to local
            // variable.
            if (assigned) {
                String pname2 = pname;
                Assert.check(pname2.startsWith("farg_"));
                pname2 = "farg2_" + pname.substring("farg_".length());
                PlcType ptype2 = transType(param.getParameter().getType());
                PlcVariable pvar2 = new PlcVariable(pname2, ptype2);

                pou.inputVars.add(pvar2);
                pou.body.add("%s := %s;", pname, pname2);
            }
        }

        // Add function to the global mapping. Make it available now, to allow
        // cross calling of functions.
        internalFuncs.put(func, pou);

        // Add local variables, if any.
        if (!func.getVariables().isEmpty()) {
            // Add local variable declarations.
            for (DiscVariable var: func.getVariables()) {
                String vname = getPlcName(var);
                PlcType vtype = transType(var.getType());
                pou.localVars.add(new PlcVariable(vname, vtype));
            }

            // Order local variables and generate initialization code.
            pou.body.add("// Initialize local variables.");
            List<DiscVariable> vars;
            vars = new FuncLocalVarOrderer().computeOrder(func.getVariables());
            for (DiscVariable var: vars) {
                String pname = getPlcName(var);
                pou.body.add("%s := %s;", pname, transExpr(first(var.getValue().getValues()), null, false));
            }

            // Code to come is actual body.
            pou.body.add();
            pou.body.add("// Actual function body.");
        }

        // Add body.
        transStatements(func, func.getStatements(), pou);

        // Return the POU for the function.
        return pou;
    }

    /**
     * Transforms a function statements to PLC code.
     *
     * @param func The internal user-defined CIF function of which the statement is a part.
     * @param stats The function statements.
     * @param pou The POU in which to generate the code.
     */
    private void transStatements(InternalFunction func, List<FunctionStatement> stats, PlcPou pou) {
        for (FunctionStatement stat: stats) {
            transStatement(func, stat, pou);
        }
    }

    /**
     * Transforms a function statement to PLC code.
     *
     * @param func The internal user-defined CIF function of which the statement is a part.
     * @param stat The function statement.
     * @param pou The POU in which to generate the code.
     */
    private void transStatement(InternalFunction func, FunctionStatement stat, PlcPou pou) {
        if (stat instanceof AssignmentFuncStatement) {
            AssignmentFuncStatement asgn = (AssignmentFuncStatement)stat;
            Expression addr = asgn.getAddressable();

            // For multi-assignments, first copy variables from the addressable
            // to temporary variables, then modify the temporary variables,
            // then copy them back. This ensures proper old vs new value
            // semantics.
            Set<Declaration> addrVars;
            try {
                addrVars = CifAddressableUtils.getRefs(addr);
            } catch (DuplVarAsgnException ex) {
                throw new RuntimeException("precond violation");
            }
            Map<Declaration, String> addrVarsMap = null;
            if (addrVars.size() > 1) {
                addrVarsMap = map();
                for (Declaration addrVar: addrVars) {
                    Assert.check(addrVar instanceof DiscVariable);
                    DiscVariable var = (DiscVariable)addrVar;
                    String varName = getPlcName(addrVar);
                    PlcType varType = transType(var.getType());
                    String tmpName = pou.addTempVar(varType, varName);
                    addrVarsMap.put(var, tmpName);
                }
            }

            // Transform actual assignment.
            transAssignment(addr, asgn.getValue(), null, null, pou, "%s", addrVarsMap);

            // Copy back temporary variables.
            if (addrVars.size() > 1) {
                for (Declaration addrVar: addrVars) {
                    Assert.check(addrVar instanceof DiscVariable);
                    String varName = getPlcName(addrVar);
                    @SuppressWarnings("null")
                    String tmpName = addrVarsMap.get(addrVar);
                    pou.body.add("%s := %s;", varName, tmpName);
                }
            }
        } else if (stat instanceof BreakFuncStatement) {
            pou.body.add("EXIT;");
        } else if (stat instanceof ContinueFuncStatement) {
            throw new RuntimeException("precond violation: " + stat);
        } else if (stat instanceof IfFuncStatement) {
            IfFuncStatement ifStat = (IfFuncStatement)stat;
            CodeBox c = pou.body;
            c.add("IF %s THEN", transPreds(ifStat.getGuards(), null, false));
            c.indent();
            transStatements(func, ifStat.getThens(), pou);
            c.dedent();
            for (ElifFuncStatement elifStat: ifStat.getElifs()) {
                c.add("ELSIF %s THEN", transPreds(elifStat.getGuards(), null, false));
                c.indent();
                transStatements(func, elifStat.getThens(), pou);
                c.dedent();
            }
            if (!ifStat.getElses().isEmpty()) {
                c.add("ELSE");
                c.indent();
                transStatements(func, ifStat.getElses(), pou);
                c.dedent();
            }
            c.add("END_IF;");
        } else if (stat instanceof ReturnFuncStatement) {
            ReturnFuncStatement rstat = (ReturnFuncStatement)stat;
            String name = getPlcName(func);
            List<Expression> values = rstat.getValues();
            Expression value = makeTuple(values);
            CodeBox c = pou.body;
            c.add("%s := %s;", name, transExpr(value, null, false));
            c.add("RETURN;");
        } else if (stat instanceof WhileFuncStatement) {
            WhileFuncStatement wstat = (WhileFuncStatement)stat;
            CodeBox c = pou.body;
            c.add("WHILE %s DO", transPreds(wstat.getGuards(), null, false));
            c.indent();
            transStatements(func, wstat.getStatements(), pou);
            c.dedent();
            c.add("END_WHILE;");
        } else {
            throw new RuntimeException("Unknown func stat: " + stat);
        }
    }

    /**
     * Transforms a CIF type to PLC code.
     *
     * @param type The CIF type.
     * @return The Structured Text type.
     * @see CifToPlcPreChecker#walkCifType
     */
    private PlcType transType(CifType type) {
        if (type instanceof BoolType) {
            return BOOL_TYPE;
        } else if (type instanceof IntType) {
            return DINT_TYPE;
        } else if (type instanceof RealType) {
            return largeRealType;
        } else if (type instanceof TypeRef) {
            return transType(((TypeRef)type).getType().getType());
        } else if (type instanceof TupleType) {
            String name = transTupleType((TupleType)type);
            return new PlcDerivedType(name);
        } else if (type instanceof EnumType) {
            String name = getPlcName(((EnumType)type).getEnum());
            return new PlcDerivedType(name);
        } else if (type instanceof StringType) {
            throw new RuntimeException("precond violation: " + type);
        } else if (type instanceof ListType) {
            // Must be an array type, due to preconditions.
            ListType arrayType = (ListType)type;
            int lower = arrayType.getLower();
            int upper = arrayType.getUpper();
            Assert.check(lower == upper);
            int size = lower;
            PlcType elemType = transType(arrayType.getElementType());
            return new PlcArrayType(0, (size == 0) ? 0 : size - 1, elemType);
        } else if (type instanceof SetType) {
            throw new RuntimeException("precond violation: " + type);
        } else if (type instanceof DictType) {
            throw new RuntimeException("precond violation: " + type);
        } else if (type instanceof DistType) {
            throw new RuntimeException("precond violation: " + type);
        } else if (type instanceof FuncType) {
            throw new RuntimeException("precond violation: " + type);
        } else {
            throw new RuntimeException("Unexpected type: " + type);
        }
    }

    /**
     * Transforms a CIF tuple type to a PLC structure. The first call for a tuple type determines the name to use, and
     * generates the code for the structure. Any subsequent calls for the same tuple type return the same structure
     * name. The generated names are globally unique, valid IEC 61131-3 identifiers, and are never keywords.
     *
     * @param tupleType The CIF tuple type.
     * @return The name of the PLC structure generated for the tuple type.
     */
    private String transTupleType(TupleType tupleType) {
        TypeEqHashWrap typeWrap = new TypeEqHashWrap(tupleType, true, false);
        String sname = structNames.get(typeWrap);
        if (sname == null) {
            // Generate struct for tuple.
            int nr = nextStructNr;
            nextStructNr++;
            sname = "TupleStruct_" + nr;

            structNames.put(typeWrap, sname);

            PlcStructType structType = new PlcStructType();
            List<Field> fields = tupleType.getFields();
            for (Field field: fields) {
                String fieldName = getPlcName(field);
                PlcType ftype = transType(field.getType());
                structType.fields.add(new PlcVariable(fieldName, ftype));
            }

            PlcTypeDecl typeDecl = new PlcTypeDecl(sname, structType);
            project.typeDecls.add(typeDecl);

            // Generate function, to create tuple literal.
            String fname = "make" + sname;
            PlcPou func = new PlcPou(fname, PlcPouType.FUNCTION, new PlcDerivedType(sname));
            project.pous.add(func);

            for (Field field: fields) {
                String fieldName = getPlcName(field);
                PlcType ftype = transType(field.getType());
                func.inputVars.add(new PlcVariable(fieldName, ftype));
            }

            func.localVars.add(new PlcVariable("rslt", new PlcDerivedType(sname)));

            for (Field field: fields) {
                String fieldName = getPlcName(field);
                func.body.add("rslt.%s := %s;", fieldName, fieldName);
            }
            func.body.add("%s := rslt;", fname);

            // Generate functions, to project tuple fields.
            for (int i = 0; i < fields.size(); i++) {
                fname = fmt("proj%d_%s", i, sname);

                Field field = fields.get(i);
                func = new PlcPou(fname, PlcPouType.FUNCTION, transType(field.getType()));
                project.pous.add(func);

                func.inputVars.add(new PlcVariable("tuple", new PlcDerivedType(sname)));

                func.body.add("%s := tuple.%s;", fname, getPlcName(field));
            }
        }
        return sname;
    }

    /**
     * Adds a CIF array type, giving it a unique number, if it wasn't already previously added.
     *
     * @param arrayType The CIF array type.
     * @return The unique number for this array type.
     */
    private int addArrayType(ListType arrayType) {
        TypeEqHashWrap typeWrap = new TypeEqHashWrap(arrayType, true, false);
        Integer arrayNr = arrayNrs.get(typeWrap);
        if (arrayNr == null) {
            arrayNr = arrayNrs.size();
            arrayNrs.put(typeWrap, arrayNr);
        }
        return arrayNr;
    }

    /**
     * Generates an array literal creation function, for the given array type, if not already generated.
     *
     * @param arrayType The CIF array type.
     * @return The name of the array literal creation function.
     *
     * @see #arrayLitCreateFuncGenerated
     */
    private String genArrayLitCreateFunc(ListType arrayType) {
        int arrayNr = addArrayType(arrayType);
        String fname = "makeArray" + str(arrayNr);
        if (arrayLitCreateFuncGenerated.contains(arrayNr)) {
            return fname;
        }
        arrayLitCreateFuncGenerated.add(arrayNr);

        PlcArrayType type = (PlcArrayType)transType(arrayType);
        PlcPou func = new PlcPou(fname, PlcPouType.FUNCTION, type);
        project.pous.add(func);

        for (int i = type.lower; i <= type.upper; i++) {
            String elemName = "elem" + str(i);
            func.inputVars.add(new PlcVariable(elemName, type.elemType));
        }

        func.localVars.add(new PlcVariable("rslt", type));

        for (int i = type.lower; i <= type.upper; i++) {
            String elemName = "elem" + str(i);
            func.body.add("rslt[%d] := %s;", i, elemName);
        }
        func.body.add("%s := rslt;", fname);

        return fname;
    }

    /**
     * Generates an array projection function, for the given array type, if not already generated.
     *
     * @param arrayType The CIF array type.
     * @return The name of the array projection function.
     *
     * @see #arrayProjFuncGenerated
     */
    private String genArrayProjFunc(ListType arrayType) {
        int arrayNr = addArrayType(arrayType);
        String fname = "projArray" + str(arrayNr);
        if (arrayProjFuncGenerated.contains(arrayNr)) {
            return fname;
        }
        arrayProjFuncGenerated.add(arrayNr);

        PlcArrayType type = (PlcArrayType)transType(arrayType);
        PlcPou func = new PlcPou(fname, PlcPouType.FUNCTION, type.elemType);
        project.pous.add(func);

        func.inputVars.add(new PlcVariable("arr", type));
        func.inputVars.add(new PlcVariable("idx", DINT_TYPE));

        String normProjIdxName = genNormProjIdxFunc();
        int size = arrayType.getLower();
        String normTxt = genFuncCall(normProjIdxName, false, list("idx", "size"), list("idx", str(size)));
        func.body.add("%s := arr[%s];", fname, normTxt);

        return fname;
    }

    /**
     * Generates the 'normProjIdx' function, if not already generated.
     *
     * @return The name of the function, i.e. {@code "normProjIdx"}.
     * @see #normProjIdxGenerated
     */
    private String genNormProjIdxFunc() {
        // Only generate it once.
        String fname = "normProjIdx";
        if (normProjIdxGenerated) {
            return fname;
        }
        normProjIdxGenerated = true;

        // Generate 'normProjIdx' function.
        PlcPou func = new PlcPou(fname, PlcPouType.FUNCTION, DINT_TYPE);
        project.pous.add(func);

        func.inputVars.add(new PlcVariable("idx", DINT_TYPE));
        func.inputVars.add(new PlcVariable("size", DINT_TYPE));

        func.localVars.add(new PlcVariable("tmp", DINT_TYPE));

        func.body.add("tmp := idx;");
        func.body.add("IF tmp < 0 THEN");
        func.body.indent();
        func.body.add("tmp := size + tmp;");
        func.body.dedent();
        func.body.add("END_IF;");
        func.body.add("%s := tmp;", fname);
        return fname;
    }

    /**
     * Transforms CIF predicates to PLC code, assuming conjunction.
     *
     * @param preds The CIF predicates.
     * @param state The textual reference of the state variable. May be {@code null} if and only if the caller knows the
     *     expression can't refer to state, directly or indirectly.
     * @param init Whether the predicates to transform are for the initialization of a variable. That is, for the
     *     initial value with the declaration of the variable, and not in a statement or so.
     * @return The text to use to refer to the result of evaluation of the predicates, in the PLC code.
     */
    private String transPreds(List<Expression> preds, String state, boolean init) {
        if (preds.isEmpty()) {
            return "TRUE";
        }
        List<String> txts = listc(preds.size());
        for (Expression pred: preds) {
            txts.add(fmt("(%s)", transExpr(pred, state, init)));
        }
        return String.join(" AND ", txts);
    }

    /**
     * Transforms a CIF expression to PLC code.
     *
     * @param expr The CIF expression.
     * @param state The textual reference of the state variable. May be {@code null} if and only if the caller knows the
     *     expression can't refer to state, directly or indirectly.
     * @param init Whether the expression to transform is for the initialization of a variable. That is, for the initial
     *     value with the declaration of the variable, and not in a statement or so.
     * @return The text to use to refer to the result of evaluation of the expression, in the PLC code.
     */
    private String transExpr(Expression expr, String state, boolean init) {
        if (expr instanceof BoolExpression) {
            return ((BoolExpression)expr).isValue() ? "TRUE" : "FALSE";
        } else if (expr instanceof IntExpression) {
            return str(((IntExpression)expr).getValue());
        } else if (expr instanceof RealExpression) {
            String rslt = ((RealExpression)expr).getValue();
            int idx = rslt.indexOf('.');
            if (idx == -1) {
                idx = rslt.indexOf('e');
                if (idx == -1) {
                    idx = rslt.indexOf('E');
                }
                rslt = rslt.substring(0, idx) + ".0" + rslt.substring(idx);
            }
            return rslt;
        } else if (expr instanceof StringExpression) {
            throw new RuntimeException("precond violation");
        } else if (expr instanceof TimeExpression) {
            Assert.notNull(state);
            return state + ".curTime";
        } else if (expr instanceof CastExpression) {
            CastExpression cexpr = (CastExpression)expr;
            CifType ctype = normalizeType(cexpr.getChild().getType());
            CifType rtype = normalizeType(cexpr.getType());
            if (ctype instanceof IntType && rtype instanceof RealType) {
                String childTxt = transExpr(cexpr.getChild(), state, init);
                return genFuncCall(fmt("DINT_TO_%s", largeRealType.name), true, "IN", childTxt);
            }
            if (CifTypeUtils.checkTypeCompat(ctype, rtype, RangeCompat.EQUAL)) {
                // Ignore cast expression.
                return transExpr(cexpr.getChild(), state, init);
            }

            throw new RuntimeException("precond violation");
        } else if (expr instanceof UnaryExpression) {
            UnaryExpression uexpr = (UnaryExpression)expr;
            Expression child = uexpr.getChild();
            String childTxt = transExpr(child, state, init);
            UnaryOperator op = uexpr.getOperator();
            switch (op) {
                case INVERSE:
                    return genFuncCall("NOT", true, null, childTxt);

                case NEGATE:
                    if (child instanceof IntExpression || child instanceof RealExpression) {
                        return fmt("-%s", childTxt);
                    }
                    return fmt("-(%s)", childTxt);

                case PLUS:
                    return childTxt;

                case SAMPLE:
                    throw new RuntimeException("precond violation");

                default:
                    throw new RuntimeException("Unknown unop: " + op);
            }
        } else if (expr instanceof BinaryExpression) {
            BinaryExpression bexpr = (BinaryExpression)expr;
            String left = transExpr(bexpr.getLeft(), state, init);
            String right = transExpr(bexpr.getRight(), state, init);
            CifType ltype = normalizeType(bexpr.getLeft().getType());
            CifType rtype = normalizeType(bexpr.getRight().getType());
            BinaryOperator op = bexpr.getOperator();
            Pair<String, String> leftRight;
            switch (op) {
                case IMPLICATION:
                    return fmt("%s OR (%s)", genFuncCall("NOT", true, null, left), right);

                case BI_CONDITIONAL:
                    return fmt("(%s) = (%s)", left, right);

                case DISJUNCTION:
                    if (ltype instanceof BoolType) {
                        return fmt("(%s) OR (%s)", left, right);
                    }

                    throw new RuntimeException("precond violation");

                case CONJUNCTION:
                    if (ltype instanceof BoolType) {
                        return fmt("(%s) AND (%s)", left, right);
                    }

                    throw new RuntimeException("precond violation");

                case LESS_THAN:
                    // S7-400 and S7-300 only support less than on the same types.
                    leftRight = convertBinaryLeftRight(left, right, ltype, rtype);
                    return fmt("(%s) < (%s)", leftRight.left, leftRight.right);

                case LESS_EQUAL:
                    // S7-400 and S7-300 only support less equal on the same types.
                    leftRight = convertBinaryLeftRight(left, right, ltype, rtype);
                    return fmt("(%s) <= (%s)", leftRight.left, leftRight.right);

                case GREATER_THAN:
                    // S7-400 and S7-300 only support greater than on the same types.
                    leftRight = convertBinaryLeftRight(left, right, ltype, rtype);
                    return fmt("(%s) > (%s)", leftRight.left, leftRight.right);

                case GREATER_EQUAL:
                    // S7-400 and S7-300 only support greater equal on the same types.
                    leftRight = convertBinaryLeftRight(left, right, ltype, rtype);
                    return fmt("(%s) >= (%s)", leftRight.left, leftRight.right);

                case EQUAL:
                    // Comparing structure types is not allowed in IEC 61131-3,
                    // and thus equality on tuples can't be supported directly.
                    // We could always create a function for it though.
                    if (ltype instanceof BoolType || ltype instanceof IntType || ltype instanceof RealType
                            || ltype instanceof EnumType)
                    {
                        return fmt("(%s) = (%s)", left, right);
                    }

                    throw new RuntimeException("precond violation");

                case UNEQUAL:
                    // Comparing structure types is not allowed in IEC 61131-3,
                    // and thus equality on tuples can't be supported directly.
                    // We could always create a function for it though.
                    if (ltype instanceof BoolType || ltype instanceof IntType || ltype instanceof RealType
                            || ltype instanceof EnumType)
                    {
                        return fmt("(%s) <> (%s)", left, right);
                    }

                    throw new RuntimeException("precond violation");

                case ADDITION:
                    // S7-400 and S7-300 only support addition on the same types.
                    leftRight = convertBinaryLeftRight(left, right, ltype, rtype);
                    if (ltype instanceof IntType || ltype instanceof RealType) {
                        return fmt("(%s) + (%s)", leftRight.left, leftRight.right);
                    }

                    throw new RuntimeException("precond violation");

                case SUBTRACTION:
                    // S7-400 and S7-300 only support subtraction on the same types.
                    leftRight = convertBinaryLeftRight(left, right, ltype, rtype);
                    if (ltype instanceof IntType || ltype instanceof RealType) {
                        return fmt("(%s) - (%s)", leftRight.left, leftRight.right);
                    }

                    throw new RuntimeException("precond violation");

                case MULTIPLICATION:
                    // S7-400 and S7-300 only support multiplication on the same types.
                    leftRight = convertBinaryLeftRight(left, right, ltype, rtype);
                    return fmt("(%s) * (%s)", leftRight.left, leftRight.right);

                case DIVISION:
                    // S7-400 and S7-300 only support division on the same types.
                    if (ltype instanceof IntType && rtype instanceof IntType) {
                        // Left value will become real type.
                        leftRight = convertBinaryLeftRight(left, right, newRealType(), rtype);

                        // Ensure real valued result.
                        String toName = fmt("DINT_TO_%s", largeRealType.name);
                        return fmt("%s / (%s)", genFuncCall(toName, true, "IN", leftRight.left), leftRight.right);
                    }

                    leftRight = convertBinaryLeftRight(left, right, ltype, rtype);
                    return fmt("(%s) / (%s)", leftRight.left, leftRight.right);

                case INTEGER_DIVISION:
                    // Truncated towards zero in both CIF and IEC 61131-3.
                    return fmt("(%s) / (%s)", left, right);

                case MODULUS:
                    // Note that in CIF division by zero is an error, while
                    // in IEC 61131-3 it results in zero.
                    return fmt("(%s) MOD (%s)", left, right);

                case ELEMENT_OF:
                    throw new RuntimeException("precond violation");

                case SUBSET:
                    throw new RuntimeException("precond violation");

                default:
                    throw new RuntimeException("Unknown binop: " + op);
            }
        } else if (expr instanceof IfExpression) {
            // Create function for the 'if' expression.
            int nr = nextIfFuncNr;
            String name = "ifExprFunc" + nr;
            nextIfFuncNr++;

            PlcType rtype = transType(expr.getType());
            PlcPou func = new PlcPou(name, PlcPouType.FUNCTION, rtype);
            project.pous.add(func);

            // Add parameters for state, as well as function parameters and
            // local variables of functions (if they occur in the 'if'
            // expression). If no parameters needed, add dummy one, as
            // functions without parameters are not allowed.
            String fstate;
            boolean funcDummyParam = false;
            if (state == null) {
                fstate = null;

                // Get function parameters and local variables of the function,
                // referred to in the 'if' expression. Each unique variable is
                // is only added once.
                List<Expression> refs = list();
                Set<DiscVariable> inputs = set();
                CifScopeUtils.collectRefExprs(expr, refs);
                for (Expression ref: refs) {
                    if (ref instanceof DiscVariableExpression) {
                        DiscVariable var = ((DiscVariableExpression)ref).getVariable();
                        EObject parent = var.eContainer();
                        if (parent instanceof ComplexComponent) {
                            continue;
                        }
                        inputs.add(var);
                    }
                }

                // Add parameters, to pass along those 'variables'.
                for (DiscVariable input: inputs) {
                    PlcType type = transType(input.getType());
                    func.inputVars.add(new PlcVariable(getPlcName(input), type));
                }

                // If no parameters, add dummy one.
                if (inputs.isEmpty()) {
                    funcDummyParam = true;
                    func.inputVars.add(new PlcVariable("dummy", INT_TYPE));
                }
            } else {
                fstate = "state";
                func.inputVars.add(new PlcVariable("state", STATE_TYPE));
            }

            // Add code for 'if' statement, for the 'if' expression.
            IfExpression ifExpr = (IfExpression)expr;
            func.body.add("IF %s THEN", transPreds(ifExpr.getGuards(), fstate, init));
            func.body.indent();
            func.body.add("%s := %s;", name, transExpr(ifExpr.getThen(), fstate, init));
            func.body.dedent();
            for (ElifExpression elifExpr: ifExpr.getElifs()) {
                func.body.add("ELSIF %s THEN", transPreds(elifExpr.getGuards(), fstate, init));
                func.body.indent();
                func.body.add("%s := %s;", name, transExpr(elifExpr.getThen(), fstate, init));
                func.body.dedent();
            }
            func.body.add("ELSE");
            func.body.indent();
            func.body.add("%s := %s;", name, transExpr(ifExpr.getElse(), fstate, init));
            func.body.dedent();
            func.body.add("END_IF;");

            // Return function call.
            if (state == null && funcDummyParam) {
                return genFuncCall(name, false, "dummy", "0");
            } else if (state == null) {
                List<String> paramNames = listc(func.inputVars.size());
                List<String> paramValues = listc(func.inputVars.size());
                for (PlcVariable var: func.inputVars) {
                    paramNames.add(var.name);
                    paramValues.add(var.name);
                }
                return genFuncCall(name, false, paramNames, paramValues);
            } else {
                return genFuncCall(name, false, "state", state);
            }
        } else if (expr instanceof ProjectionExpression) {
            // Since projection on function call results etc are not allowed,
            // we generate functions for the projections, and use them instead.
            ProjectionExpression pexpr = (ProjectionExpression)expr;
            CifType ctype = normalizeType(pexpr.getChild().getType());
            Expression child = pexpr.getChild();
            if (ctype instanceof TupleType) {
                Field field = CifValueUtils.getTupleProjField(pexpr);
                TupleType ttype = (TupleType)field.eContainer();
                int idx = ttype.getFields().indexOf(field);
                String fname = fmt("proj%d_%s", idx, transTupleType(ttype));
                String childTxt = transExpr(child, state, init);
                return genFuncCall(fname, false, "tuple", childTxt);
            } else if (ctype instanceof ListType) {
                ListType ltype = (ListType)ctype;
                String childTxt = transExpr(child, state, init);
                String idxTxt = transExpr(pexpr.getIndex(), state, init);

                if (child instanceof DiscVariableExpression || child instanceof ConstantExpression) {
                    // Optimize for direct discrete variable or constant
                    // reference, which we can directly projected.
                    int size = ltype.getLower();
                    String normProjIdxName = genNormProjIdxFunc();
                    String normTxt = genFuncCall(normProjIdxName, false, list("idx", "size"), list(idxTxt, str(size)));
                    return fmt("%s[%s]", childTxt, normTxt);
                } else {
                    // General case. Use function call on projection function.
                    String fname = genArrayProjFunc(ltype);
                    List<String> argTxts = list("arr", "idx");
                    List<String> valueTxts = list(childTxt, idxTxt);
                    return genFuncCall(fname, false, argTxts, valueTxts);
                }
            }

            throw new RuntimeException("precond violation");
        } else if (expr instanceof SliceExpression) {
            throw new RuntimeException("precond violation");
        } else if (expr instanceof FunctionCallExpression) {
            FunctionCallExpression fcexpr = (FunctionCallExpression)expr;

            List<String> paramTxts = listc(fcexpr.getParams().size());
            List<Expression> params = fcexpr.getParams();
            for (Expression param: params) {
                paramTxts.add(transExpr(param, state, init));
            }
            String paramsTxt = String.join(", ", paramTxts);

            Expression fexpr = fcexpr.getFunction();
            if (fexpr instanceof FunctionExpression) {
                // Get function, and generate code for it.
                Function func = ((FunctionExpression)fexpr).getFunction();
                Assert.check(func instanceof InternalFunction);
                PlcPou funcPou = transFunction((InternalFunction)func);

                // Get function and parameter names from the POU, as they may
                // have been renamed.
                List<String> paramNames = listc(funcPou.inputVars.size());
                for (PlcVariable param: funcPou.inputVars) {
                    paramNames.add(param.name);
                }

                // Generate function call.
                return genFuncCall(funcPou.name, false, paramNames, paramTxts);
            } else if (fexpr instanceof StdLibFunctionExpression) {
                StdLibFunction stdlib = ((StdLibFunctionExpression)fexpr).getFunction();
                switch (stdlib) {
                    case ABS:
                        return genFuncCall("ABS", true, null, paramsTxt);

                    case CBRT:
                        if (PlcOutputTypeOption.isS7Output()) {
                            // Use reals to get real result. Use two real-typed values to support S7-400 and S7-300.
                            return fmt("(%s) ** (1.0/3.0)", paramsTxt);
                        }

                        // The 'a ** b' syntax seemed not to work in TwinCAT
                        // 3.1. Using the named function instead.
                        return genFuncCall("EXPT", true, list("IN1", "IN2"), list(paramsTxt, "1.0/3"));

                    case CEIL:
                        // Unsupported. IEC 61131-3 has only TRUNC (round
                        // towards zero) and REAL_TO_INT (rounds to the nearest
                        // even integer if equally far from two integers).
                        throw new RuntimeException("precond violation");

                    case DELETE:
                        // Unsupported.
                        throw new RuntimeException("precond violation");

                    case EMPTY:
                        // Unsupported.
                        throw new RuntimeException("precond violation");

                    case EXP:
                        return genFuncCall("EXP", true, null, paramsTxt);

                    case FLOOR:
                        // Unsupported. IEC 61131-3 has only TRUNC (round
                        // towards zero) and REAL_TO_INT (rounds to the nearest
                        // even integer if equally far from two integers).
                        throw new RuntimeException("precond violation");

                    case FORMAT:
                        // Unsupported.
                        throw new RuntimeException("precond violation");

                    case LN:
                        return genFuncCall("LN", true, null, paramsTxt);

                    case LOG:
                        if (PlcOutputTypeOption.isS7Output()) {
                            // S7 doesn't have a function for log10. But log10(x) = ln(x) / ln(10).
                            return fmt("%s / %s", genFuncCall("LN", true, null, paramsTxt),
                                    genFuncCall("LN", true, null, "10"));
                        }

                        return genFuncCall("LOG", true, null, paramsTxt);

                    case MAXIMUM:
                    case MINIMUM: {
                        CifType type0 = normalizeType(params.get(0).getType());
                        CifType type1 = normalizeType(params.get(1).getType());
                        if ((type0 instanceof IntType && type1 instanceof IntType)
                                || (type0 instanceof RealType && type1 instanceof RealType))
                        {
                            // paramTxts OK.
                        } else if (type0 instanceof IntType && type1 instanceof RealType) {
                            String toName = fmt("DINT_TO_%s", largeRealType.name);
                            paramTxts.set(0, genFuncCall(toName, true, "IN", paramTxts.get(0)));
                        } else {
                            Assert.check(type0 instanceof RealType && type1 instanceof IntType);
                            String toName = fmt("DINT_TO_%s", largeRealType.name);
                            paramTxts.set(1, genFuncCall(toName, true, "IN", paramTxts.get(1)));
                        }
                        return genFuncCall((stdlib == StdLibFunction.MAXIMUM) ? "MAX" : "MIN", true, list("IN1", "IN2"),
                                paramTxts);
                    }

                    case POP:
                        // Unsupported.
                        throw new RuntimeException("precond violation");

                    case POWER: {
                        CifType type0 = normalizeType(params.get(0).getType());
                        CifType type1 = normalizeType(params.get(1).getType());

                        // S7-400 and S7-300 only support power on real types.
                        if (getPlcOutputType() == S7_400 || getPlcOutputType() == S7_300) {
                            String paramTxt0 = paramTxts.get(0);
                            String paramTxt1 = paramTxts.get(1);
                            if (type0 instanceof IntType) {
                                String toName = fmt("%s_TO_%s", largeIntType.name, largeRealType.name);
                                paramTxt0 = genFuncCall(toName, true, "IN", paramTxt0);
                            }

                            if (type1 instanceof IntType) {
                                String toName = fmt("%s_TO_%s", largeIntType.name, largeRealType.name);
                                paramTxt1 = genFuncCall(toName, true, "IN", paramTxt1);
                            }

                            String resultTxt = fmt("(%s) ** (%s)", paramTxt0, paramTxt1);

                            // If the resulting type is integer, we need to convert that explicitly.
                            FuncType functionType = (FuncType)normalizeType(fexpr.getType());
                            CifType resultType = normalizeType(functionType.getReturnType());
                            if (resultType instanceof IntType) {
                                String toName = fmt("%s_TO_%s", largeRealType.name, largeIntType.name);
                                resultTxt = genFuncCall(toName, true, "IN", resultTxt);
                            }

                            return resultTxt;
                        }

                        // S7-1500 and S7-1200 use the 'a ** b' syntax for power.
                        if (getPlcOutputType() == S7_1500 || getPlcOutputType() == S7_1200) {
                            return fmt("(%s) ** (%s)", paramTxts.get(0), paramTxts.get(1));
                        }

                        // The 'a ** b' syntax seemed not to work in TwinCAT
                        // 3.1. Using the named function instead.
                        if (type0 instanceof IntType && type1 instanceof IntType && !isRangeless((IntType)type0)
                                && !isRangeless((IntType)type1))
                        {
                            // First parameter must be of real type.
                            String f0 = fmt("DINT_TO_%s", largeRealType.name);
                            String c1 = genFuncCall(f0, true, "IN", paramTxts.get(0));
                            String c2 = genFuncCall("EXPT", true, list("IN1", "IN2"), list(c1, paramTxts.get(1)));
                            String f1 = fmt("%s_TO_DINT", largeRealType.name);
                            return genFuncCall(f1, true, "IN", c2);
                        } else if (type0 instanceof IntType && type1 instanceof RealType) {
                            // First parameter must be of real type.
                            String cf = fmt("DINT_TO_%s", largeRealType.name);
                            String f0 = genFuncCall(cf, true, "IN", paramTxts.get(0));
                            return genFuncCall("EXPT", true, list("IN1", "IN2"), list(f0, paramTxts.get(1)));
                        } else {
                            return genFuncCall("EXPT", true, list("IN1", "IN2"), paramTxts);
                        }
                    }

                    case ROUND:
                        // Unsupported. IEC 61131-3 has only TRUNC (round
                        // towards zero) and REAL_TO_INT (rounds to the nearest
                        // even integer if equally far from two integers).
                        throw new RuntimeException("precond violation");

                    case SCALE:
                        // Unsupported. We could support this by expanding
                        // it to the definition of 'scale', using addition,
                        // subtraction, etc.
                        throw new RuntimeException("precond violation");

                    case SIGN:
                        // Unsupported. We could support this by adding our
                        // own sign function.
                        throw new RuntimeException("precond violation");

                    case SIZE:
                        // Unsupported.
                        throw new RuntimeException("precond violation");

                    case SQRT:
                        return genFuncCall("SQRT", true, null, paramsTxt);

                    case ACOS:
                        return genFuncCall("ACOS", true, null, paramsTxt);

                    case ASIN:
                        return genFuncCall("ASIN", true, null, paramsTxt);

                    case ATAN:
                        return genFuncCall("ATAN", true, null, paramsTxt);

                    case COS:
                        return genFuncCall("COS", true, null, paramsTxt);

                    case SIN:
                        return genFuncCall("SIN", true, null, paramsTxt);

                    case TAN:
                        return genFuncCall("TAN", true, null, paramsTxt);

                    case ACOSH:
                    case ASINH:
                    case ATANH:
                    case COSH:
                    case SINH:
                    case TANH:
                        // Unsupported.
                        throw new RuntimeException("precond violation");

                    case BERNOULLI:
                    case BETA:
                    case BINOMIAL:
                    case CONSTANT:
                    case ERLANG:
                    case EXPONENTIAL:
                    case GAMMA:
                    case GEOMETRIC:
                    case LOG_NORMAL:
                    case NORMAL:
                    case POISSON:
                    case RANDOM:
                    case TRIANGLE:
                    case UNIFORM:
                    case WEIBULL:
                        // Unsupported.
                        throw new RuntimeException("precond violation");
                }
            }

            throw new RuntimeException("precond violation");
        } else if (expr instanceof ListExpression) {
            // Transform the elements.
            ListExpression lexpr = (ListExpression)expr;
            List<String> elemTxts = listc(lexpr.getElements().size());
            for (int i = 0; i < lexpr.getElements().size(); i++) {
                Expression elem = lexpr.getElements().get(i);
                String valueTxt = transExpr(elem, state, init);
                elemTxts.add(valueTxt);
            }

            // Optimize for initialization value, as then we can use literals,
            // and literals have the best performance. However, for the general
            // case we can't use literals, so we generate a function per array
            // type.
            if (init) {
                return fmt("[%s]", String.join(", ", elemTxts));
            } else {
                ListType ltype = (ListType)normalizeType(lexpr.getType());
                List<String> argTxts = listc(lexpr.getElements().size());
                for (int i = 0; i < lexpr.getElements().size(); i++) {
                    argTxts.add(fmt("elem%d", i));
                }
                String name = genArrayLitCreateFunc(ltype);
                return genFuncCall(name, false, argTxts, elemTxts);
            }
        } else if (expr instanceof SetExpression) {
            throw new RuntimeException("precond violation");
        } else if (expr instanceof TupleExpression) {
            // Transform the elements.
            TupleExpression texpr = (TupleExpression)expr;
            List<String> elemTxts = listc(texpr.getFields().size());
            for (int i = 0; i < texpr.getFields().size(); i++) {
                Expression value = texpr.getFields().get(i);
                String valueTxt = transExpr(value, state, init);
                elemTxts.add(valueTxt);
            }

            // Optimize for initialization value, as then we can use literals,
            // and literals have the best performance. However, for the general
            // case we can't use literals, so we generate a function per array
            // type.
            if (init) {
                TupleType ttype = (TupleType)normalizeType(texpr.getType());
                List<String> fieldTxts = listc(texpr.getFields().size());
                for (int i = 0; i < texpr.getFields().size(); i++) {
                    Field field = ttype.getFields().get(i);
                    String fieldTxt = getPlcName(field);
                    fieldTxts.add(fmt("%s:=%s", fieldTxt, elemTxts.get(i)));
                }
                return fmt("(%s)", String.join(", ", fieldTxts));
            } else {
                TupleType ttype = (TupleType)normalizeType(texpr.getType());
                List<String> argTxts = listc(texpr.getFields().size());
                for (int i = 0; i < texpr.getFields().size(); i++) {
                    Field field = ttype.getFields().get(i);
                    String fieldTxt = getPlcName(field);
                    argTxts.add(fieldTxt);
                }
                String name = transTupleType(ttype);
                return genFuncCall("make" + name, false, argTxts, elemTxts);
            }
        } else if (expr instanceof DictExpression) {
            throw new RuntimeException("precond violation");
        } else if (expr instanceof ConstantExpression) {
            Assert.check(constantsAllowed);
            Constant constant = ((ConstantExpression)expr).getConstant();
            return getPlcName(constant);
        } else if (expr instanceof DiscVariableExpression) {
            DiscVariable var = ((DiscVariableExpression)expr).getVariable();
            EObject parent = var.eContainer();
            if (parent instanceof ComplexComponent) {
                // Discrete variable.
                Assert.notNull(state);
                return state + "." + getPlcName(var);
            } else {
                // Local variable or parameter of a function.
                return getPlcName(var);
            }
        } else if (expr instanceof AlgVariableExpression) {
            Assert.notNull(state);
            AlgVariable var = ((AlgVariableExpression)expr).getVariable();
            return genFuncCall(getPlcName(var), false, "state", state);
        } else if (expr instanceof ContVariableExpression) {
            Assert.notNull(state);
            ContVariableExpression cvexpr = (ContVariableExpression)expr;
            ContVariable var = cvexpr.getVariable();
            if (cvexpr.isDerivative()) {
                return genFuncCall("deriv" + getPlcName(var), false, "state", state);
            } else {
                return state + "." + getPlcName(var);
            }
        } else if (expr instanceof LocationExpression) {
            throw new RuntimeException("loc expr unexpected in lin spec");
        } else if (expr instanceof EnumLiteralExpression) {
            // We have at most a single enumeration after linearization. There is
            // no need to prefix literals with the enumeration, as the literal
            // names are globally unique as well.
            EnumLiteral lit = ((EnumLiteralExpression)expr).getLiteral();
            return getPlcName(lit);
        } else if (expr instanceof FunctionExpression) {
            throw new RuntimeException("precond violation");
        } else if (expr instanceof InputVariableExpression) {
            InputVariable var = ((InputVariableExpression)expr).getVariable();
            return getPlcName(var);
        } else {
            throw new RuntimeException("Unexpected expr: " + expr);
        }
    }

    /**
     * Converts two expressions to compatible types that can be used as left and right expressions in a binary
     * expression. Only supports integer and real types.
     *
     * <p>
     * For S7-400 and S7-300, the left and right expression in a binary expression must have the same type. If one
     * expression has integer type and the other expression has real type, an int-to-real cast is added to the
     * integer-typed expression.
     * </p>
     *
     * @param left The left expression.
     * @param right The right expression.
     * @param ltype The normalized type of the left expression.
     * @param rtype The normalized type of the right expression.
     * @return The new left and right expressions.
     */
    private Pair<String, String> convertBinaryLeftRight(String left, String right, CifType ltype, CifType rtype) {
        if (getPlcOutputType() != S7_400 && getPlcOutputType() != S7_300) {
            return new Pair<>(left, right);
        }

        // Both integer-typed is supported.
        if (ltype instanceof IntType && rtype instanceof IntType) {
            return new Pair<>(left, right);
        }

        // Both real-typed is supported.
        if (ltype instanceof RealType && rtype instanceof RealType) {
            return new Pair<>(left, right);
        }

        // If the left expression is integer-typed, add an int-to-real cast.
        if (ltype instanceof IntType) {
            String toName = fmt("%s_TO_%s", largeIntType.name, largeRealType.name);
            left = genFuncCall(toName, true, "IN", left);
            return new Pair<>(left, right);
        }

        // If the right expression is integer-typed, add an int-to-real cast.
        if (rtype instanceof IntType) {
            String toName = fmt("%s_TO_%s", largeIntType.name, largeRealType.name);
            right = genFuncCall(toName, true, "IN", right);
            return new Pair<>(left, right);
        }

        throw new RuntimeException(
                "precond violation: Binary expression left and right must be integer or real typed.");
    }

    /**
     * Transforms a CIF edge to PLC code.
     *
     * @param edge The CIF edge.
     * @param pou The POU in which to generate code.
     */
    private void transEdge(Edge edge, PlcPou pou) {
        // Get event.
        Assert.check(edge.getEvents().size() == 1);
        Expression eventRef = first(edge.getEvents()).getEvent();
        Event event = (eventRef instanceof TauExpression) ? null : ((EventExpression)eventRef).getEvent();
        String eventName = (event == null) ? "tau" : getAbsName(event);

        // Header.
        CodeBox c = pou.body;
        c.add();
        c.add("// Event \"%s\".", eventName);

        // Only execute event if no event yet executed in this loop. This
        // helps ensures the same behavior as simulator in automatic/first
        // mode.
        //
        // We can't use 'CONTINUE' at the end of the body of the executed
        // event, as 'CONTINUE' is not part of the official standard.
        //
        // We don't combine this condition with the guard of the edge, as parts
        // of the guard may be evaluated into intermediate variables, and we
        // don't want to evaluate them if we won't take the edge anyway.
        c.add("IF NOT progress THEN");
        c.indent();

        // Get guard. After linearization, there should be exactly one guard,
        // unless it is optimized away by simplification.
        List<Expression> guards = edge.getGuards();
        Assert.check(guards.size() <= 1);
        boolean unrestricted;
        Expression guard = guards.isEmpty() ? null : first(guards);

        // Check for unrestricted event.
        if (guards.isEmpty()) {
            unrestricted = true;
        } else {
            unrestricted = CifValueUtils.isTriviallyTrue(guard, false, true);
        }
        if (unrestricted) {
            String msg = fmt("Event \"%s\" is unrestricted (always enabled), and would result in infinitely "
                    + "running PLC code.", eventName);
            throw new InvalidInputException(msg);
        }

        // Special case for large guards, with lots of and/or operators at the
        // top level. Cut them into parts and evaluate them into separate
        // variables, to ensure we don't get too large guard expressions, which
        // can cause compilation errors.
        guard = NaryExpressionConverter.convert(guard);

        // Add event code.
        c.add("IF %s THEN", transGuard(guard, 1, genStaticVarRef("state0"), pou));
        c.indent();
        c.add("progress := TRUE;");
        c.add("state1 := %s;", genStaticVarRef("state0"));
        c.add();
        transUpdates(edge.getUpdates(), pou);
        c.add();
        c.add("%s := state1;", genStaticVarRef("state0"));
        c.dedent();
        c.add("END_IF;");
        c.dedent();
        c.add("END_IF;");
    }

    /**
     * Transforms a CIF guard expression to PLC code. This method can handle the {@link NaryExpression} instances.
     *
     * @param expr The expression to transform.
     * @param level The level of the expression. The guard is level 1. Any children of n-ary expressions have a level
     *     that is 1 higher than their parent.
     * @param state The textual reference of the state variable. Must not be {@code null}.
     * @param pou The POU in which to generate code.
     * @return The text to use to
     */
    private String transGuard(Expression expr, int level, String state, PlcPou pou) {
        // If not special, use normal expression code generation.
        if (!(expr instanceof NaryExpression)) {
            return transExpr(expr, state, false);
        }

        // Special case for n-ary expressions.
        maxNaryLevel = Math.max(maxNaryLevel, level);
        NaryExpression nexpr = (NaryExpression)expr;
        int cnt = nexpr.children.size();
        for (int i = 0; i < cnt; i++) {
            // Get child.
            Expression child = nexpr.children.get(i);

            // If child is also n-ary expression, execute its code only if
            // needed, to ensure short circuit evaluation.
            if (i > 0 && child instanceof NaryExpression) {
                String guard = "b" + str(level);

                boolean inverse;
                switch (nexpr.operator) {
                    case CONJUNCTION:
                        inverse = false;
                        break;
                    case DISJUNCTION:
                        inverse = true;
                        break;
                    default:
                        String msg = "Unexpected operator: " + nexpr.operator;
                        throw new RuntimeException(msg);
                }
                if (inverse) {
                    guard = genFuncCall("NOT", true, null, guard);
                }

                pou.body.add("IF %s THEN", guard);
                pou.body.indent();
            }

            // Recursive for child.
            String crslt = transGuard(child, level + 1, state, pou);

            // Update variable for this level, for this child.
            if (i == 0) {
                pou.body.add("b%d := %s;", level, crslt);
            } else {
                String opTxt;
                switch (nexpr.operator) {
                    case CONJUNCTION:
                        opTxt = "AND";
                        break;
                    case DISJUNCTION:
                        opTxt = "OR";
                        break;
                    default:
                        String msg = "Unexpected operator: " + nexpr.operator;
                        throw new RuntimeException(msg);
                }
                pou.body.add("b%d := b%d %s (%s);", level, level, opTxt, crslt);
            }

            // Close 'if' if needed.
            if (i > 0 && child instanceof NaryExpression) {
                pou.body.dedent();
                pou.body.add("END_IF;");
            }
        }

        // Return the variable in which the result for this expression is
        // stored.
        return "b" + level;
    }

    /**
     * Transforms CIF updates to PLC code.
     *
     * @param updates The CIF updates.
     * @param pou The POU in which to generate the code.
     */
    private void transUpdates(List<Update> updates, PlcPou pou) {
        for (Update update: updates) {
            transUpdate(update, pou);
        }
    }

    /**
     * Transforms a CIF update to PLC code.
     *
     * @param update The CIF update.
     * @param pou The POU in which to generate the code.
     */
    private void transUpdate(Update update, PlcPou pou) {
        if (update instanceof Assignment) {
            // Assignment.
            Assignment asgn = (Assignment)update;
            transAssignment(asgn.getAddressable(), asgn.getValue(), genStaticVarRef("state0"), "state1", pou, "%s",
                    null);
        } else {
            // 'if' update.
            Assert.check(update instanceof IfUpdate);
            IfUpdate ifUpd = (IfUpdate)update;
            CodeBox c = pou.body;
            c.add("IF %s THEN", transPreds(ifUpd.getGuards(), genStaticVarRef("state0"), false));
            c.indent();
            transUpdates(ifUpd.getThens(), pou);
            c.dedent();
            for (ElifUpdate elifUpd: ifUpd.getElifs()) {
                c.add("ELSIF %s THEN", transPreds(elifUpd.getGuards(), genStaticVarRef("state0"), false));
                c.indent();
                transUpdates(elifUpd.getThens(), pou);
                c.dedent();
            }
            if (!ifUpd.getElses().isEmpty()) {
                c.add("ELSE");
                c.indent();
                transUpdates(ifUpd.getElses(), pou);
                c.dedent();
            }
            c.add("END_IF;");
        }
    }

    /**
     * Transforms a CIF assignment to PLC code.
     *
     * @param addr The addressable (the left-hand side).
     * @param value The value (the right-hand side).
     * @param stateBefore The textual reference of the state variable before the assignment. Must be {@code null} for
     *     assignments in functions, and may not be {@code null} for assignments on edges.
     * @param stateAfter The textual reference of the state variable after the assignment. Must be {@code null} for
     *     assignments in functions, and may not be {@code null} for assignments on edges.
     * @param pou The POU in which to generate the code.
     * @param valueProjPat The format pattern to use to insert the value, to allow for additional projections. Initial
     *     calls (for entire addressables) should provide {@code "%s"}. Recursive calls may extend the text.
     * @param varMap Mapping from addressable variable declarations to the temporary variable names to which to assign
     *     their new values. May be {@code null} to use their original names. Must be {@code null} for assignments on
     *     edges. Original names are always obtained via {@link #getPlcName}, while the names from the mapping are used
     *     'as is'.
     */
    private void transAssignment(Expression addr, Expression value, String stateBefore, String stateAfter, PlcPou pou,
            String valueProjPat, Map<Declaration, String> varMap)
    {
        // Tuple addressable.
        if (addr instanceof TupleExpression) {
            TupleExpression taddr = (TupleExpression)addr;
            TupleType ttype = (TupleType)normalizeType(taddr.getType());
            for (int i = 0; i < ttype.getFields().size(); i++) {
                // Since projection on function call results etc are not
                // allowed, we generate functions for the projections, and use
                // them instead.
                Expression childAddr = taddr.getFields().get(i);
                String projName = fmt("proj%d_%s", i, transTupleType(ttype));
                String projTxt = genFuncCall(projName, false, "tuple", "%s");
                transAssignment(childAddr, value, stateBefore, stateAfter, pou, valueProjPat.replace("%s", projTxt),
                        varMap);
            }
            return;
        }

        // Projection addressables. There is no need to check for unsupported
        // projections (on dictionaries etc), as we already transformed the
        // variables, and they have already been checked.
        String addrTxt = "";
        while (addr instanceof ProjectionExpression) {
            ProjectionExpression paddr = (ProjectionExpression)addr;
            CifType ctype = normalizeType(paddr.getChild().getType());
            if (ctype instanceof TupleType) {
                Field field = CifValueUtils.getTupleProjField(paddr);
                addrTxt = "." + getPlcName(field) + addrTxt;
            } else if (ctype instanceof ListType) {
                ListType ltype = (ListType)ctype;
                int size = ltype.getLower();
                String normProjIdxName = genNormProjIdxFunc();
                String idxTxt = transExpr(paddr.getIndex(), stateBefore, false);
                String normTxt = genFuncCall(normProjIdxName, false, list("idx", "size"), list(idxTxt, str(size)));
                addrTxt = fmt("[%s]%s", normTxt, addrTxt);
            } else {
                throw new RuntimeException("Unexpected addr proj: " + ctype);
            }
            addr = paddr.getChild();
        }

        // Get final addressable text.
        String name = null;
        if (addr instanceof DiscVariableExpression) {
            DiscVariable v = ((DiscVariableExpression)addr).getVariable();
            if (varMap != null) {
                name = varMap.get(v);
            }
            if (name == null) {
                name = getPlcName(v);
            }
        } else if (addr instanceof ContVariableExpression) {
            ContVariable v = ((ContVariableExpression)addr).getVariable();
            if (varMap != null) {
                name = varMap.get(v);
            }
            if (name == null) {
                name = getPlcName(v);
            }
        } else {
            throw new RuntimeException("Unknown addr: " + addr);
        }

        addrTxt = name + addrTxt;

        // Generate actual assignment code.
        String valueTxt = fmt(valueProjPat, transExpr(value, stateBefore, false));
        pou.body.add("%s%s := %s;", (stateAfter == null) ? "" : stateAfter + ".", addrTxt, valueTxt);
    }

    /**
     * Returns the name to use in the PLC code, for the given named CIF object. The first call for an object determines
     * the name to use. Any subsequent calls for that same object return the same name. The generated names are globally
     * unique, valid IEC 61131-3 identifiers, and are never keywords.
     *
     * @param obj The named CIF object.
     * @return The name to use in the PLC code for the given object.
     */
    private String getPlcName(PositionObject obj) {
        // Get name from mapping, in case it is a global object that was
        // already mapped before.
        String rslt = objNames.get(obj);
        if (rslt == null) {
            // Special case for fields: don't use unique/absolute names.
            if (obj instanceof Field) {
                // Can't use the field names, as compatible tuple types can
                // have different field names (or no field names at all).
                // Also note that fields have no absolute name.
                Field field = (Field)obj;
                TupleType ttype = (TupleType)field.eContainer();
                int idx = ttype.getFields().indexOf(field);
                return "field" + idx;
            }

            // New object. Get candidate name. The prefixes avoid conflicts
            // with keywords (such as TRUE, true, True, tRue, F_EDGE, R_EDGE,
            // etc).
            String prefix;
            if (obj instanceof AlgVariable) {
                prefix = "alg";
            } else if (obj instanceof Constant) {
                Assert.check(constantsAllowed);
                prefix = "const";
            } else if (obj instanceof ContVariable) {
                prefix = "cvar";
            } else if (obj instanceof DiscVariable) {
                EObject parent = obj.eContainer();
                if (parent instanceof ComplexComponent) {
                    prefix = "dvar";
                } else if (parent instanceof InternalFunction) {
                    prefix = "fvar";
                } else {
                    Assert.check(parent instanceof FunctionParameter);
                    prefix = "farg";
                }
            } else if (obj instanceof EnumDecl) {
                prefix = "enum";
            } else if (obj instanceof Function) {
                // Allow getting the name of both internal and external
                // user-defined functions, for function references. Unsupported
                // functions will be rejected later on.
                prefix = "func";
            } else if (obj instanceof InputVariable) {
                prefix = "ivar";
            } else if (obj instanceof EnumLiteral) {
                prefix = "elit";
            } else if (obj instanceof Field) {
                prefix = "tfld";
            } else {
                throw new RuntimeException("Unexpected named CIF obj: " + obj);
            }

            // Ensure candidate name. Use absolute names if possible, to
            // avoid renaming local names (local variables and parameters of
            // functions, etc). Note that we need to ensure case insensitive
            // unique names.
            String candidate = getAbsName(obj, false).replace('.', '_');

            // Ensure valid candidate name.
            while (candidate.contains("__")) {
                candidate = candidate.replace("__", "_");
            }

            while (candidate.endsWith("_")) {
                candidate = slice(candidate, 0, -1);
            }
            if (candidate.isEmpty()) {
                candidate = "x";
            }
            candidate = prefix + "_" + candidate;

            // Get final name. Rename if needed to ensure unique global name.
            rslt = candidate;
            int count = 1;
            while (names.contains(rslt.toLowerCase(Locale.US))) {
                count++;
                rslt = candidate + str(count);
            }

            // Warn in case of renaming.
            if (!rslt.equals(candidate) && RenameWarningsOption.isEnabled()) {
                warn("PLC name \"%s\" is renamed to \"%s\".", candidate, rslt);
            }

            // Update global storage.
            objNames.put(obj, rslt);
            names.add(rslt.toLowerCase(Locale.US));
        }
        return rslt;
    }

    /**
     * Generates a reference to a static (persistent) variable in IECT 61131-3 syntax. Prefixes the variable with
     * {@link #staticVarPrefix}.
     *
     * @param varName The name of the static variable.
     * @return The reference to the static variable.
     */
    private String genStaticVarRef(String varName) {
        return staticVarPrefix + varName;
    }

    /**
     * Generates a function call in IEC 61131-3 syntax, taking into account when to use the formal function invocation
     * syntax.
     *
     * @param funcName The name of the function to call.
     * @param stdFunc Is the function a standard library/conversion function?
     * @param argName The name of the argument of the function, i.e. the formal argument name. Must be {@code null} iff
     *     function is NOT, ABS, SQRT, LN, LOG, EXP, SIN, COS, TAN, ASIN, ACOS, or ATAN.
     * @param valueTxt The value to use as argument, i.e. the actual argument.
     * @return The function call text.
     * @see #formalInvokeArg
     * @see #formalInvokeFunc
     */
    private String genFuncCall(String funcName, boolean stdFunc, String argName, String valueTxt) {
        return genFuncCall(funcName, stdFunc, (argName == null) ? null : list(argName), list(valueTxt));
    }

    /**
     * Generates a function call in IEC 61131-3 syntax, taking into account when to use the formal function invocation
     * syntax.
     *
     * @param funcName The name of the function to call.
     * @param stdFunc Is the function a standard library/conversion function?
     * @param argNames The names of the arguments of the function, i.e. the formal argument names. Must be {@code null}
     *     iff function is NOT, ABS, SQRT, LN, LOG, EXP, SIN, COS, TAN, ASIN, ACOS, or ATAN.
     * @param valueTxts The values to use as arguments, i.e. the actual arguments.
     * @return The function call text.
     * @see #formalInvokeArg
     * @see #formalInvokeFunc
     */
    @SuppressWarnings("null")
    private String genFuncCall(String funcName, boolean stdFunc, List<String> argNames, List<String> valueTxts) {
        // Paranoia checking.
        boolean formalAllowed = (argNames != null);
        Assert.check(!formalAllowed || argNames.size() == valueTxts.size());

        // Which syntax should be used?
        boolean useFormalArg = false;
        if (formalAllowed) {
            switch (formalInvokeArg) {
                case ALL:
                    useFormalArg = true;
                    break;
                case MULTI:
                    useFormalArg = (argNames.size() > 1);
                    break;
                case NONE:
                    useFormalArg = false;
                    break;
            }
        }

        boolean useFormalFunc = false;
        if (formalAllowed) {
            switch (formalInvokeFunc) {
                case ALL:
                    useFormalFunc = true;
                    break;
                case STD:
                    useFormalFunc = stdFunc;
                    break;
                case OTHERS:
                    useFormalFunc = !stdFunc;
                    break;
            }
        }

        boolean useFormal = formalAllowed && useFormalArg && useFormalFunc;

        // Non-formal syntax.
        if (!useFormal) {
            return fmt("%s(%s)", funcName, String.join(", ", valueTxts));
        }

        // Formal syntax.
        Assert.check(useFormal);
        List<String> argTxts = listc(argNames.size());
        for (int i = 0; i < argNames.size(); i++) {
            argTxts.add(argNames.get(i) + ":=" + valueTxts.get(i));
        }
        return fmt("%s(%s)", funcName, String.join(", ", argTxts));
    }
}
