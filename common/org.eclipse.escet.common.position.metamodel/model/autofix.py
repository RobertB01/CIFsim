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
import re

# Get paths.
lang_name = 'position'
ecore_path = lang_name + '.ecore'
genmodel_path = lang_name + '.genmodel'

# Read files.
with open(ecore_path, 'r') as f:
    ecore_content = f.read().replace('\r', '')

with open(genmodel_path, 'r') as f:
    genmodel_content = f.read().replace('\r', '')

# Fix Java compliance level.
genmodel_content = re.sub('complianceLevel="\d+\.\d+"', 'complianceLevel="17.0"', genmodel_content)

# Set base package.
genmodel_content = genmodel_content.replace(
    '<genPackages prefix="Position"',
    '<genPackages prefix="Position" basePackage="org.eclipse.escet.common.position.metamodel"'
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

# Extra line breaks.
genmodel_content = genmodel_content.replace(
    ' modelDirectory=',
    '\n    modelDirectory='
)
genmodel_content = genmodel_content.replace(
    ' modelName=',
    '\n    modelName='
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
    'basePackage="org.eclipse.escet.common.position.metamodel" ',
    'basePackage="org.eclipse.escet.common.position.metamodel"\n      '
)

# Remove line break.
genmodel_content = genmodel_content.replace(
    '\n    importerID=',
    ' importerID='
)
genmodel_content = genmodel_content.replace(
    '\n    rootExtendsClass=',
    ' rootExtendsClass='
)
genmodel_content = genmodel_content.replace(
    '\n    complianceLevel=',
    ' complianceLevel='
)

# Write files.
with open(ecore_path, "w") as f:
    f.write(ecore_content)

with open(genmodel_path, "w") as f:
    f.write(genmodel_content)
