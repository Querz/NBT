package net.querz.mca.parsers;

import net.querz.NBTTestCase;
import net.querz.mca.MCAFile;
import net.querz.mca.ParserHandler;
import net.querz.nbt.*;
import net.querz.nbt.io.stream.TagSelector;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;

public class TestBiomeParser118 extends NBTTestCase {

	@Test
	public void testHeightmapParser() throws IOException {
		File file = getResourceFile(this, "r.0.0.mca");
		MCAFile mcaFile = new MCAFile(file);
		mcaFile.load(new TagSelector("Heightmaps", CompoundTag.READER), new TagSelector("DataVersion", IntTag.READER));

		System.out.println(NBTUtil.toSNBT(mcaFile.getChunkAt(3, 0).getData(), "\t"));

		HeightmapParser heightmapParser = ParserHandler.getHeightmapParser(mcaFile.getChunkAt(3, 0).getDataVersion(), mcaFile.getChunkAt(3, 0).getData());
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				HeightmapParser.HeightmapData height = heightmapParser.getDataAt(x, z);
				System.out.printf("%d/%d: MOTION_BLOCKING: %d, MOTION_BLOCKING_NO_LEAVES: %d, OCEAN_FLOOR: %d, OCEAN_FLOOR_WG: %d, WORLD_SURFACE: %d, WORLD_SURFACE_WG: %d\n",
						x, z,
						height.getHeight(HeightmapParser.HeightmapType.MOTION_BLOCKING),
						height.getHeight(HeightmapParser.HeightmapType.MOTION_BLOCKING_NO_LEAVES),
						height.getHeight(HeightmapParser.HeightmapType.OCEAN_FLOOR),
						height.getHeight(HeightmapParser.HeightmapType.OCEAN_FLOOR_WG),
						height.getHeight(HeightmapParser.HeightmapType.WORLD_SURFACE),
						height.getHeight(HeightmapParser.HeightmapType.WORLD_SURFACE_WG)
				);
			}
		}
	}
}
