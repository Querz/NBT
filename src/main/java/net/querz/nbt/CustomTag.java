package net.querz.nbt;

/**
 * An abstract custom tag. A custom tag is identified by its id. Keep in mind that {@code TagType} returns
 * {@code TagType.CUSTOM} for ALL custom tags, therefore implementations that handle custom tags specifically
 * (serialization, deserialization, etc.) have to use the id specifically.
 */
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
