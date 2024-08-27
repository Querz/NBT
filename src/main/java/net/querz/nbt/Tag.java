package net.querz.nbt;

import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public interface Tag {

	int MAX_DEPTH = 512;

	enum Type {
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
		LONG_ARRAY(12, LongArrayTag.class);

		public final byte id;
		public final Class<? extends Tag> tagClass;
		public final TagReader<?> reader;
		public final boolean isNumber;

		Type(int id, Class<? extends Tag> tagClass) {
			this.id = (byte) id;
			this.tagClass = tagClass;
			try {
				this.reader = (TagReader<?>) tagClass.getDeclaredField("READER").get(null);
			} catch (ReflectiveOperationException ex) {
				throw new RuntimeException("Malformed tag class: could not find reader", ex);
			}
			this.isNumber = NumberTag.class.isAssignableFrom(tagClass);
		}

		// Using an array instead of a map saves us from boxing, assuming tag ids stay continuous
		private static final Type[] idCache;
		private static final Map<Class<? extends Tag>, Type> tagClassCache;

		static {
			idCache = new Type[values().length];
			tagClassCache = new HashMap<>();

			for (Type type : values()) {
				idCache[type.id] = type;
				tagClassCache.put(type.tagClass, type);
			}
		}

		public static Type valueOf(byte b) {
			if (b < 0 || b >= idCache.length) {
				throw new IllegalArgumentException("No tag type corresponds to byte "+b);
			}
			return idCache[b];
		}

		public static Type valueOf(Class<? extends Tag> clazz) {
			if (!tagClassCache.containsKey(clazz)) {
				throw new IllegalArgumentException("No tag type corresponds to class "+clazz.getName());
			}
			return tagClassCache.get(clazz);
		}

	}

	void write(DataOutput out) throws IOException;

	String toString();

	default Type getType() {
		return Type.valueOf(getClass());
	}

	Tag copy();

	void accept(TagVisitor visitor);
}
