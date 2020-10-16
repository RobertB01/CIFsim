/* Simulink S-Function code for edges CIF file.
 *
 * GENERATED CODE, DO NOT EDIT
 */
#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include <limits.h>
#include <math.h>
#include <string.h>
#include <errno.h>

#define S_FUNCTION_NAME edges
#define S_FUNCTION_LEVEL 2

#include "simstruc.h"

/**
 * Support code for the CIF to C code generation.
 *
 * - CIF 'bool' maps to int (BoolType typedef). There are also TRUE and FALSE
 *   constants.
 * - CIF 'int' maps to int (IntType typedef). MinInt and MaxInt limits
 *   denote the smallest and biggest value that can be used for the integer type.
 * - CIF 'real' maps to double, wihout NaN, +Inf, -Inf, and -0.0 (RealType
 *   typedef).
 * - CIF 'string' maps to the 'StringType', with a compile-time MAX_STRING_SIZE
 *   limit for the maximal length strings that can be handled. The limit
 *   includes the terminating '\0' character.
 *
 * - The code does not attempt to provide nice error messages, since a crash
 *   is inevitable, and the crash location gives sufficient information what is
 *   wrong. 'assert' is used overflow/underflow/zero-division detection.
 */

/* Define max string length if not defined. */
#ifndef MAX_STRING_SIZE
#define MAX_STRING_SIZE 128
#endif

typedef int_T BoolType;
typedef int_T IntType;
typedef real_T RealType;

static const BoolType TRUE = 1;
static const BoolType FALSE = 0;
static const IntType MaxInt = INT_MAX;
static const IntType MinInt = INT_MIN;

/* {{{ Runtime support functions and data types declaration. */
/** String data type. */
struct StringTypeStruct {
    char data[MAX_STRING_SIZE];
};
typedef struct StringTypeStruct StringType;

/* String type support runtime code. */
static void StringTypeCopyText(StringType *s, const char *text);
static void StringTypeConcat(StringType *dest, StringType *left, StringType *right);
static IntType StringTypeSize(StringType *str);
static BoolType StringTypeEquals(StringType *left, StringType *right);
static void StringTypeProject(StringType *dst, StringType *src, IntType index);

/* Cast functions. */
static BoolType StringToBool(StringType *s);
static IntType  StringToInt(StringType *s);
static RealType StringToReal(StringType *s);

static void BoolToString(BoolType b, StringType *s);
static void  IntToString(IntType i, StringType *s);
static void RealToString(RealType r, StringType *s);

/* Format functions. */
enum FormatFlags {
    FMTFLAGS_NONE   = 0,
    FMTFLAGS_LEFT   = 1 << 0,
    FMTFLAGS_SIGN   = 1 << 1,
    FMTFLAGS_SPACE  = 1 << 2,
    FMTFLAGS_ZEROES = 1 << 3,
    FMTFLAGS_GROUPS = 1 << 4,
};

static int StringTypeAppendText(StringType *s, int end, int flags, int width, const char *text);
static int BoolTypePrint(BoolType b, char *dest, int start, int end);
static int IntTypePrint(IntType i, char *dest, int start, int end);
static int RealTypePrint(RealType r, char *dest, int start, int end);
static int StringTypePrintRaw(StringType *s, char *dest, int start, int end);
static int StringTypePrintEscaped(StringType *s, char *dest, int start, int end);

/* Standard operations for integers. */
static IntType IntegerAbs(IntType value);
static IntType IntegerDiv(IntType a, IntType b);
static IntType IntegerMod(IntType a, IntType b);
static IntType IntegerAdd(IntType a, IntType b);
static IntType IntegerSubtract(IntType a, IntType b);
static IntType IntegerMax(IntType a, IntType b);
static IntType IntegerMin(IntType a, IntType b);
static IntType IntegerMultiply(IntType a, IntType b);
static IntType IntegerNegate(IntType a);
static IntType IntegerSign(IntType value);
static IntType RealSign(RealType value);
static IntType IntegerSign(IntType value);
static IntType RealSign(RealType value);

/* Standard operations for reals. */
static RealType RealAbs(RealType value);
static RealType RealAdd(RealType a, RealType b);
static RealType RealSubtract(RealType a, RealType b);
static RealType RealDivision(RealType a, RealType b);
static RealType RealMax(RealType a, RealType b);
static RealType RealMin(RealType a, RealType b);
static RealType RealMultiply(RealType a, RealType b);
static RealType RealNegate(RealType a);

/* Real to int conversions. */
static IntType CeilFunction(RealType value);
static IntType FloorFunction(RealType a);
static IntType RoundFunction(RealType value);
static IntType IntegerPower(IntType x, IntType y);

static RealType ScaleFunction(RealType val, RealType inmin, RealType inmax,
                              RealType outmin, RealType outmax);

/* Standard library math functions. */
static RealType RealAcos(RealType arg);
static RealType RealAsin(RealType arg);
static RealType RealAtan(RealType arg);

static RealType RealCos(RealType arg);
static RealType RealSin(RealType arg);
static RealType RealTan(RealType arg);

static RealType RealExp(RealType arg);
static RealType RealLog(RealType arg);
static RealType RealLn(RealType arg);

static RealType RealSqrt(RealType a);
static RealType RealCbrt(RealType a);
/* }}} */
/* {{{ Runtime support functions definitions. */

/**
 * Get absolute value of an integer.
 * @param value Value to convert.
 * @return The absolute value of \a value.
 */
static IntType IntegerAbs(IntType value) {
    assert(value != MinInt);
    return (value < 0) ? -value : value;
}

/**
 * Integer division, while checking for overflow and zero division.
 * @param a Numerator of the division.
 * @param b Denominator of the division.
 * @return Integer value of the division closest to \c 0.
 */
static IntType IntegerDiv(IntType a, IntType b) {
    assert(a != MinInt || b != -1);
    assert(b != 0);
    return a / b;
}

/**
 * Integer remainder, while checking for zero division.
 * @param a Numerator of the division.
 * @param b Denominator of the division.
 * @return Difference between \a a and the integer division.
 * @note The invariant is a == b * IntegerDiv(a, b) + IntegerMod(a, b)
 */
static IntType IntegerMod(IntType a, IntType b) {
    assert(b != 0);
    return a % b;
}

/**
 * Get absolute value of a real number.
 * @param value Value to convert.
 * @return The absolute value of \a value.
 */
static RealType RealAbs(RealType value) {
    errno = 0;
    value = (value < 0) ? -value : value;
    assert(errno == 0);
    return (value == -0.0) ? 0.0 : value;
}

/**
 * Add two integers, checking for overflow or underflow.
 * @param a First value to add.
 * @param b Second value to add.
 * @return The sum of both values, iff there is no overflow or underflow.
 */
static IntType IntegerAdd(IntType a, IntType b) {
    long sum = a;
    sum += b;

    assert(sum >= MinInt && sum <= MaxInt);
    return sum;
}

/**
 * Add two real numbers, while checking for overflow and underflow, and normalizing the result.
 * @param a First value to add.
 * @param b Second value to add.
 * @return The normalized sum.
 */
static RealType RealAdd(RealType a, RealType b) {
    errno = 0;
    a = a + b;
    assert(errno == 0);
    return (a == -0.0) ? 0.0 : a;
}

/**
 * Subtract two integer values, while checking for underflow or overflow.
 * @param a First value to subtract.
 * @param b Second value to subtract.
 * @return The subtraction result.
 */
static IntType IntegerSubtract(IntType a, IntType b) {
    long result = a;
    result -= b;

    assert(result >= MinInt && result <= MaxInt);
    return result;
}

/**
 * Subtract two real numbers, while checking for underflow or overflow, and normalizing the result.
 * @param a First value to subtract.
 * @param b Second value to subtract.
 * @return The normalized subtraction result.
 */
static RealType RealSubtract(RealType a, RealType b) {
    errno = 0;
    a = a - b;
    assert(errno == 0);
    return (a == -0.0) ? 0.0 : a;
}

/**
 * Divide two real number, while checking for underflow or overflow, and normalizing the result.
 * @param a Numerator value of the division.
 * @param b Denominator value to division.
 * @return The normalize division result.
 */
static RealType RealDivision(RealType a, RealType b) {
    errno = 0;
    assert(b != 0.0);
    a = a / b;
    assert(errno == 0);
    return (a == -0.0) ? 0.0 : a;
}

/**
 * Retrieve the biggest value of two integers.
 * @param a First integer to inspect.
 * @param b Second integer to inspect.
 * @return the biggest value of \a a and \a b.
 */
static IntType IntegerMax(IntType a, IntType b) {
    return (a > b) ? a : b;
}

/**
 * Retrieve the biggest value of two real numbers.
 * @param a First integer to inspect.
 * @param b Second integer to inspect.
 * @return the biggest value of \a a and \a b.
 */
static RealType RealMax(RealType a, RealType b) {
    return (a > b) ? a : b;
}

/**
 * Retrieve the smallest value of two integers.
 * @param a First integer to inspect.
 * @param b Second integer to inspect.
 * @return the smallest value of \a a and \a b.
 */
static IntType IntegerMin(IntType a, IntType b) {
    return (a < b) ? a : b;
}

/**
 * Retrieve the smallest value of two real numbers.
 * @param a First integer to inspect.
 * @param b Second integer to inspect.
 * @return the smallest value of \a a and \a b.
 */
static RealType RealMin(RealType a, RealType b) {
    return (a < b) ? a : b;
}

/**
 * Multiply two integers while checking for overflow.
 * @param a First integer to multiply.
 * @param b Second integer to multiply.
 * @return The multiplication result.
 */
static IntType IntegerMultiply(IntType a, IntType b) {
    long result = a;
    result *= b;

    assert(result >= MinInt && result <= MaxInt);
    return result;
}

/**
 * Multiply two real numbers.
 * @param a First real number to multiply.
 * @param b Second real number to multiply.
 * @return The multiplication result.
 */
static RealType RealMultiply(RealType a, RealType b) {
    errno = 0;
    a *= b;
    assert(errno == 0);
    return (a == -0.0) ? 0.0 : a;
}

/**
 * Negate the given integer value while checking for overflow.
 * @param a Value to negate.
 * @return The negated \c -x value.
 */
static IntType IntegerNegate(IntType a) {
    assert(a != MinInt);
    return -a;
}

/**
 * Negate the given real number while normalizing the result.
 * @param a Value to negate.
 * @return The negated \c -x value.
 */
static RealType RealNegate(RealType a) {
    return (a == 0.0) ? a : -a;
}

/**
 * Compute the sign of an integer value.
 * @param value Value to convert.
 * @return Positive value if \a value is more than zero, a negative value if \a
 *      value is less than zero, else zero.
 */
static IntType IntegerSign(IntType value) {
    if (value > 0) return 1;
    if (value < 0) return -1;
    return 0;
}

/**
 * Compute the sign of a real number.
 * @param value Value to convert.
 * @return Positive value if \a value is more than zero, a negative value if \a
 *      value is less than zero, else zero.
 */
static IntType RealSign(RealType value) {
    if (value > 0) return 1;
    if (value < 0) return -1;
    return 0;
}

/**
 * Compute acos(x) while checking for underflow and overflow, and normalizing the result.
 * @param arg Argument of the function.
 * @return The function result.
 */
static RealType RealAcos(RealType arg) {
    errno = 0;
    RealType result = acos(arg);
    assert(errno == 0);
    return result == -0.0 ? 0.0 : result;
}

/**
 * Compute asin(x) while checking for underflow and overflow, and normalizing the result.
 * @param arg Argument of the function.
 * @return The function result.
 */
static RealType RealAsin(RealType arg) {
    errno = 0;
    RealType result = asin(arg);
    assert(errno == 0);
    return result == -0.0 ? 0.0 : result;
}

/**
 * Compute atan(x) while checking for underflow and overflow, and normalizing the result.
 * @param arg Argument of the function.
 * @return The function result.
 */
static RealType RealAtan(RealType arg) {
    errno = 0;
    RealType result = atan(arg);
    assert(errno == 0);
    return result == -0.0 ? 0.0 : result;
}

/**
 * Compute cos(x) while checking for underflow and overflow, and normalizing the result.
 * @param arg Argument of the function.
 * @return The function result.
 */
static RealType RealCos(RealType arg) {
    errno = 0;
    RealType result = cos(arg);
    assert(errno == 0);
    return result == -0.0 ? 0.0 : result;
}

/**
 * Compute sin(x) while checking for underflow and overflow, and normalizing the result.
 * @param arg Argument of the function.
 * @return The function result.
 */
static RealType RealSin(RealType arg) {
    errno = 0;
    RealType result = sin(arg);
    assert(errno == 0);
    return result == -0.0 ? 0.0 : result;
}

/**
 * Compute tan(x) while checking for underflow and overflow, and normalizing the result.
 * @param arg Argument of the function.
 * @return The function result.
 */
static RealType RealTan(RealType arg) {
    errno = 0;
    RealType result = tan(arg);
    assert(errno == 0);
    return result == -0.0 ? 0.0 : result;
}

/**
 * Compute e**x while checking for underflow and overflow, and normalizing the result.
 * @param arg Argument of the function.
 * @return The function result.
 */
static RealType RealExp(RealType arg) {
    errno = 0;
    RealType result = exp(arg);
    assert(errno == 0);
    return result == -0.0 ? 0.0 : result;
}

/**
 * Compute log_10(x) while checking for underflow and overflow, and normalizing the result.
 * @param arg Argument of the function.
 * @return The function result.
 */
static RealType RealLog(RealType arg) {
    errno = 0;
    RealType result = log10(arg);
    assert(errno == 0);
    return result == -0.0 ? 0.0 : result;
}

/**
 * Compute ln(x) while checking for underflow and overflow, and normalizing the result.
 * @param arg Argument of the function.
 * @return The function result.
 */
static RealType RealLn(RealType arg) {
    errno = 0;
    RealType result = log(arg);
    assert(errno == 0);
    return result == -0.0 ? 0.0 : result;
}

/**
 * Compute the square root of a real number while checking for underflow and overflow.
 * @param a Value to convert.
 * @return The square root of the given value \c x**(1/2).
 */
static RealType RealSqrt(RealType a) {
    errno = 0;
    a = sqrt(a);
    assert(errno == 0);
    return a; /* Cannot be -0.0. */
}

/**
 * Compute the cube root of a real number while checking for underflow and overflow.
 * @param a Value to convert.
 * @return The cube root of the given value \c x**(1/3).
 */
static RealType RealCbrt(RealType a) {
    RealType b = 1.0;

    if (a < 0) {
        a = -a;
        b = -1.0;
    }
    errno = 0;
    b *= pow(a, (1.0/3)); /* cbrt(a) mostly a**(1/3), for non-negative a. */
    assert(errno == 0);
    return (b == -0.0) ? 0.0 : b;
}

/**
 * Get biggest integer value less or equal to the given \a value.
 * @param value Upper limit of the returned value.
 * @return The biggest integer value at or below the given value.
 */
static IntType CeilFunction(RealType value) {
    RealType frac;

    assert(value >= MinInt && value <= MaxInt);
    frac = modf(value, &value);
    if (frac > 0) {
        value = value + 1;
        assert(value <= MaxInt);
    }
    return (IntType)value;
}

/**
 * The smallest integer at or above the given \a value.
 * @param value Limit of the returned value.
 * @return The smallest integer at or above the given value.
 */
static IntType FloorFunction(RealType value) {
    RealType frac;

    assert(isfinite(value) && value >= MinInt && value <= MaxInt);
    frac = modf(value, &value);
    if (frac < 0) {
        value = value - 1;
        assert(value >= MinInt);
    }
    return value;
}

/**
 * Round a real number to its nearest integral value while checking for underflow or overflow.
 * @param value Value to round.
 * @return The nearest integral value of \a value.
 */
static IntType RoundFunction(RealType value) {
    RealType frac;

    value = value + 0.5;
    /* Perform floor. */
    assert(value >= MinInt && value <= MaxInt);
    frac = modf(value, &value);
    if (frac < 0) {
        value = value - 1;
        assert(value >= MinInt);
    }
    return value;
}

/**
 * Power function for two integer numbers, while checking for underflow or overflow.
 * @param px Base value.
 * @param y Exponent value.
 * @return The result of the power function \c x**y.
 */
