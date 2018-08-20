package net.querz.nbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class IntTag extends NumberTag<Integer> {

	public IntTag() {}

	public IntTag(int value) {
		super(value);
	}

	@Override
	public byte getID() {
		return 3;
	}

	@Override
	protected Integer getEmptyValue() {
		return 0;
	}

	public void setValue(int value) {
		super.setValue(value);
	}

	@Override
	public void serializeValue(DataOutputStream dos, int depth) throws IOException {
		dos.writeInt(getValue());
	}

	@Override
	public void deserializeValue(DataInputStream dis, int depth) throws IOException {
		setValue(dis.readInt());
	}

	@Override
	public String valueToTagString(int depth) {
		return getValue() + "";
	}

	@Override
	public boolean equals(Object other) {
		return super.equals(other) && asInt() == ((IntTag) other).asInt();
	}

	@Override
	public IntTag clone() {
		return new IntTag(getValue());
	}
}
