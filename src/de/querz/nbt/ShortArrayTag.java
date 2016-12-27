package de.querz.nbt;

import java.io.IOException;

public class ShortArrayTag extends CustomTag {
	public static final int TAG_ID = 100;
	
	public static void register() {
		TagType.registerCustomTag(TAG_ID, ShortArrayTag.class);
	}
	
	private short[] value;
	
	protected ShortArrayTag() {
		this(new short[0]);
	}
	
	public ShortArrayTag(short[] value) {
		this("", value);
	}
	
	public ShortArrayTag(String name, short[] value) {
		super(TAG_ID, name);
		setValue(value);
	}
	
	public void setValue(short[] value) {
		this.value = value;
	}
	
	public int length() {
		return value.length;
	}
	
	@Override
	public short[] getValue() {
		return value;
	}

	@Override
	protected void serialize(NBTOutputStream nbtOut) throws IOException {
		nbtOut.dos.writeInt(value.length);
		for (short s : value)
			nbtOut.dos.writeShort(s);
	}
	
	@Override
	protected ShortArrayTag deserialize(NBTInputStream nbtIn) throws IOException {
		int length = nbtIn.dis.readInt();
		value = new short[length];
		for (int i = 0; i < length; i++)
			value[i] = nbtIn.dis.readShort();
		return this;
	}

	@Override
	public String toTagString() {
		return NBTUtil.checkColon(this) + "[" + NBTUtil.joinShorts(",", value) + "]";
	}
	
	@Override
	public String toString() {
		return "<short[]:" + getName() + ":[" + NBTUtil.joinShorts(",", value) + "]>";
	}
	
	@Override
	public ShortArrayTag clone() {
		return new ShortArrayTag(getName(), value);
	}
}
