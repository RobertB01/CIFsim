package locs_multiple_potential_initial_java;

import static locs_multiple_potential_initial_java.locs_multiple_potential_initial.locs_multiple_potential_initialUtils.*;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/** Test class for the {@link locs_multiple_potential_initial} class. */
@SuppressWarnings("unused")
public class locs_multiple_potential_initialTest extends locs_multiple_potential_initial {
    /**
     * Main method for the {@link locs_multiple_potential_initialTest} class.
     *
     * @param args The command line arguments. Ignored.
     */
    public static void main(String[] args) {
        locs_multiple_potential_initial code = new locs_multiple_potential_initialTest();
        code.exec(100);
    }

    @Override
    protected void updateInputs() {
        // No input variables.
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
