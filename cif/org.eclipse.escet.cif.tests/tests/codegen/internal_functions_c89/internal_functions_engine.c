/* CIF to C translation of internal_functions.cif
 * Generated file, DO NOT EDIT
 */

#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include "internal_functions_engine.h"

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
BoolType A4ITypeEquals(A4IType *left, A4IType *right) {
    if (left == right) return TRUE;
    return memcmp(left, right, sizeof(A4IType)) == 0;
}

/**
 * Extract an element from the array.
 * @param array Array with value to retrieve.
 * @param index Element index in the array (not normalized).
 * @return Element value at the indicated index from the array.
 */
IntType A4ITypeProject(A4IType *array, IntType index) {
    if (index < 0) index += 4; /* Normalize index. */
    assert(index >= 0 && index < 4);

    return array->data[index];
}

/**
 * In-place change of the array.
 * @param array Array to modify.
 * @param index Element index in the array (not normalized).
 * @param value New value to copy into the array.
 */
void A4ITypeModify(A4IType *array, IntType index, IntType value) {
    if (index < 0) index += 4; /* Normalize index. */
    assert(index >= 0 && index < 4);

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
int A4ITypePrint(A4IType *array, char *dest, int start, int end) {
    int last = end - 1;
    if (start < last) { dest[start++] = '['; }
    int index;
    for (index = 0; index < 4; index++) {
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
 * Compare two tuples for equality.
 * @param left First tuple to compare.
 * @param right Second tuple to compare.
 * @return Whether both tuples are the same.
 */
BoolType T2IRTypeEquals(T2IRType *left, T2IRType *right) {
    if (left == right) return TRUE;
    if (memcmp(&left->_field0, &right->_field0, sizeof(IntType)) != 0) return FALSE;
    if (memcmp(&left->_field1, &right->_field1, sizeof(RealType)) != 0) return FALSE;
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
int T2IRTypePrint(T2IRType *tuple, char *dest, int start, int end) {
    int last = end - 1;
    if (start < last) { dest[start++] = '('; }
    start = IntTypePrint(tuple->_field0, dest, start, end);
    if (start < last) { dest[start++] = ','; }
    if (start < last) { dest[start++] = ' '; }
    start = RealTypePrint(tuple->_field1, dest, start, end);
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


/** Event names. */
const char *internal_functions_event_names[] = {
    "initial-step", /**< Initial step. */
    "delay-step",   /**< Delay step. */
    "tau",          /**< Tau step. */
};

/** Enumeration names. */


/* Constants. */


/* Functions. */
IntType inc_(IntType inc_x_) {
    return IntegerAdd(inc_x_, 1);
    assert(0); /* Falling through the end of the function. */
}

IntType factorial_(IntType factorial_x_) {
    {
        IntType if_dest5;
        if ((factorial_x_) == (0)) {
            if_dest5 = 1;
        } else {
            if_dest5 = IntegerMultiply(factorial_x_, factorial_(IntegerSubtract(factorial_x_, 1)));
        }
        return if_dest5;
    }
    assert(0); /* Falling through the end of the function. */
}

IntType rec1_(IntType rec1_x_) {
    {
        IntType if_dest5;
        if ((rec1_x_) == (0)) {
            if_dest5 = 1;
        } else {
            if_dest5 = rec2_(IntegerSubtract(rec1_x_, 1));
        }
        return if_dest5;
    }
    assert(0); /* Falling through the end of the function. */
}

IntType rec2_(IntType rec2_x_) {
    {
        IntType if_dest5;
        if ((rec2_x_) == (0)) {
            if_dest5 = 2;
        } else {
            if_dest5 = rec1_(IntegerSubtract(rec2_x_, 1));
        }
        return if_dest5;
    }
    assert(0); /* Falling through the end of the function. */
}

T2IRType multi_return_() {
    {
        T2IRType tuple_tmp5;
        (tuple_tmp5)._field0 = 1;
        (tuple_tmp5)._field1 = 1.0;
        return tuple_tmp5;
    }
    assert(0); /* Falling through the end of the function. */
}

IntType f0_() {
    return 1;
    assert(0); /* Falling through the end of the function. */
}

IntType f1_(IntType f1_x_) {
    return f1_x_;
    assert(0); /* Falling through the end of the function. */
}

IntType f2_(IntType f2_x_, IntType f2_y_) {
    return IntegerAdd(f2_x_, f2_y_);
    assert(0); /* Falling through the end of the function. */
}

RealType f3_(IntType f3_x_, IntType f3_y_, RealType f3_z_) {
    return RealAdd(IntegerAdd(f3_x_, f3_y_), f3_z_);
    assert(0); /* Falling through the end of the function. */
}

IntType locals_(IntType locals_x_) {
    IntType locals_a_;
    locals_a_ = 5;
    IntType locals_c_;
    locals_c_ = locals_a_;
    IntType locals_b_;
    locals_b_ = IntegerAdd(locals_c_, locals_x_);

    return locals_b_;
    assert(0); /* Falling through the end of the function. */
}

A4IType rot1_(A4IType* rot1_x_tmp5) {
    A4IType rot1_x_ = *(rot1_x_tmp5);
    IntType rot1_tmp_;
    rot1_tmp_ = A4ITypeProject(&(rot1_x_), 0);

    {
        IntType rhs6 = A4ITypeProject(&(rot1_x_), 3);
        IntType index7 = 0;
        A4ITypeModify(&rot1_x_, index7, rhs6);
    }

    {
        IntType rhs6 = A4ITypeProject(&(rot1_x_), 2);
        IntType index7 = 1;
        A4ITypeModify(&rot1_x_, index7, rhs6);
    }

    {
        IntType rhs6 = A4ITypeProject(&(rot1_x_), 1);
        IntType index7 = 2;
        A4ITypeModify(&rot1_x_, index7, rhs6);
    }

    {
        IntType rhs6 = rot1_tmp_;
        IntType index7 = 3;
        A4ITypeModify(&rot1_x_, index7, rhs6);
    }

    return rot1_x_;
    assert(0); /* Falling through the end of the function. */
}

A4IType rot2_(A4IType* rot2_x_tmp5) {
    A4IType rot2_x_ = *(rot2_x_tmp5);
    A4IType rot2_rslt_;
    (rot2_rslt_).data[0] = 0;
    (rot2_rslt_).data[1] = 0;
    (rot2_rslt_).data[2] = 0;
    (rot2_rslt_).data[3] = 0;

    {
        IntType rhs6 = A4ITypeProject(&(rot2_x_), 3);
        IntType index7 = 0;
        A4ITypeModify(&rot2_rslt_, index7, rhs6);
    }

    {
        IntType rhs6 = A4ITypeProject(&(rot2_x_), 2);
        IntType index7 = 1;
        A4ITypeModify(&rot2_rslt_, index7, rhs6);
    }

    {
        IntType rhs6 = A4ITypeProject(&(rot2_x_), 1);
        IntType index7 = 2;
        A4ITypeModify(&rot2_rslt_, index7, rhs6);
    }

    {
        IntType rhs6 = A4ITypeProject(&(rot2_x_), 0);
        IntType index7 = 3;
        A4ITypeModify(&rot2_rslt_, index7, rhs6);
    }

    return rot2_x_;
    assert(0); /* Falling through the end of the function. */
}

IntType fa_(IntType fa_x_) {
    A3IType fa_y_;
    (fa_y_).data[0] = fa_x_;
    (fa_y_).data[1] = fa_x_;
    (fa_y_).data[2] = fa_x_;
    IntType fa_a_;
    fa_a_ = fa_x_;
    IntType fa_b_;
    fa_b_ = IntegerAdd(fa_x_, 1);
    T2IIType fa_t_;
    (fa_t_)._field0 = 0;
    (fa_t_)._field1 = 0;

    {
        IntType rhs5 = 1;
        IntType index6 = 0;
        A3ITypeModify(&fa_y_, index6, rhs5);
    }
    {
        IntType rhs5 = 2;
        IntType index6 = 1;
        A3ITypeModify(&fa_y_, index6, rhs5);
    }

    (fa_y_).data[0] = IntegerAdd(A3ITypeProject(&(fa_y_), 0), 1);
    (fa_y_).data[1] = A3ITypeProject(&(fa_y_), 1);
    (fa_y_).data[2] = A3ITypeProject(&(fa_y_), 2);

    {
        IntType fa_a_tmp5 = fa_a_;
        fa_a_ = fa_b_;
        fa_b_ = fa_a_tmp5;
    }

    (fa_t_)._field0 = IntegerAdd(fa_a_, fa_b_);
    (fa_t_)._field1 = IntegerSubtract(fa_b_, fa_a_);

    {
        T2IIType rhs5 = fa_t_;
        fa_a_ = (rhs5)._field0;
        fa_b_ = (rhs5)._field1;
    }

    fa_x_ = IntegerAdd(IntegerAdd(fa_a_, fa_b_), A3ITypeProject(&(fa_y_), 0));

    return fa_x_;
    assert(0); /* Falling through the end of the function. */
}

IntType fi_(IntType fi_x_) {
    if ((fi_x_) == (1)) {
        fi_x_ = IntegerAdd(fi_x_, 1);
    }

    if ((fi_x_) == (1)) {
        fi_x_ = IntegerAdd(fi_x_, 1);
    } else if ((fi_x_) == (2)) {
        fi_x_ = IntegerAdd(fi_x_, 2);
    }

    if ((fi_x_) == (2)) {
        fi_x_ = IntegerAdd(fi_x_, 1);
    } else if ((fi_x_) == (3)) {
        fi_x_ = IntegerAdd(fi_x_, 2);
    } else if ((fi_x_) == (4)) {
        fi_x_ = IntegerAdd(fi_x_, 3);
    }

    if ((fi_x_) == (2)) {
        fi_x_ = IntegerAdd(fi_x_, 1);
    } else if ((fi_x_) == (3)) {
        fi_x_ = IntegerAdd(fi_x_, 2);
    } else {
        fi_x_ = IntegerAdd(fi_x_, 4);
    }

    if ((fi_x_) == (6)) {
        fi_x_ = IntegerAdd(fi_x_, 1);
    } else {
        fi_x_ = IntegerAdd(fi_x_, 2);
    }

    if ((fi_x_) > (4)) {
        if ((fi_x_) < (6)) {
            fi_x_ = IntegerSubtract(fi_x_, 1);
        } else {
            fi_x_ = IntegerSubtract(fi_x_, 2);
        }
    }

    return fi_x_;
    assert(0); /* Falling through the end of the function. */
}

IntType fw_() {
    IntType fw_x_;
    fw_x_ = 0;

    while ((fw_x_) > (0)) {
        while ((fw_x_) < (10)) {
            if (((fw_x_) % (2)) == (1)) {
                continue;
            }

            if ((fw_x_) == (8)) {
                break;
            }
        }
    }

    return fw_x_;
    assert(0); /* Falling through the end of the function. */
}

IntType fu1_() {
    if (TRUE) {
        return 1;
    }

    return 0;
    assert(0); /* Falling through the end of the function. */
}

IntType fu2_() {
    return 1;

    return 0;
    assert(0); /* Falling through the end of the function. */
}

IntType fu3_() {
    while (TRUE) {
        return 1;
    }

    return 0;
    assert(0); /* Falling through the end of the function. */
}

IntType fr_() {
    A3IType fr_x_;
    (fr_x_).data[0] = 1;
    (fr_x_).data[1] = 2;
    (fr_x_).data[2] = 3;
    A3IType fr_y_;
    (fr_y_).data[0] = 2;
    (fr_y_).data[1] = 3;
    (fr_y_).data[2] = 4;

    {
        A3IType rhs5 = fr_y_;
        #if CHECK_RANGES
        {
            int rng_index0;
            for(rng_index0 = 0; rng_index0 < 3; rng_index0++) {
                IntType rng_elem0 = (rhs5).data[rng_index0];
                if ((rng_elem0) > 3) {
                    fprintf(stderr, "RangeError: Writing %d into \"list[3] int[0..3]\"\n", rng_elem0);
                    fprintf(stderr, "            at " "fr.x" "[%d]" "\n", rng_index0);
                    RangeErrorDetected();
                }
            }
        }
        #endif
        fr_x_ = rhs5;
    }

    {
        A3IType array_tmp6;
        (array_tmp6).data[0] = -(1);
        (array_tmp6).data[1] = 3;
        (array_tmp6).data[2] = 5;
        A3IType rhs5 = array_tmp6;
        #if CHECK_RANGES
        {
            int rng_index0;
            for(rng_index0 = 0; rng_index0 < 3; rng_index0++) {
                IntType rng_elem0 = (rhs5).data[rng_index0];
                if ((rng_elem0) < 0 || (rng_elem0) > 3) {
                    fprintf(stderr, "RangeError: Writing %d into \"list[3] int[0..3]\"\n", rng_elem0);
                    fprintf(stderr, "            at " "fr.x" "[%d]" "\n", rng_index0);
                    RangeErrorDetected();
                }
            }
        }
        #endif
        fr_x_ = rhs5;
    }

    return 1;
    assert(0); /* Falling through the end of the function. */
}

/* Input variables. */


/* State variables. */
IntType aut_v00_;   /**< Discrete variable "int aut.v00". */
IntType aut_v01_;   /**< Discrete variable "int aut.v01". */
IntType aut_v02_;   /**< Discrete variable "int aut.v02". */
IntType aut_v03_;   /**< Discrete variable "int aut.v03". */
IntType aut_v04_;   /**< Discrete variable "int aut.v04". */
IntType aut_v05_;   /**< Discrete variable "int aut.v05". */
IntType aut_v06_;   /**< Discrete variable "int aut.v06". */
IntType aut_v07_;   /**< Discrete variable "int aut.v07". */
IntType aut_v08_;   /**< Discrete variable "int aut.v08". */
RealType aut_v09_;  /**< Discrete variable "real aut.v09". */
IntType aut_v10_;   /**< Discrete variable "int aut.v10". */
A4IType aut_v11_;   /**< Discrete variable "list[4] int aut.v11". */
A4IType aut_v12_;   /**< Discrete variable "list[4] int aut.v12". */
IntType aut_v13_;   /**< Discrete variable "int aut.v13". */
IntType aut_v14_;   /**< Discrete variable "int aut.v14". */
IntType aut_v15_;   /**< Discrete variable "int aut.v15". */
IntType aut_v16_;   /**< Discrete variable "int aut.v16". */
IntType aut_v17_;   /**< Discrete variable "int aut.v17". */
IntType aut_v18_;   /**< Discrete variable "int aut.v18". */
IntType aut_v19_;   /**< Discrete variable "int aut.v19". */
IntType aut_combi_; /**< Discrete variable "int aut.combi". */

/* Derivative and algebraic variable functions. */



RealType model_time; /**< Current model time. */

/** Initialize constants. */
static void InitConstants(void) {

}

/** Print function. */
#if PRINT_OUTPUT
static void PrintOutput(internal_functions_Event_ event, BoolType pre) {
    StringType text_var5;

    if (!pre) {
        IntTypePrint(aut_combi_, text_var5.data, 0, MAX_STRING_SIZE);
        internal_functions_PrintOutput(text_var5.data, ":stdout");
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
void internal_functions_EngineFirstStep(void) {
    InitConstants();

    model_time = 0.0;

    aut_v00_ = 5;
    aut_v01_ = inc_(aut_v00_);
    aut_v02_ = factorial_(5);
    aut_v03_ = rec1_(7);
    aut_v04_ = rec2_(7);
    T2IRType ret_val1 = multi_return_();
    T2IRType ret_val2 = multi_return_();
    aut_v05_ = IntegerAdd((ret_val1)._field0, FloorFunction((ret_val2)._field1));
    aut_v06_ = f0_();
    aut_v07_ = f1_(1);
    aut_v08_ = f2_(1, 2);
    aut_v09_ = f3_(1, 2, 3.0);
    aut_v10_ = locals_(1);
    A4IType array_tmp3;
    (array_tmp3).data[0] = 1;
    (array_tmp3).data[1] = 2;
    (array_tmp3).data[2] = 3;
    (array_tmp3).data[3] = 4;
    aut_v11_ = rot1_(&(array_tmp3));
    A4IType array_tmp4;
    (array_tmp4).data[0] = 1;
    (array_tmp4).data[1] = 2;
    (array_tmp4).data[2] = 3;
    (array_tmp4).data[3] = 4;
    aut_v12_ = rot2_(&(array_tmp4));
    aut_v13_ = fa_(1);
    aut_v14_ = fi_(1);
    aut_v15_ = fw_();
    aut_v16_ = fu1_();
    aut_v17_ = fu2_();
    aut_v18_ = fu3_();
    aut_v19_ = fr_();
    aut_combi_ = IntegerAdd(IntegerAdd(IntegerAdd(IntegerAdd(IntegerAdd(IntegerAdd(IntegerAdd(IntegerAdd(IntegerAdd(IntegerAdd(IntegerAdd(IntegerAdd(IntegerAdd(IntegerAdd(IntegerAdd(IntegerAdd(IntegerAdd(IntegerAdd(IntegerAdd(aut_v00_, aut_v01_), aut_v02_), aut_v03_), aut_v04_), aut_v05_), aut_v06_), aut_v07_), aut_v08_), FloorFunction(aut_v09_)), aut_v10_), A4ITypeProject(&(aut_v11_), 0)), A4ITypeProject(&(aut_v12_), 0)), aut_v13_), aut_v14_), aut_v15_), aut_v16_), aut_v17_), aut_v18_), aut_v19_);

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
void internal_functions_EngineTimeStep(double delta) {


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

