package net.querz.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public class ByteArrayTag extends CollectionTag<ByteTag> {

	private byte[] value;

	public ByteArrayTag(byte[] b) {
		Objects.requireNonNull(b);
		value = b;
	}

	@Override
	public ByteTag get(int index) {
		return ByteTag.valueOf(value[index]);
	}

	@Override
	public ByteTag set(int index, ByteTag tag) {
		byte old = value[index];
		value[index] = tag.asByte();
		return ByteTag.valueOf(old);
	}

	@Override
	public void add(int index, ByteTag tag) {
		if (index > value.length|| index < 0) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + value.length);
		}
		byte[] output = new byte[value.length + 1];
		System.arraycopy(value, 0, output, 0, index);
		output[index] = tag.asByte();
		if (index < value.length) {
			System.arraycopy(value, index, output, index + 1, value.length - index);
		}
		value = output;
	}

	@Override
	public ByteTag remove(int index) {
		if (index < 0 || index >= value.length) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + value.length);
		}
		byte old = value[index];
		byte[] output = new byte[value.length - 1];
		System.arraycopy(value, 0, output, 0, index);
		if (index < value.length - 1) {
			System.arraycopy(value, index + 1, output, index, value.length - index - 1);
		}
		value = output;
		return ByteTag.valueOf(old);
	}

	@Override
	public Type getElementType() {
		return Type.BYTE;
	}

	@Override
	public int size() {
		return value.length;
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(value.length);
		out.write(value);
	}

	@Override
	public ByteArrayTag copy() {
		byte[] copy = new byte[value.length];
		System.arraycopy(value, 0, copy, 0, value.length);
		return new ByteArrayTag(copy);
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
			return other instanceof ByteArrayTag && Arrays.equals(value, ((ByteArrayTag) other).value);
		}
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(value);
	}

	public void clear() {
		value = new byte[0];
	}

	public byte[] getValue() {
		return value;
	}

	public void setValue(byte[] value) {
		this.value = value;
	}

	public static final TagReader<ByteArrayTag> READER = new TagReader<ByteArrayTag>() {

		@Override
		public ByteArrayTag read(DataInput in, int depth) throws IOException {
			byte[] data = new byte[in.readInt()];
			in.readFully(data);
			return new ByteArrayTag(data);
		}

		@Override
		public TagTypeVisitor.ValueResult read(DataInput in, TagTypeVisitor visitor) throws IOException {
			byte[] data = new byte[in.readInt()];
			in.readFully(data);
			return visitor.visit(data);
		}

		@Override
		public void skip(DataInput in) throws IOException {
			in.skipBytes(in.readInt());
		}
	};
}
