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

package org.eclipse.escet.chi.codegen.types;

import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Collections;
import java.util.List;

import org.eclipse.escet.chi.codegen.CodeGeneratorContext;
import org.eclipse.escet.chi.codegen.expressions.ExpressionBase;
import org.eclipse.escet.chi.codegen.java.JavaFile;
import org.eclipse.escet.chi.metamodel.chi.Expression;
import org.eclipse.escet.chi.metamodel.chi.Type;
import org.eclipse.escet.chi.runtime.data.io.ChiFileHandle;
import org.eclipse.escet.common.box.VBox;

/** Class representing a type in the code generator. */
public abstract class TypeID {
    /** Does this data type need a chiCoordinator constructor parameter? */
    public final boolean needsCoordinator;

    /** Top-level type-kind of this type. */
    public final TypeKind kind;

    /** Sub-types of this type if available, else {@code null}. */
    public final List<TypeID> subTypes;

    /** Hash code of the type ID. */
    private int hash;

    /**
     * Constructor for an elementary type.
     *
     * @param needsCoordinator Whether the type needs a chiCoordinator for construction.
     * @param kind Type kind of the type.
     */
    public TypeID(boolean needsCoordinator, TypeKind kind) {
        this(needsCoordinator, kind, Collections.EMPTY_LIST);
    }

