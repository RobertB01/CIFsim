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

package org.eclipse.escet.common.emf.ecore.xmi;

import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;

/**
 * EMF enabled XMI resource. Unlike instances of the {@link XMIResourceImpl} class, from which this class inherits, the
 * {@link RealXMIResource} class not only has the option to use XMI (de)serialization, but uses it by default.
 */
public class RealXMIResource extends XMIResourceImpl {
    /** Encoding to use for XMI (de)serialization. */
    private static final String XMI_ENCODING = "UTF-8";

    /** Constructor for the {@link RealXMIResource} class, without {@link URI}. */
    public RealXMIResource() {
        this(null);
    }

    /**
     * Constructor for the {@link RealXMIResource} class.
     *
     * @param uri The URI to be associated with the resource.
     */
    public RealXMIResource(URI uri) {
        super(uri);

        // Update default deserialization (load) options.
        Map<Object, Object> loadOptions = getDefaultLoadOptions();
        loadOptions.put(XMLResource.OPTION_LAX_FEATURE_PROCESSING, true);

        // Update default serialization (save) options.
        Map<Object, Object> saveOptions = getDefaultSaveOptions();
        saveOptions.put(OPTION_DECLARE_XML, true);
        saveOptions.put(OPTION_PROCESS_DANGLING_HREF, OPTION_PROCESS_DANGLING_HREF_DISCARD);
        saveOptions.put(OPTION_SCHEMA_LOCATION, true);
        saveOptions.put(OPTION_USE_XMI_TYPE, true);
        saveOptions.put(OPTION_SAVE_TYPE_INFORMATION, true);
        saveOptions.put(OPTION_SKIP_ESCAPE_URI, false);
        saveOptions.put(OPTION_ENCODING, XMI_ENCODING);

        // Set XML encoding to the encoding defined for XMI, if necessary.
        if (!getEncoding().equals(XMI_ENCODING)) {
            setEncoding(XMI_ENCODING);
        }
    }

    @Override
    protected boolean useUUIDs() {
        return true;
    }
}
