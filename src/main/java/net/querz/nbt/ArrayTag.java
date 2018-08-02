package net.querz.nbt;

import net.querz.nbt.util.Array;

public abstract class ArrayTag extends Tag {

	private String typeIdentifier;
	private String typeSuffix;

	public ArrayTag(TagType type, String name, String typeIdentifier) {
		this(type, name, typeIdentifier, "");
	}

	public ArrayTag(TagType type, String name, String typeIdentifier, String typeSuffix) {
		super(type, name);
		this.typeIdentifier = typeIdentifier;
		this.typeSuffix = typeSuffix;
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
		return "[" + typeIdentifier + ";" + NBTUtil.joinArray(",", getValue(), typeSuffix) + "]";
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
