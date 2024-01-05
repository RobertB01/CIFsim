/* Headers for the CIF to C translation of exprs.cif
 * Generated file, DO NOT EDIT
 */

#ifndef CIF_C_EXPRS_ENGINE_H
#define CIF_C_EXPRS_ENGINE_H

#include "exprs_library.h"

/* Types of the specification.
 * Note that integer ranges are ignored in C.
 */
enum Enumexprs_ {
    _exprs_A,
    _exprs_B,
};
typedef enum Enumexprs_ exprsEnum;

extern const char *enum_names[];
extern int EnumTypePrint(exprsEnum value, char *dest, int start, int end);

/* CIF type: list[2] int */
struct A2I_struct {
    IntType data[2];
};
typedef struct A2I_struct A2IType;

extern BoolType A2ITypeEquals(A2IType *left, A2IType *right);
extern IntType A2ITypeProject(A2IType *array, IntType index);
extern void A2ITypeModify(A2IType *array, IntType index, IntType value);
extern int A2ITypePrint(A2IType *array, char *dest, int start, int end);

/* CIF type: list[3] int */
struct A3I_struct {
    IntType data[3];
};
typedef struct A3I_struct A3IType;

extern BoolType A3ITypeEquals(A3IType *left, A3IType *right);
extern IntType A3ITypeProject(A3IType *array, IntType index);
extern void A3ITypeModify(A3IType *array, IntType index, IntType value);
extern int A3ITypePrint(A3IType *array, char *dest, int start, int end);

/* CIF type: list[1] bool */
struct A1B_struct {
    BoolType data[1];
};
typedef struct A1B_struct A1BType;

extern BoolType A1BTypeEquals(A1BType *left, A1BType *right);
extern BoolType A1BTypeProject(A1BType *array, IntType index);
extern void A1BTypeModify(A1BType *array, IntType index, BoolType value);
extern int A1BTypePrint(A1BType *array, char *dest, int start, int end);

/* CIF type: tuple(int a; int b) */
struct T2II_struct {
    IntType _field0;
    IntType _field1;
};
typedef struct T2II_struct T2IIType;

extern BoolType T2IITypeEquals(T2IIType *left, T2IIType *right);
extern int T2IITypePrint(T2IIType *tuple, char *dest, int start, int end);


/* Event declarations. */
enum exprsEventEnum_ {
    EVT_INITIAL_, /**< Initial step. */
    EVT_DELAY_,   /**< Delay step. */
    EVT_TAU_,     /**< Tau step. */
};
typedef enum exprsEventEnum_ exprs_Event_;

/** Names of all the events. */
extern const char *exprs_event_names[];

/* Constants. */
extern IntType x1_; /**< Constant "x1". */

/* Input variables. */

/** Input variable "int x8". */
extern IntType x8_;

extern void exprs_AssignInputVariables();

/* Declaration of internal functions. */
extern IntType f1_(IntType f1_x_);
extern IntType inc_(IntType inc_x_);

/* State variables (use for output only). */
extern RealType model_time; /**< Current model time. */

/** Continuous variable "real x5". */
extern RealType x5_;

/** Discrete variable "int a1.x". */
extern IntType a1_x_;

/** Discrete variable "bool AA.vb". */
extern BoolType AA_vb_;

/** Discrete variable "int AA.vi". */
extern IntType AA_vi_;

/** Discrete variable "int[1..3] AA.vp". */
extern IntType AA_vp_;

/** Discrete variable "int[-5..-1] AA.vn". */
extern IntType AA_vn_;

/** Discrete variable "int[0..5] AA.vz". */
extern IntType AA_vz_;

/** Discrete variable "real AA.vr". */
extern RealType AA_vr_;

/** Discrete variable "string AA.vs". */
extern StringType AA_vs_;

/** Discrete variable "E AA.ve". */
extern exprsEnum AA_ve_;

/** Discrete variable "list[2] int AA.va". */
extern A2IType AA_va_;

/** Discrete variable "real AA.v2". */
extern RealType AA_v2_;

/** Discrete variable "real AA.i2r". */
extern RealType AA_i2r_;

/** Discrete variable "string AA.b2s". */
extern StringType AA_b2s_;

