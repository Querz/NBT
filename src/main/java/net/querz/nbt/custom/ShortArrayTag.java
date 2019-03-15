package net.querz.nbt.custom;

import net.querz.nbt.ArrayTag;
import net.querz.nbt.io.NBTInputStream;
import net.querz.nbt.io.NBTOutputStream;
import net.querz.nbt.io.NBTUtil;
import java.io.IOException;
import java.util.Arrays;

public class ShortArrayTag extends ArrayTag<short[]> implements Comparable<ShortArrayTag> {

	public static final short[] ZERO_VALUE = new short[0];

	public static void register() {
		NBTUtil.registerCustomTag(100, (o, t, m) -> serialize(o, t), (i, m) -> deserialize(i), ShortArrayTag.class);
	}

	public ShortArrayTag() {
		super(ZERO_VALUE);
	}

	public ShortArrayTag(short[] value) {
		super(value);
	}

	@Override
	public byte getID() {
		return 100;
	}

	public static void serialize(NBTOutputStream dos, ShortArrayTag tag) throws IOException {
		dos.writeInt(tag.length());
		for (int i : tag.getValue()) {
			dos.writeShort(i);
		}
	}

	public static ShortArrayTag deserialize(NBTInputStream dis) throws IOException {
		ShortArrayTag tag = new ShortArrayTag(new short[dis.readInt()]);
		for (int i = 0; i < tag.length(); i++) {
			tag.getValue()[i] = dis.readShort();
		}
		return tag;
	}

	@Override
	public boolean equals(Object other) {
		return super.equals(other)
				&& (getValue() == ((ShortArrayTag) other).getValue()
				|| getValue().length == (((ShortArrayTag) other).length())
				&& Arrays.equals(getValue(), ((ShortArrayTag) other).getValue()));
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(getValue());
	}

	@Override
	public int compareTo(ShortArrayTag other) {
		return Integer.compare(length(), other.length());
	}

	@Override
	public ShortArrayTag clone() {
		return new ShortArrayTag(Arrays.copyOf(getValue(), length()));
	}
}
