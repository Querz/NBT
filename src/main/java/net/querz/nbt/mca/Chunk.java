package net.querz.nbt.mca;

import net.querz.nbt.CompoundTag;
import net.querz.nbt.ListTag;
import net.querz.nbt.Tag;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.DeflaterOutputStream;

public class Chunk {

	public static final int DEFAULT_DATA_VERSION = 1628;

	private CompoundTag data;
	private int offsetInSectors;
	private int sizeInSectors;
	private CompressionType compressionType;
	private int rawDataLength;
	private int lastUpdate;
	private int posX, posZ;

	Chunk(int offsetInSectors, int sizeInSectors, int lastUpdate) {
		this.offsetInSectors = offsetInSectors;
		this.sizeInSectors = sizeInSectors;
		this.lastUpdate = lastUpdate;
	}

	public Chunk(CompoundTag data) {
		if (data == null) {
			throw new NullPointerException("data cannot be null");
		}
		if (!data.containsKey("Level")) {
			throw new IllegalArgumentException("data does not contain \"Level\" tag");
		}
		CompoundTag level = data.getCompoundTag("Level");
		if (!level.containsKey("xPos") || !level.containsKey("zPos")) {
			throw new IllegalArgumentException("data does not contain \"xPos\" or \"zPos\" tag");
		}
		this.posX = data.getCompoundTag("Level").getInt("xPos");
		this.posZ = data.getCompoundTag("Level").getInt("zPos");
		this.data = data;
	}

	public CompoundTag getData() {
		return data;
	}

	void setData(CompoundTag data) {
		this.data = data;
	}

	public int getPosX() {
		return posX;
	}

	public int getPosZ() {
		return posZ;
	}

	public int getOffset() {
		return offsetInSectors;
	}

	public CompressionType getCompressionType() {
		return compressionType;
	}

	public void setCompressionType(CompressionType compressionType) {
		this.compressionType = compressionType;
	}

	public int getRawDataLength() {
		return rawDataLength;
	}

	public int getSizeInSectors() {
		return sizeInSectors;
	}

