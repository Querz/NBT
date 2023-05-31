package net.querz.nbt;

import java.io.DataInput;
import java.io.IOException;

public interface TagReader<T extends Tag> {

	T read(DataInput in, int depth) throws IOException;

	TagTypeVisitor.ValueResult read(DataInput in, TagTypeVisitor visitor) throws IOException;

	void skip(DataInput in) throws IOException;

}
