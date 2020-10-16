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

package org.eclipse.escet.tooldef.common.tests;

import static org.eclipse.escet.common.emf.EMFHelper.deepclone;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Lists.set2list;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.tooldef.common.ToolDefTextUtils.typeToStr;
import static org.eclipse.escet.tooldef.common.ToolDefTypeUtils.areDistinguishableTypes;
import static org.eclipse.escet.tooldef.common.ToolDefTypeUtils.areEqualTypes;
import static org.eclipse.escet.tooldef.common.ToolDefTypeUtils.compareTypes;
import static org.eclipse.escet.tooldef.common.ToolDefTypeUtils.hashType;
import static org.eclipse.escet.tooldef.common.ToolDefTypeUtils.isSubType;
import static org.eclipse.escet.tooldef.common.ToolDefTypeUtils.isSuperType;
import static org.eclipse.escet.tooldef.common.ToolDefTypeUtils.mergeTypes;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newBoolType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newDoubleType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newIntType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newListType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newLongType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newMapType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newObjectType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newScript;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newSetType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newStringType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newToolDefTool;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newTupleType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newTypeParam;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newTypeParamRef;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.escet.common.emf.EMFHelper;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.tooldef.common.ToolDefTypeUtils;
import org.eclipse.escet.tooldef.metamodel.tooldef.Tool;
import org.eclipse.escet.tooldef.metamodel.tooldef.TypeParam;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.IntType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.ToolDefType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.TypeParamRef;
import org.junit.Test;

/** Unit tests for the {@link ToolDefTypeUtils} class. */
@SuppressWarnings("javadoc")
public class ToolDefTypeUtilsTest {
    /** Type "bool". */
    private static final ToolDefType BT = newBoolType(false, null);

    /** Type "int". */
    private static final ToolDefType IT = newIntType(false, null);

    /** Type "long". */
    private static final ToolDefType NT = newLongType(false, null);

    /** Type "double". */
    private static final ToolDefType DT = newDoubleType(false, null);

    /** Type "string". */
    private static final ToolDefType GT = newStringType(false, null);

    /** Type "object". */
    private static final ToolDefType OT = newObjectType(false, null);

    /** Type "bool?". */
    private static final ToolDefType NBT = newBoolType(true, null);

    /** Type "int?". */
    private static final ToolDefType NDT = newDoubleType(true, null);

    /** Type "long?". */
    private static final ToolDefType NNT = newLongType(true, null);

    /** Type "double?". */
    private static final ToolDefType NIT = newIntType(true, null);

    /** Type "string?". */
    private static final ToolDefType NGT = newStringType(true, null);

    /** Type "object?". */
    private static final ToolDefType NOT = newObjectType(true, null);

    /** Type "tuple (int, int)". */
    private static final ToolDefType TTII = newTupleType(list(c(IT), c(IT)), false, null);

    /** Type "tuple (int, int?)". */
    private static final ToolDefType TTINI = newTupleType(list(c(IT), c(NIT)), false, null);

    /** Type "tuple (int?, int)". */
    private static final ToolDefType TTNII = newTupleType(list(c(NIT), c(IT)), false, null);

    /** Type "tuple (int?, int?)". */
    private static final ToolDefType TTNINI = newTupleType(list(c(NIT), c(NIT)), false, null);

    /** Type "tuple?(int, int)". */
    private static final ToolDefType NTTII = newTupleType(list(c(IT), c(IT)), true, null);

    /** Type "tuple?(int, int?)". */
    private static final ToolDefType NTTINI = newTupleType(list(c(IT), c(NIT)), true, null);

    /** Type "tuple?(int?, int)". */
    private static final ToolDefType NTTNII = newTupleType(list(c(NIT), c(IT)), true, null);

    /** Type "tuple?(int?, int?)". */
    private static final ToolDefType NTTNINI = newTupleType(list(c(NIT), c(NIT)), true, null);

    /** Type "tuple (int, int, int)". */
    private static final ToolDefType TTIII = newTupleType(list(c(IT), c(IT), c(IT)), false, null);

