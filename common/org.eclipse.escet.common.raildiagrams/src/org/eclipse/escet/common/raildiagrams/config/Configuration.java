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

package org.eclipse.escet.common.raildiagrams.config;

import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.makeUppercase;

import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Strings;
import org.eclipse.escet.common.raildiagrams.config.FontData.FontStyle;
import org.eclipse.escet.common.raildiagrams.output.OutputTarget;
import org.eclipse.escet.common.raildiagrams.util.DebugDisplayKind;

/** Configuration data of the diagrams. */
public class Configuration {
    /** Compiled pattern to match RGB values. */
    private static final Pattern RGB_PATTERN = Pattern.compile("\\s*(\\d+)\\s+(\\d+)\\s+(\\d+)\\s*");

    /** Textual values that mean {@code true}. */
    private static final Set<String> TRUE_BOOL_VALUES = set("TRUE", "YES", "1");

    /** Textual values that mean {@code false}. */
    private static final Set<String> FALSE_BOOL_VALUES = set("FALSE", "NO", "0");

    /** Stored key/value pairs of the properties of the rail diagram generator. */
    private Map<String, String> defaultProperties = map();

    /** Stored key/value pairs of the properties of the rail diagram generator. */
    private Map<String, String> loadedProperties = map();

    /** Cached font data information, lazily loaded. */
    private Map<NameKind, FontData> fontdataCache = map();

    /** Output target knowing about text sizes. */
    private OutputTarget outputTarget;

    /** The debug logger to use, or {@code null} to not perform debug logging. */
    private Consumer<String> debugLogger;

    /** The level of indentation for debug logging. */
    private int debugLevel = 0;

    /**
     * Constructor of the {@link Configuration} class.
     *
     * @param outputTarget Output target knowing about text sizes.
     * @param debugLogger The debug logger to use, or {@code null} to not perform debug logging.
     */
    public Configuration(OutputTarget outputTarget, Consumer<String> debugLogger) {
        this.outputTarget = outputTarget;
        this.debugLogger = debugLogger;

        // Read default properties and put into a map.
        Properties defaultConfig = new Properties();
        String defaultConfigPath = Configuration.class.getPackageName().replace(".", "/") + "/default.properties";
        try (InputStream rstream = getClass().getClassLoader().getResourceAsStream(defaultConfigPath);
             InputStream stream = new BufferedInputStream(rstream))
        {
            defaultConfig.load(stream);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to read default railroad diagram generator configuration.", ex);
        }
        for (String key: defaultConfig.stringPropertyNames()) {
            defaultProperties.put(key, defaultConfig.getProperty(key));
        }
    }

    /**
     * Returns whether debug output is enabled.
     *
     * @return {@code true} if debug output is enabled, {@code false} otherwise.
     */
    public boolean dodbg() {
        return debugLogger != null;
    }

    /** Increases the debug indentation level by one. */
    public void idbg() {
        debugLevel++;
    }

    /** Decreases the debug indentation level by one. */
    public void ddbg() {
        Assert.check(debugLevel > 0);
        debugLevel--;
    }

    /** Outputs an empty line of debug text, if debugging is enabled, and does nothing otherwise. */
    public void dbg() {
        dbg("");
    }

    /**
     * Outputs a formatted line of debug text, if debugging is enabled, and does nothing otherwise.
     *
     * @param text The line of text to output, as format pattern.
     * @param args The format pattern arguments.
     */
    public void dbg(String text, Object... args) {
        if (dodbg()) {
            String formattedText = fmt(text, args);
            if (debugLevel > 0) {
                formattedText = Strings.indent(formattedText, debugLevel * 4);
            }
            formattedText = formattedText.stripTrailing();
            debugLogger.accept(formattedText);
        }
    }

    /**
     * Get the width of the rail in the diagram.
     *
     * @return Width of the rail, is always non-negative.
     */
    public int getRailWidth() {
        return Math.max(0, getIntValue("rail.linewidth"));
    }

    /**
     * Get the color of the rail in the diagram.
     *
     * @return Color of the rail.
     */
    public Color getRailColor() {
        return getRgbColor("rail.color");
    }

    /**
     * Get both the size and the offset of a text.
     *
     * @param text Text to query.
     * @param nameKind Kind of text, defines font properties to use.
     * @return Size and offset of the formatted text.
     */
    public TextSizeOffset getTextSizeOffset(String text, NameKind nameKind) {
        FontData fd = getFont(nameKind);
        return outputTarget.getTextSizeOffset(text, fd);
    }

