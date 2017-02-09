package net.querz.nbt;

import java.util.HashMap;
import java.util.Map;

public enum TagType {
	END(0, EndTag.class),
	BYTE(1, ByteTag.class),
	SHORT(2, ShortTag.class),
	INT(3, IntTag.class),
	LONG(4, LongTag.class),
	FLOAT(5, FloatTag.class),
	DOUBLE(6, DoubleTag.class),
	BYTE_ARRAY(7, ByteArrayTag.class),
	STRING(8, StringTag.class),
	LIST(9, ListTag.class),
	COMPOUND(10, CompoundTag.class),
	INT_ARRAY(11, IntArrayTag.class),
	CUSTOM;
	
	private int id;
	private Class<? extends Tag> clazz;
	
	private static Map<Integer, Class<? extends CustomTag>> customTags = new HashMap<Integer, Class<? extends CustomTag>>();
	
	private TagType() {
	}
	
	private TagType(int id, Class<? extends Tag> clazz) {
		this.id = id;
		this.clazz = clazz;
	}
	
	public int getId() {
		return id;
	}
	
	public Class<? extends Tag> getTagClass() {
		return clazz;
	}
	
	public static Tag getCustomTag(int id) {
		try {
			return customTags.get(id).newInstance();
		} catch (InstantiationException | IllegalAccessException | NullPointerException ex) {
			throw new IllegalArgumentException("Unknown Tag ID: " + id);
		}
	}
	
	public Tag getTag() {
		if (this == TagType.CUSTOM)
			throw new IllegalArgumentException("getTag() not permitted for TagType.CUSTOM");
		try {
			return getTagClass().newInstance();
		} catch (InstantiationException | IllegalAccessException | NullPointerException ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	public static TagType match(int id) {
		for (TagType t : TagType.values())
			if (t.id == id)
				return t;
		if (customTags.containsKey(id))
			return TagType.CUSTOM;
		throw new IllegalArgumentException("Unknown Tag ID: " + id);
	}
	
	public static void registerCustomTag(int id, Class<? extends CustomTag> clazz) {
		try {
			match(id);
		} catch (IllegalArgumentException ex) {
			customTags.put(id, clazz);
			return;
		}
		throw new IllegalArgumentException("Id already registered: " + id);
	}
	
	public static void unregisterCustomTag(int id) {
		match(id);
		customTags.remove(id);
	}
	
	public static void unregisterAllCustomTags() {
		customTags.clear();
	}
}
