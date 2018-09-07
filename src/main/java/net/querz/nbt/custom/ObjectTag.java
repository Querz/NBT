package net.querz.nbt.custom;

import net.querz.nbt.Tag;
import net.querz.nbt.TagFactory;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

public class ObjectTag<T extends Serializable> extends Tag<T> {

	public static void register() {
		TagFactory.registerCustomTag(90, ObjectTag.class);
	}

	public ObjectTag() {}

	public ObjectTag(T value) {
		super(value);
	}

	@Override
	public byte getID() {
		return 90;
	}

	@Override
	protected T getEmptyValue() {
		return null;
	}

	@Override
	public T getValue() {
		return super.getValue();
	}

	@Override
	public void setValue(T value) {
		super.setValue(value);
	}

	@SuppressWarnings("unchecked")
	public <L extends Serializable> ObjectTag<L> asTypedObjectTag(Class<L> type) {
		checkTypeClass(type);
		return (ObjectTag<L>) this;
	}

	@Override
	public void serializeValue(DataOutputStream dos, int depth) throws IOException {
		new ObjectOutputStream(dos).writeObject(getValue());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deserializeValue(DataInputStream dis, int depth) throws IOException {
		try {
			setValue((T) new ObjectInputStream(dis).readObject());
		} catch (ClassNotFoundException e) {
			throw new IOException(e.getCause());
		}
	}

	@Override
	public String valueToString(int depth) {
		return getValue() == null ? "null" : escapeString(getValue().toString(), false);
	}

	@Override
	public String valueToTagString(int depth) {
		return getValue() == null ? "null" : escapeString(getValue().toString(), true);
	}

	@Override
	public boolean equals(Object other) {
		return super.equals(other)
				&& (getValue() == ((ObjectTag) other).getValue()
				|| getValue() != null
				&& getValue().equals(((ObjectTag<?>) other).getValue()));
	}

	@SuppressWarnings("unchecked")
	@Override
	public int compareTo(Tag<T> o) {
		ObjectTag<?> oo = (ObjectTag<?>) o;
		if (oo.getValue() instanceof Comparable && getValue() instanceof Comparable) {
			return ((Comparable) getValue()).compareTo(oo.getValue());
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ObjectTag<T> clone() {
		try {
			return new ObjectTag((T) getValue().getClass().getMethod("clone").invoke(getValue()));
		} catch (NullPointerException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			return new ObjectTag(getValue());
		}
	}

	private void checkTypeClass(Class<?> clazz) {
		if (getValue() != null && (!clazz.isAssignableFrom(getValue().getClass()))) {
			throw new ClassCastException(String.format(
					"cannot cast ObjectTag<%s> to ObjectTag<%s>",
					getValue().getClass().getSimpleName(), clazz.getSimpleName()));
		}
	}
}
