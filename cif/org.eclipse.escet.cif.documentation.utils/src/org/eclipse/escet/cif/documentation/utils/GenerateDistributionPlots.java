//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
//
// See the NOTICE file(s) distributed with this work for additional
// information regarding copyright ownership.
//
// This program and the accompanying materials are made available
// under the terms of the MIT License which is available at
// https://opensource.org/licenses/MIT
//
// SPDX-License-Identifier: MIT
//////////////////////////////////////////////////////////////////////////////

package org.eclipse.escet.cif.documentation.utils;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.escet.cif.simulator.runtime.distributions.BernoulliDistribution;
import org.eclipse.escet.cif.simulator.runtime.distributions.BetaDistribution;
import org.eclipse.escet.cif.simulator.runtime.distributions.BinomialDistribution;
import org.eclipse.escet.cif.simulator.runtime.distributions.BooleanDistribution;
import org.eclipse.escet.cif.simulator.runtime.distributions.CifMersenneTwister;
import org.eclipse.escet.cif.simulator.runtime.distributions.CifRandomGenerator;
import org.eclipse.escet.cif.simulator.runtime.distributions.ErlangDistribution;
import org.eclipse.escet.cif.simulator.runtime.distributions.ExponentialDistribution;
import org.eclipse.escet.cif.simulator.runtime.distributions.GammaDistribution;
import org.eclipse.escet.cif.simulator.runtime.distributions.GeometricDistribution;
import org.eclipse.escet.cif.simulator.runtime.distributions.IntegerDistribution;
import org.eclipse.escet.cif.simulator.runtime.distributions.LogNormalDistribution;
import org.eclipse.escet.cif.simulator.runtime.distributions.NormalDistribution;
import org.eclipse.escet.cif.simulator.runtime.distributions.PoissonDistribution;
import org.eclipse.escet.cif.simulator.runtime.distributions.RandomDistribution;
import org.eclipse.escet.cif.simulator.runtime.distributions.RealDistribution;
import org.eclipse.escet.cif.simulator.runtime.distributions.TriangleDistribution;
import org.eclipse.escet.cif.simulator.runtime.distributions.UniformIntegerDistribution;
import org.eclipse.escet.cif.simulator.runtime.distributions.UniformRealDistribution;
import org.eclipse.escet.cif.simulator.runtime.distributions.WeibullDistribution;
import org.eclipse.escet.common.app.framework.AppEnv;
import org.eclipse.escet.common.app.framework.io.AppStream;
import org.eclipse.escet.common.app.framework.io.FileAppStream;
import org.eclipse.escet.common.java.Assert;

/**
 * Generate Gnuplot files for the generation of plots for the actual sampled values and the expected/theoretical
 * distribution, for all the distributions.
 */
public class GenerateDistributionPlots {
    /** The number of samples to take. */
    private static final int SAMPLE_COUNT = 100 * 1000;

    /** The seed to use. */
    private static final int SEED = 123;

    /** The number of bars for the output. Must be an integer number. */
    private static final double DBARS = 1000;

    /** Constructor for the {@link GenerateDistributionPlots} class. */
    private GenerateDistributionPlots() {
        // Static class.
    }

