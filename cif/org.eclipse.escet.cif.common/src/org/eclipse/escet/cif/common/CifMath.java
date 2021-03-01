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

package org.eclipse.escet.cif.common;

import static org.eclipse.escet.common.java.Lists.copy;

import java.util.List;
import java.util.Map;

import org.apache.commons.math3.util.FastMath;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.FormatDecoder;
import org.eclipse.escet.common.java.FormatDescription;
import org.eclipse.escet.common.java.FormatDescription.Conversion;
import org.eclipse.escet.common.java.Lists;
import org.eclipse.escet.common.java.Strings;

/** CIF math functions. See also the {@code CifSimulatorMath} class. */
public class CifMath {
    /** Constructor for the {@link CifMath} class. */
    private CifMath() {
        // Static class.
    }

    /**
     * Returns the absolute value of an integer number.
     *
     * @param x The integer number.
     * @param expr The original expression, to be used for the exception. May be {@code null} if the expression can't
     *     fail to evaluate, or if the expression (or its position) is not relevant for the exception.
     * @return {@code abs(x)}.
     * @throws CifEvalException If the operation results in integer overflow.
     */
    public static int abs(int x, Expression expr) throws CifEvalException {
        if (x == Integer.MIN_VALUE) {
            String msg = fmt("Integer overflow: abs(%d).", x);
            throw new CifEvalException(msg, expr);
        }
        return Math.abs(x);
    }

    /**
     * Returns the absolute value of a real number.
     *
     * @param x The real number.
     * @return {@code abs(x)}.
     */
    public static double abs(double x) {
        return Math.abs(x);
    }

    /**
     * Returns the arc cosine of a real number.
     *
     * @param x The real number.
     * @param expr The original expression, to be used for the exception. May be {@code null} if the expression can't
     *     fail to evaluate, or if the expression (or its position) is not relevant for the exception.
     * @return {@code acos(x)}.
     * @throws CifEvalException If the operation results in real overflow, or {@code NaN}.
     */
    public static double acos(double x, Expression expr) throws CifEvalException {
        double rslt = Math.acos(x);
        if (Double.isInfinite(rslt)) {
            String msg = fmt("Real overflow: acos(%s).", x);
            throw new CifEvalException(msg, expr);
        }
        if (Double.isNaN(rslt)) {
            String msg = fmt("Invalid operation: acos(%s).", x);
            throw new CifEvalException(msg, expr);
        }
        return (rslt == -0.0) ? 0.0 : rslt;
    }

    /**
     * Returns the arc hyperbolic cosine of a real number.
     *
     * @param x The real number.
     * @param expr The original expression, to be used for the exception. May be {@code null} if the expression can't
     *     fail to evaluate, or if the expression (or its position) is not relevant for the exception.
     * @return {@code acosh(x)}.
     * @throws CifEvalException If the operation results in real overflow, or {@code NaN}.
     */
    public static double acosh(double x, Expression expr) throws CifEvalException {
        double rslt = FastMath.acosh(x);
        if (Double.isInfinite(rslt)) {
            String msg = fmt("Real overflow: acosh(%s).", x);
            throw new CifEvalException(msg, expr);
        }
        if (Double.isNaN(rslt)) {
            String msg = fmt("Invalid operation: acosh(%s).", x);
            throw new CifEvalException(msg, expr);
        }
        return (rslt == -0.0) ? 0.0 : rslt;
    }

    /**
     * Returns the arc sine of a real number.
     *
     * @param x The real number.
     * @param expr The original expression, to be used for the exception. May be {@code null} if the expression can't
     *     fail to evaluate, or if the expression (or its position) is not relevant for the exception.
     * @return {@code asin(x)}.
     * @throws CifEvalException If the operation results in real overflow, or {@code NaN}.
     */
    public static double asin(double x, Expression expr) throws CifEvalException {
        double rslt = Math.asin(x);
        if (Double.isInfinite(rslt)) {
            String msg = fmt("Real overflow: asin(%s).", x);
            throw new CifEvalException(msg, expr);
        }
        if (Double.isNaN(rslt)) {
            String msg = fmt("Invalid operation: asin(%s).", x);
            throw new CifEvalException(msg, expr);
        }
        return (rslt == -0.0) ? 0.0 : rslt;
    }

    /**
     * Returns the arc hyperbolic sine of a real number.
     *
     * @param x The real number.
     * @param expr The original expression, to be used for the exception. May be {@code null} if the expression can't
     *     fail to evaluate, or if the expression (or its position) is not relevant for the exception.
     * @return {@code asinh(x)}.
     * @throws CifEvalException If the operation results in real overflow, or {@code NaN}.
     */
    public static double asinh(double x, Expression expr) throws CifEvalException {
        double rslt = FastMath.asinh(x);
        if (Double.isInfinite(rslt)) {
            String msg = fmt("Real overflow: asinh(%s).", x);
            throw new CifEvalException(msg, expr);
        }
        if (Double.isNaN(rslt)) {
            String msg = fmt("Invalid operation: asinh(%s).", x);
            throw new CifEvalException(msg, expr);
        }
        return (rslt == -0.0) ? 0.0 : rslt;
    }

    /**
     * Returns the arc tangent of a real number.
     *
     * @param x The real number.
     * @param expr The original expression, to be used for the exception. May be {@code null} if the expression can't
     *     fail to evaluate, or if the expression (or its position) is not relevant for the exception.
     * @return {@code atan(x)}.
     * @throws CifEvalException If the operation results in real overflow, or {@code NaN}.
     */
    public static double atan(double x, Expression expr) throws CifEvalException {
        double rslt = Math.atan(x);
        if (Double.isInfinite(rslt)) {
            String msg = fmt("Real overflow: atan(%s).", x);
            throw new CifEvalException(msg, expr);
        }
        if (Double.isNaN(rslt)) {
            String msg = fmt("Invalid operation: atan(%s).", x);
            throw new CifEvalException(msg, expr);
        }
        return (rslt == -0.0) ? 0.0 : rslt;
    }

