//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.io.emf;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;

/** CIF resource factory to force using {@link CifResource} for EMF. */
public class CifResourceFactory implements Resource.Factory {
    @Override
    public Resource createResource(URI uri) {
        return new CifResource(uri);
    }
}