static IntType IntegerPower(IntType px, IntType y) {
    // Compute x**y, for y >= 0.
    long x = px;
    long z = 1;

    while (y > 0) {
        while ((y & 1) == 0) {
            y /= 2;
            x *= x;
            assert(x >= MinInt && x <= MaxInt);
        }
        y--;
        z *= x;
        assert(z >= MinInt && z <= MaxInt);
    }
    return z;
}


/**
 * Scale a value in an input range to an equivalent value in an output range.
 * @param val Value to convert.
 * @param inmin Lower boundary of the input range.
 * @param inmax Upper boundary of the input range.
 * @param outmin Lower boundary of the output range.
 * @param outmax Upper boundary of the output range.
 * @return The converted value.
 */
static RealType ScaleFunction(RealType val, RealType inmin, RealType inmax,
                              RealType outmin, RealType outmax)
{
    errno = 0;
    RealType fraction = (val - inmin) / (inmax - inmin);
    fraction = outmin + fraction * (outmax - outmin);
    assert(errno == 0);
    return (fraction == -0.0) ? 0.0 : fraction;
}

/**
 * Verify whether two strings are the same.
 * @param left First string to compare.
 * @param right Second string to compare.
 * @return Whether both strings are equal.
 */
static BoolType StringTypeEquals(StringType *left, StringType *right) {
    const char *left_p, *right_p;
    int i;

    if (left == right) return TRUE;
    left_p = left->data;
    right_p = right->data;
    for (i = 0; i < MAX_STRING_SIZE; i++) {
        if (*left_p != *right_p) return FALSE;
        if (*left_p == '\0') return TRUE;
        left_p++;
        right_p++;
    }
    assert(FALSE); /* String is too long. */
    return FALSE; /* In case the assert is removed. */
}

/**
 * Extract a character from the \a src string.
 * @param dst Destination, contains a string consisting of a single character afterwards.
 * @param src Source string to project from.
 * @param index Unnormalized index in the \a src string.
 */
static void StringTypeProject(StringType *dst, StringType *src, IntType index) {
    IntType length = StringTypeSize(src);
    if (index < 0) index += length;
    assert(index >= 0 && index < length);

    dst->data[0] = src->data[index];
    dst->data[1] = '\0';
}

static const char *true_val = "true";
static const char *false_val = "false";

/** Parse a boolean value from the string. */
static BoolType StringToBool(StringType *s) {
    if (strcmp(s->data, true_val) == 0) return TRUE;
    if (strcmp(s->data, false_val) == 0) return FALSE;

    fprintf(stderr, "Error while parsing a string to a boolean, text is neither 'true' nor 'false.'\n");
    assert(0);
    return FALSE; /* Default value in case the assert is not included. */
}

/** Parse an integer value from the string. */
static IntType StringToInt(StringType *s) {
    char *endptr;
    IntType result;

    if (s->data[0] != '\0') {
        result = strtol(s->data, &endptr, 10);
        if (*endptr == '\0') return result;
    }
    fprintf(stderr, "Error while parsing a string to an integer.\n");
    assert(0);
    return 0; /* Default value in case the assert is not included. */
}

/** Parse a real number from the string. */
static RealType StringToReal(StringType *s) {
    char *endptr;
    IntType result;

    if (s->data[0] != '\0') {
        errno = 0;
        result = strtod(s->data, &endptr);
        if (*endptr == '\0') {
            assert(errno == 0);
            return result == -0.0 ? 0.0 : result;
        }
    }
    fprintf(stderr, "Error while parsing a string to an integer.\n");
    assert(0);
    return 0; /* Default value in case the assert is not included. */
}

/**
 * Copy \a src text from \c dest[start] upto (but not including) \c dest[end]
 * until running out of destination buffer, or running out of source characters.
 * @param dest Destination buffer to write into.
 * @param start First offset in \a dest to write to.
 * @param end First offset after \a dest.
 * @param src Source text (nul-terminated).
 * @return First free index in \a dest (on the nul-terminator).
 */
static int StringCopyToEnd(char *dest, int start, int end, const char *src) {
    int last;

    dest += start;
    last = end - 1;
    assert(start <= last);

    while (start < last && *src) {
        *dest = *src;
        dest++;
        src++;
        start++;
    }
    *dest = '\0';
    return start;
}

/**
 * Copy string literal \a text into the string type.
 * @param s Destination to write into.
 * @param text Literal text to copy.
 */
static void StringTypeCopyText(StringType *s, const char *text) {
    StringCopyToEnd(s->data, 0, MAX_STRING_SIZE, text);
}

/**
 * Copy \a src, for \a src_length characters to \a dest, starting at offset \a dest_start.
 * Destination is NOT terminated.
 * @param dest Destination string.
 * @param dest_start Start offset of the first copy operation in the destination.
 * @param src Source string.
 * @param src_length Number of characters to copy.
 * @return New offset of the destination.
 */
static IntType CopyLoop(StringType *dest, IntType dest_start, StringType *src, IntType src_length) {
    char *dest_data = dest->data + dest_start;
    char *src_data = src->data;

    while (dest_start < MAX_STRING_SIZE - 1 && src_length > 0) {
        *dest_data = *src_data;
        dest_data++;
        src_data++;

        dest_start++;
        src_length--;
    }
    return dest_start;
}

/**
 * Concatenate two strings into a third. In particular, any of the strings may be shared without causing trouble.
 * @param dest Destination of the concatenated result.
 * @param left First string to use.
 * @param right Second string to use.
 */
static void StringTypeConcat(StringType *dest, StringType *left, StringType *right) {
    IntType left_length  = StringTypeSize(left);
    IntType right_length = StringTypeSize(right);
    IntType dest_start = 0; /* Start offset for copying in the destination string. */

    if (dest == left) {
        if (dest == right) {
            /* All 3 buffers are the same. */
            dest_start = left_length;
            dest_start = CopyLoop(dest, dest_start, right, right_length);
            dest->data[dest_start] = '\0';
            return;
        } else {
            /* dest, left shared */
            dest_start = left_length;
            dest_start = CopyLoop(dest, dest_start, right, right_length);
            dest->data[dest_start] = '\0';
            return;
        }
    } else if (dest == right) {
        char *dest_data, *src_data;

        /* dest and right shared */
        if (left_length == 0) return;

        /* Move dest (== right) to make space for left string. */
        right_length = IntegerMin(MAX_STRING_SIZE - 2, left_length + right_length) - left_length;
        /* Copy dest (== right) from end to start, adding a terminator. */
        dest_data = dest->data + left_length + right_length + 1; /* offset <= MAX_STRING_SIZE - 1 */
        *dest_data = '\0';
        dest_data--;
        right_length--;
        src_data = &dest->data[right_length]; /* == right[right_length]. */
        while (right_length >= 0) { /* 0 also gets copied. */
            *dest_data = *src_data;
            dest_data--;
            src_data--;
            right_length--;
        }
        assert(dest->data + left_length - 1 == dest_data); /* Should have exactly enough space for left. */

        dest_start = CopyLoop(dest, 0, left, left_length); /* Insert left string in front. */
        return;

    } else {
        /* left, right shared, or all buffers different. */
        dest_start = CopyLoop(dest, dest_start, left, left_length);
        dest_start = CopyLoop(dest, dest_start, right, right_length);
        dest->data[dest_start] = '\0';
        return;
    }
}

/**
 * Get the length of a string.
 * @param str String to inspect.
 * @return Length of the given string.
 */
IntType StringTypeSize(StringType *str) {
    /* strnlen */
    IntType length = 0;
    const char *k = str->data;

    while (length < MAX_STRING_SIZE && *k) { length++; k++; }
    assert(length < MAX_STRING_SIZE); /* String should have a terminating nul-character. */

    return length;
}

/**
 * Append a boolean value to a text output buffer.
 * @param b Value to convert.
 * @param dest Start of the destination buffer.
 * @param start First offset in \a dest to write to.
 * @param end First offset after \a dest.
 * @return New offset of the destination.
 */
static int BoolTypePrint(BoolType b, char *dest, int start, int end) {
    return StringCopyToEnd(dest, start, end, (b ? true_val : false_val));
}

/**
 * Append a integer value to a text output buffer.
 * @param i Value to convert.
 * @param dest Start of the destination buffer.
 * @param start First offset in \a dest to write to.
 * @param end First offset after \a dest.
 * @return New offset of the destination.
 */
static int IntTypePrint(IntType i, char *dest, int start, int end) {
    char buffer[128];

    sprintf(buffer, "%d", i);
    return StringCopyToEnd(dest, start, end, buffer);
}

/**
 * Append a real number value to a text output buffer.
 * @param r Value to convert.
 * @param dest Start of the destination buffer.
 * @param start First offset in \a dest to write to.
 * @param end First offset after \a dest.
 * @return New offset of the destination.
 */
static int RealTypePrint(RealType r, char *dest, int start, int end) {
    char buffer[128];

    sprintf(buffer, "%g", r);
    return StringCopyToEnd(dest, start, end, buffer);
}

/**
 * Append a string value to a text output buffer (as-is).
 * @param s Value to convert.
 * @param dest Start of the destination buffer.
 * @param start First offset in \a dest to write to.
 * @param end First offset after \a dest.
 * @return New offset of the destination.
 */
static int StringTypePrintRaw(StringType *s, char *dest, int start, int end) {
    return StringCopyToEnd(dest, start, end, s->data);
}

/**
 * Append a string value to a text output buffer (escaped \n, \t, \", and \).
 * @param s Value to convert.
 * @param dest Start of the destination buffer.
 * @param start First offset in \a dest to write to.
 * @param end First offset after \a dest.
 * @return New offset of the destination.
 */
static int StringTypePrintEscaped(StringType *s, char *dest, int start, int end) {
    /* Setup escaped version of the string. */
    char buffer[MAX_STRING_SIZE];
    int free = MAX_STRING_SIZE - 1;
    char *dp = buffer;
    const char *ps;

    if (free > 0) { /* Add opening dquote. */
        *dp++ = '\"'; free--;
    }

    ps = s->data;
    while (*ps) {
        if (*ps == '\n') {
            if (free > 0) {
                *dp++ = '\\'; free--;
                if (free > 0) {
                    *dp++ = 'n'; free--;
                }
            }
        } else if (*ps == '\t') {
            if (free > 0) {
                *dp++ = '\\'; free--;
                if (free > 0) {
                    *dp++ = 't'; free--;
                }
            }
        } else if (*ps == '\"') {
            if (free > 0) {
                *dp++ = '\\'; free--;
                if (free > 0) {
                    *dp++ = '\"'; free--;
                }
            }
        } else if (*ps == '\\') {
            if (free > 0) {
                *dp++ = '\\'; free--;
                if (free > 0) {
                    *dp++ = '\\'; free--;
                }
            }
        } else {
            if (free > 0) {
                *dp++ = *ps; free--;
            }
        }
        ps++;
    }

    if (free > 0) { /* Add closing dquote. */
        *dp++ = '\"'; free--;
    }
    *dp = '\0';
    return StringCopyToEnd(dest, start, end, buffer);
}

/**
 * Convert boolean value to 'true' or 'false' text.
 * @param b Boolean to convert.
 * @param s Destination string to write.
 */
static void BoolToString(BoolType b, StringType *s) {
    BoolTypePrint(b, s->data, 0, MAX_STRING_SIZE);
}

/**
 * Convert integer value to a text with decimal digits.
 * @param i Integer to convert.
 * @param s Destination string to write.
 */
static void IntToString(IntType i, StringType *s) {
    IntTypePrint(i, s->data, 0, MAX_STRING_SIZE);
}

/** Convert a real number to a string representation.
 * @param r Real number to convert.
 * @param s Destination string to write.
 */
static void RealToString(RealType r, StringType *s) {
    RealTypePrint(r, s->data, 0, MAX_STRING_SIZE);
}

/**
 * Append a given text to the end of the \a s string, for as far as it fits.
 * @param s String to append to.
 * @param end Current end offset of the string.
 * @param flags Formatting flags.
 * @param width Minimum width.
 * @param text Text to append.
 * @return New end of the string.
 */
static int StringTypeAppendText(StringType *s, int end, int flags, int width, const char *text) {
    int length = strlen(text);
    int offset;

    int grp_offset; /* Offset of the first "," in the text. */
    const char *tp;

    /* Compute position and count of the "," group separators. */
    if ((flags & FMTFLAGS_GROUPS) != 0) {
        int first_digit = length;
        tp = text;
        grp_offset = 0;
        while (*tp && *tp != '.') { /* Find the end of the group formatting. */
            if (first_digit > grp_offset && *tp >= '0' && *tp <= '9') first_digit = grp_offset;
            grp_offset++;
            tp++;
        }
        /* first_digit is the offset of the first digit in the text.
         * grp_offset is positioned just behind the last triplet.
         */
        if (grp_offset - 3 <= first_digit) {
            grp_offset = -1; /* Not enough digits for ",". */
        } else {
            while (grp_offset - 3 > first_digit) {
                grp_offset -= 3;
                length++; /* Count additional group "," too in the length. */
            }
        }
    } else {
        grp_offset = -1; /* Avoid offset ever matching grp_offset. */
    }

    if (((flags & FMTFLAGS_SPACE) != 0 || (flags & FMTFLAGS_SIGN) != 0) && *text != '-') length++;

    /* Prefix spaces if needed. */
    if ((flags & FMTFLAGS_LEFT) == 0 && (flags & FMTFLAGS_ZEROES) == 0) {
        /* Right alignment requested with spaces. */
        while (length < width) {
            if (end < MAX_STRING_SIZE - 1) s->data[end++] = ' ';
            width--;
        }
    }

    /* Space or sign prefix */
    if ((flags & FMTFLAGS_SPACE) != 0 && *text != '-') {
        /* Non-negative number, prefix with space. */
        if (end < MAX_STRING_SIZE - 1) s->data[end++] = ' ';
    } else if ((flags & FMTFLAGS_SIGN) != 0 && *text != '-') {
        /* Non-negative number, prefix with +. */
        if (end < MAX_STRING_SIZE - 1) s->data[end++] = '+';
    }

    /* Prefix zeroes. */
    if ((flags & FMTFLAGS_LEFT) == 0 && (flags & FMTFLAGS_ZEROES) != 0) {
        while (length < width) {
            if (end < MAX_STRING_SIZE - 1) s->data[end++] = '0';
            width--;
        }
    }

    /* Print the text, with grouping if requested. */
    tp = text;
    offset = 0;
    while (*tp) {
        if (offset == grp_offset) {
            if (end < MAX_STRING_SIZE - 1) s->data[end++] = ',';
            grp_offset += 3;
        }
        if (end < MAX_STRING_SIZE - 1) s->data[end++] = *tp;
        if (*tp == '.') grp_offset = -1; /* Make sure offset never matches grp_offset any more. */

        offset++;
        tp++;
    }

    if ((flags & FMTFLAGS_LEFT) != 0) { /* Left alignment requested. */
        while (length < width) {
            if (end < MAX_STRING_SIZE - 1) s->data[end++] = ' ';
            width--;
        }
    }
    s->data[end] = '\0';
    return end;
}

/* }}} */

/* {{{ CIF types. */
/* CIF type: list[5] int */
struct A5I_struct {
    int_T data[5];
};
typedef struct A5I_struct A5IType;

static BoolType A5ITypeEquals(A5IType *left, A5IType *right);
static int_T A5ITypeProject(A5IType *array, IntType index);
static void A5ITypeModify(A5IType *array, IntType index, int_T value);
static int A5ITypePrint(A5IType *array, char *dest, int start, int end);
static A5IType A5ITypeFromSimulink(real_T *vec);
static void A5ITypeToSimulink(real_T *vec, A5IType *arr);

/* CIF type: tuple(int a; int b) */
struct T2II_struct {
    int_T _field0;
    int_T _field1;
};
typedef struct T2II_struct T2IIType;

static BoolType T2IITypeEquals(T2IIType *left, T2IIType *right);
static int T2IITypePrint(T2IIType *tuple, char *dest, int start, int end);

/* CIF type: tuple(tuple(int a; int b) t; string c) */
struct T2T2IIS_struct {
    T2IIType _field0;
    StringType _field1;
};
typedef struct T2T2IIS_struct T2T2IISType;

