package net.querz.nbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class FloatTag extends NumberTag<Float> {

	public FloatTag() {}

	public FloatTag(float value) {
		super(value);
	}

	@Override
	public byte getID() {
		return 5;
	}

	@Override
	public void serializeValue(DataOutputStream dos, int depth) throws IOException {
		dos.writeFloat(getValue());
	}

	@Override
	public void deserializeValue(DataInputStream dis, int depth) throws IOException {
		setValue(dis.readFloat());
	}

	@Override
	public String valueToTagString(int depth) {
		return getValue() + "f";
	}

	@Override
	protected Float getEmptyValue() {
		return 0.0f;
	}

	@Override
	public boolean equals(Object other) {
		return super.equals(other) && getValue().equals(((FloatTag) other).getValue());
	}

	//use primitive type to disallow null value
	public void setValue(float value) {
		super.setValue(value);
	}

	@Override
	public FloatTag clone() {
		return new FloatTag(getValue());
	}

	@Override
	public boolean valueEquals(Float value) {
		return asFloat() == value;
	}
}
