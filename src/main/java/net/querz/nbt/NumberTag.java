package net.querz.nbt;

public abstract class NumberTag extends Tag {
	protected NumberTag(TagType type, String name) {
		super(type, name);
	}

	private RuntimeException badState() {
		return new IllegalStateException(
				"Cannot cast value of " + getClass().getName() + " to a number with a lower resolution.");
	}

	public byte asByte() {
		if (getValue() instanceof Byte)
			return (byte) getValue();
		throw badState();
	}

	public short asShort() {
		if (getValue() instanceof Byte
				|| getValue() instanceof Short)
			return ((Number) getValue()).shortValue();
		throw badState();
	}

	public int asInt() {
		if (getValue() instanceof Byte
				|| getValue() instanceof Short
				|| getValue() instanceof Integer)
			return ((Number) getValue()).intValue();
		throw badState();
	}

	public long asLong() {
		if (getValue() instanceof Byte
				|| getValue() instanceof Short
				|| getValue() instanceof Integer
				|| getValue() instanceof Long)
			return ((Number) getValue()).longValue();
		throw badState();
	}

	public float asFloat() {
		if (getValue() instanceof Byte
				|| getValue() instanceof Short
				|| getValue() instanceof Integer
				|| getValue() instanceof Float)
			return ((Number) getValue()).floatValue();
		throw badState();
	}

	public double asDouble() {
		if (getValue() instanceof Byte
				|| getValue() instanceof Short
				|| getValue() instanceof Integer
				|| getValue() instanceof Long
				|| getValue() instanceof Float
				|| getValue() instanceof Double)
			return ((Number) getValue()).doubleValue();
		throw badState();
	}
}
