package net.querz;

@FunctionalInterface
public interface ExceptionRunnable<E extends Exception> {

	void run() throws E;
}
