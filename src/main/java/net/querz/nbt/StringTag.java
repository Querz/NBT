package net.querz.nbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class StringTag extends Tag<String> {

	public StringTag() {}

	public StringTag(String value) {
		super(value);
	}

	@Override
	public byte getID() {
		return 8;
	}

	@Override
	protected String getEmptyValue() {
		return "";
	}

	@Override
	public String getValue() {
		return super.getValue();
	}

	@Override
	public void setValue(String value) {
		super.setValue(checkNull(value));
	}

	@Override
	public void serializeValue(DataOutputStream dos, int depth) throws IOException {
		dos.writeUTF(getValue());
	}

	@Override
	public void deserializeValue(DataInputStream dis, int depth) throws IOException {
		setValue(dis.readUTF());
	}

	@Override
	public String valueToString(int depth) {
		return escapeString(getValue(), false);
	}

	@Override
	public String valueToTagString(int depth) {
		return escapeString(getValue(), true);
	}

	@Override
	public boolean equals(Object other) {
		return super.equals(other) && getValue().equals(((StringTag) other).getValue());
	}

	@Override
	public int compareTo(Tag<String> o) {
		return getValue().compareTo(o.getValue());
	}

	@Override
	public StringTag clone() {
		return new StringTag(getValue());
	}
}
