//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.emf.prettyprint;

import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;

import org.eclipse.escet.common.java.Assert;

/** Provider of an Id for pretty features that want it. */
public class IdProvider {
    /** Value of the id number. A negative value means 'unknown id'. */
    private int id = -1;

    /** Pretty features that want a notification when an id is assigned. */
    private List<RegisteredIdCallback> prettyFeatureCallbacks = list();

    /**
     * Set the id number.
     *
     * @param id Assigned id number.
     */
    public void setIdNumber(int id) {
        Assert.check(this.id < 0);
        Assert.check(id >= 0);
        this.id = id;

        for (RegisteredIdCallback prettyCb: prettyFeatureCallbacks) {
            prettyCb.prettyFeat.resolveId(id, prettyCb.index);
        }
        prettyFeatureCallbacks = null;
    }

    /**
     * Provide a known id number to the given pretty feature when it is or becomes available.
     *
     * @param prettyFeat Pretty feature that desires to get it.
     * @param index Index in the values of the feature that want the id.
     */
    public void registerPrettyFeature(PrettyEFeat prettyFeat, int index) {
        if (id >= 0) {
            prettyFeat.resolveId(id, index);
        } else {
            prettyFeatureCallbacks.add(new RegisteredIdCallback(prettyFeat, index));
        }
    }

    /**
     * Record for storing that a pretty feature desires to get a known id number for its value at 'index'.
     *
     * @param prettyFeat Feature desiring the callback.
     * @param index Index to pass to the feature.
     */
    private static record RegisteredIdCallback(PrettyEFeat prettyFeat, int index) {
    }
}
