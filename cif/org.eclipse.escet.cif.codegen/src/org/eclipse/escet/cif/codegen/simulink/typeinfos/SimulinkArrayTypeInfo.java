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

package org.eclipse.escet.cif.codegen.simulink.typeinfos;

import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.cif.codegen.CodeContext;
import org.eclipse.escet.cif.codegen.c89.typeinfos.C89ArrayTypeInfo;
import org.eclipse.escet.cif.codegen.typeinfos.ArrayTypeInfo;
import org.eclipse.escet.cif.codegen.typeinfos.BoolTypeInfo;
import org.eclipse.escet.cif.codegen.typeinfos.EnumTypeInfo;
import org.eclipse.escet.cif.codegen.typeinfos.IntTypeInfo;
import org.eclipse.escet.cif.codegen.typeinfos.RealTypeInfo;
import org.eclipse.escet.cif.codegen.typeinfos.TypeInfo;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.common.box.CodeBox;

/** Simulink type generator for the array type. */
public class SimulinkArrayTypeInfo extends C89ArrayTypeInfo {
    /**
     * Constructor for {@link SimulinkArrayTypeInfo} class.
     *
     * @param genLocalFunctions If set, generate functions only available in the current source file, else generate
     *     globally accessible functions.
     * @param cifType The CIF type used for creating this type information object.
     * @param childTIs Child type information (always 1 element).
     * @param length Length of the array.
     */
    public SimulinkArrayTypeInfo(boolean genLocalFunctions, CifType cifType, TypeInfo[] childTIs, int length) {
        super(genLocalFunctions, cifType, childTIs, length);
    }

    @Override
    public void generateCode(CodeContext ctxt) {
        CodeBox declCode = ctxt.makeCodeBox();
        CodeBox defCode = ctxt.makeCodeBox();

        generateC89Code(declCode, defCode, ctxt);
        generateSimulinkCode(declCode, defCode, ctxt);
        declCode.add();

        ctxt.appendReplacement("generated-types", declCode.toString());
        ctxt.appendReplacement("type-support-code", defCode.toString());
    }

    /**
     * Generate array functions for Simulink.
     *
     * @param declCode Declarations stream for the generated types and functions. Appended in-place.
     * @param defCode Implementation of the generated functions. Appended in-place.
     * @param ctxt Code generation context.
     */
    private void generateSimulinkCode(CodeBox declCode, CodeBox defCode, CodeContext ctxt) {
        String definitionPrefix, declarationPrefix;
        if (genLocalFunctions) {
            definitionPrefix = "static ";
            declarationPrefix = "static ";
        } else {
            definitionPrefix = "";
            declarationPrefix = "extern ";
        }

        String typeName = getTypeName();

        String elementCopy = getElementConversionFromSimulinkVector(childInfos[0], "vec[i]");
        if (elementCopy != null) {
            // ArrayType ArrayTypeFromSimulink(real_T *vec)
            String header = fmt("%s %sTypeFromSimulink(real_T *vec)", getTargetType(), typeName);
            declCode.add("%s%s;", declarationPrefix, header);

            defCode.add("/**");
            defCode.add(" * Construct a CIF array from a Simulink vector.");
            defCode.add(" * @param vec Simulink vector to copy.");
            defCode.add(" * @return The constructed array.");
            defCode.add(" */");
            defCode.add("%s%s {", definitionPrefix, header);
            defCode.indent();
            defCode.add("%s result;", getTargetType());
            defCode.add("int i;");
            defCode.add("for (i = 0; i < %d; i++) result.data[i] = %s;", length, elementCopy);
            defCode.add("return result;");
            defCode.dedent();
            defCode.add("}");
            defCode.add();
        }

        elementCopy = getElementConversionFromSimulinkMatrix(fmt("vec[r + c * %d]", length));
        if (elementCopy != null) {
            // ArrayType ArrayTypeFromSimulink(real_T *vec)
            String header = fmt("%s %sTypeFromSimulink(real_T *vec)", getTargetType(), typeName);
            declCode.add("%s%s;", declarationPrefix, header);

            defCode.add("/**");
            defCode.add(" * Construct a CIF array from a Simulink matrix.");
            defCode.add(" * @param vec Simulink matrix to copy.");
            defCode.add(" * @return The constructed array.");
            defCode.add(" */");
            defCode.add("%s%s {", definitionPrefix, header);
            defCode.indent();
            defCode.add("%s result;", getTargetType());
            defCode.add("int r;");
            defCode.add("for (r = 0; r < %d; r++) {", length);
            defCode.indent();
            ArrayTypeInfo inner = (ArrayTypeInfo)childInfos[0];
            defCode.add("int c;");
            defCode.add("for (c = 0; c < %d; r++) result.data[r].data[c] = %s;", inner.length, elementCopy);
            defCode.dedent();
            defCode.add("}");
            defCode.add("return result;");
            defCode.dedent();
            defCode.add("}");
            defCode.add();
        }

        elementCopy = getElementConversionToSimulinkVector(childInfos[0], "arr->data[i]");
        if (elementCopy != null) {
            // void ArrayTypeToSimulink(real_T *vec, ArrayType *src)
            String header = fmt("void %sTypeToSimulink(real_T *vec, %s *arr)", typeName, getTargetType());
            declCode.add("%s%s;", declarationPrefix, header);

            defCode.add("/**");
            defCode.add(" * Fill a Simulink vector from a CIF array.");
            defCode.add(" * @param vec Simulink vector to copy to.");
            defCode.add(" * @param arr Source array to get values from.");
            defCode.add(" */");
            defCode.add("%s%s {", definitionPrefix, header);
            defCode.indent();
            defCode.add("int i;");
            defCode.add("for (i = 0; i < %d; i++) vec[i] = %s;", length, elementCopy);
            defCode.dedent();
            defCode.add("}");
            defCode.add();
        }

        elementCopy = getElementConversionToSimulinkMatrix("mat->data[r].data[c]");
        if (elementCopy != null) {
            // void ArrayTypeToSimulink(real_T *vec, ArrayType *src)
            String header = fmt("void %sTypeToSimulink(real_T *vec, %s *mat)", typeName, getTargetType());
            declCode.add("%s%s;", declarationPrefix, header);

            defCode.add("/**");
            defCode.add(" * Fill a Simulink matrix from a CIF array.");
            defCode.add(" * @param vec Simulink matrix to copy to.");
            defCode.add(" * @param mat Source array to get values from.");
            defCode.add(" */");
            defCode.add("%s%s {", definitionPrefix, header);
            defCode.indent();
            defCode.add("int r;");
            defCode.add("for (r = 0; r < %d; r++) {", length);
            defCode.indent();
            ArrayTypeInfo inner = (ArrayTypeInfo)childInfos[0];
            defCode.add("int c;");
            defCode.add("for (c = 0; c < %d; c++) vec[r + c * %d] = %s;", inner.length, length, elementCopy);
            defCode.dedent();
            defCode.add("}");
            defCode.dedent();
            defCode.add("}");
            defCode.add();
        }
    }

