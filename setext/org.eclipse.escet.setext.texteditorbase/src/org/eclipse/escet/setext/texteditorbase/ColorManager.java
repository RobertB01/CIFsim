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

package org.eclipse.escet.setext.texteditorbase;

import static org.eclipse.escet.common.java.Maps.map;

import java.util.Map;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

/** Color manager. Allows sharing of {@link Color} resources. */
public class ColorManager {
    /** Internal {@link RGB} to {@link Color} mapping cache. */
    protected Map<RGB, Color> cache = map();

    /**
     * Obtains a {@link Color} resource representing the given {@link RGB} color description.
     *
     * @param rgb Color description to obtain a color resource for.
     * @return The cached (shared) color resource, if known to the color manager, or a freshly created color resource
     *     otherwise.
     */
    public Color getColor(RGB rgb) {
        Color rslt = cache.get(rgb);
        if (rslt == null) {
            rslt = new Color(rgb);
            cache.put(rgb, rslt);
        }
        return rslt;
    }

    /**
     * Creates a color style token for the given {@link RGB} color description.
     *
     * @param rgb Color description to create a color style token for.
     * @return The created color style token.
     */
    public IToken createColorToken(RGB rgb) {
        return new Token(new TextAttribute(getColor(rgb)));
    }
}
