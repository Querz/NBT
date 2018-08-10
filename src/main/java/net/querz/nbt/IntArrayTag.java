package net.querz.nbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class IntArrayTag extends ArrayTag<int[]> {

	public IntArrayTag() {}

	public IntArrayTag(String name) {
		super(name);
	}

	public IntArrayTag(String name, int[] value) {
		super(name, value);
	}

	@Override
	public byte getID() {
		return 11;
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
	public String valueToString(int depth) {
		return arrayToString(getValue(), "I", "");
	}

	@Override
	protected int[] getEmptyValue() {
		return new int[0];
	}

	@Override
	public IntArrayTag clone() {
		return new IntArrayTag(getName(), Arrays.copyOf(getValue(), length()));
	}

	@Override
	public boolean valueEquals(int[] value) {
		return getValue() == value || getValue().length == value.length && Arrays.equals(getValue(), value);
	}
}
