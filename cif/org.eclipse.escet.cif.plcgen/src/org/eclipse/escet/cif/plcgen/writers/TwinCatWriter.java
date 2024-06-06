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

import org.eclipse.escet.cif.plcgen.model.declarations.PlcConfiguration;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcDeclaredType;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcGlobalVarList;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcPou;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcPouInstance;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcProject;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcResource;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcTask;
import org.eclipse.escet.cif.plcgen.model.types.PlcEnumType;
import org.eclipse.escet.cif.plcgen.model.types.PlcStructField;
import org.eclipse.escet.cif.plcgen.model.types.PlcStructType;
import org.eclipse.escet.cif.plcgen.targets.PlcTarget;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.box.Box;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.box.MemoryCodeBox;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.exceptions.InputOutputException;
import org.eclipse.escet.common.java.exceptions.InvalidOptionException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/** TwinCAT 3.1 writer. */
public class TwinCatWriter extends Writer {
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

    /**
     * Constructor of the {@link AbbWriter} class.
     *
     * @param target PLC target to generate code for.
     */
    public TwinCatWriter(PlcTarget target) {
        super(target);
    }

    /**
     * {@inheritDoc}
     *
     * @note Must point to a directory containing an already generated TwinCAT solution.
     */
    @Override
    public void write(PlcProject project, String slnDirPath) {
        slnDirPath = Paths.resolve(slnDirPath); // Switch to platform-specific file separators.

        this.project = project;

        Assert.check(project.configurations.size() == 1);
        configuration = first(project.configurations);

        Assert.check(configuration.resources.size() == 1);
        resource = first(configuration.resources);

        Assert.check(resource.tasks.size() == 1);
        task = first(resource.tasks);

        if (task.cycleTime == 0) {
            String msg = "TwinCAT output with periodic task scheduling disabled, is currently not supported.";
            throw new InvalidOptionException(msg);
        }

        findTwinCatProjects(slnDirPath);

        // POU instances in the resource are not supported.
        Assert.check(resource.pouInstances.isEmpty());

        // Update TwinCAT XAE project.
        updateXaeProj();
        genCodeFiles();
        updatePlcProj();
        updateTask();
        updateCodeFiles();
    }

    /**
     * Finds the projects within the TwinCAT solution.
     *
     * @param slnDirPath The absolute local file system path of the directory containing the TwinCAT solution, with
     *     platform specific file separators.
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
                Element compileElem = makeChild(compileGroup, "Compile");
                compileElem.setAttribute("Include", path);

                // Add new 'SubType' entry.
                Element subTypeElem = makeChild(compileElem, "SubType");
                subTypeElem.setTextContent("Code");
            }
        }

        // Ensure code directories are included in the project.
        for (String folder: list("DUTs", "GVLs", "POUs")) {
            query = fmt("//ItemGroup/Folder[@Include='%s']", folder);
            List<Node> tasks = execXPath(doc, query);
            if (tasks.isEmpty()) {
                Element folderElem = makeChild(compileGroup, "Folder");
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
                    callElem = makeChild(taskElem, "PouCall");
                }

                // Add 'Name' element.
                Element nameElem = makeChild(callElem, "Name");
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
        for (PlcDeclaredType declType: project.declaredTypes) {
            genCodeFile(declType);
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
        Element rootElem = makeRoot(doc, "TcPlcObject");
        rootElem.setAttribute("Version", "1.1.0.1");
        rootElem.setAttribute("ProductVersion", "3.1.0.18");

        Element pouElem = makeChild(rootElem, "POU");
        pouElem.setAttribute("Name", pou.name);

        Element declElem = makeChild(pouElem, "Declaration");
        String headerTxt = headerToBox(pou).toString();
        declElem.appendChild(doc.createCDATASection(headerTxt));

        Element implElem = makeChild(pouElem, "Implementation");
        Element stElem = makeChild(implElem, "ST");
        stElem.appendChild(doc.createCDATASection(pou.body.toString()));

        makeChild(pouElem, "ObjectProperties");

        // Store new file.
        String fileName = fmt("POUs\\%s.TcPOU", pou.name);
        Document prevDoc = files.put(fileName, doc);
        Assert.check(prevDoc == null);
    }

    /**
     * Generates code file for a PLC declared type.
     *
     * @param declaredType The PLC declared type.
     */
    private void genCodeFile(PlcDeclaredType declaredType) {
        String typeName;
        String declarationText;
        if (declaredType instanceof PlcStructType structType) {
            typeName = structType.typeName;
            declarationText = toTypeDeclBox(structType).toString();
        } else if (declaredType instanceof PlcEnumType enumType) {
            typeName = enumType.typeName;
            declarationText = toTypeDeclBox(enumType).toString();
        } else {
            throw new AssertionError("Unexpected declared type found: \"" + declaredType + "\".");
        }

        // Generate XML document.
        Document doc = createXmlDoc();
        Element rootElem = makeRoot(doc, "TcPlcObject");
        rootElem.setAttribute("Version", "1.1.0.1");
        rootElem.setAttribute("ProductVersion", "3.1.0.18");

        Element pouElem = makeChild(rootElem, "DUT");
        pouElem.setAttribute("Name", typeName);

        Element declElem = makeChild(pouElem, "Declaration");
        declElem.appendChild(doc.createCDATASection(declarationText));

        makeChild(pouElem, "ObjectProperties");

        // Store new file.
        String fileName = fmt("DUTs\\%s.TcDUT", typeName);
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

        Element pouElem = makeChild(rootElem, "GVL");
        pouElem.setAttribute("Name", varList.name);

        Element declElem = makeChild(pouElem, "Declaration");
        declElem.appendChild(doc.createCDATASection(toVarDeclBox(varList).toString()));

        makeChild(pouElem, "ObjectProperties");

        // Store new file.
        String fileName = fmt("GVLs\\%s.TcGVL", varList.name);
        Document prevDoc = files.put(fileName, doc);
        Assert.check(prevDoc == null, fmt("Duplicate generated file \"%s\".", fileName));
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
     * Construct a root element in the document with the given name, and return the root element.
     *
     * @param doc Document to give a new root element.
     * @param rootName Name of the root element that should be created.
     * @return The created root element.
     */
    private Element makeRoot(Document doc, String rootName) {
        Element rootElem = doc.createElement(rootName);
        doc.appendChild(rootElem);
        return rootElem;
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

    @Override
    protected Box toTypeDeclBox(PlcStructType structType) {
        // Converts the declared type to a textual representation in IEC 61131-3 syntax. The output is TwinCAT
        // specific, in that it implements a workaround for a bug in TwinCAT, where structs in type declarations
        // may not be terminated with a semicolon.
        CodeBox c = new MemoryCodeBox(INDENT);
        c.add("TYPE %s:", structType.typeName);
        c.indent();
        c.add("STRUCT");
        c.indent();
        for (PlcStructField field: structType.fields) {
            c.add(toTypeDeclBox(field));
        }
        c.dedent();
        c.add("END_STRUCT");
        c.dedent();
        c.add("END_TYPE");
        return c;
    }
}
