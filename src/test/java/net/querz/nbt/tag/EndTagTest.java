package net.querz.nbt.tag;

import net.querz.NBTTestCase;

public class EndTagTest extends NBTTestCase {

	public void testStringConversion() {
		EndTag e = EndTag.INSTANCE;
		assertEquals(0, e.getID());
		assertNull(e.getValue());
		assertEquals("{\"type\":\"" + e.getClass().getSimpleName() + "\",\"value\":\"end\"}", e.toString());
	}

	public void testClone() {
		assertTrue(EndTag.INSTANCE == EndTag.INSTANCE.clone());
	}
}
