package net.querz.nbt;

import java.util.AbstractList;

public abstract class CollectionTag<T extends Tag> extends AbstractList<T> implements Tag {

	public CollectionTag() {}

	public abstract T set(int index, T tag);

	public abstract void add(int index, T tag);

	public abstract T remove(int index);

	public abstract Type getElementType();

	@SuppressWarnings("unchecked")
	public T castElementType(Tag tag) {
		return (T) getElementType().tagClass.cast(tag);
	}

}
