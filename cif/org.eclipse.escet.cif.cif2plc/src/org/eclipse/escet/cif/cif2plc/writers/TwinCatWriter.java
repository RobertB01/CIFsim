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

package org.eclipse.escet.cif.cif2plc.writers;

import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.str;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.eclipse.escet.cif.cif2plc.plcdata.PlcConfiguration;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcGlobalVarList;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcPou;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcPouInstance;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcProject;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcResource;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcTask;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcTypeDecl;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.exceptions.InputOutputException;
import org.eclipse.escet.common.app.framework.exceptions.InvalidOptionException;
import org.eclipse.escet.common.java.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/** TwinCAT 3.1 writer. */
public class TwinCatWriter {
    /** The PLC project to use, {@code null} until available. */
    private PlcProject project;

    /** The PLC configuration to use, {@code null} until available. */
    private PlcConfiguration configuration;

    /** The PLC resource to use, {@code null} until available. */
    private PlcResource resource;

    /** The PLC task to use, {@code null} until available. */
    private PlcTask task;

    /** The TwinCAT XAE project (.tsproj) file, {@code null} until available. */
    private File xaeProjFile;

    /** The TwinCAT PLC project (.plcproj) file, {@code null} until available. */
    private File plcProjFile;

    /** The directory containing the TwinCAT PLC project (.plcproj) file, {@code null} until available. */
    private File plcProjDirFile;

    /** Mapping from PLC project relative file paths to their XML content. */
    private Map<String, Document> files = map();

    /** Old code files that are scheduled to be removed (since there are no replacements in {@link #files}). */
    private List<File> oldCodeFiles = list();

    /** Constructor for the {@link TwinCatWriter} class. */
    private TwinCatWriter() {
        // Private constructor to force the use of the public static method.
    }

    /**
     * Writes the given PLC project to a TwinCAT project.
     *
     * @param project The PLC project to write.
     * @param slnDirPath The absolute local file system path of the directory containing the TwinCAT solution, with
     *     platform specific path separators.
     */
    public static void write(PlcProject project, String slnDirPath) {
        // Initialize writer.
        TwinCatWriter writer = new TwinCatWriter();
        writer.project = project;
        Assert.check(project.configurations.size() == 1);
        writer.configuration = first(project.configurations);
        Assert.check(writer.configuration.resources.size() == 1);
        writer.resource = first(writer.configuration.resources);
        Assert.check(writer.resource.tasks.size() == 1);
        writer.task = first(writer.resource.tasks);

        if (writer.task.cycleTime == 0) {
            String msg = "TwinCAT output with periodic task scheduling disabled, is currently not supported.";
            throw new InvalidOptionException(msg);
        }

        writer.findTwinCatProjects(slnDirPath);

        // POU instances in the resource are not supported.
        Assert.check(writer.resource.pouInstances.isEmpty());

        // Update TwinCAT XAE project.
        writer.updateXaeProj();
        writer.genCodeFiles();
        writer.updatePlcProj();
        writer.updateTask();
        writer.updateCodeFiles();
    }

    /**
     * Finds the projects within the TwinCAT solution.
     *
     * @param slnDirPath The absolute local file system path of the directory containing the TwinCAT solution, with
     *     platform specific path separators.
     * @throws InvalidOptionException If the solution path does not refer to a directory with the expected files and
     *     sub-directories.
     */
    private void findTwinCatProjects(String slnDirPath) {
        // Find solution directory.
        File slnDirFile = new File(slnDirPath);
        if (!slnDirFile.isDirectory()) {
            String msg = fmt("TwinCAT solution directory \"%s\" does not exist, or is not a directory.",
                    slnDirFile.getPath());
            throw new InvalidOptionException(msg);
        }

        // Find solution file.
        String slnDirName = slnDirFile.getName();
        File slnFile = new File(slnDirFile, slnDirName + ".sln");
        if (!slnFile.isFile()) {
            String msg = fmt("TwinCAT solution file \"%s\" does not exist, or is not a file.", slnFile.getPath());
            throw new InvalidOptionException(msg);
        }

        // Find TwinCAT XAE project directory.
        File xaeDirFile = new File(slnDirFile, slnDirName);
        if (!xaeDirFile.isDirectory()) {
            String msg = fmt("TwinCAT XAE project directory \"%s\" does not exist, or is not a directory.",
                    xaeDirFile.getPath());
            throw new InvalidOptionException(msg);
        }

        // Find TwinCAT XAE project file.
        xaeProjFile = new File(xaeDirFile, slnDirName + ".tsproj");
        if (!xaeProjFile.isFile()) {
            String msg = fmt("TwinCAT XAE project file \"%s\" does not exist, or is not a file.",
                    xaeProjFile.getPath());
            throw new InvalidOptionException(msg);
        }

        // Find TwinCAT PLC project directory.
        plcProjDirFile = new File(xaeDirFile, project.name);
        if (!plcProjDirFile.isDirectory()) {
            String msg = fmt("TwinCAT PLC project directory \"%s\" does not exist, or is not a directory.",
                    plcProjDirFile.getPath());
            throw new InvalidOptionException(msg);
        }

        // Find TwinCAT PLC project file.
        plcProjFile = new File(plcProjDirFile, project.name + ".plcproj");
        if (!plcProjFile.isFile()) {
            String msg = fmt("TwinCAT PLC project file \"%s\" does not exist, or is not a file.",
                    plcProjFile.getPath());
            throw new InvalidOptionException(msg);
        }
    }

