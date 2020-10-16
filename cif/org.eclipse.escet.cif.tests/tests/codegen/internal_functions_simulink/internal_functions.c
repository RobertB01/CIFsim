/* Simulink S-Function code for internal_functions CIF file.
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

#define S_FUNCTION_NAME internal_functions
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
/* CIF type: list[4] int */
struct A4I_struct {
    int_T data[4];
};
typedef struct A4I_struct A4IType;

static BoolType A4ITypeEquals(A4IType *left, A4IType *right);
static int_T A4ITypeProject(A4IType *array, IntType index);
static void A4ITypeModify(A4IType *array, IntType index, int_T value);
static int A4ITypePrint(A4IType *array, char *dest, int start, int end);
static A4IType A4ITypeFromSimulink(real_T *vec);
static void A4ITypeToSimulink(real_T *vec, A4IType *arr);

/* CIF type: tuple(int; real) */
struct T2IR_struct {
    int_T _field0;
    real_T _field1;
};
typedef struct T2IR_struct T2IRType;

static BoolType T2IRTypeEquals(T2IRType *left, T2IRType *right);
static int T2IRTypePrint(T2IRType *tuple, char *dest, int start, int end);

enum Enuminternal_functions_ {
    _internal_functions_X,
};
typedef enum Enuminternal_functions_ internal_functionsEnum;

static const char *enum_names[];
static int EnumTypePrint(internal_functionsEnum value, char *dest, int start, int end);

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

/* CIF type: tuple(int g; int h) */
struct T2II_struct {
    int_T _field0;
    int_T _field1;
};
typedef struct T2II_struct T2IIType;

static BoolType T2IITypeEquals(T2IIType *left, T2IIType *right);
static int T2IITypePrint(T2IIType *tuple, char *dest, int start, int end);

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
static BoolType A4ITypeEquals(A4IType *left, A4IType *right) {
    if (left == right) return TRUE;
    return memcmp(left, right, sizeof(A4IType)) == 0;
}

/**
 * Extract an element from the array.
 * @param array Array with value to retrieve.
 * @param index Element index in the array (not normalized).
 * @return Element value at the indicated index from the array.
 */
static int_T A4ITypeProject(A4IType *array, IntType index) {
    if (index < 0) index += 4; /* Normalize index. */
    assert(index >= 0 && index < 4);

    return array->data[index];
}

/**
 * In-place change of the array.
 * @param array Array to modify.
 * @param index Element index in the array (not normalized).
 * @param value New value to copy into the array.
 */
