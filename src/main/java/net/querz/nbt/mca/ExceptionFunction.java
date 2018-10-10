package net.querz.nbt.mca;

@FunctionalInterface
public interface ExceptionFunction<T, R, E extends Exception> {

	R accept(T t) throws E;
}
