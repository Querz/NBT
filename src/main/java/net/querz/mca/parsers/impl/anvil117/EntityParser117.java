package net.querz.mca.parsers.impl.anvil117;

import net.querz.mca.parsers.EntityLocation;
import net.querz.mca.parsers.EntityParser;
import net.querz.nbt.CompoundTag;
import net.querz.nbt.ListTag;
import net.querz.nbt.Tag;
import java.util.HashMap;
import java.util.Map;

public class EntityParser117 implements EntityParser {

	private final ListTag entities;
	private Map<EntityLocation, CompoundTag> parsedEntities;
	private boolean forceParse = false;
	private boolean parsedEntitiesChanged = false;

	public EntityParser117(CompoundTag chunk) {
		this.entities = chunk.getList("Entities");
	}

	@Override
	public CompoundTag getDataAt(int index) {
		return (CompoundTag) entities.get(index);
	}

	@Override
	public void setDataAt(int index, CompoundTag data) {
		entities.set(index, data);
		forceParse = true;
	}

	@Override
	public int getSize() {
		return entities.size();
	}

	@Override
	public CompoundTag getEntityAt(double x, double y, double z) {
		if (parsedEntities == null || forceParse) {
			parseEntities();
		}
		return parsedEntities.get(new EntityLocation(x, y, z));
	}

	private void parseEntities() {
		parsedEntities = new HashMap<>();
		for (Tag tag : entities) {
			CompoundTag data = (CompoundTag) tag;
			parsedEntities.put(parseLocation(data), data);
		}
		forceParse = false;
	}

	private EntityLocation parseLocation(CompoundTag data) {
		ListTag pos = data.getList("Pos");
		return new EntityLocation(pos.getDouble(0), pos.getDouble(1), pos.getDouble(2));
	}

	@Override
	public void setEntityAt(double x, double y, double z, CompoundTag data) {
		parsedEntitiesChanged = true;
		parsedEntities.put(new EntityLocation(x, y, z), data);
	}

	@Override
	public CompoundTag removeEntityAt(double x, double y, double z) {
		parsedEntitiesChanged = true;
		return parsedEntities.remove(new EntityLocation(x, y, z));
	}

	@Override
	public void clear() {
		entities.clear();
		forceParse = true;
	}

	@Override
	public void apply() {
		if (parsedEntitiesChanged) {
			if (forceParse) {
				parseEntities();
			}
			entities.clear();
			for (Map.Entry<EntityLocation, CompoundTag> e : parsedEntities.entrySet()) {
				entities.add(e.getValue());
			}
		}
	}
}
