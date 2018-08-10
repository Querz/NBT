package net.querz.nbt;

import java.lang.reflect.Array;

public abstract class ArrayTag<T> extends Tag<T> {

	public ArrayTag() {}

	public ArrayTag(String name) {
		super(name);
	}

	public ArrayTag(String name, T value) {
		super(name, value);
	}

	protected String arrayToString(T array, String prefix, String suffix) {
		if (!array.getClass().isArray()) {
			throw new UnsupportedOperationException("cannot convert non-array Object to String");
		}

		StringBuilder sb = new StringBuilder("[").append(prefix).append(";");
		for (int i = 0; i < length(); i++) {
			sb.append(i == 0 ? "" : ",").append(Array.get(array, i)).append(suffix);
		}
		sb.append("]");
		return sb.toString();
	}

	public int length() {
		return Array.getLength(getValue());
	}

	@Override
	public void setValue(T value) {
		super.setValue(checkNull(value));
	}

	@Override
	public T getValue() {
		return super.getValue();
	}
}
