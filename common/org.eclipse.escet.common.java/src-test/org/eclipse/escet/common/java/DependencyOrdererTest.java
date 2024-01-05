//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.java;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Strings.str;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

/** Unit tests for the {@link DependencyOrderer} class. */
public class DependencyOrdererTest {
    /** Object 1. */
    private static final TestObject OBJ1 = new TestObject(1);

    /** Object 2. */
    private static final TestObject OBJ2 = new TestObject(2);

    /** Object 3. */
    private static final TestObject OBJ3 = new TestObject(3);

    /** Object 4. */
    private static final TestObject OBJ4 = new TestObject(4);

    /** Object 5. */
    private static final TestObject OBJ5 = new TestObject(5);

    /** Object 6. */
    private static final TestObject OBJ6 = new TestObject(6);

    /** Object 7. */
    private static final TestObject OBJ7 = new TestObject(7);

    /** Object 8. */
    private static final TestObject OBJ8 = new TestObject(8);

    /** Test empty input (no objects). */
    @Test
    public void testEmpty() {
        Map<TestObject, Set<TestObject>> directDeps = map();
        TestOrderer orderer = new TestOrderer(directDeps);
        List<TestObject> rslt = orderer.computeOrder();
        assertEquals(list(), rslt);
    }

    /** Test immutable non-empty set of direct dependencies. */
    @Test
    public void testImmutableDepSet() {
        Map<TestObject, Set<TestObject>> directDeps = map();
        directDeps.put(OBJ1, Collections.unmodifiableSet(set(OBJ2)));
        directDeps.put(OBJ2, set());

        TestOrderer orderer = new TestOrderer(directDeps);
        orderer.addObject(OBJ2);
        orderer.addObject(OBJ1);

        assertThrows(UnsupportedOperationException.class, () -> orderer.computeOrder());
    }

    /** Test single object without dependencies. */
    @Test
    public void testSingleNoDep() {
        Map<TestObject, Set<TestObject>> directDeps = map();
        directDeps.put(OBJ1, set());

        TestOrderer orderer = new TestOrderer(directDeps);
        orderer.addObject(OBJ1);

        List<TestObject> rslt = orderer.computeOrder();
        assertEquals(list(OBJ1), rslt);
    }

    /** Test single object, with single dependency, that is not added. */
    @Test
    public void testSingleNonAddedDep() {
        Map<TestObject, Set<TestObject>> directDeps = map();
        directDeps.put(OBJ1, set(OBJ2));

        TestOrderer orderer = new TestOrderer(directDeps);
        orderer.addObject(OBJ1);

        List<TestObject> rslt = orderer.computeOrder();
        assertEquals(list(OBJ1), rslt);
    }

    /** Test restrict, for a more complex combination of dependencies. */
    @Test
    public void testComplexRestrict() {
        Map<TestObject, Set<TestObject>> directDeps = map();
        directDeps.put(OBJ1, set(OBJ2, OBJ5, OBJ4));
        directDeps.put(OBJ2, set(OBJ3));
        directDeps.put(OBJ3, set());
        directDeps.put(OBJ4, set(OBJ2, OBJ5, OBJ7));
        directDeps.put(OBJ5, set(OBJ2));
        directDeps.put(OBJ6, set(OBJ8));
        directDeps.put(OBJ7, set(OBJ6));
        directDeps.put(OBJ8, set());

        TestOrderer orderer = new TestOrderer(directDeps);
        orderer.addObject(OBJ1);
        orderer.addObject(OBJ2);
        orderer.addObject(OBJ3);
        orderer.addObject(OBJ4);
        orderer.addObject(OBJ5);

        List<TestObject> rslt = orderer.computeOrder();
        assertEquals(list(OBJ3, OBJ2, OBJ5, OBJ4, OBJ1), rslt);
    }

