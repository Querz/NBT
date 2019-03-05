package net.querz.nbt.io;

import net.querz.nbt.Tag;

public interface Deserializer extends MaxDepthIO {

	Tag<?> read(int maxDepth);
}
