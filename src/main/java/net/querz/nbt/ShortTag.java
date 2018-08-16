package net.querz.nbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ShortTag extends NumberTag<Short> {

	public ShortTag() {}

	public ShortTag(short value) {
		super(value);
	}

	@Override
	public byte getID() {
		return 2;
	}

	@Override
	public void serializeValue(DataOutputStream dos, int depth) throws IOException {
		dos.writeShort(getValue());
	}

	@Override
	public void deserializeValue(DataInputStream dis, int depth) throws IOException {
		setValue(dis.readShort());
	}

	@Override
	public String valueToTagString(int depth) {
		return getValue() + "s";
	}

	@Override
	protected Short getEmptyValue() {
		return 0;
	}

	@Override
	public boolean equals(Object other) {
		return super.equals(other) && getValue().equals(((ShortTag) other).getValue());
	}

	//use primitive type to disallow null value
	public void setValue(short value) {
		super.setValue(value);
	}

	@Override
	public ShortTag clone() {
		return new ShortTag(getValue());
	}

	@Override
	public boolean valueEquals(Short value) {
		return asShort() == value;
	}
}
