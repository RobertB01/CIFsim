/* CIF to C translation of ranges.cif
 * Generated file, DO NOT EDIT
 */

#include <stdio.h>
#include <stdlib.h>
#include "ranges_engine.h"

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

/**
 * Compare two arrays for equality.
 * @param left First array to compare.
 * @param right Second array to compare.
 * @return Whether both arrays are the same.
 */
BoolType A3T2IITypeEquals(A3T2IIType *left, A3T2IIType *right) {
    if (left == right) return TRUE;
    int i;
    for (i = 0; i < 3; i++) {
        if (!(T2IITypeEquals(&(left->data[i]), &(right->data[i])))) return FALSE;
    }
    return TRUE;
}

/**
 * Extract an element from the array.
 * @param array Array with value to retrieve.
 * @param index Element index in the array (not normalized).
 * @return Element value at the indicated index from the array.
 */
T2IIType *A3T2IITypeProject(A3T2IIType *array, IntType index) {
    if (index < 0) index += 3; /* Normalize index. */
    assert(index >= 0 && index < 3);

    return &array->data[index];
}

/**
 * In-place change of the array.
 * @param array Array to modify.
 * @param index Element index in the array (not normalized).
 * @param value New value to copy into the array.
 */
void A3T2IITypeModify(A3T2IIType *array, IntType index, T2IIType *value) {
    if (index < 0) index += 3; /* Normalize index. */
    assert(index >= 0 && index < 3);

    memcpy(&array->data[index], value, sizeof(T2IIType));
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
int A3T2IITypePrint(A3T2IIType *array, char *dest, int start, int end) {
    int last = end - 1;
    if (start < last) { dest[start++] = '['; }
    int index;
    for (index = 0; index < 3; index++) {
        if (index > 0) {
            if (start < last) { dest[start++] = ','; }
            if (start < last) { dest[start++] = ' '; }
        }
        start = T2IITypePrint(&array->data[index], dest, start, end);
    }
    if (start < last) { dest[start++] = ']'; }
    dest[start] = '\0';
    return start;
}

int EnumTypePrint(rangesEnum value, char *dest, int start, int end) {
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
const char *ranges_event_names[] = {
    "initial-step", /**< Initial step. */
    "delay-step",   /**< Delay step. */
    "tau",          /**< Tau step. */
    "e11",          /**< Event e11. */
    "e12",          /**< Event e12. */
    "e13",          /**< Event e13. */
    "e14",          /**< Event e14. */
    "e15",          /**< Event e15. */
    "e16",          /**< Event e16. */
    "e17",          /**< Event e17. */
    "e18",          /**< Event e18. */
    "e21",          /**< Event e21. */
    "e22",          /**< Event e22. */
    "e23",          /**< Event e23. */
    "e24",          /**< Event e24. */
    "e25",          /**< Event e25. */
    "e26",          /**< Event e26. */
    "e27",          /**< Event e27. */
    "e28",          /**< Event e28. */
    "e31",          /**< Event e31. */
    "e32",          /**< Event e32. */
    "e33",          /**< Event e33. */
    "e34",          /**< Event e34. */
    "e35",          /**< Event e35. */
    "e36",          /**< Event e36. */
    "e37",          /**< Event e37. */
    "e38",          /**< Event e38. */
    "e41",          /**< Event e41. */
};

/** Enumeration names. */
const char *enum_names[] = {
    "X",
};

/* Constants. */


/* Functions. */


/* Input variables. */


/* State variables. */
A3T2IIType aut1_v1_; /**< Discrete variable "list[3] tuple(int[0..7] a; int[0..7] b) aut1.v1". */
A3T2IIType aut1_v2_; /**< Discrete variable "list[3] tuple(int[0..7] a; int[0..7] b) aut1.v2". */
A3T2IIType aut1_w1_; /**< Discrete variable "list[3] tuple(int[1..8] a; int[1..8] b) aut1.w1". */
IntType aut1_x1_;    /**< Discrete variable "int[0..4] aut1.x1". */
IntType aut1_x2_;    /**< Discrete variable "int[0..9] aut1.x2". */
IntType aut1_x3_;    /**< Discrete variable "int[-1..4] aut1.x3". */
IntType aut1_x4_;    /**< Discrete variable "int[-1..9] aut1.x4". */
rangesEnum aut1_;    /**< Discrete variable "E aut1". */

RealType model_time; /**< Current model time. */

/** Initialize constants. */
static void InitConstants(void) {

}

/** Print function. */
#if PRINT_OUTPUT
static void PrintOutput(ranges_Event_ event, BoolType pre) {
}
#endif

/* Event execution code. */

/**
 * Execute code for event "e11".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent0(void) {
    BoolType guard = (aut1_) == (_ranges_X);
    if (!guard) return FALSE;

    #if EVENT_OUTPUT
        ranges_InfoEvent(e11_, TRUE);
    #endif

    aut1_v1_ = aut1_v1_;

    #if EVENT_OUTPUT
        ranges_InfoEvent(e11_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e12".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent1(void) {
    BoolType guard = (aut1_) == (_ranges_X);
    if (!guard) return FALSE;

    #if EVENT_OUTPUT
        ranges_InfoEvent(e12_, TRUE);
    #endif

    aut1_v1_ = aut1_v2_;

    #if EVENT_OUTPUT
        ranges_InfoEvent(e12_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e13".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent2(void) {
    BoolType guard = (aut1_) == (_ranges_X);
    if (!guard) return FALSE;

    #if EVENT_OUTPUT
        ranges_InfoEvent(e13_, TRUE);
    #endif

    {
        A3T2IIType rhs2 = aut1_w1_;
        #if CHECK_RANGES
        for(int rng_index0 = 0; rng_index0 < 3; rng_index0++) {
            T2IIType *rng_elem0 = &((rhs2).data[rng_index0]);
            if (((*(rng_elem0))._field0) > 7) {
                fprintf(stderr, "RangeError: Writing %d into \"list[3] tuple(int[0..7] a; int[0..7] b)\"\n", (*(rng_elem0))._field0);
                fprintf(stderr, "            at " "aut1.v1" "[%d][a]" "\n", rng_index0);
                RangeErrorDetected();
            }
            if (((*(rng_elem0))._field1) > 7) {
                fprintf(stderr, "RangeError: Writing %d into \"list[3] tuple(int[0..7] a; int[0..7] b)\"\n", (*(rng_elem0))._field1);
                fprintf(stderr, "            at " "aut1.v1" "[%d][b]" "\n", rng_index0);
                RangeErrorDetected();
            }
        }
        #endif
        aut1_v1_ = rhs2;
    }

    #if EVENT_OUTPUT
        ranges_InfoEvent(e13_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e14".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent3(void) {
    BoolType guard = (aut1_) == (_ranges_X);
    if (!guard) return FALSE;

    #if EVENT_OUTPUT
        ranges_InfoEvent(e14_, TRUE);
    #endif

    ((aut1_v1_).data[0])._field0 = 1;
    ((aut1_v1_).data[0])._field1 = 2;
    ((aut1_v1_).data[1])._field0 = 2;
    ((aut1_v1_).data[1])._field1 = 3;
    ((aut1_v1_).data[2])._field0 = 3;
    ((aut1_v1_).data[2])._field1 = 4;

    #if EVENT_OUTPUT
        ranges_InfoEvent(e14_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e15".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent4(void) {
    BoolType guard = (aut1_) == (_ranges_X);
    if (!guard) return FALSE;

    #if EVENT_OUTPUT
        ranges_InfoEvent(e15_, TRUE);
    #endif

    {
        A3T2IIType array_tmp3;
        ((array_tmp3).data[0])._field0 = 1;
        ((array_tmp3).data[0])._field1 = 2;
        ((array_tmp3).data[1])._field0 = 2;
        ((array_tmp3).data[1])._field1 = 3;
        ((array_tmp3).data[2])._field0 = 9;
        ((array_tmp3).data[2])._field1 = 4;
        A3T2IIType rhs2 = array_tmp3;
        #if CHECK_RANGES
        for(int rng_index0 = 0; rng_index0 < 3; rng_index0++) {
            T2IIType *rng_elem0 = &((rhs2).data[rng_index0]);
            if (((*(rng_elem0))._field0) > 7) {
                fprintf(stderr, "RangeError: Writing %d into \"list[3] tuple(int[0..7] a; int[0..7] b)\"\n", (*(rng_elem0))._field0);
                fprintf(stderr, "            at " "aut1.v1" "[%d][a]" "\n", rng_index0);
                RangeErrorDetected();
            }
        }
        #endif
        aut1_v1_ = rhs2;
    }

    #if EVENT_OUTPUT
        ranges_InfoEvent(e15_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e16".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent5(void) {
    BoolType guard = (aut1_) == (_ranges_X);
    if (!guard) return FALSE;

    #if EVENT_OUTPUT
        ranges_InfoEvent(e16_, TRUE);
    #endif

    {
        A3T2IIType array_tmp3;
        ((array_tmp3).data[0])._field0 = 1;
        ((array_tmp3).data[0])._field1 = 2;
        ((array_tmp3).data[1])._field0 = 2;
        ((array_tmp3).data[1])._field1 = 3;
        ((array_tmp3).data[2])._field0 = 3;
        ((array_tmp3).data[2])._field1 = 9;
        A3T2IIType rhs2 = array_tmp3;
        #if CHECK_RANGES
        for(int rng_index0 = 0; rng_index0 < 3; rng_index0++) {
            T2IIType *rng_elem0 = &((rhs2).data[rng_index0]);
            if (((*(rng_elem0))._field1) > 7) {
                fprintf(stderr, "RangeError: Writing %d into \"list[3] tuple(int[0..7] a; int[0..7] b)\"\n", (*(rng_elem0))._field1);
                fprintf(stderr, "            at " "aut1.v1" "[%d][b]" "\n", rng_index0);
                RangeErrorDetected();
            }
        }
        #endif
        aut1_v1_ = rhs2;
    }

    #if EVENT_OUTPUT
        ranges_InfoEvent(e16_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e17".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent6(void) {
    BoolType guard = (aut1_) == (_ranges_X);
    if (!guard) return FALSE;

    #if EVENT_OUTPUT
        ranges_InfoEvent(e17_, TRUE);
    #endif

    {
        A3T2IIType array_tmp3;
        ((array_tmp3).data[0])._field0 = 1;
        ((array_tmp3).data[0])._field1 = 2;
        ((array_tmp3).data[1])._field0 = 2;
        ((array_tmp3).data[1])._field1 = 3;
        ((array_tmp3).data[2])._field0 = -(1);
        ((array_tmp3).data[2])._field1 = 4;
        A3T2IIType rhs2 = array_tmp3;
        #if CHECK_RANGES
        for(int rng_index0 = 0; rng_index0 < 3; rng_index0++) {
            T2IIType *rng_elem0 = &((rhs2).data[rng_index0]);
            if (((*(rng_elem0))._field0) < 0) {
                fprintf(stderr, "RangeError: Writing %d into \"list[3] tuple(int[0..7] a; int[0..7] b)\"\n", (*(rng_elem0))._field0);
                fprintf(stderr, "            at " "aut1.v1" "[%d][a]" "\n", rng_index0);
                RangeErrorDetected();
            }
        }
        #endif
        aut1_v1_ = rhs2;
    }

    #if EVENT_OUTPUT
        ranges_InfoEvent(e17_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e18".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent7(void) {
    BoolType guard = (aut1_) == (_ranges_X);
    if (!guard) return FALSE;

    #if EVENT_OUTPUT
        ranges_InfoEvent(e18_, TRUE);
    #endif

    {
        A3T2IIType array_tmp3;
        ((array_tmp3).data[0])._field0 = 1;
        ((array_tmp3).data[0])._field1 = 2;
        ((array_tmp3).data[1])._field0 = -(3);
        ((array_tmp3).data[1])._field1 = 3;
        ((array_tmp3).data[2])._field0 = 9;
        ((array_tmp3).data[2])._field1 = 9;
        A3T2IIType rhs2 = array_tmp3;
        #if CHECK_RANGES
        for(int rng_index0 = 0; rng_index0 < 3; rng_index0++) {
            T2IIType *rng_elem0 = &((rhs2).data[rng_index0]);
            if (((*(rng_elem0))._field0) < 0 || ((*(rng_elem0))._field0) > 7) {
                fprintf(stderr, "RangeError: Writing %d into \"list[3] tuple(int[0..7] a; int[0..7] b)\"\n", (*(rng_elem0))._field0);
                fprintf(stderr, "            at " "aut1.v1" "[%d][a]" "\n", rng_index0);
                RangeErrorDetected();
            }
            if (((*(rng_elem0))._field1) > 7) {
                fprintf(stderr, "RangeError: Writing %d into \"list[3] tuple(int[0..7] a; int[0..7] b)\"\n", (*(rng_elem0))._field1);
                fprintf(stderr, "            at " "aut1.v1" "[%d][b]" "\n", rng_index0);
                RangeErrorDetected();
            }
        }
        #endif
        aut1_v1_ = rhs2;
    }

    #if EVENT_OUTPUT
        ranges_InfoEvent(e18_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e21".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent8(void) {
    BoolType guard = (aut1_) == (_ranges_X);
    if (!guard) return FALSE;

    #if EVENT_OUTPUT
        ranges_InfoEvent(e21_, TRUE);
    #endif

    {
        T2IIType rhs2 = *(A3T2IITypeProject(&(aut1_v1_), 2));
        IntType index3 = 2;
        A3T2IITypeModify(&aut1_v1_, index3, &(rhs2));
    }

    #if EVENT_OUTPUT
        ranges_InfoEvent(e21_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e22".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent9(void) {
    BoolType guard = (aut1_) == (_ranges_X);
    if (!guard) return FALSE;

    #if EVENT_OUTPUT
        ranges_InfoEvent(e22_, TRUE);
    #endif

    {
        T2IIType rhs2 = *(A3T2IITypeProject(&(aut1_v2_), 2));
        IntType index3 = 2;
        A3T2IITypeModify(&aut1_v1_, index3, &(rhs2));
    }

    #if EVENT_OUTPUT
        ranges_InfoEvent(e22_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e23".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent10(void) {
    BoolType guard = (aut1_) == (_ranges_X);
    if (!guard) return FALSE;

    #if EVENT_OUTPUT
        ranges_InfoEvent(e23_, TRUE);
    #endif

    {
        T2IIType rhs2 = *(A3T2IITypeProject(&(aut1_w1_), 2));
        IntType index3 = 2;
        #if CHECK_RANGES
        if (((rhs2)._field0) > 7) {
            fprintf(stderr, "RangeError: Writing %d into \"list[3] tuple(int[0..7] a; int[0..7] b)\"\n", (rhs2)._field0);
            fprintf(stderr, "            at " "aut1.v1" "[%d][a]" "\n", index3);
            RangeErrorDetected();
        }
        if (((rhs2)._field1) > 7) {
            fprintf(stderr, "RangeError: Writing %d into \"list[3] tuple(int[0..7] a; int[0..7] b)\"\n", (rhs2)._field1);
            fprintf(stderr, "            at " "aut1.v1" "[%d][b]" "\n", index3);
            RangeErrorDetected();
        }
        #endif
        A3T2IITypeModify(&aut1_v1_, index3, &(rhs2));
    }

    #if EVENT_OUTPUT
        ranges_InfoEvent(e23_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e24".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent11(void) {
    BoolType guard = (aut1_) == (_ranges_X);
    if (!guard) return FALSE;

    #if EVENT_OUTPUT
        ranges_InfoEvent(e24_, TRUE);
    #endif

    {
        T2IIType tuple_tmp3;
        (tuple_tmp3)._field0 = aut1_x1_;
        (tuple_tmp3)._field1 = 4;
        T2IIType rhs2 = tuple_tmp3;
        IntType index4 = 2;
        A3T2IITypeModify(&aut1_v1_, index4, &(rhs2));
    }

    #if EVENT_OUTPUT
        ranges_InfoEvent(e24_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e25".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent12(void) {
    BoolType guard = (aut1_) == (_ranges_X);
    if (!guard) return FALSE;

    #if EVENT_OUTPUT
        ranges_InfoEvent(e25_, TRUE);
    #endif

    {
        T2IIType tuple_tmp3;
        (tuple_tmp3)._field0 = aut1_x2_;
        (tuple_tmp3)._field1 = 4;
        T2IIType rhs2 = tuple_tmp3;
        IntType index4 = 2;
        #if CHECK_RANGES
        if (((rhs2)._field0) > 7) {
            fprintf(stderr, "RangeError: Writing %d into \"list[3] tuple(int[0..7] a; int[0..7] b)\"\n", (rhs2)._field0);
            fprintf(stderr, "            at " "aut1.v1" "[%d][a]" "\n", index4);
            RangeErrorDetected();
        }
        #endif
        A3T2IITypeModify(&aut1_v1_, index4, &(rhs2));
    }

    #if EVENT_OUTPUT
        ranges_InfoEvent(e25_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e26".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent13(void) {
    BoolType guard = (aut1_) == (_ranges_X);
    if (!guard) return FALSE;

    #if EVENT_OUTPUT
        ranges_InfoEvent(e26_, TRUE);
    #endif

    {
        T2IIType tuple_tmp3;
        (tuple_tmp3)._field0 = 4;
        (tuple_tmp3)._field1 = aut1_x2_;
        T2IIType rhs2 = tuple_tmp3;
        IntType index4 = 2;
        #if CHECK_RANGES
        if (((rhs2)._field1) > 7) {
            fprintf(stderr, "RangeError: Writing %d into \"list[3] tuple(int[0..7] a; int[0..7] b)\"\n", (rhs2)._field1);
            fprintf(stderr, "            at " "aut1.v1" "[%d][b]" "\n", index4);
            RangeErrorDetected();
        }
        #endif
        A3T2IITypeModify(&aut1_v1_, index4, &(rhs2));
    }

    #if EVENT_OUTPUT
        ranges_InfoEvent(e26_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e27".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent14(void) {
    BoolType guard = (aut1_) == (_ranges_X);
    if (!guard) return FALSE;

    #if EVENT_OUTPUT
        ranges_InfoEvent(e27_, TRUE);
    #endif

    {
        T2IIType tuple_tmp3;
        (tuple_tmp3)._field0 = aut1_x3_;
        (tuple_tmp3)._field1 = 4;
        T2IIType rhs2 = tuple_tmp3;
        IntType index4 = 2;
        #if CHECK_RANGES
        if (((rhs2)._field0) < 0) {
            fprintf(stderr, "RangeError: Writing %d into \"list[3] tuple(int[0..7] a; int[0..7] b)\"\n", (rhs2)._field0);
            fprintf(stderr, "            at " "aut1.v1" "[%d][a]" "\n", index4);
            RangeErrorDetected();
        }
        #endif
        A3T2IITypeModify(&aut1_v1_, index4, &(rhs2));
    }

    #if EVENT_OUTPUT
        ranges_InfoEvent(e27_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e28".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent15(void) {
    BoolType guard = (aut1_) == (_ranges_X);
    if (!guard) return FALSE;

    #if EVENT_OUTPUT
        ranges_InfoEvent(e28_, TRUE);
    #endif

    {
        T2IIType tuple_tmp3;
        (tuple_tmp3)._field0 = aut1_x4_;
        (tuple_tmp3)._field1 = 4;
        T2IIType rhs2 = tuple_tmp3;
        IntType index4 = 2;
        #if CHECK_RANGES
        if (((rhs2)._field0) < 0 || ((rhs2)._field0) > 7) {
            fprintf(stderr, "RangeError: Writing %d into \"list[3] tuple(int[0..7] a; int[0..7] b)\"\n", (rhs2)._field0);
            fprintf(stderr, "            at " "aut1.v1" "[%d][a]" "\n", index4);
            RangeErrorDetected();
        }
        #endif
        A3T2IITypeModify(&aut1_v1_, index4, &(rhs2));
    }

    #if EVENT_OUTPUT
        ranges_InfoEvent(e28_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e31".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent16(void) {
    BoolType guard = (aut1_) == (_ranges_X);
    if (!guard) return FALSE;

    #if EVENT_OUTPUT
        ranges_InfoEvent(e31_, TRUE);
    #endif

    {
        IntType rhs2 = (A3T2IITypeProject(&(aut1_v1_), 2))->_field0;
        IntType index3 = 2;
        T2IIType part4 = *(A3T2IITypeProject(&(aut1_v1_), index3));
        part4._field0 = rhs2;
        A3T2IITypeModify(&aut1_v1_, index3, &(part4));
    }

    #if EVENT_OUTPUT
        ranges_InfoEvent(e31_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e32".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent17(void) {
    BoolType guard = (aut1_) == (_ranges_X);
    if (!guard) return FALSE;

    #if EVENT_OUTPUT
        ranges_InfoEvent(e32_, TRUE);
    #endif

    {
        IntType rhs2 = (A3T2IITypeProject(&(aut1_v2_), 2))->_field0;
        IntType index3 = 2;
        T2IIType part4 = *(A3T2IITypeProject(&(aut1_v1_), index3));
        part4._field0 = rhs2;
        A3T2IITypeModify(&aut1_v1_, index3, &(part4));
    }

    #if EVENT_OUTPUT
        ranges_InfoEvent(e32_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e33".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent18(void) {
    BoolType guard = (aut1_) == (_ranges_X);
    if (!guard) return FALSE;

    #if EVENT_OUTPUT
        ranges_InfoEvent(e33_, TRUE);
    #endif

    {
        IntType rhs2 = (A3T2IITypeProject(&(aut1_w1_), 2))->_field0;
        IntType index3 = 2;
        #if CHECK_RANGES
        if ((rhs2) > 7) {
            fprintf(stderr, "RangeError: Writing %d into \"list[3] tuple(int[0..7] a; int[0..7] b)\"\n", rhs2);
            fprintf(stderr, "            at " "aut1.v1" "[%d][a]" "\n", index3);
            RangeErrorDetected();
        }
        #endif
        T2IIType part4 = *(A3T2IITypeProject(&(aut1_v1_), index3));
        part4._field0 = rhs2;
        A3T2IITypeModify(&aut1_v1_, index3, &(part4));
    }

    #if EVENT_OUTPUT
        ranges_InfoEvent(e33_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e34".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent19(void) {
    BoolType guard = (aut1_) == (_ranges_X);
    if (!guard) return FALSE;

    #if EVENT_OUTPUT
        ranges_InfoEvent(e34_, TRUE);
    #endif

    {
        IntType rhs2 = aut1_x1_;
        IntType index3 = 2;
        T2IIType part4 = *(A3T2IITypeProject(&(aut1_v1_), index3));
        part4._field0 = rhs2;
        A3T2IITypeModify(&aut1_v1_, index3, &(part4));
    }

    #if EVENT_OUTPUT
        ranges_InfoEvent(e34_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e35".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent20(void) {
    BoolType guard = (aut1_) == (_ranges_X);
    if (!guard) return FALSE;

    #if EVENT_OUTPUT
        ranges_InfoEvent(e35_, TRUE);
    #endif

    {
        IntType rhs2 = aut1_x2_;
        IntType index3 = 2;
        #if CHECK_RANGES
        if ((rhs2) > 7) {
            fprintf(stderr, "RangeError: Writing %d into \"list[3] tuple(int[0..7] a; int[0..7] b)\"\n", rhs2);
            fprintf(stderr, "            at " "aut1.v1" "[%d][a]" "\n", index3);
            RangeErrorDetected();
        }
        #endif
        T2IIType part4 = *(A3T2IITypeProject(&(aut1_v1_), index3));
        part4._field0 = rhs2;
        A3T2IITypeModify(&aut1_v1_, index3, &(part4));
    }

    #if EVENT_OUTPUT
        ranges_InfoEvent(e35_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e36".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent21(void) {
    BoolType guard = (aut1_) == (_ranges_X);
    if (!guard) return FALSE;

    #if EVENT_OUTPUT
        ranges_InfoEvent(e36_, TRUE);
    #endif

    {
        IntType rhs2 = aut1_x2_;
        IntType index3 = 2;
        #if CHECK_RANGES
        if ((rhs2) > 7) {
            fprintf(stderr, "RangeError: Writing %d into \"list[3] tuple(int[0..7] a; int[0..7] b)\"\n", rhs2);
            fprintf(stderr, "            at " "aut1.v1" "[%d][b]" "\n", index3);
            RangeErrorDetected();
        }
        #endif
        T2IIType part4 = *(A3T2IITypeProject(&(aut1_v1_), index3));
        part4._field1 = rhs2;
        A3T2IITypeModify(&aut1_v1_, index3, &(part4));
    }

    #if EVENT_OUTPUT
        ranges_InfoEvent(e36_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e37".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent22(void) {
    BoolType guard = (aut1_) == (_ranges_X);
    if (!guard) return FALSE;

    #if EVENT_OUTPUT
        ranges_InfoEvent(e37_, TRUE);
    #endif

    {
        IntType rhs2 = aut1_x3_;
        IntType index3 = 2;
        #if CHECK_RANGES
        if ((rhs2) < 0) {
            fprintf(stderr, "RangeError: Writing %d into \"list[3] tuple(int[0..7] a; int[0..7] b)\"\n", rhs2);
            fprintf(stderr, "            at " "aut1.v1" "[%d][a]" "\n", index3);
            RangeErrorDetected();
        }
        #endif
        T2IIType part4 = *(A3T2IITypeProject(&(aut1_v1_), index3));
        part4._field0 = rhs2;
        A3T2IITypeModify(&aut1_v1_, index3, &(part4));
    }

    #if EVENT_OUTPUT
        ranges_InfoEvent(e37_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e38".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent23(void) {
    BoolType guard = (aut1_) == (_ranges_X);
    if (!guard) return FALSE;

    #if EVENT_OUTPUT
        ranges_InfoEvent(e38_, TRUE);
    #endif

    {
        IntType rhs2 = aut1_x4_;
        IntType index3 = 2;
        #if CHECK_RANGES
        if ((rhs2) < 0 || (rhs2) > 7) {
            fprintf(stderr, "RangeError: Writing %d into \"list[3] tuple(int[0..7] a; int[0..7] b)\"\n", rhs2);
            fprintf(stderr, "            at " "aut1.v1" "[%d][a]" "\n", index3);
            RangeErrorDetected();
        }
        #endif
        T2IIType part4 = *(A3T2IITypeProject(&(aut1_v1_), index3));
        part4._field0 = rhs2;
        A3T2IITypeModify(&aut1_v1_, index3, &(part4));
    }

    #if EVENT_OUTPUT
        ranges_InfoEvent(e38_, FALSE);
    #endif
    return TRUE;
}

/**
 * Execute code for event "e41".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent24(void) {
    BoolType guard = (aut1_) == (_ranges_X);
    if (!guard) return FALSE;

    #if EVENT_OUTPUT
        ranges_InfoEvent(e41_, TRUE);
    #endif

    {
        IntType rhs2 = aut1_x4_;
        #if CHECK_RANGES
        if ((rhs2) > 4) {
            fprintf(stderr, "RangeError: Writing %d into \"int[-1..4]\"\n", rhs2);
            fprintf(stderr, "            at " "aut1.x3" "\n");
            RangeErrorDetected();
        }
        #endif
        aut1_x3_ = rhs2;
    }

    #if EVENT_OUTPUT
        ranges_InfoEvent(e41_, FALSE);
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

        if (execEvent0()) continue;  /* (Try to) perform event "e11". */
        if (execEvent1()) continue;  /* (Try to) perform event "e12". */
        if (execEvent2()) continue;  /* (Try to) perform event "e13". */
        if (execEvent3()) continue;  /* (Try to) perform event "e14". */
        if (execEvent4()) continue;  /* (Try to) perform event "e15". */
        if (execEvent5()) continue;  /* (Try to) perform event "e16". */
        if (execEvent6()) continue;  /* (Try to) perform event "e17". */
        if (execEvent7()) continue;  /* (Try to) perform event "e18". */
        if (execEvent8()) continue;  /* (Try to) perform event "e21". */
        if (execEvent9()) continue;  /* (Try to) perform event "e22". */
        if (execEvent10()) continue;  /* (Try to) perform event "e23". */
        if (execEvent11()) continue;  /* (Try to) perform event "e24". */
        if (execEvent12()) continue;  /* (Try to) perform event "e25". */
        if (execEvent13()) continue;  /* (Try to) perform event "e26". */
        if (execEvent14()) continue;  /* (Try to) perform event "e27". */
        if (execEvent15()) continue;  /* (Try to) perform event "e28". */
        if (execEvent16()) continue;  /* (Try to) perform event "e31". */
        if (execEvent17()) continue;  /* (Try to) perform event "e32". */
        if (execEvent18()) continue;  /* (Try to) perform event "e33". */
        if (execEvent19()) continue;  /* (Try to) perform event "e34". */
        if (execEvent20()) continue;  /* (Try to) perform event "e35". */
        if (execEvent21()) continue;  /* (Try to) perform event "e36". */
        if (execEvent22()) continue;  /* (Try to) perform event "e37". */
        if (execEvent23()) continue;  /* (Try to) perform event "e38". */
        if (execEvent24()) continue;  /* (Try to) perform event "e41". */
        break; /* No event fired, done with discrete steps. */
    }
}

