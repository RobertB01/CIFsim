/* Simulink S-Function code for exprs CIF file.
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

#define S_FUNCTION_NAME exprs
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
enum Enumexprs_ {
    _exprs_A,
    _exprs_B,
};
typedef enum Enumexprs_ exprsEnum;

static const char *enum_names[];
static int EnumTypePrint(exprsEnum value, char *dest, int start, int end);

/* CIF type: list[2] int */
struct A2I_struct {
    int_T data[2];
};
typedef struct A2I_struct A2IType;

static BoolType A2ITypeEquals(A2IType *left, A2IType *right);
static int_T A2ITypeProject(A2IType *array, IntType index);
static void A2ITypeModify(A2IType *array, IntType index, int_T value);
static int A2ITypePrint(A2IType *array, char *dest, int start, int end);
static A2IType A2ITypeFromSimulink(real_T *vec);
static void A2ITypeToSimulink(real_T *vec, A2IType *arr);

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

/* CIF type: list[1] bool */
struct A1B_struct {
    BoolType data[1];
};
typedef struct A1B_struct A1BType;

static BoolType A1BTypeEquals(A1BType *left, A1BType *right);
static BoolType A1BTypeProject(A1BType *array, IntType index);
static void A1BTypeModify(A1BType *array, IntType index, BoolType value);
static int A1BTypePrint(A1BType *array, char *dest, int start, int end);
static A1BType A1BTypeFromSimulink(real_T *vec);
static void A1BTypeToSimulink(real_T *vec, A1BType *arr);

