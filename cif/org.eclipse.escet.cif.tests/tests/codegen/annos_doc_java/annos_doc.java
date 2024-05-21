package annos_doc_java;

import static annos_doc_java.annos_doc.annos_docUtils.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * annos_doc code generated from a CIF specification.
 *
 * <p>
 * First doc
 * with multiple lines.
 * </p>
 *
 * <p>
 * Second doc line 1.
 * Second doc line 2.
 * </p>
 */
@SuppressWarnings("unused")
public abstract class annos_doc {
    /** Should execution timing information be provided? */
    public boolean doInfoExec = true;

    /** Should executed event information be provided? */
    public boolean doInfoEvent = false;

    /** Should print output be provided? */
    public boolean doInfoPrintOutput = true;

    /** Whether this is the first time the code is (to be) executed. */
    protected boolean firstExec;

    /** The names of all the events, except for event 'tau'. */
    private final String[] EVENT_NAMES = {
        "events.e1",
        "events.e2",
        "events.e3",
        "events.e4",
        "events.e5",
    };

    /** Constant "constants.c1". */
    public static final int constants_c1_ = 1;

    /**
     * Constant "constants.c2".
     *
     * <p>
     * single line doc
     * </p>
     */
    public static final int constants_c2_ = 2;

    /**
     * Constant "constants.c3".
     *
     * <p>
     * doc with multiple
     * lines of
     *  text
     * </p>
     */
    public static final int constants_c3_ = 3;

    /**
     * Constant "constants.c4".
     *
     * <p>
     * some doc
     * </p>
     */
    public static final int constants_c4_ = 4;

    /**
     * Constant "constants.c5".
     *
     * <p>
     * First doc.
     * </p>
     *
     * <p>
     * Second doc line 1.
     * Second doc line 2.
     * </p>
     */
    public static final int constants_c5_ = 5;

    /** Variable 'time'. */
    public double time;

    /** Continuous variable "contvars.c1". */
    public double contvars_c1_;

    /**
     * Continuous variable "contvars.c2".
     *
     * <p>
     * single line doc
     * </p>
     */
    public double contvars_c2_;

    /**
     * Continuous variable "contvars.c3".
     *
     * <p>
     * doc with multiple
     * lines of
     *  text
     * </p>
     */
    public double contvars_c3_;

    /**
     * Continuous variable "contvars.c4".
     *
     * <p>
     * some doc
     * </p>
     */
    public double contvars_c4_;

    /**
     * Continuous variable "contvars.c5".
     *
     * <p>
     * First doc.
     * </p>
     *
     * <p>
     * Second doc line 1.
     * Second doc line 2.
     * </p>
     */
    public double contvars_c5_;

    /** Discrete variable "discvars.d1". */
    public boolean discvars_d1_;

    /**
     * Discrete variable "discvars.d2".
     *
     * <p>
     * single line doc
     * </p>
     */
    public boolean discvars_d2_;

    /**
     * Discrete variable "discvars.d3".
     *
     * <p>
     * doc with multiple
     * lines of
     *  text
     * </p>
     */
    public boolean discvars_d3_;

    /**
     * Discrete variable "discvars.d4".
     *
     * <p>
     * some doc
     * </p>
     */
    public boolean discvars_d4_;

    /**
     * Discrete variable "discvars.d5".
     *
     * <p>
     * First doc.
     * </p>
     *
     * <p>
     * Second doc line 1.
     * Second doc line 2.
     * </p>
     */
    public boolean discvars_d5_;

    /** Input variable "i1". */
    public boolean i1_;

    /**
     * Input variable "i2".
     *
     * <p>
     * single line doc
     * </p>
     */
    public boolean i2_;

    /**
     * Input variable "i3".
     *
     * <p>
     * doc with multiple
     * lines of
     *  text
     * </p>
     */
    public boolean i3_;

    /**
     * Input variable "i4".
     *
     * <p>
     * some doc
     * </p>
     */
    public boolean i4_;

    /**
     * Input variable "i5".
     *
     * <p>
     * First doc.
     * </p>
     *
     * <p>
     * Second doc line 1.
     * Second doc line 2.
     * </p>
     */
    public boolean i5_;

    /**
     * Input variable "preconditions.i".
     *
     * <p>
     * {}
     * </p>
     */
    public boolean preconditions_i_;

    /** Constructor for the {@link annos_doc} class. */
    public annos_doc() {
        firstExec = true;
        this.time = 0.0;
    }

    /**
     * Execute the code once. Inputs are read, transitions are executed until
     * none are possible, outputs are written, etc.
     *
     * @param newTime The time in seconds, since the start of the first
     *      execution.
     * @throws annos_docException In case of a runtime error caused by code
     *      generated from the CIF model.
     */
    public void execOnce(double newTime) {
        // Pre execution notification.
        preExec();

        // Update values of input variables.
        updateInputs();

        // Initialize the state.
        if (firstExec) {
            initState();
        }

        // Calculate time delta.
        double delta = newTime - time;

        // Update values of continuous variables.
        if (!firstExec) {
            double deriv0 = contvars_c1_deriv();
            double deriv1 = contvars_c2_deriv();
            double deriv2 = contvars_c3_deriv();
            double deriv3 = contvars_c4_deriv();
            double deriv4 = contvars_c5_deriv();

            contvars_c1_ = contvars_c1_ + delta * deriv0;
            checkDouble(contvars_c1_, "contvars.c1");
            if (contvars_c1_ == -0.0) contvars_c1_ = 0.0;
            contvars_c2_ = contvars_c2_ + delta * deriv1;
            checkDouble(contvars_c2_, "contvars.c2");
            if (contvars_c2_ == -0.0) contvars_c2_ = 0.0;
            contvars_c3_ = contvars_c3_ + delta * deriv2;
            checkDouble(contvars_c3_, "contvars.c3");
            if (contvars_c3_ == -0.0) contvars_c3_ = 0.0;
            contvars_c4_ = contvars_c4_ + delta * deriv3;
            checkDouble(contvars_c4_, "contvars.c4");
            if (contvars_c4_ == -0.0) contvars_c4_ = 0.0;
            contvars_c5_ = contvars_c5_ + delta * deriv4;
            checkDouble(contvars_c5_, "contvars.c5");
            if (contvars_c5_ == -0.0) contvars_c5_ = 0.0;
        }

        // Update time.
        time = newTime;

        // Apply print declarations.
        if (firstExec) {
            // For 'initial' transition.
            if (doInfoPrintOutput) printOutput(-3, true);
            if (doInfoPrintOutput) printOutput(-3, false);

        } else {
            // For 'post' of time transition.
            if (doInfoPrintOutput) printOutput(-2, false);
        }

        // Execute events as long as they are possible.
        while (true) {
            // Event "events.e1".
            if (execEvent0()) continue;

            // Event "events.e2".
            if (execEvent1()) continue;

            // Event "events.e3".
            if (execEvent2()) continue;

            // Event "events.e4".
            if (execEvent3()) continue;

            // Event "events.e5".
            if (execEvent4()) continue;

            // Event "tau".
            if (execEvent5()) continue;

            // Event "tau".
            if (execEvent6()) continue;

            break;
        }

        // Apply print declarations for 'pre' of time transition.
        if (doInfoPrintOutput) printOutput(-2, true);

        // Post execution notification.
        postExec();

        // Done.
        firstExec = false;
    }