    /** Update the TwinCAT XAE project (.tsproj) file. */
    private void updateXaeProj() {
        // Read project file.
        Document doc = readXmlFile(xaeProjFile);

        // Find and update task.
        String query = fmt("//Task/Name[text()='%s']/..", task.name);
        List<Node> tasks = execXPath(doc, query);
        if (tasks.size() != 1) {
            String msg = fmt("Found %d tasks with name \"%s\" in \"%s\".", tasks.size(), task.name,
                    xaeProjFile.getPath());
            throw new InvalidOptionException(msg);
        }
        Element taskElem = (Element)first(tasks);
        taskElem.setAttribute("Priority", str(task.priority));
        taskElem.setAttribute("CycleTime", str(task.cycleTime * 10000));

        // Write project file.
        writeXmlFile(doc, xaeProjFile);
    }

    /**
     * Update the TwinCAT PLC project (.plcproj) file. Also schedules {@link #oldCodeFiles old code files} for removal.
     */
    private void updatePlcProj() {
        // Read project file.
        Document doc = readXmlFile(plcProjFile);

        // Find compilation item group, or add one.
        List<Node> compileGroups = execXPath(doc, "//ItemGroup/Compile/..");
        Element compileGroup;
        if (compileGroups.isEmpty()) {
            // Add new ItemGroup for 'Compile' entries.
            compileGroup = doc.createElement("ItemGroup");
            doc.getDocumentElement().appendChild(compileGroup);
        } else {
            // Reuse first 'ItemGroup' with a 'Compile' in it.
            compileGroup = (Element)first(compileGroups);
        }

        // Remove all 'Compile' entries for old code files. Also remove the
        // actual files on disk.
        String query = "//ItemGroup/Compile/@Include/..";
        List<Node> compileNodes = execXPath(doc, query);
        for (Node compileNode: compileNodes) {
            // Only remove code files for POUs, DUTs, and GVLs.
            Element compileElem = (Element)compileNode;
            String path = compileElem.getAttribute("Include");
            if (!path.endsWith("TcPOU") && !path.endsWith("TcGVL") && !path.endsWith("TcDUT")) {
                continue;
            }

            // Remove XML element.
            compileNode.getParentNode().removeChild(compileNode);

            // Schedule old code files for removal, if no new code file.
            boolean remove = !files.containsKey(path);
            path = Paths.join(plcProjDirFile.getPath(), path);
            File codeFile = new File(path);
            if (remove && codeFile.exists()) {
                oldCodeFiles.add(codeFile);
            }
        }

        // Find/add compilation instructions for all new code files.
        for (String path: files.keySet()) {
            query = fmt("//ItemGroup/Compile[@Include='%s']", path);
            List<Node> compiles = execXPath(doc, query);
            if (compiles.isEmpty()) {
                // Add new 'Compile' entry.
                Element compileElem = doc.createElement("Compile");
                compileGroup.appendChild(compileElem);
                compileElem.setAttribute("Include", path);

                // Add new 'SubType' entry.
                Element subTypeElem = doc.createElement("SubType");
                compileElem.appendChild(subTypeElem);
                subTypeElem.setTextContent("Code");
            }
        }

        // Ensure code directories are included in the project.
        for (String folder: list("DUTs", "GVLs", "POUs")) {
            query = fmt("//ItemGroup/Folder[@Include='%s']", folder);
            List<Node> tasks = execXPath(doc, query);
            if (tasks.isEmpty()) {
                Element folderElem = doc.createElement("Folder");
                compileGroup.appendChild(folderElem);
                folderElem.setAttribute("Include", folder);
            }
        }

        // Write project file.
        writeXmlFile(doc, plcProjFile);
    }

