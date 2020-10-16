//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.svg;

import static org.eclipse.escet.common.java.Pair.pair;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.anim.dom.SVGOMDocument;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.XMLResourceDescriptor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.PlatformUriUtils;
import org.eclipse.escet.common.app.framework.exceptions.InvalidInputException;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Pair;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/** Scalable Vector Graphics (SVG) related utility methods. */
public class SvgUtils {
    /** Constructor for the {@link SvgUtils} class. */
    private SvgUtils() {
        // Static class.
    }

    /**
     * Loads an SVG document: the XML document of the SVG image. The exceptions thrown by this method are application
     * framework exceptions.
     *
     * @param svgAbsPath The absolute path to the SVG file. May be an absolute local file system path with platform
     *     specific path separators, or an Eclipse platform URI.
     * @return The SVG document.
     * @throws SvgException If loading the SVG image file fails.
     */
    public static Document loadSvgFile(String svgAbsPath) {
        try {
            return loadSvgFileInternal(svgAbsPath);
        } catch (FileNotFoundException ex) {
            String msg = fmt("SVG image file \"%s\" could not be found, is a directory, or for some other reason "
                    + "could not be opened for reading.", svgAbsPath);
            throw new SvgException(msg, ex);
        } catch (DOMException ex) {
            String msg = fmt("SVG image file \"%s\" is not an SVG file, is an invalid SVG file, or contains "
                    + "unsupported SVG features.", svgAbsPath);
            throw new SvgException(msg, ex);
        } catch (IOException ex) {
            String msg = fmt("SVG image file \"%s\" could not be read, is empty, is incomplete, is not an SVG file, "
                    + "or is an invalid SVG file.", svgAbsPath);
            throw new SvgException(msg, ex);
        }
    }

    /**
     * Loads an SVG document: the XML document of the SVG image. The exceptions thrown by this method are internal
     * exceptions.
     *
     * @param svgAbsPath The absolute path to the SVG file. May be an absolute local file system path with platform
     *     specific path separators, or an Eclipse platform URI.
     * @return The SVG document.
     * @throws FileNotFoundException If the SVG image file could not be found, is a directory, or for some other reason
     *     could not be opened for reading. Note that {@link FileNotFoundException} is a derived class of
     *     {@link IOException}.
     * @throws DOMException If the SVG image file is not an SVG file, is an invalid SVG file, or contains unsupported
     *     SVG features.
     * @throws IOException If the SVG image file could not be read, is empty, is incomplete, is not an SVG file, or is
     *     an invalid SVG file.
     */
    public static Document loadSvgFileInternal(String svgAbsPath) throws IOException {
        // Open the SVG file for reading.
        InputStream svgStream;
        if (PlatformUriUtils.isPlatformUri(svgAbsPath)) {
            // Platform resource. Get URI.
            URI uri;
            try {
                uri = URI.createURI(svgAbsPath);
            } catch (IllegalArgumentException ex) {
                String msg = fmt("Platform URI \"%s\" is invalid.", svgAbsPath);
                throw new IOException(msg, ex);
            }

            // Make sure URI exists.
            if (!PlatformUriUtils.exists(svgAbsPath)) {
                String msg = fmt("Resource \"%s\" not found.", svgAbsPath);
                throw new FileNotFoundException(msg);
            }

            // Get input stream.
            URIConverter converter = URIConverter.INSTANCE;
            svgStream = converter.createInputStream(uri);
        } else {
            svgStream = new FileInputStream(svgAbsPath);
        }

        // Get SVG URI.
        String uri;
        if (PlatformUriUtils.isPlatformUri(svgAbsPath)) {
            uri = svgAbsPath;
        } else {
            uri = Paths.createJavaURI(svgAbsPath).toString();
        }

        // Load the SVG document from the SVG file.
        Document document = null;
        try {
            String parser = XMLResourceDescriptor.getXMLParserClassName();
            SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(parser);
            document = factory.createDocument(uri, svgStream);
            return document;
        } finally {
            // We are returning the document, or an exception occurred. Either
            // way, always close the file.
            try {
                svgStream.close();
            } catch (IOException ex) {
                // Closing the file failed. If we have a document, then we
                // were returning it, and we now have an I/O failure. If we
                // don't have a document, we already had an other exception,
                // and we keep throwing that one (ignoring the close failure).
                if (document != null) {
                    throw ex;
                }
            }
        }
    }