    /**
     * Repeatedly {@link #execOnce executes the code}.
     *
     * @param frequency The frequency in times per second, that the code should
     *      be executed (if positive), or execute as fast as possible, that is
     *      as many times per second as possible (if negative or zero).
     * @throws annos_docException In case of a runtime error caused by code
     *      generated from the CIF model.
     */
    public void exec(long frequency) {
        // Initialize.
        boolean first = true;
        long cycleNano = (frequency <= 0) ? -1 : 1000 * 1000 * 1000 / frequency;
        long startNano = System.nanoTime();
        long targetNano = startNano;

        // Execute repeatedly.
        while (true) {
            // Pre execution timing.
            long preNano = first ? startNano : System.nanoTime();
            first = false;
            long timeNano = preNano - startNano;
            double time = timeNano / 1e9;

            // Execute once.
            execOnce(time);

            // Post execution timing.
            long postNano = System.nanoTime();
            long duration = postNano - preNano;
            if (doInfoExec) infoExec(duration, cycleNano);

            // Ensure frequency.
            if (frequency > 0) {
                targetNano += cycleNano;
                if (postNano < targetNano) {
                    try {
                        long remainderNano = targetNano - postNano;
                        long remainderMilli = remainderNano / 1000 / 1000;
                        Thread.sleep(remainderMilli);

                    } catch (InterruptedException ex) {
                        throw new RuntimeException("Thread interrupted.", ex);
                    }
                }
            }
        }
    }

    /**
     * Execute code for event "events.e1".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent0() {
        if (doInfoPrintOutput) printOutput(0, true);
        if (doInfoEvent) infoEvent(0, true);


        if (doInfoEvent) infoEvent(0, false);
        if (doInfoPrintOutput) printOutput(0, false);
        return true;
    }

    /**
     * Execute code for event "events.e2".
     *
     * <p>
     * single line doc
     * </p>
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent1() {
        if (doInfoPrintOutput) printOutput(1, true);
        if (doInfoEvent) infoEvent(1, true);


        if (doInfoEvent) infoEvent(1, false);
        if (doInfoPrintOutput) printOutput(1, false);
        return true;
    }

    /**
     * Execute code for event "events.e3".
     *
     * <p>
     * doc with multiple
     * lines of
     *  text
     * </p>
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent2() {
        if (doInfoPrintOutput) printOutput(2, true);
        if (doInfoEvent) infoEvent(2, true);


        if (doInfoEvent) infoEvent(2, false);
        if (doInfoPrintOutput) printOutput(2, false);
        return true;
    }

    /**
     * Execute code for event "events.e4".
     *
     * <p>
     * some doc
     * </p>
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent3() {
        if (doInfoPrintOutput) printOutput(3, true);
        if (doInfoEvent) infoEvent(3, true);


        if (doInfoEvent) infoEvent(3, false);
        if (doInfoPrintOutput) printOutput(3, false);
        return true;
    }

    /**
     * Execute code for event "events.e5".
     *
     * <p>
     * First doc.
     * </p>
     *
     * <p>
     * Second doc line 1.
     * Second doc line 2.
     * </p>
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent4() {
        if (doInfoPrintOutput) printOutput(4, true);
        if (doInfoEvent) infoEvent(4, true);


        if (doInfoEvent) infoEvent(4, false);
        if (doInfoPrintOutput) printOutput(4, false);
        return true;
    }

    /**
     * Execute code for event "tau".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent5() {
        boolean guard = (((((contvars_c1_) > (0)) || ((contvars_c2_) > (0))) || ((contvars_c3_) > (0))) || ((contvars_c4_) > (0))) || ((contvars_c5_) > (0));
        if (!guard) return false;

        if (doInfoPrintOutput) printOutput(-1, true);
        if (doInfoEvent) infoEvent(-1, true);


        if (doInfoEvent) infoEvent(-1, false);
        if (doInfoPrintOutput) printOutput(-1, false);
        return true;
    }

    /**
     * Execute code for event "tau".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent6() {
        boolean guard = ((((discvars_d1_) || (discvars_d2_)) || (discvars_d3_)) || (discvars_d4_)) || (discvars_d5_);
        if (!guard) return false;

        if (doInfoPrintOutput) printOutput(-1, true);
        if (doInfoEvent) infoEvent(-1, true);


        if (doInfoEvent) infoEvent(-1, false);
        if (doInfoPrintOutput) printOutput(-1, false);
        return true;
    }

    /** Initializes the state. */
    private void initState() {
        contvars_c1_ = 0.0;
        contvars_c2_ = 0.0;
        contvars_c3_ = 0.0;
        contvars_c4_ = 0.0;
        contvars_c5_ = 0.0;
        discvars_d1_ = false;
        discvars_d2_ = false;
        discvars_d3_ = false;
        discvars_d4_ = false;
        discvars_d5_ = false;
    }

    /**
     * Updates the values of the input variables. Other variables from the
     * state may not be accessed or modified.
     */
    protected abstract void updateInputs();

    /**
     * Informs about the duration of a single execution.
     *
     * @param duration The duration of the execution, in nanoseconds.
     * @param cycleTime The desired maximum duration of the execution, in
     *      nanoseconds, or {@code -1} if not available.
     */
    protected abstract void infoExec(long duration, long cycleTime);

    /**
     * Informs that an event will be or has been executed.
     *
     * @param idx The 0-based index of the event, or {@code -1} for 'tau'.
     * @param pre Whether the event will be executed ({@code true}) or has
     *      been executed ({@code false}).
     */
    protected abstract void infoEvent(int idx, boolean pre);

    /**
     * Informs that the code is about to be executed. For the
     * {@link #firstExec} the state has not yet been initialized, except for
     * {@link #time}.
     */
    protected abstract void preExec();

    /** Informs that the code was just executed. */
    protected abstract void postExec();

    /**
     * Returns the name of an event.
     *
     * @param idx The 0-based index of the event, or {@code -1} for 'tau'.
     * @return The name of the event.
     */
    protected String getEventName(int idx) {
        if (idx == -1) return "tau";
        return EVENT_NAMES[idx];
    }

    /**
     * Evaluates algebraic variable "algvars.a1".
     *
     * @return The evaluation result.
     */
    public int algvars_a1_() {
        return 1;
    }

    /**
     * Evaluates algebraic variable "algvars.a2".
     *
     * <p>
     * single line doc
     * </p>
     *
     * @return The evaluation result.
     */
    public int algvars_a2_() {
        return 2;
    }

