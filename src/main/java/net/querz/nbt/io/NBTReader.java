package net.querz.nbt.io;

import net.querz.nbt.*;
import net.querz.io.LittleEndianInputStream;
import net.querz.nbt.io.stream.SelectionStreamTagVisitor;
import net.querz.nbt.io.stream.TagSelector;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

public final class NBTReader {

	private boolean compression;
	private boolean detectCompression;
	private boolean littleEndian;
	private List<TagSelector> selectors = null;

	private TagTypeVisitor visitor = null;

	public NBTReader detectCompression() {
		detectCompression = true;
		return this;
	}

	public NBTReader compressed(boolean compressed) {
		compression = compressed;
		return this;
	}

	public NBTReader littleEndian() {
		littleEndian = true;
		return this;
	}

	public NBTReader bigEndian() {
		littleEndian = false;
		return this;
	}

	public NBTReader select(String name, TagReader<?> reader) {
		if (selectors == null) {
			selectors = new ArrayList<>();
		}
		selectors.add(new TagSelector(name, reader));
		return this;
	}

	public NBTReader select(String p1, String name, TagReader<?> reader) {
		if (selectors == null) {
			selectors = new ArrayList<>();
		}
		selectors.add(new TagSelector(p1, name, reader));
		return this;
	}

	public NBTReader select(String p1, String p2, String name, TagReader<?> reader) {
		if (selectors == null) {
			selectors = new ArrayList<>();
		}
		selectors.add(new TagSelector(p1, p2, name, reader));
		return this;
	}

	public NBTReader select(TagSelector... selection) {
		for (TagSelector selector : selection) {
			select(selector);
		}
		return this;
	}

	public NBTReader select(TagSelector selector) {
		if (selectors == null) {
			selectors = new ArrayList<>();
		}
		selectors.add(selector);
		return this;
	}

	public NBTReader withVisitor(TagTypeVisitor visitor) {
		this.visitor = visitor;
		return this;
	}

	public NamedTag readNamed(InputStream in) throws IOException {
		InputStream stream;
		if (detectCompression) {
			stream = new BufferedInputStream(detectDecompression(in));
		} else {
			if (compression) {
				stream = new BufferedInputStream(new GZIPInputStream(in));
			} else {
				stream = new BufferedInputStream(in);
			}
		}

		DataInput input;
		if (littleEndian) {
			input = new LittleEndianInputStream(stream);
		} else {
			input = new DataInputStream(stream);
		}

		if (selectors == null) {
			if (visitor != null) {
				return readWithVisitor(input, visitor);
			}
			TagReader<?> reader = Tag.Type.valueOf(input.readByte()).reader;
			String name = input.readUTF();
			return new NamedTag(name, reader.read(input, 0));
		} else {
			SelectionStreamTagVisitor visitor = new SelectionStreamTagVisitor(selectors.toArray(new TagSelector[0]));
			return readWithVisitor(input, visitor);
		}
	}

	private NamedTag readWithVisitor(DataInput input, TagTypeVisitor visitor) throws IOException {
		TagReader<?> reader = Tag.Type.valueOf(input.readByte()).reader;
		String name = "";
		if (reader == EndTag.READER) {
			if (visitor.visitRootEntry(EndTag.READER) == TagTypeVisitor.ValueResult.CONTINUE) {
				visitor.visitEnd();
			}
		} else {
			switch (visitor.visitRootEntry(reader)) {
				case BREAK:
					name = input.readUTF();
					reader.skip(input);
					break;
				case CONTINUE:
					name = input.readUTF();
					reader.read(input, visitor);
					break;
			}
		}
		return new NamedTag(name, visitor.getResult());
	}

	public Tag read(InputStream in) throws IOException {
		return readNamed(in).tag();
	}

	public NamedTag readNamed(File file) throws IOException {
		return readNamed(new FileInputStream(file));
	}

	public Tag read(File file) throws IOException {
		return read(new FileInputStream(file));
	}

	private static InputStream detectDecompression(InputStream is) throws IOException {
		PushbackInputStream pbis = new PushbackInputStream(is, 2);
		int signature = (pbis.read() & 0xFF) + (pbis.read() << 8);
		pbis.unread(signature >> 8);
		pbis.unread(signature & 0xFF);
		return signature == GZIPInputStream.GZIP_MAGIC ? new GZIPInputStream(pbis) : pbis;
	}
}
