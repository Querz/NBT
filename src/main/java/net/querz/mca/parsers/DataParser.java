package net.querz.mca.parsers;

import java.util.Iterator;

public interface DataParser<T> extends Iterable<T> {

	T getDataAt(int index);

	void setDataAt(int index, T data);

	int getSize();

	@Override
	default Iterator<T> iterator() {
		return new DataParserIterator<>(this);
	}
}
