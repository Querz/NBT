package net.querz.nbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class FloatTag extends NumberTag<Float> {

	public static final float ZERO_VALUE = 0.0F;

	public FloatTag() {
		super(ZERO_VALUE);
	}

	public FloatTag(float value) {
		super(value);
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
		return super.equals(other) && getValue().equals(((FloatTag) other).getValue());
	}

	@Override
	public FloatTag clone() {
		return new FloatTag(getValue());
	}
}
