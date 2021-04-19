package org.eclipse.escet.common.raildiagrams.solver;

import static org.eclipse.escet.common.java.Strings.fmt;

/** Variable in the constraint problem. */
public class Variable {
    /** (Unique) index number to identify the variable. */
    public final int index;

    /** Name of the variable, may be {@code null}. */
    public final String name;

    /**
     * Constructor of the {@link Variable} class.
     *
     * @param name Name of the variable, may be {@code null}.
     * @param index (Unique) index number to identify the variable.
     */
    public Variable(String name, int index) {
        this.name = name;
        this.index = index;
    }

    @Override
    public String toString() {
        if (name == null) return fmt("Var(#%d)", index);
        return fmt("Var(\"%s\"#%d)", name, index);
    }
}
