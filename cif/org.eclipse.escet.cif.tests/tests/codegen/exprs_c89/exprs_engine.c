/* CIF to C translation of exprs.cif
 * Generated file, DO NOT EDIT
 */

#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include "exprs_engine.h"

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
int EnumTypePrint(exprsEnum value, char *dest, int start, int end) {
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
 * Compare two arrays for equality.
 * @param left First array to compare.
 * @param right Second array to compare.
 * @return Whether both arrays are the same.
 */
BoolType A1BTypeEquals(A1BType *left, A1BType *right) {
    if (left == right) return TRUE;
    return memcmp(left, right, sizeof(A1BType)) == 0;
}

/**
 * Extract an element from the array.
 * @param array Array with value to retrieve.
 * @param index Element index in the array (not normalized).
 * @return Element value at the indicated index from the array.
 */
BoolType A1BTypeProject(A1BType *array, IntType index) {
    if (index < 0) index += 1; /* Normalize index. */
    assert(index >= 0 && index < 1);

    return array->data[index];
}

/**
 * In-place change of the array.
 * @param array Array to modify.
 * @param index Element index in the array (not normalized).
 * @param value New value to copy into the array.
 */
void A1BTypeModify(A1BType *array, IntType index, BoolType value) {
    if (index < 0) index += 1; /* Normalize index. */
    assert(index >= 0 && index < 1);

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
int A1BTypePrint(A1BType *array, char *dest, int start, int end) {
    int last = end - 1;
    if (start < last) { dest[start++] = '['; }
    int index;
    for (index = 0; index < 1; index++) {
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
const char *exprs_event_names[] = {
    "initial-step", /**< Initial step. */
    "delay-step",   /**< Delay step. */
    "tau",          /**< Tau step. */
};

/** Enumeration names. */
const char *enum_names[] = {
    "A",
    "B",
};

/* Constants. */
IntType x1_; /**< Constant "x1". */

/* Functions. */
IntType f1_(IntType f1_x_) {
    while (((f1_x_) != (0)) && ((f1_x_) != (4))) {
        f1_x_ = IntegerSubtract(f1_x_, 1);
    }

    if (((f1_x_) != (1)) && ((f1_x_) != (2))) {
        f1_x_ = 3;
    } else if (((f1_x_) != (2)) && ((f1_x_) != (3))) {
        f1_x_ = 4;
    }

    return f1_x_;
    assert(0); /* Falling through the end of the function. */
}

IntType inc_(IntType inc_x_) {
    return IntegerAdd(inc_x_, 1);
    assert(0); /* Falling through the end of the function. */
}

/* Input variables. */

/** Input variable "int x8". */
IntType x8_;

/* State variables. */
RealType x5_;           /**< Continuous variable "real x5". */
IntType a1_x_;          /**< Discrete variable "int a1.x". */
BoolType AA_vb_;        /**< Discrete variable "bool AA.vb". */
IntType AA_vi_;         /**< Discrete variable "int AA.vi". */
IntType AA_vp_;         /**< Discrete variable "int[1..3] AA.vp". */
IntType AA_vn_;         /**< Discrete variable "int[-5..-1] AA.vn". */
IntType AA_vz_;         /**< Discrete variable "int[0..5] AA.vz". */
RealType AA_vr_;        /**< Discrete variable "real AA.vr". */
StringType AA_vs_;      /**< Discrete variable "string AA.vs". */
exprsEnum AA_ve_;       /**< Discrete variable "E AA.ve". */
A2IType AA_va_;         /**< Discrete variable "list[2] int AA.va". */
RealType AA_v2_;        /**< Discrete variable "real AA.v2". */
RealType AA_i2r_;       /**< Discrete variable "real AA.i2r". */
StringType AA_b2s_;     /**< Discrete variable "string AA.b2s". */
StringType AA_i2s_;     /**< Discrete variable "string AA.i2s". */
StringType AA_r2s_;     /**< Discrete variable "string AA.r2s". */
BoolType AA_s2b_;       /**< Discrete variable "bool AA.s2b". */
IntType AA_s2i_;        /**< Discrete variable "int AA.s2i". */
RealType AA_s2r_;       /**< Discrete variable "real AA.s2r". */
A3IType AA_self_cast1_; /**< Discrete variable "list[3] int AA.self_cast1". */
A3IType AA_self_cast2_; /**< Discrete variable "list[3] int AA.self_cast2". */
BoolType AA_inv1_;      /**< Discrete variable "bool AA.inv1". */
BoolType AA_inv2_;      /**< Discrete variable "bool AA.inv2". */
IntType AA_neg1_;       /**< Discrete variable "int AA.neg1". */
IntType AA_neg2_;       /**< Discrete variable "int AA.neg2". */
IntType AA_neg3_;       /**< Discrete variable "int AA.neg3". */
IntType AA_neg4_;       /**< Discrete variable "int AA.neg4". */
IntType AA_pos1_;       /**< Discrete variable "int AA.pos1". */
IntType AA_pos2_;       /**< Discrete variable "int AA.pos2". */
IntType AA_posneg_;     /**< Discrete variable "int AA.posneg". */
A1BType AA_l3i_;        /**< Discrete variable "list[1] bool AA.l3i". */
IntType AA_idx1_;       /**< Discrete variable "int[0..4] AA.idx1". */
BoolType AA_vt_;        /**< Discrete variable "bool AA.vt". */
BoolType AA_vf_;        /**< Discrete variable "bool AA.vf". */
BoolType AA_short_and_; /**< Discrete variable "bool AA.short_and". */
BoolType AA_short_or_;  /**< Discrete variable "bool AA.short_or". */
BoolType AA_impl_;      /**< Discrete variable "bool AA.impl". */
BoolType AA_biimpl_;    /**< Discrete variable "bool AA.biimpl". */
BoolType AA_conj_;      /**< Discrete variable "bool AA.conj". */
BoolType AA_disj_;      /**< Discrete variable "bool AA.disj". */
BoolType AA_lt1_;       /**< Discrete variable "bool AA.lt1". */
BoolType AA_le1_;       /**< Discrete variable "bool AA.le1". */
BoolType AA_gt1_;       /**< Discrete variable "bool AA.gt1". */
BoolType AA_ge1_;       /**< Discrete variable "bool AA.ge1". */
BoolType AA_lt2_;       /**< Discrete variable "bool AA.lt2". */
BoolType AA_le2_;       /**< Discrete variable "bool AA.le2". */
BoolType AA_gt2_;       /**< Discrete variable "bool AA.gt2". */
BoolType AA_ge2_;       /**< Discrete variable "bool AA.ge2". */
BoolType AA_lt3_;       /**< Discrete variable "bool AA.lt3". */
BoolType AA_le3_;       /**< Discrete variable "bool AA.le3". */
BoolType AA_gt3_;       /**< Discrete variable "bool AA.gt3". */
BoolType AA_ge3_;       /**< Discrete variable "bool AA.ge3". */
BoolType AA_lt4_;       /**< Discrete variable "bool AA.lt4". */
BoolType AA_le4_;       /**< Discrete variable "bool AA.le4". */
BoolType AA_gt4_;       /**< Discrete variable "bool AA.gt4". */
BoolType AA_ge4_;       /**< Discrete variable "bool AA.ge4". */
BoolType AA_eq1_;       /**< Discrete variable "bool AA.eq1". */
BoolType AA_eq2_;       /**< Discrete variable "bool AA.eq2". */
BoolType AA_eq3_;       /**< Discrete variable "bool AA.eq3". */
BoolType AA_eq4_;       /**< Discrete variable "bool AA.eq4". */
BoolType AA_eq5_;       /**< Discrete variable "bool AA.eq5". */
BoolType AA_ne1_;       /**< Discrete variable "bool AA.ne1". */
BoolType AA_ne2_;       /**< Discrete variable "bool AA.ne2". */
BoolType AA_ne3_;       /**< Discrete variable "bool AA.ne3". */
BoolType AA_ne4_;       /**< Discrete variable "bool AA.ne4". */
BoolType AA_ne5_;       /**< Discrete variable "bool AA.ne5". */
IntType AA_add1_;       /**< Discrete variable "int AA.add1". */
RealType AA_add2_;      /**< Discrete variable "real AA.add2". */
RealType AA_add3_;      /**< Discrete variable "real AA.add3". */
RealType AA_add4_;      /**< Discrete variable "real AA.add4". */
StringType AA_add5_;    /**< Discrete variable "string AA.add5". */
IntType AA_add6_;       /**< Discrete variable "int AA.add6". */
IntType AA_add7_;       /**< Discrete variable "int AA.add7". */
IntType AA_add8_;       /**< Discrete variable "int AA.add8". */
IntType AA_sub1_;       /**< Discrete variable "int AA.sub1". */
RealType AA_sub2_;      /**< Discrete variable "real AA.sub2". */
RealType AA_sub3_;      /**< Discrete variable "real AA.sub3". */
RealType AA_sub4_;      /**< Discrete variable "real AA.sub4". */
IntType AA_sub5_;       /**< Discrete variable "int AA.sub5". */
IntType AA_sub6_;       /**< Discrete variable "int AA.sub6". */
IntType AA_sub7_;       /**< Discrete variable "int AA.sub7". */
IntType AA_mul1_;       /**< Discrete variable "int AA.mul1". */
RealType AA_mul2_;      /**< Discrete variable "real AA.mul2". */
RealType AA_mul3_;      /**< Discrete variable "real AA.mul3". */
RealType AA_mul4_;      /**< Discrete variable "real AA.mul4". */
IntType AA_mul5_;       /**< Discrete variable "int AA.mul5". */
IntType AA_mul6_;       /**< Discrete variable "int AA.mul6". */
IntType AA_mul7_;       /**< Discrete variable "int AA.mul7". */
RealType AA_rdiv1_;     /**< Discrete variable "real AA.rdiv1". */
RealType AA_rdiv2_;     /**< Discrete variable "real AA.rdiv2". */
RealType AA_rdiv3_;     /**< Discrete variable "real AA.rdiv3". */
RealType AA_rdiv4_;     /**< Discrete variable "real AA.rdiv4". */
RealType AA_rdiv5_;     /**< Discrete variable "real AA.rdiv5". */
RealType AA_rdiv6_;     /**< Discrete variable "real AA.rdiv6". */
IntType AA_div1_;       /**< Discrete variable "int AA.div1". */
IntType AA_div2_;       /**< Discrete variable "int AA.div2". */
IntType AA_div3_;       /**< Discrete variable "int AA.div3". */
IntType AA_div4_;       /**< Discrete variable "int AA.div4". */
IntType AA_mod1_;       /**< Discrete variable "int AA.mod1". */
IntType AA_mod2_;       /**< Discrete variable "int AA.mod2". */
A2IType AA_li_;         /**< Discrete variable "list[2] int AA.li". */
T2IIType AA_tii_;       /**< Discrete variable "tuple(int a; int b) AA.tii". */
StringType AA_ss_;      /**< Discrete variable "string AA.ss". */
IntType AA_proj1_;      /**< Discrete variable "int AA.proj1". */
IntType AA_proj2_;      /**< Discrete variable "int AA.proj2". */
IntType AA_proj3_;      /**< Discrete variable "int AA.proj3". */
IntType AA_proj4_;      /**< Discrete variable "int AA.proj4". */
StringType AA_proj5_;   /**< Discrete variable "string AA.proj5". */
StringType AA_proj6_;   /**< Discrete variable "string AA.proj6". */
RealType AA_f_acos_;    /**< Discrete variable "real AA.f_acos". */
RealType AA_f_asin_;    /**< Discrete variable "real AA.f_asin". */
RealType AA_f_atan_;    /**< Discrete variable "real AA.f_atan". */
RealType AA_f_cos_;     /**< Discrete variable "real AA.f_cos". */
RealType AA_f_sin_;     /**< Discrete variable "real AA.f_sin". */
RealType AA_f_tan_;     /**< Discrete variable "real AA.f_tan". */
IntType AA_f_abs1_;     /**< Discrete variable "int AA.f_abs1". */
IntType AA_f_abs12_;    /**< Discrete variable "int AA.f_abs12". */
RealType AA_f_abs2_;    /**< Discrete variable "real AA.f_abs2". */
RealType AA_f_cbrt_;    /**< Discrete variable "real AA.f_cbrt". */
IntType AA_f_ceil_;     /**< Discrete variable "int AA.f_ceil". */
BoolType AA_f_empty_;   /**< Discrete variable "bool AA.f_empty". */
RealType AA_f_exp_;     /**< Discrete variable "real AA.f_exp". */
IntType AA_f_floor_;    /**< Discrete variable "int AA.f_floor". */
RealType AA_f_ln_;      /**< Discrete variable "real AA.f_ln". */
RealType AA_f_log_;     /**< Discrete variable "real AA.f_log". */
IntType AA_f_max1_;     /**< Discrete variable "int AA.f_max1". */
RealType AA_f_max2_;    /**< Discrete variable "real AA.f_max2". */
RealType AA_f_max3_;    /**< Discrete variable "real AA.f_max3". */
RealType AA_f_max4_;    /**< Discrete variable "real AA.f_max4". */
IntType AA_f_min1_;     /**< Discrete variable "int AA.f_min1". */
RealType AA_f_min2_;    /**< Discrete variable "real AA.f_min2". */
RealType AA_f_min3_;    /**< Discrete variable "real AA.f_min3". */
RealType AA_f_min4_;    /**< Discrete variable "real AA.f_min4". */
RealType AA_f_pow1_;    /**< Discrete variable "real AA.f_pow1". */
IntType AA_f_pow12_;    /**< Discrete variable "int AA.f_pow12". */
RealType AA_f_pow2_;    /**< Discrete variable "real AA.f_pow2". */
RealType AA_f_pow3_;    /**< Discrete variable "real AA.f_pow3". */
RealType AA_f_pow4_;    /**< Discrete variable "real AA.f_pow4". */
IntType AA_f_round_;    /**< Discrete variable "int AA.f_round". */
RealType AA_f_scale_;   /**< Discrete variable "real AA.f_scale". */
IntType AA_f_sign1_;    /**< Discrete variable "int AA.f_sign1". */
IntType AA_f_sign2_;    /**< Discrete variable "int AA.f_sign2". */
IntType AA_f_size1_;    /**< Discrete variable "int AA.f_size1". */
IntType AA_f_size2_;    /**< Discrete variable "int AA.f_size2". */
RealType AA_f_sqrt_;    /**< Discrete variable "real AA.f_sqrt". */

/* Derivative and algebraic variable functions. */
/** Derivative of "x5". */
RealType x5_deriv(void) {
    return 1.0;
}
/**
 * Algebraic variable v1 = if M.a1_x > 0, M.a1_x < 5: 0 elif M.a1_x > 6, M.a1_x < 9: 1 else 2 end;
 */
IntType v1_(void) {
    IntType if_dest1;
    if (((a1_x_) > (0)) && ((a1_x_) < (5))) {
        if_dest1 = 0;
    } else if (((a1_x_) > (6)) && ((a1_x_) < (9))) {
        if_dest1 = 1;
    } else {
        if_dest1 = 2;
    }
    return if_dest1;
}

/**
 * Algebraic variable if1 = if time > 1: 1 else 0 end;
 */
IntType if1_(void) {
    IntType if_dest2;
    if ((model_time) > (1)) {
        if_dest2 = 1;
    } else {
        if_dest2 = 0;
    }
    return if_dest2;
}

/**
 * Algebraic variable if2 = if time > 1: 1 elif time > 0.5: 2 else 0 end + 1;
 */
IntType if2_(void) {
    IntType if_dest3;
    if ((model_time) > (1)) {
        if_dest3 = 1;
    } else if ((model_time) > (0.5)) {
        if_dest3 = 2;
    } else {
        if_dest3 = 0;
    }
    return (if_dest3) + (1);
}

/**
 * Algebraic variable if3 = if time > 1: 1 elif time > 0.5: 2 elif time > 0.25: 3 else 0 end + 2;
 */
IntType if3_(void) {
    IntType if_dest4;
    if ((model_time) > (1)) {
        if_dest4 = 1;
    } else if ((model_time) > (0.5)) {
        if_dest4 = 2;
    } else if ((model_time) > (0.25)) {
        if_dest4 = 3;
    } else {
        if_dest4 = 0;
    }
    return (if_dest4) + (2);
}

/**
 * Algebraic variable fcall1 = inc(0);
 */
IntType fcall1_(void) {
    return inc_(0);
}

/**
 * Algebraic variable fcall2 = inc(inc(0));
 */
IntType fcall2_(void) {
    return inc_(inc_(0));
}

/**
 * Algebraic variable vea = A;
 */
exprsEnum vea_(void) {
    return _exprs_A;
}

/**
 * Algebraic variable x2 = x1;
 */
IntType x2_(void) {
    return x1_;
}

/**
 * Algebraic variable x3 = x2;
 */
IntType x3_(void) {
    return x2_();
}

/**
 * Algebraic variable x4 = M.a1_x;
 */
IntType x4_(void) {
    return a1_x_;
}

/**
 * Algebraic variable x6 = x5 + x5';
 */
RealType x6_(void) {
    return RealAdd(x5_, x5_deriv());
}

/**
 * Algebraic variable x7 = vea = B;
 */
BoolType x7_(void) {
    return (vea_()) == (_exprs_B);
}

/**
 * Algebraic variable x9 = x8 + 1;
 */
IntType x9_(void) {
    return IntegerAdd(x8_, 1);
}

RealType model_time; /**< Current model time. */

/** Initialize constants. */
static void InitConstants(void) {
    x1_ = 5;
}

/** Print function. */
#if PRINT_OUTPUT
static void PrintOutput(exprs_Event_ event, BoolType pre) {
}
#endif

/* Event execution code. */

/**
 * Execute code for event "tau".
 *
 * @return Whether the event was performed.
 */
static BoolType execEvent0(void) {
    #if EVENT_OUTPUT
        exprs_InfoEvent(EVT_TAU_, TRUE);
    #endif

    if (((a1_x_) != (1)) && ((a1_x_) != (2))) {
        a1_x_ = 3;
    } else if (((a1_x_) != (2)) && ((a1_x_) != (3))) {
        a1_x_ = 4;
    }

    #if EVENT_OUTPUT
        exprs_InfoEvent(EVT_TAU_, FALSE);
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
        break; /* No event fired, done with discrete steps. */
    }
}

/** First model call, initializing, and performing discrete events before the first time step. */
void exprs_EngineFirstStep(void) {
    InitConstants();

    model_time = 0.0;
    exprs_AssignInputVariables();
    x5_ = 0.0;
    a1_x_ = 0;
    AA_vb_ = TRUE;
    AA_vi_ = 5;
    AA_vp_ = 2;
    AA_vn_ = -(1);
    AA_vz_ = 1;
    AA_vr_ = 1.23;
    StringTypeCopyText(&(AA_vs_), "a");
    AA_ve_ = _exprs_A;
    (AA_va_).data[0] = 1;
    (AA_va_).data[1] = 2;
    AA_v2_ = 5.0;
    AA_i2r_ = (RealType)(AA_vi_);
    BoolToString(AA_vb_, &(AA_b2s_));
    IntToString(AA_vi_, &(AA_i2s_));
    RealToString(AA_vr_, &(AA_r2s_));
    AA_s2b_ = StringToBool(&(AA_b2s_));
    AA_s2i_ = StringToInt(&(AA_i2s_));
    AA_s2r_ = StringToReal(&(AA_r2s_));
    (AA_self_cast1_).data[0] = 1;
    (AA_self_cast1_).data[1] = 2;
    (AA_self_cast1_).data[2] = 3;
    AA_self_cast2_ = AA_self_cast1_;
    AA_inv1_ = !(AA_vb_);
    AA_inv2_ = AA_vb_;
    AA_neg1_ = IntegerNegate(AA_vi_);
    AA_neg2_ = IntegerNegate(IntegerNegate(AA_vi_));
    AA_neg3_ = -(AA_vp_);
    AA_neg4_ = -(-(AA_vp_));
    AA_pos1_ = AA_vi_;
    AA_pos2_ = AA_vi_;
    AA_posneg_ = IntegerNegate(IntegerNegate(IntegerNegate(IntegerNegate(AA_vi_))));
    (AA_l3i_).data[0] = TRUE;
    AA_idx1_ = 1;
    AA_vt_ = TRUE;
    AA_vf_ = FALSE;
    AA_short_and_ = (AA_vf_) && (A1BTypeProject(&(AA_l3i_), AA_idx1_));
    AA_short_or_ = (AA_vt_) || (A1BTypeProject(&(AA_l3i_), AA_idx1_));
    AA_impl_ = !(AA_vb_) || (AA_vb_);
    AA_biimpl_ = (AA_vb_) == (AA_vb_);
    AA_conj_ = (AA_vb_) && (AA_vb_);
    AA_disj_ = (AA_vb_) || (AA_vb_);
    AA_lt1_ = (AA_vi_) < (AA_vi_);
    AA_le1_ = (AA_vi_) <= (AA_vi_);
    AA_gt1_ = (AA_vi_) > (AA_vi_);
    AA_ge1_ = (AA_vi_) >= (AA_vi_);
    AA_lt2_ = (AA_vi_) < (AA_vr_);
    AA_le2_ = (AA_vi_) <= (AA_vr_);
    AA_gt2_ = (AA_vi_) > (AA_vr_);
    AA_ge2_ = (AA_vi_) >= (AA_vr_);
    AA_lt3_ = (AA_vr_) < (AA_vr_);
    AA_le3_ = (AA_vr_) <= (AA_vr_);
    AA_gt3_ = (AA_vr_) > (AA_vr_);
    AA_ge3_ = (AA_vr_) >= (AA_vr_);
    AA_lt4_ = (AA_vr_) < (AA_vr_);
    AA_le4_ = (AA_vr_) <= (AA_vr_);
    AA_gt4_ = (AA_vr_) > (AA_vr_);
    AA_ge4_ = (AA_vr_) >= (AA_vr_);
    AA_eq1_ = (AA_vb_) == (AA_vb_);
    AA_eq2_ = (AA_vi_) == (AA_vi_);
    AA_eq3_ = (AA_vr_) == (AA_vr_);
    AA_eq4_ = StringTypeEquals(&(AA_vs_), &(AA_vs_));
    AA_eq5_ = (AA_ve_) == (AA_ve_);
    AA_ne1_ = (AA_vb_) != (AA_vb_);
    AA_ne2_ = (AA_vi_) != (AA_vi_);
    AA_ne3_ = (AA_vr_) != (AA_vr_);
    AA_ne4_ = !StringTypeEquals(&(AA_vs_), &(AA_vs_));
    AA_ne5_ = (AA_ve_ != AA_ve_);
    AA_add1_ = IntegerAdd(AA_vi_, AA_vi_);
    AA_add2_ = RealAdd(AA_vi_, AA_vr_);
    AA_add3_ = RealAdd(AA_vr_, AA_vi_);
    AA_add4_ = RealAdd(AA_vr_, AA_vr_);
    StringTypeConcat(&(AA_add5_), &(AA_vs_), &(AA_vs_));
    AA_add6_ = (AA_vp_) + (AA_vp_);
    AA_add7_ = IntegerAdd(AA_vi_, AA_vp_);
    AA_add8_ = IntegerAdd(AA_vp_, AA_vi_);
    AA_sub1_ = IntegerSubtract(AA_vi_, AA_vi_);
    AA_sub2_ = RealSubtract(AA_vi_, AA_vr_);
    AA_sub3_ = RealSubtract(AA_vr_, AA_vi_);
    AA_sub4_ = RealSubtract(AA_vr_, AA_vr_);
    AA_sub5_ = (AA_vp_) - (AA_vp_);
    AA_sub6_ = IntegerSubtract(AA_vi_, AA_vp_);
    AA_sub7_ = IntegerSubtract(AA_vp_, AA_vi_);
    AA_mul1_ = IntegerMultiply(AA_vi_, AA_vi_);
    AA_mul2_ = RealMultiply(AA_vi_, AA_vr_);
    AA_mul3_ = RealMultiply(AA_vr_, AA_vi_);
    AA_mul4_ = RealMultiply(AA_vr_, AA_vr_);
    AA_mul5_ = (AA_vp_) * (AA_vp_);
    AA_mul6_ = IntegerMultiply(AA_vi_, AA_vp_);
    AA_mul7_ = IntegerMultiply(AA_vp_, AA_vi_);
    AA_rdiv1_ = RealDivision(AA_vi_, AA_vi_);
    AA_rdiv2_ = RealDivision(AA_vi_, AA_vr_);
    AA_rdiv3_ = RealDivision(AA_vr_, AA_vi_);
    AA_rdiv4_ = RealDivision(AA_vr_, AA_vr_);
    AA_rdiv5_ = (double)(AA_vi_) / (AA_vp_);
    AA_rdiv6_ = (double)(AA_vi_) / (AA_vn_);
    AA_div1_ = IntegerDiv(AA_vi_, AA_vi_);
    AA_div2_ = (AA_vi_) / (AA_vp_);
    AA_div3_ = IntegerDiv(AA_vi_, AA_vn_);
    AA_div4_ = IntegerDiv(AA_vi_, AA_vz_);
    AA_mod1_ = IntegerMod(AA_vi_, AA_vi_);
    AA_mod2_ = (AA_vi_) % (AA_vp_);
    (AA_li_).data[0] = 1;
    (AA_li_).data[1] = 2;
    (AA_tii_)._field0 = 1;
    (AA_tii_)._field1 = 2;
    StringTypeCopyText(&(AA_ss_), "abc");
    AA_proj1_ = A2ITypeProject(&(AA_li_), 0);
    AA_proj2_ = A2ITypeProject(&(AA_li_), -(1));
    AA_proj3_ = (AA_tii_)._field0;
    AA_proj4_ = (AA_tii_)._field1;
    StringTypeProject(&(AA_proj5_), &(AA_ss_), 0);
    StringTypeProject(&(AA_proj6_), &(AA_ss_), -(1));
    AA_f_acos_ = RealAcos(AA_vr_);
    AA_f_asin_ = RealAsin(AA_vr_);
    AA_f_atan_ = RealAtan(AA_vr_);
    AA_f_cos_ = RealCos(AA_vr_);
    AA_f_sin_ = RealSin(AA_vr_);
    AA_f_tan_ = RealTan(AA_vr_);
    AA_f_abs1_ = IntegerAbs(AA_vi_);
    AA_f_abs12_ = IntegerAbs(AA_vp_);
    AA_f_abs2_ = RealAbs(AA_vr_);
    AA_f_cbrt_ = RealCbrt(AA_vr_);
    AA_f_ceil_ = CeilFunction(AA_vr_);
    AA_f_empty_ = FALSE;
    AA_f_exp_ = RealExp(AA_vr_);
    AA_f_floor_ = FloorFunction(AA_vr_);
    AA_f_ln_ = RealLn(AA_vr_);
    AA_f_log_ = RealLog(AA_vr_);
    AA_f_max1_ = IntegerMax(AA_vi_, AA_vi_);
    AA_f_max2_ = RealMax(AA_vi_, AA_vr_);
    AA_f_max3_ = RealMax(AA_vr_, AA_vi_);
    AA_f_max4_ = RealMax(AA_vr_, AA_vr_);
    AA_f_min1_ = IntegerMin(AA_vi_, AA_vi_);
    AA_f_min2_ = RealMin(AA_vi_, AA_vr_);
    AA_f_min3_ = RealMin(AA_vr_, AA_vi_);
    AA_f_min4_ = RealMin(AA_vr_, AA_vr_);
    AA_f_pow1_ = RealMax(AA_vi_, AA_vi_);
    AA_f_pow12_ = IntegerPower(AA_vp_, AA_vp_);
    AA_f_pow2_ = RealMax(AA_vi_, AA_vr_);
    AA_f_pow3_ = RealMax(AA_vr_, AA_vi_);
    AA_f_pow4_ = RealMax(AA_vr_, AA_vr_);
    AA_f_round_ = RoundFunction(AA_vr_);
    AA_f_scale_ = ScaleFunction(AA_vr_, 0, 10, 1, 11);
    AA_f_sign1_ = IntegerSign(AA_vi_);
    AA_f_sign2_ = IntegerSign(AA_vr_);
    AA_f_size1_ = 2;
    AA_f_size2_ = StringTypeSize(&(AA_vs_));
    AA_f_sqrt_ = RealSqrt(AA_vr_);

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
void exprs_EngineTimeStep(double delta) {
    exprs_AssignInputVariables();

    /* Update continuous variables. */
    if (delta > 0.0) {
        RealType deriv0 = x5_deriv();

        errno = 0;
        x5_ = UpdateContValue(x5_ + delta * deriv0, "x5", errno == 0);
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

