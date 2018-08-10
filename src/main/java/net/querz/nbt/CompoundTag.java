package net.querz.nbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CompoundTag extends Tag<Map<String, Tag>> {

	public CompoundTag() {}

	public CompoundTag(String name) {
		super(name);
	}

	public <C extends Tag> C get(String key, Class<C> type) {
		Tag t = getValue().get(key);
		if (t != null) {
			return type.cast(t);
		}
		return null;
	}

	public Tag remove(String key) {
		return getValue().remove(key);
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
		return put(new ByteTag(key, (byte) (value ? 1 : 0)));
	}

	public Tag putByte(String key, byte value) {
		return put(new ByteTag(key, value));
	}

	public Tag putShort(String key, short value) {
		return put(new ShortTag(key, value));
	}

	public Tag putInt(String key, int value) {
		return put(new IntTag(key, value));
	}

	public Tag putLong(String key, long value) {
		return put(new LongTag(key, value));
	}

	public Tag putFloat(String key, float value) {
		return put(new FloatTag(key, value));
	}

	public Tag putDouble(String key, double value) {
		return put(new DoubleTag(key, value));
	}

	public Tag putString(String key, String value) {
		return put(new StringTag(key, checkNull(value)));
	}

	public Tag putByteArray(String key, byte[] value) {
		return put(new ByteArrayTag(key, checkNull(value)));
	}

	public Tag putIntArray(String key, int[] value) {
		return put(new IntArrayTag(key, checkNull(value)));
	}

	public Tag putLongArray(String key, long[] value) {
		return put(new LongArrayTag(key, checkNull(value)));
	}

	public Tag put(Tag tag) {
		return getValue().put(tag.getName(), checkNull(tag));
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
		for (Tag tag : getValue().values()) {
			tag.serialize(dos, incrementDepth(depth));
		}
		new EndTag().serialize(dos, depth);
	}

	@Override
	public void deserializeValue(DataInputStream dis, int depth) throws IOException {
		for (;;) {
			Tag tag = Tag.deserialize(dis, incrementDepth(depth));
			if (tag instanceof EndTag) {
				break;
			}
			put(tag);
		}
	}

	@Override
	public String valueToString(int depth) {
		StringBuilder sb = new StringBuilder("{");
		boolean first = true;
		for (Tag t : getValue().values()) {
			sb.append(first ? "" : ",").append(t.toString(incrementDepth(depth)));
			first = false;
		}
		sb.append("}");
		return sb.toString();
	}

	@Override
	protected Map<String, Tag> getEmptyValue() {
		return new HashMap<>(1);
	}

	@Override
	public CompoundTag clone() {
		CompoundTag copy = new CompoundTag(getName());
		for (Tag t : getValue().values()) {
			copy.put(t.clone());
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
}