    /**
     * Is the attribute with the given name a CSS style attribute?
     *
     * @param elem The SVG element.
     * @param attrName The attribute name.
     * @return {@code true} if the attribute with the given name a CSS style attribute, {@code false} otherwise (SVG
     *     presentation attribute, unknown attribute, etc).
     */
    public static boolean isCssAttr(Element elem, String attrName) {
        // Check whether CSS engine knows about the attribute.
        Document doc = elem.getOwnerDocument();
        SVGOMDocument svgDoc = (SVGOMDocument)doc;
        CSSEngine cssEngine = svgDoc.getCSSEngine();
        int idx = cssEngine.getPropertyIndex(attrName);
        return idx != -1;
    }

    /**
     * Returns the text node child of the given element. If the element does not have a text node child, but does have a
     * single child element, that element is checked recursively. If at some point an element has no text node child,
     * and not exactly one child element, the search for the text node fails.
     *
     * @param elem The element for which to return the text node child.
     * @return The text node child that was found, or {@code null} if no text node child could be found.
     */
    public static Text getTextNode(Element elem) {
        Element curElem = elem;
        while (true) {
            // Try to find text child node, or otherwise a child element.
            Element child = null;
            int subElemCount = 0;

            @SuppressWarnings("null")
            NodeList children = curElem.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                if (children.item(i) instanceof Text) {
                    return (Text)children.item(i);
                } else if (children.item(i) instanceof Element) {
                    subElemCount++;
                    child = (Element)children.item(i);
                }
            }

            // We did not find a text node. If the current element has exactly
            // one child (such as a 'tspan'), continue with that child element.
            if (subElemCount == 1) {
                Assert.notNull(child);
                curElem = child;
                continue;
            }

            // We did not find a text node, or a single child element.
            return null;
        }
    }

    /**
     * Returns the id of the given SVG element.
     *
     * @param elem The SVG element.
     * @return The id of the given SVG element, or {@code null} if the SVG element has no id.
     */
    public static String getSvgElementId(Element elem) {
        NamedNodeMap attrs = elem.getAttributes();
        for (int i = 0; i < attrs.getLength(); i++) {
            Attr attr = (Attr)attrs.item(i);
            if (attr.isId()) {
                return attr.getValue();
            }
        }
        return null;
    }

    /**
     * Rename all elements in the given (sub-)tree, by adding a prefix and postfix to their ids. Elements without an
     * 'id' are not modified.
     *
     * @param root The root element of the (sub-)tree. Is modified in-place.
     * @param prefix The prefix to prepend to the ids.
     * @param postfix The postfix to append to the ids.
     * @param doc The document in which to look for duplicates of the new ids. If {@code null}, duplicate id checking is
     *     disabled.
     * @return The old and new SVG element ids of the element for which the new id is a duplicate, or {@code null} if no
     *     duplicate ids were found.
     */
    public static Pair<String, String> renameElements(Element root, String prefix, String postfix, Document doc) {
        // Change the 'id' if the element has one.
        NamedNodeMap attrs = root.getAttributes();
        for (int i = 0; i < attrs.getLength(); i++) {
            Attr attr = (Attr)attrs.item(i);
            if (attr.isId()) {
                String oldId = attr.getValue();
                String newId = prefix + oldId + postfix;
                if (doc != null && doc.getElementById(newId) != null) {
                    return pair(oldId, newId);
                }

                attr.setValue(newId);
                break;
            }
        }

        // Recursively rename the child nodes.
        NodeList children = root.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                Element childElem = (Element)child;
                Pair<String, String> rslt;
                rslt = renameElements(childElem, prefix, postfix, doc);
                if (rslt != null) {
                    return rslt;
                }
            }
        }

        // No duplicates found.
        return null;
    }

    /**
     * Updates the given SVG document, by copying a (sub-)tree. The copied (sub-tree) is added as sibling of the
     * original (sub-)tree, immediately before the original (to give it a similar z-order). Unique ids are created for
     * all elements of the copy. The new element ids are created by pre- and postfixing the original ids.
     *
     * @param doc The SVG document to which to apply the copy operation. Is modified in-place.
     * @param id The element id of the root element of the (sub-)tree to copy.
     * @param pre The prefix to prepend to the ids of the elements of the copy of the (sub-)tree.
     * @param post The postfix to append to the ids of the elements of the copy of the (sub-)tree.
     * @param svgPath The absolute or relative local file system path to the SVG file for which we are applying the
     *     operation. Used for error messages.
     * @return Whether the copy was successful. If it returns, always {@code true}.
     * @throws InvalidInputException If the given {@code prefix} and {@code postfix} are both empty.
     * @throws InvalidInputException If the given {@code prefix} is not a valid SVG name prefix.
     * @throws InvalidInputException If the given {@code postfix} is not a valid SVG name postfix.
     * @throws InvalidInputException If an SVG element with the given {@code id} does not exist in the SVG
     *     {@code document}.
     * @throws InvalidInputException If the copied element is the root element of the SVG {@code document}.
     * @throws InvalidInputException If a new element id of one of the copied elements conflicts with an element id of
     *     one of the existing elements in the {@code document}.
     */
    public static boolean copy(Document doc, String id, String pre, String post, String svgPath) {
        return copy(doc, id, pre, post, svgPath, true);
    }

    /**
     * Updates the given SVG document, by copying a (sub-)tree. The copied (sub-tree) is added as sibling of the
     * original (sub-)tree, immediately before the original (to give it a similar z-order). Unique ids are created for
     * all elements of the copy. The new element ids are created by pre- and postfixing the original ids.
     *
     * @param doc The SVG document to which to apply the copy operation. Is modified in-place.
     * @param id The element id of the root element of the (sub-)tree to copy.
     * @param pre The prefix to prepend to the ids of the elements of the copy of the (sub-)tree.
     * @param post The postfix to append to the ids of the elements of the copy of the (sub-)tree.
     * @param svgPath The absolute or relative local file system path to the SVG file for which we are applying the
     *     operation. Used for error messages.
     * @param errNotExist Whether a non-existing (missing) element is considered an error to be reported to the type
     *     checker.
     * @return Whether the copy was successful ({@code true}), or it failed due to the element to copy not being present
     *     ({@code false}, only in case {@code errNotExist} is {@code false}).
     * @throws InvalidInputException If the given {@code prefix} and {@code postfix} are both empty.
     * @throws InvalidInputException If the given {@code prefix} is not a valid SVG name prefix.
     * @throws InvalidInputException If the given {@code postfix} is not a valid SVG name postfix.
     * @throws InvalidInputException If an SVG element with the given {@code id} does not exist in the SVG
     *     {@code document}, and also {@code errNotExist} is {@code true}.
     * @throws InvalidInputException If the copied element is the root element of the SVG {@code document}.
     * @throws InvalidInputException If a new element id of one of the copied elements conflicts with an element id of
     *     one of the existing elements in the {@code document}.
     */
    public static boolean copy(Document doc, String id, String pre, String post, String svgPath, boolean errNotExist) {
        // Get SVG element.
        Element elem = doc.getElementById(id);
        if (elem == null) {
            // Report back missing element.
            if (!errNotExist) {
                return false;
            }

            // Failure due to missing element.
            String msg = fmt("Could not find an SVG element with id \"%s\" in SVG file \"%s\".", id, svgPath);
            throw new InvalidInputException(msg);
        }

        // Need a prefix, a postfix, or both.
        if (pre.isEmpty() && post.isEmpty()) {
            String msg = fmt("Copying the SVG element with id \"%s\" in SVG file \"%s\" failed, as both the prefix "
                    + "and postfix are empty.", id, svgPath, pre, post);
            throw new InvalidInputException(msg);
        }

        // Check for valid prefix/postfix.
        if (!SvgNameUtils.isValidSvgPrefixName(pre)) {
            String msg = fmt("Copying the SVG element with id \"%s\" in SVG file \"%s\" failed, as prefix \"%s\" is "
                    + "not a valid SVG name prefix.", id, svgPath, pre);
            throw new InvalidInputException(msg);
        }
        if (!SvgNameUtils.isValidSvgPostfixName(post)) {
            String msg = fmt("Copying the SVG element with id \"%s\" in SVG file \"%s\" failed, as postfix \"%s\" is "
                    + "not a valid SVG name postfix.", id, svgPath, pre);
            throw new InvalidInputException(msg);
        }

        // Check for non-root element.
        Node parent = elem.getParentNode();
        if (parent.getNodeType() != Node.ELEMENT_NODE) {
            String msg = fmt("Copying the SVG element with id \"%s\" in SVG file \"%s\" is not supported, as the "
                    + "element is the root element of the SVG file.", id, svgPath);
            throw new InvalidInputException(msg);
        }

        // Copy the sub-tree.
        Element copy = (Element)elem.cloneNode(true);

        // Rename the elements of the copied sub-tree. Check for duplicate ids.
        Pair<String, String> duplIds;
        duplIds = SvgUtils.renameElements(copy, pre, post, doc);
        if (duplIds != null) {
            String msg = fmt(
                    "Copying the SVG element with id \"%s\" in SVG file \"%s\" resulted in a copy of the SVG "
                            + "element with id \"%s\" to id \"%s\", which already exists.",
                    id, svgPath, duplIds.left, duplIds.right);
            throw new InvalidInputException(msg);
        }

        // Add the copied sub-tree as sibling of the original, immediately
        // before the original, to give it a similar z-order.
        Element parentElement = (Element)parent;
        parentElement.insertBefore(copy, elem);

        // Element to copy exists.
        return true;
    }

    /**
     * Updates the given SVG document, by moving an SVG element.
     *
     * <p>
     * This method adds a translation to the elements 'transform' attribute. Repeated application of this method on the
     * same element thus leads to more and more translations in the same 'transform' attribute, and can lead to
     * performance degradation.
     * </p>
     *
     * @param doc The SVG document to which to apply the move operation. Is modified in-place.
     * @param bridgeContext The SVG bridge context to use.
     * @param id The element id of the element to move.
     * @param x The target x coordinate of upper left corner of the bounding rectangle of the graphical representation
     *     of the element, relative to the upper left corner of the canvas.
     * @param y The target y coordinate of upper left corner of the bounding rectangle of the graphical representation
     *     of the element, relative to the upper left corner of the canvas.
     * @param svgPath The absolute or relative local file system path to the SVG file for which we are applying the
     *     operation. Used for error messages.
     * @throws InvalidInputException If an SVG element with the given {@code id} does not exist in the SVG
     *     {@code document}.
     * @throws InvalidInputException If the SVG element with the given {@code id} does not have a graphical
     *     representation.
     * @throws InvalidInputException If the SVG element can not be moved, due to conflicting existing transformations on
     *     the element itself, or one of its ancestors.
     */
    public static void move(Document doc, BridgeContext bridgeContext, String id, double x, double y, String svgPath) {
        // Get SVG element.
        Element elem = doc.getElementById(id);
        if (elem == null) {
            String msg = fmt("Could not find an SVG element with id \"%s\" in SVG file \"%s\".", svgPath);
            throw new InvalidInputException(msg);
        }

        // Get graphics node.
        GraphicsNode node = bridgeContext.getGraphicsNode(elem);
        if (node == null) {
            String msg = fmt("Moving the SVG element with id \"%s\" in SVG file \"%s\" failed, as the element does "
                    + "not have a graphical representation.", id, svgPath);
            throw new InvalidInputException(msg);
        }

        // Get current position (nx, ny) of the graphics node. That is, get
        // the coordinates of the upper left corner of the graphical
        // representation of the element, relative to the upper left corner of
        // the canvas. The following computation is used:
        //
        // [ localX ] [ a c e ] [ nx ]
        // [ localY ] * [ b d f ] = [ ny ]
        // [ 1 ] [ 0 0 1 ] [ 1 ]
        Rectangle2D rect = node.getBounds();
        Point2D.Double nxy = new Point2D.Double(rect.getX(), rect.getY());
        AffineTransform glob = node.getGlobalTransform();
        glob.transform(nxy, nxy);
        double nx = nxy.x;
        double ny = nxy.y;

        // To get the graphics node at the desired position (x, y), an
        // additional translation (dx, dy) is applied. This results in:
        //
        // nx + a * dx + c * dy = x [1]
        // ny + b * dx + d * dy = y [2]
        //
        // dx = (x - nx)/a - c/a * dy [3, from 1, a != 0]
        //
        // b * ((x - nx)/a - c/a * dy) + d * dy = y - ny [4, from 2, apply 3]
        // b * (x - nx)/a + (d - b*c/a) * dy = y - ny
        // (d - b*c/a) * dy = y - ny - b * (x - nx) / a
        // dy = (y - ny - b * (x - nx) / a) / (d - b*c/a) [d - b*c/a != 0]
        //
        // First, we compute a, b, c, and d.
        double[] abcd = new double[4];
        glob.getMatrix(abcd);
        double a = abcd[0];
        double b = abcd[1];
        double c = abcd[2];
        double d = abcd[3];

        // Check the conditions 'a != 0' and 'd - b*c/a != 0'.
        if (a == 0) {
            String msg = fmt("Moving the SVG element with id \"%s\" in SVG file \"%s\" failed, due to conflicting "
                    + "existing transformations on the element itself, or one of its ancestors.", id, svgPath);
            throw new InvalidInputException(msg);
        }

        double dbca = d - b * c / a;
        if (dbca == 0) {
            String msg = fmt("Moving the SVG element with id \"%s\" in SVG file \"%s\" failed, due to conflicting "
                    + "existing transformations on the element itself, or one of its ancestors.", id, svgPath);
            throw new InvalidInputException(msg);
        }

        // Compute relative translation (dx, dy) to add to the element.
        double dy = (y - ny - b * (x - nx) / a) / dbca;
        double dx = (x - nx) / a - c / a * dy;

        // Update the 'transform' attribute of the element.
        String value = elem.getAttribute("transform");
        if (!value.isEmpty()) {
            value += " ";
        }
        value += fmt("translate(%f,%f)", dx, dy);
        elem.setAttribute("transform", value);
    }
}
