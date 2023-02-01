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

import static org.eclipse.escet.common.java.Maps.map;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.eclipse.escet.cif.cif2plc.options.ConvertEnums;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcArrayType;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcDerivedType;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcElementaryType;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcEnumType;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcStructType;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcType;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcTypeDecl;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcValue;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcVariable;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.TypeEqHashWrap;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumLiteral;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.EnumType;
import org.eclipse.escet.cif.metamodel.cif.types.Field;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.cif.metamodel.cif.types.ListType;
import org.eclipse.escet.cif.metamodel.cif.types.RealType;
import org.eclipse.escet.cif.metamodel.cif.types.TupleType;
import org.eclipse.escet.cif.metamodel.cif.types.TypeRef;
import org.eclipse.escet.cif.plcgen.PlcGenSettings;
import org.eclipse.escet.cif.plcgen.targets.PlcTarget;
import org.eclipse.escet.common.java.Assert;

/** Class for handling types. */
public class TypeGenerator {
    /** Standard integer type. */
    private final PlcElementaryType standardIntType;

    /** Standard floating point type. */
    private final PlcElementaryType standardFloatType;

    /** How to convert enumeration declarations to the PLC. */
    private final ConvertEnums enumConversion;

    /** Next value to use for an enumeration literal in case of conversion to numbers or constants. */
    private int nextEnumLiteralValue = 1;

    /** Generator for obtaining clash-free names in the generated code. */
    private final NameSanitizer nameSanitizer;

    /** Generator that stores and writes generated PLC code. */
    private final PlcCodeStorage plcCodeStorage;

    /** Mapping from CIF tuple types wrapped in {@link TypeEqHashWrap} instances, to Structured Text structure names. */
    private final Map<TypeEqHashWrap, String> structNames = map();

    /**
     * Mapping from CIF enumerations to Structured Text enumeration type and value information.
     *
     * <p>
     * The map combines compatible enumerations (as defined by {@link CifTypeUtils#areEnumsCompatible}) to one
     * information instance in order to reduce the number of cloned enumerations.
     * </p>
     */
    private final Map<EnumDecl, EnumDeclData> enumDeclNames = new TreeMap<>(new Comparator<>() {
        @Override
        public int compare(EnumDecl arg0, EnumDecl arg1) {
            int size0 = arg0.getLiterals().size();
            int size1 = arg1.getLiterals().size();
            if (size0 != size1) {
                return (size0 < size1) ? -1 : 1;
            } else {
                int order = 0;
                for (int i = 0; order == 0 && i < size0; i++) {
                    order = arg0.getLiterals().get(i).getName().compareTo(arg1.getLiterals().get(i).getName());
                }
                return order;
            }
        }
    });

    /**
     * Constructor of the {@link TypeGenerator} class.
     *
     * @param target PLC target.
     * @param settings Configuration to use.
     * @param nameSanitizer Generator for obtaining clash-free names in the generated code.
     * @param plcCodeStorage Generator that stores and writes generated PLC code.
     */
    public TypeGenerator(PlcTarget target, PlcGenSettings settings, NameSanitizer nameSanitizer,
            PlcCodeStorage plcCodeStorage)
    {
        standardIntType = target.getIntegerType();
        standardFloatType = target.getFloatType();
        enumConversion = settings.enumConversion;

        this.nameSanitizer = nameSanitizer;
        this.plcCodeStorage = plcCodeStorage;
    }

    /**
     * Convert a CIF type to a PLC type.
     *
     * @param type CIF type to convert.
     * @return The associated PLC type.
     */
    public PlcType convertType(CifType type) {
        if (type instanceof BoolType) {
            return PlcElementaryType.BOOL_TYPE;
        } else if (type instanceof IntType) {
            return standardIntType;
        } else if (type instanceof RealType) {
            return standardFloatType;
        } else if (type instanceof TypeRef typeRef) {
            return convertType(typeRef.getType().getType());
        } else if (type instanceof TupleType tupleType) {
            return convertTupleType(tupleType);
        } else if (type instanceof EnumType enumType) {
            return convertEnumDecl(enumType.getEnum());
        } else if (type instanceof ListType arrayType) {
            int size = arrayType.getLower();
            Assert.check(CifTypeUtils.isArrayType(arrayType));
            Assert.check(!(arrayType.getElementType() instanceof ListType)); // TODO Multi-dimensional or nested arrays.
            PlcType elemType = convertType(arrayType.getElementType());
            return new PlcArrayType(0, (size == 0) ? 0 : size - 1, elemType);
        } else {
            throw new RuntimeException("Unexpected type: " + type);
        }
    }

    /**
     * Converts a CIF tuple type to a PLC structure.
     *
     * @param tupleType The CIF tuple type.
     * @return The PLC structure type generated for the tuple type.
     */
    private PlcType convertTupleType(TupleType tupleType) {
        TypeEqHashWrap typeWrap = new TypeEqHashWrap(tupleType, true, false);
        String sname = structNames.get(typeWrap);
        if (sname == null) {
            // Generate PLC struct for tuple.
            sname = nameSanitizer.sanitizeName("TupleStruct", false);
            structNames.put(typeWrap, sname);

            PlcStructType structType = new PlcStructType();
            int fieldNumber = 1;
            for (Field field: tupleType.getFields()) {
                String fieldName = "field" + String.valueOf(fieldNumber);
                PlcType ftype = convertType(field.getType());
                structType.fields.add(new PlcVariable(fieldName, ftype));
                fieldNumber++;
            }

            PlcTypeDecl typeDecl = new PlcTypeDecl(sname, structType);
            plcCodeStorage.addTypeDecl(typeDecl);
        }
        return new PlcDerivedType(sname);
    }

