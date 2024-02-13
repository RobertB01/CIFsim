/******************************************************************************
 * Copyright (c) 2024 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the terms
 * of the MIT License which is available at https://opensource.org/licenses/MIT
 *
 *  SPDX-License-Identifier: MIT
 *****************************************************************************/

let ESCET_DEBUG = false;

function onLoad() {
    var versions = getVersions();
    initVersionDropdown(versions);
    initAllVersionsList(versions);
}

function getVersions() {
    // Get versions text.
    var versionsText = null;
    if (ESCET_DEBUG) {
        versionsText =
            `nightly
            v0.1
            v0.10
            v0.2
            v2.0-RC1
            v0.3
            v0.5
            v0.4
            v2.0-M1
            v0.6
            v2.0
            v0.7
            v0.8
            v0.9
            v3.0-M1`;
    } else {
        var request = new XMLHttpRequest();
        request.open("GET", '/escet/.versions', false);
        request.send();
        if (request.status == 200) {
            versionsText = request.responseText;
        }
    }

    // Get trimmed sorted versions.
    var versions = versionsText.split(/\r?\n/);
    versions = versions.map(v => v.trim());
    versions.sort(compareVersions);
    if (ESCET_DEBUG) {
        console.log('Versions: ' + versions.join(' '));
    }
    return versions;
}

function initVersionDropdown(versions) {
    // Get 'this' version, the version currently being shown.
    var thisVersion = getThisVersion();

    // Add items to the versions dropdown.
    var previewsElem = document.getElementById('versions-previews');
    var latestElem = document.getElementById('versions-latest-releases');
    getPreviewVersions(versions, thisVersion).forEach(version => {
        addVersionToDropDown(version, thisVersion, previewsElem);
    });
    getLatestReleasesVersions(versions).forEach(version => {
        addVersionToDropDown(version, thisVersion, latestElem);
    });
}

function getThisVersion() {
    // Get 'this' version, the version currently being shown.
    // For technical reasons (see 'pom.xml'), the version number starts with a '/', so we remove it here.
    var versionsDropdownElem = document.getElementById('versions-dropdown');
    var thisVersion = versionsDropdownElem.dataset.thisVersion;
    while (thisVersion.startsWith('/')) thisVersion = thisVersion.substring(1);
    return thisVersion;
}

function compareVersions(v1, v2) {
    // Handle same versions.
    if (v1 == v2) return 0;

    // Handle nightlies, considering them larger than any other version.
    if (v1 == 'nightly') return 1;
    if (v2 == 'nightly') return -1;

    // Compare two actual versions. Get their postfixes first.
    var postfix1 = v1.includes('-') ? v1.substring(v1.indexOf('-')) : '';
    var postfix2 = v2.includes('-') ? v2.substring(v2.indexOf('-')) : '';
    v1 = v1.substring(0, v1.length - postfix1.length);
    v2 = v2.substring(0, v2.length - postfix2.length);

    // Get the numeric parts of the version. Ensure they have equal length.
    console.assert(v1.startsWith('v'), v1);
    console.assert(v2.startsWith('v'), v2);
    var parts1 = v1.substring(1).split('.');
    var parts2 = v2.substring(1).split('.');
    while (parts1.length < parts2.length) parts1.push('0');
    while (parts2.length < parts1.length) parts2.push('0');

    // Compare the numeric parts of the versions.
    for (let i = 0; i < parts1.length; i++) {
        var part1 = Number(parts1[i]);
        var part2 = Number(parts2[i]);
        var result = part1 - part2;
        if (result != 0) return result;
    }

    // Compare the postfixes of the versions. No postfix is considered a later version than with a postfix.
    if (postfix1 == postfix2) return 0;
    if (postfix1.length == 0) return 1;
    if (postfix2.length == 0) return -1;
    return postfix1.localeCompare(postfix2, 'en-US');
}

function getPreviewVersions(versions, thisVersion) {
    var thisVersionIdx = versions.indexOf(thisVersion);
    var newerVersions = versions.slice(thisVersionIdx + 1);
    return newerVersions.filter(v => v == 'nightly' || v.includes('-'));
}

function getLatestReleasesVersions(versions) {
    var releaseVersions = versions.filter(v => v != 'nightly' && !v.includes('-'));
    return releaseVersions.slice(-3);
}

function addVersionToDropDown(version, thisVersion, afterElem) {
    var liElem = document.createElement('li');
    afterElem.after(liElem);

    var aElem = document.createElement('a');
    liElem.appendChild(aElem);

    aElem.classList.add('dropdown-item');
    aElem.setAttribute('href', '/escet/' + version);
    aElem.textContent = version;
    if (version == thisVersion) {
        aElem.classList.add('disabled');
        aElem.textContent += ' (this version)';
    }
}

function initAllVersionsList(versions) {
    // Nothing to do if the all versions list is not on this page.
    var allVersionsListElem = document.getElementById('all-versions-list');
    if (allVersionsListElem === null) {
        return;
    }

    // Get 'this' version, the version currently being shown.
    var thisVersion = getThisVersion();

    // Add items to all versions list.
    versions.forEach(version => {
        var liElem = document.createElement('li');
        allVersionsListElem.appendChild(liElem);

        var aElem = document.createElement('a');
        liElem.appendChild(aElem);

        aElem.setAttribute('href', '/escet/' + version);
        aElem.textContent = version;

        if (version == thisVersion) {
            aElem.after(' (this version)');
        }
    });
}
