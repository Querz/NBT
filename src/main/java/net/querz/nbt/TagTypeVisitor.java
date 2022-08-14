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

	ValueResult visitList(TagReader<?> reader, int length);

	EntryResult visitEntry(TagReader<?> reader);

	EntryResult visitEntry(TagReader<?> reader, String name);

	EntryResult visitElement(TagReader<?> reader, int index);

	ValueResult visitContainerEnd();

	ValueResult visitRootEntry(TagReader<?> reader);

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
