package net.querz.nbt.mca;

import net.querz.nbt.CompoundTag;
import net.querz.nbt.ListTag;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

public class MCAFileTest extends MCATestCase {

	public void testGetChunkIndex() {
		assertEquals(0, MCAFile.getChunkIndex(0, 0));
		assertEquals(0, MCAFile.getChunkIndex(32, 32));
		assertEquals(0, MCAFile.getChunkIndex(-32, -32));
		assertEquals(0, MCAFile.getChunkIndex(0, 32));
		assertEquals(0, MCAFile.getChunkIndex(-32, 32));
		assertEquals(1023, MCAFile.getChunkIndex(31, 31));
		assertEquals(1023, MCAFile.getChunkIndex(-1, -1));
		assertEquals(1023, MCAFile.getChunkIndex(63, 63));
		assertEquals(632, MCAFile.getChunkIndex(24, -13));
		int i = 0;
		for (int cz = 0; cz < 32; cz++) {
			for (int cx = 0; cx < 32; cx++) {
				assertEquals(i++, MCAFile.getChunkIndex(cx, cz));
			}
		}
	}

	public void testChangeData() {
		MCAFile mcaFile = assertThrowsNoException(() -> MCAUtil.readMCAFile(copyResourceToTmp("r.2.2.mca")));
		assertNotNull(mcaFile);
		mcaFile.setChunk(0, null);
		File tmpFile = getNewTmpFile("r.2.2.mca");
		Integer x = assertThrowsNoException(() -> MCAUtil.writeMCAFile(mcaFile, tmpFile, true));
		assertNotNull(x);
		assertEquals(2, x.intValue());
		MCAFile again = assertThrowsNoException(() -> MCAUtil.readMCAFile(tmpFile));
		assertNotNull(again);
		for (int i = 0; i < 1024; i++) {
			if (i != 512 && i != 1023) {
				assertNull(again.getChunk(i));
			} else {
				assertNotNull(again.getChunk(i));
			}
		}
	}

	public void testChangeLastUpdate() {
		MCAFile from = assertThrowsNoException(() -> MCAUtil.readMCAFile(copyResourceToTmp("r.2.2.mca")));
		assertNotNull(from);
		File tmpFile = getNewTmpFile("r.2.2.mca");
		assertThrowsNoException(() -> MCAUtil.writeMCAFile(from, tmpFile, true));
		MCAFile to = assertThrowsNoException(() -> MCAUtil.readMCAFile(tmpFile));
		assertNotNull(to);
		assertFalse(from.getChunk(0).getLastMCAUpdate() == to.getChunk(0).getLastMCAUpdate());
		assertFalse(from.getChunk(512).getLastMCAUpdate() == to.getChunk(512).getLastMCAUpdate());
		assertFalse(from.getChunk(1023).getLastMCAUpdate() == to.getChunk(1023).getLastMCAUpdate());
		assertTrue(to.getChunk(0).getLastMCAUpdate() == to.getChunk(512).getLastMCAUpdate());
		assertTrue(to.getChunk(0).getLastMCAUpdate() == to.getChunk(1023).getLastMCAUpdate());
	}