    /**
     * Returns the arc hyperbolic tangent of a real number.
     *
     * @param x The real number.
     * @param expr The original expression, to be used for the exception. May be {@code null} if the expression can't
     *     fail to evaluate, or if the expression (or its position) is not relevant for the exception.
     * @return {@code atanh(x)}.
     * @throws CifEvalException If the operation results in real overflow, or {@code NaN}.
     */
    public static double atanh(double x, Expression expr) throws CifEvalException {
        double rslt = FastMath.atanh(x);
        if (Double.isInfinite(rslt)) {
            String msg = fmt("Real overflow: atanh(%s).", x);
            throw new CifEvalException(msg, expr);
        }
        if (Double.isNaN(rslt)) {
            String msg = fmt("Invalid operation: atanh(%s).", x);
            throw new CifEvalException(msg, expr);
        }
        return (rslt == -0.0) ? 0.0 : rslt;
    }

    /**
     * Returns the addition of two integer numbers.
     *
     * @param x The first integer number.
     * @param y The second integer number.
     * @param expr The original expression, to be used for the exception. May be {@code null} if the expression can't
     *     fail to evaluate, or if the expression (or its position) is not relevant for the exception.
     * @return {@code x + y}.
     * @throws CifEvalException If the operation results in integer overflow.
     */
    public static int add(int x, int y, Expression expr) throws CifEvalException {
        long rslt = (long)x + (long)y;
        if (Integer.MIN_VALUE <= rslt && rslt <= Integer.MAX_VALUE) {
            return (int)rslt;
        }

        String msg = fmt("Integer overflow: %d + %d.", x, y);
        throw new CifEvalException(msg, expr);
    }

    /**
     * Returns the addition of two real numbers.
     *
     * @param x The first real number.
     * @param y The second real number.
     * @param expr The original expression, to be used for the exception. May be {@code null} if the expression can't
     *     fail to evaluate, or if the expression (or its position) is not relevant for the exception.
     * @return {@code x + y}.
     * @throws CifEvalException If the operation results in real overflow.
     */
    public static double add(double x, double y, Expression expr) throws CifEvalException {
        double rslt = x + y;
        if (Double.isInfinite(rslt)) {
            String msg = fmt("Real overflow: %s + %s.", x, y);
            throw new CifEvalException(msg, expr);
        }
        return (rslt == -0.0) ? 0.0 : rslt;
    }

    /**
     * Converts a Java {@link Boolean} to a CIF boolean value literal, in the CIF ASCII representation.
     *
     * @param x The Java {@link Boolean} value.
     * @return The CIF boolean value literal, in the CIF ASCII representation.
     */
    public static String boolToStr(boolean x) {
        return x ? "true" : "false";
    }

    /**
     * Returns the cube root of a real number.
     *
     * @param x The real number.
     * @return {@code cbrt(x)}.
     */
    public static double cbrt(double x) {
        double rslt = Math.cbrt(x);
        return (rslt == -0.0) ? 0.0 : rslt;
    }

    /**
     * Returns the ceil of a real number.
     *
     * @param x The real number.
     * @param expr The original expression, to be used for the exception. May be {@code null} if the expression can't
     *     fail to evaluate, or if the expression (or its position) is not relevant for the exception.
     * @return {@code ceil(x)}.
     * @throws CifEvalException If the operation results in integer overflow.
     */
    public static int ceil(double x, Expression expr) throws CifEvalException {
        double rslt = Math.ceil(x);
        if (rslt < Integer.MIN_VALUE || rslt > Integer.MAX_VALUE) {
            String msg = fmt("Integer overflow: ceil(%s).", x);
            throw new CifEvalException(msg, expr);
        }
        return (int)rslt;
    }

    /**
     * Returns the cosine of a real number.
     *
     * @param x The real number.
     * @param expr The original expression, to be used for the exception. May be {@code null} if the expression can't
     *     fail to evaluate, or if the expression (or its position) is not relevant for the exception.
     * @return {@code cos(x)}.
     * @throws CifEvalException If the operation results in real overflow, or {@code NaN}.
     */
    public static double cos(double x, Expression expr) throws CifEvalException {
        double rslt = Math.cos(x);
        if (Double.isInfinite(rslt)) {
            String msg = fmt("Real overflow: cos(%s).", x);
            throw new CifEvalException(msg, expr);
        }
        if (Double.isNaN(rslt)) {
            String msg = fmt("Invalid operation: cos(%s).", x);
            throw new CifEvalException(msg, expr);
        }
        return (rslt == -0.0) ? 0.0 : rslt;
    }

    /**
     * Returns the hyperbolic cosine of a real number.
     *
     * @param x The real number.
     * @param expr The original expression, to be used for the exception. May be {@code null} if the expression can't
     *     fail to evaluate, or if the expression (or its position) is not relevant for the exception.
     * @return {@code cosh(x)}.
     * @throws CifEvalException If the operation results in real overflow, or {@code NaN}.
     */
    public static double cosh(double x, Expression expr) throws CifEvalException {
        double rslt = Math.cosh(x);
        if (Double.isInfinite(rslt)) {
            String msg = fmt("Real overflow: cosh(%s).", x);
            throw new CifEvalException(msg, expr);
        }
        if (Double.isNaN(rslt)) {
            String msg = fmt("Invalid operation: cosh(%s).", x);
            throw new CifEvalException(msg, expr);
        }
        return (rslt == -0.0) ? 0.0 : rslt;
    }

