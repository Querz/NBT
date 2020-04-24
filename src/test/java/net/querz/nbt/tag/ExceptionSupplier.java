package net.querz.nbt.tag;

@FunctionalInterface
public interface ExceptionSupplier<T, E extends Exception> {

	T run() throws E;
}
