package net.querz.nbt;

import java.util.Arrays;

public class LongArrayTagTest extends NBTTestCase {

	public void testStringConversion() {
		LongArrayTag t = new LongArrayTag(new long[]{Long.MIN_VALUE, 0, Long.MAX_VALUE});
		assertTrue(Arrays.equals(new long[]{Long.MIN_VALUE, 0, Long.MAX_VALUE}, t.getValue()));
		assertEquals(12, t.getID());
		assertEquals("[L;-9223372036854775808l,0l,9223372036854775807l]", t.toTagString());
		assertEquals("{\"type\":\"" + t.getClass().getSimpleName() + "\",\"value\":[-9223372036854775808,0,9223372036854775807]}", t.toString());
	}

	public void testEquals() {
		LongArrayTag t = new LongArrayTag(new long[]{Long.MIN_VALUE, 0, Long.MAX_VALUE});
		LongArrayTag t2 = new LongArrayTag(new long[]{Long.MIN_VALUE, 0, Long.MAX_VALUE});
		assertTrue(t.equals(t2));
		LongArrayTag t3 = new LongArrayTag(new long[]{Long.MAX_VALUE, 0, Long.MIN_VALUE});
		assertFalse(t.equals(t3));
	}

	public void testClone() {
		LongArrayTag t = new LongArrayTag(new long[]{Long.MIN_VALUE, 0, Long.MAX_VALUE});
		LongArrayTag tc = t.clone();
		assertTrue(t.equals(tc));
		assertFalse(t == tc);
		assertFalse(t.getValue() == tc.getValue());
	}

	public void testSerializeDeserialize() {
		LongArrayTag t = new LongArrayTag(new long[]{Long.MIN_VALUE, 0, Long.MAX_VALUE});
		byte[] data = serialize(t);
		assertTrue(Arrays.equals(new byte[]{12, 0, 0, 0, 0, 0, 3, -128, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 127, -1, -1, -1, -1, -1, -1, -1}, data));
		LongArrayTag tt = (LongArrayTag) deserialize(data);
		assertTrue(t.equals(tt));
	}

	public void testCompareTo() {
		LongArrayTag t = new LongArrayTag(new long[]{Long.MIN_VALUE, 0, Long.MAX_VALUE});
		LongArrayTag t2 = new LongArrayTag(new long[]{Long.MIN_VALUE, 0, Long.MAX_VALUE});
		LongArrayTag t3 = new LongArrayTag(new long[]{Long.MAX_VALUE, 0, Long.MIN_VALUE});
		LongArrayTag t4 = new LongArrayTag(new long[]{0, Long.MIN_VALUE});
		assertEquals(0, t.compareTo(t2));
		assertEquals(0, t.compareTo(t3));
		assertTrue(0 < t.compareTo(t4));
		assertTrue(0 > t4.compareTo(t));
		assertThrowsRuntimeException(() -> t.compareTo(null), NullPointerException.class);
	}
}