	public int getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(int lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	/**
	 * Sets the biome id at the specific location.
	 * @param blockX The absolute x-coordinate of the block.
	 * @param blockZ The absolute z-coordinate of the block.
	 * @param biomeID The biome id.
	 * */
	public void setBiomeAt(int blockX, int blockZ, int biomeID) {
		int[] biomes = data.getCompoundTag("Level").getIntArray("Biomes");
		if (biomes.length == 0) {
			biomes = new int[256];
			for (int i = 0; i < biomes.length; i++) {
				biomes[i] = -1;
			}
			data.getCompoundTag("Level").putIntArray("Biomes", biomes);
		}
		biomes[getSectionIndex(blockX, 0, blockZ)] = biomeID;
	}

	/**
	 * Returns the biome id at a specific location.
	 * @param blockX The x-coordinate of the block.
	 * @param blockZ The z-coordinate of the block.
	 * @return The biome id or -1 if there is no chunk or no biome data.
	 * */
	public int getBiomeAt(int blockX, int blockZ) {
		if (data == null) {
			return -1;
		}
		int[] biomes = data.getCompoundTag("Level").getIntArray("Biomes");
		if (biomes.length == 0) {
			return -1;
		}
		return biomes[getSectionIndex(blockX, 0, blockZ)];
	}

	public void cleanupAllPalettesAndBlockStates() {
		for (CompoundTag section : data.getCompoundTag("Level").getListTag("Sections").asCompoundTagList()) {
			cleanupPaletteAndBlockStates(section);
		}
	}

	/**
	 * Searches for redundant blocks in the palette in the section of the provided coordinates,
	 * removes them and updates the palette indices in the BlockStates accordingly.
	 * Changes nothing if there is no chunk or no section at the coordinates.
	 * @param sectionY The y-coordinate of the section.
	 * */
	public void cleanupPaletteAndBlockStates(int sectionY) {
		for (CompoundTag section : data.getCompoundTag("Level").getListTag("Sections").asCompoundTagList()) {
			if (section.getByte("Y") == sectionY) {
				cleanupPaletteAndBlockStates(section);
			}
		}
	}

	public String getStatus() {
		if (data == null) {
			return null;
		}
		return data.getCompoundTag("Level").getString("Status");
	}

	public void setStatus(String status) {
		if (data == null) {
			data = createDefaultChunkData(posX, posZ);
		}
		data.getCompoundTag("Level").putString("Status", status);
	}

	private void cleanupPaletteAndBlockStates(CompoundTag section) {
		long[] blockStates = section.getLongArray("BlockStates");
		ListTag<CompoundTag> palette = section.getListTag("Palette").asCompoundTagList();
		Map<Integer, Integer> oldToNewMapping = cleanupPalette(blockStates, palette);
		blockStates = adjustBlockStateBits(blockStates, palette, oldToNewMapping);
		section.putLongArray("BlockStates", blockStates);
	}

	long[] adjustBlockStateBits(long[] blockStates, ListTag<CompoundTag> palette, Map<Integer, Integer> oldToNewMapping) {
		//increases or decreases the amount of bits used per BlockState
		//based on the size of the palette. oldToNewMapping can be used to update indices
		//if the palette had been cleaned up before using MCAFile#cleanupPalette().

		int newBits = 32 - Integer.numberOfLeadingZeros(palette.size() - 1);
		newBits = newBits < 4 ? 4 : newBits;

		long[] newBlockStates = newBits == blockStates.length / 64 ? blockStates : new long[newBits * 64];
		if (oldToNewMapping != null) {
			for (int i = 0; i < 4096; i++) {
				setPaletteIndex(i, oldToNewMapping.get(getPaletteIndex(i, blockStates)), newBlockStates);
			}
		} else {
			for (int i = 0; i < 4096; i++) {
				setPaletteIndex(i, getPaletteIndex(i, blockStates), newBlockStates);
			}
		}

		return newBlockStates;
	}

	Map<Integer, Integer> cleanupPalette(long[] blockStates, ListTag<CompoundTag> palette) {
		//create index - palette mapping
		Map<Integer, Integer> allIndices = new HashMap<>();
		for (int i = 0; i < 4096; i++) {
			int paletteIndex = getPaletteIndex(i, blockStates);
			allIndices.put(paletteIndex, paletteIndex);
		}
		//delete unused blocks from palette
		int index = 1;
		for (int i = 1; i < palette.size(); i++) {
			if (!allIndices.containsKey(index)) {
				palette.remove(i);
				i--;
			} else {
				allIndices.put(index, i);
			}
			index++;
		}

		return allIndices;
	}

	/**
	 * Returns the index of the block data in the palette.
	 * @param index The index of the block in this section, ranging from 0-4095.
	 * @param blockStates  The BlockStates that store the palette indices.
	 * @return The index of the block data in the palette.
	 * */
	public static int getPaletteIndex(int index, long[] blockStates) {
		int bits = blockStates.length >> 6;
		double blockStatesIndex = index / (4096D / blockStates.length);
		int longIndex = (int) blockStatesIndex;
		int startBit = (int) ((blockStatesIndex - Math.floor(blockStatesIndex)) * 64D);
		if (startBit + bits > 64) {
			long prev = bitRange(blockStates[longIndex], startBit, 64);
			long next = bitRange(blockStates[longIndex + 1], 0, startBit + bits - 64);
			return (int) ((next << 64 - startBit) + prev);
		} else {
			return (int) bitRange(blockStates[longIndex], startBit, startBit + bits);
		}
	}

	/**
	 * Sets the index of the block data in the BlockStates. Does not adjust the size of the BlockStates array.
	 * @param index The index of the block in this section, ranging from 0-4095.
	 * @param state The block state to be set (index of block data in the palette).
	 * @param blockStates  The BlockStates that store the palette indices.
	 * */
	public static void setPaletteIndex(int index, int state, long[] blockStates) {
		int bits = blockStates.length / 64;
		double blockStatesIndex = index / (4096D / blockStates.length);
		int longIndex = (int) blockStatesIndex;
		int startBit = (int) ((blockStatesIndex - Math.floor(longIndex)) * 64D);
		if (startBit + bits > 64) {
			blockStates[longIndex] = updateBits(blockStates[longIndex], state, startBit, 64);
			blockStates[longIndex + 1] = updateBits(blockStates[longIndex + 1], state, startBit - 64, startBit + bits - 64);
		} else {
			blockStates[longIndex] = updateBits(blockStates[longIndex], state, startBit, startBit + bits);
		}
	}

	static long updateBits(long n, long m, int i, int j) {
		//replace i to j in n with j - i bits of m
		long mShifted = i > 0 ? (m & ((1L << j - i) - 1)) << i : (m & ((1L << j - i) - 1)) >>> -i;
		return ((n & ((j > 63 ? 0 : (~0L << j)) | (i < 0 ? 0 : ((1L << i) - 1L)))) | mShifted);
	}

	static long bitRange(long value, int from, int to) {
		int waste = 64 - to;
		return (value << waste) >>> (waste + from);
	}

	/**
	 * Sets block data at specific block coordinates. If there is no chunk data or no section data
	 * at the provided location, it will create default data ({@link Chunk#createDefaultChunk(int, int)},
	 * {@link Chunk#createDefaultSection(int)}).
	 * If the size of the palette reaches a number that is a power of 2, it will automatically increase
	 * the size of the BlockStates. This cleanup procedure ONLY occurs in this case, EXCEPT if {@code cleanup}
	 * is set to {@code true}. The reason for this is a rather high performance impact of the cleanup process.
	 * This may lead to unused block states in the palette, but never to an unnecessarily large number of bits
	 * used per block state in the BlockStates array.
	 * A manual cleanup can be performed using {@link Chunk#cleanupPaletteAndBlockStates(int)}.
	 * @param blockX The absolute x-coordinate of the block.
	 * @param blockY The absolute y-coordinate of the block.
	 * @param blockZ The absolute z-coordinate of the block.
	 * @param state The block data.
	 * @param cleanup If the cleanup procedure should be forced.
	 * */
	public void setBlockStateAt(int blockX, int blockY, int blockZ, CompoundTag state, boolean cleanup) {
		if (data == null) {
			this.data = createDefaultChunk(MCAUtil.blockToChunk(blockX), MCAUtil.blockToChunk(blockZ)).data;
		}

		int blockSection = MCAUtil.blockToChunk(blockY);
		for (CompoundTag section : data.getCompoundTag("Level").getListTag("Sections").asCompoundTagList()) {
			if (section.getByte("Y") == blockSection) {
				long[] blockStates = section.getLongArray("BlockStates");
				ListTag<CompoundTag> palette = section.getListTag("Palette").asCompoundTagList();

				int paletteIndex;
				if ((paletteIndex = palette.indexOf(state)) != -1) {

					//data already exists in palette, so there's nothing to do
					setPaletteIndex(getSectionIndex(blockX, blockY, blockZ), paletteIndex, blockStates);
					if (cleanup) {
						cleanupPaletteAndBlockStates(MCAUtil.blockToChunk(blockY));
					}
				} else {
					palette.add(state);

					paletteIndex = palette.size() - 1;

					long[] newBlockStates = blockStates;

					//power of 2 --> bits must increase
					if ((paletteIndex & (paletteIndex - 1)) == 0) {
						newBlockStates = adjustBlockStateBits(blockStates, palette, null);
						setPaletteIndex(getSectionIndex(blockX, blockY, blockZ), paletteIndex, newBlockStates);
					} else {
						//bits did not increase, change the index
						setPaletteIndex(getSectionIndex(blockX, blockY, blockZ), paletteIndex, newBlockStates);
					}

					if (cleanup || blockStates.length != newBlockStates.length) {
						Map<Integer, Integer> oldToNewMapping = cleanupPalette(newBlockStates, palette);
						newBlockStates = adjustBlockStateBits(newBlockStates, palette, oldToNewMapping);
					}
					section.putLongArray("BlockStates", newBlockStates);
				}
				return;
			}
		}

		//create new section
		CompoundTag section = createDefaultSection(MCAUtil.blockToChunk(blockY));
		ListTag<CompoundTag> palette = section.getListTag("Palette").asCompoundTagList();
		if (palette.indexOf(state) == 0) {
			return;
		} else {
			palette.add(state);
			setPaletteIndex(getSectionIndex(blockX, blockY, blockZ), 1, section.getLongArray("BlockStates"));
		}
		data.getCompoundTag("Level").getListTag("Sections").asCompoundTagList().add(section);
	}

	/**
	 * Returns the block data at the provided block coordinates.
	 * @param blockX The x-coordinate of the block.
	 * @param blockY The y-coordinate of the block.
	 * @param blockZ The z-coordinate of the block.
	 * @return The block data at the specific block coordinates from the palette in this section.
	 * Returns {@code null} if there is no chunk data or no section.
	 * */
	public CompoundTag getBlockStateAt(int blockX, int blockY, int blockZ) {
		//get chunk in this region
		if (data == null) {
			return null;
		}

		//get section
		int blockSection = MCAUtil.blockToChunk(blockY);
		for (CompoundTag section : data.getCompoundTag("Level").getListTag("Sections").asCompoundTagList()) {
			if (section.getByte("Y") == blockSection) {
				//get index of long in block index array
				long[] blockStates = section.getLongArray("BlockStates");
				ListTag<CompoundTag> palette = section.getListTag("Palette").asCompoundTagList();

				//convert block coordinates into section coordinates
				int index = getSectionIndex(blockX, blockY, blockZ);

				int paletteIndex = getPaletteIndex(index, blockStates);

				return palette.get(paletteIndex);
			}
		}
		return null;
	}

	public int serialize(RandomAccessFile raf, int offsetInSectors) throws IOException {
		this.offsetInSectors = offsetInSectors;

		if (data == null) {
			return 0;
		}

		raf.seek(offsetInSectors * 4096);

		//write chunk data
		ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
		try (DataOutputStream nbtOut = new DataOutputStream(new BufferedOutputStream(new DeflaterOutputStream(baos)))) {
			data.serialize(nbtOut, 0);
		}
		byte[] rawData = baos.toByteArray();
		raf.writeInt(rawData.length);
		raf.writeByte(2);
		raf.write(rawData);
		return rawData.length + 5;
	}

	public void deserialize(RandomAccessFile raf) throws IOException {
		raf.seek(offsetInSectors * 4096);

		rawDataLength = raf.readInt();

		DataInputStream dis;

		byte ctb = raf.readByte();
		compressionType = CompressionType.getFromID(ctb);
		if (compressionType == null) {
			throw new IOException("invalid compression type " + ctb);
		}
		dis = new DataInputStream(new BufferedInputStream(compressionType.decompress(new FileInputStream(raf.getFD()))));

		Tag tag = Tag.deserialize(dis, 0);

		if (tag instanceof CompoundTag) {
			data = (CompoundTag) tag;
			if (data.containsKey("Level")) {
				posX = data.getCompoundTag("Level").getInt("xPos");
				posZ = data.getCompoundTag("Level").getInt("zPos");
			}
		} else {
			throw new IOException("invalid data tag at offset " + offsetInSectors + ": " + (tag == null ? "null" : tag.getClass().getName()));
		}
	}

	private static CompoundTag createDefaultChunkData(int xPos, int zPos) {
		CompoundTag chunk = new CompoundTag();
		chunk.putInt("DataVersion", DEFAULT_DATA_VERSION);
		CompoundTag level = new CompoundTag();
		level.putInt("xPos", xPos);
		level.putInt("zPos", zPos);
		level.put("Entities", new ListTag());
		level.put("Sections", new ListTag());
		level.putString("Status", "mobs_spawned");
		chunk.put("Level", level);
		return chunk;
	}

	public static Chunk createDefaultChunk(int xPos, int zPos) {
		return new Chunk(createDefaultChunkData(xPos, zPos));
	}

	public static CompoundTag createDefaultSection(int y) {
		CompoundTag section = new CompoundTag();
		section.putByte("Y", (byte) y);
		ListTag<CompoundTag> palette = new ListTag<>();
		CompoundTag air = new CompoundTag();
		air.putString("Name", "minecraft:air");
		palette.add(air);
		section.put("Palette", palette);
		section.putLongArray("BlockStates", new long[256]);
		return section;
	}

	int getSectionIndex(int blockX, int blockY, int blockZ) {
		return (blockY & 15) * 256 + (blockZ & 15) * 16 + (blockX & 15);
	}
}
