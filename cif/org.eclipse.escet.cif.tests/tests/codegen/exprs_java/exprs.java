package exprs_java;

import static exprs_java.exprs.exprsUtils.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/** exprs code generated from a CIF specification. */
@SuppressWarnings("unused")
public abstract class exprs {
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

    /** Constant "x1". */
    public static final int x1_ = 5;

    /** Variable 'time'. */
    public double time;

    /** Continuous variable "x5". */
    public double x5_;

    /** Discrete variable "a1.x". */
    public int a1_x_;

    /** Discrete variable "AA.vb". */
    public boolean AA_vb_;

    /** Discrete variable "AA.vi". */
    public int AA_vi_;

    /** Discrete variable "AA.vp". */
    public int AA_vp_;

    /** Discrete variable "AA.vn". */
    public int AA_vn_;

    /** Discrete variable "AA.vz". */
    public int AA_vz_;

    /** Discrete variable "AA.vr". */
    public double AA_vr_;

    /** Discrete variable "AA.vs". */
    public String AA_vs_;

    /** Discrete variable "AA.ve". */
    public exprsEnum AA_ve_;

    /** Discrete variable "AA.va". */
    public List<Integer> AA_va_;

    /** Discrete variable "AA.v2". */
    public double AA_v2_;

    /** Discrete variable "AA.i2r". */
    public double AA_i2r_;

    /** Discrete variable "AA.b2s". */
    public String AA_b2s_;

    /** Discrete variable "AA.i2s". */
    public String AA_i2s_;

    /** Discrete variable "AA.r2s". */
    public String AA_r2s_;

    /** Discrete variable "AA.s2b". */
    public boolean AA_s2b_;

    /** Discrete variable "AA.s2i". */
    public int AA_s2i_;

    /** Discrete variable "AA.s2r". */
    public double AA_s2r_;

    /** Discrete variable "AA.self_cast1". */
    public List<Integer> AA_self_cast1_;

    /** Discrete variable "AA.self_cast2". */
    public List<Integer> AA_self_cast2_;

    /** Discrete variable "AA.inv1". */
    public boolean AA_inv1_;

    /** Discrete variable "AA.inv2". */
    public boolean AA_inv2_;

    /** Discrete variable "AA.neg1". */
    public int AA_neg1_;

    /** Discrete variable "AA.neg2". */
    public int AA_neg2_;

    /** Discrete variable "AA.neg3". */
    public int AA_neg3_;

    /** Discrete variable "AA.neg4". */
    public int AA_neg4_;

    /** Discrete variable "AA.pos1". */
    public int AA_pos1_;

    /** Discrete variable "AA.pos2". */
    public int AA_pos2_;

    /** Discrete variable "AA.posneg". */
    public int AA_posneg_;

    /** Discrete variable "AA.l3i". */
    public List<Boolean> AA_l3i_;

    /** Discrete variable "AA.idx1". */
    public int AA_idx1_;

    /** Discrete variable "AA.vt". */
    public boolean AA_vt_;

    /** Discrete variable "AA.vf". */
    public boolean AA_vf_;

    /** Discrete variable "AA.short_and". */
    public boolean AA_short_and_;

    /** Discrete variable "AA.short_or". */
    public boolean AA_short_or_;

    /** Discrete variable "AA.impl". */
    public boolean AA_impl_;

    /** Discrete variable "AA.biimpl". */
    public boolean AA_biimpl_;

    /** Discrete variable "AA.conj". */
    public boolean AA_conj_;

    /** Discrete variable "AA.disj". */
    public boolean AA_disj_;

    /** Discrete variable "AA.lt1". */
    public boolean AA_lt1_;

    /** Discrete variable "AA.le1". */
    public boolean AA_le1_;

    /** Discrete variable "AA.gt1". */
    public boolean AA_gt1_;

    /** Discrete variable "AA.ge1". */
    public boolean AA_ge1_;

    /** Discrete variable "AA.lt2". */
    public boolean AA_lt2_;

    /** Discrete variable "AA.le2". */
    public boolean AA_le2_;

    /** Discrete variable "AA.gt2". */
    public boolean AA_gt2_;

    /** Discrete variable "AA.ge2". */
    public boolean AA_ge2_;

    /** Discrete variable "AA.lt3". */
    public boolean AA_lt3_;

    /** Discrete variable "AA.le3". */
    public boolean AA_le3_;

    /** Discrete variable "AA.gt3". */
    public boolean AA_gt3_;

    /** Discrete variable "AA.ge3". */
    public boolean AA_ge3_;

    /** Discrete variable "AA.lt4". */
    public boolean AA_lt4_;

    /** Discrete variable "AA.le4". */
    public boolean AA_le4_;

    /** Discrete variable "AA.gt4". */
    public boolean AA_gt4_;

    /** Discrete variable "AA.ge4". */
    public boolean AA_ge4_;

    /** Discrete variable "AA.eq1". */
    public boolean AA_eq1_;

    /** Discrete variable "AA.eq2". */
    public boolean AA_eq2_;

    /** Discrete variable "AA.eq3". */
    public boolean AA_eq3_;

    /** Discrete variable "AA.eq4". */
    public boolean AA_eq4_;

    /** Discrete variable "AA.eq5". */
    public boolean AA_eq5_;

    /** Discrete variable "AA.ne1". */
    public boolean AA_ne1_;

    /** Discrete variable "AA.ne2". */
    public boolean AA_ne2_;

    /** Discrete variable "AA.ne3". */
    public boolean AA_ne3_;

