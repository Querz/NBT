package net.querz.nbt;

public final class TagTypes {

	private static final TagType<?>[] TYPES = new TagType[]{
		EndTag.TYPE,
		ByteTag.TYPE,
		ShortTag.TYPE,
		IntTag.TYPE,
		LongTag.TYPE,
		FloatTag.TYPE,
		DoubleTag.TYPE,
		ByteArrayTag.TYPE,
		StringTag.TYPE,
		ListTag.TYPE,
		CompoundTag.TYPE,
		IntArrayTag.TYPE,
		LongArrayTag.TYPE
	};

	public static TagType<?> get(int id) {
		if (id > 12 || id < 0) {
			throw new IllegalArgumentException("invalid tag type " + id);
		}
		return TYPES[id];
	}
}
