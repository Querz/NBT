package net.querz.nbt;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class TagFactory {

	private static Map<Integer, Supplier<? extends Tag<?>>> customTags = new HashMap<>();

	private TagFactory() {}

	public static Tag fromID(int id) {
		switch (id) {
			case 0:
				return EndTag.INSTANCE;
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
				return ListTag.createUnchecked();
			case 10:
				return new CompoundTag();
			case 11:
				return new IntArrayTag();
			case 12:
				return new LongArrayTag();
			default:
				Supplier<? extends Tag<?>> factory = customTags.get(id);
				if (factory != null) {
					return factory.get();
				}
				throw new IllegalArgumentException("unknown Tag id: " + id);
		}
	}

	public static void registerCustomTag(int id, Supplier<? extends Tag<?>> factory) {
		if (id < 0) {
			throw new IllegalArgumentException("id cannot be negative");
		}
		if (id <= 12) {
			throw new IllegalArgumentException("cannot overwrite default tags");
		}
		if (id > Byte.MAX_VALUE) {
			throw new IllegalArgumentException("id out of bounds: " + id);
		}
		if (customTags.containsKey(id)) {
			throw new IllegalArgumentException("custom tag already registered");
		}
		customTags.put(id, factory);
	}

	public static void unregisterCustomTag(int id) {
		customTags.remove(id);
	}
}
