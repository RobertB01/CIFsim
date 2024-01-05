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

package org.eclipse.escet.common.java.exceptions;

/**
 * Interface that all exceptions that provide an end-user readable message should implement.
 *
 * <p>
 * By implementing this interface, the exception is handled differently by the application framework, when printing
 * exception causes. For classes that implement this interface, the name of the exception class is not printed as part
 * of the cause.
 * </p>
 */
public interface EndUserException {
    // Exception is used as a property only, and does not define any behavior.
}
