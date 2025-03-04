package net.querz.nbt.io.snbt;

import net.querz.io.util.ThrowingFunction;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

class StringPointer {

	private final String value;
	private int index;

	public StringPointer(String value) {
		this.value = value;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int size() {
		return value.length();
	}

	public String parseString() throws ParseException {
		if (!hasNext()) {
			return "";
		}
		char start = currentChar();
		if (start == '"' || start == '\'') {
			skip(1);
			return parseStringUntil(start);
		}
		return parseSimpleString();
	}

	public String parseSimpleString() throws ParseException {
		int start = index;
		while (hasNext() && isSimpleChar(currentChar())) {
			skip(1);
		}
		return value.substring(start, index);
	}

	public String parseQuotedString() throws ParseException {
		if (!hasNext()) {
			return "";
		}
		char quote = next();
		if (quote == '"' || quote == '\'') {
			return parseStringUntil(quote);
		}
		throw parseException("expected quote at start of string");
	}

	public String parseStringUntil(char end) throws ParseException {
		StringBuilder sb = new StringBuilder();
		boolean escaped = false;
		while (hasNext()) {
			char c = next();
			if (escaped) {
				if (c == end || c == '\\') {
					sb.append(c);
					escaped = false;
				} else if (escapes.containsKey(c)) {
					String decoded = escapes.get(c).apply(this);
					sb.append(decoded);
					escaped = false;
				} else {
					throw parseException("invalid escape of '" + c + "'");
				}
			} else if (c == '\\') {
				escaped = true;
			} else if (c == end) {
				return sb.toString();
			} else {
				sb.append(c);
			}
		}
		throw parseException("missing end quote");
	}

	private static final Map<Character, ThrowingFunction<StringPointer, String, ParseException>> escapes = new HashMap<>();
	private static final Pattern HEX_DIGIT = Pattern.compile("^[0-9a-fA-F]*$");

	static {
		escapes.put('x', p -> p.readUnicodeHex(2));
		escapes.put('u', p -> p.readUnicodeHex(4));
		escapes.put('U', p -> p.readUnicodeHex(8));
		escapes.put('N', p -> {
			p.expectChar('{');
			String name = p.parseStringUntil('}');
			try {
				int c = Character.codePointOf(name);
				return String.valueOf(Character.toChars(c));
			} catch (IllegalArgumentException ex) {
				throw p.parseException("undefined character name '" + name + "'");
			}
		});
		escapes.put('b', p -> "\b");
		escapes.put('s', p -> " ");
		escapes.put('t', p -> "\t");
		escapes.put('n', p -> "\n");
		escapes.put('f', p -> "\f");
		escapes.put('r', p -> "\r");
	}

	private String readUnicodeHex(int length) throws ParseException {
		String seq = read(length);
		if (!HEX_DIGIT.matcher(seq).matches()) {
			throw parseException("invalid hex digit '" + seq + "'");
		}
		int c = Integer.parseInt(seq, 16);
		if (!Character.isDefined(c)) {
			throw parseException("undefined character '" + seq + "'");
		}
		return String.valueOf(Character.toChars(c));
	}

	public String read(int length) throws ParseException {
		if (!hasCharsLeft(length)) {
			index = value.length() - 1;
			throw parseException("unexpected end of string");
		}
		int start = index;
		index += length;
		return value.substring(start, index);
	}

	public void skipWhitespace() {
		while (hasNext() && Character.isWhitespace(currentChar())) {
			index++;
		}
	}

	public void expectChar(char c) throws ParseException {
		if (hasNext() && currentChar() != c) {
			throw parseException("expected " + c + " but got " + currentChar());
		}
		skip(1);
	}

	public boolean hasNext() {
		return index < value.length();
	}

	public boolean hasCharsLeft(int num) {
		return this.index + num < value.length();
	}

	public char currentChar() {
		return value.charAt(index);
	}

	public char next() {
		return value.charAt(index++);
	}

	public void skip(int offset) {
		index += offset;
	}

	public char lookAhead(int offset) {
		return value.charAt(index + offset);
	}

	private static boolean isNumberChar(char c) {
		return c >= '0' && c <= '9'
				|| c == '-' || c == '.';
	}

	private static boolean isSimpleChar(char c) {
		return c >= 'a' && c <= 'z'
				|| c >= 'A' && c <= 'Z'
				|| c >= '0' && c <= '9'
				|| c == '-' || c == '_'
				|| c == '+' || c == '.';
	}

	public ParseException parseException(String msg) {
		return new ParseException(msg, value, index);
	}
}
