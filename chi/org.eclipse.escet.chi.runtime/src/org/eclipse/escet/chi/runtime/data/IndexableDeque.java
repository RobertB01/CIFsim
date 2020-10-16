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

package org.eclipse.escet.chi.runtime.data;

import static org.eclipse.escet.common.java.Assert.check;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.eclipse.escet.chi.runtime.ChiSimulatorException;

/**
 * Templated round-robin buffer class.
 *
 * <p>
 * Works much like the Deque classes in the Collections framework, but allows indexing as well as head and tail
 * operations.
 * </p>
 *
 * @param <T> Element type of the deque.
 */
public class IndexableDeque<T> implements Iterable<T> {
    /** Minimal size of the buffer. */
    private static final int DEFAULT_MINIMAL_SIZE = 8;

    /** Data storage. */
    private T[] data;

    /** Index of the head element. */
    private int head;

    /**
     * Index one further then the last element.
     *
     * <p>
     * This index should be unequal to {@link #head} for non-empty lists, which implies that {@link #data} is never
     * entirely used.
     * </p>
     */
    private int tail;

    /** Default constructor of the {@link IndexableDeque} class. */
    public IndexableDeque() {
        this(0);
    }

    /**
     * Constructor of the {@link IndexableDeque} class.
     *
     * @param initElements Initial number of elements expected in the list.
     */
    @SuppressWarnings("unchecked")
    public IndexableDeque(int initElements) {
        int sz = computeSize(0, initElements + 1);
        data = (T[])new Object[sz];
        clear();
    }

    /**
     * Copy constructor of the {@link IndexableDeque} class, makes a shallow copy of the 'orig' object.
     *
     * @param orig List to copy.
     */
    @SuppressWarnings("unchecked")
    public IndexableDeque(IndexableDeque<T> orig) {
        data = (T[])new Object[orig.data.length];
        head = orig.head;
        tail = orig.tail;
        System.arraycopy(orig.data, 0, data, 0, orig.data.length);
    }

    /**
     * Constructor providing precise control on construction of the internal data structures. Intended to be used for
     * testing only, as the method signature closely follows internal implementation details.
     *
     * @param bufferSize Size of the internal buffer.
     * @param data Data of the list.
     * @param head Index of the head element in the internal buffer.
     */
    @SuppressWarnings("unchecked")
    public IndexableDeque(int bufferSize, T[] data, int head) {
        check(bufferSize > data.length);
        check(head >= 0 && head < bufferSize);

        this.data = (T[])new Object[bufferSize];
        clear();

        this.head = head;
        tail = head;
        for (int i = 0; i < data.length; i++) {
            this.data[tail] = data[i];
            tail = (tail == this.data.length - 1) ? 0 : tail + 1;
        }
    }

    /** Clear the list. */
    private void clear() {
        // Prevent reference leaking.
        for (int i = 0; i < data.length; i++) {
            data[i] = null;
        }
        head = 0;
        tail = 0;
    }

    /**
     * Compute a new size for the buffer.
     *
     * @param currentSize Current size of the buffer.
     * @param minimalSize Minimal required buffer size.
     * @return New size of the buffer.
     */
    private static int computeSize(int currentSize, int minimalSize) {
        if (currentSize < DEFAULT_MINIMAL_SIZE) {
            currentSize = DEFAULT_MINIMAL_SIZE;
        }

        while (currentSize < minimalSize) {
            if (currentSize < 1024) {
                currentSize *= 2;
            } else {
                currentSize += currentSize / 16;
            }

            check(currentSize >= DEFAULT_MINIMAL_SIZE); // Overflow check.
        }
        return currentSize;
    }

    /**
     * Enlarge internal buffer such that at least 'minimalElements' values fit in the buffer.
     *
     * @param minimalElements Minimal number of elements expected in the list.
     */
    @SuppressWarnings("unchecked")
    private void enlargeStorage(int minimalElements) {
        int newSize = computeSize(data.length, minimalElements + 1);
        T[] newData = (T[])new Object[newSize];
        // Prevent reference leaking.
        for (int i = 0; i < newSize; i++) {
            newData[i] = null;
        }

        if (head <= tail) {
            // One contiguous block from head up-to and excluding tail.
            // To ensure it will actually fit in the new array, the data gets
            // moved to the start of the array.
            System.arraycopy(data, head, newData, 0, tail - head);
            tail = tail - head;
            head = 0;
        } else {
            // One block from 0 up-to and excluding tail, and
            // one block from head to size().
            System.arraycopy(data, 0, newData, 0, tail);
            int count = data.length - head;
            int newHead = newSize - count;
            check(newHead > tail);
            System.arraycopy(data, head, newData, newHead, count);
            head = newHead;
        }
        data = newData;
    }

