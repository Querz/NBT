package net.querz.nbt;

public class NBTUtil {
	public static Number toNumber(Tag tag) {
		if (tag != null && tag instanceof NumberTag)
			return (Number) tag.getValue();
		return new Byte((byte) 0);
	}
	
	/**
	 * checks if tag is a Number and interprets it as a boolean.
	 * @param tag
	 * @return true if the Number is > 0
	 */
	public static boolean toBoolean(Tag tag) {
		return toNumber(tag).byteValue() > 0;
	}
	
	public static String joinObjects(String delimiter, Object... o) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		for (Object obj : o) {
			sb.append((first ? "" : delimiter) + obj);
			first = false;
		}
		return sb.toString();
	}
	
	public static String joinBytes(String delimiter, byte... b) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		for (byte bt : b) {
			sb.append((first ? "" : delimiter) + bt);
			first = false;
		}
		return sb.toString();
	}
	
	public static String joinShorts(String delimiter, short... s) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		for (short st : s) {
			sb.append((first ? "" : delimiter) + st);
			first = false;
		}
		return sb.toString();
	}
	
	public static String joinInts(String delimiter, int... i) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		for (int it : i) {
			sb.append((first ? "" : delimiter) + it);
			first = false;
		}
		return sb.toString();
	}
	
	public static String joinTagString(String delimiter, Object... o) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		for (Object obj : o) {
			sb.append((first ? "" : delimiter) + ((Tag) obj).toTagString());
			first = false;
		}
		return sb.toString();
	}
	
	public static String createNamePrefix(Tag tag) {
		return tag.getName().equals("") ? "" : (tag.getName() + ":");
	}
}