    /**
     * Evaluates algebraic variable "algvars.a3".
     *
     * <p>
     * doc with multiple
     * lines of
     *  text
     * </p>
     *
     * @return The evaluation result.
     */
    public int algvars_a3_() {
        return 3;
    }

    /**
     * Evaluates algebraic variable "algvars.a4".
     *
     * <p>
     * some doc
     * </p>
     *
     * @return The evaluation result.
     */
    public int algvars_a4_() {
        return 4;
    }

    /**
     * Evaluates algebraic variable "algvars.a5".
     *
     * <p>
     * First doc.
     * </p>
     *
     * <p>
     * Second doc line 1.
     * Second doc line 2.
     * </p>
     *
     * @return The evaluation result.
     */
    public int algvars_a5_() {
        return 5;
    }

    /**
     * Evaluates derivative of continuous variable "contvars.c1".
     *
     * @return The evaluation result.
     */
    public double contvars_c1_deriv() {
        return 1.0;
    }

    /**
     * Evaluates derivative of continuous variable "contvars.c2".
     *
     * @return The evaluation result.
     */
    public double contvars_c2_deriv() {
        return 2.0;
    }

    /**
     * Evaluates derivative of continuous variable "contvars.c3".
     *
     * @return The evaluation result.
     */
    public double contvars_c3_deriv() {
        return 3.0;
    }

    /**
     * Evaluates derivative of continuous variable "contvars.c4".
     *
     * @return The evaluation result.
     */
    public double contvars_c4_deriv() {
        return 4.0;
    }

    /**
     * Evaluates derivative of continuous variable "contvars.c5".
     *
     * @return The evaluation result.
     */
    public double contvars_c5_deriv() {
        return 5.0;
    }

    /**
     * Function "funcs.func1".
     *
     * @param funcs_func1_p_ Function parameter "funcs.func1.p".
     * @return The return value of the function.
     */
    public static boolean funcs_func1_(boolean funcs_func1_p_) {
        // Variable "funcs.func1.v1".
        boolean funcs_func1_v1_ = funcs_func1_p_;

        // Variable "funcs.func1.v2".
        boolean funcs_func1_v2_ = funcs_func1_v1_;

        // Execute statements in the function body.
        if (true) return funcs_func1_v2_;
        throw new RuntimeException("no return at end of func");
    }

    /**
     * Function "funcs.func2".
     *
     * <p>
     * single line doc
     * </p>
     *
     * @param funcs_func2_p_ Function parameter "funcs.func2.p".
     *     <p>
     *     single line doc
     *     </p>
     * @return The return value of the function.
     */
    public static boolean funcs_func2_(boolean funcs_func2_p_) {
        // Variable "funcs.func2.v1".
        //
        // single line doc
        boolean funcs_func2_v1_ = funcs_func2_p_;

        // Variable "funcs.func2.v2".
        //
        // single line doc
        boolean funcs_func2_v2_ = funcs_func2_v1_;

        // Execute statements in the function body.
        if (true) return funcs_func2_v2_;
        throw new RuntimeException("no return at end of func");
    }

    /**
     * Function "funcs.func3".
     *
     * <p>
     * doc with multiple
     * lines of
     *  text
     * </p>
     *
     * @param funcs_func3_p_ Function parameter "funcs.func3.p".
     *     <p>
     *     doc with multiple
     *     lines of
     *      text
     *     </p>
     * @return The return value of the function.
     */
    public static boolean funcs_func3_(boolean funcs_func3_p_) {
        // Variable "funcs.func3.v1".
        //
        // doc with multiple
        // lines of
        //  text
        boolean funcs_func3_v1_ = funcs_func3_p_;

        // Variable "funcs.func3.v2".
        //
        // doc with multiple
        // lines of
        //  text
        boolean funcs_func3_v2_ = funcs_func3_v1_;

        // Execute statements in the function body.
        if (true) return funcs_func3_v2_;
        throw new RuntimeException("no return at end of func");
    }

    /**
     * Function "funcs.func4".
     *
     * <p>
     * some doc
     * </p>
     *
     * @param funcs_func4_p_ Function parameter "funcs.func4.p".
     *     <p>
     *     some doc
     *     </p>
     * @return The return value of the function.
     */
    public static boolean funcs_func4_(boolean funcs_func4_p_) {
        // Variable "funcs.func4.v1".
        //
        // some doc
        boolean funcs_func4_v1_ = funcs_func4_p_;

        // Variable "funcs.func4.v2".
        //
        // some doc
        boolean funcs_func4_v2_ = funcs_func4_v1_;

        // Execute statements in the function body.
        if (true) return funcs_func4_v2_;
        throw new RuntimeException("no return at end of func");
    }

    /**
     * Function "funcs.func5".
     *
     * <p>
     * First doc.
     * </p>
     *
     * <p>
     * Second doc line 1.
     * Second doc line 2.
     * </p>
     *
     * @param funcs_func5_p_ Function parameter "funcs.func5.p".
     *     <p>
     *     First doc.
     *     </p>
     *     <p>
     *     Second doc line 1.
     *     Second doc line 2.
     *     </p>
     * @return The return value of the function.
     */
    public static boolean funcs_func5_(boolean funcs_func5_p_) {
        // Variable "funcs.func5.v1".
        //
        // First doc.
        //
        // Second doc line 1.
        // Second doc line 2.
        boolean funcs_func5_v1_ = funcs_func5_p_;

        // Variable "funcs.func5.v2".
        //
        // First doc.
        //
        // Second doc line 1.
        // Second doc line 2.
        boolean funcs_func5_v2_ = funcs_func5_v1_;

        // Execute statements in the function body.
        if (true) return funcs_func5_v2_;
        throw new RuntimeException("no return at end of func");
    }

    /**
     * Interface for CIF tuples.
     *
     * @param <T> The concrete tuple type class.
     */
    public static abstract class CifTupleBase<T extends CifTupleBase<T>> {
        /**
         * Creates a shallow copy of this tuple.
         *
         * @return A shallow copy of this tuple.
         */
        public abstract T copy();

        @Override
        public abstract int hashCode();

        @Override
        public abstract boolean equals(Object other);

        @Override
        public abstract String toString();
    }


    /**
     * Print output for all relevant print declarations.
     *
     * @param idx The 0-based event index of the transition, or {@code -1} for
     *      'tau' transitions, {@code -2} for time transitions, or {@code -3}
     *      for the 'initial' transition.
     * @param pre Whether to print output for the pre/source state of the
     *      transition ({@code true}) or for the post/target state of the
     *      transition ({@code false}).
     */
    private void printOutput(int idx, boolean pre) {
        // No print declarations.
    }