	public void testGetters() {
		MCAFile f = assertThrowsNoException(() -> MCAUtil.readMCAFile(copyResourceToTmp("r.2.2.mca")));
		assertNotNull(f);

		assertThrowsRuntimeException(() -> f.getChunk(-1), IndexOutOfBoundsException.class);
		assertThrowsRuntimeException(() -> f.getChunk(1024), IndexOutOfBoundsException.class);
		assertNotNull(assertThrowsNoRuntimeException(() -> f.getChunk(0)));
		assertNotNull(assertThrowsNoRuntimeException(() -> f.getChunk(1023)));
		assertNotNull(assertThrowsNoRuntimeException(() -> f.getChunk(96, 64)));
		assertNotNull(assertThrowsNoRuntimeException(() -> f.getChunk(95, 95)));
		assertNull(assertThrowsNoRuntimeException(() -> f.getChunk(63, 64)));
		assertNull(assertThrowsNoRuntimeException(() -> f.getChunk(95, 64)));
		assertNotNull(assertThrowsNoRuntimeException(() -> f.getChunk(64, 96)));
		assertNull(assertThrowsNoRuntimeException(() -> f.getChunk(64, 63)));
		assertNull(assertThrowsNoRuntimeException(() -> f.getChunk(64, 95)));
		//not loaded

		MCAFile u = new MCAFile(2, 2);
		assertThrowsRuntimeException(() -> u.getChunk(-1), IndexOutOfBoundsException.class);
		assertThrowsRuntimeException(() -> u.getChunk(1024), IndexOutOfBoundsException.class);
		assertNull(assertThrowsNoRuntimeException(() -> u.getChunk(0)));
		assertNull(assertThrowsNoRuntimeException(() -> u.getChunk(1023)));

		assertEquals(1628, f.getChunk(0).getDataVersion());
		assertEquals(1538048269, f.getChunk(0).getLastMCAUpdate());
		assertEquals(1205486986, f.getChunk(0).getLastUpdate());
		assertNotNull(f.getChunk(0).getBiomes());
		assertNull(f.getChunk(0).getHeightMaps());
		assertNull(f.getChunk(0).getCarvingMasks());
		assertEquals(new ListTag<>(CompoundTag.class), f.getChunk(0).getEntities());
		assertNull(f.getChunk(0).getTileEntities());
		assertNull(f.getChunk(0).getTileTicks());
		assertNull(f.getChunk(0).getLiquidTicks());
		assertNull(f.getChunk(0).getLights());
		assertNull(f.getChunk(0).getLiquidsToBeTicked());
		assertNull(f.getChunk(0).getToBeTicked());
		assertNull(f.getChunk(0).getPostProcessing());
		assertNotNull(f.getChunk(0).getStructures());

		assertNotNull(f.getChunk(0).getSection(0).getSkyLight());
		assertEquals(2048, f.getChunk(0).getSection(0).getSkyLight().length);
		assertNotNull(f.getChunk(0).getSection(0).getBlockLight());
		assertEquals(2048, f.getChunk(0).getSection(0).getBlockLight().length);
		assertNotNull(f.getChunk(0).getSection(0).getBlockStates());
		assertEquals(256, f.getChunk(0).getSection(0).getBlockStates().length);
	}

	private Chunk createChunkWithPos() {
		CompoundTag data = new CompoundTag();
		CompoundTag level = new CompoundTag();
		data.put("Level", level);
		return new Chunk(data);
	}