/** Discrete variable "string AA.i2s". */
extern StringType AA_i2s_;

/** Discrete variable "string AA.r2s". */
extern StringType AA_r2s_;

/** Discrete variable "bool AA.s2b". */
extern BoolType AA_s2b_;

/** Discrete variable "int AA.s2i". */
extern IntType AA_s2i_;

/** Discrete variable "real AA.s2r". */
extern RealType AA_s2r_;

/** Discrete variable "list[3] int AA.self_cast1". */
extern A3IType AA_self_cast1_;

/** Discrete variable "list[3] int AA.self_cast2". */
extern A3IType AA_self_cast2_;

/** Discrete variable "bool AA.inv1". */
extern BoolType AA_inv1_;

/** Discrete variable "bool AA.inv2". */
extern BoolType AA_inv2_;

/** Discrete variable "int AA.neg1". */
extern IntType AA_neg1_;

/** Discrete variable "int AA.neg2". */
extern IntType AA_neg2_;

/** Discrete variable "int AA.neg3". */
extern IntType AA_neg3_;

/** Discrete variable "int AA.neg4". */
extern IntType AA_neg4_;

/** Discrete variable "int AA.pos1". */
extern IntType AA_pos1_;

/** Discrete variable "int AA.pos2". */
extern IntType AA_pos2_;

/** Discrete variable "int AA.posneg". */
extern IntType AA_posneg_;

/** Discrete variable "list[1] bool AA.l3i". */
extern A1BType AA_l3i_;

/** Discrete variable "int[0..4] AA.idx1". */
extern IntType AA_idx1_;

/** Discrete variable "bool AA.vt". */
extern BoolType AA_vt_;

/** Discrete variable "bool AA.vf". */
extern BoolType AA_vf_;

/** Discrete variable "bool AA.short_and". */
extern BoolType AA_short_and_;

/** Discrete variable "bool AA.short_or". */
extern BoolType AA_short_or_;

/** Discrete variable "bool AA.impl". */
extern BoolType AA_impl_;

/** Discrete variable "bool AA.biimpl". */
extern BoolType AA_biimpl_;

/** Discrete variable "bool AA.conj". */
extern BoolType AA_conj_;

/** Discrete variable "bool AA.disj". */
extern BoolType AA_disj_;

/** Discrete variable "bool AA.lt1". */
extern BoolType AA_lt1_;

/** Discrete variable "bool AA.le1". */
extern BoolType AA_le1_;

/** Discrete variable "bool AA.gt1". */
extern BoolType AA_gt1_;

/** Discrete variable "bool AA.ge1". */
extern BoolType AA_ge1_;

/** Discrete variable "bool AA.lt2". */
extern BoolType AA_lt2_;

/** Discrete variable "bool AA.le2". */
extern BoolType AA_le2_;

/** Discrete variable "bool AA.gt2". */
extern BoolType AA_gt2_;

/** Discrete variable "bool AA.ge2". */
extern BoolType AA_ge2_;

/** Discrete variable "bool AA.lt3". */
extern BoolType AA_lt3_;

/** Discrete variable "bool AA.le3". */
extern BoolType AA_le3_;

/** Discrete variable "bool AA.gt3". */
extern BoolType AA_gt3_;

/** Discrete variable "bool AA.ge3". */
extern BoolType AA_ge3_;

/** Discrete variable "bool AA.lt4". */
extern BoolType AA_lt4_;

/** Discrete variable "bool AA.le4". */
extern BoolType AA_le4_;

/** Discrete variable "bool AA.gt4". */
extern BoolType AA_gt4_;

/** Discrete variable "bool AA.ge4". */
extern BoolType AA_ge4_;

/** Discrete variable "bool AA.eq1". */
extern BoolType AA_eq1_;

/** Discrete variable "bool AA.eq2". */
extern BoolType AA_eq2_;

/** Discrete variable "bool AA.eq3". */
extern BoolType AA_eq3_;

/** Discrete variable "bool AA.eq4". */
extern BoolType AA_eq4_;

/** Discrete variable "bool AA.eq5". */
extern BoolType AA_eq5_;

/** Discrete variable "bool AA.ne1". */
extern BoolType AA_ne1_;

