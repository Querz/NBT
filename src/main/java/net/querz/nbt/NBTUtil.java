package net.querz.nbt;

import net.querz.nbt.util.Array;

public class NBTUtil {
	public static Number toNumber(Tag tag) {
		if (tag != null && tag instanceof NumberTag)
			return (Number) tag.getValue();
		return 0;
	}
	
	/**
	 * checks if tag is a Number and interprets it as a boolean.
	 * @param tag the Tag instance to be turned into a boolean
	 * @return true if the Number is > 0
	 */
	public static boolean toBoolean(Tag tag) {
		return toNumber(tag).byteValue() > 0;
	}
	
	public static String joinTagString(String delimiter, Object array) {
		return joinTagString(delimiter, array, 0);
	}
	
	public static String joinTagString(String delimiter, Object array, int depth) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < Array.getLength(array); i++) {
			sb.append(first ? "" : delimiter).append(((Tag) Array.get(array, i)).toTagString(depth));
			first = false;
		}
		return sb.toString();
	}
	
	public static String joinArray(String delimiter, Object array) {
		return NBTUtil.joinArray(delimiter, array, 0);
	}
	
	public static String joinArray(String delimiter, Object array, int depth) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < Array.getLength(array); i++) {
			if (Array.get(array, i) instanceof Tag) {
				sb.append(first ? "" : delimiter).append(((Tag) Array.get(array, i)).toString(depth));
			} else {
				sb.append(first ? "" : delimiter).append(Array.get(array, i));
			}
			first = false;
		}
		return sb.toString();
	}
	
	public static String createNamePrefix(Tag tag) {
		return tag.getName().equals("") ? "" : (tag.getName() + ":");
	}
}
