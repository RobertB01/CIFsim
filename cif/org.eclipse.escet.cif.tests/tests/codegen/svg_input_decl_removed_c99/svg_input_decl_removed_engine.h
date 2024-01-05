/* Headers for the CIF to C translation of svg_input_decl_removed.cif
 * Generated file, DO NOT EDIT
 */

#ifndef CIF_C_SVG_INPUT_DECL_REMOVED_ENGINE_H
#define CIF_C_SVG_INPUT_DECL_REMOVED_ENGINE_H

#include "svg_input_decl_removed_library.h"

/* Types of the specification.
 * Note that integer ranges are ignored in C.
 */
enum Enumsvg_input_decl_removed_ {
    _svg_input_decl_removed_l1,
    _svg_input_decl_removed_l2,
};
typedef enum Enumsvg_input_decl_removed_ svg_input_decl_removedEnum;

extern const char *enum_names[];
extern int EnumTypePrint(svg_input_decl_removedEnum value, char *dest, int start, int end);


/* Event declarations. */
enum svg_input_decl_removedEventEnum_ {
    EVT_INITIAL_, /**< Initial step. */
    EVT_DELAY_,   /**< Delay step. */
    EVT_TAU_,     /**< Tau step. */
    p_c_,         /**< Event p.c. */
};
typedef enum svg_input_decl_removedEventEnum_ svg_input_decl_removed_Event_;

/** Names of all the events. */
extern const char *svg_input_decl_removed_event_names[];

/* Constants. */


/* Input variables. */

/** Input variable "bool x". */
extern BoolType x_;

extern void svg_input_decl_removed_AssignInputVariables();

/* Declaration of internal functions. */


/* State variables (use for output only). */
extern RealType model_time; /**< Current model time. */

/** Discrete variable "E p". */
extern svg_input_decl_removedEnum p_;

/* Algebraic and derivative functions (use for output only). */






/* Code entry points. */
void svg_input_decl_removed_EngineFirstStep(void);
void svg_input_decl_removed_EngineTimeStep(double delta);

#if EVENT_OUTPUT
/**
 * External callback function reporting about the execution of an event.
 * @param event Event being executed.
 * @param pre If \c TRUE, event is about to be executed. If \c FALSE, event has been executed.
 * @note Function must be implemented externally.
 */
extern void svg_input_decl_removed_InfoEvent(svg_input_decl_removed_Event_ event, BoolType pre);
#endif

#if PRINT_OUTPUT
/**
 * External callback function to output the given text-line to the given filename.
 * @param text Text to print (does not have a EOL character).
 * @param fname Name of the file to print to.
 */
extern void svg_input_decl_removed_PrintOutput(const char *text, const char *fname);
#endif

#endif

