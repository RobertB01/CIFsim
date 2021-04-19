package org.eclipse.escet.common.raildiagrams;

import static org.eclipse.escet.common.java.Strings.fmt;

/** Class for storing a size. */
public class Size2D {
    /** Width of the size. */
    public final double width;

    /** Height of the size. */
    public final double height;

    /**
     * Constructor of the {@link Size2D} class.
     *
     * @param width Width of the size.
     * @param height Height of the size.
     */
    public Size2D(double width, double height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public String toString() {
        return fmt("Size2D(%.1f, %.1f)", width, height);
    }
}
