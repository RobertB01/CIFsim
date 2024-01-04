/* CIF to C translation of edges.cif
 * Generated file, DO NOT EDIT
 */

#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include "edges_engine.h"

#ifndef MAX_NUM_EVENTS
#define MAX_NUM_EVENTS 1000
#endif

/* What to do if a range error is found in an assignment? */
#ifdef KEEP_RUNNING
static void RangeErrorDetected(void) { /* Do nothing, error is already reported. */ }
#else
static void RangeErrorDetected(void) { exit(1); }
#endif

/* Type support code. */
int EnumTypePrint(edgesEnum value, char *dest, int start, int end) {
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
BoolType A5ITypeEquals(A5IType *left, A5IType *right) {
    if (left == right) return TRUE;
    return memcmp(left, right, sizeof(A5IType)) == 0;
}

/**
 * Extract an element from the array.
 * @param array Array with value to retrieve.
 * @param index Element index in the array (not normalized).
 * @return Element value at the indicated index from the array.
 */
IntType A5ITypeProject(A5IType *array, IntType index) {
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
void A5ITypeModify(A5IType *array, IntType index, IntType value) {
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
int A5ITypePrint(A5IType *array, char *dest, int start, int end) {
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
 * Compare two tuples for equality.
 * @param left First tuple to compare.
 * @param right Second tuple to compare.
 * @return Whether both tuples are the same.
 */
BoolType T2IITypeEquals(T2IIType *left, T2IIType *right) {
    if (left == right) return TRUE;
    if (memcmp(&left->_field0, &right->_field0, sizeof(IntType)) != 0) return FALSE;
    if (memcmp(&left->_field1, &right->_field1, sizeof(IntType)) != 0) return FALSE;
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
int T2IITypePrint(T2IIType *tuple, char *dest, int start, int end) {
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
BoolType T2T2IISTypeEquals(T2T2IISType *left, T2T2IISType *right) {
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
int T2T2IISTypePrint(T2T2IISType *tuple, char *dest, int start, int end) {
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
BoolType A3ITypeEquals(A3IType *left, A3IType *right) {
    if (left == right) return TRUE;
    return memcmp(left, right, sizeof(A3IType)) == 0;
}

/**
 * Extract an element from the array.
 * @param array Array with value to retrieve.
 * @param index Element index in the array (not normalized).
 * @return Element value at the indicated index from the array.
 */
IntType A3ITypeProject(A3IType *array, IntType index) {
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
void A3ITypeModify(A3IType *array, IntType index, IntType value) {
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
int A3ITypePrint(A3IType *array, char *dest, int start, int end) {
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
 * Compare two arrays for equality.
 * @param left First array to compare.
 * @param right Second array to compare.
 * @return Whether both arrays are the same.
 */
BoolType A2A3ITypeEquals(A2A3IType *left, A2A3IType *right) {
    if (left == right) return TRUE;
    return memcmp(left, right, sizeof(A2A3IType)) == 0;
}

/**
 * Extract an element from the array.
 * @param array Array with value to retrieve.
 * @param index Element index in the array (not normalized).
 * @return Element value at the indicated index from the array.
 */
A3IType *A2A3ITypeProject(A2A3IType *array, IntType index) {
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
void A2A3ITypeModify(A2A3IType *array, IntType index, A3IType *value) {
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
int A2A3ITypePrint(A2A3IType *array, char *dest, int start, int end) {
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
 * Compare two arrays for equality.
 * @param left First array to compare.
 * @param right Second array to compare.
 * @return Whether both arrays are the same.
 */
BoolType A1RTypeEquals(A1RType *left, A1RType *right) {
    if (left == right) return TRUE;
    return memcmp(left, right, sizeof(A1RType)) == 0;
}

/**
 * Extract an element from the array.
 * @param array Array with value to retrieve.
 * @param index Element index in the array (not normalized).
 * @return Element value at the indicated index from the array.
 */
RealType A1RTypeProject(A1RType *array, IntType index) {
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
void A1RTypeModify(A1RType *array, IntType index, RealType value) {
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
int A1RTypePrint(A1RType *array, char *dest, int start, int end) {
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
 * Compare two tuples for equality.
 * @param left First tuple to compare.
 * @param right Second tuple to compare.
 * @return Whether both tuples are the same.
 */
BoolType T2A1IA1RTypeEquals(T2A1IA1RType *left, T2A1IA1RType *right) {
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
int T2A1IA1RTypePrint(T2A1IA1RType *tuple, char *dest, int start, int end) {
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
BoolType A2T2A1IA1RTypeEquals(A2T2A1IA1RType *left, A2T2A1IA1RType *right) {
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
T2A1IA1RType *A2T2A1IA1RTypeProject(A2T2A1IA1RType *array, IntType index) {
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
void A2T2A1IA1RTypeModify(A2T2A1IA1RType *array, IntType index, T2A1IA1RType *value) {
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
int A2T2A1IA1RTypePrint(A2T2A1IA1RType *array, char *dest, int start, int end) {
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
BoolType T2SA2T2A1IA1RTypeEquals(T2SA2T2A1IA1RType *left, T2SA2T2A1IA1RType *right) {
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
int T2SA2T2A1IA1RTypePrint(T2SA2T2A1IA1RType *tuple, char *dest, int start, int end) {
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
BoolType A3T2IITypeEquals(A3T2IIType *left, A3T2IIType *right) {
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
T2IIType *A3T2IITypeProject(A3T2IIType *array, IntType index) {
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
void A3T2IITypeModify(A3T2IIType *array, IntType index, T2IIType *value) {
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
int A3T2IITypePrint(A3T2IIType *array, char *dest, int start, int end) {
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


/** Event names. */
const char *edges_event_names[] = {
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

/** Enumeration names. */
const char *enum_names[] = {
    "loc1",
    "loc2",
    "loc3",
};

/* Constants. */


/* Functions. */


/* Input variables. */

/** Input variable "bool aut14.b". */
BoolType aut14_b_;

/** Input variable "int aut14.i". */
IntType aut14_i_;

/** Input variable "real aut14.r". */
RealType aut14_r_;

/* State variables. */

/** Discrete variable "int[0..3] aut02.x". */
IntType aut02_x_;

/** Discrete variable "E aut02". */
edgesEnum aut02_;

/** Continuous variable "real aut03.c". */
RealType aut03_c_;

/** Discrete variable "int aut03.d". */
IntType aut03_d_;

/** Discrete variable "int aut04.a". */
IntType aut04_a_;

/** Discrete variable "int aut04.b". */
IntType aut04_b_;

/** Discrete variable "int aut04.c". */
IntType aut04_c_;

/** Discrete variable "int aut04.d". */
IntType aut04_d_;

/** Discrete variable "list[5] int aut05.v1". */
A5IType aut05_v1_;

/** Discrete variable "list[5] int aut05.v2". */
A5IType aut05_v2_;

/** Discrete variable "tuple(int a; int b) aut06.v1". */
T2IIType aut06_v1_;

/** Discrete variable "tuple(int a; int b) aut06.v2". */
T2IIType aut06_v2_;

/** Discrete variable "int aut06.x". */
IntType aut06_x_;

/** Discrete variable "int aut06.y". */
IntType aut06_y_;

/** Continuous variable "real aut07.x". */
RealType aut07_x_;

/** Continuous variable "real aut07.y". */
RealType aut07_y_;

/** Discrete variable "tuple(tuple(int a; int b) t; string c) aut08.tt1". */
T2T2IISType aut08_tt1_;

/** Discrete variable "tuple(tuple(int a; int b) t; string c) aut08.tt2". */
T2T2IISType aut08_tt2_;

/** Discrete variable "tuple(int a; int b) aut08.t". */
T2IIType aut08_t_;

/** Discrete variable "int aut08.i". */
IntType aut08_i_;

/** Discrete variable "int aut08.j". */
IntType aut08_j_;

/** Discrete variable "string aut08.s". */
StringType aut08_s_;

/** Discrete variable "list[2] list[3] int aut09.ll1". */
A2A3IType aut09_ll1_;

/** Discrete variable "list[2] list[3] int aut09.ll2". */
A2A3IType aut09_ll2_;

/** Discrete variable "list[3] int aut09.l". */
A3IType aut09_l_;

/** Discrete variable "int aut09.i". */
IntType aut09_i_;

/** Discrete variable "int aut09.j". */
IntType aut09_j_;

/** Discrete variable "tuple(string s; list[2] tuple(list[1] int x; list[1] real y) z) aut10.x1". */
T2SA2T2A1IA1RType aut10_x1_;

/** Discrete variable "tuple(string s; list[2] tuple(list[1] int x; list[1] real y) z) aut10.x2". */
T2SA2T2A1IA1RType aut10_x2_;

/** Discrete variable "list[2] tuple(list[1] int x; list[1] real y) aut10.l". */
A2T2A1IA1RType aut10_l_;

/** Discrete variable "list[1] int aut10.li". */
A1IType aut10_li_;

/** Discrete variable "list[1] real aut10.lr". */
A1RType aut10_lr_;

/** Discrete variable "int aut10.i". */
IntType aut10_i_;

/** Discrete variable "real aut10.r". */
RealType aut10_r_;

/** Discrete variable "list[3] tuple(int a; int b) aut11.v1". */
A3T2IIType aut11_v1_;

/** Discrete variable "real aut12.x". */
RealType aut12_x_;

/** Discrete variable "real aut12.y". */
RealType aut12_y_;

/** Discrete variable "real aut12.z". */
RealType aut12_z_;

/** Discrete variable "real aut12.td". */
RealType aut12_td_;

/** Continuous variable "real aut12.t". */
RealType aut12_t_;

/** Continuous variable "real aut12.u". */
RealType aut12_u_;

/** Discrete variable "real aut13.x". */
RealType aut13_x_;

/** Discrete variable "real aut13.y". */
RealType aut13_y_;

/** Discrete variable "real aut13.z". */
RealType aut13_z_;

/* Derivative and algebraic variable functions. */
/** Derivative of "aut03.c". */
RealType aut03_c_deriv(void) {
    return 1.0;
}

/** Derivative of "aut07.x". */
RealType aut07_x_deriv(void) {
    return 1.0;
}

/** Derivative of "aut07.y". */
RealType aut07_y_deriv(void) {
    return 2.0;
}

/** Derivative of "aut12.t". */
RealType aut12_t_deriv(void) {
    return RealAdd(aut12_x_, aut12_y_);
}

/** Derivative of "aut12.u". */
RealType aut12_u_deriv(void) {
    return RealAdd(aut12_t_deriv(), aut12_z_);
}
/**
 * Algebraic variable aut12.v = M.aut12_x + M.aut12_y;
 */
RealType aut12_v_(void) {
    return RealAdd(aut12_x_, aut12_y_);
}

/**
 * Algebraic variable aut12.w = aut12.v + M.aut12_z;
 */
RealType aut12_w_(void) {
    return RealAdd(aut12_v_(), aut12_z_);
}

/**
 * Algebraic variable aut13.v = M.aut13_x + M.aut13_y;
 */
RealType aut13_v_(void) {
    return RealAdd(aut13_x_, aut13_y_);
}

/**
 * Algebraic variable aut13.w = aut13.v + M.aut13_z;
 */
RealType aut13_w_(void) {
    return RealAdd(aut13_v_(), aut13_z_);
}

RealType model_time; /**< Current model time. */

/** Initialize constants. */
static void InitConstants(void) {

}

/** Print function. */
#if PRINT_OUTPUT
static void PrintOutput(edges_Event_ event, BoolType pre) {
}
#endif

/* Event execution code. */

/**
 * Execute code for event "e02a".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent0(void) {
    BoolType guard = ((aut02_) == (_edges_loc1)) || (((aut02_) == (_edges_loc2)) || ((aut02_) == (_edges_loc3)));
    if (!guard) return FALSE;

    #if EVENT_OUTPUT
        edges_InfoEvent(e02a_, TRUE);
    #endif

    if ((aut02_) == (_edges_loc1)) {
        aut02_ = _edges_loc2;
    } else if ((aut02_) == (_edges_loc2)) {
        aut02_ = _edges_loc3;
    } else if ((aut02_) == (_edges_loc3)) {
        aut02_ = _edges_loc1;
    }

    #if EVENT_OUTPUT
        edges_InfoEvent(e02a_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e02b".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent1(void) {
    BoolType guard = (((aut02_) == (_edges_loc1)) && ((aut02_x_) == (2))) || (((aut02_) == (_edges_loc2)) || (((aut02_) == (_edges_loc3)) && ((aut02_x_) == (3))));
    if (!guard) return FALSE;

    #if EVENT_OUTPUT
        edges_InfoEvent(e02b_, TRUE);
    #endif

    if (((aut02_) == (_edges_loc1)) && ((aut02_x_) == (2))) {
        aut02_ = _edges_loc1;
    } else if ((aut02_) == (_edges_loc2)) {
        aut02_x_ = 1;
    } else if (((aut02_) == (_edges_loc3)) && ((aut02_x_) == (3))) {
        aut02_x_ = 1;
    }

    #if EVENT_OUTPUT
        edges_InfoEvent(e02b_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e03a".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent2(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e03a_, TRUE);
    #endif

    aut03_c_ = 1.23;

    #if EVENT_OUTPUT
        edges_InfoEvent(e03a_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e03b".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent3(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e03b_, TRUE);
    #endif

    aut03_d_ = 2;

    #if EVENT_OUTPUT
        edges_InfoEvent(e03b_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e04a".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent4(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e04a_, TRUE);
    #endif

    if ((aut04_a_) == (1)) {
        aut04_b_ = 2;
    }

    #if EVENT_OUTPUT
        edges_InfoEvent(e04a_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e04b".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent5(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e04b_, TRUE);
    #endif

    if ((aut04_a_) == (1)) {
        aut04_b_ = 2;
    } else if ((aut04_a_) == (2)) {
        aut04_b_ = 3;
    }

    #if EVENT_OUTPUT
        edges_InfoEvent(e04b_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e04c".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent6(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e04c_, TRUE);
    #endif

    if ((aut04_a_) == (1)) {
        aut04_b_ = 2;
    } else if ((aut04_a_) == (2)) {
        aut04_b_ = 3;
    } else if ((aut04_a_) == (3)) {
        aut04_b_ = 4;
    }

    #if EVENT_OUTPUT
        edges_InfoEvent(e04c_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e04d".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent7(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e04d_, TRUE);
    #endif

    if ((aut04_a_) == (1)) {
        aut04_b_ = 2;
    } else if ((aut04_a_) == (2)) {
        aut04_b_ = 3;
    } else if ((aut04_a_) == (3)) {
        aut04_b_ = 4;
    } else {
        aut04_b_ = 5;
    }

    #if EVENT_OUTPUT
        edges_InfoEvent(e04d_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e04e".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent8(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e04e_, TRUE);
    #endif

    if ((aut04_a_) == (1)) {
        aut04_b_ = 2;
    } else {
        aut04_b_ = 5;
    }

    #if EVENT_OUTPUT
        edges_InfoEvent(e04e_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e04f".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent9(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e04f_, TRUE);
    #endif

    if ((aut04_a_) == (1)) {
        aut04_b_ = 2;
    } else {
        aut04_b_ = 5;
    }
    if ((aut04_a_) == (1)) {
        aut04_c_ = 2;
    } else {
        aut04_d_ = 5;
    }

    #if EVENT_OUTPUT
        edges_InfoEvent(e04f_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e05a".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent10(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e05a_, TRUE);
    #endif

    {
        IntType rhs2 = 3;
        IntType index3 = 0;
        A5ITypeModify(&aut05_v1_, index3, rhs2);
    }
    {
        IntType rhs2 = 4;
        IntType index3 = 1;
        A5ITypeModify(&aut05_v1_, index3, rhs2);
    }

    #if EVENT_OUTPUT
        edges_InfoEvent(e05a_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e05b".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent11(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e05b_, TRUE);
    #endif

    {
        IntType rhs2 = 3;
        IntType index3 = 0;
        A5ITypeModify(&aut05_v1_, index3, rhs2);
    }
    {
        IntType rhs2 = 4;
        IntType index3 = 1;
        A5ITypeModify(&aut05_v1_, index3, rhs2);
    }

    #if EVENT_OUTPUT
        edges_InfoEvent(e05b_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e05c".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent12(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e05c_, TRUE);
    #endif

    aut05_v1_ = aut05_v2_;

    #if EVENT_OUTPUT
        edges_InfoEvent(e05c_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e05d".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent13(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e05d_, TRUE);
    #endif

    {
        IntType rhs2 = A5ITypeProject(&(aut05_v2_), 0);
        IntType index3 = 0;
        A5ITypeModify(&aut05_v1_, index3, rhs2);
    }
    {
        IntType rhs2 = A5ITypeProject(&(aut05_v2_), 1);
        IntType index3 = 1;
        A5ITypeModify(&aut05_v1_, index3, rhs2);
    }

    #if EVENT_OUTPUT
        edges_InfoEvent(e05d_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e05e".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent14(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e05e_, TRUE);
    #endif

    {
        IntType rhs2 = A5ITypeProject(&(aut05_v2_), 1);
        IntType index3 = 0;
        A5ITypeModify(&aut05_v1_, index3, rhs2);
    }
    {
        IntType rhs2 = A5ITypeProject(&(aut05_v2_), 0);
        IntType index3 = 1;
        A5ITypeModify(&aut05_v1_, index3, rhs2);
    }

    #if EVENT_OUTPUT
        edges_InfoEvent(e05e_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e06a".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent15(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e06a_, TRUE);
    #endif

    (aut06_v1_)._field0 = 3;
    (aut06_v1_)._field1 = 4;

    #if EVENT_OUTPUT
        edges_InfoEvent(e06a_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e06b".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent16(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e06b_, TRUE);
    #endif

    {
        IntType rhs2 = 5;
        aut06_v1_._field0 = rhs2;
    }

    #if EVENT_OUTPUT
        edges_InfoEvent(e06b_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e06c".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent17(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e06c_, TRUE);
    #endif

    {
        T2IIType rhs2 = aut06_v1_;
        aut06_x_ = (rhs2)._field0;
        aut06_y_ = (rhs2)._field1;
    }

    #if EVENT_OUTPUT
        edges_InfoEvent(e06c_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e06d".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent18(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e06d_, TRUE);
    #endif

    (aut06_v1_)._field0 = IntegerAdd(aut06_x_, 1);
    (aut06_v1_)._field1 = IntegerMultiply(aut06_y_, 2);

    #if EVENT_OUTPUT
        edges_InfoEvent(e06d_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e06e".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent19(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e06e_, TRUE);
    #endif

    aut06_v1_ = aut06_v2_;

    #if EVENT_OUTPUT
        edges_InfoEvent(e06e_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e07a".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent20(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e07a_, TRUE);
    #endif

    aut07_x_ = 5.0;

    #if EVENT_OUTPUT
        edges_InfoEvent(e07a_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e07b".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent21(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e07b_, TRUE);
    #endif

    aut07_y_ = aut07_x_;
    aut07_x_ = 5.0;

    #if EVENT_OUTPUT
        edges_InfoEvent(e07b_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e08a".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent22(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e08a_, TRUE);
    #endif

    ((aut08_tt1_)._field0)._field0 = 1;
    ((aut08_tt1_)._field0)._field1 = 2;
    StringTypeCopyText(&((aut08_tt1_)._field1), "abc");

    #if EVENT_OUTPUT
        edges_InfoEvent(e08a_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e08b".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent23(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e08b_, TRUE);
    #endif

    aut08_tt1_ = aut08_tt2_;

    #if EVENT_OUTPUT
        edges_InfoEvent(e08b_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e08c".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent24(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e08c_, TRUE);
    #endif

    {
        T2IIType rhs2 = aut08_t_;
        aut08_tt1_._field0 = rhs2;
    }

    #if EVENT_OUTPUT
        edges_InfoEvent(e08c_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e08d".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent25(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e08d_, TRUE);
    #endif

    {
        IntType rhs2 = 3;
        T2IIType part3 = (aut08_tt1_)._field0;
        part3._field1 = rhs2;
        aut08_tt1_._field0 = part3;
    }

    #if EVENT_OUTPUT
        edges_InfoEvent(e08d_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e08e".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent26(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e08e_, TRUE);
    #endif

    {
        IntType rhs2 = 4;
        T2IIType part3 = (aut08_tt1_)._field0;
        part3._field0 = rhs2;
        aut08_tt1_._field0 = part3;
    }

    #if EVENT_OUTPUT
        edges_InfoEvent(e08e_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e08f".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent27(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e08f_, TRUE);
    #endif

    {
        StringType str_tmp3;
        StringTypeCopyText(&str_tmp3, "def");
        StringType rhs2 = str_tmp3;
        aut08_tt1_._field1 = rhs2;
    }

    #if EVENT_OUTPUT
        edges_InfoEvent(e08f_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e08g".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent28(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e08g_, TRUE);
    #endif

    {
        T2IIType rhs2 = (aut08_tt1_)._field0;
        aut08_i_ = (rhs2)._field0;
        aut08_j_ = (rhs2)._field1;
    }

    #if EVENT_OUTPUT
        edges_InfoEvent(e08g_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e08h".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent29(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e08h_, TRUE);
    #endif

    {
        T2T2IISType rhs2 = aut08_tt1_;
        aut08_i_ = (rhs2)._field0._field0;
        aut08_j_ = (rhs2)._field0._field1;
        aut08_s_ = (rhs2)._field1;
    }

    #if EVENT_OUTPUT
        edges_InfoEvent(e08h_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e09a".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent30(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e09a_, TRUE);
    #endif

    ((aut09_ll1_).data[0]).data[0] = 1;
    ((aut09_ll1_).data[0]).data[1] = 2;
    ((aut09_ll1_).data[0]).data[2] = 3;
    ((aut09_ll1_).data[1]).data[0] = 4;
    ((aut09_ll1_).data[1]).data[1] = 5;
    ((aut09_ll1_).data[1]).data[2] = 6;

    #if EVENT_OUTPUT
        edges_InfoEvent(e09a_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e09b".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent31(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e09b_, TRUE);
    #endif

    aut09_ll1_ = aut09_ll2_;

    #if EVENT_OUTPUT
        edges_InfoEvent(e09b_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e09c".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent32(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e09c_, TRUE);
    #endif

    {
        A3IType rhs2 = aut09_l_;
        IntType index3 = 0;
        A2A3ITypeModify(&aut09_ll1_, index3, &(rhs2));
    }

    #if EVENT_OUTPUT
        edges_InfoEvent(e09c_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e09d".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent33(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e09d_, TRUE);
    #endif

    {
        IntType rhs2 = 6;
        IntType index3 = 0;
        IntType index4 = 1;
        A3IType part5 = *(A2A3ITypeProject(&(aut09_ll1_), index3));
        A3ITypeModify(&part5, index4, rhs2);
        A2A3ITypeModify(&aut09_ll1_, index3, &(part5));
    }

    #if EVENT_OUTPUT
        edges_InfoEvent(e09d_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e09e".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent34(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e09e_, TRUE);
    #endif

    aut09_i_ = A3ITypeProject(&(aut09_l_), 0);

    #if EVENT_OUTPUT
        edges_InfoEvent(e09e_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e09f".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent35(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e09f_, TRUE);
    #endif

    aut09_i_ = A3ITypeProject(A2A3ITypeProject(&(aut09_ll1_), 0), 1);

    #if EVENT_OUTPUT
        edges_InfoEvent(e09f_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e09g".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent36(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e09g_, TRUE);
    #endif

    ((aut09_ll1_).data[0]).data[0] = aut09_i_;
    ((aut09_ll1_).data[0]).data[1] = aut09_j_;
    ((aut09_ll1_).data[0]).data[2] = IntegerAdd(aut09_i_, aut09_j_);
    ((aut09_ll1_).data[1]).data[0] = IntegerNegate(aut09_i_);
    ((aut09_ll1_).data[1]).data[1] = IntegerNegate(aut09_j_);
    ((aut09_ll1_).data[1]).data[2] = IntegerSubtract(IntegerNegate(aut09_i_), aut09_j_);

    #if EVENT_OUTPUT
        edges_InfoEvent(e09g_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e10a".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent37(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e10a_, TRUE);
    #endif

    aut10_x1_ = aut10_x2_;

    #if EVENT_OUTPUT
        edges_InfoEvent(e10a_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e10b".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent38(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e10b_, TRUE);
    #endif

    StringTypeCopyText(&((aut10_x1_)._field0), "abc");
    ((((aut10_x1_)._field1).data[0])._field0).data[0] = 1;
    ((((aut10_x1_)._field1).data[0])._field1).data[0] = 2.0;
    ((((aut10_x1_)._field1).data[1])._field0).data[0] = aut10_i_;
    ((((aut10_x1_)._field1).data[1])._field1).data[0] = aut10_r_;

    #if EVENT_OUTPUT
        edges_InfoEvent(e10b_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e10c".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent39(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e10c_, TRUE);
    #endif

    {
        StringType str_tmp3;
        StringTypeCopyText(&str_tmp3, "def");
        StringType rhs2 = str_tmp3;
        aut10_x1_._field0 = rhs2;
    }
    {
        A2T2A1IA1RType array_tmp3;
        (((array_tmp3).data[0])._field0).data[0] = 1;
        (((array_tmp3).data[0])._field1).data[0] = 2.0;
        (((array_tmp3).data[1])._field0).data[0] = 3;
        (((array_tmp3).data[1])._field1).data[0] = 4.0;
        A2T2A1IA1RType rhs2 = array_tmp3;
        aut10_x1_._field1 = rhs2;
    }

    #if EVENT_OUTPUT
        edges_InfoEvent(e10c_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e10d".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent40(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e10d_, TRUE);
    #endif

    {
        T2A1IA1RType tuple_tmp3;
        ((tuple_tmp3)._field0).data[0] = 4;
        ((tuple_tmp3)._field1).data[0] = 5.0;
        T2A1IA1RType rhs2 = tuple_tmp3;
        IntType index4 = 0;
        A2T2A1IA1RType part5 = (aut10_x1_)._field1;
        A2T2A1IA1RTypeModify(&part5, index4, &(rhs2));
        aut10_x1_._field1 = part5;
    }

    #if EVENT_OUTPUT
        edges_InfoEvent(e10d_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e10e".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent41(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e10e_, TRUE);
    #endif

    {
        IntType rhs2 = 5;
        IntType index3 = 0;
        IntType index4 = 0;
        A2T2A1IA1RType part5 = (aut10_x1_)._field1;
        T2A1IA1RType part6 = *(A2T2A1IA1RTypeProject(&(part5), index3));
        A1IType part7 = (part6)._field0;
        A1ITypeModify(&part7, index4, rhs2);
        part6._field0 = part7;
        A2T2A1IA1RTypeModify(&part5, index3, &(part6));
        aut10_x1_._field1 = part5;
    }

    #if EVENT_OUTPUT
        edges_InfoEvent(e10e_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e10f".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent42(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e10f_, TRUE);
    #endif

    aut10_l_ = (aut10_x1_)._field1;

    #if EVENT_OUTPUT
        edges_InfoEvent(e10f_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e10g".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent43(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e10g_, TRUE);
    #endif

    aut10_li_ = (A2T2A1IA1RTypeProject(&((aut10_x1_)._field1), 0))->_field0;

    #if EVENT_OUTPUT
        edges_InfoEvent(e10g_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e10h".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent44(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e10h_, TRUE);
    #endif

    aut10_lr_ = (A2T2A1IA1RTypeProject(&((aut10_x1_)._field1), 0))->_field1;

    #if EVENT_OUTPUT
        edges_InfoEvent(e10h_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e10i".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent45(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e10i_, TRUE);
    #endif

    aut10_i_ = A1ITypeProject(&((A2T2A1IA1RTypeProject(&((aut10_x1_)._field1), 0))->_field0), 0);
    aut10_r_ = A1RTypeProject(&((A2T2A1IA1RTypeProject(&((aut10_x1_)._field1), 0))->_field1), 0);

    #if EVENT_OUTPUT
        edges_InfoEvent(e10i_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e11a".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent46(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e11a_, TRUE);
    #endif

    if (((A3T2IITypeProject(&(aut11_v1_), 0))->_field0) == (1)) {
        IntType rhs2 = IntegerAdd((A3T2IITypeProject(&(aut11_v1_), 0))->_field0, 1);
        IntType index3 = 0;
        T2IIType part4 = *(A3T2IITypeProject(&(aut11_v1_), index3));
        part4._field0 = rhs2;
        A3T2IITypeModify(&aut11_v1_, index3, &(part4));
    } else if (((A3T2IITypeProject(&(aut11_v1_), 0))->_field0) == (2)) {
        IntType rhs2 = IntegerSubtract((A3T2IITypeProject(&(aut11_v1_), 0))->_field1, 1);
        IntType index3 = 0;
        T2IIType part4 = *(A3T2IITypeProject(&(aut11_v1_), index3));
        part4._field1 = rhs2;
        A3T2IITypeModify(&aut11_v1_, index3, &(part4));
    } else {
        IntType rhs2 = (A3T2IITypeProject(&(aut11_v1_), 2))->_field0;
        IntType index3 = 1;
        T2IIType part4 = *(A3T2IITypeProject(&(aut11_v1_), index3));
        part4._field0 = rhs2;
        A3T2IITypeModify(&aut11_v1_, index3, &(part4));
    }
    {
        IntType rhs2 = 3;
        IntType index3 = 2;
        T2IIType part4 = *(A3T2IITypeProject(&(aut11_v1_), index3));
        part4._field0 = rhs2;
        A3T2IITypeModify(&aut11_v1_, index3, &(part4));
    }

    #if EVENT_OUTPUT
        edges_InfoEvent(e11a_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e12a".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent47(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e12a_, TRUE);
    #endif

    aut12_z_ = aut12_v_();
    aut12_x_ = 1.0;
    aut12_y_ = 1.0;

    #if EVENT_OUTPUT
        edges_InfoEvent(e12a_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e12b".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent48(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e12b_, TRUE);
    #endif

    {
        RealType aut12_v_tmp2 = aut12_v_();
        aut12_x_ = aut12_v_tmp2;
        aut12_y_ = aut12_v_tmp2;
    }

    #if EVENT_OUTPUT
        edges_InfoEvent(e12b_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e12c".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent49(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e12c_, TRUE);
    #endif

    {
        RealType aut12_v_tmp2 = aut12_v_();
        aut12_td_ = aut12_w_();
        aut12_x_ = aut12_v_tmp2;
        aut12_y_ = aut12_v_tmp2;
    }

    #if EVENT_OUTPUT
        edges_InfoEvent(e12c_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e12d".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent50(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e12d_, TRUE);
    #endif

    {
        RealType aut12_t_tmp2 = aut12_t_deriv();
        aut12_x_ = aut12_t_tmp2;
        aut12_y_ = aut12_t_tmp2;
    }

    #if EVENT_OUTPUT
        edges_InfoEvent(e12d_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e12e".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent51(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e12e_, TRUE);
    #endif

    aut12_td_ = aut12_u_deriv();
    aut12_x_ = 1.0;
    aut12_y_ = 1.0;

    #if EVENT_OUTPUT
        edges_InfoEvent(e12e_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e13a".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent52(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e13a_, TRUE);
    #endif

    aut13_x_ = 1.0;

    #if EVENT_OUTPUT
        edges_InfoEvent(e13a_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e13b".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent53(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e13b_, TRUE);
    #endif

    if ((aut13_z_) == (5.0)) {
        aut13_x_ = 2.0;
    } else {
        aut13_x_ = 3.0;
    }

    #if EVENT_OUTPUT
        edges_InfoEvent(e13b_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e13c".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent54(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e13c_, TRUE);
    #endif

    if ((aut13_z_) == (5.0)) {
        aut13_x_ = 2.0;
    } else if ((aut13_z_) == (21.0)) {
        aut13_x_ = 3.0;
    }

    #if EVENT_OUTPUT
        edges_InfoEvent(e13c_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e13d".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent55(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e13d_, TRUE);
    #endif

    if ((aut13_z_) == (5.0)) {
        aut13_x_ = 2.0;
    } else if ((aut13_z_) == (21.0)) {
        aut13_x_ = 3.0;
    } else {
        aut13_x_ = 4.0;
    }

    #if EVENT_OUTPUT
        edges_InfoEvent(e13d_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e13e".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent56(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(e13e_, TRUE);
    #endif

    if ((aut13_w_()) == (4.0)) {
        aut13_x_ = 1.0;
    } else if ((aut13_v_()) == (5.0)) {
        aut13_x_ = 2.0;
    }

    #if EVENT_OUTPUT
        edges_InfoEvent(e13e_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e14a".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent57(void) {
    BoolType guard = aut14_b_;
    if (!guard) return FALSE;

    #if EVENT_OUTPUT
        edges_InfoEvent(e14a_, TRUE);
    #endif

    #if EVENT_OUTPUT
        edges_InfoEvent(e14a_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e14b".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent58(void) {
    BoolType guard = (aut14_i_) > (3);
    if (!guard) return FALSE;

    #if EVENT_OUTPUT
        edges_InfoEvent(e14b_, TRUE);
    #endif

    #if EVENT_OUTPUT
        edges_InfoEvent(e14b_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e14c".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent59(void) {
    BoolType guard = (RealAdd(aut14_r_, aut14_i_)) != (18.0);
    if (!guard) return FALSE;

    #if EVENT_OUTPUT
        edges_InfoEvent(e14c_, TRUE);
    #endif

    #if EVENT_OUTPUT
        edges_InfoEvent(e14c_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e14d".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent60(void) {
    BoolType guard = !(aut14_b_);
    if (!guard) return FALSE;

    #if EVENT_OUTPUT
        edges_InfoEvent(e14d_, TRUE);
    #endif

    #if EVENT_OUTPUT
        edges_InfoEvent(e14d_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e14e".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent61(void) {
    BoolType guard = (IntegerNegate(aut14_i_)) < (5);
    if (!guard) return FALSE;

    #if EVENT_OUTPUT
        edges_InfoEvent(e14e_, TRUE);
    #endif

    #if EVENT_OUTPUT
        edges_InfoEvent(e14e_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e14f".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent62(void) {
    BoolType guard = (RealNegate(aut14_r_)) < (6);
    if (!guard) return FALSE;

    #if EVENT_OUTPUT
        edges_InfoEvent(e14f_, TRUE);
    #endif

    #if EVENT_OUTPUT
        edges_InfoEvent(e14f_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e14g".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent63(void) {
    BoolType guard = (aut14_i_) < (7);
    if (!guard) return FALSE;

    #if EVENT_OUTPUT
        edges_InfoEvent(e14g_, TRUE);
    #endif

    #if EVENT_OUTPUT
        edges_InfoEvent(e14g_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e14h".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent64(void) {
    BoolType guard = (aut14_r_) < (8);
    if (!guard) return FALSE;

    #if EVENT_OUTPUT
        edges_InfoEvent(e14h_, TRUE);
    #endif

    #if EVENT_OUTPUT
        edges_InfoEvent(e14h_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "tau".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent65(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(EVT_TAU_, TRUE);
    #endif

    #if EVENT_OUTPUT
        edges_InfoEvent(EVT_TAU_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "tau".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent66(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(EVT_TAU_, TRUE);
    #endif

    #if EVENT_OUTPUT
        edges_InfoEvent(EVT_TAU_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "tau".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent67(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(EVT_TAU_, TRUE);
    #endif

    #if EVENT_OUTPUT
        edges_InfoEvent(EVT_TAU_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "tau".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent68(void) {
    #if EVENT_OUTPUT
        edges_InfoEvent(EVT_TAU_, TRUE);
    #endif

    #if EVENT_OUTPUT
        edges_InfoEvent(EVT_TAU_, FALSE);
    #endif
    return TRUE;
}

/**
 * Normalize and check the new value of a continuous variable after an update.
 * @param new_value Unnormalized new value of the continuous variable.
 * @param var_name Name of the continuous variable in the CIF model.
 * @return The normalized new value of the continuous variable.
 */
static RealType UpdateContValue(RealType new_value, const char *var_name, BoolType ok) {
    if (ok) {
        return (new_value == -0.0) ? 0.0 : new_value;
    }
    fprintf(stderr, "Continuous variable \"%s\" has become %.1f.\n", var_name, new_value);

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

        if (execEvent0()) continue;  /* (Try to) perform event "e02a". */
        if (execEvent1()) continue;  /* (Try to) perform event "e02b". */
        if (execEvent2()) continue;  /* (Try to) perform event "e03a". */
        if (execEvent3()) continue;  /* (Try to) perform event "e03b". */
        if (execEvent4()) continue;  /* (Try to) perform event "e04a". */
        if (execEvent5()) continue;  /* (Try to) perform event "e04b". */
        if (execEvent6()) continue;  /* (Try to) perform event "e04c". */
        if (execEvent7()) continue;  /* (Try to) perform event "e04d". */
        if (execEvent8()) continue;  /* (Try to) perform event "e04e". */
        if (execEvent9()) continue;  /* (Try to) perform event "e04f". */
        if (execEvent10()) continue;  /* (Try to) perform event "e05a". */
        if (execEvent11()) continue;  /* (Try to) perform event "e05b". */
        if (execEvent12()) continue;  /* (Try to) perform event "e05c". */
        if (execEvent13()) continue;  /* (Try to) perform event "e05d". */
        if (execEvent14()) continue;  /* (Try to) perform event "e05e". */
        if (execEvent15()) continue;  /* (Try to) perform event "e06a". */
        if (execEvent16()) continue;  /* (Try to) perform event "e06b". */
        if (execEvent17()) continue;  /* (Try to) perform event "e06c". */
        if (execEvent18()) continue;  /* (Try to) perform event "e06d". */
        if (execEvent19()) continue;  /* (Try to) perform event "e06e". */
        if (execEvent20()) continue;  /* (Try to) perform event "e07a". */
        if (execEvent21()) continue;  /* (Try to) perform event "e07b". */
        if (execEvent22()) continue;  /* (Try to) perform event "e08a". */
        if (execEvent23()) continue;  /* (Try to) perform event "e08b". */
        if (execEvent24()) continue;  /* (Try to) perform event "e08c". */
        if (execEvent25()) continue;  /* (Try to) perform event "e08d". */
        if (execEvent26()) continue;  /* (Try to) perform event "e08e". */
        if (execEvent27()) continue;  /* (Try to) perform event "e08f". */
        if (execEvent28()) continue;  /* (Try to) perform event "e08g". */
        if (execEvent29()) continue;  /* (Try to) perform event "e08h". */
        if (execEvent30()) continue;  /* (Try to) perform event "e09a". */
        if (execEvent31()) continue;  /* (Try to) perform event "e09b". */
        if (execEvent32()) continue;  /* (Try to) perform event "e09c". */
        if (execEvent33()) continue;  /* (Try to) perform event "e09d". */
        if (execEvent34()) continue;  /* (Try to) perform event "e09e". */
        if (execEvent35()) continue;  /* (Try to) perform event "e09f". */
        if (execEvent36()) continue;  /* (Try to) perform event "e09g". */
        if (execEvent37()) continue;  /* (Try to) perform event "e10a". */
        if (execEvent38()) continue;  /* (Try to) perform event "e10b". */
        if (execEvent39()) continue;  /* (Try to) perform event "e10c". */
        if (execEvent40()) continue;  /* (Try to) perform event "e10d". */
        if (execEvent41()) continue;  /* (Try to) perform event "e10e". */
        if (execEvent42()) continue;  /* (Try to) perform event "e10f". */
        if (execEvent43()) continue;  /* (Try to) perform event "e10g". */
        if (execEvent44()) continue;  /* (Try to) perform event "e10h". */
        if (execEvent45()) continue;  /* (Try to) perform event "e10i". */
        if (execEvent46()) continue;  /* (Try to) perform event "e11a". */
        if (execEvent47()) continue;  /* (Try to) perform event "e12a". */
        if (execEvent48()) continue;  /* (Try to) perform event "e12b". */
        if (execEvent49()) continue;  /* (Try to) perform event "e12c". */
        if (execEvent50()) continue;  /* (Try to) perform event "e12d". */
        if (execEvent51()) continue;  /* (Try to) perform event "e12e". */
        if (execEvent52()) continue;  /* (Try to) perform event "e13a". */
        if (execEvent53()) continue;  /* (Try to) perform event "e13b". */
        if (execEvent54()) continue;  /* (Try to) perform event "e13c". */
        if (execEvent55()) continue;  /* (Try to) perform event "e13d". */
        if (execEvent56()) continue;  /* (Try to) perform event "e13e". */
        if (execEvent57()) continue;  /* (Try to) perform event "e14a". */
        if (execEvent58()) continue;  /* (Try to) perform event "e14b". */
        if (execEvent59()) continue;  /* (Try to) perform event "e14c". */
        if (execEvent60()) continue;  /* (Try to) perform event "e14d". */
        if (execEvent61()) continue;  /* (Try to) perform event "e14e". */
        if (execEvent62()) continue;  /* (Try to) perform event "e14f". */
        if (execEvent63()) continue;  /* (Try to) perform event "e14g". */
        if (execEvent64()) continue;  /* (Try to) perform event "e14h". */
        if (execEvent65()) continue;  /* (Try to) perform event "tau". */
        if (execEvent66()) continue;  /* (Try to) perform event "tau". */
        if (execEvent67()) continue;  /* (Try to) perform event "tau". */
        if (execEvent68()) continue;  /* (Try to) perform event "tau". */
        break; /* No event fired, done with discrete steps. */
    }
}

/** First model call, initializing, and performing discrete events before the first time step. */
void edges_EngineFirstStep(void) {
    InitConstants();

    model_time = 0.0;
    edges_AssignInputVariables();
    aut02_x_ = 0;
    aut02_ = _edges_loc1;
    aut03_c_ = 0.0;
    aut03_d_ = 0;
    aut04_a_ = 0;
    aut04_b_ = 0;
    aut04_c_ = 0;
    aut04_d_ = 0;
    (aut05_v1_).data[0] = 0;
    (aut05_v1_).data[1] = 0;
    (aut05_v1_).data[2] = 0;
    (aut05_v1_).data[3] = 0;
    (aut05_v1_).data[4] = 0;
    (aut05_v2_).data[0] = 0;
    (aut05_v2_).data[1] = 0;
    (aut05_v2_).data[2] = 0;
    (aut05_v2_).data[3] = 0;
    (aut05_v2_).data[4] = 0;
    (aut06_v1_)._field0 = 0;
    (aut06_v1_)._field1 = 0;
    (aut06_v2_)._field0 = 0;
    (aut06_v2_)._field1 = 0;
    aut06_x_ = 0;
    aut06_y_ = 0;
    aut07_x_ = 0.0;
    aut07_y_ = 0.0;
    ((aut08_tt1_)._field0)._field0 = 0;
    ((aut08_tt1_)._field0)._field1 = 0;
    StringTypeCopyText(&((aut08_tt1_)._field1), "");
    ((aut08_tt2_)._field0)._field0 = 0;
    ((aut08_tt2_)._field0)._field1 = 0;
    StringTypeCopyText(&((aut08_tt2_)._field1), "");
    (aut08_t_)._field0 = 0;
    (aut08_t_)._field1 = 0;
    aut08_i_ = 0;
    aut08_j_ = 0;
    StringTypeCopyText(&(aut08_s_), "");
    ((aut09_ll1_).data[0]).data[0] = 0;
    ((aut09_ll1_).data[0]).data[1] = 0;
    ((aut09_ll1_).data[0]).data[2] = 0;
    ((aut09_ll1_).data[1]).data[0] = 0;
    ((aut09_ll1_).data[1]).data[1] = 0;
    ((aut09_ll1_).data[1]).data[2] = 0;
    ((aut09_ll2_).data[0]).data[0] = 0;
    ((aut09_ll2_).data[0]).data[1] = 0;
    ((aut09_ll2_).data[0]).data[2] = 0;
    ((aut09_ll2_).data[1]).data[0] = 0;
    ((aut09_ll2_).data[1]).data[1] = 0;
    ((aut09_ll2_).data[1]).data[2] = 0;
    (aut09_l_).data[0] = 0;
    (aut09_l_).data[1] = 0;
    (aut09_l_).data[2] = 0;
    aut09_i_ = 0;
    aut09_j_ = 0;
    StringTypeCopyText(&((aut10_x1_)._field0), "");
    ((((aut10_x1_)._field1).data[0])._field0).data[0] = 0;
    ((((aut10_x1_)._field1).data[0])._field1).data[0] = 0.0;
    ((((aut10_x1_)._field1).data[1])._field0).data[0] = 0;
    ((((aut10_x1_)._field1).data[1])._field1).data[0] = 0.0;
    StringTypeCopyText(&((aut10_x2_)._field0), "");
    ((((aut10_x2_)._field1).data[0])._field0).data[0] = 0;
    ((((aut10_x2_)._field1).data[0])._field1).data[0] = 0.0;
    ((((aut10_x2_)._field1).data[1])._field0).data[0] = 0;
    ((((aut10_x2_)._field1).data[1])._field1).data[0] = 0.0;
    (((aut10_l_).data[0])._field0).data[0] = 0;
    (((aut10_l_).data[0])._field1).data[0] = 0.0;
    (((aut10_l_).data[1])._field0).data[0] = 0;
    (((aut10_l_).data[1])._field1).data[0] = 0.0;
    (aut10_li_).data[0] = 0;
    (aut10_lr_).data[0] = 0.0;
    aut10_i_ = 0;
    aut10_r_ = 0.0;
    ((aut11_v1_).data[0])._field0 = 0;
    ((aut11_v1_).data[0])._field1 = 0;
    ((aut11_v1_).data[1])._field0 = 0;
    ((aut11_v1_).data[1])._field1 = 0;
    ((aut11_v1_).data[2])._field0 = 0;
    ((aut11_v1_).data[2])._field1 = 0;
    aut12_x_ = 0.0;
    aut12_y_ = 0.0;
    aut12_z_ = 0.0;
    aut12_td_ = 0.0;
    aut12_t_ = 0.0;
    aut12_u_ = 0.0;
    aut13_x_ = 0.0;
    aut13_y_ = 0.0;
    aut13_z_ = 0.0;

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
void edges_EngineTimeStep(double delta) {
    edges_AssignInputVariables();

    /* Update continuous variables. */
    if (delta > 0.0) {
        RealType deriv0 = aut03_c_deriv();
        RealType deriv1 = aut07_x_deriv();
        RealType deriv2 = aut07_y_deriv();
        RealType deriv3 = aut12_t_deriv();
        RealType deriv4 = aut12_u_deriv();

        errno = 0;
        aut03_c_ = UpdateContValue(aut03_c_ + delta * deriv0, "aut03.c", errno == 0);
        errno = 0;
        aut07_x_ = UpdateContValue(aut07_x_ + delta * deriv1, "aut07.x", errno == 0);
        errno = 0;
        aut07_y_ = UpdateContValue(aut07_y_ + delta * deriv2, "aut07.y", errno == 0);
        errno = 0;
        aut12_t_ = UpdateContValue(aut12_t_ + delta * deriv3, "aut12.t", errno == 0);
        errno = 0;
        aut12_u_ = UpdateContValue(aut12_u_ + delta * deriv4, "aut12.u", errno == 0);
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

