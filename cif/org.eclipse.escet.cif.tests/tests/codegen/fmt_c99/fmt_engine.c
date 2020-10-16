/* CIF to C translation of fmt.cif
 * Generated file, DO NOT EDIT
 */

#include <stdio.h>
#include <stdlib.h>
#include "fmt_engine.h"

#ifndef MAX_NUM_EVENTS
#define MAX_NUM_EVENTS 1000
#endif

/* What to do if a range error is found in an assignment? */
#ifdef KEEP_RUNNING
static inline void RangeErrorDetected(void) { /* Do nothing, error is already reported. */ }
#else
static inline void RangeErrorDetected(void) { exit(1); }
#endif

/* Type support code. */
int EnumTypePrint(fmtEnum value, char *dest, int start, int end) {
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
BoolType A1STypeEquals(A1SType *left, A1SType *right) {
    if (left == right) return TRUE;
    int i;
    for (i = 0; i < 1; i++) {
        if (!(StringTypeEquals(&(left->data[i]), &(right->data[i])))) return FALSE;
    }
    return TRUE;
}

/**
 * Extract an element from the array.
 * @param array Array with value to retrieve.
 * @param index Element index in the array (not normalized).
 * @return Element value at the indicated index from the array.
 */
StringType *A1STypeProject(A1SType *array, IntType index) {
    if (index < 0) index += 1; /* Normalize index. */
    assert(index >= 0 && index < 1);

    return &array->data[index];
}

/**
 * In-place change of the array.
 * @param array Array to modify.
 * @param index Element index in the array (not normalized).
 * @param value New value to copy into the array.
 */
void A1STypeModify(A1SType *array, IntType index, StringType *value) {
    if (index < 0) index += 1; /* Normalize index. */
    assert(index >= 0 && index < 1);

    memcpy(&array->data[index], value, sizeof(StringType));
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
int A1STypePrint(A1SType *array, char *dest, int start, int end) {
    int last = end - 1;
    if (start < last) { dest[start++] = '['; }
    int index;
    for (index = 0; index < 1; index++) {
        if (index > 0) {
            if (start < last) { dest[start++] = ','; }
            if (start < last) { dest[start++] = ' '; }
        }
        start = StringTypePrintEscaped(&array->data[index], dest, start, end);
    }
    if (start < last) { dest[start++] = ']'; }
    dest[start] = '\0';
    return start;
}

/**
 * Compare two arrays for equality.
 * @param left First array to compare.
 * @param right Second array to compare.
 * @return Whether both arrays are the same.
 */
BoolType A1ITypeEquals(A1IType *left, A1IType *right) {
    if (left == right) return TRUE;
    return memcmp(left, right, sizeof(A1IType)) == 0;
}

/**
 * Extract an element from the array.
 * @param array Array with value to retrieve.
 * @param index Element index in the array (not normalized).
 * @return Element value at the indicated index from the array.
 */
IntType A1ITypeProject(A1IType *array, IntType index) {
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
void A1ITypeModify(A1IType *array, IntType index, IntType value) {
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
int A1ITypePrint(A1IType *array, char *dest, int start, int end) {
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
 * Compare two tuples for equality.
 * @param left First tuple to compare.
 * @param right Second tuple to compare.
 * @return Whether both tuples are the same.
 */
BoolType T2IA1ITypeEquals(T2IA1IType *left, T2IA1IType *right) {
    if (left == right) return TRUE;
    if (memcmp(&left->_field0, &right->_field0, sizeof(IntType)) != 0) return FALSE;
    if (memcmp(&left->_field1, &right->_field1, sizeof(A1IType)) != 0) return FALSE;
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
int T2IA1ITypePrint(T2IA1IType *tuple, char *dest, int start, int end) {
    int last = end - 1;
    if (start < last) { dest[start++] = '('; }
    start = IntTypePrint(tuple->_field0, dest, start, end);
    if (start < last) { dest[start++] = ','; }
    if (start < last) { dest[start++] = ' '; }
    start = A1ITypePrint(&tuple->_field1, dest, start, end);
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
BoolType A2STypeEquals(A2SType *left, A2SType *right) {
    if (left == right) return TRUE;
    int i;
    for (i = 0; i < 2; i++) {
        if (!(StringTypeEquals(&(left->data[i]), &(right->data[i])))) return FALSE;
    }
    return TRUE;
}

/**
 * Extract an element from the array.
 * @param array Array with value to retrieve.
 * @param index Element index in the array (not normalized).
 * @return Element value at the indicated index from the array.
 */
StringType *A2STypeProject(A2SType *array, IntType index) {
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
void A2STypeModify(A2SType *array, IntType index, StringType *value) {
    if (index < 0) index += 2; /* Normalize index. */
    assert(index >= 0 && index < 2);

    memcpy(&array->data[index], value, sizeof(StringType));
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
int A2STypePrint(A2SType *array, char *dest, int start, int end) {
    int last = end - 1;
    if (start < last) { dest[start++] = '['; }
    int index;
    for (index = 0; index < 2; index++) {
        if (index > 0) {
            if (start < last) { dest[start++] = ','; }
            if (start < last) { dest[start++] = ' '; }
        }
        start = StringTypePrintEscaped(&array->data[index], dest, start, end);
    }
    if (start < last) { dest[start++] = ']'; }
    dest[start] = '\0';
    return start;
}


/** Event names. */
const char *fmt_event_names[] = {
    "initial-step", /**< Initial step. */
    "delay-step",   /**< Delay step. */
    "tau",          /**< Tau step. */
};

/** Enumeration names. */
const char *enum_names[] = {
    "A",
    "B",
    "X",
};

/* Constants. */


/* Functions. */


/* Input variables. */
BoolType b_; /**< Input variable "bool b". */
IntType i_;  /**< Input variable "int i". */
RealType r_; /**< Input variable "real r". */

/* State variables. */
fmtEnum a_; /**< Discrete variable "E a". */

RealType model_time; /**< Current model time. */

/** Initialize constants. */
static void InitConstants(void) {

}

/** Print function. */
#if PRINT_OUTPUT
static void PrintOutput(fmt_Event_ event, BoolType pre) {
    StringType text_var10;

    if (!pre) {
        {
            int index = 0;
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, (b_) ? "true" : "false");
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            int index = 0;
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, (b_) ? "TRUE" : "FALSE");
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            char scratch[(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128]; /* Value scratch space. */
            int index = 0;
            snprintf(scratch, sizeof(scratch), "%d", i_);
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, scratch);
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            char scratch[(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128]; /* Value scratch space. */
            int index = 0;
            snprintf(scratch, sizeof(scratch), "%x", i_);
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, scratch);
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            char scratch[(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128]; /* Value scratch space. */
            int index = 0;
            snprintf(scratch, sizeof(scratch), "%X", i_);
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, scratch);
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            int index = 0;
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_LEFT, 15, (b_) ? "true" : "false");
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            char scratch[(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128]; /* Value scratch space. */
            int index = 0;
            snprintf(scratch, sizeof(scratch), "%f", r_);
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, scratch);
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            char scratch[(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128]; /* Value scratch space. */
            int index = 0;
            snprintf(scratch, sizeof(scratch), "%.3f", r_);
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_GROUPS, 0, scratch);
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            char scratch[(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128]; /* Value scratch space. */
            int index = 0;
            snprintf(scratch, sizeof(scratch), "%.3e", r_);
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_SIGN, 0, scratch);
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            char scratch[(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128]; /* Value scratch space. */
            int index = 0;
            snprintf(scratch, sizeof(scratch), "%.3E", r_);
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_SIGN, 0, scratch);
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            char scratch[(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128]; /* Value scratch space. */
            int index = 0;
            snprintf(scratch, sizeof(scratch), "%.3g", r_);
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_GROUPS, 0, scratch);
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            char scratch[(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128]; /* Value scratch space. */
            int index = 0;
            snprintf(scratch, sizeof(scratch), "%.3G", r_);
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_GROUPS, 0, scratch);
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            int index = 0;
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, (b_) ? "true" : "false");
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            char scratch[(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128]; /* Value scratch space. */
            int index = 0;
            snprintf(scratch, sizeof(scratch), "%d", i_);
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, scratch);
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            char scratch[(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128]; /* Value scratch space. */
            int index = 0;
            snprintf(scratch, sizeof(scratch), "%f", r_);
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, scratch);
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            StringType fmt_temp11 = s_();
            StringType dest_scratch; /* Resulting string scratch space. */
            int index = 0;
            index = StringTypeAppendText(&dest_scratch, index, FMTFLAGS_NONE, 0, (&fmt_temp11)->data);
            memcpy(text_var10.data, dest_scratch.data, MAX_STRING_SIZE);
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            A1SType fmt_temp12 = ls_();
            char scratch[(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128]; /* Value scratch space. */
            int index = 0;
            A1STypePrint(&fmt_temp12, scratch, 0, MAX_STRING_SIZE);
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, scratch);
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            char scratch[(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128]; /* Value scratch space. */
            int index = 0;
            snprintf(scratch, sizeof(scratch), "%.5f", r_);
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_GROUPS, 23, scratch);
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            char scratch[(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128]; /* Value scratch space. */
            int index = 0;
            snprintf(scratch, sizeof(scratch), "%.3f", r_);
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_SPACE, 25, scratch);
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            char scratch[(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128]; /* Value scratch space. */
            int index = 0;
            snprintf(scratch, sizeof(scratch), "%.3f", r_);
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_SIGN|FMTFLAGS_ZEROES|FMTFLAGS_GROUPS, 25, scratch);
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            char scratch[(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128]; /* Value scratch space. */
            int index = 0;
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, "a");
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, "%");
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, "b");
            snprintf(scratch, sizeof(scratch), "%f", r_);
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, scratch);
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, "c");
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            IntType fmt_temp13 = i_;
            char scratch[(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128]; /* Value scratch space. */
            int index = 0;
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, "a");
            snprintf(scratch, sizeof(scratch), "%d", fmt_temp13);
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, scratch);
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, "b");
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, (b_) ? "true" : "false");
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, "c");
            snprintf(scratch, sizeof(scratch), "%d", fmt_temp13);
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, scratch);
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            StringType fmt_temp14 = s1_();
            StringType dest_scratch; /* Resulting string scratch space. */
            int index = 0;
            index = StringTypeAppendText(&dest_scratch, index, FMTFLAGS_NONE, 0, (&fmt_temp14)->data);
            memcpy(text_var10.data, dest_scratch.data, MAX_STRING_SIZE);
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            StringType fmt_temp15 = s2_();
            StringType dest_scratch; /* Resulting string scratch space. */
            int index = 0;
            index = StringTypeAppendText(&dest_scratch, index, FMTFLAGS_NONE, 0, (&fmt_temp15)->data);
            memcpy(text_var10.data, dest_scratch.data, MAX_STRING_SIZE);
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            StringType fmt_temp16 = s3_();
            StringType dest_scratch; /* Resulting string scratch space. */
            int index = 0;
            index = StringTypeAppendText(&dest_scratch, index, FMTFLAGS_NONE, 0, (&fmt_temp16)->data);
            index = StringTypeAppendText(&dest_scratch, index, FMTFLAGS_NONE, 0, " # 1\n2\t3\\4\"5 6");
            index = StringTypeAppendText(&dest_scratch, index, FMTFLAGS_NONE, 0, "%");
            index = StringTypeAppendText(&dest_scratch, index, FMTFLAGS_NONE, 0, "7");
            memcpy(text_var10.data, dest_scratch.data, MAX_STRING_SIZE);
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            char scratch[(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128]; /* Value scratch space. */
            int index = 0;
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, "_");
            snprintf(scratch, sizeof(scratch), "%d", neg12345_());
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_LEFT|FMTFLAGS_SIGN|FMTFLAGS_GROUPS, 8, scratch);
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, "_");
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            char scratch[(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128]; /* Value scratch space. */
            int index = 0;
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, "_");
            snprintf(scratch, sizeof(scratch), "%d", neg12345_());
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_LEFT|FMTFLAGS_SIGN|FMTFLAGS_GROUPS, 8, scratch);
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, "_");
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            char scratch[(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128]; /* Value scratch space. */
            int index = 0;
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, "_");
            snprintf(scratch, sizeof(scratch), "%d", neg12345_());
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_LEFT|FMTFLAGS_SIGN|FMTFLAGS_GROUPS, 8, scratch);
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, "_");
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            char scratch[(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128]; /* Value scratch space. */
            int index = 0;
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, "_");
            snprintf(scratch, sizeof(scratch), "%d", neg12345_());
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_LEFT|FMTFLAGS_SIGN|FMTFLAGS_GROUPS, 8, scratch);
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, "_");
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            char scratch[(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128]; /* Value scratch space. */
            int index = 0;
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, "_");
            snprintf(scratch, sizeof(scratch), "%d", neg12345_());
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_LEFT|FMTFLAGS_SIGN|FMTFLAGS_GROUPS, 8, scratch);
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, "_");
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            char scratch[(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128]; /* Value scratch space. */
            int index = 0;
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, "_");
            snprintf(scratch, sizeof(scratch), "%d", neg12345_());
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_LEFT|FMTFLAGS_SIGN|FMTFLAGS_GROUPS, 8, scratch);
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, "_");
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            char scratch[(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128]; /* Value scratch space. */
            int index = 0;
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, "#");
            snprintf(scratch, sizeof(scratch), "%.0f", r456_());
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, scratch);
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, "#");
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            char scratch[(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128]; /* Value scratch space. */
            int index = 0;
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, "#");
            snprintf(scratch, sizeof(scratch), "%.0f", r456_());
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, scratch);
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, "#");
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            char scratch[(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128]; /* Value scratch space. */
            int index = 0;
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, "#");
            snprintf(scratch, sizeof(scratch), "%.0f", r456_());
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, scratch);
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, "#");
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            char scratch[(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128]; /* Value scratch space. */
            int index = 0;
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, "#");
            snprintf(scratch, sizeof(scratch), "%.1f", r456_());
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, scratch);
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, "#");
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            char scratch[(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128]; /* Value scratch space. */
            int index = 0;
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, "#");
            snprintf(scratch, sizeof(scratch), "%.1f", r456_());
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, scratch);
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, "#");
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            char scratch[(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128]; /* Value scratch space. */
            int index = 0;
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, "#");
            snprintf(scratch, sizeof(scratch), "%.2f", r456_());
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, scratch);
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, "#");
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            char scratch[(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128]; /* Value scratch space. */
            int index = 0;
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, "@");
            snprintf(scratch, sizeof(scratch), "%.0e", r456_());
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, scratch);
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, "@");
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            char scratch[(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128]; /* Value scratch space. */
            int index = 0;
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, "@");
            snprintf(scratch, sizeof(scratch), "%.0E", r456_());
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, scratch);
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, "@");
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            char scratch[(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128]; /* Value scratch space. */
            int index = 0;
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, "@");
            snprintf(scratch, sizeof(scratch), "%.0f", r456_());
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, scratch);
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, "@");
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            char scratch[(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128]; /* Value scratch space. */
            int index = 0;
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, "@");
            snprintf(scratch, sizeof(scratch), "%.0g", r456_());
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, scratch);
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, "@");
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            char scratch[(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128]; /* Value scratch space. */
            int index = 0;
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, "@");
            snprintf(scratch, sizeof(scratch), "%.0G", r456_());
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, scratch);
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, "@");
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            char scratch[(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128]; /* Value scratch space. */
            int index = 0;
            snprintf(scratch, sizeof(scratch), "%.4g", RealAdd(r_zero_(), 1.2345678E9));
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, scratch);
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            char scratch[(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128]; /* Value scratch space. */
            int index = 0;
            snprintf(scratch, sizeof(scratch), "%.4g", RealAdd(r_zero_(), 1.2345678E8));
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, scratch);
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            char scratch[(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128]; /* Value scratch space. */
            int index = 0;
            snprintf(scratch, sizeof(scratch), "%.4g", RealAdd(r_zero_(), 1.2345678E7));
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, scratch);
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            char scratch[(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128]; /* Value scratch space. */
            int index = 0;
            snprintf(scratch, sizeof(scratch), "%.4g", RealAdd(r_zero_(), 1234567.8));
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, scratch);
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            char scratch[(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128]; /* Value scratch space. */
            int index = 0;
            snprintf(scratch, sizeof(scratch), "%.4g", RealAdd(r_zero_(), 123456.78));
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, scratch);
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            char scratch[(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128]; /* Value scratch space. */
            int index = 0;
            snprintf(scratch, sizeof(scratch), "%.4g", RealAdd(r_zero_(), 12345.678));
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, scratch);
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            char scratch[(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128]; /* Value scratch space. */
            int index = 0;
            snprintf(scratch, sizeof(scratch), "%.4g", RealAdd(r_zero_(), 1234.5678));
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, scratch);
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            char scratch[(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128]; /* Value scratch space. */
            int index = 0;
            snprintf(scratch, sizeof(scratch), "%.4g", RealAdd(r_zero_(), 123.45678));
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, scratch);
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            char scratch[(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128]; /* Value scratch space. */
            int index = 0;
            snprintf(scratch, sizeof(scratch), "%.4g", RealAdd(r_zero_(), 12.345678));
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, scratch);
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            char scratch[(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128]; /* Value scratch space. */
            int index = 0;
            snprintf(scratch, sizeof(scratch), "%.4g", RealAdd(r_zero_(), 1.2345678));
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, scratch);
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            char scratch[(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128]; /* Value scratch space. */
            int index = 0;
            snprintf(scratch, sizeof(scratch), "%.4g", RealAdd(r_zero_(), 0.12345678));
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, scratch);
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            char scratch[(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128]; /* Value scratch space. */
            int index = 0;
            snprintf(scratch, sizeof(scratch), "%.4g", RealAdd(r_zero_(), 0.012345678));
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, scratch);
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            char scratch[(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128]; /* Value scratch space. */
            int index = 0;
            snprintf(scratch, sizeof(scratch), "%.4g", RealAdd(r_zero_(), 0.0012345678));
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, scratch);
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            char scratch[(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128]; /* Value scratch space. */
            int index = 0;
            snprintf(scratch, sizeof(scratch), "%.4g", RealAdd(r_zero_(), 1.2345678E-4));
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, scratch);
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            char scratch[(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128]; /* Value scratch space. */
            int index = 0;
            snprintf(scratch, sizeof(scratch), "%.4g", RealAdd(r_zero_(), 1.2345678E-5));
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, scratch);
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            StringType fmt_temp17 = s0_();
            StringType dest_scratch; /* Resulting string scratch space. */
            int index = 0;
            index = StringTypeAppendText(&dest_scratch, index, FMTFLAGS_NONE, 0, (&fmt_temp17)->data);
            memcpy(text_var10.data, dest_scratch.data, MAX_STRING_SIZE);
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            char scratch[(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128]; /* Value scratch space. */
            int index = 0;
            snprintf(scratch, sizeof(scratch), "%d", i0_());
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, scratch);
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            int index = 0;
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, (b0_()) ? "true" : "false");
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            T2IA1IType fmt_temp18 = t0_();
            char scratch[(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128]; /* Value scratch space. */
            int index = 0;
            T2IA1ITypePrint(&fmt_temp18, scratch, 0, MAX_STRING_SIZE);
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, scratch);
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            char scratch[(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128]; /* Value scratch space. */
            int index = 0;
            snprintf(scratch, sizeof(scratch), "%f", r0_());
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, scratch);
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            StringType fmt_temp19 = s00_();
            StringType dest_scratch; /* Resulting string scratch space. */
            int index = 0;
            index = StringTypeAppendText(&dest_scratch, index, FMTFLAGS_NONE, 0, (&fmt_temp19)->data);
            memcpy(text_var10.data, dest_scratch.data, MAX_STRING_SIZE);
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            A2SType fmt_temp20 = l0_();
            char scratch[(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128]; /* Value scratch space. */
            int index = 0;
            A2STypePrint(&fmt_temp20, scratch, 0, MAX_STRING_SIZE);
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, scratch);
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            int index = 0;
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, enum_names[e0_()]);
        }
        fmt_PrintOutput(text_var10.data, ":stdout");

        {
            IntType fmt_temp21 = ii2_();
            RealType fmt_temp22 = rr6_();
            char scratch[(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128]; /* Value scratch space. */
            int index = 0;
            snprintf(scratch, sizeof(scratch), "%d", ii1_());
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, scratch);
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, " ");
            snprintf(scratch, sizeof(scratch), "%d", ii4_());
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, scratch);
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, " ");
            snprintf(scratch, sizeof(scratch), "%d", fmt_temp21);
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, scratch);
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, " ");
            snprintf(scratch, sizeof(scratch), "%f", fmt_temp22);
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, scratch);
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, " ");
            snprintf(scratch, sizeof(scratch), "%d", fmt_temp21);
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, scratch);
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, " ");
            snprintf(scratch, sizeof(scratch), "%f", fmt_temp22);
            index = StringTypeAppendText(&text_var10, index, FMTFLAGS_NONE, 0, scratch);
        }
        fmt_PrintOutput(text_var10.data, ":stdout");
    }
}
#endif

