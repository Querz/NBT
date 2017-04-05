package net.querz.nbt.custom;

import java.io.IOException;
import java.util.Arrays;

import net.querz.nbt.*;

public class ShortArrayTag extends ArrayTag implements CustomTag {
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
		super(TagType.CUSTOM, name);
		setValue(value);
	}
	
	public void setValue(short[] value) {
		this.value = value;
	}

	@Override
	public int getId() {
		return TAG_ID;
	}
	
	@Override
	public short[] getValue() {
		return value;
	}

	@Override
	protected void serialize(NBTOutputStream nbtOut, int depth) throws IOException {
		nbtOut.getDataOutputStream().writeInt(value.length);
		for (short s : value)
			nbtOut.getDataOutputStream().writeShort(s);
	}
	
	@Override
	protected ShortArrayTag deserialize(NBTInputStream nbtIn, int depth) throws IOException {
		int length = nbtIn.getDataInputStream().readInt();
		value = new short[length];
		for (int i = 0; i < length; i++)
			value[i] = nbtIn.getDataInputStream().readShort();
		return this;
	}
	
	@Override
	public String toString() {
		return "<short[]:" + getName() + ":[" + NBTUtil.joinArray(",", value) + "]>";
	}
	
	@Override
	public ShortArrayTag clone() {
		return new ShortArrayTag(getName(), value.clone());
	}
}
