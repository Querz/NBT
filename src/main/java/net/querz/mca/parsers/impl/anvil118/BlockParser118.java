package net.querz.mca.parsers.impl.anvil118;

import net.querz.mca.parsers.BlockParser;
import net.querz.nbt.CompoundTag;
import net.querz.nbt.ListTag;
import net.querz.nbt.Tag;

import java.util.*;

public class BlockParser118 implements BlockParser<CompoundTag> {

	protected final CompoundTag section;

	private ListTag palette;
	private long[] data;
	private short[] parsedIndexes;
	private Map<CompoundTag, Short> paletteSet;

	public BlockParser118(CompoundTag section) {
		this.section = section;
		if (section.contains("block_states", Tag.COMPOUND)) {
			CompoundTag blockStates = section.getCompound("block_states");
			if (blockStates.contains("palette", Tag.LIST)) {
				palette = blockStates.getList("palette").copy();
			}
			if (blockStates.contains("data", Tag.LONG_ARRAY)) {
				data = blockStates.getLongArray("data");
			}
		}
	}

	@Override
	public CompoundTag getBlockAt(int blockX, int blockY, int blockZ) {
		return getDataAt((blockY & 0xF) * 256 + (blockZ & 0xF) * 16 + (blockX & 0xF));
	}

	@Override
	public CompoundTag getDataAt(int index) {
		if (parsedIndexes == null) {
			parseIndexes();
		}
		return palette.getCompound(parsedIndexes[index]);
	}

	private void parseIndexes() {
		parsedIndexes = new short[4096];
		if (data == null) {
			return;
		}
		int numberOfBits = data.length * 64 / 4096;
		int shortsPerLong = Math.floorDiv(64, numberOfBits);
		int mask = (1 << numberOfBits) - 1;
		int i = 0;
		for (long l : data) {
			for (int s = 0; s < shortsPerLong && i < 4096; s++) {
				parsedIndexes[i++] = (short) (l & mask);
				l >>= numberOfBits;
			}
		}
	}

	private void parsePalette() {
		paletteSet = new HashMap<>((int) Math.ceil(palette.size() * 1.25f));
		for (short i = 0; i < palette.size(); i++) {
			paletteSet.put(palette.getCompound(i), i);
		}
	}

	@Override
	public void setBlockAt(int blockX, int blockY, int blockZ, CompoundTag data) {
		setDataAt((blockY & 0xF) * 256 + (blockZ & 0xF) * 16 + (blockX & 0xF), data);
	}

	@Override
	public void setDataAt(int index, CompoundTag data) {
		if (parsedIndexes == null) {
			parseIndexes();
		}
		if (paletteSet == null) {
			parsePalette();
		}
		short i;
		if (paletteSet.containsKey(data)) {
			i = paletteSet.get(data);
		} else {
			i = (short) palette.size();
			palette.add(data);
			paletteSet.put(data, i);
		}
		parsedIndexes[index] = i;
	}

	@Override
	public int getSize() {
		return 4096;
	}

	@Override
	public void apply() {
		// collect all used block states
		Short[] usedBlockStates = new Short[palette.size()];
		for (short b : parsedIndexes) {
			usedBlockStates[b] = b;
		}

		// count number of used block states
		int numberOfUsedBlockStates = 0;
		for (Short usedBlockState : usedBlockStates) {
			if (usedBlockState != null) {
				numberOfUsedBlockStates++;
			}
		}

		// cleanup palette if we have unused block states
		if (numberOfUsedBlockStates < palette.size()) {
			ListTag newPalette = new ListTag();
			short[] oldToNewMapping = new short[palette.size()];
			for (short i = 0, j = 0; i < palette.size(); i++) {
				if (usedBlockStates[i] != null) {
					newPalette.add(palette.get(i));
					oldToNewMapping[i] = j;
					j++;
				}
			}

			for (int i = 0; i < parsedIndexes.length; i++) {
				parsedIndexes[i] = oldToNewMapping[parsedIndexes[i]];
			}

			palette = newPalette;
		}

		if (numberOfUsedBlockStates == 1) {
			data = null;
			applyToHandle(palette, null);
			return;
		}

		int numberOfBits = Math.max(32 - Integer.numberOfLeadingZeros(palette.size() - 1), 4);

		long[] newBlockStates;
		int shortsPerLong = Math.floorDiv(64, numberOfBits);
		int newLength = (int) Math.ceil(4096D / shortsPerLong);
		newBlockStates = data == null || newLength != data.length ? new long[newLength] : data;


		int index = 0;
		for (int i = 0; i < newLength; i++) {
			long l = 0L;
			for (int j = 0; j < shortsPerLong; j++, index++) {
				l += parsedIndexes[index];
				l <<= numberOfBits;
			}
			newBlockStates[i] = l;
		}
		data = newBlockStates;
		applyToHandle(palette, data);
	}

	private void applyToHandle(ListTag palette, long[] data) {
		CompoundTag blockStates = section.getCompound("block_states");
		blockStates.put("palette", palette);
		if (data == null) {
			blockStates.remove("data");
		} else {
			blockStates.putLongArray("data", data);
		}
	}
}
