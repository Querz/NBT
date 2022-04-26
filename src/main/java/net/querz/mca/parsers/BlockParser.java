package net.querz.mca.parsers;

import net.querz.nbt.CompoundTag;

public interface BlockParser<T> extends DataParser<T>, CachedParser {

	CompoundTag getBlockAt(int blockX, int blockY, int blockZ);

	void setBlockAt(int blockX, int blockY, int blockZ, T data);
}
