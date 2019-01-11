package net.querz.nbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class DummyArrayTag<T> extends ArrayTag<T> {

	public DummyArrayTag(T value) {
		super(value);
	}

	@Override
	protected T getEmptyValue() {
		return null;
	}

	@Override
	public void serializeValue(DataOutputStream dos, int depth) throws IOException {

	}

	@Override
	public void deserializeValue(DataInputStream dis, int depth) throws IOException {

	}

	@Override
	public String valueToTagString(int depth) {
		return null;
	}

	@Override
	public Tag<T> clone() {
		return null;
	}
}
