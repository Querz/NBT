package net.querz.nbt;

public interface TagVisitor {

	void visit(ByteTag t);

	void visit(ShortTag t);

	void visit(IntTag t);

	void visit(LongTag t);

	void visit(FloatTag t);

	void visit(DoubleTag t);

	void visit(StringTag t);

	void visit(ByteArrayTag t);

	void visit(IntArrayTag t);

	void visit(LongArrayTag t);

	void visit(ListTag t);

	void visit(CompoundTag t);

	void visit(EndTag t);

}
