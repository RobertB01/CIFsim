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

package org.eclipse.escet.tooldef.typechecker;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.eclipse.escet.tooldef.common.ToolDefTextUtils;
import org.eclipse.escet.tooldef.metamodel.tooldef.TypeParam;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.ToolDefType;

/** Type parameter constraints. */
public class TypeConstraints extends LinkedHashMap<TypeParam, ToolDefType> {
    @Override
    public String toString() {
        StringBuilder txt = new StringBuilder();
        txt.append("{");
        boolean first = true;
        for (Entry<TypeParam, ToolDefType> entry: entrySet()) {
            if (first) {
                first = false;
            } else {
                txt.append(", ");
            }
            txt.append(ToolDefTextUtils.getAbsName(entry.getKey()));
            txt.append(": ");
            txt.append(ToolDefTextUtils.typeToStr(entry.getValue(), false));
        }
        txt.append("}");
        return txt.toString();
    }
}
