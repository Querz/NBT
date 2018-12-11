package net.querz.nbt.mca;

public class CompressionTypeTest extends MCATestCase {

	public void testGetFromID() {
		assertEquals(CompressionType.NONE, CompressionType.getFromID(CompressionType.NONE.getID()));
		assertEquals(CompressionType.GZIP, CompressionType.getFromID(CompressionType.GZIP.getID()));
		assertEquals(CompressionType.ZLIB, CompressionType.getFromID(CompressionType.ZLIB.getID()));
		assertNull(CompressionType.getFromID((byte) -1));
	}
}
