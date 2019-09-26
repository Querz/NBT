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

	/**
	 * Create a new chunk based on raw base data from a region file.
	 * @param data The raw base data to be used.
	 */
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
				int sectionIndex = section.getByte("Y");
				if (sectionIndex > 15 || sectionIndex < 0) {
					continue;
				}
				Section newSection = new Section(section);
				if (newSection.isEmpty()) {
					continue;
				}
				this.sections[sectionIndex] = newSection;
			}
		}
	}

	/**
	 * Serializes this chunk to a <code>RandomAccessFile</code>.
	 * @param raf The RandomAccessFile to be written to.
	 * @param xPos The x-coordinate of the chunk.
	 * @param zPos The z-coodrinate of the chunk.
	 * @return The amount of bytes written to the RandomAccessFile.
	 * @throws IOException When something went wrong during writing.
	 */
	public int serialize(RandomAccessFile raf, int xPos, int zPos) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
		try (DataOutputStream nbtOut = new DataOutputStream(new BufferedOutputStream(CompressionType.ZLIB.compress(baos)))) {
			updateHandle(xPos, zPos).serialize(nbtOut, Tag.DEFAULT_MAX_DEPTH);
		}
		byte[] rawData = baos.toByteArray();
		raf.writeInt(rawData.length);
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
		byte compressionTypeByte = raf.readByte();
		CompressionType compressionType = CompressionType.getFromID(compressionTypeByte);
		if (compressionType == null) {
			throw new IOException("invalid compression type " + compressionTypeByte);
		}
		DataInputStream dis = new DataInputStream(new BufferedInputStream(compressionType.decompress(new FileInputStream(raf.getFD()))));
		Tag<?> tag = Tag.deserialize(dis, Tag.DEFAULT_MAX_DEPTH);
		if (tag instanceof CompoundTag) {
			data = (CompoundTag) tag;
			initReferences();
		} else {
			throw new IOException("invalid data tag: " + (tag == null ? "null" : tag.getClass().getName()));
		}
	}

	/**
	 * Fetches a biome id at a specific block column in this chunk.
	 * The coordinates can be absolute coordinates or relative to the region or chunk.
	 * @param blockX The x-coordinate of the block column.
	 * @param blockZ The z-coordinate of the block column.
	 * @return The biome id or -1 if the biomes are not correctly initialized.
	 */
	public int getBiomeAt(int blockX, int blockZ) {
		if (biomes == null || biomes.length != 256) {
			return -1;
		}
		return biomes[getBlockIndex(blockX, blockZ)];
	}

	/**
	 * Sets a biome id at a specific block column.
	 * The coordinates can be absolute coordinates or relative to the region or chunk.
	 * @param blockX The x-coordinate of the block column.
	 * @param blockZ The z-coordinate of the block column.
	 * @param biomeID The biome id to be set.
	 *                When set to a negative number, Minecraft will replace it with the block column's default biome.
	 */
	public void setBiomeAt(int blockX, int blockZ, int biomeID) {
		if (biomes == null || biomes.length != 256) {
			biomes = new int[256];
			for (int i = 0; i < biomes.length; i++) {
				biomes[i] = -1;
			}
		}
		biomes[getBlockIndex(blockX, blockZ)] = biomeID;
	}

	/**
	 * Fetches the block state at a specific block location in this chunk.
	 * The block coordinate can be absolute or relative to the region or chunk.
	 * @param blockX The x-coordinate of the block.
	 * @param blockY The y-coordinate of the block.
	 * @param blockZ The z-coordinate of the block.
	 * @return The block state or null if a section at the location does not exist.
	 */
	public CompoundTag getBlockStateAt(int blockX, int blockY, int blockZ) {
		Section section = sections[MCAUtil.blockToChunk(blockY)];
		if (section == null) {
			return null;
		}
		return section.getBlockStateAt(blockX, blockY, blockZ);
	}

	/**
	 * Sets a block state at a specific location.
	 * The block coordinates can be absolute or relative to the region or chunk.
	 * @param blockX The x-coordinate of the block.
	 * @param blockY The y-coordinate of the block.
	 * @param blockZ The z-coordinate of the block.
	 * @param state The block state to be set.
	 * @param cleanup When <code>true</code>, it will cleanup all palettes of this chunk.
	 *                This option should only be used moderately to avoid unnecessary recalculation of the palette indices.
	 *                Recalculating the Palette should only be executed once right before saving the Chunk to file.
	 */
	public void setBlockStateAt(int blockX, int blockY, int blockZ, CompoundTag state, boolean cleanup) {
		int sectionIndex = MCAUtil.blockToChunk(blockY);
		Section section = sections[sectionIndex];
		if (section == null) {
			section = sections[sectionIndex] = Section.newSection();
		}
		section.setBlockStateAt(blockX, blockY, blockZ, state, cleanup);
	}

	/**
	 * @return The DataVersion of this chunk.
	 */
	public int getDataVersion() {
		return dataVersion;
	}

	/**
	 * Sets the DataVersion of this chunk. This does not check if the data of this chunk conforms
	 * to that DataVersion, that is the responsibility of the developer.
	 * @param dataVersion The DataVersion to be set.
	 */
	public void setDataVersion(int dataVersion) {
		this.dataVersion = dataVersion;
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
		this.lastMCAUpdate = lastMCAUpdate;
	}

	/**
	 * @return The generation station of this chunk.
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets the generation status of this chunk.
	 * @param status The generation status of this chunk.
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Fetches the section at the given y-coordinate.
	 * @param sectionY The y-coordinate of the section in this chunk ranging from 0 to 15.
	 * @return The Section.
	 */
	public Section getSection(int sectionY) {
		return sections[sectionY];
	}

	/**
	 * Sets a section at a givesn y-coordinate
	 * @param sectionY The y-coordinate of the section in this chunk ranging from 0 to 15.
	 * @param section The section to be set.
	 */
	public void setSection(int sectionY, Section section) {
		sections[sectionY] = section;
	}

	/**
	 * @return The timestamp when this chunk was last updated as a UNIX timestamp.
	 */
	public long getLastUpdate() {
		return lastUpdate;
	}

	/**
	 * Sets the time when this chunk was last updated as a UNIX timestamp.
	 * @param lastUpdate The UNIX timestamp.
	 */
	public void setLastUpdate(long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	/**
	 * @return The cumulative amount of time players have spent in this chunk in ticks.
	 */
	public long getInhabitedTime() {
		return inhabitedTime;
	}

	/**
	 * Sets the cumulative amount of time players have spent in this chunk in ticks.
	 * @param inhabitedTime The time in ticks.
	 */
	public void setInhabitedTime(long inhabitedTime) {
		this.inhabitedTime = inhabitedTime;
	}

	/**
	 * @return A matrix of biome IDs for all block columns in this chunk.
	 */
	public int[] getBiomes() {
		return biomes;
	}

	/**
	 * Sets the biome IDs for this chunk.
	 * @param biomes The biome ID matrix of this chunk. Must have a length of <code>256</code>.
	 * @throws IllegalArgumentException When the biome matrix does not have a length of <code>256</code>
	 *                                  or is <code>null</code>
	 */
	public void setBiomes(int[] biomes) {
		if (biomes != null && biomes.length != 256) {
			throw new IllegalArgumentException("biomes array must have a length of 256");
		}
		this.biomes = biomes;
	}

	/**
	 * @return The height maps of this chunk.
	 */
	public CompoundTag getHeightMaps() {
		return heightMaps;
	}

	/**
	 * Sets the height maps of this chunk.
	 * @param heightMaps The height maps.
	 */
	public void setHeightMaps(CompoundTag heightMaps) {
		this.heightMaps = heightMaps;
	}

	/**
	 * @return The carving masks of this chunk.
	 */
	public CompoundTag getCarvingMasks() {
		return carvingMasks;
	}

	/**
	 * Sets the carving masks of this chunk.
	 * @param carvingMasks The carving masks.
	 */
	public void setCarvingMasks(CompoundTag carvingMasks) {
		this.carvingMasks = carvingMasks;
	}

	/**
	 * @return The entities of this chunk.
	 */
	public ListTag<CompoundTag> getEntities() {
		return entities;
	}

	/**
	 * Sets the entities of this chunk.
	 * @param entities The entities.
	 */
	public void setEntities(ListTag<CompoundTag> entities) {
		this.entities = entities;
	}

	/**
	 * @return The tile entities of this chunk.
	 */
	public ListTag<CompoundTag> getTileEntities() {
		return tileEntities;
	}

	/**
	 * Sets the tile entities of this chunk.
	 * @param tileEntities The tile entities of this chunk.
	 */
	public void setTileEntities(ListTag<CompoundTag> tileEntities) {
		this.tileEntities = tileEntities;
	}

	/**
	 * @return The tile ticks of this chunk.
	 */
	public ListTag<CompoundTag> getTileTicks() {
		return tileTicks;
	}

	/**
	 * Sets the tile ticks of this chunk.
	 * @param tileTicks Thee tile ticks.
	 */
	public void setTileTicks(ListTag<CompoundTag> tileTicks) {
		this.tileTicks = tileTicks;
	}

	/**
	 * @return The liquid ticks of this chunk.
	 */
	public ListTag<CompoundTag> getLiquidTicks() {
		return liquidTicks;
	}

	/**
	 * Sets the liquid ticks of this chunk.
	 * @param liquidTicks The liquid ticks.
	 */
	public void setLiquidTicks(ListTag<CompoundTag> liquidTicks) {
		this.liquidTicks = liquidTicks;
	}

	/**
	 * @return The light sources in this chunk.
	 */
	public ListTag<ListTag<?>> getLights() {
		return lights;
	}

	/**
	 * Sets the light sources in this chunk.
	 * @param lights The light sources.
	 */
	public void setLights(ListTag<ListTag<?>> lights) {
		this.lights = lights;
	}

	/**
	 * @return THe liquids to be ticked in this chunk.
	 */
	public ListTag<ListTag<?>> getLiquidsToBeTicked() {
		return liquidsToBeTicked;
	}

	/**
	 * Sets the liquids to be ticked in this chunk.
	 * @param liquidsToBeTicked The liquids to be ticked.
	 */
	public void setLiquidsToBeTicked(ListTag<ListTag<?>> liquidsToBeTicked) {
		this.liquidsToBeTicked = liquidsToBeTicked;
	}

	/**
	 * @return Stuff to be ticked in this chunk.
	 */
	public ListTag<ListTag<?>> getToBeTicked() {
		return toBeTicked;
	}

	/**
	 * Sets stuff to be ticked in this chunk.
	 * @param toBeTicked The stuff to be ticked.
	 */
	public void setToBeTicked(ListTag<ListTag<?>> toBeTicked) {
		this.toBeTicked = toBeTicked;
	}

	/**
	 * @return Things that are in post processing in this chunk.
	 */
	public ListTag<ListTag<?>> getPostProcessing() {
		return postProcessing;
	}

	/**
	 * Sets things to be post processed in this chunk.
	 * @param postProcessing The things to be post processed.
	 */
	public void setPostProcessing(ListTag<ListTag<?>> postProcessing) {
		this.postProcessing = postProcessing;
	}

	/**
	 * @return Data about structures in this chunk.
	 */
	public CompoundTag getStructures() {
		return structures;
	}

	/**
	 * Sets data about structures in this chunk.
	 * @param structures The data about structures.
	 */
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