    /**
     * Informs that new print output is available.
     *
     * @param text The text being printed.
     * @param target The file or special target to which text is to be printed.
     *      If printed to a file, an absolute or relative local file system
     *      path is given. Paths may contain both {@code "/"} and {@code "\\"}
     *      as file separators. Use {@link annos_docUtils#normalizePrintTarget}
     *      to normalize the path to use file separators for the current
     *      platform. There are two special targets: {@code ":stdout"} to print
     *      to the standard output stream, and {@code ":stderr"} to print to
     *      the standard error stream.
     */
    protected abstract void infoPrintOutput(String text, String target);

    /** annos_doc enumeration. */
    public static enum annos_docEnum {
        /** Literal "l1". */
        _l1,

        /**
         * Literal "l2".
         *
         * <p>
         * single line doc
         * </p>
         */
        _l2,

        /**
         * Literal "l3".
         *
         * <p>
         * doc with multiple
         * lines of
         *  text
         * </p>
         */
        _l3,

        /**
         * Literal "l4".
         *
         * <p>
         * some doc
         * </p>
         */
        _l4,

        /**
         * Literal "l5".
         *
         * <p>
         * First doc.
         * </p>
         *
         * <p>
         * Second doc line 1.
         * Second doc line 2.
         * </p>
         */
        _l5;

        @Override
        public String toString() {
            return name().substring(1);
        }
    }

    /**
     * annos_doc exception.
     *
     * <p>Indices a runtime error while executing the generated code.</p>
     */
    public static class annos_docException extends RuntimeException {
        /**
         * Constructor for the {@link annos_docException} class.
         *
         * @param message The message describing the exception.
         */
        public annos_docException(String message) {
            super(message);
        }

        /**
         * Constructor for the {@link annos_docException} class.
         *
         * @param message The message describing the exception.
         * @param cause The root cause of the exception.
         */
        public annos_docException(String message, Throwable cause) {
            super(message, cause);
        }

        @Override
        public String toString() {
            return getMessage();
        }
    }

    /** {@link annos_doc} utility code. */
    public static class annos_docUtils {
        /** The file separator for the current platform. */
        private static final String FILE_SEPARATOR = System.getProperty("file.separator");

        /** Constructor for the {@link annos_docUtils} class. */
        private annos_docUtils() {
            // Static class.
        }

        /**
         * Returns the absolute value of an integer number.
         *
         * @param x The integer number.
         * @return {@code abs(x)}.
         * @throws annos_docException If the operation results in integer
         *      overflow.
         */
        public static int abs(int x) {
            if (x == Integer.MIN_VALUE) {
                String msg = fmt("Integer overflow: abs(%d).", x);
                throw new annos_docException(msg);
            }
            return Math.abs(x);
        }

        /**
         * Returns the absolute value of a real number.
         *
         * @param x The real number.
         * @return {@code abs(x)}.
         */
        public static double abs(double x) {
            return Math.abs(x);
        }

        /**
         * Returns the arc cosine of a real number.
         *
         * @param x The real number.
         * @return {@code acos(x)}.
         * @throws annos_docException If the operation results in real overflow,
         *      or {@code NaN}.
         */
        public static double acos(double x) {
            double rslt = Math.acos(x);
            if (Double.isInfinite(rslt)) {
                String msg = fmt("Real overflow: acos(%s).", realToStr(x));
                throw new annos_docException(msg);
            }
            if (Double.isNaN(rslt)) {
                String msg = fmt("Invalid operation: acos(%s).", realToStr(x));
                throw new annos_docException(msg);
            }
            return (rslt == -0.0) ? 0.0 : rslt;
        }

        /**
         * Returns the arc sine of a real number.
         *
         * @param x The real number.
         * @return {@code asin(x)}.
         * @throws annos_docException If the operation results in real overflow,
         *      or {@code NaN}.
         */
        public static double asin(double x) {
            double rslt = Math.asin(x);
            if (Double.isInfinite(rslt)) {
                String msg = fmt("Real overflow: asin(%s).", realToStr(x));
                throw new annos_docException(msg);
            }
            if (Double.isNaN(rslt)) {
                String msg = fmt("Invalid operation: asin(%s).", realToStr(x));
                throw new annos_docException(msg);
            }
            return (rslt == -0.0) ? 0.0 : rslt;
        }

        /**
         * Returns the arc tangent of a real number.
         *
         * @param x The real number.
         * @return {@code atan(x)}.
         * @throws annos_docException If the operation results in real overflow,
         *      or {@code NaN}.
         */
        public static double atan(double x) {
            double rslt = Math.atan(x);
            if (Double.isInfinite(rslt)) {
                String msg = fmt("Real overflow: atan(%s).", realToStr(x));
                throw new annos_docException(msg);
            }
            if (Double.isNaN(rslt)) {
                String msg = fmt("Invalid operation: atan(%s).", realToStr(x));
                throw new annos_docException(msg);
            }
            return (rslt == -0.0) ? 0.0 : rslt;
        }

        /**
         * Returns the addition of two integer numbers.
         *
         * @param x The first integer number.
         * @param y The second integer number.
         * @return {@code x + y}.
         * @throws annos_docException If the operation results in integer
         *      overflow.
         */
        public static int addInt(int x, int y) {
            long rslt = (long)x + (long)y;
            if (Integer.MIN_VALUE <= rslt && rslt <= Integer.MAX_VALUE) {
                return (int)rslt;
            }

            String msg = fmt("Integer overflow: %d + %d.", x, y);
            throw new annos_docException(msg);
        }

        /**
         * Returns the addition of two real numbers.
         *
         * @param x The first real number.
         * @param y The second real number.
         * @return {@code x + y}.
         * @throws annos_docException If the operation results in real overflow.
         */
        public static double addReal(double x, double y) {
            double rslt = x + y;
            if (Double.isInfinite(rslt)) {
                String msg = fmt("Real overflow: %s + %s.",
                                 realToStr(x), realToStr(y));
                throw new annos_docException(msg);
            }
            return (rslt == -0.0) ? 0.0 : rslt;
        }

        /**
         * Returns the addition of two strings.
         *
         * @param x The first string.
         * @param y The second string.
         * @return {@code x + y}.
         */
        public static String addString(String x, String y) {
            return x + y;
        }

        /**
         * Converts a Java {@link Boolean} to a CIF boolean value literal, in the
         * CIF ASCII representation.
         *
         * @param x The Java {@link Boolean} value.
         * @return The CIF boolean value literal, in the CIF ASCII representation.
         */
        public static String boolToStr(boolean x) {
            return x ? "true" : "false";
        }

        /**
         * Returns the cube root of a real number.
         *
         * @param x The real number.
         * @return {@code cbrt(x)}.
         */
        public static double cbrt(double x) {
            double rslt = Math.cbrt(x);
            return (rslt == -0.0) ? 0.0 : rslt;
        }

        /**
         * Returns the ceil of a real number.
         *
         * @param x The real number.
         * @return {@code ceil(x)}.
         * @throws annos_docException If the operation results in integer
         *      overflow.
         */
        public static int ceil(double x) {
            double rslt = Math.ceil(x);
            if (rslt < Integer.MIN_VALUE || rslt > Integer.MAX_VALUE) {
                String msg = fmt("Integer overflow: ceil(%s).", realToStr(x));
                throw new annos_docException(msg);
            }
            return (int)rslt;
        }

