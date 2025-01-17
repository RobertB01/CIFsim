/* CIF to C translation of state_event_exclusion_invariants.cif
 * Generated file, DO NOT EDIT
 */

#include <stdio.h>
#include <stdlib.h>
#include "state_event_exclusion_invariants_engine.h"

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
int EnumTypePrint(state_event_exclusion_invariantsEnum value, char *dest, int start, int end) {
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
const char *state_event_exclusion_invariants_event_names[] = {
    "initial-step", /**< Initial step. */
    "delay-step",   /**< Delay step. */
    "e",            /**< Event "e". */
};

/** Enumeration names. */
const char *enum_names[] = {
    /** Literal "__some_dummy_enum_literal". */
    "__some_dummy_enum_literal",
};

/* Constants. */


/* Functions. */


/* Input variables. */

/** Input variable "int x". */
IntType x_;

/* State variables. */


RealType model_time; /**< Current model time. */

/** Initialize constants. */
static void InitConstants(void) {

}

/** Print function. */
#if PRINT_OUTPUT
static void PrintOutput(state_event_exclusion_invariants_Event_ event, BoolType pre) {
}
#endif

/* Edge execution code. */

/**
 * Execute code for edge with index 0 and event "e".
 *
 * @return Whether the edge was performed.
 */
static BoolType execEdge0(void) {
    BoolType guard = (((x_) > (1)) && (((x_) <= (1)) && ((x_) > (1)))) && ((((x_) <= (1)) && ((x_) > (1))) && (((x_) <= (1)) && (((x_) > (1)) && ((x_) <= (1)))));
    if (!guard) return FALSE;

    #if EVENT_OUTPUT
        state_event_exclusion_invariants_InfoEvent(e_, TRUE);
    #endif

    #if EVENT_OUTPUT
        state_event_exclusion_invariants_InfoEvent(e_, FALSE);
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

        if (execEdge0()) continue; /* (Try to) perform edge with index 0 and event "e". */
        break; /* No edge fired, done with discrete steps. */
    }
}

/** First model call, initializing, and performing discrete events before the first time step. */
void state_event_exclusion_invariants_EngineFirstStep(void) {
    InitConstants();

    model_time = 0.0;
    state_event_exclusion_invariants_AssignInputVariables();


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
void state_event_exclusion_invariants_EngineTimeStep(double delta) {
    state_event_exclusion_invariants_AssignInputVariables();

    /* Update continuous variables. */
    if (delta > 0.0) {

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

