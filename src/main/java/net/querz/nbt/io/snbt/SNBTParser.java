package net.querz.nbt.io.snbt;

import net.querz.nbt.ByteArrayTag;
import net.querz.nbt.ByteTag;
import net.querz.nbt.CollectionTag;
import net.querz.nbt.CompoundTag;
import net.querz.nbt.DoubleTag;
import net.querz.nbt.FloatTag;
import net.querz.nbt.IntArrayTag;
import net.querz.nbt.IntTag;
import net.querz.nbt.ListTag;
import net.querz.nbt.LongArrayTag;
import net.querz.nbt.LongTag;
import net.querz.nbt.ShortTag;
import net.querz.nbt.StringTag;
import net.querz.nbt.Tag;
import net.querz.nbt.TagType;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SNBTParser {

	private static final Pattern FLOAT_LITERAL = Pattern.compile("^[-+]?(?:\\d+\\.?|\\d*\\.\\d+)(?:e[-+]?\\d+)?f$", Pattern.CASE_INSENSITIVE);
	private static final Pattern DOUBLE_LITERAL = Pattern.compile("^[-+]?(?:\\d+\\.?|\\d*\\.\\d+)(?:e[-+]?\\d+)?d$", Pattern.CASE_INSENSITIVE);
	private static final Pattern DOUBLE_LITERAL_NO_SUFFIX = Pattern.compile("^[-+]?(?:\\d+\\.|\\d*\\.\\d+)(?:e[-+]?\\d+)?$", Pattern.CASE_INSENSITIVE);
	private static final Pattern BYTE_LITERAL = Pattern.compile("^[-+]?\\d+b$", Pattern.CASE_INSENSITIVE);
	private static final Pattern SHORT_LITERAL = Pattern.compile("^[-+]?\\d+s$", Pattern.CASE_INSENSITIVE);
	private static final Pattern INT_LITERAL = Pattern.compile("^[-+]?\\d+$", Pattern.CASE_INSENSITIVE);
	private static final Pattern LONG_LITERAL = Pattern.compile("^[-+]?\\d+l$", Pattern.CASE_INSENSITIVE);

	private final StringPointer ptr;

	public SNBTParser(String s) {
		ptr = new StringPointer(s);
	}

	public Tag parse() throws ParseException {
		return parse(false);
	}

	public Tag parse(boolean ignoreTrailing) throws ParseException {
		if (ignoreTrailing) {
			return readValue();
		} else {
			Tag result = readValue();
			ptr.skipWhitespace();
			if (ptr.hasNext()) {
				throw ptr.parseException("trailing non-whitespace characters found");
			}
			return result;
		}
	}

	private CompoundTag readCompoundTag() throws ParseException {
		ptr.skipWhitespace();
		ptr.expectChar('{');
		CompoundTag tag = new CompoundTag();
		ptr.skipWhitespace();
		while (ptr.hasNext() && ptr.currentChar() != '}') {
			int start = ptr.getIndex();
			ptr.skipWhitespace();
			String key = ptr.parseString();
			if (key.isEmpty()) {
				ptr.setIndex(start);
				throw ptr.parseException("expected key");
			}

			ptr.expectChar(':');
			tag.put(key, readValue());
			if (!hasSeparator()) {
				break;
			}
		}

		ptr.expectChar('}');
		return tag;
	}

	private CollectionTag<?> readArrayTag() throws ParseException {
		ptr.skipWhitespace();
		ptr.expectChar('[');
		int start = ptr.getIndex();
		char type = ptr.next();
		ptr.skip(1);
		ptr.skipWhitespace();
		if (type == 'B') {
			return readByteArrayTag();
		} else if (type == 'I') {
			return readIntArrayTag();
		} else if (type == 'L') {
			return readLongArrayTag();
		} else {
			ptr.setIndex(start);
			throw ptr.parseException("invalid array type " + type);
		}
	}

	private ByteArrayTag readByteArrayTag() throws ParseException {
		List<Byte> byteList = new ArrayList<>();
		while (ptr.currentChar() != ']') {
			String s = ptr.parseSimpleString();
			if (BYTE_LITERAL.matcher(s).matches()) {
				try {
					byteList.add(Byte.parseByte(s.substring(0, s.length() - 1)));
				} catch (NumberFormatException ex) {
					throw ptr.parseException("byte value " + s + " not in range");
				}
			} else {
				throw ptr.parseException("invalid byte value " + s);
			}

			if (!hasSeparator()) {
				break;
			}
		}
		ptr.expectChar(']');
		byte[] array = new byte[byteList.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = byteList.get(i);
		}
		return new ByteArrayTag(array);
	}

	private IntArrayTag readIntArrayTag() throws ParseException {
		List<Integer> intList = new ArrayList<>();
		while (ptr.currentChar() != ']') {
			String s = ptr.parseSimpleString();
			if (INT_LITERAL.matcher(s).matches()) {
				try {
					intList.add(Integer.parseInt(s));
				} catch (NumberFormatException ex) {
					throw ptr.parseException("int value " + s + " not in range");
				}
			} else {
				throw ptr.parseException("invalid int value " + s);
			}

			if (!hasSeparator()) {
				break;
			}
		}
		ptr.expectChar(']');
		int[] array = new int[intList.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = intList.get(i);
		}
		return new IntArrayTag(array);
	}

	private LongArrayTag readLongArrayTag() throws ParseException {
		List<Long> longList = new ArrayList<>();
		while (ptr.currentChar() != ']') {
			String s = ptr.parseSimpleString();
			if (LONG_LITERAL.matcher(s).matches()) {
				try {
					longList.add(Long.parseLong(s.substring(0, s.length() - 1)));
				} catch (NumberFormatException ex) {
					throw ptr.parseException("long value " + s + " not in range");
				}
			} else {
				throw ptr.parseException("invalid long value " + s);
			}

			if (!hasSeparator()) {
				break;
			}
		}
		ptr.expectChar(']');
		long[] array = new long[longList.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = longList.get(i);
		}
		return new LongArrayTag(array);
	}

	private CollectionTag<?> readCollectionTag() throws ParseException {
		ptr.skipWhitespace();
		if (ptr.hasCharsLeft(3) && ptr.lookAhead(1) != '"' && ptr.lookAhead(1) != '\'' && ptr.lookAhead(2) == ';') {
			return readArrayTag();
		} else {
			return readListTag();
		}
	}

	private ListTag readListTag() throws ParseException {
		ptr.expectChar('[');
		ListTag tag = new ListTag();
		ptr.skipWhitespace();
		TagType<?> type = null;
		while (ptr.hasNext() && ptr.currentChar() != ']') {
			int start = ptr.getIndex();
			Tag t = readValue();
			if (type == null) {
				type = t.getType();
			} else if (type != t.getType()) {
				ptr.setIndex(start);
				throw ptr.parseException("mixed types in ListTag");
			}

			tag.add(t);
			if (!hasSeparator()) {
				break;
			}
		}
		ptr.expectChar(']');
		return tag;
	}

	private Tag readValue() throws ParseException {
		ptr.skipWhitespace();
		char t = ptr.currentChar();
		if (t == '{') {
			return readCompoundTag();
		} else if (t == '[') {
			return readCollectionTag();
		} else if (ptr.currentChar() == '"' || ptr.currentChar() == '\'') {
			return StringTag.valueOf(ptr.parseQuotedString());
		} else {
			String value = ptr.parseSimpleString();
			try {
				if (BYTE_LITERAL.matcher(value).matches()) {
					return ByteTag.valueOf(Byte.parseByte(value.substring(0, value.length() - 1)));
				}
				if (SHORT_LITERAL.matcher(value).matches()) {
					return ShortTag.valueOf(Short.parseShort(value.substring(0, value.length() - 1)));
				}
				if (INT_LITERAL.matcher(value).matches()) {
					return IntTag.valueOf(Integer.parseInt(value));
				}
				if (LONG_LITERAL.matcher(value).matches()) {
					return LongTag.valueOf(Long.parseLong(value.substring(0, value.length() - 1)));
				}
				if (FLOAT_LITERAL.matcher(value).matches()) {
					return FloatTag.valueOf(Float.parseFloat(value.substring(0, value.length() - 1)));
				}
				if (DOUBLE_LITERAL.matcher(value).matches()) {
					return DoubleTag.valueOf(Double.parseDouble(value.substring(0, value.length() - 1)));
				}
				if (DOUBLE_LITERAL_NO_SUFFIX.matcher(value).matches()) {
					return DoubleTag.valueOf(Double.parseDouble(value));
				}
				if ("true".equalsIgnoreCase(value)) {
					return ByteTag.TRUE;
				}
				if ("false".equalsIgnoreCase(value)) {
					return ByteTag.FALSE;
				}
			} catch (NumberFormatException ex) {}

			return StringTag.valueOf(value);
		}
	}

	private boolean hasSeparator() {
		ptr.skipWhitespace();
		if (ptr.hasNext() && ptr.currentChar() == ',') {
			ptr.skip(1);
			ptr.skipWhitespace();
			return true;
		}
		return false;
	}


}
