package net.querz.nbt.io;

import net.querz.io.MaxDepthIO;
import net.querz.nbt.tag.ArrayTag;
import net.querz.nbt.tag.ByteArrayTag;
import net.querz.nbt.tag.ByteTag;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.DoubleTag;
import net.querz.nbt.tag.EndTag;
import net.querz.nbt.tag.FloatTag;
import net.querz.nbt.tag.IntArrayTag;
import net.querz.nbt.tag.IntTag;
import net.querz.nbt.tag.ListTag;
import net.querz.nbt.tag.LongArrayTag;
import net.querz.nbt.tag.LongTag;
import net.querz.nbt.tag.ShortTag;
import net.querz.nbt.tag.StringTag;
import net.querz.nbt.tag.Tag;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class SNBTParser implements MaxDepthIO {

	private static final Pattern
			FLOAT_PATTERN = Pattern.compile("^[-+]?(?:\\d+\\.?|\\d*\\.\\d+)(?:e[-+]?\\d+)?f$", Pattern.CASE_INSENSITIVE),
			DOUBLE_PATTERN = Pattern.compile("^[-+]?(?:\\d+\\.?|\\d*\\.\\d+)(?:e[-+]?\\d+)?d$", Pattern.CASE_INSENSITIVE),
			DOUBLE_NO_SUFFIX_PATTERN = Pattern.compile("^[-+]?(?:\\d+\\.|\\d*\\.\\d+)(?:e[-+]?\\d+)?$", Pattern.CASE_INSENSITIVE),
			BYTE_PATTERN = Pattern.compile("^[-+]?\\d+b$", Pattern.CASE_INSENSITIVE),
			SHORT_PATTERN = Pattern.compile("^[-+]?\\d+s$", Pattern.CASE_INSENSITIVE),
			INT_PATTERN = Pattern.compile("^[-+]?\\d+$", Pattern.CASE_INSENSITIVE),
			LONG_PATTERN = Pattern.compile("^[-+]?\\d+l$", Pattern.CASE_INSENSITIVE);

	private StringPointer ptr;

	private SNBTParser(String string) {
		this.ptr = new StringPointer(string);
	}

	public static Tag<?> parse(String string, int maxDepth) throws ParseException {
		return new SNBTParser(string).parseAnything(maxDepth);
	}

	public static Tag<?> parse(String string) throws ParseException {
		return parse(string, Tag.DEFAULT_MAX_DEPTH);
	}

	private Tag<?> parseAnything(int maxDepth) throws ParseException {
		ptr.skipWhitespace();
		switch (ptr.currentChar()) {
			case '{':
				return parseCompoundTag(maxDepth);
			case '[':
				if (ptr.hasCharsLeft(2) && ptr.lookAhead(1) != '"' && ptr.lookAhead(2) == ';') {
					return parseNumArray();
				}
				return parseListTag(maxDepth);
		}
		return parseStringOrLiteral();
	}

	private Tag<?> parseStringOrLiteral() throws ParseException {
		ptr.skipWhitespace();
		if (ptr.currentChar() == '"') {
			return new StringTag(ptr.parseQuotedString());
		}
		String s = ptr.parseSimpleString();
		if (s.isEmpty()) {
			throw new ParseException("expected non empty value");
		}
		try {
			if (FLOAT_PATTERN.matcher(s).matches()) {
				return new FloatTag(Float.parseFloat(s.substring(0, s.length() - 1)));
			} else if (BYTE_PATTERN.matcher(s).matches()) {
				return new ByteTag(Byte.parseByte(s.substring(0, s.length() - 1)));
			} else if (SHORT_PATTERN.matcher(s).matches()) {
				return new ShortTag(Short.parseShort(s.substring(0, s.length() - 1)));
			} else if (LONG_PATTERN.matcher(s).matches()) {
				return new LongTag(Long.parseLong(s.substring(0, s.length() - 1)));
			} else if (INT_PATTERN.matcher(s).matches()) {
				return new IntTag(Integer.parseInt(s));
			} else if (DOUBLE_PATTERN.matcher(s).matches()) {
				return new DoubleTag(Double.parseDouble(s.substring(0, s.length() - 1)));
			} else if (DOUBLE_NO_SUFFIX_PATTERN.matcher(s).matches()) {
				return new DoubleTag(Double.parseDouble(s));
			} else if ("true".equalsIgnoreCase(s)) {
				return new ByteTag(true);
			} else if ("false".equalsIgnoreCase(s)) {
				return new ByteTag(false);
			}
		} catch (NumberFormatException ex) {
			return new StringTag(s);
		}
		return new StringTag(s);
	}

	private CompoundTag parseCompoundTag(int maxDepth) throws ParseException {
		ptr.expectChar('{');

		CompoundTag compoundTag = new CompoundTag();

		ptr.skipWhitespace();
		while (ptr.hasNext() && ptr.currentChar() != '}') {
			ptr.skipWhitespace();
			String key = ptr.currentChar() == '"' ? ptr.parseQuotedString() : ptr.parseSimpleString();
			if (key.isEmpty()) {
				throw new ParseException("empty keys are not allowed");
			}
			ptr.expectChar(':');

			compoundTag.put(key, parseAnything(decrementMaxDepth(maxDepth)));

			if (!ptr.nextArrayElement()) {
				break;
			}
		}
		ptr.expectChar('}');
		return compoundTag;
	}

	private ListTag<?> parseListTag(int maxDepth) throws ParseException {
		ptr.expectChar('[');
		ptr.skipWhitespace();
		ListTag<?> list = ListTag.createUnchecked(EndTag.class);
		while (ptr.currentChar() != ']') {
			Tag<?> element = parseAnything(decrementMaxDepth(maxDepth));
			list.addUnchecked(element);
			if (!ptr.nextArrayElement()) {
				break;
			}
		}
		ptr.expectChar(']');
		return list;
	}

	private ArrayTag<?> parseNumArray() throws ParseException {
		ptr.expectChar('[');
		char arrayType = ptr.next();
		ptr.expectChar(';');
		ptr.skipWhitespace();
		switch (arrayType) {
			case 'B':
				return parseByteArrayTag();
			case 'I':
				return parseIntArrayTag();
			case 'L':
				return parseLongArrayTag();
		}
		throw new ParseException("invalid array type '" + arrayType + "'");
	}

	private ByteArrayTag parseByteArrayTag() throws ParseException {
		List<Byte> byteList = new ArrayList<>();
		while (ptr.currentChar() != ']') {
			String s = ptr.parseSimpleString();
			ptr.skipWhitespace();
			if (BYTE_PATTERN.matcher(s).matches()) {
				byteList.add(Byte.parseByte(s.substring(0, s.length() - 1)));
			} else {
				throw ptr.parseException("invalid byte literal in ByteArrayTag: \"" + s + "\"");
			}
			if (!ptr.nextArrayElement()) {
				break;
			}
		}
		ptr.expectChar(']');
		byte[] bytes = new byte[byteList.size()];
		for (int i = 0; i < byteList.size(); i++) {
			bytes[i] = byteList.get(i);
		}
		return new ByteArrayTag(bytes);
	}

	private IntArrayTag parseIntArrayTag() throws ParseException {
		List<Integer> intList = new ArrayList<>();
		while (ptr.currentChar() != ']') {
			String s = ptr.parseSimpleString();
			ptr.skipWhitespace();
			if (INT_PATTERN.matcher(s).matches()) {
				intList.add(Integer.parseInt(s.substring(0, s.length() - 1)));
			} else {
				throw ptr.parseException("invalid int literal in IntArrayTag: \"" + s + "\"");
			}
			if (!ptr.nextArrayElement()) {
				break;
			}
		}
		ptr.expectChar(']');
		return new IntArrayTag(intList.stream().mapToInt(i -> i).toArray());
	}

	private LongArrayTag parseLongArrayTag() throws ParseException {
		List<Long> longList = new ArrayList<>();
		while (ptr.currentChar() != ']') {
			String s = ptr.parseSimpleString();
			ptr.skipWhitespace();
			if (LONG_PATTERN.matcher(s).matches()) {
				longList.add(Long.parseLong(s.substring(0, s.length() - 1)));
			} else {
				throw ptr.parseException("invalid long literal in LongArrayTag: \"" + s + "\"");
			}
			if (!ptr.nextArrayElement()) {
				break;
			}
		}
		ptr.expectChar(']');
		return new LongArrayTag(longList.stream().mapToLong(l -> l).toArray());
	}
}
