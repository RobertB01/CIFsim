/* Additional code to allow compilation and test of the generated code.
 *
 * This file is generated, DO NOT EDIT
 */

#include <stdio.h>
#include "annos_doc_engine.h"

/* Assign values to the input variables. */
void annos_doc_AssignInputVariables(void) {
    /* Input variable "i1". */
    i1_ = FALSE;

    /* Input variable "i2". */
    i2_ = FALSE;

    /* Input variable "i3". */
    i3_ = FALSE;

    /* Input variable "i4". */
    i4_ = FALSE;

    /* Input variable "i5". */
    i5_ = FALSE;
}

void annos_doc_InfoEvent(annos_doc_Event_ event, BoolType pre) {
    const char *prePostText = pre ? "pre" : "post";
    printf("Executing %s-event \"%s\"\n", prePostText, annos_doc_event_names[event]);
}

void annos_doc_PrintOutput(const char *text, const char *fname) {
    printf("Print @ %s: \"%s\"\n", fname, text);
}

int main(void) {
    annos_doc_EngineFirstStep();

    annos_doc_EngineTimeStep(1.0);
    return 0;
}

