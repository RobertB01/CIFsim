/* Headers for the CIF to C translation of declarations.cif
 * Generated file, DO NOT EDIT
 */

#ifndef CIF_C_DECLARATIONS_ENGINE_H
#define CIF_C_DECLARATIONS_ENGINE_H

#include "declarations_library.h"

/* Types of the specification.
 * Note that integer ranges are ignored in C.
 */
enum Enumdeclarations_ {
    _declarations_loc1,
    _declarations_loc2,
};
typedef enum Enumdeclarations_ declarationsEnum;

extern const char *enum_names[];
extern int EnumTypePrint(declarationsEnum value, char *dest, int start, int end);

/* CIF type: tuple(int a; int b; real c) */
struct T3IIR_struct {
    IntType _field0;
    IntType _field1;
    RealType _field2;
};
typedef struct T3IIR_struct T3IIRType;

extern BoolType T3IIRTypeEquals(T3IIRType *left, T3IIRType *right);
extern int T3IIRTypePrint(T3IIRType *tuple, char *dest, int start, int end);


/* Event declarations. */
enum declarationsEventEnum_ {
    EVT_INITIAL_, /**< Initial step. */
    EVT_DELAY_,   /**< Delay step. */
    EVT_TAU_,     /**< Tau step. */
    e_e1_,        /**< Event e_e1. */
    e_e2_,        /**< Event e_e2. */
    c_e1_,        /**< Event c_e1. */
    c_e2_,        /**< Event c_e2. */
    u_e1_,        /**< Event u_e1. */
    u_e2_,        /**< Event u_e2. */
};
typedef enum declarationsEventEnum_ declarations_Event_;

/** Names of all the events. */
extern const char *declarations_event_names[];

/* Constants. */
extern RealType c1_; /**< Constant "c1". */
extern RealType c4_; /**< Constant "c4". */
extern RealType c5_; /**< Constant "c5". */
extern RealType c3_; /**< Constant "c3". */
extern RealType c2_; /**< Constant "c2". */

/* Input variables. */

/** Input variable "int i1". */
extern IntType i1_;

/** Input variable "real i2". */
extern RealType i2_;

/** Input variable "tuple(int a; int b; real c) i3". */
extern T3IIRType i3_;

extern void declarations_AssignInputVariables();

/* Declaration of internal functions. */
extern IntType inc_(IntType inc_x_);
extern RealType f1_(IntType f1_x_);

/* State variables (use for output only). */
extern RealType model_time; /**< Current model time. */
extern RealType aut1_v1_;       /**< Discrete variable "real aut1.v1". */
extern RealType aut1_v4_;       /**< Discrete variable "real aut1.v4". */
extern RealType aut1_v5_;       /**< Discrete variable "real aut1.v5". */
extern RealType aut2_v2_;       /**< Continuous variable "real aut2.v2". */
extern declarationsEnum g1_a1_; /**< Discrete variable "E g1_a1". */
extern RealType aut1_v3_;       /**< Continuous variable "real aut1.v3". */
extern RealType aut2_v1_;       /**< Discrete variable "real aut2.v1". */
extern RealType aut1_v2_;       /**< Discrete variable "real aut1.v2". */
extern RealType aut1_v7_;       /**< Discrete variable "real aut1.v7". */
extern RealType aut1_v8_;       /**< Discrete variable "real aut1.v8". */
extern RealType aut1_v6_;       /**< Discrete variable "real aut1.v6". */

/* Algebraic and derivative functions (use for output only). */
RealType aut1_v3_deriv(void);
RealType aut2_v2_deriv(void);
RealType a1_(void);
IntType a2_(void);
RealType a3_(void);
RealType a4_(void);


/* Code entry points. */
void declarations_EngineFirstStep(void);
void declarations_EngineTimeStep(double delta);

#if EVENT_OUTPUT
/**
 * External callback function reporting about the execution of an event.
 * @param event Event being executed.
 * @param pre If \c TRUE, event is about to be executed. If \c FALSE, event has been executed.
 * @note Function must be implemented externally.
 */
extern void declarations_InfoEvent(declarations_Event_ event, BoolType pre);
#endif

#if PRINT_OUTPUT
/**
 * External callback function to output the given text-line to the given filename.
 * @param text Text to print (does not have a EOL character).
 * @param fname Name of the file to print to.
 */
extern void declarations_PrintOutput(const char *text, const char *fname);
#endif

#endif

