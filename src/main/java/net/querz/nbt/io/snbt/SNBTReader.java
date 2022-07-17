package net.querz.nbt.io.snbt;

import net.querz.nbt.Tag;

import java.io.*;
import java.nio.charset.StandardCharsets;

public final class SNBTReader {

	private boolean ignoreTrailing;

	public SNBTReader ignoreTrailing(boolean ignoreTrailing) {
		this.ignoreTrailing = ignoreTrailing;
		return this;
	}

	public Tag read(String s) throws ParseException {
		return new SNBTParser(s).parse(ignoreTrailing);
	}

	public Tag read(InputStream in) throws IOException {
		String s = new String(in.readAllBytes(), StandardCharsets.UTF_8);
		return new SNBTParser(s).parse(ignoreTrailing);
	}

	public Tag read(File file) throws IOException {
		try (FileInputStream fis = new FileInputStream(file)) {
			return read(fis);
		}
	}
}
