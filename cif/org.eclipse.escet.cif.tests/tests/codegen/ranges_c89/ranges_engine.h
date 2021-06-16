/* Headers for the CIF to C translation of ranges.cif
 * Generated file, DO NOT EDIT
 */

#ifndef CIF_C_RANGES_ENGINE_H
#define CIF_C_RANGES_ENGINE_H

#include "ranges_library.h"

/* Types of the specification.
 * Note that integer ranges are ignored in C.
 */
/* CIF type: tuple(int[0..7] a; int[0..7] b) */
struct T2II_struct {
    IntType _field0;
    IntType _field1;
};
typedef struct T2II_struct T2IIType;

extern BoolType T2IITypeEquals(T2IIType *left, T2IIType *right);
extern int T2IITypePrint(T2IIType *tuple, char *dest, int start, int end);

/* CIF type: list[3] tuple(int[0..7] a; int[0..7] b) */
struct A3T2II_struct {
    T2IIType data[3];
};
typedef struct A3T2II_struct A3T2IIType;

extern BoolType A3T2IITypeEquals(A3T2IIType *left, A3T2IIType *right);
extern T2IIType *A3T2IITypeProject(A3T2IIType *array, IntType index);
extern void A3T2IITypeModify(A3T2IIType *array, IntType index, T2IIType *value);
extern int A3T2IITypePrint(A3T2IIType *array, char *dest, int start, int end);


/* Event declarations. */
enum rangesEventEnum_ {
    EVT_INITIAL_, /**< Initial step. */
    EVT_DELAY_,   /**< Delay step. */
    EVT_TAU_,     /**< Tau step. */
    e11_,         /**< Event e11. */
    e12_,         /**< Event e12. */
    e13_,         /**< Event e13. */
    e14_,         /**< Event e14. */
    e15_,         /**< Event e15. */
    e16_,         /**< Event e16. */
    e17_,         /**< Event e17. */
    e18_,         /**< Event e18. */
    e21_,         /**< Event e21. */
    e22_,         /**< Event e22. */
    e23_,         /**< Event e23. */
    e24_,         /**< Event e24. */
    e25_,         /**< Event e25. */
    e26_,         /**< Event e26. */
    e27_,         /**< Event e27. */
    e28_,         /**< Event e28. */
    e31_,         /**< Event e31. */
    e32_,         /**< Event e32. */
    e33_,         /**< Event e33. */
    e34_,         /**< Event e34. */
    e35_,         /**< Event e35. */
    e36_,         /**< Event e36. */
    e37_,         /**< Event e37. */
    e38_,         /**< Event e38. */
    e41_,         /**< Event e41. */
};
typedef enum rangesEventEnum_ ranges_Event_;

/** Names of all the events. */
extern const char *ranges_event_names[];

/* Constants. */


/* Input variables. */




/* Declaration of internal functions. */


/* State variables (use for output only). */
extern RealType model_time; /**< Current model time. */
extern A3T2IIType aut1_v1_; /**< Discrete variable "list[3] tuple(int[0..7] a; int[0..7] b) aut1.v1". */
extern A3T2IIType aut1_v2_; /**< Discrete variable "list[3] tuple(int[0..7] a; int[0..7] b) aut1.v2". */
extern A3T2IIType aut1_w1_; /**< Discrete variable "list[3] tuple(int[1..8] a; int[1..8] b) aut1.w1". */
extern IntType aut1_x1_;    /**< Discrete variable "int[0..4] aut1.x1". */
extern IntType aut1_x2_;    /**< Discrete variable "int[0..9] aut1.x2". */
extern IntType aut1_x3_;    /**< Discrete variable "int[-1..4] aut1.x3". */
extern IntType aut1_x4_;    /**< Discrete variable "int[-1..9] aut1.x4". */

/* Algebraic and derivative functions (use for output only). */



/* Code entry points. */
void ranges_EngineFirstStep(void);
void ranges_EngineTimeStep(double delta);

#if EVENT_OUTPUT
/**
 * External callback function reporting about the execution of an event.
 * @param event Event being executed.
 * @param pre If \c TRUE, event is about to be executed. If \c FALSE, event has been executed.
 * @note Function must be implemented externally.
 */
extern void ranges_InfoEvent(ranges_Event_ event, BoolType pre);
#endif

#if PRINT_OUTPUT
/**
 * External callback function to output the given text-line to the given filename.
 * @param text Text to print (does not have a EOL character).
 * @param fname Name of the file to print to.
 */
extern void ranges_PrintOutput(const char *text, const char *fname);
#endif

#endif

