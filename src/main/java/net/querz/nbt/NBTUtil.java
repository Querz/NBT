package net.querz.nbt;

import net.querz.nbt.io.*;
import net.querz.nbt.io.snbt.ParseException;
import net.querz.nbt.io.snbt.SNBTReader;
import net.querz.nbt.io.snbt.SNBTWriter;
import net.querz.nbt.io.stream.SelectionStreamTagVisitor;
import net.querz.nbt.io.stream.TagSelector;

import java.io.*;

public final class NBTUtil {

	public static Tag read(File file) throws IOException {
		return new NBTReader().detectCompression().read(file);
	}

	public static Tag read(File file, boolean compressed) throws IOException {
		return new NBTReader().compressed(compressed).read(file);
	}

	public static Tag read(File file, TagSelector... selector) throws IOException {
		return new NBTReader().detectCompression().select(selector).read(file);
	}

	public static Tag read(File file, boolean compressed, TagSelector... selector) throws IOException {
		return new NBTReader().compressed(compressed).select(selector).read(file);
	}

	public static Tag readLE(File file) throws IOException {
		return new NBTReader().littleEndian().detectCompression().read(file);
	}

	public static Tag readLE(File file, boolean compressed) throws IOException {
		return new NBTReader().littleEndian().compressed(compressed).read(file);
	}

	public static Tag readLE(File file, TagSelector... selector) throws IOException {
		return new NBTReader().littleEndian().detectCompression().select(selector).read(file);
	}

	public static Tag readLE(File file, boolean compressed, TagSelector... selector) throws IOException {
		return new NBTReader().littleEndian().compressed(compressed).select(selector).read(file);
	}

	public static NamedTag readNamed(File file) throws IOException {
		return new NBTReader().detectCompression().readNamed(file);
	}

	public static NamedTag readNamed(File file, boolean compressed) throws IOException {
		return new NBTReader().compressed(compressed).readNamed(file);
	}

	public static NamedTag readNamed(File file, TagSelector... selector) throws IOException {
		return new NBTReader().detectCompression().select(selector).readNamed(file);
	}

	public static NamedTag readNamed(File file, boolean compressed, TagSelector... selector) throws IOException {
		return new NBTReader().compressed(compressed).select(selector).readNamed(file);
	}

	public static NamedTag readNamedLE(File file) throws IOException {
		return new NBTReader().littleEndian().detectCompression().readNamed(file);
	}

	public static NamedTag readNamedLE(File file, boolean compressed) throws IOException {
		return new NBTReader().littleEndian().compressed(compressed).readNamed(file);
	}

	public static NamedTag readNamedLE(File file, TagSelector... selector) throws IOException {
		return new NBTReader().littleEndian().detectCompression().select(selector).readNamed(file);
	}

	public static NamedTag readNamedLE(File file, boolean compressed, TagSelector... selector) throws IOException {
		return new NBTReader().littleEndian().compressed(compressed).select(selector).readNamed(file);
	}

	public static Tag read(InputStream stream) throws IOException {
		return new NBTReader().detectCompression().read(stream);
	}

	public static Tag read(InputStream stream, boolean compressed) throws IOException {
		return new NBTReader().compressed(compressed).read(stream);
	}

	public static Tag read(InputStream stream, TagSelector... selector) throws IOException {
		return new NBTReader().detectCompression().select(selector).read(stream);
	}

	public static Tag read(InputStream stream, boolean compressed, TagSelector... selector) throws IOException {
		return new NBTReader().compressed(compressed).select(selector).read(stream);
	}

	public static Tag readLE(InputStream stream) throws IOException {
		return new NBTReader().littleEndian().detectCompression().read(stream);
	}

	public static Tag readLE(InputStream stream, boolean compressed) throws IOException {
		return new NBTReader().littleEndian().compressed(compressed).read(stream);
	}

	public static Tag readLE(InputStream stream, TagSelector... selector) throws IOException {
		return new NBTReader().littleEndian().detectCompression().select(selector).read(stream);
	}

	public static Tag readLE(InputStream stream, boolean compressed, TagSelector... selector) throws IOException {
		return new NBTReader().littleEndian().compressed(compressed).select(selector).read(stream);
	}

	public static NamedTag readNamed(InputStream stream) throws IOException {
		return new NBTReader().detectCompression().readNamed(stream);
	}

	public static NamedTag readNamed(InputStream stream, boolean compressed) throws IOException {
		return new NBTReader().compressed(compressed).readNamed(stream);
	}

	public static NamedTag readNamed(InputStream stream, TagSelector... selector) throws IOException {
		return new NBTReader().detectCompression().select(selector).readNamed(stream);
	}

	public static NamedTag readNamed(InputStream stream, boolean compressed, TagSelector... selector) throws IOException {
		return new NBTReader().compressed(compressed).select(selector).readNamed(stream);
	}

	public static NamedTag readNamedLE(InputStream stream) throws IOException {
		return new NBTReader().littleEndian().detectCompression().readNamed(stream);
	}

	public static NamedTag readNamedLE(InputStream stream, boolean compressed) throws IOException {
		return new NBTReader().littleEndian().compressed(compressed).readNamed(stream);
	}

	public static NamedTag readNamedLE(InputStream stream, TagSelector... selector) throws IOException {
		return new NBTReader().littleEndian().detectCompression().select(selector).readNamed(stream);
	}

	public static NamedTag readNamedLE(InputStream stream, boolean compressed, TagSelector... selector) throws IOException {
		return new NBTReader().littleEndian().compressed(compressed).select(selector).readNamed(stream);
	}

