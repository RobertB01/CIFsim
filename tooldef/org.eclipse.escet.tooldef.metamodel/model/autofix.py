#!/usr/bin/env python

################################################################################
# Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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
import re

# Get paths.
lang_name = 'tooldef'
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
genmodel_content = re.sub('complianceLevel="\d+\.\d+"', 'complianceLevel="17.0"', genmodel_content)

# Set base package.
genmodel_content = genmodel_content.replace(
    '<genPackages prefix="Tooldef"',
    '<genPackages prefix="Tooldef" basePackage="org.eclipse.escet.tooldef.metamodel"'
)

# Set file extension.
genmodel_content = genmodel_content.replace(
    'ecorePackage="tooldef.ecore#/">',
    'fileExtensions="tooldefx" ecorePackage="tooldef.ecore#/">'
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
    '/GenModel" copyrightText="Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation&#xA;&#xA;See the NOTICE file(s) distributed with this work for additional&#xA;information regarding copyright ownership.&#xA;&#xA;This program and the accompanying materials are made available under the terms&#xA;of the MIT License which is available at https://opensource.org/licenses/MIT&#xA;&#xA;SPDX-License-Identifier: MIT&#xA;&#xA;Disable Eclipse Java formatter for generated code file:&#xA;@formatter:off"'
)

# Extra line breaks.
genmodel_content = genmodel_content.replace(
    ' modelDirectory=',
    '\n    modelDirectory='
)
genmodel_content = genmodel_content.replace(
    ' editorDirectory=',
    '\n    editorDirectory='
)
genmodel_content = genmodel_content.replace(
    ' rootExtendsClass=',
    '\n    rootExtendsClass='
)
genmodel_content = genmodel_content.replace(
    'basePackage="org.eclipse.escet.tooldef.metamodel" ',
    'basePackage="org.eclipse.escet.tooldef.metamodel"\n      '
)

# Remove line break.
genmodel_content = genmodel_content.replace(
    '\n    modelName=',
    ' modelName='
)

# Write files.
with open(ecore_path, "w") as f:
    f.write(ecore_content)

with open(genmodel_path, "w") as f:
    f.write(genmodel_content)
