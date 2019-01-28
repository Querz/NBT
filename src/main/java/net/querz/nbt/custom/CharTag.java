package net.querz.nbt.custom;

import net.querz.nbt.Tag;
import net.querz.nbt.TagFactory;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class CharTag extends Tag<Character> implements Comparable<CharTag> {

	public static final char ZERO_VALUE = '\u0000';

	public static void register() {
		TagFactory.registerCustomTag(110, CharTag::new, CharTag.class);
	}

	public CharTag() {
		super(ZERO_VALUE);
	}

	public CharTag(char value) {
		super(value);
	}

	@Override
	public Character getValue() {
		return super.getValue();
	}

	public void setValue(char value) {
		super.setValue(value);
	}

	@Override
	public void serializeValue(DataOutputStream dos, int maxDepth) throws IOException {
		dos.writeChar(getValue());
	}

	@Override
	public void deserializeValue(DataInputStream dis, int maxDepth) throws IOException {
		setValue(dis.readChar());
	}

	@Override
	public String valueToString(int maxDepth) {
		return escapeString(getValue() + "", false);
	}

	@Override
	public String valueToTagString(int maxDepth) {
		return escapeString(getValue() + "", true);
	}

	@Override
	public boolean equals(Object other) {
		return super.equals(other) && getValue() == ((CharTag) other).getValue();
	}

	@Override
	public int compareTo(CharTag o) {
		return Character.compare(getValue(), o.getValue());
	}

	@Override
	public CharTag clone() {
		return new CharTag(getValue());
	}
}