    /** Test non-restrict, for a more complex combination of dependencies. */
    @Test
    public void testComplexNonRestrict() {
        Map<TestObject, Set<TestObject>> directDeps = map();
        directDeps.put(OBJ1, set(OBJ2, OBJ5, OBJ4));
        directDeps.put(OBJ2, set(OBJ3));
        directDeps.put(OBJ3, set());
        directDeps.put(OBJ4, set(OBJ2, OBJ5, OBJ7));
        directDeps.put(OBJ5, set(OBJ2));
        directDeps.put(OBJ6, set(OBJ8));
        directDeps.put(OBJ7, set(OBJ6));
        directDeps.put(OBJ8, set(OBJ5));

        TestOrderer orderer = new TestOrderer(directDeps);
        orderer.addObject(OBJ1);
        orderer.addObject(OBJ2);
        orderer.addObject(OBJ3);
        orderer.addObject(OBJ4);
        orderer.addObject(OBJ5);
        orderer.addObject(OBJ6);
        orderer.addObject(OBJ7);
        orderer.addObject(OBJ8);

        List<TestObject> rslt = orderer.computeOrder(false);
        assertEquals(list(OBJ3, OBJ2, OBJ5, OBJ8, OBJ6, OBJ7, OBJ4, OBJ1), rslt);
    }

    /** Test a more complex combination of dependencies. */
    @Test
    public void testMultipleDeps() {
        Map<TestObject, Set<TestObject>> directDeps = map();
        directDeps.put(OBJ1, set(OBJ2, OBJ5, OBJ4));
        directDeps.put(OBJ2, set(OBJ3));
        directDeps.put(OBJ3, set());
        directDeps.put(OBJ4, set(OBJ2, OBJ5));
        directDeps.put(OBJ5, set(OBJ2));

        TestOrderer orderer = new TestOrderer(directDeps);
        orderer.addObject(OBJ1);
        orderer.addObject(OBJ2);
        orderer.addObject(OBJ3);
        orderer.addObject(OBJ4);
        orderer.addObject(OBJ5);

        List<TestObject> rslt = orderer.computeOrder();
        assertEquals(list(OBJ3, OBJ2, OBJ5, OBJ4, OBJ1), rslt);
    }

    /** Test pre-adding objects, versus adding them when starting. */
    @Test
    public void testPreAddComputeAdd() {
        Map<TestObject, Set<TestObject>> directDeps = map();
        directDeps.put(OBJ1, set(OBJ2));
        directDeps.put(OBJ2, set(OBJ3));
        directDeps.put(OBJ3, set());

        TestOrderer orderer = new TestOrderer(directDeps);
        orderer.addObject(OBJ1);

        List<TestObject> rslt = orderer.computeOrder(list(OBJ2, OBJ3));
        assertEquals(list(OBJ3, OBJ2, OBJ1), rslt);
    }

    /** Test duplicate adds, for pre-adding, and for adding when starting. */
    @Test
    public void testDuplicateAdd() {
        Map<TestObject, Set<TestObject>> directDeps = map();
        directDeps.put(OBJ1, set(OBJ2));
        directDeps.put(OBJ2, set(OBJ3));
        directDeps.put(OBJ3, set());

        TestOrderer orderer = new TestOrderer(directDeps);
        orderer.addObject(OBJ1);
        orderer.addObject(OBJ1);

        List<TestObject> rslt;
        rslt = orderer.computeOrder(list(OBJ1, OBJ2, OBJ2, OBJ3));
        assertEquals(list(OBJ3, OBJ2, OBJ1), rslt);
    }

    /** Test self-cycle. */
    @Test
    public void testCycleSelf() {
        Map<TestObject, Set<TestObject>> directDeps = map();
        directDeps.put(OBJ1, set(OBJ2, OBJ3, OBJ1));
        directDeps.put(OBJ2, set());

        TestOrderer orderer = new TestOrderer(directDeps);
        orderer.addObject(OBJ1);
        orderer.addObject(OBJ2);

        List<TestObject> rslt = orderer.computeOrder();
        assertEquals(null, rslt);

        rslt = orderer.getCycle();
        assertEquals(list(OBJ1), rslt);
    }

    /** Test cycle of two objects. */
    @Test
    public void testCycle2() {
        Map<TestObject, Set<TestObject>> directDeps = map();
        directDeps.put(OBJ1, set(OBJ2, OBJ3));
        directDeps.put(OBJ2, set(OBJ4, OBJ1, OBJ5));

        TestOrderer orderer = new TestOrderer(directDeps);
        orderer.addObject(OBJ1);
        orderer.addObject(OBJ2);

        List<TestObject> rslt = orderer.computeOrder();
        assertEquals(null, rslt);

        rslt = orderer.getCycle();
        assertEquals(2, rslt.size());
        TestObject t0 = rslt.get(0);
        TestObject t1 = rslt.get(1);
        assertTrue((t0.equals(OBJ1) && t1.equals(OBJ2)) || (t0.equals(OBJ2) && t1.equals(OBJ1)));
    }