    /**
     * Main method for the {@link GenerateDistributionPlots} class.
     *
     * @param args Main method arguments. They are ignored.
     */
    public static void main(String[] args) {
        // Register with application framework.
        AppEnv.registerSimple();

        // Random generators are used for all distribution types.
        CifRandomGenerator generator;

        // Boolean distributions.
        BooleanDistribution bdist;
        org.apache.commons.math3.distribution.IntegerDistribution bdist2;

        // Bernoulli distribution.
        generator = new CifMersenneTwister(SEED);
        bdist = new BernoulliDistribution(generator, 0.2);
        bdist2 = new org.apache.commons.math3.distribution.BinomialDistribution(1, 0.2);
        generateBool(bdist, bdist2, "bernoulli", new String[] {"0.2"});

        generator = new CifMersenneTwister(SEED);
        bdist = new BernoulliDistribution(generator, 0.5);
        bdist2 = new org.apache.commons.math3.distribution.BinomialDistribution(1, 0.5);
        generateBool(bdist, bdist2, "bernoulli", new String[] {"0.5"});

        generator = new CifMersenneTwister(SEED);
        bdist = new BernoulliDistribution(generator, 0.9);
        bdist2 = new org.apache.commons.math3.distribution.BinomialDistribution(1, 0.9);
        generateBool(bdist, bdist2, "bernoulli", new String[] {"0.9"});

        // Integer distributions.
        IntegerDistribution idist;
        org.apache.commons.math3.distribution.IntegerDistribution idist2;

        // Binomial distribution:
        // http://en.wikipedia.org/wiki/Binomial_distribution
        generator = new CifMersenneTwister(SEED);
        idist = new BinomialDistribution(generator, 0.5, 20);
        idist2 = new org.apache.commons.math3.distribution.BinomialDistribution(20, 0.5);
        generateInt(idist, idist2, "binomial", new String[] {"0.5", "20"});

        generator = new CifMersenneTwister(SEED);
        idist = new BinomialDistribution(generator, 0.7, 20);
        idist2 = new org.apache.commons.math3.distribution.BinomialDistribution(20, 0.7);
        generateInt(idist, idist2, "binomial", new String[] {"0.7", "20"});

        generator = new CifMersenneTwister(SEED);
        idist = new BinomialDistribution(generator, 0.5, 40);
        idist2 = new org.apache.commons.math3.distribution.BinomialDistribution(40, 0.5);
        generateInt(idist, idist2, "binomial", new String[] {"0.5", "40"});

        // Geometric distribution:
        // http://en.wikipedia.org/wiki/Geometric_distribution
        generator = new CifMersenneTwister(SEED);
        idist = new GeometricDistribution(generator, 0.2);
        idist2 = new org.apache.commons.math3.distribution.PascalDistribution(1, 0.2);
        generateInt(idist, idist2, "geometric", new String[] {"0.2"});

        generator = new CifMersenneTwister(SEED);
        idist = new GeometricDistribution(generator, 0.5);
        idist2 = new org.apache.commons.math3.distribution.PascalDistribution(1, 0.5);
        generateInt(idist, idist2, "geometric", new String[] {"0.5"});

        generator = new CifMersenneTwister(SEED);
        idist = new GeometricDistribution(generator, 0.8);
        idist2 = new org.apache.commons.math3.distribution.PascalDistribution(1, 0.8);
        generateInt(idist, idist2, "geometric", new String[] {"0.8"});

        // Poisson distribution:
        // http://en.wikipedia.org/wiki/Poisson_distribution
        generator = new CifMersenneTwister(SEED);
        idist = new PoissonDistribution(generator, 0.1);
        idist2 = new org.apache.commons.math3.distribution.PoissonDistribution(0.1);
        generateInt(idist, idist2, "poisson", new String[] {"0.1"});

        generator = new CifMersenneTwister(SEED);
        idist = new PoissonDistribution(generator, 1.0);
        idist2 = new org.apache.commons.math3.distribution.PoissonDistribution(1.0);
        generateInt(idist, idist2, "poisson", new String[] {"1.0"});

        generator = new CifMersenneTwister(SEED);
        idist = new PoissonDistribution(generator, 4.0);
        idist2 = new org.apache.commons.math3.distribution.PoissonDistribution(4.0);
        generateInt(idist, idist2, "poisson", new String[] {"4.0"});

        generator = new CifMersenneTwister(SEED);
        idist = new PoissonDistribution(generator, 10.0);
        idist2 = new org.apache.commons.math3.distribution.PoissonDistribution(10.0);
        generateInt(idist, idist2, "poisson", new String[] {"10.0"});

        // Uniform integer distribution.
        generator = new CifMersenneTwister(SEED);
        idist = new UniformIntegerDistribution(generator, -2, 5);
        idist2 = new org.apache.commons.math3.distribution.UniformIntegerDistribution(-2, 4);
        generateInt(idist, idist2, "uniform", new String[] {"-2", "5"});

        generator = new CifMersenneTwister(SEED);
        idist = new UniformIntegerDistribution(generator, 0, 3);
        idist2 = new org.apache.commons.math3.distribution.UniformIntegerDistribution(0, 2);
        generateInt(idist, idist2, "uniform", new String[] {"0", "3"});

        generator = new CifMersenneTwister(SEED);
        idist = new UniformIntegerDistribution(generator, -100, 100);
        idist2 = new org.apache.commons.math3.distribution.UniformIntegerDistribution(-100, 99);
        generateInt(idist, idist2, "uniform", new String[] {"-100", "100"});

        // Real distributions.
        RealDistribution rdist;
        org.apache.commons.math3.distribution.RealDistribution rdist2;

        // Beta distribution:
        // http://en.wikipedia.org/wiki/Beta_distribution
        generator = new CifMersenneTwister(SEED);
        rdist = new BetaDistribution(generator, 0.5, 0.5);
        rdist2 = new org.apache.commons.math3.distribution.BetaDistribution(0.5, 0.5);
        generateReal(rdist, rdist2, "beta", new String[] {"0.5", "0.5"});

        generator = new CifMersenneTwister(SEED);
        rdist = new BetaDistribution(generator, 5.0, 1.0);
        rdist2 = new org.apache.commons.math3.distribution.BetaDistribution(5.0, 1.0);
        generateReal(rdist, rdist2, "beta", new String[] {"5.0", "1.0"});

        generator = new CifMersenneTwister(SEED);
        rdist = new BetaDistribution(generator, 1.0, 3.0);
        rdist2 = new org.apache.commons.math3.distribution.BetaDistribution(1.0, 3.0);
        generateReal(rdist, rdist2, "beta", new String[] {"1.0", "3.0"});

        generator = new CifMersenneTwister(SEED);
        rdist = new BetaDistribution(generator, 2.0, 2.0);
        rdist2 = new org.apache.commons.math3.distribution.BetaDistribution(2.0, 2.0);
        generateReal(rdist, rdist2, "beta", new String[] {"2.0", "2.0"});

        generator = new CifMersenneTwister(SEED);
        rdist = new BetaDistribution(generator, 2.0, 5.0);
        rdist2 = new org.apache.commons.math3.distribution.BetaDistribution(2.0, 5.0);
        generateReal(rdist, rdist2, "beta", new String[] {"2.0", "5.0"});

        // Erlang distribution:
        // http://en.wikipedia.org/wiki/Erlang_distribution
        generator = new CifMersenneTwister(SEED);
        rdist = new ErlangDistribution(generator, 1, 2.0);
        rdist2 = new org.apache.commons.math3.distribution.GammaDistribution(1.0, 2.0);
        generateReal(rdist, rdist2, "erlang", new String[] {"1", "2.0"});

        generator = new CifMersenneTwister(SEED);
        rdist = new ErlangDistribution(generator, 2, 2.0);
        rdist2 = new org.apache.commons.math3.distribution.GammaDistribution(2.0, 2.0);
        generateReal(rdist, rdist2, "erlang", new String[] {"2", "2.0"});

        generator = new CifMersenneTwister(SEED);
        rdist = new ErlangDistribution(generator, 3, 2.0);
        rdist2 = new org.apache.commons.math3.distribution.GammaDistribution(3.0, 2.0);
        generateReal(rdist, rdist2, "erlang", new String[] {"3", "2.0"});

        generator = new CifMersenneTwister(SEED);
        rdist = new ErlangDistribution(generator, 5, 1.0);
        rdist2 = new org.apache.commons.math3.distribution.GammaDistribution(5.0, 1.0);
        generateReal(rdist, rdist2, "erlang", new String[] {"5", "1.0"});

        generator = new CifMersenneTwister(SEED);
        rdist = new ErlangDistribution(generator, 9, 0.5);
        rdist2 = new org.apache.commons.math3.distribution.GammaDistribution(9.0, 0.5);
        generateReal(rdist, rdist2, "erlang", new String[] {"9", "0.5"});

        // Exponential distribution:
        // http://en.wikipedia.org/wiki/Exponential_distribution
        generator = new CifMersenneTwister(SEED);
        rdist = new ExponentialDistribution(generator, 2.0); // 1 / 0.5
        rdist2 = new org.apache.commons.math3.distribution.ExponentialDistribution(2.0);
        generateReal(rdist, rdist2, "exponential", new String[] {"2.0"});

        generator = new CifMersenneTwister(SEED);
        rdist = new ExponentialDistribution(generator, 1.0);
        rdist2 = new org.apache.commons.math3.distribution.ExponentialDistribution(1.0);
        generateReal(rdist, rdist2, "exponential", new String[] {"1.0"});

        generator = new CifMersenneTwister(SEED);
        rdist = new ExponentialDistribution(generator, 0.667); // 1 / 1.50
        rdist2 = new org.apache.commons.math3.distribution.ExponentialDistribution(0.667);
        generateReal(rdist, rdist2, "exponential", new String[] {"0.667"});

        // Gamma distribution:
        // http://en.wikipedia.org/wiki/Gamma_distribution
        generator = new CifMersenneTwister(SEED);
        rdist = new GammaDistribution(generator, 1.0, 2.0);
        rdist2 = new org.apache.commons.math3.distribution.GammaDistribution(1.0, 2.0);
        generateReal(rdist, rdist2, "gamma", new String[] {"1.0", "2.0"});

        generator = new CifMersenneTwister(SEED);
        rdist = new GammaDistribution(generator, 2.0, 2.0);
        rdist2 = new org.apache.commons.math3.distribution.GammaDistribution(2.0, 2.0);
        generateReal(rdist, rdist2, "gamma", new String[] {"2.0", "2.0"});

        generator = new CifMersenneTwister(SEED);
        rdist = new GammaDistribution(generator, 3.0, 2.0);
        rdist2 = new org.apache.commons.math3.distribution.GammaDistribution(3.0, 2.0);
        generateReal(rdist, rdist2, "gamma", new String[] {"3.0", "2.0"});

        generator = new CifMersenneTwister(SEED);
        rdist = new GammaDistribution(generator, 5.0, 1.0);
        rdist2 = new org.apache.commons.math3.distribution.GammaDistribution(5.0, 1.0);
        generateReal(rdist, rdist2, "gamma", new String[] {"5.0", "1.0"});

        generator = new CifMersenneTwister(SEED);
        rdist = new GammaDistribution(generator, 9.0, 0.5);
        rdist2 = new org.apache.commons.math3.distribution.GammaDistribution(9.0, 0.5);
        generateReal(rdist, rdist2, "gamma", new String[] {"9.0", "0.5"});

        // LogNormal distribution:
        // http://en.wikipedia.org/wiki/Log-normal_distribution
        generator = new CifMersenneTwister(SEED);
        rdist = new LogNormalDistribution(generator, 0.0, 0.5);
        rdist2 = new org.apache.commons.math3.distribution.LogNormalDistribution(0.0, Math.sqrt(0.5));
        generateReal(rdist, rdist2, "lognormal", new String[] {"0.0", "0.5"});

        generator = new CifMersenneTwister(SEED);
        rdist = new LogNormalDistribution(generator, 0.0, 1.0);
        rdist2 = new org.apache.commons.math3.distribution.LogNormalDistribution(0.0, Math.sqrt(1.0));
        generateReal(rdist, rdist2, "lognormal", new String[] {"0.0", "1.0"});

        generator = new CifMersenneTwister(SEED);
        rdist = new LogNormalDistribution(generator, 0.0, 1.5);
        rdist2 = new org.apache.commons.math3.distribution.LogNormalDistribution(0.0, Math.sqrt(1.5));
        generateReal(rdist, rdist2, "lognormal", new String[] {"0.0", "1.5"});

        generator = new CifMersenneTwister(SEED);
        rdist = new LogNormalDistribution(generator, -2.0, 0.5);
        rdist2 = new org.apache.commons.math3.distribution.LogNormalDistribution(-2.0, Math.sqrt(0.5));
        generateReal(rdist, rdist2, "lognormal", new String[] {"-2.0", "0.5"});

        // Normal distribution:
        // http://en.wikipedia.org/wiki/Normal_distribution
        generator = new CifMersenneTwister(SEED);
        rdist = new NormalDistribution(generator, 0.0, 0.2);
        rdist2 = new org.apache.commons.math3.distribution.NormalDistribution(0.0, Math.sqrt(0.2));
        generateReal(rdist, rdist2, "normal", new String[] {"0.0", "0.2"});

        generator = new CifMersenneTwister(SEED);
        rdist = new NormalDistribution(generator, 0.0, 1.0);
        rdist2 = new org.apache.commons.math3.distribution.NormalDistribution(0.0, Math.sqrt(1.0));
        generateReal(rdist, rdist2, "normal", new String[] {"0.0", "1.0"});

        generator = new CifMersenneTwister(SEED);
        rdist = new NormalDistribution(generator, 0.0, 5.0);
        rdist2 = new org.apache.commons.math3.distribution.NormalDistribution(0.0, Math.sqrt(5.0));
        generateReal(rdist, rdist2, "normal", new String[] {"0.0", "5.0"});

        generator = new CifMersenneTwister(SEED);
        rdist = new NormalDistribution(generator, -2.0, 0.5);
        rdist2 = new org.apache.commons.math3.distribution.NormalDistribution(-2.0, Math.sqrt(0.5));
        generateReal(rdist, rdist2, "normal", new String[] {"-2.0", "0.5"});

        // Random distribution.
        generator = new CifMersenneTwister(SEED);
        rdist = new RandomDistribution(generator);
        rdist2 = new org.apache.commons.math3.distribution.UniformRealDistribution(0.0, 1.0);
        generateReal(rdist, rdist2, "random", new String[] {});

        // Triangle distribution.
        generator = new CifMersenneTwister(SEED);
        rdist = new TriangleDistribution(generator, -2.0, 3.0, 5.0);
        rdist2 = new org.apache.commons.math3.distribution.TriangularDistribution(-2.0, 3.0, 5.0);
        generateReal(rdist, rdist2, "triangle", new String[] {"-2.0", "3.0", "5.0"});

        // Uniform real distribution.
        generator = new CifMersenneTwister(SEED);
        rdist = new UniformRealDistribution(generator, 0.0, 1.0);
        rdist2 = new org.apache.commons.math3.distribution.UniformRealDistribution(0.0, 1.0);
        generateReal(rdist, rdist2, "uniform", new String[] {"0.0", "1.0"});

        generator = new CifMersenneTwister(SEED);
        rdist = new UniformRealDistribution(generator, 0.0, 2.0);
        rdist2 = new org.apache.commons.math3.distribution.UniformRealDistribution(0.0, 2.0);
        generateReal(rdist, rdist2, "uniform", new String[] {"0.0", "2.0"});

        generator = new CifMersenneTwister(SEED);
        rdist = new UniformRealDistribution(generator, -2.0, 5.0);
        rdist2 = new org.apache.commons.math3.distribution.UniformRealDistribution(-2.0, 5.0);
        generateReal(rdist, rdist2, "uniform", new String[] {"-2.0", "5.0"});

        // Weibull distribution:
        // http://en.wikipedia.org/wiki/Weibull_distribution
        generator = new CifMersenneTwister(SEED);
        rdist = new WeibullDistribution(generator, 0.5, 1.0);
        rdist2 = new org.apache.commons.math3.distribution.WeibullDistribution(0.5, 1.0);
        generateReal(rdist, rdist2, "weibull", new String[] {"0.5", "1.0"});

        generator = new CifMersenneTwister(SEED);
        rdist = new WeibullDistribution(generator, 1.0, 1.0);
        rdist2 = new org.apache.commons.math3.distribution.WeibullDistribution(1.0, 1.0);
        generateReal(rdist, rdist2, "weibull", new String[] {"1.0", "1.0"});

        generator = new CifMersenneTwister(SEED);
        rdist = new WeibullDistribution(generator, 1.5, 1.0);
        rdist2 = new org.apache.commons.math3.distribution.WeibullDistribution(1.5, 1.0);
        generateReal(rdist, rdist2, "weibull", new String[] {"1.5", "1.0"});

        generator = new CifMersenneTwister(SEED);
        rdist = new WeibullDistribution(generator, 5.0, 1.0);
        rdist2 = new org.apache.commons.math3.distribution.WeibullDistribution(5.0, 1.0);
        generateReal(rdist, rdist2, "weibull", new String[] {"5.0", "1.0"});

        generator = new CifMersenneTwister(SEED);
        rdist = new WeibullDistribution(generator, 5.0, 2.0);
        rdist2 = new org.apache.commons.math3.distribution.WeibullDistribution(5.0, 2.0);
        generateReal(rdist, rdist2, "weibull", new String[] {"5.0", "2.0"});

        generator = new CifMersenneTwister(SEED);
        rdist = new WeibullDistribution(generator, 5.0, 0.2);
        rdist2 = new org.apache.commons.math3.distribution.WeibullDistribution(5.0, 0.2);
        generateReal(rdist, rdist2, "weibull", new String[] {"5.0", "0.2"});
    }