	public void testSetters() {
		MCAFile f = new MCAFile(2, 2);

		assertThrowsNoRuntimeException(() -> f.setChunk(0, createChunkWithPos()));
		assertEquals(createChunkWithPos().updateHandle(64, 64), f.getChunk(0).updateHandle(64, 64));
		assertThrowsRuntimeException(() -> f.setChunk(1024, createChunkWithPos()), IndexOutOfBoundsException.class);
		assertThrowsNoRuntimeException(() -> f.setChunk(1023, createChunkWithPos()));
		assertThrowsNoRuntimeException(() -> f.setChunk(0, null));
		assertNull(f.getChunk(0));
		assertThrowsNoRuntimeException(() -> f.setChunk(1023, createChunkWithPos()));
		assertThrowsNoRuntimeException(() -> f.setChunk(1023, createChunkWithPos()));

		f.getChunk(1023).setDataVersion(1627);
		assertEquals(1627, f.getChunk(1023).getDataVersion());
		f.getChunk(1023).setLastMCAUpdate(12345678);
		assertEquals(12345678, f.getChunk(1023).getLastMCAUpdate());
		f.getChunk(1023).setLastUpdate(87654321);
		assertEquals(87654321, f.getChunk(1023).getLastUpdate());
		f.getChunk(1023).setInhabitedTime(13243546);
		assertEquals(13243546, f.getChunk(1023).getInhabitedTime());
		assertThrowsRuntimeException(() -> f.getChunk(1023).setBiomes(new int[255]), IllegalArgumentException.class);
		assertThrowsNoRuntimeException(() -> f.getChunk(1023).setBiomes(new int[256]));
		assertTrue(Arrays.equals(new int[256], f.getChunk(1023).getBiomes()));
		f.getChunk(1023).setHeightMaps(getSomeCompoundTag());
		assertEquals(getSomeCompoundTag(), f.getChunk(1023).getHeightMaps());
		f.getChunk(1023).setCarvingMasks(getSomeCompoundTag());
		assertEquals(getSomeCompoundTag(), f.getChunk(1023).getCarvingMasks());
		f.getChunk(1023).setEntities(getSomeCompoundTagList());
		assertEquals(getSomeCompoundTagList(), f.getChunk(1023).getEntities());
		f.getChunk(1023).setTileEntities(getSomeCompoundTagList());
		assertEquals(getSomeCompoundTagList(), f.getChunk(1023).getTileEntities());
		f.getChunk(1023).setTileTicks(getSomeCompoundTagList());
		assertEquals(getSomeCompoundTagList(), f.getChunk(1023).getTileTicks());
		f.getChunk(1023).setLiquidTicks(getSomeCompoundTagList());
		assertEquals(getSomeCompoundTagList(), f.getChunk(1023).getLiquidTicks());
		f.getChunk(1023).setLights(getSomeListTagList());
		assertEquals(getSomeListTagList(), f.getChunk(1023).getLights());
		f.getChunk(1023).setLiquidsToBeTicked(getSomeListTagList());
		assertEquals(getSomeListTagList(), f.getChunk(1023).getLiquidsToBeTicked());
		f.getChunk(1023).setToBeTicked(getSomeListTagList());
		assertEquals(getSomeListTagList(), f.getChunk(1023).getToBeTicked());
		f.getChunk(1023).setPostProcessing(getSomeListTagList());
		assertEquals(getSomeListTagList(), f.getChunk(1023).getPostProcessing());
		f.getChunk(1023).setStructures(getSomeCompoundTag());
		assertEquals(getSomeCompoundTag(), f.getChunk(1023).getStructures());
		Section s = new Section();
		f.getChunk(1023).setSection(0, s);
		assertEquals(s, f.getChunk(1023).getSection(0));
		assertThrowsRuntimeException(() -> f.getChunk(1023).getSection(0).setBlockStates(null), NullPointerException.class);
		assertThrowsRuntimeException(() -> f.getChunk(1023).getSection(0).setBlockStates(new long[321]), IllegalArgumentException.class);
		assertThrowsRuntimeException(() -> f.getChunk(1023).getSection(0).setBlockStates(new long[255]), IllegalArgumentException.class);
		assertThrowsRuntimeException(() -> f.getChunk(1023).getSection(0).setBlockStates(new long[4097]), IllegalArgumentException.class);
		assertThrowsNoRuntimeException(() -> f.getChunk(1023).getSection(0).setBlockStates(new long[320]));
		assertThrowsNoRuntimeException(() -> f.getChunk(1023).getSection(0).setBlockStates(new long[4096]));
		assertThrowsNoRuntimeException(() -> f.getChunk(1023).getSection(0).setBlockStates(new long[256]));
		assertThrowsRuntimeException(() -> f.getChunk(1023).getSection(0).setBlockLight(new byte[2047]), IllegalArgumentException.class);
		assertThrowsRuntimeException(() -> f.getChunk(1023).getSection(0).setBlockLight(new byte[2049]), IllegalArgumentException.class);
		assertThrowsNoRuntimeException(() -> f.getChunk(1023).getSection(0).setBlockLight(new byte[2048]));
		assertThrowsNoRuntimeException(() -> f.getChunk(1023).getSection(0).setBlockLight(null));
		assertThrowsRuntimeException(() -> f.getChunk(1023).getSection(0).setSkyLight(new byte[2047]), IllegalArgumentException.class);
		assertThrowsRuntimeException(() -> f.getChunk(1023).getSection(0).setSkyLight(new byte[2049]), IllegalArgumentException.class);
		assertThrowsNoRuntimeException(() -> f.getChunk(1023).getSection(0).setSkyLight(new byte[2048]));
		assertThrowsNoRuntimeException(() -> f.getChunk(1023).getSection(0).setSkyLight(null));
	}

	public void testGetBiomeAt() {
		MCAFile f = assertThrowsNoException(() -> MCAUtil.readMCAFile(copyResourceToTmp("r.2.2.mca")));
		assertEquals(21, f.getBiomeAt(1024, 1024));
		assertEquals(-1, f.getBiomeAt(1040, 1024));
		f.setChunk(0, 1, Chunk.newChunk());
		assertEquals(-1, f.getBiomeAt(1024, 1040));

	}

	public void testSetBiomeAt() {
		MCAFile f = assertThrowsNoException(() -> MCAUtil.readMCAFile(copyResourceToTmp("r.2.2.mca")));
		f.setBiomeAt(1024, 1024, 20);
		assertEquals(20, f.getChunk(64, 64).updateHandle(64, 64).getCompoundTag("Level").getIntArray("Biomes")[0]);
		f.setBiomeAt(1039, 1039, 47);
		assertEquals(47, f.getChunk(64, 64).updateHandle(64, 64).getCompoundTag("Level").getIntArray("Biomes")[255]);
		f.setBiomeAt(1040, 1024, 20);
		int[] biomes = f.getChunk(65, 64).updateHandle(65, 64).getCompoundTag("Level").getIntArray("Biomes");
		assertEquals(256, biomes.length);
		for (int i = 0; i < 256; i++) {
			assertTrue(i == 0 ? biomes[i] == 20 : biomes[i] == -1);
		}
	}