static void A4ITypeModify(A4IType *array, IntType index, int_T value) {
    if (index < 0) index += 4; /* Normalize index. */
    assert(index >= 0 && index < 4);

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
static int A4ITypePrint(A4IType *array, char *dest, int start, int end) {
    int last = end - 1;
    if (start < last) { dest[start++] = '['; }
    int index;
    for (index = 0; index < 4; index++) {
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
static A4IType A4ITypeFromSimulink(real_T *vec) {
    A4IType result;
    int i;
    for (i = 0; i < 4; i++) result.data[i] = SimulinkToInt(vec[i]);
    return result;
}

/**
 * Fill a Simulink vector from a CIF array.
 * @param vec Simulink vector to copy to.
 * @param arr Source array to get values from.
 */
static void A4ITypeToSimulink(real_T *vec, A4IType *arr) {
    int i;
    for (i = 0; i < 4; i++) vec[i] = IntToSimulink(arr->data[i]);
}

/**
 * Compare two tuples for equality.
 * @param left First tuple to compare.
 * @param right Second tuple to compare.
 * @return Whether both tuples are the same.
 */
static BoolType T2IRTypeEquals(T2IRType *left, T2IRType *right) {
    if (left == right) return TRUE;
    if (memcmp(&left->_field0, &right->_field0, sizeof(int_T)) != 0) return FALSE;
    if (memcmp(&left->_field1, &right->_field1, sizeof(real_T)) != 0) return FALSE;
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
static int T2IRTypePrint(T2IRType *tuple, char *dest, int start, int end) {
    int last = end - 1;
    if (start < last) { dest[start++] = '('; }
    start = IntTypePrint(tuple->_field0, dest, start, end);
    if (start < last) { dest[start++] = ','; }
    if (start < last) { dest[start++] = ' '; }
    start = RealTypePrint(tuple->_field1, dest, start, end);
    if (start < last) { dest[start++] = ')'; }
    dest[start] = '\0';
    return start;
}

static int EnumTypePrint(internal_functionsEnum value, char *dest, int start, int end) {
    int last = end - 1;
    const char *lit_name = enum_names[value];
    while (start < last && *lit_name) {
        dest[start++] = *lit_name;
        lit_name++;
    }
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

/* }}} */
/* {{{ work data structure. */
struct WorkStruct {


    int_T aut_v00_;   /**< Discrete variable "int aut.v00". */
    int_T aut_v01_;   /**< Discrete variable "int aut.v01". */
    int_T aut_v02_;   /**< Discrete variable "int aut.v02". */
    int_T aut_v03_;   /**< Discrete variable "int aut.v03". */
    int_T aut_v04_;   /**< Discrete variable "int aut.v04". */
    int_T aut_v05_;   /**< Discrete variable "int aut.v05". */
    int_T aut_v06_;   /**< Discrete variable "int aut.v06". */
    int_T aut_v07_;   /**< Discrete variable "int aut.v07". */
    int_T aut_v08_;   /**< Discrete variable "int aut.v08". */
    real_T aut_v09_;  /**< Discrete variable "real aut.v09". */
    int_T aut_v10_;   /**< Discrete variable "int aut.v10". */
    A4IType aut_v11_; /**< Discrete variable "list[4] int aut.v11". */
    A4IType aut_v12_; /**< Discrete variable "list[4] int aut.v12". */
    int_T aut_v13_;   /**< Discrete variable "int aut.v13". */
    int_T aut_v14_;   /**< Discrete variable "int aut.v14". */
    int_T aut_v15_;   /**< Discrete variable "int aut.v15". */
    int_T aut_v16_;   /**< Discrete variable "int aut.v16". */
    int_T aut_v17_;   /**< Discrete variable "int aut.v17". */
    int_T aut_v18_;   /**< Discrete variable "int aut.v18". */
    int_T aut_v19_;   /**< Discrete variable "int aut.v19". */
    int_T aut_combi_; /**< Discrete variable "int aut.combi". */


};
/* }}} */

/* {{{ algvar, derivative, function declarations. */




static int_T inc_(SimStruct *sim_struct, int_T inc_x_);
static int_T factorial_(SimStruct *sim_struct, int_T factorial_x_);
static int_T rec1_(SimStruct *sim_struct, int_T rec1_x_);
static int_T rec2_(SimStruct *sim_struct, int_T rec2_x_);
static T2IRType multi_return_(SimStruct *sim_struct);
static int_T f0_(SimStruct *sim_struct);
static int_T f1_(SimStruct *sim_struct, int_T f1_x_);
static int_T f2_(SimStruct *sim_struct, int_T f2_x_, int_T f2_y_);
static real_T f3_(SimStruct *sim_struct, int_T f3_x_, int_T f3_y_, real_T f3_z_);
static int_T locals_(SimStruct *sim_struct, int_T locals_x_);
static A4IType rot1_(SimStruct *sim_struct, A4IType* rot1_x_tmp5);
static A4IType rot2_(SimStruct *sim_struct, A4IType* rot2_x_tmp5);
static int_T fa_(SimStruct *sim_struct, int_T fa_x_);
static int_T fi_(SimStruct *sim_struct, int_T fi_x_);
static int_T fw_(SimStruct *sim_struct);
static int_T fu1_(SimStruct *sim_struct);
static int_T fu2_(SimStruct *sim_struct);
static int_T fu3_(SimStruct *sim_struct);
static int_T fr_(SimStruct *sim_struct);
/* }}} */

/* {{{ Algebraic variables, derivatives, and function definitions. */
/* {{{ Algebraic variable definitions. */

/* }}} */

/* {{{ Derivative definitions. */

/* }}} */

/* {{{ Function definitions. */
static int_T inc_(SimStruct *sim_struct, int_T inc_x_) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);

    return IntegerAdd(inc_x_, 1);
    assert(0); /* Falling through the end of the function. */
}

static int_T factorial_(SimStruct *sim_struct, int_T factorial_x_) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);

    {
        int_T if_dest5;
        if ((factorial_x_) == (0)) {
            if_dest5 = 1;
        } else {
            if_dest5 = IntegerMultiply(factorial_x_, factorial_(sim_struct, IntegerSubtract(factorial_x_, 1)));
        }
        return if_dest5;
    }
    assert(0); /* Falling through the end of the function. */
}

static int_T rec1_(SimStruct *sim_struct, int_T rec1_x_) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);

    {
        int_T if_dest5;
        if ((rec1_x_) == (0)) {
            if_dest5 = 1;
        } else {
            if_dest5 = rec2_(sim_struct, IntegerSubtract(rec1_x_, 1));
        }
        return if_dest5;
    }
    assert(0); /* Falling through the end of the function. */
}

