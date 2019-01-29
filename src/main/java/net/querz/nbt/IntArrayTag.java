package net.querz.nbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class IntArrayTag extends ArrayTag<int[]> implements Comparable<IntArrayTag> {

	public static final int[] ZERO_VALUE = new int[0];

	public IntArrayTag() {
		super(ZERO_VALUE);
	}

	public IntArrayTag(int[] value) {
		super(value);
	}

	@Override
	public void serializeValue(DataOutputStream dos, int maxDepth) throws IOException {
		dos.writeInt(length());
		for (int i : getValue()) {
			dos.writeInt(i);
		}
	}

	@Override
	public void deserializeValue(DataInputStream dis, int maxDepth) throws IOException {
		int length = dis.readInt();
		setValue(new int[length]);
		for (int i = 0; i < length; i++) {
			getValue()[i] = dis.readInt();
		}
	}

	@Override
	public String valueToTagString(int maxDepth) {
		return arrayToString("I", "");
	}

	@Override
	public boolean equals(Object other) {
		return super.equals(other) && Arrays.equals(getValue(), ((IntArrayTag) other).getValue());
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(getValue());
	}

	@Override
	public int compareTo(IntArrayTag other) {
		return Integer.compare(length(), other.length());
	}

	@Override
	public IntArrayTag clone() {
		return new IntArrayTag(Arrays.copyOf(getValue(), length()));
	}
}
