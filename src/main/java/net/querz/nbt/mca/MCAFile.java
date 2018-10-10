package net.querz.nbt.mca;

import net.querz.nbt.CompoundTag;
import java.io.IOException;
import java.io.RandomAccessFile;

public class MCAFile {

	public static final int DEFAULT_DATA_VERSION = 1628;

	private Chunk[] chunks;

	public MCAFile() {}

	/**
	 * Reads an .mca file from a {@code RandomAccessFile} into this object.
	 * This method does not perform any cleanups on the data.
	 * @param raf The {@code RandomAccessFile} to read from.
	 * @throws IOException If something went wrong during deserialization.
	 * */
	public void deserialize(RandomAccessFile raf) throws IOException {
		chunks = new Chunk[1024];
		for (int i = 0; i < 1024; i++) {
			raf.seek(i * 4);
			int offset = raf.read() << 16;
			offset |= (raf.read() & 0xFF) << 8;
			offset |= raf.read() & 0xFF;

			int sectors;
			if ((sectors = raf.readByte()) == 0) {
				continue;
			}

			raf.seek(4096 + i * 4);
			int timestamp = raf.readInt();

			Chunk chunk = new Chunk(offset, sectors, timestamp);
			chunk.deserialize(raf);
			chunks[i] = chunk;
		}
	}

	/**
	 * Calls {@link MCAFile#serialize(RandomAccessFile, boolean)} without updating any timestamps.
	 * @see MCAFile#serialize(RandomAccessFile, boolean)
	 * */
	public int serialize(RandomAccessFile raf) throws IOException {
		return serialize(raf, false);
	}

	/**
	 * Serializes this object to an .mca file.
	 * This method does not perform any cleanups on the data.
	 * @param raf The {@code RandomAccessFile} to write to.
	 * @param changeLastUpdate Whether it should update all timestamps that show
	 *                         when this file was last updated.
	 * @throws IOException If something went wrong during serialization.
	 * @return The amount of chunks written to the file.
	 * */
	public int serialize(RandomAccessFile raf, boolean changeLastUpdate) throws IOException {
		int globalOffset = 2;
		int lastWritten = 0;
		int timestamp = (int) (System.currentTimeMillis() / 1000L);
		int chunksWritten = 0;

		for (int i = 0; i < 1024; i++) {
			Chunk chunk = chunks[i];
			if (chunk == null) {
				continue;
			}

			lastWritten = chunk.serialize(raf, globalOffset);

			if (lastWritten == 0) {
				continue;
			}

			chunksWritten++;

			int sectors = (lastWritten >> 12) + 1;

			raf.seek(i * 4);
			raf.writeByte(globalOffset >>> 16);
			raf.writeByte(globalOffset >> 8 & 0xFF);
			raf.writeByte(globalOffset & 0xFF);
			raf.writeByte(sectors);

			//write timestamp to tmp file
			raf.seek(i * 4 + 4096);
			raf.writeInt(changeLastUpdate ? timestamp : chunk.getLastUpdate());

			globalOffset += sectors;
		}

		//padding
		if (lastWritten % 4096 != 0) {
			raf.seek(globalOffset * 4096 - 1);
			raf.write(0);
		}
		return chunksWritten;
	}

	/**
	 * Sets the chunk data of a chunk at a specific index in this file.
	 * @param chunk The chunk data.
	 * */
	public void setChunk(Chunk chunk) {
		setChunk(getChunkIndex(chunk.getPosX(), chunk.getPosZ()), chunk);
	}

	public void setChunk(int index, Chunk chunk) {
		checkIndex(index);
		if (chunks == null) {
			chunks = new Chunk[1024];
		}
		chunks[index] = chunk;
	}

	/**
	 * Returns the chunk data of a chunk at a specific index in this file.
	 * @param index The index of the chunk in this file.
	 * @return The chunk data.
	 * */
	public Chunk getChunk(int index) {
		checkIndex(index);
		if (chunks == null) {
			return null;
		}
		return chunks[index];
	}

	/**
	 * Returns the chunk data of a chunk in this file.
	 * @param chunkX The x-coordinate of the chunk.
	 * @param chunkZ The z-coordinate of the chunk.
	 * @return The chunk data.
	 * */
	public Chunk getChunk(int chunkX, int chunkZ) {
		return getChunk(getChunkIndex(chunkX, chunkZ));
	}

	/**
	 * Calculates the index of a chunk from its x- and z-coordinates in this region.
	 * This works with absolute and relative coordinates.
	 * @param chunkX The x-coordinate of the chunk.
	 * @param chunkZ The z-coordinate of the chunk.
	 * @return The index of this chunk.
	 * */
	public static int getChunkIndex(int chunkX, int chunkZ) {
		return (chunkX & 0x1F) + (chunkZ & 0x1F) * 32;
	}

	private int checkIndex(int index) {
		if (index < 0 || index > 1023) {
			throw new IndexOutOfBoundsException();
		}
		return index;
	}

	private Chunk createChunkIfMissing(int blockX, int blockZ) {
		int chunkX = MCAUtil.blockToChunk(blockX), chunkZ = MCAUtil.blockToChunk(blockZ);
		Chunk chunk = getChunk(chunkX, chunkZ);
		if (chunk == null) {
			chunk = Chunk.createDefaultChunk(chunkX, chunkZ);
			setChunk(chunk);
		}
		return chunk;
	}

	public void setBiomeAt(int blockX, int blockZ, int biomeID) {
		createChunkIfMissing(blockX, blockZ).setBiomeAt(blockX, blockZ, biomeID);
	}

	public int getBiomeAt(int blockX, int blockZ) {
		int chunkX = MCAUtil.blockToChunk(blockX), chunkZ = MCAUtil.blockToChunk(blockZ);
		Chunk chunk = getChunk(getChunkIndex(chunkX, chunkZ));
		if (chunk == null) {
			return -1;
		}
		return chunk.getBiomeAt(blockX, blockZ);
	}

	public void setBlockStateAt(int blockX, int blockY, int blockZ, CompoundTag state, boolean cleanup) {
		createChunkIfMissing(blockX, blockZ).setBlockStateAt(blockX, blockY, blockZ, state, cleanup);
	}

	public CompoundTag getBlockStateAt(int blockX, int blockY, int blockZ) {
		int chunkX = MCAUtil.blockToChunk(blockX), chunkZ = MCAUtil.blockToChunk(blockZ);
		Chunk chunk = getChunk(chunkX, chunkZ);
		if (chunk == null) {
			return null;
		}
		return chunk.getBlockStateAt(blockX, blockY, blockZ);
	}

	public void cleanupAllPalettesAndBlockStates() {
		for (Chunk chunk : chunks) {
			if (chunk != null) {
				chunk.cleanupAllPalettesAndBlockStates();
			}
		}
	}
}