/* Event execution code. */


/**
 * Normalize and check the new value of a continuous variable after an update.
 * @param new_value Unnormalized new value of the continuous variable.
 * @param var_name Name of the continuous variable in the CIF model.
 * @return The normalized new value of the continuous variable.
 */
static inline RealType UpdateContValue(RealType new_value, const char *var_name) {
    if (isfinite(new_value)) {
        return (new_value == -0.0) ? 0.0 : new_value;
    }

    const char *err_type;
    if (isnan(new_value)) {
        err_type = "NaN";
    } else if (new_value > 0) {
        err_type = "+inf";
    } else {
        err_type = "-inf";
    }
    fprintf(stderr, "Continuous variable \"%s\" has become %s.\n", var_name, err_type);

#ifdef KEEP_RUNNING
    return 0.0;
#else
    exit(1);
#endif
}

/** Repeatedly perform discrete event steps, until no progress can be made any more. */
static void PerformEvents(void) {
    int count = 0;
    for (;;) {
        count++;
        if (count > MAX_NUM_EVENTS) { /* 'Infinite' loop detection. */
            fprintf(stderr, "Warning: Quitting after performing %d events, infinite loop?\n", count);
            break;
        }


        break; /* No event fired, done with discrete steps. */
    }
}

/** First model call, initializing, and performing discrete events before the first time step. */
void fmt_EngineFirstStep(void) {
    InitConstants();

    model_time = 0.0;
    fmt_AssignInputVariables();
    a_ = _fmt_X;

    #if PRINT_OUTPUT
        /* pre-initial and post-initial prints. */
        PrintOutput(EVT_INITIAL_, TRUE);
        PrintOutput(EVT_INITIAL_, FALSE);
    #endif

    PerformEvents();

    #if PRINT_OUTPUT
        /* pre-timestep print. */
        PrintOutput(EVT_DELAY_, TRUE);
    #endif
}

/**
 * Engine takes a time step of length \a delta.
 * @param delta Length of the time step.
 */
void fmt_EngineTimeStep(double delta) {
    fmt_AssignInputVariables();

    /* Update continuous variables. */
    if (delta > 0.0) {

        model_time += delta;
    }

    #if PRINT_OUTPUT
        /* post-timestep print. */
        PrintOutput(EVT_DELAY_, FALSE);
    #endif

    PerformEvents();

    #if PRINT_OUTPUT
        /* pre-timestep print. */
        PrintOutput(EVT_DELAY_, TRUE);
    #endif
}

