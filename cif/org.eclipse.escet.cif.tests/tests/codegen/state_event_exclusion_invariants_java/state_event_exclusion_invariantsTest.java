package state_event_exclusion_invariants_java;

import static state_event_exclusion_invariants_java.state_event_exclusion_invariants.state_event_exclusion_invariantsUtils.*;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/** Test class for the {@link state_event_exclusion_invariants} class. */
@SuppressWarnings("unused")
public class state_event_exclusion_invariantsTest extends state_event_exclusion_invariants {
    /**
     * Main method for the {@link state_event_exclusion_invariantsTest} class.
     *
     * @param args The command line arguments. Ignored.
     */
    public static void main(String[] args) {
        state_event_exclusion_invariants code = new state_event_exclusion_invariantsTest();
        code.exec(100);
    }

    @Override
    protected void updateInputs() {
        // Assign default values to the inputs, for testing.
        x_ = 0;
    }

    @Override
    protected void infoExec(long duration, long cycleTime) {
        if (duration > cycleTime) {
            String text = fmt("Cycle time exceeded: %,d ns > %,d ns.", duration, cycleTime);
            System.err.println(text);
        }
    }

    @Override
    protected void infoEvent(int idx, boolean pre) {
        // Print the executed event, for testing.
        if (pre) {
            System.out.println("EVENT pre: " + getEventName(idx));
        } else {
            System.out.println("EVENT post: " + getEventName(idx));
        }
    }

    @Override
    protected void infoPrintOutput(String text, String target) {
        if (target.equals(":stdout")) {
            System.out.println(text);
            System.out.flush();

        } else if (target.equals(":stderr")) {
            System.err.println(text);
            System.err.flush();

        } else {
            System.out.println(normalizePrintTarget(target) + ": " + text);
            System.out.flush();
        }
    }

    @Override
    protected void preExec() {
        // Ignore.
    }

    @Override
    protected void postExec() {
        // Could for instance write outputs to the environment.
    }
}
