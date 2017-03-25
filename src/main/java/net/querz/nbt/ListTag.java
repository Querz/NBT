package net.querz.nbt;

import java.io.IOException;
import java.util.ArrayList;
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
		value.add(tag);
	}
	
	public void addBoolean(boolean b) {
		add(new ByteTag(b));
	}
	
	public void addByte(byte b) {
		add(new ByteTag(b));
	}
	
	public void addShort(short s) {
		add(new ShortTag(s));
	}
	
	public void addInt(int i) {
		add(new IntTag(i));
	}
	
	public void addLong(long l) {
		add(new LongTag(l));
	}
	
	public void addFloat(float f) {
		add(new FloatTag(f));
	}
	
	public void addDouble(double d) {
		add(new DoubleTag(d));
	}
	
	public void addString(String s) {
		add(new StringTag(s));
	}
	
	public void addBytes(byte[] b) {
		add(new ByteArrayTag(b));
	}
	
	public void addInts(int[] i) {
		add(new IntArrayTag(i));
	}
	
	public void clear() {
		value = new ArrayList<>();
	}
	
	public void clear(int init) {
		value = new ArrayList<>(init);
	}
	
	protected void setType(TagType type) {
		this.type = type;
	}
	
	public Tag get(int index) {
		return value.get(index);
	}
	
	public boolean getBoolean(int index) {
		return this.type == TagType.BYTE && NBTUtil.toBoolean(get(index));
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
		return new CompoundTag();
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
	protected void serialize(NBTOutputStream nbtOut, int depth) throws IOException {
		int size = value.size();
		nbtOut.dos.writeByte(type.getId());
		nbtOut.dos.writeInt(size);
		for (Tag t : value)
			t.serialize(nbtOut, incrementDepth(depth));
	}
	
	@Override
	protected ListTag deserialize(NBTInputStream nbtIn, int depth) throws IOException {
		int typeId = nbtIn.dis.readByte();
		setType(TagType.match(typeId));
		int size = nbtIn.dis.readInt();
		clear(size);
		for (int i = 0; i < size; i++) {
			Tag typeTag = type.getTag();
			if (typeTag != null) {
				Tag tag = typeTag.deserialize(nbtIn, incrementDepth(depth));
				if (tag instanceof EndTag)
					throw new IOException("EndTag not permitted in a list.");
				add(tag);
			}
		}
		return this;
	}

	@Override
	public String toTagString() {
		return toTagString(0);
	}
	
	@Override
	protected String toTagString(int depth) {
		return NBTUtil.createNamePrefix(this) + valueToTagString(depth);
	}
	
	@Override
	protected String valueToTagString(int depth) {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (Tag tag : value) {
			sb.append(first ? "" : ",").append(tag.valueToTagString(incrementDepth(depth)));
			first = false;
		}
		return "[" + sb.toString() + "]";
	}
	
	@Override
	public String toString() {
		return toString(0);
	}
	
	@Override
	protected String toString(int depth) {
		depth = incrementDepth(depth);
		return "<list:" + getName() + ":[" + NBTUtil.joinArray(",", value.toArray(), depth) + "]>";
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof ListTag)) {
			return false;
		}
		ListTag list = (ListTag) other;
		return getName().equals(list.getName()) && valueEquals(list);
	}
	
	@Override
	public boolean valueEquals(Tag other) {
		if (!(other instanceof ListTag) || ((ListTag) other).size() != size())
			return false;
		for (int i = 0; i < size(); i++) {
			if (!((ListTag) other).get(i).valueEquals(get(i))) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public ListTag clone() {
		List<Tag> clone = new ArrayList<>(value.size());
		for (Tag t : value) {
			clone.add(t.clone());
		}
		return new ListTag(getName(), type, clone);
	}
}