    /**
     * Removes an element from a list, and returns the list without that element.
     *
     * @param lst The list.
     * @param origIdx The 0-based index into the list of the element to remove. Negative indices are allowed, and count
     *     from the right.
     * @param expr The original expression, to be used for the exception. May be {@code null} if the expression can't
     *     fail to evaluate, or if the expression (or its position) is not relevant for the exception.
     * @return {@code del(lst, origIdx)}.
     * @throws CifEvalException If the index is out of bounds for the list.
     */
    public static List<Object> delete(List<Object> lst, int origIdx, Expression expr) throws CifEvalException {
        int idx = origIdx;
        if (idx < 0) {
            idx = lst.size() + idx;
        }
        if (idx < 0 || idx >= lst.size()) {
            String msg = fmt("Index out of bounds: del(%s, %s).", CifEvalUtils.objToStr(lst), origIdx);
            throw new CifEvalException(msg, expr);
        }

        List<Object> rslt = copy(lst);
        rslt.remove(idx);
        return rslt;
    }

    /**
     * Returns the integer division of two integer numbers.
     *
     * @param x The dividend.
     * @param y The divisor.
     * @param expr The original expression, to be used for the exception. May be {@code null} if the expression can't
     *     fail to evaluate, or if the expression (or its position) is not relevant for the exception.
     * @return {@code x div y}.
     * @throws CifEvalException If the operation results in integer overflow, or division by zero.
     */
    public static int div(int x, int y, Expression expr) throws CifEvalException {
        if (y == 0) {
            String msg = fmt("Division by zero: %d div %d.", x, y);
            throw new CifEvalException(msg, expr);
        }
        if (x == Integer.MIN_VALUE && y == -1) {
            String msg = fmt("Integer overflow: %d div %d.", x, y);
            throw new CifEvalException(msg, expr);
        }
        return x / y;
    }

    /**
     * Returns the real division of two real numbers.
     *
     * @param x The first real number.
     * @param y The second real number.
     * @param expr The original expression, to be used for the exception. May be {@code null} if the expression can't
     *     fail to evaluate, or if the expression (or its position) is not relevant for the exception.
     * @return {@code x / y}.
     * @throws CifEvalException If the operation results in real overflow, or division by zero.
     */
    public static double divide(double x, double y, Expression expr) throws CifEvalException {
        if (y == 0.0) {
            String msg = fmt("Division by zero: %s / %s.", x, y);
            throw new CifEvalException(msg, expr);
        }
        double rslt = x / y;
        if (Double.isInfinite(rslt)) {
            String msg = fmt("Real overflow: %s * %s.", x, y);
            throw new CifEvalException(msg, expr);
        }
        return (rslt == -0.0) ? 0.0 : rslt;
    }

    /**
     * Returns {@code e}<sup>{@code x}</sup> of a real number {@code x}.
     *
     * @param x The real number.
     * @param expr The original expression, to be used for the exception. May be {@code null} if the expression can't
     *     fail to evaluate, or if the expression (or its position) is not relevant for the exception.
     * @return {@code exp(x)}.
     * @throws CifEvalException If the operation results in real overflow.
     */
    public static double exp(double x, Expression expr) throws CifEvalException {
        double rslt = Math.exp(x);
        if (Double.isInfinite(rslt)) {
            String msg = fmt("Real overflow: exp(%s).", x);
            throw new CifEvalException(msg, expr);
        }
        return (rslt == -0.0) ? 0.0 : rslt;
    }

    /**
     * Returns the floor of a real number.
     *
     * @param x The real number.
     * @param expr The original expression, to be used for the exception. May be {@code null} if the expression can't
     *     fail to evaluate, or if the expression (or its position) is not relevant for the exception.
     * @return {@code floor(x)}.
     * @throws CifEvalException If the operation results in integer overflow.
     */
    public static int floor(double x, Expression expr) throws CifEvalException {
        double rslt = Math.floor(x);
        if (rslt < Integer.MIN_VALUE || rslt > Integer.MAX_VALUE) {
            String msg = fmt("Integer overflow: floor(%s).", x);
            throw new CifEvalException(msg, expr);
        }
        return (int)rslt;
    }

    /**
     * Formats text given a pattern and arguments.
     *
     * @param pattern The format pattern to use.
     * @param args The arguments to use as values for the format pattern.
     * @return The formatted text.
     */
    public static String fmt(String pattern, Object... args) {
        // See also CifFormatPatternCodeGenerator for similar code.

        // Decode pattern.
        FormatDecoder decoder = new FormatDecoder();
        List<FormatDescription> parts = decoder.decode(pattern);

        // Process all parts.
        StringBuilder rslt = new StringBuilder();
        int implicitIndex = 0;
        for (FormatDescription part: parts) {
            // Literal.
            if (part.conversion == Conversion.LITERAL) {
                rslt.append(part.text);
                continue;
            }

            // Get 0-based index of specifier.
            int idx;
            if (!part.index.isEmpty()) {
                idx = part.getExplicitIndex() - 1;
            } else {
                idx = implicitIndex;
                implicitIndex++;
            }

            // Get value.
            Object value = args[idx];

            // Add to formatted result.
            switch (part.conversion) {
                case BOOLEAN:
                case INTEGER:
                case REAL: {
                    String txt = Strings.fmt(part.toString(false), value);
                    rslt.append(txt);
                    break;
                }

                case STRING: {
                    if (!(value instanceof String)) {
                        value = CifEvalUtils.objToStr(value);
                    }
                    String txt = Strings.fmt(part.toString(false), value);
                    rslt.append(txt);
                    break;
                }

                default:
                    String msg = "Unexpected: " + part.conversion;
                    throw new RuntimeException(msg);
            }
        }
        return rslt.toString();
    }

