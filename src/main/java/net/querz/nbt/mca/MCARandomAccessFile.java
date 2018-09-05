package net.querz.nbt.mca;

import net.querz.nbt.CompoundTag;
import net.querz.nbt.Tag;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

public class MCARandomAccessFile extends RandomAccessFile {

	public MCARandomAccessFile(String name, String mode) throws FileNotFoundException {
		super(name, mode);
	}

	public MCARandomAccessFile(File file, String mode) throws FileNotFoundException {
		super(file, mode);
	}

	public CompoundTag readTag(int chunkX, int chunkZ) throws IOException {
		return readTag(MCAFile.getChunkIndex(chunkX, chunkZ));
	}

	public CompoundTag readTag(int index) throws IOException {
		seek(index * 4);

		int offset = read() << 16;
		offset |= (read() & 0xFF) << 8;
		offset |= read() & 0xFF;

		//empty chunk
		if (offset == 0 || readByte() == 0) {
			return null;
		}

		return loadChunkData(null, index, offset);
	}

	CompoundTag loadChunkData(MCAFile mcaFile, int index, int offset) throws IOException {
		if (mcaFile == null) {
			seek(offset * 4096 + 4);
		} else {
			seek(offset * 4096);
			mcaFile.lengths[index] = readInt();
		}

		DataInputStream dis;

		byte ct;
		switch (ct = readByte()) {
			case 0:
				return null; //compression type 0 means no data
			case 1:
				dis = new DataInputStream(new BufferedInputStream(new GZIPInputStream(new FileInputStream(getFD()))));
				break;
			case 2:
				dis = new DataInputStream(new BufferedInputStream(new InflaterInputStream(new FileInputStream(getFD()))));
				break;
			default:
				throw new IOException("invalid compression type " + ct);
		}

		Tag tag = Tag.deserialize(dis, 0);

		if (tag instanceof CompoundTag) {
			return (CompoundTag) tag;
		} else {
			throw new IOException("invalid data tag at offset " + offset + ": " + (tag == null ? "null" : tag.getClass().getName()));
		}
	}

	//returns the number of bytes written
	int saveChunkData(CompoundTag data) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
		try (DataOutputStream nbtOut = new DataOutputStream(new BufferedOutputStream(new DeflaterOutputStream(baos)))) {
			data.serialize(nbtOut, 0);
		}
		byte[] rawData = baos.toByteArray();
		writeInt(rawData.length);
		writeByte(2);
		write(rawData);
		return rawData.length + 5;
	}

	public void readHeader(MCAFile mcaFile) throws IOException {
		for (int i = 0; i < mcaFile.offsets.length; i++) {
			int offset = read() << 16;
			offset |= (read() & 0xFF) << 8;
			mcaFile.offsets[i] = offset | read() & 0xFF;
			mcaFile.sectors[i] = readByte();
		}
		for (int i = 0; i < mcaFile.timestamps.length; i++) {
			mcaFile.timestamps[i] = readInt();
		}
	}

	//returns the number of chunks actually written
	//does not create a file if there is no chunk data
	public int writeMCAFile(MCAFile mcaFile, boolean changeLastUpdate) throws IOException {
		int globalOffset = 2;
		int lastWritten = 0;
		int timestamp = (int) (System.currentTimeMillis() / 1000L);
		int chunksWritten = 0;

		for (int i = 0; i < 1024; i++) {
			if (mcaFile.data[i] == null) {
				continue;
			}

			seek(globalOffset * 4096);

			//write chunk data
			lastWritten = saveChunkData(mcaFile.data[i]);

			chunksWritten++;

			int sectors = (lastWritten >> 12) + 1;

			seek(i * 4);
			writeByte(globalOffset >>> 16);
			writeByte(globalOffset >> 8 & 0xFF);
			writeByte(globalOffset & 0xFF);
			writeByte(sectors);

			//write timestamp to tmp file
			seek(i * 4 + 4096);
			writeInt(changeLastUpdate ? timestamp : mcaFile.timestamps[i]);

			globalOffset += sectors;
		}

		//padding
		if (lastWritten % 4096 != 0) {
			seek(globalOffset * 4096 - 1);
			write(0);
		}
		return chunksWritten;
	}
}