    /** Test cycle of three objects. */
    @Test
    public void testCycle3() {
        Map<TestObject, Set<TestObject>> directDeps = map();
        directDeps.put(OBJ1, set(OBJ2));
        directDeps.put(OBJ2, set(OBJ3));
        directDeps.put(OBJ3, set(OBJ1));

        TestOrderer orderer = new TestOrderer(directDeps);
        orderer.addObject(OBJ1);
        orderer.addObject(OBJ2);
        orderer.addObject(OBJ3);

        List<TestObject> rslt = orderer.computeOrder();
        assertEquals(null, rslt);

        rslt = orderer.getCycle();
        assertEquals(3, rslt.size());
        TestObject t0 = rslt.get(0);
        TestObject t1 = rslt.get(1);
        TestObject t2 = rslt.get(2);
        assertTrue((t0.equals(OBJ1) && t1.equals(OBJ2) && t2.equals(OBJ3))
                || (t0.equals(OBJ2) && t1.equals(OBJ3) && t2.equals(OBJ1))
                || (t0.equals(OBJ3) && t1.equals(OBJ1) && t2.equals(OBJ2)));
    }

    /** Test cycle of two objects down-stream chains last in the map. */
    @Test
    public void testCycle4() {
        Map<TestObject, Set<TestObject>> directDeps = map();
        directDeps.put(OBJ1, set(OBJ2, OBJ3)); // Cycle OBJ1 <-> OBJ2.
        directDeps.put(OBJ2, set(OBJ1));

        directDeps.put(OBJ3, set()); // Down-stream chain.

        TestOrderer orderer = new TestOrderer(directDeps);
        orderer.addObject(OBJ1);
        orderer.addObject(OBJ2);
        orderer.addObject(OBJ3);

        List<TestObject> rslt = orderer.computeOrder();
        assertEquals(null, rslt);

        rslt = orderer.getCycle();
        assertEquals(2, rslt.size());
        TestObject t0 = rslt.get(0);
        TestObject t1 = rslt.get(1);
        assertTrue((t0.equals(OBJ1) && t1.equals(OBJ2)) || (t0.equals(OBJ2) && t1.equals(OBJ1)));
    }

    /** Test cycle of two objects down-stream chains first in the map. */
    @Test
    public void testCycle5() {
        Map<TestObject, Set<TestObject>> directDeps = map();
        directDeps.put(OBJ1, set(OBJ2, OBJ3)); // Cycle OBJ1 <-> OBJ2.
        directDeps.put(OBJ2, set(OBJ1));

        directDeps.put(OBJ3, set()); // Down-stream chain.

        TestOrderer orderer = new TestOrderer(directDeps);
        orderer.addObject(OBJ3);
        orderer.addObject(OBJ1);
        orderer.addObject(OBJ2);

        List<TestObject> rslt = orderer.computeOrder();
        assertEquals(null, rslt);

        rslt = orderer.getCycle();
        assertEquals(2, rslt.size());
        TestObject t0 = rslt.get(0);
        TestObject t1 = rslt.get(1);
        assertTrue((t0.equals(OBJ1) && t1.equals(OBJ2)) || (t0.equals(OBJ2) && t1.equals(OBJ1)));
    }

    /** Test orderer, for the unit tests. */
    private static class TestOrderer extends DependencyOrderer<TestObject> {
        /** Mapping from objects to their direct dependencies. */
        private final Map<TestObject, Set<TestObject>> directDeps;

        /**
         * Constructor for the {@link TestOrderer} class.
         *
         * @param directDeps Mapping from objects to their direct dependencies.
         */
        public TestOrderer(Map<TestObject, Set<TestObject>> directDeps) {
            this.directDeps = directDeps;
        }

        @Override
        protected Set<TestObject> findDirectDependencies(TestObject obj) {
            Set<TestObject> rslt = directDeps.get(obj);
            Assert.notNull(rslt);
            return rslt;
        }
    }

    /** Test object. */
    private static class TestObject {
        /** Test value. */
        private final int x;

        /**
         * Constructor for the {@link TestObject} class.
         *
         * @param x Test value.
         */
        public TestObject(int x) {
            this.x = x;
        }

        @Override
        public String toString() {
            return str(x);
        }
    }
}