    /**
     * Converts a Java {@link Integer} to a Java {@link Double}.
     *
     * @param x The Java {@link Integer} value.
     * @return The Java {@link Double} value.
     */
    public static double intToReal(int x) {
        // The 'int' to 'double' conversion is lossless. See
        // https://www.securecoding.cert.org/confluence/display/java/NUM13-J.+Avoid+loss+of+precision+when+converting+primitive+integers+to+floating-point
        return x;
    }

    /**
     * Converts a Java {@link Integer} to a CIF integer value literal, in the CIF ASCII representation.
     *
     * @param x The Java {@link Integer} value.
     * @return The CIF integer value literal, in the CIF ASCII representation.
     */
    public static String intToStr(int x) {
        return Integer.toString(x);
    }

    /**
     * Returns the natural logarithm of a real number.
     *
     * @param x The real number.
     * @param expr The original expression, to be used for the exception. May be {@code null} if the expression can't
     *     fail to evaluate, or if the expression (or its position) is not relevant for the exception.
     * @return {@code ln(x)}.
     * @throws CifEvalException If the real number is non-positive.
     */
    public static double ln(double x, Expression expr) throws CifEvalException {
        if (x <= 0.0) {
            String msg = fmt("Invalid operation: ln(%s).", x);
            throw new CifEvalException(msg, expr);
        }
        return Math.log(x);
    }

    /**
     * Returns the logarithm (base 10) of a real number.
     *
     * @param x The real number.
     * @param expr The original expression, to be used for the exception. May be {@code null} if the expression can't
     *     fail to evaluate, or if the expression (or its position) is not relevant for the exception.
     * @return {@code log(x)}.
     * @throws CifEvalException If the real number is non-positive.
     */
    public static double log(double x, Expression expr) throws CifEvalException {
        if (x <= 0.0) {
            String msg = fmt("Invalid operation: log(%s).", x);
            throw new CifEvalException(msg, expr);
        }
        return Math.log10(x);
    }

    /**
     * Returns the maximum of two integer numbers.
     *
     * @param x The first integer number.
     * @param y The second integer number.
     * @return {@code max(x, y)}.
     */
    public static int max(int x, int y) {
        return Math.max(x, y);
    }

    /**
     * Returns the maximum of two real numbers.
     *
     * @param x The first real number.
     * @param y The second real number.
     * @return {@code max(x, y)}.
     */
    public static double max(double x, double y) {
        return Math.max(x, y);
    }

    /**
     * Returns the minimum of two integer numbers.
     *
     * @param x The first integer number.
     * @param y The second integer number.
     * @return {@code min(x, y)}.
     */
    public static int min(int x, int y) {
        return Math.min(x, y);
    }

    /**
     * Returns the minimum of two real numbers.
     *
     * @param x The first real number.
     * @param y The second real number.
     * @return {@code min(x, y)}.
     */
    public static double min(double x, double y) {
        // Assumes that the arguments are never -0.0.
        return Math.min(x, y);
    }

    /**
     * Returns the modulus of two integer numbers.
     *
     * @param x The dividend.
     * @param y The divisor.
     * @param expr The original expression, to be used for the exception. May be {@code null} if the expression can't
     *     fail to evaluate, or if the expression (or its position) is not relevant for the exception.
     * @return {@code x mod y}.
     * @throws CifEvalException If the operation results in division by zero.
     */
    public static int mod(int x, int y, Expression expr) throws CifEvalException {
        if (y == 0) {
            String msg = fmt("Division by zero: %d mod %d.", x, y);
            throw new CifEvalException(msg, expr);
        }
        return x % y;
    }

    /**
     * Returns the multiplication of two integer numbers.
     *
     * @param x The first integer number.
     * @param y The second integer number.
     * @param expr The original expression, to be used for the exception. May be {@code null} if the expression can't
     *     fail to evaluate, or if the expression (or its position) is not relevant for the exception.
     * @return {@code x * y}.
     * @throws CifEvalException If the operation results in integer overflow.
     */
    public static int multiply(int x, int y, Expression expr) throws CifEvalException {
        long rslt = (long)x * (long)y;
        if (Integer.MIN_VALUE <= rslt && rslt <= Integer.MAX_VALUE) {
            return (int)rslt;
        }

        String msg = fmt("Integer overflow: %d * %d.", x, y);
        throw new CifEvalException(msg, expr);
    }

    /**
     * Returns the multiplication of two real numbers.
     *
     * @param x The first real number.
     * @param y The second real number.
     * @param expr The original expression, to be used for the exception. May be {@code null} if the expression can't
     *     fail to evaluate, or if the expression (or its position) is not relevant for the exception.
     * @return {@code x * y}.
     * @throws CifEvalException If the operation results in real overflow.
     */
    public static double multiply(double x, double y, Expression expr) throws CifEvalException {
        double rslt = x * y;
        if (Double.isInfinite(rslt)) {
            String msg = fmt("Real overflow: %s * %s.", x, y);
            throw new CifEvalException(msg, expr);
        }
        return (rslt == -0.0) ? 0.0 : rslt;
    }

