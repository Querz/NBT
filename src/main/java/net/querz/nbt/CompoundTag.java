package net.querz.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.*;

import static net.querz.nbt.Tag.Type.*;

public class CompoundTag implements Tag, Map<String, Tag>, Iterable<Map.Entry<String, Tag>> {

	private final Map<String, Tag> value;

	public CompoundTag(Map<String, Tag> map) {
		value = map;
	}

	public CompoundTag() {
		this(new HashMap<>());
	}

	@Override
	public void write(DataOutput out) throws IOException {
		for (String key : value.keySet()) {
			Tag tag = value.get(key);
			out.writeByte(tag.getType().id);
			if (tag.getType() != END) {
				out.writeUTF(key);
				tag.write(out);
			}
		}
		out.writeByte(END.id);
	}

	@Override
	public CompoundTag copy() {
		Map<String, Tag> copy = new HashMap<>();
		value.forEach((k, v) -> copy.put(k, v.copy()));
		return new CompoundTag(copy);
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
			return other instanceof CompoundTag && Objects.equals(value, ((CompoundTag) other).value);
		}
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	@Override
	public int size() {
		return value.size();
	}

	@Override
	public void clear() {
		value.clear();
	}

	@Override
	public Tag put(String key, Tag tag) {
		Objects.requireNonNull(key);
		Objects.requireNonNull(tag);
		if (tag.getType() == END) {
			throw new IllegalArgumentException("Can't insert end tag into CompoundTag");
		}
		return value.put(key, tag);
	}

