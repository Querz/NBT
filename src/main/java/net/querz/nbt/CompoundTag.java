package net.querz.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.*;

public class CompoundTag implements Tag, Iterable<Map.Entry<String, Tag>> {

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
			out.writeByte(tag.getID());
			if (tag.getID() != END) {
				out.writeUTF(key);
				tag.write(out);
			}
		}
		out.writeByte(END);
	}

	@Override
	public byte getID() {
		return COMPOUND;
	}

	@Override
	public TagType<?> getType() {
		return TYPE;
	}

	@Override
	public CompoundTag copy() {
		Map<String, Tag> copy = new HashMap<>();
		value.forEach((k, v) -> copy.put(k, v.copy()));
		return new CompoundTag(copy);
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
			return other instanceof CompoundTag && Objects.equals(value, ((CompoundTag) other).value);
		}
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	public int size() {
		return value.size();
	}

	public void clear() {
		value.clear();
	}

	public Tag put(String key, Tag tag) {
		return value.put(key, tag);
	}

	public void putByte(String key, byte b) {
		value.put(key, ByteTag.valueOf(b));
	}

	public void putShort(String key, short s) {
		value.put(key, ShortTag.valueOf(s));
	}

	public void putInt(String key, int i) {
		value.put(key, IntTag.valueOf(i));
	}

	public void putLong(String key, long l) {
		value.put(key, LongTag.valueOf(l));
	}

	public void putFloat(String key, float f) {
		value.put(key, FloatTag.valueOf(f));
	}

	public void putDouble(String key, double d) {
		value.put(key, DoubleTag.valueOf(d));
	}

	public void putString(String key, String s) {
		value.put(key, StringTag.valueOf(s));
	}

	public void putByteArray(String key, byte[] b) {
		value.put(key, new ByteArrayTag(b));
	}

	public void putIntArray(String key, int[] i) {
		value.put(key, new IntArrayTag(i));
	}

	public void putLongArray(String key, long[] l) {
		value.put(key, new LongArrayTag(l));
	}

	public void putBoolean(String key, boolean b) {
		value.put(key, ByteTag.valueOf(b));
	}

	public Tag get(String key) {
		return value.get(key);
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
		if (contains(key, NUMBER)) {
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

	public boolean contains(String key, int type) {
		Tag tag = value.get(key);
		byte t = tag == null ? END : tag.getID();
		if (t == type) {
			return true;
		} else if (type != NUMBER) {
			return false;
		} else {
			return t >= BYTE && t <= DOUBLE;
		}
	}

	public boolean containsKey(String key) {
		return value.containsKey(key);
	}

	public boolean containsValue(Tag value) {
		return this.value.containsValue(value);
	}

	public Tag remove(String key) {
		return value.remove(key);
	}

	public boolean isEmpty() {
		return value.isEmpty();
	}

	@Override
	public Iterator<Map.Entry<String, Tag>> iterator() {
		return value.entrySet().iterator();
	}

	public Set<String> keySet() {
		return value.keySet();
	}

	public static final TagType<CompoundTag> TYPE = new TagType<>() {

		@Override
		public CompoundTag read(DataInput in, int depth) throws IOException {
			if (depth > MAX_DEPTH) {
				throw new RuntimeException("tried to read NBT tag with too high complexity, depth > " + MAX_DEPTH);
			}
			Map<String, Tag> map = new HashMap<>();
			byte type;
			while ((type = in.readByte()) != END) {
				String key = in.readUTF();
				TagType<?> tagType = TagTypes.get(type);
				Tag tag = tagType.read(in, depth + 1);
				map.put(key, tag);
			}
			return new CompoundTag(map);
		}

		@Override
		public TagTypeVisitor.ValueResult read(DataInput in, TagTypeVisitor visitor) throws IOException {
			for (;;) {
				byte id;
				if ((id = in.readByte()) != END) {
					TagType<?> type = TagTypes.get(id);
					switch (visitor.visitEntry(type)) {
						case RETURN -> {
							return TagTypeVisitor.ValueResult.RETURN;
						}
						case BREAK -> {
							StringTag.skipUTF(in);
							type.skip(in);
						}
						case SKIP -> {
							StringTag.skipUTF(in);
							type.skip(in);
							continue;
						}
						default -> {
							String name = in.readUTF();
							switch (visitor.visitEntry(type, name)) {
								case RETURN -> {
									return TagTypeVisitor.ValueResult.RETURN;
								}
								case BREAK -> type.skip(in);
								case SKIP -> {
									type.skip(in);
									continue;
								}
								case ENTER -> {
									if (type.read(in, visitor) == TagTypeVisitor.ValueResult.RETURN) {
										return TagTypeVisitor.ValueResult.RETURN;
									} else {
										continue;
									}
								}
							}
						}
					}
				}

				// skip remaining tags
				if (id != END) {
					while ((id = in.readByte()) != END) {
						StringTag.skipUTF(in);
						TagTypes.get(id).skip(in);
					}
				}

				return visitor.visitContainerEnd();
			}
		}

		@Override
		public void skip(DataInput in) throws IOException {
			byte type;
			while ((type = in.readByte()) != END) {
				in.skipBytes(in.readUnsignedShort());
				TagTypes.get(type).skip(in);
			}
		}
	};
}
