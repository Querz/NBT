package net.querz.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public class LongArrayTag extends CollectionTag<LongTag> {

	private long[] value;

	public LongArrayTag(long[] l) {
		Objects.requireNonNull(l);
		value = l;
	}

	@Override
	public LongTag get(int index) {
		return LongTag.valueOf(value[index]);
	}

	@Override
	public LongTag set(int index, LongTag tag) {
		long old = value[index];
		value[index] = tag.asLong();
		return LongTag.valueOf(old);
	}

	@Override
	public void add(int index, LongTag tag) {
		if (index > value.length || index < 0) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + value.length);
		}
		long[] output = new long[value.length + 1];
		System.arraycopy(value, 0, output, 0, index);
		output[index] = tag.asLong();
		if (index < value.length) {
			System.arraycopy(value, index, output, index + 1, value.length - index);
		}
		value = output;
	}

	@Override
	public LongTag remove(int index) {
		if (index < 0 || index >= value.length) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + value.length);
		}
		long old = value[index];
		long[] output = new long[value.length - 1];
		System.arraycopy(value, 0, output, 0, index);
		if (index < value.length - 1) {
			System.arraycopy(value, index + 1, output, index, value.length - index - 1);
		}
		value = output;
		return LongTag.valueOf(old);
	}

	@Override
	public Type getElementType() {
		return Type.LONG;
	}

	@Override
	public int size() {
		return value.length;
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(value.length);
		for (long l : value) {
			out.writeLong(l);
		}
	}

	@Override
	public LongArrayTag copy() {
		long[] copy = new long[value.length];
		System.arraycopy(value, 0, copy, 0, value.length);
		return new LongArrayTag(copy);
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
			return other instanceof LongArrayTag && Arrays.equals(value, ((LongArrayTag) other).value);
		}
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(value);
	}

	public void clear() {
		value = new long[0];
	}

	public long[] getValue() {
		return value;
	}

	public void setValue(long[] value) {
		this.value = value;
	}

	public static final TagReader<LongArrayTag> READER = new TagReader<LongArrayTag>() {

		@Override
		public LongArrayTag read(DataInput in, int depth) throws IOException {
			long[] data = new long[in.readInt()];
			for (int i = 0; i < data.length; i++) {
				data[i] = in.readLong();
			}
			return new LongArrayTag(data);
		}

		@Override
		public TagTypeVisitor.ValueResult read(DataInput in, TagTypeVisitor visitor) throws IOException {
			long[] data = new long[in.readInt()];
			for (int i = 0; i < data.length; i++) {
				data[i] = in.readLong();
			}
			return visitor.visit(data);
		}

		@Override
		public void skip(DataInput in) throws IOException {
			in.skipBytes(in.readInt() * 8);
		}
	};
}