static int_T rec2_(SimStruct *sim_struct, int_T rec2_x_) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);

    {
        int_T if_dest5;
        if ((rec2_x_) == (0)) {
            if_dest5 = 2;
        } else {
            if_dest5 = rec1_(sim_struct, IntegerSubtract(rec2_x_, 1));
        }
        return if_dest5;
    }
    assert(0); /* Falling through the end of the function. */
}

static T2IRType multi_return_(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);

    {
        T2IRType tuple_tmp5;
        (tuple_tmp5)._field0 = 1;
        (tuple_tmp5)._field1 = 1.0;
        return tuple_tmp5;
    }
    assert(0); /* Falling through the end of the function. */
}

static int_T f0_(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);

    return 1;
    assert(0); /* Falling through the end of the function. */
}

static int_T f1_(SimStruct *sim_struct, int_T f1_x_) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);

    return f1_x_;
    assert(0); /* Falling through the end of the function. */
}

static int_T f2_(SimStruct *sim_struct, int_T f2_x_, int_T f2_y_) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);

    return IntegerAdd(f2_x_, f2_y_);
    assert(0); /* Falling through the end of the function. */
}

static real_T f3_(SimStruct *sim_struct, int_T f3_x_, int_T f3_y_, real_T f3_z_) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);

    return RealAdd(IntegerAdd(f3_x_, f3_y_), f3_z_);
    assert(0); /* Falling through the end of the function. */
}

static int_T locals_(SimStruct *sim_struct, int_T locals_x_) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);

    int_T locals_a_;
    locals_a_ = 5;
    int_T locals_c_;
    locals_c_ = locals_a_;
    int_T locals_b_;
    locals_b_ = IntegerAdd(locals_c_, locals_x_);

    return locals_b_;
    assert(0); /* Falling through the end of the function. */
}

static A4IType rot1_(SimStruct *sim_struct, A4IType* rot1_x_tmp5) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);

    A4IType rot1_x_ = *(rot1_x_tmp5);
    int_T rot1_tmp_;
    rot1_tmp_ = A4ITypeProject(&(rot1_x_), 0);

    {
        int_T rhs6 = A4ITypeProject(&(rot1_x_), 3);
        int_T index7 = 0;
        A4ITypeModify(&rot1_x_, index7, rhs6);
    }

    {
        int_T rhs6 = A4ITypeProject(&(rot1_x_), 2);
        int_T index7 = 1;
        A4ITypeModify(&rot1_x_, index7, rhs6);
    }

    {
        int_T rhs6 = A4ITypeProject(&(rot1_x_), 1);
        int_T index7 = 2;
        A4ITypeModify(&rot1_x_, index7, rhs6);
    }

    {
        int_T rhs6 = rot1_tmp_;
        int_T index7 = 3;
        A4ITypeModify(&rot1_x_, index7, rhs6);
    }

    return rot1_x_;
    assert(0); /* Falling through the end of the function. */
}

