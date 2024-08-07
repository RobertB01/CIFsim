//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.map;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.TypeEqHashWrap;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumLiteral;
import org.eclipse.escet.cif.metamodel.cif.declarations.TypeDecl;
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
import org.eclipse.escet.cif.plcgen.model.declarations.PlcDataVariable;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcExpression;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcIntLiteral;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcVarExpression;
import org.eclipse.escet.cif.plcgen.model.types.PlcArrayType;
import org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType;
import org.eclipse.escet.cif.plcgen.model.types.PlcEnumType;
import org.eclipse.escet.cif.plcgen.model.types.PlcStructField;
import org.eclipse.escet.cif.plcgen.model.types.PlcStructType;
import org.eclipse.escet.cif.plcgen.model.types.PlcType;
import org.eclipse.escet.cif.plcgen.options.ConvertEnums;
import org.eclipse.escet.cif.plcgen.targets.PlcTarget;
import org.eclipse.escet.common.java.Assert;

/** Class for handling types. */
public class DefaultTypeGenerator implements TypeGenerator {
    /** PLC target to generate code for. */
    private final PlcTarget target;

    /** Standard integer type. */
    private final PlcElementaryType standardIntType;

    /** Standard real type. */
    private final PlcElementaryType standardRealType;

    /** How to convert enumerations. */
    private final ConvertEnums selectedEnumConversion;

    /** Mapping from CIF tuple types wrapped in {@link TypeEqHashWrap} instances to their structure type. */
    private final Map<TypeEqHashWrap, PlcStructType> structTypes = map();

    /**
     * Mapping from CIF enumerations to their PLC declaration data.
     *
     * <p>
     * The map combines compatible enumerations (as defined by {@link CifTypeUtils#areEnumsCompatible}) to one PLC
     * enumeration type.
     * </p>
     *
     * <p>
     * Recommend access is through {@link #getOrCreateConvertedEnumDecl}.
     * </p>
     */
    private final Map<EnumDecl, EnumDeclData> enumTypes = map();

