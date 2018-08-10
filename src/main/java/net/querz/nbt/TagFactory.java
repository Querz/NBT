package net.querz.nbt;

import java.util.HashMap;
import java.util.Map;

public class TagFactory {

	private static Map<Integer, Class<? extends Tag>> customTags = new HashMap<>();

	private TagFactory() {}

	public static Tag fromID(int id) {
		switch (id) {
			case 0:
				return new EndTag();
			case 1:
				return new ByteTag();
			case 2:
				return new ShortTag();
			case 3:
				return new IntTag();
			case 4:
				return new LongTag();
			case 5:
				return new FloatTag();
			case 6:
				return new DoubleTag();
			case 7:
				return new ByteArrayTag();
			case 8:
				return new StringTag();
			case 9:
				return new ListTag();
			case 10:
				return new CompoundTag();
			case 11:
				return new IntArrayTag();
			case 12:
				return new LongArrayTag();
			default:
				Class<?> clazz = customTags.get(id);
				if (clazz != null) {
					try {
						return (Tag) clazz.newInstance();
					} catch (InstantiationException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
				throw new IllegalArgumentException("unknown Tag id: " + id);
		}
	}

	public static void registerCustomTag(int id, Class<? extends Tag> clazz) {
		if (id < 0) {
			throw new IllegalArgumentException("id cannot be negative");
		}
		if (id <= 12) {
			throw new IllegalArgumentException("cannot overwrite default tags");
		}
		if (customTags.containsKey(id)) {
			throw new IllegalArgumentException("custom tag already registered");
		}
		customTags.put(id, clazz);
	}

	public static void unregisterCustomTag(int id) {
		customTags.remove(id);
	}
}