static A4IType rot2_(SimStruct *sim_struct, A4IType* rot2_x_tmp5) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);

    A4IType rot2_x_ = *(rot2_x_tmp5);
    A4IType rot2_rslt_;
    (rot2_rslt_).data[0] = 0;
    (rot2_rslt_).data[1] = 0;
    (rot2_rslt_).data[2] = 0;
    (rot2_rslt_).data[3] = 0;

    {
        int_T rhs6 = A4ITypeProject(&(rot2_x_), 3);
        int_T index7 = 0;
        A4ITypeModify(&rot2_rslt_, index7, rhs6);
    }

    {
        int_T rhs6 = A4ITypeProject(&(rot2_x_), 2);
        int_T index7 = 1;
        A4ITypeModify(&rot2_rslt_, index7, rhs6);
    }

    {
        int_T rhs6 = A4ITypeProject(&(rot2_x_), 1);
        int_T index7 = 2;
        A4ITypeModify(&rot2_rslt_, index7, rhs6);
    }

    {
        int_T rhs6 = A4ITypeProject(&(rot2_x_), 0);
        int_T index7 = 3;
        A4ITypeModify(&rot2_rslt_, index7, rhs6);
    }

    return rot2_x_;
    assert(0); /* Falling through the end of the function. */
}

static int_T fa_(SimStruct *sim_struct, int_T fa_x_) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);

    A3IType fa_y_;
    (fa_y_).data[0] = fa_x_;
    (fa_y_).data[1] = fa_x_;
    (fa_y_).data[2] = fa_x_;
    int_T fa_a_;
    fa_a_ = fa_x_;
    int_T fa_b_;
    fa_b_ = IntegerAdd(fa_x_, 1);
    T2IIType fa_t_;
    (fa_t_)._field0 = 0;
    (fa_t_)._field1 = 0;

    {
        int_T rhs5 = 1;
        int_T index6 = 0;
        A3ITypeModify(&fa_y_, index6, rhs5);
    }
    {
        int_T rhs5 = 2;
        int_T index6 = 1;
        A3ITypeModify(&fa_y_, index6, rhs5);
    }

    (fa_y_).data[0] = IntegerAdd(A3ITypeProject(&(fa_y_), 0), 1);
    (fa_y_).data[1] = A3ITypeProject(&(fa_y_), 1);
    (fa_y_).data[2] = A3ITypeProject(&(fa_y_), 2);

    {
        int_T fa_a_tmp5 = fa_a_;
        fa_a_ = fa_b_;
        fa_b_ = fa_a_tmp5;
    }

    (fa_t_)._field0 = IntegerAdd(fa_a_, fa_b_);
    (fa_t_)._field1 = IntegerSubtract(fa_b_, fa_a_);

    {
        T2IIType rhs5 = fa_t_;
        fa_a_ = (rhs5)._field0;
        fa_b_ = (rhs5)._field1;
    }

    fa_x_ = IntegerAdd(IntegerAdd(fa_a_, fa_b_), A3ITypeProject(&(fa_y_), 0));

    return fa_x_;
    assert(0); /* Falling through the end of the function. */
}

static int_T fi_(SimStruct *sim_struct, int_T fi_x_) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);

    if ((fi_x_) == (1)) {
        fi_x_ = IntegerAdd(fi_x_, 1);
    }

    if ((fi_x_) == (1)) {
        fi_x_ = IntegerAdd(fi_x_, 1);
    } else if ((fi_x_) == (2)) {
        fi_x_ = IntegerAdd(fi_x_, 2);
    }

    if ((fi_x_) == (2)) {
        fi_x_ = IntegerAdd(fi_x_, 1);
    } else if ((fi_x_) == (3)) {
        fi_x_ = IntegerAdd(fi_x_, 2);
    } else if ((fi_x_) == (4)) {
        fi_x_ = IntegerAdd(fi_x_, 3);
    }

    if ((fi_x_) == (2)) {
        fi_x_ = IntegerAdd(fi_x_, 1);
    } else if ((fi_x_) == (3)) {
        fi_x_ = IntegerAdd(fi_x_, 2);
    } else {
        fi_x_ = IntegerAdd(fi_x_, 4);
    }

    if ((fi_x_) == (6)) {
        fi_x_ = IntegerAdd(fi_x_, 1);
    } else {
        fi_x_ = IntegerAdd(fi_x_, 2);
    }

    if ((fi_x_) > (4)) {
        if ((fi_x_) < (6)) {
            fi_x_ = IntegerSubtract(fi_x_, 1);
        } else {
            fi_x_ = IntegerSubtract(fi_x_, 2);
        }
    }

    return fi_x_;
    assert(0); /* Falling through the end of the function. */
}

