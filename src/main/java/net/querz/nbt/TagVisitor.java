package net.querz.nbt;

public interface TagVisitor {

	void visit(ByteTag t) throws Exception;

	void visit(ShortTag t) throws Exception;

	void visit(IntTag t) throws Exception;

	void visit(LongTag t) throws Exception;

	void visit(FloatTag t) throws Exception;

	void visit(DoubleTag t) throws Exception;

	void visit(StringTag t) throws Exception;

	void visit(ByteArrayTag t) throws Exception;

	void visit(IntArrayTag t) throws Exception;

	void visit(LongArrayTag t) throws Exception;

	void visit(ListTag t) throws Exception;

	void visit(CompoundTag t) throws Exception;

	void visit(EndTag t) throws Exception;
}
