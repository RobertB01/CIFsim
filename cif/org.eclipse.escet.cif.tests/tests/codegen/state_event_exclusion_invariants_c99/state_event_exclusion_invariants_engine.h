/* Headers for the CIF to C translation of state_event_exclusion_invariants.cif
 * Generated file, DO NOT EDIT
 */

#ifndef CIF_C_STATE_EVENT_EXCLUSION_INVARIANTS_ENGINE_H
#define CIF_C_STATE_EVENT_EXCLUSION_INVARIANTS_ENGINE_H

#include "state_event_exclusion_invariants_library.h"

/* Types of the specification.
 * Note that integer ranges are ignored in C.
 */


/* Event declarations. */
enum state_event_exclusion_invariantsEventEnum_ {
    EVT_INITIAL_, /**< Initial step. */
    EVT_DELAY_,   /**< Delay step. */
    EVT_TAU_,     /**< Tau step. */
    e_,           /**< Event e. */
};
typedef enum state_event_exclusion_invariantsEventEnum_ state_event_exclusion_invariants_Event_;

/** Names of all the events. */
extern const char *state_event_exclusion_invariants_event_names[];

/* Constants. */


/* Input variables. */

/** Input variable "int x". */
extern IntType x_;

extern void state_event_exclusion_invariants_AssignInputVariables();

/* Declaration of internal functions. */


/* State variables (use for output only). */
extern RealType model_time; /**< Current model time. */


/* Algebraic and derivative functions (use for output only). */






/* Code entry points. */
void state_event_exclusion_invariants_EngineFirstStep(void);
void state_event_exclusion_invariants_EngineTimeStep(double delta);

#if EVENT_OUTPUT
/**
 * External callback function reporting about the execution of an event.
 * @param event Event being executed.
 * @param pre If \c TRUE, event is about to be executed. If \c FALSE, event has been executed.
 * @note Function must be implemented externally.
 */
extern void state_event_exclusion_invariants_InfoEvent(state_event_exclusion_invariants_Event_ event, BoolType pre);
#endif

#if PRINT_OUTPUT
/**
 * External callback function to output the given text-line to the given filename.
 * @param text Text to print (does not have a EOL character).
 * @param fname Name of the file to print to.
 */
extern void state_event_exclusion_invariants_PrintOutput(const char *text, const char *fname);
#endif

#endif

