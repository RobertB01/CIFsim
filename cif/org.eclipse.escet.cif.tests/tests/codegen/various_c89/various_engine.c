/* CIF to C translation of various.cif
 * Generated file, DO NOT EDIT
 */

#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include "various_engine.h"

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
 * Compare two arrays for equality.
 * @param left First array to compare.
 * @param right Second array to compare.
 * @return Whether both arrays are the same.
 */
BoolType A2ITypeEquals(A2IType *left, A2IType *right) {
    if (left == right) return TRUE;
    return memcmp(left, right, sizeof(A2IType)) == 0;
}

/**
 * Extract an element from the array.
 * @param array Array with value to retrieve.
 * @param index Element index in the array (not normalized).
 * @return Element value at the indicated index from the array.
 */
IntType A2ITypeProject(A2IType *array, IntType index) {
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
void A2ITypeModify(A2IType *array, IntType index, IntType value) {
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
int A2ITypePrint(A2IType *array, char *dest, int start, int end) {
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

int EnumTypePrint(variousEnum value, char *dest, int start, int end) {
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


/** Event names. */
const char *various_event_names[] = {
    "initial-step", /**< Initial step. */
    "delay-step",   /**< Delay step. */
    "e1",           /**< Event "e1". */
    "g.h1",         /**< Event "g.h1". */
    "a.e",          /**< Event "a.e". */
};

/** Enumeration names. */
const char *enum_names[] = {
    /** Literal "l1". */
    "l1",

    /** Literal "l2". */
    "l2",
};

/* Constants. */


/* Functions. */

/**
 * Function "inc".
 *
 * @param inc_x_ Function parameter "inc.x".
 * @return The return value of the function.
 */
IntType inc_(IntType inc_x_) {
    /* Execute statements in the function body. */
    return IntegerAdd(inc_x_, 1);
    assert(0); /* Falling through the end of the function. */
}

/* Input variables. */

/** Input variable "int x". */
IntType x_;

/** Input variable "int y". */
IntType y_;

/** Input variable "list[3] int[0..5] input_li". */
A3IType input_li_;

/* State variables. */

/** Discrete variable "list[2] int[0..3] a.li". */
A2IType a_li_;

/** Discrete variable "int[2..5] a.x". */
IntType a_x_;

/** Discrete variable "int g.rcv.v". */
IntType g_rcv_v_;

/** Discrete variable "int g.rcv.v2". */
IntType g_rcv_v2_;

/** Discrete variable "int g.snd.a". */
IntType g_snd_a_;

/** Continuous variable "real g.sync.c". */
RealType g_sync_c_;

/** Discrete variable "E g.sync". */
variousEnum g_sync_;

/* Derivative and algebraic variable functions. */
/** Derivative of "g.sync.c". */
RealType g_sync_c_deriv(void) {
    return 1.0;
}
/** Algebraic variable z = x + y. */
IntType z_(void) {
    return IntegerAdd(x_, y_);
}

RealType model_time; /**< Current model time. */

/** Initialize constants. */
static void InitConstants(void) {

}

/** Print function. */
#if PRINT_OUTPUT
static void PrintOutput(various_Event_ event, BoolType pre) {
    StringType text_var1;

    if (pre) {
        RealTypePrint(model_time, text_var1.data, 0, MAX_STRING_SIZE);
        various_PrintOutput(text_var1.data, ":stdout");
    } else {
        RealTypePrint(g_sync_c_, text_var1.data, 0, MAX_STRING_SIZE);
        various_PrintOutput(text_var1.data, ":stdout");

        if (event == g_h1_) {
            RealTypePrint(model_time, text_var1.data, 0, MAX_STRING_SIZE);
            various_PrintOutput(text_var1.data, ":stdout");
        }

        if (event == g_h1_) {
            IntTypePrint(g_rcv_v_, text_var1.data, 0, MAX_STRING_SIZE);
            various_PrintOutput(text_var1.data, ":stdout");
        }

        StringTypeCopyText(&(text_var1), "out");
        various_PrintOutput(text_var1.data, ":stdout");

        StringTypeCopyText(&(text_var1), "err");
        various_PrintOutput(text_var1.data, ":stderr");

        StringTypeCopyText(&(text_var1), "file");
        various_PrintOutput(text_var1.data, ".\\some_file.txt");
    }
}
#endif

/* Edge execution code. */

/**
 * Execute code for edge with index 0 and event "a.e".
 *
 * @return Whether the edge was performed.
 */
static BoolType execEdge0(void) {
    BoolType guard = FALSE;
    if (!guard) return FALSE;

    #if PRINT_OUTPUT
        PrintOutput(a_e_, TRUE);
    #endif
    #if EVENT_OUTPUT
        various_InfoEvent(a_e_, TRUE);
    #endif

    {
        IntType rhs2 = a_x_;
        IntType index3 = 0;
        #if CHECK_RANGES
        if ((rhs2) > 3) {
            fprintf(stderr, "RangeError: Writing %d into \"list[2] int[0..3]\"\n", rhs2);
            fprintf(stderr, "            at " "a.li" "[%d]" "\n", index3);
            RangeErrorDetected();
        }
        #endif
        A2ITypeModify(&a_li_, index3, rhs2);
    }

    #if EVENT_OUTPUT
        various_InfoEvent(a_e_, FALSE);
    #endif
    #if PRINT_OUTPUT
        PrintOutput(a_e_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for edge with index 1 and event "e1".
 *
 * @return Whether the edge was performed.
 */
static BoolType execEdge1(void) {
    BoolType guard = (g_sync_) == (_various_l1);
    if (!guard) return FALSE;

    #if PRINT_OUTPUT
        PrintOutput(e1_, TRUE);
    #endif
    #if EVENT_OUTPUT
        various_InfoEvent(e1_, TRUE);
    #endif

    g_sync_ = _various_l2;

    #if EVENT_OUTPUT
        various_InfoEvent(e1_, FALSE);
    #endif
    #if PRINT_OUTPUT
        PrintOutput(e1_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for edge with index 2 and event "g.h1".
 *
 * @return Whether the edge was performed.
 */
static BoolType execEdge2(void) {
    BoolType guard = ((g_sync_) == (_various_l2)) && ((g_sync_c_) >= (2));
    if (!guard) return FALSE;

    #if PRINT_OUTPUT
        PrintOutput(g_h1_, TRUE);
    #endif
    #if EVENT_OUTPUT
        various_InfoEvent(g_h1_, TRUE);
    #endif

    g_rcv_v_ = IntegerMultiply(g_rcv_v2_, IntegerAdd(z_(), g_snd_a_));
    g_snd_a_ = IntegerAdd(g_snd_a_, 1);
    g_sync_c_ = 0.0;
    g_sync_ = _various_l1;

    #if EVENT_OUTPUT
        various_InfoEvent(g_h1_, FALSE);
    #endif
    #if PRINT_OUTPUT
        PrintOutput(g_h1_, FALSE);
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
static void PerformEdges(void) {
    /* Uncontrollables. */
    int count = 0;
    for (;;) {
        count++;
        if (count > MAX_NUM_EVENTS) { /* 'Infinite' loop detection. */
            fprintf(stderr, "Warning: Quitting after performing %d uncontrollable events, infinite loop?\n", count);
            break;
        }


        break; /* No edge fired, done with discrete steps. */
    }

    /* Controllables. */
    count = 0;
    for (;;) {
        count++;
        if (count > MAX_NUM_EVENTS) { /* 'Infinite' loop detection. */
            fprintf(stderr, "Warning: Quitting after performing %d controllable events, infinite loop?\n", count);
            break;
        }

        if (execEdge0()) continue; /* (Try to) perform edge with index 0 and event "a.e". */
        if (execEdge1()) continue; /* (Try to) perform edge with index 1 and event "e1". */
        if (execEdge2()) continue; /* (Try to) perform edge with index 2 and event "g.h1". */
        break; /* No edge fired, done with discrete steps. */
    }
}

/** First model call, initializing, and performing discrete events before the first time step. */
void various_EngineFirstStep(void) {
    InitConstants();

    model_time = 0.0;
    various_AssignInputVariables();
    (a_li_).data[0] = 0;
    (a_li_).data[1] = 0;
    a_x_ = 2;
    g_rcv_v_ = 0;
    g_rcv_v2_ = g_rcv_v_;
    g_snd_a_ = IntegerAdd((5) + (3), inc_(g_rcv_v_));
    g_sync_c_ = 0.0;
    g_sync_ = _various_l1;

    #if PRINT_OUTPUT
        /* pre-initial and post-initial prints. */
        PrintOutput(EVT_INITIAL_, TRUE);
        PrintOutput(EVT_INITIAL_, FALSE);
    #endif

    PerformEdges();

    #if PRINT_OUTPUT
        /* pre-timestep print. */
        PrintOutput(EVT_DELAY_, TRUE);
    #endif
}

/**
 * Engine takes a time step of length \a delta.
 * @param delta Length of the time step.
 */
void various_EngineTimeStep(double delta) {
    various_AssignInputVariables();

    /* Update continuous variables. */
    if (delta > 0.0) {
        RealType deriv0 = g_sync_c_deriv();

        errno = 0;
        g_sync_c_ = UpdateContValue(g_sync_c_ + delta * deriv0, "g.sync.c", errno == 0);
        model_time += delta;
    }

    #if PRINT_OUTPUT
        /* post-timestep print. */
        PrintOutput(EVT_DELAY_, FALSE);
    #endif

    PerformEdges();

    #if PRINT_OUTPUT
        /* pre-timestep print. */
        PrintOutput(EVT_DELAY_, TRUE);
    #endif
}