/** Discrete variable "bool AA.ne2". */
extern BoolType AA_ne2_;

/** Discrete variable "bool AA.ne3". */
extern BoolType AA_ne3_;

/** Discrete variable "bool AA.ne4". */
extern BoolType AA_ne4_;

/** Discrete variable "bool AA.ne5". */
extern BoolType AA_ne5_;

/** Discrete variable "int AA.add1". */
extern IntType AA_add1_;

/** Discrete variable "real AA.add2". */
extern RealType AA_add2_;

/** Discrete variable "real AA.add3". */
extern RealType AA_add3_;

/** Discrete variable "real AA.add4". */
extern RealType AA_add4_;

/** Discrete variable "string AA.add5". */
extern StringType AA_add5_;

/** Discrete variable "int AA.add6". */
extern IntType AA_add6_;

/** Discrete variable "int AA.add7". */
extern IntType AA_add7_;

/** Discrete variable "int AA.add8". */
extern IntType AA_add8_;

/** Discrete variable "int AA.sub1". */
extern IntType AA_sub1_;

/** Discrete variable "real AA.sub2". */
extern RealType AA_sub2_;

/** Discrete variable "real AA.sub3". */
extern RealType AA_sub3_;

/** Discrete variable "real AA.sub4". */
extern RealType AA_sub4_;

/** Discrete variable "int AA.sub5". */
extern IntType AA_sub5_;

/** Discrete variable "int AA.sub6". */
extern IntType AA_sub6_;

/** Discrete variable "int AA.sub7". */
extern IntType AA_sub7_;

/** Discrete variable "int AA.mul1". */
extern IntType AA_mul1_;

/** Discrete variable "real AA.mul2". */
extern RealType AA_mul2_;

/** Discrete variable "real AA.mul3". */
extern RealType AA_mul3_;

/** Discrete variable "real AA.mul4". */
extern RealType AA_mul4_;

/** Discrete variable "int AA.mul5". */
extern IntType AA_mul5_;

/** Discrete variable "int AA.mul6". */
extern IntType AA_mul6_;

/** Discrete variable "int AA.mul7". */
extern IntType AA_mul7_;

/** Discrete variable "real AA.rdiv1". */
extern RealType AA_rdiv1_;

/** Discrete variable "real AA.rdiv2". */
extern RealType AA_rdiv2_;

/** Discrete variable "real AA.rdiv3". */
extern RealType AA_rdiv3_;

/** Discrete variable "real AA.rdiv4". */
extern RealType AA_rdiv4_;

/** Discrete variable "real AA.rdiv5". */
extern RealType AA_rdiv5_;

/** Discrete variable "real AA.rdiv6". */
extern RealType AA_rdiv6_;

/** Discrete variable "int AA.div1". */
extern IntType AA_div1_;

/** Discrete variable "int AA.div2". */
extern IntType AA_div2_;

/** Discrete variable "int AA.div3". */
extern IntType AA_div3_;

/** Discrete variable "int AA.div4". */
extern IntType AA_div4_;

/** Discrete variable "int AA.mod1". */
extern IntType AA_mod1_;

/** Discrete variable "int AA.mod2". */
extern IntType AA_mod2_;

/** Discrete variable "list[2] int AA.li". */
extern A2IType AA_li_;

/** Discrete variable "tuple(int a; int b) AA.tii". */
extern T2IIType AA_tii_;

/** Discrete variable "string AA.ss". */
extern StringType AA_ss_;

/** Discrete variable "int AA.proj1". */
extern IntType AA_proj1_;

/** Discrete variable "int AA.proj2". */
extern IntType AA_proj2_;

/** Discrete variable "int AA.proj3". */
extern IntType AA_proj3_;

/** Discrete variable "int AA.proj4". */
extern IntType AA_proj4_;

/** Discrete variable "string AA.proj5". */
extern StringType AA_proj5_;

/** Discrete variable "string AA.proj6". */
extern StringType AA_proj6_;

/** Discrete variable "real AA.f_acos". */
extern RealType AA_f_acos_;

/** Discrete variable "real AA.f_asin". */
extern RealType AA_f_asin_;

/** Discrete variable "real AA.f_atan". */
extern RealType AA_f_atan_;

