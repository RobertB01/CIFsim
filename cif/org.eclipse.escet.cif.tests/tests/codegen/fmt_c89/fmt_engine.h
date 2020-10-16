/* Headers for the CIF to C translation of fmt.cif
 * Generated file, DO NOT EDIT
 */

#ifndef CIF_C_FMT_ENGINE_H
#define CIF_C_FMT_ENGINE_H

#include "fmt_library.h"

/* Types of the specification.
 * Note that integer ranges are ignored in C.
 */
enum Enumfmt_ {
    _fmt_A,
    _fmt_B,
    _fmt_X,
};
typedef enum Enumfmt_ fmtEnum;

extern const char *enum_names[];
extern int EnumTypePrint(fmtEnum value, char *dest, int start, int end);

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
extern fmtEnum a_; /**< Discrete variable "E a". */

/* Algebraic and derivative functions (use for output only). */

StringType s_(void);
A1SType ls_(void);
StringType s1_(void);
StringType s2_(void);
StringType s3_(void);
IntType neg12345_(void);
RealType r456_(void);
RealType r_zero_(void);
StringType s0_(void);
IntType i0_(void);
BoolType b0_(void);
T2IA1IType t0_(void);
RealType r0_(void);
StringType s00_(void);
A2SType l0_(void);
fmtEnum e0_(void);
IntType ii1_(void);
IntType ii2_(void);
IntType ii3_(void);
IntType ii4_(void);
IntType ii5_(void);
RealType rr6_(void);
IntType ii7_(void);


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

