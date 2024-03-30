/* Headers for the CIF to C translation of unsupported_simulink_warnings.cif
 * Generated file, DO NOT EDIT
 */

#ifndef CIF_C_UNSUPPORTED_SIMULINK_WARNINGS_ENGINE_H
#define CIF_C_UNSUPPORTED_SIMULINK_WARNINGS_ENGINE_H

#include "unsupported_simulink_warnings_library.h"

/* Types of the specification.
 * Note that integer ranges are ignored in C.
 */
/* CIF type: tuple(int x; int y) */
struct T2II_struct {
    IntType _field0;
    IntType _field1;
};
typedef struct T2II_struct T2IIType;

extern BoolType T2IITypeEquals(T2IIType *left, T2IIType *right);
extern int T2IITypePrint(T2IIType *tuple, char *dest, int start, int end);

/* CIF type: list[1] tuple(int x; int y) */
struct A1T2II_struct {
    T2IIType data[1];
};
typedef struct A1T2II_struct A1T2IIType;

extern BoolType A1T2IITypeEquals(A1T2IIType *left, A1T2IIType *right);
extern T2IIType *A1T2IITypeProject(A1T2IIType *array, IntType index);
extern void A1T2IITypeModify(A1T2IIType *array, IntType index, T2IIType *value);
extern int A1T2IITypePrint(A1T2IIType *array, char *dest, int start, int end);

/* CIF type: list[1] string */
struct A1S_struct {
    StringType data[1];
};
typedef struct A1S_struct A1SType;

extern BoolType A1STypeEquals(A1SType *left, A1SType *right);
extern StringType *A1STypeProject(A1SType *array, IntType index);
extern void A1STypeModify(A1SType *array, IntType index, StringType *value);
extern int A1STypePrint(A1SType *array, char *dest, int start, int end);

/* CIF type: list[1] list[1] tuple(int x; int y) */
struct A1A1T2II_struct {
    A1T2IIType data[1];
};
typedef struct A1A1T2II_struct A1A1T2IIType;

extern BoolType A1A1T2IITypeEquals(A1A1T2IIType *left, A1A1T2IIType *right);
extern A1T2IIType *A1A1T2IITypeProject(A1A1T2IIType *array, IntType index);
extern void A1A1T2IITypeModify(A1A1T2IIType *array, IntType index, A1T2IIType *value);
extern int A1A1T2IITypePrint(A1A1T2IIType *array, char *dest, int start, int end);

/* CIF type: list[1] list[1] string */
struct A1A1S_struct {
    A1SType data[1];
};
typedef struct A1A1S_struct A1A1SType;

extern BoolType A1A1STypeEquals(A1A1SType *left, A1A1SType *right);
extern A1SType *A1A1STypeProject(A1A1SType *array, IntType index);
extern void A1A1STypeModify(A1A1SType *array, IntType index, A1SType *value);
extern int A1A1STypePrint(A1A1SType *array, char *dest, int start, int end);

/* CIF type: list[1] bool */
struct A1B_struct {
    BoolType data[1];
};
typedef struct A1B_struct A1BType;

extern BoolType A1BTypeEquals(A1BType *left, A1BType *right);
extern BoolType A1BTypeProject(A1BType *array, IntType index);
extern void A1BTypeModify(A1BType *array, IntType index, BoolType value);
extern int A1BTypePrint(A1BType *array, char *dest, int start, int end);

/* CIF type: list[1] list[1] bool */
struct A1A1B_struct {
    A1BType data[1];
};
typedef struct A1A1B_struct A1A1BType;

extern BoolType A1A1BTypeEquals(A1A1BType *left, A1A1BType *right);
extern A1BType *A1A1BTypeProject(A1A1BType *array, IntType index);
extern void A1A1BTypeModify(A1A1BType *array, IntType index, A1BType *value);
extern int A1A1BTypePrint(A1A1BType *array, char *dest, int start, int end);

/* CIF type: list[1] list[1] list[1] bool */
struct A1A1A1B_struct {
    A1A1BType data[1];
};
typedef struct A1A1A1B_struct A1A1A1BType;

extern BoolType A1A1A1BTypeEquals(A1A1A1BType *left, A1A1A1BType *right);
extern A1A1BType *A1A1A1BTypeProject(A1A1A1BType *array, IntType index);
extern void A1A1A1BTypeModify(A1A1A1BType *array, IntType index, A1A1BType *value);
extern int A1A1A1BTypePrint(A1A1A1BType *array, char *dest, int start, int end);

enum Enumunsupported_simulink_warnings_ {
    _unsupported_simulink_warnings___some_dummy_enum_literal,
};
typedef enum Enumunsupported_simulink_warnings_ unsupported_simulink_warningsEnum;

