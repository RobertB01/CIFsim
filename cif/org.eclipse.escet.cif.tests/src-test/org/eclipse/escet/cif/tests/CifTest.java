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

package org.eclipse.escet.cif.tests;

import org.eclipse.escet.tooldef.interpreter.ToolDefBasedPluginUnitTest;
import org.junit.jupiter.api.Test;

/** CIF integration and regression tests. */
public class CifTest extends ToolDefBasedPluginUnitTest {
    /** Type checker tests. */
    @Test
    public void testTypeChecker() {
        test("tests/test_tchecker.tooldef");
    }

    /** CIF checks tests. */
    @Test
    public void testChecks() {
        test("tests/test_checks.tooldef");
    }

    /** Pretty print and elimination of component definition/instantiation tests. */
    @Test
    public void testPprintElimCdef() {
        test("tests/test_pprint_elim_cdef.tooldef");
    }

    /** CIF to CIF tests. */
    @Test
    public void testCifToCif() {
        test("tests/test_cif2cif.tooldef");
    }

    /** Event-based conversion tests. */
    @Test
    public void testEventBasedConversion() {
        test("tests/test_event_based_conversion.tooldef");
    }

    /** Event-based automaton abstraction tests. */
    @Test
    public void testEventBasedAutAbstraction() {
        test("tests/test_event_based_aut_abstraction.tooldef");
    }

    /** Event-based controllability check tests. */
    @Test
    public void testEventBasedCtrlChk() {
        test("tests/test_event_based_ctrl_chk.tooldef");
    }

    /** Event-based DFA minimize tests. */
    @Test
    public void testEventBasedDfaMinimize() {
        test("tests/test_event_based_dfa_minimize.tooldef");
    }

    /** Event-based language equivalence check tests. */
    @Test
    public void testEventBasedLangEquiv() {
        test("tests/test_event_based_lang_equiv.tooldef");
    }

    /** Event-based NFA to DFA tests. */
    @Test
    public void testEventBasedNfaToDfa() {
        test("tests/test_event_based_nfa_to_dfa.tooldef");
    }

    /** Event-based non-conflicting check tests. */
    @Test
    public void testEventBasedNonConflChk() {
        test("tests/test_event_based_nonconfl_chk.tooldef");
    }

    /** Event-based observer check tests. */
    @Test
    public void testEventBasedObsChk() {
        test("tests/test_event_based_obs_chk.tooldef");
    }

    /** Event-based product tests. */
    @Test
    public void testEventBasedProduct() {
        test("tests/test_event_based_product.tooldef");
    }

    /** Event-based projection tests. */
    @Test
    public void testEventBasedProjection() {
        test("tests/test_event_based_projection.tooldef");
    }

    /** Event-based synthesis tests. */
    @Test
    public void testEventBasedSynthesis() {
        test("tests/test_event_based_synthesis.tooldef");
    }

    /** Event-based trim check tests. */
    @Test
    public void testEventBasedTrimChk() {
        test("tests/test_event_based_trim_chk.tooldef");
    }

    /** Event-based trim tests. */
    @Test
    public void testEventBasedTrim() {
        test("tests/test_event_based_trim.tooldef");
    }

    /** CIF to yEd tests. */
    @Test
    public void testCifToYed() {
        test("tests/test_cif2yed.tooldef");
    }

    /** CIF to mCRL2 tests. */
    @Test
    public void testCifToMcrl2() {
        test("tests/test_cif2mcrl2.tooldef");
    }

    /** Code generator tests. */
    @Test
    public void testCodeGen() {
        test("tests/test_codegen.tooldef");
    }

    /** PLC code generator tests. */
    @Test
    public void testCifToPlc() {
        test("tests/test_cif2plc.tooldef");
    }

    /** PLC code generator tests. */
    @Test
    public void testCifPlcGen() {
        test("tests/test_plcgen.tooldef");
    }

    /** CIF to Supremica tests. */
    @Test
    public void testCifToSupremica() {
        test("tests/test_cif2supremica.tooldef");
    }

    /** CIF to UPPAAL tests. */
    @Test
    public void testCifToUppaal() {
        test("tests/test_cif2uppaal.tooldef");
    }

    /** CIF controller checker tests. */
    @Test
    public void testControllerCheck() {
        test("tests/test_controllercheck.tooldef");
    }

    /** Data synthesis tests. */
    @Test
    public void testDataSynth() {
        test("tests/test_datasynth.tooldef");
    }

    /** Event disabler tests. */
    @Test
    public void testEventDisabler() {
        test("tests/test_event_disabler.tooldef");
    }

    /** Explorer tests. */
    @Test
    public void testExplorer() {
        test("tests/test_explorer.tooldef");
    }

    /** Merger tests. */
    @Test
    public void testMerger() {
        test("tests/test_merger.tooldef");
    }

    /** Multi-level synthesis tests. */
    @Test
    public void testMultiLevel() {
        test("tests/test_multilevel.tooldef");
    }

    /** Simulator tests. */
    @Test
    public void testSimulator() {
        test("tests/test_simulator.tooldef");
    }
}
