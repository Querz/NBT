package net.querz.nbt.mca;

import net.querz.nbt.CompoundTag;
import net.querz.nbt.ListTag;
import net.querz.nbt.NBTTestCase;
import java.io.File;

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
		mcaFile.setChunkData(512, null);
		File tmpFile = getNewTmpFile("r.2.2.mca");
		Integer x = assertThrowsNoException(() -> MCAUtil.writeMCAFile(tmpFile, mcaFile, true));
		assertNotNull(x);
		assertEquals(2, x.intValue());
		MCAFile again = assertThrowsNoException(() -> MCAUtil.readMCAFile(tmpFile));
		assertNotNull(again);
		for (int i = 0; i < 1024; i++) {
			if (i != 0 && i != 1023) {
				assertNull(again.getChunkData(i));
			} else {
				assertNotNull(again.getChunkData(i));
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
		assertFalse(from.getLastUpdate(0) == to.getLastUpdate(0));
		assertFalse(from.getLastUpdate(512) == to.getLastUpdate(512));
		assertFalse(from.getLastUpdate(1023) == to.getLastUpdate(1023));
		assertTrue(to.getLastUpdate(0) == to.getLastUpdate(512));
		assertTrue(to.getLastUpdate(0) == to.getLastUpdate(1023));
	}

	public void testGetters() {
		MCAFile f = assertThrowsNoException(() -> MCAUtil.readMCAFile(copyResourceToTmp("r.2.2.mca")));
		assertNotNull(f);
		assertThrowsRuntimeException(() -> f.getOffset(-1), IndexOutOfBoundsException.class);
		assertThrowsRuntimeException(() -> f.getOffset(1024), IndexOutOfBoundsException.class);
		assertEquals(2, assertThrowsNoRuntimeException(() -> f.getOffset(0)).intValue());
		assertEquals(6, assertThrowsNoRuntimeException(() -> f.getOffset(1023)).intValue());
		assertEquals(2, assertThrowsNoRuntimeException(() -> f.getOffset(32, 0)).intValue());
		assertEquals(6, assertThrowsNoRuntimeException(() -> f.getOffset(31, 31)).intValue());
		assertEquals(0, assertThrowsNoRuntimeException(() -> f.getOffset(-1, 0)).intValue());
		assertEquals(0, assertThrowsNoRuntimeException(() -> f.getOffset(31, 0)).intValue());
		assertEquals(2, assertThrowsNoRuntimeException(() -> f.getOffset(0, 32)).intValue());
		assertEquals(0, assertThrowsNoRuntimeException(() -> f.getOffset(0, -1)).intValue());
		assertEquals(0, assertThrowsNoRuntimeException(() -> f.getOffset(0, 31)).intValue());
		
		assertThrowsRuntimeException(() -> f.getSizeInSectors(-1), IndexOutOfBoundsException.class);
		assertThrowsRuntimeException(() -> f.getSizeInSectors(1024), IndexOutOfBoundsException.class);
		assertEquals(2, assertThrowsNoRuntimeException(() -> f.getSizeInSectors(0)).intValue());
		assertEquals(2, assertThrowsNoRuntimeException(() -> f.getSizeInSectors(1023)).intValue());
		assertEquals(2, assertThrowsNoRuntimeException(() -> f.getSizeInSectors(32, 0)).intValue());
		assertEquals(2, assertThrowsNoRuntimeException(() -> f.getSizeInSectors(31, 31)).intValue());
		assertEquals(0, assertThrowsNoRuntimeException(() -> f.getSizeInSectors(-1, 0)).intValue());
		assertEquals(0, assertThrowsNoRuntimeException(() -> f.getSizeInSectors(31, 0)).intValue());
		assertEquals(2, assertThrowsNoRuntimeException(() -> f.getSizeInSectors(0, 32)).intValue());
		assertEquals(0, assertThrowsNoRuntimeException(() -> f.getSizeInSectors(0, -1)).intValue());
		assertEquals(0, assertThrowsNoRuntimeException(() -> f.getSizeInSectors(0, 31)).intValue());

		assertThrowsRuntimeException(() -> f.getLastUpdate(-1), IndexOutOfBoundsException.class);
		assertThrowsRuntimeException(() -> f.getLastUpdate(1024), IndexOutOfBoundsException.class);
		assertEquals(1538048269, assertThrowsNoRuntimeException(() -> f.getLastUpdate(0)).intValue());
		assertEquals(1538048282, assertThrowsNoRuntimeException(() -> f.getLastUpdate(1023)).intValue());
		assertEquals(1538048269, assertThrowsNoRuntimeException(() -> f.getLastUpdate(32, 0)).intValue());
		assertEquals(1538048282, assertThrowsNoRuntimeException(() -> f.getLastUpdate(31, 31)).intValue());
		assertEquals(0, assertThrowsNoRuntimeException(() -> f.getLastUpdate(-1, 0)).intValue());
		assertEquals(0, assertThrowsNoRuntimeException(() -> f.getLastUpdate(31, 0)).intValue());
		assertEquals(1538048269, assertThrowsNoRuntimeException(() -> f.getLastUpdate(0, 32)).intValue());
		assertEquals(0, assertThrowsNoRuntimeException(() -> f.getLastUpdate(0, -1)).intValue());
		assertEquals(0, assertThrowsNoRuntimeException(() -> f.getLastUpdate(0, 31)).intValue());

		assertThrowsRuntimeException(() -> f.getRawDataLength(-1), IndexOutOfBoundsException.class);
		assertThrowsRuntimeException(() -> f.getRawDataLength(1024), IndexOutOfBoundsException.class);
		assertEquals(6159, assertThrowsNoRuntimeException(() -> f.getRawDataLength(0)).intValue());
		assertEquals(4933, assertThrowsNoRuntimeException(() -> f.getRawDataLength(1023)).intValue());
		assertEquals(6159, assertThrowsNoRuntimeException(() -> f.getRawDataLength(32, 0)).intValue());
		assertEquals(4933, assertThrowsNoRuntimeException(() -> f.getRawDataLength(31, 31)).intValue());
		assertEquals(0, assertThrowsNoRuntimeException(() -> f.getRawDataLength(-1, 0)).intValue());
		assertEquals(0, assertThrowsNoRuntimeException(() -> f.getRawDataLength(31, 0)).intValue());
		assertEquals(6159, assertThrowsNoRuntimeException(() -> f.getRawDataLength(0, 32)).intValue());
		assertEquals(0, assertThrowsNoRuntimeException(() -> f.getRawDataLength(0, -1)).intValue());
		assertEquals(0, assertThrowsNoRuntimeException(() -> f.getRawDataLength(0, 31)).intValue());
		
		assertThrowsRuntimeException(() -> f.getChunkData(-1), IndexOutOfBoundsException.class);
		assertThrowsRuntimeException(() -> f.getChunkData(1024), IndexOutOfBoundsException.class);
		assertNotNull(assertThrowsNoRuntimeException(() -> f.getChunkData(0)));
		assertNotNull(assertThrowsNoRuntimeException(() -> f.getChunkData(1023)));
		assertNotNull(assertThrowsNoRuntimeException(() -> f.getChunkData(32, 0)));
		assertNotNull(assertThrowsNoRuntimeException(() -> f.getChunkData(31, 31)));
		assertNull(assertThrowsNoRuntimeException(() -> f.getChunkData(-1, 0)));
		assertNull(assertThrowsNoRuntimeException(() -> f.getChunkData(31, 0)));
		assertNotNull(assertThrowsNoRuntimeException(() -> f.getChunkData(0, 32)));
		assertNull(assertThrowsNoRuntimeException(() -> f.getChunkData(0, -1)));
		assertNull(assertThrowsNoRuntimeException(() -> f.getChunkData(0, 31)));
		//not loaded
		MCAFile u = new MCAFile();
		assertThrowsRuntimeException(() -> u.getOffset(-1), IndexOutOfBoundsException.class);
		assertThrowsRuntimeException(() -> u.getOffset(1024), IndexOutOfBoundsException.class);
		assertEquals(0, assertThrowsNoRuntimeException(() -> u.getOffset(0)).intValue());
		assertEquals(0, assertThrowsNoRuntimeException(() -> u.getOffset(1023)).intValue());

		assertThrowsRuntimeException(() -> u.getSizeInSectors(-1), IndexOutOfBoundsException.class);
		assertThrowsRuntimeException(() -> u.getSizeInSectors(1024), IndexOutOfBoundsException.class);
		assertEquals(0, assertThrowsNoRuntimeException(() -> u.getSizeInSectors(0)).intValue());
		assertEquals(0, assertThrowsNoRuntimeException(() -> u.getSizeInSectors(1023)).intValue());

		assertThrowsRuntimeException(() -> u.getLastUpdate(-1), IndexOutOfBoundsException.class);
		assertThrowsRuntimeException(() -> u.getLastUpdate(1024), IndexOutOfBoundsException.class);
		assertEquals(0, assertThrowsNoRuntimeException(() -> u.getLastUpdate(0)).intValue());
		assertEquals(0, assertThrowsNoRuntimeException(() -> u.getLastUpdate(1023)).intValue());

		assertThrowsRuntimeException(() -> u.getRawDataLength(-1), IndexOutOfBoundsException.class);
		assertThrowsRuntimeException(() -> u.getRawDataLength(1024), IndexOutOfBoundsException.class);
		assertEquals(0, assertThrowsNoRuntimeException(() -> u.getRawDataLength(0)).intValue());
		assertEquals(0, assertThrowsNoRuntimeException(() -> u.getRawDataLength(1023)).intValue());

		assertThrowsRuntimeException(() -> u.getChunkData(-1), IndexOutOfBoundsException.class);
		assertThrowsRuntimeException(() -> u.getChunkData(1024), IndexOutOfBoundsException.class);
		assertNull(assertThrowsNoRuntimeException(() -> u.getChunkData(0)));
		assertNull(assertThrowsNoRuntimeException(() -> u.getChunkData(1023)));
	}

	public void testSetters() {
		MCAFile f = new MCAFile();
		assertThrowsNoRuntimeException(() -> f.setLastUpdate(0, Integer.MAX_VALUE));
		assertEquals(Integer.MAX_VALUE, f.getLastUpdate(0));
		assertThrowsRuntimeException(() -> f.setLastUpdate(-1, 0), IndexOutOfBoundsException.class);
		assertThrowsRuntimeException(() -> f.setLastUpdate(1024, 0), IndexOutOfBoundsException.class);
		assertThrowsNoRuntimeException(() -> f.setLastUpdate(1023, 0));
		assertThrowsNoRuntimeException(() -> f.setLastUpdate(0, 0, Integer.MIN_VALUE));
		assertEquals(Integer.MIN_VALUE, f.getLastUpdate(0));
		assertThrowsNoRuntimeException(() -> f.setLastUpdate(32, -1, 0));
		assertThrowsNoRuntimeException(() -> f.setLastUpdate(31, 31, 0));

		assertThrowsNoRuntimeException(() -> f.setChunkData(0, new CompoundTag()));
		assertEquals(new CompoundTag(), f.getChunkData(0));
		assertThrowsRuntimeException(() -> f.setChunkData(-1, new CompoundTag()), IndexOutOfBoundsException.class);
		assertThrowsRuntimeException(() -> f.setChunkData(1024, new CompoundTag()), IndexOutOfBoundsException.class);
		assertThrowsNoRuntimeException(() -> f.setChunkData(1023, new CompoundTag()));
		assertThrowsNoRuntimeException(() -> f.setChunkData(0, 0, null));
		assertNull(f.getChunkData(0));
		assertThrowsNoRuntimeException(() -> f.setChunkData(32, -1, new CompoundTag()));
		assertThrowsNoRuntimeException(() -> f.setChunkData(31, 31, new CompoundTag()));
	}

	public void testGetBiomeAt() {
		MCAFile f = assertThrowsNoException(() -> MCAUtil.readMCAFile(copyResourceToTmp("r.2.2.mca")));
		assertEquals(21, f.getBiomeAt(0, 0));
		assertEquals(-1, f.getBiomeAt(16, 0));
		CompoundTag d = f.createDefaultChunk(64, 65);
		f.setChunkData(64, 65, d);
		assertEquals(-1, f.getBiomeAt(0, 16));
	}

	public void testSetBiomeAt() {
		MCAFile f = assertThrowsNoException(() -> MCAUtil.readMCAFile(copyResourceToTmp("r.2.2.mca")));
		f.setBiomeAt(0, 0, 20);
		assertEquals(20, f.getChunkData(0, 0).getCompoundTag("Level").getIntArray("Biomes")[0]);
		f.setBiomeAt(15, 15, 47);
		assertEquals(47, f.getChunkData(0, 0).getCompoundTag("Level").getIntArray("Biomes")[255]);
		f.setBiomeAt(16, 0, 20);
		int[] biomes = f.getChunkData(1, 0).getCompoundTag("Level").getIntArray("Biomes");
		assertEquals(256, biomes.length);
		for (int i = 0; i < 256; i++) {
			assertTrue(i == 0 ? biomes[i] == 20 : biomes[i] == -1);
		}
	}

	public void testCleanupPaletteAndBlockStates() {
		MCAFile f = assertThrowsNoException(() -> MCAUtil.readMCAFile(copyResourceToTmp("r.2.2.mca")));
		assertThrowsNoRuntimeException(() -> f.cleanupPaletteAndBlockStates(1, 0, 0));
		CompoundTag c = f.getChunkData(0, 0);
		CompoundTag s = c.getCompoundTag("Level").getListTag("Sections").asCompoundTagList().get(0);
		assertEquals(10, s.getListTag("Palette").size());
		for (int i = 11; i <= 15; i++) {
			s.getListTag("Palette").asCompoundTagList().add(block("minecraft:" + i));
		}
		assertEquals(15, s.getListTag("Palette").size());
		f.cleanupPaletteAndBlockStates(0, 0, 0);
		assertEquals(10, s.getListTag("Palette").size());
		assertEquals(256, s.getLongArray("BlockStates").length);
		int y = 0;
		for (int i = 11; i <= 17; i++) {
			f.setBlockStateAt(0, y++, 0, block("minecraft:" + i), false);
		}
		assertEquals(17, s.getListTag("Palette").size());
		assertEquals(320, s.getLongArray("BlockStates").length);
		f.cleanupPaletteAndBlockStates(0, 0, 0);
		assertEquals(17, s.getListTag("Palette").size());
		assertEquals(320, s.getLongArray("BlockStates").length);
		f.setBlockStateAt(0, 0, 0, s.getListTag("Palette").asCompoundTagList().get(0), false);
		assertEquals(17, s.getListTag("Palette").size());
		assertEquals(320, s.getLongArray("BlockStates").length);
		f.cleanupPaletteAndBlockStates(0, 0, 0);
		assertEquals(16, s.getListTag("Palette").size());
		assertEquals(256, s.getLongArray("BlockStates").length);
	}

	public void testSetBlockDataAt() {
		MCAFile f = assertThrowsNoException(() -> MCAUtil.readMCAFile(copyResourceToTmp("r.2.2.mca")));
		CompoundTag section = f.getChunkData(0, 0).getCompoundTag("Level").getListTag("Sections").asCompoundTagList().get(0);
		assertEquals(10, section.getListTag("Palette").size());
		assertEquals(0b0001000100010001000100010001000100010001000100010001000100010001L, section.getLongArray("BlockStates")[0]);
		f.setBlockStateAt(0, 0, 0, block("minecraft:custom"), false);
		assertEquals(11, section.getListTag("Palette").size());
		assertEquals(0b0001000100010001000100010001000100010001000100010001000100011010L, section.getLongArray("BlockStates")[0]);

		//test "line break"
		int y = 1;
		for (int i = 12; i <= 17; i++) {
			f.setBlockStateAt(0, y++, 0, block("minecraft:" + i), false);
		}
		assertEquals(17, section.getListTag("Palette").size());
		assertEquals(320, section.getLongArray("BlockStates").length);
		assertEquals(0b0001000010000100001000010000100001000010000100001000010000101010L, section.getLongArray("BlockStates")[0]);
		assertEquals(0b0010000100001000010000100001000010000100001000010000100001000010L, section.getLongArray("BlockStates")[1]);
		f.setBlockStateAt(12, 0, 0, block("minecraft:18"), false);
		assertEquals(0b0001000010000100001000010000100001000010000100001000010000101010L, section.getLongArray("BlockStates")[0]);
		assertEquals(0b0010000100001000010000100001000010000100001000010000100001000011L, section.getLongArray("BlockStates")[1]);

		//test chunkdata == null
		assertNull(f.getChunkData(1, 0));
		f.setBlockStateAt(17, 0, 0, block("minecraft:test"), false);
		assertNotNull(f.getChunkData(1, 0));
		ListTag<CompoundTag> s = f.getChunkData(1, 0).getCompoundTag("Level").getListTag("Sections").asCompoundTagList();
		assertEquals(1, s.size());
		assertEquals(2, s.get(0).getListTag("Palette").size());
		assertEquals(256, s.get(0).getLongArray("BlockStates").length);
		assertEquals(0b0000000000000000000000000000000000000000000000000000000000010000L, s.get(0).getLongArray("BlockStates")[0]);

		//test section == null
		assertNull(f.getChunkData(2, 0));
		CompoundTag c = f.createDefaultChunk(66, 0);
		f.setChunkData(66, 0, c);
		assertNotNull(f.getChunkData(2, 0));
		ListTag<CompoundTag> ss = f.getChunkData(2, 0).getCompoundTag("Level").getListTag("Sections").asCompoundTagList();
		assertEquals(0, ss.size());
		f.setBlockStateAt(33, 0, 0, block("minecraft:air"), false);
		assertEquals(0, ss.size());
		f.setBlockStateAt(33, 0, 0, block("minecraft:foo"), false);
		assertEquals(1, ss.size());
		assertEquals(2, ss.get(0).getListTag("Palette").size());
		assertEquals(256, s.get(0).getLongArray("BlockStates").length);
		assertEquals(0b0000000000000000000000000000000000000000000000000000000000010000L, ss.get(0).getLongArray("BlockStates")[0]);

		//test force cleanup
		ListTag<CompoundTag> sss = f.getChunkData(31, 31).getCompoundTag("Level").getListTag("Sections").asCompoundTagList();
		assertEquals(12, sss.get(0).getListTag("Palette").size());
		y = 0;
		for (int i = 13; i <= 17; i++) {
			f.setBlockStateAt(1008, y++, 1008, block("minecraft:" + i), false);
		}
		f.cleanupPaletteAndBlockStates(31, 0, 31);
		assertEquals(17, sss.get(0).getListTag("Palette").size());
		assertEquals(320, sss.get(0).getLongArray("BlockStates").length);
		f.setBlockStateAt(1008, 4, 1008, block("minecraft:16"), true);
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
		assertEquals("mobs_spawned", f.getChunkStatus(0, 0));
		assertNull(assertThrowsNoRuntimeException(() -> f.getChunkStatus(1, 0)));
	}

	public void testSetChunkStatus() {
		MCAFile f = assertThrowsNoException(() -> MCAUtil.readMCAFile(copyResourceToTmp("r.2.2.mca")));
		assertThrowsNoRuntimeException(() -> f.setChunkStatus(0, 0, "base"));
		assertEquals("base", f.getChunkData(0, 0).getCompoundTag("Level").getString("Status"));
		assertNull(f.getChunkData(1, 0));
		assertThrowsNoRuntimeException(() -> f.setChunkStatus(1, 0, "test"));
		assertEquals("test", f.getChunkData(1, 0).getCompoundTag("Level").getString("Status"));
	}

	private CompoundTag block(String name) {
		CompoundTag c = new CompoundTag();
		c.putString("Name", name);
		return c;
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