/* CIF type: tuple(int a; int b) */
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
static int EnumTypePrint(exprsEnum value, char *dest, int start, int end) {
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
static BoolType A2ITypeEquals(A2IType *left, A2IType *right) {
    if (left == right) return TRUE;
    return memcmp(left, right, sizeof(A2IType)) == 0;
}

/**
 * Extract an element from the array.
 * @param array Array with value to retrieve.
 * @param index Element index in the array (not normalized).
 * @return Element value at the indicated index from the array.
 */
static int_T A2ITypeProject(A2IType *array, IntType index) {
    if (index < 0) index += 2; /* Normalize index. */
    assert(index >= 0 && index < 2);

    return array->data[index];
}

/**
 * In-place change of the array.
 * @param array Array to modify.
 * @param index Element index in the array (not normalized).
 * @param value New value to copy into the array.
 */
static void A2ITypeModify(A2IType *array, IntType index, int_T value) {
    if (index < 0) index += 2; /* Normalize index. */
    assert(index >= 0 && index < 2);

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
static int A2ITypePrint(A2IType *array, char *dest, int start, int end) {
    int last = end - 1;
    if (start < last) { dest[start++] = '['; }
    int index;
    for (index = 0; index < 2; index++) {
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
static A2IType A2ITypeFromSimulink(real_T *vec) {
    A2IType result;
    int i;
    for (i = 0; i < 2; i++) result.data[i] = SimulinkToInt(vec[i]);
    return result;
}

/**
 * Fill a Simulink vector from a CIF array.
 * @param vec Simulink vector to copy to.
 * @param arr Source array to get values from.
 */
static void A2ITypeToSimulink(real_T *vec, A2IType *arr) {
    int i;
    for (i = 0; i < 2; i++) vec[i] = IntToSimulink(arr->data[i]);
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
static BoolType A1BTypeEquals(A1BType *left, A1BType *right) {
    if (left == right) return TRUE;
    return memcmp(left, right, sizeof(A1BType)) == 0;
}

/**
 * Extract an element from the array.
 * @param array Array with value to retrieve.
 * @param index Element index in the array (not normalized).
 * @return Element value at the indicated index from the array.
 */
static BoolType A1BTypeProject(A1BType *array, IntType index) {
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
static void A1BTypeModify(A1BType *array, IntType index, BoolType value) {
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
static int A1BTypePrint(A1BType *array, char *dest, int start, int end) {
    int last = end - 1;
    if (start < last) { dest[start++] = '['; }
    int index;
    for (index = 0; index < 1; index++) {
        if (index > 0) {
            if (start < last) { dest[start++] = ','; }
            if (start < last) { dest[start++] = ' '; }
        }
        start = BoolTypePrint(array->data[index], dest, start, end);
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
static A1BType A1BTypeFromSimulink(real_T *vec) {
    A1BType result;
    int i;
    for (i = 0; i < 1; i++) result.data[i] = SimulinkToBool(vec[i]);
    return result;
}

/**
 * Fill a Simulink vector from a CIF array.
 * @param vec Simulink vector to copy to.
 * @param arr Source array to get values from.
 */
static void A1BTypeToSimulink(real_T *vec, A1BType *arr) {
    int i;
    for (i = 0; i < 1; i++) vec[i] = BoolToSimulink(arr->data[i]);
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

    /** Constant "x1". */
    int_T x1_;

    /** Discrete variable "int a1.x". */
    int_T a1_x_;

    /** Discrete variable "bool AA.vb". */
    BoolType AA_vb_;

    /** Discrete variable "int AA.vi". */
    int_T AA_vi_;

    /** Discrete variable "int[1..3] AA.vp". */
    int_T AA_vp_;

    /** Discrete variable "int[-5..-1] AA.vn". */
    int_T AA_vn_;

    /** Discrete variable "int[0..5] AA.vz". */
    int_T AA_vz_;

    /** Discrete variable "real AA.vr". */
    real_T AA_vr_;

    /** Discrete variable "string AA.vs". */
    StringType AA_vs_;

    /** Discrete variable "E AA.ve". */
    exprsEnum AA_ve_;

    /** Discrete variable "list[2] int AA.va". */
    A2IType AA_va_;

    /** Discrete variable "real AA.v2". */
    real_T AA_v2_;

    /** Discrete variable "real AA.i2r". */
    real_T AA_i2r_;

    /** Discrete variable "string AA.b2s". */
    StringType AA_b2s_;

    /** Discrete variable "string AA.i2s". */
    StringType AA_i2s_;

    /** Discrete variable "string AA.r2s". */
    StringType AA_r2s_;

    /** Discrete variable "bool AA.s2b". */
    BoolType AA_s2b_;

    /** Discrete variable "int AA.s2i". */
    int_T AA_s2i_;

    /** Discrete variable "real AA.s2r". */
    real_T AA_s2r_;

    /** Discrete variable "list[3] int AA.self_cast1". */
    A3IType AA_self_cast1_;

    /** Discrete variable "list[3] int AA.self_cast2". */
    A3IType AA_self_cast2_;

    /** Discrete variable "bool AA.inv1". */
    BoolType AA_inv1_;

    /** Discrete variable "bool AA.inv2". */
    BoolType AA_inv2_;

    /** Discrete variable "int AA.neg1". */
    int_T AA_neg1_;

    /** Discrete variable "int AA.neg2". */
    int_T AA_neg2_;

    /** Discrete variable "int AA.neg3". */
    int_T AA_neg3_;

    /** Discrete variable "int AA.neg4". */
    int_T AA_neg4_;

    /** Discrete variable "int AA.pos1". */
    int_T AA_pos1_;

    /** Discrete variable "int AA.pos2". */
    int_T AA_pos2_;

    /** Discrete variable "int AA.posneg". */
    int_T AA_posneg_;

    /** Discrete variable "list[1] bool AA.l3i". */
    A1BType AA_l3i_;

    /** Discrete variable "int[0..4] AA.idx1". */
    int_T AA_idx1_;

    /** Discrete variable "bool AA.vt". */
    BoolType AA_vt_;

    /** Discrete variable "bool AA.vf". */
    BoolType AA_vf_;

    /** Discrete variable "bool AA.short_and". */
    BoolType AA_short_and_;

    /** Discrete variable "bool AA.short_or". */
    BoolType AA_short_or_;

    /** Discrete variable "bool AA.impl". */
    BoolType AA_impl_;

    /** Discrete variable "bool AA.biimpl". */
    BoolType AA_biimpl_;

    /** Discrete variable "bool AA.conj". */
    BoolType AA_conj_;

    /** Discrete variable "bool AA.disj". */
    BoolType AA_disj_;

    /** Discrete variable "bool AA.lt1". */
    BoolType AA_lt1_;

    /** Discrete variable "bool AA.le1". */
    BoolType AA_le1_;

    /** Discrete variable "bool AA.gt1". */
    BoolType AA_gt1_;

    /** Discrete variable "bool AA.ge1". */
    BoolType AA_ge1_;

    /** Discrete variable "bool AA.lt2". */
    BoolType AA_lt2_;

    /** Discrete variable "bool AA.le2". */
    BoolType AA_le2_;

    /** Discrete variable "bool AA.gt2". */
    BoolType AA_gt2_;

    /** Discrete variable "bool AA.ge2". */
    BoolType AA_ge2_;

    /** Discrete variable "bool AA.lt3". */
    BoolType AA_lt3_;

    /** Discrete variable "bool AA.le3". */
    BoolType AA_le3_;

    /** Discrete variable "bool AA.gt3". */
    BoolType AA_gt3_;

    /** Discrete variable "bool AA.ge3". */
    BoolType AA_ge3_;

    /** Discrete variable "bool AA.lt4". */
    BoolType AA_lt4_;

    /** Discrete variable "bool AA.le4". */
    BoolType AA_le4_;

    /** Discrete variable "bool AA.gt4". */
    BoolType AA_gt4_;

    /** Discrete variable "bool AA.ge4". */
    BoolType AA_ge4_;

    /** Discrete variable "bool AA.eq1". */
    BoolType AA_eq1_;

    /** Discrete variable "bool AA.eq2". */
    BoolType AA_eq2_;

    /** Discrete variable "bool AA.eq3". */
    BoolType AA_eq3_;

    /** Discrete variable "bool AA.eq4". */
    BoolType AA_eq4_;

    /** Discrete variable "bool AA.eq5". */
    BoolType AA_eq5_;

    /** Discrete variable "bool AA.ne1". */
    BoolType AA_ne1_;

    /** Discrete variable "bool AA.ne2". */
    BoolType AA_ne2_;

    /** Discrete variable "bool AA.ne3". */
    BoolType AA_ne3_;

    /** Discrete variable "bool AA.ne4". */
    BoolType AA_ne4_;

    /** Discrete variable "bool AA.ne5". */
    BoolType AA_ne5_;

    /** Discrete variable "int AA.add1". */
    int_T AA_add1_;

    /** Discrete variable "real AA.add2". */
    real_T AA_add2_;

    /** Discrete variable "real AA.add3". */
    real_T AA_add3_;

    /** Discrete variable "real AA.add4". */
    real_T AA_add4_;

    /** Discrete variable "string AA.add5". */
    StringType AA_add5_;

    /** Discrete variable "int AA.add6". */
    int_T AA_add6_;

    /** Discrete variable "int AA.add7". */
    int_T AA_add7_;

    /** Discrete variable "int AA.add8". */
    int_T AA_add8_;

    /** Discrete variable "int AA.sub1". */
    int_T AA_sub1_;

    /** Discrete variable "real AA.sub2". */
    real_T AA_sub2_;

    /** Discrete variable "real AA.sub3". */
    real_T AA_sub3_;

    /** Discrete variable "real AA.sub4". */
    real_T AA_sub4_;

    /** Discrete variable "int AA.sub5". */
    int_T AA_sub5_;

    /** Discrete variable "int AA.sub6". */
    int_T AA_sub6_;

    /** Discrete variable "int AA.sub7". */
    int_T AA_sub7_;

    /** Discrete variable "int AA.mul1". */
    int_T AA_mul1_;

    /** Discrete variable "real AA.mul2". */
    real_T AA_mul2_;

    /** Discrete variable "real AA.mul3". */
    real_T AA_mul3_;

    /** Discrete variable "real AA.mul4". */
    real_T AA_mul4_;

    /** Discrete variable "int AA.mul5". */
    int_T AA_mul5_;

    /** Discrete variable "int AA.mul6". */
    int_T AA_mul6_;

    /** Discrete variable "int AA.mul7". */
    int_T AA_mul7_;

    /** Discrete variable "real AA.rdiv1". */
    real_T AA_rdiv1_;

    /** Discrete variable "real AA.rdiv2". */
    real_T AA_rdiv2_;

    /** Discrete variable "real AA.rdiv3". */
    real_T AA_rdiv3_;

    /** Discrete variable "real AA.rdiv4". */
    real_T AA_rdiv4_;

    /** Discrete variable "real AA.rdiv5". */
    real_T AA_rdiv5_;

    /** Discrete variable "real AA.rdiv6". */
    real_T AA_rdiv6_;

    /** Discrete variable "int AA.div1". */
    int_T AA_div1_;

    /** Discrete variable "int AA.div2". */
    int_T AA_div2_;

    /** Discrete variable "int AA.div3". */
    int_T AA_div3_;

    /** Discrete variable "int AA.div4". */
    int_T AA_div4_;

    /** Discrete variable "int AA.mod1". */
    int_T AA_mod1_;

    /** Discrete variable "int AA.mod2". */
    int_T AA_mod2_;

    /** Discrete variable "list[2] int AA.li". */
    A2IType AA_li_;

    /** Discrete variable "tuple(int a; int b) AA.tii". */
    T2IIType AA_tii_;

    /** Discrete variable "string AA.ss". */
    StringType AA_ss_;

    /** Discrete variable "int AA.proj1". */
    int_T AA_proj1_;

    /** Discrete variable "int AA.proj2". */
    int_T AA_proj2_;

    /** Discrete variable "int AA.proj3". */
    int_T AA_proj3_;

    /** Discrete variable "int AA.proj4". */
    int_T AA_proj4_;

    /** Discrete variable "string AA.proj5". */
    StringType AA_proj5_;

    /** Discrete variable "string AA.proj6". */
    StringType AA_proj6_;

    /** Discrete variable "real AA.f_acos". */
    real_T AA_f_acos_;

    /** Discrete variable "real AA.f_asin". */
    real_T AA_f_asin_;

    /** Discrete variable "real AA.f_atan". */
    real_T AA_f_atan_;

    /** Discrete variable "real AA.f_cos". */
    real_T AA_f_cos_;

    /** Discrete variable "real AA.f_sin". */
    real_T AA_f_sin_;

    /** Discrete variable "real AA.f_tan". */
    real_T AA_f_tan_;

    /** Discrete variable "int AA.f_abs1". */
    int_T AA_f_abs1_;

    /** Discrete variable "int AA.f_abs12". */
    int_T AA_f_abs12_;

    /** Discrete variable "real AA.f_abs2". */
    real_T AA_f_abs2_;

    /** Discrete variable "real AA.f_cbrt". */
    real_T AA_f_cbrt_;

    /** Discrete variable "int AA.f_ceil". */
    int_T AA_f_ceil_;

    /** Discrete variable "bool AA.f_empty". */
    BoolType AA_f_empty_;

    /** Discrete variable "real AA.f_exp". */
    real_T AA_f_exp_;

    /** Discrete variable "int AA.f_floor". */
    int_T AA_f_floor_;

    /** Discrete variable "real AA.f_ln". */
    real_T AA_f_ln_;

    /** Discrete variable "real AA.f_log". */
    real_T AA_f_log_;

    /** Discrete variable "int AA.f_max1". */
    int_T AA_f_max1_;

    /** Discrete variable "real AA.f_max2". */
    real_T AA_f_max2_;

    /** Discrete variable "real AA.f_max3". */
    real_T AA_f_max3_;

    /** Discrete variable "real AA.f_max4". */
    real_T AA_f_max4_;

    /** Discrete variable "int AA.f_min1". */
    int_T AA_f_min1_;

    /** Discrete variable "real AA.f_min2". */
    real_T AA_f_min2_;

    /** Discrete variable "real AA.f_min3". */
    real_T AA_f_min3_;

    /** Discrete variable "real AA.f_min4". */
    real_T AA_f_min4_;

    /** Discrete variable "real AA.f_pow1". */
    real_T AA_f_pow1_;

    /** Discrete variable "int AA.f_pow12". */
    int_T AA_f_pow12_;

    /** Discrete variable "real AA.f_pow2". */
    real_T AA_f_pow2_;

    /** Discrete variable "real AA.f_pow3". */
    real_T AA_f_pow3_;

    /** Discrete variable "real AA.f_pow4". */
    real_T AA_f_pow4_;

    /** Discrete variable "int AA.f_round". */
    int_T AA_f_round_;

    /** Discrete variable "real AA.f_scale". */
    real_T AA_f_scale_;

    /** Discrete variable "int AA.f_sign1". */
    int_T AA_f_sign1_;

    /** Discrete variable "int AA.f_sign2". */
    int_T AA_f_sign2_;

    /** Discrete variable "int AA.f_size1". */
    int_T AA_f_size1_;

    /** Discrete variable "int AA.f_size2". */
    int_T AA_f_size2_;

    /** Discrete variable "real AA.f_sqrt". */
    real_T AA_f_sqrt_;

    /** Input variable "int x8". */
    int_T x8_;

    unsigned char input_loaded00;
};
/* }}} */

/* {{{ algvar, derivative, function declarations. */
static int_T v1_(SimStruct *sim_struct);
static int_T if1_(SimStruct *sim_struct);
static int_T if2_(SimStruct *sim_struct);
static int_T if3_(SimStruct *sim_struct);
static int_T fcall1_(SimStruct *sim_struct);
static int_T fcall2_(SimStruct *sim_struct);
static exprsEnum vea_(SimStruct *sim_struct);
static int_T x2_(SimStruct *sim_struct);
static int_T x3_(SimStruct *sim_struct);
static int_T x4_(SimStruct *sim_struct);
static real_T x6_(SimStruct *sim_struct);
static BoolType x7_(SimStruct *sim_struct);
static int_T x9_(SimStruct *sim_struct);


static real_T deriv01(SimStruct *sim_struct);

static int_T f1_(SimStruct *sim_struct, int_T f1_x_);
static int_T inc_(SimStruct *sim_struct, int_T inc_x_);
/* }}} */

/* {{{ Algebraic variables, derivatives, and function definitions. */
/* {{{ Algebraic variable definitions. */
/** Algebraic variable v1 = if M.a1_x > 0, M.a1_x < 5: 0 elif M.a1_x > 6, M.a1_x < 9: 1 else 2 end. */
static int_T v1_(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    int_T if_dest1;
    if (((work->a1_x_) > (0)) && ((work->a1_x_) < (5))) {
        if_dest1 = 0;
    } else if (((work->a1_x_) > (6)) && ((work->a1_x_) < (9))) {
        if_dest1 = 1;
    } else {
        if_dest1 = 2;
    }
    return if_dest1;
}

/** Algebraic variable if1 = if time > 1: 1 else 0 end. */
static int_T if1_(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    int_T if_dest2;
    if ((cstate[0]) > (1)) {
        if_dest2 = 1;
    } else {
        if_dest2 = 0;
    }
    return if_dest2;
}

/** Algebraic variable if2 = if time > 1: 1 elif time > 0.5: 2 else 0 end + 1. */
static int_T if2_(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    int_T if_dest3;
    if ((cstate[0]) > (1)) {
        if_dest3 = 1;
    } else if ((cstate[0]) > (0.5)) {
        if_dest3 = 2;
    } else {
        if_dest3 = 0;
    }
    return (if_dest3) + (1);
}

/** Algebraic variable if3 = if time > 1: 1 elif time > 0.5: 2 elif time > 0.25: 3 else 0 end + 2. */
static int_T if3_(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    int_T if_dest4;
    if ((cstate[0]) > (1)) {
        if_dest4 = 1;
    } else if ((cstate[0]) > (0.5)) {
        if_dest4 = 2;
    } else if ((cstate[0]) > (0.25)) {
        if_dest4 = 3;
    } else {
        if_dest4 = 0;
    }
    return (if_dest4) + (2);
}

/** Algebraic variable fcall1 = inc(0). */
static int_T fcall1_(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    return inc_(sim_struct, 0);
}

/** Algebraic variable fcall2 = inc(inc(0)). */
static int_T fcall2_(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    return inc_(sim_struct, inc_(sim_struct, 0));
}

/** Algebraic variable vea = A. */
static exprsEnum vea_(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    return _exprs_A;
}

/** Algebraic variable x2 = x1. */
static int_T x2_(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    return work->x1_;
}

/** Algebraic variable x3 = x2. */
static int_T x3_(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    return x2_(sim_struct);
}

/** Algebraic variable x4 = M.a1_x. */
static int_T x4_(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    return work->a1_x_;
}

/** Algebraic variable x6 = x5 + x5'. */
static real_T x6_(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    return RealAdd(cstate[1], deriv01(sim_struct));
}

/** Algebraic variable x7 = vea = B. */
static BoolType x7_(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    return (vea_(sim_struct)) == (_exprs_B);
}

/** Algebraic variable x9 = x8 + 1. */
static int_T x9_(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    if (!work->input_loaded00) {
        InputRealPtrsType uPtrs = ssGetInputPortRealSignalPtrs(sim_struct, 0);
        work->x8_ = SimulinkToInt(*uPtrs[0]);
        work->input_loaded00 = TRUE;
    }
    return IntegerAdd(work->x8_, 1);
}
/* }}} */

/* {{{ Derivative definitions. */
/** Derivative of "x5". */
static real_T deriv01(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);

    return 1.0;
}
/* }}} */

/* {{{ Function definitions. */
static int_T f1_(SimStruct *sim_struct, int_T f1_x_) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);

    while (((f1_x_) != (0)) && ((f1_x_) != (4))) {
        f1_x_ = IntegerSubtract(f1_x_, 1);
    }

    if (((f1_x_) != (1)) && ((f1_x_) != (2))) {
        f1_x_ = 3;
    } else if (((f1_x_) != (2)) && ((f1_x_) != (3))) {
        f1_x_ = 4;
    }

    return f1_x_;
    assert(0); /* Falling through the end of the function. */
}

static int_T inc_(SimStruct *sim_struct, int_T inc_x_) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);

    return IntegerAdd(inc_x_, 1);
    assert(0); /* Falling through the end of the function. */
}
/* }}} */
/* }}} */

enum exprsEventEnum_ {
    /** Initial step. */
    EVT_INITIAL_,

    /** Delay step. */
    EVT_DELAY_,

    /** Tau step. */
    EVT_TAU_,
};
typedef enum exprsEventEnum_ exprs_Event_;

const char *evt_names[] = { /** < Event names. */
    "initial-step", /**< Initial step. */
    "delay-step",   /**< Delay step. */
    "tau",          /**< Tau step. */
};

/** Enum names. */
static const char *enum_names[] = {
    "A",
    "B",
};

/**
 * Reset 'loaded' status of all input variables.
 * @param work Work data.
 */
static void ClearInputFlags(struct WorkStruct *work) {
    work->input_loaded00 = FALSE;
}

/* Time-dependent guards. */


/* Event execution. */

/**
 * Execute code for event "tau".
 *
 * @return Whether the event was performed.
 */
static BoolType ExecEvent0(SimStruct *sim_struct) {
    struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);
    int_T *modes = ssGetModeVector(sim_struct);
    real_T *cstate = ssGetContStates(sim_struct);


    if (((work->a1_x_) != (1)) && ((work->a1_x_) != (2))) {
        work->a1_x_ = 3;
    } else if (((work->a1_x_) != (2)) && ((work->a1_x_) != (3))) {
        work->a1_x_ = 4;
    }

    return TRUE;
}

#if PRINT_OUTPUT
static void PrintOutput(exprs_Event_ event, BoolType pre) {
}
#endif

/* {{{ mdlInitializeSizes, mdlInitializeSampleTimes */
static void mdlInitializeSizes(SimStruct *sim_struct) {
    /* Parameters. */
    ssSetNumSFcnParams(sim_struct, 0);
    if (ssGetNumSFcnParams(sim_struct) != ssGetSFcnParamsCount(sim_struct)) return;

    /* Inputs. */
    if (!ssSetNumInputPorts(sim_struct, 1)) return;

    ssSetInputPortWidth(sim_struct, 0, 1);

    int idx;
    for (idx = 0; idx < 1; idx++) {
        ssSetInputPortDataType(sim_struct, idx, SS_DOUBLE);
        ssSetInputPortComplexSignal(sim_struct, idx, COMPLEX_NO);
        ssSetInputPortDirectFeedThrough(sim_struct, idx, 1); /* Assume always feed-through. */
    }

    /* Outputs. */
    if (!ssSetNumOutputPorts(sim_struct, 149)) return;

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
    ssSetOutputPortWidth(sim_struct, 25, 1);
    ssSetOutputPortWidth(sim_struct, 26, 1);
    ssSetOutputPortWidth(sim_struct, 27, 1);
    ssSetOutputPortWidth(sim_struct, 28, 1);
    ssSetOutputPortWidth(sim_struct, 29, 1);
    ssSetOutputPortWidth(sim_struct, 30, 1);
    ssSetOutputPortWidth(sim_struct, 31, 1);
    ssSetOutputPortWidth(sim_struct, 32, 1);
    ssSetOutputPortWidth(sim_struct, 33, 1);
    ssSetOutputPortWidth(sim_struct, 34, 1);
    ssSetOutputPortWidth(sim_struct, 35, 1);
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
    ssSetOutputPortWidth(sim_struct, 51, 1);
    ssSetOutputPortWidth(sim_struct, 52, 1);
    ssSetOutputPortWidth(sim_struct, 53, 1);
    ssSetOutputPortWidth(sim_struct, 54, 1);
    ssSetOutputPortWidth(sim_struct, 55, 1);
    ssSetOutputPortWidth(sim_struct, 56, 1);
    ssSetOutputPortWidth(sim_struct, 57, 1);
    ssSetOutputPortWidth(sim_struct, 58, 1);
    ssSetOutputPortWidth(sim_struct, 59, 1);
    ssSetOutputPortWidth(sim_struct, 60, 1);
    ssSetOutputPortWidth(sim_struct, 61, 1);
    ssSetOutputPortWidth(sim_struct, 62, 1);
    ssSetOutputPortWidth(sim_struct, 63, 1);
    ssSetOutputPortWidth(sim_struct, 64, 1);
    ssSetOutputPortWidth(sim_struct, 65, 1);
    ssSetOutputPortWidth(sim_struct, 66, 1);
    ssSetOutputPortWidth(sim_struct, 67, 1);
    ssSetOutputPortWidth(sim_struct, 68, 1);
    ssSetOutputPortWidth(sim_struct, 69, 1);
    ssSetOutputPortWidth(sim_struct, 70, 1);
    ssSetOutputPortWidth(sim_struct, 71, 1);
    ssSetOutputPortWidth(sim_struct, 72, 1);
    ssSetOutputPortWidth(sim_struct, 73, 1);
    ssSetOutputPortWidth(sim_struct, 74, 2);
    ssSetOutputPortWidth(sim_struct, 75, 1);
    ssSetOutputPortWidth(sim_struct, 76, 1);
    ssSetOutputPortWidth(sim_struct, 77, 1);
    ssSetOutputPortWidth(sim_struct, 78, 1);
    ssSetOutputPortWidth(sim_struct, 79, 1);
    ssSetOutputPortWidth(sim_struct, 80, 1);
    ssSetOutputPortWidth(sim_struct, 81, 1);
    ssSetOutputPortWidth(sim_struct, 82, 1);
    ssSetOutputPortWidth(sim_struct, 83, 1);
    ssSetOutputPortWidth(sim_struct, 84, 1);
    ssSetOutputPortWidth(sim_struct, 85, 1);
    ssSetOutputPortWidth(sim_struct, 86, 1);
    ssSetOutputPortWidth(sim_struct, 87, 1);
    ssSetOutputPortWidth(sim_struct, 88, 1);
    ssSetOutputPortWidth(sim_struct, 89, 1);
    ssSetOutputPortWidth(sim_struct, 90, 1);
    ssSetOutputPortWidth(sim_struct, 91, 1);
    ssSetOutputPortWidth(sim_struct, 92, 1);
    ssSetOutputPortWidth(sim_struct, 93, 1);
    ssSetOutputPortWidth(sim_struct, 94, 1);
    ssSetOutputPortWidth(sim_struct, 95, 1);
    ssSetOutputPortWidth(sim_struct, 96, 1);
    ssSetOutputPortWidth(sim_struct, 97, 1);
    ssSetOutputPortWidth(sim_struct, 98, 1);
    ssSetOutputPortWidth(sim_struct, 99, 1);
    ssSetOutputPortWidth(sim_struct, 100, 1);
    ssSetOutputPortWidth(sim_struct, 101, 1);
    ssSetOutputPortWidth(sim_struct, 102, 1);
    ssSetOutputPortWidth(sim_struct, 103, 1);
    ssSetOutputPortWidth(sim_struct, 104, 1);
    ssSetOutputPortWidth(sim_struct, 105, 1);
    ssSetOutputPortWidth(sim_struct, 106, 1);
    ssSetOutputPortWidth(sim_struct, 107, 1);
    ssSetOutputPortWidth(sim_struct, 108, 1);
    ssSetOutputPortWidth(sim_struct, 109, 1);
    ssSetOutputPortWidth(sim_struct, 110, 1);
    ssSetOutputPortWidth(sim_struct, 111, 1);
    ssSetOutputPortWidth(sim_struct, 112, 1);
    ssSetOutputPortWidth(sim_struct, 113, 3);
    ssSetOutputPortWidth(sim_struct, 114, 3);
    ssSetOutputPortWidth(sim_struct, 115, 1);
    ssSetOutputPortWidth(sim_struct, 116, 1);
    ssSetOutputPortWidth(sim_struct, 117, 1);
    ssSetOutputPortWidth(sim_struct, 118, 1);
    ssSetOutputPortWidth(sim_struct, 119, 1);
    ssSetOutputPortWidth(sim_struct, 120, 1);
    ssSetOutputPortWidth(sim_struct, 121, 1);
    ssSetOutputPortWidth(sim_struct, 122, 1);
    ssSetOutputPortWidth(sim_struct, 123, 1);
    ssSetOutputPortWidth(sim_struct, 124, 1);
    ssSetOutputPortWidth(sim_struct, 125, 2);
    ssSetOutputPortWidth(sim_struct, 126, 1);
    ssSetOutputPortWidth(sim_struct, 127, 1);
    ssSetOutputPortWidth(sim_struct, 128, 1);
    ssSetOutputPortWidth(sim_struct, 129, 1);
    ssSetOutputPortWidth(sim_struct, 130, 1);
    ssSetOutputPortWidth(sim_struct, 131, 1);
    ssSetOutputPortWidth(sim_struct, 132, 1);
    ssSetOutputPortWidth(sim_struct, 133, 1);
    ssSetOutputPortWidth(sim_struct, 134, 1);
    ssSetOutputPortWidth(sim_struct, 135, 1);
    ssSetOutputPortWidth(sim_struct, 136, 1);
    ssSetOutputPortWidth(sim_struct, 137, 1);
    ssSetOutputPortWidth(sim_struct, 138, 1);
    ssSetOutputPortWidth(sim_struct, 139, 1);
    ssSetOutputPortWidth(sim_struct, 140, 1);
    ssSetOutputPortWidth(sim_struct, 141, 1);
    ssSetOutputPortWidth(sim_struct, 142, 1);
    ssSetOutputPortWidth(sim_struct, 143, 1);
    ssSetOutputPortWidth(sim_struct, 144, 1);
    ssSetOutputPortWidth(sim_struct, 145, 1);
    ssSetOutputPortWidth(sim_struct, 146, 1);
    ssSetOutputPortWidth(sim_struct, 147, 1);
    ssSetOutputPortWidth(sim_struct, 148, 1);

    for (idx = 0; idx < 149; idx++) {
        ssSetOutputPortDataType(sim_struct, idx, SS_DOUBLE);
        ssSetOutputPortComplexSignal(sim_struct, idx, COMPLEX_NO);
    }

    /* Disc state and cont state. */
    ssSetNumContStates(sim_struct, 2); /* CState[0] is time. */
    ssSetNumDiscStates(sim_struct, 0);

    /* Work vectors. */
    ssSetNumRWork(sim_struct, 0);
    ssSetNumIWork(sim_struct, 0);
    ssSetNumPWork(sim_struct, 1);

    /* Modes. */
    ssSetNumModes(sim_struct, 0);

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
    work->x1_ = 5;
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
    cstate[1] = 0.0;
    work->a1_x_ = 0;
    work->AA_vb_ = TRUE;
    work->AA_vi_ = 5;
    work->AA_vp_ = 2;
    work->AA_vn_ = -(1);
    work->AA_vz_ = 1;
    work->AA_vr_ = 1.23;
    StringTypeCopyText(&(work->AA_vs_), "a");
    work->AA_ve_ = _exprs_A;
    (work->AA_va_).data[0] = 1;
    (work->AA_va_).data[1] = 2;
    work->AA_v2_ = 5.0;
    work->AA_i2r_ = (RealType)(work->AA_vi_);
    BoolToString(work->AA_vb_, &(work->AA_b2s_));
    IntToString(work->AA_vi_, &(work->AA_i2s_));
    RealToString(work->AA_vr_, &(work->AA_r2s_));
    work->AA_s2b_ = StringToBool(&(work->AA_b2s_));
    work->AA_s2i_ = StringToInt(&(work->AA_i2s_));
    work->AA_s2r_ = StringToReal(&(work->AA_r2s_));
    (work->AA_self_cast1_).data[0] = 1;
    (work->AA_self_cast1_).data[1] = 2;
    (work->AA_self_cast1_).data[2] = 3;
    work->AA_self_cast2_ = work->AA_self_cast1_;
    work->AA_inv1_ = !(work->AA_vb_);
    work->AA_inv2_ = work->AA_vb_;
    work->AA_neg1_ = IntegerNegate(work->AA_vi_);
    work->AA_neg2_ = IntegerNegate(IntegerNegate(work->AA_vi_));
    work->AA_neg3_ = -(work->AA_vp_);
    work->AA_neg4_ = -(-(work->AA_vp_));
    work->AA_pos1_ = work->AA_vi_;
    work->AA_pos2_ = work->AA_vi_;
    work->AA_posneg_ = IntegerNegate(IntegerNegate(IntegerNegate(IntegerNegate(work->AA_vi_))));
    (work->AA_l3i_).data[0] = TRUE;
    work->AA_idx1_ = 1;
    work->AA_vt_ = TRUE;
    work->AA_vf_ = FALSE;
    work->AA_short_and_ = (work->AA_vf_) && (A1BTypeProject(&(work->AA_l3i_), work->AA_idx1_));
    work->AA_short_or_ = (work->AA_vt_) || (A1BTypeProject(&(work->AA_l3i_), work->AA_idx1_));
    work->AA_impl_ = !(work->AA_vb_) || (work->AA_vb_);
    work->AA_biimpl_ = (work->AA_vb_) == (work->AA_vb_);
    work->AA_conj_ = (work->AA_vb_) && (work->AA_vb_);
    work->AA_disj_ = (work->AA_vb_) || (work->AA_vb_);
    work->AA_lt1_ = (work->AA_vi_) < (work->AA_vi_);
    work->AA_le1_ = (work->AA_vi_) <= (work->AA_vi_);
    work->AA_gt1_ = (work->AA_vi_) > (work->AA_vi_);
    work->AA_ge1_ = (work->AA_vi_) >= (work->AA_vi_);
    work->AA_lt2_ = (work->AA_vi_) < (work->AA_vr_);
    work->AA_le2_ = (work->AA_vi_) <= (work->AA_vr_);
    work->AA_gt2_ = (work->AA_vi_) > (work->AA_vr_);
    work->AA_ge2_ = (work->AA_vi_) >= (work->AA_vr_);
    work->AA_lt3_ = (work->AA_vr_) < (work->AA_vr_);
    work->AA_le3_ = (work->AA_vr_) <= (work->AA_vr_);
    work->AA_gt3_ = (work->AA_vr_) > (work->AA_vr_);
    work->AA_ge3_ = (work->AA_vr_) >= (work->AA_vr_);
    work->AA_lt4_ = (work->AA_vr_) < (work->AA_vr_);
    work->AA_le4_ = (work->AA_vr_) <= (work->AA_vr_);
    work->AA_gt4_ = (work->AA_vr_) > (work->AA_vr_);
    work->AA_ge4_ = (work->AA_vr_) >= (work->AA_vr_);
    work->AA_eq1_ = (work->AA_vb_) == (work->AA_vb_);
    work->AA_eq2_ = (work->AA_vi_) == (work->AA_vi_);
    work->AA_eq3_ = (work->AA_vr_) == (work->AA_vr_);
    work->AA_eq4_ = StringTypeEquals(&(work->AA_vs_), &(work->AA_vs_));
    work->AA_eq5_ = (work->AA_ve_) == (work->AA_ve_);
    work->AA_ne1_ = (work->AA_vb_) != (work->AA_vb_);
    work->AA_ne2_ = (work->AA_vi_) != (work->AA_vi_);
    work->AA_ne3_ = (work->AA_vr_) != (work->AA_vr_);
    work->AA_ne4_ = !StringTypeEquals(&(work->AA_vs_), &(work->AA_vs_));
    work->AA_ne5_ = (work->AA_ve_ != work->AA_ve_);
    work->AA_add1_ = IntegerAdd(work->AA_vi_, work->AA_vi_);
    work->AA_add2_ = RealAdd(work->AA_vi_, work->AA_vr_);
    work->AA_add3_ = RealAdd(work->AA_vr_, work->AA_vi_);
    work->AA_add4_ = RealAdd(work->AA_vr_, work->AA_vr_);
    StringTypeConcat(&(work->AA_add5_), &(work->AA_vs_), &(work->AA_vs_));
    work->AA_add6_ = (work->AA_vp_) + (work->AA_vp_);
    work->AA_add7_ = IntegerAdd(work->AA_vi_, work->AA_vp_);
    work->AA_add8_ = IntegerAdd(work->AA_vp_, work->AA_vi_);
    work->AA_sub1_ = IntegerSubtract(work->AA_vi_, work->AA_vi_);
    work->AA_sub2_ = RealSubtract(work->AA_vi_, work->AA_vr_);
    work->AA_sub3_ = RealSubtract(work->AA_vr_, work->AA_vi_);
    work->AA_sub4_ = RealSubtract(work->AA_vr_, work->AA_vr_);
    work->AA_sub5_ = (work->AA_vp_) - (work->AA_vp_);
    work->AA_sub6_ = IntegerSubtract(work->AA_vi_, work->AA_vp_);
    work->AA_sub7_ = IntegerSubtract(work->AA_vp_, work->AA_vi_);
    work->AA_mul1_ = IntegerMultiply(work->AA_vi_, work->AA_vi_);
    work->AA_mul2_ = RealMultiply(work->AA_vi_, work->AA_vr_);
    work->AA_mul3_ = RealMultiply(work->AA_vr_, work->AA_vi_);
    work->AA_mul4_ = RealMultiply(work->AA_vr_, work->AA_vr_);
    work->AA_mul5_ = (work->AA_vp_) * (work->AA_vp_);
    work->AA_mul6_ = IntegerMultiply(work->AA_vi_, work->AA_vp_);
    work->AA_mul7_ = IntegerMultiply(work->AA_vp_, work->AA_vi_);
    work->AA_rdiv1_ = RealDivision(work->AA_vi_, work->AA_vi_);
    work->AA_rdiv2_ = RealDivision(work->AA_vi_, work->AA_vr_);
    work->AA_rdiv3_ = RealDivision(work->AA_vr_, work->AA_vi_);
    work->AA_rdiv4_ = RealDivision(work->AA_vr_, work->AA_vr_);
    work->AA_rdiv5_ = (double)(work->AA_vi_) / (work->AA_vp_);
    work->AA_rdiv6_ = (double)(work->AA_vi_) / (work->AA_vn_);
    work->AA_div1_ = IntegerDiv(work->AA_vi_, work->AA_vi_);
    work->AA_div2_ = (work->AA_vi_) / (work->AA_vp_);
    work->AA_div3_ = IntegerDiv(work->AA_vi_, work->AA_vn_);
    work->AA_div4_ = IntegerDiv(work->AA_vi_, work->AA_vz_);
    work->AA_mod1_ = IntegerMod(work->AA_vi_, work->AA_vi_);
    work->AA_mod2_ = (work->AA_vi_) % (work->AA_vp_);
    (work->AA_li_).data[0] = 1;
    (work->AA_li_).data[1] = 2;
    (work->AA_tii_)._field0 = 1;
    (work->AA_tii_)._field1 = 2;
    StringTypeCopyText(&(work->AA_ss_), "abc");
    work->AA_proj1_ = A2ITypeProject(&(work->AA_li_), 0);
    work->AA_proj2_ = A2ITypeProject(&(work->AA_li_), -(1));
    work->AA_proj3_ = (work->AA_tii_)._field0;
    work->AA_proj4_ = (work->AA_tii_)._field1;
    StringTypeProject(&(work->AA_proj5_), &(work->AA_ss_), 0);
    StringTypeProject(&(work->AA_proj6_), &(work->AA_ss_), -(1));
    work->AA_f_acos_ = RealAcos(work->AA_vr_);
    work->AA_f_asin_ = RealAsin(work->AA_vr_);
    work->AA_f_atan_ = RealAtan(work->AA_vr_);
    work->AA_f_cos_ = RealCos(work->AA_vr_);
    work->AA_f_sin_ = RealSin(work->AA_vr_);
    work->AA_f_tan_ = RealTan(work->AA_vr_);
    work->AA_f_abs1_ = IntegerAbs(work->AA_vi_);
    work->AA_f_abs12_ = IntegerAbs(work->AA_vp_);
    work->AA_f_abs2_ = RealAbs(work->AA_vr_);
    work->AA_f_cbrt_ = RealCbrt(work->AA_vr_);
    work->AA_f_ceil_ = CeilFunction(work->AA_vr_);
    work->AA_f_empty_ = FALSE;
    work->AA_f_exp_ = RealExp(work->AA_vr_);
    work->AA_f_floor_ = FloorFunction(work->AA_vr_);
    work->AA_f_ln_ = RealLn(work->AA_vr_);
    work->AA_f_log_ = RealLog(work->AA_vr_);
    work->AA_f_max1_ = IntegerMax(work->AA_vi_, work->AA_vi_);
    work->AA_f_max2_ = RealMax(work->AA_vi_, work->AA_vr_);
    work->AA_f_max3_ = RealMax(work->AA_vr_, work->AA_vi_);
    work->AA_f_max4_ = RealMax(work->AA_vr_, work->AA_vr_);
    work->AA_f_min1_ = IntegerMin(work->AA_vi_, work->AA_vi_);
    work->AA_f_min2_ = RealMin(work->AA_vi_, work->AA_vr_);
    work->AA_f_min3_ = RealMin(work->AA_vr_, work->AA_vi_);
    work->AA_f_min4_ = RealMin(work->AA_vr_, work->AA_vr_);
    work->AA_f_pow1_ = RealMax(work->AA_vi_, work->AA_vi_);
    work->AA_f_pow12_ = IntegerPower(work->AA_vp_, work->AA_vp_);
    work->AA_f_pow2_ = RealMax(work->AA_vi_, work->AA_vr_);
    work->AA_f_pow3_ = RealMax(work->AA_vr_, work->AA_vi_);
    work->AA_f_pow4_ = RealMax(work->AA_vr_, work->AA_vr_);
    work->AA_f_round_ = RoundFunction(work->AA_vr_);
    work->AA_f_scale_ = ScaleFunction(work->AA_vr_, 0, 10, 1, 11);
    work->AA_f_sign1_ = IntegerSign(work->AA_vi_);
    work->AA_f_sign2_ = IntegerSign(work->AA_vr_);
    work->AA_f_size1_ = 2;
    work->AA_f_size2_ = StringTypeSize(&(work->AA_vs_));
    work->AA_f_sqrt_ = RealSqrt(work->AA_vr_);
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
    ClearInputFlags(work);
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
    ClearInputFlags(work);
    real_T *derivs = ssGetdX(sim_struct);

    derivs[0] = 1.0;
    derivs[1] = deriv01(sim_struct);
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
    *y = RealToSimulink(cstate[1]);

    y = ssGetOutputPortSignal(sim_struct, 1);
    *y = IntToSimulink(work->AA_add1_);

    y = ssGetOutputPortSignal(sim_struct, 2);
    *y = RealToSimulink(work->AA_add2_);

    y = ssGetOutputPortSignal(sim_struct, 3);
    *y = RealToSimulink(work->AA_add3_);

    y = ssGetOutputPortSignal(sim_struct, 4);
    *y = RealToSimulink(work->AA_add4_);

    y = ssGetOutputPortSignal(sim_struct, 5);
    *y = IntToSimulink(work->AA_add6_);

    y = ssGetOutputPortSignal(sim_struct, 6);
    *y = IntToSimulink(work->AA_add7_);

    y = ssGetOutputPortSignal(sim_struct, 7);
    *y = IntToSimulink(work->AA_add8_);

    y = ssGetOutputPortSignal(sim_struct, 8);
    *y = BoolToSimulink(work->AA_biimpl_);

    y = ssGetOutputPortSignal(sim_struct, 9);
    *y = BoolToSimulink(work->AA_conj_);

    y = ssGetOutputPortSignal(sim_struct, 10);
    *y = BoolToSimulink(work->AA_disj_);

    y = ssGetOutputPortSignal(sim_struct, 11);
    *y = IntToSimulink(work->AA_div1_);

    y = ssGetOutputPortSignal(sim_struct, 12);
    *y = IntToSimulink(work->AA_div2_);

    y = ssGetOutputPortSignal(sim_struct, 13);
    *y = IntToSimulink(work->AA_div3_);

    y = ssGetOutputPortSignal(sim_struct, 14);
    *y = IntToSimulink(work->AA_div4_);

    y = ssGetOutputPortSignal(sim_struct, 15);
    *y = BoolToSimulink(work->AA_eq1_);

    y = ssGetOutputPortSignal(sim_struct, 16);
    *y = BoolToSimulink(work->AA_eq2_);

    y = ssGetOutputPortSignal(sim_struct, 17);
    *y = BoolToSimulink(work->AA_eq3_);

    y = ssGetOutputPortSignal(sim_struct, 18);
    *y = BoolToSimulink(work->AA_eq4_);

    y = ssGetOutputPortSignal(sim_struct, 19);
    *y = BoolToSimulink(work->AA_eq5_);

    y = ssGetOutputPortSignal(sim_struct, 20);
    *y = IntToSimulink(work->AA_f_abs1_);

    y = ssGetOutputPortSignal(sim_struct, 21);
    *y = IntToSimulink(work->AA_f_abs12_);

    y = ssGetOutputPortSignal(sim_struct, 22);
    *y = RealToSimulink(work->AA_f_abs2_);

    y = ssGetOutputPortSignal(sim_struct, 23);
    *y = RealToSimulink(work->AA_f_acos_);

    y = ssGetOutputPortSignal(sim_struct, 24);
    *y = RealToSimulink(work->AA_f_asin_);

    y = ssGetOutputPortSignal(sim_struct, 25);
    *y = RealToSimulink(work->AA_f_atan_);

    y = ssGetOutputPortSignal(sim_struct, 26);
    *y = RealToSimulink(work->AA_f_cbrt_);

    y = ssGetOutputPortSignal(sim_struct, 27);
    *y = IntToSimulink(work->AA_f_ceil_);

    y = ssGetOutputPortSignal(sim_struct, 28);
    *y = RealToSimulink(work->AA_f_cos_);

    y = ssGetOutputPortSignal(sim_struct, 29);
    *y = BoolToSimulink(work->AA_f_empty_);

    y = ssGetOutputPortSignal(sim_struct, 30);
    *y = RealToSimulink(work->AA_f_exp_);

    y = ssGetOutputPortSignal(sim_struct, 31);
    *y = IntToSimulink(work->AA_f_floor_);

    y = ssGetOutputPortSignal(sim_struct, 32);
    *y = RealToSimulink(work->AA_f_ln_);

    y = ssGetOutputPortSignal(sim_struct, 33);
    *y = RealToSimulink(work->AA_f_log_);

    y = ssGetOutputPortSignal(sim_struct, 34);
    *y = IntToSimulink(work->AA_f_max1_);

    y = ssGetOutputPortSignal(sim_struct, 35);
    *y = RealToSimulink(work->AA_f_max2_);

    y = ssGetOutputPortSignal(sim_struct, 36);
    *y = RealToSimulink(work->AA_f_max3_);

    y = ssGetOutputPortSignal(sim_struct, 37);
    *y = RealToSimulink(work->AA_f_max4_);

    y = ssGetOutputPortSignal(sim_struct, 38);
    *y = IntToSimulink(work->AA_f_min1_);

    y = ssGetOutputPortSignal(sim_struct, 39);
    *y = RealToSimulink(work->AA_f_min2_);

    y = ssGetOutputPortSignal(sim_struct, 40);
    *y = RealToSimulink(work->AA_f_min3_);

    y = ssGetOutputPortSignal(sim_struct, 41);
    *y = RealToSimulink(work->AA_f_min4_);

    y = ssGetOutputPortSignal(sim_struct, 42);
    *y = RealToSimulink(work->AA_f_pow1_);

    y = ssGetOutputPortSignal(sim_struct, 43);
    *y = IntToSimulink(work->AA_f_pow12_);

    y = ssGetOutputPortSignal(sim_struct, 44);
    *y = RealToSimulink(work->AA_f_pow2_);

    y = ssGetOutputPortSignal(sim_struct, 45);
    *y = RealToSimulink(work->AA_f_pow3_);

    y = ssGetOutputPortSignal(sim_struct, 46);
    *y = RealToSimulink(work->AA_f_pow4_);

    y = ssGetOutputPortSignal(sim_struct, 47);
    *y = IntToSimulink(work->AA_f_round_);

    y = ssGetOutputPortSignal(sim_struct, 48);
    *y = RealToSimulink(work->AA_f_scale_);

    y = ssGetOutputPortSignal(sim_struct, 49);
    *y = IntToSimulink(work->AA_f_sign1_);

    y = ssGetOutputPortSignal(sim_struct, 50);
    *y = IntToSimulink(work->AA_f_sign2_);

    y = ssGetOutputPortSignal(sim_struct, 51);
    *y = RealToSimulink(work->AA_f_sin_);

    y = ssGetOutputPortSignal(sim_struct, 52);
    *y = IntToSimulink(work->AA_f_size1_);

    y = ssGetOutputPortSignal(sim_struct, 53);
    *y = IntToSimulink(work->AA_f_size2_);

    y = ssGetOutputPortSignal(sim_struct, 54);
    *y = RealToSimulink(work->AA_f_sqrt_);

    y = ssGetOutputPortSignal(sim_struct, 55);
    *y = RealToSimulink(work->AA_f_tan_);

    y = ssGetOutputPortSignal(sim_struct, 56);
    *y = BoolToSimulink(work->AA_ge1_);

    y = ssGetOutputPortSignal(sim_struct, 57);
    *y = BoolToSimulink(work->AA_ge2_);

    y = ssGetOutputPortSignal(sim_struct, 58);
    *y = BoolToSimulink(work->AA_ge3_);

    y = ssGetOutputPortSignal(sim_struct, 59);
    *y = BoolToSimulink(work->AA_ge4_);

    y = ssGetOutputPortSignal(sim_struct, 60);
    *y = BoolToSimulink(work->AA_gt1_);

    y = ssGetOutputPortSignal(sim_struct, 61);
    *y = BoolToSimulink(work->AA_gt2_);

    y = ssGetOutputPortSignal(sim_struct, 62);
    *y = BoolToSimulink(work->AA_gt3_);

    y = ssGetOutputPortSignal(sim_struct, 63);
    *y = BoolToSimulink(work->AA_gt4_);

    y = ssGetOutputPortSignal(sim_struct, 64);
    *y = RealToSimulink(work->AA_i2r_);

    y = ssGetOutputPortSignal(sim_struct, 65);
    *y = IntToSimulink(work->AA_idx1_);

    y = ssGetOutputPortSignal(sim_struct, 66);
    *y = BoolToSimulink(work->AA_impl_);

    y = ssGetOutputPortSignal(sim_struct, 67);
    *y = BoolToSimulink(work->AA_inv1_);

    y = ssGetOutputPortSignal(sim_struct, 68);
    *y = BoolToSimulink(work->AA_inv2_);

    y = ssGetOutputPortSignal(sim_struct, 69);
    A1BTypeToSimulink(y, &work->AA_l3i_);

    y = ssGetOutputPortSignal(sim_struct, 70);
    *y = BoolToSimulink(work->AA_le1_);

    y = ssGetOutputPortSignal(sim_struct, 71);
    *y = BoolToSimulink(work->AA_le2_);

    y = ssGetOutputPortSignal(sim_struct, 72);
    *y = BoolToSimulink(work->AA_le3_);

    y = ssGetOutputPortSignal(sim_struct, 73);
    *y = BoolToSimulink(work->AA_le4_);

    y = ssGetOutputPortSignal(sim_struct, 74);
    A2ITypeToSimulink(y, &work->AA_li_);

    y = ssGetOutputPortSignal(sim_struct, 75);
    *y = BoolToSimulink(work->AA_lt1_);

    y = ssGetOutputPortSignal(sim_struct, 76);
    *y = BoolToSimulink(work->AA_lt2_);

    y = ssGetOutputPortSignal(sim_struct, 77);
    *y = BoolToSimulink(work->AA_lt3_);

    y = ssGetOutputPortSignal(sim_struct, 78);
    *y = BoolToSimulink(work->AA_lt4_);

    y = ssGetOutputPortSignal(sim_struct, 79);
    *y = IntToSimulink(work->AA_mod1_);

    y = ssGetOutputPortSignal(sim_struct, 80);
    *y = IntToSimulink(work->AA_mod2_);

    y = ssGetOutputPortSignal(sim_struct, 81);
    *y = IntToSimulink(work->AA_mul1_);

    y = ssGetOutputPortSignal(sim_struct, 82);
    *y = RealToSimulink(work->AA_mul2_);

    y = ssGetOutputPortSignal(sim_struct, 83);
    *y = RealToSimulink(work->AA_mul3_);

    y = ssGetOutputPortSignal(sim_struct, 84);
    *y = RealToSimulink(work->AA_mul4_);

    y = ssGetOutputPortSignal(sim_struct, 85);
    *y = IntToSimulink(work->AA_mul5_);

    y = ssGetOutputPortSignal(sim_struct, 86);
    *y = IntToSimulink(work->AA_mul6_);

    y = ssGetOutputPortSignal(sim_struct, 87);
    *y = IntToSimulink(work->AA_mul7_);

    y = ssGetOutputPortSignal(sim_struct, 88);
    *y = BoolToSimulink(work->AA_ne1_);

    y = ssGetOutputPortSignal(sim_struct, 89);
    *y = BoolToSimulink(work->AA_ne2_);

    y = ssGetOutputPortSignal(sim_struct, 90);
    *y = BoolToSimulink(work->AA_ne3_);

    y = ssGetOutputPortSignal(sim_struct, 91);
    *y = BoolToSimulink(work->AA_ne4_);

    y = ssGetOutputPortSignal(sim_struct, 92);
    *y = BoolToSimulink(work->AA_ne5_);

    y = ssGetOutputPortSignal(sim_struct, 93);
    *y = IntToSimulink(work->AA_neg1_);

    y = ssGetOutputPortSignal(sim_struct, 94);
    *y = IntToSimulink(work->AA_neg2_);

    y = ssGetOutputPortSignal(sim_struct, 95);
    *y = IntToSimulink(work->AA_neg3_);

    y = ssGetOutputPortSignal(sim_struct, 96);
    *y = IntToSimulink(work->AA_neg4_);

    y = ssGetOutputPortSignal(sim_struct, 97);
    *y = IntToSimulink(work->AA_pos1_);

    y = ssGetOutputPortSignal(sim_struct, 98);
    *y = IntToSimulink(work->AA_pos2_);

    y = ssGetOutputPortSignal(sim_struct, 99);
    *y = IntToSimulink(work->AA_posneg_);

    y = ssGetOutputPortSignal(sim_struct, 100);
    *y = IntToSimulink(work->AA_proj1_);

    y = ssGetOutputPortSignal(sim_struct, 101);
    *y = IntToSimulink(work->AA_proj2_);

    y = ssGetOutputPortSignal(sim_struct, 102);
    *y = IntToSimulink(work->AA_proj3_);

    y = ssGetOutputPortSignal(sim_struct, 103);
    *y = IntToSimulink(work->AA_proj4_);

    y = ssGetOutputPortSignal(sim_struct, 104);
    *y = RealToSimulink(work->AA_rdiv1_);

    y = ssGetOutputPortSignal(sim_struct, 105);
    *y = RealToSimulink(work->AA_rdiv2_);

    y = ssGetOutputPortSignal(sim_struct, 106);
    *y = RealToSimulink(work->AA_rdiv3_);

    y = ssGetOutputPortSignal(sim_struct, 107);
    *y = RealToSimulink(work->AA_rdiv4_);

    y = ssGetOutputPortSignal(sim_struct, 108);
    *y = RealToSimulink(work->AA_rdiv5_);

    y = ssGetOutputPortSignal(sim_struct, 109);
    *y = RealToSimulink(work->AA_rdiv6_);

    y = ssGetOutputPortSignal(sim_struct, 110);
    *y = BoolToSimulink(work->AA_s2b_);

    y = ssGetOutputPortSignal(sim_struct, 111);
    *y = IntToSimulink(work->AA_s2i_);

    y = ssGetOutputPortSignal(sim_struct, 112);
    *y = RealToSimulink(work->AA_s2r_);

    y = ssGetOutputPortSignal(sim_struct, 113);
    A3ITypeToSimulink(y, &work->AA_self_cast1_);

    y = ssGetOutputPortSignal(sim_struct, 114);
    A3ITypeToSimulink(y, &work->AA_self_cast2_);

    y = ssGetOutputPortSignal(sim_struct, 115);
    *y = BoolToSimulink(work->AA_short_and_);

    y = ssGetOutputPortSignal(sim_struct, 116);
    *y = BoolToSimulink(work->AA_short_or_);

    y = ssGetOutputPortSignal(sim_struct, 117);
    *y = IntToSimulink(work->AA_sub1_);

    y = ssGetOutputPortSignal(sim_struct, 118);
    *y = RealToSimulink(work->AA_sub2_);

    y = ssGetOutputPortSignal(sim_struct, 119);
    *y = RealToSimulink(work->AA_sub3_);

    y = ssGetOutputPortSignal(sim_struct, 120);
    *y = RealToSimulink(work->AA_sub4_);

    y = ssGetOutputPortSignal(sim_struct, 121);
    *y = IntToSimulink(work->AA_sub5_);

    y = ssGetOutputPortSignal(sim_struct, 122);
    *y = IntToSimulink(work->AA_sub6_);

    y = ssGetOutputPortSignal(sim_struct, 123);
    *y = IntToSimulink(work->AA_sub7_);

    y = ssGetOutputPortSignal(sim_struct, 124);
    *y = RealToSimulink(work->AA_v2_);

    y = ssGetOutputPortSignal(sim_struct, 125);
    A2ITypeToSimulink(y, &work->AA_va_);

    y = ssGetOutputPortSignal(sim_struct, 126);
    *y = BoolToSimulink(work->AA_vb_);

    y = ssGetOutputPortSignal(sim_struct, 127);
    *y = IntToSimulink(work->AA_ve_);

    y = ssGetOutputPortSignal(sim_struct, 128);
    *y = BoolToSimulink(work->AA_vf_);

    y = ssGetOutputPortSignal(sim_struct, 129);
    *y = IntToSimulink(work->AA_vi_);

    y = ssGetOutputPortSignal(sim_struct, 130);
    *y = IntToSimulink(work->AA_vn_);

    y = ssGetOutputPortSignal(sim_struct, 131);
    *y = IntToSimulink(work->AA_vp_);

    y = ssGetOutputPortSignal(sim_struct, 132);
    *y = RealToSimulink(work->AA_vr_);

    y = ssGetOutputPortSignal(sim_struct, 133);
    *y = BoolToSimulink(work->AA_vt_);

    y = ssGetOutputPortSignal(sim_struct, 134);
    *y = IntToSimulink(work->AA_vz_);

    y = ssGetOutputPortSignal(sim_struct, 135);
    *y = IntToSimulink(work->a1_x_);

    y = ssGetOutputPortSignal(sim_struct, 136);
    *y = IntToSimulink(fcall1_(sim_struct));

    y = ssGetOutputPortSignal(sim_struct, 137);
    *y = IntToSimulink(fcall2_(sim_struct));

    y = ssGetOutputPortSignal(sim_struct, 138);
    *y = IntToSimulink(if1_(sim_struct));

    y = ssGetOutputPortSignal(sim_struct, 139);
    *y = IntToSimulink(if2_(sim_struct));

    y = ssGetOutputPortSignal(sim_struct, 140);
    *y = IntToSimulink(if3_(sim_struct));

    y = ssGetOutputPortSignal(sim_struct, 141);
    *y = IntToSimulink(v1_(sim_struct));

    y = ssGetOutputPortSignal(sim_struct, 142);
    *y = IntToSimulink(vea_(sim_struct));

    y = ssGetOutputPortSignal(sim_struct, 143);
    *y = IntToSimulink(x2_(sim_struct));

    y = ssGetOutputPortSignal(sim_struct, 144);
    *y = IntToSimulink(x3_(sim_struct));

    y = ssGetOutputPortSignal(sim_struct, 145);
    *y = IntToSimulink(x4_(sim_struct));

    y = ssGetOutputPortSignal(sim_struct, 146);
    *y = RealToSimulink(x6_(sim_struct));

    y = ssGetOutputPortSignal(sim_struct, 147);
    *y = BoolToSimulink(x7_(sim_struct));

    y = ssGetOutputPortSignal(sim_struct, 148);
    *y = IntToSimulink(x9_(sim_struct));
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
        if (ExecEvent0(sim_struct)) continue;  /* (Try to) perform event "tau". */

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

