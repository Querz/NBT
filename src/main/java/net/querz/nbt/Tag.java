package net.querz.nbt;

import java.io.IOException;
import java.nio.charset.Charset;

public abstract class Tag implements Comparable<Tag>, Cloneable {
	public static final Charset CHARSET = Charset.forName("UTF-8");
	public static final int MAX_DEPTH = 512;
	
	private TagType type;
	private String name;
	
	protected Tag(TagType type, String name) {
		this.type = type;
		setName(name);
	}
	
	protected Tag(TagType type) {
		this(type, "");
	}
	
	protected Tag() {
		this(TagType.END, null);
	}
	
	public TagType getType() {
		return type;
	}
	
	public String getName() {
		return name;
	}
	
	public final void setName(String name) {
		this.name = name == null ? "" : name;
	}
	
	public final void serializeTag(NBTOutputStream nbtOut) throws IOException {
		serializeTag(nbtOut, 0);
	}
	
	public final void serializeTag(NBTOutputStream nbtOut, int depth) throws IOException {
		nbtOut.dos.writeByte(type.getId(this));
		if (type != TagType.END) {
			byte[] nameBytes = name.getBytes(CHARSET);
			nbtOut.dos.writeShort(nameBytes.length);
			nbtOut.dos.write(nameBytes);
		}
		serialize(nbtOut, depth);
	}
	
	public static Tag deserializeTag(NBTInputStream nbtIn) throws IOException {
		return deserializeTag(nbtIn, 0);
	}
	
	public static Tag deserializeTag(NBTInputStream nbtIn, int depth) throws IOException {
		int typeId = nbtIn.dis.readByte() & 0xFF;
		Tag tag = TagType.getTag(typeId);
		if (tag.getType() != TagType.END) {
			int nameLength = nbtIn.dis.readShort() & 0xFFFF;
			byte[] nameBytes = new byte[nameLength];
			nbtIn.dis.readFully(nameBytes);
			tag.setName(new String(nameBytes, CHARSET));
		}
		tag.deserialize(nbtIn, depth);
		return tag;
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Tag)) {
			return false;
		}
		Tag tag = (Tag) other;
		return getValue().equals(tag.getValue()) && getName().equals(tag.getName());
	}
	
	protected boolean valueEquals(Tag other) {
		return other.getValue().equals(getValue());
	}
	
	@Override
	public int compareTo(Tag other) {
		if (equals(other))
			return 0;
		else {
			if (other.getName().equals(getName()))
				throw new IllegalStateException("Cannot compare two Tags with the same name but different values.");
			else
				return getName().compareTo(other.getName());
		}
	}
	
	protected String toString(int depth) {
		return toString();
	}
	
	protected static int incrementDepth(int depth) {
		if (depth >= MAX_DEPTH) {
			throw new MaxDepthReachedException();
		}
		return ++depth;
	}
	
	protected String toTagString(int depth) {
		return toTagString();
	}
	
	protected abstract String valueToTagString(int depth);
	public abstract Object getValue();
	public abstract String toTagString();
	public abstract Tag clone();
	protected abstract void serialize(NBTOutputStream nbtOut, int depth) throws IOException;
	protected abstract Tag deserialize(NBTInputStream nbtIn, int depth) throws IOException;
}
