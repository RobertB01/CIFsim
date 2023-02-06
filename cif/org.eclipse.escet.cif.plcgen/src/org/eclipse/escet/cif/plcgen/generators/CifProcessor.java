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

package org.eclipse.escet.cif.plcgen.generators;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Map;

import org.eclipse.escet.cif.cif2cif.AddDefaultInitialValues;
import org.eclipse.escet.cif.cif2cif.ElimComponentDefInst;
import org.eclipse.escet.cif.cif2cif.ElimConsts;
import org.eclipse.escet.cif.cif2cif.ElimStateEvtExclInvs;
import org.eclipse.escet.cif.cif2cif.EnumsToConsts;
import org.eclipse.escet.cif.cif2cif.EnumsToInts;
import org.eclipse.escet.cif.cif2cif.RemoveIoDecls;
import org.eclipse.escet.cif.cif2cif.SimplifyOthers;
import org.eclipse.escet.cif.cif2cif.SimplifyValues;
import org.eclipse.escet.cif.cif2plc.CifToPlcPreChecker;
import org.eclipse.escet.cif.cif2plc.options.ConvertEnums;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcType;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcVariable;
import org.eclipse.escet.cif.common.CifCollectUtils;
import org.eclipse.escet.cif.common.checkers.CifPreconditionChecker;
import org.eclipse.escet.cif.common.checkers.checks.AutOnlyWithOneInitLocCheck;
import org.eclipse.escet.cif.common.checkers.checks.CompNoInitPredsCheck;
import org.eclipse.escet.cif.common.checkers.checks.EdgeNoUrgentCheck;
import org.eclipse.escet.cif.common.checkers.checks.ExprNoSpecificBinaryExprsCheck;
import org.eclipse.escet.cif.common.checkers.checks.ExprNoSpecificBinaryExprsCheck.NoSpecificBinaryOp;
import org.eclipse.escet.cif.common.checkers.checks.ExprNoSpecificExprsCheck;
import org.eclipse.escet.cif.common.checkers.checks.ExprNoSpecificExprsCheck.NoSpecificExpr;
import org.eclipse.escet.cif.common.checkers.checks.ExprNoSpecificUnaryExprsCheck;
import org.eclipse.escet.cif.common.checkers.checks.ExprNoSpecificUnaryExprsCheck.NoSpecificUnaryOp;
import org.eclipse.escet.cif.common.checkers.checks.FuncNoSpecificIntUserDefFuncStatsCheck;
import org.eclipse.escet.cif.common.checkers.checks.FuncNoSpecificIntUserDefFuncStatsCheck.NoSpecificStatement;
import org.eclipse.escet.cif.common.checkers.checks.FuncNoSpecificStdLibCheck;
import org.eclipse.escet.cif.common.checkers.checks.FuncNoSpecificStdLibCheck.NoSpecificStdLib;
import org.eclipse.escet.cif.common.checkers.checks.FuncNoSpecificUserDefCheck;
import org.eclipse.escet.cif.common.checkers.checks.FuncNoSpecificUserDefCheck.NoSpecificUserDefFunc;
import org.eclipse.escet.cif.common.checkers.checks.InvNoSpecificInvsCheck;
import org.eclipse.escet.cif.common.checkers.checks.LocNoUrgentCheck;
import org.eclipse.escet.cif.common.checkers.checks.SpecAutomataCountsCheck;
import org.eclipse.escet.cif.common.checkers.checks.TypeNoSpecificTypesCheck;
import org.eclipse.escet.cif.common.checkers.checks.TypeNoSpecificTypesCheck.NoSpecificType;
import org.eclipse.escet.cif.common.checkers.checks.VarNoDiscWithMultiInitValuesCheck;
import org.eclipse.escet.cif.common.checkers.checks.invcheck.NoInvariantKind;
import org.eclipse.escet.cif.common.checkers.checks.invcheck.NoInvariantPlaceKind;
import org.eclipse.escet.cif.common.checkers.checks.invcheck.NoInvariantSupKind;
import org.eclipse.escet.cif.io.CifReader;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.plcgen.PlcGenSettings;
import org.eclipse.escet.cif.plcgen.WarnOutput;
import org.eclipse.escet.cif.plcgen.targets.PlcTarget;
import org.eclipse.escet.common.app.framework.exceptions.InvalidInputException;

