package net.querz.nbt.test;

import junit.framework.TestCase;
import net.querz.nbt.mca.MCAFile;
import net.querz.nbt.mca.MCAUtil;

import java.io.File;
import static net.querz.nbt.test.TestUtil.*;

public class MCATest extends TestCase {

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

	public void tearDown() throws Exception {
		super.tearDown();
		cleanupTmpDir();
	}
}
