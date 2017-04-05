package net.querz.nbt;

/**
 * An abstract custom tag. A custom tag is identified by its id. Keep in mind that {@code TagType} returns
 * {@code TagType.CUSTOM} for ALL custom tags, therefore implementations that handle custom tags specifically
 * (serialization, deserialization, etc.) have to use the id specifically.
 */
public interface CustomTag {
	int getId();
}
