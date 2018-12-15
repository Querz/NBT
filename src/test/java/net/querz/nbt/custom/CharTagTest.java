package net.querz.nbt.custom;

import net.querz.nbt.NBTTestCase;
import java.util.Arrays;

public class CharTagTest extends NBTTestCase {

	public void testStringConversion() {
		CharTag.register();
		CharTag t = new CharTag('a');
		assertEquals('a', (char) t.getValue());
		assertEquals(110, t.getID());
		assertEquals("a", t.toTagString());
		assertEquals("{\"type\":\"" + t.getClass().getSimpleName() + "\",\"value\":\"a\"}", t.toString());
	}

	public void testEquals() {
		CharTag t = new CharTag('a');
		CharTag t2 = new CharTag('a');
		assertTrue(t.equals(t2));
		CharTag t3 = new CharTag('b');
		assertFalse(t.equals(t3));
	}

	public void testClone() {
		CharTag t = new CharTag('a');
		CharTag tc = t.clone();
		assertTrue(t.equals(tc));
		assertFalse(t == tc);
	}

	public void testSerializeDeserialize() {
		CharTag t = new CharTag('a');
		CharTag.register();
		byte[] data = serialize(t);
		assertTrue(Arrays.equals(new byte[]{110, 0, 0, 0, 97}, data));
		CharTag tt = (CharTag) deserialize(data);
		assertTrue(t.equals(tt));
	}

	public void testCompareTo() {
		assertEquals(0, new CharTag('a').compareTo(new CharTag('a')));
		assertTrue(0 < new CharTag('b').compareTo(new CharTag('a')));
		assertTrue(0 > new CharTag('a').compareTo(new CharTag('b')));

	}
}
