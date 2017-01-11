package net.querz.nbt;

import java.io.IOException;

public class ShortTag extends Tag implements NumberTag {
	private short value;
	
	protected ShortTag() {
		this((short) 0);
	}
	
	public ShortTag(short value) {
		this("", value);
	}
	
	public ShortTag(String name, short value) {
		super(TagType.SHORT, name);
		setValue(value);
	}
	
	public void setValue(short value) {
		this.value = value;
	}
	
	@Override
	public Short getValue() {
		return value;
	}

	@Override
	protected void serialize(NBTOutputStream nbtOut) throws IOException {
		nbtOut.dos.writeShort(value);
	}

	@Override
	protected ShortTag deserialize(NBTInputStream nbtIn) throws IOException {
		value = nbtIn.dis.readShort();
		return this;
	}

	@Override
	public String toTagString() {
		return NBTUtil.createNamePrefix(this) + value;
	}
	
	@Override
	public String toString() {
		return "<short:" + getName() + ":" + value + ">";
	}
	
	@Override
	public ShortTag clone() {
		return new ShortTag(getName(), value);
	}
}
