package de.querz.nbt;

import java.io.IOException;

public abstract class Tag implements Comparable<Tag> {
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
	
	public void setName(String name) {
		this.name = name;
	}
	
	protected void serializeTag(NBTOutputStream nbtOut) throws IOException {
		if (type.isCustom())
			nbtOut.dos.writeByte(((CustomTag) this).getId());
		else
			nbtOut.dos.writeByte(type.getId());
		if (name != null) {
			byte[] nameBytes = name.getBytes(NBTConstants.CHARSET);
			nbtOut.dos.writeShort(nameBytes.length);
			nbtOut.dos.write(nameBytes);
		}
		serialize(nbtOut);
	}
	
	protected static Tag deserializeTag(NBTInputStream nbtIn) throws IOException {
		int typeId = nbtIn.dis.readByte() & 0xFF;
		TagType type = TagType.match(typeId);
		Tag tag;
		if (type.isCustom())
			tag = TagType.getCustomTag(typeId);
		else
			tag = type.getTag();
		if (type != TagType.END) {
			int nameLength = nbtIn.dis.readShort() & 0xFFFF;
			byte[] nameBytes = new byte[nameLength];
			nbtIn.dis.readFully(nameBytes);
			tag.setName(new String(nameBytes, NBTConstants.CHARSET));
		}
		tag.deserialize(nbtIn);
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
	
	public abstract Object getValue();
	public abstract String toTagString();
	protected abstract void serialize(NBTOutputStream nbtOut) throws IOException;
	protected abstract Tag deserialize(NBTInputStream nbtIn) throws IOException;
}
