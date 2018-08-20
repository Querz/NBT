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
	protected Float getEmptyValue() {
		return 0.0f;
	}

	public void setValue(float value) {
		super.setValue(value);
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
	public boolean equals(Object other) {
		return super.equals(other) && asFloat() == ((FloatTag) other).asFloat();
	}

	@Override
	public FloatTag clone() {
		return new FloatTag(getValue());
	}
}
