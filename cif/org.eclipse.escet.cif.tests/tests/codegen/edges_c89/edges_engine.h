/* Headers for the CIF to C translation of edges.cif
 * Generated file, DO NOT EDIT
 */

#ifndef CIF_C_EDGES_ENGINE_H
#define CIF_C_EDGES_ENGINE_H

#include "edges_library.h"

/* Types of the specification.
 * Note that integer ranges are ignored in C.
 */
enum Enumedges_ {
    /** Literal "loc1". */
    _edges_loc1,

    /** Literal "loc2". */
    _edges_loc2,

    /** Literal "loc3". */
    _edges_loc3,
};
typedef enum Enumedges_ edgesEnum;

extern const char *enum_names[];
extern int EnumTypePrint(edgesEnum value, char *dest, int start, int end);

/* CIF type: list[5] int */
struct A5I_struct {
    IntType data[5];
};
typedef struct A5I_struct A5IType;

extern BoolType A5ITypeEquals(A5IType *left, A5IType *right);
extern IntType A5ITypeProject(A5IType *array, IntType index);
extern void A5ITypeModify(A5IType *array, IntType index, IntType value);
extern int A5ITypePrint(A5IType *array, char *dest, int start, int end);

/* CIF type: tuple(int a; int b) */
struct T2II_struct {
    IntType _field0;
    IntType _field1;
};
typedef struct T2II_struct T2IIType;

extern BoolType T2IITypeEquals(T2IIType *left, T2IIType *right);
extern int T2IITypePrint(T2IIType *tuple, char *dest, int start, int end);

/* CIF type: tuple(tuple(int a; int b) t; string c) */
struct T2T2IIS_struct {
    T2IIType _field0;
    StringType _field1;
};
typedef struct T2T2IIS_struct T2T2IISType;

extern BoolType T2T2IISTypeEquals(T2T2IISType *left, T2T2IISType *right);
extern int T2T2IISTypePrint(T2T2IISType *tuple, char *dest, int start, int end);

/* CIF type: list[3] int */
struct A3I_struct {
    IntType data[3];
};
typedef struct A3I_struct A3IType;

extern BoolType A3ITypeEquals(A3IType *left, A3IType *right);
extern IntType A3ITypeProject(A3IType *array, IntType index);
extern void A3ITypeModify(A3IType *array, IntType index, IntType value);
extern int A3ITypePrint(A3IType *array, char *dest, int start, int end);

/* CIF type: list[2] list[3] int */
struct A2A3I_struct {
    A3IType data[2];
};
typedef struct A2A3I_struct A2A3IType;

extern BoolType A2A3ITypeEquals(A2A3IType *left, A2A3IType *right);
extern A3IType *A2A3ITypeProject(A2A3IType *array, IntType index);
extern void A2A3ITypeModify(A2A3IType *array, IntType index, A3IType *value);
extern int A2A3ITypePrint(A2A3IType *array, char *dest, int start, int end);

/* CIF type: list[1] int */
struct A1I_struct {
    IntType data[1];
};
typedef struct A1I_struct A1IType;

extern BoolType A1ITypeEquals(A1IType *left, A1IType *right);
extern IntType A1ITypeProject(A1IType *array, IntType index);
extern void A1ITypeModify(A1IType *array, IntType index, IntType value);
extern int A1ITypePrint(A1IType *array, char *dest, int start, int end);

/* CIF type: list[1] real */
struct A1R_struct {
    RealType data[1];
};
typedef struct A1R_struct A1RType;

extern BoolType A1RTypeEquals(A1RType *left, A1RType *right);
extern RealType A1RTypeProject(A1RType *array, IntType index);
extern void A1RTypeModify(A1RType *array, IntType index, RealType value);
extern int A1RTypePrint(A1RType *array, char *dest, int start, int end);

/* CIF type: tuple(list[1] int x; list[1] real y) */
struct T2A1IA1R_struct {
    A1IType _field0;
    A1RType _field1;
};
typedef struct T2A1IA1R_struct T2A1IA1RType;

extern BoolType T2A1IA1RTypeEquals(T2A1IA1RType *left, T2A1IA1RType *right);
extern int T2A1IA1RTypePrint(T2A1IA1RType *tuple, char *dest, int start, int end);

/* CIF type: list[2] tuple(list[1] int x; list[1] real y) */
struct A2T2A1IA1R_struct {
    T2A1IA1RType data[2];
};
typedef struct A2T2A1IA1R_struct A2T2A1IA1RType;

extern BoolType A2T2A1IA1RTypeEquals(A2T2A1IA1RType *left, A2T2A1IA1RType *right);
extern T2A1IA1RType *A2T2A1IA1RTypeProject(A2T2A1IA1RType *array, IntType index);
extern void A2T2A1IA1RTypeModify(A2T2A1IA1RType *array, IntType index, T2A1IA1RType *value);
extern int A2T2A1IA1RTypePrint(A2T2A1IA1RType *array, char *dest, int start, int end);