static BoolType T2T2IISTypeEquals(T2T2IISType *left, T2T2IISType *right);
static int T2T2IISTypePrint(T2T2IISType *tuple, char *dest, int start, int end);

/* CIF type: list[3] int */
struct A3I_struct {
    int_T data[3];
};
typedef struct A3I_struct A3IType;

static BoolType A3ITypeEquals(A3IType *left, A3IType *right);
static int_T A3ITypeProject(A3IType *array, IntType index);
static void A3ITypeModify(A3IType *array, IntType index, int_T value);
static int A3ITypePrint(A3IType *array, char *dest, int start, int end);
static A3IType A3ITypeFromSimulink(real_T *vec);
static void A3ITypeToSimulink(real_T *vec, A3IType *arr);

/* CIF type: list[2] list[3] int */
struct A2A3I_struct {
    A3IType data[2];
};
typedef struct A2A3I_struct A2A3IType;

static BoolType A2A3ITypeEquals(A2A3IType *left, A2A3IType *right);
static A3IType *A2A3ITypeProject(A2A3IType *array, IntType index);
static void A2A3ITypeModify(A2A3IType *array, IntType index, A3IType *value);
static int A2A3ITypePrint(A2A3IType *array, char *dest, int start, int end);
static A2A3IType A2A3ITypeFromSimulink(real_T *vec);
static void A2A3ITypeToSimulink(real_T *vec, A2A3IType *mat);

/* CIF type: list[1] int */
struct A1I_struct {
    int_T data[1];
};
typedef struct A1I_struct A1IType;

static BoolType A1ITypeEquals(A1IType *left, A1IType *right);
static int_T A1ITypeProject(A1IType *array, IntType index);
static void A1ITypeModify(A1IType *array, IntType index, int_T value);
static int A1ITypePrint(A1IType *array, char *dest, int start, int end);
static A1IType A1ITypeFromSimulink(real_T *vec);
static void A1ITypeToSimulink(real_T *vec, A1IType *arr);

/* CIF type: list[1] real */
struct A1R_struct {
    real_T data[1];
};
typedef struct A1R_struct A1RType;

static BoolType A1RTypeEquals(A1RType *left, A1RType *right);
static real_T A1RTypeProject(A1RType *array, IntType index);
static void A1RTypeModify(A1RType *array, IntType index, real_T value);
static int A1RTypePrint(A1RType *array, char *dest, int start, int end);
static A1RType A1RTypeFromSimulink(real_T *vec);
static void A1RTypeToSimulink(real_T *vec, A1RType *arr);

/* CIF type: tuple(list[1] int x; list[1] real y) */
struct T2A1IA1R_struct {
    A1IType _field0;
    A1RType _field1;
};
typedef struct T2A1IA1R_struct T2A1IA1RType;

static BoolType T2A1IA1RTypeEquals(T2A1IA1RType *left, T2A1IA1RType *right);
static int T2A1IA1RTypePrint(T2A1IA1RType *tuple, char *dest, int start, int end);

/* CIF type: list[2] tuple(list[1] int x; list[1] real y) */
struct A2T2A1IA1R_struct {
    T2A1IA1RType data[2];
};
typedef struct A2T2A1IA1R_struct A2T2A1IA1RType;

static BoolType A2T2A1IA1RTypeEquals(A2T2A1IA1RType *left, A2T2A1IA1RType *right);
static T2A1IA1RType *A2T2A1IA1RTypeProject(A2T2A1IA1RType *array, IntType index);
static void A2T2A1IA1RTypeModify(A2T2A1IA1RType *array, IntType index, T2A1IA1RType *value);
static int A2T2A1IA1RTypePrint(A2T2A1IA1RType *array, char *dest, int start, int end);

/* CIF type: tuple(string s; list[2] tuple(list[1] int x; list[1] real y) z) */
struct T2SA2T2A1IA1R_struct {
    StringType _field0;
    A2T2A1IA1RType _field1;
};
typedef struct T2SA2T2A1IA1R_struct T2SA2T2A1IA1RType;

static BoolType T2SA2T2A1IA1RTypeEquals(T2SA2T2A1IA1RType *left, T2SA2T2A1IA1RType *right);
static int T2SA2T2A1IA1RTypePrint(T2SA2T2A1IA1RType *tuple, char *dest, int start, int end);

/* CIF type: list[3] tuple(int a; int b) */
struct A3T2II_struct {
    T2IIType data[3];
};
typedef struct A3T2II_struct A3T2IIType;

static BoolType A3T2IITypeEquals(A3T2IIType *left, A3T2IIType *right);
static T2IIType *A3T2IITypeProject(A3T2IIType *array, IntType index);
static void A3T2IITypeModify(A3T2IIType *array, IntType index, T2IIType *value);
static int A3T2IITypePrint(A3T2IIType *array, char *dest, int start, int end);

enum Enumedges_ {
    _edges_loc1,
    _edges_loc2,
    _edges_loc3,
    _edges_X,
};
typedef enum Enumedges_ edgesEnum;

static const char *enum_names[];
static int EnumTypePrint(edgesEnum value, char *dest, int start, int end);

/* }}} */

/* {{{ Elementary CIF element type <-> Simulink conversions. */
/**
 * Conversion function from CIF boolean value to Simulink value.
 * @param b CIF bool value to convert.
 * @return The converted value.
 */
static real_T BoolToSimulink(BoolType b) {
    return b ? 1.0 : 0.0;
}

/**
 * Conversion function from Simulink value to CIF bool value.
 * @param sr Simulink value to convert.
 * @return The converted value.
 */
static BoolType SimulinkToBool(real_T sr) {
    assert(isfinite(sr));
    return (sr == 0.0 || sr == -0.0) ? FALSE : TRUE;
}

/**
 * Conversion function from CIF int value to Simulink value.
 * @param i CIF integer value to convert.
 * @return The converted value.
 */
static real_T IntToSimulink(int_T i) {
    return i;
}

/**
 * Conversion function from Simulink value to CIF enum value.
 * @param sr Simulink value to convert.
 * @return The converted value.
 */
static int_T SimulinkToEnum(real_T sr) {
    assert(isfinite(sr));

    int val = sr;
    assert(val >= 0 && val < ENUM_NAMES_COUNT);
    return val;
}

/**
 * Conversion function from Simulink value to CIF int value.
 * @param sr Simulink value to convert.
 * @return The converted value.
 */
static int_T SimulinkToInt(real_T sr) {
    assert(isfinite(sr));
    return sr;
}

/**
 * Conversion function from CIF real value to Simulink value.
 * @param r CIF real value to convert.
 * @return The converted value.
 */
static real_T RealToSimulink(real_T r) {
    return r;
}

/**
 * Conversion function from Simulink value to CIF real value.
 * @param sr Simulink value to convert.
 * @return The converted value.
 */
static real_T SimulinkToReal(real_T sr) {
    assert(isfinite(sr));
    return (sr == -0.0) ? 0.0 : sr;
}
/* }}} */

/* {{{ Type functions. */
/**
 * Compare two arrays for equality.
 * @param left First array to compare.
 * @param right Second array to compare.
 * @return Whether both arrays are the same.
 */
static BoolType A5ITypeEquals(A5IType *left, A5IType *right) {
    if (left == right) return TRUE;
    return memcmp(left, right, sizeof(A5IType)) == 0;
}

/**
 * Extract an element from the array.
 * @param array Array with value to retrieve.
 * @param index Element index in the array (not normalized).
 * @return Element value at the indicated index from the array.
 */
static int_T A5ITypeProject(A5IType *array, IntType index) {
    if (index < 0) index += 5; /* Normalize index. */
    assert(index >= 0 && index < 5);

    return array->data[index];
}

/**
 * In-place change of the array.
 * @param array Array to modify.
 * @param index Element index in the array (not normalized).
 * @param value New value to copy into the array.
 */
static void A5ITypeModify(A5IType *array, IntType index, int_T value) {
    if (index < 0) index += 5; /* Normalize index. */
    assert(index >= 0 && index < 5);

    array->data[index] = value;
}

/**
 * Append textual representation of the array value into the provided
 * destination, space permitting.
 * @param array Array to print.
 * @param dest Destination to write text to.
 * @param start First available offset in \a dest for new text.
 * @param end Fist offset behind \a dest.
 * @return First free offset in \a dest, mat be \a end.
 */
static int A5ITypePrint(A5IType *array, char *dest, int start, int end) {
    int last = end - 1;
    if (start < last) { dest[start++] = '['; }
    int index;
    for (index = 0; index < 5; index++) {
        if (index > 0) {
            if (start < last) { dest[start++] = ','; }
            if (start < last) { dest[start++] = ' '; }
        }
        start = IntTypePrint(array->data[index], dest, start, end);
    }
    if (start < last) { dest[start++] = ']'; }
    dest[start] = '\0';
    return start;
}

/**
 * Construct a CIF array from a Simulink vector.
 * @param vec Simulink vector to copy.
 * @return The constructed array.
 */
static A5IType A5ITypeFromSimulink(real_T *vec) {
    A5IType result;
    int i;
    for (i = 0; i < 5; i++) result.data[i] = SimulinkToInt(vec[i]);
    return result;
}

/**
 * Fill a Simulink vector from a CIF array.
 * @param vec Simulink vector to copy to.
 * @param arr Source array to get values from.
 */
static void A5ITypeToSimulink(real_T *vec, A5IType *arr) {
    int i;
    for (i = 0; i < 5; i++) vec[i] = IntToSimulink(arr->data[i]);
}

/**
 * Compare two tuples for equality.
 * @param left First tuple to compare.
 * @param right Second tuple to compare.
 * @return Whether both tuples are the same.
 */
static BoolType T2IITypeEquals(T2IIType *left, T2IIType *right) {
    if (left == right) return TRUE;
    if (memcmp(&left->_field0, &right->_field0, sizeof(int_T)) != 0) return FALSE;
    if (memcmp(&left->_field1, &right->_field1, sizeof(int_T)) != 0) return FALSE;
    return TRUE;
}

/**
 * Append textual representation of the tuple value into the provided
 * destination, space permitting.
 * @param tuple Tuple to print.
 * @param dest Destination to write text to.
 * @param start First available offset in \a dest for new text.
 * @param end Fist offset behind \a dest.
 * @return First free offset in \a dest, mat be \a end.
 */
static int T2IITypePrint(T2IIType *tuple, char *dest, int start, int end) {
    int last = end - 1;
    if (start < last) { dest[start++] = '('; }
    start = IntTypePrint(tuple->_field0, dest, start, end);
    if (start < last) { dest[start++] = ','; }
    if (start < last) { dest[start++] = ' '; }
    start = IntTypePrint(tuple->_field1, dest, start, end);
    if (start < last) { dest[start++] = ')'; }
    dest[start] = '\0';
    return start;
}

/**
 * Compare two tuples for equality.
 * @param left First tuple to compare.
 * @param right Second tuple to compare.
 * @return Whether both tuples are the same.
 */
static BoolType T2T2IISTypeEquals(T2T2IISType *left, T2T2IISType *right) {
    if (left == right) return TRUE;
    if (!(T2IITypeEquals(&(left->_field0), &(right->_field0)))) return FALSE;
    if (!(StringTypeEquals(&(left->_field1), &(right->_field1)))) return FALSE;
    return TRUE;
}

/**
 * Append textual representation of the tuple value into the provided
 * destination, space permitting.
 * @param tuple Tuple to print.
 * @param dest Destination to write text to.
 * @param start First available offset in \a dest for new text.
 * @param end Fist offset behind \a dest.
 * @return First free offset in \a dest, mat be \a end.
 */
static int T2T2IISTypePrint(T2T2IISType *tuple, char *dest, int start, int end) {
    int last = end - 1;
    if (start < last) { dest[start++] = '('; }
    start = T2IITypePrint(&tuple->_field0, dest, start, end);
    if (start < last) { dest[start++] = ','; }
    if (start < last) { dest[start++] = ' '; }
    start = StringTypePrintEscaped(&tuple->_field1, dest, start, end);
    if (start < last) { dest[start++] = ')'; }
    dest[start] = '\0';
    return start;
}

/**
 * Compare two arrays for equality.
 * @param left First array to compare.
 * @param right Second array to compare.
 * @return Whether both arrays are the same.
 */
static BoolType A3ITypeEquals(A3IType *left, A3IType *right) {
    if (left == right) return TRUE;
    return memcmp(left, right, sizeof(A3IType)) == 0;
}

/**
 * Extract an element from the array.
 * @param array Array with value to retrieve.
 * @param index Element index in the array (not normalized).
 * @return Element value at the indicated index from the array.
 */
static int_T A3ITypeProject(A3IType *array, IntType index) {
    if (index < 0) index += 3; /* Normalize index. */
    assert(index >= 0 && index < 3);

    return array->data[index];
}

/**
 * In-place change of the array.
 * @param array Array to modify.
 * @param index Element index in the array (not normalized).
 * @param value New value to copy into the array.
 */
static void A3ITypeModify(A3IType *array, IntType index, int_T value) {
    if (index < 0) index += 3; /* Normalize index. */
    assert(index >= 0 && index < 3);

    array->data[index] = value;
}

/**
 * Append textual representation of the array value into the provided
 * destination, space permitting.
 * @param array Array to print.
 * @param dest Destination to write text to.
 * @param start First available offset in \a dest for new text.
 * @param end Fist offset behind \a dest.
 * @return First free offset in \a dest, mat be \a end.
 */
static int A3ITypePrint(A3IType *array, char *dest, int start, int end) {
    int last = end - 1;
    if (start < last) { dest[start++] = '['; }
    int index;
    for (index = 0; index < 3; index++) {
        if (index > 0) {
            if (start < last) { dest[start++] = ','; }
            if (start < last) { dest[start++] = ' '; }
        }
        start = IntTypePrint(array->data[index], dest, start, end);
    }
    if (start < last) { dest[start++] = ']'; }
    dest[start] = '\0';
    return start;
}

/**
 * Construct a CIF array from a Simulink vector.
 * @param vec Simulink vector to copy.
 * @return The constructed array.
 */
static A3IType A3ITypeFromSimulink(real_T *vec) {
    A3IType result;
    int i;
    for (i = 0; i < 3; i++) result.data[i] = SimulinkToInt(vec[i]);
    return result;
}

/**
 * Fill a Simulink vector from a CIF array.
 * @param vec Simulink vector to copy to.
 * @param arr Source array to get values from.
 */
static void A3ITypeToSimulink(real_T *vec, A3IType *arr) {
    int i;
    for (i = 0; i < 3; i++) vec[i] = IntToSimulink(arr->data[i]);
}

/**
 * Compare two arrays for equality.
 * @param left First array to compare.
 * @param right Second array to compare.
 * @return Whether both arrays are the same.
 */
static BoolType A2A3ITypeEquals(A2A3IType *left, A2A3IType *right) {
    if (left == right) return TRUE;
    return memcmp(left, right, sizeof(A2A3IType)) == 0;
}

/**
 * Extract an element from the array.
 * @param array Array with value to retrieve.
 * @param index Element index in the array (not normalized).
 * @return Element value at the indicated index from the array.
 */
static A3IType *A2A3ITypeProject(A2A3IType *array, IntType index) {
    if (index < 0) index += 2; /* Normalize index. */
    assert(index >= 0 && index < 2);

    return &array->data[index];
}

/**
 * In-place change of the array.
 * @param array Array to modify.
 * @param index Element index in the array (not normalized).
 * @param value New value to copy into the array.
 */
static void A2A3ITypeModify(A2A3IType *array, IntType index, A3IType *value) {
    if (index < 0) index += 2; /* Normalize index. */
    assert(index >= 0 && index < 2);

    memcpy(&array->data[index], value, sizeof(A3IType));
}

/**
 * Append textual representation of the array value into the provided
 * destination, space permitting.
 * @param array Array to print.
 * @param dest Destination to write text to.
 * @param start First available offset in \a dest for new text.
 * @param end Fist offset behind \a dest.
 * @return First free offset in \a dest, mat be \a end.
 */
static int A2A3ITypePrint(A2A3IType *array, char *dest, int start, int end) {
    int last = end - 1;
    if (start < last) { dest[start++] = '['; }
    int index;
    for (index = 0; index < 2; index++) {
        if (index > 0) {
            if (start < last) { dest[start++] = ','; }
            if (start < last) { dest[start++] = ' '; }
        }
        start = A3ITypePrint(&array->data[index], dest, start, end);
    }
    if (start < last) { dest[start++] = ']'; }
    dest[start] = '\0';
    return start;
}

