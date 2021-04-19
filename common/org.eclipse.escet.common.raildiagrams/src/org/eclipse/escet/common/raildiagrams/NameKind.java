package org.eclipse.escet.common.raildiagrams;

/** Available kinds of nodes. */
public enum NameKind {
    /** Name represents a fixed terminal, like 'colon'. */
    TERMINAL("terminal"),

    /** Name represents a variable terminal, like 'number', or 'identifier'. */
    META_TERMINAL("meta-terminal"),

    /** Name represents another railroad diagram. */
    NON_TERMINAL("nonterminal"),

    /** Name represents a branch label. */
    LABEL("branch-label"),

    /** Name is a header of a diagram. */
    HEADER("diagram-header");

    /** Prefix to use for the name kind in the configuration file. */
    public final String configPrefix;

    /**
     * Constructor of the {@link NameKind} enumeration.
     *
     * @param configPrefix Prefix to use for the name kind in the configuration file.
     */
    private NameKind(String configPrefix) {
        this.configPrefix = configPrefix;
    }
}