    /** Discrete variable "AA.ne4". */
    public boolean AA_ne4_;

    /** Discrete variable "AA.ne5". */
    public boolean AA_ne5_;

    /** Discrete variable "AA.add1". */
    public int AA_add1_;

    /** Discrete variable "AA.add2". */
    public double AA_add2_;

    /** Discrete variable "AA.add3". */
    public double AA_add3_;

    /** Discrete variable "AA.add4". */
    public double AA_add4_;

    /** Discrete variable "AA.add5". */
    public String AA_add5_;

    /** Discrete variable "AA.add6". */
    public int AA_add6_;

    /** Discrete variable "AA.add7". */
    public int AA_add7_;

    /** Discrete variable "AA.add8". */
    public int AA_add8_;

    /** Discrete variable "AA.sub1". */
    public int AA_sub1_;

    /** Discrete variable "AA.sub2". */
    public double AA_sub2_;

    /** Discrete variable "AA.sub3". */
    public double AA_sub3_;

    /** Discrete variable "AA.sub4". */
    public double AA_sub4_;

    /** Discrete variable "AA.sub5". */
    public int AA_sub5_;

    /** Discrete variable "AA.sub6". */
    public int AA_sub6_;

    /** Discrete variable "AA.sub7". */
    public int AA_sub7_;

    /** Discrete variable "AA.mul1". */
    public int AA_mul1_;

    /** Discrete variable "AA.mul2". */
    public double AA_mul2_;

    /** Discrete variable "AA.mul3". */
    public double AA_mul3_;

    /** Discrete variable "AA.mul4". */
    public double AA_mul4_;

    /** Discrete variable "AA.mul5". */
    public int AA_mul5_;

    /** Discrete variable "AA.mul6". */
    public int AA_mul6_;

    /** Discrete variable "AA.mul7". */
    public int AA_mul7_;

    /** Discrete variable "AA.rdiv1". */
    public double AA_rdiv1_;

    /** Discrete variable "AA.rdiv2". */
    public double AA_rdiv2_;

    /** Discrete variable "AA.rdiv3". */
    public double AA_rdiv3_;

    /** Discrete variable "AA.rdiv4". */
    public double AA_rdiv4_;

    /** Discrete variable "AA.rdiv5". */
    public double AA_rdiv5_;

    /** Discrete variable "AA.rdiv6". */
    public double AA_rdiv6_;

    /** Discrete variable "AA.div1". */
    public int AA_div1_;

    /** Discrete variable "AA.div2". */
    public int AA_div2_;

    /** Discrete variable "AA.div3". */
    public int AA_div3_;

    /** Discrete variable "AA.div4". */
    public int AA_div4_;

    /** Discrete variable "AA.mod1". */
    public int AA_mod1_;

    /** Discrete variable "AA.mod2". */
    public int AA_mod2_;

    /** Discrete variable "AA.li". */
    public List<Integer> AA_li_;

    /** Discrete variable "AA.tii". */
    public CifTuple_T2II AA_tii_;

    /** Discrete variable "AA.ss". */
    public String AA_ss_;

    /** Discrete variable "AA.proj1". */
    public int AA_proj1_;

    /** Discrete variable "AA.proj2". */
    public int AA_proj2_;

    /** Discrete variable "AA.proj3". */
    public int AA_proj3_;

    /** Discrete variable "AA.proj4". */
    public int AA_proj4_;

    /** Discrete variable "AA.proj5". */
    public String AA_proj5_;

    /** Discrete variable "AA.proj6". */
    public String AA_proj6_;

    /** Discrete variable "AA.f_acos". */
    public double AA_f_acos_;

    /** Discrete variable "AA.f_asin". */
    public double AA_f_asin_;

    /** Discrete variable "AA.f_atan". */
    public double AA_f_atan_;

    /** Discrete variable "AA.f_cos". */
    public double AA_f_cos_;

    /** Discrete variable "AA.f_sin". */
    public double AA_f_sin_;

    /** Discrete variable "AA.f_tan". */
    public double AA_f_tan_;

    /** Discrete variable "AA.f_abs1". */
    public int AA_f_abs1_;

    /** Discrete variable "AA.f_abs12". */
    public int AA_f_abs12_;

    /** Discrete variable "AA.f_abs2". */
    public double AA_f_abs2_;

    /** Discrete variable "AA.f_cbrt". */
    public double AA_f_cbrt_;

    /** Discrete variable "AA.f_ceil". */
    public int AA_f_ceil_;

    /** Discrete variable "AA.f_empty". */
    public boolean AA_f_empty_;

    /** Discrete variable "AA.f_exp". */
    public double AA_f_exp_;

    /** Discrete variable "AA.f_floor". */
    public int AA_f_floor_;

    /** Discrete variable "AA.f_ln". */
    public double AA_f_ln_;

    /** Discrete variable "AA.f_log". */
    public double AA_f_log_;

    /** Discrete variable "AA.f_max1". */
    public int AA_f_max1_;

    /** Discrete variable "AA.f_max2". */
    public double AA_f_max2_;

    /** Discrete variable "AA.f_max3". */
    public double AA_f_max3_;

    /** Discrete variable "AA.f_max4". */
    public double AA_f_max4_;

    /** Discrete variable "AA.f_min1". */
    public int AA_f_min1_;

    /** Discrete variable "AA.f_min2". */
    public double AA_f_min2_;

    /** Discrete variable "AA.f_min3". */
    public double AA_f_min3_;

