/* CIF to C translation of ${prefix}.cif
 * Generated file, DO NOT EDIT
${spec-comments} */

#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include "${prefix}_engine.h"

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
${type-support-code}

/** Event names. */
const char *${prefix}_event_names[] = {
${event-name-list}
};

/** Enumeration names. */
${enum-names-list}

/* Constants. */
${constant-definitions}

/* Functions. */
${functions-code}

/* Input variables. */
${inputvar-definitions}

/* State variables. */
${statevar-definitions}

/* Derivative and algebraic variable functions. */
${derivative-functions}
${algvar-functions}

RealType model_time; /**< Current model time. */

/** Initialize constants. */
static void InitConstants(void) {
${constant-initialization}
}

/** Print function. */
${print-function}

/* Edge execution code. */
${edge-methods-code}

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

${edge-calls-code-uncontrollables}
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

${edge-calls-code-controllables}
        break; /* No edge fired, done with discrete steps. */
    }
}

/** First model call, initializing, and performing discrete events before the first time step. */
void ${prefix}_EngineFirstStep(void) {
    InitConstants();

    model_time = 0.0;
${inputvar-function-call}
${initialize-statevars}

${initial-print-calls}

    PerformEdges();

${time-pre-print-call}
}

/**
 * Engine takes a time step of length \a delta.
 * @param delta Length of the time step.
 */
void ${prefix}_EngineTimeStep(double delta) {
${inputvar-function-call}

    /* Update continuous variables. */
    if (delta > 0.0) {
${contvars-update}
        model_time += delta;
    }

${time-post-print-call}

    PerformEdges();

${time-pre-print-call}
}

