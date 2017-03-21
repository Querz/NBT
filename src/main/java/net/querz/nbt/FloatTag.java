package net.querz.nbt;

import java.io.IOException;

import net.querz.nbt.util.NBTUtil;

public class FloatTag extends NumberTag<Float> {
	private float value;
	
	protected FloatTag() {
		this(0F);
	}
	
	public FloatTag(float value) {
		this("", value);
	}
	
	public FloatTag(String name, float value) {
		super(TagType.FLOAT, name);
		setValue(value);
	}
	
	public void setValue(float value) {
		this.value = value;
	}
	
	@Override
	public Float getValue() {
		return value;
	}

	@Override
	protected void serialize(NBTOutputStream nbtOut) throws IOException {
		nbtOut.dos.writeFloat(value);
	}

	@Override
	protected FloatTag deserialize(NBTInputStream nbtIn) throws IOException {
		value = nbtIn.dis.readFloat();
		return this;
	}

	@Override
	public String toTagString() {
		return NBTUtil.createNamePrefix(this) + valueToTagString();
	}
	
	@Override
	public String valueToTagString() {
		return value + "f";
	}
	
	@Override
	public String toString() {
		return "<float:" + getName() + ":" + value + ">";
	}
	
	@Override
	public FloatTag clone() {
		return new FloatTag(getName(), value);
	}
}
