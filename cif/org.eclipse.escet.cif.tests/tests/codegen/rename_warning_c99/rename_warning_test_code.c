/* Additional code to allow compilation and test of the generated code.
 *
 * This file is generated, DO NOT EDIT
 */

#include <stdio.h>
#include "rename_warning_engine.h"

/* Assign values to the input variables. */
void rename_warning_AssignInputVariables(void) {

}

void rename_warning_InfoEvent(rename_warning_Event_ event, BoolType pre) {
    const char *prePostText = pre ? "pre" : "post";
    printf("Executing %s-event \"%s\"\n", prePostText, rename_warning_event_names[event]);
}

void rename_warning_PrintOutput(const char *text, const char *fname) {
    printf("Print @ %s: \"%s\"\n", fname, text);
}

int main(void) {
    rename_warning_EngineFirstStep();

    rename_warning_EngineTimeStep(1.0);
    return 0;
}

