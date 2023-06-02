package net.querz.mca;

import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;

import java.util.Arrays;
import java.util.Map;

import static net.querz.mca.DataVersion.JAVA_1_15_19W36A;
import static net.querz.mca.LoadFlags.*;

/**
 * Represents a REGION data mca chunk. Region chunks are composed of a set of {@link Section} where any empty/null
 * section is filled with air blocks by the game.
 */
public class Chunk extends SectionedChunkBase<Section> {

	/**
	 * The default chunk data version used when no custom version is supplied.
	 * @deprecated Use {@code DataVersion.latest().id()} instead.
	 */
	@Deprecated
	public static final int DEFAULT_DATA_VERSION = DataVersion.latest().id();

	protected long lastUpdate;
	protected long inhabitedTime;
	protected int[] biomes;
	protected CompoundTag heightMaps;
	protected CompoundTag carvingMasks;
	protected ListTag<CompoundTag> entities;  // never populated for versions >= 1.17
	protected ListTag<CompoundTag> tileEntities;
	protected ListTag<CompoundTag> tileTicks;
	protected ListTag<CompoundTag> liquidTicks;
	protected ListTag<ListTag<?>> lights;
	protected ListTag<ListTag<?>> liquidsToBeTicked;
	protected ListTag<ListTag<?>> toBeTicked;
	protected ListTag<ListTag<?>> postProcessing;
	protected String status;
	protected CompoundTag structures;

	Chunk(int lastMCAUpdate) {
		super(lastMCAUpdate);
	}

