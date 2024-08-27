package net.querz.nbt.io.snbt;

import net.querz.nbt.Tag;

import java.io.*;
import java.nio.charset.StandardCharsets;

public final class SNBTWriter {

	private String indent = "";

	public SNBTWriter indent(String indent) {
		this.indent = indent;
		return this;
	}

	public void write(OutputStream out, Tag tag) throws IOException {
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8), false);
		tag.accept(new SNBTWriterTagVisitor(writer, indent));
		writer.flush();
	}

	public void write(File file, Tag tag) throws IOException {
		write(new FileOutputStream(file), tag);
	}

	public String toString(Tag tag) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			write(baos, tag);
			return baos.toString(StandardCharsets.UTF_8.name());
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}
}
