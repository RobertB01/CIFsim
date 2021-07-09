/* Additional code to allow compilation and test of the generated code.
 *
 * This file is generated, DO NOT EDIT
 */

#include <stdio.h>
#include "state_event_exclusion_invariants_engine.h"

/* Assign values to the input variables. */
void state_event_exclusion_invariants_AssignInputVariables(void) {
    /* Input variable "x". */
    x_ = 0;
}

void state_event_exclusion_invariants_InfoEvent(state_event_exclusion_invariants_Event_ event, BoolType pre) {
    const char *prePostText = pre ? "pre" : "post";
    printf("Executing %s-event \"%s\"\n", prePostText, state_event_exclusion_invariants_event_names[event]);
}

void state_event_exclusion_invariants_PrintOutput(const char *text, const char *fname) {
    printf("Print @ %s: \"%s\"\n", fname, text);
}

int main(void) {
    state_event_exclusion_invariants_EngineFirstStep();

    state_event_exclusion_invariants_EngineTimeStep(1.0);
    return 0;
}