        /**
         * Checks a double value to ensure it is not NaN or infinite.
         *
         * @param value The double value to check.
         * @param name The name of the CIF variable that contains the value.
         */
        public static void checkDouble(double value, String name) {
            if (Double.isFinite(value)) return;

            String msg;
            if (Double.isNaN(value)) {
                msg = "NaN";
            } else if (value == Double.POSITIVE_INFINITY) {
                msg = "+inf";
            } else {
                msg = "-inf";
            }
            msg = fmt("The value of variable \"%s\" has become \"%s\".", name, msg);
            throw new annos_docException(msg);
        }

        /**
         * Creates and returns a shallow copy of a list.
         *
         * @param <TI> The type of the elements of the input list.
         * @param <TR> The type of the elements of the copied list.
         * @param lst The list to copy.
         * @return The shallow copy of the list.
         */
        public static <TR, TI extends TR> List<TR> copy(List<TI> lst) {
            List<TR> rslt = new ArrayList<TR>(lst.size());
            for (TI elem: lst) {
                rslt.add(elem);
            }
            return rslt;
        }

        /**
         * Returns the cosine of a real number.
         *
         * @param x The real number.
         * @return {@code cos(x)}.
         * @throws annos_docException If the operation results in real overflow,
         *      or {@code NaN}.
         */
        public static double cos(double x) {
            double rslt = Math.cos(x);
            if (Double.isInfinite(rslt)) {
                String msg = fmt("Real overflow: cos(%s).", realToStr(x));
                throw new annos_docException(msg);
            }
            if (Double.isNaN(rslt)) {
                String msg = fmt("Invalid operation: cos(%s).", realToStr(x));
                throw new annos_docException(msg);
            }
            return (rslt == -0.0) ? 0.0 : rslt;
        }

        /**
         * Returns the integer division of two integer numbers.
         *
         * @param x The dividend.
         * @param y The divisor.
         * @return {@code x div y}.
         * @throws annos_docException If the operation results in integer
         *      overflow, or division by zero.
         */
        public static int div(int x, int y) {
            if (y == 0) {
                String msg = fmt("Division by zero: %d div %d.", x, y);
                throw new annos_docException(msg);
            }
            if (x == Integer.MIN_VALUE && y == -1) {
                String msg = fmt("Integer overflow: %d div %d.", x, y);
                throw new annos_docException(msg);
            }
            return x / y;
        }

        /**
         * Returns the real division of two real numbers.
         *
         * @param x The first real number.
         * @param y The second real number.
         * @return {@code x / y}.
         * @throws annos_docException If the operation results in real overflow,
         *      or division by zero.
         */
        public static double divide(double x, double y) {
            if (y == 0.0) {
                String msg = fmt("Division by zero: %s / %s.",
                                 realToStr(x), realToStr(y));
                throw new annos_docException(msg);
            }
            double rslt = x / y;
            if (Double.isInfinite(rslt)) {
                String msg = fmt("Real overflow: %s * %s.",
                                 realToStr(x), realToStr(y));
                throw new annos_docException(msg);
            }
            return (rslt == -0.0) ? 0.0 : rslt;
        }

        /**
         * Returns {@code empty(x)} for the given list.
         *
         * @param x The list.
         * @return {@code empty(x)}
         */
        public static boolean empty(List<?> x) {
            return x.isEmpty();
        }

        /**
         * Returns {@code true} if the arguments are equal to each other and
         * {@code false} otherwise. Can handle {@code null} values.
         *
         * @param obj1 The first object.
         * @param obj2 The second object.
         * @return {@code true} if the arguments are equal to each other,
         *      {@code false} otherwise.
         */
        public static boolean equalObjs(Object obj1, Object obj2) {
            return Objects.equals(obj1, obj2);
        }

        /**
         * Returns a backslash escaped version of the string. That is:
         * <ul>
         * <li>backslash ({@code "\\"}) becomes {@code "\\\\"}</li>
         * <li>new line ({@code "\n"}) becomes {@code "\\n"}</li>
         * <li>tab ({@code "\t"}) becomes {@code "\\t"}</li>
         * <li>double quotes ({@code "\""}) becomes {@code "\\\""}</li>
         * </ul>
         *
         * @param s The string value to escape.
         * @return The backslash escaped string value.
         */
        public static String escape(String s) {
            return s.replace("\\", "\\\\")
                    .replace("\n", "\\n")
                    .replace("\t", "\\t")
                    .replace("\"", "\\\"");
        }

        /**
         * Returns {@code e}<sup>{@code x}</sup> of a real number {@code x}.
         *
         * @param x The real number.
         * @return {@code exp(x)}.
         * @throws annos_docException If the operation results in real overflow.
         */
        public static double exp(double x) {
            double rslt = Math.exp(x);
            if (Double.isInfinite(rslt)) {
                String msg = fmt("Real overflow: exp(%s).", realToStr(x));
                throw new annos_docException(msg);
            }
            return (rslt == -0.0) ? 0.0 : rslt;
        }

        /**
         * Returns the floor of a real number.
         *
         * @param x The real number.
         * @return {@code floor(x)}.
         * @throws annos_docException If the operation results in integer
         *      overflow.
         */
        public static int floor(double x) {
            double rslt = Math.floor(x);
            if (rslt < Integer.MIN_VALUE || rslt > Integer.MAX_VALUE) {
                String msg = fmt("Integer overflow: floor(%s).", realToStr(x));
                throw new annos_docException(msg);
            }
            return (int)rslt;
        }

        /**
         * Returns a formatted string using the specified format string and
         * arguments.
         *
         * <p>No localization is applied, which defaults to the US locale.</p>
         *
         * @param format A format/pattern string.
         * @param args The arguments referenced by the format specifiers in the
         *      format string.
         * @return A formatted string.
         */
        public static String fmt(String format, Object... args) {
            return String.format(null, format, args);
        }

       /**
        * Generates a hash code for a sequence of arguments. The hash code is
        * generated as if all the arguments were placed into an array, and that
        * array were hashed by calling {@link Arrays#hashCode(Object[])}.
        *
        * @param objs The objects for which to compute a hash code.
        * @return A hash value of the sequence of arguments.
        */
        public static int hashObjs(Object... objs) {
            return Arrays.hashCode(objs);
        }

        /**
         * Converts a Java {@link Integer} to a Java {@link Double}.
         *
         * @param x The Java {@link Integer} value.
         * @return The Java {@link Double} value.
         */
        public static double intToReal(int x) {
            // The 'int' to 'double' conversion is lossless. See
            //   https://www.securecoding.cert.org/confluence/display/java/
            //   NUM13-J.+Avoid+loss+of+precision+when+converting+primitive+
            //   integers+to+floating-point
            return x;
        }

