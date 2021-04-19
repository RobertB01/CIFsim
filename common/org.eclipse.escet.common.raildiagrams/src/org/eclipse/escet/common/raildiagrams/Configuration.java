//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.raildiagrams;

import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.escet.common.app.framework.exceptions.InputOutputException;
import org.eclipse.escet.common.raildiagrams.FontData.FontStyle;
import org.eclipse.escet.common.java.Assert;

/** Configuration data of the diagrams. */
public class Configuration {
    /** Compiled pattern to match RGB values. */
    private static final Pattern RGB_PATTERN = Pattern.compile("\\s*(\\d+)\\s+(\\d+)\\s+(\\d+)\\s*");

    /** Default properties of the diagram generator. */
    private static final String PROPERTIES_LITERAL = "" +
    // Background.
            "diagram.background.color: 255 255 255\n" +

            // Rail.
            "rail.linewidth: 1.0\n" + "rail.color:     0 0 0\n" +

            // Diagram rule.
            "rule.padding.top:          10\n" + "rule.padding.left:         10\n" + "rule.padding.bottom:       10\n"
            + "rule.padding.right:        10\n" + "rule.diagram.padding.top:  10\n" + "rule.diagram.padding.left: 10\n"
            + "rule.diagram.lead.width:   20\n" + "rule.diagram.trail.width:  20\n" +

            // Choice.
            "choice.arc-radius:       10\n" + "choice.padding.vertical:  5\n" +

            // Loop.
            "loop.arc-radius:       10\n" + "loop.padding.vertical:  5\n" + // Padding between forward and backward
                                                                            // sequence.
            "loop.padding.left:      5\n" + // Padding between the left-side and the left loop vertical line.
            "loop.padding.right:     5\n" + // Padding between the right-side and the right loop vertical line.

            // Sequence.
            "sequence.padding.first-row.prefix:  0\n" + "sequence.padding.other-row.prefix:  5\n"
            + "sequence.padding.row.suffix:        5\n" + // Padding behind a row, between the vertical lines down and
                                                          // up.
            "sequence.padding.interrow:          8\n" + // Excludes width of horizontal line.
            "sequence.arc-radius:               10\n" +

            // Branch label.
            "branch-label.padding.left:    5\n" + "branch-label.padding.right:   5\n"
            + "branch-label.padding.top:     5\n" + "branch-label.padding.bottom:  5\n"
            + "branch-label.min-width:       5\n" + "branch-label.text.color:      0 0 0\n"
            + "branch-label.text.font:       Serif\n" + "branch-label.text.font.size:  12\n"
            + "branch-label.text.font.style: plain\n" +

            // Empty node.
            "empty.width: 10\n" +

            // Name node, width of the entry and exit connections.
            "name.rail.entry.width: 5\n" + "name.rail.exit.width:  5\n" +

            // Header font and text.
            "diagram-header.text.color:      0 0 0\n" + "diagram-header.text.font:       Serif\n"
            + "diagram-header.text.font.size:  15\n" + "diagram-header.text.font.style: bold\n" +

            // 'terminal' name properties.
            "terminal.name.padding.horizontal: 5\n" + // Horizontal padding around the name.
            "terminal.name.padding.vertical:   5\n" + // Vertical padding around the name.
            "terminal.corner.radius: 12\n" + "terminal.box.color:     0 0 0\n" + "terminal.box.linewidth: 1.0\n"
            + "terminal.text.color:    0 0 0\n" + "terminal.text.font:       Monospaced\n"
            + "terminal.text.font.size:  12\n" + "terminal.text.font.style: plain\n" +

            // 'meta-terminal' name properties.
            "meta-terminal.name.padding.horizontal: 5\n" + "meta-terminal.name.padding.vertical:   5\n"
            + "meta-terminal.corner.radius: 12\n" + "meta-terminal.box.color:     0 0 0\n"
            + "meta-terminal.box.linewidth: 1.0\n" + "meta-terminal.text.color:    0 0 0\n"
            + "meta-terminal.text.font:       Serif\n" + "meta-terminal.text.font.size:  12\n"
            + "meta-terminal.text.font.style: plain\n" +

            // 'nonterminal' name properties.
            "nonterminal.name.padding.horizontal: 5\n" + "nonterminal.name.padding.vertical:   5\n"
            + "nonterminal.corner.radius:   0\n" + "nonterminal.box.color:       0 0 0\n"
            + "nonterminal.box.linewidth:   1.0\n" + "nonterminal.text.color:      0 0 0\n"
            + "nonterminal.text.font:       Serif\n" + "nonterminal.text.font.size:  12\n"
            + "nonterminal.text.font.style: italic\n";

