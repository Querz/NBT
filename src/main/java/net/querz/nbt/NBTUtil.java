package net.querz.nbt;

import net.querz.nbt.util.Array;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NBTUtil {

    public static Pattern noNeedToEscapePattern = Pattern.compile("[a-zA-Z0-9_\\-+]*");

	/**
	 * gets the {@code Number} value of any tag
	 * @param tag The tag to get the {@code Number} value from
	 * @return the value of {@code tag} or 0 if {@code} tag is not an instance of {@code NumberTag}
	 */
	public static Number toNumber(Tag tag) {
		if (tag != null && tag instanceof NumberTag)
			return (Number) tag.getValue();
		return 0;
	}

	/**
	 * checks if tag is a {@code Number} and interprets it as a boolean.
	 * @param tag the Tag instance to be turned into a boolean
	 * @return true if the Number is &gt; 0
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
		return NBTUtil.joinArray(delimiter, array, "", 0, true);
	}

	public static String joinArray(String delimiter, Object array, int depth) {
		return NBTUtil.joinArray(delimiter, array, "", depth, true);
	}

	public static String joinArray(String delimiter, Object array, String typeSuffix) {
		return NBTUtil.joinArray(delimiter, array, typeSuffix, 0, true);
	}

	public static String joinArray(String delimiter, Object array, String typeSuffix, int depth, boolean useToTagString) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < Array.getLength(array); i++) {
			if (Array.get(array, i) instanceof Tag) {
				if (useToTagString) {
					sb.append(first ? "" : delimiter).append(((Tag) Array.get(array, i)).toTagString(depth));
				} else {
					sb.append(first ? "" : delimiter).append(((Tag) Array.get(array, i)).toString(depth));
				}
			} else {
				sb.append(first ? "" : delimiter).append(Array.get(array, i)).append(typeSuffix);
			}
			first = false;
		}
		return sb.toString();
	}

	public static String createNamePrefix(Tag tag) {
		return tag.getName().equals("") ? "" : (createPossiblyEscapedString(tag.getName()) + ":");
	}

	public static String createPossiblyEscapedString(String input) {
		Matcher matcher = noNeedToEscapePattern.matcher(input);

		if (matcher.matches()) {
			return input;
		}

		return "\"" + input
				.replace("\\", "\\\\")
				.replace("\"", "\\\"")
				.replace("\n", "\\n") + "\"";
	}
}
