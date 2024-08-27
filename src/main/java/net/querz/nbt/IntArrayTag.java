package net.querz.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public class IntArrayTag extends CollectionTag<IntTag> {

	private int[] value;

	public IntArrayTag(int[] i) {
		Objects.requireNonNull(i);
		value = i;
	}

	@Override
	public IntTag get(int index) {
		return IntTag.valueOf(value[index]);
	}

	@Override
	public IntTag set(int index, IntTag tag) {
		int old = value[index];
		value[index] = tag.asInt();
		return IntTag.valueOf(old);
	}

	@Override
	public void add(int index, IntTag tag) {
		if (index > value.length|| index < 0) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + value.length);
		}
		int[] output = new int[value.length + 1];
		System.arraycopy(value, 0, output, 0, index);
		output[index] = tag.asInt();
		if (index < value.length) {
			System.arraycopy(value, index, output, index + 1, value.length - index);
		}
		value = output;
	}

	@Override
	public IntTag remove(int index) {
		if (index < 0 || index >= value.length) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + value.length);
		}
		int old = value[index];
		int[] output = new int[value.length - 1];
		System.arraycopy(value, 0, output, 0, index);
		if (index < value.length - 1) {
			System.arraycopy(value, index + 1, output, index, value.length - index - 1);
		}
		value = output;
		return IntTag.valueOf(old);
	}

	@Override
	public Type getElementType() {
		return Type.INT;
	}

	@Override
	public int size() {
		return value.length;
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(value.length);
		for (int i : value) {
			out.writeInt(i);
		}
	}

	@Override
	public IntArrayTag copy() {
		int[] copy = new int[value.length];
		System.arraycopy(value, 0, copy, 0, value.length);
		return new IntArrayTag(copy);
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
			return other instanceof IntArrayTag && Arrays.equals(value, ((IntArrayTag) other).value);
		}
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(value);
	}

	public void clear() {
		value = new int[0];
	}

	public int[] getValue() {
		return value;
	}

	public void setValue(int[] value) {
		this.value = value;
	}

	public static final TagReader<IntArrayTag> READER = new TagReader<IntArrayTag>() {

		@Override
		public IntArrayTag read(DataInput in, int depth) throws IOException {
			int[] data = new int[in.readInt()];
			for (int i = 0; i < data.length; i++) {
				data[i] = in.readInt();
			}
			return new IntArrayTag(data);
		}

		@Override
		public TagTypeVisitor.ValueResult read(DataInput in, TagTypeVisitor visitor) throws IOException {
			int[] data = new int[in.readInt()];
			for (int i = 0; i < data.length; i++) {
				data[i] = in.readInt();
			}
			return visitor.visit(data);
		}

		@Override
		public void skip(DataInput in) throws IOException {
			in.skipBytes(in.readInt() * 4);
		}
	};
}
