package net.querz;

@FunctionalInterface
public interface ExceptionSupplier<T, E extends Throwable> {

	T run() throws E;
}