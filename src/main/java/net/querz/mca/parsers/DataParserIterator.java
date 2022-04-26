package net.querz.mca.parsers;

import java.util.Iterator;

final class DataParserIterator<T> implements Iterator<T> {

	private int index = 0;
	private final DataParser<T> dataParser;

	public DataParserIterator(DataParser<T> dataParser) {
		this.dataParser = dataParser;
	}

	@Override
	public boolean hasNext() {
		return index < dataParser.getSize();
	}

	@Override
	public T next() {
		T data = dataParser.getDataAt(index);
		index++;
		return data;
	}
}