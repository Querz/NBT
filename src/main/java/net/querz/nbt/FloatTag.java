package net.querz.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class FloatTag extends NumberTag {

	private final float value;

	public static final FloatTag ZERO = new FloatTag(0.0f);

	private FloatTag(float f) {
		value = f;
	}

	public static FloatTag valueOf(float f) {
		return f == 0.0f ? ZERO : new FloatTag(f);
	}

	@Override
	public byte asByte() {
		return (byte) (floor(value) & 0xFF);
	}

	@Override
	public short asShort() {
		return (short) (floor(value) & 0xFFFF);
	}

	@Override
	public int asInt() {
		return floor(value);
	}

	@Override
	public long asLong() {
		return (long) value;
	}

	@Override
	public float asFloat() {
		return value;
	}

	@Override
	public double asDouble() {
		return value;
	}

	@Override
	public Number asNumber() {
		return value;
	}

	private int floor(float f) {
		int o = (int) f;
		return f < (float) o ? o - 1 : o;
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeFloat(value);
	}

	@Override
	public FloatTag copy() {
		return this;
	}

	@Override
	public void accept(TagVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		} else {
			return other instanceof FloatTag && value == ((FloatTag) other).value;
		}
	}

	@Override
	public int hashCode() {
		return Float.floatToIntBits(value);
	}

	@Override
	public String toString() {
		return value + "f";
	}

	public static final TagReader<FloatTag> READER = new TagReader<FloatTag>() {

		@Override
		public FloatTag read(DataInput in, int depth) throws IOException {
			return valueOf(in.readFloat());
		}

		@Override
		public TagTypeVisitor.ValueResult read(DataInput in, TagTypeVisitor visitor) throws IOException {
			return visitor.visit(in.readFloat());
		}

		@Override
		public void skip(DataInput in) throws IOException {
			in.skipBytes(4);
		}
	};
}
