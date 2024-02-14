#!/bin/sh
set -e -u -x

# MAX_STRING_SIZE = (127+1)  default
# Maximum length of string including terminator.

# MAX_NUM_EVENTS = 1000  default
# Maximum number of events to process before deciding it may be an infinite loop.

# PRINT_OUTPUT
# If set, 'print' statements are forwarded to unsupported_simulink_warnings_PrintOutput.

# EVENT_OUTPUT
# If set, performed events are reported to unsupported_simulink_warnings_EventOutput

# CHECK_RANGES (recommended to set to 1)
# If set, enable checking of integer in partly overlapping ranged integers (as defined by CIF).

# KEEP_RUNNING
# If set, do not abort on range errors and bad continuous variable updates.
# Note that most other checks are in assert() which are not controlled by this flag.

COMPILE_OPTIONS="-DPRINT_OUTPUT=1 -DEVENT_OUTPUT=1 -DCHECK_RANGES=1"
CFLAGS="-Wall -std=c89 -g $COMPILE_OPTIONS"
LFLAGS="-lm"

gcc $CFLAGS -c -o unsupported_simulink_warnings_library.o unsupported_simulink_warnings_library.c
gcc $CFLAGS -c -o unsupported_simulink_warnings_engine.o unsupported_simulink_warnings_engine.c
gcc $CFLAGS -c -o unsupported_simulink_warnings_test_code.o unsupported_simulink_warnings_test_code.c
gcc $CFLAGS -o unsupported_simulink_warnings_engine_test unsupported_simulink_warnings_test_code.o unsupported_simulink_warnings_engine.o unsupported_simulink_warnings_library.o $LFLAGS