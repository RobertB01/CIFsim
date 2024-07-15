/******************************************************************************
 * Copyright (c) 2006, 2024 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the terms
 * of the MIT License which is available at https://opensource.org/licenses/MIT
 *
 * SPDX-License-Identifier: MIT
 *****************************************************************************/

function adaptCodeBlocks() {
    // To have nice source code in the HTML file, we indent the 'code' in the 'pre' and the 'pre' has indented code as
    // well. Adapt it so that we have normal code in the code blocks.
    const elems = document.querySelectorAll('pre > code');
    elems.forEach(elem => {
        // Get lines of code.
        const text = elem.textContent;
        var lines = text.split(/\r?\n/);

        // Remove trailing whitespace and remove leading/trailing empty lines.
        lines = lines.map(l => l.trimEnd());
        while (lines.length > 0 && lines[0].length == 0)                lines = lines.slice(1);
        while (lines.length > 0 && lines[lines.length - 1].length == 0) lines = lines.slice(0, -1);

        // Remove common indentation of the lines. Empty lines are ingored while computing the common indentation.
        const indents = lines.filter(l => l.length > 0).map(l => l.search(/\S/));
        const commonIndent = Math.min(...indents);
        lines = lines.map(l => l.slice(commonIndent));

        // Set new text.
        elem.textContent = lines.join('\n');

        // Remove whitespace around 'code' in 'pre'.
        const parent = elem.parentElement;
        while (parent.firstChild !== elem) parent.removeChild(parent.firstChild);
        while (parent.lastChild !== elem) parent.removeChild(parent.lastChild);
    });
}

function doHighlighting() {
    // Apply code syntax highlighting.
    if (!hljs.initHighlighting.called) {
        hljs.initHighlighting.called = true;
        [].slice.call(document.querySelectorAll('pre.highlight > code')).forEach(
            function (el) {
                hljs.highlightElement(el);
            }
        );
    }
}

window.onload = function () {
    // Fix pre/post empty lines and indentation of code blocks, including ones with and without syntax highlighting.
    adaptCodeBlocks();

    // Apply code syntax highlighting.
    doHighlighting();

    // Build quizzes.
    buildQuizzes();

    // Version selector.
    initVersionDropdown(getVersions());
}