    /**
     * Returns the negation of an integer number.
     *
     * @param x The integer number.
     * @param expr The original expression, to be used for the exception. May be {@code null} if the expression can't
     *     fail to evaluate, or if the expression (or its position) is not relevant for the exception.
     * @return {@code -value}.
     * @throws CifEvalException If the operation results in integer overflow.
     */
    public static int negate(int x, Expression expr) throws CifEvalException {
        if (x == Integer.MIN_VALUE) {
            String msg = fmt("Integer overflow: -%d.", x);
            throw new CifEvalException(msg, expr);
        }
        return -x;
    }

    /**
     * Returns the negation of a real number.
     *
     * @param x The real number.
     * @return {@code -value}.
     */
    public static double negate(double x) {
        return (x == 0.0) ? 0.0 : -x;
    }

    /**
     * Pops the first element from a list, and returns a tuple, with the popped element, and the list without that
     * element.
     *
     * @param lst The list.
     * @param expr The original expression, to be used for the exception. May be {@code null} if the expression can't
     *     fail to evaluate, or if the expression (or its position) is not relevant for the exception.
     * @return {@code pop(lst)}.
     * @throws CifEvalException If the list is empty.
     */
    public static CifTuple pop(List<Object> lst, Expression expr) throws CifEvalException {
        if (lst.isEmpty()) {
            String msg = "Invalid operation: pop([]).";
            throw new CifEvalException(msg, expr);
        }

        List<Object> lst2 = slice(lst, 1, null);

        CifTuple rslt = new CifTuple();
        rslt.add(lst.get(0));
        rslt.add(lst2);
        return rslt;
    }

    /**
     * Returns the exponentiation (power) of two integer numbers.
     *
     * @param x The base integer number.
     * @param y The exponent integer number, {@code y >= 0}.
     * @param expr The original expression, to be used for the exception. May be {@code null} if the expression can't
     *     fail to evaluate, or if the expression (or its position) is not relevant for the exception.
     * @return {@code pow(x, y)}.
     * @throws CifEvalException If the operation results in integer overflow.
     */
    public static int pow(int x, int y, Expression expr) throws CifEvalException {
        Assert.check(y >= 0);
        double rslt = Math.pow(x, y);
        if (Integer.MIN_VALUE <= rslt && rslt <= Integer.MAX_VALUE) {
            return (int)rslt;
        }
        String msg = fmt("Integer overflow: pow(%d, %d).", x, y);
        throw new CifEvalException(msg, expr);
    }

    /**
     * Returns the exponentiation (power) of two real numbers.
     *
     * @param x The base real number.
     * @param y The exponent real number.
     * @param expr The original expression, to be used for the exception. May be {@code null} if the expression can't
     *     fail to evaluate, or if the expression (or its position) is not relevant for the exception.
     * @return {@code pow(x, y)}.
     * @throws CifEvalException If the operation results in real overflow, or {@code NaN}.
     */
    public static double pow(double x, double y, Expression expr) throws CifEvalException {
        // Assumes that the arguments are valid doubles (no inf/NaN/-0.0).
        double rslt = Math.pow(x, y);
        if (Double.isInfinite(rslt)) {
            String msg = fmt("Real overflow: pow(%s, %s).", x, y);
            throw new CifEvalException(msg, expr);
        }
        if (Double.isNaN(rslt)) {
            String msg = fmt("Invalid operation: pow(%s, %s).", x, y);
            throw new CifEvalException(msg, expr);
        }
        return (rslt == -0.0) ? 0.0 : rslt;
    }

    /**
     * Converts a Java {@link Double} to a CIF real value literal, in the CIF ASCII representation.
     *
     * <p>
     * Note that the {@link Double} values may be negative, and the resulting textual representation may thus have a
     * {@code "-"} prefix, unlike real value literals in the CIF ASCII representation, where the {@code "-"} character
     * is a unary operator.
     * </p>
     *
     * @param x The Java {@link Double} value.
     * @return The CIF real value literal, in the CIF ASCII representation.
     */
    public static String realToStr(double x) {
        // Double.toString always results in valid CIF ASCII representations
        // of real values. It also is round-trip compatible (up to fixed point
        // behavior) with strToReal.
        Assert.check(!Double.isNaN(x));
        Assert.check(!Double.isInfinite(x));
        return Double.toString(x).replace('E', 'e');
    }

    /**
     * Projects a list, using a zero-based index.
     *
     * @param lst The list.
     * @param origIdx The 0-based index into the list of the element to return. Negative indices are allowed, and count
     *     from the right.
     * @param expr The original expression, to be used for the exception. May be {@code null} if the expression can't
     *     fail to evaluate, or if the expression (or its position) is not relevant for the exception.
     * @return {@code lst[origIdx]}.
     * @throws CifEvalException If the index is out of bounds for the list.
     */
    public static Object project(List<Object> lst, int origIdx, Expression expr) throws CifEvalException {
        int idx = origIdx;
        if (idx < 0) {
            idx = lst.size() + idx;
        }
        if (idx < 0 || idx >= lst.size()) {
            String msg = fmt("Index out of bounds: %s[%s].", CifEvalUtils.objToStr(lst), origIdx);
            throw new CifEvalException(msg, expr);
        }

        return lst.get(idx);
    }

    /**
     * Projects a dictionary, using a key.
     *
     * @param dict The dictionary.
     * @param key The key.
     * @param expr The original expression, to be used for the exception. May be {@code null} if the expression can't
     *     fail to evaluate, or if the expression (or its position) is not relevant for the exception.
     * @return {@code dict[key]}.
     * @throws CifEvalException If there is no key/value pair in the dictionary for the given key.
     */
    public static Object project(Map<Object, Object> dict, Object key, Expression expr) throws CifEvalException {
        Object rslt = dict.get(key);
        if (rslt == null) {
            String msg = fmt("Key not found: %s[%s].", CifEvalUtils.objToStr(dict), key);
            throw new CifEvalException(msg, expr);
        }
        return rslt;
    }

