package net.querz.nbt.util;

import net.querz.nbt.NumberTag;
import net.querz.nbt.Tag;

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
	
	public static String joinTagString(String delimiter, Object... o) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		for (Object obj : o) {
			sb.append((first ? "" : delimiter) + ((Tag) obj).toTagString());
			first = false;
		}
		return sb.toString();
	}
	
	public static String joinArray(String delimiter, Object array) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < Array.getLength(array); i++) {
			sb.append((first ? "" : delimiter) + Array.get(array, i));
			first = false;
		}
		return sb.toString();
	}
	
	public static String createNamePrefix(Tag tag) {
		return tag.getName().equals("") ? "" : (tag.getName() + ":");
	}
}
