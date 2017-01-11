package net.querz.nbt;

import java.io.IOException;

public class LongTag extends Tag implements NumberTag {
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
	protected void serialize(NBTOutputStream nbtOut) throws IOException {
		nbtOut.dos.writeLong(value);
	}

	@Override
	protected LongTag deserialize(NBTInputStream nbtIn) throws IOException {
		value = nbtIn.dis.readLong();
		return this;
	}

	@Override
	public String toTagString() {
		return NBTUtil.createNamePrefix(this) + value + "l";
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