/**
 * Construct a CIF array from a Simulink matrix.
 * @param vec Simulink matrix to copy.
 * @return The constructed array.
 */
static A2A3IType A2A3ITypeFromSimulink(real_T *vec) {
    A2A3IType result;
    int r;
    for (r = 0; r < 2; r++) {
        int c;
        for (c = 0; c < 3; r++) result.data[r].data[c] = SimulinkToInt(vec[r + c * 2]);
    }
    return result;
}

/**
 * Fill a Simulink matrix from a CIF array.
 * @param vec Simulink matrix to copy to.
 * @param mat Source array to get values from.
 */
static void A2A3ITypeToSimulink(real_T *vec, A2A3IType *mat) {
    int r;
    for (r = 0; r < 2; r++) {
        int c;
        for (c = 0; c < 3; c++) vec[r + c * 2] = IntToSimulink(mat->data[r].data[c]);
    }
}

/**
 * Compare two arrays for equality.
 * @param left First array to compare.
 * @param right Second array to compare.
 * @return Whether both arrays are the same.
 */
static BoolType A1ITypeEquals(A1IType *left, A1IType *right) {
    if (left == right) return TRUE;
    return memcmp(left, right, sizeof(A1IType)) == 0;
}

/**
 * Extract an element from the array.
 * @param array Array with value to retrieve.
 * @param index Element index in the array (not normalized).
 * @return Element value at the indicated index from the array.
 */
static int_T A1ITypeProject(A1IType *array, IntType index) {
    if (index < 0) index += 1; /* Normalize index. */
    assert(index >= 0 && index < 1);

    return array->data[index];
}

/**
 * In-place change of the array.
 * @param array Array to modify.
 * @param index Element index in the array (not normalized).
 * @param value New value to copy into the array.
 */
static void A1ITypeModify(A1IType *array, IntType index, int_T value) {
    if (index < 0) index += 1; /* Normalize index. */
    assert(index >= 0 && index < 1);

    array->data[index] = value;
}

/**
 * Append textual representation of the array value into the provided
 * destination, space permitting.
 * @param array Array to print.
 * @param dest Destination to write text to.
 * @param start First available offset in \a dest for new text.
 * @param end Fist offset behind \a dest.
 * @return First free offset in \a dest, mat be \a end.
 */
static int A1ITypePrint(A1IType *array, char *dest, int start, int end) {
    int last = end - 1;
    if (start < last) { dest[start++] = '['; }
    int index;
    for (index = 0; index < 1; index++) {
        if (index > 0) {
            if (start < last) { dest[start++] = ','; }
            if (start < last) { dest[start++] = ' '; }
        }
        start = IntTypePrint(array->data[index], dest, start, end);
    }
    if (start < last) { dest[start++] = ']'; }
    dest[start] = '\0';
    return start;
}

/**
 * Construct a CIF array from a Simulink vector.
 * @param vec Simulink vector to copy.
 * @return The constructed array.
 */
static A1IType A1ITypeFromSimulink(real_T *vec) {
    A1IType result;
    int i;
    for (i = 0; i < 1; i++) result.data[i] = SimulinkToInt(vec[i]);
    return result;
}

/**
 * Fill a Simulink vector from a CIF array.
 * @param vec Simulink vector to copy to.
 * @param arr Source array to get values from.
 */
static void A1ITypeToSimulink(real_T *vec, A1IType *arr) {
    int i;
    for (i = 0; i < 1; i++) vec[i] = IntToSimulink(arr->data[i]);
}

/**
 * Compare two arrays for equality.
 * @param left First array to compare.
 * @param right Second array to compare.
 * @return Whether both arrays are the same.
 */
static BoolType A1RTypeEquals(A1RType *left, A1RType *right) {
    if (left == right) return TRUE;
    return memcmp(left, right, sizeof(A1RType)) == 0;
}

/**
 * Extract an element from the array.
 * @param array Array with value to retrieve.
 * @param index Element index in the array (not normalized).
 * @return Element value at the indicated index from the array.
 */
static real_T A1RTypeProject(A1RType *array, IntType index) {
    if (index < 0) index += 1; /* Normalize index. */
    assert(index >= 0 && index < 1);

    return array->data[index];
}

/**
 * In-place change of the array.
 * @param array Array to modify.
 * @param index Element index in the array (not normalized).
 * @param value New value to copy into the array.
 */
static void A1RTypeModify(A1RType *array, IntType index, real_T value) {
    if (index < 0) index += 1; /* Normalize index. */
    assert(index >= 0 && index < 1);

    array->data[index] = value;
}

/**
 * Append textual representation of the array value into the provided
 * destination, space permitting.
 * @param array Array to print.
 * @param dest Destination to write text to.
 * @param start First available offset in \a dest for new text.
 * @param end Fist offset behind \a dest.
 * @return First free offset in \a dest, mat be \a end.
 */
static int A1RTypePrint(A1RType *array, char *dest, int start, int end) {
    int last = end - 1;
    if (start < last) { dest[start++] = '['; }
    int index;
    for (index = 0; index < 1; index++) {
        if (index > 0) {
            if (start < last) { dest[start++] = ','; }
            if (start < last) { dest[start++] = ' '; }
        }
        start = RealTypePrint(array->data[index], dest, start, end);
    }
    if (start < last) { dest[start++] = ']'; }
    dest[start] = '\0';
    return start;
}

/**
 * Construct a CIF array from a Simulink vector.
 * @param vec Simulink vector to copy.
 * @return The constructed array.
 */
static A1RType A1RTypeFromSimulink(real_T *vec) {
    A1RType result;
    int i;
    for (i = 0; i < 1; i++) result.data[i] = SimulinkToReal(vec[i]);
    return result;
}

/**
 * Fill a Simulink vector from a CIF array.
 * @param vec Simulink vector to copy to.
 * @param arr Source array to get values from.
 */
static void A1RTypeToSimulink(real_T *vec, A1RType *arr) {
    int i;
    for (i = 0; i < 1; i++) vec[i] = RealToSimulink(arr->data[i]);
}

/**
 * Compare two tuples for equality.
 * @param left First tuple to compare.
 * @param right Second tuple to compare.
 * @return Whether both tuples are the same.
 */
static BoolType T2A1IA1RTypeEquals(T2A1IA1RType *left, T2A1IA1RType *right) {
    if (left == right) return TRUE;
    if (memcmp(&left->_field0, &right->_field0, sizeof(A1IType)) != 0) return FALSE;
    if (memcmp(&left->_field1, &right->_field1, sizeof(A1RType)) != 0) return FALSE;
    return TRUE;
}

/**
 * Append textual representation of the tuple value into the provided
 * destination, space permitting.
 * @param tuple Tuple to print.
 * @param dest Destination to write text to.
 * @param start First available offset in \a dest for new text.
 * @param end Fist offset behind \a dest.
 * @return First free offset in \a dest, mat be \a end.
 */
static int T2A1IA1RTypePrint(T2A1IA1RType *tuple, char *dest, int start, int end) {
    int last = end - 1;
    if (start < last) { dest[start++] = '('; }
    start = A1ITypePrint(&tuple->_field0, dest, start, end);
    if (start < last) { dest[start++] = ','; }
    if (start < last) { dest[start++] = ' '; }
    start = A1RTypePrint(&tuple->_field1, dest, start, end);
    if (start < last) { dest[start++] = ')'; }
    dest[start] = '\0';
    return start;
}

/**
 * Compare two arrays for equality.
 * @param left First array to compare.
 * @param right Second array to compare.
 * @return Whether both arrays are the same.
 */
static BoolType A2T2A1IA1RTypeEquals(A2T2A1IA1RType *left, A2T2A1IA1RType *right) {
    if (left == right) return TRUE;
    int i;
    for (i = 0; i < 2; i++) {
        if (!(T2A1IA1RTypeEquals(&(left->data[i]), &(right->data[i])))) return FALSE;
    }
    return TRUE;
}

/**
 * Extract an element from the array.
 * @param array Array with value to retrieve.
 * @param index Element index in the array (not normalized).
 * @return Element value at the indicated index from the array.
 */
static T2A1IA1RType *A2T2A1IA1RTypeProject(A2T2A1IA1RType *array, IntType index) {
    if (index < 0) index += 2; /* Normalize index. */
    assert(index >= 0 && index < 2);

    return &array->data[index];
}

/**
 * In-place change of the array.
 * @param array Array to modify.
 * @param index Element index in the array (not normalized).
 * @param value New value to copy into the array.
 */
static void A2T2A1IA1RTypeModify(A2T2A1IA1RType *array, IntType index, T2A1IA1RType *value) {
    if (index < 0) index += 2; /* Normalize index. */
    assert(index >= 0 && index < 2);

    memcpy(&array->data[index], value, sizeof(T2A1IA1RType));
}

/**
 * Append textual representation of the array value into the provided
 * destination, space permitting.
 * @param array Array to print.
 * @param dest Destination to write text to.
 * @param start First available offset in \a dest for new text.
 * @param end Fist offset behind \a dest.
 * @return First free offset in \a dest, mat be \a end.
 */
static int A2T2A1IA1RTypePrint(A2T2A1IA1RType *array, char *dest, int start, int end) {
    int last = end - 1;
    if (start < last) { dest[start++] = '['; }
    int index;
    for (index = 0; index < 2; index++) {
        if (index > 0) {
            if (start < last) { dest[start++] = ','; }
            if (start < last) { dest[start++] = ' '; }
        }
        start = T2A1IA1RTypePrint(&array->data[index], dest, start, end);
    }
    if (start < last) { dest[start++] = ']'; }
    dest[start] = '\0';
    return start;
}

/**
 * Compare two tuples for equality.
 * @param left First tuple to compare.
 * @param right Second tuple to compare.
 * @return Whether both tuples are the same.
 */
static BoolType T2SA2T2A1IA1RTypeEquals(T2SA2T2A1IA1RType *left, T2SA2T2A1IA1RType *right) {
    if (left == right) return TRUE;
    if (!(StringTypeEquals(&(left->_field0), &(right->_field0)))) return FALSE;
    if (!(A2T2A1IA1RTypeEquals(&(left->_field1), &(right->_field1)))) return FALSE;
    return TRUE;
}

/**
 * Append textual representation of the tuple value into the provided
 * destination, space permitting.
 * @param tuple Tuple to print.
 * @param dest Destination to write text to.
 * @param start First available offset in \a dest for new text.
 * @param end Fist offset behind \a dest.
 * @return First free offset in \a dest, mat be \a end.
 */
static int T2SA2T2A1IA1RTypePrint(T2SA2T2A1IA1RType *tuple, char *dest, int start, int end) {
    int last = end - 1;
    if (start < last) { dest[start++] = '('; }
    start = StringTypePrintEscaped(&tuple->_field0, dest, start, end);
    if (start < last) { dest[start++] = ','; }
    if (start < last) { dest[start++] = ' '; }
    start = A2T2A1IA1RTypePrint(&tuple->_field1, dest, start, end);
    if (start < last) { dest[start++] = ')'; }
    dest[start] = '\0';
    return start;
}

/**
 * Compare two arrays for equality.
 * @param left First array to compare.
 * @param right Second array to compare.
 * @return Whether both arrays are the same.
 */
static BoolType A3T2IITypeEquals(A3T2IIType *left, A3T2IIType *right) {
    if (left == right) return TRUE;
    int i;
    for (i = 0; i < 3; i++) {
        if (!(T2IITypeEquals(&(left->data[i]), &(right->data[i])))) return FALSE;
    }
    return TRUE;
}

/**
 * Extract an element from the array.
 * @param array Array with value to retrieve.
 * @param index Element index in the array (not normalized).
 * @return Element value at the indicated index from the array.
 */
static T2IIType *A3T2IITypeProject(A3T2IIType *array, IntType index) {
    if (index < 0) index += 3; /* Normalize index. */
    assert(index >= 0 && index < 3);

    return &array->data[index];
}

/**
 * In-place change of the array.
 * @param array Array to modify.
 * @param index Element index in the array (not normalized).
 * @param value New value to copy into the array.
 */
static void A3T2IITypeModify(A3T2IIType *array, IntType index, T2IIType *value) {
    if (index < 0) index += 3; /* Normalize index. */
    assert(index >= 0 && index < 3);

    memcpy(&array->data[index], value, sizeof(T2IIType));
}

/**
 * Append textual representation of the array value into the provided
 * destination, space permitting.
 * @param array Array to print.
 * @param dest Destination to write text to.
 * @param start First available offset in \a dest for new text.
 * @param end Fist offset behind \a dest.
 * @return First free offset in \a dest, mat be \a end.
 */
static int A3T2IITypePrint(A3T2IIType *array, char *dest, int start, int end) {
    int last = end - 1;
    if (start < last) { dest[start++] = '['; }
    int index;
    for (index = 0; index < 3; index++) {
        if (index > 0) {
            if (start < last) { dest[start++] = ','; }
            if (start < last) { dest[start++] = ' '; }
        }
        start = T2IITypePrint(&array->data[index], dest, start, end);
    }
    if (start < last) { dest[start++] = ']'; }
    dest[start] = '\0';
    return start;
}

static int EnumTypePrint(edgesEnum value, char *dest, int start, int end) {
    int last = end - 1;
    const char *lit_name = enum_names[value];
    while (start < last && *lit_name) {
        dest[start++] = *lit_name;
        lit_name++;
    }
    dest[start] = '\0';
    return start;
}

/* }}} */
/* {{{ work data structure. */
struct WorkStruct {


    int_T aut02_x_;              /**< Discrete variable "int[0..3] aut02.x". */
    int_T aut03_d_;              /**< Discrete variable "int aut03.d". */
    int_T aut04_a_;              /**< Discrete variable "int aut04.a". */
    int_T aut04_b_;              /**< Discrete variable "int aut04.b". */
    int_T aut04_c_;              /**< Discrete variable "int aut04.c". */
    int_T aut04_d_;              /**< Discrete variable "int aut04.d". */
    A5IType aut05_v1_;           /**< Discrete variable "list[5] int aut05.v1". */
    A5IType aut05_v2_;           /**< Discrete variable "list[5] int aut05.v2". */
    T2IIType aut06_v1_;          /**< Discrete variable "tuple(int a; int b) aut06.v1". */
    T2IIType aut06_v2_;          /**< Discrete variable "tuple(int a; int b) aut06.v2". */
    int_T aut06_x_;              /**< Discrete variable "int aut06.x". */
    int_T aut06_y_;              /**< Discrete variable "int aut06.y". */
    T2T2IISType aut08_tt1_;      /**< Discrete variable "tuple(tuple(int a; int b) t; string c) aut08.tt1". */
    T2T2IISType aut08_tt2_;      /**< Discrete variable "tuple(tuple(int a; int b) t; string c) aut08.tt2". */
    T2IIType aut08_t_;           /**< Discrete variable "tuple(int a; int b) aut08.t". */
    int_T aut08_i_;              /**< Discrete variable "int aut08.i". */
    int_T aut08_j_;              /**< Discrete variable "int aut08.j". */
    StringType aut08_s_;         /**< Discrete variable "string aut08.s". */
    A2A3IType aut09_ll1_;        /**< Discrete variable "list[2] list[3] int aut09.ll1". */
    A2A3IType aut09_ll2_;        /**< Discrete variable "list[2] list[3] int aut09.ll2". */
    A3IType aut09_l_;            /**< Discrete variable "list[3] int aut09.l". */
    int_T aut09_i_;              /**< Discrete variable "int aut09.i". */
    int_T aut09_j_;              /**< Discrete variable "int aut09.j". */
    T2SA2T2A1IA1RType aut10_x1_; /**< Discrete variable "tuple(string s; list[2] tuple(list[1] int x; list[1] real y) z) aut10.x1". */
    T2SA2T2A1IA1RType aut10_x2_; /**< Discrete variable "tuple(string s; list[2] tuple(list[1] int x; list[1] real y) z) aut10.x2". */
    A2T2A1IA1RType aut10_l_;     /**< Discrete variable "list[2] tuple(list[1] int x; list[1] real y) aut10.l". */
    A1IType aut10_li_;           /**< Discrete variable "list[1] int aut10.li". */
    A1RType aut10_lr_;           /**< Discrete variable "list[1] real aut10.lr". */
    int_T aut10_i_;              /**< Discrete variable "int aut10.i". */
    real_T aut10_r_;             /**< Discrete variable "real aut10.r". */
    A3T2IIType aut11_v1_;        /**< Discrete variable "list[3] tuple(int a; int b) aut11.v1". */
    real_T aut12_x_;             /**< Discrete variable "real aut12.x". */
    real_T aut12_y_;             /**< Discrete variable "real aut12.y". */
    real_T aut12_z_;             /**< Discrete variable "real aut12.z". */
    real_T aut12_td_;            /**< Discrete variable "real aut12.td". */
    real_T aut13_x_;             /**< Discrete variable "real aut13.x". */
    real_T aut13_y_;             /**< Discrete variable "real aut13.y". */
    real_T aut13_z_;             /**< Discrete variable "real aut13.z". */

