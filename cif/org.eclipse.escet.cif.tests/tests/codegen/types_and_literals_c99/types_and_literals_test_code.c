/* Additional code to allow compilation and test of the generated code.
 *
 * This file is generated, DO NOT EDIT
 */

#include <stdio.h>
#include "types_and_literals_engine.h"

/* Assign values to the input variables. */
void types_and_literals_AssignInputVariables(void) {

}

void types_and_literals_InfoEvent(types_and_literals_Event_ event, BoolType pre) {
    const char *prePostText = pre ? "pre" : "post";
    printf("Executing %s-event \"%s\"\n", prePostText, types_and_literals_event_names[event]);
}

void types_and_literals_PrintOutput(const char *text, const char *fname) {
    printf("Print @ %s: \"%s\"\n", fname, text);
}

int main(void) {
    types_and_literals_EngineFirstStep();

    types_and_literals_EngineTimeStep(1.0);
    return 0;
}

