/**
 * Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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
package org.eclipse.escet.cif.metamodel.cif.util;

import java.util.Map;

import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.EObjectValidator;

import org.eclipse.emf.ecore.util.EcoreValidator;
import org.eclipse.emf.ecore.xml.type.util.XMLTypeUtil;

import org.eclipse.escet.cif.metamodel.cif.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Validator</b> for the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.escet.cif.metamodel.cif.CifPackage
 * @generated
 */
public class CifValidator extends EObjectValidator
{
    /**
     * The cached model package
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final CifValidator INSTANCE = new CifValidator();

    /**
     * A constant for the {@link org.eclipse.emf.common.util.Diagnostic#getSource() source} of diagnostic {@link org.eclipse.emf.common.util.Diagnostic#getCode() codes} from this package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.emf.common.util.Diagnostic#getSource()
     * @see org.eclipse.emf.common.util.Diagnostic#getCode()
     * @generated
     */
    public static final String DIAGNOSTIC_SOURCE = "org.eclipse.escet.cif.metamodel.cif";

    /**
     * A constant with a fixed name that can be used as the base value for additional hand written constants.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static final int GENERATED_DIAGNOSTIC_CODE_COUNT = 0;

    /**
     * A constant with a fixed name that can be used as the base value for additional hand written constants in a derived class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static final int DIAGNOSTIC_CODE_COUNT = GENERATED_DIAGNOSTIC_CODE_COUNT;

    /**
     * The cached base package validator.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EcoreValidator ecoreValidator;

    /**
     * Creates an instance of the switch.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CifValidator()
    {
        super();
        ecoreValidator = EcoreValidator.INSTANCE;
    }

    /**
     * Returns the package of this validator switch.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EPackage getEPackage()
    {
      return CifPackage.eINSTANCE;
    }

    /**
     * Calls <code>validateXXX</code> for the corresponding classifier of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected boolean validate(int classifierID, Object value, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        switch (classifierID)
        {
            case CifPackage.COMPONENT:
                return validateComponent((Component)value, diagnostics, context);
            case CifPackage.GROUP:
                return validateGroup((Group)value, diagnostics, context);
            case CifPackage.COMPONENT_DEF:
                return validateComponentDef((ComponentDef)value, diagnostics, context);
            case CifPackage.COMPONENT_INST:
                return validateComponentInst((ComponentInst)value, diagnostics, context);
            case CifPackage.COMPLEX_COMPONENT:
                return validateComplexComponent((ComplexComponent)value, diagnostics, context);
            case CifPackage.SPECIFICATION:
                return validateSpecification((Specification)value, diagnostics, context);
            case CifPackage.PARAMETER:
                return validateParameter((Parameter)value, diagnostics, context);
            case CifPackage.EVENT_PARAMETER:
                return validateEventParameter((EventParameter)value, diagnostics, context);
            case CifPackage.LOCATION_PARAMETER:
                return validateLocationParameter((LocationParameter)value, diagnostics, context);
            case CifPackage.ALG_PARAMETER:
                return validateAlgParameter((AlgParameter)value, diagnostics, context);
            case CifPackage.COMPONENT_PARAMETER:
                return validateComponentParameter((ComponentParameter)value, diagnostics, context);
            case CifPackage.EQUATION:
                return validateEquation((Equation)value, diagnostics, context);
            case CifPackage.IO_DECL:
                return validateIoDecl((IoDecl)value, diagnostics, context);
            case CifPackage.INVARIANT:
                return validateInvariant((Invariant)value, diagnostics, context);
            case CifPackage.SUP_KIND:
                return validateSupKind((SupKind)value, diagnostics, context);
            case CifPackage.INV_KIND:
                return validateInvKind((InvKind)value, diagnostics, context);
            case CifPackage.CIF_IDENTIFIER:
                return validateCifIdentifier((String)value, diagnostics, context);
            default:
                return true;
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateComponent(Component component, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(component, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateGroup(Group group, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(group, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateComponentDef(ComponentDef componentDef, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(componentDef, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateComponentInst(ComponentInst componentInst, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(componentInst, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateComplexComponent(ComplexComponent complexComponent, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(complexComponent, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSpecification(Specification specification, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(specification, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateParameter(Parameter parameter, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(parameter, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateEventParameter(EventParameter eventParameter, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(eventParameter, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateLocationParameter(LocationParameter locationParameter, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(locationParameter, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAlgParameter(AlgParameter algParameter, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(algParameter, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateComponentParameter(ComponentParameter componentParameter, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(componentParameter, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateEquation(Equation equation, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(equation, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateIoDecl(IoDecl ioDecl, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(ioDecl, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateInvariant(Invariant invariant, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(invariant, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSupKind(SupKind supKind, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateInvKind(InvKind invKind, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCifIdentifier(String cifIdentifier, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        boolean result = validateCifIdentifier_Pattern(cifIdentifier, diagnostics, context);
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @see #validateCifIdentifier_Pattern
     */
    public static final  PatternMatcher [][] CIF_IDENTIFIER__PATTERN__VALUES =
        new PatternMatcher [][]
        {
            new PatternMatcher []
            {
                XMLTypeUtil.createPatternMatcher("[A-Za-z_][A-Za-z0-9_]*")
            }
        };

    /**
     * Validates the Pattern constraint of '<em>Identifier</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCifIdentifier_Pattern(String cifIdentifier, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validatePattern(CifPackage.Literals.CIF_IDENTIFIER, cifIdentifier, CIF_IDENTIFIER__PATTERN__VALUES, diagnostics, context);
    }

    /**
     * Returns the resource locator that will be used to fetch messages for this validator's diagnostics.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ResourceLocator getResourceLocator()
    {
        // TODO
        // Specialize this to return a resource locator for messages specific to this validator.
        // Ensure that you remove @generated or mark it @generated NOT
        return super.getResourceLocator();
    }

} //CifValidator
