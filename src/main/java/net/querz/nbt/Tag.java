package net.querz.nbt;

import java.io.DataOutput;
import java.io.IOException;

public sealed interface Tag permits CollectionTag, CompoundTag, EndTag, NumberTag, StringTag {

	int MAX_DEPTH = 512;

	enum TypeId {
		END(0, EndTag.READER, false),
		BYTE(1, ByteTag.READER, true),
		SHORT(2, ShortTag.READER, true),
		INT(3, IntTag.READER, true),
		LONG(4, LongTag.READER, true),
		FLOAT(5, FloatTag.READER, true),
		DOUBLE(6, DoubleTag.READER, true),
		BYTE_ARRAY(7, ByteArrayTag.READER, false),
		STRING(8, StringTag.READER, false),
		LIST(9, ListTag.READER, false),
		COMPOUND(10, CompoundTag.READER, false),
		INT_ARRAY(11, IntArrayTag.READER, false),
		LONG_ARRAY(12, LongArrayTag.READER, false);

		public final byte id;
		public final TagReader<?> reader;
		public final boolean isNumber;

		TypeId(int id, TagReader<?> reader, boolean isNumber) {
			this.id = (byte) id;
			this.reader = reader;
			this.isNumber = isNumber;
		}

		public static TypeId valueOf(byte b) {
			for (TypeId id : values()) {
				if (id.id == b) {
					return id;
				}
			}
			throw new IllegalArgumentException("No tag type corresponds to byte "+b);
		}

	}

	void write(DataOutput out) throws IOException;

	String toString();

	TypeId getID();

	Tag copy();

	void accept(TagVisitor visitor) throws Exception;
}
