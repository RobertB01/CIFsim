#!/usr/bin/env sh

################################################################################
# Copyright (c) 2021 Contributors to the Eclipse Foundation
#
# See the NOTICE file(s) distributed with this work for additional
# information regarding copyright ownership.
#
# This program and the accompanying materials are made available under the terms
# of the MIT License which is available at https://opensource.org/licenses/MIT
#
# SPDX-License-Identifier: MIT
################################################################################

# See also https://wiki.eclipse.org/IT_Infrastructure_Doc#macOS_Notarization

SCRIPT_DIR="$(dirname "$0")"

PRODUCTS_DIR="${SCRIPT_DIR}/target/products/"
pushd $PRODUCTS_DIR

DMG="$(find . -type f -name "*-mac*.dmg")"
PRIMARY_BUNDLE_ID="eclipse-escet-app-bundle"

RESPONSE=$(curl -s -X POST \
        -F file=@${DMG} \
        -F 'options={"primaryBundleId": "'${PRIMARY_BUNDLE_ID}'", "staple": true};type=application/json' \
        https://cbi.eclipse.org/macos/xcrun/notarize)
UUID=$(echo $RESPONSE | grep -Po '"uuid"\s*:\s*"\K[^"]+')
STATUS=$(echo $RESPONSE | grep -Po '"status"\s*:\s*"\K[^"]+')

while [[ ${STATUS} == 'IN_PROGRESS' ]]; do
    sleep 10s
    RESPONSE=$(curl -s https://cbi.eclipse.org/macos/xcrun/${UUID}/status)
    STATUS=$(echo $RESPONSE | grep -Po '"status"\s*:\s*"\K[^"]+')
done

if [[ ${STATUS} != 'COMPLETE' ]]; then
    echo "Notarization failed: ${RESPONSE}"
    exit 1
fi

rm "${DMG}"

curl -JO https://cbi.eclipse.org/macos/xcrun/${UUID}/download

popd