	@Override
	public void putAll(Map<? extends String, ? extends Tag> m) {
		for (Map.Entry<? extends String, ? extends Tag> entry : m.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	public void putByte(String key, byte b) {
		put(key, ByteTag.valueOf(b));
	}

	public void putShort(String key, short s) {
		put(key, ShortTag.valueOf(s));
	}

	public void putInt(String key, int i) {
		put(key, IntTag.valueOf(i));
	}

	public void putLong(String key, long l) {
		put(key, LongTag.valueOf(l));
	}

	public void putFloat(String key, float f) {
		put(key, FloatTag.valueOf(f));
	}

	public void putDouble(String key, double d) {
		put(key, DoubleTag.valueOf(d));
	}

	public void putString(String key, String s) {
		put(key, StringTag.valueOf(s));
	}

	public void putByteArray(String key, byte[] b) {
		put(key, new ByteArrayTag(b));
	}

	public void putIntArray(String key, int[] i) {
		put(key, new IntArrayTag(i));
	}

	public void putLongArray(String key, long[] l) {
		put(key, new LongArrayTag(l));
	}

	public void putBoolean(String key, boolean b) {
		put(key, ByteTag.valueOf(b));
	}

	public Tag get(String key) {
		return value.get(key);
	}

	@Deprecated
	@Override
	public Tag get(Object key) {
		return get((String) key);
	}

	public byte getByte(String key) {
		NumberTag tag = getNumberTag(key);
		if (tag == null) {
			throw new NoSuchElementException("No numeric tag with key '"+key+"'");
		}
		return tag.asByte();
	}

	public short getShort(String key) {
		NumberTag tag = getNumberTag(key);
		if (tag == null) {
			throw new NoSuchElementException("No numeric tag with key '"+key+"'");
		}
		return tag.asShort();
	}

	public int getInt(String key) {
		NumberTag tag = getNumberTag(key);
		if (tag == null) {
			throw new NoSuchElementException("No numeric tag with key '"+key+"'");
		}
		return tag.asInt();
	}

	public long getLong(String key) {
		NumberTag tag = getNumberTag(key);
		if (tag == null) {
			throw new NoSuchElementException("No numeric tag with key '"+key+"'");
		}
		return tag.asLong();
	}

	public float getFloat(String key) {
		NumberTag tag = getNumberTag(key);
		if (tag == null) {
			throw new NoSuchElementException("No numeric tag with key '"+key+"'");
		}
		return tag.asFloat();
	}

	public double getDouble(String key) {
		NumberTag tag = getNumberTag(key);
		if (tag == null) {
			throw new NoSuchElementException("No numeric tag with key '"+key+"'");
		}
		return tag.asDouble();
	}

	public String getString(String key) {
		StringTag tag = getStringTag(key);
		if (tag == null) {
			throw new NoSuchElementException("No string tag with key '"+key+"'");
		}
		return tag.getValue();
	}

	public byte[] getByteArray(String key) {
		ByteArrayTag tag = getByteArrayTag(key);
		if (tag == null) {
			throw new NoSuchElementException("No byte array tag with key '"+key+"'");
		}
		return tag.getValue();
	}

	public int[] getIntArray(String key) {
		IntArrayTag tag = getIntArrayTag(key);
		if (tag == null) {
			throw new NoSuchElementException("No int array tag with key '"+key+"'");
		}
		return tag.getValue();
	}

	public long[] getLongArray(String key) {
		LongArrayTag tag = getLongArrayTag(key);
		if (tag == null) {
			throw new NoSuchElementException("No long array tag with key '"+key+"'");
		}
		return tag.getValue();
	}

	public CompoundTag getCompound(String key) {
		CompoundTag tag = getCompoundTag(key);
		if (tag == null) {
			throw new NoSuchElementException("No compound tag with key '"+key+"'");
		}
		return tag;
	}

	public ListTag getList(String key) {
		ListTag tag = getListTag(key);
		if (tag == null) {
			throw new NoSuchElementException("No byte array tag with key '"+key+"'");
		}
		return tag;
	}

	public boolean getBoolean(String key) {
		return getByte(key) != 0;
	}

	public Tag getOrDefault(String key, Tag def) {
		return value.getOrDefault(key, def);
	}

	public byte getByteOrDefault(String key, byte def) {
		NumberTag tag = getNumberTag(key);
		if (tag == null) {
			return def;
		}
		return tag.asByte();
	}

	public short getShortOrDefault(String key, short def) {
		NumberTag tag = getNumberTag(key);
		if (tag == null) {
			return def;
		}
		return tag.asShort();
	}

	public int getIntOrDefault(String key, int def) {
		NumberTag tag = getNumberTag(key);
		if (tag == null) {
			return def;
		}
		return tag.asInt();
	}

	public long getLongOrDefault(String key, long def) {
		NumberTag tag = getNumberTag(key);
		if (tag == null) {
			return def;
		}
		return tag.asLong();
	}

	public float getFloatOrDefault(String key, float def) {
		NumberTag tag = getNumberTag(key);
		if (tag == null) {
			return def;
		}
		return tag.asFloat();
	}

	public double getDoubleOrDefault(String key, double def) {
		NumberTag tag = getNumberTag(key);
		if (tag == null) {
			return def;
		}
		return tag.asDouble();
	}

	public String getStringOrDefault(String key, String def) {
		StringTag tag = getStringTag(key);
		if (tag == null) {
			return def;
		}
		return tag.getValue();
	}

	public byte[] getByteArrayOrDefault(String key, byte[] def) {
		ByteArrayTag tag = getByteArrayTag(key);
		if (tag == null) {
			return def;
		}
		return tag.getValue();
	}

	public int[] getIntArrayOrDefault(String key, int[] def) {
		IntArrayTag tag = getIntArrayTag(key);
		if (tag == null) {
			return def;
		}
		return tag.getValue();
	}

	public long[] getLongArrayOrDefault(String key, long[] def) {
		LongArrayTag tag = getLongArrayTag(key);
		if (tag == null) {
			return def;
		}
		return tag.getValue();
	}

	public CompoundTag getCompoundOrDefault(String key, CompoundTag def) {
		CompoundTag tag = getCompoundTag(key);
		if (tag == null) {
			return def;
		}
		return tag;
	}

	public ListTag getListOrDefault(String key, ListTag def) {
		ListTag tag = getListTag(key);
		if (tag == null) {
			return def;
		}
		return tag;
	}

	public boolean getBooleanOrDefault(String key, boolean b) {
		return getByteOrDefault(key, (byte)(b ? 1 : 0)) != 0;
	}

	public NumberTag getNumberTag(String key) {
		if (containsNumber(key)) {
			return (NumberTag) value.get(key);
		}
		return null;
	}

	public ByteTag getByteTag(String key) {
		if (contains(key, BYTE)) {
			return (ByteTag) value.get(key);
		}
		return null;
	}

	public ShortTag getShortTag(String key) {
		if (contains(key, SHORT)) {
			return (ShortTag) value.get(key);
		}
		return null;
	}

	public IntTag getIntTag(String key) {
		if (contains(key, INT)) {
			return (IntTag) value.get(key);
		}
		return null;
	}

	public LongTag getLongTag(String key) {
		if (contains(key, LONG)) {
			return (LongTag) value.get(key);
		}
		return null;
	}

	public FloatTag getFloatTag(String key) {
		if (contains(key, FLOAT)) {
			return (FloatTag) value.get(key);
		}
		return null;
	}

	public DoubleTag getDoubleTag(String key) {
		if (contains(key, DOUBLE)) {
			return (DoubleTag) value.get(key);
		}
		return null;
	}

	public StringTag getStringTag(String key) {
		if (contains(key, STRING)) {
			return (StringTag) value.get(key);
		}
		return null;
	}

	public ByteArrayTag getByteArrayTag(String key) {
		if (contains(key, BYTE_ARRAY)) {
			return (ByteArrayTag) value.get(key);
		}
		return null;
	}

	public IntArrayTag getIntArrayTag(String key) {
		if (contains(key, INT_ARRAY)) {
			return (IntArrayTag) value.get(key);
		}
		return null;
	}

	public LongArrayTag getLongArrayTag(String key) {
		if (contains(key, LONG_ARRAY)) {
			return (LongArrayTag) value.get(key);
		}
		return null;
	}

	public ListTag getListTag(String key) {
		if (contains(key, LIST)) {
			return (ListTag) value.get(key);
		}
		return null;
	}

	public CompoundTag getCompoundTag(String key) {
		if (contains(key, COMPOUND)) {
			return (CompoundTag) value.get(key);
		}
		return null;
	}

	public boolean contains(String key, Type type) {
		Tag tag = value.get(key);
		return tag != null && tag.getType() == type;
	}

	public boolean containsNumber(String key) {
		Tag tag = value.get(key);
		return tag != null && tag.getType().isNumber;
	}

	public boolean containsKey(String key) {
		return value.containsKey(key);
	}

	@Deprecated
	@Override
	public boolean containsKey(Object key) {
		return containsKey((String) key);
	}

	public boolean containsValue(Tag value) {
		return this.value.containsValue(value);
	}

	@Deprecated
	@Override
	public boolean containsValue(Object value) {
		return containsValue((Tag) value);
	}

	// checks if all elements of this CompoundTag are contained in the other CompoundTag
	public boolean partOf(CompoundTag other) {
		if (other == null) {
			return false;
		}
		for (Entry<String, Tag> entry : this) {
			if (!other.containsKey(entry.getKey())) {
				return false;
			}
			Tag tag = entry.getValue();
			switch (tag.getType()) {
				case COMPOUND:
					CompoundTag otherCompound = other.getCompoundTag(entry.getKey());
					if (!((CompoundTag) tag).partOf(otherCompound)) {
						return false;
					}
					break;
				case LIST:
					ListTag otherList = other.getListTag(entry.getKey());
					if (!((ListTag) tag).partOf(otherList)) {
						return false;
					}
					break;
				default:
					return tag.equals(other.get(entry.getKey()));
			}
		}
		return true;
	}

	public Tag remove(String key) {
		return value.remove(key);
	}

	@Deprecated
	@Override
	public Tag remove(Object key) {
		return remove((String) key);
	}

	@Override
	public boolean isEmpty() {
		return value.isEmpty();
	}

	@Override
	public Iterator<Map.Entry<String, Tag>> iterator() {
		return value.entrySet().iterator();
	}

	@Override
	public Set<String> keySet() {
		return value.keySet();
	}

	@Override
	public Collection<Tag> values() {
		return value.values();
	}

	@Override
	public Set<Entry<String, Tag>> entrySet() {
		return value.entrySet();
	}

	public static final TagReader<CompoundTag> READER = new TagReader<CompoundTag>() {

		@Override
		public CompoundTag read(DataInput in, int depth) throws IOException {
			if (depth > MAX_DEPTH) {
				throw new RuntimeException("tried to read NBT tag with too high complexity, depth > " + MAX_DEPTH);
			}
			Map<String, Tag> map = new HashMap<>();
			byte type;
			while ((type = in.readByte()) != END.id) {
				String key = in.readUTF();
				TagReader<?> tagReader = valueOf(type).reader;
				Tag tag = tagReader.read(in, depth + 1);
				map.put(key, tag);
			}
			return new CompoundTag(map);
		}

		@Override
		public TagTypeVisitor.ValueResult read(DataInput in, TagTypeVisitor visitor) throws IOException {
			for (;;) {
				byte id;
				if ((id = in.readByte()) != END.id) {
					TagReader<?> reader = valueOf(id).reader;
					switch (visitor.visitEntry(reader)) {
						case RETURN:
							return TagTypeVisitor.ValueResult.RETURN;
						case BREAK:
							StringTag.skipUTF(in);
							reader.skip(in);
							break;
						case SKIP:
							StringTag.skipUTF(in);
							reader.skip(in);
							continue;
						default:
							String name = in.readUTF();
							switch (visitor.visitEntry(reader, name)) {
								case RETURN:
									return TagTypeVisitor.ValueResult.RETURN;
								case BREAK:
									reader.skip(in);
									break;
								case SKIP:
									reader.skip(in);
									continue;
								case ENTER:
									if (reader.read(in, visitor) == TagTypeVisitor.ValueResult.RETURN) {
										return TagTypeVisitor.ValueResult.RETURN;
									} else {
										continue;
									}
							}
							break;
					}
				}

				// skip remaining tags
				if (id != END.id) {
					while ((id = in.readByte()) != END.id) {
						StringTag.skipUTF(in);
						valueOf(id).reader.skip(in);
					}
				}

				return visitor.visitContainerEnd();
			}
		}

		@Override
		public void skip(DataInput in) throws IOException {
			byte type;
			while ((type = in.readByte()) != END.id) {
				in.skipBytes(in.readUnsignedShort());
				valueOf(type).reader.skip(in);
			}
		}
	};
}
