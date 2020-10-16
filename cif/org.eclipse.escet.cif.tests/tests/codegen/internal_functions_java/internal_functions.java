package internal_functions_java;

import static internal_functions_java.internal_functions.internal_functionsUtils.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/** internal_functions code generated from a CIF specification. */
@SuppressWarnings("unused")
public abstract class internal_functions {
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

    };


    /** Variable 'time'. */
    public double time;

    /** Discrete variable "aut.v00". */
    public int aut_v00_;

    /** Discrete variable "aut.v01". */
    public int aut_v01_;

    /** Discrete variable "aut.v02". */
    public int aut_v02_;

    /** Discrete variable "aut.v03". */
    public int aut_v03_;

    /** Discrete variable "aut.v04". */
    public int aut_v04_;

    /** Discrete variable "aut.v05". */
    public int aut_v05_;

    /** Discrete variable "aut.v06". */
    public int aut_v06_;

    /** Discrete variable "aut.v07". */
    public int aut_v07_;

    /** Discrete variable "aut.v08". */
    public int aut_v08_;

    /** Discrete variable "aut.v09". */
    public double aut_v09_;

    /** Discrete variable "aut.v10". */
    public int aut_v10_;

    /** Discrete variable "aut.v11". */
    public List<Integer> aut_v11_;

    /** Discrete variable "aut.v12". */
    public List<Integer> aut_v12_;

    /** Discrete variable "aut.v13". */
    public int aut_v13_;

    /** Discrete variable "aut.v14". */
    public int aut_v14_;

    /** Discrete variable "aut.v15". */
    public int aut_v15_;

    /** Discrete variable "aut.v16". */
    public int aut_v16_;

    /** Discrete variable "aut.v17". */
    public int aut_v17_;

    /** Discrete variable "aut.v18". */
    public int aut_v18_;

    /** Discrete variable "aut.v19". */
    public int aut_v19_;

    /** Discrete variable "aut.combi". */
    public int aut_combi_;

    /** Discrete variable "aut". */
    public internal_functionsEnum aut_;


    /** Constructor for the {@link internal_functions} class. */
    public internal_functions() {
        firstExec = true;
        this.time = 0.0;
    }

    /**
     * Execute the code once. Inputs are read, transitions are executed until
     * none are possible, outputs are written, etc.
     *
     * @param newTime The time in seconds, since the start of the first
     *      execution.
     * @throws internal_functionsException In case of a runtime error caused by code
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
            // No continuous variables, except variable 'time'.
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
     * @throws internal_functionsException In case of a runtime error caused by code
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


    /** Initializes the state. */
    private void initState() {
        aut_v00_ = 5;
        aut_v01_ = inc_(aut_v00_);
        aut_v02_ = factorial_(5);
        aut_v03_ = rec1_(7);
        aut_v04_ = rec2_(7);
        aut_v05_ = addInt((multi_return_())._field0, floor((multi_return_())._field1));
        aut_v06_ = f0_();
        aut_v07_ = f1_(1);
        aut_v08_ = f2_(1, 2);
        aut_v09_ = f3_(1, 2, 3.0);
        aut_v10_ = locals_(1);
        aut_v11_ = rot1_(makelist(new ArrayList<Integer>(4), 1, 2, 3, 4));
        aut_v12_ = rot2_(makelist(new ArrayList<Integer>(4), 1, 2, 3, 4));
        aut_v13_ = fa_(1);
        aut_v14_ = fi_(1);
        aut_v15_ = fw_();
        aut_v16_ = fu1_();
        aut_v17_ = fu2_();
        aut_v18_ = fu3_();
        aut_v19_ = fr_();
        aut_combi_ = addInt(addInt(addInt(addInt(addInt(addInt(addInt(addInt(addInt(addInt(addInt(addInt(addInt(addInt(addInt(addInt(addInt(addInt(addInt(aut_v00_, aut_v01_), aut_v02_), aut_v03_), aut_v04_), aut_v05_), aut_v06_), aut_v07_), aut_v08_), floor(aut_v09_)), aut_v10_), project(aut_v11_, 0)), project(aut_v12_, 0)), aut_v13_), aut_v14_), aut_v15_), aut_v16_), aut_v17_), aut_v18_), aut_v19_);
        aut_ = internal_functionsEnum._X;
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
     * Evaluation for function "inc".
     *
     * @param inc_x_ Function parameter "inc.x".
     * @return The return value of the function.
     */
    public static int inc_(int inc_x_) {
        if (true) return addInt(inc_x_, 1);
        throw new RuntimeException("no return at end of func");
    }

    /**
     * Evaluation for function "factorial".
     *
     * @param factorial_x_ Function parameter "factorial.x".
     * @return The return value of the function.
     */
    public static int factorial_(int factorial_x_) {
        if (true) return (equalObjs(factorial_x_, 0)) ? 1 : (multiply(factorial_x_, factorial_(subtract(factorial_x_, 1))));
        throw new RuntimeException("no return at end of func");
    }

    /**
     * Evaluation for function "rec1".
     *
     * @param rec1_x_ Function parameter "rec1.x".
     * @return The return value of the function.
     */
    public static int rec1_(int rec1_x_) {
        if (true) return (equalObjs(rec1_x_, 0)) ? 1 : (rec2_(subtract(rec1_x_, 1)));
        throw new RuntimeException("no return at end of func");
    }

    /**
     * Evaluation for function "rec2".
     *
     * @param rec2_x_ Function parameter "rec2.x".
     * @return The return value of the function.
     */
    public static int rec2_(int rec2_x_) {
        if (true) return (equalObjs(rec2_x_, 0)) ? 2 : (rec1_(subtract(rec2_x_, 1)));
        throw new RuntimeException("no return at end of func");
    }

    /**
     * Evaluation for function "multi_return".
     *
     * @return The return value of the function.
     */
    public static CifTuple_T2IR multi_return_() {
        if (true) return new CifTuple_T2IR(1, 1.0);
        throw new RuntimeException("no return at end of func");
    }

    /**
     * Evaluation for function "f0".
     *
     * @return The return value of the function.
     */
    public static int f0_() {
        if (true) return 1;
        throw new RuntimeException("no return at end of func");
    }

    /**
     * Evaluation for function "f1".
     *
     * @param f1_x_ Function parameter "f1.x".
     * @return The return value of the function.
     */
    public static int f1_(int f1_x_) {
        if (true) return f1_x_;
        throw new RuntimeException("no return at end of func");
    }

    /**
     * Evaluation for function "f2".
     *
     * @param f2_x_ Function parameter "f2.x".
     * @param f2_y_ Function parameter "f2.y".
     * @return The return value of the function.
     */
    public static int f2_(int f2_x_, int f2_y_) {
        if (true) return addInt(f2_x_, f2_y_);
        throw new RuntimeException("no return at end of func");
    }

    /**
     * Evaluation for function "f3".
     *
     * @param f3_x_ Function parameter "f3.x".
     * @param f3_y_ Function parameter "f3.y".
     * @param f3_z_ Function parameter "f3.z".
     * @return The return value of the function.
     */
    public static double f3_(int f3_x_, int f3_y_, double f3_z_) {
        if (true) return addReal(addInt(f3_x_, f3_y_), f3_z_);
        throw new RuntimeException("no return at end of func");
    }

    /**
     * Evaluation for function "locals".
     *
     * @param locals_x_ Function parameter "locals.x".
     * @return The return value of the function.
     */
    public static int locals_(int locals_x_) {
        int locals_a_ = 5;
        int locals_c_ = locals_a_;
        int locals_b_ = addInt(locals_c_, locals_x_);

        if (true) return locals_b_;
        throw new RuntimeException("no return at end of func");
    }

    /**
     * Evaluation for function "rot1".
     *
     * @param rot1_x_ Function parameter "rot1.x".
     * @return The return value of the function.
     */
    public static List<Integer> rot1_(List<Integer> rot1_x_) {
        int rot1_tmp_ = project(rot1_x_, 0);

        {
            int rhs1 = project(rot1_x_, 3);
            int index2 = 0;
            rot1_x_ = modify(rot1_x_, index2, rhs1);
        }

        {
            int rhs1 = project(rot1_x_, 2);
            int index2 = 1;
            rot1_x_ = modify(rot1_x_, index2, rhs1);
        }

        {
            int rhs1 = project(rot1_x_, 1);
            int index2 = 2;
            rot1_x_ = modify(rot1_x_, index2, rhs1);
        }

        {
            int rhs1 = rot1_tmp_;
            int index2 = 3;
            rot1_x_ = modify(rot1_x_, index2, rhs1);
        }

        if (true) return rot1_x_;
        throw new RuntimeException("no return at end of func");
    }

    /**
     * Evaluation for function "rot2".
     *
     * @param rot2_x_ Function parameter "rot2.x".
     * @return The return value of the function.
     */
    public static List<Integer> rot2_(List<Integer> rot2_x_) {
        List<Integer> rot2_rslt_ = makelist(new ArrayList<Integer>(4), 0, 0, 0, 0);

        {
            int rhs1 = project(rot2_x_, 3);
            int index2 = 0;
            rot2_rslt_ = modify(rot2_rslt_, index2, rhs1);
        }

        {
            int rhs1 = project(rot2_x_, 2);
            int index2 = 1;
            rot2_rslt_ = modify(rot2_rslt_, index2, rhs1);
        }

        {
            int rhs1 = project(rot2_x_, 1);
            int index2 = 2;
            rot2_rslt_ = modify(rot2_rslt_, index2, rhs1);
        }

        {
            int rhs1 = project(rot2_x_, 0);
            int index2 = 3;
            rot2_rslt_ = modify(rot2_rslt_, index2, rhs1);
        }

        if (true) return rot2_x_;
        throw new RuntimeException("no return at end of func");
    }

    /**
     * Evaluation for function "fa".
     *
     * @param fa_x_ Function parameter "fa.x".
     * @return The return value of the function.
     */
    public static int fa_(int fa_x_) {
        List<Integer> fa_y_ = makelist(new ArrayList<Integer>(3), fa_x_, fa_x_, fa_x_);
        int fa_a_ = fa_x_;
        int fa_b_ = addInt(fa_x_, 1);
        CifTuple_T2II fa_t_ = new CifTuple_T2II(0, 0);

        {
            int rhs1 = 1;
            int index2 = 0;
            fa_y_ = modify(fa_y_, index2, rhs1);
        }
        {
            int rhs1 = 2;
            int index2 = 1;
            fa_y_ = modify(fa_y_, index2, rhs1);
        }

        fa_y_ = makelist(new ArrayList<Integer>(3), addInt(project(fa_y_, 0), 1), project(fa_y_, 1), project(fa_y_, 2));

        {
            int fa_a_tmp1 = fa_a_;
            fa_a_ = fa_b_;
            fa_b_ = fa_a_tmp1;
        }

        fa_t_ = new CifTuple_T2II(addInt(fa_a_, fa_b_), subtract(fa_b_, fa_a_));

        {
            CifTuple_T2II rhs1 = fa_t_;
            fa_a_ = (rhs1)._field0;
            fa_b_ = (rhs1)._field1;
        }

        fa_x_ = addInt(addInt(fa_a_, fa_b_), project(fa_y_, 0));

        if (true) return fa_x_;
        throw new RuntimeException("no return at end of func");
    }

    /**
     * Evaluation for function "fi".
     *
     * @param fi_x_ Function parameter "fi.x".
     * @return The return value of the function.
     */
    public static int fi_(int fi_x_) {
        if (equalObjs(fi_x_, 1)) {
            fi_x_ = addInt(fi_x_, 1);
        }

        if (equalObjs(fi_x_, 1)) {
            fi_x_ = addInt(fi_x_, 1);
        } else if (equalObjs(fi_x_, 2)) {
            fi_x_ = addInt(fi_x_, 2);
        }

        if (equalObjs(fi_x_, 2)) {
            fi_x_ = addInt(fi_x_, 1);
        } else if (equalObjs(fi_x_, 3)) {
            fi_x_ = addInt(fi_x_, 2);
        } else if (equalObjs(fi_x_, 4)) {
            fi_x_ = addInt(fi_x_, 3);
        }

        if (equalObjs(fi_x_, 2)) {
            fi_x_ = addInt(fi_x_, 1);
        } else if (equalObjs(fi_x_, 3)) {
            fi_x_ = addInt(fi_x_, 2);
        } else {
            fi_x_ = addInt(fi_x_, 4);
        }

        if (equalObjs(fi_x_, 6)) {
            fi_x_ = addInt(fi_x_, 1);
        } else {
            fi_x_ = addInt(fi_x_, 2);
        }

        if ((fi_x_) > (4)) {
            if ((fi_x_) < (6)) {
                fi_x_ = subtract(fi_x_, 1);
            } else {
                fi_x_ = subtract(fi_x_, 2);
            }
        }

        if (true) return fi_x_;
        throw new RuntimeException("no return at end of func");
    }

    /**
     * Evaluation for function "fw".
     *
     * @return The return value of the function.
     */
    public static int fw_() {
        int fw_x_ = 0;

        if (true) while ((fw_x_) > (0)) {
            if (true) while ((fw_x_) < (10)) {
                if (equalObjs((fw_x_) % (2), 1)) {
                    if (true) continue;
                }

                if (equalObjs(fw_x_, 8)) {
                    if (true) break;
                }
            }
        }

        if (true) return fw_x_;
        throw new RuntimeException("no return at end of func");
    }

    /**
     * Evaluation for function "fu1".
     *
     * @return The return value of the function.
     */
    public static int fu1_() {
        if (true) {
            if (true) return 1;
        }

        if (true) return 0;
        throw new RuntimeException("no return at end of func");
    }

    /**
     * Evaluation for function "fu2".
     *
     * @return The return value of the function.
     */
    public static int fu2_() {
        if (true) return 1;

        if (true) return 0;
        throw new RuntimeException("no return at end of func");
    }

    /**
     * Evaluation for function "fu3".
     *
     * @return The return value of the function.
     */
    public static int fu3_() {
        if (true) while (true) {
            if (true) return 1;
        }

        if (true) return 0;
        throw new RuntimeException("no return at end of func");
    }

    /**
     * Evaluation for function "fr".
     *
     * @return The return value of the function.
     */
    public static int fr_() {
        List<Integer> fr_x_ = makelist(new ArrayList<Integer>(3), 1, 2, 3);
        List<Integer> fr_y_ = makelist(new ArrayList<Integer>(3), 2, 3, 4);

        {
            List<Integer> rhs1 = fr_y_;
            for(int rng_index0 = 0; rng_index0 < rhs1.size(); rng_index0++) {
                Integer rng_elem0 = rhs1.get(rng_index0);
                if ((rng_elem0) > 3) {
                    rangeErrInt("fr.x" + "[" + Integer.toString(rng_index0) + "]", valueToStr(rng_elem0), "list[3] int[0..3]");
                }
            }
            fr_x_ = rhs1;
        }

        {
            List<Integer> rhs1 = makelist(new ArrayList<Integer>(3), -(1), 3, 5);
            for(int rng_index0 = 0; rng_index0 < rhs1.size(); rng_index0++) {
                Integer rng_elem0 = rhs1.get(rng_index0);
                if ((rng_elem0) < 0 || (rng_elem0) > 3) {
                    rangeErrInt("fr.x" + "[" + Integer.toString(rng_index0) + "]", valueToStr(rng_elem0), "list[3] int[0..3]");
                }
            }
            fr_x_ = rhs1;
        }

        if (true) return 1;
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


    /** Tuple class for CIF tuple type representative "tuple(int; real)". */
    public static class CifTuple_T2IR extends CifTupleBase<CifTuple_T2IR> {
        /** The 1st field. */
        public int _field0;

        /** The 2nd field. */
        public double _field1;

        /**
         * Constructor for the {@link CifTuple_T2IR} class.
         *
         * @param _field0 The 1st field.
         * @param _field1 The 2nd field.
         */
        public CifTuple_T2IR(int _field0, double _field1) {
            this._field0 = _field0;
            this._field1 = _field1;
        }

        @Override
        public CifTuple_T2IR copy() {
            return new CifTuple_T2IR(_field0, _field1);
        }

        @Override
        public int hashCode() {
            return hashObjs(_field0, _field1);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            CifTuple_T2IR other = (CifTuple_T2IR)obj;
            return equalObjs(this._field0, other._field0) &&
                   equalObjs(this._field1, other._field1);
        }

        @Override
        public String toString() {
            StringBuilder rslt = new StringBuilder();
            rslt.append("(");
            rslt.append(valueToStr(_field0));
            rslt.append(", ");
            rslt.append(valueToStr(_field1));
            rslt.append(")");
            return rslt.toString();
        }
    }


    /** Tuple class for CIF tuple type representative "tuple(int g; int h)". */
    public static class CifTuple_T2II extends CifTupleBase<CifTuple_T2II> {
        /** The 1st field. */
        public int _field0;

        /** The 2nd field. */
        public int _field1;

        /**
         * Constructor for the {@link CifTuple_T2II} class.
         *
         * @param _field0 The 1st field.
         * @param _field1 The 2nd field.
         */
        public CifTuple_T2II(int _field0, int _field1) {
            this._field0 = _field0;
            this._field1 = _field1;
        }

        @Override
        public CifTuple_T2II copy() {
            return new CifTuple_T2II(_field0, _field1);
        }

        @Override
        public int hashCode() {
            return hashObjs(_field0, _field1);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            CifTuple_T2II other = (CifTuple_T2II)obj;
            return equalObjs(this._field0, other._field0) &&
                   equalObjs(this._field1, other._field1);
        }

        @Override
        public String toString() {
            StringBuilder rslt = new StringBuilder();
            rslt.append("(");
            rslt.append(valueToStr(_field0));
            rslt.append(", ");
            rslt.append(valueToStr(_field1));
            rslt.append(")");
            return rslt.toString();
        }
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
        if (true) {
            if (!pre) {
                Object value = aut_combi_;
                String text = valueToStr(value);
                infoPrintOutput(text, ":stdout");
            }
        }
    }

    /**
     * Informs that new print output is available.
     *
     * @param text The text being printed.
     * @param target The file or special target to which text is to be printed.
     *      If printed to a file, an absolute or relative local file system
     *      path is given. Paths may contain both {@code "/"} and {@code "\\"}
     *      as path separators. Use {@link internal_functionsUtils#normalizePrintTarget}
     *      to normalize the path to use path separators for the current
     *      platform. There are two special targets: {@code ":stdout"} to print
     *      to the standard output stream, and {@code ":stderr"} to print to
     *      the standard error stream.
     */
    protected abstract void infoPrintOutput(String text, String target);

    /** internal_functions enumeration. */
    public static enum internal_functionsEnum {
        /** X */ _X;

        @Override
        public String toString() {
            return name().substring(1);
        }
    }

    /**
     * internal_functions exception.
     *
     * <p>Indices a runtime error while executing the generated code.</p>
     */
    public static class internal_functionsException extends RuntimeException {
        /**
         * Constructor for the {@link internal_functionsException} class.
         *
         * @param message The message describing the exception.
         */
        public internal_functionsException(String message) {
            super(message);
        }

        /**
         * Constructor for the {@link internal_functionsException} class.
         *
         * @param message The message describing the exception.
         * @param cause The root cause of the exception.
         */
        public internal_functionsException(String message, Throwable cause) {
            super(message, cause);
        }

        @Override
        public String toString() {
            return getMessage();
        }
    }

    /** {@link internal_functions} utility code. */
    public static class internal_functionsUtils {
        /** The path separator for the current platform. */
        private static final String PATH_SEPARATOR = System.getProperty("file.separator");

        /** Constructor for the {@link internal_functionsUtils} class. */
        private internal_functionsUtils() {
            // Static class.
        }

        /**
         * Returns the absolute value of an integer number.
         *
         * @param x The integer number.
         * @return {@code abs(x)}.
         * @throws internal_functionsException If the operation results in integer
         *      overflow.
         */
        public static int abs(int x) {
            if (x == Integer.MIN_VALUE) {
                String msg = fmt("Integer overflow: abs(%d).", x);
                throw new internal_functionsException(msg);
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
         * @throws internal_functionsException If the operation results in real overflow,
         *      or {@code NaN}.
         */
        public static double acos(double x) {
            double rslt = Math.acos(x);
            if (Double.isInfinite(rslt)) {
                String msg = fmt("Real overflow: acos(%s).", realToStr(x));
                throw new internal_functionsException(msg);
            }
            if (Double.isNaN(rslt)) {
                String msg = fmt("Invalid operation: acos(%s).", realToStr(x));
                throw new internal_functionsException(msg);
            }
            return (rslt == -0.0) ? 0.0 : rslt;
        }

        /**
         * Returns the arc sine of a real number.
         *
         * @param x The real number.
         * @return {@code asin(x)}.
         * @throws internal_functionsException If the operation results in real overflow,
         *      or {@code NaN}.
         */
        public static double asin(double x) {
            double rslt = Math.asin(x);
            if (Double.isInfinite(rslt)) {
                String msg = fmt("Real overflow: asin(%s).", realToStr(x));
                throw new internal_functionsException(msg);
            }
            if (Double.isNaN(rslt)) {
                String msg = fmt("Invalid operation: asin(%s).", realToStr(x));
                throw new internal_functionsException(msg);
            }
            return (rslt == -0.0) ? 0.0 : rslt;
        }

        /**
         * Returns the arc tangent of a real number.
         *
         * @param x The real number.
         * @return {@code atan(x)}.
         * @throws internal_functionsException If the operation results in real overflow,
         *      or {@code NaN}.
         */
        public static double atan(double x) {
            double rslt = Math.atan(x);
            if (Double.isInfinite(rslt)) {
                String msg = fmt("Real overflow: atan(%s).", realToStr(x));
                throw new internal_functionsException(msg);
            }
            if (Double.isNaN(rslt)) {
                String msg = fmt("Invalid operation: atan(%s).", realToStr(x));
                throw new internal_functionsException(msg);
            }
            return (rslt == -0.0) ? 0.0 : rslt;
        }

        /**
         * Returns the addition of two integer numbers.
         *
         * @param x The first integer number.
         * @param y The second integer number.
         * @return {@code x + y}.
         * @throws internal_functionsException If the operation results in integer
         *      overflow.
         */
        public static int addInt(int x, int y) {
            long rslt = (long)x + (long)y;
            if (Integer.MIN_VALUE <= rslt && rslt <= Integer.MAX_VALUE) {
                return (int)rslt;
            }

            String msg = fmt("Integer overflow: %d + %d.", x, y);
            throw new internal_functionsException(msg);
        }

        /**
         * Returns the addition of two real numbers.
         *
         * @param x The first real number.
         * @param y The second real number.
         * @return {@code x + y}.
         * @throws internal_functionsException If the operation results in real overflow.
         */
        public static double addReal(double x, double y) {
            double rslt = x + y;
            if (Double.isInfinite(rslt)) {
                String msg = fmt("Real overflow: %s + %s.",
                                 realToStr(x), realToStr(y));
                throw new internal_functionsException(msg);
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
         * @throws internal_functionsException If the operation results in integer
         *      overflow.
         */
        public static int ceil(double x) {
            double rslt = Math.ceil(x);
            if (rslt < Integer.MIN_VALUE || rslt > Integer.MAX_VALUE) {
                String msg = fmt("Integer overflow: ceil(%s).", realToStr(x));
                throw new internal_functionsException(msg);
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
            throw new internal_functionsException(msg);
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
         * @throws internal_functionsException If the operation results in real overflow,
         *      or {@code NaN}.
         */
        public static double cos(double x) {
            double rslt = Math.cos(x);
            if (Double.isInfinite(rslt)) {
                String msg = fmt("Real overflow: cos(%s).", realToStr(x));
                throw new internal_functionsException(msg);
            }
            if (Double.isNaN(rslt)) {
                String msg = fmt("Invalid operation: cos(%s).", realToStr(x));
                throw new internal_functionsException(msg);
            }
            return (rslt == -0.0) ? 0.0 : rslt;
        }

        /**
         * Returns the integer division of two integer numbers.
         *
         * @param x The dividend.
         * @param y The divisor.
         * @return {@code x div y}.
         * @throws internal_functionsException If the operation results in integer
         *      overflow, or division by zero.
         */
        public static int div(int x, int y) {
            if (y == 0) {
                String msg = fmt("Division by zero: %d div %d.", x, y);
                throw new internal_functionsException(msg);
            }
            if (x == Integer.MIN_VALUE && y == -1) {
                String msg = fmt("Integer overflow: %d div %d.", x, y);
                throw new internal_functionsException(msg);
            }
            return x / y;
        }

        /**
         * Returns the real division of two real numbers.
         *
         * @param x The first real number.
         * @param y The second real number.
         * @return {@code x / y}.
         * @throws internal_functionsException If the operation results in real overflow,
         *      or division by zero.
         */
        public static double divide(double x, double y) {
            if (y == 0.0) {
                String msg = fmt("Division by zero: %s / %s.",
                                 realToStr(x), realToStr(y));
                throw new internal_functionsException(msg);
            }
            double rslt = x / y;
            if (Double.isInfinite(rslt)) {
                String msg = fmt("Real overflow: %s * %s.",
                                 realToStr(x), realToStr(y));
                throw new internal_functionsException(msg);
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
         * @throws internal_functionsException If the operation results in real overflow.
         */
        public static double exp(double x) {
            double rslt = Math.exp(x);
            if (Double.isInfinite(rslt)) {
                String msg = fmt("Real overflow: exp(%s).", realToStr(x));
                throw new internal_functionsException(msg);
            }
            return (rslt == -0.0) ? 0.0 : rslt;
        }

        /**
         * Returns the floor of a real number.
         *
         * @param x The real number.
         * @return {@code floor(x)}.
         * @throws internal_functionsException If the operation results in integer
         *      overflow.
         */
        public static int floor(double x) {
            double rslt = Math.floor(x);
            if (rslt < Integer.MIN_VALUE || rslt > Integer.MAX_VALUE) {
                String msg = fmt("Integer overflow: floor(%s).", realToStr(x));
                throw new internal_functionsException(msg);
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
         * @throws internal_functionsException If the real number is non-positive.
         */
        public static double ln(double x) {
            if (x <= 0.0) {
                String msg = fmt("Invalid operation: ln(%s).", realToStr(x));
                throw new internal_functionsException(msg);
            }
            return Math.log(x);
        }

        /**
         * Returns the logarithm (base 10) of a real number.
         *
         * @param x The real number.
         * @return {@code log(x)}.
         * @throws internal_functionsException If the real number is non-positive.
         */
        public static double log(double x) {
            if (x <= 0.0) {
                String msg = fmt("Invalid operation: log(%s).", realToStr(x));
                throw new internal_functionsException(msg);
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
         * @throws internal_functionsException If the operation results in division by
         *      zero.
         */
        public static int mod(int x, int y) {
            if (y == 0) {
                String msg = fmt("Division by zero: %d mod %d.", x, y);
                throw new internal_functionsException(msg);
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
         * @throws internal_functionsException If the index is out of range for the list.
         */
        public static <T> List<T> modify(List<T> lst, int origIdx, T newValue) {
            // Normalize index and check for out of bounds.
            int idx = origIdx;
            if (idx < 0) idx = lst.size() + idx;
            if (idx < 0 || idx >= lst.size()) {
                String msg = fmt("Index out of bounds: %s[%s].",
                                 valueToStr(lst), origIdx);
                throw new internal_functionsException(msg);
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
         * @throws internal_functionsException If the operation results in integer
         *      overflow.
         */
        public static int multiply(int x, int y) {
            long rslt = (long)x * (long)y;
            if (Integer.MIN_VALUE <= rslt && rslt <= Integer.MAX_VALUE) {
                return (int)rslt;
            }

            String msg = fmt("Integer overflow: %d * %d.", x, y);
            throw new internal_functionsException(msg);
        }

        /**
         * Returns the multiplication of two real numbers.
         *
         * @param x The first real number.
         * @param y The second real number.
         * @return {@code x * y}.
         * @throws internal_functionsException If the operation results in real overflow.
         */
        public static double multiply(double x, double y) {
            double rslt = x * y;
            if (Double.isInfinite(rslt)) {
                String msg = fmt("Real overflow: %s * %s.",
                                 realToStr(x), realToStr(y));
                throw new internal_functionsException(msg);
            }
            return (rslt == -0.0) ? 0.0 : rslt;
        }

        /**
         * Returns the negation of an integer number.
         *
         * @param x The integer number.
         * @return {@code -value}.
         * @throws internal_functionsException If the operation results in integer
         *      overflow.
         */
        public static int negate(int x) {
            if (x == Integer.MIN_VALUE) {
                String msg = fmt("Integer overflow: -%d.", x);
                throw new internal_functionsException(msg);
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
         * Normalizes a print target path. Path separators {@code "\\"} and
         * {@code "/"} are replaced by the path separator of the current platform,
         * i.e. {@link #PATH_SEPARATOR}.
         *
         * @param path The print target path. Should not be special target
         *      {@code ":stdout"} or {@code ":stderr"}.
         * @return The normalized print target path.
         */
        public static String normalizePrintTarget(String path) {
            if (!PATH_SEPARATOR.equals("/"))  path = path.replace("/",  PATH_SEPARATOR);
            if (!PATH_SEPARATOR.equals("\\")) path = path.replace("\\", PATH_SEPARATOR);
            return path;
        }

        /**
         * Returns the exponentiation (power) of two integer numbers.
         *
         * @param x The base integer number.
         * @param y The exponent integer number, {@code y >= 0}.
         * @return {@code pow(x, y)}.
         * @throws internal_functionsException If the operation results in integer
         *      overflow.
         */
        public static int powInt(int x, int y) {
            if (y < 0) throw new RuntimeException("y < 0");
            double rslt = Math.pow(x, y);
            if (Integer.MIN_VALUE <= rslt && rslt <= Integer.MAX_VALUE) {
                return (int)rslt;
            }
            String msg = fmt("Integer overflow: pow(%d, %d).", x, y);
            throw new internal_functionsException(msg);
        }

        /**
         * Returns the exponentiation (power) of two real numbers.
         *
         * @param x The base real number.
         * @param y The exponent real number.
         * @return {@code pow(x, y)}.
         * @throws internal_functionsException If the operation results in real overflow,
         *      or {@code NaN}.
         */
        public static double powReal(double x, double y) {
            // Assumes that the arguments are valid doubles (no inf/NaN/-0.0).
            double rslt = Math.pow(x, y);
            if (Double.isInfinite(rslt)) {
                String msg = fmt("Real overflow: pow(%s, %s).",
                                 realToStr(x), realToStr(y));
                throw new internal_functionsException(msg);
            }
            if (Double.isNaN(rslt)) {
                String msg = fmt("Invalid operation: pow(%s, %s).",
                                 realToStr(x), realToStr(y));
                throw new internal_functionsException(msg);
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
         * @throws internal_functionsException If the index is out of range for the list.
         */
        public static <T> T project(List<T> lst, int origIdx) {
            // Normalize index and check for out of bounds.
            int idx = origIdx;
            if (idx < 0) idx = lst.size() + idx;
            if (idx < 0 || idx >= lst.size()) {
                String msg = fmt("Index out of bounds: %s[%s].",
                                 valueToStr(lst), origIdx);
                throw new internal_functionsException(msg);
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
         * @throws internal_functionsException If the index is out of range for the
         *      string.
         */
        public static String project(String str, int origIdx) {
            int idx = origIdx;
            if (idx < 0) idx = str.length() + idx;
            if (idx < 0 || idx >= str.length()) {
                String msg = fmt("Index out of bounds: \"%s\"[%s].",
                                 escape(str), origIdx);
                throw new internal_functionsException(msg);
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
         * @throws internal_functionsException Always thrown.
         */
        public static void rangeErrInt(String name, String value, String type) {
            String msg = fmt("Variable \"%s\" is assigned value \"%s\", which " +
                             "violates the integer type bounds of the type " +
                             "\"%s\" of that variable.", name, value, type);
            throw new internal_functionsException(msg);
        }

        /**
         * Returns the round of a real number.
         *
         * @param x The real number.
         * @return {@code round(x)}.
         * @throws internal_functionsException If the operation results in integer
         *      overflow.
         */
        public static int round(double x) {
            if (x < Integer.MIN_VALUE - 0.5 || x >= Integer.MAX_VALUE + 0.5) {
                String msg = fmt("Integer overflow: round(%s).", realToStr(x));
                throw new internal_functionsException(msg);
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
         * @throws internal_functionsException If the input interval is empty, or the
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
                throw new internal_functionsException(msg);
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
         * @throws internal_functionsException If the operation results in real overflow,
         *      or {@code NaN}.
         */
        public static double sin(double x) {
            double rslt = Math.sin(x);
            if (Double.isInfinite(rslt)) {
                String msg = fmt("Real overflow: sin(%s).", realToStr(x));
                throw new internal_functionsException(msg);
            }
            if (Double.isNaN(rslt)) {
                String msg = fmt("Invalid operation: sin(%s).", realToStr(x));
                throw new internal_functionsException(msg);
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
         * @throws internal_functionsException If the real number is negative.
         */
        public static double sqrt(double x) {
            // Assumes that the argument is never -0.0.
            if (x < 0.0) {
                String msg = fmt("Invalid operation: sqrt(%s).", realToStr(x));
                throw new internal_functionsException(msg);
            }
            return Math.sqrt(x);
        }

        /**
         * Converts a CIF boolean value literal, in the CIF ASCII representation,
         * to a Java {@link Boolean}.
         *
         * @param x The CIF boolean value literal, in the CIF ASCII representation.
         * @return The Java {@link Boolean} value.
         * @throws internal_functionsException If the string value does not represent a
         *      boolean value.
         */
        public static boolean strToBool(String x) {
            if (x.equals("true")) return true;
            if (x.equals("false")) return false;

            String msg = fmt("Cast from type \"string\" to type \"bool\" " +
                             "failed: the string value does not represent a " +
                             "boolean value: \"%s\".", escape(x));
            throw new internal_functionsException(msg);
        }

        /**
         * Converts a CIF integer value literal, in the CIF ASCII representation,
         * to a Java {@link Integer}.
         *
         * <p>See also the {@code CifTypeChecker.transIntExpression} method.</p>
         *
         * @param x The CIF integer value literal, in the CIF ASCII representation.
         * @return The Java {@link Integer} value.
         * @throws internal_functionsException If the string value does not represent an
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
                throw new internal_functionsException(msg);
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
         * @throws internal_functionsException If the string value does not represent an
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
                throw new internal_functionsException(msg);
            }

            if (Double.isInfinite(rslt)) {
                String msg = fmt("Cast from type \"string\" to type \"real\" " +
                                 "failed, due to real overflow: \"%s\".",
                                 escape(x));
                throw new internal_functionsException(msg);
            }

            return (rslt == -0.0) ? 0.0 : rslt;
        }

        /**
         * Returns the subtraction of two integer numbers.
         *
         * @param x The first integer number.
         * @param y The second integer number.
         * @return {@code x - y}.
         * @throws internal_functionsException If the operation results in integer
         *      overflow.
         */
        public static int subtract(int x, int y) {
            long rslt = (long)x - (long)y;
            if (Integer.MIN_VALUE <= rslt && rslt <= Integer.MAX_VALUE) {
                return (int)rslt;
            }

            String msg = fmt("Integer overflow: %d - %d.", x, y);
            throw new internal_functionsException(msg);
        }

        /**
         * Returns the subtraction of two real numbers.
         *
         * @param x The first real number.
         * @param y The second real number.
         * @return {@code x - y}.
         * @throws internal_functionsException If the operation results in real overflow.
         */
        public static double subtract(double x, double y) {
            double rslt = x - y;
            if (Double.isInfinite(rslt)) {
                String msg = fmt("Real overflow: %s - %s.",
                                 realToStr(x), realToStr(y));
                throw new internal_functionsException(msg);
            }
            return (rslt == -0.0) ? 0.0 : rslt;
        }

        /**
         * Returns the tangent of a real number.
         *
         * @param x The real number.
         * @return {@code tan(x)}.
         * @throws internal_functionsException If the operation results in real overflow,
         *      or {@code NaN}.
         */
        public static double tan(double x) {
            double rslt = Math.tan(x);
            if (Double.isInfinite(rslt)) {
                String msg = fmt("Real overflow: tan(%s).", realToStr(x));
                throw new internal_functionsException(msg);
            }
            if (Double.isNaN(rslt)) {
                String msg = fmt("Invalid operation: tan(%s).", realToStr(x));
                throw new internal_functionsException(msg);
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

            } else if (value instanceof internal_functions.CifTupleBase) {
                return value.toString();

            } else {
                throw new RuntimeException("Unsupported value: " + value);
            }
        }
    }
}
