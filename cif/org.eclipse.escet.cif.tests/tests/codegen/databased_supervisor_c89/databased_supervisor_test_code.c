/* Additional code to allow compilation and test of the generated code.
 *
 * This file is generated, DO NOT EDIT
 */

#include <stdio.h>
#include "databased_supervisor_engine.h"

/* Assign values to the input variables. */
void databased_supervisor_AssignInputVariables(void) {

}

void databased_supervisor_InfoEvent(databased_supervisor_Event_ event, BoolType pre) {
    const char *prePostText = pre ? "pre" : "post";
    printf("Executing %s-event \"%s\"\n", prePostText, databased_supervisor_event_names[event]);
}

void databased_supervisor_PrintOutput(const char *text, const char *fname) {
    printf("Print @ %s: \"%s\"\n", fname, text);
}

int main(void) {
    databased_supervisor_EngineFirstStep();

    databased_supervisor_EngineTimeStep(1.0);
    return 0;
}

