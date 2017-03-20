package net.querz.nbt.custom;

import java.io.IOException;
import java.util.Arrays;

import net.querz.nbt.CustomTag;
import net.querz.nbt.NBTInputStream;
import net.querz.nbt.NBTOutputStream;
import net.querz.nbt.TagType;
import net.querz.nbt.util.NBTUtil;

public class ShortArrayTag extends CustomTag {
	public static final int TAG_ID = 100;
	
	public static void register() {
		TagType.registerCustomTag(TAG_ID, ShortArrayTag.class);
	}
	
	private short[] value;
	
	public ShortArrayTag() {
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
		nbtOut.getDataOutputStream().writeInt(value.length);
		for (short s : value)
			nbtOut.getDataOutputStream().writeShort(s);
	}
	
	@Override
	protected ShortArrayTag deserialize(NBTInputStream nbtIn) throws IOException {
		int length = nbtIn.getDataInputStream().readInt();
		value = new short[length];
		for (int i = 0; i < length; i++)
			value[i] = nbtIn.getDataInputStream().readShort();
		return this;
	}

	@Override
	public String toTagString() {
		return NBTUtil.createNamePrefix(this) + "[" + NBTUtil.joinArray(",", value) + "]";
	}
	
	@Override
	public String toString() {
		return "<short[]:" + getName() + ":[" + NBTUtil.joinArray(",", value) + "]>";
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof ShortArrayTag)) {
			return false;
		}
		ShortArrayTag tag = (ShortArrayTag) other;
		return getName() != null && getName().equals(tag.getName()) && Arrays.equals(value, tag.getValue());
	}
	
	@Override
	public ShortArrayTag clone() {
		return new ShortArrayTag(getName(), value.clone());
	}
}
