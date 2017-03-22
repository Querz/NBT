package net.querz.nbt;

import java.io.IOException;

import net.querz.nbt.util.NBTUtil;

public class IntTag extends NumberTag<Integer> {
	private int value;
	
	protected IntTag() {
		this(0);
	}
	
	public IntTag(int value) {
		this("", value);
	}
	
	public IntTag(String name, int value) {
		super(TagType.INT, name);
		setValue(value);
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	@Override
	public Integer getValue() {
		return value;
	}

	@Override
	protected void serialize(NBTOutputStream nbtOut, int depth) throws IOException {
		nbtOut.dos.writeInt(value);
	}

	@Override
	protected IntTag deserialize(NBTInputStream nbtIn, int depth) throws IOException {
		value = nbtIn.dis.readInt();
		return this;
	}

	@Override
	public String toTagString() {
		return NBTUtil.createNamePrefix(this) + valueToTagString(0);
	}
	
	@Override
	public String valueToTagString(int depth) {
		return value + "";
	}
	
	@Override
	public String toString() {
		return "<int:" + getName() + ":" + value + ">";
	}
	
	@Override
	public IntTag clone() {
		return new IntTag(getName(), value);
	}
}
