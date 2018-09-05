package net.querz.nbt.mca;

import net.querz.nbt.CompoundTag;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * a complete representation of an .mca file, capable of reading and saving.
 * */
public class MCAFile {

	File file;
	int[] offsets;
	byte[] sectors;
	int[] lengths;
	int[] timestamps = new int[1024];
	CompoundTag[] data = new CompoundTag[1024];

	public MCAFile(File file) {
		this.file = file;
	}

	public void deserialize() throws IOException {
		offsets = new int[1024];
		sectors = new byte[1024];
		lengths = new int[1024];
		try (MCARandomAccessFile mcaRaf = new MCARandomAccessFile(file, "r")) {
			mcaRaf.readHeader(this);
			for (int i = 0; i < offsets.length; i++) {
				data[i] = mcaRaf.loadChunkData(this, i, offsets[i]);
			}
		}
	}

	public int serialize() throws IOException {
		return serialize(false);
	}

	//returns the number of chunks written to the file.
	public int serialize(boolean changeLastUpdate) throws IOException {
		File file = this.file;
		if (file.exists()) {
			file = File.createTempFile(file.getName(), null);
		}
		int chunks;
		try (MCARandomAccessFile mcaRaf = new MCARandomAccessFile(file, "rw")) {
			chunks = mcaRaf.writeMCAFile(this, changeLastUpdate);
		}
		if (chunks != 0 && this.file != file) {
			Files.move(file.toPath(), this.file.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
		return chunks;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public int getOffset(int index) {
		return offsets[index];
	}

	public int getOffset(int chunkX, int chunkZ) {
		return getOffset(getChunkIndex(chunkX, chunkZ));
	}

	public byte getSizeInSectors(int index) {
		return sectors[index];
	}

	public byte getSizeInSectors(int chunkX, int chunkZ) {
		return getSizeInSectors(getChunkIndex(chunkX, chunkZ));
	}

	public int getLastUpdate(int index) {
		return timestamps[index];
	}

	public int getLastUpdate(int chunkX, int chunkZ) {
		return getLastUpdate(getChunkIndex(chunkX, chunkZ));
	}

	public int getRawDataLength(int index) {
		return lengths[index];
	}

	public int getRawDataLength(int chunkX, int chunkZ) {
		return getRawDataLength(getChunkIndex(chunkX, chunkZ));
	}

	public void setLastUpdate(int index, int lastUpdate) {
		timestamps[index] = lastUpdate;
	}

	public void setLastUpdate(int chunkX, int chunkZ, int lastUpdate) {
		setLastUpdate(getChunkIndex(chunkX, chunkZ), lastUpdate);
	}

	public CompoundTag getChunkData(int index) {
		return data[index];
	}

	public CompoundTag getChunkData(int chunkX, int chunkZ) {
		return getChunkData(getChunkIndex(chunkX, chunkZ));
	}

	public void setChunkData(int index, CompoundTag data) {
		this.data[index] = data;
	}

	public void setChunkData(int chunkX, int chunkZ, CompoundTag data) {
		setChunkData(getChunkIndex(chunkX, chunkZ), data);
	}

	public static int getChunkIndex(int chunkX, int chunkZ) {
		return (chunkX & 31) + (chunkZ & 31) * 32;
	}
}
