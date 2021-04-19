package org.eclipse.escet.common.raildiagrams;

/** Class holding size of a given text, and offset relative to top-left. */
public class TextSizeOffset {
    /** Offset of the text relative to its top-left corner. */
    public final Position2D offset;

    /** Size of the text. */
    public final Size2D size;

    /**
     * Constructor of the {@link TextSizeOffset} class.
     *
     * @param offset Offset of the text relative to its top-left corner.
     * @param size Size of the text.
     */
    public TextSizeOffset(Position2D offset, Size2D size) {
        this.offset = offset;
        this.size = size;
    }
}