/** Extracts information from the CIF input file, to be used during PLC code generation. */
public class CifProcessor {
    /** PLC target to generate code for. */
    private final PlcTarget target;

    /** User-specified path to the CIF specification for which to generate PLC code. */
    private final String inputPath;

    /** Absolute path to the CIF specification for which to generate PLC code. */
    private final String absInputPath;

    /** Whether to simplify values during pre-processing. */
    public boolean simplifyValues;

    /** How to treat enumerations. */
    public final ConvertEnums enumConversion;

    /** Callback to send warnings to the user. */
    private final WarnOutput warnOutput;

    /** Type generator. */
    private final TypeGenerator typeGen;

    /** PLC code storage and writer. */
    private final PlcCodeStorage codeStorage;

    /** Generator for obtaining clash-free names in the generated code. */
    private final NameGenerator nameGenerator;

    /** Names of converted declarations. */
    private final Map<Declaration, String> variableNames = map();

    /**
     * Process the input CIF specification, reading it, and extracting the relevant information for PLC code generation.
     *
     * @param target PLC target to generate code for.
     * @param settings Configuration to use.
     * @param typeGen Class for handling types.
     * @param codeStorage Class that stores and writes generated PLC code.
     * @param nameGenerator Generator for obtaining clash-free names in the generated code.
     */
    public CifProcessor(PlcTarget target, PlcGenSettings settings, TypeGenerator typeGen, PlcCodeStorage codeStorage,
            NameGenerator nameGenerator)
    {
        this.target = target;
        inputPath = settings.inputPath;
        absInputPath = settings.absInputPath;
        simplifyValues = settings.simplifyValues;
        enumConversion = settings.enumConversion;
        warnOutput = settings.warnOutput;
        this.typeGen = typeGen;
        this.codeStorage = codeStorage;
        this.nameGenerator = nameGenerator;
    }

    /** Process the input CIF specification, extracting the relevant information for PLC code generation. */
    public void process() {
        // Read CIF specification.
        Specification spec = new CifReader().init(inputPath, absInputPath, false).read();
        widenSpec(spec);
        preCheckSpec(spec);
        normalizeSpec(spec);

        // Convert the discrete and input variables as well as enumeration declarations throughout the specification.
        for (Declaration decl: CifCollectUtils.collectDeclarations(spec, list())) {
            if (decl instanceof DiscVariable discVar) {
                convertVariable(decl, discVar.getType());
            } else if (decl instanceof InputVariable inpVar) {
                convertVariable(decl, inpVar.getType());
            } else if (decl instanceof EnumDecl enumDecl) {
                typeGen.convertEnumDecl(enumDecl);
            }

            // TODO Constants.
            // TODO Initial value -> precheckers restrict to constant initial value.
            // TODO Extend allowed initial values by computing at runtime.
        }
    }

    /**
     * Convert a CIF variable and add it to the global variable table in the PLC.
     *
     * @param decl Discrete variable or input variable to convert.
     * @param type Type of the variable.
     */
    private void convertVariable(Declaration decl, CifType type) {
        PlcType varType = typeGen.convertType(type);
        String varName = nameGenerator.generateName(decl);
        variableNames.put(decl, varName);

        codeStorage.addStateVariable(new PlcVariable(varName, varType));
    }

    /**
     * Eliminate CIF features to widen the set of accepted specifications.
     *
     * @param spec Specification to widen.
     */
    private void widenSpec(Specification spec) {
        // Eliminate component definition/instantiation, to avoid having to handle them.
        new ElimComponentDefInst().transform(spec);

        // Eliminate state/event exclusion invariants, to avoid having to handle them.
        new ElimStateEvtExclInvs().transform(spec); // TODO Is this a good idea wrt finding the original name again?

        // Simplify the specification, to increase the supported subset. Since simplification of values fills in all
        // constants, we can also remove the constants. However, this may lead to large amounts of duplication for
        // constants with large literal values. Therefore, it is an option. We could always use less expensive variants
        // of value simplification, in the future.
        if (simplifyValues) {
            new SimplifyValues().transform(spec);
            new ElimConsts().transform(spec);
        }

        // Simplify the specification, to reduce PLC code size.
        new SimplifyOthers().transform(spec);

        // Remove/ignore I/O declarations, to increase the supported subset.
        RemoveIoDecls removeIoDecls = new RemoveIoDecls();
        removeIoDecls.transform(spec);
        if (removeIoDecls.haveAnySvgInputDeclarationsBeenRemoved()) {
            warnOutput.warn("The specification contains CIF/SVG input declarations. These will be ignored.");
        }
    }

