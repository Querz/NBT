package net.querz.nbt.mca;

import net.querz.nbt.CompoundTag;
import net.querz.nbt.EndTag;
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
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

/**
 * a complete representation of an .mca file, capable of reading and saving.
 * */
public class MCAFile {

	public static final int DEFAULT_DATA_VERSION = 1628;

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

	public int getBiomeAt(int blockX, int blockZ) {
		CompoundTag chunkData = getChunkData(MCAUtil.blockToChunk(blockX), MCAUtil.blockToChunk(blockZ));
		if (chunkData == null) {
			return -1;
		}
		int[] biomes = chunkData.getCompoundTag("Level").getIntArray("Biomes");
		if (biomes.length == 0) {
			return -1;
		}
		return biomes[getSectionIndex(blockX, 0, blockZ)];
	}

	public void setBlockDataAt(int blockX, int blockY, int blockZ, CompoundTag data, boolean cleanup) {
		CompoundTag chunkData = getChunkData(MCAUtil.blockToChunk(blockX), MCAUtil.blockToChunk(blockZ));
		if (chunkData == null) {
			chunkData = createDefaultChunk(MCAUtil.blockToChunk(blockX), MCAUtil.blockToChunk(blockZ));
		}

		int blockSection = MCAUtil.blockToChunk(blockY);
		for (CompoundTag section : chunkData.getCompoundTag("Level").getListTag("Sections").asCompoundTagList()) {
			if (section.getByte("Y") == blockSection) {
				long[] blockStates = section.getLongArray("BlockStates");
				ListTag<CompoundTag> palette = section.getListTag("Palette").asCompoundTagList();

				int paletteIndex;
				if ((paletteIndex = palette.indexOf(data)) != -1) {
					//data already exists in palette, so there's nothing to do
					setPaletteIndex(getSectionIndex(blockX, blockY, blockZ), paletteIndex, blockStates);
				} else {

					palette.add(data);

					paletteIndex = palette.size() - 1;

					long[] newBlockStates = blockStates;

					//power of 2 --> if bits are increasing
					if ((palette.size() & (palette.size() - 1)) == 0) {
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
		long[] blockStates = section.getLongArray("BlockStates");
		ListTag<CompoundTag> palette = section.getListTag("Palette").asCompoundTagList();
		if (palette.indexOf(data) == 0) {
			return;
		} else {
			palette.add(data);
			setPaletteIndex(getSectionIndex(blockX, blockY, blockZ), 1, blockStates);
		}
		chunkData.getCompoundTag("Level").getListTag("Sections").asCompoundTagList().add(section);
	}

	private CompoundTag createDefaultSection(int y) {
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

	private CompoundTag createDefaultChunk(int xPos, int zPos) {
		CompoundTag chunk = new CompoundTag();
		chunk.putInt("DataVersion", DEFAULT_DATA_VERSION);
		CompoundTag level = new CompoundTag();
		level.putInt("xPos", xPos);
		level.putInt("zPos", zPos);
		level.put("Entities", new ListTag());
		level.put("Sections", new ListTag());
		level.putString("Status", "base");
		chunk.put("Level", level);
		return chunk;
	}

	private int getSectionIndex(int blockX, int blockY, int blockZ) {
		return (blockY & 15) * 256 + (blockZ & 15) * 16 + (blockX & 15);
	}

	private Map<Integer, Integer> cleanupPalette(long[] blockStates, ListTag<CompoundTag> palette) {
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

	//increases or decreases the amount of bits used per BlockState
	//based on the size of the palette. oldToNewMapping can be used to update indices
	//if the palette had been cleaned up before using MCAFile#cleanupPalette().
	private long[] adjustBlockStateBits(long[] blockStates, ListTag<CompoundTag> palette, Map<Integer, Integer> oldToNewMapping) {
		int newBits = 32 - Integer.numberOfLeadingZeros(palette.size());
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

	private void setPaletteIndex(int index, int state, long[] blockStates) {
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

	private long updateBits(long n, long m, int i, int j) {
        //replace i to j in n with j - i bits of m
		long mShifted = i > 0 ? (m & ((1L << j - i) - 1)) << i : (m & ((1L << j - i) - 1)) >>> -i;
		return ((n & ((j > 63 ? 0 : (~0L << j)) | (i < 0 ? 0 : ((1L << i) - 1L)))) | mShifted);
	}

	public CompoundTag getBlockDataAt(int blockX, int blockY, int blockZ) {
		//get chunk in this region
		CompoundTag chunkData = getChunkData(MCAUtil.blockToChunk(blockX), MCAUtil.blockToChunk(blockZ));

		if (chunkData == null) {
			return null;
		}

		//get section
		int blockSection = MCAUtil.blockToChunk(blockY);
		for (CompoundTag section : chunkData.getCompoundTag("Level").getListTag("Sections").asCompoundTagList()) {
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

	private int getPaletteIndex(int index, long[] blockStates) {
		int bits = blockStates.length / 64;

		double blockStatesIndex = index / (4096D / blockStates.length);

		int longIndex = (int) blockStatesIndex;
		int startBit = (int) ((blockStatesIndex - Math.floor(blockStatesIndex)) * 64D);

		if (startBit + bits > 64) {
			//get msb from current long, no need to cleanup manually, just fill with 0
			int previous = (int) (blockStates[longIndex] >>> startBit);

			//cleanup pattern for bits from next long
			int remainingClean = (1 << startBit + bits - 64) - 1;

			//get lsb from next long
			int next = ((int) blockStates[longIndex + 1]) & remainingClean;
			return (next << 64 - startBit) + previous;
		} else {
			return (int) (blockStates[longIndex] >> startBit) & ((1 << bits) - 1);
		}
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



	public static String longToBinaryString(long n) {
		StringBuilder s = new StringBuilder(Long.toBinaryString(n));
		for (int i = s.length(); i < 64; i++) {
			s.insert(0, "0");
		}
		return s.toString();
	}
}
