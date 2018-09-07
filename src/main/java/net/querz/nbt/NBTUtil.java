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

public class NBTUtil {

	private NBTUtil() {}

	public static void writeTag(Tag tag, String file) throws IOException {
		writeTag(tag, "", new File(file), true);
	}

	public static void writeTag(Tag tag, File file) throws IOException {
		writeTag(tag, "", file, true);
	}

	public static void writeTag(Tag tag, String file, boolean compressed) throws IOException {
		writeTag(tag, "", new File(file), compressed);
	}

	public static void writeTag(Tag tag, File file, boolean compressed) throws IOException {
		writeTag(tag, "", file, compressed);
	}

	public static void writeTag(Tag tag, String name, String file) throws IOException {
		writeTag(tag, name, new File(file), true);
	}

	public static void writeTag(Tag tag, String name, File file) throws IOException {
		writeTag(tag, name, file, true);
	}

	public static void writeTag(Tag tag, String name, String file, boolean compressed) throws IOException {
		writeTag(tag, name, new File(file), compressed);
	}

	public static void writeTag(Tag tag, String name, File file, boolean compressed) throws IOException {
		try (
				DataOutputStream dos = new DataOutputStream(
						compressed ? new GZIPOutputStream(new FileOutputStream(file)) : new FileOutputStream(file))
		) {
			tag.serialize(dos, name, 0);
		}
	}

	public static Tag readTag(String file) throws IOException {
		return readTag(new File(file));
	}

	public static Tag readTag(File file) throws IOException {
		try (DataInputStream dis = new DataInputStream(applyDecompression(new FileInputStream(file)))) {
			return Tag.deserialize(dis, 0);
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
