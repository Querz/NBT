package net.querz.nbt.io.snbt;

import net.querz.nbt.ByteArrayTag;
import net.querz.nbt.ByteTag;
import net.querz.nbt.CompoundTag;
import net.querz.nbt.DoubleTag;
import net.querz.nbt.EndTag;
import net.querz.nbt.FloatTag;
import net.querz.nbt.IntArrayTag;
import net.querz.nbt.IntTag;
import net.querz.nbt.ListTag;
import net.querz.nbt.LongArrayTag;
import net.querz.nbt.LongTag;
import net.querz.nbt.ShortTag;
import net.querz.nbt.StringTag;
import net.querz.nbt.Tag;
import net.querz.nbt.TagVisitor;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

public class SNBTTagVisitor implements TagVisitor {

	private static final Pattern NO_ESCAPE = Pattern.compile("[a-zA-Z0-9._+-]+");

	private final String indent;
	private final int depth;
	private String result;

	public SNBTTagVisitor() {
		this("", 0);
	}

	public SNBTTagVisitor(String indent) {
		this(indent, 0);
	}

	private SNBTTagVisitor(String indent, int depth) {
		result = "";
		this.indent = indent;
		this.depth = depth;
	}

	@Override
	public void visit(ByteTag t) {
		result = t.asNumber() + "b";
	}

	@Override
	public void visit(ShortTag t) {
		result = t.asNumber() + "s";
	}

	@Override
	public void visit(IntTag t) {
		result = String.valueOf(t.asNumber());
	}

	@Override
	public void visit(LongTag t) {
		result = t.asNumber() + "L";
	}

	@Override
	public void visit(FloatTag t) {
		result = t.asNumber() + "f";
	}

	@Override
	public void visit(DoubleTag t) {
		result = t.asNumber() + "d";
	}

	@Override
	public void visit(StringTag t) {
		result = StringTag.escapeString(t.getValue());
	}

	@Override
	public void visit(ByteArrayTag t) {
		StringBuilder sb = new StringBuilder("[B;");
		for (int i = 0; i < t.size(); i++) {
			sb.append(" ").append(t.getValue()[i]).append("B");
			if (i < t.size() - 1) {
				sb.append(",");
			}
		}
		sb.append("]");
		result = sb.toString();
	}

	@Override
	public void visit(IntArrayTag t) {
		StringBuilder sb = new StringBuilder("[I;");
		for (int i = 0; i < t.size(); i++) {
			sb.append(" ").append(t.getValue()[i]);
			if (i < t.size() - 1) {
				sb.append(",");
			}
		}
		sb.append("]");
		result = sb.toString();
	}

	@Override
	public void visit(LongArrayTag t) {
		StringBuilder sb = new StringBuilder("[L;");
		for (int i = 0; i < t.size(); i++) {
			sb.append(" ").append(t.getValue()[i]).append("L");
			if (i < t.size() - 1) {
				sb.append(",");
			}
		}
		sb.append("]");
		result = sb.toString();
	}

	@Override
	public void visit(ListTag t) {
		if (t.isEmpty()) {
			result = "[]";
		} else {
			StringBuilder sb = new StringBuilder("[");
			if (!indent.isEmpty()) {
				sb.append('\n');
			}
			for (int i = 0; i < t.size(); i++) {
				Tag tag = t.get(i);
				sb.append(indent.repeat(depth + 1));
				sb.append(new SNBTTagVisitor(indent, depth + 1).visit(tag));
				if (i < t.size() - 1) {
					sb.append(", ");
				}
				if (!indent.isEmpty()) {
					sb.append('\n');
				}
			}
			if (!indent.isEmpty()) {
				sb.append(indent.repeat(depth));
			}
			sb.append(']');
			result = sb.toString();
		}
	}

	@Override
	public void visit(CompoundTag t) {
		if (t.isEmpty()) {
			result = "{}";
		} else {
			StringBuilder sb = new StringBuilder("{");
			if (!indent.isEmpty()) {
				sb.append('\n');
			}
			Iterator<Map.Entry<String, Tag>> iterator = t.iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, Tag> entry = iterator.next();
				String key = entry.getKey();
				Tag tag = t.get(entry.getKey());
				String escapedKey = NO_ESCAPE.matcher(key).matches() ? key : StringTag.escapeString(key);
				sb.append(indent.repeat(depth + 1));
				sb.append(escapedKey);
				sb.append(": ");
				sb.append(new SNBTTagVisitor(indent, depth + 1).visit(tag));
				if (iterator.hasNext()) {
					sb.append(", ");
				}
				if (!indent.isEmpty()) {
					sb.append("\n");
				}
			}
			sb.append(indent.repeat(depth));
			sb.append("}");
			result = sb.toString();
		}
	}

	@Override
	public void visit(EndTag t) {}

	@Override
	public String toString() {
		return result;
	}

	public String visit(Tag tag) {
		try {
			tag.accept(this);
		} catch (Exception ex) {}
		return result;
	}
}
