package net.querz.nbt;

import java.io.IOException;
import java.util.Arrays;

public class IntArrayTag extends Tag {
	private int[] value;
	
	protected IntArrayTag() {
		this(new int[0]);
	}
	
	public IntArrayTag(int[] value) {
		this("", value);
	}
	
	public IntArrayTag(String name, int[] value) {
		super(TagType.INT_ARRAY, name);
		setValue(value);
	}
	
	public void setValue(int[] value) {
		this.value = value;
	}
	
	public int length() {
		return value.length;
	}
	
	@Override
	public int[] getValue() {
		return value;
	}

	@Override
	protected void serialize(NBTOutputStream nbtOut, int depth) throws IOException {
		nbtOut.dos.writeInt(value.length);
		for (int i : value)
			nbtOut.dos.writeInt(i);
	}
	
	@Override
	protected IntArrayTag deserialize(NBTInputStream nbtIn, int depth) throws IOException {
		int length = nbtIn.dis.readInt();
		value = new int[length];
		for (int i = 0; i < length; i++)
			value[i] = nbtIn.dis.readInt();
		return this;
	}

	@Override
	public String toTagString() {
		return NBTUtil.createNamePrefix(this) + valueToTagString(0);
	}
	
	@Override
	protected String valueToTagString(int depth) {
		return "[" + NBTUtil.joinArray(",", value) + "]";
	}
	
	@Override
	public String toString() {
		return "<int[]:" + getName() + ":[" + NBTUtil.joinArray(",", value) + "]>";
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof IntArrayTag)) {
			return false;
		}
		IntArrayTag tag = (IntArrayTag) other;
		return getName() != null && getName().equals(tag.getName()) && Arrays.equals(value, tag.getValue());
	}
	
	@Override
	public boolean valueEquals(Tag other) {
		return other instanceof IntArrayTag && Arrays.equals(value, ((IntArrayTag) other).getValue());
	}
	
	@Override
	public IntArrayTag clone() {
		return new IntArrayTag(getName(), value.clone());
	}
}
