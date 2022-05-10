package net.querz.nbt;

public interface TagTypeVisitor {

	ValueResult visitEnd();

	ValueResult visit(byte b);

	ValueResult visit(short s);

	ValueResult visit(int i);

	ValueResult visit(long l);

	ValueResult visit(float f);

	ValueResult visit(double d);

	ValueResult visit(String s);

	ValueResult visit(byte[] b);

	ValueResult visit(int[] i);

	ValueResult visit(long[] l);

	ValueResult visitList(TagType<?> type, int length);

	EntryResult visitEntry(TagType<?> type);

	EntryResult visitEntry(TagType<?> type, String name);

	EntryResult visitElement(TagType<?> type, int index);

	ValueResult visitContainerEnd();

	ValueResult visitRootEntry(TagType<?> type);

	Tag getResult();

	enum ValueResult {
		CONTINUE,
		BREAK,
		RETURN
	}

	enum EntryResult {
		ENTER,
		SKIP,
		BREAK,
		RETURN
	}
}
