package net.querz.nbt;

import java.io.IOException;

public class LongArrayTag extends ArrayTag {
	private long[] value;

	protected LongArrayTag() {
		this(new long[0]);
	}

	public LongArrayTag(long[] value) {
		this("", value);
	}

	public LongArrayTag(String name, long[] value) {
		super(TagType.LONG_ARRAY, name, "L", "l");
		setValue(value);
	}
	
	public void setValue(long[] value) {
		this.value = value;
	}
	
	@Override
	public long[] getValue() {
		return value;
	}

	@Override
	protected void serialize(NBTOutputStream nbtOut, int depth) throws IOException {
		nbtOut.dos.writeInt(value.length);
		for (long l : value)
			nbtOut.dos.writeLong(l);
	}
	
	@Override
	protected LongArrayTag deserialize(NBTInputStream nbtIn, int depth) throws IOException {
		int length = nbtIn.dis.readInt();
		value = new long[length];
		for (int i = 0; i < length; i++)
			value[i] = nbtIn.dis.readLong();
		return this;
	}
	
	@Override
	public String toString() {
		return "<long[]:" + getName() + ":[" + NBTUtil.joinArray(",", value) + "]>";
	}

	@Override
	public LongArrayTag clone() {
		return new LongArrayTag(getName(), value.clone());
	}
}
