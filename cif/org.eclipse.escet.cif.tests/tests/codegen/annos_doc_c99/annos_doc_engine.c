/* CIF to C translation of annos_doc.cif
 * Generated file, DO NOT EDIT
 *
 * First doc
 * with multiple lines.
 *
 * Second doc line 1.
 * Second doc line 2.
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
    "e",            /**< Event "e". */
    "events.e1",    /**< Event "events.e1". */
    "events.e2",    /**< Event "events.e2". */
    "events.e3",    /**< Event "events.e3". */
    "events.e4",    /**< Event "events.e4". */
    "events.e5",    /**< Event "events.e5". */
};

/** Enumeration names. */
const char *enum_names[] = {
    /** Literal "l1". */
    "l1",

    /**
     * Literal "l2".
     *
     * single line doc
     */
    "l2",

    /**
     * Literal "l3".
     *
     * doc with multiple
     * lines of
     *  text
     */
    "l3",

    /**
     * Literal "l4".
     *
     * some doc
     */
    "l4",

    /**
     * Literal "l5".
     *
     * First doc.
     *
     * Second doc line 1.
     * Second doc line 2.
     */
    "l5",
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
 * Second doc line 1.
 * Second doc line 2.
 */
IntType constants_c5_;

/* Functions. */

/**
 * Function "funcs.func1".
 *
 * @param funcs_func1_p_ Function parameter "funcs.func1.p".
 * @return The return value of the function.
 */
BoolType funcs_func1_(BoolType funcs_func1_p_) {
    // Variable "funcs.func1.v1".
    BoolType funcs_func1_v1_;
    funcs_func1_v1_ = funcs_func1_p_;

    // Variable "funcs.func1.v2".
    BoolType funcs_func1_v2_;
    funcs_func1_v2_ = funcs_func1_v1_;

    // Execute statements in the function body.
    return funcs_func1_v2_;
    assert(0); /* Falling through the end of the function. */
}


/**
 * Function "funcs.func2".
 *
 * single line doc
 *
 * @param funcs_func2_p_ Function parameter "funcs.func2.p".
 *
 *     single line doc
 * @return The return value of the function.
 */
BoolType funcs_func2_(BoolType funcs_func2_p_) {
    // Variable "funcs.func2.v1".
    //
    // single line doc
    BoolType funcs_func2_v1_;
    funcs_func2_v1_ = funcs_func2_p_;

    // Variable "funcs.func2.v2".
    //
    // single line doc
    BoolType funcs_func2_v2_;
    funcs_func2_v2_ = funcs_func2_v1_;

    // Execute statements in the function body.
    return funcs_func2_v2_;
    assert(0); /* Falling through the end of the function. */
}


/**
 * Function "funcs.func3".
 *
 * doc with multiple
 * lines of
 *  text
 *
 * @param funcs_func3_p_ Function parameter "funcs.func3.p".
 *
 *     doc with multiple
 *     lines of
 *      text
 * @return The return value of the function.
 */
BoolType funcs_func3_(BoolType funcs_func3_p_) {
    // Variable "funcs.func3.v1".
    //
    // doc with multiple
    // lines of
    //  text
    BoolType funcs_func3_v1_;
    funcs_func3_v1_ = funcs_func3_p_;

    // Variable "funcs.func3.v2".
    //
    // doc with multiple
    // lines of
    //  text
    BoolType funcs_func3_v2_;
    funcs_func3_v2_ = funcs_func3_v1_;

    // Execute statements in the function body.
    return funcs_func3_v2_;
    assert(0); /* Falling through the end of the function. */
}


/**
 * Function "funcs.func4".
 *
 * some doc
 *
 * @param funcs_func4_p_ Function parameter "funcs.func4.p".
 *
 *     some doc
 * @return The return value of the function.
 */
BoolType funcs_func4_(BoolType funcs_func4_p_) {
    // Variable "funcs.func4.v1".
    //
    // some doc
    BoolType funcs_func4_v1_;
    funcs_func4_v1_ = funcs_func4_p_;

    // Variable "funcs.func4.v2".
    //
    // some doc
    BoolType funcs_func4_v2_;
    funcs_func4_v2_ = funcs_func4_v1_;

    // Execute statements in the function body.
    return funcs_func4_v2_;
    assert(0); /* Falling through the end of the function. */
}


/**
 * Function "funcs.func5".
 *
 * First doc.
 *
 * Second doc line 1.
 * Second doc line 2.
 *
 * @param funcs_func5_p_ Function parameter "funcs.func5.p".
 *
 *     First doc.
 *
 *     Second doc line 1.
 *     Second doc line 2.
 * @return The return value of the function.
 */
BoolType funcs_func5_(BoolType funcs_func5_p_) {
    // Variable "funcs.func5.v1".
    //
    // First doc.
    //
    // Second doc line 1.
    // Second doc line 2.
    BoolType funcs_func5_v1_;
    funcs_func5_v1_ = funcs_func5_p_;

    // Variable "funcs.func5.v2".
    //
    // First doc.
    //
    // Second doc line 1.
    // Second doc line 2.
    BoolType funcs_func5_v2_;
    funcs_func5_v2_ = funcs_func5_v1_;

    // Execute statements in the function body.
    return funcs_func5_v2_;
    assert(0); /* Falling through the end of the function. */
}

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
 * Second doc line 1.
 * Second doc line 2.
 */
BoolType i5_;

/**
 * Input variable "bool preconditions.i".
 *
 * {}
 */
BoolType preconditions_i_;

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
 * Second doc line 1.
 * Second doc line 2.
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
 * Second doc line 1.
 * Second doc line 2.
 */
BoolType discvars_d5_;

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
 * Execute code for event "e".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent0(void) {
    BoolType guard = ((((contvars_c1_) > (0)) || ((contvars_c2_) > (0))) || (((contvars_c3_) > (0)) || (((contvars_c4_) > (0)) || ((contvars_c5_) > (0))))) && (((discvars_d1_) || (discvars_d2_)) || ((discvars_d3_) || ((discvars_d4_) || (discvars_d5_))));
    if (!guard) return FALSE;

    #if EVENT_OUTPUT
        annos_doc_InfoEvent(e_, TRUE);
    #endif

    #if EVENT_OUTPUT
        annos_doc_InfoEvent(e_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "events.e1".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent1(void) {
    #if EVENT_OUTPUT
        annos_doc_InfoEvent(events_e1_, TRUE);
    #endif

    #if EVENT_OUTPUT
        annos_doc_InfoEvent(events_e1_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "events.e2".
 *
 * single line doc
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent2(void) {
    #if EVENT_OUTPUT
        annos_doc_InfoEvent(events_e2_, TRUE);
    #endif

    #if EVENT_OUTPUT
        annos_doc_InfoEvent(events_e2_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "events.e3".
 *
 * doc with multiple
 * lines of
 *  text
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent3(void) {
    #if EVENT_OUTPUT
        annos_doc_InfoEvent(events_e3_, TRUE);
    #endif

    #if EVENT_OUTPUT
        annos_doc_InfoEvent(events_e3_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "events.e4".
 *
 * some doc
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent4(void) {
    #if EVENT_OUTPUT
        annos_doc_InfoEvent(events_e4_, TRUE);
    #endif

    #if EVENT_OUTPUT
        annos_doc_InfoEvent(events_e4_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "events.e5".
 *
 * First doc.
 *
 * Second doc line 1.
 * Second doc line 2.
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent5(void) {
    #if EVENT_OUTPUT
        annos_doc_InfoEvent(events_e5_, TRUE);
    #endif

    #if EVENT_OUTPUT
        annos_doc_InfoEvent(events_e5_, FALSE);
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

        if (execEvent0()) continue;  /* (Try to) perform event "e". */
        if (execEvent1()) continue;  /* (Try to) perform event "events.e1". */
        if (execEvent2()) continue;  /* (Try to) perform event "events.e2". */
        if (execEvent3()) continue;  /* (Try to) perform event "events.e3". */
        if (execEvent4()) continue;  /* (Try to) perform event "events.e4". */
        if (execEvent5()) continue;  /* (Try to) perform event "events.e5". */
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

        contvars_c1_ = UpdateContValue(contvars_c1_ + delta * deriv0, "contvars.c1");
        contvars_c2_ = UpdateContValue(contvars_c2_ + delta * deriv1, "contvars.c2");
        contvars_c3_ = UpdateContValue(contvars_c3_ + delta * deriv2, "contvars.c3");
        contvars_c4_ = UpdateContValue(contvars_c4_ + delta * deriv3, "contvars.c4");
        contvars_c5_ = UpdateContValue(contvars_c5_ + delta * deriv4, "contvars.c5");
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

