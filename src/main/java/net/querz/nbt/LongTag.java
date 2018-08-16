package net.querz.nbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class LongTag extends NumberTag<Long> {

	public LongTag() {}

	public LongTag(long value) {
		super(value);
	}

	@Override
	public byte getID() {
		return 4;
	}

	@Override
	public void serializeValue(DataOutputStream dos, int depth) throws IOException {
		dos.writeLong(getValue());
	}

	@Override
	public void deserializeValue(DataInputStream dis, int depth) throws IOException {
		setValue(dis.readLong());
	}

	@Override
	public String valueToTagString(int depth) {
		return getValue() + "l";
	}

	@Override
	protected Long getEmptyValue() {
		return 0L;
	}

	@Override
	public boolean equals(Object other) {
		return super.equals(other) && getValue().equals(((LongTag) other).getValue());
	}

	//use primitive type to disallow null value
	public void setValue(long value) {
		super.setValue(value);
	}

	@Override
	public LongTag clone() {
		return new LongTag(getValue());
	}

	@Override
	public boolean valueEquals(Long value) {
		return asLong() == value;
	}
}