    /** Type "list (int)". */
    private static final ToolDefType LTI = newListType(c(IT), false, null);

    /** Type "list (int?)". */
    private static final ToolDefType LTNI = newListType(c(NIT), false, null);

    /** Type "list?(int)". */
    private static final ToolDefType NLTI = newListType(c(IT), true, null);

    /** Type "list?(int?)". */
    private static final ToolDefType NLTNI = newListType(c(NIT), true, null);

    /** Type "set (int)". */
    private static final ToolDefType STI = newSetType(c(IT), false, null);

    /** Type "set (int?)". */
    private static final ToolDefType STNI = newSetType(c(NIT), false, null);

    /** Type "set?(int)". */
    private static final ToolDefType NSTI = newSetType(c(IT), true, null);

    /** Type "set?(int?)". */
    private static final ToolDefType NSTNI = newSetType(c(NIT), true, null);

    /** Type "map (int: int)". */
    private static final ToolDefType MTII = newMapType(c(IT), false, null, c(IT));

    /** Type "map (int: int?)". */
    private static final ToolDefType MTINI = newMapType(c(IT), false, null, c(NIT));

    /** Type "map (int?:int)". */
    private static final ToolDefType MTNII = newMapType(c(NIT), false, null, c(IT));

    /** Type "map (int?:int?)". */
    private static final ToolDefType MTNINI = newMapType(c(NIT), false, null, c(NIT));

    /** Type "map?(int: int)". */
    private static final ToolDefType NMTII = newMapType(c(IT), true, null, c(IT));

    /** Type "map?(int: int?)". */
    private static final ToolDefType NMTINI = newMapType(c(IT), true, null, c(NIT));

    /** Type "map?(int?:int)". */
    private static final ToolDefType NMTNII = newMapType(c(NIT), true, null, c(IT));

    /** Type "map?(int?:int?)". */
    private static final ToolDefType NMTNINI = newMapType(c(NIT), true, null, c(NIT));

    /** Type "T". **/
    private static final TypeParam T = newTypeParam("T", null);

    /** Type "U". **/
    private static final TypeParam U = newTypeParam("U", null);

    /** Type "T". **/
    private static final ToolDefType PTT = newTypeParamRef(false, null, T);

    /** Type "U". **/
    private static final ToolDefType PTU = newTypeParamRef(false, null, U);

    static {
        // Ensure declarations have a proper script containment, for proper absolute names.
        Tool tool = newToolDefTool("tool", null, null, null, null, list(T, U));
        newScript().getDeclarations().add(tool);
    }

    /** All the above types. */
    private static final Set<ToolDefType> TYPES = set(BT, IT, NT, DT, GT, OT, NBT, NIT, NNT, NDT, NGT, NOT, TTII, TTINI,
            TTNII, TTNINI, NTTII, NTTINI, NTTNII, NTTNINI, TTIII, LTI, LTNI, NLTI, NLTNI, STI, STNI, NSTI, NSTNI, MTII,
            MTINI, MTNII, MTNINI, NMTII, NMTINI, NMTNII, NMTNINI, PTT, PTU);

    /**
     * Same as {@link EMFHelper#deepclone}, but shorter name.
     *
     * @param <T> The type of object to deep-clone.
     * @param obj The {@link EObject} instance to deep-clone.
     * @return The deep-clone of the {@link EObject}.
     */
    private static <T extends EObject> T c(T obj) {
        return EMFHelper.deepclone(obj);
    }

    @Test
    public void testAreEqualTypes1() {
        for (ToolDefType t1: TYPES) {
            for (ToolDefType t2: TYPES) {
                String msg12 = fmt("equal(%s, %s)", typeToStr(t1), typeToStr(t2));
                String msg21 = fmt("equal(%s, %s)", typeToStr(t2), typeToStr(t1));
                assertEquals(msg12, t1 == t2, areEqualTypes(t1, t2));
                assertEquals(msg21, t1 == t2, areEqualTypes(t2, t1));
            }
        }
    }

