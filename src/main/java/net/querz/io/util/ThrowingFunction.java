package net.querz.io.util;

@FunctionalInterface
public interface ThrowingFunction<T, R, E extends Throwable> {

    R apply(T t) throws E;

    static <T, E extends Throwable> ThrowingFunction<T, T, E> identity() {
        return t -> t;
    }

}