        /**
         * Converts a Java {@link Integer} to a CIF integer value literal, in the
         * CIF ASCII representation.
         *
         * @param x The Java {@link Integer} value.
         * @return The CIF integer value literal, in the CIF ASCII representation.
         */
        public static String intToStr(int x) {
            return Integer.toString(x);
        }

        /**
         * Returns the natural logarithm of a real number.
         *
         * @param x The real number.
         * @return {@code ln(x)}.
         * @throws annos_docException If the real number is non-positive.
         */
        public static double ln(double x) {
            if (x <= 0.0) {
                String msg = fmt("Invalid operation: ln(%s).", realToStr(x));
                throw new annos_docException(msg);
            }
            return Math.log(x);
        }

        /**
         * Returns the logarithm (base 10) of a real number.
         *
         * @param x The real number.
         * @return {@code log(x)}.
         * @throws annos_docException If the real number is non-positive.
         */
        public static double log(double x) {
            if (x <= 0.0) {
                String msg = fmt("Invalid operation: log(%s).", realToStr(x));
                throw new annos_docException(msg);
            }
            return Math.log10(x);
        }

        /**
         * Fills the given empty list with the given elements, modifying the
         * list in-place, and returning that same list. This method is only used
         * for the construction of list literals.
         *
         * @param <T> The type of the elements of the list.
         * @param lst The list.
         * @param elems The elements.
         * @return The input list.
         */
        @SafeVarargs
        public static <T> List<T> makelist(List<T> lst, T... elems) {
            for (T elem: elems) {
                lst.add(elem);
            }
            return lst;
        }

        /**
         * Returns the maximum of two integer numbers.
         *
         * @param x The first integer number.
         * @param y The second integer number.
         * @return {@code max(x, y)}.
         */
        public static int max(int x, int y) {
            return Math.max(x, y);
        }

        /**
         * Returns the maximum of two real numbers.
         *
         * @param x The first real number.
         * @param y The second real number.
         * @return {@code max(x, y)}.
         */
        public static double max(double x, double y) {
            return Math.max(x, y);
        }

        /**
         * Returns the minimum of two integer numbers.
         *
         * @param x The first integer number.
         * @param y The second integer number.
         * @return {@code min(x, y)}.
         */
        public static int min(int x, int y) {
            return Math.min(x, y);
        }

        /**
         * Returns the minimum of two real numbers.
         *
         * @param x The first real number.
         * @param y The second real number.
         * @return {@code min(x, y)}.
         */
        public static double min(double x, double y) {
            // Assumes that the arguments are never -0.0.
            return Math.min(x, y);
        }

        /**
         * Returns the modulus of two integer numbers.
         *
         * @param x The dividend.
         * @param y The divisor.
         * @return {@code x mod y}.
         * @throws annos_docException If the operation results in division by
         *      zero.
         */
        public static int mod(int x, int y) {
            if (y == 0) {
                String msg = fmt("Division by zero: %d mod %d.", x, y);
                throw new annos_docException(msg);
            }
            return x % y;
        }

        /**
         * Creates a shallow copy of the given list, and replaces the element at
         * the given index with a new value.
         *
         * @param <T> The type of the elements of the list.
         * @param lst The list.
         * @param origIdx The 0-based index into the list of the element to
         *      replace. Negative indices are allowed, and count from the right.
         * @param newValue The new value.
         * @return The modified list.
         * @throws annos_docException If the index is out of range for the list.
         */
        public static <T> List<T> modify(List<T> lst, int origIdx, T newValue) {
            // Normalize index and check for out of bounds.
            int idx = origIdx;
            if (idx < 0) idx = lst.size() + idx;
            if (idx < 0 || idx >= lst.size()) {
                String msg = fmt("Index out of bounds: %s[%s].",
                                 valueToStr(lst), origIdx);
                throw new annos_docException(msg);
            }

            // Create a shallow copy, replace the element, and return the list.
            List<T> rslt = copy(lst);
            rslt.set(idx, newValue);
            return rslt;
        }

        /**
         * Returns the multiplication of two integer numbers.
         *
         * @param x The first integer number.
         * @param y The second integer number.
         * @return {@code x * y}.
         * @throws annos_docException If the operation results in integer
         *      overflow.
         */
        public static int multiply(int x, int y) {
            long rslt = (long)x * (long)y;
            if (Integer.MIN_VALUE <= rslt && rslt <= Integer.MAX_VALUE) {
                return (int)rslt;
            }

            String msg = fmt("Integer overflow: %d * %d.", x, y);
            throw new annos_docException(msg);
        }

        /**
         * Returns the multiplication of two real numbers.
         *
         * @param x The first real number.
         * @param y The second real number.
         * @return {@code x * y}.
         * @throws annos_docException If the operation results in real overflow.
         */
        public static double multiply(double x, double y) {
            double rslt = x * y;
            if (Double.isInfinite(rslt)) {
                String msg = fmt("Real overflow: %s * %s.",
                                 realToStr(x), realToStr(y));
                throw new annos_docException(msg);
            }
            return (rslt == -0.0) ? 0.0 : rslt;
        }

        /**
         * Returns the negation of an integer number.
         *
         * @param x The integer number.
         * @return {@code -value}.
         * @throws annos_docException If the operation results in integer
         *      overflow.
         */
        public static int negate(int x) {
            if (x == Integer.MIN_VALUE) {
                String msg = fmt("Integer overflow: -%d.", x);
                throw new annos_docException(msg);
            }
            return -x;
        }

        /**
         * Returns the negation of a real number.
         *
         * @param x The real number.
         * @return {@code -value}.
         */
        public static double negate(double x) {
            return (x == 0.0) ? 0.0 : -x;
        }

        /**
         * Normalizes a print target path. File separators {@code "\\"} and
         * {@code "/"} are replaced by the file separator of the current platform,
         * i.e. {@link #FILE_SEPARATOR}.
         *
         * @param path The print target path. Should not be special target
         *      {@code ":stdout"} or {@code ":stderr"}.
         * @return The normalized print target path.
         */
        public static String normalizePrintTarget(String path) {
            if (!FILE_SEPARATOR.equals("/"))  path = path.replace("/",  FILE_SEPARATOR);
            if (!FILE_SEPARATOR.equals("\\")) path = path.replace("\\", FILE_SEPARATOR);
            return path;
        }

        /**
         * Returns the exponentiation (power) of two integer numbers.
         *
         * @param x The base integer number.
         * @param y The exponent integer number, {@code y >= 0}.
         * @return {@code pow(x, y)}.
         * @throws annos_docException If the operation results in integer
         *      overflow.
         */
        public static int powInt(int x, int y) {
            if (y < 0) throw new RuntimeException("y < 0");
            double rslt = Math.pow(x, y);
            if (Integer.MIN_VALUE <= rslt && rslt <= Integer.MAX_VALUE) {
                return (int)rslt;
            }
            String msg = fmt("Integer overflow: pow(%d, %d).", x, y);
            throw new annos_docException(msg);
        }