    @Test
    public void testAreEqualTypes2() {
        IntType itype1 = newIntType(false, null);
        IntType itype2 = newIntType(false, null);
        IntType nitype1 = newIntType(true, null);
        IntType nitype2 = newIntType(true, null);
        List<IntType> types = list(itype1, itype2, nitype1, nitype2);
        for (IntType t1: types) {
            for (IntType t2: types) {
                String txt1 = typeToStr(t1);
                String txt2 = typeToStr(t2);
                String msg12 = fmt("equal(%s, %s)", txt1, txt2);
                String msg21 = fmt("equal(%s, %s)", txt2, txt1);
                assertEquals(msg12, txt1.equals(txt2), areEqualTypes(t1, t2));
                assertEquals(msg21, txt1.equals(txt2), areEqualTypes(t2, t1));
            }
        }
    }

    @Test
    public void testAreEqualTypes3() {
        TypeParamRef ptype1 = newTypeParamRef(false, null, T);
        TypeParamRef ptype2 = newTypeParamRef(false, null, T);
        TypeParamRef ptype3 = newTypeParamRef(false, null, U);
        TypeParamRef ptype4 = newTypeParamRef(false, null, U);
        List<TypeParamRef> types = list(ptype1, ptype2, ptype3, ptype4);
        for (TypeParamRef t1: types) {
            for (TypeParamRef t2: types) {
                String txt1 = typeToStr(t1);
                String txt2 = typeToStr(t2);
                String msg12 = fmt("equal(%s, %s)", txt1, txt2);
                String msg21 = fmt("equal(%s, %s)", txt2, txt1);
                assertEquals(msg12, txt1.equals(txt2), areEqualTypes(t1, t2));
                assertEquals(msg21, txt1.equals(txt2), areEqualTypes(t2, t1));
            }
        }
    }

    @Test
    public void testDistinguishableTypes() {
        for (ToolDefType t1: TYPES) {
            for (ToolDefType t2: TYPES) {
                String msg12 = fmt("distinguishable(%s, %s)", typeToStr(t1), typeToStr(t2));
                String msg21 = fmt("distinguishable(%s, %s)", typeToStr(t2), typeToStr(t1));
                boolean rslt;
                if (t1 instanceof TypeParamRef || t2 instanceof TypeParamRef) {
                    rslt = false;
                } else {
                    rslt = t1 != t2;
                }
                assertEquals(msg12, rslt, areDistinguishableTypes(t1, t2));
                assertEquals(msg21, rslt, areDistinguishableTypes(t2, t1));
            }
        }
    }

    @Test
    public void testSubSuperEqual() {
        for (ToolDefType t1: TYPES) {
            for (ToolDefType t2: TYPES) {
                String txt1 = typeToStr(t1);
                String txt2 = typeToStr(t2);
                String msg = fmt("test(%s, %s)", txt1, txt2);
                boolean equal = areEqualTypes(t1, t2);
                boolean sub = isSubType(t1, t2);
                boolean supr = isSuperType(t1, t2);
                Assert.implies(equal, sub, msg);
                Assert.implies(equal, supr, msg);
                Assert.implies(sub && supr, equal, msg);
            }
        }
    }

