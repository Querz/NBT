package net.querz.mca.parsers;

import net.querz.nbt.CompoundTag;

public interface BlockEntityParser extends DataParser<CompoundTag>, CachedParser {

	CompoundTag getBlockEntityAt(int blockX, int blockY, int blockZ);

	void setBlockEntityAt(int blockX, int blockY, int blockZ, CompoundTag data);

	CompoundTag removeBlockEntityAt(int blockX, int blockY, int blockZ);

	void clear();
}
