package declarations_java;

import static declarations_java.declarations.declarationsUtils.*;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/** Test class for the {@link declarations} class. */
@SuppressWarnings("unused")
public class declarationsTest extends declarations {
    /**
     * Main method for the {@link declarationsTest} class.
     *
     * @param args The command line arguments. Ignored.
     */
    public static void main(String[] args) {
        declarations code = new declarationsTest();
        code.exec(100);
    }

    @Override
    protected void updateInputs() {
        // Assign default values to the inputs, for testing.
        i1_ = 0;
        i2_ = 0.0;
        i3_ = new CifTuple_T3IIR(0, 0, 0.0);
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
