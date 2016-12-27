package de.querz.nbt;

public abstract class CustomTag extends Tag {
	private int id;
	
	public CustomTag(int id, String name) {
		super(TagType.CUSTOM, name);
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
}