    @Test
    public void testIsSubType() {
        Map<ToolDefType, List<ToolDefType>> subs = map();

        for (ToolDefType t1: TYPES) {
            List<ToolDefType> sub = list();
            for (ToolDefType t2: TYPES) {
                if (isSubType(t2, t1)) {
                    sub.add(t2);
                }
            }
            subs.put(t1, sub);
        }

        String[] actuals = new String[TYPES.size()];
        int i = 0;
        for (Entry<ToolDefType, List<ToolDefType>> entry: subs.entrySet()) {
            ToolDefType type = entry.getKey();
            List<ToolDefType> types = entry.getValue();
            List<String> txts = listc(types.size());
            for (ToolDefType t: types) {
                txts.add(typeToStr(t));
            }
            actuals[i] = fmt("%-20s: %s", typeToStr(type), StringUtils.join(txts, ", "));
            // System.out.println("\"" + actuals[i] + "\",");
            i++;
        }

        String[] expecteds = { //
                "bool                : bool", //
                "int                 : int", //
                "long                : int, long", //
                "double              : int, long, double", //
                "string              : string", //
                "object              : bool, int, long, double, string, object, tuple(int, int), tuple(int, int?), tuple(int?, int), tuple(int?, int?), tuple(int, int, int), list int, list int?, set int, set int?, map(int:int), map(int:int?), map(int?:int), map(int?:int?)", //
                "bool?               : bool, bool?", //
                "int?                : int, int?", //
                "long?               : int, long, int?, long?", //
                "double?             : int, long, double, int?, long?, double?", //
                "string?             : string, string?", //
                "object?             : bool, int, long, double, string, object, bool?, int?, long?, double?, string?, object?, tuple(int, int), tuple(int, int?), tuple(int?, int), tuple(int?, int?), tuple?(int, int), tuple?(int, int?), tuple?(int?, int), tuple?(int?, int?), tuple(int, int, int), list int, list int?, list? int, list? int?, set int, set int?, set? int, set? int?, map(int:int), map(int:int?), map(int?:int), map(int?:int?), map?(int:int), map?(int:int?), map?(int?:int), map?(int?:int?), tool.T, tool.U", //
                "tuple(int, int)     : tuple(int, int)", //
                "tuple(int, int?)    : tuple(int, int), tuple(int, int?)", //
                "tuple(int?, int)    : tuple(int, int), tuple(int?, int)", //
                "tuple(int?, int?)   : tuple(int, int), tuple(int, int?), tuple(int?, int), tuple(int?, int?)", //
                "tuple?(int, int)    : tuple(int, int), tuple?(int, int)", //
                "tuple?(int, int?)   : tuple(int, int), tuple(int, int?), tuple?(int, int), tuple?(int, int?)", //
                "tuple?(int?, int)   : tuple(int, int), tuple(int?, int), tuple?(int, int), tuple?(int?, int)", //
                "tuple?(int?, int?)  : tuple(int, int), tuple(int, int?), tuple(int?, int), tuple(int?, int?), tuple?(int, int), tuple?(int, int?), tuple?(int?, int), tuple?(int?, int?)", //
                "tuple(int, int, int): tuple(int, int, int)", //
                "list int            : list int", //
                "list int?           : list int, list int?", //
                "list? int           : list int, list? int", //
                "list? int?          : list int, list int?, list? int, list? int?", //
                "set int             : set int", //
                "set int?            : set int, set int?", //
                "set? int            : set int, set? int", //
                "set? int?           : set int, set int?, set? int, set? int?", //
                "map(int:int)        : map(int:int)", //
                "map(int:int?)       : map(int:int), map(int:int?)", //
                "map(int?:int)       : map(int:int), map(int?:int)", //
                "map(int?:int?)      : map(int:int), map(int:int?), map(int?:int), map(int?:int?)", //
                "map?(int:int)       : map(int:int), map?(int:int)", //
                "map?(int:int?)      : map(int:int), map(int:int?), map?(int:int), map?(int:int?)", //
                "map?(int?:int)      : map(int:int), map(int?:int), map?(int:int), map?(int?:int)", //
                "map?(int?:int?)     : map(int:int), map(int:int?), map(int?:int), map(int?:int?), map?(int:int), map?(int:int?), map?(int?:int), map?(int?:int?)", //
                "tool.T              : tool.T", //
                "tool.U              : tool.U", //
        };

        assertArrayEquals(expecteds, actuals);
    }

