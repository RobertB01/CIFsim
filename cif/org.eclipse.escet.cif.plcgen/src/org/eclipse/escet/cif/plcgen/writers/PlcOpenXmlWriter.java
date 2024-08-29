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

package org.eclipse.escet.cif.plcgen.writers;

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

import org.eclipse.escet.cif.plcgen.model.declarations.PlcConfiguration;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcDataVariable;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcDeclaredType;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcGlobalVarList;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcGlobalVarList.PlcVarListKind;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcPou;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcPouInstance;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcProject;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcResource;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcTask;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcEnumLiteral;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcExpression;
import org.eclipse.escet.cif.plcgen.model.types.PlcArrayType;
import org.eclipse.escet.cif.plcgen.model.types.PlcDerivedType;
import org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType;
import org.eclipse.escet.cif.plcgen.model.types.PlcEnumType;
import org.eclipse.escet.cif.plcgen.model.types.PlcFuncBlockType;
import org.eclipse.escet.cif.plcgen.model.types.PlcStructField;
import org.eclipse.escet.cif.plcgen.model.types.PlcStructType;
import org.eclipse.escet.cif.plcgen.model.types.PlcType;
import org.eclipse.escet.cif.plcgen.targets.PlcTarget;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.java.PathPair;
import org.eclipse.escet.common.java.exceptions.InputOutputException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/** PLCopen XML (version 2.01) writer. */
public class PlcOpenXmlWriter extends Writer {
    /** PLCopen XML version namespace URI. */
    private static final String PLCOPEN_NS = "http://www.plcopen.org/xml/tc6_0201";

    /** XHTML namespace URI. */
    private static final String XHTML_NS = "http://www.w3.org/1999/xhtml";

    /**
     * Constructor of the {@link PlcOpenXmlWriter} class.
     *
     * @param target PLC target to generate code for.
     */
    public PlcOpenXmlWriter(PlcTarget target) {
        super(target);
    }

