package edges_java;

import static edges_java.edges.edgesUtils.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/** edges code generated from a CIF specification. */
@SuppressWarnings("unused")
public abstract class edges {
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
        "e02a",
        "e02b",
        "e03a",
        "e03b",
        "e04a",
        "e04b",
        "e04c",
        "e04d",
        "e04e",
        "e04f",
        "e05a",
        "e05b",
        "e05c",
        "e05d",
        "e05e",
        "e06a",
        "e06b",
        "e06c",
        "e06d",
        "e06e",
        "e07a",
        "e07b",
        "e08a",
        "e08b",
        "e08c",
        "e08d",
        "e08e",
        "e08f",
        "e08g",
        "e08h",
        "e09a",
        "e09b",
        "e09c",
        "e09d",
        "e09e",
        "e09f",
        "e09g",
        "e10a",
        "e10b",
        "e10c",
        "e10d",
        "e10e",
        "e10f",
        "e10g",
        "e10h",
        "e10i",
        "e11a",
        "e12a",
        "e12b",
        "e12c",
        "e12d",
        "e12e",
        "e13a",
        "e13b",
        "e13c",
        "e13d",
        "e13e",
        "e14a",
        "e14b",
        "e14c",
        "e14d",
        "e14e",
        "e14f",
        "e14g",
        "e14h",
    };


    /** Variable 'time'. */
    public double time;

    /** Discrete variable "aut02.x". */
    public int aut02_x_;

    /** Discrete variable "aut02". */
    public edgesEnum aut02_;

    /** Continuous variable "aut03.c". */
    public double aut03_c_;

    /** Discrete variable "aut03.d". */
    public int aut03_d_;

    /** Discrete variable "aut04.a". */
    public int aut04_a_;

    /** Discrete variable "aut04.b". */
    public int aut04_b_;

    /** Discrete variable "aut04.c". */
    public int aut04_c_;

    /** Discrete variable "aut04.d". */
    public int aut04_d_;

    /** Discrete variable "aut05.v1". */
    public List<Integer> aut05_v1_;

    /** Discrete variable "aut05.v2". */
    public List<Integer> aut05_v2_;

    /** Discrete variable "aut06.v1". */
    public CifTuple_T2II aut06_v1_;

    /** Discrete variable "aut06.v2". */
    public CifTuple_T2II aut06_v2_;

    /** Discrete variable "aut06.x". */
    public int aut06_x_;

    /** Discrete variable "aut06.y". */
    public int aut06_y_;

    /** Continuous variable "aut07.x". */
    public double aut07_x_;

    /** Continuous variable "aut07.y". */
    public double aut07_y_;

    /** Discrete variable "aut08.tt1". */
    public CifTuple_T2T2IIS aut08_tt1_;

    /** Discrete variable "aut08.tt2". */
    public CifTuple_T2T2IIS aut08_tt2_;

    /** Discrete variable "aut08.t". */
    public CifTuple_T2II aut08_t_;

    /** Discrete variable "aut08.i". */
    public int aut08_i_;

    /** Discrete variable "aut08.j". */
    public int aut08_j_;

    /** Discrete variable "aut08.s". */
    public String aut08_s_;

    /** Discrete variable "aut09.ll1". */
    public List<List<Integer>> aut09_ll1_;

    /** Discrete variable "aut09.ll2". */
    public List<List<Integer>> aut09_ll2_;

    /** Discrete variable "aut09.l". */
    public List<Integer> aut09_l_;

    /** Discrete variable "aut09.i". */
    public int aut09_i_;

    /** Discrete variable "aut09.j". */
    public int aut09_j_;

    /** Discrete variable "aut10.x1". */
    public CifTuple_T2SLT2LILR aut10_x1_;

    /** Discrete variable "aut10.x2". */
    public CifTuple_T2SLT2LILR aut10_x2_;

    /** Discrete variable "aut10.l". */
    public List<CifTuple_T2LILR> aut10_l_;

    /** Discrete variable "aut10.li". */
    public List<Integer> aut10_li_;

    /** Discrete variable "aut10.lr". */
    public List<Double> aut10_lr_;

    /** Discrete variable "aut10.i". */
    public int aut10_i_;

    /** Discrete variable "aut10.r". */
    public double aut10_r_;

    /** Discrete variable "aut11.v1". */
    public List<CifTuple_T2II> aut11_v1_;

    /** Discrete variable "aut12.x". */
    public double aut12_x_;

    /** Discrete variable "aut12.y". */
    public double aut12_y_;

    /** Discrete variable "aut12.z". */
    public double aut12_z_;

    /** Discrete variable "aut12.td". */
    public double aut12_td_;

    /** Continuous variable "aut12.t". */
    public double aut12_t_;

    /** Continuous variable "aut12.u". */
    public double aut12_u_;

    /** Discrete variable "aut13.x". */
    public double aut13_x_;

    /** Discrete variable "aut13.y". */
    public double aut13_y_;

    /** Discrete variable "aut13.z". */
    public double aut13_z_;

    /** Input variable "aut14.b". */
    public boolean aut14_b_;

    /** Input variable "aut14.i". */
    public int aut14_i_;

    /** Input variable "aut14.r". */
    public double aut14_r_;

    /** Constructor for the {@link edges} class. */
    public edges() {
        firstExec = true;
        this.time = 0.0;
    }

    /**
     * Execute the code once. Inputs are read, transitions are executed until
     * none are possible, outputs are written, etc.
     *
     * @param newTime The time in seconds, since the start of the first
     *      execution.
     * @throws edgesException In case of a runtime error caused by code
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
            double deriv0 = aut03_c_deriv();
            double deriv1 = aut07_x_deriv();
            double deriv2 = aut07_y_deriv();
            double deriv3 = aut12_t_deriv();
            double deriv4 = aut12_u_deriv();

            aut03_c_ = aut03_c_ + delta * deriv0;
            checkDouble(aut03_c_, "aut03.c");
            if (aut03_c_ == -0.0) aut03_c_ = 0.0;
            aut07_x_ = aut07_x_ + delta * deriv1;
            checkDouble(aut07_x_, "aut07.x");
            if (aut07_x_ == -0.0) aut07_x_ = 0.0;
            aut07_y_ = aut07_y_ + delta * deriv2;
            checkDouble(aut07_y_, "aut07.y");
            if (aut07_y_ == -0.0) aut07_y_ = 0.0;
            aut12_t_ = aut12_t_ + delta * deriv3;
            checkDouble(aut12_t_, "aut12.t");
            if (aut12_t_ == -0.0) aut12_t_ = 0.0;
            aut12_u_ = aut12_u_ + delta * deriv4;
            checkDouble(aut12_u_, "aut12.u");
            if (aut12_u_ == -0.0) aut12_u_ = 0.0;
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
            // Event "e02a".
            if (execEvent0()) continue;

            // Event "e02b".
            if (execEvent1()) continue;

            // Event "e03a".
            if (execEvent2()) continue;

            // Event "e03b".
            if (execEvent3()) continue;

            // Event "e04a".
            if (execEvent4()) continue;

            // Event "e04b".
            if (execEvent5()) continue;

            // Event "e04c".
            if (execEvent6()) continue;

            // Event "e04d".
            if (execEvent7()) continue;

            // Event "e04e".
            if (execEvent8()) continue;

            // Event "e04f".
            if (execEvent9()) continue;

            // Event "e05a".
            if (execEvent10()) continue;

            // Event "e05b".
            if (execEvent11()) continue;

            // Event "e05c".
            if (execEvent12()) continue;

            // Event "e05d".
            if (execEvent13()) continue;

            // Event "e05e".
            if (execEvent14()) continue;

            // Event "e06a".
            if (execEvent15()) continue;

            // Event "e06b".
            if (execEvent16()) continue;

            // Event "e06c".
            if (execEvent17()) continue;

            // Event "e06d".
            if (execEvent18()) continue;

            // Event "e06e".
            if (execEvent19()) continue;

            // Event "e07a".
            if (execEvent20()) continue;

            // Event "e07b".
            if (execEvent21()) continue;

            // Event "e08a".
            if (execEvent22()) continue;

            // Event "e08b".
            if (execEvent23()) continue;

            // Event "e08c".
            if (execEvent24()) continue;

            // Event "e08d".
            if (execEvent25()) continue;

            // Event "e08e".
            if (execEvent26()) continue;

            // Event "e08f".
            if (execEvent27()) continue;

            // Event "e08g".
            if (execEvent28()) continue;

            // Event "e08h".
            if (execEvent29()) continue;

            // Event "e09a".
            if (execEvent30()) continue;

            // Event "e09b".
            if (execEvent31()) continue;

            // Event "e09c".
            if (execEvent32()) continue;

            // Event "e09d".
            if (execEvent33()) continue;

            // Event "e09e".
            if (execEvent34()) continue;

            // Event "e09f".
            if (execEvent35()) continue;

            // Event "e09g".
            if (execEvent36()) continue;

            // Event "e10a".
            if (execEvent37()) continue;

            // Event "e10b".
            if (execEvent38()) continue;

            // Event "e10c".
            if (execEvent39()) continue;

            // Event "e10d".
            if (execEvent40()) continue;

            // Event "e10e".
            if (execEvent41()) continue;

            // Event "e10f".
            if (execEvent42()) continue;

            // Event "e10g".
            if (execEvent43()) continue;

            // Event "e10h".
            if (execEvent44()) continue;

            // Event "e10i".
            if (execEvent45()) continue;

            // Event "e11a".
            if (execEvent46()) continue;

            // Event "e12a".
            if (execEvent47()) continue;

            // Event "e12b".
            if (execEvent48()) continue;

            // Event "e12c".
            if (execEvent49()) continue;

            // Event "e12d".
            if (execEvent50()) continue;

            // Event "e12e".
            if (execEvent51()) continue;

            // Event "e13a".
            if (execEvent52()) continue;

            // Event "e13b".
            if (execEvent53()) continue;

            // Event "e13c".
            if (execEvent54()) continue;

            // Event "e13d".
            if (execEvent55()) continue;

            // Event "e13e".
            if (execEvent56()) continue;

            // Event "e14a".
            if (execEvent57()) continue;

            // Event "e14b".
            if (execEvent58()) continue;

            // Event "e14c".
            if (execEvent59()) continue;

            // Event "e14d".
            if (execEvent60()) continue;

            // Event "e14e".
            if (execEvent61()) continue;

            // Event "e14f".
            if (execEvent62()) continue;

            // Event "e14g".
            if (execEvent63()) continue;

            // Event "e14h".
            if (execEvent64()) continue;

            // Event "tau".
            if (execEvent65()) continue;

            // Event "tau".
            if (execEvent66()) continue;

            // Event "tau".
            if (execEvent67()) continue;

            // Event "tau".
            if (execEvent68()) continue;

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
     * @throws edgesException In case of a runtime error caused by code
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
     * Execute code for event "e02a".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent0() {
        boolean guard = ((aut02_) == (edgesEnum._loc1)) || (((aut02_) == (edgesEnum._loc2)) || ((aut02_) == (edgesEnum._loc3)));
        if (!guard) return false;

        if (doInfoPrintOutput) printOutput(0, true);
        if (doInfoEvent) infoEvent(0, true);

        if ((aut02_) == (edgesEnum._loc1)) {
            aut02_ = edgesEnum._loc2;
        } else if ((aut02_) == (edgesEnum._loc2)) {
            aut02_ = edgesEnum._loc3;
        } else if ((aut02_) == (edgesEnum._loc3)) {
            aut02_ = edgesEnum._loc1;
        }

        if (doInfoEvent) infoEvent(0, false);
        if (doInfoPrintOutput) printOutput(0, false);
        return true;
    }

    /**
     * Execute code for event "e02b".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent1() {
        boolean guard = (((aut02_) == (edgesEnum._loc1)) && (equalObjs(aut02_x_, 2))) || (((aut02_) == (edgesEnum._loc2)) || (((aut02_) == (edgesEnum._loc3)) && (equalObjs(aut02_x_, 3))));
        if (!guard) return false;

        if (doInfoPrintOutput) printOutput(1, true);
        if (doInfoEvent) infoEvent(1, true);

        if (((aut02_) == (edgesEnum._loc1)) && (equalObjs(aut02_x_, 2))) {
            aut02_ = edgesEnum._loc1;
        } else if ((aut02_) == (edgesEnum._loc2)) {
            aut02_x_ = 1;
        } else if (((aut02_) == (edgesEnum._loc3)) && (equalObjs(aut02_x_, 3))) {
            aut02_x_ = 1;
        }

        if (doInfoEvent) infoEvent(1, false);
        if (doInfoPrintOutput) printOutput(1, false);
        return true;
    }

    /**
     * Execute code for event "e03a".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent2() {
        if (doInfoPrintOutput) printOutput(2, true);
        if (doInfoEvent) infoEvent(2, true);

        aut03_c_ = 1.23;

        if (doInfoEvent) infoEvent(2, false);
        if (doInfoPrintOutput) printOutput(2, false);
        return true;
    }

    /**
     * Execute code for event "e03b".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent3() {
        if (doInfoPrintOutput) printOutput(3, true);
        if (doInfoEvent) infoEvent(3, true);

        aut03_d_ = 2;

        if (doInfoEvent) infoEvent(3, false);
        if (doInfoPrintOutput) printOutput(3, false);
        return true;
    }

    /**
     * Execute code for event "e04a".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent4() {
        if (doInfoPrintOutput) printOutput(4, true);
        if (doInfoEvent) infoEvent(4, true);

        if (equalObjs(aut04_a_, 1)) {
            aut04_b_ = 2;
        }

        if (doInfoEvent) infoEvent(4, false);
        if (doInfoPrintOutput) printOutput(4, false);
        return true;
    }

    /**
     * Execute code for event "e04b".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent5() {
        if (doInfoPrintOutput) printOutput(5, true);
        if (doInfoEvent) infoEvent(5, true);

        if (equalObjs(aut04_a_, 1)) {
            aut04_b_ = 2;
        } else if (equalObjs(aut04_a_, 2)) {
            aut04_b_ = 3;
        }

        if (doInfoEvent) infoEvent(5, false);
        if (doInfoPrintOutput) printOutput(5, false);
        return true;
    }

    /**
     * Execute code for event "e04c".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent6() {
        if (doInfoPrintOutput) printOutput(6, true);
        if (doInfoEvent) infoEvent(6, true);

        if (equalObjs(aut04_a_, 1)) {
            aut04_b_ = 2;
        } else if (equalObjs(aut04_a_, 2)) {
            aut04_b_ = 3;
        } else if (equalObjs(aut04_a_, 3)) {
            aut04_b_ = 4;
        }

        if (doInfoEvent) infoEvent(6, false);
        if (doInfoPrintOutput) printOutput(6, false);
        return true;
    }

    /**
     * Execute code for event "e04d".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent7() {
        if (doInfoPrintOutput) printOutput(7, true);
        if (doInfoEvent) infoEvent(7, true);

        if (equalObjs(aut04_a_, 1)) {
            aut04_b_ = 2;
        } else if (equalObjs(aut04_a_, 2)) {
            aut04_b_ = 3;
        } else if (equalObjs(aut04_a_, 3)) {
            aut04_b_ = 4;
        } else {
            aut04_b_ = 5;
        }

        if (doInfoEvent) infoEvent(7, false);
        if (doInfoPrintOutput) printOutput(7, false);
        return true;
    }

    /**
     * Execute code for event "e04e".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent8() {
        if (doInfoPrintOutput) printOutput(8, true);
        if (doInfoEvent) infoEvent(8, true);

        if (equalObjs(aut04_a_, 1)) {
            aut04_b_ = 2;
        } else {
            aut04_b_ = 5;
        }

        if (doInfoEvent) infoEvent(8, false);
        if (doInfoPrintOutput) printOutput(8, false);
        return true;
    }

    /**
     * Execute code for event "e04f".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent9() {
        if (doInfoPrintOutput) printOutput(9, true);
        if (doInfoEvent) infoEvent(9, true);

        if (equalObjs(aut04_a_, 1)) {
            aut04_b_ = 2;
        } else {
            aut04_b_ = 5;
        }
        if (equalObjs(aut04_a_, 1)) {
            aut04_c_ = 2;
        } else {
            aut04_d_ = 5;
        }

        if (doInfoEvent) infoEvent(9, false);
        if (doInfoPrintOutput) printOutput(9, false);
        return true;
    }

    /**
     * Execute code for event "e05a".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent10() {
        if (doInfoPrintOutput) printOutput(10, true);
        if (doInfoEvent) infoEvent(10, true);

        {
            int rhs1 = 3;
            int index2 = 0;
            aut05_v1_ = modify(aut05_v1_, index2, rhs1);
        }
        {
            int rhs1 = 4;
            int index2 = 1;
            aut05_v1_ = modify(aut05_v1_, index2, rhs1);
        }

        if (doInfoEvent) infoEvent(10, false);
        if (doInfoPrintOutput) printOutput(10, false);
        return true;
    }

    /**
     * Execute code for event "e05b".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent11() {
        if (doInfoPrintOutput) printOutput(11, true);
        if (doInfoEvent) infoEvent(11, true);

        {
            int rhs1 = 3;
            int index2 = 0;
            aut05_v1_ = modify(aut05_v1_, index2, rhs1);
        }
        {
            int rhs1 = 4;
            int index2 = 1;
            aut05_v1_ = modify(aut05_v1_, index2, rhs1);
        }

        if (doInfoEvent) infoEvent(11, false);
        if (doInfoPrintOutput) printOutput(11, false);
        return true;
    }

    /**
     * Execute code for event "e05c".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent12() {
        if (doInfoPrintOutput) printOutput(12, true);
        if (doInfoEvent) infoEvent(12, true);

        aut05_v1_ = aut05_v2_;

        if (doInfoEvent) infoEvent(12, false);
        if (doInfoPrintOutput) printOutput(12, false);
        return true;
    }

    /**
     * Execute code for event "e05d".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent13() {
        if (doInfoPrintOutput) printOutput(13, true);
        if (doInfoEvent) infoEvent(13, true);

        {
            int rhs1 = project(aut05_v2_, 0);
            int index2 = 0;
            aut05_v1_ = modify(aut05_v1_, index2, rhs1);
        }
        {
            int rhs1 = project(aut05_v2_, 1);
            int index2 = 1;
            aut05_v1_ = modify(aut05_v1_, index2, rhs1);
        }

        if (doInfoEvent) infoEvent(13, false);
        if (doInfoPrintOutput) printOutput(13, false);
        return true;
    }

    /**
     * Execute code for event "e05e".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent14() {
        if (doInfoPrintOutput) printOutput(14, true);
        if (doInfoEvent) infoEvent(14, true);

        {
            int rhs1 = project(aut05_v2_, 1);
            int index2 = 0;
            aut05_v1_ = modify(aut05_v1_, index2, rhs1);
        }
        {
            int rhs1 = project(aut05_v2_, 0);
            int index2 = 1;
            aut05_v1_ = modify(aut05_v1_, index2, rhs1);
        }

        if (doInfoEvent) infoEvent(14, false);
        if (doInfoPrintOutput) printOutput(14, false);
        return true;
    }

    /**
     * Execute code for event "e06a".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent15() {
        if (doInfoPrintOutput) printOutput(15, true);
        if (doInfoEvent) infoEvent(15, true);

        aut06_v1_ = new CifTuple_T2II(3, 4);

        if (doInfoEvent) infoEvent(15, false);
        if (doInfoPrintOutput) printOutput(15, false);
        return true;
    }

    /**
     * Execute code for event "e06b".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent16() {
        if (doInfoPrintOutput) printOutput(16, true);
        if (doInfoEvent) infoEvent(16, true);

        {
            int rhs1 = 5;
            aut06_v1_ = aut06_v1_.copy();
            aut06_v1_._field0 = rhs1;
        }

        if (doInfoEvent) infoEvent(16, false);
        if (doInfoPrintOutput) printOutput(16, false);
        return true;
    }

    /**
     * Execute code for event "e06c".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent17() {
        if (doInfoPrintOutput) printOutput(17, true);
        if (doInfoEvent) infoEvent(17, true);

        {
            CifTuple_T2II rhs1 = aut06_v1_;
            aut06_x_ = (rhs1)._field0;
            aut06_y_ = (rhs1)._field1;
        }

        if (doInfoEvent) infoEvent(17, false);
        if (doInfoPrintOutput) printOutput(17, false);
        return true;
    }

    /**
     * Execute code for event "e06d".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent18() {
        if (doInfoPrintOutput) printOutput(18, true);
        if (doInfoEvent) infoEvent(18, true);

        aut06_v1_ = new CifTuple_T2II(addInt(aut06_x_, 1), multiply(aut06_y_, 2));

        if (doInfoEvent) infoEvent(18, false);
        if (doInfoPrintOutput) printOutput(18, false);
        return true;
    }

    /**
     * Execute code for event "e06e".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent19() {
        if (doInfoPrintOutput) printOutput(19, true);
        if (doInfoEvent) infoEvent(19, true);

        aut06_v1_ = aut06_v2_;

        if (doInfoEvent) infoEvent(19, false);
        if (doInfoPrintOutput) printOutput(19, false);
        return true;
    }

    /**
     * Execute code for event "e07a".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent20() {
        if (doInfoPrintOutput) printOutput(20, true);
        if (doInfoEvent) infoEvent(20, true);

        aut07_x_ = 5.0;

        if (doInfoEvent) infoEvent(20, false);
        if (doInfoPrintOutput) printOutput(20, false);
        return true;
    }

    /**
     * Execute code for event "e07b".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent21() {
        if (doInfoPrintOutput) printOutput(21, true);
        if (doInfoEvent) infoEvent(21, true);

        aut07_y_ = aut07_x_;
        aut07_x_ = 5.0;

        if (doInfoEvent) infoEvent(21, false);
        if (doInfoPrintOutput) printOutput(21, false);
        return true;
    }

    /**
     * Execute code for event "e08a".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent22() {
        if (doInfoPrintOutput) printOutput(22, true);
        if (doInfoEvent) infoEvent(22, true);

        aut08_tt1_ = new CifTuple_T2T2IIS(new CifTuple_T2II(1, 2), "abc");

        if (doInfoEvent) infoEvent(22, false);
        if (doInfoPrintOutput) printOutput(22, false);
        return true;
    }

    /**
     * Execute code for event "e08b".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent23() {
        if (doInfoPrintOutput) printOutput(23, true);
        if (doInfoEvent) infoEvent(23, true);

        aut08_tt1_ = aut08_tt2_;

        if (doInfoEvent) infoEvent(23, false);
        if (doInfoPrintOutput) printOutput(23, false);
        return true;
    }

    /**
     * Execute code for event "e08c".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent24() {
        if (doInfoPrintOutput) printOutput(24, true);
        if (doInfoEvent) infoEvent(24, true);

        {
            CifTuple_T2II rhs1 = aut08_t_;
            aut08_tt1_ = aut08_tt1_.copy();
            aut08_tt1_._field0 = rhs1;
        }

        if (doInfoEvent) infoEvent(24, false);
        if (doInfoPrintOutput) printOutput(24, false);
        return true;
    }

    /**
     * Execute code for event "e08d".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent25() {
        if (doInfoPrintOutput) printOutput(25, true);
        if (doInfoEvent) infoEvent(25, true);

        {
            int rhs1 = 3;
            CifTuple_T2II part2 = (aut08_tt1_)._field0;
            part2 = part2.copy();
            part2._field1 = rhs1;
            aut08_tt1_ = aut08_tt1_.copy();
            aut08_tt1_._field0 = part2;
        }

        if (doInfoEvent) infoEvent(25, false);
        if (doInfoPrintOutput) printOutput(25, false);
        return true;
    }

    /**
     * Execute code for event "e08e".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent26() {
        if (doInfoPrintOutput) printOutput(26, true);
        if (doInfoEvent) infoEvent(26, true);

        {
            int rhs1 = 4;
            CifTuple_T2II part2 = (aut08_tt1_)._field0;
            part2 = part2.copy();
            part2._field0 = rhs1;
            aut08_tt1_ = aut08_tt1_.copy();
            aut08_tt1_._field0 = part2;
        }

        if (doInfoEvent) infoEvent(26, false);
        if (doInfoPrintOutput) printOutput(26, false);
        return true;
    }

    /**
     * Execute code for event "e08f".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent27() {
        if (doInfoPrintOutput) printOutput(27, true);
        if (doInfoEvent) infoEvent(27, true);

        {
            String rhs1 = "def";
            aut08_tt1_ = aut08_tt1_.copy();
            aut08_tt1_._field1 = rhs1;
        }

        if (doInfoEvent) infoEvent(27, false);
        if (doInfoPrintOutput) printOutput(27, false);
        return true;
    }

    /**
     * Execute code for event "e08g".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent28() {
        if (doInfoPrintOutput) printOutput(28, true);
        if (doInfoEvent) infoEvent(28, true);

        {
            CifTuple_T2II rhs1 = (aut08_tt1_)._field0;
            aut08_i_ = (rhs1)._field0;
            aut08_j_ = (rhs1)._field1;
        }

        if (doInfoEvent) infoEvent(28, false);
        if (doInfoPrintOutput) printOutput(28, false);
        return true;
    }

    /**
     * Execute code for event "e08h".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent29() {
        if (doInfoPrintOutput) printOutput(29, true);
        if (doInfoEvent) infoEvent(29, true);

        {
            CifTuple_T2T2IIS rhs1 = aut08_tt1_;
            aut08_i_ = (rhs1)._field0._field0;
            aut08_j_ = (rhs1)._field0._field1;
            aut08_s_ = (rhs1)._field1;
        }

        if (doInfoEvent) infoEvent(29, false);
        if (doInfoPrintOutput) printOutput(29, false);
        return true;
    }

    /**
     * Execute code for event "e09a".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent30() {
        if (doInfoPrintOutput) printOutput(30, true);
        if (doInfoEvent) infoEvent(30, true);

        aut09_ll1_ = makelist(new ArrayList<List<Integer>>(2), makelist(new ArrayList<Integer>(3), 1, 2, 3), makelist(new ArrayList<Integer>(3), 4, 5, 6));

        if (doInfoEvent) infoEvent(30, false);
        if (doInfoPrintOutput) printOutput(30, false);
        return true;
    }

    /**
     * Execute code for event "e09b".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent31() {
        if (doInfoPrintOutput) printOutput(31, true);
        if (doInfoEvent) infoEvent(31, true);

        aut09_ll1_ = aut09_ll2_;

        if (doInfoEvent) infoEvent(31, false);
        if (doInfoPrintOutput) printOutput(31, false);
        return true;
    }

    /**
     * Execute code for event "e09c".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent32() {
        if (doInfoPrintOutput) printOutput(32, true);
        if (doInfoEvent) infoEvent(32, true);

        {
            List<Integer> rhs1 = aut09_l_;
            int index2 = 0;
            aut09_ll1_ = modify(aut09_ll1_, index2, rhs1);
        }

        if (doInfoEvent) infoEvent(32, false);
        if (doInfoPrintOutput) printOutput(32, false);
        return true;
    }

    /**
     * Execute code for event "e09d".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent33() {
        if (doInfoPrintOutput) printOutput(33, true);
        if (doInfoEvent) infoEvent(33, true);

        {
            int rhs1 = 6;
            int index2 = 0;
            int index3 = 1;
            List<Integer> part4 = project(aut09_ll1_, index2);
            part4 = modify(part4, index3, rhs1);
            aut09_ll1_ = modify(aut09_ll1_, index2, part4);
        }

        if (doInfoEvent) infoEvent(33, false);
        if (doInfoPrintOutput) printOutput(33, false);
        return true;
    }

    /**
     * Execute code for event "e09e".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent34() {
        if (doInfoPrintOutput) printOutput(34, true);
        if (doInfoEvent) infoEvent(34, true);

        aut09_i_ = project(aut09_l_, 0);

        if (doInfoEvent) infoEvent(34, false);
        if (doInfoPrintOutput) printOutput(34, false);
        return true;
    }

    /**
     * Execute code for event "e09f".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent35() {
        if (doInfoPrintOutput) printOutput(35, true);
        if (doInfoEvent) infoEvent(35, true);

        aut09_i_ = project(project(aut09_ll1_, 0), 1);

        if (doInfoEvent) infoEvent(35, false);
        if (doInfoPrintOutput) printOutput(35, false);
        return true;
    }

    /**
     * Execute code for event "e09g".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent36() {
        if (doInfoPrintOutput) printOutput(36, true);
        if (doInfoEvent) infoEvent(36, true);

        aut09_ll1_ = makelist(new ArrayList<List<Integer>>(2), makelist(new ArrayList<Integer>(3), aut09_i_, aut09_j_, addInt(aut09_i_, aut09_j_)), makelist(new ArrayList<Integer>(3), negate(aut09_i_), negate(aut09_j_), subtract(negate(aut09_i_), aut09_j_)));

        if (doInfoEvent) infoEvent(36, false);
        if (doInfoPrintOutput) printOutput(36, false);
        return true;
    }

    /**
     * Execute code for event "e10a".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent37() {
        if (doInfoPrintOutput) printOutput(37, true);
        if (doInfoEvent) infoEvent(37, true);

        aut10_x1_ = aut10_x2_;

        if (doInfoEvent) infoEvent(37, false);
        if (doInfoPrintOutput) printOutput(37, false);
        return true;
    }

    /**
     * Execute code for event "e10b".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent38() {
        if (doInfoPrintOutput) printOutput(38, true);
        if (doInfoEvent) infoEvent(38, true);

        aut10_x1_ = new CifTuple_T2SLT2LILR("abc", makelist(new ArrayList<CifTuple_T2LILR>(2), new CifTuple_T2LILR(makelist(new ArrayList<Integer>(1), 1), makelist(new ArrayList<Double>(1), 2.0)), new CifTuple_T2LILR(makelist(new ArrayList<Integer>(1), aut10_i_), makelist(new ArrayList<Double>(1), aut10_r_))));

        if (doInfoEvent) infoEvent(38, false);
        if (doInfoPrintOutput) printOutput(38, false);
        return true;
    }

    /**
     * Execute code for event "e10c".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent39() {
        if (doInfoPrintOutput) printOutput(39, true);
        if (doInfoEvent) infoEvent(39, true);

        {
            String rhs1 = "def";
            aut10_x1_ = aut10_x1_.copy();
            aut10_x1_._field0 = rhs1;
        }
        {
            List<CifTuple_T2LILR> rhs1 = makelist(new ArrayList<CifTuple_T2LILR>(2), new CifTuple_T2LILR(makelist(new ArrayList<Integer>(1), 1), makelist(new ArrayList<Double>(1), 2.0)), new CifTuple_T2LILR(makelist(new ArrayList<Integer>(1), 3), makelist(new ArrayList<Double>(1), 4.0)));
            aut10_x1_ = aut10_x1_.copy();
            aut10_x1_._field1 = rhs1;
        }

        if (doInfoEvent) infoEvent(39, false);
        if (doInfoPrintOutput) printOutput(39, false);
        return true;
    }

    /**
     * Execute code for event "e10d".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent40() {
        if (doInfoPrintOutput) printOutput(40, true);
        if (doInfoEvent) infoEvent(40, true);

        {
            CifTuple_T2LILR rhs1 = new CifTuple_T2LILR(makelist(new ArrayList<Integer>(1), 4), makelist(new ArrayList<Double>(1), 5.0));
            int index2 = 0;
            List<CifTuple_T2LILR> part3 = (aut10_x1_)._field1;
            part3 = modify(part3, index2, rhs1);
            aut10_x1_ = aut10_x1_.copy();
            aut10_x1_._field1 = part3;
        }

        if (doInfoEvent) infoEvent(40, false);
        if (doInfoPrintOutput) printOutput(40, false);
        return true;
    }

    /**
     * Execute code for event "e10e".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent41() {
        if (doInfoPrintOutput) printOutput(41, true);
        if (doInfoEvent) infoEvent(41, true);

        {
            int rhs1 = 5;
            int index2 = 0;
            int index3 = 0;
            List<CifTuple_T2LILR> part4 = (aut10_x1_)._field1;
            CifTuple_T2LILR part5 = project(part4, index2);
            List<Integer> part6 = (part5)._field0;
            part6 = modify(part6, index3, rhs1);
            part5 = part5.copy();
            part5._field0 = part6;
            part4 = modify(part4, index2, part5);
            aut10_x1_ = aut10_x1_.copy();
            aut10_x1_._field1 = part4;
        }

        if (doInfoEvent) infoEvent(41, false);
        if (doInfoPrintOutput) printOutput(41, false);
        return true;
    }

    /**
     * Execute code for event "e10f".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent42() {
        if (doInfoPrintOutput) printOutput(42, true);
        if (doInfoEvent) infoEvent(42, true);

        aut10_l_ = (aut10_x1_)._field1;

        if (doInfoEvent) infoEvent(42, false);
        if (doInfoPrintOutput) printOutput(42, false);
        return true;
    }

    /**
     * Execute code for event "e10g".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent43() {
        if (doInfoPrintOutput) printOutput(43, true);
        if (doInfoEvent) infoEvent(43, true);

        aut10_li_ = (project((aut10_x1_)._field1, 0))._field0;

        if (doInfoEvent) infoEvent(43, false);
        if (doInfoPrintOutput) printOutput(43, false);
        return true;
    }

    /**
     * Execute code for event "e10h".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent44() {
        if (doInfoPrintOutput) printOutput(44, true);
        if (doInfoEvent) infoEvent(44, true);

        aut10_lr_ = (project((aut10_x1_)._field1, 0))._field1;

        if (doInfoEvent) infoEvent(44, false);
        if (doInfoPrintOutput) printOutput(44, false);
        return true;
    }

    /**
     * Execute code for event "e10i".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent45() {
        if (doInfoPrintOutput) printOutput(45, true);
        if (doInfoEvent) infoEvent(45, true);

        aut10_i_ = project((project((aut10_x1_)._field1, 0))._field0, 0);
        aut10_r_ = project((project((aut10_x1_)._field1, 0))._field1, 0);

        if (doInfoEvent) infoEvent(45, false);
        if (doInfoPrintOutput) printOutput(45, false);
        return true;
    }

    /**
     * Execute code for event "e11a".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent46() {
        if (doInfoPrintOutput) printOutput(46, true);
        if (doInfoEvent) infoEvent(46, true);

        if (equalObjs((project(aut11_v1_, 0))._field0, 1)) {
            int rhs1 = addInt((project(aut11_v1_, 0))._field0, 1);
            int index2 = 0;
            CifTuple_T2II part3 = project(aut11_v1_, index2);
            part3 = part3.copy();
            part3._field0 = rhs1;
            aut11_v1_ = modify(aut11_v1_, index2, part3);
        } else if (equalObjs((project(aut11_v1_, 0))._field0, 2)) {
            int rhs1 = subtract((project(aut11_v1_, 0))._field1, 1);
            int index2 = 0;
            CifTuple_T2II part3 = project(aut11_v1_, index2);
            part3 = part3.copy();
            part3._field1 = rhs1;
            aut11_v1_ = modify(aut11_v1_, index2, part3);
        } else {
            int rhs1 = (project(aut11_v1_, 2))._field0;
            int index2 = 1;
            CifTuple_T2II part3 = project(aut11_v1_, index2);
            part3 = part3.copy();
            part3._field0 = rhs1;
            aut11_v1_ = modify(aut11_v1_, index2, part3);
        }
        {
            int rhs1 = 3;
            int index2 = 2;
            CifTuple_T2II part3 = project(aut11_v1_, index2);
            part3 = part3.copy();
            part3._field0 = rhs1;
            aut11_v1_ = modify(aut11_v1_, index2, part3);
        }

        if (doInfoEvent) infoEvent(46, false);
        if (doInfoPrintOutput) printOutput(46, false);
        return true;
    }

    /**
     * Execute code for event "e12a".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent47() {
        if (doInfoPrintOutput) printOutput(47, true);
        if (doInfoEvent) infoEvent(47, true);

        aut12_z_ = aut12_v_();
        aut12_x_ = 1.0;
        aut12_y_ = 1.0;

        if (doInfoEvent) infoEvent(47, false);
        if (doInfoPrintOutput) printOutput(47, false);
        return true;
    }

    /**
     * Execute code for event "e12b".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent48() {
        if (doInfoPrintOutput) printOutput(48, true);
        if (doInfoEvent) infoEvent(48, true);

        {
            double aut12_v_tmp1 = aut12_v_();
            aut12_x_ = aut12_v_tmp1;
            aut12_y_ = aut12_v_tmp1;
        }

        if (doInfoEvent) infoEvent(48, false);
        if (doInfoPrintOutput) printOutput(48, false);
        return true;
    }

    /**
     * Execute code for event "e12c".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent49() {
        if (doInfoPrintOutput) printOutput(49, true);
        if (doInfoEvent) infoEvent(49, true);

        {
            double aut12_v_tmp1 = aut12_v_();
            aut12_td_ = aut12_w_();
            aut12_x_ = aut12_v_tmp1;
            aut12_y_ = aut12_v_tmp1;
        }

        if (doInfoEvent) infoEvent(49, false);
        if (doInfoPrintOutput) printOutput(49, false);
        return true;
    }

    /**
     * Execute code for event "e12d".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent50() {
        if (doInfoPrintOutput) printOutput(50, true);
        if (doInfoEvent) infoEvent(50, true);

        {
            double aut12_t_tmp1 = aut12_t_deriv();
            aut12_x_ = aut12_t_tmp1;
            aut12_y_ = aut12_t_tmp1;
        }

        if (doInfoEvent) infoEvent(50, false);
        if (doInfoPrintOutput) printOutput(50, false);
        return true;
    }

    /**
     * Execute code for event "e12e".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent51() {
        if (doInfoPrintOutput) printOutput(51, true);
        if (doInfoEvent) infoEvent(51, true);

        aut12_td_ = aut12_u_deriv();
        aut12_x_ = 1.0;
        aut12_y_ = 1.0;

        if (doInfoEvent) infoEvent(51, false);
        if (doInfoPrintOutput) printOutput(51, false);
        return true;
    }

    /**
     * Execute code for event "e13a".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent52() {
        if (doInfoPrintOutput) printOutput(52, true);
        if (doInfoEvent) infoEvent(52, true);

        aut13_x_ = 1.0;

        if (doInfoEvent) infoEvent(52, false);
        if (doInfoPrintOutput) printOutput(52, false);
        return true;
    }

    /**
     * Execute code for event "e13b".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent53() {
        if (doInfoPrintOutput) printOutput(53, true);
        if (doInfoEvent) infoEvent(53, true);

        if (equalObjs(aut13_z_, 5.0)) {
            aut13_x_ = 2.0;
        } else {
            aut13_x_ = 3.0;
        }

        if (doInfoEvent) infoEvent(53, false);
        if (doInfoPrintOutput) printOutput(53, false);
        return true;
    }

    /**
     * Execute code for event "e13c".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent54() {
        if (doInfoPrintOutput) printOutput(54, true);
        if (doInfoEvent) infoEvent(54, true);

        if (equalObjs(aut13_z_, 5.0)) {
            aut13_x_ = 2.0;
        } else if (equalObjs(aut13_z_, 21.0)) {
            aut13_x_ = 3.0;
        }

        if (doInfoEvent) infoEvent(54, false);
        if (doInfoPrintOutput) printOutput(54, false);
        return true;
    }

    /**
     * Execute code for event "e13d".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent55() {
        if (doInfoPrintOutput) printOutput(55, true);
        if (doInfoEvent) infoEvent(55, true);

        if (equalObjs(aut13_z_, 5.0)) {
            aut13_x_ = 2.0;
        } else if (equalObjs(aut13_z_, 21.0)) {
            aut13_x_ = 3.0;
        } else {
            aut13_x_ = 4.0;
        }

        if (doInfoEvent) infoEvent(55, false);
        if (doInfoPrintOutput) printOutput(55, false);
        return true;
    }

    /**
     * Execute code for event "e13e".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent56() {
        if (doInfoPrintOutput) printOutput(56, true);
        if (doInfoEvent) infoEvent(56, true);

        if (equalObjs(aut13_w_(), 4.0)) {
            aut13_x_ = 1.0;
        } else if (equalObjs(aut13_v_(), 5.0)) {
            aut13_x_ = 2.0;
        }

        if (doInfoEvent) infoEvent(56, false);
        if (doInfoPrintOutput) printOutput(56, false);
        return true;
    }

    /**
     * Execute code for event "e14a".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent57() {
        boolean guard = aut14_b_;
        if (!guard) return false;

        if (doInfoPrintOutput) printOutput(57, true);
        if (doInfoEvent) infoEvent(57, true);


        if (doInfoEvent) infoEvent(57, false);
        if (doInfoPrintOutput) printOutput(57, false);
        return true;
    }

    /**
     * Execute code for event "e14b".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent58() {
        boolean guard = (aut14_i_) > (3);
        if (!guard) return false;

        if (doInfoPrintOutput) printOutput(58, true);
        if (doInfoEvent) infoEvent(58, true);


        if (doInfoEvent) infoEvent(58, false);
        if (doInfoPrintOutput) printOutput(58, false);
        return true;
    }

    /**
     * Execute code for event "e14c".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent59() {
        boolean guard = !equalObjs(addReal(aut14_r_, aut14_i_), 18.0);
        if (!guard) return false;

        if (doInfoPrintOutput) printOutput(59, true);
        if (doInfoEvent) infoEvent(59, true);


        if (doInfoEvent) infoEvent(59, false);
        if (doInfoPrintOutput) printOutput(59, false);
        return true;
    }

    /**
     * Execute code for event "e14d".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent60() {
        boolean guard = !(aut14_b_);
        if (!guard) return false;

        if (doInfoPrintOutput) printOutput(60, true);
        if (doInfoEvent) infoEvent(60, true);


        if (doInfoEvent) infoEvent(60, false);
        if (doInfoPrintOutput) printOutput(60, false);
        return true;
    }

    /**
     * Execute code for event "e14e".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent61() {
        boolean guard = (negate(aut14_i_)) < (5);
        if (!guard) return false;

        if (doInfoPrintOutput) printOutput(61, true);
        if (doInfoEvent) infoEvent(61, true);


        if (doInfoEvent) infoEvent(61, false);
        if (doInfoPrintOutput) printOutput(61, false);
        return true;
    }

    /**
     * Execute code for event "e14f".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent62() {
        boolean guard = (negate(aut14_r_)) < (6);
        if (!guard) return false;

        if (doInfoPrintOutput) printOutput(62, true);
        if (doInfoEvent) infoEvent(62, true);


        if (doInfoEvent) infoEvent(62, false);
        if (doInfoPrintOutput) printOutput(62, false);
        return true;
    }

    /**
     * Execute code for event "e14g".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent63() {
        boolean guard = (aut14_i_) < (7);
        if (!guard) return false;

        if (doInfoPrintOutput) printOutput(63, true);
        if (doInfoEvent) infoEvent(63, true);


        if (doInfoEvent) infoEvent(63, false);
        if (doInfoPrintOutput) printOutput(63, false);
        return true;
    }

    /**
     * Execute code for event "e14h".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent64() {
        boolean guard = (aut14_r_) < (8);
        if (!guard) return false;

        if (doInfoPrintOutput) printOutput(64, true);
        if (doInfoEvent) infoEvent(64, true);


        if (doInfoEvent) infoEvent(64, false);
        if (doInfoPrintOutput) printOutput(64, false);
        return true;
    }

    /**
     * Execute code for event "tau".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent65() {
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
    private boolean execEvent66() {
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
    private boolean execEvent67() {
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
    private boolean execEvent68() {
        if (doInfoPrintOutput) printOutput(-1, true);
        if (doInfoEvent) infoEvent(-1, true);


        if (doInfoEvent) infoEvent(-1, false);
        if (doInfoPrintOutput) printOutput(-1, false);
        return true;
    }

    /** Initializes the state. */
    private void initState() {
        aut02_x_ = 0;
        aut02_ = edgesEnum._loc1;
        aut03_c_ = 0.0;
        aut03_d_ = 0;
        aut04_a_ = 0;
        aut04_b_ = 0;
        aut04_c_ = 0;
        aut04_d_ = 0;
        aut05_v1_ = makelist(new ArrayList<Integer>(5), 0, 0, 0, 0, 0);
        aut05_v2_ = makelist(new ArrayList<Integer>(5), 0, 0, 0, 0, 0);
        aut06_v1_ = new CifTuple_T2II(0, 0);
        aut06_v2_ = new CifTuple_T2II(0, 0);
        aut06_x_ = 0;
        aut06_y_ = 0;
        aut07_x_ = 0.0;
        aut07_y_ = 0.0;
        aut08_tt1_ = new CifTuple_T2T2IIS(new CifTuple_T2II(0, 0), "");
        aut08_tt2_ = new CifTuple_T2T2IIS(new CifTuple_T2II(0, 0), "");
        aut08_t_ = new CifTuple_T2II(0, 0);
        aut08_i_ = 0;
        aut08_j_ = 0;
        aut08_s_ = "";
        aut09_ll1_ = makelist(new ArrayList<List<Integer>>(2), makelist(new ArrayList<Integer>(3), 0, 0, 0), makelist(new ArrayList<Integer>(3), 0, 0, 0));
        aut09_ll2_ = makelist(new ArrayList<List<Integer>>(2), makelist(new ArrayList<Integer>(3), 0, 0, 0), makelist(new ArrayList<Integer>(3), 0, 0, 0));
        aut09_l_ = makelist(new ArrayList<Integer>(3), 0, 0, 0);
        aut09_i_ = 0;
        aut09_j_ = 0;
        aut10_x1_ = new CifTuple_T2SLT2LILR("", makelist(new ArrayList<CifTuple_T2LILR>(2), new CifTuple_T2LILR(makelist(new ArrayList<Integer>(1), 0), makelist(new ArrayList<Double>(1), 0.0)), new CifTuple_T2LILR(makelist(new ArrayList<Integer>(1), 0), makelist(new ArrayList<Double>(1), 0.0))));
        aut10_x2_ = new CifTuple_T2SLT2LILR("", makelist(new ArrayList<CifTuple_T2LILR>(2), new CifTuple_T2LILR(makelist(new ArrayList<Integer>(1), 0), makelist(new ArrayList<Double>(1), 0.0)), new CifTuple_T2LILR(makelist(new ArrayList<Integer>(1), 0), makelist(new ArrayList<Double>(1), 0.0))));
        aut10_l_ = makelist(new ArrayList<CifTuple_T2LILR>(2), new CifTuple_T2LILR(makelist(new ArrayList<Integer>(1), 0), makelist(new ArrayList<Double>(1), 0.0)), new CifTuple_T2LILR(makelist(new ArrayList<Integer>(1), 0), makelist(new ArrayList<Double>(1), 0.0)));
        aut10_li_ = makelist(new ArrayList<Integer>(1), 0);
        aut10_lr_ = makelist(new ArrayList<Double>(1), 0.0);
        aut10_i_ = 0;
        aut10_r_ = 0.0;
        aut11_v1_ = makelist(new ArrayList<CifTuple_T2II>(3), new CifTuple_T2II(0, 0), new CifTuple_T2II(0, 0), new CifTuple_T2II(0, 0));
        aut12_x_ = 0.0;
        aut12_y_ = 0.0;
        aut12_z_ = 0.0;
        aut12_td_ = 0.0;
        aut12_t_ = 0.0;
        aut12_u_ = 0.0;
        aut13_x_ = 0.0;
        aut13_y_ = 0.0;
        aut13_z_ = 0.0;
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
     * Evaluates algebraic variable "aut12.v".
     *
     * @return The evaluation result.
     */
    public double aut12_v_() {
        return addReal(aut12_x_, aut12_y_);
    }

    /**
     * Evaluates algebraic variable "aut12.w".
     *
     * @return The evaluation result.
     */
    public double aut12_w_() {
        return addReal(aut12_v_(), aut12_z_);
    }

    /**
     * Evaluates algebraic variable "aut13.v".
     *
     * @return The evaluation result.
     */
    public double aut13_v_() {
        return addReal(aut13_x_, aut13_y_);
    }

    /**
     * Evaluates algebraic variable "aut13.w".
     *
     * @return The evaluation result.
     */
    public double aut13_w_() {
        return addReal(aut13_v_(), aut13_z_);
    }

    /**
     * Evaluates derivative of continuous variable "aut03.c".
     *
     * @return The evaluation result.
     */
    public double aut03_c_deriv() {
        return 1.0;
    }

    /**
     * Evaluates derivative of continuous variable "aut07.x".
     *
     * @return The evaluation result.
     */
    public double aut07_x_deriv() {
        return 1.0;
    }

    /**
     * Evaluates derivative of continuous variable "aut07.y".
     *
     * @return The evaluation result.
     */
    public double aut07_y_deriv() {
        return 2.0;
    }

    /**
     * Evaluates derivative of continuous variable "aut12.t".
     *
     * @return The evaluation result.
     */
    public double aut12_t_deriv() {
        return addReal(aut12_x_, aut12_y_);
    }

    /**
     * Evaluates derivative of continuous variable "aut12.u".
     *
     * @return The evaluation result.
     */
    public double aut12_u_deriv() {
        return addReal(aut12_t_deriv(), aut12_z_);
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


    /** Tuple class for CIF tuple type representative "tuple(int a; int b)". */
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


    /** Tuple class for CIF tuple type representative "tuple(tuple(int a; int b) t; string c)". */
    public static class CifTuple_T2T2IIS extends CifTupleBase<CifTuple_T2T2IIS> {
        /** The 1st field. */
        public CifTuple_T2II _field0;

        /** The 2nd field. */
        public String _field1;

        /**
         * Constructor for the {@link CifTuple_T2T2IIS} class.
         *
         * @param _field0 The 1st field.
         * @param _field1 The 2nd field.
         */
        public CifTuple_T2T2IIS(CifTuple_T2II _field0, String _field1) {
            this._field0 = _field0;
            this._field1 = _field1;
        }

        @Override
        public CifTuple_T2T2IIS copy() {
            return new CifTuple_T2T2IIS(_field0, _field1);
        }

        @Override
        public int hashCode() {
            return hashObjs(_field0, _field1);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            CifTuple_T2T2IIS other = (CifTuple_T2T2IIS)obj;
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


    /** Tuple class for CIF tuple type representative "tuple(list[1] int x; list[1] real y)". */
    public static class CifTuple_T2LILR extends CifTupleBase<CifTuple_T2LILR> {
        /** The 1st field. */
        public List<Integer> _field0;

        /** The 2nd field. */
        public List<Double> _field1;

        /**
         * Constructor for the {@link CifTuple_T2LILR} class.
         *
         * @param _field0 The 1st field.
         * @param _field1 The 2nd field.
         */
        public CifTuple_T2LILR(List<Integer> _field0, List<Double> _field1) {
            this._field0 = _field0;
            this._field1 = _field1;
        }

        @Override
        public CifTuple_T2LILR copy() {
            return new CifTuple_T2LILR(_field0, _field1);
        }

        @Override
        public int hashCode() {
            return hashObjs(_field0, _field1);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            CifTuple_T2LILR other = (CifTuple_T2LILR)obj;
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


    /** Tuple class for CIF tuple type representative "tuple(string s; list[2] tuple(list[1] int x; list[1] real y) z)". */
    public static class CifTuple_T2SLT2LILR extends CifTupleBase<CifTuple_T2SLT2LILR> {
        /** The 1st field. */
        public String _field0;

        /** The 2nd field. */
        public List<CifTuple_T2LILR> _field1;

        /**
         * Constructor for the {@link CifTuple_T2SLT2LILR} class.
         *
         * @param _field0 The 1st field.
         * @param _field1 The 2nd field.
         */
        public CifTuple_T2SLT2LILR(String _field0, List<CifTuple_T2LILR> _field1) {
            this._field0 = _field0;
            this._field1 = _field1;
        }

        @Override
        public CifTuple_T2SLT2LILR copy() {
            return new CifTuple_T2SLT2LILR(_field0, _field1);
        }

        @Override
        public int hashCode() {
            return hashObjs(_field0, _field1);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            CifTuple_T2SLT2LILR other = (CifTuple_T2SLT2LILR)obj;
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
        // No print declarations.
    }

    /**
     * Informs that new print output is available.
     *
     * @param text The text being printed.
     * @param target The file or special target to which text is to be printed.
     *      If printed to a file, an absolute or relative local file system
     *      path is given. Paths may contain both {@code "/"} and {@code "\\"}
     *      as file separators. Use {@link edgesUtils#normalizePrintTarget}
     *      to normalize the path to use file separators for the current
     *      platform. There are two special targets: {@code ":stdout"} to print
     *      to the standard output stream, and {@code ":stderr"} to print to
     *      the standard error stream.
     */
    protected abstract void infoPrintOutput(String text, String target);

    /** edges enumeration. */
    public static enum edgesEnum {
        /** loc1 */ _loc1,
        /** loc2 */ _loc2,
        /** loc3 */ _loc3;

        @Override
        public String toString() {
            return name().substring(1);
        }
    }

    /**
     * edges exception.
     *
     * <p>Indices a runtime error while executing the generated code.</p>
     */
    public static class edgesException extends RuntimeException {
        /**
         * Constructor for the {@link edgesException} class.
         *
         * @param message The message describing the exception.
         */
        public edgesException(String message) {
            super(message);
        }

        /**
         * Constructor for the {@link edgesException} class.
         *
         * @param message The message describing the exception.
         * @param cause The root cause of the exception.
         */
        public edgesException(String message, Throwable cause) {
            super(message, cause);
        }

        @Override
        public String toString() {
            return getMessage();
        }
    }

    /** {@link edges} utility code. */
    public static class edgesUtils {
        /** The file separator for the current platform. */
        private static final String FILE_SEPARATOR = System.getProperty("file.separator");

        /** Constructor for the {@link edgesUtils} class. */
        private edgesUtils() {
            // Static class.
        }

        /**
         * Returns the absolute value of an integer number.
         *
         * @param x The integer number.
         * @return {@code abs(x)}.
         * @throws edgesException If the operation results in integer
         *      overflow.
         */
        public static int abs(int x) {
            if (x == Integer.MIN_VALUE) {
                String msg = fmt("Integer overflow: abs(%d).", x);
                throw new edgesException(msg);
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
         * @throws edgesException If the operation results in real overflow,
         *      or {@code NaN}.
         */
        public static double acos(double x) {
            double rslt = Math.acos(x);
            if (Double.isInfinite(rslt)) {
                String msg = fmt("Real overflow: acos(%s).", realToStr(x));
                throw new edgesException(msg);
            }
            if (Double.isNaN(rslt)) {
                String msg = fmt("Invalid operation: acos(%s).", realToStr(x));
                throw new edgesException(msg);
            }
            return (rslt == -0.0) ? 0.0 : rslt;
        }

        /**
         * Returns the arc sine of a real number.
         *
         * @param x The real number.
         * @return {@code asin(x)}.
         * @throws edgesException If the operation results in real overflow,
         *      or {@code NaN}.
         */
        public static double asin(double x) {
            double rslt = Math.asin(x);
            if (Double.isInfinite(rslt)) {
                String msg = fmt("Real overflow: asin(%s).", realToStr(x));
                throw new edgesException(msg);
            }
            if (Double.isNaN(rslt)) {
                String msg = fmt("Invalid operation: asin(%s).", realToStr(x));
                throw new edgesException(msg);
            }
            return (rslt == -0.0) ? 0.0 : rslt;
        }

        /**
         * Returns the arc tangent of a real number.
         *
         * @param x The real number.
         * @return {@code atan(x)}.
         * @throws edgesException If the operation results in real overflow,
         *      or {@code NaN}.
         */
        public static double atan(double x) {
            double rslt = Math.atan(x);
            if (Double.isInfinite(rslt)) {
                String msg = fmt("Real overflow: atan(%s).", realToStr(x));
                throw new edgesException(msg);
            }
            if (Double.isNaN(rslt)) {
                String msg = fmt("Invalid operation: atan(%s).", realToStr(x));
                throw new edgesException(msg);
            }
            return (rslt == -0.0) ? 0.0 : rslt;
        }

        /**
         * Returns the addition of two integer numbers.
         *
         * @param x The first integer number.
         * @param y The second integer number.
         * @return {@code x + y}.
         * @throws edgesException If the operation results in integer
         *      overflow.
         */
        public static int addInt(int x, int y) {
            long rslt = (long)x + (long)y;
            if (Integer.MIN_VALUE <= rslt && rslt <= Integer.MAX_VALUE) {
                return (int)rslt;
            }

            String msg = fmt("Integer overflow: %d + %d.", x, y);
            throw new edgesException(msg);
        }

        /**
         * Returns the addition of two real numbers.
         *
         * @param x The first real number.
         * @param y The second real number.
         * @return {@code x + y}.
         * @throws edgesException If the operation results in real overflow.
         */
        public static double addReal(double x, double y) {
            double rslt = x + y;
            if (Double.isInfinite(rslt)) {
                String msg = fmt("Real overflow: %s + %s.",
                                 realToStr(x), realToStr(y));
                throw new edgesException(msg);
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
         * @throws edgesException If the operation results in integer
         *      overflow.
         */
        public static int ceil(double x) {
            double rslt = Math.ceil(x);
            if (rslt < Integer.MIN_VALUE || rslt > Integer.MAX_VALUE) {
                String msg = fmt("Integer overflow: ceil(%s).", realToStr(x));
                throw new edgesException(msg);
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
            throw new edgesException(msg);
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
         * @throws edgesException If the operation results in real overflow,
         *      or {@code NaN}.
         */
        public static double cos(double x) {
            double rslt = Math.cos(x);
            if (Double.isInfinite(rslt)) {
                String msg = fmt("Real overflow: cos(%s).", realToStr(x));
                throw new edgesException(msg);
            }
            if (Double.isNaN(rslt)) {
                String msg = fmt("Invalid operation: cos(%s).", realToStr(x));
                throw new edgesException(msg);
            }
            return (rslt == -0.0) ? 0.0 : rslt;
        }

        /**
         * Returns the integer division of two integer numbers.
         *
         * @param x The dividend.
         * @param y The divisor.
         * @return {@code x div y}.
         * @throws edgesException If the operation results in integer
         *      overflow, or division by zero.
         */
        public static int div(int x, int y) {
            if (y == 0) {
                String msg = fmt("Division by zero: %d div %d.", x, y);
                throw new edgesException(msg);
            }
            if (x == Integer.MIN_VALUE && y == -1) {
                String msg = fmt("Integer overflow: %d div %d.", x, y);
                throw new edgesException(msg);
            }
            return x / y;
        }

        /**
         * Returns the real division of two real numbers.
         *
         * @param x The first real number.
         * @param y The second real number.
         * @return {@code x / y}.
         * @throws edgesException If the operation results in real overflow,
         *      or division by zero.
         */
        public static double divide(double x, double y) {
            if (y == 0.0) {
                String msg = fmt("Division by zero: %s / %s.",
                                 realToStr(x), realToStr(y));
                throw new edgesException(msg);
            }
            double rslt = x / y;
            if (Double.isInfinite(rslt)) {
                String msg = fmt("Real overflow: %s * %s.",
                                 realToStr(x), realToStr(y));
                throw new edgesException(msg);
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
         * @throws edgesException If the operation results in real overflow.
         */
        public static double exp(double x) {
            double rslt = Math.exp(x);
            if (Double.isInfinite(rslt)) {
                String msg = fmt("Real overflow: exp(%s).", realToStr(x));
                throw new edgesException(msg);
            }
            return (rslt == -0.0) ? 0.0 : rslt;
        }

        /**
         * Returns the floor of a real number.
         *
         * @param x The real number.
         * @return {@code floor(x)}.
         * @throws edgesException If the operation results in integer
         *      overflow.
         */
        public static int floor(double x) {
            double rslt = Math.floor(x);
            if (rslt < Integer.MIN_VALUE || rslt > Integer.MAX_VALUE) {
                String msg = fmt("Integer overflow: floor(%s).", realToStr(x));
                throw new edgesException(msg);
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
         * @throws edgesException If the real number is non-positive.
         */
        public static double ln(double x) {
            if (x <= 0.0) {
                String msg = fmt("Invalid operation: ln(%s).", realToStr(x));
                throw new edgesException(msg);
            }
            return Math.log(x);
        }

        /**
         * Returns the logarithm (base 10) of a real number.
         *
         * @param x The real number.
         * @return {@code log(x)}.
         * @throws edgesException If the real number is non-positive.
         */
        public static double log(double x) {
            if (x <= 0.0) {
                String msg = fmt("Invalid operation: log(%s).", realToStr(x));
                throw new edgesException(msg);
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
         * @throws edgesException If the operation results in division by
         *      zero.
         */
        public static int mod(int x, int y) {
            if (y == 0) {
                String msg = fmt("Division by zero: %d mod %d.", x, y);
                throw new edgesException(msg);
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
         * @throws edgesException If the index is out of range for the list.
         */
        public static <T> List<T> modify(List<T> lst, int origIdx, T newValue) {
            // Normalize index and check for out of bounds.
            int idx = origIdx;
            if (idx < 0) idx = lst.size() + idx;
            if (idx < 0 || idx >= lst.size()) {
                String msg = fmt("Index out of bounds: %s[%s].",
                                 valueToStr(lst), origIdx);
                throw new edgesException(msg);
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
         * @throws edgesException If the operation results in integer
         *      overflow.
         */
        public static int multiply(int x, int y) {
            long rslt = (long)x * (long)y;
            if (Integer.MIN_VALUE <= rslt && rslt <= Integer.MAX_VALUE) {
                return (int)rslt;
            }

            String msg = fmt("Integer overflow: %d * %d.", x, y);
            throw new edgesException(msg);
        }

        /**
         * Returns the multiplication of two real numbers.
         *
         * @param x The first real number.
         * @param y The second real number.
         * @return {@code x * y}.
         * @throws edgesException If the operation results in real overflow.
         */
        public static double multiply(double x, double y) {
            double rslt = x * y;
            if (Double.isInfinite(rslt)) {
                String msg = fmt("Real overflow: %s * %s.",
                                 realToStr(x), realToStr(y));
                throw new edgesException(msg);
            }
            return (rslt == -0.0) ? 0.0 : rslt;
        }

        /**
         * Returns the negation of an integer number.
         *
         * @param x The integer number.
         * @return {@code -value}.
         * @throws edgesException If the operation results in integer
         *      overflow.
         */
        public static int negate(int x) {
            if (x == Integer.MIN_VALUE) {
                String msg = fmt("Integer overflow: -%d.", x);
                throw new edgesException(msg);
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
         * @throws edgesException If the operation results in integer
         *      overflow.
         */
        public static int powInt(int x, int y) {
            if (y < 0) throw new RuntimeException("y < 0");
            double rslt = Math.pow(x, y);
            if (Integer.MIN_VALUE <= rslt && rslt <= Integer.MAX_VALUE) {
                return (int)rslt;
            }
            String msg = fmt("Integer overflow: pow(%d, %d).", x, y);
            throw new edgesException(msg);
        }

        /**
         * Returns the exponentiation (power) of two real numbers.
         *
         * @param x The base real number.
         * @param y The exponent real number.
         * @return {@code pow(x, y)}.
         * @throws edgesException If the operation results in real overflow,
         *      or {@code NaN}.
         */
        public static double powReal(double x, double y) {
            // Assumes that the arguments are valid doubles (no inf/NaN/-0.0).
            double rslt = Math.pow(x, y);
            if (Double.isInfinite(rslt)) {
                String msg = fmt("Real overflow: pow(%s, %s).",
                                 realToStr(x), realToStr(y));
                throw new edgesException(msg);
            }
            if (Double.isNaN(rslt)) {
                String msg = fmt("Invalid operation: pow(%s, %s).",
                                 realToStr(x), realToStr(y));
                throw new edgesException(msg);
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
         * @throws edgesException If the index is out of range for the list.
         */
        public static <T> T project(List<T> lst, int origIdx) {
            // Normalize index and check for out of bounds.
            int idx = origIdx;
            if (idx < 0) idx = lst.size() + idx;
            if (idx < 0 || idx >= lst.size()) {
                String msg = fmt("Index out of bounds: %s[%s].",
                                 valueToStr(lst), origIdx);
                throw new edgesException(msg);
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
         * @throws edgesException If the index is out of range for the
         *      string.
         */
        public static String project(String str, int origIdx) {
            int idx = origIdx;
            if (idx < 0) idx = str.length() + idx;
            if (idx < 0 || idx >= str.length()) {
                String msg = fmt("Index out of bounds: \"%s\"[%s].",
                                 escape(str), origIdx);
                throw new edgesException(msg);
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
         * @throws edgesException Always thrown.
         */
        public static void rangeErrInt(String name, String value, String type) {
            String msg = fmt("Variable \"%s\" is assigned value \"%s\", which " +
                             "violates the integer type bounds of the type " +
                             "\"%s\" of that variable.", name, value, type);
            throw new edgesException(msg);
        }

        /**
         * Returns the round of a real number.
         *
         * @param x The real number.
         * @return {@code round(x)}.
         * @throws edgesException If the operation results in integer
         *      overflow.
         */
        public static int round(double x) {
            if (x < Integer.MIN_VALUE - 0.5 || x >= Integer.MAX_VALUE + 0.5) {
                String msg = fmt("Integer overflow: round(%s).", realToStr(x));
                throw new edgesException(msg);
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
         * @throws edgesException If the input interval is empty, or the
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
                throw new edgesException(msg);
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
         * @throws edgesException If the operation results in real overflow,
         *      or {@code NaN}.
         */
        public static double sin(double x) {
            double rslt = Math.sin(x);
            if (Double.isInfinite(rslt)) {
                String msg = fmt("Real overflow: sin(%s).", realToStr(x));
                throw new edgesException(msg);
            }
            if (Double.isNaN(rslt)) {
                String msg = fmt("Invalid operation: sin(%s).", realToStr(x));
                throw new edgesException(msg);
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
         * @throws edgesException If the real number is negative.
         */
        public static double sqrt(double x) {
            // Assumes that the argument is never -0.0.
            if (x < 0.0) {
                String msg = fmt("Invalid operation: sqrt(%s).", realToStr(x));
                throw new edgesException(msg);
            }
            return Math.sqrt(x);
        }

        /**
         * Converts a CIF boolean value literal, in the CIF ASCII representation,
         * to a Java {@link Boolean}.
         *
         * @param x The CIF boolean value literal, in the CIF ASCII representation.
         * @return The Java {@link Boolean} value.
         * @throws edgesException If the string value does not represent a
         *      boolean value.
         */
        public static boolean strToBool(String x) {
            if (x.equals("true")) return true;
            if (x.equals("false")) return false;

            String msg = fmt("Cast from type \"string\" to type \"bool\" " +
                             "failed: the string value does not represent a " +
                             "boolean value: \"%s\".", escape(x));
            throw new edgesException(msg);
        }

        /**
         * Converts a CIF integer value literal, in the CIF ASCII representation,
         * to a Java {@link Integer}.
         *
         * <p>See also the {@code CifTypeChecker.transIntExpression} method.</p>
         *
         * @param x The CIF integer value literal, in the CIF ASCII representation.
         * @return The Java {@link Integer} value.
         * @throws edgesException If the string value does not represent an
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
                throw new edgesException(msg);
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
         * @throws edgesException If the string value does not represent an
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
                throw new edgesException(msg);
            }

            if (Double.isInfinite(rslt)) {
                String msg = fmt("Cast from type \"string\" to type \"real\" " +
                                 "failed, due to real overflow: \"%s\".",
                                 escape(x));
                throw new edgesException(msg);
            }

            return (rslt == -0.0) ? 0.0 : rslt;
        }

        /**
         * Returns the subtraction of two integer numbers.
         *
         * @param x The first integer number.
         * @param y The second integer number.
         * @return {@code x - y}.
         * @throws edgesException If the operation results in integer
         *      overflow.
         */
        public static int subtract(int x, int y) {
            long rslt = (long)x - (long)y;
            if (Integer.MIN_VALUE <= rslt && rslt <= Integer.MAX_VALUE) {
                return (int)rslt;
            }

            String msg = fmt("Integer overflow: %d - %d.", x, y);
            throw new edgesException(msg);
        }

        /**
         * Returns the subtraction of two real numbers.
         *
         * @param x The first real number.
         * @param y The second real number.
         * @return {@code x - y}.
         * @throws edgesException If the operation results in real overflow.
         */
        public static double subtract(double x, double y) {
            double rslt = x - y;
            if (Double.isInfinite(rslt)) {
                String msg = fmt("Real overflow: %s - %s.",
                                 realToStr(x), realToStr(y));
                throw new edgesException(msg);
            }
            return (rslt == -0.0) ? 0.0 : rslt;
        }

        /**
         * Returns the tangent of a real number.
         *
         * @param x The real number.
         * @return {@code tan(x)}.
         * @throws edgesException If the operation results in real overflow,
         *      or {@code NaN}.
         */
        public static double tan(double x) {
            double rslt = Math.tan(x);
            if (Double.isInfinite(rslt)) {
                String msg = fmt("Real overflow: tan(%s).", realToStr(x));
                throw new edgesException(msg);
            }
            if (Double.isNaN(rslt)) {
                String msg = fmt("Invalid operation: tan(%s).", realToStr(x));
                throw new edgesException(msg);
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

            } else if (value instanceof edges.CifTupleBase) {
                return value.toString();

            } else {
                throw new RuntimeException("Unsupported value: " + value);
            }
        }
    }
}
