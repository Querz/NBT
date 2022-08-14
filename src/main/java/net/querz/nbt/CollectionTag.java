package net.querz.nbt;

import java.util.AbstractList;

public sealed abstract class CollectionTag<T extends Tag> extends AbstractList<T> implements Tag permits ByteArrayTag, IntArrayTag, ListTag, LongArrayTag {

	public CollectionTag() {}

	public abstract T set(int index, T tag);

	public abstract void add(int index, T tag);

	public abstract T remove(int index);

	public abstract Type getElementType();

}
