package net.querz.nbt.io;

import net.querz.nbt.ArrayTag;
import net.querz.nbt.ByteArrayTag;
import net.querz.nbt.ByteTag;
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
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

public final class MSONDeserializer implements Deserializer {

	private static final Pattern
			FLOAT_PATTERN = Pattern.compile("^[-+]?(?:\\d+\\.?|\\d*\\.\\d+)(?:e[-+]?\\d+)?f$", Pattern.CASE_INSENSITIVE),
			DOUBLE_PATTERN = Pattern.compile("^[-+]?(?:\\d+\\.?|\\d*\\.\\d+)(?:e[-+]?\\d+)?d$", Pattern.CASE_INSENSITIVE),
			DOUBLE_NO_SUFFIX_PATTERN = Pattern.compile("^[-+]?(?:\\d+\\.|\\d*\\.\\d+)(?:e[-+]?\\d+)?$", Pattern.CASE_INSENSITIVE),
			BYTE_PATTERN = Pattern.compile("^[-+]?\\d+b$", Pattern.CASE_INSENSITIVE),
			SHORT_PATTERN = Pattern.compile("^[-+]?\\d+s$", Pattern.CASE_INSENSITIVE),
			INT_PATTERN = Pattern.compile("^[-+]?\\d+$", Pattern.CASE_INSENSITIVE),
			LONG_PATTERN = Pattern.compile("^[-+]?\\d+l$", Pattern.CASE_INSENSITIVE);

	private StringPointer stream;

	public MSONDeserializer(String string) {
		this.stream = new StringPointer(string);
	}

	@Override
	public Tag<?> read(int maxDepth) {
		stream.skipWhitespace();
		switch (stream.currentChar()) {
		case '{':
			return parseCompoundTag(maxDepth);
		case '[':
			if (stream.hasCharsLeft(2) && stream.lookAhead(1) != '"' && stream.lookAhead(2) == ';') {
				return parseNumArray();
			}
			return parseListTag(maxDepth);
		}
		return parseStringOrLiteral();
	}

	private Tag<?> parseStringOrLiteral() {
		stream.skipWhitespace();
		if (stream.currentChar() == '"') {
			return new StringTag(stream.parseQuotedString());
		}
		String s = stream.parseSimpleString();
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

	private CompoundTag parseCompoundTag(int maxDepth) {
		stream.expectChar('{');

		CompoundTag compoundTag = new CompoundTag();

		stream.skipWhitespace();
		while (stream.hasNext() && stream.currentChar() != '}') {
			stream.skipWhitespace();
			String key = stream.currentChar() == '"' ? stream.parseQuotedString() : stream.parseSimpleString();
			if (key.isEmpty()) {
				throw new ParseException("empty keys are not allowed");
			}
			stream.expectChar(':');

			compoundTag.put(key, read(decrementMaxDepth(maxDepth)));

			if (!stream.nextArrayElement()) {
				break;
			}
		}
		stream.expectChar('}');
		return compoundTag;
	}

	private ListTag<?> parseListTag(int maxDepth) {
		stream.expectChar('[');
		stream.skipWhitespace();
		ListTag<?> list = ListTag.createUnchecked();
		while (stream.currentChar() != ']') {
			Tag<?> element = read(decrementMaxDepth(maxDepth));
			list.addUnchecked(element);
			if (!stream.nextArrayElement()) {
				break;
			}
		}
		stream.expectChar(']');
		return list;
	}

	private ArrayTag<?> parseNumArray() {
		stream.expectChar('[');
		char arrayType = stream.next();
		stream.expectChar(';');
		stream.skipWhitespace();
		switch (arrayType) {
		case 'B':
			return parseNumericArrayTag(
					a -> new ByteArrayTag((byte[]) a),
					Byte::parseByte,
					li -> {
				byte[] bytes = new byte[li.size()];
				for (int i = 0; i < li.size(); i++) {
					bytes[i] = li.get(i).byteValue();
				}
				return bytes;
			});
		case 'I':
			return parseNumericArrayTag(
					a -> new IntArrayTag((int[]) a),
					Integer::parseInt,
					li -> li.stream().mapToInt(i -> (int) i).toArray());
		case 'L':
			return parseNumericArrayTag(
					a -> new LongArrayTag((long[]) a),
					Long::parseLong,
					li -> li.stream().mapToLong(l -> (long) l).toArray());
		}
		throw new ParseException("invalid array type '" + arrayType + "'");
	}

	private ArrayTag<?> parseNumericArrayTag(
			Function<Object, ArrayTag<?>> constructor,
			Function<String, Number> numberParser,
			Function<List<Number>, Object> arrayParser) {
		
		List<Number> numberList = new ArrayList<>();
		while (stream.currentChar() != ']') {
			String s = stream.parseSimpleString();
			stream.skipWhitespace();
			numberList.add(numberParser.apply(s));
			if (!stream.nextArrayElement()) {
				break;
			}
		}
		stream.expectChar(']');
		return constructor.apply(arrayParser.apply(numberList));
	}
}
