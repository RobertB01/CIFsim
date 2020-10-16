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

package org.eclipse.escet.chi.runtime.data.tests;

import static org.junit.Assert.assertEquals;

import java.util.Comparator;

import org.eclipse.escet.chi.runtime.data.IndexableDeque;
import org.junit.Test;

/** Tests on the {@link IndexableDeque} class. */
public class IndexableDequeTest {
    /** Test that head/tail administration works for size counts while adding to head. */
    @Test
    public void lengthTest1() {
        IndexableDeque<Integer> idq = new IndexableDeque<>();
        for (int n = 0; n < idq.capacity() + 2; n++) {
            assertEquals(0, idq.size());
            assertEquals(true, idq.isEmpty());
            assertEquals(0, idq.copyToArray(null).length);
            idq.prepend(n);
            assertEquals(1, idq.copyToArray(null).length);
            idq.removeTail();
        }
        assertEquals(0, idq.size());
        assertEquals(true, idq.isEmpty());
        assertEquals(0, idq.copyToArray(null).length);
    }

    /** Test that head/tail administration works for size counts while adding to tail. */
    @Test
    public void lengthTest2() {
        IndexableDeque<Integer> idq = new IndexableDeque<>();
        for (int n = 0; n < idq.capacity() + 2; n++) {
            assertEquals(0, idq.size());
            assertEquals(0, idq.copyToArray(null).length);
            assertEquals(true, idq.isEmpty());
            idq.append(n);
            assertEquals(1, idq.size());
            assertEquals(1, idq.copyToArray(null).length);
            idq.removeHead();
        }
        assertEquals(0, idq.size());
        assertEquals(true, idq.isEmpty());
        assertEquals(0, idq.copyToArray(null).length);
    }

    /**
     * Verify that the deque buffer contains the specified number of values, starting with the specified first number.
     *
     * @param idq Buffer to test.
     * @param first First value it should return.
     * @param increment Increase or decrease from one value to the next.
     * @param count Number of values it should contain.
     */
    private void testValues(IndexableDeque<Integer> idq, int first, int increment, int count) {
        for (Integer i: idq) {
            assertEquals(first, i.intValue());
            first += increment;
            count--;
        }
        assertEquals(0, count);
    }

    /**
     * Verify that the deque buffer contains the specified number of values, starting with the specified first number.
     *
     * @param idq Buffer to test.
     * @param first First value it should return.
     * @param increment Increase or decrease from one value to the next.
     * @param count Number of values it should contain.
     */
    private void testArray(IndexableDeque<Integer> idq, int first, int increment, int count) {
        Object[] a = idq.copyToArray(null);
        assertEquals(count, a.length); // Array should have the right length.
        for (Object obj: a) {
            Integer i = (Integer)obj;
            assertEquals(first, i.intValue());
            first += increment;
            count--;
        }
        assertEquals(0, count);
    }

    /** Check that data added remains available. */
    @Test
    public void sizeTailTest1() {
        int[] sizes = {1, 3, 5, 6, 7, 8, 9, 11, 13, 16, 22, 32, 60};
        IndexableDeque<Integer> idq = new IndexableDeque<>();
        int first = 11;
        int curSize = idq.size();
        int start = first;
        assertEquals(0, curSize);
        for (int destSize: sizes) {
            while (curSize < destSize) {
                idq.append(first);
                first++;
                curSize++;
            }
            testValues(idq, start, 1, destSize);
            testArray(idq, start, 1, destSize);
        }
    }

    /** Like sizeTailTest1, except shift head one position first. */
    @Test
    public void sizeTailTest2() {
        int[] sizes = {1, 3, 5, 6, 7, 8, 9, 11, 13, 16, 22, 32, 60};
        IndexableDeque<Integer> idq = new IndexableDeque<>();
        int first = 11;
        idq.append(first);
        first++;
        idq.removeHead();
        int curSize = idq.size();
        int start = first;
        assertEquals(0, curSize);
        for (int destSize: sizes) {
            while (curSize < destSize) {
                idq.append(first);
                first++;
                curSize++;
            }
            testValues(idq, start, 1, destSize);
            testArray(idq, start, 1, destSize);
        }
    }

    /** Check that data added remains available. */
    @Test
    public void sizeHeadTest1() {
        int[] sizes = {1, 3, 5, 6, 7, 8, 9, 11, 13, 16, 22, 32, 60};
        for (int b = 0; b < 8; b++) {
            IndexableDeque<Integer> idq = new IndexableDeque<>();
            // First crawl 'b' back.
            for (int i = 0; i < b; i++) {
                idq.append(5);
                idq.removeHead();
            }
            int first = 11;
            int curSize = idq.size();
            assertEquals(0, curSize);
            for (int destSize: sizes) {
                while (curSize < destSize) {
                    idq.prepend(first);
                    first++;
                    curSize++;
                }
                testValues(idq, first - 1, -1, destSize);
                testArray(idq, first - 1, -1, destSize);
            }
        }
    }

