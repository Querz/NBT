package net.querz.mca.parsers;

import net.querz.nbt.CompoundTag;

public interface EntityParser extends DataParser<CompoundTag>, CachedParser {

	CompoundTag getEntityAt(double x, double y, double z);

	void setEntityAt(double x, double y, double z, CompoundTag data);

	CompoundTag removeEntityAt(double x, double y, double z);

	void clear();
}
