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

package org.eclipse.escet.common.emf;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.junit.jupiter.api.Test;

/** Unit tests for the {@link EMFPath} class. */
@SuppressWarnings("javadoc")
public class EMFPathTest {
    @Test
    public void testRootWithNameNoFeature() {
        EPackage pkg = EcoreFactory.eINSTANCE.createEPackage();
        pkg.setName("rootPackage");

        EMFPath path1 = new EMFPath(pkg, null);
        EMFPath path2 = new EMFPath(pkg, null, pkg);
        assertEquals("EPackage(name=rootPackage)", path1.toString());
        assertEquals(path1.toString(), path2.toString());
    }

    @Test
    public void testRootNoNameNoFeature() {
        EPackage pkg = EcoreFactory.eINSTANCE.createEPackage();

        EMFPath path1 = new EMFPath(pkg, null);
        EMFPath path2 = new EMFPath(pkg, null, pkg);
        assertEquals("EPackage", path1.toString());
        assertEquals(path1.toString(), path2.toString());
    }

    @Test
    public void testRootWithNameWithFeature() {
        EPackage pkg = EcoreFactory.eINSTANCE.createEPackage();
        pkg.setName("rootPackage");

        EStructuralFeature feat = pkg.eClass().getEStructuralFeature("name");
        EMFPath path1 = new EMFPath(pkg, feat);
        EMFPath path2 = new EMFPath(pkg, feat, pkg);

        assertEquals("EPackage(name=rootPackage).name / \"rootPackage\"", path1.toString());
        assertEquals(path1.toString(), path2.toString());
    }

    @Test
    public void testRootNoNameWithFeature() {
        EPackage pkg = EcoreFactory.eINSTANCE.createEPackage();

        EStructuralFeature feat = pkg.eClass().getEStructuralFeature("name");
        EMFPath path1 = new EMFPath(pkg, feat);
        EMFPath path2 = new EMFPath(pkg, feat, pkg);

        assertEquals("EPackage.name", path1.toString());
        assertEquals(path1.toString(), path2.toString());
    }

    @Test
    public void testTwoLevelsWithNameNoFeature() {
        EPackage pkg = EcoreFactory.eINSTANCE.createEPackage();
        pkg.setName("rootPackage");
        EClass cls = EcoreFactory.eINSTANCE.createEClass();
        pkg.getEClassifiers().add(cls);

        EMFPath path1 = new EMFPath(cls, null);
        EMFPath path2 = new EMFPath(cls, null, pkg);
        assertEquals("EPackage(name=rootPackage).eClassifiers[0] / EClass", path1.toString());
        assertEquals(path1.toString(), path2.toString());
    }

    @Test
    public void testTwoLevelsNoNameNoFeature() {
        EPackage pkg = EcoreFactory.eINSTANCE.createEPackage();
        EClass cls = EcoreFactory.eINSTANCE.createEClass();
        pkg.getEClassifiers().add(cls);

        EMFPath path1 = new EMFPath(cls, null);
        EMFPath path2 = new EMFPath(cls, null, pkg);
        assertEquals("EPackage.eClassifiers[0] / EClass", path1.toString());
        assertEquals(path1.toString(), path2.toString());
    }

    @Test
    public void testTwoLevelsWithNameWithFeature() {
        EPackage pkg = EcoreFactory.eINSTANCE.createEPackage();
        pkg.setName("rootPackage");
        EClass cls = EcoreFactory.eINSTANCE.createEClass();
        pkg.getEClassifiers().add(cls);

        EStructuralFeature feat = cls.eClass().getEStructuralFeature("eSuperTypes");
        EMFPath path1 = new EMFPath(cls, feat);
        EMFPath path2 = new EMFPath(cls, feat, pkg);

        assertEquals("EPackage(name=rootPackage).eClassifiers[0] / EClass.eSuperTypes / []", path1.toString());
        assertEquals(path1.toString(), path2.toString());
    }

    @Test
    public void testTwoLevelsNoNameWithFeature() {
        EPackage pkg = EcoreFactory.eINSTANCE.createEPackage();
        EClass cls = EcoreFactory.eINSTANCE.createEClass();
        pkg.getEClassifiers().add(cls);

        EStructuralFeature feat = cls.eClass().getEStructuralFeature("eSuperTypes");
        EMFPath path1 = new EMFPath(cls, feat);
        EMFPath path2 = new EMFPath(cls, feat, pkg);

        assertEquals("EPackage.eClassifiers[0] / EClass.eSuperTypes / []", path1.toString());
        assertEquals(path1.toString(), path2.toString());
    }

    @Test
    public void testIntermediateRootToSelf() {
        EPackage pkg = EcoreFactory.eINSTANCE.createEPackage();
        EClass cls = EcoreFactory.eINSTANCE.createEClass();
        cls.setName("XYZ");
        pkg.getEClassifiers().add(cls);
        EReference ref = EcoreFactory.eINSTANCE.createEReference();
        cls.getEStructuralFeatures().add(ref);

        EMFPath path = new EMFPath(cls, null, cls);

        assertEquals("EClass(name=XYZ)", path.toString());
    }

