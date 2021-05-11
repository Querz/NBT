package net.querz.nbt.io;

import net.querz.nbt.tag.Tag;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.GZIPInputStream;

public final class NBTUtil {

	private NBTUtil() {}

	/**
	 * Writer helper that follows the builder pattern.
	 * <p>
	 * Usage example:
	 * <pre>{@code Writer.write()
	 *     .tag(nbtTag)
	 *     .littleEndian()
	 *     .compressed(false)
	 *     .to("file.schematic")}</pre>
	 */
	public static class Writer {

		private NamedTag tag;
		private boolean compressed = true;
		private boolean littleEndian = false;

		public Writer() {
		}

		public static Writer write() {
			return new Writer();
		}

		public Writer tag(NamedTag tag) {
			this.tag = tag;
			return this;
		}

		public Writer tag(Tag<?> tag) {
			this.tag = new NamedTag(null, tag);
			return this;
		}

		public Writer compressed(boolean compressed) {
			this.compressed = compressed;
			return this;
		}

		public Writer littleEndian() {
			this.littleEndian = true;
			return this;
		}

		public Writer bigEndian() {
			this.littleEndian = false;
			return this;
		}

		public void to(OutputStream os) throws IOException {
			if (tag == null)
				throw new IllegalStateException("tag must be set");
			if (os == null)
				throw new IllegalStateException("output must be set");

			new NBTSerializer(compressed, littleEndian).toStream(tag, os);
		}

		public void to(Path path) throws IOException {
			try (OutputStream os = new BufferedOutputStream(Files.newOutputStream(path))) {
				to(os);
			}
		}

		public void to(File file) throws IOException {
			to(file.toPath());
		}

		public void to(String file) throws IOException {
			to(Paths.get(file));
		}
	}

	/**
	 * Reader helper that follows the builder pattern.
	 * <p>
	 * Usage example:
	 * <pre>{@code Reader.read()
	 *     .littleEndian()
	 *     .from("file.schematic")}</pre>
	 */
	public static class Reader {

		private boolean littleEndian = false;

		public Reader() {
		}

		public static Reader read() {
			return new Reader();
		}

		public Reader littleEndian() {
			this.littleEndian = true;
			return this;
		}

		public Reader bigEndian() {
			this.littleEndian = false;
			return this;
		}

		public NamedTag from(InputStream is) throws IOException {
			return new NBTDeserializer(false/* ignored, will autodetect compression */, littleEndian)
					.fromStream(detectDecompression(is));
		}

		public NamedTag from(Path path) throws IOException {
			try (InputStream is = new BufferedInputStream(Files.newInputStream(path))) {
				return from(is);
			}
		}

		public NamedTag from(File file) throws IOException {
			return from(file.toPath());
		}

		public NamedTag from(String file) throws IOException {
			return from(Paths.get(file));
		}

		private static InputStream detectDecompression(InputStream is) throws IOException {
			PushbackInputStream pbis = new PushbackInputStream(is, 2);
			int signature = (pbis.read() & 0xFF) + (pbis.read() << 8);
			pbis.unread(signature >> 8);
			pbis.unread(signature & 0xFF);
			return signature == GZIPInputStream.GZIP_MAGIC ? new GZIPInputStream(pbis) : pbis;
		}
	}


	@Deprecated
	public static void write(NamedTag tag, File file, boolean compressed) throws IOException {
		Writer.write().tag(tag).compressed(compressed).to(file);
	}

	@Deprecated
	public static void write(NamedTag tag, String file, boolean compressed) throws IOException {
		Writer.write().tag(tag).compressed(compressed).to(file);
	}

	@Deprecated
	public static void write(NamedTag tag, File file) throws IOException {
		Writer.write().tag(tag).to(file);
	}

	@Deprecated
	public static void write(NamedTag tag, String file) throws IOException {
		Writer.write().tag(tag).to(file);
	}

	@Deprecated
	public static void write(Tag<?> tag, File file, boolean compressed) throws IOException {
		Writer.write().tag(tag).compressed(compressed).to(file);
	}

	@Deprecated
	public static void write(Tag<?> tag, String file, boolean compressed) throws IOException {
		Writer.write().tag(tag).compressed(compressed).to(file);
	}

	@Deprecated
	public static void write(Tag<?> tag, File file) throws IOException {
		Writer.write().tag(tag).to(file);
	}

	@Deprecated
	public static void write(Tag<?> tag, String file) throws IOException {
		Writer.write().tag(tag).to(file);
	}

	@Deprecated
	public static void writeLE(NamedTag tag, File file, boolean compressed) throws IOException {
		Writer.write().tag(tag).littleEndian().compressed(compressed).to(file);
	}

	@Deprecated
	public static void writeLE(NamedTag tag, String file, boolean compressed) throws IOException {
		Writer.write().tag(tag).littleEndian().compressed(compressed).to(file);
	}

	@Deprecated
	public static void writeLE(NamedTag tag, File file) throws IOException {
		Writer.write().tag(tag).littleEndian().to(file);
	}

	@Deprecated
	public static void writeLE(NamedTag tag, String file) throws IOException {
		Writer.write().tag(tag).littleEndian().to(file);
	}

	@Deprecated
	public static void writeLE(Tag<?> tag, File file, boolean compressed) throws IOException {
		Writer.write().tag(tag).littleEndian().compressed(compressed).to(file);
	}

	@Deprecated
	public static void writeLE(Tag<?> tag, String file, boolean compressed) throws IOException {
		Writer.write().tag(tag).littleEndian().compressed(compressed).to(file);
	}

	@Deprecated
	public static void writeLE(Tag<?> tag, File file) throws IOException {
		Writer.write().tag(tag).littleEndian().to(file);
	}

	@Deprecated
	public static void writeLE(Tag<?> tag, String file) throws IOException {
		Writer.write().tag(tag).littleEndian().to(file);
	}

	@Deprecated
	@SuppressWarnings("unused")
	public static NamedTag read(File file, boolean compressed) throws IOException {
		return Reader.read().from(file);
	}

	@Deprecated
	@SuppressWarnings("unused")
	public static NamedTag read(String file, boolean compressed) throws IOException {
		return Reader.read().from(file);
	}

	@Deprecated
	public static NamedTag read(File file) throws IOException {
		return Reader.read().from(file);
	}

	@Deprecated
	public static NamedTag read(String file) throws IOException {
		return Reader.read().from(file);
	}

	@Deprecated
	@SuppressWarnings("unused")
	public static NamedTag readLE(File file, boolean compressed) throws IOException {
		return Reader.read().littleEndian().from(file);
	}

	@Deprecated
	@SuppressWarnings("unused")
	public static NamedTag readLE(String file, boolean compressed) throws IOException {
		return Reader.read().littleEndian().from(file);
	}

	@Deprecated
	public static NamedTag readLE(File file) throws IOException {
		return Reader.read().littleEndian().from(file);
	}

	@Deprecated
	public static NamedTag readLE(String file) throws IOException {
		return Reader.read().littleEndian().from(file);
	}

}
