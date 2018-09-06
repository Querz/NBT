package net.querz.nbt.mca;

import net.querz.nbt.CompoundTag;
import net.querz.nbt.Tag;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

/**
 * a complete representation of an .mca file, capable of reading and saving.
 * */
public class MCAFile {

	private int[] offsets;
	private byte[] sectors;
	private int[] lengths;
	private int[] timestamps;
	private CompoundTag[] data;

	public MCAFile() {}

	public void deserialize(RandomAccessFile raf) throws IOException {
		offsets = new int[1024];
		sectors = new byte[1024];
		lengths = new int[1024];
		timestamps = new int[1024];
		data = new CompoundTag[1024];
		for (int i = 0; i < offsets.length; i++) {
			raf.seek(i * 4);
			int offset = raf.read() << 16;
			offset |= (raf.read() & 0xFF) << 8;
			offset |= raf.read() & 0xFF;
			offsets[i] = offset;

			if ((sectors[i] = raf.readByte()) == 0) {
				continue;
			}

			raf.seek(offset * 4096);
			lengths[i] = raf.readInt();

			DataInputStream dis;

			byte ct;
			switch (ct = raf.readByte()) {
				case 0:
					continue; //compression type 0 means no data
				case 1:
					dis = new DataInputStream(new BufferedInputStream(new GZIPInputStream(new FileInputStream(raf.getFD()))));
					break;
				case 2:
					dis = new DataInputStream(new BufferedInputStream(new InflaterInputStream(new FileInputStream(raf.getFD()))));
					break;
				default:
					throw new IOException("invalid compression type " + ct);
			}

			Tag tag = Tag.deserialize(dis, 0);

			if (tag instanceof CompoundTag) {
				data[i] = (CompoundTag) tag;
			} else {
				throw new IOException("invalid data tag at offset " + offset + ": " + (tag == null ? "null" : tag.getClass().getName()));
			}
		}
		raf.seek(4096);
		for (int i = 0; i < timestamps.length; i++) {
			timestamps[i] = raf.readInt();
		}
	}

	public int serialize(RandomAccessFile raf) throws IOException {
		return serialize(raf, false);
	}

	//returns the number of chunks written to the file.
	public int serialize(RandomAccessFile raf, boolean changeLastUpdate) throws IOException {
		int globalOffset = 2;
		int lastWritten = 0;
		int timestamp = (int) (System.currentTimeMillis() / 1000L);
		int chunksWritten = 0;

		for (int i = 0; i < 1024; i++) {
			if (data[i] == null) {
				continue;
			}

			raf.seek(globalOffset * 4096);

			//write chunk data
			ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
			try (DataOutputStream nbtOut = new DataOutputStream(new BufferedOutputStream(new DeflaterOutputStream(baos)))) {
				data[i].serialize(nbtOut, 0);
			}
			byte[] rawData = baos.toByteArray();
			raf.writeInt(rawData.length);
			raf.writeByte(2);
			raf.write(rawData);
			lastWritten = rawData.length + 5;

			chunksWritten++;

			int sectors = (lastWritten >> 12) + 1;

			raf.seek(i * 4);
			raf.writeByte(globalOffset >>> 16);
			raf.writeByte(globalOffset >> 8 & 0xFF);
			raf.writeByte(globalOffset & 0xFF);
			raf.writeByte(sectors);

			//write timestamp to tmp file
			raf.seek(i * 4 + 4096);
			raf.writeInt(changeLastUpdate ? timestamp : timestamps[i]);

			globalOffset += sectors;
		}

		//padding
		if (lastWritten % 4096 != 0) {
			raf.seek(globalOffset * 4096 - 1);
			raf.write(0);
		}
		return chunksWritten;
	}

	public int getOffset(int index) {
		checkIndex(index);
		if (offsets == null) {
			return 0;
		}
		return offsets[index];
	}

	public int getOffset(int chunkX, int chunkZ) {
		return getOffset(getChunkIndex(chunkX, chunkZ));
	}

	public byte getSizeInSectors(int index) {
		checkIndex(index);
		if (sectors == null) {
			return 0;
		}
		return sectors[index];
	}

	public byte getSizeInSectors(int chunkX, int chunkZ) {
		return getSizeInSectors(getChunkIndex(chunkX, chunkZ));
	}

	public int getLastUpdate(int index) {
		checkIndex(index);
		if (timestamps == null) {
			return 0;
		}
		return timestamps[index];
	}

	public int getLastUpdate(int chunkX, int chunkZ) {
		return getLastUpdate(getChunkIndex(chunkX, chunkZ));
	}

	public int getRawDataLength(int index) {
		checkIndex(index);
		if (lengths == null) {
			return 0;
		}
		return lengths[index];
	}

	public int getRawDataLength(int chunkX, int chunkZ) {
		return getRawDataLength(getChunkIndex(chunkX, chunkZ));
	}

	public void setLastUpdate(int index, int lastUpdate) {
		checkIndex(index);
		if (timestamps == null) {
			timestamps = new int[1024];
		}
		timestamps[index] = lastUpdate;
	}

	public void setLastUpdate(int chunkX, int chunkZ, int lastUpdate) {
		setLastUpdate(getChunkIndex(chunkX, chunkZ), lastUpdate);
	}

	public CompoundTag getChunkData(int index) {
		checkIndex(index);
		if (data == null) {
			return null;
		}
		return data[index];
	}

	public CompoundTag getChunkData(int chunkX, int chunkZ) {
		return getChunkData(getChunkIndex(chunkX, chunkZ));
	}

	public void setChunkData(int index, CompoundTag data) {
		checkIndex(index);
		if (this.data == null) {
			this.data = new CompoundTag[1024];
		}
		this.data[index] = data;
	}

	public void setChunkData(int chunkX, int chunkZ, CompoundTag data) {
		setChunkData(getChunkIndex(chunkX, chunkZ), data);
	}

	public static int getChunkIndex(int chunkX, int chunkZ) {
		return (chunkX & 31) + (chunkZ & 31) * 32;
	}

	private int checkIndex(int index) {
		if (index < 0 || index > 1023) {
			throw new IndexOutOfBoundsException();
		}
		return index;
	}
}
