/* Additional code to allow compilation and test of the generated code.
 *
 * This file is generated, DO NOT EDIT
 */

#include <stdio.h>
#include "internal_functions_engine.h"

/* Assign values to the input variables. */
void internal_functions_AssignInputVariables(void) {

}

void internal_functions_InfoEvent(internal_functions_Event_ event, BoolType pre) {
    const char *prePostText = pre ? "pre" : "post";
    printf("Executing %s-event \"%s\"\n", prePostText, internal_functions_event_names[event]);
}

void internal_functions_PrintOutput(const char *text, const char *fname) {
    printf("Print @ %s: \"%s\"\n", fname, text);
}

int main(void) {
    internal_functions_EngineFirstStep();

    internal_functions_EngineTimeStep(1.0);
    return 0;
}

