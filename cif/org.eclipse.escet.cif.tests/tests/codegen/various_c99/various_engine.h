/* Headers for the CIF to C translation of various.cif
 * Generated file, DO NOT EDIT
 */

#ifndef CIF_C_VARIOUS_ENGINE_H
#define CIF_C_VARIOUS_ENGINE_H

#include "various_library.h"

/* Types of the specification.
 * Note that integer ranges are ignored in C.
 */
/* CIF type: list[2] int[0..3] */
struct A2I_struct {
    IntType data[2];
};
typedef struct A2I_struct A2IType;

extern BoolType A2ITypeEquals(A2IType *left, A2IType *right);
extern IntType A2ITypeProject(A2IType *array, IntType index);
extern void A2ITypeModify(A2IType *array, IntType index, IntType value);
extern int A2ITypePrint(A2IType *array, char *dest, int start, int end);

enum Enumvarious_ {
    _various_l1,
    _various_l2,
};
typedef enum Enumvarious_ variousEnum;

extern const char *enum_names[];
extern int EnumTypePrint(variousEnum value, char *dest, int start, int end);

/* CIF type: list[3] int[0..5] */
struct A3I_struct {
    IntType data[3];
};
typedef struct A3I_struct A3IType;

extern BoolType A3ITypeEquals(A3IType *left, A3IType *right);
extern IntType A3ITypeProject(A3IType *array, IntType index);
extern void A3ITypeModify(A3IType *array, IntType index, IntType value);
extern int A3ITypePrint(A3IType *array, char *dest, int start, int end);


/* Event declarations. */
enum variousEventEnum_ {
    EVT_INITIAL_, /**< Initial step. */
    EVT_DELAY_,   /**< Delay step. */
    EVT_TAU_,     /**< Tau step. */
    e1_,          /**< Event e1. */
    g_h1_,        /**< Event g.h1. */
};
typedef enum variousEventEnum_ various_Event_;

/** Names of all the events. */
extern const char *various_event_names[];

/* Constants. */


/* Input variables. */

/** Input variable "int x". */
extern IntType x_;

/** Input variable "int y". */
extern IntType y_;

/** Input variable "list[3] int[0..5] input_li". */
extern A3IType input_li_;

extern void various_AssignInputVariables();

/* Declaration of internal functions. */
extern IntType inc_(IntType inc_x_);

/* State variables (use for output only). */
extern RealType model_time; /**< Current model time. */
extern A2IType a_li_;       /**< Discrete variable "list[2] int[0..3] a.li". */
extern IntType a_x_;        /**< Discrete variable "int[2..5] a.x". */
extern IntType g_rcv_v_;    /**< Discrete variable "int g.rcv.v". */
extern IntType g_rcv_v2_;   /**< Discrete variable "int g.rcv.v2". */
extern IntType g_snd_a_;    /**< Discrete variable "int g.snd.a". */
extern RealType g_sync_c_;  /**< Continuous variable "real g.sync.c". */
extern variousEnum g_sync_; /**< Discrete variable "E g.sync". */

/* Algebraic and derivative functions (use for output only). */
static inline RealType g_sync_c_deriv(void);
static inline IntType z_(void);


/** Derivative of "g.sync.c". */
static inline RealType g_sync_c_deriv(void) {
    return 1.0;
}
/**
 * Algebraic variable z = x + y;
 */
static inline IntType z_(void) {
    return IntegerAdd(x_, y_);
}

/* Code entry points. */
void various_EngineFirstStep(void);
void various_EngineTimeStep(double delta);

#if EVENT_OUTPUT
/**
 * External callback function reporting about the execution of an event.
 * @param event Event being executed.
 * @param pre If \c TRUE, event is about to be executed. If \c FALSE, event has been executed.
 * @note Function must be implemented externally.
 */
extern void various_InfoEvent(various_Event_ event, BoolType pre);
#endif

#if PRINT_OUTPUT
/**
 * External callback function to output the given text-line to the given filename.
 * @param text Text to print (does not have a EOL character).
 * @param fname Name of the file to print to.
 */
extern void various_PrintOutput(const char *text, const char *fname);
#endif

#endif

