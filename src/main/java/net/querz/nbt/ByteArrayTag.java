package net.querz.nbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class ByteArrayTag extends ArrayTag<byte[]> implements Comparable<ByteArrayTag> {

	public static final byte[] ZERO_VALUE = new byte[0];

	public ByteArrayTag() {
		super(ZERO_VALUE);
	}

	public ByteArrayTag(byte[] value) {
		super(value);
	}

	@Override
	public void serializeValue(DataOutputStream dos, int maxDepth) throws IOException {
		dos.writeInt(length());
		dos.write(getValue());
	}

	@Override
	public void deserializeValue(DataInputStream dis, int maxDepth) throws IOException {
		int length = dis.readInt();
		setValue(new byte[length]);
		dis.readFully(getValue());
	}

	@Override
	public String valueToTagString(int maxDepth) {
		return arrayToString("B", "b");
	}

	@Override
	public boolean equals(Object other) {
		return super.equals(other) && Arrays.equals(getValue(), ((ByteArrayTag) other).getValue());
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(getValue());
	}

	@Override
	public int compareTo(ByteArrayTag other) {
		return Integer.compare(length(), other.length());
	}

	@Override
	public ByteArrayTag clone() {
		return new ByteArrayTag(Arrays.copyOf(getValue(), length()));
	}
}