    /**
     * Test whether the list is empty.
     *
     * @return Whether the list is empty or not.
     */
    public boolean isEmpty() {
        return head == tail;
    }

    /**
     * Get the number of elements in the list.
     *
     * @return The number of elements in the list.
     */
    public int size() {
        if (head <= tail) {
            return tail - head;
        }
        return tail + (data.length - head);
    }

    /**
     * Get the size of the internal buffer. Method is intended to be used for testing only, as it closely follows
     * internal implementation details.
     *
     * @return Whether the list is empty or not.
     */
    public int capacity() {
        return data.length;
    }

    /**
     * Normalizes the index to a non-negative value.
     *
     * @param index Original index. May be less than zero, which indicates the index starts from the end of the list.
     * @param allowSize Allow index to point to {@link #size()}.
     * @return The normalized index.
     * @throws ChiSimulatorException When the given index is too small (that is, more negative than the length of the
     *     list), or too large (index access values beyond the end of the list).
     */
    public int normalizeIndex(int index, boolean allowSize) {
        int sz = size();
        if (index < 0) {
            index += sz;
            if (index < 0) {
                String msg = fmt("Array index is too small (index %d does not exist for a list with %d elements).",
                        index - sz, size());
                throw new ChiSimulatorException(msg);
            }
            return index;
        }
        if (index > sz || (!allowSize && index == sz)) {
            String msg = fmt("Array index is too large (index %d does not exist for a list with %d elements).", index,
                    size());
            throw new ChiSimulatorException(msg);
        }
        return index;
    }

    /**
     * Get the value at the given index.
     *
     * @param index Index in the list.
     * @return Value of the element at the given index.
     */
    public T get(int index) {
        index = normalizeIndex(index, false) + head;
        if (head > tail && index >= data.length) {
            return data[index - data.length];
        }
        return data[index];
    }

    /**
     * Set a value at the given index.
     *
     * @param index Index in the list.
     * @param newVal New value to write at the given index.
     */
    public void set(int index, T newVal) {
        index = normalizeIndex(index, false) + head;
        if (head > tail && index >= data.length) {
            data[index - data.length] = newVal;
            return;
        }
        data[index] = newVal;
    }

    /**
     * Prepend an element at the front of the list.
     *
     * @param value Value to prepend.
     */
    public void prepend(T value) {
        int prev = head - 1;
        if (prev < 0) {
            prev = data.length - 1;
        }

        if (prev != tail) {
            data[prev] = value;
            head = prev;
        } else {
            enlargeStorage(data.length);
            head = (head == 0) ? data.length - 1 : head - 1;
            data[head] = value;
        }
    }

    /**
     * Append an element at the end of the list.
     *
     * @param value Value to append.
     */
    public void append(T value) {
        int next = tail + 1;
        if (next == data.length) {
            next = 0;
        }

        if (next != head) {
            data[tail] = value;
            tail = next;
        } else {
            enlargeStorage(data.length);
            data[tail] = value;
            tail = (tail == this.data.length - 1) ? 0 : tail + 1;
        }
    }

    /**
     * Remove the first element of the list.
     *
     * @return The removed value.
     * @throws ChiSimulatorException when removing from an empty list.
     */
    public T removeHead() {
        if (isEmpty()) {
            String msg = "Cannot remove the first element from an empty list.";
            throw new ChiSimulatorException(msg);
        }
        T value = data[head];
        data[head] = null; // Prevent reference leaking.
        head = (head == this.data.length - 1) ? 0 : head + 1;
        return value;
    }

    /**
     * Remove the last element of the list.
     *
     * @return The removed value.
     * @throws ChiSimulatorException when removing from an empty list.
     */
    public T removeTail() {
        if (isEmpty()) {
            String msg = "Cannot remove the last element from an empty list.";
            throw new ChiSimulatorException(msg);
        }
        tail = (tail == 0) ? data.length - 1 : tail - 1;
        T value = data[tail];
        data[tail] = null; // Prevent reference leaking.
        return value;
    }

