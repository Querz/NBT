package net.querz.nbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ByteTag extends NumberTag<Byte> implements Comparable<ByteTag> {

	public static final byte ZERO_VALUE = 0;

	public ByteTag() {
		super(ZERO_VALUE);
	}

	public ByteTag(byte value) {
		super(value);
	}

	public ByteTag(boolean value) {
		super((byte) (value ? 1 : 0));
	}

	public boolean asBoolean() {
		return getValue() > 0;
	}

	public void setValue(byte value) {
		super.setValue(value);
	}

	@Override
	public void serializeValue(DataOutputStream dos, int maxDepth) throws IOException {
		dos.writeByte(getValue());
	}

	@Override
	public void deserializeValue(DataInputStream dis, int maxDepth) throws IOException {
		setValue(dis.readByte());
	}

	@Override
	public String valueToTagString(int maxDepth) {
		return getValue() + "b";
	}

	@Override
	public boolean equals(Object other) {
		return super.equals(other) && asByte() == ((ByteTag) other).asByte();
	}

	@Override
	public int compareTo(ByteTag other) {
		return getValue().compareTo(other.getValue());
	}

	@Override
	public ByteTag clone() {
		return new ByteTag(getValue());
	}
}