    /** Update the TwinCAT task (.TcTTO) file. */
    private void updateTask() {
        // Read task file.
        File taskFile = new File(plcProjDirFile, fmt("%s.TcTTO", task.name));
        Document doc = readXmlFile(taskFile);

        // Update cycle time.
        List<Node> cycleNodes = execXPath(doc, "//Task/CycleTime");
        for (Node cycleNode: cycleNodes) {
            cycleNode.setTextContent(str(task.cycleTime * 1000));
        }

        // Update priority.
        List<Node> prioNodes = execXPath(doc, "//Task/Priority");
        for (Node prioNode: prioNodes) {
            prioNode.setTextContent(str(task.priority));
        }

        // Find task.
        Element taskElem = null;
        NodeList nodes = doc.getDocumentElement().getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeName().equals("Task")) {
                taskElem = (Element)node;
                break;
            }
        }
        if (taskElem == null) {
            throw new RuntimeException("Task not found.");
        }

        // Find/add 'PouCall'.
        Element callElem = null;
        nodes = taskElem.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeName().equals("PouCall")) {
                callElem = (Element)node;
                break;
            }
        }

        // Find/add POU calls for all POU instances.
        for (PlcPouInstance pouInst: task.pouInstances) {
            // TwinCAT does not have a different name for a POU and its
            // instance.
            Assert.check(pouInst.name.equals(pouInst.pou.name));

            // Find 'PouCall/Name' for POU instance.
            String query = fmt("//Task/PouCall/Name[text()='%s']", pouInst.name);
            List<Node> callNodes = execXPath(doc, query);

            // Add 'PouCall/Name' if not found.
            if (callNodes.isEmpty()) {
                // Ensure we have a 'PouCall' element.
                if (callElem == null) {
                    callElem = doc.createElement("PouCall");
                    taskElem.appendChild(callElem);
                }

                // Add 'Name' element.
                Element nameElem = doc.createElement("Name");
                callElem.appendChild(nameElem);
                nameElem.setTextContent(pouInst.name);
            }
        }

        // Write task file.
        writeXmlFile(doc, taskFile);
    }

    /**
     * Read a TwinCAT XML file.
     *
     * @param file The XML file. Must represent and absolute local file system path.
     * @return The XML document resulting from parsing the project file.
     */
    private Document readXmlFile(File file) {
        // Paranoia check.
        Assert.check(file.isAbsolute());

        // Create builder.
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }

        // Parse file.
        try {
            return builder.parse(file);
        } catch (SAXException e) {
            String msg = fmt("TwinCAT file \"%s\" could not be read.", file.getPath());
            throw new InvalidOptionException(msg, e);
        } catch (IOException e) {
            String msg = fmt("TwinCAT file \"%s\" could not be read.", file.getPath());
            throw new InputOutputException(msg, e);
        }
    }

    /**
     * Write a TwinCAT file.
     *
     * @param doc The XML document to write to the file.
     * @param file The file to which to write. Must represent and absolute local file system path.
     */
    private void writeXmlFile(Document doc, File file) {
        // Paranoia check.
        Assert.check(file.isAbsolute());

        // Write file.
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = factory.newTransformer();
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        }
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        String indentAmountKey = "{http://xml.apache.org/xslt}indent-amount";
        transformer.setOutputProperty(indentAmountKey, "2");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(file);
        try {
            transformer.transform(source, result);
        } catch (TransformerException e) {
            String msg = fmt("Failed to write TwinCAT file \"%s\".", file.getPath());
            throw new InputOutputException(msg, e);
        }
    }

    /**
     * Executes an XPath query on the given XML node.
     *
     * @param node The start node of the query.
     * @param query The query in XPath syntax.
     * @return The nodes resulting from the query.
     */
    private List<Node> execXPath(Node node, String query) {
        // Create XPath expression.
        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath xpath = xPathfactory.newXPath();
        XPathExpression expr;
        try {
            expr = xpath.compile(query);
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }

        // Evaluate expression.
        NodeList nodes;
        try {
            nodes = (NodeList)expr.evaluate(node, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }

        // Return as a list of nodes.
        List<Node> rslt = listc(nodes.getLength());
        for (int i = 0; i < nodes.getLength(); i++) {
            rslt.add(nodes.item(i));
        }
        return rslt;
    }

    /** Generate code files. */
    private void genCodeFiles() {
        for (PlcPou pou: project.pous) {
            genCodeFile(pou);
        }
        for (PlcTypeDecl tdecl: project.typeDecls) {
            genCodeFile(tdecl);
        }
        for (PlcGlobalVarList varList: configuration.globalVarLists) {
            genCodeFile(varList);
        }
        for (PlcGlobalVarList varList: resource.globalVarLists) {
            genCodeFile(varList);
        }
    }

    /**
     * Generates code file for a PLC POU.
     *
     * @param pou The PLC POU.
     */
    private void genCodeFile(PlcPou pou) {
        // Generate XML document.
        Document doc = createXmlDoc();

        Element rootElem = doc.createElement("TcPlcObject");
        doc.appendChild(rootElem);
        rootElem.setAttribute("Version", "1.1.0.1");
        rootElem.setAttribute("ProductVersion", "3.1.0.18");

        Element pouElem = doc.createElement("POU");
        rootElem.appendChild(pouElem);
        pouElem.setAttribute("Name", pou.name);

        Element declElem = doc.createElement("Declaration");
        pouElem.appendChild(declElem);

        String headerTxt = pou.headerToBox().toString();
        declElem.appendChild(doc.createCDATASection(headerTxt));

        Element implElem = doc.createElement("Implementation");
        pouElem.appendChild(implElem);

        Element stElem = doc.createElement("ST");
        implElem.appendChild(stElem);

        stElem.appendChild(doc.createCDATASection(pou.body.toString()));

        Element opElem = doc.createElement("ObjectProperties");
        pouElem.appendChild(opElem);

        // Store new file.
        String fileName = fmt("POUs\\%s.TcPOU", pou.name);
        Document prevDoc = files.put(fileName, doc);
        Assert.check(prevDoc == null);
    }

    /**
     * Generates code file for a PLC type declaration.
     *
     * @param typeDecl The PLC type declaration.
     */
    private void genCodeFile(PlcTypeDecl typeDecl) {
        // Generate XML document.
        Document doc = createXmlDoc();

        Element rootElem = doc.createElement("TcPlcObject");
        doc.appendChild(rootElem);
        rootElem.setAttribute("Version", "1.1.0.1");
        rootElem.setAttribute("ProductVersion", "3.1.0.18");

        Element pouElem = doc.createElement("DUT");
        rootElem.appendChild(pouElem);
        pouElem.setAttribute("Name", typeDecl.name);

        Element declElem = doc.createElement("Declaration");
        pouElem.appendChild(declElem);

        String txt = typeDecl.toStringTwinCat();
        declElem.appendChild(doc.createCDATASection(txt));

        Element opElem = doc.createElement("ObjectProperties");
        pouElem.appendChild(opElem);

        // Store new file.
        String fileName = fmt("DUTs\\%s.TcDUT", typeDecl.name);
        Document prevDoc = files.put(fileName, doc);
        Assert.check(prevDoc == null);
    }

    /**
     * Generates code file for a PLC global variable list.
     *
     * @param varList The PLC global variable list.
     */
    private void genCodeFile(PlcGlobalVarList varList) {
        // Skip empty variable lists.
        if (varList.variables.isEmpty()) {
            return;
        }

        // Generate XML document.
        Document doc = createXmlDoc();

        Element rootElem = doc.createElement("TcPlcObject");
        doc.appendChild(rootElem);
        rootElem.setAttribute("Version", "1.1.0.1");
        rootElem.setAttribute("ProductVersion", "3.1.0.18");

        Element pouElem = doc.createElement("GVL");
        rootElem.appendChild(pouElem);
        pouElem.setAttribute("Name", varList.name);

        Element declElem = doc.createElement("Declaration");
        pouElem.appendChild(declElem);

        declElem.appendChild(doc.createCDATASection(varList.toString()));

        Element opElem = doc.createElement("ObjectProperties");
        pouElem.appendChild(opElem);

        // Store new file.
        String fileName = fmt("GVLs\\%s.TcGVL", varList.name);
        Document prevDoc = files.put(fileName, doc);
        Assert.check(prevDoc == null);
    }

    /** Removes old code files, and (over)writes the new code files. */
    private void updateCodeFiles() {
        // Remove old code files.
        for (File oldFile: oldCodeFiles) {
            boolean success = oldFile.delete();
            if (!success) {
                String msg = fmt("Could not remove TwinCAT code file \"%s\".", oldFile.getPath());
                throw new InputOutputException(msg);
            }
        }

        // (Over)write new code files.
        for (Entry<String, Document> entry: files.entrySet()) {
            String path = Paths.join(plcProjDirFile.getPath(), entry.getKey());
            File entryFile = new File(path);
            writeXmlFile(entry.getValue(), entryFile);
        }
    }

    /**
     * Creates and returns a fresh new XML document.
     *
     * @return The XML document.
     */
    private Document createXmlDoc() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        return builder.newDocument();
    }
}
