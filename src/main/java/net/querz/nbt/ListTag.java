package net.querz.nbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class ListTag<T extends Tag> extends Tag<List<T>> implements Iterable<T> {

	private byte typeID = 0;
	private Class<? extends Tag> typeClass = EndTag.class;

	public ListTag() {}

	@Override
	public byte getID() {
		return 9;
	}

	public byte getTypeID() {
		return typeID;
	}

	public Class<? extends Tag> getTypeClass() {
		return typeClass;
	}

	@Override
	protected List<T> getEmptyValue() {
		return new ArrayList<>(3);
	}

	public int size() {
		return getValue().size();
	}

	public T remove(int index) {
		T removed = getValue().remove(index);
		checkEmpty();
		return removed;
	}

	public void clear() {
		getValue().clear();
		checkEmpty();
	}

	public boolean contains(T t) {
		return getValue().contains(t);
	}

	public boolean containsAll(Collection<Tag> tags) {
		return getValue().containsAll(tags);
	}

	public void sort(Comparator<T> comparator) {
		getValue().sort(comparator);
	}

	@Override
	public Iterator<T> iterator() {
		return getValue().iterator();
	}

	@Override
	public void forEach(Consumer<? super T> action) {
		getValue().forEach(action);
	}

	public T set(int index, T t) {
		checkValue(t);
		return getValue().set(index, t);
	}

	/**
	 * Adds a Tag to this ListTag after the last index.
	 * @param t The element to be added.
	 * @throws IllegalArgumentException if this ListTag already contains a Tag of another type.
	 * */
	public void add(T t) {
		add(size(), t);
	}

	public void add(int index, T t) {
		checkValue(t);
		getValue().add(index, t);
		typeID = t.getID();
		typeClass = t.getClass();
	}

	public void addAll(Collection<T> t) {
		for (T tt : t) {
			add(tt);
		}
	}

	public void addAll(int index, Collection<T> t) {
		int i = 0;
		for (T tt : t) {
			add(index + i, tt);
			i++;
		}
	}

	@SuppressWarnings("unchecked")
	public void addBoolean(boolean value) {
		add((T) new ByteTag(value));
	}

	@SuppressWarnings("unchecked")
	public void addByte(byte value) {
		add((T) new ByteTag(value));
	}

	@SuppressWarnings("unchecked")
	public void addShort(short value) {
		add((T) new ShortTag(value));
	}

	@SuppressWarnings("unchecked")
	public void addInt(int value) {
		add((T) new IntTag(value));
	}

	@SuppressWarnings("unchecked")
	public void addLong(long value) {
		add((T) new LongTag(value));
	}

	@SuppressWarnings("unchecked")
	public void addFloat(float value) {
		add((T) new FloatTag(value));
	}

	@SuppressWarnings("unchecked")
	public void addDouble(byte value) {
		add((T) new DoubleTag(value));
	}

	@SuppressWarnings("unchecked")
	public void addString(String value) {
		add((T) new StringTag(checkNull(value)));
	}

	@SuppressWarnings("unchecked")
	public void addByteArray(byte[] value) {
		add((T) new ByteArrayTag(checkNull(value)));
	}

	@SuppressWarnings("unchecked")
	public void addIntArray(int[] value) {
		add((T) new IntArrayTag(checkNull(value)));
	}

	@SuppressWarnings("unchecked")
	public void addLongArray(long[] value) {
		add((T) new LongArrayTag(checkNull(value)));
	}

	public T get(int index) {
		return getValue().get(index);
	}

	@SuppressWarnings({"unchecked", "unused"})
	public <L extends Tag> ListTag<L> asTypedList(Class<L> type) {
		checkTypeClass(type);
		return (ListTag<L>) this;
	}

	@SuppressWarnings("unchecked")
	public ListTag<ByteTag> asByteTagList() {
		checkTypeClass(ByteTag.class);
		return (ListTag<ByteTag>) this;
	}

	@SuppressWarnings("unchecked")
	public ListTag<ShortTag> asShortTagList() {
		checkTypeClass(ShortTag.class);
		return (ListTag<ShortTag>) this;
	}

	@SuppressWarnings("unchecked")
	public ListTag<IntTag> asIntTagList() {
		checkTypeClass(IntTag.class);
		return (ListTag<IntTag>) this;
	}

	@SuppressWarnings("unchecked")
	public ListTag<LongTag> asLongTagList() {
		checkTypeClass(LongTag.class);
		return (ListTag<LongTag>) this;
	}

	@SuppressWarnings("unchecked")
	public ListTag<FloatTag> asFloatTagList() {
		checkTypeClass(FloatTag.class);
		return (ListTag<FloatTag>) this;
	}

	@SuppressWarnings("unchecked")
	public ListTag<DoubleTag> asDoubleTagList() {
		checkTypeClass(DoubleTag.class);
		return (ListTag<DoubleTag>) this;
	}

	@SuppressWarnings("unchecked")
	public ListTag<StringTag> asStringTagList() {
		checkTypeClass(StringTag.class);
		return (ListTag<StringTag>) this;
	}

	@SuppressWarnings("unchecked")
	public ListTag<ByteArrayTag> asByteArrayTagList() {
		checkTypeClass(ByteArrayTag.class);
		return (ListTag<ByteArrayTag>) this;
	}

	@SuppressWarnings("unchecked")
	public ListTag<IntArrayTag> asIntArrayTagList() {
		checkTypeClass(IntArrayTag.class);
		return (ListTag<IntArrayTag>) this;
	}

	@SuppressWarnings("unchecked")
	public ListTag<LongArrayTag> asLongArrayTagList() {
		checkTypeClass(LongArrayTag.class);
		return (ListTag<LongArrayTag>) this;
	}

	@SuppressWarnings("unchecked")
	public ListTag<ListTag<?>> asListTagList() {
		checkTypeClass(ListTag.class);
		return (ListTag<ListTag<?>>) this;
	}

	@SuppressWarnings("unchecked")
	public ListTag<CompoundTag> asCompoundTagList() {
		checkTypeClass(CompoundTag.class);
		return (ListTag<CompoundTag>) this;
	}

	@Override
	public void serializeValue(DataOutputStream dos, int depth) throws IOException {
		dos.writeByte(typeID);
		dos.writeInt(size());
		if (typeID != 0) {
			for (T t : getValue()) {
				t.serializeValue(dos, incrementDepth(depth));
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deserializeValue(DataInputStream dis, int depth) throws IOException {
		typeID = dis.readByte();
		int size = dis.readInt();
		if (typeID != 0) {
			setValue(new ArrayList<>(size));
			for (int i = 0; i < size; i++) {
				Tag tag = TagFactory.fromID(typeID);
				tag.deserializeValue(dis, incrementDepth(depth));
				add((T) tag);
			}
		}
		checkEmpty();
	}

	@Override
	public String valueToString(int depth) {
		StringBuilder sb = new StringBuilder("{\"type\":\"").append(typeClass.getSimpleName()).append("\",\"list\":[");
		for (int i = 0; i < size(); i++) {
			sb.append(i > 0 ? "," : "").append(get(i).toString(incrementDepth(depth)));
		}
		sb.append("]}");
		return sb.toString();
	}

	@Override
	public String valueToTagString(int depth) {
		StringBuilder sb = new StringBuilder("[");
		for (int i = 0; i < size(); i++) {
			sb.append(i > 0 ? "," : "").append(get(i).valueToTagString(incrementDepth(depth)));
		}
		sb.append("]");
		return sb.toString();
	}

	@Override
	public boolean equals(Object other) {
		if (!super.equals(other) || size() != ((ListTag<?>) other).size() || typeID != ((ListTag<?>) other).getTypeID()) {
			return false;
		}
		for (int i = 0; i < size(); i++) {
			if (!get(i).equals(((ListTag<?>) other).get(i))) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int compareTo(Tag<List<T>> o) {
		return Integer.compare(size(), o.getValue().size());
	}

	@SuppressWarnings("unchecked")
	@Override
	public ListTag<T> clone() {
		ListTag<T> copy = new ListTag<>();
		for (T t : getValue()) {
			copy.add((T) t.clone());
		}
		return copy;
	}

	private void checkValue(T t) {
		checkNull(t);
		if (typeID != 0 && t.getID() != typeID) {
			throw new IllegalArgumentException(String.format(
					"cannot add %s to ListTag<%s>",
					t.getClass().getSimpleName(), typeClass.getSimpleName()));
		}
	}

	private void checkTypeClass(Class<?> clazz) {
		if (typeClass != EndTag.class && clazz != typeClass) {
			throw new ClassCastException(String.format(
					"cannot cast ListTag<%s> to ListTag<%s>",
					typeClass.getSimpleName(), clazz.getSimpleName()));
		}
	}

	private void checkEmpty() {
		if (size() == 0) {
			typeID = 0;
			typeClass = EndTag.class;
		}
	}
}