/** Discrete variable "real AA.f_cos". */
extern RealType AA_f_cos_;

/** Discrete variable "real AA.f_sin". */
extern RealType AA_f_sin_;

/** Discrete variable "real AA.f_tan". */
extern RealType AA_f_tan_;

/** Discrete variable "int AA.f_abs1". */
extern IntType AA_f_abs1_;

/** Discrete variable "int AA.f_abs12". */
extern IntType AA_f_abs12_;

/** Discrete variable "real AA.f_abs2". */
extern RealType AA_f_abs2_;

/** Discrete variable "real AA.f_cbrt". */
extern RealType AA_f_cbrt_;

/** Discrete variable "int AA.f_ceil". */
extern IntType AA_f_ceil_;

/** Discrete variable "bool AA.f_empty". */
extern BoolType AA_f_empty_;

/** Discrete variable "real AA.f_exp". */
extern RealType AA_f_exp_;

/** Discrete variable "int AA.f_floor". */
extern IntType AA_f_floor_;

/** Discrete variable "real AA.f_ln". */
extern RealType AA_f_ln_;

/** Discrete variable "real AA.f_log". */
extern RealType AA_f_log_;

/** Discrete variable "int AA.f_max1". */
extern IntType AA_f_max1_;

/** Discrete variable "real AA.f_max2". */
extern RealType AA_f_max2_;

/** Discrete variable "real AA.f_max3". */
extern RealType AA_f_max3_;

/** Discrete variable "real AA.f_max4". */
extern RealType AA_f_max4_;

/** Discrete variable "int AA.f_min1". */
extern IntType AA_f_min1_;

/** Discrete variable "real AA.f_min2". */
extern RealType AA_f_min2_;

/** Discrete variable "real AA.f_min3". */
extern RealType AA_f_min3_;

/** Discrete variable "real AA.f_min4". */
extern RealType AA_f_min4_;

/** Discrete variable "real AA.f_pow1". */
extern RealType AA_f_pow1_;

/** Discrete variable "int AA.f_pow12". */
extern IntType AA_f_pow12_;

/** Discrete variable "real AA.f_pow2". */
extern RealType AA_f_pow2_;

/** Discrete variable "real AA.f_pow3". */
extern RealType AA_f_pow3_;

/** Discrete variable "real AA.f_pow4". */
extern RealType AA_f_pow4_;

/** Discrete variable "int AA.f_round". */
extern IntType AA_f_round_;

/** Discrete variable "real AA.f_scale". */
extern RealType AA_f_scale_;

/** Discrete variable "int AA.f_sign1". */
extern IntType AA_f_sign1_;

/** Discrete variable "int AA.f_sign2". */
extern IntType AA_f_sign2_;

/** Discrete variable "int AA.f_size1". */
extern IntType AA_f_size1_;

/** Discrete variable "int AA.f_size2". */
extern IntType AA_f_size2_;

/** Discrete variable "real AA.f_sqrt". */
extern RealType AA_f_sqrt_;

/* Algebraic and derivative functions (use for output only). */
RealType x5_deriv(void);
IntType v1_(void);
IntType if1_(void);
IntType if2_(void);
IntType if3_(void);
IntType fcall1_(void);
IntType fcall2_(void);
exprsEnum vea_(void);
IntType x2_(void);
IntType x3_(void);
IntType x4_(void);
RealType x6_(void);
BoolType x7_(void);
IntType x9_(void);


/* Code entry points. */
void exprs_EngineFirstStep(void);
void exprs_EngineTimeStep(double delta);

#if EVENT_OUTPUT
/**
 * External callback function reporting about the execution of an event.
 * @param event Event being executed.
 * @param pre If \c TRUE, event is about to be executed. If \c FALSE, event has been executed.
 * @note Function must be implemented externally.
 */
extern void exprs_InfoEvent(exprs_Event_ event, BoolType pre);
#endif

#if PRINT_OUTPUT
/**
 * External callback function to output the given text-line to the given filename.
 * @param text Text to print (does not have a EOL character).
 * @param fname Name of the file to print to.
 */
extern void exprs_PrintOutput(const char *text, const char *fname);
#endif

#endif

