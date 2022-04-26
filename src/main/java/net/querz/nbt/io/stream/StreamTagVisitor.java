package net.querz.nbt.io.stream;

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
import net.querz.nbt.TagType;
import net.querz.nbt.TagTypeVisitor;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Consumer;

public class StreamTagVisitor implements TagTypeVisitor {

	private String lastName = "";
	private Tag tag;
	private final Deque<Consumer<Tag>> consumers = new ArrayDeque<>();

	public StreamTagVisitor() {}

	public Tag getResult() {
		return tag;
	}

	protected int depth() {
		return consumers.size();
	}

	private void appendEntry(Tag tag) {
		consumers.getLast().accept(tag);
	}

	@Override
	public ValueResult visitEnd() {
		appendEntry(EndTag.INSTANCE);
		return ValueResult.CONTINUE;
	}

	@Override
	public ValueResult visit(byte b) {
		appendEntry(ByteTag.valueOf(b));
		return ValueResult.CONTINUE;
	}

	@Override
	public ValueResult visit(short s) {
		appendEntry(ShortTag.valueOf(s));
		return ValueResult.CONTINUE;
	}

	@Override
	public ValueResult visit(int i) {
		appendEntry(IntTag.valueOf(i));
		return ValueResult.CONTINUE;
	}

	@Override
	public ValueResult visit(long l) {
		appendEntry(LongTag.valueOf(l));
		return ValueResult.CONTINUE;
	}

	@Override
	public ValueResult visit(float f) {
		appendEntry(FloatTag.valueOf(f));
		return ValueResult.CONTINUE;
	}

	@Override
	public ValueResult visit(double d) {
		appendEntry(DoubleTag.valueOf(d));
		return ValueResult.CONTINUE;
	}

	@Override
	public ValueResult visit(String s) {
		appendEntry(StringTag.valueOf(s));
		return ValueResult.CONTINUE;
	}

	@Override
	public ValueResult visit(byte[] b) {
		appendEntry(new ByteArrayTag(b));
		return ValueResult.CONTINUE;
	}

	@Override
	public ValueResult visit(int[] i) {
		appendEntry(new IntArrayTag(i));
		return ValueResult.CONTINUE;
	}

	@Override
	public ValueResult visit(long[] l) {
		appendEntry(new LongArrayTag(l));
		return ValueResult.CONTINUE;
	}

	@Override
	public ValueResult visitList(TagType<?> type, int length) {
		return ValueResult.CONTINUE;
	}

	@Override
	public EntryResult visitEntry(TagType<?> type) {
		return EntryResult.ENTER;
	}

	@Override
	public EntryResult visitEntry(TagType<?> type, String name) {
		lastName = name;
		enterContainer(type);
		return EntryResult.ENTER;
	}

	@Override
	public EntryResult visitElement(TagType<?> type, int index) {
		enterContainer(type);
		return EntryResult.ENTER;
	}

	@Override
	public ValueResult visitContainerEnd() {
		consumers.removeLast();
		return ValueResult.CONTINUE;
	}

	@Override
	public ValueResult visitRootEntry(TagType<?> type) {
		if (type == ListTag.TYPE) {
			ListTag l = new ListTag();
			tag = l;
			consumers.addLast(l::add);
		} else if (type == CompoundTag.TYPE) {
			CompoundTag c = new CompoundTag();
			tag = c;
			consumers.addLast(v -> c.put(lastName, v));
		} else {
			consumers.addLast(v -> tag = v);
		}
		return ValueResult.CONTINUE;
	}

	private void enterContainer(TagType<?> type) {
		if (type == ListTag.TYPE) {
			ListTag l = new ListTag();
			appendEntry(l);
			consumers.addLast(l::add);
		} else if (type == CompoundTag.TYPE) {
			CompoundTag c = new CompoundTag();
			appendEntry(c);
			consumers.addLast(v -> c.put(lastName, v));
		}
	}
}
