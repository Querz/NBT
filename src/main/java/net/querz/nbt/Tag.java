package net.querz.nbt;

import java.io.DataOutput;
import java.io.IOException;

public sealed interface Tag permits CollectionTag, CompoundTag, EndTag, NumberTag, StringTag {

	int MAX_DEPTH = 512;

	enum Type {
		END(0, EndTag.class, EndTag.READER, false),
		BYTE(1, ByteTag.class, ByteTag.READER, true),
		SHORT(2, ShortTag.class, ShortTag.READER, true),
		INT(3, IntTag.class, IntTag.READER, true),
		LONG(4, LongTag.class, LongTag.READER, true),
		FLOAT(5, FloatTag.class, FloatTag.READER, true),
		DOUBLE(6, DoubleTag.class, DoubleTag.READER, true),
		BYTE_ARRAY(7, ByteArrayTag.class, ByteArrayTag.READER, false),
		STRING(8, StringTag.class, StringTag.READER, false),
		LIST(9, ListTag.class, ListTag.READER, false),
		COMPOUND(10, CompoundTag.class, CompoundTag.READER, false),
		INT_ARRAY(11, IntArrayTag.class, IntArrayTag.READER, false),
		LONG_ARRAY(12, LongArrayTag.class, LongArrayTag.READER, false);

		public final byte id;
		public final Class<? extends Tag> tagClass;
		public final TagReader<?> reader;
		public final boolean isNumber;

		Type(int id, Class<? extends Tag> tagClass, TagReader<?> reader, boolean isNumber) {
			this.id = (byte) id;
			this.tagClass = tagClass;
			this.reader = reader;
			this.isNumber = isNumber;
		}

		public static Type valueOf(byte b) {
			for (Type type : values()) {
				if (type.id == b) {
					return type;
				}
			}
			throw new IllegalArgumentException("No tag type corresponds to byte "+b);
		}

	}

	void write(DataOutput out) throws IOException;

	String toString();

	Type getType();

	Tag copy();

	void accept(TagVisitor visitor) throws Exception;
}
