package net.querz.nbt.mca;

import net.querz.nbt.CompoundTag;
import net.querz.nbt.ListTag;
import net.querz.nbt.NBTTestCase;
import java.io.File;
import java.util.Arrays;

public class MCAFileTest extends NBTTestCase {

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
		Integer x = assertThrowsNoException(() -> MCAUtil.writeMCAFile(tmpFile, mcaFile, true));
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
		assertThrowsNoException(() -> MCAUtil.writeMCAFile(tmpFile, from, true));
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
	}

	private Chunk createChunkWithPos(int x, int z) {
		CompoundTag data = new CompoundTag();
		CompoundTag level = new CompoundTag();
		level.putInt("xPos", x);
		level.putInt("zPos", z);
		data.put("Level", level);
		return new Chunk(data);
	}

	public void testSetters() {
		MCAFile f = new MCAFile(2, 2);

		assertThrowsNoRuntimeException(() -> f.setChunk(0, createChunkWithPos(64, 64)));
		assertEquals(createChunkWithPos(64, 64).toCompoundTag(64, 64), f.getChunk(0).toCompoundTag(64, 64));
		assertThrowsRuntimeException(() -> f.setChunk(1024, createChunkWithPos(64, 64)), IndexOutOfBoundsException.class);
		assertThrowsNoRuntimeException(() -> f.setChunk(1023, createChunkWithPos(64, 64)));
		assertThrowsNoRuntimeException(() -> f.setChunk(0, null));
		assertNull(f.getChunk(0));
		assertThrowsNoRuntimeException(() -> f.setChunk(1023, createChunkWithPos(96, 63)));
		assertThrowsNoRuntimeException(() -> f.setChunk(1023, createChunkWithPos(95, 95)));
	}

	public void testGetBiomeAt() {
		MCAFile f = assertThrowsNoException(() -> MCAUtil.readMCAFile(copyResourceToTmp("r.2.2.mca")));
		assertEquals(21, f.getBiomeAt(1024, 1024));
		assertEquals(-1, f.getBiomeAt(1040, 1024));
		f.setChunk(1, Chunk.newChunk());
		assertEquals(-1, f.getBiomeAt(1024, 1040));
	}

	public void testSetBiomeAt() {
		MCAFile f = assertThrowsNoException(() -> MCAUtil.readMCAFile(copyResourceToTmp("r.2.2.mca")));
		f.setBiomeAt(1024, 1024, 20);
		assertEquals(20, f.getChunk(64, 64).toCompoundTag(64, 64).getCompoundTag("Level").getIntArray("Biomes")[0]);
		f.setBiomeAt(1039, 1039, 47);
		assertEquals(47, f.getChunk(64, 64).toCompoundTag(64, 64).getCompoundTag("Level").getIntArray("Biomes")[255]);
		f.setBiomeAt(1040, 1024, 20);
		int[] biomes = f.getChunk(65, 64).toCompoundTag(65, 64).getCompoundTag("Level").getIntArray("Biomes");
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
		assertEquals(256, s.toCompoundTag(0).getLongArray("BlockStates").length);
		int y = 0;
		for (int i = 11; i <= 17; i++) {
			f.setBlockStateAt(1, y++, 1, block("minecraft:" + i), false);
		}
		assertEquals(17, s.getPalette().size());
		assertEquals(320, s.toCompoundTag(0).getLongArray("BlockStates").length);
		f.cleanupPalettesAndBlockStates();
		assertEquals(17, s.getPalette().size());
		assertEquals(320, s.toCompoundTag(0).getLongArray("BlockStates").length);
		f.setBlockStateAt(1, 0, 1, block("minecraft:bedrock"), false);
		assertEquals(17, s.getPalette().size());
		assertEquals(320, s.toCompoundTag(0).getLongArray("BlockStates").length);
		f.cleanupPalettesAndBlockStates();
		assertEquals(16, s.getPalette().size());
		assertEquals(256, s.toCompoundTag(0).getLongArray("BlockStates").length);
	}