	/**
	 * Create a new chunk based on raw base data from a region file.
	 * @param data The raw base data to be used.
	 */
	public Chunk(CompoundTag data) {
		super(data);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Section createSection(int sectionY) throws IllegalArgumentException {
		if (containsSection(sectionY)) throw new IllegalArgumentException("section already exists at section-y " + sectionY);
		Section section = createSection();
		putSection(sectionY, section);
		return section;
	}

	@Override
	protected void initReferences(final long loadFlags) {
		CompoundTag level = data.getCompoundTag("Level");
		if (level == null) {
			throw new IllegalArgumentException("data does not contain \"Level\" tag");
		}
		inhabitedTime = level.getLong("InhabitedTime");
		lastUpdate = level.getLong("LastUpdate");
		if ((loadFlags & BIOMES) != 0) {
			biomes = level.getIntArray("Biomes");
			if (biomes.length == 0) biomes = null;
		}
		if ((loadFlags & HEIGHTMAPS) != 0) {
			heightMaps = level.getCompoundTag("Heightmaps");
		}
		if ((loadFlags & CARVING_MASKS) != 0) {
			carvingMasks = level.getCompoundTag("CarvingMasks");
		}
		if ((loadFlags & ENTITIES) != 0) {
			entities = level.containsKey("Entities") ? level.getListTag("Entities").asCompoundTagList() : null;
		}
		if ((loadFlags & TILE_ENTITIES) != 0) {
			tileEntities = level.containsKey("TileEntities") ? level.getListTag("TileEntities").asCompoundTagList() : null;
		}
		if ((loadFlags & TILE_TICKS) != 0) {
			tileTicks = level.containsKey("TileTicks") ? level.getListTag("TileTicks").asCompoundTagList() : null;
		}
		if ((loadFlags & LIQUID_TICKS) != 0) {
			liquidTicks = level.containsKey("LiquidTicks") ? level.getListTag("LiquidTicks").asCompoundTagList() : null;
		}
		if ((loadFlags & LIGHTS) != 0) {
			lights = level.containsKey("Lights") ? level.getListTag("Lights").asListTagList() : null;
		}
		if ((loadFlags & LIQUIDS_TO_BE_TICKED) != 0) {
			liquidsToBeTicked = level.containsKey("LiquidsToBeTicked") ? level.getListTag("LiquidsToBeTicked").asListTagList() : null;
		}
		if ((loadFlags & TO_BE_TICKED) != 0) {
			toBeTicked = level.containsKey("ToBeTicked") ? level.getListTag("ToBeTicked").asListTagList() : null;
		}
		if ((loadFlags & POST_PROCESSING) != 0) {
			postProcessing = level.containsKey("PostProcessing") ? level.getListTag("PostProcessing").asListTagList() : null;
		}
		status = level.getString("Status");
		if ((loadFlags & STRUCTURES) != 0) {
			structures = level.getCompoundTag("Structures");
		}
		if ((loadFlags & (BLOCK_LIGHTS|BLOCK_STATES|SKY_LIGHT)) != 0 && level.containsKey("Sections")) {
			for (CompoundTag section : level.getListTag("Sections").asCompoundTagList()) {
				int sectionIndex = section.getNumber("Y").byteValue();
				Section newSection = new Section(section, dataVersion, loadFlags);
				putSection(sectionIndex, newSection, false);
			}
		}
	}

	/**
	 * May only be used for data versions LT 2203 which includes all of 1.14
	 * and up until 19w36a (a 1.15 weekly snapshot).
	 * @deprecated Use {@link #getBiomeAt(int, int, int)} instead for 1.15 and beyond
	 */
	@Deprecated
	public int getBiomeAt(int blockX, int blockZ) {
		if (dataVersion < JAVA_1_15_19W36A.id()) {
			if (biomes == null || biomes.length != 256) {
				return -1;
			}
			return biomes[getBlockIndex(blockX, blockZ)];
		} else {
			throw new IllegalStateException("cannot get biome using Chunk#getBiomeAt(int,int) from biome data with DataVersion of 2203 or higher (1.15+), use Chunk#getBiomeAt(int,int,int) instead");
		}
	}

	/**
	 * Fetches a biome id at a specific block in this chunk.
	 * The coordinates can be absolute coordinates or relative to the region or chunk.
	 * @param blockX The x-coordinate of the block.
	 * @param blockY The y-coordinate of the block.
	 * @param blockZ The z-coordinate of the block.
	 * @return The biome id or -1 if the biomes are not correctly initialized.
	 */
	public int getBiomeAt(int blockX, int blockY, int blockZ) {
		if (dataVersion >= JAVA_1_15_19W36A.id()) {
			if (biomes == null || biomes.length != 1024) {
				return -1;
			}
			int biomeX = (blockX & 0xF) >> 2;
			int biomeY = (blockY & 0xF) >> 2;
			int biomeZ = (blockZ & 0xF) >> 2;

			return biomes[getBiomeIndex(biomeX, biomeY, biomeZ)];
		} else {
			return getBiomeAt(blockX, blockZ);
		}
	}

	/**
	 * Should only be used for data versions LT 2203 which includes all of 1.14
	 * and up until 19w36a (a 1.15 weekly snapshot).
	 * @deprecated Use {@link #setBiomeAt(int, int, int, int)} instead for 1.15 and beyond
	 */
	@Deprecated
	public void setBiomeAt(int blockX, int blockZ, int biomeID) {
		checkRaw();
		if (dataVersion < JAVA_1_15_19W36A.id()) {
			if (biomes == null || biomes.length != 256) {
				biomes = new int[256];
				Arrays.fill(biomes, -1);
			}
			biomes[getBlockIndex(blockX, blockZ)] = biomeID;
		} else {
			if (biomes == null || biomes.length != 1024) {
				biomes = new int[1024];
				Arrays.fill(biomes, -1);
			}

			int biomeX = (blockX & 0xF) >> 2;
			int biomeZ = (blockZ & 0xF) >> 2;

			for (int y = 0; y < 64; y++) {
				biomes[getBiomeIndex(biomeX, y, biomeZ)] = biomeID;
			}
		}
	}

	 /**
	  * Sets a biome id at a specific block column.
	  * The coordinates can be absolute coordinates or relative to the region or chunk.
	  * @param blockX The x-coordinate of the block column.
	  * @param blockZ The z-coordinate of the block column.
	  * @param biomeID The biome id to be set.
	  *                When set to a negative number, Minecraft will replace it with the block column's default biome.
	  */
	public void setBiomeAt(int blockX, int blockY, int blockZ, int biomeID) {
		checkRaw();
		if (dataVersion >= JAVA_1_15_19W36A.id()) {
			if (biomes == null || biomes.length != 1024) {
				biomes = new int[1024];
				Arrays.fill(biomes, -1);
			}

			int biomeX = (blockX & 0xF) >> 2;
			int biomeZ = (blockZ & 0xF) >> 2;

			biomes[getBiomeIndex(biomeX, blockY, biomeZ)] = biomeID;
		} else {
			if (biomes == null || biomes.length != 256) {
				biomes = new int[256];
				Arrays.fill(biomes, -1);
			}
			biomes[getBlockIndex(blockX, blockZ)] = biomeID;
		}
	}

	int getBiomeIndex(int biomeX, int biomeY, int biomeZ) {
		return biomeY * 16 + biomeZ * 4 + biomeX;
	}

	public CompoundTag getBlockStateAt(int blockX, int blockY, int blockZ) {
		Section section = getSection(MCAUtil.blockToChunk(blockY));
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
		checkRaw();
		int sectionIndex = MCAUtil.blockToChunk(blockY);
		Section section = getSection(sectionIndex);
		if (section == null) {
			putSection(sectionIndex, section = createSection(), false);
			section.setDataVersion(dataVersion);
		}
		section.setBlockStateAt(blockX, blockY, blockZ, state, cleanup);
	}

	/**
	 * Creates a new section appropriately initialized for use inside this chunk.
	 */
	public Section createSection() {
		return new Section(dataVersion);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDataVersion(int dataVersion) {
		super.setDataVersion(dataVersion);
		for (Section section : this) {
			if (section != null) {
				section.setDataVersion(dataVersion);
			}
		}
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
		checkRaw();
		this.status = status;
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
		checkRaw();
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
		checkRaw();
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
	 * @param biomes The biome ID matrix of this chunk. Must have a length of {@code 1024} for 1.15+ or {@code 256}
	 *                  for prior versions.
	 * @throws IllegalArgumentException When the biome matrix is {@code null} or does not have a version appropriate length.
	 */
	public void setBiomes(int[] biomes) {
		checkRaw();
		if (biomes != null) {
			final int requiredSize = dataVersion <= 0 || dataVersion >= JAVA_1_15_19W36A.id() ? 1024 : 256;
			if (biomes.length != requiredSize) {
				throw new IllegalArgumentException("biomes array must have a length of " + requiredSize);
			}
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
		checkRaw();
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
		checkRaw();
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
		checkRaw();
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
		checkRaw();
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
		checkRaw();
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
		checkRaw();
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
		checkRaw();
		this.lights = lights;
	}

	/**
	 * @return The liquids to be ticked in this chunk.
	 */
	public ListTag<ListTag<?>> getLiquidsToBeTicked() {
		return liquidsToBeTicked;
	}

	/**
	 * Sets the liquids to be ticked in this chunk.
	 * @param liquidsToBeTicked The liquids to be ticked.
	 */
	public void setLiquidsToBeTicked(ListTag<ListTag<?>> liquidsToBeTicked) {
		checkRaw();
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
		checkRaw();
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
		checkRaw();
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
		checkRaw();
		this.structures = structures;
	}

	int getBlockIndex(int blockX, int blockZ) {
		return (blockZ & 0xF) * 16 + (blockX & 0xF);
	}

	public void cleanupPalettesAndBlockStates() {
		checkRaw();
		for (Section section : this) {
			if (section != null) {
				section.cleanupPaletteAndBlockStates();
			}
		}
	}

	/**
	 * @deprecated Dangerous - assumes latest full release data version defined by {@link DataVersion}
	 * prefer using {@link MCAFileBase#createChunk()} or {@link MCAFileBase#createChunkIfMissing(int, int)}.
	 */
	@Deprecated
	public static Chunk newChunk() {
		return Chunk.newChunk(DataVersion.latest().id());
	}

	public static Chunk newChunk(int dataVersion) {
		Chunk c = new Chunk(0);
		c.dataVersion = dataVersion;
		c.data = new CompoundTag();
		c.data.put("Level", new CompoundTag());
		c.status = "mobs_spawned";
		return c;
	}

	@Override
	public CompoundTag updateHandle(int xPos, int zPos) {
		if (raw) {
			return data;
		}
		super.updateHandle(xPos, zPos);
		CompoundTag level = data.getCompoundTag("Level");
		level.putInt("xPos", xPos);
		level.putInt("zPos", zPos);
		level.putLong("LastUpdate", lastUpdate);
		level.putLong("InhabitedTime", inhabitedTime);
		if (biomes != null) {
			final int requiredSize = dataVersion <= 0 || dataVersion >= JAVA_1_15_19W36A.id() ? 1024 : 256;
			if (biomes.length != requiredSize)
				throw new IllegalStateException(
						String.format("Biomes array must be %d bytes for version %d, array size is %d",
								requiredSize, dataVersion, biomes.length));
			level.putIntArray("Biomes", biomes);
		}
		level.putIfNotNull("Heightmaps", heightMaps);
		level.putIfNotNull("CarvingMasks", carvingMasks);
		level.putIfNotNull("Entities", entities);
		level.putIfNotNull("TileEntities", tileEntities);
		level.putIfNotNull("TileTicks", tileTicks);
		level.putIfNotNull("LiquidTicks", liquidTicks);
		level.putIfNotNull("Lights", lights);
		level.putIfNotNull("LiquidsToBeTicked", liquidsToBeTicked);
		level.putIfNotNull("ToBeTicked", toBeTicked);
		level.putIfNotNull("PostProcessing", postProcessing);
		level.putString("Status", status);
		level.putIfNotNull("Structures", structures);

		ListTag<CompoundTag> sections = new ListTag<>(CompoundTag.class);
		for (Section section : this) {
			if (section != null) {
				sections.add(section.updateHandle(section.getHeight() /* contract of iterator assures correctness */));
			}
		}
		level.put("Sections", sections);
		return data;
	}
}