/* CIF type: tuple(string s; list[2] tuple(list[1] int x; list[1] real y) z) */
struct T2SA2T2A1IA1R_struct {
    StringType _field0;
    A2T2A1IA1RType _field1;
};
typedef struct T2SA2T2A1IA1R_struct T2SA2T2A1IA1RType;

extern BoolType T2SA2T2A1IA1RTypeEquals(T2SA2T2A1IA1RType *left, T2SA2T2A1IA1RType *right);
extern int T2SA2T2A1IA1RTypePrint(T2SA2T2A1IA1RType *tuple, char *dest, int start, int end);

/* CIF type: list[3] tuple(int a; int b) */
struct A3T2II_struct {
    T2IIType data[3];
};
typedef struct A3T2II_struct A3T2IIType;

extern BoolType A3T2IITypeEquals(A3T2IIType *left, A3T2IIType *right);
extern T2IIType *A3T2IITypeProject(A3T2IIType *array, IntType index);
extern void A3T2IITypeModify(A3T2IIType *array, IntType index, T2IIType *value);
extern int A3T2IITypePrint(A3T2IIType *array, char *dest, int start, int end);


/* Event declarations. */
enum edgesEventEnum_ {
    /** Initial step. */
    EVT_INITIAL_,

    /** Delay step. */
    EVT_DELAY_,

    /** Event "e02a". */
    e02a_,

    /** Event "e02b". */
    e02b_,

    /** Event "e03a". */
    e03a_,

    /** Event "e03b". */
    e03b_,

    /** Event "e04a". */
    e04a_,

    /** Event "e04b". */
    e04b_,

    /** Event "e04c". */
    e04c_,

    /** Event "e04d". */
    e04d_,

    /** Event "e04e". */
    e04e_,

    /** Event "e04f". */
    e04f_,

    /** Event "e05a". */
    e05a_,

    /** Event "e05b". */
    e05b_,

    /** Event "e05c". */
    e05c_,

    /** Event "e05d". */
    e05d_,

    /** Event "e05e". */
    e05e_,

    /** Event "e06a". */
    e06a_,

    /** Event "e06b". */
    e06b_,

    /** Event "e06c". */
    e06c_,

    /** Event "e06d". */
    e06d_,

    /** Event "e06e". */
    e06e_,

    /** Event "e07a". */
    e07a_,

    /** Event "e07b". */
    e07b_,

    /** Event "e08a". */
    e08a_,

    /** Event "e08b". */
    e08b_,

    /** Event "e08c". */
    e08c_,

    /** Event "e08d". */
    e08d_,

    /** Event "e08e". */
    e08e_,

    /** Event "e08f". */
    e08f_,

    /** Event "e08g". */
    e08g_,

    /** Event "e08h". */
    e08h_,

    /** Event "e09a". */
    e09a_,

    /** Event "e09b". */
    e09b_,

    /** Event "e09c". */
    e09c_,

    /** Event "e09d". */
    e09d_,

    /** Event "e09e". */
    e09e_,

    /** Event "e09f". */
    e09f_,

    /** Event "e09g". */
    e09g_,

    /** Event "e10a". */
    e10a_,

    /** Event "e10b". */
    e10b_,

    /** Event "e10c". */
    e10c_,

    /** Event "e10d". */
    e10d_,

    /** Event "e10e". */
    e10e_,

    /** Event "e10f". */
    e10f_,

    /** Event "e10g". */
    e10g_,

    /** Event "e10h". */
    e10h_,

    /** Event "e10i". */
    e10i_,

    /** Event "e11a". */
    e11a_,

    /** Event "e12a". */
    e12a_,

    /** Event "e12b". */
    e12b_,

    /** Event "e12c". */
    e12c_,

    /** Event "e12d". */
    e12d_,

    /** Event "e12e". */
    e12e_,

    /** Event "e13a". */
    e13a_,

    /** Event "e13b". */
    e13b_,

    /** Event "e13c". */
    e13c_,

    /** Event "e13d". */
    e13d_,

    /** Event "e13e". */
    e13e_,

    /** Event "e14a". */
    e14a_,

    /** Event "e14b". */
    e14b_,

    /** Event "e14c". */
    e14c_,

    /** Event "e14d". */
    e14d_,

    /** Event "e14e". */
    e14e_,

    /** Event "e14f". */
    e14f_,

    /** Event "e14g". */
    e14g_,

    /** Event "e14h". */
    e14h_,
};
typedef enum edgesEventEnum_ edges_Event_;

/** Names of all the events. */
extern const char *edges_event_names[];

/* Constants. */


/* Input variables. */

/** Input variable "bool aut14.b". */
extern BoolType aut14_b_;

/** Input variable "int aut14.i". */
extern IntType aut14_i_;

/** Input variable "real aut14.r". */
extern RealType aut14_r_;

extern void edges_AssignInputVariables();

/* Declaration of internal functions. */


/* State variables (use for output only). */
extern RealType model_time; /**< Current model time. */

/** Discrete variable "int[0..3] aut02.x". */
extern IntType aut02_x_;

/** Discrete variable "E aut02". */
extern edgesEnum aut02_;

