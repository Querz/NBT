package net.querz.nbt.custom;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import net.querz.nbt.LongArrayTag;
import net.querz.nbt.LongTag;
import net.querz.nbt.NBTInputStream;
import net.querz.nbt.NBTOutputStream;
import net.querz.nbt.NBTUtil;
import net.querz.nbt.ShortTag;
import net.querz.nbt.StringTag;
import net.querz.nbt.Tag;
import net.querz.nbt.TagType;

public class StructTag extends Tag implements CustomTag {
	public static final int TAG_ID = 120;
	
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
		super(TagType.CUSTOM, name);
		setValue(value);
	}
	
	public void setValue(List<Tag> value) {
		if (value == null)
			clear();
		else
			this.value = value;
	}
	
	public void clear() {
		value = new ArrayList<>();
	}
	
	public void clear(int init) {
		value = new ArrayList<>(init);
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

	public void addString(String s) {
		add(new StringTag(s));
	}
	
	public void addByteArray(byte[] b) {
		add(new ByteArrayTag(b));
	}
	
	public void addIntArray(int[] i) {
		add(new IntArrayTag(i));
	}

	public void addLongArray(long[] l) {
		add(new LongArrayTag(l));
	}

	public void addList(TagType type, List<Tag> l) {
		add(new ListTag(type, l));
	}

	public void addList(TagType type, Tag... t) {
		add(new ListTag(type, Arrays.asList(t)));
	}

	public void addMap(Map<String, Tag> m) {
		add(new CompoundTag(m));
	}
	
	public Tag get(int index) {
		return value.get(index);
	}
	
	public boolean getBoolean(int index) {
		return get(index).getType() == TagType.BYTE && ((ByteTag) get(index)).getBoolean();
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

	public byte[] getBytes(int index) {
		if (get(index).getType() == TagType.BYTE_ARRAY)
			return ((ByteArrayTag) get(index)).getValue();
		return new byte[0];
	}

	public int[] getInts(int index) {
		if (get(index).getType() == TagType.INT_ARRAY)
			return ((IntArrayTag) get(index)).getValue();
		return new int[0];
	}

	public long[] getLongs(int index) {
		if (get(index).getType() == TagType.LONG_ARRAY)
			return ((LongArrayTag) get(index)).getValue();
		return new long[0];
	}

	public List<Tag> getList(int index) {
		if (get(index).getType() == TagType.LIST)
			return ((ListTag) get(index)).getValue();
		return new ArrayList<>(0);
	}

	public Map<String, Tag> getMap(int index) {
		if (get(index).getType() == TagType.COMPOUND)
			return ((CompoundTag) get(index)).getValue();
		return new HashMap<>(0);
	}

	public ByteTag getByteTag(int index) {
		Tag tag = get(index);
		if (tag.getType() == TagType.BYTE)
			return (ByteTag) tag;
		return null;
	}

	public ShortTag getShortTag(int index) {
		Tag tag = get(index);
		if (tag.getType() == TagType.SHORT)
			return (ShortTag) tag;
		return null;
	}

	public IntTag getIntTag(int index) {
		Tag tag = get(index);
		if (tag.getType() == TagType.INT)
			return (IntTag) tag;
		return null;
	}

	public LongTag getLongTag(int index) {
		Tag tag = get(index);
		if (tag.getType() == TagType.LONG)
			return (LongTag) tag;
		return null;
	}

	public FloatTag getFloatTag(int index) {
		Tag tag = get(index);
		if (tag.getType() == TagType.FLOAT)
			return (FloatTag) tag;
		return null;
	}

	public DoubleTag getDoubleTag(int index) {
		Tag tag = get(index);
		if (tag.getType() == TagType.DOUBLE)
			return (DoubleTag) tag;
		return null;
	}

	public StringTag getStringTag(int index) {
		Tag tag = get(index);
		if (tag.getType() == TagType.STRING)
			return (StringTag) tag;
		return null;
	}

	public ByteArrayTag getByteArrayTag(int index) {
		Tag tag = get(index);
		if (tag.getType() == TagType.BYTE_ARRAY)
			return (ByteArrayTag) tag;
		return null;
	}

	public IntArrayTag getIntArrayTag(int index) {
		Tag tag = get(index);
		if (tag.getType() == TagType.INT_ARRAY)
			return (IntArrayTag) tag;
		return null;
	}

	public LongArrayTag getLongArrayTag(int index) {
		Tag tag = get(index);
		if (tag.getType() == TagType.LONG_ARRAY)
			return (LongArrayTag) tag;
		return null;
	}

	public ListTag getListTag(int index) {
		Tag tag = get(index);
		if (tag.getType() == TagType.LIST)
			return (ListTag) tag;
		return null;
	}

	public CompoundTag getCompoundTag(int index) {
		Tag tag = get(index);
		if (tag.getType() == TagType.COMPOUND)
			return (CompoundTag) tag;
		return null;
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

	public int size() {
		return value.size();
	}

	@Override
	public int getId() {
		return TAG_ID;
	}
	
	@Override
	public List<Tag> getValue() {
		return value;
	}

	@Override
	protected void serialize(NBTOutputStream nbtOut, int depth) throws Exception {
		int size = value.size();
		nbtOut.getDataOutputStream().writeInt(size);
		for (Tag tag : value)
			tag.serializeTag(nbtOut, depth);
	}

	@Override
	protected Tag deserialize(NBTInputStream nbtIn, int depth) throws Exception {
		int size = nbtIn.getDataInputStream().readInt();
		clear(size);
		for (int i = 0; i < size; i++) {
			Tag tag = Tag.deserializeTag(nbtIn, depth);
			if (tag instanceof EndTag)
				throw new IOException("EndTag not permitted in a struct.");
			add(tag);
		}
		return null;
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
	public String valueToTagString(int depth) {
		return "[" + NBTUtil.joinArray(",", value.toArray(new Tag[0]), depth) + "]";
	}
	
	@Override
	public String toString() {
		return toString(0);
	}
	
	@Override
	public String toString(int depth) {
		depth = incrementDepth(depth);
		return "<struct:" + getName() + ":[" + NBTUtil.joinArray(",", value.toArray(), depth) + "]>";
	}

	@Override
	public boolean valueEquals(Tag other) {
		return other instanceof StructTag && ((StructTag) other).value.equals(value);
	}
	
	@Override
	public StructTag clone() {
		List<Tag> clone = new ArrayList<>(value.size());
		for (Tag tag : value) {
			clone.add(tag.clone());
		}
		return new StructTag(getName(), clone);
	}
}
