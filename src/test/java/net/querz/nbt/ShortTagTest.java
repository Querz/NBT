package net.querz.nbt;

import java.util.Arrays;

public class ShortTagTest extends NBTTestCase {

	public void testStringConversion() {
		ShortTag t = new ShortTag(Short.MAX_VALUE);
		assertEquals(Short.MAX_VALUE, t.asShort());
		assertEquals(Short.MAX_VALUE, t.asInt());
		assertEquals(Short.MAX_VALUE, t.asLong());
		assertEquals(2, t.getID());
		assertEquals(Short.MAX_VALUE + "s", t.toTagString());
		assertEquals("{\"type\":\"" + t.getClass().getSimpleName() + "\",\"value\":" + Short.MAX_VALUE + "}", t.toString());
	}

	public void testEquals() {
		ShortTag t = new ShortTag(Short.MAX_VALUE);
		ShortTag t2 = new ShortTag(Short.MAX_VALUE);
		assertTrue(t.equals(t2));
		ShortTag t3 = new ShortTag(Short.MIN_VALUE);
		assertFalse(t.equals(t3));
	}

	public void testClone() {
		ShortTag t = new ShortTag(Short.MAX_VALUE);
		ShortTag tc = t.clone();
		assertTrue(t.equals(tc));
		assertFalse(t == tc);
	}

	public void testSerializeDeserialize() {
		ShortTag t = new ShortTag(Short.MAX_VALUE);
		byte[] data = serialize(t);
		assertTrue(Arrays.equals(new byte[]{2, 0, 0, 127, -1}, data));
		ShortTag tt = (ShortTag) deserialize(data);
		assertTrue(t.equals(tt));
	}

	public void testCompareTo() {
		assertEquals(0, new ShortTag((short) 5).compareTo(new ShortTag((short) 5)));
		assertTrue(0 < new ShortTag((short) 7).compareTo(new ShortTag((short) 5)));
		assertTrue(0 > new ShortTag((short) 5).compareTo(new ShortTag((short) 7)));
		assertThrowsRuntimeException(() -> new ShortTag((short) 5).compareTo(null), NullPointerException.class);
	}
}