        /**
         * Returns the exponentiation (power) of two real numbers.
         *
         * @param x The base real number.
         * @param y The exponent real number.
         * @return {@code pow(x, y)}.
         * @throws annos_docException If the operation results in real overflow,
         *      or {@code NaN}.
         */
        public static double powReal(double x, double y) {
            // Assumes that the arguments are valid doubles (no inf/NaN/-0.0).
            double rslt = Math.pow(x, y);
            if (Double.isInfinite(rslt)) {
                String msg = fmt("Real overflow: pow(%s, %s).",
                                 realToStr(x), realToStr(y));
                throw new annos_docException(msg);
            }
            if (Double.isNaN(rslt)) {
                String msg = fmt("Invalid operation: pow(%s, %s).",
                                 realToStr(x), realToStr(y));
                throw new annos_docException(msg);
            }
            return (rslt == -0.0) ? 0.0 : rslt;
        }

        /**
         * Converts a Java {@link Double} to a CIF real value literal, in the CIF
         * ASCII representation.
         *
         * <p>Note that the {@link Double} values may be negative, and the
         * resulting textual representation may thus have a {@code "-"} prefix,
         * unlike real value literals in the CIF ASCII representation, where the
         * {@code "-"} character is a unary operator.</p>
         *
         * @param x The Java {@link Double} value.
         * @return The CIF real value literal, in the CIF ASCII representation.
         */
        public static String realToStr(double x) {
            // Double.toString always results in valid CIF ASCII representations
            // of real values. It also is round-trip compatible (up to fixed point
            // behavior) with strToReal.
            if (!Double.isFinite(x)) throw new RuntimeException(Double.toString(x));
            return Double.toString(x).replace('E', 'e');
        }

        /**
         * Projects a list, using a zero-based index.
         *
         * @param <T> The type of the elements of the list.
         * @param lst The list.
         * @param origIdx The 0-based index into the list of the element to return.
         *      Negative indices are allowed, and count from the right.
         * @return {@code lst[origIdx]}.
         * @throws annos_docException If the index is out of range for the list.
         */
        public static <T> T project(List<T> lst, int origIdx) {
            // Normalize index and check for out of bounds.
            int idx = origIdx;
            if (idx < 0) idx = lst.size() + idx;
            if (idx < 0 || idx >= lst.size()) {
                String msg = fmt("Index out of bounds: %s[%s].",
                                 valueToStr(lst), origIdx);
                throw new annos_docException(msg);
            }

            // Return the element.
            return lst.get(idx);
        }

        /**
         * Projects a string, using a zero-based index.
         *
         * @param str The string.
         * @param origIdx The 0-based index into the string of the character to
         *      return. Negative indices are allowed, and count from the right.
         * @return {@code str[origIdx]}.
         * @throws annos_docException If the index is out of range for the
         *      string.
         */
        public static String project(String str, int origIdx) {
            int idx = origIdx;
            if (idx < 0) idx = str.length() + idx;
            if (idx < 0 || idx >= str.length()) {
                String msg = fmt("Index out of bounds: \"%s\"[%s].",
                                 escape(str), origIdx);
                throw new annos_docException(msg);
            }

            return str.substring(idx, idx + 1);
        }

        /**
         * Invoked in case of an integer range out of bounds error.
         *
         * @param name The name of the variable that contains the integer value, as
         *      end-user readable text.
         * @param value The value of the variable that contains the integer value,
         *      as end-user readable text.
         * @param type The type of the variable that contains the integer value,
         *      as end-user readable text.
         * @throws annos_docException Always thrown.
         */
        public static void rangeErrInt(String name, String value, String type) {
            String msg = fmt("Variable \"%s\" is assigned value \"%s\", which " +
                             "violates the integer type bounds of the type " +
                             "\"%s\" of that variable.", name, value, type);
            throw new annos_docException(msg);
        }

        /**
         * Returns the round of a real number.
         *
         * @param x The real number.
         * @return {@code round(x)}.
         * @throws annos_docException If the operation results in integer
         *      overflow.
         */
        public static int round(double x) {
            if (x < Integer.MIN_VALUE - 0.5 || x >= Integer.MAX_VALUE + 0.5) {
                String msg = fmt("Integer overflow: round(%s).", realToStr(x));
                throw new annos_docException(msg);
            }
            long rslt = Math.round(x);
            return (int)rslt;
        }

        /**
         * Returns a linearly scaled value.
         *
         * @param v The value to scale.
         * @param inmin The minimum of the input interval.
         * @param inmax The maximum of the input interval.
         * @param outmin The minimum of the output interval.
         * @param outmax The maximum of the output interval.
         * @return {@code scale(v, inmin, inmax, outmin, outmax)}.
         * @throws annos_docException If the input interval is empty, or the
         *      operation results in real overflow.
         */
        public static double scale(double v, double inmin, double inmax,
                                   double outmin, double outmax)
        {
            // fraction = (v - inmin) / (inmax - inmin);
            // result = outmin + fraction * (outmax - outmin);
            double inrange = subtract(inmax, inmin);
            if (inrange == 0) {
                String msg = fmt("Empty input interval: scale(%s, %s, %s, %s, " +
                                 "%s).", realToStr(v),
                                 realToStr(inmin), realToStr(inmax),
                                 realToStr(outmin), realToStr(outmax));
                throw new annos_docException(msg);
            }
            double fraction = divide(subtract(v, inmin), inrange);
            return addReal(outmin, multiply(fraction, subtract(outmax, outmin)));
        }

        /**
         * Returns the sign of an integer number.
         *
         * @param x The integer number.
         * @return {@code sign(x)}.
         */
        public static int sign(int x) {
            return (x == 0) ? 0 : (x < 0) ? -1 : 1;
        }

        /**
         * Returns the sign of a real number.
         *
         * @param x The real number.
         * @return {@code sign(x)}.
         */
        public static int sign(double x) {
            return (x == 0.0) ? 0 : (x < 0.0) ? -1 : 1;
        }

        /**
         * Returns the sine of a real number.
         *
         * @param x The real number.
         * @return {@code sin(x)}.
         * @throws annos_docException If the operation results in real overflow,
         *      or {@code NaN}.
         */
        public static double sin(double x) {
            double rslt = Math.sin(x);
            if (Double.isInfinite(rslt)) {
                String msg = fmt("Real overflow: sin(%s).", realToStr(x));
                throw new annos_docException(msg);
            }
            if (Double.isNaN(rslt)) {
                String msg = fmt("Invalid operation: sin(%s).", realToStr(x));
                throw new annos_docException(msg);
            }
            return (rslt == -0.0) ? 0.0 : rslt;
        }

        /**
         * Returns {@code size(x)} for the given string.
         *
         * @param x The string.
         * @return {@code size(x)}
         */
        public static int size(String x) {
            return x.length();
        }

