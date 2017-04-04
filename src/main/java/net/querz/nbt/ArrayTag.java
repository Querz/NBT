package net.querz.nbt;

import net.querz.nbt.util.Array;

public abstract class ArrayTag extends Tag {

	public ArrayTag(TagType type, String name) {
		super(type, name);
	}

	public int length() {
		return Array.getLength(getValue());
	}

	@Override
	public String toTagString() {
		return NBTUtil.createNamePrefix(this) + valueToTagString(0);
	}

	@Override
	protected String valueToTagString(int depth) {
		return "[" + NBTUtil.joinArray(",", getValue()) + "]";
	}

	@Override
	public boolean equals(Object other) {
		if (this.getClass().equals(other.getClass())) {
			Tag tag = (Tag) other;
			return getName() != null && getName().equals(tag.getName()) && Array.equals(getValue(), tag.getValue());
		}
		return false;
	}

	@Override
	protected boolean valueEquals(Tag other) {
		return this.getClass().equals(other.getClass()) && Array.equals(getValue(), other.getValue());
	}
}
