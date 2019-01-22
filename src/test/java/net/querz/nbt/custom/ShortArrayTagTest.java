package net.querz.nbt.custom;

import net.querz.nbt.NBTTestCase;
import java.util.Arrays;
import static org.junit.Assert.assertNotEquals;

public class ShortArrayTagTest extends NBTTestCase {

	public void testStringConversion() {
		ShortArrayTag.register();
		ShortArrayTag t = new ShortArrayTag(new short[]{Short.MIN_VALUE, 0, Short.MAX_VALUE});
		assertTrue(Arrays.equals(new short[]{Short.MIN_VALUE, 0, Short.MAX_VALUE}, t.getValue()));
		assertEquals(100, t.getID());
		assertEquals("[S;-32768s,0s,32767s]", t.toTagString());
		assertEquals("{\"type\":\"" + t.getClass().getSimpleName() + "\",\"value\":[-32768,0,32767]}", t.toString());
	}

	public void testEquals() {
		ShortArrayTag t = new ShortArrayTag(new short[]{Short.MIN_VALUE, 0, Short.MAX_VALUE});
		ShortArrayTag t2 = new ShortArrayTag(new short[]{Short.MIN_VALUE, 0, Short.MAX_VALUE});
		assertTrue(t.equals(t2));
		ShortArrayTag t3 = new ShortArrayTag(new short[]{Short.MAX_VALUE, 0, Short.MIN_VALUE});
		assertFalse(t.equals(t3));
	}

	public void testHashCode() {
		ShortArrayTag t = new ShortArrayTag(new short[]{Short.MIN_VALUE, 0, Short.MAX_VALUE});
		ShortArrayTag t2 = new ShortArrayTag(new short[]{Short.MAX_VALUE, 0, Short.MIN_VALUE});
		assertNotEquals(t.hashCode(), t2.hashCode());
		assertEquals(t.hashCode(), t.clone().hashCode());
	}

	public void testClone() {
		ShortArrayTag t = new ShortArrayTag(new short[]{Short.MIN_VALUE, 0, Short.MAX_VALUE});
		ShortArrayTag tc = t.clone();
		assertTrue(t.equals(tc));
		assertFalse(t == tc);
		assertFalse(t.getValue() == tc.getValue());
	}

	public void testSerializeDeserialize() {
		ShortArrayTag t = new ShortArrayTag(new short[]{Short.MIN_VALUE, 0, Short.MAX_VALUE});
		ShortArrayTag.register();
		byte[] data = serialize(t);
		assertTrue(Arrays.equals(new byte[]{100, 0, 0, 0, 0, 0, 3, -128, 0, 0, 0, 127, -1}, data));
		ShortArrayTag tt = (ShortArrayTag) deserialize(data);
		assertTrue(t.equals(tt));
	}

	public void testCompareTo() {
		ShortArrayTag t = new ShortArrayTag(new short[]{Short.MIN_VALUE, 0, Short.MAX_VALUE});
		ShortArrayTag t2 = new ShortArrayTag(new short[]{Short.MIN_VALUE, 0, Short.MAX_VALUE});
		ShortArrayTag t3 = new ShortArrayTag(new short[]{Short.MAX_VALUE, 0, Short.MIN_VALUE});
		ShortArrayTag t4 = new ShortArrayTag(new short[]{0, Short.MIN_VALUE});
		assertEquals(0, t.compareTo(t2));
		assertEquals(0, t.compareTo(t3));
		assertTrue(0 < t.compareTo(t4));
		assertTrue(0 > t4.compareTo(t));
		assertThrowsRuntimeException(() -> t.compareTo(null), NullPointerException.class);
	}
}
