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

package org.eclipse.escet.cif.codegen.javascript;

import static org.eclipse.escet.common.java.Sets.set;

import java.util.Set;

/** JavaScript code utility methods. */
public class JavaScriptCodeUtils {
    /** Constructor for the {@link JavaScriptCodeUtils} class. */
    private JavaScriptCodeUtils() {
        // Static class.
    }

    /**
     * JavaScript language keywords, reserved identifiers and identifiers with special meaning.
     *
     * @see <a href="https://www.w3schools.com/js/js_reserved.asp">Keywords, JavaScript Language Specification</a>
     */
    public static final Set<String> JAVASCRIPT_IDS = set(
            // Reserved keywords.

            // In JavaScript we cannot use these reserved words as variables, labels, or function names:
            "abstract", "arguments", "await", // a
            "boolean", "break", "byte", // b
            "case", "catch", "char", "class", "const", "continue", // c
            "debugger", "default", "delete", "do", "double", // d
            "else", "enum", "eval", "export", "extends", // e
            "false", "final", "finally", "float", "for", "function", // f
            "goto", // g
            "if", "implements", "import", "in", "instanceof", "int", "interface", // i
            "let", "long", // l
            "native", "new", "null", // n
            "package", "private", "protected", "public", // p
            "return", // r
            "short", "static", "super", "switch", "synchronized", // s
            "this", "throw", "throws", "transient", "true", "try", "typeof", // t
            "var", "void", "volatile", // v
            "while", "with", // w
            "yield", // y

            // We also avoid using the name of JavaScript built-in objects, properties, and methods:
            "Array", // a
            "Date", // d
            "hasOwnProperty", // h
            "Infinity", "isFinite", "isNaN", "isPrototypeOf", // i
            "length", // l
            "Math", // m
            "NaN", "name", "Number", // n
            "Object", // o
            "prototype", // p
            "String", // s
            "toString", // t
            "undefined", // u
            "valueOf", // v

            // JavaScript is often used together with Java. We avoid using some Java objects and properties
            // as JavaScript identifiers:
            "getClass", // g
            "java", "JavaArray", "javaClass", "JavaObject", "JavaPackage", // j

            // JavaScript can be used as the programming language in many applications.
            // We also avoid using the name of HTML and Window objects and properties:
            "alert", "all", "anchor", "anchors", "area", "assign", // a
            "blur", "button", // b
            "checkbox", "clearInterval", "clearTimeout", "clientInformation", "close", "closed", "confirm", // c
            "constructor", "crypto", // c
            "decodeURI", "decodeURIComponent", "defaultStatus", "document", // d
            "element", "elements", "embed", "embeds", "encodeURI", "encodeURIComponent", "escape", "event", // e
            "fileUpload", "focus", "form", "forms", "frame", "frames", "frameRate", // f
            "hidden", "history", // h
            "image", "images", "innerHeight", "innerWidth", // i
            "layer", "layers", "link", "location", // l
            "mimeTypes", // m
            "navigate", "navigator", // n
            "offscreenBuffering", "open", "opener", "option", "outerHeight", "outerWidth", // o
            "packages", "pageXOffset", "pageYOffset", "parent", "parseFloat", "parseInt", "password", "pkcs11", // p
            "plugin", "prompt", "propertyIsEnum", // p
            "radio", "reset", // r
            "screenX", "screenY", "scroll", "secure", "select", "self", "setInterval", "setTimeout", "status", // s
            "submit", // s
            "taint", "text", "textarea", "top", // t
            "unescape", "untaint", // u
            "window", // w

            // In addition, we avoid using the names of all HTML event handlers:
            "onblur", "onclick", "onerror", "onfocus", "onkeydown", "onkeypress", "onkeyup", "onmouseover", // o
            "onload", "onmouseup", "onmousedown", "onsubmit" // o
    );

    /**
     * Returns a JavaScript compatible name for the given {@code name}, that does not conflict with JavaScript's
     * reserved identifiers. Conflicting identifiers are prefixed with an underscore character ({@code _}).
     *
     * @param name The name to make JavaScript compatible.
     * @return The JavaScript compatible name.
     * @see #JAVASCRIPT_IDS
     */
    public static String makeJavaScriptName(String name) {
        if (!JavaScriptCodeUtils.JAVASCRIPT_IDS.contains(name)) {
            return name;
        }
        return "_" + name;
    }
}