    /**
     * Generates Gnuplot files for the generation of plots for the actual sampled values and the expected/theoretical
     * distribution.
     *
     * @param sampleDistr The distribution to sample.
     * @param idealDistr The distribution from which to obtain the ideal densities.
     * @param name The name of the distribution.
     * @param params The distribution parameters.
     */
    public static void generateBool(BooleanDistribution sampleDistr,
            org.apache.commons.math3.distribution.IntegerDistribution idealDistr, String name, String[] params)
    {
        generateInt(new BoolToIntDistribution(sampleDistr), idealDistr, name, params);
    }

    /**
     * Generates Gnuplot files for the generation of plots for the actual sampled values and the expected/theoretical
     * distribution.
     *
     * @param sampleDistr The distribution to sample.
     * @param idealDistr The distribution from which to obtain the ideal densities.
     * @param name The name of the distribution.
     * @param params The distribution parameters.
     */
    public static void generateInt(IntegerDistribution sampleDistr,
            org.apache.commons.math3.distribution.IntegerDistribution idealDistr, String name, String[] params)
    {
        // Get file name base, and readable distribution name.
        String fileName = name + "_" + StringUtils.join(params, "_");
        String distrName = name + "(" + StringUtils.join(params, ", ") + ")";

        // Print the distribution we are working on.
        System.out.println(distrName);

        // Get and check integer value of DBARS.
        final int IBARS = (int)DBARS;
        Assert.check((int)Math.floor(DBARS) == IBARS);

        // Sample the distribution.
        int[] samples = new int[SAMPLE_COUNT];
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < SAMPLE_COUNT; i++) {
            int sample = sampleDistr.sample();
            samples[i] = sample;
            min = Math.min(min, sample);
            max = Math.max(max, sample);
        }

