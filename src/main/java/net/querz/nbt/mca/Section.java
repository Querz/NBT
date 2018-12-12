package net.querz.nbt.mca;

import net.querz.nbt.CompoundTag;
import net.querz.nbt.ListTag;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Section {

	private CompoundTag data;
	private Map<String, List<PaletteIndex>> valueIndexedPalette = new HashMap<>();
	private ListTag<CompoundTag> palette;
	private byte[] blockLight;
	private long[] blockStates;
	private byte[] skyLight;

	public Section(CompoundTag sectionRoot) {
		data = sectionRoot;
		palette = sectionRoot.getListTag("Palette").asCompoundTagList();
		for (int i = 0; i < palette.size(); i++) {
			CompoundTag data = palette.get(i);
			putValueIndexedPalette(data, i);
		}
		blockLight = sectionRoot.getByteArray("BlockLight");
		blockStates = sectionRoot.getLongArray("BlockStates");
		skyLight = sectionRoot.getByteArray("SkyLight");
	}

	Section() {}

	void putValueIndexedPalette(CompoundTag data, int index) {
		PaletteIndex leaf = new PaletteIndex(data, index);
		String name = data.getString("Name");
		List<PaletteIndex> leaves = valueIndexedPalette.get(name);
		if (leaves == null) {
			leaves = new ArrayList<>(1);
			leaves.add(leaf);
			valueIndexedPalette.put(name, leaves);
		} else {
			for (PaletteIndex pal : leaves) {
				if (pal.data.equals(data)) {
					return;
				}
			}
			leaves.add(leaf);
		}
	}

	PaletteIndex getValueIndexedPalette(CompoundTag data) {
		List<PaletteIndex> leaves = valueIndexedPalette.get(data.getString("Name"));
		if (leaves == null) {
			return null;
		}
		for (PaletteIndex leaf : leaves) {
			if (leaf.data.equals(data)) {
				return leaf;
			}
		}
		return null;
	}

	private class PaletteIndex {

		CompoundTag data;
		int index;

		PaletteIndex(CompoundTag data, int index) {
			this.data = data;
			this.index = index;
		}
	}

	public CompoundTag getBlockStateAt(int blockX, int blockY, int blockZ) {
		int index = getBlockIndex(blockX, blockY, blockZ);
		int paletteIndex = getPaletteIndex(index);
		return palette.get(paletteIndex);
	}

	public void setBlockStateAt(int blockX, int blockY, int blockZ, CompoundTag state, boolean cleanup) {
		int paletteSizeBefore = palette.size();
		int paletteIndex = addToPalette(state);
		//power of 2 --> bits must increase, but only if the palette size changed
		//otherwise we would attempt to update all blockstates and the entire palette
		//every time an existing blockstate was added while having 2^x blockstates in the palette
		if (paletteSizeBefore != palette.size() && (paletteIndex & (paletteIndex - 1)) == 0) {
			adjustBlockStateBits(null, blockStates);
			cleanup = true;
		}

		setPaletteIndex(getBlockIndex(blockX, blockY, blockZ), paletteIndex, blockStates);

		if (cleanup) {
			cleanupPaletteAndBlockStates();
		}
	}

	/**
	 * Returns the index of the block data in the palette.
	 * @param blockStateIndex The index of the block in this section, ranging from 0-4095.
	 * @return The index of the block data in the palette.
	 * */
	public int getPaletteIndex(int blockStateIndex) {
		int bits = blockStates.length >> 6;
		double blockStatesIndex = blockStateIndex / (4096D / blockStates.length);
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
	 * @param blockIndex The index of the block in this section, ranging from 0-4095.
	 * @param paletteIndex The block state to be set (index of block data in the palette).
	 * @param blockStates The block states to be updated.
	 * */
	public void setPaletteIndex(int blockIndex, int paletteIndex, long[] blockStates) {
		int bits = blockStates.length / 64;
		double blockStatesIndex = blockIndex / (4096D / blockStates.length);
		int longIndex = (int) blockStatesIndex;
		int startBit = (int) ((blockStatesIndex - Math.floor(longIndex)) * 64D);
		if (startBit + bits > 64) {
			blockStates[longIndex] = updateBits(blockStates[longIndex], paletteIndex, startBit, 64);
			blockStates[longIndex + 1] = updateBits(blockStates[longIndex + 1], paletteIndex, startBit - 64, startBit + bits - 64);
		} else {
			blockStates[longIndex] = updateBits(blockStates[longIndex], paletteIndex, startBit, startBit + bits);
		}
	}

	ListTag<CompoundTag> getPalette() {
		return palette;
	}

	int addToPalette(CompoundTag data) {
		PaletteIndex index;
		if ((index = getValueIndexedPalette(data)) != null) {
			return index.index;
		}
		palette.add(data);
		putValueIndexedPalette(data, palette.size() - 1);
		return palette.size() - 1;
	}

	int getBlockIndex(int blockX, int blockY, int blockZ) {
		return (blockY & 0xF) * 256 + (blockZ & 0xF) * 16 + (blockX & 0xF);
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

	public void cleanupPaletteAndBlockStates() {
		Map<Integer, Integer> oldToNewMapping = cleanupPalette();
		adjustBlockStateBits(oldToNewMapping, blockStates);
	}

	Map<Integer, Integer> cleanupPalette() {
		//create index - palette mapping
		Map<Integer, Integer> allIndices = new HashMap<>();
		for (int i = 0; i < 4096; i++) {
			int paletteIndex = getPaletteIndex(i);
			allIndices.put(paletteIndex, paletteIndex);
		}
		//delete unused blocks from palette
		//start at index 1 because we need to keep minecraft:air
		int index = 1;
		valueIndexedPalette = new HashMap<>(valueIndexedPalette.size());
		putValueIndexedPalette(palette.get(0), 0);
		for (int i = 1; i < palette.size(); i++) {
			if (!allIndices.containsKey(index)) {
				palette.remove(i);
				i--;
			} else {
				putValueIndexedPalette(palette.get(i), i);
				allIndices.put(index, i);
			}
			index++;
		}

		return allIndices;
	}

	void adjustBlockStateBits(Map<Integer, Integer> oldToNewMapping, long[] blockStates) {
		//increases or decreases the amount of bits used per BlockState
		//based on the size of the palette. oldToNewMapping can be used to update indices
		//if the palette had been cleaned up before using MCAFile#cleanupPalette().

		int newBits = 32 - Integer.numberOfLeadingZeros(palette.size() - 1);
		newBits = newBits < 4 ? 4 : newBits;

		long[] newBlockStates = newBits == blockStates.length / 64 ? blockStates : new long[newBits * 64];
		if (oldToNewMapping != null) {
			for (int i = 0; i < 4096; i++) {
				setPaletteIndex(i, oldToNewMapping.get(getPaletteIndex(i)), newBlockStates);
			}
		} else {
			for (int i = 0; i < 4096; i++) {
				setPaletteIndex(i, getPaletteIndex(i), newBlockStates);
			}
		}
		this.blockStates = newBlockStates;
	}

	public byte[] getBlockLight() {
		return blockLight;
	}

	public void setBlockLight(byte[] blockLight) {
		if (blockLight != null && blockLight.length != 2048) {
			throw new IllegalArgumentException("BlockLight array must have a length of 2048");
		}
		this.blockLight = blockLight;
	}

	public long[] getBlockStates() {
		return blockStates;
	}

	public void setBlockStates(long[] blockStates) {
		if (blockStates == null) {
			throw new NullPointerException("BlockStates cannot be null");
		} else if (blockStates.length % 64 != 0 || blockStates.length < 256 || blockStates.length > 4096) {
			throw new IllegalArgumentException("BlockStates must have a length > 255 and < 4097 and must be divisible by 64");
		}
		this.blockStates = blockStates;
	}

	public byte[] getSkyLight() {
		return skyLight;
	}

	public void setSkyLight(byte[] skyLight) {
		if (skyLight != null && skyLight.length != 2048) {
			throw new IllegalArgumentException("SkyLight array must have a length of 2048");
		}
		this.skyLight = skyLight;
	}

	public static Section newSection() {
		Section s = new Section();
		s.blockStates = new long[256];
		s.palette = new ListTag<>(CompoundTag.class);
		CompoundTag air = new CompoundTag();
		air.putString("Name", "minecraft:air");
		s.palette.add(air);
		s.data = new CompoundTag();
		return s;
	}

	public CompoundTag updateHandle(int y) {
		data.putByte("Y", (byte) y);
		data.put("Palette", palette);
		if (blockLight != null) data.putByteArray("BlockLight", blockLight);
		data.putLongArray("BlockStates", blockStates);
		if (skyLight != null) data.putByteArray("SkyLight", skyLight);
		return data;
	}
}
