package net.querz.nbt.io.snbt;

import net.querz.nbt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SNBTParser {

	private static final Pattern FLOAT_LITERAL = Pattern.compile("^([+-]?(?:\\d+(?:_+\\d+)*|\\d?)\\.?(?:\\d+(?:_+\\d+)*|\\d?)(?:[eE][+-]?(?:\\d+(?:_+\\d+)*))?)[fF]$");
	private static final Pattern DOUBLE_LITERAL = Pattern.compile("^([+-]?(?:\\d+(?:_+\\d+)*|\\d?)\\.?(?:\\d+(?:_+\\d+)*|\\d?)(?:[eE][+-]?(?:\\d+(?:_+\\d+)*))?)[dD]?$");
	private static final Pattern BYTE_LITERAL = Pattern.compile("^([-+]?\\d+(?:_+\\d+)*|0x[\\da-fA-F]+(?:_+[\\da-fA-F]+)*(?=[su])|0b[01]+(?:_+[01]+)*)([su]?)[bB]$");
	private static final Pattern SHORT_LITERAL = Pattern.compile("^([-+]?\\d+(?:_+\\d+)*|0x[\\da-fA-F]+(?:_+[\\da-fA-F]+)*|0b[01]+(?:_+[01]+)*)([su]?)[sS]$");
	private static final Pattern INT_LITERAL = Pattern.compile("^([-+]?\\d+(?:_+\\d+)*|0x[\\da-fA-F]+(?:_+[\\da-fA-F]+)*|0b[01]+(?:_+[01]+)*)(?:([su]?)[iI]|[iI]?)$");
	private static final Pattern LONG_LITERAL = Pattern.compile("^([-+]?\\d+(?:_+\\d+)*|0x[\\da-fA-F]+(?:_+[\\da-fA-F]+)*|0b[01]+(?:_+[01]+)*)([su]?)[lL]$");
	private static final Pattern AUTO_TYPE_ARRAY_LITERAL = Pattern.compile("^([-+]?\\d+(?:_+\\d+)*|0x[\\da-fA-F]+(?:_+[\\da-fA-F]+)*|0b[01]+(?:_+[01]+)*)$");
	private static final Pattern UNQUOTED_STRING = Pattern.compile("^[a-zA-Z_][\\w.+-]*$");

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
			if (hasSeparator()) {
				throw ptr.parseException("unexpected separator");
			}
			ptr.skipWhitespace();
			boolean quoted = ptr.currentChar() == '"' || ptr.currentChar() == '\'';
			String key = ptr.parseString();
			if (!quoted && !UNQUOTED_STRING.matcher(key).matches()) {
				throw ptr.parseException("invalid unquoted key " + key);
			}
			ptr.skipWhitespace();
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
			try {
				Matcher m;
				if ((m = AUTO_TYPE_ARRAY_LITERAL.matcher(s)).matches()) {
					byteList.add(parseByte(m.group(1), null));
				} else if ((m = BYTE_LITERAL.matcher(s)).matches()) {
					byteList.add(parseByte(m.group(1), m.group(2)));
				} else {
					throw ptr.parseException("invalid byte value " + s);
				}
			} catch (NumberFormatException ex) {
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
			try {
				Matcher m;
				if ((m = AUTO_TYPE_ARRAY_LITERAL.matcher(s)).matches()) {
					intList.add(parseInt(m.group(1), null));
				} else if ((m = INT_LITERAL.matcher(s)).matches()) {
					intList.add(parseInt(m.group(1), m.group(2)));
				} else if ((m = SHORT_LITERAL.matcher(s)).matches()) {
					intList.add((int) parseShort(m.group(1), m.group(2)));
				} else if ((m = BYTE_LITERAL.matcher(s)).matches()) {
					intList.add((int) parseByte(m.group(1), m.group(2)));
				} else {
					throw ptr.parseException("invalid int value " + s);
				}
			} catch (NumberFormatException ex) {
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
			try {
				Matcher m;
				if ((m = AUTO_TYPE_ARRAY_LITERAL.matcher(s)).matches()) {
					longList.add(parseLong(m.group(1), null));
				} else if ((m = LONG_LITERAL.matcher(s)).matches()) {
					longList.add(parseLong(m.group(1), m.group(2)));
				} else if ((m = INT_LITERAL.matcher(s)).matches()) {
					longList.add((long) parseInt(m.group(1), m.group(2)));
				} else if ((m = SHORT_LITERAL.matcher(s)).matches()) {
					longList.add((long) parseShort(m.group(1), m.group(2)));
				} else if ((m = BYTE_LITERAL.matcher(s)).matches()) {
					longList.add((long) parseByte(m.group(1), m.group(2)));
				} else {
					throw ptr.parseException("invalid long value " + s);
				}
			} catch (NumberFormatException ex) {
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
		List<Tag> list = new ArrayList<>();
		ptr.skipWhitespace();
		Tag.Type listType = null;
		boolean heterogeneous = false;
		while (ptr.hasNext() && ptr.currentChar() != ']') {
			if (hasSeparator()) {
				throw ptr.parseException("unexpected list separator");
			}
			Tag t = readValue();
			if (listType == null) {
				listType = t.getType();
			} else if (listType != t.getType()) {
				heterogeneous = true;
			}
			list.add(t);
			if (!hasSeparator()) {
				break;
			}
		}
		ptr.expectChar(']');
		ListTag tag = new ListTag();
		for (Tag t : list) {
			if (heterogeneous && t.getType() != Tag.Type.COMPOUND) {
				CompoundTag wrapper = new CompoundTag();
				wrapper.put("", t);
				tag.add(wrapper);
			} else {
				tag.add(t);
			}
		}

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
				Matcher m;
				if ((m = BYTE_LITERAL.matcher(value)).matches()) {
					return ByteTag.valueOf(parseByte(m.group(1), m.group(2)));
				}
				if ((m = SHORT_LITERAL.matcher(value)).matches()) {
					return ShortTag.valueOf(parseShort(m.group(1), m.group(2)));
				}
				if ((m = INT_LITERAL.matcher(value)).matches()) {
					return IntTag.valueOf(parseInt(m.group(1), m.group(2)));
				}
				if ((m = LONG_LITERAL.matcher(value)).matches()) {
					return LongTag.valueOf(parseLong(m.group(1), m.group(2)));
				}
				if ((m = FLOAT_LITERAL.matcher(value)).matches()) {
					return FloatTag.valueOf(Float.parseFloat(m.group(1)));
				}
				if ((m = DOUBLE_LITERAL.matcher(value)).matches()) {
					return DoubleTag.valueOf(Double.parseDouble(m.group(1)));
				}
			} catch (NumberFormatException ex) {
				// do nothing, check if it's a valid unquoted string below
			}

			if (!value.isEmpty() && !UNQUOTED_STRING.matcher(value).matches()) {
				throw ptr.parseException("invalid number or unquoted string " + value);
			}

			return StringTag.valueOf(value);
		}
	}

	private int getRadix(String s) {
		return s.startsWith("0x") ? 16 : s.startsWith("0b") ? 2 : 10;
	}

	private String trimNumeric(String s, int radix) {
		if (radix != 10) {
			s = s.substring(2);
		}
		return s.replace("_", "");
	}

	private boolean getSigned(String sign, int radix) {
		// When a suffix is used without u or s, it defaults to signed for decimal numbers and unsigned for binary and hexadecimal numbers
		return "".equals(sign) || sign == null ? radix == 10 : "s".equals(sign);
	}

	private byte parseByte(String s, String sign) throws NumberFormatException {
		int radix = getRadix(s);
		if (getSigned(sign, radix)) {
			return Byte.parseByte(trimNumeric(s, radix));
		}
		int v = Integer.parseUnsignedInt(trimNumeric(s, radix), radix);
		if (v > 0xFF) {
			throw new NumberFormatException("Value out of range. Value:\"" + s + "\" Radix:" + radix);
		}
		return (byte) v;
	}

	private short parseShort(String s, String sign) throws NumberFormatException {
		int radix = getRadix(s);
		if (getSigned(sign, radix)) {
			return Short.parseShort(trimNumeric(s, radix));
		}
		int v = Integer.parseUnsignedInt(trimNumeric(s, radix), radix);
		if (v > 0xFFFF) {
			throw new NumberFormatException("Value out of range. Value:\"" + s + "\" Radix:" + radix);
		}
		return (short) v;
	}

	private int parseInt(String s, String sign) throws NumberFormatException {
		int radix = getRadix(s);
		return getSigned(sign, radix) ? Integer.parseInt(trimNumeric(s, radix)) : Integer.parseUnsignedInt(trimNumeric(s, radix), radix);
	}

	private long parseLong(String s, String sign) throws NumberFormatException {
		int radix = getRadix(s);
		return getSigned(sign, radix) ? Long.parseLong(trimNumeric(s, radix)) : Long.parseUnsignedLong(trimNumeric(s, radix), radix);
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

	public int getReadChars() {
		return ptr.getIndex() + 1;
	}
}
