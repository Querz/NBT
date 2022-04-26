package net.querz.mca.parsers;

public interface HeightmapParser extends DataParser<HeightmapParser.HeightmapData>, CachedParser {

	HeightmapData getDataAt(int blockX, int blockZ);

	int getHeightAt(HeightmapType type, int blockX, int blockZ);

	int getHeightAt(HeightmapType type, int index);

	void setHeightAt(HeightmapType type, int blockX, int blockZ, int height);

	void setHeightAt(HeightmapType type, int index, int height);

	@Override
	default int getSize() {
		return 256;
	}

	enum HeightmapType {

		MOTION_BLOCKING("MOTION_BLOCKING", 0),
		MOTION_BLOCKING_NO_LEAVES("MOTION_BLOCKING_NO_LEAVES", 1),
		OCEAN_FLOOR("OCEAN_FLOOR", 2),
		OCEAN_FLOOR_WG("OCEAN_FLOOR_WG", 3),
		WORLD_SURFACE("WORLD_SURFACE", 4),
		WORLD_SURFACE_WG("WORLD_SURFACE_WG", 5);

		private final String name;
		private final int id;

		HeightmapType(String name, int id) {
			this.name = name;
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public int getID() {
			return id;
		}
	}

	record HeightmapData(int[] data) {

		public int getHeight(HeightmapType type) {
			return data[type.getID()];
		}
	}
}
