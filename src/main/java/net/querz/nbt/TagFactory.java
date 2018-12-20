package net.querz.nbt;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class TagFactory {

	private static class TagMapping<T extends Tag<?>> {

		private int id;
		private Supplier<T> factory;
		private Class<T> clazz;

		TagMapping(int id, Supplier<T> factory, Class<T> clazz) {
			this.id = id;
			this.factory = factory;
			this.clazz = clazz;
		}
	}

	private static Map<Integer, TagMapping<?>> idMapping = new HashMap<>();
	private static Map<Class<?>, TagMapping<?>> classMapping = new HashMap<>();
	static {
		put(0, () -> EndTag.INSTANCE, EndTag.class);
		put(1, ByteTag::new, ByteTag.class);
		put(2, ShortTag::new, ShortTag.class);
		put(3, IntTag::new, IntTag.class);
		put(4, LongTag::new, LongTag.class);
		put(5, FloatTag::new, FloatTag.class);
		put(6, DoubleTag::new, DoubleTag.class);
		put(7, ByteArrayTag::new, ByteArrayTag.class);
		put(8, StringTag::new, StringTag.class);
		put(9, ListTag::createUnchecked, ListTag.class);
		put(10, CompoundTag::new, CompoundTag.class);
		put(11, IntArrayTag::new, IntArrayTag.class);
		put(12, LongArrayTag::new, LongArrayTag.class);
	}

	private static <T extends Tag<?>> void put(int id, Supplier<T> factory, Class<T> clazz) {
		TagMapping<T> mapping = new TagMapping<>(id, factory, clazz);
		idMapping.put(id, mapping);
		classMapping.put(clazz, mapping);
	}

	private TagFactory() {}

	public static Tag<?> fromID(int id) {
		TagMapping<?> mapping = idMapping.get(id);
		if (mapping == null) {
			throw new IllegalArgumentException("unknown Tag id " + id);
		}
		return mapping.factory.get();
	}

	public static Class<?> classFromID(int id) {
		TagMapping<?> mapping = idMapping.get(id);
		if (mapping == null) {
			throw new IllegalArgumentException("unknown Tag id " + id);
		}
		return mapping.clazz;
	}

	public static byte idFromClass(Class<?> clazz) {
		TagMapping<?> mapping = classMapping.get(clazz);
		if (mapping == null) {
			throw new IllegalArgumentException("unknown Tag class " + clazz.getName());
		}
		return (byte) mapping.id;
	}

	public static <T extends Tag<?>> void registerCustomTag(int id, Supplier<T> factory, Class<T> clazz) {
		checkID(id);
		if (idMapping.containsKey(id)) {
			throw new IllegalArgumentException("custom tag already registered");
		}
		put(id, factory, clazz);
	}

	public static void unregisterCustomTag(int id) {
		idMapping.remove(id);
		for (TagMapping<?> mapping : classMapping.values()) {
			if (mapping.id == id) {
				classMapping.remove(mapping.clazz);
				return;
			}
		}
	}

	private static void checkID(int id) {
		if (id < 0) {
			throw new IllegalArgumentException("id cannot be negative");
		}
		if (id <= 12) {
			throw new IllegalArgumentException("cannot change default tags");
		}
		if (id > Byte.MAX_VALUE) {
			throw new IllegalArgumentException("id out of bounds: " + id);
		}
	}
}
