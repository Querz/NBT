package net.querz.nbt.tag;

@FunctionalInterface
public interface ExceptionRunnable<E extends Exception> {

	void run() throws E;
}
