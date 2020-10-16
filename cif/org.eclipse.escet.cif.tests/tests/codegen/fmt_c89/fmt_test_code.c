/* Additional code to allow compilation and test of the generated code.
 *
 * This file is generated, DO NOT EDIT
 */

#include <stdio.h>
#include "fmt_engine.h"

/* Assign values to the input variables. */
void fmt_AssignInputVariables(void) {
    /* Input variable "b". */
    b_ = FALSE;

    /* Input variable "i". */
    i_ = 0;

    /* Input variable "r". */
    r_ = 0.0;
}

void fmt_InfoEvent(fmt_Event_ event, BoolType pre) {
    const char *prePostText = pre ? "pre" : "post";
    printf("Executing %s-event \"%s\"\n", prePostText, fmt_event_names[event]);
}

void fmt_PrintOutput(const char *text, const char *fname) {
    printf("Print @ %s: \"%s\"\n", fname, text);
}

int main(void) {
    fmt_EngineFirstStep();

    fmt_EngineTimeStep(1.0);
    return 0;
}

