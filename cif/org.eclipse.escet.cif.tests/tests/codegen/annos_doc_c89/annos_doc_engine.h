/* Headers for the CIF to C translation of annos_doc.cif
 * Generated file, DO NOT EDIT
 */

#ifndef CIF_C_ANNOS_DOC_ENGINE_H
#define CIF_C_ANNOS_DOC_ENGINE_H

#include "annos_doc_library.h"

/* Types of the specification.
 * Note that integer ranges are ignored in C.
 */
enum Enumannos_doc_ {
    /** Literal "l1". */
    _annos_doc_l1,

    /**
     * Literal "l2".
     *
     * single line doc
     */
    _annos_doc_l2,

    /**
     * Literal "l3".
     *
     * doc with multiple
     * lines of
     *  text
     */
    _annos_doc_l3,

    /**
     * Literal "l4".
     *
     * some doc
     */
    _annos_doc_l4,

    /**
     * Literal "l5".
     *
     * First doc.
     *
     * Second doc line 1.
     * Second doc line 2.
     */
    _annos_doc_l5,
};
typedef enum Enumannos_doc_ annos_docEnum;

extern const char *enum_names[];
extern int EnumTypePrint(annos_docEnum value, char *dest, int start, int end);


/* Event declarations. */
enum annos_docEventEnum_ {
    /** Initial step. */
    EVT_INITIAL_,

    /** Delay step. */
    EVT_DELAY_,

    /** Tau step. */
    EVT_TAU_,

    /** Event "events.e1". */
    events_e1_,

    /**
     * Event "events.e2".
     *
     * single line doc
     */
    events_e2_,

    /**
     * Event "events.e3".
     *
     * doc with multiple
     * lines of
     *  text
     */
    events_e3_,

    /**
     * Event "events.e4".
     *
     * some doc
     */
    events_e4_,

    /**
     * Event "events.e5".
     *
     * First doc.
     *
     * Second doc line 1.
     * Second doc line 2.
     */
    events_e5_,
};
typedef enum annos_docEventEnum_ annos_doc_Event_;

/** Names of all the events. */
extern const char *annos_doc_event_names[];

/* Constants. */

/** Constant "constants.c1". */
extern IntType constants_c1_;

/**
 * Constant "constants.c2".
 *
 * single line doc
 */
extern IntType constants_c2_;

/**
 * Constant "constants.c3".
 *
 * doc with multiple
 * lines of
 *  text
 */
extern IntType constants_c3_;

/**
 * Constant "constants.c4".
 *
 * some doc
 */
extern IntType constants_c4_;

/**
 * Constant "constants.c5".
 *
 * First doc.
 *
 * Second doc line 1.
 * Second doc line 2.
 */
extern IntType constants_c5_;

/* Input variables. */

/** Input variable "bool i1". */
extern BoolType i1_;

/**
 * Input variable "bool i2".
 *
 * single line doc
 */
extern BoolType i2_;

/**
 * Input variable "bool i3".
 *
 * doc with multiple
 * lines of
 *  text
 */
extern BoolType i3_;

/**
 * Input variable "bool i4".
 *
 * some doc
 */
extern BoolType i4_;

/**
 * Input variable "bool i5".
 *
 * First doc.
 *
 * Second doc line 1.
 * Second doc line 2.
 */
extern BoolType i5_;

/**
 * Input variable "bool preconditions.i".
 *
 * {}
 */
extern BoolType preconditions_i_;

extern void annos_doc_AssignInputVariables();

/* Declaration of internal functions. */

/**
 * Function "funcs.func1".
 *
 * @param funcs_func1_p_ Function parameter "funcs.func1.p".
 * @return The return value of the function.
 */
extern BoolType funcs_func1_(BoolType funcs_func1_p_);

/**
 * Function "funcs.func2".
 *
 * single line doc
 *
 * @param funcs_func2_p_ Function parameter "funcs.func2.p".
 *
 *     single line doc
 * @return The return value of the function.
 */
extern BoolType funcs_func2_(BoolType funcs_func2_p_);

/**
 * Function "funcs.func3".
 *
 * doc with multiple
 * lines of
 *  text
 *
 * @param funcs_func3_p_ Function parameter "funcs.func3.p".
 *
 *     doc with multiple
 *     lines of
 *      text
 * @return The return value of the function.
 */
extern BoolType funcs_func3_(BoolType funcs_func3_p_);

/**
 * Function "funcs.func4".
 *
 * some doc
 *
 * @param funcs_func4_p_ Function parameter "funcs.func4.p".
 *
 *     some doc
 * @return The return value of the function.
 */
extern BoolType funcs_func4_(BoolType funcs_func4_p_);

/**
 * Function "funcs.func5".
 *
 * First doc.
 *
 * Second doc line 1.
 * Second doc line 2.
 *
 * @param funcs_func5_p_ Function parameter "funcs.func5.p".
 *
 *     First doc.
 *
 *     Second doc line 1.
 *     Second doc line 2.
 * @return The return value of the function.
 */
extern BoolType funcs_func5_(BoolType funcs_func5_p_);

/* State variables (use for output only). */
extern RealType model_time; /**< Current model time. */

/** Continuous variable "real contvars.c1". */
extern RealType contvars_c1_;

/**
 * Continuous variable "real contvars.c2".
 *
 * single line doc
 */
extern RealType contvars_c2_;

/**
 * Continuous variable "real contvars.c3".
 *
 * doc with multiple
 * lines of
 *  text
 */
extern RealType contvars_c3_;

/**
 * Continuous variable "real contvars.c4".
 *
 * some doc
 */
extern RealType contvars_c4_;

/**
 * Continuous variable "real contvars.c5".
 *
 * First doc.
 *
 * Second doc line 1.
 * Second doc line 2.
 */
extern RealType contvars_c5_;

/** Discrete variable "bool discvars.d1". */
extern BoolType discvars_d1_;

/**
 * Discrete variable "bool discvars.d2".
 *
 * single line doc
 */
extern BoolType discvars_d2_;

/**
 * Discrete variable "bool discvars.d3".
 *
 * doc with multiple
 * lines of
 *  text
 */
extern BoolType discvars_d3_;

/**
 * Discrete variable "bool discvars.d4".
 *
 * some doc
 */
extern BoolType discvars_d4_;

/**
 * Discrete variable "bool discvars.d5".
 *
 * First doc.
 *
 * Second doc line 1.
 * Second doc line 2.
 */
extern BoolType discvars_d5_;

/* Algebraic and derivative functions (use for output only). */
RealType contvars_c1_deriv(void);
RealType contvars_c2_deriv(void);
RealType contvars_c3_deriv(void);
RealType contvars_c4_deriv(void);
RealType contvars_c5_deriv(void);
IntType algvars_a1_(void);
IntType algvars_a2_(void);
IntType algvars_a3_(void);
IntType algvars_a4_(void);
IntType algvars_a5_(void);


/* Code entry points. */
void annos_doc_EngineFirstStep(void);
void annos_doc_EngineTimeStep(double delta);

#if EVENT_OUTPUT
/**
 * External callback function reporting about the execution of an event.
 * @param event Event being executed.
 * @param pre If \c TRUE, event is about to be executed. If \c FALSE, event has been executed.
 * @note Function must be implemented externally.
 */
extern void annos_doc_InfoEvent(annos_doc_Event_ event, BoolType pre);
#endif

#if PRINT_OUTPUT
/**
 * External callback function to output the given text-line to the given filename.
 * @param text Text to print (does not have a EOL character).
 * @param fname Name of the file to print to.
 */
extern void annos_doc_PrintOutput(const char *text, const char *fname);
#endif

#endif

