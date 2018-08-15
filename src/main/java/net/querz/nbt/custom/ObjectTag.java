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

	public ObjectTag(String name) {
		super(name);
	}

	public ObjectTag(String name, T value) {
		super(name, value);
	}

	@Override
	public byte getID() {
		return 90;
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
	public void setValue(T value) {
		super.setValue(value);
	}

	@Override
	public String valueToTagString(int depth) {
		return escapeString(getValue().toString(), true);
	}

	@Override
	public String valueToString(int depth) {
		return escapeString(getValue().toString(), false);
	}

	@Override
	protected T getEmptyValue() {
		throw new NullPointerException("ObjectTag does not support a null value");
	}

	@SuppressWarnings("unchecked")
	@Override
	public ObjectTag clone() {
		try {
			return new ObjectTag(getName(), (T) getValue().getClass().getMethod("clone").invoke(getValue()));
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			return new ObjectTag(getName(), getValue());
		}
	}

	@Override
	public boolean valueEquals(T value) {
		return getValue().equals(value);
	}
}
