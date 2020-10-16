/* Headers for the CIF to C translation of types_and_literals.cif
 * Generated file, DO NOT EDIT
 */

#ifndef CIF_C_TYPES_AND_LITERALS_ENGINE_H
#define CIF_C_TYPES_AND_LITERALS_ENGINE_H

#include "types_and_literals_library.h"

/* Types of the specification.
 * Note that integer ranges are ignored in C.
 */
/* CIF type: list[3] bool */
struct A3B_struct {
    BoolType data[3];
};
typedef struct A3B_struct A3BType;

extern BoolType A3BTypeEquals(A3BType *left, A3BType *right);
extern BoolType A3BTypeProject(A3BType *array, IntType index);
extern void A3BTypeModify(A3BType *array, IntType index, BoolType value);
extern int A3BTypePrint(A3BType *array, char *dest, int start, int end);

/* CIF type: list[3] int[-3..2] */
struct A3I_struct {
    IntType data[3];
};
typedef struct A3I_struct A3IType;

extern BoolType A3ITypeEquals(A3IType *left, A3IType *right);
extern IntType A3ITypeProject(A3IType *array, IntType index);
extern void A3ITypeModify(A3IType *array, IntType index, IntType value);
extern int A3ITypePrint(A3IType *array, char *dest, int start, int end);

enum Enumtypes_and_literals_ {
    _types_and_literals_BLUE,
    _types_and_literals_RED,
    _types_and_literals_WHITE,
    _types_and_literals_X,
};
typedef enum Enumtypes_and_literals_ types_and_literalsEnum;

extern const char *enum_names[];
extern int EnumTypePrint(types_and_literalsEnum value, char *dest, int start, int end);

/* CIF type: list[10] real */
struct A10R_struct {
    RealType data[10];
};
typedef struct A10R_struct A10RType;

extern BoolType A10RTypeEquals(A10RType *left, A10RType *right);
extern RealType A10RTypeProject(A10RType *array, IntType index);
extern void A10RTypeModify(A10RType *array, IntType index, RealType value);
extern int A10RTypePrint(A10RType *array, char *dest, int start, int end);

/* CIF type: list[3] string */
struct A3S_struct {
    StringType data[3];
};
typedef struct A3S_struct A3SType;

extern BoolType A3STypeEquals(A3SType *left, A3SType *right);
extern StringType *A3STypeProject(A3SType *array, IntType index);
extern void A3STypeModify(A3SType *array, IntType index, StringType *value);
extern int A3STypePrint(A3SType *array, char *dest, int start, int end);

/* CIF type: list[0] int */
struct A0I_struct {
    IntType data[0];
};
typedef struct A0I_struct A0IType;

extern BoolType A0ITypeEquals(A0IType *left, A0IType *right);
extern IntType A0ITypeProject(A0IType *array, IntType index);
extern void A0ITypeModify(A0IType *array, IntType index, IntType value);
extern int A0ITypePrint(A0IType *array, char *dest, int start, int end);

/* CIF type: list[1] int */
struct A1I_struct {
    IntType data[1];
};
typedef struct A1I_struct A1IType;

extern BoolType A1ITypeEquals(A1IType *left, A1IType *right);
extern IntType A1ITypeProject(A1IType *array, IntType index);
extern void A1ITypeModify(A1IType *array, IntType index, IntType value);
extern int A1ITypePrint(A1IType *array, char *dest, int start, int end);

/* CIF type: list[2] bool */
struct A2B_struct {
    BoolType data[2];
};
typedef struct A2B_struct A2BType;

extern BoolType A2BTypeEquals(A2BType *left, A2BType *right);
extern BoolType A2BTypeProject(A2BType *array, IntType index);
extern void A2BTypeModify(A2BType *array, IntType index, BoolType value);
extern int A2BTypePrint(A2BType *array, char *dest, int start, int end);

/* CIF type: list[2] real */
struct A2R_struct {
    RealType data[2];
};
typedef struct A2R_struct A2RType;

extern BoolType A2RTypeEquals(A2RType *left, A2RType *right);
extern RealType A2RTypeProject(A2RType *array, IntType index);
extern void A2RTypeModify(A2RType *array, IntType index, RealType value);
extern int A2RTypePrint(A2RType *array, char *dest, int start, int end);

/* CIF type: list[3] list[2] real */
struct A3A2R_struct {
    A2RType data[3];
};
typedef struct A3A2R_struct A3A2RType;

extern BoolType A3A2RTypeEquals(A3A2RType *left, A3A2RType *right);
extern A2RType *A3A2RTypeProject(A3A2RType *array, IntType index);
extern void A3A2RTypeModify(A3A2RType *array, IntType index, A2RType *value);
extern int A3A2RTypePrint(A3A2RType *array, char *dest, int start, int end);

/* CIF type: tuple(int a; int b) */
struct T2II_struct {
    IntType _field0;
    IntType _field1;
};
typedef struct T2II_struct T2IIType;

extern BoolType T2IITypeEquals(T2IIType *left, T2IIType *right);
extern int T2IITypePrint(T2IIType *tuple, char *dest, int start, int end);

/* CIF type: tuple(int c; int d; string e) */
struct T3IIS_struct {
    IntType _field0;
    IntType _field1;
    StringType _field2;
};
typedef struct T3IIS_struct T3IISType;

extern BoolType T3IISTypeEquals(T3IISType *left, T3IISType *right);
extern int T3IISTypePrint(T3IISType *tuple, char *dest, int start, int end);

/* CIF type: tuple(tuple(int a; int b) c; string d) */
struct T2T2IIS_struct {
    T2IIType _field0;
    StringType _field1;
};
typedef struct T2T2IIS_struct T2T2IISType;

