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

package org.eclipse.escet.common.typechecker;

import java.io.IOException;
import java.io.OutputStream;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.escet.common.app.framework.exceptions.InputOutputException;
import org.eclipse.escet.common.emf.EMFHelper;
import org.eclipse.escet.common.emf.EMFValidationHelper;

/**
 * Base class for type checkers that return instances of Ecore metamodels.
 *
 * @param <TIn> The type of the abstract syntax tree (AST) input of the type checker.
 * @param <TOut> The type of the decorated abstract syntax tree (DAST) output of the type checker.
 */
public abstract class EcoreTypeChecker<TIn, TOut extends EObject> extends TypeChecker<TIn, TOut> {
    /**
     * Whether to perform EMF validation after type checking completes without errors. Disabled by default for
     * performance reasons. May be enabled to perform the validation, for instance during the development of the type
     * checker.
     */
    public boolean doEmfValidation = false;

    @Override
    public TOut typeCheck(TIn rootObj) {
        TOut rslt = super.typeCheck(rootObj);
        if (rslt == null) {
            return null;
        }

        // Write XML representation of the type check result.
        OutputStream stream = getXmlOutputStream();
        if (stream != null) {
            EMFHelper.printEObjectXML(rslt, getEcoreFileExtension(), stream);
            try {
                stream.close();
            } catch (IOException ex) {
                throw new InputOutputException("Failed to close stream.", ex);
            }
        }

        // Validate result.
        if (doEmfValidation) {
            EMFValidationHelper.validateObject(rslt);
        }

        // Return type check result.
        return rslt;
    }

    /**
     * Returns the file extension for instances of the Ecore metamodel that this type checker returns. The file
     * extension must not include a dot. For instance, return {@code "ecore"} and not {@code ".ecore"}.
     *
     * @return The file extension for instances of the Ecore metamodel that this type checker returns.
     */
    protected abstract String getEcoreFileExtension();

    /**
     * Returns the stream to which to write the XML representation of the type checking result. The default
     * implementation returns {@code null}, meaning the XML representation is not written at all. Derived classes may
     * override this method to provide an actual stream, and thus enable writing.
     *
     * @return The output stream to which to write the XML representation of the type checking result, or {@code null}.
     */
    protected OutputStream getXmlOutputStream() {
        return null;
    }
}
