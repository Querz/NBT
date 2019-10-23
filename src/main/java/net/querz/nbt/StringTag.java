package net.querz.nbt;

import 	java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.IOException;

import static net.querz.nbt.NBTUtil.writeUTF;

public class StringTag extends Tag<String> implements Comparable<StringTag> {

	public static final String ZERO_VALUE = "";

	public StringTag() {
		super(ZERO_VALUE);
	}

	public StringTag(String value) {
		super(value);
	}

	@Override
	public String getValue() {
		return super.getValue();
	}

	@Override
	public void setValue(String value) {
		super.setValue(value);
	}

	@Override
	public void serializeValue(DataOutput dos, int maxDepth) throws IOException {
		writeUTF(getValue(), dos);
	}

	@Override
	public void deserializeValue(DataInput dis, int maxDepth) throws IOException {
		setValue(DataInputStream.readUTF(dis));
	}

	@Override
	public String valueToString(int maxDepth) {
		return escapeString(getValue(), false);
	}

	@Override
	public String valueToTagString(int maxDepth) {
		return escapeString(getValue(), true);
	}

	@Override
	public boolean equals(Object other) {
		return super.equals(other) && getValue().equals(((StringTag) other).getValue());
	}

	@Override
	public int compareTo(StringTag o) {
		return getValue().compareTo(o.getValue());
	}

	@Override
	public StringTag clone() {
		return new StringTag(getValue());
	}
}
