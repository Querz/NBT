package net.querz.mca.parsers.impl.anvil118;

import net.querz.mca.parsers.HeightmapParser;
import net.querz.nbt.CompoundTag;
import net.querz.nbt.LongArrayTag;
import net.querz.nbt.Tag;
import java.util.Map;

public class HeightmapParser118 implements HeightmapParser {

	private final long[][] data = new long[6][];
	private final short[][] parsedHeightmaps = new short[6][];

	public HeightmapParser118(CompoundTag chunk) {
		CompoundTag heightmaps = chunk.getCompound("Heightmaps");
		for (Map.Entry<String, Tag> e : heightmaps) {
			data[HeightmapType.valueOf(e.getKey()).getID()] = ((LongArrayTag) e.getValue()).getValue();
		}
	}

	@Override
	public HeightmapData getDataAt(int index) {
		int[] data = new int[6];
		for (HeightmapType type : HeightmapType.values()) {
			data[type.getID()] = getHeightAt(type, index);
		}
		return new HeightmapData(data);
	}

	@Override
	public HeightmapData getDataAt(int blockX, int blockZ) {
		return getDataAt(getHeightmapIndex(blockX, blockZ));
	}

	@Override
	public int getHeightAt(HeightmapType type, int blockX, int blockZ) {
		return getHeightAt(type, getHeightmapIndex(blockX, blockZ));
	}

	@Override
	public int getHeightAt(HeightmapType type, int index) {
		if (data[type.getID()] == null) {
			return 0;
		}
		if (parsedHeightmaps[type.getID()] == null) {
			parseHeightmap(type);
		}
		return parsedHeightmaps[type.getID()][index];
	}

	private int getHeightmapIndex(int blockX, int blockZ) {
		return (blockZ & 0xF) * 16 + (blockX & 0xF);
	}

	private void parseHeightmap(HeightmapType type) {
		short[] heightmap = new short[256];
		int i = 0;
		for (long l : data[type.getID()]) {
			for (int b = 0; b < 7 && i < 256; b++) {
				heightmap[i++] = (short) (l & 0x1FF);
				l >>= 9;
			}
		}
		parsedHeightmaps[type.getID()] = heightmap;
	}

	@Override
	public void setDataAt(int index, HeightmapData data) {
		for (HeightmapType type : HeightmapType.values()) {
			setHeightAt(type, index, data.data()[type.getID()]);
		}
	}

	@Override
	public void setHeightAt(HeightmapType type, int blockX, int blockZ, int height) {
		setHeightAt(type, getHeightmapIndex(blockX, blockZ), height);
	}

	@Override
	public void setHeightAt(HeightmapType type, int index, int height) {
		if (parsedHeightmaps[type.getID()] == null) {
			parseHeightmap(type);
		}
		parsedHeightmaps[type.getID()][index] = (short) height;
	}

	@Override
	public void apply() {
		for (int p = 0; p < parsedHeightmaps.length; p++) {
			if (parsedHeightmaps[p] == null) {
				continue;
			}
			int index = 0;
			for (int i = 0; i < 37; i++) {
				long l = 0L;
				for (int j = 0; j < 7; j++, index++) {
					l += parsedHeightmaps[p][index];
					l <<= 9;
				}
				data[p][i] = l;
			}
		}
	}
}
