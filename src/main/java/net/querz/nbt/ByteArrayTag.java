package net.querz.nbt;

import java.io.IOException;
import java.util.Arrays;

public class ByteArrayTag extends ArrayTag {
	private byte[] value;
	
	protected ByteArrayTag() {
		this(new byte[0]);
	}
	
	public ByteArrayTag(byte[] value) {
		this("", value);
	}
	
	public ByteArrayTag(String name, byte[] value) {
		super(TagType.BYTE_ARRAY, name);
		setValue(value);
	}
	
	public void setValue(byte[] value) {
		this.value = value;
	}
	
	@Override
	public byte[] getValue() {
		return value;
	}

	@Override
	protected void serialize(NBTOutputStream nbtOut, int depth) throws IOException {
		nbtOut.dos.writeInt(value.length);
		nbtOut.dos.write(value);
	}
	
	@Override
	protected ByteArrayTag deserialize(NBTInputStream nbtIn, int depth) throws IOException {
		int length = nbtIn.dis.readInt();
		value = new byte[length];
		nbtIn.dis.readFully(value);
		return this;
	}
	
	@Override
	public String toString() {
		return "<byte[]:" + getName() + ":[" + NBTUtil.joinArray(",", value) + "]>";
	}
	
	@Override
	public ByteArrayTag clone() {
		return new ByteArrayTag(getName(), value.clone());
	}
}
