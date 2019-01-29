package net.querz.nbt.custom;

import net.querz.nbt.ArrayTag;
import net.querz.nbt.TagFactory;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class ShortArrayTag extends ArrayTag<short[]> implements Comparable<ShortArrayTag> {

	public static final short[] ZERO_VALUE = new short[0];

	public static void register() {
		TagFactory.registerCustomTag(100, ShortArrayTag::new, ShortArrayTag.class);
	}

	public ShortArrayTag() {
		super(ZERO_VALUE);
	}

	public ShortArrayTag(short[] value) {
		super(value);
	}

	@Override
	public void serializeValue(DataOutputStream dos, int maxDepth) throws IOException {
		dos.writeInt(length());
		for (int i : getValue()) {
			dos.writeShort(i);
		}
	}

	@Override
	public void deserializeValue(DataInputStream dis, int maxDepth) throws IOException {
		int length = dis.readInt();
		setValue(new short[length]);
		for (int i = 0; i < length; i++) {
			getValue()[i] = dis.readShort();
		}
	}

	@Override
	public String valueToTagString(int maxDepth) {
		return arrayToString("S", "s");
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
