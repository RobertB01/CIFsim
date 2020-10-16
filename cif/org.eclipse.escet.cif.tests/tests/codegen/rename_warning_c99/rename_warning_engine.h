/* Headers for the CIF to C translation of rename_warning.cif
 * Generated file, DO NOT EDIT
 */

#ifndef CIF_C_RENAME_WARNING_ENGINE_H
#define CIF_C_RENAME_WARNING_ENGINE_H

#include "rename_warning_library.h"

/* Types of the specification.
 * Note that integer ranges are ignored in C.
 */
enum Enumrename_warning_ {
    _rename_warning_X,
};
typedef enum Enumrename_warning_ rename_warningEnum;

extern const char *enum_names[];
extern int EnumTypePrint(rename_warningEnum value, char *dest, int start, int end);


/* Event declarations. */
enum rename_warningEventEnum_ {
    EVT_INITIAL_, /**< Initial step. */
    EVT_DELAY_,   /**< Delay step. */
    EVT_TAU_,     /**< Tau step. */
};
typedef enum rename_warningEventEnum_ rename_warning_Event_;

/** Names of all the events. */
extern const char *rename_warning_event_names[];

/* Constants. */
extern IntType a_b_; /**< Constant "a_b". */
extern IntType a_b_2; /**< Constant "a.b". */

/* Input variables. */




/* Declaration of internal functions. */


/* State variables (use for output only). */
extern RealType model_time; /**< Current model time. */
extern rename_warningEnum a_; /**< Discrete variable "E a". */

/* Algebraic and derivative functions (use for output only). */






/* Code entry points. */
void rename_warning_EngineFirstStep(void);
void rename_warning_EngineTimeStep(double delta);

#if EVENT_OUTPUT
/**
 * External callback function reporting about the execution of an event.
 * @param event Event being executed.
 * @param pre If \c TRUE, event is about to be executed. If \c FALSE, event has been executed.
 * @note Function must be implemented externally.
 */
extern void rename_warning_InfoEvent(rename_warning_Event_ event, BoolType pre);
#endif

#if PRINT_OUTPUT
/**
 * External callback function to output the given text-line to the given filename.
 * @param text Text to print (does not have a EOL character).
 * @param fname Name of the file to print to.
 */
extern void rename_warning_PrintOutput(const char *text, const char *fname);
#endif

#endif

