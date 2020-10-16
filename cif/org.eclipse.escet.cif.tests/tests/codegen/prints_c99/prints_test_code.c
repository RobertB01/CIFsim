/* Additional code to allow compilation and test of the generated code.
 *
 * This file is generated, DO NOT EDIT
 */

#include <stdio.h>
#include "prints_engine.h"

/* Assign values to the input variables. */
void prints_AssignInputVariables(void) {

}

void prints_InfoEvent(prints_Event_ event, BoolType pre) {
    const char *prePostText = pre ? "pre" : "post";
    printf("Executing %s-event \"%s\"\n", prePostText, prints_event_names[event]);
}

void prints_PrintOutput(const char *text, const char *fname) {
    printf("Print @ %s: \"%s\"\n", fname, text);
}

int main(void) {
    prints_EngineFirstStep();

    prints_EngineTimeStep(1.0);
    return 0;
}

