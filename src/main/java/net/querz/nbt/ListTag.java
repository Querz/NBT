package net.querz.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static net.querz.nbt.Tag.Type.*;

public non-sealed class ListTag extends CollectionTag<Tag> {

	private final List<Tag> value = new ArrayList<>();
	private Type type;

	public ListTag() {
		this(null);
	}

	public ListTag(Type type) {
		this(List.of(), type);
	}

	public ListTag(Collection<Tag> list, Type type) {
		Objects.requireNonNull(list);
		assimilateType(type);
		addAll(list);
	}

	private void assimilateType(Type type) {
		if (type == END) {
			throw new IllegalArgumentException("ListTag can not contain tags of type END");
		} else if (this.type == null) {
			this.type = type;
		} else if (this.type != type) {
			throw new UnsupportedOperationException(String.format("incompatible tag type, ListTag is of type %s, got %s", this.type, type));
		}
	}

	@Override
	public Tag get(int index) {
		return value.get(index);
	}

	@Override
	public Tag set(int index, Tag tag) {
		Objects.requireNonNull(tag);
		assimilateType(tag.getType());

		Tag old = value.get(index);
		value.set(index, tag);
		return old;
	}

	@Override
	public void add(int index, Tag tag) {
		Objects.requireNonNull(tag);
		assimilateType(tag.getType());

		value.add(index, tag);
	}


	// checks if all elements of this ListTag are contained in the other ListTag
	public boolean partOf(ListTag other) {
		if (other == null || type != other.type) {
			return false;
		}
		switch (type) {
			case COMPOUND -> {
				loop:
				for (CompoundTag tag : iterateType(CompoundTag.class)) {
					for (CompoundTag otherTag : other.iterateType(CompoundTag.class)) {
						if (tag.partOf(otherTag)) {
							continue loop;
						}
					}
					return false;
				}
			}
			case LIST -> {
				loop:
				for (ListTag tag : iterateType(ListTag.class)) {
					for (ListTag otherTag : other.iterateType(ListTag.class)) {
						if (tag.partOf(otherTag)) {
							continue loop;
						}
					}
					return false;
				}
			}
			default -> {
				loop:
				for (Tag tag : this) {
					for (Tag otherTag : other) {
						if (tag.equals(otherTag)) {
							continue loop;
						}
					}
					return false;
				}
			}
		}
		return true;
	}

	public void addBoolean(boolean b) {
		add(ByteTag.valueOf(b));
	}

	public void addByte(byte b) {
		add(ByteTag.valueOf(b));
	}

	public void addShort(short s) {
		add(ShortTag.valueOf(s));
	}

	public void addInt(int i) {
		add(IntTag.valueOf(i));
	}

	public void addLong(long l) {
		add(LongTag.valueOf(l));
	}

	public void addFloat(float f) {
		add(FloatTag.valueOf(f));
	}

	public void addDouble(double d) {
		add(DoubleTag.valueOf(d));
	}

	public void addString(String s) {
		add(StringTag.valueOf(s));
	}

	public void addByteArray(byte[] b) {
		add(new ByteArrayTag(b));
	}

	public void addIntArray(int[] i) {
		add(new IntArrayTag(i));
	}

	public void addLongArray(long[] l) {
		add(new LongArrayTag(l));
	}

	@Override
	public Tag remove(int index) {
		return value.remove(index);
	}

	@Override
	public Type getElementType() {
		return type;
	}

	@Override
	public int size() {
		return value.size();
	}

	public boolean isEmpty() {
		return value.isEmpty();
	}

	@Override
	public void write(DataOutput out) throws IOException {
		if (value.size() > 0) {
			out.writeByte(type.id);
		} else {
			out.writeByte(END.id);
		}
		out.writeInt(value.size());
		for (Tag tag : value) {
			tag.write(out);
		}
	}

	@Override
	public ListTag copy() {
		List<Tag> copy = new ArrayList<>(value.size());
		value.forEach(t -> copy.add(t.copy()));
		return new ListTag(copy, type);
	}

	@Override
	public void accept(TagVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		} else {
			return other instanceof ListTag otherList && value.equals(otherList.value);
		}
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	@Override
	public void clear() {
		value.clear();
	}
	
	public Tag getTag(int index) {
		return value.get(index);
	}

	public ByteTag getByteTag(int index) {
		return (ByteTag) getTag(index);
	}

	public ShortTag getShortTag(int index) {
		return (ShortTag) getTag(index);
	}

	public IntTag getIntTag(int index) {
		return (IntTag) getTag(index);
	}

	public LongTag getLongTag(int index) {
		return (LongTag) getTag(index);
	}

	public FloatTag getFloatTag(int index) {
		return (FloatTag) getTag(index);
	}

	public DoubleTag getDoubleTag(int index) {
		return (DoubleTag) getTag(index);
	}

	public StringTag getStringTag(int index) {
		return (StringTag) getTag(index);
	}

	public ByteArrayTag getByteArrayTag(int index) {
		return (ByteArrayTag) getTag(index);
	}

	public IntArrayTag getIntArrayTag(int index) {
		return (IntArrayTag) getTag(index);
	}

	public LongArrayTag getLongArrayTag(int index) {
		return (LongArrayTag) getTag(index);
	}

	public NumberTag getNumberTag(int index) {
		return (NumberTag) getTag(index);
	}

	public byte getByte(int index) {
		return getNumberTag(index).asByte();
	}

	public short getShort(int index) {
		return getNumberTag(index).asShort();
	}

	public int getInt(int index) {
		return getNumberTag(index).asInt();
	}

	public long getLong(int index) {
		return getNumberTag(index).asLong();
	}

	public float getFloat(int index) {
		return getNumberTag(index).asFloat();
	}

	public double getDouble(int index) {
		return getNumberTag(index).asDouble();
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

	public CompoundTag getCompound(int index) {
		return (CompoundTag) value.get(index);
	}

	public ListTag getList(int index) {
		return (ListTag) value.get(index);
	}

	public Number getNumber(int index) {
		return getNumberTag(index).asNumber();
	}

	public boolean getBoolean(int index) {
		return getByte(index) != 0;
	}

	public Tag getTagOrNull(int index) {
		if (index < value.size()) {
			return value.get(index);
		} else {
			return null;
		}
	}

	public ByteTag getByteTagOrNull(int index) {
		return (ByteTag) getTagOrNull(index);
	}

	public ShortTag getShortTagOrNull(int index) {
		return (ShortTag) getTagOrNull(index);
	}

	public IntTag getIntTagOrNull(int index) {
		return (IntTag) getTagOrNull(index);
	}

	public LongTag getLongTagOrNull(int index) {
		return (LongTag) getTagOrNull(index);
	}

	public FloatTag getFloatTagOrNull(int index) {
		return (FloatTag) getTagOrNull(index);
	}

	public DoubleTag getDoubleTagOrNull(int index) {
		return (DoubleTag) getTagOrNull(index);
	}

	public StringTag getStringTagOrNull(int index) {
		return (StringTag) getTagOrNull(index);
	}

	public ByteArrayTag getByteArrayTagOrNull(int index) {
		return (ByteArrayTag) getTagOrNull(index);
	}

	public IntArrayTag getIntArrayTagOrNull(int index) {
		return (IntArrayTag) getTagOrNull(index);
	}

	public LongArrayTag getLongArrayTagOrNull(int index) {
		return (LongArrayTag) getTagOrNull(index);
	}

	public NumberTag getNumberTagOrNull(int index) {
		return (NumberTag) getTagOrNull(index);
	}

	public CompoundTag getCompoundOrNull(int index) {
		return (CompoundTag) getTagOrNull(index);
	}

	public ListTag getListOrNull(int index) {
		return (ListTag) getTagOrNull(index);
	}

	public byte getByteOrDefault(int index, byte def) {
		NumberTag tag = getNumberTagOrNull(index);
		return tag == null ? def : tag.asByte();
	}

	public short getShortOrDefault(int index, short def) {
		NumberTag tag = getNumberTagOrNull(index);
		return tag == null ? def : tag.asShort();
	}

	public int getIntOrDefault(int index, int def) {
		NumberTag tag = getNumberTagOrNull(index);
		return tag == null ? def : tag.asInt();
	}

	public long getLongOrDefault(int index, long def) {
		NumberTag tag = getNumberTagOrNull(index);
		return tag == null ? def : tag.asLong();
	}

	public float getFloatOrDefault(int index, float def) {
		NumberTag tag = getNumberTagOrNull(index);
		return tag == null ? def : tag.asFloat();
	}

	public double getDoubleOrDefault(int index, double def) {
		NumberTag tag = getNumberTagOrNull(index);
		return tag == null ? def : tag.asDouble();
	}

	public String getStringOrDefault(int index, String def) {
		StringTag tag = getStringTagOrNull(index);
		return tag == null ? def : tag.getValue();
	}

	public byte[] getByteArrayOrDefault(int index, byte[] def) {
		ByteArrayTag tag = getByteArrayTagOrNull(index);
		return tag == null ? def : tag.getValue();
	}

	public int[] getIntArrayOrDefault(int index, int[] def) {
		IntArrayTag tag = getIntArrayTagOrNull(index);
		return tag == null ? def : tag.getValue();
	}

	public long[] getLongArrayOrDefault(int index, long[] def) {
		LongArrayTag tag = getLongArrayTagOrNull(index);
		return tag == null ? def : tag.getValue();
	}

	public CompoundTag getCompoundOrDefault(int index, CompoundTag def) {
		CompoundTag tag = getCompoundOrNull(index);
		return tag == null ? def : tag;
	}

	public ListTag getListOrDefault(int index, ListTag def) {
		ListTag tag = getListOrNull(index);
		return tag == null ? def : tag;
	}

	public <T extends Tag> List<T> asList(Class<T> tagClass) {
		if (type != null && type.tagClass != tagClass) {
			throw new IllegalArgumentException("Incorrect tagClass "+tagClass.getName()+", list is of type "+type.tagClass.getName());
		}
		return new TypedListTag<>(tagClass);
	}

	private class TypedListTag<T extends Tag> extends AbstractList<T> {
		private final Class<T> tagClass;

		private TypedListTag(Class<T> tagClass) {
			this.tagClass = tagClass;
		}

		@Override
		public T get(int index) {
			return tagClass.cast(ListTag.this.value.get(index));
		}

		@Override
		public int size() {
			return ListTag.this.value.size();
		}

		@Override
		public T set(int index, T element) {
			Objects.requireNonNull(element);
			return tagClass.cast(ListTag.this.set(index, element));
		}

		@Override
		public void add(int index, T element) {
			Objects.requireNonNull(element);
			ListTag.this.add(index, element);
		}

		@Override
		public T remove(int index) {
			return tagClass.cast(ListTag.this.remove(index));
		}

	}

	public static final TagReader<ListTag> READER = new TagReader<>() {

		@Override
		public ListTag read(DataInput in, int depth) throws IOException {
			byte type = in.readByte();
			int length = in.readInt();
			if (type == END.id && length > 0) {
				throw new RuntimeException("missing type on ListTag");
			} else {
				TagReader<?> tagReader = valueOf(type).reader;
				List<Tag> list = new ArrayList<>(length);
				for (int i = 0; i < length; i++) {
					list.add(tagReader.read(in, depth + 1));
				}
				return new ListTag(list, type == 0 ? null : Type.valueOf(type));
			}
		}

		@Override
		public TagTypeVisitor.ValueResult read(DataInput in, TagTypeVisitor visitor) throws IOException {
			TagReader<?> reader = valueOf(in.readByte()).reader;
			int length = in.readInt();
			switch (visitor.visitList(reader, length)) {
				case RETURN -> {
					return TagTypeVisitor.ValueResult.RETURN;
				}
				case BREAK -> {
					reader.skip(in);
					return visitor.visitContainerEnd();
				}
				default -> {
					int i = 0;
					loop:
					for (; i < length; i++) {
						switch (visitor.visitElement(reader, i)) {
							case RETURN -> {
								return TagTypeVisitor.ValueResult.RETURN;
							}
							case BREAK -> {
								reader.skip(in);
								break loop;
							}
							case SKIP -> reader.skip(in);
							case ENTER -> {
								switch (reader.read(in, visitor)) {
									case RETURN -> {
										return TagTypeVisitor.ValueResult.RETURN;
									}
									case BREAK -> {
										break loop;
									}
								}
							}
						}
					}

					// skip remaining tags
					int remainingElements = length - 1 - i;
					if (remainingElements > 0) {
						for (int j = 0; j < remainingElements; j++) {
							skip(in);
						}
					}

					return visitor.visitContainerEnd();
				}
			}
		}

		@Override
		public void skip(DataInput in) throws IOException {
			TagReader<?> tagReader = valueOf(in.readByte()).reader;
			int length = in.readInt();
			for (int i = 0; i < length; i++) {
				tagReader.skip(in);
			}
		}
	};
}
