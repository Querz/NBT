package net.querz.mca.parsers.impl.anvil118;

import net.querz.mca.parsers.BlockEntityParser;
import net.querz.mca.parsers.BlockLocation;
import net.querz.nbt.CompoundTag;
import net.querz.nbt.ListTag;
import net.querz.nbt.Tag;
import java.util.HashMap;
import java.util.Map;

public class BlockEntityParser118 implements BlockEntityParser {

	private final ListTag blockEntities;
	private Map<BlockLocation, CompoundTag> parsedBlockEntities;
	private boolean forceParse = false;
	private boolean parsedBlockEntitiesChanged = false;

	public BlockEntityParser118(CompoundTag chunk) {
		this.blockEntities = chunk.getList("block_entities");
	}

	@Override
	public CompoundTag getDataAt(int index) {
		return (CompoundTag) blockEntities.get(index);
	}

	@Override
	public void setDataAt(int index, CompoundTag data) {
		blockEntities.set(index, data);
		forceParse = true;
	}

	@Override
	public int getSize() {
		return blockEntities.size();
	}

	@Override
	public CompoundTag getBlockEntityAt(int blockX, int blockY, int blockZ) {
		if (parsedBlockEntities == null || forceParse) {
			parseBlockEntities();
		}
		return parsedBlockEntities.get(new BlockLocation(blockX, blockY, blockZ));
	}

	private void parseBlockEntities() {
		parsedBlockEntities = new HashMap<>();
		for (Tag tag : blockEntities) {
			CompoundTag data = (CompoundTag) tag;
			parsedBlockEntities.put(parseLocation(data), data);
		}
		forceParse = false;
	}

	private BlockLocation parseLocation(CompoundTag data) {
		return new BlockLocation(data.getInt("x"), data.getInt("y"), data.getInt("z"));
	}

	@Override
	public void setBlockEntityAt(int blockX, int blockY, int blockZ, CompoundTag data) {
		parsedBlockEntitiesChanged = true;
		parsedBlockEntities.put(new BlockLocation(blockX, blockY, blockZ), data);
	}

	@Override
	public CompoundTag removeBlockEntityAt(int blockX, int blockY, int blockZ) {
		parsedBlockEntitiesChanged = true;
		return parsedBlockEntities.remove(new BlockLocation(blockX, blockY, blockZ));
	}

	@Override
	public void clear() {
		blockEntities.clear();
		forceParse = true;
	}

	@Override
	public void apply() {
		if (parsedBlockEntitiesChanged) {
			if (forceParse) {
				parseBlockEntities();
			}
			blockEntities.clear();
			for (Map.Entry<BlockLocation, CompoundTag> e : parsedBlockEntities.entrySet()) {
				blockEntities.add(e.getValue());
			}
		}
	}
}
