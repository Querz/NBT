package net.querz.nbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

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
	public void serializeValue(DataOutputStream dos, int maxDepth) throws IOException {
		dos.writeUTF(getValue());
	}

	@Override
	public void deserializeValue(DataInputStream dis, int maxDepth) throws IOException {
		setValue(dis.readUTF());
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
