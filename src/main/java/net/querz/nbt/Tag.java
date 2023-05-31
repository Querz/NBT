package net.querz.nbt;

import java.io.DataOutput;
import java.io.IOException;

public sealed interface Tag permits CollectionTag, CompoundTag, EndTag, NumberTag, StringTag {

	int MAX_DEPTH = 512;
	byte END = 0;
	byte BYTE = 1;
	byte SHORT = 2;
	byte INT = 3;
	byte LONG = 4;
	byte FLOAT = 5;
	byte DOUBLE = 6;
	byte BYTE_ARRAY = 7;
	byte STRING = 8;
	byte LIST = 9;
	byte COMPOUND = 10;
	byte INT_ARRAY = 11;
	byte LONG_ARRAY = 12;
	byte NUMBER = 99;

	void write(DataOutput out) throws IOException;

	String toString();

	byte getID();

	TagType<?> getType();

	Tag copy();

	void accept(TagVisitor visitor);
}
