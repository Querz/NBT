package net.querz.mca.parsers.impl.anvil118;

import net.querz.mca.parsers.BiomeParser;
import net.querz.nbt.CompoundTag;
import net.querz.nbt.ListTag;
import net.querz.nbt.StringTag;
import net.querz.nbt.Tag;

import java.util.HashMap;
import java.util.Map;

public class BiomeParser118 implements BiomeParser<String> {

	private final CompoundTag section;

	private ListTag palette;
	private long[] data;
	private byte[] parsedIndexes;
	private Map<String, Byte> paletteSet;


	public BiomeParser118(CompoundTag section) {
		this.section = section;
		if (section.contains("biomes", Tag.Type.COMPOUND)) {
			CompoundTag biomes = section.getCompound("biomes");
			if (biomes.contains("palette", Tag.Type.LIST)) {
				palette = biomes.getList("palette").copy();
			}
			if (biomes.contains("data", Tag.Type.LONG_ARRAY)) {
				data = biomes.getLongArray("data");
			}
		}
	}

	private int getBiomeIndex(int blockX, int blockY, int blockZ) {
		return (blockY >> 2 & 0x3) * 16 + (blockZ >> 2 & 0x3) * 4 + (blockX >> 2 & 0x3);
	}

	@Override
	public String getBiomeAt(int blockX, int blockY, int blockZ) {
		return getDataAt(getBiomeIndex(blockX, blockY, blockZ));
	}

	@Override
	public String getDataAt(int index) {
		if (parsedIndexes == null) {
			parseIndexes();
		}
		return palette.getString(parsedIndexes[index]);
	}

	private void parseIndexes() {
		parsedIndexes = new byte[64];
		if (data == null) {
			return;
		}
		int numberOfBits = 32 - Integer.numberOfLeadingZeros(Math.max(palette.size() - 1, 1));
		int bytesPerLong = Math.floorDiv(64, numberOfBits);
		int mask = (1 << numberOfBits) - 1;
		int i = 0;
		for (long l : data) {
			for (int b = 0; b < bytesPerLong && i < 64; b++) {
				parsedIndexes[i++] = (byte) (l & mask);
				l >>= numberOfBits;
			}
		}
	}

	private void parsePalette() {
		paletteSet = new HashMap<>((int) Math.ceil(palette.size() * 1.25f));
		for (byte i = 0; i < palette.size(); i++) {
			paletteSet.put(palette.getString(i), i);
		}
	}

	@Override
	public void setBiomeAt(int blockX, int blockY, int blockZ, String data) {
		setDataAt(getBiomeIndex(blockX, blockY, blockZ), data);
	}

	@Override
	public void setDataAt(int index, String data) {
		if (parsedIndexes == null) {
			parseIndexes();
		}
		if (paletteSet == null) {
			parsePalette();
		}
		byte i;
		if (paletteSet.containsKey(data)) {
			i = paletteSet.get(data);
		} else {
			i = (byte) palette.size();
			palette.add(StringTag.valueOf(data));
			paletteSet.put(data, i);
		}
		parsedIndexes[index] = i;
	}

	@Override
	public int getSize() {
		return 64;
	}

	@Override
	public void apply() {
		// collect all used biomes
		Byte[] usedBiomes = new Byte[palette.size()];
		for (byte b : parsedIndexes) {
			usedBiomes[b] = b;
		}

		// count number of used biomes
		int numberOfUsedBiomes = 0;
		for (Byte usedBiome : usedBiomes) {
			if (usedBiome != null) {
				numberOfUsedBiomes++;
			}
		}

		// cleanup palette if we have unused block states
		if (numberOfUsedBiomes < palette.size()) {
			ListTag newPalette = new ListTag();
			byte[] oldToNewMapping = new byte[palette.size()];
			for (byte i = 0, j = 0; i < palette.size(); i++) {
				if (usedBiomes[i] != null) {
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

		if (numberOfUsedBiomes == 1) {
			data = null;
			applyToHandle(palette, null);
			return;
		}

		int numberOfBits = 32 - Integer.numberOfLeadingZeros(palette.size() - 1);

		long[] newBiomes;
		int bytesPerLong = Math.floorDiv(64, numberOfBits);
		int newLength = (int) Math.ceil(64D / bytesPerLong);
		newBiomes = data == null || newLength != data.length ? new long[newLength] : data;

		for (int i = 0; i < newLength - 1; i++) {
			long l = 0L;
			for (int j = 0; j < bytesPerLong - 1; j++) {
				l += parsedIndexes[(i + 1) * bytesPerLong - j - 1];
				l <<= numberOfBits;
			}
			l += parsedIndexes[i * bytesPerLong];

			newBiomes[i] = l;
		}
		{
			int i = newLength - 1;
			long l = 0L;
			int jm = 64 - (newLength - 1) * bytesPerLong;
			for (int j = 0; j < jm - 1; j++) {
				l += parsedIndexes[i * bytesPerLong + jm - j - 1];
				l <<= numberOfBits;
			}
			l += parsedIndexes[i * bytesPerLong];

			newBiomes[newLength - 1] = l;
		}
		data = newBiomes;
		applyToHandle(palette, data);
	}

	private void applyToHandle(ListTag palette, long[] data) {
		CompoundTag biomes = section.getCompound("biomes");
		biomes.put("palette", palette);
		if (data == null) {
			biomes.remove("data");
		} else {
			biomes.putLongArray("data", data);
		}
	}
}
