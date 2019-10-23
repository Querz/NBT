package net.querz.nbt;

import com.google.common.io.LittleEndianDataInputStream;
import com.google.common.io.LittleEndianDataOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.io.UTFDataFormatException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Provides main functions to read Tags from and write Tags to nbt files.
 * This does not support reading or writing .mca files, use {@link net.querz.nbt.mca.MCAUtil} instead.
 * */
public final class NBTUtil {

	private NBTUtil() {}

	/**
	 * Calls {@link NBTUtil#writeTag(Tag, String, File, boolean)} with an empty name and GZIP compression.
	 * @see NBTUtil#writeTag(Tag, String, File, boolean)
	 * @param tag The tag to be written to the file.
	 * @param file The file to write {@code tag} into.
	 * @throws IOException If something during the serialization goes wrong.
	 * @throws NullPointerException If {@code tag}, {@code name} or {@code file} is {@code null}.
	 * @throws MaxDepthReachedException If the NBT structure exceeds {@link Tag#DEFAULT_MAX_DEPTH}.
	 */
	public static void writeTag(Tag<?> tag, String file) throws IOException {
		writeTag(tag, "", new File(file), true);
	}

	/**
	 * Calls {@link NBTUtil#writeTag(Tag, String, File, boolean)} with an empty name and GZIP compression.
	 * @see NBTUtil#writeTag(Tag, String, File, boolean)
	 * @param tag The tag to be written to the file.
	 * @param file The file to write {@code tag} into.
	 * @throws IOException If something during the serialization goes wrong.
	 * @throws NullPointerException If {@code tag}, {@code name} or {@code file} is {@code null}.
	 * @throws MaxDepthReachedException If the NBT structure exceeds {@link Tag#DEFAULT_MAX_DEPTH}.
	 */
	public static void writeTag(Tag<?> tag, File file) throws IOException {
		writeTag(tag, "", file, true);
	}

	/**
	 * Calls {@link NBTUtil#writeTag(Tag, String, File, boolean)} with an empty name.
	 * @see NBTUtil#writeTag(Tag, String, File, boolean)
	 * @param tag The tag to be written to the file.
	 * @param file The file to write {@code tag} into.
	 * @param compressed {@code true} if the file should be GZIP compressed, {@code false} if not.
	 * @throws IOException If something during the serialization goes wrong.
	 * @throws NullPointerException If {@code tag}, {@code name} or {@code file} is {@code null}.
	 * @throws MaxDepthReachedException If the NBT structure exceeds {@link Tag#DEFAULT_MAX_DEPTH}.
	 */
	public static void writeTag(Tag<?> tag, String file, boolean compressed) throws IOException {
		writeTag(tag, "", new File(file), compressed);
	}

	/**
	 * Calls {@link NBTUtil#writeTag(Tag, String, File, boolean)} with an empty name.
	 * @see NBTUtil#writeTag(Tag, String, File, boolean)
	 * @param tag The tag to be written to the file.
	 * @param file The file to write {@code tag} into.
	 * @param compressed {@code true} if the file should be GZIP compressed, {@code false} if not.
	 * @throws IOException If something during the serialization goes wrong.
	 * @throws NullPointerException If {@code tag}, {@code name} or {@code file} is {@code null}.
	 * @throws MaxDepthReachedException If the NBT structure exceeds {@link Tag#DEFAULT_MAX_DEPTH}.
	 * */
	public static void writeTag(Tag<?> tag, File file, boolean compressed) throws IOException {
		writeTag(tag, "", file, compressed);
	}

	/**
	 * @see NBTUtil#writeTag(Tag, String, File)
	 * @param tag The tag to be written to the file.
	 * @param name The name of the root tag.
	 * @param file The file to write {@code tag} into.
	 * @throws IOException If something during the serialization goes wrong.
	 * @throws NullPointerException If {@code tag}, {@code name} or {@code file} is {@code null}.
	 * @throws MaxDepthReachedException If the NBT structure exceeds {@link Tag#DEFAULT_MAX_DEPTH}.
	 */
	public static void writeTag(Tag<?> tag, String name, String file) throws IOException {
		writeTag(tag, name, new File(file), true);
	}

	/**
	 * Calls {@link NBTUtil#writeTag(Tag, String, String, boolean)} with GZIP compression.
	 * @see NBTUtil#writeTag(Tag, String, File, boolean)
	 * @param tag The tag to be written to the file.
	 * @param name The name of the root tag.
	 * @param file The file to write {@code tag} into.
	 * @throws IOException If something during the serialization goes wrong.
	 * @throws NullPointerException If {@code tag}, {@code name} or {@code file} is {@code null}.
	 * @throws MaxDepthReachedException If the NBT structure exceeds {@link Tag#DEFAULT_MAX_DEPTH}.
	 */
	public static void writeTag(Tag<?> tag, String name, File file) throws IOException {
		writeTag(tag, name, file, true);
	}

