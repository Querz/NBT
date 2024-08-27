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
		// InputStream.readAllBytes is only available in Java 9 and beyond
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		for (int length; (length = in.read(buffer)) != -1; ) {
			result.write(buffer, 0, length);
		}
		String s = result.toString(StandardCharsets.UTF_8.name());

		return new SNBTParser(s).parse(ignoreTrailing);
	}

	public Tag read(File file) throws IOException {
		try (FileInputStream fis = new FileInputStream(file)) {
			return read(fis);
		}
	}
}