static int_T fw_(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);

    int_T fw_x_;
    fw_x_ = 0;

    while ((fw_x_) > (0)) {
        while ((fw_x_) < (10)) {
            if (((fw_x_) % (2)) == (1)) {
                continue;
            }

            if ((fw_x_) == (8)) {
                break;
            }
        }
    }

    return fw_x_;
    assert(0); /* Falling through the end of the function. */
}

static int_T fu1_(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);

    if (TRUE) {
        return 1;
    }

    return 0;
    assert(0); /* Falling through the end of the function. */
}

static int_T fu2_(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);

    return 1;

    return 0;
    assert(0); /* Falling through the end of the function. */
}

static int_T fu3_(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);

    while (TRUE) {
        return 1;
    }

    return 0;
    assert(0); /* Falling through the end of the function. */
}

static int_T fr_(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);

    A3IType fr_x_;
    (fr_x_).data[0] = 1;
    (fr_x_).data[1] = 2;
    (fr_x_).data[2] = 3;
    A3IType fr_y_;
    (fr_y_).data[0] = 2;
    (fr_y_).data[1] = 3;
    (fr_y_).data[2] = 4;

    {
        A3IType rhs5 = fr_y_;
        #if CHECK_RANGES
        {
            int rng_index0;
            for(rng_index0 = 0; rng_index0 < 3; rng_index0++) {
                int_T rng_elem0 = (rhs5).data[rng_index0];
                if ((rng_elem0) > 3) {
                    fprintf(stderr, "RangeError: Writing %d into \"list[3] int[0..3]\"\n", rng_elem0);
                    fprintf(stderr, "            at " "fr.x" "[%d]" "\n", rng_index0);
                    RangeErrorDetected();
                }
            }
        }
        #endif
        fr_x_ = rhs5;
    }

    {
        A3IType array_tmp6;
        (array_tmp6).data[0] = -(1);
        (array_tmp6).data[1] = 3;
        (array_tmp6).data[2] = 5;
        A3IType rhs5 = array_tmp6;
        #if CHECK_RANGES
        {
            int rng_index0;
            for(rng_index0 = 0; rng_index0 < 3; rng_index0++) {
                int_T rng_elem0 = (rhs5).data[rng_index0];
                if ((rng_elem0) < 0 || (rng_elem0) > 3) {
                    fprintf(stderr, "RangeError: Writing %d into \"list[3] int[0..3]\"\n", rng_elem0);
                    fprintf(stderr, "            at " "fr.x" "[%d]" "\n", rng_index0);
                    RangeErrorDetected();
                }
            }
        }
        #endif
        fr_x_ = rhs5;
    }

    return 1;
    assert(0); /* Falling through the end of the function. */
}
/* }}} */
/* }}} */

enum internal_functionsEventEnum_ {
    EVT_INITIAL_, /**< Initial step. */
    EVT_DELAY_,   /**< Delay step. */
    EVT_TAU_,     /**< Tau step. */
};
typedef enum internal_functionsEventEnum_ internal_functions_Event_;

const char *evt_names[] = { /** < Event names. */
    "initial-step", /**< Initial step. */
    "delay-step",   /**< Delay step. */
    "tau",          /**< Tau step. */
};

/** Enum names. */
static const char *enum_names[] = {
    "X",
};

/**
 * Reset 'loaded' status of all input variables.
 * @param work Work data.
 */
static void ClearInputFlags(struct WorkStruct *work) {

}

/* Time-dependent guards. */


/* Event execution. */


#if PRINT_OUTPUT
static void PrintOutput(internal_functions_Event_ event, BoolType pre) {
}
#endif

