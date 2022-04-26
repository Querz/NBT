package net.querz.nbt.io;

import net.querz.nbt.Tag;
import net.querz.nbt.io.snbt.SNBTTagVisitor;
import net.querz.nbt.io.snbt.SNBTTagVisitorWriter;

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
		new SNBTTagVisitorWriter(new PrintWriter(new FileOutputStream(file)), indent).visit(tag);
	}

	public String toString(Tag tag) {
		return new SNBTTagVisitor(indent).visit(tag);
	}
}