	// ---------------------------------------------

	public static void write(File file, Tag tag) throws IOException {
		new NBTWriter().write(file, tag);
	}

	public static void write(File file, Tag tag, boolean compressed) throws IOException {
		new NBTWriter().compressed(compressed).write(file, tag);
	}

	public static void writeLE(File file, Tag tag) throws IOException {
		new NBTWriter().littleEndian().write(file, tag);
	}

	public static void writeLE(File file, Tag tag, boolean compressed) throws IOException {
		new NBTWriter().littleEndian().compressed(compressed).write(file, tag);
	}

	public static void writeNamed(File file, NamedTag tag) throws IOException {
		new NBTWriter().writeNamed(file, tag);
	}

	public static void writeNamed(File file, NamedTag tag, boolean compressed) throws IOException {
		new NBTWriter().compressed(compressed).writeNamed(file, tag);
	}

	public static void writeNamedLE(File file, NamedTag tag) throws IOException {
		new NBTWriter().littleEndian().writeNamed(file, tag);
	}

	public static void writeNamedLE(File file, NamedTag tag, boolean compressed) throws IOException {
		new NBTWriter().littleEndian().compressed(compressed).writeNamed(file, tag);
	}

	public static void writeNamed(File file, String name, Tag tag) throws IOException {
		new NBTWriter().writeNamed(file, name, tag);
	}

	public static void writeNamed(File file, String name, Tag tag, boolean compressed) throws IOException {
		new NBTWriter().compressed(compressed).writeNamed(file, name, tag);
	}

	public static void writeNamedLE(File file, String name, Tag tag) throws IOException {
		new NBTWriter().littleEndian().writeNamed(file, name, tag);
	}

	public static void writeNamedLE(File file, String name, Tag tag, boolean compressed) throws IOException {
		new NBTWriter().littleEndian().compressed(compressed).writeNamed(file, name, tag);
	}

	public static void write(OutputStream stream, Tag tag) throws IOException {
		new NBTWriter().write(stream, tag);
	}

	public static void write(OutputStream stream, Tag tag, boolean compressed) throws IOException {
		new NBTWriter().compressed(compressed).write(stream, tag);
	}

	public static void writeLE(OutputStream stream, Tag tag) throws IOException {
		new NBTWriter().littleEndian().write(stream, tag);
	}

	public static void writeLE(OutputStream stream, Tag tag, boolean compressed) throws IOException {
		new NBTWriter().littleEndian().compressed(compressed).write(stream, tag);
	}

	public static void writeNamed(OutputStream stream, NamedTag tag) throws IOException {
		new NBTWriter().writeNamed(stream, tag);
	}

	public static void writeNamed(OutputStream stream, NamedTag tag, boolean compressed) throws IOException {
		new NBTWriter().compressed(compressed).writeNamed(stream, tag);
	}

	public static void writeNamedLE(OutputStream stream, NamedTag tag) throws IOException {
		new NBTWriter().littleEndian().writeNamed(stream, tag);
	}

	public static void writeNamedLE(OutputStream stream, NamedTag tag, boolean compressed) throws IOException {
		new NBTWriter().littleEndian().compressed(compressed).writeNamed(stream, tag);
	}

	public static void writeNamed(OutputStream stream, String name, Tag tag) throws IOException {
		new NBTWriter().writeNamed(stream, name, tag);
	}

	public static void writeNamed(OutputStream stream, String name, Tag tag, boolean compressed) throws IOException {
		new NBTWriter().compressed(compressed).writeNamed(stream, name, tag);
	}

	public static void writeNamedLE(OutputStream stream, String name, Tag tag) throws IOException {
		new NBTWriter().littleEndian().writeNamed(stream, name, tag);
	}

	public static void writeNamedLE(OutputStream stream, String name, Tag tag, boolean compressed) throws IOException {
		new NBTWriter().littleEndian().compressed(compressed).writeNamed(stream, name, tag);
	}

	// ---------------------------------------------

	public static void parseStream(DataInput in, TagTypeVisitor visitor) throws IOException {
		TagReader<?> reader = Tag.Type.valueOf(in.readByte()).reader;
		if (reader == EndTag.READER) {
			if (visitor.visitRootEntry(EndTag.READER) == TagTypeVisitor.ValueResult.CONTINUE) {
				visitor.visitEnd();
			}
		} else {
			switch (visitor.visitRootEntry(reader)) {
				case BREAK:
					StringTag.skipUTF(in);
					reader.skip(in);
					break;
				case CONTINUE:
					StringTag.skipUTF(in);
					reader.read(in, visitor);
					break;
			}
		}
	}

	public static Tag parseStream(DataInput in, TagSelector... selectors) throws IOException {
		SelectionStreamTagVisitor visitor = new SelectionStreamTagVisitor(selectors);
		parseStream(in, visitor);
		return visitor.getResult();
	}

	// ---------------------------------------------

	public static String toSNBT(Tag tag, String indent) {
		return new SNBTWriter().indent(indent).toString(tag);
	}

	public static String toSNBT(Tag tag) {
		return new SNBTWriter().toString(tag);
	}

	public static Tag fromSNBT(String snbt, boolean ignoreTrailing) throws ParseException {
		return new SNBTReader().ignoreTrailing(ignoreTrailing).read(snbt);
	}

	public static Tag fromSNBT(String snbt) throws ParseException {
		return fromSNBT(snbt, false);
	}
}
