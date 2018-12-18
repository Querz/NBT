package net.querz.nbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class IntArrayTag extends ArrayTag<int[]> {

	public IntArrayTag() {
		super(new int[0]);
	}

	public IntArrayTag(int[] value) {
		super(value);
	}

	@Override
	protected int[] getEmptyValue() {
		return new int[0];
	}

	@Override
	public void serializeValue(DataOutputStream dos, int depth) throws IOException {
		dos.writeInt(length());
		for (int i : getValue()) {
			dos.writeInt(i);
		}
	}

	@Override
	public void deserializeValue(DataInputStream dis, int depth) throws IOException {
		int length = dis.readInt();
		setValue(new int[length]);
		for (int i = 0; i < length; i++) {
			getValue()[i] = dis.readInt();
		}
	}

	@Override
	public String valueToTagString(int depth) {
		return arrayToString("I", "");
	}

	@Override
	public boolean equals(Object other) {
		return super.equals(other)
				&& (getValue() == ((IntArrayTag) other).getValue()
				|| getValue().length == (((IntArrayTag) other).length())
				&& Arrays.equals(getValue(), ((IntArrayTag) other).getValue()));
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(getValue());
	}

	@Override
	public IntArrayTag clone() {
		return new IntArrayTag(Arrays.copyOf(getValue(), length()));
	}
}
