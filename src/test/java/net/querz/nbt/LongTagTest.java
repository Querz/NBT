package net.querz.nbt;

import java.util.Arrays;

public class LongTagTest extends NBTTestCase {

	public void testStringConversion() {
		LongTag t = new LongTag(Long.MAX_VALUE);
		assertEquals(Long.MAX_VALUE, t.asLong());
		assertEquals(4, t.getID());
		assertEquals(Long.MAX_VALUE + "l", t.toTagString());
		assertEquals("{\"type\":\"" + t.getClass().getSimpleName() + "\",\"value\":" + Long.MAX_VALUE + "}", t.toString());
	}

	public void testEquals() {
		LongTag t = new LongTag(Long.MAX_VALUE);
		LongTag t2 = new LongTag(Long.MAX_VALUE);
		assertTrue(t.equals(t2));
		LongTag t3 = new LongTag(Long.MIN_VALUE);
		assertFalse(t.equals(t3));
	}

	public void testClone() {
		LongTag t = new LongTag(Long.MAX_VALUE);
		LongTag tc = t.clone();
		assertTrue(t.equals(tc));
		assertFalse(t == tc);
	}

	public void testSerializeDeserialize() {
		LongTag t = new LongTag(Long.MAX_VALUE);
		byte[] data = serialize(t);
		assertTrue(Arrays.equals(new byte[]{4, 0, 0, 127, -1, -1, -1, -1, -1, -1, -1}, data));
		LongTag tt = (LongTag) deserialize(data);
		assertTrue(t.equals(tt));
	}

	public void testCompareTo() {
		assertEquals(0, new LongTag(5).compareTo(new LongTag(5)));
		assertTrue(0 < new LongTag(7).compareTo(new LongTag(5)));
		assertTrue(0 > new LongTag(5).compareTo(new LongTag(7)));
		assertThrowsRuntimeException(() -> new LongTag(5).compareTo(null), NullPointerException.class);
	}
}
