package net.querz.nbt;

import java.io.DataOutput;
import java.io.IOException;

public sealed interface Tag permits CollectionTag, CompoundTag, EndTag, NumberTag, StringTag {

	int MAX_DEPTH = 512;

	enum TypeId {
		END(0, false),
		BYTE(1, true),
		SHORT(2, true),
		INT(3, true),
		LONG(4, true),
		FLOAT(5, true),
		DOUBLE(6, true),
		BYTE_ARRAY(7, false),
		STRING(8, false),
		LIST(9, false),
		COMPOUND(10, false),
		INT_ARRAY(11, false),
		LONG_ARRAY(12, false);

		public final byte id;
		public final boolean isNumber;

		TypeId(int id, boolean isNumber) {
			this.id = (byte) id;
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

	TagReader<?> getReader();

	Tag copy();

	void accept(TagVisitor visitor) throws Exception;
}
