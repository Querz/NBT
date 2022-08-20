package net.querz.nbt.io.snbt;

import net.querz.nbt.Tag;

import java.io.*;

public final class SNBTWriter {

	private String indent = "";

	public SNBTWriter indent(String indent) {
		this.indent = indent;
		return this;
	}

	public void write(OutputStream out, Tag tag) throws IOException {
		new SNBTTagVisitorWriter(new PrintWriter(out), indent).visit(tag);
	}

	public void write(File file, Tag tag) throws IOException {
		write(new FileOutputStream(file), tag);
	}

	public String toString(Tag tag) {
		return new SNBTTagVisitor(indent).visit(tag);
	}
}