    /**
     * Projects a string, using a zero-based index.
     *
     * @param str The string.
     * @param origIdx The 0-based index into the string of the character to return. Negative indices are allowed, and
     *     count from the right.
     * @param expr The original expression, to be used for the exception. May be {@code null} if the expression can't
     *     fail to evaluate, or if the expression (or its position) is not relevant for the exception.
     * @return {@code str[origIdx]}.
     * @throws CifEvalException If the index is out of bounds for the string.
     */
    public static String project(String str, int origIdx, Expression expr) throws CifEvalException {
        int idx = origIdx;
        if (idx < 0) {
            idx = str.length() + idx;
        }
        if (idx < 0 || idx >= str.length()) {
            String msg = fmt("Index out of bounds: \"%s\"[%s].", Strings.escape(str), origIdx);
            throw new CifEvalException(msg, expr);
        }

        return str.substring(idx, idx + 1);
    }

    /**
     * Projects a tuple, using a zero-based index.
     *
     * @param tuple The tuple.
     * @param idx The 0-based index into the tuple of the element to return. Negative indices are not allowed.
     * @param expr The original expression, to be used for the exception. May be {@code null} if the expression can't
     *     fail to evaluate, or if the expression (or its position) is not relevant for the exception.
     * @return {@code tuple[origIdx]}.
     * @throws CifEvalException If the index is out of bounds for the tuple.
     */
    public static Object project(CifTuple tuple, int idx, Expression expr) throws CifEvalException {
        if (idx < 0 || idx >= tuple.size()) {
            String msg = fmt("Index out of bounds: %s[%s].", CifEvalUtils.objToStr(tuple), idx);
            throw new CifEvalException(msg, expr);
        }

        return tuple.get(idx);
    }

    /**
     * Returns the round of a real number.
     *
     * @param x The real number.
     * @param expr The original expression, to be used for the exception. May be {@code null} if the expression can't
     *     fail to evaluate, or if the expression (or its position) is not relevant for the exception.
     * @return {@code round(x)}.
     * @throws CifEvalException If the operation results in integer overflow.
     */
    public static int round(double x, Expression expr) throws CifEvalException {
        if (x < Integer.MIN_VALUE - 0.5 || x >= Integer.MAX_VALUE + 0.5) {
            String msg = fmt("Integer overflow: round(%s).", x);
            throw new CifEvalException(msg, expr);
        }
        long rslt = Math.round(x);
        return (int)rslt;
    }

    /**
     * Returns a linearly scaled value.
     *
     * @param v The value to scale.
     * @param inmin The minimum of the input interval.
     * @param inmax The maximum of the input interval.
     * @param outmin The minimum of the output interval.
     * @param outmax The maximum of the output interval.
     * @param expr The original expression, to be used for the exception. May be {@code null} if the expression can't
     *     fail to evaluate, or if the expression (or its position) is not relevant for the exception.
     * @return {@code scale(v, inmin, inmax, outmin, outmax)}.
     * @throws CifEvalException If the input interval is empty, or the operation results in real overflow.
     */
    public static double scale(double v, double inmin, double inmax, double outmin, double outmax, Expression expr)
            throws CifEvalException
    {
        // fraction = (v - inmin) / (inmax - inmin);
        // result = outmin + fraction * (outmax - outmin);
        double inrange = subtract(inmax, inmin, expr);
        if (inrange == 0) {
            String msg = fmt("Empty input interval: scale(%s, %s, %s, %s, %s).", realToStr(v), realToStr(inmin),
                    realToStr(inmax), realToStr(outmin), realToStr(outmax));
            throw new CifEvalException(msg, expr);
        }
        double fraction = divide(subtract(v, inmin, expr), inrange, expr);
        return add(outmin, multiply(fraction, subtract(outmax, outmin, expr), expr), expr);
    }

    /**
     * Returns the sign of an integer number.
     *
     * @param x The integer number.
     * @return {@code sign(x)}.
     */
    public static int sign(int x) {
        return (x == 0) ? 0 : (x < 0) ? -1 : 1;
    }

    /**
     * Returns the sign of a real number.
     *
     * @param x The real number.
     * @return {@code sign(x)}.
     */
    public static int sign(double x) {
        return (x == 0.0) ? 0 : (x < 0.0) ? -1 : 1;
    }

    /**
     * Returns the sine of a real number.
     *
     * @param x The real number.
     * @param expr The original expression, to be used for the exception. May be {@code null} if the expression can't
     *     fail to evaluate, or if the expression (or its position) is not relevant for the exception.
     * @return {@code sin(x)}.
     * @throws CifEvalException If the operation results in real overflow, or {@code NaN}.
     */
    public static double sin(double x, Expression expr) throws CifEvalException {
        double rslt = Math.sin(x);
        if (Double.isInfinite(rslt)) {
            String msg = fmt("Real overflow: sin(%s).", x);
            throw new CifEvalException(msg, expr);
        }
        if (Double.isNaN(rslt)) {
            String msg = fmt("Invalid operation: sin(%s).", x);
            throw new CifEvalException(msg, expr);
        }
        return (rslt == -0.0) ? 0.0 : rslt;
    }

