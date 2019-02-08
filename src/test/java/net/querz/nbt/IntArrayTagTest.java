package net.querz.nbt;

import java.util.Arrays;

public class IntArrayTagTest extends NBTTestCase {

	public void testStringConversion() {
		IntArrayTag t = new IntArrayTag(new int[]{Integer.MIN_VALUE, 0, Integer.MAX_VALUE});
		assertTrue(Arrays.equals(new int[]{Integer.MIN_VALUE, 0, Integer.MAX_VALUE}, t.getValue()));
		assertEquals(11, t.getID());
		assertEquals("[I;-2147483648,0,2147483647]", t.toTagString());
		assertEquals("{\"type\":\"" + t.getClass().getSimpleName() + "\",\"value\":[-2147483648,0,2147483647]}", t.toString());
	}

	public void testEquals() {
		IntArrayTag t = new IntArrayTag(new int[]{Integer.MIN_VALUE, 0, Integer.MAX_VALUE});
		IntArrayTag t2 = new IntArrayTag(new int[]{Integer.MIN_VALUE, 0, Integer.MAX_VALUE});
		assertTrue(t.equals(t2));
		IntArrayTag t3 = new IntArrayTag(new int[]{Integer.MAX_VALUE, 0, Integer.MIN_VALUE});
		assertFalse(t.equals(t3));
	}

	public void testClone() {
		IntArrayTag t = new IntArrayTag(new int[]{Integer.MIN_VALUE, 0, Integer.MAX_VALUE});
		IntArrayTag tc = t.clone();
		assertTrue(t.equals(tc));
		assertFalse(t == tc);
		assertFalse(t.getValue() == tc.getValue());
	}

	public void testSerializeDeserialize() {
		IntArrayTag t = new IntArrayTag(new int[]{Integer.MIN_VALUE, 0, Integer.MAX_VALUE});
		byte[] data = serialize(t);
		assertTrue(Arrays.equals(new byte[]{11, 0, 0, 0, 0, 0, 3, -128, 0, 0, 0, 0, 0, 0, 0, 127, -1, -1, -1}, data));
		IntArrayTag tt = (IntArrayTag) deserialize(data);
		assertTrue(t.equals(tt));
	}

	public void testCompareTo() {
		IntArrayTag t = new IntArrayTag(new int[]{Integer.MIN_VALUE, 0, Integer.MAX_VALUE});
		IntArrayTag t2 = new IntArrayTag(new int[]{Integer.MIN_VALUE, 0, Integer.MAX_VALUE});
		IntArrayTag t3 = new IntArrayTag(new int[]{Integer.MAX_VALUE, 0, Integer.MIN_VALUE});
		IntArrayTag t4 = new IntArrayTag(new int[]{0, Integer.MIN_VALUE});
		assertEquals(0, t.compareTo(t2));
		assertEquals(0, t.compareTo(t3));
		assertTrue(0 < t.compareTo(t4));
		assertTrue(0 > t4.compareTo(t));
		assertThrowsRuntimeException(() -> t.compareTo(null), NullPointerException.class);
	}
}
