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

package org.eclipse.escet.setext.texteditor.tests;

import static org.eclipse.escet.common.java.Lists.list;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.eclipse.escet.setext.texteditor.SeTextCodeHighlighter;
import org.eclipse.escet.setext.texteditorbase.highlight.CodeHighlighter;
import org.junit.Test;

/** SeText code highlighter tests. Essentially tests the {@link CodeHighlighter} class. */
public class SeTextCodeHighlighterTest {
    @Test
    @SuppressWarnings("javadoc")
    public void test() {
        List<String> code = list( //
                "@terminals: /* bla */ x = 5; \n", //
                "ARROWTK       = \"\\->\"; // abc \r\n", //
                "\r\n", //
                "\t @shortcut identifier = \"[$]?[a-zA-Z_][a-zA-Z0-9_]*\";" //
        );

        List<String> expectedHtml = list( //
                "<html>", //
                "<span style=\"color:#402020;\">@</span>", //
                "<span style=\"color:#0000ff;\">terminals</span>", //
                "<span style=\"color:#402020;\">:</span>&nbsp;", //
                "<span style=\"text-style:italic;color:#808080;\">/*</span>&nbsp;", //
                "<span style=\"text-style:italic;color:#808080;\">bla</span>&nbsp;", //
                "<span style=\"text-style:italic;color:#808080;\">*/</span>&nbsp;", //
                "<span style=\"color:#000000;\">x</span>&nbsp;", //
                "<span style=\"color:#402020;\">=</span>&nbsp;", //
                "<span style=\"color:#402020;\">5;</span>&nbsp;", //
                "<br><span style=\"color:#000000;\">ARROWTK</span>", //
                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", //
                "<span style=\"color:#402020;\">=</span>&nbsp;", //
                "<span style=\"color:#c00000;\">&quot;\\-&gt;&quot;</span>", //
                "<span style=\"color:#402020;\">;</span>&nbsp;", //
                "<span style=\"text-style:italic;color:#808080;\">//</span>&nbsp;", //
                "<span style=\"text-style:italic;color:#808080;\">abc</span>&nbsp;", //
                "<br><br>&nbsp;&nbsp;<span style=\"color:#402020;\">@</span>", //
                "<span style=\"color:#0000ff;\">shortcut</span>&nbsp;", //
                "<span style=\"color:#000000;\">identifier</span>&nbsp;", //
                "<span style=\"color:#402020;\">=</span>&nbsp;", //
                "<span style=\"color:#c00000;\">&quot;[$]?[a-zA-Z_][a-zA-Z0-9_]*&quot;</span>", //
                "<span style=\"color:#402020;\">;</span>", //
                "</html>" //
        );

        String actualHtml;
        try (CodeHighlighter highlighter = new SeTextCodeHighlighter()) {
            actualHtml = highlighter.toHtml(String.join("", code));
        }
        assertEquals(String.join("", expectedHtml), actualHtml);
    }
}
