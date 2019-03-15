package net.querz.nbt.custom;

import net.querz.nbt.Tag;
import net.querz.nbt.io.NBTInputStream;
import net.querz.nbt.io.NBTOutputStream;
import net.querz.nbt.io.NBTUtil;
import java.io.IOException;

public class CharTag extends Tag<Character> implements Comparable<CharTag> {

	public static final char ZERO_VALUE = '\u0000';

	public static void register() {
		NBTUtil.registerCustomTag(110, (o, t, m) -> serialize(o, t), (i, m) -> deserialize(i), CharTag.class);
	}

	public CharTag() {
		super(ZERO_VALUE);
	}

	public CharTag(char value) {
		super(value);
	}

	@Override
	public byte getID() {
		return 110;
	}

	@Override
	public Character getValue() {
		return super.getValue();
	}

	public void setValue(char value) {
		super.setValue(value);
	}

	public static void serialize(NBTOutputStream dos, CharTag tag) throws IOException {
		dos.writeChar(tag.getValue());
	}

	public static CharTag deserialize(NBTInputStream dis) throws IOException {
		return new CharTag(dis.readChar());
	}

	@Override
	public String valueToString(int maxDepth) {
		return escapeString(getValue() + "", false);
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
