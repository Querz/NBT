package net.querz.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class IntTag extends NumberTag {

	private final int value;

	private static final class Cache {
		private static final IntTag[] cache = new IntTag[1153];

		static {
			for (int i = 0; i < cache.length; i++) {
				cache[i] = new IntTag(i - 128);
			}
		}
	}

	private IntTag(int i) {
		value = i;
	}

	public static IntTag valueOf(int i) {
		return i >= -128 && i <= 1024 ? Cache.cache[i + 128] : new IntTag(i);
	}

	@Override
	public byte asByte() {
		return (byte) (value & 0xFF);
	}

	@Override
	public short asShort() {
		return (short) (value & 0xFFFF);
	}

	@Override
	public int asInt() {
		return value;
	}

	@Override
	public long asLong() {
		return value;
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

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(value);
	}

	@Override
	public IntTag copy() {
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
			return other instanceof IntTag && value == ((IntTag) other).value;
		}
	}

	@Override
	public int hashCode() {
		return value;
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

	public static final TagReader<IntTag> READER = new TagReader<IntTag>() {

		@Override
		public IntTag read(DataInput in, int depth) throws IOException {
			return valueOf(in.readInt());
		}

		@Override
		public TagTypeVisitor.ValueResult read(DataInput in, TagTypeVisitor visitor) throws IOException {
			return visitor.visit(in.readInt());
		}

		@Override
		public void skip(DataInput in) throws IOException {
			in.skipBytes(4);
		}
	};
}