    /**
     * Returns the hyperbolic sine of a real number.
     *
     * @param x The real number.
     * @param expr The original expression, to be used for the exception. May be {@code null} if the expression can't
     *     fail to evaluate, or if the expression (or its position) is not relevant for the exception.
     * @return {@code sinh(x)}.
     * @throws CifEvalException If the operation results in real overflow, or {@code NaN}.
     */
    public static double sinh(double x, Expression expr) throws CifEvalException {
        double rslt = Math.sinh(x);
        if (Double.isInfinite(rslt)) {
            String msg = fmt("Real overflow: sinh(%s).", x);
            throw new CifEvalException(msg, expr);
        }
        if (Double.isNaN(rslt)) {
            String msg = fmt("Invalid operation: sinh(%s).", x);
            throw new CifEvalException(msg, expr);
        }
        return (rslt == -0.0) ? 0.0 : rslt;
    }

    /**
     * Slices a list. A slice is a sub-list of the original (input) list. The begin and end indices can be thought of as
     * the range of elements that should be maintained. That is, in principle, the range of element indexes
     * {@code [beginIndex .. endIndex - 1]} is maintained.
     *
     * <p>
     * Note that:
     * <ul>
     * <li>{@code slice(lst, null, 2) + slice(lst, 2, null) == lst}, for any {@code lst}.</li>
     * <li>Out of bounds slice indices are handled gracefully. An index that is too large is replaced by the list size.
     * A lower bound larger than the upper bound results in an empty list. This applies to negative indices as
     * well.</li>
     * <li>Indices may be negative numbers, to start counting from the right instead of from the left. That is,
     * {@code -1} can be used instead of {@code lst.size() - 1}, {@code -2} can be used instead of
     * {@code lst.size() - 2}, etc. However, since {@code -0} is really the same as {@code 0}, it does not count from
     * the right.</li>
     * </ul>
     * </p>
     *
     * @param lst The list.
     * @param beginIndex The 0-based begin index (inclusive), or {@code null} for index {@code 0}.
     * @param endIndex The 0-based end index (exclusive), or {@code null} for the size of the input list.
     * @return {@code lst[beginIndex:endIndex]}.
     */
    public static List<Object> slice(List<Object> lst, Integer beginIndex, Integer endIndex) {
        return Lists.slice(lst, beginIndex, endIndex);
    }

    /**
     * Slices a string. A slice is a sub-string of the original (input) string. The begin and end indices can be thought
     * of as the range of characters that should be maintained. That is, in principle, the range of character indexes
     * {@code [beginIndex .. endIndex - 1]} is maintained.
     *
     * <p>
     * Note that:
     * <ul>
     * <li>{@code slice(str, null, 2) + slice(str, 2, null) == str}, for any {@code str}.</li>
     * <li>Out of bounds slice indices are handled gracefully. An index that is too large is replaced by the string
     * length. A lower bound larger than the upper bound results in an empty string. This applies to negative indices as
     * well.</li>
     * <li>Indices may be negative numbers, to start counting from the right instead of from the left. That is,
     * {@code -1} can be used instead of {@code str.length() - 1}, {@code -2} can be used instead of
     * {@code str.length() - 2}, etc. However, since {@code -0} is really the same as {@code 0}, it does not count from
     * the right.</li>
     * </ul>
     * </p>
     *
     * @param str The string.
     * @param beginIndex The 0-based begin index (inclusive), or {@code null} for index {@code 0}.
     * @param endIndex The 0-based end index (exclusive), or {@code null} for the length of the input string.
     * @return {@code str[beginIndex:endIndex]}.
     */
    public static String slice(String str, Integer beginIndex, Integer endIndex) {
        return Strings.slice(str, beginIndex, endIndex);
    }

    /**
     * Returns the square root of a real number.
     *
     * @param x The real number.
     * @param expr The original expression, to be used for the exception. May be {@code null} if the expression can't
     *     fail to evaluate, or if the expression (or its position) is not relevant for the exception.
     * @return {@code sqrt(x)}.
     * @throws CifEvalException If the real number is negative.
     */
    public static double sqrt(double x, Expression expr) throws CifEvalException {
        // Assumes that the argument is never -0.0.
        if (x < 0.0) {
            String msg = fmt("Invalid operation: sqrt(%s).", x);
            throw new CifEvalException(msg, expr);
        }
        return Math.sqrt(x);
    }

    /**
     * Converts a CIF boolean value literal, in the CIF ASCII representation, to a Java {@link Boolean}.
     *
     * @param x The CIF boolean value literal, in the CIF ASCII representation.
     * @param expr The original expression, to be used for the exception. May be {@code null} if the expression can't
     *     fail to evaluate, or if the expression (or its position) is not relevant for the exception.
     * @return The Java {@link Boolean} value.
     * @throws CifEvalException If the string value does not represent a boolean value.
     */
    public static boolean strToBool(String x, Expression expr) throws CifEvalException {
        if (x.equals("true")) {
            return true;
        }
        if (x.equals("false")) {
            return false;
        }

        String msg = fmt("Cast from type \"string\" to type \"bool\" failed: the string value does not represent "
                + "a boolean value: \"%s\".", Strings.escape(x));
        throw new CifEvalException(msg, expr);
    }