    /**
     * Copy the list to an array.
     *
     * <p>
     * The copier does not write a {@code null} end-of-data marker, the {@link #size} function gives the length of the
     * copied data.
     * </p>
     *
     * @param array Array to copy the list into. If the provided array is {@code null} or not big enough, a new one is
     *     created.
     * @return The array with the copied data.
     */
    @SuppressWarnings("unchecked")
    public Object[] copyToArray(T[] array) {
        if (array == null || array.length < size()) {
            array = (T[])new Object[size()];
        }

        if (head < tail) {
            System.arraycopy(data, head, array, 0, tail - head);
            return array;
        }

        if (head > tail) {
            System.arraycopy(data, head, array, 0, data.length - head);
            System.arraycopy(data, 0, array, data.length - head, tail);
        }
        // else head == tail, which means 'empty list'
        return array;
    }

    /**
     * Insert a new value at a given index. All values at or after that index are shifted one position to the end, and
     * the list becomes one element longer.
     *
     * @param idx Index of the new value. The index may also be equal to the size of the list, in which case the new
     *     value is appended rather than inserted.
     * @param val Value to insert.
     */
    public void insert(int idx, T val) {
        idx = normalizeIndex(idx, true);
        if (size() + 1 == data.length) {
            enlargeStorage(data.length);
        }

        // idx has a sane value (including 'size()').
        // there is sufficient space for the additional value.
        idx += head;
        if (head <= tail) {
            if (idx - head > tail - idx) {
                // Moving the back-part is cheaper. Since tail is behind the
                // last element, there is always sufficient room at the end.
                System.arraycopy(data, idx, data, idx + 1, tail - idx);
                data[idx] = val;
                tail = (tail == data.length - 1) ? 0 : tail + 1;
                return;
            } else {
                // Moving the front-part is cheaper.
                if (idx > head) {
                    // There is data to move.
                    if (head == 0) { // but no space at the front.
                        data[data.length - 1] = data[0]; // Make room.
                        System.arraycopy(data, 1, data, 0, idx - 1);
                    } else { // and there is space at the front.
                        System.arraycopy(data, head, data, head - 1, idx - head);
                    }
                }
                idx = (idx == 0) ? data.length - 1 : idx - 1;
                data[idx] = val;
                head = (head == 0) ? data.length - 1 : head - 1;
                return;
            }
        } else {
            // tail < head

            if (idx >= data.length) {
                idx -= data.length; // Wrap around to the tail part.
                System.arraycopy(data, idx, data, idx + 1, tail - idx);
                data[idx] = val;
                tail = (tail == data.length - 1) ? 0 : tail + 1;
                return;
            } else { // At the head part.
                System.arraycopy(data, head, data, head - 1, idx - head);
                data[idx - 1] = val;
                head = (head == 0) ? data.length - 1 : head - 1;
                return;
            }
        }
    }

    /**
     * Remove a value from the list.
     *
     * @param idx Index of value to remove.
     * @return The removed value.
     */
    public T remove(int idx) {
        idx = normalizeIndex(idx, false) + head;

        if (head <= tail) {
            T val = data[idx];
            if (idx - head > tail - idx) {
                System.arraycopy(data, idx + 1, data, idx, tail - idx - 1);
                tail--;
                data[tail] = null;
            } else {
                System.arraycopy(data, head, data, head + 1, idx - head);
                data[head] = null;
                head++;
            }
            return val;
        }
        // tail < head
        if (idx >= data.length) {
            idx -= data.length;
            T val = data[idx];
            System.arraycopy(data, idx + 1, data, idx, tail - idx - 1);
            tail--;
            data[tail] = null;
            return val;
        } else {
            T val = data[idx];
            System.arraycopy(data, head, data, head + 1, idx - head);
            data[head] = null;
            head = (head == data.length - 1) ? 0 : head + 1;
            return val;
        }
    }

    /**
     * Insert the given element in the sorted list according to a sort predicate function.
     *
     * @param elm Element to insert
     * @param comp Sort predicate.
     */
    public void insert(T elm, Comparator<T> comp) {
        int lower = -1;
        int upper = size();
        while (lower + 1 < upper) {
            int middle = (lower + upper) / 2;
            if (comp.compare(get(middle), elm) < 0) { // elm < 'middle'
                lower = middle;
            } else { // elm >= 'middle'
                upper = middle;
            }
        }
        if (upper == size()) {
            append(elm);
        } else {
            insert(upper, elm);
        }
    }

