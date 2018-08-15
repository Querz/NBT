package net.querz.nbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class StringTag extends Tag<String> {

	public StringTag() {}

	public StringTag(String name) {
		super(name);
	}

	public StringTag(String name, String value) {
		super(name, value);
	}

	@Override
	public byte getID() {
		return 8;
	}

	@Override
	public void serializeValue(DataOutputStream dos, int depth) throws IOException {
		byte[] data = getValue().getBytes(CHARSET);
		dos.writeShort(data.length);
		dos.write(data);
	}

	@Override
	public void deserializeValue(DataInputStream dis, int depth) throws IOException {
		int length = dis.readShort() & 0xFFFF;
		byte[] bytes = new byte[length];
		dis.readFully(bytes);
		setValue(new String(bytes, CHARSET));
	}

	@Override
	public String valueToTagString(int depth) {
		return escapeString(getValue(), true);
	}

	@Override
	public String valueToString(int depth) {
		return escapeString(getValue(), false);
	}

	@Override
	protected String getEmptyValue() {
		return "";
	}

	@Override
	public void setValue(String value) {
		super.setValue(checkNull(value));
	}

	@Override
	public String getValue() {
		return super.getValue();
	}

	@Override
	public StringTag clone() {
		return new StringTag(getName(), getValue());
	}

	@Override
	public boolean valueEquals(String value) {
		return getValue().equals(value);
	}
}
