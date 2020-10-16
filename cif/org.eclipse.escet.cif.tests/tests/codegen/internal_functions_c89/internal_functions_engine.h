/* Headers for the CIF to C translation of internal_functions.cif
 * Generated file, DO NOT EDIT
 */

#ifndef CIF_C_INTERNAL_FUNCTIONS_ENGINE_H
#define CIF_C_INTERNAL_FUNCTIONS_ENGINE_H

#include "internal_functions_library.h"

/* Types of the specification.
 * Note that integer ranges are ignored in C.
 */
/* CIF type: list[4] int */
struct A4I_struct {
    IntType data[4];
};
typedef struct A4I_struct A4IType;

extern BoolType A4ITypeEquals(A4IType *left, A4IType *right);
extern IntType A4ITypeProject(A4IType *array, IntType index);
extern void A4ITypeModify(A4IType *array, IntType index, IntType value);
extern int A4ITypePrint(A4IType *array, char *dest, int start, int end);

enum Enuminternal_functions_ {
    _internal_functions_X,
};
typedef enum Enuminternal_functions_ internal_functionsEnum;

extern const char *enum_names[];
extern int EnumTypePrint(internal_functionsEnum value, char *dest, int start, int end);

/* CIF type: tuple(int; real) */
struct T2IR_struct {
    IntType _field0;
    RealType _field1;
};
typedef struct T2IR_struct T2IRType;

extern BoolType T2IRTypeEquals(T2IRType *left, T2IRType *right);
extern int T2IRTypePrint(T2IRType *tuple, char *dest, int start, int end);

/* CIF type: list[3] int */
struct A3I_struct {
    IntType data[3];
};
typedef struct A3I_struct A3IType;

extern BoolType A3ITypeEquals(A3IType *left, A3IType *right);
extern IntType A3ITypeProject(A3IType *array, IntType index);
extern void A3ITypeModify(A3IType *array, IntType index, IntType value);
extern int A3ITypePrint(A3IType *array, char *dest, int start, int end);

/* CIF type: tuple(int g; int h) */
struct T2II_struct {
    IntType _field0;
    IntType _field1;
};
typedef struct T2II_struct T2IIType;

extern BoolType T2IITypeEquals(T2IIType *left, T2IIType *right);
extern int T2IITypePrint(T2IIType *tuple, char *dest, int start, int end);


/* Event declarations. */
enum internal_functionsEventEnum_ {
    EVT_INITIAL_, /**< Initial step. */
    EVT_DELAY_,   /**< Delay step. */
    EVT_TAU_,     /**< Tau step. */
};
typedef enum internal_functionsEventEnum_ internal_functions_Event_;

/** Names of all the events. */
extern const char *internal_functions_event_names[];

/* Constants. */


/* Input variables. */




/* Declaration of internal functions. */
extern IntType inc_(IntType inc_x_);
extern IntType factorial_(IntType factorial_x_);
extern IntType rec1_(IntType rec1_x_);
extern IntType rec2_(IntType rec2_x_);
extern T2IRType multi_return_();
extern IntType f0_();
extern IntType f1_(IntType f1_x_);
extern IntType f2_(IntType f2_x_, IntType f2_y_);
extern RealType f3_(IntType f3_x_, IntType f3_y_, RealType f3_z_);
extern IntType locals_(IntType locals_x_);
extern A4IType rot1_(A4IType* rot1_x_tmp5);
extern A4IType rot2_(A4IType* rot2_x_tmp5);
extern IntType fa_(IntType fa_x_);
extern IntType fi_(IntType fi_x_);
extern IntType fw_();
extern IntType fu1_();
extern IntType fu2_();
extern IntType fu3_();
extern IntType fr_();

/* State variables (use for output only). */
extern RealType model_time; /**< Current model time. */
extern IntType aut_v00_;            /**< Discrete variable "int aut.v00". */
extern IntType aut_v01_;            /**< Discrete variable "int aut.v01". */
extern IntType aut_v02_;            /**< Discrete variable "int aut.v02". */
extern IntType aut_v03_;            /**< Discrete variable "int aut.v03". */
extern IntType aut_v04_;            /**< Discrete variable "int aut.v04". */
extern IntType aut_v05_;            /**< Discrete variable "int aut.v05". */
extern IntType aut_v06_;            /**< Discrete variable "int aut.v06". */
extern IntType aut_v07_;            /**< Discrete variable "int aut.v07". */
extern IntType aut_v08_;            /**< Discrete variable "int aut.v08". */
extern RealType aut_v09_;           /**< Discrete variable "real aut.v09". */
extern IntType aut_v10_;            /**< Discrete variable "int aut.v10". */
extern A4IType aut_v11_;            /**< Discrete variable "list[4] int aut.v11". */
extern A4IType aut_v12_;            /**< Discrete variable "list[4] int aut.v12". */
extern IntType aut_v13_;            /**< Discrete variable "int aut.v13". */
extern IntType aut_v14_;            /**< Discrete variable "int aut.v14". */
extern IntType aut_v15_;            /**< Discrete variable "int aut.v15". */
extern IntType aut_v16_;            /**< Discrete variable "int aut.v16". */
extern IntType aut_v17_;            /**< Discrete variable "int aut.v17". */
extern IntType aut_v18_;            /**< Discrete variable "int aut.v18". */
extern IntType aut_v19_;            /**< Discrete variable "int aut.v19". */
extern IntType aut_combi_;          /**< Discrete variable "int aut.combi". */
extern internal_functionsEnum aut_; /**< Discrete variable "E aut". */

/* Algebraic and derivative functions (use for output only). */



/* Code entry points. */
void internal_functions_EngineFirstStep(void);
void internal_functions_EngineTimeStep(double delta);

#if EVENT_OUTPUT
/**
 * External callback function reporting about the execution of an event.
 * @param event Event being executed.
 * @param pre If \c TRUE, event is about to be executed. If \c FALSE, event has been executed.
 * @note Function must be implemented externally.
 */
extern void internal_functions_InfoEvent(internal_functions_Event_ event, BoolType pre);
#endif

#if PRINT_OUTPUT
/**
 * External callback function to output the given text-line to the given filename.
 * @param text Text to print (does not have a EOL character).
 * @param fname Name of the file to print to.
 */
extern void internal_functions_PrintOutput(const char *text, const char *fname);
#endif

#endif

