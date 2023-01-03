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

package org.eclipse.escet.common.emf.ecore.validation;

import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EObjectValidator;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.emf.validation.service.IBatchValidator;
import org.eclipse.emf.validation.service.ModelValidationService;

/** An adapter that plugs the EMF Model Validation Service API into the {@link org.eclipse.emf.ecore.EValidator} API. */
public class EValidatorAdapter extends EObjectValidator {
    /** The batch validator to use to perform validation. */
    private final IBatchValidator batchValidator;

    /** Constructor for the {@link EValidatorAdapter} class. */
    public EValidatorAdapter() {
        batchValidator = ModelValidationService.getInstance().newValidator(EvaluationMode.BATCH);
        batchValidator.setIncludeLiveConstraints(true);
        batchValidator.setReportSuccesses(false);
    }

    @Override
    public boolean validate(EObject eObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate(eObject.eClass(), eObject, diagnostics, context);
    }

    @Override
    public boolean validate(EClass eClass, EObject eObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
        // First, do whatever the basic EcoreValidator does.
        super.validate(eClass, eObject, diagnostics, context);

        IStatus status = Status.OK_STATUS;

        // No point in validating if we can't report results.
        if (diagnostics != null) {
            // If EMF Mode Validation Service already covered the sub-tree,
            // which it does for efficient computation and error reporting,
            // then don't repeat (the Diagnostician does the recursion
            // externally). If there is no context map, then we can't
            // help it.
            if (!hasProcessed(eObject, context)) {
                status = batchValidator.validate(eObject, new NullProgressMonitor());

                processed(eObject, context, status);

                appendDiagnostics(status, diagnostics);
            }
        }

        return status.isOK();
    }

    @Override
    public boolean validate(EDataType eDataType, Object value, DiagnosticChain diagnostics,
            Map<Object, Object> context)
    {
        // Direct validation of EDataTypes is not supported by the EMF
        // validation framework. They are validated indirectly via the
        // EObjects that hold their values.
        return super.validate(eDataType, value, diagnostics, context);
    }

    /**
     * If we have a context map, record this object's {@code status} in it so that we will know later that we have
     * processed it and its sub-tree.
     *
     * @param eObject An element that we have validated.
     * @param context The context (may be {@code null}).
     * @param status The element's validation status.
     */
    private void processed(EObject eObject, Map<Object, Object> context, IStatus status) {
        if (context != null) {
            context.put(eObject, status);
        }
    }

    /**
     * Determines whether we have processed this {@code eObject} before, by automatic recursion of the EMF Model
     * Validation Service. This is only possible if we do, indeed, have a context.
     *
     * @param eObject An element to be validated (we hope not).
     * @param context The context (may be {@code null}).
     * @return {@code true} if the context is not {@code null} and the {@code eObject} or one of its containers has
     *     already been validated, {@code false} otherwise.
     */
    private boolean hasProcessed(EObject eObject, Map<Object, Object> context) {
        boolean result = false;

        if (context != null) {
            // This is O(N log N) but there's no helping it.
            while (eObject != null) {
                if (context.containsKey(eObject)) {
                    result = true;
                    eObject = null;
                } else {
                    eObject = eObject.eContainer();
                }
            }
        }

        return result;
    }

    /**
     * Converts a status result from the EMF validation service to diagnostics.
     *
     * @param status The EMF validation service's status result.
     * @param diagnostics A diagnostic chain to accumulate results on.
     */
    private void appendDiagnostics(IStatus status, DiagnosticChain diagnostics) {
        if (status.isMultiStatus()) {
            IStatus[] children = status.getChildren();

            for (IStatus element: children) {
                appendDiagnostics(element, diagnostics);
            }
        } else if (status instanceof IConstraintStatus) {
            diagnostics.add(new BasicDiagnostic(status.getSeverity(), status.getPlugin(), status.getCode(),
                    status.getMessage(), ((IConstraintStatus)status).getResultLocus().toArray()));
        }
    }
}
