package net.querz.nbt.custom;

import net.querz.nbt.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ObjectTag<T extends Serializable & Cloneable> extends Tag implements CustomTag {
	private static final int TAG_ID = 90;

	public static void register() {
		TagType.registerCustomTag(TAG_ID, ObjectTag.class);
	}

	private T value;

	public ObjectTag() {
		this("", null);
	}

	public ObjectTag(String name, T value) {
		super(TagType.CUSTOM, name);
		setValue(value);
	}

	public void setValue(T value) {
		this.value = value;
	}

	@Override
	public int getId() {
		return TAG_ID;
	}

	@Override
	protected String valueToTagString(int depth) {
		return "<" + value.toString().replace("\"", "\\\"") + ">";
	}

	@Override
	protected void serialize(NBTOutputStream nbtOut, int depth) throws IOException {
		ObjectOutputStream o = new ObjectOutputStream(nbtOut.getDataOutputStream());
		o.writeObject(value);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected Tag deserialize(NBTInputStream nbtIn, int depth) throws Exception {
		ObjectInputStream o = new ObjectInputStream(nbtIn.getDataInputStream());
		value = (T) o.readObject();
		return this;
	}

	@Override
	public T getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "<object:" + getName() + ":" + value + ">";
	}

	@Override
	public String toTagString() {
		return getName() + ":\"" + valueToTagString(0) + "\"";
	}

	@Override
	@SuppressWarnings("unchecked")
	public ObjectTag<T> clone() {
		ObjectTag<T> clone = new ObjectTag<>(getName(), null);
		try {
			clone.value = (T) value.getClass().getMethod("clone").invoke(value);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return clone;
	}
}
