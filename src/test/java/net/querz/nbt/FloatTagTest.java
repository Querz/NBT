package net.querz.nbt;

import static org.junit.Assert.assertNotEquals;
import java.util.Arrays;

public class FloatTagTest extends NBTTestCase {

	public void testStringConversion() {
		FloatTag t = new FloatTag(Float.MAX_VALUE);
		assertEquals(Float.MAX_VALUE, t.asFloat());
		assertEquals(5, t.getID());
		assertEquals(Float.MAX_VALUE + "f", t.toTagString());
		assertEquals("{\"type\":\"" + t.getClass().getSimpleName() + "\",\"value\":" + Float.MAX_VALUE + "}", t.toString());
	}

	public void testEquals() {
		FloatTag t = new FloatTag(Float.MAX_VALUE);
		assertEquals(t, new FloatTag(Float.MAX_VALUE));
		assertNotEquals(t, new FloatTag(Float.MIN_VALUE));
		assertEquals(new FloatTag(Float.NaN), new FloatTag(Float.NaN));
	}

	public void testClone() {
		FloatTag t = new FloatTag(Float.MAX_VALUE);
		FloatTag tc = t.clone();
		assertTrue(t.equals(tc));
		assertFalse(t == tc);
	}

	public void testSerializeDeserialize() {
		FloatTag t = new FloatTag(Float.MAX_VALUE);
		byte[] data = serialize(t);
		assertTrue(Arrays.equals(new byte[]{5, 0, 0, 127, 127, -1, -1}, data));
		FloatTag tt = (FloatTag) deserialize(data);
		assertTrue(t.equals(tt));
	}

	public void testCompareTo() {
		assertEquals(0, new FloatTag(5).compareTo(new FloatTag(5)));
		assertTrue(0 < new FloatTag(7).compareTo(new FloatTag(5)));
		assertTrue(0 > new FloatTag(5).compareTo(new FloatTag(7)));
		assertThrowsRuntimeException(() -> new FloatTag(5).compareTo(null), NullPointerException.class);
	}
}
