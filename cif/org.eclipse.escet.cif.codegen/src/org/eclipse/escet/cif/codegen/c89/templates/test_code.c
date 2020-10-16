/* Additional code to allow compilation and test of the generated code.
 *
 * This file is generated, DO NOT EDIT
 */

#include <stdio.h>
#include "${prefix}_engine.h"

/* Assign values to the input variables. */
void ${prefix}_AssignInputVariables(void) {
${input-vars-test-inputvalues}
}

void ${prefix}_InfoEvent(${prefix}_Event_ event, BoolType pre) {
    const char *prePostText = pre ? "pre" : "post";
    printf("Executing %s-event \"%s\"\n", prePostText, ${prefix}_event_names[event]);
}

void ${prefix}_PrintOutput(const char *text, const char *fname) {
    printf("Print @ %s: \"%s\"\n", fname, text);
}

int main(void) {
    ${prefix}_EngineFirstStep();

    ${prefix}_EngineTimeStep(1.0);
    return 0;
}

