package net.querz.nbt;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.querz.nbt.NBTUtil.writeUTF;

/**
 * Base class for all NBT tags.
 * 
 * <h1>Nesting</h1>
 * <p>All methods serializing instances or deserializing data track the nesting levels to prevent 
 * circular references or malicious data which could, when deserialized, result in thousands 
 * of instances causing a denial of service.</p>
 * 
 * <p>These methods have a parameter for the maximum nesting depth they are allowed to traverse. A 
 * value of {@code 0} means that only the object itself, but no nested objects may be processed. 
 * If an instance is nested further than allowed, a {@link MaxDepthReachedException} will be thrown. 
 * Providing a negative maximum nesting depth will cause an {@code IllegalArgumentException} 
 * to be thrown.</p>
 * 
 * <p>Some methods do not provide a parameter to specify the maximum nesting depth, but instead use 
 * {@link #DEFAULT_MAX_DEPTH}, which is also the maximum used by Minecraft. This is documented for 
 * the respective methods.</p>
 * 
 * <p>If custom NBT tags contain objects other than NBT tags, which can be nested as well, then there 
 * is no guarantee that {@code MaxDepthReachedException}s are thrown for them. The respective class 
 * will document this behavior accordingly.</p>
 * 
 * @param <T> The type of the contained value
 * */
public abstract class Tag<T> implements Cloneable {

	/**
	 * The default maximum depth of the NBT structure.
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
	 * Initializes this Tag with some value. If the value is {@code null}, it will
	 * throw a {@code NullPointerException}
	 * @param value The value to be set for this Tag.
	 * */
	public Tag(T value) {
		setValue(value);
	}

	/**
	 * @return This Tag's ID, usually used for serialization and deserialization.
	 * */
	public byte getID() {
		return TagFactory.idFromClass(getClass());
	}

	/**
	 * @return The value of this Tag.
	 * */
	protected T getValue() {
		return value;
	}

	/**
	 * Sets the value for this Tag directly.
	 * @param value The value to be set.
	 * @throws NullPointerException If the value is null
	 * */
	protected void setValue(T value) {
		this.value = checkValue(value);
	}

	/**
	 * Checks if the value {@code value} is {@code null}.
	 * @param value The value to check
	 * @throws NullPointerException If {@code value} was {@code null}
	 * @return The parameter {@code value}
	 * */
	protected T checkValue(T value) {
		return Objects.requireNonNull(value);
	}

	/**
	 * Calls {@link Tag#serialize(DataOutput, String, int)} with an empty name.
	 * @see Tag#serialize(DataOutput, String, int)
	 * @param dos The DataOutputStream to write to
	 * @param maxDepth The maximum nesting depth
	 * @throws IOException If something went wrong during serialization.
	 * @throws NullPointerException If {@code dos} is {@code null}.
	 * @throws MaxDepthReachedException If the maximum nesting depth is exceeded.
	 * */
	public final void serialize(DataOutput dos, int maxDepth) throws IOException {
		serialize(dos, "", maxDepth);
	}

	/**
	 * Serializes this Tag starting at the gives depth.
	 * @param dos The DataOutputStream to write to.
	 * @param name The name of this Tag, if this is the root Tag.
	 * @param maxDepth The maximum nesting depth
	 * @throws IOException If something went wrong during serialization.
	 * @throws NullPointerException If {@code dos} or {@code name} is {@code null}.
	 * @throws MaxDepthReachedException If the maximum nesting depth is exceeded.
	 * */
	public final void serialize(DataOutput dos, String name, int maxDepth) throws IOException {
		dos.writeByte(getID());
		if (getID() != 0) {
			writeUTF(name, dos);
		}
		serializeValue(dos, maxDepth);
	}

	/**
	 * Deserializes this Tag starting at the given depth.
	 * The name of the root Tag is ignored.
	 * @param dis The DataInputStream to read from.
	 * @param maxDepth The maximum nesting depth.
	 * @throws IOException If something went wrong during deserialization.
	 * @throws NullPointerException If {@code dis} is {@code null}.
	 * @throws MaxDepthReachedException If the maximum nesting depth is exceeded.
	 * @return The deserialized NBT structure.
	 * */
	public static Tag<?> deserialize(DataInput dis, int maxDepth) throws IOException {
		int id = dis.readByte() & 0xFF;
		Tag<?> tag = TagFactory.fromID(id);
		if (id != 0) {
			DataInputStream.readUTF(dis);
			tag.deserializeValue(dis, maxDepth);
		}
		return tag;
	}