/* {{{ mdlInitializeSizes, mdlInitializeSampleTimes */
static void mdlInitializeSizes(SimStruct *sim_struct) {
    /* Parameters. */
    ssSetNumSFcnParams(sim_struct, 0);
    if (ssGetNumSFcnParams(sim_struct) != ssGetSFcnParamsCount(sim_struct)) return;

    /* Inputs. */
    if (!ssSetNumInputPorts(sim_struct, 0)) return;



    int idx;
    for (idx = 0; idx < 0; idx++) {
        ssSetInputPortDataType(sim_struct, idx, SS_DOUBLE);
        ssSetInputPortComplexSignal(sim_struct, idx, COMPLEX_NO);
        ssSetInputPortDirectFeedThrough(sim_struct, idx, 1); /* Assume always feed-through. */
    }

    /* Outputs. */
    if (!ssSetNumOutputPorts(sim_struct, 22)) return;

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
    ssSetOutputPortWidth(sim_struct, 13, 4);
    ssSetOutputPortWidth(sim_struct, 14, 4);
    ssSetOutputPortWidth(sim_struct, 15, 1);
    ssSetOutputPortWidth(sim_struct, 16, 1);
    ssSetOutputPortWidth(sim_struct, 17, 1);
    ssSetOutputPortWidth(sim_struct, 18, 1);
    ssSetOutputPortWidth(sim_struct, 19, 1);
    ssSetOutputPortWidth(sim_struct, 20, 1);
    ssSetOutputPortWidth(sim_struct, 21, 1);

    for (idx = 0; idx < 22; idx++) {
        ssSetOutputPortDataType(sim_struct, idx, SS_DOUBLE);
        ssSetOutputPortComplexSignal(sim_struct, idx, COMPLEX_NO);
    }

    /* Disc state and cont state. */
    ssSetNumContStates(sim_struct, 1); /* CState[0] is time. */
    ssSetNumDiscStates(sim_struct, 0);

    /* Work vectors. */
    ssSetNumRWork(sim_struct, 0);
    ssSetNumIWork(sim_struct, 0);
    ssSetNumPWork(sim_struct, 1);

    /* Modes. */
    ssSetNumModes(sim_struct, 1);

    ssSetNumSampleTimes(sim_struct, 1);
    ssSetNumNonsampledZCs(sim_struct, 0);

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

    /* Initialize discrete, continuous, and location pointer variables. */
    cstate[0] = 0.0; /* time = 0.0 */
    work->aut_v00_ = 5;
    work->aut_v01_ = inc_(sim_struct, work->aut_v00_);
    work->aut_v02_ = factorial_(sim_struct, 5);
    work->aut_v03_ = rec1_(sim_struct, 7);
    work->aut_v04_ = rec2_(sim_struct, 7);
    T2IRType ret_val1 = multi_return_(sim_struct);
    T2IRType ret_val2 = multi_return_(sim_struct);
    work->aut_v05_ = IntegerAdd((ret_val1)._field0, FloorFunction((ret_val2)._field1));
    work->aut_v06_ = f0_(sim_struct);
    work->aut_v07_ = f1_(sim_struct, 1);
    work->aut_v08_ = f2_(sim_struct, 1, 2);
    work->aut_v09_ = f3_(sim_struct, 1, 2, 3.0);
    work->aut_v10_ = locals_(sim_struct, 1);
    A4IType array_tmp3;
    (array_tmp3).data[0] = 1;
    (array_tmp3).data[1] = 2;
    (array_tmp3).data[2] = 3;
    (array_tmp3).data[3] = 4;
    work->aut_v11_ = rot1_(sim_struct, &(array_tmp3));
    A4IType array_tmp4;
    (array_tmp4).data[0] = 1;
    (array_tmp4).data[1] = 2;
    (array_tmp4).data[2] = 3;
    (array_tmp4).data[3] = 4;
    work->aut_v12_ = rot2_(sim_struct, &(array_tmp4));
    work->aut_v13_ = fa_(sim_struct, 1);
    work->aut_v14_ = fi_(sim_struct, 1);
    work->aut_v15_ = fw_(sim_struct);
    work->aut_v16_ = fu1_(sim_struct);
    work->aut_v17_ = fu2_(sim_struct);
    work->aut_v18_ = fu3_(sim_struct);
    work->aut_v19_ = fr_(sim_struct);
    work->aut_combi_ = IntegerAdd(IntegerAdd(IntegerAdd(IntegerAdd(IntegerAdd(IntegerAdd(IntegerAdd(IntegerAdd(IntegerAdd(IntegerAdd(IntegerAdd(IntegerAdd(IntegerAdd(IntegerAdd(IntegerAdd(IntegerAdd(IntegerAdd(IntegerAdd(IntegerAdd(work->aut_v00_, work->aut_v01_), work->aut_v02_), work->aut_v03_), work->aut_v04_), work->aut_v05_), work->aut_v06_), work->aut_v07_), work->aut_v08_), FloorFunction(work->aut_v09_)), work->aut_v10_), A4ITypeProject(&(work->aut_v11_), 0)), A4ITypeProject(&(work->aut_v12_), 0)), work->aut_v13_), work->aut_v14_), work->aut_v15_), work->aut_v16_), work->aut_v17_), work->aut_v18_), work->aut_v19_);
    modes[0] = _internal_functions_X;
}
#endif
/* }}} */

