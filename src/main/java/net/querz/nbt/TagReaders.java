package net.querz.nbt;

public final class TagReaders {

	private static final TagReader<?>[] READERS = new TagReader[]{
		EndTag.READER,
		ByteTag.READER,
		ShortTag.READER,
		IntTag.READER,
		LongTag.READER,
		FloatTag.READER,
		DoubleTag.READER,
		ByteArrayTag.READER,
		StringTag.READER,
		ListTag.READER,
		CompoundTag.READER,
		IntArrayTag.READER,
		LongArrayTag.READER
	};

	public static TagReader<?> get(int id) {
		if (id > 12 || id < 0) {
			throw new IllegalArgumentException("invalid tag type " + id);
		}
		return READERS[id];
	}
}
