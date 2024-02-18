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
    _annos_doc___some_dummy_enum_literal,
};
typedef enum Enumannos_doc_ annos_docEnum;

extern const char *enum_names[];
extern int EnumTypePrint(annos_docEnum value, char *dest, int start, int end);


/* Event declarations. */
enum annos_docEventEnum_ {
    EVT_INITIAL_, /**< Initial step. */
    EVT_DELAY_,   /**< Delay step. */
    EVT_TAU_,     /**< Tau step. */
};
typedef enum annos_docEventEnum_ annos_doc_Event_;

/** Names of all the events. */
extern const char *annos_doc_event_names[];

/* Constants. */


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
 * Second doc.
 */
extern BoolType i5_;

extern void annos_doc_AssignInputVariables();

/* Declaration of internal functions. */


/* State variables (use for output only). */
extern RealType model_time; /**< Current model time. */

/** Discrete variable "bool a.i1". */
extern BoolType a_i1_;

/**
 * Discrete variable "bool a.i2".
 *
 * single line doc
 */
extern BoolType a_i2_;

/**
 * Discrete variable "bool a.i3".
 *
 * doc with multiple
 * lines of
 *  text
 */
extern BoolType a_i3_;

/**
 * Discrete variable "bool a.i4".
 *
 * some doc
 */
extern BoolType a_i4_;

/**
 * Discrete variable "bool a.i5".
 *
 * First doc.
 *
 * Second doc.
 */
extern BoolType a_i5_;

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
 * Second doc.
 */
extern RealType contvars_c5_;

/* Algebraic and derivative functions (use for output only). */
static inline RealType contvars_c1_deriv(void);
static inline RealType contvars_c2_deriv(void);
static inline RealType contvars_c3_deriv(void);
static inline RealType contvars_c4_deriv(void);
static inline RealType contvars_c5_deriv(void);


/** Derivative of "contvars.c1". */
static inline RealType contvars_c1_deriv(void) {
    return 1.0;
}

/** Derivative of "contvars.c2". */
static inline RealType contvars_c2_deriv(void) {
    return 2.0;
}

/** Derivative of "contvars.c3". */
static inline RealType contvars_c3_deriv(void) {
    return 3.0;
}

/** Derivative of "contvars.c4". */
static inline RealType contvars_c4_deriv(void) {
    return 4.0;
}

/** Derivative of "contvars.c5". */
static inline RealType contvars_c5_deriv(void) {
    return 5.0;
}


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

