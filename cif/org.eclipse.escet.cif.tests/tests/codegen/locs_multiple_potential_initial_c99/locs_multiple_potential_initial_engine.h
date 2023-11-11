/* Headers for the CIF to C translation of locs_multiple_potential_initial.cif
 * Generated file, DO NOT EDIT
 */

#ifndef CIF_C_LOCS_MULTIPLE_POTENTIAL_INITIAL_ENGINE_H
#define CIF_C_LOCS_MULTIPLE_POTENTIAL_INITIAL_ENGINE_H

#include "locs_multiple_potential_initial_library.h"

/* Types of the specification.
 * Note that integer ranges are ignored in C.
 */
enum Enumlocs_multiple_potential_initial_ {
    _locs_multiple_potential_initial_loc1,
    _locs_multiple_potential_initial_loc2,
};
typedef enum Enumlocs_multiple_potential_initial_ locs_multiple_potential_initialEnum;

extern const char *enum_names[];
extern int EnumTypePrint(locs_multiple_potential_initialEnum value, char *dest, int start, int end);


/* Event declarations. */
enum locs_multiple_potential_initialEventEnum_ {
    EVT_INITIAL_, /**< Initial step. */
    EVT_DELAY_,   /**< Delay step. */
    EVT_TAU_,     /**< Tau step. */
};
typedef enum locs_multiple_potential_initialEventEnum_ locs_multiple_potential_initial_Event_;

/** Names of all the events. */
extern const char *locs_multiple_potential_initial_event_names[];

/* Constants. */


/* Input variables. */




/* Declaration of internal functions. */


/* State variables (use for output only). */
extern RealType model_time; /**< Current model time. */
extern locs_multiple_potential_initialEnum a_; /**< Discrete variable "E a". */

/* Algebraic and derivative functions (use for output only). */

static inline BoolType a_x_(void);



/**
 * Algebraic variable a.x = true;
 */
static inline BoolType a_x_(void) {
    return TRUE;
}

/* Code entry points. */
void locs_multiple_potential_initial_EngineFirstStep(void);
void locs_multiple_potential_initial_EngineTimeStep(double delta);

#if EVENT_OUTPUT
/**
 * External callback function reporting about the execution of an event.
 * @param event Event being executed.
 * @param pre If \c TRUE, event is about to be executed. If \c FALSE, event has been executed.
 * @note Function must be implemented externally.
 */
extern void locs_multiple_potential_initial_InfoEvent(locs_multiple_potential_initial_Event_ event, BoolType pre);
#endif

#if PRINT_OUTPUT
/**
 * External callback function to output the given text-line to the given filename.
 * @param text Text to print (does not have a EOL character).
 * @param fname Name of the file to print to.
 */
extern void locs_multiple_potential_initial_PrintOutput(const char *text, const char *fname);
#endif

#endif

