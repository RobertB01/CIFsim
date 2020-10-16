/* Headers for the CIF to C translation of prints.cif
 * Generated file, DO NOT EDIT
 */

#ifndef CIF_C_PRINTS_ENGINE_H
#define CIF_C_PRINTS_ENGINE_H

#include "prints_library.h"

/* Types of the specification.
 * Note that integer ranges are ignored in C.
 */
enum Enumprints_ {
    _prints_A,
    _prints_B,
    _prints_X,
};
typedef enum Enumprints_ printsEnum;

extern const char *enum_names[];
extern int EnumTypePrint(printsEnum value, char *dest, int start, int end);

/* CIF type: list[3] int[1..3] */
struct A3I_struct {
    IntType data[3];
};
typedef struct A3I_struct A3IType;

extern BoolType A3ITypeEquals(A3IType *left, A3IType *right);
extern IntType A3ITypeProject(A3IType *array, IntType index);
extern void A3ITypeModify(A3IType *array, IntType index, IntType value);
extern int A3ITypePrint(A3IType *array, char *dest, int start, int end);

/* CIF type: list[2] string */
struct A2S_struct {
    StringType data[2];
};
typedef struct A2S_struct A2SType;

extern BoolType A2STypeEquals(A2SType *left, A2SType *right);
extern StringType *A2STypeProject(A2SType *array, IntType index);
extern void A2STypeModify(A2SType *array, IntType index, StringType *value);
extern int A2STypePrint(A2SType *array, char *dest, int start, int end);

/* CIF type: tuple(int[1..1]; bool; string) */
struct T3IBS_struct {
    IntType _field0;
    BoolType _field1;
    StringType _field2;
};
typedef struct T3IBS_struct T3IBSType;

extern BoolType T3IBSTypeEquals(T3IBSType *left, T3IBSType *right);
extern int T3IBSTypePrint(T3IBSType *tuple, char *dest, int start, int end);


/* Event declarations. */
enum printsEventEnum_ {
    EVT_INITIAL_, /**< Initial step. */
    EVT_DELAY_,   /**< Delay step. */
    EVT_TAU_,     /**< Tau step. */
    e1_,          /**< Event e1. */
    e2_,          /**< Event e2. */
};
typedef enum printsEventEnum_ prints_Event_;

/** Names of all the events. */
extern const char *prints_event_names[];

/* Constants. */


/* Input variables. */




/* Declaration of internal functions. */


/* State variables (use for output only). */
extern RealType model_time; /**< Current model time. */
extern printsEnum a1_; /**< Discrete variable "E a1". */

/* Algebraic and derivative functions (use for output only). */



/* Code entry points. */
void prints_EngineFirstStep(void);
void prints_EngineTimeStep(double delta);

#if EVENT_OUTPUT
/**
 * External callback function reporting about the execution of an event.
 * @param event Event being executed.
 * @param pre If \c TRUE, event is about to be executed. If \c FALSE, event has been executed.
 * @note Function must be implemented externally.
 */
extern void prints_InfoEvent(prints_Event_ event, BoolType pre);
#endif

#if PRINT_OUTPUT
/**
 * External callback function to output the given text-line to the given filename.
 * @param text Text to print (does not have a EOL character).
 * @param fname Name of the file to print to.
 */
extern void prints_PrintOutput(const char *text, const char *fname);
#endif

#endif

