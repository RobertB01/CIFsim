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

package org.eclipse.escet.cif.simulator.compiler;

import static org.eclipse.escet.cif.simulator.output.DebugOutputType.PARSER;

import org.eclipse.escet.cif.cif2cif.ElimComponentDefInst;
import org.eclipse.escet.cif.cif2cif.RemovePositionInfo;
import org.eclipse.escet.cif.common.CifScopeUtils;
import org.eclipse.escet.cif.io.CifReader;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.simulator.output.DebugOutputOption;
import org.eclipse.escet.cif.typechecker.CifTypeChecker;
import org.eclipse.escet.common.app.framework.Paths;

/** CIF compiler. Loads and compiles CIF specifications to Java code. */
public class CifCompiler {
    /** Constructor for the {@link CifCompiler} class. */
    private CifCompiler() {
        // Static class.
    }

    /**
     * Loads and compiles a CIF ASCII file.
     *
     * @param cifPath The absolute or relative local file system path to the CIF specification to simulate.
     * @param ctxt The compiler context to use.
     */
    public static void compileSpec(String cifPath, CifCompilerContext ctxt) {
        // Read CIF specification.
        String absCifPath = Paths.resolve(cifPath);
        boolean debug = DebugOutputOption.doPrint(PARSER);
        CifReader cifReader = new CifReader().init(cifPath, absCifPath, debug);
        Specification cifSpec = cifReader.read();

        // Remove position information from CIF specification, to free some
        // memory. This takes CPU time, though.
        new RemovePositionInfo().transform(cifSpec);

        // Eliminate component definition/instantiation from the CIF
        // specification.
        if (CifScopeUtils.hasCompDefInst(cifSpec)) {
            new ElimComponentDefInst().transform(cifSpec);
        }

        // Eliminate position information from CIF specification, to
        // free some memory. This takes CPU time, though.
        new RemovePositionInfo().transform(cifSpec);

        // Compile the CIF specification.
        String cifSpecFileDir = cifReader.getTypeChecker().getSourceFileDir();
        compileSpec(cifSpec, ctxt, cifSpecFileDir, cifReader.getTypeChecker());
    }

    /**
     * Compiles a CIF specification.
     *
     * @param cifSpec The CIF specification to compile. Component definitions/instantiations are not supported.
     * @param ctxt The compiler context to use.
     * @param cifSpecFileDir The absolute local file system path of the directory that contains the CIF specification
     *     for which code is generated.
     * @param tchecker The CIF type checker that was used to check the CIF specification that is to be compiled.
     * @see ElimComponentDefInst
     */
    public static void compileSpec(Specification cifSpec, CifCompilerContext ctxt, String cifSpecFileDir,
            CifTypeChecker tchecker)
    {
        // Set specification on context.
        ctxt.setSpecification(cifSpec, cifSpecFileDir);

        // Check specification.
        AutomatonKindChecker.checkKinds(cifSpec);

        // Generate code.
        VersionCodeGenerator.gencodeVersion(ctxt);
        EventCodeGenerator.gencodeEvents(cifSpec, ctxt);
        EnumCodeGenerator.gencodeEnums(ctxt);
        StateCodeGenerator.gencodeState(cifSpec, ctxt);
        StateCodeGenerator.gencodeSubStates(cifSpec, ctxt);
        StateInitCodeGenerator.gencodeStateInit(ctxt);
        AutomatonCodeGenerator.gencodeAutomata(ctxt);
        ConstCodeGenerator.gencodeConsts(cifSpec, ctxt);
        AlgVarCodeGenerator.gencodeAlgVars(ctxt);
        DerivativeCodeGenerator.gencodeDerivatives(cifSpec, ctxt);
        StateInvPredCodeGenerator.gencodeStateInvPreds(cifSpec, ctxt);
        InitPredCodeGenerator.gencodeInitPreds(cifSpec, ctxt);
        FuncCodeGenerator.gencodeFuncs(cifSpec, ctxt);
        OdeStateEventsCodeGenerator.gencodeOdeStateEvents(cifSpec, ctxt);
        UrgLocsCodeGenerator.gencodeUrgLocs(cifSpec, ctxt);
        UrgEdgesCodeGenerator.gencodeUrgEdges(cifSpec, ctxt);
        SolverCodeGenerator.gencodeSolver(cifSpec, ctxt);
        CifSvgCodeGenerator.gencodeCifSvg(cifSpec, ctxt);
        PrintCodeGenerator.gencodePrint(cifSpec, ctxt);
        SpecCodeGenerator.gencodeSpec(ctxt);
        SamplerCodeGenerator.gencodeSampler(ctxt); // As late as possible.
        DefaultValueCodeGenerator.gencodeDefaultValues(ctxt); // As late as possible.
        LiteralCodeGenerator.gencodeLiteralReaders(ctxt); // As late as possible.

        // Write source code to disk, for debugging.
        ctxt.writeSourceCode();

        // Compile code.
        ctxt.compile();

        // Write compiled code to disk, for repeated simulation experiments.
        ctxt.writeCompiledCode();
    }
}
