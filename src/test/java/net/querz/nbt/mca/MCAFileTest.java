package net.querz.nbt.mca;

import net.querz.nbt.CompoundTag;
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
		assertEquals(1536157024, assertThrowsNoRuntimeException(() -> f.getLastUpdate(0)).intValue());
		assertEquals(1536157024, assertThrowsNoRuntimeException(() -> f.getLastUpdate(1023)).intValue());
		assertEquals(1536157024, assertThrowsNoRuntimeException(() -> f.getLastUpdate(32, 0)).intValue());
		assertEquals(1536157024, assertThrowsNoRuntimeException(() -> f.getLastUpdate(31, 31)).intValue());
		assertEquals(0, assertThrowsNoRuntimeException(() -> f.getLastUpdate(-1, 0)).intValue());
		assertEquals(0, assertThrowsNoRuntimeException(() -> f.getLastUpdate(31, 0)).intValue());
		assertEquals(1536157024, assertThrowsNoRuntimeException(() -> f.getLastUpdate(0, 32)).intValue());
		assertEquals(0, assertThrowsNoRuntimeException(() -> f.getLastUpdate(0, -1)).intValue());
		assertEquals(0, assertThrowsNoRuntimeException(() -> f.getLastUpdate(0, 31)).intValue());

		assertThrowsRuntimeException(() -> f.getRawDataLength(-1), IndexOutOfBoundsException.class);
		assertThrowsRuntimeException(() -> f.getRawDataLength(1024), IndexOutOfBoundsException.class);
		assertEquals(5277, assertThrowsNoRuntimeException(() -> f.getRawDataLength(0)).intValue());
		assertEquals(4276, assertThrowsNoRuntimeException(() -> f.getRawDataLength(1023)).intValue());
		assertEquals(5277, assertThrowsNoRuntimeException(() -> f.getRawDataLength(32, 0)).intValue());
		assertEquals(4276, assertThrowsNoRuntimeException(() -> f.getRawDataLength(31, 31)).intValue());
		assertEquals(0, assertThrowsNoRuntimeException(() -> f.getRawDataLength(-1, 0)).intValue());
		assertEquals(0, assertThrowsNoRuntimeException(() -> f.getRawDataLength(31, 0)).intValue());
		assertEquals(5277, assertThrowsNoRuntimeException(() -> f.getRawDataLength(0, 32)).intValue());
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
}