    BoolType aut14_b_; /**< Input variable "bool aut14.b". */
    int_T aut14_i_;    /**< Input variable "int aut14.i". */
    real_T aut14_r_;   /**< Input variable "real aut14.r". */
    unsigned char input_loaded00;
    unsigned char input_loaded01;
    unsigned char input_loaded02;
};
/* }}} */

/* {{{ algvar, derivative, function declarations. */
static real_T aut12_v_(SimStruct *sim_struct);
static real_T aut12_w_(SimStruct *sim_struct);
static real_T aut13_v_(SimStruct *sim_struct);
static real_T aut13_w_(SimStruct *sim_struct);


static real_T deriv01(SimStruct *sim_struct);
static real_T deriv02(SimStruct *sim_struct);
static real_T deriv03(SimStruct *sim_struct);
static real_T deriv04(SimStruct *sim_struct);
static real_T deriv05(SimStruct *sim_struct);


/* }}} */

/* {{{ Algebraic variables, derivatives, and function definitions. */
/* {{{ Algebraic variable definitions. */
/** Algebraic variable aut12_v = M.aut12_x + M.aut12_y; */
static real_T aut12_v_(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    return RealAdd(work->aut12_x_, work->aut12_y_);
}

/** Algebraic variable aut12_w = M.aut12_v + M.aut12_z; */
static real_T aut12_w_(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    return RealAdd(aut12_v_(sim_struct), work->aut12_z_);
}

/** Algebraic variable aut13_v = M.aut13_x + M.aut13_y; */
static real_T aut13_v_(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    return RealAdd(work->aut13_x_, work->aut13_y_);
}

/** Algebraic variable aut13_w = M.aut13_v + M.aut13_z; */
static real_T aut13_w_(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    return RealAdd(aut13_v_(sim_struct), work->aut13_z_);
}
/* }}} */

/* {{{ Derivative definitions. */
/** Derivative of "aut03.c". */
static real_T deriv01(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    return 1.0;
}

/** Derivative of "aut07.x". */
static real_T deriv02(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    return 1.0;
}

/** Derivative of "aut07.y". */
static real_T deriv03(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    return 2.0;
}

/** Derivative of "aut12.t". */
static real_T deriv04(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    return RealAdd(work->aut12_x_, work->aut12_y_);
}

/** Derivative of "aut12.u". */
static real_T deriv05(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    return RealAdd(deriv04(sim_struct), work->aut12_z_);
}
/* }}} */

/* {{{ Function definitions. */

/* }}} */
/* }}} */

enum edgesEventEnum_ {
    EVT_INITIAL_, /**< Initial step. */
    EVT_DELAY_,   /**< Delay step. */
    EVT_TAU_,     /**< Tau step. */
    e02a_,        /**< Event e02a. */
    e02b_,        /**< Event e02b. */
    e03a_,        /**< Event e03a. */
    e03b_,        /**< Event e03b. */
    e04a_,        /**< Event e04a. */
    e04b_,        /**< Event e04b. */
    e04c_,        /**< Event e04c. */
    e04d_,        /**< Event e04d. */
    e04e_,        /**< Event e04e. */
    e04f_,        /**< Event e04f. */
    e05a_,        /**< Event e05a. */
    e05b_,        /**< Event e05b. */
    e05c_,        /**< Event e05c. */
    e05d_,        /**< Event e05d. */
    e05e_,        /**< Event e05e. */
    e06a_,        /**< Event e06a. */
    e06b_,        /**< Event e06b. */
    e06c_,        /**< Event e06c. */
    e06d_,        /**< Event e06d. */
    e06e_,        /**< Event e06e. */
    e07a_,        /**< Event e07a. */
    e07b_,        /**< Event e07b. */
    e08a_,        /**< Event e08a. */
    e08b_,        /**< Event e08b. */
    e08c_,        /**< Event e08c. */
    e08d_,        /**< Event e08d. */
    e08e_,        /**< Event e08e. */
    e08f_,        /**< Event e08f. */
    e08g_,        /**< Event e08g. */
    e08h_,        /**< Event e08h. */
    e09a_,        /**< Event e09a. */
    e09b_,        /**< Event e09b. */
    e09c_,        /**< Event e09c. */
    e09d_,        /**< Event e09d. */
    e09e_,        /**< Event e09e. */
    e09f_,        /**< Event e09f. */
    e09g_,        /**< Event e09g. */
    e10a_,        /**< Event e10a. */
    e10b_,        /**< Event e10b. */
    e10c_,        /**< Event e10c. */
    e10d_,        /**< Event e10d. */
    e10e_,        /**< Event e10e. */
    e10f_,        /**< Event e10f. */
    e10g_,        /**< Event e10g. */
    e10h_,        /**< Event e10h. */
    e10i_,        /**< Event e10i. */
    e11a_,        /**< Event e11a. */
    e12a_,        /**< Event e12a. */
    e12b_,        /**< Event e12b. */
    e12c_,        /**< Event e12c. */
    e12d_,        /**< Event e12d. */
    e12e_,        /**< Event e12e. */
    e13a_,        /**< Event e13a. */
    e13b_,        /**< Event e13b. */
    e13c_,        /**< Event e13c. */
    e13d_,        /**< Event e13d. */
    e13e_,        /**< Event e13e. */
    e14a_,        /**< Event e14a. */
    e14b_,        /**< Event e14b. */
    e14c_,        /**< Event e14c. */
    e14d_,        /**< Event e14d. */
    e14e_,        /**< Event e14e. */
    e14f_,        /**< Event e14f. */
    e14g_,        /**< Event e14g. */
    e14h_,        /**< Event e14h. */
};
typedef enum edgesEventEnum_ edges_Event_;

const char *evt_names[] = { /** < Event names. */
    "initial-step", /**< Initial step. */
    "delay-step",   /**< Delay step. */
    "tau",          /**< Tau step. */
    "e02a",         /**< Event e02a. */
    "e02b",         /**< Event e02b. */
    "e03a",         /**< Event e03a. */
    "e03b",         /**< Event e03b. */
    "e04a",         /**< Event e04a. */
    "e04b",         /**< Event e04b. */
    "e04c",         /**< Event e04c. */
    "e04d",         /**< Event e04d. */
    "e04e",         /**< Event e04e. */
    "e04f",         /**< Event e04f. */
    "e05a",         /**< Event e05a. */
    "e05b",         /**< Event e05b. */
    "e05c",         /**< Event e05c. */
    "e05d",         /**< Event e05d. */
    "e05e",         /**< Event e05e. */
    "e06a",         /**< Event e06a. */
    "e06b",         /**< Event e06b. */
    "e06c",         /**< Event e06c. */
    "e06d",         /**< Event e06d. */
    "e06e",         /**< Event e06e. */
    "e07a",         /**< Event e07a. */
    "e07b",         /**< Event e07b. */
    "e08a",         /**< Event e08a. */
    "e08b",         /**< Event e08b. */
    "e08c",         /**< Event e08c. */
    "e08d",         /**< Event e08d. */
    "e08e",         /**< Event e08e. */
    "e08f",         /**< Event e08f. */
    "e08g",         /**< Event e08g. */
    "e08h",         /**< Event e08h. */
    "e09a",         /**< Event e09a. */
    "e09b",         /**< Event e09b. */
    "e09c",         /**< Event e09c. */
    "e09d",         /**< Event e09d. */
    "e09e",         /**< Event e09e. */
    "e09f",         /**< Event e09f. */
    "e09g",         /**< Event e09g. */
    "e10a",         /**< Event e10a. */
    "e10b",         /**< Event e10b. */
    "e10c",         /**< Event e10c. */
    "e10d",         /**< Event e10d. */
    "e10e",         /**< Event e10e. */
    "e10f",         /**< Event e10f. */
    "e10g",         /**< Event e10g. */
    "e10h",         /**< Event e10h. */
    "e10i",         /**< Event e10i. */
    "e11a",         /**< Event e11a. */
    "e12a",         /**< Event e12a. */
    "e12b",         /**< Event e12b. */
    "e12c",         /**< Event e12c. */
    "e12d",         /**< Event e12d. */
    "e12e",         /**< Event e12e. */
    "e13a",         /**< Event e13a. */
    "e13b",         /**< Event e13b. */
    "e13c",         /**< Event e13c. */
    "e13d",         /**< Event e13d. */
    "e13e",         /**< Event e13e. */
    "e14a",         /**< Event e14a. */
    "e14b",         /**< Event e14b. */
    "e14c",         /**< Event e14c. */
    "e14d",         /**< Event e14d. */
    "e14e",         /**< Event e14e. */
    "e14f",         /**< Event e14f. */
    "e14g",         /**< Event e14g. */
    "e14h",         /**< Event e14h. */
};

/** Enum names. */
static const char *enum_names[] = {
    "loc1",
    "loc2",
    "loc3",
    "X",
};

/**
 * Reset 'loaded' status of all input variables.
 * @param work Work data.
 */
static void ClearInputFlags(struct WorkStruct *work) {
    work->input_loaded00 = FALSE;
    work->input_loaded01 = FALSE;
    work->input_loaded02 = FALSE;
}

/* Time-dependent guards. */
static BoolType GuardEval57(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    if (!work->input_loaded00) {
        InputRealPtrsType uPtrs = ssGetInputPortRealSignalPtrs(sim_struct, 0);
        work->aut14_b_ = SimulinkToBool(*uPtrs[0]);
        work->input_loaded00 = TRUE;
    }
    return ((modes[13]) == (_edges_X)) && (work->aut14_b_);
}

static BoolType GuardEval58(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    if (!work->input_loaded01) {
        InputRealPtrsType uPtrs = ssGetInputPortRealSignalPtrs(sim_struct, 1);
        work->aut14_i_ = SimulinkToInt(*uPtrs[0]);
        work->input_loaded01 = TRUE;
    }
    return ((modes[13]) == (_edges_X)) && ((work->aut14_i_) > (3));
}

static BoolType GuardEval59(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    if (!work->input_loaded02) {
        InputRealPtrsType uPtrs = ssGetInputPortRealSignalPtrs(sim_struct, 2);
        work->aut14_r_ = SimulinkToReal(*uPtrs[0]);
        work->input_loaded02 = TRUE;
    }
    if (!work->input_loaded01) {
        InputRealPtrsType uPtrs = ssGetInputPortRealSignalPtrs(sim_struct, 1);
        work->aut14_i_ = SimulinkToInt(*uPtrs[0]);
        work->input_loaded01 = TRUE;
    }
    return ((modes[13]) == (_edges_X)) && ((RealAdd(work->aut14_r_, work->aut14_i_)) != (18.0));
}

static BoolType GuardEval60(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    if (!work->input_loaded00) {
        InputRealPtrsType uPtrs = ssGetInputPortRealSignalPtrs(sim_struct, 0);
        work->aut14_b_ = SimulinkToBool(*uPtrs[0]);
        work->input_loaded00 = TRUE;
    }
    return ((modes[13]) == (_edges_X)) && (!(work->aut14_b_));
}

static BoolType GuardEval61(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    if (!work->input_loaded01) {
        InputRealPtrsType uPtrs = ssGetInputPortRealSignalPtrs(sim_struct, 1);
        work->aut14_i_ = SimulinkToInt(*uPtrs[0]);
        work->input_loaded01 = TRUE;
    }
    return ((modes[13]) == (_edges_X)) && ((IntegerNegate(work->aut14_i_)) < (5));
}

static BoolType GuardEval62(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    if (!work->input_loaded02) {
        InputRealPtrsType uPtrs = ssGetInputPortRealSignalPtrs(sim_struct, 2);
        work->aut14_r_ = SimulinkToReal(*uPtrs[0]);
        work->input_loaded02 = TRUE;
    }
    return ((modes[13]) == (_edges_X)) && ((RealNegate(work->aut14_r_)) < (6));
}

static BoolType GuardEval63(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    if (!work->input_loaded01) {
        InputRealPtrsType uPtrs = ssGetInputPortRealSignalPtrs(sim_struct, 1);
        work->aut14_i_ = SimulinkToInt(*uPtrs[0]);
        work->input_loaded01 = TRUE;
    }
    return ((modes[13]) == (_edges_X)) && ((work->aut14_i_) < (7));
}

static BoolType GuardEval64(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    if (!work->input_loaded02) {
        InputRealPtrsType uPtrs = ssGetInputPortRealSignalPtrs(sim_struct, 2);
        work->aut14_r_ = SimulinkToReal(*uPtrs[0]);
        work->input_loaded02 = TRUE;
    }
    return ((modes[13]) == (_edges_X)) && ((work->aut14_r_) < (8));
}

/* Event execution. */