/** First model call, initializing, and performing discrete events before the first time step. */
void ranges_EngineFirstStep(void) {
    InitConstants();

    model_time = 0.0;

    ((aut1_v1_).data[0])._field0 = 0;
    ((aut1_v1_).data[0])._field1 = 0;
    ((aut1_v1_).data[1])._field0 = 0;
    ((aut1_v1_).data[1])._field1 = 0;
    ((aut1_v1_).data[2])._field0 = 0;
    ((aut1_v1_).data[2])._field1 = 0;
    ((aut1_v2_).data[0])._field0 = 0;
    ((aut1_v2_).data[0])._field1 = 0;
    ((aut1_v2_).data[1])._field0 = 0;
    ((aut1_v2_).data[1])._field1 = 0;
    ((aut1_v2_).data[2])._field0 = 0;
    ((aut1_v2_).data[2])._field1 = 0;
    ((aut1_w1_).data[0])._field0 = 1;
    ((aut1_w1_).data[0])._field1 = 1;
    ((aut1_w1_).data[1])._field0 = 1;
    ((aut1_w1_).data[1])._field1 = 1;
    ((aut1_w1_).data[2])._field0 = 1;
    ((aut1_w1_).data[2])._field1 = 1;
    aut1_x1_ = 0;
    aut1_x2_ = 0;
    aut1_x3_ = 0;
    aut1_x4_ = 0;
    aut1_ = _ranges_X;

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
void ranges_EngineTimeStep(double delta) {


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

