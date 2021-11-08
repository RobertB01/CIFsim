/* Additional code to allow compilation and test of the generated code.
 *
 * This file is generated, DO NOT EDIT
 */

#include <stdio.h>
#include "svg_input_decl_removed_engine.h"

/* Assign values to the input variables. */
void svg_input_decl_removed_AssignInputVariables(void) {
    /* Input variable "x". */
    x_ = FALSE;
}

void svg_input_decl_removed_InfoEvent(svg_input_decl_removed_Event_ event, BoolType pre) {
    const char *prePostText = pre ? "pre" : "post";
    printf("Executing %s-event \"%s\"\n", prePostText, svg_input_decl_removed_event_names[event]);
}

void svg_input_decl_removed_PrintOutput(const char *text, const char *fname) {
    printf("Print @ %s: \"%s\"\n", fname, text);
}

int main(void) {
    svg_input_decl_removed_EngineFirstStep();

    svg_input_decl_removed_EngineTimeStep(1.0);
    return 0;
}

