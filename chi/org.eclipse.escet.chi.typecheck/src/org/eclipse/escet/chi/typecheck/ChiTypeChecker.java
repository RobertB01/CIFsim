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

package org.eclipse.escet.chi.typecheck;

import static org.eclipse.escet.chi.typecheck.CheckSpecification.transSpecification;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import org.eclipse.escet.chi.metamodel.chi.ChiFactory;
import org.eclipse.escet.chi.metamodel.chi.Declaration;
import org.eclipse.escet.chi.metamodel.chi.Specification;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.java.exceptions.InputOutputException;
import org.eclipse.escet.common.typechecker.EcoreTypeChecker;

/** Type checker class, since an application always needs an object. */
public class ChiTypeChecker extends EcoreTypeChecker<List<Declaration>, Specification> {
    /**
     * File name of the EMF file to write if type checking succeeded. May be {@code null}, in which case nothing is
     * written.
     */
    private final String emfBaseName;

    /** Default constructor of the {@link ChiTypeChecker} class. */
    public ChiTypeChecker() {
        this(null);
    }

    /**
     * Construct a Chi type checker.
     *
     * @param emfBaseName Optional EMF model file name without file extension.
     */
    public ChiTypeChecker(String emfBaseName) {
        super();
        this.emfBaseName = emfBaseName;
    }

    @Override
    public Specification transRoot(List<Declaration> decls) {
        Specification spec = ChiFactory.eINSTANCE.createSpecification();
        spec.getDeclarations().addAll(decls);

        spec = transSpecification(spec, this);
        return spec;
    }

    @Override
    protected String getEcoreFileExtension() {
        return "chix";
    }

    @Override
    protected OutputStream getXmlOutputStream() {
        if (emfBaseName == null) {
            return null;
        }

        String path = emfBaseName + "." + getEcoreFileExtension();
        path = Paths.resolve(path);
        try {
            return new BufferedOutputStream(new FileOutputStream(path));
        } catch (FileNotFoundException e) {
            String msg = "Failed to write generated EMF model.";
            throw new InputOutputException(msg, e);
        }
    }
}