/**
 * Execute code for event "e02a".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent0(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = ((modes[1]) == (_edges_loc1)) || (((modes[1]) == (_edges_loc2)) || ((modes[1]) == (_edges_loc3)));
    if (!guard) return FALSE;


    if ((modes[1]) == (_edges_loc1)) {
        modes[1] = _edges_loc2;
    } else if ((modes[1]) == (_edges_loc2)) {
        modes[1] = _edges_loc3;
    } else if ((modes[1]) == (_edges_loc3)) {
        modes[1] = _edges_loc1;
    }

    return TRUE;
}

/**
 * Execute code for event "e02b".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent1(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (((modes[1]) == (_edges_loc1)) && ((work->aut02_x_) == (2))) || (((modes[1]) == (_edges_loc2)) || (((modes[1]) == (_edges_loc3)) && ((work->aut02_x_) == (3))));
    if (!guard) return FALSE;


    if (((modes[1]) == (_edges_loc1)) && ((work->aut02_x_) == (2))) {
        modes[1] = _edges_loc1;
    } else if ((modes[1]) == (_edges_loc2)) {
        work->aut02_x_ = 1;
    } else if (((modes[1]) == (_edges_loc3)) && ((work->aut02_x_) == (3))) {
        work->aut02_x_ = 1;
    }

    return TRUE;
}

/**
 * Execute code for event "e03a".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent2(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[2]) == (_edges_X);
    if (!guard) return FALSE;


    cstate[1] = 1.23;

    return TRUE;
}

/**
 * Execute code for event "e03b".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent3(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[2]) == (_edges_X);
    if (!guard) return FALSE;


    work->aut03_d_ = 2;

    return TRUE;
}

/**
 * Execute code for event "e04a".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent4(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[3]) == (_edges_X);
    if (!guard) return FALSE;


    if ((work->aut04_a_) == (1)) {
        work->aut04_b_ = 2;
    }

    return TRUE;
}

/**
 * Execute code for event "e04b".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent5(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[3]) == (_edges_X);
    if (!guard) return FALSE;


    if ((work->aut04_a_) == (1)) {
        work->aut04_b_ = 2;
    } else if ((work->aut04_a_) == (2)) {
        work->aut04_b_ = 3;
    }

    return TRUE;
}

/**
 * Execute code for event "e04c".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent6(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[3]) == (_edges_X);
    if (!guard) return FALSE;


    if ((work->aut04_a_) == (1)) {
        work->aut04_b_ = 2;
    } else if ((work->aut04_a_) == (2)) {
        work->aut04_b_ = 3;
    } else if ((work->aut04_a_) == (3)) {
        work->aut04_b_ = 4;
    }

    return TRUE;
}

/**
 * Execute code for event "e04d".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent7(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[3]) == (_edges_X);
    if (!guard) return FALSE;


    if ((work->aut04_a_) == (1)) {
        work->aut04_b_ = 2;
    } else if ((work->aut04_a_) == (2)) {
        work->aut04_b_ = 3;
    } else if ((work->aut04_a_) == (3)) {
        work->aut04_b_ = 4;
    } else {
        work->aut04_b_ = 5;
    }

    return TRUE;
}

/**
 * Execute code for event "e04e".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent8(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[3]) == (_edges_X);
    if (!guard) return FALSE;


    if ((work->aut04_a_) == (1)) {
        work->aut04_b_ = 2;
    } else {
        work->aut04_b_ = 5;
    }

    return TRUE;
}

/**
 * Execute code for event "e04f".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent9(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[3]) == (_edges_X);
    if (!guard) return FALSE;


    if ((work->aut04_a_) == (1)) {
        work->aut04_b_ = 2;
    } else {
        work->aut04_b_ = 5;
    }
    if ((work->aut04_a_) == (1)) {
        work->aut04_c_ = 2;
    } else {
        work->aut04_d_ = 5;
    }

    return TRUE;
}

/**
 * Execute code for event "e05a".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent10(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[4]) == (_edges_X);
    if (!guard) return FALSE;


    {
        int_T rhs2 = 3;
        int_T index3 = 0;
        A5ITypeModify(&work->aut05_v1_, index3, rhs2);
    }
    {
        int_T rhs2 = 4;
        int_T index3 = 1;
        A5ITypeModify(&work->aut05_v1_, index3, rhs2);
    }

    return TRUE;
}

/**
 * Execute code for event "e05b".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent11(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[4]) == (_edges_X);
    if (!guard) return FALSE;


    {
        int_T rhs2 = 3;
        int_T index3 = 0;
        A5ITypeModify(&work->aut05_v1_, index3, rhs2);
    }
    {
        int_T rhs2 = 4;
        int_T index3 = 1;
        A5ITypeModify(&work->aut05_v1_, index3, rhs2);
    }

    return TRUE;
}

/**
 * Execute code for event "e05c".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent12(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[4]) == (_edges_X);
    if (!guard) return FALSE;


    work->aut05_v1_ = work->aut05_v2_;

    return TRUE;
}

/**
 * Execute code for event "e05d".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent13(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[4]) == (_edges_X);
    if (!guard) return FALSE;


    {
        int_T rhs2 = A5ITypeProject(&(work->aut05_v2_), 0);
        int_T index3 = 0;
        A5ITypeModify(&work->aut05_v1_, index3, rhs2);
    }
    {
        int_T rhs2 = A5ITypeProject(&(work->aut05_v2_), 1);
        int_T index3 = 1;
        A5ITypeModify(&work->aut05_v1_, index3, rhs2);
    }

    return TRUE;
}

/**
 * Execute code for event "e05e".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent14(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[4]) == (_edges_X);
    if (!guard) return FALSE;


    {
        int_T rhs2 = A5ITypeProject(&(work->aut05_v2_), 1);
        int_T index3 = 0;
        A5ITypeModify(&work->aut05_v1_, index3, rhs2);
    }
    {
        int_T rhs2 = A5ITypeProject(&(work->aut05_v2_), 0);
        int_T index3 = 1;
        A5ITypeModify(&work->aut05_v1_, index3, rhs2);
    }

    return TRUE;
}

/**
 * Execute code for event "e06a".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent15(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[5]) == (_edges_X);
    if (!guard) return FALSE;


    (work->aut06_v1_)._field0 = 3;
    (work->aut06_v1_)._field1 = 4;

    return TRUE;
}

/**
 * Execute code for event "e06b".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent16(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[5]) == (_edges_X);
    if (!guard) return FALSE;


    {
        int_T rhs2 = 5;
        work->aut06_v1_._field0 = rhs2;
    }

    return TRUE;
}

/**
 * Execute code for event "e06c".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent17(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[5]) == (_edges_X);
    if (!guard) return FALSE;


    {
        T2IIType rhs2 = work->aut06_v1_;
        work->aut06_x_ = (rhs2)._field0;
        work->aut06_y_ = (rhs2)._field1;
    }

    return TRUE;
}

/**
 * Execute code for event "e06d".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent18(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[5]) == (_edges_X);
    if (!guard) return FALSE;


    (work->aut06_v1_)._field0 = IntegerAdd(work->aut06_x_, 1);
    (work->aut06_v1_)._field1 = IntegerMultiply(work->aut06_y_, 2);

    return TRUE;
}

/**
 * Execute code for event "e06e".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent19(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[5]) == (_edges_X);
    if (!guard) return FALSE;


    work->aut06_v1_ = work->aut06_v2_;

    return TRUE;
}

/**
 * Execute code for event "e07a".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent20(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[6]) == (_edges_X);
    if (!guard) return FALSE;


    cstate[2] = 5.0;

    return TRUE;
}

/**
 * Execute code for event "e07b".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent21(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[6]) == (_edges_X);
    if (!guard) return FALSE;


    cstate[3] = cstate[2];
    cstate[2] = 5.0;

    return TRUE;
}

/**
 * Execute code for event "e08a".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent22(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[7]) == (_edges_X);
    if (!guard) return FALSE;


    ((work->aut08_tt1_)._field0)._field0 = 1;
    ((work->aut08_tt1_)._field0)._field1 = 2;
    StringTypeCopyText(&((work->aut08_tt1_)._field1), "abc");

    return TRUE;
}

/**
 * Execute code for event "e08b".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent23(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[7]) == (_edges_X);
    if (!guard) return FALSE;


    work->aut08_tt1_ = work->aut08_tt2_;

    return TRUE;
}

/**
 * Execute code for event "e08c".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent24(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[7]) == (_edges_X);
    if (!guard) return FALSE;


    {
        T2IIType rhs2 = work->aut08_t_;
        work->aut08_tt1_._field0 = rhs2;
    }

    return TRUE;
}

/**
 * Execute code for event "e08d".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent25(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[7]) == (_edges_X);
    if (!guard) return FALSE;


    {
        int_T rhs2 = 3;
        T2IIType part3 = (work->aut08_tt1_)._field0;
        part3._field1 = rhs2;
        work->aut08_tt1_._field0 = part3;
    }

    return TRUE;
}

/**
 * Execute code for event "e08e".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent26(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[7]) == (_edges_X);
    if (!guard) return FALSE;


    {
        int_T rhs2 = 4;
        T2IIType part3 = (work->aut08_tt1_)._field0;
        part3._field0 = rhs2;
        work->aut08_tt1_._field0 = part3;
    }

    return TRUE;
}

/**
 * Execute code for event "e08f".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent27(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[7]) == (_edges_X);
    if (!guard) return FALSE;


    {
        StringType str_tmp3;
        StringTypeCopyText(&str_tmp3, "def");
        StringType rhs2 = str_tmp3;
        work->aut08_tt1_._field1 = rhs2;
    }

    return TRUE;
}

/**
 * Execute code for event "e08g".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent28(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[7]) == (_edges_X);
    if (!guard) return FALSE;


    {
        T2IIType rhs2 = (work->aut08_tt1_)._field0;
        work->aut08_i_ = (rhs2)._field0;
        work->aut08_j_ = (rhs2)._field1;
    }

    return TRUE;
}

/**
 * Execute code for event "e08h".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent29(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[7]) == (_edges_X);
    if (!guard) return FALSE;


    {
        T2T2IISType rhs2 = work->aut08_tt1_;
        work->aut08_i_ = (rhs2)._field0._field0;
        work->aut08_j_ = (rhs2)._field0._field1;
        work->aut08_s_ = (rhs2)._field1;
    }

    return TRUE;
}

/**
 * Execute code for event "e09a".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent30(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[8]) == (_edges_X);
    if (!guard) return FALSE;


    ((work->aut09_ll1_).data[0]).data[0] = 1;
    ((work->aut09_ll1_).data[0]).data[1] = 2;
    ((work->aut09_ll1_).data[0]).data[2] = 3;
    ((work->aut09_ll1_).data[1]).data[0] = 4;
    ((work->aut09_ll1_).data[1]).data[1] = 5;
    ((work->aut09_ll1_).data[1]).data[2] = 6;

    return TRUE;
}

/**
 * Execute code for event "e09b".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent31(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[8]) == (_edges_X);
    if (!guard) return FALSE;


    work->aut09_ll1_ = work->aut09_ll2_;

    return TRUE;
}

/**
 * Execute code for event "e09c".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent32(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[8]) == (_edges_X);
    if (!guard) return FALSE;


    {
        A3IType rhs2 = work->aut09_l_;
        int_T index3 = 0;
        A2A3ITypeModify(&work->aut09_ll1_, index3, &(rhs2));
    }

    return TRUE;
}

/**
 * Execute code for event "e09d".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent33(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[8]) == (_edges_X);
    if (!guard) return FALSE;


    {
        int_T rhs2 = 6;
        int_T index3 = 0;
        int_T index4 = 1;
        A3IType part5 = *(A2A3ITypeProject(&(work->aut09_ll1_), index3));
        A3ITypeModify(&part5, index4, rhs2);
        A2A3ITypeModify(&work->aut09_ll1_, index3, &(part5));
    }

    return TRUE;
}

/**
 * Execute code for event "e09e".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent34(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[8]) == (_edges_X);
    if (!guard) return FALSE;


    work->aut09_i_ = A3ITypeProject(&(work->aut09_l_), 0);

    return TRUE;
}

/**
 * Execute code for event "e09f".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent35(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[8]) == (_edges_X);
    if (!guard) return FALSE;


    work->aut09_i_ = A3ITypeProject(A2A3ITypeProject(&(work->aut09_ll1_), 0), 1);

    return TRUE;
}

/**
 * Execute code for event "e09g".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent36(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[8]) == (_edges_X);
    if (!guard) return FALSE;


    ((work->aut09_ll1_).data[0]).data[0] = work->aut09_i_;
    ((work->aut09_ll1_).data[0]).data[1] = work->aut09_j_;
    ((work->aut09_ll1_).data[0]).data[2] = IntegerAdd(work->aut09_i_, work->aut09_j_);
    ((work->aut09_ll1_).data[1]).data[0] = IntegerNegate(work->aut09_i_);
    ((work->aut09_ll1_).data[1]).data[1] = IntegerNegate(work->aut09_j_);
    ((work->aut09_ll1_).data[1]).data[2] = IntegerSubtract(IntegerNegate(work->aut09_i_), work->aut09_j_);

    return TRUE;
}

/**
 * Execute code for event "e10a".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent37(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[9]) == (_edges_X);
    if (!guard) return FALSE;


    work->aut10_x1_ = work->aut10_x2_;

    return TRUE;
}

/**
 * Execute code for event "e10b".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent38(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[9]) == (_edges_X);
    if (!guard) return FALSE;


    StringTypeCopyText(&((work->aut10_x1_)._field0), "abc");
    ((((work->aut10_x1_)._field1).data[0])._field0).data[0] = 1;
    ((((work->aut10_x1_)._field1).data[0])._field1).data[0] = 2.0;
    ((((work->aut10_x1_)._field1).data[1])._field0).data[0] = work->aut10_i_;
    ((((work->aut10_x1_)._field1).data[1])._field1).data[0] = work->aut10_r_;

    return TRUE;
}

/**
 * Execute code for event "e10c".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent39(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[9]) == (_edges_X);
    if (!guard) return FALSE;


    {
        StringType str_tmp3;
        StringTypeCopyText(&str_tmp3, "def");
        StringType rhs2 = str_tmp3;
        work->aut10_x1_._field0 = rhs2;
    }
    {
        A2T2A1IA1RType array_tmp3;
        (((array_tmp3).data[0])._field0).data[0] = 1;
        (((array_tmp3).data[0])._field1).data[0] = 2.0;
        (((array_tmp3).data[1])._field0).data[0] = 3;
        (((array_tmp3).data[1])._field1).data[0] = 4.0;
        A2T2A1IA1RType rhs2 = array_tmp3;
        work->aut10_x1_._field1 = rhs2;
    }

    return TRUE;
}

/**
 * Execute code for event "e10d".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent40(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[9]) == (_edges_X);
    if (!guard) return FALSE;


    {
        T2A1IA1RType tuple_tmp3;
        ((tuple_tmp3)._field0).data[0] = 4;
        ((tuple_tmp3)._field1).data[0] = 5.0;
        T2A1IA1RType rhs2 = tuple_tmp3;
        int_T index4 = 0;
        A2T2A1IA1RType part5 = (work->aut10_x1_)._field1;
        A2T2A1IA1RTypeModify(&part5, index4, &(rhs2));
        work->aut10_x1_._field1 = part5;
    }

    return TRUE;
}

/**
 * Execute code for event "e10e".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent41(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[9]) == (_edges_X);
    if (!guard) return FALSE;


    {
        int_T rhs2 = 5;
        int_T index3 = 0;
        int_T index4 = 0;
        A2T2A1IA1RType part5 = (work->aut10_x1_)._field1;
        T2A1IA1RType part6 = *(A2T2A1IA1RTypeProject(&(part5), index3));
        A1IType part7 = (part6)._field0;
        A1ITypeModify(&part7, index4, rhs2);
        part6._field0 = part7;
        A2T2A1IA1RTypeModify(&part5, index3, &(part6));
        work->aut10_x1_._field1 = part5;
    }

    return TRUE;
}

/**
 * Execute code for event "e10f".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent42(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[9]) == (_edges_X);
    if (!guard) return FALSE;


    work->aut10_l_ = (work->aut10_x1_)._field1;

    return TRUE;
}

/**
 * Execute code for event "e10g".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent43(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[9]) == (_edges_X);
    if (!guard) return FALSE;


    work->aut10_li_ = (A2T2A1IA1RTypeProject(&((work->aut10_x1_)._field1), 0))->_field0;

    return TRUE;
}

/**
 * Execute code for event "e10h".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent44(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[9]) == (_edges_X);
    if (!guard) return FALSE;


    work->aut10_lr_ = (A2T2A1IA1RTypeProject(&((work->aut10_x1_)._field1), 0))->_field1;

    return TRUE;
}

/**
 * Execute code for event "e10i".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent45(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[9]) == (_edges_X);
    if (!guard) return FALSE;


    work->aut10_i_ = A1ITypeProject(&((A2T2A1IA1RTypeProject(&((work->aut10_x1_)._field1), 0))->_field0), 0);
    work->aut10_r_ = A1RTypeProject(&((A2T2A1IA1RTypeProject(&((work->aut10_x1_)._field1), 0))->_field1), 0);

    return TRUE;
}

/**
 * Execute code for event "e11a".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent46(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[10]) == (_edges_X);
    if (!guard) return FALSE;


    if (((A3T2IITypeProject(&(work->aut11_v1_), 0))->_field0) == (1)) {
        int_T rhs2 = IntegerAdd((A3T2IITypeProject(&(work->aut11_v1_), 0))->_field0, 1);
        int_T index3 = 0;
        T2IIType part4 = *(A3T2IITypeProject(&(work->aut11_v1_), index3));
        part4._field0 = rhs2;
        A3T2IITypeModify(&work->aut11_v1_, index3, &(part4));
    } else if (((A3T2IITypeProject(&(work->aut11_v1_), 0))->_field0) == (2)) {
        int_T rhs2 = IntegerSubtract((A3T2IITypeProject(&(work->aut11_v1_), 0))->_field1, 1);
        int_T index3 = 0;
        T2IIType part4 = *(A3T2IITypeProject(&(work->aut11_v1_), index3));
        part4._field1 = rhs2;
        A3T2IITypeModify(&work->aut11_v1_, index3, &(part4));
    } else {
        int_T rhs2 = (A3T2IITypeProject(&(work->aut11_v1_), 2))->_field0;
        int_T index3 = 1;
        T2IIType part4 = *(A3T2IITypeProject(&(work->aut11_v1_), index3));
        part4._field0 = rhs2;
        A3T2IITypeModify(&work->aut11_v1_, index3, &(part4));
    }
    {
        int_T rhs2 = 3;
        int_T index3 = 2;
        T2IIType part4 = *(A3T2IITypeProject(&(work->aut11_v1_), index3));
        part4._field0 = rhs2;
        A3T2IITypeModify(&work->aut11_v1_, index3, &(part4));
    }

    return TRUE;
}

/**
 * Execute code for event "e12a".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent47(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[11]) == (_edges_X);
    if (!guard) return FALSE;


    work->aut12_z_ = aut12_v_(sim_struct);
    work->aut12_x_ = 1.0;
    work->aut12_y_ = 1.0;

    return TRUE;
}

/**
 * Execute code for event "e12b".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent48(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[11]) == (_edges_X);
    if (!guard) return FALSE;


    {
        real_T aut12_v_tmp2 = aut12_v_(sim_struct);
        work->aut12_x_ = aut12_v_tmp2;
        work->aut12_y_ = aut12_v_tmp2;
    }

    return TRUE;
}

/**
 * Execute code for event "e12c".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent49(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[11]) == (_edges_X);
    if (!guard) return FALSE;


    {
        real_T aut12_v_tmp2 = aut12_v_(sim_struct);
        work->aut12_td_ = aut12_w_(sim_struct);
        work->aut12_x_ = aut12_v_tmp2;
        work->aut12_y_ = aut12_v_tmp2;
    }

    return TRUE;
}

/**
 * Execute code for event "e12d".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent50(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[11]) == (_edges_X);
    if (!guard) return FALSE;


    {
        real_T aut12_t_tmp2 = deriv04(sim_struct);
        work->aut12_x_ = aut12_t_tmp2;
        work->aut12_y_ = aut12_t_tmp2;
    }

    return TRUE;
}

/**
 * Execute code for event "e12e".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent51(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[11]) == (_edges_X);
    if (!guard) return FALSE;


    work->aut12_td_ = deriv05(sim_struct);
    work->aut12_x_ = 1.0;
    work->aut12_y_ = 1.0;

    return TRUE;
}

/**
 * Execute code for event "e13a".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent52(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[12]) == (_edges_X);
    if (!guard) return FALSE;


    work->aut13_x_ = 1.0;

    return TRUE;
}

/**
 * Execute code for event "e13b".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent53(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[12]) == (_edges_X);
    if (!guard) return FALSE;


    if ((work->aut13_z_) == (5.0)) {
        work->aut13_x_ = 2.0;
    } else {
        work->aut13_x_ = 3.0;
    }

    return TRUE;
}

/**
 * Execute code for event "e13c".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent54(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[12]) == (_edges_X);
    if (!guard) return FALSE;


    if ((work->aut13_z_) == (5.0)) {
        work->aut13_x_ = 2.0;
    } else if ((work->aut13_z_) == (21.0)) {
        work->aut13_x_ = 3.0;
    }

    return TRUE;
}

/**
 * Execute code for event "e13d".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent55(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[12]) == (_edges_X);
    if (!guard) return FALSE;


    if ((work->aut13_z_) == (5.0)) {
        work->aut13_x_ = 2.0;
    } else if ((work->aut13_z_) == (21.0)) {
        work->aut13_x_ = 3.0;
    } else {
        work->aut13_x_ = 4.0;
    }

    return TRUE;
}

/**
 * Execute code for event "e13e".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent56(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[12]) == (_edges_X);
    if (!guard) return FALSE;


    if ((aut13_w_(sim_struct)) == (4.0)) {
        work->aut13_x_ = 1.0;
    } else if ((aut13_v_(sim_struct)) == (5.0)) {
        work->aut13_x_ = 2.0;
    }

    return TRUE;
}

/**
 * Execute code for event "e14a".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent57(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = GuardEval57(sim_struct);
    if (!guard) return FALSE;


    return TRUE;
}

/**
 * Execute code for event "e14b".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent58(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = GuardEval58(sim_struct);
    if (!guard) return FALSE;


    return TRUE;
}

/**
 * Execute code for event "e14c".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent59(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = GuardEval59(sim_struct);
    if (!guard) return FALSE;


    return TRUE;
}

/**
 * Execute code for event "e14d".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent60(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = GuardEval60(sim_struct);
    if (!guard) return FALSE;


    return TRUE;
}

/**
 * Execute code for event "e14e".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent61(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = GuardEval61(sim_struct);
    if (!guard) return FALSE;


    return TRUE;
}

/**
 * Execute code for event "e14f".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent62(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = GuardEval62(sim_struct);
    if (!guard) return FALSE;


    return TRUE;
}

/**
 * Execute code for event "e14g".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent63(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = GuardEval63(sim_struct);
    if (!guard) return FALSE;


    return TRUE;
}

/**
 * Execute code for event "e14h".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent64(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = GuardEval64(sim_struct);
    if (!guard) return FALSE;


    return TRUE;
}

/**
 * Execute code for event "tau".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent65(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[0]) == (_edges_X);
    if (!guard) return FALSE;


    return TRUE;
}

/**
 * Execute code for event "tau".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent66(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[0]) == (_edges_X);
    if (!guard) return FALSE;


    return TRUE;
}

/**
 * Execute code for event "tau".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent67(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[0]) == (_edges_X);
    if (!guard) return FALSE;


    return TRUE;
}

/**
 * Execute code for event "tau".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent68(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    BoolType guard = (modes[0]) == (_edges_X);
    if (!guard) return FALSE;


    return TRUE;
}

#if PRINT_OUTPUT
static void PrintOutput(edges_Event_ event, BoolType pre) {
}
#endif

/* {{{ mdlInitializeSizes, mdlInitializeSampleTimes */
static void mdlInitializeSizes(SimStruct *sim_struct) {
    /* Parameters. */
    ssSetNumSFcnParams(sim_struct, 0);
    if (ssGetNumSFcnParams(sim_struct) != ssGetSFcnParamsCount(sim_struct)) return;

    /* Inputs. */
    if (!ssSetNumInputPorts(sim_struct, 3)) return;

    ssSetInputPortWidth(sim_struct, 0, 1);
    ssSetInputPortWidth(sim_struct, 1, 1);
    ssSetInputPortWidth(sim_struct, 2, 1);

    int idx;
    for (idx = 0; idx < 3; idx++) {
        ssSetInputPortDataType(sim_struct, idx, SS_DOUBLE);
        ssSetInputPortComplexSignal(sim_struct, idx, COMPLEX_NO);
        ssSetInputPortDirectFeedThrough(sim_struct, idx, 1); /* Assume always feed-through. */
    }

    /* Outputs. */
    if (!ssSetNumOutputPorts(sim_struct, 51)) return;

    ssSetOutputPortWidth(sim_struct, 0, 1);
    ssSetOutputPortWidth(sim_struct, 1, 1);
    ssSetOutputPortWidth(sim_struct, 2, 1);
    ssSetOutputPortWidth(sim_struct, 3, 1);
    ssSetOutputPortWidth(sim_struct, 4, 1);
    ssSetOutputPortWidth(sim_struct, 5, 1);
    ssSetOutputPortWidth(sim_struct, 6, 1);
    ssSetOutputPortWidth(sim_struct, 7, 1);
    ssSetOutputPortWidth(sim_struct, 8, 1);
    ssSetOutputPortWidth(sim_struct, 9, 1);
    ssSetOutputPortWidth(sim_struct, 10, 1);
    ssSetOutputPortWidth(sim_struct, 11, 1);
    ssSetOutputPortWidth(sim_struct, 12, 1);
    ssSetOutputPortWidth(sim_struct, 13, 1);
    ssSetOutputPortWidth(sim_struct, 14, 1);
    ssSetOutputPortWidth(sim_struct, 15, 1);
    ssSetOutputPortWidth(sim_struct, 16, 1);
    ssSetOutputPortWidth(sim_struct, 17, 1);
    ssSetOutputPortWidth(sim_struct, 18, 1);
    ssSetOutputPortWidth(sim_struct, 19, 1);
    ssSetOutputPortWidth(sim_struct, 20, 1);
    ssSetOutputPortWidth(sim_struct, 21, 1);
    ssSetOutputPortWidth(sim_struct, 22, 1);
    ssSetOutputPortWidth(sim_struct, 23, 1);
    ssSetOutputPortWidth(sim_struct, 24, 1);
    ssSetOutputPortWidth(sim_struct, 25, 5);
    ssSetOutputPortWidth(sim_struct, 26, 5);
    ssSetOutputPortWidth(sim_struct, 27, 1);
    ssSetOutputPortWidth(sim_struct, 28, 1);
    ssSetOutputPortWidth(sim_struct, 29, 1);
    ssSetOutputPortWidth(sim_struct, 30, 1);
    ssSetOutputPortWidth(sim_struct, 31, 1);
    ssSetOutputPortWidth(sim_struct, 32, 1);
    ssSetOutputPortWidth(sim_struct, 33, 3);
    ssSetOutputPortMatrixDimensions(sim_struct, 34, 2, 3);
    ssSetOutputPortMatrixDimensions(sim_struct, 35, 2, 3);
    ssSetOutputPortWidth(sim_struct, 36, 1);
    ssSetOutputPortWidth(sim_struct, 37, 1);
    ssSetOutputPortWidth(sim_struct, 38, 1);
    ssSetOutputPortWidth(sim_struct, 39, 1);
    ssSetOutputPortWidth(sim_struct, 40, 1);
    ssSetOutputPortWidth(sim_struct, 41, 1);
    ssSetOutputPortWidth(sim_struct, 42, 1);
    ssSetOutputPortWidth(sim_struct, 43, 1);
    ssSetOutputPortWidth(sim_struct, 44, 1);
    ssSetOutputPortWidth(sim_struct, 45, 1);
    ssSetOutputPortWidth(sim_struct, 46, 1);
    ssSetOutputPortWidth(sim_struct, 47, 1);
    ssSetOutputPortWidth(sim_struct, 48, 1);
    ssSetOutputPortWidth(sim_struct, 49, 1);
    ssSetOutputPortWidth(sim_struct, 50, 1);

    for (idx = 0; idx < 51; idx++) {
        ssSetOutputPortDataType(sim_struct, idx, SS_DOUBLE);
        ssSetOutputPortComplexSignal(sim_struct, idx, COMPLEX_NO);
    }

    /* Disc state and cont state. */
    ssSetNumContStates(sim_struct, 6); /* CState[0] is time. */
    ssSetNumDiscStates(sim_struct, 0);

    /* Work vectors. */
    ssSetNumRWork(sim_struct, 0);
    ssSetNumIWork(sim_struct, 0);
    ssSetNumPWork(sim_struct, 1);

    /* Modes. */
    ssSetNumModes(sim_struct, 14);

    ssSetNumSampleTimes(sim_struct, 1);
    ssSetNumNonsampledZCs(sim_struct, 8);

    ssSetOptions(sim_struct, 0);
}

