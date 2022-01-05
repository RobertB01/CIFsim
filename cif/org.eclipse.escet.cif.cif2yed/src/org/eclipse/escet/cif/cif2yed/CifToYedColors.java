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

package org.eclipse.escet.cif.cif2yed;

/** Colors to use. */
public enum CifToYedColors {
    /** Component background close color. */
    COMP_BG_CLOSED_COLOR("#cccccc"),

    /** Component background opened color. */
    COMP_BG_OPENED_COLOR("#eeeeee"),

    /** Code header color. */
    CODE_HEADER_COLOR("#eeee80"),

    /** Code background color. */
    CODE_BG_COLOR("#ffffcc"),

    /** Event declaration color. */
    EVENT_DECL_COLOR("#ffc000"),

    /** Event parameter color. */
    EVENT_PARAM_COLOR("#ffe0c0"),

    /** Event reference color. */
    EVENT_REF_COLOR("#ffc080"),

    /** Data declaration color. */
    DATA_DECL_COLOR("#00c0ff"),

    /** Data parameter color. */
    DATA_PARAM_COLOR("#c0e0ff"),

    /** Data reference color. */
    DATA_REF_COLOR("#80c0ff"),

    /** Component header color. */
    COMP_HEADER_COLOR("#ff0000"),

    /** Component instantiation header color. */
    INST_HEADER_COLOR("#ff8000"),

    /** Component definition header color. */
    DEF_HEADER_COLOR("#40c0ff"),

    /** Wrap box header color. */
    WRAP_BOX_HEADER_COLOR("#80ff40"),

    /** Location background color. */
    LOC_BG_COLOR("#ccccff");

    /** The color code, e.g. "#ff0000" for red. */
    public final String color;

    /**
     * Constructor for the {@link CifToYedColors} enumeration.
     *
     * @param color The color code, e.g. "#ff0000" for red.
     */
    private CifToYedColors(String color) {
        this.color = color;
    }
}