        // Calculate range.
        int range = max - min + 1;

        // Calculate counts for each of the values in the range.
        int[] counts = new int[range];
        for (int i = 0; i < SAMPLE_COUNT; i++) {
            int sample = samples[i];
            sample -= min;

            counts[sample]++;
        }

        // Write data file.
        AppStream stream = new FileAppStream(fileName + ".dat");

        int clen = String.valueOf(SAMPLE_COUNT).length();
        for (int i = 0; i < range; i++) {
            int count = counts[i];
            int value = min + i;
            double chance = count / (double)SAMPLE_COUNT;
            double chance2 = idealDistr.probability(value);

            stream.printf("%" + clen + "d   %.4f   %.4f\n", value, chance, chance2);
        }

        stream.close();

        // Write Gnuplot script file.
        stream = new FileAppStream(fileName + ".plt");

        stream.println("# Requires Gnuplot 4.4 or higher.");
        stream.println();
        stream.println("set terminal pngcairo dashed size 1024,768 font 'sans'");
        stream.printf("set output \"%s.png\"\n", fileName);
        stream.println();
        stream.printf("set title \"%s\"\n", distrName);
        stream.println("set key top right vertical");
        stream.println();
        stream.println("set style line 1 linecolor rgb '#dc3912' lt 1 lw 2");
        stream.println("set style line 2 linecolor rgb '#3366cc' lt 2 lw 2");
        stream.println();
        stream.println("set style line 11 lc rgb '#808080' lt 1");
        stream.println("set border 3 back ls 11");
        stream.println("set border linewidth 1.5");
        stream.println();
        stream.println("set style line 12 lc rgb'#808080' lt 0 lw 1");
        stream.println("set grid back ls 12");
        stream.println("set grid back");
        stream.println();
        stream.println("set autoscale yfixmin");
        stream.println("set autoscale yfixmax");
        stream.println("set autoscale xfixmin");
        stream.println("set autoscale xfixmax");
        stream.println("set offsets 0, 0, graph 0.05, graph 0.05");
        stream.println();
        stream.println("plot \\");
        stream.printf("  \"%s.dat\" using 1:($2) title \"sampled\" with lines ls 1, \\\n", fileName);
        stream.printf("  \"%s.dat\" using 1:($3) title \"ideal\" with lines ls 2\n", fileName);