    /**
     * Converts a CIF integer value literal, in the CIF ASCII representation, to a Java {@link Integer}.
     *
     * <p>
     * See also the {@code CifTypeChecker.transIntExpression} method.
     * </p>
     *
     * @param x The CIF integer value literal, in the CIF ASCII representation.
     * @param expr The original expression, to be used for the exception. May be {@code null} if the expression can't
     *     fail to evaluate, or if the expression (or its position) is not relevant for the exception.
     * @return The Java {@link Integer} value.
     * @throws CifEvalException If the string value does not represent an integer value.
     */
    public static int strToInt(String x, Expression expr) throws CifEvalException {
        // Integer.parseInt allows all valid integer values in CIF ASCII
        // syntax, as well as negative values (which are unary operator '-'
        // with an integer value in CIF ASCII syntax), and integer values
        // with arbitrary '0' prefixes (not allowed in CIF ASCII syntax).
        try {
            return Integer.parseInt(x);
        } catch (NumberFormatException e) {
            String msg = fmt(
                    "Cast from type \"string\" to type \"int\" failed: the string value does not represent "
                            + "an integer value, or the integer value resulted in integer overflow: \"%s\".",
                    Strings.escape(x));
            throw new CifEvalException(msg, expr);
        }
    }

    /**
     * Converts a CIF real value literal, in the CIF ASCII representation, to a Java {@link Double}.
     *
     * <p>
     * See also the {@code CifTypeChecker.transRealExpression} method.
     * </p>
     *
     * @param x The CIF real value literal, in the CIF ASCII representation.
     * @param expr The original expression, to be used for the exception. May be {@code null} if the expression can't
     *     fail to evaluate, or if the expression (or its position) is not relevant for the exception.
     * @return The Java {@link Double} value.
     * @throws CifEvalException If the string value does not represent an integer value.
     */
    public static double strToReal(String x, Expression expr) throws CifEvalException {
        // Double.parseDouble allows all valid real values in CIF ASCII syntax,
        // as well a whole bunch of additional representations, such as
        // negative values (which are unary operator '-' with an real value
        // in CIF ASCII syntax), signed integers (not allowed in the CIF
        // ASCII syntax), hexadecimal floating point notation (not allowed in
        // CIF ASCII syntax), etc.

        double rslt;
        try {
            rslt = Double.parseDouble(x);
            if (Double.isNaN(rslt)) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            String msg = fmt("Cast from type \"string\" to type \"real\" failed: the string value does not represent "
                    + "a real value: \"%s\".", Strings.escape(x));
            throw new CifEvalException(msg, expr);
        }

        if (Double.isInfinite(rslt)) {
            String msg = fmt("Cast from type \"string\" to type \"real\" failed, due to real overflow: \"%s\".",
                    Strings.escape(x));
            throw new CifEvalException(msg, expr);
        }

        return (rslt == -0.0) ? 0.0 : rslt;
    }

    /**
     * Returns the subtraction of two integer numbers.
     *
     * @param x The first integer number.
     * @param y The second integer number.
     * @param expr The original expression, to be used for the exception. May be {@code null} if the expression can't
     *     fail to evaluate, or if the expression (or its position) is not relevant for the exception.
     * @return {@code x - y}.
     * @throws CifEvalException If the operation results in integer overflow.
     */
    public static int subtract(int x, int y, Expression expr) throws CifEvalException {
        long rslt = (long)x - (long)y;
        if (Integer.MIN_VALUE <= rslt && rslt <= Integer.MAX_VALUE) {
            return (int)rslt;
        }

        String msg = fmt("Integer overflow: %d - %d.", x, y);
        throw new CifEvalException(msg, expr);
    }

    /**
     * Returns the subtraction of two real numbers.
     *
     * @param x The first real number.
     * @param y The second real number.
     * @param expr The original expression, to be used for the exception. May be {@code null} if the expression can't
     *     fail to evaluate, or if the expression (or its position) is not relevant for the exception.
     * @return {@code x - y}.
     * @throws CifEvalException If the operation results in real overflow.
     */
    public static double subtract(double x, double y, Expression expr) throws CifEvalException {
        double rslt = x - y;
        if (Double.isInfinite(rslt)) {
            String msg = fmt("Real overflow: %s - %s.", x, y);
            throw new CifEvalException(msg, expr);
        }
        return (rslt == -0.0) ? 0.0 : rslt;
    }

    /**
     * Returns the tangent of a real number.
     *
     * @param x The real number.
     * @param expr The original expression, to be used for the exception. May be {@code null} if the expression can't
     *     fail to evaluate, or if the expression (or its position) is not relevant for the exception.
     * @return {@code tan(x)}.
     * @throws CifEvalException If the operation results in real overflow, or {@code NaN}.
     */
    public static double tan(double x, Expression expr) throws CifEvalException {
        double rslt = Math.tan(x);
        if (Double.isInfinite(rslt)) {
            String msg = fmt("Real overflow: tan(%s).", x);
            throw new CifEvalException(msg, expr);
        }
        if (Double.isNaN(rslt)) {
            String msg = fmt("Invalid operation: tan(%s).", x);
            throw new CifEvalException(msg, expr);
        }
        return (rslt == -0.0) ? 0.0 : rslt;
    }

    /**
     * Returns the hyperbolic tangent of a real number.
     *
     * @param x The real number.
     * @param expr The original expression, to be used for the exception. May be {@code null} if the expression can't
     *     fail to evaluate, or if the expression (or its position) is not relevant for the exception.
     * @return {@code tanh(x)}.
     * @throws CifEvalException If the operation results in real overflow, or {@code NaN}.
     */
    public static double tanh(double x, Expression expr) throws CifEvalException {
        double rslt = Math.tanh(x);
        if (Double.isInfinite(rslt)) {
            String msg = fmt("Real overflow: tanh(%s).", x);
            throw new CifEvalException(msg, expr);
        }
        if (Double.isNaN(rslt)) {
            String msg = fmt("Invalid operation: tanh(%s).", x);
            throw new CifEvalException(msg, expr);
        }
        return (rslt == -0.0) ? 0.0 : rslt;
    }
}