    /** Discrete variable "AA.f_min4". */
    public double AA_f_min4_;

    /** Discrete variable "AA.f_pow1". */
    public double AA_f_pow1_;

    /** Discrete variable "AA.f_pow12". */
    public int AA_f_pow12_;

    /** Discrete variable "AA.f_pow2". */
    public double AA_f_pow2_;

    /** Discrete variable "AA.f_pow3". */
    public double AA_f_pow3_;

    /** Discrete variable "AA.f_pow4". */
    public double AA_f_pow4_;

    /** Discrete variable "AA.f_round". */
    public int AA_f_round_;

    /** Discrete variable "AA.f_scale". */
    public double AA_f_scale_;

    /** Discrete variable "AA.f_sign1". */
    public int AA_f_sign1_;

    /** Discrete variable "AA.f_sign2". */
    public int AA_f_sign2_;

    /** Discrete variable "AA.f_size1". */
    public int AA_f_size1_;

    /** Discrete variable "AA.f_size2". */
    public int AA_f_size2_;

    /** Discrete variable "AA.f_sqrt". */
    public double AA_f_sqrt_;

    /** Input variable "x8". */
    public int x8_;

    /** Constructor for the {@link exprs} class. */
    public exprs() {
        firstExec = true;
        this.time = 0.0;
    }

    /**
     * Execute the code once. Inputs are read, transitions are executed until
     * none are possible, outputs are written, etc.
     *
     * @param newTime The time in seconds, since the start of the first
     *      execution.
     * @throws exprsException In case of a runtime error caused by code
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
            double deriv0 = x5_deriv();

            x5_ = x5_ + delta * deriv0;
            checkDouble(x5_, "x5");
            if (x5_ == -0.0) x5_ = 0.0;
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
            // Event "tau".
            if (execEvent0()) continue;

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
     * @throws exprsException In case of a runtime error caused by code
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
     * Execute code for event "tau".
     *
     * @return {@code true} if the event was executed, {@code false} otherwise.
     */
    private boolean execEvent0() {
        if (doInfoPrintOutput) printOutput(-1, true);
        if (doInfoEvent) infoEvent(-1, true);

        if ((!equalObjs(a1_x_, 1)) && (!equalObjs(a1_x_, 2))) {
            a1_x_ = 3;
        } else if ((!equalObjs(a1_x_, 2)) && (!equalObjs(a1_x_, 3))) {
            a1_x_ = 4;
        }

        if (doInfoEvent) infoEvent(-1, false);
        if (doInfoPrintOutput) printOutput(-1, false);
        return true;
    }

