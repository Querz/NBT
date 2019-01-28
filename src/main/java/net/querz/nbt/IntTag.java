package net.querz.nbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class IntTag extends NumberTag<Integer> implements Comparable<IntTag> {

	public static final int ZERO_VALUE = 0;

	public IntTag() {
		super(ZERO_VALUE);
	}

	public IntTag(int value) {
		super(value);
	}

	public void setValue(int value) {
		super.setValue(value);
	}

	@Override
	public void serializeValue(DataOutputStream dos, int maxDepth) throws IOException {
		dos.writeInt(getValue());
	}

	@Override
	public void deserializeValue(DataInputStream dis, int maxDepth) throws IOException {
		setValue(dis.readInt());
	}

	@Override
	public String valueToTagString(int maxDepth) {
		return getValue() + "";
	}

	@Override
	public boolean equals(Object other) {
		return super.equals(other) && asInt() == ((IntTag) other).asInt();
	}

	@Override
	public int compareTo(IntTag other) {
		return getValue().compareTo(other.getValue());
	}

	@Override
	public IntTag clone() {
		return new IntTag(getValue());
	}
}
