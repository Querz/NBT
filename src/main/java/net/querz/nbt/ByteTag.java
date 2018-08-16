package net.querz.nbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ByteTag extends NumberTag<Byte> {

	public ByteTag() {}

	public ByteTag(byte value) {
		super(value);
	}

	public ByteTag(boolean value) {
		super((byte) (value ? 1 : 0));
	}

	@Override
	public byte getID() {
		return 1;
	}

	@Override
	public void serializeValue(DataOutputStream dos, int depth) throws IOException {
		dos.writeByte(getValue());
	}

	@Override
	public void deserializeValue(DataInputStream dis, int depth) throws IOException {
		setValue(dis.readByte());
	}

	@Override
	public String valueToTagString(int depth) {
		return getValue() + "b";
	}

	@Override
	protected Byte getEmptyValue() {
		return 0;
	}

	@Override
	public boolean equals(Object other) {
		return super.equals(other)
				&& getValue()
				.equals(((ByteTag) other)
						.getValue());
	}

	//use primitive type to disallow null value
	public void setValue(byte value) {
		super.setValue(value);
	}

	public boolean asBoolean() {
		return getValue() > 0;
	}

	@Override
	public ByteTag clone() {
		return new ByteTag(getValue());
	}

	@Override
	public boolean valueEquals(Byte value) {
		return asByte() == value;
	}
}
