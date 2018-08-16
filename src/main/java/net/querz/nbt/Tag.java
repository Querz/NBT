package net.querz.nbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Tag<T> implements Comparable<Tag<T>>, Cloneable {

	public static final Charset CHARSET = Charset.forName("UTF-8");
	public static final int MAX_DEPTH = 512;
	private static final Map<String, String> ESCAPE_CHARACTERS = new HashMap<String, String>() {{
		put("\\", "\\\\\\\\");
		put("\n", "\\\\n");
		put("\t", "\\\\t");
		put("\r", "\\\\r");
		put("\"", "\\\\\"");
	}};

	private static final Pattern ESCAPE_PATTERN = Pattern.compile("[\\\\\n\t\r\"]");
	private static final Pattern NON_QUOTE_PATTERN = Pattern.compile("[a-zA-Z0-9_\\-+]+");

	private T value;

	public Tag() {
		this(null);
	}

	public Tag(T value) {
		//the value of a tag can never be null
		this.value = value == null ? getEmptyValue() : value;
	}

	protected <V> V checkNull(V v) {
		if (v == null) {
			throw new NullPointerException(getClass().getSimpleName() + " does not allow setting null");
		}
		return v;
	}

	protected T getValue() {
		return value;
	}

	protected void setValue(T value) {
		this.value = value;
	}

	public final void serialize(DataOutputStream dos, int depth) throws IOException {
		dos.writeByte(getID());
		if (getID() != 0) {
			dos.writeUTF("");
		}
		serializeValue(dos, depth);
	}

	public static Tag deserialize(DataInputStream dis, int depth) throws IOException {
		int id = dis.readByte() & 0xFF;
		Tag tag = TagFactory.fromID(id);
		if (id != 0) {
			dis.readUTF();
			tag.deserializeValue(dis, depth);
		}
		return tag;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object other) {
		return other.getClass() == getClass() && valueEquals((T) ((Tag) other).getValue());
	}

	@Override
	public String toString() {
		return toString(0);
	}

	protected String toString(int depth) {
		return "{\"type\":\""+ getClass().getSimpleName() + "\"," +
				"\"value\":" + valueToString(depth) + "}";
	}

	public String toTagString() {
		return toTagString(0);
	}

	protected String toTagString(int depth) {
		return valueToTagString(depth);
	}

	protected int incrementDepth(int depth) {
		if (depth >= MAX_DEPTH) {
			throw new MaxDepthReachedException("reached maximum depth (" + Tag.MAX_DEPTH + ") of NBT structure");
		}
		if (depth < 0) {
			throw new IllegalArgumentException("initial depth cannot be negative");
		}
		return ++depth;
	}


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

	public abstract byte getID();
	public abstract void serializeValue(DataOutputStream dos, int depth) throws IOException;
	public abstract void deserializeValue(DataInputStream dis, int depth) throws IOException;
	public abstract String valueToTagString(int depth);
	public abstract String valueToString(int depth);
	public abstract boolean valueEquals(T value);
	protected abstract T getEmptyValue();
	protected abstract Tag clone();
}
