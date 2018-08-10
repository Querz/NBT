package net.querz.nbt.custom;

import net.querz.nbt.Tag;
import net.querz.nbt.TagFactory;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class CharTag extends Tag<Character> {

	public static void register() {
		TagFactory.registerCustomTag(110, CharTag.class);
	}

	public CharTag() {}

	public CharTag(String name) {
		super(name);
	}

	public CharTag(String name, char value) {
		super(name, value);
	}

	@Override
	public byte getID() {
		return 110;
	}

	@Override
	public void serializeValue(DataOutputStream dos, int depth) throws IOException {
		dos.writeChar(getValue());
	}

	@Override
	public void deserializeValue(DataInputStream dis, int depth) throws IOException {
		setValue(dis.readChar());
	}

	@Override
	public String valueToString(int depth) {
		return getValue() + "";
	}

	@Override
	protected Character getEmptyValue() {
		return 0;
	}

	@Override
	public CharTag clone() {
		return new CharTag(getName(), getValue());
	}

	@Override
	public boolean valueEquals(Character value) {
		return getValue() == value;
	}
}