static void mdlInitializeSampleTimes(SimStruct *sim_struct) {
    ssSetSampleTime(sim_struct, 0, CONTINUOUS_SAMPLE_TIME);
    ssSetOffsetTime(sim_struct, 0, 0.0);
}
/* }}} */

/* {{{ mdlStart */
#define MDL_START
#if defined(MDL_START)
static void mdlStart(SimStruct *sim_struct) {
    /* Allocate PWork[0]. */
    struct WorkStruct *work = malloc(sizeof(struct WorkStruct));
    if (work == NULL) {
        ssSetErrorStatus(sim_struct, "Claiming of memory failed");
        return;
    }
    ssSetPWorkValue(sim_struct, 0, work);

    /* Initialize all constants. */

}
#endif
/* }}} */

/* {{{ mdlInitializeConditions */
#define MDL_INITIALIZE_CONDITIONS
#if defined(MDL_INITIALIZE_CONDITIONS)
static void mdlInitializeConditions(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);
    ClearInputFlags(work);

    /* Initialize discrete, continuous, and location pointer variables. */
    cstate[0] = 0.0; /* time = 0.0 */
    modes[0] = _edges_X;
    work->aut02_x_ = 0;
    modes[1] = _edges_loc1;
    cstate[1] = 0.0;
    work->aut03_d_ = 0;
    modes[2] = _edges_X;
    work->aut04_a_ = 0;
    work->aut04_b_ = 0;
    work->aut04_c_ = 0;
    work->aut04_d_ = 0;
    modes[3] = _edges_X;
    (work->aut05_v1_).data[0] = 0;
    (work->aut05_v1_).data[1] = 0;
    (work->aut05_v1_).data[2] = 0;
    (work->aut05_v1_).data[3] = 0;
    (work->aut05_v1_).data[4] = 0;
    (work->aut05_v2_).data[0] = 0;
    (work->aut05_v2_).data[1] = 0;
    (work->aut05_v2_).data[2] = 0;
    (work->aut05_v2_).data[3] = 0;
    (work->aut05_v2_).data[4] = 0;
    modes[4] = _edges_X;
    (work->aut06_v1_)._field0 = 0;
    (work->aut06_v1_)._field1 = 0;
    (work->aut06_v2_)._field0 = 0;
    (work->aut06_v2_)._field1 = 0;
    work->aut06_x_ = 0;
    work->aut06_y_ = 0;
    modes[5] = _edges_X;
    cstate[2] = 0.0;
    cstate[3] = 0.0;
    modes[6] = _edges_X;
    ((work->aut08_tt1_)._field0)._field0 = 0;
    ((work->aut08_tt1_)._field0)._field1 = 0;
    StringTypeCopyText(&((work->aut08_tt1_)._field1), "");
    ((work->aut08_tt2_)._field0)._field0 = 0;
    ((work->aut08_tt2_)._field0)._field1 = 0;
    StringTypeCopyText(&((work->aut08_tt2_)._field1), "");
    (work->aut08_t_)._field0 = 0;
    (work->aut08_t_)._field1 = 0;
    work->aut08_i_ = 0;
    work->aut08_j_ = 0;
    StringTypeCopyText(&(work->aut08_s_), "");
    modes[7] = _edges_X;
    ((work->aut09_ll1_).data[0]).data[0] = 0;
    ((work->aut09_ll1_).data[0]).data[1] = 0;
    ((work->aut09_ll1_).data[0]).data[2] = 0;
    ((work->aut09_ll1_).data[1]).data[0] = 0;
    ((work->aut09_ll1_).data[1]).data[1] = 0;
    ((work->aut09_ll1_).data[1]).data[2] = 0;
    ((work->aut09_ll2_).data[0]).data[0] = 0;
    ((work->aut09_ll2_).data[0]).data[1] = 0;
    ((work->aut09_ll2_).data[0]).data[2] = 0;
    ((work->aut09_ll2_).data[1]).data[0] = 0;
    ((work->aut09_ll2_).data[1]).data[1] = 0;
    ((work->aut09_ll2_).data[1]).data[2] = 0;
    (work->aut09_l_).data[0] = 0;
    (work->aut09_l_).data[1] = 0;
    (work->aut09_l_).data[2] = 0;
    work->aut09_i_ = 0;
    work->aut09_j_ = 0;
    modes[8] = _edges_X;
    StringTypeCopyText(&((work->aut10_x1_)._field0), "");
    ((((work->aut10_x1_)._field1).data[0])._field0).data[0] = 0;
    ((((work->aut10_x1_)._field1).data[0])._field1).data[0] = 0.0;
    ((((work->aut10_x1_)._field1).data[1])._field0).data[0] = 0;
    ((((work->aut10_x1_)._field1).data[1])._field1).data[0] = 0.0;
    StringTypeCopyText(&((work->aut10_x2_)._field0), "");
    ((((work->aut10_x2_)._field1).data[0])._field0).data[0] = 0;
    ((((work->aut10_x2_)._field1).data[0])._field1).data[0] = 0.0;
    ((((work->aut10_x2_)._field1).data[1])._field0).data[0] = 0;
    ((((work->aut10_x2_)._field1).data[1])._field1).data[0] = 0.0;
    (((work->aut10_l_).data[0])._field0).data[0] = 0;
    (((work->aut10_l_).data[0])._field1).data[0] = 0.0;
    (((work->aut10_l_).data[1])._field0).data[0] = 0;
    (((work->aut10_l_).data[1])._field1).data[0] = 0.0;
    (work->aut10_li_).data[0] = 0;
    (work->aut10_lr_).data[0] = 0.0;
    work->aut10_i_ = 0;
    work->aut10_r_ = 0.0;
    modes[9] = _edges_X;
    ((work->aut11_v1_).data[0])._field0 = 0;
    ((work->aut11_v1_).data[0])._field1 = 0;
    ((work->aut11_v1_).data[1])._field0 = 0;
    ((work->aut11_v1_).data[1])._field1 = 0;
    ((work->aut11_v1_).data[2])._field0 = 0;
    ((work->aut11_v1_).data[2])._field1 = 0;
    modes[10] = _edges_X;
    work->aut12_x_ = 0.0;
    work->aut12_y_ = 0.0;
    work->aut12_z_ = 0.0;
    work->aut12_td_ = 0.0;
    cstate[4] = 0.0;
    cstate[5] = 0.0;
    modes[11] = _edges_X;
    work->aut13_x_ = 0.0;
    work->aut13_y_ = 0.0;
    work->aut13_z_ = 0.0;
    modes[12] = _edges_X;
    modes[13] = _edges_X;
}
#endif
/* }}} */

