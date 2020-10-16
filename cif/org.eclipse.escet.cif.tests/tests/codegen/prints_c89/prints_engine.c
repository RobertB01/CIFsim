/* CIF to C translation of prints.cif
 * Generated file, DO NOT EDIT
 */

#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include "prints_engine.h"

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
int EnumTypePrint(printsEnum value, char *dest, int start, int end) {
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

/**
 * Compare two tuples for equality.
 * @param left First tuple to compare.
 * @param right Second tuple to compare.
 * @return Whether both tuples are the same.
 */
BoolType T3IBSTypeEquals(T3IBSType *left, T3IBSType *right) {
    if (left == right) return TRUE;
    if (memcmp(&left->_field0, &right->_field0, sizeof(IntType)) != 0) return FALSE;
    if (memcmp(&left->_field1, &right->_field1, sizeof(BoolType)) != 0) return FALSE;
    if (!(StringTypeEquals(&(left->_field2), &(right->_field2)))) return FALSE;
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
int T3IBSTypePrint(T3IBSType *tuple, char *dest, int start, int end) {
    int last = end - 1;
    if (start < last) { dest[start++] = '('; }
    start = IntTypePrint(tuple->_field0, dest, start, end);
    if (start < last) { dest[start++] = ','; }
    if (start < last) { dest[start++] = ' '; }
    start = BoolTypePrint(tuple->_field1, dest, start, end);
    if (start < last) { dest[start++] = ','; }
    if (start < last) { dest[start++] = ' '; }
    start = StringTypePrintEscaped(&tuple->_field2, dest, start, end);
    if (start < last) { dest[start++] = ')'; }
    dest[start] = '\0';
    return start;
}


/** Event names. */
const char *prints_event_names[] = {
    "initial-step", /**< Initial step. */
    "delay-step",   /**< Delay step. */
    "tau",          /**< Tau step. */
    "e1",           /**< Event e1. */
    "e2",           /**< Event e2. */
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


/* State variables. */
printsEnum a1_; /**< Discrete variable "E a1". */

/* Derivative and algebraic variable functions. */



RealType model_time; /**< Current model time. */

/** Initialize constants. */
static void InitConstants(void) {

}

/** Print function. */
#if PRINT_OUTPUT
static void PrintOutput(prints_Event_ event, BoolType pre) {
    StringType text_var1;

    if (pre) {
        StringTypeCopyText(&(text_var1), "bla19");
        prints_PrintOutput(text_var1.data, ":stdout");

        StringTypeCopyText(&(text_var1), "bla21");
        prints_PrintOutput(text_var1.data, ":stdout");

        if ((model_time) > (5)) {
            StringTypeCopyText(&(text_var1), "bla25");
            prints_PrintOutput(text_var1.data, ":stdout");
        }
    } else {
        StringTypeCopyText(&(text_var1), "bla5");
        prints_PrintOutput(text_var1.data, ":stdout");

        StringTypeCopyText(&(text_var1), "bla6");
        prints_PrintOutput(text_var1.data, "e.txt");

        StringTypeCopyText(&(text_var1), "bla7");
        prints_PrintOutput(text_var1.data, ":stdout");

        StringTypeCopyText(&(text_var1), "bla8");
        prints_PrintOutput(text_var1.data, ":stderr");

        StringTypeCopyText(&(text_var1), "bla9");
        prints_PrintOutput(text_var1.data, ":stdout");

        if (event == EVT_INITIAL_) {
            StringTypeCopyText(&(text_var1), "bla10");
            prints_PrintOutput(text_var1.data, ":stdout");
        }

        if (event >= EVT_TAU_) {
            StringTypeCopyText(&(text_var1), "bla12");
            prints_PrintOutput(text_var1.data, ":stdout");
        }

        if (event == EVT_DELAY_) {
            StringTypeCopyText(&(text_var1), "bla13");
            prints_PrintOutput(text_var1.data, ":stdout");
        }

        if (event == e1_) {
            StringTypeCopyText(&(text_var1), "bla14");
            prints_PrintOutput(text_var1.data, ":stdout");
        }

        if (event == e2_) {
            StringTypeCopyText(&(text_var1), "bla15");
            prints_PrintOutput(text_var1.data, ":stdout");
        }

        if (event == e1_ || event == e2_) {
            StringTypeCopyText(&(text_var1), "bla16");
            prints_PrintOutput(text_var1.data, ":stdout");
        }

        if (event == EVT_INITIAL_ || event >= EVT_TAU_ || event == EVT_DELAY_) {
            StringTypeCopyText(&(text_var1), "bla17");
            prints_PrintOutput(text_var1.data, ":stdout");
        }

        StringTypeCopyText(&(text_var1), "bla18");
        prints_PrintOutput(text_var1.data, ":stdout");

        StringTypeCopyText(&(text_var1), "bla20");
        prints_PrintOutput(text_var1.data, ":stdout");

        StringTypeCopyText(&(text_var1), "bla22");
        prints_PrintOutput(text_var1.data, ":stdout");

        if ((model_time) > (5)) {
            StringTypeCopyText(&(text_var1), "bla24");
            prints_PrintOutput(text_var1.data, ":stdout");
        }

        if ((model_time) > (5)) {
            StringTypeCopyText(&(text_var1), "bla28");
            prints_PrintOutput(text_var1.data, ":stdout");
        }

        BoolTypePrint(TRUE, text_var1.data, 0, MAX_STRING_SIZE);
        prints_PrintOutput(text_var1.data, ":stdout");

        IntTypePrint(123, text_var1.data, 0, MAX_STRING_SIZE);
        prints_PrintOutput(text_var1.data, ":stdout");

        RealTypePrint(1.23, text_var1.data, 0, MAX_STRING_SIZE);
        prints_PrintOutput(text_var1.data, ":stdout");

        StringTypeCopyText(&(text_var1), "a\nbc");
        prints_PrintOutput(text_var1.data, ":stdout");

        EnumTypePrint(_prints_A, text_var1.data, 0, MAX_STRING_SIZE);
        prints_PrintOutput(text_var1.data, ":stdout");

        EnumTypePrint(_prints_B, text_var1.data, 0, MAX_STRING_SIZE);
        prints_PrintOutput(text_var1.data, ":stdout");

        A3IType array_tmp2;
        (array_tmp2).data[0] = 1;
        (array_tmp2).data[1] = 3;
        (array_tmp2).data[2] = 2;
        A3ITypePrint(&(array_tmp2), text_var1.data, 0, MAX_STRING_SIZE);
        prints_PrintOutput(text_var1.data, ":stdout");

        A2SType array_tmp3;
        StringTypeCopyText(&((array_tmp3).data[0]), "a\nbc");
        StringTypeCopyText(&((array_tmp3).data[1]), "def");
        A2STypePrint(&(array_tmp3), text_var1.data, 0, MAX_STRING_SIZE);
        prints_PrintOutput(text_var1.data, ":stdout");

        T3IBSType tuple_tmp4;
        (tuple_tmp4)._field0 = 1;
        (tuple_tmp4)._field1 = TRUE;
        StringTypeCopyText(&((tuple_tmp4)._field2), "a");
        T3IBSTypePrint(&(tuple_tmp4), text_var1.data, 0, MAX_STRING_SIZE);
        prints_PrintOutput(text_var1.data, ":stdout");

        StringTypeCopyText(&(text_var1), "bla3");
        prints_PrintOutput(text_var1.data, "c.txt");

        StringTypeCopyText(&(text_var1), "bla4");
        prints_PrintOutput(text_var1.data, "d.txt");

        StringTypeCopyText(&(text_var1), "bla1");
        prints_PrintOutput(text_var1.data, "a.txt");

        StringTypeCopyText(&(text_var1), "bla2");
        prints_PrintOutput(text_var1.data, "b.txt");
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


        break; /* No event fired, done with discrete steps. */
    }
}

/** First model call, initializing, and performing discrete events before the first time step. */
void prints_EngineFirstStep(void) {
    InitConstants();

    model_time = 0.0;

    a1_ = _prints_X;

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
void prints_EngineTimeStep(double delta) {


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

