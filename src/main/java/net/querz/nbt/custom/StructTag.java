package net.querz.nbt.custom;

import net.querz.nbt.ByteArrayTag;
import net.querz.nbt.ByteTag;
import net.querz.nbt.CompoundTag;
import net.querz.nbt.DoubleTag;
import net.querz.nbt.FloatTag;
import net.querz.nbt.IntArrayTag;
import net.querz.nbt.IntTag;
import net.querz.nbt.ListTag;
import net.querz.nbt.LongArrayTag;
import net.querz.nbt.LongTag;
import net.querz.nbt.ShortTag;
import net.querz.nbt.StringTag;
import net.querz.nbt.Tag;
import net.querz.nbt.TagFactory;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class StructTag extends Tag<List<Tag<?>>> implements Iterable<Tag<?>>, Comparable<StructTag> {

	public static void register() {
		TagFactory.registerCustomTag(120, StructTag::new, StructTag.class);
	}

	public StructTag() {
		super(createEmptyValue());
	}

	private static List<Tag<?>> createEmptyValue() {
		return new ArrayList<>(3);
	}

	public int size() {
		return getValue().size();
	}

	public Tag<?> remove(int index) {
		return getValue().remove(index);
	}

	public boolean remove(Tag<?> tag) {
		return getValue().remove(tag);
	}

	public void clear() {
		getValue().clear();
	}

	public boolean contains(Tag<?> tag) {
		return getValue().contains(tag);
	}

	public boolean containsAll(Collection<Tag<?>> tags) {
		return getValue().containsAll(tags);
	}

	@Override
	public Iterator<Tag<?>> iterator() {
		return getValue().iterator();
	}

	@Override
	public void forEach(Consumer<? super Tag<?>> action) {
		getValue().forEach(action);
	}

	public <S extends Tag<?>> S get(int index, Class<S> type) {
		Tag<?> t = getValue().get(index);
		return type.cast(t);
	}

	public Tag<?> get(int index) {
		return getValue().get(index);
	}

	public ByteTag getByteTag(int index) {
		return get(index, ByteTag.class);
	}

	public ShortTag getShortTag(int index) {
		return get(index, ShortTag.class);
	}

	public IntTag getIntTag(int index) {
		return get(index, IntTag.class);
	}

	public LongTag getLongTag(int index) {
		return get(index, LongTag.class);
	}

	public FloatTag getFloatTag(int index) {
		return get(index, FloatTag.class);
	}

	public DoubleTag getDoubleTag(int index) {
		return get(index, DoubleTag.class);
	}

	public StringTag getStringTag(int index) {
		return get(index, StringTag.class);
	}

	public ByteArrayTag getByteArrayTag(int index) {
		return get(index, ByteArrayTag.class);
	}

	public IntArrayTag getIntArrayTag(int index) {
		return get(index, IntArrayTag.class);
	}

	public LongArrayTag getLongArrayTag(int index) {
		return get(index, LongArrayTag.class);
	}

	public ListTag<?> getListTag(int index) {
		return get(index, ListTag.class);
	}

	public CompoundTag getCompoundTag(int index) {
		return get(index, CompoundTag.class);
	}

	public boolean getBoolean(int index) {
		Tag<?> t = get(index);
		return t instanceof ByteTag && ((ByteTag) t).asByte() > 0;
	}

	public byte getByte(int index) {
		return getByteTag(index).asByte();
	}

	public short getShort(int index) {
		return getShortTag(index).asShort();
	}

	public int getInt(int index) {
		return getIntTag(index).asInt();
	}

	public long getLong(int index) {
		return getLongTag(index).asLong();
	}

	public float getFloat(int index) {
		return getFloatTag(index).asFloat();
	}

	public double getDouble(int index) {
		return getDoubleTag(index).asDouble();
	}

	public String getString(int index) {
		return getStringTag(index).getValue();
	}

	public byte[] getByteArray(int index) {
		return getByteArrayTag(index).getValue();
	}

	public int[] getIntArray(int index) {
		return getIntArrayTag(index).getValue();
	}

	public long[] getLongArray(int index) {
		return getLongArrayTag(index).getValue();
	}

	public Tag<?> set(int index, Tag<?> tag) {
		return getValue().set(index, Objects.requireNonNull(tag));
	}

	public void add(Tag<?> tag) {
		getValue().add(Objects.requireNonNull(tag));
	}

	public void add(int index, Tag<?> tag) {
		getValue().add(index, Objects.requireNonNull(tag));
	}

	public void addBoolean(boolean value) {
		add(new ByteTag(value));
	}
	
	public void addByte(byte value) {
		add(new ByteTag(value));
	}

	public void addShort(short value) {
		add(new ShortTag(value));
	}

	public void addInt(int value) {
		add(new IntTag(value));
	}

	public void addLong(long value) {
		add(new LongTag(value));
	}

	public void addFloat(float value) {
		add(new FloatTag(value));
	}

	public void addDouble(double value) {
		add(new DoubleTag(value));
	}

	public void addString(String value) {
		add(new StringTag(value));
	}

	public void addByteArray(byte[] value) {
		add(new ByteArrayTag(value));
	}

	public void addIntArray(int[] value) {
		add(new IntArrayTag(value));
	}

	public void addLongArray(long[] value) {
		add(new LongArrayTag(value));
	}

	@Override
	public void serializeValue(DataOutputStream dos, int maxDepth) throws IOException {
		dos.writeInt(size());
		for (Tag<?> tag : getValue()) {
			dos.writeByte(tag.getID());
			tag.serializeValue(dos, decrementMaxDepth(maxDepth));
		}
	}

	@Override
	public void deserializeValue(DataInputStream dis, int maxDepth) throws IOException {
		int size = dis.readInt();
		size = size < 0 ? 0 : size;
		setValue(new ArrayList<>(size));
		for (int i = 0; i < size; i++) {
			Tag<?> tag = TagFactory.fromID(dis.readByte());
			tag.deserializeValue(dis, decrementMaxDepth(maxDepth));
			add(tag);
		}
	}

	@Override
	public String valueToString(int maxDepth) {
		StringBuilder sb = new StringBuilder("[");
		for (int i = 0; i < size(); i++) {
			sb.append(i > 0 ? "," : "").append(get(i).toString(decrementMaxDepth(maxDepth)));
		}
		sb.append("]");
		return sb.toString();
	}

	@Override
	public String valueToTagString(int maxDepth) {
		StringBuilder sb = new StringBuilder("[");
		for (int i = 0; i < size(); i++) {
			sb.append(i > 0 ? "," : "").append(get(i).valueToTagString(decrementMaxDepth(maxDepth)));
		}
		sb.append("]");
		return sb.toString();
	}

	@Override
	public boolean equals(Object other) {
		if (!super.equals(other) || size() != ((StructTag) other).size()) {
			return false;
		}
		for (int i = 0; i < size(); i++) {
			if (!get(i).equals(((StructTag) other).get(i))) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		return getValue().hashCode();
	}

	@Override
	public int compareTo(StructTag o) {
		return Integer.compare(size(), o.size());
	}

	@Override
	public StructTag clone() {
		StructTag copy = new StructTag();
		for (Tag<?> tag : getValue()) {
			copy.add(tag.clone());
		}
		return copy;
	}
}