extern BoolType T2T2IISTypeEquals(T2T2IISType *left, T2T2IISType *right);
extern int T2T2IISTypePrint(T2T2IISType *tuple, char *dest, int start, int end);

/* CIF type: tuple(real b; string c) */
struct T2RS_struct {
    RealType _field0;
    StringType _field1;
};
typedef struct T2RS_struct T2RSType;

extern BoolType T2RSTypeEquals(T2RSType *left, T2RSType *right);
extern int T2RSTypePrint(T2RSType *tuple, char *dest, int start, int end);

/* CIF type: list[2] tuple(real b; string c) */
struct A2T2RS_struct {
    T2RSType data[2];
};
typedef struct A2T2RS_struct A2T2RSType;

extern BoolType A2T2RSTypeEquals(A2T2RSType *left, A2T2RSType *right);
extern T2RSType *A2T2RSTypeProject(A2T2RSType *array, IntType index);
extern void A2T2RSTypeModify(A2T2RSType *array, IntType index, T2RSType *value);
extern int A2T2RSTypePrint(A2T2RSType *array, char *dest, int start, int end);

/* CIF type: tuple(list[1] int a; list[2] tuple(real b; string c) d) */
struct T2A1IA2T2RS_struct {
    A1IType _field0;
    A2T2RSType _field1;
};
typedef struct T2A1IA2T2RS_struct T2A1IA2T2RSType;

extern BoolType T2A1IA2T2RSTypeEquals(T2A1IA2T2RSType *left, T2A1IA2T2RSType *right);
extern int T2A1IA2T2RSTypePrint(T2A1IA2T2RSType *tuple, char *dest, int start, int end);

/* CIF type: list[1] tuple(list[1] int a; list[2] tuple(real b; string c) d) */
struct A1T2A1IA2T2RS_struct {
    T2A1IA2T2RSType data[1];
};
typedef struct A1T2A1IA2T2RS_struct A1T2A1IA2T2RSType;

extern BoolType A1T2A1IA2T2RSTypeEquals(A1T2A1IA2T2RSType *left, A1T2A1IA2T2RSType *right);
extern T2A1IA2T2RSType *A1T2A1IA2T2RSTypeProject(A1T2A1IA2T2RSType *array, IntType index);
extern void A1T2A1IA2T2RSTypeModify(A1T2A1IA2T2RSType *array, IntType index, T2A1IA2T2RSType *value);
extern int A1T2A1IA2T2RSTypePrint(A1T2A1IA2T2RSType *array, char *dest, int start, int end);


/* Event declarations. */
enum types_and_literalsEventEnum_ {
    EVT_INITIAL_, /**< Initial step. */
    EVT_DELAY_,   /**< Delay step. */
    EVT_TAU_,     /**< Tau step. */
};
typedef enum types_and_literalsEventEnum_ types_and_literals_Event_;

/** Names of all the events. */
extern const char *types_and_literals_event_names[];

/* Constants. */
extern BoolType c1_; /**< Constant "c1". */
extern BoolType c2_; /**< Constant "c2". */
extern A3BType c3_; /**< Constant "c3". */
extern IntType c4_; /**< Constant "c4". */
extern IntType c5_; /**< Constant "c5". */
extern A3IType c6_; /**< Constant "c6". */
extern A3BType c7_; /**< Constant "c7". */
extern A3IType c8_; /**< Constant "c8". */
extern A3IType c9_; /**< Constant "c9". */
extern types_and_literalsEnum c10_; /**< Constant "c10". */
extern types_and_literalsEnum c11_; /**< Constant "c11". */
extern types_and_literalsEnum c12_; /**< Constant "c12". */
extern RealType c13_; /**< Constant "c13". */
extern RealType c14_; /**< Constant "c14". */
extern A10RType c15_; /**< Constant "c15". */
extern StringType c16_; /**< Constant "c16". */
extern StringType c17_; /**< Constant "c17". */
extern StringType c18_; /**< Constant "c18". */
extern StringType c19_; /**< Constant "c19". */
extern A3SType c20_; /**< Constant "c20". */
extern A0IType c21_; /**< Constant "c21". */
extern A1IType c22_; /**< Constant "c22". */
extern A2BType c23_; /**< Constant "c23". */
extern A2BType c24_; /**< Constant "c24". */
extern A3A2RType c25_; /**< Constant "c25". */
extern T2IIType c26_; /**< Constant "c26". */
extern T2IIType c27_; /**< Constant "c27". */
extern T3IISType c28_; /**< Constant "c28". */
extern T2T2IISType c29_; /**< Constant "c29". */
extern A1T2A1IA2T2RSType c31_; /**< Constant "c31". */
extern A1T2A1IA2T2RSType c30_; /**< Constant "c30". */

/* Input variables. */




/* Declaration of internal functions. */


/* State variables (use for output only). */
extern RealType model_time; /**< Current model time. */
extern types_and_literalsEnum a_; /**< Discrete variable "E a". */

/* Algebraic and derivative functions (use for output only). */



/* Code entry points. */
void types_and_literals_EngineFirstStep(void);
void types_and_literals_EngineTimeStep(double delta);

#if EVENT_OUTPUT
/**
 * External callback function reporting about the execution of an event.
 * @param event Event being executed.
 * @param pre If \c TRUE, event is about to be executed. If \c FALSE, event has been executed.
 * @note Function must be implemented externally.
 */
extern void types_and_literals_InfoEvent(types_and_literals_Event_ event, BoolType pre);
#endif

#if PRINT_OUTPUT
/**
 * External callback function to output the given text-line to the given filename.
 * @param text Text to print (does not have a EOL character).
 * @param fname Name of the file to print to.
 */
extern void types_and_literals_PrintOutput(const char *text, const char *fname);
#endif

#endif

