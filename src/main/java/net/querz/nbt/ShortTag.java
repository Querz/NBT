package net.querz.nbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ShortTag extends NumberTag<Short> {

	public static final short ZERO_VALUE = 0;

	public ShortTag() {
		super(ZERO_VALUE);
	}

	public ShortTag(short value) {
		super(value);
	}

	public void setValue(short value) {
		super.setValue(value);
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
	public boolean equals(Object other) {
		return super.equals(other) && asShort() == ((ShortTag) other).asShort();
	}

	@Override
	public ShortTag clone() {
		return new ShortTag(getValue());
	}
}
