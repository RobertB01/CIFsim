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

package org.eclipse.escet.cif.cif2cif;

import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgOut;
import org.eclipse.escet.cif.metamodel.java.CifWalker;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/**
 * In-place transformation that removes position information.
 *
 * <p>
 * It may be useful to apply this transformation before other transformations, to make sure that position information
 * does not need to be processed. This may speed up subsequent transformations.
 * </p>
 */
public class RemovePositionInfo extends CifWalker implements CifToCifTransformation {
    @Override
    public void transform(Specification spec) {
        walkSpecification(spec);
    }

    @Override
    protected void preprocessPositionObject(PositionObject obj) {
        obj.setPosition(null);
    }

    @Override
    protected void preprocessSvgOut(SvgOut svgOut) {
        svgOut.setAttrTextPos(null);
    }
}
