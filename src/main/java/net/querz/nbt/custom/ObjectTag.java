package net.querz.nbt.custom;

import net.querz.nbt.Tag;
import net.querz.nbt.io.NBTOutputStream;
import net.querz.nbt.io.NBTUtil;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class ObjectTag<T extends Serializable> extends Tag<T> implements Comparable<ObjectTag<T>> {

	public static void register() {
		NBTUtil.registerCustomTag(90, (o, t, m) -> serialize(o, t), (i, m) -> deserialize(i), ObjectTag.class);
	}

	public ObjectTag() {
		super(null);
	}

	public ObjectTag(T value) {
		super(value);
	}

	@Override
	public byte getID() {
		return 90;
	}

	private static <O extends Serializable> ObjectTag<?> createUnchecked(O value) {
		return new ObjectTag<>(value);
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

	public static void serialize(NBTOutputStream dos, ObjectTag<?> tag) throws IOException {
		new ObjectOutputStream(dos).writeObject(tag.getValue());
	}

	@SuppressWarnings("unchecked")
	public static ObjectTag<?> deserialize(DataInputStream dis) throws IOException {
		try {
			return createUnchecked((Serializable) new ObjectInputStream(dis).readObject());
		} catch (InvalidClassException | ClassNotFoundException e) {
			throw new IOException(e.getCause());
		}
	}

	@Override
	public String valueToString(int maxDepth) {
		return getValue() == null ? "null" : escapeString(getValue().toString(), false);
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
