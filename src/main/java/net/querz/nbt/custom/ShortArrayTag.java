package net.querz.nbt.custom;

import net.querz.nbt.ArrayTag;
import net.querz.nbt.TagFactory;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class ShortArrayTag extends ArrayTag<short[]> {

	public static void register() {
		TagFactory.registerCustomTag(100, ShortArrayTag.class);
	}

	public ShortArrayTag() {}

	public ShortArrayTag(String name) {
		super(name);
	}

	public ShortArrayTag(String name, short[] value) {
		super(name, value);
	}

	@Override
	public byte getID() {
		return 100;
	}

	@Override
	public void serializeValue(DataOutputStream dos, int depth) throws IOException {
		dos.writeInt(length());
		for (int i : getValue()) {
			dos.writeShort(i);
		}
	}

	@Override
	public void deserializeValue(DataInputStream dis, int depth) throws IOException {
		int length = dis.readInt();
		setValue(new short[length]);
		for (int i = 0; i < length; i++) {
			getValue()[i] = dis.readShort();
		}
	}

	@Override
	public String valueToString(int depth) {
		return arrayToString(getValue(), "S", "s");
	}

	@Override
	protected short[] getEmptyValue() {
		return new short[0];
	}

	@Override
	public ShortArrayTag clone() {
		return new ShortArrayTag(getName(), Arrays.copyOf(getValue(), length()));
	}

	@Override
	public boolean valueEquals(short[] value) {
		return getValue() == value || getValue().length == value.length && Arrays.equals(getValue(), value);
	}
}
