/* Headers for the CIF to C translation of fmt.cif
 * Generated file, DO NOT EDIT
 */

#ifndef CIF_C_FMT_ENGINE_H
#define CIF_C_FMT_ENGINE_H

#include "fmt_library.h"

/* Types of the specification.
 * Note that integer ranges are ignored in C.
 */
/* CIF type: list[1] string */
struct A1S_struct {
    StringType data[1];
};
typedef struct A1S_struct A1SType;

extern BoolType A1STypeEquals(A1SType *left, A1SType *right);
extern StringType *A1STypeProject(A1SType *array, IntType index);
extern void A1STypeModify(A1SType *array, IntType index, StringType *value);
extern int A1STypePrint(A1SType *array, char *dest, int start, int end);

/* CIF type: list[1] int */
struct A1I_struct {
    IntType data[1];
};
typedef struct A1I_struct A1IType;

extern BoolType A1ITypeEquals(A1IType *left, A1IType *right);
extern IntType A1ITypeProject(A1IType *array, IntType index);
extern void A1ITypeModify(A1IType *array, IntType index, IntType value);
extern int A1ITypePrint(A1IType *array, char *dest, int start, int end);

/* CIF type: tuple(int a; list[1] int b) */
struct T2IA1I_struct {
    IntType _field0;
    A1IType _field1;
};
typedef struct T2IA1I_struct T2IA1IType;

extern BoolType T2IA1ITypeEquals(T2IA1IType *left, T2IA1IType *right);
extern int T2IA1ITypePrint(T2IA1IType *tuple, char *dest, int start, int end);

/* CIF type: list[2] string */
struct A2S_struct {
    StringType data[2];
};
typedef struct A2S_struct A2SType;

extern BoolType A2STypeEquals(A2SType *left, A2SType *right);
extern StringType *A2STypeProject(A2SType *array, IntType index);
extern void A2STypeModify(A2SType *array, IntType index, StringType *value);
extern int A2STypePrint(A2SType *array, char *dest, int start, int end);

enum Enumfmt_ {
    _fmt_A,
    _fmt_B,
};
typedef enum Enumfmt_ fmtEnum;

extern const char *enum_names[];
extern int EnumTypePrint(fmtEnum value, char *dest, int start, int end);


/* Event declarations. */
enum fmtEventEnum_ {
    EVT_INITIAL_, /**< Initial step. */
    EVT_DELAY_,   /**< Delay step. */
    EVT_TAU_,     /**< Tau step. */
};
typedef enum fmtEventEnum_ fmt_Event_;

/** Names of all the events. */
extern const char *fmt_event_names[];

/* Constants. */


/* Input variables. */
extern BoolType b_; /**< Input variable "bool b". */
extern IntType i_;  /**< Input variable "int i". */
extern RealType r_; /**< Input variable "real r". */

extern void fmt_AssignInputVariables();

/* Declaration of internal functions. */


/* State variables (use for output only). */
extern RealType model_time; /**< Current model time. */


/* Algebraic and derivative functions (use for output only). */

static inline StringType s_(void);
static inline A1SType ls_(void);
static inline StringType s1_(void);
static inline StringType s2_(void);
static inline StringType s3_(void);
static inline IntType neg12345_(void);
static inline RealType r456_(void);
static inline RealType r_zero_(void);
static inline StringType s0_(void);
static inline IntType i0_(void);
static inline BoolType b0_(void);
static inline T2IA1IType t0_(void);
static inline RealType r0_(void);
static inline StringType s00_(void);
static inline A2SType l0_(void);
static inline fmtEnum e0_(void);
static inline IntType ii1_(void);
static inline IntType ii2_(void);
static inline IntType ii3_(void);
static inline IntType ii4_(void);
static inline IntType ii5_(void);
static inline RealType rr6_(void);
static inline IntType ii7_(void);



/**
 * Algebraic variable s = "a\nb\tc\\d\"e";
 */
static inline StringType s_(void) {
    StringType str_tmp1;
    StringTypeCopyText(&str_tmp1, "a\nb\tc\\d\"e");
    return str_tmp1;
}

/**
 * Algebraic variable ls = [s];
 */
static inline A1SType ls_(void) {
    A1SType array_tmp2;
    (array_tmp2).data[0] = s_();
    return array_tmp2;
}

/**
 * Algebraic variable s1 = "a";
 */
