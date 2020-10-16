//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.chi.codegen;

import org.eclipse.escet.chi.runtime.ArithmeticFunctions;
import org.eclipse.escet.chi.runtime.ChiCoordinator;
import org.eclipse.escet.chi.runtime.ChiSimulatorException;
import org.eclipse.escet.chi.runtime.ChiSpecification;
import org.eclipse.escet.chi.runtime.IoFunctions;
import org.eclipse.escet.chi.runtime.SelectChoice;
import org.eclipse.escet.chi.runtime.SelectChoice.ChannelKind;
import org.eclipse.escet.chi.runtime.SelectDelay;
import org.eclipse.escet.chi.runtime.SelectWaitRunChilds;
import org.eclipse.escet.chi.runtime.data.BaseProcess;
import org.eclipse.escet.chi.runtime.data.Channel;
import org.eclipse.escet.chi.runtime.data.ChiMatrix;
import org.eclipse.escet.chi.runtime.data.CoreProcess;
import org.eclipse.escet.chi.runtime.data.DataSupport;
import org.eclipse.escet.chi.runtime.data.DefinitionKind;
import org.eclipse.escet.chi.runtime.data.DummyProcess;
import org.eclipse.escet.chi.runtime.data.IndexableDeque;
import org.eclipse.escet.chi.runtime.data.PositionData;
import org.eclipse.escet.chi.runtime.data.Timer;
import org.eclipse.escet.chi.runtime.data.io.ChiFileHandle;
import org.eclipse.escet.chi.runtime.data.io.ChiReadMemoryFile;
import org.eclipse.escet.chi.runtime.data.io.ChiWriteMemoryFile;
import org.eclipse.escet.chi.runtime.data.io.DummyChiFileHandle;
import org.eclipse.escet.chi.runtime.data.random.BooleanDistribution;
import org.eclipse.escet.chi.runtime.data.random.DoubleDistribution;
import org.eclipse.escet.chi.runtime.data.random.IntegerDistribution;
import org.eclipse.escet.common.java.RangeIterator;

/** Constant definitions referring to names in the runtime bundle. */
public class Constants {
    /** Private constructor to prevent instantiation. */
    private Constants() {
        // Intentionally empty.
    }

    /** Amount of indentation in the code (in number of spaces). */
    public static final int INDENT_SIZE = 4;

    /** Name of the Chi runtime package. */
    private static final String PKG = "org.eclipse.escet.chi.runtime";

    /** Name of the runtime data package. */
    public static final String DATA_PKG = PKG + ".data";

    /** Name of the runtime (file) io package. */
    private static final String IO_PKG = PKG + ".data.io";

    /** Name of the runtime stochastic package. */
    public static final String RANDOM_PKG = PKG + ".data.random";

    // ------------------------------------------------------------

    /** Class name of the simulator application. */
    public static final String SIMULATOR_APPLICATION_CLASSNAME = "SimulatorApplication";

    /**
     * Fully qualified class name of the simulator application. (in org.eclipse.escet.chi.simulator, no link to prevent
     * a cycle in the plug-in dependencies).
     */
    public static final String SIMULATOR_APPLICATION_FQC = "org.eclipse.escet.chi.simulator."
            + SIMULATOR_APPLICATION_CLASSNAME;

    // ------------------------------------------------------------

    /** Fully qualified name of the Chi specification base class {@link ChiSpecification}. */
    public static final String CHI_SPEC_FQC = PKG + ".ChiSpecification";

    /** Fully qualified class name of the runtime {@link ChiCoordinator} class. */
    public static final String COORDINATOR_FQC = PKG + ".ChiCoordinator";

    /** Fully qualified class name of the runtime 'select' choice class {@link SelectChoice}. */
    public static final String SELECT_CHOICE_FQC = PKG + ".SelectChoice";

    /** Fully qualified class name of the runtime {@link ChannelKind} enum. */
    public static final String CHANNELKIND_FQC = SELECT_CHOICE_FQC + ".ChannelKind";

    /** Fully qualified class name of the runtime 'delay' class {@link SelectDelay}. */
    public static final String SELECT_DELAY_FQC = PKG + ".SelectDelay";

    /** Fully qualified class name of the runtime 'wait for child process' class {@link SelectWaitRunChilds}. */
    public static final String SELECT_WAIT_RUN_CHILDS_FQC = PKG + ".SelectWaitRunChilds";

    /** Fully qualified class name of the base Chi file handle class {@link ChiFileHandle}. */
    public static final String CHI_FILE_HANDLE_FQC = IO_PKG + ".ChiFileHandle";

    /** Fully qualified class name of the dummy Chi file handle class {@link DummyChiFileHandle}. */
    public static final String DUMMY_FILE_HANDLE_FQC = IO_PKG + ".DummyChiFileHandle";

    /** Fully qualified class name of the runtime memory file reader class {@link ChiReadMemoryFile}. */
    public static final String CHI_READ_MEMORY_FILE_FQC = IO_PKG + ".ChiReadMemoryFile";

    /** Fully qualified class name of the runtime memory file writer class {@link ChiWriteMemoryFile}. */
    public static final String CHI_WRITE_MEMORY_FILE_FQC = IO_PKG + ".ChiWriteMemoryFile";

    /** Fully qualified class name of the runtime exception reporting class {@link ChiSimulatorException}. */
    public static final String CHI_SIMULATOR_EXCEPTION_FQC = PKG + ".ChiSimulatorException";

    // ------------------------------------------------------------
    // runtime.data package