	/**
	 * @see NBTUtil#writeTag(Tag, String, String, boolean)
	 * @param tag The tag to be written to the file.
	 * @param name The name of the root tag.
	 * @param file The file to write {@code tag} into.
	 * @param compressed {@code true} if the file should be GZIP compressed, {@code false} if not.
	 * @throws IOException If something during the serialization goes wrong.
	 * @throws NullPointerException If {@code tag}, {@code name} or {@code file} is {@code null}.
	 * @throws MaxDepthReachedException If the NBT structure exceeds {@link Tag#DEFAULT_MAX_DEPTH}.
	 */
	public static void writeTag(Tag<?> tag, String name, String file, boolean compressed) throws IOException {
		writeTag(tag, name, new File(file), compressed);
	}

	/**
	 * Writes the Tag {@code tag} to File {@code file}. A {@code name} for the root tag can be specified,
	 * but will be ignored during deserialization. Also allows for optional GZIP compression.
	 * @param tag The tag to be written to the file.
	 * @param name The name of the root tag.
	 * @param file The file to write {@code tag} into.
	 * @param compressed {@code true} if the file should be GZIP compressed, {@code false} if not.
	 * @throws IOException If something during the serialization goes wrong.
	 * @throws NullPointerException If {@code tag}, {@code name} or {@code file} is {@code null}.
	 * @throws MaxDepthReachedException If the NBT structure exceeds {@link Tag#DEFAULT_MAX_DEPTH}.
	 */
	public static void writeTag(Tag<?> tag, String name, File file, boolean compressed) throws IOException {
		writeTag(tag, file, FileOptions.builder()
				.rootTagName(name)
				.compressionOption(compressed ? CompressionOption.GZIP : CompressionOption.NONE)
				.build());
	}

	public static void writeTag(Tag<?> tag, File file, FileOptions options) throws IOException {
		DataOutput dos = null;
		try {
			dos = options.write(file);
			options.headerWriter.writeHeader(dos);
			tag.serialize(dos, options.rootTagName, Tag.DEFAULT_MAX_DEPTH);
		}
		finally
		{
			if (dos instanceof OutputStream) {
				((OutputStream) dos).close();
			}
		}

	}

	/**
	 * @see NBTUtil#readTag(File)
	 * @param file The file to read a Tag from.
	 * @return The tag read from the file.
	 * @throws IOException If something during deserialization goes wrong.
	 * */
	public static Tag<?> readTag(String file) throws IOException {
		return readTag(new File(file));
	}

	/**
	 * Reads a Tag from the specified file. Automatically detects GZIP detection
	 * and decompresses the stream if necessary.
	 * @param file The file to read a Tag from.
	 * @return The tag read from the file.
	 * @throws IOException If something during deserialization goes wrong.
	 * @throws NullPointerException If {@code file} is {@code null}.
	 * @throws MaxDepthReachedException If the NBT structure exceeds {@link Tag#DEFAULT_MAX_DEPTH}.
	 * */
	public static Tag<?> readTag(File file) throws IOException {
		return readTag(file, FileOptions.builder().build());
	}

	public static Tag<?> readTag(File file, FileOptions options) throws IOException {
		DataInput dis = null;
		try {
			dis = options.read(file);
			options.headerReader.readHeader(dis);
			return Tag.deserialize(dis, Tag.DEFAULT_MAX_DEPTH);
		}
		finally
		{
			if (dis instanceof InputStream)	{
				((InputStream) dis).close();
			}
		}
	}

	public enum CompressionOption {
		NONE,
		GZIP {
			@Override
			InputStream read(InputStream is) throws IOException
			{
				return new GZIPInputStream(is);
			}

			@Override
			OutputStream write(OutputStream os) throws IOException
			{
				return new GZIPOutputStream(os);
			}
		},
		DETECT {
			@Override
			InputStream read(InputStream is) throws IOException
			{
				PushbackInputStream pbis = new PushbackInputStream(is, 2);
				int sig = (pbis.read() & 0xFF) + (pbis.read() << 8);
				pbis.unread(sig >> 8);
				pbis.unread(sig & 0xFF);
				if (sig == GZIPInputStream.GZIP_MAGIC) {
					return new GZIPInputStream(pbis);
				}
				return pbis;
			}

			@Override
			OutputStream write(OutputStream os)
			{
				throw new UnsupportedOperationException();
			}
		};

