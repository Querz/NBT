package net.querz;

@FunctionalInterface
public interface ExceptionRunnable<E extends Throwable> {

	void run() throws E;
}