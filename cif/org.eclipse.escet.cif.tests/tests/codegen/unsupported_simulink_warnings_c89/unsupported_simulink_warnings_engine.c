/* CIF to C translation of unsupported_simulink_warnings.cif
 * Generated file, DO NOT EDIT
 */

#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include "unsupported_simulink_warnings_engine.h"

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
 * Compare two arrays for equality.
 * @param left First array to compare.
 * @param right Second array to compare.
 * @return Whether both arrays are the same.
 */
BoolType A1T2IITypeEquals(A1T2IIType *left, A1T2IIType *right) {
    if (left == right) return TRUE;
    int i;
    for (i = 0; i < 1; i++) {
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
T2IIType *A1T2IITypeProject(A1T2IIType *array, IntType index) {
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
void A1T2IITypeModify(A1T2IIType *array, IntType index, T2IIType *value) {
    if (index < 0) index += 1; /* Normalize index. */
    assert(index >= 0 && index < 1);

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
int A1T2IITypePrint(A1T2IIType *array, char *dest, int start, int end) {
    int last = end - 1;
    if (start < last) { dest[start++] = '['; }
    int index;
    for (index = 0; index < 1; index++) {
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
BoolType A1A1T2IITypeEquals(A1A1T2IIType *left, A1A1T2IIType *right) {
    if (left == right) return TRUE;
    int i;
    for (i = 0; i < 1; i++) {
        if (!(A1T2IITypeEquals(&(left->data[i]), &(right->data[i])))) return FALSE;
    }
    return TRUE;
}

/**
 * Extract an element from the array.
 * @param array Array with value to retrieve.
 * @param index Element index in the array (not normalized).
 * @return Element value at the indicated index from the array.
 */
A1T2IIType *A1A1T2IITypeProject(A1A1T2IIType *array, IntType index) {
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
void A1A1T2IITypeModify(A1A1T2IIType *array, IntType index, A1T2IIType *value) {
    if (index < 0) index += 1; /* Normalize index. */
    assert(index >= 0 && index < 1);

    memcpy(&array->data[index], value, sizeof(A1T2IIType));
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
int A1A1T2IITypePrint(A1A1T2IIType *array, char *dest, int start, int end) {
    int last = end - 1;
    if (start < last) { dest[start++] = '['; }
    int index;
    for (index = 0; index < 1; index++) {
        if (index > 0) {
            if (start < last) { dest[start++] = ','; }
            if (start < last) { dest[start++] = ' '; }
        }
        start = A1T2IITypePrint(&array->data[index], dest, start, end);
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
BoolType A1A1STypeEquals(A1A1SType *left, A1A1SType *right) {
    if (left == right) return TRUE;
    int i;
    for (i = 0; i < 1; i++) {
        if (!(A1STypeEquals(&(left->data[i]), &(right->data[i])))) return FALSE;
    }
    return TRUE;
}

/**
 * Extract an element from the array.
 * @param array Array with value to retrieve.
 * @param index Element index in the array (not normalized).
 * @return Element value at the indicated index from the array.
 */
A1SType *A1A1STypeProject(A1A1SType *array, IntType index) {
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
void A1A1STypeModify(A1A1SType *array, IntType index, A1SType *value) {
    if (index < 0) index += 1; /* Normalize index. */
    assert(index >= 0 && index < 1);

    memcpy(&array->data[index], value, sizeof(A1SType));
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
int A1A1STypePrint(A1A1SType *array, char *dest, int start, int end) {
    int last = end - 1;
    if (start < last) { dest[start++] = '['; }
    int index;
    for (index = 0; index < 1; index++) {
        if (index > 0) {
            if (start < last) { dest[start++] = ','; }
            if (start < last) { dest[start++] = ' '; }
        }
        start = A1STypePrint(&array->data[index], dest, start, end);
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
BoolType A1BTypeEquals(A1BType *left, A1BType *right) {
    if (left == right) return TRUE;
    return memcmp(left, right, sizeof(A1BType)) == 0;
}

/**
 * Extract an element from the array.
 * @param array Array with value to retrieve.
 * @param index Element index in the array (not normalized).
 * @return Element value at the indicated index from the array.
 */
BoolType A1BTypeProject(A1BType *array, IntType index) {
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
void A1BTypeModify(A1BType *array, IntType index, BoolType value) {
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
int A1BTypePrint(A1BType *array, char *dest, int start, int end) {
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
 * Compare two arrays for equality.
 * @param left First array to compare.
 * @param right Second array to compare.
 * @return Whether both arrays are the same.
 */
BoolType A1A1BTypeEquals(A1A1BType *left, A1A1BType *right) {
    if (left == right) return TRUE;
    return memcmp(left, right, sizeof(A1A1BType)) == 0;
}

/**
 * Extract an element from the array.
 * @param array Array with value to retrieve.
 * @param index Element index in the array (not normalized).
 * @return Element value at the indicated index from the array.
 */
A1BType *A1A1BTypeProject(A1A1BType *array, IntType index) {
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
void A1A1BTypeModify(A1A1BType *array, IntType index, A1BType *value) {
    if (index < 0) index += 1; /* Normalize index. */
    assert(index >= 0 && index < 1);

    memcpy(&array->data[index], value, sizeof(A1BType));
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
int A1A1BTypePrint(A1A1BType *array, char *dest, int start, int end) {
    int last = end - 1;
    if (start < last) { dest[start++] = '['; }
    int index;
    for (index = 0; index < 1; index++) {
        if (index > 0) {
            if (start < last) { dest[start++] = ','; }
            if (start < last) { dest[start++] = ' '; }
        }
        start = A1BTypePrint(&array->data[index], dest, start, end);
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
BoolType A1A1A1BTypeEquals(A1A1A1BType *left, A1A1A1BType *right) {
    if (left == right) return TRUE;
    return memcmp(left, right, sizeof(A1A1A1BType)) == 0;
}

/**
 * Extract an element from the array.
 * @param array Array with value to retrieve.
 * @param index Element index in the array (not normalized).
 * @return Element value at the indicated index from the array.
 */
A1A1BType *A1A1A1BTypeProject(A1A1A1BType *array, IntType index) {
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
void A1A1A1BTypeModify(A1A1A1BType *array, IntType index, A1A1BType *value) {
    if (index < 0) index += 1; /* Normalize index. */
    assert(index >= 0 && index < 1);

    memcpy(&array->data[index], value, sizeof(A1A1BType));
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
int A1A1A1BTypePrint(A1A1A1BType *array, char *dest, int start, int end) {
    int last = end - 1;
    if (start < last) { dest[start++] = '['; }
    int index;
    for (index = 0; index < 1; index++) {
        if (index > 0) {
            if (start < last) { dest[start++] = ','; }
            if (start < last) { dest[start++] = ' '; }
        }
        start = A1A1BTypePrint(&array->data[index], dest, start, end);
    }
    if (start < last) { dest[start++] = ']'; }
    dest[start] = '\0';
    return start;
}

int EnumTypePrint(unsupported_simulink_warningsEnum value, char *dest, int start, int end) {
    int last = end - 1;
    const char *lit_name = enum_names[value];
    while (start < last && *lit_name) {
        dest[start++] = *lit_name;
        lit_name++;
    }
    dest[start] = '\0';
    return start;
}


/** Event names. */
const char *unsupported_simulink_warnings_event_names[] = {
    "initial-step", /**< Initial step. */
    "delay-step",   /**< Delay step. */
    "tau",          /**< Tau step. */
    "a.e",          /**< Event "a.e". */
};

/** Enumeration names. */
const char *enum_names[] = {
    "__some_dummy_enum_literal",
};

/* Constants. */


/* Functions. */


/* Input variables. */


/* State variables. */

/** Discrete variable "tuple(int x; int y) a.d1". */
T2IIType a_d1_;

/** Discrete variable "string a.d2". */
StringType a_d2_;

/** Discrete variable "list[1] tuple(int x; int y) a.d3". */
A1T2IIType a_d3_;

/** Discrete variable "list[1] string a.d4". */
A1SType a_d4_;

/** Discrete variable "list[1] list[1] tuple(int x; int y) a.d5". */
A1A1T2IIType a_d5_;

/** Discrete variable "list[1] list[1] string a.d6". */
A1A1SType a_d6_;

/** Discrete variable "list[1] list[1] list[1] bool a.d7". */
A1A1A1BType a_d7_;

/* Derivative and algebraic variable functions. */

/** Algebraic variable a1 = (1, 2). */
T2IIType a1_(void) {
    T2IIType tuple_tmp1;
    (tuple_tmp1)._field0 = 1;
    (tuple_tmp1)._field1 = 2;
    return tuple_tmp1;
}

/** Algebraic variable a2 = "a". */
StringType a2_(void) {
    StringType str_tmp2;
    StringTypeCopyText(&str_tmp2, "a");
    return str_tmp2;
}

/** Algebraic variable a3 = [(1, 2)]. */
A1T2IIType a3_(void) {
    A1T2IIType array_tmp3;
    ((array_tmp3).data[0])._field0 = 1;
    ((array_tmp3).data[0])._field1 = 2;
    return array_tmp3;
}

/** Algebraic variable a4 = ["a"]. */
A1SType a4_(void) {
    A1SType array_tmp4;
    StringTypeCopyText(&((array_tmp4).data[0]), "a");
    return array_tmp4;
}

/** Algebraic variable a5 = [[(1, 2)]]. */
A1A1T2IIType a5_(void) {
    A1A1T2IIType array_tmp5;
    (((array_tmp5).data[0]).data[0])._field0 = 1;
    (((array_tmp5).data[0]).data[0])._field1 = 2;
    return array_tmp5;
}

/** Algebraic variable a6 = [["a"]]. */
A1A1SType a6_(void) {
    A1A1SType array_tmp6;
    StringTypeCopyText(&(((array_tmp6).data[0]).data[0]), "a");
    return array_tmp6;
}

/** Algebraic variable a7 = [[[true]]]. */
A1A1A1BType a7_(void) {
    A1A1A1BType array_tmp7;
    (((array_tmp7).data[0]).data[0]).data[0] = TRUE;
    return array_tmp7;
}

RealType model_time; /**< Current model time. */

/** Initialize constants. */
static void InitConstants(void) {

}

/** Print function. */
#if PRINT_OUTPUT
static void PrintOutput(unsupported_simulink_warnings_Event_ event, BoolType pre) {
}
#endif

/* Event execution code. */

/**
 * Execute code for event "a.e".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent0(void) {
    BoolType guard = ((((a_d1_)._field0) > (0)) || (((StringTypeSize(&(a_d2_))) > (0)) || (((A1T2IITypeProject(&(a_d3_), 0))->_field0) > (0)))) || ((((StringTypeSize(A1STypeProject(&(a_d4_), 0))) > (0)) || (((A1T2IITypeProject(A1A1T2IITypeProject(&(a_d5_), 0), 0))->_field0) > (0))) || (((StringTypeSize(A1STypeProject(A1A1STypeProject(&(a_d6_), 0), 0))) > (0)) || (A1BTypeProject(A1A1BTypeProject(A1A1A1BTypeProject(&(a_d7_), 0), 0), 0))));
    if (!guard) return FALSE;

    #if EVENT_OUTPUT
        unsupported_simulink_warnings_InfoEvent(a_e_, TRUE);
    #endif

    #if EVENT_OUTPUT
        unsupported_simulink_warnings_InfoEvent(a_e_, FALSE);
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

        if (execEvent0()) continue;  /* (Try to) perform event "a.e". */
        break; /* No event fired, done with discrete steps. */
    }
}

/** First model call, initializing, and performing discrete events before the first time step. */
void unsupported_simulink_warnings_EngineFirstStep(void) {
    InitConstants();

    model_time = 0.0;

    (a_d1_)._field0 = 0;
    (a_d1_)._field1 = 0;
    StringTypeCopyText(&(a_d2_), "");
    ((a_d3_).data[0])._field0 = 0;
    ((a_d3_).data[0])._field1 = 0;
    StringTypeCopyText(&((a_d4_).data[0]), "");
    (((a_d5_).data[0]).data[0])._field0 = 0;
    (((a_d5_).data[0]).data[0])._field1 = 0;
    StringTypeCopyText(&(((a_d6_).data[0]).data[0]), "");
    (((a_d7_).data[0]).data[0]).data[0] = FALSE;

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
void unsupported_simulink_warnings_EngineTimeStep(double delta) {


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

