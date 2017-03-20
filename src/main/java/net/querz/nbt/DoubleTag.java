package net.querz.nbt;

import java.io.IOException;

import net.querz.nbt.util.NBTUtil;

public class DoubleTag extends NumberTag<Double> {
	private double value;
	
	protected DoubleTag() {
		this(0D);
	}
	
	public DoubleTag(double value) {
		this("", value);
	}
	
	public DoubleTag(String name, double value) {
		super(TagType.DOUBLE, name);
		setValue(value);
	}
	
	public void setValue(double value) {
		this.value = value;
	}
	
	@Override
	public Double getValue() {
		return value;
	}

	@Override
	protected void serialize(NBTOutputStream nbtOut) throws IOException {
		nbtOut.dos.writeDouble(value);
	}
	
	@Override
	protected DoubleTag deserialize(NBTInputStream nbtIn) throws IOException {
		value = nbtIn.dis.readDouble();
		return this;
	}

	@Override
	public String toTagString() {
		return NBTUtil.createNamePrefix(this) + value + "d";
	}
	
	@Override
	public String toString() {
		return "<double:" + getName() + ":" + value + ">";
	}
	
	@Override
	public DoubleTag clone() {
		return new DoubleTag(getName(), value);
	}
}
