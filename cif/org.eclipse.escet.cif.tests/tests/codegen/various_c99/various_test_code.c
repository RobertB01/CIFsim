/* Additional code to allow compilation and test of the generated code.
 *
 * This file is generated, DO NOT EDIT
 */

#include <stdio.h>
#include "various_engine.h"

/* Assign values to the input variables. */
void various_AssignInputVariables(void) {
    /* Input variable "x". */
    x_ = 0;

    /* Input variable "y". */
    y_ = 0;

    /* Input variable "input_li". */
    (input_li_).data[0] = 0;
    (input_li_).data[1] = 0;
    (input_li_).data[2] = 0;
}

void various_InfoEvent(various_Event_ event, BoolType pre) {
    const char *prePostText = pre ? "pre" : "post";
    printf("Executing %s-event \"%s\"\n", prePostText, various_event_names[event]);
}

void various_PrintOutput(const char *text, const char *fname) {
    printf("Print @ %s: \"%s\"\n", fname, text);
}

int main(void) {
    various_EngineFirstStep();

    various_EngineTimeStep(1.0);
    return 0;
}

