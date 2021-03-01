//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.chi.documentation.utils;

import static org.eclipse.escet.common.java.Strings.fmt;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.math3.distribution.AbstractIntegerDistribution;
import org.apache.commons.math3.distribution.BetaDistribution;
import org.apache.commons.math3.distribution.BinomialDistribution;
import org.apache.commons.math3.distribution.ExponentialDistribution;
import org.apache.commons.math3.distribution.GammaDistribution;
import org.apache.commons.math3.distribution.LogNormalDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.distribution.TriangularDistribution;
import org.apache.commons.math3.distribution.WeibullDistribution;

/** Generate distribution plots for inclusion in the documentation. */
public class GenerateDistributions {
    /** Constructor for the {@link GenerateDistributions} class. */
    private GenerateDistributions() {
        // Static class.
    }

    @SuppressWarnings("javadoc")
    public static void generateDensityFunction(double low, double high, double increment, RealDistribution rd,
            String filename)
    {
        BufferedWriter out;
        try {
            FileWriter fstream = new FileWriter(filename);
            out = new BufferedWriter(fstream);

            while (low < high) {
                String line = fmt("%f\t%f\n", low, rd.density(low));
                out.write(line);
                low += increment;
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("javadoc")
    public static void generateDensityFunction(int low, int high, AbstractIntegerDistribution id, String filename) {
        BufferedWriter out;
        try {
            FileWriter fstream = new FileWriter(filename);
            out = new BufferedWriter(fstream);

            while (low < high) {
                String line = fmt("%d\t%f\n", low, id.probability(low));
                out.write(line);
                low++;
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Run the generator program.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        RealDistribution rd;
        AbstractIntegerDistribution id;
        double step = 0.01;

        rd = new GammaDistribution(1.0, 2.0);
        generateDensityFunction(0, 20, step, rd, "gamma_1.0_2.0.dat");

        rd = new GammaDistribution(3.0, 2.0);
        generateDensityFunction(0, 20, step, rd, "gamma_3.0_2.0.dat");

        rd = new GammaDistribution(6.0, 2.0);
        generateDensityFunction(0, 20, step, rd, "gamma_6.0_2.0.dat");

        rd = new GammaDistribution(6.0, 0.5);
        generateDensityFunction(0, 20, step, rd, "gamma_6.0_0.5.dat");

        rd = new TriangularDistribution(1, 2, 4);
        generateDensityFunction(1, 4.01, step, rd, "triangular_1_2_4.dat");

        rd = new NormalDistribution(3, 1);
        generateDensityFunction(0, 10.01, step, rd, "normal_3_1.dat");

        rd = new NormalDistribution(5, 2);
        generateDensityFunction(0, 10.01, step, rd, "normal_5_2.dat");

        rd = new TriangularDistribution(1, 2, 4);
        generateDensityFunction(1, 4.01, step, rd, "triangular_1_2_4.dat");

        id = new BinomialDistribution(20, 0.7);
        generateDensityFunction(0, 40, id, "binomial_20_0.7.dat");

        id = new BinomialDistribution(20, 0.5);
        generateDensityFunction(0, 40, id, "binomial_20_0.5.dat");

        id = new BinomialDistribution(40, 0.5);
        generateDensityFunction(0, 40, id, "binomial_40_0.5.dat");

        id = new PoissonDistribution(4.0);
        generateDensityFunction(0, 20, id, "poisson_4.0.dat");

        id = new PoissonDistribution(10.0);
        generateDensityFunction(0, 20, id, "poisson_10.0.dat");

        rd = new BetaDistribution(5.0, 1.5);
        generateDensityFunction(0, 1.0, 0.02, rd, "beta_5.0_1.5.dat");

        rd = new BetaDistribution(1.5, 3.0);
        generateDensityFunction(0, 1.0, 0.02, rd, "beta_1.5_3.0.dat");

        rd = new BetaDistribution(2.0, 2.0);
        generateDensityFunction(0, 1.0, 0.02, rd, "beta_2.0_2.0.dat");

        rd = new BetaDistribution(0.8, 0.5);
        generateDensityFunction(0.01, 0.99, 0.01, rd, "beta_0.8_0.5.dat");

        rd = new ExponentialDistribution(0.5);
        generateDensityFunction(0, 5.0, step, rd, "exponential_0.5.dat");

        rd = new ExponentialDistribution(1.0);
        generateDensityFunction(0, 5.0, step, rd, "exponential_1.0.dat");

        rd = new ExponentialDistribution(1.5);
        generateDensityFunction(0, 5.0, step, rd, "exponential_1.5.dat");

        rd = new LogNormalDistribution(0, 1);
        generateDensityFunction(0, 4.0, step, rd, "lognormal_0.0_1.0.dat");

        rd = new LogNormalDistribution(0, 0.5);
        generateDensityFunction(0, 4.0, step, rd, "lognormal_0.0_0.5.dat");

        rd = new LogNormalDistribution(0, 0.25);
        generateDensityFunction(0, 4.0, step, rd, "lognormal_0.0_0.25.dat");

        rd = new WeibullDistribution(0.5, 1);
        generateDensityFunction(0, 3.0, step, rd, "weibull_0.5_1.0.dat");

        rd = new WeibullDistribution(1, 1);
        generateDensityFunction(0, 3.0, step, rd, "weibull_1.0_1.0.dat");

        rd = new WeibullDistribution(1.5, 1);
        generateDensityFunction(0, 3.0, step, rd, "weibull_1.5_1.0.dat");

        rd = new WeibullDistribution(5, 1);
        generateDensityFunction(0, 3.0, step, rd, "weibull_5.0_1.0.dat");
    }
}