extern const char *enum_names[];
extern int EnumTypePrint(unsupported_simulink_warningsEnum value, char *dest, int start, int end);


/* Event declarations. */
enum unsupported_simulink_warningsEventEnum_ {
    /** Initial step. */
    EVT_INITIAL_,

    /** Delay step. */
    EVT_DELAY_,

    /** Tau step. */
    EVT_TAU_,

    /** Event "a.e". */
    a_e_,
};
typedef enum unsupported_simulink_warningsEventEnum_ unsupported_simulink_warnings_Event_;

/** Names of all the events. */
extern const char *unsupported_simulink_warnings_event_names[];

/* Constants. */


/* Input variables. */




/* Declaration of internal functions. */


/* State variables (use for output only). */
extern RealType model_time; /**< Current model time. */

/** Discrete variable "tuple(int x; int y) a.d1". */
extern T2IIType a_d1_;

/** Discrete variable "string a.d2". */
extern StringType a_d2_;

/** Discrete variable "list[1] tuple(int x; int y) a.d3". */
extern A1T2IIType a_d3_;

/** Discrete variable "list[1] string a.d4". */
extern A1SType a_d4_;

/** Discrete variable "list[1] list[1] tuple(int x; int y) a.d5". */
extern A1A1T2IIType a_d5_;

/** Discrete variable "list[1] list[1] string a.d6". */
extern A1A1SType a_d6_;

/** Discrete variable "list[1] list[1] list[1] bool a.d7". */
extern A1A1A1BType a_d7_;

/* Algebraic and derivative functions (use for output only). */

static inline T2IIType a1_(void);
static inline StringType a2_(void);
static inline A1T2IIType a3_(void);
static inline A1SType a4_(void);
static inline A1A1T2IIType a5_(void);
static inline A1A1SType a6_(void);
static inline A1A1A1BType a7_(void);



/** Algebraic variable a1 = (1, 2). */
static inline T2IIType a1_(void) {
    T2IIType tuple_tmp1;
    (tuple_tmp1)._field0 = 1;
    (tuple_tmp1)._field1 = 2;
    return tuple_tmp1;
}

/** Algebraic variable a2 = "a". */
static inline StringType a2_(void) {
    StringType str_tmp2;
    StringTypeCopyText(&str_tmp2, "a");
    return str_tmp2;
}

/** Algebraic variable a3 = [(1, 2)]. */
static inline A1T2IIType a3_(void) {
    A1T2IIType array_tmp3;
    ((array_tmp3).data[0])._field0 = 1;
    ((array_tmp3).data[0])._field1 = 2;
    return array_tmp3;
}

/** Algebraic variable a4 = ["a"]. */
static inline A1SType a4_(void) {
    A1SType array_tmp4;
    StringTypeCopyText(&((array_tmp4).data[0]), "a");
    return array_tmp4;
}

/** Algebraic variable a5 = [[(1, 2)]]. */
static inline A1A1T2IIType a5_(void) {
    A1A1T2IIType array_tmp5;
    (((array_tmp5).data[0]).data[0])._field0 = 1;
    (((array_tmp5).data[0]).data[0])._field1 = 2;
    return array_tmp5;
}

/** Algebraic variable a6 = [["a"]]. */
static inline A1A1SType a6_(void) {
    A1A1SType array_tmp6;
    StringTypeCopyText(&(((array_tmp6).data[0]).data[0]), "a");
    return array_tmp6;
}

/** Algebraic variable a7 = [[[true]]]. */
static inline A1A1A1BType a7_(void) {
    A1A1A1BType array_tmp7;
    (((array_tmp7).data[0]).data[0]).data[0] = TRUE;
    return array_tmp7;
}

/* Code entry points. */
void unsupported_simulink_warnings_EngineFirstStep(void);
void unsupported_simulink_warnings_EngineTimeStep(double delta);

#if EVENT_OUTPUT
/**
 * External callback function reporting about the execution of an event.
 * @param event Event being executed.
 * @param pre If \c TRUE, event is about to be executed. If \c FALSE, event has been executed.
 * @note Function must be implemented externally.
 */
extern void unsupported_simulink_warnings_InfoEvent(unsupported_simulink_warnings_Event_ event, BoolType pre);
#endif

#if PRINT_OUTPUT
/**
 * External callback function to output the given text-line to the given filename.
 * @param text Text to print (does not have a EOL character).
 * @param fname Name of the file to print to.
 */
extern void unsupported_simulink_warnings_PrintOutput(const char *text, const char *fname);
#endif

#endif

