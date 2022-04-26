package net.querz.mca.parsers;

import net.querz.nbt.CompoundTag;

public interface SectionParser extends DataParser<CompoundTag>, CachedParser {

	default CompoundTag getSectionAt(int y) {
		return getDataAt(y);
	}

	default void setSectionAt(int y, CompoundTag data) {
		setDataAt(y, data);
	}

	default CompoundTag getSectionAtBlock(int blockY) {
		return getDataAt(blockY >> 4);
	}

	default void setSectionAtBlock(int blockY, CompoundTag data) {
		setDataAt(blockY >> 4, data);
	}
}
