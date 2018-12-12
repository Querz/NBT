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

public class Chunk {

	public static final int DEFAULT_DATA_VERSION = 1628;

	private int lastMCAUpdate;

	private CompoundTag data;

	private int dataVersion;
	private long lastUpdate;
	private long inhabitedTime;
	private int[] biomes;
	private CompoundTag heightMaps;
	private CompoundTag carvingMasks;
	private Section[] sections = new Section[16]; //always initialized with size = 16 for fast access
	private ListTag<CompoundTag> entities;
	private ListTag<CompoundTag> tileEntities;
	private ListTag<CompoundTag> tileTicks;
	private ListTag<CompoundTag> liquidTicks;
	private ListTag<ListTag<?>> lights;
	private ListTag<ListTag<?>> liquidsToBeTicked;
	private ListTag<ListTag<?>> toBeTicked;
	private ListTag<ListTag<?>> postProcessing;
	private String status;
	private CompoundTag structures;

	Chunk(int lastMCAUpdate) {
		this.lastMCAUpdate = lastMCAUpdate;
	}

	public Chunk(CompoundTag data) {
		this.data = data;
		initReferences();
	}

	private void initReferences() {
		if (data == null) {
			throw new NullPointerException("data cannot be null");
		}
		CompoundTag level;
		if ((level = data.getCompoundTag("Level")) == null) {
			throw new IllegalArgumentException("data does not contain \"Level\" tag");
		}
		this.dataVersion = data.getInt("DataVersion");
		this.inhabitedTime = level.getLong("InhabitedTime");
		this.lastUpdate = level.getLong("LastUpdate");
		this.biomes = level.getIntArray("Biomes");
		this.heightMaps = level.getCompoundTag("HeightMaps");
		this.carvingMasks = level.getCompoundTag("CarvingMasks");
		this.entities = level.containsKey("Entities") ? level.getListTag("Entities").asCompoundTagList() : null;
		this.tileEntities = level.containsKey("TileEntities") ? level.getListTag("TileEntities").asCompoundTagList() : null;
		this.tileTicks = level.containsKey("TileTicks") ? level.getListTag("TileTicks").asCompoundTagList() : null;
		this.liquidTicks = level.containsKey("LiquidTicks") ? level.getListTag("LiquidTicks").asCompoundTagList() : null;
		this.lights = level.containsKey("Lights") ? level.getListTag("Lights").asListTagList() : null;
		this.liquidsToBeTicked = level.containsKey("LiquidsToBeTicked") ? level.getListTag("LiquidsToBeTicked").asListTagList() : null;
		this.toBeTicked = level.containsKey("ToBeTicked") ? level.getListTag("ToBeTicked").asListTagList() : null;
		this.postProcessing = level.containsKey("PostProcessing") ? level.getListTag("PostProcessing").asListTagList() : null;
		this.status = level.getString("Status");
		this.structures = level.getCompoundTag("Structures");
		if (level.containsKey("Sections")) {
			for (CompoundTag section : level.getListTag("Sections").asCompoundTagList()) {
				this.sections[section.getByte("Y")] = new Section(section);
			}
		}
	}

