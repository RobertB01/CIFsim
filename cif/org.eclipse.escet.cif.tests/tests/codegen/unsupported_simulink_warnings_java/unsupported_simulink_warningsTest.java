package unsupported_simulink_warnings_java;

import static unsupported_simulink_warnings_java.unsupported_simulink_warnings.unsupported_simulink_warningsUtils.*;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/** Test class for the {@link unsupported_simulink_warnings} class. */
@SuppressWarnings("unused")
public class unsupported_simulink_warningsTest extends unsupported_simulink_warnings {
    /**
     * Main method for the {@link unsupported_simulink_warningsTest} class.
     *
     * @param args The command line arguments. Ignored.
     */
    public static void main(String[] args) {
        unsupported_simulink_warnings code = new unsupported_simulink_warningsTest();
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
