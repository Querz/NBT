package net.querz.nbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Tag<T> implements Comparable<Tag<T>>, Cloneable {

	/**
	 * The maximum depth of the NBT structure.
	 * */
	public static final int DEFAULT_MAX_DEPTH = 512;

	private static final Map<String, String> ESCAPE_CHARACTERS;
	static {
		final Map<String, String> temp = new HashMap<>();
		temp.put("\\", "\\\\\\\\");
		temp.put("\n", "\\\\n");
		temp.put("\t", "\\\\t");
		temp.put("\r", "\\\\r");
		temp.put("\"", "\\\\\"");
		ESCAPE_CHARACTERS = Collections.unmodifiableMap(temp);
	}

	private static final Pattern ESCAPE_PATTERN = Pattern.compile("[\\\\\n\t\r\"]");
	private static final Pattern NON_QUOTE_PATTERN = Pattern.compile("[a-zA-Z0-9_\\-+]+");

	private T value;

	/**
	 * Initializes this Tag with a {@code null} value.
	 */
	public Tag() {
		this(null);
	}

	/**
	 * Initializes this Tag with some value. If the value is {@code null}, it will call
	 * this Tag's implementation of {@link Tag#getEmptyValue()} to set as the default value.
	 * @param value The value to be set for this Tag.
	 * */
	public Tag(T value) {
		//the value of a tag can never be null
		this.value = value == null ? getEmptyValue() : value;
	}

	/**
	 * @return This Tag's ID, usually used for serialization and deserialization.
	 * */
	public byte getID() {
		return TagFactory.idFromClass(getClass());
	}

	/**
	 * @return A default value for this Tag.
	 * */
	protected abstract T getEmptyValue();

	/**
	 * @return The value of this Tag.
	 * */
	protected T getValue() {
		return value;
	}

	/**
	 * Sets the value for this Tag directly.
	 * @param value The value to be set.
	 * */
	protected void setValue(T value) {
		this.value = value;
	}

	/**
	 * Calls {@link Tag#serialize(DataOutputStream, String, int)} with an empty name.
	 * @see Tag#serialize(DataOutputStream, String, int)
	 * @param dos The DataOutputStream to write to
	 * @param depth The current depth of the structure
	 * @throws IOException If something went wrong during serialization
	 * */
	public final void serialize(DataOutputStream dos, int depth) throws IOException {
		serialize(dos, "", depth);
	}

	/**
	 * Serializes this Tag starting at the gives depth.
	 * @param dos The DataOutputStream to write to.
	 * @param name The name of this Tag, if this is the root Tag.
	 * @param depth The current depth of the structure.
	 * @throws IOException If something went wrong during serialization.
	 * @exception NullPointerException If {@code dos} or {@code name} is {@code null}.
	 * @exception MaxDepthReachedException If the structure depth exceeds {@link Tag#DEFAULT_MAX_DEPTH}.
	 * */
	public final void serialize(DataOutputStream dos, String name, int depth) throws IOException {
		dos.writeByte(getID());
		if (getID() != 0) {
			dos.writeUTF(name);
		}
		serializeValue(dos, depth);
	}

	/**
	 * Deserializes this Tag starting at the given depth.
	 * The name of the root Tag is ignored.
	 * @param dis The DataInputStream to read from.
	 * @param depth The current depth of the structure.
	 * @throws IOException If something went wrong during deserialization.
	 * @exception NullPointerException If {@code dis} is {@code null}.
	 * @exception MaxDepthReachedException If the structure depth exceeds {@link Tag#DEFAULT_MAX_DEPTH}.
	 * @return The deserialized NBT structure.
	 * */
	public static Tag<?> deserialize(DataInputStream dis, int depth) throws IOException {
		int id = dis.readByte() & 0xFF;
		Tag<?> tag = TagFactory.fromID(id);
		if (id != 0) {
			dis.readUTF();
			tag.deserializeValue(dis, depth);
		}
		return tag;
	}

	/**
	 * Serializes only the value of this Tag.
	 * @param dos The DataOutputStream to write to.
	 * @param depth The current depth of the structure.
	 * @throws IOException If something went wrong during serialization.
	 * @exception MaxDepthReachedException If the structure depth exceeds {@link Tag#DEFAULT_MAX_DEPTH}.
	 * */
	public abstract void serializeValue(DataOutputStream dos, int depth) throws IOException;

	/**
	 * Deserializes only the value of this Tag.
	 * @param dis The DataInputStream to read from.
	 * @param depth The current depth of the structure.
	 * @throws IOException If something went wrong during deserialization.
	 * @exception MaxDepthReachedException If the structure depth exceeds {@link Tag#DEFAULT_MAX_DEPTH}.
	 * */
	public abstract void deserializeValue(DataInputStream dis, int depth) throws IOException;

	/**
	 * Calls {@link Tag#toString(int)} with an initial depth of {@code 0}.
	 * @see Tag#toString(int)
	 * */
	@Override
	public final String toString() {
		return toString(DEFAULT_MAX_DEPTH);
	}

	/**
	 * Creates a string representation of this Tag in a valid JSON format.
	 * @param depth The current depth of the structure.
	 * @return The string representation of this Tag.
	 * @exception MaxDepthReachedException If the structure depth exceeds {@link Tag#DEFAULT_MAX_DEPTH}.
	 * */
	public String toString(int depth) {
		return "{\"type\":\""+ getClass().getSimpleName() + "\"," +
				"\"value\":" + valueToString(depth) + "}";
	}

	/**
	 * Returns a JSON representation of the value of this Tag.
	 * @param depth The current depth of the structure.
	 * @return The string representation of the value of this Tag.
	 * @exception MaxDepthReachedException If the structure depth exceeds {@link Tag#DEFAULT_MAX_DEPTH}.
	 * */
	public abstract String valueToString(int depth);

	/**
	 * Calls {@link Tag#toTagString(int)} with an initial depth of {@code 0}.
	 * @see Tag#toTagString(int)
	 * @return The JSON-like string representation of this Tag.
	 * */
	public final String toTagString() {
		return toTagString(DEFAULT_MAX_DEPTH);
	}

	/**
	 * Returns a JSON-like representation of the value of this Tag, usually used for Minecraft commands.
	 * @param depth The current depth of the structure.
	 * @return The JSON-like string representation of this Tag.
	 * @exception MaxDepthReachedException If the structure depth exceeds {@link Tag#DEFAULT_MAX_DEPTH}.
	 * */
	public String toTagString(int depth) {
		return valueToTagString(depth);
	}

	/**
	 * Returns a JSON-like representation of the value of this Tag.
	 * @param depth The current depth of the structure.
	 * @return The JSON-like string representation of the value of this Tag.
	 * @exception MaxDepthReachedException If the structure depth exceeds {@link Tag#DEFAULT_MAX_DEPTH}.
	 * */
	public abstract String valueToTagString(int depth);

	/**
	 * Returns whether this Tag and some other Tag are equal.
	 * They are equal if {@code other} is not {@code null} and they are of the same class.
	 * Custom Tag implementations should overwrite this but check the result
	 * of this {@code super}-method while comparing.
	 * @param other The Tag to compare to.
	 * @return {@code true} if they are equal based on the conditions mentioned above.
	 * */
	@Override
	public boolean equals(Object other) {
		return other != null && getClass() == other.getClass();
	}

	/**
	 * Calculates the hash code of this Tag. Tags which are equal according to {@link Tag#equals(Object)}
	 * must return an equal hash code.
	 * @return The hash code of this Tag.
	 * */
	@Override
	public int hashCode() {
		return value.hashCode();
	}

	/**
	 * Creates a clone of this Tag.
	 * @return A clone of this Tag.
	 * */
	@SuppressWarnings("CloneDoesntDeclareCloneNotSupportedException")
	public abstract Tag<T> clone();

	/**
	 * A utility method to check if some value is {@code null}.
	 * @param <V> Any type that should be checked for being {@code null}
	 * @param v The value to check.
	 * @return {@code v}, if it's not {@code null}.
	 * @exception NullPointerException If {@code v} is {@code null}.
	 * */
	protected <V> V checkNull(V v) {
		if (v == null) {
			throw new NullPointerException(getClass().getSimpleName() + " does not allow setting null");
		}
		return v;
	}

	/**
	 * Decrements {@code depth} by {@code 1}.
	 * @param depth The value to decrement.
	 * @return The decremented value.
	 * @exception MaxDepthReachedException If {@code depth <= 0}.
	 * */
	protected int decrementDepth(int depth) {
		if (depth <= 0) {
			throw new MaxDepthReachedException("reached maximum depth of NBT structure");
		}
		return --depth;
	}

	/**
	 * Escapes a string to fit into a JSON-like string representation for Minecraft
	 * or to create the JSON string representation of a Tag returned from {@link Tag#toString()}
	 * @param s The string to be escaped.
	 * @param lenient {@code true} if it should force double quotes ({@code "}) at the start and
	 *                the end of the string.
	 * @return The escaped string.
	 * */
	protected static String escapeString(String s, boolean lenient) {
		StringBuffer sb = new StringBuffer();
		Matcher m = ESCAPE_PATTERN.matcher(s);
		while (m.find()) {
			m.appendReplacement(sb, ESCAPE_CHARACTERS.get(m.group()));
		}
		m.appendTail(sb);
		m = NON_QUOTE_PATTERN.matcher(s);
		if (!lenient || !m.matches()) {
			sb.insert(0, "\"").append("\"");
		}
		return sb.toString();
	}
}
