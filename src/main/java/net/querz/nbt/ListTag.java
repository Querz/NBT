package net.querz.nbt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListTag extends Tag {
	private TagType type;
	private List<Tag> value;
	
	protected ListTag() {
		this(TagType.END);
	}
	
	public ListTag(TagType type) {
		this("", type, null);
	}
	
	public ListTag(TagType type, List<Tag> value) {
		this("", type, value);
	}
	
	public ListTag(String name, TagType type) {
		this(name, type, null);
	}
	
	public ListTag(String name, TagType type, List<Tag> value) {
		super(TagType.LIST, name);
		setType(type);
		setValue(value);
	}
	
	public void setValue(List<Tag> value) {
		if (value == null)
			clear();
		else
			this.value = value;
	}
	
	public TagType getListType() {
		return type;
	}
	
	public int size() {
		return value.size();
	}
	
	public void add(Tag tag) {
		checkAddType(tag.getType());
		Tag clone = tag.clone();
		clone.setName("");
		value.add(clone);
	}
	
	private void addTagInstance(Tag tag) {
		checkAddType(tag.getType());
		value.add(tag);
	}
	
	public void addBoolean(boolean b) {
		addTagInstance(new ByteTag(b));
	}
	
	public void addByte(byte b) {
		addTagInstance(new ByteTag(b));
	}
	
	public void addShort(short s) {
		addTagInstance(new ShortTag(s));
	}
	
	public void addInt(int i) {
		addTagInstance(new IntTag(i));
	}
	
	public void addLong(long l) {
		addTagInstance(new LongTag(l));
	}
	
	public void addFloat(float f) {
		addTagInstance(new FloatTag(f));
	}
	
	public void addDouble(double d) {
		addTagInstance(new DoubleTag(d));
	}
	
	public void addString(String s) {
		addTagInstance(new StringTag(s));
	}
	
	public void addBytes(byte[] b) {
		addTagInstance(new ByteArrayTag(b));
	}
	
	public void addInts(int[] i) {
		addTagInstance(new IntArrayTag(i));
	}
	
	public void clear() {
		value = new ArrayList<Tag>();
	}
	
	public void clear(int init) {
		value = new ArrayList<Tag>(init);
	}
	
	protected void setType(TagType type) {
		this.type = type;
	}
	
	public Tag get(int index) {
		return value.get(index);
	}
	
	public boolean getBoolean(int index) {
		if (this.type == TagType.BYTE)
			return NBTUtil.toBoolean(get(index));
		return false;
	}
	
	public byte getByte(int index) {
		if (this.type == TagType.BYTE)
			return ((ByteTag) get(index)).getValue();
		return 0;
	}
	
	public short getShort(int index) {
		if (this.type == TagType.SHORT)
			return ((ShortTag) get(index)).getValue();
		return 0;
	}
	
	public int getInt(int index) {
		if (this.type == TagType.INT)
			return ((IntTag) get(index)).getValue();
		return 0;
	}
	
	public long getLong(int index) {
		if (this.type == TagType.LONG)
			return ((LongTag) get(index)).getValue();
		return 0;
	}
	
	public float getFloat(int index) {
		if (this.type == TagType.FLOAT)
			return ((FloatTag) get(index)).getValue();
		return 0;
	}
	
	public double getDouble(int index) {
		if (this.type == TagType.DOUBLE)
			return ((DoubleTag) get(index)).getValue();
		return 0;
	}
	
	public String getString(int index) {
		if (this.type == TagType.STRING)
			return ((StringTag) get(index)).getValue();
		return "";
	}
	
	public byte asByte(int index) {
		return NBTUtil.toNumber(get(index)).byteValue();
	}
	
	public short asShort(int index) {
		return NBTUtil.toNumber(get(index)).shortValue();
	}
	
	public int asInt(int index) {
		return NBTUtil.toNumber(get(index)).intValue();
	}
	
	public long asLong(int index) {
		return NBTUtil.toNumber(get(index)).longValue();
	}
	
	public float asFloat(int index) {
		return NBTUtil.toNumber(get(index)).floatValue();
	}
	
	public double asDouble(int index) {
		return NBTUtil.toNumber(get(index)).doubleValue();
	}
	
	public CompoundTag getCompoundTag(int index) {
		Tag tag = get(index);
		if (tag.getType() == TagType.COMPOUND)
			return (CompoundTag) tag;
		return new CompoundTag("", new HashMap<String, Tag>());
	}
	
	public ListTag getListTag(int index) {
		Tag tag = get(index);
		if (tag.getType() == TagType.LIST)
			return (ListTag) tag;
		return new ListTag(TagType.STRING);
	}
	
	private void checkAddType(TagType type) {
		if (this.type != type)
			throw new IllegalArgumentException("A list does not support multiple tag types: " + this.type + " != " + type);
	}
	
	@Override
	public List<Tag> getValue() {
		return value;
	}

	@Override
	protected void serialize(NBTOutputStream nbtOut) throws IOException {
		int size = value.size();
		nbtOut.dos.writeByte(type.getId());
		nbtOut.dos.writeInt(size);
		for (Tag t : value)
			t.serialize(nbtOut);
	}
	
	@Override
	protected ListTag deserialize(NBTInputStream nbtIn) throws IOException {
		int typeId = nbtIn.dis.readByte();
		setType(TagType.match(typeId));
		int size = nbtIn.dis.readInt();
		clear(size);
		for (int i = 0; i < size; i++) {
			Tag tag = type.getTag().deserialize(nbtIn);
			if (tag instanceof EndTag)
				throw new IOException("EndTag not permitted in a list.");
			add(tag);
		}
		return this;
	}

	@Override
	public String toTagString() {
		return NBTUtil.createNamePrefix(this) + "[" + NBTUtil.joinTagString(",", value.toArray()) + "]";
	}
	
	@Override
	public String toString() {
		return "<list:" + getName() + ":[" + NBTUtil.joinObjects(",", value.toArray()) + "]>";
	}
	
	@Override
	public ListTag clone() {
		List<Tag> clone = new ArrayList<Tag>(value.size());
		for (Tag t : value) {
			clone.add(t.clone());
		}
		return new ListTag(getName(), type, clone);
	}
}