    @Test
    public void testIsSuperType() {
        Map<ToolDefType, List<ToolDefType>> supers = map();

        for (ToolDefType t1: TYPES) {
            List<ToolDefType> supr = list();
            for (ToolDefType t2: TYPES) {
                if (isSuperType(t2, t1)) {
                    supr.add(t2);
                }
            }
            supers.put(t1, supr);
        }

        String[] actuals = new String[TYPES.size()];
        int i = 0;
        for (Entry<ToolDefType, List<ToolDefType>> entry: supers.entrySet()) {
            ToolDefType type = entry.getKey();
            List<ToolDefType> types = entry.getValue();
            List<String> txts = listc(types.size());
            for (ToolDefType t: types) {
                txts.add(typeToStr(t));
            }
            actuals[i] = fmt("%-20s: %s", typeToStr(type), StringUtils.join(txts, ", "));
            // System.out.println("\"" + actuals[i] + "\",");
            i++;
        }

        String[] expecteds = { //
                "bool                : bool, object, bool?, object?", //
                "int                 : int, long, double, object, int?, long?, double?, object?", //
                "long                : long, double, object, long?, double?, object?", //
                "double              : double, object, double?, object?", //
                "string              : string, object, string?, object?", //
                "object              : object, object?", //
                "bool?               : bool?, object?", //
                "int?                : int?, long?, double?, object?", //
                "long?               : long?, double?, object?", //
                "double?             : double?, object?", //
                "string?             : string?, object?", //
                "object?             : object?", //
                "tuple(int, int)     : object, object?, tuple(int, int), tuple(int, int?), tuple(int?, int), tuple(int?, int?), tuple?(int, int), tuple?(int, int?), tuple?(int?, int), tuple?(int?, int?)", //
                "tuple(int, int?)    : object, object?, tuple(int, int?), tuple(int?, int?), tuple?(int, int?), tuple?(int?, int?)", //
                "tuple(int?, int)    : object, object?, tuple(int?, int), tuple(int?, int?), tuple?(int?, int), tuple?(int?, int?)", //
                "tuple(int?, int?)   : object, object?, tuple(int?, int?), tuple?(int?, int?)", //
                "tuple?(int, int)    : object?, tuple?(int, int), tuple?(int, int?), tuple?(int?, int), tuple?(int?, int?)", //
                "tuple?(int, int?)   : object?, tuple?(int, int?), tuple?(int?, int?)", //
                "tuple?(int?, int)   : object?, tuple?(int?, int), tuple?(int?, int?)", //
                "tuple?(int?, int?)  : object?, tuple?(int?, int?)", //
                "tuple(int, int, int): object, object?, tuple(int, int, int)", //
                "list int            : object, object?, list int, list int?, list? int, list? int?", //
                "list int?           : object, object?, list int?, list? int?", //
                "list? int           : object?, list? int, list? int?", //
                "list? int?          : object?, list? int?", //
                "set int             : object, object?, set int, set int?, set? int, set? int?", //
                "set int?            : object, object?, set int?, set? int?", //
                "set? int            : object?, set? int, set? int?", //
                "set? int?           : object?, set? int?", //
                "map(int:int)        : object, object?, map(int:int), map(int:int?), map(int?:int), map(int?:int?), map?(int:int), map?(int:int?), map?(int?:int), map?(int?:int?)", //
                "map(int:int?)       : object, object?, map(int:int?), map(int?:int?), map?(int:int?), map?(int?:int?)", //
                "map(int?:int)       : object, object?, map(int?:int), map(int?:int?), map?(int?:int), map?(int?:int?)", //
                "map(int?:int?)      : object, object?, map(int?:int?), map?(int?:int?)", //
                "map?(int:int)       : object?, map?(int:int), map?(int:int?), map?(int?:int), map?(int?:int?)", //
                "map?(int:int?)      : object?, map?(int:int?), map?(int?:int?)", //
                "map?(int?:int)      : object?, map?(int?:int), map?(int?:int?)", //
                "map?(int?:int?)     : object?, map?(int?:int?)", //
                "tool.T              : object?, tool.T", //
                "tool.U              : object?, tool.U", //
        };

        assertArrayEquals(expecteds, actuals);
    }

    @Test
    public void testHashCode() {
        final Collection<ToolDefType> TYPES2 = deepclone(TYPES);
        for (ToolDefType t1: TYPES) {
            for (ToolDefType t2: TYPES2) {
                int h1 = hashType(t1);
                int h2 = hashType(t2);
                if (areEqualTypes(t1, t2)) {
                    assertEquals(true, h1 == h2);
                }
            }
        }
    }