	public int serialize(RandomAccessFile raf, int xPos, int zPos) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
		try (DataOutputStream nbtOut = new DataOutputStream(new BufferedOutputStream(CompressionType.ZLIB.compress(baos)))) {
			updateHandle(xPos, zPos).serialize(nbtOut, 0);
		}
		byte[] rawData = baos.toByteArray();
		raf.writeInt(rawData.length);
		raf.writeByte(CompressionType.ZLIB.getID());
		raf.write(rawData);
		return rawData.length + 5;
	}

	public void deserialize(RandomAccessFile raf) throws IOException {
		byte compressionTypeByte = raf.readByte();
		CompressionType compressionType = CompressionType.getFromID(compressionTypeByte);
		if (compressionType == null) {
			throw new IOException("invalid compression type " + compressionTypeByte);
		}
		DataInputStream dis = new DataInputStream(new BufferedInputStream(compressionType.decompress(new FileInputStream(raf.getFD()))));
		Tag tag = Tag.deserialize(dis, 0);
		if (tag instanceof CompoundTag) {
			data = (CompoundTag) tag;
			initReferences();
		} else {
			throw new IOException("invalid data tag: " + (tag == null ? "null" : tag.getClass().getName()));
		}
	}

	public int getBiomeAt(int blockX, int blockZ) {
		if (biomes == null || biomes.length != 256) {
			return -1;
		}
		return biomes[getBlockIndex(blockX, blockZ)];
	}

	public void setBiomeAt(int blockX, int blockZ, int biomeID) {
		if (biomes == null || biomes.length != 256) {
			biomes = new int[256];
			for (int i = 0; i < biomes.length; i++) {
				biomes[i] = -1;
			}
		}
		biomes[getBlockIndex(blockX, blockZ)] = biomeID;
	}

	public CompoundTag getBlockStateAt(int blockX, int blockY, int blockZ) {
		Section section = sections[MCAUtil.blockToChunk(blockY)];
		if (section == null) {
			return null;
		}
		return section.getBlockStateAt(blockX, blockY, blockZ);
	}

	public void setBlockStateAt(int blockX, int blockY, int blockZ, CompoundTag state, boolean cleanup) {
		int sectionIndex = MCAUtil.blockToChunk(blockY);
		Section section = sections[sectionIndex];
		if (section == null) {
			section = sections[sectionIndex] = Section.newSection();
		}
		section.setBlockStateAt(blockX, blockY, blockZ, state, cleanup);
	}

	public int getDataVersion() {
		return dataVersion;
	}

	public void setDataVersion(int dataVersion) {
		this.dataVersion = dataVersion;
	}

	public int getLastMCAUpdate() {
		return lastMCAUpdate;
	}

	public void setLastMCAUpdate(int lastMCAUpdate) {
		this.lastMCAUpdate = lastMCAUpdate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Section getSection(int sectionY) {
		return sections[sectionY];
	}

	public void setSection(int sectionY, Section section) {
		sections[sectionY] = section;
	}

	public long getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public long getInhabitedTime() {
		return inhabitedTime;
	}

	public void setInhabitedTime(long inhabitedTime) {
		this.inhabitedTime = inhabitedTime;
	}

	public int[] getBiomes() {
		return biomes;
	}

	public void setBiomes(int[] biomes) {
		if (biomes != null && biomes.length != 256) {
			throw new IllegalArgumentException("biomes array must have a length of 256");
		}
		this.biomes = biomes;
	}

	public CompoundTag getHeightMaps() {
		return heightMaps;
	}

	public void setHeightMaps(CompoundTag heightMaps) {
		this.heightMaps = heightMaps;
	}

	public CompoundTag getCarvingMasks() {
		return carvingMasks;
	}

	public void setCarvingMasks(CompoundTag carvingMasks) {
		this.carvingMasks = carvingMasks;
	}

	public ListTag<CompoundTag> getEntities() {
		return entities;
	}

	public void setEntities(ListTag<CompoundTag> entities) {
		this.entities = entities;
	}

	public ListTag<CompoundTag> getTileEntities() {
		return tileEntities;
	}

	public void setTileEntities(ListTag<CompoundTag> tileEntities) {
		this.tileEntities = tileEntities;
	}

	public ListTag<CompoundTag> getTileTicks() {
		return tileTicks;
	}

	public void setTileTicks(ListTag<CompoundTag> tileTicks) {
		this.tileTicks = tileTicks;
	}

	public ListTag<CompoundTag> getLiquidTicks() {
		return liquidTicks;
	}

	public void setLiquidTicks(ListTag<CompoundTag> liquidTicks) {
		this.liquidTicks = liquidTicks;
	}

	public ListTag<ListTag<?>> getLights() {
		return lights;
	}

	public void setLights(ListTag<ListTag<?>> lights) {
		this.lights = lights;
	}

	public ListTag<ListTag<?>> getLiquidsToBeTicked() {
		return liquidsToBeTicked;
	}

	public void setLiquidsToBeTicked(ListTag<ListTag<?>> liquidsToBeTicked) {
		this.liquidsToBeTicked = liquidsToBeTicked;
	}

	public ListTag<ListTag<?>> getToBeTicked() {
		return toBeTicked;
	}

	public void setToBeTicked(ListTag<ListTag<?>> toBeTicked) {
		this.toBeTicked = toBeTicked;
	}

	public ListTag<ListTag<?>> getPostProcessing() {
		return postProcessing;
	}

	public void setPostProcessing(ListTag<ListTag<?>> postProcessing) {
		this.postProcessing = postProcessing;
	}

	public CompoundTag getStructures() {
		return structures;
	}

	public void setStructures(CompoundTag structures) {
		this.structures = structures;
	}

	int getBlockIndex(int blockX, int blockZ) {
		return (blockZ & 0xF) * 16 + (blockX & 0xF);
	}

	public void cleanupPalettesAndBlockStates() {
		for (Section section : sections) {
			if (section != null) {
				section.cleanupPaletteAndBlockStates();
			}
		}
	}

	public static Chunk newChunk() {
		Chunk c = new Chunk(0);
		c.dataVersion = DEFAULT_DATA_VERSION;
		c.data = new CompoundTag();
		c.data.put("Level", new CompoundTag());
		c.status = "mobs_spawned";
		return c;
	}

	public CompoundTag updateHandle(int xPos, int zPos) {
		data.putInt("DataVersion", dataVersion);
		CompoundTag level = data.getCompoundTag("Level");
		level.putInt("xPos", xPos);
		level.putInt("zPos", zPos);
		level.putLong("LastUpdate", lastUpdate);
		level.putLong("InhabitedTime", inhabitedTime);
		if (biomes != null && biomes.length == 256) level.putIntArray("Biomes", biomes);
		if (heightMaps != null) level.put("HeightMaps", heightMaps);
		if (carvingMasks != null) level.put("CarvingMasks", carvingMasks);
		if (entities != null) level.put("Entities", entities);
		if (tileEntities != null) level.put("TileEntities", tileEntities);
		if (tileTicks != null) level.put("TileTicks", tileTicks);
		if (liquidTicks != null) level.put("LiquidTicks", liquidTicks);
		if (lights != null) level.put("Lights", lights);
		if (liquidsToBeTicked != null) level.put("LiquidsToBeTicked", liquidsToBeTicked);
		if (toBeTicked != null) level.put("ToBeTicked", toBeTicked);
		if (postProcessing != null) level.put("PostProcessing", postProcessing);
		level.putString("Status", status);
		if (structures != null) level.put("Structures", structures);
		ListTag<CompoundTag> sections = new ListTag<>(CompoundTag.class);
		for (int i = 0; i < this.sections.length; i++) {
			if (this.sections[i] != null) {
				sections.add(this.sections[i].updateHandle(i));
			}
		}
		level.put("Sections", sections);
		return data;
	}
}