/* {{{ mdlZeroCrossings */
#undef MDL_ZERO_CROSSINGS
#if defined(MDL_ZERO_CROSSINGS) && (defined(MATLAB_MEX_FILE) || defined(NRT))
static void mdlZeroCrossings(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);
    real_T *zcSignals = ssGetNonsampledZCs(sim_struct);


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
    real_T *derivs = ssGetdX(sim_struct);

    derivs[0] = 1.0;
}
#endif
/* }}} */

/* {{{ mdlOutput */
static void mdlOutputs(SimStruct *sim_struct, int_T tid) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);
    UNUSED_ARG(tid);

    real_T *y;
    y = ssGetOutputPortSignal(sim_struct, 0);
    *y = IntToSimulink(modes[0]);

    y = ssGetOutputPortSignal(sim_struct, 1);
    *y = IntToSimulink(work->aut_combi_);

    y = ssGetOutputPortSignal(sim_struct, 2);
    *y = IntToSimulink(work->aut_v00_);

    y = ssGetOutputPortSignal(sim_struct, 3);
    *y = IntToSimulink(work->aut_v01_);

    y = ssGetOutputPortSignal(sim_struct, 4);
    *y = IntToSimulink(work->aut_v02_);

    y = ssGetOutputPortSignal(sim_struct, 5);
    *y = IntToSimulink(work->aut_v03_);

    y = ssGetOutputPortSignal(sim_struct, 6);
    *y = IntToSimulink(work->aut_v04_);

    y = ssGetOutputPortSignal(sim_struct, 7);
    *y = IntToSimulink(work->aut_v05_);

    y = ssGetOutputPortSignal(sim_struct, 8);
    *y = IntToSimulink(work->aut_v06_);

    y = ssGetOutputPortSignal(sim_struct, 9);
    *y = IntToSimulink(work->aut_v07_);

    y = ssGetOutputPortSignal(sim_struct, 10);
    *y = IntToSimulink(work->aut_v08_);

    y = ssGetOutputPortSignal(sim_struct, 11);
    *y = RealToSimulink(work->aut_v09_);

    y = ssGetOutputPortSignal(sim_struct, 12);
    *y = IntToSimulink(work->aut_v10_);

    y = ssGetOutputPortSignal(sim_struct, 13);
    A4ITypeToSimulink(y, &work->aut_v11_);

    y = ssGetOutputPortSignal(sim_struct, 14);
    A4ITypeToSimulink(y, &work->aut_v12_);

    y = ssGetOutputPortSignal(sim_struct, 15);
    *y = IntToSimulink(work->aut_v13_);

    y = ssGetOutputPortSignal(sim_struct, 16);
    *y = IntToSimulink(work->aut_v14_);

    y = ssGetOutputPortSignal(sim_struct, 17);
    *y = IntToSimulink(work->aut_v15_);

    y = ssGetOutputPortSignal(sim_struct, 18);
    *y = IntToSimulink(work->aut_v16_);

    y = ssGetOutputPortSignal(sim_struct, 19);
    *y = IntToSimulink(work->aut_v17_);

    y = ssGetOutputPortSignal(sim_struct, 20);
    *y = IntToSimulink(work->aut_v18_);

    y = ssGetOutputPortSignal(sim_struct, 21);
    *y = IntToSimulink(work->aut_v19_);
}
/* }}} */

/* {{{ mdlUpdate */
#define MDL_UPDATE
#if defined(MDL_UPDATE)
static void mdlUpdate(SimStruct *sim_struct, int_T tid) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);
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

