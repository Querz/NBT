package net.querz.nbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class ListTag<T extends Tag> extends Tag<List<T>> {

	private byte typeID = 0;

	public ListTag() {}

	public ListTag(String name) {
		super(name);
	}

	private void checkValue(T t) {
		checkNull(t);
		if (typeID != 0 && t.getID() != typeID) {
			throw new IllegalArgumentException(String.format(
					"invalid element of type %d in ListTag: ListTag already has elements of type %d",
					t.getID(), typeID));
		}
	}

	private void checkEmpty() {
		if (size() == 0) {
			typeID = 0;
		}
	}

	public void add(T t) {
		add(size(), t);
	}

	public void add(int index, T t) {
		checkValue(t);
		getValue().add(index, t);
		typeID = t.getID();
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

	public T remove(int index) {
		T removed = getValue().remove(index);
		checkEmpty();
		return removed;
	}

	public boolean contains(T t) {
		return getValue().contains(t);
	}

	@SuppressWarnings({"unchecked", "unused"})
	public <L extends Tag> ListTag<L> asTypedList(ListTag<?> listTag, Class<L> type) {
		return (ListTag<L>) listTag;
	}

	@SuppressWarnings("unchecked")
	public ListTag<ByteTag> asByteTagList() {
		return (ListTag<ByteTag>) this;
	}

	@SuppressWarnings("unchecked")
	public ListTag<ShortTag> asShortTagList() {
		return (ListTag<ShortTag>) this;
	}

	@SuppressWarnings("unchecked")
	public ListTag<IntTag> asIntTagList() {
		return (ListTag<IntTag>) this;
	}

	@SuppressWarnings("unchecked")
	public ListTag<LongTag> asLongTagList() {
		return (ListTag<LongTag>) this;
	}

	@SuppressWarnings("unchecked")
	public ListTag<FloatTag> asFloatTagList() {
		return (ListTag<FloatTag>) this;
	}

	@SuppressWarnings("unchecked")
	public ListTag<DoubleTag> asDoubleTagList() {
		return (ListTag<DoubleTag>) this;
	}

	@SuppressWarnings("unchecked")
	public ListTag<StringTag> asStringTagList() {
		return (ListTag<StringTag>) this;
	}

	@SuppressWarnings("unchecked")
	public ListTag<ByteArrayTag> asByteArrayTagList() {
		return (ListTag<ByteArrayTag>) this;
	}

	@SuppressWarnings("unchecked")
	public ListTag<IntArrayTag> asIntArrayTagList() {
		return (ListTag<IntArrayTag>) this;
	}

	@SuppressWarnings("unchecked")
	public ListTag<LongArrayTag> asLongArrayTagList() {
		return (ListTag<LongArrayTag>) this;
	}

	@SuppressWarnings("unchecked")
	public ListTag<ListTag<?>> asListTagList() {
		return (ListTag<ListTag<?>>) this;
	}

	@SuppressWarnings("unchecked")
	public ListTag<CompoundTag> asCompoundTagList() {
		return (ListTag<CompoundTag>) this;
	}

	@SuppressWarnings("unchecked")
	public void addBoolean(boolean value) {
		add((T) new ByteTag("", (byte) (value ? 1 : 0)));
	}

	@SuppressWarnings("unchecked")
	public void addByte(byte value) {
		add((T) new ByteTag("", value));
	}

	@SuppressWarnings("unchecked")
	public void addShort(short value) {
		add((T) new ShortTag("", value));
	}

	@SuppressWarnings("unchecked")
	public void addInt(int value) {
		add((T) new IntTag("", value));
	}

	@SuppressWarnings("unchecked")
	public void addLong(long value) {
		add((T) new LongTag("", value));
	}

	@SuppressWarnings("unchecked")
	public void addFloat(float value) {
		add((T) new FloatTag("", value));
	}

	@SuppressWarnings("unchecked")
	public void addDouble(byte value) {
		add((T) new DoubleTag("", value));
	}

	@SuppressWarnings("unchecked")
	public void addString(String value) {
		add((T) new StringTag("", checkNull(value)));
	}

	@SuppressWarnings("unchecked")
	public void addByteArray(byte[] value) {
		add((T) new ByteArrayTag("", checkNull(value)));
	}

	@SuppressWarnings("unchecked")
	public void addIntArray(int[] value) {
		add((T) new IntArrayTag("", checkNull(value)));
	}

	@SuppressWarnings("unchecked")
	public void addLongArray(long[] value) {
		add((T) new LongArrayTag("", checkNull(value)));
	}

	public T get(int index) {
		return getValue().get(index);
	}

	public T set(int index, T t) {
		checkValue(t);
		return getValue().set(index, t);
	}

	public void clear() {
		getValue().clear();
		checkEmpty();
	}

	public void sort(Comparator<T> comparator) {
		getValue().sort(comparator);
	}

	public int size() {
		return getValue().size();
	}

	public byte getTypeID() {
		return typeID;
	}

	@Override
	public byte getID() {
		return 9;
	}

	@Override
	public void serializeValue(DataOutputStream dos, int depth) throws IOException {
		dos.writeByte(typeID);
		if (typeID != 0) {
			dos.writeInt(size());
			for (T t : getValue()) {
				t.serializeValue(dos, incrementDepth(depth));
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deserializeValue(DataInputStream dis, int depth) throws IOException {
		typeID = dis.readByte();
		if (typeID != 0) {
			int size = dis.readInt();
			setValue(new ArrayList<>(size));
			for (int i = 0; i < size; i++) {
				Tag tag = TagFactory.fromID(typeID);
				tag.deserializeValue(dis, incrementDepth(depth));
				add((T) tag);
			}
		}
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
	public String valueToString(int depth) {
		StringBuilder sb = new StringBuilder("[");
		for (int i = 0; i < size(); i++) {
			sb.append(i > 0 ? "," : "").append(get(i).toString(incrementDepth(depth)));
		}
		sb.append("]");
		return sb.toString();
	}

	@Override
	protected List<T> getEmptyValue() {
		return new ArrayList<>(3);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ListTag<T> clone() {
		ListTag<T> copy = new ListTag<>(getName());
		for (T t : getValue()) {
			copy.add((T) t.clone());
		}
		return copy;
	}

	@Override
	public boolean valueEquals(List<T> value) {
		if (size() != value.size()) {
			return false;
		}
		for (int i = 0; i < size(); i++) {
			if (!get(i).equals(value.get(i))) {
				return false;
			}
		}
		return true;
	}
}
