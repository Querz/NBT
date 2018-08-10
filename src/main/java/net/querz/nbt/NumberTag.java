package net.querz.nbt;

public abstract class NumberTag<T extends Number & Comparable<T>> extends Tag<T> {

	public NumberTag() {}

	public NumberTag(String name) {
		super(name);
	}

	public NumberTag(String name, T value) {
		super(name, value);
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
	public int compareTo(Tag<T> other) {
		if (!(other instanceof NumberTag) || this.getClass() != other.getClass()) {
			return 0;
		}
		int nameComparison = super.compareTo(other);
		return nameComparison == 0 ? getValue().compareTo(other.getValue()) : nameComparison;
	}
}