    /**
     * Get the conversion for the elements of a Simulink vector to a CIF array.
     *
     * @param elmTi Type information of the element of the array.
     * @param elmText Expression in the target language providing the element value to be copied.
     * @return Expression in the target language providing the resulting CIF value, or {@code null} if the vector cannot
     *     be copied.
     */
    public static String getElementConversionFromSimulinkVector(TypeInfo elmTi, String elmText) {
        if (elmTi instanceof BoolTypeInfo) {
            return fmt("SimulinkToBool(%s)", elmText);
        } else if (elmTi instanceof IntTypeInfo) {
            return fmt("SimulinkToInt(%s)", elmText);
        } else if (elmTi instanceof EnumTypeInfo) {
            return fmt("(%s)SimulinkToEnum(%s)", elmTi.getTargetType(), elmText);
        } else if (elmTi instanceof RealTypeInfo) {
            return fmt("SimulinkToReal(%s)", elmText);
        }
        return null;
    }

    /**
     * Get the conversion for the elements of a Simulink matrix to this CIF array.
     *
     * @param elmText Expression in the target language providing the element value to be copied.
     * @return Expression in the target language providing the resulting CIF value, or {@code null} if the matrix cannot
     *     be copied.
     */
    private String getElementConversionFromSimulinkMatrix(String elmText) {
        if (childInfos[0] instanceof ArrayTypeInfo) {
            ArrayTypeInfo outer = (ArrayTypeInfo)childInfos[0];
            return getElementConversionFromSimulinkVector(outer.childInfos[0], elmText);
        }
        return null;
    }

    /**
     * Get the conversion for the elements of a CIF array to a Simulink vector.
     *
     * @param elmTi Type information of the element of the array.
     * @param elmText Expression in the target language providing the element value to be copied.
     * @return Expression in the target language providing the resulting Simulink value, or {@code null} if the array
     *     cannot be copied.
     */
    public static String getElementConversionToSimulinkVector(TypeInfo elmTi, String elmText) {
        if (elmTi instanceof BoolTypeInfo) {
            return fmt("BoolToSimulink(%s)", elmText);
        } else if (elmTi instanceof IntTypeInfo) {
            return fmt("IntToSimulink(%s)", elmText);
        } else if (elmTi instanceof EnumTypeInfo) {
            return fmt("IntToSimulink(%s)", elmText);
        } else if (elmTi instanceof RealTypeInfo) {
            return fmt("RealToSimulink(%s)", elmText);
        }
        return null;
    }

    /**
     * Get the conversion for the elements of this CIF array to a Simulink matrix.
     *
     * @param elmText Expression in the target language providing the element value to be copied.
     * @return Expression in the target language providing the resulting Simulink value, or {@code null} if the array
     *     cannot be copied.
     */
    private String getElementConversionToSimulinkMatrix(String elmText) {
        if (childInfos[0] instanceof ArrayTypeInfo) {
            ArrayTypeInfo outer = (ArrayTypeInfo)childInfos[0];
            return getElementConversionToSimulinkVector(outer.childInfos[0], elmText);
        }
        return null;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof SimulinkArrayTypeInfo)) {
            return false;
        }
        SimulinkArrayTypeInfo otherArray = (SimulinkArrayTypeInfo)other;
        if (!childInfos[0].equals(otherArray.childInfos[0])) {
            return false;
        }
        return length == otherArray.length;
    }

    @Override
    public int hashCode() {
        int h = SimulinkArrayTypeInfo.class.hashCode();
        h = h + childInfos[0].hashCode();
        return h + length;
    }
}