	public void testCleanupPaletteAndBlockStates() {
		MCAFile f = assertThrowsNoException(() -> MCAUtil.readMCAFile(copyResourceToTmp("r.2.2.mca")));
		assertThrowsNoRuntimeException(f::cleanupPalettesAndBlockStates);
		Chunk c = f.getChunk(0, 0);
		Section s = c.getSection(0);
		assertEquals(10, s.getPalette().size());
		for (int i = 11; i <= 15; i++) {
			s.addToPalette(block("minecraft:" + i));
		}
		assertEquals(15, s.getPalette().size());
		f.cleanupPalettesAndBlockStates();
		assertEquals(10, s.getPalette().size());
		assertEquals(256, s.updateHandle(0).getLongArray("BlockStates").length);
		int y = 0;
		for (int i = 11; i <= 17; i++) {
			f.setBlockStateAt(1, y++, 1, block("minecraft:" + i), false);
		}
		assertEquals(17, s.getPalette().size());
		assertEquals(320, s.updateHandle(0).getLongArray("BlockStates").length);
		f.cleanupPalettesAndBlockStates();
		assertEquals(17, s.getPalette().size());
		assertEquals(320, s.updateHandle(0).getLongArray("BlockStates").length);
		f.setBlockStateAt(1, 0, 1, block("minecraft:bedrock"), false);
		assertEquals(17, s.getPalette().size());
		assertEquals(320, s.updateHandle(0).getLongArray("BlockStates").length);
		f.cleanupPalettesAndBlockStates();
		assertEquals(16, s.getPalette().size());
		assertEquals(256, s.updateHandle(0).getLongArray("BlockStates").length);
	}

	public void testSetBlockDataAt() {
		MCAFile f = assertThrowsNoException(() -> MCAUtil.readMCAFile(copyResourceToTmp("r.2.2.mca")));
		Section section = f.getChunk(0, 0).getSection(0);
		assertEquals(10, section.getPalette().size());
		assertEquals(0b0001000100010001000100010001000100010001000100010001000100010001L, section.getBlockStates()[0]);
		f.setBlockStateAt(0, 0, 0, block("minecraft:custom"), false);
		assertEquals(11, section.getPalette().size());
		assertEquals(0b0001000100010001000100010001000100010001000100010001000100011010L, section.getBlockStates()[0]);

		//test "line break"
		int y = 1;
		for (int i = 12; i <= 17; i++) {
			f.setBlockStateAt(0, y++, 0, block("minecraft:" + i), false);
		}
		assertEquals(17, section.getPalette().size());
		assertEquals(320, section.getBlockStates().length);
		assertEquals(0b0001000010000100001000010000100001000010000100001000010000101010L, section.getBlockStates()[0]);
		assertEquals(0b0010000100001000010000100001000010000100001000010000100001000010L, section.getBlockStates()[1]);
		f.setBlockStateAt(12, 0, 0, block("minecraft:18"), false);
		assertEquals(0b0001000010000100001000010000100001000010000100001000010000101010L, section.getBlockStates()[0]);
		assertEquals(0b0010000100001000010000100001000010000100001000010000100001000011L, section.getBlockStates()[1]);

		//test chunkdata == null
		assertNull(f.getChunk(1, 0));
		f.setBlockStateAt(17, 0, 0, block("minecraft:test"), false);
		assertNotNull(f.getChunk(1, 0));
		ListTag<CompoundTag> s = f.getChunk(1, 0).updateHandle(65, 64).getCompoundTag("Level").getListTag("Sections").asCompoundTagList();
		assertEquals(1, s.size());
		assertEquals(2, s.get(0).getListTag("Palette").size());
		assertEquals(256, s.get(0).getLongArray("BlockStates").length);
		assertEquals(0b0000000000000000000000000000000000000000000000000000000000010000L, s.get(0).getLongArray("BlockStates")[0]);

		//test section == null
		assertNull(f.getChunk(66, 64));
		Chunk c = Chunk.newChunk();
		f.setChunk(66, 64, c);
		assertNotNull(f.getChunk(66, 64));
		ListTag<CompoundTag> ss = f.getChunk(66, 64).updateHandle(66, 64).getCompoundTag("Level").getListTag("Sections").asCompoundTagList();
		assertEquals(0, ss.size());
		f.setBlockStateAt(33, 0, 0, block("minecraft:air"), false);
		ss = f.getChunk(66, 64).updateHandle(66, 64).getCompoundTag("Level").getListTag("Sections").asCompoundTagList();
		assertEquals(1, ss.size());
		f.setBlockStateAt(33, 0, 0, block("minecraft:foo"), false);
		ss = f.getChunk(66, 64).updateHandle(66, 64).getCompoundTag("Level").getListTag("Sections").asCompoundTagList();
		assertEquals(1, ss.size());
		assertEquals(2, ss.get(0).getListTag("Palette").size());
		assertEquals(256, s.get(0).getLongArray("BlockStates").length);
		assertEquals(0b0000000000000000000000000000000000000000000000000000000000010000L, ss.get(0).getLongArray("BlockStates")[0]);

		//test force cleanup
		ListTag<CompoundTag> sss = f.getChunk(31, 31).updateHandle(65, 65).getCompoundTag("Level").getListTag("Sections").asCompoundTagList();
		assertEquals(12, sss.get(0).getListTag("Palette").size());
		y = 0;
		for (int i = 13; i <= 17; i++) {
			f.setBlockStateAt(1008, y++, 1008, block("minecraft:" + i), false);
		}
		f.getChunk(31, 31).getSection(0).cleanupPaletteAndBlockStates();
		sss = f.getChunk(31, 31).updateHandle(65, 65).getCompoundTag("Level").getListTag("Sections").asCompoundTagList();
		assertEquals(17, sss.get(0).getListTag("Palette").size());
		assertEquals(320, sss.get(0).getLongArray("BlockStates").length);
		f.setBlockStateAt(1008, 4, 1008, block("minecraft:16"), true);
		sss = f.getChunk(31, 31).updateHandle(65, 65).getCompoundTag("Level").getListTag("Sections").asCompoundTagList();
		assertEquals(16, sss.get(0).getListTag("Palette").size());
		assertEquals(256, sss.get(0).getLongArray("BlockStates").length);
	}