        stream.close();
    }

    /**
     * Generates Gnuplot files for the generation of plots for the actual sampled values and the expected/theoretical
     * distribution.
     *
     * @param sampleDistr The distribution to sample.
     * @param idealDistr The distribution from which to obtain the ideal densities.
     * @param name The name of the distribution.
     * @param params The distribution parameters.
     */
    public static void generateReal(RealDistribution sampleDistr,
            org.apache.commons.math3.distribution.RealDistribution idealDistr, String name, String[] params)
    {
        // Get file name base, and readable distribution name.
        String fileName = name + "_" + StringUtils.join(params, "_");
        String distrName = name + "(" + StringUtils.join(params, ", ") + ")";

        // Print the distribution we are working on.
        System.out.println(distrName);

        // Get and check integer value of DBARS.
        final int IBARS = (int)DBARS;
        Assert.check((int)Math.floor(DBARS) == IBARS);

        // Sample the distribution.
        double[] samples = new double[SAMPLE_COUNT];
        double mind = Double.MAX_VALUE;
        double maxd = -mind;
        for (int i = 0; i < SAMPLE_COUNT; i++) {
            double sample = sampleDistr.sample();
            samples[i] = sample;
            mind = Math.min(mind, sample);
            maxd = Math.max(maxd, sample);
        }
        int mini = (int)Math.floor(mind);
        int maxi = (int)Math.floor(maxd);

        // Calculate range and fraction.
        double range = maxi - mini + 1;
        double fraction = range / DBARS;

        // Calculate counts for each of the bars.
        int[] counts = new int[IBARS];
        for (int i = 0; i < SAMPLE_COUNT; i++) {
            double sample = samples[i];
            sample -= mini;

            double sfraction = sample / fraction;
            counts[(int)sfraction]++;
        }

        // Write data file.
        AppStream stream = new FileAppStream(fileName + ".dat");

        for (int i = 0; i < IBARS; i++) {
            int count = counts[i];
            double value = mini + (i * fraction);
            double chance = count / (double)SAMPLE_COUNT / range * IBARS;
            double chance2 = idealDistr.density(value + fraction * 0.5);

            stream.printf("%.4f   %.4f   %.4f\n", value, chance, chance2);
        }

        stream.close();

        // Write Gnuplot script file.
        stream = new FileAppStream(fileName + ".plt");

        stream.println("# Requires Gnuplot 4.4 or higher.");
        stream.println();
        stream.println("set terminal pngcairo size 1024,768 font 'sans'");
        stream.printf("set output \"%s.png\"\n", fileName);
        stream.println();
        stream.printf("set title \"%s\"\n", distrName);
        stream.println("set key top right vertical");
        stream.println();
        stream.println("set style line 1 linecolor rgb '#dc3912' lt 1 lw 2");
        stream.println("set style line 2 linecolor rgb '#3366cc' lt 1 lw 2");
        stream.println();
        stream.println("set style line 11 lc rgb '#808080' lt 1");
        stream.println("set border 3 back ls 11");
        stream.println("set border linewidth 1.5");
        stream.println();
        stream.println("set style line 12 lc rgb'#808080' lt 0 lw 1");
        stream.println("set grid back ls 12");
        stream.println("set grid back");
        stream.println();
        stream.println("set autoscale yfixmin");
        stream.println("set autoscale yfixmax");
        stream.println("set autoscale xfixmin");
        stream.println("set autoscale xfixmax");
        stream.println("set offsets 0, 0, graph 0.05, graph 0.05");
        stream.println();
        stream.println("plot \\");
        stream.printf("  \"%s.dat\" using 1:($2) title \"sampled\" with lines ls 1, \\\n", fileName);
        stream.printf("  \"%s.dat\" using 1:($3) title \"ideal\" with lines ls 2\n", fileName);

        stream.close();
    }

    /**
     * Wrapper around a boolean distribution, to convert it to an integer distribution. Converts {@code false} to
     * {@code 0}, and {@code true} to {@code 1}.
     */
    private static class BoolToIntDistribution extends IntegerDistribution {
        /** The boolean distribution. */
        private final BooleanDistribution distr;

        /**
         * Constructor for the {@link BoolToIntDistribution} class.
         *
         * @param distr The boolean distribution.
         */
        public BoolToIntDistribution(BooleanDistribution distr) {
            this.distr = distr;
        }

        @Override
        public int sample() {
            return distr.sample() ? 1 : 0;
        }

        @Override
        public IntegerDistribution copy() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String toString() {
            return distr.toString();
        }
    }
}
