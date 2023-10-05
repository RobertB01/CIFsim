//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

import groovy.json.JsonSlurper

def jsonSlurper = new JsonSlurper()
data = jsonSlurper.parse(new File("${project.basedir}/src/create_scripts.json"))

data.configurations.each{
    configuration ->

    new File("${project.basedir}/target/bin/${configuration.name}").mkdirs()
    data.scripts.each{
        script ->

        File file = new File("${project.basedir}/target/bin/${configuration.name}/${script.script_name}${configuration.extension}")
        file.write "${configuration.header}${configuration.newline}${configuration.newline}"
        file << "${configuration.eclipse} --launcher.suppressErrors -application org.eclipse.escet.common.app.framework.application -nosplash ${script.plugin_name} ${script.class_name} ${configuration.arguments}${configuration.newline}"
    }
}
