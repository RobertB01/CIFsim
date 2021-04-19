package org.eclipse.escet.common.java;

/**
 * Class that explicitly expresses its value may be {@code null}.
 *
 * @param <T> Type of the value.
 */
public class Optional<T> {
    /** Stored value that may or may not be {@code null}. */
    private final T value;

    /**
     * Constructor of the {@link Optional} class.
     *
     * @param value Value to store in the instance.
     */
    public Optional(T value) {
        this.value = value;
    }

    /**
     * Get the raw value.
     *
     * <p>No checking is done whether the value is {@code null}.</p>
     *
     * @return The stored value.
     * @see #isNull
     * @see #getValue
     */
    public T rawGet() {
        return value;
    }

    /**
     * Query whether the stored value is {@code null}.
     *
     * @return {@code true} if the value is {@code null}, else {@code false}.
     */
    public boolean isNull() {
        return value == null;
    }

    /**
     * Get the value only if it is not {@code null}.
     *
     * @return The stored value.
     * @throws AssertionError if the value of the instance is {@code null}.
     */
    public T getValue() {
        Assert.notNull(value);
        return value;
    }
}
