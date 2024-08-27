package net.querz.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static net.querz.nbt.Tag.Type.*;

public class ListTag extends CollectionTag<Tag> {

	private final List<Tag> value;
	private Type type;

	public ListTag() {
		this(null);
	}

	public ListTag(Type type) {
		this(new ArrayList<>(), type);
	}

	public ListTag(List<Tag> list, Type type) {
		Objects.requireNonNull(list);

		if (type == END) {
			throw new IllegalArgumentException("ListTag can not be of type END");
		}

		for (int i = 0; i < list.size(); i++) {
			Objects.requireNonNull(list.get(i));

			if (list.get(i).getType() != type) {
				throw new IllegalArgumentException("Incorrect tag type "+list.get(i).getType()+" at index "+i+" (expected "+type+")");
			}
		}

		value = list;
		this.type = type;
	}

	@Override
	public Tag get(int index) {
		return value.get(index);
	}

	@Override
	public Tag set(int index, Tag tag) {
		Objects.requireNonNull(tag);

		Tag old = value.get(index);
		if (!updateType(tag)) {
			throw new UnsupportedOperationException(String.format("trying to set tag of type %s in ListTag of %s", tag.getType(), type));
		}
		value.set(index, tag);
		return old;
	}

	@Override
	public void add(int index, Tag tag) {
		Objects.requireNonNull(tag);

		if (!updateType(tag)) {
			throw new UnsupportedOperationException(String.format("trying to add tag of type %s to ListTag of %s", tag.getType(), type));
		}
		value.add(index, tag);
	}