        /**
         * Returns {@code size(x)} for the given list.
         *
         * @param x The list.
         * @return {@code size(x)}
         */
        public static int size(List<?> x) {
            return x.size();
        }

        /**
         * Returns the square root of a real number.
         *
         * @param x The real number.
         * @return {@code sqrt(x)}.
         * @throws annos_docException If the real number is negative.
         */
        public static double sqrt(double x) {
            // Assumes that the argument is never -0.0.
            if (x < 0.0) {
                String msg = fmt("Invalid operation: sqrt(%s).", realToStr(x));
                throw new annos_docException(msg);
            }
            return Math.sqrt(x);
        }

        /**
         * Converts a CIF boolean value literal, in the CIF ASCII representation,
         * to a Java {@link Boolean}.
         *
         * @param x The CIF boolean value literal, in the CIF ASCII representation.
         * @return The Java {@link Boolean} value.
         * @throws annos_docException If the string value does not represent a
         *      boolean value.
         */
        public static boolean strToBool(String x) {
            if (x.equals("true")) return true;
            if (x.equals("false")) return false;

            String msg = fmt("Cast from type \"string\" to type \"bool\" " +
                             "failed: the string value does not represent a " +
                             "boolean value: \"%s\".", escape(x));
            throw new annos_docException(msg);
        }

        /**
         * Converts a CIF integer value literal, in the CIF ASCII representation,
         * to a Java {@link Integer}.
         *
         * <p>See also the {@code CifTypeChecker.transIntExpression} method.</p>
         *
         * @param x The CIF integer value literal, in the CIF ASCII representation.
         * @return The Java {@link Integer} value.
         * @throws annos_docException If the string value does not represent an
         *      integer value.
         */
        public static int strToInt(String x) {
            // Integer.parseInt allows all valid integer values in CIF ASCII
            // syntax, as well as negative values (which are unary operator '-'
            // with an integer value in CIF ASCII syntax), and integer values
            // with arbitrary '0' prefixes (not allowed in CIF ASCII syntax).
            try {
                return Integer.parseInt(x);
            } catch (NumberFormatException ex) {
                String msg = fmt("Cast from type \"string\" to type \"int\" " +
                                 "failed: the string value does not represent " +
                                 "an integer value, or the integer value " +
                                 "resulted in integer overflow: \"%s\".",
                                 escape(x));
                throw new annos_docException(msg);
            }
        }

        /**
         * Converts a CIF real value literal, in the CIF ASCII representation,
         * to a Java {@link Double}.
         *
         * <p>See also the {@code CifTypeChecker.transRealExpression} method.</p>
         *
         * @param x The CIF real value literal, in the CIF ASCII representation.
         * @return The Java {@link Double} value.
         * @throws annos_docException If the string value does not represent an
         *      integer value.
         */
        public static double strToReal(String x) {
            // Double.parseDouble allows all valid real values in CIF ASCII syntax,
            // as well a whole bunch of additional representations, such as
            // negative values (which are unary operator '-' with an real value
            // in CIF ASCII syntax), signed integers (not allowed in the CIF
            // ASCII syntax), hexadecimal floating point notation (not allowed in
            // CIF ASCII syntax), etc.

            double rslt;
            try {
                rslt = Double.parseDouble(x);
                if (Double.isNaN(rslt)) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                String msg = fmt("Cast from type \"string\" to type \"real\" " +
                                 "failed: the string value does not represent a " +
                                 "real value: \"%s\".", escape(x));
                throw new annos_docException(msg);
            }

            if (Double.isInfinite(rslt)) {
                String msg = fmt("Cast from type \"string\" to type \"real\" " +
                                 "failed, due to real overflow: \"%s\".",
                                 escape(x));
                throw new annos_docException(msg);
            }

            return (rslt == -0.0) ? 0.0 : rslt;
        }

        /**
         * Returns the subtraction of two integer numbers.
         *
         * @param x The first integer number.
         * @param y The second integer number.
         * @return {@code x - y}.
         * @throws annos_docException If the operation results in integer
         *      overflow.
         */
        public static int subtract(int x, int y) {
            long rslt = (long)x - (long)y;
            if (Integer.MIN_VALUE <= rslt && rslt <= Integer.MAX_VALUE) {
                return (int)rslt;
            }

            String msg = fmt("Integer overflow: %d - %d.", x, y);
            throw new annos_docException(msg);
        }

        /**
         * Returns the subtraction of two real numbers.
         *
         * @param x The first real number.
         * @param y The second real number.
         * @return {@code x - y}.
         * @throws annos_docException If the operation results in real overflow.
         */
        public static double subtract(double x, double y) {
            double rslt = x - y;
            if (Double.isInfinite(rslt)) {
                String msg = fmt("Real overflow: %s - %s.",
                                 realToStr(x), realToStr(y));
                throw new annos_docException(msg);
            }
            return (rslt == -0.0) ? 0.0 : rslt;
        }

        /**
         * Returns the tangent of a real number.
         *
         * @param x The real number.
         * @return {@code tan(x)}.
         * @throws annos_docException If the operation results in real overflow,
         *      or {@code NaN}.
         */
        public static double tan(double x) {
            double rslt = Math.tan(x);
            if (Double.isInfinite(rslt)) {
                String msg = fmt("Real overflow: tan(%s).", realToStr(x));
                throw new annos_docException(msg);
            }
            if (Double.isNaN(rslt)) {
                String msg = fmt("Invalid operation: tan(%s).", realToStr(x));
                throw new annos_docException(msg);
            }
            return (rslt == -0.0) ? 0.0 : rslt;
        }

        /**
         * Converts the given value into a textual representation, closely
         * resembling the CIF ASCII syntax.
         *
         * @param value The value.
         * @return The textual representation.
         */
        public static String valueToStr(Object value) {
            if (value instanceof Boolean) {
                boolean bvalue = (Boolean)value;
                return bvalue ? "true" : "false";

            } else if (value instanceof Integer) {
                int ivalue = (Integer)value;
                return Integer.toString(ivalue);

            } else if (value instanceof Double) {
                double dvalue = (Double)value;
                return Double.toString(dvalue).replace('E', 'e');

            } else if (value instanceof String) {
                String svalue = (String)value;
                return "\"" + escape(svalue) + "\"";

            } else if (value instanceof List) {
                List<?> lst = (List<?>)value;
                StringBuilder txt = new StringBuilder();
                txt.append("[");
                for (int i = 0; i < lst.size(); i++) {
                    if (i > 0) txt.append(", ");
                    txt.append(valueToStr(lst.get(i)));
                }
                txt.append("]");
                return txt.toString();

            } else if (value instanceof Enum) {
                return value.toString();

            } else if (value instanceof annos_doc.CifTupleBase) {
                return value.toString();

            } else {
                throw new RuntimeException("Unsupported value: " + value);
            }
        }
    }
}
