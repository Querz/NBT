package net.querz.nbt;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class CompoundTag extends Tag {
	private Map<String, Tag> value;
	
	protected CompoundTag() {
		this("", null);
	}
	
	public CompoundTag(Map<String, Tag> value) {
		this("", value);
	}
	
	public CompoundTag(String name) {
		this(name, null);
	}
	
	public CompoundTag(String name, Map<String, Tag> value) {
		super(TagType.COMPOUND, name);
		setValue(value);
	}
	
	public Tag remove(String key) {
		return value.remove(key);
	}
	
	public boolean containsKey(String key) {
		return value.containsKey(key);
	}
	
	public void set(Tag tag) {
		value.put(tag.getName(), tag);
	}
	
	public void setBoolean(String key, boolean b) {
		set(new ByteTag(key, b));
	}
	
	public void setByte(String key, byte b) {
		set(new ByteTag(key, b));
	}
	
	public void setShort(String key, short s) {
		set(new ShortTag(key, s));
	}
	
	public void setInt(String key, int i) {
		set(new IntTag(key, i));
	}
	
	public void setLong(String key, long l) {
		set(new LongTag(key, l));
	}
	
	public void setFloat(String key, float f) {
		set(new FloatTag(key, f));
	}
	
	public void setDouble(String key, double d) {
		set(new DoubleTag(key, d));
	}
	
	public void setString(String key, String s) {
		set(new StringTag(key, s));
	}
	
	public void setBytes(String key, byte[] b) {
		set(new ByteArrayTag(key, b));
	}
	
	public void setInts(String key, int[] i) {
		set(new IntArrayTag(key, i));
	}

	public void setLongs(String key, long[] l) {
		set(new LongArrayTag(key, l));
	}
	
	public Tag get(String key) {
		return value.get(key);
	}
	
	private boolean isType(Tag tag, TagType type) {
		return tag != null && tag.getType() == type;
	}
	
	public boolean getBoolean(String key) {
		Tag tag = get(key);
		return isType(tag, TagType.BYTE) && ((ByteTag) tag).getBoolean();
	}
	
	public byte getByte(String key) {
		Tag tag = get(key);
		if (isType(tag, TagType.BYTE))
			return (byte) tag.getValue();
		return 0;
	}
	
	public short getShort(String key) {
		Tag tag = get(key);
		if (isType(tag, TagType.SHORT))
			return (short) tag.getValue();
		return 0;
	}
	
	public int getInt(String key) {
		Tag tag = get(key);
		if (isType(tag, TagType.INT))
			return (int) tag.getValue();
		return 0;
	}
	
	public long getLong(String key) {
		Tag tag = get(key);
		if (isType(tag, TagType.LONG))
			return (long) tag.getValue();
		return 0;
	}
	
	public float getFloat(String key) {
		Tag tag = get(key);
		if (isType(tag, TagType.FLOAT))
			return (float) tag.getValue();
		return 0;
	}
	
	public double getDouble(String key) {
		Tag tag = get(key);
		if (isType(tag, TagType.DOUBLE))
			return (double) tag.getValue();
		return 0;
	}
	
	public boolean asBoolean(String key) {
		return NBTUtil.toBoolean(get(key));
	}
	
	public byte asByte(String key) {
		return NBTUtil.toNumber(get(key)).byteValue();
	}
	
	public short asShort(String key) {
		return NBTUtil.toNumber(get(key)).shortValue();
	}
	
	public int asInt(String key) {
		return NBTUtil.toNumber(get(key)).intValue();
	}
	
	public long asLong(String key) {
		return NBTUtil.toNumber(get(key)).longValue();
	}
	
	public float asFloat(String key) {
		return NBTUtil.toNumber(get(key)).floatValue();
	}
	
	public double asDouble(String key) {
		return NBTUtil.toNumber(get(key)).doubleValue();
	}
	
	public String getString(String key) {
		Tag tag = get(key);
		if (isType(tag, TagType.STRING))
			return (String) tag.getValue();
		return "";
	}
	
	public byte[] getBytes(String key) {
		Tag tag = get(key);
		if (isType(tag, TagType.BYTE_ARRAY))
			return ((byte[]) tag.getValue());
		return new byte[0];
	}
	
	public int[] getInts(String key) {
		Tag tag = get(key);
		if (isType(tag, TagType.INT_ARRAY))
			return ((int[]) tag.getValue());
		return new int[0];
	}

	public long[] getLongs(String key) {
		Tag tag = get(key);
		if (isType(tag, TagType.LONG_ARRAY))
			return ((long[]) tag.getValue());
		return new long[0];
	}
	
	public void clear() {
		value = new HashMap<>();
	}
	
	public void clearOrdered() {
		value = new LinkedHashMap<>();
	}
	
	public void setValue(Map<String, Tag> value) {
		if (value == null)
			clear();
		else
			this.value = value;
	}
	
	@Override
	public Map<String, Tag> getValue() {
		return value;
	}

	@Override
	protected void serialize(NBTOutputStream nbtOut, int depth) throws Exception {
		for (Tag tag : value.values()) {
			tag.serializeTag(nbtOut, incrementDepth(depth));
		}
		new EndTag().serializeTag(nbtOut, depth);
	}

	@Override
	protected Tag deserialize(NBTInputStream nbtIn, int depth) throws Exception {
		clear();
		for (;;) {
			Tag tag = Tag.deserializeTag(nbtIn, incrementDepth(depth));
			if (tag.getType() == TagType.END) {
				break;
			}
			set(tag);
		}
		return this;
	}

	@Override
	public String toTagString() {
		return toTagString(0);
	}
	
	@Override
	protected String toTagString(int depth) {
		depth = incrementDepth(depth);
		return NBTUtil.createNamePrefix(this) + valueToTagString(depth);
	}
	
	@Override
	protected String valueToTagString(int depth) {
		return "{" + NBTUtil.joinTagString(",", value.values().toArray(new Tag[0]), depth) + "}";
	}
	
	@Override
	public String toString() {
		return toString(0);
	}
	
	@Override
	protected String toString(int depth) {
		depth = incrementDepth(depth);
		return "<compound:" + getName() + ":{" + NBTUtil.joinArray(",", value.values().toArray(), "", depth, false) + "}>";
	}
	
	@Override
	public CompoundTag clone() {
		CompoundTag clone = new CompoundTag(getName());
		if (value instanceof LinkedHashMap) {
			clone.clearOrdered();
		}
		for (Entry<String, Tag> entry : value.entrySet()) {
			clone.set(entry.getValue().clone());
		}
		return clone;
	}
}
