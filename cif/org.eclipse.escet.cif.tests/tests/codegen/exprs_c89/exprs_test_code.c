/* Additional code to allow compilation and test of the generated code.
 *
 * This file is generated, DO NOT EDIT
 */

#include <stdio.h>
#include "exprs_engine.h"

/* Assign values to the input variables. */
void exprs_AssignInputVariables(void) {
    /* Input variable "x8". */
    x8_ = 0;
}

void exprs_InfoEvent(exprs_Event_ event, BoolType pre) {
    const char *prePostText = pre ? "pre" : "post";
    printf("Executing %s-event \"%s\"\n", prePostText, exprs_event_names[event]);
}

void exprs_PrintOutput(const char *text, const char *fname) {
    printf("Print @ %s: \"%s\"\n", fname, text);
}

int main(void) {
    exprs_EngineFirstStep();

    exprs_EngineTimeStep(1.0);
    return 0;
}

