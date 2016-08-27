package com.alexstyl.specialdates;

/*
 * A simplified version of Java 8's Optional class.
 * See: https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html
 */
public class Optional<T> {

    private final T object;

    public Optional(T object) {
        this.object = object;
    }

    public T get() {
        if (!isPresent()) {
            throw new IllegalStateException("Optional was not present");
        }
        return object;
    }

    public boolean isPresent() {
        return object != null;
    }

    public static <T> Optional<T> absent() {
        return new Optional<>(null);
    }

    public boolean contains(T object) {
        if (isPresent()) {
            return get().equals(object);
        } else {
            return object == null;
        }
    }
}
