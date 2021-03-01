//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.StreamCorruptedException;

/** Cloning related utility methods. */
public class CloneUtils {
    /** Constructor for the {@link CloneUtils} class. */
    private CloneUtils() {
        // Static class.
    }

    /**
     * Deep clones a serializable object. For {@code null} objects, this method returns {@code null}.
     *
     * @param <T> The type of the object to deep clone.
     * @param obj The object to deep clone.
     * @return The deep clone of the object.
     */
    public static <T> T deepclone(Object obj) {
        // Special case for 'null' objects.
        if (obj == null) {
            return null;
        }

        // Deep clone through serialization.
        Object clone;
        try {
            // Serialize to byte array.
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            ObjectOutputStream oout = new ObjectOutputStream(bout);
            oout.writeObject(obj);
            oout.flush();
            oout.close();
            byte[] data = bout.toByteArray();

            // Deserialize from byte array.
            ByteArrayInputStream bin = new ByteArrayInputStream(data);
            ClassLoader classLoader = obj.getClass().getClassLoader();
            ObjectInputStream oin = new MyObjectInputStream(bin, classLoader);
            clone = oin.readObject();
            oin.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to deep clone object.", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to deep clone object.", e);
        }

        // Return type-safe result.
        Assert.notNull(clone);
        @SuppressWarnings("unchecked")
        T rslt = (T)clone;
        return rslt;
    }

    /** Custom {@link ObjectInputStream} that accepts a user provided {@link ClassLoader class loader}. */
    private static class MyObjectInputStream extends ObjectInputStream {
        /** The class loader to use to resolve classes during deserialization. */
        private final ClassLoader classLoader;

        /**
         * Constructor for the {@link MyObjectInputStream} class.
         *
         * @param in The input stream to read from.
         * @param classLoader The class loader to use to resolve classes during deserialization.
         * @throws StreamCorruptedException If the stream header is incorrect.
         * @throws IOException If an I/O error occurs while reading the stream header.
         * @throws SecurityException If untrusted subclass illegally overrides security-sensitive methods.
         * @throws NullPointerException If {@code in} is {@code null}.
         *
         * @see ObjectInputStream#ObjectInputStream(InputStream)
         */
        public MyObjectInputStream(InputStream in, ClassLoader classLoader) throws IOException {
            super(in);
            this.classLoader = classLoader;
        }

        @Override
        protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
            // Make sure we use our own class loader. Also does not provide
            // a fall-back for primitive types, as the default implementation
            // does. It is not useful to deep clone them anyway...
            return Class.forName(desc.getName(), false, classLoader);
        }
    }
}