    /**
     * Sort the list in-place using a sort predicate function.
     *
     * @param comp Compare function.
     */
    public void sort(Comparator<T> comp) {
        if (size() <= 1) {
            return;
        }

        // head < tail is already OK.
        int end;
        if (head < tail) {
            end = tail;
        } else {
            int blk = 0; // Assume the 0..tail part is the smallest.
            int length = tail;
            if (head - tail < length) { // in-between empty gap is smaller.
                blk = 1;
                length = head - tail;
            }
            if (data.length - head < length) { // the head..end part is smaller
                blk = 2;
                length = data.length - head;
            }

            switch (blk) {
                case 0:
                    System.arraycopy(data, 0, data, head - length, length);
                    head -= length;
                    tail = 0;
                    end = data.length;

                    // Clean out the remaining data.
                    for (int i = 0; i < head; i++) {
                        data[i] = null;
                    }
                    break;

                case 1:
                    System.arraycopy(data, 0, data, tail, length);
                    head = length;
                    tail = 0;
                    end = data.length;

                    // Clean out the remaining data.
                    for (int i = 0; i < head; i++) {
                        data[i] = null;
                    }
                    break;

                case 2:
                    System.arraycopy(data, head, data, tail, length);
                    head = 0;
                    tail += length;
                    end = tail;

                    // Clean out the remaining data.
                    for (int i = tail; i < data.length; i++) {
                        data[i] = null;
                    }
                    break;

                default:
                    end = head; // Never reached, false positive.
                    break;
            }
        }

        Arrays.sort(data, head, end, comp);
    }

    /**
     * Is an element available in the list?
     *
     * @param elm Element to look for.
     *
     * @return Whether the given element could be found in the list.
     */
    public boolean contains(T elm) {
        int idx = head;
        while (idx != tail) {
            if (data[idx].equals(elm)) {
                return true;
            }
            idx++;
            if (idx == data.length) {
                idx = 0;
            }
        }
        return false;
    }

    /** Iterator class of the list. */
    protected final class IndexableDequeIterator implements Iterator<T> {
        /** Current (non-negative) index in the list. */
        private int position;

        /** Step size and direction. */
        private int step;

        /**
         * Upper bound of the iteration (one beyond the last index to return, value depends also on the direction of
         * iterating).
         */
        private int end;

        /**
         * Constructor of the {@link IndexableDequeIterator} class.
         *
         * @param start Start index in the deque.
         * @param step Step-size and direction of the iterator.
         * @param end One further than the last index value to return (that is, it is an exclusive upper bound).
         */
        public IndexableDequeIterator(int start, int step, int end) {
            int length = size();
            this.step = (step == 0) ? 1 : step;
            if (this.step > 0) { // Positive direction of stepping.
                if (start < 0) {
                    start = 0;
                }
                if (end > length) {
                    end = length;
                }
            } else { // Negative direction of stepping.
                if (start > length - 1) {
                    start = length - 1;
                }
                if (end < -1) {
                    end = -1;
                }
            }
            position = start;
            this.end = end;
        }

        @Override
        public boolean hasNext() {
            if (step > 0) {
                return position < end;
            }
            return position > end;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T value = get(position);
            position += step;
            return value;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Construct an iterator for the deque.
     *
     * @param start Start index in the deque.
     * @param step Step-size and direction of the iterator.
     * @param end One further than the last index value to return (that is, it is an exclusive upper bound).
     * @return The constructed iterator.
     */
    public Iterator<T> iterator(int start, int step, int end) {
        return new IndexableDequeIterator(start, step, end);
    }

    @Override
    public Iterator<T> iterator() {
        return iterator(0, 1, size());
    }

    /**
     * Get a descending iterator.
     *
     * @return The default descending iterator.
     */
    public Iterator<T> descendingIterator() {
        return iterator(size() - 1, -1, -1);
    }

    @Override
    public int hashCode() {
        int hash = 387;
        int idx = head;
        int n = 1;
        while (idx != tail) {
            hash += n * data[idx].hashCode();
            n = n % 32 + 1;
            idx++;
            if (idx == data.length) {
                idx = 0;
            }
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof IndexableDeque)) {
            return false;
        }
        IndexableDeque<?> other = (IndexableDeque<?>)obj;
        if (size() != other.size()) {
            return false;
        }

        int thisIdx = head;
        int otherIdx = other.head;
        while (thisIdx != tail) {
            if (!(data[thisIdx].equals(other.data[otherIdx]))) {
                return false;
            }
            thisIdx++;
            if (thisIdx >= data.length) {
                thisIdx = 0;
            }
            otherIdx++;
            if (otherIdx >= other.data.length) {
                otherIdx = 0;
            }
        }
        return true;
    }
}
