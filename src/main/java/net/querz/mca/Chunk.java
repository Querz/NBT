package net.querz.mca;

import net.querz.io.ExposedByteArrayOutputStream;
import net.querz.mca.parsers.SectionParser;
import net.querz.mca.seekable.SeekableData;
import net.querz.nbt.CompoundTag;
import net.querz.nbt.NBTUtil;
import net.querz.nbt.Tag;
import net.querz.nbt.TagTypeVisitor;
import net.querz.nbt.io.NBTReader;
import net.querz.nbt.io.NBTWriter;

import java.io.*;
import java.util.function.Supplier;

public class Chunk {

	private final int x;
	private final int z;
	private int timestamp;
	private CompressionType compressionType;

	private CompoundTag data;

	private SectionParser sectionParser;

	public Chunk(int x, int z, int timestamp) {
		this.x = x;
		this.z = z;
		this.timestamp = timestamp;
	}

	public void load(MCAFileHandle handle, Supplier<TagTypeVisitor> tagTypeVisitorSupplier) throws IOException {
		SeekableData s = handle.seekableData();

		compressionType = CompressionType.fromByte(s.read());
		// do not close this stream, we're going to close the handle later
		Tag tag;
		if (compressionType.isExternal()) {
			tag = handle.mccFileHandler().read(handle, x, z, compressionType);
		} else {
			InputStream nbtInput = compressionType.wrap(handle, 8196);
			tag = new NBTReader().withVisitor(tagTypeVisitorSupplier.get()).read(nbtInput);
		}

		if (tag == null || tag.getType() != CompoundTag.TYPE) {
			throw new IOException("chunk data root tag is not a CompoundTag");
		}

		data = (CompoundTag) tag;
	}

	public int save(MCAFileHandle handle) throws IOException {
		SeekableData s = handle.seekableData();

		ExposedByteArrayOutputStream baos;
		OutputStream nbtOutput = compressionType.wrap(baos = new ExposedByteArrayOutputStream(), 8196);
		new NBTWriter().write(nbtOutput, data);
		nbtOutput.close();

		// check if we need to save the chunk as .mcc file
		if (baos.size() > 1048576) {
			handle.mccFileHandler().write(handle, getDataVersion(), x, z, baos.getBuffer(), baos.size(), compressionType);
			s.writeInt(1); // length is 1 because we only write the external compression type
			s.writeByte(compressionType.getExternal().getID());
			return 5;
		} else {
			s.writeInt(baos.size() + 1); // length includes the compression type byte
			s.writeByte(compressionType.getID());
			s.write(baos.getBuffer(), 0, baos.size());
			return baos.size() + 5; // data length + compression type byte + length int
		}
	}

	public int getDataVersion() {
		return isEmpty() ? 0 : data.getIntOrDefault("DataVersion", 0);
	}

	public static File getMCCFile(File parent, int x, int z) {
		return new File(parent, "c." + x + "." + z + ".mcc");
	}

	public CompoundTag getData() {
		return data;
	}

	public boolean isEmpty() {
		return data == null;
	}

	public CompoundTag getSection(int y) {
		return getSectionParser().getSectionAt(y);
	}

	public SectionParser getSectionParser() {
		if (sectionParser == null) {
			sectionParser = ParserHandler.getSectionParser(getDataVersion(), data);
		}
		return sectionParser;
	}

	@Override
	public String toString() {
		return NBTUtil.toSNBT(data, "\t");
	}

	public int getX() {
		return x;
	}

	public int getZ() {
		return z;
	}

	public int getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}
}
