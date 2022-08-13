package net.querz.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.*;

public non-sealed class CompoundTag implements Tag, Iterable<Map.Entry<String, Tag>> {

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
		try {
			if (contains(key, NUMBER)) {
				return ((NumberTag) value.get(key)).asByte();
			}
		} catch (ClassCastException ex) {}
		return 0;
	}

	public short getShort(String key) {
		try {
			if (contains(key, NUMBER)) {
				return ((NumberTag) value.get(key)).asShort();
			}
		} catch (ClassCastException ex) {}
		return 0;
	}

	public int getInt(String key) {
		try {
			if (contains(key, NUMBER)) {
				return ((NumberTag) value.get(key)).asInt();
			}
		} catch (ClassCastException ex) {}
		return 0;
	}

	public long getLong(String key) {
		try {
			if (contains(key, NUMBER)) {
				return ((NumberTag) value.get(key)).asLong();
			}
		} catch (ClassCastException ex) {}
		return 0;
	}

	public float getFloat(String key) {
		try {
			if (contains(key, NUMBER)) {
				return ((NumberTag) value.get(key)).asFloat();
			}
		} catch (ClassCastException ex) {}
		return 0.0f;
	}

	public double getDouble(String key) {
		try {
			if (contains(key, NUMBER)) {
				return ((NumberTag) value.get(key)).asDouble();
			}
		} catch (ClassCastException ex) {}
		return 0.0;
	}

	public String getString(String key) {
		try {
			if (contains(key, STRING)) {
				return ((StringTag) value.get(key)).getValue();
			}
		} catch (ClassCastException ex) {}
		return "";
	}

	public byte[] getByteArray(String key) {
		try {
			if (contains(key, BYTE_ARRAY)) {
				return ((ByteArrayTag) value.get(key)).getValue();
			}
		} catch (ClassCastException ex) {}
		return new byte[0];
	}

	public int[] getIntArray(String key) {
		try {
			if (contains(key, INT_ARRAY)) {
				return ((IntArrayTag) value.get(key)).getValue();
			}
		} catch (ClassCastException ex) {}
		return new int[0];
	}

	public long[] getLongArray(String key) {
		try {
			if (contains(key, LONG_ARRAY)) {
				return ((LongArrayTag) value.get(key)).getValue();
			}
		} catch (ClassCastException ex) {}
		return new long[0];
	}

	public CompoundTag getCompound(String key) {
		try {
			if (contains(key, COMPOUND)) {
				return (CompoundTag) value.get(key);
			}
		} catch (ClassCastException ex) {}
		return new CompoundTag();
	}

	public ListTag getList(String key) {
		try {
			if (contains(key, LIST)) {
				return (ListTag) value.get(key);
			}
		} catch (ClassCastException ex) {}
		return new ListTag();
	}

	public boolean getBoolean(String key) {
		return getByte(key) != 0;
	}

	public Tag getOrDefault(String key, Tag def) {
		return value.getOrDefault(key, def);
	}

	public byte getByteOrDefault(String key, byte def) {
		try {
			if (contains(key, NUMBER)) {
				return ((NumberTag) value.get(key)).asByte();
			}
		} catch (ClassCastException ex) {}
		return def;
	}

	public short getShortOrDefault(String key, short def) {
		try {
			if (contains(key, NUMBER)) {
				return ((NumberTag) value.get(key)).asShort();
			}
		} catch (ClassCastException ex) {}
		return def;
	}

	public int getIntOrDefault(String key, int def) {
		try {
			if (contains(key, NUMBER)) {
				return ((NumberTag) value.get(key)).asInt();
			}
		} catch (ClassCastException ex) {}
		return def;
	}

	public long getLongOrDefault(String key, long def) {
		try {
			if (contains(key, NUMBER)) {
				return ((NumberTag) value.get(key)).asLong();
			}
		} catch (ClassCastException ex) {}
		return def;
	}

	public float getFloatOrDefault(String key, float def) {
		try {
			if (contains(key, NUMBER)) {
				return ((NumberTag) value.get(key)).asFloat();
			}
		} catch (ClassCastException ex) {}
		return def;
	}

	public double getDoubleOrDefault(String key, double def) {
		try {
			if (contains(key, NUMBER)) {
				return ((NumberTag) value.get(key)).asDouble();
			}
		} catch (ClassCastException ex) {}
		return def;
	}

	public String getStringOrDefault(String key, String def) {
		try {
			if (contains(key, STRING)) {
				return ((StringTag) value.get(key)).getValue();
			}
		} catch (ClassCastException ex) {}
		return def;
	}

	public byte[] getByteArrayOrDefault(String key, byte[] def) {
		try {
			if (contains(key, BYTE_ARRAY)) {
				return ((ByteArrayTag) value.get(key)).getValue();
			}
		} catch (ClassCastException ex) {}
		return def;
	}

	public int[] getIntArrayOrDefault(String key, int[] def) {
		try {
			if (contains(key, INT_ARRAY)) {
				return ((IntArrayTag) value.get(key)).getValue();
			}
		} catch (ClassCastException ex) {}
		return def;
	}

	public long[] getLongArrayOrDefault(String key, long[] def) {
		try {
			if (contains(key, LONG_ARRAY)) {
				return ((LongArrayTag) value.get(key)).getValue();
			}
		} catch (ClassCastException ex) {}
		return def;
	}

	public CompoundTag getCompoundOrDefault(String key, CompoundTag def) {
		try {
			if (contains(key, COMPOUND)) {
				return (CompoundTag) value.get(key);
			}
		} catch (ClassCastException ex) {}
		return def;
	}

	public ListTag getListOrDefault(String key, ListTag def) {
		try {
			if (contains(key, LIST)) {
				return (ListTag) value.get(key);
			}
		} catch (ClassCastException ex) {}
		return def;
	}

	public ByteTag getByteTag(String key) {
		try {
			if (contains(key, BYTE)) {
				return (ByteTag) value.get(key);
			}
		} catch (ClassCastException ex) {}
		return null;
	}

	public ShortTag getShortTag(String key) {
		try {
			if (contains(key, SHORT)) {
				return (ShortTag) value.get(key);
			}
		} catch (ClassCastException ex) {}
		return null;
	}

	public IntTag getIntTag(String key) {
		try {
			if (contains(key, INT)) {
				return (IntTag) value.get(key);
			}
		} catch (ClassCastException ex) {}
		return null;
	}

	public LongTag getLongTag(String key) {
		try {
			if (contains(key, LONG)) {
				return (LongTag) value.get(key);
			}
		} catch (ClassCastException ex) {}
		return null;
	}

	public FloatTag getFloatTag(String key) {
		try {
			if (contains(key, FLOAT)) {
				return (FloatTag) value.get(key);
			}
		} catch (ClassCastException ex) {}
		return null;
	}

	public DoubleTag getDoubleTag(String key) {
		try {
			if (contains(key, DOUBLE)) {
				return (DoubleTag) value.get(key);
			}
		} catch (ClassCastException ex) {}
		return null;
	}

	public StringTag getStringTag(String key) {
		try {
			if (contains(key, STRING)) {
				return (StringTag) value.get(key);
			}
		} catch (ClassCastException ex) {}
		return null;
	}

	public ByteArrayTag getByteArrayTag(String key) {
		try {
			if (contains(key, BYTE_ARRAY)) {
				return (ByteArrayTag) value.get(key);
			}
		} catch (ClassCastException ex) {}
		return null;
	}

	public IntArrayTag getIntArrayTag(String key) {
		try {
			if (contains(key, INT_ARRAY)) {
				return (IntArrayTag) value.get(key);
			}
		} catch (ClassCastException ex) {}
		return null;
	}

	public LongArrayTag getLongArrayTag(String key) {
		try {
			if (contains(key, LONG_ARRAY)) {
				return (LongArrayTag) value.get(key);
			}
		} catch (ClassCastException ex) {}
		return null;
	}

	public ListTag getListTag(String key) {
		try {
			if (contains(key, LIST)) {
				return (ListTag) value.get(key);
			}
		} catch (ClassCastException ex) {}
		return null;
	}

	public CompoundTag getCompoundTag(String key) {
		try {
			if (contains(key, COMPOUND)) {
				return (CompoundTag) value.get(key);
			}
		} catch (ClassCastException ex) {}
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
