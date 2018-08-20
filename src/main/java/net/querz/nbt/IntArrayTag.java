package net.querz.nbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class IntArrayTag extends ArrayTag<int[]> {

	public IntArrayTag() {}

	public IntArrayTag(int[] value) {
		super(value);
	}

	@Override
	public byte getID() {
		return 11;
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
		return arrayToString(getValue(), "I", "");
	}

	@Override
	public boolean valueEquals(int[] value) {
		return getValue() == value || getValue().length == value.length && Arrays.equals(getValue(), value);
	}

	@Override
	public IntArrayTag clone() {
		return new IntArrayTag(Arrays.copyOf(getValue(), length()));
	}
}