    /**
     * Constructor for a container type.
     *
     * @param needsCoordinator Whether the type needs a chiCoordinator for construction.
     * @param kind Type kind of the type.
     * @param subTypes Sub-types of the type.
     */
    public TypeID(boolean needsCoordinator, TypeKind kind, List<TypeID> subTypes) {
        this.needsCoordinator = needsCoordinator;
        this.kind = kind;
        this.subTypes = subTypes;
        hash = computeHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TypeID)) {
            return false;
        }
        TypeID other = (TypeID)obj;
        if (this.kind != other.kind) {
            return false;
        }
        return this.subTypes.equals(other.subTypes);
    }

    @Override
    public int hashCode() {
        return hash;
    }

    /**
     * Compute the hash code of this type.
     *
     * @return The computed hash code.
     */
    private int computeHashCode() {
        int value = kind.hashCode();
        int n = 2;
        for (TypeID ti: subTypes) {
            value ^= n * ti.hashCode();
            n = n + 1;
        }
        return value;
    }

    /**
     * Get a textual definition of the type in Chi syntax.
     *
     * @return The type as text.
     */
    public abstract String getTypeText();

    /**
     * Get the Java type for this type kind.
     *
     * @return Text with the Java type in the Java implementation.
     */
    public abstract String getJavaType();

    /**
     * Get the Java type for this kind as a class. For primitive types, this is different from the data type used to
     * store the information in.
     *
     * @return Text with the Java class name of this type.
     */
    public abstract String getJavaClassType();

    /**
     * Return whether the type has a deep-copy operation.
     *
     * @return Whether the type has a deep-copy operation.
     */
    public abstract boolean hasDeepCopy();

    /**
     * Return the expression string that performs a deep copy on the value.
     *
     * @param val Value (of this type) that needs to be deep-copied.
     * @param jf Current java class (for adding imports).
     * @param doDeep Do a real deep copy (do a shallow one if {@code false}).
     * @return Expression string returning a deep-copied value.
     */
    public abstract String getDeepCopyName(String val, JavaFile jf, boolean doDeep);

    /**
     * Is this type serializable from/to text?
     *
     * @return Whether the type can be serialized from/to text.
     */
    public final boolean isPrintable() {
        if (!kind.printable) {
            return false;
        }
        for (TypeID tid: subTypes) {
            if (!tid.isPrintable()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Return an expression that reads a value from input stream 'stream'.
     *
     * @param stream Expression denoting the input stream. Its value at run-time is a {@link ChiFileHandle}.
     * @param jf Current java class (for adding imports).
     * @return An expression that reads a value from input stream.
     */
    public abstract String getReadName(String stream, JavaFile jf);

    /**
     * Return a statement that writes a value to the output stream 'stream'.
     *
     * @param stream Expression denoting the output stream. Its value at run-time is a {@link ChiFileHandle}.
     * @param val Expression denoting a value of this type.
     * @param jf Current java class (for adding imports).
     * @return A statement that writes a value to the output stream.
     */
    public abstract String getWriteName(String stream, String val, JavaFile jf);

    /**
     * Return an expression that computes the hash code of a value of this type.
     *
     * @param val Expression denoting a value of this type.
     * @param jf Current java class (for adding imports).
     * @return An expression that computes the hash code of the value.
     */
    public abstract String getHashCodeName(String val, JavaFile jf);

    /**
     * Return an expression that gives a string containing a value of this type.
     *
     * @param val Expression denoting a value of this type.
     * @param jf Current java class (for adding imports).
     * @return An expression that computes a string for a value of this type.
     */
    public abstract String getToString(String val, JavaFile jf);

    /**
     * Return an expression that computes equality between two values of this type.
     *
     * @param lhs Expression containing the first value.
     * @param rhs Expression containing the second value.
     * @return Expression computing equality between both types.
     */
    public abstract String getEqual(String lhs, String rhs);

    /**
     * Return an expression that computes in-equality between two values of this type.
     *
     * @param lhs Expression containing the first value.
     * @param rhs Expression containing the second value.
     * @return Expression computing in-equality between both types.
     */
    public abstract String getUnequal(String lhs, String rhs);

    /**
     * Return the empty value for this type. Note this is a helper for the {@link #assignInitialValue} method.
     *
     * @param jf Current java class (for adding imports).
     * @return The default value for this type.
     */
    protected abstract String getEmptyValue(JavaFile jf);

    /**
     * Assign the initial value of the type.
     *
     * @param name Variable to assign to.
     * @param tp Type of the variable (with proper list initial length). May be {@code null}.
     * @param box Code box to store the assignment in.
     * @param ctxt Code generation context.
     * @param javaFile Java file used as target for the code generation.
     *
     */
    public void assignInitialValue(String name, Type tp, VBox box, CodeGeneratorContext ctxt, JavaFile javaFile) {
        String line = fmt("%s = %s;", name, getEmptyValue(javaFile));
        box.add(line);
    }

    /**
     * Get the most simple Java value for the Java type (does not need to be correct for Chi).
     *
     * @return Text expressing a cheap Java value to use as place holder.
     */
    public abstract String getSimplestJavaValue();

    /**
     * Convert an expression node to implementation language.
     *
     * @param expr Expression to convert.
     * @param ctxt Code generation context.
     * @param javaFile Java file used as target for the code generation.
     * @return Converted expression operation.
     */
    public abstract ExpressionBase convertExprNode(Expression expr, CodeGeneratorContext ctxt, JavaFile javaFile);

    /**
     * Does this type id represent the Chi integer data type?
     *
     * @return Whether this type id represents the Chi integer data type.
     */
    public final boolean isIntTypeID() {
        return kind == TypeKind.INT;
    }

    /** Available kinds of types. */
    public enum TypeKind {
        /** Dictionary type kind. */
        DICTIONARY(true),

        /** Timer type kind. */
        TIMER(false),

        /** File type kind. */
        FILE(false),

        /** Enumeration type kind. */
        ENUM(true),

        /** Set type kind. */
        SET(true),

        /** Boolean type kind. */
        BOOL(true),

        /** Distribution type kind. */
        DISTRIBUTION(false),

        /** Integer type kind. */
        INT(true),

        /** String type kind. */
        STRING(true),

        /** Real type kind. */
        REAL(true),

        /** Process instance type kind. */
        INSTANCE(false),

        /** Data channel type kind. */
        CHANNEL(false),

        /** Synchronization channel type kind. */
        SYNCHRONIZATION(false),

        /** Process definition type kind. */
        PROCESS(false),

        /** Function type kind. */
        FUNCTION(false),

        /** Tuple type kind. */
        TUPLE(true),

        /** Matrix type kind. */
        MATRIX(true),

        /** List type kind. */
        LIST(true);

        /** Whether this type kind can be serialized from/to text. */
        public final boolean printable;

        /**
         * Constructor of the enumeration value.
         *
         * @param printable Whether this type kind can be serialized.
         */
        private TypeKind(boolean printable) {
            this.printable = printable;
        }
    }
}
