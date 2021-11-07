package net.querz.mca;

import net.querz.nbt.io.NBTDeserializer;
import net.querz.nbt.io.NBTSerializer;
import net.querz.nbt.io.NamedTag;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;

import java.io.*;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static net.querz.mca.LoadFlags.*;

public abstract class SectionedChunkBase<T extends Section> implements Iterable<T> {

	protected boolean partial;
	protected boolean raw;
	protected int lastMCAUpdate;
	protected CompoundTag data;
	protected final TreeMap<Integer, T> sections = new TreeMap<>();

	SectionedChunkBase(int lastMCAUpdate) {
		this.lastMCAUpdate = lastMCAUpdate;
	}

	/**
	 * Create a new chunk based on raw base data from a region file.
	 * @param data The raw base data to be used.
	 */
	public SectionedChunkBase(CompoundTag data) {
		this.data = data;
		initReferences(ALL_DATA);
	}

	private void initReferences0(long loadFlags) {
		Objects.requireNonNull(data, "data cannot be null");

		if ((loadFlags != ALL_DATA) && (loadFlags & RAW) != 0) {
			raw = true;
			return;
		}

		initReferences(loadFlags);

		// If we haven't requested the full set of data we can drop the underlying raw data to let the GC handle it.
		if (loadFlags != ALL_DATA) {
			data = null;
			partial = true;
		}
	}

	/**
	 * Child classes should not call this method directly.
	 * Raw and partial data handling is taken care of, this method will not be called if {@code loadFlags} is
	 * {@link LoadFlags#RAW}.
	 */
	protected abstract void initReferences(long loadFlags);

	/**
	 * Serializes this chunk to a <code>RandomAccessFile</code>.
	 * @param raf The RandomAccessFile to be written to.
	 * @param xPos The x-coordinate of the chunk.
	 * @param zPos The z-coodrinate of the chunk.
	 * @return The amount of bytes written to the RandomAccessFile.
	 * @throws UnsupportedOperationException When something went wrong during writing.
	 * @throws IOException When something went wrong during writing.
	 */
	public int serialize(RandomAccessFile raf, int xPos, int zPos) throws IOException {
		if (partial) {
			throw new UnsupportedOperationException("Partially loaded chunks cannot be serialized");
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
		try (BufferedOutputStream nbtOut = new BufferedOutputStream(CompressionType.ZLIB.compress(baos))) {
			new NBTSerializer(false).toStream(new NamedTag(null, updateHandle(xPos, zPos)), nbtOut);
		}
		byte[] rawData = baos.toByteArray();
		raf.writeInt(rawData.length + 1); // including the byte to store the compression type
		raf.writeByte(CompressionType.ZLIB.getID());
		raf.write(rawData);
		return rawData.length + 5;
	}

	/**
	 * Reads chunk data from a RandomAccessFile. The RandomAccessFile must already be at the correct position.
	 * @param raf The RandomAccessFile to read the chunk data from.
	 * @throws IOException When something went wrong during reading.
	 */
	public void deserialize(RandomAccessFile raf) throws IOException {
		deserialize(raf, ALL_DATA);
	}

	/**
	 * Reads chunk data from a RandomAccessFile. The RandomAccessFile must already be at the correct position.
	 * @param raf The RandomAccessFile to read the chunk data from.
	 * @param loadFlags A logical or of {@link LoadFlags} constants indicating what data should be loaded
	 * @throws IOException When something went wrong during reading.
	 */
	public void deserialize(RandomAccessFile raf, long loadFlags) throws IOException {
		byte compressionTypeByte = raf.readByte();
		CompressionType compressionType = CompressionType.getFromID(compressionTypeByte);
		if (compressionType == null) {
			throw new IOException("invalid compression type " + compressionTypeByte);
		}
		BufferedInputStream dis = new BufferedInputStream(compressionType.decompress(new FileInputStream(raf.getFD())));
		NamedTag tag = new NBTDeserializer(false).fromStream(dis);
		if (tag != null && tag.getTag() instanceof CompoundTag) {
			data = (CompoundTag) tag.getTag();
			initReferences0(loadFlags);
		} else {
			throw new IOException("invalid data tag: " + (tag == null ? "null" : tag.getClass().getName()));
		}
	}

	/**
	 * @return The timestamp when this region file was last updated in seconds since 1970-01-01.
	 */
	public int getLastMCAUpdate() {
		return lastMCAUpdate;
	}

	/**
	 * Sets the timestamp when this region file was last updated in seconds since 1970-01-01.
	 * @param lastMCAUpdate The time in seconds since 1970-01-01.
	 */
	public void setLastMCAUpdate(int lastMCAUpdate) {
		checkRaw();
		this.lastMCAUpdate = lastMCAUpdate;
	}

	/**
	 * Fetches the section at the given y-coordinate.
	 * @param sectionY The y-coordinate of the section in this chunk. One section y is equal to 16 world y's
	 * @return The Section.
	 */
	public T getSection(int sectionY) {
		return sections.get(sectionY);
	}

	/**
	 * Sets a section at a given section y-coordinate
	 * @param sectionY The y-coordinate of the section in this chunk. One section y is equal to 16 world y's
	 * @param section The section to be set.
	 */
	public void setSection(int sectionY, T section) {
		checkRaw();
		sections.put(sectionY, section);
	}

	/**
	 * Gets the minimum section y-coordinate.
	 * @return The y of the lowest section in the chunk.
	 */
	public int getMinSectionY() {
		return sections.firstKey();
	}

	/**
	 * Gets the minimum section y-coordinate.
	 * @return The y of the highest populated section in the chunk.
	 */
	public int getMaxSectionY() {
		return sections.lastKey();
	}


	protected void checkRaw() {
		if (raw) {
			throw new UnsupportedOperationException("cannot update field when working with raw data");
		}
	}

	/**
	 * Provides a reference to the full chunk data.
	 * @return The full chunk data or null if there is none, e.g. when this chunk has only been loaded partially.
	 */
	public CompoundTag getHandle() {
		return data;
	}

	public abstract CompoundTag updateHandle(int xPos, int zPos);

	@Override
	public Iterator<T> iterator() {
		return sections.values().iterator();
	}

	public Stream<T> stream() {
		return sections.values().stream();
	}
}
