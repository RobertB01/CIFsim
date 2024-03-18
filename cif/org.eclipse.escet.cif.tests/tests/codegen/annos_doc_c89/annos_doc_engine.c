/* CIF to C translation of annos_doc.cif
 * Generated file, DO NOT EDIT
 */

#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include "annos_doc_engine.h"

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

/** Constant "constants.c1". */
IntType constants_c1_;

/**
 * Constant "constants.c2".
 *
 * single line doc
 */
IntType constants_c2_;

/**
 * Constant "constants.c3".
 *
 * doc with multiple
 * lines of
 *  text
 */
IntType constants_c3_;

/**
 * Constant "constants.c4".
 *
 * some doc
 */
IntType constants_c4_;

/**
 * Constant "constants.c5".
 *
 * First doc.
 *
 * Second doc.
 */
IntType constants_c5_;

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

/** Continuous variable "real contvars.c1". */
RealType contvars_c1_;

/**
 * Continuous variable "real contvars.c2".
 *
 * single line doc
 */
RealType contvars_c2_;

/**
 * Continuous variable "real contvars.c3".
 *
 * doc with multiple
 * lines of
 *  text
 */
RealType contvars_c3_;

/**
 * Continuous variable "real contvars.c4".
 *
 * some doc
 */
RealType contvars_c4_;

/**
 * Continuous variable "real contvars.c5".
 *
 * First doc.
 *
 * Second doc.
 */
RealType contvars_c5_;

/** Discrete variable "bool discvars.d1". */
BoolType discvars_d1_;

/**
 * Discrete variable "bool discvars.d2".
 *
 * single line doc
 */
BoolType discvars_d2_;

/**
 * Discrete variable "bool discvars.d3".
 *
 * doc with multiple
 * lines of
 *  text
 */
BoolType discvars_d3_;

/**
 * Discrete variable "bool discvars.d4".
 *
 * some doc
 */
BoolType discvars_d4_;

/**
 * Discrete variable "bool discvars.d5".
 *
 * First doc.
 *
 * Second doc.
 */
BoolType discvars_d5_;

/* Derivative and algebraic variable functions. */
/** Derivative of "contvars.c1". */
RealType contvars_c1_deriv(void) {
    return 1.0;
}

/** Derivative of "contvars.c2". */
RealType contvars_c2_deriv(void) {
    return 2.0;
}

/** Derivative of "contvars.c3". */
RealType contvars_c3_deriv(void) {
    return 3.0;
}

/** Derivative of "contvars.c4". */
RealType contvars_c4_deriv(void) {
    return 4.0;
}

/** Derivative of "contvars.c5". */
RealType contvars_c5_deriv(void) {
    return 5.0;
}
/** Algebraic variable algvars.a1 = 1. */
IntType algvars_a1_(void) {
    return 1;
}

/**
 * Algebraic variable algvars.a2 = 2.
 *
 * single line doc
 */
IntType algvars_a2_(void) {
    return 2;
}

/**
 * Algebraic variable algvars.a3 = 3.
 *
 * doc with multiple
 * lines of
 *  text
 */
IntType algvars_a3_(void) {
    return 3;
}

/**
 * Algebraic variable algvars.a4 = 4.
 *
 * some doc
 */
IntType algvars_a4_(void) {
    return 4;
}

/**
 * Algebraic variable algvars.a5 = 5.
 *
 * First doc.
 *
 * Second doc.
 */
IntType algvars_a5_(void) {
    return 5;
}

RealType model_time; /**< Current model time. */

/** Initialize constants. */
static void InitConstants(void) {
    constants_c1_ = 1;
    constants_c2_ = 2;
    constants_c3_ = 3;
    constants_c4_ = 4;
    constants_c5_ = 5;
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
    BoolType guard = (((((contvars_c1_) > (0)) || ((contvars_c2_) > (0))) || ((contvars_c3_) > (0))) || ((contvars_c4_) > (0))) || ((contvars_c5_) > (0));
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
 * Execute code for event "tau".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent1(void) {
    BoolType guard = ((((discvars_d1_) || (discvars_d2_)) || (discvars_d3_)) || (discvars_d4_)) || (discvars_d5_);
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

        if (execEvent0()) continue;  /* (Try to) perform event "tau". */
        if (execEvent1()) continue;  /* (Try to) perform event "tau". */
        break; /* No event fired, done with discrete steps. */
    }
}

/** First model call, initializing, and performing discrete events before the first time step. */
void annos_doc_EngineFirstStep(void) {
    InitConstants();

    model_time = 0.0;
    annos_doc_AssignInputVariables();
    contvars_c1_ = 0.0;
    contvars_c2_ = 0.0;
    contvars_c3_ = 0.0;
    contvars_c4_ = 0.0;
    contvars_c5_ = 0.0;
    discvars_d1_ = FALSE;
    discvars_d2_ = FALSE;
    discvars_d3_ = FALSE;
    discvars_d4_ = FALSE;
    discvars_d5_ = FALSE;

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
        RealType deriv0 = contvars_c1_deriv();
        RealType deriv1 = contvars_c2_deriv();
        RealType deriv2 = contvars_c3_deriv();
        RealType deriv3 = contvars_c4_deriv();
        RealType deriv4 = contvars_c5_deriv();

        errno = 0;
        contvars_c1_ = UpdateContValue(contvars_c1_ + delta * deriv0, "contvars.c1", errno == 0);
        errno = 0;
        contvars_c2_ = UpdateContValue(contvars_c2_ + delta * deriv1, "contvars.c2", errno == 0);
        errno = 0;
        contvars_c3_ = UpdateContValue(contvars_c3_ + delta * deriv2, "contvars.c3", errno == 0);
        errno = 0;
        contvars_c4_ = UpdateContValue(contvars_c4_ + delta * deriv3, "contvars.c4", errno == 0);
        errno = 0;
        contvars_c5_ = UpdateContValue(contvars_c5_ + delta * deriv4, "contvars.c5", errno == 0);
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

