/* CIF to C translation of declarations.cif
 * Generated file, DO NOT EDIT
 */

#include <stdio.h>
#include <stdlib.h>
#include "declarations_engine.h"

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
int EnumTypePrint(declarationsEnum value, char *dest, int start, int end) {
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
 * Compare two tuples for equality.
 * @param left First tuple to compare.
 * @param right Second tuple to compare.
 * @return Whether both tuples are the same.
 */
BoolType T3IIRTypeEquals(T3IIRType *left, T3IIRType *right) {
    if (left == right) return TRUE;
    if (memcmp(&left->_field0, &right->_field0, sizeof(IntType)) != 0) return FALSE;
    if (memcmp(&left->_field1, &right->_field1, sizeof(IntType)) != 0) return FALSE;
    if (memcmp(&left->_field2, &right->_field2, sizeof(RealType)) != 0) return FALSE;
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
int T3IIRTypePrint(T3IIRType *tuple, char *dest, int start, int end) {
    int last = end - 1;
    if (start < last) { dest[start++] = '('; }
    start = IntTypePrint(tuple->_field0, dest, start, end);
    if (start < last) { dest[start++] = ','; }
    if (start < last) { dest[start++] = ' '; }
    start = IntTypePrint(tuple->_field1, dest, start, end);
    if (start < last) { dest[start++] = ','; }
    if (start < last) { dest[start++] = ' '; }
    start = RealTypePrint(tuple->_field2, dest, start, end);
    if (start < last) { dest[start++] = ')'; }
    dest[start] = '\0';
    return start;
}


/** Event names. */
const char *declarations_event_names[] = {
    "initial-step", /**< Initial step. */
    "delay-step",   /**< Delay step. */
    "tau",          /**< Tau step. */
    "e_e1",         /**< Event e_e1. */
    "e_e2",         /**< Event e_e2. */
    "c_e1",         /**< Event c_e1. */
    "c_e2",         /**< Event c_e2. */
    "u_e1",         /**< Event u_e1. */
    "u_e2",         /**< Event u_e2. */
};

/** Enumeration names. */
const char *enum_names[] = {
    "loc1",
    "loc2",
};

/* Constants. */
RealType c1_; /**< Constant "c1". */
RealType c4_; /**< Constant "c4". */
RealType c5_; /**< Constant "c5". */
RealType c3_; /**< Constant "c3". */
RealType c2_; /**< Constant "c2". */

/* Functions. */
IntType inc_(IntType inc_x_) {
    return IntegerAdd(inc_x_, 1);
    assert(0); /* Falling through the end of the function. */
}

RealType f1_(IntType f1_x_) {
    RealType f1_v1_;
    f1_v1_ = 2.449489742783178;
    RealType f1_v4_;
    f1_v4_ = RealAdd(f1_v1_, 2.0);
    RealType f1_v5_;
    f1_v5_ = f1_v4_;
    RealType f1_v3_;
    f1_v3_ = f1_v5_;
    RealType f1_v2_;
    f1_v2_ = f1_v3_;

    return RealAdd(RealAdd(RealAdd(RealAdd(f1_v1_, f1_v2_), f1_v3_), RealMultiply(f1_v4_, f1_v5_)), f1_x_);
    assert(0); /* Falling through the end of the function. */
}

/* Input variables. */

/** Input variable "int i1". */
IntType i1_;

/** Input variable "real i2". */
RealType i2_;

/** Input variable "tuple(int a; int b; real c) i3". */
T3IIRType i3_;

/* State variables. */

/** Discrete variable "real aut1.v1". */
RealType aut1_v1_;

/** Discrete variable "real aut1.v4". */
RealType aut1_v4_;

/** Discrete variable "real aut1.v5". */
RealType aut1_v5_;

/** Continuous variable "real aut2.v2". */
RealType aut2_v2_;

/** Discrete variable "E g1.a1". */
declarationsEnum g1_a1_;

/** Continuous variable "real aut1.v3". */
RealType aut1_v3_;

/** Discrete variable "real aut2.v1". */
RealType aut2_v1_;

/** Discrete variable "real aut1.v2". */
RealType aut1_v2_;

/** Discrete variable "real aut1.v7". */
RealType aut1_v7_;

/** Discrete variable "real aut1.v8". */
RealType aut1_v8_;

/** Discrete variable "real aut1.v6". */
RealType aut1_v6_;

RealType model_time; /**< Current model time. */

/** Initialize constants. */
static void InitConstants(void) {
    c1_ = 2.23606797749979;
    c4_ = RealAdd(c1_, 2.0);
    c5_ = c4_;
    c3_ = c5_;
    c2_ = c3_;
}

/** Print function. */
#if PRINT_OUTPUT
static void PrintOutput(declarations_Event_ event, BoolType pre) {
}
#endif

/* Event execution code. */

/**
 * Execute code for event "c_e1".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent0(void) {
    #if EVENT_OUTPUT
        declarations_InfoEvent(c_e1_, TRUE);
    #endif

    #if EVENT_OUTPUT
        declarations_InfoEvent(c_e1_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "c_e2".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent1(void) {
    #if EVENT_OUTPUT
        declarations_InfoEvent(c_e2_, TRUE);
    #endif

    #if EVENT_OUTPUT
        declarations_InfoEvent(c_e2_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e_e1".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent2(void) {
    BoolType guard = (g1_a1_) == (_declarations_loc1);
    if (!guard) return FALSE;

    #if EVENT_OUTPUT
        declarations_InfoEvent(e_e1_, TRUE);
    #endif

    g1_a1_ = _declarations_loc2;

    #if EVENT_OUTPUT
        declarations_InfoEvent(e_e1_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e_e2".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent3(void) {
    BoolType guard = (g1_a1_) == (_declarations_loc2);
    if (!guard) return FALSE;

    #if EVENT_OUTPUT
        declarations_InfoEvent(e_e2_, TRUE);
    #endif

    g1_a1_ = _declarations_loc1;

    #if EVENT_OUTPUT
        declarations_InfoEvent(e_e2_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "u_e1".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent4(void) {
    #if EVENT_OUTPUT
        declarations_InfoEvent(u_e1_, TRUE);
    #endif

    #if EVENT_OUTPUT
        declarations_InfoEvent(u_e1_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "u_e2".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent5(void) {
    #if EVENT_OUTPUT
        declarations_InfoEvent(u_e2_, TRUE);
    #endif

    #if EVENT_OUTPUT
        declarations_InfoEvent(u_e2_, FALSE);
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

        if (execEvent0()) continue;  /* (Try to) perform event "c_e1". */
        if (execEvent1()) continue;  /* (Try to) perform event "c_e2". */
        if (execEvent2()) continue;  /* (Try to) perform event "e_e1". */
        if (execEvent3()) continue;  /* (Try to) perform event "e_e2". */
        if (execEvent4()) continue;  /* (Try to) perform event "u_e1". */
        if (execEvent5()) continue;  /* (Try to) perform event "u_e2". */
        break; /* No event fired, done with discrete steps. */
    }
}

/** First model call, initializing, and performing discrete events before the first time step. */
void declarations_EngineFirstStep(void) {
    InitConstants();

    model_time = 0.0;
    declarations_AssignInputVariables();
    aut1_v1_ = 2.6457513110645907;
    aut1_v4_ = RealAdd(aut1_v1_, 2.0);
    aut1_v5_ = aut1_v4_;
    aut2_v2_ = 0.0;
    g1_a1_ = _declarations_loc1;
    aut1_v3_ = aut1_v5_;
    aut2_v1_ = aut2_v2_;
    aut1_v2_ = aut1_v3_;
    aut1_v7_ = RealAdd(aut1_v2_, aut2_v1_);
    aut1_v8_ = aut1_v7_;
    aut1_v6_ = aut1_v8_;

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
void declarations_EngineTimeStep(double delta) {
    declarations_AssignInputVariables();

    /* Update continuous variables. */
    if (delta > 0.0) {
        RealType deriv0 = aut1_v3_deriv();
        RealType deriv1 = aut2_v2_deriv();

        aut1_v3_ = UpdateContValue(aut1_v3_ + delta * deriv0, "aut1.v3");
        aut2_v2_ = UpdateContValue(aut2_v2_ + delta * deriv1, "aut2.v2");
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