    @Test
    public void testCompareTypes() {
        String expected = "bool, int, long, double, string, tuple(int, int), tuple(int, int?), "
                + "tuple(int?, int), tuple(int?, int?), tuple(int, int, int), "
                + "list int, list int?, set int, set int?, map(int:int), map(int:int?), "
                + "map(int?:int), map(int?:int?), object, bool?, int?, long?, double?, string?, "
                + "tuple?(int, int), tuple?(int, int?), tuple?(int?, int), tuple?(int?, int?), "
                + "list? int, list? int?, set? int, set? int?, map?(int:int), map?(int:int?), "
                + "map?(int?:int), map?(int?:int?), tool.T, tool.U, object?";

        // Original order.
        List<ToolDefType> types = set2list(TYPES);
        testCompareTypes2(expected, types);

        // Reverse sorted order.
        Collections.reverse(types);
        testCompareTypes2(expected, types);

        // Random orders.
        Random random = new Random(1234);
        for (int i = 0; i < 1000; i++) {
            Collections.shuffle(types, random);
            testCompareTypes2(expected, types);
        }
    }

    public void testCompareTypes2(String expected, List<ToolDefType> types) {
        // Sort.
        Collections.sort(types, new Comparator<ToolDefType>() {
            @Override
            public int compare(ToolDefType t1, ToolDefType t2) {
                return compareTypes(t1, t2);
            }
        });

        // Convert to text.
        List<String> actuals = listc(TYPES.size());
        for (ToolDefType t: types) {
            actuals.add(typeToStr(t));
        }

        // Compare.
        assertEquals(expected, StringUtils.join(actuals, ", "));
    }

    @Test
    public void testMergeTypes() {
        for (ToolDefType t1: TYPES) {
            for (ToolDefType t2: TYPES) {
                String txt = fmt("mergeTypes(%s, %s)", typeToStr(t1), typeToStr(t2));

                // Merge.
                ToolDefType merged = mergeTypes(t1, t2);

                // Fresh/cloned result.
                assertNotSame(txt, t1, merged);
                assertNotSame(txt, t2, merged);

                // Symmetric result.
                ToolDefType merged2 = mergeTypes(t2, t1);
                assertEquals(txt, true, areEqualTypes(merged, merged2));

                // Merged type contains both original types.
                assertEquals(txt, true, isSubType(t1, merged));
                assertEquals(txt, true, isSubType(t2, merged));

                // If equal, types themselves are the merge result.
                if (areEqualTypes(t1, t2)) {
                    assertEquals(txt, true, areEqualTypes(t1, merged));
                    assertEquals(txt, true, areEqualTypes(t2, merged));
                }

                // If one contains the other, larger one is the merge result.
                if (isSubType(t1, t2)) {
                    assertEquals(txt, true, areEqualTypes(t2, merged));
                } else if (isSubType(t2, t1)) {
                    assertEquals(txt, true, areEqualTypes(t1, merged));
                }

                // If result is one of them, that one contains the other.
                if (areEqualTypes(t2, merged)) {
                    assertEquals(txt, true, isSubType(t1, t2));
                } else if (areEqualTypes(t1, merged)) {
                    assertEquals(txt, true, isSubType(t2, t1));
                }

                // Make sure no type in between the merge result and both
                // other types. We test only for the types that we have here,
                // and not all infinite types that exist.
                for (ToolDefType t3: TYPES) {
                    String txt3 = fmt("%s, merged: %s, t3: %s", txt, typeToStr(merged), typeToStr(t3));

                    // Skip equal to t1, t2, or merged, as not in between.
                    if (areEqualTypes(t3, merged)) {
                        continue;
                    }
                    if (areEqualTypes(t3, t1)) {
                        continue;
                    }
                    if (areEqualTypes(t3, t2)) {
                        continue;
                    }

                    // Detect in between type.
                    boolean between1 = isSubType(t1, t3) && isSubType(t3, merged);
                    boolean between2 = isSubType(t2, t3) && isSubType(t3, merged);
                    assertEquals(txt3, false, between1 && between2);
                }
            }
        }
    }
}