    @Test
    public void testLeafToIntermediateRoot() {
        EPackage pkg = EcoreFactory.eINSTANCE.createEPackage();
        EClass cls = EcoreFactory.eINSTANCE.createEClass();
        pkg.getEClassifiers().add(cls);
        EReference ref = EcoreFactory.eINSTANCE.createEReference();
        cls.getEStructuralFeatures().add(ref);

        EMFPath path = new EMFPath(ref, null, cls);

        assertEquals("EClass.eStructuralFeatures[0] / EReference", path.toString());
    }

    @Test
    @SuppressWarnings("unused")
    public void testPathConstructionInvalidRoot() {
        EPackage pkg = EcoreFactory.eINSTANCE.createEPackage();
        EClass cls = EcoreFactory.eINSTANCE.createEClass();
        pkg.getEClassifiers().add(cls);

        // assertThrows(AssertionError.class, () -> new EMFPath(pkg, null, cls));
    }

    @Test
    public void testResolveAgainst() {
        // pkg1 -> cls1 -> ref1
        EPackage pkg1 = EcoreFactory.eINSTANCE.createEPackage();
        EClass cls1 = EcoreFactory.eINSTANCE.createEClass();
        pkg1.getEClassifiers().add(cls1);
        EReference ref1 = EcoreFactory.eINSTANCE.createEReference();
        cls1.getEStructuralFeatures().add(ref1);

        // pkg2 -> cls2 -> ref2
        EPackage pkg2 = EcoreFactory.eINSTANCE.createEPackage();
        EClass cls2 = EcoreFactory.eINSTANCE.createEClass();
        pkg2.getEClassifiers().add(cls2);
        EReference ref2 = EcoreFactory.eINSTANCE.createEReference();
        cls2.getEStructuralFeatures().add(ref2);

        EMFPath path = new EMFPath(ref1, null);
        Object rslt = path.resolveAgainst(pkg2);
        assertSame(ref2, rslt);
    }

    @Test
    public void testResolveAgainstIntermediateRoot() {
        // pkg1 -> cls1 -> ref1
        EPackage pkg1 = EcoreFactory.eINSTANCE.createEPackage();
        EClass cls1 = EcoreFactory.eINSTANCE.createEClass();
        pkg1.getEClassifiers().add(cls1);
        EReference ref1 = EcoreFactory.eINSTANCE.createEReference();
        cls1.getEStructuralFeatures().add(ref1);

        // pkg2 -> cls2 -> ref2
        EPackage pkg2 = EcoreFactory.eINSTANCE.createEPackage();
        EClass cls2 = EcoreFactory.eINSTANCE.createEClass();
        pkg2.getEClassifiers().add(cls2);
        EReference ref2 = EcoreFactory.eINSTANCE.createEReference();
        cls2.getEStructuralFeatures().add(ref2);

        EMFPath path = new EMFPath(ref1, null, cls1);
        Object rslt = path.resolveAgainst(cls2);
        assertSame(ref2, rslt);
    }

    @Test
    public void testResolveAgainstInvalidFeature() {
        // pkg1 -> cls1
        EPackage pkg1 = EcoreFactory.eINSTANCE.createEPackage();
        EClass cls1 = EcoreFactory.eINSTANCE.createEClass();
        pkg1.getEClassifiers().add(cls1);

        // cls2
        EClass cls2 = EcoreFactory.eINSTANCE.createEClass();

        EMFPath path = new EMFPath(cls1, null);
        // assertThrows(IllegalArgumentException.class, () -> path.resolveAgainst(cls2));
    }

    @Test
    public void testResolveAgainstFeatureOnNull() {
        // ref1 -> gentype1
        EReference ref1 = EcoreFactory.eINSTANCE.createEReference();
        EGenericType gentype1 = EcoreFactory.eINSTANCE.createEGenericType();
        ref1.setEGenericType(gentype1);

        // ref2 -> null
        EReference ref2 = EcoreFactory.eINSTANCE.createEReference();

        EStructuralFeature feat = gentype1.eClass().getEStructuralFeature("eLowerBound");
        EMFPath path = new EMFPath(gentype1, feat);
        // assertThrows(IllegalArgumentException.class, () -> path.resolveAgainst(ref2));
    }

    @Test
    public void testResolveAgainstFeatureInvalidIndex() {
        // pkg1 -> cls1 -> ref1
        EPackage pkg1 = EcoreFactory.eINSTANCE.createEPackage();
        EClass cls1 = EcoreFactory.eINSTANCE.createEClass();
        pkg1.getEClassifiers().add(cls1);
        EReference ref1 = EcoreFactory.eINSTANCE.createEReference();
        cls1.getEStructuralFeatures().add(ref1);

        // pkg2 -> []
        EPackage pkg2 = EcoreFactory.eINSTANCE.createEPackage();

        EMFPath path = new EMFPath(ref1, null);
        // assertThrows(IllegalArgumentException.class, () -> path.resolveAgainst(pkg2));
    }
}
