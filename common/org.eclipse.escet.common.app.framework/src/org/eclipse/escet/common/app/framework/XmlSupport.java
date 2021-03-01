//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.app.framework;

import static org.eclipse.escet.common.java.Strings.fmt;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.escet.common.app.framework.exceptions.InputOutputException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;

/** Application framework XML DOM tree support functions. */
public class XmlSupport {
    /** Constructor of the {@link XmlSupport} class. */
    private XmlSupport() {
        // Static class.
    }

    /**
     * Writes an XML file, given the contents as an XML document.
     *
     * @param doc The XML document to use as contents for the file.
     * @param typeName Name of the document type.
     * @param absPath The absolute local file system path of the file to write the output to.
     */
    public static void writeFile(Document doc, String typeName, String absPath) {
        // Construct transformer.
        TransformerFactory xmlTransFactory = TransformerFactory.newInstance();
        Transformer xmlTrans;
        try {
            xmlTrans = xmlTransFactory.newTransformer();
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        }
        xmlTrans.setOutputProperty(OutputKeys.INDENT, "yes");
        xmlTrans.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        // Set document type.
        DocumentType docType = doc.getDoctype();
        if (docType != null) {
            xmlTrans.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, docType.getPublicId());
            xmlTrans.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, docType.getSystemId());
        }

        // Set input/output.
        DOMSource source = new DOMSource(doc);
        FileOutputStream xmlStream;
        try {
            xmlStream = new FileOutputStream(absPath);
        } catch (FileNotFoundException ex) {
            String msg = fmt("Failed to write the %s file to \"%s\".", typeName, absPath);
            throw new InputOutputException(msg, ex);
        }
        StreamResult result = new StreamResult(xmlStream);

        // Transform in-memory tree to the XML file.
        try {
            xmlTrans.transform(source, result);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }

        // Close the file.
        try {
            xmlStream.close();
        } catch (IOException e) {
            String msg = fmt("Failed to close file \"%s\".", absPath);
            throw new InputOutputException(msg, e);
        }
    }
}
