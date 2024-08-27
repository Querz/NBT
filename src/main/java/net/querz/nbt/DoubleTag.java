package net.querz.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class DoubleTag extends NumberTag {

	private final double value;

	public static final DoubleTag ZERO = new DoubleTag(0.0d);

	private DoubleTag(double d) {
		value = d;
	}

	public static DoubleTag valueOf(double d) {
		return d == 0.0d ? ZERO : new DoubleTag(d);
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
		return (long) Math.floor(value);
	}

	@Override
	public float asFloat() {
		return (float) value;
	}

	@Override
	public double asDouble() {
		return value;
	}

	@Override
	public Number asNumber() {
		return value;
	}

	private int floor(double d) {
		int o = (int) d;
		return d < (double) o ? o - 1 : o;
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeDouble(value);
	}

	@Override
	public DoubleTag copy() {
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
			return other instanceof DoubleTag && value == ((DoubleTag) other).value;
		}
	}

	@Override
	public int hashCode() {
		long l = Double.doubleToLongBits(value);
		return (int) (l ^ l >>> 32);
	}

	@Override
	public String toString() {
		return value + "d";
	}

	public static final TagReader<DoubleTag> READER = new TagReader<DoubleTag>() {

		@Override
		public DoubleTag read(DataInput in, int depth) throws IOException {
			return valueOf(in.readDouble());
		}

		@Override
		public TagTypeVisitor.ValueResult read(DataInput in, TagTypeVisitor visitor) throws IOException {
			return visitor.visit(in.readDouble());
		}

		@Override
		public void skip(DataInput in) throws IOException {
			in.skipBytes(8);
		}
	};
}
