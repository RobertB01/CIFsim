//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.asciidoc.html.multipage;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.google.common.base.Verify;

/** AsciiDoc Eclipse help TOC generator. */
class AsciiDocEclipseHelpTocUtil {
    /** Constructor for the {@link AsciiDocEclipseHelpTocUtil} class. */
    private AsciiDocEclipseHelpTocUtil() {
        // Static class.
    }

    /**
     * Convert TOC to Eclipse help XML format.
     *
     * @param toc The TOC.
     * @return The TOC in Eclipse help XML format.
     */
    static org.w3c.dom.Document tocToEclipseHelpXml(AsciiDocTocEntry toc) {
        // Create document.
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException("Failed to create XML document builder.", e);
        }
        Document doc = builder.getDOMImplementation().createDocument(null, null, null);
        doc.setXmlVersion("1.0");

        // Add TOC entries.
        addEntries(doc, toc, toc);

        // Return document.
        return doc;
    }

    /**
     * Add TOC entries to the given node.
     *
     * @param node The node to which to add the TOC entries.
     * @param entry The TOC entry to add.
     * @param rootEntry The TOC root entry.
     */
    private static void addEntries(Node node, AsciiDocTocEntry entry, AsciiDocTocEntry rootEntry) {
        // Add XML element for TOC entry.
        String tagName = (node instanceof Document) ? "toc" : "topic";
        Document doc = (node instanceof Document) ? (Document)node : node.getOwnerDocument();
        Element elem = doc.createElement(tagName);
        node.appendChild(elem);

        // Set attributes.
        elem.setAttribute("label", entry.title);
        String refAttrName = (node instanceof Document) ? "topic" : "href";
        String refTxt = AsciiDocHtmlUtil.getFileOrSectionHref(rootEntry.page, entry.page, entry.refId);
        if (node instanceof Document) {
            Verify.verify(entry == rootEntry);
            Verify.verify(refTxt.equals("#"));
            refTxt = rootEntry.page.sourceFile.getBaseName() + ".html";
        }
        elem.setAttribute(refAttrName, refTxt);

        // Recursively add child TOC entries.
        for (AsciiDocTocEntry childEntry: entry.children) {
            addEntries(elem, childEntry, rootEntry);
        }
    }

    /**
     * Writes TOC XML file.
     *
     * @param doc The TOC XML document.
     * @param absPath The absolute local file system path of the TOC XML file to write.
     */
    static void writeTocXmlFile(Document doc, String absPath) {
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

        // Transform in-memory tree to the XML file.
        DOMSource source = new DOMSource(doc);
        try (OutputStream xmlStream = new BufferedOutputStream(new FileOutputStream(absPath))) {
            StreamResult result = new StreamResult(xmlStream);
            try {
                xmlTrans.transform(source, result);
            } catch (TransformerException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(String.format("Failed to write the TOC XML file to \"%s\".", absPath), e);
        }
    }
}
