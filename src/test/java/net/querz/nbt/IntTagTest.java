package net.querz.nbt;

import java.util.Arrays;

public class IntTagTest extends NBTTestCase {

	public void testStringConversion() {
		IntTag t = new IntTag(Integer.MAX_VALUE);
		assertEquals(Integer.MAX_VALUE, t.asInt());
		assertEquals(Integer.MAX_VALUE, t.asLong());
		assertEquals(3, t.getID());
		assertEquals(Integer.MAX_VALUE + "", t.toTagString());
		assertEquals("{\"type\":\"" + t.getClass().getSimpleName() + "\",\"value\":" + Integer.MAX_VALUE + "}", t.toString());
	}

	public void testEquals() {
		IntTag t = new IntTag(Integer.MAX_VALUE);
		IntTag t2 = new IntTag(Integer.MAX_VALUE);
		assertTrue(t.equals(t2));
		IntTag t3 = new IntTag(Integer.MIN_VALUE);
		assertFalse(t.equals(t3));
	}

	public void testClone() {
		IntTag t = new IntTag(Integer.MAX_VALUE);
		IntTag tc = t.clone();
		assertTrue(t.equals(tc));
		assertFalse(t == tc);
	}

	public void testSerializeDeserialize() {
		IntTag t = new IntTag(Integer.MAX_VALUE);
		byte[] data = serialize(t);
		assertTrue(Arrays.equals(new byte[]{3, 0, 0, 127, -1, -1, -1}, data));
		IntTag tt = (IntTag) deserialize(data);
		assertTrue(t.equals(tt));
	}

	public void testCompareTo() {
		assertEquals(0, new IntTag(5).compareTo(new IntTag(5)));
		assertTrue(0 < new IntTag(7).compareTo(new IntTag(5)));
		assertTrue(0 > new IntTag(5).compareTo(new IntTag(7)));
		assertThrowsRuntimeException(() -> new IntTag(5).compareTo(null), NullPointerException.class);
	}
}
