package net.querz.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class ByteTag extends NumberTag {

	private final byte value;

	public static final ByteTag TRUE = valueOf((byte) 1);
	public static final ByteTag FALSE = valueOf((byte) 0);

	private static final class Cache {
		private static final ByteTag[] cache = new ByteTag[256];

		static {
			for (int i = 0; i < cache.length; i++) {
				cache[i] = new ByteTag((byte) (i - 128));
			}
		}
	}

	private ByteTag(byte b) {
		this.value = b;
	}

	public static ByteTag valueOf(byte b) {
		return Cache.cache[b + 128];
	}

	public static ByteTag valueOf(boolean b) {
		return b ? TRUE : FALSE;
	}

	@Override
	public byte asByte() {
		return value;
	}

	@Override
	public short asShort() {
		return value;
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
		out.writeByte(value);
	}

	@Override
	public ByteTag copy() {
		return this; // ByteTag is immutable
	}

	@Override
	public void accept(TagVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public boolean equals(Object other) {
		return this == other; // we only used cached values
	}

	@Override
	public int hashCode() {
		return value;
	}

	@Override
	public String toString() {
		return value + "b";
	}

	public static final TagReader<ByteTag> READER = new TagReader<ByteTag>() {

		@Override
		public ByteTag read(DataInput in, int depth) throws IOException {
			return valueOf(in.readByte());
		}

		@Override
		public TagTypeVisitor.ValueResult read(DataInput in, TagTypeVisitor visitor) throws IOException {
			return visitor.visit(in.readByte());
		}

		@Override
		public void skip(DataInput in) throws IOException {
			in.skipBytes(1);
		}
	};
}
