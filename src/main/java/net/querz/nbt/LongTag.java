package net.querz.nbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class LongTag extends NumberTag<Long> implements Comparable<LongTag> {

	public static final long ZERO_VALUE = 0L;

	public LongTag() {
		super(ZERO_VALUE);
	}

	public LongTag(long value) {
		super(value);
	}

	public void setValue(long value) {
		super.setValue(value);
	}

	@Override
	public void serializeValue(DataOutputStream dos, int maxDepth) throws IOException {
		dos.writeLong(getValue());
	}

	@Override
	public void deserializeValue(DataInputStream dis, int maxDepth) throws IOException {
		setValue(dis.readLong());
	}

	@Override
	public String valueToTagString(int maxDepth) {
		return getValue() + "l";
	}

	@Override
	public boolean equals(Object other) {
		return super.equals(other) && asLong() == ((LongTag) other).asLong();
	}

	@Override
	public int compareTo(LongTag other) {
		return getValue().compareTo(other.getValue());
	}

	@Override
	public LongTag clone() {
		return new LongTag(getValue());
	}
}
