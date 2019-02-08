package net.querz.nbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
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
		try (
				DataOutputStream dos = new DataOutputStream(
						compressed ? new GZIPOutputStream(new FileOutputStream(file)) : new FileOutputStream(file))
		) {
			tag.serialize(dos, name, Tag.DEFAULT_MAX_DEPTH);
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
		try (DataInputStream dis = new DataInputStream(applyDecompression(new FileInputStream(file)))) {
			return Tag.deserialize(dis, Tag.DEFAULT_MAX_DEPTH);
		}
	}

	static InputStream applyDecompression(InputStream is) throws IOException {
		PushbackInputStream pbis = new PushbackInputStream(is, 2);
		int sig = (pbis.read() & 0xFF) + (pbis.read() << 8);
		pbis.unread(sig >> 8);
		pbis.unread(sig & 0xFF);
		if (sig == GZIPInputStream.GZIP_MAGIC) {
			return new GZIPInputStream(pbis);
		}
		return pbis;
	}
}
