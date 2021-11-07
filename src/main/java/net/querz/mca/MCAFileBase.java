package net.querz.mca;


import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * An abstract representation of an mca file.
 */
public abstract class MCAFileBase<T extends Chunk> implements Iterable<T> {

	protected int regionX, regionZ;
	protected T[] chunks;

	/**
	 * MCAFile represents a world save file used by Minecraft to store world
	 * data on the hard drive.
	 * This constructor needs the x- and z-coordinates of the stored region,
	 * which can usually be taken from the file name {@code r.x.z.mca}
	 * @param regionX The x-coordinate of this mca file in region coordinates.
	 * @param regionZ The z-coordinate of this mca file in region coordinates.
	 */
	public MCAFileBase(int regionX, int regionZ) {
		this.regionX = regionX;
		this.regionZ = regionZ;
	}

	/**
	 * @return The x-value currently set for this mca file in region coordinates.
	 */
	public int getRegionX() {
		return regionX;
	}

	/**
	 * Sets a new x-value for this mca file in region coordinates.
	 */
	public void setRegionX(int regionX) {
		this.regionX = regionX;
	}

	/**
	 * @return The z-value currently set for this mca file in region coordinates.
	 */
	public int getRegionZ() {
		return regionZ;
	}

	/**
	 * Sets a new z-value for this mca file in region coordinates.
	 */
	public void setRegionZ(int regionZ) {
		this.regionZ = regionZ;
	}

	/**
	 * Sets both the x and z values for this mca file in region coordinates.
	 * @param regionX New x-value for this mca file in region coordinates.
	 * @param regionZ New z-value for this mca file in region coordinates.
	 */
	public void setRegionXZ(int regionX, int regionZ) {
		this.regionX = regionX;
		this.regionZ = regionZ;
	}

	/**
	 * Returns result of calling {@link MCAUtil#createNameFromRegionLocation(int, int)}
	 * with current region coordinate values.
	 * @return A mca filename in the format "r.{regionX}.{regionZ}.mca"
	 */
	public String createRegionName() {
		return MCAUtil.createNameFromRegionLocation(regionX, regionZ);
	}

	/**
	 * @return type of chunk this MCA File holds
	 */
	public abstract Class<T> chunkClass();

	/**
	 * Chunk creator.
	 */
	protected abstract T newChunk();

	/**
	 * Called to deserialize a Chunk. Caller will have set the position of {@code raf} to start reading.
	 * @param raf The {@code RandomAccessFile} to read from.
	 * @param loadFlags A logical or of {@link LoadFlags} constants indicating what data should be loaded
	 * @param timestamp The timestamp when this chunk was last updated as a UNIX timestamp.
	 * @return Deserialized chunk.
	 * @throws IOException if something went wrong during deserialization.
	 */
	protected abstract T deserializeChunk(RandomAccessFile raf, long loadFlags, int timestamp) throws IOException;

	/**
	 * Reads an .mca file from a {@code RandomAccessFile} into this object.
	 * This method does not perform any cleanups on the data.
	 * @param raf The {@code RandomAccessFile} to read from.
	 * @throws IOException If something went wrong during deserialization.
	 */
	public void deserialize(RandomAccessFile raf) throws IOException {
		deserialize(raf, LoadFlags.ALL_DATA);
	}

	/**
	 * Reads an .mca file from a {@code RandomAccessFile} into this object.
	 * This method does not perform any cleanups on the data.
	 * @param raf The {@code RandomAccessFile} to read from.
	 * @param loadFlags A logical or of {@link LoadFlags} constants indicating what data should be loaded
	 * @throws IOException If something went wrong during deserialization.
	 */
	@SuppressWarnings("unchecked")
	public void deserialize(RandomAccessFile raf, long loadFlags) throws IOException {
		chunks = (T[]) Array.newInstance(chunkClass(), 1024);
		for (int i = 0; i < 1024; i++) {
			raf.seek(i * 4);
			int offset = raf.read() << 16;
			offset |= (raf.read() & 0xFF) << 8;
			offset |= raf.read() & 0xFF;
			if (raf.readByte() == 0) {
				continue;
			}
			raf.seek(4096 + i * 4);
			int timestamp = raf.readInt();
			raf.seek(4096L * offset + 4); //+4: skip data size
			chunks[i] = deserializeChunk(raf, loadFlags, timestamp);
		}
	}

	/**
	 * Calls {@link MCAFileBase#serialize(RandomAccessFile, boolean)} without updating any timestamps.
	 * @see MCAFileBase#serialize(RandomAccessFile, boolean)
	 * @param raf The {@code RandomAccessFile} to write to.
	 * @return The amount of chunks written to the file.
	 * @throws IOException If something went wrong during serialization.
	 */
	public int serialize(RandomAccessFile raf) throws IOException {
		return serialize(raf, false);
	}

