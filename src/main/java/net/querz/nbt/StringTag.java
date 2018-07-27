package net.querz.nbt;

import java.io.IOException;

public class StringTag extends Tag {
	private String value;
	
	protected StringTag() {
		this("");
	}
	
	public StringTag(String value) {
		this("", value);
	}
	
	public StringTag(String name, String value) {
		super(TagType.STRING, name);
		setValue(value);
	}
	
	public void setValue(String value) {
		this.value = value == null ? "" : value;
	}
	
	public int length() {
		return value.length();
	}
	
	@Override
	public String getValue() {
		return value;
	}

	@Override
	protected void serialize(NBTOutputStream nbtOut, int depth) throws IOException {
		byte[] bytes = value.getBytes(CHARSET);
		nbtOut.dos.writeShort(bytes.length);
		nbtOut.dos.write(bytes);
	}
	
	@Override
	protected StringTag deserialize(NBTInputStream nbtIn, int depth) throws IOException {
		int length = nbtIn.dis.readShort();
		byte[] bytes = new byte[length];
		nbtIn.dis.readFully(bytes);
		value = new String(bytes, CHARSET);
		return this;
	}

	@Override
	public String toTagString() {
		return NBTUtil.createNamePrefix(this) + valueToTagString(0);
	}
	
	@Override
	protected String valueToTagString(int depth) {
		return NBTUtil.createPossiblyEscapedString(value);
	}
	
	@Override
	public String toString() {
		return "<string:" + getName() + ":" + value + ">";
	}
	
	@Override
	public StringTag clone() {
		return new StringTag(getName(), value);
	}
}