    // Token text properties:
    // "terminal.token.<tokname>: <toktext>" // Should be complete.
    // "meta-terminal.<tokname>: <toktext>" // Should be complete.
    // "nonterminal.token.<tokname>: <toktext>" // Optional, default: tokname == toktext.

    /** Stored key/value pairs of the properties of the rail diagram generator. */
    private Map<String, String> defaultProperties = map();

    /** Stored key/value pairs of the properties of the rail diagram generator. */
    private Map<String, String> loadedProperties = map();

    /** Cached font data information, lazily loaded. */
    private Map<NameKind, FontData> fontdataCache = map();

    /** Graphics render engine. */
    private Graphics2D gd;

    /**
     * Constructor of the {@link Configuration} class.
     *
     * @param gd Graphics render engine.
     */
    public Configuration(Graphics2D gd) {
        this.gd = gd;

        // Copy the properties of PROPERTIES_LITERAL to a map.
        Properties defaultConfig = new Properties();
        try {
            defaultConfig.load(new StringReader(PROPERTIES_LITERAL));
        } catch (IOException ex) {
            throw new RuntimeException("Properties literal not readable.");
        }
        for (String key: defaultConfig.stringPropertyNames()) {
            defaultProperties.put(key, defaultConfig.getProperty(key));
        }
    }

    /**
     * Get the width of the rail in the diagram.
     *
     * @return Width of the rail, is always non-negative.
     */
    public double getRailWidth() {
        return Math.max(0, getRealValue("rail.linewidth"));
    }

    /**
     * Get the color of the rail in the diagram.
     *
     * @return Color of the rail.
     */
    public Color getRailColor() {
        return getRgbColor("rail.color");
    }

    public TextSizeOffset getTextSizeOffset(String text, NameKind nameKind) {
        FontData fd = getFont(nameKind);
        return new TextSizeOffset(fd.getTextOffset(gd, text), fd.getTextSize(gd, text));
    }

    /**
     * Get a real value from the configuration by its name.
     *
     * @param name Name of the requested value.
     * @return The stored value or {@code 1.0} if it does not exist.
     */
    public double getRealValue(String name) {
        String value = getPropertyValue(name);
        return decodeReal(value, 1.0);
    }

    /**
     * Get the color of the text for a given kind of names.
     *
     * @param nameKind Kind of names being queried.
     * @return Color of the text of the queried kind of names.
     */
    public Color getTextColor(NameKind nameKind) {
        return getRgbColor(nameKind.configPrefix + ".text.color");
    }

    /**
     * Get the color of the given property name.
     *
     * @param name Name of the property to inspect.
     * @return Color of the property if it was a valid color, else a default color.
     */
    public Color getRgbColor(String name) {
        String colorText = getPropertyValue(name);
        return decodeColor(colorText);
    }

    /**
     * Get the color of the box for a given kind of names.
     *
     * @param nameKind Kind of names being queried.
     * @return Color of the box of the queried kind of names.
     */
    public Color getBoxColor(NameKind nameKind) {
        return getRgbColor(nameKind.configPrefix + ".box.color");
    }

    /**
     * Get the font to use for a kind of name.
     *
     * @param nameKind Kind of name to obtain the font for.
     * @return Font information for the requested kind of name.
     */
    public FontData getFont(NameKind nameKind) {
        FontData fontData = fontdataCache.get(nameKind);
        if (fontData != null)
            return fontData;

        String fontName = getPropertyValue(nameKind.configPrefix + ".text.font");

        String fontSizeText = getPropertyValue(nameKind.configPrefix + ".text.font.size");
        int fontSize = decodeInt(fontSizeText, 10);

        String fontStyleText = getPropertyValue(nameKind.configPrefix + ".text.font.style");
        fontStyleText = fontStyleText.toLowerCase(Locale.US);

        FontStyle fontStyle;
        if (fontStyleText.equals("bold")) {
            fontStyle = FontStyle.BOLD;
        } else if (fontStyleText.equals("italic")) {
            fontStyle = FontStyle.ITALIC;
        } else {
            fontStyle = FontStyle.PLAIN;
        }

        fontData = new FontData(fontName, fontStyle, fontSize);
        fontdataCache.put(nameKind, fontData);
        return fontData;
    }

    /**
     * Get the radius of the corners of the box around the name.
     *
     * @param nameKind Kind of name.
     * @return Radius of the corners of the box around the name.
     */
    public double getCornerRadius(NameKind nameKind) {
        return getRealValue(nameKind.configPrefix + ".corner.radius");
    }

