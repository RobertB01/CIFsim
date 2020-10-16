/* Headers for the CIF to C translation of ${prefix}.cif
 * Generated file, DO NOT EDIT
 */

#ifndef CIF_C_${PREFIX}_ENGINE_H
#define CIF_C_${PREFIX}_ENGINE_H

#include "${prefix}_library.h"

/* Types of the specification.
 * Note that integer ranges are ignored in C.
 */
${generated-types}

/* Event declarations. */
${event-declarations}

/** Names of all the events. */
extern const char *${prefix}_event_names[];

/* Constants. */
${constant-declarations}

/* Input variables. */
${inputvar-declarations}

${inputvar-function-declaration}

/* Declaration of internal functions. */
${functions-declarations}

/* State variables (use for output only). */
extern RealType model_time; /**< Current model time. */
${statevar-declarations}

/* Algebraic and derivative functions (use for output only). */
${derivative-declarations}
${algvar-declarations}

/* Code entry points. */
void ${prefix}_EngineFirstStep(void);
void ${prefix}_EngineTimeStep(double delta);

#if EVENT_OUTPUT
/**
 * External callback function reporting about the execution of an event.
 * @param event Event being executed.
 * @param pre If \c TRUE, event is about to be executed. If \c FALSE, event has been executed.
 * @note Function must be implemented externally.
 */
extern void ${prefix}_InfoEvent(${prefix}_Event_ event, BoolType pre);
#endif

#if PRINT_OUTPUT
/**
 * External callback function to output the given text-line to the given filename.
 * @param text Text to print (does not have a EOL character).
 * @param fname Name of the file to print to.
 */
extern void ${prefix}_PrintOutput(const char *text, const char *fname);
#endif

#endif

