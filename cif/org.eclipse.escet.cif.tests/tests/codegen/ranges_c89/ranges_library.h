/**
 * Headers and functions of the runtime support for the CIF to C89 translation.
 */

#ifndef CIF_C_RANGES_CODEGEN_LIBRARY_H
#define CIF_C_RANGES_CODEGEN_LIBRARY_H

/**
 * Support library for the CIF to C code generation.
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
 * - The library does not attempt to provide nice error messages, since a crash
 *   is inevitable, and the crash location gives sufficient information what is
 *   wrong. 'assert' is used overflow/underflow/zero-division detection.
 */

#include <string.h>
#include <math.h>
#include <limits.h>
#include <assert.h>

/* Define max string length if not defined. */
#ifndef MAX_STRING_SIZE
#define MAX_STRING_SIZE 128
#endif

typedef int BoolType;
typedef int IntType;
typedef double RealType;

#define TRUE 1
#define FALSE 0
#define MaxInt INT_MAX
#define MinInt INT_MIN

/** String data type. */
struct StringTypeStruct {
    char data[MAX_STRING_SIZE];
};
typedef struct StringTypeStruct StringType;

/* String type support runtime code. */
void StringTypeCopyText(StringType *s, const char *text);
void StringTypeConcat(StringType *dest, StringType *left, StringType *right);
IntType StringTypeSize(StringType *str);
BoolType StringTypeEquals(StringType *left, StringType *right);
void StringTypeProject(StringType *dst, StringType *src, IntType index);

/* Cast functions. */
BoolType StringToBool(StringType *s);
IntType  StringToInt(StringType *s);
RealType StringToReal(StringType *s);

void BoolToString(BoolType b, StringType *s);
void  IntToString(IntType i, StringType *s);
void RealToString(RealType r, StringType *s);

/* Format functions. */
enum FormatFlags {
    FMTFLAGS_NONE   = 0,
    FMTFLAGS_LEFT   = 1 << 0,
    FMTFLAGS_SIGN   = 1 << 1,
    FMTFLAGS_SPACE  = 1 << 2,
    FMTFLAGS_ZEROES = 1 << 3,
    FMTFLAGS_GROUPS = 1 << 4,
};

int StringTypeAppendText(StringType *s, int end, int flags, int width, const char *text);
int BoolTypePrint(BoolType b, char *dest, int start, int end);
int IntTypePrint(IntType i, char *dest, int start, int end);
int RealTypePrint(RealType r, char *dest, int start, int end);
int StringTypePrintRaw(StringType *s, char *dest, int start, int end);
int StringTypePrintEscaped(StringType *s, char *dest, int start, int end);

/* Standard operations for integers. */
IntType IntegerAbs(IntType value);
IntType IntegerDiv(IntType a, IntType b);
IntType IntegerMod(IntType a, IntType b);
IntType IntegerAdd(IntType a, IntType b);
IntType IntegerSubtract(IntType a, IntType b);
IntType IntegerMax(IntType a, IntType b);
IntType IntegerMin(IntType a, IntType b);
IntType IntegerMultiply(IntType a, IntType b);
IntType IntegerNegate(IntType a);
IntType IntegerSign(IntType value);
IntType RealSign(RealType value);
IntType IntegerSign(IntType value);
IntType RealSign(RealType value);

/* Standard operations for reals. */
RealType RealAbs(RealType value);
RealType RealAdd(RealType a, RealType b);
RealType RealSubtract(RealType a, RealType b);
RealType RealDivision(RealType a, RealType b);
RealType RealMax(RealType a, RealType b);
RealType RealMin(RealType a, RealType b);
RealType RealMultiply(RealType a, RealType b);
RealType RealNegate(RealType a);

/* Real to int conversions. */
IntType CeilFunction(RealType value);
IntType FloorFunction(RealType a);
IntType RoundFunction(RealType value);
IntType IntegerPower(IntType x, IntType y);

RealType ScaleFunction(RealType val, RealType inmin, RealType inmax,
                       RealType outmin, RealType outmax);

/* Standard library math functions. */
RealType RealAcos(RealType arg);
RealType RealAsin(RealType arg);
RealType RealAtan(RealType arg);

RealType RealCos(RealType arg);
RealType RealSin(RealType arg);
RealType RealTan(RealType arg);

RealType RealExp(RealType arg);
RealType RealLog(RealType arg);
RealType RealLn(RealType arg);

RealType RealSqrt(RealType a);
RealType RealCbrt(RealType a);

#endif

