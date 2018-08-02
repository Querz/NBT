package net.querz.nbt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListTag extends Tag {
	private TagType type;
	private int typeId;
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
		checkAddType(tag);
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

	public void addLongs(long[] l) {
		add(new LongArrayTag(l));
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

	public byte[] getBytes(int index) {
		if (this.type == TagType.BYTE_ARRAY)
			return ((ByteArrayTag) get(index)).getValue();
		return new byte[0];
	}

	public int[] getInts(int index) {
		if (this.type == TagType.INT_ARRAY)
			return ((IntArrayTag) get(index)).getValue();
		return new int[0];
	}

	public long[] getLongs(int index) {
		if (this.type == TagType.LONG_ARRAY)
			return ((LongArrayTag) get(index)).getValue();
		return new long[0];
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
	
	private void checkAddType(Tag tag) {
		if (this.type != tag.getType()
				|| (tag.getType() == TagType.CUSTOM && value.size() != 0 && typeId != tag.getType().getId(tag))) {

			throw new IllegalArgumentException(
					String.format(
							"A list does not support multiple tag types: %s (%d) != %s (%d)",
							type,
							typeId,
							tag.getType(),
							tag.getType().getId(tag)
					)
			);
		} else if (tag.getType() == TagType.CUSTOM && value.size() == 0) {
			typeId = tag.getType().getId(tag);
		}
	}
	
	@Override
	public List<Tag> getValue() {
		return value;
	}

	@Override
	protected void serialize(NBTOutputStream nbtOut, int depth) throws Exception {
		int size = value.size();
		if (type == TagType.CUSTOM) {
			nbtOut.dos.writeByte(typeId);
		} else {
			nbtOut.dos.writeByte(type.getId());
		}
		nbtOut.dos.writeInt(size);
		for (Tag t : value)
			t.serialize(nbtOut, incrementDepth(depth));
	}
	
	@Override
	protected ListTag deserialize(NBTInputStream nbtIn, int depth) throws Exception {
		int typeId = nbtIn.dis.readByte();
		setType(TagType.match(typeId));
		int size = nbtIn.dis.readInt();
		clear(size);
		for (int i = 0; i < size; i++) {
			Tag typeTag = TagType.getTag(typeId);
			if (typeTag != null) {
				Tag tag = typeTag.deserialize(nbtIn, incrementDepth(depth));
				if (tag instanceof EndTag) {
					throw new IOException("EndTag not permitted in a list.");
				}
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
		return "<list:" + getName() + ":[" + NBTUtil.joinArray(",", value.toArray(), "", depth,  false) + "]>";
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
