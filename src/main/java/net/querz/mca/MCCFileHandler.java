package net.querz.mca;

import net.querz.nbt.NBTUtil;
import net.querz.nbt.Tag;
import net.querz.nbt.io.stream.TagSelector;
import java.io.*;

public interface MCCFileHandler {

	void write(MCAFileHandle handle, int dataVersion, int x, int z, byte[] buffer, int size, CompressionType compressionType) throws IOException;

	Tag read(MCAFileHandle handle, int x, int z, CompressionType compressionType, TagSelector... selectors) throws IOException;

	DefaultHandler DEFAULT_HANDLER = new DefaultHandler();

	final class DefaultHandler implements MCCFileHandler {

		private DefaultHandler() {}

		@Override
		public void write(MCAFileHandle handle, int dataVersion, int x, int z, byte[] buffer, int size, CompressionType compressionType) throws IOException {
			if (dataVersion < 2203) {
				throw new IOException("chunk at " + x + "," + z + " cannot be saved because its DataVersion does not support saving oversize chunks as .mcc");
			}

			try (OutputStream dos = compressionType.wrap(new FileOutputStream(Chunk.getMCCFile(handle.directory(), x, z)), size)) {
				dos.write(buffer, 0, size);
			}
		}

		@Override
		public Tag read(MCAFileHandle handle, int x, int z, CompressionType compressionType, TagSelector... selectors) throws IOException {
			try (DataInputStream in = new DataInputStream(compressionType.wrap(new FileInputStream(Chunk.getMCCFile(handle.directory(), x, z)), 8196))) {
				return NBTUtil.parseStream(in, selectors);
			}
		}
	}
}
