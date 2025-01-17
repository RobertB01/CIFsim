/* CIF to C translation of databased_supervisor.cif
 * Generated file, DO NOT EDIT
 */

#include <stdio.h>
#include <stdlib.h>
#include "databased_supervisor_engine.h"

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
/**
 * Compare two tuples for equality.
 * @param left First tuple to compare.
 * @param right Second tuple to compare.
 * @return Whether both tuples are the same.
 */
BoolType T3IIITypeEquals(T3IIIType *left, T3IIIType *right) {
    if (left == right) return TRUE;
    if (memcmp(&left->_field0, &right->_field0, sizeof(IntType)) != 0) return FALSE;
    if (memcmp(&left->_field1, &right->_field1, sizeof(IntType)) != 0) return FALSE;
    if (memcmp(&left->_field2, &right->_field2, sizeof(IntType)) != 0) return FALSE;
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
int T3IIITypePrint(T3IIIType *tuple, char *dest, int start, int end) {
    int last = end - 1;
    if (start < last) { dest[start++] = '('; }
    start = IntTypePrint(tuple->_field0, dest, start, end);
    if (start < last) { dest[start++] = ','; }
    if (start < last) { dest[start++] = ' '; }
    start = IntTypePrint(tuple->_field1, dest, start, end);
    if (start < last) { dest[start++] = ','; }
    if (start < last) { dest[start++] = ' '; }
    start = IntTypePrint(tuple->_field2, dest, start, end);
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
BoolType A12T3IIITypeEquals(A12T3IIIType *left, A12T3IIIType *right) {
    if (left == right) return TRUE;
    int i;
    for (i = 0; i < 12; i++) {
        if (!(T3IIITypeEquals(&(left->data[i]), &(right->data[i])))) return FALSE;
    }
    return TRUE;
}

/**
 * Extract an element from the array.
 * @param array Array with value to retrieve.
 * @param index Element index in the array (not normalized).
 * @return Element value at the indicated index from the array.
 */
T3IIIType *A12T3IIITypeProject(A12T3IIIType *array, IntType index) {
    if (index < 0) index += 12; /* Normalize index. */
    assert(index >= 0 && index < 12);

    return &array->data[index];
}

/**
 * In-place change of the array.
 * @param array Array to modify.
 * @param index Element index in the array (not normalized).
 * @param value New value to copy into the array.
 */
void A12T3IIITypeModify(A12T3IIIType *array, IntType index, T3IIIType *value) {
    if (index < 0) index += 12; /* Normalize index. */
    assert(index >= 0 && index < 12);

    memcpy(&array->data[index], value, sizeof(T3IIIType));
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
int A12T3IIITypePrint(A12T3IIIType *array, char *dest, int start, int end) {
    int last = end - 1;
    if (start < last) { dest[start++] = '['; }
    int index;
    for (index = 0; index < 12; index++) {
        if (index > 0) {
            if (start < last) { dest[start++] = ','; }
            if (start < last) { dest[start++] = ' '; }
        }
        start = T3IIITypePrint(&array->data[index], dest, start, end);
    }
    if (start < last) { dest[start++] = ']'; }
    dest[start] = '\0';
    return start;
}

int EnumTypePrint(databased_supervisorEnum value, char *dest, int start, int end) {
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
BoolType A6BTypeEquals(A6BType *left, A6BType *right) {
    if (left == right) return TRUE;
    return memcmp(left, right, sizeof(A6BType)) == 0;
}

/**
 * Extract an element from the array.
 * @param array Array with value to retrieve.
 * @param index Element index in the array (not normalized).
 * @return Element value at the indicated index from the array.
 */
BoolType A6BTypeProject(A6BType *array, IntType index) {
    if (index < 0) index += 6; /* Normalize index. */
    assert(index >= 0 && index < 6);

    return array->data[index];
}

/**
 * In-place change of the array.
 * @param array Array to modify.
 * @param index Element index in the array (not normalized).
 * @param value New value to copy into the array.
 */
void A6BTypeModify(A6BType *array, IntType index, BoolType value) {
    if (index < 0) index += 6; /* Normalize index. */
    assert(index >= 0 && index < 6);

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
int A6BTypePrint(A6BType *array, char *dest, int start, int end) {
    int last = end - 1;
    if (start < last) { dest[start++] = '['; }
    int index;
    for (index = 0; index < 6; index++) {
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


/** Event names. */
const char *databased_supervisor_event_names[] = {
    "initial-step",      /**< Initial step. */
    "delay-step",        /**< Delay step. */
    "Button.u_pushed",   /**< Event "Button.u_pushed". */
    "Button.u_released", /**< Event "Button.u_released". */
    "Lamp.c_on",         /**< Event "Lamp.c_on". */
    "Lamp.c_off",        /**< Event "Lamp.c_off". */
    "Timer.c_start",     /**< Event "Timer.c_start". */
    "Timer.u_timeout",   /**< Event "Timer.u_timeout". */
};

/** Enumeration names. */
const char *enum_names[] = {
    /** Literal "Idle". */
    "Idle",

    /** Literal "Off". */
    "Off",

    /** Literal "On". */
    "On",

    /** Literal "Pushed". */
    "Pushed",

    /** Literal "Released". */
    "Released",

    /** Literal "Running". */
    "Running",

    /** Literal "StartTimer". */
    "StartTimer",

    /** Literal "TurnLampOff". */
    "TurnLampOff",

    /** Literal "TurnLampOn". */
    "TurnLampOn",

    /** Literal "WaitForButtonPush". */
    "WaitForButtonPush",

    /** Literal "WaitForTimeout". */
    "WaitForTimeout",
};

/* Constants. */

/** Constant "bdd_nodes". */
A12T3IIIType bdd_nodes_;

/* Functions. */

/**
 * Function "bdd_eval".
 *
 * @param bdd_eval_idx_ Function parameter "bdd_eval.idx".
 * @param bdd_eval_values_tmp2 Function parameter "bdd_eval.values".
 * @return The return value of the function.
 */
BoolType bdd_eval_(IntType bdd_eval_idx_, A6BType* bdd_eval_values_tmp2) {
    // Parameter "bdd_eval.values".
    A6BType bdd_eval_values_ = *(bdd_eval_values_tmp2);

    // Variable "bdd_eval.node".
    T3IIIType bdd_eval_node_;
    (bdd_eval_node_)._field0 = 0;
    (bdd_eval_node_)._field1 = 0;
    (bdd_eval_node_)._field2 = 0;

    // Variable "bdd_eval.val".
    BoolType bdd_eval_val_;
    bdd_eval_val_ = FALSE;

    // Execute statements in the function body.
    while ((bdd_eval_idx_) >= (0)) {
        bdd_eval_node_ = *(A12T3IIITypeProject(&(bdd_nodes_), bdd_eval_idx_));

        bdd_eval_val_ = A6BTypeProject(&(bdd_eval_values_), (bdd_eval_node_)._field0);

        {
            IntType if_dest3;
            if (bdd_eval_val_) {
                if_dest3 = (bdd_eval_node_)._field2;
            } else {
                if_dest3 = (bdd_eval_node_)._field1;
            }
            bdd_eval_idx_ = if_dest3;
        }
    }

    return (bdd_eval_idx_) == (-(1));
    assert(0); /* Falling through the end of the function. */
}

/* Input variables. */


/* State variables. */

/** Discrete variable "E Button". */
databased_supervisorEnum Button_;

/** Discrete variable "E Cycle". */
databased_supervisorEnum Cycle_;

/** Discrete variable "E Lamp". */
databased_supervisorEnum Lamp_;

/** Discrete variable "E Timer". */
databased_supervisorEnum Timer_;

RealType model_time; /**< Current model time. */

/** Initialize constants. */
static void InitConstants(void) {
    ((bdd_nodes_).data[0])._field0 = 1;
    ((bdd_nodes_).data[0])._field1 = -(2);
    ((bdd_nodes_).data[0])._field2 = 1;
    ((bdd_nodes_).data[1])._field0 = 2;
    ((bdd_nodes_).data[1])._field1 = 2;
    ((bdd_nodes_).data[1])._field2 = -(2);
    ((bdd_nodes_).data[2])._field0 = 3;
    ((bdd_nodes_).data[2])._field1 = 3;
    ((bdd_nodes_).data[2])._field2 = -(2);
    ((bdd_nodes_).data[3])._field0 = 4;
    ((bdd_nodes_).data[3])._field1 = 4;
    ((bdd_nodes_).data[3])._field2 = -(2);
    ((bdd_nodes_).data[4])._field0 = 5;
    ((bdd_nodes_).data[4])._field1 = -(1);
    ((bdd_nodes_).data[4])._field2 = -(2);
    ((bdd_nodes_).data[5])._field0 = 1;
    ((bdd_nodes_).data[5])._field1 = 6;
    ((bdd_nodes_).data[5])._field2 = -(2);
    ((bdd_nodes_).data[6])._field0 = 2;
    ((bdd_nodes_).data[6])._field1 = 7;
    ((bdd_nodes_).data[6])._field2 = -(2);
    ((bdd_nodes_).data[7])._field0 = 3;
    ((bdd_nodes_).data[7])._field1 = -(2);
    ((bdd_nodes_).data[7])._field2 = 8;
    ((bdd_nodes_).data[8])._field0 = 4;
    ((bdd_nodes_).data[8])._field1 = -(2);
    ((bdd_nodes_).data[8])._field2 = 4;
    ((bdd_nodes_).data[9])._field0 = 1;
    ((bdd_nodes_).data[9])._field1 = 10;
    ((bdd_nodes_).data[9])._field2 = -(2);
    ((bdd_nodes_).data[10])._field0 = 2;
    ((bdd_nodes_).data[10])._field1 = -(2);
    ((bdd_nodes_).data[10])._field2 = 11;
    ((bdd_nodes_).data[11])._field0 = 3;
    ((bdd_nodes_).data[11])._field1 = 8;
    ((bdd_nodes_).data[11])._field2 = -(2);
}

/** Print function. */
#if PRINT_OUTPUT
static void PrintOutput(databased_supervisor_Event_ event, BoolType pre) {
}
#endif

/* Edge execution code. */

/**
 * Execute code for edge with index 0 and event "Button.u_pushed".
 *
 * @return Whether the edge was performed.
 */
static BoolType execEdge0(void) {
    BoolType guard = ((Button_) == (_databased_supervisor_Released)) && ((((Cycle_) == (_databased_supervisor_WaitForButtonPush)) || ((Cycle_) == (_databased_supervisor_TurnLampOn))) || (((Cycle_) == (_databased_supervisor_StartTimer)) || (((Cycle_) == (_databased_supervisor_WaitForTimeout)) || ((Cycle_) == (_databased_supervisor_TurnLampOff)))));
    if (!guard) return FALSE;

    #if EVENT_OUTPUT
        databased_supervisor_InfoEvent(Button_u_pushed_, TRUE);
    #endif

    Button_ = _databased_supervisor_Pushed;
    if ((Cycle_) == (_databased_supervisor_WaitForButtonPush)) {
        Cycle_ = _databased_supervisor_TurnLampOn;
    } else if ((Cycle_) == (_databased_supervisor_TurnLampOn)) {
        Cycle_ = _databased_supervisor_TurnLampOn;
    } else if ((Cycle_) == (_databased_supervisor_StartTimer)) {
        Cycle_ = _databased_supervisor_StartTimer;
    } else if ((Cycle_) == (_databased_supervisor_WaitForTimeout)) {
        Cycle_ = _databased_supervisor_WaitForTimeout;
    } else if ((Cycle_) == (_databased_supervisor_TurnLampOff)) {
        Cycle_ = _databased_supervisor_TurnLampOff;
    }

    #if EVENT_OUTPUT
        databased_supervisor_InfoEvent(Button_u_pushed_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for edge with index 1 and event "Button.u_released".
 *
 * @return Whether the edge was performed.
 */
static BoolType execEdge1(void) {
    BoolType guard = (Button_) == (_databased_supervisor_Pushed);
    if (!guard) return FALSE;

    #if EVENT_OUTPUT
        databased_supervisor_InfoEvent(Button_u_released_, TRUE);
    #endif

    Button_ = _databased_supervisor_Released;

    #if EVENT_OUTPUT
        databased_supervisor_InfoEvent(Button_u_released_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for edge with index 2 and event "Timer.u_timeout".
 *
 * @return Whether the edge was performed.
 */
static BoolType execEdge2(void) {
    BoolType guard = ((Cycle_) == (_databased_supervisor_WaitForTimeout)) && ((Timer_) == (_databased_supervisor_Running));
    if (!guard) return FALSE;

    #if EVENT_OUTPUT
        databased_supervisor_InfoEvent(Timer_u_timeout_, TRUE);
    #endif

    Cycle_ = _databased_supervisor_TurnLampOff;
    Timer_ = _databased_supervisor_Idle;

    #if EVENT_OUTPUT
        databased_supervisor_InfoEvent(Timer_u_timeout_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for edge with index 3 and event "Lamp.c_off".
 *
 * @return Whether the edge was performed.
 */
static BoolType execEdge3(void) {
    A6BType deref_store3 = bdd_values_();
    BoolType guard = (((Cycle_) == (_databased_supervisor_TurnLampOff)) && ((Lamp_) == (_databased_supervisor_On))) && (bdd_eval_(5, &deref_store3));
    if (!guard) return FALSE;

    #if EVENT_OUTPUT
        databased_supervisor_InfoEvent(Lamp_c_off_, TRUE);
    #endif

    Cycle_ = _databased_supervisor_WaitForButtonPush;
    Lamp_ = _databased_supervisor_Off;

    #if EVENT_OUTPUT
        databased_supervisor_InfoEvent(Lamp_c_off_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for edge with index 4 and event "Lamp.c_on".
 *
 * @return Whether the edge was performed.
 */
static BoolType execEdge4(void) {
    A6BType deref_store4 = bdd_values_();
    BoolType guard = (((Cycle_) == (_databased_supervisor_TurnLampOn)) && ((Lamp_) == (_databased_supervisor_Off))) && (bdd_eval_(0, &deref_store4));
    if (!guard) return FALSE;

    #if EVENT_OUTPUT
        databased_supervisor_InfoEvent(Lamp_c_on_, TRUE);
    #endif

    Cycle_ = _databased_supervisor_StartTimer;
    Lamp_ = _databased_supervisor_On;

    #if EVENT_OUTPUT
        databased_supervisor_InfoEvent(Lamp_c_on_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for edge with index 5 and event "Timer.c_start".
 *
 * @return Whether the edge was performed.
 */
static BoolType execEdge5(void) {
    A6BType deref_store5 = bdd_values_();
    BoolType guard = ((Cycle_) == (_databased_supervisor_StartTimer)) && ((bdd_eval_(9, &deref_store5)) && ((Timer_) == (_databased_supervisor_Idle)));
    if (!guard) return FALSE;

    #if EVENT_OUTPUT
        databased_supervisor_InfoEvent(Timer_c_start_, TRUE);
    #endif

    Cycle_ = _databased_supervisor_WaitForTimeout;
    Timer_ = _databased_supervisor_Running;

    #if EVENT_OUTPUT
        databased_supervisor_InfoEvent(Timer_c_start_, FALSE);
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

        if (execEdge0()) continue; /* (Try to) perform edge with index 0 and event "Button.u_pushed". */
        if (execEdge1()) continue; /* (Try to) perform edge with index 1 and event "Button.u_released". */
        if (execEdge2()) continue; /* (Try to) perform edge with index 2 and event "Timer.u_timeout". */
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

        if (execEdge3()) continue; /* (Try to) perform edge with index 3 and event "Lamp.c_off". */
        if (execEdge4()) continue; /* (Try to) perform edge with index 4 and event "Lamp.c_on". */
        if (execEdge5()) continue; /* (Try to) perform edge with index 5 and event "Timer.c_start". */
        break; /* No edge fired, done with discrete steps. */
    }
}

/** First model call, initializing, and performing discrete events before the first time step. */
void databased_supervisor_EngineFirstStep(void) {
    InitConstants();

    model_time = 0.0;

    Button_ = _databased_supervisor_Released;
    Cycle_ = _databased_supervisor_WaitForButtonPush;
    Lamp_ = _databased_supervisor_Off;
    Timer_ = _databased_supervisor_Idle;

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
void databased_supervisor_EngineTimeStep(double delta) {


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

