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

import org.eclipse.escet.cif.common.CifEnumUtils.EnumDeclEqHashWrap;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.TypeEqHashWrap;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl;
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
import org.eclipse.escet.cif.plcgen.model.types.PlcArrayType;
import org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType;
import org.eclipse.escet.cif.plcgen.model.types.PlcEnumType;
import org.eclipse.escet.cif.plcgen.model.types.PlcStructField;
import org.eclipse.escet.cif.plcgen.model.types.PlcStructType;
import org.eclipse.escet.cif.plcgen.model.types.PlcType;
import org.eclipse.escet.cif.plcgen.options.ConvertEnums;
import org.eclipse.escet.cif.plcgen.targets.PlcTarget;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Lists;

/** Class for handling types. */
public class DefaultTypeGenerator implements TypeGenerator {
    /** PLC target to generate code for. */
    private final PlcTarget target;

    /** Standard integer type. */
    private final PlcElementaryType standardIntType;

    /** Standard real type. */
    private final PlcElementaryType standardRealType;

    /** Mapping from CIF tuple types wrapped in {@link TypeEqHashWrap} instances to their structure type. */
    private final Map<TypeEqHashWrap, PlcStructType> structTypes = map();

    /**
     * Mapping from CIF enumerations to their PLC enumeration type.
     *
     * <p>
     * The map combines compatible enumerations (as defined by {@link CifTypeUtils#areEnumsCompatible}) to one PLC
     * enumeration type.
     * </p>
     */
    private final Map<EnumDeclEqHashWrap, PlcEnumType> enumTypes = map();

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
            // TODO Improve the relation from the PLC code back to the CIF specification by using supplied field names.
            String fieldName = "field" + String.valueOf(fieldNumber);
            PlcType ftype = convertType(field.getType());
            structFields.add(new PlcStructField(fieldName, ftype));
            fieldNumber++;
        }

        // Construct the structure type.
        String structName;
        if (tupleType.eContainer() instanceof TypeDecl typeDecl) {
            structName = CifTextUtils.getAbsName(typeDecl, false);
        } else {
            structName = "TupleStruct" + tupleType.getFields().size();
        }
        String typeName = target.getNameGenerator().generateGlobalName(structName, false);
        PlcStructType structType = new PlcStructType(typeName, structFields);

        // Declare the type.
        target.getCodeStorage().addDeclaredType(structType);

        return structType;
    }

    @Override
    public PlcEnumType convertEnumDecl(EnumDecl enumDecl) {
        EnumDeclEqHashWrap wrappedEnumDecl = new EnumDeclEqHashWrap(enumDecl);
        return enumTypes.computeIfAbsent(wrappedEnumDecl, key -> makePlcEnumType(enumDecl));
    }

    /**
     * Make a new enumeration type.
     *
     * @param enumDecl Enumeration declaration to convert.
     * @return The created PLC type.
     */
    public PlcEnumType makePlcEnumType(EnumDecl enumDecl) {
        // Ensure enum conversion is only done when they are supposed to be used.
        Assert.areEqual(target.getActualEnumerationsConversion(), ConvertEnums.KEEP);

        NameGenerator nameGenerator = target.getNameGenerator();

        // Convert the enumeration literals to unique value names.
        List<String> litNames = enumDecl.getLiterals().stream()
                .map(lit -> nameGenerator.generateGlobalName(CifTextUtils.getAbsName(lit, false), true))
                .collect(Lists.toList());

        // Construct the type and add it to the global declared types.
        String typeName = nameGenerator.generateGlobalName(CifTextUtils.getAbsName(enumDecl, false), true);
        PlcEnumType plcEnumType = new PlcEnumType(typeName, litNames);
        target.getCodeStorage().addDeclaredType(plcEnumType);

        return plcEnumType;
    }
}
