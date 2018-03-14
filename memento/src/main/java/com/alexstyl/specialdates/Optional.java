package com.alexstyl.specialdates;

import android.support.annotation.Nullable;

/*
 * A simplified version of Java 8's Optional class.
 * See: https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html
 */
public class Optional<T> {

    @Nullable
    private final T object;

    public Optional(@Nullable T object) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Optional<?> optional = (Optional<?>) o;

        return object != null ? object.equals(optional.object) : optional.object == null;

    }

    @Override
    public int hashCode() {
        return object != null ? object.hashCode() : 0;
    }

    @Override
    public String toString() {
        return String.valueOf(isPresent() ? object : "absent");
    }
}
