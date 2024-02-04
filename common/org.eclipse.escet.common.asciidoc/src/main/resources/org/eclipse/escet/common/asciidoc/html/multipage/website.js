/******************************************************************************
 * Copyright (c) 2023, 2024 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the terms
 * of the MIT License which is available at https://opensource.org/licenses/MIT
 *
 *  SPDX-License-Identifier: MIT
 *****************************************************************************/

/** The page has loaded. */
function onLoad() {
    tocAddCurrentSection();
    window.onhashchange = onHashChange;
}

/** The hash part ('#...') of the windows's location has changed. */
function onHashChange(event) {
    tocClearCurrentSection();
    tocAddCurrentSection();
}

/** Add current section class markers. */
function tocAddCurrentSection() {
    if (window.location.hash) {
        var aElem = document.querySelector('#toc a[href="' + window.location.hash + '"]');
        if (aElem) {
            // Mark TOC item as the current section.
            var liElem = aElem.parentElement;
            liElem.classList.add('toc-cur-section');
            liElem.classList.add('expanded');

            // Mark ancestors. Don't give TOC items a duplicate marking though.
            var elem = liElem.parentElement;
            while (true) {
                if (elem === null) break;
                if (elem.id === 'toc') break;
                if (elem.classList.contains('toc-cur-page')) break;
                if (elem.classList.contains('toc-cur-page-ancestor')) break;
                if (elem.tagName === 'LI') {
                    elem.classList.add('toc-cur-section-ancestor');
                    elem.classList.add('expanded');
                }
                elem = elem.parentElement;
            }
        }
    }
}

/** Clear all current section class markers. */
function tocClearCurrentSection() {
    var elems = document.querySelectorAll('#toc li.toc-cur-section, #toc li.toc-cur-section-ancestor');
    for (let i = 0; i < elems.length; i++) {
        var elem = elems.item(i);
        elem.classList.remove('toc-cur-section', 'toc-cur-section-ancestor');
    }
}

/**
 * Toggle a TOC item.
 *
 * @param {object} divElem - The TOC item's 'div' element.
 */
function tocToggle(divElem) {
    var liElem = divElem.parentElement;
    liElem.classList.toggle('expanded');
}
