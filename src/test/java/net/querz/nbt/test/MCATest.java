package net.querz.nbt.test;

import junit.framework.TestCase;
import net.querz.nbt.mca.MCAFile;
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
		MCAFile mcaFile = new MCAFile(copyResourceToTmp("r.2.2.mca"));
		assertThrowsNoException(mcaFile::deserialize);
		mcaFile.setChunkData(512, null);
		File tmpFile = getNewTmpFile("r.2.2.mca");
		mcaFile.setFile(tmpFile);
		assertThrowsNoException(() -> {
			int x = mcaFile.serialize(true);
			assertEquals(2, x);
		});
		MCAFile again = new MCAFile(tmpFile);
		assertThrowsNoException(again::deserialize);
		for (int i = 0; i < 1024; i++) {
			if (i != 0 && i != 1023) {
				assertNull(again.getChunkData(i));
			} else {
				assertNotNull(again.getChunkData(i));
			}
		}
	}

	public void testChangeLastUpdate() {
		MCAFile from = new MCAFile(copyResourceToTmp("r.2.2.mca"));
		assertThrowsNoException(from::deserialize);
		from.setFile(getNewTmpFile("r.2.2.mca"));
		assertThrowsNoException(() -> from.serialize(true));
		MCAFile to = new MCAFile(from.getFile());
		assertThrowsNoException(to::deserialize);
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
