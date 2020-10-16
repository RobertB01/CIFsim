/* Additional code to allow compilation and test of the generated code.
 *
 * This file is generated, DO NOT EDIT
 */

#include <stdio.h>
#include "edges_engine.h"

/* Assign values to the input variables. */
void edges_AssignInputVariables(void) {
    /* Input variable "aut14.b". */
    aut14_b_ = FALSE;

    /* Input variable "aut14.i". */
    aut14_i_ = 0;

    /* Input variable "aut14.r". */
    aut14_r_ = 0.0;
}

void edges_InfoEvent(edges_Event_ event, BoolType pre) {
    const char *prePostText = pre ? "pre" : "post";
    printf("Executing %s-event \"%s\"\n", prePostText, edges_event_names[event]);
}

void edges_PrintOutput(const char *text, const char *fname) {
    printf("Print @ %s: \"%s\"\n", fname, text);
}

int main(void) {
    edges_EngineFirstStep();

    edges_EngineTimeStep(1.0);
    return 0;
}