    /**
     * Constructor of the {@link DefaultTypeGenerator} class.
     *
     * @param target PLC target.
     * @param settings Configuration to use.
     */
    public DefaultTypeGenerator(PlcTarget target, PlcGenSettings settings) {
        this.target = target;
        standardIntType = target.getStdIntegerType();
        standardRealType = target.getStdRealType();
        selectedEnumConversion = target.getActualEnumerationsConversion();
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
            EnumDeclData declData = getOrCreateConvertedEnumDecl(enumType.getEnum());
            return declData.plcEnumType;
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

    @Override
    public PlcStructType convertTupleType(TupleType tupleType) {
        TypeEqHashWrap typeWrap = new TypeEqHashWrap(tupleType, true, false);
        PlcStructType structType = structTypes.get(typeWrap);
        if (structType == null) {
            structType = makePlcStructType(tupleType);
            structTypes.put(typeWrap, structType);
        }
        return structType;
    }

    /**
     * Make a new structure type.
     *
     * @param tupleType Tuple type to convert.
     * @return The created structure type.
     */
    private PlcStructType makePlcStructType(TupleType tupleType) {
        // Convert the fields.
        List<Field> tupleFields = tupleType.getFields();
        List<PlcStructField> structFields = listc(tupleFields.size());
        int fieldNumber = 1;
        for (Field field: tupleFields) {
            String fieldName = "field" + String.valueOf(fieldNumber);
            PlcType ftype = convertType(field.getType());
            structFields.add(new PlcStructField(fieldName, ftype));
            fieldNumber++;
        }

        // Construct the structure type.
        String typeName;
        if (tupleType.eContainer() instanceof TypeDecl typeDecl) {
            String structName = CifTextUtils.getAbsName(typeDecl, false);
            typeName = target.getNameGenerator().generateGlobalName(structName, true);
        } else {
            String structName = "TupleStruct" + tupleType.getFields().size();
            typeName = target.getNameGenerator().generateGlobalName(structName, false);
        }
        PlcStructType structType = new PlcStructType(typeName, structFields);

        // Declare the type.
        target.getCodeStorage().addDeclaredType(structType);

        return structType;
    }

    @Override
    public void convertEnumDecl(EnumDecl enumDecl) {
        getOrCreateConvertedEnumDecl(enumDecl);
    }

    @Override
    public PlcExpression convertEnumLiteral(EnumLiteral enumLit) {
        EnumDecl enumDecl = (EnumDecl)enumLit.eContainer();
        EnumDeclData declData = getOrCreateConvertedEnumDecl(enumDecl);
        return declData.getLiteralValue(enumDecl.getLiterals().indexOf(enumLit));
    }

    /**
     * Retrieve or create a converted representation of the requested enumeration declaration.
     *
     * <p>
     * The function combines compatible enumerations (as defined by {@link CifTypeUtils#areEnumsCompatible}) to one PLC
     * enumeration type.
     * </p>
     *
     * @param enumDecl Enumeration declaration to retrieve or create.
     * @return The declaration data associated with the supplied enumeration declaration.
     */
    private EnumDeclData getOrCreateConvertedEnumDecl(EnumDecl enumDecl) {
        return enumTypes.computeIfAbsent(enumDecl, key -> switch (selectedEnumConversion) {
            case CONSTS -> convertToPlcConstants(enumDecl);
            case INTS -> convertToPlcIntegers(enumDecl);
            case KEEP -> convertToPlcEnumType(enumDecl);

            // AUTO also should never happen.
            default -> throw new AssertionError("Unexpected enumeration conversion \""
                    + selectedEnumConversion + "\" found.");
        });
    }

    /**
     * Convert a CIF enumeration to a PLC enum type.
     *
     * @param enumDecl Enumeration declaration to convert.
     * @return Data of the converted enumeration.
     */
    private EnumDeclData convertToPlcEnumType(EnumDecl enumDecl) {
        NameGenerator nameGenerator = target.getNameGenerator();

        // Create name for enum.
        String typeName = nameGenerator.generateGlobalName(enumDecl);

        // Construct the names of the literals.
        List<String> literalNames = enumDecl.getLiterals().stream()
                .map(lit -> nameGenerator.generateGlobalName(lit)).toList();
        PlcEnumType plcEnumType = new PlcEnumType(typeName, literalNames);
        PlcExpression[] litValues = plcEnumType.literals.toArray(PlcExpression[]::new);

        // Give the created enumeration type to code storage, and return the constructed data to the caller.
        target.getCodeStorage().addDeclaredType(plcEnumType);
        return new EnumDeclData(plcEnumType, litValues);
    }

    /**
     * Convert a CIF enumeration to a set of constants.
     *
     * @param enumDecl Enumeration declaration to convert.
     * @return Data of the converted enumeration.
     */
    private EnumDeclData convertToPlcConstants(EnumDecl enumDecl) {
        NameGenerator nameGenerator = target.getNameGenerator();
        PlcCodeStorage codeStorage = target.getCodeStorage();

        // Create the type of the converted enumeration literal values.
        int numLiterals = enumDecl.getLiterals().size();
        PlcType valueType = PlcElementaryType.getTypeByRequiredCount(numLiterals, target.getSupportedBitStringTypes());

        // Construct constants.
        PlcExpression[] litValues = new PlcExpression[numLiterals];
        for (int idx = 0; idx < numLiterals; idx++) {
            String name = nameGenerator.generateGlobalName(enumDecl.getLiterals().get(idx));

            PlcDataVariable constVar = new PlcDataVariable(name, valueType, null, new PlcIntLiteral(idx, valueType));
            codeStorage.addConstant(constVar);
            litValues[idx] = new PlcVarExpression(constVar);
        }

        // Return the data to the caller.
        return new EnumDeclData(valueType, litValues);
    }

    /**
     * Convert a CIF enumeration to a set of integers.
     *
     * @param enumDecl Enumeration declaration to convert.
     * @return Data of the converted enumeration.
     */
    private EnumDeclData convertToPlcIntegers(EnumDecl enumDecl) {
        // Create the type of the converted enumeration literal values.
        int numLiterals = enumDecl.getLiterals().size();
        PlcType valueType = PlcElementaryType.getTypeByRequiredCount(numLiterals, target.getSupportedBitStringTypes());

        // Construct the literal values.
        PlcExpression[] litValues = IntStream.range(0, numLiterals)
                .mapToObj(idx -> new PlcIntLiteral(idx, valueType)).toArray(PlcExpression[]::new);

        // Return the data to the caller.
        return new EnumDeclData(valueType, litValues);
    }

    /** PLC enumeration declaration data of a CIF enumeration declaration. */
    private static class EnumDeclData {
        /** The type of the converted enumeration declaration. */
        public final PlcType plcEnumType;

        /** Values for all literals of the converted enumeration declaration. */
        private final PlcExpression[] literalValues;

        /**
         * Constructor of the {@link EnumDeclData} class.
         *
         * @param plcEnumType The type of the converted enumeration declaration.
         * @param literalValues Values for all literals of the converted enumeration declaration.
         */
        private EnumDeclData(PlcType plcEnumType, PlcExpression[] literalValues) {
            this.plcEnumType = plcEnumType;
            this.literalValues = literalValues;
        }

        /**
         * Convert an enumeration literal of the enumeration declaration to its PLC expression.
         *
         * @param litIndex Index of the literal in the CIF enumeration declaration.
         * @return The associated PLC expression.
         */
        public PlcExpression getLiteralValue(int litIndex) {
            return literalValues[litIndex];
        }
    }
}
