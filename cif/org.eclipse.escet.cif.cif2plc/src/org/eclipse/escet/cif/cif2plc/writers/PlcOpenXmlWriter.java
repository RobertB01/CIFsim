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

package org.eclipse.escet.cif.cif2plc.writers;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.str;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.eclipse.escet.cif.cif2plc.plcdata.PlcArrayType;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcConfiguration;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcDerivedType;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcElementaryType;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcEnumType;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcGlobalVarList;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcPou;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcPouInstance;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcProject;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcResource;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcStructType;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcTask;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcType;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcTypeDecl;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcValue;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcVariable;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.java.exceptions.InputOutputException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/** PLCopen XML (version 2.01) writer. */
public class PlcOpenXmlWriter extends OutputTypeWriter {
    /** PLCopen XML version namespace URI. */
    private static final String PLCOPEN_NS = "http://www.plcopen.org/xml/tc6_0201";

    /** XHTML namespace URI. */
    private static final String XHTML_NS = "http://www.w3.org/1999/xhtml";

    /**
     * {@inheritDoc}
     *
     * @note Writes a PLCopen XML file at the indicated path.
     */
    @Override
    public void write(PlcProject project, String filePath) {
        filePath = Paths.resolve(filePath); // Switch to platform-specific file separators.

        // Create document from project.
        Document doc = transProject(project);

        // Write XML document.
        writeDocument(doc, filePath);

        // Validate XML file against XSD.
        validateDocument(filePath);
    }

