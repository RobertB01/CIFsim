/* Additional code to allow compilation and test of the generated code.
 *
 * This file is generated, DO NOT EDIT
 */

#include <stdio.h>
#include "declarations_engine.h"

/* Assign values to the input variables. */
void declarations_AssignInputVariables(void) {
    /* Input variable "i1". */
    i1_ = 0;

    /* Input variable "i2". */
    i2_ = 0.0;

    /* Input variable "i3". */
    (i3_)._field0 = 0;
    (i3_)._field1 = 0;
    (i3_)._field2 = 0.0;
}

void declarations_InfoEvent(declarations_Event_ event, BoolType pre) {
    const char *prePostText = pre ? "pre" : "post";
    printf("Executing %s-event \"%s\"\n", prePostText, declarations_event_names[event]);
}

void declarations_PrintOutput(const char *text, const char *fname) {
    printf("Print @ %s: \"%s\"\n", fname, text);
}

int main(void) {
    declarations_EngineFirstStep();

    declarations_EngineTimeStep(1.0);
    return 0;
}

