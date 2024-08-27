package net.querz.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class LongTag extends NumberTag {

	private final long value;

	private static final class Cache {
		private static final LongTag[] cache = new LongTag[1153];

		static {
			for (int i = 0; i < cache.length; i++) {
				cache[i] = new LongTag(i - 128);
			}
		}
	}

	private LongTag(long l) {
		value = l;
	}

	public static LongTag valueOf(long l) {
		return l >= -128L && l <= 1024L ? Cache.cache[(int) l + 128] : new LongTag(l);
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
		return (int) value;
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
		out.writeLong(value);
	}

	@Override
	public LongTag copy() {
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
			return other instanceof LongTag && value == ((LongTag) other).value;
		}
	}

	@Override
	public int hashCode() {
		return (int) (value ^ value >>> 32);
	}

	@Override
	public String toString() {
		return value + "L";
	}

	public static final TagReader<LongTag> READER = new TagReader<LongTag>() {

		@Override
		public LongTag read(DataInput in, int depth) throws IOException {
			return valueOf(in.readLong());
		}

		@Override
		public TagTypeVisitor.ValueResult read(DataInput in, TagTypeVisitor visitor) throws IOException {
			return visitor.visit(in.readLong());
		}

		@Override
		public void skip(DataInput in) throws IOException {
			in.skipBytes(8);
		}
	};
}