    /**
     * Verify that the specification only contains supported language constructs.
     *
     * @param spec Specification to check.
     */
    private void preCheckSpec(Specification spec) {
        PlcGenPreChecker checker = new PlcGenPreChecker(target.supportsArrays());
        checker.reportPreconditionViolations(spec, "CIF PLC code generator");
    }

    /** CIF PLC code generator precondition checker. Does not support component definition/instantiation. */
    private static class PlcGenPreChecker extends CifPreconditionChecker {
        /**
         * Constructor of the {@link CifToPlcPreChecker} class.
         *
         * @param supportArrays Whether the target supports arrays.
         */
        public PlcGenPreChecker(boolean supportArrays) {
            super(
                    // At least one automaton.
                    new SpecAutomataCountsCheck().setMinMaxAuts(1, SpecAutomataCountsCheck.NO_CHANGE),

                    // No initialization predicates in components, ignoring initialization predicates that trivially
                    // hold.
                    new CompNoInitPredsCheck(true),

                    // Discrete variables must have a single initial value.
                    new VarNoDiscWithMultiInitValuesCheck(),

                    // Automata must have a single initial location.
                    new AutOnlyWithOneInitLocCheck(),

                    // Disallow state invariants, except ones that never block behavior.
                    new InvNoSpecificInvsCheck() //
                            .ignoreNeverBlockingInvariants() //
                            .disallow(NoInvariantSupKind.ALL_KINDS, NoInvariantKind.STATE,
                                    NoInvariantPlaceKind.ALL_PLACES),

                    // No urgency.
                    new LocNoUrgentCheck(), //
                    new EdgeNoUrgentCheck(),

                    // Disallow external user-defined functions, and only allow internal user-defined functions with at
                    // least one parameter.
                    new FuncNoSpecificUserDefCheck(NoSpecificUserDefFunc.EXTERNAL, NoSpecificUserDefFunc.NO_PARAMETER),

                    // Limit internal user-defined function assignments and disallow the 'continue' statement.
                    //
                    // We use CifAddressableUtils.getRefs in the code generation, which doesn't properly handle
                    // multi-assignments to different non-overlapping parts of the same variable.
                    new FuncNoSpecificIntUserDefFuncStatsCheck( //
                            NoSpecificStatement.ASSIGN_MULTI_PARTS_SAME_VAR, //
                            NoSpecificStatement.CONTINUE),

                    // Disallow various types completely and function types in non-call context.
                    new TypeNoSpecificTypesCheck( //
                            NoSpecificType.DICT_TYPES, //
                            NoSpecificType.DIST_TYPES, //
                            NoSpecificType.FUNC_TYPES_AS_DATA, //
                            NoSpecificType.SET_TYPES, //
                            NoSpecificType.STRING_TYPES, //
                            (supportArrays ? NoSpecificType.LIST_TYPES_NON_ARRAY : NoSpecificType.LIST_TYPES)),

                    // Allow only casting to the same type and int to real, allow projection only on tuples and arrays,
                    // forbid string, set, and dictionary literals, forbid slicing, and function references outside call
                    // context.
                    new ExprNoSpecificExprsCheck( //
                            NoSpecificExpr.CAST_EXPRS_FROM_STRING, //
                            NoSpecificExpr.CAST_EXPRS_TO_STRING, //
                            NoSpecificExpr.DICT_LITS, //
                            NoSpecificExpr.FUNC_REFS_USER_DEF_AS_DATA, //
                            NoSpecificExpr.PROJECTION_EXPRS_DICTS, //
                            NoSpecificExpr.PROJECTION_EXPRS_LISTS_NON_ARRAY, //
                            NoSpecificExpr.PROJECTION_EXPRS_STRINGS, //
                            NoSpecificExpr.SET_LITS, //
                            NoSpecificExpr.STRING_LITS, //
                            NoSpecificExpr.SLICE_EXPRS),

                    // Disallow sampling.
                    new ExprNoSpecificUnaryExprsCheck(NoSpecificUnaryOp.SAMPLE),

                    // Disallow element of, and subset operators. Allow conjunction and disjunction only on booleans,
                    // allow equality only on booleans, integers, reals and enums, allow addition and subtraction only
                    // on integers and reals.
                    new ExprNoSpecificBinaryExprsCheck( //
                            NoSpecificBinaryOp.ADDITION_LISTS, //
                            NoSpecificBinaryOp.ADDITION_STRINGS, //
                            NoSpecificBinaryOp.ADDITION_DICTS, //
                            NoSpecificBinaryOp.ELEMENT_OF, //
                            NoSpecificBinaryOp.EQUAL_DICT, //
                            NoSpecificBinaryOp.EQUAL_LIST, //
                            NoSpecificBinaryOp.EQUAL_SET, //
                            NoSpecificBinaryOp.EQUAL_STRING, //
                            NoSpecificBinaryOp.EQUAL_TUPLE, //
                            NoSpecificBinaryOp.SUBSET, //
                            NoSpecificBinaryOp.SUBTRACTION_DICTS, //
                            NoSpecificBinaryOp.SUBTRACTION_LISTS, //
                            NoSpecificBinaryOp.SUBTRACTION_SETS, //
                            NoSpecificBinaryOp.CONJUNCTION_SETS, //
                            NoSpecificBinaryOp.DISJUNCTION_SETS, //
                            NoSpecificBinaryOp.UNEQUAL_DICT, //
                            NoSpecificBinaryOp.UNEQUAL_LIST, //
                            NoSpecificBinaryOp.UNEQUAL_SET, //
                            NoSpecificBinaryOp.UNEQUAL_STRING, //
                            NoSpecificBinaryOp.UNEQUAL_TUPLE),

                    // Limit standard library functions.
                    new FuncNoSpecificStdLibCheck( //
                            NoSpecificStdLib.STD_LIB_STOCHASTIC_GROUP, //
                            NoSpecificStdLib.STD_LIB_ACOSH, //
                            NoSpecificStdLib.STD_LIB_ASINH, //
                            NoSpecificStdLib.STD_LIB_ATANH, //
                            NoSpecificStdLib.STD_LIB_COSH, //
                            NoSpecificStdLib.STD_LIB_SINH, //
                            NoSpecificStdLib.STD_LIB_TANH, //
                            NoSpecificStdLib.STD_LIB_CEIL, //
                            NoSpecificStdLib.STD_LIB_DELETE, //
                            NoSpecificStdLib.STD_LIB_EMPTY, //
                            NoSpecificStdLib.STD_LIB_FLOOR, //
                            NoSpecificStdLib.STD_LIB_FORMAT, //
                            NoSpecificStdLib.STD_LIB_POP, //
                            NoSpecificStdLib.STD_LIB_ROUND, //
                            NoSpecificStdLib.STD_LIB_SCALE, //
                            NoSpecificStdLib.STD_LIB_SIGN, //
                            NoSpecificStdLib.STD_LIB_SIZE)
            //
            );
        }
    }

    /**
     * Normalize CIF features to reduce the number of cases to deal with in the generator.
     *
     * @param spec Specification to normalize.
     */
    private void normalizeSpec(Specification spec) {
        // Add default initial values, to simplify the code generation.
        new AddDefaultInitialValues().transform(spec);

        // If requested, convert enumerations.
        if (enumConversion == ConvertEnums.INTS) {
            new EnumsToInts().transform(spec);
        } else if (enumConversion == ConvertEnums.CONSTS) {
            // This transformation introduces new constants that are intentionally not removed if simplify values is
            // enabled.
            new EnumsToConsts().transform(spec);
        } else if (!target.supportsEnumerations()) {
            // Enumerations are not converted.
            String msg = fmt("Enumerations are not converted, while this is required for %s code. Please set the "
                    + "\"Convert enumerations\" option accordingly.", target.targetType.dialogText);
            throw new InvalidInputException(msg);
        }
    }
}
