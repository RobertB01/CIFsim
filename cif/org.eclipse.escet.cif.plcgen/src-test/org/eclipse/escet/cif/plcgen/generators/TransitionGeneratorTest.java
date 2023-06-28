//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023 Contributors to the Eclipse Foundation
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

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.function.Supplier;

import org.eclipse.escet.cif.plcgen.PlcGenSettings;
import org.eclipse.escet.cif.plcgen.WarnOutput;
import org.eclipse.escet.cif.plcgen.options.ConvertEnums;
import org.eclipse.escet.cif.plcgen.options.PlcNumberBits;
import org.eclipse.escet.cif.plcgen.targets.PlcBaseTarget;
import org.eclipse.escet.cif.plcgen.targets.PlcTargetType;
import org.eclipse.escet.cif.plcgen.writers.Writer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for the event transitions generator. */
@SuppressWarnings("javadoc")
public class TransitionGeneratorTest {
    private static class TestPlcTarget extends PlcBaseTarget {
        public TestPlcTarget() {
            super(PlcTargetType.IEC_61131_3);
            // Configure the target.
            String projectName = "projName";
            String configurationName = "confName";
            String resourceName = "resName";
            String plcTaskName = "taskName";
            int taskCyceTime = 1;
            int priority = 1;
            String inputPath = "input/path";
            String outputPath = "/output/path";
            PlcNumberBits intSize = PlcNumberBits.BITS_32;
            PlcNumberBits realSize = PlcNumberBits.BITS_64;
            boolean simplifyValues = false;
            ConvertEnums enumConversion = ConvertEnums.NO;
            Supplier<Boolean> shouldTerminate = () -> false;
            boolean warnOnRename = false;
            WarnOutput warnOutput = message -> { /* Do nothing. */ };

            PlcGenSettings settings = new PlcGenSettings(projectName, configurationName, resourceName, plcTaskName,
                    taskCyceTime, priority, inputPath, "/" + inputPath, outputPath, intSize, realSize, simplifyValues,
                    enumConversion, shouldTerminate, warnOnRename, warnOutput);
            setup(settings);

            // Setup the generators, part of PlcBaseTarget.generate() originally.
            nameGenerator = new DefaultNameGenerator(settings);
            codeStorage = new PlcCodeStorage(this, settings);
            typeGenerator = new DefaultTypeGenerator(this, settings);
            varStorage = new DefaultVariableStorage(this);
            cifProcessor = new CifProcessor(this, settings);
            transitionGenerator = new DefaultTransitionGenerator(this);
        }

        @Override
        public boolean supportsArrays() {
            throw new UnsupportedOperationException("Not needed for the test.");
        }

        @Override
        public boolean supportsConstants() {
            throw new UnsupportedOperationException("Not needed for the test.");
        }

        @Override
        public boolean supportsEnumerations() {
            throw new UnsupportedOperationException("Not needed for the test.");
        }

        @Override
        public String getPathSuffixReplacement() {
            throw new UnsupportedOperationException("Not needed for the test.");
        }

        @Override
        protected Writer getPlcCodeWriter() {
            throw new UnsupportedOperationException("Not needed for the test.");
        }

        @Override
        protected int getMaxIntegerTypeSize() {
            return 64;
        }

        @Override
        protected int getMaxRealTypeSize() {
            return 64;
        }
    }

    /** PLC target used in the tests. */
    private TestPlcTarget target;

    /** Generator instance under test. */
    private DefaultTransitionGenerator transitionGenerator;

    @BeforeEach
    public void setup() {
        target = new TestPlcTarget();
        transitionGenerator = new DefaultTransitionGenerator(target);
    }

    @Test
    public void testCreateTransitionGenerator() {
        transitionGenerator.setTransitions(List.of());
        transitionGenerator.generate();
        assertTrue(true);
    }
}

