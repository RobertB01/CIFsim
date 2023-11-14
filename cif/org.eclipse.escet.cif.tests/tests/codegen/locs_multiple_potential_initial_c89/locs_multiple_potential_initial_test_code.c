/* Additional code to allow compilation and test of the generated code.
 *
 * This file is generated, DO NOT EDIT
 */

#include <stdio.h>
#include "locs_multiple_potential_initial_engine.h"

/* Assign values to the input variables. */
void locs_multiple_potential_initial_AssignInputVariables(void) {

}

void locs_multiple_potential_initial_InfoEvent(locs_multiple_potential_initial_Event_ event, BoolType pre) {
    const char *prePostText = pre ? "pre" : "post";
    printf("Executing %s-event \"%s\"\n", prePostText, locs_multiple_potential_initial_event_names[event]);
}

void locs_multiple_potential_initial_PrintOutput(const char *text, const char *fname) {
    printf("Print @ %s: \"%s\"\n", fname, text);
}

int main(void) {
    locs_multiple_potential_initial_EngineFirstStep();

    locs_multiple_potential_initial_EngineTimeStep(1.0);
    return 0;
}

