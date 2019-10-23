package net.querz.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class DoubleTag extends NumberTag<Double> implements Comparable<DoubleTag> {

	public static final double ZERO_VALUE = 0.0D;

	public DoubleTag() {
		super(ZERO_VALUE);
	}

	public DoubleTag(double value) {
		super(value);
	}

	public void setValue(double value) {
		super.setValue(value);
	}

	@Override
	public void serializeValue(DataOutput dos, int maxDepth) throws IOException {
		dos.writeDouble(getValue());
	}

	@Override
	public void deserializeValue(DataInput dis, int maxDepth) throws IOException {
		setValue(dis.readDouble());
	}

	@Override
	public String valueToTagString(int maxDepth) {
		return getValue() + "d";
	}

	@Override
	public boolean equals(Object other) {
		return super.equals(other) && getValue().equals(((DoubleTag) other).getValue());
	}

	@Override
	public int compareTo(DoubleTag other) {
		return getValue().compareTo(other.getValue());
	}

	@Override
	public DoubleTag clone() {
		return new DoubleTag(getValue());
	}
}
