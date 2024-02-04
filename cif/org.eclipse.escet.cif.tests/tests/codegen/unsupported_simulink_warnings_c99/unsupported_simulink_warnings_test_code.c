/* Additional code to allow compilation and test of the generated code.
 *
 * This file is generated, DO NOT EDIT
 */

#include <stdio.h>
#include "unsupported_simulink_warnings_engine.h"

/* Assign values to the input variables. */
void unsupported_simulink_warnings_AssignInputVariables(void) {

}

void unsupported_simulink_warnings_InfoEvent(unsupported_simulink_warnings_Event_ event, BoolType pre) {
    const char *prePostText = pre ? "pre" : "post";
    printf("Executing %s-event \"%s\"\n", prePostText, unsupported_simulink_warnings_event_names[event]);
}

void unsupported_simulink_warnings_PrintOutput(const char *text, const char *fname) {
    printf("Print @ %s: \"%s\"\n", fname, text);
}

int main(void) {
    unsupported_simulink_warnings_EngineFirstStep();

    unsupported_simulink_warnings_EngineTimeStep(1.0);
    return 0;
}