    /**
     * Get the line-width of the box around a name.
     *
     * @param nameKind Kind of name.
     * @return The line-width of the box around a name.
     */
    public double getBoxLineWidth(NameKind nameKind) {
        return getRealValue(nameKind.configPrefix + ".box.linewidth");
    }

    /**
     * Compute the kind of token from its name.
     *
     * @param name Name to analyze.
     * @return Kind of token associated with the name.
     */
    public NameKind getNameKind(String name) {
        String propName = "terminal.token." + name;
        if (loadedProperties.containsKey(propName))
            return NameKind.TERMINAL;
        propName = "meta-terminal.token." + name;
        if (loadedProperties.containsKey(propName))
            return NameKind.META_TERMINAL;
        return NameKind.NON_TERMINAL;
    }

    /**
     * Get the text of a name, supplying a name kind speeds up the search.
     *
     * @param name Name of the token.
     * @param nameKind If not {@code null} the kind of name.
     * @return The text to use in the diagram.
     */
    public String getNameText(String name, NameKind nameKind) {
        if (nameKind == null || nameKind == NameKind.TERMINAL) {
            String propName = "terminal.token." + name;
            String text = loadedProperties.get(propName);
            if (nameKind != null || text != null)
                return text;
        }

        if (nameKind == null || nameKind == NameKind.META_TERMINAL) {
            String propName = "meta-terminal.token." + name;
            String text = loadedProperties.get(propName);
            if (nameKind != null || text != null)
                return text;
        }

        String propName = "nonterminal.token." + name;
        return loadedProperties.getOrDefault(propName, name);
    }

    /**
     * Get the text of a property by its name.
     * 
     * @param key Name of the property to look for.
     * @return The text of the requested property, is never {@code null}.
     */
    private String getPropertyValue(String key) {
        String value = loadedProperties.get(key);
        if (value == null)
            value = defaultProperties.get(key); // Should always have a value!
        Assert.notNull(value, fmt("Property \"%s\" not found at all.", key));
        return value;
    }

    /**
     * Decode a string consisting of 3 integer values [0..255] to an RGB color.
     *
     * @param colorText Text describing the color as 3 integer values.
     * @return The decoded color, or a default color if decoding failed.
     */
    private Color decodeColor(String colorText) {
        Color defaultColor = Color.pink;

        if (colorText == null)
            return defaultColor;

        Matcher m = RGB_PATTERN.matcher(colorText);
        if (!m.matches())
            return defaultColor;

        int red = decodeInt(m.group(1), -1);
        int green = decodeInt(m.group(2), -1);
        int blue = decodeInt(m.group(3), -1);
        if (red < 0 || green < 0 || blue < 0)
            return defaultColor;
        if (red > 255 || green > 255 || blue > 255)
            return defaultColor;
        return new Color(red, green, blue);
    }

    /**
     * Convert a piece of text to an integer number.
     *
     * @param valueText Text to convert.
     * @param defaultValue Value to use if the conversion fails.
     * @return The result value.
     */
    private int decodeInt(String valueText, int defaultValue) {
        try {
            return Integer.parseInt(valueText);
        } catch (NumberFormatException ex) {
            // Bad numeric string, use the default.
        }
        return defaultValue;
    }

    /**
     * Convert a piece of text to an integer number.
     *
     * @param valueText Text to convert.
     * @param defaultValue Value to use if the conversion fails.
     * @return The result value.
     */
    private double decodeReal(String valueText, double defaultValue) {
        try {
            return Double.parseDouble(valueText);
        } catch (NumberFormatException ex) {
            // Bad numeric string, use the default.
        }
        return defaultValue;
    }

    /**
     * Load a properties file into the configuration.
     *
     * @param fname Name of the file to load.
     * @return The loaded properties.
     */
    public void loadPropertiesFile(String fname) {
        Properties props = new Properties();

        FileInputStream stream = null;
        try {
            stream = new FileInputStream(fname);
            props.load(stream);
        } catch (FileNotFoundException ex) {
            throw new InputOutputException(fmt("Could not open file \"%s\".", fname), ex);
        } catch (IOException ex) {
            throw new InputOutputException(fmt("Could not read file \"%s\".", fname), ex);
        } finally {
            try {
                if (stream != null)
                    stream.close();
            } catch (IOException ex) {
                throw new InputOutputException(fmt("Could not close file \"%s\".", fname), ex);
            }
        }
        // Extract the strings from the loaded properties and copy them to the global map.
        for (String propName: props.stringPropertyNames()) {
            String propValue = props.getProperty(propName);
            if (propValue == null || propValue.isEmpty())
                propValue = "*empty*";
            loadedProperties.put(propName, propValue);
        }
    }
}
