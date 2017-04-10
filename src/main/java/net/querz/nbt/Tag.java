package net.querz.nbt;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * This is an abstract representation of an NBT Tag.
 * An NBT Tag always consists of a name, a type and a value, of which the name and the type are stored
 * in this class.
 */
public abstract class Tag implements Comparable<Tag>, Cloneable {

	/**
	 * Strings in the NBT specification are always UTF-8 encoded.
	 */
	public static final Charset CHARSET = Charset.forName("UTF-8");

	/**
	 * An NBT structure must have a maximum depth of 512.
	 */
	public static final int MAX_DEPTH = 512;
	
	private TagType type;
	private String name;
	
	protected Tag(TagType type, String name) {
		this.type = type;
		setName(name);
	}
	
	protected Tag(TagType type) {
		this(type, "");
	}
	
	protected Tag() {
		this(TagType.END, null);
	}
	
	public TagType getType() {
		return type;
	}
	
	public String getName() {
		return name;
	}
	
	public final void setName(String name) {
		this.name = name == null ? "" : name;
	}
	
	public final void serializeTag(NBTOutputStream nbtOut) throws Exception {
		serializeTag(nbtOut, 0);
	}
	
	public final void serializeTag(NBTOutputStream nbtOut, int depth) throws Exception {
		nbtOut.dos.writeByte(type.getId(this));
		if (type != TagType.END) {
			byte[] nameBytes = name.getBytes(CHARSET);
			nbtOut.dos.writeShort(nameBytes.length);
			nbtOut.dos.write(nameBytes);
		}
		serialize(nbtOut, depth);
	}
	
	public static Tag deserializeTag(NBTInputStream nbtIn) throws Exception {
		return deserializeTag(nbtIn, 0);
	}
	
	public static Tag deserializeTag(NBTInputStream nbtIn, int depth) throws Exception {
		int typeId = nbtIn.dis.readByte() & 0xFF;
		Tag tag = TagType.getTag(typeId);
		if (tag.getType() != TagType.END) {
			int nameLength = nbtIn.dis.readShort() & 0xFFFF;
			byte[] nameBytes = new byte[nameLength];
			nbtIn.dis.readFully(nameBytes);
			tag.setName(new String(nameBytes, CHARSET));
		}
		tag.deserialize(nbtIn, depth);
		return tag;
	}

	/**
	 * A dummy {@code toString()} method accepting a depth value.<br>
	 * Depth calculation should be done with {@link Tag#incrementDepth(int)}.
	 *
	 * @param depth The current depth of the NBT structure
	 * @return A {@link String} representation of the current tag
	 */
	protected String toString(int depth) {
		return toString();
	}

	/**
	 * A {@link Tag#toTagString()} method accepting a depth value.<br>
	 * Depth calculation should be done with {@link Tag#incrementDepth(int)}.
	 *
	 * @param depth The current depth of the NBT structure
	 * @return A json-like {@link String} representation of the current tag, usable
	 * in Minecraft commands
	 */
	protected String toTagString(int depth) {
		return toTagString();
	}

	/**
	 * Compares this tags's name and value with another tag's name and value.<br>
	 * Should be overridden in implementations with complex values.
	 *
	 * @param other The object to compare to
	 * @return true if they are equals under the conditions described above
	 */
	@Override
	public boolean equals(Object other) {
		if (other == null || !this.getClass().equals(other.getClass())) {
			return false;
		}
		Tag tag = (Tag) other;
		return valueEquals(tag) && getName().equals(tag.getName());
	}

	/**
	 * Compares the value of {@code other} to this tag's value.
	 * Should be overridden in implementations with complex values.
	 *
	 * @param other The tag to compare the value to
	 * @return true if the values are equal
	 */
	protected boolean valueEquals(Tag other) {
		return other.getValue().equals(getValue());
	}

	/**
	 * Compares this tag's name with another tag's name, because the name is the only thing that is
	 * consistent throughout all tags.
	 *
	 * @param other The tag to compare to
	 * @return See {@link java.lang.String#compareTo(String)}
	 */
	@Override
	public int compareTo(Tag other) {
		return getName().compareTo(other.getName());
	}

	/**
	 * Increments {@code depth} by one.
	 *
	 * @param depth The initial depth given to this method
	 * @return returns the incremented depth
	 * @throws MaxDepthReachedException if {@link Tag#MAX_DEPTH} is reached
	 * @throws IllegalArgumentException if {@code depth < 0}
	 */
	protected static int incrementDepth(int depth) {
		if (depth >= MAX_DEPTH)
			throw new MaxDepthReachedException();
		if (depth < 0)
			throw new IllegalArgumentException("Initial depth cannot be negative.");
		return ++depth;
	}

	protected abstract String valueToTagString(int depth);
	protected abstract void serialize(NBTOutputStream nbtOut, int depth) throws Exception;
	protected abstract Tag deserialize(NBTInputStream nbtIn, int depth) throws Exception;
	public abstract Object getValue();
	public abstract String toString();
	public abstract String toTagString();
	public abstract Tag clone();
}
