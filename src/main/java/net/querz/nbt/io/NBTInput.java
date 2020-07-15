package net.querz.nbt.io;

import net.querz.nbt.tag.Tag;
import java.io.IOException;

public interface NBTInput {

	NamedTag readTag(int maxDepth) throws IOException;

	Tag<?> readRawTag(int maxDepth) throws IOException;
}