/** Continuous variable "real aut03.c". */
extern RealType aut03_c_;

/** Discrete variable "int aut03.d". */
extern IntType aut03_d_;

/** Discrete variable "int aut04.a". */
extern IntType aut04_a_;

/** Discrete variable "int aut04.b". */
extern IntType aut04_b_;

/** Discrete variable "int aut04.c". */
extern IntType aut04_c_;

/** Discrete variable "int aut04.d". */
extern IntType aut04_d_;

/** Discrete variable "list[5] int aut05.v1". */
extern A5IType aut05_v1_;

/** Discrete variable "list[5] int aut05.v2". */
extern A5IType aut05_v2_;

/** Discrete variable "tuple(int a; int b) aut06.v1". */
extern T2IIType aut06_v1_;

/** Discrete variable "tuple(int a; int b) aut06.v2". */
extern T2IIType aut06_v2_;

/** Discrete variable "int aut06.x". */
extern IntType aut06_x_;

/** Discrete variable "int aut06.y". */
extern IntType aut06_y_;

/** Continuous variable "real aut07.x". */
extern RealType aut07_x_;

/** Continuous variable "real aut07.y". */
extern RealType aut07_y_;

/** Discrete variable "tuple(tuple(int a; int b) t; string c) aut08.tt1". */
extern T2T2IISType aut08_tt1_;

/** Discrete variable "tuple(tuple(int a; int b) t; string c) aut08.tt2". */
extern T2T2IISType aut08_tt2_;

/** Discrete variable "tuple(int a; int b) aut08.t". */
extern T2IIType aut08_t_;

/** Discrete variable "int aut08.i". */
extern IntType aut08_i_;

/** Discrete variable "int aut08.j". */
extern IntType aut08_j_;

/** Discrete variable "string aut08.s". */
extern StringType aut08_s_;

/** Discrete variable "list[2] list[3] int aut09.ll1". */
extern A2A3IType aut09_ll1_;

/** Discrete variable "list[2] list[3] int aut09.ll2". */
extern A2A3IType aut09_ll2_;

/** Discrete variable "list[3] int aut09.l". */
extern A3IType aut09_l_;

/** Discrete variable "int aut09.i". */
extern IntType aut09_i_;

/** Discrete variable "int aut09.j". */
extern IntType aut09_j_;

/** Discrete variable "tuple(string s; list[2] tuple(list[1] int x; list[1] real y) z) aut10.x1". */
extern T2SA2T2A1IA1RType aut10_x1_;

/** Discrete variable "tuple(string s; list[2] tuple(list[1] int x; list[1] real y) z) aut10.x2". */
extern T2SA2T2A1IA1RType aut10_x2_;

/** Discrete variable "list[2] tuple(list[1] int x; list[1] real y) aut10.l". */
extern A2T2A1IA1RType aut10_l_;

/** Discrete variable "list[1] int aut10.li". */
extern A1IType aut10_li_;

/** Discrete variable "list[1] real aut10.lr". */
extern A1RType aut10_lr_;

/** Discrete variable "int aut10.i". */
extern IntType aut10_i_;

/** Discrete variable "real aut10.r". */
extern RealType aut10_r_;

/** Discrete variable "list[3] tuple(int a; int b) aut11.v1". */
extern A3T2IIType aut11_v1_;

/** Discrete variable "real aut12.x". */
extern RealType aut12_x_;

/** Discrete variable "real aut12.y". */
extern RealType aut12_y_;

/** Discrete variable "real aut12.z". */
extern RealType aut12_z_;

/** Discrete variable "real aut12.td". */
extern RealType aut12_td_;

/** Continuous variable "real aut12.t". */
extern RealType aut12_t_;

/** Continuous variable "real aut12.u". */
extern RealType aut12_u_;

/** Discrete variable "real aut13.x". */
extern RealType aut13_x_;

/** Discrete variable "real aut13.y". */
extern RealType aut13_y_;

/** Discrete variable "real aut13.z". */
extern RealType aut13_z_;

/* Algebraic and derivative functions (use for output only). */
RealType aut03_c_deriv(void);
RealType aut07_x_deriv(void);
RealType aut07_y_deriv(void);
RealType aut12_t_deriv(void);
RealType aut12_u_deriv(void);
RealType aut12_v_(void);
RealType aut12_w_(void);
RealType aut13_v_(void);
RealType aut13_w_(void);


/* Code entry points. */
void edges_EngineFirstStep(void);
void edges_EngineTimeStep(double delta);

#if EVENT_OUTPUT
/**
 * External callback function reporting about the execution of an event.
 * @param event Event being executed.
 * @param pre If \c TRUE, event is about to be executed. If \c FALSE, event has been executed.
 * @note Function must be implemented externally.
 */
extern void edges_InfoEvent(edges_Event_ event, BoolType pre);
#endif

#if PRINT_OUTPUT
/**
 * External callback function to output the given text-line to the given filename.
 * @param text Text to print (does not have a EOL character).
 * @param fname Name of the file to print to.
 */
extern void edges_PrintOutput(const char *text, const char *fname);
#endif

#endif

