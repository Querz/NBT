package net.querz.nbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class LongArrayTag extends ArrayTag<long[]> implements Comparable<LongArrayTag> {

	public static final long[] ZERO_VALUE = new long[0];

	public LongArrayTag() {
		super(ZERO_VALUE);
	}

	public LongArrayTag(long[] value) {
		super(value);
	}

	@Override
	public void serializeValue(DataOutputStream dos, int maxDepth) throws IOException {
		dos.writeInt(length());
		for (long i : getValue()) {
			dos.writeLong(i);
		}
	}

	@Override
	public void deserializeValue(DataInputStream dis, int maxDepth) throws IOException {
		int length = dis.readInt();
		setValue(new long[length]);
		for (int i = 0; i < length; i++) {
			getValue()[i] = dis.readLong();
		}
	}

	@Override
	public String valueToTagString(int maxDepth) {
		return arrayToString("L", "l");
	}

	@Override
	public boolean equals(Object other) {
		return super.equals(other) && Arrays.equals(getValue(), ((LongArrayTag) other).getValue());
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(getValue());
	}

	@Override
	public int compareTo(LongArrayTag other) {
		return Integer.compare(length(), other.length());
	}

	@Override
	public LongArrayTag clone() {
		return new LongArrayTag(Arrays.copyOf(getValue(), length()));
	}
}