    /** Initializes the state. */
    private void initState() {
        x5_ = 0.0;
        a1_x_ = 0;
        AA_vb_ = true;
        AA_vi_ = 5;
        AA_vp_ = 2;
        AA_vn_ = -(1);
        AA_vz_ = 1;
        AA_vr_ = 1.23;
        AA_vs_ = "a";
        AA_ve_ = exprsEnum._A;
        AA_va_ = makelist(new ArrayList<Integer>(2), 1, 2);
        AA_v2_ = 5.0;
        AA_i2r_ = intToReal(AA_vi_);
        AA_b2s_ = boolToStr(AA_vb_);
        AA_i2s_ = intToStr(AA_vi_);
        AA_r2s_ = realToStr(AA_vr_);
        AA_s2b_ = strToBool(AA_b2s_);
        AA_s2i_ = strToInt(AA_i2s_);
        AA_s2r_ = strToReal(AA_r2s_);
        AA_self_cast1_ = makelist(new ArrayList<Integer>(3), 1, 2, 3);
        AA_self_cast2_ = AA_self_cast1_;
        AA_inv1_ = !(AA_vb_);
        AA_inv2_ = AA_vb_;
        AA_neg1_ = negate(AA_vi_);
        AA_neg2_ = negate(negate(AA_vi_));
        AA_neg3_ = -(AA_vp_);
        AA_neg4_ = -(-(AA_vp_));
        AA_pos1_ = AA_vi_;
        AA_pos2_ = AA_vi_;
        AA_posneg_ = negate(negate(negate(negate(AA_vi_))));
        AA_l3i_ = makelist(new ArrayList<Boolean>(1), true);
        AA_idx1_ = 1;
        AA_vt_ = true;
        AA_vf_ = false;
        AA_short_and_ = (AA_vf_) && (project(AA_l3i_, AA_idx1_));
        AA_short_or_ = (AA_vt_) || (project(AA_l3i_, AA_idx1_));
        AA_impl_ = !(AA_vb_) || (AA_vb_);
        AA_biimpl_ = equalObjs(AA_vb_, AA_vb_);
        AA_conj_ = (AA_vb_) && (AA_vb_);
        AA_disj_ = (AA_vb_) || (AA_vb_);
        AA_lt1_ = (AA_vi_) < (AA_vi_);
        AA_le1_ = (AA_vi_) <= (AA_vi_);
        AA_gt1_ = (AA_vi_) > (AA_vi_);
        AA_ge1_ = (AA_vi_) >= (AA_vi_);
        AA_lt2_ = (AA_vi_) < (AA_vr_);
        AA_le2_ = (AA_vi_) <= (AA_vr_);
        AA_gt2_ = (AA_vi_) > (AA_vr_);
        AA_ge2_ = (AA_vi_) >= (AA_vr_);
        AA_lt3_ = (AA_vr_) < (AA_vr_);
        AA_le3_ = (AA_vr_) <= (AA_vr_);
        AA_gt3_ = (AA_vr_) > (AA_vr_);
        AA_ge3_ = (AA_vr_) >= (AA_vr_);
        AA_lt4_ = (AA_vr_) < (AA_vr_);
        AA_le4_ = (AA_vr_) <= (AA_vr_);
        AA_gt4_ = (AA_vr_) > (AA_vr_);
        AA_ge4_ = (AA_vr_) >= (AA_vr_);
        AA_eq1_ = equalObjs(AA_vb_, AA_vb_);
        AA_eq2_ = equalObjs(AA_vi_, AA_vi_);
        AA_eq3_ = equalObjs(AA_vr_, AA_vr_);
        AA_eq4_ = equalObjs(AA_vs_, AA_vs_);
        AA_eq5_ = (AA_ve_) == (AA_ve_);
        AA_ne1_ = !equalObjs(AA_vb_, AA_vb_);
        AA_ne2_ = !equalObjs(AA_vi_, AA_vi_);
        AA_ne3_ = !equalObjs(AA_vr_, AA_vr_);
        AA_ne4_ = !equalObjs(AA_vs_, AA_vs_);
        AA_ne5_ = (AA_ve_) != (AA_ve_);
        AA_add1_ = addInt(AA_vi_, AA_vi_);
        AA_add2_ = addReal(AA_vi_, AA_vr_);
        AA_add3_ = addReal(AA_vr_, AA_vi_);
        AA_add4_ = addReal(AA_vr_, AA_vr_);
        AA_add5_ = addString(AA_vs_, AA_vs_);
        AA_add6_ = (AA_vp_) + (AA_vp_);
        AA_add7_ = addInt(AA_vi_, AA_vp_);
        AA_add8_ = addInt(AA_vp_, AA_vi_);
        AA_sub1_ = subtract(AA_vi_, AA_vi_);
        AA_sub2_ = subtract(AA_vi_, AA_vr_);
        AA_sub3_ = subtract(AA_vr_, AA_vi_);
        AA_sub4_ = subtract(AA_vr_, AA_vr_);
        AA_sub5_ = (AA_vp_) - (AA_vp_);
        AA_sub6_ = subtract(AA_vi_, AA_vp_);
        AA_sub7_ = subtract(AA_vp_, AA_vi_);
        AA_mul1_ = multiply(AA_vi_, AA_vi_);
        AA_mul2_ = multiply(AA_vi_, AA_vr_);
        AA_mul3_ = multiply(AA_vr_, AA_vi_);
        AA_mul4_ = multiply(AA_vr_, AA_vr_);
        AA_mul5_ = (AA_vp_) * (AA_vp_);
        AA_mul6_ = multiply(AA_vi_, AA_vp_);
        AA_mul7_ = multiply(AA_vp_, AA_vi_);
        AA_rdiv1_ = divide(AA_vi_, AA_vi_);
        AA_rdiv2_ = divide(AA_vi_, AA_vr_);
        AA_rdiv3_ = divide(AA_vr_, AA_vi_);
        AA_rdiv4_ = divide(AA_vr_, AA_vr_);
        AA_rdiv5_ = ((double)(AA_vi_)) / (AA_vp_);
        AA_rdiv6_ = ((double)(AA_vi_)) / (AA_vn_);
        AA_div1_ = div(AA_vi_, AA_vi_);
        AA_div2_ = (AA_vi_) / (AA_vp_);
        AA_div3_ = div(AA_vi_, AA_vn_);
        AA_div4_ = div(AA_vi_, AA_vz_);
        AA_mod1_ = mod(AA_vi_, AA_vi_);
        AA_mod2_ = (AA_vi_) % (AA_vp_);
        AA_li_ = makelist(new ArrayList<Integer>(2), 1, 2);
        AA_tii_ = new CifTuple_T2II(1, 2);
        AA_ss_ = "abc";
        AA_proj1_ = project(AA_li_, 0);
        AA_proj2_ = project(AA_li_, -(1));
        AA_proj3_ = (AA_tii_)._field0;
        AA_proj4_ = (AA_tii_)._field1;
        AA_proj5_ = project(AA_ss_, 0);
        AA_proj6_ = project(AA_ss_, -(1));
        AA_f_acos_ = acos(AA_vr_);
        AA_f_asin_ = asin(AA_vr_);
        AA_f_atan_ = atan(AA_vr_);
        AA_f_cos_ = cos(AA_vr_);
        AA_f_sin_ = sin(AA_vr_);
        AA_f_tan_ = tan(AA_vr_);
        AA_f_abs1_ = abs(AA_vi_);
        AA_f_abs12_ = abs(AA_vp_);
        AA_f_abs2_ = abs(AA_vr_);
        AA_f_cbrt_ = cbrt(AA_vr_);
        AA_f_ceil_ = ceil(AA_vr_);
        AA_f_empty_ = empty(AA_va_);
        AA_f_exp_ = exp(AA_vr_);
        AA_f_floor_ = floor(AA_vr_);
        AA_f_ln_ = ln(AA_vr_);
        AA_f_log_ = log(AA_vr_);
        AA_f_max1_ = max(AA_vi_, AA_vi_);
        AA_f_max2_ = max(AA_vi_, AA_vr_);
        AA_f_max3_ = max(AA_vr_, AA_vi_);
        AA_f_max4_ = max(AA_vr_, AA_vr_);
        AA_f_min1_ = min(AA_vi_, AA_vi_);
        AA_f_min2_ = min(AA_vi_, AA_vr_);
        AA_f_min3_ = min(AA_vr_, AA_vi_);
        AA_f_min4_ = min(AA_vr_, AA_vr_);
        AA_f_pow1_ = powReal(AA_vi_, AA_vi_);
        AA_f_pow12_ = powInt(AA_vp_, AA_vp_);
        AA_f_pow2_ = powReal(AA_vi_, AA_vr_);
        AA_f_pow3_ = powReal(AA_vr_, AA_vi_);
        AA_f_pow4_ = powReal(AA_vr_, AA_vr_);
        AA_f_round_ = round(AA_vr_);
        AA_f_scale_ = scale(AA_vr_, 0, 10, 1, 11);
        AA_f_sign1_ = sign(AA_vi_);
        AA_f_sign2_ = sign(AA_vr_);
        AA_f_size1_ = size(AA_va_);
        AA_f_size2_ = size(AA_vs_);
        AA_f_sqrt_ = sqrt(AA_vr_);
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
     * Evaluates algebraic variable "v1".
     *
     * @return The evaluation result.
     */
    public int v1_() {
        return (((a1_x_) > (0)) && ((a1_x_) < (5))) ? 0 : ((((a1_x_) > (6)) && ((a1_x_) < (9))) ? 1 : (2));
    }

    /**
     * Evaluates algebraic variable "if1".
     *
     * @return The evaluation result.
     */
    public int if1_() {
        return ((time) > (1)) ? 1 : (0);
    }

    /**
     * Evaluates algebraic variable "if2".
     *
     * @return The evaluation result.
     */
    public int if2_() {
        return (((time) > (1)) ? 1 : (((time) > (0.5)) ? 2 : (0))) + (1);
    }

    /**
     * Evaluates algebraic variable "if3".
     *
     * @return The evaluation result.
     */
    public int if3_() {
        return (((time) > (1)) ? 1 : (((time) > (0.5)) ? 2 : (((time) > (0.25)) ? 3 : (0)))) + (2);
    }

    /**
     * Evaluates algebraic variable "fcall1".
     *
     * @return The evaluation result.
     */
    public int fcall1_() {
        return inc_(0);
    }

    /**
     * Evaluates algebraic variable "fcall2".
     *
     * @return The evaluation result.
     */
    public int fcall2_() {
        return inc_(inc_(0));
    }

    /**
     * Evaluates algebraic variable "vea".
     *
     * @return The evaluation result.
     */
    public exprsEnum vea_() {
        return exprsEnum._A;
    }

    /**
     * Evaluates algebraic variable "x2".
     *
     * @return The evaluation result.
     */
    public int x2_() {
        return x1_;
    }

    /**
     * Evaluates algebraic variable "x3".
     *
     * @return The evaluation result.
     */
    public int x3_() {
        return x2_();
    }

    /**
     * Evaluates algebraic variable "x4".
     *
     * @return The evaluation result.
     */
    public int x4_() {
        return a1_x_;
    }

    /**
     * Evaluates algebraic variable "x6".
     *
     * @return The evaluation result.
     */
    public double x6_() {
        return addReal(x5_, x5_deriv());
    }

    /**
     * Evaluates algebraic variable "x7".
     *
     * @return The evaluation result.
     */
    public boolean x7_() {
        return (vea_()) == (exprsEnum._B);
    }

    /**
     * Evaluates algebraic variable "x9".
     *
     * @return The evaluation result.
     */
    public int x9_() {
        return addInt(x8_, 1);
    }

    /**
     * Evaluates derivative of continuous variable "x5".
     *
     * @return The evaluation result.
     */
    public double x5_deriv() {
        return 1.0;
    }

    /**
     * Evaluation for function "f1".
     *
     * @param f1_x_ Function parameter "f1.x".
     * @return The return value of the function.
     */
    public static int f1_(int f1_x_) {
        if (true) while ((!equalObjs(f1_x_, 0)) && (!equalObjs(f1_x_, 4))) {
            f1_x_ = subtract(f1_x_, 1);
        }

        if ((!equalObjs(f1_x_, 1)) && (!equalObjs(f1_x_, 2))) {
            f1_x_ = 3;
        } else if ((!equalObjs(f1_x_, 2)) && (!equalObjs(f1_x_, 3))) {
            f1_x_ = 4;
        }

        if (true) return f1_x_;
        throw new RuntimeException("no return at end of func");
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
     *      as path separators. Use {@link exprsUtils#normalizePrintTarget}
     *      to normalize the path to use path separators for the current
     *      platform. There are two special targets: {@code ":stdout"} to print
     *      to the standard output stream, and {@code ":stderr"} to print to
     *      the standard error stream.
     */
    protected abstract void infoPrintOutput(String text, String target);

    /** exprs enumeration. */
    public static enum exprsEnum {
        /** A */ _A,
        /** B */ _B;

        @Override
        public String toString() {
            return name().substring(1);
        }
    }

    /**
     * exprs exception.
     *
     * <p>Indices a runtime error while executing the generated code.</p>
     */
    public static class exprsException extends RuntimeException {
        /**
         * Constructor for the {@link exprsException} class.
         *
         * @param message The message describing the exception.
         */
        public exprsException(String message) {
            super(message);
        }

        /**
         * Constructor for the {@link exprsException} class.
         *
         * @param message The message describing the exception.
         * @param cause The root cause of the exception.
         */
        public exprsException(String message, Throwable cause) {
            super(message, cause);
        }

        @Override
        public String toString() {
            return getMessage();
        }
    }

    /** {@link exprs} utility code. */
    public static class exprsUtils {
        /** The path separator for the current platform. */
        private static final String PATH_SEPARATOR = System.getProperty("file.separator");

        /** Constructor for the {@link exprsUtils} class. */
        private exprsUtils() {
            // Static class.
        }

        /**
         * Returns the absolute value of an integer number.
         *
         * @param x The integer number.
         * @return {@code abs(x)}.
         * @throws exprsException If the operation results in integer
         *      overflow.
         */
        public static int abs(int x) {
            if (x == Integer.MIN_VALUE) {
                String msg = fmt("Integer overflow: abs(%d).", x);
                throw new exprsException(msg);
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
         * @throws exprsException If the operation results in real overflow,
         *      or {@code NaN}.
         */
        public static double acos(double x) {
            double rslt = Math.acos(x);
            if (Double.isInfinite(rslt)) {
                String msg = fmt("Real overflow: acos(%s).", realToStr(x));
                throw new exprsException(msg);
            }
            if (Double.isNaN(rslt)) {
                String msg = fmt("Invalid operation: acos(%s).", realToStr(x));
                throw new exprsException(msg);
            }
            return (rslt == -0.0) ? 0.0 : rslt;
        }

        /**
         * Returns the arc sine of a real number.
         *
         * @param x The real number.
         * @return {@code asin(x)}.
         * @throws exprsException If the operation results in real overflow,
         *      or {@code NaN}.
         */
        public static double asin(double x) {
            double rslt = Math.asin(x);
            if (Double.isInfinite(rslt)) {
                String msg = fmt("Real overflow: asin(%s).", realToStr(x));
                throw new exprsException(msg);
            }
            if (Double.isNaN(rslt)) {
                String msg = fmt("Invalid operation: asin(%s).", realToStr(x));
                throw new exprsException(msg);
            }
            return (rslt == -0.0) ? 0.0 : rslt;
        }

        /**
         * Returns the arc tangent of a real number.
         *
         * @param x The real number.
         * @return {@code atan(x)}.
         * @throws exprsException If the operation results in real overflow,
         *      or {@code NaN}.
         */
        public static double atan(double x) {
            double rslt = Math.atan(x);
            if (Double.isInfinite(rslt)) {
                String msg = fmt("Real overflow: atan(%s).", realToStr(x));
                throw new exprsException(msg);
            }
            if (Double.isNaN(rslt)) {
                String msg = fmt("Invalid operation: atan(%s).", realToStr(x));
                throw new exprsException(msg);
            }
            return (rslt == -0.0) ? 0.0 : rslt;
        }

        /**
         * Returns the addition of two integer numbers.
         *
         * @param x The first integer number.
         * @param y The second integer number.
         * @return {@code x + y}.
         * @throws exprsException If the operation results in integer
         *      overflow.
         */
        public static int addInt(int x, int y) {
            long rslt = (long)x + (long)y;
            if (Integer.MIN_VALUE <= rslt && rslt <= Integer.MAX_VALUE) {
                return (int)rslt;
            }

            String msg = fmt("Integer overflow: %d + %d.", x, y);
            throw new exprsException(msg);
        }

        /**
         * Returns the addition of two real numbers.
         *
         * @param x The first real number.
         * @param y The second real number.
         * @return {@code x + y}.
         * @throws exprsException If the operation results in real overflow.
         */
        public static double addReal(double x, double y) {
            double rslt = x + y;
            if (Double.isInfinite(rslt)) {
                String msg = fmt("Real overflow: %s + %s.",
                                 realToStr(x), realToStr(y));
                throw new exprsException(msg);
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
         * @throws exprsException If the operation results in integer
         *      overflow.
         */
        public static int ceil(double x) {
            double rslt = Math.ceil(x);
            if (rslt < Integer.MIN_VALUE || rslt > Integer.MAX_VALUE) {
                String msg = fmt("Integer overflow: ceil(%s).", realToStr(x));
                throw new exprsException(msg);
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
            throw new exprsException(msg);
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
         * @throws exprsException If the operation results in real overflow,
         *      or {@code NaN}.
         */
        public static double cos(double x) {
            double rslt = Math.cos(x);
            if (Double.isInfinite(rslt)) {
                String msg = fmt("Real overflow: cos(%s).", realToStr(x));
                throw new exprsException(msg);
            }
            if (Double.isNaN(rslt)) {
                String msg = fmt("Invalid operation: cos(%s).", realToStr(x));
                throw new exprsException(msg);
            }
            return (rslt == -0.0) ? 0.0 : rslt;
        }

        /**
         * Returns the integer division of two integer numbers.
         *
         * @param x The dividend.
         * @param y The divisor.
         * @return {@code x div y}.
         * @throws exprsException If the operation results in integer
         *      overflow, or division by zero.
         */
        public static int div(int x, int y) {
            if (y == 0) {
                String msg = fmt("Division by zero: %d div %d.", x, y);
                throw new exprsException(msg);
            }
            if (x == Integer.MIN_VALUE && y == -1) {
                String msg = fmt("Integer overflow: %d div %d.", x, y);
                throw new exprsException(msg);
            }
            return x / y;
        }

        /**
         * Returns the real division of two real numbers.
         *
         * @param x The first real number.
         * @param y The second real number.
         * @return {@code x / y}.
         * @throws exprsException If the operation results in real overflow,
         *      or division by zero.
         */
        public static double divide(double x, double y) {
            if (y == 0.0) {
                String msg = fmt("Division by zero: %s / %s.",
                                 realToStr(x), realToStr(y));
                throw new exprsException(msg);
            }
            double rslt = x / y;
            if (Double.isInfinite(rslt)) {
                String msg = fmt("Real overflow: %s * %s.",
                                 realToStr(x), realToStr(y));
                throw new exprsException(msg);
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
         * @throws exprsException If the operation results in real overflow.
         */
        public static double exp(double x) {
            double rslt = Math.exp(x);
            if (Double.isInfinite(rslt)) {
                String msg = fmt("Real overflow: exp(%s).", realToStr(x));
                throw new exprsException(msg);
            }
            return (rslt == -0.0) ? 0.0 : rslt;
        }

        /**
         * Returns the floor of a real number.
         *
         * @param x The real number.
         * @return {@code floor(x)}.
         * @throws exprsException If the operation results in integer
         *      overflow.
         */
        public static int floor(double x) {
            double rslt = Math.floor(x);
            if (rslt < Integer.MIN_VALUE || rslt > Integer.MAX_VALUE) {
                String msg = fmt("Integer overflow: floor(%s).", realToStr(x));
                throw new exprsException(msg);
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
         * @throws exprsException If the real number is non-positive.
         */
        public static double ln(double x) {
            if (x <= 0.0) {
                String msg = fmt("Invalid operation: ln(%s).", realToStr(x));
                throw new exprsException(msg);
            }
            return Math.log(x);
        }

        /**
         * Returns the logarithm (base 10) of a real number.
         *
         * @param x The real number.
         * @return {@code log(x)}.
         * @throws exprsException If the real number is non-positive.
         */
        public static double log(double x) {
            if (x <= 0.0) {
                String msg = fmt("Invalid operation: log(%s).", realToStr(x));
                throw new exprsException(msg);
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
         * @throws exprsException If the operation results in division by
         *      zero.
         */
        public static int mod(int x, int y) {
            if (y == 0) {
                String msg = fmt("Division by zero: %d mod %d.", x, y);
                throw new exprsException(msg);
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
         * @throws exprsException If the index is out of range for the list.
         */
        public static <T> List<T> modify(List<T> lst, int origIdx, T newValue) {
            // Normalize index and check for out of bounds.
            int idx = origIdx;
            if (idx < 0) idx = lst.size() + idx;
            if (idx < 0 || idx >= lst.size()) {
                String msg = fmt("Index out of bounds: %s[%s].",
                                 valueToStr(lst), origIdx);
                throw new exprsException(msg);
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
         * @throws exprsException If the operation results in integer
         *      overflow.
         */
        public static int multiply(int x, int y) {
            long rslt = (long)x * (long)y;
            if (Integer.MIN_VALUE <= rslt && rslt <= Integer.MAX_VALUE) {
                return (int)rslt;
            }

            String msg = fmt("Integer overflow: %d * %d.", x, y);
            throw new exprsException(msg);
        }

        /**
         * Returns the multiplication of two real numbers.
         *
         * @param x The first real number.
         * @param y The second real number.
         * @return {@code x * y}.
         * @throws exprsException If the operation results in real overflow.
         */
        public static double multiply(double x, double y) {
            double rslt = x * y;
            if (Double.isInfinite(rslt)) {
                String msg = fmt("Real overflow: %s * %s.",
                                 realToStr(x), realToStr(y));
                throw new exprsException(msg);
            }
            return (rslt == -0.0) ? 0.0 : rslt;
        }

        /**
         * Returns the negation of an integer number.
         *
         * @param x The integer number.
         * @return {@code -value}.
         * @throws exprsException If the operation results in integer
         *      overflow.
         */
        public static int negate(int x) {
            if (x == Integer.MIN_VALUE) {
                String msg = fmt("Integer overflow: -%d.", x);
                throw new exprsException(msg);
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
         * @throws exprsException If the operation results in integer
         *      overflow.
         */
        public static int powInt(int x, int y) {
            if (y < 0) throw new RuntimeException("y < 0");
            double rslt = Math.pow(x, y);
            if (Integer.MIN_VALUE <= rslt && rslt <= Integer.MAX_VALUE) {
                return (int)rslt;
            }
            String msg = fmt("Integer overflow: pow(%d, %d).", x, y);
            throw new exprsException(msg);
        }

        /**
         * Returns the exponentiation (power) of two real numbers.
         *
         * @param x The base real number.
         * @param y The exponent real number.
         * @return {@code pow(x, y)}.
         * @throws exprsException If the operation results in real overflow,
         *      or {@code NaN}.
         */
        public static double powReal(double x, double y) {
            // Assumes that the arguments are valid doubles (no inf/NaN/-0.0).
            double rslt = Math.pow(x, y);
            if (Double.isInfinite(rslt)) {
                String msg = fmt("Real overflow: pow(%s, %s).",
                                 realToStr(x), realToStr(y));
                throw new exprsException(msg);
            }
            if (Double.isNaN(rslt)) {
                String msg = fmt("Invalid operation: pow(%s, %s).",
                                 realToStr(x), realToStr(y));
                throw new exprsException(msg);
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
         * @throws exprsException If the index is out of range for the list.
         */
        public static <T> T project(List<T> lst, int origIdx) {
            // Normalize index and check for out of bounds.
            int idx = origIdx;
            if (idx < 0) idx = lst.size() + idx;
            if (idx < 0 || idx >= lst.size()) {
                String msg = fmt("Index out of bounds: %s[%s].",
                                 valueToStr(lst), origIdx);
                throw new exprsException(msg);
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
         * @throws exprsException If the index is out of range for the
         *      string.
         */
        public static String project(String str, int origIdx) {
            int idx = origIdx;
            if (idx < 0) idx = str.length() + idx;
            if (idx < 0 || idx >= str.length()) {
                String msg = fmt("Index out of bounds: \"%s\"[%s].",
                                 escape(str), origIdx);
                throw new exprsException(msg);
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
         * @throws exprsException Always thrown.
         */
        public static void rangeErrInt(String name, String value, String type) {
            String msg = fmt("Variable \"%s\" is assigned value \"%s\", which " +
                             "violates the integer type bounds of the type " +
                             "\"%s\" of that variable.", name, value, type);
            throw new exprsException(msg);
        }

        /**
         * Returns the round of a real number.
         *
         * @param x The real number.
         * @return {@code round(x)}.
         * @throws exprsException If the operation results in integer
         *      overflow.
         */
        public static int round(double x) {
            if (x < Integer.MIN_VALUE - 0.5 || x >= Integer.MAX_VALUE + 0.5) {
                String msg = fmt("Integer overflow: round(%s).", realToStr(x));
                throw new exprsException(msg);
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
         * @throws exprsException If the input interval is empty, or the
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
                throw new exprsException(msg);
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
         * @throws exprsException If the operation results in real overflow,
         *      or {@code NaN}.
         */
        public static double sin(double x) {
            double rslt = Math.sin(x);
            if (Double.isInfinite(rslt)) {
                String msg = fmt("Real overflow: sin(%s).", realToStr(x));
                throw new exprsException(msg);
            }
            if (Double.isNaN(rslt)) {
                String msg = fmt("Invalid operation: sin(%s).", realToStr(x));
                throw new exprsException(msg);
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
         * @throws exprsException If the real number is negative.
         */
        public static double sqrt(double x) {
            // Assumes that the argument is never -0.0.
            if (x < 0.0) {
                String msg = fmt("Invalid operation: sqrt(%s).", realToStr(x));
                throw new exprsException(msg);
            }
            return Math.sqrt(x);
        }

        /**
         * Converts a CIF boolean value literal, in the CIF ASCII representation,
         * to a Java {@link Boolean}.
         *
         * @param x The CIF boolean value literal, in the CIF ASCII representation.
         * @return The Java {@link Boolean} value.
         * @throws exprsException If the string value does not represent a
         *      boolean value.
         */
        public static boolean strToBool(String x) {
            if (x.equals("true")) return true;
            if (x.equals("false")) return false;

            String msg = fmt("Cast from type \"string\" to type \"bool\" " +
                             "failed: the string value does not represent a " +
                             "boolean value: \"%s\".", escape(x));
            throw new exprsException(msg);
        }

        /**
         * Converts a CIF integer value literal, in the CIF ASCII representation,
         * to a Java {@link Integer}.
         *
         * <p>See also the {@code CifTypeChecker.transIntExpression} method.</p>
         *
         * @param x The CIF integer value literal, in the CIF ASCII representation.
         * @return The Java {@link Integer} value.
         * @throws exprsException If the string value does not represent an
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
                throw new exprsException(msg);
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
         * @throws exprsException If the string value does not represent an
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
                throw new exprsException(msg);
            }

            if (Double.isInfinite(rslt)) {
                String msg = fmt("Cast from type \"string\" to type \"real\" " +
                                 "failed, due to real overflow: \"%s\".",
                                 escape(x));
                throw new exprsException(msg);
            }

            return (rslt == -0.0) ? 0.0 : rslt;
        }

        /**
         * Returns the subtraction of two integer numbers.
         *
         * @param x The first integer number.
         * @param y The second integer number.
         * @return {@code x - y}.
         * @throws exprsException If the operation results in integer
         *      overflow.
         */
        public static int subtract(int x, int y) {
            long rslt = (long)x - (long)y;
            if (Integer.MIN_VALUE <= rslt && rslt <= Integer.MAX_VALUE) {
                return (int)rslt;
            }

            String msg = fmt("Integer overflow: %d - %d.", x, y);
            throw new exprsException(msg);
        }

        /**
         * Returns the subtraction of two real numbers.
         *
         * @param x The first real number.
         * @param y The second real number.
         * @return {@code x - y}.
         * @throws exprsException If the operation results in real overflow.
         */
        public static double subtract(double x, double y) {
            double rslt = x - y;
            if (Double.isInfinite(rslt)) {
                String msg = fmt("Real overflow: %s - %s.",
                                 realToStr(x), realToStr(y));
                throw new exprsException(msg);
            }
            return (rslt == -0.0) ? 0.0 : rslt;
        }

        /**
         * Returns the tangent of a real number.
         *
         * @param x The real number.
         * @return {@code tan(x)}.
         * @throws exprsException If the operation results in real overflow,
         *      or {@code NaN}.
         */
        public static double tan(double x) {
            double rslt = Math.tan(x);
            if (Double.isInfinite(rslt)) {
                String msg = fmt("Real overflow: tan(%s).", realToStr(x));
                throw new exprsException(msg);
            }
            if (Double.isNaN(rslt)) {
                String msg = fmt("Invalid operation: tan(%s).", realToStr(x));
                throw new exprsException(msg);
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

            } else if (value instanceof exprs.CifTupleBase) {
                return value.toString();

            } else {
                throw new RuntimeException("Unsupported value: " + value);
            }
        }
    }
}
