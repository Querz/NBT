package net.querz.mca.parsers;

import java.util.Arrays;

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

	class HeightmapData {

		private final int[] data;

		public HeightmapData(int[] data) {
			this.data = data;
		}

		public int[] data() {
			return data;
		}

		public int getHeight(HeightmapType type) {
			return data[type.getID()];
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			HeightmapData that = (HeightmapData) o;
			return Arrays.equals(data, that.data);
		}

		@Override
		public int hashCode() {
			return Arrays.hashCode(data);
		}

	}
}