	public void testGetBlockDataAt() {
		MCAFile f = assertThrowsNoException(() -> MCAUtil.readMCAFile(copyResourceToTmp("r.2.2.mca")));
		assertEquals(block("minecraft:bedrock"), f.getBlockStateAt(0, 0, 0));
		assertNull(f.getBlockStateAt(16, 0, 0));
		assertEquals(block("minecraft:dirt"), f.getBlockStateAt(0, 62, 0));
		assertEquals(block("minecraft:dirt"), f.getBlockStateAt(15, 67, 15));
		assertNull(f.getBlockStateAt(3, 100, 3));
	}

	public void testGetChunkStatus() {
		MCAFile f = assertThrowsNoException(() -> MCAUtil.readMCAFile(copyResourceToTmp("r.2.2.mca")));
		assertEquals("mobs_spawned", f.getChunk(0, 0).getStatus());
	}

	public void testSetChunkStatus() {
		MCAFile f = assertThrowsNoException(() -> MCAUtil.readMCAFile(copyResourceToTmp("r.2.2.mca")));
		assertThrowsNoRuntimeException(() -> f.getChunk(0, 0).setStatus("base"));
		assertEquals("base", f.getChunk(0, 0).updateHandle(64, 64).getCompoundTag("Level").getString("Status"));
		assertNull(f.getChunk(1, 0));
	}

	public void testChunkInitReferences() {
		CompoundTag t = new CompoundTag();
		assertThrowsRuntimeException(() -> new Chunk(null), NullPointerException.class);
		assertThrowsRuntimeException(() -> new Chunk(t), IllegalArgumentException.class);
	}

	public void testChunkInvalidCompressionType() {
		assertThrowsException(() -> {
			try (RandomAccessFile raf = new RandomAccessFile(getResourceFile("invalid_compression.dat"), "r")) {
				Chunk c = new Chunk(0);
				c.deserialize(raf);
			}
		}, IOException.class);
	}

	public void testChunkInvalidDataTag() {
		assertThrowsException(() -> {
			try (RandomAccessFile raf = new RandomAccessFile(getResourceFile("invalid_data_tag.dat"), "r")) {
				Chunk c = new Chunk(0);
				c.deserialize(raf);
			}
		}, IOException.class);
	}
}
