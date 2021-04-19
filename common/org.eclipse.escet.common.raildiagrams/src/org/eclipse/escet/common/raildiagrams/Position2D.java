package org.eclipse.escet.common.raildiagrams;


import static org.eclipse.escet.common.java.Strings.fmt;

/** Class for storing a position. */
public class Position2D {
    /** Horizontal position. */
    public final double x;

    /** Vertical position. */
    public final double y;

    /**
     * Constructor of the {@link Position2D} class.
     *
     * @param x Horizontal position.
     * @param x Vertical position.
     */
    public Position2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return fmt("Position2D(%.1f, %.1f)", x, y);
    }
}
