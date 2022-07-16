package net.querz.nbt.io;

import net.querz.nbt.NamedTag;
import net.querz.nbt.Tag;
import net.querz.io.LittleEndianOutputStream;

import java.io.*;
import java.util.zip.GZIPOutputStream;

public final class NBTWriter {

	private boolean compression;
	private boolean littleEndian;

	public NBTWriter compressed(boolean compressed) {
		compression = compressed;
		return this;
	}

	public NBTWriter littleEndian() {
		littleEndian = true;
		return this;
	}

	public NBTWriter bigEndian() {
		littleEndian = false;
		return this;
	}

	public void writeNamed(OutputStream out, NamedTag named) throws IOException {
		writeNamed(out, named.name(), named.tag());
	}

	public void write(OutputStream out, Tag tag) throws IOException {
		writeNamed(out, "", tag);
	}

	public void writeNamed(OutputStream out, String name, Tag tag) throws IOException {
		OutputStream stream;
		if (compression) {
			stream = new BufferedOutputStream(new GZIPOutputStream(out));
		} else {
			stream = new BufferedOutputStream(out);
		}

		DataOutput output;
		if (littleEndian) {
			output = new LittleEndianOutputStream(stream);
		} else {
			output = new DataOutputStream(stream);
		}

		output.writeByte(tag.getID());
		if (tag.getID() != Tag.END) {
			output.writeUTF(name);
			tag.write(output);
		}

		stream.flush();
	}

	public void writeNamed(File file, NamedTag named) throws IOException {
		try (FileOutputStream fos = new FileOutputStream(file)) {
			writeNamed(fos, named);
		}
	}

	public void write(File file, Tag tag) throws IOException {
		try (FileOutputStream fos = new FileOutputStream(file)) {
			write(fos, tag);
		}
	}

	public void writeNamed(File file, String name, Tag tag) throws IOException {
		try (FileOutputStream fos = new FileOutputStream(file)) {
			writeNamed(fos, name, tag);
		}
	}
}
