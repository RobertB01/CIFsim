/**
 * Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
 * 
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 * 
 * This program and the accompanying materials are made available under the terms
 * of the MIT License which is available at https://opensource.org/licenses/MIT
 * 
 * SPDX-License-Identifier: MIT
 * 
 * Disable Eclipse Java formatter for generated code file:
 * @formatter:off
 */
package org.eclipse.escet.tooldef.metamodel.tooldef.impl;

import java.lang.reflect.Method;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.escet.setext.runtime.Token;

import org.eclipse.escet.tooldef.metamodel.tooldef.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class TooldefFactoryImpl extends EFactoryImpl implements TooldefFactory
{
    /**
     * Creates the default factory implementation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static TooldefFactory init()
    {
        try
        {
            TooldefFactory theTooldefFactory = (TooldefFactory)EPackage.Registry.INSTANCE.getEFactory(TooldefPackage.eNS_URI);
            if (theTooldefFactory != null)
            {
                return theTooldefFactory;
            }
        }
        catch (Exception exception)
        {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new TooldefFactoryImpl();
    }

    /**
     * Creates an instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TooldefFactoryImpl()
    {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EObject create(EClass eClass)
    {
        switch (eClass.getClassifierID())
        {
            case TooldefPackage.SCRIPT: return createScript();
            case TooldefPackage.TYPE_DECL: return createTypeDecl();
            case TooldefPackage.TYPE_PARAM: return createTypeParam();
            case TooldefPackage.TOOL_PARAMETER: return createToolParameter();
            case TooldefPackage.TOOL_DEF_IMPORT: return createToolDefImport();
            case TooldefPackage.JAVA_IMPORT: return createJavaImport();
            case TooldefPackage.TOOL_DEF_TOOL: return createToolDefTool();
            case TooldefPackage.JAVA_TOOL: return createJavaTool();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object createFromString(EDataType eDataType, String initialValue)
    {
        switch (eDataType.getClassifierID())
        {
            case TooldefPackage.TOKEN:
                return createTokenFromString(eDataType, initialValue);
            case TooldefPackage.METHOD:
                return createMethodFromString(eDataType, initialValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String convertToString(EDataType eDataType, Object instanceValue)
    {
        switch (eDataType.getClassifierID())
        {
            case TooldefPackage.TOKEN:
                return convertTokenToString(eDataType, instanceValue);
            case TooldefPackage.METHOD:
                return convertMethodToString(eDataType, instanceValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Script createScript()
    {
        ScriptImpl script = new ScriptImpl();
        return script;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public TypeDecl createTypeDecl()
    {
        TypeDeclImpl typeDecl = new TypeDeclImpl();
        return typeDecl;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public TypeParam createTypeParam()
    {
        TypeParamImpl typeParam = new TypeParamImpl();
        return typeParam;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ToolParameter createToolParameter()
    {
        ToolParameterImpl toolParameter = new ToolParameterImpl();
        return toolParameter;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ToolDefImport createToolDefImport()
    {
        ToolDefImportImpl toolDefImport = new ToolDefImportImpl();
        return toolDefImport;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public JavaImport createJavaImport()
    {
        JavaImportImpl javaImport = new JavaImportImpl();
        return javaImport;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ToolDefTool createToolDefTool()
    {
        ToolDefToolImpl toolDefTool = new ToolDefToolImpl();
        return toolDefTool;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public JavaTool createJavaTool()
    {
        JavaToolImpl javaTool = new JavaToolImpl();
        return javaTool;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Token createTokenFromString(EDataType eDataType, String initialValue)
    {
        return (Token)super.createFromString(eDataType, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertTokenToString(EDataType eDataType, Object instanceValue)
    {
        return super.convertToString(eDataType, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Method createMethodFromString(EDataType eDataType, String initialValue)
    {
        return (Method)super.createFromString(eDataType, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertMethodToString(EDataType eDataType, Object instanceValue)
    {
        return super.convertToString(eDataType, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public TooldefPackage getTooldefPackage()
    {
        return (TooldefPackage)getEPackage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    @Deprecated
    public static TooldefPackage getPackage()
    {
        return TooldefPackage.eINSTANCE;
    }

} //TooldefFactoryImpl
