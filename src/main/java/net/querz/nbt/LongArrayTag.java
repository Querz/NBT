package net.querz.nbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class LongArrayTag extends ArrayTag<long[]> {

	public LongArrayTag() {}

	public LongArrayTag(long[] value) {
		super(value);
	}

	@Override
	public byte getID() {
		return 12;
	}

	@Override
	protected long[] getEmptyValue() {
		return new long[0];
	}

	@Override
	public void serializeValue(DataOutputStream dos, int depth) throws IOException {
		dos.writeInt(length());
		for (long i : getValue()) {
			dos.writeLong(i);
		}
	}

	@Override
	public void deserializeValue(DataInputStream dis, int depth) throws IOException {
		int length = dis.readInt();
		setValue(new long[length]);
		for (int i = 0; i < length; i++) {
			getValue()[i] = dis.readLong();
		}
	}

	@Override
	public String valueToTagString(int depth) {
		return arrayToString(getValue(), "L", "l");
	}

	@Override
	public boolean valueEquals(long[] value) {
		return getValue() == value || getValue().length == value.length && Arrays.equals(getValue(), value);
	}

	@Override
	public LongArrayTag clone() {
		return new LongArrayTag(Arrays.copyOf(getValue(), length()));
	}
}