    /**
     * Convert a CIF enumeration declaration to a named PLC enumeration.
     *
     * @param enumDecl Enumeration declaration to convert.
     * @return The PLC type generated for the enumeration.
     */
    public PlcType convertEnumDecl(EnumDecl enumDecl) {
        EnumDeclData declData = enumDeclNames.get(enumDecl);
        if (declData == null) {
            declData = makeEnumDeclData(enumDecl);
            enumDeclNames.put(enumDecl, declData);
        }
        return declData.enumDeclType;
    }

    /** Enumeration declaration type and value information storage. */
    private static class EnumDeclData {
        /** The enumeration data type in the PLC. */
        public final PlcType enumDeclType;

        /** Values of the converted enumeration literals. */
        private final PlcValue[] values;

        /**
         * Constructor of the {@link EnumDeclData} class.
         *
         * @param enumDeclType Type of enumeration values in the PLC.
         * @param values Values of the enumeration declaration.
         */
        public EnumDeclData(PlcType enumDeclType, PlcValue[] values) {
            this.enumDeclType = enumDeclType;
            this.values = values;
        }

        /**
         * Get the equivalent value of the provided enumeration literal in the PLC.
         *
         * @param literal Enumeration literal to translate. Must be a literal of a compatible enumeration.
         * @return The translated value.
         */
        @SuppressWarnings("unused") // TODO Use the function in expression conversions.
        public PlcValue getLiteral(EnumLiteral literal) {
            // Use the enumeration containing the literal itself for getting the index.
            EnumDecl enumDecl = (EnumDecl)literal.eContainer();
            return values[enumDecl.getLiterals().indexOf(literal)];
        }
    }

    /**
     * Create the equivalent enumeration type and values to use in the PLC for the given declaration.
     *
     * @param enumDecl Enumeration declaration to convert.
     * @return The created equivalent PLC type and value information.
     */
    public EnumDeclData makeEnumDeclData(EnumDecl enumDecl) {
        List<EnumLiteral> cifLiterals = enumDecl.getLiterals();

        if (enumConversion.equals(ConvertEnums.NO)) {
            // Convert the declaration to an enumeration in the PLC.

            // Convert the enumeration literals.
            PlcValue[] literals = new PlcValue[cifLiterals.size()];
            int litIndex = 0;
            for (EnumLiteral lit: cifLiterals) {
                String litName = nameSanitizer.sanitizeName(CifTextUtils.getAbsName(lit, false), true);
                literals[litIndex] = new PlcValue(litName);

                litIndex++;
            }

            // Construct the type and add it to the global type declarations.1
            String declName = nameSanitizer.sanitizeName(CifTextUtils.getAbsName(enumDecl, false), true);
            PlcType declType = new PlcDerivedType(declName);
            PlcEnumType plcEnumType = new PlcEnumType(
                    Arrays.stream(literals).map(v -> v.value).collect(Collectors.toList()));
            plcCodeStorage.addTypeDecl(new PlcTypeDecl(declName, plcEnumType));

            return new EnumDeclData(declType, literals);
        } else if (enumConversion.equals(ConvertEnums.CONSTS)) {
            // Convert the declaration to constants with unique integer values.
            PlcType declType = standardIntType;
            PlcValue[] literals = new PlcValue[cifLiterals.size()];

            int litIndex = 0;
            for (EnumLiteral lit: cifLiterals) {
                String litConstName = nameSanitizer.sanitizeName(CifTextUtils.getAbsName(lit, false), true);
                // TODO Have grouping in the constant values of a declaration (eg always start at multiple of 10).
                PlcValue litVal = new PlcValue(String.valueOf(nextEnumLiteralValue));
                plcCodeStorage.addConstant(new PlcVariable(litConstName, declType, null, litVal));
                literals[litIndex] = litVal;

                nextEnumLiteralValue++;
                litIndex++;
            }
            return new EnumDeclData(declType, literals);
        } else if (enumConversion.equals(ConvertEnums.INTS)) {
            // Convert the declaration to fixed unique integer values.
            PlcType declType = standardIntType;
            PlcValue[] literals = new PlcValue[cifLiterals.size()];

            for (int litIndex = 0; litIndex < cifLiterals.size(); litIndex++) {
                // TODO Have grouping in the constant values of a declaration (eg always start at multiple of 10).
                literals[litIndex] = new PlcValue(String.valueOf(nextEnumLiteralValue));

                nextEnumLiteralValue++;
            }
            return new EnumDeclData(declType, literals);
        } else {
            throw new AssertionError("Unknown enum literal conversion found: \"" + enumConversion + "\".");
        }
    }
}