	/**
	 * Serializes this object to an .mca file.
	 * This method does not perform any cleanups on the data.
	 * @param raf The {@code RandomAccessFile} to write to.
	 * @param changeLastUpdate Whether it should update all timestamps that show
	 *                         when this file was last updated.
	 * @return The amount of chunks written to the file.
	 * @throws IOException If something went wrong during serialization.
	 */
	public int serialize(RandomAccessFile raf, boolean changeLastUpdate) throws IOException {
		int globalOffset = 2;
		int lastWritten = 0;
		int timestamp = (int) (System.currentTimeMillis() / 1000L);
		int chunksWritten = 0;
		int chunkXOffset = MCAUtil.regionToChunk(regionX);
		int chunkZOffset = MCAUtil.regionToChunk(regionZ);

		if (chunks == null) {
			return 0;
		}

		for (int cz = 0; cz < 32; cz++) {
			for (int cx = 0; cx < 32; cx++) {
				int index = getChunkIndex(cx, cz);
				Chunk chunk = chunks[index];
				if (chunk == null) {
					continue;
				}
				raf.seek(4096L * globalOffset);
				lastWritten = chunk.serialize(raf, chunkXOffset + cx, chunkZOffset + cz);

				if (lastWritten == 0) {
					continue;
				}

				chunksWritten++;

				int sectors = (lastWritten >> 12) + (lastWritten % 4096 == 0 ? 0 : 1);

				raf.seek(index * 4L);
				raf.writeByte(globalOffset >>> 16);
				raf.writeByte(globalOffset >> 8 & 0xFF);
				raf.writeByte(globalOffset & 0xFF);
				raf.writeByte(sectors);

				// write timestamp
				raf.seek(index * 4L + 4096);
				raf.writeInt(changeLastUpdate ? timestamp : chunk.getLastMCAUpdate());

				globalOffset += sectors;
			}
		}

		// padding
		if (lastWritten % 4096 != 0) {
			raf.seek(globalOffset * 4096L - 1);
			raf.write(0);
		}
		return chunksWritten;
	}

	/**
	 * Set a specific Chunk at a specific index. The index must be in range of 0 - 1023.
	 * Take care as the given chunk is NOT copied by this call.
	 * @param index The index of the Chunk.
	 * @param chunk The Chunk to be set.
	 * @throws IndexOutOfBoundsException If index is not in the range.
	 */
	@SuppressWarnings("unchecked")
	public void setChunk(int index, T chunk) {
		checkIndex(index);
		if (chunks == null) {
			chunks = (T[]) Array.newInstance(chunkClass(), 1024);
		}
		chunks[index] = chunk;
	}

	/**
	 * Set a specific Chunk at a specific chunk location.
	 * The x- and z-value can be absolute chunk coordinates or they can be relative to the region origin.
	 * @param chunkX The x-coordinate of the Chunk.
	 * @param chunkZ The z-coordinate of the Chunk.
	 * @param chunk The chunk to be set.
	 *
	 */
	public void setChunk(int chunkX, int chunkZ, T chunk) {
		setChunk(getChunkIndex(chunkX, chunkZ), chunk);
	}

	/**
	 * Returns the chunk data of a chunk at a specific index in this file.
	 * @param index The index of the chunk in this file.
	 * @return The chunk data.
	 */
	public T getChunk(int index) {
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
	 */
	public T getChunk(int chunkX, int chunkZ) {
		return getChunk(getChunkIndex(chunkX, chunkZ));
	}

	/**
	 * Calculates the index of a chunk from its x- and z-coordinates in this region.
	 * This works with absolute and relative coordinates.
	 * @param chunkX The x-coordinate of the chunk.
	 * @param chunkZ The z-coordinate of the chunk.
	 * @return The index of this chunk.
	 */
	public static int getChunkIndex(int chunkX, int chunkZ) {
		return ((chunkZ & 0x1F) << 5) | (chunkX & 0x1F);
	}

	protected void checkIndex(int index) {
		if (index < 0 || index > 1023) {
			throw new IndexOutOfBoundsException();
		}
	}

	protected T createChunkIfMissing(int blockX, int blockZ) {
		int chunkX = MCAUtil.blockToChunk(blockX), chunkZ = MCAUtil.blockToChunk(blockZ);
		T chunk = getChunk(chunkX, chunkZ);
		if (chunk == null) {
			chunk = newChunk();
			setChunk(getChunkIndex(chunkX, chunkZ), chunk);
		}
		return chunk;
	}

	@Override
	public ChunkIterator<T> iterator() {
		return new ChunkIteratorImpl<>(this);
	}

	public Stream<T> stream() {
		return StreamSupport.stream(spliterator(), false);
	}

	protected static class ChunkIteratorImpl<I extends Chunk> implements ChunkIterator<I> {
		private final MCAFileBase<I> owner;
		private int currentIndex;

		public ChunkIteratorImpl(MCAFileBase<I> owner) {
			this.owner = owner;
			currentIndex = -1;
		}

		@Override
		public boolean hasNext() {
			return currentIndex < 1023;
		}

		@Override
		public I next() {
			if (!hasNext()) throw new NoSuchElementException();
			return owner.getChunk(++currentIndex);
		}

		@Override
		public void remove() {
			owner.setChunk(currentIndex, null);
		}

		@Override
		public void set(I chunk) {
			owner.setChunk(currentIndex, chunk);
		}

		@Override
		public int currentIndex() {
			return currentIndex;
		}

		@Override
		public int currentX() {
			return currentIndex & 0x1F;
		}

		@Override
		public int currentZ() {
			return (currentIndex >> 5) & 0x1F;
		}
	}
}