    /**
     * Get a real value from the configuration by its name.
     *
     * @param name Name of the requested value.
     * @return The stored value or {@code 1.0} if it does not exist.
     */
    public int getIntValue(String name) {
        String value = getPropertyValue(name);
        return decodeInt(value, 1);
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
        Color override = outputTarget.getOverrideColor(name);
        if (override != null) {
            return override;
        }

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
        if (fontData != null) {
            return fontData;
        }

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
    public int getCornerRadius(NameKind nameKind) {
        return getIntValue(nameKind.configPrefix + ".corner.radius");
    }

    /**
     * Get the line-width of the box around a name.
     *
     * @param nameKind Kind of name.
     * @return The line-width of the box around a name.
     */
    public int getBoxLineWidth(NameKind nameKind) {
        return getIntValue(nameKind.configPrefix + ".box.linewidth");
    }

    /**
     * Compute the kind of token from its name.
     *
     * @param name Name to analyze.
     * @return Kind of token associated with the name.
     */
    public NameKind getNameKind(String name) {
        String propName = "terminal.token." + name;
        if (loadedProperties.containsKey(propName)) {
            return NameKind.TERMINAL;
        }
        propName = "meta-terminal.token." + name;
        if (loadedProperties.containsKey(propName)) {
            return NameKind.META_TERMINAL;
        }
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
            if (nameKind != null || text != null) {
                return text;
            }
        }

        if (nameKind == null || nameKind == NameKind.META_TERMINAL) {
            String propName = "meta-terminal.token." + name;
            String text = loadedProperties.get(propName);
            if (nameKind != null || text != null) {
                return text;
            }
        }

        String propName = "nonterminal.token." + name;
        return loadedProperties.getOrDefault(propName, name);
    }

    /**
     * Obtain whether debug output for the given kind is switched on.
     *
     * @param kind Kind of debug output.
     * @return Whether the requested kind of debug output was enabled.
     * @note Function returns {@code true} if the setting is not explicitly set to {@code false}.
     */
    public boolean getDebugSetting(DebugDisplayKind kind) {
        String propName = "debug." + kind.name().toLowerCase(Locale.US);
        String text = getPropertyValue(propName);
        if (text == null) {
            text = "";
        }
        return decodeBool(text, true);
    }

    /**
     * Get the text of a property by its name.
     *
     * @param key Name of the property to look for.
     * @return The text of the requested property, is never {@code null}.
     */
    private String getPropertyValue(String key) {
        String value = loadedProperties.get(key);
        if (value == null) {
            value = defaultProperties.get(key); // Should always have a value!
        }
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
        final Color defaultColor = Color.pink;

        if (colorText == null) {
            return defaultColor;
        }

        Matcher m = RGB_PATTERN.matcher(colorText);
        if (!m.matches()) {
            return defaultColor;
        }

        int red = decodeInt(m.group(1), -1);
        int green = decodeInt(m.group(2), -1);
        int blue = decodeInt(m.group(3), -1);
        if (red < 0 || green < 0 || blue < 0) {
            return defaultColor;
        }
        if (red > 255 || green > 255 || blue > 255) {
            return defaultColor;
        }
        return new Color(red, green, blue);
    }

    /**
     * Convert a piece of text to a boolean value.
     *
     * @param valueText Text to convert.
     * @param defaultValue Value to use if the conversion fails.
     * @return The result value.
     */
    private boolean decodeBool(String valueText, boolean defaultValue) {
        valueText = makeUppercase(valueText);

        if (TRUE_BOOL_VALUES.contains(valueText)) {
            return true;
        }
        if (FALSE_BOOL_VALUES.contains(valueText)) {
            return false;
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
    private int decodeInt(String valueText, int defaultValue) {
        try {
            return Integer.parseInt(valueText);
        } catch (NumberFormatException ex) {
            // Bad numeric string, use the default.
        }
        return defaultValue;
    }

    /**
     * Load a properties file into the configuration.
     *
     * @param path The path to the properties file.
     * @throws IOException In case of an I/O error.
     */
    public void loadPropertiesFile(Path path) throws IOException {
        Properties props = new Properties();

        try (FileInputStream fstream = new FileInputStream(path.toFile());
             InputStream stream = new BufferedInputStream(fstream))
        {
            props.load(stream);
        } catch (FileNotFoundException ex) {
            throw new IOException(fmt("Could not open file \"%s\".", path), ex);
        } catch (IOException ex) {
            throw new IOException(fmt("Could not read file \"%s\".", path), ex);
        }

        // Extract the strings from the loaded properties and copy them to the global map.
        for (String propName: props.stringPropertyNames()) {
            String propValue = props.getProperty(propName);
            if (propValue == null || propValue.isEmpty()) {
                propValue = "*empty*";
            }
            loadedProperties.put(propName, propValue);
        }
    }
}
