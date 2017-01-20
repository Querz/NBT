package net.querz.nbt;

public abstract class NumberTag<T extends Number> extends Tag {

	protected NumberTag(TagType type, String name) {
		super(type, name);
	}
	
	protected NumberTag(TagType type) {
		super(type);
	}
	
	protected NumberTag() {
		super();
	}
}
