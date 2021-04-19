package org.eclipse.escet.common.raildiagrams.railroad;

import static org.eclipse.escet.common.java.Lists.list;

import java.awt.Graphics2D;
import java.util.List;

import org.eclipse.escet.common.raildiagrams.Configuration;
import org.eclipse.escet.common.raildiagrams.graphics.Area;
import org.eclipse.escet.common.raildiagrams.solver.Solver;
import org.eclipse.escet.common.raildiagrams.solver.Variable;

/** A (rectangular) part of a rail-road diagram, also acts as the edges of its contents. */
public abstract class DiagramElement {
    /** Variable storage, constraint storage, and solver of the variable values. */
    public final Solver solver = new Solver();

    /** Displayed graphics within the element. */
    private final List<Area> graphics = list();

    /** Sub-diagrams of the element. */
    private final List<ProxyDiagramElement> childDiagramElements = list();

    /** Variable holding the X coordinate of the left side of the diagram element. */
    public final Variable left;

    /** Variable holding the X coordinate of the right side of the diagram element. */
    public final Variable right;

    /** Variable holding the Y coordinate of the top side of the diagram element. */
    public final Variable top;

    /** Variable holding the Y coordinate of the bottom side of the diagram element. */
    public final Variable bottom;

    /** Variable holding the Y coordinate of the top side of the diagram element rail connection point. */
    public final Variable connectTop;

    /** Constructor of the {@link DiagramElement} class. */
    public DiagramElement() {
        left = solver.newVar("diagram.left");
        right = solver.newVar("diagram.right");
        top = solver.newVar("diagram.top");
        bottom = solver.newVar("diagram.bottom");
        connectTop = solver.newVar("diagram.connectTop");
    }

    /**
     * Add a graphic element to this diagram element.
     *
     * @param graphic The graphic element to add.
     */
    public void addGraphic(Area graphic) {
        graphics.add(graphic);
    }

    /**
     * Add all graphics to this diagram element.
     *
     * @param graphics The graphic elements to add.
     */
    public void addGraphics(Area... graphics) {
        for (Area graphic: graphics) this.graphics.add(graphic);
    }

    /**
     * Add a child element to this diagram element.
     *
     * @param element Element to add.
     * @param elemName Name of the element.
     * @return The proxied element with variables from the element environment.
     *      Use this for connecting the child element to other parts of the diagram here.
     */
    public ProxyDiagramElement addDiagramElement(DiagramElement element, String elemName) {
        ProxyDiagramElement proxyDiagram = new ProxyDiagramElement(element, elemName, solver);
        childDiagramElements.add(proxyDiagram);
        return proxyDiagram;
    }

    /**
     * Creates the node in the diagram.
     *
     * @param config Configuration to use.
     * @param direction Direction of flow through the node, positive is left to right, negative is right to left.
     */
    abstract public void create(Configuration config, int direction);

    /**
     * Paint the graphic elements and the child diagrams to the graphics output.
     *
     * @param left Left position of the area that may be used for painting.
     * @param top Top position of the area that may be used for painting.
     * @param solver Solver containing variable values.
     * @param gd Graphics output handle.
     */
    public void paint(double left, double top, Graphics2D gd) {
        for (Area graphic: graphics) graphic.paint(left, top, solver, gd);

        for (ProxyDiagramElement childElement: childDiagramElements) {
            double childLeft = left + solver.getVarValue(childElement.left);
            double childTop = top + solver.getVarValue(childElement.top);
            childElement.paint(childLeft, childTop, gd);
        }
    }
}