static inline StringType s1_(void) {
    StringType str_tmp3;
    StringTypeCopyText(&str_tmp3, "a");
    return str_tmp3;
}

/**
 * Algebraic variable s2 = "a\nb\tc\\d\"e f%g%%h%si%fj";
 */
static inline StringType s2_(void) {
    StringType str_tmp4;
    StringTypeCopyText(&str_tmp4, "a\nb\tc\\d\"e f%g%%h%si%fj");
    return str_tmp4;
}

/**
 * Algebraic variable s3 = "a\nb\tc\\d\"e f%g%%h%si%fj";
 */
static inline StringType s3_(void) {
    StringType str_tmp5;
    StringTypeCopyText(&str_tmp5, "a\nb\tc\\d\"e f%g%%h%si%fj");
    return str_tmp5;
}

/**
 * Algebraic variable neg12345 = -12345;
 */
static inline IntType neg12345_(void) {
    return -(12345);
}

/**
 * Algebraic variable r456 = 4.56;
 */
static inline RealType r456_(void) {
    return 4.56;
}

/**
 * Algebraic variable r_zero = 0.0;
 */
static inline RealType r_zero_(void) {
    return 0.0;
}

/**
 * Algebraic variable s0 = "a";
 */
static inline StringType s0_(void) {
    StringType str_tmp6;
    StringTypeCopyText(&str_tmp6, "a");
    return str_tmp6;
}

/**
 * Algebraic variable i0 = 1;
 */
static inline IntType i0_(void) {
    return 1;
}

/**
 * Algebraic variable b0 = true;
 */
static inline BoolType b0_(void) {
    return TRUE;
}

/**
 * Algebraic variable t0 = (1, [2]);
 */
static inline T2IA1IType t0_(void) {
    T2IA1IType tuple_tmp7;
    (tuple_tmp7)._field0 = 1;
    ((tuple_tmp7)._field1).data[0] = 2;
    return tuple_tmp7;
}

/**
 * Algebraic variable r0 = 1.23456e7;
 */
static inline RealType r0_(void) {
    return 1.23456E7;
}

/**
 * Algebraic variable s00 = "1.23456e7";
 */
static inline StringType s00_(void) {
    StringType str_tmp8;
    StringTypeCopyText(&str_tmp8, "1.23456e7");
    return str_tmp8;
}

/**
 * Algebraic variable l0 = ["a", "b"];
 */
static inline A2SType l0_(void) {
    A2SType array_tmp9;
    StringTypeCopyText(&((array_tmp9).data[0]), "a");
    StringTypeCopyText(&((array_tmp9).data[1]), "b");
    return array_tmp9;
}

/**
 * Algebraic variable e0 = A;
 */
static inline fmtEnum e0_(void) {
    return _fmt_A;
}

/**
 * Algebraic variable ii1 = 1;
 */
static inline IntType ii1_(void) {
    return 1;
}

/**
 * Algebraic variable ii2 = 2;
 */
static inline IntType ii2_(void) {
    return 2;
}

/**
 * Algebraic variable ii3 = 3;
 */
static inline IntType ii3_(void) {
    return 3;
}

/**
 * Algebraic variable ii4 = 4;
 */
static inline IntType ii4_(void) {
    return 4;
}

/**
 * Algebraic variable ii5 = 5;
 */
static inline IntType ii5_(void) {
    return 5;
}

/**
 * Algebraic variable rr6 = 6.0;
 */
static inline RealType rr6_(void) {
    return 6.0;
}

/**
 * Algebraic variable ii7 = 7;
 */
static inline IntType ii7_(void) {
    return 7;
}

/* Code entry points. */
void fmt_EngineFirstStep(void);
void fmt_EngineTimeStep(double delta);

#if EVENT_OUTPUT
/**
 * External callback function reporting about the execution of an event.
 * @param event Event being executed.
 * @param pre If \c TRUE, event is about to be executed. If \c FALSE, event has been executed.
 * @note Function must be implemented externally.
 */
extern void fmt_InfoEvent(fmt_Event_ event, BoolType pre);
#endif

#if PRINT_OUTPUT
/**
 * External callback function to output the given text-line to the given filename.
 * @param text Text to print (does not have a EOL character).
 * @param fname Name of the file to print to.
 */
extern void fmt_PrintOutput(const char *text, const char *fname);
#endif

#endif

