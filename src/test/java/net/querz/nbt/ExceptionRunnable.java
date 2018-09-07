package net.querz.nbt;

@FunctionalInterface
public interface ExceptionRunnable<T, E extends Exception> {

	T run() throws E;
}
