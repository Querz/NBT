package net.querz.nbt.io.snbt;

public class StringPointer {

	private String value;
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

	public int parseInt() throws ParseException {
		int start = index;
		while (hasNext() && isNumberChar(currentChar())) {
			skip(1);
		}
		String num = value.substring(start, index);
		try {
			return Integer.parseInt(num);
		} catch (NumberFormatException ex) {
			index = start;
			throw parseException("invalid int");
		}
	}

	public long parseLong() throws ParseException {
		int start = index;
		while (hasNext() && isNumberChar(currentChar())) {
			skip(1);
		}
		String num = value.substring(start, index);
		try {
			return Long.parseLong(num);
		} catch (NumberFormatException ex) {
			index = start;
			throw parseException("invalid long");
		}
	}

	public float parseFloat() throws ParseException {
		int start = index;
		while (hasNext() && isNumberChar(currentChar())) {
			skip(1);
		}
		String num = value.substring(start, index);
		try {
			return Float.parseFloat(num);
		} catch (NumberFormatException ex) {
			index = start;
			throw parseException("invalid float");
		}
	}

	public double parseDouble() throws ParseException {
		int start = index;
		while (hasNext() && isNumberChar(currentChar())) {
			skip(1);
		}
		String num = value.substring(start, index);
		try {
			return Double.parseDouble(num);
		} catch (NumberFormatException ex) {
			index = start;
			throw parseException("invalid double");
		}
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