//	public void testSetBlockDataAt() {
//		MCAFile f = assertThrowsNoException(() -> MCAUtil.readMCAFile(copyResourceToTmp("r.2.2.mca")));
//		CompoundTag section = f.getChunk(0, 0).getData().getCompoundTag("Level").getListTag("Sections").asCompoundTagList().get(0);
//		assertEquals(10, section.getListTag("Palette").size());
//		assertEquals(0b0001000100010001000100010001000100010001000100010001000100010001L, section.getLongArray("BlockStates")[0]);
//		f.setBlockStateAt(0, 0, 0, block("minecraft:custom"), false);
//		assertEquals(11, section.getListTag("Palette").size());
//		assertEquals(0b0001000100010001000100010001000100010001000100010001000100011010L, section.getLongArray("BlockStates")[0]);
//
//		//test "line break"
//		int y = 1;
//		for (int i = 12; i <= 17; i++) {
//			f.setBlockStateAt(0, y++, 0, block("minecraft:" + i), false);
//		}
//		assertEquals(17, section.getListTag("Palette").size());
//		assertEquals(320, section.getLongArray("BlockStates").length);
//		assertEquals(0b0001000010000100001000010000100001000010000100001000010000101010L, section.getLongArray("BlockStates")[0]);
//		assertEquals(0b0010000100001000010000100001000010000100001000010000100001000010L, section.getLongArray("BlockStates")[1]);
//		f.setBlockStateAt(12, 0, 0, block("minecraft:18"), false);
//		assertEquals(0b0001000010000100001000010000100001000010000100001000010000101010L, section.getLongArray("BlockStates")[0]);
//		assertEquals(0b0010000100001000010000100001000010000100001000010000100001000011L, section.getLongArray("BlockStates")[1]);
//
//		//test chunkdata == null
//		assertNull(f.getChunk(1, 0));
//		f.setBlockStateAt(17, 0, 0, block("minecraft:test"), false);
//		assertNotNull(f.getChunk(1, 0));
//		ListTag<CompoundTag> s = f.getChunk(1, 0).getData().getCompoundTag("Level").getListTag("Sections").asCompoundTagList();
//		assertEquals(1, s.size());
//		assertEquals(2, s.get(0).getListTag("Palette").size());
//		assertEquals(256, s.get(0).getLongArray("BlockStates").length);
//		assertEquals(0b0000000000000000000000000000000000000000000000000000000000010000L, s.get(0).getLongArray("BlockStates")[0]);
//
//		//test section == null
//		assertNull(f.getChunk(2, 0));
//		Chunk c = Chunk.createDefaultChunk(66, 0);
//		f.setChunk(66, 0, c);
//		assertNotNull(f.getChunk(2, 0));
//		ListTag<CompoundTag> ss = f.getChunk(2, 0).getData().getCompoundTag("Level").getListTag("Sections").asCompoundTagList();
//		assertEquals(0, ss.size());
//		f.setBlockStateAt(33, 0, 0, block("minecraft:air"), false);
//		assertEquals(0, ss.size());
//		f.setBlockStateAt(33, 0, 0, block("minecraft:foo"), false);
//		assertEquals(1, ss.size());
//		assertEquals(2, ss.get(0).getListTag("Palette").size());
//		assertEquals(256, s.get(0).getLongArray("BlockStates").length);
//		assertEquals(0b0000000000000000000000000000000000000000000000000000000000010000L, ss.get(0).getLongArray("BlockStates")[0]);
//
//		//test force cleanup
//		ListTag<CompoundTag> sss = f.getChunk(31, 31).getData().getCompoundTag("Level").getListTag("Sections").asCompoundTagList();
//		assertEquals(12, sss.get(0).getListTag("Palette").size());
//		y = 0;
//		for (int i = 13; i <= 17; i++) {
//			f.setBlockStateAt(1008, y++, 1008, block("minecraft:" + i), false);
//		}
//		f.getChunk(31, 31).cleanupPaletteAndBlockStates(0);
//		assertEquals(17, sss.get(0).getListTag("Palette").size());
//		assertEquals(320, sss.get(0).getLongArray("BlockStates").length);
//		f.setBlockStateAt(1008, 4, 1008, block("minecraft:16"), true);
//		assertEquals(16, sss.get(0).getListTag("Palette").size());
//		assertEquals(256, sss.get(0).getLongArray("BlockStates").length);
//	}

	public void testGetBlockDataAt() {
		MCAFile f = assertThrowsNoException(() -> MCAUtil.readMCAFile(copyResourceToTmp("r.2.2.mca")));
		assertEquals(block("minecraft:bedrock"), f.getBlockStateAt(0, 0, 0));
		assertNull(f.getBlockStateAt(16, 0, 0));
		assertEquals(block("minecraft:dirt"), f.getBlockStateAt(0, 62, 0));
		assertEquals(block("minecraft:dirt"), f.getBlockStateAt(15, 67, 15));
		assertNull(f.getBlockStateAt(3, 100, 3));
	}

	public void test() {
		int regionX = 0, regionZ = -1;


		int x = 3, z = 4;

		int i = z * 16 + x;



		int ix = regionX * 32 + i % 16, iz = regionZ * 32 + i / 16 % 16;


		System.out.println("x: " + ix);
		System.out.println("z: " + iz);
	}

//	public void testGetChunkStatus() {
//		MCAFile f = assertThrowsNoException(() -> MCAUtil.readMCAFile(copyResourceToTmp("r.2.2.mca")));
//		assertEquals("mobs_spawned", f.getChunk(0, 0));
//		assertNull(assertThrowsNoRuntimeException(() -> f.getChunkStatus(1, 0)));
//	}
//
//	public void testSetChunkStatus() {
//		MCAFile f = assertThrowsNoException(() -> MCAUtil.readMCAFile(copyResourceToTmp("r.2.2.mca")));
//		assertThrowsNoRuntimeException(() -> f.setChunkStatus(0, 0, "base"));
//		assertEquals("base", f.getChunkData(0, 0).getCompoundTag("Level").getString("Status"));
//		assertNull(f.getChunkData(1, 0));
//		assertThrowsNoRuntimeException(() -> f.setChunkStatus(1, 0, "test"));
//		assertEquals("test", f.getChunkData(1, 0).getCompoundTag("Level").getString("Status"));
//	}

	private CompoundTag block(String name) {
		CompoundTag c = new CompoundTag();
		c.putString("Name", name);
		return c;
	}

	private CompoundTag getSection(CompoundTag chunk, int y) {
		for (CompoundTag section : chunk.getCompoundTag("Level").getListTag("Sections").asCompoundTagList()) {
			if (section.getByte("Y") == y) {
				return section;
			}
		}
		fail("could not find section");
		return null;
	}

	public static String longToBinaryString(long n) {
		StringBuilder s = new StringBuilder(Long.toBinaryString(n));
		for (int i = s.length(); i < 64; i++) {
			s.insert(0, "0");
		}
		return s.toString();
	}

	public static String intToBinaryString(int n) {
		StringBuilder s = new StringBuilder(Integer.toBinaryString(n));
		for (int i = s.length(); i < 32; i++) {
			s.insert(0, "0");
		}
		return s.toString();
	}
}
