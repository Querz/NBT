package net.querz.nbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class ByteArrayTag extends ArrayTag<byte[]> {

	public ByteArrayTag() {}

	public ByteArrayTag(byte[] value) {
		super(value);
	}

	@Override
	public byte getID() {
		return 7;
	}

	@Override
	protected byte[] getEmptyValue() {
		return new byte[0];
	}

	@Override
	public void serializeValue(DataOutputStream dos, int depth) throws IOException {
		dos.writeInt(length());
		dos.write(getValue());
	}

	@Override
	public void deserializeValue(DataInputStream dis, int depth) throws IOException {
		int length = dis.readInt();
		setValue(new byte[length]);
		dis.readFully(getValue());
	}

	@Override
	public String valueToTagString(int depth) {
		return arrayToString(getValue(), "B", "b");
	}

	@Override
	public boolean equals(Object other) {
		return super.equals(other)
				&& (getValue() == ((ByteArrayTag) other).getValue()
				|| getValue().length == (((ByteArrayTag) other).length())
				&& Arrays.equals(getValue(), ((ByteArrayTag) other).getValue()));
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(getValue());
	}

	@Override
	public ByteArrayTag clone() {
		return new ByteArrayTag(Arrays.copyOf(getValue(), length()));
	}
}
