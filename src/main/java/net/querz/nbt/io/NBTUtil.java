package net.querz.nbt.io;

import net.querz.nbt.tag.Tag;

import java.io.*;
import java.util.zip.GZIPInputStream;

public final class NBTUtil {

	private NBTUtil() {}

	/**
	 * Writes the value returned by {@link Tag#toString()} to the specified file.
	 * <p>Useful for looking at large data structures, sorry it's not pretty printed.</p>
	 */
	public static void debugWrite(Tag<?> tag, File file) throws IOException {
		try (PrintWriter pw = new PrintWriter(new FileOutputStream(file))) {
			pw.write(tag.toString());
		}
	}

	public static void write(NamedTag tag, File file, boolean compressed) throws IOException {
		try (FileOutputStream fos = new FileOutputStream(file)) {
			new NBTSerializer(compressed).toStream(tag, fos);
		}
	}

	public static void write(NamedTag tag, String file, boolean compressed) throws IOException {
		write(tag, new File(file), compressed);
	}

	public static void write(NamedTag tag, File file) throws IOException {
		write(tag, file, true);
	}

	public static void write(NamedTag tag, String file) throws IOException {
		write(tag, new File(file), true);
	}

	public static void write(Tag<?> tag, File file, boolean compressed) throws IOException {
		write(new NamedTag(null, tag), file, compressed);
	}

	public static void write(Tag<?> tag, String file, boolean compressed) throws IOException {
		write(new NamedTag(null, tag), new File(file), compressed);
	}

	public static void write(Tag<?> tag, File file) throws IOException {
		write(new NamedTag(null, tag), file, true);
	}

	public static void write(Tag<?> tag, String file) throws IOException {
		write(new NamedTag(null, tag), new File(file), true);
	}

	public static void writeLE(NamedTag tag, File file, boolean compressed) throws IOException {
		try (FileOutputStream fos = new FileOutputStream(file)) {
			new NBTSerializer(compressed, true).toStream(tag, fos);
		}
	}

	public static void writeLE(NamedTag tag, String file, boolean compressed) throws IOException {
		writeLE(tag, new File(file), compressed);
	}

	public static void writeLE(NamedTag tag, File file) throws IOException {
		writeLE(tag, file, true);
	}

	public static void writeLE(NamedTag tag, String file) throws IOException {
		writeLE(tag, new File(file), true);
	}

	public static void writeLE(Tag<?> tag, File file, boolean compressed) throws IOException {
		writeLE(new NamedTag(null, tag), file, compressed);
	}

	public static void writeLE(Tag<?> tag, String file, boolean compressed) throws IOException {
		writeLE(new NamedTag(null, tag), new File(file), compressed);
	}

	public static void writeLE(Tag<?> tag, File file) throws IOException {
		writeLE(new NamedTag(null, tag), file, true);
	}

	public static void writeLE(Tag<?> tag, String file) throws IOException {
		writeLE(new NamedTag(null, tag), new File(file), true);
	}

	public static NamedTag read(File file, boolean compressed) throws IOException {
		try (FileInputStream fis = new FileInputStream(file)) {
			return new NBTDeserializer(compressed).fromStream(fis);
		}
	}

	public static NamedTag read(String file, boolean compressed) throws IOException {
		return read(new File(file), compressed);
	}

	public static NamedTag read(File file) throws IOException {
		try (FileInputStream fis = new FileInputStream(file)) {
			return new NBTDeserializer(false).fromStream(detectDecompression(fis));
		}
	}

	public static NamedTag read(String file) throws IOException {
		return read(new File(file));
	}

	public static NamedTag readLE(File file, boolean compressed) throws IOException {
		try (FileInputStream fis = new FileInputStream(file)) {
			return new NBTDeserializer(compressed, true).fromStream(fis);
		}
	}

	public static NamedTag readLE(String file, boolean compressed) throws IOException {
		return readLE(new File(file), compressed);
	}

	public static NamedTag readLE(File file) throws IOException {
		try (FileInputStream fis = new FileInputStream(file)) {
			return new NBTDeserializer(false, true).fromStream(detectDecompression(fis));
		}
	}

	public static NamedTag readLE(String file) throws IOException {
		return readLE(new File(file));
	}

	private static InputStream detectDecompression(InputStream is) throws IOException {
		PushbackInputStream pbis = new PushbackInputStream(is, 2);
		int signature = (pbis.read() & 0xFF) + (pbis.read() << 8);
		pbis.unread(signature >> 8);
		pbis.unread(signature & 0xFF);
		if (signature == GZIPInputStream.GZIP_MAGIC) {
			return new GZIPInputStream(pbis);
		}
		return pbis;
	}
}
