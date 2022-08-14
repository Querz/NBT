package net.querz.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListTag extends CollectionTag<Tag> {

	private final List<Tag> value;
	private byte type;

	public ListTag() {
		this(new ArrayList<>(), END);
	}

	public ListTag(List<Tag> list, byte type) {
		Objects.requireNonNull(list);
		list.forEach(Objects::requireNonNull);

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
			throw new UnsupportedOperationException(String.format("trying to set tag of type %d in ListTag of %d", tag.getID(), type));
		}
		value.set(index, tag);
		return old;
	}

	@Override
	public void add(int index, Tag tag) {
		Objects.requireNonNull(tag);

		if (!updateType(tag)) {
			throw new UnsupportedOperationException(String.format("trying to add tag of type %d to ListTag of %d", tag.getID(), type));
		}
		value.add(index, tag);
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
		if (tag.getID() == END) {
			return false;
		} else if (type == END) {
			type = tag.getID();
			return true;
		} else {
			return type == tag.getID();
		}
	}

	@Override
	public Tag remove(int index) {
		Tag old = value.remove(index);
		if (value.isEmpty()) {
			type = END;
		}
		return old;
	}

	@Override
	public byte getElementType() {
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
		out.writeByte(type);
		out.writeInt(value.size());
		for (Tag tag : value) {
			tag.write(out);
		}
	}

	@Override
	public byte getID() {
		return LIST;
	}

	@Override
	public TagType<?> getType() {
		return TYPE;
	}

	@Override
	public ListTag copy() {
		List<Tag> copy = new ArrayList<>(value.size());
		value.forEach(t -> copy.add(t.copy()));
		return new ListTag(copy, type);
	}

	@Override
	public void accept(TagVisitor visitor) throws Exception {
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
		type = END;
	}

	public byte getByte(int index) {
		if (index >= 0 && index < value.size()) {
			Tag tag = value.get(index);
			if (tag instanceof NumberTag) {
				return ((NumberTag) tag).asByte();
			}
		}
		return 0;
	}

	public short getShort(int index) {
		if (index >= 0 && index < value.size()) {
			Tag tag = value.get(index);
			if (tag instanceof NumberTag) {
				return ((NumberTag) tag).asShort();
			}
		}
		return 0;
	}

	public int getInt(int index) {
		if (index >= 0 && index < value.size()) {
			Tag tag = value.get(index);
			if (tag instanceof NumberTag) {
				return ((NumberTag) tag).asInt();
			}
		}
		return 0;
	}

	public long getLong(int index) {
		if (index >= 0 && index < value.size()) {
			Tag tag = value.get(index);
			if (tag instanceof NumberTag) {
				return ((NumberTag) tag).asLong();
			}
		}
		return 0;
	}

	public float getFloat(int index) {
		if (index >= 0 && index < value.size()) {
			Tag tag = value.get(index);
			if (tag instanceof NumberTag) {
				return ((NumberTag) tag).asFloat();
			}
		}
		return 0.0f;
	}

	public double getDouble(int index) {
		if (index >= 0 && index < value.size()) {
			Tag tag = value.get(index);
			if (tag instanceof NumberTag) {
				return ((NumberTag) tag).asDouble();
			}
		}
		return 0.0;
	}

	public String getString(int index) {
		if (index >= 0 && index < value.size()) {
			Tag tag = value.get(index);
			if (tag.getID() == STRING) {
				return ((StringTag) tag).getValue();
			}
		}
		return "";
	}

	public byte[] getByteArray(int index) {
		if (index >= 0 && index < value.size()) {
			Tag tag = value.get(index);
			if (tag.getID() == BYTE_ARRAY) {
				return ((ByteArrayTag) tag).getValue();
			}
		}
		return new byte[0];
	}

	public int[] getIntArray(int index) {
		if (index >= 0 && index < value.size()) {
			Tag tag = value.get(index);
			if (tag.getID() == INT_ARRAY) {
				return ((IntArrayTag) tag).getValue();
			}
		}
		return new int[0];
	}

	public long[] getLongArray(int index) {
		if (index >= 0 && index < value.size()) {
			Tag tag = value.get(index);
			if (tag.getID() == LONG_ARRAY) {
				return ((LongArrayTag) tag).getValue();
			}
		}
		return new long[0];
	}

	public CompoundTag getCompound(int index) {
		if (index >= 0 && index < value.size()) {
			Tag tag = value.get(index);
			if (tag.getID() == COMPOUND) {
				return (CompoundTag) tag;
			}
		}
		return new CompoundTag();
	}

	public ListTag getList(int index) {
		if (index >= 0 && index < value.size()) {
			Tag tag = value.get(index);
			if (tag.getID() == LIST) {
				return (ListTag) tag;
			}
		}
		return new ListTag();
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
			if (tag.getID() == STRING) {
				return ((StringTag) tag).getValue();
			}
		}
		return def;
	}

	public byte[] getByteArrayOrDefault(int index, byte[] def) {
		if (index >= 0 && index < value.size()) {
			Tag tag = value.get(index);
			if (tag.getID() == BYTE_ARRAY) {
				return ((ByteArrayTag) tag).getValue();
			}
		}
		return def;
	}

	public int[] getIntArrayOrDefault(int index, int[] def) {
		if (index >= 0 && index < value.size()) {
			Tag tag = value.get(index);
			if (tag.getID() == INT_ARRAY) {
				return ((IntArrayTag) tag).getValue();
			}
		}
		return def;
	}

	public long[] getLongArrayOrDefault(int index, long[] def) {
		if (index >= 0 && index < value.size()) {
			Tag tag = value.get(index);
			if (tag.getID() == LONG_ARRAY) {
				return ((LongArrayTag) tag).getValue();
			}
		}
		return def;
	}

	public CompoundTag getCompoundOrDefault(int index, CompoundTag def) {
		if (index >= 0 && index < value.size()) {
			Tag tag = value.get(index);
			if (tag.getID() == COMPOUND) {
				return (CompoundTag) tag;
			}
		}
		return def;
	}

	public ListTag getListOrDefault(int index, ListTag def) {
		if (index >= 0 && index < value.size()) {
			Tag tag = value.get(index);
			if (tag.getID() == LIST) {
				return (ListTag) tag;
			}
		}
		return def;
	}

	public <T extends Tag> List<T> iterateType(TagType<T> type) {
		return new TypedListTag<>();
	}

	private class TypedListTag<T extends Tag> extends AbstractList<T> {

		@SuppressWarnings("unchecked")
		@Override
		public T get(int index) {
			return (T) ListTag.this.value.get(index);
		}

		@Override
		public int size() {
			return ListTag.this.value.size();
		}

		@SuppressWarnings("unchecked")
		@Override
		public T set(int index, T element) {
			Objects.requireNonNull(element);
			return (T) ListTag.this.set(index, element);
		}

		@Override
		public void add(int index, T element) {
			Objects.requireNonNull(element);
			ListTag.this.add(index, element);
		}

		@SuppressWarnings("unchecked")
		@Override
		public T remove(int index) {
			return (T) ListTag.this.remove(index);
		}
	}

	public static final TagType<ListTag> TYPE = new TagType<>() {

		@Override
		public ListTag read(DataInput in, int depth) throws IOException {
			byte type = in.readByte();
			int length = in.readInt();
			if (type == END && length > 0) {
				throw new RuntimeException("missing type on ListTag");
			} else {
				TagType<?> tagType = TagTypes.get(type);
				List<Tag> list = new ArrayList<>(length);
				for (int i = 0; i < length; i++) {
					list.add(tagType.read(in, depth + 1));
				}
				return new ListTag(list, type);
			}
		}

		@Override
		public TagTypeVisitor.ValueResult read(DataInput in, TagTypeVisitor visitor) throws IOException {
			TagType<?> type = TagTypes.get(in.readByte());
			int length = in.readInt();
			switch (visitor.visitList(type, length)) {
				case RETURN -> {
					return TagTypeVisitor.ValueResult.RETURN;
				}
				case BREAK -> {
					type.skip(in);
					return visitor.visitContainerEnd();
				}
				default -> {
					int i = 0;
					loop:
					for (; i < length; i++) {
						switch (visitor.visitElement(type, i)) {
							case RETURN -> {
								return TagTypeVisitor.ValueResult.RETURN;
							}
							case BREAK -> {
								type.skip(in);
								break loop;
							}
							case SKIP -> type.skip(in);
							case ENTER -> {
								switch (type.read(in, visitor)) {
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
			TagType<?> tagType = TagTypes.get(in.readByte());
			int length = in.readInt();
			for (int i = 0; i < length; i++) {
				tagType.skip(in);
			}
		}
	};
}