    /**
     * Transforms a PLC project to PLCopen XML.
     *
     * @param project The project.
     * @return The XML document.
     */
    private Document transProject(PlcProject project) {
        // Create document builder.
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);

        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }

        // Create document and get root.
        DOMImplementation domImpl = builder.getDOMImplementation();
        Document doc = domImpl.createDocument(PLCOPEN_NS, "project", null);
        doc.setXmlStandalone(true);
        Element root = doc.getDocumentElement();

        // Add required elements and project data.
        Element fileHeader = doc.createElement("fileHeader");
        root.appendChild(fileHeader);
        fileHeader.setAttribute("companyName", "Eclipse Foundation");
        fileHeader.setAttribute("productName", "CIF to Structured Text");
        fileHeader.setAttribute("productVersion", "0.0"); // Dummy version.
        Instant instant = Instant.ofEpochMilli(0); // Dummy instant, to allow for automated testing.
        String formattedDateTime = DateTimeFormatter.ISO_INSTANT.format(instant);
        fileHeader.setAttribute("creationDateTime", formattedDateTime);

        Element contentHeader = doc.createElement("contentHeader");
        root.appendChild(contentHeader);
        contentHeader.setAttribute("name", project.name);

        Element coordInfo = doc.createElement("coordinateInfo");
        contentHeader.appendChild(coordInfo);

        Element fbd = doc.createElement("fbd");
        coordInfo.appendChild(fbd);

        Element fbdScaling = doc.createElement("scaling");
        fbd.appendChild(fbdScaling);
        fbdScaling.setAttribute("x", "1");
        fbdScaling.setAttribute("y", "1");

        Element ld = doc.createElement("ld");
        coordInfo.appendChild(ld);

        Element ldScaling = doc.createElement("scaling");
        ld.appendChild(ldScaling);
        ldScaling.setAttribute("x", "1");
        ldScaling.setAttribute("y", "1");

        Element sfc = doc.createElement("sfc");
        coordInfo.appendChild(sfc);

        Element sfcScaling = doc.createElement("scaling");
        sfc.appendChild(sfcScaling);
        sfcScaling.setAttribute("x", "1");
        sfcScaling.setAttribute("y", "1");

        Element types = doc.createElement("types");
        root.appendChild(types);

        Element dataTypes = doc.createElement("dataTypes");
        types.appendChild(dataTypes);

        Element pous = doc.createElement("pous");
        types.appendChild(pous);

        Element instances = doc.createElement("instances");
        root.appendChild(instances);

        Element configurations = doc.createElement("configurations");
        instances.appendChild(configurations);

        // Add data types.
        for (PlcTypeDecl typeDecl: project.typeDecls) {
            transTypeDecl(typeDecl, dataTypes);
        }

        // Add POUs.
        for (PlcPou pou: project.pous) {
            transPou(pou, pous);
        }

        // Add configurations.
        for (PlcConfiguration config: project.configurations) {
            transConfig(config, configurations);
        }

        // Return document.
        return doc;
    }

    /**
     * Transforms a PLC type declaration to PLCopen XML.
     *
     * @param typeDecl The type declaration.
     * @param parent The parent element in which to generate new elements.
     */
    private void transTypeDecl(PlcTypeDecl typeDecl, Element parent) {
        Element dataType = parent.getOwnerDocument().createElement("dataType");
        parent.appendChild(dataType);

        dataType.setAttribute("name", typeDecl.name);

        Element baseType = parent.getOwnerDocument().createElement("baseType");
        dataType.appendChild(baseType);

        transType(typeDecl.type, baseType);
    }

    /**
     * Transforms a PLC type to PLCopen XML.
     *
     * @param type The type.
     * @param parent The parent element in which to generate new elements.
     */
    private void transType(PlcType type, Element parent) {
        if (type instanceof PlcElementaryType) {
            PlcElementaryType etype = (PlcElementaryType)type;
            Element elem = parent.getOwnerDocument().createElement(etype.name);
            parent.appendChild(elem);
        } else if (type instanceof PlcDerivedType) {
            Element derived = parent.getOwnerDocument().createElement("derived");
            parent.appendChild(derived);

            PlcDerivedType dtype = (PlcDerivedType)type;
            derived.setAttribute("name", dtype.name);
        } else if (type instanceof PlcEnumType) {
            Element enumElem = parent.getOwnerDocument().createElement("enum");
            parent.appendChild(enumElem);

            Element values = parent.getOwnerDocument().createElement("values");
            enumElem.appendChild(values);

            PlcEnumType etype = (PlcEnumType)type;
            for (String lit: etype.literals) {
                Element value = parent.getOwnerDocument().createElement("value");
                values.appendChild(value);
                value.setAttribute("name", lit);
            }
        } else if (type instanceof PlcStructType) {
            Element struct = parent.getOwnerDocument().createElement("struct");
            parent.appendChild(struct);

            PlcStructType stype = (PlcStructType)type;
            for (PlcVariable field: stype.fields) {
                transVariable(field, struct);
            }
        } else if (type instanceof PlcArrayType) {
            Element array = parent.getOwnerDocument().createElement("array");
            parent.appendChild(array);

            PlcArrayType atype = (PlcArrayType)type;

            Element dim = parent.getOwnerDocument().createElement("dimension");
            array.appendChild(dim);
            dim.setAttribute("lower", str(atype.lower));
            dim.setAttribute("upper", str(atype.upper));

            Element bt = parent.getOwnerDocument().createElement("baseType");
            array.appendChild(bt);
            transType(atype.elemType, bt);
        } else {
            throw new RuntimeException("Unknown plc type: " + type);
        }
    }

    /**
     * Transforms a PLC variable to PLCopen XML.
     *
     * @param var The variable.
     * @param parent The parent element in which to generate new elements.
     */
    private void transVariable(PlcVariable var, Element parent) {
        Element varElem = parent.getOwnerDocument().createElement("variable");
        parent.appendChild(varElem);

        varElem.setAttribute("name", var.name);
        if (var.address != null) {
            varElem.setAttribute("address", var.address);
        }

        Element type = parent.getOwnerDocument().createElement("type");
        varElem.appendChild(type);
        transType(var.type, type);

        if (var.value != null) {
            Element value = parent.getOwnerDocument().createElement("initialValue");
            varElem.appendChild(value);
            transValue(var.value, value);
        }
    }

    /**
     * Transforms a PLC value to PLCopen XML.
     *
     * @param value The value.
     * @param parent The parent element in which to generate new elements.
     */
    private void transValue(PlcValue value, Element parent) {
        Element vElem = parent.getOwnerDocument().createElement("simpleValue");
        parent.appendChild(vElem);

        vElem.setAttribute("value", value.value);
    }

    /**
     * Transforms a PLC POU to PLCopen XML.
     *
     * @param pou The POU.
     * @param parent The parent element in which to generate new elements.
     */
    private void transPou(PlcPou pou, Element parent) {
        Element pouElem = parent.getOwnerDocument().createElement("pou");
        parent.appendChild(pouElem);

        pouElem.setAttribute("name", pou.name);
        String pouTypeText = null;
        switch (pou.pouType) {
            case FUNCTION:
                pouTypeText = "function";
                break;
            case PROGRAM:
                pouTypeText = "program";
                break;
        }
        pouElem.setAttribute("pouType", pouTypeText);

        Element iface = parent.getOwnerDocument().createElement("interface");
        pouElem.appendChild(iface);

        if (pou.retType != null) {
            Element rtElem = parent.getOwnerDocument().createElement("returnType");
            iface.appendChild(rtElem);

            transType(pou.retType, rtElem);
        }

        if (!pou.inputVars.isEmpty()) {
            Element e = parent.getOwnerDocument().createElement("inputVars");
            iface.appendChild(e);

            for (PlcVariable var: pou.inputVars) {
                transVariable(var, e);
            }
        }

        if (!pou.outputVars.isEmpty()) {
            Element e = parent.getOwnerDocument().createElement("outputVars");
            iface.appendChild(e);

            for (PlcVariable var: pou.outputVars) {
                transVariable(var, e);
            }
        }

        if (!pou.localVars.isEmpty()) {
            Element e = parent.getOwnerDocument().createElement("localVars");
            iface.appendChild(e);

            for (PlcVariable var: pou.localVars) {
                transVariable(var, e);
            }
        }

        if (!pou.tempVars.isEmpty()) {
            Element e = parent.getOwnerDocument().createElement("tempVars");
            iface.appendChild(e);

            for (PlcVariable var: pou.tempVars) {
                transVariable(var, e);
            }
        }

        transBody(pou.body, pouElem);
    }

    /**
     * Transforms a PLC POU body to PLCopen XML.
     *
     * @param body The body.
     * @param parent The parent element in which to generate new elements.
     */
    private void transBody(CodeBox body, Element parent) {
        Element bodyElem = parent.getOwnerDocument().createElement("body");
        parent.appendChild(bodyElem);

        Element st = parent.getOwnerDocument().createElement("ST");
        bodyElem.appendChild(st);

        Element xhtml = parent.getOwnerDocument().createElementNS(XHTML_NS, "xhtml");
        st.appendChild(xhtml);

        xhtml.setTextContent(body.toString());
    }

    /**
     * Transforms a PLC configuration to PLCopen XML.
     *
     * @param config The configuration.
     * @param parent The parent element in which to generate new elements.
     */
    private void transConfig(PlcConfiguration config, Element parent) {
        Element configElem = parent.getOwnerDocument().createElement("configuration");
        parent.appendChild(configElem);

        configElem.setAttribute("name", config.name);

        for (PlcResource resource: config.resources) {
            transResource(resource, configElem);
        }

        for (PlcGlobalVarList varList: config.globalVarLists) {
            transGlobalVarList(varList, configElem);
        }
    }

    /**
     * Transforms a PLC global variable list to PLCopen XML.
     *
     * @param varList The global variable list.
     * @param parent The parent element in which to generate new elements.
     */
    private void transGlobalVarList(PlcGlobalVarList varList, Element parent) {
        // Skip if no variables.
        if (varList.variables.isEmpty()) {
            return;
        }

        // We have variables, so add it.
        Element gv = parent.getOwnerDocument().createElement("globalVars");
        parent.appendChild(gv);

        gv.setAttribute("name", varList.name);
        gv.setAttribute("constant", varList.constants ? "true" : "false");
        for (PlcVariable var: varList.variables) {
            transVariable(var, gv);
        }
    }

    /**
     * Transforms a PLC resource to PLCopen XML.
     *
     * @param resource The resource.
     * @param parent The parent element in which to generate new elements.
     */
    private void transResource(PlcResource resource, Element parent) {
        Element resElem = parent.getOwnerDocument().createElement("resource");
        parent.appendChild(resElem);

        resElem.setAttribute("name", resource.name);

        for (PlcTask task: resource.tasks) {
            transTask(task, resElem);
        }

        for (PlcGlobalVarList varList: resource.globalVarLists) {
            transGlobalVarList(varList, resElem);
        }

        for (PlcPouInstance inst: resource.pouInstances) {
            transPouInstance(inst, resElem);
        }
    }

    /**
     * Transforms a PLC POU instance to PLCopen XML.
     *
     * @param inst The POU instance.
     * @param parent The parent element in which to generate new elements.
     * @return The newly created element for the POU instance.
     */
    private Element transPouInstance(PlcPouInstance inst, Element parent) {
        Element instElem = parent.getOwnerDocument().createElement("pouInstance");
        parent.appendChild(instElem);

        instElem.setAttribute("name", inst.name);
        instElem.setAttribute("typeName", inst.pou.name);

        return instElem;
    }

    /**
     * Transforms a PLC POU instance to PLCopen XML and adds a documentation child element to the POU instance.
     *
     * @param inst The POU instance.
     * @param parent The parent element in which to generate new elements.
     */
    private void transPouInstanceWithDoc(PlcPouInstance inst, Element parent) {
        Element instElem = transPouInstance(inst, parent);

        Element docElem = instElem.getOwnerDocument().createElement("documentation");
        instElem.appendChild(docElem);

        Element xhtml = docElem.getOwnerDocument().createElementNS(XHTML_NS, "xhtml");
        docElem.appendChild(xhtml);
    }

    /**
     * Transforms a PLC task to PLCopen XML.
     *
     * @param task The task.
     * @param parent The parent element in which to generate new elements.
     */
    private void transTask(PlcTask task, Element parent) {
        Element taskElem = parent.getOwnerDocument().createElement("task");
        parent.appendChild(taskElem);

        taskElem.setAttribute("name", task.name);
        // Interval value is vendor specific, TwinCAT and CODESYS use ISO 8601 Durations.
        taskElem.setAttribute("interval", fmt("PT%.3fS", (float)task.cycleTime / 1000));
        taskElem.setAttribute("priority", str(task.priority));

        for (PlcPouInstance inst: task.pouInstances) {
            // Make sure to also include a documentation element as a child of this POU otherwise TwinCAT and CODESYS
            // give an 'object reference not set' error when creating this task element.
            transPouInstanceWithDoc(inst, taskElem);
        }
    }

    /**
     * Validates the written PLCopen XML file against the XSD schema.
     *
     * @param filePath The absolute local file system path of the PLCopen XML file, with platform specific file
     *     separators.
     */
    private void validateDocument(String filePath) {
        InputStream schemaStream = null;
        InputStream xmlStream = null;
        try {
            // Get XSD schema source.
            String schemaName = PlcOpenXmlWriter.class.getPackage().getName();
            schemaName = schemaName.replace('.', '/') + "/tc6_xml_v201.xsd";
            ClassLoader classLoader = PlcOpenXmlWriter.class.getClassLoader();
            schemaStream = classLoader.getResourceAsStream(schemaName);
            Source schemaSrc = new StreamSource(schemaStream);

            // Get XML file source.
            xmlStream = new FileInputStream(filePath);
            xmlStream = new BufferedInputStream(xmlStream);
            Source xmlSrc = new StreamSource(xmlStream);

            // Get validator.
            SchemaFactory schemaFactory = SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);
            Schema schema;
            try {
                schema = schemaFactory.newSchema(schemaSrc);
            } catch (SAXException e) {
                throw new RuntimeException(e);
            }
            Validator validator = schema.newValidator();

            // Validate.
            try {
                validator.validate(xmlSrc);
            } catch (SAXException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            String msg = fmt("Failed to validate \"%s\" due to an I/O error.", filePath);
            throw new InputOutputException(msg, e);
        } finally {
            // Always close streams.
            if (schemaStream != null) {
                try {
                    schemaStream.close();
                } catch (IOException e) {
                    // Ignore.
                }
            }
            if (xmlStream != null) {
                try {
                    xmlStream.close();
                } catch (IOException e) {
                    // Ignore.
                }
            }
        }
    }

    /**
     * Writes a PLCopen XML file, given the contents as an XML document.
     *
     * @param doc The XML document to use as contents for the file.
     * @param filePath The absolute local file system path of the PLCopen XML file to write, with platform specific file
     *     separators.
     */
    private void writeDocument(Document doc, String filePath) {
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

        // Set input/output.
        DOMSource source = new DOMSource(doc);
        FileOutputStream xmlStream;
        try {
            xmlStream = new FileOutputStream(filePath);
        } catch (FileNotFoundException ex) {
            String msg = fmt("Failed to write PLCopen XML file to \"%s\".", filePath);
            throw new InputOutputException(msg, ex);
        }
        StreamResult result = new StreamResult(xmlStream);

        // Transform in-memory tree to file.
        try {
            xmlTrans.transform(source, result);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }

        // Close the file.
        try {
            xmlStream.close();
        } catch (IOException e) {
            String msg = fmt("Failed to close file \"%s\".", filePath);
            throw new InputOutputException(msg, e);
        }
    }
}