    /**
     * {@inheritDoc}
     *
     * @note Writes a PLCopen XML file at the indicated path.
     */
    @Override
    public void write(PlcProject project, PathPair filePaths) {
        // Create document from project.
        Document doc = transProject(project);

        // Write XML document.
        writeDocument(doc, filePaths);

        // Validate XML file against XSD.
        validateDocument(filePaths);
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
        Element fileHeader = makeChild(root, "fileHeader");
        fileHeader.setAttribute("companyName", "Eclipse Foundation");
        fileHeader.setAttribute("productName", "CIF to Structured Text");
        fileHeader.setAttribute("productVersion", "0.0"); // Dummy version.
        Instant instant = Instant.ofEpochMilli(0); // Dummy instant, to allow for automated testing.
        String formattedDateTime = DateTimeFormatter.ISO_INSTANT.format(instant);
        fileHeader.setAttribute("creationDateTime", formattedDateTime);

        Element contentHeader = makeChild(root, "contentHeader");
        contentHeader.setAttribute("name", project.name);

        Element coordInfo = makeChild(contentHeader, "coordinateInfo");

        Element fbd = makeChild(coordInfo, "fbd");
        Element fbdScaling = makeChild(fbd, "scaling");
        fbdScaling.setAttribute("x", "1");
        fbdScaling.setAttribute("y", "1");

        Element ld = makeChild(coordInfo, "ld");
        Element ldScaling = makeChild(ld, "scaling");
        ldScaling.setAttribute("x", "1");
        ldScaling.setAttribute("y", "1");

        Element sfc = makeChild(coordInfo, "sfc");
        Element sfcScaling = makeChild(sfc, "scaling");
        sfcScaling.setAttribute("x", "1");
        sfcScaling.setAttribute("y", "1");

        Element types = makeChild(root, "types");
        Element dataTypes = makeChild(types, "dataTypes");

        Element pous = makeChild(types, "pous");

        Element instances = makeChild(root, "instances");
        Element configurations = makeChild(instances, "configurations");

        // Add data types.
        for (PlcDeclaredType declaredType: project.declaredTypes) {
            transDeclaredType(declaredType, dataTypes);
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
     * Transforms a PLC declared type to PLCopen XML.
     *
     * @param declaredType The declared type.
     * @param parent The parent element in which to generate new elements.
     */
    private void transDeclaredType(PlcDeclaredType declaredType, Element parent) {
        if (declaredType instanceof PlcStructType structType) {
            transDeclaredType(structType, parent);
        } else if (declaredType instanceof PlcEnumType enumType) {
            transDeclaredType(enumType, parent);
        } else {
            throw new AssertionError("Unexpected declared type found: \"" + declaredType + "\".");
        }
    }

    /**
     * Transforms a PLC struct type to PLCopen XML.
     *
     * @param structType The structure type.
     * @param parent The parent element in which to generate new elements.
     */
    private void transDeclaredType(PlcStructType structType, Element parent) {
        Element dataType = makeChild(parent, "dataType");
        dataType.setAttribute("name", structType.typeName);

        Element baseType = makeChild(dataType, "baseType");
        Element struct = makeChild(baseType, "struct");
        for (PlcStructField field: structType.fields) {
            transStructField(field, struct);
        }
    }

    /**
     * Transforms a PLC enum type to PLCopen XML.
     *
     * @param enumType The enum type.
     * @param parent The parent element in which to generate new elements.
     */
    private void transDeclaredType(PlcEnumType enumType, Element parent) {
        Element dataType = makeChild(parent, "dataType");
        dataType.setAttribute("name", enumType.typeName);

        Element baseType = makeChild(dataType, "baseType");
        Element enumElem = makeChild(baseType, "enum");
        Element values = makeChild(enumElem, "values");
        for (PlcEnumLiteral enumLit: enumType.literals) {
            Element value = makeChild(values, "value");
            value.setAttribute("name", enumLit.value);
        }
    }

    /**
     * Transforms a PLC type to PLCopen XML.
     *
     * @param type The type.
     * @param parent The parent element in which to generate new elements.
     */
    private void transType(PlcType type, Element parent) {
        if (type instanceof PlcElementaryType eType) {
            makeChild(parent, eType.name);

        } else if (type instanceof PlcDerivedType dType) {
            Element derived = makeChild(parent, "derived");
            derived.setAttribute("name", dType.name);

        } else if (type instanceof PlcStructType structType) {
            Element derived = makeChild(parent, "derived");
            derived.setAttribute("name", structType.typeName);

        } else if (type instanceof PlcEnumType enumType) {
            Element derived = makeChild(parent, "derived");
            derived.setAttribute("name", enumType.typeName);

        } else if (type instanceof PlcArrayType aType) {
            Element array = makeChild(parent, "array");

            Element dim = makeChild(array, "dimension");
            dim.setAttribute("lower", str(aType.lower));
            dim.setAttribute("upper", str(aType.upper));

            Element bt = makeChild(array, "baseType");
            transType(aType.elemType, bt);

        } else if (type instanceof PlcFuncBlockType blockType) {
            Element derived = makeChild(parent, "derived");
            derived.setAttribute("name", blockType.typeName);

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
    private void transVariable(PlcDataVariable var, Element parent) {
        Element varElem = makeChild(parent, "variable");
        varElem.setAttribute("name", var.varName);
        if (var.address != null) {
            varElem.setAttribute("address", var.address);
        }

        Element type = makeChild(varElem, "type");
        transType(var.type, type);

        if (var.value != null) {
            Element value = makeChild(varElem, "initialValue");
            transValue(var.value, value);
        }
    }

    /**
     * Transforms a PLC structure field to PLCopen XML.
     *
     * @param field The structure field.
     * @param parent The parent element in which to generate new elements.
     */
    private void transStructField(PlcStructField field, Element parent) {
        Element varElem = makeChild(parent, "variable");
        varElem.setAttribute("name", field.fieldName);

        Element type = makeChild(varElem, "type");
        transType(field.type, type);
    }

    /**
     * Transforms a PLC value to PLCopen XML.
     *
     * @param value The value.
     * @param parent The parent element in which to generate new elements.
     */
    private void transValue(PlcExpression value, Element parent) {
        Element vElem = makeChild(parent, "simpleValue");
        vElem.setAttribute("value", target.getModelTextGenerator().literalToString(value));
    }

    /**
     * Transforms a PLC POU to PLCopen XML.
     *
     * @param pou The POU.
     * @param parent The parent element in which to generate new elements.
     */
    private void transPou(PlcPou pou, Element parent) {
        Element pouElem = makeChild(parent, "pou");
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

        Element iface = makeChild(pouElem, "interface");

        if (pou.retType != null) {
            Element rtElem = makeChild(iface, "returnType");
            transType(pou.retType, rtElem);
        }

        if (!pou.inputVars.isEmpty()) {
            Element e = makeChild(iface, "inputVars");
            for (PlcDataVariable var: pou.inputVars) {
                transVariable(var, e);
            }
        }

        if (!pou.inOutVars.isEmpty()) {
            Element e = makeChild(iface, "inOutVars");
            for (PlcDataVariable var: pou.inOutVars) {
                transVariable(var, e);
            }
        }

        if (!pou.outputVars.isEmpty()) {
            Element e = makeChild(iface, "outputVars");
            for (PlcDataVariable var: pou.outputVars) {
                transVariable(var, e);
            }
        }

        if (!pou.localVars.isEmpty()) {
            Element e = makeChild(iface, "localVars");
            for (PlcDataVariable var: pou.localVars) {
                transVariable(var, e);
            }
        }

        if (!pou.tempVars.isEmpty()) {
            Element e = makeChild(iface, "tempVars");
            for (PlcDataVariable var: pou.tempVars) {
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
        Element bodyElem = makeChild(parent, "body");
        Element st = makeChild(bodyElem, "ST");

        Element xhtml = st.getOwnerDocument().createElementNS(XHTML_NS, "xhtml");
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
        Element configElem = makeChild(parent, "configuration");
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
        Element gv = makeChild(parent, "globalVars");
        gv.setAttribute("name", varList.name);
        gv.setAttribute("constant", (varList.listKind == PlcVarListKind.CONSTANTS) ? "true" : "false");
        for (PlcDataVariable var: varList.variables) {
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
        Element resElem = makeChild(parent, "resource");
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
        Element instElem = makeChild(parent, "pouInstance");
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
        Element docElem = makeChild(instElem, "documentation");

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
        Element taskElem = makeChild(parent, "task");

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
     * Construct a child element from the given parent with the given name, attach the new child to the parent and
     * return the new child.
     *
     * @param parent Parent element to extend with a new child element.
     * @param childName Name of the child element that should be created.
     * @return The created child element.
     */
    private Element makeChild(Element parent, String childName) {
        Element child = parent.getOwnerDocument().createElement(childName);
        parent.appendChild(child);
        return child;
    }

    /**
     * Validates the written PLCopen XML file against the XSD schema.
     *
     * @param filePaths The relative or absolute local file system path and the absolute local file system path of the
     *     PLCopen XML file to write.
     */
    private void validateDocument(PathPair filePaths) {
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
            xmlStream = new FileInputStream(filePaths.systemPath);
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
            String msg = fmt("Failed to validate \"%s\" due to an I/O error.", filePaths.userPath);
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
     * @param filePaths The relative or absolute local file system path and the absolute local file system path of the
     *     PLCopen XML file to write.
     */
    private void writeDocument(Document doc, PathPair filePaths) {
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
            xmlStream = new FileOutputStream(filePaths.systemPath);
        } catch (FileNotFoundException ex) {
            String msg = fmt("Failed to write PLCopen XML file to \"%s\".", filePaths.userPath);
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
            String msg = fmt("Failed to close file \"%s\".", filePaths.userPath);
            throw new InputOutputException(msg, e);
        }
    }
}
