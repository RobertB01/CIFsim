//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.app.framework;

import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.DisabledInfo;
import org.eclipse.osgi.service.resolver.PlatformAdmin;
import org.eclipse.osgi.service.resolver.State;
import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;

/** Utility class with OSGi platform and bundle related helper methods. */
public final class PlatformUtils {
    /** Constructor for the {@link PlatformUtils} class. */
    private PlatformUtils() {
        // Private constructor to make it a static class.
    }

    /**
     * Returns a readable representation of the bundles state. It has a special case for handling disabled bundles.
     *
     * <p>
     * The code of this method is based on the code from the
     * {@code org.eclipse.osgi.framework.internal.core.FrameworkCommandProvider#getStateName} method, which no longer
     * exists as of Eclipse Luna.
     * </p>
     *
     * @param bundle The bundle to get the readable representation of the state for.
     * @return A readable representation of the bundles state.
     */
    public static String getStateName(Bundle bundle) {
        int state = bundle.getState();
        switch (state) {
            case Bundle.UNINSTALLED:
                return "UNINSTALLED";
            case Bundle.INSTALLED:
                return isDisabled(bundle) ? "<DISABLED>" : "INSTALLED";
            case Bundle.RESOLVED:
                return "RESOLVED";
            case Bundle.STARTING:
                return "STARTING";
            case Bundle.STOPPING:
                return "STOPPING";
            case Bundle.ACTIVE:
                return "ACTIVE";
            default:
                return Integer.toHexString(state);
        }
    }

    /**
     * Returns a value indicating whether the given bundle is disabled.
     *
     * @param bundle The bundle to get the disabled status for.
     * @return A value indicating whether the given bundle is disabled.
     */
    private static boolean isDisabled(Bundle bundle) {
        boolean disabled = false;
        ServiceReference<PlatformAdmin> platformAdminRef = null;
        try {
            platformAdminRef = Activator.getContext().getServiceReference(PlatformAdmin.class);
            if (platformAdminRef != null) {
                PlatformAdmin platAdmin = Activator.getContext().getService(platformAdminRef);
                if (platAdmin != null) {
                    State state = platAdmin.getState(false);
                    BundleDescription bundleDesc = state.getBundle(bundle.getBundleId());
                    DisabledInfo[] disabledInfos = state.getDisabledInfos(bundleDesc);
                    if (disabledInfos != null && disabledInfos.length != 0) {
                        disabled = true;
                    }
                }
            }
        } finally {
            if (platformAdminRef != null) {
                Activator.getContext().ungetService(platformAdminRef);
            }
        }
        return disabled;
    }
}
