#!/usr/bin/env python

################################################################################
# Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
#
# See the NOTICE file(s) distributed with this work for additional
# information regarding copyright ownership.
#
# This program and the accompanying materials are made available under the terms
# of the MIT License which is available at https://opensource.org/licenses/MIT
#
# SPDX-License-Identifier: MIT
################################################################################# 

import os.path

# Get paths.
lang_name = 'chi'
ecore_path = lang_name + '.ecore'
genmodel_path = lang_name + '.genmodel'

# Read files.
with open(ecore_path, 'r') as f:
    ecore_content = f.read().replace('\r', '')

with open(genmodel_path, 'r') as f:
    genmodel_content = f.read().replace('\r', '')

# Fix references to position information metamodel.
ecore_content = ecore_content.replace(
    '../../org.eclipse.escet.common.position.metamodel/model/position',
    'platform:/plugin/org.eclipse.escet.common.position.metamodel/model/position'
)
genmodel_content = genmodel_content.replace(
    '../../org.eclipse.escet.common.position.metamodel/model/position',
    'platform:/plugin/org.eclipse.escet.common.position.metamodel/model/position'
)

# Fix Java compliance level.
genmodel_content = genmodel_content.replace(
    'complianceLevel="5.0"',
    'complianceLevel="17.0"'
)
genmodel_content = genmodel_content.replace(
    'complianceLevel="6.0"',
    'complianceLevel="17.0"'
)
genmodel_content = genmodel_content.replace(
    'complianceLevel="7.0"',
    'complianceLevel="17.0"'
)
genmodel_content = genmodel_content.replace(
    'complianceLevel="8.0"',
    'complianceLevel="17.0"'
)
genmodel_content = genmodel_content.replace(
    'complianceLevel="9.0"',
    'complianceLevel="17.0"'
)
genmodel_content = genmodel_content.replace(
    'complianceLevel="10.0"',
    'complianceLevel="17.0"'
)
genmodel_content = genmodel_content.replace(
    'complianceLevel="11.0"',
    'complianceLevel="17.0"'
)
genmodel_content = genmodel_content.replace(
    'complianceLevel="12.0"',
    'complianceLevel="17.0"'
)
genmodel_content = genmodel_content.replace(
    'complianceLevel="13.0"',
    'complianceLevel="17.0"'
)
genmodel_content = genmodel_content.replace(
    'complianceLevel="14.0"',
    'complianceLevel="17.0"'
)
genmodel_content = genmodel_content.replace(
    'complianceLevel="15.0"',
    'complianceLevel="17.0"'
)
genmodel_content = genmodel_content.replace(
    'complianceLevel="16.0"',
    'complianceLevel="17.0"'
)

# Set base package.
genmodel_content = genmodel_content.replace(
    '<genPackages prefix="Chi"',
    '<genPackages prefix="Chi" basePackage="org.eclipse.escet.chi.metamodel"'
)

# Set file extension.
genmodel_content = genmodel_content.replace(
    'ecorePackage="chi.ecore#/">',
    'fileExtensions="chix" ecorePackage="chi.ecore#/">'
)

# Set generated files directory.
genmodel_content = genmodel_content.replace(
    'metamodel/src"',
    'metamodel/src-gen"'
)

# No edit project.
genmodel_content = genmodel_content.replace(
    '/src-gen"',
    '/src-gen" editDirectory=""'
)

# No editor project.
genmodel_content = genmodel_content.replace(
    'modelPluginID="',
    'editorDirectory="" modelPluginID="'
)

# No tests project.
genmodel_content = genmodel_content.replace(
    'MinimalEObjectImpl$Container"',
    'MinimalEObjectImpl$Container" testsDirectory=""'
)

# Copyright text.
genmodel_content = genmodel_content.replace(
    '/GenModel"',
    '/GenModel" copyrightText="Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation&#xA;&#xA;See the NOTICE file(s) distributed with this work for additional&#xA;information regarding copyright ownership.&#xA;&#xA;This program and the accompanying materials are made available under the terms&#xA;of the MIT License which is available at https://opensource.org/licenses/MIT&#xA;&#xA;SPDX-License-Identifier: MIT&#xA;&#xA;Disable Eclipse Java formatter for generated code file:&#xA;@formatter:off"'
)

# Remove operation reflection setting.
genmodel_content = genmodel_content.replace(
    'operationReflection="true"',
    ''
)

# Remove import organizing setting.
genmodel_content = genmodel_content.replace(
    'importOrganizing="true"',
    ''
)

# Extra line breaks.
genmodel_content = genmodel_content.replace(
    ' modelDirectory=',
    '\n    modelDirectory='
)
genmodel_content = genmodel_content.replace(
    ' modelPluginID=',
    '\n    modelPluginID='
)
genmodel_content = genmodel_content.replace(
    ' testsDirectory=',
    '\n    testsDirectory='
)
genmodel_content = genmodel_content.replace(
    ' copyrightFields=',
    '\n    copyrightFields='
)
genmodel_content = genmodel_content.replace(
    'basePackage="org.eclipse.escet.chi.metamodel" disposableProviderFactory="true" ',
    'basePackage="org.eclipse.escet.chi.metamodel" disposableProviderFactory="true"\n      '
)

# Remove line break.
genmodel_content = genmodel_content.replace(
    '\n    importerID=',
    ' importerID='
)
genmodel_content = genmodel_content.replace(
    '\n    usedGenPackages=',
    ' usedGenPackages='
)
genmodel_content = genmodel_content.replace(
    '\n    modelName=',
    ' modelName='
)
genmodel_content = genmodel_content.replace(
    'genmodel#//position"\n     >',
    'genmodel#//position">'
)

# Write files.
with open(ecore_path, "w") as f:
    f.write(ecore_content)

with open(genmodel_path, "w") as f:
    f.write(genmodel_content)
