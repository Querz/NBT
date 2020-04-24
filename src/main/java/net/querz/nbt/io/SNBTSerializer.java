package net.querz.nbt.io;

import net.querz.io.StringSerializer;
import net.querz.nbt.tag.Tag;
import java.io.IOException;
import java.io.Writer;

public class SNBTSerializer implements StringSerializer<Tag<?>> {

	private final boolean pretty;

	public SNBTSerializer() {
		this(false);
	}

	public SNBTSerializer(boolean pretty) {
		this.pretty = pretty;
	}

	@Override
	public void toWriter(Tag<?> tag, Writer writer) throws IOException {
		toWriter(tag, writer, Tag.DEFAULT_MAX_DEPTH);
	}

	public void toWriter(Tag<?> tag, Writer writer, int maxDepth) throws IOException {
		SNBTWriter.write(tag, writer, maxDepth);
	}
}