		InputStream read(InputStream is) throws IOException
		{
			return is;
		}

		OutputStream write(OutputStream os) throws IOException
		{
			return os;
		}
	}

	public interface HeaderReader {
		void readHeader(DataInput input) throws IOException;
	}

	public interface HeaderWriter {
		void writeHeader(DataOutput output) throws IOException;
	}

	public static class FileOptions {
		private final CompressionOption compressionOption;
		private final boolean isLittleEndian;
		private final String rootTagName;
		private final HeaderReader headerReader;
		private final HeaderWriter headerWriter;

		private FileOptions(Builder builder) {
			compressionOption = builder.compressionOption;
			isLittleEndian = builder.isLittleEndian;
			rootTagName = builder.rootTagName;
			headerReader = builder.headerReader;
			headerWriter = builder.headerWriter;
		}

		DataInput read(File file) throws IOException {
			InputStream is = new FileInputStream(file);
			is = compressionOption.read(is);
			if (isLittleEndian) {
				return new LittleEndianDataInputStream(is);
			}
			return new DataInputStream(is);
		}

		DataOutput write(File file) throws IOException {
			OutputStream os = new FileOutputStream(file);
			os = compressionOption.write(os);
			if (isLittleEndian) {
				return new LittleEndianDataOutputStream(os);
			}
			return new DataOutputStream(os);
		}

		public static Builder builder() {
			return new Builder();
		}

		public static class Builder {
			public CompressionOption compressionOption = CompressionOption.DETECT;
			public boolean isLittleEndian = false;
			public String rootTagName = "";
			public HeaderReader headerReader = input -> {};
			public HeaderWriter headerWriter = output -> {};

			private Builder() {}

			public FileOptions build() {
				return new FileOptions(this);
			}

			public Builder compressionOption(CompressionOption compressionOption) {
				this.compressionOption = compressionOption;
				return this;
			}

			public Builder isLittleEndian(boolean value) {
				this.isLittleEndian = value;
				return this;
			}

			public Builder rootTagName(String value) {
				this.rootTagName = value;
				return this;
			}

			public Builder headerReader(HeaderReader value) {
				headerReader = value;
				return this;
			}

			public Builder headerWriter(HeaderWriter value) {
				headerWriter = value;
				return this;
			}
		}
	}

	/**
	 * hacked version of writeUTF because in the little endian NBT format the string length
	 * is written as little endian short but existing Java libraries were always writing it
	 * as big endian
	 * @param str
	 * @param out
	 * @return
	 * @throws IOException
	 */
	static int writeUTF(String str, DataOutput out) throws IOException {
		int strlen = str.length();
		int utflen = 0;
		int c, count = 0;

		/* use charAt instead of copying String to char array */
		for (int i = 0; i < strlen; i++) {
			c = str.charAt(i);
			if ((c >= 0x0001) && (c <= 0x007F)) {
				utflen++;
			} else if (c > 0x07FF) {
				utflen += 3;
			} else {
				utflen += 2;
			}
		}

		if (utflen > 65535)
			throw new UTFDataFormatException(
					"encoded string too long: " + utflen + " bytes");

		byte[] bytearr = new byte[utflen];

		out.writeShort(utflen);

		int i=0;
		for (i=0; i<strlen; i++) {
			c = str.charAt(i);
			if (!((c >= 0x0001) && (c <= 0x007F))) break;
			bytearr[count++] = (byte) c;
		}

		for (;i < strlen; i++){
			c = str.charAt(i);
			if ((c >= 0x0001) && (c <= 0x007F)) {
				bytearr[count++] = (byte) c;

			} else if (c > 0x07FF) {
				bytearr[count++] = (byte) (0xE0 | ((c >> 12) & 0x0F));
				bytearr[count++] = (byte) (0x80 | ((c >>  6) & 0x3F));
				bytearr[count++] = (byte) (0x80 | ((c >>  0) & 0x3F));
			} else {
				bytearr[count++] = (byte) (0xC0 | ((c >>  6) & 0x1F));
				bytearr[count++] = (byte) (0x80 | ((c >>  0) & 0x3F));
			}
		}
		out.write(bytearr, 0, utflen);
		return utflen + 2;
	}

	static int serializedSize(Tag<?> tag) throws IOException
	{
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		tag.serialize(dataOutput, Tag.DEFAULT_MAX_DEPTH);
		return byteArrayOutputStream.size();
	}
}
