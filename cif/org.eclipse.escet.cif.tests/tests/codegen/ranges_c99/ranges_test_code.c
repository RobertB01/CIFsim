/* Additional code to allow compilation and test of the generated code.
 *
 * This file is generated, DO NOT EDIT
 */

#include <stdio.h>
#include "ranges_engine.h"

/* Assign values to the input variables. */
void ranges_AssignInputVariables(void) {

}

void ranges_InfoEvent(ranges_Event_ event, BoolType pre) {
    const char *prePostText = pre ? "pre" : "post";
    printf("Executing %s-event \"%s\"\n", prePostText, ranges_event_names[event]);
}

void ranges_PrintOutput(const char *text, const char *fname) {
    printf("Print @ %s: \"%s\"\n", fname, text);
}

int main(void) {
    ranges_EngineFirstStep();

    ranges_EngineTimeStep(1.0);
    return 0;
}

