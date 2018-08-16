package net.querz.nbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CompoundTag extends Tag<Map<String, Tag>> {

	public CompoundTag() {}

	public Tag remove(String key) {
		return getValue().remove(key);
	}

	public <C extends Tag> C get(String key, Class<C> type) {
		Tag t = getValue().get(key);
		if (t != null) {
			return type.cast(t);
		}
		return null;
	}

	public Tag get(String key) {
		return getValue().get(key);
	}

	public ByteTag getByteTag(String key) {
		return get(key, ByteTag.class);
	}

	public ShortTag getShortTag(String key) {
		return get(key, ShortTag.class);
	}

	public IntTag getIntTag(String key) {
		return get(key, IntTag.class);
	}

	public LongTag getLongTag(String key) {
		return get(key, LongTag.class);
	}

	public FloatTag getFloatTag(String key) {
		return get(key, FloatTag.class);
	}

	public DoubleTag getDoubleTag(String key) {
		return get(key, DoubleTag.class);
	}

	public StringTag getStringTag(String key) {
		return get(key, StringTag.class);
	}

	public ByteArrayTag getByteArrayTag(String key) {
		return get(key, ByteArrayTag.class);
	}

	public IntArrayTag getIntArrayTag(String key) {
		return get(key, IntArrayTag.class);
	}

	public LongArrayTag getLongArrayTag(String key) {
		return get(key, LongArrayTag.class);
	}

	public ListTag<?> getListTag(String key) {
		return get(key, ListTag.class);
	}

	public CompoundTag getCompoundTag(String key) {
		return get(key, CompoundTag.class);
	}

	public boolean getBoolean(String key) {
		Tag t = get(key);
		return t instanceof ByteTag && ((ByteTag) t).asByte() > 0;
	}

	public byte getByte(String key) {
		return getByteTag(key).asByte();
	}

	public short getShort(String key) {
		return getShortTag(key).asShort();
	}

	public int getInt(String key) {
		return getIntTag(key).asInt();
	}

	public long getLong(String key) {
		return getLongTag(key).asLong();
	}

	public float getFloat(String key) {
		return getFloatTag(key).asFloat();
	}

	public double getDouble(String key) {
		return getDoubleTag(key).asDouble();
	}

	public String getString(String key) {
		return getStringTag(key).getValue();
	}

	public byte[] getByteArray(String key) {
		return getByteArrayTag(key).getValue();
	}

	public int[] getIntArray(String key) {
		return getIntArrayTag(key).getValue();
	}

	public long[] getLongArray(String key) {
		return getLongArrayTag(key).getValue();
	}

	public Tag putBoolean(String key, boolean value) {
		return put(key, new ByteTag(value));
	}

	public Tag putByte(String key, byte value) {
		return put(key, new ByteTag(value));
	}

	public Tag putShort(String key, short value) {
		return put(key, new ShortTag(value));
	}

	public Tag putInt(String key, int value) {
		return put(key, new IntTag(value));
	}

	public Tag putLong(String key, long value) {
		return put(key, new LongTag(value));
	}

	public Tag putFloat(String key, float value) {
		return put(key, new FloatTag(value));
	}

	public Tag putDouble(String key, double value) {
		return put(key, new DoubleTag(value));
	}

	public Tag putString(String key, String value) {
		return put(key, new StringTag(checkNull(value)));
	}

	public Tag putByteArray(String key, byte[] value) {
		return put(key, new ByteArrayTag(checkNull(value)));
	}

	public Tag putIntArray(String key, int[] value) {
		return put(key, new IntArrayTag(checkNull(value)));
	}

	public Tag putLongArray(String key, long[] value) {
		return put(key, new LongArrayTag(checkNull(value)));
	}

	public Tag put(String key, Tag tag) {
		return getValue().put(key, checkNull(tag));
	}

	public int size() {
		return getValue().size();
	}

	public void clear() {
		getValue().clear();
	}

	@Override
	public byte getID() {
		return 10;
	}

	@Override
	public void serializeValue(DataOutputStream dos, int depth) throws IOException {
		for (Map.Entry<String, Tag> e : getValue().entrySet()) {
			dos.writeByte(e.getValue().getID());
			dos.writeUTF(e.getKey());
			e.getValue().serializeValue(dos, incrementDepth(depth));
		}
		new EndTag().serialize(dos, depth);
	}

	@Override
	public void deserializeValue(DataInputStream dis, int depth) throws IOException {
		for (;;) {
			String name = dis.readUTF();
			Tag tag = Tag.deserialize(dis, incrementDepth(depth));
			put(name, tag);
		}
	}

	@Override
	public String valueToTagString(int depth) {
		StringBuilder sb = new StringBuilder("{");
		boolean first = true;
		for (Map.Entry<String, Tag> e : getValue().entrySet()) {
			sb.append(first ? "" : ",")
					.append(escapeString(e.getKey(), true)).append(":")
					.append(e.getValue().valueToTagString(incrementDepth(depth)));
			first = false;
		}
		sb.append("}");
		return sb.toString();
	}

	@Override
	public String valueToString(int depth) {
		StringBuilder sb = new StringBuilder("{");
		boolean first = true;
		for (Map.Entry<String, Tag> e : getValue().entrySet()) {
			sb.append(first ? "" : ",")
					.append(escapeString(e.getKey(), false)).append(":")
					.append(e.getValue().toString(incrementDepth(depth)));
			first = false;
		}
		sb.append("}");
		return sb.toString();
	}

	@Override
	protected Map<String, Tag> getEmptyValue() {
		return new HashMap<>(8);
	}

	@Override
	public CompoundTag clone() {
		CompoundTag copy = new CompoundTag();
		for (Map.Entry<String, Tag> e : getValue().entrySet()) {
			copy.put(e.getKey(), e.getValue().clone());
		}
		return copy;
	}

	@Override
	public boolean valueEquals(Map<String, Tag> value) {
		if (size() != value.size()) {
			return false;
		}
		for (Map.Entry<String, Tag> e : getValue().entrySet()) {
			Tag v;
			if ((v = value.get(e.getKey())) == null || !e.getValue().equals(v)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int compareTo(Tag<Map<String, Tag>> o) {
		return Integer.compare(size(), o.getValue().size());
	}
}
