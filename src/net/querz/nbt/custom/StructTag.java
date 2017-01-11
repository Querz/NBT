package net.querz.nbt.custom;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

import net.querz.nbt.ByteArrayTag;
import net.querz.nbt.ByteTag;
import net.querz.nbt.CompoundTag;
import net.querz.nbt.CustomTag;
import net.querz.nbt.DoubleTag;
import net.querz.nbt.EndTag;
import net.querz.nbt.FloatTag;
import net.querz.nbt.IntArrayTag;
import net.querz.nbt.IntTag;
import net.querz.nbt.ListTag;
import net.querz.nbt.LongTag;
import net.querz.nbt.NBTInputStream;
import net.querz.nbt.NBTOutputStream;
import net.querz.nbt.NBTUtil;
import net.querz.nbt.ShortTag;
import net.querz.nbt.StringTag;
import net.querz.nbt.Tag;
import net.querz.nbt.TagType;

public class StructTag extends CustomTag {
	public static final int TAG_ID = 20;
	
	public static void register() {
		TagType.registerCustomTag(TAG_ID, StructTag.class);
	}
	
	private List<Tag> value;
	
	public StructTag() {
		this("", null);
	}
	
	public StructTag(String name) {
		this(name, null);
	}
	
	public StructTag(List<Tag> value) {
		this("", value);
	}
	
	public StructTag(String name, List<Tag> value) {
		super(TAG_ID, name);
		setValue(value);
	}
	
	public void setValue(List<Tag> value) {
		if (value == null)
			clear();
		else
			this.value = value;
	}
	
	public void clear() {
		value = new ArrayList<Tag>();
	}
	
	public void clear(int init) {
		value = new ArrayList<Tag>(init);
	}
	
	public void add(Tag tag) {
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
	
	public void addByteArray(byte[] b) {
		add(new ByteArrayTag(b));
	}
	
	public void addList(TagType type, List<Tag> l) {
		add(new ListTag(type, l));
	}
	
	public void addIntArray(int[] i) {
		add(new IntArrayTag(i));
	}
	
	public void addString(String s) {
		add(new StringTag(s));
	}
	
	public Tag get(int index) {
		try {
			return value.get(index);
		} catch (NoSuchElementException ex) {
			return null;
		}
	}
	
	public boolean getBoolean(int index) {
		if (get(index).getType() == TagType.BYTE)
			return ((ByteTag) get(index)).getBoolean();
		return false;
	}
	
	public byte getByte(int index) {
		if (get(index).getType() == TagType.BYTE)
			return ((ByteTag) get(index)).getValue();
		return 0;
	}
	
	public short getShort(int index) {
		if (get(index).getType() == TagType.SHORT)
			return ((ShortTag) get(index)).getValue();
		return 0;
	}
	
	public int getInt(int index) {
		if (get(index).getType() == TagType.INT)
			return ((IntTag) get(index)).getValue();
		return 0;
	}
	
	public long getLong(int index) {
		if (get(index).getType() == TagType.LONG)
			return ((LongTag) get(index)).getValue();
		return 0;
	}
	
	public float getFloat(int index) {
		if (get(index).getType() == TagType.FLOAT)
			return ((FloatTag) get(index)).getValue();
		return 0;
	}
	
	public double getDouble(int index) {
		if (get(index).getType() == TagType.DOUBLE)
			return ((DoubleTag) get(index)).getValue();
		return 0;
	}
	
	public String getString(int index) {
		if (get(index).getType() == TagType.STRING)
			return ((StringTag) get(index)).getValue();
		return "";
	}
	
	public boolean asBoolean(int index) {
		return NBTUtil.toBoolean(get(index));
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
	
	@Override
	public List<Tag> getValue() {
		return value;
	}

	@Override
	protected void serialize(NBTOutputStream nbtOut) throws IOException {
		int size = value.size();
		nbtOut.getDataOutputStream().writeInt(size);
		for (Tag tag : value)
			tag.serializeTag(nbtOut);
	}

	@Override
	protected Tag deserialize(NBTInputStream nbtIn) throws IOException {
		int size = nbtIn.getDataInputStream().readInt();
		clear(size);
		for (int i = 0; i < size; i++) {
			Tag tag = Tag.deserializeTag(nbtIn);
			if (tag instanceof EndTag)
				throw new IOException("EndTag not permitted in a struct.");
			add(tag);
		}
		return null;
	}

	@Override
	public String toTagString() {
		return NBTUtil.createNamePrefix(this) + "[" + NBTUtil.joinTagString(",", value.toArray()) + "]";
	}
	
	@Override
	public String toString() {
		return "<struct:" + getName() + ":[" + NBTUtil.joinObjects(",", value.toArray()) + "]>";
	}
	
	@Override
	public StructTag clone() {
		return new StructTag(getName(), value);
	}
}
