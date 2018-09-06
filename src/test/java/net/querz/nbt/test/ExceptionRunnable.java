package net.querz.nbt.test;

@FunctionalInterface
public interface ExceptionRunnable<T, E extends Exception> {

	T run() throws E;
}