/* {{{ mdlZeroCrossings */
#define MDL_ZERO_CROSSINGS
#if defined(MDL_ZERO_CROSSINGS) && (defined(MATLAB_MEX_FILE) || defined(NRT))
static void mdlZeroCrossings(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);
    ClearInputFlags(work);
    real_T *zcSignals = ssGetNonsampledZCs(sim_struct);

    zcSignals[0] = GuardEval57(sim_struct);
    zcSignals[1] = GuardEval58(sim_struct);
    zcSignals[2] = GuardEval59(sim_struct);
    zcSignals[3] = GuardEval60(sim_struct);
    zcSignals[4] = GuardEval61(sim_struct);
    zcSignals[5] = GuardEval62(sim_struct);
    zcSignals[6] = GuardEval63(sim_struct);
    zcSignals[7] = GuardEval64(sim_struct);
}
#endif
/* }}} */

/* {{{ mdlDerivatives */
#define MDL_DERIVATIVES
#if defined(MDL_DERIVATIVES)
static void mdlDerivatives(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);
    ClearInputFlags(work);
    real_T *derivs = ssGetdX(sim_struct);

    derivs[0] = 1.0;
    derivs[1] = deriv01(sim_struct);
    derivs[2] = deriv02(sim_struct);
    derivs[3] = deriv03(sim_struct);
    derivs[4] = deriv04(sim_struct);
    derivs[5] = deriv05(sim_struct);
}
#endif
/* }}} */

/* {{{ mdlOutput */
static void mdlOutputs(SimStruct *sim_struct, int_T tid) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);
    ClearInputFlags(work);
    UNUSED_ARG(tid);

    real_T *y;
    y = ssGetOutputPortSignal(sim_struct, 0);
    *y = IntToSimulink(modes[0]);

    y = ssGetOutputPortSignal(sim_struct, 1);
    *y = IntToSimulink(modes[1]);

    y = ssGetOutputPortSignal(sim_struct, 2);
    *y = IntToSimulink(modes[2]);

    y = ssGetOutputPortSignal(sim_struct, 3);
    *y = IntToSimulink(modes[3]);

    y = ssGetOutputPortSignal(sim_struct, 4);
    *y = IntToSimulink(modes[4]);

    y = ssGetOutputPortSignal(sim_struct, 5);
    *y = IntToSimulink(modes[5]);

    y = ssGetOutputPortSignal(sim_struct, 6);
    *y = IntToSimulink(modes[6]);

    y = ssGetOutputPortSignal(sim_struct, 7);
    *y = IntToSimulink(modes[7]);

    y = ssGetOutputPortSignal(sim_struct, 8);
    *y = IntToSimulink(modes[8]);

    y = ssGetOutputPortSignal(sim_struct, 9);
    *y = IntToSimulink(modes[9]);

    y = ssGetOutputPortSignal(sim_struct, 10);
    *y = IntToSimulink(modes[10]);

    y = ssGetOutputPortSignal(sim_struct, 11);
    *y = IntToSimulink(modes[11]);

    y = ssGetOutputPortSignal(sim_struct, 12);
    *y = IntToSimulink(modes[12]);

    y = ssGetOutputPortSignal(sim_struct, 13);
    *y = IntToSimulink(modes[13]);

    y = ssGetOutputPortSignal(sim_struct, 14);
    *y = RealToSimulink(cstate[1]);

    y = ssGetOutputPortSignal(sim_struct, 15);
    *y = RealToSimulink(cstate[2]);

    y = ssGetOutputPortSignal(sim_struct, 16);
    *y = RealToSimulink(cstate[3]);

    y = ssGetOutputPortSignal(sim_struct, 17);
    *y = RealToSimulink(cstate[4]);

    y = ssGetOutputPortSignal(sim_struct, 18);
    *y = RealToSimulink(cstate[5]);

    y = ssGetOutputPortSignal(sim_struct, 19);
    *y = IntToSimulink(work->aut02_x_);

    y = ssGetOutputPortSignal(sim_struct, 20);
    *y = IntToSimulink(work->aut03_d_);

    y = ssGetOutputPortSignal(sim_struct, 21);
    *y = IntToSimulink(work->aut04_a_);

    y = ssGetOutputPortSignal(sim_struct, 22);
    *y = IntToSimulink(work->aut04_b_);

    y = ssGetOutputPortSignal(sim_struct, 23);
    *y = IntToSimulink(work->aut04_c_);

    y = ssGetOutputPortSignal(sim_struct, 24);
    *y = IntToSimulink(work->aut04_d_);

    y = ssGetOutputPortSignal(sim_struct, 25);
    A5ITypeToSimulink(y, &work->aut05_v1_);

    y = ssGetOutputPortSignal(sim_struct, 26);
    A5ITypeToSimulink(y, &work->aut05_v2_);

    y = ssGetOutputPortSignal(sim_struct, 27);
    *y = IntToSimulink(work->aut06_x_);

    y = ssGetOutputPortSignal(sim_struct, 28);
    *y = IntToSimulink(work->aut06_y_);

    y = ssGetOutputPortSignal(sim_struct, 29);
    *y = IntToSimulink(work->aut08_i_);

    y = ssGetOutputPortSignal(sim_struct, 30);
    *y = IntToSimulink(work->aut08_j_);

    y = ssGetOutputPortSignal(sim_struct, 31);
    *y = IntToSimulink(work->aut09_i_);

    y = ssGetOutputPortSignal(sim_struct, 32);
    *y = IntToSimulink(work->aut09_j_);

    y = ssGetOutputPortSignal(sim_struct, 33);
    A3ITypeToSimulink(y, &work->aut09_l_);

    y = ssGetOutputPortSignal(sim_struct, 34);
    A2A3ITypeToSimulink(y, &work->aut09_ll1_);

    y = ssGetOutputPortSignal(sim_struct, 35);
    A2A3ITypeToSimulink(y, &work->aut09_ll2_);

    y = ssGetOutputPortSignal(sim_struct, 36);
    *y = IntToSimulink(work->aut10_i_);

    y = ssGetOutputPortSignal(sim_struct, 37);
    A1ITypeToSimulink(y, &work->aut10_li_);

    y = ssGetOutputPortSignal(sim_struct, 38);
    A1RTypeToSimulink(y, &work->aut10_lr_);

    y = ssGetOutputPortSignal(sim_struct, 39);
    *y = RealToSimulink(work->aut10_r_);

    y = ssGetOutputPortSignal(sim_struct, 40);
    *y = RealToSimulink(work->aut12_td_);

    y = ssGetOutputPortSignal(sim_struct, 41);
    *y = RealToSimulink(work->aut12_x_);

    y = ssGetOutputPortSignal(sim_struct, 42);
    *y = RealToSimulink(work->aut12_y_);

    y = ssGetOutputPortSignal(sim_struct, 43);
    *y = RealToSimulink(work->aut12_z_);

    y = ssGetOutputPortSignal(sim_struct, 44);
    *y = RealToSimulink(work->aut13_x_);

    y = ssGetOutputPortSignal(sim_struct, 45);
    *y = RealToSimulink(work->aut13_y_);

    y = ssGetOutputPortSignal(sim_struct, 46);
    *y = RealToSimulink(work->aut13_z_);

    y = ssGetOutputPortSignal(sim_struct, 47);
    *y = RealToSimulink(aut12_v_(sim_struct));

    y = ssGetOutputPortSignal(sim_struct, 48);
    *y = RealToSimulink(aut12_w_(sim_struct));

    y = ssGetOutputPortSignal(sim_struct, 49);
    *y = RealToSimulink(aut13_v_(sim_struct));

    y = ssGetOutputPortSignal(sim_struct, 50);
    *y = RealToSimulink(aut13_w_(sim_struct));
}
/* }}} */

/* {{{ mdlUpdate */
#define MDL_UPDATE
#if defined(MDL_UPDATE)
static void mdlUpdate(SimStruct *sim_struct, int_T tid) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);
    ClearInputFlags(work);
    UNUSED_ARG(tid);

    /* Print statements for init or time end. */
    if (cstate[0] == 0.0) { /* Time == 0 */
        #if PRINT_OUTPUT
            /* pre-initial and post-initial prints. */
            PrintOutput(EVT_INITIAL_, TRUE);
            PrintOutput(EVT_INITIAL_, FALSE);
        #endif
    } else {
        #if PRINT_OUTPUT
            /* post-timestep print. */
            PrintOutput(EVT_DELAY_, FALSE);
        #endif
    }

    for (;;) {
        if (ExecEvent0(sim_struct)) continue;  /* (Try to) perform event "e02a". */
        if (ExecEvent1(sim_struct)) continue;  /* (Try to) perform event "e02b". */
        if (ExecEvent2(sim_struct)) continue;  /* (Try to) perform event "e03a". */
        if (ExecEvent3(sim_struct)) continue;  /* (Try to) perform event "e03b". */
        if (ExecEvent4(sim_struct)) continue;  /* (Try to) perform event "e04a". */
        if (ExecEvent5(sim_struct)) continue;  /* (Try to) perform event "e04b". */
        if (ExecEvent6(sim_struct)) continue;  /* (Try to) perform event "e04c". */
        if (ExecEvent7(sim_struct)) continue;  /* (Try to) perform event "e04d". */
        if (ExecEvent8(sim_struct)) continue;  /* (Try to) perform event "e04e". */
        if (ExecEvent9(sim_struct)) continue;  /* (Try to) perform event "e04f". */
        if (ExecEvent10(sim_struct)) continue;  /* (Try to) perform event "e05a". */
        if (ExecEvent11(sim_struct)) continue;  /* (Try to) perform event "e05b". */
        if (ExecEvent12(sim_struct)) continue;  /* (Try to) perform event "e05c". */
        if (ExecEvent13(sim_struct)) continue;  /* (Try to) perform event "e05d". */
        if (ExecEvent14(sim_struct)) continue;  /* (Try to) perform event "e05e". */
        if (ExecEvent15(sim_struct)) continue;  /* (Try to) perform event "e06a". */
        if (ExecEvent16(sim_struct)) continue;  /* (Try to) perform event "e06b". */
        if (ExecEvent17(sim_struct)) continue;  /* (Try to) perform event "e06c". */
        if (ExecEvent18(sim_struct)) continue;  /* (Try to) perform event "e06d". */
        if (ExecEvent19(sim_struct)) continue;  /* (Try to) perform event "e06e". */
        if (ExecEvent20(sim_struct)) continue;  /* (Try to) perform event "e07a". */
        if (ExecEvent21(sim_struct)) continue;  /* (Try to) perform event "e07b". */
        if (ExecEvent22(sim_struct)) continue;  /* (Try to) perform event "e08a". */
        if (ExecEvent23(sim_struct)) continue;  /* (Try to) perform event "e08b". */
        if (ExecEvent24(sim_struct)) continue;  /* (Try to) perform event "e08c". */
        if (ExecEvent25(sim_struct)) continue;  /* (Try to) perform event "e08d". */
        if (ExecEvent26(sim_struct)) continue;  /* (Try to) perform event "e08e". */
        if (ExecEvent27(sim_struct)) continue;  /* (Try to) perform event "e08f". */
        if (ExecEvent28(sim_struct)) continue;  /* (Try to) perform event "e08g". */
        if (ExecEvent29(sim_struct)) continue;  /* (Try to) perform event "e08h". */
        if (ExecEvent30(sim_struct)) continue;  /* (Try to) perform event "e09a". */
        if (ExecEvent31(sim_struct)) continue;  /* (Try to) perform event "e09b". */
        if (ExecEvent32(sim_struct)) continue;  /* (Try to) perform event "e09c". */
        if (ExecEvent33(sim_struct)) continue;  /* (Try to) perform event "e09d". */
        if (ExecEvent34(sim_struct)) continue;  /* (Try to) perform event "e09e". */
        if (ExecEvent35(sim_struct)) continue;  /* (Try to) perform event "e09f". */
        if (ExecEvent36(sim_struct)) continue;  /* (Try to) perform event "e09g". */
        if (ExecEvent37(sim_struct)) continue;  /* (Try to) perform event "e10a". */
        if (ExecEvent38(sim_struct)) continue;  /* (Try to) perform event "e10b". */
        if (ExecEvent39(sim_struct)) continue;  /* (Try to) perform event "e10c". */
        if (ExecEvent40(sim_struct)) continue;  /* (Try to) perform event "e10d". */
        if (ExecEvent41(sim_struct)) continue;  /* (Try to) perform event "e10e". */
        if (ExecEvent42(sim_struct)) continue;  /* (Try to) perform event "e10f". */
        if (ExecEvent43(sim_struct)) continue;  /* (Try to) perform event "e10g". */
        if (ExecEvent44(sim_struct)) continue;  /* (Try to) perform event "e10h". */
        if (ExecEvent45(sim_struct)) continue;  /* (Try to) perform event "e10i". */
        if (ExecEvent46(sim_struct)) continue;  /* (Try to) perform event "e11a". */
        if (ExecEvent47(sim_struct)) continue;  /* (Try to) perform event "e12a". */
        if (ExecEvent48(sim_struct)) continue;  /* (Try to) perform event "e12b". */
        if (ExecEvent49(sim_struct)) continue;  /* (Try to) perform event "e12c". */
        if (ExecEvent50(sim_struct)) continue;  /* (Try to) perform event "e12d". */
        if (ExecEvent51(sim_struct)) continue;  /* (Try to) perform event "e12e". */
        if (ExecEvent52(sim_struct)) continue;  /* (Try to) perform event "e13a". */
        if (ExecEvent53(sim_struct)) continue;  /* (Try to) perform event "e13b". */
        if (ExecEvent54(sim_struct)) continue;  /* (Try to) perform event "e13c". */
        if (ExecEvent55(sim_struct)) continue;  /* (Try to) perform event "e13d". */
        if (ExecEvent56(sim_struct)) continue;  /* (Try to) perform event "e13e". */
        if (ExecEvent57(sim_struct)) continue;  /* (Try to) perform event "e14a". */
        if (ExecEvent58(sim_struct)) continue;  /* (Try to) perform event "e14b". */
        if (ExecEvent59(sim_struct)) continue;  /* (Try to) perform event "e14c". */
        if (ExecEvent60(sim_struct)) continue;  /* (Try to) perform event "e14d". */
        if (ExecEvent61(sim_struct)) continue;  /* (Try to) perform event "e14e". */
        if (ExecEvent62(sim_struct)) continue;  /* (Try to) perform event "e14f". */
        if (ExecEvent63(sim_struct)) continue;  /* (Try to) perform event "e14g". */
        if (ExecEvent64(sim_struct)) continue;  /* (Try to) perform event "e14h". */
        if (ExecEvent65(sim_struct)) continue;  /* (Try to) perform event "tau". */
        if (ExecEvent66(sim_struct)) continue;  /* (Try to) perform event "tau". */
        if (ExecEvent67(sim_struct)) continue;  /* (Try to) perform event "tau". */
        if (ExecEvent68(sim_struct)) continue;  /* (Try to) perform event "tau". */

        break; /* None of the events triggered. */
    }

    /* Print statement for time start. */
    #if PRINT_OUTPUT
        /* pre-timestep print. */
        PrintOutput(EVT_DELAY_, TRUE);
    #endif
}
#endif

/* }}} */

/* {{{ mdlTerminate */
static void mdlTerminate(SimStruct *sim_struct) {
    free(ssGetPWorkValue(sim_struct, 0));
    ssSetPWorkValue(sim_struct, 0, NULL);
}
/* }}} */

#ifdef MATLAB_MEX_FILE
#include "simulink.c"
#else
#include "cg_sfun.h"
#endif