    /** Fully qualified class name of the runtime {@link BaseProcess} class. */
    public static final String BASEPROCESS_FQC = DATA_PKG + ".BaseProcess";

    /** Fully qualified class name of the runtime {@link DefinitionKind} class. */
    public static final String DEFINITION_KIND_FQC = DATA_PKG + ".DefinitionKind";

    /** Fully qualified class name of the runtime {@link CoreProcess} class. */
    public static final String COREPROCESS_FQC = DATA_PKG + ".CoreProcess";

    /** Fully qualified class name of the runtime {@link DummyProcess} class. */
    public static final String DUMMY_PROCESS_FQC = DATA_PKG + ".DummyProcess";

    /** Fully qualified class name of the runtime {@link Channel} class. */
    public static final String CHANNEL_FQC = DATA_PKG + ".Channel";

    /** Fully qualified class name of the runtime {@link Timer} class. */
    public static final String TIMER_FQC = DATA_PKG + ".Timer";

    /** Fully qualified class name of the runtime list {@link IndexableDeque} class. */
    public static final String INDEXABLE_DEQUE_FQC = DATA_PKG + ".IndexableDeque";

    /** Fully qualified class name of the runtime matrix {@link ChiMatrix} class. */
    public static final String MATRIX_FQC = DATA_PKG + ".ChiMatrix";

    /** Fully qualified class name of the runtime data support {@link DataSupport} class. */
    public static final String DATA_SUPPORT_FQC = DATA_PKG + ".DataSupport";

    /** Fully qualified class name of the runtime position data class {@link PositionData}. */
    public static final String POSITION_DATA_FQC = DATA_PKG + ".PositionData";

    /** Fully qualified class name of the runtime range iterator {@link RangeIterator} class. */
    public static final String RANGE_ITERATOR_FQC = "org.eclipse.escet.common.java.RangeIterator";

    // ------------------------------------------------------------
    // IoFunctions.

    /** Fully qualified name of the 'read a boolean literal' function {@link IoFunctions#writeBool}. */
    public static final String WRITE_BOOL_FQM = PKG + ".IoFunctions.writeBool";

    /** Fully qualified name of the 'read a boolean literal' function {@link IoFunctions#readBool}. */
    public static final String READ_BOOL_FQM = PKG + ".IoFunctions.readBool";

    /** Fully qualified name of the 'write an integer number' function {@link IoFunctions#writeInt}. */
    public static final String WRITE_INT_FQM = PKG + ".IoFunctions.writeInt";

    /** Fully qualified name of the 'read an integer number' function {@link IoFunctions#readInt}. */
    public static final String READ_INT_FQM = PKG + ".IoFunctions.readInt";

    /** Fully qualified name of the 'read a real number' function {@link IoFunctions#readReal}. */
    public static final String READ_REAL_FQM = PKG + ".IoFunctions.readReal";

    /** Fully qualified name of the 'write a real number' function {@link IoFunctions#writeReal}. */
    public static final String WRITE_REAL_FQM = PKG + ".IoFunctions.writeReal";

    /** Fully qualified name of the 'read a string' function {@link IoFunctions#readString}. */
    public static final String READ_STRING_FQM = PKG + ".IoFunctions.readString";

    /** Fully qualified name of the 'write a string' function {@link IoFunctions#writeString}. */
    public static final String WRITE_STRING_FQM = PKG + ".IoFunctions.writeString";

    // ------------------------------------------------------------
    // ArithmeticFunctions.

    /** Fully qualified name of the power function with integer result {@link ArithmeticFunctions#intPower}. */
    public static final String INT_POWER_FQM = PKG + ".ArithmeticFunctions.intPower";

    /** Fully qualified name of the 'div' function {@link ArithmeticFunctions#floorDivision}. */
    public static final String DIV_FQM = PKG + ".ArithmeticFunctions.floorDivision";

    /** Fully qualified name of the 'mod' function {@link ArithmeticFunctions#modulus}. */
    public static final String MOD_FQM = PKG + ".ArithmeticFunctions.modulus";

    // ------------------------------------------------------------
    // Random

    /** Fully qualified class name of the boolean distribution class {@link BooleanDistribution}. */
    public static final String BOOL_DISTRIBUTION_CLASSNAME = "BooleanDistribution";

    /** Fully qualified class name of the integer distribution class {@link IntegerDistribution}. */
    public static final String INT_DISTRIBUTION_CLASSNAME = "IntegerDistribution";

    /** Fully qualified class name of the double distribution class {@link DoubleDistribution}. */
    public static final String DOUBLE_DISTRIBUTION_CLASSNAME = "DoubleDistribution";

    /** Fully qualified class name of the boolean distribution class {@link BooleanDistribution}. */
    public static final String CONSTANT_BOOL_DISTRIBUTION_CLASSNAME = "BooleanConstantDistribution";

    /** Fully qualified class name of the integer distribution class {@link IntegerDistribution}. */
    public static final String CONSTANT_INT_DISTRIBUTION_CLASSNAME = "IntegerConstantDistribution";

    /** Fully qualified class name of the double distribution class {@link DoubleDistribution}. */
    public static final String CONSTANT_DOUBLE_DISTRIBUTION_CLASSNAME = "DoubleConstantDistribution";

    /**
     * Get the class-name part of a fully qualified class name.
     *
     * @param fqName The fully qualified class name.
     * @return The last part of the fully qualified class name.
     */
    public static String getClassname(String fqName) {
        int idx = fqName.lastIndexOf('.');
        if (idx == -1) {
            return fqName;
        }
        return fqName.substring(idx + 1);
    }
}
