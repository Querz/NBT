package net.querz.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class ShortTag extends NumberTag {

	private final short value;

	private static final class Cache {
		private static final ShortTag[] cache = new ShortTag[1153];

		static {
			for (int i = 0; i < cache.length; i++) {
				cache[i] = new ShortTag((short) (i - 128));
			}
		}
	}

	private ShortTag(short s) {
		value = s;
	}

	public static ShortTag valueOf(short s) {
		return s >= -128 && s <= 1024 ? Cache.cache[s +128] : new ShortTag(s);
	}

	@Override
	public byte asByte() {
		return (byte) (value & 0xFF);
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
		out.writeShort(value);
	}

	@Override
	public ShortTag copy() {
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
			return other instanceof ShortTag && value == ((ShortTag) other).value;
		}
	}

	@Override
	public int hashCode() {
		return value;
	}

	@Override
	public String toString() {
		return value + "s";
	}

	public static final TagReader<ShortTag> READER = new TagReader<ShortTag>() {

		@Override
		public ShortTag read(DataInput in, int depth) throws IOException {
			return valueOf(in.readShort());
		}

		@Override
		public TagTypeVisitor.ValueResult read(DataInput in, TagTypeVisitor visitor) throws IOException {
			return visitor.visit(in.readShort());
		}

		@Override
		public void skip(DataInput in) throws IOException {
			in.skipBytes(2);
		}
	};
}
