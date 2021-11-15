package svg_input_decl_removed_java;

import static svg_input_decl_removed_java.svg_input_decl_removed.svg_input_decl_removedUtils.*;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/** Test class for the {@link svg_input_decl_removed} class. */
@SuppressWarnings("unused")
public class svg_input_decl_removedTest extends svg_input_decl_removed {
    /**
     * Main method for the {@link svg_input_decl_removedTest} class.
     *
     * @param args The command line arguments. Ignored.
     */
    public static void main(String[] args) {
        svg_input_decl_removed code = new svg_input_decl_removedTest();
        code.exec(100);
    }

    @Override
    protected void updateInputs() {
        // Assign default values to the inputs, for testing.
        x_ = false;
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
