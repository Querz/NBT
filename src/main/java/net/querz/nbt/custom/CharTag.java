package net.querz.nbt.custom;

import net.querz.nbt.*;

import java.io.IOException;

public class CharTag extends Tag implements CustomTag {
	public static final int TAG_ID = 110;

	public static void register() {
		TagType.registerCustomTag(TAG_ID, CharTag.class);
	}

	private char value;

	public CharTag(String name) {
		this(name, (char) 0);
	}

	public CharTag() {
		this((char) 0);
	}

	public CharTag(char value) {
		this("", value);
	}

	public CharTag(String name, char value) {
		super(TagType.CUSTOM, name);
		setValue(value);
	}

	public void setValue(char value) {
		this.value = value;
	}

	@Override
	public int getId() {
		return TAG_ID;
	}

	@Override
	protected String valueToTagString(int depth) {
		return value + "";
	}

	@Override
	protected void serialize(NBTOutputStream nbtOut, int depth) throws IOException {
		nbtOut.getDataOutputStream().writeChar(value);
	}

	@Override
	protected Tag deserialize(NBTInputStream nbtIn, int depth) throws IOException {
		value = nbtIn.getDataInputStream().readChar();
		return this;
	}

	@Override
	public Character getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "<char:" + getName() + ":" + value + ">";
	}

	@Override
	public String toTagString() {
		return NBTUtil.createNamePrefix(this) + valueToTagString(0);
	}

	@Override
	public Tag clone() {
		return new CharTag();
	}
}
