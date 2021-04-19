package org.eclipse.escet.common.raildiagrams.graphics;

import static org.eclipse.escet.common.raildiagrams.graphics.PaintSupport.setLineWidth;

import java.awt.Color;
import java.awt.Graphics2D;

import org.eclipse.escet.common.raildiagrams.solver.Solver;

/** Horizontal line. */
public class HorLine extends Area {
    /** Color of the rail line. */
    public final Color railColor;

    /**
     * Constructor of the {@link HorLine} class.
     *
     * @param solver Variable and relation storage.
     * @param prefix Name prefix of the horizontal line.
     * @param railColor Color of the line.
     * @param lineWidth Width of the line.
     */
    public HorLine(Solver solver, String prefix, Color railColor, double lineWidth) {
        super(solver, prefix + ".hor");
        this.railColor = railColor;

        solver.addEq(top, lineWidth, bottom);
    }

    @Override
    public void paint(double baseLeft, double baseTop, Solver solver, Graphics2D gd) {
        double top = solver.getVarValue(this.top) + baseTop;
        double bottom = solver.getVarValue(this.bottom) + baseTop - 1;
        double width = bottom - top + 1;
        double center = (bottom + top + 1) / 2;
        double left = solver.getVarValue(this.left) + baseLeft;
        double right = solver.getVarValue(this.right) + baseLeft - 1;
        if (right <= left) return;

        gd.setColor(railColor);
        setLineWidth(gd, (int)width);
        gd.drawLine((int)left, (int)center, (int)right, (int)center);
    }
}