	/**
	 * Serializes only the value of this Tag.
	 * @param dos The DataOutputStream to write to.
	 * @param maxDepth The maximum nesting depth.
	 * @throws IOException If something went wrong during serialization.
	 * @throws MaxDepthReachedException If the maximum nesting depth is exceeded.
	 * */
	public abstract void serializeValue(DataOutput dos, int maxDepth) throws IOException;

	/**
	 * Deserializes only the value of this Tag.
	 * @param dis The DataInputStream to read from.
	 * @param maxDepth The maximum nesting depth.
	 * @throws IOException If something went wrong during deserialization.
	 * @throws MaxDepthReachedException If the maximum nesting depth is exceeded
	 * */
	public abstract void deserializeValue(DataInput dis, int maxDepth) throws IOException;

	/**
	 * Calls {@link Tag#toString(int)} with an initial depth of {@code 0}.
	 * @see Tag#toString(int)
	 * @throws MaxDepthReachedException If the maximum nesting depth is exceeded.
	 * */
	@Override
	public final String toString() {
		return toString(DEFAULT_MAX_DEPTH);
	}

	/**
	 * Creates a string representation of this Tag in a valid JSON format.
	 * @param maxDepth The maximum nesting depth.
	 * @return The string representation of this Tag.
	 * @throws MaxDepthReachedException If the maximum nesting depth is exceeded.
	 * */
	public String toString(int maxDepth) {
		return "{\"type\":\""+ getClass().getSimpleName() + "\"," +
				"\"value\":" + valueToString(maxDepth) + "}";
	}

	/**
	 * Returns a JSON representation of the value of this Tag.
	 * @param maxDepth The maximum nesting depth.
	 * @return The string representation of the value of this Tag.
	 * @throws MaxDepthReachedException If the maximum nesting depth is exceeded.
	 * */
	public abstract String valueToString(int maxDepth);

	/**
	 * Calls {@link Tag#toTagString(int)} with {@link #DEFAULT_MAX_DEPTH}.
	 * @see Tag#toTagString(int)
	 * @return The JSON-like string representation of this Tag.
	 * */
	public final String toTagString() {
		return toTagString(DEFAULT_MAX_DEPTH);
	}

	/**
	 * Returns a JSON-like representation of the value of this Tag, usually used for Minecraft commands.
	 * @param maxDepth The maximum nesting depth.
	 * @return The JSON-like string representation of this Tag.
	 * @throws MaxDepthReachedException If the maximum nesting depth is exceeded.
	 * */
	public String toTagString(int maxDepth) {
		return valueToTagString(maxDepth);
	}

	/**
	 * Returns a JSON-like representation of the value of this Tag.
	 * @param maxDepth The maximum nesting depth.
	 * @return The JSON-like string representation of the value of this Tag.
	 * @throws MaxDepthReachedException If the maximum nesting depth is exceeded.
	 * */
	public abstract String valueToTagString(int maxDepth);

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
	 * Decrements {@code maxDepth} by {@code 1}. This method has to be used when a tag is 
	 * (de-)serialized and contains nested tags. Their respective methods are then called 
	 * with {@code decrementMaxDepth(maxDepth)} as maximum nesting depth.
	 * 
	 * @param maxDepth The value to decrement.
	 * @return The decremented value.
	 * @throws MaxDepthReachedException If {@code maxDepth == 0}.
	 * @throws IllegalArgumentException If {@code maxDepth < 0}.
	 * */
	protected int decrementMaxDepth(int maxDepth) {
		if (maxDepth < 0) {
			throw new IllegalArgumentException("negative maximum depth is not allowed");
		} else if (maxDepth == 0) {
			throw new MaxDepthReachedException("reached maximum depth of NBT structure");
		}
		return --maxDepth;
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
