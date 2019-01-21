package net.querz.nbt;

public class EndTagTest extends NBTTestCase {

	public void testStringConversion() {
		EndTag e = EndTag.INSTANCE;
		assertEquals(0, e.getID());
		assertNull(e.getValue());
		assertEquals("{\"type\":\"" + e.getClass().getSimpleName() + "\",\"value\":\"end\"}", e.toString());
		assertThrowsRuntimeException(e::toTagString, UnsupportedOperationException.class);
	}

	public void testClone() {
		assertTrue(EndTag.INSTANCE == EndTag.INSTANCE.clone());
	}

	public void testSerializeDeserialize() {
		assertThrowsNoRuntimeException(() -> EndTag.INSTANCE.serializeValue(null, 0));
		assertThrowsNoRuntimeException(() -> EndTag.INSTANCE.deserializeValue(null, 0));
	}
}
