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

import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBoolType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newDistType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newField;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newIntType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newRealType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newTupleType;

import org.eclipse.escet.cif.metamodel.cif.types.DistType;
import org.eclipse.escet.cif.metamodel.cif.types.Field;
import org.eclipse.escet.cif.metamodel.cif.types.TupleType;
import org.eclipse.escet.common.box.CodeBox;

/** Sampler code generator. */
public class SamplerCodeGenerator {
    /** Constructor for the {@link SamplerCodeGenerator} class. */
    private SamplerCodeGenerator() {
        // Static class.
    }

    /**
     * Generate Java code for the sampler, which is used to sample stochastic distributions.
     *
     * <p>
     * All code that can use sampling should have already been generated before generating the sampler. If that code did
     * not use sampling, no sampler code is generated.
     * </p>
     *
     * @param ctxt The compiler context to use.
     */
    public static void gencodeSampler(CifCompilerContext ctxt) {
        // Only generate the class if we need it.
        if (!ctxt.needSampler) {
            return;
        }

        // Add new code file.
        JavaCodeFile file = ctxt.addCodeFile("Sampler");

        // Add header.
        CodeBox h = file.header;
        h.add("/** Sampler for sampling stochastic distributions. */");
        h.add("public final class Sampler {");

        // Add body.
        CodeBox c = file.body;

        // Construct tuple return types for the 'sample' methods.
        Field bfield1 = newField();
        Field bfield2 = newField();
        DistType bdtype = newDistType();
        bdtype.setSampleType(newBoolType());
        bfield1.setType(newBoolType());
        bfield2.setType(bdtype);
        TupleType bttype = newTupleType();
        bttype.getFields().add(bfield1);
        bttype.getFields().add(bfield2);

        Field ifield1 = newField();
        Field ifield2 = newField();
        DistType idtype = newDistType();
        idtype.setSampleType(newIntType());
        ifield1.setType(newIntType());
        ifield2.setType(idtype);
        TupleType ittype = newTupleType();
        ittype.getFields().add(ifield1);
        ittype.getFields().add(ifield2);

        Field rfield1 = newField();
        Field rfield2 = newField();
        DistType rdtype = newDistType();
        rdtype.setSampleType(newRealType());
        rfield1.setType(newRealType());
        rfield2.setType(rdtype);
        TupleType rttype = newTupleType();
        rttype.getFields().add(rfield1);
        rttype.getFields().add(rfield2);

        // Get the class names for the tuple return types. Also generates the
        // classes, if not haven't been generated yet.
        String brclass = ctxt.getTupleTypeClassName(bttype);
        String irclass = ctxt.getTupleTypeClassName(ittype);
        String rrclass = ctxt.getTupleTypeClassName(rttype);

        // Add 'sample' method (for boolean distributions).
        c.add("public static %s sample(BooleanDistribution d) {", brclass);
        c.indent();
        c.add("d = d.copy();");
        c.add("boolean value = d.sample();");
        c.add("return new %s(value, d);", brclass);
        c.dedent();
        c.add("}");

        // Add 'sample' method (for integer distributions).
        c.add();
        c.add("public static %s sample(IntegerDistribution d) {", irclass);
        c.indent();
        c.add("d = d.copy();");
        c.add("int value = d.sample();");
        c.add("return new %s(value, d);", irclass);
        c.dedent();
        c.add("}");

        // Add 'sample' method (for real distributions).
        c.add();
        c.add("public static %s sample(RealDistribution d) {", rrclass);
        c.indent();
        c.add("d = d.copy();");
        c.add("double value = d.sample();");
        c.add("if (Double.isNaN(value)) {");
        c.indent();
        c.add("String msg = fmt(\"Invalid operation: sample %s: result is NaN.\", d);");
        c.add("throw new CifSimulatorException(msg);");
        c.dedent();
        c.add("}");
        c.add("if (Double.isInfinite(value)) {");
        c.indent();
        c.add("String valueTxt;");
        c.add("if (value == Double.POSITIVE_INFINITY) {");
        c.indent();
        c.add("valueTxt = \"+inf\";");
        c.dedent();
        c.add("} else {");
        c.indent();
        c.add("Assert.check(value == Double.NEGATIVE_INFINITY);");
        c.add("valueTxt = \"-inf\";");
        c.dedent();
        c.add("}");
        c.add("String msg = fmt(\"Invalid operation: sample %s: result is %s.\", d, valueTxt);");
        c.add("throw new CifSimulatorException(msg);");
        c.dedent();
        c.add("}");
        c.add("return new %s(value, d);", rrclass);
        c.dedent();
        c.add("}");
    }
}