	// checks if all elements of this ListTag are contained in the other ListTag
	public boolean partOf(ListTag other) {
		if (other == null || type != other.type) {
			return false;
		}
		switch (type) {
			case COMPOUND:
				loop:
				for (CompoundTag tag : iterateType(CompoundTag.class)) {
					for (CompoundTag otherTag : other.iterateType(CompoundTag.class)) {
						if (tag.partOf(otherTag)) {
							continue loop;
						}
					}
					return false;
				}
				break;
			case LIST:
				loop:
				for (ListTag tag : iterateType(ListTag.class)) {
					for (ListTag otherTag : other.iterateType(ListTag.class)) {
						if (tag.partOf(otherTag)) {
							continue loop;
						}
					}
					return false;
				}
				break;
			default:
				loop:
				for (Tag tag : this) {
					for (Tag otherTag : other) {
						if (tag.equals(otherTag)) {
							continue loop;
						}
					}
					return false;
				}
				break;
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

	private boolean updateType(Tag tag) {
		if (type == null && tag.getType() != END) {
			type = tag.getType();
			return true;
		} else {
			return type == tag.getType();
		}
	}

	@Override
	public Tag remove(int index) {
		Tag old = value.remove(index);
		if (value.isEmpty()) {
			type = null;
		}
		return old;
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
		if (type != null) {
			out.writeByte(type.id);
		} else {
			out.writeByte(0);
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
			return other instanceof ListTag && value.equals(((ListTag) other).value);
		}
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	@Override
	public void clear() {
		value.clear();
		type = null;
	}

	private NumberTag getNumber(int index) {
		return (NumberTag) value.get(index);
	}

	public byte getByte(int index) {
		return getNumber(index).asByte();
	}

	public short getShort(int index) {
		return getNumber(index).asShort();
	}

	public int getInt(int index) {
		return getNumber(index).asInt();
	}

	public long getLong(int index) {
		return getNumber(index).asLong();
	}

	public float getFloat(int index) {
		return getNumber(index).asFloat();
	}

	public double getDouble(int index) {
		return getNumber(index).asDouble();
	}

	public String getString(int index) {
		return ((StringTag) value.get(index)).getValue();
	}

	public byte[] getByteArray(int index) {
		return ((ByteArrayTag) value.get(index)).getValue();
	}

	public int[] getIntArray(int index) {
		return ((IntArrayTag) value.get(index)).getValue();
	}

	public long[] getLongArray(int index) {
		return ((LongArrayTag) value.get(index)).getValue();
	}

	public CompoundTag getCompound(int index) {
		return (CompoundTag) value.get(index);
	}

	public ListTag getList(int index) {
		return (ListTag) value.get(index);
	}

	public boolean getBoolean(int index) {
		return getByte(index) != 0;
	}

	public byte getByteOrDefault(int index, byte def) {
		if (index >= 0 && index < value.size()) {
			Tag tag = value.get(index);
			if (tag instanceof NumberTag) {
				return ((NumberTag) tag).asByte();
			}
		}
		return def;
	}

	public short getShortOrDefault(int index, short def) {
		if (index >= 0 && index < value.size()) {
			Tag tag = value.get(index);
			if (tag instanceof NumberTag) {
				return ((NumberTag) tag).asShort();
			}
		}
		return def;
	}

	public int getIntOrDefault(int index, int def) {
		if (index >= 0 && index < value.size()) {
			Tag tag = value.get(index);
			if (tag instanceof NumberTag) {
				return ((NumberTag) tag).asInt();
			}
		}
		return def;
	}

	public long getLongOrDefault(int index, long def) {
		if (index >= 0 && index < value.size()) {
			Tag tag = value.get(index);
			if (tag instanceof NumberTag) {
				return ((NumberTag) tag).asLong();
			}
		}
		return def;
	}

	public float getFloatOrDefault(int index, float def) {
		if (index >= 0 && index < value.size()) {
			Tag tag = value.get(index);
			if (tag instanceof NumberTag) {
				return ((NumberTag) tag).asFloat();
			}
		}
		return def;
	}

	public double getDoubleOrDefault(int index, double def) {
		if (index >= 0 && index < value.size()) {
			Tag tag = value.get(index);
			if (tag instanceof NumberTag) {
				return ((NumberTag) tag).asDouble();
			}
		}
		return def;
	}

	public String getStringOrDefault(int index, String def) {
		if (index >= 0 && index < value.size()) {
			Tag tag = value.get(index);
			if (tag.getType() == STRING) {
				return ((StringTag) tag).getValue();
			}
		}
		return def;
	}

	public byte[] getByteArrayOrDefault(int index, byte[] def) {
		if (index >= 0 && index < value.size()) {
			Tag tag = value.get(index);
			if (tag.getType() == BYTE_ARRAY) {
				return ((ByteArrayTag) tag).getValue();
			}
		}
		return def;
	}

	public int[] getIntArrayOrDefault(int index, int[] def) {
		if (index >= 0 && index < value.size()) {
			Tag tag = value.get(index);
			if (tag.getType() == INT_ARRAY) {
				return ((IntArrayTag) tag).getValue();
			}
		}
		return def;
	}

	public long[] getLongArrayOrDefault(int index, long[] def) {
		if (index >= 0 && index < value.size()) {
			Tag tag = value.get(index);
			if (tag.getType() == LONG_ARRAY) {
				return ((LongArrayTag) tag).getValue();
			}
		}
		return def;
	}

	public CompoundTag getCompoundOrDefault(int index, CompoundTag def) {
		if (index >= 0 && index < value.size()) {
			Tag tag = value.get(index);
			if (tag.getType() == COMPOUND) {
				return (CompoundTag) tag;
			}
		}
		return def;
	}

	public ListTag getListOrDefault(int index, ListTag def) {
		if (index >= 0 && index < value.size()) {
			Tag tag = value.get(index);
			if (tag.getType() == LIST) {
				return (ListTag) tag;
			}
		}
		return def;
	}

	public <T extends Tag> List<T> iterateType(Class<T> tagClass) {
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

	public static final TagReader<ListTag> READER = new TagReader<ListTag>() {

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
				case RETURN:
					return TagTypeVisitor.ValueResult.RETURN;
				case BREAK:
					reader.skip(in);
					return visitor.visitContainerEnd();
				default:
					int i = 0;
					loop:
					for (; i < length; i++) {
						switch (visitor.visitElement(reader, i)) {
							case RETURN:
								return TagTypeVisitor.ValueResult.RETURN;
							case BREAK:
								reader.skip(in);
								break loop;
							case SKIP:
								reader.skip(in);
								break;
							case ENTER:
								switch (reader.read(in, visitor)) {
									case RETURN:
										return TagTypeVisitor.ValueResult.RETURN;
									case BREAK:
										break loop;
								}
								break;
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
