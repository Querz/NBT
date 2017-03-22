package net.querz.nbt;

import java.io.IOException;

import net.querz.nbt.util.NBTUtil;

public class LongTag extends NumberTag<Long> {
	private long value;
	
	protected LongTag() {
		this(0);
	}
	
	public LongTag(long value) {
		this("", value);
	}
	
	public LongTag(String name, long value) {
		super(TagType.LONG, name);
		setValue(value);
	}
	
	public void setValue(long value) {
		this.value = value;
	}
	
	@Override
	public Long getValue() {
		return value;
	}

	@Override
	protected void serialize(NBTOutputStream nbtOut, int depth) throws IOException {
		nbtOut.dos.writeLong(value);
	}

	@Override
	protected LongTag deserialize(NBTInputStream nbtIn, int depth) throws IOException {
		value = nbtIn.dis.readLong();
		return this;
	}

	@Override
	public String toTagString() {
		return NBTUtil.createNamePrefix(this) + valueToTagString(0);
	}
	
	@Override
	public String valueToTagString(int depth) {
		return value + "l";
	}
	
	@Override
	public String toString() {
		return "<long:" + getName() + ":" + value + ">";
	}
	
	@Override
	public LongTag clone() {
		return new LongTag(getName(), value);
	}
}
