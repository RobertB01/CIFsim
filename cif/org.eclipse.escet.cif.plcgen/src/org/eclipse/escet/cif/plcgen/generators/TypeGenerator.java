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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.escet.cif.cif2plc.options.ConvertEnums;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcArrayType;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcDerivedType;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcElementaryType;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcEnumType;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcStructType;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcType;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcTypeDecl;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcVariable;
import org.eclipse.escet.cif.common.CifEnumUtils.EnumDeclEqHashWrap;
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
import org.eclipse.escet.cif.plcgen.model.expressions.PlcEnumLiteral;
import org.eclipse.escet.cif.plcgen.targets.PlcTargetInterface;
import org.eclipse.escet.common.java.Assert;

/** Class for handling types. */
public class TypeGenerator implements TypeGeneratorInterface {
    /** Standard integer type. */
    private final PlcElementaryType standardIntType;

    /** Standard real type. */
    private final PlcElementaryType standardRealType;

    /** How to convert enumeration declarations to the PLC. */
    private final ConvertEnums enumConversion;

    /** Generator for obtaining clash-free names in the generated code. */
    private final NameGeneratorInterface nameGenerator;

    /** Generator that stores and writes generated PLC code. */
    private final PlcCodeStorage plcCodeStorage;

    /** Mapping from CIF tuple types wrapped in {@link TypeEqHashWrap} instances, to PLC type-declaration names. */
    private final Map<TypeEqHashWrap, String> structNames = map();

    /** Mapping from type declaration names to their underlying structure type. */
    private final Map<String, PlcStructType> structTypes = map();

    /**
     * Mapping from CIF enumerations to PLC enumeration type and value information.
     *
     * <p>
     * The map combines compatible enumerations (as defined by {@link CifTypeUtils#areEnumsCompatible}) to one
     * information instance.
     * </p>
     */
    private final Map<EnumDeclEqHashWrap, EnumDeclData> enumDeclNames = map();

    /**
     * Constructor of the {@link TypeGenerator} class.
     *
     * @param target PLC target.
     * @param settings Configuration to use.
     * @param nameGenerator Generator for obtaining clash-free names in the generated code.
     * @param plcCodeStorage Generator that stores and writes generated PLC code.
     */
    public TypeGenerator(PlcTargetInterface target, PlcGenSettings settings, NameGeneratorInterface nameGenerator,
            PlcCodeStorage plcCodeStorage)
    {
        standardIntType = target.getIntegerType();
        standardRealType = target.getRealType();
        enumConversion = settings.enumConversion;

        this.nameGenerator = nameGenerator;
        this.plcCodeStorage = plcCodeStorage;
    }

    @Override
    public PlcType convertType(CifType type) {
        if (type instanceof BoolType) {
            return PlcElementaryType.BOOL_TYPE;
        } else if (type instanceof IntType) {
            return standardIntType;
        } else if (type instanceof RealType) {
            return standardRealType;
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
            // TODO Improve the relation from the PLC code back to the CIF specification by using supplied field names.
            PlcStructType structType = new PlcStructType();
            int fieldNumber = 1;
            for (Field field: tupleType.getFields()) {
                String fieldName = "field" + String.valueOf(fieldNumber);
                PlcType ftype = convertType(field.getType());
                structType.fields.add(new PlcVariable(fieldName, ftype));
                fieldNumber++;
            }

            // Wrap a type declaration around the struct type, make it findable for future queries, and store the
            // created PLC structure type.
            sname = nameGenerator.generateGlobalName("TupleStruct", false);
            PlcTypeDecl typeDecl = new PlcTypeDecl(sname, structType);
            structNames.put(typeWrap, sname);
            structTypes.put(sname, structType);
            plcCodeStorage.addTypeDecl(typeDecl);
        }
        return new PlcDerivedType(sname);
    }

    @Override
    public PlcStructType getStructureType(PlcType type) {
        Assert.check(type instanceof PlcDerivedType);
        PlcStructType structType = structTypes.get(((PlcDerivedType)type).name);
        Assert.notNull(structType);
        return structType;
    }

    @Override
    public PlcType convertEnumDecl(EnumDecl enumDecl) {
        return ensureEnumDecl(enumDecl).enumDeclType;
    }

    /**
     * Ensure that the given CIF enumeration declaration is or becomes available as a named PLC enumeration.
     *
     * @param enumDecl Enumeration declaration to check.
     * @return The PLC equivalent of the given CIF declaration.
     */
    private EnumDeclData ensureEnumDecl(EnumDecl enumDecl) {
        EnumDeclEqHashWrap wrappedEnumDecl = new EnumDeclEqHashWrap(enumDecl);
        EnumDeclData declData = enumDeclNames.get(wrappedEnumDecl);
        if (declData == null) {
            declData = makeEnumDeclData(enumDecl);
            enumDeclNames.put(wrappedEnumDecl, declData);
        }
        return declData;
    }

    @Override
    public PlcEnumLiteral getPlcEnumLiteral(EnumLiteral enumLit) {
        EnumDecl enumDecl = (EnumDecl)enumLit.eContainer();
        return ensureEnumDecl(enumDecl).getLiteral(enumLit);
    }

    /** Enumeration declaration type and value information storage. */
    private static class EnumDeclData {
        /** The enumeration data type in the PLC. */
        public final PlcType enumDeclType;

        /** Values of the converted enumeration literals. */
        private final PlcEnumLiteral[] values;

        /**
         * Constructor of the {@link EnumDeclData} class.
         *
         * @param enumDeclType The enumeration data type in the PLC.
         * @param values Values of the converted enumeration literals.
         */
        public EnumDeclData(PlcType enumDeclType, PlcEnumLiteral[] values) {
            this.enumDeclType = enumDeclType;
            this.values = values;
        }

        /**
         * Get the equivalent value of the provided enumeration literal in the PLC.
         *
         * @param literal Enumeration literal to translate. Must be a literal of a compatible enumeration.
         * @return The translated value.
         */
        public PlcEnumLiteral getLiteral(EnumLiteral literal) {
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
        Assert.check(enumConversion.equals(ConvertEnums.NO)); // Other conversions have been eliminated already.

        // Convert the enumeration literals.
        List<EnumLiteral> cifLiterals = enumDecl.getLiterals();
        PlcEnumLiteral[] literals = new PlcEnumLiteral[cifLiterals.size()];
        int litIndex = 0;
        for (EnumLiteral lit: cifLiterals) {
            String litName = nameGenerator.generateGlobalName(CifTextUtils.getAbsName(lit, false), true);
            literals[litIndex] = new PlcEnumLiteral(litName);

            litIndex++;
        }

        // Construct the type and add it to the global type declarations.
        String declName = nameGenerator.generateGlobalName(CifTextUtils.getAbsName(enumDecl, false), true);
        PlcType declType = new PlcDerivedType(declName);
        PlcEnumType plcEnumType = new PlcEnumType(
                Arrays.stream(literals).map(v -> v.value).collect(Collectors.toList()));
        plcCodeStorage.addTypeDecl(new PlcTypeDecl(declName, plcEnumType));

        return new EnumDeclData(declType, literals);
    }
}
