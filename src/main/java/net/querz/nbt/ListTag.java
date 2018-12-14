package net.querz.nbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * ListTag represents a typed List in the nbt structure.
 * An empty ListTag is of type {@code EndTag}.
 * Having this in mind, ListTag will always act consistent with serialization
 * regarding its type. Therefore  will return {@code 0}
 * and {@link ListTag#getTypeClass()} will return {@code EndTag.class}
 * when called on an empty ListTag.
 * */
public class ListTag<T extends Tag<?>> extends Tag<List<T>> implements Iterable<T> {

	private Class<?> typeClass = null;

	private ListTag() {}
	
	/**
	 * <p>Creates a non-type-safe ListTag. Its element type will be set after the first 
	 * element was added.</p>
	 * 
	 * <p>This is an internal helper method for cases where the element type is not known 
	 * at construction time. Use {@link #ListTag(Class)} when the type is known.</p>
	 * 
	 * @return A new non-type-safe ListTag
	 */
	protected static ListTag<?> createUnchecked() {
		return new ListTag<>();
	}

	/**
	 * @param typeClass The exact class of the elements
	 * @throws IllegalArgumentException When {@code typeClass} is {@link EndTag}{@code .class}
	 * @throws NullPointerException When {@code typeClass} is {@code null}
	 */
	public ListTag(Class<? super T> typeClass) throws IllegalArgumentException, NullPointerException {
		if (typeClass == EndTag.class) {
			throw new IllegalArgumentException("cannot create ListTag with EndTag elements");
		}
		this.typeClass = checkNull(typeClass);
	}

	@Override
	public byte getID() {
		return 9;
	}

	public Class<?> getTypeClass() {
		return typeClass == null ? EndTag.class : typeClass;
	}

	@Override
	protected List<T> getEmptyValue() {
		return new ArrayList<>(3);
	}

	public int size() {
		return getValue().size();
	}

	public T remove(int index) {
		return getValue().remove(index);
	}

	public void clear() {
		getValue().clear();
	}

	public boolean contains(T t) {
		return getValue().contains(t);
	}

	public boolean containsAll(Collection<Tag<?>> tags) {
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
		return getValue().set(index, checkNull(t));
	}

	/**
	 * Adds a Tag to this ListTag after the last index.
	 * @param t The element to be added.
	 * */
	public void add(T t) {
		add(size(), t);
	}

	@SuppressWarnings("unchecked")
	public void add(int index, T t) {
		checkNull(t);
		getValue().add(index, t);
		if (typeClass == null) {
			typeClass = t.getClass();
		}
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

	public void addBoolean(boolean value) {
		addUnchecked(new ByteTag(value));
	}

	public void addByte(byte value) {
		addUnchecked(new ByteTag(value));
	}

	public void addShort(short value) {
		addUnchecked(new ShortTag(value));
	}

	public void addInt(int value) {
		addUnchecked(new IntTag(value));
	}

	public void addLong(long value) {
		addUnchecked(new LongTag(value));
	}

	public void addFloat(float value) {
		addUnchecked(new FloatTag(value));
	}

	public void addDouble(double value) {
		addUnchecked(new DoubleTag(value));
	}

	public void addString(String value) {
		addUnchecked(new StringTag(checkNull(value)));
	}

	public void addByteArray(byte[] value) {
		addUnchecked(new ByteArrayTag(checkNull(value)));
	}

	public void addIntArray(int[] value) {
		addUnchecked(new IntArrayTag(checkNull(value)));
	}

	public void addLongArray(long[] value) {
		addUnchecked(new LongArrayTag(checkNull(value)));
	}

	public T get(int index) {
		return getValue().get(index);
	}

	public int indexOf(T t) {
		return getValue().indexOf(t);
	}

	@SuppressWarnings("unchecked")
	public <L extends Tag<?>> ListTag<L> asTypedList(Class<L> type) {
		checkTypeClass(type);
		typeClass = type;
		return (ListTag<L>) this;
	}

	public ListTag<ByteTag> asByteTagList() {
		return asTypedList(ByteTag.class);
	}

	public ListTag<ShortTag> asShortTagList() {
		return asTypedList(ShortTag.class);
	}

	public ListTag<IntTag> asIntTagList() {
		return asTypedList(IntTag.class);
	}

	public ListTag<LongTag> asLongTagList() {
		return asTypedList(LongTag.class);
	}

	public ListTag<FloatTag> asFloatTagList() {
		return asTypedList(FloatTag.class);
	}

	public ListTag<DoubleTag> asDoubleTagList() {
		return asTypedList(DoubleTag.class);
	}

	public ListTag<StringTag> asStringTagList() {
		return asTypedList(StringTag.class);
	}

	public ListTag<ByteArrayTag> asByteArrayTagList() {
		return asTypedList(ByteArrayTag.class);
	}

	public ListTag<IntArrayTag> asIntArrayTagList() {
		return asTypedList(IntArrayTag.class);
	}

	public ListTag<LongArrayTag> asLongArrayTagList() {
		return asTypedList(LongArrayTag.class);
	}

	@SuppressWarnings("unchecked")
	public ListTag<ListTag<?>> asListTagList() {
		checkTypeClass(ListTag.class);
		typeClass = ListTag.class;
		return (ListTag<ListTag<?>>) this;
	}

	public ListTag<CompoundTag> asCompoundTagList() {
		return asTypedList(CompoundTag.class);
	}

	@Override
	public void serializeValue(DataOutputStream dos, int depth) throws IOException {
		dos.writeByte(TagFactory.idFromClass(getTypeClass()));
		dos.writeInt(size());
		if (size() != 0) {
			for (T t : getValue()) {
				t.serializeValue(dos, incrementDepth(depth));
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deserializeValue(DataInputStream dis, int depth) throws IOException {
		int typeID = dis.readByte();
		if (typeID != 0) {
			typeClass = TagFactory.classFromID(typeID);
		}
		int size = dis.readInt();
		if (size != 0) {
			setValue(new ArrayList<>(size));
			for (int i = 0; i < size; i++) {
				Tag<?> tag = TagFactory.fromID(typeID);
				tag.deserializeValue(dis, incrementDepth(depth));
				add((T) tag);
			}
		}
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
		if (this == other) {
			return true;
		}
		if (!super.equals(other) || size() != ((ListTag<?>) other).size() || typeClass != ((ListTag<?>) other).getTypeClass()) {
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
	public int hashCode() {
		return Objects.hash(getTypeClass().hashCode(), getValue().hashCode());
	}

	@Override
	public int compareTo(Tag<List<T>> o) {
		if (!(o instanceof ListTag)) {
			return 0;
		}
		return Integer.compare(size(), o.getValue().size());
	}

	@SuppressWarnings("unchecked")
	@Override
	public ListTag<T> clone() {
		ListTag<T> copy = new ListTag<>();
		// assure type safety for clone
		copy.typeClass = typeClass;
		for (T t : getValue()) {
			copy.add((T) t.clone());
		}
		return copy;
	}

	@SuppressWarnings("unchecked")
	private void addUnchecked(Tag<?> tag) {
		if (typeClass != null && typeClass != tag.getClass()) {
			throw new IllegalArgumentException(String.format(
					"cannot add %s to ListTag<%s>",
					tag.getClass().getSimpleName(), typeClass.getSimpleName()));
		}
		add(size(), (T) tag);
	}

	private void checkTypeClass(Class<?> clazz) {
		if (typeClass != null && typeClass != clazz) {
			throw new ClassCastException(String.format(
					"cannot cast ListTag<%s> to ListTag<%s>",
					typeClass.getSimpleName(), clazz.getSimpleName()));
		}
	}
}
