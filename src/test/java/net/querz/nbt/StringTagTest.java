package net.querz.nbt;

import java.util.Arrays;

public class StringTagTest extends NBTTestCase {

	public void testStringConversion() {
		StringTag t = new StringTag("foo");
		assertEquals("foo", t.getValue());
		assertEquals(8, t.getID());
		assertEquals("foo", t.toTagString());
		assertEquals("{\"type\":\"" + t.getClass().getSimpleName() + "\",\"value\":\"foo\"}", t.toString());
	}

	public void testEquals() {
		StringTag t = new StringTag("foo");
		StringTag t2 = new StringTag("foo");
		assertTrue(t.equals(t2));
		StringTag t3 = new StringTag("something else");
		assertFalse(t.equals(t3));
	}

	public void testClone() {
		StringTag t = new StringTag("foo");
		StringTag tc = t.clone();
		assertTrue(t.equals(tc));
		assertFalse(t == tc);
	}

	public void testSerializeDeserialize() {
		StringTag t = new StringTag("foo");
		byte[] data = serialize(t);
		assertTrue(Arrays.equals(new byte[]{8, 0, 0, 0, 3, 102, 111, 111}, data));
		StringTag tt = (StringTag) deserialize(data);
		assertTrue(t.equals(tt));
	}

	public void testEscape() {
		StringTag allValue = new StringTag("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXZY0123456789_-+");
		assertEquals("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXZY0123456789_-+", allValue.toTagString());
		StringTag escapeValue = new StringTag("öäü");
		assertEquals("\"öäü\"", escapeValue.toTagString());
		StringTag escapeSpecialChars = new StringTag("\\\n\r\t\"");
		assertEquals("\"\\\\\\n\\r\\t\\\"\"", escapeSpecialChars.toTagString());
		StringTag escapeEmpty = new StringTag("");
		assertEquals("\"\"", escapeEmpty.toTagString());

		//no null values allowed
		assertThrowsRuntimeException(() -> new StringTag().setValue(null), NullPointerException.class);
	}

	public void testCompareTo() {
		StringTag t = new StringTag("abc");
		StringTag t2 = new StringTag("abc");
		StringTag t3 = new StringTag("abd");
		assertEquals(0, t.compareTo(t2));
		assertTrue(0 > t.compareTo(t3));
		assertTrue(0 < t3.compareTo(t));
		assertThrowsRuntimeException(() -> t.compareTo(null), NullPointerException.class);
	}
}
