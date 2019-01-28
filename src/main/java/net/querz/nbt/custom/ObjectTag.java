package net.querz.nbt.custom;

import net.querz.nbt.Tag;
import net.querz.nbt.TagFactory;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class ObjectTag<T extends Serializable> extends Tag<T> implements Comparable<ObjectTag<T>> {

	public static void register() {
		TagFactory.registerCustomTag(90, ObjectTag::new, ObjectTag.class);
	}

	public ObjectTag() {
		super(null);
	}

	public ObjectTag(T value) {
		super(value);
	}

	@Override
	protected T checkValue(T value) {
		return value;
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
	public void serializeValue(DataOutputStream dos, int maxDepth) throws IOException {
		new ObjectOutputStream(dos).writeObject(getValue());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deserializeValue(DataInputStream dis, int maxDepth) throws IOException {
		try {
			setValue((T) new ObjectInputStream(dis).readObject());
		} catch (InvalidClassException | ClassNotFoundException e) {
			throw new IOException(e.getCause());
		}
	}

	@Override
	public String valueToString(int maxDepth) {
		return getValue() == null ? "null" : escapeString(getValue().toString(), false);
	}

	@Override
	public String valueToTagString(int maxDepth) {
		return getValue() == null ? "null" : escapeString(getValue().toString(), true);
	}

	@Override
	public boolean equals(Object other) {
		return super.equals(other) && Objects.equals(getValue(), ((ObjectTag<?>) other).getValue());
	}

	@Override
	public int hashCode() {
		if (getValue() == null) {
			return 0;
		}
		return getValue().hashCode();
	}

	@SuppressWarnings("unchecked")
	@Override
	public int compareTo(ObjectTag<T> o) {
		if (o.getValue() instanceof Comparable && getValue() instanceof Comparable) {
			return ((Comparable<T>) getValue()).compareTo(o.getValue());
		} else if (o.getValue() == getValue()) {
			return 0;
		} else if (getValue() == null) {
			// sort a null value to the end
			return 1;
		} else if (o.getValue() == null) {
			return -1;
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ObjectTag<T> clone() {
		if (getValue() == null) {
			return new ObjectTag<>();
		}
		try {
			return new ObjectTag<>((T) getValue().getClass().getMethod("clone").invoke(getValue()));
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			return new ObjectTag<>(getValue());
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
