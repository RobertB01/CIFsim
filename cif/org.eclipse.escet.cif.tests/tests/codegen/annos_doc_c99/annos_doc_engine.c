/* CIF to C translation of annos_doc.cif
 * Generated file, DO NOT EDIT
 */

#include <stdio.h>
#include <stdlib.h>
#include "annos_doc_engine.h"

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
int EnumTypePrint(annos_docEnum value, char *dest, int start, int end) {
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
const char *annos_doc_event_names[] = {
    "initial-step", /**< Initial step. */
    "delay-step",   /**< Delay step. */
    "tau",          /**< Tau step. */
};

/** Enumeration names. */
const char *enum_names[] = {
    "__some_dummy_enum_literal",
};

/* Constants. */


/* Functions. */


/* Input variables. */

/** Input variable "bool i1". */
BoolType i1_;

/**
 * Input variable "bool i2".
 *
 * single line doc
 */
BoolType i2_;

/**
 * Input variable "bool i3".
 *
 * doc with multiple
 * lines of
 *  text
 */
BoolType i3_;

/**
 * Input variable "bool i4".
 *
 * some doc
 */
BoolType i4_;

/**
 * Input variable "bool i5".
 *
 * First doc.
 *
 * Second doc.
 */
BoolType i5_;

/* State variables. */

/** Discrete variable "bool a.i1". */
BoolType a_i1_;

/**
 * Discrete variable "bool a.i2".
 *
 * single line doc
 */
BoolType a_i2_;

/**
 * Discrete variable "bool a.i3".
 *
 * doc with multiple
 * lines of
 *  text
 */
BoolType a_i3_;

/**
 * Discrete variable "bool a.i4".
 *
 * some doc
 */
BoolType a_i4_;

/**
 * Discrete variable "bool a.i5".
 *
 * First doc.
 *
 * Second doc.
 */
BoolType a_i5_;

RealType model_time; /**< Current model time. */

/** Initialize constants. */
static void InitConstants(void) {

}

/** Print function. */
#if PRINT_OUTPUT
static void PrintOutput(annos_doc_Event_ event, BoolType pre) {
}
#endif

/* Event execution code. */

/**
 * Execute code for event "tau".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent0(void) {
    BoolType guard = ((((a_i1_) || (a_i2_)) || (a_i3_)) || (a_i4_)) || (a_i5_);
    if (!guard) return FALSE;

    #if EVENT_OUTPUT
        annos_doc_InfoEvent(EVT_TAU_, TRUE);
    #endif

    #if EVENT_OUTPUT
        annos_doc_InfoEvent(EVT_TAU_, FALSE);
    #endif
    return TRUE;
}

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

        if (execEvent0()) continue;  /* (Try to) perform event "tau". */
        break; /* No event fired, done with discrete steps. */
    }
}

/** First model call, initializing, and performing discrete events before the first time step. */
void annos_doc_EngineFirstStep(void) {
    InitConstants();

    model_time = 0.0;
    annos_doc_AssignInputVariables();
    a_i1_ = FALSE;
    a_i2_ = FALSE;
    a_i3_ = FALSE;
    a_i4_ = FALSE;
    a_i5_ = FALSE;

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
void annos_doc_EngineTimeStep(double delta) {
    annos_doc_AssignInputVariables();

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