    /** Check that indexing works. */
    @Test
    public void indexingTest1() {
        String[] mydata = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
        IndexableDeque<String> idq = new IndexableDeque<>(12, mydata, 1);
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < mydata.length; j++) {
                assertEquals(mydata[j], idq.get(j));
                assertEquals(mydata[j], idq.get(j - mydata.length));
            }
            String s = mydata[0];
            System.arraycopy(mydata, 1, mydata, 0, mydata.length - 1);
            mydata[mydata.length - 1] = s;

            idq.append(idq.removeHead());
        }
    }

    /** Check that indexing works. */
    @Test
    public void indexingTest2() {
        String[] mydata = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
        IndexableDeque<String> idq = new IndexableDeque<>(12, mydata, 1);
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < mydata.length; j++) {
                assertEquals(mydata[j], idq.get(j));
                assertEquals(mydata[j], idq.get(j - mydata.length));
            }
            String s = mydata[mydata.length - 1];
            System.arraycopy(mydata, 0, mydata, 1, mydata.length - 1);
            mydata[0] = s;

            idq.prepend(idq.removeTail());
        }
    }

    /** Check that insertion in an empty list works. */
    @Test
    public void insertIndexEmptyText() {
        IndexableDeque<Integer> idq = new IndexableDeque<>();
        idq.insert(0, 0);
        assertEquals(Integer.valueOf(0), idq.get(0));
    }

    /** Test for inserting on index. */
    @Test
    public void insertIndexTest() {
        String[] mydata = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
        for (int head = 0; head < 12; head++) {
            for (int insertPoint = 0; insertPoint <= mydata.length; insertPoint++) {
                IndexableDeque<String> idq;
                idq = new IndexableDeque<>(12, mydata, head);
                idq.insert(insertPoint, null);
                int v = 0;
                assertEquals(mydata.length + 1, idq.size());
                for (int j = 0; j < mydata.length + 1; j++) {
                    if (j == insertPoint) {
                        assertEquals(null, idq.get(j));
                    } else {
                        assertEquals(mydata[v], idq.get(j));
                        v++;
                    }
                }
            }
        }
    }

    /** Insert values. */
    @Test
    public void insertValueEmptyText() {
        IndexableDeque<Integer> idq = new IndexableDeque<>();
        Comparator<Integer> cc = new IntegerComparator();
        idq.insert(0, cc);
        assertEquals(Integer.valueOf(0), idq.get(0));
    }

    /** Test for inserting on index. */
    @Test
    @SuppressWarnings("null")
    public void insertValueTest() {
        Comparator<Integer> cc = new IntegerComparator();
        Integer[] mydata = {1, 3, 5, 7, 9, 11, 13, 15, 17};
        for (int head = 0; head < 12; head++) {
            for (int insertPoint = 0; insertPoint < 20; insertPoint += 2) {
                IndexableDeque<Integer> idq;
                idq = new IndexableDeque<>(12, mydata, head);
                idq.insert(insertPoint, cc);
                Integer prev = null;
                assertEquals(mydata.length + 1, idq.size());
                for (int j = 0; j < mydata.length + 1; j++) {
                    if (j == 0) {
                        prev = idq.get(j);
                    } else {
                        Integer v = idq.get(j);
                        assertEquals(true, prev.intValue() < v.intValue());
                        prev = v;
                    }
                }
            }
        }
    }

    /** Test for removal on index. */
    @Test
    public void removeIndexTest() {
        String[] mydata = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
        for (int head = 0; head < 12; head++) {
            for (int removePoint = 0; removePoint < mydata.length; removePoint++) {
                IndexableDeque<String> idq;
                idq = new IndexableDeque<>(12, mydata, head);
                idq.remove(removePoint);
                int v = 0;
                assertEquals(mydata.length - 1, idq.size());
                for (int j = 0; j < mydata.length; j++) {
                    if (j != removePoint) {
                        assertEquals(mydata[j], idq.get(v));
                        v++;
                    }
                }
            }
        }
    }

    /** Sort test. */
    @Test
    public void sortTest() {
        Integer[] mydata = {4, 7, 68, 4, 3, 6, 8, 6, 23};
        int[] sorted = {3, 4, 4, 6, 6, 7, 8, 23, 68};
        Comparator<Integer> cc = new IntegerComparator();
        for (int head = 0; head < 12; head++) {
            IndexableDeque<Integer> idq;
            idq = new IndexableDeque<>(12, mydata, head);
            idq.sort(cc);
            for (int j = 0; j < sorted.length; j++) {
                assertEquals(idq.get(j).intValue(), sorted[j]);
            }
        }
    }
}
