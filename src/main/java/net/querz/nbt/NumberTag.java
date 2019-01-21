package net.querz.nbt;

public abstract class NumberTag<T extends Number & Comparable<T>> extends Tag<T> {

	public NumberTag(T value) {
		super(value);
	}

	public byte asByte() {
		return getValue().byteValue();
	}

	public short asShort() {
		return getValue().shortValue();
	}

	public int asInt() {
		return getValue().intValue();
	}

	public long asLong() {
		return getValue().longValue();
	}

	public float asFloat() {
		return getValue().floatValue();
	}

	public double asDouble() {
		return getValue().doubleValue();
	}

	@Override
	public String valueToString(int depth) {
		return getValue().toString();
	}

	@Override
	public int compareTo(Tag<T> other) {
		if (this.getClass() != other.getClass()) {
			throw new IllegalArgumentException("number types are incompatible");
		}
		return getValue().compareTo(other.getValue());
	}
}
