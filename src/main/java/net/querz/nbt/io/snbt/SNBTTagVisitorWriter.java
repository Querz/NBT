package net.querz.nbt.io.snbt;

import net.querz.nbt.*;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

public class SNBTTagVisitorWriter implements TagVisitor {

	private static final Pattern NO_ESCAPE = Pattern.compile("[a-zA-Z0-9._+-]+");

	private final String indent;
	private final int depth;
	private final Writer writer;

	public SNBTTagVisitorWriter(Writer writer) {
		this(writer, "", 0);
	}

	public SNBTTagVisitorWriter(Writer writer, String indent) {
		this(writer, indent, 0);
	}

	private SNBTTagVisitorWriter(Writer writer, String indent, int depth) {
		this.writer = writer;
		this.indent = indent;
		this.depth = depth;
	}

	@Override
	public void visit(ByteTag t) throws IOException {
		writer.write(t.asNumber() + "b");
	}

	@Override
	public void visit(ShortTag t) throws IOException {
		writer.write(t.asNumber() + "s");
	}

	@Override
	public void visit(IntTag t) throws IOException {
		writer.write(String.valueOf(t.asNumber()));
	}

	@Override
	public void visit(LongTag t) throws IOException {
		writer.write(t.asNumber() + "L");
	}

	@Override
	public void visit(FloatTag t) throws IOException {
		writer.write(t.asNumber() + "f");
	}

	@Override
	public void visit(DoubleTag t) throws IOException {
		writer.write(t.asNumber() + "d");
	}

	@Override
	public void visit(StringTag t) throws IOException {
		writer.write(StringTag.escapeString(t.getValue()));
	}

	@Override
	public void visit(ByteArrayTag t) throws IOException {
		writer.write("[B;");
		for (int i = 0; i < t.size(); i++) {
			writer.write(" ");
			writer.write(String.valueOf(t.getValue()[i]));
			writer.write("B");
			if (i < t.size() - 1) {
				writer.write(",");
			}
		}
		writer.write("]");
	}

	@Override
	public void visit(IntArrayTag t) throws IOException {
		writer.write("[I;");
		for (int i = 0; i < t.size(); i++) {
			writer.write(" ");
			writer.write(String.valueOf(t.getValue()[i]));
			if (i < t.size() - 1) {
				writer.write(",");
			}
		}
		writer.write("]");
	}

	@Override
	public void visit(LongArrayTag t) throws IOException {
		writer.write("[L;");
		for (int i = 0; i < t.size(); i++) {
			writer.write(" ");
			writer.write(String.valueOf(t.getValue()[i]));
			writer.write("L");
			if (i < t.size() - 1) {
				writer.write(",");
			}
		}
		writer.write("]");
	}

	@Override
	public void visit(ListTag t) throws IOException {
		if (t.isEmpty()) {
			writer.write("[]");
		} else {
			writer.write("[");
			if (!indent.isEmpty()) {
				writer.write('\n');
			}
			for (int i = 0; i < t.size(); i++) {
				Tag tag = t.get(i);
				writer.write(indent.repeat(depth + 1));
				new SNBTTagVisitorWriter(writer, indent, depth + 1).visit(tag);
				if (i < t.size() - 1) {
					writer.write(", ");
				}
				if (!indent.isEmpty()) {
					writer.write('\n');
				}
			}
			if (!indent.isEmpty()) {
				writer.write(indent.repeat(depth));
			}
			writer.write(']');
		}
	}

	@Override
	public void visit(CompoundTag t) throws IOException {
		if (t.isEmpty()) {
			writer.write("{}");
		} else {
			writer.write("{");
			if (!indent.isEmpty()) {
				writer.write('\n');
			}
			Iterator<Map.Entry<String, Tag>> iterator = t.iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, Tag> entry = iterator.next();
				String key = entry.getKey();
				Tag tag = t.get(entry.getKey());
				String escapedKey = NO_ESCAPE.matcher(key).matches() ? key : StringTag.escapeString(key);
				writer.write(indent.repeat(depth + 1));
				writer.write(escapedKey);
				writer.write(": ");
				new SNBTTagVisitorWriter(writer, indent, depth + 1).visit(tag);
				if (iterator.hasNext()) {
					writer.write(", ");
				}
				if (!indent.isEmpty()) {
					writer.write("\n");
				}
			}
			writer.write(indent.repeat(depth));
			writer.write("}");
		}
	}

	@Override
	public void visit(EndTag t) throws IOException {}

	public void visit(Tag tag) throws IOException {
		try {
			tag.accept(this);
		} catch (Exception ex) {
			throw new IOException(ex);
		}
	}
}
