package net.querz.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class StringTag implements Tag {

	private final String value;

	private static final StringTag EMPTY = new StringTag("");

	private StringTag(String s) {
		value = s;
	}

	public static StringTag valueOf(String s) {
		return s.isEmpty() ? EMPTY : new StringTag(s);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeUTF(value);
	}

	public static void skipUTF(DataInput in) throws IOException {
		in.skipBytes(in.readUnsignedShort());
	}

	@Override
	public StringTag copy() {
		return this;
	}

	@Override
	public void accept(TagVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		} else {
			return other instanceof StringTag  && value.equals(((StringTag) other).value);
		}
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	public String getValue() {
		return value;
	}

	public static final TagReader<StringTag> READER = new TagReader<StringTag>() {

		@Override
		public StringTag read(DataInput in, int depth) throws IOException {
			return StringTag.valueOf(in.readUTF());
		}

		@Override
		public TagTypeVisitor.ValueResult read(DataInput in, TagTypeVisitor visitor) throws IOException {
			return visitor.visit(in.readUTF());
		}

		@Override
		public void skip(DataInput in) throws IOException {
			in.skipBytes(in.readUnsignedShort());
		}
	};

	public static String escapeString(String s) {
		StringBuilder sb = new StringBuilder(" ");
		char quote = 0;

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == '\\') {
				sb.append('\\');
			} else if (c == '"' || c == '\'') {
				if (quote == 0) {
					quote = c == '"' ? '\'' : '"';
				}
				if (quote == c) {
					sb.append('\\');
				}
			}
			sb.append(c);
		}

		if (quote == 0) {
			quote = '"';
		}

		sb.setCharAt(0, quote);
		sb.append(quote);
		return sb.toString();
	}
}
